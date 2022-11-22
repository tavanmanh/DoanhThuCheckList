(function() {
    'use strict';
    var controllerId = 'rpWoController';

    angular.module('MetronicApp').controller(controllerId, rpWoController,'$modal');

    function rpWoController($scope, $rootScope, $timeout, gettextCatalog,
                                    kendoConfig, $kWindow,htmlCommonService,rpWOService,
                                    CommonService, PopupConst, Restangular, RestEndpoint,Constant,$http,$modal) {
        var vm = this;
        vm.searchForm={
            obstructedState:1
        };
        vm.d={};
        vm.String="Quản lý công trình > Báo cáo > Báo cáo tổng hợp TR-WO ";
        vm.reportType = 0;
        $scope.woTypes = [];
        $scope.groupIds = [];
        $scope.modalTitle='';
        
        vm.listSysGroupAssign = [ {
			text : "TTHT",
			value : 1
		}, {
			text : "CNKT",
			value : 2
		}, {
			text : "TT-XDDD",
			value : 3
		}, {
			text : "TTGPTH",
			value : 4
		}, {
            text : "TT-DTHT",
            value : 5
        } ];
        
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
            var date = new Date();
            var start = new Date(date.getFullYear(),date.getMonth(), 1);
            vm.searchForm.toDate = htmlCommonService.formatDate(date);
            vm.searchForm.fromDate  = htmlCommonService.formatDate(start);
            vm.searchForm.type= 1;
            getListCd();
            getWoTypes();
            fillDataTable();
            fillDataTable2();
            fillDataTable3();
            initDropDownList();

        }
        function getWoTypes() {
            rpWOService.getWOTypes().then(
                function (resp) {
                    if (resp.data) $scope.woTypes = resp.data;
                    $scope.woTypes = $scope.woTypes.filter(function (item) {
                        return item.woTypeCode != 'HCQT';
                    })
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }
        function getListCd() {
            rpWOService.getListCDLevel2().then(
                function (resp) {
                    if (resp.data) $scope.groupIds = resp.data;
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )
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
            var grid =vm.gridByTrWo;
            var type = vm.searchForm.type;
            if (type =="1"){
                 grid = vm.gridByTrWo;
            }
            if (type =="2"){
                 grid = vm.gridByProvince;
            }
            if (type =="3"){
                 grid = vm.gridByGroup;
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
                templateUrl: 'coms/wo_xl/rpWO/detailTRWO.html',
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
                templateUrl: 'coms/wo_xl/rpWO/detailTRWO.html',
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
            vm.gridByTrWoOptions = kendoConfig.getGridOptions({
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
                            url: Constant.BASE_SERVICE_URL + "woService/doSearchReportWoTr",
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
                        title: "Đơn vị giao TR",
                        width: '270px',
                        field:"name",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.name;
                        }
                    }
                    ,  {
                        title: "TTHT đã nhận",
                        width: '150px',
                        field:"tthtApprove",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' TTHT đã nhận';
                            return '<div ng-click="vm.viewTrDetails(\''+ titleName +'\','+data.tthtTrWoPrecent +','+'\'tthtApprove\''+')">'+  numberWithCommas(data.tthtApprove) + '(' +data.tthtApprovePrecent + '%)' +'</div>';
                        }
                    }
                    ,  {
                        title: "TTHT tự động nhận ",
                        width: '150px',
                        field:"tthtSystem",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' TTHT tự động nhận';
                            return '<div ng-click="vm.viewTrDetails(\''+ titleName +'\','+data.groupCreated +','+'\'tthtSystem\''+')">'+  numberWithCommas(data.tthtSystem) + '(' +data.tthtSystemPrecent + '%)' +'</div>';
                        }
                    }
                    ,  {
                        title: "TTHT chờ nhận",
                        width: '150px',
                        field:"tthtAssignCd",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' TTHT chờ nhận';
                            return '<div ng-click="vm.viewTrDetails(\''+ titleName +'\','+data.groupCreated +','+'\'tthtAssignCd\''+')">'+  numberWithCommas(data.tthtAssignCd)+ '(' +data.tthtAssignCdPrecent + '%)' +'</div>';
                        }
                    }
                    ,  {
                        title: "TTHT từ chối",
                        width: '150px',
                        field:"tthtRejectCd",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' TTHT từ chối';
                            return '<div ng-click="vm.viewTrDetails(\''+ titleName +'\','+data.groupCreated +','+'\'tthtRejectCd\''+')">'+  numberWithCommas(data.tthtRejectCd) + '(' +data.tthtRejectCdPrecent + '%)' +'</div>';
                        }
                    }
                    ,  {
                        title: "Số TR TTHT nhận đã giao WO",
                        width: '150px',
                        field:"tthtTrWo",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số TR TTHT nhận đã giao WO';
                            return '<div ng-click="vm.viewTrDetails(\''+ titleName +'\','+data.groupCreated +','+'\'tthtTrWo\''+')">'+  numberWithCommas(data.tthtTrWo) + '(' +data.tthtTrWoPrecent + '%)' +'</div>';
                        }
                    },
                     {
                        title: "Số TR TTHT nhận chưa giao WO",
                        width: '150px',
                        field:"tthtNotTrWo",
                        headerAttributes: {style: "text-align:center;white-space:normal;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số TR TTHT nhận chưa giao WO';
                            return '<div ng-click="vm.viewTrDetails(\''+ titleName +'\','+data.groupCreated +','+'\'tthtNotTrWo\''+')">'+  numberWithCommas(data.tthtNotTrWo) + '(' +data.tthtNotTrWoPrecent + '%)' +'</div>';
                        }
                    },
                     {
                        title: "Số WO CNKT đã nhận đúng hạn",
                        width: '150px',
                        field:"cnktApprove",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT đã nhận đúng hạn';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'cnktApprove\''+')">'+  numberWithCommas(data.cnktApprove) + '(' +data.cnktApprovePrecent + '%)' +'</div>';
                        }
                    },
                     {
                        title: "Số WO CNKT tự động nhận",
                        width: '150px',
                        field:"cnktSystemWo",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT tự động nhận';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'cnktSystemWo\''+')">'+  numberWithCommas(data.cnktSystemWo) + '(' +data.cnktSystemWoPrecent + '%)'+'</div>';
                        }
                    },
                     {
                        title: "Số WO CNKT chờ nhận",
                        width: '150px',
                        field:"woAssignCd",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT chờ nhận';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woAssignCd\''+')">'+  numberWithCommas(data.woAssignCd)+ '(' +data.woAssignCdPrecent + '%)' +'</div>';
                        }
                    },
                     {
                        title: "Số WO CNKT giao FT đã tiếp nhận",
                        width: '150px',
                        field:"woAcceptFt",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT giao FT đã tiếp nhận';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woAcceptFt\''+')">'+  numberWithCommas(data.woAcceptFt) + '(' +data.woAcceptFtPrecent + '%)' +'</div>';
                        }
                    },
                     {
                        title: "Số WO CNKT FT tự động nhận",
                        width: '150px',
                        field:"woAcceptFtSystem",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT FT tự động nhận';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woAcceptFtSystem\''+')">'+  numberWithCommas(data.woAcceptFtSystem)+ '(' +data.woAcceptFtSystemPrecent + '%)' +'</div>';
                        }
                    },
                     {
                        title: "Số WO CNKT nhận, chưa giao FT",
                        width: '150px',
                        field:"woAcceptFtWo",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT nhận, chưa giao FT';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woAcceptFtWo\''+')">'+  numberWithCommas(data.woAcceptFtWo)+ '(' +data.woAcceptFtWoPrecent + '%)' +'</div>';
                        }
                    },
                     {
                        title: "Số WO CNKT giao FT chờ FT tiếp nhận",
                        width: '150px',
                        field:"woAssignFt",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT giao FT chờ FT tiếp nhận';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woAssignFt\''+')">'+  numberWithCommas(data.woAssignFt)+ '(' +data.woAssignFtPrecent + '%)' +'</div>';
                        }
                    },
                     {
                        title: "Số WO CNKT đã hoàn thành",
                        width: '150px',
                        field:"woFinish",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT đã hoàn thành';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woFinish\''+')">'+  numberWithCommas(data.woFinish)+ '(' +data.woFinishPrecent + '%)' +'</div>';
                        }
                    },
                     {
                        title: "Số WO CNKT đã hoàn thành đúng hạn",
                        width: '150px',
                        field:"woCompleted",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT đã hoàn thành đúng hạn';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woCompleted\''+')">'+  numberWithCommas(data.woCompleted)+ '(' +data.woNotCompletedPrecent + '%)' +'</div>';
                        }
                    },
                     {
                        title: "Số WO CNKT đã hoàn thành quá hạn",
                        width: '150px',
                        field:"woNotCompleted",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT đã hoàn thành quá hạn';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woNotCompleted\''+')">'+  numberWithCommas(data.woNotCompleted)+ '(' +data.woNotCompletedPrecent + '%)' +'</div>';
                        }
                    },
                     {
                        title: "Số WO CNKT chưa hoàn thành",
                        width: '150px',
                        field:"woNotFinish",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT chưa hoàn thành';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woNotFinish\''+')">'+  numberWithCommas(data.woNotFinish)+ '(' +data.woNotFinishPrecent + '%)' +'</div>';
                        }
                    },
                     {
                        title: "Số WO CNKT chưa hoàn thành trong hạn",
                        width: '150px',
                        field:"woNotFinishDate",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT đã hoàn thành quá hạn';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woNotFinishDate\''+')">'+  numberWithCommas(data.woNotFinishDate)+ '(' +data.woNotFinishDatePrecent + '%)' +'</div>';
                        }
                    },
                     {
                        title: "Số WO CNKT chưa hoàn thành quá hạn",
                        width: '150px',
                        field:"woFinishDayEx",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT chưa hoàn thành quá hạn';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woFinishDayEx\''+')">'+  numberWithCommas(data.woFinishDayEx)+ '(' +data.woFinishDayExPrecent + '%)' +'</div>';
                        }
                    },
                ]
            });
        }

        function fillDataTable3(){
            vm.gridByProvinceOptions = kendoConfig.getGridOptions({
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
                            url: Constant.BASE_SERVICE_URL + "woService/doSearchReportWoTr",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            console.log( vm.searchForm);
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
                    },
                    {
                        title: "CNKT",
                        width: '250px',
                        field:"name",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.name;
                        }
                    },
                    {
                        title: "Số WO CNKT đã nhận đúng hạn",
                        width: '150px',
                        field:"cnktApprove",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT đã nhận đúng hạn';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+ data.groupCreated +','+'\'cnktApprove\''+')">'+  numberWithCommas(data.cnktApprove) +'(' +data.cnktApprovePrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT tự động nhận",
                        width: '150px',
                        field:"cnktSystemWo",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT tự động nhận';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'cnktSystemWo\''+')">'+  numberWithCommas(data.cnktSystemWo) +'(' +data.cnktSystemWoPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT chờ nhận",
                        width: '150px',
                        field:"woAssignCd",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT chờ nhận';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woAssignCd\''+')">'+  numberWithCommas(data.woAssignCd) + '(' +data.woAssignCdPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT giao FT đã tiếp nhận",
                        width: '150px',
                        field:"woAcceptFt",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT giao FT đã tiếp nhận';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woAcceptFt\''+')">'+  numberWithCommas(data.woAcceptFt) + '(' +data.woAcceptFtPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT FT tự động nhận",
                        width: '150px',
                        field:"woAcceptFtSystem",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT FT tự động nhận';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woAcceptFtSystem\''+')">'+  numberWithCommas(data.woAcceptFtSystem) + '(' +data.woAcceptFtSystemPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT nhận, chưa giao FT",
                        width: '150px',
                        field:"woAcceptFtWo",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT nhận, chưa giao FT';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woAcceptFtWo\''+')">'+  numberWithCommas(data.woAcceptFtWo) + '(' +data.woAcceptFtWoPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT giao FT chờ FT tiếp nhận",
                        width: '150px',
                        field:"woAssignFt",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT giao FT chờ FT tiếp nhận';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woAssignFt\''+')">'+  numberWithCommas(data.woAssignFt) + '(' +data.woAssignFtPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT đã hoàn thành",
                        width: '150px',
                        field:"woFinish",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT đã hoàn thành';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woFinish\''+')">'+  numberWithCommas(data.woFinish) + '(' +data.woFinishPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT đã hoàn thành đúng hạn",
                        width: '150px',
                        field:"woCompleted",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT đã hoàn thành đúng hạn';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woCompleted\''+')">'+  numberWithCommas(data.woCompleted) + '(' +data.woCompletedPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT đã hoàn thành quá hạn",
                        width: '150px',
                        field:"woNotCompleted",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT đã hoàn thành quá hạn';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woNotCompleted\''+')">'+  numberWithCommas(data.woNotCompleted) + '(' +data.woNotCompletedPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT chưa hoàn thành",
                        width: '150px',
                        field:"woNotFinish",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT chưa hoàn thành';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woNotFinish\''+')">'+  numberWithCommas(data.woNotFinish) + '(' +data.woNotFinishPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT chưa hoàn thành trong hạn",
                        width: '150px',
                        field:"woNotFinishDate",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT đã hoàn thành quá hạn';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woNotFinishDate\''+')">'+  numberWithCommas(data.woNotFinishDate) + '(' +data.woNotFinishDatePrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT chưa hoàn thành quá hạn",
                        width: '150px',
                        field:"woFinishDayEx",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT chưa hoàn thành quá hạn';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woFinishDayEx\''+')">'+  numberWithCommas(data.woFinishDayEx) + '(' +data.woFinishDayExPrecent + '%)' +'</div>';
                        }
                    },
                ]
            });
        }

        function fillDataTable2(){
            vm.gridByGroupOptions = kendoConfig.getGridOptions({
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
                            url: Constant.BASE_SERVICE_URL + "woService/doSearchReportWoTr",
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
                        title: "Đơn vị giao WO",
                        width: '270px',
                        field:"name",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.name;
                        }
                    },
                    {
                        title: "Số WO CNKT đã nhận đúng hạn",
                        width: '150px',
                        field:"cnktApprove",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT đã nhận đúng hạn';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'cnktApprove\''+')">'+  numberWithCommas(data.cnktApprove) + '(' + data.cnktApprovePrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT tự động nhận",
                        width: '150px',
                        field:"cnktSystemWo",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT tự động nhận';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'cnktSystemWo\''+')">'+  numberWithCommas(data.cnktSystemWo) + '(' +data.cnktSystemWoPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT chờ nhận",
                        width: '150px',
                        field:"woAssignCd",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT chờ nhận';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woAssignCd\''+')">'+  numberWithCommas(data.woAssignCd) + '(' +data.woAssignCdPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT giao FT đã tiếp nhận",
                        width: '150px',
                        field:"woAcceptFt",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT giao FT đã tiếp nhận';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woAcceptFt\''+')">'+  numberWithCommas(data.woAcceptFt) + '(' +data.woAcceptFtPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT FT tự động nhận",
                        width: '150px',
                        field:"woAcceptFtSystem",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT FT tự động nhận';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woAcceptFtSystem\''+')">'+  numberWithCommas(data.woAcceptFtSystem) + '(' +data.woAcceptFtSystemPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT nhận, chưa giao FT",
                        width: '150px',
                        field:"woAcceptFtWo",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT nhận, chưa giao FT';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woAcceptFtWo\''+')">'+  numberWithCommas(data.woAcceptFtWo) + '(' +data.woAcceptFtWoPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT giao FT chờ FT tiếp nhận",
                        width: '150px',
                        field:"woAssignFt",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT giao FT chờ FT tiếp nhận';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woAssignFt\''+')">'+  numberWithCommas(data.woAssignFt) + '(' +data.woAssignFtPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT đã hoàn thành",
                        width: '150px',
                        field:"woFinish",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT đã hoàn thành';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woFinish\''+')">'+  numberWithCommas(data.woFinish) + '(' + data.woFinishPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT đã hoàn thành đúng hạn",
                        width: '150px',
                        field:"woCompleted",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT đã hoàn thành đúng hạn';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woCompleted\''+')">'+  numberWithCommas(data.woCompleted) + '(' +data.woCompletedPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT đã hoàn thành quá hạn",
                        width: '150px',
                        field:"woNotCompleted",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT đã hoàn thành quá hạn';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woNotCompleted\''+')">'+  numberWithCommas(data.woNotCompleted) + '(' +data.woNotCompletedPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT chưa hoàn thành",
                        width: '150px',
                        field:"woNotFinish",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT chưa hoàn thành';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woNotFinish\''+')">'+  numberWithCommas(data.woNotFinish) + '(' +data.woNotFinishPrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT chưa hoàn thành trong hạn",
                        width: '150px',
                        field:"woNotFinishDate",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT đã hoàn thành quá hạn';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woNotFinishDate\''+')">'+  numberWithCommas(data.woNotFinishDate) + '(' +data.woNotFinishDatePrecent + '%)' +'</div>';
                        }
                    },
                    {
                        title: "Số WO CNKT chưa hoàn thành quá hạn",
                        width: '150px',
                        field:"woFinishDayEx",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        template: function(data){
                            var titleName = data.name + ' Số WO CNKT chưa hoàn thành quá hạn';
                            return '<div ng-click="vm.viewWODetails(\''+ titleName +'\','+data.groupCreated +','+'\'woFinishDayEx\''+')">'+  numberWithCommas(data.woFinishDayEx) + '(' +data.woFinishDayExPrecent + '%)' +'</div>';
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
                    {
                        title: "Nguyên nhân quá hạn",
                        width: '150px',
                        field:"overdueReason",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return valueOrEmptyStr(data.overdueReason);
                        }
                    },
                    {
                        title: "Trạng thái quá hạn",
                        width: '150px',
                        field:"overdueApprovePerson",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return valueOrEmptyStr(data.overdueApprovePerson);
                        }
                    },
                ]
            });
        }
        function fillDataTrDetail() {
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
                            url: Constant.BASE_SERVICE_URL + "woService/doSearchDetailReportTr",
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
                        empty: "<div style='margin:5px'></div>"
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
                        title: "Tên TR",
                        width: '150px',
                        field:"woType",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){

                            return valueOrEmptyStr(data.trName);
                        }
                    },  {
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
                    ,
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
                    return Restangular.all("woService/exportFileTrWo").post(vm.searchForm).then(function (d) {
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
            if(id=='dept') {
                vm.searchForm.sysGroupName = ''
                vm.searchForm.sysGroupId = undefined;
            }
        }
    }
})();
