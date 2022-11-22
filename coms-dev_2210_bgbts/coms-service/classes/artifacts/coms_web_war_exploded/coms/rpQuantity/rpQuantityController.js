(function () {
    'use strict';
    var controllerId = 'rpQuantityController';

    angular.module('MetronicApp').controller(controllerId, rpQuantityController);

    function rpQuantityController($scope, $rootScope, $timeout, gettextCatalog,
                                kendoConfig, $kWindow, rpQuantityService,
                                CommonService, PopupConst, Restangular, RestEndpoint, Constant, htmlCommonService) {
        var vm = this;
        vm.showSearch = true;
        vm.isCreateNew = false;
        vm.showDetail = false;
//        vm.cancelListYear= function()
//        {
//            vm.workItemSearch.yearList = [];
//        }
        $scope.listCheck = [];
        vm.workItemSearch = {};
        vm.String = "Quản lý công trình > Báo cáo > Chi tiết sản lượng";
        vm.workItem = {};
        vm.changeImage = changeImage;
        vm.imageSelected = {};
        
        
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
//        	hungnx 270718 start init date in month
        	var date = new Date();
        	var start = new Date(date.getFullYear(), date.getMonth(), 1);
        	var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
        	vm.workItemSearch.dateTo = htmlCommonService.formatDate(end);
        	vm.workItemSearch.dateFrom = htmlCommonService.formatDate(start);
//        	hungnx 270718 end
            fillDataTable([]);
            initDropDownList();
            initMonthYear();
        }

        function initMonthYear(){

            $scope.monthSelectorOptions = {
                start: "year",
                depth: "year"
            };

        }
        function initDropDownList() {
            vm.yearDataList = [];
            vm.monthDataList = [];
            var currentYear = (new Date()).getFullYear();
            for (var i = currentYear - 2; i < currentYear + 19; i++) {
                vm.yearDataList.push({
                    id: i,
                    name: i

                })
            }

            for (var i = 1; i < 13; i++) {
                vm.monthDataList.push({

                    id: i,
                    name: i+'/'+(currentYear)

                })
            }
            vm.yearDownListOptions = {
                dataSource: vm.yearDataList,
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true
            }
            vm.monthDownListOptions = {
                dataSource: vm.monthDataList,
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true
            }
        }
        vm.d={}
        vm.validatorOptions = kendoConfig.get('validatorOptions');
        vm.formatAction = function (dataItem) {
            var template =
                '<div class="text-center #=workItemId#"">'
            template += '<button type="button"' +
            'class="btn btn-default padding-button box-shadow  #=workItemId#"' +
            'disble="" ng-click=vm.edit(#=workItemId#)>' +
            '<div class="action-button edit" uib-tooltip="Sửa" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
            '</button>' +
            '<button type="button"' +
            'class="btn btn-default padding-button box-shadow #=workItemId#"' +
            'ng-click=vm.send(#=workItemId#)>' +
            '<div class="action-button export" uib-tooltip="Gửi tài chính" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
            '</button>' +
            '<button type="button"' +
            'class="btn btn-default padding-button box-shadow #=workItemId#"' +
            'ng-click=vm.remove(#=workItemId#)>' +
            '<div class="action-button del" uib-tooltip="Xóa" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
            '</button>'
            +
            '<button type="button" class="btn btn-default padding-button box-shadow #=workItemId#"' +
            'ng-click=vm.cancelUpgradeLta(#=workItemId#)>' +
            '<div class="action-button cancelUpgrade" uib-tooltip="Hủy nâng cấp" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
            '</button>';
            template += '</div>';
            return dataItem.groupId;
        }
        setTimeout(function () {
            $("#keySearch").focus();
        }, 15);
        /*
         * setTimeout(function(){ $("#appIds1").focus(); },15);
         */
        var record = 0;

        function fillDataTable(data) {
            vm.workItemGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable:true,
                save : function(){
                    vm.workItemGrid.refresh();
                },
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
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-disabled="vm.disableExportExcel" ng-click="vm.exportWorkItemFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">' +
                        //'<label ng-repeat="column in vm.workItemGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                        '<label ng-repeat="column in vm.workItemGrid.columns.slice(1,vm.workItemGrid.columns.length)| filter: vm.gridColumnShowHideFilter">' +

                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#appCountWorkItem").text("" + response.total);
                            vm.countWorkItem = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "rpQuantityService/doSearchQuantity",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            // vm.constructionSearch.employeeId =
                            // Constant.user.srvUser.catEmployeeId;
                            vm.workItemSearch.page = options.page
                            vm.workItemSearch.pageSize = options.pageSize
                            return JSON.stringify(vm.workItemSearch)

                        }
                    },
                    pageSize: 10,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                edit: function(e) {

                    if(e.model.statusConstruction == "6" || e.model.statusConstruction == "7" || e.model.statusConstruction == "8"){

                        e.sender.closeCell();

                    }

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
                edit: function(dataItem) {
//                	chinhpxn20180719_start
                	dataItem.model.oldQuantity = dataItem.model.quantity;
//                	chinhpxn20180719_end
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
                        },
                        type :'text',
                        editable:false

                    },
                    {
                        title: "Ngày thực hiện",
                        width: '10%',
                        field:"dateComplete",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        editable:false,
//                        hungnx 20180714 start
                        footerTemplate: function(item) {
                        	var data = vm.workItemGrid.dataSource.data();
                        	var item, sum = 0;
                            for (var idx = 0; idx < data.length; idx++) {
                            	if (idx == 0) {
                            		item = data[idx];
                            		sum = item.countDateComplete;
                            		break;
                            	}
                            }
                            return kendo.toString(sum, "");
						},
                    },
                    {
                        title: "Đơn vị thực hiện",
                        field: 'constructorName',
                        width: '25%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Đối tác thi công",
                        field: 'constructorName1',
                        width: '25%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Mã trạm",
                        field: 'catstationCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false,
                        footerTemplate: function(item) {
                        	var data = vm.workItemGrid.dataSource.data();
                        	var sum = 0;
                            for (var idx = 0; idx < data.length; idx++) {
                            	if (idx == 0) {
                            		sum = data[idx].countCatstationCode;
                            		break;
                            	}
                            }
                            return kendo.toString(sum, "");
						},
                    },
                    {
                        title: "Mã công trình",
                        field: 'constructionCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false,
                        footerTemplate: function(item) {
                        	var data = vm.workItemGrid.dataSource.data();
                        	var sum = 0;
                            for (var idx = 0; idx < data.length; idx++) {
                            	if (idx == 0) {
                            		sum = data[idx].countConstructionCode;
                            		break;
                            	}
                            }
                            return kendo.toString(sum, "");
						},
                    },
                    {
                        title: "Hạng mục",
                        field: 'name',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false,
                        footerTemplate: function(item) {
                        	var data = vm.workItemGrid.dataSource.data();
                        	var sum = 0;
                            for (var idx = 0; idx < data.length; idx++) {
                            	if (idx == 0) {
                            		sum = data[idx].countWorkItemName;
                            		break;
                            	}
                            }
                            return kendo.toString(sum, "");
						},

                    },

                    {
                        title: "Sản lượng kế hoạch",
                        field: 'quantity',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        format: "{0:n3}",
                        type :'number',
                        editable: true,
                        
                        footerTemplate: function(item) {
                        	var data = vm.workItemGrid.dataSource.data();
                        	var sum = 0;
                            for (var idx = 0; idx < data.length; idx++) {
                            	if (idx == 0) {
                            		sum = data[idx].totalQuantity;
                            		break;
                            	}
                            }
                            return kendo.toString(sum, "n3");
						},
                    },
                    {
                        title: "Trạng thái",
                        field: 'status',
                        width: '10%',
                        template: function (dataItem) {
                            if (dataItem.status == 1) {
                                return "<span name='status' font-weight: bold;'>Chưa thực hiện</span>"
                            } else if (dataItem.status == 2) {
                                return "<span name='status' font-weight: bold;'>Đang thực hiện</span>"
                            } else if (dataItem.status == 3) {
                                return "<span name='status' font-weight: bold;'>Đã hoàn thành</span>"
                            }
                        },
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false
                    },
    ]
    });
}

	function quantityEdited(dataItem) {
		dataItem.isQuantityEdited = 1;
	}

