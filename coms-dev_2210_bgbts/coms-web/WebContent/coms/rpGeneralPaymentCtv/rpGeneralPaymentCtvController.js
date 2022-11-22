(function() {
	'use strict';
	var controllerId = 'rpGeneralPaymentCtvController';

	angular.module('MetronicApp').controller(controllerId,
			rpGeneralPaymentCtvController);

	function rpGeneralPaymentCtvController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, rpGeneralPaymentCtvService,
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
			fillDataTable([]);
			vm.searchForm.thang = kendo.toString(new Date(), "MM/yyyy");
		}

		vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.rpGeneralPaymentCtvGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.rpGeneralPaymentCtvGrid.showColumn(column);
			} else {
				vm.rpGeneralPaymentCtvGrid.hideColumn(column);
			}

		}

		vm.gridColumnShowHideFilter = function(item) {
			return item.type == null || item.type !== 1;
		};
		
		vm.monthSelectorOptions = {
		        start: "year",
		        depth: "year"
		    };

		
		vm.doSearch = doSearch;
		function doSearch(){
			if(vm.searchForm.typeTime==null || vm.searchForm.typeTime==""){
				toastr.warning("Chưa chọn Loại thời gian !");
				return;
			} else {
				if(vm.searchForm.typeTime==1){
					if($("#dateGeneral").val()==""){
						toastr.warning("Chưa chọn Tháng/năm !");
						$("#dateGeneral").focus();
						vm.searchForm.dateFrom=null;
						vm.searchForm.dateTo=null;
						return;
					}
				} else {
					if($("#dateFromGeneral").val()==""){
						toastr.warning("Chưa chọn Ngày !");
						$("#dateFromGeneral").focus();
						vm.searchForm.thang=null;
						return;
					}
				}
			}
			
//			fillDataTable([]);
			var grid = vm.rpGeneralPaymentCtvGrid;
			if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 10
				});
			}
		}
		
		var record = 0;
		function fillDataTable(data) {
			vm.rpGeneralPaymentCtvGridOptions = kendoConfig
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
//							+ '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-disabled="vm.disableExportExcel" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>'
							+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
							+ '<label ng-repeat="column in vm.rpGeneralPaymentCtvGrid.columns.slice(1,vm.rpGeneralPaymentCtvGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
							+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}'
							+ '</label>' + '</div></div>'
				} ],
				dataSource : {
					serverPaging : true,
					schema : {
						total : function(response) {
							$("#appCountRpGeneralPaymentCtv").text(
									"" + response.total);
							vm.countRpGeneralPaymentCtv = response.total;
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
									+ "rpBTSService/doSearchRpGeneralPaymentCtv",
							contentType : "application/json; charset=utf-8",
							type : "POST"
						},
						parameterMap : function(options, type) {
							vm.searchForm.page = options.page;
							vm.searchForm.pageSize = options.pageSize;
							vm.searchForm.thang = $("#dateGeneral").val();
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
							width : '150px',
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
							title : "Thông tin quản lý kênh",
							width : '300px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							columns : [
								{
									title : "Mã nhân viên",
									width : '100px',
									field : "employeeCode",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',
									editable : false,
								},
								{
									title : "Họ và tên nhân viên",
									width : '100px',
									field : "fullName",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',
									editable : false,
								},
								{
									title : "Điện thoại",
									width : '100px',
									field : "phoneNumber",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type: 'text',
									editable : false,
								},
							]
						},
						{
							title : "Thông tin cộng tác viên",
							width : '300px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							columns : [
								{
									title : "Mã CTV",
									width : '100px',
									field : "employeeCodeCTV",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',
									editable : false,
								},
								{
									title : "Họ tên",
									width : '100px',
									field : "fullNameCTV",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',
									editable : false,
								},
								{
									title : "Số CMND/CCCD",
									width : '100px',
									field : "cmtnd",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',
									editable : false,
								},
//								{
//									title : "Ngày cấp",
//									width : '100px',
//									field : "",
//									headerAttributes : {
//										style : "text-align:center;font-weight: bold;"
//									},
//									attributes : {
//										style : "text-align:center;"
//									},
//									type : 'text',
//									editable : false,
//								},
//								{
//									title : "Nơi cấp",
//									width : '100px',
//									field : "",
//									headerAttributes : {
//										style : "text-align:center;font-weight: bold;"
//									},
//									attributes : {
//										style : "text-align:center;"
//									},
//									type : 'text',
//									editable : false,
//								},
								{
									title : "Số điện thoại/TK Vtpay",
									width : '100px',
									field : "vtpay",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',
									editable : false,
								},
							]
						},
						{
							title : "Thông tin thu nhập",
							width : '500px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							columns : [
								{
									title : "Giá trị trước thuế của hợp đồng thi công",
									width : '100px',
									field : "giaTruocThue",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',
									editable : false,
									format: '{0:n3}',
									footerTemplate: function(item) {
				                        var data = vm.rpGeneralPaymentCtvGrid.dataSource.data();
				                        if(data.length>0){
				                        	return kendo.toString(data[0].sumGiaTruocThue, "n3");
				                        } else {
				                        	return 0;
				                        }
									},
								},
								{
									title : "Tổng phí hoa hồng môi giới",
									width : '100px',
									field : "hoaHong",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',
									editable : false,
									format: '{0:n3}',
									footerTemplate: function(item) {
				                        var data = vm.rpGeneralPaymentCtvGrid.dataSource.data();
				                        if(data.length>0){
				                        	return kendo.toString(data[0].sumHoaHong, "n3");
				                        } else {
				                        	return 0;
				                        }
									},
								},
								{
									title : "Thuế TNCN/GTGT",
									width : '100px',
									field : "thueTNCN",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',
									editable : false,
									format: '{0:n3}',
									footerTemplate: function(item) {
				                        var data = vm.rpGeneralPaymentCtvGrid.dataSource.data();
				                        if(data.length>0){
				                        	return kendo.toString(data[0].sumThueTNCN, "n3");
				                        } else {
				                        	return 0;
				                        }
									},
								},
								{
									title : "Thực nhận",
									width : '100px',
									field : "thucNhan",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'number',
									editable : false,
									format: '{0:n3}',
									footerTemplate: function(item) {
				                        var data = vm.rpGeneralPaymentCtvGrid.dataSource.data();
				                        if(data.length>0){
				                        	return kendo.toString(data[0].sumThucNhan, "n3");
				                        } else {
				                        	return 0;
				                        }
									},
								},
								{
									title : "Trạng thái",
									width : '100px',
									field : "status",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',
									editable : false,
									template: function(dataItem) {
										if(dataItem.status=="0") {
											return "Đã hủy";
										} else if(dataItem.status=="1") {
											return "Chờ tiếp nhận";
										} else if(dataItem.status=="2") {
											return "Chờ tiếp xúc";
										} else if(dataItem.status=="3") {
											return "Chờ trình bày giải pháp";
										} else if(dataItem.status=="4") {
											return "Từ chối tiếp xúc & chờ phê duyệt";
										} else if(dataItem.status=="5") {
											return "Chờ ký hợp đồng";
										} else if(dataItem.status=="6") {
											return "Bổ sung / chỉnh sửa giải pháp";
										} else if(dataItem.status=="7") {
											return "Từ chối giải pháp & chờ phê duyệt";
										} else if(dataItem.status=="8") {
											return "Hoàn thành tiếp xúc";
										} else if(dataItem.status=="9") {
											return "Đóng yêu cầu tiếp xúc";
										} else if(dataItem.status=="10") {
											return "Hoàn thành thanh toán hoa hồng";
										}
									}
								},
								
							]
						},
						]
			});
		}
		
		vm.exportFile = function(){
			if(vm.searchForm.typeTime==null || vm.searchForm.typeTime==""){
				toastr.warning("Chưa chọn Loại thời gian !");
				return;
			} else {
				if(vm.searchForm.typeTime==1){
					if($("#dateGeneral").val()==""){
						toastr.warning("Chưa chọn Tháng/năm !");
						$("#dateGeneral").focus();
						vm.searchForm.dateFrom=null;
						vm.searchForm.dateTo=null;
						return;
					}
				} else {
					if($("#dateFromGeneral").val()==""){
						toastr.warning("Chưa chọn Từ ngày !");
						$("#dateFromGeneral").focus();
						vm.searchForm.thang=null;
						return;
					}
					
					if($("#dateToGeneral").val()==""){
						toastr.warning("Chưa chọn đến ngày !");
						$("#dateToGeneral").focus();
						vm.searchForm.thang=null;
						return;
					}
				}
			}
			kendo.ui.progress($("#rpGeneralPaymentCtvGrid"), true);
			vm.searchForm.thang = $("#dateGeneral").val();
			rpGeneralPaymentCtvService.exportRpGeneralPaymentCtv(vm.searchForm).then(function(d){
				var data = d.plain();
        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	    kendo.ui.progress($("#rpGeneralPaymentCtvGrid"), false);
			}).catch(function (e) {
				kendo.ui.progress($("#rpGeneralPaymentCtvGrid"), false);
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
// end controller
	}
})();