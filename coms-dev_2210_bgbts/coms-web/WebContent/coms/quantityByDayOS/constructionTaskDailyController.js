(function () {
    'use strict';
    var controllerId = 'constructionTaskDailyController';

    angular.module('MetronicApp').controller(controllerId, constructionTaskDailyController);

    function constructionTaskDailyController($scope, $rootScope, $timeout, gettextCatalog,
                                kendoConfig, $kWindow, constructionTaskDailyService,
                                CommonService, PopupConst, Restangular, RestEndpoint, Constant, htmlCommonService) {
        var vm = this;
        vm.showSearch = true;
        vm.isCreateNew = false;
        vm.showDetail = false;
        $scope.listCheck = [];
        vm.constructionTaskDailySearch = {};
        vm.constructionTaskDaily = {};
        vm.String = "Quản lý công trình > Quản lý giá trị công trình > Phê duyệt sản lượng theo ngày ngoài OS";
        vm.workItem = {};
        vm.disableBtnExcel = false;
        vm.changeImage = changeImage;
        vm.imageSelected = {};
        
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
			var date = new Date();
        	var start = new Date(date.getFullYear(), date.getMonth(), 1);
        	var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
        	vm.constructionTaskDailySearch.endDate = htmlCommonService.formatDate(end);
        	vm.constructionTaskDailySearch.startDate = htmlCommonService.formatDate(start);
			
        	//vm.constructionTaskDailySearch.startDate = htmlCommonService.getFullDate();
        	//vm.constructionTaskDailySearch.endDate = htmlCommonService.getFullDate();
            fillDataTable([]);
        }

        vm.validatorOptions = kendoConfig.get('validatorOptions');
        setTimeout(function () {
            $("#keySearch").focus();
        }, 15);
        var record = 0;

        function fillDataTable(data) {
            vm.constructionTaskDailyGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable:true,
                save : function(){
                	vm.constructionTaskDailyGrids.refresh();
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: [
                    {
                        name: "actions",
                        template: '<div class=" pull-left ">' +

                        '<button   class="approveQuantityDaily btn btn-qlk padding-search-right"' +
                        'ng-click="vm.approveQuantityDayChecked()" uib-tooltip="Xác nhận" translate>Xác nhận</button>' +

                        '</div>'
                        +
                        '<div class="btn-group pull-right margin_top_button ">' +
                        '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: Triệu VNĐ</div>'+

                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i id="xuatExcel" ng-disabled="vm.disableBtnExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportConstructionTaskDaily()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">' +
                        '<label ng-repeat="column in vm.constructionTaskDailyGrids.columns.slice(1,vm.constructionTaskDailyGrids.columns.length)| filter: vm.gridColumnShowHideFilter">' +

                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#appCountTaskDailyDay").text("" + response.total);
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
                            url: Constant.BASE_SERVICE_URL + "constructionTaskDailyService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.constructionTaskDailySearch.page = options.page
                            vm.constructionTaskDailySearch.pageSize = options.pageSize
                            return JSON.stringify(vm.constructionTaskDailySearch)
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

                        title : "<input  type='checkbox' id='checkalllistdraw' name='gridchkselectall' ng-click='vm.chkSelectAll();' ng-model='vm.chkAll' />",
                        template: function(data){
                        	var isMonth = (!!data.createdDate) ? new Date().getMonth() + 1 ==  parseInt(data.createdDate.split('/')[1]) : false;
                            if (data.confirm != 1 && isMonth && data.confirm != 2) {
                                return "<input type='checkbox' id='childcheck' name='gridcheckbox' ng-click='vm.handleCheck(dataItem)'/>";
                            }else{
                                return '';
                            }
                        },
                        width: '5%',
                        headerAttributes: {style:"text-align:center;"},
                        attributes:{style:"text-align:center;"}
                    },
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
                        field:"createdDate",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Đơn vị thực hiện",
                        field: 'sysGroupName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Mã trạm",
                        field: 'catStationCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Mã công trình",
                        field: 'constructionCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Hạng mục",
                        field: 'workItemName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false
                    },{
                        title: "Công việc",
                        field: 'taskName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Số lượng",
                        field: 'amount',
                        width: '5%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'text',
                        format: "{0:n3}",
                        editable:false
                    }
                    ,{
                        title: "Sản lượng",
                        field: 'quantity',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        format: "{0:n0}",
                        type :'number',
                        editable:false
                    },
                    {
                        title: "Trạng thái",
                        field: 'confirm',
                        width: '10%',
                        template: function (dataItem) {
                            if (dataItem.confirm == 1) {
                                return "<span name='status' font-weight: bold;'>Đã xác nhận</span>"
                            } else if (dataItem.confirm == 0) {
                                return "<span name='status' font-weight: bold;'>Chưa xác nhận</span>"
                            } else if (dataItem.confirm == 2) {
                            	return "<span name='status' font-weight: bold;'>Đã từ chối</span>"
                            } else {
                                return "<span name='status' font-weight: bold;'></span>"
                            }
                        },
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Thao tác",
                        type :'text',
                        editable:false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    template: function(dataItem) {
						var html = '<div class="text-center">'
		                    + '<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.editQuantityDaily(dataItem)" class=" icon_table "' +
		                    '   uib-tooltip="Xem chi tiết" translate>' +
		                    '<i class="fa fa-list-alt" style="color:#e0d014"     aria-hidden="true"></i>' +
		                    '</button>';
						var isMonth = (!!dataItem.createdDate) ? new Date().getMonth() + 1 ==  parseInt(dataItem.createdDate.split('/')[1]) : false;
						dataItem.isMonth = isMonth;
						if (isMonth && dataItem.confirm != 2) {
							html += '<button style=" border: none; background-color: white;" id=""' +
		                    'class=" icon_table" ng-click="vm.rejectTaskDaily(dataItem)" ng-show="dataItem.confirm==0"  uib-tooltip="Từ chối" translate' + '>' +
		                    '<i class="fa fa-times-circle" style="color: #B00D0D;" aria-hidden="true"></i>' +

		                    '</button>'
							+ '<button style=" border: none; background-color: white;" id=""' +
		                    'class=" icon_table" ng-click="vm.cancelApproveTaskDaily(dataItem)" ng-show="dataItem.confirm==1"  uib-tooltip="Hủy xác nhận" translate' + '>' +
		                    '<i class="fa fa-reply" style="color: rgb(113, 219, 66);"  aria-hidden="true"></i>' +

		                    '</button>'
		                    + '<button style=" border: none; background-color: white;" id="approveQuantityOS"' +
		                    'class=" icon_table" ng-click="vm.approveQuantityOS(dataItem)" ng-show="dataItem.confirm != 1"  uib-tooltip="Xác nhận" translate' + '>' +
		                    '<i class="fa fa-check" style="color: #00FF00;"  aria-hidden="true"></i>' +
		                    
		                    '</button>';
						}
						html += '</div>';
						return html;
					},
                    width: '20%',
                    field: "action"
        }
    ]
    });
}
        