vm.disableExportExcel = false;
//HuyPQ-25/08/2018-edit-start
vm.exportWorkItemFile= function(){
	function displayLoading(target) {
      var element = $(target);
      kendo.ui.progress(element, true);
      setTimeout(function(){
    	  
		if (vm.disableExportExcel)
        		return;
        	vm.disableExportExcel = true;
        	return Restangular.all("rpQuantityService/exportWorkItemServiceTask").post(vm.workItemSearch).then(function (d) {
        	    var data = d.plain();
        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	    kendo.ui.progress(element, false);
        	    vm.disableExportExcel = false;
        	}).catch(function (e) {
        		kendo.ui.progress(element, false);
        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        	    vm.disableExportExcel = false;
        	    return;
        	});
	});
		
  }
	displayLoading("#workItemGrid");
}
//HuyPQ-edit-end

vm.import = function () {
    var teamplateUrl = "coms/workItem/workItemPopup.html";
    var title = "Import từ file excel";
    var windowId = "YEAR_PLAN";
    CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '700', 'auto', "files");
}


function refreshGrid(d) {
    var grid = vm.workItemGrid;
    if (grid) {
        grid.dataSource.data(d);
        grid.refresh();
    }
}


vm.cancel = cancel;
function cancel() {
    vm.showDetail = false;
    vm.workItemSearch = {}
    vm.workItem = {};
    vm.doSearch();
}

