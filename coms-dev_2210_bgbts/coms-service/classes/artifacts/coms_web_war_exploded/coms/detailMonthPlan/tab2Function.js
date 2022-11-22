/**
 * Created by pm1_os36 on 2/9/2018.
 */
function initTab2Function($scope, $rootScope, $timeout, gettextCatalog,
                          kendoConfig, $kWindow, detailMonthPlanService,
                          CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm, htmlCommonService) {
    var THI_CONG = 1;
    var HO_SO_HOAN_CONG = 2;
    var LEN_DOANH_THU = 3;
    var YEU_CAU_VAT_TU = 4;
    var DONG_TIEN = 5;
    var CONG_VIEC_KHAC = 6;
    vm.sumQuantity = 0;
    vm.searchForm = {};

    vm.searchFormHSHC = {};
    vm.searchFormForceMainTain = {};
    vm.searchFormBTS = {};
    vm.searchFormCashFlow = {};
    vm.searchFormOtherJobs = {};
    vm.value = 1;
    $scope.fisrtElement = '#CC5424';
    vm.chooseTab = chooseTab;
//  hungnx 20180618 start
    vm.loadingUpdate = false;
  //hungnx 20180618 end
    function chooseTab(value) {
        vm.detailMonthPlan.listConstrTaskIdDelete = [];
//        chinhpxn20180710_start
        vm.detailMonthPlan.constructionIdLst = [];
//        chinhpxn20180710_end
        vm.value = value;
        if (vm.value == THI_CONG) {
            //vm.targetGrid.dataSource.page(1);
        } else if (vm.value == HO_SO_HOAN_CONG) {
            //vm.sourceGrid.dataSource.page(1);
        } else if (vm.value == LEN_DOANH_THU) {
            //vm.forceMaintainGrid.dataSource.page(1);
        } else if (vm.value == YEU_CAU_VAT_TU) {
            //vm.BTSGrid.dataSource.page(1);
        } else if (vm.value == DONG_TIEN) {
            //vm.CashFlow.dataSource.page(1);
        } else if (vm.value == CONG_VIEC_KHAC) {
            //vm.OtherJobs.dataSource.page(1);
        }
    }


    vm.editTab1 = function (dataItem,e) {
        var grid = vm.targetGrid;
        var row = $(e.target).closest("tr");
        vm.indexTab1 = row.index();
        var templateUrl = "coms/detailMonthPlan/popup-edit-thi-cong.html";
        var title = "Thông tin chi tiết thi công";
        var windowId = "THI_CONG";
        vm.thiCongItem = angular.copy(dataItem);
        vm.errMessageDateFrom=""
        vm.errMessageDateTo=""
        vm.constructionCode = vm.thiCongItem.constructionCode;
        vm.thiCongItem.quantity1 = vm.thiCongItem.quantity;
        CommonService.populatePopupCreate(templateUrl, title, vm.thiCongItem, vm, windowId, false, '1000', '600', "quantity");

    }

    vm.editTab2 = function (dataItem,e) {
        var grid = vm.sourceGrid;
        var row = $(e.target).closest("tr");
        vm.indexTab2 = row.index();
        var templateUrl = "coms/detailMonthPlan/popup-edit-tab2.html";
        var title = "Thông tin chi tiết hồ sơ hoàn công";
        var windowId = "HO_SO_HOAN_CONG";
        vm.popUpOpen = 'hoanCong';
        vm.hoanCongItem = angular.copy(dataItem);
        vm.errMessageDateFrom=""
        vm.errMessageDateTo=""
        vm.constructionCode = vm.hoanCongItem.constructionCode;
        //vm.catProvinceCode=vm.hoanCongItem.catProvinceCode;
        CommonService.populatePopupCreate(templateUrl, title, vm.hoanCongItem, vm, windowId, false, '1000', 'auto', "performerId");

    }

    vm.editTab3 = function (dataItem,e) {
        var grid = vm.forceMaintainGrid;
        var row = $(e.target).closest("tr");
        vm.indexTab3 = row.index();
        vm.doanhThuItem = angular.copy(dataItem);
        vm.constructionCode = vm.doanhThuItem.constructionCode
        var teamplateUrl = "coms/detailMonthPlan/popup-edit-tab3.html";
        var title = "Thông tin chi tiết lên doanh thu";
        var windowId = "LEN_DOANH_THU";
        vm.popUpOpen = 'doanhThu';
        vm.errMessageDateFrom=""
        vm.errMessageDateTo=""
        /* $("#appParamGridCatLine").data('kendoGrid').dataSource.read();
         $("#appParamGridCatLine").data('kendoGrid').refresh();*/
        CommonService.populatePopupCreate(teamplateUrl, title, vm.doanhThuItem, vm, windowId, false, '1000', 'auto', "performerId");

    }
    vm.editTab5 = function (dataItem) {
        vm.dongTienItem = dataItem;
        var teamplateUrl = "coms/detailMonthPlan/popup-edit-tab5.html";
        var title = "Thông tin chi tiết  dòng tiền";
        var windowId = "DONG_TIEN";
        vm.popUpOpen = 'dongTien';
        /* $("#appParamGridCatLine").data('kendoGrid').dataSource.read();
         $("#appParamGridCatLine").data('kendoGrid').refresh();*/
        CommonService.populatePopupCreate(teamplateUrl, title, vm.dongTienItem, vm, windowId, false, '1000', 'auto', "performerId");

    }

    vm.initAllTab = initAllTab
    function initAllTab() {
        vm.targetGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
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
                        url: Constant.BASE_SERVICE_URL + "DetailMonthPlanRsService/constructionTask/doSearch",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.searchForm.page = options.page
                        vm.searchForm.pageSize = options.pageSize
                        vm.searchForm.detailMonthPlanId = vm.detailMonthPlan.detailMonthPlanId
                        vm.searchForm.type = "1";
                        vm.detailMonthPlan.isTCImport = 0;
                        vm.detailMonthPlan.listConstrTaskIdDelete = [];
                        return JSON.stringify(vm.searchForm)


                    }
                },
                pageSize: 10
            },
            reorderable: true,
            noRecords: true,
            save: function () {
                var grid = this;
                setTimeout(function () {
                    grid.refresh();
                })
            },
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            pageable: {
                refresh: false,
                pageSize: 10,
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
                    },
                    editable: false
                },
                {
                    title: "Mã trạm",
                    field: 'catStationCode',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false

                },
                {
                    title: "Mã công trình",
                    field: 'constructionCode',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true,
                    format: "{0:n3}",
                    aggregates: ["sum"],
                },
                {
                    title: "Hạng mục",
                    field: 'workItemName',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true,
                },
                {

                    field: 'constructionId',
                    hidden: true
                },
                {

                    field: 'sourceType',
                    hidden: true
                },
                {

                    field: 'deployType',
                    hidden: true
                },
                {

                    field: 'workItemId',
                    hidden: true
                },
                {
                    title: "Hợp đồng đầu ra",
                    field: 'cntContract',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true,
                },

                {
                    title: "Giá trị",
                    field: 'quantity',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    editable: false,
                    format: "{0:n3}"
                },
                {
                    title: "Thực hiện",
                    field: 'performerName',
                    width: '10%',
                    format: "{0:n3}",
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true,
                },
                {
                    title: "Bắt đầu",
                    field: 'startDate',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
                    editable: false,
                    //template: function (dataItem) {
                    //    return kendo.toString(new Date(dataItem.startDate), "dd/MM/yyyy");
                    //}
                },
                {
                    title: "Kết thúc",
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
                    field: 'endDate',
                    editable: false,
                    //template: function (dataItem) {
                    //    return kendo.toString(new Date(dataItem.endDate), "dd/MM/yyyy");
                    //}
                },
                {
                    title: "Thao tác",
                    template: dataItem =>
                    '<div class="text-center">'
                    + '<button style=" border: none; background-color: white;" id="updateId" ng-hide="dataItem.status==0" ng-click="vm.editTab1(dataItem,$event)" class=" icon_table "' +
                    '   uib-tooltip="Sửa" translate>' +
                    '<i class="fa fa-list-alt" style="color:#e0d014" ng-hide="dataItem.status==0"   aria-hidden="true"></i>' +
                    '</button>'
                    +
//                    '<button style=" border: none; background-color: white;" id="removeId"' +
//                    'class=" icon_table" ng-click="vm.removeTargetRow($event)" uib-tooltip="Xóa" translate' +
//                    '>' +
//                    '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
//                    '</button>'
                    //tanqn start 20181108
		        	'		<button ng-show="vm.showHideButtonRemove(dataItem)" style=" border: none; background-color: white;" class=" icon_table" uib-tooltip="Khóa" translate> '+
		        	'			<i style="color:grey" class="fa fa-trash" aria-hidden="true"></i>'+
		        	'		</button> '+
		        	'		<button ng-hide="vm.showHideButtonRemove(dataItem)" style=" border: none; background-color: white;" ng-click=vm.removeTargetRow(dataItem) class=" icon_table" uib-tooltip="Xóa" translate>'+
		        	'			<i style="color:steelblue;" class="fa fa-trash" aria-hidden="true"></i> '+
		        	'		</button>'
                    + '</div>',
                    field: 'action',
                    width: '10%'

                }
            ],
        });
        vm.showHideButtonRemove = function (dataItem) {
//			
				if(dataItem.completePercent > '0' ){
				return true;
			}
			else {
				
					return false;
				 
			}
		}

        //tanqn end 20181108
        vm.BTSGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
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
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "DetailMonthPlanRsService/doSearchMaterial",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.searchFormBTS.page = options.page
                        vm.searchFormBTS.pageSize = options.pageSize
                        vm.searchFormBTS.detailMonthPlanId = vm.detailMonthPlan.detailMonthPlanId
                        vm.detailMonthPlan.isYCVTImport = 0;
                        vm.detailMonthPlan.listConstrTaskIdDelete = [];
                        return JSON.stringify(vm.searchFormBTS)


                    }
                },
                pageSize: 10
            },
            reorderable: true,
            noRecords: true,
            save: function () {
                var grid = this;
                setTimeout(function () {
                    grid.refresh();
                })
            },
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            pageable: {
                refresh: false,
                pageSize: 10,
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
                    },
                    editable: false
                },
                {
                    title: "Mã hàng hóa",
                    field: 'goodsCode',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false

                },
                {
                    title: "Tên hàng hóa",
                    field: 'goodsName',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true
                },

                {
                    title: "Đơn vị tính",
                    field: 'unitName',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false
                },
                {
                    title: "Số lượng",
                    width: '10%',
                    field: 'quantity',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    editable: false,
                },
                {
                    title: "Thao tác",
                    template: dataItem =>
                    '<div class="text-center">'
                    +
                    '<button style=" border: none; background-color: white;" id="removeId"' +
                    'class=" icon_table" ng-click="vm.removeBTSRow($event)" uib-tooltip="Xóa" translate' +
                    '>' +
                    '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
                    '</button>'
                    + '</div>',
                    width: '10%',
                    field: "action"
                }

            ]
        });

        vm.CashFlowGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
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
                        url: Constant.BASE_SERVICE_URL + "DetailMonthPlanRsService/constructionTask/doSearch",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.searchFormCashFlow.page = options.page
                        vm.searchFormCashFlow.pageSize = options.pageSize
                        vm.searchFormCashFlow.detailMonthPlanId = vm.detailMonthPlan.detailMonthPlanId
                        vm.searchFormCashFlow.type = "5";
                        vm.detailMonthPlan.isDTImport = 0;
                        vm.detailMonthPlan.listConstrTaskIdDelete = [];
                        return JSON.stringify(vm.searchFormCashFlow)


                    }
                },
                pageSize: 10
            },
            reorderable: true,
            noRecords: true,
            save: function () {
                var grid = this;
                setTimeout(function () {
                    grid.refresh();
                })
            },
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            pageable: {
                refresh: false,
                pageSize: 10,
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
                    },
                    editable: false
                },
                {
                    title: "Mã Trạm",
                    field: 'catStationCode',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false

                },
                {
                    title: "Mã công trình",
                    field: 'constructionCode',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true,
                },

                {
                    title: "Loại công trình",
                    field: 'constructionType',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false,
                },
                {
                    title: "Hợp đồng đầu ra",
                    width: '10%',
                    field: 'cntContract',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false,
                },
                {
                    title: "Tổng cộng",
                    width: '15%',
                    field: 'quantity',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    //chinhpxn 20180606 start
                    template:"#= kendo.toString(quantity*1000000, '0.00') #",
                    //chinhpxn 20180606 end
                    editable: false,
                    format: "{0:n3}"
                },
                {
                    title: "VAT",
                    width: '10%',
                    field: 'vat',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    editable: false,
                    //chinhpxn 20180606 start
                    template:"#= kendo.toString(vat*1000000, '0.00') #",
                    //chinhpxn 2018 end
                },
                {
                    title: "Thành tiền",
                    width: '10%',
                    field: '',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    editable: false,
                    template: function (dataItem) {
                    	//chinhpxn 20180606 start
                        return kendo.toString(kendo.parseFloat((dataItem.quantity*1000000 ||0) + ((dataItem.vat*1000000 ||0))), "n3");
                        //chinhpxn 20180606 end
                    }
                },
                {
                    title: "Thao tác",
                    template: dataItem =>
                    '<div class="text-center">'
                    + '<button style=" border: none; background-color: white;" id="updateId" ng-hide="dataItem.status==0" ng-click="vm.editTab5(dataItem)" class=" icon_table "' +
                    '   uib-tooltip="Sửa" translate>' +
                    '<i class="fa fa-list-alt" style="color:#e0d014"  ng-hide="dataItem.status==0"   aria-hidden="true"></i>' +
                    '</button>'
                    +
                    '<button style=" border: none; background-color: white;" id="removeId"' +
                    'class=" icon_table" ng-click="vm.removeCashFlowRow($event)" uib-tooltip="Xóa" translate' +
                    '>' +
                    '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
                    '</button>'
                    + '</div>',
                    width: '10%',
                    field: "action"
                }

            ],

        });

        vm.fillDataOtherJobsGrid = function fillDataOtherJobsGrid() {
            vm.OtherJobsGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
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
                            url: Constant.BASE_SERVICE_URL + "DetailMonthPlanRsService/constructionTask/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchFormOtherJobs.page = options.page
                            vm.searchFormOtherJobs.pageSize = options.pageSize
                            vm.searchFormOtherJobs.detailMonthPlanId = vm.detailMonthPlan.detailMonthPlanId
                            vm.searchFormOtherJobs.type = "6";
                            vm.detailMonthPlan.isCVKImport = 0;
                            vm.detailMonthPlan.listConstrTaskIdDelete = [];
                            return JSON.stringify(vm.searchFormOtherJobs)


                        }
                    },
                    pageSize: 10
                },
                reorderable: true,
                noRecords: true,
                save: function () {
                    var grid = this;
                    setTimeout(function () {
                        grid.refresh();
                    })
                },
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: false,
                    pageSize: 10,
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
                        },
                        editable: false
                    },
                    {
                        title: "Nội dung công việc",
                        field: 'taskName',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        editable: false

                    },
                    {
                        title: "Thực hiện",
                        field: 'performerName',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        editable: false

                    },
                    {
                        title: "Bắt đầu",
                        field: 'startDate',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        //template: function (dataItem) {
                        //    return kendo.toString(new Date(dataItem.startDate), "dd/MM/yyyy");
                        //},
                        editable: false
                    },
                    {
                        title: "Kết thúc",
                        field: 'endDate',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        //template: function (dataItem) {
                        //    return kendo.toString(new Date(dataItem.endDate), "dd/MM/yyyy");
                        //},
                        editable: false
                    },
                    {
                        title: "Ghi chú",
                        field: 'description',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        editable: false
                    },
                    {
                        title: "Thao tác",
                        template: dataItem =>
                        '<div class="text-center">'
                        +
//                        '<button style=" border: none; background-color: white;" id="removeId"' +
//                        'class=" icon_table" ng-click="vm.removeOtherJobsRow($event)" uib-tooltip="Xóa" translate' +
//                        '>' +
//                        '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
//                        '</button>'
                        '		<button ng-show="vm.showHideButtonRemove(dataItem)" style=" border: none; background-color: white;" class=" icon_table" uib-tooltip="Khóa" translate> '+
    		        	'			<i style="color:grey" class="fa fa-trash" aria-hidden="true"></i>'+
    		        	'		</button> '+
    		        	'		<button ng-hide="vm.showHideButtonRemove(dataItem)" style=" border: none; background-color: white;" ng-click=vm.removeOtherJobsRow(dataItem) class=" icon_table" uib-tooltip="Xóa" translate>'+
    		        	'			<i style="color:steelblue;" class="fa fa-trash" aria-hidden="true"></i> '+
    		        	'		</button>'
                        + '</div>',
                        width: '10%',
                        field: "action"
                    }

                ],
            });
        }


        vm.forceMaintainGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
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
                        url: Constant.BASE_SERVICE_URL + "DetailMonthPlanRsService/constructionTask/doSearch",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.searchFormForceMainTain.page = options.page
                        vm.searchFormForceMainTain.pageSize = options.pageSize
                        vm.searchFormForceMainTain.detailMonthPlanId = vm.detailMonthPlan.detailMonthPlanId
                        vm.searchFormForceMainTain.type = "3";
                        vm.detailMonthPlan.isLDTImport = 0;
                        vm.detailMonthPlan.listConstrTaskIdDelete = [];
                        return JSON.stringify(vm.searchFormForceMainTain)


                    }
                },
                pageSize: 10
            },
            reorderable: true,
            noRecords: true,
            save: function () {
                var grid = this;
                setTimeout(function () {
                    grid.refresh();
                })
            },
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            pageable: {
                refresh: false,
                pageSize: 10,
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
                    },
                    editable: false
                },
                {
                    title: "Tên công việc",
                    field: 'taskName',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false

                },
                {
                    title: "Mã trạm",
                    field: 'catStationCode',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false

                },
                {
                    title: "Mã công trình",
                    field: 'constructionCode',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false

                },
                {
                    field: 'provinceName',
                    hidden: true
                },
                {
                    field: 'startDate',
                    hidden: true
                },
                {
                    field: 'constructionId',
                    hidden: true
                },
                {
                    title: "Loại công trình",
                    field: 'constructionType',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false
                },
                {
                    title: "Hợp đồng đầu ra",
                    field: 'cntContract',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false
                },
                {
                    title: "Giá trị",
                    field: 'quantity',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    //chinhpxn 20180606 start
//                    template:"#= kendo.toString(quantity*1000000, '0.00') #",
                    //chinhpxn 20180606 end
                    editable: false,
                    format: "{0:n3}",
                },
				{
                    title: "Thực hiện",
                    field: 'performerName',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true,
                },
                {
                    title: "Thời gian bắt đầu",
                    width: '10%',
                    field: 'startDate',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    }
