/**
 * Created by pm1_os42 on 2/24/2018.
 */
function acceptanceFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
    kendoConfig, $kWindow, constructionService,
    CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm){


    //Ben A
    vm.listDSVTA=[];
    vm.listDSTBA=[];
    //Ben B
    vm.listDSVTB=[];
    vm.listDSTBB=[];
    vm.listDSTP =[];
    vm.listStatus=[];
    vm.TBObj={};
    vm.showTabVT=1;
    vm.certObj={};
    vm.dataStatus={};
    vm.checkStatus = true;
    vm.showVTTB = function(value){
        vm.showTabVT = value;
    }
    
    vm.initDataNT = function(){
        Restangular.all("constructionService"+"/construction/getStockStrans").post(vm.constrObj.constructionId).then(function(data){
            populateData(data);
            showVTTBA();
            showVTTBB();
            showTP();
        },function(err){
            
        });
    }

    function populateData(data){
        vm.listDSVTA = data.listDsvtDto;
        vm.listDSTBA = data.listSynStockTrans;
        vm.listDSVTB = data.listDSVTBB;
        vm.listDSTBB = data.listStockTrans;
        vm.listDSTP = data.listDSTP;
        /*vm.listStatus = data.listStatusObs;
        var a = vm.listStatus.0;*/
        vm.dataStatus.status = data.lstatus;
        if((data.lstatus==4 && data.lstatus1==2)||data.lstatus==5||data.lstatus=="6"){
            vm.checkStatus=false;
        }else if(data.lstatus > 6){
            vm.checkStatus=true;
        }
        if(data.certDTO!=null){
            vm.certObj = data.certDTO;
        }

    }

    function showVTTBA(){
        vm.DSVTAGrid.dataSource.data(vm.listDSVTA);
        vm.DSVTAGrid.refresh();
        vm.DSTBAGrid.dataSource.data(vm.listDSTBA);
        vm.DSTBAGrid.refresh();
    }

    function showVTTBB(){
        vm.DSVTBGrid.dataSource.data(vm.listDSVTB);
        vm.DSVTBGrid.refresh();
        vm.DSTBBGrid.dataSource.data(vm.listDSTBB);
        vm.DSTBBGrid.refresh();
    }
    function showTP(){
        vm.TPGrid.dataSource.data(vm.listDSTP);
        vm.TPGrid.refresh();
       
    }
    function showVTTBTP(){
        
    }
    function validateCert(){
        if(vm.certObj.importer==null ||vm.certObj.importer==''){
            toastr.warning("Chưa nhập tên người nhập thông tin!")
            return false;
        }
        if(vm.certObj.startingDate==null ||vm.certObj.startingDate==''){
            toastr.warning("Chưa nhập từ ngày !  ")
            return false;
        }
        return true
    }

    vm.addVTTBTP = function (obj){
        if (validateTP()) {
            var grid=vm.TPGrid;
            if (grid) {
                var data=grid.dataSource._data;
                for (var i =0; i<data.length;i++) {
                    if (obj.serial==data[i].serial && obj.serial!=null) {
                        toastr.warning("Serial đã tồn tại");
                        return ;
                    }
                };
                grid.dataSource.add(obj);
                grid.refresh();
            };
        };
        
    }

    function validateTP(){
        if (vm.TBObj.goodsName==null || vm.TBObj.goodsName=='') {
            toastr.warning('Chưa nhập tên VTTB')
            return
        }
        if (vm.TBObj.quantity==null || vm.TBObj.quantity=='') {
            toastr.warning('Chưa nhập số lượng')
            return
        }
        if (vm.TBObj.goodsUnitName==null || vm.TBObj.goodsUnitName=='') {
            toastr.warning('Chưa nhập đơn vị tính')
            return
        }
        return true;
    }

    vm.saveMerchandise =function(){
        if(validateCert()){
            var data =vm.constrObj;
            //danh sach thiet bi ben B
            tbbb=vm.DSTBBGrid.dataSource._data;
            if(tbbb.length==0){
                data.listStockTrans = null;
            }else{
                data.listStockTrans = getListStockTrans();
            }
            data.listSynStockTrans = getListSynStockTrans();
            //thiet bi ben A
            tbba = vm.DSTBAGrid.dataSource._data;
            if(tbba.length==0){
                data.listDsvtDto = null;
            }else{
                data.listDsvtDto = getListDsvtDto();
            }
            //danh sach  vat tu ben B
            data.listDSVTBB = getListDSVTTB();
            data.certDTO = vm.certObj;
            data.listTP = getListTP();
//          hungnx 20180619 start
            vm.disableSubmit = true;
//          hungnx 20180619 end
            Restangular.all("constructionService"+"/construction/saveMerchandise").post(data).then(function(data){
                if (data.error) {
                    toastr.error("Có lỗi xảy ra");
//                  hungnx 20180619 start
                    vm.disableSubmit = false;
//                  hungnx 20180619 end
                    return;
                }
                var grid = vm.DSVTAGrid;
                if (grid) {
                    grid.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });

                };
                toastr.success("Ghi lại thành công!");
 //               phucvx_07/06
                vm.initDataMer();
                initFormData();
                /*initWorkItemTable();
                vm.initConvenant();
                vm.deliveryBill();
                vm.contractInputGrid();
                vm.entangledGrid();
                vm.initComplete();*/
 //phucend
//              hungnx 20180619 start
                vm.disableSubmit = false;
//              hungnx 20180619 end
            },function(error){
                toastr.error("Có lỗi xảy ra");
//              hungnx 20180619 start
                vm.disableSubmit = false;
//              hungnx 20180619 end
            });
        }
    }
