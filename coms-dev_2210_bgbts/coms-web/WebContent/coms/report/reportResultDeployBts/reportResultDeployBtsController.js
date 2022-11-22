(function() {
	'use strict';
	var controllerId = 'reportResultDeployBtsController';

	angular.module('MetronicApp').controller(controllerId,
			reportResultDeployBtsController);

	function reportResultDeployBtsController($scope, $templateCache, $rootScope,
			$timeout, gettextCatalog, $filter, kendoConfig, $kWindow,
			reportResultDeployBtsService, htmlCommonService, CommonService,
			PopupConst, Restangular, RestEndpoint, Constant, $http) {

		var vm = this;
		vm.String = "Quản lý công trình > Báo cáo > Báo cáo chi tiết triển khai BTS";
		vm.searchForm = {
				dateType: "1"
		};

		init();
		function init() {
			var date = new Date();
        	var start = new Date(date.getFullYear(), date.getMonth(), 1);
//        	var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
        	vm.searchForm.dateFrom = htmlCommonService.formatDate(start);
        	vm.searchForm.dateTo = htmlCommonService.formatDate(new Date());
			fillDataTable([]);
		}

		vm.dateTypeList = [
			{
				code: "1",
				name: "Ngày thuê MB"
			},
			{
				code: "2",
				name: "Ngày hoàn thànhTK-DT"
			},
			{
				code: "3",
				name: "Ngày hoàn thành thẩm TK-DT"
			},
			{
				code: "4",
				name: "Ngày ra QĐ TK_DT"
			},
			{
				code: "5",
				name: "Ngày khởi công"
			},
			{
				code: "6",
				name: "Ngày đồng bộ"
			},
			{
				code: "7",
				name: "Ngày phát sóng"
			},
			{
				code: "8",
				name: "Ngày HSHC gửi TTHT"
			},
			{
				code: "9",
				name: "Ngày KPI HSHC"
			},
		]
		
		vm.doSearch = doSearch;
		function doSearch() {
			vm.showDetail = false;
			var grid = vm.reportResultDeployBtsGrid;
			if (grid) {
				grid.dataSource.query({
					page : 1,
					pageSize : 10
				});
			}
		}

		vm.resetFormField = function(param){
			if(param=='date'){
				vm.searchForm.dateFrom = null;
				vm.searchForm.dateTo = null;
			}
		}
		
		vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.reportResultDeployBtsGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.reportResultDeployBtsGrid.showColumn(column);
			} else {
				vm.reportResultDeployBtsGrid.hideColumn(column);
			}
		}

		vm.gridColumnShowHideFilter = function(item) {
			return item.type == null || item.type !== 1;
		};
		
		// Grid theo huyện
		var record = 0;
		function fillDataTable(data) {
			kendo.ui.progress($("#reportResultDeployBtsGrid"), true);
			vm.reportResultDeployBtsGridOptions = kendoConfig
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
									+ '<label ng-repeat="column in vm.reportResultDeployBtsGrid.columns.slice(1,vm.reportResultDeployBtsGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
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
									$("#appCountResultDeployBts").text(
											"" + response.total);
									vm.reportResultDeployBts = response.total;
									return response.total;
								},
								data : function(response) {
									kendo.ui.progress($("#reportResultDeployBtsGrid"), false);
									return response.data;
								}
							},
							transport : {
								read : {
									url : Constant.BASE_SERVICE_URL
											+ "reportServiceRest/doSearchReportResultDeployBts",
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
									type : 'text'

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
									type : 'text'
								},
								{
									title : "Mã VTN",
									field : 'tramVTN',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text'
								},
								{
									title : "Mã VCC",
									field : 'tramVCC',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text'
								},
								{
									title : "Dự án",
									field : 'contractCode',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text'
								},
								{
									title : "Ngày thuê MB",
									field : 'ngayThueMb',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text'
								},
								{
									title : "Ngày hoàn thànhTK-DT",
									field : 'ngayHoanThanhTkdt',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text'
								},
								{
									title : "Ngày hoàn thành thẩm TK-DT",
									field : 'ngayHoanThanhThamDinh',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text'
								},
								{
									title : "Ngày ra QĐ TK_DT",
									field : 'ngayRaQuyetDinhTkdt',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text'
								},
								{
									title : "Ngày khởi công",
									field : 'ngayKhoiCong',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text'
								},
								{
									title : "Ngày đồng bộ",
									field : 'ngayDongBo',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text'
								},
								{
									title : "Ngày phát sóng",
									field : 'ngayPhatSong',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text'
								},
								{
									title : "Ngày HSHC gửi TTHT",
									field : 'ngayHshcGuiTtht',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text'
								},
								{
									title : "Ngày KPI HSHC",
									field : 'ngayKpiHshc',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text'
								},
								{
									title : "Quá hạn KPI HSHC",
									field : 'quaHanKpi',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text'
								},
								]
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
		
		vm.exportFile = function(){
			kendo.ui.progress($("#reportResultDeployBtsGrid"), true);
			vm.searchForm.page = null;
        	vm.searchForm.pageSize = null;
        	vm.listRemove = [];
        	vm.listConvert = [];
        	reportResultDeployBtsService.doSearch(vm.searchForm).then(function(data){
        		kendo.ui.progress($("#reportResultDeployBtsGrid"), false);
        		CommonService.exportFile(vm.reportResultDeployBtsGrid, data.data, vm.listRemove, vm.listConvert, "Báo cáo chi tiết triển khai BTS");
        	}).catch(function(e){
        		kendo.ui.progress($("#reportResultDeployBtsGrid"), false);
        		toastr.error("Có lỗi xảy ra khi xuất file !");
        	});
		}
		// Controller end
	}
})();