 (function () {
    'use strict';
    var controllerId = 'manageMEController';

    angular.module('MetronicApp').controller(controllerId, manageMEController);

    function manageMEController($scope, $rootScope, $timeout, gettextCatalog, $filter,
        kendoConfig, $kWindow, manageMEService, $http,
        CommonService, PopupConst, Restangular, RestEndpoint, Constant, htmlCommonService) {
        var vm = this;
        vm.countCons = 0;
        vm.showSearch = true;
        vm.isCreateNew = true;
        vm.showDetail = false;
        vm.checkRoleCD = false;
		vm.provinceId = null;
       
    	var start = new Date();
    	start.setMonth(start.getMonth() - 6);
    	var end = new Date();
    	
        vm.MESearch = {
        	status:1
        };

        vm.stationTypeDataList = [
        	{name: "Trên mái", code: 1}, 
        	{name: "Dưới đất", code: 2}
        ];
        
        vm.areaDataList = [
        	{name: "KV1", code: 1}, 
        	{name: "KV2", code: 2},
        	{name: "KV3", code: 3},
        ];
             
        vm.cancel = cancel;
        function cancel() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }
        
        vm.fileLst = [];
       
        vm.checkB2BB2C = false; 
        vm.String = "Quản lý WO > Quản lý cơ điện";
        
        vm.allowAction = false;
        vm.openCatProvincePopup = openCatProvincePopup;
        vm.onSaveCatProvince = onSaveCatProvince;
        

        // Khoi tao du lieu cho form
        vm.iconDiss = true;
        vm.hidenIcon = hidenIcon;
        function hidenIcon() {
            vm.iconDiss = false;
        }
        vm.showIcon = function () {
            vm.iconDiss = true;
        }
        
        initFormData();
        
        function initFormData() {
        	checkRoleCD();
        	fillDataTable([]);
        }
        
        $scope.changeImage = changeImage;
        $scope.imageSelected = {};
        
        function changeImage(image) {
            $scope.imageSelected = image;
        }

       
//        function validate() {
//            var isValid = true;
//            if (vm.totalMonthPlan.year == undefined || vm.totalMonthPlan.year == '') {
//                toastr.warning("Năm không được để trống!");
//                return false;
//            }
//            if (vm.totalMonthPlan.month == undefined || vm.totalMonthPlan.month == '') {
//                toastr.warning("Tháng không được để trống!");
//                return false;
//            }
//            if (vm.totalMonthPlan.code == undefined || vm.totalMonthPlan.code == '') {
//                toastr.warning("Mã kế hoạch không được để trống!");
//                return false;
//            }
//            if (vm.totalMonthPlan.name == undefined || vm.totalMonthPlan.name == '') {
//                toastr.warning("Tên kế hoạch không được để trống!");
//                return false;
//            }
//            return isValid;
//        }

        vm.cancelImport = cancelImport;
        function cancelImport() {
            CommonService.dismissPopup1();
        }

        var record = 0;
        function fillDataTable(data) {
        	kendo.ui.progress($("#manageMEGrid"), true);
        	vm.manageMEGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: true,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                toolbar: [
                    {
                        name: "actions",
                        template: 
                            '<div class="btn-group pull-right margin_top_button margin10">' +
                            '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                            '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                            '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                            '<label ng-repeat="column in vm.manageMEGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                            '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                            '</label>' +
                            '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $timeout( function(){ 
                            	vm.countCons = response.total;
                            } );
                            return response.total;
                        },
                        data: function (response) {
                        	kendo.ui.progress($("#manageMEGrid"), false);
                        	return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "manageMERsService/station/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.MESearch.page = options.page
                            vm.MESearch.pageSize = options.pageSize
                            return JSON.stringify(vm.MESearch)

                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: false,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
                columns: [
                    {
                        title: "TT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: 10,
                        columnMenu: false,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Mã trạm",
                        field: 'stationCode',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(dataItem){
							return '<a href="javascript:void(0);" ng-click="vm.showDetailStation(dataItem)">' + dataItem.stationCode + '</a>';
						}
                    },
                    {
                        title: "Mã trạm VTNet",
                        field: 'stationCodeVtnet',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Khu vực",
                        field: 'areaCode',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tên NVQL",
                        field: 'manageUserName',
                        width: 30,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Loại trạm",
                        field: 'stationType',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Ngày PS",
                        field: 'formedAssetDate',
                        width: 30,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        },
//                        template: function(dataItem) {
//                        	return dataItem.broadcastingDate.toLocaleDateString("en-US")
//                        }
                    },
                    {
                        title: "Lần BD gần nhất",
                        field: 'lastMaintenance',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Thao tác",
                        field: 'lastMaintenance',
                        width: '5%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function(dataItem){
							return '<i ng-show="vm.checkRoleCD && vm.provinceId == dataItem.provinceId" class="fa fa-arrow-right" href="javascript:void(0);" uib-tooltip="Điều phối" ng-click="vm.directional(dataItem)"></i>';
						}
                    }
                ]
            });
        }

        vm.showDetailStation = function (dataItem) {
        	
        	removeTab("Chi tiết trạm cơ điện"); 
        	
            var template = Constant.getTemplateUrl('STATION_ELECTRICAL_DETAIL');
            $rootScope.viewDetailStationId = dataItem.stationId;
            $rootScope.workingStation = dataItem;
//          template.stationId = dataItem.stationId;
            postal.publish({
                channel: "Tab",
                topic: "open",
                data: template
            });

            postal.publish({
                channel: "Tab",
                topic: "action",
                data: {action: 'refresh', stationId: dataItem.stationId}
            });
        };
        
        function removeTab (title) {
            for(var i=0; i<$scope.tabs.length; i++){
            	if($scope.tabs[i].title==title){
            		$scope.tabs.splice(i, 1);
            		break;
            	}
            }
		};

        vm.listRemove = [{
            title: "Thao tác"
        }]
        vm.listConvert = [
        	{
	            field: "status",
	            data: {
	                1: 'Hiệu lực',
	                0: 'Hết Hiệu lực'
	            }
        	}
        ]
        function refreshGrid(d) {
            var grid = vm.constructionGrid;
            if (grid) {
                grid.dataSource.data(d);
                grid.refresh();
            }
        }
       
        vm.cancelDoSearch = function () {
            vm.showDetail = false;
            vm.MESearch = {
                status: "1"
            };
            doSearch();
        };

        vm.doSearch = doSearch;
        function doSearch() {
            vm.showDetail = false;
            var grid = vm.manageMEGrid;
            console.log()
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        }


        vm.showHideColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.manageMEGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.manageMEGrid.showColumn(column);
            } else {
                vm.manageMEGrid.hideColumn(column);
            }


        }
       
        vm.gridColumnShowHideFilter = function (item) {
            return item.type == null || item.type !== 1;
        };

        vm.cancelInput = function (param, form) {
        	if (param == "province") {
                vm.MESearch.provinceId = null;
                vm.MESearch.catProvince = null;
             }
        }

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

        
//        vm.getExcelTemplate = function () {
//            constructionService.downloadTemplate({}).then(function (d) {
//                var data = d.plain();
//                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
//            }).catch(function () {
//                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
//                return;
//            });
//        };



        vm.cancelPopup = cancelPopup;
        function cancelPopup() {
            $rootScope.$broadcast("handlerReturnFromPopup", "ok");
            $(".k-icon.k-i-close").click();
            kendo.ui.progress($("#constructionGrid"), false);
        }
        
        vm.onSelect = function (e) {
            if ($("#files")[0].files[0].name.split('.').pop() != 'xls' && $("#files")[0].files[0].name.split('.').pop() != 'xlsx') {
                toastr.warning("Sai định dạng file");
                setTimeout(function () {
                    $(".k-upload-files.k-reset").find("li").remove();
                    $(".k-upload-files").remove();
                    $(".k-upload-status").remove();
                    $(".k-upload.k-header").addClass("k-upload-empty");
                    $(".k-upload-button").removeClass("k-state-focused");
                }, 10);
                return;
            }
            else {
                $("#fileName").innerHTML = $("#files")[0].files[0].name
            }
        };

        function sendFile(id, callback) {
            var files = $("#" + id)[0].files;
            if (!$("#" + id)[0].files[0]) {
                toastr.warning("Bạn chưa chọn file");
                return;
            }
            if (!htmlCommonService.checkFileExtension(id)) {
                toastr.warning("File không đúng định dạng cho phép");
                return;
            }
            var formData = new FormData();
            jQuery.each($("#" + id)[0].files, function (i, file) {
            	console.log(file);
                formData.append('multipartFile' + i, file);
            });
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "fileservice/uploadATTTInput",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    callback(data, files)
                }
            });
        }
        // callback sau khi upload file thành công
        vm.callback = callback;
        function callback(data, files) {
            for (var i = 0; i < data.length; i++) {
                var file = {};
                file.name = files[i].name;
                file.createdDate = htmlCommonService.getFullDate();
                file.createdUserName = Constant.userInfo.vpsUserToken.fullName;
                file.createdUserId = Constant.userInfo.vpsUserToken.sysUserId;
                file.filePath = data[i];
                file.type = 0;
                vm.fileLst.push(file);
            }
            var grid = !!vm.contractFileGrid ? vm.contractFileGrid : vm.contractFileEditGrid;
            refreshGrid(grid, vm.fileLst);
            //      	vm.fileLst = $("#contractFileGrid").data("kendoGrid").dataSource.data();
            vm.fileLst = grid.dataSource.data();
            $('#attachfiles').val(null);
            $('#attachfilesEdit').val(null);
        }


        vm.submitAttachFile = submitAttachFile;
        function submitAttachFile() {
            sendFile("attachfiles", callback);
        }
        vm.submitAttachFileEdit = submitAttachFileEdit;
        function submitAttachFileEdit() {
            sendFile("attachfilesEdit", callback);
        }
        function refreshGrid(grid, data) {
            grid.dataSource.data(data);
            grid.refresh();
        }
        
        
        
        vm.downloadFile = function (dataItem) {
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
        }
        
        vm.checkNum = checkNum;
        function checkNum() {
            $("#conHighHTCT, #conCapexHTCT").keypress(function (event) {
            	var $this = $(this);
                if (!htmlCommonService.validateIntKeydown(event, $this.val())) {
                    event.preventDefault();
                }
                var text = $(this).val().replace(/,/g, "");
                if ($(this).val() == 0) {
                    text = '';
                }
                if (text.length >= 19) {
                    event.preventDefault();
                }
            });
        }
        
        function openCatProvincePopup(){
    		var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm tỉnh");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
        }

        function onSaveCatProvince(data){
            vm.MESearch.catProvince = data.name;
            vm.MESearch.provinceId = data.catProvinceId;
            htmlCommonService.dismissPopup();
            $("#province").focus();
	    };
	    
	    vm.directional = function (dataItem) {
            var teamplateUrl = "coms/manageME/directionalPopup.html";
            var title = "Điều phối quản lý trạm";
            var windowId = "Directional";
            vm.addForm = dataItem;
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '800', '350', null);
        }
        
        vm.userOptions = {
	            dataTextField: "employeeCode",
	            dataValueField: "employeeCode",
	            placeholder: "Nhập tên hoặc mã nhân viên",
	            select: function (e) {
	                vm.selectedDept1 = true;
	                var dataItem = this.dataItem(e.item.index());
	                vm.addForm.manageUserCode = dataItem.employeeCode;
                	vm.addForm.manageUserName = dataItem.fullName;
                	vm.addForm.manageUserEmail = dataItem.email;
                	vm.addForm.manageUserId = dataItem.userId;
	            },
	            pageSize: 10,
	            open: function (e) {
	            },
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function (options) {
	                        return Restangular.all("manageMERsService/doSearchUser").post({
	                            name: vm.addForm.manageUserCode,
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
	                	vm.addForm.manageUserCode = null;
	                	vm.addForm.manageUserName = null;
	                	vm.addForm.manageUserEmail = null;
	                	vm.addForm.manageUserId = null;
	                }
	            },
	            ignoreCase: false
	        };
        
        function checkRoleCD() {
        	var obj={
        			sysGroupId: Constant.userInfo.VpsUserInfo.sysGroupId
        	}
        	return Restangular.all("manageMERsService/checkRoleCD").post(obj).then(
                    function (response) {
                    	vm.checkRoleCD = response.checkRoleCD;
						vm.provinceId = response.provinceId;
                    }
                ).catch(
                    function (err) {
                        console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                    }
                );
		}
        
        vm.saveManageStation = function() {
			if(vm.addForm.manageUserCode == null || vm.addForm.manageUserCode == ""){
				toastr.warning("Mã nhân viên quản lý không được để trống !");
            	return false;
			}
			manageMEService.saveManageStation(vm.addForm).then(
	                function (res) {
	                    if (res && res.statusCode != null) {
	                        toastr.success("Thành công !");
	                        cancelImport();
	                    } else {
	                        toastr.error("Có lỗi xảy ra!");
	                    }
	                }
	            )
		}
        
        vm.exportFile = function exportFile() {
			var teamplateUrl = "coms/manageME/exportPopup.html";
            var title = "Export file";
            var windowId = "ELECTRICAL";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1100', '500', null);
				
		}
        
        vm.exportDeviceList = [
        	{name: "Lưới điện", value: 0},
        	{name: "Thông gió lọc bụi", value: 1},
        	{name: "Nhà máy nổ", value: 2},
        	{name: "Tủ nguồn AC", value: 3},
        	{name: "Máy phát điện", value: 4},
        	{name: "Bộ cắt lọc sét", value: 5},
        	{name: "Tủ nguồn DC", value: 6},
        	{name: "Ắc quy", value: 7},
        	{name: "Hệ thống tiếp địa", value: 8},
        	{name: "Điều hòa AC", value: 9},
        	{name: "Bình cứu hỏa", value: 10},
        	{name: "Thiết bị Rectifer", value: 11},
        	{name: "Điều hòa DC", value: 12},
        	{name: "Hệ thống cảnh báo", value: 13},
        	{name: "Thông tin nhà trạm", value: 14},
        	{name: "Bộ trao đổi nhiệt", value: 15},
        	{name: "ATS/ATS timer/GSĐK", value: 16}
        ];
        vm.cancelExport = cancelExport;
        function cancelExport() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
            vm.listType = [];
        }
        
        vm.listType = [];
        vm.selectRadio = selectRadio
        function selectRadio(type) {
        	if(vm.listType.length > 0){
        		var check = true;
        		var index = 0;
            	for (var i = 0; i < vm.listType.length; i++) {
        			if(vm.listType[i] == type){
        				check = false;
        				index = i;
        				break;
        			}else{
        				check = true;
        			}
				};
        		if(check){
        			vm.listType.push(type);
        		}else{
        			vm.listType.splice(i,1);
        		}
        	}else{
        		vm.listType.push(type);
        	}
		}
        
        vm.exportExcel = exportExcel
        function exportExcel() {

			kendo.ui.progress($("#manageMEGrid"), true);
			vm.MESearch.page = null;
			vm.MESearch.pageSize = null;
			vm.MESearch.listType = vm.listType.sort(function (a, b) {  return a - b;  });
			if(vm.MESearch.listType.length <1){
				  toastr.warning("Phải chọn hạng mục!");
	                return;
			}
			return Restangular.all("manageMERsService/exportExcel").post(vm.MESearch).then(function (d) {
				var data = d.plain();                         
				window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
				kendo.ui.progress($("#manageMEGrid"), false);
			}).catch(function (e) {
				kendo.ui.progress($("#manageMEGrid"), false);
				toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
				return;
			});
		}
        
    }
})();
