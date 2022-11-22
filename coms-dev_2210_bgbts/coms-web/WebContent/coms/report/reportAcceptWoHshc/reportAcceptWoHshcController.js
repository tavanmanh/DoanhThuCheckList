(function() {
	'use strict';
	var controllerId = 'reportAcceptWoHshcController';

	angular.module('MetronicApp').controller(controllerId,
			reportAcceptWoHshcController);

	function reportAcceptWoHshcController($scope, $templateCache, $rootScope,
			$timeout, gettextCatalog, $filter, kendoConfig, $kWindow,
			reportAcceptWoHshcService, htmlCommonService, CommonService,
			PopupConst, Restangular, RestEndpoint, Constant, $http) {

		var vm = this;
		vm.String = "Quản lý công trình > Báo cáo > Báo cáo ghi nhận WO HSHC";
		vm.searchForm = {
			dateType: 5
		};
		
		init();
		function init() {
			var date = new Date();
        	var start = new Date(date.getFullYear(), date.getMonth(), 1);
//        	var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
        	vm.searchForm.dateTo = htmlCommonService.formatDate(date);
        	vm.searchForm.dateFrom = htmlCommonService.formatDate(start);
			fillDataTable([]);
		}

		vm.doSearch = doSearch;
		function doSearch() {
			if(vm.searchForm.dateFrom==null || vm.searchForm.dateFrom==""){
				toastr.warning("Từ ngày không được để trống");
				return;
			}
			if(vm.searchForm.dateTo==null || vm.searchForm.dateTo==""){
				toastr.warning("Đến ngày không được để trống");
				return;
			}
			vm.showDetail = false;
			var grid = vm.reportAcceptWoHshcGrid;
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
				vm.reportAcceptWoHshcGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.reportAcceptWoHshcGrid.showColumn(column);
			} else {
				vm.reportAcceptWoHshcGrid.hideColumn(column);
			}
		}

		vm.gridColumnShowHideFilter = function(item) {
			return item.type == null || item.type !== 1;
		};

		vm.woStateLst = [
			{
				code: "CD_OK",
				name: "Điều phối duyệt OK"
			},
			{
				code: "OK",
				name: "Hoàn thành"
			},
			{
				code: "WAIT_TC_BRANCH",
				name: "Chờ TC trụ duyệt"
			},
			{
				code: "WAIT_TC_TCT",
				name: "Chờ TC TCT duyệt"
			}
		];
		
		vm.dateTypeLst = [
			{
				code: "5",
				name: "Thời gian ghi nhận hoàn thành HSHC"
			},
			{
				code: "1",
				name: "Thời gian CD duyệt"
			},
			{
				code: "2",
				name: "Thời gian TTHT duyệt"
			},
			{
				code: "3",
				name: "Thời gian TC trụ duyệt"
			},
			{
				code: "4",
				name: "Thời gian TC TCT duyệt"
			}
			
		];
		
		vm.woStateLstOptions={
			dataSource: vm.woStateLst,
	        dataTextField: "name",
	        dataValueField: "code",
	        valuePrimitive: true
	    }
		
		vm.dateTypeLstOptions={
			dataSource: vm.dateTypeLst,
		    dataTextField: "name",
		    dataValueField: "code",
		    valuePrimitive: true
		}
		
		var record = 0;
		function fillDataTable(data) {
			kendo.ui.progress($("#reportAcceptWoHshcGridId"),true);
			vm.reportAcceptWoHshcGridOptions = kendoConfig
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
									+ '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: triệu VNĐ</div>'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
									+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'
									+ '<label ng-repeat="column in vm.reportDetailCtvGrid.columns.slice(1,vm.reportDetailCtvGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}'
									+ '</label>' + '</div>' + '</div>'
						} ],
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									$("#appCountReportAcceptWoHshc").text(
											"" + response.total);
									vm.reportAcceptWoHshcCount = response.total;
									return response.total;
								},
								data : function(response) {
									kendo.ui.progress($("#reportAcceptWoHshcGridId"),false);
									return response.data;
								}
							},
							transport : {
								read : {
									url : Constant.BASE_SERVICE_URL
											+ RestEndpoint.API_REPORT_SERVICE
											+ "/doSearchReportAcceptHSHC",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									vm.searchForm.page = options.page;
									vm.searchForm.pageSize = options.pageSize;
									return JSON.stringify(vm.searchForm);
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

						}, {
							title : "Đơn vị",
							field : 'sysGroupName',
							width : '250px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text'
						}, {
							title : "Mã WO",
							field : 'woCode',
							width : '180px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text'
						}, {
							title : "Tên WO",
							field : 'woName',
							width : '150px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text'
						}, {
							title : "Thời gian CD duyệt",
							field : 'updateCdApproveWo',
							width : '150px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text'
						}, {
							title : "Thời gian TTHT duyệt",
							field : 'updateTthtApproveWo',
							width : '150px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text'
						}, {
							title : "Thời gian TC trụ duyệt",
							field : 'updateTcBranchApproveWo',
							width : '150px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text'
						}, {
							title : "Thời gian TC TCT duyệt",
							field : 'updateTcTctApproveWo',
							width : '160px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text'
						}, {
							title : "Thời gian ghi nhận hoàn thành HSHC",
							field : 'approveDateReportWo',
							width : '150px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold; white-space: normal"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text'
						}, {
							title : "Hợp đồng",
							field : 'contractCode',
							width : '180px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text'
						}, {
							title : "Công trình",
							field : 'constructionCode',
							width : '180px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text'
						}, {
							title : "Mã trạm",
							field : 'stationCode',
							width : '150px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text'
						}, {
							title : "Nguồn việc",
							field : 'apWorkSrc',
							width : '110px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text'
						}, {
							title : "Loại công trình",
							field : 'apConstructionType',
							width : '120px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text'
						}, {
							title : "Trạng thái",
							field : 'woState',
							width : '150px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text'
						}, {
							title : "Giá trị",
							field : 'moneyValue',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
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
			if(vm.searchForm.dateFrom==null || vm.searchForm.dateFrom==""){
				toastr.warning("Từ ngày không được để trống");
				return;
			}
			if(vm.searchForm.dateTo==null || vm.searchForm.dateTo==""){
				toastr.warning("Đến ngày không được để trống");
				return;
			}
			kendo.ui.progress($("#reportAcceptWoHshcGridId"),true);
        	vm.searchForm.page = null;
        	vm.searchForm.pageSize = null;
        	vm.listRemove = [];
        	vm.listConvert = [];
        	reportAcceptWoHshcService.doSearch(vm.searchForm).then(function(d){
				CommonService.exportFile(vm.reportAcceptWoHshcGrid,d.data,vm.listRemove,vm.listConvert,"Báo cáo ghi nhận WO HSHC");
				kendo.ui.progress($("#reportAcceptWoHshcGridId"),false);
			});
		}
		
		// Controller end
	}
})();