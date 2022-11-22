(function() {
    'use strict';
    var controllerId = 'contructionQLOSController';

    angular.module('MetronicApp').controller(controllerId, contructionQLOSController);
    function contructionQLOSController($scope, $rootScope, $timeout, gettextCatalog,contructionQLOSService,
                                          kendoConfig, $kWindow,$filter,htmlCommonService,
                                          CommonService, PopupConst, Restangular, RestEndpoint,Constant) {

        var vm = this;
        vm.searchForm={};
        vm.d={};
        var record=0;
        $scope.listCheck = [];
        vm.String = "Quản lý công trình > Quản lý giá trị công trình > Quản lý quỹ lương ngoài OS";
        vm.constrObj={};
        vm.constrTaskObj={};
//	hungtd_20181210_start
        vm.saveDataItem =[];
//	hungtd_20181210_end
        vm.obj={};
        init();
        function init(){
        	vm.searchForm.monthYear = new Date();
            $scope.monthSelectorOptions = {
                start: "year",
                depth: "year"
            };
        }
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
                        url: Constant.BASE_SERVICE_URL + "constructionTaskService/doSearchForConsManager",
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

        vm.constructionQLOSGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: true,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },	
            reorderable: true,
            toolbar: [
                {
                    name: "actions",
                    template: '<div class=" pull-left ">' +
                    '<button   id="cancelAppQuantity" class="btn btn-qlk padding-search-right  cancel_confirm_quantity"' +
                    'ng-click="vm.removeComplete()" uib-tooltip="Hủy hoàn thành" translate>Hủy hoàn thành</button>' +
                    '</div>'
                    /*+
                    '<button class="btn btn-qlk padding-search-right TkQLK " ' +
                    'ng-click="vm.importConstructionHSHC()" uib-tooltip="Import Hồ sơ hoàn công" translate>Import</button>'*/

                    +
                    '<div class="btn-group pull-right margin_top_button ">' + 
                    '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: triệu VNĐ</div>'+
                    '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                    '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportexcelHSHC()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                    '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                    '<label ng-repeat="column in vm.constructionQLOSGrid.columns| filter: vm.gridColumnShowHideFilter">' +
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
//                    		hoamm1_20181215_start
                    		stt: {editable: false},
                    		dateComplete: {editable: false},
                    		sysGroupName: {editable: false},
                    		catStationCode: {editable: false},
                    		status: {editable: false},
                    		constructionCode: {editable: false},
                    		cntContract: {editable: false},
                    		catStationHouseCode: {editable: false},
                    		completeValuePlan: {editable: false},
                    		receiveRecordsDate: {editable: false},
                    		approceCompleteDescription: {editable: false},                    		
                    		completeValue: {editable: true},
//                    		hoamm1_20181215_end
                    	}
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "constructionTaskService/doSearchForConsManager",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        // vm.yearPlanSearch.employeeId =
                        // Constant.user.srvUser.catEmployeeId;
                        vm.searchForm.page = options.page
                        vm.searchForm.pageSize = options.pageSize
                        vm.searchForm.type = 1
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
                	
                    title: "Chọn",
                    template: "<input type='checkbox' id='childcheckHC' name='gridcheckbox' ng-click='vm.handleCheck(dataItem)' />",
                    width: '5%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes:{style:"text-align:center;"},

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
                        style: "text-align:center;",
                       
                    },
                  
                }, {
                    title: "Ngày thực hiện",
                    width: '9%',
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
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                }, 
//                hoanm1_20181203_start
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
                    width: '12%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                }, {
                    title: "Hợp đồng đầu ra",
                    field: 'cntContract',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                 {
                    title: "Quỹ lương kế hoạch",
                    width: '10%',
                     field: 'completeValuePlan',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                     format: "{0:n3}",
//                    template:function(dataItem){
//                        return numberWithCommas(dataItem.completeValuePlan||0);
//                    },
                    footerTemplate: function(item) {
                    	var data = vm.constructionQLOSGrid.dataSource.data();
                    	var item, sum = 0;
                        for (var idx = 0; idx < data.length; idx++) {
                        	if (idx == 0) {
                        		item = data[idx];
                        		sum = item.totalPlanQuantity;
//                        		sum =numberWithCommas(item.totalQuantity||0);
                        		break;
                        	}
                        }
                        return kendo.toString(sum, "");
					},
                },
                {
                    title: "Quỹ lương phê duyệt",
                    width: '10%',
                     field: 'completeValue',
//                     field: 'completeValue',
                     editable: true,
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    format: "{0:n3}",
                    type :'number',
//                    template:function(dataItem){
//                        return numberWithCommas(dataItem.completeValue||0);
//                    },
                    footerTemplate: function(item) {
                    	var data = vm.constructionQLOSGrid.dataSource.data();
                    	var item, sum = 0;
                        for (var idx = 0; idx < data.length; idx++) {
                        	if (idx == 0) {
                        		item = data[idx];
                        		sum = item.totalQuantity;
//                        		sum =numberWithCommas(item.totalQuantity||0);
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
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: function(dataItem){
                        if(dataItem.status==1){
                        	return "Chờ phê duyệt"
                        }
                        if(dataItem.status==2){
                        	return "Đã phê duyệt"
                        }
                        if(dataItem.status==3){
                        	return "Từ chối"
                        }
                    
//                        if(dataItem.status==7){
//                    	return "Đã hoàn công"
//                        }
//                        if(dataItem.status==8){
//                    	return "Đã quyết toán" 
//                        }else{
//                        	return "Đã hoàn thành"
//                        }
                    }
                 },
                 {		
                	 field: "receiveRecordsDate",
                	 title:"Ngày nhận",
//                	 template: dataItem=> "<input kendo-date-picker id='abc' ng-model='dataItem.receiveRecordsDate' ng-change='vm.updateDayHshc(dataItem)' style='width: 90px;' min-year='1000' date-time />",
                	 editor: function(container,options){
                		 var stringDate = "";
                		var input= $("<input ng-model='dataItem.receiveRecordsDate' ng-change='vm.approveHSHC(dataItem)' style='width: 90px;' />");
                		input.attr("receiveRecordsDate", options.field);
                		input.appendTo(container);
                		input.kendoDatePicker({
                			format:"dd/MM/yyyy",
                			
                		});
                	 },
                     
                	
                	 width: '7%',
                     headerAttributes: {style: "text-align:center;font-weight: bold;"},
                     attributes: {
                         style: "text-align:left;"
                     }
                    
                         
                         
                 },  {
                     title: "Lý do hủy",
//                     hoanm1_20181203_start
                     field: 'approceCompleteDescription',
//                     hoanm1_20181203_end
                     width: '10%',
                     headerAttributes: {style: "text-align:center;font-weight: bold;"},
                     attributes: {
                         style: "text-align:left;"
                     }
                 },
				{
                        title: "Loại tác động hoàn thành",
                        field: 'importCompleteHSHC',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false
                    },
                 
                 {
                     title: "Thao tác",
                     headerAttributes: {style: "text-align:center;font-weight: bold;"},
                     template: dataItem =>
                 '<div class="text-center">'
                 + '<button style=" border: none; background-color: white;" id="updateId" ng-hide="dataItem.status==0" ng-click="vm.edit(dataItem)" class=" icon_table "' +
                 '   uib-tooltip="Chi tiết" translate>' +
                 '<i class="fa fa-list-alt" style="color:#e0d014"  ng-hide="dataItem.status==0"   aria-hidden="true"></i>' +
                 '</button>'
                 +'<button style=" border: none; background-color: white;" id="removeId"' +
                 'class=" icon_table" ng-click="vm.removeHSHC(dataItem)" ng-hide="dataItem.status!=1"  uib-tooltip="Từ chối" translate' + '>' +
                 '<i class="fa fa-times-circle" style="color: #FF0000;" ng-hide="dataItem.status!=1"  aria-hidden="true"></i>' +
                 '</button>'
                 +'<button style=" border: none; background-color: white;" id="Acceptid"' +
                 'class=" icon_table" ng-click="vm.approveHSHC(dataItem)" ng-hide="dataItem.status==2"  uib-tooltip="Phê duyệt" translate' + '>' +
                 '<i class="fa fa-check" style="color: #00FF00;" ng-hide="dataItem.status!=1"  aria-hidden="true"></i>' +
                 '</button>'
                 +'<button style=" border: none; background-color: white;" id="UndoId"' +
                 'class=" icon_table" ng-click="vm.undoHSHC(dataItem)" ng-hide="dataItem.status==1"  uib-tooltip="Undo" translate' + '>' +
                 '<i class="fa fa-reply" style="color: #00FF00;" ng-hide="dataItem.status==1"  aria-hidden="true"></i>' +
                 '</button>'
                 + '</div>',
                 width: '15%'
     }
            ]
        });
        
        //tungtt20181128 _ start
      
        vm.approveHSHC =function(dataItem){
//            var list = [];
//         	
//              var valueDateComple = dataItem.dateComplete.toString();
//      	    var monthComple = Number( valueDateComple.split("/",2).pop());//Tháng N 
//  	        var yearComple = Number( valueDateComple.split("/").pop());
//       	    var d = new Date();  
       	    //debugger;
        	
       	 var list = [];
     	var dateNow = kendo.parseDate(new Date(),"dd/MM/yyyy");
     	dateNow.setHours(0);
     	dateNow.setMinutes(0);
     	dateNow.setSeconds(0);
     	dateNow.setMilliseconds(0);
     	 var dateNowCurr = kendo.parseDate(dateNow,"dd/MM/yyyy");
     	 var dateCheck = kendo.parseDate(dataItem.dateComplete,"dd/MM/yyyy");
     	 var monthDateNow = dateCheck.getMonth() +1;
     	 var yearDateNow = dateCheck.getFullYear();
     	 
     	 
     	 var currMonthNext = monthDateNow+1;
      	 var currMonthNextDate = '05' + '/' + currMonthNext +'/'+ yearDateNow;
      	 var dateNowNext = kendo.parseDate(currMonthNextDate,"dd/MM/yyyy");
      	 
      	var currMonthNext1 = '01';
     	var currYearNext = yearDateNow + 1;
     	var currMonthNextDate1 = '05' + '/' + currMonthNext1 +'/'+ currYearNext;
     	var dateNowNext1 = kendo.parseDate(currMonthNextDate1,"dd/MM/yyyy");
            return Restangular.all("constructionTaskService/checkPermissionsApprovedHSHC").post(dataItem).then(function (data) {
                if(data.error){
                    toastr.error(data.error);
                    return;
                }
            }).catch(function (e) {
        
            	if(monthDateNow<12 && dateNowCurr > dateNowNext){
            	
            		  toastr.error(gettextCatalog.getString("Đã quá thời gian phê duyệt HSHC"));
            	
            	  }else if(monthDateNow = 12 && dateNowCurr > dateNowNext1) {
            		  toastr.error(gettextCatalog.getString("Đã quá thời gian phê duyệt HSHC"));

            	     }else {
            		
	                    confirm('Xác nhận bản ghi đã chọn?', function () {
	                    	//debugger;
	                    	dataItem.sysUserId = Constant.userInfo.vpsUserToken.sysUserId;
	                        Restangular.all("constructionTaskService/approveHSHC").post(dataItem).then(function (data) {
	                        	debugger;
	                            if (data.error) {
	                                toastr.error(data.error);
	                                return;
	                            }
	                            vm.constructionQLOSGrid.dataSource.page(1);
	                            toastr.success("Phê duyệt thành công!");
	                            $(".k-icon.k-i-close").click();
	                            return;

                        }).catch(function (e) {
                            toastr.error(gettextCatalog.getString("Lỗi khi phê duyệt"));
                            return;
                        });
                    });
                }
            });
       }
        
        vm.undoHSHC = function(dataItem){
        	
               dataItem.sysUserId = Constant.userInfo.vpsUserToken.sysUserId;
               /*hoanm1 if(dataItem.completeUserUpdate != dataItem.sysUserId) {
          		  toastr.error(gettextCatalog.getString("Không có quyền Undo HSHC"));

          	     }else{*/
          	    	confirm('Xác nhận bản ghi đã chọn?', function () {
                      	//debugger;
                          Restangular.all("constructionTaskService/UpdateUndoHshc").post(dataItem).then(function (data) {
                          	debugger;
                              if (data.error) {
                                  toastr.error(data.error);
                                  return;
                              }
                              vm.constructionQLOSGrid.dataSource.page(1);
                              debugger;
                              toastr.success("Undo thành công!");
                              $(".k-icon.k-i-close").click();
                              return;

                      }).catch(function (e) {
                          toastr.error(gettextCatalog.getString("Lỗi khi Undo"));
                          return;
                      });
                  });
          	     //hoanm1}
     
        }
      //tungtt20181128 _ end
       // vm.handleCheck = handleCheck;
       // function handleCheck(dataItem) {
        //    if (dataItem.selected) {
        //        $scope.listCheck.push(dataItem);
        //    } else {
        //        return Restangular.all("constructionTaskService/checkPermissions").post(dataItem).then(function (d) {
        //            if(d.error){
        //                $('[name="gridcheckbox"]').addClass("checked");
        //                $('.checked').prop('checked', false);
        //                toastr.error(d.error);
        //            }
        //        }).catch(function (e) {
         //           for (var i = 0; i < $scope.listCheck.length; i++) {
         //               if ($scope.listCheck[i].workItemId === dataItem.workItemId) {
         //                   $scope.listCheck.splice(i, 1);
          //              }
          //          }
           //     })
          //  }
	  //        }
//	hungtd_20181211_start
vm.handleCheck = handleCheck;
        function handleCheck(dataItem) {
        	vm.constrObj = dataItem;
        	if(vm.saveDataItem.length > 0){
        		debugger;
        		for(var k=0; k < vm.saveDataItem.length; k++){
        			if(!(vm.saveDataItem[k].constructionId == vm.constrObj.constructionId)){
        				vm.saveDataItem.push(vm.constrObj);
        			}
        		}
        	} else {
        		vm.saveDataItem.push(vm.constrObj);
        	}
        	
        	var list = [];
            var dateNow = kendo.parseDate(new Date(),"dd/MM/yyyy");
            dateNow.setHours(0);
            dateNow.setMinutes(0);
            dateNow.setSeconds(0);
            dateNow.setMilliseconds(0);
            var dateNowCurr = kendo.parseDate(dateNow,"dd/MM/yyyy");
            var dateCheck = kendo.parseDate(dataItem.dateComplete,"dd/MM/yyyy");
            var monthDateNow = dateCheck.getMonth() +1;
            var yearDateNow = dateCheck.getFullYear();
            debugger;
            if(monthDateNow<12){
            	var currMonthNext = monthDateNow+1;
            	var currMonthNextDate = '05' + '/' + currMonthNext +'/'+ yearDateNow;
            	var dateNowNext = kendo.parseDate(currMonthNextDate,"dd/MM/yyyy");
            		if(dateNowCurr > dateNowNext){
            		toastr.warning("Đã quá thời gian phê duyệt!");
            		return;
            	}
            } else{
            	var currMonthNext = '01';
            	var currYearNext = yearDateNow + 1;
            	var currMonthNextDate = '05' + '/' + currMonthNext +'/'+ currYearNext;
            	var dateNowNext = kendo.parseDate(currMonthNextDate,"dd/MM/yyyy");
            	if(dateNowCurr > dateNowNext){
            		toastr.warning("Đã quá thời gian phê duyệt!");
            		return;
            	}
            }
            if (dataItem.selected) {
                $scope.listCheck.push(dataItem);
            } else {
                return Restangular.all("constructionTaskService/checkPermissions").post(dataItem).then(function (d) {
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
//	hungtd_20181211_end
	
        vm.updateDayHshc = function(dataItem){
        	return Restangular.all("constructionTaskService/checkPermissions").post(dataItem).then(function (d) {
                if(d.error){
                    toastr.error(d.error);
                    return;
                }
            }).catch(function(e){
        		dataItem.receiveRecordsDate;
        		
        		
        	 return Restangular.all("constructionService/updateDayHshc").post(dataItem).then(function (d){
        		 if (d.error) {
                     toastr.error(d.error);
                     return;
                 }
        		 toastr.success("cập nhật ngày nhận Hshc thành công!")
        	 }).catch(function (e) {
                 toastr.error(gettextCatalog.getString("Lỗi khi cập nhật"));
                 return;
             });
            });
        }
       
    vm.removeComplete = function(dataItem){
    	vm.constrObj = dataItem;
    	
        return Restangular.all("constructionTaskService/checkPermissions").post(dataItem).then(function (data) {
            if(data.error){
                toastr.error(data.error);
                return;
            }
        }).catch(function (e) {

           
        	var templateUrl = "coms/contructionQLOS/popupCancelConfirmOS.html";
            var title = "Thông tin hủy bỏ xác nhận";
            var windowId = "CANCEL_CONFIRM_FILLTER";
            vm.popUpOpen = 'CANCEL_CONFIRM_FILLTER';
            CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, false, '550', 'auto', "code");
        });


    }
    
//    vm.updateHSHC=updateHSHC;
//	function updateHSHC(){
    vm.removeComplete1 =removeComplete1;
    function removeComplete1(){
//    	obj.userId = Constant.userInfo.casUser.userId;
        return Restangular.all("constructionTaskService/checkPermissions").post(dataItem).then(function (d) {
            if(d.error){
                toastr.error(d.error);
                $("#childcheck").addClass("checked");
                $('.checked').prop('checked', false);
                return;
            }
        }).catch(function (e) {
            var listId = getConstruction();
//            if (vm.constrObj.approveDescription == undefined || vm.constrObj.approveDescription == '') {
//                toastr.warning("Lý do hủy xác nhận không được để trống")
//                return;
//            }
            var description = $("#approveCompleteDescription").val();
            if (listId != null && listId.length > 0) {
            	debugger;
                return Restangular.all("constructionTaskService/UpdateConstructionTask").post(obj).then(function (d) {
                    if (d.error) {
                        toastr.error(d.error);
                        return;
                    }
                    toastr.success("Từ chối thành công !")
                    CommonService.dismissPopup1();
                    vm.constructionQLOSGrid.dataSource.page(1);
                }).catch(function (e) {
                    toastr.error(gettextCatalog.getString("Lỗi khi hủy hoàn thành"));
                    return;
                });
                ;
            } else {
                toastr.warning("Chưa chọn công việc !")
            }
        });

    }
//tanqn config 20181102 start
vm.removeHSHC = function(dataItem){
    var list = [];
    var dateNow = kendo.parseDate(new Date(),"dd/MM/yyyy");
    dateNow.setHours(0);
    dateNow.setMinutes(0);
    dateNow.setSeconds(0);
    dateNow.setMilliseconds(0);
    var dateNowCurr = kendo.parseDate(dateNow,"dd/MM/yyyy");
    var dateCheck = kendo.parseDate(dataItem.dateComplete,"dd/MM/yyyy");
    var monthDateNow = dateCheck.getMonth() +1;
    var yearDateNow = dateCheck.getFullYear();
    debugger;
    if(monthDateNow<12){
    	var currMonthNext = monthDateNow+1;
    	var currMonthNextDate = '05' + '/' + currMonthNext +'/'+ yearDateNow;
    	var dateNowNext = kendo.parseDate(currMonthNextDate,"dd/MM/yyyy");
    		if(dateNowCurr > dateNowNext){
    		toastr.warning("Đã quá thời gian phê duyệt!");
    		return;
    	}
    } else{
    	var currMonthNext = '01';
    	var currYearNext = yearDateNow + 1;
    	var currMonthNextDate = '05' + '/' + currMonthNext +'/'+ currYearNext;
    	var dateNowNext = kendo.parseDate(currMonthNextDate,"dd/MM/yyyy");
    	if(dateNowCurr > dateNowNext){
    		toastr.warning("Đã quá thời gian phê duyệt!");
    		return;
    	}
    }
	vm.constrObj = dataItem;
    //return Restangular.all("constructionTaskService/checkPermissions").post(dataItem).then(function (data) {
	return Restangular.all("constructionTaskService/checkPermissionsApprovedHSHC").post(dataItem).then(function (data) {
        if(data.error){
            toastr.error(data.error);
            return;
        }
    }).catch(function (e) {
    	var templateUrl = "coms/contructionQLOS/popupCancelConfirmOS.html";
        var title = "Thông tin hủy bỏ xác nhận";
        var windowId = "CANCEL_CONFIRM_FILLTER";
        vm.popUpOpen = 'CANCEL_CONFIRM_FILLTER';
        vm.constrObj.approveCompleteDescription=null;
        $('#approveCompleteDescription').focus();
        CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, false, '550', 'auto', "approveCompleteDescription");
    });

}
	vm.updateHSHC=updateHSHC;
	function updateHSHC(){
		debugger;
		var obj={};
	    vm.listContractionId = [];
		obj = vm.constrObj;
		obj.approveDescription = vm.constrObj.approveDescription;
		obj.sysUserId = Constant.userInfo.VpsUserInfo.sysUserId;
		for(var i=0; i<vm.saveDataItem.length; i++){
			var constructionId = vm.saveDataItem[i].constructionId;
			vm.listContractionId.push(constructionId);
		}
		obj.listContractionId = vm.listContractionId;
		 if (vm.constrObj.approveDescription == undefined || vm.constrObj.approveDescription == '') {
             toastr.warning("Lý do hủy xác nhận không được để trống")
             return;
         }
		return Restangular.all("constructionService/UpdateConstructionTask").post(obj).then(function (result) {
//			if (monthComple <= Number(d.getMonth()) && Number(d.getDate()) > 5 && yearComple <= Number(d.getFullYear())) {
//	            toastr.error(gettextCatalog.getString("Đã quá thời gian từ chối HSHC"));
//	        } else {
			if(result.error){
				$('#approveCompleteDescription').focus();
				toastr.error(result.error);
				return;
			}
			toastr.success("Từ chối thành công!");
			vm.constructionQLOSGrid.dataSource.page(1);
			//$("#exGrid").data('kendoGrid').dataSource.read();
			//$("#exGrid").data('kendoGrid').refresh();
			$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
			//vm.cancel();
//		}
	}, function(errResponse){
          toastr.error(gettextCatalog.getString("Lỗi khi từ chối"));
          return;
      });
		
	}
//end
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
        var data = vm.constructionQLOSGrid.dataSource._data;
        vm.constructionQLOSGrid.table.find("tr").each(function(idx, item) {
            var row = $(item);
            var checkbox = $('[name="gridcheckbox"]', row);
            if (checkbox.is(':checked')) {
                var tr = vm.constructionQLOSGrid.select().closest("tr");
                var dataItem = vm.constructionQLOSGrid.dataItem(item);
                list.push(dataItem.constructionTaskId);
            }
        });
        return list;
    }

function getConstruction(){
    var list=[];
    var data = vm.constructionQLOSGrid.dataSource._data;
    vm.constructionQLOSGrid.table.find("tr").each(function(idx, item) {
        var row = $(item);
        var checkbox = $('[name="gridcheckbox"]', row);
        if (checkbox.is(':checked')) {
            var tr = vm.constructionQLOSGrid.select().closest("tr");
            var dataItem = vm.constructionQLOSGrid.dataItem(item);

            list.push(dataItem.constructionId);
        }
    });
    return list;
}



        function refreshGrid(d) {
            var grid = vm.constructionQLOSGrid;
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
        	    	  vm.searchForm.type = 1;
        	    	  return Restangular.all("constructionTaskService/exportContructionHSHC").post(vm.searchForm).then(function (d) {
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

        vm.doSearch = doSearch;
        function doSearch() {
        	//console.log(vm.constructionQLOSGrid.dataSource);
            //vm.constructionQLOSGrid.dataSource.page(1);
            var grid = vm.constructionQLOSGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        };
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
        vm.constructionQLOSGrid.hideColumn(column);
    } else if (column.hidden) {
        vm.constructionQLOSGrid.showColumn(column);
    } else {
        vm.constructionQLOSGrid.hideColumn(column);
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
    var obj = contructionQLOSService.getItem();
    obj.listFileHSHC = vm.BGMBgrid.dataSource._data
    return Restangular.all("constructionService"+"/updateHSHCItem").post(obj).then(function(data){
        if(data.error){
            toastr.error(data.error);
            return;
        }
        toastr.success("Hủy hoàn thành thành công!");
        CommonService.dismissPopup1();
        vm.constructionQLOSGrid.dataSource.page(1);
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
                contructionQLOSService.setItem(dataItem);
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
            }else if(id=='importComplete'){
				vm.searchForm.importCompleteHSHC = null;
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
        };

        // VietNT_20181128_start
        vm.disableSubmit = false;

        vm.getConstructionExcelTemplate= function(){
            function displayLoading(target) {
                var element = $(target);
                kendo.ui.progress(element, true);
                setTimeout(function(){

                    return Restangular.all("constructionTaskService/downloadTemplateFileHSHC").post(vm.searchForm).then(function (d) {
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

        vm.importConstructionHSHC = importConstructionHSHC;
    	function importConstructionHSHC() {
            vm.fileImportData = false;
            var templateUrl = "coms/contructionQLOS/importConstructionQLOS.html";
            var title = "Import Doanh thu ngoài OS";
            var windowId = "IMPORT_CONSTRUCTION_QLOS";
            CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, true, '1000', '275', null);
        }

        vm.submitConstructionHSHC = submitConstructionHSHC;
        function submitConstructionHSHC() {
            $('#testSubmit').addClass('loadersmall');
            vm.disableSubmit = true;

            // validate file
            if (!vm.fileImportData) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Bạn chưa chọn file để import");
                return;
            }
            if ($('.k-invalid-msg').is(':visible')) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                return;
            }
            if (vm.fileImportData.name.split('.').pop() !== 'xls' && vm.fileImportData.name.split('.').pop() !== 'xlsx') {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Sai định dạng file");
                return;
            }

            // start importing
            var formData = new FormData();
            formData.append('multipartFile', vm.fileImportData);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "constructionTaskService/importConstructionHSHC?folder=temp",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if (data === 'NO_CONTENT') {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.warning("File import không có nội dung");
                    }
                    else if (!!data.error) {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.error(data.error);
                    }
                    else if (data.length === 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length > 0) {
                        vm.lstErrImport = data[data.length - 1].errorList;
                        vm.objectErr = data[data.length - 1];
                        var templateUrl = "wms/createImportRequirementManagement/importResultPopUp.html";
                        var title = "Kết quả Import";
                        var windowId = "ERR_IMPORT";
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        CommonService.populatePopupCreate(templateUrl, title, vm.lstErrImport, vm, windowId, false, '80%', '420px');
                        fillDataImportErrTable(vm.lstErrImport);

                    }
                    else {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.success("Import thành công");
                        doSearch();
                    }
                    $scope.$apply();
                }
            });
        }

        function fillDataImportErrTable(data) {
            vm.importGoodResultGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                resizable: true,
                dataSource: data,
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("Không có kết quả hiển thị")
                },
                pageSize: 10,
                pageable: {
                    pageSize: 10,
                    refresh: false,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "Không có kết quả hiển thị"
                    }
                },
                columns: [
                    {
                        title: "TT",
                        field: "stt",
                        template: dataItem => $("#importGoodResultGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
                        width: 70,
                        headerAttributes: { style: "text-align:center;" },
                        attributes: { style: "text-align:center;" },
                    }, {
                        title: "Dòng lỗi",
                        field: 'lineError',
                        width: 100,
                        headerAttributes: { style: "text-align:center;" },
                        attributes: { style: "text-align:center;" }
                    }, {
                        title: "Cột lỗi",
                        field: 'columnError',
                        width: 100,
                        headerAttributes: { style: "text-align:center;" },
                        attributes: { style: "text-align:center;" },
                    }, {
                        title: "Nội dung lỗi",
                        field: 'detailError',
                        width: 250,
                        headerAttributes: { style: "text-align:center;" },
                        attributes: { style: "text-align:left;" },
                    }
                ]
            });
        }


        vm.exportExcelErr = function () {
        	contructionQLOSService.downloadErrorExcel(vm.objectErr).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
                return;
            });
        };

        vm.closeErrImportPopUp = closeErrImportPopUp;
        function closeErrImportPopUp() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

        vm.cancelPopup = cancelPopup;
        function cancelPopup() {
            $rootScope.$broadcast("handlerReturnFromPopup", "ok");
            $(".k-icon.k-i-close").click();
        }
        vm.cancelImport = cancelImport;
        function cancelImport() {
            cancelPopup();
        }
        //VietNT_end
    }
})();
