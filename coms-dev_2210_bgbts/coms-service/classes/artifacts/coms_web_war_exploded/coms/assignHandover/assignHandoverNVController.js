//VietNT_19122018_created
(function () {
    'use strict';
    var controllerId = 'assignHandoverNVController';

    angular.module('MetronicApp').controller(controllerId, assignHandoverNVController);

    function assignHandoverNVController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, assignHandoverService, htmlCommonService, constructionTaskService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant) {

        var vm = this;
        // variables
        var modalInstance;
        var today = new Date();
        vm.String = "Quản lý công trình > Quản lý công trình > Giao việc cho nhân viên";
        vm.keySearch = null;
        vm.sysGroupId = null;
        vm.searchForm = {
            status: 1,
            isReceivedGoods: false,
            isReceivedObstruct: false,
            receivedStatusList: [0] 
        };
        vm.workItemSearch = {
            listImage: []
        };
        vm.assignHandoverGridNV = {};
        vm.performerPopupGrid = {};
        vm.showSearch = true;
        vm.showDetail = false;
        vm.folder = '';
        vm.assignHandover = {
            isDesign: 0
        };
        vm.fileLst = [];
        vm.dataEdit = {};
        vm.listChecked = [];
        vm.listConstructionCodeChecked = [];
        vm.disableSearch = false;
        vm.imageSelected = {};
        vm.assignHandoverSearch = 1;
        vm.checkFormat=false;

        //list function
        vm.canDoSearch = canDoSearch;
        vm.doSearch = doSearch;
        vm.callbackDoSearch = callbackDoSearch;

        vm.openSysUserPopup = openSysUserPopup;
        vm.onSaveSysUser = onSaveSysUser;
        vm.readFileStation = readFileStation;
        vm.getStationCodeTemplate = getStationCodeTemplate;
        vm.downloadFile = downloadFile;
        vm.getAttachFile = getAttachFile;

        // reset input function
        vm.resetFormFieldReceiveStatus = resetFormFieldReceiveStatus;
        vm.resetFormFieldConstructionStatus = resetFormFieldConstructionStatus;
        vm.resetFormFieldDate = resetFormFieldDate;
        vm.resetFormFieldDeptDate = resetFormFieldDeptDate;
        vm.resetFormFieldPerforment = resetFormFieldPerforment;
        vm.resetFormFile = resetFormFile;
        vm.resetFormFieldOutOfDateConstruction = resetFormFieldOutOfDateConstruction;
        vm.resetFormFieldSysUserTask = resetFormFieldSysUserTask;
        vm.resetFormFieldOutOfDateStartDate = resetFormFieldOutOfDateStartDate;

        // show hide column
        vm.showHideColumn = showHideColumn;
        vm.gridColumnShowHideFilter = gridColumnShowHideFilter;

        // main function
        vm.viewDetail = viewDetail;
        vm.changeImage = changeImage;
        vm.removeImage = removeImage;
        vm.resetViewDetail = resetViewDetail;
        vm.handover = handover;
        vm.handleCheck = handleCheck;
        vm.doAssignHandoverUser = doAssignHandoverUser;
        vm.handoverPopup = handoverPopup;

        // construction task popup function
        vm.openConsTaskPopup = openConsTaskPopup;
        vm.openCatProvincePopup = openCatProvincePopup;
        // vm.resetFormFieldProvince = resetFormFieldProvince;
        // vm.onSaveCatProvince = onSaveCatProvince;
        vm.openPerformerPopup = openPerformerPopup;

        // validate function
        vm.validateDateField = validateDateField;
        vm.validateDate = validateDate;
        vm.parseDateType = parseDateType;
        vm.assignSearch={};
        
        vm.columnHeight=false;
        vm.houseType = false;
        vm.groundingType = false;
        vm.listHaveWorkItemName=[];
        vm.listCheckAssignHandover = [];
        initFormData();
        function initFormData() {
            initConstructionStatus();
            initReceiStatus();
            initOutOfDateConstruction();
            initDateField();
        }

        //init default data for search form
        function initReceiStatus() {
            vm.receivedStatusDataList = [{
                "id": 0,
                "name": "Chưa giao"
            },{
                "id": 1,
                "name": "Chưa nhận"
            },{
                "id": 2,
                "name": "Đã nhận đủ điều kiện"
            },{
                "id": 3,
                "name": "Đã nhận có vướng"
            },{
                "id": 4,
                "name": "Đã nhận có vướng có vật tư"
            },{
                "id": 5,
                "name": "Đã nhận có vật tư may đo"
            },{
                "id": 6,
                "name": "Quá hạn"
            }]
        }

        function initConstructionStatus() {
            vm.constructionStatusDataList = [{
                "id": 1,
                "name": "Chưa thi công"
            },{
                "id": 2,
                "name": "Đang thi công"
            },{
                "id": 3,
                "name": "Thi công xong"
            }]
        }

        function initOutOfDateConstruction() {
            vm.outOfDateConstructionDataList = [{
                "id": 1,
                "name": "Quá hạn"
            },{
                "id": 0,
                "name": "Trong hạn"
            }]
        }

        function initDateField() {
            var from = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
            vm.searchForm.dateTo = htmlCommonService.formatDate(today);
            vm.searchForm.dateFrom = htmlCommonService.formatDate(from);
        }

        function canDoSearch() {
            if (!vm.disableSearch) {
                doSearch();
            }
        }

        function doSearch() {
            vm.readFileStation();
        }

        function callbackDoSearch() {
            vm.assignHandoverGridNV.dataSource.page(1);
        }

        vm.replaceVAlue=function(value){
			  if(value!=null){
				  value=value.toString();
				  if(value!=null&&value.includes(',')){
					  value= value.replaceAll(',',"");
				  } 
			  }
			  return value;
		  }

        // searchForm function
        // sysuser popup
        function openSysUserPopup() {
           var templateUrl = 'coms/popup/sysUserSearchPopUp.html';
           var title = gettextCatalog.getString("Tìm kiếm nhân viên");
           htmlCommonService.populatePopup(templateUrl, title, null, null, vm, null, 'string', false, '92%', '89%', 'sysUserSearchController');
        }

        function onSaveSysUser(data) {
            vm.searchForm.fullName = data.fullName;
            vm.searchForm.performentId = data.sysUserId;
            htmlCommonService.dismissPopup();
            $("#signerGroup").focus();
        }

        // file constructionCode
        function readFileStation() {
            if (!$("#fileSearchStation")[0].files[0]) {
                callbackDoSearch();
                return;
            }
            else if (($("#fileSearchStation")[0].files[0].name.split('.').pop() != 'xls' && $("#fileSearchStation")[0].files[0].name.split('.').pop() != 'xlsx')) {
                toastr.warning("Sai định dạng file");
                callbackDoSearch();
                return;
            }
            var formData = new FormData();
            formData.append('multipartFile', $('#fileSearchStation')[0].files[0]);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "assignHandoverService/readFileConstructionCode",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if (data.length === 0) {
                        toastr.warning("File import không có dữ liệu");
                        return;
                    } else {
                        if (data.constructionCodeList.length > 0 ) {
                            vm.searchForm.constructionCodeList = data.constructionCodeList;
                        }
                    }
                    callbackDoSearch();
                },
                error: function(data) {
                    if (!!data.error) {
                        toastr.error("File import phải có định dạng xlsx !");
                    }
                }
            });
        }

        function getStationCodeTemplate() {
            var fileName="construction_code";
            CommonService.downloadTemplate2(fileName).then(function(d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch( function(){
                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
                return;
            });
        }

// BEGIN MAIN FUNCTION
        function viewDetail(dataItem) {
        	var today = new Date();
            resetViewDetail();
            vm.detail = angular.copy(dataItem);
            if(dataItem.companyAssignDate!=null){
            	vm.detail.assignedSince = Math.floor((today - new Date(dataItem.companyAssignDate)) / (1000*60*60*24));
            } else {
            	vm.detail.assignedSince = null;
            }

            var templateUrl = "coms/assignHandover/assignHandoverNVDetail.html";
            var title = "Thông tin chi tiết nhận BGMB công trình \"" + dataItem.constructionCode + "\"";
            var windowId = "ASSIGN_HANDOVER_DETAIL";
            vm.detail.companyAssignDate = dataItem.companyAssignDate;
            assignHandoverService.getListImageHandover(dataItem.assignHandoverId).then(function (data) {
                if (!!data.imgList && data.imgList.length > 0) {
                    vm.detail.listImage = data.imgList;
                    vm.changeImage(vm.detail.listImage[0]);
                } else {
                    vm.detail.listImage = [];
                }
                var height = vm.detail.listImage.length > 0 ? '930' : '430';

                CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, true, '70%', height, null);
            }, function (error) {
                vm.detail.listImage = [];
            });
        }

        function parseDateType(dateData) {
            return dateData ? kendo.toString(kendo.parseDate(dateData), 'dd/MM/yyyy') : "";
        }

        function changeImage(image) {
            vm.imageSelected = image;
        }

        function removeImage(image, list){
            list.splice(list.indexOf(image), 1);
        }
        
        vm.downloadImage = downloadImage;
        function downloadImage() {
        	console.log(vm.imageSelected);
        	var link = document.createElement('a');
        	var uri = 'data:image/jpeg;base64,' + vm.imageSelected.base64String;
        	  if (typeof link.download === 'string') {
        	    link.href = uri;
        	    link.download = vm.imageSelected.name;

        	    //Firefox requires the link to be in the body
        	    document.body.appendChild(link);
        	    
        	    //simulate click
        	    link.click();

        	    //remove the link when done
        	    document.body.removeChild(link);
        	  } else {
        	    window.open(uri);
        	  }
        }

        //detail data
        vm.detail = {};

        //label name
        vm.detailLabel = {
            headerBGMB: "Thông tin nhận bàn giao mặt bằng:",
            headerImages: "Ảnh nhận BGMB:",
            companyAssignDate: "Ngày TTHT giao việc:",
            assignedSince: "Số ngày TTHT đã giao:",
            departmentAssignDate: "Ngày giao nhận BGMB:",
            performentId: "Người nhận BGMB:",
            receivedDate: "Ngày nhận đủ điều kiện:",
            receivedObstructDate: "Ngày nhận gặp vướng:",
            receivedObstructContent: "Nội dung vướng:",
            receivedGoodsDate: "Ngày nhận đặt vật tư may đo:",
            receivedGoodsContent: "Nội dung đặt vật tư:",
            columnHeight: "Độ cao cột (m):",
            houseTypeName: "Loại nhà:",
            numCo: "Số mố co (cái):",
            groundingTypeName: "Loại tiếp địa:",
            haveWorkItemName: "Hạng mục có sẵn:",
            isFence: "Thi công tường rào:",
            totalLength: "Tổng chiều dài tuyến:",
            hiddenImmediacy:"Chôn trực tiếp:",
            cableInTank:"Cáp trong cống bể xây:",
            cableInTankDrain:"Cáp trong cống bể có sẵn:",
            plantColumns:"Trồng cột:",
            availableColumns:"Cột có sẵn:"
        };
        
        function getAttachFile(dataItem) {
            var templateUrl = "coms/assignHandover/assignHandoverAttachNV.html";
            var title = "Chọn file Thiết kế";
            var windowId = "ATTACH_DESIGN_VIEW";
            vm.assignHandoverId = dataItem.assignHandoverId;
            CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, true, '1000', '200', null);
        }

        vm.attachFileEditGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            resizable: true,
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                        $timeout(function() {vm.countCons = response.total});
                        return response.total;
                    },
                    data: function (response) {
                        return response.data;
                    }
                },
                transport: {
                    read: {
                        url: Constant.BASE_SERVICE_URL + RestEndpoint.ASSIGN_HANDOVER_SERVICE_URL + "/getById",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        return JSON.stringify(vm.assignHandoverId);
                    }
                },
                pageSize: 1
            },
            noRecords: true,
            columnMenu: false,
            scrollable: false,
            editable: true,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            dataBound: function () {
                var GridDestination = $("#attachFileEditGridNV").data("kendoGrid");
                GridDestination.pager.element.hide();
            },
            columns: [
                {
                    title: "TT",
                    field: "stt",
                    template: dataItem => $("#attachFileEditGridNV").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
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
                        style: "text-align:left;"
                    }
                }
            ]
        });

        function downloadFile(dataItem) {
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
        }

        function handoverPopup() {
            cancel();
            handover(vm.detail);
        }

        function handover(dataItem) {
        	var obj={};
            if (!!dataItem) {
            	obj.catStationHouseCode = dataItem.catStationHouseCode;
            	obj.cntContractCode = dataItem.cntContractCode;
//            	assignHandoverService.checkStationContractBGMB(obj).then(function (d) {
//                    if(d.length>=1){
////                    	toastr.warning("Mã nhà trạm và hợp đồng đã được giao nhận BGMB cho công trình khác !");
//                    	toastr.warning("Mã nhà trạm và hợp đồng đã được giao nhận BGMB cho công trình khác !");
//                    	return;
//                    } else {
                    	vm.listChecked = [dataItem.assignHandoverId];
                        vm.listConstructionCodeChecked = [dataItem.constructionCode];
                        vm.sysGroupId = dataItem.sysGroupId;
//                    }
//                }, function (errResponse) {
//                        toastr.error("Lỗi lấy dữ liệu !");
//                });
                // vm.listChecked = [dataItem];
            } else if (vm.listChecked.length < 1) {
                toastr.error("Chọn ít nhất 1 bản ghi trước khi thực hiện giao việc");
                return;
            } 
            
            var templateUrl = 'coms/popup/sysUserSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm nhân viên");
            // htmlCommonService.populatePopup(templateUrl, title, null, vm.listConstructionCodeChecked.join(", "), vm, null, 'string', false, '92%', '89%', 'assignHandoverUserController');
            vm.chooseUserPopup = vm.openCustomSizePopup(templateUrl, title, vm.listConstructionCodeChecked, vm, null, 'assignHandoverUserNVController', false, '75%', '80%');
//            else if(vm.listChecked.length >= 1){
//            	obj.lstAssignId = vm.listCheckAssignHandover;
//            	assignHandoverService.checkStationBGMB(obj).then(function (d) {
//                    if(d.length>=1){
//                    	for(var i=0;i<d.length;i++){
//                    		toastr.warning("Mã nhà trạm và hợp đồng đã được giao nhận BGMB cho công trình khác !");
//                    		return;
//                    	}
//                    	doSearch();
//                    } else {
//                    	var templateUrl = 'coms/popup/sysUserSearchPopUp.html';
//                        var title = gettextCatalog.getString("Tìm kiếm nhân viên");
//                        // htmlCommonService.populatePopup(templateUrl, title, null, vm.listConstructionCodeChecked.join(", "), vm, null, 'string', false, '92%', '89%', 'assignHandoverUserController');
//                        vm.chooseUserPopup = vm.openCustomSizePopup(templateUrl, title, vm.listConstructionCodeChecked, vm, null, 'assignHandoverUserController', false, '75%', '80%');
//                    }
//                }, function (errResponse) {
//                        toastr.error("Lỗi lấy dữ liệu !");
//                });
//            }
        }

        vm.openCustomSizePopup = openCustomSizePopup;
        function openCustomSizePopup(templateUrl, gridTitle, data, caller, popupId,
                                     controllerName, isMultiSelect, width, height) {
            modalInstance = $kWindow.open({
                options : {
                    modal : true,
                    title : gridTitle,
                    visible : false,
                    width : width,
                    height : height,
                    actions : [ "Minimize", "Maximize",
                        "Close" ],
                    open : function() {
                        this.wrapper.children(
                            '.k-window-content')
                            .addClass("fix-footer");
                    },
                    close: function () {
                        vm.listChecked = [];
                        vm.listConstructionCodeChecked = [];
                        callbackDoSearch();
                    }
                },
                templateUrl : templateUrl,
                controller : controllerName,
                resolve : {
                    data : function() {
                        return data;
                    },
                    caller : function() {
                        return caller;
                    },
                    modalInstance : function() {
                        return this;
                    },
                    popupId : function() {
                        return popupId;
                    },
                    isMultiSelect : function() {
                        return isMultiSelect;
                    }
                }
            });

            modalInstance.result.then(function(result) {
                CommonService.dismissPopup();
            });
            return modalInstance;
        }

        function handleCheck(dataItem) {
            if (!!!vm.sysGroupId) {
                vm.sysGroupId = dataItem.sysGroupId;
            }
        	var index = vm.listChecked.indexOf(dataItem.assignHandoverId);
            if (index === -1) {
                // vm.listChecked.push(dataItem);
                vm.listChecked.push(dataItem.assignHandoverId);
                vm.listConstructionCodeChecked.push(dataItem.constructionCode);
//                if(vm.listCheckAssignHandover.length>1){
//                	for(var i = 0;i< vm.listCheckAssignHandover.length;i++){
//                		if(vm.listCheckAssignHandover[i].catStationHouseCode == dataItem.catStationHouseCode 
//                				&& vm.listCheckAssignHandover[i].cntContractCode == dataItem.cntContractCode){
//                			dataItem.checked=false;
//                			toastr.warning("Không được chọn 2 công trình cùng mã nhà trạm và hợp đồng");
//                			if(dataItem.assignHandoverId==vm.listCheckAssignHandover[i].assignHandoverId){
//                				vm.listChecked.splice(i, 1);
//                                vm.listConstructionCodeChecked.splice(i, 1);
//                                vm.listCheckAssignHandover.splice(i, 1);
//                			}
//                			return;
////                			doSearch();
//                		} 
////                		else {
//////                			dataItem.checked=true;
////                			vm.listCheckAssignHandover.push(dataItem);
////                		}
//                	}
//                } else {
                	vm.listCheckAssignHandover.push(dataItem);
//                }
                
            } else {
                vm.listChecked.splice(index, 1);
                vm.listConstructionCodeChecked.splice(index, 1);
                vm.listCheckAssignHandover.splice(index, 1);
//                doSearch();
            }
        }

        function doAssignHandoverUser(userData) {
            var dataObj = {
                assignHandoverIdList: vm.listChecked,
                performentId: userData.sysUserId,
                email: userData.email,
                phoneNumber: userData.phoneNumber
            };
            assignHandoverService.doAssignHandover(dataObj).then(
                function (data) {
                    vm.listChecked = [];
                    vm.listConstructionCodeChecked = [];
                    // if (data.error) {
                    //     toastr.error(data.error);
                    //     return;
                    // }
                    toastr.success("Giao việc thành công!");
                    callbackDoSearch();
                }, function (e) {
                    vm.listChecked = [];
                    vm.listConstructionCodeChecked = [];
                    toastr.error("Đã có lỗi xảy ra khi giao việc cho nhân viên");
                });
            cancel();
        }
