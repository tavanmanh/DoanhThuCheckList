(function () {
    'use strict';
    var controllerId = 'surveyRequestController';

    angular.module('MetronicApp').controller(controllerId, surveyRequestController);

    function surveyRequestController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,$modal,
                                     kendoConfig, $kWindow, surveyRequestService,surveyRequestDetailService, htmlCommonService,
                                     CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http) {
    	
        var vm = this;
        var record = 0;
        var modalChooseGoods;
        vm.check = null;
        vm.getSysUser = (Constant.user.VpsUserInfo == undefined ) ==true ? Constant.user.vpsUserToken.employeeCode : Constant.user.VpsUserInfo.employeeCode;
        
        $scope.sysGroupId = Constant.user.VpsUserInfo.sysGroupId;
        $scope.sysUserId = $rootScope.casUser.sysUserId;
        vm.String = "Tiếp xúc khách hàng > Quản lý yêu cầu xử lý sự cố/ khiếu nại ";
        var user = Constant.user;
        vm.editMode = false;
        vm.isCompleted = false;
        vm.isExtend = false;
        vm.isDetail = false;
        vm.isShowReason = false;
        vm.isCreateNew = false;
        vm.showDeploy = false;
        vm.isShowSave = true;
        vm.choosePerformerForm = {};
        vm.isChooseSource = false;
        vm.listChecked = [];
        vm.ticketReasonList = [];
            // label name html
       vm.htmlLabel = {
                keySearch: "Nội dung tìm kiếm",
                processStatus: "Tiến độ",
                userCreate: "Người tạo",
                dateFrom: "Ngày tạo từ ngày",
                dateTo: "đến",
                ticketStatus: "Trạng thái thực hiện",
                userPerformer: "Người thực hiện",
       };

       vm.searchForm = {
            	status: 5,
            	process: "",
            	createDateTo: null,
            	createDateFrom: null,
                keySearch: "",
                performerName: vm.getSysUser,
                isFirst: 0,
                createUser: vm.getSysUser,
                sysGroupId : $scope.sysGroupId ,
                sysUserId: $scope.sysUserId
       };

            // status drop down options
       vm.processStatusList = [
            {
                id: 3,
                name: "Quá hạn"
            },{
                id: 2,
                name: "Sắp quá hạn"
            },{
                id: 1,
                name: "Trong hạn"
            },
            {
                id: "",
                name: "Tất cả"
            }
         ];
         vm.ticketStatusList = [
            {
                id: 5,
                name: "Tất cả"
            }, {
                id: 1,
                name: "Chưa thực hiện"
            }, {
                id: 2,
                name: "Đang thực hiện"
            }, {
                id: 3,
                name: "Đã hoàn thành"
            }, {
                id: 4,
                name: "Đóng yêu cầu"
            }
         ];
         
         vm.ticketSourceExtendList = [
             {
                 code: "CUSTOMER",
                 name: "Do khách hàng"
             }, {
            	 code: "CNKT",
                 name: "Do CNKT"
             }
          ];
         
         vm.ticketSourceCloseList = [
             {
            	 code: "PERSONAL", 
                 name: "Con người"
             }, {
            	 code: "MANAGEMENT", 
                 name: "Quản trị"
             },
             {
            	 code: "SYSTEM", 
                 name: "Hệ thống"
             }, {
            	 code: "PROCESS", 
                 name: "Quy trình"
             }
          ];
         
         vm.ticketReasonFormList = [
             {
                 id: 1,
                 name: "Khách hàng chưa sắp xếp thời gian",
                 key: "CUSTOMER"
             }, 
             {
                 id: 2,
                 name: "Khách hàng không đồng ý phương án xử lý",
                 key: "CUSTOMER"
             }, 
             {
                 id: 3,
                 name: "Chi nhánh chưa sắp xếp được nhân sự",
                 key: "CNKT"
             },
             {
                 id: 4,
                 name: "Chi nhánh đang xử lý nhưng chưa xong",
                 key: "CNKT"
             }, 
             {
                 id: 5,
                 name: "Chưa thống nhất phương án xử lý cho Khách hàng",
                 key: "CNKT"
             }, 
             {
                 id: 6,
                 name: "Chưa có đủ vật tư/ thiết bị để xử lý",
                 key: "CNKT"
             }, 
             {
                 id: 7,
                 name: "Lý do khác",
                 key: "CNKT"
             }
        ];
         
        vm.ticketCloseFormList = [
             {
                 id: 1,
                 name: "Giám sát không đảm bảo chuyên môn",
                 key: "PERSONAL"
             }, 
             {
                 id: 2,
                 name: "Chất lượng nhà thầu phụ kém",
                 key: "PERSONAL"
             }, 
             {
                 id: 3,
                 name: "Ý thức nhân sự thực thi kém",
                 key: "PERSONAL"
             },
             {
                 id: 4,
                 name: "Nhân sự không được đào tạo",
                 key: "PERSONAL"
             }, 
             {
                 id: 5,
                 name: "Khác",
                 key: "PERSONAL"
             }, 
             {
                 id: 6,
                 name: "BGĐ của chi nhánh không chỉ đạo sâu sát",
                 key: "MANAGEMENT"
             }, 
             {
                 id: 7,
                 name: "Các phòng ban không phối hợp",
                 key: "MANAGEMENT"
             }, 
             {
                 id: 8,
                 name: "Sự cố bị tạo trùng",
                 key: "SYSTEM"
             }, 
             {
                 id: 9,
                 name: "Điều phối sai luồng xử lý",
                 key: "SYSTEM"
             }, 
             {
                 id: 10,
                 name: "Quy trình giám sát đang có lỗ hổng",
                 key: "PROCESS"
             }, 
             {
                 id: 11,
                 name: "Quy trình chồng chéo, không thể thực thi",
                 key: "PROCESS"
             }
        ];

         vm.ticketDetail = {
                customerAddress: '',
                customerName: '',
                customerPhone: '',
                service: '',
         };
          
         init();
         
         function init() {
            	var obj = {};
                surveyRequestService.checkRoleTTHTView(obj).then(function(d){
                	if (d && d == 'OK') {
                        vm.showDeploy = true;
                    } else {
                    	vm.showDeploy = false;
                    }
                }).catch(function () {
                	vm.showDeploy = false;
                });
                
                obj.sysUserId = $scope.sysUserId;
                obj.sysGroupId = $scope.sysGroupId ;
                surveyRequestService.checkRoleDeployTicket(obj).then(function(d){
                	if (d && d == 'OK') {
                		$scope.checkRoleDeployTicket = 1;
                		fillDataTable();
                    } else {
// $scope.checkRoleDeployTicket = 0;
                    	$scope.checkRoleDeployTicket = 1;
                    	fillDataTable();
                    }
                }
                ).catch(function () {
// $scope.checkRoleDeployTicket = 0;
                	$scope.checkRoleDeployTicket = 1;
                	fillDataTable();
                });
                
                postal.subscribe({
                    channel: "Tab",
                    topic: "action",
                    callback: function (data) {
                    	if (data.action == 'refresh') vm.ordersRequestGrid.dataSource.read();
                    }
             });
                
        }   

            // append css
         $("head").append('<style type="text/css">' +
                '.k-widget.k-tooltip.k-tooltip-validation.k-invalid-msg { background-color: #fcf8e3; font-size: 13px; color: red; border: thin solid #dcd8c3; overflow: visible;}' +
                
         '</style>');
            
         


        

        vm.doSearch = doSearch;
        
        function doSearch() {
          vm.searchForm.isFirst = 1;
          vm.searchForm.checkRoleTTHTView = $scope.checkRoleTTHTView;
          vm.searchForm.checkRoleCSKH = $scope.checkRoleCSKH;
          vm.searchForm.checkRoleDeployTicket = $scope.checkRoleDeployTicket;
// vm.searchForm.provinceViewDeploy = $scope.provinceViewDeploy;
          vm.searchForm.checkRoleViewData =	$scope.checkRoleViewData;
          vm.searchForm.provinceViewData = $scope.provinceViewData;
          var grid = vm.ordersRequestGrid;
          CommonService.doSearchGrid(grid, {pageSize: grid.dataSource.pageSize()});
        
        };
        
        function strToDateNoTime(dateStr) {
            if (!dateStr) return null;
            var dateOnly = dateStr.split(" ")[0];
            var dateParts = dateOnly.split("/");

            return new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0],);
        }

        // ----- Add start
        vm.saveChangeTicketLog = function () {
            var obj = angular.copy(vm.ticketDetail);
            obj.status = 3;
            obj.complainOrderRequestDTO.status = 3;
            if (!obj.source) {
	               toastr.error("Chưa chọn nguồn xuất phát lí do");
	               return;
		    }
          	if (!obj.reason) {
	               toastr.error("Chưa chọn nguyên nhân/lí do");
	               return;
		    }
          	if (!obj.note) {
 		       toastr.error("Chưa ghi/giải thích cụ thể lí do");
 		        return;
 		    }
            kendo.ui.progress($('.k-window'), true);
            surveyRequestService.update(obj)
                .then(function (resp) {
                	if(resp && resp.resultInfo && resp.resultInfo.type == 'success'){
                    	kendo.ui.progress($('.k-window'), false);
                        toastr.success("Lưu thay đổi thành công");
                        closeAll();
                        doSearch();
                    }else{
                        toastr.error(resp.resultInfo.message);
                        kendo.ui.progress($('.k-window'), false);
                        return;
                    }
                    
                }, function (err) {
                    kendo.ui.progress($('.k-window'), false);
                    toastr.error("Lưu thay đổi thất bại");
                    console.log(err);
                });
        };

        vm.resetAddForm = resetAddForm;

        function resetAddForm() {
            vm.ticketDetail = {};
        };

        vm.openAddView = function () {
            vm.buttonSaveLabel = "Tạo mới yêu cầu";
            vm.delayDateErr = '';
            vm.isCompleted = false;
            vm.isExtend = false;
            vm.isDetail = false;
            vm.isCreateNew = true;
            var templateUrl = 'coms/handleComplain/surveyRequestAdd.html';
            var title = gettextCatalog.getString("Thêm mới yêu cầu");
            var windowId = "ADD_COMPLAIN_ORDER_REQUEST";
            resetAddForm();
            CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, true, '80%', '80%', "deptAdd")
        };

        vm.closeAll = closeAll;

        function closeAll() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        };


        // check user
        vm.checkSysUser= function(data) {
            if (data.performerName != vm.getSysUser) {
                return false
            } else if (data.performerName == vm.getSysUser) {
                return true;

            }
        };
        
        vm.edit = function (dataItem) {
            vm.buttonSaveLabel = "Lưu thay đổi";
            vm.delayDateErr = '';
            vm.ticketDetail = {};
            vm.isShowReason = false;
            vm.isDetail = true;
            vm.isCreateNew = false;
            if(vm.checkSysUser(dataItem) == true)	vm.editMode = true;
            else vm.editMode = false;
            
	        vm.ticketDetail.complainOrderRequestDTO = angular.copy(dataItem);
	        if(vm.ticketDetail.complainOrderRequestDTO.status == 1){
	        	vm.buttonSaveLabel = "Tiếp nhận";
	        	vm.isShowSave=true;
	        }else {
	        	vm.buttonSaveLabel = "Lưu thay đổi";
	        	vm.isShowSave=false;
	        }
	        checkTicketSourceList();
	        bindTicketFieldHtml(vm.ticketDetail.complainOrderRequestDTO);
	        console.log(vm.editMode);
            var templateUrl = 'coms/handleComplain/surveyRequestEdit.html';
            var title = gettextCatalog.getString("Chi tiết yêu cầu");
            var windowId = "VIEW_COMPLAIN_ORDER_REQUEST_DETAIL";
            CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, true, '95%', '95%', "deptAdd");
            console.log(vm.editMode);

        };

        vm.showApproveExtend = function (dataItem) {
        		vm.ticketDetail = {};
        		vm.ticketDetail.complainOrderRequestDTO = angular.copy(dataItem);
		        confirm('Xác nhận tiếp nhận thực hiện ticket',function(){
					vm.ticketDetail.complainOrderRequestDTO.status = 2;
					surveyRequestService.approve(vm.ticketDetail).then(
		                function (resp) {                    
		                    if(resp && resp.resultInfo && resp.resultInfo.type == 'success'){
		                    	kendo.ui.progress($('.k-window'), false);
		                        toastr.success("Tiếp nhận thành công");
		                        closeAll();
		                        doSearch();
		                    }else{
		                        toastr.error(resp.resultInfo.message);
		                        kendo.ui.progress($('.k-window'), false);
		                        return;
		                    }
		                    
		                },
		                function (err) {
		                    console.log(err);
		                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại ."); !result
		                }
		            )
		        });
		        
        };
        
        
