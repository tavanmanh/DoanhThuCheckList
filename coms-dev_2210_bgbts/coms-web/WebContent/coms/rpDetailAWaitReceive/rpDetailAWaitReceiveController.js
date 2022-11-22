(function() {
	'use strict';
	var controllerId = 'rpDetailAWaitReceiveController';

	angular.module('MetronicApp').controller(controllerId,
			rpDetailAWaitReceiveController);

	function rpDetailAWaitReceiveController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, rpDetailAWaitReceiveService,
			CommonService, PopupConst, Restangular, RestEndpoint, Constant,
			htmlCommonService) {
		var vm = this;
		vm.isCreateNew = false;

		vm.detailSearch = {
				lstConfirm : 0
		};
		
		init();
		function init() {
			fillDataTable([]);
			var date = new Date();
        	var start = new Date(date.getFullYear(), date.getMonth(), 1);
        	var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
        	vm.detailSearch.dateFrom = htmlCommonService.formatDate(start);
            vm.detailSearch.dateTo = htmlCommonService.formatDate(end);
		}
		
		var record = 0;
		function fillDataTable(data) {
			vm.detailGridOptions = kendoConfig
					.getGridOptions({
						autoBind : true,
						scrollable : true,
						resizable : true,
						editable : false,
						save : function() {
							vm.workItemGrid.refresh();
						},
						dataBinding: function () {
			                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
			            },
						reorderable : true,
						toolbar : [ {
							name : "actions",
							template : '<div class="btn-group pull-right margin_top_button ">'
									+ '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: triệu VNĐ</div>'
									+
									'<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
									+
									'<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
									+
									'<label ng-repeat="column in vm.workItemGrid.columns.slice(1,vm.workItemGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
									+
									'<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}'
									+ '</label>' + '</div></div>'
						} ],
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									$("#appCountWorkItem").text(
											"" + response.total);
									vm.countWorkItem = response.total;
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
											+ "synStockTransService/reportDetailAWaitReceive",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									vm.detailSearch.page = options.page
									vm.detailSearch.pageSize = options.pageSize
									return JSON.stringify(vm.detailSearch)

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

								},
								{
									title : "Mã phiếu xuất",
									field : 'code',
									width : '250px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},

								},
								{
									title : "Trạng thái phiếu",
									field : 'confirm',
									width : '160px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},

								},
								{
									title : "Ngày xuất",
									width : '160px',
									field : 'realIeTransDateString',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',

								},
								{
									title : "Kho xuất",
									field : 'stockCode',
									width : '200px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},

								},
								{
									title : "Mã công trình",
									width : '200px',
									field : 'constructionCode',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',

								},
								{
									title : "Đơn vị",
									width : '200px',
									field : 'sysGroupName',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',

								},
								{
									title : "Người nhận",
									field : 'fullName',
									width : '160px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},

								},
								{
									title : "Email người nhận",
									field : 'email',
									width : '160px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
								},
								{
									title : "SDT Người nhận",
									field : 'phoneNumber',
									width : '160px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},

								},
								{
									title : "Tỉnh",
									width : '180px',
									field : 'catProvinceCode',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',

								},
								{
									title : "Mã nhà trạm",
									width : '200px',
									field : 'catStationHouseCode',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',

								},
								{
									title : "Mã trạm",
									width : '200px',
									field : 'catStationCode',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',

								},
								{
									title : "Hợp đồng",
									width : '200px',
									field : 'cntContractCode',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',

								},
								{
									title : "Tên vật tư thiết bị",
									width : '200px',
									field : 'goodsName',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',

								},
								{
									title : "Mã vật tư thiết bị",
									width : '200px',
									field : 'goodsCode',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',

								},
								{
									title : "Đơn vị tính",
									width : '200px',
									field : 'goodsUnitName',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:center;"
									},
									type : 'text',

								},
								{
									title : "Serial",
									width : '150px',
									field : 'serial',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									type : 'text',

								},
//								{
//									title : "Hợp đồng",
//									width : '200px',
//									field : 'cntContractCode',
//									headerAttributes : {
//										style : "text-align:center;font-weight: bold;"
//									},
//									attributes : {
//										style : "text-align:center;"
//									},
//									type : 'text',
//
//								},
								{
									title : "Yêu cầu",
									width : '200px',
									field : 'amountReal',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:right;"
									},
									type : 'number',
									format: "{0:n3}"

								},
								{
									title : "Thực xuất",
									width : '200px',
									field : 'amount',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:right;"
									},
									type : 'number',
									format: "{0:n3}"

								},
								{
									title : "Đơn giá",
									width : '200px',
									field : 'unitPrice',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:right;"
									},
									type : 'number',

								},
								{
									title : "Thành tiền",
									width : '200px',
									field : 'thanhTien',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:right;"
									},
									type : 'number',

								},
							 ]
					});
		}

		var listRemove = [{
            title: "Thao tác",
        }];
        var listConvert = [{
            field:"isDesign",
            data: {
                1: 'Có thiết kế',
                0: 'Không có thiết kế',
                null: 'Không có thiết kế'
            }
        }];
		
		vm.doSearch = doSearch;
		function doSearch(){
			var grid = vm.detailGrid;
			if(grid){
				grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
			}
		}
		
		vm.exportFile = exportFile;
        function exportFile() {
			vm.detailSearch.page = null;
			vm.detailSearch.pageSize = null;
			var data = vm.detailSearch;

			Restangular.all("synStockTransService" + "/reportDetailAWaitReceive").post(data).then(function(d) {
                var data = d.data;
				CommonService.exportFile(vm.detailGrid, d.data, listRemove, listConvert, "Báo cáo Chi tiết phiếu xuất kho A cấp chưa nhận");
			});
        }
        
        vm.stationCodeOptions = {
                dataTextField: "email",
                dataValueField: "employeeCode",
                placeholder: "Nhập vào Email",
                select: function (e) {
                    
                    vm.isSelect = true;
                    var dataItem = this.dataItem(e.item.index());
                    vm.detailSearch.email = dataItem.email;
//                    vm.workItemSearch.email = dataItem.email;
                },
                open: function (e) {
                    vm.isSelect = false;
                },
                pageSize: 10,
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function (options) {
                            vm.isSelect = false;
                            return Restangular.all("rpQuantityService/doSearchPopup").post({
                            	email: vm.detailSearch.email,
                                pageSize: vm.stationCodeOptions.pageSize,
                                page: 1
                            }).then(function (response) {
                                options.success(response.data);
                            }).catch(function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            });
                        }
                    }
                },
                headerTemplate: '<div class="dropdown-header row text-left k-widget k-header">' +
                '<p class="col-md-6 text-header-auto">Mã NV</p>' +
                '<p class="col-md-6 text-header-auto">Email</p>' +
                '</div>',
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.employeeCode #</div><div  style="padding-right: 5px;width:auto;overflow: hidden"> #: data.email #</div> </div>',
            change: function (e) {
                	if (!vm.isSelect) {
                        vm.detailSearch.fullName = null;
                        vm.detailSearch.email = null;
                    }
                },
                close: function (e) {
                    if (!vm.isSelect) {
                        vm.detailSearch.fullName = null;
                        vm.detailSearch.email = null;
                    }
                }
            }
        
        vm.selectedDept1 = false;
        vm.deprtOptions1 = {
            dataTextField: "text",
            dataValueField: "id",
        	placeholder:"Nhập mã hoặc tên đơn vị",
            select: function (e) {
                vm.selectedDept1 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.detailSearch.sysGroupName = dataItem.text;
                vm.detailSearch.sysGroupId = dataItem.id;
            },
            pageSize: 10,
            open: function (e) {
                vm.selectedDept1 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedDept1 = false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({
                            name: vm.detailSearch.sysGroupName,
                            pageSize: vm.deprtOptions1.pageSize
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.detailSearch.sysGroupName = null;// thành name
                    vm.detailSearch.sysGroupId = null;
                }
            },
            ignoreCase: false
        }
        
        vm.couponPopup = function(popUp) {
        	vm.obj = {};
        	vm.listDataObj =[];
            var templateUrl = 'coms/rpCouponExport/couponPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm người nhận");
            CommonService.populatePopupcoupon(templateUrl, title, null, null, vm, popUp, 'string', false, '80%', '70%');
        }
        
        vm.onSaveStationCode = onSaveStationCode;
    	function onSaveStationCode(data) {
    		vm.detailSearch.email = data.email;
    		vm.detailSearch.employeeCode = data.employeeCode;
    	}
    	vm.openDepartmentTo=openDepartmentTo;
    	function openDepartmentTo(popUp) {
    	    vm.obj = {};
    	    var templateUrl = 'coms/RPConstructionDK/findDepartmentPopUp.html';
    	    var title = gettextCatalog.getString("Tìm kiếm đơn vị");
    	    CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
    	}
    	
    	vm.onSave = onSave;
    	function onSave(data) {
    	    vm.detailSearch.sysGroupName = data.text;
    	    vm.detailSearch.sysGroupId = data.id;
    	    	
    	}
    	
    	vm.cancelListYear= cancelListYear;
    	function cancelListYear(){
    		vm.detailSearch.dateTo = null;
    		vm.detailSearch.dateFrom = null;
    	}
    	
    	vm.cancelInput = function (param) {
    	    if (param == 'dept') {
    	        vm.detailSearch.sysGroupId = null;
    	        vm.detailSearch.sysGroupName = null;
    	    }
    		if(param=='status') {
    	        vm.detailSearch.lstConfirm =[]
    	    }
    	}
    	
    	vm.clearStationCode=clearStationCode;
    	function clearStationCode(){
    		vm.detailSearch.email = null;
    		vm.detailSearch.employeeCode = null;
    	}
    	
		//end controller
	}
})();
