/**
 * Created by pm1_os42 on 2/8/2018.
 */
function initTab2FunctionTotal($scope, $rootScope, $timeout, gettextCatalog,
                          kendoConfig, $kWindow, totalMonthPlanService,
                          CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm,htmlCommonService) {
    var CHI_TIEU = 1;
    var DAM_BAO_NGUON_VIEC = 2;
    var DAM_BAO_LUC_LUONG = 3;
    var GIA_CO_DU_AN = 31;
    var BTS = 32;
    var NGAM_HOA = 33;
    var DAM_BAO_VAT_TU = 4;
    var DAM_BAO_TAI_CHINH = 5;
    var DAM_BAO_HD = 6;
    var PHU_LUC = 7;
    vm.searchForm = {};

    vm.value = 1;
    vm.different={quantity:0,complete:0,revenue:0};
    vm.yearPlan={quantity:0,complete:0,revenue:0};
    vm.differentSource={source:0};
    vm.yearPlanSource={source:0};
    vm.totalMonthPlanTemp={}
    vm.chooseTab = chooseTab;
    function chooseTab(value) {
        vm.totalMonthPlanTemp = {
        };
        vm.value = value;
        if (value == DAM_BAO_LUC_LUONG || value == GIA_CO_DU_AN || value == BTS || value == NGAM_HOA)
            vm.isForce = true;
        else {
            vm.isForce = false;
        }
        //vm.fillMaterialTable();
        //vm.fillYearPlanDetailTable();
        vm.fillAppendixTable();
        if(value == CHI_TIEU){
            vm.fillTargetTable();
        }
        if(value == DAM_BAO_LUC_LUONG || value == GIA_CO_DU_AN){
            vm.fillForceMaintainTable();
        }
        if(value == BTS){
            vm.fillBTSTable();
        }
        if(value == NGAM_HOA){
            vm.fillForceNewTable();
        }

        if(value == DAM_BAO_VAT_TU){
            vm.fillMaterialTable();
        }
        if(value == DAM_BAO_TAI_CHINH){
            vm.fillFinanceTable();
        }
        if(value == DAM_BAO_HD){
            vm.fillContractTable();
        }
        if(value == DAM_BAO_HD){
            vm.fillContractTable();
        }
        if(value == PHU_LUC){
            vm.fillAppendixTable();

        }

        //HaLV - Đồng bộ sản lượng tab chỉ tiêu và nguồn việc
        if(value == DAM_BAO_NGUON_VIEC){
            vm.fillSourceTable();

            var dataTarget = vm.targetGrid.dataSource._data;
        	var dataSource = vm.sourceGrid.dataSource._data;       	

        	if(dataTarget&&dataSource){
        	angular.forEach(dataTarget,function(dataTargetItem){
        		var i=0;
        		angular.forEach(dataSource,function(dataSourceItem){
        			if(dataSourceItem.sysGroupId===dataTargetItem.sysGroupId){
        				dataSourceItem.quantity=dataTargetItem.quantity;
        			}
        			dataSource[i]=dataSourceItem;
    				i++;
        		})   
        		
        	})
        	}
        	vm.sourceGrid.dataSource.data(dataSource);
        	vm.sourceGrid.dataSource.sync();
        	vm.sourceGrid.refresh();
        }


    }

    vm.fillTargetTable = fillTargetTable;
    function fillTargetTable() {
        vm.targetGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            //dataSource: {
            //        serverPaging: true,
            //        schema: {
            //            total: function (response) {
            //                return response.total; // total is returned in
            //                // the "total" field of
            //                // the response
            //            },
            //            data: function (response) {
            //                return response.data; // data is returned in
            //                // the "data" field of
            //                // the response
            //            }
            //        },
            //        transport: {
            //            read: {
            //                // Thuc hien viec goi service
            //                url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/doSearchTarget",
            //                contentType: "application/json; charset=utf-8",
            //                type: "POST"
            //            },
            //            parameterMap: function (options, type) {
            //                vm.searchForm.page = options.page
            //                vm.searchForm.pageSize = options.pageSize
            //                vm.searchForm.totalMonthPlanId = vm.totalMonthPlan.totalMonthPlanId
            //                return JSON.stringify(vm.searchForm)
            //            }
            //        },
            //        pageSize: 10
            //    },
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
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
                    editable: false
                },
                {
                    title: "Đơn vị",
                    field: 'sysGroupName',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false

                },
                {
                    title: "Chỉ tiêu tháng",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    columns: [{
                        title: "Sản lượng",
                        field: 'quantity',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        format: "{0:n3}",
                        aggregates: ["sum"],
                        footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
                    },
                        {
                            title: "HSHC",
                            field: 'complete',
                            width: '10%',
                            format: "{0:n3}",
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            editable: true,
                            aggregates: ["sum"],
                            footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
                        },
                        {
                            title: "Doanh thu",
                            field: 'revenue',
                            width: '10%',
                            format: "{0:n3}",
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            editable: true,
                            aggregates: ["sum"],
                            footerTemplate: function(data){
                                if(data.revenue.sum!=null){
                                    return kendo.toString(kendo.parseFloat(data.revenue.sum), "n3");
                                }else return ''
                            }
                        }
                    ]
                },
                {
                    title: "Lũy kế TH đến tháng trước hiện tại",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    columns: [{
                        title: "Sản lượng",
                        field: 'quantityLk',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: false,
                        format: "{0:n3}",
                        aggregates: ["sum"],
                        footerTemplate: function(data){
                            if(data.quantityLk.sum!=null){
                                return kendo.toString(kendo.parseFloat(data.quantityLk.sum), "n3");
                            }else return ''
                        }
                    },
                        {
                            title: "HSHC",
                            field: 'completeLk',
                            width: '10%',
                            format: "{0:n3}",
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            editable: true,
                            aggregates: ["sum"],
                            footerTemplate: function(data){
                                if(data.completeLk.sum!=null){
                                    return kendo.toString(kendo.parseFloat(data.completeLk.sum), "n3");
                                }else return ''
                            }
                        },
                        {
                            title: "Doanh thu",
                            field: 'revenueLk',
                            width: '10%',
                            format: "{0:n3}",
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            editable: false,
                            aggregates: ["sum"],
                            footerTemplate: function(data){
                                if(data.revenueLk.sum!=null){
                                    return kendo.toString(kendo.parseFloat(data.revenueLk.sum), "n3");
                                }else return ''
                            }
                        }]
                },
                {
                    title: "Kế hoạch năm ",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    columns: [{
                        title: "Sản lượng",
                        field: 'quantityInYear',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: false,
                        format: "{0:n3}",
                        aggregates: ["sum"],
                        footerTemplate: function(data){
                            if(data.quantityInYear.sum!=null){
                                return kendo.toString(kendo.parseFloat(data.quantityInYear.sum), "n3");
                            }else return ''
                        }
                    },
                        {
                            title: "Tỷ lệ",
                            width: '10%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            field: 'quantityTl',
                            editable: false,
                            template: function (data) {
                                if(data.quantityInYear!=null&&data.quantityInYear!=0&&data.quantityInYear!='')
                                return kendo.toString(Number(data.quantityLk||0)  * 100/ Number(data.quantityInYear||0), "n0") + '%';
                                else return '';
                            },
                            footerTemplate: function(data){
                                if(data.quantityInYear.sum!=null&&data.quantityInYear.sum!=0&&data.quantityInYear.sum!='')
                                    return kendo.toString(kendo.parseInt(Number(data.quantityLk.sum||0)  * 100/ Number(data.quantityInYear.sum||0))) + '%';
                                else return '';
                            }
                        },
                        {
                            title: "HSHC",
                            field: 'completeInYear',
                            width: '10%',
                            format: "{0:n3}",
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            editable: true,
                            aggregates: ["sum"],
                            footerTemplate: function(data){
                                if(data.completeInYear.sum!=null){
                                    return kendo.toString(kendo.parseFloat(data.completeInYear.sum), "n3");
                                }else return ''
                            }
                        },
                        {
                            title: "Tỷ lệ",
                            width: '10%',
                            field: 'completeTl',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            editable: false,
                            template: function (data) {
                                if(data.completeInYear!=0&&data.completeInYear!=undefined&&data.completeInYear!='')
                                return  kendo.toString(Number(data.completeLk||0)  * 100/ Number(data.completeInYear||0), "n0")  + '%';
                                else return '';
                            },
                            footerTemplate: function(data){
                                if(data.completeInYear.sum!=0&&data.completeInYear.sum!=undefined&&data.completeInYear.sum!='')
                                    return kendo.toString(Number(data.completeLk.sum||0)  * 100/ Number(data.completeInYear.sum||0), "n0")  + '%';
                                else return '';
                            }
                        },
                        {
                            title: "Doanh thu",
                            field: 'revenueInYear',
                            width: '10%',
                            format: "{0:n3}",
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            editable: false,
                            aggregates: ["sum"],
                            footerTemplate: function(data){
                                if(data.revenueInYear.sum!=null){
                                    return kendo.toString(kendo.parseFloat(data.revenueInYear.sum), "n3");
                                }else return ''
                            }
                        },
                        {
                            title: "Tỷ lệ",
                            width: '10%',
                            field: 'revenueTl',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            editable: false,
                            template: function (data) {
                                if(data.revenueInYear!=0&&data.revenueInYear!=undefined&&data.revenueInYear!='')
                                return kendo.toString(Number(data.revenueLk||0)* 100 / Number(data.revenueInYear||0), "n0")  + '%';
                                else return '';
                            },
                            footerTemplate: function(data){
                                if(data.revenueInYear.sum!=0&&data.revenueInYear.sum!=undefined&&data.revenueInYear.sum!='')
                                    return kendo.toString(Number(data.revenueLk.sum||0)* 100 / Number(data.revenueInYear.sum||0), "n0")  + '%';
                                else return '';
                            }
                        },
                    ]
                },
                {
                    title: "HSHC",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    columns: [
                        {
                            title: "HSHC Chi phí",
                            field: 'hshcCp',
                            width: '10%',
                            format: "{0:n3}",
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            editable: true,
                            aggregates: ["sum"],
                            footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
                        },
                        {
                            title: "HSHC Xây lắp",
                            field: 'hshcXl',
                            width: '10%',
                            format: "{0:n3}",
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            editable: true,
                            aggregates: ["sum"],
                            footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
                        },
                        {
                            title: "HSHC Ngoài tập đoàn",
                            field: 'hshcNtd',
                            width: '10%',
                            format: "{0:n3}",
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            editable: true,
                            aggregates: ["sum"],
                            footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
                        },
                        {
                            title: "HSHC XDDD",
                            field: 'hshcXddd',
                            width: '10%',
                            format: "{0:n3}",
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            editable: true,
                            aggregates: ["sum"],
                            footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
                        },
                        {
                            title: "HSHC HTCT",
                            field: 'hshcHtct',
                            width: '10%',
                            format: "{0:n3}",
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            editable: true,
                            aggregates: ["sum"],
                            footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
                        }
                    ]
                },
                {
                    title: "Thao tác",
                    template: dataItem =>
                '<div class="text-center">'
                +
                '<button style=" border: none; background-color: white;" id="removeId"' +
                'class=" icon_table" ng-click="vm.removeTargetRow($event)" uib-tooltip="Xóa" translate' +
                '>' +
                '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
                '</button>'
                + '</div>',
                field: 'action',
                width:        '10%'

    }
]
                ,
    dataSource: {
        schema: {
            //total: function (response) {
            //    return response.total; // total is returned in
            //    // the "total" field of
            //    // the response
            //},
            //data: function (response) {
            //    return response.data; // data is returned in
            //    // the "data" field of
            //    // the response
            //},
            model: {
                id: "tmpnTargetId",
                    fields
            :
                {
                    stt:{
                        editable: false, nullable
                    :
                        true
                    }
                ,
                    sysGroupName:{
                        editable: false, nullable
                    :
                        true
                    }
                ,
                    complete:{
                        editable: true, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,
                    revenue:{
                        editable: true, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,
                    quantity:{
                        editable: true, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,
                    completeLk:{
                        editable: false, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,
                    revenueLk:{
                        editable: false, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,
                    quantityLk:{
                        editable: false, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,
                    completeInYear:{
                        editable: false, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,
                    revenueInYear:{
                        editable: false, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,
                    quantityInYear:{
                        editable: false, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,
                    completeTl:{
                        editable: false, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,
                    revenueTl:{
                        editable: false, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,
                    quantityTl:{
                        editable: false, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,  hshcCp:{
                        editable: true, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,  hshcXl:{
                        editable: true, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,  hshcNtd:{
                        editable: true, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,  hshcXddd:{
                        editable: true, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,  hshcHtct:{
                        editable: true, type
                    :
                        "number", nullable
                    :
                        true
                    }
                ,
                    action:{
                        editable: false, nullable
                    :
                        true
                    }
                }
            }
        },
    //    serverPaging: true,
    //    transport: {
        //        read: {
        //            // Thuc hien viec goi service
        //            url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/doSearchTarget",
        //            contentType: "application/json; charset=utf-8",
        //            type: "POST"
        //        },
        //        parameterMap: function (options, type) {
        //            vm.searchForm.page = options.page
        //            vm.searchForm.pageSize = options.pageSize
        //            vm.searchForm.totalMonthPlanId = vm.totalMonthPlan.totalMonthPlanId
        //            return JSON.stringify(vm.searchForm)
        //        }
        //    },
    //    pageSize: 10
    //,
        aggregate: [
            {field: "quantity", aggregate: "sum"},
            {field: "complete", aggregate: "sum"},
            {field: "revenue", aggregate: "sum"},
            {field: "quantityLk", aggregate: "sum"},
            {field: "completeLk", aggregate: "sum"},
            {field: "revenueLk", aggregate: "sum"},
            {field: "quantityInYear", aggregate: "sum"},
            {field: "completeInYear", aggregate: "sum"},
            {field: "revenueInYear", aggregate: "sum"},
            
            {field: "hshcXl", aggregate: "sum"},
            {field: "hshcCp", aggregate: "sum"},
            {field: "hshcNtd", aggregate: "sum"},
            {field: "hshcXddd", aggregate: "sum"},
            {field: "hshcHtct", aggregate: "sum"},
        ]
    }
}
)
;
}
vm.fillSourceTable= fillSourceTable;
function fillSourceTable() {
    vm.sourceGridOptions = kendoConfig.getGridOptions({
        autoBind: true,
        scrollable: false,
        resizable: true,
        dataBinding: function () {
            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
        },
        //dataSource: {
        //    serverPaging: true,
        //    schema: {
        //        total: function (response) {
        //            return response.total; // total is returned in
        //            // the "total" field of
        //            // the response
        //        },
        //        data: function (response) {
        //            return response.data; // data is returned in
        //            // the "data" field of
        //            // the response
        //        }
        //    },
        //    transport: {
        //        read: {
        //            // Thuc hien viec goi service
        //            url: Constant.BASE_SERVICE_URL + "TotalMonthPlanRsService/doSearchSource",
        //            contentType: "application/json; charset=utf-8",
        //            type: "POST"
        //        },
        //        parameterMap: function (options, type) {
        //            vm.searchForm.page = options.page
        //            vm.searchForm.pageSize = options.pageSize
        //            vm.searchForm.totalMonthPlanId = vm.totalMonthPlan.totalMonthPlanId
        //            return JSON.stringify(vm.searchForm)
        //        }
        //    },
        //    pageSize: 10
        //},
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
                width: '15%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: false

            },
            {
                title: "Nguồn việc",
                field: 'source',
                width: '10%',
                format: "{0:n3}",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: true,
                aggregates: ["sum"],
                footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
            },

            {
                title: "Giao chỉ tiêu",
                field: 'quantity',
                width: '10%',
                format: "{0:n3}",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: false,
                aggregates: ["sum"],
                footerTemplate: function(data){
                    if(data.quantity.sum!=null){
                        return kendo.toString(kendo.parseFloat(data.quantity.sum), "n3")
                    }else return ''
                }
            },
            {
                title: "Thừa thiếu",
                width: '10%',
                format: "{0:n3}",
                field: 'difference',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: false,
                aggregates: ["sum"],
                template:function(data){
                    return kendo.toString(kendo.parseFloat(Number(data.source||0)- Number(data.quantity||0)) ,"n3");
                },
                footerAttributes: {style: "text-align:right;"},
                //footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
                footerTemplate: function(data) {
                    //if (data.difference.sum != null) {
                    //    return kendo.toString(kendo.parseFloat(Number(data.difference.sum || 0)), "n3")
                    //}
                    //else
                    //    return '';
                    return kendo.toString(kendo.parseFloat(Number(data.source.sum||0)- Number(data.quantity.sum||0)), "n3");

                }
            },
            {
                title: "Tỷ lệ thiếu",
                width: '10%',
                field: 'tlt',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: false,
                template: function (data) {
                    if(Number(data.source||0)-Number(data.quantity||0)<0&&Number(data.quantity||0)!=0)
                        //return  kendo.toString(kendo.parseInt(((Number(data.difference||0) / Number(data.quantity||0) * 100))) ) + '%';
                        return  kendo.toString(kendo.parseInt((((Number(data.source||0)- Number(data.quantity||0))/Number(data.quantity||0))*100)) ) + '%';
                    else return '';
                },
                //footerAttributes: {style: "text-align:right;"},
                //footerTemplate: function(data){
                //    if(data.difference.sum<0&&data.quantity.sum!=0)
                //        return kendo.toString(kendo.parseFloat(((Number(data.difference.sum||0) / Number(data.quantity.sum||0) * 100))), "n3") + '%';
                //    else return '';
                //    //if(Number(data.source.sum||0)<0&&Number(data.source.sum||0)!=0)
                //    //    return  kendo.toString(kendo.parseInt((((Number(data.source.sum||0)- Number(data.quantity.sum||0))/Number(data.quantity.sum||0))*100)))+'%';
                //}
            },
            {
                title: "Ghi chú",
                width: '10%',
                field: 'description',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: true
            },
            {
                title: "Thao tác",
                template: dataItem =>
            '<div class="text-center">'
            +
            '<button style=" border: none; background-color: white;" id="removeId"' +
            'class=" icon_table" ng-click="vm.removeSourceRow($event)" uib-tooltip="Xóa" translate' +
            '>' +
            '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
            '</button>'
            + '</div>',
            width:   '10%',
        field:    "action"}

],
dataSource: {
    schema: {
        //total: function (response) {
        //    return response.total; // total is returned in
        //    // the "total" field of
        //    // the response
        //},
        //data: function (response) {
        //    return response.data; // data is returned in
        //    // the "data" field of
        //    // the response
        //},
        model: {
            id: "tmpnSourceId ",
                fields    :
            {
                stt:{
                    editable: false, nullable :   true
                }
            ,
                sysGroupName:{
                    editable: false, nullable :   true
                }
            ,
                source:{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                quantity:{
                    editable: false, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                difference :{
                    editable: false, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                tlt:{
                    editable: false, nullable:true
                }
            ,
                description:{
                    editable: true, type
                        :
                        "text", nullable
                        :
                        true
                }
            ,
                action:{
                    editable: false, nullable
                :
                    true
                }
            }
        }
    },
//            serverPaging: true,
//            transport: {
//                read: {
//                    // Thuc hien viec goi service
//                    url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/doSearchSource",
//                    contentType: "application/json; charset=utf-8",
//                    type: "POST"
//                },
//                parameterMap: function (options, type) {
//                    vm.searchForm.page = options.page
//                    vm.searchForm.pageSize = options.pageSize
//                    vm.searchForm.totalMonthPlanId = vm.totalMonthPlan.totalMonthPlanId
//                    return JSON.stringify(vm.searchForm)
//                }
//            },
//            pageSize: 10
//,
    aggregate: [
        {field: "source", aggregate: "sum"},
        {field: "quantity", aggregate: "sum"},
        {field: "difference", aggregate: "sum"},
    ]}});
}
vm.fillForceMaintainTable= fillForceMaintainTable
function fillForceMaintainTable() {
    vm.forceMaintainGridOptions = kendoConfig.getGridOptions({
        autoBind: true,
        scrollable: false,
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
                width: '15%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: false

            },
            {
                title: "Kế hoạch tháng",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                columns: [
                    {
                        title: "Xây dựng",
                        field: 'buildPlan',
                        width: '7%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;cursor:pointer;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: '#=sum#'
                    },
                    {
                        title: "Tổng",
                        field: 'sumKht',
                        width: '7%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: false,
                        aggregates: ["sum"],
                        footerAttributes: {style: "text-align:right;"},
                        footerTemplate: '#=data.installPlan.sum+data.replacePlan.sum#',
                        template: function(dataItem){
                            return dataItem.installPlan+dataItem.replacePlan
                        }
                    },
                    {
                        title: "Lắp bao cột",
                        field: 'installPlan',
                        width: '7%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: '#=sum#'
                    },
                    {
                        title: "Thay thân cột",
                        field: 'replacePlan',
                        width: '7%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: '#=sum#'
                    }
                ]
            },
            {
                title: "Xây dựng móng",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                columns: [
                    {
                        title: "Số đội cần",
                        field: 'teamBuildRequire',
                        width: '7%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: '#=sum#'
                    },
                    {
                        title: "Số đội đang XD",
                        field: 'teamBuildAvaiable',
                        width: '7%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: false,
                        aggregates: ["sum"],
                        footerTemplate: '#=sum#',
                    },
                    {
                        title: "Số đội cần tăng thêm",
                        field: 'teamBuildNeed',
                        width: '7%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: '#=data.teamBuildRequire.sum-data.teamBuildAvaiable.sum#',
                        template: function(data){
                            return data.teamBuildRequire-data.teamBuildAvaiable;
                        }
                    }
                ]
            },
            {
                title: "Lắp dựng",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                columns: [
                    {
                        title: "Số đội cần",
                        field: 'teamInstallRequire',
                        width: '7%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: '#=sum#'
                    },
                    {
                        title: "Số đội đang LD",
                        field: 'teamInstallAvaiable',
                        width: '7%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: false,
                        aggregates: ["sum"],
                        footerTemplate: '#=sum#',
                    },
                    {
                        title: "Số đội cần tăng thêm",
                        field: 'teamInstallNeed',
                        width: '7%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: '#=data.teamInstallRequire.sum-data.teamInstallAvaiable.sum#',
                        template: function(data){
                            return data.teamInstallRequire-data.teamInstallAvaiable;
                        }
                    }
                ]
            },
            {
                title: "Thao tác",
                template: dataItem =>
            '<div class="text-center">'
            +
            '<button style=" border: none; background-color: white;" id="removeId"' +
            'class=" icon_table" ng-click="vm.removeForceMaintainRow($event)" uib-tooltip="Xóa" translate' +
            '>' +
            '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
            '</button>'
            + '</div>',
            width:   '10%',
        field:    "action"}

],
dataSource: {
    schema: {
        //total: function (response) {
        //    return response.total; // total is returned in
        //    // the "total" field of
        //    // the response
        //},
        //data: function (response) {
        //    return response.data; // data is returned in
        //    // the "data" field of
        //    // the response
        //},
        model: {
            id: "tmpnForceMaintainId ",
                fields    :{
                stt:{
                    editable: false, nullable :   true
                },
                sysGroupName:{
                    editable: false, nullable :   true
                },
                buildPlan:{
                    editable: true, type
                :
                    "number", nullable
                :
                    false
                },
                installPlan:{
                    editable: true, type
                :
                    "number", nullable
                :
                    false
                },
                replacePlan:{
                    editable: true, type
                :
                    "number", nullable
                :
                    false
                },
                sumKht:{
                    editable: false, type
                :
                    "number", nullable
                :
                    true
                },
                teamBuildRequire :{
                    editable: true, type
                :
                    "number", nullable
                :
                    false
                },
                teamBuildAvaiable :{
                    editable: true,
                    type : "number",
                    nullable: false
                },
                teamInstallRequire :{
                    editable: true,
                    type : "number",
                    nullable: false
                },
                teamInstallAvaiable :{
                    editable: true,
                    type : "number",
                    nullable: false
                },
                teamInstallNeed :{
                    editable: false,
                    type : "number",
                    nullable: false
                },
                teamBuildNeed :{
                    editable: false,
                    type : "number",
                    nullable: false
                },
                action:{
                    editable: false, nullable
                :
                    true
                }
            }
        }
    },
//            serverPaging: true,
//            transport: {
//                read: {
//                    // Thuc hien viec goi service
//                    url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/doSearchForcemaintain",
//                    contentType: "application/json; charset=utf-8",
//                    type: "POST"
//                },
//                parameterMap: function (options, type) {
//                    vm.searchForm.page = options.page
//                    vm.searchForm.pageSize = options.pageSize
//                    vm.searchForm.totalMonthPlanId = vm.totalMonthPlan.totalMonthPlanId
//                    return JSON.stringify(vm.searchForm)
//                }
//            },
//            pageSize: 10
//
//,
    aggregate: [
        {field: "teamBuildNeed", aggregate: "sum"},
        {field: "teamInstallNeed", aggregate: "sum"},
        {field: "buildPlan", aggregate: "sum"},
        {field: "installPlan", aggregate: "sum"},
        {field: "replacePlan", aggregate: "sum"},
        {field: "sumKht", aggregate: "sum"},

        {field: "teamBuildRequire", aggregate: "sum"},
        {field: "teamBuildAvaiable", aggregate: "sum"},
        {field: "teamInstallRequire", aggregate: "sum"},
        {field: "teamInstallAvaiable", aggregate: "sum"},
    ]}});
}
vm.fillBTSTable =fillBTSTable;
function fillBTSTable(){
    vm.BTSGridOptions = kendoConfig.getGridOptions({
        autoBind: true,
        scrollable: false,
        resizable: true,
        editable:true,

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
                width: '15%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: false

            },
            {
                title: "Tổng số trạm",
                field: 'stationNumber',
                width: '10%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: true,
                aggregates: ["sum"],
                footerTemplate: '#=sum#'
            },

            {
                title: "Đội xây dựng cần",
                field: 'teamBuildRequire',
                width: '10%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: true,
                aggregates: ["sum"],
                footerTemplate: '#=sum#'
            },
            {
                title: "Số đội đang triển khai",
                width: '10%',
                field: 'teamBuildAvaiable',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: true,
                aggregates: ["sum"],
                footerTemplate: '#=sum#'
            },
            {
                title: "Thi công trực tiếp",
                width: '10%',
                field: 'selfImplementPercent',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;",
                },
                editable: true,
                template: function (data) {
                    if(Number(data.selfImplementPercent || 0) <= 100 && Number(data.selfImplementPercent || 0) >= 0){
                        return data.selfImplementPercent + '%';
                    }else if(Number(data.selfImplementPercent || 0) > 100){
                        return 100 + '%';
                    }else if(Number(data.selfImplementPercent || 0) < 0){
                        return 0 + '%';
                    }
                },
                aggregates: ["average"],
                footerTemplate:
                    function (data) {
                        if(Number(data.selfImplementPercent.average || 0) <= 100 && Number(data.selfImplementPercent.average || 0) >= 0){
                            return data.selfImplementPercent.average + "%";
                        } else if (Number(data.selfImplementPercent.average || 0) > 100) {
                            return 100 + '%';
                        } else if (Number(data.selfImplementPercent.average || 0) < 0) {
                            return 0 + '%';
                        }
                    }

            },
            {
                title: "Đối tác",
                width: '10%',
                field: 'themImplementPercent',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: false,
                template: function (data) {
                    if(Number(data.selfImplementPercent || 0) <= 100 && Number(data.selfImplementPercent || 0) >= 0) {
                        return (100 - Number(data.selfImplementPercent || 0)) + '%';
                    }else if(Number(data.selfImplementPercent || 0) > 100){
                        return 100 -  100  + '%';
                    }else if(Number(data.selfImplementPercent || 0) < 0){
                        return 0 + '%';
                    }
                },
                footerTemplate:  function (data) {
                    if(Number(data.selfImplementPercent.average || 0) <= 100 && Number(data.selfImplementPercent.average || 0) >= 0) {
                        return (100 - Number(data.selfImplementPercent.average || 0)) + '%';
                    }else if(Number(data.selfImplementPercent.average || 0) > 100){
                        return 100 -  100  + '%';
                    }else if(Number(data.selfImplementPercent.average || 0) < 0){
                        return 0 + '%';
                    }
                }
            },
            {
                title: "Số đội cần tăng thêm",
                width: '10%',
                field: 'teamBuildNeed',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: true,
                template: function (data) {
                    return (Number(data.teamBuildRequire || 0) - Number(data.teamBuildAvaiable || 0));
                },
                footerTemplate: function (data) {
                    return (Number(data.teamBuildRequire.sum || 0) - Number(data.teamBuildAvaiable.sum || 0));
                }
            },
            {
                title: "Ghi chú",
                width: '10%',

                field:'description' ,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                type :'text',
                editable:true,
                template: function (data) {
                    if(data.description==null){
                        return '';

                    }else{
                        return data.description;

                    }
                }


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
            width:   '10%',
        field:    "action"}

],
dataSource: {
    schema: {
        //total: function (response) {
        //    return response.total; // total is returned in
        //    // the "total" field of
        //    // the response
        //},
        //data: function (response) {
        //    return response.data; // data is returned in
        //    // the "data" field of
        //    // the response
        //},
        model: {
            id: "tmpnForceNewBtsId ",
                fields    :
            {
                stt:{
                    editable: false, nullable :   true
                }
            ,
                sysGroupName:{
                    editable: false, nullable :   true
                }
            ,
                stationNumber:{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                teamBuildRequire:{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                teamBuildAvaiable :{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                selfImplementPercent :{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                teamBuildNeed :{
                    editable: false, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                themImplementPercent :{
                    editable: false, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                description:{
                    editable: true, type
                        :
                        "text", nullable
                        :
                        true
                }
            ,
                action:{
                    editable: false, nullable
                :
                    true
                }
            }
        }
    },
//            serverPaging: true,
//            transport: {
//                read: {
//                    // Thuc hien viec goi service
//                    url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/doSearchBTS",
//                    contentType: "application/json; charset=utf-8",
//                    type: "POST"
//                },
//                parameterMap: function (options, type) {
//                    vm.searchForm.page = options.page
//                    vm.searchForm.pageSize = options.pageSize
//                    vm.searchForm.totalMonthPlanId = vm.totalMonthPlan.totalMonthPlanId
//                    return JSON.stringify(vm.searchForm)
//                }
//            },
//            pageSize: 10
//
//,
    aggregate: [
        {field: "stationNumber", aggregate: "sum"},
        {field: "teamBuildRequire", aggregate: "sum"},
        {field: "teamBuildAvaiable", aggregate: "sum"},
        {field: "teamBuildNeed", aggregate: "sum"},
        {field: "selfImplementPercent", aggregate: "average"},
        {field: "themImplementPercent", aggregate: "sum"},
    ]}});
}
vm.fillForceNewTable =fillForceNewTable;
function fillForceNewTable(){
    vm.forceNewGridOptions = kendoConfig.getGridOptions({
        autoBind: true,
        scrollable: false,
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
                width: '10%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: false

            },
            {
                title: "Tổng tuyến",
                field: 'stationNumber',
                width: '10%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: true,
                aggregates: ["sum"],
                footerTemplate: '#=sum#'
            },

            {
                title: "Tuyến",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                columns:[{

                title: "Có phép thi công",
                field: 'currentHaveLicense',
                width: '10%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: true,
                aggregates: ["sum"],
                footerTemplate: '#=sum#'
            },
            {
                title: "Khối lượng",
                width: '10%',
                field: 'currentQuantity',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: true,
                format: "{0:n3}",
                aggregates: ["sum"],
                footerTemplate: '#=sum#'
            },
            {
                title: "Hoàn thành tuyến",
                width: '10%',
                field: 'curentStationComplete',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: true,
                aggregates: ["sum"],
                footerTemplate: '#=sum#'
            }
            ]
        },
            {
                title: "Tuyến tồn",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                columns:[{

                title: "Có phép thi công",
                field: 'remainHaveLicense',
                width: '10%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: true,
                aggregates: ["sum"],
                footerTemplate: '#=sum#'
            },
            {
                title: "Khối lượng",
                width: '10%',
                field: 'remainQuantity',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: true,
                format: "{0:n3}",
                aggregates: ["sum"],
                footerTemplate: '#=sum#'
            },
            {
                title: "Hoàn thành tuyến",
                width: '10%',
                field: 'remainStationComplete',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: true,
                aggregates: ["sum"],
                footerTemplate: '#=sum#'
            }
            ]
        },
            {
                title: "Ghi chú",
                width: '10%',
                field: 'description',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: false,
            },
            {
                title: "Thao tác",
                template: dataItem =>
            '<div class="text-center">'
            +
            '<button style=" border: none; background-color: white;" id="removeId"' +
            'class=" icon_table" ng-click="vm.removeForceNewRow($event)" uib-tooltip="Xóa" translate' +
            '>' +
            '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
            '</button>'
            + '</div>',
            width:   '5%',
        field:    "action"}

],
dataSource: {
    schema: {
        //total: function (response) {
        //    return response.total; // total is returned in
        //    // the "total" field of
        //    // the response
        //},
        //data: function (response) {
        //    return response.data; // data is returned in
        //    // the "data" field of
        //    // the response
        //},
        model: {
            id: "tmpnForceNewId ",
                fields    :
            {
                stt:{
                    editable: false, nullable :   true
                }
            ,
                sysGroupName:{
                    editable: false, nullable :   true
                }
            ,
                stationNumber:{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                currentHaveLicense:{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                currentQuantity :{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                curentStationComplete :{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                remainHaveLicense :{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                remainQuantity :{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                remainStationComplete :{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                description:{
                    editable: true,
                }
            ,
                action:{
                    editable: false, nullable
                :
                    true
                }
            }
        }
    },

    //    serverPaging: true,
    //    transport: {
    //        read: {
    //            // Thuc hien viec goi service
    //            url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/doSearchForceNew",
    //            contentType: "application/json; charset=utf-8",
    //            type: "POST"
    //        },
    //        parameterMap: function (options, type) {
    //            vm.searchForm.page = options.page
    //            vm.searchForm.pageSize = options.pageSize
    //            vm.searchForm.totalMonthPlanId = vm.totalMonthPlan.totalMonthPlanId
    //            return JSON.stringify(vm.searchForm)
    //        }
    //    },
    //    pageSize: 10
    //,
    aggregate: [
                {field: "stationNumber", aggregate: "sum"},
                {field: "currentHaveLicense", aggregate: "sum"},
                {field: "currentQuantity", aggregate: "sum"},
                {field: "curentStationComplete", aggregate: "sum"},
                {field: "remainHaveLicense", aggregate: "sum"},
                {field: "remainQuantity", aggregate: "sum"},
                {field: "remainStationComplete", aggregate: "sum"},
            ]
}});
}
vm.fillFinanceTable=fillFinanceTable;
function fillFinanceTable() {
    vm.financeGridOptions = kendoConfig.getGridOptions({
        autoBind: true,
        scrollable: false,
        resizable: true,
        dataBinding: function () {
            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
        },
        //dataSource: {
        //    serverPaging: true,
        //    schema: {
        //        total: function (response) {
        //            return response.total; // total is returned in
        //            // the "total" field of
        //            // the response
        //        },
        //        data: function (response) {
        //            return response.data; // data is returned in
        //            // the "data" field of
        //            // the response
        //        }
        //    },
        //    transport: {
        //        read: {
        //            // Thuc hien viec goi service
        //            url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/doSearchFinance",
        //            contentType: "application/json; charset=utf-8",
        //            type: "POST"
        //        },
        //        parameterMap: function (options, type) {
        //            vm.searchForm.page = options.page
        //            vm.searchForm.pageSize = options.pageSize
        //            vm.searchForm.totalMonthPlanId = vm.totalMonthPlan.totalMonthPlanId
        //            return JSON.stringify(vm.searchForm)
        //        }
        //    },
        //    pageSize: 10
        //},
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
                width: '30%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: false

            },
            {
                title: "Dòng tiền đảm bảo",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                columns:[{
                title: "Đợt 1",
                field: 'firstTimes',
                width: '15%',
                format: "{0:n3}",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: true,
                aggregates: ["sum"],
                footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
            },

            {
                title: "Đợt 2",
                field: 'secondTimes',
                width: '15%',
                format: "{0:n3}",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: false,
                aggregates: ["sum"],
                footerTemplate: '#= kendo.toString(kendo.parseFloat(sum) ? kendo.parseFloat(sum) : 0, \"n3\") #'
            },
            {
                title: "Đợt 3",
                width: '15%',
                format: "{0:n3}",
                field: 'threeTimes',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: false,
                aggregates: ["sum"],
                footerTemplate: '#= kendo.toString(kendo.parseFloat(sum) ? kendo.parseFloat(sum) : 0, \"n3\") #'
            },
            ]
    },
            {
                title: "Ghi chú",
                width: '15%',
                field: 'description',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: true,
            },
            {
                title: "Thao tác",
                template: dataItem =>
            '<div class="text-center">'
            +
            '<button style=" border: none; background-color: white;" id="removeId"' +
            'class=" icon_table" ng-click="vm.removeFinanceRow($event)" uib-tooltip="Xóa" translate' +
            '>' +
            '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
            '</button>'
            + '</div>',
            width:   '10%',
        field:    "action"}

],
dataSource: {
    schema: {
        //total: function (response) {
        //    return response.total; // total is returned in
        //    // the "total" field of
        //    // the response
        //},
        //data: function (response) {
        //    return response.data; // data is returned in
        //    // the "data" field of
        //    // the response
        //},
        model: {
            id: "tmpnSourceId ",
                fields    :
            {
                stt:{
                    editable: false, nullable :   true
                }
            ,
                sysGroupName:{
                    editable: false, nullable :   true
                }
            ,
                firstTimes:{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                secondTimes:{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                threeTimes :{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                description:{
                    editable: true,
                }
            ,
                action:{
                    editable: false, nullable
                :
                    true
                }
            }
        }
    },

//        serverPaging: true,
//        transport: {
//            read: {
//                // Thuc hien viec goi service
//                url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/doSearchFinance",
//                contentType: "application/json; charset=utf-8",
//                type: "POST"
//            },
//            parameterMap: function (options, type) {
//                vm.searchForm.page = options.page
//                vm.searchForm.pageSize = options.pageSize
//                vm.searchForm.totalMonthPlanId = vm.totalMonthPlan.totalMonthPlanId
//                return JSON.stringify(vm.searchForm)
//            }
//        },
//        pageSize: 10
//
//,
    aggregate: [
        {field: "firstTimes", aggregate: "sum"},
        {field: "secondTimes", aggregate: "sum"},
        {field: "threeTimes", aggregate: "sum"},
    ]}});
}
vm.fillContractTable=fillContractTable;
function fillContractTable() {
    vm.contractGridOptions = kendoConfig.getGridOptions({
        autoBind: true,
        scrollable: false,
        resizable: true,
        dataBinding: function () {
            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
        },
        //dataSource: {
        //    serverPaging: true,
        //    schema: {
        //        total: function (response) {
        //            return response.total; // total is returned in
        //            // the "total" field of
        //            // the response
        //        },
        //        data: function (response) {
        //            return response.data; // data is returned in
        //            // the "data" field of
        //            // the response
        //        }
        //    },
        //    transport: {
        //        read: {
        //            // Thuc hien viec goi service
        //            url: Constant.BASE_SERVICE_URL + "TotalMonthPlanRsService/doSearchContract",
        //            contentType: "application/json; charset=utf-8",
        //            type: "POST"
        //        },
        //        parameterMap: function (options, type) {
        //            vm.searchForm.page = options.page
        //            vm.searchForm.pageSize = options.pageSize
        //            vm.searchForm.totalMonthPlanId = vm.totalMonthPlan.totalMonthPlanId
        //            return JSON.stringify(vm.searchForm)
        //        }
        //    },
        //    pageSize: 10
        //},
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
                width: '30%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: false

            },
            {
                title: "HSHC",
                field: 'complete',
                width: '15%',
                format: "{0:n3}",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: true,
                aggregates: ["sum"],
                footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
            },
            {
                title: "Nguồn đủ điều kiện",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                columns:[{
                title: "HSHC đủ điều kiện",
                field: 'enoughCondition',
                width: '15%',
                format: "{0:n3}",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: true,
                aggregates: ["sum"],
                footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
            },

            {
                title: "Phải đối soát vật tư",
                field: 'noEnoughCondition',
                width: '15%',
                format: "{0:n3}",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: false,
                aggregates: ["sum"],
                footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
            },
            {
                title: "Không phải đối soát",
                width: '15%',
                format: "{0:n3}",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: false,
                aggregates: ["sum"],
                footerTemplate: function(data){
                        return kendo.toString(kendo.parseFloat(Number(data.enoughCondition.sum||0)-Number(data.noEnoughCondition.sum||0)), "n3");
                },
                template: function(data){
                    return Number(data.enoughCondition||0)-Number(data.noEnoughCondition||0);
            },
            }]
    },
            {
                title: "Ghi chú",
                width: '10%',
                editable: true,
                field: 'description',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: true,
            },
            {
                title: "Thao tác",
                template: dataItem =>
            '<div class="text-center">'
            +
            '<button style=" border: none; background-color: white;" id="removeId"' +
            'class=" icon_table" ng-click="vm.removeContractRow($event)" uib-tooltip="Xóa" translate' +
            '>' +
            '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
            '</button>'
            + '</div>',
            width:   '10%',
        field:    "action"}

],
dataSource: {
    schema: {
        //total: function (response) {
        //    return response.total; // total is returned in
        //    // the "total" field of
        //    // the response
        //},
        //data: function (response) {
        //    return response.data; // data is returned in
        //    // the "data" field of
        //    // the response
        //},
        model: {
            id: "tmpnSourceId ",
                fields    :
            {
                stt:{
                    editable: false, nullable :   true
                }
            ,
                sysGroupName:{
                    editable: false, nullable :   true
                }
            ,
                complete:{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                enoughCondition:{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                noEnoughCondition :{
                    editable: true, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                tempEnoughCondition :{
                    editable: false, type
                :
                    "number", nullable
                :
                    true
                }
            ,
                description:{
                    editable: true,
                }
            ,
                action:{
                    editable: false, nullable
                :
                    true
                }
            }
        }
    },

//        serverPaging: true,
//        transport: {
//            read: {
//                // Thuc hien viec goi service
//                url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/doSearchContract",
//                contentType: "application/json; charset=utf-8",
//                type: "POST"
//            },
//            parameterMap: function (options, type) {
//                vm.searchForm.page = options.page
//                vm.searchForm.pageSize = options.pageSize
//                vm.searchForm.totalMonthPlanId = vm.totalMonthPlan.totalMonthPlanId
//                return JSON.stringify(vm.searchForm)
//            }
//        },
//        pageSize: 10
//
//,
    aggregate: [
        {field: "tempEnoughCondition", aggregate: "sum"},
        {field: "noEnoughCondition", aggregate: "sum"},
        {field: "enoughCondition", aggregate: "sum"},
        {field: "complete", aggregate: "sum"},
    ]}});
}
vm.fillMaterialTable = fillMaterialTable
function fillMaterialTable() {
    vm.materialGridOptions = kendoConfig.getGridOptions({
        autoBind: true,
        scrollable: false,
        resizable: true,
        editable: true,
        dataBinding: function () {
            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
        },
        //dataSource: {
        //    serverPaging: true,
        //    schema: {
        //        total: function (response) {
        //            return response.total; // total is returned in
        //            // the "total" field of
        //            // the response
        //        },
        //        data: function (response) {
        //            return response.data; // data is returned in
        //            // the "data" field of
        //            // the response
        //        }
        //    },
        //    transport: {
        //        read: {
        //            // Thuc hien viec goi service
        //            url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/doSearchMaterial",
        //            contentType: "application/json; charset=utf-8",
        //            type: "POST"
        //        },
        //        parameterMap: function (options, type) {
        //            vm.searchForm.page = options.page
        //            vm.searchForm.pageSize = options.pageSize
        //            vm.searchForm.totalMonthPlanId = vm.totalMonthPlan.totalMonthPlanId
        //            return JSON.stringify(vm.searchForm)
        //        }
        //    },
        //    pageSize: 10
        //},
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
                width: '25%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: false

            },
            {
                title: "Loại công trình",
                field: 'catConstructionTypeName',
                width: '20%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: false,
            },
            {
                title: "Hình thức triển khai",
                field: 'catConstructionDeployName',
                width: '20%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: false,
            },

            {
                title: "Ghi chú",
                width: '20%',
                field: 'description',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: true,
                type: "text"
            },
            {
                title: "Thao tác",
                template: dataItem =>
                '<div class="text-center">'
                +
                '<button style=" border: none; background-color: white;" id="removeId"' +
                'class=" icon_table" ng-click="vm.removeMaterialRow($event)" uib-tooltip="Xóa" translate' +
                '>' +
                '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
                '</button>'
                + '</div>',
                width:   '10%',
                field:    "action"}
],
        dataSource: {
            schema: {
                model: {
                    id: "tmpnSourceId ",
                    fields    :
                    {
                        stt:{
                            editable: false, nullable :   true
                        }
                        ,
                        sysGroupName:{
                            editable: false, nullable :   true
                        }
                        ,
                        catConstructionTypeName:{
                            editable: false, nullable :   true
                        }
                        ,
                        catConstructionDeployName:{
                            editable: false, nullable :   true
                        }
                        ,
                        description:{
                            editable: true,
                        }
                        ,
                        action:{
                            editable: false, nullable
                                :
                                true
                        }
                    }
                }
            },
            aggregate: [
                {field: "tempEnoughCondition", aggregate: "sum"},
                {field: "noEnoughCondition", aggregate: "sum"},
                {field: "enoughCondition", aggregate: "sum"},
                {field: "complete", aggregate: "sum"},
            ]}});
}
    vm.fillAppendixTable=fillAppendixTable;
    function fillAppendixTable(data) {
        vm.appendixGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            //dataSource: {
            //    serverPaging: true,
            //    schema: {
            //        total: function (response) {
            //            return response.total; // total is returned in
            //            // the "total" field of
            //            // the response
            //        },
            //        data: function (response) {
            //            return response.data; // data is returned in
            //            // the "data" field of
            //            // the response
            //        }
            //    },
            //    transport: {
            //        read: {
            //            // Thuc hien viec goi service
            //            url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/doSearchAppendix",
            //            contentType: "application/json; charset=utf-8",
            //            type: "POST"
            //        },
            //        parameterMap: function (options, type) {
            //            vm.searchForm.page = options.page
            //            vm.searchForm.pageSize = options.pageSize
            //            vm.searchForm.totalMonthPlanId = vm.totalMonthPlan.totalMonthPlanId
            //            return JSON.stringify(vm.searchForm)
            //        }
            //    },
            //    pageSize: 10
            //},
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
                }
                , {
                    title: "Tên file",
                    field: 'name',
                    width: 150,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    },
                    template :  function(dataItem) {
                        return "<a href='' ng-click='vm.downloadFile(dataItem)'>" + dataItem.name + "</a>";
                    },
                    editable: false
                },
              {
                    title: "Loại file",
                    field: 'appParam',
                    width: 150,
                    editor: categoryDropDownEditor,
                    template: "#=appParam.name#" ,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        "id":"appFile",
                        style: "text-align:left;"
                    },editable:true,
                  type:"text"
                },
                {
                    title: "Thao tác",
                    editable: false,
                    template: dataItem =>
                    '<div class="text-center">'
                    +
                    '<button style=" border: none; background-color: white;" id="removeId"' +
                    'class=" icon_table" ng-click="vm.removeAppendixRow($event)" uib-tooltip="Xóa" translate' +
                    '>' +
                    '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
                    '</button>'
                    + '</div>',
                    width:   '10%',
                    field:    "action"}

            ]
        });
    }
vm.removeAppendixRow = removeAppendixRow;
function removeAppendixRow(e) {
    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
        toastr.success("Xóa thành công!");
        var grid = vm.appendixGrid;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
        grid.removeRow(dataItem); //just gives alert message
        grid.dataSource.remove(dataItem); //removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
    })
}
vm.removeMaterialRow = removeMaterialRow;
function removeMaterialRow(e) {
    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
        toastr.success("Xóa thành công!");
        var grid = vm.materialGrid;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
//        grid.removeRow(dataItem); //just gives alert message
        grid.dataSource.remove(dataItem); //removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
    })
}
vm.removeContractRow = removeContractRow;
function removeContractRow(e) {
    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
        toastr.success("Xóa thành công!");
        var grid = vm.contractGrid;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
        grid.removeRow(dataItem); //just gives alert message
        grid.dataSource.remove(dataItem); //removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
    })
}
vm.removeFinanceRow = removeFinanceRow;
function removeFinanceRow(e) {
    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
        toastr.success("Xóa thành công!");
        var grid = vm.financeGrid;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
        grid.removeRow(dataItem); //just gives alert message
        grid.dataSource.remove(dataItem); //removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
    })
}
vm.removeForceNewRow = removeForceNewRow;
function removeForceNewRow(e) {
    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
        toastr.success("Xóa thành công!");
        var grid = vm.forceNewGrid;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
        grid.removeRow(dataItem); //just gives alert message
        grid.dataSource.remove(dataItem); //removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
    })
}
vm.removeBTSRow = removeBTSRow;
function removeBTSRow(e) {
    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
        toastr.success("Xóa thành công!");
        var grid = vm.BTSGrid;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
        grid.removeRow(dataItem); //just gives alert message
        grid.dataSource.remove(dataItem); //removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
    })
}
vm.removeForceMaintainRow = removeForceMaintainRow;
function removeForceMaintainRow(e) {
    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
        toastr.success("Xóa thành công!");
        var grid = vm.forceMaintainGrid;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
        grid.removeRow(dataItem); //just gives alert message
        grid.dataSource.remove(dataItem); //removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
    })
}
vm.removeTargetRow = removeTargetRow;
function removeTargetRow(e) {
    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
        var grid = vm.targetGrid;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
        grid.removeRow(dataItem); //just gives alert message
        grid.dataSource.remove(dataItem); //removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
    })
}
vm.removeSourceRow = removeSourceRow;
function removeSourceRow(e) {
    confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function() {
        toastr.success("Xóa thành công!");
        var grid = vm.sourceGrid;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
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
        vm.totalMonthPlanTemp.sysGroupName = data.text;
        vm.totalMonthPlanTemp.sysGroupId = data.id;
    }else if (vm.departmentpopUp === 'deptTarget') {
        vm.totalMonthPlanTemp.sysGroupName = data.text;
        vm.totalMonthPlanTemp.sysGroupId = data.id;
        getYearPlanTarget();
    }else if (vm.departmentpopUp === 'deptSource') {
        vm.totalMonthPlanTemp.sysGroupName = data.text;
        vm.totalMonthPlanTemp.sysGroupId = data.id;
        getYearPlanSource();
    }

}
function getYearPlanTarget(){
    var obj={
        month:vm.totalMonthPlan.month,
        year:vm.totalMonthPlan.year,
        sysGroupId:vm.totalMonthPlanTemp.sysGroupId
    }
    totalMonthPlanService.getYearPlanDetail(obj).then(function(result){
        if(result!=undefined){
            vm.yearPlan = result;
            vm.totalMonthPlanTemp.quantity1 = result.quantity
            vm.totalMonthPlanTemp.complete1 = result.complete
            vm.totalMonthPlanTemp.revenue1 = result.revenue
            vm.totalMonthPlanTemp.quantity = result.quantity
            vm.totalMonthPlanTemp.complete = result.complete
            vm.totalMonthPlanTemp.revenue = result.revenue

            if(vm.totalMonthPlanTemp.quantity == null ||vm.totalMonthPlanTemp.quantity == undefined){
                vm.totalMonthPlanTemp.quantity = 0;
            }
            if(vm.totalMonthPlanTemp.complete == null || vm.totalMonthPlanTemp.complete ==undefined ){
                vm.totalMonthPlanTemp.complete = 0;
            }
            if(vm.totalMonthPlanTemp.revenue == null || vm.totalMonthPlanTemp.revenue == undefined){
                vm.totalMonthPlanTemp.revenue = 0;
            }
            calculateYearPlanDifferent();
        }else{
            //vm.different={quantity:0,complete:0,revenue:0};
            //vm.yearPlan={quantity:0,complete:0,revenue:0};
            //vm.totalMonthPlanTemp.quantity = 0
            //vm.totalMonthPlanTemp.complete = 0
            //vm.totalMonthPlanTemp.revenue = 0
        }
    },function(error) {
    })
}
function getYearPlanSource() {
    var obj = {
        month: vm.totalMonthPlan.month,
        year: vm.totalMonthPlan.year,
        sysGroupId: vm.totalMonthPlanTemp.sysGroupId
    }
    totalMonthPlanService.getYearPlanDetail(obj).then(function (result) {
        if(result!=undefined) {
            vm.yearPlanSource = result;
            vm.totalMonthPlanTemp.source1 = result.source
            vm.totalMonthPlanTemp.source = result.source
            calculateYearPlanDifferentSource();
        }else{
            vm.differentSource={source:0};
            vm.yearPlanSource={source:0};
            vm.totalMonthPlanTemp.source = 0
        }
    }, function (error) {

    })
}
// clear data
vm.changeDataDept = changeDataDept
function changeDataDept(id) {
    switch (id) {
        case 'dept':
        {
            if (processSearch(id, vm.selectedDeptChoose)) {
                vm.totalMonthPlanTemp.sysGroupId = null;
                vm.totalMonthPlanTemp.sysGroupName = null;
                vm.selectedDeptChoose = false;
            }
            break;
        }
        case 'deploy':
        {
            if (processSearch(id, vm.selectedDeployChoose)) {
                vm.totalMonthPlanTemp.catConstructionDeployId = null;
                vm.totalMonthPlanTemp.catConstructionDeployName = null;
                vm.selectedDeployChoose = false;
            }
            break;
        }
        case 'type':
        {
            if (processSearch(id, vm.selectedTypeChoose)) {
                vm.totalMonthPlanTemp.catConstructionTypeId = null;
                vm.totalMonthPlanTemp.catConstructionTypeName = null;
                vm.selectedTypeChoose = false;
            }
            break;
        }
    }
}