// list data ds vat tu ben B
    function getListDSVTTB(){
        var list=[];
        var data = vm.DSVTBGrid.dataSource._data;
        for (var i =0; i<data.length;i++) {
            data[i].remainQuantity=data[i].numberXuat-data[i].numberThuhoi-data[i].numberSuDung;
            list.push(data[i]);
        };
 //       return list;
        return vm.DSVTBGrid.dataSource._data;
    }
//danh sach thiet bi ben B
    function getListStockTrans(){
        var list=[];
        vm.DSTBBGrid.table.find("tr").each(function(idx, item) {
            var row = $(item);
            var checkbox = $('[name="gridcheckbox"]', row);
            var dataItem = vm.DSTBBGrid.dataItem(item);
            if (checkbox.is(':checked')) {
                var tr = vm.DSTBBGrid.select().closest("tr");
//                var dataItem = vm.DSTBAGrid.dataItem(item);
                 dataItem.employ = 1;
            }
            else{
                var tr = vm.DSTBBGrid.select().closest("tr");
//                var dataItem = vm.DSTBAGrid.dataItem(item);
                 dataItem.employ = 0;
            }
            list.push(dataItem);
        });
//        return list;
         return vm.DSTBBGrid.dataSource._data;
    }
//List vat tu ben A
    function getListSynStockTrans(){
        var list=[];
        var data = vm.DSVTAGrid.dataSource._data;
        for (var i =0; i<data.length;i++) {
            data[i].remainQuantity=data[i].numberXuat-data[i].numberThuhoi-data[i].quantity;
            list.push(data[i]);
        };
//        return list;
        return vm.DSVTAGrid.dataSource._data;
    }
    //thiet bi ben A
    function getListDsvtDto(){
        var list=[];
        vm.DSTBAGrid.table.find("tr").each(function(idx, item) {
            var row = $(item);
            var checkbox = $('[name="gridcheckbox"]', row);
            var dataItem = vm.DSTBAGrid.dataItem(item);
            if (checkbox.is(':checked')) {
                var tr = vm.DSTBAGrid.select().closest("tr");
//                var dataItem = vm.DSTBAGrid.dataItem(item);
                 dataItem.employ = 1;
            }
            else{
                var tr = vm.DSTBAGrid.select().closest("tr");
//                var dataItem = vm.DSTBAGrid.dataItem(item);
                 dataItem.employ = 0;
            }
            list.push(dataItem);
        });
//        return list;
        return vm.DSTBAGrid.dataSource._data;
    }

    function getListTP(){
        var list=[];
        var data = vm.TPGrid.dataSource._data;
        for (var i =0; i<data.length;i++) {
            data[i].type=3;
            data[i].constructionId=vm.constrObj.constructionId;
            if (data[i].serial==null || data[i].serial=='' ) {
                data[i].goodsIsSerial = 0;
            }else{
                data[i].goodsIsSerial = 1;
            }
            list.push(data[i]);
        };

        return list;
    }

    function numberWithCommas(x) {
        if(x == null || x == undefined){
            return '0';
        }
        var parts = x.toFixed(2).toString().split(".");
        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        return parts.join(".");
    }


    vm.DSVTAGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                save : function(){
                    vm.DSVTAGrid.refresh();
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                pageable: {
                    refresh: false,
                    pageSize: 5,
                    pageSizes: [5,10, 15, 20, 25],
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
                        editable: false,
                        type:'text',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }

                    },
                    {
                        title: "Mã VTTB",
                        field: 'goodsCode',
                        width: '10%',
                        type:'text',
                        editable: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }

                    },
                    {
                        title: "Tên VTTB",
                        field: 'goodsName',
                        type:'text',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        editable: false
                    },
                    {
                        title: "Đơn vị tính",
                        field: 'goodsUnitName',
                        width: '10%',
                        type:'text',
                        editable: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "SL xuất",
                        field: 'numberXuat',
                        width: '10%',
                        type: 'number',
                        editable: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function(data){
                            return numberWithCommas(data.numberXuat);
                        }
                    }, {
                        title: "SL thu hồi",
                        field: 'numberThuhoi',
                        width: '10%',
                        type: 'number',
                        editable: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function(data){
                            return numberWithCommas(data.numberThuhoi);
                        }
                    },

                    {
                        title: "SL sử dụng",
                        field: 'quantity',
                        width: '10%',
                        type: 'number',
                        //chinhpxn20180618
                        editable: false,
                        //chinhpxn20180618
                        
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {	
                            style: "text-align:right;"
                        },
                        template: function(data){
                            if((data.quantity>data.numberXuat-data.numberThuhoi)||data.quantity==null){
                                data.quantity= data.numberXuat - data.numberThuhoi;
                                 return numberWithCommas(data.quantity);
                            }
                            if(data.quantity<0){
                                data.quantity=0;
                                return numberWithCommas(data.quantity);
                            }
                            else
                                return numberWithCommas(data.quantity);
                        }
                    },
                    {
                        title: "SL còn lại",
                        field: 'remainQuantity',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'number',
                        editable: false,
                        template: function(data){
                           return numberWithCommas(data.numberXuat-data.numberThuhoi-data.quantity);
                        }
                    },
                    {
                        
                        field: 'goodsIsSerial',
                        hidden: true
                    },
                    {

                        field: 'merEntityId',
                        hidden: true
                    },
                    {
                        
                        field: 'goodsId',
                        hidden: true
                    }
                ]
            });
        

    vm.DSTBAGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                filterable: true,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                pageable: {
                    refresh: false,
                    pageSize: 5,
                    pageSizes: [5,10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
  //             dataBound: onDataBound,
                columns: [
                    {
                        title: "TT",
                        field: "stt",
                        editable: false,
                        template: function () {
                            return ++record;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Mã VTTB",
                        field: 'goodsCode',
                        width: '10%',
                        editable: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tên VTTB",
                        field: 'goodsName',
                        editable: false,
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Đơn vị tính",
                        editable: false,
                        field: 'goodsUnitName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Serial",
                        field: 'serial',
                        editable: false,
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Sử dụng",
                        width: '10%',
                        editable: true,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template : function(data){
                        	//chinhpxn20180618
//                            if(data.employ==1||vm.dataStatus.status==5){
                            	if(data.employ==1){
                                return "<input disabled type='checkbox' /*ng-click='vm.check()' */ name='gridcheckbox' ng-checked='true'/>"
                            }
                            else{
                                return "<input disabled type='checkbox' /*ng-click='vm.check()'*/   name='gridcheckbox'  />"
                            }
                            //chinhpxn20180618
                        }
                    },
                    {
                        
                        field: 'consQuantity',
                        hidden: true
                    },
                    {

                        field: 'remainQuantity',
                        hidden: true
                    },
                    {

                        field: 'goodsIsSerial',
                        hidden: true
                    },
                    {

                        field: 'constructionMerchadiseId',
                        hidden: true
                    },
                    {

                        field: 'merEntityId',
                        hidden: true
                    },
                    {
                        
                        field: 'detailSerial',
                        hidden: true
                    }
                ]
            });    


    vm.DSVTBGridOptions = kendoConfig.getGridOptions({
        autoBind: true,
        scrollable: false,
        resizable: true,
        save : function(){
            vm.DSVTBGrid.refresh();
        },
        dataBinding: function () {
            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
        },
        pageable: {
            refresh: false,
            pageSize: 5,
            pageSizes: [5,10, 15, 20, 25],
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
                editable: false,
                type:'text',
                columnMenu: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:center;"
                }

            },
            {
                title: "Mã VTTB",
                field: 'goodsCode',
                width: '10%',
                type:'text',
                editable: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }

            },
            {
                title: "Tên VTTB",
                field: 'goodsName',
                type:'text',
                width: '15%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: false
            },
            {
                title: "Đơn vị tính",
                field: 'goodsUnitName',
                width: '10%',
                type:'text',
                editable: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }
            },
            {
                title: "SL xuất",
                field: 'numberXuat',
                width: '10%',
                type: 'number',
                editable: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                template: function(data){
                    return numberWithCommas(data.numberXuat);
                }
            },
            {
                title: "SL thu hồi",
                field: 'numberThuhoi',
                width: '10%',
                type: 'number',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: false,
                template: function(data){
                    return numberWithCommas(data.numberThuhoi);
                }
            },
            {
                title: "SL sử dụng",
                field: 'numberSuDung',
                width: '10%',
                type: 'number',
                //chinhpxn20180618 
                editable: false,
                //chinhpxn20180618
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                template: function(data){
                	//chinhpxn20180619
//                    if((data.numberSuDung > (data.numberXuat-data.numberThuhoi))||(data.numberSuDung == null)){
//                        data.numberSuDung = data.numberXuat-data.numberThuhoi;
//                    }
                    if(data.numberSuDung == null){
                        data.numberSuDung = 0;
                    }
                    //chinhpxn20180619
                    return numberWithCommas(data.numberSuDung);
                }
            },
            {
                title: "SL còn lại",
                field: 'remainQuantity',
                width: '10%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                type: 'number',
                editable: false,
                template: function(data){
                    return numberWithCommas(data.numberXuat-data.numberThuhoi-data.numberSuDung);
                }
            },
            {

                field: 'goodsIsSerial',
                hidden: true
            },
            {

                field: 'merEntityId',
                hidden: true
            },
            {

                field: 'goodsId',
                hidden: true
            }
        ]
    });
        

    vm.DSTBBGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                pageable: {
                    refresh: false,
                    pageSize: 5,
                    pageSizes: [5,10, 15, 20, 25],
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
                    },
                    {
                        title: "Mã VTTB",
                        field: 'goodsCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tên VTTB",
                        field: 'goodsName',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Đơn vị tính",
                        field: 'goodsUnitName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Serial",
                        field: 'serial',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Sử dụng",
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template : function(data){
                        	//chinhpxn20180618
//                            if(data.employ==1||vm.dataStatus.status==5){
                        	if(data.employ==1){
                        	//chinhpxn20180618
                                return "<input disabled type='checkbox' name='gridcheckbox' ng-checked='true'/>"
                            }
                            else{
                                return "<input disabled type='checkbox' name='gridcheckbox'  />"
                            }
                        }
                    },
                    {
                        
                        field: 'consQuantity',
                        hidden: true
                    },
                    {

                        field: 'remainQuantity',
                        hidden: true
                    },
                    {

                        field: 'goodsIsSerial',
                        hidden: true
                    },
                    {

                        field: 'merEntityId',
                        hidden: true
                    },
                    {
                        
                        field: 'goodsId',
                        hidden: true
                    }
                ]
            });    

    vm.TPGridOptions = kendoConfig.getGridOptions({
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
                        template: 
                        '<div class="btn-group pull-right margin_top_button margin10">' +
                        '<button class="btn btn-qlk text-Btn padding-search-right closeQLK  "' +
                        'ng-click="vm.deleteAll()" uib-tooltip="Xóa tất cả" translate>Xóa tất cả</button>' +
                        '</div>'
                    }
                ],
        pageable: {
            refresh: false,
            pageSize: 5,
            pageSizes: [5,10, 15, 20, 25],
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
            },

            {
                title: "Tên VTTB",
                field: 'goodsName',
                width: '33%',
                headerAttributes: {style: "text-align:left;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }
            },
            {
                title: "Serial",
                field: 'serial',
                width: '22%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }
            },
            {
                title: "Số lượng sử dụng",
                field: 'quantity',
                width: '15%',
                headerAttributes: {style: "text-align:right;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                }
            },
            {
                title: "Đơn vị tính",
                field: 'goodsUnitName',
                width: '15%',
                headerAttributes: {style: "text-align:left;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }
            },
            
            
            {
                title: "Xóa",
                width: '10%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:center;"
                },
                template : function(data){
                        return '<button style=" border: none; background-color: white;" id=""' +
                    'class=" icon_table" ng-click="vm.removeTP($event)"  uib-tooltip="Xóa" translate' + '>' +
                    '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
                    '</button>'
                    
                }
            },
            {
                
                field: 'type',
                hidden: true
            },
            {

                field: 'constructionId',
                hidden: true
            },
            {

                field: 'goodsIsSerial',
                hidden: true
            }
        ]
    });    
     vm.deleteAll = function (){
        var teamplateUrl = "coms/construction/deleteAll.html";
        var title = "Xác nhận xóa dữ liệu";
        var windowId = "DELETE";
        CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '350', 'auto', "code");
     }
     vm.deleteTC = function (){
        $("#TP").data('kendoGrid').dataSource.data([]);
        CommonService.dismissPopup1();
    }
    vm.cancelConfirmPopup = function (){
        CommonService.dismissPopup1();
    }
    vm.removeTP = removeTP;
    function removeTP(e){
 //       vm.TPGrid.dataSource.remove(data);
        var grid = vm.TPGrid;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
        grid.removeRow(dataItem); //just gives alert message
        grid.dataSource.remove(dataItem); //removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
        
    }
