(function() {
    'use strict';
    var controllerId = 'rpHSHCStatusController';

    angular.module('MetronicApp').controller(controllerId, rpHSHCStatusController);

    function rpHSHCStatusController($scope, $rootScope, $timeout, gettextCatalog,
                                         kendoConfig, $kWindow,htmlCommonService,
                                         CommonService, PopupConst, Restangular, RestEndpoint,Constant,$http) {
        var vm = this;
        vm.searchForm={
            obstructedState:1
        };
        vm.d={};
        vm.String="Quản lý công trình > Báo cáo > Báo cáo doanh thu theo các dạng vướng";
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
            fillDataTable();
            initDropDownList();
            var date = new Date();
            var start = new Date(date.getFullYear(),0, 1);
            vm.searchForm.dateTo = htmlCommonService.formatDate(date);
            vm.searchForm.dateFrom  = htmlCommonService.formatDate(start);
            vm.searchForm.dateToDNQT =null;
            vm.searchForm.dateFromDNQT  = null;
            vm.searchForm.dateToVTnet = null;
            vm.searchForm.dateFromVTnet  = null;
            vm.searchForm.dateToInvestor = null;
            vm.searchForm.dateFromInvestor  = null;

            vm.searchForm.type= 1;
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
            vm.rpMonthPlanProgressOSGrid.dataSource.page(1);
        };

        function fillDataTable() {
            vm.gridProgressOSOptions = kendoConfig.getGridOptions({
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
                            $("#countRPStatus").text(""+response.total);
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
                            url: Constant.BASE_SERVICE_URL + "woService/doSearchReportHSHCStatus",
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
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,  {
                        title: "Nội dung",
                        width: '200px',
                        field:"problemName",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.problemName;
                        }
                    }
                    ,  {
                        title: "Lọc tình trạng",
                        width: '150px',
                        field:"valueTotal",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function(data){
                            return numberWithCommas( data.valueTotal);
                        }
                    },
                    {
                        title: "Đã chốt chưa có bảng thẩm",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Số lượng",
                                field: 'quantityAproved',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.quantityAproved);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.quantityAprovedTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            },
                            {
                                title: "Giá trị",
                                field: 'aprovedValue',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.aprovedValue);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.aprovedValueTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            }
                        ]
                    },
                    {
                        title: "Đã nộp chưa chốt",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Số lượng",
                                field: 'quantityVtnet',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.quantityVtnet);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.quantityVtnetTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            },
                            {
                                title: "Giá trị",
                                field: 'vtnetValue',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.vtnetValue);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.vtnetValueTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            }
                        ]
                    },
                    {
                        title: "Đã lập ĐNQT chưa nộp VTNet",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Số lượng",
                                field: 'quantityDnqt',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.quantityDnqt);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.quantityDnqtTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            },
                            {
                                title: "Giá trị",
                                field: 'dnqtValue',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.dnqtValue);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
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
                        title: "Chưa lập ĐNQT",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Số lượng",
                                field: 'quantityMoney',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.quantityMoney);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.quantityMoneyTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            },
                            {
                                title: "Giá trị",
                                field: 'moneyValue',
                                width: '140px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.moneyValue);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.moneyValueTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            }
                        ]
                    },
                ]
            });
        }

        vm.showHideEntangledGrid1Column = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.rpMonthPlanProgressOSGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.rpMonthPlanProgressOSGrid.showColumn(column);
            } else {
                vm.rpMonthPlanProgressOSGrid.hideColumn(column);
            }


        }

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };


        vm.exportexcel= function(){
            function displayLoading(target) {
                debugger;
                var element = $(target);
                kendo.ui.progress(element, true);
                setTimeout(function(){
                    return Restangular.all("woService/exportFileHSHCStatus").post(vm.searchForm).then(function (d) {
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


    }
})();