function getConstructionTask(){
    var list=[];
    var data = vm.workItemGrid.dataSource._data;
    vm.workItemGrid.table.find("tr").each(function(idx, data) {
        var row = $(data);
        var checkbox = $('[name="gridcheckbox"]', row);
        if (checkbox.is(':checked')) {
            var tr = vm.workItemGrid.select().closest("tr");
            var dataItem = vm.workItemGrid.dataItem(data);
            list.push(dataItem.workItemId);
        }
    });
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

vm.cancelConfirmPopup = cancelConfirmPopup;
function cancelConfirmPopup(){
    CommonService.dismissPopup1();
    //vm.showDetail=false;
}



vm.cancelDoSearch = function () {
    vm.showDetail = false;
    vm.workItemSearch = {
        status: "1"
    };
    doSearch();
}

vm.doSearch = doSearch;
function doSearch() {
    vm.showDetail = false;
    var grid = vm.workItemGrid;
    if (grid) {
        grid.dataSource.query({
            page: 1,
            pageSize: 10
        });
    }
}


vm.showHideWorkItemColumn = function (column) {
    if (angular.isUndefined(column.hidden)) {
        vm.workItemGrid.hideColumn(column);
    } else if (column.hidden) {
        vm.workItemGrid.showColumn(column);
    } else {
        vm.workItemGrid.hideColumn(column);
    }


}
/*
 * * Filter các cột của select
 */

vm.gridColumnShowHideFilter = function (item) {

    return item.type == null || item.type !== 1;
};


vm.exportpdf = function () {
    var obj = {};
    workItemService.exportpdf(obj);
}

vm.errNumber = "";
vm.checkNumber = checkNumber;
function checkNumber() {
    var val = $('#parOder').val();
    if (val === 0) {
        if (val === 0) {
            if (val === "") {
                vm.errNumber = "";
            } else {
                vm.errNumber = "Phải nhập kiểu số nguyên từ 1-99";
                return false;
            }

        }
    } else {
        var isNaN = function (val) {
            if (Number.isNaN(Number(val))) {
                return false;
            }
            return true;
        }
        if (isNaN(val) === false) {
            vm.errNumber = "Phải nhập kiểu số nguyên từ 1-99";
        } else {
            vm.errNumber = "";
        }

    }


}

vm.openDepartmentTo = openDepartmentTo

function openDepartmentTo(popUp) {
    vm.obj = {};
    vm.departmentpopUp = popUp;
    var templateUrl = 'coms/popup/findDepartmentPopUp.html';
    var title = gettextCatalog.getString("Tìm kiếm đơn vị");
    CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
}

vm.onSave = onSave;
function onSave(data) {
    if (vm.departmentpopUp === 'dept') {
        vm.workItemSearch.sysGroupName = data.text;
        vm.workItemSearch.sysGroupId = data.id;
    }
}

// clear data
vm.changeDataAuto = changeDataAuto
function changeDataAuto(id) {
    switch (id) {
        case 'dept':
        {
            if (processSearch(id, vm.selectedDept1)) {
                vm.workItemSearch.sysGroupId = null;
                vm.workItemSearch.sysGroupName = null;
                vm.selectedDept1 = false;
            }
            break;
        }
    }
}

vm.cancelInput = function (param) {
    if (param == 'dept') {
        vm.workItemSearch.sysGroupId = null;
        vm.workItemSearch.sysGroupName = null;
    }
}
    vm.cancelListYear= cancelListYear;
function cancelListYear(){
//    vm.workItemSearch.monthYear = null;
//	hungnx 20170703 start
	vm.workItemSearch.dateTo = null;
	vm.workItemSearch.dateFrom = null;
//	hungnx 20170703 end
}
// 8.2 Search SysGroup
vm.selectedDept1 = false;
vm.deprtOptions1 = {
    dataTextField: "text",
    dataValueField: "id",
	placeholder:"Nhập mã hoặc tên đơn vị",
    select: function (e) {
        vm.selectedDept1 = true;
        var dataItem = this.dataItem(e.item.index());
        vm.workItemSearch.sysGroupName = dataItem.text;
        vm.workItemSearch.sysGroupId = dataItem.id;
    },
    pageSize: 10,
    open: function (e) {
        vm.selectedDept1 = false;
    },
    dataSource: {
        serverFiltering: true,
        transport: {
            read: function (options) {
                vm.selectedDept1 = false;
                return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({
                    name: vm.workItemSearch.sysGroupName,
                    pageSize: vm.deprtOptions1.pageSize
                }).then(function (response) {
                    options.success(response);
                }).catch(function (err) {
                    console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                });
            }
        }
    },
    template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
    change: function (e) {
        if (e.sender.value() === '') {
            vm.workItemSearch.sysGroupName = null;// thành name
            vm.workItemSearch.sysGroupId = null;
        }
    },
    ignoreCase: false
}
vm.getExcelTemplate = function () {
    workItemService.downloadTemplate({}).then(function (d) {
        var data = d.plain();
        window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
    }).catch(function () {
        toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        return;
    });
}

    vm.cancelImport = cancelImport;
    function cancelImport() {
        CommonService.dismissPopup1();
    }
    vm.downloadFile = downloadFile;
     function downloadFile(path){
         window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + path;
     }
    vm.removeImage=removeImage
    function removeImage(image,list){
        list.splice(list.indexOf(image), 1);
        }

    function changeImage(image) {
    	vm.imageSelected = image;
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
        vm.workItemSearch.catProvinceId = data.catProvinceId;
        vm.workItemSearch.catProvinceCode = data.code;
		vm.workItemSearch.catProvinceName = data.name;
        htmlCommonService.dismissPopup();
        $("#provincename").focus();
    };
	function clearProvince (){
		vm.workItemSearch.catProvinceId = null;
		vm.workItemSearch.catProvinceCode = null;
		vm.workItemSearch.catProvinceName = null;
		$("#provincename").focus();
	}
    vm.provinceOptions = {
        dataTextField: "name",
        dataValueField: "id",
		placeholder:"Nhập mã hoặc tên tỉnh",
        select: function (e) {
            vm.isSelect = true;
            var dataItem = this.dataItem(e.item.index());
            vm.workItemSearch.catProvinceId = dataItem.catProvinceId;
            vm.workItemSearch.catProvinceCode = dataItem.code;
			vm.workItemSearch.catProvinceName = dataItem.name;
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
                        name: vm.workItemSearch.catProvinceName,
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
                vm.workItemSearch.catProvinceId = null;
                vm.workItemSearch.catProvinceCode = null;
				vm.workItemSearch.catProvinceName = null;
            }
        },
        close: function (e) {
            if (!vm.isSelect) {
                vm.workItemSearch.catProvinceId = null;
                vm.workItemSearch.catProvinceCode = null;
				vm.workItemSearch.catProvinceName = null;
            }
        }
    }

}
})();