vm.cancelInputDept = function (param) {
    if (param == 'dept') {
        vm.totalMonthPlanTemp.sysGroupId = null;
        vm.totalMonthPlanTemp.sysGroupName = null;
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
        vm.totalMonthPlanTemp.sysGroupName = dataItem.text;
        vm.totalMonthPlanTemp.sysGroupId = dataItem.id;
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
                    name: vm.totalMonthPlanTemp.sysGroupName,
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
            vm.totalMonthPlanTemp.sysGroupName = null;// thành name
            vm.totalMonthPlanTemp.sysGroupId = null;
        }
    },
    ignoreCase: false
}
vm.deptOptionsTarget = {
    dataTextField: "text",
    dataValueField: "id",
    select: function (e) {
        vm.selectedDeptChoose = true;
        var dataItem = this.dataItem(e.item.index());
        vm.totalMonthPlanTemp.sysGroupName = dataItem.text;
        vm.totalMonthPlanTemp.sysGroupId = dataItem.id;
        var obj={
            month:vm.totalMonthPlan.month,
            year:vm.totalMonthPlan.year,
            sysGroupId:dataItem.id
        }

        totalMonthPlanService.getYearPlanDetail(obj).then(function(result){
            if(result!=undefined) {
                vm.yearPlan = result;
                vm.totalMonthPlanTemp.quantity1 = result.quantity
                vm.totalMonthPlanTemp.complete1 = result.complete
                vm.totalMonthPlanTemp.revenue1 = result.revenue
                vm.totalMonthPlanTemp.quantity = result.quantity
                vm.totalMonthPlanTemp.complete = result.complete
                vm.totalMonthPlanTemp.revenue = result.revenue
                calculateYearPlanDifferent();
            }else{
                vm.different={quantity:0,complete:0,revenue:0};
                vm.yearPlan={quantity:0,complete:0,revenue:0};
                vm.totalMonthPlanTemp.quantity = 0
                vm.totalMonthPlanTemp.complete = 0
                vm.totalMonthPlanTemp.revenue = 0
            }
            if(vm.totalMonthPlanTemp.quantity == null ||vm.totalMonthPlanTemp.quantity == undefined){
                vm.totalMonthPlanTemp.quantity = null;
            }
            if(vm.totalMonthPlanTemp.complete == null || vm.totalMonthPlanTemp.complete ==undefined ){
                vm.totalMonthPlanTemp.complete = null;
            }
            if(vm.totalMonthPlanTemp.revenue == null || vm.totalMonthPlanTemp.revenue == undefined){
                vm.totalMonthPlanTemp.revenue = null;
            }

        },function(error){

        });
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
                    name: vm.totalMonthPlanTemp.sysGroupName,
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
            vm.totalMonthPlanTemp.sysGroupName = null;// thành name
            vm.totalMonthPlanTemp.sysGroupId = null;
        }
    },
    ignoreCase: false
}
vm.deptOptionsSource = {
    dataTextField: "text",
    dataValueField: "id",
    select: function (e) {
        vm.selectedDeptChoose = true;
        var dataItem = this.dataItem(e.item.index());
        vm.totalMonthPlanTemp.sysGroupName = dataItem.text;
        vm.totalMonthPlanTemp.sysGroupId = dataItem.id;
        var obj={
            month:vm.totalMonthPlan.month,
            year:vm.totalMonthPlan.year,
            sysGroupId:dataItem.id
        }
        totalMonthPlanService.getYearPlanDetail(obj).then(function(result){
                if(result!=undefined) {
                    vm.yearPlanSource = result;
                    vm.totalMonthPlanTemp.source1 = result.source
                    vm.totalMonthPlanTemp.source = result.source
                    calculateYearPlanDifferentSource();
                }else{
                    vm.differentSource={source:0};
                    vm.yearPlanSource={source:0};
                    vm.totalMonthPlanTemp.source = 0
                }
        },function(error){

        });
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
                    name: vm.totalMonthPlanTemp.sysGroupName,
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
            vm.totalMonthPlanTemp.sysGroupName = null;// thành name
            vm.totalMonthPlanTemp.sysGroupId = null;
        }
    },
    ignoreCase: false
}
    vm.calculateYearPlanDifferent=calculateYearPlanDifferent;
    function calculateYearPlanDifferent(key){

        if('quantity'==key){
                if(vm.totalMonthPlanTemp.quantity == null ||vm.totalMonthPlanTemp.quantity == undefined || vm.totalMonthPlanTemp.quantity ==''){
                    vm.totalMonthPlanTemp.quantity = 0;
                }

            if(vm.totalMonthPlanTemp.quantity != null && vm.totalMonthPlanTemp.quantity != undefined && vm.totalMonthPlanTemp.quantity != '') {
                vm.different.quantity = Number(kendo.parseFloat(vm.totalMonthPlanTemp.quantity) || 0) - Number(vm.yearPlan.quantity || 0);
            }

        }
        else if('complete'==key){
            if(vm.totalMonthPlanTemp.complete == null || vm.totalMonthPlanTemp.complete ==undefined|| vm.totalMonthPlanTemp.complete =='' ){
                vm.totalMonthPlanTemp.complete = 0;
            }

            vm.different.complete = Number(kendo.parseFloat(vm.totalMonthPlanTemp.complete)||0)-Number(vm.yearPlan.complete||0);
        }
        else if('revenue'==key){
            if(vm.totalMonthPlanTemp.revenue == null || vm.totalMonthPlanTemp.revenue == undefined || vm.totalMonthPlanTemp.revenue ==''){
                vm.totalMonthPlanTemp.revenue = 0;
            }
            vm.different.revenue = Number(kendo.parseFloat(vm.totalMonthPlanTemp.revenue)||0)-Number(vm.yearPlan.revenue||0);
        }else{
            vm.different.quantity = Number(kendo.parseFloat(vm.totalMonthPlanTemp.quantity)||0)-Number(vm.yearPlan.quantity||0);
            vm.different.complete = Number(kendo.parseFloat(vm.totalMonthPlanTemp.complete)||0)-Number(vm.yearPlan.complete||0);
            vm.different.revenue = Number(kendo.parseFloat(vm.totalMonthPlanTemp.revenue)||0)-Number(vm.yearPlan.revenue||0);
        }
    }
    vm.calculateYearPlanDifferentSource =calculateYearPlanDifferentSource;
    function calculateYearPlanDifferentSource(){
        vm.differentSource.source = Number(kendo.parseFloat(vm.totalMonthPlanTemp.source)||0)-Number(vm.yearPlanSource.source||0);
    }
