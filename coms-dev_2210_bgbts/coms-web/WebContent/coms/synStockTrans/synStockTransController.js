//VietNT_19122018_created
(function () {
    'use strict';
    var controllerId = 'synStockTransController';

    angular.module('MetronicApp').controller(controllerId, synStockTransController);

    function synStockTransController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, synStockTransService, htmlCommonService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http) {

        var vm = this;
        // variables
        vm.String = "Quản lý công trình > Quản lý công trình > Điều phối PXK A cấp";
        var ERROR_MESSAGE = "Có lỗi xảy ra";
        vm.searchForm = {
            status: 1,
            isReceivedGoods: false,
            isReceivedObstruct: false
        };
        vm.listChecked = [];

        // label name html
        vm.htmlLabel = {
            orderCode: "Mã yêu cầu",
            code: "Mã phiếu xuất",
            constructionCode: "Mã công trình",
            sysGroupName: "Đơn vị nhận",
            dateFrom: "Ngày thực xuất từ",
            dateTo: "Đến",
            type: "Tình trạng"
        };
        vm.confirmDataList = [{
            id: 0,
            name: "Chờ tiếp nhận"
        },{
            id: 2,
            name: "Đã từ chối"
        }];

        initDataSearchForm();
        function initDataSearchForm() {
            // date field
            var today = new Date();
//            var from = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
            var from = new Date(today.getFullYear(), today.getMonth(), 1);
            vm.searchForm.dateTo = htmlCommonService.formatDate(today);
            vm.searchForm.dateFrom = htmlCommonService.formatDate(from);
        }

        // function
        vm.doSearch = doSearch;
        vm.cancel = cancel;
        function cancel() {
            CommonService.dismissPopup1();
        }

        function doSearch() {
            if ($('.k-invalid-msg').is(':visible')) {
                return;
            }
//            if (vm.disableSearch) {
//                return;
//            }
            var grid = vm.synStockTransGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        }

        // ----- Search form start
        vm.resetFormField = function (fieldName) {
            switch (fieldName) {
                case 'orderCode':
                    vm.searchForm.orderCode = null;
                    break;
                case 'code':
                    vm.searchForm.code = null;
                    break;
                case 'constructionCode':
                    vm.searchForm.constructionCode = null;
                    break;
                case 'date':
                    vm.searchForm.dateFrom = null;
                    vm.searchForm.dateTo = null;
                    $('.k-invalid-msg').hide();
//                    vm.disableSearch = false;
//                    vm.dateFromErr = null;
//                    vm.dateToErr = null;
                    break;
                case 'group':
                    vm.searchForm.sysGroupText = null;
                    vm.searchForm.sysGroupId = null;
                    break;
                case 'confirm':
                	vm.searchForm.confirm = null;
                	break;
            }
        };

        // --- sysGroup autofill start
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
                	vm.resetFormField('group');
                }
            },
            close: function (e) {
                if (!vm.isSelectedDept) {
                	vm.resetFormField('group');
                }
            },
            ignoreCase: false
        };
        // sysGroup autofill end ---
        // --- dateField start
        var validator = $("#realIeDateSynStockTrans").kendoValidator({
            rules: {
                // dateEmpty: function(input) {
                //     if (input.is("[data-role='datepicker']")) {
                //         if (input.val() === "" || input.val() === null) {
                //             return false;
                //         }
                //     }
                //     return true;
                // },
                dateValid: function(input) {
                    if (input.is("[data-role='datepicker']")) {
                    	if (!input.val()) {
                    		return true;
                    	}
                        var date = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                        if (!date) {
                            return false;
                        }
                    }
                    return true;
                },
                dateFrom: function(input) {
                    if (input.is("#syn_dateFrom")) {
                        var dateTo = kendo.parseDate($("#syn_dateTo").val(), 'dd/MM/yyyy');
                        var dateFrom = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                        if (!dateTo || !dateFrom) {
                            return true;
                        }
                        if (dateTo < dateFrom) {
                            return false;
                        }
                    }
                    return true;
                },
                dateTo: function(input) {
                    if (input.is("#syn_dateTo")) {
                        var dateTo = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                        var dateFrom = kendo.parseDate($("#syn_dateFrom").val(), 'dd/MM/yyyy');
                        if (!dateFrom || !dateTo) {
                            return true;
                        }
                        if (dateTo < dateFrom) {
                            return false;
                        }
                    }
                    return true;
                },
                dateIn31: function (input, a) {
                    if (input.is("#syn_dateFrom") || input.is("#syn_dateTo")) {
                        var dateTo = kendo.parseDate($("#syn_dateTo").val(), 'dd/MM/yyyy');
                        var dateFrom = kendo.parseDate($("#syn_dateFrom").val(), 'dd/MM/yyyy');
                        if (!dateTo || !dateFrom) {
                        	return true;
                        }
                        var dayDiff = Math.floor((dateTo - dateFrom) / (1000*60*60*24));
                        if (dayDiff > 31) {
                            return false;
                        }
                    }
                    return true;
                }
            },
            messages: {
                // dateEmpty: "Ngày không được bỏ trống",
                dateValid: "Ngày không hợp lệ",
                dateTo: "Chọn \"Đến ngày\" lớn hơn",
                dateFrom: "Chọn \"Từ ngày\" nhỏ hơn",
                dateIn31: "Chọn khoảng thời gian nhỏ hơn 31 ngày"
            }
        }).data("kendoValidator");

        /*
        vm.validateDateField = function (key) {
            var date;
            var element = $('#' + key).val();
            switch (key) {
                case 'dateFrom':
                    date = kendo.parseDate(element, "dd/MM/yyyy");
                    vm.dateFromErr = validateDate(date, element);
                    break;
                case 'dateTo':
                    date = kendo.parseDate(element, "dd/MM/yyyy");
                    vm.dateToErr = validateDate(date, element);
                    break;
                default:
                    vm.disableSearch = false;
                    break;
            }
        };

        function validateDate(date, element) {
            if (!!!element) {
                vm.disableSearch = false;
                return null;
            }
            if (date == null || date.getFullYear() > 9999 || date.getFullYear() < 1000) {
                vm.disableSearch = true;
                return 'Ngày không hợp lệ';
            } else {
                vm.disableSearch = false;
                return null;
            }
        }
        */
        // dateField end ---
        // Search form end -----
        
        // ----- Main Function start
        var listSynStockTrans = [];
        vm.doForwardGroup = function(dataItem) {
            vm.deptType = 'forward';

            // prepare data checkbox
            if (!!dataItem) {
                listSynStockTrans = [dataItem];
            } else if (vm.listChecked.length < 1) {
                toastr.error("Chọn ít nhất 1 yêu cầu!");
                return;
            } else {
                listSynStockTrans = angular.copy(vm.listChecked);
            }
            console.log(listSynStockTrans);

            //open popup choose user
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Chuyển đơn vị nhận");
            CommonService.populatePopupDept(templateUrl, title, null, null, vm, 'sst', 'string', false, '92%', '89%');
        };

        vm.onSave = function (sysGroupInfo) {
            if (vm.deptType === 'search') {
                // save searchFunction
                vm.searchForm.sysGroupText = sysGroupInfo.text;
                vm.searchForm.sysGroupId = sysGroupInfo.id;
            } else {
                var postData = {};
                // save forward sysGroup
                postData.sysGroupId = sysGroupInfo.id;
                postData.listToForward = listSynStockTrans;

                // prepare msg data
                var listCode = [];
                for (var i = 0; i < listSynStockTrans.length; i++) {
                    listCode.push(listSynStockTrans[i].code);
                }

                var message = 'Bạn muốn chuyển phiếu xuất kho: ' +
                    '<br>' +
                    '<span style="color: red;font-weight: bold;">"' + listCode.join('", "') + '"</span>' +
                    '<br><br>' +
                    ' sang đơn vị <span style="color: red;font-weight: bold;">"' + sysGroupInfo.text + '"</span> ?';

                // confirm box
                confirm(message,
                    function () {
                        //confirm
                        synStockTransService.doForwardGroup(postData)
                            .then(function (response) {
                                if (!!response.error) {
                                    toastr.error(response.error);
                                    return;
                                }
                                toastr.success("Chuyển đơn vị thành công!");
                                vm.listChecked = [];
                                cancel();
                                doSearch();
                            }, function (err) {
                                toastr.error(ERROR_MESSAGE);
                                console.log(err);
                            })
                        },
                    null,
                    null,
                    '400',
                    '180');
            }
        };

        vm.handleCheck = function(dataItem) {
            var index = indexInArraySst(vm.listChecked, dataItem);
            if (index === -1) {
                // vm.listChecked.push(dataItem);
                vm.listChecked.push(dataItem);
            } else {
                vm.listChecked.splice(index, 1);
            }
            console.log(vm.listChecked);
        };
        
        function indexInArraySst(arr, item) {
        	for (var i = 0; i < arr.length; i++) {
        		if (arr[i].synStockTransId === item.synStockTransId) {
        			return i;
        		}
        	}
        	return -1;
        }
        
        vm.selectAll = function() {
        	var dataList = $('#synStockTransGrid').data("kendoGrid").dataSource.data();
            var index;
            for (var i = 0; i < dataList.length; i++) {
                index = indexInArraySst(vm.listChecked, dataList[i]);
                if (index !== -1) {
                    vm.listChecked.splice(index, 1);
                }
                dataList[i].checked = vm.checkedAll;
                if (vm.checkedAll) {
                    vm.listChecked.push(dataList[i]);
                }
            }
        };
        // Main Function end -----

        // ----- viewDetail start
        vm.synStockTransView = true;
        covenantFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
            kendoConfig, $kWindow, synStockTransService,
            CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm, $http);

        vm.viewDetailPXK = viewDetailPXK;
        function viewDetailPXK(dataItem) {
            if (!!!dataItem.buss) {
                dataItem.buss = dataItem.bussinessTypeName;        	
            }
            vm.detaillPXK(dataItem);
        }
        // viewDetail end -----

        // ----- Main Grid start
        vm.exportFile = function() {
            vm.searchForm.page = null;
            vm.searchForm.pageSize = null;
            var data = vm.searchForm;
            var listRemove = [{
                title: "Thao tác"
            }, {
                title: "Chọn"
            }];

            Restangular.all(RestEndpoint.SYN_STOCK_TRANS_URL + "/doSearch").post(data).then(function (d) {
                var data = d.data;
                CommonService.exportFile(vm.synStockTransGrid, data, listRemove, [], "Danh sách PXK A cấp");
            });
        };

        vm.showHideColumn = function(column) {
            if (angular.isUndefined(column.hidden)) {
                vm.synStockTransGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.synStockTransGrid.showColumn(column);
            } else {
                vm.synStockTransGrid.hideColumn(column);
            }
        };

        vm.gridColumnShowHideFilter = function(item) {
            return item.type == null || item.type !== 1;
        };

        var record = 0;
        vm.synStockTransGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: true,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                vm.checkedAll = false;
            },
            reorderable: true,
            toolbar: [
                {
                    name: "actions",
                    template:
                        '<span><i class="fa fa-arrow-right" style="color: deepskyblue; position: relative; left: 26px; top: 2px; pointer-events: none; font-size: 19px;"></i></span>' +
                        '<button class="btn btn-qlk padding-search-right" ng-click="vm.doForwardGroup()" uib-tooltip="Chuyển đơn vị nhận" style="width: 190px;" translate>Chuyển đơn vị nhận</button>'
                        +
                        '<div class="btn-group pull-right margin_top_button margin_right10">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()" aria-hidden="true"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.synStockTransGrid.columns.slice(1, vm.synStockTransGrid.columns.length) | filter: vm.gridColumnShowHideFilter">' +
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
                        var gridData = response.data;
                        for (var i = 0; i < gridData.length; i++) {
                            for (var j = 0; j < vm.listChecked.length; j++) {
                                if (gridData[i].synStockTransId === vm.listChecked[j].synStockTransId) {
                                    gridData[i].checked = true;
                                }
                            }
                        }

                        return response.data;
                    }
                },
                transport: {
                    read: {
                        url: Constant.BASE_SERVICE_URL + RestEndpoint.SYN_STOCK_TRANS_URL + "/doSearch",
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
                    title: "TT",
                    field: "stt",
                    hidden: true
                },{
                    title: "Chọn",
                    field: "",
                    width: '40px',
                    columnMenu: false,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                    headerTemplate: function (dataItem) {
                        return "<input type='checkbox' id='checkAll' name='checkAll' ng-click='vm.selectAll()' ng-model='vm.checkedAll'/>";
                    },
                    template: function (dataItem) {
                        return "<input type='checkbox' id='childcheck' name='gridcheckbox' ng-model='dataItem.checked' ng-click='vm.handleCheck(dataItem)'/>";
                    }
                },
                {
                    title: "Mã yêu cầu",
                    field: 'orderCode',
                    width: '11%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;height:44px;"
                    }
                },
                {
                    title: "Mã phiếu xuất",
                    field: 'code',
                    width: '12%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        return "<a href='#' ng-click='vm.viewDetailPXK(dataItem)'>" + dataItem.code + "</a>";
                    }
                    // template: function (dataItem) {
                    //     return "<a href='#' ng-click='vm.viewDetail(dataItem)'>#code#</a>/>";
                    // }
                },
                {
                    title: "Mã trạm",
                    field: 'customField',
                    width: '6%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal;"
                    }
                },
				{
                    title: "Mã hợp đồng",
                    field: 'contractCode',
                    width: '12%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal;"
                    }
                },
                {
                    title: "Mã công trình",
                    field: 'constructionCode',
                    width: '12%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal;"
                    }
                },
                {
                    title: "Mã kho xuất",
                    field: 'stockCode',
                    width: '6%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                {
                    title: "Người nhận",
                    field: 'createdByName',
                    width: '11%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Đơn vị nhận",
                    field: 'sysGroupName',
                    width: '17%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal;"
                    }
                },
                {
                    title: "Tình trạng",
                    field: 'confirm',
                    width: '10%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal;"
                    },
                    template: function (dataItem) {
                        if (dataItem.confirm.trim() === '2') {
                            return 'Đã từ chối';
                        }
                        if (dataItem.confirm.trim() === '0') {
                            return 'Chờ tiếp nhận';
                        }
                        return null;
                    }
                },
                {
                    title: "Lý do từ chối",
                    field: 'confirmDescription',
                    width: '15%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal;"
                    }
                },
                {
                    title: "Thao tác",
                    width: '70px',
                    field: "action",
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        return '<div class="text-center">'
                            + '<button  style=" border: none; background-color: white;" id="sst_forwardGroup" ng-click="vm.doForwardGroup(dataItem)" ' +
                            'class="icon_table aria-hidden" ng-hide="dataItem.status === 1"' +
                            'uib-tooltip="Chuyển đơn vị nhận" translate>' +
                            '<i class="fa fa-arrow-right" style="color:deepskyblue"  ng-disabled="" aria-hidden="true"></i>' +
                            '</button>'
                            + '</div>';
                    }
                }
            ]
        });
        // Main Grid end -----
        // Controller end
    }
})();