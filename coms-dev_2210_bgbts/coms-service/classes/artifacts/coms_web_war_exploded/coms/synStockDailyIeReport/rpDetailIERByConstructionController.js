(function () {
    'use strict';
    var controllerId = 'rpDetailIERByConstructionController';

    angular.module('MetronicApp').controller(controllerId, rpDetailIERByConstructionController);

    function rpDetailIERByConstructionController($scope, $templateCache, $rootScope, $timeout, gettextCatalog,
                                    kendoConfig, $kWindow, synStockDailyIeReportService, htmlCommonService,
                                    CommonService, PopupConst, Restangular, RestEndpoint, Constant) {
        var vm = this;

        //var
        var today;
        var dateToMax;
        vm.String = "Quản lý công trình > Báo cáo > Báo cáo chi tiết nhập - xuất - tồn vật tư A cấp theo mã công trình";
        vm.searchForm = {};
        vm.constructionStateList = [{
            "id": 0,
            "name": "Đang thực hiện"
        }, {
            "id": 3,
            "name": "Thi công xong"
        }];
        vm.disableSearchDetail = false;

        // label name html
        vm.htmlLabel = {
            contract: "Số hợp đồng",
            dateFrom: "Thời gian từ ngày",
            dateTo: "Đến",
            sysGroup: "Đơn vị",
            province: "Tỉnh",
            goods: "Hàng hóa"
        };

        //VietNT_20190301_start
        vm.sumHolder = {};
        vm.lastSearch = {};
        initSumValue();
        function initSumValue() {
            vm.sumHolder.numberTonDauKy = 0;
            vm.sumHolder.totalMoneyTonDauKy = 0;
            vm.sumHolder.numberNhapTrongKy = 0;
            vm.sumHolder.totalMoneyNhapTrongKy = 0;
            vm.sumHolder.numberNghiemThuDoiSoat4A = 0;
            vm.sumHolder.totalMoneyNghiemThuDoiSoat4A = 0;
            vm.sumHolder.numberTraDenBu = 0;
            vm.sumHolder.totalMoneyTraDenBu = 0;
            vm.sumHolder.numberTonCuoiKy = 0;
            vm.sumHolder.totalMoneyTonCuoiKy = 0;
        }

        initDataSearchForm();
        function initDataSearchForm() {
            // date field
            today = new Date();
            dateToMax = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 1);
            var from = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
            vm.searchForm.dateTo = htmlCommonService.formatDate(dateToMax);
            vm.searchForm.dateFrom = htmlCommonService.formatDate(from);

            doSearchWithSum();
            vm.lastSearch = angular.copy(vm.searchForm);
        }

        function isSameSearch() {
            return (vm.searchForm.sysGroupText === vm.lastSearch.sysGroupText
                && vm.searchForm.dateFrom === vm.lastSearch.dateFrom
                && vm.searchForm.dateTo === vm.lastSearch.dateTo
                && vm.searchForm.provinceCode === vm.lastSearch.provinceCode
                && vm.searchForm.goodsCode === vm.lastSearch.goodsCode);
        }

        vm.doSearchNoSum = function() {
            var grid = vm.rpDetailIERByConstructionGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
                grid.dataSource.read();
                grid.refresh();
            }
        };

        function doSearchWithSum() {
            console.log("dosearch with sum");
            // reinit sum value
            initSumValue();

            // make copy of search obj
            var obj = angular.copy(vm.searchForm);
            // set no page size to get all data
            obj.page = null;
            obj.pageSize = null;

            Restangular.all("synStockDailyIeService/doSearchRpDetailIERByConstructionCode").post(obj).then(function (d) {
                var data = d.data;
                $.each(data, function (index, record) {
                    vm.sumHolder.numberTonDauKy += record.numberTonDauKy;
                    vm.sumHolder.totalMoneyTonDauKy += record.totalMoneyTonDauKy;
                    vm.sumHolder.numberNhapTrongKy += record.numberNhapTrongKy;
                    vm.sumHolder.totalMoneyNhapTrongKy += record.totalMoneyNhapTrongKy;
                    vm.sumHolder.numberNghiemThuDoiSoat4A += record.numberNghiemThuDoiSoat4A;
                    vm.sumHolder.totalMoneyNghiemThuDoiSoat4A += record.totalMoneyNghiemThuDoiSoat4A;
                    vm.sumHolder.numberTraDenBu += record.numberTraDenBu;
                    vm.sumHolder.totalMoneyTraDenBu += record.totalMoneyTraDenBu;
                    vm.sumHolder.numberTonCuoiKy += record.numberTonCuoiKy;
                    vm.sumHolder.totalMoneyTonCuoiKy += record.totalMoneyTonCuoiKy;
                });

                vm.doSearchNoSum();
            });
        }

        vm.doSearch = doSearch;
        function doSearch() {
            if (dateFieldNotValidated()) {
                return;
            }
            if (!isSameSearch()) {
                // save to compare next time
                vm.lastSearch = angular.copy(vm.searchForm);
                doSearchWithSum();
            } else {
                vm.doSearchNoSum();
            }
        }
        //VietNT_end

        function dateFieldNotValidated() {
            if ($('.k-invalid-msg').is(':visible')) {
                toastr.warning("Nhập ngày chốt số liệu trước khi thực hiện thao tác!");
                return true;
            }
            return false;
        }

        //function
        vm.doSearch = doSearch;
        vm.showHideColumn = showHideColumn;
        vm.gridColumnShowHideFilter = gridColumnShowHideFilter;

        // ----- Search form start
        vm.resetFormField = resetFormField;
        function resetFormField(fieldName) {
            switch (fieldName) {
                case 'date':
//                	initDataSearchForm();
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
                case 'goods':

                    break;
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
        // --- goods autofill start
        vm.template = '<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;float:left">#: data.code #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.name #</div> </div>';
        vm.headerTemplate = '<div class="dropdown-header row text-center k-widget k-header">' +
            '<p class="col-md-6 text-header-auto border-right-ccc">Mã</p>' +
            '<p class="col-md-6 text-header-auto">Tên</p>' +
            '</div>';
        vm.commonSearch = { name: '', code: '', page: 1, pageSize: 10 };

        vm.openGoodsPopup = function () {
            var templateUrl = 'wms/popup/findGoodsPopup.html';
            var title = gettextCatalog.getString("Tìm kiếm hàng hóa");
            CommonService.populatePopupCreate(templateUrl, title, null, vm, null, null, '92%', '89%');
        };

        vm.gridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            reorderable: true,
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
                        url: Constant.BASE_SERVICE_URL + "goodsRsServiceRest" + "/getForAutoCompleteGoods",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
//                    read: function (options) {
//                        options.success(vm.testDataGrid);
//                    },
                    parameterMap: function (options, type) {
                        vm.searchForm.page = options.page;
                        vm.searchForm.pageSize = options.pageSize;
                        return JSON.stringify(vm.searchForm);
                    }
                },
                pageSize: 10
            },
            dataBound: function(e){
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
                    title: "Mã công trình",
                    field: "constructionCode",
                    width: "200px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;"}
                }
            ]
        });

        vm.onSave = function (sysGroupInfo) {
            vm.searchForm.sysGroupText = sysGroupInfo.text;
            vm.searchForm.sysGroupId = sysGroupInfo.id;
        };

        // goods autofill end ---
        // --- dateField start
        var validator = $("#realIeTransDate-rpDetailIER").kendoValidator({
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
                    if (input.is("#rpIERC_dateFrom")) {
                        var dateTo = kendo.parseDate($("#rpIERC_dateTo").val(), 'dd/MM/yyyy');
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
                    if (input.is("#rpIERC_dateTo")) {
                        var dateTo = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                        var dateFrom = kendo.parseDate($("#rpIERC_dateFrom").val(), 'dd/MM/yyyy');
                        if (!dateFrom || !dateTo) {
                            return true;
                        }
                        if (dateTo < dateFrom) {
                        	input.attr("data-dateTo-msg", "Chọn \"Đến ngày\" lớn hơn");
                            return false;
                        }
                        if (dateTo > dateToMax) {
                        	input.attr("data-dateTo-msg", "Chọn \"Đến ngày\" nhỏ hơn ngày hôm nay");
                        	return false;
                        }
                    }
                    return true;
                }
            },
            messages: {
                dateEmpty: "Ngày không được bỏ trống",
                dateValid: "Ngày không hợp lệ",
//                dateTo: "Chọn \"Đến ngày\" lớn hơn",
                dateFrom: "Chọn \"Từ ngày\" nhỏ hơn",
                dateIn31: "Chọn khoảng thời gian nhỏ hơn 31 ngày"
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

            vm.searchForm.pageSize = null;
            vm.searchForm.page = null;
            synStockDailyIeReportService.exportDetailIERGoods(vm.searchForm).then(function (response) {
                kendo.ui.progress(element, false);
                if (!!response.error) {
                    toastr.error(response.error);
                    return;
                }
                var data = response.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function (e) {
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                kendo.ui.progress(element, false);
            });
        };

        vm.numberWithCommas = numberWithCommas
        function numberWithCommas(x) {
            if(!!!x){
                return '0.00';
            }
            var parts = parseFloat(x).toFixed(2).toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
        }

// show hide column
        function showHideColumn(column) {
            if (angular.isUndefined(column.hidden)) {
                vm.rpDetailIERByConstructionGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.rpDetailIERByConstructionGrid.showColumn(column);
            } else {
                vm.rpDetailIERByConstructionGrid.hideColumn(column);
            }
        }

        function gridColumnShowHideFilter(item) {
            return item.type == null || item.type !== 1;
        }

// ---------- Grid Opts

//main grid
        var record = 0;
        vm.rpDetailIERByConstructionGridOptions = kendoConfig.getGridOptions({
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
                        '<label ng-repeat="column in vm.rpDetailIERByConstructionGrid.columns | filter: vm.gridColumnShowHideFilter">' +
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
                        url: Constant.BASE_SERVICE_URL + RestEndpoint.SYN_STOCK_DAILY_IE_URL + "/doSearchRpDetailIERByConstructionCode",
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
                            vm.searchForm.dateTo = dateToMax.getDate() + "/" + dateToMax.getMonth() + 1 + "/" + dateToMax.getFullYear();
                        }
                        return JSON.stringify(vm.searchForm)
                    }
                },
                /*
                aggregate: [
                    { field: "numberTonDauKy", aggregate: "sum" },
                    { field: "totalMoneyTonDauKy", aggregate: "sum" },
                    { field: "numberNhapTrongKy", aggregate: "sum" },
                    { field: "totalMoneyNhapTrongKy", aggregate: "sum" },
                    { field: "numberNghiemThuDoiSoat4A", aggregate: "sum" },
                    { field: "totalMoneyNghiemThuDoiSoat4A", aggregate: "sum" },
                    { field: "numberTraDenBu", aggregate: "sum" },
                    { field: "totalMoneyTraDenBu", aggregate: "sum" },
                    { field: "numberTonCuoiKy", aggregate: "sum" },
                    { field: "totalMoneyTonCuoiKy", aggregate: "sum" }
                ],
                */
                // group: {
                //     field: "provinceName",
                //     aggregates: [
                //         { field: "countTotal", aggregate: "sum" },
                //         { field: "countDone", aggregate: "sum" },
                //         { field: "countConstructing", aggregate: "sum" },
                //         { field: "countPending", aggregate: "sum" },
                //         { field: "countCancel", aggregate: "sum" },
                //         { field: "contractCode", aggregate: "count" },
                //         { field: "sumDone", aggregate: "sum" },
                //         { field: "sumConstructing", aggregate: "sum" },
                //         { field: "sumPending", aggregate: "sum" },
                //         { field: "sumCancel", aggregate: "sum" }
                //     ]},
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
                }, {
                    title: "CNKV/TTKT tỉnh",
                    field: "sysGroupName",
                    width: '220px',
                    columnMenu: false,
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;white-space: normal;"
                    }
                }, {
                    title: "Mã công trình",
                    field: "constructionCode",
                    width: "180px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:left;"}
                }, {
                    title: "Ngày tháng",
                    field: "realIeTransDate",
                    width: "90px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;"}
                }, {
                    title: "Số XK",
                    field: "synStockTransCode",
                    width: "180px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:left;"}
                }, {
                    title: "Tên VT HH",
                    field: "goodsName",
                    width: "250px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:left;white-space:normal;"},
                    footerTemplate: "Tổng cộng:"
                }, {
                    title: "Mã vật tư",
                    field: "goodsCode",
                    width: "80px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:left;"}
                }, {
                    title: "Mã trạm/tuyến",
                    field: "catStationCode",
                    width: "80px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:left;"}
                }, {
                    title: "ĐVT",
                    field: "goodsUnitName",
                    width: "80px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;"}
                }, {
                    title: "Tồn đầu kỳ<br>(Công nợ với CĐT)",
                    headerAttributes: {style: "text-align:center;white-space:normal;font-weight: bold;"},
                    attributes: {style: "text-align:center;"},
                    columns: [
                        {
                            title: "Số lượng",
                            field: "numberTonDauKy",
                            width: "90px",
                            headerAttributes: {style: "text-align:center;white-space:normal;"},
                            attributes: {style: "text-align:right;"},
                            format: "{0:n}",
                            // footerAttributes: {style: "text-align:center;white-space:normal;"},
                            footerTemplate : function(data) {
                                return kendo.toString(vm.sumHolder.numberTonDauKy, "n");
                            }
                        }, {
                            title: "Thành tiền",
                            field: "totalMoneyTonDauKy",
                            width: "130px",
                            headerAttributes: {style: "text-align:center;white-space:normal;"},
                            attributes: {style: "text-align:right;"},
                            format: "{0:n}",
                            footerTemplate : function(data) {
                                return kendo.toString(vm.sumHolder.totalMoneyTonDauKy, "n");
                            }
                        }
                    ]
                }, {
                    title: "Nhập trong kỳ<br>(Số nhận nợ mới trong kỳ)",
                    headerAttributes: {style: "text-align:center;white-space:normal;font-weight: bold;"},
                    attributes: {style: "text-align:center;"},
                    columns: [
                        {
                            title: "Số lượng",
                            field: "numberNhapTrongKy",
                            width: "90px",
                            headerAttributes: {style: "text-align:center;white-space:normal;"},
                            attributes: {style: "text-align:right;"},
                            format: "{0:n}",
                            footerTemplate : function(data) {
                                return kendo.toString(vm.sumHolder.numberNhapTrongKy, "n");
                            }
                        }, {
                            title: "Thành tiền",
                            field: "totalMoneyNhapTrongKy",
                            width: "130px",
                            headerAttributes: {style: "text-align:center;white-space:normal;"},
                            attributes: {style: "text-align:right;"},
                            format: "{0:n}",
                            footerTemplate : function(data) {
                                return kendo.toString(vm.sumHolder.totalMoneyNhapTrongKy, "n");
                            }
                        }]
                }, {
                    title: "Số được nghiệm thu theo đối soát 4A",
                    headerAttributes: {style: "text-align:center;white-space:normal;font-weight: bold;"},
                    attributes: {style: "text-align:center;"},
                    columns: [{
                        title: "Số lượng",
                        field: "numberNghiemThuDoiSoat4A",
                        width: "90px",
                        headerAttributes: {style: "text-align:center;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        format: "{0:n}",
                        footerTemplate : function(data) {
                            return kendo.toString(vm.sumHolder.numberNghiemThuDoiSoat4A, "n");
                        }
                    }, {
                        title: "Thành tiền",
                        field: "totalMoneyNghiemThuDoiSoat4A",
                        width: "130px",
                        headerAttributes: {style: "text-align:center;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        format: "{0:n}",
                        footerTemplate : function(data) {
                            return kendo.toString(vm.sumHolder.totalMoneyNghiemThuDoiSoat4A, "n");
                        }
                    }]
                }, {
                    title: "Số đã trả chủ đầu tư, đã xử lý đền bù nếu thất thoát (theo đối soát 4A)",
                    headerAttributes: {style: "text-align:center;white-space:normal;font-weight: bold;"},
                    attributes: {style: "text-align:center;"},
                    columns: [{
                        title: "Số lượng",
                        field: "numberTraDenBu",
                        width: "90px",
                        headerAttributes: {style: "text-align:center;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        format: "{0:n}",
                        footerTemplate : function(data) {
                            return kendo.toString(vm.sumHolder.numberTraDenBu, "n");
                        }
                    }, {
                        title: "Thành tiền",
                        field: "totalMoneyTraDenBu",
                        width: "130px",
                        headerAttributes: {style: "text-align:center;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        format: "{0:n}",
                        footerTemplate : function(data) {
                            return kendo.toString(vm.sumHolder.totalMoneyTraDenBu, "n");
                        }
                    }]
                }, {
                    title: "Tồn cuối kỳ<br>(Công nợ với CĐT)",
                    headerAttributes: {style: "text-align:center;white-space:normal;font-weight: bold;"},
                    attributes: {style: "text-align:center;"},
                    columns: [{
                        title: "Số lượng",
                        field: "numberTonCuoiKy",
                        width: "90px",
                        headerAttributes: {style: "text-align:center;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        format: "{0:n}",
                        footerTemplate : function(data) {
                            return kendo.toString(vm.sumHolder.numberTonCuoiKy, "n");
                        }
                    }, {
                        title: "Thành tiền",
                        field: "totalMoneyTonCuoiKy",
                        width: "130px",
                        headerAttributes: {style: "text-align:center;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        format: "{0:n}",
                        footerTemplate : function(data) {
                            return kendo.toString(vm.sumHolder.totalMoneyTonCuoiKy, "n");
                        }
                    }]
                }, {
                    title: "Số phiếu nhập thu hồi",
                    field: "fromSynStockTransCode",
                    width: "140px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;"}
                }
            ]
        });
// end
    }
})
();