vm.cancelImport = cancelImport;
function cancelImport() {
    CommonService.dismissPopup1();
}
vm.cancelYearDetail = cancelYearDetail;
function cancelYearDetail() {
    CommonService.dismissPopup1();
}

vm.importExcelTab2 = importExcelTab2;
function importExcelTab2() {
    var teamplateUrl = "coms/totalMonthPlan/import-popup.html";
    var title = "Import từ file excel";
    var windowId = "TEMPLATE";
    CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '700', 'auto', "code");
}

vm.addMaterialTemp = addMaterialTemp;
function addMaterialTemp() {
    if (validateMaterialTemp()) {
        var grid = vm.materialGrid;
        if (grid) {
            var detailList = grid.dataSource._data;
            var valid = true;
            angular.forEach(detailList, function (dataItem) {
            		//chinhpxn20180705_start
                    if(vm.totalMonthPlanTemp.sysGroupId == dataItem.sysGroupId && vm.totalMonthPlanTemp.catConstructionTypeId==dataItem.catConstructionTypeId && vm.totalMonthPlanTemp.catConstructionDeployId==dataItem.catConstructionDeployId ){
                        valid = false;
                    }
//                    else if(vm.totalMonthPlanTemp.catConstructionTypeId==dataItem.catConstructionTypeId){
//                        toastr.warning("Loại công trình " + vm.totalMonthPlanTemp.catConstructionTypeName + " đã tồn tại!");
//                        valid = false;
//                    }else if(vm.totalMonthPlanTemp.catConstructionDeployId==dataItem.catConstructionDeployId){
//                        toastr.warning("Loại công trình " + vm.totalMonthPlanTemp.catConstructionDeployName + " đã tồn tại!");
//                        valid = false;
//                    }
                    //chinhpxn20180705_end
            })
            if (valid) {
                grid.dataSource.add(vm.totalMonthPlanTemp);
                grid.dataSource.sync();
                grid.refresh();
                vm.totalMonthPlanTemp={};

            } else {
            	//chinhpxn20180705_start
                toastr.warning("Đơn vị " + vm.totalMonthPlanTemp.sysGroupName + ", Loại công trình " + vm.totalMonthPlanTemp.catConstructionTypeName
                		+ ", Hình thức triển khai " + vm.totalMonthPlanTemp.catConstructionDeployName + " đã cùng tồn tại!");
                //chinhpxn20180705_end
            }
        }
    }
}
vm.addContractTemp = addContractTemp;
function addContractTemp() {
    if (validateContractTemp()) {
        var grid = vm.contractGrid;
        if (grid) {
            var detailList = grid.dataSource._data;
            var valid = true;
            angular.forEach(detailList, function (dataItem) {
                if (vm.totalMonthPlanTemp.sysGroupId == dataItem.sysGroupId) {
                    valid = false;
                }
            })
            if (valid) {
                grid.dataSource.insert(0, vm.totalMonthPlanTemp);
                grid.dataSource.sync();
                grid.refresh();
                vm.totalMonthPlanTemp={};
            } else {
                toastr.warning("Đơn vị " + vm.totalMonthPlanTemp.sysGroupName + " đã tồn tại!");
            }
        }
    }
}
vm.addFinanceTemp = addFinanceTemp;
function addFinanceTemp() {
    if (validateFinanceTemp()) {
        var grid = vm.financeGrid;
        if (grid) {
            var detailList = grid.dataSource._data;
            var valid = true;
            angular.forEach(detailList, function (dataItem) {
                if (vm.totalMonthPlanTemp.sysGroupId == dataItem.sysGroupId) {
                    valid = false;
                }
            })
            if (valid) {

                /*if(vm.totalMonthPlanTemp.secondTimes==null){
                    vm.totalMonthPlanTemp.secondTimes1=0;
                    vm.totalMonthPlanTemp.secondTimes=0;

                }
                if(vm.totalMonthPlanTemp.threeTimes==null){
                    vm.totalMonthPlanTemp.threeTimes1=0;
                    vm.totalMonthPlanTemp.threeTimes=0;

                }*/


                grid.dataSource.insert(0, vm.totalMonthPlanTemp);
                grid.dataSource.sync();
                grid.refresh();
                //vm.totalMonthPlanTemp={};
            } else {
                toastr.warning("Đơn vị " + vm.totalMonthPlanTemp.sysGroupName + " đã tồn tại!");
            }
        }
        vm.cancelInputDept('dept');
        vm.totalMonthPlanTemp.threeTimes = "";
        vm.totalMonthPlanTemp.secondTimes = "";
        vm.totalMonthPlanTemp.firstTimes = "";
        vm.totalMonthPlanTemp.description = "";
    }
}
vm.addForceNewTemp = addForceNewTemp;
function addForceNewTemp() {
    if (validateForceNewTemp()) {
        var grid = vm.forceNewGrid;
        if (grid) {
            var detailList = grid.dataSource._data;
            var valid = true;
            angular.forEach(detailList, function (dataItem) {
                if (vm.totalMonthPlanTemp.sysGroupId == dataItem.sysGroupId) {
                    valid = false;
                }
            })
            if (valid) {
                grid.dataSource.insert(0, vm.totalMonthPlanTemp);
                grid.dataSource.sync();
                grid.refresh();
                vm.totalMonthPlanTemp={};
            } else {
                toastr.warning("Đơn vị " + vm.totalMonthPlanTemp.sysGroupName + " đã tồn tại!");
            }
        }
    }
}
vm.addBTSTemp = addBTSTemp;
function addBTSTemp() {
    if (validateBTSTemp()) {
        var grid = vm.BTSGrid;
        if (grid) {
            var detailList = grid.dataSource._data;
            var valid = true;
            angular.forEach(detailList, function (dataItem) {
                if (vm.totalMonthPlanTemp.sysGroupId == dataItem.sysGroupId) {
                    valid = false;
                }
            })
            if (valid) {
                grid.dataSource.insert(0, vm.totalMonthPlanTemp);
                grid.dataSource.sync();
                grid.refresh();
                vm.totalMonthPlanTemp={};
            } else {
                toastr.warning("Đơn vị " + vm.totalMonthPlanTemp.sysGroupName + " đã tồn tại!");
            }
        }
    }
}
vm.addTargetTemp = addTargetTemp;
function addTargetTemp() {
	debugger
    if (validateTargetTemp()) {
        var obj={
            month:vm.totalMonthPlan.month,
            year:vm.totalMonthPlan.year,
            sysGroupId:vm.totalMonthPlanTemp.sysGroupId
        }
        Restangular.all("totalMonthPlanRsService/getByIdTarget").post(obj).then(function(data){
    if(data!=undefined){
    vm.totalMonthPlanTemp.quantityLk = data.quantityLk;
    vm.totalMonthPlanTemp.completeLk = data.completeLk;
    vm.totalMonthPlanTemp.revenueLk = data.revenueLk;
    vm.totalMonthPlanTemp.quantityInYear = data.quantityInYear;
    vm.totalMonthPlanTemp.completeInYear = data.completeInYear;
    vm.totalMonthPlanTemp.revenueInYear = data.revenueInYear;


}

            var grid = vm.targetGrid;
            if (grid) {
                var detailList = grid.dataSource._data;
                var valid = true;
                angular.forEach(detailList, function (dataItem) {
                    if (vm.totalMonthPlanTemp.sysGroupId == dataItem.sysGroupId) {
                        valid = false;
                    }
                })
                if (valid) {
                    //grid.dataSource.add(vm.totalMonthPlanTemp);
                    grid.dataSource.insert(0, vm.totalMonthPlanTemp);
                    grid.dataSource.sync();
                    grid.refresh();
                    vm.totalMonthPlanTemp={};
                    vm.yearPlan={};
                    vm.different={};
                } else {
                    toastr.warning("Đơn vị " + vm.totalMonthPlanTemp.sysGroupName + " đã tồn tại!");
                }
            }
        },function(error){

        });

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
                if (vm.totalMonthPlanTemp.sysGroupId == dataItem.sysGroupId) {
                    valid = false;
                }
            })
            if (valid) {
                var gridTar = vm.targetGrid;
                if(gridTar){
                    var detailList = gridTar.dataSource._data;
                    angular.forEach(detailList, function (dataItem) {
                        if (vm.totalMonthPlanTemp.sysGroupId == dataItem.sysGroupId) {
                            vm.totalMonthPlanTemp.quantity =dataItem.quantity;
                        }
                    })
                }

                vm.totalMonthPlanTemp.difference = Number(kendo.parseFloat(vm.totalMonthPlanTemp.source)||0)- Number(vm.totalMonthPlanTemp.quantity||0)
                grid.dataSource.insert(0, vm.totalMonthPlanTemp);
                grid.dataSource.sync();
                grid.refresh();
                vm.totalMonthPlanTemp={};
                vm.yearPlanSource={};
                vm.differentSource={};

            } else {
                toastr.warning("Đơn vị " + vm.totalMonthPlanTemp.sysGroupName + " đã tồn tại!");
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
                if (vm.totalMonthPlanTemp.sysGroupId == dataItem.sysGroupId) {
                    valid = false;
                    toastr.warning("Đơn vị đã tồn tại!");
                }
            })
            if (valid) {
                //vm.totalMonthPlanTemp.sumKht= Number(vm.totalMonthPlanTemp.installPlan || 0)+Number(vm.totalMonthPlanTemp.replacePlan||0)
                grid.dataSource.insert(0, vm.totalMonthPlanTemp);
                grid.dataSource.sync();
                grid.refresh();
                vm.totalMonthPlanTemp={}
            }
        }
    }
}
function validateForceNewTemp() {
    if (vm.totalMonthPlanTemp.sysGroupId == undefined) {
        toastr.warning("Bạn phải chọn đơn vị!");
        return false;
    }
    if (vm.totalMonthPlanTemp.stationNumber == undefined||vm.totalMonthPlanTemp.stationNumber == '') {
        toastr.warning("Bạn phải nhập tổng tuyến!");
        return false;
    }
    if (vm.totalMonthPlanTemp.currentHaveLicense == undefined||vm.totalMonthPlanTemp.currentHaveLicense == '') {
        toastr.warning("Bạn phải nhập có phép thi công");
        return false;
    }
    if (vm.totalMonthPlanTemp.currentQuantity == undefined||vm.totalMonthPlanTemp.currentQuantity == '') {
        toastr.warning("Bạn phải nhập khối lượng");
        return false;
    }
    if (vm.totalMonthPlanTemp.curentStationComplete == undefined||vm.totalMonthPlanTemp.curentStationComplete == '') {
        toastr.warning("Bạn phải nhập hoàn thành tuyến");
        return false;
    }
    if (vm.totalMonthPlanTemp.remainHaveLicense == undefined||vm.totalMonthPlanTemp.remainHaveLicense == '') {
        toastr.warning("Bạn phải nhập có phép thi công còn tồn");
        return false;
    }
    if (vm.totalMonthPlanTemp.remainQuantity == undefined||vm.totalMonthPlanTemp.remainQuantity == '') {
        toastr.warning("Bạn phải nhập khối lượng tồn");
        return false;
    }
    if (vm.totalMonthPlanTemp.remainStationComplete == undefined||vm.totalMonthPlanTemp.remainStationComplete == '') {
        toastr.warning("Bạn phải nhập hoàn thành tuyến tồn");
        return false;
    }
    return true;
}
function validateMaterialTemp() {
    if (vm.totalMonthPlanTemp.sysGroupId == undefined) {
        toastr.warning("Bạn phải chọn đơn vị!");
        return false;
    }
    if (vm.totalMonthPlanTemp.catConstructionTypeId == undefined||vm.totalMonthPlanTemp.catConstructionTypeId == '') {
        toastr.warning("Bạn phải nhập loại công trình");
        return false;
    }
    if (vm.totalMonthPlanTemp.catConstructionDeployId == undefined||vm.totalMonthPlanTemp.catConstructionDeployId == '') {
        toastr.warning("Bạn phải nhập hình thức triển khai!");
        return false;
    }
    return true;
}
function validateContractTemp() {
    if (vm.totalMonthPlanTemp.sysGroupId == undefined) {
        toastr.warning("Bạn phải chọn đơn vị!");
        return false;
    }
    if (vm.totalMonthPlanTemp.complete == undefined||vm.totalMonthPlanTemp.complete == '') {
        toastr.warning("Bạn phải nhập HSHC!");
        return false;
    }
    if (vm.totalMonthPlanTemp.enoughCondition == undefined||vm.totalMonthPlanTemp.enoughCondition == '') {
        toastr.warning("Bạn phải nhập HSHC đủ điều kiện!");
        return false;
    }
    if (vm.totalMonthPlanTemp.noEnoughCondition == undefined||vm.totalMonthPlanTemp.noEnoughCondition == '') {
        toastr.warning("Bạn phải nhập HSHC phải đối soát vật tư!");
        return false;
    }
    return true;
}
function validateFinanceTemp() {
    if (vm.totalMonthPlanTemp.sysGroupId == undefined) {
        toastr.warning("Bạn phải chọn đơn vị!");
        return false;
    }
    if (vm.totalMonthPlanTemp.firstTimes == undefined||vm.totalMonthPlanTemp.firstTimes == '') {
        toastr.warning("Bạn phải nhập số tiền đảm bảo đợt 1!");
        return false;
    }
    //if (vm.totalMonthPlanTemp.secondTimes == undefined||vm.totalMonthPlanTemp.secondTimes == '') {
    //    toastr.warning("Bạn phải nhập số tiền đảm bảo đợt 2!");
    //    return false;
    //}
    //if (vm.totalMonthPlanTemp.threeTimes == undefined||vm.totalMonthPlanTemp.threeTimes == '') {
    //    toastr.warning("Bạn phải nhập số tiền đảm bảo đợt 3!");
    //    return false;
    //}
    return true;
}
function validateBTSTemp() {
    if (vm.totalMonthPlanTemp.sysGroupId == undefined) {
        toastr.warning("Bạn phải chọn đơn vị!");
        return false;
    }
    if (vm.totalMonthPlanTemp.stationNumber == undefined||vm.totalMonthPlanTemp.stationNumber == '') {
        toastr.warning("Bạn phải nhập tổng số trạm!");
        return false;
    }
    if (vm.totalMonthPlanTemp.selfImplementPercent == undefined||vm.totalMonthPlanTemp.selfImplementPercent == '') {
        toastr.warning("Bạn phải nhập thi công trực tiếp");
        return false;
    }else if(Number(vm.totalMonthPlanTemp.selfImplementPercent)>100){
            toastr.warning("Thi công trực tiếp phải nhỏ hơn 100");
        return false;
    }
    if (vm.totalMonthPlanTemp.teamBuildRequire == undefined||vm.totalMonthPlanTemp.teamBuildRequire == '') {
        toastr.warning("Bạn phải nhập số đội xây dựng cần");
        return false;
    }
    if (vm.totalMonthPlanTemp.teamBuildAvaiable == undefined||vm.totalMonthPlanTemp.teamBuildAvaiable == '') {
        toastr.warning("Bạn phải nhập số đội đang triển khai");
        return false;
    }
    return true;
}
function validateMaintainTemp() {
    if (vm.totalMonthPlanTemp.sysGroupId == undefined) {
        toastr.warning("Bạn phải chọn đơn vị!");
        return false;
    }
    if (vm.totalMonthPlanTemp.buildPlan == undefined||vm.totalMonthPlanTemp.buildPlan == '') {
        toastr.warning("Bạn phải nhập xây dựng!");
        return false;
    }
    if (vm.totalMonthPlanTemp.installPlan == undefined||vm.totalMonthPlanTemp.installPlan == '') {
        toastr.warning("Bạn phải nhập KH lắp cột bao");
        return false;
    }
    if (vm.totalMonthPlanTemp.replacePlan == undefined||vm.totalMonthPlanTemp.replacePlan == '') {
        toastr.warning("Bạn phải nhập KH thay thân cột");
        return false;
    }
    if (vm.totalMonthPlanTemp.teamBuildRequire == undefined||vm.totalMonthPlanTemp.teamBuildRequire == '') {
        toastr.warning("Bạn phải nhập số đội cần  XD");
        return false;
    }
    if (vm.totalMonthPlanTemp.teamBuildAvaiable == undefined||vm.totalMonthPlanTemp.teamBuildAvaiable == '') {
        toastr.warning("Bạn phải nhập số đội đang XD");
        return false;
    }
    if (vm.totalMonthPlanTemp.teamInstallRequire == undefined||vm.totalMonthPlanTemp.teamInstallRequire == '') {
        toastr.warning("Bạn phải nhập số đội cần lắp dựng");
        return false;
    }
    if (vm.totalMonthPlanTemp.teamInstallAvaiable == undefined||vm.totalMonthPlanTemp.teamInstallAvaiable == '') {
        toastr.warning("Bạn phải nhập số đội đang lắp dựng");
        return false;
    }
    return true;
}
function validateTargetTemp() {
    if (vm.totalMonthPlanTemp.sysGroupId == undefined) {
        toastr.warning("Bạn phải chọn đơn vị!");
        return false;
    }
    if (vm.totalMonthPlanTemp.quantity == undefined) {
        toastr.warning("Bạn phải nhập sản lượng!");
        return false;
    }
    if (vm.totalMonthPlanTemp.complete == undefined) {
        toastr.warning("Bạn phải nhập HSHC");
        return false;
    }
    if (vm.totalMonthPlanTemp.revenue == undefined) {
        toastr.warning("Bạn phải nhập doanh thu");
        return false;
    }
    return true;
}
function validateSourceTemp() {
    if (vm.totalMonthPlanTemp.sysGroupId == undefined) {
        toastr.warning("Bạn phải chọn đơn vị!");
        return false;
    }
    if (vm.totalMonthPlanTemp.source == undefined) {
        toastr.warning("Bạn phải nhập nguồn việc!");
        return false;
    }
    return true;
}
vm.getExcelTemplate = function () {
    var obj = {};

    if (vm.value == CHI_TIEU) {
        obj.request = 'exportExcelTemplateTargetV2';
        $('#loadding').show();

    } else if (vm.value == DAM_BAO_NGUON_VIEC) {
        obj.request = 'exportExcelTemplateSource';
        $('#loadding').show();

    } else if (vm.value == GIA_CO_DU_AN||vm.value == 3) {
        obj.request = 'exportExcelTemplateForceMaintain';
        $('#loadding').show();

    } else if (vm.value == BTS) {
        obj.request = 'exportExcelTemplateForceNewBts';
        $('#loadding').show();

    } else if (vm.value == NGAM_HOA) {
        obj.request = 'exportExcelTemplateForceNewLine';
        $('#loadding').show();

    } else if (vm.value == DAM_BAO_VAT_TU) {
        obj.request = 'exportExcelTemplateMaterial';
        $('#loadding').show();

    } else if (vm.value == DAM_BAO_TAI_CHINH) {
        obj.request = 'exportExcelTemplateFinance';
        $('#loadding').show();

    } else if (vm.value == DAM_BAO_HD) {
        obj.request = 'exportExcelTemplateContract';
        $('#loadding').show();

    }
    totalMonthPlanService.downloadTemplate(obj).then(function (d) {
        data = d.plain();
        window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;

        $('#loadding').hide();

    }).catch(function (e) {
        $('#loadding').hide();

        toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        return;
    });
}

