/**
 * Created by pm1_os24 on 2/24/2018.
 */
/**
 * Created by pm1_os42 on 2/23/2018.
 */
function constructionMerchandiseFunction($scope, $rootScope, $timeout, gettextCatalog,$filter,
                                  kendoConfig, $kWindow,constructionService,
                                  CommonService, PopupConst, Restangular, RestEndpoint,Constant,vm){
   

    vm.listDSVT=[];
    vm.listDSTB=[];
    //chinhpxn20180620
    vm.listConstrReturn=[];
    //chinhpsn20180620
    vm.initDataMer = function(){
    	searchMerchandiseForSave();
        Restangular.all("constructionService"+"/searchMerchandise").post(vm.constrObj).then(function(data){
          populateData(data);
            showVTTBA();
        },function(err){

        });
    }
    
    //chinhpxn20180620
    function searchMerchandiseForSave() {
    	Restangular.all("constructionService"+"/searchMerchandiseForSave").post(vm.constrObj).then(function(data){
    		vm.listConstrReturn = data.data;
          },function(err){
          });
    }
    //chinhpxn20180620

    function populateData(data){
        if(getListDSVT(data.data)== null && getListDSTB(data.data)==null){
            toastr.error("Không có vật tư thiết bị hoàn trả");
        } else{
        vm.listDSVT = getListDSVT(data.data);
        vm.listDSTB = getListDSTB(data.data);
        }
    }

    function getListDSVT(data){
        var list=[];
        for(var i in data){
            if (data[i].goodsIsSerial==0) {
                list.push(data[i]);
            };
        }
        return list;
    }

    function getListDSTB(data){
        var list=[];
        for(var i in data){
            if (data[i].goodsIsSerial==1) {
                list.push(data[i]);
            };
        }
        return list;
    }

    function showVTTBA(){
        vm.merchandisegrid1.dataSource.data(vm.listDSVT);
        vm.merchandisegrid1.refresh();
        vm.merchandisegrid2.dataSource.data(vm.listDSTB);
        vm.merchandisegrid2.refresh();
    }

    vm.updateMerchandiseItem=updateMerchandiseItem;
    function updateMerchandiseItem(){
        if(vm.constrObj.returner==null||vm.constrObj.returner==''){
            toastr.warning("Người hoàn trả không được để trống!")
            return;
        }
        if(vm.constrObj.returnDate==null||vm.constrObj.returnDate==''){
            toastr.warning("Ngày hoàn trả không được để trống!")
            return;
        }
        vm.constrObj.listFileMerchandise = vm.merchandisegrid.dataSource._data
        //chinhpxn20180620
        Restangular.all("constructionService"+"/searchMerchandiseForSave").post(vm.constrObj).then(function(data){
        	vm.constrObj.listConstructionReturn = data;
        	constructionService.updateMerchandiseItem(vm.constrObj).then(function(data){
                if (data.error) {
                    toastr.error("Có lỗi xảy ra");
                    return;
                }
                toastr.success("Ghi lại thành công!");
                vm.initDataNT();
            },function(error){
                toastr.error("Có lỗi xảy ra");
            })
          },function(err){
          });
        //chinhpxn20180620
        ;
    }

    vm.downloadFile = function(dataItem){
        window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + dataItem.filePath;
    }
    $scope.onSelectMerchandiseFile = function(e) {
        if($("#fileMerchandise")[0].files[0].name.split('.').pop() !='xls' && $("#fileMerchandise")[0].files[0].name.split('.').pop() !='xlsx' && $("#fileMerchandise")[0].files[0].name.split('.').pop() !='doc'&& $("#fileMerchandise")[0].files[0].name.split('.').pop() !='docx'&& $("#fileMerchandise")[0].files[0].name.split('.').pop() !='pdf' ){
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
        if(104857600<$("#fileMerchandise")[0].files[0].size){
            toastr.warning("Dung lượng file vượt quá 100MB! ");
            return;
        }
        var formData = new FormData();
        jQuery.each(jQuery('#fileMerchandise')[0].files, function(i, file) {
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
                jQuery.each(jQuery('#fileMerchandise')[0].files, function(index, file) {
                    var obj={};
                    obj.name=file.name;
                    obj.filePath=data[index];
                    obj.createdDate = new Date();

                    obj.createdUserName = Constant.userInfo.casUser.fullName
                    vm.constrObj.returnDate = kendo.toString(new Date(),'dd/MM/yyyy');
                    vm.constrObj.returner = Constant.userInfo.casUser.fullName
                    vm.merchandisegrid.dataSource.add(obj)
                });
                vm.merchandisegrid.refresh();
                setTimeout(function() {
                    $(".k-upload-files.k-reset").find("li").remove();
                    $(".k-upload-files").remove();
                    $(".k-upload-status").remove();
                    $(".k-upload.k-header").addClass("k-upload-empty");
                    $(".k-upload-button").removeClass("k-state-focused");
                },10);
                $("#fileNameMerchandise")[0].innerHTML=$("#fileMerchandise")[0].files[0].name
            }
        });
    }
    function refreshMerchandiseGrid(data,grid) {
        if(grid){
            grid.dataSource.data(data);
            grid.refresh();
        }
    }
    vm.merchandisegridOptions = kendoConfig.getGridOptions({
        autoBind: true,
        scrollable: false,
        resizable: true,
        editable: false,
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
                }
            },
            {
                title: "Tên file",
                field: 'name',
                width: '40%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                template: function (dataItem) {
                    return "<a href='' ng-click='vm.downloadFile(dataItem)'>" + dataItem.name + "</a>";
                }
            },
            {
                title: "Ngày upload",
                field: 'createdDate',
                width: '20%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:center;"
                },
                template: function (dataItem) {
                    return $filter('date')(dataItem.createdDate, 'dd/MM/yyyy');
                }
            },
            {
                title: "Người upload",
                field: 'createdUserName',
                width: '20%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }
            },
            {
                title: "Xóa",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                template: dataItem =>
            '<div class="text-center">'
            + '<button style=" border: none; background-color: white;" id=""' +
            'class=" icon_table" ng-click="vm.removeMerchandiseItemFile($event)"  uib-tooltip="Xóa" translate' + '>' +
            '<i class="fa fa-trash" style="color: #337ab7;" aria-hidden="true"></i>' +
            '</button>'
            + '</div>',
            width: '15%'
}
]
//    dataSource:{
//        data:[
//            { catStationCode:'10',constructionCode:'1' }]
//    }
});
vm.merchandisegridOptions1 = kendoConfig.getGridOptions({
    autoBind: true,
    scrollable: false,
    resizable: true,
    editable: false,
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
            }
        },
        {
            title: "Mã VTTB",
            field: 'goodsCode',
            width: '20%',
            headerAttributes: {style: "text-align:center;font-weight: bold;"},
            attributes: {
                style: "text-align:left;"
            }
        },
        {
            title: "Tên VTTB",
            field: 'goodsName',
            width: '40%',
            headerAttributes: {style: "text-align:center;font-weight: bold;"},
            attributes: {
                style: "text-align:left;"
            }
        },
        {
            title: "Đơn vị tính",
            field: 'goodsUnitName',
            width: '15%',
            headerAttributes: {style: "text-align:center;font-weight: bold;"},
            attributes: {
                style: "text-align:left;"
            }
        },
        {
            title: "SL trả lại",
            field: 'slConLai',
            width: '20%',
            headerAttributes: {style: "text-align:center;font-weight: bold;"},
            attributes: {
                style: "text-align:right;"
            }
        }
    ]
