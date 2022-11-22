(function() {
	'use strict';
	var controllerId = 'rpTranfersCtvController';

	angular.module('MetronicApp').controller(controllerId,
			rpTranfersCtvController);

	function rpTranfersCtvController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, rpTranfersCtvService,
			CommonService, PopupConst, Restangular, RestEndpoint, Constant,
			htmlCommonService) {
		var vm = this;
		vm.showSearch = true;
		vm.showDetail = false;
		vm.searchForm = {
			typeTime: 1
		};
		vm.String = $rootScope.stringtile;

		init();
		function init() {
			vm.searchForm.thang = kendo.toString(new Date(), "MM/yyyy");
			fillDataTable([]);
		}

		vm.timeTypeList = [
	    	{
	    		"code": 1,
	    		"name": "Tháng"
	    	},
	    	{
	    		"code": 2,
	    		"name": "Ngày"
	    	}
	    ];
		
		vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.rpTranfersCtvGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.rpTranfersCtvGrid.showColumn(column);
			} else {
				vm.rpTranfersCtvGrid.hideColumn(column);
			}

		}

		vm.gridColumnShowHideFilter = function(item) {
			return item.type == null || item.type !== 1;
		};
		
		vm.doSearch = doSearch;
		function doSearch(){
			if(vm.searchForm.typeTime==null || vm.searchForm.typeTime==""){
				toastr.warning("Chưa chọn Loại thời gian !");
				return;
			} else {
				if(vm.searchForm.typeTime==1){
					if($("#dateTranfer").val()==""){
						toastr.warning("Chưa chọn Tháng/năm !");
						$("#dateTranfer").focus();
						vm.searchForm.dateFrom=null;
						vm.searchForm.dateTo=null;
						return;
					}
				} else {
					if($("#dateFromTranfer").val()==""){
						toastr.warning("Chưa chọn Từ ngày !");
						$("#dateFromTranfer").focus();
						vm.searchForm.thang=null;
						return;
					}
					
					if($("#dateToTranfer").val()==""){
						toastr.warning("Chưa chọn đến ngày !");
						$("#dateToTranfer").focus();
						vm.searchForm.thang=null;
						return;
					}
				}
			}
			var grid = vm.rpTranfersCtvGrid;
			if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 10
				});
			}
		}
		
		var record = 0;
		function fillDataTable(data) {
			vm.rpTranfersCtvGridOptions = kendoConfig
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
							+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
							+ '<label ng-repeat="column in vm.rpTranfersCtvGrid.columns.slice(1,vm.rpTranfersCtvGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
							+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}'
							+ '</label>' + '</div></div>'
				} ],
				dataSource : {
					serverPaging : true,
					schema : {
						total : function(response) {
							$("#appCountRpTranfersCtv").text(
									"" + response.total);
							vm.countRpTranfersCtv = response.total;
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
									+ "rpBTSService/doSearchRpTranfersCtv",
							contentType : "application/json; charset=utf-8",
							type : "POST"
						},
						parameterMap : function(options, type) {
							vm.searchForm.page = options.page;
							vm.searchForm.pageSize = options.pageSize;
							if(vm.searchForm.typeTime==1){
								vm.searchForm.dateFrom = null;
								vm.searchForm.dateTo = null;
							} else {
								vm.searchForm.thang=null;
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
							title : "Mã tỉnh",
							field : 'provinceCode',
							width : '200px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false,
							footerTemplate: function(item) {
		                        return kendo.toString("Tổng");
							},
						},
						{
							title : "Mã CTV",
							field : 'employeeCodeCTV',
							width : '200px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Họ và tên CTV",
							field : 'fullNameCTV',
							width : '200px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Số điện thoại/TK Vtpay",
							field : 'vtpay',
							width : '200px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Số tiền (VNĐ)",
							field : 'soTien',
							width : '200px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'number',
							editable : false,
//							format: '{0:n3}',
							footerTemplate: function(item) {
		                        var data = vm.rpTranfersCtvGrid.dataSource.data();
		                        if(data.length>0){
		                        	return kendo.toString(data[0].sumGiaTruocThue, "n3");
		                        } else {
		                        	return 0;
		                        }
							},
						},
						{
							title : "Nội dung gửi SMS",
							field : 'tinNhan',
							width : '200px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
//						{
//							title : "Ghi chú",
//							field : '',
//							width : '200px',
//							headerAttributes : {
//								style : "text-align:center;font-weight: bold;"
//							},
//							attributes : {
//								style : "text-align:left;"
//							},
//							type : 'text',
//							editable : false
//						},
						]
			});
		}
		
		vm.exportFile = function(){
			if(vm.searchForm.typeTime==null || vm.searchForm.typeTime==""){
				toastr.warning("Chưa chọn Loại thời gian !");
				return;
			} else {
				if(vm.searchForm.typeTime==1){
					if($("#dateTranfer").val()==""){
						toastr.warning("Chưa chọn Tháng/năm !");
						$("#dateTranfer").focus();
						vm.searchForm.dateFrom=null;
						vm.searchForm.dateTo=null;
						return;
					}
				} else {
					if($("#dateFromTranfer").val()==""){
						toastr.warning("Chưa chọn Từ ngày !");
						$("#dateFromTranfer").focus();
						vm.searchForm.thang=null;
						return;
					}
					
					if($("#dateToTranfer").val()==""){
						toastr.warning("Chưa chọn đến ngày !");
						$("#dateToTranfer").focus();
						vm.searchForm.thang=null;
						return;
					}
				}
			}
			kendo.ui.progress($("#rpTranfersCtvTable"), true);
			vm.searchForm.thang = $("#dateTranfer").val();
			rpTranfersCtvService.exportRpTranfersCtv(vm.searchForm).then(function(d){
				var data = d.plain();
        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	    kendo.ui.progress($("#rpTranfersCtvTable"), false);
			}).catch(function (e) {
				kendo.ui.progress($("#rpTranfersCtvTable"), false);
        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        	    vm.disableExportExcel = false;
        	    return;
        	});
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
// end controller
	}
})();