//                    ,
//                    template: function (dataItem) {
//                        if (dataItem.startDate != null && dataItem.startDate != '')
//                            return kendo.toString(new Date(dataItem.startDate), "dd/MM/yyyy");
//                        else return '';
//                    }
                },
                {
                    title: "Thời gian hoàn thành",
                    field: 'endDate',
                    width: '10%',

                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
                    editable: true,
                    //template: function (dataItem) {
                    //    return kendo.toString(new Date(dataItem.endDate), "dd/MM/yyyy");
                    //}
                },
                {
                    title: "Thao tác",
                    template: dataItem =>
                    '<div class="text-center">'
                    + '<button style=" border: none; background-color: white;" id="updateId" ng-hide="dataItem.status==0" ng-click="vm.editTab3(dataItem,$event)" class=" icon_table "' +
                    '   uib-tooltip="Sửa" translate>' +
                    '<i class="fa fa-list-alt" style="color:#e0d014" ng-hide="dataItem.status==0"   aria-hidden="true"></i>' +
                    '</button>'

                    +
//                    '<button style=" border: none; background-color: white;" id="removeId"' +
//                    'class=" icon_table" ng-click="vm.removeForceMaintainRow($event)" uib-tooltip="Xóa" translate' +
//                    '>' +
//                    '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
//                    '</button>'
                    '		<button ng-show="vm.showHideButtonRemove(dataItem)" style=" border: none; background-color: white;" class=" icon_table" uib-tooltip="Khóa" translate> '+
		        	'			<i style="color:grey" class="fa fa-trash" aria-hidden="true"></i>'+
		        	'		</button> '+
		        	'		<button ng-hide="vm.showHideButtonRemove(dataItem)" style=" border: none; background-color: white;" ng-click=vm.removeForceMaintainRow(dataItem) class=" icon_table" uib-tooltip="Xóa" translate>'+
		        	'			<i style="color:steelblue;" class="fa fa-trash" aria-hidden="true"></i> '+
		        	'		</button>'
                    + '</div>',
                    width: '10%',
                    field: "action"
                }

            ],

        });


        vm.sourceGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
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
                        url: Constant.BASE_SERVICE_URL + "DetailMonthPlanRsService/constructionTask/doSearch",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.searchFormHSHC.page = options.page
                        vm.searchFormHSHC.pageSize = options.pageSize
                        vm.searchFormHSHC.detailMonthPlanId = vm.detailMonthPlan.detailMonthPlanId
                        vm.searchFormHSHC.type = "2";
                        vm.detailMonthPlan.isHSHCImport = 0;
                        vm.detailMonthPlan.listConstrTaskIdDelete = [];
                        return JSON.stringify(vm.searchFormHSHC)


                    }
                },
                pageSize: 10
            },
            reorderable: true,
            noRecords: true,
            save: function () {
                var grid = this;
                setTimeout(function () {
                    grid.refresh();
                })
            },
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            pageable: {
                refresh: false,
                pageSize: 10,
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
                    },
                    editable: false
                },
                {
                    title: "Mã trạm",
                    field: 'catStationCode',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false

                },
                {
                    title: "Mã công trình",
                    field: 'constructionCode',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true,
                },

                {
                    title: "Loại công trình",
                    field: 'constructionType',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true,
                },
				//hoanm1_20181229_start
				{
                    title: "Hạng mục hoàn công",
                    field: 'workItemNameHSHC',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true,
                },
				//hoanm1_20181229_end
                {
                    title: "Hợp đồng đầu ra",
                    width: '10%',
                    field: 'cntContract',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true
                },
                {
                    title: "Tổng giá trị",
                    width: '10%',
                    field: 'quantity',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    editable: true,
                    format: "{0:n3}"
                },
                {
                    field: 'constructionId',
                    hidden: true
                },
                {
                    field: 'provinceName',
                    hidden: true
                },
                {
                    field: 'startDate',
                    hidden: true
                },
                {
                    title: "Thực hiện",
                    width: '10%',
                    field: 'performerName',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false,
                },
                {
                    title: "Thời gian bắt đầu",
                    width: '10%',
                    field: 'startDate',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    }
