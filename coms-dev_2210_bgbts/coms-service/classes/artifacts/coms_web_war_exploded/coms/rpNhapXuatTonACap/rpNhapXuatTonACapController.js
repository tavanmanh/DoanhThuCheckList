(function () {
    'use strict';
    var controllerId = 'rpNhapXuatTonACapController';

    angular.module('MetronicApp').controller(controllerId, rpNhapXuatTonACapController);

    function rpNhapXuatTonACapController($scope, $rootScope, $timeout, gettextCatalog,
                                kendoConfig, $kWindow, rpNhapXuatTonACapService,
                                CommonService, PopupConst, Restangular, RestEndpoint, Constant, htmlCommonService) {
        var vm = this;
        vm.importExportSearch = {};
        vm.String = "Quản lý công trình > Báo cáo > Báo cáo Nhập-Xuất-Tồn vật tư A cấp theo từng Đơn vị/Cá nhân";

        vm.sumHolder = {
            numberTonDauKy: 0,
            totalMoneyTonDauKy: 0,
            numberNhapTrongKy: 0,
            totalMoneyNhapTrongKy: 0,
            numberNghiemThuDoiSoat4A: 0,
            totalMoneyNghiemThuDoiSoat4A: 0,
            numberTraDenBu: 0,
            totalMoneyTraDenBu: 0,
            numberTonCuoiKy: 0,
            totalMoneyTonCuoiKy: 0
        };

        vm.lastSearch = {};

        //VietNT_20190301_start
        /*
        var date = new Date();
    	date.setDate(date.getDate() - 30);
    	var dateString = date.toISOString().split('T')[0];
    	var dateSplit = dateString.split("-");
    	var dateSub = dateSplit[2] +'/'+ dateSplit[1] +'/'+ dateSplit[0];

    	var dateCurr = new Date();
    	dateCurr.setDate(date.getDate() - 2);
    	var dateStringCurr = dateCurr.toISOString().split('T')[0];
    	var dateSplitCurr = dateStringCurr.split("-");
    	var dateSubCurr = dateSplitCurr[2] +'/'+ dateSplitCurr[1] +'/'+ dateSplitCurr[0];
        init();
        function init(){
        	fillDataTable([]);
        	vm.importExportSearch.dateFrom = dateSub;
        	vm.importExportSearch.dateTo = dateSubCurr;
        }
        */
        var today;
        var dateToMax;

        init();
        function init() {
            // date field
            today = new Date();
            dateToMax = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 1);
            var from = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
            vm.importExportSearch.dateTo = htmlCommonService.formatDate(dateToMax);
            vm.importExportSearch.dateFrom = htmlCommonService.formatDate(from);

            fillDataTable([]);
            doSearchWithSum();
            vm.lastSearch = angular.copy(vm.importExportSearch);
        }

        function isSameSearch() {
            return (vm.importExportSearch.sysGroupName === vm.lastSearch.sysGroupName
                && vm.importExportSearch.dateFrom === vm.lastSearch.dateFrom
                && vm.importExportSearch.dateTo === vm.lastSearch.dateTo
                && vm.importExportSearch.goodsName === vm.lastSearch.goodsName);
        }

        vm.doSearchNoSum = function() {
            var grid = vm.importExportGrid;
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

            // make copy of search obj
            var obj = angular.copy(vm.importExportSearch);
            // set no page size to get all data
            obj.page = null;
            obj.pageSize = null;

            Restangular.all("synStockDailyIeService/doSearchImportExportTonACap").post(obj).then(function (d) {
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
            if (!isSameSearch()) {
                // save to compare next time
                vm.lastSearch = angular.copy(vm.importExportSearch);
                doSearchWithSum();
            } else {
                vm.doSearchNoSum();
            }
        }
        //VietNT_end

        vm.goodsOptions = {
        	    dataTextField: "name",
        	    dataValueField: "id",
        		placeholder:"Nhập mã hoặc tên hàng hoá",
        	    select: function (e) {
        	    	vm.isSelect = true;
        	        var dataItem = this.dataItem(e.item.index());
        	        vm.importExportSearch.goodsName = dataItem.name;
        	        vm.importExportSearch.goodsCode = dataItem.code;
        	        vm.importExportSearch.goodsId = dataItem.goodsId;
        	    },
        	    pageSize: 10,
        	    dataSource: {
        	        serverFiltering: true,
        	        transport: {
        	            read: function (options) {
        	            	vm.isSelect = false;
        	                return Restangular.all("synStockDailyIeService/" + 'getForCompleteGoods').post({
        	                	name: vm.importExportSearch.goodsName,
        	                    pageSize: vm.goodsOptions.pageSize,
        	                    page: 1
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
        	            vm.importExportSearch.goodsName = null;// thành name
        	            vm.importExportSearch.goodsCode = null;
        	            vm.importExportSearch.goodsId = null;
        	        }
        	    },
        	    close: function (e) {
                    if (!vm.isSelect) {
                    	vm.importExportSearch.goodsName = null;// thành name
        	            vm.importExportSearch.goodsCode = null;
        	            vm.importExportSearch.goodsId = null;
                    }
                },
        	    ignoreCase: false
        	}

        vm.deprtOptions1 = {
        	    dataTextField: "text",
        	    dataValueField: "id",
        		placeholder:"Nhập mã hoặc tên đơn vị",
        	    select: function (e) {
        	    	vm.isSelect = true;
        	        var dataItem = this.dataItem(e.item.index());
        	        vm.importExportSearch.sysGroupName = dataItem.text;
        	        vm.importExportSearch.sysGroupId = dataItem.id;
        	    },
        	    pageSize: 10,
        	    dataSource: {
        	        serverFiltering: true,
        	        transport: {
        	            read: function (options) {
        	            	vm.isSelect = false;
        	                return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({
        	                    name: vm.importExportSearch.sysGroupName,
        	                    pageSize: vm.deprtOptions1.pageSize
        	                }).then(function (response) {
        	                    options.success(response);
        	                }).catch(function (err) {
        	                    console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
        	                });
        	            }
        	        }
        	    },
        	    template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
        	    change: function (e) {
        	        if (e.sender.value() === '') {
        	            vm.importExportSearch.sysGroupName = null;// thành name
        	            vm.importExportSearch.sysGroupId = null;
        	        }
        	    },
        	    close: function (e) {
                    if (!vm.isSelect) {
                    	vm.importExportSearch.sysGroupName = null;
                    	vm.importExportSearch.sysGroupId = null;
                    }
                },
        	    ignoreCase: false
        	}
        vm.openGoodsTo = openGoodsTo;

        function openGoodsTo(popUp) {
            vm.obj = {};
            var templateUrl = 'coms/rpNhapXuatTonACap/searchGoods.html';
            var title = gettextCatalog.getString("Tìm kiếm hàng hoá");
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,null, vm, 'fff', 'ggfd', false, '85%','85%','goodsSearchController');
        }

        vm.onSaveGoods = onSaveGoods;
        function onSaveGoods(data) {
             vm.importExportSearch.goodsName = data.goodsName;
             vm.importExportSearch.goodsId = data.goodsId;
             vm.importExportSearch.goodsCode = data.goodsCode;
        }

        vm.openDepartmentTo = openDepartmentTo

        function openDepartmentTo(popUp) {
            vm.obj = {page:1,pageSize:10};
            CommonService.getDepartment(obj).then(function(result){
				var data=result.plain();
	            vm.departmentpopUp = popUp;
	            var templateUrl = 'coms/popup/findDepartmentPopUp.html';
	            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
	            CommonService.populatePopupDept(templateUrl, title, null, data, vm, popUp, 'string', false, '92%', '89%');
            });
            }

        vm.onSave = onSave;
        function onSave(data) {
             vm.importExportSearch.sysGroupName = data.text;
             vm.importExportSearch.sysGroupId = data.id;
        }

        var record = 0;
        function fillDataTable(data) {
        	vm.importExportGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save : function(){
                    vm.importExportGrid.refresh();
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: [
                    {
                        name: "actions",
                        template:'<div class="btn-group pull-right margin_top_button ">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">' +
                        '<label ng-repeat="column in vm.importExportGrid.columns.slice(1,vm.importExportGrid.columns.length)| filter: vm.gridColumnShowHideFilter">' +

                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#appCountWorkItem").text("" + response.total);
                            vm.countImportExport = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "synStockDailyIeService/doSearchImportExportTonACap",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.importExportSearch.page = options.page
                            vm.importExportSearch.pageSize = options.pageSize
                            return JSON.stringify(vm.importExportSearch)
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
                        width: '50px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                    },
                    {
                        title: "CNKV/TTKT tỉnh",
                        field: "sysGroupName",
                        width: '200px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        }
                    },
                    {
                        title: "Tên vật tư hàng hoá",
                        field: 'goodsName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        },
                        type :'text',
                        footerTemplate: function(item) {
                        	return "Tổng cộng";
						},
                    },
                    {
                        title: "Mã vật tư",
                        field: 'goodsCode',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "ĐVT",
                        field: 'goodsUnitName',
                        width: '80px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'text',
                    },
                    {
                        title: "Tồn đầu kỳ (Công nợ với CĐT)",
                        width: '300px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        columns: [
                            {
                            	title: "Số lượng",
                                field: 'numberTonDauKy',
                                width: '150px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                format: "{0:n}",
                                type :'number',
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.numberTonDauKy, "n");
        						},
                            }, {
                            	title: "Thành tiền",
            					field: 'totalMoneyTonDauKy',
                                width: '150px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                format: "{0:n}",
                                type :'number',
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.totalMoneyTonDauKy, "n");
        						},
                            }
                        ]

                    },
                    {
                        title: "Nhập trong kỳ (Số nhận nợ mới trong kỳ)",
                        width: '300px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        columns: [
                            {
                            	title: "Số lượng",
                                field: 'numberNhapTrongKy',
                                width: '150px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                type :'number',
                                format: "{0:n}",
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.numberNhapTrongKy, "n");
        						},
                            }, {
                            	title: "Thành tiền",
            					field: 'totalMoneyNhapTrongKy',
                                width: '150px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                type :'number',
                                format: "{0:n}",
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.totalMoneyNhapTrongKy, "n");
        						},
                            }
                        ]

                    },
                    {
                        title: "Số được nghiệm thu theo đối soat 4A",
                        width: '300px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        columns: [
                            {
                            	title: "Số lượng",
                                field: 'numberNghiemThuDoiSoat4A',
                                width: '150px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                type :'number',
                                format: "{0:n}",
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.numberNghiemThuDoiSoat4A, "n");
        						},
                            }, {
                            	title: "Thành tiền",
            					field: 'totalMoneyNghiemThuDoiSoat4A',
                                width: '150px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                type :'number',
                                format: "{0:n}",
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.totalMoneyNghiemThuDoiSoat4A, "n");
        						},
                            }
                        ]

                    },
                    {
                        title: "Số đã trả chủ đầu tư, đã xử lý đền bù nếu thất thoát (theo đối soát 4A)",
                        width: '300px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        columns: [
                            {
                            	title: "Số lượng",
                                field: 'numberTraDenBu',
                                width: '150px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                type :'number',
                                format: "{0:n}",
                                footerTemplate: function(item) {
                            	    return kendo.toString(vm.sumHolder.numberTraDenBu, "n");
        						},
                            }, {
                            	title: "Thành tiền",
            					field: 'totalMoneyTraDenBu',
                                width: '150px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                type :'number',
                                format: "{0:n}",
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.totalMoneyTraDenBu, "n");
        						},
                            }
                        ]

                    },
                    {
                        title: "Tồn cuối kỳ (Công nợ với CĐT)",
                        width: '300px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        columns: [
                            {
                            	title: "Số lượng",
                                field: 'numberTonCuoiKy',
                                width: '150px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                type :'number',
                                format: "{0:n}",
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.numberTonCuoiKy, "n");
        						},
                            }, {
                            	title: "Thành tiền",
            					field: 'totalMoneyTonCuoiKy',
                                width: '150px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                type :'number',
                                format: "{0:n}",
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.totalMoneyTonCuoiKy, "n");
        						},
                            }
                        ]

                    }
