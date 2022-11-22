(function() {
	'use strict';
	var controllerId = 'reportCostOfSalesController';

	angular.module('MetronicApp').controller(controllerId,
			reportCostOfSalesController);

	function reportCostOfSalesController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, reportCostOfSalesService,
			CommonService, PopupConst, Restangular, RestEndpoint, Constant,
			htmlCommonService) {
		var vm = this;
		vm.showSearch = true;
		vm.showDetail = false;
		vm.searchForm = {
			type: 1
		};
		vm.String = $rootScope.stringtile;
		vm.monthYear1 = "";
		vm.monthYear2 = "";
		vm.monthYear3 = "";
		vm.year1 = "";
		vm.year2 = "";
		vm.year3 = "";
		vm.area1 = "";
		vm.area2 = "";
		vm.area3 = "";
		vm.provinceCode1 = "";
		vm.provinceCode2 = "";
		vm.provinceCode3 = "";
		init();
		function init(){
//			vm.searchForm.monthYear = kendo.toString(new Date(), "MM/yyyy");
			fillDataTableDetailContract([]);
			fillDataTableTHProvince([]);
			fillDataTableDetailAllocation([]);
		}
		
		vm.listRemove=[{
			title: "Thao tác",
		}]
		vm.listConvert=[
			{
				field:"dasType",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"cdbrType",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"engineRoomDas",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"feederAntenDas",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"costOtherDas",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"axisCdbr",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"apartmentsAllCdbr",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"apartmentsCdbr",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"costOtherCdbr",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"engineRoomCdbr",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"engineRoomCableCdbr",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"effective",
				data:{
					1:'Có',
					0:'Không'
				}
			}
			
		]
		
		vm.clickRadio = function(){
//			vm.searchForm.keySearch = null;
			if(vm.searchForm.type == 1){
//				vm.searchForm.monthYear = (vm.monthYear1 != null && vm.monthYear1 != "") ? vm.monthYear1 : vm.searchForm.monthYear = kendo.toString(new Date(), "MM/yyyy");
				vm.searchForm.monthYear = vm.monthYear1;
				vm.searchForm.year = vm.year1;
				vm.searchForm.provinceCode = vm.provinceCode1;
				vm.searchForm.area = vm.area1;
			}
			if(vm.searchForm.type == 2){
//				vm.searchForm.monthYear = (vm.monthYear2 != null && vm.monthYear2 != "") ? vm.monthYear2 : vm.searchForm.monthYear = kendo.toString(new Date(), "MM/yyyy");
				vm.searchForm.monthYear = vm.monthYear2;
				vm.searchForm.year = vm.year2;
				vm.searchForm.provinceCode = vm.provinceCode2;
				vm.searchForm.area = vm.area2;
			}
			if(vm.searchForm.type == 3){
//				vm.searchForm.monthYear = (vm.monthYear3 != null && vm.monthYear3 != "") ? vm.monthYear3 : vm.searchForm.monthYear = kendo.toString(new Date(), "MM/yyyy");
				vm.searchForm.monthYear = vm.monthYear3;
				vm.searchForm.year = vm.year3;
				vm.searchForm.provinceCode = vm.provinceCode3;
				vm.searchForm.area = vm.area3;
			}
		}
		
		vm.showHideWorkItemColumnDas = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.detailContractGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.detailContractGrid.showColumn(column);
			} else {
				vm.detailContractGrid.hideColumn(column);
			}

		}

		vm.gridColumnShowHideFilterDas = function(item) {
			return item.type == null || item.type !== 1;
		};
		
		vm.showHideWorkItemColumnBts = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.reportCostOfSalesBtsGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.reportCostOfSalesBtsGrid.showColumn(column);
			} else {
				vm.reportCostOfSalesBtsGrid.hideColumn(column);
			}

		}

		vm.gridColumnShowHideFilterBts = function(item) {
			return item.type == null || item.type !== 1;
		};
		
		vm.doSearch = doSearch;
		function doSearch(){
//			if(vm.searchForm.monthYear == null || vm.searchForm.monthYear == ""){
//				toastr.warning("Tháng/Năm không được để trống !");
//            	return false;
//			}
			var grid = null;
			if(vm.searchForm.type=="1"){
				vm.monthYear1 = vm.searchForm.monthYear;
				vm.year1 = vm.searchForm.year;
				vm.provinceCode1 = vm.searchForm.provinceCode;
				vm.area1 = vm.searchForm.area;
				grid = vm.detailContractGrid;
			} else if(vm.searchForm.type=="2") {
				vm.monthYear2 = vm.searchForm.monthYear;
				vm.year2 = vm.searchForm.year;
				vm.provinceCode2 = vm.searchForm.provinceCode;
				vm.area2 = vm.searchForm.area;
				grid = vm.reportTHProvinceGrid;
			}else{
				vm.monthYear3 = vm.searchForm.monthYear;
				vm.year3 = vm.searchForm.year;
				vm.provinceCode3 = vm.searchForm.provinceCode;
				vm.area3 = vm.searchForm.area;
				grid = vm.detailAllocationGrid;
			}
			
			if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 10
				});
			}
		}
		
		var record = 0;
