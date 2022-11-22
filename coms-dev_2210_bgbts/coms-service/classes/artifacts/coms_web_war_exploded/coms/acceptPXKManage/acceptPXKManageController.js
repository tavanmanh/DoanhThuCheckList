(function () {
    'use strict';
    var controllerId = 'acceptPXKManageController';

    angular.module('MetronicApp').controller(controllerId, acceptPXKManageController);

    function acceptPXKManageController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, acceptPXKManageService, htmlCommonService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http) {

        var vm = this;
        // variables
        vm.String = "Quản lý công trình > Quản lý công trình > Quản lý PXK vật tư A cấp";
        var ERROR_MESSAGE = "Có lỗi xảy ra";
        vm.searchForm = {
                status: 1,
                isReceivedGoods: false,
                isReceivedObstruct: false
            };
        vm.fileLst = [];
        $scope.listChecked = [];
        
        var userId = $rootScope.authenticatedUser.VpsUserInfo.sysUserId;
        vm.userId = userId;
        var showView = false;
        
        vm.htmlLabel = {
                orderCode: "Mã yêu cầu",
                code: "Mã phiếu xuất",
                constructionCode: "Mã công trình",
                sysGroupName: "Đơn vị nhận",
                dateFrom: "Ngày thực xuất từ",
                dateTo: "Đến",
                type: "Tình trạng xác nhận phiếu",
                contractCode: "Mã hợp đồng"
            };
            vm.confirmDataList = [{
                id: 0,
                name: "Chờ tiếp nhận"
            },{
                id: 1,
                name: "Đã tiếp nhận"
            },
            {
                id: 2,
                name: "Đã từ chối"
            }];

            vm.checkRolePGD = false;
            vm.listUser = null;
            
            initDataSearchForm();
            function initDataSearchForm() {
                var today = new Date();
                var from = new Date(today.getFullYear(), today.getMonth(), 1);
                vm.searchForm.dateTo = htmlCommonService.formatDate(today);
                vm.searchForm.dateFrom = htmlCommonService.formatDate(from);
                
                vm.listUser = null;
                acceptPXKManageService.checkRolePGD().then(function(result){
                	if(result!=null && result[0]!=null){
                		vm.checkRolePGD = true;
                		vm.listUser = result[0].sysUserId;
                	}
                });
                
                
                fillDataTable([]);
//                fillDataMaterial([]);
                fillDataFile([]);
            }
        
            vm.doSearch = doSearch;
            vm.cancel = cancel;
            function cancel() {
                CommonService.dismissPopup1();
            }

            vm.cancelReason = cancelReason;
            function cancelReason() {
                CommonService.dismissPopup1();
            }
            
            function doSearch() {
                if ($('.k-invalid-msg').is(':visible')) {
                    return;
                }
                $('[name="checkAll"]').prop('checked', false);
                $scope.listChecked = [];
                var grid = vm.acceptPXKManageGrid;
                if (grid) {
                    grid.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });
                }
            }

            // ----- Search form start
            vm.resetFormField = function (fieldName) {
                switch (fieldName) {
                    case 'orderCode':
                        vm.searchForm.orderCode = null;
                        break;
                    case 'code':
                        vm.searchForm.code = null;
                        break;
                    case 'constructionCode':
                        vm.searchForm.constructionCode = null;
                        break;
                    case 'date':
                        vm.searchForm.dateFrom = null;
                        vm.searchForm.dateTo = null;
                        $('.k-invalid-msg').hide();
// vm.disableSearch = false;
// vm.dateFromErr = null;
// vm.dateToErr = null;
                        break;
                    case 'group':
                        vm.searchForm.sysGroupText = null;
                        vm.searchForm.sysGroupId = null;
                        break;
                    case 'confirm':
                    	vm.searchForm.confirm = null;
                    	break;
    				case 'contractCode':
    					vm.searchForm.cntContractCode = null;
    					break;
                }
            };

            // --- sysGroup autofill start
            vm.openDepartmentPopup = function () {
                vm.deptType = 'search';
                var templateUrl = 'wms/popup/findDepartmentPopUp.html';
                var title = gettextCatalog.getString("Tìm kiếm đơn vị");
                CommonService.populatePopupDept(templateUrl, title, null, null, vm, null, 'string', false, '92%', '89%');
            };

            // sysGroup autofill options
            vm.isSelectedDept = false;
            vm.deptOptions = {
                dataTextField: "text",
                placeholder: "Nhập mã hoặc tên đơn vị",
                select: function (e) {
                    vm.isSelectedDept = true;
                    var dataItem = this.dataItem(e.item.index());
                    vm.searchForm.sysGroupText = dataItem.text;
                    vm.searchForm.sysGroupId = dataItem.id;
                },
                pageSize: 10,
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function (options) {
                            vm.isSelectedDept = false;
                            return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({
                                name: vm.searchForm.sysGroupText,
                                pageSize: vm.deptOptions.pageSize
                            }).then(function (response) {
                                options.success(response);
                            }).catch(function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            });
                        }
                    }
                },
                template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
                change: function (e) {
                    if (!vm.isSelectedDept) {
                    	vm.resetFormField('group');
                    }
                },
                close: function (e) {
                    if (!vm.isSelectedDept) {
                    	vm.resetFormField('group');
                    }
                },
                ignoreCase: false
            };
            // sysGroup autofill end ---
            // --- dateField start
            vm.changeDate = function(){
            	
            }
            
            var validator = $("#realIeDateSynStockTrans").kendoValidator({
                rules: {
                    dateValid: function(input) {
                        if (input.is("[data-role='datepicker']")) {
                        	if (!input.val()) {
                        		document.getElementById("errorDateFrom").style.display = "none";
                            	document.getElementById("errorDateTo").style.display = "none";
                        		return true;
                        	}
                            var date = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                            if (!date) {
                            	document.getElementById("errorDateFrom").style.display = "block";
                            	document.getElementById("errorDateTo").style.display = "block";
                                return false;
                            }
                        }
                        document.getElementById("errorDateFrom").style.display = "none";
                    	document.getElementById("errorDateTo").style.display = "none";
                        return true;
                    },
                    dateFrom: function(input) {
                        if (input.is("#syn_dateFrom")) {
                            var dateTo = kendo.parseDate($("#syn_dateTo").val(), 'dd/MM/yyyy');
                            var dateFrom = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                            if (!dateTo || !dateFrom) {
                            	document.getElementById("errorDateFrom").style.display = "none";
                                return true;
                            }
                            if (dateTo < dateFrom) {
                            	document.getElementById("errorDateFrom").style.display = "block";
                                return false;
                            }
                        }
                        document.getElementById("errorDateFrom").style.display = "none";
                        return true;
                    },
                    dateTo: function(input) {
                        if (input.is("#syn_dateTo")) {
                            var dateTo = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                            var dateFrom = kendo.parseDate($("#syn_dateFrom").val(), 'dd/MM/yyyy');
                            if (!dateFrom || !dateTo) {
                            	document.getElementById("errorDateTo").style.display = "none";
                                return true;
                            }
                            if (dateTo < dateFrom) {
                            	document.getElementById("errorDateTo").style.display = "block";
                                return false;
                            }
                        }
                    	document.getElementById("errorDateTo").style.display = "none";
                        return true;
                    },
                    dateIn31: function (input, a) {
                        if (input.is("#syn_dateFrom") || input.is("#syn_dateTo")) {
                            var dateTo = kendo.parseDate($("#syn_dateTo").val(), 'dd/MM/yyyy');
                            var dateFrom = kendo.parseDate($("#syn_dateFrom").val(), 'dd/MM/yyyy');
                            if (!dateTo || !dateFrom) {
                            	document.getElementById("errorDateFrom").style.display = "none";
                            	document.getElementById("errorDateTo").style.display = "none";
                            	return true;
                            }
                            var dayDiff = Math.floor((dateTo - dateFrom) / (1000*60*60*24));
                            if (dayDiff > 31) {
                            	document.getElementById("errorDateFrom").style.display = "block";
//                            	document.getElementById("errorDateTo").style.display = "block";
                                return false;
                            }
                        }
                        document.getElementById("errorDateFrom").style.display = "none";
                        document.getElementById("errorDateTo").style.display = "none";
                        return true;
                    }
                },
                messages: {
                    // dateEmpty: "Ngày không được bỏ trống",
                    dateValid: "Ngày không hợp lệ",
                    dateTo: "Chọn \"Đến ngày\" lớn hơn",
                    dateFrom: "Chọn \"Từ ngày\" nhỏ hơn",
                    dateIn31: "Chọn khoảng thời gian nhỏ hơn 31 ngày"
                }
            }).data("kendoValidator");
            
            // ----- Main Function start
            var listSynStockTrans = [];
            vm.doForwardGroup = function(dataItem) {
                vm.deptType = 'forward';

                // prepare data checkbox
                if (!!dataItem) {
                    listSynStockTrans = [dataItem];
                } else if ($scope.listChecked.length < 1) {
                    toastr.error("Chọn ít nhất 1 yêu cầu!");
                    return;
                } else {
                    listSynStockTrans = angular.copy($scope.listChecked);
                }
                console.log(listSynStockTrans);

                // open popup choose user
                var templateUrl = 'wms/popup/findDepartmentPopUp.html';
                var title = gettextCatalog.getString("Chuyển đơn vị nhận");
                CommonService.populatePopupDept(templateUrl, title, null, null, vm, 'sst', 'string', false, '92%', '89%');
            };

            vm.exportFile = function() {
                vm.searchForm.page = null;
                vm.searchForm.pageSize = null;
                var data = vm.searchForm;
                var listRemove = [{
                    title: "Thao tác"
                }, {
                    title: "Chọn"
                },{
                    title: "<input type='checkbox' id='checkAll' name='checkAll' ng-click='vm.selectAll()' ng-model='vm.checkedAll'/>"
                }
                ];

                Restangular.all("synStockTransService/doSearchAcceptManage").post(data).then(function (d) {
                    var data = d.data;
                    for(var i=0;i<data.length;i++){
                    	if(data[i].confirm==0){
                    		data[i].confirm = 'Chờ tiếp nhận';
                    	} else if(data[i].confirm==1){
                    		data[i].confirm = 'Đã tiếp nhận';
                    	} else {
                    		data[i].confirm = 'Đã từ chối';
                    	}
                    	if (data[i].state == '0' && data[i].receiverId!=null && data[i].receiverId==userId) {
                    		data[i].state = 'Chờ xác nhận';
                        } else
                    	if(data[i].state == '0' && data[i].receiverId!=null && data[i].lastShipperId==userId){
                    		data[i].state = 'Đang bàn giao';
                    	} else
                        if (data[i].state === '1') {
                        	data[i].state = 'Đã xác nhận';
                        } else
                        if (data[i].state === '2') {
                        	data[i].state = 'Từ chối xác nhận';
                        } else {
                        	data[i].state = "";
                        }
                    }
                    CommonService.exportFile(vm.acceptPXKManageGrid, data, listRemove, [], "Danh sách PXK A cấp");
                });
            };
            
            vm.handleCheck = function(dataItem) {
//                var index = indexInArraySst($scope.listChecked, dataItem);
                if (dataItem.selected) {
                    $scope.listChecked.push(dataItem);
                } else {
                	for(var i=0;i<$scope.listChecked.length;i++){
                		if($scope.listChecked[i].synStockTransId == dataItem.synStockTransId){
                			$scope.listChecked.splice(i, 1);
                		}
                	}
                    
                }
                $('[name="checkAll"]').prop('checked', false);
            };
            
            vm.selectAll = function() {
            	$scope.listChecked = [];
            	if(vm.checkedAll){
            		$('[name="checkAll"]').prop('checked', true);
            		vm.searchForm.page = null;
                    vm.searchForm.pageSize = null;
            		acceptPXKManageService.doSearchAcceptManage(vm.searchForm).then(function(d){
            			for(var i=0;i<d.data.length;i++){
            				if(!d.data[i].confirm==2 || !(d.data[i].state == 0 && d.data[i].receiverId 
            						&& d.data[i].lastShipperId!=d.data[i].receiverId  
            						&& d.data[i].lastShipperId==vm.userId)){
            					d.data[i].selected=true;
            					$scope.listChecked.push(d.data[i]);
            				}
            			}
            			$("#acceptPXKManageId").data("kendoGrid").dataSource.read();
            		}, function(e){
            			toastr.error("Có lỗi xảy ra !");
            		});
            	} else {
            		$('[name="checkAll"]').prop('checked', false);
            		$scope.listChecked=[];
            		$("#acceptPXKManageId").data("kendoGrid").dataSource.read();
            	}
            };
            
            var listSynStockTrans = [];
            vm.acceptPXK = function(dataItem){
            	var listSynStockTrans = [];
            	
            	if(!!dataItem){
                	listSynStockTrans = [dataItem];
                } else if ($scope.listChecked.length < 1) {
                    toastr.warning("Chọn ít nhất 1 yêu cầu!");
                    return;
                } else {
                	listSynStockTrans = angular.copy($scope.listChecked);
                }
            	
            	var checkAcceptStatus = true;
            	var listFail = [];
            	for(var i=0;i<listSynStockTrans.length;i++){
//            		if(listSynStockTrans[i].confirm!=0){
            		if(listSynStockTrans[i].confirm.trim()=='0'  && (listSynStockTrans[i].lastShipperId==userId 
            				|| (vm.checkRolePGD && listSynStockTrans[i].lastShipperId==vm.listUser))){
            			checkAcceptStatus = true;
            		} else {
            			checkAcceptStatus = false;
            			listFail.push(listSynStockTrans[i].code);
            		}
            	}
            	
            	if(listFail.length>0){
            		toastr.warning("Phiếu: "+ listFail.join(",") +" không đúng trạng thái tiếp nhận");
            		return;
            	}
            	
            	var postData = {};
                postData.listSynStockTrans = listSynStockTrans;
            	
            	var listCode = [];
                for (var i = 0; i < listSynStockTrans.length; i++) {
                    listCode.push(listSynStockTrans[i].code);
                }

                var message = 'Bạn muốn tiếp nhận phiếu xuất kho: ' +
                    '<br>' +
                    '<span style="color: red;font-weight: bold;">"' + listCode.join('", "');
            	confirm(message, function(){
            		acceptPXKManageService.updateAcceptPXK(postData)
                    .then(function (response) {
                        if (!!response.error) {
                            toastr.error(response.error);
                            return;
                        }
                        toastr.success("Tiếp nhận thành công!");
                        $scope.listChecked = [];
                        $('[name="checkAll"]').prop('checked', false);
                        cancel();
                        doSearch();
                    }, function (err) {
                        toastr.error(ERROR_MESSAGE);
                        console.log(err);
                    });
            	});
            }
            
