/**
 * Created by pm1_os36 on 2/26/2018.
 */
function covenantSysPXKFunction($scope, $rootScope, $timeout, gettextCatalog,$filter,
    kendoConfig, $kWindow,constructionService,
    CommonService, PopupConst, Restangular, RestEndpoint,Constant,vm,$http){
    
    vm.covenantSearch={};
    vm.initConvenant = initConvenant;
    function initConvenant() {
            vm.covenantGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: [
                    {
                        name: "actions",
                        template: '<div class="btn-group pull-right margin_top_button margin10 ">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportCovenantFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.covenantGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideCovenantColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            vm.covenantSearchCout = response.total;
                            return response.total; // total is returned in
                            // the "total" field of
                            // the response
                        },
                        data: function (response) {
                            return response.data; // data is returned in
                            // the "data" field of
                            // the response
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "workItemService/doSearchCovenant",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            // vm.constructionSearch.employeeId =
                            // Constant.user.srvUser.catEmployeeId;
                            vm.covenantSearch.page = options.page;
                            vm.covenantSearch.pageSize = options.pageSize;
                            vm.covenantSearch.type=0;
                            vm.covenantSearch.constructionId= vm.constrObj.constructionId;
                            return JSON.stringify(vm.covenantSearch)

                        }
                    },
                    pageSize: 10
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
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Số hợp đồng",
                        field: 'code',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tên hợp đồng",
                        field: 'name',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Đối tác",
                        field: 'partnerName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Đơn vị ký",
                        field: 'sysGroupName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Ngày ký",
                        width: '10%',
                        field: 'signDate',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function(dataItem){
                           if(dataItem.signDate!=''||dataItem.signDate==null){
                                return $filter('date')(dataItem.signDate,'dd/MM/yyyy');                
                            }
                            else
                                return ' ';
                        }
                    },
                    {
                        title: "Giá trị (Tr)",
                        width: '10%',
                        field: 'price',
                        format: "{0:n3}",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align: right;"
                        },
                        // template: function(dataItem){
                        // if(dataItem.price != null){
                        // return dataItem.price/1000000;
                        // }
                        // return " ";
                        // }
                    },
                    {
                        title: "Tên đơn hàng",
                        field: 'orderName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Trạng thái",
                        field: 'status',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                         template: function (dataItem) {
                            if (dataItem.status == 1) {
                                return "<span name='status' font-weight: bold;'>Đang thực hiện</span>"
                            } else if (dataItem.status == 2) {
                                return "<span name='status' font-weight: bold;'>Đã nghiệm thu</span>"
                            } else if (dataItem.status == 3) {
                                return "<span name='status' font-weight: bold;'>Đã thanh toán</span>"
                            }else if (dataItem.status == 4) {
                                return "<span name='status' font-weight: bold;'>Đã thanh lý</span>"
                            }
                            else return " ";
                        },
                    }
                ]
            });
        }
        // /xuat excell
    vm.exportCovenantFile= function(){
    	function displayLoading(target) {
  	      var element = $(target);
  	      kendo.ui.progress(element, true);
  	      setTimeout(function(){
  	    	  
  	    	return Restangular.all("workItemService/exportCovenantProgress").post(vm.covenantSearch).then(function (d) {
  	            var data = d.plain();
  	            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
  	            kendo.ui.progress(element, false);
  	        }).catch(function (e) {
  	        	kendo.ui.progress(element, false);
  	            toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
  	            return;
  	        });

  		});
  			
  	  }
  		displayLoading(".tab-content");
        
    }
    vm.exportDeliveryBill= function(){
    	function displayLoading(target) {
    	      var element = $(target);
    	      kendo.ui.progress(element, true);
    	      setTimeout(function(){
    	    	  
    	    	  return Restangular.all("workItemService/exportDeliveryBill").post(vm.deliveryBillSearch).then(function (d) {
    	              var data = d.plain();
    	              window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
    	              kendo.ui.progress(element, false);
    	          }).catch(function (e) {
    	          	kendo.ui.progress(element, false);
    	              toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
    	              return;
    	          });

    		});
    			
    	  }
    		displayLoading(".tab-content");
    }

    vm.exportCovenantFile1= function(){
    	function displayLoading(target) {
  	      var element = $(target);
  	      kendo.ui.progress(element, true);
  	      setTimeout(function(){
  	    	  
  	    	return Restangular.all("workItemService/exportCovenantProgress").post(vm.contractInputSearch).then(function (d) {
  	            var data = d.plain();
  	            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
  	            kendo.ui.progress(element, false);
  	        }).catch(function (e) {
  	        	kendo.ui.progress(element, false);
  	            toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
  	            return;
  	        });

  		});
  			
  	  }
  		displayLoading(".tab-content");
    }
     // an cot hop dong dau ra
    vm.showHideCovenantColumn = function (column) {
        if (angular.isUndefined(column.hidden)) {
            vm.covenantGrid.hideColumn(column);
        } else if (column.hidden) {
            vm.covenantGrid.showColumn(column);
        } else {
            vm.covenantGrid.hideColumn(column);
        }


    }
        /*
		 * * Filter các cột của select
		 */

    vm.gridColumnShowHideFilter = function (item) {

        return item.type == null || item.type !== 1;
    };   


        // phiếu xuất kho
    vm.deliveryBillSearch={};
    vm.deliveryBill=deliveryBill;
     vm.checkTotalPxk=false;
    function deliveryBill() {
            vm.deliveryBillGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: [
                    {
                        name: "actions",
                        template: '<div class="btn-group pull-right margin_top_button margin10 ">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportDeliveryBill()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.deliveryBillTable.columns| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideBillTableColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            vm.deliveryBillSearchCout = response.total;
                            vm.checkTotalPxk = false;
                            if(response.total>0){
                                vm.checkTotalPxk = true;
                            }
                            return response.total; // total is returned in
                            // the "total" field of
                            // the response
                        },
                        data: function (response) {
                            for(var x in response.data){
                                response.data[x].sysUserId=Constant.userInfo.VpsUserInfo.sysUserId
                                response.data[x].checkSupperViso=0;
         //                       response.data[x].visorId=vm.IdSupperVisor.supperVisorIdPxk
                                for(var y in vm.IdSupperVisor){
                                    if(Constant.userInfo.VpsUserInfo.sysUserId==vm.IdSupperVisor[y]){
                                        response.data[x].checkSupperViso=1;
                                    }
                                }
                                if(response.data[x].confirm == null)
                                    response.data[x].confirm = 0;
                            }
                            return response.data; // data is returned in
                            // the "data" field of
                            // the response
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "workItemService/doSearchDeliveryBill",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            // vm.constructionSearch.employeeId =
                            // Constant.user.srvUser.catEmployeeId;
                            vm.deliveryBillSearch.page = options.page
                            vm.deliveryBillSearch.pageSize = options.pageSize
                            vm.deliveryBillSearch.constructionId= vm.constrObj.constructionId
                            return JSON.stringify(vm.deliveryBillSearch)

                        }
                    },
                    pageSize: 10
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
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Mã yêu cầu",
                        field: 'orderCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Mã phiếu",
                        field: 'code',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Kho xuất",
                        field: 'stockName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Người tạo phiếu",
                        field: 'createdByName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Ngày thực xuất",
                        width: '10%',
                        field: 'realIeTransDate',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function(dataItem){
                            if(dataItem.realIeTransDate == null){
                               return '';
                            }
                            else
                                return $filter('date')(dataItem.realIeTransDate,'dd/MM/yyyy');
                        }
                    },
                    {
                        title: "Trạng thái ",
                        width: '10%',
                        field: 'status',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {

                            style: "text-align: left;"
                        },
                        template: function(dataItem){
                            if(dataItem.status == 1){
                               return 'Chưa nhập xuất';
                            }
                            if(dataItem.status == 2){
                               return 'Đã nhập xuất';
                            }
                            if(dataItem.status == 3){
                               return 'Đã hủy';
                            }
                            else
                                return '';
                        }
                    },
                    {
                        title: "Tình trạng xác nhận",
                        width: '10%',
                        field: 'confirm',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(dataItem){
                            if(dataItem.confirm == 1){
                               return 'Đã xác nhận';
                            }
                            else if(dataItem.confirm == 0){
                                return "Chưa xác nhận";
                            }
                            else{
                                return " ";
                            }
                        }
                    },
                    {
                        title: "Thao tác",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        template: dataItem =>
                        '<div class="text-center pull-left">'
                        +'<button style=" border: none; background-color: white;" id="detaillPXK" ng-click="vm.detaillPXK(dataItem)" class=" icon_table "'+
                            '   uib-tooltip="Chi tiết" translate>'+
                            '<i class="fa fa-list-alt" style="color:#e0d014"  aria-hidden="true"></i>'+
                        '</button>'
                        + '<button style=" border: none; background-color: white;" id=""' +
                        'class=" icon_table"  ng-click="vm.confirmPxk(dataItem)"   uib-tooltip="Xác nhận" translate' + '>' +
                        '<i class="fa fa-check" style="color: #337ab7;" ng-show="(dataItem.shipperId==dataItem.sysUserId && dataItem.confirm==0 && dataItem.synType==2) || (dataItem.checkSupperViso==1 && dataItem.confirm==0 && dataItem.synType==1)"  aria-hidden="true"></i>' +
                        '</button>'
                        + '</div>',
                        width: '10%'
                    }
                ]
            });
        }


        // an cot phieu xuat kho
        vm.showHideBillTableColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.deliveryBillTable.hideColumn(column);
            } else if (column.hidden) {
                vm.deliveryBillTable.showColumn(column);
            } else {
                vm.deliveryBillTable.hideColumn(column);
            }


        }
     

         vm.typeExportList=[
                {id: "1", name: "Chủ đầu tư"},
                {id: "2", name: "Nội bộ"},
                {id: "", name: "Tất cả"}
            ]
            
            vm.typeExport = {
            dataSource: vm.typeExportList,
            dataTextField: "name",
            dataValueField: "id",
            valuePrimitive: true
            }

            vm.typeVuongList=[
                {id: "1", name: "1.Chưa có xác nhận của CNT"},
                {id: "2", name: "2.Đã có xác nhận của CNT"},
                {id: "0", name: "3.Hết vướng"}
            ]
            
            vm.typeVuong = {
            dataSource: vm.typeVuongList,
            dataTextField: "name",
            dataValueField: "id",
            valuePrimitive: true
            }