vm.approveQuantityOS = approveQuantityOS;
function approveQuantityOS(dataItem){
	debugger;
    var list = [];
        if (dataItem.amount == null) {
            toastr.error(gettextCatalog.getString("Bạn chưa nhập số lượng đã thi công"));
        } else {
        	var pass = true;
        	if (dataItem.amount < 0) {
				pass = false;
				return false;
			}
        	if (!pass) {
        		toastr.error("Số lượng không được âm !");
        		return;
        	}
        	/*constructionTaskDailyService.validPriceConstruction(dataItem).then(function (response) {
        		if (!!response && !!response.error) {
        			toastr.error(response.error);
        			return;
        		}*/
        		cancelConfirmPopup();
                confirm('Xác nhận bản ghi đã chọn?', function () {

                    Restangular.all("constructionTaskDailyService/approveQuantityByDay").post(dataItem).then(function (data) {
                        if (data.error) {
                            toastr.error(data.error);
                            return;
                        }
                        vm.constructionTaskDailyGrids.dataSource.page(1);

                        toastr.success("Xác nhận thành công !");

                    }).catch(function (e) {
                        toastr.error(gettextCatalog.getString("Lỗi khi phê duyệt"));
                        return;
                    });
                });
//            }, function (errResponse) {
//                toastr.error(errResponse);
//            });
        }
}