// END MAIN FUNCTION

// construction task popup START
        vm.performerSearch = {};
        vm.consTask = {};
        vm.consTask.taskOrder = 1;
        vm.consTask.type = 1;

        vm.resetInputConsTask = resetInputConsTask;
        function resetInputConsTask() {
            vm.disableConsTaskSubmit = true;
            vm.consTask.catContructionTypeId = null;
            vm.consTask.catTaskCode = null;
            vm.consTask.catTaskId = null;
            vm.consTask.constructionCode = null;
            vm.consTask.constructionId = null;
            vm.consTask.constructionName = null;
            vm.consTask.catProvince = null;
            vm.consTask.taskName = null;
            vm.consTask.workItemCode = null;
            vm.consTask.workItemId = null;
            vm.consTask.workItemName = null;
            vm.consTask.performerName = null;
            vm.consTask.performerIdDetail = null;
        }

        function openConsTaskPopup(dataItem) {
        	cancel();
            resetInputConsTask();
            if (!!dataItem) {
                vm.consTask.constructionCode = dataItem.constructionCode;
                vm.consTask.assignHandoverId = dataItem.assignHandoverId;
                vm.performerSearch.sysGroupId = dataItem.sysGroupId;
                vm.consTask.receivedStatus = dataItem.receivedStatus;
            } else {
                vm.consTask.constructionCode = vm.detail.constructionCode;
                vm.consTask.assignHandoverId = vm.detail.assignHandoverId;
                vm.performerSearch.sysGroupId = vm.detail.sysGroupId;
                vm.consTask.receivedStatus = vm.detail.receivedStatus;
            }
            // data for performer search
            // vm.workItemSearch.constructionId = data.constructionId;
            // vm.workItemSearch.sysGroupId = data.sysGroupId;


            assignHandoverService.getConstructionProvinceByCode(vm.consTask.constructionCode).then(function (data) {
                // init consTask data
                if (!!data.construction) {
                    vm.consTask.catProvince = data.construction.catProvince;
                    vm.consTask.constructionName = data.construction.name;
                    vm.consTask.constructionCode = data.construction.code;
                    vm.consTask.constructionId = data.construction.constructionId;
                    vm.consTask.catContructionTypeId = data.construction.catContructionTypeId;
                    vm.consTask.workItemName = null;
                    vm.consTask.workItemCode = null;
                    vm.consTask.workItemId = null;
                    vm.consTask.taskName = null;
                    vm.consTask.catTaskCode = null;
                    vm.consTask.catTaskId = null;

                    var templateUrl = "coms/assignHandover/constructionTaskHandoverPopup.html";
                    var title = "Thêm mới công việc";
                    var windowId = "CONSTRUCTION_TASK_POPUP";
                    CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, true, '60%', '80%', null);
                } else {
                    toastr.error("Không tìm thấy công trình!");
                }
            }, function (err) {
                toastr.error("Đã có lỗi xảy ra");
            });
        }

        function openCatProvincePopup(){
            var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm tỉnh");
            htmlCommonService.populatePopup(templateUrl, title, null,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
        }

        function resetFormFieldSysUserTask() {
            vm.consTask.performerName = null;
            vm.consTask.performerId = null;
            vm.disableConsTaskSubmit = true;
        }

        vm.openTaskPopup = openTaskPopup;
        function openTaskPopup() {
            vm.keySearch = undefined;
            var data = '3';
            var title = "Danh sách người thực hiện";
            vm.labelSearch = "Nội dung tìm kiếm";
            vm.plSearch = "Tên đăng nhập/ Họ tên/ Email";
            var teamplateUrl = "coms/constructionTask/popupOnPopup.html";
            var windowId = "CONSTRUCTION_TASK";
            CommonService.populatePopupOnPopup(teamplateUrl, title, data, vm, windowId, true, '950', '450', null);
        }

        function openPerformerPopup() {
            vm.consTask.sysGroupId = null;
            var teamplateUrl = "coms/constructionTask/performerPopup.html";
            var title = "Danh sách người thực hiện";
            var windowId = "CONSTRUCTION_TASK";
            CommonService.populatePopupOnPopup(teamplateUrl, title, data, vm, windowId, true, '950', '450', null);
        }

        vm.doSearchPopup = doSearchPopup;
        function doSearchPopup() {
            var grid = vm.performerPopupGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        }

        vm.choosePerformer = choosePerformer;
        function choosePerformer(dataItem) {
            vm.consTask.performerName = dataItem.fullName;
            vm.consTask.performerIdDetail = dataItem.sysUserId;
            validateConsTaskAdd();
            CommonService.dismissPopup1();
        }

        vm.addWorkItem = addWorkItem;
        function addWorkItem() {
            vm.workItemRecord = {workItemTypeList: []};
            vm.workItemRecord.isInternal = 1;
            prepareDataforAssign(Constant.userInfo.vpsUserToken.sysUserId);
            vm.showHm = true;
            vm.showHm1 = false;
            vm.disabledHangMuc = false;
            var teamplateUrl = "coms/constructionTask/workItemPopupDetail.html";
            var title = "Thêm mới hạng mục";
            var windowId = "addWorkItem";
            CommonService.populatePopupPartner(teamplateUrl, title, null, vm, windowId, true, '600', '500', "isInternal1");
        }

        function prepareDataforAssign(id) {
            constructionTaskService.getSysGroupInfo(id).then(function (data) {
                vm.workItemRecord.constructorName1 = data.name;
                vm.workItemRecord.constructorId = data.groupId;
                vm.workItemRecord.supervisorName = data.name;
                vm.workItemRecord.supervisorId = data.groupId;
            });

            var wiSearch = {
                id: vm.consTask.constructionId,
                catConstructionTypeId: vm.consTask.catContructionTypeId
            };
            constructionTaskService.getCatWorkItemType(wiSearch).then(function (data) {
                vm.catWorkItemTypeList = data;
            }, function (e) {
                toastr.error("Có lỗi trong quá trình lấy dữ liệu.");
            });
            // use in workItemDetailFunction
            vm.constrObj = {};
            vm.constrObj.constructionId = vm.consTask.constructionId;
            vm.constrObj.code = vm.consTask.constructionCode;
        }

        // save data user from popup choose
        vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'deptWork') {
                vm.workItemRecord.constructorName1 = data.text;
                vm.workItemRecord.constructorId = data.id;
            } else if (vm.departmentpopUp === 'sup') {
                vm.workItemRecord.supervisorName = data.text;
                vm.workItemRecord.supervisorId = data.id;
            }
        }

        vm.disableConsTaskSubmit = true;
        vm.validateConsTaskAdd = validateConsTaskAdd;
        function validateConsTaskAdd() {
        	vm.disableConsTaskSubmit = !(vm.consTask.performerName != null && vm.consTask.performerIdDetail != null);
        }


        vm.saveConstructionTask = saveConstructionTask;
        function saveConstructionTask() {
            $('#fuckThisShit').addClass('loadersmall');
            vm.disableConsTaskSubmit = true;
            // validate selected work items
            if (!getListWorkItemfromGrid()) {
                $('#fuckThisShit').removeClass('loadersmall');
                return;
            }

            var obj = vm.consTask;
            return Restangular.all("constructionTaskService/construction/addConstructionTaskTC").post(obj).then(function (d) {
                $('#fuckThisShit').removeClass('loadersmall');
                if (d.error) {
                    toastr.error(d.error);
                    return;
                }
                toastr.success("Thêm công việc thành công");
                getWorkItemForAddingTask();
                vm.consTask.workItemName = null;
                vm.consTask.quantity = null;
                vm.consTask.performerName = null;
                vm.consTask.endDate = null;
                vm.consTask.startDate = null;

                vm.consTask.workItemId = null;
                vm.consTask.quantity = null;
                vm.consTask.taskName = null;
                vm.consTask.performerIdDetail = null;
                vm.consTask.taskName = null;
                // vm.cancel();

            }).catch(function (e) {
                $('#fuckThisShit').removeClass('loadersmall');
                toastr.error("Có lỗi xảy ra");
            });
        }

        function getWorkItemForAddingTask(){
            if(!!$('#workItemAddingGrid').data('kendoGrid')){
                $('#workItemAddingGrid').data('kendoGrid').dataSource.read();
                $('#workItemAddingGrid').data('kendoGrid').refresh();
            }
        }

        function getListWorkItemfromGrid(){
            vm.consTask.childDTOList = [];
            var listWorkItem = $("#workItemAddingGrid").data().kendoGrid.dataSource.data();
            vm.consTask.performerId = vm.consTask.performerIdDetail;
            var table = $("#workItemAddingGrid  table")[0];
            var isQuantityValid = true;
            var isStartDateValid = true;
            var isEndDateValid = true;
            var isPerformerValid = true;
            var gridPageSize = $("#workItemAddingGrid").data("kendoGrid").dataSource.pageSize();
            for(var i = 0; i<listWorkItem.length; i++){
                if(listWorkItem[i].selected == true){
                    var row = i+1;
                    var tableRow = row % gridPageSize;
                    if ((listWorkItem[i].quantity == undefined || listWorkItem[i].quantity == '')) {
                        $(table.rows[tableRow].cells[3]).addClass("k-state-focused");
                        $(table.rows[tableRow]).addClass("k-state-selected");
                        $(table.rows[tableRow]).val(null);
                        isQuantityValid = false;
                    }

                    if ((!listWorkItem[i].startDate)) {
                        $(table.rows[tableRow].cells[4]).addClass("k-state-focused");
                        $(table.rows[tableRow]).addClass("k-state-selected");
                        isStartDateValid = false;
                    }
                    if ((!listWorkItem[i].endDate)) {
                        $(table.rows[tableRow].cells[5]).addClass("k-state-focused");
                        $(table.rows[tableRow]).addClass("k-state-selected");
                        isEndDateValid = false;
                    }
                    listWorkItem[i].taskOrder = vm.consTask.taskOrder;
                    listWorkItem[i].constructionId = vm.consTask.constructionId;
                    listWorkItem[i].constructionName = vm.consTask.constructionName;
                    listWorkItem[i].constructionCode = vm.consTask.constructionCode;
                    listWorkItem[i].workItemName = listWorkItem[i].name;
                    listWorkItem[i].type = vm.consTask.type;
                    listWorkItem[i].performerId = vm.consTask.performerIdDetail;
                    listWorkItem[i].performerIdDetail = vm.consTask.performerIdDetail;
                    // update constructor id
                    // listWorkItem[i].constructorId = listWorkItem[i].constructorId;
                    listWorkItem[i].endDate =kendo.toString(kendo.parseDate(listWorkItem[i].endDate,'dd/MM/yyyy'), 'dd/MM/yyyy');
                    listWorkItem[i].startDate =kendo.toString(kendo.parseDate(listWorkItem[i].startDate,'dd/MM/yyyy'), 'dd/MM/yyyy')
                    vm.consTask.childDTOList.push(listWorkItem[i]);
                }
            }
            if (!isStartDateValid)
                toastr.warning("Thời gian từ ngày không được để trống!");
            if (!isEndDateValid)
                toastr.warning("Thời gian từ ngày không được để trống!");
            if (!isPerformerValid)
                toastr.warning("Người thực hiện không được để trống!");
            if (!isQuantityValid)
                toastr.warning("Sản lượng không được để trống!");
            if (!!!vm.consTask.childDTOList || vm.consTask.childDTOList.length === 0) {
                toastr.warning("Chưa có hạng mục nào được chọn!");
                validateConsTaskAdd();
                return false;
            }
            return isPerformerValid && isStartDateValid && isEndDateValid && isPerformerValid;
        }

        vm.doSearchWorkItem = doSearchWorkItem;
        function doSearchWorkItem() {
            var grid = vm.workItemGrid;
            if (grid) {
                grid.dataSource.query({
                    page: grid.dataSource.page(),
                    pageSize: 10
                });
            }
        }

        vm.assignCallback = function () {
            $('div[data-role="draggable"]').each(function (index, element) {
                var index = $(element).css('z-index');
                if (index == 10005) {
                    $(element).find(".k-icon.k-i-close").click();
                    // $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
                }
            });
            getWorkItemForAddingTask();
        };

        vm.closePopupOnPopup = function () {
            CommonService.dismissPopup1();
        }

        //file chua function phuc vu popup gan hang muc
        workItemDetailFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
            kendoConfig, $kWindow, constructionTaskService,
            CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm);

        // grid opts user add task
        vm.performerOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                        return response.total;
                    },
                    data: function (response) {
                        return response.data;
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "constructionService/doSearchPerformer",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.performerSearch.page = options.page;
                        vm.performerSearch.pageSize = options.pageSize;
                        vm.performerSearch.keySearch = vm.keySearch;
                        return JSON.stringify(vm.performerSearch)

                    }
                },
                pageSize: 10
            },
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            pageable: {
                refresh: false,
                pageSize: 5,
                pageSizes: [5, 10, 15, 20, 25],
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
                    width: '10%',
                    columnMenu: false,
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    }

                },
                {
                    title: "Tên đăng nhập",
                    field: 'loginName',
                    width: '15%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }

                },
                {
                    title: "Mã nhân viên",
                    field: 'employeeCode',
                    width: '15%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }

                },
                {
                    title: "Họ tên",
                    field: 'fullName',
                    width: '20%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Email",
                    field: 'email',
                    width: '15%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:right;"
                    }
                },
                {
                    title: "Số điện thoại",
                    field: 'phoneNumber',
                    width: '15%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:right;"
                    }
                },
                {
                    title: "Chọn",
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    template: dataItem =>
                        '<div class="text-center">' +
                        '<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate >' +
                        '<i id="#=code#"  ng-click="caller.choosePerformer(dataItem)"  class="fa fa-check color-green" aria-hidden="true"></i>' +
                        '</a>'
                        + '</div>',
                    width: '10%'
                }
            ]
        });

        // gridopts work items
        vm.gridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            reorderable: true,
            dataSource: {
                autoSync: true,
                serverPaging: false,
                schema: {
                    type: "json",
                    model: {
                        fields: {
                            stt: {editable: false},
                            name: {editable: false},
                            partnerName: {editable: false},
                            quantity: {type: "number", format: "{0:c}", editable: true},
                            startDate: {editable: true},
                            endDate: {editable: true},
                        }
                    },
                    total: function (response) {
                        $("#cntCount").text("" + response.total);
                        return response.total;
                    },
                    data: function (response) {
                        for (var x in response.data) {
                            response.data[x].sysUserId = Constant.userInfo.vpsUserToken.sysUserId
                        }
                        return response.data;
                    },
                },
                transport: {
                    read: {
                        url: Constant.BASE_SERVICE_URL + "constructionTaskService/getWorkItemForAddingTask",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.workItemSearch.constructionId = vm.consTask.constructionId;
                        vm.workItemSearch.month = 1 + today.getMonth();
                        vm.workItemSearch.year = today.getFullYear();
                        return JSON.stringify(vm.workItemSearch)
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
                    itemsPerPage: "<span translate>kết quả/trang</span>",
                    empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                }
            },
            columns: [{
                title: "Chọn",
                field: "stt",
                width: '5%',
                template: "<input type='checkbox' id='checkWorkItem' name='gridcheckbox' ng-click='caller.handleCheckForWorkItem(dataItem)' ng-model='dataItem.selected'/>",
                columnMenu: false,
                headerAttributes: {style: "text-align:center;"},
                attributes: {style: "text-align:center;"}
            }, {
                title: "Hạng mục",
                field: 'name',
                width: '25%',
                headerAttributes: {
                    style: "text-align:center; font-weight: bold;",
                    translate: ""
                },
                attributes: {
                    style: "text-align:left;",
                    readonly: "readonly"
                }
            }, {
                title: "Đối tác",
                field: 'partnerName',
                width: '30%',
                headerAttributes: {
                    style: "text-align:center; font-weight: bold;",
                    translate: ""
                },
                attributes: {
                    style: "text-align:left;",
                    readonly: "readonly"
                },
                template: function (dataItem) {
                    return '<div style="border:none;background:transparent;">' +
                                '<div class="pull-right directive">' +
                                    '<i class="fa fa-search directive" ng-click="caller.openPartnerPopup(dataItem)"></i>' +
//                                    '<i class="fa fa-remove directive col-md-offset-2" ng-click="caller.removePartner(dataItem)"></i>' +
                                '</div>' +
                                '<span ng-bind="dataItem.partnerName" style="white-space: normal;" translate></span>' +
                            '</div>';
                }
            }, {
                title: "Sản lượng <br>(Triệu VND)",
                field: 'quantity',
                width: '10%',
                headerAttributes: {
                    style: "text-align:center; font-weight: bold;"
                },
                attributes: {style: "text-align:left;"},
                format: "{0:n2}",
                editor: function(container, options) {
                	var input = $("<input id='qtt' ng-blur ='caller.validateWorkItemQtt(dataItem)'/>");
                    input.attr("name", options.field);
                    input.attr("type", "number");
                    input.attr("style", "width:100%");
                    input.appendTo(container);
                }
            }, {
                title: "Từ ngày",
                field: 'startDate',
                width: '15%',
                headerAttributes: {
                    style: "text-align:center; font-weight: bold;",
                },
                attributes: {style: "text-align:left;"},
                editor: function (container, options) {
                    var input = $("<input id = 'kendoDate' ng-field = 'startDate' ng-blur ='caller.validateDateCell(dataItem)'/>");
                    input.attr("name", options.field);
                    input.appendTo(container);
                    input.kendoDatePicker({format:"dd/MM/yyyy"});
                },
                template: dataItem =>!!dataItem.startDate ? kendo.toString(kendo.parseDate(dataItem.startDate), 'dd/MM/yyyy') : ""
            }, {
                title: "Đến ngày",
                field: 'endDate',
                width: '15%',
                headerAttributes: {
                    style: "text-align:center; font-weight: bold;",
                },
                attributes: {style: "text-align:center;"},
                editor: function (container, options) {
                    var input = $("<input id = 'kendoDate' ng-field = 'endDate' ng-blur ='caller.validateDateCell(dataItem)'/>");
                    input.attr("name", options.field);
                    input.appendTo(container);
                    input.kendoDatePicker({format:"dd/MM/yyyy"});
                },
                template: dataItem =>!!dataItem.endDate ? kendo.toString(kendo.parseDate(dataItem.endDate), 'dd/MM/yyyy') : ""
            }]
        });

        // assign popup update partner
        vm.partnerSearchAssign = {};
        vm.partnerSearchAssignGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            vm.countCons = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            url: Constant.BASE_SERVICE_URL + "departmentRsService/catPartner/" + "doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            // vm.constructionSearch.employeeId =
                            // Constant.user.srvUser.catEmployeeId;
                            vm.partnerSearchAssign.page = options.page;
                            vm.partnerSearchAssign.pageSize = options.pageSize;

                            return JSON.stringify(vm.partnerSearchAssign)

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
                        width: '10%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }

                    },
                    {
                        title: "Loại đối tác",
                        field: 'partnerType',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }, template: function (dataItem) {
                            if (dataItem.partnerType == 1) {
                                return "<span name='status' font-weight: bold;'>Đối tác ngoài Viettel</span>"
                            } else if (dataItem.partnerType == 0) {
                                return "<span name='status' font-weight: bold;'>Đối tác trong Viettel</span>"
                            }
                        }

                    },
                    {
                        title: "Mã đối tác",
                        field: 'code',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }

                    },
                    {
                        title: "Tên đối tác",
                        field: 'text',
                        width: '30%',
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
                        },
                        template: function (dataItem) {
                            if (dataItem.status == 1) {
                                return "<span name='status' font-weight: bold;'>Hiệu lực</span>"
                            } else if (dataItem.status == 0) {
                                return "<span name='status' font-weight: bold;'>Hết hiệu lực</span>"
                            }
                        }
                    },
                    {
                        title: "Chọn",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        template:
                            '<div class="text-center "> '	+
                            '		<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate>'+
                            '			<i id="#=code#" ng-click=caller.onSavePartner(dataItem) class="fa fa-check color-green" aria-hidden="true"></i> '+
                            '		</a>'
                            +'</div>',
                        width: "10%"
                    }
                ]
            });
        vm.partnerSearchAssignGrid = {};

        vm.doSearchPartnerPopup = function () {
            vm.partnerSearchAssignGrid.dataSource.page(1);
        };

        vm.closePartnerPopup = function () {
            CommonService.dismissPopup1();
        };

        vm.openPartnerPopup = function (dataItem) {
            vm.workItemRow = dataItem;
            var templateUrl = 'coms/assignHandover/findPartnerPopup.html';
            var title = gettextCatalog.getString("Tìm kiếm đối tác");
            CommonService.populatePopupCreate(templateUrl, title, null, vm, "partnerUpdate", true, '800', '600', null);
        };

        vm.onSavePartner = function (dataReturn) {
            vm.workItemRow.partnerName = dataReturn.name;
            vm.workItemRow.constructorId = dataReturn.id;
            Restangular.all("assignHandoverService" + "/updateWorkItemPartner").post(vm.workItemRow).then(function(response) {
                if (!!response.error) {
                    vm.workItemRow.partnerName = null;
                    vm.workItemRow.constructorId = null;
                    toastr.error("Đã có lỗi xảy ra!");
                    CommonService.dismissPopup1();
                } else {
                    toastr.success("Cập nhật đối tác thành công!");
                    CommonService.dismissPopup1();
                }
            });
        };

        vm.validateWorkItemQtt = function (dataItem) {
            var quantity = dataItem.quantity;
            if (quantity <= 0) {
                dataItem.quantity = null;
                toastr.error("Sản lượng phải lớn hơn 0");
                return false;
            }
            return true;
        };

        vm.validateDateCell = function (obj) {
            var attr = $("#kendoDate").attr('ng-field');
            var attrName = attr == 'startDate' ? 'Ngày bắt đầu' : 'Ngày kết thúc';
            var date = kendo.parseDate(obj[attr]);
            if (!$("#kendoDate").val()) {
                obj[attr] = "";
                return;
            }
            if (!date) {
                $("#kendoDate").focus();
                $("#kendoDate").val('');
                obj[attr] = '';
                toastr.error(attrName + " không hợp lệ!");
                vm.disableConsTaskSubmit = true;
                return false;
            }
            if (!!obj.startDate && !!obj.endDate) {
                if (obj.startDate.getFullYear() !== obj.endDate.getFullYear() || obj.startDate.getMonth() !== obj.endDate.getMonth()) {
                    $("#kendoDate").focus();
                    obj[attr] = null;
                    toastr.error("Ngày bắt đầu và kết thúc phải trong cùng 1 tháng");
                    vm.disableConsTaskSubmit = true;
                    return false;
                } else if (obj.startDate > obj.endDate) {
                    $("#kendoDate").focus();
                    obj[attr] = null;
                    toastr.error("Ngày bắt đầu không được lớn hơn ngày kết thúc!");
                    vm.disableConsTaskSubmit = true;
                    return false;
                }
            }
            validateConsTaskAdd();
            return true;
        };