//                    ,
//                    template: function (dataItem) {
//                        if (dataItem.startDate != null && dataItem.startDate != '')
//                            return kendo.toString(new Date(dataItem.startDate), "dd/MM/yyyy");
//                        else return '';
//                    }
                },
                {
                    title: "Thời gian hoàn thành",
                    field: 'endDate',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
                    editable: true,
                    //template: function (dataItem) {
                    //    return kendo.toString(new Date(dataItem.endDate), "dd/MM/yyyy");
                    //}
                },
                {
                    title: "Thao tác",
                    template: dataItem =>
                    '<div class="text-center">'

                    + '<button style=" border: none; background-color: white;" id="updateId" ng-hide="dataItem.status==0" ng-click="vm.editTab2(dataItem,$event)" class=" icon_table "' +

                    '   uib-tooltip="Sửa" translate>' +
                    '<i class="fa fa-list-alt" style="color:#e0d014" ng-hide="dataItem.status==0"   aria-hidden="true"></i>' +
                    '</button>'
                    +
//                    '<button style=" border: none; background-color: white;" id="removeId"' +
//                    'class=" icon_table" ng-click="vm.removeSourceRow($event)" uib-tooltip="Xóa" translate' +
//                    '>' +
//                    '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
//                    '</button>'
                    '		<button ng-show="vm.showHideButtonRemove(dataItem)" style=" border: none; background-color: white;" class=" icon_table" uib-tooltip="Khóa" translate> '+
		        	'			<i style="color:grey" class="fa fa-trash" aria-hidden="true"></i>'+
		        	'		</button> '+
		        	'		<button ng-hide="vm.showHideButtonRemove(dataItem)" style=" border: none; background-color: white;" ng-click=vm.removeSourceRow(dataItem) class=" icon_table" uib-tooltip="Xóa" translate>'+
		        	'			<i style="color:steelblue;" class="fa fa-trash" aria-hidden="true"></i> '+
		        	'		</button>'
                    + '</div>',
                    width: '10%',
                    field: "action"
                }

            ],
        });
    }
vm.removeBTSRow = removeBTSRow;
function removeBTSRow(e) {
    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
        toastr.success("Xóa thành công!");
        var grid = vm.BTSGrid;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
        vm.detailMonthPlan.listConstrTaskIdDelete.push(dataItem.dmpnOrderId);
        grid.removeRow(dataItem); //just gives alert message
        grid.dataSource.remove(dataItem); //removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
    })
}
vm.removeOtherJobsRow = removeOtherJobsRow;
function removeOtherJobsRow(data){
//function removeOtherJobsRow(e) {
//    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
//        toastr.success("Xóa thành công!");
//        var grid = vm.OtherJobs;
//        var row = $(e.target).closest("tr");
//        var dataItem = grid.dataItem(row);
//        vm.detailMonthPlan.listConstrTaskIdDelete.push(dataItem.constructionTaskId);
//        grid.removeRow(dataItem); //just gives alert message
//        grid.dataSource.remove(dataItem); //removes it actually from the grid
//        grid.dataSource.sync();
//        grid.refresh();
//    })
confirm('Bạn thật sự muốn xóa bản ghi đã chọn?',function (){
	detailMonthPlanService.removeRow(data).then(
		function(d) {
			toastr.success("Xóa thành công!");
			$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
    		var sizePage = $("#OtherJobs").data("kendoGrid").dataSource.total();
			var pageSize = $("#OtherJobs").data("kendoGrid").dataSource.pageSize();
			if(sizePage % pageSize == 1){
				var currentPage = $("#OtherJobs").data("kendoGrid").dataSource.page();
		        if (currentPage > 1) {
		            $("#OtherJobs").data("kendoGrid").dataSource.page(currentPage - 1);
		        }
			}
			$('#OtherJobs').data('kendoGrid').dataSource.read();
			$('#OtherJobs').data('kendoGrid').refresh();
		}, function(errResponse) {
			toastr.error("Lỗi không xóa được!");
		});
	} 
)
}
vm.removeCashFlowRow = removeCashFlowRow;
function removeCashFlowRow(e) {
    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
        toastr.success("Xóa thành công!");
        var grid = vm.CashFlow;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
        vm.detailMonthPlan.listConstrTaskIdDelete.push(dataItem.constructionTaskId);
        grid.removeRow(dataItem); //just gives alert message
        grid.dataSource.remove(dataItem); //removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
    })
}
vm.removeForceMaintainRow = removeForceMaintainRow;
function removeForceMaintainRow(data){
//function removeForceMaintainRow(e) {
//    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
//        toastr.success("Xóa thành công!");
//        var grid = vm.forceMaintainGrid;
//        var row = $(e.target).closest("tr");
//        var dataItem = grid.dataItem(row);
//        vm.detailMonthPlan.listConstrTaskIdDelete.push(dataItem.constructionTaskId);
//        grid.removeRow(dataItem); //just gives alert message
//        grid.dataSource.remove(dataItem); //removes it actually from the grid
//        grid.dataSource.sync();
//        grid.refresh();
//    })
confirm('Bạn thật sự muốn xóa bản ghi đã chọn?',function (){
	detailMonthPlanService.removeRow(data).then(
		function(d) {
			toastr.success("Xóa thành công!");
			$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
    		var sizePage = $("#forceMaintainGrid").data("kendoGrid").dataSource.total();
			var pageSize = $("#forceMaintainGrid").data("kendoGrid").dataSource.pageSize();
			if(sizePage % pageSize == 1){
				var currentPage = $("#forceMaintainGrid").data("kendoGrid").dataSource.page();
		        if (currentPage > 1) {
		            $("#forceMaintainGrid").data("kendoGrid").dataSource.page(currentPage - 1);
		        }
			}
			$('#forceMaintainGrid').data('kendoGrid').dataSource.read();
			$('#forceMaintainGrid').data('kendoGrid').refresh();
		}, function(errResponse) {
			toastr.error("Lỗi không xóa được!");
		});
	} 
)
}
//vm.removeTargetRow = removeTargetRow;
//function removeTargetRow(e) {
//    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
//        toastr.success("Xóa thành công!");
//        var grid = vm.targetGrid;
//        var row = $(e.target).closest("tr");
//        var dataItem = grid.dataItem(row);
//        vm.detailMonthPlan.listConstrTaskIdDelete.push(dataItem.constructionTaskId);
//        grid.removeRow(dataItem); //just gives alert message
//        grid.dataSource.remove(dataItem); //removes it actually from the grid
//        grid.dataSource.sync();
//        grid.refresh();
//    })
//}
//tanqn start 20181108
vm.removeTargetRow=removeTargetRow;
function removeTargetRow(data){
	confirm('Bạn thật sự muốn xóa bản ghi đã chọn?',function (){
		detailMonthPlanService.removeRow(data).then(
			function(d) {
				toastr.success("Xóa thành công!");
				$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        		var sizePage = $("#targetGrid").data("kendoGrid").dataSource.total();
				var pageSize = $("#targetGrid").data("kendoGrid").dataSource.pageSize();
				if(sizePage % pageSize == 1){
					var currentPage = $("#targetGrid").data("kendoGrid").dataSource.page();
			        if (currentPage > 1) {
			            $("#targetGrid").data("kendoGrid").dataSource.page(currentPage - 1);
			        }
				}
				$('#targetGrid').data('kendoGrid').dataSource.read();
				$('#targetGrid').data('kendoGrid').refresh();
			}, function(errResponse) {
				toastr.error("Lỗi không xóa được!");
			});
		} 
	)
}
//tanqn end 20181108
vm.removeSourceRow = removeSourceRow;
function removeSourceRow(data){
//function removeSourceRow(e) {
//    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
//        toastr.success("Xóa thành công!");
//        var grid = vm.sourceGrid;
//        var row = $(e.target).closest("tr");
//        var dataItem = grid.dataItem(row);
//        vm.detailMonthPlan.listConstrTaskIdDelete.push(dataItem.constructionTaskId);
//        grid.removeRow(dataItem); //just gives alert message
//        grid.dataSource.remove(dataItem); //removes it actually from the grid
//        grid.dataSource.sync();
//        grid.refresh();
//    })
	confirm('Bạn thật sự muốn xóa bản ghi đã chọn?',function (){
		detailMonthPlanService.removeRow(data).then(
			function(d) {
				toastr.success("Xóa thành công!");
				$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        		var sizePage = $("#sourceGrid").data("kendoGrid").dataSource.total();
				var pageSize = $("#sourceGrid").data("kendoGrid").dataSource.pageSize();
				if(sizePage % pageSize == 1){
					var currentPage = $("#sourceGrid").data("kendoGrid").dataSource.page();
			        if (currentPage > 1) {
			            $("#sourceGrid").data("kendoGrid").dataSource.page(currentPage - 1);
			        }
				}
				$('#sourceGrid').data('kendoGrid').dataSource.read();
				$('#sourceGrid').data('kendoGrid').refresh();
			}, function(errResponse) {
				toastr.error("Lỗi không xóa được!");
			});
		} 
	)
}
vm.removeOtherRow = removeOtherRow;
function removeOtherRow(e) {
    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
        toastr.success("Xóa thành công!");
        var grid = vm.OtherJobs;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
        vm.detailMonthPlan.listConstrTaskIdDelete.push(dataItem.constructionTaskId);
        grid.removeRow(dataItem); //just gives alert message
        grid.dataSource.remove(dataItem); //removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
    })
}
vm.openDepartmentChoose = openDepartmentChoose