//    dataSource:{
//        data:[
//            { catStationCode:'10',constructionCode:'1' }]
//    }
});
vm.merchandisegridOptions2 = kendoConfig.getGridOptions({
    autoBind: true,
    scrollable: false,
    resizable: true,
    editable: false,
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
            }
        },
        {
            title: "Mã VTTB",
            field: 'goodsCode',
            width: '20%',
            headerAttributes: {style: "text-align:center;font-weight: bold;"},
            attributes: {
                style: "text-align:left;"
            }
        },
        {
            title: "Tên VTTB",
            field: 'goodsName',
            width: '40%',
            headerAttributes: {style: "text-align:center;font-weight: bold;"},
            attributes: {
                style: "text-align:center;"
            }
        },
        {
            title: "Đơn vị tính",
            field: 'goodsUnitName',
            width: '15%',
            headerAttributes: {style: "text-align:center;font-weight: bold;"},
            attributes: {
                style: "text-align:left;"
            }
        },
        {
            title: "Serial",
            field: 'serial',
            width: '20%',
            headerAttributes: {style: "text-align:center;font-weight: bold;"},
            attributes: {
                style: "text-align:left;"
            }
        }
    ]
//    dataSource:{
//        data:[
//            { catStationCode:'10',constructionCode:'1' }]
//    }
});
     vm.removeMerchandiseItemFile = removeMerchandiseItemFile
    function removeMerchandiseItemFile(e) {
        var grid = vm.merchandisegrid;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
        grid.removeRow(dataItem); //just gives alert message
        grid.dataSource.remove(dataItem); //removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
    }
}