// function checkTicketReasonList() {
//            
// if (vm.isCompleted == true) {
// vm.ticketReasonList = [
//                    
// ];
// }else if (vm.isExtend == true) {
// vm.ticketReasonList = [
// {
// id: 1,
// name: "KH yêu cầu"
// }, {
// id: 2,
// name: "KH nằm trong khu vực giãn cách"
// }, {
// id: 3,
// name: "Lên phương án thi công/thiết kế"
// }, {
// id: 4,
// name: "Khác"
// }
// ];
// }
//            
// return vm.ticketReasonList;
// };
        
        
	     vm.changeSource=function changeSource(data) {
	    	 if (vm.isCompleted == true) {
	            	vm.ticketReasonList = vm.ticketCloseFormList.filter(function(record) {
						  return record.key == data;
					});
	            }else if (vm.isExtend == true) {
	            	vm.ticketReasonList  = vm.ticketReasonFormList.filter(function(record) {
						  return record.key == data;
					});
	            }
	            return vm.ticketReasonList;
	    };
        
        function checkTicketSourceList() {
            if (vm.isCompleted == true) {
            	vm.ticketSourceClose = angular.copy(vm.ticketSourceCloseList);
            }else if (vm.isExtend == true) {
            	vm.ticketSourceExtend = angular.copy(vm.ticketSourceExtendList);
            }
            vm.isChooseSource = true;
            return vm.isChooseSource;
        };
        
        function bindTicketFieldHtml(data) {
            
            if (data.status != null) {
            	if(data.status == 1){
            		vm.ticketDetail.complainOrderRequestDTO.htmlStatus = 'Chưa thực hiện';
            	}else if(data.status == 2){
            		vm.ticketDetail.complainOrderRequestDTO.htmlStatus = 'Đang thực hiện';
            	}else if(data.status == 3){
            		vm.ticketDetail.complainOrderRequestDTO.htmlStatus = 'Đã hoàn thành';
            		vm.ticketDetail.complainOrderRequestDTO.result = '1';vm.isCompleted = true;
            	}else if(data.status == 4){
            		vm.ticketDetail.complainOrderRequestDTO.htmlStatus = 'Đã đóng';
            	}
            	
            }
            if (data.process != null) {
            	if(data.process == "1"){
            		vm.ticketDetail.complainOrderRequestDTO.htmlProcess = 'Trong hạn';
            	}else if(data.process == "2"){
            		vm.ticketDetail.complainOrderRequestDTO.htmlProcess = 'Sắp hết hạn';
            	}else if(data.process == "3"){
            		vm.ticketDetail.complainOrderRequestDTO.htmlProcess = 'Quá hạn';
            	}
            }
            vm.ticketDetail.complainOrderRequestDTO.createDateString = ''+vm.ticketDetail.complainOrderRequestDTO.createDateString;
	        vm.ticketDetail.complainOrderRequestDTO.receivedDateString = ''+vm.ticketDetail.complainOrderRequestDTO.receivedDateString;
	        vm.ticketDetail.complainOrderRequestDTO.completedTimeExpectedString = ''+vm.ticketDetail.complainOrderRequestDTO.completedTimeExpectedString;
	        vm.ticketDetail.complainOrderRequestDTO.completedTimeRealString = ''+vm.ticketDetail.complainOrderRequestDTO.completedTimeRealString;
	        
        };
        
        vm.completed=completed;
		function completed(dataItem){
			  vm.buttonSaveLabel = "Lưu thay đổi";
	          vm.isCompleted = true;
	          vm.isDetail = true;
	          vm.isExtend = false;
	          vm.isCreateNew = false;
	          vm.editMode = true;
	          vm.ticketDetail = {};
	          vm.ticketDetail.complainOrderRequestDTO = angular.copy(dataItem);
	          checkTicketSourceList();
	          var templateUrl = 'coms/handleComplain/surveyRequestClose.html';
	          var title = gettextCatalog.getString("Sửa thông tin");
	          var windowId="COMPLAIN_COMPLETED";
			  CommonService.populatePopupCreate(templateUrl,title,vm.ticketDetail,vm,windowId,true,'50%','50%',"code");
	       
		};

        vm.extendTicket = function (dataItem) {
            vm.buttonSaveLabel = "Lưu thay đổi";
            vm.delayDateErr = '';
            vm.isExtend = true;
            vm.isDetail = true;
            vm.isCompleted = false;
            vm.isCreateNew = false;
            vm.editMode = true;
            checkTicketSourceList();
            vm.ticketDetail = {};
            vm.ticketDetail.complainOrderRequestDTO = angular.copy(dataItem);
            var templateUrl = 'coms/handleComplain/surveyRequestExtend.html';
	        var title = gettextCatalog.getString("Sửa thông tin");
	        var windowId="COMPLAIN_EXTEND";
	        CommonService.populatePopupCreate(templateUrl,title,vm.ticketDetail,vm,windowId,true,'50%','50%',"code");
        };

        vm.submitEdit = function () {

            var obj = angular.copy(vm.ordersData);
            obj.status = vm.ordersData.statusOrder;
            if (!validateSave(obj, true)) {
                return;
            }
            obj.customerAddress = vm.ordersData.customerAddress + "," + vm.ordersData.addressTrail;

            surveyRequestService.submitEdit(obj)
                .then(function (response) {
                    if (response.error) {
                        toastr.error(response.error);
                        return;
                    }

                    toastr.success("Sửa yêu cầu thành công");
                    closeAll();
                    doSearch();
                }, function (err) {
                    toastr.error("Sửa yêu cầu thất bại");
                    console.log(err);
                });
        };
        
       vm.checkChangeDate = function () {
            if (vm.searchForm.createDateTo < vm.searchForm.createDateFrom) {
                vm.searchForm.createDateTo = "";
                toastr.error("Ngày chọn ko hợp lệ, vui lòng chọn lại");
                return;
            }
       };

        vm.resetInput = resetInput;

        function resetInput(fieldName) {
            switch (fieldName) {
                case 'performerUser':
                    vm.searchForm.performerSearchUser = null;
                    vm.searchForm.performerUser = null;
                    break;
                case 'userCreate':
                    vm.searchForm.createUserName = null;
                    vm.searchForm.createUser = null;
                    break;
                case 'date':
                    vm.searchForm.createDateFrom = null;
                    vm.searchForm.createDateTo = null;
                    break;
                case 'keySearch':
                    vm.searchForm.keySearch = null;
                    break;
            }
        };

        var listRemove = [
        	{
            title: "Thao tác"  
        	},
        	{
            title: "Chọn"  
            },
        
        ];

        var listConvert = [
          {
            field: "status",
            data: {
                1: "Chưa thực hiện",
                2: "Đang thực hiện",
                3: "Đã hoàn thành",
                4: "Đóng yêu cầu",

            }
          }, 
          {
            field: "process",
            data: {
                1: "Trong hạn",
                2: "Sắp hết hạn",
                3: "Quá hạn",
          }
        }
        ];

        vm.exportFile = function () {
            vm.searchForm.page = null;
            vm.searchForm.pageSize = null;
            vm.searchForm.isFirst = 1;
            vm.searchForm.checkRoleTTHTView = $scope.checkRoleTTHTView;
            vm.searchForm.checkRoleCSKH = $scope.checkRoleCSKH;
            vm.searchForm.checkRoleDeployTicket = $scope.checkRoleDeployTicket;
// vm.searchForm.provinceViewDeploy = $scope.provinceViewDeploy;
            vm.searchForm.checkRoleViewData =	$scope.checkRoleViewData;
            vm.searchForm.provinceViewData = $scope.provinceViewData;
            var data = vm.searchForm;
            surveyRequestService.doSearch(data).then(function (d) {
                var data = d.dataListDTO.data;
                CommonService.exportFile(vm.ordersRequestGrid, data, listRemove, listConvert, "Danh sách yêu cầu ticket");
            });
        };


        vm.showHideColumnDetail = function (column) {
            var grid = vm.ordersRequestGrid;
            CommonService.showHideColumnGrid(grid, column)
        };

        vm.gridColumnShowHideFilter = function (item) {
            return item.type == null || item.type !== 1;
        };
        
        function fillDataTable() {
	        var record = 0;
	        vm.ordersRequestGridOptions = kendoConfig.getGridOptions({
	            autoBind: true,
	            scrollable: true,
	            resizable: true,
	            editable: false,
	            dataBinding: function () {
	                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
	            },
	            reorderable: true,
	            toolbar: [
	                {
	                    name: "actions",
	                    template:
	                    	 
	                        '<button class="btn btn-qlk padding-search-right addQLK ng-scope" ng-click="vm.openChoosePerformer()" ng-if="(' + vm.showDeploy + '||' + $scope.checkRoleDeployTicket + ')"  style="margin-left: 10px;width: 110px;" uib-tooltip="Điều phối">Điều phối</button>' +
	                        '<div class="btn-group pull-right margin_top_button margin_right10">' +
	                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false">' +
	                        '<span class="tooltipArrow"></span>' +
	                        '<span class="tooltiptext">Cài đặt</span>' +
	                        '<i class="fa fa-cog" aria-hidden="true"></i>' +
	                        '</i>' +
	                        '<i class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()" aria-hidden="true"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
	                        '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
	                        '<label ng-repeat="column in vm.ordersRequestGrid.columns | filter: vm.gridColumnShowHideFilter">' +
	                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumnDetail(column, 1)"> {{column.title}}' +
	                        '</label>' +
	                        '</div>' +
	                        '</div>'
	                }
	            ],
	            dataSource: {
	                serverPaging: true,
	                schema: {
	                    total: function (response) {
	                        $timeout(function () {
	                            vm.count = response.dataListDTO.total
	                        });
	                        return response.dataListDTO.total;
	                    },
	                    data: function (response) {
	                        var listData = response.dataListDTO.data;
	                        var count = 0;
	                        for (var i = 0; i < vm.listChecked.length; i++) {
	                            for (var j = 0; j < listData.length; j++) {
	                                if (listData[j].complainOrderRequestId == vm.listChecked[i].complainOrderRequestId) {
	                                    listData[j].checked = true;
	                                    count++;
	                                    break;
	                                }
	                            }
	                        }
	                        vm.checkAll = listData.length > 0 && count === listData.length;
	                        if(vm.searchForm.isFirst==0){
	                        	$scope.checkRoleTTHTView = response.checkRoleTTHTView;
	                        	$scope.checkRoleCSKH = response.checkRoleCSKH;
// $scope.checkRoleDeployTicket = response.checkRoleDeployTicket;
	                        	$scope.checkRoleDeployTicket = 1;
	// $scope.provinceViewDeploy = response.provinceViewDeploy;
	            				$scope.checkRoleViewData = response.checkRoleViewData;
	            				$scope.provinceViewData = response.provinceViewData;
	                        }
	                        return response.dataListDTO.data;
	                    }
	                },
	                transport: {
	                    read: {
	                        url: Constant.BASE_SERVICE_URL + RestEndpoint.COMPLAIN_ORDERS_REQUEST_SERVICE_URL + "/doSearch",
	                        contentType: "application/json; charset=utf-8",
	                        type: "POST"
	                    },
	                    parameterMap: function (options, type) {
	                        var obj = angular.copy(vm.searchForm);
	                        obj.page = options.page;
	                        obj.pageSize = options.pageSize;
	                        obj.checkRoleDeployTicket = $scope.checkRoleDeployTicket;
	                        return JSON.stringify(obj)
	                    }
	                },
	                pageSize: 10
	            },
	            columnMenu: false,
	            noRecords: true,
	            messages: {
	                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
	            },
	            pageable: {
	            	refresh: true,
	                pageSizes: [10, 15, 20, 25],
	                messages: {
	                    display: "{0}-{1} của {2} kết quả",
	                    itemsPerPage: "kết quả/trang",
	                    empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
	                }
	            },
	            columns: [
	                {
	                    title: "Chọn",
	                    field: "",
	                    width: "50px",
	                    columnMenu: false,
	                    attributes: {
	                        style: "text-align:center;white-space:normal;"
	                    },
	                    hidden: (((vm.showDeploy==true ||$scope.checkRoleDeployTicket==1))==true)? false : true,
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    
	                    headerTemplate: function (dataItem) {
	                        return '<input type="checkbox" ng-model="vm.checkAll" ng-click="vm.handleCheckAll()" ng-if="(' + vm.showDeploy + '||' + $scope.checkRoleDeployTicket + ')" />';
	                    },
	                    template: function (dataItem) {
	                        return '<input type="checkbox" id="childcheck" name="gridcheckbox" ng-model="dataItem.checked" ng-click="vm.handleCheckbox(dataItem)" ng-show="(dataItem.status == 1 ||dataItem.status == 2) && (('+ vm.showDeploy + '||' + $scope.checkRoleDeployTicket + '))" />';
	                    }
	                    
	                },
	                {
	                     title: "STT",
	                     field: "stt",
	                     width: "50px",
	                     headerAttributes: {style: "text-align:center;white-space:normal;"},
	                     attributes: {style: "text-align:center;white-space:normal;"},
	                     template: function (dataItem) {
	                         return ++record;
	                     }
	                },
	                {
	                    title: "Người thực hiện",
	                    field: "performerShow",
	                    width: "150px",
	                    format: "{0:n0}",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:center;white-space:normal;"}
	                },
	                
	                {
	                    title: "Mã ticket",
	                    field: "ticketCode",
	                    width: "120px",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:center;white-space:normal;font-weight: bold;"}
	                },
	                {
	                    title: "Tỉnh",
	                    field: "provinceName",
	                    width: "120px",
	                    format: "{0:n0}",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:left;white-space:normal;"}
	                },
	                {
	                    title: "Huyện",
	                    field: "districtName",
	                    width: "120px",
	                    format: "{0:n0}",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:left;white-space:normal;"}
	                },
	                {
	                    title: "Xã",
	                    field: "wardsName",
	                    width: "120px",
	                    format: "{0:n0}",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:left;white-space:normal;"}
	                },
	                {
	                    title: "Họ và tên khách hàng",
	                    field: "customerName",
	                    width: "120px",
	                    format: "{0:n0}",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:left;white-space:normal;"}
	                },
	                {
	                    title: "Địa chỉ",
	                    field: "customerAddress",
	                    width: "200px",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:left;white-space:normal;"},
	                },
	                {
	                    title: "Số điện thoại",
	                    field: "customerPhone",
	                    width: "120px",
	                    format: "{0:n0}",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:center;white-space:normal;"}
	                },
	                {
	                    title: "Thời gian tạo",
	                    field: "createDateString",
	                    width: "120px",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:center;white-space:normal;color: green ; font-weight: bold;"}
	                },
	                {
	                    title: "Thời gian NV tiếp nhận",
	                    field: "receivedDateString",
	                    width: "120px",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:center;white-space:normal;color: green ; font-weight: bold;"}
	                },
	                {
	                    title: "Thời gian kết thúc dự kiến",
	                    field: "completedTimeExpectedString",
	                    width: "120px",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:center;white-space:normal;color: red ; font-weight: bold;"}
	                },
	                {
	                    title: "Thời gian hoàn thành thực tế",
	                    field: "completedTimeRealString",
	                    width: "120px",
	                    format: "{0:n0}",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:center;white-space:normal;color: blue ; font-weight: bold;"}
	                },
	                {
	                    title: "Dịch vụ",
	                    field: "service",
	                    width: "100px",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:center;white-space:normal;"}
	                },
	                {
	                    title: "Loại khiếu nại",
	                    field: "complainGroup",
	                    width: "100px",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:center;white-space:normal;"}
	                },
	//                
	// {
	// title: "Nội dung phản ánh",
	// field: "title",
	// width: "120px",
	// format: "{0:n0}",
	// headerAttributes: {style: "text-align:center;white-space:normal;"},
	// attributes: {style: "text-align:center;white-space:normal;"}
	// },
	//                
	                {
	                    title: "Người tạo xử cố/khiếu nại",
	                    field: "createUserName",
	                    width: "120px",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:center;white-space:normal;"}
	                },
	                {
	                    title: "Trạng thái",
	                    field: "status",
	                    width: "120px",
	                    format: "{0:n0}",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:center;white-space:normal;"},
	                    template: function (dataItem) {
	
	                        switch (dataItem.status) {
	                        	case 1 :
	                        		return "Chưa thực hiện";
	                            case 2 :
	                                return "Đang thực hiện";
	                            case 3 :
	                                return "Đã hoàn thành";
	                            case 4:
	                                return "Đóng yêu cầu";  
	                        }
	                        return ""
	                    }
	                },
	                {
	                    title: "Tiến độ",
	                    field: "process",
	                    width: "120px",
	                    format: "{0:n0}",
	                    headerAttributes: {style: "text-align:center;white-space:normal;"},
	                    attributes: {style: "text-align:center;white-space:normal;"},
	                    template: function (dataItem) {
	
	                        switch (dataItem.process) {
	                            case "1" :
	                               
	                                return "<div ><span style='font-weight: bold;color:green;'>Trong hạn</span></div>";
	                            case "2" :
	                                
	                                return "<div ><span style='font-weight: bold;color:orange;'>Sắp quá hạn</span></div>";
	                            case "3":
	                                
	                                return "<div ><span style='font-weight: bold;color:red;'>Quá hạn</span></div>";
	                           
	                        }
	                        return ""
	                    }
	                }, 
	                {
	                    title: "Thao tác",
	                    width: '200px',
	                    field: "action",
	                    template: function (dataItem) {
	                        return '<div class="text-center">'
	                            +
	                            '<button ng-if = "dataItem.status == 2 && dataItem.process !=3 && vm.checkSysUser(dataItem)==true" style=" border: none; background-color: white;"' +
	// '<button ng-if = "dataItem.status == 2 && dataItem.process !=3" style="
	// border: none; background-color: white;"' +
	                            'class="#=complainOrderRequestId# icon_table"  uib-tooltip="Gia hạn" translate' +
	                            ' ng-click="vm.extendTicket(dataItem)" >' +
	                            '<i class="fa fa-clock-o" style="color: #337ab7;" aria-hidden="true"></i>' +
	                            
	                            '</button>' +
	                            '<button  ng-if = "dataItem.status == 2 && vm.checkSysUser(dataItem)==true" style=" border: none; background-color: white;"' +
	                            'class="#=complainOrderRequestId# icon_table"  uib-tooltip="Hoàn thành" translate' +
	                            ' ng-click="vm.completed(dataItem)" >' +
	                            '<i class="fa fa-check ng-scope" style="color: lawngreen;" aria-hidden="true"></i>' +
	                            
	                            '</button>' +
	                            '<button  ng-if = "dataItem.status == 1 && dataItem.process !=null && vm.checkSysUser(dataItem)==true" style=" border: none;background-color: white;"' +
	                            'class="#=complainOrderRequestId# icon_table"  uib-tooltip="Nhận ticket" translate' +
	                            ' ng-click="vm.showApproveExtend(dataItem)" >' 
	                            +'<i class="fa fa-external-link-square" style="color:steelblue;" aria-hidden="true"></i>'+
	                            '</button>' +
	                            
	                            '<button style=" border: none; background-color: white;" class="#=complainOrderRequestId# icon_table" ' +
	                            'ng-click="vm.edit(dataItem)" uib-tooltip="Xem chi tiết" translate>' +
	                            '<i class="fa fa-list-alt ng-scope" style="color: #0a4d85;" aria-hidden="true"></i>' +
	                            '</button>' +
	
	                            '</div>'
	                            
	                    }
	                }
	            ]
	        });
        }


