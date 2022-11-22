//VietNT_19122018_created
(function () {
    'use strict';
    var controllerId = 'synStockDailyIeReportController';

    angular.module('MetronicApp').controller(controllerId, synStockDailyIeReportController);

    function synStockDailyIeReportController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, synStockDailyIeReportService, htmlCommonService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http) {

        var vm = this;
        var today;
        var dateToMax;
        var validator;
        // variables
        vm.String = "Quản lý công trình > Báo cáo > Biên bản chi tiết xác nhận nợ VTTB nhận của chủ đầu tư";
        vm.StringGeneral = "Quản lý công trình > Báo cáo > Biên bản tổng hợp xác nhận nợ VTTB nhận của chủ đầu tư";
        var ERROR_MESSAGE = "Có lỗi xảy ra";
        vm.searchForm = {
            status: 1
        };

        vm.sumHolder = {};
        vm.lastSearch = {};

        initSumValue();
        function initSumValue() {
            vm.sumHolder.totalMoney = 0;
            vm.sumHolder.amountTotal = 0;
        }

        // label name html
        vm.htmlLabel = {
            keySearch: "Nội dung tìm kiếm",
            dateFrom: "Số liệu chốt từ ngày",
            dateTo: "Đến",
            sysGroup: "Đơn vị",
            province: "Tỉnh"
        };

        // init controller data
        $scope.$watch('vm.rpType', function() {
            vm.dataController = {};
            switch (vm.rpType) {
                case 0:
                    vm.dataController = {
                        grid: vm.goodsDebtConfirmGeneralGrid,
                        dateContainerSelector: '#realIeDateGeneral',
                        dateFromIdSelector: '#debtGeneral_dateFrom',
                        dateToIdSelector: '#debtGeneral_dateTo'
                    };
                    break;
                case 1:
                    vm.dataController = {
                        grid: vm.goodsDebtConfirmDetailGrid,
                        dateContainerSelector: '#realIeDateDetail',
                        dateFromIdSelector: '#debtDetail_dateFrom',
                        dateToIdSelector: '#debtDetail_dateTo'
                    };
                    break;
                default:
                    return;
            }

            validator = $(vm.dataController.dateContainerSelector).kendoValidator({
                rules: {
                    dateEmpty: function(input) {
                        if (input.is("[data-role='datepicker']")) {
                            if (input.val() === "" || input.val() === null) {
                                return false;
                            }
                        }
                        return true;
                    },
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
                        if (input.is(vm.dataController.dateFromIdSelector)) {
                            var dateTo = kendo.parseDate($(vm.dataController.dateToIdSelector).val(), 'dd/MM/yyyy');
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
                        if (input.is(vm.dataController.dateToIdSelector)) {
                            var dateTo = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                            var dateFrom = kendo.parseDate($(vm.dataController.dateFromIdSelector).val(), 'dd/MM/yyyy');
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
                    dateTo: "Chọn \"Đến ngày\" lớn hơn",
                    dateFrom: "Chọn \"Từ ngày\" nhỏ hơn"
                }
            }).data("kendoValidator");

            doSearchWithSum();
            vm.lastSearch = angular.copy(vm.searchForm);
        });

        initDataSearchForm();
        function initDataSearchForm() {
            // date field
            today = new Date();
            dateToMax = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 1);
            // var from = new Date(dateToMax.getFullYear(), dateToMax.getMonth() - 1, dateToMax.getDate());
            var from = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
            vm.searchForm.dateTo = htmlCommonService.formatDate(dateToMax);
            vm.searchForm.dateFrom = htmlCommonService.formatDate(from);
        }
        //VietNT_20190301_start

        function isSameSearch() {
            return (vm.searchForm.sysGroupText === vm.lastSearch.sysGroupText
                && vm.searchForm.keySearch === vm.lastSearch.keySearch
                && vm.searchForm.provinceCode === vm.lastSearch.provinceCode
                && vm.searchForm.dateFrom === vm.lastSearch.dateFrom
                && vm.searchForm.dateTo === vm.lastSearch.dateTo);
        }

        vm.doSearchNoSum = function() {
            var grid = vm.dataController.grid;
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
            console.log(vm.rpType);
            // reinit sum value
            initSumValue();

            // make copy of search obj
            var obj = angular.copy(vm.searchForm);
            // set no page size to get all data
            obj.page = null;
            obj.pageSize = null;

            var uri = vm.rpType === 0 ? "doSearchGoodsDebtConfirmGeneral" : "doSearchGoodsDebtConfirmDetail";
            Restangular.all("synStockDailyIeService/" + uri).post(obj).then(function (d) {
                var data = d.data;
                $.each(data, function (index, record) {
                    vm.sumHolder.totalMoney += record.totalMoney;
                    vm.sumHolder.amountTotal += record.amountTotal;
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

        // function dosearch
        vm.cancel = cancel;

        function dateFieldNotValidated() {
            if ($('.k-invalid-msg').is(':visible')) {
                toastr.warning("Nhập ngày chốt số liệu trước khi thực hiện thao tác!");
                return true;
            }
            return false;
        }

        function cancel() {
            CommonService.dismissPopup1();
        }

        // ----- Search form start
        vm.resetFormField = resetFormField;
        function resetFormField(fieldName) {
            switch (fieldName) {
                case 'keySearch':
                    vm.searchForm.keySearch = null;
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
//                    vm.disableSearchDetail = false;
//                    vm.dateFromErr = null;
//                    vm.dateToErr = null;
                    break;
                case 'group':
                    vm.searchForm.sysGroupText = null;
                    vm.searchForm.sysGroupId = null;
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
        // Search form end -----
        
        // ----- Main Function start
        vm.exportExcelDetail = function() {
            if (dateFieldNotValidated()) {
                return;
            }

            var element = $(".tab-content");
            kendo.ui.progress(element, true);

            vm.searchForm.page = null;
            vm.searchForm.pageSize = null;
            synStockDailyIeReportService.doSearchGoodsDebtConfirmDetail(vm.searchForm).then(function (response) {
                if (!!!response.data) {
                    toastr.error("Không có dữ liệu!");
                    kendo.ui.progress(element, false);
                    return;
                }

                var sum = 0;
                for (var i = 0; i < response.data.length; i++) {
                    sum += response.data[i].totalMoney;
                }
                sum = Math.round(sum);

                var requestData = {};
                requestData.exportData = response.data;
                requestData.sum = sum;
                requestData.sumText = DocTienBangChu(sum).replaceAll("  ", " ");
                requestData.dateFrom = vm.searchForm.dateFrom;
                requestData.dateTo = vm.searchForm.dateTo;

                synStockDailyIeReportService.exportGoodsDebtConfirmDetail(requestData).then(function (response) {
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
            });
        };

        vm.exportExcelGeneral = function() {
            if (dateFieldNotValidated()) {
                return;
            }

            var element = $(".tab-content");
            kendo.ui.progress(element, true);

            vm.searchForm.page = null;
            vm.searchForm.pageSize = null;
            synStockDailyIeReportService.doSearchGoodsDebtConfirmGeneral(vm.searchForm).then(function (response) {
                if (!!!response.data) {
                    toastr.error("Không có dữ liệu!");
                    kendo.ui.progress(element, false);
                    return;
                }

                var sum = 0;
                for (var i = 0; i < response.data.length; i++) {
                    sum += response.data[i].totalMoney;
                }
                sum = Math.round(sum);

                var requestData = {};
                requestData.exportData = response.data;
                requestData.sum = sum;
                requestData.sumText = DocTienBangChu(sum).replaceAll("  ", " ");
                requestData.dateFrom = vm.searchForm.dateFrom;
                requestData.dateTo = vm.searchForm.dateTo;

                synStockDailyIeReportService.exportGoodsDebtConfirmGeneral(requestData).then(function (response) {
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
        
        function convertConstructionState(state) {
        	if (!!!state) {
        		return null;
        	}
        	switch (state) {
                case 0:
                    return "Đã hủy";
                case 1:
                    return "Chưa thi công";
                case 2:
                    return "Đang thi công";
                case 3:
                    return "Thi công xong";
                default:
                    return null;
            }
        }
        // Main Function end -----

        // ----- Main Grid start
        vm.showHideColumnDetail = function(column, whichGrid) {
            // var grid = whichGrid === 0 ? vm.goodsDebtConfirmGeneralGrid : vm.goodsDebtConfirmDetailGrid;
            var grid = vm.dataController.grid;
            if (angular.isUndefined(column.hidden)) {
                grid.hideColumn(column);
            } else if (column.hidden) {
                grid.showColumn(column);
            } else {
                grid.hideColumn(column);
            }
        };

        vm.gridColumnShowHideFilter = function(item) {
            return item.type == null || item.type !== 1;
        };

        var record = 0;
        vm.goodsDebtConfirmDetailGridOptions = kendoConfig.getGridOptions({
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
                                '<label ng-repeat="column in vm.goodsDebtConfirmDetailGrid.columns | filter: vm.gridColumnShowHideFilter">' +
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
                        url: Constant.BASE_SERVICE_URL + RestEndpoint.SYN_STOCK_DAILY_IE_URL + "/doSearchGoodsDebtConfirmDetail",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.searchForm.page = options.page;
                        vm.searchForm.pageSize = options.pageSize;
                        /*
                        if (vm.searchForm.dateTo === null || vm.searchForm.dateTo === "") {
                            vm.searchForm.dateTo = dateToMax.getDate() + "/" + (dateToMax.getMonth() + 1) + "/" + dateToMax.getFullYear();
                        }
                        if (!vm.searchForm.dateFrom) {
                            vm.searchForm.dateFrom = dateToMax.getDate() + "/" + dateToMax.getMonth() + "/" + dateToMax.getFullYear();
                        }
                        */

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
                    title: "STT",
                    field: "stt",
                    width: "50px",
                    headerAttributes: {style: "text-align:center;"},
                    attributes: {style: "text-align:center;"},
                    template: function (dataItem) {
                        return ++record;
                    }
                },
                {
                    title: "Mã tỉnh",
                    field: "provinceCode",
                    width: "70px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;"}
                },
                {
                    title: "CNKV/TTKT tỉnh",
                    field: "sysGroupName",
                    width: "200px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:left;white-space:normal;"}
                },
                {
                    title: "Số phiếu xuất",
                    field: "synStockTransCode",
                    width: "180px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:left;white-space:normal;"},
                    footerTemplate : function(data) {
                        return "Tổng cộng:";
                    }
                },
                {
                    title: "Ngày thực xuất",
                    field: "realIeTransDate",
                    width: "110px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;"}
                },
                {
                    title: "Tên vật tư",
                    field: "goodsName",
                    width: "200px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:left;white-space:normal;"}
                },
                {
                    title: "Mã vật tư",
                    field: "goodsCode",
                    width: "90px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;"}
                },
                {
                    title: "Số lượng",
                    field: "amountTotal",
                    width: "80px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:right;"},
                    template: function (dataItem) {
                    	return numberWithCommas(dataItem.amountTotal);
                    },
                    footerTemplate : function(data) {
                        return kendo.toString(vm.sumHolder.amountTotal, "n");
                    }
                },
                {
                    title: "Serial",
                    field: "serial",
                    width: "140px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;"}
                },
                {
                    title: "Giá trị",
                    field: "totalMoney",
                    width: "140px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:right;"},
                    template: function (dataItem) {
                    	return numberWithCommas(dataItem.totalMoney);
                    },
                    footerTemplate : function(data) {
                        return kendo.toString(vm.sumHolder.totalMoney, "n");
                    }
                },
                {
                    title: "Mã công trình",
                    field: "constructionCode",
                    width: "180px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:left;white-space:normal;"}
                },
                {
                    title: "Tình trạng công trình",
                    field: "constructionState",
                    width: "120px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;"},
                    template: function (dataItem) {
                        return convertConstructionState(dataItem.constructionState);
                    }
                },
                {
                    title: "Mã trạm nhận",
                    field: "catStationCode",
                    width: "90px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;"}
                },
                {
                    title: "Số hợp đồng",
                    field: "contractCode",
                    width: "180px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:left;white-space:normal;"}
                },
                {
                    title: "Người nhận (Đội trưởng tỉnh)",
                    field: "provinceUserName",
                    width: "150px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;"}
                },
                {
                    title: "Người nhận (Nhân viên thi công trực tiếp)",
                    field: "sysUserName",
                    width: "150px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;"}
                }
            ]
        });

        vm.goodsDebtConfirmGeneralGridOptions = kendoConfig.getGridOptions({
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
                        '<label ng-repeat="column in vm.goodsDebtConfirmGeneralGrid.columns | filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumnDetail(column, 0)"> {{column.title}}' +
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
                        url: Constant.BASE_SERVICE_URL + RestEndpoint.SYN_STOCK_DAILY_IE_URL + "/doSearchGoodsDebtConfirmGeneral",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        var postBody = angular.copy(vm.searchForm);
                        postBody.page = options.page;
                        postBody.pageSize = options.pageSize;
                        if (postBody.dateTo === null || postBody.dateTo === "") {
                            postBody.dateTo = dateToMax.getDate() + "/" + (dateToMax.getMonth() + 1) + "/" + dateToMax.getFullYear();
                        }
                        return JSON.stringify(postBody)
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
                    width: "50px",
                    headerAttributes: {style: "text-align:center;"},
                    attributes: {style: "text-align:center;"},
                    template: function (dataItem) {
                        return ++record;
                    }
                },
                {
                    title: "Mã tỉnh",
                    field: "provinceCode",
                    width: "80px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"}
                },
                {
                    title: "CNKV/TTKT tỉnh",
                    field: "sysGroupName",
                    width: "200px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:left;white-space:normal;"}
                },
                {
                    title: "Số phiếu xuất",
                    field: "synStockTransCode",
                    width: "180px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:left;white-space:normal;"},
                    footerTemplate : function(data) {
                        return "Tổng cộng:";
                    }
                },
                {
                    title: "Ngày thực xuất",
                    field: "realIeTransDate",
                    width: "90px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"}
                },
                {
                    title: "Giá trị",
                    field: "totalMoney",
                    width: "120px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:right;white-space:normal;"},
                    template: function (dataItem) {
                    	return numberWithCommas(dataItem.totalMoney);
                    },
                    footerTemplate : function(data) {
                        return kendo.toString(vm.sumHolder.totalMoney, "n");
                    }
                },
                {
                    title: "Mã công trình",
                    field: "constructionCode",
                    width: "180px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:left;white-space:normal;"}
                },
                {
                    title: "Tình trạng công trình",
                    field: "constructionState",
                    width: "120px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"},
                    template: function (dataItem) {
                        return convertConstructionState(dataItem.constructionState);
                    }
                },
                {
                    title: "Mã trạm nhận",
                    field: "catStationCode",
                    width: "100px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"}
                },
                {
                    title: "Số hợp đồng",
                    field: "contractCode",
                    width: "180px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:left;white-space:normal;"}
                },
                {
                    title: "Người nhận (Đội trưởng tỉnh)",
                    field: "provinceUserName",
                    width: "120px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"}
                },
                {
                    title: "Người nhận (Nhân viên thi công trực tiếp)",
                    field: "sysUserName",
                    width: "120px",
                    headerAttributes: {style: "text-align:center;white-space:normal;"},
                    attributes: {style: "text-align:center;white-space:normal;"}
                }
            ]
        });
        // Main Grid end -----
        // Controller end
    }
})();