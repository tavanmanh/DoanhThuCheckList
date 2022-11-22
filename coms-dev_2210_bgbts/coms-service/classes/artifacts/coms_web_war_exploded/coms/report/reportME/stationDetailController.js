(function () {
    'use strict';
    var controllerId = 'stationDetailController';

    angular.module('MetronicApp').controller(controllerId, stationDetailController);

    function stationDetailController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter, $modal, $sce, $compile,
                                 kendoConfig, $kWindow, stationDetailService, htmlCommonService, vpsPermissionService,
                                 CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modalStack) {

        var vm = this;
        var today = new Date();
        
        // variables
        vm.String = "Quản lý Cơ điện > Dashboard";
        vm.workingStation = {};
        vm.lstDevice = {};
        vm.label = {};
        $scope.workingStation = [];
        $scope.loggedInUser = $rootScope.casUser.userName;
        $scope.sysUserId = $rootScope.casUser.sysUserId;
        $scope.sysUserGroup = {};
        $scope.enable = {};
        $scope.headerColor = {'background-color': '#33CCCC'};
        
        vm.fileLst = [];
       
        vm.file = {};
        vm.folder='';
        vm.modalOpened = false;
        vm.isCreateNew = false;
        vm.showDetail = false;
        vm.viewPopupDetail = false;
        vm.addForm = {};
        vm.serchDetail = {};
        vm.MESearch = {};
        vm.checkUserKCQTD = false;
        init();
        function init() {
            	$scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
//            	getCheckUserKCQTD($rootScope.casUser.employeeCode);
            	getInforDashboard();
              if ($rootScope.viewDetailStationId) getStationDetails($rootScope.viewDetailStationId);
            
        }
        
        vm.areaDataList = [
        	{name: "" , code: "" },
        	{name: "KV1", code: 1},
        	{name: "KV2", code: 2},
        	{name: "KV3", code: 3},
        ]
        
        vm.provinceOptions = {
                dataTextField: "name",
                dataValueField: "id",
                placeholder: "Nhập mã hoặc tên tỉnh",
                select: function (e) {
                    vm.selectedDept1 = true;
                    var dataItem = this.dataItem(e.item.index());
                    vm.MESearch.catProvince = dataItem.name;
                    vm.MESearch.provinceId = dataItem.catProvinceId;
                },
                pageSize: 10,
                open: function (e) {
                    vm.selectedDept1 = false;
                },
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function (options) {
                            vm.selectedDept1 = false;
                            return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
                                name: vm.MESearch.catProvince,
                                pageSize: vm.provinceOptions.pageSize,
                                page: 1
                            }).then(function (response) {
                                options.success(response.data);
                            }).catch(function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            });
                        }
                    }
                },
                template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
                change: function (e) {
                    if (e.sender.value() === '') {
                        vm.MESearch.catProvince = null;
                        vm.MESearch.provinceId = null;
                    }
                },
                ignoreCase: false
            };
        // ducpm
        vm.userOptions = {
                dataTextField: "fullName",
        		dataValueField: "employeeCode",
        		placeholder: "Nhập mã hoặc tên nhân viên",
	            select: function (e) {
	                vm.selectedDept1 = true;
	                var dataItem = this.dataItem(e.item.index());
	              
	                vm.manageUserCode = dataItem.employeeCode;
                	vm.manageUserName = dataItem.fullName;
	            },
	            pageSize: 10,
	            open: function (e) {
	            },
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function (options) {
	                        return Restangular.all("manageMERsService/doSearchUserSysGroup").post({
	                        	checkUserKCQTD: vm.checkUserKCQTD,
	                            name: vm.MESearch.manageUserCode,
	                            sysGroupId: Constant.userInfo.VpsUserInfo.sysGroupId,
	                            pageSize: 10,
	                            page: 1
	                        }).then(function (response) {
	                            options.success(response.data);
	                        }).catch(function (err) {
	                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
	                        });
	                    }
	                }
	            },
	            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.employeeCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
	            change: function (e) {
	                if (e.sender.value() === '') {
	                	vm.manageUserCode = null;
	                	vm.manageUserName = null;
	                }
	            },
	            ignoreCase: false
        }
        
        vm.cancelInput = function(param) {
			if(param === 'user'){
				vm.manageUserCode = null;
				vm.manageUserName = null;
			}
			if(param === 'province' ){
				 vm.MESearch.catProvince = null;
                 vm.MESearch.provinceId = null;
			}
		}
        //ducpm end
