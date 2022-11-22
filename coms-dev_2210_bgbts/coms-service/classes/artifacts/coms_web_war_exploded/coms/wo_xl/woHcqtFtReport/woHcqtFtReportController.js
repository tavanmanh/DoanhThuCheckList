(function () {
    'use strict';
    var controllerId = 'woHcqtFtReportController';

    angular.module('MetronicApp').controller(controllerId, woHcqtFtReportController, '$scope','$modal','$rootScope');

    function woHcqtFtReportController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, htmlCommonService, vpsPermissionService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modal) {

        var vm = this;
        vm.disableBtnExcel = false;
        vm.String = "Báo cáo > Báo cáo theo cá nhân thực hiện ";
        vm.dataResult  = [];
        vm.searchForm = {};
        vm.label = {};
        $scope.permissions = {};

        init();
        function init(){
            $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
        	fillDataTable();
            initDate();
        }

        function initDate(){
            var date = new Date();
            var start = new Date(date.getFullYear(), date.getMonth(), 1);
            var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
            vm.searchForm.dateTo = htmlCommonService.formatDate(end);
            vm.searchForm.dateFrom = htmlCommonService.formatDate(start);
        }
        vm.genReport = function(){
            vm.hcqtFtReportTable.dataSource.query({
                page: 1,
                pageSize: 10
            });
        };

        var record = 0;
        function fillDataTable(){

            vm.hcqtFtReportTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable:false,
                save : function(){
                    vm.hcqtFtReportTable.dataSource.read();
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            // // $("#appCount").text("" + response.total);
                            vm.count = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            //Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/report/woHcqtFtReport",
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
                    refresh: true,
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
                        width: '40px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,  {
                        title: "Cá nhân thực hiện",
                        width: '200px',
                        field:"ftName",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.ftName;
                        }
                    },
                    {
                        title: "KH lập ĐNQT",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Số lượng",
                                field: 'totalQuantityByPlan',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.totalQuantityByPlan);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.hcqtFtReportTable.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.quantityByPlanTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            },
                            {
                                title: "Giá trị",
                                field: 'totalValueByPlan',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.totalValueByPlan);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.hcqtFtReportTable.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.valueByPlanTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            }
                        ]
                    },
                    {
                        title: "T/H Lập ĐNQT",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Số lượng",
                                field: 'totalHasDnqtQuantity',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.totalHasDnqtQuantity);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.hcqtFtReportTable.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.hasDnqtQuantityTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            },
                            {
                                title: "Giá trị",
                                field: 'totalDnqtValue',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.totalDnqtValue);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.hcqtFtReportTable.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.dnqtValueTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            }
                        ]
                    },
                    {
                        title: "% Thực hiện",
                        width: '100px',
                        field:"percent",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return numberWithCommas(data.dnqtPercent) + '%';
                        }
                    },
                    {
                        title: "SL chốt",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Số lượng",
                                field: 'totalSendVtnetQuantity',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.totalSendVtnetQuantity);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.hcqtFtReportTable.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.sendVtnetQuantityTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            },
                            {
                                title: "Giá trị",
                                field: 'totalSendVtnetValue',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.totalSendVtnetValue);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.hcqtFtReportTable.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.sendVtnetValueTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            }
                        ]
                    },
                    {
                        title: "Thực hiện chốt",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Số lượng",
                                field: 'totalVtnetConfirmedQuantity',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.totalVtnetConfirmedQuantity);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.hcqtFtReportTable.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.vtnetConfirmedQuantityTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            },
                            {
                                title: "Giá trị",
                                field: 'totalVtnetConfirmedValue',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.totalVtnetConfirmedValue);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.hcqtFtReportTable.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.vtnetConfirmedValueTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            }
                        ]
                    },
                    {
                        title: "% Thực hiện",
                        width: '100px',
                        field:"percent1",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return numberWithCommas(data.vtnetPercent) + '%';
                        }
                    },
                    {
                        title: "Lấy bảng thẩm về",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Số lượng",
                                field: 'totalApprovedQuantity',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.totalApprovedQuantity);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.hcqtFtReportTable.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.approvedQuantityTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            },
                            {
                                title: "Giá trị",
                                field: 'totalApprovedValue',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.totalApprovedValue);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.hcqtFtReportTable.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.approvedValueTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            }
                        ]
                    },
                    {
                        title: "% Thực hiện",
                        width: '100px',
                        field:"percent2",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return numberWithCommas(data.approvedPercent) + '%';
                        }
                    },
                ]

            });
        }

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };

        vm.exportexcel= function(){
            function displayLoading(target) {
                var element = $(target);
                kendo.ui.progress(element, true);
                setTimeout(function(){
                    return Restangular.all("woService/exportFileHcqtFtReport").post(vm.searchForm).then(function (d) {
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


        function numberWithCommas(x) {
            if(x == null || x == undefined){
                return '0';
            }
            var parts = x.toFixed(0).toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ".");
            return parts.join(".");
        }

// end controller
    }
})();