vm.handleCheck = handleCheck;
function handleCheck(dataItem) {
        if (dataItem.selected) {
             $scope.listCheck.push(dataItem);
        } else {
                for (var i = 0; i < $scope.listCheck.length; i++) {
                    if ($scope.listCheck[i].workItemId === dataItem.workItemId) {
                        $scope.listCheck.splice(i, 1);
                    }
                }
                $('[name="gridchkselectall"]').prop('checked', false);
        }

}

vm.chkSelectAll = chkSelectAll;
$scope.checkSearch = false;
function chkSelectAll() {
    var grid = vm.constructionTaskDailyGrids;
    chkSelectAllBase(vm.chkAll, grid);
    for (var i = 0; i < vm.constructionTaskDailyGrids._data.length; i++) {
    if (vm.chkAll) {
            if ($scope.checkSearch && $scope.dataSearch.length > 0) {
                $scope.listCheck = $scope.dataSearch;
            } else {
            }

    } else {
        $scope.listCheck = [];
    }
//    })
}
};

vm.exportFile = function exportFile() {
    var data = vm.constructionTaskDailyGrids.dataSource.data();
    CommonService.exportFile(vm.constructionTaskDailyGrids, data, vm.listRemove, vm.listConvert, "Danh sách tra cứu kế hoạch năm");
}
//HuyPQ-25/08/2018-edit-start
vm.exportConstructionTaskDaily= function(){
	function displayLoading(target) {
	      var element = $(target);
	      kendo.ui.progress(element, true);
	      setTimeout(function(){
	    	  if (vm.disableBtnExcel)
	    			return;
	    		vm.disableBtnExcel = true;
	    	    return Restangular.all("constructionTaskDailyService/exportConstructionTaskDaily").post(vm.constructionTaskDailySearch).then(function (d) {
	    	        var data = d.plain();
	    	        window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
	    	        kendo.ui.progress(element, false);
	    	        vm.disableBtnExcel = false;
	    	    }).catch(function (e) {
	    	    	kendo.ui.progress(element, false);
	    	        toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
	    	        return;
	    	    }).finally(function() {
	    	    	kendo.ui.progress(element, false);
	    	    	vm.disableBtnExcel = false;
	    		});
	      });
	
	}
		displayLoading("#constructionTaskDailyGrid");
}
// HuyPQ-end
vm.import = function () {
    var teamplateUrl = "coms/workItem/workItemPopup.html";
    var title = "Import từ file excel";
    var windowId = "YEAR_PLAN";
    CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '700', 'auto', "files");
}


function refreshGrid(d) {
    var grid = vm.constructionTaskDailyGrids;
    if (grid) {
        grid.dataSource.data(d);
        grid.refresh();
    }
}


vm.cancel = cancel;
function cancel() {
    vm.showDetail = false;
    vm.constructionTaskDailySearch = {}
    vm.workItem = {};
    vm.doSearch();
}