// vm.checkEditCustomerService = function (dataItem) {
// if (dataItem.createUser == vm.getSysUser && vm.isExtend==true) {
// return false
// } else return true;
// }

        vm.handleCheckbox = function (dataItem) {
            for (var i = 0; i < vm.listChecked.length; i++) {
                if (vm.listChecked[i].complainOrderRequestId === dataItem.complainOrderRequestId) {
                    vm.listChecked.splice(i, 1);
                    return;
                }
            }

            vm.listChecked.push(dataItem);
        };

        vm.handleCheckAll = function () {
        	vm.listChecked = [];
        	if(vm.checkAll == false) {
        		for (var i = 0; i < vm.ordersRequestGrid.dataSource.data().length; i++) {
                    var dataItem = vm.ordersRequestGrid.dataSource.data()[i];
                    dataItem.checked = false;
                    
                }

        		vm.listChecked = [];
        		
        	}
        	else{
        		for (var i = 0; i < vm.ordersRequestGrid.dataSource.data().length; i++) {
                    var dataItem = vm.ordersRequestGrid.dataSource.data()[i];
                    dataItem.checked = vm.checkAll;
                    var exist = false;
                    for (var j = 0; j < vm.listChecked.length; j++) {
                        if (dataItem.complainOrderRequestId === vm.listChecked[j].complainOrderRequestId) {
                            exist = true;
                            if (!vm.checkAll) {
                                vm.listChecked.splice(j, 1);
                            }
                            break;
                        }
                    }

                    if (!exist && (dataItem.status == 1 || dataItem.status == 2) ) {
                        vm.listChecked.push(dataItem);
                    }
                }
        	}
            
        };
        
        function strToDate(dtStr) {
            var dateParts = dtStr.split("/");
            var timeParts = dateParts[2].split(" ")[1].split(":");
            dateParts[2] = dateParts[2].split(" ")[0];
            // month is 0-based, that's why we need dataParts[1] - 1
            return new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0], timeParts[0], timeParts[1], timeParts[2]);
        }
        
        
	    vm.approveExtend = function (dataItem) {
	    	var obj = angular.copy(vm.ticketDetail);
	    	obj.status = 2;  
	    	if (!obj.source) {
	               toastr.error("Chưa chọn nguồn xuất phát lí do");
	               return;
		    }
          	if (!obj.reason) {
	               toastr.error("Chưa chọn nguyên nhân/lí do");
	               return;
		    }
		    if (!obj.note) {
		       toastr.error("Chưa ghi/giải thích cụ thể lí do");
		        return;
		    }
	        var current = new Date();
	        var extendTime = kendo.toString($('#aio_edit_delayDate').val(), "dd/MM/yyyy hh:mm:ss");
	        var currAddTime = strToDate(dataItem.complainOrderRequestDTO.createDateString);  
	        currAddTime.setMonth( currAddTime.getMonth() + 3);
            if(obj.extendDate==null){
            		toastr.error("Vui lòng nhập/chọn thời gian xin gia hạn mới");
            		return;
            }else if (obj.extendDate < current.getTime()){
            		toastr.error("Thời gian xin gia hạn mới phải lớn hơn thời gian hiện tại");
            		return;
            }else if (obj.extendDate < strToDate(dataItem.complainOrderRequestDTO.completedTimeExpectedString)){
        		toastr.error("Thời gian xin gia hạn mới phải lớn hơn thời gian hoàn thành dự kiến trước đó");
        		return;
            } else if(obj.extendDate > currAddTime.getTime()) {
				$("#aio_edit_delayDate").focus();
				toastr.error("Ticket của bạn không được gia hạn quá 3 tháng so với thời gian tạo yêu cầu ticket.");
				return;
            }
            confirm("Thời gian xin gia hạn mới là <i class='bold' style='color: red;'>"
                    + extendTime + "</i>.Dữ liệu sẽ không được hoàn tác.Bạn có chắc chắn không ?",
                    function () {
                    	obj.complainOrderRequestDTO.completedTimeExpectedString = angular.copy(extendTime);
                    	surveyRequestService.update(obj).then(function (resp) {
        	                if(resp && resp.resultInfo && resp.resultInfo.type == 'success'){
                            	kendo.ui.progress($('.k-window'), false);
                                toastr.success("Gia hạn thành công");
                                closeAll();
                                doSearch();
                            }else{
                                toastr.error(resp.resultInfo.message);
                                kendo.ui.progress($('.k-window'), false);
                                return;
                            }
        	                
        	            }, function (err) {
        	                toastr.error("Lỗi khi cập nhật !");
        	                return;
        	           });
                        
                    })
            
	        
	       };
	      
	      vm.chooseResult = chooseResult;
	      function chooseResult() {
	    	  vm.isShowSave = true;
	    	  if(vm.ticketDetail.complainOrderRequestDTO.result == 1){
	    		  vm.isCompleted = true;  
	    	  }else {
	    		  vm.isExtend = true;
	    	  }
	    	  vm.isShowReason = true;
	    	  checkTicketSourceList();
	      };
	       
        function validateSave(obj, isEdit) {
            var result = true;

            if (obj.customerPhone == undefined || obj.customerPhone == "") {
                toastr.error("Vui lòng nhập số điện thoại");
                result = false;
            }
// if (!obj.customerAddress || !(obj.customerAddress.trim())) {
// toastr.error("Chưa nhập địa chỉ cụ thể");
// result = false;
// }
            if (!obj.service) {
                toastr.error("Chưa nhập dịch vụ");
                result = false;
            }
	        
            return result;
        };

        vm.saveComplainOrder = function () {
            var obj = {};
            obj = angular.copy(vm.ticketDetail);
            if (!validateSave(obj.complainOrderRequestDTO)) {
                return;
            }
            if (vm.isDetail) {
            	if(obj.complainOrderRequestDTO.status == 2){
            		if (!obj.reason) {
	                    toastr.error("Chưa chọn nguyên nhân/lí do");
	                    return;
		            }
		            if(obj.reason == 'Khác') {
		                if (!obj.note) {
		                     toastr.error("Chưa ghi/giải thích cụ thể lí do");
		                     return;
		                }
		            }
		            if (!obj.note) {
		 		       toastr.error("Chưa ghi/giải thích cụ thể lí do");
		 		        return;
		 		    }
		            if(obj.complainOrderRequestDTO.result == 2){
		            	obj.status = 2;
		            	var current = new Date();
		            	var extendTime = kendo.toString($('#aio_edit_delayDate').val(), "dd/MM/yyyy hh:mm:ss");
		            	var currAddTime = strToDate(dataItem.complainOrderRequestDTO.createDateString);  
		    	        currAddTime.setMonth( currAddTime.getMonth() + 3);
		            	if(obj.extendDate==null){
		            		toastr.error("Vui lòng nhập/chọn thời gian xin gia hạn mới");
		            		return;
		            	}else if (obj.extendDate < current.getTime()){
		            		toastr.error("Thời gian xin gia hạn mới phải lớn hơn thời gian hiện tại");
		            		return;
		            	}else if (obj.extendDate < strToDate(obj.complainOrderRequestDTO.completedTimeExpectedString)){
		            		toastr.error("Thời gian xin gia hạn mới phải lớn hơn thời gian hoàn thành dự kiến trước đó");
		            		return;
		                } else if(obj.extendDate > currAddTime.getTime()) {
		    				$("#aio_edit_delayDate").focus();
		    				toastr.error("Ticket của bạn không được gia hạn quá 3 tháng so với thời gian tạo yêu cầu ticket.");
		    				return;
		                }else {
		            		obj.complainOrderRequestDTO.completedTimeExpectedString = angular.copy(extendTime);
		            	}
		            }else{
		            	
		            	obj.complainOrderRequestDTO.status = 3;
		            	obj.status = angular.copy(obj.complainOrderRequestDTO.status);
		            }
            	}else{
            		obj.complainOrderRequestDTO.status = 2;
            		obj.status = angular.copy(obj.complainOrderRequestDTO.status);
            	}
	               
	        }
            
            kendo.ui.progress($('.k-window'), true);
            if(vm.isCreateNew){
            	surveyRequestService.createComplainOrder(obj)
                .then(function (resp) {
                        if(resp && resp.resultInfo && resp.resultInfo.type == 'success'){
                        	kendo.ui.progress($('.k-window'), false);
                            toastr.success("Thêm yêu cầu thành công");
                            closeAll();
                            doSearch();
                        }else{
                            toastr.error(resp.resultInfo.message);
                            kendo.ui.progress($('.k-window'), false);
                            return;
                        }
                        
                    }, function (err) {
                        kendo.ui.progress($('.k-window'), false);
                        toastr.error("Thêm yêu cầu thất bại");
                        console.log(err);
                    });
            }else{
            	surveyRequestService.update(obj)
                .then(function (resp) {
                        if(resp && resp.resultInfo && resp.resultInfo.type == 'success'){
                        	kendo.ui.progress($('.k-window'), false);
                            toastr.success("Cập nhật yêu cầu thành công");
                            closeAll();
                            doSearch();
                        }else{
                            toastr.error(resp.resultInfo.message);
                            kendo.ui.progress($('.k-window'), false);
                            return;
                        }
                        
                        
                    }, function (err) {
                        kendo.ui.progress($('.k-window'), false);
                        toastr.error("Cập nhật yêu cầu thất bại");
                        console.log(err);
                });
            }
            
        };

        vm.handleCheck = function (dataItem) {
            for (var i = 0; i < vm.listChecked.length; i++) {
                if (vm.listChecked[i].complainOrderRequestId === dataItem.complainOrderRequestId) {
                    vm.listChecked.splice(i, 1);
                    return;
                }
            }

            vm.listChecked.push(dataItem);
        };
        var modalChooseUser;

        vm.openChoosePerformer = function () {
            if (vm.listChecked.length < 1) {
                toastr.error("Chọn ít nhất 1 bản ghi/Hệ thống ko còn ticket nào thỏa mãn yêu cầu điều phối!");
                return;
            }
            vm.currentDeploy = {};
            vm.keySearchPerformer = null;
            vm.choosePerformerForm = {};
            if($scope.checkRoleTTHTView==0){
            	if($scope.checkRoleDeployTicket==1)	vm.sysGroupId = $scope.sysGroupId;
            }
            else vm.sysGroupId = null;
            var templateUrl = 'coms/handleComplain/popupPerformerChoose.html';
            var title = gettextCatalog.getString("Điều phối");
	        var windowId="ASSIGN_TICKET";
			CommonService.populatePopupCreate(templateUrl,title,vm.choosePerformerForm,vm,windowId,true,'90%','90%',"code");
        };

        var recordPerformer = 0;
        vm.choosePerformerGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: true,
            resizable: true,
            editable: false,
            dataBinding: function () {
                recordPerformer = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            reorderable: true,
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                        return response.total;
                    },
                    data: function (response) {
                        return response.data;
                    }
                },
                transport: {
                    read: {
                        url: Constant.BASE_SERVICE_URL + RestEndpoint.COMPLAIN_ORDERS_REQUEST_SERVICE_URL + "/getListPerformer",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        var obj = {
                            page: options.page,
                            pageSize: options.pageSize,
                            sysGroupId: vm.sysGroupId,
                            keySearch: vm.keySearchPerformer
                        };

                        return JSON.stringify(obj)
                    }
                },
                pageSize: 10
            },
            columnMenu: false,
            noRecords: true,
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
                    title: "STT",
                    field: "stt",
                    width: "40px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"},
                    template: function () {
                        return ++recordPerformer;
                    }
                },
                {
                    title: "Mã nhân viên",
                    field: "employeeCode",
                    width: "80px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"}
                },
                {
                    title: "Tên nhân viên",
                    field: "fullName",
                    width: "180px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"}
                },
                {
                    title: "Email",
                    field: "email",
                    width: "120px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"}
                },
                {
                    title: "Số điện thoại",
                    field: "phoneNumber",
                    width: "100px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"}
                },
                {
                    title: "Thao tác",
                    width: '70px',
                    field: "action",
                    template: function (dataItem) {
                        if (dataItem.lock) {
                            return '<span></span>';
                        }
                        return '<div class="text-center">'
                            +
                            '<button style=" border: none; background-color: white;" class="#=appParamId# icon_table" ' +
                            'ng-click="caller.choosePerformer(dataItem)" uib-tooltip="Chọn" translate>' +
                            '<i class="fa fa-check" style="color: lawngreen;" aria-hidden="true"></i>' +
                            '</button>'
                            +
                            '</div>'
                    }
                }
            ]
        });

        vm.doSearchPerformer = doSearchPerformer;

        function doSearchPerformer() {
            var grid = vm.choosePerformerGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
                // grid.dataSource.read();
                // grid.refresh();
            }
        };

        vm.closePerformerPopup = function () {
        	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        };
        
	  // Autosuccess Nhân viên
		vm.performerOptions = {
		        dataTextField: "staffName",
		        dataValueField: "staffCode",
				placeholder:"Nhập mã hoặc tên nhân viên",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.searchForm.performerUser = dataItem.staffCode;
		            vm.searchForm.performerSearchUser = dataItem.staffName;
		        },
		        pageSize: 10,
		        open: function (e) {
		            vm.isSelect = false;
		        },
		        dataSource: {
		            serverFiltering: true,
		            transport: {
		                read: function (options) {
		                    vm.isSelect = false;
		                    return Restangular.all("configStaffTangentRestService/catProvince/doSearchStaffByPopup").post({
		                        staffName: vm.searchForm.performerSearchUser,
		                        pageSize: vm.staffOptions.pageSize,
		                        page: 1
		                    }).then(function (response) {
		                        options.success(response.data);
		                    }).catch(function (err) {
		                        console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
		                    });
		                }
		            }
		        },
				headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
				'<p class="col-md-6 text-header-auto border-right-ccc">Mã NV</p>' +
				'<p class="col-md-6 text-header-auto">Tên NV</p>' +
				'</div>',
		        template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.staffCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.staffName #</div> </div>',
		        change: function (e) {
		        	if (!vm.isSelect) {
			            vm.searchForm.performerUser = null;
			            vm.searchForm.performerSearchUser = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.searchForm.performerUser = null;
			            vm.searchForm.performerSearchUser = null;
		            }
		        }
		 }
        
        vm.staffOptions = {
		        dataTextField: "staffName",
		        dataValueField: "staffCode",
				placeholder:"Nhập mã hoặc tên nhân viên",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.searchForm.createUser = dataItem.staffCode;
		            vm.searchForm.createUserName = dataItem.staffName;
		        },
		        pageSize: 10,
		        open: function (e) {
		            vm.isSelect = false;
		        },
		        dataSource: {
		            serverFiltering: true,
		            transport: {
		                read: function (options) {
		                    vm.isSelect = false;
		                    return Restangular.all("configStaffTangentRestService/catProvince/doSearchStaffByPopup").post({
		                        staffName: vm.searchForm.createUserName,
		                        pageSize: vm.staffOptions.pageSize,
		                        page: 1
		                    }).then(function (response) {
		                        options.success(response.data);
		                    }).catch(function (err) {
		                        console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
		                    });
		                }
		            }
		        },
				headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
				'<p class="col-md-6 text-header-auto border-right-ccc">Mã NV</p>' +
				'<p class="col-md-6 text-header-auto">Tên NV</p>' +
				'</div>',
		        template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.staffCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.staffName #</div> </div>',
		        change: function (e) {
		        	if (!vm.isSelect) {
			            vm.searchForm.createUser = null;
			            vm.searchForm.createUserName = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.searchForm.createUser = null;
			            vm.searchForm.createUserName = null;
		            }
		        }
		 }

        vm.choosePerformer = function (dataItem) {
            confirm("Chọn nhân viên </br></br><i class='bold' style='color: red;width:100px;height:50px;'>"
                + dataItem.fullName + "</i></br></br>Thực hiện các  ticket được chọn. ?",
                function () {
                    vm.choosePerformerForm.performerId = dataItem.sysUserId;
                    vm.choosePerformerForm.performerEmail = dataItem.email;
                    vm.choosePerformerForm.performerPhone = dataItem.phoneNumber;
                    vm.choosePerformerForm.performerfullName = dataItem.fullName;
                    vm.choosePerformerForm.performerName = dataItem.employeeCode;
                    
                    vm.choosePerformerForm.performerName = dataItem.employeeCode;
                    
                    vm.choosePerformerForm.performerCode = dataItem.employeeCode;
                    vm.searchForm.keySearch = "";
                    vm.searchForm.performerName= vm.choosePerformerForm.performerCode;
                    
// return
// Restangular.all("complainOrdersRequestRsService/doSearch").post(vm.searchForm).then(function
// (response) {
// if (response.dataListDTO.data.length == 0) {
                    vm.submitDeploy();
//                  	
// }else{
// toastr.error("Nhân viên được chọn vẫn còn tồn ticket chưa thực hiện xong.");
// return;
// }
// }, function (e) {
// console.log(e);
// toastr.error("Có lỗi xảy ra!");
// });
                    
                })
        }
        
        vm.submitDeploy = submitDeploy;

        function submitDeploy() {
            vm.choosePerformerForm.listPerform = [];
            for (var i = 0; i < vm.listChecked.length; i++) {
            	var obj = {};
            	obj.appParamId = vm.listChecked[i].complainOrderRequestId;
            	obj.status = vm.listChecked[i].status;
                vm.choosePerformerForm.listPerform.push(obj);
            }
            var obj = {};
            obj.complainOrderRequestDTO = vm.choosePerformerForm;
            kendo.ui.progress($('.k-window'), true);
            surveyRequestService.choosePerformer(obj).then(function (resp) {
                if(resp && resp.resultInfo && resp.resultInfo.type == 'success'){
                	kendo.ui.progress($('.k-window'), false);
                	toastr.success("Điều phối thành công"); vm.listChecked = [];
                	vm.closeAll();
                    vm.searchForm.status = 5; 
                    vm.doSearch();
                    kendo.ui.progress($('.k-window'), false);
                }else{
                	toastr.error(resp.resultInfo.message);
                    kendo.ui.progress($('.k-window'), false);
                    return;
                }
                
                
                
            }, function (e) {
                kendo.ui.progress($('.k-window'), false);
                console.log(e);
                toastr.error("Có lỗi xảy ra!");
            })
        }
        
        
        var recordLog = 0;
        vm.ComplainDetailGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: true,
            resizable: true,
            editable: false,
            dataBinding: function () {
            	recordLog = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            reorderable: true,
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                    	$timeout(function () {
                            vm.countDetail = response.total
                        });
                        return response.total;
                    },
                    data: function (response) {
                        return response.data;
                    }
                },
                transport: {
                    read: {
                        url: Constant.BASE_SERVICE_URL + RestEndpoint.COMPLAIN_ORDERS_REQUEST_DETAIL_LOG_HISTORY_SERVICE_URL + "/doSearch",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        var obj = {
                            page: options.page,
                            pageSize: options.pageSize,
                            complainOrderRequestId: vm.ticketDetail.complainOrderRequestDTO.complainOrderRequestId
                        };

                        return JSON.stringify(obj)
                    }
                },
                pageSize: 5
            },
            columnMenu: false,
            noRecords: true,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            pageable: {
                refresh: false,
                pageSizes: [4, 5, 8, 10],
                messages: {
                    display: "{0}-{1} của {2} kết quả",
                    itemsPerPage: "kết quả/trang",
                    empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                }
            },
            columns: [
                {
                    title: "STT",
                    field: "stt",
                    width: "40px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"},
                    template: function () {
                        return ++recordLog;
                    }
                },
                {
                    title: "Action",
                    field: "action",
                    width: "80px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"}
                },
                {
                    title: "Trạng thái",
                    field: "status",
                    width: "180px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"},
                    template: function (dataItem) {
                        switch (dataItem.status) {
                        	case 1 :
                        		return "Chưa thực hiện";
                            case 2 :
                                return "Đang thực hiện";
                            case 3 :
                                return "Đã hoàn thành";
                            case 4:
                                return "Đóng yêu cầu";  
                        }
                        return ""
                    }
                },
                {
                    title: "Nguyên nhân/Lí do",
                    field: "reason",
                    width: "120px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"},
                    template: function (dataItem) {
                        switch (dataItem.reason) {
                        	case 'Khác' :
                        		return dataItem.reason+ " - " + dataItem.note;
                        	case null :
                        		return "";
                            default :
                            	return dataItem.reason;  
                        }
                        return ""
                    }
                },
                {
                    title: "Ngày gia hạn thêm(nếu có)",
                    field: "extendDate",
                    width: "100px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"}
                },
                {
                    title: "Thời gian tác động",
                    field: "createDateString",
                    width: "70px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:right;white-space:normal;"}
                },
                {
                    title: "Người tác động",
                    width: '70px',
                    field: "createUserName",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:right;white-space:normal;"}
                }
            ]
        });

    }
        
}
)();
