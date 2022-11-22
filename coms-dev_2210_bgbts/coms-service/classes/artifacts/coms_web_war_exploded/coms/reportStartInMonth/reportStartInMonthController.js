(function () {
    'use strict';
    var controllerId = 'reportStartInMonthController';

    angular.module('MetronicApp').controller(controllerId, reportStartInMonthController);

    function reportStartInMonthController($scope, $templateCache, $rootScope, $timeout, gettextCatalog,
                                      kendoConfig, $kWindow, reportStartInMonthService, htmlCommonService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant) {

        var vm = this;
        vm.String = "Quản lý công trình > Báo cáo > Báo cáo khởi công trong tháng";
        vm.searchForm = {
            status: 1
        };
        
        init();
        function init(){
        	var date = new Date();
        	var start = new Date(date.getFullYear(), date.getMonth(), 1);
        	var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
        	vm.searchForm.dateTo = htmlCommonService.formatDate(end);
        	vm.searchForm.dateFrom = htmlCommonService.formatDate(start);
        	fillDataTable([]);
        	reportStartInMonthService.getconstructionType().then(function (data) {
                 vm.catConstructionTypeDataList = data;
             });
        }
 
        vm.showHideColumn = showHideColumn;
        function showHideColumn(column) {
            if (angular.isUndefined(column.hidden)) {
                vm.reportInMonthGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.reportInMonthGrid.showColumn(column);
            } else {
                vm.reportInMonthGrid.hideColumn(column);
            }
        }

        vm.gridColumnShowHideFilter = gridColumnShowHideFilter;
        function gridColumnShowHideFilter(item) {
            return item.type == null || item.type !== 1;
        }
        
        var record = 0;
        function fillDataTable(data){
        vm.reportInMonthGridOptions = kendoConfig.getGridOptions({
            autoBind : true,
            scrollable : true,
            resizable : true,
            editable : false,
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
                        '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.reportInMonthGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                }
            ],
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                        $timeout(function() {vm.count = response.total});
                        return response.total;
                    },
                    data: function (response) {
                        return response.data;
                    }
                },
                transport: {
                    read: {
                        url: Constant.BASE_SERVICE_URL + RestEndpoint.ASSIGN_HANDOVER_SERVICE_URL + "/getDataReportStartInMonth",
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
            //TODO check field names, resize
            columns: [
                {
                    title: "TT",
                    field: "stt",
                    width: '50px',
                    template: function () {
                        return ++record;
                    },
                    columnMenu: false,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                {
                    title: "Đơn vị",
                    field: 'sysGroupName',
                    width: '300px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Tỉnh",
                    field: 'catProvinceCode',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                {
                    title: "Mã nhà trạm",
                    field: 'catStationHouseCode',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Mã trạm",
                    field: 'catStationCode',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Mã CT",
                    field: 'constructionCode',
                    width: '220px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Hợp đồng",
                    field: 'cntContractCode',
                    width: '280px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Loại CT",
                    field: 'constructTypeName',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal"
                    },
                },
                {
                    title: "Ngày giao mặt bằng",
                    field: 'companyAssignDateConvert',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                },
                {
                    title: "Ngày nhận mặt bằng",
                    field: 'handoverDateConvert',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;white-space: normal"
                    },

                },
                {
                    title: "Ngày khởi công",
                    field: 'startDateConvert',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;white-space: normal"
                    },
                },
    ]
    });
 }   
        
      //pop up for SysGroupField
        vm.openDepartmentTo = openDepartmentTo;
        function openDepartmentTo(popUpId) {
            vm.obj = {};
            vm.popupId = popUpId;
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUpId, 'string', false, '92%', '89%');
        }

        // grid options for field SysGroup
        vm.deptOptions = {
            dataTextField: "text",
            placeholder: "Nhập mã hoặc tên đơn vị",
            select: function (e) {
                var dataItem = this.dataItem(e.item.index());
                vm.searchForm.sysGroupText = dataItem.text;
                vm.searchForm.sysGroupId = dataItem.id;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
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
                if (e.sender.value() === '') {
                    vm.searchForm.sysGroupText = null;
                    vm.searchForm.sysGroupId = null;
                }
            },
            ignoreCase: false
        };
        
        vm.onSave = onSave;
        function onSave(data) {
            switch (vm.popupId) {
                case 'search':
                    vm.searchForm.sysGroupText = data.code + '-' + data.name;
                    vm.searchForm.text = data.code + '-' + data.name;
                    vm.searchForm.sysGroupId = data.id;
                    break;
                case 'add':
                    vm.isSelectDeptAdd = true;
                    vm.assignHandover.sysGroupText = data.code + '-' + data.name;
                    vm.assignHandover.sysGroupCode = data.code;
                    vm.assignHandover.sysGroupName = data.name;
                    vm.assignHandover.sysGroupId = data.id;
                    vm.validateAddDept();
                    break;
            }
        }
        
        vm.resetFormFieldConsType = resetFormFieldConsType;
        vm.resetFormFieldDate = resetFormFieldDate;
        vm.resetFormFieldSysGroup = resetFormFieldSysGroup;
        
        function resetFormFieldConsType() {
            vm.searchForm.listCatConstructionType = null;
        }

        function resetFormFieldDate() {
            vm.searchForm.dateFrom = null;
            vm.searchForm.dateTo = null;
            vm.disableSearch = false;
            vm.errMessage = null;
            vm.errMessage1 = null;
            vm.errMessage2 = null;
        }

        function resetFormFieldSysGroup() {
            vm.searchForm.sysGroupId = null;
            vm.searchForm.sysGroupName = null;
            vm.searchForm.sysGroupCode = null;
            vm.searchForm.sysGroupText = null;
        }

        vm.clearSearchDate = function(){
            vm.searchForm.dateFrom = null;
            vm.searchForm.dateTo = null;
            vm.errMessage1 = null;
            vm.errMessage = null;
        }
        
        // validate date man hinh tim kiem
        vm.validateDate = validateDate;
        function validateDate(){
            vm.errMessage = null;
            vm.errMessage1 = null;

            vm.checkStartDate = false;
            vm.checkEndDate = false;

            var startDate = $('#dateFrom').val();
            var endDate = $('#dateTo').val();

            // kiem tra field nao dc nhap
            if(startDate !== "") {
                if(kendo.parseDate(startDate,"dd/MM/yyyy") == null){
                    vm.errMessage1 = "Ngày bắt đầu không hợp lệ";
                    $("#dateFrom").focus();
                    vm.checkStartDate = false;
                }else if(kendo.parseDate(startDate,"dd/MM/yyyy").getFullYear() > 2099 || kendo.parseDate(startDate,"dd/MM/yyyy").getFullYear() < 1900){
                    vm.errMessage1 = 'Ngày bắt đầu không hợp lệ';
                    $("#dateFrom").focus();
                    vm.checkStartDate = false;
                } else {
                    vm.errMessage1 = null;
                    vm.checkStartDate = true;
                }
            }

            if(endDate !== "") {
                if ( kendo.parseDate(endDate,"dd/MM/yyyy") == null) {
                    vm.errMessage = "Ngày kết thúc không hợp lệ";
                    $("#dateTo").focus();
                    vm.checkEndDate = false;
                }else if(kendo.parseDate(endDate,"dd/MM/yyyy").getFullYear() > 2099 || kendo.parseDate(endDate,"dd/MM/yyyy").getFullYear() < 1900){
                    vm.errMessage = 'Ngày kết thúc không hợp lệ';
                    $("#dateTo").focus();
                    vm.checkEndDate = false;
                } else {
                    vm.errMessage = null;
                    vm.checkEndDate = true;
                }
            }
            if(!!vm.checkStartDate && !!vm.checkEndDate) {
                if(kendo.parseDate(startDate,"dd/MM/yyyy") > kendo.parseDate(endDate,"dd/MM/yyyy")) {
                    vm.errMessage1 = 'Giá trị Từ ngày phải nhỏ hơn hoặc bằng giá trị Đến ngày';
                    $("#workItemSearchDateFrom").focus();
                }else {
                    vm.errMessage = null;
                    vm.errMessage1 = null;
                }
                return;
            }
        }
        
        vm.doSearch = doSearch;
        function doSearch(){
        	var grid = vm.reportInMonthGrid;
        	if(grid){
        		grid.dataSource.query({
        			page: 1,
        			pageSize: 10
        		});
        	}
        	grid.refresh();
        }
        
        var listRemove = [{}];
        var listConvert = [{}];
        
        vm.exportFile = exportFile;
        function exportFile() {
            vm.searchForm.page = null;
            vm.searchForm.pageSize = null;
            var data = vm.searchForm;

            Restangular.all("assignHandoverService" + "/getDataReportStartInMonth").post(data).then(function(d) {
                var data = d.data;
//                for (var i = 0; i < data.length; i++) {
//                    data[i].companyAssignDate = !!data[i].companyAssignDate
//                        ? kendo.toString(kendo.parseDate(data[i].companyAssignDate), 'dd/MM/yyyy')
//                        : "";
//                }
                CommonService.exportFile(vm.reportInMonthGrid, d.data, listRemove, listConvert, "Báo cáo khởi công trong tháng");
            });
        }
     //end controller
}
})();