vm.approveQuantityDayChecked =function() {
	var listDaily = getConstructionTask();

    if (listDaily != null && listDaily.length > 0) {
        confirm('Bạn thật sự muốn xác nhận bản ghi đã chọn?', function () {
        	debugger;
            return Restangular.all("constructionTaskDailyService/approveQuantityDayChecked").post(listDaily).then(function (d) {
                if (d.error) {
                    toastr.error(d.error);
                    return;
                }
                toastr.success("Xác nhận thành công !")
                cancelConfirmPopup();
                $("#constructionTaskDailyGrid").data('kendoGrid').dataSource.read();
                $("#constructionTaskDailyGrid").data('kendoGrid').refresh();
                vm.chkAll = false;
                vm.constructionTaskDaily.approveDescription = "";

            }).catch(function (e) {
                toastr.error(gettextCatalog.getString("Lỗi khi hủy hoàn thành"));
                return;
            });
        });
    } else {
        toastr.warning("Chưa chọn !");
    }
}
function getConstructionTask(){
    var list=[];
    var data = vm.constructionTaskDailyGrids.dataSource._data;
    vm.constructionTaskDailyGrids.table.find("tr").each(function(idx, data) {
        var row = $(data);
        var checkbox = $('[name="gridcheckbox"]', row);
        if (checkbox.is(':checked')) {
            var tr = vm.constructionTaskDailyGrids.select().closest("tr");
            var dataItem = vm.constructionTaskDailyGrids.dataItem(data);
//            list.push(dataItem.workItemId);
            list.push(dataItem);
        }
    });
    return list;
}

vm.cancelApproveTaskDaily = function(dataItem){
        vm.constructionTaskDaily = angular.copy(dataItem);
        confirm('Hủy xác nhận bản ghi đã chọn?', function () {
        	console.log('confirm cancel hnx');
            constructionTaskDailyService.saveCancelConfirmPopup(vm.constructionTaskDaily).then(function (response) {
                toastr.success("Ghi lại thành công!");
                cancelConfirmPopup();
                $("#constructionTaskDailyGrid").data('kendoGrid').dataSource.read();
                $("#constructionTaskDailyGrid").data('kendoGrid').refresh();
//                doSearch();
            }, function (errResponse) {
                toastr.error("Lỗi không hủy xác nhận được!");
            });

        });
}

vm.editQuantityDaily=function(dataItem){
	vm.constructionTaskDaily = angular.copy(dataItem);
    constructionTaskDailyService.getListImage(dataItem).then(function(data){
    	if (data.listImage.length > 0) {
            vm.constructionTaskDaily.listImage= data.listImage;
            vm.changeImage(vm.constructionTaskDaily.listImage[0]);
    	} else {
    		vm.constructionTaskDaily.listImage=[];
    	}
    },function(error){
    	vm.constructionTaskDaily.listImage=[];
    });
    var templateUrl="coms/quantityByDayOS/popup-detail-cancel-confirm-os.html";
    var title="Thông tin chi tiết thi công ";
    var windowId="DETAIL_CANCEL_CONFIRM";
    vm.popUpOpen = 'DETAIL_QUANTITY_DAY';
    CommonService.populatePopupCreate(templateUrl,title,vm.constructionTaskDaily,vm,windowId,false,'1000','auto',"null");
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
    vm.constructionTaskDailySearch = {
        status: "1"
    };
    doSearch();
}

vm.doSearch = doSearch;
function doSearch() {
    vm.showDetail = false;
    var grid = vm.constructionTaskDailyGrids;
    if (grid) {
        grid.dataSource.query({
            page: 1,
            pageSize: 10
        });
    }
}


