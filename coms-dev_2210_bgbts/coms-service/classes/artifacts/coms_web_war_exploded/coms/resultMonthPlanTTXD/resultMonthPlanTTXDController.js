(function () {
    'use strict';
    var controllerId = 'resultMonthPlanTTXDController';

    angular.module('MetronicApp').controller(controllerId, resultMonthPlanTTXDController);

    function resultMonthPlanTTXDController($scope, $rootScope, $timeout, gettextCatalog,
                                kendoConfig, $kWindow, resultMonthPlanTTXDService,
                                CommonService, PopupConst, Restangular, RestEndpoint, Constant, htmlCommonService) {
        var vm = this;
        vm.showSearch = true;
        vm.isCreateNew = false;
        vm.showDetail = false;
        vm.searchForm = {};
        vm.viewForm = {};
        vm.String = "Quản lý công trình > Quản lý kế hoạch > Kết quả thực hiện kế hoạch tháng TTXD";
        
        init();
        function init(){
        	fillDataTable([]);
        }
        
        vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.resultQuantityGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.resultQuantityGrid.showColumn(column);
			} else {
				vm.resultQuantityGrid.hideColumn(column);
			}

		}

		vm.gridColumnShowHideFilter = function(item) {
			return item.type == null || item.type !== 1;
		};
        
		vm.doSearch = doSearch;
		function doSearch(){
			var grid = vm.resultQuantityGrid;
			if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 10
				});
			}
		}
		
		vm.changeImage = changeImage;
		function changeImage(image) {
	    	vm.imageSelected = image;
		}
		
		var record = 0;
        function fillDataTable(data){
        	vm.resultQuantityGridOptions = kendoConfig
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
//							+ '<button class="btn btn-qlk padding-search addQLK" '
//							+ 'ng-click="vm.add()" uib-tooltip="Tạo mới" translate style="width: 100px;white-space:normal;">Tạo mới</button>'
							+ '</div>'
							+ '<div class="btn-group pull-right margin_top_button ">'
							+ '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: triệu VNĐ</div>'
							+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
							+ '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>'
							+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
							+ '<label ng-repeat="column in vm.resultQuantityGrid.columns.slice(1,vm.resultQuantityGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
							+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}'
							+ '</label>' + '</div></div>'
				} ],
				dataSource : {
					serverPaging : true,
					schema : {
						total : function(response) {
							vm.count = response.total;
							return response.total;
						},
						data : function(response) {
							return response.data;
						}
					},
					transport : {
						read : {
							url : Constant.BASE_SERVICE_URL
									+ "DetailMonthPlanOSRsService/doSearchResultMonthQuantityTTXD",
							contentType : "application/json; charset=utf-8",
							type : "POST"
						},
						parameterMap : function(options, type) {
							vm.searchForm.page = options.page;
	                        vm.searchForm.pageSize = options.pageSize;
							var obj = {};
							obj = angular.copy(vm.searchForm);
							obj.monthYear = kendo.parseDate($("#monthYearId").val(),"MM/yyyy");
							return JSON.stringify(obj);

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
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							type : 'text',
						},
						{
							title : "Hợp đồng",
							width : '100px',
							field : "cntContractCode",
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							type : 'text',
						},
						{
							title : "Giao chỉ tiêu",
							width : '300px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							type : 'text',
							columns : [
								{
									title : "Sản lượng",
									width : '100px',
									field : "quantityTarget",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
									format: "{0:n3}",
								},
								{
									title : "Doanh thu",
									width : '100px',
									field : "revenueTarget",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
									format: "{0:n3}",
								}, 
								{
									title : "Yêu cầu khác",
									width : '100px',
									field : "otherTarget",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
								}
							]
						},
						{
							title : "Người thực hiện",
							width : '100px',
							field : "performerName",
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							type : 'text',
						},
						{
							title : "Người điều hành",
							width : '100px',
							field : "supervisorName",
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;white-space:normal;"
							},
							type : 'text',
						},
						{
		                    title: "Ngày hoàn thành",
		                    field: 'completeDate',
		                    width: '15%',
		                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
		                    attributes: {
		                        style: "text-align:right;"
		                    },
		                    editable: false,
		                },
		                {
		                    title: "Kết quả thực hiện",
		                    width: '300',
		                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
		                    attributes: {
		                        style: "text-align:right;"
		                    },
		                    editable: false,
		                    columns : [
		                    	{
									title : "Sản lượng",
									width : '100px',
									field : "ttkv",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
									format: "{0:n3}",
									template: function(dataItem){
										if(dataItem.quantityXNXD!=null && dataItem.detailMonthQuantityType=="1"){
											return dataItem.quantityXNXD;
										} else {
											return "";
										}
									}
								},
								{
									title : "Doanh thu",
									width : '100px',
									field : "ttkt",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
									format: "{0:n3}",
									template: function(dataItem){
										if(dataItem.quantityXNXD!=null && dataItem.detailMonthQuantityType=="2"){
											return dataItem.quantityXNXD;
										} else {
											return "";
										}
									}
								},
								{
									title : "Nội dung kết quả thực hiện",
									width : '100px',
									field : "description",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
								}, 
		                    ]
		                },
						{
							title: "Thao tác",
							 headerAttributes: {style: "text-align:center;font-weight: bold;"},
					        template: dataItem =>
							'<div class="text-center">'
							 +'<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.view(dataItem)" class=" icon_table "'+
							 '   uib-tooltip="Xem chi tiết" translate>'+
							 '<i class="fa fa-list-alt" style="color:#e0d014"   aria-hidden="true"></i>'+
							 '</button>'
							+'</div>',
					        width: '150px'
						}
						]
			});
        }
        
        vm.view = function(dataItem){
        	vm.viewForm = angular.copy(dataItem);
        	var templateUrl="coms/resultMonthPlanTTXD/popup-detail-cancel-confirm-os.html";
            var title="Thông tin chi tiết kết quả ";
            var windowId="DETAIL_CANCEL_CONFIRM";
            vm.popUpOpen = 'DETAIL_CANCEL_CONFIRM';
            resultMonthPlanTTXDService.getListAttachmentByIdAndType(vm.viewForm.constructionTaskId).then(function(data){
            	vm.viewForm.listImage = data.plain();
            	vm.changeImage(vm.viewForm.listImage[0]);
            	CommonService.populatePopupCreate(templateUrl,title,vm.viewForm,vm,windowId,false,'1000','auto',"null");
            });
        }
        
        vm.exportFile = function(){
        	vm.searchForm.page = null;
        	vm.searchForm.pageSize = null;
        	kendo.ui.progress($("#resultQuantityGrid"), true);
        	return Restangular.all("DetailMonthPlanOSRsService/exportResultQuantityTTXD").post(vm.searchForm).then(function (d) {
        	    var data = d.plain();
        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	    kendo.ui.progress($("#resultQuantityGrid"), false);
        	}).catch(function (e) {
        		kendo.ui.progress($("#resultQuantityGrid"), false);
        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        	    return;
        	});
        }
        
      //Auto success mã nhân viên
		vm.performerSolutionNameOptions = {
		        dataTextField: "performerName",
		        dataValueField: "performerId",
				placeholder:"Nhập mã hoặc tên nhân viên",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.searchForm.performerId = dataItem.performerId;
		        	vm.searchForm.performerName = dataItem.performerName;
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
		                    return Restangular.all("DetailMonthPlanOSRsService/doSearchStaffByPopup").post({
		                        performerName: vm.searchForm.performerName,
		                        pageSize: vm.performerSolutionNameOptions.pageSize,
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
//				'<p class="col-md-6 text-header-auto border-right-ccc">Mã NV</p>' +
				'<p class="col-md-6 text-header-auto">Tên NV</p>' +
				'</div>',
		        template: '<div class="row" ><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.performerName #</div> </div>',
		        change: function (e) {
		        	if (!vm.isSelect) {
		        		vm.searchForm.performerId = null;
			        	vm.searchForm.performerName = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.searchForm.performerId = null;
			        	vm.searchForm.performerName = null;
		            }
		        }
		    }
        
      //open popup Nhân viên
		vm.openStaffPopup = openStaffPopup;
		function openStaffPopup(param){
			var templateUrl = 'coms/resultMonthPlanTTXD/staffSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm nhân viên");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, param, 'ggfd', false, '75%','75%','staffSearchController');
	    }
		
		//save Nhân viên tiếp xúc popup tạo mới
		vm.onSaveStaff = onSaveStaff;
		function onSaveStaff(dataItem){
			vm.searchForm.performerId = dataItem.staffId;
        	vm.searchForm.performerName = dataItem.staffName;
	        htmlCommonService.dismissPopup();
	    };
	    ///
	    
	  //Auto success mã hợp đồng
		vm.contractNameOptions = {
		        dataTextField: "cntContractCode",
		        dataValueField: "cntContractId",
				placeholder:"Nhập mã hợp đồng",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.searchForm.contractId = dataItem.cntContractId;
		            vm.searchForm.cntContractCode = dataItem.cntContractCode;
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
		                    return Restangular.all("DetailMonthPlanOSRsService/doSearchContractByPopup").post({
		                    	cntContractCode: vm.searchForm.cntContractCode,
		                        pageSize: vm.contractNameOptions.pageSize,
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
//				'<p class="col-md-6 text-header-auto border-right-ccc">Mã NV</p>' +
				'<p class="col-md-6 text-header-auto">Mã hợp đồng</p>' +
				'</div>',
		        template: '<div class="row" ><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.cntContractCode #</div> </div>',
		        change: function (e) {
		        	if (!vm.isSelect) {
		        		vm.searchForm.contractId = null;
			        	vm.searchForm.cntContractCode = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.searchForm.contractId = null;
			        	vm.searchForm.cntContractCode = null;
		            }
		        }
		    }
        
      //open popup hợp đồng
		vm.openContractPopup = openContractPopup;
		function openContractPopup(param){
			var templateUrl = 'coms/resultMonthPlanTTXD/contractSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm hợp đồng");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, param, 'ggfd', false, '75%','75%','contractSearchController');
	    }
		
		//save hợp đồng 
		vm.onSaveContract = onSaveContract;
		function onSaveContract(dataItem){
			vm.searchForm.contractId = dataItem.cntContractId;
        	vm.searchForm.cntContractCode = dataItem.cntContractCode;
	        htmlCommonService.dismissPopup();
	    };
      //end controller
}
})();
