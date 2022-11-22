(function() {
	'use strict';
	var controllerId = 'rpProgressMonthPlanOsController';

	angular.module('MetronicApp').controller(controllerId,
			rpProgressMonthPlanOsController);

	function rpProgressMonthPlanOsController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, rpProgressMonthPlanOsService,
			CommonService, PopupConst, Restangular, RestEndpoint, Constant,
			htmlCommonService) {
		var vm = this;
		vm.showSearch = true;
		vm.showDetail = false;
		vm.searchForm = {};
		vm.String = $rootScope.stringtile;

		init();
		function init() {
			vm.searchForm.monthYear = kendo.toString(new Date(), "MM/yyyy");
			fillDataTable([]);
		}

		vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.progressMonthOsGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.progressMonthOsGrid.showColumn(column);
			} else {
				vm.progressMonthOsGrid.hideColumn(column);
			}

		}

		vm.gridColumnShowHideFilter = function(item) {
			return item.type == null || item.type !== 1;
		};

		var record = 0;

		function fillDataTable(data) {
			vm.progressMonthOsGridOptions = kendoConfig
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
									+ '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: Triệu VNĐ</div>'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
//									+ '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>'
									+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
									+ '<label ng-repeat="column in vm.progressMonthOsGrid.columns.slice(1,vm.progressMonthOsGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}'
									+ '</label>' + '</div></div>'
						} ],
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									$("#appCountProgressMonthOs").text(
											"" + response.total);
									vm.countProgressMonthOs = response.total;
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
											+ "progressTaskOsRsService/doSearchBaoCaoTienDoOs",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									vm.searchForm.page = options.page;
									vm.searchForm.pageSize = options.pageSize;
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
									title : "Tháng",
									width : '100px',
									field : "month",
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
									title : "Năm",
									width : '100px',
									field : "year",
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
									title : "Đơn vị",
									field : 'sysGroupName',
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
									title : "Sản lượng",
									width : '300px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									columns : [
										{
											title : "Kế hoạch",
											width : '100px',
											field : "quantity",
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:center;"
											},
											type : 'text',
											editable : false,
											footerTemplate: function(item) {
			                                	var data = vm.progressMonthOsGrid.dataSource.data();
			                                	var item, sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                    	if (idx == 0) {
			                                    		item = data[idx];
			                                    		sum =numberWithCommas(item.quantityTotal||0);
			                                    		break;
			                                    	}
			                                    }
			                                    return kendo.toString(sum, "");
			            					},
										},
										{
											title : "Thực hiện",
											width : '100px',
											field : "currentQuantity",
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:center;"
											},
											type : 'text',
											editable : false,
											footerTemplate: function(item) {
			                                	var data = vm.progressMonthOsGrid.dataSource.data();
			                                	var item, sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                    	if (idx == 0) {
			                                    		item = data[idx];
			                                    		sum =numberWithCommas(item.currentQuantityTotal||0);
			                                    		break;
			                                    	}
			                                    }
			                                    return kendo.toString(sum, "");
			            					},
										},
										{
											title : "% Tiến độ",
											width : '100px',
											field : "progressQuantity",
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:center;"
											},
											type: 'text',
											format: '{0:n3}',
											editable : false,
											footerTemplate: function(item) {
			                                	var data = vm.progressMonthOsGrid.dataSource.data();
			                                	var item, sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                    	if (idx == 0) {
			                                    		item = data[idx];
			                                    		sum =numberWithCommas(item.progressQuantityTotal||0) + "%";
			                                    		break;
			                                    	}
			                                    }
			                                    return kendo.toString(sum, "");
			            					},
										},
									]
								},
								{
									title : "Quỹ lương",
									width : '300px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									columns : [
										{
											title : "Kế hoạch",
											width : '100px',
											field : "complete",
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:center;"
											},
											type : 'text',
											editable : false,
											footerTemplate: function(item) {
			                                	var data = vm.progressMonthOsGrid.dataSource.data();
			                                	var item, sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                    	if (idx == 0) {
			                                    		item = data[idx];
			                                    		sum =numberWithCommas(item.completeTotal||0);
			                                    		break;
			                                    	}
			                                    }
			                                    return kendo.toString(sum, "");
			            					},
										},
										{
											title : "Thực hiện",
											width : '100px',
											field : "currentComplete",
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:center;"
											},
											type : 'text',
											editable : false,
											footerTemplate: function(item) {
			                                	var data = vm.progressMonthOsGrid.dataSource.data();
			                                	var item, sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                    	if (idx == 0) {
			                                    		item = data[idx];
			                                    		sum =numberWithCommas(item.currentCompleteTotal||0);
			                                    		break;
			                                    	}
			                                    }
			                                    return kendo.toString(sum, "");
			            					},
										},
										{
											title : "% Tiến độ",
											width : '100px',
											field : "progressComplete",
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:center;"
											},
											type : 'text',
											format: '{0:n3}',
											editable : false,
											footerTemplate: function(item) {
			                                	var data = vm.progressMonthOsGrid.dataSource.data();
			                                	var item, sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                    	if (idx == 0) {
			                                    		item = data[idx];
			                                    		sum =numberWithCommas(item.progressCompleteTotal||0) + "%";
			                                    		break;
			                                    	}
			                                    }
			                                    return kendo.toString(sum, "");
			            					},
										},
									]
								},
								{
									title : "HSHC",
									width : '500px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									columns : [
										{
											title : "Kế hoạch",
											width : '100px',
											field : "revenue",
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:center;"
											},
											type : 'text',
											editable : false,
											footerTemplate: function(item) {
			                                	var data = vm.progressMonthOsGrid.dataSource.data();
			                                	var item, sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                    	if (idx == 0) {
			                                    		item = data[idx];
			                                    		sum =numberWithCommas(item.revenueTotal||0);
			                                    		break;
			                                    	}
			                                    }
			                                    return kendo.toString(sum, "");
			            					},
										},
										{
											title : "Thực hiện tỉnh cập nhật",
											width : '100px',
											field : "revenueNotApprove",
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:center;"
											},
											type : 'text',
											editable : false,
											footerTemplate: function(item) {
			                                	var data = vm.progressMonthOsGrid.dataSource.data();
			                                	var item, sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                    	if (idx == 0) {
			                                    		item = data[idx];
			                                    		sum =numberWithCommas(item.revenueNotApproveTotal||0);
			                                    		break;
			                                    	}
			                                    }
			                                    return kendo.toString(sum, "");
			            					},
										},
										{
											title : "% Tiến độ",
											width : '100px',
											field : "processRevenueNotApprove",
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:center;"
											},
											type : 'text',
											editable : false,
											footerTemplate: function(item) {
			                                	var data = vm.progressMonthOsGrid.dataSource.data();
			                                	var item, sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                    	if (idx == 0) {
			                                    		item = data[idx];
			                                    		sum =numberWithCommas(item.processRevenueNotApproveTotal||0) + "%";
			                                    		break;
			                                    	}
			                                    }
			                                    return kendo.toString(sum, "");
			            					},
										},
										{
											title : "Thực hiện TTHT duyệt",
											width : '100px',
											field : "currentRevenueMonth",
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:center;"
											},
											type : 'number',
											editable : false,
											footerTemplate: function(item) {
			                                	var data = vm.progressMonthOsGrid.dataSource.data();
			                                	var item, sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                    	if (idx == 0) {
			                                    		item = data[idx];
			                                    		sum =numberWithCommas(item.currentRevenueTotal||0);
			                                    		break;
			                                    	}
			                                    }
			                                    return kendo.toString(sum, "");
			            					},
										},
										{
											title : "% Tiến độ",
											width : '100px',
											field : "progressRevenueMonth",
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:center;"
											},
											type : 'number',
											editable : false,
											footerTemplate: function(item) {
			                                	var data = vm.progressMonthOsGrid.dataSource.data();
			                                	var item, sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                    	if (idx == 0) {
			                                    		item = data[idx];
			                                    		sum =numberWithCommas(item.progressRevenueTotal||0) + "%";
			                                    		break;
			                                    	}
			                                    }
			                                    return kendo.toString(sum, "");
			            					},
										},
									]
								},
								
								{
									title : "Thu hồi dòng tiền",
									width : '300px',
									headerAttributes : {
										style : "text-align:center;font-weight: bold;"
									},
									attributes : {
										style : "text-align:left;"
									},
									columns : [
										{
											title : "Kế hoạch",
											width : '100px',
											field : "revokeCashTarget",
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:center;"
											},
											type : 'text',
											format: '{0:n3}',
											editable : false,
											footerTemplate: function(item) {
			                                	var data = vm.progressMonthOsGrid.dataSource.data();
			                                	var item, sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                    	if (idx == 0) {
			                                    		item = data[idx];
			                                    		sum =numberWithCommas(item.revokeCashTargetTotal||0);
			                                    		break;
			                                    	}
			                                    }
			                                    return kendo.toString(sum, "");
			            					},
										},
										{
											title : "Thực hiện",
											width : '100px',
											field : "revokeCashComplete",
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:center;"
											},
											type : 'text',
											format: '{0:n3}',
											editable : false,
											footerTemplate: function(item) {
			                                	var data = vm.progressMonthOsGrid.dataSource.data();
			                                	var item, sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                    	if (idx == 0) {
			                                    		item = data[idx];
			                                    		sum =numberWithCommas(item.revokeCashCompleteTotal||0);
			                                    		break;
			                                    	}
			                                    }
			                                    return kendo.toString(sum, "");
			            					},
										},
										{
											title : "% Tiến độ",
											width : '100px',
											field : "processRevokeCash",
											headerAttributes : {
												style : "text-align:center;font-weight: bold;"
											},
											attributes : {
												style : "text-align:center;"
											},
											type : 'text',
											format: '{0:n3}',
											editable : false,
											footerTemplate: function(item) {
			                                	var data = vm.progressMonthOsGrid.dataSource.data();
			                                	var item, sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                    	if (idx == 0) {
			                                    		item = data[idx];
			                                    		sum =numberWithCommas(item.processRevokeCashTotal||0) + "%";
			                                    		break;
			                                    	}
			                                    }
			                                    return kendo.toString(sum, "");
			            					},
										},
									]
								}
								]
					});
		}
		
		vm.doSearch = doSearch;
		function doSearch(){
			var grid = vm.progressMonthOsGrid;
			if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 10
				});
			}
		}
		
		vm.exportFile = function(){
			kendo.ui.progress($("#progressMonthOsGrid"), true);
			return Restangular.all("progressTaskOsRsService/exportFileBaoCaoKHOs").post(vm.searchForm).then(function (d) {
        	    var data = d.plain();
        	    kendo.ui.progress($("#progressMonthOsGrid"), false);
        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	}).catch(function (e) {
        		kendo.ui.progress($("#progressMonthOsGrid"), false);
        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        	    return;
        	});
		}
		
		vm.openDepartmentTo1=openDepartmentTo1
        function openDepartmentTo1(popUp){
            vm.obj={};
            vm.departmentpopUp=popUp;
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null,null, vm, popUp, 'string', false, '92%','89%');
        }

        vm.selectedDept1=false;
        vm.deprtOptions1 = {
            dataTextField: "text",
            dataValueField:"id",
            select: function(e) {
                vm.selectedDept1=true;
                var dataItem = this.dataItem(e.item.index());
                vm.searchForm.sysGroupName = dataItem.text;
                vm.searchForm.sysGroupId = dataItem.id;
            },
            pageSize: 10,
            open: function(e) {
                vm.selectedDept1 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function(options) {
                        vm.selectedDept1=false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({name:vm.searchForm.sysGroupName,pageSize:vm.deprtOptions1.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template:'<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
            change: function(e) {
                if (e.sender.value() === '') {
                    vm.searchForm.sysGroupName = null;// thành name
                    vm.searchForm.sysGroupId = null;
                }
            },
            ignoreCase: false
        }

        vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'dept') {
                vm.searchForm.sysGroupName = data.text;
                vm.searchForm.sysGroupId = data.id;
            }
        }
		
        vm.cancelInput = cancelInput;
        function cancelInput(x){
        	if(x==1){
        		 vm.searchForm.sysGroupName=null;
                 vm.searchForm.sysGroupId=null;
        	}
        	if(x==3){
        		vm.searchForm.monthYear=null;
        	}
           
        }
        
        vm.monthSelectorOptions = {
                start: "year",
                depth: "year"
            };
            vm.monthListOptions={
                dataSource: {
                    serverPaging: true,
                    schema: {
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            url: Constant.BASE_SERVICE_URL + "DetailMonthPlanRsService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.d.page = 1;
                            vm.d.signStateList = ['3'];
                            vm.d.pageSize = 100;
                            return JSON.stringify(vm.d);

                        }
                    }
                },
                dataValueField: "detailMonthPlanId",
                template: '<span>Tháng #:data.month#</span>'+'/'+'<span>#:data.year#</span>',
                tagTemplate: '<span>Tháng #:data.month#</span>'+'/'+'<span>#:data.year#</span>',
                valuePrimitive: true
            }
        
            function numberWithCommas(x) {
                if(x == null || x == undefined){
                return '0';
                }
                var parts = x.toFixed(2).toString().split(".");
                parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                return parts.join(".");
            }
		// end controller
	}
})();