function openDepartmentChoose(popUp) {
    vm.obj = {};
    vm.departmentpopUp = popUp;
    var templateUrl = 'coms/popup/findDepartmentPopUp.html';
    var title = gettextCatalog.getString("Tìm kiếm đơn vị");
    CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
}


    vm.removeBTSRow = removeBTSRow;
    function removeBTSRow(e) {
        confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function () {
            toastr.success("Xóa thành công!");
            var grid = vm.BTSGrid;
            var row = $(e.target).closest("tr");
            var dataItem = grid.dataItem(row);
            vm.detailMonthPlan.listConstrTaskIdDelete.push(dataItem.dmpnOrderId);
            grid.removeRow(dataItem); //just gives alert message
            grid.dataSource.remove(dataItem); //removes it actually from the grid
            grid.dataSource.sync();
            grid.refresh();
        })
    }

//    vm.removeOtherJobsRow = removeOtherJobsRow;
//    function removeOtherJobsRow(e) {
//        confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function () {
//            toastr.success("Xóa thành công!");
//            var grid = vm.OtherJobs;
//            var row = $(e.target).closest("tr");
//            var dataItem = grid.dataItem(row);
//            vm.detailMonthPlan.listConstrTaskIdDelete.push(dataItem.constructionTaskId);
//            grid.removeRow(dataItem); //just gives alert message
//            grid.dataSource.remove(dataItem); //removes it actually from the grid
//            grid.dataSource.sync();
//            grid.refresh();
//        })
//    }

    vm.removeCashFlowRow = removeCashFlowRow;
    function removeCashFlowRow(e) {
        confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function () {
            toastr.success("Xóa thành công!");
            var grid = vm.CashFlow;
            var row = $(e.target).closest("tr");
            var dataItem = grid.dataItem(row);
            vm.detailMonthPlan.listConstrTaskIdDelete.push(dataItem.constructionTaskId);
//            chinhpxn20180710_start
            vm.detailMonthPlan.constructionIdLst.push(dataItem.constructionId);
//            chinhpxn20180710_end
            grid.removeRow(dataItem); //just gives alert message
            grid.dataSource.remove(dataItem); //removes it actually from the grid
            grid.dataSource.sync();
            grid.refresh();
        })
    }

//    vm.removeForceMaintainRow = removeForceMaintainRow;
//    function removeForceMaintainRow(e) {
//        confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function () {
//            toastr.success("Xóa thành công!");
//            var grid = vm.forceMaintainGrid;
//            var row = $(e.target).closest("tr");
//            var dataItem = grid.dataItem(row);
//            vm.detailMonthPlan.listConstrTaskIdDelete.push(dataItem.constructionTaskId);
//            grid.removeRow(dataItem); //just gives alert message
//            grid.dataSource.remove(dataItem); //removes it actually from the grid
//            grid.dataSource.sync();
//            grid.refresh();
//        })
//    }

//    vm.removeTargetRow = removeTargetRow;
//    function removeTargetRow(e) {
//        confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function () {
//            toastr.success("Xóa thành công!");
//            var grid = vm.targetGrid;
//            var row = $(e.target).closest("tr");
//            var dataItem = grid.dataItem(row);
//            vm.detailMonthPlan.listConstrTaskIdDelete.push(dataItem.constructionTaskId);
//            grid.removeRow(dataItem); //just gives alert message
//            grid.dataSource.remove(dataItem); //removes it actually from the grid
//            grid.dataSource.sync();
//            grid.refresh();
//        })
//    }

//    vm.removeSourceRow = removeSourceRow;
//    function removeSourceRow(e) {
//        confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function () {
//            toastr.success("Xóa thành công!");
//            var grid = vm.sourceGrid;
//            var row = $(e.target).closest("tr");
//            var dataItem = grid.dataItem(row);
//            vm.detailMonthPlan.listConstrTaskIdDelete.push(dataItem.constructionTaskId);
//            grid.removeRow(dataItem); //just gives alert message
//            grid.dataSource.remove(dataItem); //removes it actually from the grid
//            grid.dataSource.sync();
//            grid.refresh();
//        })
//    }

    vm.removeOtherRow = removeOtherRow;
    function removeOtherRow(e) {
        confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function () {
            toastr.success("Xóa thành công!");
            var grid = vm.OtherJobs;
            var row = $(e.target).closest("tr");
            var dataItem = grid.dataItem(row);
            vm.detailMonthPlan.listConstrTaskIdDelete.push(dataItem.constructionTaskId);
            grid.removeRow(dataItem); //just gives alert message
            grid.dataSource.remove(dataItem); //removes it actually from the grid
            grid.dataSource.sync();
            grid.refresh();
        })
    }

    vm.openDepartmentChoose = openDepartmentChoose

    function openDepartmentChoose(popUp) {
        vm.obj = {};
        vm.departmentpopUp = popUp;
        var templateUrl = 'coms/popup/findDepartmentPopUp.html';
        var title = gettextCatalog.getString("Tìm kiếm đơn vị");
        CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
    }

    vm.onSave = onSave;
    function onSave(data) {
        if (vm.departmentpopUp === 'dept') {
            vm.selectedDeptChoose = true;
            vm.detailMonthPlan.sysGroupName = data.text;
            vm.detailMonthPlan.sysGroupId = data.id;
            vm.sysGroupCode = data.code;
            vm.changeCodeName();
        }
        if (vm.departmentpopUp === 'dept1') {
            vm.detailMonthPlanSearch.sysGroupName = data.text;
            vm.detailMonthPlanSearch.sysGroupId = data.id;
        }
    }

// clear data
    vm.changeDataDept = changeDataDept
    function changeDataDept(id) {
        switch (id) {
            case 'dept':
            {
                if (processSearch(id, vm.selectedDeptChoose)) {
                    vm.detailMonthPlan.sysGroupId = null;
                    vm.detailMonthPlan.sysGroupName = null;
                    vm.selectedDeptChoose = false;
                }
                break;
            }
        }
    }

    vm.cancelInputDept = function (param) {
        if (param == 'dept') {
            vm.detailMonthPlan.sysGroupId = null;
            vm.detailMonthPlan.sysGroupName = null;
        }
    }