//            vm.acceptAssignPXK = function(dataItem){
//            	var listSynStockTrans = [];
//            	
//            	if(!!dataItem){
//                	listSynStockTrans = [dataItem];
//                } else if ($scope.listChecked.length < 1) {
//                    toastr.error("Chọn ít nhất 1 yêu cầu!");
//                    return;
//                } else {
//                	listSynStockTrans = angular.copy($scope.listChecked);
//                }
//            	
//            	var postData = {};
//                postData.listSynStockTrans = listSynStockTrans;
//            	
//            	var listCode = [];
//                for (var i = 0; i < listSynStockTrans.length; i++) {
//                    listCode.push(listSynStockTrans[i].code);
//                }
//
//                var message = 'Bạn muốn xác nhận bàn giao phiếu xuất kho: ' +
//                    '<br>' +
//                    '<span style="color: red;font-weight: bold;">"' + listCode.join('", "');
//            	confirm(message, function(){
//            		acceptPXKManageService.updateAcceptAssignPXK(postData)
//                    .then(function (response) {
//                        if (!!response.error) {
//                            toastr.error(response.error);
//                            return;
//                        }
//                        toastr.success("Tiếp nhận thành công!");
//                        $scope.listChecked = [];
//                        cancel();
//                        doSearch();
//                    }, function (err) {
//                        toastr.error(ERROR_MESSAGE);
//                        console.log(err);
//                    });
//            	});
//            }
            
            vm.acceptAssignPXK = function(dataItem){
            	vm.listDataMaterial = [];
            	vm.dataItem = dataItem;
            	var teamplateUrl = "coms/acceptPXKManage/popupAcceptAsign.html";
                var title = "Thông tin bàn giao phiếu xuất kho";
                var windowId = "DETAIL_PXK_A";
                var obj = {};
//                obj = angular.copy(dataItem);
                obj.typeExport = "1";
                obj.page = null;
                obj.pageSize = null;
                obj.idTable = dataItem.synStockTransId;
                acceptPXKManageService.getMaterialInPxk(obj).then(function(d){
                	var data = d.data;
                	for(var i=0; i<data.length; i++){
                		data[i].realRecieveAmount = data[i].amountReal;
                	}
                	CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '70%', '450', "deptAdd");
                    fillDataMaterial(data);
                }).catch(function(error){
                	toastr.error("Có lỗi xảy ra trong quá trình lấy dữ liệu");
                	return;
                });
            }
            
            vm.dataConfirm = {};
            vm.denyPXK = function(dataItem){
            	if(dataItem==undefined){
            		if($scope.listChecked.length==0){
                		toastr.warning("Chưa chọn bản ghi để từ chối !");
                		return;
                	}
            	}
            	
            	var checkAcceptStatus = true;
            	var listFail=[];
            	for(var i=0;i<$scope.listChecked.length;i++){
            		if((($scope.listChecked[i].confirm.trim()=='0' && $scope.listChecked[i].lastShipperId==userId) 
                			|| ($scope.listChecked[i].confirm != 2 && ($scope.listChecked[i].state.trim() == '0' 
                				&& $scope.listChecked[i].receiverId!=null && $scope.listChecked[i].receiverId==userId))) 
                				|| (vm.checkRolePGD && dataItem.lastShipperId==vm.listUser)			
            		){
            			checkAcceptStatus = true;
            		} else {
            			checkAcceptStatus = false;
            			listFail.push($scope.listChecked[i].code);
            		}
            	}
            	
            	if(listFail.length>0){
            		toastr.warning("Phiếu: "+ listFail.join(",") +" không đúng trạng thái từ chối");
            		return;
            	}
            	
            	vm.dataConfirm.reason = "";
            	vm.dataItem = dataItem;
            	var templateUrl = 'coms/acceptPXKManage/popupDenyPXK.html';
                var title = gettextCatalog.getString("Lý do từ chối");
                CommonService.populatePopupDept(templateUrl, title, null, null, vm, 'deny', 'string', false, '30%', '30%');
            }
            
            vm.saveReason = function(){
            	if(vm.dataConfirm.reason==""){
            		toastr.warning("Chưa chọn lý do từ chối !");
            		return;
            	}
            	var listSynStockTrans = [];
            	if(!!vm.dataItem){
                	listSynStockTrans = [vm.dataItem];
                } else if ($scope.listChecked.length < 1) {
                    toastr.warning("Chọn ít nhất 1 yêu cầu!");
                    return;
                } else {
                	listSynStockTrans = angular.copy($scope.listChecked);
                }
            	
            	var postData = {};
                postData.listSynStockTrans = listSynStockTrans;
                postData.confirmDescription = vm.dataConfirm.reason;
            		acceptPXKManageService.updateDenyPXK(postData)
                    .then(function (response) {
                        if (response!= undefined&& !!response.error) {
                            toastr.error(response.error);
                            return;
                        }
                        toastr.success("Từ chối thành công!");
                        $('[name="checkAll"]').prop('checked', false);
                        $scope.listChecked = [];
                        cancelReason();
                        doSearch();
                    }, function (err) {
                        toastr.error(ERROR_MESSAGE);
                        console.log(err);
                    });
            }
            
            vm.lstSysUserId = [];
            vm.assignPXK = function(dataItem) {
            	vm.lstSysUserId = [];
            	if(dataItem==undefined){
            		if($scope.listChecked.length==0){
                		toastr.warning("Chưa chọn bản ghi để bàn giao !");
                		return;
                	}
            	}
            	
            	var checkState = true;
            	var listFail = [];
            	for(var i=0;i<$scope.listChecked.length;i++){
            		if($scope.listChecked[i].confirm != 2 && (($scope.listChecked[i].state.trim() == '0' && $scope.listChecked[i].receiverId==vm.userId) 
                			|| ($scope.listChecked[i].receiverId==null && $scope.listChecked[i].lastShipperId==vm.userId)
                			|| ($scope.listChecked[i].state.trim() == '1' && $scope.listChecked[i].lastShipperId==vm.userId && $scope.listChecked[i].lastShipperId==$scope.listChecked[i].receiverId)
                			|| ($scope.listChecked[i].state.trim() == '2' && $scope.listChecked[i].lastShipperId==vm.userId)) 
                			|| (vm.checkRolePGD && ($scope.listChecked[i].lastShipperId==vm.listUser || $scope.listChecked[i].receiverId==vm.listUser))){
            			checkState = true;
            		} else {
            			checkState = false;
            			listFail.push($scope.listChecked[i].code);
            		}
            		vm.lstSysUserId.push($scope.listChecked[i].lastShipperId);
            	}
            	
            	if(listFail.length>0){
            		toastr.warning("Phiếu: "+ listFail.join(",") +" có trạng thái không đúng để bàn giao");
            		return;
            	}
            	
            	if(dataItem!=undefined){
            		vm.dataItemAssign = dataItem;
                	vm.lstSysUserId.push(dataItem.lastShipperId);
            	}
            	var templateUrl = 'coms/popup/sysUserSearchPopUp.html';
                var title = gettextCatalog.getString("Tìm kiếm nhân viên");
                htmlCommonService.populatePopup(templateUrl, title, null, null, vm, null, 'string', false, '92%', '89%', 'sysUserSearchController');
            };

            vm.onSaveSysUser = function (sysGroupInfo) {
//            	confirm("Xác nhận bàn giao phiếu: "+ vm.dataItemAssign.code + " cho " + sysGroupInfo.fullName,function(){
            		var listSynStockTrans = [];
                	if(!!vm.dataItemAssign){
                    	listSynStockTrans = [vm.dataItemAssign];
                    } else if ($scope.listChecked.length < 1) {
                        toastr.warning("Chọn ít nhất 1 yêu cầu!");
                        return;
                    } else {
                    	listSynStockTrans = angular.copy($scope.listChecked);
                    }
                	
                	var postData = {};
                    postData.listSynStockTrans = listSynStockTrans;
                    postData.sysUserIdRecieve = sysGroupInfo.sysUserId;

                    acceptPXKManageService.updateAssignPXK(postData)
                                .then(function (response) {
                                    if (!!response.error) {
                                        toastr.error(response.error);
                                        return;
                                    }
                                    toastr.success("Bàn giao thành công!");
                                    $scope.listChecked = [];
                                    cancel();
                                    doSearch();
                                }, function (err) {
                                    toastr.error(ERROR_MESSAGE);
                                    console.log(err);
                                });
//            	});
            };
            
            vm.showHideColumn = function(column) {
                if (angular.isUndefined(column.hidden)) {
                	vm.acceptPXKManageGrid.hideColumn(column);
                } else if (column.hidden) {
                    vm.acceptPXKManageGrid.showColumn(column);
                } else {
                    vm.acceptPXKManageGrid.hideColumn(column);
                }
            };

            vm.gridColumnShowHideFilter = function(item) {
                return item.type == null || item.type !== 1;
            };
            
            var record = 0;
            function fillDataTable(data){
                vm.acceptPXKManageGridOptions = kendoConfig.getGridOptions({
                    autoBind: true,
                    scrollable: true,
                    resizable: true,
                    editable: false,
                    dataBinding: function () {
                        record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
//                        vm.checkedAll = false;
                    },
                    reorderable: true,
                    toolbar: [
                        {
                            name: "actions",
                            template:
                                '<span><i class="fa fa-file" style="color: deepskyblue; position: relative; left: 40px; top: 2px; pointer-events: none; font-size: 19px;"></i></span>' +
                                '<button class="btn btn-qlk padding-search-right" ng-click="vm.acceptPXK()" uib-tooltip="Tiếp nhận" style="width: 130px;" translate>Tiếp nhận</button>'
                                +
                                '<span><i class="fa fa-arrow-right" style="color: orange; position: relative; left: 26px; top: 2px; pointer-events: none; font-size: 19px;"></i></span>' +
                                '<button class="btn btn-qlk padding-search-right " ng-click="vm.assignPXK()" uib-tooltip="Bàn giao" style="width: 130px;" translate>Bàn giao</button>'
                                +
                                '<span><i class="fa fa-close" style="color: red; position: relative; left: 26px; top: 2px; pointer-events: none; font-size: 19px;"></i></span>' +
                                '<button class="btn btn-qlk padding-search-right " ng-click="vm.denyPXK()" uib-tooltip="Từ chối nhận" style="width: 130px;" translate>Từ chối nhận</button>'
                                +
                                '<div class="btn-group pull-right margin_top_button margin_right10">' +
                                '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                                '<i class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()" aria-hidden="true"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                                '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                                '<label ng-repeat="column in vm.acceptPXKManageGrid.columns.slice(1, vm.acceptPXKManageGrid.columns.length) | filter: vm.gridColumnShowHideFilter">' +
                                '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                                '</label>' +
                                '</div></div>'
                        }
                    ],
                    dataSource: {
                        serverPaging: true,
                        schema: {
                            total: function (response) {
                                $timeout(function () {
                                    vm.count = response.total
                                });
                                return response.total;
                            },
                            data: function (response) {
                                var gridData = response.data;
                                for (var i = 0; i < gridData.length; i++) {
                                    for (var j = 0; j < $scope.listChecked.length; j++) {
                                        if (gridData[i].synStockTransId == $scope.listChecked[j].synStockTransId) {
                                            gridData[i].selected = true;
                                        }
                                    }
                                }

                                return gridData;
                            }
                        },
                        transport: {
                            read: {
                                url: Constant.BASE_SERVICE_URL + "synStockTransService/doSearchAcceptManage",
                                contentType: "application/json; charset=utf-8",
                                type: "POST"
                            },
                            parameterMap: function (options, type) {
                                vm.searchForm.page = options.page;
                                vm.searchForm.pageSize = options.pageSize;
                                return JSON.stringify(vm.searchForm)
                            }
                        },
                        pageSize: 10
                    },
                    noRecords: true,
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
                            hidden: true
                        },{
                            title: "<input type='checkbox' id='checkAll' name='checkAll' ng-click='vm.selectAll()' ng-model='vm.checkedAll'/>",
                            template: "<input type='checkbox' id='childcheck' name='gridcheckbox' ng-model='dataItem.selected' ng-click='vm.handleCheck(dataItem)' ng-hide='dataItem.confirm==2 " +
                            		"|| (dataItem.confirm == 1 && dataItem.state == 0 && dataItem.receiverId && dataItem.lastShipperId!=dataItem.receiverId  && dataItem.lastShipperId==vm.userId) " +
                            		"|| (dataItem.state == 2 && dataItem.receiverId==vm.userId)'/>",
                            width: '40px',
                            headerAttributes: {
                                style: "text-align:center;"
                            },
                            attributes: {
                                style: "text-align:center;"
                            },
//                           
                        },
                        {
                            title: "Mã yêu cầu",
                            field: 'orderCode',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:left;height:44px;white-space: normal;"
                            }
                        },
                        {
                            title: "Mã phiếu xuất",
                            field: 'code',
                            width: '180px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            template: function (dataItem) {
                                return "<a href='#' ng-click='vm.viewDetailPXK(dataItem)'>" + dataItem.code + "</a>";
                            }
                            // template: function (dataItem) {
                            // return "<a href='#'
							// ng-click='vm.viewDetail(dataItem)'>#code#</a>/>";
                            // }
                        },
                        {
                            title: "Mã trạm",
                            field: 'catStationCode',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:left;white-space: normal;"
                            }
                        },
        				{
                            title: "Mã hợp đồng",
                            field: 'cntContractCode',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:left;white-space: normal;"
                            }
                        },
                        {
                            title: "Mã công trình",
                            field: 'constructionCode',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:left;white-space: normal;"
                            }
                        },
