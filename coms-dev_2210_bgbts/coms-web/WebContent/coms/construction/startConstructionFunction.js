/**
 * Created by pm1_os24 on 2/24/2018.
 */
/**
 * Created by pm1_os42 on 2/23/2018.
 */
function startConstructionFunction($scope, $rootScope, $timeout, gettextCatalog,$filter,
                                  kendoConfig, $kWindow,constructionService,
                                  CommonService, PopupConst, Restangular, RestEndpoint,Constant,vm){
    vm.removeStartItemFile = removeStartItemFile
    function removeStartItemFile(e) {
        var grid = vm.startgrid;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
        grid.removeRow(dataItem); //just gives alert message
        grid.dataSource.remove(dataItem); //removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
    }

    vm.updateStartItem=updateStartItem;
    function updateStartItem(){
        if(vm.constrObj.startingDate==undefined||vm.constrObj.startingDate==''){
            toastr.warning("Ngày khởi công không được để trống!")
            return;
        }
        if(vm.constrObj.excpectedCompleteDate==undefined||vm.constrObj.excpectedCompleteDate==''){
            toastr.warning("Ngày kết thúc dự kiến không được để trống!")
            return;
        }
        if(vm.startgrid!=undefined && vm.startgrid.dataSource!=undefined)
            vm.constrObj.listFileStart = vm.startgrid.dataSource._data
        constructionService.updateStartItem(vm.constrObj).then(function(data){
            if (data.error) {
                toastr.error("Có lỗi xảy ra");
                return;
            }
            if(vm.constrObj.status == '2'){
                vm.constrObj.status ='3';
            };
            if(Number(vm.constrObj.status||0)  < 3){
                vm.construction = true;
            }else if(Number(vm.constrObj.status||0)  >= 3){
                vm.construction = false;
            };
            toastr.success("Ghi lại thành công!" );
        },function(error){
            toastr.error("Có lỗi xảy ra");
        });
    }

    vm.downloadFile = function(dataItem){
        window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + dataItem.filePath;
    }
    $scope.onSelectStartFile = function(e) {
        if($("#fileStart")[0].files[0].name.split('.').pop() !='xls' && $("#fileStart")[0].files[0].name.split('.').pop() !='xlsx' && $("#fileStart")[0].files[0].name.split('.').pop() !='doc'&& $("#fileStart")[0].files[0].name.split('.').pop() !='docx'&& $("#fileStart")[0].files[0].name.split('.').pop() !='pdf' ){
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
        if(104857600<$("#fileStart")[0].files[0].size){
            toastr.warning("Dung lượng file vượt quá 100MB! ");
            return;
        }
        var formData = new FormData();
        jQuery.each(jQuery('#fileStart')[0].files, function(i, file) {
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
                jQuery.each(jQuery('#fileStart')[0].files, function(index, file) {
                    var obj={};
                    obj.name=file.name;
                    obj.filePath=data[index];
                    obj.createdDate = new Date();
                    obj.createdUserName = Constant.userInfo.casUser.fullName
                    vm.startgrid.dataSource.add(obj)
                });
                vm.startgrid.refresh();
                setTimeout(function() {
                    $(".k-upload-files.k-reset").find("li").remove();
                    $(".k-upload-files").remove();
                    $(".k-upload-status").remove();
                    $(".k-upload.k-header").addClass("k-upload-empty");
                    $(".k-upload-button").removeClass("k-state-focused");
                },10);
                $("#fileNameStart")[0].innerHTML=$("#fileStart")[0].files[0].name
            }
        });
    }
    function refreshStartGrid(data,grid) {
        if(grid){
            grid.dataSource.data(data);
            grid.refresh();
        }
    }
    vm.startgridOptions = kendoConfig.getGridOptions({
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
            'class=" icon_table" ng-click="vm.removeStartItemFile($event)"  uib-tooltip="Xóa" translate' + '>' +
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
}