// 8.2 Search SysGroup
    vm.selectedDeptChoose = false;
    vm.deptOptions1 = {
        dataTextField: "text",
        dataValueField: "id",
        select: function (e) {
            vm.selectedDeptChoose = true;
            var dataItem = this.dataItem(e.item.index());
            vm.detailMonthPlan.sysGroupName = dataItem.text;
            vm.detailMonthPlan.sysGroupId = dataItem.id;
            vm.sysGroupCode = dataItem.code;
            vm.changeCodeName();
        },
        pageSize: 10,
        open: function (e) {
            vm.selectedDeptChoose = false;
        },
        dataSource: {
            serverFiltering: true,
            transport: {
                read: function (options) {
                    vm.selectedDeptChoose = false;
                    return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({
                        name: vm.detailMonthPlan.sysGroupName,
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
                vm.detailMonthPlan.sysGroupName = null;// thành name
                vm.detailMonthPlan.sysGroupId = null;
            }
        },
        ignoreCase: false
    }
    vm.cancelImport = cancelImport;
    function cancelImport() {
        CommonService.dismissPopup1();
    }

    vm.importExcelTab2 = importExcelTab2;
    function importExcelTab2() {
        var teamplateUrl = "coms/detailMonthPlan/import-popup.html";
        var title = "Import từ file excel";
        var windowId = "DETAIL_MONTH_PLAN";
        CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '700', 'auto', "code");
    }

    vm.addTargetTemp = addTargetTemp;
    function addTargetTemp() {
        if (validateTargetTemp()) {
            var grid = vm.targetGrid;
            if (grid) {
                var detailList = grid.dataSource._data;
                var valid = true;
                angular.forEach(detailList, function (dataItem) {
                    if (vm.detailMonthPlan.sysGroupId == dataItem.sysGroupId) {
                        valid = false;
                    }
                })
                if (valid) {
                    grid.dataSource.add(vm.detailMonthPlan);
                    grid.dataSource.sync();
                    grid.refresh();
                } else {
                    toastr.warning("Đơn vị " + vm.detailMonthPlan.sysGroupName + " đã tồn tại!");
                }
            }
        }
    }

    vm.addSourceTemp = addSourceTemp;
    function addSourceTemp() {
        if (validateSourceTemp()) {
            var grid = vm.sourceGrid;
            if (grid) {
                var detailList = grid.dataSource._data;
                var valid = true;
                angular.forEach(detailList, function (dataItem) {
                    if (vm.detailMonthPlan.sysGroupId == dataItem.sysGroupId) {
                        valid = false;
                    }
                })
                if (valid) {
                    grid.dataSource.add(vm.detailMonthPlan);
                    grid.dataSource.sync();
                    grid.refresh();
                } else {
                    toastr.warning("Đơn vị " + vm.detailMonthPlan.sysGroupName + " đã tồn tại!");
                }
            }
        }
    }

    vm.addForceMaintainTemp = addForceMaintainTemp;
    function addForceMaintainTemp() {
        if (validateMaintainTemp()) {
            var grid = vm.forceMaintainGrid;
            if (grid) {
                var detailList = grid.dataSource._data;
                var valid = true;
                angular.forEach(detailList, function (dataItem) {
                    if (vm.detailMonthPlan.sysGroupId == dataItem.sysGroupId) {
                        valid = false;
                    }
                })
                if (valid) {
                    //vm.detailMonthPlan.sumKht= Number(vm.detailMonthPlan.installPlan || 0)+Number(vm.detailMonthPlan.replacePlan||0)
                    grid.dataSource.add(vm.detailMonthPlan);
                    grid.dataSource.sync();
                    grid.refresh();
                } else {
                    toastr.warning("Đơn vị " + vm.detailMonthPlan.sysGroupName + " đã tồn tại!");
                }
            }
        }
    }

    function validateMaintainTemp() {
        if (vm.detailMonthPlan.sysGroupId == undefined) {
            toastr.warning("Bạn phải chọn đơn vị!");
            return false;
        }
        if (vm.detailMonthPlan.buildPlan == undefined || vm.detailMonthPlan.buildPlan == '') {
            toastr.warning("Bạn phải nhập xây dựng!");
            return false;
        }
        if (vm.detailMonthPlan.installPlan == undefined || vm.detailMonthPlan.installPlan == '') {
            toastr.warning("Bạn phải nhập KH lắp cột bao");
            return false;
        }
        if (vm.detailMonthPlan.replacePlan == undefined || vm.detailMonthPlan.buildPlan == '') {
            toastr.warning("Bạn phải nhập KH thay thân cột");
            return false;
        }
        if (vm.detailMonthPlan.teamBuildRequire == undefined || vm.detailMonthPlan.teamBuildRequire == '') {
            toastr.warning("Bạn phải nhập số đội cần  XD");
            return false;
        }
        if (vm.detailMonthPlan.teamBuildAvaiable == undefined || vm.detailMonthPlan.teamBuildAvaiable == '') {
            toastr.warning("Bạn phải nhập số đội đang XD");
            return false;
        }
        if (vm.detailMonthPlan.teamInstallRequire == undefined || vm.detailMonthPlan.teamInstallRequire == '') {
            toastr.warning("Bạn phải nhập số đội cần lắp dựng");
            return false;
        }
        if (vm.detailMonthPlan.teamInstallAvaiable == undefined || vm.detailMonthPlan.teamInstallAvaiable == '') {
            toastr.warning("Bạn phải nhập số đội đang lắp dựng");
            return false;
        }
        return true;
    }

    function validateTargetTemp() {
        if (vm.detailMonthPlan.sysGroupId == undefined) {
            toastr.warning("Bạn phải chọn đơn vị!");
            return false;
        }
        if (vm.detailMonthPlan.quantity == undefined) {
            toastr.warning("Bạn phải nhập sản lượng!");
            return false;
        }
        if (vm.detailMonthPlan.complete == undefined) {
            toastr.warning("Bạn phải nhập HSHC");
            return false;
        }
        if (vm.detailMonthPlan.revenue == undefined) {
            toastr.warning("Bạn phải nhập doanh thu");
            return false;
        }
        return true;
    }

    function validateSourceTemp() {
        if (vm.detailMonthPlan.sysGroupId == undefined) {
            toastr.warning("Bạn phải chọn đơn vị!");
            return false;
        }
        if (vm.detailMonthPlan.source == undefined) {
            toastr.warning("Bạn phải nhập nguồn việc!");
            return false;
        }
        return true;
    }

    vm.getExcelTemplate = function () {
        var obj = {};
        if (vm.value == THI_CONG) {
            obj.request = 'exportTemplateListTC';
        } else if (vm.value == HO_SO_HOAN_CONG) {
            obj.request = 'exportTemplateListHSHC';
        } else if (vm.value == LEN_DOANH_THU) {
            obj.request = 'exportTemplateListLDT';
        } else if (vm.value == YEU_CAU_VAT_TU) {
            obj.request = 'exportTemplateListYCVT';
        } else if (vm.value == DONG_TIEN) {
            obj.request = 'exportTemplateListDT';
        } else if (vm.value == CONG_VIEC_KHAC) {
            obj.request = 'exportTemplateListCVK';
        }
        obj.sysGroupId = vm.detailMonthPlan.sysGroupId;
        detailMonthPlanService.downloadTemplate(obj).then(function (d) {

            data = d.plain();
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        }).catch(function (e) {
            //toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
            return;
        });
    }
    vm.saveImportFile = saveImportFile;
    function saveImportFile() {
        if ($("#fileImportDetailMonth")[0].files[0] == null) {
            toastr.warning("Bạn chưa chọn file để import");
            return;
        }
        if ($("#fileImportDetailMonth")[0].files[0].name.split('.').pop() != 'xls' && $("#fileImportDetailMonth")[0].files[0].name.split('.').pop() != 'xlsx') {
            toastr.warning("Sai định dạng file");
            return;
        }
        var formData = new FormData();
        formData.append('multipartFile', $('#fileImportDetailMonth')[0].files[0]);
        vm.detailMonthPlan.listConstrTaskIdDelete = [];
        if (vm.value == THI_CONG) {
            importTargetFile(formData);
        } else if (vm.value == HO_SO_HOAN_CONG) {
            importHSHCFile(formData);
        } else if (vm.value == LEN_DOANH_THU) {
            importLDTFile(formData);
        } else if (vm.value == YEU_CAU_VAT_TU) {
            importYCVTFile(formData);
        } else if (vm.value == DONG_TIEN) {
            importDTFile(formData);
        } else if (vm.value == CONG_VIEC_KHAC) {
            importCVKFile(formData);
        }
    }

    //Quangnd - start

    function getDataGrid(id) {
        var grid = $(id).data("kendoGrid");
        if (grid) {
            return grid.dataSource._data;
        }
        return null;
    }

    function fillDataGrid(data, id) {
        var grid = $(id).data("kendoGrid");
        if (grid) {
            grid.dataSource.data(data);
            grid.refresh();
        }
    }

    vm.updateListTC = function () {
    //  hungnx 20180618 start
        vm.loadingUpdate = true;
      //hungnx 20180618 end
        vm.detailMonthPlan.listTC = vm.targetGrid.dataSource._data;
        detailMonthPlanService.updateListTC(vm.detailMonthPlan).then(function (response) {
            toastr.success("Ghi lại thành công!");
        //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
        }, function (error) {
            toastr.error("Có lỗi xảy ra!");
        //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
        });
    }
    vm.updateListHSHC = function () {
        //  hungnx 20180618 start
        vm.loadingUpdate = true;
      //hungnx 20180618 end
        vm.detailMonthPlan.listHSHC = getDataGrid("#sourceGrid");
        detailMonthPlanService.updateListHSHC(vm.detailMonthPlan).then(function (response) {
            toastr.success("Ghi lại thành công!");
            //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
        }, function (error) {
            toastr.error("Có lỗi xảy ra!");
            //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
        });
    }
    vm.updateListLDT = function () {
        //  hungnx 20180618 start
        vm.loadingUpdate = true;
      //hungnx 20180618 end
        vm.detailMonthPlan.listLDT = getDataGrid("#forceMaintainGrid");
        detailMonthPlanService.updateListLDT(vm.detailMonthPlan).then(function (response) {
                toastr.success("Ghi lại thành công!");
                //  hungnx 20180618 start
                vm.loadingUpdate = false;
              //hungnx 20180618 end
            }, function (error) {
                toastr.error("Có lỗi xảy ra!");
                //  hungnx 20180618 start
                vm.loadingUpdate = false;
              //hungnx 20180618 end
            }
        );
    }
    vm.updateListDT = function () {
        //  hungnx 20180618 start
        vm.loadingUpdate = true;
      //hungnx 20180618 end
        vm.detailMonthPlan.listDT = getDataGrid("#CashFlow");
        detailMonthPlanService.updateListDT(vm.detailMonthPlan).then(function (response) {
            toastr.success("Ghi lại thành công!");
            $('#CashFlow').data("kendoGrid").dataSource.page(1);
            //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
        });
    }
    vm.updateListCVK = function () {
        //  hungnx 20180618 start
        vm.loadingUpdate = true;
      //hungnx 20180618 end
        vm.detailMonthPlan.listCVK = getDataGrid("#OtherJobs");
        detailMonthPlanService.updateListCVK(vm.detailMonthPlan).then(function (response) {
            toastr.success("Ghi lại thành công!");
            //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
        }, function (error) {
            toastr.error("Có lỗi xảy ra!");
            //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
        });
    }

    vm.updateListDmpnOrder = function () {
        //  hungnx 20180618 start
        vm.loadingUpdate = true;
      //hungnx 20180618 end
        vm.detailMonthPlan.listDmpnOrder = getDataGrid("#BTSGrid");
        detailMonthPlanService.updateListDmpnOrder(vm.detailMonthPlan).then(function (response) {
            toastr.success("Ghi lại thành công!");
            //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
        }, function (eror) {
            toastr.error("Có lỗi xảy ra!");
            //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
        });
    }


    //end
    function importTargetFile(formData) {
    //  hungnx 20180618 start
    	$('#savepopupMonth').prop('disabled', true);
    	htmlCommonService.showLoading('#loadingIpMonth');
    //  hungnx 20180618 end
        return $.ajax({
			//hoanm1_20190829_start
            url: Constant.BASE_SERVICE_URL + "DetailMonthPlanRsService/importTC?sysGroupId=" + vm.detailMonthPlan.sysGroupId,
			//hoanm1_20190829_end
            type: "POST",
            data: formData,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                if (data.length === 0) {
                    toastr.warning("File imp" +
                    "ort không có dữ liệu");
                    return;
                } else if (data[data.length - 1].errorFilePath != null) {
                    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                    toastr.warning("File import không hợp lệ");
                    return;
                }
                else {
                    toastr.success("Import dữ liệu thành công");

                    var grid = vm.targetGrid;
                    if (grid != undefined) {
                        grid.dataSource.data(data);
                        grid.refresh();
                        vm.detailMonthPlan.isTCImport = 1;
                    }
                }
                vm.cancelImport();
            }
        }).always(function() {
//          hungnx 20180618 start
        	$('#savepopupMonth').prop('disabled', false);
        	htmlCommonService.hideLoading('#loadingIpMonth');
//          hungnx 20180618 end
		});

    }

    function importHSHCFile(formData) {
//      hungnx 20180618 start
        $('#savepopupMonth').prop('disabled', true);
    	htmlCommonService.showLoading('#loadingIpMonth');
//    hungnx 20180618 end
        return $.ajax({
            url: Constant.BASE_SERVICE_URL + "DetailMonthPlanRsService/importHSHC",
            type: "POST",
            data: formData,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
            	debugger;
                if (data.length === 0) {
                    toastr.warning("File imp" +
                    "ort không có dữ liệu");
                    return;
                } else if (data[data.length - 1].errorFilePath != null) {
                    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                    toastr.warning("File import không hợp lệ");
                    return;
                } else {
                	//Huypq-start
                	var grid = $("#sourceGrid").data("kendoGrid").dataSource.data();
                	if(data.length>=2){
                		for(var k=0;k<data.length;k++){
                    		for(var m=k+1;m<data.length;m++){
                    			if(data[k].constructionCode==data[m].constructionCode){
                    				window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                    				toastr.warning("File import không hợp lệ");
                                    return;
                    			}
                    		}
//                    		for(var h=0;h<grid.length;h++){
//                    			if(data[k].constructionCode==grid[h].constructionCode){
//                    				toastr.warning("File import có mã công trình trùng mã công trình trong lưới");
//                                    return;
//                    			}
//                    		}
                    	}
                	} 
//                	else {
//                		for(var k=0;k<data.length;k++){
//                    		for(var h=0;h<grid.length;h++){
//                    			if(data[k].constructionCode==grid[h].constructionCode){
//                    				toastr.warning("File import có mã công trình trùng mã công trình trong lưới");
//                                    return;
//                    			}
//                    		}
//                    	}
//                	}
                	//Huypq-start
                    toastr.success("Import dữ liệu thành công");
                    var grid = vm.sourceGrid;
                    if (grid != undefined) {
                        grid.dataSource.data(data);
                        grid.dataSource.sync();
                        grid.refresh();
                        vm.detailMonthPlan.isHSHCImport = 1;
                    }

                }
                vm.cancelImport();
            }
        }).always(function() {
//          hungnx 20180618 start
        	$('#savepopupMonth').prop('disabled', false);
        	htmlCommonService.hideLoading('#loadingIpMonth');
//          hungnx 20180618 end
		});

    }

    function importLDTFile(formData) {
//      hungnx 20180618 start
    	$('#savepopupMonth').prop('disabled', true);
    	htmlCommonService.showLoading('#loadingIpMonth');
//      hungnx 20180618 end
        return $.ajax({
            url: Constant.BASE_SERVICE_URL + "DetailMonthPlanRsService/importLDT",
            type: "POST",
            data: formData,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                if (data.length === 0) {
                    toastr.warning("File imp" +
                    "ort không có dữ liệu");
                    return;
                } else if (data[data.length - 1].errorFilePath != null) {
                    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                    toastr.warning("File import không hợp lệ");
                    return;
                }
                else {


                    toastr.success("Import dữ liệu thành công");
                    var detailList = [];
                    var grid = vm.forceMaintainGrid;
                    if (grid != undefined) {
                        grid.dataSource.data(data);
                        grid.dataSource.sync();
                        grid.refresh();
                        vm.detailMonthPlan.isLDTImport = 1;
                    }
                }
                vm.cancelImport();
            }
        }).always(function() {
//          hungnx 20180618 start
        	$('#savepopupMonth').prop('disabled', false);
        	htmlCommonService.hideLoading('#loadingIpMonth');
//          hungnx 20180618 end
		});

    }

    function importCVKFile(formData) {
//      hungnx 20180618 start
    	$('#savepopupMonth').prop('disabled', true);
    	htmlCommonService.showLoading('#loadingIpMonth');
//      hungnx 20180618 end
        return $.ajax({
            url: Constant.BASE_SERVICE_URL + "DetailMonthPlanRsService/importCVK",
            type: "POST",
            data: formData,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                if (data.length === 0) {
                    toastr.warning("File imp" +
                    "ort không có dữ liệu");
                    return;
                } else if (data[data.length - 1].errorFilePath != null) {
                    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                    toastr.warning("File import không hợp lệ");
                    return;
                }
                else {

                    var grid = $("#OtherJobs").data("kendoGrid");
                    if (grid != undefined) {
                        grid.dataSource.data(data);
                        grid.dataSource.sync();
                        grid.refresh();
                        vm.detailMonthPlan.isCVKImport = 1;
                    }
                    toastr.success("Import dữ liệu thành công");
                }
                vm.cancelImport();
            }
        }).always(function() {
//          hungnx 20180618 start
        	$('#savepopupMonth').prop('disabled', false);
        	htmlCommonService.hideLoading('#loadingIpMonth');
//          hungnx 20180618 end
		});

    }

    function importDTFile(formData) {
//      hungnx 20180618 start
    	$('#savepopupMonth').prop('disabled', true);
    	htmlCommonService.showLoading('#loadingIpMonth');
//      hungnx 20180618 end
        return $.ajax({
            url: Constant.BASE_SERVICE_URL + "DetailMonthPlanRsService/importDT",
            type: "POST",
            data: formData,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                if (data.length === 0) {
                    toastr.warning("File imp" +
                    "ort không có dữ liệu");
                    return;
                } else if (data[data.length - 1].errorFilePath != null) {
                    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                    toastr.warning("File import không hợp lệ");
                    return;
                }
                else {

                    var grid = $("#CashFlow").data("kendoGrid");
                    if (grid != undefined) {
                        grid.dataSource.data(data);
                        grid.dataSource.sync();
                        grid.refresh();
                        vm.detailMonthPlan.isDTImport = 1;
                    }
                    toastr.success("Import dữ liệu thành công");
                }
                vm.cancelImport();
            }
        }).always(function() {
//          hungnx 20180618 start
        	$('#savepopupMonth').prop('disabled', false);
        	htmlCommonService.hideLoading('#loadingIpMonth');
//          hungnx 20180618 end
		});

    }

    function importYCVTFile(formData) {
//      hungnx 20180618 start
    	$('#savepopupMonth').prop('disabled', true);
    	htmlCommonService.showLoading('#loadingIpMonth');
//      hungnx 20180618 end
        return $.ajax({
            url: Constant.BASE_SERVICE_URL + "DetailMonthPlanRsService/importYCVT",
            type: "POST",
            data: formData,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                if (data.length === 0) {
                    toastr.warning("File imp" +
                    "ort không có dữ liệu");
                    return;
                } else if (data[data.length - 1].errorFilePath != null) {
                    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                    toastr.warning("File import không hợp lệ");
                    return;
                }
                else {

                    var grid = $("#BTSGrid").data("kendoGrid");
                    if (grid != undefined) {
                        grid.dataSource.data(data);
                        grid.dataSource.sync();
                        grid.refresh();
                        vm.detailMonthPlan.isYCVTImport = 1;
                    }
                    toastr.success("Import dữ liệu thành công");
                }
                vm.cancelImport();
            }
        }).always(function() {
//          hungnx 20180618 start
        	$('#savepopupMonth').prop('disabled', false);
        	htmlCommonService.hideLoading('#loadingIpMonth');
//          hungnx 20180618 end
		});

    }

    function fillGrid(id, data) {
        var grid = $(id).data("kendoGrid");
        grid.dataSource.data(data);
        grid.refresh();
    }

    //luanlv-thongtin-thang-tongthe
    vm.getTotalMonthPlanDetail = getTotalMonthPlanDetail;
    function getTotalMonthPlanDetail() {
        var teamplateUrl = "coms/detailMonthPlan/total-month-plan-detail-form.html";
        var title = "Thông tin kế hoạch tháng tổng thể";
        var windowId = "yearPlan1";
        vm.popUpOpen = 'detailMonth';
        CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '700', '', "");
    }

    vm.cancelPopupDetail = cancelPopupDetail;
    function cancelPopupDetail() {
        CommonService.dismissPopup1();
    }

    $scope.$on("Popup.open", function () {
        if (vm.popUpOpen == 'detailMonth') {
            detailMonthPlanService.getYearPlanDetailTarget(vm.detailMonthPlan).then(function (result) {
                //var grid = $("#yearPlanDetail").data("kendoGrid");
                var grid = vm.planDetailGrid;
                if (grid) {
                    grid.dataSource.data(result);
                    grid.refresh();
                }
            }, function (error) {

            });
        }
        else if (vm.popUpOpen == 'hoanCong') {
            detailMonthPlanService.getWorkItemDetail(vm.hoanCongItem).then(function (result) {
                //var grid = $("#yearPlanDetail").data("kendoGrid");
                var grid = vm.sourceDetailGrid;
                if (grid) {
                    grid.dataSource.data(result);
                    grid.refresh();
                }
                vm.sumQuantity = 0;
                if (result != undefined) {
                    angular.forEach(result, function (item) {
                        vm.sumQuantity += item.quantity;
                    })
                }
            }, function (error) {

            });
        }
        else if (vm.popUpOpen == 'doanhThu') {
            detailMonthPlanService.getWorkItemDetail(vm.doanhThuItem).then(function (result) {
                //var grid = $("#yearPlanDetail").data("kendoGrid");
                var grid = vm.forceMaintainDetailGrid;
                if (grid) {
                    grid.dataSource.data(result);
                    grid.refresh();
                }
                vm.sumQuantity = 0;
                if (result != undefined) {
                    angular.forEach(result, function (item) {
                        vm.sumQuantity += item.quantity;
                    })
                }
            }, function (error) {

            });
        }
        else if (vm.popUpOpen == 'dongTien') {
            detailMonthPlanService.getWorkItemDetail(vm.dongTienItem).then(function (result) {
                //var grid = $("#yearPlanDetail").data("kendoGrid");
                var grid = vm.CashFlowDetailGrid;
                if (grid) {
                    grid.dataSource.data(result);
                    grid.refresh();
                }
                vm.sumQuantity = 0;
                if (result != undefined) {
                    angular.forEach(result, function (item) {
                        vm.sumQuantity += item.quantity;
                    })
                }
            }, function (error) {

            });
        }
    });
    vm.fillTotalMonthPlanDetailTable = fillTotalMonthPlanDetailTable;
    function fillTotalMonthPlanDetailTable() {
        vm.planDetailGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            editable: false,
            resizable: true,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            reorderable: true,
            noRecords: true,
            save: function () {
                var grid = this;
                setTimeout(function () {
                    grid.refresh();
                })
            },
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            pageable: {
                refresh: false,
                pageSize: 10,
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
                    },
                    editable: false
                },
                {
                    title: "Đơn vị",
                    field: 'sysGroupName',
                    width: '35%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false

                },
                {
                    title: "Nguồn việc",
                    field: 'source',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    editable: true,
                    format: "{0:n3}",
                },
                {
                    title: "Sản lượng",
                    field: 'quantity',
                    width: '15%',
                    format: "{0:n3}",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    editable: false,
                }, {
                    title: "HSHC",
                    field: 'complete',
                    width: '15%',
                    format: "{0:n3}",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    editable: false
                },
                {
                    title: "Doanh thu",
                    field: 'revenue',
                    width: '15%',
                    format: "{0:n3}",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    editable: false
                }
            ]
        });
    }

    vm.checkDateFrom = checkDateFrom;
    function checkDateFrom() {
        var startDate = $('#createFrom').val();
        var endDate = $('#createTo').val();
        vm.errMessageDateFrom = '';
        var curDate = new Date();

        //if(kendo.parseDate(startDate,"dd/MM/yyyy") > curDate){
        //    vm.errMessage1 = 'Ngày tạo phải nhỏ hơn bằng ngày hiện tại';
        //    $("#impNoteCreateFrom").focus();
        //    return vm.errMessage1;
        //}
        //else if(startDate <= curDate){
        //    vm.errMessage1 = '';
        //    return vm.errMessage1;
        //}
        if (endDate !== "" && startDate !== "") {
            if (kendo.parseDate(startDate, "dd/MM/yyyy") > kendo.parseDate(endDate, "dd/MM/yyyy")) {
                vm.errMessageDateFrom = 'Ngày thực hiện từ phải nhỏ hơn bằng Ngày thực hiện đến';
                $("#createFrom").focus();
                vm.invalidDate = true;
                return vm.errMessageDateFrom;
            } else {
                vm.errMessageDateTo = '';
                vm.invalidDate = false;
            }

        }

    }

    vm.checkDateTo = checkDateTo;
    function checkDateTo() {
        var startDate = $('#createFrom').val();
        var endDate = $('#createTo').val();
        vm.errMessageDateTo = '';
        var curDate = new Date();

        //if(kendo.parseDate(startDate,"dd/MM/yyyy") > curDate){
        //    vm.errMessage1 = 'Ngày tạo phải nhỏ hơn bằng ngày hiện tại';
        //    $("#impNoteCreateFrom").focus();
        //    return vm.errMessage1;
        //}
        //else if(startDate <= curDate){
        //    vm.errMessage1 = '';
        //    return vm.errMessage1;
        //}
        if (startDate !== "" && endDate != "") {
            if (kendo.parseDate(startDate, "dd/MM/yyyy") > kendo.parseDate(endDate, "dd/MM/yyyy")) {
                vm.errMessageDateTo = 'Ngày thực hiện từ phải nhỏ hơn bằng Ngày thực hiện đến';
                $("#createTo").focus();
                vm.invalidDate = true;
                return vm.errMessageDateTo;
            } else {
                vm.errMessageDateFrom = '';
                vm.invalidDate = false;
            }
        }
    }

    vm.cancelEditPopup = cancelEditPopup;
    function cancelEditPopup() {
        CommonService.dismissPopup1();
    }

    vm.saveThiCongPopup = saveThiCongPopup;
    function saveThiCongPopup() {
        if (vm.thiCongItem.performerId == undefined) {
            toastr.warning("Người thi công không được để trống")
            return;
        } else if (vm.thiCongItem.quantity == undefined || vm.thiCongItem.quantity == '') {
            toastr.warning("Giá trị sản lượng không được để trống")
            return;
        }
        else if (vm.thiCongItem.startDate == undefined || vm.thiCongItem.startDate == '') {
            toastr.warning("Ngày tạo từ không được để trống")
            return;
        }
        else if (vm.thiCongItem.endDate == undefined || vm.thiCongItem.endDate == '') {
            toastr.warning("Ngày tạo đến không được để trống")
            return;
        }
        vm.cancelEditPopup();
        toastr.success("Ghi lại thành công");
        var list= vm.targetGrid.dataSource.data();
        var dataItem = list[vm.indexTab1];
        var quan = vm.thiCongItem.quantity.replace(/\,/g,'');
        dataItem.performerId = vm.thiCongItem.performerId;
        dataItem.performerName = vm.thiCongItem.performerName;
        dataItem.quantity = parseInt(quan);
        dataItem.startDate = vm.thiCongItem.startDate;
        dataItem.endDate = vm.thiCongItem.endDate;
        dataItem.sourceType = vm.thiCongItem.sourceType;
        dataItem.deployType = vm.thiCongItem.deployType;
        dataItem.supervisorId = vm.thiCongItem.supervisorId;
        dataItem.supervisorName = vm.thiCongItem.supervisorName;
        dataItem.directorId = vm.thiCongItem.directorId;
        dataItem.directorName = vm.thiCongItem.directorName;
        dataItem.description = vm.thiCongItem.description;
        vm.targetGrid.refresh();


    }