function fillDataGrid(data,id){
    var grid = $(id).data("kendoGrid");
    if(grid){
        grid.dataSource.data(data);
        grid.refresh();
    }
}
vm.saveImportFile = saveImportFile;
function saveImportFile(data) {
    if ($("#fileImportTotal")[0].files[0] == null) {
        toastr.warning("Bạn chưa chọn file để import");
        return;
    }
    if ($("#fileImportTotal")[0].files[0].name.split('.').pop() != 'xls' && $("#fileImportTotal")[0].files[0].name.split('.').pop() != 'xlsx') {
        toastr.warning("Sai định dạng file");
        return;
    }
    var formData = new FormData();
    formData.append('multipartFile', $('#fileImportTotal')[0].files[0]);
    var obj = {}
    if (vm.value == CHI_TIEU) {
        importTargetFile(formData);
    } else if (vm.value == DAM_BAO_NGUON_VIEC) {
        importSourceFile(formData);
    } else if (vm.value == GIA_CO_DU_AN||vm.value==3) {
        importForceMaintainFile(formData);
    } else if (vm.value == BTS) {
        importForceNewBtsFile(formData);
    } else if (vm.value == NGAM_HOA) {
        importForceNewLineFile(formData);
    } else if (vm.value == DAM_BAO_VAT_TU) {
        importMaterialFile(formData);
    } else if (vm.value == DAM_BAO_TAI_CHINH) {
        importFinanceFile(formData);
    } else if (vm.value == DAM_BAO_HD) {
        importContractFile(formData);
    }
}
function importTargetFile(formData) {
    // Bật span loaddding lên
//    $('#loadding').show();
//  hungnx 20180618 start
	$('#savepopupMonth').prop('disabled', true);
	htmlCommonService.showLoading('#loadingIpMonth');
//  hungnx 20180618 end
    return $.ajax({
        url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/importTarget",
        type: "POST",
        data: formData,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            if (data.error) {
                toastr.error("File import không có dữ liệu!");
                return;
            }
            else if (data.length === 0) {
                toastr.warning("File imp" +
                "ort không có dữ liệu");
                return;
            }else
            if (data[data.length - 1].errorFilePath != null) {
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                toastr.warning("File import không hợp lệ");
                return;
            }
            else {
                // add list import to grid table
            	var sysGroupIdList=[];
            	angular.forEach(data,function(dataItem,i){
            		sysGroupIdList[i]=dataItem.sysGroupId;
            	});
                var obj = {
                    month: vm.totalMonthPlan.month,
                    year: vm.totalMonthPlan.year,
                    sysGroupIdList: sysGroupIdList,
                }
                Restangular.all("totalMonthPlanRsService/getLKBySysList").post(obj).then(function (dataTarget) {
                    if(dataTarget!=null) {
                        for (var i = 0; i < data.length; i++) {
                            for (var j = 0; j < dataTarget.length; j++) {
                                if (data[i].sysGroupId == dataTarget[j].sysGroupId) {
                                    data[i].quantityLk = dataTarget[j].quantityLk
                                    data[i].completeLk = dataTarget[j].completeLk
                                    data[i].revenueLk = dataTarget[j].revenueLk
                                    data[i].quantityInYear = dataTarget[j].quantityInYear
                                    data[i].completeInYear = dataTarget[j].completeInYear
                                    data[i].revenueInYear = dataTarget[j].revenueInYear;
                                    break;
                                }
                            }
                        }
                        var detailList = [];
                        var targetGrid = vm.targetGrid;
                        if (targetGrid != undefined && targetGrid.dataSource != undefined) {
                            targetGrid.dataSource.data(data);
                            targetGrid.dataSource.sync();
                            targetGrid.refresh();
                        }
                    }else{
                        var detailList = [];
                        var targetGrid = vm.targetGrid;
                        if (targetGrid != undefined && targetGrid.dataSource != undefined) {
                            targetGrid.dataSource.data(data);
                            targetGrid.dataSource.sync();
                            targetGrid.refresh();
                        }
                    }
                },function(error){
                    var detailList = [];
                    var targetGrid = vm.targetGrid;
                    if (targetGrid != undefined && targetGrid.dataSource != undefined) {
                        targetGrid.dataSource.data(data);
                        targetGrid.dataSource.sync();
                        targetGrid.refresh();
                    }
                });

            }
            vm.cancelImport();
        }
    }).always(function(){
        ajax_sendding = false;
//        $('#loadding').hide();
        $('#savepopupMonth').prop('disabled', false);
    	htmlCommonService.hideLoading('#loadingIpMonth');
    });
}
function importSourceFile(formData) {
    var ajax_sendding = false;
    if (ajax_sendding == true){
        alert('Dang Load Ajax');
        return false;
    }

    // Ngược lại thì bắt đầu gửi ajax
    // Nhưng trước hết gán biến ajax_sendding = true để khi người dùng click tiếp
    // se không có tác dụng
    ajax_sendding = true;

    // Bật span loaddding lên
//    $('#loadding').show();
//  hungnx 20180618 start
	$('#savepopupMonth').prop('disabled', true);
	htmlCommonService.showLoading('#loadingIpMonth');
//  hungnx 20180618 end
    return $.ajax({
        url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/importSource",
        type: "POST",
        data: formData,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            if (data.error) {
                toastr.error("Lỗi khi import file!");
                return;
            }
            else if (data.length === 0) {
                toastr.warning("File imp" +
                "ort không có dữ liệu");
                return;
            }else
            if (data[data.length - 1].errorFilePath != null) {
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                toastr.warning("File import không hợp lệ");
                return;
            }
            else {
                // add list import to grid table
                var grid = vm.sourceGrid;
                if (grid != undefined && grid.dataSource != undefined) {
                    var gridTar = vm.targetGrid;
                    if(gridTar) {
                        var detailList = gridTar.dataSource._data;
                    }
                    angular.forEach(data,function(sourceItem){
                        angular.forEach(detailList, function (dataItem) {
                            if (sourceItem.sysGroupId == dataItem.sysGroupId) {
                                sourceItem.quantity =dataItem.quantity;
                                sourceItem.difference = Number(sourceItem.source||0)- Number(sourceItem.quantity||0);
                            }
                        })
                    });

                    grid.dataSource.data(data);
                    grid.dataSource.sync();
                    grid.refresh();
                }
            }
            vm.cancelImport();
        }
    }).always(function(){
        ajax_sendding = false;
//        $('#loadding').hide();
    //  hungnx 20180618 start
    	$('#savepopupMonth').prop('disabled', false);
    	htmlCommonService.hideLoading('#loadingIpMonth');
    //  hungnx 20180618 end
    });
}
function importForceMaintainFile(formData) {
//  hungnx 20180618 start
	$('#savepopupMonth').prop('disabled', true);
	htmlCommonService.showLoading('#loadingIpMonth');
//  hungnx 20180618 end
    return $.ajax({
        url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/importForceMaintain",
        type: "POST",
        data: formData,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            if (data.error) {
                toastr.error("Lỗi khi import file!");
                return;
            }
            else if (data.length === 0) {
                toastr.warning("File imp" +
                "ort không có dữ liệu");
                return;
            }else
            if (data[data.length - 1].errorFilePath != null) {
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                toastr.warning("File import không hợp lệ");
                return;
            }
            else {
                // add list import to grid table
                var detailList = [];
                var grid = vm.forceMaintainGrid;
                if (grid != undefined && grid.dataSource != undefined) {
                    grid.dataSource.data(data);
                    grid.dataSource.sync();
                    grid.refresh();
                }
            }
            vm.cancelImport();
        }
    }).always(function(){
        ajax_sendding = false;
//      $('#loadding').hide();
  //  hungnx 20180618 start
  	$('#savepopupMonth').prop('disabled', false);
  	htmlCommonService.hideLoading('#loadingIpMonth');
  //  hungnx 20180618 end
  });
}
function importForceNewBtsFile(formData) {
//  hungnx 20180618 start
  	$('#savepopupMonth').prop('disabled', true);
  	htmlCommonService.showLoading('#loadingIpMonth');
  //  hungnx 20180618 end
    return $.ajax({
        url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/importForceNewBts",
        type: "POST",
        data: formData,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            if (data.error) {
                toastr.error("Lỗi khi import file!");
                return;
            }
            else if (data.length === 0) {
                toastr.warning("File imp" +
                "ort không có dữ liệu");
                return;
            }else
            if (data[data.length - 1].errorFilePath != null) {
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                toastr.warning("File import không hợp lệ");
                return;
            }
           else {
                // add list import to grid table
                var detailList = [];
                var grid = vm.BTSGrid;
                if (grid != undefined && grid.dataSource != undefined) {
                    grid.dataSource.data(data);
                    grid.dataSource.sync();
                    grid.refresh();
                }
            }
            vm.cancelImport();
        }
    }).always(function(){
        ajax_sendding = false;
//      $('#loadding').hide();
  //  hungnx 20180618 start
  	$('#savepopupMonth').prop('disabled', false);
  	htmlCommonService.hideLoading('#loadingIpMonth');
  //  hungnx 20180618 end
  });
}
function importForceNewLineFile(formData) {
//  hungnx 20180618 start
  	$('#savepopupMonth').prop('disabled', true);
  	htmlCommonService.showLoading('#loadingIpMonth');
  //  hungnx 20180618 end
    return $.ajax({
        url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/importForceNewLine",
        type: "POST",
        data: formData,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            if (data.error) {
                toastr.error("Lỗi khi import file!");
                return;
            }
            else if (data.length === 0) {
                toastr.warning("File imp" +
                "ort không có dữ liệu");
                return;
            }else
            if (data[data.length - 1].errorFilePath != null) {
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                toastr.warning("File import không hợp lệ");
                return;
            }
            else {
                // add list import to grid table
                var detailList = [];
                var grid = vm.forceNewGrid;
                if (grid != undefined && grid.dataSource != undefined) {
                    grid.dataSource.data(data);
                    grid.dataSource.sync();
                    grid.refresh();
                }
            }
            vm.cancelImport();
        }
    }).always(function(){
        ajax_sendding = false;
//      $('#loadding').hide();
  //  hungnx 20180618 start
  	$('#savepopupMonth').prop('disabled', false);
  	htmlCommonService.hideLoading('#loadingIpMonth');
  //  hungnx 20180618 end
  });
}
function importMaterialFile(formData) {
//  hungnx 20180618 start
  	$('#savepopupMonth').prop('disabled', true);
  	htmlCommonService.showLoading('#loadingIpMonth');
  //  hungnx 20180618 end
    return $.ajax({
        url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/importMaterial",
        type: "POST",
        data: formData,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            if (data.error) {
                toastr.error("Lỗi khi import file!");
                return;
            }
            else if (data.length === 0) {
                toastr.warning("File imp" +
                "ort không có dữ liệu");
                return;
            }else
            if (data[data.length - 1].errorFilePath != null) {
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                toastr.warning("File import không hợp lệ");
                return;
            }
            else {
                // add list import to grid table
                var detailList = [];
                var grid = vm.materialGrid;
                if (grid != undefined && grid.dataSource != undefined) {
                    grid.dataSource.data(data);
                    grid.dataSource.sync();
                    grid.refresh();
                }
            }
            vm.cancelImport();
        }
    }).always(function(){
//      $('#loadding').hide();
  //  hungnx 20180618 start
  	$('#savepopupMonth').prop('disabled', false);
  	htmlCommonService.hideLoading('#loadingIpMonth');
  //  hungnx 20180618 end
  });
}
function importFinanceFile(formData) {
//  hungnx 20180618 start
  	$('#savepopupMonth').prop('disabled', true);
  	htmlCommonService.showLoading('#loadingIpMonth');
  //  hungnx 20180618 end
    return $.ajax({
        url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/importFinance",
        type: "POST",
        data: formData,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            if (data.error) {
                toastr.error("Lỗi khi import file!");
                return;
            }
            else if (data.length === 0) {
                toastr.warning("File imp" +
                "ort không có dữ liệu");
                return;
            }else
            if (data[data.length - 1].errorFilePath != null) {
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                toastr.warning("File import không hợp lệ");
                return;
            }
            else {
                // add list import to grid table
                var detailList = [];
                var grid = vm.financeGrid;
                if (grid != undefined && grid.dataSource != undefined) {
                    grid.dataSource.data(data);
                    grid.dataSource.sync();
                    grid.refresh();
                }
            }
            vm.cancelImport();
        }
    }).always(function(){
//      $('#loadding').hide();
  //  hungnx 20180618 start
  	$('#savepopupMonth').prop('disabled', false);
  	htmlCommonService.hideLoading('#loadingIpMonth');
  //  hungnx 20180618 end
  });
}
function importContractFile(formData) {
//  hungnx 20180618 start
  	$('#savepopupMonth').prop('disabled', true);
//	vm.disableSubmit = true;
  	htmlCommonService.showLoading('#loadingIpMonth');
  //  hungnx 20180618 end
    return $.ajax({
        url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/importContract",
        type: "POST",
        data: formData,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            if (data.error) {
                toastr.error("Lỗi khi import file!");
                return;
            }
            else if (data.length === 0) {
                toastr.warning("File imp" +
                "ort không có dữ liệu");
                return;
            }else
            if (data[data.length - 1].errorFilePath != null) {
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                toastr.warning("File import không hợp lệ");
                return;
            }
            else if (data.length === 0) {
                toastr.warning("File imp" +
                "ort không có dữ liệu");
                return;
            } else {
                // add list import to grid table
                var detailList = [];
                var grid = vm.contractGrid;
                if (grid != undefined && grid.dataSource != undefined) {
                    grid.dataSource.data(data);
                    grid.dataSource.sync();
                    grid.refresh();
                }
            }
            vm.cancelImport();
        }
    }).always(function(){
//      $('#loadding').hide();
  //  hungnx 20180618 start
    	$('#savepopupMonth').prop('disabled', false);
//    	vm.disableSubmit = false;
  	htmlCommonService.hideLoading('#loadingIpMonth');
//  	$scope.$apply();
  //  hungnx 20180618 end
  });
}
vm.typeMoneyList=[
    {id:'1',name:'Đợt 1'},
    {id:'2',name:'Đợt 2'},
    {id:'3',name:'Đợt 3'},
]
vm.typeMoneyListOptions={
    dataSource:vm.typeMoneyList,
    dataTextField: "name",
    dataValueField: "id",
    valuePrimitive: true
}
vm.headerTemplateContructionType='<div class="dropdown-header row text-center k-widget k-header">' +
'<p class="col-md-6 text-header-auto">Tên loại công trình</p>' +
'</div>';
vm.headerTemplateContructionDeploy='<div class="dropdown-header row text-center k-widget k-header">' +
'<p class="col-md-6 text-header-auto">Hình thức triển khai</p>' +
'</div>';

