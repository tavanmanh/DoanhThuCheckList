(function() {
    'use strict';
    var controllerId = 'rpMonthProgressController';

    angular.module('MetronicApp').controller(controllerId, rpMonthProgressController);

    function rpMonthProgressController($scope, $rootScope, $timeout, gettextCatalog,
                                kendoConfig, $kWindow,
                                CommonService, PopupConst, Restangular, RestEndpoint,Constant,$http) {
        var vm = this;
        vm.searchForm={
        	obstructedState:1
        };
        vm.d={};
        vm.String="Quản lý công trình > Báo cáo > Tiến độ KH tháng";
        
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
            fillDataTable();
            initDropDownList();
            vm.searchForm.fullYear = kendo.toString(new Date(), "MM/yyyy");
        }
        function initDropDownList(){
            vm.progressOptions={
                dataSource:[
                    {id:1,name:"Vượt chỉ tiêu"},
                    {id:0,name:"Chưa vượt chỉ tiêu"}
                ],
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true
            }
        }

        vm.listMonthPlan=[];

        vm.validatorOptions = kendoConfig.get('validatorOptions');
        setTimeout(function(){
            $("#keySearch").focus();
        },15);
        /*
		 * setTimeout(function(){ $("#appIds1").focus(); },15);
		 */
        var record=0;

        vm.doSearch = function(){
            vm.rpMonthPlanProgressGrid.dataSource.page(1);
        };

        // Xuất PDF
        vm.exportpdf= function(){
            var obj={};
            obj.listStockId=vm.stock_transSearch.listStockId;
            obj.startDate=vm.stock_transSearch.startDate;
            obj.endDate=vm.stock_transSearch.endDate;
            obj.loginName=vm.stock_transSearch.loginName;
            obj.reportType="PDF";
            obj.reportName="BaoCaoKeHoachNam";
            var ds1=$("#stock_transGrid").data("kendoGrid").dataSource.data();
            if (ds1.length === 0&&obj.listStockId.length===0&&obj.endDate==undefined&&obj.loginName==undefined&&obj.startDate===kendo.toString(new Date((new Date()).getTime()-30*24*3600*1000),"dd/MM/yyyy")){
                toastr.error(gettextCatalog.getString("Không có dữ liệu xuất "));
                return ;
            }
            CommonService.exportReport(obj).then(
                function(data) {
                    var binarydata= new Blob([data],{ type:'application/pdf'});
                    kendo.saveAs({dataURI: binarydata, fileName: "Báo cáo phiếu xuất kho đang đi đường" + '.pdf'});
                }, function(errResponse) {
                    toastr.error("Lỗi không export PDF được!");
                });
        }


        function fillDataTable() {
            vm.gridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
                dataSource:{
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                        	$("#countRPMonthProgress").text(""+response.total);
                            vm.count=response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "tmpnTargetService/reportProgress",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page
                            vm.searchForm.pageSize = options.pageSize
                            if(vm.searchForm.fullYear != null){
                                vm.searchForm.month = vm.searchForm.fullYear.split("/")[0];
                                vm.searchForm.year = vm.searchForm.fullYear.split("/")[1];
                            }
                            return JSON.stringify(vm.searchForm)

                        }
                    },
                    pageSize: 100
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
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
                        field:"stt",
                        template: function () {
                            return ++record;
                        },
                        width: '2%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,  {
                        title: "KH tháng",
                        width: '8%',
                        field:"month",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return "Tháng "+data.month+"/"+data.year;
                        }
                    },{
                        title: "Đơn vị",
                        field: 'sysGroupName',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Sản lượng",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Chỉ tiêu",
                                field: 'quantity',
                                width: '6%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.quantity);
                                }, 
                                footerTemplate: function(item) {
                                	var data = vm.rpMonthPlanProgressGrid.dataSource.data();
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
                                title: "Hiện trạng",
                                field: 'currentQuantity',
                                width: '5%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.currentQuantity);
                                },
                                footerTemplate: function(item) {
                                	var data = vm.rpMonthPlanProgressGrid.dataSource.data();
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
                                title: "% Tiến độ",
                                field: 'code',
                                width: '5%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.progressQuantity) + "%";
                                },
                                footerTemplate: function(item) {
                                	var data = vm.rpMonthPlanProgressGrid.dataSource.data();
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

                            }
                        ]
                    },
                    {
                        title: "HSHC",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Chỉ tiêu",
                                field: 'complete',
                                width: '5%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.complete);
                                },
                                footerTemplate: function(item) {
                                	var data = vm.rpMonthPlanProgressGrid.dataSource.data();
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
							//hoanm1_20181211_start
							 {
                                title: "Trạm tạm tính",
                                field: 'countStationPlan',
                                width: '7%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                //template: function(data){
                                //    return numberWithCommas(data.countStationPlan);
                               // },
							   footerTemplate: function(item) {
                                	var data = vm.rpMonthPlanProgressGrid.dataSource.data();
                                	var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                    	if (idx == 0) {
                                    		item = data[idx];
                                    		sum =numberWithCommas(item.countStationPlanTotal||0);
                                    		break;
                                    	}
                                    }
                                    return kendo.toString(sum, "");
            					},
                            },
							 {
                                title: "Giá trị tạm tính",
                                field: 'completePlanMonth',
                                width: '7%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                               // template: function(data){
                                 //   return numberWithCommas(data.completePlanMonth);
                                //},
                                footerTemplate: function(item) {
                                	var data = vm.rpMonthPlanProgressGrid.dataSource.data();
                                	var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                    	if (idx == 0) {
                                    		item = data[idx];
                                    		sum =numberWithCommas(item.completePlanTotal||0);
                                    		break;
                                    	}
                                    }
                                    return kendo.toString(sum, "");
            					},

                            },
                            {
                                title: "% Tiến độ tạm tính",
                                field: 'progressCompletePlanMonth',
                                width: '8%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.progressCompletePlanMonth) + "%";
                                },
                                footerTemplate: function(item) {
                                	var data = vm.rpMonthPlanProgressGrid.dataSource.data();
                                	var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                    	if (idx == 0) {
                                    		item = data[idx];
                                    		sum =numberWithCommas(item.progressCompletePlanTotal||0) + "%";
                                    		break;
                                    	}
                                    }
                                    return kendo.toString(sum, "");
            					},
                            },
							 {
                                title: "Trạm phê duyệt",
                                field: 'countStation',
                                width: '7%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                              //  template: function(data){
                              //      return numberWithCommas(data.countStation);
                              //  },
							  footerTemplate: function(item) {
                                	var data = vm.rpMonthPlanProgressGrid.dataSource.data();
                                	var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                    	if (idx == 0) {
                                    		item = data[idx];
                                    		sum =numberWithCommas(item.countStationTotal||0);
                                    		break;
                                    	}
                                    }
                                    return kendo.toString(sum, "");
            					},
                            },
							//hoanm1_20181211_end
							
                            {
                                title: "Giá trị phê duyệt",
                                field: 'currentComplete',
                                width: '8%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.currentComplete);
                                },
                                footerTemplate: function(item) {
                                	var data = vm.rpMonthPlanProgressGrid.dataSource.data();
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
                                title: "% Tiến độ phê duyệt",
                                field: 'code',
                                width: '8%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.progressComplete) + "%";
                                },
                                footerTemplate: function(item) {
                                	var data = vm.rpMonthPlanProgressGrid.dataSource.data();
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
                            }
                        ]
                    },
                    {
                        title: "Doanh thu",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Chỉ tiêu",
                                field: 'revenue',
                                width: '5%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.revenue);
                                },
                                footerTemplate: function(item) {
                                	var data = vm.rpMonthPlanProgressGrid.dataSource.data();
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
                                title: "Hiện trạng",
                                field: 'currentRevenueMonth',
                                width: '5%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.currentRevenueMonth);
                                },
                                footerTemplate: function(item) {
                                	var data = vm.rpMonthPlanProgressGrid.dataSource.data();
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
                                title: "% Tiến độ",
                                field: 'progressRevenueMonth',
                                width: '5%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.progressRevenueMonth) + "%";
                                },
                                footerTemplate: function(item) {
                                	var data = vm.rpMonthPlanProgressGrid.dataSource.data();
                                	var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                    	if (idx == 0) {
                                    		item = data[idx];
                                    		sum =numberWithCommas(item.progressRevenueTotal||0)  + "%";
                                    		break;
                                    	}
                                    }
                                    return kendo.toString(sum, "");
            					},
                            }
                        ]
                    }
                ]
            });
        }

        vm.showHideEntangledGrid1Column = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.rpMonthPlanProgressGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.rpMonthPlanProgressGrid.showColumn(column);
            } else {
                vm.rpMonthPlanProgressGrid.hideColumn(column);
            }


        }

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };

        // HuyPQ-25/08/2018-start
        vm.exportpdf = function(){
        	function displayLoading(target) {
        	      var element = $(target);
        	      kendo.ui.progress(element, true);
        	      setTimeout(function(){
        	    	  
        	    	  var obj={};
        	            obj = vm.searchForm;
        	            obj.reportType = "DOC";
        	            obj.reportName = "Export_Tien_Do_Ke_Hoach_Thang";

        	            $http({url: RestEndpoint.BASE_SERVICE_URL + "tmpnTargetService"+"/rpMonthProgress",
        	                dataType: 'json',
        	                method: 'POST',
        	                data: obj,
        	                headers: {
        	                    "Content-Type": "application/json"
        	                },
        	                responseType : 'arraybuffer'// THIS IS IMPORTANT
        	            }).success(function(data, status, headers, config){
        	                if(data.error){
        	                	kendo.ui.progress(element, false);
        	                    toastr.error(data.error);
        	                } else {
        	                	kendo.ui.progress(element, false);
        	                    var binarydata= new Blob([data],{type: "text/plain;charset=utf-8"});
        	                    kendo.saveAs({dataURI: binarydata, fileName: "Export_Tien_Do_Ke_Hoach_Thang" + '.pdf'});
        	                }

        	            })
        	                .error(function(data){
        	                	kendo.ui.progress(element, false);
        	                    toastr.error("Có lỗi xảy ra!");
        	                });
        		});
        			
        	  }
        		displayLoading(".tab-content");
        }
        //HuyPQ-end
        //HuyPQ-25/08/2018-edit-start
        vm.exportexcel= function(){
        	debugger;
        	function displayLoading(target) {
        	      var element = $(target);
        	      kendo.ui.progress(element, true);
        	      setTimeout(function(){
        	    	  
        	    	  if(vm.searchForm.fullYear != null){
        	                vm.searchForm.month = vm.searchForm.fullYear.split("/")[0];
        	                vm.searchForm.year = vm.searchForm.fullYear.split("/")[1];
        	            }
        	            return Restangular.all("tmpnTargetService/exportPlanProgress").post(vm.searchForm).then(function (d) {
        	                var data = d.plain();
        	                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	                kendo.ui.progress(element, false);
        	            }).catch(function (e) {
        	            	kendo.ui.progress(element, false);
        	                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        	                return;
        	            });;

        		});
        			
        	  }
        		displayLoading(".tab-content");
        }
      //HuyPQ-edit-end
        
        function numberWithCommas(x) {
            if(x == null || x == undefined){
            return '0';
            }
            var parts = x.toFixed(2).toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
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
        	if(x==2){
        		vm.searchForm.progress=null;
        	}
        	if(x==3){
        		vm.searchForm.fullYear=null;
        		vm.searchForm.month=null;
        		vm.searchForm.year=null;

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
                        return response.data; // data is returned in
                        // the "data" field of
                        // the response
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
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
    }
})();
