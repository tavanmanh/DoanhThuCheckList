(function() {
	'use strict';
	var controllerId = 'progressTaskOsController';

	angular.module('MetronicApp').controller(controllerId,
			progressTaskOsController);

	function progressTaskOsController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, progressTaskOsService,
			CommonService, PopupConst, Restangular, RestEndpoint, Constant,
			htmlCommonService) {
		var vm = this;
		vm.showSearch = true;
		vm.isCreateNew = false;
		vm.searchForm = {};
		vm.showDetail = false;
		vm.String = "Quản lý công trình > Quản lý công trình > Tiến độ công việc ngoài OS";
		vm.addForm = {};
		vm.addFormCons = {};
		
		vm.sourceTaskList = [
			{
				code: 1,
				name: "Xây lắp"
			},
			{
				code: 2,
				name: "Chi phí"
			},
			{
				code: 3,
				name: "Ngoài Tập đoàn"
			},
			{
				code: 4,
				name: "Hạ tầng cho thuê xây dựng móng"
			},
			{
				code: 5,
				name: "Hạ tầng cho thuê hoàn thiện"
			},
			{
				code: 6,
				name: "Công trình XDDD"
			},
		];
		
		vm.constructionTypeList = [
			{
				code: 1,
				name: "Các Công trình BTS, Costie, SWAP và các công trình nguồn đầu tư mảng xây lắp"
			},
			{
				code: 2,
				name: "Các công trình nguồn chi phí"
			},
			{
				code: 3,
				name: "Các công trình Bảo dưỡng ĐH và MFĐ"
			},
			{
				code: 4,
				name: "Các công trình Gpon"
			},
			{
				code: 5,
				name: "Các công trình Hợp đồng 12 đầu việc"
			},
			{
				code: 6,
				name: "Các công trình Ngoài Tập đoàn"
			},
			{
				code: 7,
				name: "Hạ tầng cho thuê"
			},
			{
				code: 8,
				name: "Công trình XDDD"
			},
			{
				code: 9,
				name: "Các hợp đồng cơ điện nguồn đầu tư TCTY ký"
			},
		];
		
		init();
		function init() {
//			progressTaskOsService.doSearch({}).then(function(response){
//				var data = response.data;
//				for(var i=0; i<data.length; i++){
//					progressTaskOsService.updateProgressTaskOs(data[i]).then(function(result){
//						if(result.error){
//							toastr.error("Có lỗi xảy ra trong quá trình cập nhật tiến độ");
//							return;
//						}
////					}).catch(function(){
////						toastr.error("Có lỗi xảy ra trong quá trình cập nhật dữ liệu");
////						return;
//					});
//				}
//			}).catch(function(){
//				toastr.error("Có lỗi xảy ra trong quá trình lấy dữ liệu");
//				return;
//			});
			fillDataTable([]);
			fillDataPopup([]);
		}

		vm.doSearch = doSearch;
		function doSearch() {
			vm.showDetail = false;
			vm.searchForm.monthYear = $("#monthYearId").val();
			var grid = vm.progressTaskGrid;
			if (grid) {
				grid.dataSource.query({
					page : 1,
					pageSize : 10
				});
			}
		}

		vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.progressTaskGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.progressTaskGrid.showColumn(column);
			} else {
				vm.progressTaskGrid.hideColumn(column);
			}

		}

		vm.gridColumnShowHideFilter = function(item) {

			return item.type == null || item.type !== 1;
		};

		var record = 0;
		function fillDataTable(data) {
			kendo.ui.progress($("#progressTaskGrid"), true);
			vm.progressTaskGridOptions = kendoConfig
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
									+ '<button class="btn btn-qlk padding-search addQLK" '
									+ 'ng-click="vm.add()" uib-tooltip="Tạo mới" translate style="width: 100px;white-space:normal;">Tạo mới</button>'
									+ '</div>'
									+ '<div class="btn-group pull-right margin_top_button ">'
									+ '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: VNĐ</div>'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
									+ '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>'
									+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
									+ '<label ng-repeat="column in vm.workItemGrid.columns.slice(1,vm.workItemGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}'
									+ '</label>' + '</div></div>'
						} ],
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									$("#appCountProgress").text(
											"" + response.total);
									vm.countProgress = response.total;
									kendo.ui.progress($("#progressTaskGrid"), false);
									return response.total;
								},
								data : function(response) {
									kendo.ui.progress($("#progressTaskGrid"), false);
									return response.data;
								}
							},
							transport : {
								read : {
									// Thuc hien viec goi service
									url : Constant.BASE_SERVICE_URL
											+ "progressTaskOsRsService/doSearchMain",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									// vm.constructionSearch.employeeId =
									// Constant.user.srvUser.catEmployeeId;
									vm.searchForm.page = options.page
									vm.searchForm.pageSize = options.pageSize
									vm.searchForm.monthYear = $("#monthYearId").val();
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
//						edit : function(dataItem) {
//							// chinhpxn20180719_start
//							dataItem.model.oldQuantity = dataItem.model.quantity;
//							// chinhpxn20180719_end
//						},
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
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
								},
								{
									title : "TTKV",
									width : '100px',
									field : "ttkv",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
								},
								{
									title : "Tỉnh",
									width : '100px',
									field : "ttkt",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
								}, 
								{
									title : "Tháng năm",
									width : '100px',
									field : "monthYear",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
								},
								{
									title: "Thao tác",
									 headerAttributes: {style: "text-align:center;font-weight: bold;"},
							        template: dataItem =>
									'<div class="text-center">'
									 +'<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.edit(dataItem)" class=" icon_table "'+
									 '   uib-tooltip="Xem chi tiết" translate>'+
									 '<i class="fa fa-list-alt" style="color:#e0d014"   aria-hidden="true"></i>'+
									 '</button>'
//									 +'<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.updateProgress(dataItem)" class=" icon_table "'+
//									 '   uib-tooltip="Cập nhật tiến độ" translate>'+
//									 '<i class="fa fa-refresh" style="color:#17e014"   aria-hidden="true"></i>'+
//									 '</button>'
									 +'<button style=" border: none; background-color: white;" id="removeId"'+
									 'class="#=progressTaskOsId# icon_table" ng-click="vm.remove(dataItem)"  uib-tooltip="Xóa" translate'+
										'>'+
										'<i class="fa fa-trash" style="color: #337ab7;" aria-hidden="true"></i>'+
									'</button>'
									+'</div>',
							        width: '150px'
								}
								]
					});
		}

		function fillDataPopup(data) {
			var dataSource = new kendo.data.DataSource({
    			data: data,
    			autoSync: false,
    			group: { 
    				field: "constructionCode"
            	},
    			schema: {
    				model: {
    					id: "constrTaskGrid",
    					fields: {
    						stt: { editable: false },
    						cntContractCode: { editable: false },
    						constructionCode:{ editable: false },
    						description:{ editable: false },
    						sourceTask: { editable: false },
    						constructionType: { editable: false },
    						quantityValue: { editable: false },
    						hshcValue: { editable: false },
    						salaryValue: { editable: false },
    						billValue: { editable: false },
    						tdslAccomplishedDate: { editable: false },
    						tdslConstructing: { editable: true },
    						tdslExpectedCompleteDate: { editable: true },
    						tdhsTctNotApproval: { editable: true },
    						tdhsSigningGdcn: { editable: true },
    						tdhsControl4a: { editable: true },
    						tdhsPhtApprovaling: { editable: true },
    						tdhsPhtAcceptancing: { editable: true },
    						tdhsTtktProfile: { editable: true },
    						tdhsExpectedCompleteDate: { editable: true },
    						tdttCollectMoneyDate: { editable: true },
    						tdttProfilePht: { editable: true },
    						tdttProfilePtc: { editable: true },
    						tdttExpectedCompleteDate: { editable: true }
    					}
    				}
    			},
    			pageSize : 10,
    		});
			vm.constrTaskGridOptions = kendoConfig
					.getGridOptions({
						autoBind : true,
						scrollable : true,
						resizable : true,
						editable : false,
						dataSource:dataSource,
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
									+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
									+ '<label ng-repeat="column in vm.workItemGrid.columns.slice(1,vm.workItemGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}'
									+ '</label>' + '</div></div>'
						} ],
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
		                          title: CommonService.translate("constructionCode"),
		                          field: "constructionCode",
		                          hidden: true,
		                          groupHeaderTemplate: "Mã công trình "+":#:value#",
		                          width: 120
								},
								{
									title : "TT",
									field : "stt",
									template : function() {
										return ++record;
									},
									width : '50px',
									columnMenu : false,
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
								},
								{
									title: "Thao tác",
									 headerAttributes: {style: "text-align:center;font-weight: bold;"},
							        template: dataItem =>
									'<div class="text-center">'
									 +'<button style=" border: none; background-color: white;" id="updateId"  ng-click="caller.editCons(dataItem)" class=" icon_table "'+
									 '   uib-tooltip="Xem chi tiết" translate>'+
									 '<i class="fa fa-list-alt" style="color:#e0d014"   aria-hidden="true"></i>'+
									 '</button>'
									+'</div>',
							        width: '150px'
								},
								{
									title : "Số hợp đồng",
									width : '200px',
									field : "cntContractCode",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;white-space:normal;"
									},
									type : 'text',
								},
								{
									title : "Mã trạm/tuyến/công trình",
									width : '200px',
									field : "constructionCode",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;white-space:normal;"
									},
									type : 'text',
								},
								{
									title : "Nội dung",
									width : '100px',
									field : "workItemName",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;white-space:normal;"
									},
									type : 'text',
								},
								{
									title : "Nguồn việc",
									width : '100px',
									field : "sourceTask",
									headerAttributes : {
										style : "text-align:center;font-weight: boldwhite-space:normal;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;white-space:normal;"
									},
									type : 'text',
									template: function(dataItem){
										if(dataItem.sourceTask==1){
											return "Xây lắp";
										} else if(dataItem.sourceTask==2){
											return "Chi phí";
										} else if(dataItem.sourceTask==3){
											return "Ngoài Tập đoàn";
										} else if(dataItem.sourceTask==4){
											return "Hạ tầng cho thuê xây dựng móng";
										} else if(dataItem.sourceTask==5){
											return "Hạ tầng cho thuê hoàn thiện";
										} else if(dataItem.sourceTask==6){
											return "Công trình XDDD";
										} else {
											return "";
										}
									}
								},
								{
									title : "Loại công trình",
									width : '100px',
									field : "constructionType",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;white-space:normal;"
									},
									type : 'text',
									template: function(dataItem){
										if(dataItem.constructionType==1){
											return "Các Công trình BTS, Costie, SWAP và các công trình nguồn đầu tư mảng xây lắp";
										} else if(dataItem.constructionType==2){
											return "Các công trình nguồn chi phí";
										} else if(dataItem.constructionType==3){
											return "Các công trình Bảo dưỡng ĐH và MFĐ";
										} else if(dataItem.constructionType==4){
											return "Các công trình Gpon";
										} else if(dataItem.constructionType==5){
											return "Các công trình Hợp đồng 12 đầu việc";
										} else if(dataItem.constructionType==6){
											return "Các công trình Ngoài Tập đoàn";
										} else if(dataItem.constructionType==7){
											return "Hạ tầng cho thuê";
										} else if(dataItem.constructionType==8){
											return "Công trình XDDD";
										} else if(dataItem.constructionType==9){
											return "Các hợp đồng cơ điện nguồn đầu tư TCTY ký";
										} else {
											return "";
										}
									}
								},
								{
									title : "Giá trị Sản lượng (VNĐ)",
									field : 'quantityValue',
									width : '250px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;white-space:normal;"
									},
									attributes : {
										style : "text-align:right;white-space:normal;white-space:normal;"
									},
									type : 'text',
									format: '{0:n0}',
								},
								{
									title : "Giá trị Quỹ lương (VNĐ)",
									width : '100px',
									field : "salaryValue",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;white-space:normal;"
									},
									attributes : {
										style : "text-align:right;white-space:normal;white-space:normal;"
									},
									type : 'text',
									format: '{0:n0}',
								},
								{
									title : "Giá trị HSHC (VNĐ)",
									width : '100px',
									field : "hshcValue",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;white-space:normal;"
									},
									attributes : {
										style : "text-align:right;white-space:normal;white-space:normal;"
									},
									type : 'text',
									format: '{0:n0}',
								},
								{
									title : "Giá trị Hoá đơn",
									width : '100px',
									field : "billValue",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;white-space:normal;"
									},
									attributes : {
										style : "text-align:right;white-space:normal;white-space:normal;"
									},
									type : 'text',
									format: '{0:n0}',
								},
								{
									title : "Tiến độ Sản lượng",
									width : '300px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;white-space:normal;"
									},
									attributes : {
										style : "text-align:left;white-space:normal;white-space:normal;"
									},
									columns : [
											{
												title : "Đã hoàn thành",
												field : 'tdslAccomplishedDate',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;white-space:normal;white-space:normal;"
												},
												attributes : {
													style : "text-align:center;white-space:normal;white-space:normal;"
												},
												type : 'text',
											},
											{
												title : "Đang thi công",
												field : 'tdslConstructing',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;white-space:normal;white-space:normal;"
												},
												attributes : {
													style : "text-align:center;white-space:normal;white-space:normal;"
												},
												type : 'text',
											},
											{
												title : "Dự kiến hoàn thành",
												field : 'tdslExpectedCompleteDate',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;white-space:normal;white-space:normal;"
												},
												attributes : {
													style : "text-align:center;white-space:normal;white-space:normal;"
												},
												type : 'text',
											}, ],
								},
								{
									title : "Tiến độ Hồ sơ",
									width : '300px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:left;white-space:normal;"
									},
									columns : [
											{
												title : "HSHC đang trên đường + TCTy chưa thẩm duyệt",
												field : 'tdhsTctNotApproval',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;white-space:normal;"
												},
												attributes : {
													style : "text-align:center;white-space:normal;"
												},
												type : 'text',
											},
											{
												title : "Đang trình GĐ CN ký",
												field : 'tdhsSigningGdcn',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;white-space:normal;"
												},
												attributes : {
													style : "text-align:center;white-space:normal;"
												},
												type : 'text',
											},
											{
												title : "Đối soát 4A",
												field : 'tdhsControl4a',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;white-space:normal;"
												},
												attributes : {
													style : "text-align:center;white-space:normal;"
												},
												type : 'text',
											},
											{
												title : "PHT chi nhánh đang thẩm duyệt",
												field : 'tdhsPhtApprovaling',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;white-space:normal;"
												},
												attributes : {
													style : "text-align:center;white-space:normal;"
												},
												type : 'text',
											},
											{
												title : "PHT chi nhánh đang nghiệm thu",
												field : 'tdhsPhtAcceptancing',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;white-space:normal;"
												},
												attributes : {
													style : "text-align:center;white-space:normal;"
												},
												type : 'text',
											},
											{
												title : "TTKT đang làm hồ sơ",
												field : 'tdhsTtktProfile',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;white-space:normal;"
												},
												attributes : {
													style : "text-align:center;white-space:normal;"
												},
												type : 'text',
											},
											{
												title : "Dự kiến hoàn thành",
												field : 'tdhsExpectedCompleteDate',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;white-space:normal;"
												},
												attributes : {
													style : "text-align:center;white-space:normal;"
												},
												type : 'text',
											}, ],
								},
								{
									title : "Tiến độ Thu tiền",
									width : '300px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:left;white-space:normal;"
									},
									columns : [
											{
												title : "Đã hoàn thành thu tiền",
												field : 'tdttCollectMoneyDate',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;white-space:normal;"
												},
												attributes : {
													style : "text-align:center;white-space:normal;"
												},
												type : 'text',
											},
											{
												title : "Hồ sơ đang ở P.HT",
												field : 'tdttProfilePht',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;white-space:normal;"
												},
												attributes : {
													style : "text-align:center;white-space:normal;"
												},
												type : 'text',
											},
											{
												title : "HS đang ở P.TC Viettel tỉnh/TP",
												field : 'tdttProfilePtc',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;white-space:normal;"
												},
												attributes : {
													style : "text-align:center;white-space:normal;"
												},
												type : 'text',
											},
											{
												title : "Dự kiến hoàn thành",
												field : 'tdttExpectedCompleteDate',
												width : '100px',
												headerAttributes : {
													style : "text-align:center;font-weight: bold;white-space:normal;"
												},
												attributes : {
													style : "text-align:center;white-space:normal;"
												},
												type : 'text',
											}, ],
								}, ]
					});
		}

		vm.provinceOptions = {
		        dataTextField: "name",
		        dataValueField: "id",
				placeholder:"Nhập mã hoặc tên tỉnh",
		        select: function (e) {
		        		vm.isSelect = true;
			            var dataItem = this.dataItem(e.item.index());
			            vm.addForm.ttkt = dataItem.code;
						vm.addForm.provinceName = dataItem.name;
						vm.addForm.ttkv = dataItem.areaCode;
						vm.addForm.ttktId = dataItem.catProvinceId;
						vm.addForm.monthYear = $("#dateOptionsId").val();
						kendo.ui.progress($("#constrTaskGrid"), true);
						progressTaskOsService.getDataTaskByProvince(vm.addForm).then(function(data){
							kendo.ui.progress($("#constrTaskGrid"), false);
							fillDataPopup(data.data);
							$("#constrTaskGrid").data("kendoGrid").dataSource.data(data.data);
							$("#constrTaskGrid").data("kendoGrid").refresh();
						}).catch(function(e){
							kendo.ui.progress($("#constrTaskGrid"), false);
						});
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
		                        name: vm.addForm.provinceName,
		                        pageSize: vm.provinceOptions.pageSize,
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
		        		vm.addForm.ttkt = null;
						vm.addForm.provinceName = null;
						vm.addForm.ttkv = null;
						vm.addForm.ttktId = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.addForm.ttkt = null;
						vm.addForm.provinceName = null;
						vm.addForm.ttkv = null;
						vm.addForm.ttktId = null;
		            }
		        }
		    }
		
		vm.provinceSearchOptions = {
		        dataTextField: "name",
		        dataValueField: "id",
				placeholder:"Nhập mã hoặc tên tỉnh",
		        select: function (e) {
		        		vm.isSelect = true;
			            var dataItem = this.dataItem(e.item.index());
			            vm.searchForm.ttkt = dataItem.code;
						vm.searchForm.catProvinceName = dataItem.name;
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
		                        name: vm.searchForm.catProvinceName,
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
		        		vm.searchForm.ttkt = null;
						vm.searchForm.catProvinceName = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.searchForm.ttkt = null;
						vm.searchForm.catProvinceName = null;
		            }
		        }
		    }
		
		vm.changeDate = changeDate;
		function changeDate(){
//			kendo.ui.progress($("#constrTaskGrid"), true);
//			progressTaskOsService.getDataTaskByProvince(vm.addForm).then(function(data){
//				kendo.ui.progress($("#constrTaskGrid"), false);
//				fillDataPopup(data.data);
//				$("#constrTaskGrid").data("kendoGrid").dataSource.data(data.data);
//				$("#constrTaskGrid").data("kendoGrid").refresh();
//			}).catch(function(e){
//				kendo.ui.progress($("#constrTaskGrid"), false);
//			});
			vm.addForm.ttkt = null;
			vm.addForm.provinceName = null;
			vm.addForm.ttkv = null;
			vm.addForm.ttktId = null;
		}
		
		vm.openCatProvincePopup = openCatProvincePopup;
		function openCatProvincePopup(param){
			if($("#dateOptionsId").val()==""){
        		toastr.warning("Chọn tháng năm trước khi chọn tỉnh !");
        		return;
        	}
			var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm tỉnh");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, param, 'ggfd', false, '75%','75%','catProvinceSearchController');
	    }
		
		vm.onSaveCatProvince = onSaveCatProvince;
		function onSaveCatProvince(dataItem){
			vm.addForm.ttkt = dataItem.code;
			vm.addForm.provinceName = dataItem.name;
			vm.addForm.ttkv = dataItem.areaCode;
			vm.addForm.ttktId = dataItem.catProvinceId;
	        htmlCommonService.dismissPopup();
	    };
		
	    vm.openCatProvinceSearch = openCatProvinceSearch;
		function openCatProvinceSearch(param){
			var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm tỉnh");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, param, 'ggfd', false, '75%','75%','catProvinceSearchController');
	    }
	    
		vm.onSaveCatProvinceSearch = onSaveCatProvinceSearch;
		function onSaveCatProvinceSearch(dataItem){
			vm.searchForm.ttkt = dataItem.code;
			vm.searchForm.catProvinceName = dataItem.name;
//			vm.searchForm.ttktId = dataItem.catProvinceId;
	        htmlCommonService.dismissPopup();
	    };
		
	    vm.dataCons = {};
	    
	    //Tạo mới màn chính
		vm.add = function() {
			vm.isCreate = true;
			vm.addForm = {};
			vm.dataCons = {};
			var teamplateUrl = "coms/progressTaskOs/popupChangeTask.html";
			var title = "Khai báo công trình hợp đồng";
			var windowId = "ADD_CONS_CONTRACT";
			CommonService.populatePopupCreate(teamplateUrl, title, null, vm,
					windowId, true, '85%', '85%', "deptAdd");
			fillDataPopup([]);
		}
		
		//Cập nhật màn chính
		vm.edit = function(dataItem) {
			vm.isCreate = false;
			vm.addForm = angular.copy(dataItem);
			var teamplateUrl = "coms/progressTaskOs/popupChangeTask.html";
			var title = "Cập nhật công trình hợp đồng";
			var windowId = "EDIT_CONS_CONTRACT";
			
			progressTaskOsService.getById(vm.addForm).then(function(data){
				vm.dataCons = data.data;
				CommonService.populatePopupCreate(teamplateUrl, title, null, vm,
						windowId, true, '85%', '85%', "deptAdd");
				fillDataPopup(data.data);
				setTimeout(function(){
					$("#constrTaskGrid").data("kendoGrid").dataSource.data(data.data);
					$("#constrTaskGrid").data("kendoGrid").refresh();
				},100);
			});
		}
		
		//Cập nhật tiến độ 
		vm.updateProgress = function(dataItem){
			kendo.ui.progress($("#progressTaskGrid"), true);
			progressTaskOsService.updateProgressTaskOs(dataItem).then(function(result){
				kendo.ui.progress($("#progressTaskGrid"), false);
				if(result.error){
					toastr.error("Có lỗi xảy ra trong quá trình cập nhật tiến độ");
					return;
				}
				toastr.success("Cập nhật tiến độ thành công !");
				doSearch();
			}).catch(function(){
				kendo.ui.progress($("#progressTaskGrid"), false);
				toastr.error("Có lỗi xảy ra trong quá trình lấy dữ liệu");
				return;
			});
		}
		
		//Cập nhật popup
		var modalInstance1 = null;
		vm.editCons = function(dataItem){
			modalInstance1 = null;
			vm.addFormCons = angular.copy(dataItem);
			var grid = $("#constrTaskGrid").data("kendoGrid").dataSource.data();
			var teamplateUrl = "coms/progressTaskOs/popupAddNew.html";
			var title = "Chỉnh sửa bản ghi";
			var windowId = "ADD_CONS_CONTRACT_POP";
			CommonService.populatePopupCreate(teamplateUrl, title, null, vm,
					windowId, true, '80%', '80%', "deptAdd");
			setTimeout(function(){
				modalInstance1 = CommonService.getModalInstance1();
			},100);
			$("#constrTaskGrid").data("kendoGrid").dataSource.data(grid);
			$("#constrTaskGrid").data("kendoGrid").refresh();
		}
		
		vm.cancelEditCons = function() {
			modalInstance1.dismiss();
		}
		
		vm.saveEditCons = function(){
			var grid = $("#constrTaskGrid").data("kendoGrid").dataSource.data();
			for(var i=0;i<grid.length;i++){
				if(grid[i].uid==vm.addFormCons.uid){
					grid[i] = vm.addFormCons;
				}
			}
			$("#constrTaskGrid").data("kendoGrid").dataSource.data(grid);
			modalInstance1.dismiss();
			$("#constrTaskGrid").data("kendoGrid").refresh();
		}
		
		vm.cancel = function() {
			$("div.k-window-actions > a.k-window-action > span.k-i-close")
					.click();
		}

		vm.saveAdd = function() {
			if($("#dateOptionsId").val()==""){
				toastr.warning("Tháng năm không được để trống !");
				$("#dateOptionsId").focus();
				return;
			}
			if($("#provinceName").val()==""){
				toastr.warning("Tỉnh không được để trống !");
				$("#provinceName").focus();
				return;
			}
			vm.addForm.listProgressTask = $("#constrTaskGrid").data("kendoGrid").dataSource.data();
			if(vm.isCreate){
				progressTaskOsService.saveAdd(vm.addForm).then(function(result) {
					if (result.error) {
						toastr.error(result.error);
						return;
					}

					toastr.success("Thêm mới thành công !");
					CommonService.dismissPopup1();
					$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
					doSearch();
				});
			} else {
				progressTaskOsService.saveUpdate(vm.addForm).then(function(result) {
					if (result.error) {
						toastr.error(result.error);
						return;
					}

					toastr.success("Cập nhật thành công !");
					CommonService.dismissPopup1();
					$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
					doSearch();
				});
			}
		}
		
		vm.remove = function(dataItem){
			confirm("Xác nhận xoá bản ghi", function(){
				progressTaskOsService.deleteRecord(dataItem).then(function(result){
					if(result.error){
						toastr.error(result.error);
						return;
					}
					toastr.success("Xoá bản ghi thành công !");
					doSearch();
				});
			});
		}

		vm.exportFile = function(){
			kendo.ui.progress($("#progressTaskGrid"), true);
			return Restangular.all("progressTaskOsRsService/exportCompleteProgress").post(vm.searchForm).then(function (d) {
        	    var data = d.plain();
        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	    kendo.ui.progress($("#progressTaskGrid"), false);
        	    vm.disableExportExcel = false;
        	}).catch(function (e) {
        		kendo.ui.progress($("#progressTaskGrid"), false);
        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        	    vm.disableExportExcel = false;
        	    return;
        	});
		}
		// end controller
	}
})();