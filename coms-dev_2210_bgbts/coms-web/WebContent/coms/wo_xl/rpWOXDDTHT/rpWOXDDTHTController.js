(function() {
    'use strict';
    var controllerId = 'rpWOXDDTHTController';

    angular.module('MetronicApp').controller(controllerId, rpWOXDDTHTController,'$modal');

    function rpWOXDDTHTController($scope, $rootScope, $timeout, gettextCatalog,
                                    kendoConfig, $kWindow,htmlCommonService,rpWOXDDTHTService,
                                    CommonService, PopupConst, Restangular, RestEndpoint,Constant,$http,$modal) {
        var vm = this;
        vm.searchForm={};
        vm.d={};
        vm.String="Quản lý công trình > Báo cáo > Báo cáo tổng hợp XDDTHT";
        vm.reportType = 0;
        $scope.modalTitle='';
        $scope.overallRecord = {};
        
        
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
            var date = new Date();
            var start = new Date(date.getFullYear(),date.getMonth(), 1);
            vm.searchForm.toDate = htmlCommonService.formatDate(date);
            vm.searchForm.fromDate  = htmlCommonService.formatDate(start);
            vm.searchForm.type= 1;
            fillDataTable();
            fillDataTable2();
            initDropDownList();
        }
        vm.validatorOptions = kendoConfig.get('validatorOptions');
        setTimeout(function(){
            $("#keySearch").focus();
        },15);
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
        var record=0;
        vm.doSearch = function(){
            var grid =vm.gridGeneralReport;
            var type = vm.searchForm.type;
            if (type =="1"){
                 grid = vm.gridGeneralReport;
            }
            if (type =="2"){
                 grid = vm.gridWoDetail;
            }
            if(grid){
                grid.dataSource.page(1);
            }
        };
        vm.viewWODetails = viewWODetails;
        function viewWODetails(titles,groupCreatedId,status){
            $scope.modalTitle = titles;
            vm.searchForm.status=status;
            vm.searchForm.groupCreated=groupCreatedId;
            $modal.open({
                templateUrl: 'coms/wo_xl/rpWOXDDTHT/detailTRWO.html',
                controller: null,
                windowClass: '',
                scope: $scope
            })
                .result.then(
                function() {
                },
                function() {
                }
            )
            fillDataDetail();
        }

        vm.viewTrDetails = viewTrDetails;
        function viewTrDetails(titles,groupCreatedId,status){
            $scope.modalTitle = titles;
            vm.searchForm.status=status;
            vm.searchForm.groupCreated=groupCreatedId;
            $modal.open({
                templateUrl: 'coms/wo_xl/rpWOXDDTHT/detailTRWO.html',
                controller: null,
                windowClass: '',
                scope: $scope
            })
                .result.then(
                function() {
                },
                function() {
                }
            )
            fillDataTrDetail();
        }
        function fillDataTable() {
            vm.gridByGeneralReport = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
                dataSource:{
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#countRPMonthProgressOS").text(""+response.total);
                            vm.count=response.total;
                            return response.total;
                        },
                        data: function (response) {
                            if(response.data && response.data.length > 0) {
                                $scope.overallRecord = response.data[0]
                            } else {
                                $scope.overallRecord={}
                            };
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/getGeneralReportXDDTHT",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page
                            vm.searchForm.pageSize = options.pageSize
                            return JSON.stringify(vm.searchForm)
                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'></div>")
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
                        title: "Nhân viên thực hiện",
                        field: 'ftName',
                        width: '220px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
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
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalWO);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllWO != undefined){
                                sum = $scope.overallRecord.totalAllWO;
                            }
                            return sum;
                        },
                    },
                    {
                        title: "Chờ CD tiếp nhận",
                        field: 'totalAssignCd',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalAssignCd);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllAssignCd != undefined){
                                sum = $scope.overallRecord.totalAllAssignCd;
                            }
                            return sum;
                        },
                    },
                    {
                        title: "CD đã tiếp nhận",
                        field: 'totalAcceptCd',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalAcceptCd);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllAcceptCd != undefined){
                                sum = $scope.overallRecord.totalAllAcceptCd;
                            }
                            return sum;
                        },
                    },
                    {
                        title: "CD từ chối",
                        field: 'totalRejectCd',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalRejectCd);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllRejectCd != undefined){
                                sum = $scope.overallRecord.totalAllRejectCd;
                            }
                            return sum;
                        },
                    },
                    {
                        title: "Chờ FT tiếp nhận",
                        field: 'totalAssignFt',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalAssignFt);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllAssignFt != undefined){
                                sum = $scope.overallRecord.totalAllAssignFt;
                            }
                            return sum;
                        },
                    },
                    {
                        title: "FT đã tiếp nhận",
                        field: 'totalAcceptFt',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalAcceptFt);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllAcceptFt != undefined){
                                sum = $scope.overallRecord.totalAllAcceptFt;
                            }
                            return sum;
                        },
                    },
                    {
                        title: "FT từ chối",
                        field: 'totalRejectFt',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalRejectFt);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllRejectFt != undefined){
                                sum = $scope.overallRecord.totalAllRejectFt;
                            }
                            return sum;
                        },
                    },
                    {
                        title: "Đang thực hiện",
                        field: 'totalProcessing',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalProcessing);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllProcessing != undefined){
                                sum = $scope.overallRecord.totalAllProcessing;
                            }
                            return sum;
                        },
                    },
                    {
                        title: "Đã thực hiện xong",
                        field: 'totalDone',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalDone);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllDone != undefined){
                                sum = $scope.overallRecord.totalAllDone;
                            }
                            return sum;
                        },
                    },
                    {
                        title: "Điều phối duyệt OK",
                        field: 'totalCdOk',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalCdOk);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllCdOk != undefined){
                                sum = $scope.overallRecord.totalAllCdOk;
                            }
                            return sum;
                        },
                    },
                    {
                        title: "Điều phối duyệt chưa tốt",
                        field: 'totalCdNotGood',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalCdNotGood);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllCdNotGood != undefined){
                                sum = $scope.overallRecord.totalAllCdNotGood;
                            }
                            return sum;
                        },
                    },
                    {
                        title: "Hoàn thành tốt",
                        field: 'totalOk',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalOk);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllOk != undefined){
                                sum = $scope.overallRecord.totalAllOk;
                            }
                            return sum;
                        },
                    },
                    {
                        title: "Hoàn thành chưa tốt",
                        field: 'totalNotGood',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalNotGood);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllNotGood != undefined){
                                sum = $scope.overallRecord.totalAllNotGood;
                            }
                            return sum;
                        },
                    },
                    {
                        title: "Xin ý kiến",
                        field: 'totalOpinionRequest',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalOpinionRequest);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllOpinionRequest != undefined){
                                sum = $scope.overallRecord.totalAllOpinionRequest;
                            }
                            return sum;
                        },
                    },

                    {
                        title: "Số WO quá hạn chưa hoàn thành",
                        field: 'totalOverDue',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalOverDue);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllOverDue != undefined){
                                sum = $scope.overallRecord.totalAllOverDue;
                            }
                            return sum;
                        },
                    },
                    {
                        title: "Số WO quá hạn đã hoàn thành",
                        field: 'totalFinishOverDue',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {style: "text-align:right;"},
                        type :'text',
                        template: function (dataItem) {
                            return valueOrZero(dataItem.totalFinishOverDue);
                        },
                        footerTemplate: function() {
                            var sum = 0;
                            if($scope.overallRecord.totalAllTotalFinishOverDue != undefined){
                                sum = $scope.overallRecord.totalAllTotalFinishOverDue;
                            }
                            return sum;
                        },
                    },
                ]
            });
        }

        function fillDataTable2(){
            vm.gridByWoDetailOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
                dataSource:{
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#countRPMonthProgressOS").text(""+response.total);
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
                            url: Constant.BASE_SERVICE_URL + "woService/getWoDetailReportXDDTHT",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page
                            vm.searchForm.pageSize = options.pageSize
                            return JSON.stringify(vm.searchForm)
                        }
                    },
                    pageSize: 10
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
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '40px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                    },
                    {
                        title: "Nhân viên phục trách",
                        field: 'cdLevel5Name',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.ftName);
                        }
                    },
                    {
                        title: "Nhân viên thực hiện",
                        field: 'ftName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.ftName);
                        }
                    },
                    {
                        title: "Tên WO",
                        field: 'woName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.woName);
                        }
                    },
                    {
                        title: "Mã WO",
                        field: 'woCode',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.woCode);
                        }
                    },
                    {
                        title: "Trạng thái",
                        field: 'state',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.state);
                        }
                    },
                    {
                        title: "Hợp đồng",
                        field: 'contractCode',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.contractCode);
                        }
                    },
                    {
                        title: "Mã trạm",
                        field: 'ftName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.stationCode);
                        }
                    },
                    {
                        title: "Mã công trình",
                        field: 'ftName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.constructionCode);
                        }
                    },
                    {
                        title: "Hạng mục",
                        field: 'catWorkItemTypeName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.catWorkItemTypeName);
                        }
                    },
                    {
                        title: "Loại công trình",
                        field: 'catConstructionTypeName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.catConstructionTypeName);
                        }
                    },
                    {
                        title: "Nguồn việc",
                        field: 'ftName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.apWorkSrcName);
                        }
                    },
                    {
                        title: "Ngày hoàn thành theo kế hoạch",
                        field: 'finishDateStr',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.finishDateStr);
                       }
                    },
                    {
                        title: "Ngày FT hoàn thành thực tế",
                        field: 'endTimeStr',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.endTimeStr);
                        }
                    },
                    {
                        title: "Ngày phê duyệt WO",
                        field: 'updateCdLevel5ReceiveWoStr',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        template: function (dataItem) {
                           return valueOrEmptyStr(dataItem.updateCdLevel5ReceiveWoStr);
                        }
                    },
                    {
                        title: "Tháng ghi nhận WO",
                        field: 'approveDateReportWoStr',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.approveDateReportWoStr);
                       }
                    },
                    {
                        title: "Công việc ",
                        field: 'woNameFromConfig',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.woNameFromConfig);
                        }
                    },
                    {
                        title: "Giá trị sản lượng đầu vào (triệu VND)",
                        field: 'moneyFlowValue',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            if(!dataItem.moneyFlowValue || dataItem.moneyFlowValue == 0) return 0;
                            var moneyM = dataItem.moneyFlowValue/1000000;
                            moneyM = moneyM.toFixed(3);
                            moneyM = CommonService.removeTrailingZero(moneyM);
                            return CommonService.numberWithCommas(moneyM);
                        }
                    },
                    {
                        title: "Sản lượng đã ghi nhận (triệu VND)",
                        field: 'moneyValue',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            if(!dataItem.moneyValue || dataItem.moneyValue == 0) return 0;
                            var moneyM = dataItem.moneyValue/1000000;
                            moneyM = moneyM.toFixed(3);
                            moneyM = CommonService.removeTrailingZero(moneyM);
                            return valueOrEmptyStr(moneyM);
                        }
                    },
                ]
            });
        }
        var record=0;
        function fillDataDetail() {
            vm.gridByDetail = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
                dataSource:{
                    serverPaging: true,
                    schema: {
                        total: function (response) {
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
                            url: Constant.BASE_SERVICE_URL + "woService/doSearchDetailReportWoTr",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page
                            vm.searchForm.pageSize = options.pageSize
                            return JSON.stringify(vm.searchForm)
                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
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
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,  {
                        title: "Mã WO",
                        width: '200px',
                        field:"woCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return valueOrEmptyStr(data.woCode);
                        }
                    }
                    ,  {
                        title: "Loại WO",
                        width: '150px',
                        field:"woType",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){

                            return valueOrEmptyStr(data.woTypeName);
                        }
                    },{
                        title: "Mã TR",
                        width: '200px',
                        field:"trCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return valueOrEmptyStr(data.trCode);
                        }
                    }
                    ,  {
                        title: "Trạng thái",
                        width: '150px',
                        field:"state",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return valueOrEmptyStr(data.state);
                        }
                    }
                    ,  {
                        title: "Ngày tạo",
                        width: '150px',
                        field:"createdDateStr",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function(data){
                            return valueOrEmptyStr(data.createdDateStr);
                        }
                    }
                    ,  {
                        title: "Hạn hoàn thành",
                        width: '150px',
                        field:"finishDateStr",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function(data){
                            return valueOrEmptyStr(data.finishDateStr);
                        }
                    }
                    ,  {
                        title: "Sản lượng",
                        width: '120px',
                        field:"moneyValue",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return numberWithCommas(data.moneyValue);
                        }
                    },
                    {
                        title: "CD Level 2",
                        width: '150px',
                        field:"cdLevel2Name",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return valueOrEmptyStr(data.cdLevel2Name);
                        }
                    },
                    {
                        title: "CD Level 3",
                        width: '150px',
                        field:"cdLevel3Name",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return valueOrEmptyStr(data.cdLevel3Name);
                        }
                    },
                    {
                        title: "CD Level 4",
                        width: '150px',
                        field:"cdLevel4Name",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return valueOrEmptyStr(data.cdLevel4Name);
                        }
                    },
                    {
                        title: "Nhân viên thực hiện",
                        width: '150px',
                        field:"ftName",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return valueOrEmptyStr(data.ftName);
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
                    return Restangular.all("woService/exportFileReportXDDTHT").post(vm.searchForm).then(function (d) {
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
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
        }

        function valueOrZero(value) {
            return value? value: 0;
        }
        vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'dept') {
                vm.searchForm.sysGroupName = data.text;
                vm.searchForm.sysGroupId = data.id;
            }
        }
        vm.cancelListYear= cancelListYear;
        function cancelListYear(){
            vm.searchForm.fromDate = null;
            vm.searchForm.toDate = null;
        }
        function valueOrEmptyStr(value){
            return value ? value : '';
        }
        vm.cancelPopupDetail = cancelPopupDetail;
        function cancelPopupDetail() {
            $(".k-icon.k-i-close").click();
        };
        vm.cancelInput = function(id){
            if(id=='month') {
                vm.searchForm.monthYear = ''
            }
        }
    }
})();
