(function() {
    'use strict';
    var controllerId = 'rpHSHCController';

    angular.module('MetronicApp').controller(controllerId, rpHSHCController);
    function rpHSHCController($scope, $rootScope, $timeout, gettextCatalog,rpHSHCService,
                                          kendoConfig, $kWindow,$filter,htmlCommonService,
                                          CommonService, PopupConst, Restangular, RestEndpoint,Constant) {

        var vm = this;
        vm.searchForm={};
        vm.d={};
        var record=0;
        $scope.listCheck = [];
        vm.String = "Quản lý công trình > Báo cáo > Chi tiết hồ sơ hoàn công";
        vm.constrObj={};
        vm.constrTaskObj={};
        vm.obj={};
        init();





        function init(){
        	var date = new Date();
        	var start = new Date(date.getFullYear(), date.getMonth(), 1);
        	var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
        	vm.searchForm.dateTo = htmlCommonService.formatDate(end);
        	vm.searchForm.dateFrom = htmlCommonService.formatDate(start);
        }
        
        vm.constructionTaskGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
//            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },	
            reorderable: true,
            toolbar: [
                {
                    name: "actions",
                    template: 
                    '<div class="btn-group pull-right margin_top_button ">' + 
                    '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: triệu VNĐ</div>'+
                    '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                    '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportexcelHSHC()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                    '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                    '<label ng-repeat="column in vm.constructionTaskGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                    '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}' +
                    '</label>' +
                    '</div></div>'
                }
            ],
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                        $("#appCount").text("" + response.total);
                        vm.count = response.total;
                        return response.total; // total is returned in
                        // the "total" field of
                        // the response
                    },
                    data: function (response) {
                        vm.list = response.data;

                        return response.data; // data is returned in
                        // the "data" field of
                        // the response
                    },
                    model: {
                    	fields: {
                    		stt: {editable: false},
                    		dateComplete: {editable: false},
                    		sysGroupName: {editable: false},
                    		catStationCode: {editable: false},
                    		
                    		constructionCode: {editable: false},
                    		cntContract: {editable: false},
                    		completeValue: {editable: false},
                    		status: {editable: false},
                    	}
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "rpHSHCService/doSearchForConsManager",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        // vm.yearPlanSearch.employeeId =
                        // Constant.user.srvUser.catEmployeeId;
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
                        style: "text-align:center;",
                       
                    },
                  
                }, {
                    title: "Ngày thực hiện",
                    width: '10%',
                    field: "dateComplete",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
                    // template:function(dataItem){
                    // return formatDate(dataItem.dateComplete);
                    // }
                }, {
                    title: "Đơn vị thực hiện",
                    field: 'sysGroupName',
                    width: '25%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                }, 
//              hoanm1_20181203_start
                {
                    title: "Mã nhà trạm",
                    field: 'catStationHouseCode',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                },
//                hoanm1_20181203_end
                {
                    title: "Mã trạm",
                    field: 'catStationCode',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                },  {
                    title: "Mã công trình",
                    field: 'constructionCode',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                }, {
                    title: "Chứng từ",
                    field: 'cntContract',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                 {
                    title: "HSHC kế hoạch",
                    width: '10%',
                     field: 'completeValue',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                     format: "{0:n3}",
                    template:function(dataItem){
                        return numberWithCommas(dataItem.completeValue||0);
                    },
                    footerTemplate: function(item) {
                    	var data = vm.constructionTaskGrid.dataSource.data();
                    	var item, sum = 0;
                        for (var idx = 0; idx < data.length; idx++) {
                        	if (idx == 0) {
                        		item = data[idx];
//                        		sum = item.totalQuantity;
                        		sum =numberWithCommas(item.totalQuantity||0);
                        		break;
                        	}
                        }
                        return kendo.toString(sum, "");
					},
                },
                {
                    title: "Trạng thái",
                    field: 'status',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: function(dataItem){
                        if(dataItem.status==4){
                        	return "Đã tạm dừng"
                        }
                        if(dataItem.status==5){
                        	return "Đã hoàn thành"
                        }
                        if(dataItem.status==6){
                        	return "Đã nghiệm thu"
                        }
                    
                        if(dataItem.status==7){
                    	return "Đã hoàn công"
                        }
                        if(dataItem.status==8){
                    	return "Đã quyết toán" 
                        }else{
                        	return "Đã hoàn công"
                        }
                    }
                 },
                 {		
                	 field: "receiveRecordsDate",
                	 title:"Ngày nhận HSHC",
//                	 template: dataItem=> "<input kendo-date-picker id='abc' ng-model='dataItem.receiveRecordsDate' ng-change='vm.updateDayHshc(dataItem)' style='width: 90px;' min-year='1000' date-time />",
                	 editor: function(container,options){
                		 var stringDate = "";
                		var input= $("<input ng-model='dataItem.receiveRecordsDate' ng-change='vm.updateDayHshc(dataItem)' style='width: 90px;' />");
                		input.attr("receiveRecordsDate", options.field);
                		input.appendTo(container);
                		input.kendoDatePicker({
                			format:"dd/MM/yyyy",
                			
                		});
                	 },
                     
                	
                	 width: '100px',
                     headerAttributes: {style: "text-align:center;font-weight: bold;"},
                     attributes: {
                         style: "text-align:left;"
                     }       
                 }
            ]
        });
        


