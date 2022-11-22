/**
 * Created by pm1_os42 on 2/23/2018.
 */
function constructionDetailFunction($scope, $rootScope, $timeout, gettextCatalog,$filter,
    kendoConfig, $kWindow,constructionService,
    CommonService, PopupConst, Restangular, RestEndpoint,Constant,vm){
    vm.removeVuongItemFile = removeVuongItemFile
    function removeVuongItemFile(e) {
        var grid = vm.vuongGrid;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
        grid.removeRow(dataItem); //just gives alert message
        grid.dataSource.remove(dataItem); //removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
    }

    vm.updateVuongItem=updateVuongItem;
    function updateVuongItem(dataItem){
        if(vm.addVuongItem.obstructedContent==undefined||vm.addVuongItem.obstructedContent==''){
            toastr.warning("Nội dung vướng không được để trống!")
            return;
        }
        if(vm.addVuongItem.obstructedState==undefined||vm.addVuongItem.obstructedState==''){
            toastr.warning("Xác nhận không được để trống!")
            return;
        }
        if(vm.vuongGrid!=undefined && vm.vuongGrid.dataSource!=undefined)
            vm.addVuongItem.listFileVuong = vm.vuongGrid.dataSource._data
            vm.addVuongItem.constructionId = vm.constrObj.constructionId;
        constructionService.updateVuongItem(vm.addVuongItem).then(function(data){
            if (data.error) {
                toastr.error("Có lỗi xảy ra!");
                return;
            }
            toastr.success("Ghi lại thành công!");
            vm.constrObj.obstructedState = data.obstructedState
            vm.constrObj.isObstructed = data.isObstructed
            vm.constrObj.status = data.status
            CommonService.dismissPopup1();
            vm.doSearchEl();
            //phuc_start
             initWorkItemTable();
             initFormData();
            vm.initDataNT();
                //phuc_end
        },function(error){
            toastr.error("Có lỗi xảy ra");
        });
    }
    vm.cancelVuongPopup = function(){
        CommonService.dismissPopup1();
        vm.doSearchEl();
    }
    vm.downloadFile = function(dataItem){
        window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + dataItem.filePath;
    }
    vm.onSelectVuongFile = function(e) {
        if($("#fileVuongPopup")[0].files[0].name.split('.').pop() !='xls' && $("#fileVuongPopup")[0].files[0].name.split('.').pop() !='xlsx' && $(fileVuongPopup)[0].files[0].name.split('.').pop() !='doc'&& $("#fileVuongPopup")[0].files[0].name.split('.').pop() !='docx'&& $("#fileVuongPopup")[0].files[0].name.split('.').pop() !='pdf' ){
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
        if(104857600<$("#fileVuongPopup")[0].files[0].size){
            toastr.warning("Dung lượng file vượt quá 100MB! ");
            return;
        }
        var formData = new FormData();
        jQuery.each(jQuery('#fileVuongPopup')[0].files, function(i, file) {
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
                jQuery.each(jQuery('#fileVuongPopup')[0].files, function(index, file) {
                    var obj={};
                    obj.name=file.name;
                    obj.filePath=data[index];
                    obj.createdDate = new Date();
                    obj.createdUserName = Constant.userInfo.casUser.fullName
                    vm.vuongGrid.dataSource.add(obj)
                });
                vm.vuongGrid.refresh();
                setTimeout(function() {
                    $(".k-upload-files.k-reset").find("li").remove();
                    $(".k-upload-files").remove();
                    $(".k-upload-status").remove();
                    $(".k-upload.k-header").addClass("k-upload-empty");
                    $(".k-upload-button").removeClass("k-state-focused");
                },10);
                $("#fileNameVuong")[0].innerHTML=$("#fileVuongPopup")[0].files[0].name
            }
        });
    }
    function refreshGrid(data,grid) {
        if(grid){
            grid.dataSource.data(data);
            grid.refresh();
        }
    }
    vm.vuongGridOptions = kendoConfig.getGridOptions({
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
                    return "<a href='' ng-click='caller.downloadFile(dataItem)'>" + dataItem.name + "</a>";
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
                'class=" icon_table" ng-click="caller.removeVuongItemFile($event)"  uib-tooltip="Xóa" translate' + '>' +
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
    vm.exportVuongFile= function(){
    	function displayLoading(target) {
  	      var element = $(target);
  	      kendo.ui.progress(element, true);
  	      setTimeout(function(){
  	    	  
  	    	return Restangular.all("workItemService/exportVuongFile").post(vm.entangledSearch).then(function (d) {
  	            var data = d.plain();
  	            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
  	            kendo.ui.progress(element, false);
  	        }).catch(function (e) {
  	        	kendo.ui.progress(element, false);
  	            toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
  	            return;
  	        });
  		});
  			
  	  }
  		displayLoading(".tab-content");
    }
}