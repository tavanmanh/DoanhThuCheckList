(function() {
	'use strict';
	var controllerId = 'rpOrderlyWOController';

	angular.module('MetronicApp').controller(controllerId,
			rpOrderlyWOController);

	function rpOrderlyWOController($scope, $templateCache, $rootScope,
			$timeout, gettextCatalog, $filter, kendoConfig, $kWindow,
			rpOrderlyWOService, htmlCommonService, CommonService, PopupConst,
			Restangular, RestEndpoint, Constant, $http) {

		var vm = this;
		vm.String = "Quản lý công trình > Báo cáo > Báo cáo nền nếp phần mềm giao WO";
		vm.searchForm = {
			status : "1",
			typeBc : "1"
		};
		
		vm.showTableReceiveTH = true;
		vm.showTableAssignTH = false;
		vm.showTableAcceptTH = false;
		vm.showTableReceiveCT = false;
		vm.showTableAssignCT = false;
		vm.showTableAcceptCT = false;

		vm.listStatus = [ {
			text : "Tiếp nhận WO quá hạn",
			value : 1
		}, {
			text : "Giao WO quá hạn",
			value : 2
		}, {
			text : "Xác nhận kết quả hoàn thành quá hạn",
			value : 3
		} ];

		initData();
		function initData() {
			var date = new Date();
        	var start = new Date(date.getFullYear(), date.getMonth(), 1);
        	var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
        	vm.searchForm.startDate = htmlCommonService.formatDate(start);
        	vm.searchForm.endDate = htmlCommonService.formatDate(end);
			fillDataTableReceiveAll([]);
			fillDataTableAssignAll([]);
			fillDataTableAcceptAll([]);
			fillDataTableReceiveDetail([]);
			fillDataTableAssignDetail([]);
			fillDataTableAcceptDetail([]);
		}

		vm.showHideWorkItemColumn = function(column) {
			var grid = null;
			if(vm.searchForm.status=="1" && vm.searchForm.typeBc=="1"){
				grid = vm.recieveWoAllGrid;
			} else
			if(vm.searchForm.status=="2" && vm.searchForm.typeBc=="1"){
				grid = vm.assignWoAllGrid;
			} else
			if(vm.searchForm.status=="3" && vm.searchForm.typeBc=="1"){
				grid = vm.acceptWoAllGrid;
			} else
			if(vm.searchForm.status=="1" && vm.searchForm.typeBc=="2"){
				grid = vm.recieveWoDetailGrid;
			} else
			if(vm.searchForm.status=="2" && vm.searchForm.typeBc=="2"){
				grid = vm.assignWoDetailGrid;
			} else
			if(vm.searchForm.status=="3" && vm.searchForm.typeBc=="2"){
				grid = vm.acceptWoDetailGrid;
			}
			if (angular.isUndefined(column.hidden)) {
				grid.hideColumn(column);
			} else if (column.hidden) {
				grid.showColumn(column);
			} else {
				grid.hideColumn(column);
			}

		}
		vm.gridColumnShowHideFilter = function(item) {
			return item.type == null || item.type !== 1;
		};

		vm.doSearch = doSearch;
		function doSearch() {
			var grid = null;
			if(vm.searchForm.status=="1" && vm.searchForm.typeBc=="1"){
				vm.showTableReceiveTH = true;
				vm.showTableAssignTH = false;
				vm.showTableAcceptTH = false;
				vm.showTableReceiveCT = false;
				vm.showTableAssignCT = false;
				vm.showTableAcceptCT = false;
				grid = vm.recieveWoAllGrid;
			}
			if(vm.searchForm.status=="2" && vm.searchForm.typeBc=="1"){
				vm.showTableReceiveTH = false;
				vm.showTableAssignTH = true;
				vm.showTableAcceptTH = false;
				vm.showTableReceiveCT = false;
				vm.showTableAssignCT = false;
				vm.showTableAcceptCT = false;
				grid = vm.assignWoAllGrid;
			}
			if(vm.searchForm.status=="3" && vm.searchForm.typeBc=="1"){
				vm.showTableReceiveTH = false;
				vm.showTableAssignTH = false;
				vm.showTableAcceptTH = true;
				vm.showTableReceiveCT = false;
				vm.showTableAssignCT = false;
				vm.showTableAcceptCT = false;
				grid = vm.acceptWoAllGrid;
			}
			if(vm.searchForm.status=="1" && vm.searchForm.typeBc=="2"){
				vm.showTableReceiveTH = false;
				vm.showTableAssignTH = false;
				vm.showTableAcceptTH = false;
				vm.showTableReceiveCT = true;
				vm.showTableAssignCT = false;
				vm.showTableAcceptCT = false;
				grid = vm.recieveWoDetailGrid;
			}
			if(vm.searchForm.status=="2" && vm.searchForm.typeBc=="2"){
				vm.showTableReceiveTH = false;
				vm.showTableAssignTH = false;
				vm.showTableAcceptTH = false;
				vm.showTableReceiveCT = false;
				vm.showTableAssignCT = true;
				vm.showTableAcceptCT = false;
				grid = vm.assignWoDetailGrid;
			}
			if(vm.searchForm.status=="3" && vm.searchForm.typeBc=="2"){
				vm.showTableReceiveTH = false;
				vm.showTableAssignTH = false;
				vm.showTableAcceptTH = false;
				vm.showTableReceiveCT = false;
				vm.showTableAssignCT = false;
				vm.showTableAcceptCT = true;
				grid = vm.acceptWoDetailGrid;
			}
			grid.dataSource.query({
				page: 1,
				pageSize: 10
			});
		}

		var record = 0;

		// Tiếp nhận wo tổng hợp
		function fillDataTableReceiveAll(data) {
			kendo.ui.progress($("#recieveWoAllGrid"), true);
			vm.recieveWoAllGridOptions = kendoConfig
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
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									$("#appCountRecieveWoAll").text(
											"" + response.total);
									vm.recieveWoAll = response.total;
									return response.total;
								},
								data : function(response) {
									kendo.ui.progress($("#recieveWoAllGrid"),
											false);
									return response.data;
								}
							},
							transport : {
								read : {
									url : Constant.BASE_SERVICE_URL
											+ "rpOrderlyWoService/getDataReceiveWoSynthetic",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									var obj = angular.copy(vm.searchForm);
									obj.page = options.page;
									obj.pageSize = options.pageSize;
									obj.endDate = kendo.toString(kendo.date.addDays(kendo.parseDate(vm.searchForm.endDate, "dd/MM/yyyy"),1) , "dd/MM/yyyy");;
									return JSON.stringify(obj);
								}
							},
							pageSize : 10
						},
						toolbar : [ {
							name : "actions",
							template : '<div class=" pull-left ">'
									+ '</div>'
									+ '<div class="btn-group pull-right margin_top_button margin_right10">'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false">'
									+ '<span class="tooltipArrow"></span>'
									+ '<span class="tooltiptext">Cài đặt</span>'
									+ '<i class="fa fa-cog" aria-hidden="true"></i>'
									+ '</i>'
									+ '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'
									+ '<label ng-repeat="column in vm.recieveWoAllGrid.columns | filter: vm.gridColumnShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column, 1)"> {{column.title}}'
									+ '</label>' + '</div>' + '</div>'
						} ],
						columnMenu : false,
						noRecords : true,
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
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									template : function() {
										return ++record;
									},
									width : '50px'
								},
								{
									title : "Đơn vị",
									field : "cdLevel2Name",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:left;white-space:normal;"
									},
									width : '150px',
								},
								{
									title : "Tổng WO đã giao cho CNKT",
									field : "tong",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									width : '100px',
								},
								{
									title : "Hệ thống tự động nhận WO do quá hạn 24h mà đơn vị/FT chưa nhận",
									field : "quahan",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:right;white-space:normal;"
									},
									width : '100px'
								},
								{
									title : "Tỷ lệ",
									field : "tyle",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									width : '100px',
									template: '#=kendo.format("{0:p0}", tyle / 100)#'
								}, ]
					});
		}

		// Giao wo tổng hợp
		function fillDataTableAssignAll(data) {
			kendo.ui.progress($("#assignWoAllGrid"), true);
			vm.assignWoAllGridOptions = kendoConfig
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
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									$("#appCountAssignWoAll").text(
											"" + response.total);
									vm.assignWoAll = response.total;
									return response.total;
								},
								data : function(response) {
									kendo.ui.progress($("#assignWoAllGrid"),
											false);
									return response.data;
								}
							},
							transport : {
								read : {
									url : Constant.BASE_SERVICE_URL
											+ "rpOrderlyWoService/getDataReceiveWoSynthetic",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									var obj = angular.copy(vm.searchForm);
									obj.page = options.page;
									obj.pageSize = options.pageSize;
									obj.endDate = kendo.toString(kendo.date.addDays(kendo.parseDate(vm.searchForm.endDate, "dd/MM/yyyy"),1) , "dd/MM/yyyy");;
									return JSON.stringify(obj);
								}
							},
							pageSize : 10
						},
						toolbar : [ {
							name : "actions",
							template : '<div class=" pull-left ">'
									+ '</div>'
									+ '<div class="btn-group pull-right margin_top_button margin_right10">'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false">'
									+ '<span class="tooltipArrow"></span>'
									+ '<span class="tooltiptext">Cài đặt</span>'
									+ '<i class="fa fa-cog" aria-hidden="true"></i>'
									+ '</i>'
									+ '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'
									+ '<label ng-repeat="column in vm.assignWoAllGrid.columns | filter: vm.gridColumnShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column, 1)"> {{column.title}}'
									+ '</label>' + '</div>' + '</div>'
						} ],
						columnMenu : false,
						noRecords : true,
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
							title : "STT",
							field : "stt",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							template : function() {
								return ++record;
							},
							width : '50px'
						}, {
							title : "Đơn vị",
							field : "cdLevel2Name",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:left;white-space:normal;"
							},
							width : '150px',
						}, {
							title : "Tổng WO CNKT đã nhận",
							field : "tong",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px',
						}, {
							title : "CNKT chưa giao WO cho FT sau 48h nhận WO",
							field : "quahan",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:right;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Tỷ lệ",
							field : "tyle",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px',
							template: '#=kendo.format("{0:p}", tyle / 100)#'
						}, ]
					});
		}
		
		// Xác nhận wo tổng hợp
		function fillDataTableAcceptAll(data) {
			kendo.ui.progress($("#acceptWoAllGrid"), true);
			vm.acceptWoAllGridOptions = kendoConfig
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
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									$("#appCountAcceptWoAll").text(
											"" + response.total);
									vm.acceptWoAll = response.total;
									return response.total;
								},
								data : function(response) {
									kendo.ui.progress($("#acceptWoAllGrid"),
											false);
									return response.data;
								}
							},
							transport : {
								read : {
									url : Constant.BASE_SERVICE_URL
											+ "rpOrderlyWoService/getDataReceiveWoSynthetic",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									var obj = angular.copy(vm.searchForm);
									obj.page = options.page;
									obj.pageSize = options.pageSize;
									obj.endDate = kendo.toString(kendo.date.addDays(kendo.parseDate(vm.searchForm.endDate, "dd/MM/yyyy"),1) , "dd/MM/yyyy");;
									return JSON.stringify(obj);
								}
							},
							pageSize : 10
						},
						toolbar : [ {
							name : "actions",
							template : '<div class=" pull-left ">'
									+ '</div>'
									+ '<div class="btn-group pull-right margin_top_button margin_right10">'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false">'
									+ '<span class="tooltipArrow"></span>'
									+ '<span class="tooltiptext">Cài đặt</span>'
									+ '<i class="fa fa-cog" aria-hidden="true"></i>'
									+ '</i>'
									+ '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'
									+ '<label ng-repeat="column in vm.acceptWoAllGrid.columns | filter: vm.gridColumnShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column, 1)"> {{column.title}}'
									+ '</label>' + '</div>' + '</div>'
						} ],
						columnMenu : false,
						noRecords : true,
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
							title : "STT",
							field : "stt",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							template : function() {
								return ++record;
							},
							width : '50px'
						}, {
							title : "Đơn vị",
							field : "cdLevel2Name",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:left;white-space:normal;"
							},
							width : '150px',
						}, {
							title : "Tổng WO FT đã thực hiện",
							field : "tong",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px',
						}, {
							title : "Số WO CNKT chưa phê duyệt  sau 48h FT hoàn thành xong",
							field : "quahan",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:right;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Tỷ lệ",
							field : "tyle",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px',
							template: '#=kendo.format("{0:p}", tyle / 100)#'
						}, ]
					});
		}
		
		// Tiếp nhận wo chi tiết
		function fillDataTableReceiveDetail(data) {
			kendo.ui.progress($("#recieveWoDetailGrid"), true);
			vm.recieveWoDetailGridOptions = kendoConfig
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
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									$("#appCountRecieveWoDetail").text(
											"" + response.total);
									vm.recieveWoDetail = response.total;
									return response.total;
								},
								data : function(response) {
									kendo.ui.progress($("#recieveWoDetailGrid"),
											false);
									return response.data;
								}
							},
							transport : {
								read : {
									url : Constant.BASE_SERVICE_URL
											+ "rpOrderlyWoService/getDataReceiveWoDetail",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									var obj = angular.copy(vm.searchForm);
									obj.page = options.page;
									obj.pageSize = options.pageSize;
									obj.endDate = kendo.toString(kendo.date.addDays(kendo.parseDate(vm.searchForm.endDate, "dd/MM/yyyy"),1) , "dd/MM/yyyy");;
									return JSON.stringify(obj);
								}
							},
							pageSize : 10
						},
						toolbar : [ {
							name : "actions",
							template : '<div class=" pull-left ">'
									+ '</div>'
									+ '<div class="btn-group pull-right margin_top_button margin_right10">'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false">'
									+ '<span class="tooltipArrow"></span>'
									+ '<span class="tooltiptext">Cài đặt</span>'
									+ '<i class="fa fa-cog" aria-hidden="true"></i>'
									+ '</i>'
									+ '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'
									+ '<label ng-repeat="column in vm.recieveWoDetailGrid.columns | filter: vm.gridColumnShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column, 1)"> {{column.title}}'
									+ '</label>' + '</div>' + '</div>'
						} ],
						columnMenu : false,
						noRecords : true,
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
							title : "STT",
							field : "stt",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							template : function() {
								return ++record;
							},
							width : '50px'
						}, 
						{
							title : "Đơn vị",
							field : "cdLevel2Name",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						},
						{
							title : "Mã WO",
							field : "woCode",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:left;white-space:normal;"
							},
							width : '150px',
						}, {
							title : "Tên WO",
							field : "woName",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px',
						}, {
							title : "Công trình",
							field : "constructionCode",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:right;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Hợp đồng",
							field : "contractCode",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Ngày giao WO",
							field : "ngayGiaoWo",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Ngày nhận WO",
							field : "ngayNhanWo",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Ngày giao WO cho FT",
							field : "ngayGiaoFt",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Ngày FT nhận WO",
							field : "ngayFtTiepNhan",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Tên nhân viên FT",
							field : "fullName",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Email FT",
							field : "email",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Số điện thoại FT",
							field : "phoneNumber",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Giá tiền(triệu VNĐ)",
							field : "moneyValue",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px',
						}, {
							title : "Trạng thái WO",
							field : "state",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Tình trạng giao nhận WO",
							field : "trangThai",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, ]
					});
		}

		
		// Giao wo chi tiết
		function fillDataTableAssignDetail(data) {
			kendo.ui.progress($("#assignWoDetailGrid"), true);
			vm.assignWoDetailGridOptions = kendoConfig
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
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									$("#appCountAssingWoDetail").text(
											"" + response.total);
									vm.assignWoDetail = response.total;
									return response.total;
								},
								data : function(response) {
									kendo.ui.progress($("#assignWoDetailGrid"),
											false);
									return response.data;
								}
							},
							transport : {
								read : {
									url : Constant.BASE_SERVICE_URL
											+ "rpOrderlyWoService/getDataReceiveWoDetail",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									var obj = angular.copy(vm.searchForm);
									obj.page = options.page;
									obj.pageSize = options.pageSize;
									obj.endDate = kendo.toString(kendo.date.addDays(kendo.parseDate(vm.searchForm.endDate, "dd/MM/yyyy"),1) , "dd/MM/yyyy");;
									return JSON.stringify(obj);
								}
							},
							pageSize : 10
						},
						toolbar : [ {
							name : "actions",
							template : '<div class=" pull-left ">'
									+ '</div>'
									+ '<div class="btn-group pull-right margin_top_button margin_right10">'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false">'
									+ '<span class="tooltipArrow"></span>'
									+ '<span class="tooltiptext">Cài đặt</span>'
									+ '<i class="fa fa-cog" aria-hidden="true"></i>'
									+ '</i>'
									+ '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'
									+ '<label ng-repeat="column in vm.assignWoDetailGrid.columns | filter: vm.gridColumnShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column, 1)"> {{column.title}}'
									+ '</label>' + '</div>' + '</div>'
						} ],
						columnMenu : false,
						noRecords : true,
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
							title : "STT",
							field : "stt",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							template : function() {
								return ++record;
							},
							width : '50px'
						}, 
						{
							title : "Đơn vị",
							field : "cdLevel2Name",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						},
						{
							title : "Mã WO",
							field : "woCode",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:left;white-space:normal;"
							},
							width : '150px',
						}, {
							title : "Tên WO",
							field : "woName",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px',
						}, {
							title : "Công trình",
							field : "constructionCode",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:right;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Hợp đồng",
							field : "contractCode",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Ngày giao WO",
							field : "ngayGiaoWo",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Ngày nhận WO",
							field : "ngayNhanWo",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Ngày giao WO cho FT",
							field : "ngayGiaoFt",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Tên nhân viên FT",
							field : "fullName",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Email FT",
							field : "email",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Số điện thoại FT",
							field : "phoneNumber",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Giá tiền(triệu VNĐ)",
							field : "moneyValue",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px',
						}, {
							title : "Trạng thái WO",
							field : "state",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Tình trạng giao nhận WO",
							field : "trangThai",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, ]
					});
		}
		
		// Giao wo chi tiết
		function fillDataTableAcceptDetail(data) {
			kendo.ui.progress($("#acceptWoDetailGrid"), true);
			vm.acceptWoDetailGridOptions = kendoConfig
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
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									$("#appCountAcceptWoDetail").text(
											"" + response.total);
									vm.acceptWoDetail = response.total;
									return response.total;
								},
								data : function(response) {
									kendo.ui.progress($("#acceptWoDetailGrid"),
											false);
									return response.data;
								}
							},
							transport : {
								read : {
									url : Constant.BASE_SERVICE_URL
											+ "rpOrderlyWoService/getDataReceiveWoDetail",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									var obj = angular.copy(vm.searchForm);
									obj.page = options.page;
									obj.pageSize = options.pageSize;
									obj.endDate = kendo.toString(kendo.date.addDays(kendo.parseDate(vm.searchForm.endDate, "dd/MM/yyyy"),1) , "dd/MM/yyyy");;
									return JSON.stringify(obj);
								}
							},
							pageSize : 10
						},
						toolbar : [ {
							name : "actions",
							template : '<div class=" pull-left ">'
									+ '</div>'
									+ '<div class="btn-group pull-right margin_top_button margin_right10">'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false">'
									+ '<span class="tooltipArrow"></span>'
									+ '<span class="tooltiptext">Cài đặt</span>'
									+ '<i class="fa fa-cog" aria-hidden="true"></i>'
									+ '</i>'
									+ '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'
									+ '<label ng-repeat="column in vm.acceptWoDetailGrid.columns | filter: vm.gridColumnShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column, 1)"> {{column.title}}'
									+ '</label>' + '</div>' + '</div>'
						} ],
						columnMenu : false,
						noRecords : true,
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
							title : "STT",
							field : "stt",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							template : function() {
								return ++record;
							},
							width : '50px'
						}, 
						{
							title : "Đơn vị",
							field : "cdLevel2Name",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						},
						{
							title : "Mã WO",
							field : "woCode",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:left;white-space:normal;"
							},
							width : '150px',
						}, {
							title : "Tên WO",
							field : "woName",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px',
						}, {
							title : "Công trình",
							field : "constructionCode",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:right;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Hợp đồng",
							field : "contractCode",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Ngày giao WO",
							field : "ngayGiaoWo",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Ngày FT thực hiện xong",
							field : "ngayGiaoFt",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Ngày CD CNKT duyệt",
							field : "ngayGiaoFt",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Tên nhân viên FT",
							field : "fullName",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Email FT",
							field : "email",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Số điện thoại FT",
							field : "phoneNumber",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Giá tiền(triệu VNĐ)",
							field : "moneyValue",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px',
						}, {
							title : "Trạng thái WO",
							field : "state",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, {
							title : "Tình trạng giao nhận WO",
							field : "trangThai",
							headerAttributes : {
								style : "text-align:center;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							width : '100px'
						}, ]
					});
		}
		
		vm.selectedDept1 = false;
		vm.sysGroupACOptions = {
			dataTextField : "text",
			placeholder : "Nhập mã, tên của đơn vị hoặc cụm đội",
			select : function(e) {
				vm.selectedDept1 = true;
				var dataItem = this.dataItem(e.item.index());
				vm.searchForm.cdLevel1Name = dataItem.text;
				vm.searchForm.cdLevel1 = dataItem.departmentId;
			},
			pageSize : 10,
			open: function (e) {
		        vm.selectedDept1 = false;
		    },
			dataSource: {
		        serverFiltering: true,
		        transport: {
		            read: function (options) {
		                vm.selectedDept1 = false;
		                return Restangular.all("departmentRsService/department/" + 'getForAutoCompleteCnkt').post({
		                    name: vm.searchForm.cdLevel1Name,
		                    pageSize: vm.sysGroupACOptions.pageSize,
		                    page: 1
		                }).then(function (response) {
		                    options.success(response.data);
		                }).catch(function (err) {
		                    console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
		                });
		            }
		        }
		    },
			headerTemplate : '<div class="dropdown-header row text-center k-widget k-header">'
					+ '<p class="col-md-6 text-header-auto border-right-ccc">Mã đơn vị</p>'
					+ '<p class="col-md-6 text-header-auto">Tên đơn vị</p></div>',
			template : '<div class="row" >'
					+ '<div class="col-xs-5" style="float:left">#: data.code #</div>'
					+ '<div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
			change : function(e) {
				if (e.sender.value() === '') {
					vm.searchForm.cdLevel1Name = null;
					vm.searchForm.cdLevel1 = null;
				}
			},
			ignoreCase : false
		};

		vm.openPopupGroup = function() {
			var templateUrl = 'wms/popup/findDepartmentPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm đơn vị");
			CommonService.populatePopupCnkt(templateUrl, title, null, null, vm,
					null, 'string', false, '92%', '89%');
		};

		vm.onSave = function(data) {
			vm.searchForm.cdLevel1Name = data.text;
			vm.searchForm.cdLevel1 = data.departmentId;
		};

		vm.exportFile = function(){
			function displayLoading(target) {
                var element = $(target);
                kendo.ui.progress(element, true);
                setTimeout(function(){
                	vm.searchForm.page = null;
                	vm.searchForm.pageSize = null;
                	vm.searchForm.endDate = kendo.toString(kendo.date.addDays(kendo.parseDate(vm.searchForm.endDate, "dd/MM/yyyy"),1) , "dd/MM/yyyy");;
                    return Restangular.all("rpOrderlyWoService/exportFile").post(vm.searchForm).then(function (d) {
                        var data = d.plain();
                        window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                        kendo.ui.progress(element, false);
                    }).catch(function (e) {
                        kendo.ui.progress(element, false);
                        toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                        return;
                    });

                });
            }
            displayLoading(".tab-content");
		}
		// Controller end
	}
})();