function sumHSHC(){
    var data = vm.constructionHSHCGrid.dataSource._data;
    var sumHSHC =0;
    for (var i=0;i< data.length;i++) {
        sumHSHC += data[i].quantity;
    };
    vm.sumHSHC = numberWithCommas(sumHSHC);
}

    function getConstructionTask(){
        var list=[];
        var data = vm.constructionTaskGrid.dataSource._data;
        vm.constructionTaskGrid.table.find("tr").each(function(idx, item) {
            var row = $(item);
            var checkbox = $('[name="gridcheckbox"]', row);
            if (checkbox.is(':checked')) {
                var tr = vm.constructionTaskGrid.select().closest("tr");
                var dataItem = vm.constructionTaskGrid.dataItem(item);
                list.push(dataItem.constructionTaskId);
            }
        });
        return list;
    }

function getConstruction(){
    var list=[];
    var data = vm.constructionTaskGrid.dataSource._data;
    vm.constructionTaskGrid.table.find("tr").each(function(idx, item) {
        var row = $(item);
        var checkbox = $('[name="gridcheckbox"]', row);
        if (checkbox.is(':checked')) {
            var tr = vm.constructionTaskGrid.select().closest("tr");
            var dataItem = vm.constructionTaskGrid.dataItem(item);
            list.push(dataItem.constructionId);
        }
    });
    return list;
}



        function refreshGrid(d) {
            var grid = vm.constructionTaskGrid;
            if (grid) {
                grid.dataSource.data(d);
                grid.refresh();
            }
        }
      //HuyPQ-25/08/2018-edit-start
        vm.exportexcelHSHC= function(){
        	function displayLoading(target) {
        	      var element = $(target);
        	      kendo.ui.progress(element, true);
        	      setTimeout(function(){
        	    	  
        	    	  return Restangular.all("rpHSHCService/exportContructionHSHC").post(vm.searchForm).then(function (d) {
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
        // HuyPQ-end
        function formatDate(date) {
            if (date!=null) {
                return date.substring(3);
            }
            return '';            
        }

        vm.openDepartmentTo1=openDepartmentTo1

        function openDepartmentTo1(popUp){
            vm.obj={};
            vm.departmentpopUp=popUp;
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null,null, vm, popUp, 'string', false, '92%','89%');
        }

        vm.doSearch = function(){
            vm.constructionTaskGrid.dataSource.page(1);
        }
        vm.exportpdf= function(){
            var ds1=$("#rpConstructionTask").data("kendoGrid").dataSource.data();
            if (ds1.length === 0){
                toastr.error(gettextCatalog.getString("Không có dữ liệu xuất "));
                return ;
            }
            var binarydata= new Blob([ds1],{ type:'application/pdf'});
            kendo.saveAs({dataURI: binarydata, fileName: "a" + '.pdf'});
        }

        vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'dept') {
                vm.searchForm.sysGroupName = data.text;
                vm.searchForm.sysGroupId = data.id;
            }
        }

        vm.openDepartmentTo1=openDepartmentTo1

        function openDepartmentTo1(popUp){
            vm.obj={};
            vm.departmentpopUp=popUp;
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null,null, vm, popUp, 'string', false, '92%','89%');
        }

        vm.cancelListYear= cancelListYear;
        function cancelListYear(){
        	vm.searchForm.dateFrom = null;
        	vm.searchForm.dateTo = null;
        }

vm.constructionHSHCGridOptions = kendoConfig.getGridOptions({
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
            title: "Mã hạng mục",
            field: 'code',
            width: '40%',
            headerAttributes: {style: "text-align:center;font-weight: bold;"},
            attributes: {
                style: "text-align:left;"
            }
        },
        {
            title: "Loại hạng mục",
            field: 'name',
            width: '20%',
            headerAttributes: {style: "text-align:center;font-weight: bold;"},
            attributes: {
                style: "text-align:left;"
            }
        },
        {
            title: "Trạng thái",
            field: 'status',
            width: '10%',
            headerAttributes: {style: "text-align:center;font-weight: bold;"},
            attributes: {
                style: "text-align:left;"
            },
            template: "# if(status == 1){ #" + "#= 'Chưa thực hiện' #" + "# } " + "else if (status == 2) { # " + "#= 'Đang thực hiện' #" + "#}" + "else if (status == 3) { # " + "#= 'Đã hoàn thành' #" + "#} #",
            type :'text',
            editable:false
        },
        {
            title: "Giá trị sản lượng(triệu  VNĐ)",
            headerAttributes: {style: "text-align:center;font-weight: bold;"},
            field: 'quantity'
            ,
            template: function(data){
                                    return numberWithCommas(data.quantity);
                                },
            attributes: {
                style: "text-align:right;"
            },
            width: '20%'
}
]
// dataSource:{
// data:[
// { catStationCode:'10',constructionCode:'1' }]
// }
});