// construction task popup END
        // validate
        function validateDateField(key) {
            var date;
            var element = $('#' + key).val();
            switch (key) {
                case 'dateFrom':
                    date = kendo.parseDate(element, "dd/MM/yyyy");
                    vm.dateFromErr = validateDate(date, element);
                    break;
                case 'dateTo':
                    date = kendo.parseDate(element, "dd/MM/yyyy");
                    vm.dateToErr = validateDate(date, element);
                    break;
                case 'dateDeptFrom':
                    date = kendo.parseDate(element, "dd/MM/yyyy");
                    vm.dateDeptFromErr = validateDate(date, element);
                    break;
                case 'dateDeptTo':
                    date = kendo.parseDate(element, "dd/MM/yyyy");
                    vm.dateDeptToErr = validateDate(date, element);
                    break;
                default:
                    vm.disableSearch = false;
                    break;
            }
        }

        function validateDate(date, element) {
            if (!!!element) {
                vm.disableSearch = false;
                return null;
            }
            if (date == null || date.getFullYear() > 9999 || date.getFullYear() < 1000) {
                vm.disableSearch = true;
                return 'Ngày không hợp lệ';
            } else {
                vm.disableSearch = false;
                return null;
            }
        }

        // close popup
        vm.cancel = cancel;
        function cancel() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

        // export excel
        vm.exportFile = exportFile;
        /*
        function exportFile() {
			vm.searchForm.page = null;
			vm.searchForm.pageSize = null;
			var data = vm.searchForm;
			var listRemove = [{
				title: "Thao tác"
			},{
				title: ""
			}];
			var listConvert = [{
				field: "isDesign",
				data: {
					1: 'Có thiết kế',
					0: 'Không có thiết kế',
					null: 'Không có thiết kế'
				}
			}, {
                field: "constructionStatus",
                data: {
                    1: 'Chưa thi công',
                    2: 'Đang thi công',
                    3: 'Thi công xong'
                }
            }];

			Restangular.all("assignHandoverService" + "/doSearchNV").post(data).then(function(d) {
			    var data = d.data;
                for (var i = 0; i < data.length; i++) {
                    data[i].companyAssignDate = kendo.toString(kendo.parseDate(data[i].companyAssignDate), 'dd/MM/yyyy');
                    data[i].departmentAssignDate = kendo.toString(kendo.parseDate(data[i].departmentAssignDate), 'dd/MM/yyyy');
                    data[i].receivedObstructDate = kendo.toString(kendo.parseDate(data[i].receivedObstructDate), 'dd/MM/yyyy');
                    data[i].receivedGoodsDate = kendo.toString(kendo.parseDate(data[i].receivedGoodsDate), 'dd/MM/yyyy');
                    data[i].receivedDate = kendo.toString(kendo.parseDate(data[i].receivedDate), 'dd/MM/yyyy');
                    data[i].deliveryConstructionDate = kendo.toString(kendo.parseDate(data[i].deliveryConstructionDate), 'dd/MM/yyyy');
                    data[i].startingDate = kendo.toString(kendo.parseDate(data[i].startingDate), 'dd/MM/yyyy');
                }
				CommonService.exportFile(vm.assignHandoverGridNV, data, listRemove, listConvert,"Danh sách Giao việc cho nhân viên");
			});
        }
        */
        function exportFile() {
            var element = $(".tab-content");
            kendo.ui.progress(element, true);

            vm.searchForm.page = null;
            vm.searchForm.pageSize = null;
            return assignHandoverService.exportHandoverNV(vm.searchForm).then(function (response) {
                kendo.ui.progress(element, false);
                if (!!response.error) {
                    toastr.error(response.error);
                    return;
                }
                var data = response.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function (e) {
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                kendo.ui.progress(element, true);
            });
        }

        // reuse func
        function showHideColumn(column) {
            if (angular.isUndefined(column.hidden)) {
                vm.assignHandoverGridNV.hideColumn(column);
            } else if (column.hidden) {
                vm.assignHandoverGridNV.showColumn(column);
            } else {
                vm.assignHandoverGridNV.hideColumn(column);
            }
        }

        function gridColumnShowHideFilter(item) {
            return item.type == null || item.type !== 1;
        }

        // reset input
        function resetFormFieldReceiveStatus() {
            vm.searchForm.receivedStatusList = null;
        }

        function resetFormFieldConstructionStatus() {
            vm.searchForm.constructionStatusList = null;
        }

        function resetFormFieldDate() {
            vm.searchForm.dateFrom = null;
            vm.searchForm.dateTo = null;
            vm.disableSearch = false;
            vm.dateFromErr = null;
            vm.dateToErr = null;
        }

        function resetFormFieldDeptDate() {
            vm.searchForm.dateDeptFrom = null;
            vm.searchForm.dateDeptTo = null;
            vm.disableSearch = false;
            vm.dateDeptFromErr = null;
            vm.dateDeptToErr = null;
        }

        function resetFormFieldPerforment() {
            vm.searchForm.fullName = null;
            vm.searchForm.performentId = null;
        }

        function resetFormFile() {
            $("#fileSearchStation").val(null);
            vm.searchForm.constructionCodeList = null;
        }

        function resetFormFieldOutOfDateConstruction() {
            vm.searchForm.outOfDateConstruction = null;
        }

        function resetViewDetail() {
            vm.detail.companyAssignDate = null;
            vm.detail.dateAssigned = null;
            vm.detail.departmentAssignDate = null;
            vm.detail.performentId = null;
            vm.detail.receiveDate = null;
            vm.detail.receiveObstructDate = null;
            vm.detail.receiveObstructContent = null;
            vm.detail.receiveGoodsDate = null;
            vm.detail.receiveGoodsContent = null;
            vm.detail.columnHeight = null;
            vm.detail.houseTypeName = null;
            vm.detail.numCo = null;
            vm.detail.groundingTypeName = null;
            vm.detail.haveWorkItemName = null;
            vm.detail.isFence = null;
            vm.detail.receivedStatus = null;
            vm.imageSelected = null;
        }
        
        function resetFormFieldOutOfDateStartDate() {
        	vm.searchForm.outOfDateStartDate = null;
        }


        // --- gridOpts
        // gridOps autofill sysuser
        vm.sysUserOptions = {
            dataTextField: "fullName",
            placeholder:"Nhập mã hoặc tên người thực hiện",
            open: function (e) {
                vm.isSelect = false;
            },
            select: function (e) {
                vm.isSelect = true;
                var data = this.dataItem(e.item.index());
                vm.searchForm.performentId = data.sysUserId;
                vm.searchForm.fullName = data.fullName;
            },
            dataSource: {
                serverFiltering: true,
                schema: {
                    total: function (response) {
                        return response.total;
                    },
                    data: function (response) {
                        return response.data;
                    }
                },
                transport: {
                    read: function (options) {
                        vm.isSelect = false;
                        return Restangular.all("assignHandoverService/getForSysUserAutoComplete").post({
                            fullName: vm.searchForm.fullName,
                            pageSize: vm.sysUserOptions.pageSize,
                            page: 1
                        }).then(function (response) {
                            options.success(response);
                            if (response.data === [] || vm.searchForm.fullName === "") {
                                vm.searchForm.performentId = null;
                                vm.searchForm.fullName = null;
                            }
                        }).catch(function (err) {
                            vm.searchForm.performentId = null;
                            vm.searchForm.fullName = null;
                        });
                    }
                }
            },
            pageSize: 10,
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-6 text-header-auto border-right-ccc">Mã nhân viên</p>' +
                '<p class="col-md-6 text-header-auto">Họ tên</p>' +
                '</div>',
            template: '<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.employeeCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
            change: function (e) {
                if (!vm.isSelect) {
                    vm.searchForm.performentId = null;
                    vm.searchForm.fullName = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.searchForm.performentId = null;
                    vm.searchForm.fullName = null;
                }
            }
        };

        // gridOpts user in task popup
        vm.isSelectUser = false;
        vm.sysUserTaskOptions = {
            dataTextField: "fullName",
            placeholder:"Nhập mã hoặc tên người thực hiện",
            open: function (e) {
                vm.isSelectUser = false;
            },
            select: function (e) {
                vm.isSelectUser = true;
                var data = this.dataItem(e.item.index());
                vm.consTask.performerName = data.fullName;
                vm.consTask.performerIdDetail = data.sysUserId;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.isSelectUser = false;
                        return Restangular.all("constructionTaskService/construction/searchPerformer").post({
                            keySearch: vm.consTask.performerName,
                            pageSize: vm.sysUserTaskOptions.pageSize,
                            sysGroupId: vm.performerSearch.sysGroupId,
                            page: 1
                        }).then(function (response) {
                            options.success(response);
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
            template: '<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.employeeCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
            change: function (e) {
                if (!vm.isSelectUser) {
                    vm.consTask.performerIdDetail = null;
                    vm.consTask.performerName = null;
                }
            },
            close: function (e) {
                if (!vm.isSelectUser) {
                    vm.consTask.performerIdDetail = null;
                    vm.consTask.performerName = null;
                    // vm.consTask.performentId = null;
                    // vm.consTask.fullName = null;
                }
            }
        };

        // main grid
        var record = 0;
        vm.assignHandoverGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: true,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            reorderable: true,
            toolbar: [
                {
                    name: "actions",
                    template: 
                        '<button class="btn btn-qlk padding-search-right btnHandover ng-scope" ng-click="vm.handover()" uib-tooltip="Giao nhận BGMB" style="width:160px;height: 30px;" translate="">Giao nhận BGMB</button>'
                    	+'<button class="btn btn-qlk padding-search-right TkQLK " '+
                        'ng-click="vm.importAssignHandoverBGMB()" uib-tooltip="Import dữ liệu giao việc" translate>Import</button>'
                        +
                        '<div class="btn-group pull-right margin_top_button margin_right10">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()" aria-hidden="true"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.assignHandoverGridNV.columns| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                }
            ],
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                        $timeout(function() {vm.count = response.total});
                        return response.total;
                    },
                    data: function (response) {
                        var data = response.data;
                        // count field exist
                        vm.countReceive = [0, 0, 0];
                        for (var i = 0; i < data.length; i++) {
                            if (!!data[i].receivedObstructDate) {
                                vm.countReceive[0]++;
                            }
                            if (!!data[i].receivedGoodsDate) {
                                vm.countReceive[1]++;
                            }
                            if (!!data[i].receivedDate) {
                                vm.countReceive[2]++;
                            }

                            for (var j = 0; j < vm.listChecked.length; j++) {
                                if (data[i].assignHandoverId === vm.listChecked[j]) {
                                	data[i].checked = true;
                                }
                            }
                        }

                        return response.data;
                    }
                },
                transport: {
                    read: {
                        url: Constant.BASE_SERVICE_URL + RestEndpoint.ASSIGN_HANDOVER_SERVICE_URL + "/doSearchNV",
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
            dataBound: function (data) {
                var grid = $("#assignHandoverGridNV").data("kendoGrid");
                var gridData = $("#assignHandoverGridNV").data("kendoGrid").dataSource.data();
                var row;
                for (var i = 0; i < gridData.length; i++) {
                    if (!!gridData[i].outOfDateStartDate && gridData[i].outOfDateStartDate > 0) {
                        row = grid.table.find("tr[data-uid='" + data._data[i].uid + "']").addClass("currenRow"+i);
                        row.css({"background":"#FFFF66"});
                    }
                }
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
            //TODO check field names, resize
            columns: [
                {
                    title: "TT",
                    field: "stt",
                    hidden: true
                },
                {
                    title: "",
                    field: "",
                    width: '50px',
                    columnMenu: false,
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        // TODO comment for dev
                        if (dataItem.receivedStatus===1) {
                            return "<input type='checkbox' id='childcheck' name='gridcheckbox' ng-model='dataItem.checked' ng-click='vm.handleCheck(dataItem)'/>";
                        } else {
                            return "<span></span>";
                        }
                    },
                    footerTemplate: function() {
                        return '<b>Tổng:<b>';
                    }
                },
                {
                    title: "Thao tác",
                    width: '180px',
                    field: "action",
                    template: dataItem =>
                '<div class="text-center">'
                
                + '<button  style=" border: none; background-color: white;" id="updateId" ng-click="vm.editDetail(dataItem)" ' +
                'class="icon_table aria-hidden pull-left" ng-show="dataItem.receivedStatus>1"' +
                'uib-tooltip="Sửa" translate>' +
                '<i class="fa fa-pencil" style="color:orange;padding: 5px 5px 5px 5px;"  ng-disabled="dataItem.status===0"   aria-hidden="true"></i>' +
                '</button>'
                
                + '<button  style=" border: none; background-color: white;" id="updateId" ng-click="vm.viewDetail(dataItem)" ' +
                'class="icon_table aria-hidden pull-left"' +
                'uib-tooltip="Xem chi tiết" translate>' +
                '<i class="fa fa-list-alt" style="color:#e0d014"  ng-disabled="dataItem.status===0"   aria-hidden="true"></i>' +
                '</button>'

                + '<button style=" border: none; background-color: white;" id="updateId" ng-click="vm.handover(dataItem)" ' +
                'ng-hide="dataItem.status===0 || dataItem.receivedStatus!=1" ' +
                'class="icon_table aria-hidden pull-left" ' +
                'uib-tooltip="Giao nhận BGMB" translate>' +
                // '<i class="fa fa-check" style="color:#00ff00"  ng-hide="dataItem.status===0"   aria-hidden="true"></i>' +
                //TODO comment for dev
                '<i class="fa fa-check-circle pull-right" style="color:#68b354" aria-hidden="true"></i>' +
                '</button>'

                + '<button style=" border: none; background-color: white; margin: 0px 0px 0px 6px;" ' +
                'ng-hide="dataItem.status===0 || (dataItem.receivedStatus!=2 && dataItem.receivedStatus!=3) "' +
                'class="#=appParamId# icon_table aria-hidden pull-left"  ng-hide="dataItem.status==0"  ng-click="vm.openConsTaskPopup(dataItem)" uib-tooltip="Giao thi công" translate>' +
                '<i class="fa fa-building" style="color: #337ab7;" aria-hidden="true"></i>' +
                '</button>'
                + '</div>'
                },
                {
                    title: "Ngày HT giao",
                    field: "companyAssignDate",
                    width: '90px',
                    columnMenu: false,
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        return !!dataItem.companyAssignDate ? kendo.toString(kendo.parseDate(dataItem.companyAssignDate), 'dd/MM/yyyy') : "";
                    }
                },
                {
                    title: "Mã công trình",
                    field: 'constructionCode',
                    width: '180px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Thiết kế",
                    field: 'isDesign',
                    width: '70px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        if (dataItem.isDesign === 1) {
                            return "<a href='' ng-click='vm.getAttachFile(dataItem)'>Có</a>";
                            // return "<span name='isDesign' font-weight: bold;'>Có</span>"
                        } else {
                            return "<span name='isDesign' font-weight: bold;'>Không</span>"
                        }
                    }
                },
                {
                    title: "Người nhận",
                    field: 'fullName',
                    width: '140px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Ngày giao nhận BGMB",
                    field: 'departmentAssignDate',
                    width: '90px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        return !!dataItem.departmentAssignDate ? kendo.toString(kendo.parseDate(dataItem.departmentAssignDate), 'dd/MM/yyyy') : "";
                    }
                },
                {
                    title: "Quá hạn nhận BGMB",
                    field: 'outOfDateReceived',
                    width: '70px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                {
                    title: "Đã nhận vướng",
                    field: 'receivedObstructDate',
                    width: '90px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                    footerTemplate: function() {
                        return vm.countReceive[0];
                    },
                    template: function (dataItem) {
                        return !!dataItem.receivedObstructDate ? kendo.toString(kendo.parseDate(dataItem.receivedObstructDate), 'dd/MM/yyyy') : "";
                    }
                },
                {
                    title: "Đã nhận có vật tư may đo",
                    field: 'receivedGoodsDate',
                    width: '90px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                    footerTemplate: function() {
                        return vm.countReceive[1];
                    },
                    template: function (dataItem) {
                        return !!dataItem.receivedGoodsDate ? kendo.toString(kendo.parseDate(dataItem.receivedGoodsDate), 'dd/MM/yyyy') : "";
                    }
                },
                {
                    title: "Đã nhận đủ ĐK",
                    field: 'receivedDate',
                    width: '90px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                    footerTemplate: function() {
                        return vm.countReceive[2];
                    },
                    template: function (dataItem) {
                        return !!dataItem.receivedDate ? kendo.toString(kendo.parseDate(dataItem.receivedDate), 'dd/MM/yyyy') : "";
                    }
                },{
                    title: "Ngày giao thi công",
                    field: 'deliveryConstructionDate',
                    width: '90px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        return !!dataItem.deliveryConstructionDate ? kendo.toString(kendo.parseDate(dataItem.deliveryConstructionDate), 'dd/MM/yyyy') : "";
                    }
                },{
                    title: "Thi công trực tiếp",
                    field: 'performentConstructionName',
                    width: '140px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    }
                },{
                    title: "Người giám sát",
                    field: 'supervisorConstructionName',
                    width: '140px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    }
                },{
                    title: "Ngày khởi công",
                    field: 'startingDate',
                    width: '90px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        return !!dataItem.startingDate ? kendo.toString(kendo.parseDate(dataItem.startingDate), 'dd/MM/yyyy') : "";
                    }
                },{
                    title: "Quá hạn khởi công",
                    field: 'outOfDateStartDate',
                    width: '90px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    }
                },{
                    title: "Thi công",
                    field: 'constructionStatus',
                    width: '90px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        var status = "";
                        switch (dataItem.constructionStatus) {
                            case 1:
                                status = 'Chưa thi công';
                                break;
                            case 2:
                                status = 'Đang thi công';
                                break;
                            case 3:
                                status = 'Thi công xong';
                                break;
                        }
                        return "<span name='sp-constructionStatus'>" + status + "</span>"
                    }
                },{
                    title: "Mã nhà trạm",
                    field: 'catStationHouseCode',
                    width: '90px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                {
                    title: "Hợp đồng đầu ra",
                    field: 'cntContractCode',
                    width: '120px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                {
                    title: "Đối tác",
                    field: 'partnerName',
                    width: '300px',
                    headerAttributes: {
                        style: "text-align:center;white-space: normal;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    }
                }
            ]}
        );
        
        vm.houseTypeOptions = {
                dataTextField: "houseTypeName",
                open: function (e) {
                    vm.isSelect = false;
                },
                select: function (e) {
                    vm.isSelect = true;
                    var data = this.dataItem(e.item.index());
                    vm.assignSearch.houseTypeName = data.houseTypeName;
                    vm.assignSearch.houseTypeId = data.houseTypeId;
                },
                dataSource: {
                    serverFiltering: true,
                    schema: {
                        total: function (response) {
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: function (options) {
                            vm.isSelect = false;
                            return Restangular.all("assignHandoverService/getListHouseType").post({
                            	houseTypeName: vm.assignSearch.houseTypeName,
                                pageSize: vm.houseTypeOptions.pageSize,
                                page: 1
                            }).then(function (response) {
                                options.success(response);
                                if (response.plain() === [] || vm.assignSearch.houseTypeName === "") {
                                	vm.assignSearch.houseTypeName = null;
                                    vm.assignSearch.houseTypeId = null;
                                }
                            }).catch(function (err) {
                            	vm.assignSearch.houseTypeName = null;
                                vm.assignSearch.houseTypeId = null;
                            });
                        }
                    }
                },
                pageSize: 10,
//                headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
//                    '<p class="col-md-6 text-header-auto border-right-ccc">Mã nhân viên</p>' +
//                    '<p class="col-md-6 text-header-auto">Họ tên</p>' +
//                    '</div>',
                template: '<div class="row" ><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.houseTypeName #</div> </div>',
                change: function (e) {
                    if (!vm.isSelect) {
                    	vm.assignSearch.houseTypeName = null;
                        vm.assignSearch.houseTypeId = null;
                    }
                },
                close: function (e) {
                    if (!vm.isSelect) {
                    	vm.assignSearch.houseTypeName = null;
                        vm.assignSearch.houseTypeId = null;
                    }
                }
            };
        
        vm.groundingTypeOptions = {
                dataTextField: "groundingTypeName",
                open: function (e) {
                    vm.isSelect = false;
                },
                select: function (e) {
                    vm.isSelect = true;
                    var data = this.dataItem(e.item.index());
                    vm.assignSearch.groundingTypeName = data.groundingTypeName;
                    vm.assignSearch.groundingTypeId = data.groundingTypeId;
                },
                dataSource: {
                    serverFiltering: true,
                    schema: {
                        total: function (response) {
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: function (options) {
                            vm.isSelect = false;
                            return Restangular.all("assignHandoverService/getListGroundingType").post({
                            	groundingTypeName: vm.assignSearch.groundingTypeName,
                                pageSize: vm.houseTypeOptions.pageSize,
                                page: 1
                            }).then(function (response) {
                                options.success(response);
                                if (response.plain() === [] || vm.assignSearch.houseTypeName === "") {
                                	vm.assignSearch.groundingTypeName = null;
                                    vm.assignSearch.groundingTypeId = null;
                                }
                            }).catch(function (err) {
                            	vm.assignSearch.groundingTypeName = null;
                                vm.assignSearch.groundingTypeId = null;
                            });
                        }
                    }
                },
                pageSize: 10,
                template: '<div class="row" ><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.groundingTypeName #</div> </div>',
                change: function (e) {
                    if (!vm.isSelect) {
                    	vm.assignSearch.groundingTypeName = null;
                        vm.assignSearch.groundingTypeId = null;
                    }
                },
                close: function (e) {
                    if (!vm.isSelect) {
                    	vm.assignSearch.groundingTypeName = null;
                        vm.assignSearch.groundingTypeId = null;
                    }
                }
            };
        
        vm.importAssignHandoverBGMB = importAssignHandoverBGMB;
        function importAssignHandoverBGMB() {
            var templateUrl = "coms/assignHandover/assignHandoverImportBGMB.html";
            var title = "Tải lên file giao nhận BGMB";
            var windowId = "IMPORT_ASSIGN_HANOVER_BGMB";
            CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, true, '1000', '275', null);
        }
        
        vm.editDetail = function(dataItem){
        	assignHandoverService.findSignStateGoodsPlanByConsId(dataItem.constructionId).then(function (data) {
                if(data.plain().length>0 && data.signState!=1){
                	toastr.warning("Công trình đã được đặt vật tư sang phòng đầu tư.");
                	return;
                } else {
                	doSearch();
                	vm.columnHeight=false;
                    vm.houseType = false;
                    vm.groundingType = false;
                    vm.checkFormat=false;
                	vm.listHaveWorkItemName=[];
                	vm.assignSearch = dataItem;
                	if(dataItem.stationType==3){
                		vm.assignSearch.totalLengthNgam=dataItem.totalLength;
                	} else if(dataItem.stationType==4){
                		vm.assignSearch.totalLengthTreo=dataItem.totalLength;
                	}
                	vm.assignHandoverId = dataItem.assignHandoverId;
//                	vm.assignSearch.companyAssignDate = parseDateType(dataItem.companyAssignDate);
                	vm.departmentAssignDate = dataItem.departmentAssignDate;
                	vm.companyAssignDate = dataItem.companyAssignDate;
                	vm.assignSearch.departmentAssignDate = dataItem.departmentAssignDate;
                	vm.assignSearch.stationType = dataItem.stationType;
                	if(dataItem.companyAssignDate!=null){
                		vm.assignSearch.numberCompanyAssignDate = Math.floor((today - new Date(dataItem.companyAssignDate)) / (1000*60*60*24));
//                		vm.assignSearch.numberCompanyAssignDate = Math.floor((today - kendo.parseDate(dataItem.companyAssignDate,"dd/MM/yyyy")) / (1000*60*60*24));
                	} else {
                		vm.assignSearch.numberCompanyAssignDate=null;
                	}
                	var obj={};
                	if(dataItem.receivedObstructDate!=null){
                		vm.assignSearch.statusVuong=true;
                		vm.assignSearch.receivedObstructContent = dataItem.receivedObstructContent;
                	} else {
                		vm.assignSearch.statusVuong=false;
                		vm.assignSearch.receivedObstructContent = null;
                	}
                	
                	if(dataItem.receivedGoodsDate!=null){
                		vm.assignSearch.statusVtmd=true;
                		vm.assignSearch.receivedGoodsContent = dataItem.receivedGoodsContent;
                	} else {
                		vm.assignSearch.statusVtmd=false;
                		vm.assignSearch.receivedGoodsContent = null;
                	}
                	
                	if(dataItem.haveWorkItemName!=null){
                		var listName = dataItem.haveWorkItemName.split(",");
                		for(var i=0;i<listName.length;i++){
                			if(listName[i]=='Cột'){
                				vm.assignSearch.haveCot = true;
                				vm.columnHeight = true;
                			}
                			if(listName[i]=='Tiếp địa'){
                				vm.assignSearch.haveTiepDia = true;
                				vm.groundingType = true;
                			}
                			if(listName[i]=='Nhà'){
                				vm.assignSearch.haveNha = true;
                				vm.houseType = true;
                			}
                			if(listName[i]=='AC'){
                				vm.assignSearch.haveAC = true;
                			}
                		}
                		vm.listHaveWorkItemName = dataItem.haveWorkItemName.split(",");
                	}
                	
                	if(dataItem.isFence==1){
        				vm.assignSearch.isFence = true;
        			} else {
        				vm.assignSearch.isFence = false;
        			}
                	
//                	vm.houseTypeDataList = dataItem.houseType;
//                	vm.groundingTypeDataList = dataItem.groundingType;
                	obj.constructionCode = dataItem.constructionCode;
                	obj.catStationHouseCode = dataItem.catStationHouseCode;
                	obj.cntContractCode = dataItem.cntContractCode;
                	assignHandoverService.getCheckDataWorkItem(obj).then(function (d) {
//            			if(d.length>0){
//            				toastr.warning("Đã có hạng mục ghi nhận sản lượng, không được sửa thông tin nhận BGMB !");
//            				return;
//            			} else {
            				if(dataItem.catConstructionTypeId !=2){
            					var templateUrl = 'coms/assignHandover/updateDetailAssignHandover.html';
            		            var title = gettextCatalog.getString("Cập nhật thông tin nhận BGMB công trình "+dataItem.constructionCode);
            		            assignHandoverService.getListImageHandover(dataItem.assignHandoverId).then(function (data) {
                                    if (!!data.imgList && data.imgList.length > 0) {
                                        vm.detail.listImage = data.imgList;
                                        vm.changeImage(vm.detail.listImage[0]);
                                    } else {
                                        vm.detail.listImage = [];
                                    }
                                    var height = vm.detail.listImage.length > 0 ? '930' : '430';

//                                    CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, true, '70%', height, null);
                                    CommonService.populatePopupCreate(templateUrl, title, null, vm, "cvUpdate", true, '60%', '80%', null);
                                }, function (error) {
                                    vm.detail.listImage = [];
                                });
//            		            CommonService.populatePopupCreate(templateUrl, title, null, vm, "cvUpdate", true, '60%', '80%', null);
            				} else if(dataItem.catConstructionTypeId==2){
            					var templateUrl = 'coms/assignHandover/updateDetailAssignHandoverTuyen.html';
            		            var title = gettextCatalog.getString("Cập nhật thông tin nhận BGMB tuyến công trình "+dataItem.constructionCode);
            		            assignHandoverService.getListImageHandover(dataItem.assignHandoverId).then(function (data) {
                                    if (!!data.imgList && data.imgList.length > 0) {
                                        vm.detail.listImage = data.imgList;
                                        vm.changeImage(vm.detail.listImage[0]);
                                    } else {
                                        vm.detail.listImage = [];
                                    }
                                    var height = vm.detail.listImage.length > 0 ? '930' : '430';

                                    CommonService.populatePopupCreate(templateUrl, title, null, vm, "cvUpdate", true, '60%', '80%', null);
                                }, function (error) {
                                    vm.detail.listImage = [];
                                });
//            		            CommonService.populatePopupCreate(templateUrl, title, null, vm, "cvUpdateTuyen", true, '60%', '80%', null);
            				}
//            			}
                    });
                }
            }, function (error) {
                
            });
        }
        
        vm.changeCheckCot = changeCheckCot;
        	function changeCheckCot(){
        	if(vm.assignSearch.haveCot){
        		vm.columnHeight = true;
        		vm.listHaveWorkItemName.push('Cột');
        	} else {
        		vm.columnHeight = false;
        		for(var i=0;i<vm.listHaveWorkItemName.length;i++){
        			if(vm.listHaveWorkItemName[i]=='Cột'){
        				vm.listHaveWorkItemName.splice(i,1);
        			}
        		}
        	}
        }
        
        vm.changeCheckNha = changeCheckNha;
        	function changeCheckNha(){
        	if(vm.assignSearch.haveNha){
        		vm.houseType = true;
        		vm.listHaveWorkItemName.push('Nhà');
        	} else {
        		vm.houseType = false;
        		for(var i=0;i<vm.listHaveWorkItemName.length;i++){
        			if(vm.listHaveWorkItemName[i]=='Nhà'){
        				vm.listHaveWorkItemName.splice(i,1);
        			}
        		}
        	}
        }
        
        vm.changeCheckTiepDia = changeCheckTiepDia;
        	function changeCheckTiepDia(){
        	if(vm.assignSearch.haveTiepDia){
        		vm.listHaveWorkItemName.push('Tiếp địa');
        		vm.groundingType = true;
        	} else {
        		vm.groundingType = false;
        		for(var i=0;i<vm.listHaveWorkItemName.length;i++){
        			if(vm.listHaveWorkItemName[i]=='Tiếp địa'){
        				vm.listHaveWorkItemName.splice(i,1);
        			}
        		}
        	}
        }
        
        vm.changeCheckAC = changeCheckAC;
        function changeCheckAC(){
        	if(vm.assignSearch.haveAC){
        		vm.listHaveWorkItemName.push('AC');
        	} else {
        		for(var i=0;i<vm.listHaveWorkItemName.length;i++){
        			if(vm.listHaveWorkItemName[i]=='AC'){
        				vm.listHaveWorkItemName.splice(i,1);
        			}
        		}
        	}
        }
        
//        vm.changeCheckIsFence = changeCheckIsFence;
        function changeCheckIsFence(){
        	if(vm.assignSearch.isFence){
        		vm.assignSearch.isFence = 1;
        	} else {
        		vm.assignSearch.isFence = null;
        	}
        }
        
        vm.saveEdit = saveEdit;
        function saveEdit(){
        	var obj={};
        	changeCheckIsFence();
        	obj = vm.assignSearch;
        	assignHandoverService.getCheckDataWorkItem(obj).then(function (d) {
    			if(d.length>0){
    				toastr.warning("Đã có hạng mục ghi nhận sản lượng, không được sửa thông tin nhận BGMB !");
    				return;
    			} else {
    				if(vm.assignSearch.statusVuong){
    	        		if(vm.assignSearch.receivedObstructContent!=null && vm.assignSearch.receivedObstructContent!=""){
    				  		$('#vuongReq').css({"color":"red", "display":"none"});
    				  	} else {
    				  		$('#vuongReq').css({"color":"red", "display":""});
    				  		$("#vuongContentId").focus();
    				  		return false;
    				  	}
    	        	} else {
    	        		$('#vuongReq').css({"color":"red", "display":"none"});
    	        		vm.assignSearch.receivedObstructContent = null;
    	        	}
    	        	
    	        	if(vm.assignSearch.statusVtmd){
    	        		if(vm.assignSearch.receivedGoodsContent!=null && vm.assignSearch.receivedGoodsContent!=""){
    				  		$('#vatTuReq').css({"color":"red", "display":"none"});
    				  	} else {
    				  		$('#vatTuReq').css({"color":"red", "display":""});
    				  		$("#vatTuId").focus();
    				  		return false;
    				  	}
    	        	} else {
    	        		$('#vatTuReq').css({"color":"red", "display":"none"});
    	        		vm.assignSearch.receivedGoodsContent=null;
    	        	}
    	        	
    	        	if(vm.assignSearch.numberCo>99){
    	        		toastr.warning("Số mố co quá lớn (nhỏ hơn 99)");
    	        		return;
    	        	}
    	        	
    	        	if(!vm.assignSearch.haveCot && vm.assignSearch.columnHeight==null){
    	        		toastr.error("Độ cao cột không được để trống");
    	        		$("#columnHeightId").focus();
    	        		return;
    	        	} else if(!vm.assignSearch.haveNha && vm.assignSearch.houseTypeName==null){
    	        		toastr.error("Loại nhà không được để trống");
    	        		$("#loaiNha").focus();
    	        		return;
    	        	} else if(!vm.assignSearch.haveTiepDia && vm.assignSearch.groundingTypeName==null){
    	        		toastr.error("Loại tiếp địa không được để trống");
    	        		$("#loaiTd").focus();
    	        		return;
    	        	} else if(!vm.assignSearch.haveCot && vm.assignSearch.columnHeight<1){
    	        		toastr.error("Độ cao cột phải lớn hơn 0");
    	        		$("#columnHeightId").focus();
    	        		return;
    	        	}
    				confirm('Xác nhận cập nhật giao việc ?', function () {
//    	            	changeCheckCot();
//    	            	changeCheckNha();
//    	            	changeCheckTiepDia();
//    	            	changeCheckAC();
    	            	changeCheckIsFence();
    	            	obj = vm.assignSearch;
//    	            	vm.assignSearch.departmentAssignDate = vm.departmentAssignDate;
    	            	vm.assignSearch.companyAssignDate = vm.companyAssignDate;
    	            	obj.columnHeight = vm.assignSearch.columnHeight;
    	            	obj.stationType = vm.assignSearch.stationType;
    	            	obj.numberCo = vm.assignSearch.numberCo;
    	            	obj.houseTypeId = vm.assignSearch.houseTypeId;
    	            	obj.houseTypeName = vm.assignSearch.houseTypeName;
    	            	obj.isFence = vm.assignSearch.isFence;
    	            	obj.haveWorkItemName = vm.listHaveWorkItemName.join(',');
    	            	obj.constructionId = vm.assignSearch.constructionId;
    	            	obj.constructionCode = vm.assignSearch.constructionCode;
    	            	obj.cntContractCode = vm.assignSearch.cntContractCode;
    	            	obj.cntContractId = vm.assignSearch.cntContractId;
    	            	obj.catStationHouseCode = vm.assignSearch.catStationHouseCode;
    	            	obj.catStationHouseId = vm.assignSearch.catStationHouseId;
    	            	obj.constructionStatus = vm.assignSearch.constructionStatus;
    	            	obj.assignHandoverId = vm.assignSearch.assignHandoverId;
    	            	obj.receivedGoodsContent = vm.assignSearch.receivedGoodsContent;
    	            	obj.receivedObstructContent = vm.assignSearch.receivedObstructContent;
    	            	obj.isFence = vm.assignSearch.isFence;
    	            	
    	                assignHandoverService.removeAssignHandoverById(vm.assignSearch).then(function (d) {
    	                    
    	                }, function (errResponse) {
    	                        toastr.error("Lỗi không xóa được!");
    	                        return;
    	                });
    	                
//    	                assignHandoverService.updateAssignHandover(obj).then(function (d) {
//    	                	
//    	                }, function (errResponse) {
//    	                    toastr.error("Lỗi khi lưu 1!");
//    	                    return;
//    	                });
//    	                obj.receivedStatus = vm.assignSearch.receivedStatus;
    	                
    	                if(vm.assignSearch.isFence){
    	                	obj.typeSave = [13];
    	                	obj.lstType = [13];
    	                	obj.isFence=1;
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 19!");
    	                            return;
    	                    });
    	                } else {
    	                	obj.isFence=null;
    	                	obj.typeSave = [];
    	                	obj.lstType = [13];
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 19!");
    	                            return;
    	                    });
    	                }
    	                
    	                if(!vm.assignSearch.haveAC){
    	                	obj.typeSave = [14];
    	                	obj.lstType = [14];
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 20!");
    	                            return;
    	                    });
    	                } else {
    	                	obj.typeSave = [];
    	                	obj.lstType = [14];
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 21!");
    	                            return;
    	                    });
    	                }
    	                if(vm.assignSearch.columnHeight==null && vm.assignSearch.stationType==1){
    	                		vm.assignSearch.columnHeight = null;
    	                		obj.typeSave = [15];
    	                    	obj.lstType = [1,2,16];
    	                    	obj.stationType=1;
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 22!");
    	                                return;
    	                        });
    	                } else  if(vm.assignSearch.columnHeight==null && vm.assignSearch.stationType==2){
    	            		vm.assignSearch.columnHeight = null;
    	            		obj.typeSave = [16];
    	                	obj.lstType = [1,2,15];
    	                	obj.stationType=2;
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 22!");
    	                            return;
    	                    });
    	                } else if(vm.assignSearch.columnHeight!=null && vm.assignSearch.stationType==1){
    	                	if(!vm.assignSearch.haveCot){
    	                		obj.typeSave = [1,15];
    	                    	obj.lstType = [2,16];
    	                    	obj.stationType=1;
    	                    	obj.columnHeight = vm.assignSearch.columnHeight;
    	                    	obj.constructionStatus = vm.assignSearch.constructionStatus;
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 6!");
    	                                return;
    	                        });
    	                	} else {
    	                		vm.assignSearch.columnHeight = null;
    	                		obj.typeSave = [15];
    	                    	obj.lstType = [1,2,16];
    	                    	obj.stationType=1;
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 6!");
    	                                return;
    	                        });
    	                	}
    	                } else if(vm.assignSearch.columnHeight!=null && vm.assignSearch.stationType==2){
    	                	if(!vm.assignSearch.haveCot){
    	                		obj.typeSave = [2,16];
    	                    	obj.lstType = [1,15];
    	                    	obj.stationType=2;
    	                    	obj.columnHeight = vm.assignSearch.columnHeight;
    	                    	obj.constructionStatus = vm.assignSearch.constructionStatus;
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 7!");
    	                                return;
    	                        });
    	                	} else {
    	                		vm.assignSearch.columnHeight = null;
    	                		obj.typeSave = [16];
    	                    	obj.lstType = [1,2,15];
    	                    	obj.stationType=2;
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 6!");
    	                                return;
    	                        });
    	                	}
    	                }
    	                
    	                if(vm.assignSearch.houseTypeName=='Nhà LG'){
    	                	if(vm.assignSearch.haveNha){
    	                		obj.typeSave = [];
    	                    	obj.lstType = [3,4,5,6,7,8];
    	                    	vm.assignSearch.houseTypeName = null;
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 8!");
    	                                return;
    	                        });
    	                	} else {
    	                		obj.typeSave = [3];
    	                    	obj.lstType = [4,5,6,7,8];
    	                    	obj.houseTypeId = vm.assignSearch.houseTypeId;
    	                    	obj.houseTypeName = vm.assignSearch.houseTypeName;
    	                    	obj.constructionStatus = vm.assignSearch.constructionStatus;
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 8!");
    	                                return;
    	                        });
    	                	}
    	                } else if(vm.assignSearch.houseTypeName=='Nhà Cabin'){
    	                	if(vm.assignSearch.haveNha){
    	                		vm.assignSearch.houseTypeName = null;
    	                		obj.typeSave = [];
    	                    	obj.lstType = [3,4,5,6,7,8];
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 8!");
    	                                return;
    	                        });
    	                	} else {
    	                	obj.typeSave = [4];
    	                	obj.lstType = [3,5,6,7,8];
    	                	obj.houseTypeId = vm.assignSearch.houseTypeId;
    	                	obj.houseTypeName = vm.assignSearch.houseTypeName;
    	                	obj.constructionStatus = vm.assignSearch.constructionStatus;
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 9!");
    	                            return;
    	                    });
    	                	}
    	                } else if(vm.assignSearch.houseTypeName=='Nhà Minishelter'){
    	                	if(vm.assignSearch.haveNha){
    	                		vm.assignSearch.houseTypeName = null;
    	                		obj.typeSave = [];
    	                    	obj.lstType = [3,4,5,6,7,8];
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 8!");
    	                                return;
    	                        });
    	                	} else {
    	                	obj.typeSave = [5];
    	                	obj.lstType = [3,4,6,7,8];
    	                	obj.houseTypeId = vm.assignSearch.houseTypeId;
    	                	obj.houseTypeName = vm.assignSearch.houseTypeName;
    	                	obj.constructionStatus = vm.assignSearch.constructionStatus;
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 10!");
    	                            return;
    	                    });
    	                	}
    	                } else if(vm.assignSearch.houseTypeName=='Nhà xây' && vm.assignSearch.stationType==1){
    	                	if(vm.assignSearch.haveNha){
    	                		vm.assignSearch.houseTypeName = null;
    	                		obj.typeSave = [];
    	                    	obj.lstType = [3,4,5,6,7,8];
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 8!");
    	                                return;
    	                        });
    	                	} else {
    	                	obj.typeSave = [6];
    	                	obj.lstType = [3,4,5,7,8];
    	                	obj.houseTypeId = vm.assignSearch.houseTypeId;
    	                	obj.houseTypeName = vm.assignSearch.houseTypeName;
    	                	obj.stationType=1;
    	                	obj.constructionStatus = vm.assignSearch.constructionStatus;
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 11!");
    	                            return;
    	                    });
    	                	}
    	                } else if(vm.assignSearch.houseTypeName=='Nhà xây' && vm.assignSearch.stationType==2){
    	                	if(vm.assignSearch.haveNha){
    	                		vm.assignSearch.houseTypeName = null;
    	                		obj.typeSave = [];
    	                    	obj.lstType = [3,4,5,6,7,8];
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 8!");
    	                                return;
    	                        });
    	                	} else {
    	                	obj.typeSave = [7];
    	                	obj.lstType = [3,4,5,6,8];
    	                	obj.houseTypeId = vm.assignSearch.houseTypeId;
    	                	obj.houseTypeName = vm.assignSearch.houseTypeName;
    	                	obj.stationType=2;
    	                	obj.constructionStatus = vm.assignSearch.constructionStatus;
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 12!");
    	                            return;
    	                    });
    	                	}
    	                } else if(vm.assignSearch.houseTypeName=='Nhà cải tạo'){
    	                	if(vm.assignSearch.haveNha){
    	                		vm.assignSearch.houseTypeName = null;
    	                		obj.typeSave = [];
    	                    	obj.lstType = [3,4,5,6,7,8];
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 8!");
    	                                return;
    	                        });
    	                	} else {
    	                	obj.typeSave = [8];
    	                	obj.lstType = [3,4,5,6,7];
    	                	obj.houseTypeId = vm.assignSearch.houseTypeId;
    	                	obj.houseTypeName = vm.assignSearch.houseTypeName;
    	                	obj.constructionStatus = vm.assignSearch.constructionStatus;
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 13!");
    	                            return;
    	                    });
    	                	}
    	                }
    	                
    	                if(vm.assignSearch.groundingTypeName=='Tiếp địa GEM'){
    	                	if(vm.assignSearch.haveTiepDia){
    	                		vm.assignSearch.groundingTypeName =null;
    	                		obj.typeSave = [];
    	                    	obj.lstType = [9,10,11,12];
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 8!");
    	                                return;
    	                        });
    	                	} else {
    	                	obj.typeSave = [9];
    	                	obj.lstType = [10,11,12];
    	                	obj.groundingTypeId = vm.assignSearch.groundingTypeId;
    	                	obj.groundingTypeName = vm.assignSearch.groundingTypeName;
    	                	obj.constructionStatus = vm.assignSearch.constructionStatus;
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 14!");
    	                            return;
    	                    });
    	                	}
    	                } else if(vm.assignSearch.groundingTypeName=='Tiếp địa lập là'){
    	                	if(vm.assignSearch.haveTiepDia){
    	                		vm.assignSearch.groundingTypeName =null;
    	                		obj.typeSave = [];
    	                    	obj.lstType = [9,10,11,12];
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 8!");
    	                                return;
    	                        });
    	                	} else {
    	                	obj.typeSave = [10];
    	                	obj.lstType = [9,11,12];
    	                	obj.groundingTypeId = vm.assignSearch.groundingTypeId;
    	                	obj.groundingTypeName = vm.assignSearch.groundingTypeName;
    	                	obj.constructionStatus = vm.assignSearch.constructionStatus;
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 15!");
    	                            return;
    	                    });
    	                	}
    	                } else if(vm.assignSearch.groundingTypeName=='Tiếp địa khoan cọc' && vm.assignSearch.stationType==1){
    	                	if(vm.assignSearch.haveTiepDia){
    	                		vm.assignSearch.groundingTypeName =null;
    	                		obj.typeSave = [];
    	                    	obj.lstType = [9,10,11,12];
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 8!");
    	                                return;
    	                        });
    	                	} else {
    	                	obj.typeSave = [11];
    	                	obj.lstType = [9,10,12];
    	                	obj.groundingTypeId = vm.assignSearch.groundingTypeId;
    	                	obj.groundingTypeName = vm.assignSearch.groundingTypeName;
    	                	obj.stationType=1;
    	                	obj.constructionStatus = vm.assignSearch.constructionStatus;
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 16!");
    	                            return;
    	                    });
    	                	}
    	                } else if(vm.assignSearch.groundingTypeName=='Tiếp địa khoan cọc' && vm.assignSearch.stationType==2){
    	                	if(vm.assignSearch.haveTiepDia){
    	                		vm.assignSearch.groundingTypeName =null;
    	                		obj.typeSave = [];
    	                    	obj.lstType = [9,10,11,12];
    	                    	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Lỗi khi lưu 8!");
    	                                return;
    	                        });
    	                	} else {
    	                	obj.typeSave = [12];
    	                	obj.lstType = [9,10,11];
    	                	obj.groundingTypeId = vm.assignSearch.groundingTypeId;
    	                	obj.groundingTypeName = vm.assignSearch.groundingTypeName;
    	                	obj.stationType=2;
    	                	obj.constructionStatus = vm.assignSearch.constructionStatus;
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 17!");
    	                            return;
    	                    });
    	                	}
    	                } else if(vm.assignSearch.stationType==1 && vm.assignSearch.columnHeight==null
    	                		&& vm.assignSearch.houseTypeName!='Nhà xây' && vm.assignSearch.groundingTypeName!='Tiếp địa khoan cọc'){
    	                	obj.typeSave = [15];
    	                	obj.lstType = [15];
    	                	obj.stationType=1;
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 18!");
    	                            return;
    	                    });
    	                } else if(vm.assignSearch.stationType==2 && vm.assignSearch.columnHeight==null
    	                		&& vm.assignSearch.houseTypeName!='Nhà xây' && vm.assignSearch.groundingTypeName!='Tiếp địa khoan cọc'){
    	                	obj.typeSave = [16];
    	                	obj.lstType = [16];
    	                	obj.stationType=2;
    	                	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
    	                        
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 18!");
    	                            return;
    	                    });
    	                }
    	                
    	                if(vm.assignSearch.statusVuong && !vm.assignSearch.statusVtmd){
    	            		obj.receivedObstructDate = new Date();
    	            		obj.receivedObstructContent = vm.assignSearch.receivedObstructContent;
    	            		obj.receivedStatus = 3;
    	            		obj.constructionStatus = vm.assignSearch.constructionStatus;
    	            		obj.receivedGoodsDate = null;
//    	            		if(vm.assignSearch.constructionStatus!=4){
    	            			assignHandoverService.updateAssignHandoverVuong(obj).then(function (d) {
    	                            
    	                        }, function (err) {
    	                                toastr.error("Công trình đã được đặt vật tư sang phòng đầu tư.");
    	                                vm.checkFormat=true;
    	                                return;
    	                        });
//    	            		}
    	            	} else if(!vm.assignSearch.statusVuong && vm.assignSearch.statusVtmd){
    	            		obj.receivedGoodsDate = new Date();
    	            		obj.receivedObstructDate = null;
    	            		obj.receivedGoodsContent = vm.assignSearch.receivedGoodsContent;
    	            		obj.receivedStatus = 5;
    	            		obj.constructionStatus = vm.assignSearch.constructionStatus;
//    	            		if(vm.assignSearch.constructionStatus==4){
    	            			assignHandoverService.updateAssignHandoverVtmd(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Công trình đã được đặt vật tư sang phòng đầu tư.");
    	                                vm.checkFormat=true;
    	                                return;
    	                        });
