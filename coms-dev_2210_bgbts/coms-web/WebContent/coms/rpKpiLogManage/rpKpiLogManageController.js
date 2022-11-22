(function () {
    'use strict';
    var controllerId = 'rpKpiLogManageController';

    angular.module('MetronicApp').controller(controllerId, rpKpiLogManageController);

    function rpKpiLogManageController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, rpKpiLogManageService, htmlCommonService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http) {

        var vm = this;
        // variables
        vm.String = "Quản lý công trình > Báo cáo > Thống kê truy cập chức năng PXK A cấp";
        vm.searchForm = {};
        
        init();
        function init(){
        	fillDataTable([]);
        }
        
        vm.showHideColumn = function(column) {
            if (angular.isUndefined(column.hidden)) {
            	vm.kpiLogManageGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.kpiLogManageGrid.showColumn(column);
            } else {
                vm.kpiLogManageGrid.hideColumn(column);
            }
        };

        vm.gridColumnShowHideFilter = function(item) {
            return item.type == null || item.type !== 1;
        };
        
        var record = 0;
        function fillDataTable(data){
        	kendo.ui.progress($("#kpiLogManageId"),true);
            vm.kpiLogManageGridOptions = kendoConfig.getGridOptions({
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
                            '<div class="btn-group pull-right margin_top_button margin_right10">' +
                            '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                            '<i class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()" aria-hidden="true"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                            '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                            '<label ng-repeat="column in vm.kpiLogManageGrid.columns.slice(1, vm.kpiLogManageGrid.columns.length) | filter: vm.gridColumnShowHideFilter">' +
                            '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                            '</label>' +
                            '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $timeout(function () {
                                vm.count = response.total
                            });
                            return response.total;
                        },
                        data: function (response) {
                        	kendo.ui.progress($("#kpiLogManageId"),false);
                            var gridData = response.data;
                            return gridData;
                        }
                    },
                    transport: {
                        read: {
                            url: Constant.BASE_SERVICE_URL + "kpiLogRsService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page;
                            vm.searchForm.pageSize = options.pageSize;
                            return JSON.stringify(vm.searchForm)
                        }
                    },
                    pageSize: 10
                },
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
                        title: "TT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '80px',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Người truy cập",
                        field: 'sysUserName',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:left;height:44px;white-space: normal;"
                        }
                    },
                    {
                        title: "Email",
                        field: 'email',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        }
                    },
    				{
                        title: "Đơn vị",
                        field: 'sysGroupName',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        }
                    },
                    {
                        title: "Chức năng",
                        field: 'description',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        }
                    },
                    {
                        title: "Ngày truy cập",
                        field: 'submitDay',
                        width: '130px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        }
                    },
                    {
                        title: "Thời gian bắt đầu",
                        field: 'submitStartTime',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        }
                    },
                    {
                        title: "Thời gian kết thúc",
                        field: 'submitEndTime',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        }
                    },
                ]
            });
        }
        
        vm.openDepartmentPopup = function () {
            vm.deptType = 'search';
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null, null, vm, null, 'string', false, '92%', '89%');
        };

        // sysGroup autofill options
        vm.isSelectedDept = false;
        vm.deptOptions = {
            dataTextField: "text",
            placeholder: "Nhập mã hoặc tên đơn vị",
            select: function (e) {
                vm.isSelectedDept = true;
                var dataItem = this.dataItem(e.item.index());
                vm.searchForm.sysGroupText = dataItem.text;
                vm.searchForm.sysGroupId = dataItem.id;
                vm.searchForm.sysGroupName = dataItem.name;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.isSelectedDept = false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({
                            name: vm.searchForm.sysGroupText,
                            pageSize: vm.deptOptions.pageSize
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (!vm.isSelectedDept) {
                	vm.cancelInput('group');
                }
            },
            close: function (e) {
                if (!vm.isSelectedDept) {
                	vm.cancelInput('group');
                }
            },
            ignoreCase: false
        };
        
        vm.cancelInput = function(data){
        	if(data=="keySearch"){
        		vm.searchForm.keySearch = null;
        	}
        	if(data=="date"){
        		vm.searchForm.dateFrom = null;
        		vm.searchForm.dateTo = null;
        	}

        	if(data=="group"){
        		vm.searchForm.sysGroupText = null;
        		vm.searchForm.sysGroupId = null;
        		vm.searchForm.sysGroupName = null;
        	}
        	
        }
        
        vm.onSave = function(data){
        	vm.searchForm.sysGroupText = data.text;
        	vm.searchForm.sysGroupId = data.id;
        	vm.searchForm.sysGroupName = data.name;
        	CommonService.dismissPopup1();
        }
        
//        vm.exportFile = function(){
//        	kendo.ui.progress($("#kpiLogManageId"),true);
//        	vm.searchForm.page = null;
//        	vm.searchForm.pageSize = null;
//        	vm.listRemove = [];
//        	vm.listConvert = [];
//        	rpKpiLogManageService.doSearch(vm.searchForm).then(function(d){
//				CommonService.exportFile(vm.kpiLogManageGrid,d.data,vm.listRemove,vm.listConvert,"Báo cáo thống kê truy cập PXK A cấp");
//				kendo.ui.progress($("#kpiLogManageId"),false);
//			});
//        }
        
       vm.exportFile= function(){
    	   kendo.ui.progress($("#kpiLogManageId"),true);
              	return Restangular.all("kpiLogRsService/exportExcelDataLog").post(vm.searchForm).then(function (d) {
              	    var data = d.plain();
              	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
              	    kendo.ui.progress($("#kpiLogManageId"),false);
              	}).catch(function (e) {
              		kendo.ui.progress($("#kpiLogManageId"),false);
              	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
              	    return;
              	});
      }
        
        vm.doSearch = doSearch;
        function doSearch(){
        	var grid = vm.kpiLogManageGrid;
        	if(grid){
        		grid.dataSource.query({
        			page: 1,
        			pageSize: 10
        		});
        	}
        }
        
// end controller
    }
})();