function numberWithCommas(x) {
    if(x == null || x == undefined){
    return '0';
    }
    x=x/1000000;
    var parts = x.toFixed(2).toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
}

vm.BGMBgridOptions = kendoConfig.getGridOptions({
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
        'class=" icon_table" ng-click="caller.removeBGMBItemFile($event)"  uib-tooltip="Xóa" translate' + '>' +
        '<i class="fa fa-trash" style="color: #337ab7;" aria-hidden="true"></i>' +
        '</button>'
        + '</div>',
        width: '15%'
}
]
// dataSource:{
// data:[
// { catStationCode:'10',constructionCode:'1' }]
// }
});

vm.showHideWorkItemColumn = function (column) {
    if (angular.isUndefined(column.hidden)) {
        vm.constructionTaskGrid.hideColumn(column);
    } else if (column.hidden) {
        vm.constructionTaskGrid.showColumn(column);
    } else {
        vm.constructionTaskGrid.hideColumn(column);
    }


}
/*
 * * Filter các cột của select
 */

vm.gridColumnShowHideFilter = function (item) {

    return item.type == null || item.type !== 1;
};

vm.cancelPopup = cancelPopup;
function cancelPopup(){
    CommonService.dismissPopup1();
}

vm.updateBGMBItem=updateBGMBItem;
function updateBGMBItem(){
    var obj = contructionHSHCService.getItem();
    obj.listFileHSHC = vm.BGMBgrid.dataSource._data
    return Restangular.all("constructionService"+"/updateHSHCItem").post(obj).then(function(data){
        if(data.error){
            toastr.error(data.error);
            return;
        }
        toastr.success("Hủy hoàn thành thành công!");
        CommonService.dismissPopup1();
        vm.constructionTaskGrid.dataSource.page(1);
    },function(error){
        toastr.error("Có lỗi xảy ra");
    });
}

vm.removeBGMBItemFile = removeBGMBItemFile
    function removeBGMBItemFile(e) {
        var grid = vm.BGMBgrid;
        var row = $(e.target).closest("tr");
        var dataItem = grid.dataItem(row);
        grid.removeRow(dataItem); // just gives alert message
        grid.dataSource.remove(dataItem); // removes it actually from the grid
        grid.dataSource.sync();
        grid.refresh();
    }

