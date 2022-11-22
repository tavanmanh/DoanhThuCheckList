(function() {
    'use strict';
    var controllerId = 'rpMonthWoController';

    angular.module('MetronicApp').controller(controllerId, rpMonthWoController);

    function rpMonthWoController($scope, $rootScope, $timeout, gettextCatalog,
                                         kendoConfig, $kWindow,
                                         CommonService, PopupConst, Restangular, RestEndpoint,Constant,$http) {
        var vm = this;
        vm.searchForm={
            obstructedState:1
        };
        vm.d={};
        vm.String="Quản lý công trình > Báo cáo > Tiến độ KH WO tháng ngoài OS";
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
                            url: Constant.BASE_SERVICE_URL + "woService/doSearchReport",
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
                        title: "Tháng",
                        width: '50px',
                        field:"month",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.month;
                        }
                    }
                    ,  {
                        title: "Năm",
                        width: '50px',
                        field:"year",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.year;
                        }
                    },{
                        title: "Đơn vị",
                        field: 'name',
                        width: '270px',
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
                                field: 'quantityMonth',
                                width: '100px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.quantityMonth);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.quantityMonthTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            },
                            {
                                title: "Hiện trạng",
                                field: 'quantityWo',
                                width: '100px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.quantityWo);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.quantityWoTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            },
                            {
                                title: "% Tiến độ",
                                field: 'quantityPercent',
                                width: '100px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.quantityPercent) + "%";
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.quantityPercentTotal||0) + "%";
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            }
                        ]
                    },
                    {
                        title: "Quỹ lương",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Chỉ tiêu",
                                field: 'salaryMonth',
                                width: '100px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.salaryMonth);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.salaryMonthTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            },
                            {
                                title: "Hiện trạng",
                                field: 'salaryWo',
                                width: '100px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.salaryWo);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.salaryWoTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },

                            },
                            {
                                title: "% Tiến độ",
//                                field: 'code',
                                field: 'salaryPercent',
                                width: '100px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.salaryPercent) + "%";
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.salaryPercentTotal||0) + "%";
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
                                field: 'fileMonth',
                                width: '100px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.fileMonth);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.fileMonthTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },
                            },
                            {
                                title: "Tỉnh thực hiện cập nhật",
                                field: 'provinceTH',
                                width: '100px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.provinceTH);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.provinceTHTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },
                            },
                            {
                                title: "% Tiến độ",
                                field: 'provinceTHPercent',
                                width: '100px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.provinceTHPercent) + "%";
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.provinceTHPercentTotal||0)  + "%";
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },
                            },
                            {
                                title: "Thực hiện TTHT duyệt",
                                field: 'fileWo',
                                width: '100px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.fileWo);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.fileWoTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },
                            },
                            {
                                title: "% Tiến độ",
                                field: 'filePercent',
                                width: '100px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.filePercent) + "%";
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.filePercentTotal||0)  + "%";
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },
                            }
                        ]
                    },
                    {
                        title: "Thu hồi dòng tiền",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Chỉ tiêu",
                                field: 'moneyMonth',
                                width: '100px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.moneyMonth);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.moneyMonthTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },
                            },
                            {
                                title: "Hiện trạng",
                                field: 'moneyWo',
                                width: '100px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.moneyWo);
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.moneyWoTotal||0);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },
                            },
                            {
                                title: "% Tiến độ",
                                field: 'moneyPercent',
                                width: '100px',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.moneyPercent) + "%";
                                },
                                footerTemplate: function(item) {
                                    var data = vm.rpMonthPlanProgressOSGrid.dataSource.data();
                                    var item, sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.moneyPercentTotal||0)  + "%";
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
        vm.exportexcel= function(){
            function displayLoading(target) {
                var element = $(target);
                kendo.ui.progress(element, true);
                setTimeout(function(){

                    if(vm.searchForm.fullYear != null){
                        vm.searchForm.month = vm.searchForm.fullYear.split("/")[0];
                        vm.searchForm.year = vm.searchForm.fullYear.split("/")[1];
                    }
                    return Restangular.all("woService/exportFile").post(vm.searchForm).then(function (d) {
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
