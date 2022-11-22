(function() {
	'use strict';
	var controllerId = 'reportRevenueCtvController';

	angular.module('MetronicApp').controller(controllerId,
			reportRevenueCtvController);

	function reportRevenueCtvController($scope, $templateCache, $rootScope,
			$timeout, gettextCatalog, $filter, kendoConfig, $kWindow,
			reportRevenueCtvService, htmlCommonService, CommonService,
			PopupConst, Restangular, RestEndpoint, Constant, $http) {

		var vm = this;
		vm.String = "Quản lý công trình > Tiếp xúc khách hàng > Báo cáo doanh thu Cộng tác viên";
		vm.searchForm = {
			typeBc: "1"
		};

		init();
		function init() {
			fillDataTable([]);
			fillDataTableXa([]);
		}

		vm.doSearch = doSearch;
		function doSearch() {
			if(vm.searchForm.typeBc == "1" && vm.searchForm.sysGroupName==null) {
				toastr.warning("Chưa chọn đơn vị !");
				$("#aio_rpgt_sysGroup").focus();
				return;
			}
			if(vm.searchForm.typeBc == "2" && vm.searchForm.sysGroupName==null) {
				toastr.warning("Chưa chọn đơn vị !");
				$("#aio_rpgt_province").focus();
				return;
			}
			vm.showDetail = false;
			var grid = vm.reportRevenueCtvGrid;
			if(vm.searchForm.typeBc == "2"){
				grid = vm.reportRevenueCtvXaGrid;
			}
			if (grid) {
				grid.dataSource.query({
					page : 1,
					pageSize : 10
				});
			}
		}

		vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.reportRevenueCtvGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.reportRevenueCtvGrid.showColumn(column);
			} else {
				vm.reportRevenueCtvGrid.hideColumn(column);
			}
		}

		vm.gridColumnShowHideFilter = function(item) {
			return item.type == null || item.type !== 1;
		};
		
		vm.showHideWorkItemXaColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.reportRevenueCtvXaGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.reportRevenueCtvXaGrid.showColumn(column);
			} else {
				vm.reportRevenueCtvXaGrid.hideColumn(column);
			}
		}

		vm.changeTypeBc = function(){
			vm.searchForm.sysGroupName = null;
			vm.searchForm.sysGroupId = null;
		}
		
		// Grid theo huyện
		var record = 0;
		function fillDataTable(data) {
			vm.reportRevenueCtvGridOptions = kendoConfig
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
									+ '<label ng-repeat="column in vm.reportRevenueCtvGrid.columns.slice(1,vm.reportRevenueCtvGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
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
									$("#appCountReportRevenueCtv").text(
											"" + response.total);
									vm.reportRevenueCtvCount = response.total;
									return response.total;
								},
								data : function(response) {
									return response.data;
								}
							},
							transport : {
								read : {
									url : Constant.BASE_SERVICE_URL
											+ RestEndpoint.REPORT_SERVICE
											+ "/doSearchRevenueCtv",
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
									title : "Đơn vị",
									field : 'districtName',
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
									title : "Số CTV hiện có",
									field : 'numberCtvCurrent',
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
									title : "Số CTV có phát sinh doanh thu",
									field : 'numberCtvRevenueIncurred',
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
									title : "Doanh thu kênh XHH",
									width : '200px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									columns : [
											{
												title : "Tổng",
												field : 'sumRevenueXhh',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;"
												},
												attributes : {
													style : "text-align:left;"
												},
												type : 'text'
											},
											{
												title : "Trung bình/CTV",
												field : 'mediumRevenueXhh',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;"
												},
												attributes : {
													style : "text-align:left;"
												},
												type : 'text'
											}, ]
								},
								{
									title : "Hoa hồng kênh XHH",
									width : '200px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									columns : [
											{
												title : "Tổng",
												field : 'sumDiscountXhh',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;"
												},
												attributes : {
													style : "text-align:left;"
												},
												type : 'text'
											},
											{
												title : "Trung bình/CTV",
												field : 'mediumDiscountXhh',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;"
												},
												attributes : {
													style : "text-align:left;"
												},
												type : 'text'
											} ]
								} ]
					});
		}
		
		// Grid theo xã
		var recordXa = 0;
		function fillDataTableXa(data) {
			vm.reportRevenueCtvXaGridOptions = kendoConfig
					.getGridOptions({
						autoBind : true,
						scrollable : true,
						resizable : true,
						editable : false,
						sortable: true,
						dataBinding : function() {
							recordXa = (this.dataSource.page() - 1)
									* this.dataSource.pageSize();
						},
						reorderable : true,
						toolbar : [ {
							name : "actions",
							template : '<div class="btn-group pull-right margin_top_button ">'
									+ '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: VNĐ</div>'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
									+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'
									+ '<label ng-repeat="column in vm.reportRevenueCtvXaGrid.columns.slice(1,vm.reportRevenueCtvXaGrid.columns.length)| filter: vm.gridColumnXaShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}'
									+ '</label>' + '</div>' + '</div>'
						} ],
						dataSource : {
							serverPaging : true,
							serverSorting: true,
	                        serverFiltering: true,
							schema : {
								total : function(response) {
									if(response.total==null){
										response.total = 0;
									}
									$("#appCountReportRevenueCtvXa").text(
											"" + response.total);
									vm.reportRevenueCtvXaCount = response.total;
									return response.total;
								},
								data : function(response) {
									return response.data;
								},
								model: {
	                                fields: {
	                                	districtName: { editable: false, nullable: true },
	                                	communeName: { editable: false, nullable: true },
	                                	numberCtvCurrent: { editable: false, nullable: true },
	                                	numberCtvRevenueIncurred: { editable: false, nullable: true },
	                                	sumRevenueXhh: { editable: false, nullable: true },
	                                	mediumRevenueXhh: { editable: false, nullable: true },
	                                	sumDiscountXhh: { editable: false, nullable: true },
	                                	mediumDiscountXhh: { editable: false, nullable: true },
	                                }
	                            }
							},
							transport : {
								read : {
									url : Constant.BASE_SERVICE_URL
											+ RestEndpoint.REPORT_SERVICE
											+ "/doSearchRevenueCtv",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									vm.searchForm.page = options.page
									vm.searchForm.pageSize = options.pageSize
									return JSON.stringify(vm.searchForm)

								}
							},
							group: { 
			    				field: "districtName",
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
						groupable: true,
						columns : [
//								{ 
//									field: "districtName",
//			                        hidden:true,
//			                        groupHeaderTemplate: "Quận huyện: #= value#",
//			                    },
								{
									title : "STT",
									field : "stt",
									template : function() {
										return ++recordXa;
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
									title : "Quận/huyện",
									field : 'districtName',
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
									title : "Phường/xã",
									field : 'communeName',
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
									title : "Số CTV hiện có",
									field : 'numberCtvCurrent',
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
									title : "Số CTV có phát sinh doanh thu",
									field : 'numberCtvRevenueIncurred',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text'
								},
								{
									title : "Doanh thu kênh XHH",
									width : '200px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									columns : [
											{
												title : "Tổng",
												field : 'sumRevenueXhh',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;"
												},
												attributes : {
													style : "text-align:left;"
												},
												type : 'text'
											},
											{
												title : "Trung bình/CTV",
												field : 'mediumRevenueXhh',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;"
												},
												attributes : {
													style : "text-align:left;"
												},
												type : 'text'
											}, ]
								},
								{
									title : "Hoa hồng kênh XHH",
									width : '200px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									columns : [
											{
												title : "Tổng",
												field : 'sumDiscountXhh',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;"
												},
												attributes : {
													style : "text-align:left;"
												},
												type : 'text'
											},
											{
												title : "Trung bình/CTV",
												field : 'mediumDiscountXhh',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;"
												},
												attributes : {
													style : "text-align:left;"
												},
												type : 'text'
											} ]
								} ]
					});
		}

		vm.selectedDept1 = false;
		vm.sysGroupACOptions = {
			dataTextField : "name",
			placeholder : "Nhập mã, tên của đơn vị",
			select : function(e) {
				vm.selectedDept1 = true;
				var dataItem = this.dataItem(e.item.index());
				vm.searchForm.sysGroupName = dataItem.name;
				vm.searchForm.sysGroupId = dataItem.sysGroupId;
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
		                return Restangular.all(RestEndpoint.REPORT_SERVICE + '/getForAutoCompleteByGroupLv2').post({
		                    keySearch: vm.searchForm.sysGroupName,
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
					+ '<div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
			change : function(e) {
				if (e.sender.value() === '') {
					vm.searchForm.sysGroupName = null;
					vm.searchForm.sysGroupId = null;
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
			vm.searchForm.sysGroupName = dataItem.name;
			vm.searchForm.sysGroupId = dataItem.sysGroupId;
		};
		
		vm.exportFile = function(){
			if(vm.searchForm.sysGroupName==null){
				toastr.warning("Chưa chọn đơn vị ");
				$("#aio_rpgt_sysGroup").focus();
				return;
			}
			kendo.ui.progress($("#reportRevenueCtvGrid"), true);
        	vm.searchForm.page = null;
        	vm.searchForm.pageSize = null;
        	vm.searchForm.status = "Doanh thu";
            return Restangular.all(RestEndpoint.REPORT_SERVICE + "/exportFileCtv").post(vm.searchForm).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                kendo.ui.progress($("#reportRevenueCtvGrid"), false);
            }).catch(function (e) {
            	kendo.ui.progress($("#reportRevenueCtvGrid"), false);
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                return;
            });
		}
		
		vm.selectedDept = false;
		vm.proviceACOptions = {
			dataTextField : "name",
			placeholder : "Nhập mã, tên của tỉnh",
			select : function(e) {
				vm.selectedDept = true;
				var dataItem = this.dataItem(e.item.index());
				vm.searchForm.sysGroupName = dataItem.name;
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
		                return Restangular.all(RestEndpoint.REPORT_SERVICE + '/catProvince/doSearchProvinceInPopup').post({
		                    name: vm.searchForm.sysGroupName,
		                    pageSize: vm.proviceACOptions.pageSize,
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
					+ '<p class="col-md-12 text-header-auto">Tên đơn vị</p></div>',
			template : '<div class="row" >'
					+ '<div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
			change : function(e) {
				if (e.sender.value() === '') {
					vm.searchForm.sysGroupName = null;
				}
			},
			ignoreCase : false
		};
		
		// Controller end
	}
})();