//    	            		}
    	            	} else if(vm.assignSearch.statusVuong && vm.assignSearch.statusVtmd){
    	            		obj.receivedObstructDate = new Date();
    	            		obj.receivedObstructContent = vm.assignSearch.receivedObstructContent;
    	            		obj.receivedStatus = 4;
    	            		obj.receivedGoodsDate = new Date();
    	            		obj.receivedGoodsContent = vm.assignSearch.receivedGoodsContent;
    	            		obj.constructionStatus = vm.assignSearch.constructionStatus;
//    	            		if(vm.assignSearch.constructionStatus!=4){
    	            			assignHandoverService.updateAssignHandoverVuongVtmd(obj).then(function (d) {
    	                            
    	                        }, function (errResponse) {
    	                                toastr.error("Công trình đã được đặt vật tư sang phòng đầu tư.");
    	                                vm.checkFormat=true;
    	                                return;
    	                        });
//    	            		}
    	            	} else if(!vm.assignSearch.statusVuong && !vm.assignSearch.statusVtmd){
    	            		obj.receivedStatus = 2;
    	            		obj.receivedDate = new Date();
    	            		obj.receivedGoodsDate = null;
    	            		obj.receivedObstructDate = null;
    	            		obj.constructionStatus = vm.assignSearch.constructionStatus;
    	            		assignHandoverService.updateAssignHandoverNotVuongVtmd(obj).then(function (d) {
    	                        
    	                    }, function (error) {
    	                            toastr.error("Công trình đã được đặt vật tư sang phòng đầu tư.");
    	                            vm.checkFormat=true;
    	                            return;
    	                    });
    	            	}
    	                
    	                if(vm.assignSearch.stationType==1){
    	                	obj.stationType=1;
    	                } else if(vm.assignSearch.stationType==2){
    	                	obj.stationType=2;
    	                }
    	                
    	                if(vm.assignSearch.isFence!=1){
    	                	obj.isFence=null;
    	                } else {
    	                	obj.isFence=1;
    	                }
    	                
    	                if(vm.assignSearch.haveCot){
    	                	obj.columnHeight=null;
    	                }
    	                
    	                if(vm.assignSearch.haveNha){
    	                	obj.houseTypeId =null;
    	                	obj.houseTypeName =null;
    	                }
    	                
    	                if(vm.assignSearch.haveTiepDia){
    	                	obj.groundingTypeId = null;
    	                	obj.groundingTypeName = null;
    	                }
    	                $setTimeout(function(){
    	                	
    	                },100);
    	                	assignHandoverService.updateWorkItem(obj).then(function (d) {
    	                		if(!vm.checkFormat){
    	                        toastr.success("Cập nhật thành công !");
    	                        CommonService.dismissPopup1();
    	                        doSearch();
    	                        vm.listHaveWorkItemName=[];
    	                		}
    	                    }, function (errResponse) {
    	                            toastr.error("Lỗi khi lưu 18!");
    	                            return;
    	                    });
    	                	doSearch();
    	                //end confirm
    	            });
    			}
            });
        }
        
        vm.downloadTemplateBGMB = downloadTemplateBGMB;
    	function downloadTemplateBGMB(){
    		assignHandoverService.downloadTemplateBGMB({}).then(function (d) {
            var data = d.plain();
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        }).catch(function () {
            toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        });
    	}
    	
    	vm.submitImportBGMB = submitImportBGMB;
        function submitImportBGMB() {
            console.log("submitImport body");
            $('#testSubmit').addClass('loadersmall');
            vm.disableSubmit = true;
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
            var formData = new FormData();
            formData.append('multipartFile', vm.fileImportData);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "assignHandoverService/importExcelBGMB?folder=temp",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if (data == 'NO_CONTENT') {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.warning("File import không có nội dung");
                    }
                    else if (!!data.error) {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.error(data.error);
                    }
                    else if (data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length > 0) {
                        vm.lstErrImport = data[data.length - 1].errorList;
                        vm.objectErr = data[data.length - 1];
                        var templateUrl = "wms/createImportRequirementManagement/importResultPopUp.html";
                        var title = "Kết quả Import";
                        var windowId = "ERR_IMPORT_BGMB";
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
                        vm.cancel();
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
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:center;"
                        },
                    }, {
                        title: "Dòng lỗi",
                        field: 'lineError',
                        width: 100,
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:center;"
                        }
                    }, {
                        title: "Cột lỗi",
                        field: 'columnError',
                        width: 100,
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:center;"
                        }
                    }, {
                        title: "Nội dung lỗi",
                        field: 'detailError',
                        width: 250,
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:left;"
                        }
                    }
                ]
            });
        }
        
        vm.exportExcelErr = function () {
        	Restangular.all("fileservice/exportExcelError").post(vm.objectErr).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
                return;
            });
        }
        vm.closeErrImportPopUp=closeErrImportPopUp;
        function closeErrImportPopUp() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }
        //end edit BTS
        
        //start edit tuyến
        vm.saveEditTuyen = function(){
        	var obj = {};
        	changeCheckIsFence();
        	obj = vm.assignSearch;
        	
        	if(vm.assignSearch.stationType == 3){
        		if(vm.assignSearch.totalLengthNgam==null){
        			toastr.warning("Tổng độ dài tuyến không được để trống !");
        			$("#columnHeightNgamId").focus();
        			return;
        		} else if (kendo.parseFloat(vm.replaceVAlue(vm.assignSearch.totalLengthNgam))==null){
        			toastr.warning("Tổng độ dài tuyến sai định dạng !");
        			$("#columnHeightNgamId").focus();
        			return;
        		} else if(vm.assignSearch.hiddenImmediacy==null){
        			toastr.warning("Chôn trực tiếp không được để trống !");
        			$("#hiddenImmediacyId").focus();
        			return;
        		} else if(kendo.parseFloat(vm.replaceVAlue(vm.assignSearch.hiddenImmediacy))==null){
        			toastr.warning("Chôn trực tiếp sai định dạng !");
        			$("#hiddenImmediacyId").focus();
        			return;
        		} else if(vm.assignSearch.cableInTank==null){
        			toastr.warning("Cáp trong cống bể xây không được để trống !");
        			$("#cableInTankId").focus();
        			return;
        		} else if(kendo.parseFloat(vm.replaceVAlue(vm.assignSearch.cableInTank))==null){
        			toastr.warning("Cáp trong cống bể xây sai định dạng !");
        			$("#cableInTankId").focus();
        			return;
        		} else if(vm.assignSearch.cableInTankDrain==null){
        			toastr.warning("Cáp trong cống bể có sẵn không được để trống !");
        			$("#cableInTankDrainId").focus();
        			return;
        		} else if(vm.assignSearch.cableInTankDrain==null){
        			toastr.warning("Cáp trong cống bể có sẵn sai định dạng !");
        			$("#cableInTankDrainId").focus();
        			return;
        		} else if(vm.replaceVAlue(vm.assignSearch.totalLengthNgam)<1){
        			toastr.warning("Tổng độ dài tuyến phải lớn hơn 0 !");
        			$("#columnHeightNgamId").focus();
        			return;
        		}
        		obj.totalLength = vm.replaceVAlue(vm.assignSearch.totalLengthNgam);
        		obj.hiddenImmediacy = vm.replaceVAlue(vm.assignSearch.hiddenImmediacy);
        		obj.cableInTank = vm.replaceVAlue(vm.assignSearch.cableInTank);
        		obj.cableInTankDrain = vm.replaceVAlue(vm.assignSearch.cableInTankDrain);
        		obj.typeSave = [17];
            	obj.lstType = [18];
            	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
                    
                }, function (errResponse) {
                        toastr.error("Lỗi khi lưu quang ngầm !");
                        return;
                });
        	} else if(vm.assignSearch.stationType == 4){
	        	if(vm.assignSearch.totalLengthTreo==null){
	        		toastr.warning("Tổng độ dài tuyến không được để trống !");
	    			$("#columnHeightTreoId").focus();
	    			return;
	        	} else if(kendo.parseFloat(vm.replaceVAlue(vm.assignSearch.totalLengthTreo))==null){
	        		toastr.warning("Tổng độ dài tuyến sai định dạng !");
	    			$("#columnHeightTreoId").focus();
	    			return;
	        	} else if(vm.assignSearch.plantColunms==null){
	        		toastr.warning("Trồng cột không được để trống !");
	    			$("#plantColumnsId").focus();
	    			return;
	        	} else if(kendo.parseFloat(vm.replaceVAlue(vm.assignSearch.plantColunms))==null){
	        		toastr.warning("Trồng cột sai định dạng !");
	    			$("#plantColumnsId").focus();
	    			return;
	        	} else if(vm.assignSearch.availableColumns==null){
	        		toastr.warning("Cột có sẵn không được để trống !");
	    			$("#availableColumnsId").focus();
	    			return;
	        	} else if(kendo.parseFloat(vm.replaceVAlue(vm.assignSearch.availableColumns))==null){
	        		toastr.warning("Cột có sẵn sai định dạng !");
	    			$("#availableColumnsId").focus();
	    			return;
	        	} else if(vm.replaceVAlue(vm.assignSearch.totalLengthTreo)<1){
        			toastr.warning("Tổng độ dài tuyến phải lớn hơn 0 !");
        			$("#columnHeightNgamId").focus();
        			return;
        		}
	        	obj.totalLength = vm.replaceVAlue(vm.assignSearch.totalLengthTreo);
	        	obj.plantColunms = vm.replaceVAlue(vm.assignSearch.plantColunms);
	        	obj.availableColumns = vm.replaceVAlue(vm.assignSearch.availableColumns);
	        	obj.typeSave = [18];
            	obj.lstType = [17];
            	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
                    
                }, function (errResponse) {
                        toastr.error("Lỗi khi lưu quang treo !");
                        return;
                });
        	}
        	
