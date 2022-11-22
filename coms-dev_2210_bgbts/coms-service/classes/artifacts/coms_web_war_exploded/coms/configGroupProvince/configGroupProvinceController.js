/**
 * Created by pm1_os36 on 09/05/2018.
 */
(function() {
    'use strict';
    var controllerId = 'configGroupProvinceController';

    angular.module('MetronicApp').controller(controllerId, configGroupProvinceController);

    function configGroupProvinceController($scope, $rootScope, $timeout, gettextCatalog,$filter,
                                      kendoConfig, $kWindow,configGroupProvinceService,
                                      CommonService, PopupConst, Restangular, RestEndpoint,Constant,$http) {
        var vm = this;
        vm.searchForm={
            obstructedState:1
        };
        vm.String="Quản lý công trình > Tiện ích và cấu hình > Cấu hình đơn vị tỉnh";
        vm.searchForm.dateBD=setCurrentDay();
        function setCurrentDay(){
            var today=new Date();
            return today.getDate()+'/'+(today.getMonth()+1)+'/'+today.getFullYear();
        }
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
            fillDataTable1();
            vm.rpDayQuantity = {};
            catProvinceList();
        }



        vm.listYearPlan=[];
        vm.validatorOptions = kendoConfig.get('validatorOptions');
        setTimeout(function(){
            $("#keySearch").focus();
        },15);
        /*
         * setTimeout(function(){ $("#appIds1").focus(); },15);
         */
        var record=0;

        vm.doSearch = function(){
            vm.provinceUnitGrid.dataSource.page(1);
        };

        vm.openSysGroup=openSysGroup

        function openSysGroup(popUp){
            vm.obj={};
            vm.departmentpopUp=popUp;
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null,null, vm, popUp, 'string', false, '92%','89%');
        }

        vm.exportexcel= exportexcel;
        function exportexcel(dataSearch){
            return Restangular.all("workItemService/exportSLTN").post(dataSearch).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function (e) {
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                return;
            });
        }


        vm.rpDayQuantity.obstructedState = 1;
        vm.option1Show = true;

        function fillDataTable1() {
            vm.provinceUnitgridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                toolbar: [
                    {
                        name: "actions",
                        template: '<div class=" pull-left ">' +
                        '<button class="btn btn-qlk padding-search-right addQLK"' +
                        'ng-click="vm.add()" uib-tooltip="Tạo mới" translate>Tạo mới</button>' +
                        '</div>'
                        +
                        '<div style="margin:0;" class="btn-group pull-right margin_top_button margin10 ">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.provinceUnitGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideEntangledGrid1Column(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#countRPDayQuantity").text(""+response.total);
                            
                            vm.count1 = response.total;                           
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        },
                    },

                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "configGroupProvinceService/doSearchCatprovince",
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
                    }, {
                        title: "Đơn vị",
                        field: 'sysGroupName',
                        width: '35%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    }, {
                        title: "Tỉnh",
                        field: 'catProvinceCode',
                        width: '60%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }

                    },
                    {
                        title: "Thao tác",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        template: dataItem =>
                        '<div class="text-center">'
                        + '<button style=" border: none; background-color: white;" id="updateId" ng-click="vm.edit(dataItem)" class=" icon_table "' +
                        '   uib-tooltip="Sửa" translate>' +
                        '<i class="fa fa-list-alt" style="color:#e0d014"  aria-hidden="true"></i>' +
                        '</button>'
                        + '<button style=" border: none; background-color: white;" id=""' +
                        'class=" icon_table" ng-click="vm.remove(dataItem)"  uib-tooltip="Xóa" translate' + '>' +
                        '<i class="fa fa-trash" style="color: #337ab7;" aria-hidden="true"></i>' +
                        '</button>'
                        + '</div>',
                        width: '10%'
                    }
                ]
            });
        }

        
        vm.showHideEntangledGrid1Column = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.provinceUnitGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.provinceUnitGrid.showColumn(column);
            } else {
                vm.provinceUnitGrid.hideColumn(column);
            }


        }
        vm.showHideEntangledGrid2Column = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.searchFormGrid2.hideColumn(column);
            } else if (column.hidden) {
                vm.searchFormGrid2.showColumn(column);
            } else {
                vm.searchFormGrid2.hideColumn(column);
            }


        }
        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };
        function numberWithCommas(x) {
            if(x == null || x == undefined){
                return '';
            }
            x = x/1000000;
            var parts = x.toFixed(2).toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
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

        vm.openDepartmentTo1=openDepartmentTo1
        vm.catProvinForm={};
        function openDepartmentTo1(popUp){
            vm.obj={};
            vm.departmentpopUp=popUp;
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null,null, vm, popUp, 'string', false, '92%','89%');
        }
        vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'dept') {
                vm.searchForm.sysGroupName = data.text;
                vm.searchForm.sysGroupId = data.id;
            }
            if (vm.departmentpopUp === 'dept1') {
                vm.catProvinForm.sysGroupName = data.text;
                vm.catProvinForm.sysGroupId = data.id;
            }
        }
       vm.DeleteDB = DeleteDB;
       function DeleteDB(d){
                d.sysGroupName = null ;
                d.sysGroupId= null;
       }
       //list tinh 
       function catProvinceList(){
            configGroupProvinceService.getCatProvince().then(function (data) {
                    vm.listCatProvince = data;
                }, function (e) {
                    toastr.error("Có lỗi trong quá trình lấy dữ liệu!");
                });
       }
      

      //them moi
      vm.add = add;
        function add() {
            var list1=vm.listCatProvince;
            for(var i=0;i<vm.listCatProvince.length;i++){
                vm.listCatProvince[i].check=false;
            }
 //           vm.listCatProvince=list1;
            vm.catProvinForm={};
            vm.workItemRecord={workItemTypeList:[]};
            vm.workItemRecord.isInternal = 1;
            vm.showHm = true;
            vm.showHm1= false;
            vm.disabledHangMuc = false;
            var teamplateUrl = "coms/configGroupProvince/CatProvincePopup.html";
            var title = "Thêm mới cấu hình đơn vị tỉnh";
            var windowId = "addProvince";
            CommonService.populatePopupPartner(teamplateUrl, title, null, vm, windowId, true, '1000', '550', "isInternal1");
        }
    
    vm.clearDV = clearDV;
    function clearDV(){
        vm.catProvinForm={
            sysGroupName : null,
            sysGroupId : null
        }
    }
    vm.cancel = cancel;
    function cancel(){
        $(".k-icon.k-i-close").click();
    }
    vm.saveCatProvinceCon = function(data){
        if(validateCreate(data)){
            configGroupProvinceService.saveCatProvince(data).then(function (d) {
            toastr.success("Ghi lại thành công!")
            cancel();
            vm.provinceUnitGrid.dataSource.page(1);
            }, function (e) {
                toastr.error("Có lỗi xãy ra!");
            });
        }
        
    }
    function validateCreate(data){
        if(data.sysGroupName==null || data.sysGroupName==''){
            toastr.warning("Bạn chưa chọn đơn vị");
            return;
        }
        else if(data.workItemTypeList==null || data.workItemTypeList==''){
            toastr.warning("Bạn chưa chọn tỉnh");
            return;
        }
        else return true;

    }
   
       

     // clear data
        vm.changeDV = changeDV
        vm.selectedDept2=false;
        function changeDV(id) {
            switch (id) {
                case 'dept1':
                {
                    if (processSearch(id, vm.selectedDept2)) {
                        vm.catProvinForm.sysGroupName = null;
                        vm.catProvinForm.sysGroupId = null;
                        vm.selectedDept2 = false;
                    }
                    break;
                }              
            }
        }

     vm.deprtOptions2 = {
            dataTextField: "text",
            dataValueField:"id",
            select: function(e) {
                vm.selectedDept2=true;
                var dataItem = this.dataItem(e.item.index());
                vm.catProvinForm.sysGroupName = dataItem.text;
                vm.catProvinForm.sysGroupId = dataItem.id;
            },
            pageSize: 10,
            open: function(e) {
                vm.selectedDept2 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function(options) {
                        vm.selectedDept2=false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({name:vm.catProvinForm.sysGroupName,pageSize:vm.deprtOptions1.pageSize}).then(function(response){
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
                    vm.catProvinForm.sysGroupName = null;// thành name
                    vm.catProvinForm.sysGroupId = null;
                }
            },
            ignoreCase: false
        }       

    vm.remove = function(data){
        confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function () {
            configGroupProvinceService.removeCat(data.sysGroupId).then(function(d){
                toastr.success("Xóa thành công!")
                vm.provinceUnitGrid.dataSource.page(1);
            },function(e){
                toastr.error("Lỗi khi xóa dữ liệu!")
            });
        })
    }
       vm.edit = edit;
        function edit(data) {
            var listMT = [];
            configGroupProvinceService.getListCode(data.sysGroupId).then(function(d){
                listMT = d;
                var listTinh =vm.listCatProvince;
                for(var i =0;i<listTinh.length;i++){
                    for(var j=0;j<listMT.length;j++){
                        if(listTinh[i].catProvinceCode==listMT[j].catProvinceCode){
                            listTinh[i].check=true;
                            break;
                        }
                        else{
                            listTinh[i].check=false;
                        }
                    }
                }
                vm.listCatProvince= listTinh;
                vm.workItemRecord={workItemTypeList:[]};
                vm.catProvinForm=data;
                vm.showHm = true;
                vm.showHm1= false;
 //\               vm.catProvinForm.workItemTypeList=d;
                var teamplateUrl = "coms/configGroupProvince/CatProvincePopup.html";
                var title = "Thêm mới cấu hình đơn vị tỉnh";
                var windowId = "addProvince";
                CommonService.populatePopupPartner(teamplateUrl, title, null, vm, windowId, true, '1000', '550', "isInternal1");
            });
        } 
}   
})();