vm.downloadFile = function(dataItem){
    window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + dataItem.filePath;
}
vm.onSelectBGMBFile = function(e) {
    if($("#fileBGMB")[0].files[0].name.split('.').pop() !='xls' && $("#fileBGMB")[0].files[0].name.split('.').pop() !='xlsx' && $("#fileBGMB")[0].files[0].name.split('.').pop() !='doc'&& $("#fileBGMB")[0].files[0].name.split('.').pop() !='docx'&& $("#fileBGMB")[0].files[0].name.split('.').pop() !='pdf' ){
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
    if(104857600<$("#fileBGMB")[0].files[0].size){
        toastr.warning("Dung lượng file vượt quá 100MB! ");
        return;
    }
    var formData = new FormData();
    jQuery.each(jQuery('#fileBGMB')[0].files, function(i, file) {
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
            jQuery.each(jQuery('#fileBGMB')[0].files, function(index, file) {
                var obj={};
                obj.name=file.name;
                obj.filePath=data[index];
                obj.createdDate = new Date();
                obj.createdUserName = Constant.userInfo.casUser.fullName
                dataFile.push(obj);
                vm.BGMBgrid.dataSource.add(obj)
            });
            vm.BGMBgrid.refresh();
            setTimeout(function() {
                $(".k-upload-files.k-reset").find("li").remove();
                $(".k-upload-files").remove();
                $(".k-upload-status").remove();
                $(".k-upload.k-header").addClass("k-upload-empty");
                $(".k-upload-button").removeClass("k-state-focused");
            },10);
        }
    });
}

        vm.edit = edit;

        function edit(dataItem) {
                contructionHSHCService.setItem(dataItem);
            // //vm.constrObj=data;
            // console.log(obj);
            // console.log(data);
            // vm.obj1={};
            vm.constrObj=dataItem;
                // vm.obj1= obj;
                var teamplateUrl = "coms/contructionHSHC/contructionHSHCPopup.html";
                var title = "Thông tin chi tiết hồ sơ hoàn công";
                var windowId = "CONTRUCTION_HSHC";
                CommonService.populatePopupHSHC(teamplateUrl, title, vm.constrObj, vm, windowId, true, '1000', '700','provinceCode' );
        }



        $scope.$on("PopupHSHC.open", function () {
            vm.constrObj.typeHSHC = 1;
            Restangular.all('constructionService'+"/construction/getConstructionHSHCById").post(vm.constrObj).then(function(data){
                vm.constrObjDetail={};
                vm.constrObjDetail=data;
                var grid = vm.BGMBgrid;
                if (grid) {

                    grid.dataSource.data(vm.constrObjDetail.listFileHSHC);
                    grid.refresh();
                }
                grid=vm.constructionHSHCGrid;
                if (grid) {
                    grid.dataSource.data(vm.constrObjDetail.listWorkItem);
                    grid.refresh();
                }
                sumHSHC();
            },function(err){
                toastr.error("Có lỗi xảy ra!")
            });

        });


        vm.selectedDept1=false;

        vm.deprtOptions1 = {
            dataTextField: "text",
            dataValueField:"id",
        	placeholder:"Nhập mã hoặc tên đơn vị",
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
        vm.cancelInput = function(id){
            if(id=='month') {
                vm.searchForm.monthYear = ''
            }
            if(id=='dept') {
                vm.searchForm.sysGroupName = ''
                vm.searchForm.sysGroupId = undefined;
            }
        }
        vm.cancelConfirmPopup=function(){
            CommonService.dismissPopup1();
        }

    	vm.openCatProvincePopup = openCatProvincePopup;
    	vm.onSaveCatProvince = onSaveCatProvince;
    	vm.clearProvince = clearProvince;
        function openCatProvincePopup(){
    		var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm tỉnh");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
        }
        function onSaveCatProvince(data){
            vm.searchForm.catProvinceId = data.catProvinceId;
            vm.searchForm.catProvinceCode = data.code;
    		vm.searchForm.catProvinceName = data.name;
            htmlCommonService.dismissPopup();
            $("#provincename").focus();
        };
    	function clearProvince (){
    		vm.searchForm.catProvinceId = null;
    		vm.searchForm.catProvinceCode = null;
    		vm.searchForm.catProvinceName = null;
    		$("#provincename").focus();
    	}
        vm.provinceOptions = {
            dataTextField: "name",
            dataValueField: "id",
    		placeholder:"Nhập mã hoặc tên tỉnh",
            select: function (e) {
                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.searchForm.catProvinceId = dataItem.catProvinceId;
                vm.searchForm.catProvinceCode = dataItem.code;
    			vm.searchForm.catProvinceName = dataItem.name;
            },
            pageSize: 10,
            open: function (e) {
                vm.isSelect = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.isSelect = false;
                        return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
                            name: vm.searchForm.catProvinceName,
                            pageSize: vm.provinceOptions.pageSize,
                            page: 1
                        }).then(function (response) {
                            options.success(response.data);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
    		headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
    		'<p class="col-md-6 text-header-auto border-right-ccc">Mã tỉnh</p>' +
    		'<p class="col-md-6 text-header-auto">Tên tỉnh</p>' +
    		'</div>',
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
            	if (!vm.isSelect) {
                    vm.searchForm.catProvinceId = null;
                    vm.searchForm.catProvinceCode = null;
    				vm.searchForm.catProvinceName = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.searchForm.catProvinceId = null;
                    vm.searchForm.catProvinceCode = null;
    				vm.searchForm.catProvinceName = null;
                }
            }
        }

    }
})();