//  hungnx 20180619 start
    vm.disableSubmit = false;
//  hungnx 20180619 end
    vm.saveThauphu = function(){
        var data =vm.constrObj;
        data.listTP = getListTP();
//        hungnx 20180619 start
        vm.disableSubmit = true;
//      hungnx 20180619 end
        Restangular.all("constructionService"+"/saveThauPhu").post(data).then(function(data){
            if (data.error) {
                toastr.error("Có lỗi xảy ra");
                return;
            } else
            	toastr.success("Ghi lại thành công!");
//          hungnx 20180619 start
            vm.disableSubmit = false;
//          hungnx 20180619 end
        },function(error){
            toastr.error("Có lỗi xảy ra");
//          hungnx 20180619 start
            vm.disableSubmit = false;
//          hungnx 20180619 end
        });
    }

    vm.cancelThauPhu = function(){
        var data =vm.constrObj;
        data.listTP = getListTP();
        Restangular.all("constructionService"+"/cancelThauPhu").post(data).then(function(data){
            if (data.error) {
                toastr.error("Có lỗi xảy ra");
                return;
            }
            toastr.success("Huỷ lưu thầu phụ thành công!");
        },function(error){
            toastr.error("Có lỗi xảy ra");
        });
    }
  /* vm.listCheckTBA=[];
    vm.check = function(){
        var gridTBA = vm.DSTBAGrid;
        gridTBA.table.find("tr").each(function(idx, item){
            var row = $(item);
            var checkbox =$('[name="gridcheckbox"]',row);
            if(checkbox.is(':checked')){
                var dataItem = gridTBA.dataItem(item);
                vm.listCheckTBA.push(dataItem.detailSerial);
            }
            else{
                var dataItem = gridTBA.dataItem(item);
                var index = vm.listCheckTBA.indexOf(dataItem.detailSerial);
                if(index>-1){
                    vm.listCheckTBA.splice(index,1);
                }
            }
        });

    }
    function onDataBound(e){
        var dataItem=$("#DSTBA").data("kendoGrid").dataSource._data;
        for(var i =0;i<dataItem.length;i++){
            var row = $("[data-udi='"+ dataItem[i].uid + "']");
            var input = row.find("td").eq(5).find("input");
            if(vm.listCheckTBA.includes(dataItem[i].detailSerial)){
                input.prop('checked',true);
            }
        };
    }*/

   /* find('tr[data-uid=' + uid + ']')*/
    
}