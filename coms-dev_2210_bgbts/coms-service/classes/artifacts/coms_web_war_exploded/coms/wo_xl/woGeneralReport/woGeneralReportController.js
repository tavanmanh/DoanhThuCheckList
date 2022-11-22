(function () {
    'use strict';
    var controllerId = 'woGeneralReportController';

    angular.module('MetronicApp').controller(controllerId, woGeneralReportController, '$scope','$modal','$rootScope');

    function woGeneralReportController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                       kendoConfig, $kWindow, woGeneralReportService, htmlCommonService, vpsPermissionService,
                                       CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modal) {

        var vm = this;
        vm.String = "Báo cáo > Báo cáo tổng thể WO";
        vm.dataResult  = [];
        vm.searchForm = {};
        vm.searchForm.byLowerCd = 0;
        vm.searchForm.byFt = 0;
        $scope.trTypeForm ={};
        $scope.cdLv2List = {};
        $scope.cdLv3List = {};
        $scope.ftList = {};
        $scope.ftOrCd = {};
        $scope.geoAreas = [
            {code:'KV1', name: 'Khu vực 1'},
            {code:'KV2', name: 'Khu vực 2'},
            {code:'KV3', name: 'Khu vực 3'},
            // {code:'OTHERS', name: 'Khác'},
        ];

        $scope.permissions = {};
        $scope.overallRecord = {};

        $scope.originTypes = [
            {code:0, name: 'Trung tâm Hạ tầng'},
            {code:1, name: 'Các đơn vị chi nhánh kỹ thuật'},
            {code:2, name: 'Trung tâm Vận hành khai thác'},
        ];

        init();
        function init(){
            $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
            fillDataTable();
            //getCdLv2List();
            initDate();
        }

        function initDate(){
            var date = new Date();
            var start = new Date(date.getFullYear(), date.getMonth(), 1);
            var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
            vm.searchForm.toDate = htmlCommonService.formatDate(end);
            vm.searchForm.fromDate = htmlCommonService.formatDate(start);
        }

        $scope.getCdLv2List = function (){
            var obj = {geoAreaList: vm.searchForm.geoAreaList}
            woGeneralReportService.getCdLv2List(obj).then(
                function (resp) {
                    console.log(resp);
                    if(resp.data) $scope.cdLv2List = resp.data;
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        };

        $scope.getCdLv4List = function(){
            if(!vm.searchForm.areaId || vm.searchForm.areaId == ""){
                $scope.cdLv4List = [];
                return;
            }

            var postData = {cdLevel2: vm.searchForm.areaId}
            woGeneralReportService.getCdLv4List(postData).then(
                function (resp) {
                    console.log(resp);
                    if(resp.data) $scope.cdLv4List = resp.data;
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        };

        vm.genReport = function(){

            if(vm.searchForm.areaId){
                if($scope.ftOrCd.isFt == true) {
                    vm.searchForm.byFt = 1;
                    vm.searchForm.byLowerCd = 0;
                }
                else{
                    vm.searchForm.byLowerCd = 1;
                    vm.searchForm.byFt = 0;
                }
            }

            if(vm.searchForm.sectionId){
                vm.searchForm.byLowerCd = 0;
                vm.searchForm.byFt = 0;
            }

            vm.generalReportTable.dataSource.query({
                page: 1,
                pageSize: 10
            });
        }

        var record = 0;
        function fillDataTable(){

            vm.generalReportTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable:false,
                save : function(){
                    vm.generalReportTable.dataSource.read();
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
                            if(response.data && response.data.length > 0) $scope.overallRecord = response.data[0];
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            //Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/report/woGeneralReport",
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
                        empty: ''
                        // empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '40px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:center;"},
                        type :'text',
                    },
                    {
                        title: "Khu vực",
                        field: 'geoArea',
                        width: '220px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type :'text',
                        template : function(dataItem){
                            return valueOrEmptyStr(dataItem.geoArea);
                        },
                    },
                    {
                        title: "Chi nhánh",
                        field: 'areaName',
                        width: '220px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type :'text',
                        template : function(dataItem){
                            return valueOrEmptyStr(dataItem.areaName);
                        },
                    },
                    {
                        title: "Cụm",
                        field: 'sectionName',
                        width: '280px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.sectionName);
                        }
                    },
                    {
                        title: "Nhân viên thực hiện",
                        field: 'ftName',
                        width: '220px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.ftName);
                        }
                    },
                    {
                        title: "Tổng số WO",
                        field: 'totalWO',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalWO);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllWO;
                        },
                    },
                    {
                        title: "Chờ CD tiếp nhận",
                        field: 'totalAssignCd',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalAssignCd);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllAssignCd;
                        },
                    },
                    {
                        title: "% Chờ CD tiếp nhận",
                        field: 'totalAssignCd',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return calculatePercent(dataItem.totalAssignCd, dataItem.totalWO) + '%';
                        },
                    },
                    {
                        title: "CD đã tiếp nhận",
                        field: 'totalAcceptCd',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalAcceptCd);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllAcceptCd;
                        },
                    },
                    {
                        title: "% CD đã tiếp nhận",
                        field: 'totalAcceptCd',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return calculatePercent(dataItem.totalAcceptCd, dataItem.totalWO) + '%';
                        },
                    },
                    {
                        title: "CD từ chối",
                        field: 'totalRejectCd',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalRejectCd);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllRejectCd;
                        },
                    },
                    {
                        title: "% CD từ chối",
                        field: 'totalRejectCd',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return calculatePercent(dataItem.totalRejectCd, dataItem.totalWO) + '%';
                        },
                    },
                    {
                        title: "Chờ FT tiếp nhận",
                        field: 'totalAssignFt',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalAssignFt);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllAssignFt;
                        },
                    },
                    {
                        title: "% Chờ FT tiếp nhận",
                        field: 'totalAssignFt',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return calculatePercent(dataItem.totalAssignFt, dataItem.totalWO) + '%';
                        },
                    },
                    {
                        title: "FT đã tiếp nhận",
                        field: 'totalAcceptFt',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalAcceptFt);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllAcceptFt;
                        },
                    },
                    {
                        title: "% FT đã tiếp nhận",
                        field: 'totalAcceptFt',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return calculatePercent(dataItem.totalAcceptFt, dataItem.totalWO) + '%';
                        },
                    },
                    {
                        title: "FT từ chối",
                        field: 'totalRejectFt',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalRejectFt);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllRejectFt;
                        },
                    },
                    {
                        title: "% FT từ chối",
                        field: 'totalRejectFt',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return calculatePercent(dataItem.totalRejectFt, dataItem.totalWO) + '%';
                        },
                    },
                    {
                        title: "Đang thực hiện",
                        field: 'totalProcessing',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalProcessing);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllProcessing;
                        },
                    },
                    {
                        title: "% Đang thực hiện",
                        field: 'totalProcessing',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return calculatePercent(dataItem.totalProcessing, dataItem.totalWO) + '%';
                        },
                    },
                    {
                        title: "Đã thực hiện xong",
                        field: 'totalDone',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalDone);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllDone;
                        },
                    },
                    {
                        title: " Đã thực hiện xong",
                        field: 'totalDone',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return calculatePercent(dataItem.totalDone, dataItem.totalWO) + '%';
                        },
                    },
                    {
                        title: "Điều phối duyệt OK",
                        field: 'totalCdOk',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalCdOk);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllCdOk;
                        },
                    },
                    {
                        title: "% Điều phối duyệt OK",
                        field: 'totalCdOk',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return calculatePercent(dataItem.totalCdOk, dataItem.totalWO) + '%';
                        },
                    },
                    {
                        title: "Điều phối duyệt chưa tốt",
                        field: 'totalCdNotGood',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalCdNotGood);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllCdNotGood;
                        },
                    },
                    {
                        title: "% Điều phối duyệt chưa tốt",
                        field: 'totalCdNotGood',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return calculatePercent(dataItem.totalCdNotGood, dataItem.totalWO) + '%';
                        },
                    },
                    {
                        title: "TTHT duyệt",
                        field: 'totalOk',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalOk);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllOk;
                        },
                    },
                    {
                        title: "% TTHT duyệt",
                        field: 'totalOk',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return calculatePercent(dataItem.totalOk, dataItem.totalWO) + '%';
                        },
                    },
                    {
                        title: "TTHT từ chối",
                        field: 'totalNotGood',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalNotGood);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllNotGood;
                        },
                    },
                    {
                        title: "% TTHT từ chối",
                        field: 'totalNotGood',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return calculatePercent(dataItem.totalNotGood, dataItem.totalWO) + '%';
                        },
                    },
                    {
                        title: "Xin ý kiến",
                        field: 'totalOpinionRequest',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalOpinionRequest);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllOpinionRequest;
                        },
                    },
                    {
                        title: "% Xin ý kiến",
                        field: 'totalOpinionRequest',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return calculatePercent(dataItem.totalOpinionRequest, dataItem.totalWO) + '%';
                        },
                    },
                    {
                        title: "Số WO quá hạn chưa hoàn thành",
                        field: 'totalOverDue',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalOverDue);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllOverDue;
                        },
                    },
                    {
                        title: "% Số WO quá hạn chưa hoàn thành",
                        field: 'totalOverDue',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return calculatePercent(dataItem.totalOverDue, dataItem.totalWO) + '%';
                        },
                    },
                    {
                        title: "Số WO quá hạn đã hoàn thành",
                        field: 'totalFinishOverDue',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalFinishOverDue);
                        },
                        footerTemplate: function() {
                            return $scope.overallRecord.totalAllTotalFinishOverDue;
                        },
                    },
                    {
                        title: "% Số WO quá hạn đã hoàn thành",
                        field: 'totalFinishOverDue',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return calculatePercent(dataItem.totalFinishOverDue, dataItem.totalWO) + '%';
                        },
                    },
                ]
            });
        }

        function calculatePercent(val, total){
            if(!total || total == 0 || !val || val == 0) return 0;
            var result = (val/total)*100;
            return result.toFixed(2);
        }

        function valueOrEmptyStr(value) {
            return value? value:'';
        }

        function valueOrZero(value) {
            return value? value: 0;
        }

        vm.toExcelReport= function(){
            function displayLoading(target) {
                var element = $(target);
                vm.searchForm.page = 1;
                vm.searchForm.pageSize = 999999;
                kendo.ui.progress(element, true);
                setTimeout(function(){
                    return Restangular.all("woService/report/getExcelGeneralReport").post(vm.searchForm).then(function (d) {
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



// end controller
    }
})();