//                        {
//                            title: "Mã kho xuất",
//                            field: 'synStockName',
//                            width: '150px',
//                            headerAttributes: {
//                                style: "text-align:center;white-space: normal;"
//                            },
//                            attributes: {
//                                style: "text-align:center;white-space: normal;"
//                            }
//                        },
                        {
                            title: "Người nhận",
                            field: 'lastShipperName',
                            width: '120px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:left;white-space: normal;"
                            }
                        },
                        {
                            title: "Đơn vị nhận",
                            field: 'sysGroupName',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:left;white-space: normal;"
                            }
                        },
                        {
                            title: "Tình trạng xác nhận phiếu",
                            field: 'confirm',
                            width: '100px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:left;white-space: normal;"
                            },
                            template: function (dataItem) {
//                            	if (dataItem.confirm.trim() === '0' && dataItem.lastShipperId==userId) {
                            	if (dataItem.confirm.trim() === '0') {
                                    return 'Chờ tiếp nhận';
                                }
                                if (dataItem.confirm.trim() === '1') {
                                    return 'Đã tiếp nhận';
                                }
                                if (dataItem.confirm.trim() === '2' || (dataItem.confirm.trim() === '0' && !dataItem.receiverId && dataItem.state.trim() == '0' && dataItem.lastShipperId!=userId)) {
                                    return 'Đã từ chối';
                                }
                                return null;
                            }
                        },
                        {
                            title: "Tình trạng bàn giao phiếu",
                            field: 'state',
                            width: '100px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:left;white-space: normal;"
                            },
                            template: function (dataItem) {
                            	if (dataItem.state.trim() == '0' && dataItem.receiverId!=null && dataItem.receiverId==userId) {
                                    return 'Chờ xác nhận';
                                } else
                            	if(dataItem.state == '0' && dataItem.receiverId!=null && dataItem.lastShipperId==userId){
                            		return 'Đang bàn giao';
                            	}
                                if (dataItem.state.trim() === '1') {
                                    return 'Đã xác nhận';
                                }
                                if (dataItem.state.trim() === '2') {
                                    return 'Từ chối xác nhận';
                                }
                                return "";
                            }
                        },
                        {
                            title: "Lý do từ chối",
                            field: 'confirmDescription',
                            width: '120px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:left;white-space: normal;"
                            },
                            template: function(dataItem){
                            	if(dataItem.confirmDescription==null){
                            		return "";
                            	} else {
                            		return dataItem.confirmDescription;
                            	}
                            }
                        },
                        {
                            title: "Thao tác",
                            width: '180px',
                            field: "action",
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            template: function (dataItem) {
                            	if(dataItem.confirm != 2 && (dataItem.state.trim() == '0' && dataItem.receiverId!=null && dataItem.receiverId==userId)){
                            		dataItem.showViewAcceptAssign = true;
                            	} else {
                            		dataItem.showViewAcceptAssign = false;
                            	}

                            	if(dataItem.confirm != 2 && ((dataItem.state.trim() == '0' && dataItem.receiverId==vm.userId) 
                            			|| (dataItem.receiverId==null && dataItem.lastShipperId==vm.userId)
                            			|| (dataItem.state.trim() == '1' && dataItem.lastShipperId==vm.userId && dataItem.lastShipperId==dataItem.receiverId)
                            			|| (dataItem.state.trim() == '2' && dataItem.lastShipperId==vm.userId))
                            			|| (vm.checkRolePGD && (dataItem.lastShipperId==vm.listUser || dataItem.receiverId==vm.listUser))
                            	){
                            		dataItem.showViewAssign = true;
                            	} else {
                            		dataItem.showViewAssign = false;
                            	}
                            	
                            	if(dataItem.confirm.trim()=='0'  && (dataItem.lastShipperId==userId || (vm.checkRolePGD && dataItem.lastShipperId==vm.listUser))){
                            		dataItem.viewAccept = true;
                            	} else {
                            		dataItem.viewAccept = false;
                            	}
                            	if(((dataItem.confirm.trim()=='0' && dataItem.lastShipperId==userId) 
                            			|| dataItem.showViewAcceptAssign) || (vm.checkRolePGD && dataItem.lastShipperId==vm.listUser)
                            		){
                            		dataItem.viewDeny = true;
                            	} else {
                            		dataItem.viewDeny = false;
                            	}
                            	if(dataItem.status!=1 && (dataItem.confirm.trim()=='0' || dataItem.confirm.trim()=='2')){
                            		dataItem.viewChangeGroup = true;
                            	} else {
                            		dataItem.viewChangeGroup = false;
                            	}
                                return '<div class="text-center">'
	                                +'<button style=" border: none; background-color: white;" id="Acceptid"' +
	                                'class=" icon_table" ng-click="vm.acceptPXK(dataItem)" ng-show="dataItem.viewAccept"  uib-tooltip="Tiếp nhận" translate' + '>' +
	                                '<i class="fa fa-file" style="color: deepskyblue;" aria-hidden="true"></i>' +
	                                '</button>'
                                    + '<button  style=" border: none; background-color: white;" id="sst_forwardGroup" ng-click="vm.assignPXK(dataItem)" ' +
                                    'class="icon_table aria-hidden" ng-show="dataItem.showViewAssign"' +
                                    'uib-tooltip="Bàn giao" translate>' +
                                    '<i class="fa fa-arrow-right" style="color: #FFCC00;" aria-hidden="true"></i>' +
                                    '</button>'
                                    +'<button style=" border: none; background-color: white;" id="AcceptAssignId" ng-show="dataItem.showViewAcceptAssign"' +
                                    'class=" icon_table" ng-click="vm.acceptAssignPXK(dataItem)" uib-tooltip="Xác nhận bàn giao" translate' + '>' +
                                    '<i class="fa fa-check" style="color:#00FF00" ng-disabled="" aria-hidden="true"></i>' +
                                    '</button>'
                                    +'<button style=" border: none; background-color: white;" id="removeId"' +
                                    'class=" icon_table" ng-click="vm.denyPXK(dataItem)" ng-show="dataItem.viewDeny"  uib-tooltip="Từ chối" translate' + '>' +
                                    '<i class="fa fa-times-circle" style="color: #FF0000;"  aria-hidden="true"></i>' +
                                    '</button>'
                                    //
                                    +'<button style=" border: none; background-color: white;" id="removeId"' +
                                    'class=" icon_table" ng-click="vm.doForwardGroup(dataItem)" ng-show="dataItem.viewChangeGroup"  uib-tooltip="Chuyển đơn vị" translate' + '>' +
                                    '<i class="fa fa-share" style="color: #093cf5;"  ng-disabled="" aria-hidden="true"></i>' +
                                    '</button>'
                                    //
                                    + '</div>';
                            }
                        }
                    ]
                });
            }
            
            //Huypq-20200203-start
            var listSynStockTrans = [];
            vm.doForwardGroup = function(dataItem) {
                vm.deptType = 'forward';

                // prepare data checkbox
                if (!!dataItem) {
                    listSynStockTrans = [dataItem];
                } else if (vm.listChecked.length < 1) {
                    toastr.error("Chọn ít nhất 1 yêu cầu!");
                    return;
                } else {
                    listSynStockTrans = angular.copy(vm.listChecked);
                }

                //open popup choose user
                var templateUrl = 'wms/popup/findDepartmentPopUp.html';
                var title = gettextCatalog.getString("Chuyển đơn vị nhận");
                CommonService.populatePopupDept(templateUrl, title, null, null, vm, 'sst', 'string', false, '92%', '89%');
            };

            vm.onSave = function (sysGroupInfo) {
                if (vm.deptType === 'search') {
                    // save searchFunction
                    vm.searchForm.sysGroupText = sysGroupInfo.text;
                    vm.searchForm.sysGroupId = sysGroupInfo.id;
                } else {
                    var postData = {};
                    // save forward sysGroup
                    postData.sysGroupId = sysGroupInfo.id;
                    postData.listToForward = listSynStockTrans;

                    // prepare msg data
                    var listCode = [];
                    for (var i = 0; i < listSynStockTrans.length; i++) {
                        listCode.push(listSynStockTrans[i].code);
                    }

                    var message = 'Bạn muốn chuyển phiếu xuất kho: ' +
                        '<br>' +
                        '<span style="color: red;font-weight: bold;">"' + listCode.join('", "') + '"</span>' +
                        '<br><br>' +
                        ' sang đơn vị <span style="color: red;font-weight: bold;">"' + sysGroupInfo.text + '"</span> ?';

                    // confirm box
                    confirm(message,
                        function () {
                            //confirm
                    	acceptPXKManageService.doForwardGroup(postData)
                                .then(function (response) {
                                    if (!!response.error) {
                                        toastr.error(response.error);
                                        return;
                                    }
                                    toastr.success("Chuyển đơn vị thành công!");
                                    vm.listChecked = [];
                                    cancel();
                                    doSearch();
                                }, function (err) {
                                    toastr.error(ERROR_MESSAGE);
                                    console.log(err);
                                })
                            },
                        null,
                        null,
                        '400',
                        '180');
                }
            };
            //Huy-end
            
            vm.synStockTransView = true;
            covenantFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
                kendoConfig, $kWindow, acceptPXKManageService,
                CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm, $http);
            
            // view detail
            vm.viewDetailPXK = viewDetailPXK;
            function viewDetailPXK(dataItem) {
                if (!!!dataItem.buss) {
                    dataItem.buss = dataItem.bussinessTypeName;        	
                }
                dataItem.synType = 1;
                vm.synStockTransId = dataItem.synStockTransId;
                vm.detaillPXK(dataItem);
            }
            
            //Danh sách file đính kèm
            function fillDataFile(data){
            	vm.attachFileGridOptions = kendoConfig.getGridOptions({
                    autoBind: true,
                    resizable: true,
                    dataSource: vm.fileLst,
                    noRecords: true,
                    columnMenu: false,
                    scrollable: false,
                    editable: true,
                    messages: {
                        noRecords: gettextCatalog.getString("Không có kết quả hiển thị")
                    },
                    dataBound: function () {
                        var GridDestination = $("#attachFileGrid").data("kendoGrid");
                        GridDestination.pager.element.hide();
                    },
                    columns: [
                        {
                            title: "TT",
                            field: "stt",
                            template: dataItem => $("#attachFileGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
                            width: 20,
                            headerAttributes: {
                                style: "text-align:center;"
                            },
                            attributes: {
                                style: "text-align:center;"
                            }
                        }, {
                            title: "Tên file",
                            field: 'name',
                            width: 150,
                            headerAttributes: {
                                style: "text-align:center;"
                            },
                            attributes: {
                                style: "text-align:left;"
                            },
                            template: function (dataItem) {
                                return "<a href='' ng-click='caller.downloadFile(dataItem)'>" + dataItem.name + "</a>";
                            }
                        }, {
                            title: "Ngày upload",
                            field: 'createdDate',
                            width: 150,
                            headerAttributes: {
                                style: "text-align:center;"
                            },
                            attributes: {
                                "id": "appFile",
                                style: "text-align:left;"
                            }
                        }, {
                            title: "Người upload",
                            field: 'createdUserName',
                            width: 150,
                            headerAttributes: {
                                style: "text-align:center;"
                            },
                            attributes: {
                                "id": "appFile",
                                style: "text-align:left;"
                            }
                        }, {
                            title: "Xóa",
                            template: dataItem =>
                                '<div class="text-center #=attactmentId#""> ' +
                                '<a type="button" class="#=attactmentId# icon_table" uib-tooltip="Xóa" translate> ' +
                                '<i class="fa fa-trash" ng-click=caller.removeRowFile(dataItem) ria-hidden="true"></i>' +
                                '</a>' +
                                '</div>',
                            width: 20,
                            field: "acctions"
                        }
                    ]
                });
            }
            
            vm.submitAttachFile = submitAttachFile;
            function submitAttachFile() {
                sendFile("attachfile", callback);
            }
            
            function sendFile(id, callback) {
            	var file = null;
            	try {
            		file = $("#" + id)[0].files[0];
            	} catch (err) {
                    toastr.warning("Bạn chưa chọn file");
            		return;
            	}

                if (!htmlCommonService.checkFileExtension(id)) {
                    toastr.warning("File không đúng định dạng cho phép");
                    return;
                }
                var formData = new FormData();
                formData.append('multipartFile', file);
                return $.ajax({
                    url: Constant.BASE_SERVICE_URL + "fileservice/uploadATTTInput",
                    type: "POST",
                    data: formData,
                    enctype: 'multipart/form-data',
                    processData: false,
                    contentType: false,
                    cache: false,
                    success: function (data) {
                        callback(data, file, id)
                    }
                });
            }
            
            function callback(data, fileReturn, id) {
                for (var i = 0; i < data.length; i++) {
                     var file = {};
                    file.name = fileReturn.name;
                    file.createdDate = htmlCommonService.getFullDate();
                    file.createdUserName = Constant.userInfo.VpsUserInfo.fullName;
                    file.createdUserId = Constant.userInfo.VpsUserInfo.sysUserId;
                    file.filePath = data[i];
                    file.type = 0;

//                    vm.fileLst = [];
                    vm.fileLst.push(file);
                }

                var grid = id === "attachfile" ? vm.attachFileGrid : vm.attachFileEditGrid;
                refreshGrid(grid, vm.fileLst);
                $('#attachfile').val(null);
            }

            function downloadFile(dataItem) {
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
            }

            vm.removeRowFile = removeRowFile;
            function removeRowFile(dataItem) {
                confirm('Xác nhận xóa', function () {
                    var index = vm.fileLst.indexOf(dataItem);
                    vm.fileLst.splice(index, 1);
                    var grid = vm.attachFileGrid;
                    refreshGrid(grid, vm.fileLst);
                })
            }

            function refreshGrid(grid, fileLst) {
                grid.dataSource.data(fileLst);
                grid.refresh();
            }
            
            //Danh sách vật tư PXK
            
            function fillDataMaterial(data){
            	var dataSource = new kendo.data.DataSource({
        			data: data,
        			pageSize: 10,
        			autoSync: false,
        			schema: {
        				model: {
        					id: "gridMaterialId",
        					fields: {
        						stt: {type: "text",editable: false },
        						goodsCode: {type: "text",editable: false },
        						goodsName: {type: "text",editable: false },
        						goodsUnitName: {type: "text",editable: false },
        						amountReal: {type: "number",editable: false },
        						realRecieveAmount: {type: "number",editable: true },
        						realRecieveDate: {type: "date",editable: true  },
        					}
        				}
        			}
        		});
            	vm.materialGridOptions = kendoConfig.getGridOptions({
                    autoBind: true,
                    scrollable: true,
                    resizable: true,
//                    editable: false,
                    dataBinding: function () {
                        record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    reorderable: true,
                    dataSource: dataSource,
                    noRecords: true,
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
                            width: '80px',
                            columnMenu: false,
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:center;"
                            }
                        },
                        {
                            title: "Mã vật tư",
                            field: 'goodsCode',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:left;height:44px;white-space: normal;"
                            }
                        },
                        {
                            title: "Tên vật tư",
                            field: 'goodsName',
                            width: '180px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                        },
                        {
                            title: "Đơn vị tính",
                            field: 'goodsUnitName',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:left;white-space: normal;"
                            }
                        },
        				{
                            title: "Số lượng theo PXK",
                            field: 'amountReal',
                            width: '100px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:left;white-space: normal;"
                            }
                        },
                        {
                            title: "Số lượng thực nhận",
                            field: 'realRecieveAmount',
                            width: '100px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:left;white-space: normal;"
                            },
                            editor: function(container, options) {
                                var input = $("<input id='qtt' ng-blur ='caller.validateQuantityNow(dataItem)'/>");
                                    input.attr("name", options.field);
                                    input.attr("type", "number");
                                    input.attr("style", "width:100%");
                                    input.appendTo(container);
                            }
                        },
                        {
                            title: "Ngày thực nhận",
                            field: 'realRecieveDate',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            editor: function (container, options) {
                                var input = $("<input/>");
                                input.attr("name", options.field);
                                input.appendTo(container);
                                input.kendoDatePicker({
                                	format: "dd/MM/yyyy",
                                });
                            },
                            template: function(dataItem) {
                            	if(!dataItem.realRecieveDate){
                            		return "";
                            	}
                            	return kendo.toString(dataItem.realRecieveDate, "dd/MM/yyyy");
                            }
                        },
                    ]
                });
            }
            
       vm.validateQuantityNow = function (dataItem) {
    	   if(dataItem.realRecieveAmount < 0){
    		   dataItem.realRecieveAmount = 0;
    		   toastr.warning("Số lượng thực nhận phải lớn hơn hoặc bằng 0");
    		   return;
    	   } else {
    		   if(dataItem.realRecieveAmount > dataItem.amountReal){
        		   toastr.warning("Số lượng thực nhận phải nhỏ hơn hoặc bằng Số lượng theo PXK");
        		   return;
        	   }
    	   }
       }

       vm.listDataMaterial = []; 
       vm.saveAccept = function (){
    	   vm.listDataMaterial = []; 
    	   var listFileData = [];
    	   var listSynStockTrans = [];
    	   listSynStockTrans = [vm.dataItem];
    	   
    	   var gridFile = $("#attachFileGrid").data("kendoGrid").dataSource.data();
    	   if(gridFile.length==0){
    		   toastr.warning("Chưa chọn file ảnh Biên bản bàn giao đính kèm");
    		   return;
    	   }
    	   for(var j=0;j<gridFile.length;j++){
    		   listFileData.push(gridFile[j]);
    	   }
    	   
    	   var grid = $("#gridMaterialId").data("kendoGrid").dataSource.data();
    	   for(var i=0;i<grid.length;i++){
    		   grid[i].realRecieveDate = kendo.toString(grid[i].realRecieveDate, "dd/MM/yyyy");
    		   vm.listDataMaterial.push(grid[i]);
    	   }
    	   
    	   var obj = {};
    	   obj.listDataMaterial = vm.listDataMaterial;
    	   obj.listSynStockTrans = listSynStockTrans;
    	   obj.listFileData = listFileData;
    	   
    	   acceptPXKManageService.updateAcceptAssign(obj)
           .then(function (response) {
               if (!!response.error) {
                   toastr.error(response.error);
                   return;
               }
               toastr.success("Xác nhận bàn giao thành công !");
               vm.listDataMaterial = [];
               cancel();
               doSearch();
           }, function (err) {
               toastr.error("Có lỗi xảy ra trong quá trình xác nhận bàn giao !");
               console.log(err);
           })
       }
    // end controller
    }
})();