(function() {
	'use strict';
	var controllerId = 'reportMassSearchConstructionController';

	angular.module('MetronicApp').controller(controllerId,
			reportMassSearchConstructionController);

	function reportMassSearchConstructionController($scope, $templateCache, $rootScope,
			$timeout, gettextCatalog, $filter, kendoConfig, $kWindow,
			reportMassSearchConstructionService, htmlCommonService, CommonService,
			PopupConst, Restangular, RestEndpoint, Constant, $http) {

		var vm = this;
		vm.String = "Quản lý công trình > Báo cáo > Báo cáo tổng hợp tháng BTS";
		vm.searchForm = {
			typeBc: "1"
		};

		init();
		function init() {
			vm.searchForm.thang = kendo.toString(new Date(), "MM/yyyy");
			fillDataTable([]);
		}

		vm.doSearch = doSearch;
		function doSearch() {
			vm.showDetail = false;
			var grid = vm.reportMassSearchConstructionGrid;
			if (grid) {
				grid.dataSource.query({
					page : 1,
					pageSize : 10
				});
			}
		}

		vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.reportMassSearchConstructionGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.reportMassSearchConstructionGrid.showColumn(column);
			} else {
				vm.reportMassSearchConstructionGrid.hideColumn(column);
			}
		}

		vm.gridColumnShowHideFilter = function(item) {
			return item.type == null || item.type !== 1;
		};
		
		// Grid theo huyện
		var record = 0;
		function fillDataTable(data) {
			kendo.ui.progress($("#reportMassSearchConstructionGrid"), true);
			vm.reportMassSearchConstructionGridOptions = kendoConfig
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
							template : '<div class="btn-group pull-right margin_top_button ">'
									 + '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: VNĐ</div>'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
									+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'
									+ '<label ng-repeat="column in vm.reportMassSearchConstructionGrid.columns.slice(1,vm.reportMassSearchConstructionGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}'
									+ '</label>' + '</div>' + '</div>'
						} ],
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									if(response.total==null){
										response.total = 0;
									}
									$("#appCountMassSearchConstruction").text(
											"" + response.total);
									vm.reportMassSearchConstructionCount = response.total;
									return response.total;
								},
								data : function(response) {
									kendo.ui.progress($("#reportMassSearchConstructionGrid"), false);
									return response.data;
								}
							},
							transport : {
								read : {
									url : Constant.BASE_SERVICE_URL
											+ "reportServiceRest/doSearchReportMassSearchConstr",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									vm.searchForm.page = options.page;
									vm.searchForm.pageSize = options.pageSize;
									vm.searchForm.thang = $("#dateGeneral").val();
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
									title : "STT",
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
									locked: true,
								},
//								{
//									title : "Mã đơn vị",
//									field : 'districtName',
//									width : '150px',
//									headerAttributes : {
//										style : "text-align:center;font-weight: bold;"
//									},
//									attributes : {
//										style : "text-align:left;"
//									},
//									type : 'text'
//								},
								{
									title : "Tháng",
									field : 'month',
									width : '80px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',
									locked: true,
								},
								{
									title : "Năm",
									field : 'year',
									width : '80px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',
									locked: true,
								},
								{
									title : "Mã tỉnh",
									field : 'provinceCode',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',
									locked: true,
								},
								{
									title : "TMB",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									columns: [
										{
											title : "Kế hoạch",
											field : 'planTMB',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											footerTemplate: function(dataItem){
												var grid = vm.reportMassSearchConstructionGrid.dataSource.data();
												return grid[0] ? grid[0].sumPlanTMB : 0;
											}
										},
										{
											title : "Thực hiện",
											field : 'performTMB',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											footerTemplate: function(dataItem){
												var grid = vm.reportMassSearchConstructionGrid.dataSource.data();
												return grid[0] ? grid[0].sumPerformTMB : 0;
											}
										},
										{
											title : "Tỷ lệ",
											field : 'ratioTMB',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											template: function(dataItem){
												return kendo.toString(dataItem.ratioTMB, "# \\\%");
											},
											footerTemplate: function(dataItem){
												var grid = vm.reportMassSearchConstructionGrid.dataSource.data();
												return grid[0] ? grid[0].sumRatioTMB : 0;
											}
										},
									]
								},
								{
									title : "Xong xây dựng",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									columns: [
										{
											title : "Kế hoạch",
											field : 'planKC',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											footerTemplate: function(dataItem){
												var grid = vm.reportMassSearchConstructionGrid.dataSource.data();
												return grid[0] ? grid[0].sumPlanKC : 0;
											}
										},
										{
											title : "Thực hiện",
											field : 'performKC',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											footerTemplate: function(dataItem){
												var grid = vm.reportMassSearchConstructionGrid.dataSource.data();
												return grid[0] ? grid[0].sumPerformKC : 0;
											}
										},
										{
											title : "Tỷ lệ",
											field : 'ratioKC',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											template: function(dataItem){
												return kendo.toString(dataItem.ratioKC, "# \\\%");
											},
											footerTemplate: function(dataItem){
												var grid = vm.reportMassSearchConstructionGrid.dataSource.data();
												return grid[0] ? grid[0].sumRatioKC : 0;
											}
										},
									]
								},
								{
									title : "ĐBHT",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									columns: [
										{
											title : "Kế hoạch",
											field : 'planDBHT',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											footerTemplate: function(dataItem){
												var grid = vm.reportMassSearchConstructionGrid.dataSource.data();
												return grid[0] ? grid[0].sumPlanDBHT : 0;
											}
										},
										{
											title : "Thực hiện",
											field : 'performDBHT',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											footerTemplate: function(dataItem){
												var grid = vm.reportMassSearchConstructionGrid.dataSource.data();
												return grid[0] ? grid[0].sumPerformDBHT : 0;
											}
										},
										{
											title : "Tỷ lệ",
											field : 'ratioDBHT',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											template: function(dataItem){
												return kendo.toString(dataItem.ratioDBHT, "# \\\%");
											},
											footerTemplate: function(dataItem){
												var grid = vm.reportMassSearchConstructionGrid.dataSource.data();
												return grid[0] ? grid[0].sumRatioDBHT : 0;
											}
										},
									]
								},
								{
									title : "PS",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									columns: [
										{
											title : "Kế hoạch",
											field : 'planPS',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											footerTemplate: function(dataItem){
												var grid = vm.reportMassSearchConstructionGrid.dataSource.data();
												return grid[0] ? grid[0].sumPlanPS : 0;
											}
										},
										{
											title : "Thực hiện",
											field : 'performPS',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											footerTemplate: function(dataItem){
												var grid = vm.reportMassSearchConstructionGrid.dataSource.data();
												return grid[0] ? grid[0].sumPerformPS : 0;
											}
										},
										{
											title : "Tỷ lệ",
											field : 'ratioPS',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											template: function(dataItem){
												return kendo.toString(dataItem.ratioPS, "# \\\%");
											},
											footerTemplate: function(dataItem){
												var grid = vm.reportMassSearchConstructionGrid.dataSource.data();
												return grid[0] ? grid[0].sumRatioPS : 0;
											}
										},
									]
								},
								{
									title : "HSHC",
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									columns: [
										{
											title : "Kế hoạch",
											field : 'planHSHC',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											footerTemplate: function(dataItem){
												var grid = vm.reportMassSearchConstructionGrid.dataSource.data();
												return grid[0] ? grid[0].sumPlanHSHC : 0;
											}
										},
										{
											title : "Thực hiện",
											field : 'performHSHC',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											footerTemplate: function(dataItem){
												var grid = vm.reportMassSearchConstructionGrid.dataSource.data();
												return grid[0] ? grid[0].sumPerformHSHC : 0;
											}
										},
										{
											title : "Tỷ lệ",
											field : 'ratioHSHC',
											width : '150px',
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:right;"
											},
											type : 'text',
											template: function(dataItem){
												return kendo.toString(dataItem.ratioHSHC, "# \\\%");
											},
											footerTemplate: function(dataItem){
												var grid = vm.reportMassSearchConstructionGrid.dataSource.data();
												return grid[0] ? grid[0].sumRatioHSHC : 0;
											}
										},
									]
								},
								]
					});
		}
		
		vm.exportFile = function(){
			kendo.ui.progress($("#reportMassSearchConstructionGrid"), true);
        	vm.searchForm.page = null;
        	vm.searchForm.pageSize = null;
        	vm.searchForm.status = "Doanh thu";
        	vm.searchForm.thang = $("#dateGeneral").val();
            return Restangular.all("reportServiceRest/exportRpMassSearchConstr").post(vm.searchForm).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                kendo.ui.progress($("#reportMassSearchConstructionGrid"), false);
            }).catch(function (e) {
            	kendo.ui.progress($("#reportMassSearchConstructionGrid"), false);
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                return;
            });
		}
		
		vm.selectedDept = false;
		vm.proviceOptions = {
			dataTextField : "name",
			placeholder : "Nhập mã, tên của tỉnh",
			select : function(e) {
				vm.selectedDept = true;
				var dataItem = this.dataItem(e.item.index());
				vm.searchForm.provinceCode = dataItem.code;
				vm.searchForm.provinceName = dataItem.name;
			},
			pageSize : 10,
			open: function (e) {
		        vm.selectedDept = false;
		    },
			dataSource: {
		        serverFiltering: true,
		        transport: {
		            read: function (options) {
		                vm.selectedDept = false;
		                return Restangular.all('catProvinceServiceRest/catProvince/doSearchProvinceInPopup').post({
		                    name: vm.searchForm.provinceName,
		                    pageSize: vm.proviceOptions.pageSize,
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
			change : function(e) {
				if (e.sender.value() === '') {
					vm.searchForm.provinceCode = null;
					vm.searchForm.provinceName = null;
				}
			},
			ignoreCase : false
		};
		
		// Controller end
	}
})();