// hợp đồng đầu vào
    vm.contractInputSearch={};
    vm.contractInputGrid = contractInputGrid;
    function contractInputGrid() {
            vm.contractInputGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: [
                    {
                        name: "actions",
                        template: '<div class="btn-group pull-right margin_top_button margin10 ">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportCovenantFile1()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.contractInput.columns| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHidecontractInputColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            vm.contractInputSearchCout = response.total;
                            return response.total; // total is returned in
                            // the "total" field of
                            // the response
                        },
                        data: function (response) {
                            return response.data; // data is returned in
                            // the "data" field of
                            // the response
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "workItemService/doSearchContractInput",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            // vm.constructionSearch.employeeId =
                            // Constant.user.srvUser.catEmployeeId;
                            vm.contractInputSearch.page = options.page
                            vm.contractInputSearch.pageSize = options.pageSize
                            vm.contractInputSearch.type=1
                            vm.contractInputSearch.constructionId= vm.constrObj.constructionId
                            return JSON.stringify(vm.contractInputSearch)

                        }
                    },
                    pageSize: 10
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
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Số hợp đồng",
                        field: 'code',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tên hợp đồng",
                        field: 'name',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Đơn vị ký",
                        field: 'sysGroupName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Đối tác",
                        field: 'partnerName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Ngày ký",
                        width: '10%',
                        field: 'signDate',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function(dataItem){
                            if(dataItem.signDate!=''||dataItem.signDate==null){
                                return $filter('date')(dataItem.signDate,'dd/MM/yyyy');                
                            }
                            else
                                return ' ';
                        }
                    },
                    {
                        title: "Hợp đồng đầu ra",
                        field: 'outContract',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align: left;"
                        }
                    },
                    {
                        title: "Giá trị (Tr)",
                        width: '10%',
                        field: 'price',
                        format: "{0:n3}",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        // template: function(dataItem){
                        // if(dataItem.price != null){
                        // return dataItem.price/1000000;
                        // }
                        // return " ";
                        // }
                    },
                    {
                        title: "Tình trạng",
                        field: 'status',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                         template: function (dataItem) {
                            if (dataItem.status == 1) {
                                return "<span name='status' font-weight: bold;'>Đang thực hiện</span>"
                            }
                            else if (dataItem.status == 0) {
                                return "<span name='status' font-weight: bold;'>Đã hủy</span>"
                            } else if (dataItem.status == 4) {
                                return "<span name='status' font-weight: bold;'>Đã thanh lý</span>"
                            }else{
                                return "<span name='status' font-weight: bold;'></span>"
                            }
                        },
                    }
                ]
            });
        }
         // an cot dau vao
        vm.showHidecontractInputColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.contractInput.hideColumn(column);
            } else if (column.hidden) {
                vm.contractInput.showColumn(column);
            } else {
                vm.contractInput.hideColumn(column);
            }


        }

         vm.doSearchPXK=doSearchPXK;
        function doSearchPXK(){
            var grid =vm.deliveryBillTable;
            if(grid){
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        }

        vm.statusEntangledList=[
                {id: "", name: "Tất cả"},
                {id: "2", name: "Đã có xác nhận CĐT"},
                {id: "1", name: "Chưa có xác nhận CĐT"},
                {id: "0", name: "Đóng"}
            ]
            vm.statusEntangled = {
            dataSource: vm.statusEntangledList,
            dataTextField: "name",
            dataValueField: "id",
            valuePrimitive: true
            }       
            vm.entangledSearch ={};
            vm.entangledGrid = entangledGrid ; 
             function entangledGrid() {
            vm.entangledGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: [
                    {
                        name: "actions",
                        template: '<div class=" pull-left ">' +
                        '<button class="btn btn-qlk padding-search-right addQLK"' +
                        'ng-click="vm.addEntangled()" ng-disabled="vm.construction1" uib-tooltip="Tạo mới" translate>Tạo mới</button>' +
                        '</div>'
                        +
                        '<div class="btn-group pull-right margin_top_button margin10 ">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportVuongFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.entangledGrid1.columns| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideEntangledGrid1Column(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            vm.entangledlSearchCout = response.total;
                            return response.total; // total is returned in
                            // the "total" field of
                            // the response
                        },
                        data: function (response) {
                            return response.data; // data is returned in
                            // the "data" field of
                            // the response
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "workItemService/doSearchEntangled",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            // vm.constructionSearch.employeeId =
                            // Constant.user.srvUser.catEmployeeId;
                            vm.entangledSearch.page = options.page
                            vm.entangledSearch.pageSize = options.pageSize
                            vm.entangledSearch.constructionId= vm.constrObj.constructionId
                            return JSON.stringify(vm.entangledSearch)

                        }
                    },
                    pageSize: 10
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
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Mã công trình",
                        field: 'constructionCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Nội dung vướng",
                        field: 'obstructedContent',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Người tạo",
                        field: 'createdUserName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Ngày tạo",
                        field: 'createdDate',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function(dataItem){
                            if(dataItem.createdDate!=''&&dataItem.createdDate!=null){
                                return $filter('date')(dataItem.createdDate,'dd/MM/yyyy');                
                            }
                            else
                                return ' ';
                        }
                    },
                    {
                        title: "Ngày đóng",
                        width: '10%',
                        field: 'closedDate',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function(dataItem){
                            if(dataItem.closedDate!=''&&dataItem.closedDate!=null){
                                return $filter('date')(dataItem.closedDate,'dd/MM/yyyy');                
                            }
                            else
                                return ' ';
                        }
                    },
                    {
                        title: "Trạng thái vướng",
                        field: 'obstructedState',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                         template: function (dataItem) {
                            if (dataItem.obstructedState == 1) {
                                return "<span name='status' font-weight: bold;'>Chưa có xác nhận của CNT</span>"
                            } else if (dataItem.obstructedState == 2) {
                                return "<span name='status' font-weight: bold;'>Đã có xác nhận của CNT</span>"
                            } else if (dataItem.obstructedState == 0) {
                                return "<span name='status' font-weight: bold;'>Hết vướng</span>"
                            }else return ''
                        },
                    },
                    {
                        title: "Thao tác",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        template: dataItem =>
                        '<div class="text-center">'
                        + '<button style=" border: none; background-color: ng-hide="dataItem.obstructedState==0" white;" id="updateId" ng-click="vm.editEntangled(dataItem)" class=" icon_table "' +
                        '   uib-tooltip="Sửa" translate>'
                        +'<i class="fa fa-list-alt" style="color:#e0d014" ng-hide="dataItem.obstructedState==0" aria-hidden="true"></i>'+

                        '</button>'
                        + '</div>',
                        width: '10%'
                    }
                ]
            });
        }
        // an cot vuong
        vm.showHideEntangledGrid1Column = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.entangledGrid1.hideColumn(column);
            } else if (column.hidden) {
                vm.entangledGrid1.showColumn(column);
            } else {
                vm.entangledGrid1.hideColumn(column);
            }


        }

    vm.entangledSearch={};
     vm.doSearchEl=doSearchEl;
    function doSearchEl(){
        var grid =vm.entangledGrid1;
        if(grid){
            grid.dataSource.query({
                page: 1,
                pageSize: 10
            });
        }
    }
    vm.addEntangled = addEntangled;
    function addEntangled(){
        vm.addVuongItem={ obstructedState:1};
        var grid =vm.vuongGrid;
         if(grid){
                    grid.dataSource.data([]);
                    grid.dataSource.sync();
                    
                }
        vm.action = "createdVuong";        
        var teamplateUrl = "coms/construction/popupAddEntangled.html";
        var title = "Thêm vướng của công trình";
        var windowId = "POPUP_VUONG";
        CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '900', '500', "content");
    }
    vm.editEntangled = editEntangled;
    function editEntangled(item){
        vm.addVuongItem=angular.copy(item);
        vm.action = "updateVuong";
        var teamplateUrl = "coms/construction/popupAddEntangled.html";
        var title = "Chỉnh sửa vướng của công trình";
        var windowId = "POPUP_VUONG";
        CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '900', '500', "content");
        
    }
     $scope.$on("Popup.open", function () {
            // var grid = $("#yearPlanDetail").data("kendoGrid");
            if(vm.action=="updateVuong"){
             Restangular.all("obstructedRsService"+"/getAttachFileById").post(vm.addVuongItem.obstructedId).then(function(data){
             vm.addVuongItem.listFileVuong = data.listFileVuong;
              var grid =vm.vuongGrid;
            if(vm.addVuongItem!=null &&vm.addVuongItem.listFileVuong!=null)
                if(grid){
                    grid.dataSource.data(vm.addVuongItem.listFileVuong);
                    grid.refresh();
                }
         },function(error){});
         }
    });
    vm.detaillPXK = detaillPXK;
    function detaillPXK(dataa){
        vm.tab1= true;
        vm.tab2=false;
        vm.tab3=false;
        vm.dataItemPhieuXK = dataa;
        var teamplateUrl = "coms/sysPXK/popupDetailPXK.html";
        var title = "Thông tin phiếu xuất kho";
        var windowId = "POPUP_PHIEU_XK";
        
        CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '615', "");

        if(dataa.type==2){
            Restangular.all("constructionService"+"/getDataSign").post(dataa.synStockTransId).then(function(data){
                if(data!=undefined){
                vm.dataItemPhieuXK.dataSign = data;
                // vm.dataItemPhieuXK.thuKho = data[0].name+" - "data[0].email+"
				// - "data[0].name
            }
            },function(error){})
        }   
        vm.fillDataTableGoodsList();
        vm.fillDataTableGoodsDetail();
        var gridTTHH = vm.goodsDetailGrid;
        if (gridTTHH) {
            gridTTHH.dataSource.query({
                page: 1,
                pageSize: 10
            });

        };
        var gridTTHH1 = vm.goodsListGrid;
        if (gridTTHH1) {
            gridTTHH1.dataSource.query({
                page: 1,
                pageSize: 10
            });

        };
    }
    vm.gotoTabOnePopUp = function(){
        vm.tab2=false;
        vm.tab1=true;
        vm.tab3=false;
    }
     vm.gotoTabTwoPopUp = function(){
        vm.tab2=true;
        vm.tab1=false;
        vm.tab3=false;
    }
    vm.gotoTabTherePopUp = function(){
        vm.tab2=false;
        vm.tab1=false;
        vm.tab3=true;
    }
    vm.orderGoodsSearch={};
    vm.fillDataTableGoodsList = fillDataTableGoodsList;
    function fillDataTableGoodsList(data) {
            vm.goodsListGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                resizable: true,
                scrollable: true,
                change : onChangeGoodList,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                dataBound: function(e) {
                     var grid = $("#goodsListGrid").data("kendoGrid");
                         grid.select("tr:eq(0)");
                },
                dataSource: {
                    serverPaging: true,
                     schema: {
                         total: function (response) {
                                return response.total; // total is returned in
                                                        // the "total" field of
                                                        // the response
                            },
                            data: function (response) {
                                return response.data; // data is returned in
                                                        // the "data" field of
                                                        // the response
                            },
                        },
                    transport: {
                        read: {
                                // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "workItemService/GoodsListTable",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },                  
                        parameterMap: function (options, type) {
                             // vm.appParamSearch.employeeId =
                                // Constant.user.srvUser.catEmployeeId;
                                vm.orderGoodsSearch.page = options.page;
                                vm.orderGoodsSearch.pageSize = options.pageSize;
                                vm.orderGoodsSearch.idTable = vm.dataItemPhieuXK.synStockTransId;
                                vm.orderGoodsSearch.typeExport = vm.dataItemPhieuXK.synTransType;

                                return JSON.stringify(vm.orderGoodsSearch)
                        }
                    },                   
                    pageSize: 20
                },
                noRecords: true,
                messages: {
                    noRecords : gettextCatalog.getString("Không có kết quả hiển thị")
                },
                pageable: {
                    refresh: false,
                     pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "Không có kết quả hiển thị"
                    }
                },
                columns: [
                {
                    title: "STT",
                    field:"stt",
                    template: dataItem => $("#goodsListGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
                    width: 70,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                }
                ,  {
                    title: "Mã hàng",
                    field: 'goodsCode',
                    width: 90,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    },
                }, {
                    title: "Tên hàng",
                    field: 'goodsName',
                    width: 200,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    },
                }, {
                    title: "Đơn vị tính",
                    field: 'goodsUnitName',
                    width: 90,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    },
                },  {
                    title: "Số lượng",
                     field: 'amountReal',
                    width: 90,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:right;"
                    },
                },  {
                    title: "Tình trạng",
                    width: 120,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: function (dataItem) {
                        if (dataItem.goodsState == 1) {
                            return "<span name='status' font-weight: bold;'>Bình thường</span>"
                        }if (dataItem.goodsState == 2) {
                            return "<span name='status' font-weight: bold;'>Hỏng</span>"
                        }
                        else
                            return "";

                    }
                }]

            });
        }
        vm.orderGoodsDetailSearch={};
        vm.fillDataTableGoodsDetail=fillDataTableGoodsDetail;
        function fillDataTableGoodsDetail(data) {
            vm.goodsDetailGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                resizable: true,
                dataSource: {
                    serverPaging: true,
                     schema: {
                         total: function (response) {
                                return response.total; // total is returned in
                                                        // the "total" field of
                                                        // the response
                            },
                            data: function (response) {
                                return response.data; // data is returned in
                                                        // the "data" field of
                                                        // the response
                            },
                        },
                    transport: {
                        read: {
                                // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "workItemService/GoodsListDetail",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },                  
                        parameterMap: function (options, type) {
                            vm.orderGoodsDetailSearch.page = options.page;
                            vm.orderGoodsDetailSearch.pageSize = options.pageSize;
                            vm.orderGoodsDetailSearch.idDetail = vm.orderGoodsDetailSearch.synStockTransDetailId;
                            vm.orderGoodsDetailSearch.typeExport = vm.dataItemPhieuXK.synTransType;
                            return JSON.stringify(vm.orderGoodsDetailSearch);
                        }
                    },                   
                    pageSize: 20
                },
                noRecords: true,
                messages: {
                    noRecords : gettextCatalog.getString("Không có kết quả hiển thị")
                },
                pageable: {
                    refresh: false,
                     pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "Không có kết quả hiển thị"
                    }
                },
                columns: [
                {
                    title: "STT",
                    field:"stt",
                    template: dataItem => $("#goodsDetailGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
                    width: 70,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                }
                ,  {
                    title: "Serial",
                    field: 'serial',
                    width: 150,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    },
                }, {
                    title: "Mã hợp đồng",
                    field: 'contractCode',
                    width: 150,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    },
                }, {
                    title: "Part number",
                    field: 'partNumber',
                    width: 90,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:right;"
                    },
                },  {
                    title: "Hãng sản xuất",
                     field: 'nameCountry',
                    width: 170,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    },
                },  {
                    title: "Nước sản xuất",
                     field: 'nameMan',
                    width: 170,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    },
                }]
            });
            
        }
    vm.confirmPxk = confirmPxk;
    function confirmPxk(data){
        data.typeExport = data.synType;
        data.userLogin = Constant.userInfo.VpsUserInfo.sysUserId;
        confirm('Bạn có chắc chắn xác nhận phiếu xuất kho?', function(){
                constructionService.confirmPkx(data).then(
                    function(d) {
                        toastr.success("Xác nhận phiếu xuất kho thành công!");
                         $("#deliveryBillTable").data('kendoGrid').dataSource.read();
                         $("#deliveryBillTable").data('kendoGrid').refresh(); 
                         vm.initDataNT();

                    }, function(errResponse) {
                        toastr.error("Lỗi!");
                    });
            } )
    }
    function onChangeGoodList(){
            
            if ($("#goodsListGrid").data("kendoGrid").select().length > 0){
                var tr = $("#goodsListGrid").data("kendoGrid").select().closest("tr");
                var dataItem = $("#goodsListGrid").data("kendoGrid").dataItem(tr);
                
                vm.orderGoodsDetailSearch = dataItem;
                $("#nameHangHoa").text(vm.orderGoodsDetailSearch.goodsCode+" "+vm.orderGoodsDetailSearch.goodsName);
                if(vm.orderGoodsDetailSearch.goodsState==1){
                    $("#trangThai").text("Bình thường");
                }
                if(vm.orderGoodsDetailSearch.goodsState==2){
                    $("#trangThai").text("Hỏng");
                }
                var grid = $("#goodsDetailGrid").data("kendoGrid"); 
                if(grid){
                    grid.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });
                } else {
                    fillDataTableGoodsDetail([]);
                }
            }
            
        }
        vm.editWorkItem = editWorkItem;
    vm.commonSearch = {name: '', code: ''};
        function editWorkItem(dataItem) {
            vm.showHm1 = true;
            vm.showHm = false;
            vm.checkEditGPon = true;
            vm.workItemRecord = angular.copy(dataItem);
            vm.workItemRecord.price=numberWithCommas(dataItem.price);
            vm.disabledHangMuc = dataItem.status!=1;
            if(dataItem.catConstructionTypeId == 3){
            	debugger;
            	vm.lstWorkItemGPon = [];
            	vm.workItemRecord.price = dataItem.priceCable;
            	vm.workItemRecord.nodeCode = dataItem.name.replace(dataItem.catWorkItemType,"");
            	if(vm.workItemRecord.nodeCode == ""){
            		vm.checkNode = true;
            	} else {
            		vm.checkNode = false;
            	}
            	var obj = {};
            	obj.amount = dataItem.amount;
            	obj.price = dataItem.priceCable;
            	obj.priceChest = dataItem.priceChest;
            	obj.totalAmountChest = dataItem.totalAmountChest;
            	obj.totalAmountGate = dataItem.totalAmountGate;
            	obj.priceGate = dataItem.priceGate;
            	obj.workItemId = dataItem.workItemId;
            	obj.catConstructionTypeId = dataItem.catConstructionTypeId;
            	obj.catWorkItemType = dataItem.catWorkItemType;
            	obj.catWorkItemTypeId = dataItem.catWorkItemTypeId;
            	obj.constructionId = dataItem.constructionId;
            	obj.constructorName = dataItem.constructorName;
            	obj.isInternal = dataItem.isInternal;
            	obj.code = dataItem.constructionCode + dataItem.catWorkItemType;//
            	obj.name = dataItem.name;
            	vm.workItemId =dataItem.workItemId;
            	vm.lstWorkItemGPon.push(obj);
            	$scope.vm.fillDataGrid(vm.lstWorkItemGPon);
            	var teamplateUrl = "coms/construction/workItemPopupGPonDetail.html";
                var title = "Thông tin hạng mục";
                var windowId = "CONSTRUCTION_LAND_HANDOVER_PLAN";
                CommonService.populatePopupPartner(teamplateUrl, title, null, vm, windowId, true, '80%', '80%', "");
            } else {
            	var teamplateUrl = "coms/construction/workItemPopupDetail.html";
                var title = "Thông tin hạng mục";
                var windowId = "CONSTRUCTION_LAND_HANDOVER_PLAN";
                CommonService.populatePopupPartner(teamplateUrl, title, null, vm, windowId, true, '600', 'unset', "");
            }
        }
        function numberWithCommas(x) {
            if(x == null || x == undefined){
                return '';
            }            
            var parts = x.toFixed(2).toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
        }
    vm.exportNoSerial = function(){
        var obj={};
        obj = vm.dataItemPhieuXK;
        obj.objectId = vm.dataItemPhieuXK.synStockTransId;
        obj.typeData = vm.dataItemPhieuXK.synType;
        obj.shipper =vm.dataItemPhieuXK.shipperName;
        obj.getNameDVYC =   vm.dataItemPhieuXK.deptReceiveName;
        obj.namePetitioner =   vm.dataItemPhieuXK.bussinessTypeName;
        obj.reportType = "DOC";
        obj.reportName = "YeuCauXuatKho_KhongSerial";

        $http({url: RestEndpoint.BASE_SERVICE_URL + "constructionService"+"/noSerial",
            dataType: 'json',
            method: 'POST',
            data: obj,
            headers: {
                "Content-Type": "application/json"
            },
            responseType : 'arraybuffer'// THIS IS IMPORTANT
        }).success(function(data, status, headers, config){
            if(data.error){
                toastr.error(data.error);
            } else {
                 var binarydata= new Blob([data],{type: "text/plain;charset=utf-8"});
        kendo.saveAs({dataURI: binarydata, fileName: "YeuCauKhongSerial" + '.docx'});
            }

        })
            .error(function(data){
                toastr.error("Có lỗi xảy ra!");
            });
    } 
    vm.exportSerial = function(){
        var obj={};
        obj = vm.dataItemPhieuXK;
        obj.objectId = vm.dataItemPhieuXK.synStockTransId;
        obj.typeData = vm.dataItemPhieuXK.synType;
        obj.shipper =vm.dataItemPhieuXK.shipperName;
        obj.getNameDVYC =   vm.dataItemPhieuXK.deptReceiveName;
        obj.namePetitioner =   vm.dataItemPhieuXK.bussinessTypeName;
        obj.reportType = "DOC";
        obj.reportName = "YeuCauXuatKho_KhongSerial";

        $http({url: RestEndpoint.BASE_SERVICE_URL + "constructionService"+"/yesSerial",
            dataType: 'json',
            method: 'POST',
            data: obj,
            headers: {
                "Content-Type": "application/json"
            },
            responseType : 'arraybuffer'// THIS IS IMPORTANT
        }).success(function(data, status, headers, config){
            if(data.error){
                toastr.error(data.error);
            } else {
                 var binarydata= new Blob([data],{type: "text/plain;charset=utf-8"});
        kendo.saveAs({dataURI: binarydata, fileName: "YeuCauCoSerial" + '.docx'});
            }

        })
            .error(function(data){
                toastr.error("Có lỗi xảy ra!");
            });
    }

    vm.doSearchDeliveryBill1 = doSearchDeliveryBill1;
     function doSearchDeliveryBill1(){
        var grid =vm.deliveryBillTable;
        if(grid){
            grid.dataSource.query({
                page: 1,
                pageSize: 10
            });
        }
    }

    vm.IdSupperVisor={};
    getSupperVisor=function(id){
         constructionService.getCatProvinCodePxk(id).then(function (data) {
            vm.IdSupperVisor = data;
        }, function (e) {
            toastr.error(gettextCatalog.getString("Lỗi"));
        });
    }
    
}