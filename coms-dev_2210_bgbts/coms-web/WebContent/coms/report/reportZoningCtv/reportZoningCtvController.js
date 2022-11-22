(function() {
	'use strict';
	var controllerId = 'reportZoningCtvController';

	angular.module('MetronicApp').controller(controllerId,
			reportZoningCtvController);

	function reportZoningCtvController($scope, $templateCache, $rootScope,
			$timeout, gettextCatalog, $filter, kendoConfig, $kWindow,
			reportZoningCtvService, htmlCommonService, CommonService,
			PopupConst, Restangular, RestEndpoint, Constant, $http) {

		var vm = this;
		vm.String = "Quản lý công trình > Tiếp xúc khách hàng > Báo cáo quy hoạch Cộng tác viên";
		vm.searchForm = {
			typeBc: "2"
		};

		init();
		function init() {
			fillDataTable([]);
		}
		
		vm.doSearch = doSearch;
		function doSearch() {
			if(vm.searchForm.sysGroupName==null) {
				toastr.warning("Chưa chọn đơn vị !");
				$("#aio_rpgt_sysGroup").focus();
				return;
			}
			vm.showDetail = false;
			var grid = vm.reportZoningCtvGrid;
			if (grid) {
				grid.dataSource.query({
					page : 1,
					pageSize : 10
				});
			}
		}

		vm.resetFormField = function(param) {
			if(param=='group'){
				vm.searchForm.sysGroupName = null;
				vm.searchForm.sysGroupId = null;
			}
		}
		
		vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.reportZoningCtvGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.reportZoningCtvGrid.showColumn(column);
			} else {
				vm.reportZoningCtvGrid.hideColumn(column);
			}
		}

		vm.gridColumnShowHideFilter = function(item) {
			return item.type == null || item.type !== 1;
		};

		var record = 0;
		function fillDataTable(data) {
			vm.reportZoningCtvGridOptions = kendoConfig
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
									// + '<div class="btn-group
									// margin_top_button unit_text"> Đơn vị
									// tính: triệu VNĐ</div>'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
									+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'
									+ '<label ng-repeat="column in vm.reportZoningCtvGrid.columns.slice(1,vm.reportZoningCtvGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
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
									$("#appCountReportZoningCtv").text(
											"" + response.total);
									vm.reportZoningCtvCount = response.total;
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
											+ "/doSearchZoningCtv",
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
									title : "Số lượng xã/phường",
									field : 'numberCommune',
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
									title : "Số lượng CTV",
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
									title : "% độ phủ quy hoạch",
									field : 'percentZoningCoverage',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',
									template: function(dataItem) {
										if(dataItem.numberCommune!=0){
											return kendo.toString((dataItem.numberCtvCurrent / dataItem.numberCommune) * 100, "n3");
										} else {
											return kendo.toString(0 , "n0");
										}
									}
								},
								{
									title : "Ghi chú",
									field : 'description',
									width : '150px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text'
								},
								]
					});
		}
		
		vm.exportFile = function(){
			if(vm.searchForm.sysGroupName==null) {
				toastr.warning("Chưa chọn đơn vị !");
				$("#aio_rpgt_sysGroup").focus();
				return;
			}
			kendo.ui.progress($("#reportZoningCtvGrid"),true);
        	vm.searchForm.page = null;
        	vm.searchForm.pageSize = null;
        	vm.listRemove = [];
        	vm.listConvert = [];
        	reportZoningCtvService.doSearch(vm.searchForm).then(function(d){
        		for(var i=0;i<d.data.length;i++){
        			d.data[i].percentZoningCoverage = kendo.toString((d.data[i].numberCtvCurrent / d.data[i].numberCommune) * 100, "n3");
        		}
				CommonService.exportFile(vm.reportZoningCtvGrid,d.data,vm.listRemove,vm.listConvert,"Báo cáo quy hoạch CTV");
				kendo.ui.progress($("#reportZoningCtvGrid"),false);
			});
		}
		
		vm.selectedDept1 = false;
		vm.sysGroupACOptions = {
			dataTextField : "name",
			placeholder : "Nhập mã, tên của tỉnh",
			select : function(e) {
				vm.selectedDept1 = true;
				var dataItem = this.dataItem(e.item.index());
				vm.searchForm.sysGroupName = dataItem.name;
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
		                return Restangular.all(RestEndpoint.REPORT_SERVICE + '/catProvince/doSearchProvinceInPopup').post({
		                    name: vm.searchForm.sysGroupName,
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