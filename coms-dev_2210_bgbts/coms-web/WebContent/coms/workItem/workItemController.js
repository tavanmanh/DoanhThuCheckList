(function () {
    'use strict';
    var controllerId = 'workItemController';

    angular.module('MetronicApp').controller(controllerId, workItemController);

    function workItemController($scope, $rootScope, $timeout, gettextCatalog,
                                kendoConfig, $kWindow, workItemService,
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
        vm.String = "Quản lý công trình > Quản lý giá trị công trình > Quản lý sản lượng";
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
        vm.d={};
        vm.monthListOptions={
            dataSource: {
                serverPaging: true,
                schema: {
                    data: function (response) {
                        return response.data;
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "workItemService/doSearchCompleteDate",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.d.page = 1;
                        //vm.d.signStateList = ['3'];
                        vm.d.pageSize = 100;
                        return JSON.stringify(vm.d);

                    }
                }
            },
            dataValueField: "dateComplete",
            template: '<span>Tháng #:data.monthComplete#</span>'+'/'+'<span>#:data.yearComplete#</span>',
            tagTemplate: '<span>Tháng #:data.monthComplete#</span>'+'/'+'<span>#:data.yearComplete#</span>',
            valuePrimitive: true
        }
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
                        template: '<div class=" pull-left ">' +

                        '<button   id="cancelAppQuantity" class="btn btn-qlk padding-search-right  cancel_confirm_quantity"' +
                        'ng-click="vm.removeFillterWorkItem()" uib-tooltip="Hủy xác nhận" translate>Hủy xác nhận</button>' +

                        '</div>'
                        +
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
                            url: Constant.BASE_SERVICE_URL + "workItemService/doSearchQuantity",
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

                        title : "<input  type='checkbox'  id='checkalllistdraw' name='gridchkselectall' ng-click='vm.chkSelectAll();' ng-model='vm.chkAll' />",
                        //template: "<input type='checkbox' id='childcheck' name='gridcheckbox' ng-click='vm.handleCheck(dataItem)'/>",
                        template: function(data){
                            if (data.status==3 && data.quantityByDate == 0
                            		&& data.statusConstruction != 6 && data.statusConstruction != 7 && data.statusConstruction != 8) {
                                return "<input type='checkbox' data-order = '"+record+"' id='childcheck' name='gridcheckbox' ng-click='vm.handleCheck(dataItem)'/>";
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
                        width: '9%',
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
                        width: '20%',
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
                    {
                        title: "Thao tác",
                        type :'text',
                        editable:false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        template: function(dataItem) {
//                        	hungnx 20180628 start
                        	var html =  '<div class="text-center">'
                                + '<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.editWorkItem(dataItem)" class=" icon_table "' +
                                '   uib-tooltip="Xem chi tiết" translate>' +
                                '<i class="fa fa-list-alt" style="color:#e0d014"     aria-hidden="true"></i>' +
                                '</button>'
							if (!!dataItem.quantityByDate && dataItem.quantityByDate == 1) {
								html += '';
							} else {
								html += '<button style=" border: none; background-color: white;" id=""' +
			                    'class=" icon_table" ng-click="vm.removeWorkItem(dataItem)" ng-show="dataItem.status==3"  uib-tooltip="Hủy xác nhận" translate' + '>' +
			                    '<i class="fa fa-reply" style="color: rgb(113, 219, 66);" ng-show="dataItem.status==3"  aria-hidden="true"></i>' +

			                    '</button>'

			                    +'<button style=" border: none; background-color: white;" id=""' +
			                    'class=" icon_table" ng-click="vm.approveQuantity(dataItem)" ng-show="dataItem.status==2"  uib-tooltip="Xác nhận" translate' + '>' +
			                    '<i class="fa fa-check" style="color: #00FF00;" ng-show="dataItem.status==2"  aria-hidden="true"></i>' +
			                    '</button>'

			                    +'<button style=" border: none; background-color: white;" id=""' +
			                    'class=" icon_table" ng-click="vm.approveQuantity(dataItem)" ng-show="dataItem.status==3"  uib-tooltip="Xác nhận" translate' + '>' +
			                    '<i class="fa fa-check" style="color: #00FF00;" ng-show="dataItem.status==3"  aria-hidden="true"></i>' +
			                    '</button>'
			                   
							}
                        	html  += '</div>';
                        	return html;
//                        	hungnx 20180628 end
						},
                    
						width: '12%',field:    "action",
                    },
    ]
    });
}
        
        

	function quantityEdited(dataItem) {
		dataItem.isQuantityEdited = 1;
	}
        
vm.approveQuantity =function(dataItem){
    var list = [];
    return Restangular.all("workItemService/checkPermissionsApproved").post(dataItem).then(function (d) {
        if(d.error){
            toastr.error(d.error);
            return;
        }
    }).catch(function (e) {
        if (dataItem.quantity == null) {
            toastr.error(gettextCatalog.getString("Giá trị sản lượng không được để trống"));
        } else {

            dataItem.quantity = dataItem.quantity * 1000000;
            //list.push(dataItem);
            //var obj = {};
            //obj.listConsTask = list;
            confirm('Xác nhận bản ghi đã chọn?', function () {

                Restangular.all("workItemService/approveWorkItem").post(dataItem).then(function (data) {
                    if (data.error) {
                        toastr.error(data.error);
                        return;
                    }
                    vm.workItemGrid.dataSource.page(1);
                    toastr.success("Phê duyệt thành công!");
                    $(".k-icon.k-i-close").click();


                }).catch(function (e) {
                    toastr.error(gettextCatalog.getString("Lỗi khi phê duyệt"));
                    return;
                });
            });
        }
    });
}

vm.approveCompleteQuantity =function(dataItem){
    var list = [];
    return Restangular.all("workItemService/checkPermissionsApproved").post(dataItem).then(function (d) {
        if(d.error){
            toastr.error(d.error);
            return;
        }
    }).catch(function (e) {
        if (dataItem.quantity == null) {
            toastr.error(gettextCatalog.getString("Giá trị sản lượng không được để trống"));
        } else {

            dataItem.quantity = dataItem.quantity * 1000000;
            //list.push(dataItem);
            //var obj = {};
            //obj.listConsTask = list;
            confirm('Xác nhận bản ghi đã chọn?', function () {

                Restangular.all("workItemService/approveCompleteWorkItem").post(dataItem).then(function (data) {
                    if (data.error) {
                        toastr.error(data.error);
                        return;
                    }
                    vm.workItemGrid.dataSource.page(1);
                     $(".k-icon.k-i-close").click();
                    toastr.success("Phê duyệt thành công!");

                }).catch(function (e) {
                    toastr.error(gettextCatalog.getString("Lỗi khi phê duyệt"));
                    return;
                });
            });
        }
    });
}

vm.handleCheck = handleCheck;
function handleCheck(dataItem) {
        if (dataItem.selected) {
             $scope.listCheck.push(dataItem);
        } else {
            return Restangular.all("workItemService/checkPermissionsCancelConfirm").post(dataItem).then(function (d) {
                if(d.error){
                    $('[name="gridcheckbox"]').addClass("checked");
                    $('.checked').prop('checked', false);
                    toastr.error(d.error);
                }
            }).catch(function (e) {
                for (var i = 0; i < $scope.listCheck.length; i++) {
                    if ($scope.listCheck[i].workItemId === dataItem.workItemId) {
                        $scope.listCheck.splice(i, 1);
                    }
                }
                $('[name="gridchkselectall"]').prop('checked', false);
            })
        }

}

vm.chkSelectAll = chkSelectAll;
$scope.checkSearch = false;
function chkSelectAll() {

    var grid = vm.workItemGrid;
    chkSelectAllBase(vm.chkAll, grid);
    $.each(grid._data, function(key,value){
        return Restangular.all("workItemService/checkPermissionsCancelConfirm").post(value).then(function (d) {
        if( !!d && d.error){
            $('[data-order = "'+key+'"]').addClass("checked");
            $('.checked').prop('checked', false);
        }else {
            $('[data-order = "'+key+'"]').addClass("check");
            $('.check').prop('checked', true);
        }
    }).catch(function (e) {
    if (vm.chkAll) {
            if ($scope.checkSearch && $scope.dataSearch.length > 0) {
                $scope.listCheck = $scope.dataSearch;
            } else {

                CommonService.getallData("workItemRsService/doSearch", vm.workItemSearch).then(function (data) {
                    $scope.listCheck = data.plain().data;
                })
            }

    } else {
        $scope.listCheck = [];
        //$('[name="gridcheckbox"]').prop('checked', false);
        //$('#childcheck').removeClass('check');
    }
    })
    })

}

vm.exportFile = function exportFile() {
    var data = vm.workItemGrid.dataSource.data();
    CommonService.exportFile(vm.workItemGrid, data, vm.listRemove, vm.listConvert, "Danh sách tra cứu kế hoạch năm");
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
        	return Restangular.all("workItemService/exportWorkItemServiceTask").post(vm.workItemSearch).then(function (d) {
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

vm.removeFillterWorkItem =function() {
    return Restangular.all("workItemService/checkPermissionsCancelConfirm").post().then(function (d) {
        if(d.error){
            toastr.error(d.error);
            return;
        }
    }).catch(function (e) {
        var listId = getConstructionTask();
        if (listId != null && listId.length > 0) {
            var templateUrl = "coms/workItem/pupup-cancel-confirm.html";
            var title = "Thông tin hủy bỏ xác nhận";
            var windowId = "CANCEL_CONFIRM_FILLTER";
            vm.popUpOpen = 'CANCEL_CONFIRM_FILLTER';
            CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, false, '550', 'auto', "null");
        } else {
            toastr.warning("Chưa chọn công việc !")
        }
        ;
    });
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
vm.removedObj;
vm.removeWorkItem = function(dataItem){
    //vm.showDetail=true;
    return Restangular.all("workItemService/checkPermissionsCancelConfirm").post(dataItem).then(function (d) {
        if(d.error){
            toastr.error(d.error);
            return;
        }
    }).catch(function (e) {
//    	hungnx 20180709 start
    	var search = vm.workItemSearch.keySearch;
//    	hungnx 20180709 end
    	vm.removedObj = dataItem;
    	vm.removedObj.keySearch = search;
//        vm.workItemSearch = dataItem;
//        vm.workItemSearch.keySearch = search;
        if (vm.workItemSearch.statusConstruction == 7) {
            toastr.error("Công trình đã hoàn công, không được hủy xác nhận");
        } else if (vm.workItemSearch.statusConstruction == 6) {
            toastr.error("Công trình đã nghiệm thu, không được hủy xác nhận");
        } else if (vm.workItemSearch.statusConstruction == 8) {
            toastr.error("Công trình đã quyết toán, không được hủy xác nhận");
        } else {
            var templateUrl = "coms/workItem/pupup-cancel-confirm.html";
            var title = "Thông tin hủy bỏ xác nhận";
            var windowId = "CANCEL_CONFIRM";
            vm.popUpOpen = 'CANCEL_CONFIRM';
            CommonService.populatePopupCreate(templateUrl, title, vm.removedObj, vm, windowId, false, '550', 'auto', "null");
        }
    });
}
vm.disableDetailPopup = true;
vm.editWorkItem=function(dataItem){
    vm.disableDetailPopup = true;
    if((dataItem.statusConstruction=="4" && dataItem.obstructedState=="2" ) || dataItem.statusConstruction=="5"){
        vm.disableDetailPopup = false;
    }

//	hungnx 20180709 start
	var search = vm.workItemSearch.keySearch;
	var dateTo = vm.workItemSearch.dateTo;
	var dateFrom = vm.workItemSearch.dateFrom;
	var sysGroupName = vm.workItemSearch.sysGroupName;
	var sysGroupId = vm.workItemSearch.sysGroupId;
//	hungnx 20180709 end

    vm.workItemSearch = dataItem;
    vm.workItemSearch.abc=dataItem.status;
    vm.workItemSearch.keySearch= search;
    vm.workItemSearch.dateTo = dateTo;
    vm.workItemSearch.dateFrom = dateFrom;
	vm.workItemSearch.sysGroupName = sysGroupName;
	vm.workItemSearch.sysGroupId = sysGroupId;
    workItemService.getListImageById({tableId: dataItem.workItemId, tableName: 'CONSTRUCTION_TASK'}).then(function(data){
    	if (data.listImage.length > 0) {
            vm.workItemSearch.listImage= data.listImage;
            vm.changeImage(vm.workItemSearch.listImage[0]);
    	} else {
    		vm.workItemSearch.listImage=[];
    	}
    },function(error){
        vm.workItemSearch.listImage=[];
    })

    var templateUrl="coms/workItem/popup-detail-cancel-confirm.html";
    var title="Thông tin chi tiết thi công ";
    var windowId="DETAIL_CANCEL_CONFIRM";
    vm.popUpOpen = 'DETAIL_CANCEL_CONFIRM';
    CommonService.populatePopupCreate(templateUrl,title,vm.workItemSearch,vm,windowId,false,'1000','auto',"null");
}
function numberWithCommas(x) {
    if(x == null || x == undefined){
    return '0';
    }
    var parts = x.toFixed(2).toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
}
$scope.$on("Popup.open", function () {
    if (vm.popUpOpen == 'DETAIL_CANCEL_CONFIRM') {
        vm.workItemSearchDetail={};

        workItemService.getWorkItemById(vm.workItemSearch).then(function (data) {
            vm.workItemSearchDetail = data;
            vm.workItemSearchDetail.quantity=numberWithCommas(data.quantity);
            //phuc_09/07
            vm.workItemSearch.quantity=numberWithCommas(data.quantity);
            //phuc_end
//            hungnx 20180704 start
            if (vm.workItemSearch.quantityByDate == 1) {
            	if (vm.workItemSearch.dateComplete != null) {
            		var arrDate = vm.workItemSearch.dateComplete.split('/');
            		vm.workItemSearchDetail.startingDate = new Date(arrDate[1], arrDate[0] - 1);
            		vm.workItemSearchDetail.completeDate = new Date(arrDate[1], arrDate[0], 0);
            	}
            }
//          hungnx 20180704 end

        }, function (error) {
            toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
        });
        vm.saveCancelConfirmPopup = function(dataItem){
            return Restangular.all("workItemService/checkPermissionsCancelConfirm").post(dataItem).then(function (d) {
                if(d.error){
                    toastr.error(d.error);
                    return;
                }
            }).catch(function (e) {
                vm.workItemSearch = dataItem;
                if (vm.workItemSearch.statusConstruction == 7) {
                    cancelConfirmPopup();
                    toastr.error("Công trình đã hoàn công, không được hủy xác nhận");
                } else if (vm.workItemSearch.statusConstruction == 6) {
                    cancelConfirmPopup();
                    toastr.error("Công trình đã nghiệm thu, không được hủy xác nhận");
                }
                else if (vm.workItemSearch.statusConstruction == 8) {
                    cancelConfirmPopup();
                    toastr.error("Công trình đã quyết toán, không được hủy xác nhận");
                }else {
                    var templateUrl = "coms/workItem/pupup-cancel-confirm.html";
                    cancelConfirmPopup();
                    var title = "Thông tin hủy bỏ xác nhận";
                    var windowId = "CANCEL_CONFIRM_DETAIL";
                    vm.popUpOpen = 'CANCEL_CONFIRM_DETAIL';
                    CommonService.populatePopupCreate(templateUrl, title, vm.workItemSearch, vm, windowId, false, '550', 'auto', "null");
                }
            })
        }

    }else if(vm.popUpOpen == 'CANCEL_CONFIRM' ||vm.popUpOpen == 'CANCEL_CONFIRM_DETAIL'  ){
        vm.saveCancelConfirmPopup = function(){
            //vm.detailMonthPlan.listHSHC=getDataGrid("#sourceGrid");
            return Restangular.all("workItemService/checkPermissionsCancelConfirm").post().then(function (d) {
                if(d.error){
                    toastr.error(d.error);
                    return;
                }
            }).catch(function (e) {
                if (vm.removedObj.approveDescription == undefined || vm.removedObj.approveDescription == '') {
                    toastr.warning("Lý do hủy xác nhận không được để trống")
                    return;
                }
                if (vm.removedObj.statusConstruction == 7) {
                    toastr.error("Công trình đã hoàn công, không được hủy xác nhận");
                } else if (vm.removedObj.statusConstruction == 6) {
                    toastr.error("Công trình đã nghiệm thu, không được hủy xác nhận");
                }
                else if (vm.removedObj.statusConstruction == 8) {
                    toastr.error("Công trình đã quyết toán, không được hủy xác nhận");
                }else {
                    confirm('Hủy xác nhận bản ghi đã chọn?', function () {

                        workItemService.saveCancelConfirmPopup(vm.removedObj).then(function (response) {
                            toastr.success("Ghi lại thành công!");
                            cancelConfirmPopup();
                            $("#workItemGrid").data('kendoGrid').dataSource.read();
                            $("#workItemGrid").data('kendoGrid').refresh();
                            doSearch();
                        }, function (errResponse) {
                            toastr.error("Lỗi không hủy xác nhận được!");
                        });

                    });
                }
            })
        }

    }else if(vm.popUpOpen == 'CANCEL_CONFIRM_FILLTER'){
        vm.saveCancelConfirmPopup = function() {
            return Restangular.all("workItemService/checkPermissionsApproved").post().then(function (d) {
                if(d.error){
                    toastr.error(d.error);
                    return;
                }
            }).catch(function (e) {
                if (vm.removedObj.approveDescription == undefined || vm.removedObj.approveDescription == '') {
                    toastr.warning("Lý do hủy xác nhận không được để trống")
                    return;
                }
                var listId = getConstructionTask();
                var obj = {};

                if (listId != null && listId.length > 0) {
                    confirm('Bạn thật sự muốn hủy xác nhận bản ghi đã chọn?', function () {
                        obj.approveDescription = vm.removedObj.approveDescription;
						obj.workItemDetailList = listId;
                        return Restangular.all("workItemService/removeFillterWorkItem").post(obj).then(function (d) {
                            if (d.error) {
                                toastr.error(d.error);
                                return;
                            }
                            toastr.success("Hủy hoàn thành thành công !")
                            cancelConfirmPopup();
                            $("#workItemGrid").data('kendoGrid').dataSource.read();
                            $("#workItemGrid").data('kendoGrid').refresh();

                            vm.doSearch();
                            vm.removedObj.approveDescription = "";

                        }).catch(function (e) {
                            toastr.error(gettextCatalog.getString("Lỗi khi hủy hoàn thành"));
                            return;
                        });
                    });
                } else {
                    toastr.warning("Chưa chọn công việc !");
                }
            })
        }
    }


});




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
