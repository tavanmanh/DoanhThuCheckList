(function() {
	'use strict';
	var controllerId = 'rpMarkWoKpiController';

	angular.module('MetronicApp').controller(controllerId,
			rpMarkWoKpiController);

	function rpMarkWoKpiController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, rpMarkWoKpiService,
			CommonService, PopupConst, Restangular, RestEndpoint, Constant,
			htmlCommonService) {
		var vm = this;
		vm.showSearch = true;
		vm.showDetail = false;
		vm.searchForm = {};
		vm.String = $rootScope.stringtile;

		init();
		function init() {
			vm.searchForm.fullYear = kendo.toString(new Date(), "MM/yyyy");
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
											+ "woService/doSearchBaoCaoChamDiemKpi",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									vm.searchForm.page = options.page;
									vm.searchForm.pageSize = options.pageSize;
									  if(vm.searchForm.fullYear != null){
                                    vm.searchForm.month = vm.searchForm.fullYear.split("/")[0];
                                    vm.searchForm.year = vm.searchForm.fullYear.split("/")[1];
                                 }
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
									field : 'proCode',
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
											field : 'salaryMonth',
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
											field : 'salaryWo',
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
											field : 'salaryPercent',
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
													field : 'salaryDat',
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
													field : 'salaryThuong',
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
													field : 'hshcXayLapKH',
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
													field : 'hshcXayLapWo',
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
													field : 'hshcXayLapPercent',
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
													field : 'hshcXayLapDat',
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
													field : 'hshcXayLapThuong',
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
													field : 'qtKH',
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
													field : 'qtWo',
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
													field : 'qtPercent',
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
													field : 'qtDat',
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
													field : 'qtThuong',
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
													field : 'qtGPDNKH',
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
													field : 'qtGPDNWo',
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
													field : 'qtGPDNPercent',
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
													field : 'qtGPDNDat',
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
													field : 'qtGPDNThuong',
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
													field : 'qtSLXLKH',
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
													field : 'qtSLXLWo',
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
													field : 'qtSLXLPercent',
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
													field : 'qtSLXLDat',
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
													field : 'qtSLXLThuong',
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
													field : 'slKH',
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
													field : 'slWo',
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
													field : 'slPercent',
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
													field : 'slDat',
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
													field : 'slThuong',
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
													field : 'slGPDNKH',
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
													field : 'slGPDNWo',
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
													field : 'slGPDNPercent',
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
													field : 'slGPDNDat',
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
													field : 'slGPDNThuong',
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
													field : 'tkvKH',
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
													field : 'tkvWo',
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
													field : 'tkvPercent',
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
													field : 'tkvDat',
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
													field : 'tkvThuong',
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
													field : 'thdtKH',
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
													field : 'thdtWo',
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
													field : 'thdtPercent',
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
													field : 'thdtDat',
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
													field : 'thdtThuong',
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
													field : 'ttkKH',
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
													field : 'ttkWo',
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
													field : 'ttkPercent',
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
													field : 'ttkDat',
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
													field : 'ttkThuong',
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
													field : 'xdXMKH',
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
													field : 'xdXMWo',
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
													field : 'xdXMPercent',
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
													field : 'xdXMDat',
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
													field : 'xdXMThuong',
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
													field : 'xDBKH',
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
													field : 'xDBWo',
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
													field : 'xDBPercent',
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
													field : 'xDBDat',
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
													field : 'xDBThuong',
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
													field : 'tmbKH',
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
													field : 'tmbWo',
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
													field : 'tmbPercent',
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
													field : 'tmbDat',
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
													field : 'tmbThuong',
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

		vm.openDepartmentTo1=openDepartmentTo1

		function openDepartmentTo1(popUp){
			vm.obj={};
			vm.departmentpopUp=popUp;
			var templateUrl = 'wms/popup/findDepartmentPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm đơn vị");
			CommonService.populatePopupDept(templateUrl, title, null,null, vm, popUp, 'string', false, '92%','89%');
		}

		vm.selectedDept1=false;
		vm.deprtOptions1 = {
			dataTextField: "text",
			dataValueField:"id",
			select: function(e) {
				vm.selectedDept1=true;
				var dataItem = this.dataItem(e.item.index());
				vm.searchForm.sysGroupName = dataItem.text;
				vm.searchForm.sysGroupId = dataItem.id;
			},
			pageSize: 10,
			open: function(e) {
				vm.selectedDept1 = false;
			},
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						vm.selectedDept1=false;
						return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({name:vm.searchForm.sysGroupName,pageSize:vm.deprtOptions1.pageSize}).then(function(response){
							options.success(response);
						}).catch(function (err) {
							console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
						});
					}
				}
			},
			template:'<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
			change: function(e) {
				if (e.sender.value() === '') {
					vm.searchForm.sysGroupName = null;// thành name
					vm.searchForm.sysGroupId = null;
				}
			},
			ignoreCase: false
		}
		vm.cancelInput = cancelInput;
		function cancelInput(x){
			if(x==1){
				vm.searchForm.sysGroupName=null;
				vm.searchForm.sysGroupId=null;
			}
			if(x==2){
				vm.searchForm.progress=null;
			}
			if(x==3){
				vm.searchForm.fullYear=null;
				vm.searchForm.month=null;
				vm.searchForm.year=null;

			}

		}
		vm.monthSelectorOptions = {
			start: "year",
			depth: "year"
		};
		vm.exportFile = function(){
			kendo.ui.progress($("#resultKpiGrid"), true);
			return Restangular.all("woService/exportFileWoKpi").post(vm.searchForm).then(function (d) {
        	    var data = d.plain();
        	    kendo.ui.progress($("#resultKpiGrid"), false);
        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	}).catch(function (e) {
        		kendo.ui.progress($("#resultKpiGrid"), false);
        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        	    return;
        	});
		}
		vm.onSave = onSave;
		function onSave(data) {
			if (vm.departmentpopUp === 'dept') {
				vm.searchForm.sysGroupName = data.text;
				vm.searchForm.sysGroupId = data.id;
			}
		}

		vm.cancelInput = cancelInput;
		function cancelInput(x){
			if(x==1){
				vm.searchForm.sysGroupName=null;
				vm.searchForm.sysGroupId=null;
			}
			if(x==2){
				vm.searchForm.progress=null;
			}
			if(x==3){
				vm.searchForm.fullYear=null;
				vm.searchForm.month=null;
				vm.searchForm.year=null;

			}

		}
		vm.clearDate = function(){
			vm.searchForm.monthYear = null;
		}
		// end controller
	}
})();