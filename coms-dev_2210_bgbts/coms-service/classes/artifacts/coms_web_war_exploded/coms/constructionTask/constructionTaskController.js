(function () {
    'use strict';
    var controllerId = 'constructionTaskController';

    angular.module('MetronicApp').controller(controllerId, constructionTaskController);

    function constructionTaskController($scope, $rootScope, $timeout, gettextCatalog, $filter,
        kendoConfig, $kWindow, constructionTaskService,
        CommonService, PopupConst, Restangular, RestEndpoint, Constant, htmlCommonService, detailMonthPlanService ) {
        initCommonFunction($scope, $rootScope, $filter);
        var vm = this;
        vm.String = 'Quản lý công trình > Quản lý kế hoạch > Thông tin tiến độ công trình'
        vm.obj = {};
        vm.catTaskSearch = {};
        vm.workItemSearch = {
            listImage: []
        };
        vm.performerSearch = {};
        vm.curentObj = {};
        vm.obj.type = 1;
        //list work item
        vm.obj.childDTOList = [];
        vm.obj.taskOrder = 1;
        var record = 0;
        var startDateConstr;
        var endDateConstr;
        vm.disableInputSL = true;
        //        chinhpxn20180714_start
        vm.actionType = 0;
        //        chinhpxn20180714_end
        vm.dateGrantt = '';
        vm.changeImage = changeImage;
        vm.imageSelected = {};
        vm.isDisableAssign = !!vm.obj.constructionId;
        vm.isDisable=false;
        vm.constrTaskTypeData = [
            { id: 1, name: "Thi công" },
            { id: 2, name: "Làm Quỹ lương" },
            { id: 3, name: "Lên doanh thu" },
            { id: 6, name: "Công việc khác" },
        ]
        vm.typeConstruction = {
            dataSource: vm.constrTaskTypeData,
            dataTextField: "name",
            dataValueField: "id",
            valuePrimitive: true,
            select: changeTaskType
        }
        
        vm.sourceWorkData = [
        		{ id: 1, name: "Xây lắp" },
                { id: 2, name: "Chi phí" },
                { id: 3, name: "Ngoài Tập Đoàn" },
                { id: 4, name: "Hạ tầng cho thuê xây dựng móng" },
                { id: 5, name: "Hạ tầng cho thuê hoàn thiện" },
                { id: 6, name: "Công trình XDDD" },
                { id: 7, name: "Xây lắp - Trung tâm xây dựng" },
        ]
        
        vm.sourceWorkLst = {
        		dataSource: vm.sourceWorkData,
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true,
//                select: changeSourceWork
        }
        
        vm.constructionTypeData = [
        		{ id: 1, name: "Các Công trình BTS, Costie, SWAP và các công trình nguồn đầu tư mảng xây lắp" },
                { id: 2, name: "Các công trình nguồn chi phí" },
                { id: 3, name: "Các công trình Bảo dưỡng ĐH và MFĐ" },
                { id: 4, name: "Các công trình Gpon" },
                { id: 5, name: "Các công trình Hợp đồng 12 đầu việc" },
                { id: 6, name: "Các công trình Ngoài Tập đoàn" },
                { id: 7, name: "Hạ tầng cho thuê" },
                { id: 8, name: "Công trình XDDD" },
                { id: 9, name: "Các hợp đồng cơ điện nguồn đầu tư TCTY ký" },
        ]
        
        vm.constructionTypeLst = {
        		dataSource: vm.constructionTypeData,
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true,
//                select: changeConstructionType
        }
        	
        vm.constructionOptions = {
            dataTextField: "code",
            dataValueField: "constructionId",
            select: function (e) {
                vm.selectedDept1 = true;
                var data = this.dataItem(e.item.index());
                vm.obj.constructionName = data.name;
                vm.obj.constructionCode = data.code;
                vm.obj.constructionId = data.constructionId;
                vm.constructionTypeId = data.catContructionTypeId;
                startDateConstr = data.startingDate;
                endDateConstr = data.excpectedCompleteDate;
                vm.workItemSearch.constructionId = data.constructionId;
                vm.workItemSearch.sysGroupId = data.sysGroupId;
                vm.performerSearch.sysGroupId = data.sysGroupId;
                vm.obj.workItemName = null;
                vm.obj.workItemCode = null;
                vm.obj.workItemId = null;
                vm.catTaskSearch.catWorkItemTypeId = null;
                vm.obj.taskName = null;
                vm.obj.catTaskCode = null;
                vm.obj.catTaskId = null;
                //tatph -starrt -27112019
                if(data.checkHTCT != null){
                	  vm.obj.checkHTCT = data.checkHTCT;
                }else{
                	 vm.obj.checkHTCT = 0;
                }
                if(data.catConstructionTypeId != null){
              	  vm.obj.catConstructionTypeId = data.catConstructionTypeId;
                }else{
              	 vm.obj.catConstructionTypeId = 0;
                }
                if(data.workItemName != null){
                	  vm.obj.workItemName = data.workItemName;
                  }else{
                	 vm.obj.workItemName = "";
                  }
                //tatph -starrt -27112019
                
                if(data.listWorkItemName!=null && data.listWorkItemName.length>0){
                	vm.listWorkItemName = data.listWorkItemName;
                } else {
                	vm.listWorkItemName = null;
                }
                
                if (vm.obj.type == 3) {
                    if (vm.obj.taskOrder == 1) {
                        vm.obj.taskName = "Tạo đề nghị quyết toán cho công trình " + vm.obj.constructionCode;
                    } else {
                        vm.obj.taskName = "Lên doanh thu cho công trình " + vm.obj.constructionCode;
                    }
                } else if (vm.obj.type == 2) {
                    vm.obj.taskName = "Làm HSHC cho công trình " + vm.obj.constructionCode;
                }
                
                getWorkItemForAddingTask();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        return Restangular.all("constructionTaskService/construction/searchConstruction").post({
                            keySearch: vm.obj.constructionCode,
                            taskType: vm.obj.type,
                            catProvinceId:vm.obj.provinceId ,
                            pageSize: vm.constructionOptions.pageSize,
                            page: 1
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.code #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.obj.constructionName = null;
                    vm.obj.constructionCode = null;
                    vm.obj.constructionId = null;
                    vm.workItemSearch.constructionId = null;
                    vm.workItemSearch.sysGroupId = null;
                    vm.performerSearch.sysGroupId = null;
                    vm.obj.workItemName = null;
                    vm.obj.workItemCode = null;
                    vm.obj.workItemId = null;
                    vm.catTaskSearch.catWorkItemTypeId = null;
                    vm.obj.taskName = null;
                    vm.obj.catTaskCode = null;
                    vm.obj.catTaskId = null;
                    vm.listWorkItemName = null;
                }
            },
            ignoreCase: false
        }


        vm.workItemOptions = {
            dataTextField: "name",
            dataValueField: "workItemId",
            select: function (e) {
                vm.selectedDept1 = true;
                var data = this.dataItem(e.item.index());
                vm.obj.workItemName = data.name;
                vm.obj.workItemCode = data.code;
                vm.obj.workItemId = data.workItemId;
                vm.catTaskSearch.catWorkItemTypeId = data.catWorkItemTypeId;
                vm.obj.taskName = null;
                vm.obj.catTaskCode = null;
                vm.obj.catTaskId = null;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        return Restangular.all("constructionTaskService/construction/rpDailyTaskWorkItems").post({
                            keySearch: vm.obj.workItemName,
                            pageSize: vm.workItemOptions.pageSize,
                            sysGroupId: vm.workItemSearch.sysGroupId,
                            constructionId: vm.workItemSearch.constructionId,
                            page: 1,
                            listStatus: [1, 2]
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left;word-wrap: break-word;">#: data.code #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden">#: data.name # </div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.obj.workItemName = null;
                    vm.obj.workItemCode = null;
                    vm.obj.workItemId = null;
                    vm.catTaskSearch.catWorkItemTypeId = null;
                    vm.obj.taskName = null;
                    vm.obj.catTaskCode = null;
                    vm.obj.catTaskId = null;
                }
            },
            ignoreCase: false
        }

        vm.catTaskOptions = {
            dataTextField: "name",
            dataValueField: "workItemId",
            select: function (e) {
                vm.selectedDept1 = true;
                var data = this.dataItem(e.item.index());
                vm.obj.taskName = data.name;
                vm.obj.catTaskCode = data.code;
                vm.obj.catTaskId = data.workItemId;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        if ((vm.obj.taskName != undefined || vm.obj.taskName != '') && (vm.obj.type == 1)) {
                            vm.disableInputSL = true;
                        } else {
                            vm.disableInputSL = false;
                        }
                        return Restangular.all("constructionTaskService/construction/searchCatTask").post({
                            keySearch: vm.obj.taskName,
                            pageSize: vm.catTaskOptions.pageSize,
                            catWorkItemTypeId: vm.catTaskSearch.catWorkItemTypeId,
                            page: 1
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left;word-wrap: break-word;">#: data.code #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.obj.taskName = null;
                    vm.obj.catTaskCode = null;
                    vm.obj.catTaskId = null;
                }
            },
            ignoreCase: false
        }

        vm.catTaskOptions1 = {
            dataTextField: "fullName",
            dataValueField: "sysUserId",
            select: function (e) {
                vm.selectedDept1 = true;
                var data = this.dataItem(e.item.index());
                vm.obj.performerName = data.fullName;
                vm.obj.performerIdDetail = data.sysUserId;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        //if(vm.performerSearch.sysGroupId==null||vm.performerSearch.sysGroupId==""&&vm.obj.type!=6){
                        //	toastr.error("Bạn phải chọn công trình trước");
                        //
                        //}
                        return Restangular.all("constructionTaskService/construction/searchPerformer").post({
                            keySearch: vm.obj.performerName,
                            pageSize: vm.catTaskOptions1.pageSize,
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
            template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.loginName #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.obj.performerName = null;
                    vm.obj.performerIdDetail = null;
                }
            },
            ignoreCase: false
        }

        vm.clearInput = function (data, form) {
            switch (data) {
                case '0':
                    vm.obj.constructionName = null;
                    vm.obj.constructionCode = null;
                    vm.obj.constructionId = null;
                    vm.obj.workItemCode = null;
                    vm.obj.workItemId = null;
                    vm.obj.workItemName = null;
                    vm.obj.taskName = null;
                    vm.obj.catTaskCode = null;
                    vm.obj.catTaskId = null;
                    vm.catTaskSearch.catWorkItemTypeId = null;
                    vm.performerSearch.sysGroupId = null;
                    break;
                case '1':
                    vm.obj.workItemCode = null;
                    vm.obj.workItemId = null;
                    vm.obj.workItemName = null;
                    vm.obj.taskName = null;
                    vm.obj.catTaskCode = null;
                    vm.obj.catTaskId = null;
                    vm.catTaskSearch.catWorkItemTypeId = null;
                    break;
                case '2':
                    vm.obj.taskName = null;
                    vm.obj.catTaskCode = null;
                    vm.obj.catTaskId = null;
                    if ((vm.obj.taskName == null) && (vm.obj.type == 1)) {
                        vm.disableInputSL = false;
                    }
                    break;
                case '3':
                    vm.obj.performerName = null;
                    vm.obj.performerId = null;
                    break;
                case '4':
                    vm.obj.startDate = null;
                    $rootScope.validateDate(vm.obj.startDate, null, null, form)
                    break;
                case '5':
                    vm.obj.endDate = null;
                    $rootScope.validateDate(vm.obj.endDate, null, null, form)
                    break;
                //chinhpxn20180622_start
                case '6':
                    vm.obj.quantity = null;
                    break;
                //chinhpxn20180622_end
                case '7':
                	vm.changePerformer.performerName = null;
	                vm.changePerformer.performerId = null;
                    break;
                case '8':
                    vm.changePerformer.altPerformerName = null;
	                vm.changePerformer.altPerformerid = null;
                    break;
                case '9':
                	vm.changePerformer.constructionCode = null;
 	                vm.changePerformer.constructionId = null;
                    break;
                case '10':
                	 vm.obj.provinceName = null;
                     vm.obj.provinceCode = null;
                     vm.obj.provinceId = null;
                    break;
                default: break;
            }
        }

        //chinhpxn20180622_start
        vm.genTaskName = function () {
            if (vm.obj.type == 3) {
                if (vm.obj.taskOrder == 1) {
                    vm.obj.taskName = "Tạo đề nghị quyết toán cho công trình " + vm.obj.constructionCode;
                } else {
                    vm.obj.taskName = "Lên doanh thu cho công trình " + vm.obj.constructionCode;
                }
            } else if (vm.obj.type == 2) {
                vm.obj.taskName = "Làm HSHC cho công trình " + vm.obj.constructionCode;
            }

        }
        //chinhpxn20180622_end

        //chinhpxn20180713_start
        vm.changeType = function () {
            vm.clearInput('0');
            vm.clearInput('1');
            vm.clearInput('2');
            vm.clearInput('3');
            vm.clearInput('4');
            vm.clearInput('5');
            vm.clearInput('6');
        }

        $scope.openPopupTC = function () {
            vm.clearInput('0');
            vm.clearInput('1');
            vm.clearInput('2');
            vm.clearInput('3');
            vm.clearInput('4');
            vm.clearInput('5');
            vm.clearInput('6');
            var teamplateUrl = "coms/constructionTask/popupAddTask.html";
            var title = "Thêm mới công việc";
            var windowId = "CONSTRUCTION_TASK";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '80%', '500', null);
        }
        //chinhpxn20180713_end

        //nhantv 180821 begin
        $scope.openPopupChangePerformer = function () {
            vm.fileImportData = false;
            vm.clearInput('0');
            vm.clearInput('1');
            vm.clearInput('2');
            vm.clearInput('3');
            vm.clearInput('4');
            vm.clearInput('5');
            vm.clearInput('6');
            vm.clearInput('7');
            vm.clearInput('8');
            vm.clearInput('9');
            vm.changePerformer.altPerformerName = null;
            vm.changePerformer.altPerformerId = null;
            var teamplateUrl = "coms/constructionTask/changePerformerPopup.html";
            var title = "Chuyển người";
            var windowId = "CHANGE_PERFORMER";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '70%', '550', null);
        }
        //nhantv 180821 end
        
        //chinhpxn20180714_start
        $scope.openPopupImportTC = function () {
            vm.fileImportData = false;
            var teamplateUrl = "coms/constructionTask/importConstructionTask.html";
            var title = "Import công việc";
            var windowId = "IMPORT_CONSTRUCTION_TASK";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '325', null);
        }

        vm.getConstructionExcelTemplate = function () {
            var fileName;
            if (vm.actionType == 1) {
                fileName = "Import_CapNhatCV_TheoKeHoachThang";
            } else if (vm.actionType == 0) {
                fileName = "Import_ThemCV_TheoKeHoachThang";
            } else if(vm.actionType == 4){
            	fileName = "Import_thicong_thang_chitiet_ngoaiOS"; //Huypq-add
            } 
            
            var obj = {};
            obj.sysGroupId = Constant.user.VpsUserInfo.sysGroupId;
            if (vm.actionType == 2 ){
                obj.request = 'exportTemplateListHSHC';
            } else if (vm.actionType == 3) {
                obj.request = 'exportTemplateListLDT';
            } else if (vm.actionType == 5) {
                obj.request = 'exportTemplateListDT';
            } else if (vm.actionType == 6) {
                obj.request = 'exportTemplateRent';
            }
            
            if(vm.actionType == 1 || vm.actionType == 0 || vm.actionType == 4 ){ //Huypq-edit
	            CommonService.downloadTemplate(fileName).then(function (d) { 
	                var data = d.plain();
	                window.location = Constant.BASE_SERVICE_URL + "constructionTaskService/downloadFileForConstructionTask?" + data.fileName;
	            }).catch(function () {
	                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
	                return;
	            });
            } else{
            	 detailMonthPlanService.downloadTemplate(obj).then(function (d) {
                     data = d.plain();
                     window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                 }).catch(function (e) {
                     toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                     return;
                 });
            }
        }

        vm.cancelImport = cancelImport;
        function cancelImport() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
            vm.disableSubmit = false;

        }

        $("div.k-window-actions > a.k-window-action > span.k-i-close").on("click", function () {
            vm.disableSubmit = false;
        });

        vm.submitConstruction = submitConstruction;
        function submitConstruction(data) {
        	var url;
        	if(vm.actionType == 1 || vm.actionType ==0){
        		url =  Constant.BASE_SERVICE_URL + "constructionTaskService/importConstructionTask?folder=temp" +
                "&month=" + $scope.monthGrantt + "&year=" + $scope.yearGrantt + "&actionType=" + vm.actionType;
        	} else if (vm.actionType == 2){
        		url = Constant.BASE_SERVICE_URL + "constructionTaskService/importHSHC"+
                "?month=" + $scope.monthGrantt + "&year=" + $scope.yearGrantt;
        	} else if (vm.actionType == 3){
        		url = Constant.BASE_SERVICE_URL + "constructionTaskService/importLDT"+
                "?month=" + $scope.monthGrantt + "&year=" + $scope.yearGrantt;
        	} else if (vm.actionType == 5){
        		url = Constant.BASE_SERVICE_URL + "constructionTaskService/importThuHoiDT"+
                "?month=" + $scope.monthGrantt + "&year=" + $scope.yearGrantt;
        	} else if (vm.actionType == 6){
        		url = Constant.BASE_SERVICE_URL + "constructionTaskService/importRentGround"+
                "?month=" + $scope.monthGrantt + "&year=" + $scope.yearGrantt;
        	}
        	else if (vm.actionType == 4){
        		url = Constant.BASE_SERVICE_URL + "constructionTaskService/importSanLuong"+
                "?month=" + $scope.monthGrantt + "&year=" + $scope.yearGrantt;
        	} 
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
//            if(vm.actionType == 4){
//            	return $.ajax({
//                    url: Constant.BASE_SERVICE_URL + "DetailMonthPlanOSRsService/importTC",
//                    type: "POST",
//                    data: formData,
//                    enctype: 'multipart/form-data',
//                    processData: false,
//                    contentType: false,
//                    cache: false,
//                    success: function (data) {
//                        if (data.length === 0) {
//                            toastr.warning("File imp" +
//                            "ort không có dữ liệu");
//                            return;
//                        } else if (data==0) {
//                            return;
//                        } else if (data[data.length - 1].errorFilePath != null) {
//                            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
//                            toastr.warning("File import không hợp lệ");
//                            return;
//                        } 
//                        else {
//                        	saveImportedTaskNgoaiOS(data, vm.actionType);
//                            cancelImport();
//                            }
//                        }
//                }).always(function() {
//                	htmlCommonService.hideLoading('#loadingIpMonth');
//        		});
//            } else {
            	return $.ajax({
                    url: url,
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
                        } else if (data == 'UNAUTHORIZED') {
                            $('#testSubmit').removeClass('loadersmall');
                            vm.disableSubmit = false;
                            toastr.error("Không thể import công việc do người dùng chưa được phân quyền miền dữ liệu!");
                        } else if (data == 'NOT_ACCEPTABLE') {
                            $('#testSubmit').removeClass('loadersmall');
                            vm.disableSubmit = false;
                            toastr.error("Không thể import công việc do người dùng được phân quyền nhiều miền dữ liệu!");
                        } else if (data == 'METHOD_NOT_ALLOWED') {
                            $('#testSubmit').removeClass('loadersmall');
                            vm.disableSubmit = false;
                            toastr.error("Không thể tạo công việc do chưa tồn tại kế hoạch tháng!");
                        } else if (!!data.error) {
                            $('#testSubmit').removeClass('loadersmall');
                            vm.disableSubmit = false;
                            toastr.error(data.error);
                        }
                        else if (data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length > 0) {
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
                        else if (data[data.length - 1].errorFilePath != null) {
                            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                            toastr.warning("File import không hợp lệ");
                            return;
                        }
                        else if (vm.actionType == 2 || vm.actionType ==3){ 
                        	saveImportedTask(data, vm.actionType);
                        } else if(vm.actionType == 5){
                        	saveRevokeTask(data);
                        } else if(vm.actionType == 4){
                        	saveImportedTaskNgoaiOS(data, vm.actionType);
                        } else if(vm.actionType == 6){
                        	saveRentGround(data);
                        }
                        else {
                            $('#testSubmit').removeClass('loadersmall');
                            vm.disableSubmit = false;
                            toastr.success("Import thành công");
                            cancelImport();
                            $("#gantt").data("kendoGantt").dataSource.read();
                        }
                        $scope.$apply();
                    }
                });
//            }
            
        }

        function saveRevokeTask(data){
        	var obj = {};
        	obj.month = $scope.monthGrantt;
        	obj.year = $scope.yearGrantt;
        	obj.listDataImport = data;
        	return Restangular.all("constructionTaskService/saveRevokeTask").post(obj).then(function (d) {
                $("#gantt").data("kendoGantt").dataSource.read();
                if (d.error) {
                    toastr.error(d.error);
                    return;
                }
                toastr.success("Thêm công việc thành công");
                cancelImport();
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
            }).catch(function (e) {
                toastr.error("Có lỗi xảy ra");
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                return;
            });
        }
        
        //Huypq-20190525-start
        function saveImportedTaskNgoaiOS(data, taskType){
        	var obj = {};
        	obj.month=$scope.monthGrantt;
        	obj.year=$scope.yearGrantt;
        	obj.listTC = data;
        	return Restangular.all("DetailMonthPlanOSRsService/addTask").post(obj).then(function (d) {
                if (d.error) {
                    toastr.error(d.error);
                    return;
                }
                toastr.success("Import dữ liệu thành công");
                cancelImport();
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
            }).catch(function (e) {
//                toastr.error("Có lỗi xảy ra");
                return;
            });
        }
        //Huy-end
        
        //Huypq-20200514-start
        function saveRentGround(data){
        	var obj = {};
        	obj.month = $scope.monthGrantt;
        	obj.year = $scope.yearGrantt;
        	obj.childDTOList = data;
        	return Restangular.all("constructionTaskService/saveRentGround").post(obj).then(function (d) {
                $("#gantt").data("kendoGantt").dataSource.read();
                if (d.error) {
                    toastr.error(d.error);
                    return;
                }
                toastr.success("Thêm mặt bằng trạm thành công");
                cancelImport();
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
            }).catch(function (e) {
                toastr.error("Có lỗi xảy ra");
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                return;
            });
        }
        //Huypq-end
        
        vm.exportExcelErr = function () {
            constructionTaskService.downloadErrorExcel(vm.objectErr).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
                return;
            });
        }

        //------HuyPQ-17/10/2018-start-exportConsSlow-------
        vm.exportFile= function(){
        	function displayLoading(target) {
            var element = $(target);
            kendo.ui.progress(element, true);
            setTimeout(function(){
            	var obj={};
            	obj.complete_state=$scope.complete_state;
            	obj.month=$scope.monthGrantt;
            	obj.year=$scope.yearGrantt;
            	obj.catProvinceCode=vm.granttSearch.catProvinceCode;
            	obj.keySearch=vm.granttSearch.keyword;
              	return Restangular.all("constructionTaskService/exportConstructionTaskSlow").post(obj).then(function (d) {
              		
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
        //---------HuyPQ-end----------

        
        vm.closeErrImportPopUp = closeErrImportPopUp;
        function closeErrImportPopUp() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
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
                    }
                    , {
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
                        },
                    }, {
                        title: "Nội dung lỗi",
                        field: 'detailError',
                        width: 250,
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:left;"
                        },
                    }
                ]
            });
        }

        vm.closeErrImportPopUp = closeErrImportPopUp;
        function closeErrImportPopUp() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

        //chinhpxn20180714_end

        vm.closePopup = function () {
            CommonService.dismissPopup1();
            vm.clearInput('0');
            vm.clearInput('1');
            vm.clearInput('2');
            vm.clearInput('3');
            vm.clearInput('4');
            vm.clearInput('5');
            vm.clearInput('6');
            vm.clearInput('7');
            vm.clearInput('8');
            vm.clearInput('9');
            vm.changePerformer.priority = 1;
            $(".k-icon.k-i-close").click();
        }
        vm.closePopupOnPopup = function () {
            CommonService.dismissPopup1();
        }
        vm.doSearchPopup = doSearchPopup;
        function doSearchPopup(data) {
            var grid;
            if (data == '0') {
                grid = vm.listConstructionPopupGrid;
            }
            else if (data == '1') {
                grid = vm.listWorkItemPopupGrid;
            }
            else if (data == '2') {
                grid = vm.listCatTaskPopupGrid;
            }
            else if (data == '3') {
                grid = vm.performerPopupGrid;
            }
            else if (data == '4') {
                grid = vm.findConstructionPopup;
            }
            else if (data == '5') {
                grid = vm.performerFindPopup;
            }
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        }
        vm.openTaskPopup = function (data) {
            vm.keySearch = undefined;
            if (data == '0') {
                var title = "Danh sách công trình";
                vm.labelSearch = "Mã/Tên công trình";
                vm.plSearch = "";
            }
            else if (data == '1') {
                var title = "Danh sách hạng mục";
                vm.labelSearch = "Mã/Tên hạng mục";
            }
            else if (data == '2') {
                var title = "Danh sách công việc";
                vm.labelSearch = "Mã/Tên công việc";
            }
            else if (data == '3') {
                var title = "Danh sách người thực hiện";
                vm.labelSearch = "Nội dung tìm kiếm";
                vm.plSearch = "Tên đăng nhập/ Họ tên/ Email"
            }
            else if (data == '4') {
            	 if(vm.changePerformer.priority==2 && !vm.changePerformer.performerId){
                    toastr.error("Bạn phải chọn người thực hiện trước");
                    return;
                }
            	var title = "Danh sách công trình";
                vm.labelSearch = "Mã/Tên công trình";
                vm.plSearch = "";
            }
            else if (data == '5') {
            	  if(vm.changePerformer.priority==1 && !vm.changePerformer.constructionCode){
                  	toastr.error("Bạn phải chọn công trình trước");
                  	return;
                  }
            	  var title = "Danh sách người thực hiện";
                  vm.labelSearch = "Nội dung tìm kiếm";
                  vm.plSearch = "Tên đăng nhập/ Họ tên/ Email"
            }
            else if (data == '6') {
                var title = "Danh sách tỉnh";
                vm.labelSearch = "Mã/tên tỉnh";
            }
            var teamplateUrl = "coms/constructionTask/popupOnPopup.html";
            var windowId = "CONSTRUCTION_TASK";
            CommonService.populatePopupOnPopup(teamplateUrl, title, data, vm, windowId, true, '950', '450', null);
        }


        vm.openPerformerPopup = function (data) {
            vm.performerSearch.sysGroupId = null;
            var teamplateUrl = "coms/constructionTask/performerPopup.html";
            var title = "Danh sách người thực hiện";
            var windowId = "CONSTRUCTION_TASK";
            CommonService.populatePopupOnPopup(teamplateUrl, title, data, vm, windowId, true, '950', '450', null);
        }


        vm.chooseWorkItem = function (data) {
            vm.obj.workItemName = data.name;
            vm.obj.workItemCode = data.code;
            vm.obj.workItemId = data.workItemId;
            vm.catTaskSearch.catWorkItemTypeId = data.catWorkItemTypeId;
            CommonService.dismissPopup1();
            vm.listCatTaskPopupGrid.dataSource.page(1);
            vm.performerPopupGrid.dataSource.page(1);
            vm.obj.taskName = null;
            vm.obj.catTaskCode = null;
            vm.obj.catTaskId = null;
        }

        vm.choosePerformer = function (data) {
            vm.obj.performerName = data.fullName;
            vm.obj.performerIdDetail = data.sysUserId;
            vm.changePerformer.altPerformerName = data.fullName;
            vm.changePerformer.altPerformerId = data.sysUserId;
            CommonService.dismissPopup1();
        }

        vm.choosePerformer1 = function (data) {
            vm.curentObj.taskName = vm.curentObj.title;
            vm.curentObj.oldPerformerId = vm.curentObj.performerId;
            vm.curentObj.performerId = data.sysUserId;
			vm.curentObj.startDate = vm.curentObj.start;
			vm.curentObj.endDate = vm.curentObj.end;

            return Restangular.all("constructionTaskService/construction/updatePerfomer").post(vm.curentObj).then(function (d) {
                if (d.error) {
                    toastr.error(d.error);
                    return;
                }

                toastr.success("Cập nhật dữ liệu thành công")
                //            chinhpxn_20180714_start
                var dataItems = $("#gantt").data("kendoGantt").dataSource._data;
                for (var j = 0; j < dataItems.length; j++) {
                    var dataItem = dataItems[j];
                    if (dataItem.id == vm.curentObj.id) {
                        var row = $("[data-uid='" + dataItem.uid + "']");
                        var perFormer = row.find("td").eq(3);
                        perFormer.html(data.fullName);
                        var parentId = dataItem.id;
                        for (var k = 0; k < dataItems.length; k++) {
                            var d = dataItems[k];
                            if (d.parentId == parentId) {
                                var taskRow = $("[data-uid='" + d.uid + "']");
                                var taskPerFormer = taskRow.find("td").eq(3);
                                taskPerFormer.html(data.fullName);
                            }
                        }
                    }
                }
                //            $("#gantt").data("kendoGantt").dataSource.read();
                //	        chinhpxn_20180714_end
                CommonService.dismissPopup1();


            }).catch(function (e) {
                toastr.error("Có lỗi xảy ra");
                return;
            });;
        }

        vm.chooseCatTask = function (data) {
            vm.obj.taskName = data.name;
            vm.obj.catTaskCode = data.code;
            vm.obj.catTaskId = data.workItemId;
            CommonService.dismissPopup1();
        }


        vm.chooseConstruction = function (data) {
            vm.obj.constructionName = data.name;
            vm.obj.constructionCode = data.code;
            vm.obj.constructionId = data.constructionId;
            vm.constructionTypeId = data.catContructionTypeId;
            vm.workItemSearch.constructionId = data.constructionId;
            vm.workItemSearch.sysGroupId = data.sysGroupId;
            vm.performerSearch.sysGroupId = data.sysGroupId;
            CommonService.dismissPopup1();
            vm.listWorkItemPopupGrid.dataSource.page(1);
            vm.obj.workItemName = null;
            vm.obj.workItemCode = null;
            vm.obj.workItemId = null;
            vm.catTaskSearch.catWorkItemTypeId = null;
            vm.obj.taskName = null;
            vm.obj.catTaskCode = null;
            vm.obj.catTaskId = null;
            //chinhpxn20180713_start
            vm.genTaskName();
            //chinhpxn20180713_end
            
            //tatph-start-27112019
            if(data.checkHTCT != null){
            	 vm.obj.checkHTCT = data.checkHTCT ;
            }else{
            	 vm.obj.checkHTCT = 0;
            }
            if(data.catConstructionTypeId != null){
            	  vm.obj.catConstructionTypeId = data.catConstructionTypeId;
            }else{
            	 vm.obj.catConstructionTypeId = 0;
            }
            if(data.workItemName != null){
          	  vm.obj.workItemName = data.workItemName;
            }else{
          	 vm.obj.workItemName = "";
            }
            //tatph-end-27112019
            
            if(data.listWorkItemName!=null && data.listWorkItemName.length>0){
            	vm.listWorkItemName = data.listWorkItemName;
            } else {
            	vm.listWorkItemName = null;
            }
        }


        function onEdit(e) {
            $("#numerictextbox").kendoNumericTextBox({
                format: "## \\\%"
            });
            $(".k-gantt-delete").text('Xóa');
            $(".k-gantt-update").text('Lưu lại');
            $(".k-gantt-cancel").text('Hủy bỏ');
            $(".k-window-title").text('Cập nhật tiến độ');
        }
        vm.saveConstructionTask = function (obj) {
        	$('#fuckThisShit').addClass('loadersmall');
        	vm.isDisable = true;
            if (validateConstructionTask(obj)) {
            	if(vm.obj.type == 1){
            		if(!getListWorkItemfromGrid()){
            			vm.isDisable = false;
                		return;
                	}
            	}
				//hoanm1_20191018_comment
            	//obj.quantity = obj.quantity * 1000000;
				//hoanm1_20191018_comment
                return Restangular.all("constructionTaskService/construction/addConstructionTaskTC").post(obj).then(function (d) {
                    $("#gantt").data("kendoGantt").dataSource.read();
                    if (d.error) {
                        toastr.error(d.error);
                        $('#fuckThisShit').removeClass('loadersmall');
                        vm.isDisable = false;
                        return;
                    }
                    $('#fuckThisShit').removeClass('loadersmall');
                    toastr.success("Thêm công việc thành công");
                    vm.isDisable = false;
                    getWorkItemForAddingTask();
//                    vm.clearInput('0');
                    vm.clearInput('1');
                    vm.clearInput('2');
                    vm.clearInput('3');
                    vm.clearInput('4');
                    vm.clearInput('5');
                    vm.clearInput('6');
                    vm.obj.workItemName = null;
                    vm.obj.quantity = null;
                    vm.obj.performerName = null;
                    vm.obj.endDate = null;
                    vm.obj.startDate = null;
                    //chinhpxn20180622
                    
                    vm.obj.workItemId = null;
                    vm.obj.quantity = null;
                    vm.obj.taskName = null;
                    vm.obj.performerIdDetail = null;
                    vm.obj.taskName = null;
                    
                    //chinhpxn20180622
                    $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
                }).catch(function (e) {
                    toastr.error("Có lỗi xảy ra");
                    vm.isDisable = false;
                    return;
                });
            } else{
            	$('#fuckThisShit').removeClass('loadersmall');
            	vm.isDisable = false;
            }
        }


        vm.constructionSearch = {}
        vm.listConstructionPopupOptions = kendoConfig.getGridOptions({
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
                    total: function (response) {//return total record of query result
                        vm.countCons = response.total;
                        return response.total; 
                    },
                    data: function (response) {// return query result data
                        return response.data;
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "constructionService/doSearchTTTDCT",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.constructionSearch.page = options.page
                        vm.constructionSearch.pageSize = options.pageSize
                        vm.constructionSearch.keySearch = vm.keySearch;
                        //chinhpxn20180630_start
                        vm.constructionSearch.taskType = vm.obj.type;
                        vm.constructionSearch.catProvinceId = vm.obj.provinceId;
                        //chinhpxn20180630_end
                        return JSON.stringify(vm.constructionSearch)

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
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                {
                    title: "Loại công trình",
                    field: 'catContructionTypeName',
                    width: '10%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Mã công trình",
                    field: 'code',
                    width: '20%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Tên công trình",
                    field: 'name',
                    width: '35%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Trạng thái",
                    field: 'status',
                    width: '10%',
                    template: function (dataItem) {
                        if (dataItem.status == 1) {
                            return "<span name='status' font-weight: bold;'>Chờ bàn giao mặt bằng</span>"
                        } else if (dataItem.status == 2) {
                            return "<span name='status' font-weight: bold;'>Chờ khởi công</span>"
                        } else if (dataItem.status == 3) {
                            return "<span name='status' font-weight: bold;'>Đang thực hiện</span>"
                        } else if (dataItem.status == 4) {
                            return "<span name='status' font-weight: bold;'>Đã tạm dừng</span>"
                        } else if (dataItem.status == 5) {
                            return "<span name='status' font-weight: bold;'>Đã hoàn thành</span>"
                        } else if (dataItem.status == 6) {
                            return "<span name='status' font-weight: bold;'>Đã nghiệm thu</span>"
                        } else if (dataItem.status == 7) {
                            return "<span name='status' font-weight: bold;'>Đã hoàn công</span>"
                        } else if (dataItem.status == 8) {
                            return "<span name='status' font-weight: bold;'>Đã quyết toán</span>"
                        } else if (dataItem.status == 0) {
                            return "<span name='status' font-weight: bold;'>Đã hủy</span>"
                        }
                    },
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Chọn",
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    template: dataItem =>
                        '<div class="text-center">' +
                        '<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate >' +
                        '<i id="#=code#"  ng-click="caller.chooseConstruction(dataItem)"  class="fa fa-check color-green" aria-hidden="true"></i>' +
                        '</a>'
                        + '</div>',
                    width: '10%'
                },
            ]
        });

        vm.listWorkItemPopupOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                        vm.countWorkItem = response.total;
                        return response.total; // total is returned in
                        // the "total" field of
                        // the response
                    },
                    data: function (response) {
                        return response.data; // data is returned in
                        // the "data" field of
                        // the response
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "workItemService/doSearchForTask",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        // vm.constructionSearch.employeeId =
                        // Constant.user.srvUser.catEmployeeId;
                        vm.workItemSearch.page = options.page
                        vm.workItemSearch.pageSize = options.pageSize
                        vm.workItemSearch.keySearch = vm.keySearch;
                        return JSON.stringify(vm.workItemSearch)

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
                    title: "Loại hạng mục",
                    field: 'catWorkItemType',
                    width: '15%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }

                },
                {
                    title: "Mã hạng mục",
                    field: 'code',
                    width: '20%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }

                },
                {
                    title: "Tên hạng mục",
                    field: 'name',
                    width: '30%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Trạng thái",
                    field: 'status',
                    width: '15%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: function (dataItem) {
                        if (dataItem.status == 1) {
                            return "<span name='status' font-weight: bold;'>Chưa thực hiện</span>"
                        } else if (dataItem.status == 2) {
                            return "<span name='status' font-weight: bold;'>Đang thực hiện</span>"
                        } else if (dataItem.status == 3) {
                            return "<span name='status' font-weight: bold;'>Đã hoàn thành</span>"
                        }
                    }
                },
                {
                    title: "Chọn",
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    template: dataItem =>
                        '<div class="text-center">' +
                        '<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate >' +
                        '<i id="#=code#"  ng-click="caller.chooseWorkItem(dataItem)"  class="fa fa-check color-green" aria-hidden="true"></i>' +
                        '</a>'
                        + '</div>',
                    width: '10%'
                },
                {
                    field: 'workItemId',
                    hidden: true
                },
                {
                    field: 'catWorkItemTypeId',
                    hidden: true
                }
            ]
        });

        vm.listCatTaskPopupOptions = kendoConfig.getGridOptions({
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
                        url: Constant.BASE_SERVICE_URL + "constructionService/doSearchCatTask",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.catTaskSearch.page = options.page
                        vm.catTaskSearch.pageSize = options.pageSize;
                        vm.catTaskSearch.keySearch = vm.keySearch;
                        return JSON.stringify(vm.catTaskSearch)

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
                    title: "Hạng mục",
                    field: 'catWorkItemType',
                    width: '15%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }

                },
                {
                    title: "Mã công việc",
                    field: 'code',
                    width: '20%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }

                },
                {
                    title: "Tên công việc",
                    field: 'name',
                    width: '30%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Trạng thái",
                    field: 'status',
                    width: '15%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:right;"
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
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    template: dataItem =>
                        '<div class="text-center">' +
                        '<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate >' +
                        '<i id="#=code#"  ng-click="caller.chooseCatTask(dataItem)"  class="fa fa-check color-green" aria-hidden="true"></i>' +
                        '</a>'
                        + '</div>',
                    width: '10%'
                },
                {
                    field: 'workItemId',
                    hidden: true
                }
            ]
        });


        vm.doSearchPerformer = function () {
            vm.performerPopupGrid1.dataSource.page(1);
        }


        vm.performerOptions1 = kendoConfig.getGridOptions({
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
                        vm.performerSearch.page = options.page
                        vm.performerSearch.pageSize = options.pageSize
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
                        '<i id="#=code#"  ng-click="caller.choosePerformer1(dataItem)"  class="fa fa-check color-green" aria-hidden="true"></i>' +
                        '</a>'
                        + '</div>',
                    width: '10%'
                },
                {
                    field: 'sysUserId',
                    hidden: true
                }
            ]
        });

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
                        vm.performerSearch.page = options.page
                        vm.performerSearch.pageSize = options.pageSize
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
                },
                {
                    field: 'sysUserId',
                    hidden: true
                }
            ]
        });
        // ============================End============================












        $scope.taskAll = 0;
        $scope.taskSlow = 0;
        $scope.taskPause = 0;
        $scope.taskUnfulfilled = 0;
        vm.granttSearch = {
            keyword: undefined
        };
        function getCountConstruction() {
            var obj = {};
            obj.month = $scope.monthGrantt;
            obj.year = $scope.yearGrantt;
            return Restangular.all("constructionTaskService/construction/getCountConstruction").post(obj).then(function (d) {
                $scope.taskAll = d.taskAll;
                $scope.taskUnfulfilled = d.taskUnfulfilled;
                $scope.taskSlow = d.taskSlow;
                $scope.taskPause = d.taskPause;
            }).catch(function (e) {
                return;
            });
        }
        function init() {
            $scope.viewGrantt = false;
            $scope.monthSelectorOptions = {
                start: "year",
                depth: "year"
            };

        }

        function editWorkItem(dataItem) {
            constructionTaskService.getListImageById(dataItem).then(function (data) {
            	if (data.listImage.length > 0) {
                    vm.workItemSearch.listImage = data.listImage;
                    vm.changeImage(vm.workItemSearch.listImage[0]);
            	} else {
            		vm.workItemSearch.listImage = [];
            	}
            }).catch(function (err) {
                vm.workItemSearch.listImage = [];
            });
            var templateUrl = "coms/constructionTask/popup-detail-workItem.html";
            var title = "Hình ảnh";
            var windowId = "DETAIL_WORKITEM_CONFIRM";
            vm.popUpOpen = 'DETAIL_WORKITEM_CONFIRM';
            CommonService.populatePopupCreate(templateUrl, title, vm.workItemSearch, vm, windowId, false, '1000', 'auto', "null");
        }

        var tasksDataSource = new kendo.data.GanttDataSource({
            batch: false,
            transport: {
                read: {
                    url: Constant.BASE_SERVICE_URL + "constructionTaskService/constructionTask/getDataForGrant",
                    contentType: "application/json; charset=utf-8",
                    type: "POST"
                },
                update: {
                    url: Constant.BASE_SERVICE_URL + "constructionTaskService/constructionTask/updateCompletePercent",
                    contentType: "application/json; charset=utf-8",
                    type: "POST"
                },
                destroy: {
                    url: Constant.BASE_SERVICE_URL + "constructionTaskService/constructionTask/deleteGrantt",
                    contentType: "application/json; charset=utf-8",
                    type: "POST"
                },
                create: {
                    url: Constant.BASE_SERVICE_URL + "constructionTaskService/constructionTask/createTask",
                    contentType: "application/json; charset=utf-8",
                    type: "POST"
                },
                parameterMap: function (options, operation) {
                	//console.log(this);
                	//console.log("sending....")
                    if (operation !== "read") {
                        var obj = { models: kendo.stringify(options.models || [options]) };
                        var strObj = {};
                        strObj.id = options.id;
                        strObj.parentID = options.parentID;
                        strObj.orderID = options.orderID;
                        strObj.title = options.title;
                        strObj.start = options.start;
                        strObj.end = options.end;
                        strObj.percentComplete = options.percentComplete;
                        strObj.summary = options.summary;
                        strObj.expanded = options.expanded;
                        strObj.levelId = options.levelId;
                        strObj.performerId = options.performerId;
                        //                        hoanm1_20180612_start
                        strObj.type = options.type;
                        strObj.constructionId = options.constructionId;
                        strObj.quantity = options.quantity;
                        strObj.workItemId = options.workItemId;
                        strObj.taskOrder = options.taskOrder;
                        //                        hoanm1_20180612_end
                        //                        hoanm1_20180723_start
                        strObj.sysGroupId = options.sysGroupId;
                        //                        hoanm1_20180723_end
                        return JSON.stringify(strObj);
                    } else if (operation === "read") {
                        var obj = {};
                        obj.month = $scope.monthGrantt;
                        obj.year = $scope.yearGrantt;
                        obj.keySearch = vm.granttSearch.keyword;
                        obj.catProvinceCode = vm.granttSearch.catProvinceCode;
                        obj.status = $scope.status;
                        obj.complete_state = $scope.complete_state;
                        return JSON.stringify(obj);
                    }
                }
            },
            schema: {
                model: {
                    id: "id",
                    fields: {
                        id: { from: "id", type: "number" },
                        orderId: { from: "orderID", type: "number", validation: { required: true } },
                        parentId: { from: "parentID", type: "number", defaultValue: null, validation: { required: true } },
                        start: { from: "start", type: "date" },
                        end: { from: "end", type: "date" },
                        title: { from: "title", defaultValue: "", type: "string" },
                        percentComplete: { from: "percentComplete", type: "number" },
                        summary: { from: "summary", type: "boolean" },
                        status: { from: "status", type: "number" },
                        performerName: { from: "fullname", type: "string" },
                        performerId: { from: "performerId", hidden: "true", type: "number" },
                        checkProgress: { from: "checkProgress", hidden: "true", type: "number" },
                        expanded: { from: "Expanded", type: "boolean", defaultValue: false }
                        //                        hoanm1_20180612_start
                        , type: { from: "type", hidden: "true", type: "string" }
                        , constructionId: { from: "constructionId", hidden: "true", type: "number" }
                        , quantity: { from: "quantity", hidden: "true", type: "number" }
                        , workItemId: { from: "workItemId", hidden: "true", type: "number" }
                        , taskOrder: { from: "taskOrder", hidden: "true", type: "string" }
                        //                        hoanm1_20180612_end
                        //                        hoanm1_20180723_start
                        , sysGroupId: { from: "sysGroupId", hidden: "true", type: "number" }
                        //                        hoanm1_20180723_end
                    }
                },
                data: function (response) {
                    return response;
                }
            }
        });

        function onDataBound(e) {
            $("[data-title='Tiến độ (%)']").css("white-space", "normal");
            $("[data-title='Tiến độ (%)']").css("text-align", "center");
            $("[data-title='Tên CV']").css("text-align", "center");
            $("[data-title='Người TH']").css("text-align", "center");
			//  hoanm1_20181210_start
			$("[data-title='Check vật tư']").css("white-space", "normal");
            $("[data-title='Check vật tư']").css("text-align", "center");
			//  hoanm1_20181210_end
            
            var ganttList = e.sender.list;
            var dataItems = ganttList.dataSource.view();
            var count = 0;
            //huypq_20181009_start
            var cell = $("[data-level='" + 2 + "']").find("td");
            var cell2 = $("[data-level='" + 3 + "']").find("td");
             //huypq_20181009_end
            for (var j = 0; j < dataItems.length; j++) {
//            	hoanm1_20180906_start
            	if(vm.needExpand && dataItems[j].levelId <3 && dataItems[j].expanded != false){
            		dataItems[j].set("expanded",true);
            	}
            	if(dataItems[j].levelId == 3 && dataItems[j].isInteral == 2){
            		$("#gantt").find("tr[data-uid='" + dataItems[j].uid + "']").addClass('rowColor');
            	}
            	if(dataItems[j].levelId == 3 && dataItems[j].completeState == 2){
            		$("#gantt").find("tr[data-uid='" + dataItems[j].uid + "']").children().find(".k-i-expand").addClass('lateTaskClosed');
            		$("#gantt").find("tr[data-uid='" + dataItems[j].uid + "']").children().find(".k-i-collapse").addClass('lateTaskExpanded');
            	}
//            	hoanm1_20180906_end
            //huypq_20181009_start
//            	$(".rowColor2").css({'background-color': '#e0ebeb'});
            //huypq_20181009_end
                var dataItem = dataItems[j];
                var row = $("[data-uid='" + dataItem.uid + "']");
				//hoanm1_20190104_start
                row.find("td").eq(4).unbind('dblclick');
                row.find("td").eq(4).dblclick(function (e) {
				//hoanm1_20190104_end
                    //                	chinhpxn20180720_start
                    if (vm.curentObj.workItemStatus == '3') {
                        toastr.error("Hạng mục đã hoàn thành không được phép chuyển người!")
                        return false;
                    } else {
                        vm.openPerformerPopup();
                        return false;
                    }
                    //                	chinhpxn20180720_end
                });

                // tuannt_20180806_start
                row.find("td").eq(1).unbind('dblclick');
                row.find("td").eq(1).dblclick(function (e) {
                    $timeout(function () {
                        switch (vm.curentObj.levelId) {
                            case 2:
                                CommonService.goTo("CONSTRUCTION");
                                $timeout(function () {
                                    $rootScope.$broadcast("constructionEdit", vm.curentObj);
                                }, 200);
                                return false;
                                break;
                            case 3:
                                editWorkItem(vm.curentObj);
                                return false;
                                break;
                            case 4:
                                editWorkItem(vm.curentObj);
                                return false;
                                break;
                            default:
                                return false;
                                break;
                        }
                    });
                });
                // tuannt_20180806_end

                //chinhpxn20180706_start
                if (dataItem.start == null) {
                    var start = row.find("td").eq(4);
                    start.html("");
                }
                if (dataItem.end == null) {
                    var end = row.find("td").eq(5);
                    end.html("");
                }
                //chinhpxn20180706_end

                row.find("td").eq(3).attr('uib-tooltip', 'Click đúp để chọn người TH')
                row.find("td").eq(0).addClass("text-center");
                row.find("td").eq(2).addClass("text-center");

                //                chinhpxn20180704_start
                if (dataItem.levelId == 1) {
                    var start = row.find("td").eq(4);
                    start.html("");
                    var end = row.find("td").eq(5);
                    end.html("");
                }
                if (dataItem.levelId == 2) {
                    count = count + 1;
                    var stt = row.find("td").eq(0);
                    stt.html(count);
                } else {
                    var stt = row.find("td").eq(0);
                    stt.html("");
                }
                //                chinhpxn20180704_end

                var span = row.find("td").eq(1).find("span").eq(dataItem.levelId - 1);
                if (dataItem.levelId == 1) {
                    if (dataItem.expanded) {
                        span.addClass("gantt-expanded-lv-1");
                    } else {
                        span.addClass("gantt-close-lv-1");
                    }
                } 
                //                huypq_20181009_start
                else if (dataItem.levelId == 2 ) {
                //                huypq_20181009_end
                    if (dataItem.expanded) {
                        span.addClass("gantt-expaned-lv2");
                    } else {
                        span.addClass("gantt-close-lv2");
                    }
            }
//                huypq_20181009_start
                else if (dataItem.levelId == 3) {
                    if (dataItem.expanded) {
                        span.addClass("gantt-expanded-lv2");
                       
                    } else {
                        span.addClass("gantt-close-lv2");
                    } 
                //                huypq_20181009_end
                } else if (dataItem.levelId == 4) {
                    if (dataItem.checkProgress == 2) {
                        span.removeClass("k-i-none");
                        span.addClass("gantt-dot-red-lv4");
                    } else {
                        span.removeClass("k-i-none");
                        span.addClass("gantt-dot-lv4");
                    }
                }
            }
            var dateNow = new Date();
            var gantt = this;
            gantt.element.find(".k-task").each(function (e) {
                var dataItem = gantt.dataSource.getByUid($(this).attr("data-uid"));
                if (dataItem.levelId == 4) {
                    // $scope.taskAll++;
                    if (dataItem.status === 3) {
                        // $scope.taskPause++;
                    }
                    if (dataItem.status === 1) {
                        // $scope.taskUnfulfilled++;
                    }
                    if (dataItem.checkProgress == 1 && dataItem.levelId == 4) {
                        // $scope.taskSlow++;
                        this.style.backgroundColor = "#FF2121";
                    };
                }
            });
        }

        vm.ganttOptions = {
            dataSource: tasksDataSource,
            views: [
                "day",
                {
                    type: "week",
                    weekHeaderTemplate: "#=kendo.toString(start, 'ddd dd/MM')# - #=kendo.toString(kendo.date.addDays(end, -1), 'ddd dd/MM')#",
                    dayHeaderTemplate: kendo.template("#=kendo.toString(start, 'ddd dd/MM')#"),
                    selected: true
                },
                { type: "month", weekHeaderTemplate: "#=kendo.toString(start, 'ddd dd/MM')# - #=kendo.toString(kendo.date.addDays(end, -1), 'ddd dd/MM')#" }
            ],
            columns: [
                { field: "id", title: "STT",width: 40 },
                { field: "title", title: "Tên CV", editable: false, width: 200 },
                { field: "percentComplete", title: "Tiến độ (%)", editable: false, width: 60, format: "{0:n0}" },
                { field: "checkStockVT", title: "Check vật tư", editable: false, width: 60,format: "{0:n0}"  },
				{ field: "performerName", title: "Người TH", editable: false, width: 120 },
                { field: "start", title: "Dự kiến khởi công", format: "{0:dd/MM/yyyy}", width: 80, template: "#if(start === null){# '' #}else{# #=start#  #}#" },
                { field: "end", title: "Dự kiến hoàn thành", format: "{0:dd/MM/yyyy}", width: 80, template: "#if(end === null){# '' #}else{# #=end#  #}#" }
            ],
            height: 700,
            showWorkHours: false,
            showWorkDays: false,
            snap: false,
            resizable: true,
            toolbar: [],
            rowHeight: 35,
            tooltip: {
                visible: true,
                template: $("#myTemplate").html()
            },
            taskTemplate: $("#template").html(),
            edit: onEdit,
            save: onSave,
            editable: {
                template: $("#editor").html()
            },
            change: function (e) {
                var gantt = e.sender;
                var selection = gantt.select();
                if (selection.length) {
                    vm.curentObj = gantt.dataItem(selection);
                }
            },
            dataBinding: onDataBinding,
            dataBound: onDataBound
        };


        function onDataBinding(e) {

        }

        vm.needExpand = false
        // dataBound: onDataBound
        vm.searchGrantt = function () {
            vm.ganttOptions.dataSource.read();
        };

        vm.viewGrantAll = function () {
        	vm.needExpand = false;
            $scope.viewGrantAll = true;
            $scope.complete_state = null;
            $scope.status = null;
			$scope.keyword = vm.granttSearch.keyword;
			$scope.catProvinceCode = vm.granttSearch.catProvinceCode;
			
            vm.ganttOptions.dataSource.read();
            getCountConstruction();
        }