vm.selectedTypeChoose = false;

vm.constructionType1Options = {
    dataTextField:"catConstructionTypeName",

    select: function(e) {
        var dataItem = this.dataItem(e.item.index());
        vm.totalMonthPlanTemp.catConstructionTypeId = dataItem.catConstructionTypeId;
        vm.selectedTypeChoose = true;
        vm.totalMonthPlanTemp.catConstructionDeployName=null;
        vm.totalMonthPlanTemp.catConstructionDeployId=null;
    },
    open: function (e) {
        vm.selectedTypeChoose = false;
    },
    pageSize: 10,
    dataSource: {
        serverFiltering: true,
        transport: {
            read: function(options) {
                vm.selectedTypeChoose = false;
                return Restangular.all("totalMonthPlanRsService/" + 'fillterAllActiveCatConstructionType').post(
                    {keySearch: vm.totalMonthPlanTemp.catConstructionTypeName,
                        pageSize: 10,
                        page: 1
                    }).then(function(response){
                        options.success(response);
                    }).catch(function (err) {
                        console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                    });
            }
        }
    },
    template:'<div class="row" ><div class="col-xs-5" style="padding: 0px;float:left;width:100%">#: data.catConstructionTypeName #</div> </div>',
    change: function(e) {
        if (e.sender.value() === '') {
            vm.totalMonthPlanTemp.catConstructionTypeName = null;
            vm.totalMonthPlanTemp.catConstructionTypeId = null;
            vm.totalMonthPlanTemp.catConstructionDeployId = null;
            vm.totalMonthPlanTemp.catConstructionDeployName = null;
        }
    },
    ignoreCase: false
};
vm.selectedDeployChoose = false;
vm.constructionDeployOptions = {
    dataTextField:"catConstructionDeployName",

    select: function(e) {
        var dataItem = this.dataItem(e.item.index());
        vm.totalMonthPlanTemp.catConstructionDeployId = dataItem.catConstructionDeployId;
        vm.selectedDeployChoose = true;
    },
    open: function (e) {
        vm.selectedDeployChoose = false;
    },
    pageSize: 10,
    dataSource: {
        serverFiltering: true,
        transport: {
            read: function(options) {
                vm.selectedDeployChoose = false;
                return Restangular.all("totalMonthPlanRsService/" + 'fillterAllActiveCatConstructionDeploy').post(
                    {keySearch: vm.totalMonthPlanTemp.catConstructionDeployName,
                        pageSize: 10,
                        page: 1,
                        catConstructionTypeId:vm.totalMonthPlanTemp.catConstructionTypeId
                    }).then(function(response){
                        options.success(response);
                    }).catch(function (err) {
                        console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                    });
            }
        }
    },
    template:'<div class="row" ><div class="col-xs-5" style="padding: 0px;float:left;width:100%">#: data.catConstructionDeployName #</div> </div>',
    change: function(e) {
        if (e.sender.value() === '') {
            vm.totalMonthPlanTemp.catConstructionDeployId = null;
            vm.totalMonthPlanTemp.catConstructionDeployName = null;
        }
    },
    ignoreCase: false
};