//luanlv-end
//save popup doanh thu
    vm.saveDoanhThuPopup = saveDoanhThuPopup;
    function saveDoanhThuPopup() {
        if (vm.doanhThuItem.performerId == undefined) {
            toastr.warning("Người thi công không được để trống")
            return;
        }
        else if (vm.doanhThuItem.startDate == undefined || vm.doanhThuItem.startDate == '') {
            toastr.warning("Ngày tạo từ không được để trống")
            return;
        }
        else if (vm.doanhThuItem.endDate == undefined || vm.doanhThuItem.endDate == '') {
            toastr.warning("Ngày tạo đến không được để trống")
            return;
        }
        vm.cancelEditPopup();
        toastr.success("Ghi lại thành công");
        var list= vm.forceMaintainGrid.dataSource.data();
        var dataItem = list[vm.indexTab3];
        dataItem.performerId = vm.doanhThuItem.performerId;
        dataItem.performerName = vm.doanhThuItem.performerName;
        dataItem.startDate = vm.doanhThuItem.startDate;
        dataItem.endDate = vm.doanhThuItem.endDate;
        dataItem.description = vm.doanhThuItem.description;
        vm.forceMaintainGrid.refresh();
        /*detailMonthPlanService.updateDT(vm.doanhThuItem).then(function(result){
         toast.success("Ghi lại thành công");
         vm.doanhThuItem={};
         cancelEditPopup();
         },function(error){
         toast.error("Ghi lại thất bại");
         });*/

    }

