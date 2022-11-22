(function() {
    'use strict';
    var controllerId = 'contructionDTController';

    angular.module('MetronicApp').controller(controllerId, contructionDTController);

    function contructionDTController($scope, $rootScope, $timeout, gettextCatalog,
                                       kendoConfig, $kWindow,$filter,contructionDTService,htmlCommonService,
                                       CommonService, PopupConst, Restangular, RestEndpoint,Constant) {

        var vm = this;
        vm.searchForm={};
        var record=0;
        $scope.listCheck = [];
        vm.String = "Quản lý công trình > Quản lý giá trị công trình > Quản lý doanh thu";
        vm.constrObj={};


        vm.monthListOptions={
            dataSource: {
                serverPaging: true,
                schema: {
                    data: function (response) {
                        return response.data; // data is returned in
                        // the "data" field of
                        // the response
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "constructionTaskService/doSearchForRevenue",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.d.page = 1;
                        vm.d.pageSize = 100;
                        return JSON.stringify(vm.d);

                    }
                }
            },
            dataValueField: "constructionTaskId",
            template: '<span>Tháng #:data.month#</span>'+'/'+'<span>#:data.year#</span>',
            tagTemplate: '<span>Tháng #:data.month#</span>'+'/'+'<span>#:data.year#</span>',
            valuePrimitive: true
        }


        vm.constructionTaskGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
			editable: true,
            save : function(){
                vm.constructionTaskGrid.refresh();
            },
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            reorderable: true,
            toolbar: [
                {
                    name: "actions",
                    template: '<div class=" pull-left ">' +
                    '<button style="padding: 4px 30px ;margin-right: 5px; padding-right: 17px !important;width: inherit;" id ="confirmpopupApp" class="btn btn-qlk padding-search-right  confirmpopupApp"' +
                        'ng-click="vm.approveRevenue()" translate>Phê duyệt</button>' +
                    '</div>'
                    +
                    '<button style="padding: 4px 30px ;padding-right: 17px !important;width: inherit;" id ="savepopupAppQuantity" class="btn btn-qlk padding-search-right savepopupAppQuantity"' +
                    'ng-click="vm.cancelComplete()" translate>Từ chối</button>' +
                    '</div>'
                    +
                    '<div class="btn-group pull-right margin_top_button ">' +
                    '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: VNĐ</div>'+
                    '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                    '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportDT()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
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
                        var list = response.data;
                        return response.data; // data is returned in
                        // the "data" field of
                        // the response
                    },
				 model: {
                    	fields: {
//                    		hoamm1_20190103_start
                    		stt: {editable: false},
							dateComplete: {editable: false},
							sysGroupName: {editable: false},
							catStationCode: {editable: false},
							constructionCode: {editable: false},
							cntContract: {editable: false},
							completeValue: {editable: false},
							status: {editable: false},
							consAppRevenueState: {editable: false},
                    		consAppRevenueValue: {editable: true},
//                    		hoamm1_20190103_end
                    	}
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "constructionTaskService/doSearchForRevenue",
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
            edit: function(e) {

                if(e.model.consAppRevenueState != "1"){

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
            columns: [
                {
                    title: "Chọn",
                    template: function(data){
                        if (data.consAppRevenueState==1 || data.consAppRevenueState==null) {
                            return "<input type='checkbox' id='childcheckDT' name='gridcheckbox' ng-click='vm.handleCheck(dataItem)'/>";
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
                }, {
                    title: "Ngày thực hiện",
                    field: 'dateComplete',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
//                    template:function(data){
//                        return monthWithCommas(data.dateComplete);
//                    },
                    type :'text',
                    editable:false
                }, {
                    title: "Đơn vị thực hiện",
                    field: 'sysGroupName',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    type :'text',
                    editable:false
                }, {
                    title: "Mã trạm",
                    field: 'catStationCode',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    type :'text',
                    editable:false
                },  {
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
                    title: "Hợp đồng đầu ra",
                    field: 'cntContract',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    type :'text',
                    editable:false
                }, 
                {
                    title: "Doanh thu kế hoạch",
                    field: 'completeValue',
                    width: '10%',
                    format: "{0:n3}",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    template : function(data){
                        return numberWithCommas(data.completeValue)
                    },
                    footerTemplate: function(item) {
                    	var data = vm.constructionTaskGrid.dataSource.data();
                    	var item, sum = 0;
                        for (var idx = 0; idx < data.length; idx++) {
                        	if (idx == 0) {
                        		item = data[idx];
                        		sum =numberWithCommas(item.completeValueTotal||0);
                        		break;
                        	}
                        }
                        return kendo.toString(sum, "");
					},
                    type :'text',
                    editable:false
                },
                {
                    title: "Doanh thu phê duyệt",
                    field: 'consAppRevenueValue',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    format: "{0:n3}",
                    type : 'number',
                    editable: true,
                    editable: function(data){
                        if (data.consAppRevenueState==1||data.consAppRevenueState==null) {
                            return true;
                        } else {
                            return false;
                        }
                    },
                    footerTemplate: function(item) {
                    	var data = vm.constructionTaskGrid.dataSource.data();
                    	var item, sum = 0;
                        for (var idx = 0; idx < data.length; idx++) {
                        	if (idx == 0) {
                        		item = data[idx];
                        		sum =numberWithCommas(item.consAppRevenueValueTotal||0);
                        		break;
                        	}
                        }
                        return kendo.toString(sum, "");
					},
                    type :'text',
                    editable:false
                },
                {
                    title: "Trạng thái",
                    field: 'status',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: "# if(status == 4){ #" + "#= 'Đã tạm dừng' #" + "# } "
                    + "else if (status == 5) { # " + "#= 'Đã hoàn thành' #" + "#}"
                    + "else if (status == 6) { # " + "#= 'Đã nghiệm thu' #" + "#}"
                    + "else if (status == 7) { # " + "#= 'Đã hoàn công' #" + "#}"
                    + "else if (status == 8) { # " + "#= 'Đã quyết toán' #" + "#}"+"#",
                    		
                   
                 
                    type :'text',
                    editable:false
                },
                {
                    title: "Tình trạng phê duyệt",
                    field: 'consAppRevenueState',
                    width: '12%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    type : 'text',
                    editable:false,
                    template: "# if(consAppRevenueState == 1 || consAppRevenueState == null ){ #" + "#= 'Chưa phê duyệt' #" + "# } " + "else if (consAppRevenueState == 2) { # " + "#= 'Đã phê duyệt' #" + "#}" + "else if (consAppRevenueState == 3) { # " + "#= 'Đã từ chối' #" + "#} #"
                },
                {
                    title: "Thao tác",
                    type :'text',
                    editable:false,
                    attributes: {
                        style: "text-align:center;"
                    },
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    template: dataItem =>
                '<div class="text-left">'
                + '<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.edit(dataItem)" class=" icon_table "' +
                '   uib-tooltip="Xem chi tiết" translate>' +
                '<i class="fa fa-list-alt" style="color:#e0d014"   aria-hidden="true"></i>' +
                '</button>'
                +'<button style=" border: none; background-color: white;" id=""' +
                'class=" icon_table" ng-click="vm.approveButtonDT(dataItem)" ng-show="dataItem.consAppRevenueState==1 || dataItem.consAppRevenueState==null"  uib-tooltip="Phê duyệt" translate' + '>' +
                '<i class="fa fa-check" style="color: #00FF00;" ng-show="dataItem.consAppRevenueState==1 || dataItem.consAppRevenueState==null "  aria-hidden="true"></i>' +
                '</button>'
                +'<button style=" border: none; background-color: white;" id=""' +
                'class=" icon_table" ng-click="vm.cancelDT(dataItem)" ng-show="dataItem.consAppRevenueState==1 || dataItem.consAppRevenueState==null"  uib-tooltip="Từ chối" translate' + '>' +
                '<i class="fa fa-times-circle" style="color: #FF0000;" ng-show="dataItem.consAppRevenueState==1 || dataItem.consAppRevenueState==null"  aria-hidden="true"></i>' +
                '</button>'
                +'<button style=" border: none; background-color: white;" id=""' +
                'class=" icon_table" ng-click="vm.callback(dataItem)" ng-show="dataItem.consAppRevenueState==2 || dataItem.consAppRevenueState==3" uib-tooltip="Undo" translate'  + '>' +
                '<i class="fa fa-reply" style="color: #00FF00;" ng-show="dataItem.consAppRevenueState==2||dataItem.consAppRevenueState==3"  aria-hidden="true"></i>' +
                '</button>'
                + '</div>',
                width: '14%'
    }
    ]
});


            vm.handleCheck = handleCheck;
            function handleCheck(dataItem) {
                if (dataItem.selected) {
                    $scope.listCheck.push(dataItem);
                } else {
                    return Restangular.all("constructionService/checkPermissions").post(dataItem).then(function (d) {

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
                    })
                }

            }

        function monthWithCommas(x){
        	if(x == null || x == undefined){
                return '';
                }
        	return x.substring(3);
        }
        function numberWithCommas(x) {
            if(x == null || x == undefined){
            return '0';
            }
            var parts =parseFloat(x).toFixed(2).toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
        }
// function numberWithCommas(x) {
// if(x == null || x == undefined){
// return '0';
// }
// var parts = parseFloat(x).toFixed(3);
// return parts;
// }

vm.cancelComplete = cancelComplete;
 function cancelComplete(obj){
    var listId = getConstructionTask();
    if (listId!=null && listId.length>0) {
            var teamplateUrl = "coms/contructionDT/cancelCompleteDTpopup.html";
            var title = "Thông tin hủy bỏ xác nhận";
            var windowId = "CONTRUCTION_DT";
            CommonService.populatePopupCreate(teamplateUrl, title, obj, vm, windowId, true, '600', '','provinceCode' );
    }else{
        toastr.warning("Chưa chọn công việc !")
    };

}

vm.cancelDT = cancelDT;
var contrsListDT = [];
function cancelDT(dataItem){
// contrsListDT.push(dataItem);
    return Restangular.all("constructionService/checkPermissionsCancel").post(dataItem).then(function (d) {
        if(d.error){
            toastr.error(d.error);
            return;
        }
    }).catch(function (e) {
        contructionDTService.setItem(dataItem);
        var teamplateUrl = "coms/contructionDT/cancelDTpopup.html";
        var title = "Thông tin hủy bỏ xác nhận";
        var windowId = "CONTRUCTION_DT";
        CommonService.populatePopupOnPopup(teamplateUrl, title, null, vm, windowId, true, '600', '', 'provinceCode');
    })
}
vm.cancelDT2 = cancelDT2;
var contrsListDT = [];
function cancelDT2(dataItem){
// contrsListDT.push(dataItem);
    return Restangular.all("constructionService/checkPermissionsCancel").post(dataItem).then(function (d) {
        if(d.error){
            toastr.error(d.error);
            return;
        }
    }).catch(function (e) {
        contructionDTService.setItem(dataItem);
        var teamplateUrl = "coms/contructionDT/cancelDT2popup.html";
        var title = "Thông tin hủy bỏ xác nhận";
        var windowId = "CONTRUCTION_DT";
        CommonService.populatePopupOnPopup(teamplateUrl, title, null, vm, windowId, true, '600', '', 'provinceCode');
    })
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
            list.push(dataItem);
        }
    });
    return list;
}

function sumHSHC(){
    var data = vm.constructionDTGrid.dataSource._data;
    var sumHSHC =0;
    for (var i=0;i< data.length;i++) {
        sumHSHC += data[i].quantity;
    };
    vm.sumHSHC = numberWithCommas(sumHSHC);
}

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

vm.approveDT =function(dataItem){
    return Restangular.all("constructionService/checkPermissionsApproved").post(dataItem).then(function (d) {
        if(d.error){
            toastr.error(d.error);
            return;
        }
    }).catch(function (e) {
        contructionDTService.setItem(dataItem);
        var obj = contructionDTService.getItem();
//        hoanm1_20180613_start
//        obj.consAppRevenueValue = dataItem.consAppRevenueValue * 1000000;
//        hoanm1_20180613_end
        obj.consAppRevenueValue = dataItem.consAppRevenueValue ;
        obj.listFileDT = vm.BGMBgrid.dataSource._data;
        return Restangular.all("constructionService" + "/updateDTItemApproved").post(obj).then(function (data) {
            if (data.error) {
                toastr.error(data.error);
                return;
            }
            vm.constructionTaskGrid.dataSource.page(1);
            toastr.success("Phê duyệt thành công!");
            $(".k-i-close").click();
            CommonService.dismissPopup1();

        }, function (error) {
            toastr.error("Có lỗi xảy ra");
        });
    })
//  var list = [];
//  dataItem.consAppRevenueValue = dataItem.consAppRevenueValue*1000000;
//  list.push(dataItem);
//    var obj = contructionDTService.getItem();
//    obj.listFileDT = vm.BGMBgrid.dataSource._data;
//    obj.listConsTask = list;   
//        Restangular.all("constructionTaskService/approveConstrRevenue").post(obj).then(function(data){
//            vm.constructionTaskGrid.dataSource.page(1);
//            toastr.success("Phê duyệt thành công!");
//            CommonService.dismissPopup1();
//        }).catch(function (e) {
//            toastr.error(gettextCatalog.getString("Lỗi khi phê duyệt"));
//            return;
//        });;

}
vm.approveButtonDT =function(dataItem){
    return Restangular.all("constructionService/checkPermissionsApproved").post(dataItem).then(function (d) {
        if(d.error){
            toastr.error(d.error);
            return;
        }
    }).catch(function (e) {
        contructionDTService.setItem(dataItem);
        var obj = contructionDTService.getItem();
//        hoanm1_20180613_start
//        obj.consAppRevenueValue = dataItem.consAppRevenueValue * 1000000;
//        hoanm1_20180613_end
        obj.consAppRevenueValue = dataItem.consAppRevenueValue;
        //obj.listFileDT = vm.BGMBgrid.dataSource._data;
        return Restangular.all("constructionService" + "/updateDTItemApproved").post(obj).then(function (data) {
            if (data.error) {
                toastr.error(data.error);
                return;
            }
            vm.constructionTaskGrid.dataSource.page(1);
            toastr.success("Phê duyệt thành công!");
            $(".k-i-close").click();
            CommonService.dismissPopup1();

        }, function (error) {
            toastr.error("Có lỗi xảy ra");
        });
    })
//  var list = [];
//  dataItem.consAppRevenueValue = dataItem.consAppRevenueValue*1000000;
//  list.push(dataItem);
//    var obj = contructionDTService.getItem();
//    obj.listFileDT = vm.BGMBgrid.dataSource._data;
//    obj.listConsTask = list;
//        Restangular.all("constructionTaskService/approveConstrRevenue").post(obj).then(function(data){
//            vm.constructionTaskGrid.dataSource.page(1);
//            toastr.success("Phê duyệt thành công!");
//            CommonService.dismissPopup1();
//        }).catch(function (e) {
//            toastr.error(gettextCatalog.getString("Lỗi khi phê duyệt"));
//            return;
//        });;

}

vm.approveRevenue =function(){
    var list = getConstructionTask();
    var obj ={};
    obj.listConsTask = list;
    if (list!=null && list.length>0) {
        Restangular.all("constructionTaskService/approveConstrRevenue").post(obj).then(function(data){
            if(data.error){
                toastr.error(data.error);
                return;
            }
            vm.constructionTaskGrid.dataSource.page(1);
            toastr.success("Phê duyệt thành công!");

        }).catch(function (e) {
            toastr.error(gettextCatalog.getString("Lỗi khi phê duyệt"));
            return;
        });;
    }else{
        toastr.warning("Chưa chọn công việc !")
    };
}

vm.callback =function(dataItem){
    return Restangular.all("constructionService/checkPermissionsUndo").post(dataItem).then(function (d) {
        if(d.error){
            toastr.error(d.error);
            return;
        }
    }).catch(function (e) {
        contructionDTService.setItem(dataItem);
        var obj = contructionDTService.getItem();
        confirm('Bạn có muốn quay lại chưa phê duyệt không?', function () {
            Restangular.all("constructionTaskService/callbackConstrRevenue").post(obj).then(function (data) {
                if (data.error) {
                    toastr.error(data.error);
                    return;
                }
                vm.constructionTaskGrid.dataSource.page(1);
                toastr.success("Hoàn tác thành công!");
                CommonService.dismissPopup1();
            }).catch(function (e) {
                toastr.error(gettextCatalog.getString("Lỗi khi phê duyệt"));
                return;
            });
            ;
        });
    })
}

vm.rejectionDT =function(){

    contrsListDT.approveRevenueDescription=vm.constrObj.approveRevenueDescription;

    var obj ={};
    obj.listConsTask = contrsListDT;
    var obj = contructionDTService.getItem();
    if(vm.popupId=='EDIT'){
    //var obj = contructionDTService.getItem();
    obj.listFileDT = vm.BGMBgrid.dataSource._data}
    return Restangular.all("constructionService"+"/updateDTItem").post(obj).then(function(data){
        if(data.error){
            toastr.error(data.error);
            return;
        }
        vm.constructionTaskGrid.dataSource.page(1);
        toastr.success("Từ chối phê duyệt thành công!");
        $(".k-i-close").click();
        CommonService.dismissPopup1();

    },function(error){
        toastr.error("Có lỗi xảy ra");
    });
        //
        // Restangular.all("constructionTaskService/rejectionConstrRevenue").post(obj).then(function(data){
        //
        // vm.constructionTaskGrid.dataSource.page(1);
        // CommonService.dismissPopup1();
        // toastr.success("Từ chối phê duyệt thành công!");
        //
        // }).catch(function (e) {
        // toastr.error(gettextCatalog.getString("Lỗi"));
        // return;
        // });
}

vm.rejectionRevenue =function(){
    var list = getConstructionTask();
    for (var i in list) {
        list[i].approveRevenueDescription=vm.constrObj.approveRevenueDescription;
    };
    var obj ={};
    obj.listConsTask = list;
    if (list!=null && list.length>0) {
        Restangular.all("constructionTaskService/rejectionConstrRevenue").post(obj).then(function(data){
            if(data.error){
                toastr.error(data.error);
                return;
            }
            vm.constructionTaskGrid.dataSource.page(1);
            CommonService.dismissPopup1();
            toastr.success("Từ chối phê duyệt thành công!");

        }).catch(function (e) {
            toastr.error(gettextCatalog.getString("Lỗi"));
            return;
        });
    }else{
        toastr.warning("Chưa chọn công việc !")
    };
}
vm.rejectionRevenueItem=function(){
	 var obj ={};
	obj.listConsTask=contructionDTService.getItem();
	obj.listConsTask.approveRevenueDescription=vm.constrObj.approveRevenueDescription;
	 Restangular.all("constructionTaskService/rejectionConstrRevenue").post(obj).then(function(data){
         if(data.error){
             toastr.error(data.error);
             return;
         }
         vm.constructionTaskGrid.dataSource.page(1);
         CommonService.dismissPopup1();
         toastr.success("Từ chối phê duyệt thành công!");

     }).catch(function (e) {
         toastr.error(gettextCatalog.getString("Lỗi"));
         return;
     });
}



vm.gridColumnShowHideFilter = function (item) {

    return item.type == null || item.type !== 1;
};




function refreshGrid(d) {
    var grid = vm.constructionTaskGrid;
    if (grid) {
        grid.dataSource.data(d);
        grid.refresh();
    }
}

function formatDate(date) {
    var newdate = new Date(date);
    return kendo.toString(newdate, "dd/MM/yyyy");
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
    console.log(vm.searchForm.keySearch);
    console.log(vm.searchForm.monthYear);
    vm.constructionTaskGrid.dataSource.page(1);
}

//HuyPQ-25/08/2018-edit-start
vm.exportDT= function(){
	function displayLoading(target) {
	      var element = $(target);
	      kendo.ui.progress(element, true);
	      setTimeout(function(){
	    	  
	    	  return Restangular.all("constructionTaskService/exportContructionDT").post(vm.searchForm).then(function (d) {
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
//HuyPQ-end
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


vm.constructionDTGridOptions= kendoConfig.getGridOptions({
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
            width: '15%',
            headerAttributes: {style: "text-align:center;font-weight: bold;"},
            attributes: {
                style: "text-align:left;"
            },template:function(dataItem){
            	if(dataItem.status==1){
            		return 'Chưa thực hiện'
            	}else if(dataItem.status==2){
            		return 'Đang thực hiện'
            	}else if(dataItem.status==3){
            		return 'Đã hoàn thành'
            	}
            
        }
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

vm.cancelPopup=cancelPopup;
function cancelPopup(){
    CommonService.dismissPopup1();
}

vm.updateBGMBItem=updateBGMBItem;
function updateBGMBItem(){

    var obj = contructionDTService.getItem();
    obj.listFileDT = vm.BGMBgrid.dataSource._data
    return Restangular.all("constructionService"+"/updateDTItem").post(obj).then(function(data){
        if(data.error){
            toastr.error(data.error);
            return;
        }
        vm.constructionTaskGrid.dataSource.page(1);
        toastr.success("Ghi lại thành công!");
        CommonService.dismissPopup1();
    },function(error){
        toastr.error("Có lỗi xảy ra");
    });
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

$scope.$on("Popup.open", function () {
    if(vm.popupId=='EDIT') {
        vm.constrObj.typeHSHC = 2;
        Restangular.all('constructionService' + "/construction/getConstructionHSHCById").post(vm.constrObj).then(function (data) {
            vm.constrObjDetail = {};
            vm.constrObjDetail = data;
            var grid = vm.BGMBgrid;
            if (grid) {

                grid.dataSource.data(vm.constrObjDetail.listFileDT);
                grid.refresh();
            }
            grid = vm.constructionDTGrid;
            if (grid) {
                grid.dataSource.data(vm.constrObjDetail.listWorkItem);
                grid.refresh();
            }
            sumHSHC();
        }, function (err) {
            toastr.error("Có lỗi xảy ra!")
        });
    }
});

vm.edit = edit;
vm.consAppRevenueStatetoString='';
function edit(dataItem) {
    contructionDTService.setItem(dataItem);

    // //vm.constrObj=data;
    // console.log(obj);
    // console.log(data);
    // vm.obj1={};
    if(dataItem.consAppRevenueState == 1){vm.consAppRevenueStatetoString = 'Chưa phê duyệt'};
    if(dataItem.consAppRevenueState == 2){vm.consAppRevenueStatetoString = 'Đã phê duyệt'};
    if(dataItem.consAppRevenueState == 3){vm.consAppRevenueStatetoString = 'Đã từ chối'};
    vm.constrObj=dataItem;
    // vm.obj1= obj;
    var teamplateUrl = "coms/contructionDT/contructionDTpopup.html";
    var title = "Thông tin chi tiết doanh thu";
    var windowId = "CONTRUCTION_DT";
    vm.popupId='EDIT';
    CommonService.populatePopupCreate(teamplateUrl, title, dataItem, vm, windowId, true, '1000', '700','provinceCode' );


}


vm.selectedDept1=false;

init();

function init(){
	vm.searchForm.monthYear = new Date();
    $scope.monthSelectorOptions = {
        start: "year",
        depth: "year"
    };

}


vm.cancelInput = function(id){
    if(id=='month') {
        vm.searchForm.monthYear = ''
    }
    if(id=='dept') {
        vm.searchForm.sysGroupName = ''
        vm.searchForm.sysGroupId = undefined;
    }
    if(id=='status') {
        vm.searchForm.listAppRevenueState =[]
    }
}

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