//        function getCheckUserKCQTD(employeeCode) {
//        	stationDetailService.checkUserKCQTD(employeeCode).then(function(result){
//        			vm.checkUserKCQTD = result;
//    	  });
//		}
        function getInforDashboard() {
        	stationDetailService.checkUserKCQTD($rootScope.casUser.employeeCode).then(function(result){
    			vm.checkUserKCQTD = result;
    			var obj = {
            			checkUserKCQTD: vm.checkUserKCQTD,
            			sysGroupId: $rootScope.authenticatedUser.VpsUserInfo.sysGroupId
            	};
            	stationDetailService.getInforDashboard(obj).then(function(result){
            		if(result){
            			vm.elictric = result;
        			}else toast.error("Chưa có dữ liệu cấu hình loại thiết bị")
        	  });
        	});
		}
        
        vm.doSearch = function() {
        	vm.MESearch.checkUserKCQTD = vm.checkUserKCQTD;
        	vm.MESearch.sysGroupId = $rootScope.authenticatedUser.VpsUserInfo.sysGroupId
        	stationDetailService.getInforDashboard(vm.MESearch).then(function(result){
        		if(result){
        			vm.elictric = result;
    			}else toast.error("Chưa có dữ liệu cấu hình loại thiết bị")
    	  });
		}

        vm.click = function(typeClick) {
        	var teamplateUrl = "coms/report/reportME/detailPopup.html";
            var title = "Chi tiết trạm";
            var windowId = "DEVICE";
            var array=[3,4,5,6,8,10,11,13,18,23,24,25];
            if(typeClick == 1 || typeClick ==2){
            	fillDataTable(typeClick);
            }
            if(array.includes(typeClick)){
            	fillDataTableStation(typeClick);
            }
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1100', '500', null);
		}
        
       var record =0;
       function fillDataTable(type) {
    	   if(type == 1){
    		   vm.type = '1';
    	   }
    	   if(type == 2){
    		   vm.type = '2';
    	   }
            vm.dataWOListTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                save: function () {
                    vm.dataWOListTable.dataSource.read();
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            vm.count = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            $scope.currentPageWoList = response.data;
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            //Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "manageMERsService/wo/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                        	vm.serchDetail.page = options.page
                            vm.serchDetail.pageSize = options.pageSize
                            vm.serchDetail.type = vm.type
                            vm.serchDetail.checkUserKCQTD = vm.checkUserKCQTD
                            vm.serchDetail.sysGroupId = $rootScope.authenticatedUser.VpsUserInfo.sysGroupId
                            vm.serchDetail.manageUserCode = vm.MESearch.manageUserCode
                            vm.serchDetail.provinceId = vm.MESearch.provinceId
                            vm.serchDetail.areaCode = vm.MESearch.areaCode
                            vm.serchDetail.stationCode = vm.MESearch.stationCode
                            return JSON.stringify(vm.serchDetail)
                        }
                    },
                    pageSize: 10,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: true,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: ''
                        // empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '10%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Tên WO",
                        field: 'nameWo',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.woName);
                        },
                    },
                    {
                        title: "Mã Wo",
                        field: 'codeWo',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.woCode);
                        },
                    },
                    {
                        title: "Mã Tr",
                        field: "trCode",
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.trCode);
                        },
                        hidden: true,
                    },
                    {
                        title: "Nguồn việc",
                        field: 'sourceWork',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return htmlCommonService.apWorkSrcShortName(valueOrEmptyStr(dataItem.apWorkSrcName));
                        }
                    },
                    //Huypq-01092020-start
                    {
                        title: "Hợp đồng",
                        field: 'contractCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            if (dataItem.hcqtContractCode) return valueOrEmptyStr(dataItem.hcqtContractCode);
                            else return valueOrEmptyStr(dataItem.contractCode);
                        }
                    },
                    {
                        title: "Mã trạm",
                        field: 'stationCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Công trình",
                        field: 'constructionCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    //huy-end
                    {
                        title: "Giá trị (Triệu VND)",
                        field: 'moneyValue',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            if (!dataItem.moneyValue || dataItem.moneyValue == 0) return 0;
                            var moneyM = dataItem.moneyValue / 1000000;
                            moneyM = moneyM.toFixed(3);
                            moneyM = CommonService.removeTrailingZero(moneyM);
                            return !!CommonService.numberWithCommas(moneyM) ? CommonService.numberWithCommas(moneyM) : 0;
                        }
                    },{
                        title: "Hạng mục",
                        field: 'itemName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Tên FT",
                        field: 'ftName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Email FT",
                        field: 'ftEmailSysUser',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Chức vụ FT",
                        field: 'ftPositionName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Trạng thái",
                        field: 'status',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                        	var txt = '';
                        	if(dataItem.opinionType!=null && dataItem.state!='PAUSE'){
                        		txt = CommonService.getStateText(dataItem.stateVuong);
                        	} else {
                        		txt = CommonService.getStateText(dataItem.state);
                                if (
                                    dataItem.state == Constant.WO_XL_STATE.opinionRq1.stateCode ||
                                    dataItem.state == Constant.WO_XL_STATE.opinionRq2.stateCode ||
                                    dataItem.state == Constant.WO_XL_STATE.opinionRq3.stateCode ||
                                    dataItem.state == Constant.WO_XL_STATE.opinionRq4.stateCode
                                ) {
                                    if (dataItem.opinionResult != '' && dataItem.opinionResult == 'ACCEPTED') txt += ' - Đã chấp thuận ';
                                    else if (dataItem.opinionResult != '' && dataItem.opinionResult == 'REJECTED') txt += ' - Đã từ chối ';
                                }
                        	}
                            return txt;
                        }
                    },
                    {
                    	title : "Việc chưa hoàn thành",
						field : 'jobUnfinished',
						width : '20%',
						headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
                        attributes: { style: "text-align:left;"},
                        hidden: true,
                    },
                    {
                        title: "Hạn hoàn thành",
                        field: 'dateComplete',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.finishDate);
                        }
                    },
                ]
            });
        }
        var record = 0;
        function fillDataTableStation(type) {
        	vm.type = type;
            vm.dataWOListTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            vm.count = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            $scope.currentPageWoList = response.data;
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            //Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "manageMERsService/station/doSearchDetail",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                        	vm.serchDetail.page = options.page
                            vm.serchDetail.pageSize = options.pageSize
                            vm.serchDetail.type = vm.type
                            vm.serchDetail.checkUserKCQTD = vm.checkUserKCQTD
                            vm.serchDetail.sysGroupId = $rootScope.authenticatedUser.VpsUserInfo.sysGroupId
                            vm.serchDetail.manageUserCode = vm.MESearch.manageUserCode
                            vm.serchDetail.provinceId = vm.MESearch.provinceId
                            vm.serchDetail.areaCode = vm.MESearch.areaCode
                            vm.serchDetail.stationCode = vm.MESearch.stationCode
                            return JSON.stringify(vm.serchDetail)
                        }
                    },
                    pageSize: 10,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: true,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: ''
                    }
                },
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '10%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Mã trạm",
                        field: 'stationCode',
                        width: '20',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Tỉnh",
                        field: 'provinceCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Khu vực",
                        field: 'areaCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Địa chỉ",
                        field: 'address',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Vị trí",
                        field: 'location',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                ]
            });
        }
        
        vm.cancel = cancel;
        function cancel() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }
        
        function valueOrEmptyStr(value) {
            return value ? value : '';
        }

        
        vm.exportFile= function(){
	    	vm.serchDetail.page = null;
			vm.serchDetail.pageSize = null;
			 var array=[3,4,5,6,8,10,11,13,18,23,24,25];
	            if(array.includes(vm.type)){
	            	kendo.ui.progress($("#dataWOListTable"), true);
					var obj = {};
					obj = angular.copy(vm.serchDetail);
					obj.type= vm.type;
					stationDetailService.exportWo1(obj).then(function(d){
						CommonService.exportFile(vm.dataWOListTable,d.data,[],[],"Báo cáo thông tin trạm");
						kendo.ui.progress($("#dataWOListTable"), false);
				});
	            }
				if(vm.type == '1' || vm.type == '2' ){
						kendo.ui.progress($("#dataWOListTable"), true);
						var obj = {};
						obj = angular.copy(vm.serchDetail);
						obj.type= vm.type;
						stationDetailService.exportWo(obj).then(function(d){
							CommonService.exportFile(vm.dataWOListTable,d.data,[],[],"Báo cáo WO");
							kendo.ui.progress($("#dataWOListTable"), false);
					});
		    	}
        }
        
        
        
    }
})();