//save popup hoan cong
    vm.saveHoanCongPopup = saveHoanCongPopup;
    function saveHoanCongPopup(x) {
        if (vm.hoanCongItem.performerId == undefined) {
            toastr.warning("Người thi công không được để trống")
            return;
        }
        else if (vm.hoanCongItem.startDate == undefined || vm.hoanCongItem.startDate == '') {
            toastr.warning("Ngày tạo từ không được để trống")
            return;
        }
        else if (vm.hoanCongItem.endDate == undefined || vm.hoanCongItem.endDate == '') {
            toastr.warning("Ngày tạo đến không được để trống")
            return;
        }
        vm.cancelEditPopup();
        toastr.success("Ghi lại thành công");
        var list= vm.sourceGrid.dataSource.data();
        var dataItem = list[vm.indexTab2];
        dataItem.performerId = vm.hoanCongItem.performerId;
        dataItem.performerName = vm.hoanCongItem.performerName;
        dataItem.supervisorId = vm.hoanCongItem.supervisorId;
        dataItem.supervisorName = vm.hoanCongItem.supervisorName;
        dataItem.startDate = vm.hoanCongItem.startDate;
        dataItem.endDate = vm.hoanCongItem.endDate;
        dataItem.directorId = vm.hoanCongItem.directorId;
        dataItem.directorName = vm.hoanCongItem.directorName;
        dataItem.description = vm.hoanCongItem.description;
        vm.sourceGrid.refresh();

    }

    function updateDataTable(x, id) {
        var listData = getDataGrid(id);
        for (var i = 0; i < listData.length; i++) {
            if (x.constructionCode == listData[i].constructionCode) {
                listData[i] = x;
            }
            ;
        }
        fillDataGrid(listData, id);
    }

