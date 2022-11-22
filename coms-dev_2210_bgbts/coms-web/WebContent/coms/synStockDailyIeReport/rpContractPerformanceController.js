(function () {
    'use strict';
    var controllerId = 'rpContractPerformanceController';

    angular.module('MetronicApp').controller(controllerId, rpContractPerformanceController);

    function rpContractPerformanceController($scope, $templateCache, $rootScope, $timeout, gettextCatalog,
                                    kendoConfig, $kWindow, synStockDailyIeReportService, htmlCommonService,
                                    CommonService, PopupConst, Restangular, RestEndpoint, Constant) {

        var vm = this;

        //var
        var today;
        var dateToMax;
        vm.String = "Quản lý công trình > Báo cáo > Báo cáo thực hiện hợp đồng";
        vm.searchForm = {};
        vm.constructionStateList = [{
            "id": 0,
            "name": "Đang thực hiện"
        }, {
            "id": 3,
            "name": "Thi công xong"
        }];
        vm.disableSearch = false;

        // label name html
        vm.htmlLabel = {
            contract: "Số hợp đồng",
            dateFrom: "Thời gian từ ngày",
            dateTo: "Đến",
            sysGroup: "Đơn vị",
            province: "Tỉnh"
        };

        initDataSearchForm();
        function initDataSearchForm() {
            // date field
            today = new Date();
            dateToMax = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 1);
            var from = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
            vm.searchForm.dateTo = htmlCommonService.formatDate(dateToMax);
            vm.searchForm.dateFrom = htmlCommonService.formatDate(from);
        }

        function dateFieldNotValidated() {
            if ($('.k-invalid-msg').is(':visible')) {
                toastr.warning("Nhập ngày chốt số liệu trước khi thực hiện thao tác!");
                return true;
            }
            return false;
        }

        function doSearch() {
            if (dateFieldNotValidated()) {
                return;
            }
            var grid = vm.rpContractPerformanceGrid;
            if (grid) {
                grid.dataSource.read();
            }
        }

        //function
        vm.doSearch = doSearch;
        vm.showHideColumn = showHideColumn;
        vm.gridColumnShowHideFilter = gridColumnShowHideFilter;

        // ----- Search form start
        vm.resetFormField = resetFormField;
        function resetFormField(fieldName) {
            switch (fieldName) {
                case 'contract':
                    vm.searchForm.contractCode = null;
                    break;
                case 'province':
                    vm.searchForm.provinceId = null;
                    vm.searchForm.provinceCode = null;
                    break;
                case 'date':
                    vm.searchForm.dateFrom = null;
                    vm.searchForm.dateTo = null;
                    $timeout(function() {
                        validator.validate();
                    }, 25);
                    break;
                case 'group':
                    vm.searchForm.sysGroupText = null;
                    vm.searchForm.sysGroupId = null;
                    break;
                case 'state':
                    vm.searchForm.constructionState = null;
                    break;
                case 'filter':
                    vm.searchForm.filterSearch = 0;
            }
        }

        // --- sysGroup autofill start
        vm.openDepartmentPopup = function () {
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null, null, vm, null, 'string', false, '92%', '89%');
        };

        vm.onSave = function (sysGroupInfo) {
            vm.searchForm.sysGroupText = sysGroupInfo.text;
            vm.searchForm.sysGroupId = sysGroupInfo.id;
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
                    resetFormField('group');
                }
            },
            close: function (e) {
                if (!vm.isSelectedDept) {
                    resetFormField('group');
                }
            },
            ignoreCase: false
        };
        // sysGroup autofill end ---

        // --- province start
        vm.openCatProvincePopup = openCatProvincePopup;
        vm.onSaveCatProvince = onSaveCatProvince;
        vm.clearProvince = clearProvince;

        function openCatProvincePopup() {
            var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm tỉnh");
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions, data, vm, 'fff', 'ggfd', false, '85%', '85%', 'catProvinceSearchController');
        }

        function onSaveCatProvince(data) {
            vm.searchForm.provinceId = data.catProvinceId;
            vm.searchForm.provinceCode = data.code;
            htmlCommonService.dismissPopup();
        }

        function clearProvince() {
            vm.searchForm.provinceId = null;
            vm.searchForm.provinceCode = null;
        }

        vm.isSelectProvince = false;
        vm.provinceOptions = {
            dataTextField: "code",
            placeholder: "Nhập mã hoặc tên tỉnh",
            select: function (e) {
                vm.isSelectProvince = true;
                var dataItem = this.dataItem(e.item.index());
                vm.searchForm.provinceId = dataItem.catProvinceId;
                vm.searchForm.provinceCode = dataItem.code;
            },
            pageSize: 10,
            open: function (e) {
                vm.isSelectProvince = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.isSelectProvince = false;
                        return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
                            name: vm.searchForm.provinceCode,
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
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-6 text-header-auto border-right-ccc">Mã tỉnh</p>' +
                '<p class="col-md-6 text-header-auto">Tên tỉnh</p>' +
                '</div>',
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (!vm.isSelectProvince) {
                    resetFormField('province');
                }
            },
            close: function (e) {
                if (!vm.isSelectProvince) {
                    resetFormField('province');
                }
            }
        };
        // province end ---
        // --- contract start
        vm.openContractPopup = openContractPopup;
        vm.onSaveContract = onSaveContract;

        function openContractPopup() {
            var templateUrl = 'coms/popup/findContractPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm hợp đồng");
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,null, vm, 'fff', 'ggfd', false, '85%','85%','contractSearchController');
        }

        function onSaveContract(data) {
            vm.searchForm.contractCode = data.code;
            htmlCommonService.dismissPopup();
        }

        vm.isSelectContract = false;
        vm.contractOptions = {
            dataTextField: "code",
            placeholder: "Mã hợp đồng",
            open: function(e) {
                vm.isSelectContract = false;
            },
            select: function(e) {
                vm.isSelectContract = true;
                var data = this.dataItem(e.item.index());
                vm.searchForm.contractCode = data.code;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function(options) {
                        vm.isSelectContract = false;
                        return Restangular.all(RestEndpoint.CNT_CONTRACT_SERVICE_URL + "/cntContract/doSearch").post({
                            isSize: true,
                            code: vm.searchForm.contractCode,
                            contractType: 0
                        }).then(function(response){
                            options.success(response.data);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-12 text-header-auto">Mã hợp đồng</p>' +
                '</div>',
            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.code #</div></div>',
            change: function(e) {
                if (!vm.isSelectContract) {
                    vm.searchForm.cntContractCode = null;
                }
            },
            close: function (e) {
                if (!vm.isSelectContract) {
                    vm.searchForm.cntContractCode = null;
                }
            }
        };

        vm.isSelectProvince = false;
        vm.provinceOptions = {
            dataTextField: "code",
            placeholder: "Nhập mã hoặc tên tỉnh",
            select: function (e) {
                vm.isSelectProvince = true;
                var dataItem = this.dataItem(e.item.index());
                vm.searchForm.provinceCode = dataItem.code;
            },
            pageSize: 10,
            open: function (e) {
                vm.isSelectProvince = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.isSelectProvince = false;
                        return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
                            name: vm.searchForm.provinceCode,
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
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-6 text-header-auto border-right-ccc">Mã tỉnh</p>' +
                '<p class="col-md-6 text-header-auto">Tên tỉnh</p>' +
                '</div>',
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div></div>',
            change: function (e) {
                if (!vm.isSelectProvince) {
                    resetFormField('province');
                }
            },
            close: function (e) {
                if (!vm.isSelectProvince) {
                    resetFormField('province');
                }
            }
        };
        // contract end ---
        // --- dateField start
        var validator = $("#realIeTransDate-rpContractPerformance").kendoValidator({
            rules: {
                dateEmpty: function(input) {
                    if (input.is("[data-role='datepicker']")) {
                        if (!input.val() || input.val() === "" || input.val() === null) {
                            return false;
                        }
                    }
                    return true;
                },
                dateValid: function(input) {
                    if (input.is("[data-role='datepicker']")) {
                        var date = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                        if (!date) {
                            return false;
                        }
                    }
                    return true;
                },
                dateFrom: function(input) {
                    if (input.is("#rpcp_dateFrom")) {
                        var dateTo = kendo.parseDate($("#rpcp_dateTo").val(), 'dd/MM/yyyy');
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
                    if (input.is("#rpcp_dateTo")) {
                        var dateTo = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                        var dateFrom = kendo.parseDate($("#rpcp_dateFrom").val(), 'dd/MM/yyyy');
                        if (!dateFrom || !dateTo) {
                            return true;
                        }
                        if (dateTo < dateFrom) {
                            return false;
                        }
//                        if (dateTo > dateToMax) {
//                        	input.attr("data-dateTo-msg", "Chọn ngày đến nhỏ hơn \"" + kendo.toString(dateToMax, "dd/MM/yyyy") + "\"");
//                        	return false;
//                        }
                    }
                    return true;
                }
            },
            messages: {
                dateEmpty: "Ngày không được bỏ trống",
                dateValid: "Ngày không hợp lệ",
                dateTo: "Chọn \"Đến ngày\" lớn hơn",
                dateFrom: "Chọn \"Từ ngày\" nhỏ hơn"
            }
        }).data("kendoValidator");

        // dateField end ---
        // Search form end -----

        vm.exportExcel = function () {
            if (dateFieldNotValidated()) {
                return;
            }
            var element = $(".tab-content");
            kendo.ui.progress(element, true);

        	vm.searchForm.page = null;
        	vm.searchForm.pageSize = null;
            synStockDailyIeReportService.exportContractPerformance(vm.searchForm).then(function (response) {
                kendo.ui.progress(element, false);
                if (!!response.error) {
                    toastr.error(response.error);
                    return;
                }
                var data = response.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function (e) {
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                kendo.ui.progress(element, true);
            });
        };

        vm.numberWithCommas = numberWithCommas;
        function numberWithCommas(x) {
            if(!!!x){
                return '0';
            }
            var parts = parseFloat(x).toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
        }

// show hide column
        function showHideColumn(column) {
            if (angular.isUndefined(column.hidden)) {
                vm.rpContractPerformanceGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.rpContractPerformanceGrid.showColumn(column);
            } else {
                vm.rpContractPerformanceGrid.hideColumn(column);
            }
        }

        function gridColumnShowHideFilter(item) {
            return item.type == null || item.type !== 1;
        }

// ---------- Grid Opts

//main grid
        var record = 0;
        vm.rpContractPerformanceGridOptions = kendoConfig.getGridOptions({
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
                        '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.rpContractPerformanceGrid.columns | filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column, 1)"> {{column.title}}' +
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
                            vm.count = response.total
                        });
                        return response.total;
                    },
                    data: function (response) {
                        return response.data;
                    }
                },
                transport: {
                    read: {
                        url: Constant.BASE_SERVICE_URL + RestEndpoint.SYN_STOCK_DAILY_IE_URL + "/doSearchContractPerformance",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
//                    read: function (options) {
//                        options.success(vm.testDataGrid);
//                    },
                    parameterMap: function (options, type) {
                        vm.searchForm.page = options.page;
                        vm.searchForm.pageSize = options.pageSize;
                        if (vm.searchForm.dateTo === null || vm.searchForm.dateTo === "") {
                            vm.searchForm.dateTo = dateToMax.getDate() + "/" + (dateToMax.getMonth() + 1) + "/" + dateToMax.getFullYear();
                        }
                        return JSON.stringify(vm.searchForm)
                    }
                },
                aggregate: [
                    { field: "countTotal", aggregate: "sum" },
                    { field: "countDone", aggregate: "sum" },
                    { field: "countConstructing", aggregate: "sum" },
                    { field: "countPending", aggregate: "sum" },
                    { field: "countCancel", aggregate: "sum" },
                    { field: "contractCode", aggregate: "count" },
                    { field: "sumDone", aggregate: "sum" },
                    { field: "sumConstructing", aggregate: "sum" },
                    { field: "sumPending", aggregate: "sum" },
                    { field: "sumCancel", aggregate: "sum" }
                ],
                group: {
                    field: "provinceName",
                    aggregates: [
                        { field: "countTotal", aggregate: "sum" },
                        { field: "countDone", aggregate: "sum" },
                        { field: "countConstructing", aggregate: "sum" },
                        { field: "countPending", aggregate: "sum" },
                        { field: "countCancel", aggregate: "sum" },
                        { field: "contractCode", aggregate: "count" },
                        { field: "sumDone", aggregate: "sum" },
                        { field: "sumConstructing", aggregate: "sum" },
                        { field: "sumPending", aggregate: "sum" },
                        { field: "sumCancel", aggregate: "sum" }
                    ]},
                pageSize: 10
            },
            dataBound: function(e){
                var provinceGroup = e.element.find(".k-grouping-row td:first-child");
                provinceGroup.attr("colspan", 3);
                var groupHeaderRow = e.element.find(".k-grouping-row td");
                groupHeaderRow.css({
                	"border-top": "1px dotted black",
                	"border-bottom": "1px thin black",
                });
                var footerRow = e.element.find(".k-footer-template td");
                footerRow.css("border-top", "1px solid black");
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
                    width: "50px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;"},
                    template: function (dataItem) {
                        return ++record;
                    }
                },{
                    title: "Mã hợp đồng",
                    field: "contractCode",
                    width: "200px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;"},
                    footerTemplate: function() {
                        return "Tổng";
                    }
                },{
                    title: "Tổng công trình (công trình)",
                    field: "countTotal",
                    width: "80px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:right;"},
                    footerTemplate: function(data) {
                    	return data.countTotal.sum;
                    }
                },{
                    title: "Công trình thi công xong (công trình)",
                    field: "countDone",
                    width: "80px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:right;"},
                    footerTemplate: function(data) {
                    	return data.countDone.sum;
                    }
                },{
                    title: "Công trình đang thi công (công trình)",
                    field: "countConstructing",
                    width: "80px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:right;"},
                    footerTemplate: function(data) {
                    	return data.countConstructing.sum;
                    }
                },{
                    title: "Công trình chưa thi công (công trình)",
                    field: "countPending",
                    width: "80px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:right;"},
                    footerTemplate: function(data) {
                    	return data.countPending.sum;
                    }
                },{
                    title: "Công trình hủy (công trình)",
                    field: "countCancel",
                    width: "80px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:right;"},
                    footerTemplate: function(data) {
                    	return data.countCancel.sum;
                    }
                },{
                    title: "Tổng hợp tình trạng triển khai (Thi công xong/ đang thi công)",
                    field: "constructionStateName",
                    width: "120px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;"},
                    template: function (dataItem) {
                        return dataItem.countTotal === dataItem.countDone ? "Thi công xong" : "Đang thi công";
                    },
                    footerAttributes: {style: "text-align:center;white-space:normal;"},
                    footerTemplate: function(data) {
                    	return data.contractCode.count;
                    }
                },{
                    title: "Giá trị công nợ theo từng trạng thái (đồng)",
                    headerAttributes: {style: "text-align:center;white-space:normal;font-weight: bold;"},
                    attributes: {style: "text-align:center;"},
                    columns: [
                        {
                            title: "Giá trị công nợ công trình thi công xong",
                            field: "sumDone",
                            width: "120px",
                            headerAttributes: {style: "text-align:center;white-space:normal;"},
                            attributes: {style: "text-align:right;"},
                            template : function(data) {
                            	return numberWithCommas(data.sumDone);
                            },
                            footerTemplate: function(data) {
                                return numberWithCommas(data.sumDone.sum);
                            }
                        },{
                            title: "Giá trị công nợ công trình đang thi công",
                            field: "sumConstructing",
                            width: "120px",
                            headerAttributes: {style: "text-align:center;white-space:normal;"},
                            attributes: {style: "text-align:right;"},
                            template : function(data) {
                            	return numberWithCommas(data.sumConstructing);
                            },
                            footerTemplate: function(data) {
                                return numberWithCommas(data.sumConstructing.sum);
                            }
                        },{
                            title: "Giá trị công nợ công trình chưa thi công",
                            field: "sumPending",
                            width: "120px",
                            headerAttributes: {style: "text-align:center;white-space:normal;"},
                            attributes: {style: "text-align:right;"},
                            template : function(data) {
                            	return numberWithCommas(data.sumPending);
                            },
                            footerTemplate: function(data) {
                                return numberWithCommas(data.sumPending.sum);
                            }
                        },{
                            title: "Giá trị công nợ công trình hủy",
                            field: "sumCancel",
                            width: "120px",
                            headerAttributes: {style: "text-align:center;white-space:normal;"},
                            attributes: {style: "text-align:right;"},
                            template : function(data) {
                            	return numberWithCommas(data.sumCancel);
                            },
                            footerTemplate: function(data) {
                                return numberWithCommas(data.sumCancel.sum);
                            }
                        }
                    ]
                },{
                    title: "Tỉnh",
                    field: "provinceName",
                    hidden: true,
                    headerAttributes: {style: "text-align:center;"},
                    attributes: {style: "text-align:center;"},
                    groupHeaderTemplate: function (data) {
                    	return data.value + "</td>"
                            + "<td style='text-align:right;'>" + data.aggregates.countTotal.sum + "</td>"
            		        + "<td style='text-align:right;'>" + data.aggregates.countDone.sum + "</td>"
            		        + "<td style='text-align:right;'>" + data.aggregates.countConstructing.sum + "</td>"
            		        + "<td style='text-align:right;'>" + data.aggregates.countPending.sum + "</td>"
            		        + "<td style='text-align:right;'>" + data.aggregates.countCancel.sum + "</td>"
            		        + "<td style='text-align:center;'>" + data.aggregates.contractCode.count + "</td>"
            		        + "<td style='text-align:right;'>" + numberWithCommas(data.aggregates.sumDone.sum) + "</td>"
            		        + "<td style='text-align:right;'>" + numberWithCommas(data.aggregates.sumConstructing.sum) + "</td>"
            		        + "<td style='text-align:right;'>" + numberWithCommas(data.aggregates.sumPending.sum) + "</td>"
            		        + "<td style='text-align:right;padding-right: 0px;'>" + numberWithCommas(data.aggregates.sumCancel.sum);
                    }
                }
            ]
        });
// end
    }
})
();