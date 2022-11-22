(function () {
    'use strict';
    var controllerId = 'rpCongNoACapController';

    angular.module('MetronicApp').controller(controllerId, rpCongNoACapController);

    function rpCongNoACapController($scope, $rootScope, $timeout, gettextCatalog,
                                kendoConfig, $kWindow, rpCongNoACapService,
                                CommonService, PopupConst, Restangular, RestEndpoint, Constant, htmlCommonService) {
        var vm = this;
        vm.debtSearch = {};
        vm.String = "Quản lý công trình > Báo cáo > Báo cáo tổng hợp công nợ vật tư A cấp";
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
        	vm.debtSearch.dateFrom = dateSub;
        	vm.debtSearch.dateTo = dateSubCurr;
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
            vm.debtSearch.dateTo = htmlCommonService.formatDate(dateToMax);
            vm.debtSearch.dateFrom = htmlCommonService.formatDate(from);

            fillDataTable([]);
            doSearchWithSum();
            vm.lastSearch = angular.copy(vm.debtSearch);
        }

        function isSameSearch() {
            return (vm.debtSearch.sysGroupName === vm.lastSearch.sysGroupName
                && vm.debtSearch.dateFrom === vm.lastSearch.dateFrom
                && vm.debtSearch.dateTo === vm.lastSearch.dateTo);
        }

        vm.doSearchNoSum = function() {
            var grid = vm.debtGrid;
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
            var obj = angular.copy(vm.debtSearch);
            // set no page size to get all data
            obj.page = null;
            obj.pageSize = null;

            Restangular.all("synStockDailyIeService/doSearchDebt").post(obj).then(function (d) {
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
                vm.lastSearch = angular.copy(vm.debtSearch);
                doSearchWithSum();
            } else {
                vm.doSearchNoSum();
            }
        }
        //VietNT_20190301_end

        vm.cancelInput=function(){
        	vm.debtSearch.sysGroupName = null;
        	vm.debtSearch.sysGroupCode = null;
        	vm.debtSearch.sysGroupId = null;
        }

        vm.cancelListYear=function(){
        	vm.debtSearch.dateFrom = null;
        	vm.debtSearch.dateTo = null;
        }

        var record = 0;
        function fillDataTable(data) {
        	vm.debtGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save : function(){
                    vm.debtGrid.refresh();
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
                        '<label ng-repeat="column in vm.debtGrid.columns.slice(1,vm.debtGrid.columns.length)| filter: vm.gridColumnShowHideFilter">' +

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
                            vm.countDebt = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "synStockDailyIeService/doSearchDebt",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.debtSearch.page = options.page
                            vm.debtSearch.pageSize = options.pageSize
                            return JSON.stringify(vm.debtSearch)
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
                        title: "Tồn tại đơn vị/ cá nhân",
                        field: 'text',
                        width: '270px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        footerTemplate: function(item) {
                        	return "Tổng cộng";
						},
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
                        title: "Số được nghiệm thu theo đối soát 4A",
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
        vm.deprtOptions1 = {
        	    dataTextField: "text",
        	    dataValueField: "id",
        		placeholder:"Nhập mã hoặc tên đơn vị",
        	    select: function (e) {
        	    	vm.isSelect = true;
        	        var dataItem = this.dataItem(e.item.index());
        	        vm.debtSearch.sysGroupName = dataItem.text;
        	        vm.debtSearch.sysGroupId = dataItem.id;
        	    },
        	    pageSize: 10,
        	    dataSource: {
        	        serverFiltering: true,
        	        transport: {
        	            read: function (options) {
        	            	vm.isSelect = false;
        	                return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({
        	                    name: vm.debtSearch.sysGroupName,
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
        	            vm.debtSearch.sysGroupName = null;// thành name
        	            vm.debtSearch.sysGroupId = null;
        	        }
        	    },
        	    close: function (e) {
                    if (!vm.isSelect) {
                    	vm.debtSearch.sysGroupName = null;
                    	vm.debtSearch.sysGroupId = null;
                    }
                },
        	    ignoreCase: false
        	}

        vm.openDepartmentTo = openDepartmentTo

        function openDepartmentTo(popUp) {
            var obj = {};
            vm.departmentpopUp = popUp;
	        var templateUrl = 'coms/popup/findDepartmentPopUp.html';
	        var title = gettextCatalog.getString("Tìm kiếm đơn vị");
	        CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
          }

        vm.onSave = onSave;
        function onSave(data) {
             vm.debtSearch.sysGroupName = data.text;
             vm.debtSearch.sysGroupId = data.id;
        }

        vm.showHideWorkItemColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.debtGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.debtGrid.showColumn(column);
            } else {
                vm.debtGrid.hideColumn(column);
            }
        }

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
                	return Restangular.all("synStockDailyIeService/exportExcelDebt").post(vm.debtSearch).then(function (d) {
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
