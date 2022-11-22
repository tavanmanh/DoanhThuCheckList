(function() {
    'use strict';
    var controllerId = 'rpWoAIOController';

    angular.module('MetronicApp').controller(controllerId, rpWoAIOController);

    function rpWoAIOController($scope, $rootScope, $timeout, gettextCatalog,
                                kendoConfig, $kWindow,
                                CommonService, PopupConst, Restangular, RestEndpoint,Constant,$http) {
        var vm = this;
        vm.searchForm={
            obstructedState:1
        };
        vm.d={};
        vm.String="Quản lý công trình > Báo cáo > Báo cáo quản trị hợp đồng AIO";
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
            var date = new Date();
            var start = new Date(date.getFullYear(), date.getMonth(), 1);
            var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
            vm.searchForm.dateTo = kendo.toString(end,"dd/MM/yyyy");
            vm.searchForm.dateFrom = kendo.toString(start,"dd/MM/yyyy");
            fillDataTable();
            initDropDownList();
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
            vm.rpWoAIOGrid.dataSource.page(1);
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
                            url: Constant.BASE_SERVICE_URL + "woService/doSearchReportAIO",
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
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,  {
                        title: "Chi Nhánh",
                        width: '250px',
                        field:"cdLevel2",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.cdLevel2?data.cdLevel2:'';
                        }
                    }
                    ,  {
                        title: "Cụm",
                        width: '250px',
                        field:"cdLevel4",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.cdLevel4?data.cdLevel4:'';
                        }
                    } ,
                    {
                        title: "Mã TR",
                        width: '250px',
                        field:"trCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.trCode?data.trCode:'';
                        }
                    }
                    ,  {
                        title: "Mã hợp đồng",
                        width: '250px',
                        field:"contractCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.contractCode?data.contractCode:'';
                        }
                    }
                    ,  {
                        title: "Mã WO",
                        width: '250px',
                        field:"woCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.woCode?data.woCode:'';
                        }
                    }
                    ,  {
                        title: "Nghành hàng",
                        width: '150px',
                        field:"insdustryName",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.insdustryName?data.insdustryName:'';
                        }
                    }
                    ,  {
                        title: "Sản lượng",
                        width: '150px',
                        field:"moneyValue",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function(data){
                            return numberWithCommas(data.moneyValue);
                        }
                    },
                      {
                        title: "Trạng thái",
                        width: '150px',
                        field:"state",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function(data){
                            return data.state?data.state:'';
                        }
                    }
                    ,  {
                        title: "Người tạo",
                        width: '150px',
                        field:"userCreated",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.userCreated?data.userCreated:'';
                        }
                    },
                      {
                        title: "Người thực hiện",
                        width: '150px',
                        field:"userFt",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.userFt?data.userFt:'';
                        }
                    }
                    ,  {
                        title: "Ngày tiếp nhận",
                        width: '150px',
                        field:"acceptTime",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function(data){
                            return data.acceptTime?data.acceptTime:'';
                        }
                    },
                     {
                        title: "Ngày hoàn thành",
                        width: '150px',
                        field:"endTime",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function(data){
                            return data.endTime?data.endTime:'';
                        }
                    }
                    ,  {
                        title: "Lý do từ chối",
                        width: '250px',
                        field:"reason",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.reason?data.reason:'';
                        }
                    }
                ]
            });
        }

        vm.showHideEntangledGrid1Column = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.rpWoAIOGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.rpWoAIOGrid.showColumn(column);
            } else {
                vm.rpWoAIOGrid.hideColumn(column);
            }


        }

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };
        function numberWithCommas(x) {
            if(x == null || x == undefined){
                return '0';
            }
            var parts = x.toFixed(2).toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
        }
        vm.exportexcel= function(){
            function displayLoading(target) {
                var element = $(target);
                kendo.ui.progress(element, true);
                setTimeout(function(){
                    return Restangular.all("woService/exportFileAIO").post(vm.searchForm).then(function (d) {
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

        vm.cancelInput = cancelInput;
        vm.monthSelectorOptions = {
            start: "year",
            depth: "year"
        };
        function cancelListYear(){
            vm.workItemSearch.dateTo = null;
            vm.workItemSearch.dateFrom = null;
        }
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
                vm.searchForm.dateTo=null;
                vm.searchForm.dateFrom=null;

            }

        }
    }
})();