//save popup dong tien
    vm.saveDongTienPopup = saveDongTienPopup;
    function saveDongTienPopup() {
        vm.cancelEditPopup();
    }


//detail -tab2-SourceDetail-start
    vm.fillDetailsourceGridTable = fillDetailsourceGridTable;
    function fillDetailsourceGridTable() {
        vm.sourceDetailGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            editable: false,
            resizable: true,
            toolbar: [
                {
                    name: "actions",
                    format: "{0:n3}",
                    template: '<div class="btn-group pull-right margin_top_button margin10">' +
                    '<span>Tổng giá trị sản lượng :{{caller.sumQuantity|currency : ""}}</span>' +
                        //'<td class="text-right">#= kendo.toString(kendo.parseFloat(total), "n3") #</td>'+
                    '</div>'

                }
            ],
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            reorderable: true,
            noRecords: true,
            save: function () {
                var grid = this;
                setTimeout(function () {
                    grid.refresh();
                })
            },
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            pageable: {
                refresh: false,
                pageSize: 10,
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
                    },
                    editable: false
                },
                {
                    title: "Mã hạng mục",
                    field: 'code',
                    width: '35%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false

                },
                {
                    title: "Loại hạng mục",
                    field: 'catWorkItemTypeName',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true,
                },
                {
                    title: "Trạng thái",
                    field: 'status',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false,
                    template: function (dataItem) {
                        if (dataItem.status == 1) {
                            return "Chưa thực hiện";
                        } else if (dataItem.status == 2) {
                            return "Đang thực hiện"
                        } else if (dataItem.status == 3) {
                            return "Đã Hoàn thành"
                        }

                    }
                }, {
                    title: "Giá trị sản lượng",
                    field: 'quantity',
                    width: '15%',
                    format: "{0:n3}",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    editable: false
                }
            ],

        });
    }

//detail -tab2-SourceDetail-end
//detail -tab3-start
    vm.fillDetailforceMaintainGridTable = fillDetailforceMaintainGridTable;
    function fillDetailforceMaintainGridTable() {
        vm.forceMaintainDetailGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            editable: false,
            resizable: true,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            toolbar: [
                {
                    name: "actions",
                    format: "{0:n3}",
                    template: '<div class="btn-group pull-right margin_top_button margin10">' +
                    '<span>Tổng giá trị sản lượng :{{caller.sumQuantity|currency : ""}}</span>' +
                        //'<td class="text-right">#= kendo.toString(kendo.parseFloat(total), "n3") #</td>'+
                    '</div>'

                }
            ],
            reorderable: true,
            noRecords: true,
            save: function () {
                var grid = this;
                setTimeout(function () {
                    grid.refresh();
                })
            },
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            pageable: {
                refresh: false,
                pageSize: 10,
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
                    },
                    editable: false
                },
                {
                    title: "Mã hạng mục",
                    field: 'code',
                    width: '35%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false

                },
                {
                    title: "Loại hạng mục",
                    field: 'catWorkItemTypeName',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                },
                {
                    title: "Trạng thái",
                    field: 'status',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: function (dataItem) {
                        if (dataItem.status == 1) {
                            return "Chưa thực hiện";
                        } else if (dataItem.status == 2) {
                            return "Đang thực hiện"
                        } else if (dataItem.status == 3) {
                            return "Đã Hoàn thành"
                        }

                    }
                }, {
                    title: "Giá trị sản lượng",
                    field: 'quantity',
                    width: '15%',
                    format: "{0:n3}",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    editable: false
                }
            ],
        });
    }

//detail -tab3-end

//datail-CashFlow-tab5-start
    vm.fillDetailcashFlowDetailGridTable = fillDetailcashFlowDetailGridTable;
    function fillDetailcashFlowDetailGridTable() {
        vm.CashFlowDetailGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            editable: false,
            resizable: true,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            toolbar: [
                {
                    name: "actions",
                    format: "{0:n3}",
                    template: '<div class="btn-group pull-right margin_top_button margin10">' +
                    '<span>Tổng giá trị:{{caller.sumQuantity|currency : ""}}</span>' +
                        //'<td class="text-right">#= kendo.toString(kendo.parseFloat(total), "n3") #</td>'+
                    '</div>'

                }
            ],
            reorderable: true,
            noRecords: true,
            save: function () {
                var grid = this;
                setTimeout(function () {
                    grid.refresh();
                })
            },
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            pageable: {
                refresh: false,
                pageSize: 10,
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
                    },
                    editable: false
                },
                {
                    title: "Mã hạng mục",
                    field: 'code',
                    width: '35%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false

                },
                {
                    title: "Loại hạng mục",
                    field: 'catWorkItemTypeName',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true,
                },
                {
                    title: "Trạng thái",
                    field: 'status',
                    width: '15%',
                    format: "{0:n3}",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
                    editable: false,
                    template: function (dataItem) {
                        if (dataItem.status == 1) {
                            return "Chưa thực hiện";
                        } else if (dataItem.status == 2) {
                            return "Đang thực hiện"
                        } else if (dataItem.status == 3) {
                            return "Đã Hoàn thành"
                        }

                    }
                }, {
                    title: "Giá trị",
                    field: 'quantity',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    editable: false
                }
            ],
        });
    }

//detail-CashFlow-tab5-end
    vm.doSearchDetail = doSearchDetail
    function doSearchDetail(id) {
        vm.detailMonthPlan.listConstrTaskIdDelete = [];
        $('#' + id).data("kendoGrid").dataSource.page(1);
        if ('OtherJobs' == id) {
            vm.detailMonthPlan.isCVKImport = 0;
        } else if ('forceMaintainGrid' == id) {
            vm.detailMonthPlan.isLDTImport = 0;
        } else if ('BTSGrid' == id) {
            vm.detailMonthPlan.isYCVTImport = 0;
        } else if ('CashFlow' == id) {
            vm.detailMonthPlan.isDTImport = 0;
        } else if ('targetGrid' == id) {
            vm.detailMonthPlan.isTCImport = 0;
        } else if ('sourceGrid' == id) {
            vm.detailMonthPlan.isHSHCImport = 0;
        }
    }
    vm.cancelDateFrom = cancelDateFrom
    function cancelDateFrom(id){
        if(id=='thicong')
            vm.thiCongItem.startDate = undefined;
        else if(id=='hshc')
            vm.hoanCongItem.startDate = undefined;
        else if (id=='ldt')
            vm.doanhThuItem.startDate = undefined;
        vm.checkDateFrom()
    }
    vm.cancelDateTo = cancelDateTo
    function cancelDateTo(id,formTo,formFrom){
        if(id=='thicong') {
            vm.thiCongItem.endDate = undefined;
            vm.thiCongItem.startDate = undefined;
            $rootScope.validateDate( vm.thiCongItem.endDate,null,null,formTo)
            $rootScope.validateDate( vm.thiCongItem.startDate,null,null,formFrom)
        }
        else if(id=='hshc') {
            vm.hoanCongItem.startDate = undefined;
            vm.hoanCongItem.endDate = undefined;
            $rootScope.validateDate( vm.hoanCongItem.endDate,null,null,formTo)
            $rootScope.validateDate( vm.hoanCongItem.startDate,null,null,formFrom)
        }
        else if (id=='ldt') {
            vm.doanhThuItem.endDate = undefined;
            vm.doanhThuItem.startDate = undefined;
            $rootScope.validateDate( vm.doanhThuItem.endDate,null,null,formTo)
            $rootScope.validateDate( vm.doanhThuItem.startDate,null,null,formFrom)
        }
        vm.checkDateTo();
        vm.checkDateFrom();
    }
}