vm.saveForceMaintain =saveForceMaintain;
function saveForceMaintain(){
    var data = vm.totalMonthPlan;
    var  forceMaintainGrid = vm.forceMaintainGrid;
//  hungnx 20180618 start
    vm.loadingUpdate = true;
  //hungnx 20180618 end
    if(forceMaintainGrid!=undefined && forceMaintainGrid.dataSource!=undefined)
        data.tmpnForceMaintainDTOList=forceMaintainGrid.dataSource._data;
    totalMonthPlanService.saveForceMaintain(data).then(function (result) {
        if (result.error) {
            toastr.error(result.error);
            return;
        }
        toastr.success("Ghi lại thành công!");
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    }, function (errResponse) {
        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi ghi lại"));
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    });
}
vm.saveForceNew =saveForceNew;
function saveForceNew(){
    var data = vm.totalMonthPlan;
    var  forceNewGrid = vm.forceNewGrid;
//  hungnx 20180618 start
    vm.loadingUpdate = true;
  //hungnx 20180618 end
    if(forceNewGrid!=undefined && forceNewGrid.dataSource!=undefined)
        data.tmpnForceNewLineDTOList=forceNewGrid.dataSource._data;
    totalMonthPlanService.saveForceNew(data).then(function (result) {
        if (result.error) {
            toastr.error(result.error);
        //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
            return;
        }
        toastr.success("Ghi lại thành công!");
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    }, function (errResponse) {
        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi ghi lại"));
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    });
}
vm.saveForceNewBTS =saveForceNewBTS;
function saveForceNewBTS(){
    var data = vm.totalMonthPlan;
    var  BTSGrid = vm.BTSGrid;
//  hungnx 20180618 start
    vm.loadingUpdate = true;
  //hungnx 20180618 end
    if(BTSGrid!=undefined && BTSGrid.dataSource!=undefined)
        data.tmpnForceNewBtsDTOList=BTSGrid.dataSource._data;
    totalMonthPlanService.saveForceNewBTS(data).then(function (result) {
        if (result.error) {
            toastr.error(result.error);
        //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
            return;
        }
        toastr.success("Ghi lại thành công!");
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    }, function (errResponse) {
        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi ghi lại"));
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    });
}
vm.saveMaterial =saveMaterial;
function saveMaterial(){
    var data = vm.totalMonthPlan;
    var  materialGrid = vm.materialGrid;
//  hungnx 20180618 start
    vm.loadingUpdate = true;
  //hungnx 20180618 end
    if(materialGrid!=undefined && materialGrid.dataSource!=undefined)
        data.tmpnMaterialDTOList=materialGrid.dataSource._data;

    totalMonthPlanService.saveMaterial(data).then(function (result) {
        if (result.error) {
            toastr.error(result.error);
        //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
            return;
        }
        toastr.success("Ghi lại thành công!");
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    }, function (errResponse) {
        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi ghi lại"));
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    });
}
vm.saveFinance =saveFinance;
function saveFinance(){
    var data = vm.totalMonthPlan;
    var  financeGrid = vm.financeGrid;
//  hungnx 20180618 start
    vm.loadingUpdate = true;
  //hungnx 20180618 end
    if(financeGrid!=undefined && financeGrid.dataSource!=undefined)
        data.tmpnFinanceDTOList=financeGrid.dataSource._data;
    totalMonthPlanService.saveFinance(data).then(function (result) {
        if (result.error) {
            toastr.error(result.error);
        //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
            return;
        }
        toastr.success("Ghi lại thành công!");
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    }, function (errResponse) {
        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi ghi lại"));
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    });
}
vm.saveContract =saveContract;
function saveContract(){
    var data = vm.totalMonthPlan;
    var  contractGrid = vm.contractGrid;
//  hungnx 20180618 start
    vm.loadingUpdate = true;
  //hungnx 20180618 end
    if(contractGrid!=undefined && contractGrid.dataSource!=undefined)
        data.tmpnContractDTOList=contractGrid.dataSource._data;
    totalMonthPlanService.saveContract(data).then(function (result) {
        if (result.error) {
            toastr.error(result.error);
        //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
            return;
        }
        toastr.success("Ghi lại thành công!");
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    }, function (errResponse) {
        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi ghi lại"));
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    });
}
vm.saveSource =saveSource;
function saveSource(){
    var data = vm.totalMonthPlan;
    var  sourceGrid = vm.sourceGrid;
//  hungnx 20180618 start
    vm.loadingUpdate = true;
  //hungnx 20180618 end
    if(sourceGrid!=undefined && sourceGrid.dataSource!=undefined)
        data.tmpnSourceDTOList=sourceGrid.dataSource._data;
    totalMonthPlanService.saveSource(data).then(function (result) {
        if (result.error) {
            toastr.error(result.error);
        //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
            return;
        }
        toastr.success("Ghi lại thành công!");
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    }, function (errResponse) {
        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi ghi lại"));
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    });
}
//hungnx 20180618 start
vm.loadingUpdate = false;
//hungnx 20180618 end
vm.saveTarget =saveTarget;
function saveTarget(){
    var data = vm.totalMonthPlan;
    var  targetGrid = vm.targetGrid;
//  hungnx 20180618 start
    vm.loadingUpdate = true;
  //hungnx 20180618 end
    if(targetGrid!=undefined && targetGrid.dataSource!=undefined)
        data.tmpnTargetDTOList=targetGrid.dataSource._data;
    totalMonthPlanService.saveTarget(data).then(function (result) {
        if (result.error) {
            toastr.error(result.error);
            return;
        }
        toastr.success("Ghi lại thành công!");
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    }, function (errResponse) {
        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi ghi lại"));
    //  hungnx 20180618 start
        vm.loadingUpdate = false;
      //hungnx 20180618 end
    });
}
    vm.saveAppendixFile =saveAppendixFile;
    function saveAppendixFile(){
        var data = vm.totalMonthPlan;
        var  appendixGrid = vm.appendixGrid;
        if(vm.appendixGrid!=undefined && vm.appendixGrid.dataSource!=null){
            var list = vm.appendixGrid.dataSource._data;

            for(var i = 0 ;i < list.length;i++) {
                if (list[i].appParam.code === "choose" || list[i].appParam.name == "---Chọn---") {
                    vm.appendixGrid.editCell(vm.appendixGrid.tbody.find('tr:eq(' + i + ')').find("td").eq(2));
                    toastr.warning("Chưa chọn loại file phụ lục!");
                    return;
                }
                list[i].appParamCode = list[i].appParam.code;
                for(var j =i+1;j<list.length;j++){
                    if(list[i].appParam.code==list[j].appParam.code){
                        vm.appendixGrid.editCell(vm.appendixGrid.tbody.find('tr:eq(' + j + ')').find("td").eq(2));
                        toastr.warning("Phụ lục không hợp lệ do nhiều bản ghi cùng một loại file!");
                        return;
                    }
                }
            }
            data.appendixFileDTOList=list;
            //data.appendixFileDTOList = vm.appendixGrid.dataSource._data;

        }
    //  hungnx 20180618 start
        vm.loadingUpdate = true;
      //hungnx 20180618 end
        totalMonthPlanService.saveAppendixFile(data).then(function (result) {
            if (result.error) {
                toastr.error(result.error);
                return;
            }
            toastr.success("Ghi lại thành công!");
        //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
        }, function (errResponse) {
            toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi ghi lại"));
        //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
        });
    }
    vm.getYearPlanDetail = getYearPlanDetail;
    function getYearPlanDetail(){
        var teamplateUrl = "coms/totalMonthPlan/year-plan-detail-form.html";
        var title = "Thông tin kế hoạch năm";
        var windowId = "yearPlan1";
        CommonService.populatePopupYearDetail(teamplateUrl, title, null, vm, windowId, true, '700', '', "");

    }
    $scope.$on("PopupYearPlan.open", function () {
        totalMonthPlanService.getYearPlanDetail(vm.totalMonthPlan).then(function(result){
            //var grid = $("#yearPlanDetail").data("kendoGrid");
            var grid =vm.yearPlanDetailGrid;
            if(grid){
                grid.dataSource.data(result.data);
                grid.refresh();
            }
        },function(error){

        });
    });
