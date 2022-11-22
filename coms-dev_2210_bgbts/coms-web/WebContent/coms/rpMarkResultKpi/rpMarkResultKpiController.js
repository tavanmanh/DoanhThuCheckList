(function() {
	'use strict';
	var controllerId = 'rpMarkResultKpiController';

	angular.module('MetronicApp').controller(controllerId,
			rpMarkResultKpiController);

	function rpMarkResultKpiController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, rpMarkResultKpiService,
			CommonService, PopupConst, Restangular, RestEndpoint, Constant,
			htmlCommonService) {
		var vm = this;
		vm.showSearch = true;
		vm.showDetail = false;
		vm.searchForm = {};
		vm.String = $rootScope.stringtile;

		init();
		function init() {
			vm.searchForm.monthYear = kendo.toString(new Date(), "MM/yyyy");
			fillDataTable([]);
		}

		vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.resultKpiGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.resultKpiGrid.showColumn(column);
			} else {
				vm.resultKpiGrid.hideColumn(column);
			}

		}

		vm.gridColumnShowHideFilter = function(item) {

			return item.type == null || item.type !== 1;
		};

		var record = 0;

		function fillDataTable(data) {
			vm.resultKpiGridOptions = kendoConfig
					.getGridOptions({
						autoBind : true,
						scrollable : true,
						resizable : true,
						editable : false,
						dataBinding : function() {
							record = (this.dataSource.page() - 1)
									* this.dataSource.pageSize();
						},
						reorderable : true,
						toolbar : [ {
							name : "actions",
							template : '<div class=" pull-left ">'
									+ '</div>'
									+ '<div class="btn-group pull-right margin_top_button ">'
									+ '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: VNĐ</div>'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
//									+ '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>'
									+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
									+ '<label ng-repeat="column in vm.resultKpiGrid.columns.slice(1,vm.resultKpiGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}'
									+ '</label>' + '</div></div>'
						} ],
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									$("#appCountResultKpi").text(
											"" + response.total);
									vm.countResultKpi = response.total;
									return response.total;
								},
								data : function(response) {
									return response.data;
								}
							},
							transport : {
								read : {
									// Thuc hien viec goi service
									url : Constant.BASE_SERVICE_URL
											+ "progressTaskOsRsService/doSearchBaoCaoChamDiemKpi",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									vm.searchForm.page = options.page;
									vm.searchForm.pageSize = options.pageSize;
//									vm.searchForm.monthYear = $("#monthYearId").val();
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
						columns : [
								{
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
									editable : false
								},
								{
									title : "Khu vực",
									field : 'areaCode',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',
									editable : false
								},
								{
									title : "Tỉnh",
									field : 'provinceCode',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',
									editable : false
								},
//								{
//									title : "",
//									field : '',
//									width : '150px',
//									headerAttributes : {
//										style : "text-align:center;font-weight: bold;"
//									},
//									attributes : {
//										style : "text-align:left;"
//									},
//									type : 'text',
//									editable : false
//								},
								{
									title : "Tổng điểm đạt ngoài OS",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',
									columns : [
										{
											title : "Điểm đạt",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "16",
													field : 'diemDatTong',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "Điểm thưởng",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "1",
													field : 'diemThuongTong',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "Tổng điểm",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "17",
													field : 'tongDiem',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "Quy đổi về thang điểm",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "100",
													field : 'quyDoiDiem',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
									]
								},
								//Quỹ lương
								{
									title : "Quỹ lương",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',
									columns : [
										{
											title : "Kế hoạch",
											width : '150px',
											field : 'quyLuongTarget',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
										},
										{
											title : "Thực hiện",
											field : 'quyLuongComplete',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
										},
										{
											title : "Tỷ lệ hoàn thành",
											field : 'processQuyLuong',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
										},
										{
											title : "1.000",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm đạt",
													field : 'diemDatQuyLuong',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "0.000",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:center;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm thưởng",
													field : 'diemThuongQuyLuong',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
									]
								},
								//HSHC xây lắp
								{
									title : "HSHC xây lắp",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',
									columns : [
										{
											title : "Điểm chuẩn",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Kế hoạch",
													field : 'hshcXlTarget',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Thực hiện",
													field : 'hshcXlComplete',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Tỷ lệ hoàn thành",
													field : 'processHshcXl',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "2.5",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm đạt",
													field : 'diemDatHschXl',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "0.25",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm thưởng",
													field : 'diemThuongHshcXl',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
									]
								},
								//QT lên doanh thu nguồn CP
								{
									title : "QT lên doanh thu nguồn CP",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',
									columns : [
										{
											title : "Điểm chuẩn",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Kế hoạch",
													field : 'revenueCpTarget',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Thực hiện",
													field : 'revenueCpComplete',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Tỷ lệ hoàn thành",
													field : 'processRevenueCp',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "1.0",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm đạt",
													field : 'diemDatRevenueCp',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "0.25",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm thưởng",
													field : 'diemThuongRevenueCp',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
									]
								},
								//QT lên doanh thu NTĐ (XDDD)
								{
									title : "QT lên doanh thu NTĐ (XDDD)",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',
									columns : [
										{
											title : "Điểm chuẩn",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Kế hoạch",
													field : 'revenueNtdXdddTarget',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Thực hiện",
													field : 'revenueNtdXdddComplete',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Tỷ lệ hoàn thành",
													field : 'processRevenueNtdXddd',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "1.0",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm đạt",
													field : 'diemDatRevenueNtdXddd',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "0.000",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm thưởng",
													field : 'diemThuongRevenueNtdXddd',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
									]
								},
								//Sản lượng xây lắp
								{
									title : "Sản lượng xây lắp",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',
									columns : [
										{
											title : "Điểm chuẩn",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Kế hoạch",
													field : 'quantityXlTarget',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Thực hiện",
													field : 'quantityXlComplete',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Tỷ lệ hoàn thành",
													field : 'processQuantityXl',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "1.5",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm đạt",
													field : 'diemDatQuantityXl',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "0",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm thưởng",
													field : 'diemThuongQuantityXl',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
									]
								},
								//Sản lượng nguồn chi phí
								{
									title : "Sản lượng nguồn chi phí",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',
									columns : [
										{
											title : "Điểm chuẩn",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Kế hoạch",
													field : 'quantityCpTarget',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Thực hiện",
													field : 'quantityCpComplete',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Tỷ lệ hoàn thành",
													field : 'processQuantityCp',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "1",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm đạt",
													field : 'diemDatQuantityCp',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "0",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm thưởng",
													field : 'diemThuongQuantityCp',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
									]
								},
								//Sản lượng NTĐ (XDDD)
								{
									title : "Sản lượng NTĐ (XDDD)",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',
									columns : [
										{
											title : "Điểm chuẩn",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Kế hoạch",
													field : 'quantityNtdXdddTarget',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Thực hiện",
													field : 'quantityNtdXdddComplete',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Tỷ lệ hoàn thành",
													field : 'processQuantityNtdXddd',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "1.5",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm đạt",
													field : 'diemDatQuantityNtdXddd',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "0",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm thưởng",
													field : 'diemThuongQuantityNtdXddd',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
									]
								},
								//Tìm kiếm việc XDDD
								{
									title : "Tìm kiếm việc XDDD",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',
									columns : [
										{
											title : "Điểm chuẩn",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											columns : [
												{
													title : "Kế hoạch",
													field : 'taskXdddTarget',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Thực hiện",
													field : 'taskXdddComplete',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Tỷ lệ hoàn thành",
													field : 'processTaskXddd',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "1.0",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm đạt",
													field : 'diemDatTaskXddd',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "0.25",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm thưởng",
													field : 'diemThuongTaskXddd',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
									]
								},
								//Thu hồi dòng tiền
								{
									title : "Thu hồi dòng tiền",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',
									columns : [
										{
											title : "Điểm chuẩn",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											columns : [
												{
													title : "Kế hoạch",
													field : 'revokeCashTarget',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Thực hiện",
													field : 'revokeCashComplete',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Tỷ lệ hoàn thành",
													field : 'processRevokeCash',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "1.5",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm đạt",
													field : 'diemDatRevokeCash',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "0.25",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm thưởng",
													field : 'diemThuongRevokeCash',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
									]
								},
								//Triển khai HTCT
								{
									title : "Triển khai HTCT (Trạm)",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',
									columns : [
										{
											title : "Điểm chuẩn",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Kế hoạch",
													field : 'tkHtctTarget',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Thực hiện",
													field : 'tkHtctComplete',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Tỷ lệ hoàn thành",
													field : 'processTkHtct',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "4",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm đạt",
													field : 'diemDatTkHtct',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "0",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm thưởng",
													field : 'diemThuongTkHtct',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
									]
								},
								//Triển khai HTCT (Xd móng)
								{
									title : "Triển khai HTCT (Xd móng) (Trạm)",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',
									columns : [
										{
											title : "Điểm chuẩn",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											columns : [
												{
													title : "Kế hoạch",
													field : 'mongHtctTarget',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Thực hiện",
													field : 'mongHtctComplete',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Tỷ lệ hoàn thành",
													field : 'processMongHtct',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "1",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm đạt",
													field : 'diemDatMongHtct',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "0",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm thưởng",
													field : 'diemThuongMongHtct',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
									]
								},
								//Triển khai HTCT (Xong ĐB trạm)
								{
									title : "Triển khai HTCT (Xong ĐB trạm) (Trạm)",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',
									columns : [
										{
											title : "Điểm chuẩn",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Kế hoạch",
													field : 'dbHtctTarget',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Thực hiện",
													field : 'dbHtctComplete',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Tỷ lệ hoàn thành",
													field : 'processDbHtct',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "1",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm đạt",
													field : 'diemDatDbHtct',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "0",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm thưởng",
													field : 'diemThuongDbHtct',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
									]
								},
								//Thuê mặt bằng
								{
									title : "Thuê mặt bằng (Trạm)",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',
									columns : [
										{
											title : "Điểm chuẩn",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Kế hoạch",
													field : 'rentHtctTarget',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Thực hiện",
													field : 'rentHtctComplete',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
												{
													title : "Tỷ lệ hoàn thành",
													field : 'processRentHtct',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "2",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm đạt",
													field : 'diemdatRentHtct',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
										{
											title : "0",
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:left;"
											},
											type : 'text',
											columns : [
												{
													title : "Điểm thưởng",
													field : 'diemThuongRentHtct',
													width : '150px',
													headerAttributes : {
														style : "text-align:center;font-weight: bold;"
													},
													attributes : {
														style : "text-align:right;"
													},
													type : 'text',
													editable : false
												},
											]
										},
									]
								},
								//
								
					]
					});
		}
		
		vm.doSearch = doSearch;
		function doSearch(){
			vm.showDetail = false;
//			vm.searchForm.monthYear = $("#monthYearId").val();
			var grid = vm.resultKpiGrid;
			if (grid) {
				grid.dataSource.query({
					page : 1,
					pageSize : 10
				});
			}
		}
		
		vm.provinceSearchOptions = {
		        dataTextField: "code",
		        dataValueField: "id",
				placeholder:"Nhập mã hoặc tên tỉnh",
		        select: function (e) {
		        		vm.isSelect = true;
			            var dataItem = this.dataItem(e.item.index());
			            vm.searchForm.provinceCode = dataItem.code;
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
		                        name: vm.searchForm.provinceCode,
		                        pageSize: vm.provinceSearchOptions.pageSize,
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
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.searchForm.provinceCode = null;
		            }
		        }
		    }
		
		vm.openCatProvinceSearch = openCatProvinceSearch;
		function openCatProvinceSearch(param){
			var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm tỉnh");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, param, 'ggfd', false, '75%','75%','catProvinceSearchController');
	    }
	    
		vm.onSaveCatProvinceSearch = onSaveCatProvinceSearch;
		function onSaveCatProvinceSearch(dataItem){
			vm.searchForm.provinceCode = dataItem.code;
	        htmlCommonService.dismissPopup();
	    };
		
		vm.exportFile = function(){
			kendo.ui.progress($("#resultKpiGrid"), true);
			return Restangular.all("progressTaskOsRsService/exportFileBaoCaoChamDiemKpi").post(vm.searchForm).then(function (d) {
        	    var data = d.plain();
        	    kendo.ui.progress($("#resultKpiGrid"), false);
        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	}).catch(function (e) {
        		kendo.ui.progress($("#resultKpiGrid"), false);
        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        	    return;
        	});
		}
		
		vm.clearProvince = function(){
			vm.searchForm.provinceCode = null;
		}
		
		vm.clearDate = function(){
			vm.searchForm.monthYear = null;
		}
		// end controller
	}
})();