/*        vm.viewGrantUnfulfilled = function () {
        	vm.needExpand = true;
            $scope.viewGrantUnfulfilled = true;
            $scope.status = 1;
            $scope.complete_state = null;

            vm.ganttOptions.dataSource.read();
            getCountConstruction();
        }*/

        vm.viewGrantProgress = function () {
        	vm.needExpand = true;
            $scope.viewGrantProgress = true;
            $scope.complete_state = 2;
            $scope.status = null;
			$scope.keyword = vm.granttSearch.keyword;
			$scope.catProvinceCode = vm.granttSearch.catProvinceCode;

            vm.ganttOptions.dataSource.read();
            getCountConstruction();
        }

/*        vm.viewGrantStop = function () {
        	vm.needExpand = true;
            $scope.viewGrantStop = true;
            $scope.status = 3;
            $scope.complete_state = null;

            vm.ganttOptions.dataSource.read();
            getCountConstruction();
        };*/

        vm.viewGrantt = function () {
        //	console.log("view");
        	vm.needExpand = false;
            var obj = vm.dateGrantt.split(" ");
            $scope.viewGrantt = true;
            $scope.monthGrantt = undefined;
            $scope.yearGrantt = obj[1];
            switch (obj[0]) {
                case "January":
                    $scope.monthGrantt = 1;
                    break;
                case "February":
                    $scope.monthGrantt = 2;
                    break;
                case "March":
                    $scope.monthGrantt = 3;
                    break;
                case "April":
                    $scope.monthGrantt = 4;
                    break;
                case "May":
                    $scope.monthGrantt = 5;
                    break;
                case "June":
                    $scope.monthGrantt = 6;
                    break;
                case "July":
                    $scope.monthGrantt = 7;
                    break;
                case "August":
                    $scope.monthGrantt = 8;
                    break;
                case "September":
                    $scope.monthGrantt = 9;
                    break;
                case "October":
                    $scope.monthGrantt = 10;
                    break;
                case "November":
                    $scope.monthGrantt = 11;
                    break;
                case "December":
                    $scope.monthGrantt = 12;
                    break;
            }

           // vm.ganttOptions.dataSource.read();
            getCountConstruction();

        };




        init();


        // function refreshGrantt() {
        // var gantt = $("div [kendo-gantt]").data("kendoGantt");
        // if (gantt) {
        // gantt.refresh();
        // }
        // }
        function validateConstructionTask(obj) {
            if ((obj.constructionId == undefined || obj.constructionId == '') && (obj.type != 6)) {
                toastr.warning("Công trình không được để trống!")
                return false;
            } else
                 if ((obj.taskName == undefined || obj.taskName == '') && (obj.type != 1)) {
                    toastr.warning("Công việc không được để trống!")
                    return false;

                } else if ((obj.performerIdDetail == undefined || obj.performerIdDetail == '') ) {
                    toastr.warning("Người thực hiện không được để trống!")
                    return false;

                } else if ((obj.startDate == undefined || obj.startDate == '') && (obj.type != 1)) {
                    toastr.warning("Thời gian từ ngày không được để trống!")
                    return false;

                } else if ((obj.endDate == undefined || obj.endDate == '') && (obj.type != 1)) {
                    toastr.warning("Thời gian đến ngày không được để trống!")
                    return false;
                }
                //chinhpxn20180713_start
                else if ((obj.quantity == undefined || obj.quantity == '') && obj.type == 3) {
                    toastr.warning("Giá trị không được để trống!")
                    return false;
                }
                //chinhpxn20180713_end
            	//nhantv 20180830 start
	            if ((obj.quantity == undefined || obj.quantity == '') && obj.type == 2) {
	                toastr.warning("Sản lượng không được để trống!")
	                return false;
	            }
	            //nhantv 20180830 start
                //chinhpxn20182206_start
                else if ((kendo.parseDate(obj.startDate, "dd/MM/yyyy") > kendo.parseDate(obj.endDate, "dd/MM/yyyy")) && (obj.type != 1)) {
                    toastr.warning("Thời gian đến không nhỏ hơn thời gian bắt đầu!")
                    return false;
                }
            //chinhpxn20182206_end
	            if(obj.type == 1 || obj.type == 3){
	            	if(obj.sourceWork==null){
	            		toastr.warning("Nguồn việc của công trình không được để trống !");
            			return false;
	            	} else {
	            		if(obj.sourceWork==7 && obj.constructionTypeNew!=6){
	            			toastr.warning("Loại công trình của nguồn việc: Xây lắp - Trung tâm xây dựng phải là: Các công trình Ngoài tập đoàn !");
	            			return false;
	            		}
	            		if(obj.checkHTCT==1){
		            		if(obj.sourceWork!=4 && obj.sourceWork!=5){
		            			toastr.warning("Nguồn việc của công trình HTCT chỉ được nhập Xây dựng móng hoặc Hoàn thiện !");
		            			return false;
		            		}
		            	} else {
		            		if(obj.sourceWork==4 || obj.sourceWork==5){
		            			toastr.warning("Nguồn việc Xây dựng móng và Hoàn thiện chỉ ứng với công trình HTCT !");
		            			return false;
		            		}
		            	}
	            	}
	            	
	            	if(obj.constructionTypeNew==null){
	            		toastr.warning("Mã công trình của công trình không được để trống !");
            			return false;
	            	}
	            }
	            
            return true;
        }

        function changeTaskType() {
            // clear data when change type
            vm.obj.workItemName = null;
            vm.obj.workItemCode = null;
            vm.obj.workItemId = null;
            vm.catTaskSearch.catWorkItemTypeId = null;
            vm.obj.taskName = null;
            vm.obj.catTaskCode = null;
            vm.obj.catTaskId = null;
            vm.obj.sourceWork = null;
            vm.obj.constructionType = null;
            vm.obj.constructionCode = null;
        }
        function onSave(e) {
            var gantt = e.sender;
            var selection = gantt.select();

            if (selection.length) {
                toastr.success("Cập nhập thành công!")
                console.log("Task saved :: " + gantt.dataItem(selection).title);
                return;
            } else {
                toastr.error("Có lỗi xảy ra!")
            }
        }

        getMonthYear();
        function getMonthYear() {
        	var dateNow = new Date();
        	var months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
        	vm.dateGrantt = months[dateNow.getMonth()] + " " + dateNow.getFullYear();
        	vm.viewGrantt();
        }
        
        function changeImage(image) {
        	vm.imageSelected = image;
		}
        
        //nhantv begin
        //popup gan hang muc
        vm.addWorkItem = addWorkItem;
        function addWorkItem() {
        	if(vm.obj.checkHTCT != null && vm.obj.checkHTCT){
        		toastr.warning("Công trình đã chọn thuộc hạ tầng cho thuê , không được gán hạng mục");
        		return;
        	}
        	if(vm.obj.catConstructionTypeId != null){
        		if(vm.obj.catConstructionTypeId ==3 && vm.listWorkItemName != null){
        			toastr.warning("Công trình đã chọn là công trình GPON và đã tồn tại hạng mục , không gán");
            		return;
        		}else if(vm.obj.catConstructionTypeId ==3 && vm.listWorkItemName==null){
        			toastr.warning("Công trình đã chọn là công trình GPON .Để gán hạng mục : Quản lý công trình -> Thông tin công trình -> Xem chi tiết -> Hạng mục -> Tạo mới");
            		return;
        		}
        		
        	}
        	  vm.workItemRecord = { workItemTypeList: [] };
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
        /**
         * nhantv
         * lay thong tin don vi, dan sach hang muc
         */
        function prepareDataforAssign(id) {
        	constructionTaskService.getSysGroupInfo(id).then(function (data) {
                vm.workItemRecord.constructorName1 = data.name;
                vm.workItemRecord.constructorId = data.groupId;
                vm.workItemRecord.supervisorName = data.name;
                vm.workItemRecord.supervisorId = data.groupId;
            })
            
            var wiSearch = {
        		id:vm.obj.constructionId,
        		catConstructionTypeId:vm.constructionTypeId
        	};
            constructionTaskService.getCatWorkItemType(wiSearch).then(function (data) {
                vm.catWorkItemTypeList = data;
            }, function (e) {
                toastr.error("Có lỗi trong quá trình lấy dữ liệu.");
            });
            vm.constrObj = {};
            vm.constrObj.constructionId = vm.obj.constructionId;
            vm.constrObj.code = vm.obj.constructionCode;
        }
        
        /**
         * nhantv
         * load lai danh sach hang muc cua cong trinh sau khi gan hang muc
         */
        vm.assignCallback = function(){
        	$('div[data-role="draggable"]').each(function(index,element){
        	    var index = $(element).css('z-index');
        	    if(index == 10005){
        	    	$(element).find(".k-icon.k-i-close").click();
        	    }
        	});
        	getWorkItemForAddingTask();
        }
        
        function getWorkItemForAddingTask(){
        	if(!!$('#workItemAddingGrid').data('kendoGrid')){
	        	$('#workItemAddingGrid').data('kendoGrid').dataSource.read();
	        	$('#workItemAddingGrid').data('kendoGrid').refresh();
	        }
        }   
        
        //disable button gan hang muc neu chua chon cong trinh
        $scope.$watch('vm.obj.constructionId', function(){
        	vm.isDisableAssign = !!vm.obj.constructionId;
        	getWorkItemForAddingTask();
        })
        
        //file chua function phuc vu popup gan hang muc
        workItemDetailFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
                kendoConfig, $kWindow, constructionTaskService,
                CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm);

        
        //file chua function phuc vu popup chuyen nguoi
        changePerformerFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
                kendoConfig, $kWindow, constructionTaskService,
                CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm);
        
        vm.gridOptions = kendoConfig.getGridOptions({
        	editable:true,
			autoBind: true,
			scrollable: false, 
			resizable: true,
			dataBinding: function() {
                record = (this.dataSource.page() -1) * this.dataSource.pageSize();
            },
            reorderable: true,
			dataSource: {
	        	autoSync: true,    
				serverPaging: false,
				schema: {
					type:"json",
					 model: {
		                 	fields: {
		                 		stt: {editable: false},
		                 		name: {editable: false},
		                 		perFormerName: { editable: true },
		                 		quantity:  { type: "number", format: "{0:c}", editable: true },
		                 		startDate: {editable: true},
		                 		endDate: {editable: true},
		                 	}
		                 },
					 total: function (response) {
						    $("#cntCount").text(""+response.total);
							return response.total; 
						},
					data: function (response) {				
						for(var x in response.data){
							response.data[x].sysUserId=Constant.userInfo.vpsUserToken.sysUserId
						}
						return response.data; 
					},
	            },
				transport: {
					read: {
						url: Constant.BASE_SERVICE_URL +"constructionTaskService/getWorkItemForAddingTask",
						contentType: "application/json; charset=utf-8",
						type: "POST"
					},					
					parameterMap: function (options, type) {
						    vm.workItemSearch.constructionId = vm.obj.constructionId;
						    vm.workItemSearch.month = $scope.monthGrantt;
					    	vm.workItemSearch.year = $scope.yearGrantt;
							return JSON.stringify(vm.workItemSearch)
					}
				},					 
				pageSize: 10
			},
			noRecords: true,
			columnMenu: false,
			messages: {
				noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
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
				field:"stt",
				width: '5%',
				template: "<input type='checkbox' id='checkWorkItem' name='gridcheckbox' ng-click='caller.handleCheckForWorkItem(dataItem)' ng-model='dataItem.selected'/>",
		        columnMenu: false,
		        headerAttributes: {style: "text-align:center;"},
				attributes: {style: "text-align:center;"},
			},{
				title: "Hạng mục",
		        field: 'name',
		        width: '25%',
		        headerAttributes: {
					style: "text-align:center; font-weight: bold;",
					translate:"",
				},
				attributes: {
					style: "text-align:left;",
					readonly:"readonly"
				},
			},{
				title: "Đối tác",
		        field: 'partnerName',
		        width: '25%',
		        headerAttributes: {
					style: "text-align:center; font-weight: bold;",
					translate:"",
				},
				attributes: {
					style: "text-align:left;",
					readonly:"readonly"
				},
			},
			{
				title: "Sản lượng <br>(Triệu VND)",
		        field: 'quantity',
		        width: '15%',
		        headerAttributes: {
		        	style: "text-align:center; font-weight: bold;"
				},
				attributes: {style: "text-align:left;"},
				format: "{0:n2}"
			},{
				title: "Từ ngày",
		        field: 'startDate',
		        width: '15%',
		        template: dataItem =>!!dataItem.startDate ? kendo.toString(kendo.parseDate(dataItem.startDate), 'dd/MM/yyyy') : "",
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
			},{
				title: "Đến ngày",
		        field: 'endDate',
		        template: dataItem =>!!dataItem.endDate ? kendo.toString(kendo.parseDate(dataItem.endDate), 'dd/MM/yyyy') : "",
		        width: '15%',
		        headerAttributes: {
		        	style: "text-align:center; font-weight: bold;",
				},
				attributes: {style: "text-align:center;"},
				editor: function (container, options) {
				    var input = $("<input id = 'kendoDate'  ng-field = 'endDate' ng-blur ='caller.validateDateCell(dataItem)'/>");
				    input.attr("name", options.field);
				    input.appendTo(container);
				    input.kendoDatePicker({format:"dd/MM/yyyy"});
			    },
			}]
		});
        
     // Autocomplete trường người thực hiện
		function manufacturerAutocomplete(container, options) {
			vm.manufacturer = {};
			$('<input id= "selectedItem" data-bind="value:performerName"/>')
				.appendTo(container)
				.kendoAutoComplete({
					select: function (e) {
						var dataItem = this.dataItem(e.item.index());
						vm.performerName = dataItem.fullName;
						vm.performerIdDetail = dataItem.sysUserId;
						options.model.performerName = dataItem.fullName;
						options.model.performerIdDetail = dataItem.sysUserId;
					},
					dataSource: {
						serverFiltering: true,
						transport: {
							read: function (options) {
								var keySerch = $("#selectedItem").val();
								return Restangular.all("constructionTaskService/construction/searchPerformer").
								post({
			                          keySearch: keySerch,
			                          pageSize: vm.catTaskOptions1.pageSize*10,
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
					change: function (e) {
						if (e.sender.value() === '') {
							vm.performerName = null;
							vm.performerIdDetail = null;
						}
					},
					suggest: false,
					filter: "contains",
					valuePrimitive:true,
					dataTextField: "fullName",
					dataTextValue: "sysUserId",
					minLength: 1
				});
			var autoComplete = $("#selectedItem").data("kendoAutoComplete");
			autoComplete.list.width(300);
		}
		
		vm.validateDateCell = function(obj){
			var attr = $("#kendoDate").attr('ng-field');
			var attrName = attr =='startDate'? 'Ngày bắt đầu' : 'Ngày kết thúc';
            var date = kendo.parseDate(obj[attr]);
            if(!$("#kendoDate").val()){
            	obj[attr] = "";
            	return;
            }
            if (!date) {
            	$("#kendoDate").focus();
            	$("#kendoDate").val('');
            	obj[attr] = '';
            	toastr.error(attrName+" không hợp lệ!");
            	return false;
            }
            if(!!obj.startDate && !!obj.endDate && obj.startDate>obj.endDate){
            	$("#kendoDate").focus();
            	toastr.error("Ngày bắt đầu không được lớn hơn ngày kết thúc!");
            	return false;
            }
            return true;
		};
		
		function getListWorkItemfromGrid(){
			vm.obj.childDTOList = [];
			var listWorkItem = $("#workItemAddingGrid").data().kendoGrid.dataSource.data();
			
			vm.obj.performerId = vm.obj.performerIdDetail;
			var table = $("#workItemAddingGrid  table")[0];
			var isQuantityValid = true;
			var isStartDateValid = true;
			var isEndDateValid = true;
			var isPerformerValid = true;
			for(var i = 0; i<listWorkItem.length; i++){
				if(listWorkItem[i].selected == true){
					var row = i+1;
					if ((listWorkItem[i].quantity == undefined || listWorkItem[i].quantity == '')) {
						$(table.rows[row].cells[3]).addClass("k-state-focused");
						$(table.rows[row]).addClass("k-state-selected");
						isQuantityValid = false;
	                }
					
					if ((!listWorkItem[i].startDate)) {
						$(table.rows[row].cells[4]).addClass("k-state-focused");
						$(table.rows[row]).addClass("k-state-selected");
						isStartDateValid = false;
	                }
					if ((!listWorkItem[i].endDate)) {
						$(table.rows[row].cells[5]).addClass("k-state-focused");
						$(table.rows[row]).addClass("k-state-selected");
						isEndDateValid = false;
	                }
					listWorkItem[i].taskOrder = vm.obj.taskOrder;
					listWorkItem[i].constructionId = vm.obj.constructionId;
					listWorkItem[i].constructionName = vm.obj.constructionName;
					listWorkItem[i].constructionCode = vm.obj.constructionCode;
					listWorkItem[i].workItemName = listWorkItem[i].name;
					listWorkItem[i].type = vm.obj.type;
					listWorkItem[i].performerId = vm.obj.performerIdDetail;
					listWorkItem[i].performerIdDetail = vm.obj.performerIdDetail;
					listWorkItem[i].endDate =kendo.toString(kendo.parseDate(listWorkItem[i].endDate,'dd/MM/yyyy'), 'dd/MM/yyyy');
					listWorkItem[i].startDate =kendo.toString(kendo.parseDate(listWorkItem[i].startDate,'dd/MM/yyyy'), 'dd/MM/yyyy')
		        	vm.obj.childDTOList.push(listWorkItem[i]);
				}
			}
			if(!isStartDateValid)
				toastr.warning("Thời gian từ ngày không được để trống!")
			if(!isEndDateValid)
				toastr.warning("Thời gian từ ngày không được để trống!")
			if(!isPerformerValid)
				toastr.warning("Người thực hiện không được để trống!")
			if(!isQuantityValid)
				toastr.warning("Sản lượng không được để trống!")
				
			if(!vm.obj.childDTOList || vm.obj.childDTOList.length==0){
				toastr.warning("Chưa có hạng mục nào được chọn!")
				return false;
			}
			return isPerformerValid && isStartDateValid && isEndDateValid && isPerformerValid;
		}
		
		 vm.provinceOptions = {
		            dataTextField: "name",
		            dataValueField: "workItemId",
		            select: function (e) {
		                vm.selectedDept1 = true;
		                var data = this.dataItem(e.item.index());
		                vm.obj.provinceName = data.name;
		                vm.obj.provinceCode = data.code;
		                vm.obj.provinceId = data.catProvinceId;
		            },
		            pageSize: 10,
		            dataSource: {
		                serverFiltering: true,
		                transport: {
		                    read: function (options) {
		                        return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
		                        	name: vm.obj.provinceName,
		                            pageSize: vm.catTaskOptions.pageSize,
		                            page: 1
		                        }).then(function (response) {
		                            options.success(response.data);
		                        }).catch(function (err) {
		                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
		                        });
		                    }
		                }
		            },
		            template: '<div class="row" ><div class="col-xs-5" style="float:left;word-wrap: break-word;">#: data.code #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden"> #: data.name #</div> </div>',
		            change: function (e) {
		                if (e.sender.value() === '') {
		                    vm.obj.taskName = null;
		                    vm.obj.catTaskCode = null;
		                    vm.obj.catTaskId = null;
		                }
		            },
		            ignoreCase: false
		        }
		 
		vm.openCatProvincePopup = openCatProvincePopup;
        function openCatProvincePopup(){
    		var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm tỉnh");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
        }
        vm.onSaveCatProvince = onSaveCatProvince;
        function onSaveCatProvince(data){
            vm.obj.provinceName = data.name;
            vm.obj.provinceCode = data.code;
            vm.obj.provinceId = data.catProvinceId;
            htmlCommonService.dismissPopup();
            $("#province").focus();
	    };
		
		//lưu đơn vị khi thêm hạng mục
		vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'dept') {
                vm.constructionSearch.sysGroupName = data.text;
                vm.constructionSearch.sysGroupId = data.id;
            } else
                if (vm.departmentpopUp === 'deptWork') {
                    vm.workItemRecord.constructorName1 = data.text;
                    vm.workItemRecord.constructorId = data.id;
                } else
                    if (vm.departmentpopUp === 'sup') {
                        vm.workItemRecord.supervisorName = data.text;
                        vm.workItemRecord.supervisorId = data.id;
                    } else
                        if (vm.departmentpopUp === 'part') {
                            vm.workItemRecord.constructorName2 = data.text;
                            vm.workItemRecord.constructorId = data.id;
                        } else if (vm.departmentpopUp === 'dept1') {
                            vm.constrObj.sysGroupName = data.text;
                            vm.constrObj.sysGroupId = data.id;
                        }
        }
        
        vm.closeChangePerformerPopup = function () {
            CommonService.dismissPopup1();
            vm.clearInput('0');
            vm.clearInput('1');
            vm.clearInput('2');
            vm.clearInput('3');
            vm.clearInput('4');
            vm.clearInput('5');
            vm.clearInput('6');
            vm.clearInput('7');
            vm.clearInput('8');
            vm.clearInput('9');
            vm.changePerformer.priority = 1;
            $(".k-icon.k-i-close").click();
        }
        
        function saveImportedTask(data, taskType){
        	for(var i =0;i< data.length; i++){
        		data[i].month =$scope.monthGrantt;
        		data[i].year = $scope.yearGrantt;
        		data[i].performerIdDetail = data[i].performerId
        		if(taskType == 2){
        			data[i].taskName = 'Làm HSHC cho công trình ' + data[i].constructionCode;
        			data[i].type = taskType;
        		} else if(taskType == 3){
        			if (data[i].taskOrder == 1) {
        				data[i].taskName = "Tạo đề nghị quyết toán cho công trình " +  data[i].constructionCode;
                    } else {
                    	data[i].taskName = "Lên doanh thu cho công trình " +  data[i].constructionCode;
                    }
        			data[i].type = taskType;
        			data[i].quantity = data[i].quantity*1000000;
        		}
        			//Huypq-20190523-start
        		else if(taskType == 4){
//        			data[i].taskName = 'Thi công ngoài OS cho công trình ' + data[i].constructionCode;
        			data[i].type = taskType;
        		}
        	}
        	var obj = {childDTOList: data};
        	return Restangular.all("constructionTaskService/saveListTaskFromImport").post(obj).then(function (d) {
                $("#gantt").data("kendoGantt").dataSource.read();
                if (d.error) {
                    toastr.error(d.error);
                    return;
                }
                debugger;
                console.log("Thêm công việc thành công");
//                toastr.success("Thêm công việc thành công");
                toastr.success("Thêm công việc thành công");
                cancelImport();
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
            }).catch(function (e) {
                toastr.error("Có lỗi xảy ra");
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                return;
            });
        }
        //nhantv end
    }

    $(document).bind("kendo:skinChange", function () {
        var gantt = $("div [kendo-gantt]").data("kendoGantt");
        if (gantt) {
            gantt.refresh();
        }
    });

})();
