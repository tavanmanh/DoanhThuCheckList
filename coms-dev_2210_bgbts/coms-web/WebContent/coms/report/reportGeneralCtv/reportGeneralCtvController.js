(function() {
	'use strict';
	var controllerId = 'reportGeneralCtvController';

	angular.module('MetronicApp').controller(controllerId,
			reportGeneralCtvController);

	function reportGeneralCtvController($scope, $templateCache, $rootScope,
			$timeout, gettextCatalog, $filter, kendoConfig, $kWindow,
			reportGeneralCtvService, htmlCommonService, CommonService,
			PopupConst, Restangular, RestEndpoint, Constant, $http) {

		var vm = this;
		vm.String = "Quản lý công trình > Tiếp xúc khách hàng > Báo cáo tổng hợp Cộng tác viên";
		vm.searchForm = {};

		init();
		function init() {
			fillDataTable([]);
		}

		vm.doSearch = doSearch;
		function doSearch() {
			if(vm.searchForm.sysGroupName==null){
				toastr.warning("Chưa chọn đơn vị ");
				$("#sysGroup").focus();
				return;
			}
			if(vm.searchForm.dateFrom==null){
				toastr.warning("Chưa chọn kỳ từ ngày ");
				$("#aio_cm_dateFrom").focus();
				return;
			}
			if(vm.searchForm.dateTo==null){
				toastr.warning("Chưa chọn kỳ đến ngày ");
				$("#aio_cm_dateTo").focus();
				return;
			}
			vm.showDetail = false;
			var grid = vm.reportGeneralCtvGrid;
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
				vm.reportGeneralCtvGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.reportGeneralCtvGrid.showColumn(column);
			} else {
				vm.reportGeneralCtvGrid.hideColumn(column);
			}
		}

		vm.gridColumnShowHideFilter = function(item) {
			return item.type == null || item.type !== 1;
		};

		var record = 0;
		function fillDataTable(data) {
			vm.reportGeneralCtvGridOptions = kendoConfig
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
									+ '<label ng-repeat="column in vm.reportGeneralCtvGrid.columns.slice(1,vm.reportGeneralCtvGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
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
									$("#appCountReportGeneralCtv").text(
											"" + response.total);
									vm.reportGeneralCtvCount = response.total;
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
											+ "/doSearchGeneralCtv",
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
									field : 'sysGroupName',
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
									title : "Số lượng CTV",
									width : '300px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									columns : [
											{
												title : "Còn hoạt động đầu kỳ",
												field : 'numberCtvActiveFirstStage',
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
												title : "Tuyển mới trong kỳ",
												field : 'numberCtvNewInsideStage',
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
												title : "Chấm dứt hoạt động trong kỳ",
												field : 'numberCtvEndInsideStage',
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
												title : "Còn hoạt động cuối kỳ",
												field : 'numberCtvActiveFinalStage',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;"
												},
												attributes : {
													style : "text-align:left;"
												},
												type : 'text',
												template: function(dataItem){
													return dataItem.numberCtvActiveFirstStage + dataItem.numberCtvNewInsideStage - dataItem.numberCtvEndInsideStage;
												}
											}, ]
								}, ]
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
				$("#sysGroup").focus();
				return;
			}
			if(vm.searchForm.dateFrom==null){
				toastr.warning("Chưa chọn kỳ từ ngày ");
				$("#aio_cm_dateFrom").focus();
				return;
			}
			if(vm.searchForm.dateTo==null){
				toastr.warning("Chưa chọn kỳ đến ngày ");
				$("#aio_cm_dateTo").focus();
				return;
			}
			kendo.ui.progress($("#reportGeneralCtvGrid"), true);
        	vm.searchForm.page = null;
        	vm.searchForm.pageSize = null;
        	vm.searchForm.status = "Tổng hợp";
            return Restangular.all(RestEndpoint.REPORT_SERVICE + "/exportFileCtv").post(vm.searchForm).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                kendo.ui.progress($("#reportGeneralCtvGrid"), false);
            }).catch(function (e) {
            	kendo.ui.progress($("#reportGeneralCtvGrid"), false);
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                return;
            });
		}
		
		// Controller end
	}
})();