vm.fillYearPlanDetailTable=fillYearPlanDetailTable;
    function fillYearPlanDetailTable(){
        vm.yearPlanDetailGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            editable: false,
            resizable: true,
            dataBinding: function() {
                record = (this.dataSource.page() -1) * this.dataSource.pageSize();
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
                noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
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
                    field:"stt",
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
                },{
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
            ],
            dataSource: {
                schema: {
                    model: {
                        id: "yearPlanId",
                        fields: {
                            month:{ editable: false, nullable: true },
                            source:{ editable: false, type: "number",nullable: true },
                            complete:{ editable: false,type: "number", nullable: true },
                            revenue:{ editable: false,type: "number", nullable: true },
                            quantity:{ editable: false,type: "number", nullable: true },
                        }
                    }
                },sort: { field: "month", dir: "asc" }
            }
        });
    }
    vm.downloadFile = function(dataItem){
        window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + dataItem.filePath;
    }
    $scope.onSelectAppendixFile = function(e) {
        if($("#appendixFile")[0].files[0].name.split('.').pop() !='pdf' ){
            toastr.warning("Sai định dạng file");
            setTimeout(function() {
                $(".k-upload-files.k-reset").find("li").remove();
                $(".k-upload-files").remove();
                $(".k-upload-status").remove();
                $(".k-upload.k-header").addClass("k-upload-empty");
                $(".k-upload-button").removeClass("k-state-focused");
            },10);
            return;
        }
        if(104857600<$("#appendixFile")[0].files[0].size){
            toastr.warning("Dung lượng file vượt quá 100MB! ");
            return;
        }
        var formData = new FormData();
        jQuery.each(jQuery('#appendixFile')[0].files, function(i, file) {
            formData.append('multipartFile'+i, file);
        });
        return   $.ajax({
            url:Constant.BASE_SERVICE_URL+"fileservice/uploadATTTInput",
            type: "POST",
            data: formData,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            success:function(data) {
                var dataFile=[]
                jQuery.each(jQuery('#appendixFile')[0].files, function(index, file) {
                    var obj={};
                    obj.name=file.name;
                    obj.filePath=data[index];
                    obj.createdDate = new Date();
                    obj.createdUserName = Constant.userInfo.casUser.fullName;
                    obj.appParam ={
                        code : "choose",
                        name : "---Chọn---"
                    };
                    vm.appendixGrid.dataSource.insert(0, obj);
                });
                vm.appendixGrid.refresh();
            }
        });
    }
    function categoryDropDownEditor(container, options) {
        $('<input required name="' + options.field + '"/>')
            .appendTo(container)
            .kendoDropDownList({
                autoBind: false,
                suggest: true,
                dataTextField: "name",
                dataValueField: "code",
                dataSource: vm.dataRropFile
            });
    }
}