vm.showHideWorkItemColumn = function (column) {
    if (angular.isUndefined(column.hidden)) {
    	vm.constructionTaskDailyGrids.hideColumn(column);
    } else if (column.hidden) {
    	vm.constructionTaskDailyGrids.showColumn(column);
    } else {
    	vm.constructionTaskDailyGrids.hideColumn(column);
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
    constructionTaskDailyService.exportpdf(obj);
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
	console.log('hnx -- on save ???');
    if (vm.departmentpopUp === 'dept') {
        vm.constructionTaskDailySearch.sysGroupName = data.text;
        vm.constructionTaskDailySearch.sysGroupId = data.id;
    }
}

// clear data
vm.changeDataAuto = changeDataAuto
function changeDataAuto(id) {
    switch (id) {
        case 'dept':
        {
            if (processSearch(id, vm.selectedDept1)) {
                vm.constructionTaskDailySearch.sysGroupId = null;
                vm.constructionTaskDailySearch.sysGroupName = null;
                vm.selectedDept1 = false;
            }
            break;
        }
        case 'user':
        	{
        		if (processSearch(id, vm.selectedDept1)) {
    		    	vm.constructionTaskDailySearch.userName = null;
    	            vm.constructionTaskDailySearch.sysUserId = null;
    	            vm.selectedDept1 = false;
        		}
        	}
    }
}

vm.cancelInput = function (param) {
    if (param == 'dept') {
        vm.constructionTaskDailySearch.sysGroupId = null;
        vm.constructionTaskDailySearch.sysGroupName = null;
    }
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
        vm.constructionTaskDailySearch.sysGroupName = dataItem.text;
        vm.constructionTaskDailySearch.sysGroupId = dataItem.id;
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
                    name: vm.constructionTaskDailySearch.sysGroupName,
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
            vm.constructionTaskDailySearch.sysGroupName = null;// thành name
            vm.constructionTaskDailySearch.sysGroupId = null;
        }
    },
    ignoreCase: false
}
vm.getExcelTemplate = function () {
    constructionTaskDailyService.downloadTemplate({}).then(function (d) {
        var data = d.plain();
        window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
    }).catch(function () {
        toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        return;
    });
}

    vm.cancelImport = cancelImport
    function cancelImport() {
        CommonService.dismissPopup1();
    }
    vm.downloadFile = downloadFile;
     function downloadFile(path){
         window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + path;
     }
    vm.removeImage=removeImage;
    function removeImage(image,list){
        list.splice(list.indexOf(image), 1);
    }

    vm.patternSignerOptions={
				dataTextField: "fullName", 
				placeholder:"Nhập mã hoặc tên người thực hiện",
				open: function(e) {
					vm.isSelect = false;
					vm.selectedDept1 = false;
				},
	            select: function(e) {
	            	vm.isSelect = true;
	            	vm.selectedDept1 = true;
	            	data = this.dataItem(e.item.index());
	                vm.constructionTaskDailySearch.userName =data.fullName;
	                vm.constructionTaskDailySearch.sysUserId = data.sysUserId;
	            },
	            pageSize: 10,
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function(options) {
	                    	vm.selectedDept1 = false;
	                        return Restangular.all("sysUserCOMSRsService/getForAutoCompleteInSign").post({pageSize:10, page:1, fullName:$("#signerGroup").val().trim()}).then(function(response){
	                            options.success(response);
	                            if(response == [] || $("#signerGroup").val().trim() == ""){
	            	                vm.constructionTaskDailySearch.userName = null;
	            	                vm.constructionTaskDailySearch.sysUserId = null;
	                            }
	                        }).catch(function (err) {
	                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
	                        });
	                    }
	                }
	            },
	            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
	            '<p class="col-md-6 text-header-auto border-right-ccc">Mã nhân viên</p>' +
	            '<p class="col-md-6 text-header-auto">Họ tên</p>' +
	            	'</div>',
	            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.employeeCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
	          change: function(e) {
	        	  if (e.sender.value() === '') {
	                  vm.constructionTaskDailySearch.userName = null;
	                  vm.constructionTaskDailySearch.sysUserId = null;
	              }
	        	},
	            close: function(e) { 
	              }
			};

    vm.openUser = function openUser(){

		var templateUrl = 'coms/popup/sysUserSearchPopUp.html';
		var title = gettextCatalog.getString("Tìm kiếm nhân viên");
		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','sysUserSearchController');

    }
    vm.onSaveSysUser = function onSaveSysUser(data){
  			vm.constructionTaskDailySearch.userName = data.fullName;
            vm.constructionTaskDailySearch.sysUserId = data.sysUserId;
            console.log(vm.constructionTaskDailySearch);
            htmlCommonService.dismissPopup();
            $("#signerGroup").focus();
	    };
    vm.clearDate = function() {
    	vm.constructionTaskDailySearch.endDate = null;
        vm.constructionTaskDailySearch.startDate = null;
	}
    vm.clearUser = function() {
    	vm.constructionTaskDailySearch.userName = null;
        vm.constructionTaskDailySearch.sysUserId = null;
	}
    vm.clearStatus = function() {
    	vm.constructionTaskDailySearch.confirmLst = null;
	}
    vm.taskDailyGridOptions = kendoConfig.getGridOptions({
        autoBind: true,
        scrollable: false,
        resizable: true,
        editable:true,
        save : function(){
            vm.taskDailyGrid.refresh();
        },
        dataBinding: function () {
            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
        },
        reorderable: true,
        toolbar: [
                  {template:
                	  '<div class="btn-group pull-right margin_top_button ">' +
                	  '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: Triệu VNĐ</div>'
                	   +'</div>'
                	  }
        ],
        dataSource: {
            serverPaging: false,
            schema: {
                total: function (response) {
                    return 1;
//                    return vm.constructionTaskDaily.length;
                },
                data: function (response) {
                    return vm.constructionTaskDaily;
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
                title: "Số lượng",
                field: 'amount',
                width: '10%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                type :'number',
                format: "{0:n3}",
                editable:true
            },
            {
                title: "Sản lượng",
                field: 'quantity',
                width: '10%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                format: "{0:n0}",
                type :'number',
                editable:false,
                template: function(item) {
					if (vm.constructionTaskDaily.price == null) {
						return htmlCommonService.addThousandComma(!!item.quantity ? item.quantity.toString() : '');
					} else {
						var qt = vm.constructionTaskDaily.price * item.amount;
						return htmlCommonService.addThousandComma(qt.toString());
					}
				}
            }
]
});
    
    vm.saveApproveQuantity = function(obj) {
    	vm.approveQuantityOS(obj);
	}
    vm.rejectTaskDaily = function(dataItem){
            vm.constructionTaskDaily = angular.copy(dataItem);
            confirm('Từ chối xác nhận bản ghi đã chọn?', function () {
                constructionTaskDailyService.rejectTaskDaily(vm.constructionTaskDaily).then(function (response) {
                    toastr.success("Ghi lại thành công!");
                    cancelConfirmPopup();
                    $("#constructionTaskDailyGrid").data('kendoGrid').dataSource.read();
                    $("#constructionTaskDailyGrid").data('kendoGrid').refresh();
                }, function (errResponse) {
                    toastr.error("Lỗi không hủy xác nhận được!");
                });

            });
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
        vm.constructionTaskDailySearch.catProvinceId = data.catProvinceId;
        vm.constructionTaskDailySearch.catProvinceCode = data.code;
		vm.constructionTaskDailySearch.catProvinceName = data.name;
        htmlCommonService.dismissPopup();
        $("#provincename").focus();
    };
	function clearProvince (){
		vm.constructionTaskDailySearch.catProvinceId = null;
		vm.constructionTaskDailySearch.catProvinceCode = null;
		vm.constructionTaskDailySearch.catProvinceName = null;
		$("#provincename").focus();
	}
    vm.provinceOptions = {
        dataTextField: "name",
        dataValueField: "id",
		placeholder:"Nhập mã hoặc tên tỉnh",
        select: function (e) {
            vm.isSelect = true;
            var dataItem = this.dataItem(e.item.index());
            vm.constructionTaskDailySearch.catProvinceId = dataItem.catProvinceId;
            vm.constructionTaskDailySearch.catProvinceCode = dataItem.code;
			vm.constructionTaskDailySearch.catProvinceName = dataItem.name;
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
                        name: vm.constructionTaskDailySearch.catProvinceName,
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
                vm.constructionTaskDailySearch.catProvinceId = null;
                vm.constructionTaskDailySearch.catProvinceCode = null;
				vm.constructionTaskDailySearch.catProvinceName = null;
            }
        },
        close: function (e) {
            if (!vm.isSelect) {
                vm.constructionTaskDailySearch.catProvinceId = null;
                vm.constructionTaskDailySearch.catProvinceCode = null;
				vm.constructionTaskDailySearch.catProvinceName = null;
            }
        }
    }

//end controller
    }
})();
