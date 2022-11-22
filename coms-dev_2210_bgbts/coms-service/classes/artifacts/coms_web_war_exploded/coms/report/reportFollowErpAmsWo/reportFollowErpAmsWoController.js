(function() {
	'use strict';
	var controllerId = 'reportFollowErpAmsWoController';

	angular.module('MetronicApp').controller(controllerId,
			reportFollowErpAmsWoController);

	function reportFollowErpAmsWoController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, reportFollowErpAmsWoService,
			CommonService, PopupConst, Restangular, RestEndpoint, Constant,
			htmlCommonService) {
		var vm = this;
		vm.showSearch = true;
		vm.searchForm = {};
		vm.String = "Quản lý công trình > Báo cáo > Báo cáo theo dõi Tài chính - Tài sản - WO";
		initFormData();
		function initFormData() {
			fillDataTable([]);
		}

		vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.reportFollowErpAmsWoGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.reportFollowErpAmsWoGrid.showColumn(column);
			} else {
				vm.reportFollowErpAmsWoGrid.hideColumn(column);
			}
		}

		vm.gridColumnShowHideFilter = function(item) {
			return item.type == null || item.type !== 1;
		};

		vm.doSearch = function doSearch() {
			kendo.ui.progress($("#reportFollowErpAmsWoGridId"), true);
			var grid = vm.reportFollowErpAmsWoGrid;
			if (grid) {
				grid.dataSource.query({
					page : 1,
					pageSize : 10
				});
				kendo.ui.progress($("#reportFollowErpAmsWoGridId"), false);
			}
		}

		var record = 0;

		function fillDataTable(data) {
			kendo.ui.progress($("#reportFollowErpAmsWoGridId"), true);
			vm.reportFollowErpAmsWoGridOptions = kendoConfig
					.getGridOptions({
						autoBind : true,
						scrollable : true,
						resizable : true,
						editable : false,
						save : function() {
							vm.reportFollowErpAmsWoGrid.refresh();
						},
						dataBinding : function() {
							record = (this.dataSource.page() - 1)
									* this.dataSource.pageSize();
						},
						reorderable : true,
						toolbar : [ {
							name : "actions",
							template : '<div class="btn-group pull-right margin_top_button ">'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
									+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
									+ '<label ng-repeat="column in vm.reportFollowErpAmsWoGrid.columns.slice(1,vm.reportFollowErpAmsWoGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
									+

									'<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}'
									+ '</label>' + '</div></div>'
						} ],
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									$("#appCountReportErp").text("" + response.total);
									vm.countReportErp = response.total;
									return response.total;
								},
								data : function(response) {
									kendo.ui.progress($("#reportFollowErpAmsWoGridId"), false);
									return response.data;
								}
							},
							transport : {
								read : {
									// Thuc hien viec goi service
									url : Constant.BASE_SERVICE_URL
											+ "rpOrderlyWoService/doSearchReportErpAmsWo",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									vm.searchForm.page = options.page
									vm.searchForm.pageSize = options.pageSize
									return JSON.stringify(vm.searchForm)
								}
							},
							pageSize : 10,
						},
						noRecords : true,
						columnMenu : false,
						messages : {
							noRecords : gettextCatalog
									.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
						},
						pageable : {
							refresh : false,
							pageSizes : [ 10, 15, 20, 25 ],
							messages : {
								display : "{0}-{1} của {2} kết quả",
								itemsPerPage : "kết quả/trang",
								empty : "<div style='margin:5px'>Không có kết quả hiển thị</div>"
							}
						},
						columns : [ {
							title : "TT",
							field : "stt",
							template : function() {
								return ++record;
							},
							width : '50px',
							columnMenu : false,
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',

						}, {
							title : "Tỉnh",
							field : 'provinceCode',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:right;"
							},
							type : 'text'
						}, {
							title : "Mã trạm",
							field : 'stationCode',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:right;"
							},
							type : 'text'
						}, {
							title : "Mã công trình",
							field : 'constructionCode',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:right;"
							},
							type : 'text'
						}, {
							title : "Mã hợp đồng",
							field : 'contractCode',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:right;"
							},
							type : 'text'
						}, {
							title : "Nhân viên thực giám sát trạm",
							field : 'ftName',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:right;"
							},
							type : 'text'
						}, {
							title : "Đã phát sóng trạm",
							field : 'state',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:right;"
							},
							type : 'text'
						}, {
							title : "Thời gian phát sóng ",
							field : 'endTime',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:right;"
							},
							type : 'text'
						}, {
							title : "Vật tư xuất ra",
							field : 'goodsName',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:right;"
							},
							type : 'text'
						}, {
							title : "Số lượng đã xuất",
							field : 'totalPrice',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:right;"
							},
							type : 'number'
						}, {
							title : "Giá trị hàng hóa xuất",
							field : 'amountReal',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:right;"
							},
							type : 'number'
						}, {
							title : "Thời gian thanh toán",
							field : 'datePayment',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:right;"
							},
							type : 'text'
						}, {
							title : "Số hóa đơn thanh toán",
							field : 'billCode',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:right;"
							},
							type : 'text'
						}, {
							title : "Giá trị thanh toán lên doanh thu",
							field : 'amount',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:right;"
							},
							type : 'number'
						}, ]
					});
		}
		
		vm.provinceOptions = {
		        dataTextField: "name",
		        dataValueField: "id",
				placeholder:"Nhập mã hoặc tên tỉnh",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.searchForm.provinceCode = dataItem.code;
					vm.searchForm.provinceName = dataItem.name;
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
		                    return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
		                        name: vm.searchForm.provinceName,
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
		        	if (!vm.isSelect) {
		                vm.searchForm.provinceCode = null;
						vm.searchForm.provinceName = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		                vm.searchForm.provinceCode = null;
						vm.searchForm.provinceName = null;
		            }
		        }
		    }
		
		vm.openCatProvincePopup = openCatProvincePopup;
		vm.onSaveCatProvince = onSaveCatProvince;
	    function openCatProvincePopup(){
			var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm tỉnh");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
	    }
	    function onSaveCatProvince(data){
	        vm.searchForm.provinceCode = data.code;
			vm.searchForm.provinceName = data.name;
	        htmlCommonService.dismissPopup();
	    };
		
		vm.exportFile = function exportFile(){
			kendo.ui.progress($("#reportFollowErpAmsWoGridId"), true);
			vm.searchForm.page = null;
            vm.searchForm.pageSize = null;
            vm.listRemove = [];
            vm.listConvert = [];
            reportFollowErpAmsWoService.doSearch(vm.searchForm).then(function (d) {
                CommonService.exportFile(vm.reportFollowErpAmsWoGrid, d.data, vm.listRemove, vm.listConvert, "Báo cáo theo dõi Tài chính - Tài sản - WO");
                kendo.ui.progress($("#reportFollowErpAmsWoGridId"), false);
            });
		}
		// end controller
	}
})();