//        	if(vm.assignSearch.stationType==3){
//        		obj.typeSave = [17];
//            	obj.lstType = [18];
//            	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
//                    
//                }, function (errResponse) {
//                        toastr.error("Lỗi khi lưu quang ngầm !");
//                        return;
//                });
//        	} else if(vm.assignSearch.stationType==4){
//        		obj.typeSave = [18];
//            	obj.lstType = [17];
//            	assignHandoverService.checkWorkItemConsTask(obj).then(function (d) {
//                    
//                }, function (errResponse) {
//                        toastr.error("Lỗi khi lưu quang treo !");
//                        return;
//                });
//        	}
        	
        	if(vm.assignSearch.statusVuong){
        		if(vm.assignSearch.receivedObstructContent==null){
        			toastr.warning("Nội dung vướng không được để trống !");
        			$("#vuongContentId").focus();
        			return;
        		} else {
        			obj.receivedObstructDate = new Date();
            		obj.receivedObstructContent = vm.assignSearch.receivedObstructContent;
            		obj.receivedStatus = 3;
            		obj.constructionStatus = vm.assignSearch.constructionStatus;
            		obj.receivedGoodsDate = null;
            		assignHandoverService.updateAssignHandoverVuong(obj).then(function (d) {
                            
                       }, function (err) {
//                               toastr.error("Công trình đã được đặt vật tư sang phòng đầu tư.");
//                               vm.checkFormat=true;
                               return;
                        });
        		}
        	} else {
        		obj.receivedStatus = 2;
        		obj.receivedDate = new Date();
        		obj.receivedGoodsDate = null;
        		obj.receivedObstructDate = null;
        		obj.constructionStatus = vm.assignSearch.constructionStatus;
        		assignHandoverService.updateAssignHandoverNotVuongVtmd(obj).then(function (d) {
                    
                }, function (error) {
//                        toastr.error("Công trình đã được đặt vật tư sang phòng đầu tư.");
//                        vm.checkFormat=true;
                        return;
                });
        	}
        	
        	assignHandoverService.updateConstructionTuyen(obj).then(function (d) {
                toastr.success("Cập nhật thành công !");
                CommonService.dismissPopup1();
                doSearch();
                vm.listHaveWorkItemName=[];
            }, function (errResponse) {
                    toastr.error("Lỗi khi lưu 18!");
                    return;
            });
        	
        	
        }
        //end tuyến
        
        
        //end controller
    }
})();