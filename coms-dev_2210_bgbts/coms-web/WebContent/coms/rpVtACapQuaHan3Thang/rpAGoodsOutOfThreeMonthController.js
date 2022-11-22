(function() {
	'use strict';
	var controllerId = 'rpAGoodsOutOfThreeMonthController';

	angular.module('MetronicApp').controller(controllerId,
			rpAGoodsOutOfThreeMonthController);

	function rpAGoodsOutOfThreeMonthController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow,
			CommonService, PopupConst, Restangular, RestEndpoint, Constant,
			htmlCommonService) {
		var vm = this;
		vm.String = "Quản lý công trình > Báo cáo > Báo cáo vật tư A cấp quá hạn 135 ngày";
		vm.searchForm = {};
		
		init();
		function init(){
			fillTable();
			var date = new Date();
        	var start = new Date(date.getFullYear(), date.getMonth(), 1);
        	var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
        	vm.searchForm.dateTo = htmlCommonService.formatDate(end);
        	vm.searchForm.dateFrom = htmlCommonService.formatDate(start);
		}
		
		var record = 0;
		function fillTable(){
			vm.detailGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable:false,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: [
                    {
                        name: "actions",
                        template: '<div class="btn-group pull-right margin_top_button ">' +
                        '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: triệu VNĐ</div>'+

                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">' +
                        '<label ng-repeat="column in vm.workItemGrid.columns.slice(1,vm.workItemGrid.columns.length)| filter: vm.gridColumnShowHideFilter">' +

                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#appCountWorkItem").text("" + response.total);
                            vm.countWorkItem = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "settlementDebtARsService/doSearchThreeMonth",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page
                            vm.searchForm.pageSize = options.pageSize
                            return JSON.stringify(vm.searchForm)

                        }
                    },
                    pageSize: 10,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: false,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
                columns: [
                    {
                        title: "TT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '60px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Đơn vị thi công",
                        width: '250px',
                        field:"sysGroupName",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Hợp đồng",
                        field: 'contractCode',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Công trình",
                        field: 'constructionCode',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Ngày thực xuất vật tư",
                        field: 'realIeTransDate',
                        width: '120px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Mã vật tư thiết bị",
                        field: 'goodsCode',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tên vật tư thiết bị",
                        field: 'goodsName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Đơn vị tính",
                        field: 'goodsUnitName',
                        width: '80px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }

                    },
                    {
                        title: "Tích hợp",
                        width: '300px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        columns: [
                            {
                            	title: "Số lượng",
                                field: 'amountTotal',
                                width: '150px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
//                                format: "{0:n}",
//                                type :'number',
//                                footerTemplate: function(item) {
//                                    return kendo.toString(vm.sumHolder.numberTonDauKy, "n");
//        						},
                            }, {
                            	title: "Thành tiền",
            					field: 'moneyTotal',
                                width: '150px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                }
                            }
                        ]

                    },
                    {
                        title: "Quyết toán",
                        width: '300px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        columns: [
                            {
                            	title: "Số lượng",
                                field: 'amountErp',
                                width: '150px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                }
                            }, {
                            	title: "Thành tiền",
            					field: 'moneyErp',
                                width: '150px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                }
                            }
                        ]

                    },
                    {
                        title: "Thời gian quá hạn",
                        field: 'overDay',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    }
                    
    ]
    });
		}
		
		vm.doSearch = doSearch;
		function doSearch(){
			if(vm.searchForm.dateFrom==null){
				toastr.warning("Ngày tìm kiếm không được để trống !");
				$("#dateFrom").focus();
				return;
			}
			if(vm.searchForm.dateTo==null){
				toastr.warning("Ngày tìm kiếm không được để trống !");
				$("#dateTo").focus();
				return;
			}
			var grid = vm.detailGrid;
			if(grid){
				grid.dataSource.query({
		            page: 1,
		            pageSize: 10
		        });
			}
		}
		
		vm.cancelInput = function(data){
			if(data=='times'){
				vm.searchForm.dateFrom = null;
				vm.searchForm.dateTo = null;
			}
			if(data=='dept'){
				vm.searchForm.sysGroupName = null;
				vm.searchForm.BpartnerDrValue = null;
			}
		}
		
		vm.disableExportExcel = false;
		//HuyPQ-25/08/2018-edit-start
		vm.exportFile= function(){
			function displayLoading(target) {
		      var element = $(target);
		      kendo.ui.progress(element, true);
		      setTimeout(function(){
		    	  
				if (vm.disableExportExcel)
		        		return;
		        	vm.disableExportExcel = true;
		        	return Restangular.all("settlementDebtARsService/exportExcelACapThreeMonth").post(vm.searchForm).then(function (d) {
		        	    var data = d.plain();
		        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
		        	    kendo.ui.progress(element, false);
		        	    vm.disableExportExcel = false;
		        	}).catch(function (e) {
		        		kendo.ui.progress(element, false);
		        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
		        	    vm.disableExportExcel = false;
		        	    return;
		        	});
			});
				
		  }
			displayLoading("#detailGrid");
		}
		
		vm.selectedDept1 = false;
		vm.deprtOptions1 = {
		    dataTextField: "name",
		    dataValueField: "id",
			placeholder:"Nhập mã hoặc tên đơn vị",
		    select: function (e) {
		        vm.selectedDept1 = true;
		        var dataItem = this.dataItem(e.item.index());
		        //vm.searchForm.sysGroupName = dataItem.id;
				vm.searchForm.sysGroupName = dataItem.text;
		        vm.searchForm.bpartnerDrValue = dataItem.id;
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
		                    name: vm.searchForm.sysGroupName,
		                    pageSize: vm.deprtOptions1.pageSize
		                }).then(function (response) {
		                    options.success(response);
		                }).catch(function (err) {
		                    console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
		                });
		            }
		        }
		    },
		    template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
		    change: function (e) {
		        if (e.sender.value() === '') {
		        	vm.searchForm.sysGroupName = null;
					vm.searchForm.bpartnerDrValue=null;
		        }
		    },
		    ignoreCase: false
		}
		
		vm.openDepartmentTo = openDepartmentTo

		function openDepartmentTo(popUp) {
		    vm.obj = {};
		    vm.departmentpopUp = popUp;
		    var templateUrl = 'coms/popup/findDepartmentPopUp.html';
		    var title = gettextCatalog.getString("Tìm kiếm đơn vị");
		    CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
		}

		vm.onSave = onSave;
		function onSave(data) {
		    if (vm.departmentpopUp === 'dept') {
		    	vm.searchForm.sysGroupName = data.text;
				vm.searchForm.bpartnerDrValue = data.id;
		    }
		}
		
//end controller
	}
})();