//                    {
//                        title: "Ghi chú",
//                        field: '',
//                        width: '150px',
//                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
//                        attributes: {
//                            style: "text-align:right;"
//                        },
//                        type :'text'
//                    }
    ]

    });
}


        vm.cancelInput=function(){
        	vm.importExportSearch.sysGroupName = null;
        	vm.importExportSearch.sysGroupCode = null;
        	vm.importExportSearch.sysGroupId = null;
        };

        vm.cancelListYear=function(){
        	vm.importExportSearch.dateFrom = null;
        	vm.importExportSearch.dateTo = null;
        };

        vm.cancelInputGoods = function(){
        	vm.importExportSearch.goodsName = null;
        	vm.importExportSearch.goodsCode = null;
        	vm.importExportSearch.goodsId = null;
        };

        vm.showHideWorkItemColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.importExportGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.importExportGrid.showColumn(column);
            } else {
                vm.importExportGrid.hideColumn(column);
            }
        };

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };

        vm.checkErr = checkErr;
		function checkErr() {
			var startDate = $('#dateFrom').val();
			var endDate = $('#dateTo').val();

			if (endDate == undefined) {
				endDate = null;
			}

			if (startDate == undefined) {
				startDate = null;
			}

			vm.errMessage = '';
			vm.errMessage1 = '';

			if (endDate !== "") {
				if (kendo.parseDate(endDate, "dd/MM/yyyy") == null) {
					vm.errMessage = CommonService.translate('Ngày đến không hợp lệ');
					$("#dateTo").focus();
					vm.checkDateTo = false;
					return vm.errMessage;
				} else if (kendo.parseDate(endDate, "dd/MM/yyyy").getFullYear() > 9999 || kendo.parseDate(endDate, "dd/MM/yyyy").getFullYear() < 1000) {
					vm.errMessage = CommonService.translate('Ngày đến không hợp lệ');
					$("#dateTo").focus();
					vm.checkDateTo = false;
					return vm.errMessage;
				} else if (kendo.parseDate(startDate, "dd/MM/yyyy") > kendo.parseDate(endDate, "dd/MM/yyyy")) {
					vm.errMessage = CommonService.translate('Ngày đến phải lớn hơn ngày tạo');
					$("#dateTo").focus();
					vm.checkDateTo = false;
					return vm.errMessage;
				}

				if (kendo.parseDate(startDate, "dd/MM/yyyy") <= kendo.parseDate(endDate, "dd/MM/yyyy")) {
					vm.errMessage = '';
					vm.errMessage1 = '';
					vm.checkDateFrom = true;
					vm.checkDateTo = true;
					return vm.errMessage;
				}
			} else {
				vm.errMessage = '';
				vm.checkDateTo = true;
				return vm.errMessage;
			}
		}
		// validate ngay thang
		vm.checkErr1 = checkErr1;
		function checkErr1() {
			debugger;
			var startDate = $('#dateFrom').val();
			var endDate = $('#dateTo').val();
			vm.errMessage = '';
			vm.errMessage1 = '';

			if (startDate !== "") {
				if (kendo.parseDate(startDate, "dd/MM/yyyy") == null) {
				vm.errMessage1 = CommonService.translate('Ngày tạo không hợp lệ');
				$("#dateFrom").focus();
				vm.checkDateFrom = false;
				return vm.errMessage1;
			} else if (kendo.parseDate(startDate, "dd/MM/yyyy").getFullYear() > 9999 || kendo.parseDate(startDate, "dd/MM/yyyy").getFullYear() < 1000) {
				vm.errMessage1 = CommonService.translate('Ngày tạo không hợp lệ');
				$("#dateFrom").focus();
				vm.checkDateFrom = false;
				return vm.errMessage1;
			}
			} else {
				vm.errMessage1 = '';
				vm.checkDateFrom = true;
				return vm.errMessage1;
			}
			if (endDate !== "") {
				if (kendo.parseDate(startDate, "dd/MM/yyyy") > kendo.parseDate(endDate, "dd/MM/yyyy")) {
					vm.errMessage1 = CommonService.translate('Ngày tạo phải nhỏ hơn ngày đến');
					$("#dateFrom").focus();
					vm.checkDateFrom = false;
					return vm.errMessage1;
				} else if (kendo.parseDate(startDate, "dd/MM/yyyy") <= kendo.parseDate(endDate, "dd/MM/yyyy")) {
					vm.errMessage = '';
					vm.errMessage1 = '';
					vm.checkDateFrom = true;
					vm.checkDateTo = true;
					return vm.errMessage;
				}
			}

		}

        vm.exportFile= function(){
        	function displayLoading(target) {
              var element = $(target);
              kendo.ui.progress(element, true);
              setTimeout(function(){
                	return Restangular.all("synStockDailyIeService/exportExcelImportExportTonACap").post(vm.importExportSearch).then(function (d) {
                	    var data = d.plain();
                	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                	    kendo.ui.progress(element, false);
                	    vm.disableExportExcel = false;
                	}).catch(function (e) {
                		kendo.ui.progress(element, false);
                	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                	    vm.disableExportExcel = false;
                	    return;
                	});
        	});

          }
        	displayLoading(".tab-content");
        }
}
})();