//		var stt = -1;
		function fillDataTableDetailContract(data) {
			vm.detailContractGridOptions = kendoConfig
			.getGridOptions({
				autoBind : true,
				scrollable : true,
				resizable : true,
				editable : false,
				dataBinding : function() {
					record = (this.dataSource.page() - 1)* this.dataSource.pageSize();
				},
				reorderable : true,
				toolbar : [ {
					name : "actions",
					template : '<div class=" pull-left ">'
							+ '</div>'
							+ '<div class="btn-group pull-right margin_top_button ">'
							+ '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: VNĐ</div>'
							+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
							+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
							+ '<label ng-repeat="column in vm.detailContractGrid.columns.slice(1,vm.detailContractGrid.columns.length)| filter: vm.gridColumnShowHideFilterDas">'
							+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumnDas(column)"> {{column.title}}'
							+ '</label>' + '</div></div>'
				} ],
				dataSource : {
					serverPaging : true,
					schema : {
						total : function(response) {
							$("#appreportCostOfSalesDas").text(
									"" + response.total);
							vm.countDetailContract = response.total;
							return response.total;
						},
						data : function(response) {
							return response.data;
						}
					},
					transport : {
						read : {
							url : Constant.BASE_SERVICE_URL
									+ "reportCostOfSalesService/doSearchDetailContract",
							contentType : "application/json; charset=utf-8",
							type : "POST"
						},
						parameterMap : function(options, type) {
							var obj = {};
							vm.searchForm.page = options.page;
							vm.searchForm.pageSize = options.pageSize;
							obj = angular.copy(vm.searchForm);
							obj.type = 1;
							return JSON.stringify(obj)
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
//								if(record <10){
//									return ++stt;
//								}else{
									return ++record;
//								}
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
							field : 'area',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false,
						},
						{
							title : "Tỉnh",
							field : 'provinceCode',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;"},
							attributes : { style : "text-align:center;"},
							type : 'text',
							editable : false,
						},
						{
							title : "Nội dung hợp đồng",
							field : 'contendContract',
							width : '200px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:left;" },
							type : 'text',
							editable : false
						},
						{
							title : "CĐT",
							field : 'cDT',
							width : '200px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:left;" },
							type : 'text',
							editable : false
						},
						{
							title : "Số hợp đồng",
							field : 'contractNumber',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false,
						},
						{
							title : "Giá trị HĐ trước thuế",
							field : 'prirceContract',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},
						
						{
							title : "Ngày ký",
							field : 'signDate',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},
						{
							title : "Ngày bắt đầu",
							field : 'startDate',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},
						{
							title : "Số ngày thực hiện theo HĐ",
							field : 'dayNumber',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},
						{
							title : "Ngày kết thúc ",
							field : 'endDate',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false,
						},
						{
							title : "Lọc cơ cấu",
							field : 'filter',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false,
						},
						{
							title : "Tháng ghi nhận",
							field : 'recordedInMonth',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false,
						},
						{
							title : "Nguồn việc nội bộ",
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							columns : [
								{
									title : "Mã nhân viên",
									field : 'employeeCode',
									width : '100px',
									headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
									attributes : { style : "text-align:center;" },
									type : 'text',
									editable : false,
								},
								{
									title : "Tên nhân viên",
									field : 'employeeName',
									width : '100px',
									headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
									attributes : { style : "text-align:center;" },
									type : 'text',
									editable : false,
								},
								{
									title : "Chức danh",
									field : 'tilte',
									width : '100px',
									headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
									attributes : { style : "text-align:center;" },
									type : 'text',
									editable : false,
								},
								{
									title : "Chi phí bán hàng",
									field : 'costOfSales',
									width : '100px',
									headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
									attributes : { style : "text-align:center;" },
									type : 'text',
									editable : false,
								},
							]},
						]
			});
		}
		
		var record2 = 0;
//		var stt2 = -1;
		function fillDataTableTHProvince(data) {
			vm.reportTHProvinceGridOptions = kendoConfig
			.getGridOptions({
				autoBind : true,
				scrollable : true,
				resizable : true,
				editable : false,
				dataBinding : function() {
					record2 = (this.dataSource.page() - 1)* this.dataSource.pageSize();
				},
				reorderable : true,
				toolbar : [ {
					name : "actions",
					template : '<div class=" pull-left ">'
							+ '</div>'
							+ '<div class="btn-group pull-right margin_top_button ">'
							+ '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: VNĐ</div>'
							+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
							+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
							+ '<label ng-repeat="column in vm.detailContractGrid.columns.slice(1,vm.detailContractGrid.columns.length)| filter: vm.gridColumnShowHideFilterDas">'
							+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumnDas(column)"> {{column.title}}'
							+ '</label>' + '</div></div>'
				} ],
				dataSource : {
					serverPaging : true,
					schema : {
						total : function(response) {
							$("#countTHProvince").text(
									"" + response.total);
							vm.countTHProvince = response.total;
							return response.total;
						},
						data : function(response) {
							return response.data;
						}
					},
					transport : {
						read : {
							url : Constant.BASE_SERVICE_URL
									+ "reportCostOfSalesService/doSearchTHProvince",
							contentType : "application/json; charset=utf-8",
							type : "POST"
						},
						parameterMap : function(options, type) {
							var obj = {};
							vm.searchForm.page = options.page;
							vm.searchForm.pageSize = options.pageSize;
							obj = angular.copy(vm.searchForm);
//							obj.type = 1;
							return JSON.stringify(obj)
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
//								if(record <10){
//									return ++stt2;
//								}else{
									return ++record2;
//								}
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
							title : "Tỉnh",
							field : 'provinceCode',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;"},
							attributes : { style : "text-align:center;"},
							type : 'text',
							editable : false,
						},
						{
							title : "Số tiền",
							field : 'totalMoney',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},{
							title : "Giám đốc",
							field : 'giamdoc',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},{
							title : "PGĐ kỹ thuật/TP kỹ thuật",
							field : 'pgdkythuat',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},{
							title : "PGĐ Hạ tầng/TP Hạ tầng",
							field : 'pgdhatang',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},{
							title : "PGĐ Kinh doanh/TP Kinh doanh",
							field : 'pgdkinhdoanh',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},{
							title : "P. Kỹ thuật",
							field : 'phongkythuat',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},{
							title : "P. Hạ tầng",
							field : 'phonghatang',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},{
							title : "P. Kinh doanh",
							field : 'phongkinhdoanh',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},{
							title : "Khối Hỗ trợ (TC, KHTH)",
							field : 'khoihotro',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},{
							title : "GĐ TTQH",
							field : 'gdttqh',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},{
							title : "NV bán hàng",
							field : 'employee',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},{
							title : "Tổng",
							field : 'totalMoney',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},
						]
			});
		}
		
		var record3 = 0;
//		var stt3 = -1;
		function fillDataTableDetailAllocation(data) {
			vm.detailAllocationGridOptions = kendoConfig
			.getGridOptions({
				autoBind : true,
				scrollable : true,
				resizable : true,
				editable : false,
				dataBinding : function() {
					record3 = (this.dataSource.page() - 1)* this.dataSource.pageSize();
				},
				reorderable : true,
				toolbar : [ {
					name : "actions",
					template : '<div class=" pull-left ">'
							+ '</div>'
							+ '<div class="btn-group pull-right margin_top_button ">'
							+ '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: VNĐ</div>'
							+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
							+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
							+ '<label ng-repeat="column in vm.detailContractGrid.columns.slice(1,vm.detailContractGrid.columns.length)| filter: vm.gridColumnShowHideFilterDas">'
							+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumnDas(column)"> {{column.title}}'
							+ '</label>' + '</div></div>'
				} ],
				dataSource : {
					serverPaging : true,
					schema : {
						total : function(response) {
							$("#appreportCostOfSalesDas").text(
									"" + response.total);
							vm.countDetailAllocation = response.total;
							return response.total;
						},
						data : function(response) {
							return response.data;
						}
					},
					transport : {
						read : {
							url : Constant.BASE_SERVICE_URL
									+ "reportCostOfSalesService/doSearchDetailAllocation",
							contentType : "application/json; charset=utf-8",
							type : "POST"
						},
						parameterMap : function(options, type) {
							var obj = {};
							vm.searchForm.page = options.page;
							vm.searchForm.pageSize = options.pageSize;
							obj = angular.copy(vm.searchForm);
//							obj.type = 1;
							return JSON.stringify(obj)
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
//								if(record <10){
//									return ++stt3;
//								}else{
									return ++record3;
//								}
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
							title : "Tỉnh",
							field : 'provinceCode',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;"},
							attributes : { style : "text-align:center;"},
							type : 'text',
							editable : false,
						},
						{
							title : "Số hợp đồng",
							field : 'contractNumber',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false,
						},
						{
							title : "Giá trị HĐ trước thuế",
							field : 'prirceContract',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},
						{
							title : "Chi phí bán hàng",
							field : 'costOfSales',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false,
						},
						{
							title : "Quỹ cho CN",
							field : 'branchFund',
							width : '100px',
							headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
							attributes : { style : "text-align:center;" },
							type : 'text',
							editable : false
						},
						{
							title : "Nguồn việc nội bộ",
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							columns : [
								{
									title : "Mã nhân viên",
									field : 'employeeCode',
									width : '100px',
									headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
									attributes : { style : "text-align:center;" },
									type : 'text',
									editable : false,
								},
								{
									title : "Số tiền",
									field : 'totalMoney',
									width : '100px',
									headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
									attributes : { style : "text-align:center;" },
									type : 'text',
									editable : false
								},{
									title : "Giám đốc",
									field : 'giamdoc',
									width : '100px',
									headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
									attributes : { style : "text-align:center;" },
									type : 'text',
									editable : false
								},{
									title : "PGĐ kỹ thuật/TP kỹ thuật",
									field : 'pgdkythuat',
									width : '100px',
									headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
									attributes : { style : "text-align:center;" },
									type : 'text',
									editable : false
								},{
									title : "PGĐ Hạ tầng/TP Hạ tầng",
									field : 'pgdhatang',
									width : '100px',
									headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
									attributes : { style : "text-align:center;" },
									type : 'text',
									editable : false
								},{
									title : "PGĐ Kinh doanh/TP Kinh doanh",
									field : 'pgdkinhdoanh',
									width : '100px',
									headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
									attributes : { style : "text-align:center;" },
									type : 'text',
									editable : false
								},{
									title : "P. Kỹ thuật",
									field : 'phongkythuat',
									width : '100px',
									headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
									attributes : { style : "text-align:center;" },
									type : 'text',
									editable : false
								},{
									title : "P. Hạ tầng",
									field : 'phonghatang',
									width : '100px',
									headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
									attributes : { style : "text-align:center;" },
									type : 'text',
									editable : false
								},{
									title : "P. Kinh doanh",
									field : 'phongkinhdoanh',
									width : '100px',
									headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
									attributes : { style : "text-align:center;" },
									type : 'text',
									editable : false
								},{
									title : "Khối Hỗ trợ (TC, KHTH)",
									field : 'khoihotro',
									width : '100px',
									headerAttributes : { style : "text-align:center;font-weight: bold;white-space:normal;" },
									attributes : { style : "text-align:center;" },
									type : 'text',
									editable : false
								},
							]},
						]
			});
		}
		
		vm.provinceSearchOptions = {
		        dataTextField: "name",
		        dataValueField: "id",
				placeholder:"Nhập mã hoặc tên tỉnh",
		        select: function (e) {
		        		vm.isSelect = true;
			            var dataItem = this.dataItem(e.item.index());
			            vm.searchForm.provinceCode = dataItem.code;
			            vm.searchForm.area = dataItem.name;
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
		                        name: vm.searchForm.area,
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
		        		vm.searchForm.catProvinceCode = null;
		        		vm.searchForm.catProvinceName = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.searchForm.catProvinceCode = null;
		        		vm.searchForm.catProvinceName = null;
		            }
		        }
		    }
		
		 vm.openCatProvinceSearch = openCatProvinceSearch;
		function openCatProvinceSearch(param){
			var templateUrl = 'views/popup/catProvinceSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm tỉnh");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, param, 'ggfd', false, '75%','75%','catProvinceSearchController');
	    }
	    
		vm.onSaveCatProvinceSearch = onSaveCatProvinceSearch;
		function onSaveCatProvinceSearch(dataItem){
			vm.searchForm.catProvinceCode = dataItem.code;
			vm.searchForm.catProvinceName = dataItem.name;
	        htmlCommonService.dismissPopup();
	    };
		
	    vm.exportFile= function(){
	    	vm.searchForm.page = null;
			vm.searchForm.pageSize = null;
	    		kendo.ui.progress($("#detailContractGrid"), true);
	    		var obj = {};
	    		obj = angular.copy(vm.searchForm);
				reportCostOfSalesService.exportFile(obj).then(function(d){
		    		  data = d.plain();
		    		  window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
		    		  vm.disableBtnExcel  = false;
		    		  kendo.ui.progress($("#detailContractGrid"), false);
				});

	    }
	    
	    vm.userSearchOptions = {
		        dataTextField: "staffName",
		        dataValueField: "staffId",
				placeholder:"Nhân viên tìm kiếm",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.searchForm.userSearchId = dataItem.staffId;
		            vm.searchForm.employeeName = dataItem.staffName;
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
		                    return Restangular.all("configStaffTangentRestService/catProvince/doSearchStaffByPopup").post({
		                        staffName: vm.searchForm.employeeName,
		                        pageSize: 10,
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
				'<p class="col-md-6 text-header-auto border-right-ccc">Mã NV</p>' +
				'<p class="col-md-6 text-header-auto">Tên NV</p>' +
				'</div>',
		        template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.staffCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.staffName #</div> </div>',
		        change: function (e) {
		        	if (!vm.isSelect) {
		        		vm.searchForm.userSearchId = null;
		        		vm.searchForm.employeeName = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.searchForm.userSearchId = null;
		            	vm.searchForm.employeeName = null;
		            }
		        }
		    }
	    
	}
})();