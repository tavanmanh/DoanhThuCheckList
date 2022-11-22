(function () {
    'use strict';
    var controllerId = 'woDetailsController';

    angular.module('MetronicApp').controller(controllerId, woDetailsController);

    function woDetailsController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter, $modal, $sce, $compile,
                                 kendoConfig, $kWindow, woDetailsService, htmlCommonService, vpsPermissionService,
                                 CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modalStack) {
        var vm = this;
        var today = new Date();
        // variables
        vm.String = "Quản lý WO > Danh sách WO > Chi tiết WO";
        vm.workingWO = {};
        vm.fileAttachDataTbl = {};
        vm.attachFile = {};
        vm.label = {};
        vm.isAssignable = false;
        vm.isReAssignable = false;
        vm.isAcceptable = false;
        vm.isRejectable = false;
        vm.isOpinionAsking = false;
        vm.isDone = false;
        vm.isCdOk = false;
        vm.isStateOk = false;
        vm.isGpon = false;
        vm.isPause = false;
        vm.isGpxd = false;
        vm.modalOpened = false;
        vm.checkAsignAdminWo = false;
        $scope.checklistDate = '';
        $scope.wokingWoDetail = [];
        $scope.selectedUnit = {};
        $scope.loggedInUser = $rootScope.casUser.userName;
        $scope.sysUserId = $rootScope.casUser.sysUserId;
        $scope.sysUserGroup = {};
        $scope.enable = {};
        $scope.unassignState = "UNASSIGN";
        $scope.opinionOrderLabel = {};
        $scope.opinionComment = {};
        $scope.headerColor = {'background-color': '#33CCCC'};
        $scope.confirmWO = {};
        $scope.selectCheckList = {};
        $scope.listCheckList = {};
        $scope.permissions = {};
        $scope.isAcceptRejectQuantity = false;
        $scope.permissions = {};
        $scope.permissions.cdDomainDataList = [];
        $scope.isReassigning = false;
        vm.checkRoleApproveHshc = false;
        $scope.dnqtValue = {};
        $scope.aprovedValue = {};
        $scope.aprovedDocValue = {};
        $scope.hcqtChecklist = [];
        $scope.hcqtHasProblem = false;
        $scope.remainQuantityByDate = false;
        $scope.checklists = [];
        vm.label.maxDueDate = '';
        vm.comment = {};
        vm.comment.text = '';
        vm.checkViewHcqt = false;
        vm.additionalChecklistItem = {};
        $scope.isTcBranchApproved = false;

        vm.oldDueDate = '';
        vm.canExtendDueDate = false;
        vm.canApproveCdOk = false;
        vm.canPostOverdueReason = false;
        vm.canApproveOverdueReason = false;
        vm.overdueReasonInput = '';
        vm.overdueReasonLevel = '';
        vm.woOverdueReason = {};
        vm.overdueReasonAction2 = '';
        vm.overdueReasonAction3 = '';
        vm.overdueReasonAction4 = '';
        vm.overdueReasonAction5 = '';
        vm.overdueReasonActionFt = '';
        vm.isOverdue = false;
        vm.canPostOverdueReason = false;

        vm.overdueReasonText = 'Chưa nhập lý do';
        vm.overdueReasonPerson = '';
        vm.overdueReasonDate = '';
        vm.overdueApproveState = '';
        vm.overdueApprovePerson = '';
        vm.overdueApproveDate = '';

        vm.canApprovedOkWoTkdt = false;
        vm.canApprovedOkWoTthq = false;
        vm.searchForm = {};

        $scope.tcTctEmails = [];
        $scope.tcApproveModalType = 1;
        $scope.attachHd = false;
        $scope.attachBbks = false;

        vm.checkSysAdminTTHT = false; // HienLT56 add 27052021
        vm.checkStateForRoleTTHTEdit = false; // HienLT56 add 27052021
        vm.checkStateForRoleTTHTDelete = false; // HienLT56 add 27052021
        $scope.apWorkSrcs = {}; // HienLT56 add 31052021
        $scope.cTypes = {}; // HienLT56 add 31052021
        $scope.apWorkSrcs1 = {}; // Taotq add

        vm.checkDisabledWoValue = true;
        vm.checkWorkSrc = false;
        vm.checkAssignWO = false;
        $scope.isHCQT = false;
        vm.sysGroupId = $scope.sysUserGroup.sysGroupId;

        vm.checkCD_PKTCNKT = false;// Duonghv13 add 14092021

        vm.canApprovedOkWoVuong = false;

        vm.lstTuNguon=[];
        vm.lstXayDungNha=[];
        vm.lstXayDungMong=[];
        vm.lstThiCong=[];
        vm.lstLapDung=[];
        vm.lstDienAc=[];

        vm.objectTuNguon={};
        vm.objectXayDungNha={};
        vm.objectXayDungMong={};
        vm.objectThiCong={};
        vm.objectLapDung={};
        vm.objectDienAc={};
        vm.workingWODetail = {};
        vm.checkCDLV5Approve = false;

        // ThanhPT - WO BGBTS - Start
        vm.listChecklistId = [];
        // ThanhPT - WO BGBTS - End

        init();
        function init() {
            // Huypq-04092020-start
            woDetailsService.checkRoleApproveHshc().then(function (d) {
                if (d && d == 'OK') {
                    vm.checkRoleApproveHshc = true;
                } else {
                    vm.checkRoleApproveHshc = false;
                }
            }).catch(function () {
                vm.checkRoleApproveHshc = false;
            });
            // HienLT56 start 27052021
            woDetailsService.checkRoleTTHT().then(function(d){
            	if(d && d == 'OK'){
            		vm.checkSysAdminTTHT = true;
            	} else{
            		vm.checkSysAdminTTHT = false;
            	}
            }).catch(function () {
                vm.checkSysAdminTTHT = false;
            });
            // Duonghv13-start 14092021
            woDetailsService.checkRoleCDPKTCNKT().then(function(d){
            	if(d && d == 'OK'){
            		vm.checkCD_PKTCNKT = true;
            	} else{
            		vm.checkCD_PKTCNKT = false;
            	}
            }).catch(function () {
                vm.checkCD_PKTCNKT = false;
            });
            // Duong-end

            woDetailsService.checkRoleApproveCDLV5().then(function(d){
            	if(d && d == 'OK'){
            		vm.checkCDLV5Approve = true;
            	} else{
            		vm.checkCDLV5Approve = false;
            	}
            }).catch(function () {
                vm.checkCDLV5Approve = false;
            });
            // taotq start 15092022
            woDetailsService.checkAsignAdminWo().then(function(d){
            	if(d && d == 'OK'){
            		vm.checkAsignAdminWo = true;
            	} else{
            		vm.checkAsignAdminWo = false;
            	}
            }).catch(function () {
                vm.checkAsignAdminWo = false;
            });
            //            taotq end
            getAppWorkSrcs();
            getApConstructionTypes(); // Get loại công trình
            // HienLT56 end 27052021
            vm.checkViewHcqt = false;
            getCheckViewHcqt(); // Huypq-02112020-add
            // Huy-end
            $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
            console.log($scope.permissions);
            vm.isAssignable = false;
            vm.isRejectAcceptable = false;
            vm.label.stateText = '';
            if ($rootScope.viewDetailsWoId) getSysUserGroup($rootScope.viewDetailsWoId);
            subWO();
            getTcTctEmails();

            woDetailsService.getDataTableTTTHQ({woId: $rootScope.viewDetailsWoId}).then(function (data) {
            	fillDataTthq(data.data);
//            	fillDataTthqContract(data.data);
            });

            dataGridWorkItemMap([]);
        }

        // Huypq-02112020-start
        function getCheckViewHcqt() {
            woDetailsService.getCheckViewHcqt().then(
                function (resp) {
                    if (resp) {
                        vm.checkViewHcqt = true;
                    }
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra !");
                }
            )
        }

        // Huy-end

        function getTcTctEmails() {
            woDetailsService.getTcTctEmails().then(
                function (resp) {
                    if (resp) {
                        $scope.tcTctEmails = resp.data;
                    }
                },
                function (error) {
                    toastr.error("Không thể kết nối để lấy dữ liệu!");
                }
            )
        }

        function getSysUserGroup(woId) {
            var obj = {loggedInUser: $scope.loggedInUser}
            woDetailsService.getSysUserGroup(obj).then(
                function (resp) {
                    console.log(resp);
                    if (resp && resp.data) {
                        $scope.sysUserGroup = resp.data;
                        getWODetails(woId);
                        if ($scope.sysUserGroup.cdLevel1 == true) {
                            $scope.enable.removeAttachment = true;
                        }
                    }
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )
        }

        function subWO() {
            postal.subscribe({
                channel: "Tab",
                topic: "action",
                callback: function (data) {
                    if (data.action == 'refresh' &&
                        vm.workingWO.woId &&
                        data.woId != vm.workingWO.woId
                    ) {
                        getWODetails(data.woId, true);
                    }
                }
            });
        }

        function strToDate(dtStr) {
            var dateParts = dtStr.split("/");
            var timeParts = dateParts[2].split(" ")[1].split(":");
            dateParts[2] = dateParts[2].split(" ")[0];
            // month is 0-based, that's why we need dataParts[1] - 1
            return new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0], timeParts[0], timeParts[1], timeParts[2]);
        }

        function strToDateNoTime(dateStr) {
            if (!dateStr) return null;
            var dateOnly = dateStr.split(" ")[0];
            var dateParts = dateOnly.split("/");

            return new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0],);
        }

        function calculateActualWorkTime() {
            if (vm.workingWO.startTime && vm.workingWO.endTime) {
                var start = strToDate(vm.workingWO.startTime);
                var end = strToDate(vm.workingWO.endTime);
                var milisecsDiff = Math.abs(end - start);
                var hoursDiff = milisecsDiff / (1000 * 60 * 60);
                vm.label.actualWorkTime = hoursDiff.toFixed(2);
            }
        }
        // HienLT56 start 31052021
        function getAppWorkSrcs() {
        	woDetailsService.getAppWorkSrcs().then(
                function (resp) {
                    console.log(resp);
                    if (resp.data) $scope.apWorkSrcs = resp.data;

                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
            // taotq start 25082021
            woDetailsService.getAppWorkSource().then(
                function (resp) {
                    // console.log(resp);
                	if (resp.data) $scope.apWorkSrcs1 = resp.data;

                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
            // taotq end 25082021
        }
        $scope.apWorkSrcShortName = function (name) {
            return htmlCommonService.apWorkSrcShortName(name);
        }
        function getApWorkSrcs(apWorkSrc) {
            Object.keys($scope.apWorkSrcs).forEach(
                function (key, index) {
                    if ($scope.apWorkSrcs[key].code == apWorkSrc) {
                    	vm.label.apWorkSrc = $scope.apWorkSrcs[key].code;
                    }
                }
            );
            return vm.label.apWorkSrc;
        }
        function getApConstructionTypes() {
        	woDetailsService.getApConstructionTypes().then(
                function (resp) {
                    console.log(resp);
                    if (resp.data) $scope.cTypes = resp.data;

                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        };
        $scope.acceptChangeForTTHT = function (data) {
        	if(vm.workingWO.woNameId == 21 && (vm.workingWO.apWorkSrc == undefined || vm.workingWO.apWorkSrc == null)){
        		toastr.error(gettextCatalog.getString("Nguồn việc không được bỏ trống!"));
        		return;
        	}
        	vm.workingWO.moneyFlowValue = vm.label.moneyFlowValueMil ? (vm.label.moneyFlowValueMil * 1000000) : '';
        	vm.workingWO.moneyFlowRequired = vm.label.moneyFlowRequiredMil ? (vm.label.moneyFlowRequiredMil * 1000000) : '';
        	vm.workingWO.moneyValue = vm.label.moneyValueMil ? (vm.label.moneyValueMil * 1000000) : '';
        	vm.workingWO.loggedInUser = $scope.loggedInUser;
        	data = vm.workingWO;

        	woDetailsService.saveChangeForTTHT(data).then(function(result){
				if(result.error){
					toastr.error(result.error);
					return;
				}
    			toastr.success("Ghi nhận thay đổi thành công!");
    			vm.checkWorkSrc = true;
    			vm.label.moneyValueMil = vm.workingWO.moneyValue ? (vm.workingWO.moneyValue / 1000000).toFixed(3) : '';
                vm.label.moneyFlowValueMil = vm.workingWO.moneyFlowValue ? (vm.workingWO.moneyFlowValue / 1000000).toFixed(3) : '';
                vm.label.moneyFlowRequiredMil = vm.workingWO.moneyFlowRequired ? (vm.workingWO.moneyFlowRequired / 1000000).toFixed(3) : '';
                vm.label.moneyValueMil = CommonService.removeTrailingZero(vm.label.moneyValueMil);
                vm.label.moneyFlowValueMil = CommonService.removeTrailingZero(vm.label.moneyFlowValueMil);
                vm.label.moneyFlowRequiredMil = CommonService.removeTrailingZero(vm.label.moneyFlowRequiredMil);
    		}, function(errResponse){
                if (errResponse.status === 409) {
                	toastr.error(gettextCatalog.getString("Lỗi khi ghi nhận thay đổi!"));
                	return;
                } else {
                	toastr.error(gettextCatalog.getString("Lỗi khi ghi nhận thay đổi"));
                	return;
                }
	        });
        };
        // HienLT56 end 31052021

        function getWODetails(woId, refresh) {
            var obj = {woId: woId};
            woDetailsService.getOneWODetails(obj).then(
                function (resp) {
                    //console.log(resp);
                    if (resp && resp.data) {
                        vm.workingWO = resp.data;
                        if(vm.workingWO.woTypeCode=="TMBT" && vm.workingWO.constructionCode!=null) {
                        	vm.workingWO.stationCode = vm.workingWO.constructionCode.split(" / ")[0];
                        	vm.workingWO.vtNetStationCode = vm.workingWO.constructionCode.split(" / ")[1];
                        }
                        //console.log(vm.workingWO.cdLevel4Name);
                        fillDataCheckList();
                        fillDataWoHistory();
                        fillDataFileAttachments();
                        if(vm.workingWO.opinionType!=null && vm.workingWO.state!='PAUSE') {
                        	getStateText(vm.workingWO.stateVuong);
                        } else {
                        	getStateText(vm.workingWO.state);
                        }

                        vm.isAssignable = checkIsAssignable(vm.workingWO);
                        vm.isReAssignable = checkIsReAssignable(vm.workingWO);
                        vm.isAcceptable = checkIsAcceptable(vm.workingWO);
                        vm.isRejectable = checkIsRejectable(vm.workingWO);
                        vm.isOpinionAsking = checkIsOpinionAsking(vm.workingWO);
                        vm.isDone = checkDone(vm.workingWO);
                        vm.isCdOk = checkCdOk(vm.workingWO);
                        vm.isPause = checkPause(vm.workingWO);
                        vm.canApproveCdOk = checkCanApproveCdOk();
// vm.label.apWorkSrcShortName = vm.workingWO.apWorkSrcName ?
// htmlCommonService.apWorkSrcShortName(vm.workingWO.apWorkSrcName) : '';
                        // HienLT56 start 31052021
// vm.label.apWorkSrc = getApWorkSrcs(vm.workingWO.apWorkSrc);
                        vm.workingWO.apWorkSrc = kendo.toString(vm.workingWO.apWorkSrc, "n0");
                        vm.workingWO.apConstructionType = kendo.toString(vm.workingWO.apConstructionType, "n0");
                        // HienLT56 end 31052021
                        vm.label.moneyValueMil = vm.workingWO.moneyValue ? (vm.workingWO.moneyValue / 1000000).toFixed(3) : '';
                        vm.label.moneyValueHtctMil = vm.workingWO.moneyValueHtct ? (vm.workingWO.moneyValueHtct / 1000000).toFixed(3) : '';
                        vm.label.moneyFlowValueMil = vm.workingWO.moneyFlowValue ? (vm.workingWO.moneyFlowValue / 1000000).toFixed(3) : '';
                        vm.label.moneyFlowRequiredMil = vm.workingWO.moneyFlowRequired ? (vm.workingWO.moneyFlowRequired / 1000000).toFixed(3) : '';
                        vm.label.moneyValueMil = CommonService.removeTrailingZero(vm.label.moneyValueMil);
                        vm.label.moneyValueHtctMil = CommonService.removeTrailingZero(vm.label.moneyValueHtctMil);
                        vm.label.moneyFlowValueMil = CommonService.removeTrailingZero(vm.label.moneyFlowValueMil);
                        vm.label.moneyFlowRequiredMil = CommonService.removeTrailingZero(vm.label.moneyFlowRequiredMil);
                        vm.canExtendDueDate = isAbleToExtendDueDate();
                        // getWoOverdueReason();

                        vm.isOverdue = checkOverdue();

                        calculateActualWorkTime();
                        if (refresh == true) {
                            if (vm.woCheckList) vm.woCheckList.dataSource.read();
                            if (vm.woHistory) vm.woHistory.dataSource.read();
                            if (vm.fileAttachDataTbl && vm.fileAttachDataTbl.dataSource) vm.fileAttachDataTbl.dataSource.read();
                            if (vm.woHCQTCheckList) vm.woHCQTCheckList.dataSource.read();
                            if (vm.woTKDTCheckList) vm.woTKDTCheckList.dataSource.read();
                            if (vm.tmbtStationDataTbl) vm.tmbtStationDataTbl.dataSource.read();
                            if (vm.tthqDataTbl) vm.tthqDataTbl.dataSource.read();
                            if (vm.tthqDataContractTbl) vm.tthqDataContractTbl.dataSource.read();
                        }

                        if (vm.workingWO.woTypeCode == 'HCQT') {
                            getHcqtProject();
                        }


                        if (vm.workingWO.woTypeCode == 'HSHC') {
                            if (vm.workingWO.catConstructionTypeId == 8) {
                                fillXdddChecklistDataTbl()
                            } else {
                                fillConstructionWoDataTbl();
                                fillDataTableWorkItem();
                            }
                        }

                        if ((vm.workingWO.woTypeCode == 'UCTT' || vm.workingWO.woTypeCode == 'AVG') && (vm.workingWO.state == 'DONE'
                            || vm.workingWO.state == 'OK' || vm.workingWO.state == 'NG' || vm.workingWO.state == 'CD_OK' || vm.workingWO.state == 'CD_NG')) {
                            fillDataGoods();
                        }

                        if (vm.workingWO.woTypeCode == 'TMBT') {
                            fillTmbtStationDataTbl();
                        }

                        // Check quyen dong wo thiet ke du toan
                        // Co quyen CD tru XDDTHT, loai wo TKDT, trang thai
						// PROCESSING va da thuc hien het cac checklist
                        if ($scope.permissions.cdDomainDataList.includes(vm.workingWO.cdLevel1)
                            && vm.workingWO.woTypeCode == 'TKDT' && vm.workingWO.state == 'DONE' && vm.workingWO.checklistStep == 3) {
                            vm.canApprovedOkWoTkdt = true;
                        } else {
                            vm.canApprovedOkWoTkdt = false;
                        }
                        // Huypq-05072021-start
                        if ($scope.permissions.cdDomainDataList.includes(vm.workingWO.cdLevel2)
                                && vm.workingWO.woTypeCode == 'TTHQ' && vm.workingWO.state == 'PROCESSING') {
                                vm.canApprovedOkWoTthq = true;
                            } else {
                                vm.canApprovedOkWoTthq = false;
                            }
                        // Huy-end
                        // HienLT56 start 27052021
                        if(vm.workingWO.state == "ASSIGN_CD" || vm.workingWO.state == "ASSIGN_FT" || vm.workingWO.state == "ACCEPT_FT" || vm.workingWO.state == "ACCEPT_CD" || vm.workingWO.state == "REJECT_FT"
                        	|| vm.workingWO.state == "PROCESSING" || vm.workingWO.state == "DONE" || vm.workingWO.state == "CD_OK"){
                        	vm.checkStateForRoleTTHTEdit = true;
                        } else{
                        	vm.checkStateForRoleTTHTEdit = false;
                        }

                        // HeinLT56 end 27052021

                      // Huypq-27072021-start
                        if((vm.workingWO.woTypeCode=='THICONG' || vm.workingWO.woTypeCode=='HSHC' || vm.workingWO.woTypeCode=='THDT')){
                        	if (vm.checkSysAdminTTHT && vm.checkStateForRoleTTHTEdit){
                        		vm.checkDisabledWoValue = false;
	                        } else {
	                        	vm.checkDisabledWoValue = true;
	                        }
                        }
                        // Huy-end
// Taotq start 25082021
                        if(vm.workingWO.woNameId == 21 && (vm.workingWO.apWorkSrc != undefined || vm.workingWO.apWorkSrc != null)){
                    		vm.lo = true;
                    	}
//                      Taotq end 25082021
                        //Huypq-25102021-start
                        if(vm.workingWO.woTypeCode == 'TKDT' && vm.workingWO.state == 'OK') {
                        	fillDataTableWorkItem();
                        }
                        //Huy-end
                        if ($scope.permissions.cdDomainDataList.includes(vm.workingWO.cdLevel1) && vm.workingWO.state == 'DTHT_PAUSE') {
                            vm.canApprovedOkWoVuong = true;
                        } else {
                            vm.canApprovedOkWoVuong = false;
                        }

                        if(vm.workingWO.woTypeCode == 'DBHT') {
                        	fillDataTableWoComplete();
                        }

                        // ThanhPT - Danh sach vat tu KTTS - start
                        if(vm.workingWO.woTypeCode == 'BGBTS_DTHT' || vm.workingWO.woTypeCode == 'BGBTS_VHKT') {
                            fillDataTableWoGoods();
                        }

                        $scope.ngMissingImageItems = vm.workingWO.lstAppParams;
                        $scope.ngMissingImageSelected = [];
                        $scope.ngMissingImageToggle = function (item, list) {
                            var idx = list.indexOf(item);
                            if (idx > -1) {
                                list.splice(idx, 1);
                            }
                            else {
                                list.push(item);
                            }
                        };

                        $scope.ngMissingImageExists = function (item, list) {
                            return list.indexOf(item) > -1;
                        };
                        // ThanhPT - Danh sach vat tu KTTS - end
                    }
                    $rootScope.viewDetailsWoId = null;
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    $rootScope.viewDetailsWoId = null;
                }
            )

        };

        function checkOverdue() {
            var finishDate = strToDateNoTime(vm.workingWO.finishDate);
            var endTime = strToDateNoTime(vm.workingWO.endTime);
            var currTime = new Date();

            var isOver = false;
            // WO da hoan thanh
            if ((!vm.workingWO.endTime || vm.workingWO.endTime == '') && vm.workingWO.state == 'OK') {
                if (finishDate - endTime < 0) {
                    isOver = true;
                }
            } else { // WO chua hoan thanh
                if (currTime - finishDate > 0) {
                    isOver = true;
                }
            }

            if (isOver) {
                if (vm.workingWO.overdueReason) {
                    var overdueParts = vm.workingWO.overdueReason.split('|');
                    vm.overdueReasonText = overdueParts[0];
                    vm.overdueReasonPerson = overdueParts[2];
                    vm.overdueReasonDate = overdueParts[3];
                }

                if (vm.workingWO.overdueApproveState == 'WAIT') vm.overdueApproveState = 'Chờ phê duyệt';
                if (vm.workingWO.overdueApproveState == 'APPROVED') vm.overdueApproveState = 'Đã được chấp thuận';
                if (vm.workingWO.overdueApproveState == 'REJECTED') vm.overdueApproveState = 'Đã bị từ chối';

                if (vm.workingWO.overdueApprovePerson) {
                    var overduePersonParts = vm.workingWO.overdueApprovePerson.split('|');
                    vm.overdueApprovePerson = overduePersonParts[2];
                    vm.overdueApproveDate = overduePersonParts[3];
                }

                checkCanPostOverdueReason();

                return true;
            }
            return false;
        }

        function checkCanPostOverdueReason() {
            if ($scope.sysUserId == vm.workingWO.ftId || $scope.sysUserId == vm.workingWO.cdLevel5) vm.canPostOverdueReason = true;
            if ($scope.permissions.cdDomainDataList.includes(vm.workingWO.cdLevel4)) vm.canPostOverdueReason = true;
            if ($scope.permissions.cdDomainDataList.includes(vm.workingWO.cdLevel3)) vm.canPostOverdueReason = true;
            if ($scope.permissions.cdDomainDataList.includes(vm.workingWO.cdLevel2)) vm.canPostOverdueReason = true;
        }

        // function getWoOverdueReason(){
        // var obj = {woId:vm.workingWO.woId};
        // woDetailsService.getWoOverdueReason(obj).then(
        // function (res) {
        // vm.woOverdueReason = res.data;
        // vm.canPostOverdueReason = checkIsAbleToPostReason();
        // vm.overdueReasonAction2 = vm.checkIsAbleToApproveOverdueReason('2');
        // vm.overdueReasonAction3 = vm.checkIsAbleToApproveOverdueReason('3');
        // vm.overdueReasonAction4 = vm.checkIsAbleToApproveOverdueReason('4');
        // vm.overdueReasonAction5 = vm.checkIsAbleToApproveOverdueReason('5');
        // vm.overdueReasonActionFt =
		// vm.checkIsAbleToApproveOverdueReason('ft');
        // }
        // )
        // }

        // function checkIsAbleToPostReason(){
        // if(!vm.woOverdueReason) return false;
        //
        // if(vm.workingWO.userFtReceiveWo == '-1' && $scope.sysUserId ==
		// vm.workingWO.ftId && vm.woOverdueReason.approveStateLevelFt !=
		// 'ACCEPTED' && vm.woOverdueReason.approveStateLevelFt != 'REJECTED'){
        // vm.overdueReasonLevel = 'ft';
        // return true;
        // }
        //
        // if(vm.workingWO.userCdLevel5ReceiveWo == '-1' && $scope.sysUserId ==
		// vm.workingWO.cdLevel5 && vm.woOverdueReason.approveStateLevel5 !=
		// 'ACCEPTED' && vm.woOverdueReason.approveStateLevel5 != 'REJECTED'){
        // vm.overdueReasonLevel = '5';
        // return true;
        // }
        //
        // if(vm.workingWO.userCdLevel4ReceiveWo == '-1' &&
		// $scope.permissions.cdDomainDataList.includes(vm.workingWO.cdLevel4)
		// && vm.woOverdueReason.approveStateLevel4 != 'ACCEPTED' &&
		// vm.woOverdueReason.approveStateLevel4 != 'REJECTED'){
        // vm.overdueReasonLevel = '4';
        // return true;
        // }
        //
        // if(vm.workingWO.userCdLevel3ReceiveWo == '-1' &&
		// $scope.permissions.cdDomainDataList.includes(vm.workingWO.cdLevel3)
		// && vm.woOverdueReason.approveStateLevel3 != 'ACCEPTED' &&
		// vm.woOverdueReason.approveStateLevel3 != 'REJECTED'){
        // vm.overdueReasonLevel = '3';
        // return true;
        // }
        //
        // if(vm.workingWO.userCdLevel2ReceiveWo == '-1' &&
		// $scope.permissions.cdDomainDataList.includes(vm.workingWO.cdLevel2)
		// && vm.woOverdueReason.approveStateLevel2 != 'ACCEPTED' &&
		// vm.woOverdueReason.approveStateLevel2 != 'REJECTED'){
        // vm.overdueReasonLevel = '2';
        // return true;
        // }
        //
        // return false;
        // }

        vm.openOverdueReasonModal = function () {
            vm.modalOpened = true;
            $modal.open({
                templateUrl: 'coms/wo_xl/woDetails/overdueReason.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            }).result.then(
                function () {
                    postOverdueReason();
                    vm.modalOpened = false;
                },
                function () {
                    vm.modalOpened = false;
                }
            )
        };

        vm.validateOverdueReason = function () {
            if (!vm.overdueReasonInput || vm.overdueReasonInput.trim() == '') {
                toastr.error('Chưa nhập lý do!');
                return false;
            }
            return true;
        };

        function postOverdueReason() {
            var obj = {
                overdueReasonText: vm.overdueReasonInput,
                woId: vm.workingWO.woId,
                sysUserId: $scope.sysUserId,
            };

            woDetailsService.postOverdueReason(obj).then(
                function (res) {
                    if (res && res.statusCode == 1) {
                        toastr.success('Thành công!');
                    } else {
                        toastr.error('Không thành công!');
                    }
                    getWODetails(vm.workingWO.woId, true);
                },
                function (err) {
                    toastr.error('Có lỗi xảy ra!');
                    console.log(err);
                }
            );
        }

        vm.checkIsAbleToApproveOverdueReason = function (level) {
            if (!$scope.permissions.approveOverdue) return '';
            if (level == '2' && vm.woOverdueReason.approveStateLevel2 == 'WAITING_APPROVAL') return overdueReasonActionTemplate('2');
            if (level == '3' && vm.woOverdueReason.approveStateLevel3 == 'WAITING_APPROVAL') return overdueReasonActionTemplate('3');
            if (level == '4' && vm.woOverdueReason.approveStateLevel4 == 'WAITING_APPROVAL') return overdueReasonActionTemplate('4');
            if (level == '5' && vm.woOverdueReason.approveStateLevel5 == 'WAITING_APPROVAL') return overdueReasonActionTemplate('5');
            if (level == 'ft' && vm.woOverdueReason.approveStateLevelFt == 'WAITING_APPROVAL') return overdueReasonActionTemplate('ft');
        };

        vm.acceptRejectOverdueReason = function (accept) {
            var state = '';
            var label = '';
            if (accept == 1) {
                state = 'APPROVED';
                label = 'chấp thuận';
            }
            if (accept == 0) {
                state = 'REJECTED';
                label = 'từ chối';
            }

            confirm('Bạn muốn ' + label + ' lý do?', function () {
                var obj = {
                    woId: vm.workingWO.woId,
                    sysUserId: $scope.sysUserId,
                    overdueApproveState: state,
                };

                woDetailsService.acceptRejectOverdueReason(obj).then(
                    function (res) {
                        if (res && res.statusCode == 1) {
                            toastr.success('Thành công!');
                        } else {
                            toastr.error('Không thành công!');
                        }
                        getWODetails(vm.workingWO.woId, true);
                    },
                    function (err) {
                        toastr.error('Có lỗi xảy ra!');
                        console.log(err);
                    }
                );
            });
        };

        function isAbleToExtendDueDate() {
            if (vm.workingWO.state == 'CD_OK' || vm.workingWO.state == 'OK') return false;
            if (!$scope.permissions.cdDomainDataList.includes(vm.workingWO.cdLevel1)) return false;
            if (!$scope.permissions.cdWO || 'THICONG' != vm.workingWO.woTypeCode || vm.workingWO.apWorkSrc != 6) return false;
            return true;
        }

        function getHcqtProject() {
            if (vm.workingWO.hcqtProjectId && vm.workingWO.hcqtProjectId != '') {

                var obj = {hcqtProjectId: vm.workingWO.hcqtProjectId};

                woDetailsService.getHcqtProject(obj).then(
                    function (res) {
                        if (res && res.statusCode == 1) {
                            vm.workingWO.hcqtProjectName = res.data.name;
                        }
                    }
                )
            }
        }

        vm.getCheckListNeedAdd = getCheckListNeedAdd

        function getCheckListNeedAdd() {
            var param = {
                woId: vm.workingWO.woId
            };

            woDetailsService.getCheckListNeedAdd(param).then(
                function (res) {
                    if (res && res.statusCode == 1) {
                        $scope.listCheckList = res.data;
                    }
                }
            )
        }

        vm.openModalAddCheckList = function () {
            vm.additionalChecklistItem.name = null;
            vm.modalOpened = true;
            $modal.open({
                templateUrl: 'coms/wo_xl/woDetails/woAddCheckList.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            }).result.then(
                function () {
                    vm.addCheckList();
                },
                function () {
                    vm.modalOpened = false;
                }
            )
        };

        vm.addCheckList = addCheckList;

        function addCheckList() {
            if (!vm.additionalChecklistItem.name || vm.additionalChecklistItem.name == '') {
                toastr.error("Tên đầu việc không được để trống!");
                vm.modalOpened = false;
                return;
            }

            var addType = 'XDDD';
            if (vm.workingWO.woTypeCode == '5S') addType = '5S';

            var obj = {
                name: vm.additionalChecklistItem.name,
                woId: vm.workingWO.woId,
                confirm: 0,
                hshc: 0,
                loggedInUser: $scope.loggedInUser,
                customField: addType
            };

            woDetailsService.addCheckList(obj).then(
                function (res) {
                    if (res && res.statusCode == 1) {
                        toastr.success(res.message);
                        reloadTables();
                        publishChange();
                    } else {
                        toastr.error(res.message);
                    }
                    vm.modalOpened = false;
                }, function (error) {
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    vm.modalOpened = false;
                }
            )
        }


        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };

        vm.deleteCheckList = function (id, name) {
            var obj = {
                id: id,
                name: name,
                woId: vm.workingWO.woId,
                catConstructionTypeId: vm.workingWO.catConstructionTypeId,
                loggedInUser: $scope.loggedInUser,
            };

            confirm('Xác nhận xóa bản ghi đã chọn?',
                function () {
                    woDetailsService.deleteCheckList(obj).then(
                        function (resp) {
                            if (resp && resp.statusCode == 1) toastr.success("Xóa thành công!");
                            else toastr.success("Đã xóa hoặc không tồn tại!");
                            reloadTables();
                        },
                        function (error) {
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                        }
                    )
                }
            );
        };

        function isAbleToCDThisWo() {
            var wo = vm.workingWO;
            var cdDomain = $scope.permissions.cdDomainDataList;

            if (!$scope.permissions.cdWO) return false;

            if (cdDomain.includes(wo.cdLevel1) || cdDomain.includes(wo.cdLevel2) || cdDomain.includes(wo.cdLevel3) || cdDomain.includes(wo.cdLevel4) || $scope.sysUserId == wo.cdLevel5) return true;
            return false;
        }

        function isChecklistDeletable() {
            if (vm.workingWO.state == 'UNASSIGN' || vm.workingWO.state == 'ASSIGN_CD' || vm.workingWO.state == 'ACCEPT_CD' || vm.workingWO.state == 'REJECT_CD'
                || vm.workingWO.state == 'ASSIGN_FT' || vm.workingWO.state == 'REJECT_FT') return true;
            return false;
        }

        function createActionTemplate(dataItem) {
            var template = '';
            var checklistName = dataItem.checklistName;
            if (vm.workingWO.catConstructionTypeId == 8) checklistName = dataItem.name;

            if (isAbleToCDThisWo()) {
                if (vm.workingWO.woTypeCode == 'THICONG' && dataItem.state == 'NEW' && isChecklistDeletable()) {
                    template += '<i class="fa fa-trash-o icon-table" ng-click="vm.deleteCheckList(' + dataItem.id + ',\'' + checklistName + '\')"></i>&nbsp;';
                }

                if (dataItem.quantityByDate == 1 && (vm.workingWO.state == 'PROCESSING' || vm.workingWO.state == 'DONE')) {
                    if (dataItem.addedQuantityLength && dataItem.addedQuantityLength > 0) $scope.remainQuantityByDate = true;
                    // template += '<button
					// ng-click="vm.acceptQuantityLength('+dataItem.id+','+dataItem.quantityLength+','+dataItem.unapprovedQuantity+')">Ghi
					// nhận sản lượng</button>&nbsp;';
                    template += '<button class="btn btn-primary smaller-btn" ng-click="openAcceptQuantityByDateModal(' + dataItem.id + ')">Sản lượng theo ngày</button>&nbsp;';
                }

                if (dataItem.quantityByDate == 2 && dataItem.unapprovedQuantity && dataItem.unapprovedQuantity > 0 && (vm.workingWO.state == 'PROCESSING' || vm.workingWO.state == 'DONE')) {
                    if (dataItem.addedQuantityLength && dataItem.addedQuantityLength > 0) $scope.remainQuantityByDate = true;
                    template += '<button ng-click="vm.acceptQuantityLength(' + dataItem.id + ',' + dataItem.quantityLength + ',' + dataItem.unapprovedQuantity + ')">Ghi nhận sản lượng</button>&nbsp;';
                    template += '<button ng-click="vm.rejectQuantityLength(' + dataItem.id + ')">Từ chối</button>&nbsp;';
                }

                if (vm.workingWO.woTypeCode == 'THICONG' && vm.workingWO.apWorkSrc == 6 && dataItem.value!=null && dataItem.value >= 0 && dataItem.confirm != 1 && vm.workingWO.state != 'CD_NG' && vm.workingWO.state != 'NG') {
                    template += '<button class="btn btn-primary" ng-click="vm.acceptXdddValue(' + dataItem.id + ')">Ghi nhận</button>&nbsp;';
                    template += '<button class="btn btn-danger" ng-click="vm.rejectXdddValue(' + dataItem.id + ')">Từ chối</button>&nbsp;';
                }
            }

            if (vm.workingWO.woTypeCode == 'DOANHTHU' && vm.workingWO.state == 'WAIT_TC_TCT' && $scope.permissions.approveTcTct) {
                template += '<button class="btn btn-warning" ng-click="vm.tcApprove(' + dataItem.id + ', 1)">Duyệt</button>&nbsp;';
            }

            if (dataItem.checklistId == 2 && dataItem.state == 'DONE') $scope.isTcBranchApproved = true;
            else $scope.isTcBranchApproved = false;

            if (vm.workingWO.woTypeCode == 'HSHC' && vm.workingWO.state == 'DONE' && $scope.permissions.approveTcBranch && dataItem.checklistId == 2 && dataItem.state == 'NEW') {
                template += '<button class="btn btn-primary" ng-click="vm.tcApprove(' + dataItem.id + ', 2)">Duyệt</button>&nbsp;';
            }

            if (vm.workingWO.woTypeCode == 'HSHC' && vm.workingWO.state == 'CD_OK' && $scope.permissions.approveTcTct && dataItem.checklistId == 3 && dataItem.state == 'NEW' && $scope.isTcBranchApproved == false) {
                template += '<button class="btn btn-warning" ng-click="vm.tcApprove(' + dataItem.id + ',1)">Duyệt</button>&nbsp;';
            }

            if ($scope.permissions.update5S && vm.workingWO.woTypeCode == '5S' && isChecklistDeletable()) template += '<i class="fa fa-trash-o icon-table" ng-click="vm.deleteCheckList(' + dataItem.id + ',\'' + dataItem.name + '\')"></i>&nbsp;';

            return template;
        }

        vm.tcApprove = function (id, type) {
            if (id == '') id = null;
            $scope.rejectComment = {};
            $scope.tcApproveModalType = type;
            $modal.open({
                templateUrl: 'coms/wo_xl/common/tcApproveModal.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function () {
                    if ($scope.confirmWO.isWoDoneOk == undefined) {
                        toastr.error("Chưa chọn đánh giá!");
                    } else {
                        if ($scope.confirmWO.isWoDoneOk) {
                            if (type == 3) { // type = 3 là tài chính tổng
												// cty duyệt hoàn công quyết
												// toán
                                vm.completeChecklist(id, 5);
                                return;
                            }

                            var woState = '';
                            if (type == 1) { // type = 1 là tài chính tổng
												// cty duyệt
                                woState = 'OK';
                            } else if (type == 2) { // type = 2 là tài chính trụ
													// duyệt
                                if (!vm.workingWO.emailTcTct || vm.workingWO.emailTcTct == '') {
                                    toastr.error('Phải chọn người duyệt thuộc khối tài chính tổng công ty. ');
                                    return;
                                }
                                woState = 'WAIT_TC_TCT';
                            }

                            changeStateAndAcceptTcTct(id, woState);
                        } else {
                            if ($scope.rejectComment.text == undefined || $scope.rejectComment.text == '') {
                                toastr.error('Lý do từ chối không được để trống. ');
                            } else {
                                var woState = '';
                                if (type == 1) { // type = 1 là tài chính
													// tổng cty từ chối
                                    woState = 'TC_TCT_REJECTED';
                                } else if (type == 2) { // type = 2 là tài chính
														// trụ từ chối
                                    woState = 'TC_BRANCH_REJECTED';
                                }
                                changeStateAndAcceptTcTct(id, woState);
                            }
                        }
                    }
                },
                function () {
                    $scope.confirmWO.isWoDoneOk = true;
                }
            )
        };

        function changeStateAndAcceptTcTct(id, woState) {
            var obj = {
                id: id,
                woId: vm.workingWO.woId,
                loggedInUser: $scope.loggedInUser,
                state: woState,
                emailTcTct: vm.workingWO.emailTcTct,
                text: $scope.rejectComment.text,
                moneyValue: vm.label.moneyValue
            };

            woDetailsService.changeStateAndAcceptTcTct(obj).then(
                function (resp) {
                    if (resp && resp.statusCode == 1) toastr.success("Duyệt thành công !");
                    else toastr.error("Có lỗi xảy ra!");
                    getWODetails(vm.workingWO.woId);
                    vm.woCheckList.dataSource.read();
                    reloadTables();
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function changeStateNotGood(id, woState) {
            var obj = {
                id: id,
                ftId: vm.workingWO.ftId,
                woId: vm.workingWO.woId,
                loggedInUser: $scope.loggedInUser,
                state: woState,
                text: $scope.rejectComment.text,
                woTypeCode: vm.workingWO.woTypeCode,
                moneyValue: $scope.confirmWO.moneyValueMil
            };

            woDetailsService.changeStateNotGood(obj).then(
                function (resp) {
                    if (resp && resp.statusCode == 1) {
                        publishChange();
                        toastr.success("Đã từ chối WO !");
                    } else {
                    	if(resp.data.customField) {
                    		toastr.success(resp.data.customField);
                    	} else {
                    		toastr.error("Có lỗi xảy ra!");
                    	}
                    }
                    getWODetails(vm.workingWO.woId, true);
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        vm.acceptXdddValue = function (id, noNeedConfirm) {
            if (!id) return;
            var obj = {id: id, loggedInUser: $scope.loggedInUser};
            if (noNeedConfirm) {
                processAcceptXdddValue(obj);
            } else {
                confirm('Xác nhận ghi nhận sản lượng?', function () {
                    processAcceptXdddValue(obj);
                });
            }
        };

        function processAcceptXdddValue(obj) {
            woDetailsService.acceptXdddValue(obj).then(
                function (resp) {
                    if (resp && resp.statusCode == 1) toastr.success("Ghi nhận thành công!");
                    else toastr.error("Có lỗi xảy ra!");
                    getWODetails(vm.workingWO.woId);
                    vm.woCheckList.dataSource.read();
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        vm.rejectXdddValue = function (id) {
            var obj = {id: id, loggedInUser: $scope.loggedInUser};
            woDetailsService.rejectXdddValue(obj).then(
                function (resp) {
                    if (resp && resp.statusCode == 1) toastr.success("Từ chối thành công!");
                    else toastr.error("Có lỗi xảy ra!");
                    getWODetails(vm.workingWO.woId);
                    vm.woCheckList.dataSource.read();
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        };

        vm.acceptQuantityLength = function (id, oldValue, addedValue, noNeedConfirm) {
            if (!addedValue || addedValue == '' || addedValue == 0) return;

            if (!oldValue) oldValue = 0;
            var obj = {
                id: id,
                quantityLength: oldValue,
                unapprovedQuantity: addedValue,
                woId: vm.workingWO.woId,
                loggedInUser: $scope.loggedInUser,
                sysUserId: $scope.sysUserId
            };

            if (noNeedConfirm) {
                processAcceptQuantity(obj);
            } else {
                confirm('Xác nhận ghi nhận sản lượng?', function () {
                    processAcceptQuantity(obj);
                });
            }

        };

        vm.rejectQuantityLength = function (id) {
            if (!id) return;

            confirm('Xác nhận từ chối sản lượng?', function () {

                var obj = {
                    id: id,
                    woId: vm.workingWO.woId,
                    loggedInUser: $scope.loggedInUser,
                    sysUserId: $scope.sysUserId
                };

                woDetailsService.rejectQuantityLength(obj).then(
                    function (resp) {
                        if (resp && resp.statusCode == 1) toastr.success(resp.message);
                        else toastr.error("Có lỗi xảy ra!");
                        getWODetails(vm.workingWO.woId, true);
                    },
                    function (error) {
                        console.log(error);
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            });

        };

        function processAcceptQuantity(obj) {
            woDetailsService.acceptQuantityLength(obj).then(
                function (resp) {
                    if (resp && resp.statusCode == 1) toastr.success(resp.message);
                    else toastr.error("Có lỗi xảy ra!");
                    getWODetails(vm.workingWO.woId);
                    vm.woCheckList.dataSource.read();
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }


        function fillDataCheckList() {
            getCatWorkName();
            if (vm.workingWO.woTypeCode == 'HCQT') {
                fillHCQTChecklist();
            } else if (vm.workingWO.woTypeCode == 'TKDT') {
                fillTKDTChecklist();
            } else if (vm.workingWO.woTypeCode == 'AVG') {
                fillAvgChecklist();
            } else {
                fillNormalChecklist();
            }
        }

        var checkListRecord = 0;

        function fillNormalChecklist() {
            $scope.remainQuantityByDate = false;
            vm.woCheckListOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save: function () {
                    vm.woCheckList.dataSource.read();
                },
                dataBinding: function () {
                    checkListRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            // $("#appCount").text("" + response.total);
                            // vm.count = response.total;
                            // return response.total;
                        },
                        data: function (response) {
                            $scope.checklists = response.data;
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/wo/getCheckList",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var obj = {
                                woId: vm.workingWO.woId,
                                constructionCode: vm.workingWO.constructionCode,
                                woTypeCode: vm.workingWO.woTypeCode
                            };
                            return JSON.stringify(obj)

                        }
                    },
                    pageSize: 10,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: false,
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++checkListRecord;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',

                    },
                    {
                        title: "Tên công việc",
                        field: 'constructionCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text',
                        template: function (dataItem) {
                            if (dataItem.checklistName) return dataItem.checklistName;
                            else if (dataItem.name) return dataItem.name;
                            else return vm.workingWO.woName;
                        },

                    },
                    {
                        title: "Trạng thái",
                        field: 'valueConstruction',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.state;
                            ;
                        },

                    },
                    {
                        title: "Sản lượng theo ngày/tháng",
                        field: 'quantityByDate',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        hidden: (vm.workingWO.woTypeCode != 'THICONG' && vm.workingWO.woTypeCode != 'THDT') || vm.workingWO.woTypeCode == 'PHATSONG',
                        template: function (dataItem) {
                            if (dataItem.quantityByDate == 1 || dataItem.quantityByDate == 2) return 'Có';
                            return 'Không';
                        },

                    },
                    {
                        title: "Sản lượng đã ghi nhận",
                        field: 'quantityLength',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            var returnValue = '';
                            if (dataItem.confirm == 1) returnValue = dataItem.value;
                            else returnValue = dataItem.quantityLength ? dataItem.quantityLength : '';
                            if (returnValue != '') {
                                returnValue = CommonService.numberWithCommas(returnValue);
                            }
                            return returnValue;
                        },
                        hidden: vm.workingWO.woTypeCode == 'PHATSONG'
                    },
                    {
                        title: "Sản lượng thêm mới",
                        field: 'unapprovedQuantity',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            var returnValue = dataItem.unapprovedQuantity ? dataItem.unapprovedQuantity : '';
                            if (returnValue != '') returnValue = CommonService.numberWithCommas(returnValue);
                            return returnValue;
                        },
                        hidden: (vm.workingWO.woTypeCode == 'THICONG' && vm.workingWO.apWorkSrc == 6) || vm.workingWO.woTypeCode == 'PHATSONG'
                    },
                    {
                        title: "Giá trị sản lượng (VND)",
                        field: 'addedQuantityLength',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            var returnValue = dataItem.value ? dataItem.value : '';
                            if (returnValue != '') returnValue = CommonService.numberWithCommas(returnValue);
                            return returnValue;
                        },
                        hidden: (vm.workingWO.woTypeCode != 'THICONG' || vm.workingWO.apWorkSrc != 6) || vm.workingWO.woTypeCode == 'PHATSONG'
                    },
                    {
                        title: "Ảnh thực hiện",
                        field: 'sysGroupName',
                        width: '25%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            var template = ''
                            if (dataItem.lstImgs) {
                                for (var i = 0; i < dataItem.lstImgs.length; i++) {
                                    template += '<div><img style="width: 150px;" src="data:image/png;base64,' + dataItem.lstImgs[i].imgBase64 + '" ng-click="showBigImg($event)"></div>';
                                }
                            }
                            return template;
                        },
                    },
                    {
                        title: "Giá trị thực tế",
                        field: 'actualValue',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.actualValue ? dataItem.actualValue : '';
                        },
                        hidden: vm.workingWO.trTypeCode != 'TR_THUE_MAT_BANG_TRAM' && vm.workingWO.trTypeCode != 'TR_DONG_BO_HA_TANG'
                    },
                    {
                        title: "Thông tin vướng",
                        field: 'dbhtVuong',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.dbhtVuong ? dataItem.dbhtVuong : '';
                        },
                        hidden: vm.workingWO.trTypeCode != 'TR_THUE_MAT_BANG_TRAM' && vm.workingWO.trTypeCode != 'TR_DONG_BO_HA_TANG'
                    },
                    {
                        title: "Thao tác",
                        field: 'del',
                        width: '215px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center; min-width: 210px !important;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return createActionTemplate(dataItem);
                        }
                    }
                ]
            });
        }

        function checkProblem() {
            if (!$scope.hcqtChecklist) return;
            var dueDateList = [];

            for (var i = 0; i < $scope.hcqtChecklist.length; i++) {
                var checklist = $scope.hcqtChecklist[i];
                if (checklist.hasProblem > 0) $scope.hcqtHasProblem = true;
                if (checklist.resolveDueDate) {
                    var dateParts = checklist.resolveDueDate.split("/");
                    var dateObject = new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0]);
                    if (checklist.resolveDueDate) dueDateList.push(new Date(dateObject));
                }
            }

            var maxDueDate = Math.max.apply(null, dueDateList);
            if (maxDueDate) vm.label.maxDueDate = getFormattedDate(new Date(maxDueDate));
        }

        var hcqtCheckListRecord = 0;

        function fillHCQTChecklist() {
            vm.woHCQTCheckListOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save: function () {
                    vm.woHCQTCheckList.dataSource.read();
                },
                dataBinding: function () {
                    hcqtCheckListRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            // $("#appCount").text("" + response.total);
                            // vm.count = response.total;
                            // return response.total;
                        },
                        data: function (response) {
                            if (response.data[0]) $scope.dnqtValue = response.data[0].dnqtValue;
                            if (response.data[2]) $scope.vtnetConfirmValue = response.data[2].vtnetConfirmValue;
                            if (response.data[3]) $scope.aprovedDocValue = response.data[3].aprovedDocValue;
                            $scope.hcqtChecklist = response.data;
                            checkProblem();
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/hcqtChecklist/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var obj = {
                                woId: vm.workingWO.woId
                            };
                            return JSON.stringify(obj)

                        }
                    },
                    pageSize: 10,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: false,
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++hcqtCheckListRecord;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',

                    },
                    {
                        title: "Tên công việc",
                        field: 'constructionCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text',
                        template: function (dataItem) {
                            if (dataItem.checklistName)
                                return dataItem.checklistName;
                            else return vm.workingWO.woName;
                        },

                    },
                    {
                        title: "Trạng thái",
                        field: 'state',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.state;
                        },

                    },
                    {
                        title: "Giá trị (VND)",
                        field: 'state',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return getHcqtChecklistValue(dataItem);
                        },

                    },
                    {
                        title: "Ngày thực hiện",
                        field: 'state',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return getHcqtChecklistDate(dataItem);
                        },

                    },
                    {
                        title: "Ảnh thực hiện",
                        field: 'sysGroupName',
                        width: '25%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            var template = '';
                            for (var i = 0; i < dataItem.lstImgs.length; i++) {
                                template += '<div><img style="width: 150px;" src="data:image/png;base64,' + dataItem.lstImgs[i].imgBase64 + '" ng-click="showBigImg($event, ' + dataItem.lstImgs[i].id + ')"></div>';
                            }
                            return template;
                        }
                    },
                    {
                        title: "Thao tác",
                        field: 'del',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return createHcqtActions(dataItem);
                        }
                    }
                ]
            });

        }

        // AVG checklist
        var avgCheckListRecord = 0;

        function fillAvgChecklist() {
            vm.woAvgCheckListOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save: function () {
                    vm.woAvgCheckList.dataSource.read();
                },
                dataBinding: function () {
                    avgCheckListRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                        },
                        data: function (response) {
                            $scope.avgChecklist = response.data;
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/avgChecklist/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var obj = {
                                woId: vm.workingWO.woId
                            };
                            return JSON.stringify(obj)

                        }
                    },
                    pageSize: 10,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: false,
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++avgCheckListRecord;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',

                    },
                    {
                        title: "Tên công việc",
                        field: 'constructionCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text',
                        template: function (dataItem) {
                            if (dataItem.checklistName)
                                return dataItem.checklistName;
                            else return vm.workingWO.woName;
                        },

                    },
                    {
                        title: "Trạng thái",
                        field: 'state',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.state;
                        },

                    },
                    {
                        title: "Ngày thực hiện",
                        field: 'state',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.completedDate != null ? dataItem.completedDate : '';
                        },

                    },
                    {
                        title: "Ảnh thực hiện",
                        field: 'sysGroupName',
                        width: '25%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            var template = '';
                            for (var i = 0; i < dataItem.lstImgs.length; i++) {
                                template += '<div><img style="width: 150px;" src="data:image/png;base64,' + dataItem.lstImgs[i].imgBase64 + '" ng-click="showBigImg($event, ' + dataItem.lstImgs[i].id + ')"></div>';
                            }
                            return template;
                        }
                    }
                ]
            });

        }
        // AVG checklist - END

        var tkdtCheckListRecord = 0;

        function fillTKDTChecklist() {
            vm.woTKDTCheckListOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save: function () {
                    vm.woTKDTCheckList.dataSource.read();
                },
                dataBinding: function () {
                    tkdtCheckListRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                        },
                        data: function (response) {
                            if (response.data[0]) $scope.dnqtValue = response.data[0].dnqtValue;
                            if (response.data[2]) $scope.vtnetConfirmValue = response.data[2].vtnetConfirmValue;
                            if (response.data[3]) $scope.aprovedDocValue = response.data[3].aprovedDocValue;
                            $scope.tkdtChecklist = response.data;
                            checkProblem();
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            url: Constant.BASE_SERVICE_URL + "woService/hcqtChecklist/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var obj = {
                                woId: vm.workingWO.woId
                            };
                            return JSON.stringify(obj)

                        }
                    },
                    pageSize: 10,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: false,
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++tkdtCheckListRecord;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',

                    },
                    {
                        title: "Tên công việc",
                        field: 'constructionCode',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text',
                        template: function (dataItem) {
                            if (dataItem.checklistName)
                                return dataItem.checklistName;
                            else return vm.workingWO.woName;
                        },

                    },
                    {
                        title: "Trạng thái",
                        field: 'state',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.state;
                        },

                    },
                    {
                        title: "Ngày thực hiện",
                        field: 'state',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return getHcqtChecklistDate(dataItem);
                        },

                    },
                    {
                        title: "Ảnh thực hiện",
                        field: 'sysGroupName',
                        width: '25%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            var template = '';
                            for (var i = 0; i < dataItem.lstImgs.length; i++) {
                                template += '<div><img style="width: 150px;" src="data:image/png;base64,' + dataItem.lstImgs[i].imgBase64 + '" ng-click="showBigImg($event, ' + dataItem.lstImgs[i].id + ')"></div>';
                            }
                            return template;
                        }
                    },
                    {
                        title: "File thẩm định",
                        field: 'attachmentLst',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                        	var html="";
    			        	for (var i=0; i<dataItem.attachmentLst.length; i++){
    			        		var item = dataItem.attachmentLst[i];
    			        		html += "<a href='' class='path-grid' data-path='"+ item.filePath +"' ng-click='vm.downloadAttachmentFile(dataItem)'>"+ item.fileName +"</a> " +
    			        		"<br>";
    			        	};
    			        	return html;
                        }
                    },
                    {
                        title: "Thao tác",
                        field: 'del',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "align:center;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return createTkdtActions(dataItem);
                        }
                    }
                ]
            });

        }

        function getHcqtChecklistValue(dataItem) {
            if (dataItem.checklistOrder == 1) return dataItem.dnqtValue ? dataItem.dnqtValue : '';
            if (dataItem.checklistOrder == 2) return dataItem.vtnetSentValue ? dataItem.vtnetSentValue : '';
            if (dataItem.checklistOrder == 3) return dataItem.vtnetConfirmValue ? dataItem.vtnetConfirmValue : '';
            if (dataItem.checklistOrder == 4) return dataItem.aprovedDocValue ? dataItem.aprovedDocValue : '';
            if (dataItem.checklistOrder == 5) return dataItem.finalValue ? dataItem.finalValue : '';
            return '';
        }

        function getHcqtChecklistDate(dataItem) {
            if (dataItem.checklistOrder == 1) return dataItem.dnqtDate ? dataItem.dnqtDate : '';
            if (dataItem.checklistOrder == 2) return dataItem.vtnetSendDate ? dataItem.vtnetSendDate : '';
            if (dataItem.checklistOrder == 3) return dataItem.vtnetConfirmDate ? dataItem.vtnetConfirmDate : '';
            if (dataItem.checklistOrder == 4) return dataItem.aprovedDocDate ? dataItem.aprovedDocDate : '';
            if (dataItem.checklistOrder == 5) return dataItem.finalDate ? dataItem.finalDate : '';
            return '';
        }

        $scope.isHcqtNo1Done = false;
        $scope.isHcqtNo2Done = false;
        $scope.isHcqtNo3Done = false;
        $scope.isHcqtNo4Done = false;
        $scope.isHcqtNo5Done = false;

        function createHcqtActions(dataItem) {

            if (dataItem.state == 'DONE') {
                if (dataItem.checklistOrder == 1) $scope.isHcqtNo1Done = true;
                if (dataItem.checklistOrder == 2) $scope.isHcqtNo2Done = true;
                if (dataItem.checklistOrder == 3) $scope.isHcqtNo3Done = true;
                if (dataItem.checklistOrder == 4) $scope.isHcqtNo4Done = true;
                if (dataItem.checklistOrder == 5) $scope.isHcqtNo5Done = true;
            }

            if (vm.workingWO.ftId == $scope.sysUserId && dataItem.state != 'DONE') {
                if (dataItem.checklistOrder == 1) return getHcqtActions(dataItem);
                if (dataItem.checklistOrder == 2 && $scope.isHcqtNo1Done) return getHcqtActions(dataItem);
                if (dataItem.checklistOrder == 3 && $scope.isHcqtNo2Done) return getHcqtActions(dataItem);
                if (dataItem.checklistOrder == 4 && $scope.isHcqtNo3Done) return getHcqtActions(dataItem);
            }


            if (dataItem.checklistOrder == 5 && $scope.isHcqtNo4Done && $scope.permissions.approveTcTct && !$scope.isHcqtNo5Done && vm.workingWO.state != 'NG') {
                return '<button class="btn btn-warning" ng-click="vm.tcApprove(' + dataItem.checklistId + ', 3)">Duyệt</button>&nbsp;';
            }

            if (dataItem.checklistOrder != 5 && vm.workingWO.state == 'NG' && vm.workingWO.ftId == $scope.sysUserId) return getHcqtActions(dataItem);

            return '';

        }

        function createTkdtActions(dataItem) {
            var template = '';
            if (dataItem.state == 'NEW') {
                // Neu user dang nhap la cd level 2 thi hien thi nut cho dau
				// viec 1
                // Neu user dang nhap la cd levl 3 thi hien dau viec 2, 3
                if ((($scope.sysUserGroup.sysGroupId == 9008172 || $scope.sysUserGroup.sysGroupId == 166689) && dataItem.checklistOrder == 1) || (($scope.sysUserGroup.sysGroupId == 9008145 || $scope.sysUserGroup.sysGroupId == 166686) && dataItem.checklistOrder == 3 && vm.workingWO.checklistStep == 2)) {
                    template += '<i class="fa fa-picture-o icon-table grey" title="Thêm ảnh" ng-click="vm.uploadHcqtImage(' + dataItem.checklistId + ',' + dataItem.checklistOrder + ')"></i>&nbsp;';
                    template += '<i class="fa fa-check-circle icon-table green" title="Hoàn thành" ng-click="vm.completeChecklistTkdt(' + dataItem.checklistId + ',' + dataItem.lstImgs.length + ')"></i>&nbsp;';
                }
                if(($scope.sysUserGroup.sysGroupId == 9008145 || $scope.sysUserGroup.sysGroupId == 166686) && dataItem.checklistOrder == 2 && vm.workingWO.checklistStep == 1){
//                	template += '<input type="file" file-model="filePackageAcceptance" name="filePackageAcceptance" id="fileUpload" list-file-type="jpeg,jpg,png,doc,docx,xls,xlsx,pdf,JPEG,JPG,PNG,DOC,DOCX,XLS,XLSX,PDF">&nbsp;';
//                	template += '<button ng-click="vm.uploadFile(dataItem)" id="submitAttachFileKcs" style="width: 70px; border-width: 1px;">Tải lên</button>&nbsp;';
                	template +='<button type="button" class="btn btn-qlk padding-search-right ng-scope" ng-click="vm.uploadFile(0)"><icon class="icon-plus"></icon>Tải file</button><input id="fileUpload" type="file" ng-model="" style="display: none">';
                    template += '<i class="fa fa-check-circle icon-table green" title="Hoàn thành" ng-click="vm.completeChecklistTkdtTD(dataItem)"></i>&nbsp;';
                }
            }
            return template;
        }

        function getHcqtActions(dataItem) {
            var template = '';
            if (dataItem.checklistOrder != 5) {
                template += '<i class="fa fa-picture-o icon-table grey" title="Thêm ảnh" ng-click="vm.uploadHcqtImage(' + dataItem.checklistId + ',' + dataItem.checklistOrder + ')"></i>&nbsp;';
                template += '<i class="fa fa-paper-plane icon-table azure" title="Chuyển tiếp" ng-click="vm.openAssignmentModal(' + vm.workingWO.woId + ')"></i>&nbsp;';
                if (dataItem.state != 'PROBLEM') {
                    template += '<i class="fa fa-check-circle icon-table green" title="Hoàn thành" ng-click="vm.completeChecklist(' + dataItem.checklistId + ',' + dataItem.checklistOrder + ')"></i>&nbsp;';
                    template += '<i class="fa fa-exclamation-triangle icon-table" title="Báo vướng" ng-click="vm.hasProblem(' + dataItem.checklistId + ',' + dataItem.checklistOrder + ')"></i>&nbsp;';
                } else {
                    template += '<i class="fa fa-exclamation-triangle icon-table" title="Hết vướng" ng-click="vm.resolveProblem(' + dataItem.checklistId + ',' + dataItem.checklistOrder + ')" style="color: #26c281;"></i>&nbsp;';
                }
            }

            return template;
        }

        function getChecklistItemByOrder(order) {
            for (var i = 0; i < $scope.hcqtChecklist.length; i++) {
                if ($scope.hcqtChecklist[i].checklistOrder == order) return $scope.hcqtChecklist[i];
            }
        }

        function getFormattedDate(date) {
            var year = date.getFullYear();

            var month = (1 + date.getMonth()).toString();
            month = month.length > 1 ? month : '0' + month;

            var day = date.getDate().toString();
            day = day.length > 1 ? day : '0' + day;

            return day + '/' + month + '/' + year;
        }

        vm.completeChecklist = function (checklistId, checklistOrder) {
        	if((vm.workingWO.woNameId == 21 && (vm.workingWO.apWorkSrc == undefined || vm.workingWO.apWorkSrc == null)) || (vm.workingWO.woNameId == 21 && vm.checkWorkSrc == false)){
        		toastr.error(gettextCatalog.getString("Nguồn việc không được bỏ trống! Ghi nhận thay đổi trước khi tiếp tục."));
        		return;
        	}
            var checklistItem = getChecklistItemByOrder(checklistOrder);
            if ((!checklistItem.lstImgs || checklistItem.lstImgs.length == 0) && checklistOrder == 4) {
                toastr.error('Chưa có hình ảnh thực hiện công việc!');
                return;
            }

            $scope.checklistOrder = checklistOrder;
            var obj = {checklistId: checklistId};
            obj.completePersonId = $scope.sysUserId;
            obj.completePersonName = $rootScope.casUser.fullName;
            obj.loggedInUser = $scope.loggedInUser;
            obj.checklistOrder = checklistOrder;
            vm.checklistDate = getFormattedDate(today);

            if (checklistOrder != 5) {
                $modal.open({
                    templateUrl: 'coms/wo_xl/woDetails/woHcqtCompleteChecklistModal.html',
                    controller: null,
                    windowClass: '',
                    scope: $scope
                })
                    .result.then(
                    function () {
                        if (vm.checklistDate == null || vm.checklistDate == '') {
                            toastr('Ngày không được để trống.')
                        } else {
                            if (checklistOrder == 1) {
                                obj.dnqtDate = vm.checklistDate;
                                obj.dnqtValue = vm.dnqtValue;
                            }

                            if (checklistOrder == 2) {
                                obj.vtnetSendDate = vm.checklistDate;
                                obj.vtnetSentValue = $scope.dnqtValue;
                            }

                            if (checklistOrder == 3) {
                                obj.vtnetConfirmDate = vm.checklistDate;
                                obj.vtnetConfirmValue = vm.vtnetConfirmValue;
                                obj.aprovedPerson = vm.aprovedPerson;
                            }

                            if (checklistOrder == 4) {
                                obj.aprovedDocDate = vm.checklistDate;
                                obj.aprovedDocValue = $scope.vtnetConfirmValue;
                                obj.emailTcTct = vm.workingWO.emailTcTct;
                            }

                            doneChecklist(obj);
                        }
                    },
                    function () {
                    }
                )
            } else {
                obj.finalValue = $scope.vtnetConfirmValue;
                doneChecklist(obj);
            }

        };

        function doneChecklist(obj) {
            confirm('Xác nhận hoàn thành đầu việc ?', function () {
                woDetailsService.completeHcqtChecklist(obj).then(
                    function (resp) {
                        toastr.success("Thực hiện thành công");
                        getWODetails(vm.workingWO.woId, true);
                        reloadTables();
                    },
                    function (err) {
                        console.log(err);
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    }
                )
            });
        };

        vm.completeChecklistTkdt = function (checklistId, lstImgsLength) {
//        	if(lstImgsLength==0){
//        		toastr.warning("Chưa đính kèm file thực hiện");
//        		return;
//        	}
            var obj = {checklistId: checklistId};
            obj.loggedInUser = $scope.loggedInUser;
            confirm('Xác nhận hoàn thành đầu việc ?', function () {
                woDetailsService.completeTkdtChecklist(obj).then(
                    function (resp) {
                        toastr.success("Thực hiện thành công");
                        getWODetails(vm.workingWO.woId, true);
                        reloadTables();
                    },
                    function (err) {
                        console.log(err);
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    }
                )
            });
        };
        vm.completeChecklistTkdtTD = function (dataItem) {
        	if(dataItem.attachmentLst.length < 1){
        		toastr.error("Bắt buộc chọn file");
        		return;
        	}
            var obj = {checklistId: dataItem.checklistId};
            obj.loggedInUser = $scope.loggedInUser;
            confirm('Xác nhận hoàn thành đầu việc ?', function () {
                woDetailsService.completeTkdtChecklist(obj).then(
                    function (resp) {
                        toastr.success("Thực hiện thành công");
                        getWODetails(vm.workingWO.woId, true);
                        reloadTables();
                    },
                    function (err) {
                        console.log(err);
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    }
                )
            });
        };

        vm.hasProblem = function (checklistId, checklistOrder) {
            woDetailsService.getHcqtIssueList({}).then(
                function (resp) {
                    $scope.problems = resp.data;
                    vm.openHcqtIssueModal(checklistId, checklistOrder);
                },
                function (err) {
                    console.log(err);
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            );
        };

        vm.openHcqtIssueModal = function (checklistId, checklistOrder) {
            $scope.checklistOrder = checklistOrder;
            var obj = {checklistId: checklistId};
            obj.completePersonId = $scope.sysUserId;
            obj.completePersonName = $rootScope.casUser.fullName;
            $modal.open({
                templateUrl: 'coms/wo_xl/woDetails/woHcqtChecklistProblem.html',
                controller: null,
                windowClass: '',
                scope: $scope
            })
                .result.then(
                function () {
                    console.log(vm.problem);
                    vm.declareHcqtIssue(checklistId, checklistOrder, vm.problem);
                },
                function () {
                }
            )
        };

        vm.declareHcqtIssue = function (checklistId, checklistOrder, problem) {
            var obj = {checklistId: checklistId};
            obj.loggedInUser = $scope.loggedInUser;
            obj.problemCode = problem.code;
            obj.problemName = problem.name;
            obj.content = vm.problemContent;
            obj.problemDeclarePersonName = $rootScope.casUser.fullName;
            obj.problemDeclarePersonId = $rootScope.casUser.sysUserId;

            woDetailsService.declareHcqtIssue(obj).then(
                function (resp) {
                    if (resp && resp.statusCode == 1) {
                        toastr.success('Báo vướng thành công.');
                        reloadTables();
                    }
                    vm.problem.code = '';
                    vm.problem.name = '';
                    vm.problemContent = '';
                },
                function (err) {
                    console.log(err);
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            );
        };

        vm.resolveProblem = function (checklistId, checklistOrder) {
            confirm('Xác nhận xử lý xong vướng?', function () {
                var obj = {checklistId: checklistId};
                obj.loggedInUser = $scope.loggedInUser;
                obj.checklistOrder = checklistOrder;

                woDetailsService.resolveHcqtIssue(obj).then(
                    function (resp) {
                        if (resp && resp.statusCode == 1) {
                            toastr.success('Đã hết vướng, có thể hoàn thành công việc. ');
                            reloadTables();
                        }
                    },
                    function (err) {
                        console.log(err);
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    }
                );
            });
        };

        function reloadTables() {
            if (vm.woCheckList) vm.woCheckList.dataSource.read();
            if (vm.woHCQTCheckList) vm.woHCQTCheckList.dataSource.read();
            if (vm.woHistory) vm.woHistory.dataSource.read();
            if (vm.woTKDTCheckList) vm.woTKDTCheckList.dataSource.read();
        }

        $scope.showBigImg = function (event, id) {
            var popupTemplate = '<div style="position: relative"><img style="max-width: 600px;" src="' + event.target.currentSrc + '">';
            if (vm.workingWO.woTypeCode == 'HCQT' && vm.workingWO.state != 'OK') popupTemplate += '<div class="delete-image-btn" ng-click="vm.removeAttachment(' + id + ')">-</div>';
            popupTemplate += '</div>';
            $modal.open({
                template: popupTemplate,
                controller: null,
                windowClass: 'big-image-modal',
                scope: $scope
            })
                .result.then(
                function () {
                },
                function () {
                }
            )

        };

        function getCatWorkName() {

            if (vm.workingWO.catWorkItemTypeId == -9999) {
                vm.label.catWorkName = "Hạng mục theo Hợp đồng AIO";
                return;
            }

            var obj = {catWorkItemTypeId: vm.workingWO.catWorkItemTypeId, constructionId: vm.workingWO.constructionId}
            woDetailsService.getCatWorkName(obj).then(
                function (res) {
                    if (res && res.data) vm.label.catWorkName = res.data;
                    else vm.label.catWorkName = vm.workingWO.catWorkItemTypeName;
                }
            )
        }

        var woHistoryRecord = 0;

        function fillDataWoHistory() {
            vm.woHistoryOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                save: function () {
                    vm.woHistory.dataSource.read();
                },
                dataBinding: function () {
                    woHistoryRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: false,
                    schema: {
                        total: function (response) {
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/workLogs/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var obj = {woId: vm.workingWO.woId};
                            return JSON.stringify(obj);
                        }
                    },
                    pageSize: 9999,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: false,
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++woHistoryRecord;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',

                    },
                    {
                        title: "Thời gian",
                        field: 'logTime',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.logTimeStr;
                        },
                    },
                    {
                        title: "Nội dung",
                        field: 'content',
                        width: '45%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.content;
                        }
                    },
                    {
                        title: "Người tác động",
                        field: 'userCreated',
                        width: '40%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.userCreated;
                        }
                    },
                ]
            });
        }

        var attRecord = 0;

        function fillDataFileAttachments() {
            vm.fileAttachDataTblOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                save: function () {
                    vm.fileAttachDataTbl.dataSource.read();
                },
                dataBinding: function () {
                    attRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            // $("#appCount").text("" + response.total);
                            // vm.count = response.total;
                            // return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/fileAttach/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var fileAttSearchOptions = {woId: vm.workingWO.woId, trId: vm.workingWO.trId};
                            fileAttSearchOptions.page = options.page;
                            fileAttSearchOptions.pageSize = options.pageSize;
                            return JSON.stringify(fileAttSearchOptions)
                        }
                    },
                    pageSize: 10,
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
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++attRecord;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',

                    },
                    {
                        title: "Tên file",
                        field: 'fileName',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.fileName;
                        },
                    },
                    {
                        title: "Người tải lên",
                        field: 'userCreated',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'number',
                        // format: '{0:n3}',
                        template: function (dataItem) {
                            return dataItem.userCreated;
                        },

                    },
                    {
                        title: "Ngày tải lên",
                        field: 'timeUpload',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.createdDate ? dataItem.createdDate : '';
                        }
                    },
                    {
                        title: "Thao tác",
                        field: 'sysGroupName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return '<div class="display-block cedtpl">' +
                                '<i class="fa fa-download azure fs20" ng-click=vm.downloadAttachment("' + dataItem.id + '")></i> &nbsp;' +
                                '<i ng-if="enable.removeAttachment" class="fa fa-trash-o azure fs20" ng-click=vm.removeAttachment("' + dataItem.id + '")></i>' +
                                '</div>'
                        }
                    },
                ]
            });
        }

        vm.addAttachment = function (type) {
        	var idString = "#woAttachmentFileId";
            if (type == 1) {
                idString = "#woAttachmentFileHd";
            }
            if (type == 2) {
                idString = "#woAttachmentFileBbks";
            }
            $(idString).unbind().click();
            $(idString).change(function () {
                var selectedFile = $(idString)[0].files[0];
                var reader = new FileReader();
                var formData = new FormData();
                formData.append('multipartFile', selectedFile);
                // formData.append('folder', '/input/mobile_images');
                // send file through fileService
                var uploadFileService = Constant.BASE_SERVICE_URL + 'fileService/uploadWoFileATTT?folder=' + Constant.UPLOAD_FOLDER_IMAGES;
                woDetailsService.uploadAttachment(uploadFileService, formData)
                    .success(function (resp) {
                        console.log(resp);

                        if (resp[0]) {
                            var filePath = resp[0];
                            var fileName = selectedFile.name;
                            var mappingObj = {
                                woId: vm.workingWO.woId,
                                fileName: fileName,
                                filePath: filePath,
                                userCreated: $scope.loggedInUser,
                                status: 1
                            }

                            woDetailsService.mappingAttachmentToWo(mappingObj).then(
                                function (resp) {
                                    console.log(resp);
                                    toastr.success("Thêm mới thành công");

                                    vm.fileAttachDataTbl.dataSource.read();
                                },
                                function (error) {
                                    console.log(error);
                                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                                }
                            )
                        }

                        if (type == 1) {
                            $scope.attachHd = true;
                        }
                        if (type == 2) {
                            $scope.attachBbks = true;
                        }
                    })
                    .error(function (err) {
                        console.log(err);
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    });
            });
            //
        };

        vm.removeAttachment = function (attId) {
            confirm('Bạn có muốn xóa file này?', function () {
                woDetailsService.removeAtachment({id: attId}).then(
                    function (resp) {
                        console.log(resp);
                        toastr.success("Xóa thành công");

                        vm.fileAttachDataTbl.dataSource.read();
                        $modalStack.dismissAll();
                        if (vm.woHCQTCheckList && vm.woHCQTCheckList.dataSource) vm.woHCQTCheckList.dataSource.read();
                    },
                    function (err) {
                        console.log(err);
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    }
                )
            })
        }

        vm.downloadAttachment = function (attId) {
            confirm('Bạn có muốn tải về file đính kèm này?', function () {
                var obj = {
                    id: attId,
                    page: 1,
                    pageSize: 1
                }
                woDetailsService.doSearchAtachment(obj).then(
                    function (resp) {
                        console.log(resp);
                        if (resp && resp.data) {
                            var filePath = resp.data[0].filePath;
                            var fileName = resp.data[0].fileName;
                            // send file through fileService
                            var downloadLink = Constant.BASE_SERVICE_URL + 'fileService/downloadFileATTT?' + filePath;
                            saveFile(data, fileName, downloadLink);
                        }
                    },
                    function (err) {
                        console.log(err);
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    }
                )
            });
        };

        function saveFile(data, fileName, url) {
            var linkElement = document.createElement('a');
            try {
                linkElement.setAttribute('href', url);
                linkElement.setAttribute("download", fileName);

                var clickEvent = new MouseEvent("click", {
                    "view": window,
                    "bubbles": true,
                    "cancelable": false
                });
                linkElement.dispatchEvent(clickEvent);
            } catch (ex) {
                console.log(ex);
            }
        }

        function getStateText(state) {
            Object.keys(Constant.WO_XL_STATE).forEach(
                function (key, index) {
                    if (Constant.WO_XL_STATE[key].stateCode == state) {
                        vm.label.stateText = Constant.WO_XL_STATE[key].stateText;
                    }
                }
            );
        }

        function checkIsAssignable(woItem) {
            if (!$scope.permissions.cdWO) return false;

            if (woItem.state == "UNASSIGN") return true;

            if (woItem.state == "ACCEPT_CD" || woItem.state == 'REJECT_FT') {

                if (($scope.sysUserGroup.sysGroupId == woItem.cdLevel1 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel1)) && !woItem.cdLevel2 && !woItem.cdLevel5) return true;

                if (($scope.sysUserGroup.sysGroupId == woItem.cdLevel2 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel2)) && !woItem.cdLevel3) return true;

                if (($scope.sysUserGroup.sysGroupId == woItem.cdLevel3 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel3)) && !woItem.cdLevel4) return true;

                if (($scope.sysUserGroup.sysGroupId == woItem.cdLevel4 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel4)) && !woItem.ftId) return true;

                if ($scope.sysUserId == woItem.cdLevel5) return true;

            }

            if (woItem.state == "REJECT_FT" && ($scope.sysUserGroup.sysGroupId == woItem.cdLevel4 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel4))) return true;

            if (woItem.state == "REJECT_FT" && ($scope.sysUserGroup.sysGroupId == woItem.cdLevel2 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel2)) && !woItem.cdLevel3) return true;

            return false;
        }

        function checkIsAcceptable(woItem) {
            if (woItem.state == "ASSIGN_FT" && $scope.sysUserId == woItem.ftId) return true;

            if (!$scope.permissions.cdWO) return false;
            if (woItem.state == "ASSIGN_CD") {
                if (!woItem.cdLevel5) {

                    if (($scope.sysUserGroup.sysGroupId == woItem.cdLevel1 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel1)) && !woItem.cdLevel2) return true;

                    if (($scope.sysUserGroup.sysGroupId == woItem.cdLevel2 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel2)) && !woItem.cdLevel3) return true;

                    if (($scope.sysUserGroup.sysGroupId == woItem.cdLevel3 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel3)) && !woItem.cdLevel4) return true;

                    if ($scope.sysUserGroup.sysGroupId == woItem.cdLevel4 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel4)) return true;
                } else {
                    if ($scope.sysUserId == woItem.cdLevel5) return true;
                }
            } else if (woItem.state == "NG") {
                // Nếu loại wo là DOANHTHU_DTHT và FT là user đăng nhập
                if (woItem.woTypeCode == "DOANHTHU_DTHT" && $scope.sysUserId == woItem.ftId) {
                    return false;
                }
            }
            // Huypq-05072021-start
            if (woItem.woTypeCode == "TTHQ" && woItem.state == "NG" && ($scope.sysUserGroup.sysGroupId == woItem.cdLevel2 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel2))) return true;
            // Huy-end
            return false;
        }

        function checkIsRejectable(woItem) {

            if (woItem.state == "ASSIGN_FT" && $scope.sysUserId == woItem.ftId) return true;
            // if(woItem.state == "ASSIGN_CD") return true;
            return false;
        }

        function checkIsOpinionAsking(woItem) {

            if (!$scope.permissions.cdWO) return false;

            if (woItem.state == "OPINION_RQ_4" && ($scope.sysUserGroup.sysGroupId == woItem.cdLevel4 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel4)) && (woItem.opinionResult == '' || woItem.opinionResult == null)) return true;

            if (woItem.state == "OPINION_RQ_3" && ($scope.sysUserGroup.sysGroupId == woItem.cdLevel3 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel3)) && (woItem.opinionResult == '' || woItem.opinionResult == null)) return true;

            if (woItem.state == "OPINION_RQ_2" && ($scope.sysUserGroup.sysGroupId == woItem.cdLevel2 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel2)) && (woItem.opinionResult == '' || woItem.opinionResult == null)) return true;

            if (woItem.state == "OPINION_RQ_1" && ($scope.sysUserGroup.sysGroupId == woItem.cdLevel1 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel1)) && (woItem.opinionResult == '' || woItem.opinionResult == null)) return true;

            return false;
        }

        function checkIsReAssignable(woItem) {
            if (!$scope.permissions.cdWO) return false;

            if (woItem.state == "ASSIGN_CD") {

                if (($scope.sysUserGroup.sysGroupId == woItem.cdLevel1 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel1)) && woItem.cdLevel2 && !woItem.cdLevel3) return true;

                if (($scope.sysUserGroup.sysGroupId == woItem.cdLevel2 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel2)) && woItem.cdLevel3 && !woItem.cdLevel4 && woItem.cdLevel2 != woItem.cdLevel3) return true;

                if (($scope.sysUserGroup.sysGroupId == woItem.cdLevel3 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel3)) && woItem.cdLevel4 && !woItem.ftId) return true;

                if (woItem.userCreated == $scope.loggedInUser && woItem.cdLevel5) return true;
            }


            if (woItem.state == "ASSIGN_FT" && ($scope.sysUserGroup.sysGroupId == woItem.cdLevel4 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel4))) return true;
            if (woItem.state == "ASSIGN_FT" && 'HCQT' == woItem.woTypeCode && ($scope.sysUserGroup.sysGroupId == woItem.cdLevel1 || $scope.permissions.cdDomainDataList.includes(woItem.cdLevel1))) return true;

            return false;
        }

        function checkDone(wo) {
            if (wo.state == "DONE" && wo.woTypeCode == '5S' && $scope.permissions.cdDomainDataList.includes(wo.cdLevel2)) return true;

            if (wo.state == "DONE" && wo.woTypeCode != 'HCQT' && wo.woTypeCode != '5S') return true;

            return false;
        }

        function checkCanApproveCdOk() {
            if ($scope.sysUserId == vm.workingWO.cdLevel5) return true;
            if ($scope.permissions.cdDomainDataList.includes(vm.workingWO.cdLevel4)) return true;
            if ($scope.permissions.cdDomainDataList.includes(vm.workingWO.cdLevel3)) return true;
            if ($scope.permissions.cdDomainDataList.includes(vm.workingWO.cdLevel2)) return true;
            return false;
        }

        function checkCdOk(woItem) {
            if (woItem.state == "CD_OK" && $scope.permissions.cdDomainDataList && $scope.permissions.cdDomainDataList.includes(woItem.cdLevel1)) {
                if (woItem.woTypeCode != 'HSHC') {
                    return true;
                } else {
                    if ($scope.permissions.approveRevenueSalary || vm.workingWO.cdLevel1 == '9006003') {
                        return true;
                    }
                }
            }

            return false;
        }

        function checkOk(woItem) {
            if (woItem.state == "OK") return true;
            return false;
        }

        vm.openAssignmentModal = function (woId, type) {
            $scope.workingWO = vm.workingWO;
            if(type = 'REASSIGN_ADMIN_WO' && (vm.workingWO.state == 'ASSIGN_FT' || vm.workingWO.state == 'ACCEPT_FT' || vm.workingWO.state == 'PROCESSING')){
            	$scope.checkAsignAdminWo = vm.checkAsignAdminWo;
            }
            if (type == 'REASSIGN') {
                $scope.isReassigning = true;
            } else {
                $scope.isReassigning = false;
            }

            $modal.open({
                templateUrl: 'coms/wo_xl/common/giveAssignmentModal.html?v=34k2',
                controller: 'giveAssignmentModalController',
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function () {
                    vm.giveAssignment(woId, $scope.selectedUnit);
                },
                function () {
                    $scope.workingWO = {};
                }
            )
        }

        vm.giveAssignment = function (woId, selectedUnit) {
        	console.log(selectedUnit);
            console.log(vm.workingWO);
            // Duonghv13 add 29092021
            if( ($scope.sysUserGroup.isCdLevel2 && !$scope.isHCQT) || $scope.checkAssignWOS ){
            	 if($scope.selectedUnit.cdLv3 == null && $scope.selectedUnit.isFt2 == true){
            		 if($("#autoCompleteFt2").val().trim()==""|| selectedUnit.ft == null){
            			 toastr.error("Chưa chọn cụ thể FT2!");
            			 $scope.selectedUnit ={};
                         return;
            		 }
            	 }
            	 if($scope.selectedUnit.isFt2 == false){
            		 if($scope.selectedUnit.cdLv3 == null || $scope.selectedUnit.cdLv3 == undefined ){
            			 toastr.error("Chưa chọn cụ thể đối tượng giao việc!");
            			 $scope.selectedUnit ={};
                         return;
            		 }
            	 }
            }
            if (!selectedUnit || Object.keys(selectedUnit).length === 0||selectedUnit == null || selectedUnit == undefined  ) {
                toastr.error("Chưa chọn đối tượng giao việc!");
                $scope.selectedUnit ={};
                return;
            }
            //Duong-end 29092021
            //Duonghv13-start 14092021
            var obj = {
                woId: woId,
                loggedInUser: $scope.loggedInUser,
                woTypeCode: vm.workingWO.woTypeCode,
                cdLevel1: vm.workingWO.cdLevel1,
                cdLevel2: vm.workingWO.cdLevel2,
                cdLevel3: vm.workingWO.cdLevel3,
                cdLevel4: vm.workingWO.cdLevel4,
                cdLevel5: vm.workingWO.cdLevel5,
                //Duonghv13-end
                woTypeId: vm.workingWO.woTypeId //Duonghv13-start 18102021

            }

            //Duonghv13-start 14092021
            if(vm.workingWO.woTypeCode =='XLSC'){
            	if (selectedUnit.cdLv4) {
   	             obj.cdLevel4 = selectedUnit.cdLv4.sysGroupId;
   	             obj.cdLevel4Name = selectedUnit.cdLv4.groupName;
               }else{
	               	if(!selectedUnit.isFt && vm.workingWO.cdLevel4 == null){
	   	            	toastr.error("Chưa chọn cụ thể Cụm đội!");
	   	            	$scope.selectedUnit ={};
	   	                return;
	               	}
                 }
                //Duonghv13-end
                //Duonghv13-start 14092021
                 if (selectedUnit.cdLv5) {
                   obj.cdLevel5 = selectedUnit.cdLv5.userId;
                   obj.cdLevel5Name = selectedUnit.cdLv5.userName;
                 }
                 if(selectedUnit.isFt){
                	 if ($("#autoCompleteFt1").val().trim()==""|| selectedUnit.isFt == null) {
  	                   toastr.error("Chưa chọn cụ thể FT!");
  	                   $scope.selectedUnit ={};
  	                   return;
  	                 }
                     obj.ftId = selectedUnit.ft.ftId;
                     obj.ftName = selectedUnit.ft.fullName;
                     obj.ftEmail = selectedUnit.ft.email;
                 }

            }
            //Duonghv13-end

            // AVG - start
            if(vm.workingWO.woTypeCode =='AVG'){
                if (selectedUnit.cdLv2) {
                    obj.cdLevel3 = selectedUnit.cdLv2.sysGroupId;
                    obj.cdLevel3Name = selectedUnit.cdLv2.groupName;
                } else if (selectedUnit.cdLv4) {
                    obj.cdLevel4 = selectedUnit.cdLv4.sysGroupId;
                    obj.cdLevel4Name = selectedUnit.cdLv4.groupName;
                }
            }
            // AVG - end

            if (vm.workingWO.woTypeCode == 'THICONG'
                && vm.workingWO.catConstructionTypeId == 8
                && $scope.checklists.length == 0
                && ($scope.permissions.cdDomainDataList.includes(vm.workingWO.cdLevel2) || vm.workingWO.cdLevel5 == $scope.sysUserId)
            ) {
                toastr.error("Cần tạo đầu việc XDDD trước khi giao xuống");
                return;
            }


            if (selectedUnit.cdLv2) {
                obj.cdLevel2 = selectedUnit.cdLv2.sysGroupId;
                obj.cdLevel2Name = selectedUnit.cdLv2.groupName;
            }

            if (selectedUnit.cdLv3) {
                obj.cdLevel3 = selectedUnit.cdLv3.sysGroupId;
                obj.cdLevel3Name = selectedUnit.cdLv3.groupName;
            }
            if (selectedUnit.cdLv4) {
 	             obj.cdLevel4 = selectedUnit.cdLv4.sysGroupId;
 	             obj.cdLevel4Name = selectedUnit.cdLv4.groupName;
            }
       	 	if (selectedUnit.cdLv5) {
               obj.cdLevel5 = selectedUnit.cdLv5.userId;
               obj.cdLevel5Name = selectedUnit.cdLv5.userName;

            }
	       	if (selectedUnit.ft){
	        	  obj.ftId = selectedUnit.ft.ftId;
	             obj.ftName = selectedUnit.ft.fullName;
	             obj.ftEmail = selectedUnit.ft.email;
	        }
       	 	if (selectedUnit.isFt){
              	  obj.ftId = selectedUnit.ft.ftId;
                  obj.ftName = selectedUnit.ft.fullName;
                  obj.ftEmail = selectedUnit.ft.email;
            }
	       	if (selectedUnit.isFt2){
	         	  obj.ftId = selectedUnit.ft.ftId;
	              obj.ftName = selectedUnit.ft.fullName;
	              obj.ftEmail = selectedUnit.ft.email;
	        }
	       	if(vm.checkAsignAdminWo && (vm.workingWO.state == 'ASSIGN_FT' || vm.workingWO.state == 'ACCEPT_FT' || vm.workingWO.state == 'PROCESSING')){
	       		obj.state = vm.workingWO.state;
	       	}


            woDetailsService.giveAssignment(obj).then(
                function (resp) {
                	if(resp && resp.error){
                		toastr.error(resp.error);
                		return;
                	}
                    if (resp && resp.statusCode == 1) {
                        publishChange();
                        getWODetails(woId);
                        toastr.success("Giao việc thành công.");
                    } else {
                        toastr.error(resp.message);
                    }

                    getWODetails(woId);
                    vm.woHistory.dataSource.read();
                    if (vm.woHCQTCheckList) vm.woHCQTCheckList.dataSource.read();
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        };

        vm.acceptWO = function (woId) {

            confirm('Xác nhận đồng ý nhận việc WO?',
                function () {
                    var obj = {
                        woId: woId,
                        loggedInUser: $scope.loggedInUser,
                        ftEmail: $rootScope.casUser.email,
                        ftId: $rootScope.casUser.sysUserId,
                        ftName: $rootScope.casUser.fullName
                    };

                    woDetailsService.acceptWO(obj).then(
                        function (resp) {
                            console.log(resp);

                            if (resp && resp.statusCode == 1) {
                                publishChange();
                                toastr.success("Nhận việc thành công.");
                            } else toastr.error("Có lỗi xảy ra!");

                            getWODetails(woId);
                            vm.woHistory.dataSource.read();
                            if (vm.woHCQTCheckList) vm.woHCQTCheckList.dataSource.read();
                        },
                        function (error) {
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                        }
                    )
                }
            );
        };

        vm.rejectWO = function (woId, reason) {

            confirm('Xác nhận từ chối WO? Lý do: ' + reason,
                function () {
                    var obj = {
                        woId: woId,
                        loggedInUser: $scope.loggedInUser,
                        text: reason
                    };

                    woDetailsService.rejectWO(obj).then(
                        function (resp) {
                            console.log(resp);

                            if (resp && resp.statusCode == 1) {
                                publishChange();
                                toastr.success("Từ chối thành công.");
                            } else toastr.error("Có lỗi xảy ra!");

                            getWODetails(woId);
                            vm.woHistory.dataSource.read();
                        },
                        function (error) {
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                        }
                    )
                }
            );
        };

        // ThanhPT - FT từ chối nhận WO BGBTS_VHKT - Start
        vm.rejectWoBgbtsVhkt = function (woId, reason) {
            confirm('Xác nhận từ chối WO? Lý do: ' + reason,
                function () {
                    var obj = {
                        woId: woId,
                        loggedInUser: $scope.loggedInUser,
                        text: reason
                    };
                    woDetailsService.rejectWoBgbtsVhkt(obj).then(
                        function (resp) {
                            console.log(resp);

                            if (resp && resp.statusCode == 1) {
                                publishChange();
                                toastr.success("Từ chối thành công.");
                            } else toastr.error("Có lỗi xảy ra!");

                            getWODetails(woId);
                            vm.woHistory.dataSource.read();
                        },
                        function (error) {
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                        }
                    )
                }
            );
        };
        // ThanhPT - FT từ chối nhận WO BGBTS_VHKT - End

        vm.cloneThisWO = function () {
            $rootScope.cloneWo = vm.workingWO;
            $rootScope.isClonning = 1;

            var template = Constant.getTemplateUrl('WO_XL_WO_CREATE_NEW');
            postal.publish({
                channel: "Tab",
                topic: "open",
                data: template
            });
        };

        function publishChange() {
            postal.publish({
                channel: "Tab",
                topic: "action",
                data: {action: 'refresh'}
            });
        }

        vm.openOpinionProcessModal = function (woId, type) {

            switch (type) {
                case 'ACCEPT':
                    $scope.opinionOrderLabel.text = 'Chấp thuận ý kiến';
                    $scope.headerColor = {'background-color': '#204d74'};
                    break;
                case 'REJECT':
                    $scope.opinionOrderLabel.text = 'Từ chối ý kiến';
                    $scope.headerColor = {'background-color': '#F1C40F'};
                    break;
                case 'PASS_UP':
                    $scope.opinionOrderLabel.text = 'Chuyển ý kiến lên trên';
                    $scope.headerColor = {'background-color': '#ed6b75'};
                    break;
            }

            $modal.open({
                templateUrl: 'coms/wo_xl/common/acceptRejectOpinionModal.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function () {
                    console.log($scope.opinionComment.text);
                    vm.processOpinion(woId, type);
                },
                function () {
                    $scope.opinionOrderLabel.text = '';
                    $scope.headerColor = {};
                }
            )
        }

        vm.processOpinion = function (woId, type) {
            var toastrTxt = '';
            var obj = {woId: woId, loggedInUser: $scope.loggedInUser, opinionComment: $scope.opinionComment.text}
            switch (type) {
                case 'ACCEPT':
                    obj.opinionResult = 'ACCEPTED';
                    toastrTxt = 'Đã chấp thuận ý kiến được xin.';
                    break;
                case 'REJECT':
                    obj.opinionResult = 'REJECTED';
                    toastrTxt = 'Đã từ chối ý kiến được xin.';
                    break;
                case 'PASS_UP':
                    if (vm.workingWO.state == 'OPINION_RQ_4') obj.state = 'OPINION_RQ_3';
                    else if (vm.workingWO.state == 'OPINION_RQ_3') obj.state = 'OPINION_RQ_2';
                    else if (vm.workingWO.state == 'OPINION_RQ_2') obj.state = 'OPINION_RQ_1';

                    toastrTxt = 'Đã chuyển ý kiến được xin lên cấp trên.';
                    break;
            }

            woDetailsService.processOpinion(obj).then(
                function (resp) {
                    console.log(resp);
                    if (resp && resp.statusCode == 1) {
                        publishChange();
                        toastr.success(toastrTxt);
                    } else toastr.error("Có lỗi xảy ra!");
                    getWODetails(woId);
                    vm.woHistory.dataSource.read();
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        };

        // ThanhPT - Nâng cấp đánh giá cv wo BGBTS-VHKT - start
        $scope.openCdOkOrNgModal = function () {
            //ducpm23 add
            $scope.confirmWO.catWorkItemTypeId = vm.workingWO.catWorkItemTypeId;
            if (vm.workingWO.contractId) {
                woDetailsService.checkContractIsGpxd(vm.workingWO.contractId).then(function (d) {
                    if (d && d == 'OK') {
                        vm.isGpxd = true;
                    } else {
                        vm.isGpxd = false;
                    }
                }).catch(function () {
                    vm.isGpxd = false;
                });
            } else {
                vm.isGpxd = false;
            }

            if (vm.workingWO.woTypeCode == 'DOANHTHU_DTHT') {
                var gridFileWo = [];
                var grid = vm.fileAttachDataTbl.dataSource.data();
                for (var i = 0; i < grid.length; i++) {
                    if (grid[i].woId != null) {
                        gridFileWo.push(grid[i]);
                    }
                }
                if (gridFileWo.length == 0) {
                    toastr.warning("Chưa đính kèm file cho WO !");
                    return;
                }
            }

            $scope.ngMissingImageSelected = [];
            $scope.isAcceptRejectQuantity = false;
            $scope.rejectComment = {};
            $scope.confirmWO.woTypeCode = vm.workingWO.woTypeCode;
            $scope.confirmWO.isAcceptRejectQuantity = true;
            $modal.open({
                templateUrl: 'coms/wo_xl/common/okOrNotGoodModal.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            }).result.then(
                function () {
                    kendo.ui.progress($(".tab-content"), true);
                    if ($scope.confirmWO.isWoDoneOk == undefined) {
                        toastr.error("Chưa chọn đánh giá!");
                        kendo.ui.progress($(".tab-content"), false);
                        return;
                    } else {
                        if ($scope.confirmWO.isWoDoneOk) {
                            vm.isApprovedTmbt = true;
                            // Neu la TMBT thi phai dinh kem hop dong va bien
                            // ban khao sat
                            if (vm.workingWO.woTypeCode == 'TMBT') {
                                if (!$scope.attachHd) {
                                    toastr.warning('Chưa đính kèm file hợp đồng !');
                                    vm.isApprovedTmbt = false;
                                    kendo.ui.progress($(".tab-content"), false);
                                    return;
                                } else if (!$scope.attachBbks) {
                                    toastr.warning('Chưa đính kèm file biên bản khảo sát !');
                                    vm.isApprovedTmbt = false;
                                    kendo.ui.progress($(".tab-content"), false);
                                    return;
                                }
                            }

                            //Huypq-02112021-start
                            if (vm.workingWO.state == 'PAUSE' && vm.workingWO.opinionType != null) {
                                changeStateCdPause(vm.workingWO.opinionType);
                                kendo.ui.progress($(".tab-content"), false);
                            } else
                                //Huy-end
                            if (vm.isApprovedTmbt) {
                                changeStateAndAcceptQuantity();
                                kendo.ui.progress($(".tab-content"), false);
                            }
                        } else {
                            if (vm.workingWO.woTypeCode == 'BGBTS_DTHT') {
                                $scope.rejectComment.text = '';
                                for (var i = 0; i < $scope.ngMissingImageSelected.length; i++) {
                                    var checklist = $scope.ngMissingImageSelected[i];
                                    $scope.rejectComment.text += checklist.name + ",";
                                    vm.listChecklistId.push(checklist.parOrder);
                                }
                                if ($scope.confirmWO.bgbtsResult == '2' && $scope.rejectComment.text == '') {
                                    $scope.rejectComment.text = 'Thi công chưa đạt';
                                }
                            }

                            if ($scope.rejectComment.text == undefined || $scope.rejectComment.text == '') {
                                toastr.error('Lý do từ chối không được để trống. ');
                                kendo.ui.progress($(".tab-content"), false);
                                return;
                            } else {
                                //Huypq-02112021-start
                                if (vm.workingWO.state == 'PAUSE' && vm.workingWO.opinionType != null) {
                                    changeStateCdPauseReject(vm.workingWO.opinionType);
                                    kendo.ui.progress($(".tab-content"), false);
                                } else {
                                    changeStateCdNg();
                                    kendo.ui.progress($(".tab-content"), false);
                                }
                                //Huy-end
                            }
                        }
                    }
                },
                function () {
                    $scope.confirmWO.isWoDoneOk = true;
                    kendo.ui.progress($(".tab-content"), false);
                }
            )
        };
        // ThanhPT - Nâng cấp đánh giá cv wo BGBTS-VHKT - end

        function changeStateAndAcceptQuantity() {
            if ($scope.remainQuantityByDate == true) {
                for (var i = 0; i < $scope.checklistts.length; i++) {
                    var checklist = $scope.checklists[i];
                    if (checklist.quantityByDate && checklist.quantityByDate > 0 && checklist.unapprovedQuantity && checklist.unapprovedQuantity > 0) {
                        vm.acceptQuantityLength(checklist.id, checklist.quantityLength, checklist.unapprovedQuantity, true);
                    }
                }
                changeStateCdOk();
            } else changeStateCdOk();
        }

        function changeStateCdOk() {
            var obj = {
                ftId: vm.workingWO.ftId,
                woId: vm.workingWO.woId,
                loggedInUser: $scope.loggedInUser,
                text: $scope.rejectComment.text,
                // Huypq-03092020-start
                moneyValue: vm.label.moneyValueMil,
                woTypeCode: vm.workingWO.woTypeCode,
                state: vm.workingWO.state,
                apConstructionType: vm.workingWO.apConstructionType,
                woTypeId: vm.workingWO.woTypeId,
                cdLevel1: vm.workingWO.cdLevel1,
                constructionId: vm.workingWO.constructionId,
                licenceName: $scope.confirmWO.licenceName,
                attachFile: vm.fileLst,

                // Huypq-end
                invoicePeriod: $scope.confirmWO.invoicePeriod,
                stationRevenueDate: $scope.confirmWO.stationRevenueDate,
                contractId: vm.workingWO.contractId,
                deploymentDateReality: $scope.confirmWO.deploymentDateReality,
                 // tuan anh
                pmtStatus: $scope.confirmWO.pmtStatus,
                handoverUseDateReality: $scope.confirmWO.handoverUseDateReality,
                cntContractId: vm.workingWO.contractId,
            };


            $scope.confirmWO.isWoDoneOk = true;
            woDetailsService.changeStateCdOk(obj).then(
                function (resp) {
                    if (resp.statusCode == -1) { // resp.statusCode == 1
                        kendo.ui.progress($(".tab-content"), false);
                        if (resp.message) {
                            var errorMsg = resp.message;
                            if (errorMsg == 'ERROR') errorMsg = 'Có lỗi xảy ra! ';
                            toastr.error(resp.message + ' ' + (resp.data != null ? resp.data.customField : ""));
                            return;
                        } else {
                            toastr.error("Có lỗi xảy ra!");
                            return;
                        }
                    } else {
                        publishChange();
                        toastr.success("Đánh giá thành công.");
                        kendo.ui.progress($(".tab-content"), false);
                    }
                    var woId = vm.workingWO.woId;
                    getWODetails(woId);
                    vm.woHistory.dataSource.read();
                    vm.woCheckList.dataSource.read();
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function changeStateCdNg() {

            var obj = {
                ftId: vm.workingWO.ftId,
                woId: vm.workingWO.woId,
                loggedInUser: $scope.loggedInUser,
                text: $scope.rejectComment.text,
                woTypeCode: vm.workingWO.woTypeCode,
                description: $scope.rejectComment.text,
                listChecklistId: vm.listChecklistId,
                bgbtsResult: $scope.confirmWO.bgbtsResult,
            };

            woDetailsService.changeStateCdNg(obj).then(
                function (resp) {
                    if (resp && resp.statusCode == 1) {
                        publishChange();
                        toastr.success("Đánh giá thành công.");
                    } else toastr.error("Có lỗi xảy ra!");
                    var woId = vm.workingWO.woId;
                    getWODetails(woId);
                    vm.woHistory.dataSource.read();
                    vm.woCheckList.dataSource.read();
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        vm.changeStateOk = changeStateOk

        function changeStateOk(newMoneyValue) {
            var obj = {
                ftId: vm.workingWO.ftId,
                woId: vm.workingWO.woId,
                loggedInUser: $scope.loggedInUser,
                contractId: vm.workingWO.contractId,
                // Huypq-03092020-start
                moneyValue: newMoneyValue,
                woTypeCode: vm.workingWO.woTypeCode,
                state: vm.workingWO.state,
                // Huypq-end
                finishDate: $scope.confirmWO.finishDate,
                catWorkItemTypeName: vm.label.catWorkName,
                text: $scope.rejectComment.text,
                //taotq-14012022-start
                apConstructionType: vm.workingWO.apConstructionType,
                constructionCode: vm.workingWO.constructionCode,
                constructionId: vm.workingWO.constructionId,
                contractCode: vm.workingWO.contractCode,
                stationCode: vm.workingWO.stationCode,
                stationVtnet: vm.workingWO.stationVtnet,
                trCode: vm.workingWO.trCode,
                trId: vm.workingWO.trId,
                trTypeCode: vm.workingWO.trTypeCode,
                trTypeName: vm.workingWO.trTypeName,
                trExecuteLat: vm.workingWO.trExecuteLat,
                trExecuteLong: vm.workingWO.trExecuteLong,
                woTypeId: vm.workingWO.woTypeId,
                cdLevel1: vm.workingWO.cdLevel1,
                //taotq-14012022-end
                //   datnq start
                deploymentDateReality: $scope.confirmWO.deploymentDateReality,
                cntContractId: vm.workingWO.contractId,
                pmtStatus: $scope.confirmWO.pmtStatus,
                handoverUseDateReality: $scope.confirmWO.handoverUseDateReality
                // datnq end
            };

            woDetailsService.changeStateOk(obj).then(
                function (resp) {
                    if (resp && resp.statusCode == 1) {
                        publishChange();
                        checkWoCompleteToUpdateTr();
                        if (vm.workingWO.state === 'CD_OK') {
                            toastr.success("Ghi nhận sản lượng thành công.");
                        } else {
                            toastr.success("Đánh giá thành công.");
                        }
                        // toastr.success('Đánh giá thành công.');
                    } else {
                    	if(resp.data && resp.data.customField){
                    		toastr.error(resp.data.customField);
                            return;
                    	}
                        if (resp.message) {
                            console.log('rs', resp)
                            toastr.error(resp.message);
                            return;
                        } else {
                            toastr.error('Có lỗi xảy ra!');
                            return;
                        }
                    }
                    var woId = vm.workingWO.woId;
                    getWODetails(woId, true);
                },
                function (error) {
                    toastr.error('Có lỗi xảy ra!');
                }
            )
        }

        $scope.acceptRejectQuantityValue = function () {
            $scope.isAcceptRejectQuantity = true;
            $scope.rejectComment = {};
            $scope.confirmWO.woTypeCode = vm.workingWO.woTypeCode;
            $scope.confirmWO.trTypeCode = vm.workingWO.trTypeCode;
            $scope.confirmWO.state = vm.workingWO.state;
            $scope.confirmWO.opinionType = vm.workingWO.opinionType;
            $scope.confirmWO.catWorkItemTypeId = vm.workingWO.catWorkItemTypeId;
            $scope.confirmWO.isAcceptRejectQuantity = true;
            if(vm.workingWO.state=="TTHT_PAUSE") {
            	if(vm.workingWO.opinionType=="Đề xuất gia hạn") {
            		$scope.confirmWO.finishDate = vm.workingWO.endTime;
            	}
            	if(vm.workingWO.opinionType=="Đề xuất hủy") {
            		$scope.confirmWO.moneyValueMil = vm.workingWO.moneyValueHtct!=null ? vm.workingWO.moneyValueHtct : vm.label.moneyValueMil;
            	}
            } else {
            	if ('HSHC' == vm.workingWO.woTypeCode || 'THICONG' == vm.workingWO.woTypeCode) {
                	if('HSHC' == vm.workingWO.woTypeCode && vm.workingWO.apWorkSrc== 4 && vm.workingWO.trId!=null && vm.workingWO.state!="CD_OK" && vm.workingWO.state!="RECEIVED_PQT" && vm.workingWO.state!="PQT_NG" && vm.workingWO.state!="TTDTHT_NG"){
                		$scope.confirmWO.moneyValueMil = vm.label.moneyValueHtctMil;
                	} else {
                		$scope.confirmWO.moneyValueMil = vm.label.moneyValueMil;
                	}
                }
            }
            $modal.open({
                templateUrl: 'coms/wo_xl/common/okOrNotGoodModal.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function () {
                    debugger
                    if ($scope.confirmWO.isWoDoneOk == undefined) {
                        toastr.error("Chưa chọn đánh giá!");
                        return;
                    } else {
                        if ($scope.confirmWO.isWoDoneOk) {
                        	//Huypq-22102021-start
                        	if(vm.workingWO.state=="CD_PAUSE" && vm.workingWO.apConstructionType!= 7){
                        		changeStateTthtPause($scope.confirmWO);
                        	} else if(vm.workingWO.state=="TTHT_PAUSE" || (vm.workingWO.state=="CD_PAUSE" && vm.workingWO.apConstructionType ==7)) {
                        		changeStateDthtPause($scope.confirmWO);
                        	} else {
                        		if('HSHC' == vm.workingWO.woTypeCode && vm.workingWO.apWorkSrc== 4 && vm.workingWO.trId!=null){
                            		if($scope.confirmWO.moneyValueMil === null || $scope.confirmWO.moneyValueMil === undefined || $scope.confirmWO.moneyValueMil == ''){
                						toastr.warning("Giá trị sản lượng không được để trống!");
                						return;
                            		} else if(($scope.confirmWO.emailTcTct === null || $scope.confirmWO.emailTcTct === undefined || $scope.confirmWO.emailTcTct == '') && "RECEIVED_TTDTHT"==$scope.confirmWO.state){
                            			toastr.warning("Chưa chọn nhân viên tài chính duyệt!");
                						return;
                            		} else {
                            			if(vm.workingWO.state=="CD_OK" || vm.workingWO.state=="PQT_NG") {
                            				changeStateWaitPqt($scope.confirmWO.moneyValueMil);
                            			}
                            			if(vm.workingWO.state=="RECEIVED_PQT" || vm.workingWO.state=="TTDTHT_NG"){
                            				changeStateWaitTtDtht($scope.confirmWO.moneyValueMil);
                            			}
                            			if(vm.workingWO.state=="RECEIVED_TTDTHT"){
                            				changeStateWaitTcTct($scope.confirmWO.moneyValueMil);
                            			}
                            			if(vm.workingWO.state=="WAIT_TC_TCT"){
                            				changeStateOk($scope.confirmWO.moneyValueMil);
                            			}
                            		}
                            	} else {
                            		if (('THICONG' == vm.workingWO.woTypeCode || 'HSHC' == vm.workingWO.woTypeCode) &&
                            				($scope.confirmWO.moneyValueMil === null || $scope.confirmWO.moneyValueMil === undefined || $scope.confirmWO.moneyValueMil == '') && vm.label.catWorkName !=null && !vm.label.catWorkName.includes("Khởi công") && vm.workingWO.cdLevel1!=9006003 && vm.workingWO.cdLevel1!=9006210 && vm.workingWO.cdLevel1!=242656) {
                                        toastr.error("Giá trị sản lượng không được để trống!");
                                    } else {
                                        // changeStateCdOk($scope.confirmWO.moneyValueMil);
                                        changeStateOk($scope.confirmWO.moneyValueMil);
                                    }
                            	}
                        	}
                        	//Huy-end
                        } else {
                        	//Huypq-09112021-start
                        	if(vm.workingWO.state=="CD_PAUSE"){
                        		changeStateTthtPauseReject($scope.confirmWO.moneyValueMil);
                        	} else if(vm.workingWO.state=="TTHT_PAUSE") {
                        		changeStateDthtPauseReject($scope.confirmWO.moneyValueMil);
                        	} else if(vm.workingWO.state=="RECEIVED_PQT") {
                        		changeStateWoHtctPQTReject($scope.confirmWO.moneyValueMil);
                        	} else if(vm.workingWO.state=="RECEIVED_TTDTHT") {
                        		changeStateWoHtctTtDthtReject($scope.confirmWO.moneyValueMil);
                        	} else {
                        		changeStateNotGood();
                        	}
                        	//Huy-end
                        }
                    }
                },
                function () {
                    $scope.isWoDoneOk = true;
                }
            )
        };

        vm.validateRejectComment = validateRejectComment;

        function validateRejectComment() {
            if($scope.confirmWO.woTypeCode == 'THICONG' && !$scope.confirmWO.deploymentDateReality && $scope.confirmWO.isWoDoneOk){
                toastr.error("Chưa nhập ngày khởi công thực tế");
                return false;
            }
            if($scope.confirmWO.woTypeCode == 'HSHC' && $scope.confirmWO.pmtStatus == 2 && !$scope.confirmWO.handoverUseDateReality){
                toastr.error("Chưa nhập ngày bàn giao đưa vào sử dụng thực tế");
                return false;
            }
            if ($scope.confirmWO.woTypeCode != 'BGBTS_DTHT' && $scope.confirmWO.isWoDoneOk === false && (!$scope.rejectComment.text || $scope.rejectComment.text == '')) {
                toastr.error('Chưa nhập lý do từ chối!');
                return false;
            }
            if($scope.confirmWO.isWoDoneOk && ($scope.confirmWO.state=='CD_PAUSE' || $scope.confirmWO.state=='TTHT_PAUSE') && $scope.confirmWO.opinionType=='Đề xuất gia hạn'){
            	if($scope.confirmWO.finishDate==null || $scope.confirmWO.finishDate=='') {
            		toastr.error("Chưa chọn ngày duyệt gia hạn!");
        			return false;
            	} else {
            		var finish = new Date(kendo.parseDate($scope.confirmWO.finishDate,"dd/MM/yyyy"));
            		finish.setHours(0);
            		finish.setMinutes(0);
            		finish.setSeconds(0);
            		finish.setMilliseconds(0);
		    		var current = new Date();
		    		current.setHours(0);
		    		current.setMinutes(0);
		    		current.setSeconds(0);
		    		current.setMilliseconds(0);
            		if(finish < current) {
            			toastr.error("Ngày duyệt gia hạn phải lớn hơn hoặc bằng ngày hiện tại!");
            			return false;
            		}
            	}
    		}
            if(vm.isGpxd && vm.workingWO.catWorkItemTypeId===2083 && $scope.confirmWO.isWoDoneOk){
            	if(!$scope.confirmWO.licenceName){
            		toastr.error("Chưa nhập tên GPXD/Giấy chấp nhận !");
            		return false;
            	}

            	var files = $('#attachFile')[0].files;
    			if(files.length <= 0){
    				toastr.warning("Bạn chưa chọn file tải lên");
    				return false;
    			}
    			submitAttachFile();
            }
            if($scope.confirmWO.isWoDoneOk && ($scope.confirmWO.state=='CD_PAUSE' || $scope.confirmWO.state=='TTHT_PAUSE') && $scope.confirmWO.opinionType=='Đề xuất hủy'){
            	if($scope.confirmWO.moneyValueMil==null || $scope.confirmWO.moneyValueMil=='') {
            		toastr.error("Chưa nhập giá trị sản lượng!");
        			return false;
            	}
            }

            return true;
        }

        vm.checkWoCompleteToUpdateTr = checkWoCompleteToUpdateTr;
        vm.listWo = [];

        function checkWoCompleteToUpdateTr() {
            console.log("checkWoCompleteToUpdateTr");
            woDetailsService.checkWoCompleteToUpdateTr({trId: vm.workingWO.trId}).then(
                function (res) {
                    if (res && res.statusCode == 1 && res.data == true) {
                        toastr.success("Đã tự động hoàn thành TR");
                    }
                }
            )
        }


        $scope.openRejectModal = function () {
            $scope.rejectReason = {};
            $scope.label = {};
            $scope.label.headerText = "Từ chối WO";
            $modal.open({
                templateUrl: 'coms/wo_xl/common/rejectModal.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function () {
                    // ThanhPT - FT từ chối nhận WO BGBTS_VHKT - Start
                    if (vm.workingWO.woTypeCode == "BGBTS_VHKT" && $scope.sysUserId == vm.workingWO.ftId) {
                        vm.rejectWoBgbtsVhkt(vm.workingWO.woId, $scope.rejectReason.text);
                    } else {
                        vm.rejectWO(vm.workingWO.woId, $scope.rejectReason.text);
                    }
                    // ThanhPT - FT từ chối nhận WO BGBTS_VHKT - End
                },
                function () {
                }
            )
        }

        $scope.validateReason = function () {
            if (!$scope.rejectReason.text || $scope.rejectReason.text == '') {
                toastr.error('Xin nhập lý do!');
                return false;
            }

            return true;
        };

        function getTextOpResult(key) {
            if (key == 'ACCEPTED') return 'Đồng ý.';
            if (key == 'REJECTED') return 'Từ chối';
            return '';
        }

        vm.uploadHcqtImage = function (checklistId) {
        		$("#imgUpload").val(null);
                $("#imgUpload").unbind().click();
                $("#imgUpload").change(function () {
                    var selectedFile = $("#imgUpload")[0].files[0];

                    if (!CommonService.isImageFile(selectedFile.name)) {
                        toastr.error('File không đúng định dạng.');
                        return;
                    }

                    var reader = new FileReader();
                    reader.onload = function (event) {
                        var base64Str = reader.result;
                        var checklistDto = {};
                        checklistDto.checklistId = checklistId;
                        checklistDto.userCreated = checklistDto.loggedInUser = $scope.loggedInUser;
                        checklistDto.woId = vm.workingWO.woId;
                        checklistDto.lstImgs = [];
                        checklistDto.lstImgs.push({imgBase64: base64Str.split(',')[1]});

                        woDetailsService.uploadHcqtImage(checklistDto).then(function (res) {
                            if (res && res.statusCode == 1) {
                                toastr.success("Thêm ảnh thành công.");
                                if(vm.woHCQTCheckList){
                                	vm.woHCQTCheckList.dataSource.read();
                                } else {
                                	vm.woTKDTCheckList.dataSource.read();
                                }
                                getWODetails(vm.workingWO.woId, true);
                            } else {
                                toastr.error("Có lỗi xảy ra.");
                            }
                        })
                    };
                    reader.onerror = function (event) {
                        console.error("File could not be read! Code " + event.target.error.code);
                    };
                    reader.readAsDataURL(selectedFile);
                });
        };
//        taotq start 08112021
        vm.uploadFile = function (type) {
        	var idString = "#fileUpload";
            $(idString).unbind().click();
            $(idString).change(function () {
              var selectedFile = $("#fileUpload")[0].files[0];
              var reader = new FileReader();
              var formData = new FormData();
              formData.append('multipartFile', selectedFile);
              var uploadFileService = Constant.BASE_SERVICE_URL + 'fileService/uploadWoFileATTT?folder=' + Constant.UPLOAD_FOLDER_IMAGES;
              woDetailsService.uploadAttachment(uploadFileService, formData)
                  .success(function (resp) {
                      console.log(resp);

                      if (resp[0]) {
                          var filePath = resp[0];
                          var fileName = selectedFile.name;
                          var mappingObj = {
                              woId: vm.workingWO.woId,
                              fileName: fileName,
                              filePath: filePath,
                              userCreated: $scope.loggedInUser,
                              status: 1,
                              type: 1
                          }

                          woDetailsService.createFileCheckList(mappingObj).then(
                              function (resp) {
                                  console.log(resp);
                                  toastr.success("Thêm mới thành công");
                                  vm.woTKDTCheckList.dataSource.read();
                              },
                              function (error) {
                                  console.log(error);
                                  toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                              }
                          )
                      }

                      if (type == 1) {
                          $scope.attachHd = true;
                      }
                      if (type == 2) {
                          $scope.attachBbks = true;
                      }
                  })
                  .error(function (err) {
                      console.log(err);
                      toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                  });
          $("#fileUpload").val(null);
          $("#woTKDTCheckList").data("kendoGrid").refresh();
            });
        };
//        vm.uploadFile = function (dataItem) {
//                var selectedFile = $("#fileUpload")[0].files[0];
//                var reader = new FileReader();
//                var formData = new FormData();
//                formData.append('multipartFile', selectedFile);
//                var uploadFileService = Constant.BASE_SERVICE_URL + 'fileService/uploadWoFileATTT?folder=' + Constant.UPLOAD_FOLDER_IMAGES;
//                woDetailsService.uploadAttachment(uploadFileService, formData)
//                    .success(function (resp) {
//                        console.log(resp);
//
//                        if (resp[0]) {
//                            var filePath = resp[0];
//                            var fileName = selectedFile.name;
//                            var mappingObj = {
//                                woId: vm.workingWO.woId,
//                                fileName: fileName,
//                                filePath: filePath,
//                                userCreated: $scope.loggedInUser,
//                                status: 1,
//                                type: 1
//                            }
//
//                            woDetailsService.createFileCheckList(mappingObj).then(
//                                function (resp) {
//                                    console.log(resp);
//                                    toastr.success("Thêm mới thành công");
//                                    vm.woTKDTCheckList.dataSource.read();
//                                },
//                                function (error) {
//                                    console.log(error);
//                                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
//                                }
//                            )
//                        }
//
//                        if (type == 1) {
//                            $scope.attachHd = true;
//                        }
//                        if (type == 2) {
//                            $scope.attachBbks = true;
//                        }
//                    })
//                    .error(function (err) {
//                        console.log(err);
//                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
//                    });
//            $("#fileUpload").val(null);
//            $("#woTKDTCheckList").data("kendoGrid").refresh();
//        }
        vm.downloadAttachmentFile = function (dataItem) {
            confirm('Bạn có muốn tải về file này?', function () {
            	var filePath = dataItem.attachmentLst[0].filePath;
                var fileName = dataItem.attachmentLst[0].fileName;
                var downloadLink = Constant.BASE_SERVICE_URL + 'fileService/downloadFileATTT?' + filePath;
                saveFile(data, fileName, downloadLink);
            });
        };
//      taotq end 08112021

        $scope.validateHcqtInput = function () {

            if (!vm.checklistDate || vm.checklistDate == '') {
                toastr.error("Ngày chốt không được để trống.");
                return false;
            }

            if ($scope.checklistOrder == 1 && (!vm.dnqtValue || vm.dnqtValue == '')) {
                toastr.error("Giá trị đề nghị không được để trống.");
                return false;
            }

            if ($scope.checklistOrder == 3 && (!vm.vtnetConfirmValue || vm.vtnetConfirmValue == '')) {
                toastr.error("Giá trị chốt không được để trống.");
                return false;
            }

            if ($scope.checklistOrder == 3 && (!vm.aprovedPerson || vm.aprovedPerson == '')) {
                toastr.error("Người chốt không được để trống.");
                return false;
            }

            if ($scope.checklistOrder == 4 && (!vm.workingWO.emailTcTct || vm.workingWO.emailTcTct == '')) {
                toastr.error("Cần chọn người duyệt tài chính tổng công ty.");
                return false;
            }

            return true;
        };

        $scope.validateHcqtProblem = function () {

            if (!vm.problem || vm.problem.code == '') {
                toastr.error("Loại vướng không được để trống.");
                return false;
            }

            if (!vm.problemContent || vm.problemContent == '') {
                toastr.error("Nội dung không được để trống.");
                return false;
            }

            return true;
        }

        var workItemRecord = 0;

        function fillDataTableWorkItem() {
            vm.workItemDataTblOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                save: function () {
                    vm.workItemDataTbl.dataSource.read();
                },
                dataBinding: function () {
                    workItemRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: false,
                    schema: {
                        total: function (response) {
                            // $("#appCount").text("" + response.total);
                            // vm.count = response.total;
                            // return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/workItem/getWorkItemByConstruction",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var searchOptions = {constructionId: vm.workingWO.constructionId}
                            // fileAttSearchOptions.page = options.page
                            // fileAttSearchOptions.pageSize = options.pageSize
                            searchOptions.woType = vm.workingWO.woTypeCode;
                            return JSON.stringify(searchOptions)
                        }
                    },
                    pageSize: 10,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: false,
                // {
                // refresh: false,
                // pageSizes: [10, 15, 20, 25],
                // messages: {
                // display: "{0}-{1} của {2} kết quả",
                // itemsPerPage: "kết quả/trang",
                // empty: "<div style='margin:5px'>Không có kết quả hiển
				// thị</div>"
                // }
                // },
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++workItemRecord;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',

                    },
                    {
                        title: "Hạng mục",
                        field: 'name',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text'
                    },
                    {
                        title: "Giá trị",
                        field: 'quantity',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'number',
                        format: '{0:n3}'
                    },
                ]
            });
        }

        $scope.openAcceptQuantityByDateModal = openAcceptQuantityByDateModal;

        function openAcceptQuantityByDateModal(checklistItemId) {
            fillDataTableQuantityByDate(checklistItemId);
            $modal.open({
                templateUrl: 'coms/wo_xl/woDetails/quantityByDateList.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function () {
                },
                function () {
                }
            )
        }

        var qtyByDateRecord = 0;

        function fillDataTableQuantityByDate(checklistItemId) {
            vm.dataQuantityByDateTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                save: function () {
                    vm.dataQuantityByDateTable.dataSource.read();
                },
                dataBinding: function () {
                    qtyByDateRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: false,
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
                            url: Constant.BASE_SERVICE_URL + "woService/woTaskDaily/doSearchWoTaskDaily",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var searchOptions = {woMappingChecklistId: checklistItemId};
                            searchOptions.page = options.page;
                            searchOptions.pageSize = options.pageSize;
                            return JSON.stringify(searchOptions)
                        }
                    },
                    pageSize: 10,
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
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++qtyByDateRecord;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:center;"},
                        type: 'text',

                    },
                    {
                        title: "Ngày",
                        field: 'createdDate',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text'
                    },
                    {
                        title: "Số lượng",
                        field: 'amount',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'number',
                        format: '{0}'
                    },
                    {
                        title: "Sản lượng",
                        field: 'quantity',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'number',
                        format: '{0}'
                    },
                    {
                        title: "Ngày ghi nhận",
                        field: 'approveDate',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text'
                    },
                    {
                        title: "Ghi nhận sản lượng",
                        field: 'quantity',
                        width: '25%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:center;"},
                        type: 'text',
                        template: function (dataItem) {
                            if (dataItem.confirm == '0') {
                                var template = '<button class="btn btn-primary smaller-btn" ng-click="acceptQuantityByDate(' + dataItem.id + ')">Ghi nhận</button>&nbsp;';
                                template += '<button class="btn info smaller-btn" ng-click="rejectQuantityByDate(' + dataItem.id + ')">Từ chối</button>&nbsp;';
                                return template;
                            }
                            return '';
                        }
                    },
                ]
            });
        }

        $scope.acceptQuantityByDate = function (taskDailyId) {
            var obj = {
                id: taskDailyId,
                approveBy: $scope.loggedInUser,
                confirmUserId: $scope.sysUserId
            };

            confirm('Ghi nhận sản lượng?', function () {
                woDetailsService.acceptQuantityByDate(obj).then(
                    function (res) {
                        if (res && res.statusCode == 1) {
                            toastr.success("Ghi nhận thành công!");
                            vm.woCheckList.dataSource.read();
                            vm.dataQuantityByDateTable.dataSource.read();
                            vm.woHistory.dataSource.read();
                        } else {
                            toastr.error("Có lỗi xảy ra!");
                        }
                    }
                )
            });

        };

        $scope.rejectQuantityByDate = function (taskDailyId) {
            confirm('Từ chối ghi nhận sản lượng này?', function () {
                woDetailsService.rejectQuantityByDate({id: taskDailyId}).then(
                    function (res) {
                        if (res && res.statusCode == 1) {
                            toastr.success("Từ chối thành công!");
                            vm.woCheckList.dataSource.read();
                            vm.dataQuantityByDateTable.dataSource.read();
                        } else {
                            toastr.error("Có lỗi xảy ra!");
                        }
                    }
                )
            });

        };

        vm.extendDueDate = extendDueDate;

        function extendDueDate() {
            vm.oldDueDate = vm.workingWO.finishDate;
            $modal.open({
                templateUrl: 'coms/wo_xl/woDetails/woExtendDueDate.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function () {
                    extendFinishDate(vm.oldDueDate);
                },
                function () {
                    vm.workingWO.finishDate = vm.oldDueDate;
                }
            )
        }

        vm.validateExtendDueDate = function () {
            if (!vm.workingWO.finishDate || vm.workingWO.finishDate == '') {
                toastr.error("Chưa chọn ngày gia hạn!");
                return false
            } else {
                if (!vm.comment.text || vm.comment.text == '') {
                    toastr.error("Chưa nhập lý do!");
                    return false
                }
            }

            return true;
        };

        function extendFinishDate(oldDueDate) {
            var newDueDate = vm.workingWO.finishDate;

            if (!CommonService.isAValidDate(newDueDate, true)) {
                toastr.success("Định dạng ngày tháng không đúng hoặc nhỏ hơn ngày hiện tại!");
                vm.workingWO.finishDate = oldDueDate;
                return;
            }

            var obj = {
                woId: vm.workingWO.woId,
                finishDate: vm.workingWO.finishDate,
                text: vm.comment.text.trim(),
                loggedInUser: $scope.loggedInUser
            };

            woDetailsService.extendFinishDate(obj).then(
                function (res) {
                    if (res && res.statusCode == 1) {
                        toastr.success("Gia hạn thành công!");
                        getWODetails(vm.workingWO.woId, true);
                    } else {
                        toastr.error("Có lỗi xảy ra!");
                        vm.workingWO.finishDate = oldDueDate;
                    }
                }
            )
        }

        var xdddItemRecord = 0;

        function fillXdddChecklistDataTbl() {
            vm.xdddChecklistDataTblOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                save: function () {
                    vm.xdddChecklistDataTbl.dataSource.read();
                },
                dataBinding: function () {
                    xdddItemRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: false,
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
                            url: Constant.BASE_SERVICE_URL + "woService/xddd/getXdddChecklistByWorkItem",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var searchOptions = {
                                constructionId: vm.workingWO.constructionId,
                                catWorkItemTypeId: vm.workingWO.catWorkItemTypeId,
                                woId: vm.workingWO.woId
                            };
                            searchOptions.page = options.page;
                            searchOptions.pageSize = options.pageSize;
                            return JSON.stringify(searchOptions)
                        }
                    },
                    pageSize: 10,
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
                        title: "STT", field: "stt",
                        template: function () {
                            return ++xdddItemRecord;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:center;"},
                        type: 'text',
                    },
                    {
                        title: "Tên đầu việc",
                        field: 'name',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text'
                    },
                    {
                        title: "Giá trị",
                        field: 'value',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'number',
                        format: '{0:n3}'
                    },
                    {
                        title: "Trạng thái",
                        field: 'state',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text',
                        template: function (dataItem) {
                            if (dataItem.confirm != 1) return "Chưa duyệt";
                            else return "Đã duyệt";
                        }
                    },
                ]
            });
        }

        var constructionWoRecord = 0;

        function fillConstructionWoDataTbl() {
            vm.constructionWoDataTblOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                save: function () {
                    vm.xdddChecklistDataTbl.dataSource.read();
                },
                dataBinding: function () {
                    constructionWoRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: false,
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
                            url: Constant.BASE_SERVICE_URL + "woService/hshc/getConstructionWOByHSHC",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var searchOptions = {
                                woHshcId: vm.workingWO.woId
                            };
                            searchOptions.page = options.page;
                            searchOptions.pageSize = options.pageSize;
                            return JSON.stringify(searchOptions)
                        }
                    },
                    pageSize: 10,
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
                        title: "STT", field: "stt",
                        template: function () {
                            return ++xdddItemRecord;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:center;"},
                        type: 'text',
                    },
                    {
                        title: "Tên WO",
                        field: 'woName',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text'
                    },
                    {
                        title: "Mã WO",
                        field: 'woCode',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text'
                    },
                    {
                        title: "Mã Công Trình",
                        field: 'constructionCode',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text'
                    },
                    {
                        title: "Thời gian hoàn thành",
                        field: 'endTime',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text'
                    },
                    {
                        title: "Giá trị sản lượng",
                        field: 'moneyValue',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'number',
                        format: '{0}'
                    },
                ]
            });
        }

        var woGoodsRecord = 0;

        function fillDataGoods() {
            vm.woGooodsOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                save: function () {
                    vm.goodsOfWo.dataSource.read();
                },
                dataBinding: function () {
                    woGoodsRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: false,
                    schema: {
                        total: function (response) {
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/wo/getListGoodsByWoId",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var obj = {woId: vm.workingWO.woId};
                            return JSON.stringify(obj);
                        }
                    },
                    pageSize: 9999,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: false,
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++woGoodsRecord;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',

                    },
                    {
                        title: "Wo ID",
                        field: 'woId',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.woId;
                        },
                    },
                    {
                        title: "Goods ID",
                        field: 'goodsId',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.goodsId;
                        }
                    },
                    {
                        title: "Tên vật tư",
                        field: 'name',
                        width: '40%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.name;
                        }
                    },
                    {
                        title: "Loại",
                        field: 'isSerial',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.isSerial == 1 ? 'Serial' : 'Không serial';
                        }
                    },
                    {
                        title: "Trong kho",
                        field: 'amount',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.amount != null ? dataItem.amount : '';
                        }
                    },
                    {
                        title: "Đơn vị",
                        field: 'goodsUnitName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.goodsUnitName != null ? dataItem.goodsUnitName : '';
                        }
                    },
                    {
                        title: "Cần dùng",
                        field: 'amountNeed',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.amountNeed != null ? dataItem.amountNeed : '';
                        }
                    },
                    {
                        title: "Dùng thực tế",
                        field: 'amountReal',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.amountReal != null ? dataItem.amountReal : '';
                        }
                    },
                    {
                        title: "Serial",
                        field: 'serial',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.serial != null ? dataItem.serial : '';
                        }
                    }
                ]
            });
        }

        // TMBT - Danh sach tram
        var tmbtStationRecord = 0;

        function fillTmbtStationDataTbl() {
            vm.tmbtStationDataTblOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                save: function () {
                    vm.tmbtStations.dataSource.read();
                },
                dataBinding: function () {
                    tmbtStationRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: false,
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
                            url: Constant.BASE_SERVICE_URL + "woService/tmbt/getLstStationsOfWo",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var searchOptions = {
                                woId: vm.workingWO.woId
                            };
                            searchOptions.page = options.page;
                            searchOptions.pageSize = options.pageSize;
                            return JSON.stringify(searchOptions)
                        }
                    },
                    pageSize: 10,
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
                        title: "STT", field: "stt",
                        template: function () {
                            return ++xdddItemRecord;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:center;"},
                        type: 'text',
                    },
                    {
                        title: "Mã trạm",
                        field: 'code',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text'
                    },
                    {
                        title: "Địa chỉ",
                        field: 'address',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text'
                    },
                    {
                        title: "Trạng thái",
                        field: 'status',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text',
                        template: function (dataItem) {
                            if (dataItem.status == 1) {
                                return "Thành công";
                            } else if (dataItem.status == 0) {
                                return "Không thành công"
                            } else if (dataItem.status == 2) {
                                return "Chưa thuê"
                            }
                        }
                    },
                    {
                        title: "Nguyên nhân / Thông tin",
                        field: 'reason',
                        width: '40%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text'
                    },
                ]
            });
        }

        vm.approvedOkWoTkdt = approvedOkWoTkdt;

        function approvedOkWoTkdt() {
        	 woDetailsService.getDataConstructionContractByStationCode(vm.workingWO.stationCode).then(
                     function (res) {
                         if (res) {
                        	 vm.oldDueDate = vm.workingWO.finishDate;
                        	 vm.searchForm.contractCode = res.contractCode;
                             vm.searchForm.contractId = res.contractId;
                             vm.searchForm.constructionCode = res.constructionCode;
                             vm.searchForm.constructionId = res.constructionId;

                             vm.lstTuNguon=[
                            	 {
	                            	 code: 'Lắp đặt tủ nguồn',
	                            	 name: 'Lắp đặt tủ nguồn'
                            	 }
                             ];

                             vm.lstDienAc = [
                            	 {
	                            	 code: 'Thi công điện AC',
	                            	 name: 'Thi công điện AC'
                            	 }
                             ];
                             vm.objectTuNguon.name='Lắp đặt tủ nguồn';
                             vm.objectDienAc.name='Thi công điện AC';
                             //Trên mái
                             if(res.placement=="0"){
                            	 vm.lstXayDungNha = [
                            		 {
	                                	 code: 'XD Nhà xây trên mái',
	                                	 name: 'XD Nhà xây trên mái'
                            		 }
                                 ];

                            	 vm.lstXayDungMong = [
                            		 {
	                                	 code: 'XD móng nhà LG',
	                                	 name: 'XD móng nhà LG'
                            		 },
                            		 {
	                                	 code: 'XD móng cột trên mái',
	                                	 name: 'XD móng cột trên mái'
                            		 }
                                 ];

                            	 vm.lstThiCong=[
                            		 {
	                                	 code: 'Thi công tiếp địa khoan cọc trên mái',
	                                	 name: 'Thi công tiếp địa khoan cọc trên mái'
                            		 },
                            		 {
	                                	 code: 'Thi công tiếp địa lập là',
	                                	 name: 'Thi công tiếp địa lập là'
                            		 },
                            		 {
	                                	 code: 'Thi công tiếp địa Gem',
	                                	 name: 'Thi công tiếp địa Gem'
                            		 },
                            	 ];

                                 vm.lstLapDung=[
                                	 {
	                                	 code: 'Lắp dựng cột trên mái',
	                                	 name: 'Lắp dựng cột trên mái'
                            		 },
                            		 {
	                                	 code: 'Lắp dựng nhà LG',
	                                	 name: 'Lắp dựng nhà LG'
                            		 }
                                 ];
                             }

                             //Dưới đất
                             if(res.placement=="1"){
                            	 vm.lstXayDungNha = [
                            		 {
	                                	 code: 'XD Nhà xây mới dưới đất',
	                                	 name: 'XD Nhà xây mới dưới đất'
                            		 }
                                 ];

                            	 vm.lstXayDungMong = [
                            		 {
	                                	 code: 'XD móng nhà LG',
	                                	 name: 'XD móng nhà LG'
                            		 },
                            		 {
	                                	 code: 'XD móng cột dưới đất',
	                                	 name: 'XD móng cột dưới đất'
                            		 }
                                 ];

                            	 vm.lstThiCong=[
                            		 {
	                                	 code: 'Thi công tiếp địa khoan cọc dưới đất',
	                                	 name: 'Thi công tiếp địa khoan cọc dưới đất'
                            		 },
                            		 {
	                                	 code: 'Thi công tiếp địa lập là',
	                                	 name: 'Thi công tiếp địa lập là'
                            		 },
                            		 {
	                                	 code: 'Thi công tiếp địa Gem',
	                                	 name: 'Thi công tiếp địa Gem'
                            		 },
                            	 ];

                            	 vm.lstLapDung=[
                                	 {
	                                	 code: 'Lắp dựng cột dưới đất',
	                                	 name: 'Lắp dựng cột dưới đất'
                            		 },
                            		 {
	                                	 code: 'Lắp dựng nhà LG',
	                                	 name: 'Lắp dựng nhà LG'
                            		 }
                                 ];
                             }
//                             dataGridWorkItemMap([]);
                             $modal.open({
                                 templateUrl: 'coms/wo_xl/woDetails/woApprovedTkdt.html',
                                 controller: null,
                                 windowClass: 'app-modal-window',
                                 scope: $scope
                             })
                                 .result.then(
                                 function () {
                                     approvedTkdt(vm.searchForm.contractCode, vm.searchForm.constructionCode);
                                 }
                             )
                         } else {
                             toastr.error("Chưa gán trạm vào hợp đồng cho thuê đầu vào!");
                             vm.workingWO.finishDate = oldDueDate;
                         }
                     }
                 );
        }

//        $scope.autoCompleteFilterContractOptions = {
//            dataTextField: "contractCode", placeholder: "Thông tin hợp đồng",
//            open: function (e) {
//            },
//            select: function (e) {
//                data = this.dataItem(e.item.index());
//                vm.searchForm.contractCode = data.contractCode;
//                vm.searchForm.contractId = data.contractId;
//                $scope.$apply();
//            },
//            pageSize: 10,
//            dataSource: {
//                serverFiltering: true,
//                transport: {
//                    read: function (options) {
//                        var keySearch = vm.searchForm.contractCode;
//
//                        if (keySearch == '') vm.searchForm.contractId = null;
//
//                        var objSearch = {keySearch: keySearch, page: 1, pageSize: 20};
//
//                        return Restangular.all("trService/tr/doSearchContracts").post(objSearch).then(
//                            function (response) {
//                                options.success(response.data);
//                            }
//                        ).catch(
//                            function (err) {
//                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
//                            }
//                        );
//
//                    }
//                }
//            },
//            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
//                '<p class="col-md-12 text-header-auto">Các hợp đồng</p></div>',
//            template: '<div class="row" >' +
//                '<div class="col-xs-9" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.contractCode #</div></div>',
//            change: function (e) {
//            },
//            close: function (e) {
//                // handle the event0
//            }
//        };
//
//        $scope.autoCompleteConstructionOptions = {
//            dataTextField: "code", placeholder: "Chọn công trình",
//            open: function (e) {
//            },
//            select: function (e) {
//                data = this.dataItem(e.item.index());
//                vm.searchForm.constructionCode = data.constructionCode;
//                vm.searchForm.catConstructionTypeId = data.catConstructionTypeId,
//                vm.searchForm.constructionId = data.constructionId;
//                $scope.$apply();
//            },
//            pageSize: 10,
//            dataSource: {
//                serverFiltering: true,
//                transport: {
//                    read: function (options) {
//                        var keySearch = vm.searchForm.constructionCode;
//                        if (keySearch == '') {
//                            vm.searchForm.constructionCode = null;
//                            vm.searchForm.catConstructionTypeId = null;
//                            vm.searchForm.constructionId = null;
//                            return options.success([]);
//                        }
//                        var obj = {keySearch: keySearch}
//
//                        return Restangular.all("trService/tr/doSearchConstruction").post(obj).then(
//                            function (response) {
//                                options.success(response.data);
//                            }
//                        ).catch(
//                            function (err) {
//                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
//                            }
//                        );
//                    }
//                }
//            },
//            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
//                '<p class="col-md-12 text-header-auto">Chọn Mã công trình</p></div>',
//            template: '<div class="row" >' +
//                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.constructionCode #</div></div>',
//            change: function (e) {
//            },
//            close: function (e) {
//                // handle the event0
//            }
//        };

        vm.validateApprovedTkdt = function () {
            if (!vm.searchForm.contractCode || vm.searchForm.contractCode == '') {
                toastr.error("Chưa chọn hợp đồng !");
                return false
            } else {
                if (!vm.searchForm.constructionCode || vm.searchForm.constructionCode == '') {
                    toastr.error("Chưa chọn công trình !");
                    return false
                }
            }
//            var dataGrid = $('#workItemGridContract').data('kendoGrid').dataSource.data();
//        	if(dataGrid.length==0) {
//        		toastr.warning("Chưa chọn gán hạng mục vào công trình !");
//                return false;
//        	} else {
//        		for(var i=0; i< dataGrid.length; i++){
//            		if(dataGrid[i].workItemValue==null || dataGrid[i].workItemValue==""){
//            			toastr.warning("Chưa nhập giá trị hạng mục !");
//                        return false;
//            		}
//            	}
//        	}
            if(vm.objectTuNguon.name==null || vm.objectTuNguon.name=='') {
            	toastr.warning("Chưa chọn loại hạng mục của Tủ nguồn !");
            	return false;
            }
            if(vm.objectTuNguon.workItemValue==null) {
            	toastr.warning("Chưa nhập giá trị hạng mục của Tủ nguồn !");
            	return false;
            }

            if(vm.objectXayDungNha.name==null || vm.objectXayDungNha.name=='') {
            	toastr.warning("Chưa chọn loại hạng mục của Tủ nguồn !");
            	return false;
            }
            if(vm.objectXayDungNha.workItemValue==null) {
            	toastr.warning("Chưa nhập giá trị hạng mục của Tủ nguồn !");
            	return false;
            }

            if(vm.objectXayDungMong.name==null || vm.objectXayDungMong.name=='') {
            	toastr.warning("Chưa chọn loại hạng mục của Tủ nguồn !");
            	return false;
            }
            if(vm.objectXayDungMong.workItemValue==null) {
            	toastr.warning("Chưa nhập giá trị hạng mục của Tủ nguồn !");
            	return false;
            }

            if(vm.objectThiCong.name==null || vm.objectThiCong.name=='') {
            	toastr.warning("Chưa chọn loại hạng mục của Tủ nguồn !");
            	return false;
            }
            if(vm.objectThiCong.workItemValue==null) {
            	toastr.warning("Chưa nhập giá trị hạng mục của Tủ nguồn !");
            	return false;
            }

            if(vm.objectLapDung.name==null || vm.objectLapDung.name=='') {
            	toastr.warning("Chưa chọn loại hạng mục của Tủ nguồn !");
            	return false;
            }
            if(vm.objectLapDung.workItemValue==null) {
            	toastr.warning("Chưa nhập giá trị hạng mục của Tủ nguồn !");
            	return false;
            }

            if(vm.objectDienAc.name==null || vm.objectDienAc.name=='') {
            	toastr.warning("Chưa chọn loại hạng mục của Tủ nguồn !");
            	return false;
            }
            if(vm.objectDienAc.workItemValue==null) {
            	toastr.warning("Chưa nhập giá trị hạng mục của Tủ nguồn !");
            	return false;
            }
            return true;
        };

        function approvedTkdt(contractCode, constructionCode) {
        	var dataGrid = [];
            dataGrid.push(vm.objectTuNguon);
            dataGrid.push(vm.objectXayDungNha);
            dataGrid.push(vm.objectXayDungMong);
            dataGrid.push(vm.objectThiCong);
            dataGrid.push(vm.objectLapDung);
            dataGrid.push(vm.objectDienAc);
            var obj = {
                woId: vm.workingWO.woId,
                contractCode: contractCode,
                constructionCode: constructionCode,
                loggedInUser: $scope.loggedInUser,
                //Huypq-25102021-start
                woCode: vm.workingWO.woCode,
                listWorkItem: dataGrid
                //Huy-end
            };

            woDetailsService.approvedOkWoTkdt(obj).then(
                function (res) {
                    if (res && res.statusCode == 1) {
                        toastr.success("Thành công !");
                        getWODetails(vm.workingWO.woId, true);
                        reloadTables();
                        publishChange();
                    } else {
                        toastr.error("Có lỗi xảy ra!");
                        vm.workingWO.finishDate = oldDueDate;
                    }
                }
            )
        }

      // Huypq-28062021-start
        var record = 0;
        function fillDataTthq(data){
        	var dataSource = new kendo.data.DataSource({
    			data: data,
    			pageSize: 10,
    			autoSync: false,
    			schema: {
    				model: {
    					id: "stationCodeVcc",
    					fields: {
    						stt: {type: "text",editable: false },
    						areaCode: {type: "text",editable: false },
    						provinceCode: {type: "text",editable: false },
    						stationCodeVtn: {type: "text",editable: false },
    						stationCodeVcc: {type: "text",editable: false },
    						address: {type: "text",editable: false },
    						stationType: {type: "text",editable: false  },
    						maiDat: {type: "text",editable: false },
    						doCaoCot: {type: "number",editable: false },
    						loaiCot: {type: "text",editable: false },
    						mongCo: {type: "text",editable: false },
    						loaiNha: {type: "text",editable: false },
    						tiepDia: {type: "text",editable: false  },
    						dienCnkt: {type: "text",editable: false },
    						soCotDien: {type: "text",editable: false },
    						vanChuyenBo: {type: "text",editable: false },
    						thueAcquy: {type: "text",editable: false },
    						giaThueMbThucTe: {type: "number",editable: false },
    						giaThueMbTheoDinhMuc: {type: "text",editable: false  },
    						capexCot: {type: "text",editable: false },
    						capexTiepDia: {type: "text",editable: false },
    						capexAc: {type: "text",editable: false },
    						capexPhongMay: {type: "text",editable: false },
    						tongCapexHt: {type: "text",editable: false },
    						vccChaoGiaHt: {type: "text",editable: false  },
    						vccChaoGiaAcquy: {type: "text",editable: false },
    						tongCong: {type: "text",editable: false },
    						description: {type: "text",editable: false },
    						ngayGui: {type: "text",editable: false },
    						ngayHoanThanh: {type: "text",editable: false },
    						nguoiLap: {type: "text",editable: false  },
    						capexHtVcc: {type: "text",editable: false },
    						npv: {type: "text",editable: false },
    						irr: {type: "text",editable: false },
    						thoiGianHv: {type: "text",editable: false },
    						lnstDt: {type: "text",editable: false },
    						conclude: {type: "text",editable: false  },
    					}
    				}
    			}
    		});
        	vm.tthqDataTblOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
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
                        title: "Khu vực",
                        field: 'areaCode',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:left;height:44px;white-space: normal;"
                        }
                    },
                    {
                        title: "Tỉnh",
                        field: 'provinceCode',
                        width: '180px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Mã trạm (VTN)",
                        field: 'stationCodeVtn',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        }
                    },
    				{
                        title: "Mã trạm (VCC)",
                        field: 'stationCodeVcc',
                        width: '100px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        }
                    },
                    {
                        title: "Địa chỉ",
                        field: 'address',
                        width: '100px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        },
                    },
                    {
                        title: "Loại trạm",
                        field: 'stationType',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Mái/đất",
                        field: 'maiDat',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Độ cao cột",
                        field: 'doCaoCot',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Loại cột",
                        field: 'loaiCot',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Móng co",
                        field: 'mongCo',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Loại nhà",
                        field: 'loaiNha',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Tiếp địa",
                        field: 'tiepDia',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Điện CNKT",
                        field: 'dienCnkt',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Số cột điện",
                        field: 'soCotDien',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Vận chuyển bộ",
                        field: 'vanChuyenBo',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Thuê nguồn",
                        field: 'thueAcquy',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Giá thuê MB thực tế",
                        field: 'giaThueMbThucTe',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
//                    {
//                        title: "Giá thuê MB theo định mức",
//                        field: 'giaThueMbTheoDinhMuc',
//                        width: '150px',
//                        headerAttributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                        attributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                    },
//                    {
//                        title: "Capex cột",
//                        field: 'capexCot',
//                        width: '150px',
//                        headerAttributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                        attributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                    },
//                    {
//                        title: "Capex tiếp địa",
//                        field: 'capexTiepDia',
//                        width: '150px',
//                        headerAttributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                        attributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                    },
//                    {
//                        title: "Capex AC",
//                        field: 'capexAc',
//                        width: '150px',
//                        headerAttributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                        attributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                    },
//                    {
//                        title: "Capex phòng máy",
//                        field: 'capexPhongMay',
//                        width: '150px',
//                        headerAttributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                        attributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                    },
                    {
                        title: "Capex (không nguồn) trước VAT",
                        field: 'capexTruocVatString',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Doanh thu hạ tầng",
                        field: 'vccChaoGiaHt',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Doanh thu nguồn",
                        field: 'vccChaoGiaAcquy',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Tổng doanh thu/tháng",
                        field: 'tongCong',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
//                    {
//                        title: "Ghi chú",
//                        field: 'description',
//                        width: '150px',
//                        headerAttributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                        attributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                    },
//                    {
//                        title: "Ngày gửi",
//                        field: 'ngayGui',
//                        width: '150px',
//                        headerAttributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                        attributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                    },
//                    {
//                        title: "Hoàn thành",
//                        field: 'ngayHoanThanh',
//                        width: '150px',
//                        headerAttributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                        attributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                    },
//                    {
//                        title: "Người lập",
//                        field: 'nguoiLap',
//                        width: '150px',
//                        headerAttributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                        attributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                    },
//                    {
//                        title: "Capex HT VCC",
//                        field: 'capexHtVcc',
//                        width: '150px',
//                        headerAttributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                        attributes: {
//                            style: "text-align:center;white-space: normal;"
//                        },
//                    },
                    {
                        title: "NPV",
                        field: 'npvString',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "IRR",
                        field: 'irrString',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Thời gian hoàn vốn",
                        field: 'thoiGianHvString',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "LNST/DT",
                        field: 'lnstDtString',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Kết luận",
                        field: 'conclude',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                ]
            });
        }

        vm.importFileTthq = importFileTthq;
        function importFileTthq() {
            vm.fileImportData = false;
            vm.searchForm.type = 1;
            var teamplateUrl = "coms/wo_xl/woManagement/importFileTthq.html";
            var title = "Import file tính toán hiệu quả";
            var windowId = "IMPORT_FILE_TTHQ";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '275', null);
        }

        vm.exportFileImportTthq = function () {
        	kendo.ui.progress($("#yearPlan"), true);
        	var obj = {
        		woId: vm.workingWO.woId
        	};
        	return Restangular.all("woService/exportFileImportTthq").post(obj).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                kendo.ui.progress($("#yearPlan"), false);
            }).catch(function (e) {
            	kendo.ui.progress($("#yearPlan"), false);
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                return;
            });
        }

        vm.cancelImport = cancelImport;
        function cancelImport() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

        vm.onSelectFile = onSelectFile;
        function onSelectFile() {
            if ($("#fileImporttthq")[0].files[0].name.split('.').pop() != 'xls' && $("#fileImporttthq")[0].files[0].name.split('.').pop() != 'xlsx') {
                toastr.warning("Sai định dạng file");
                setTimeout(function () {
                    $(".k-upload-files.k-reset").find("li").remove();
                    $(".k-upload-files").remove();
                    $(".k-upload-status").remove();
                    $(".k-upload.k-header").addClass("k-upload-empty");
                    $(".k-upload-button").removeClass("k-state-focused");
                }, 10);
                return;
            } else {
                if (104857600 < $("#fileImporttthq")[0].files[0].size) {
                    toastr.warning("Dung lượng file vượt quá 100MB! ");
                    return;
                }
                $("#fileNameTthq")[0].innerHTML = $("#fileImporttthq")[0].files[0].name
            }
        }


        vm.submitImportTthq = submitImportTthq;
        function submitImportTthq(data) {
            var ajax_sendding = false;
            if (ajax_sendding == true) {
                alert('Dang Load Ajax');
                return false;
            }
            if ($("#fileImporttthq")[0].files[0] == null) {
                toastr.warning("Bạn chưa chọn file để import");
                return;
            }
            if ($("#fileImporttthq")[0].files[0].name.split('.').pop() != 'xls' && $("#fileImporttthq")[0].files[0].name.split('.').pop() != 'xlsx') {
                toastr.warning("Sai định dạng file");
                return;
            }

            var formDataTTHQ = new FormData();
            var selectedFileUploadTTHQ = $("#fileImporttthq")[0].files[0];
            formDataTTHQ.append('multipartFile', selectedFileUploadTTHQ);
            var uploadFileService = Constant.BASE_SERVICE_URL + 'fileService/uploadWoFileAndSaveWoMappingAttach?folder=' + Constant.UPLOAD_FOLDER_IMAGES
            		+ '&woId=' + vm.workingWO.woId
            		+ '&userCreate=' + $scope.loggedInUser
            		+ '&nameFile=' + selectedFileUploadTTHQ.name;
            woDetailsService.uploadAttachment(uploadFileService, formDataTTHQ)
                .success(function (resp) {
                    console.log(resp);
                    toastr.success("Thêm mới file thành công");
                    vm.fileAttachDataTbl.dataSource.read();
                }).error(function (err) {
                    console.log(err);
                    toastr.error("Có lỗi xảy ra khi upload file! Vui lòng thử lại sau.");
                });
            cancelImport();
//            var formData = new FormData();
//            var selectedFileUpload = $("#fileImporttthq")[0].files[0];

//            formData.append('multipartFile', $('#fileImporttthq')[0].files[0]);
//            $('#loadding').show();
//            return $.ajax({
//                url: Constant.BASE_SERVICE_URL + "woService/woDetails/importFileTthq?" + "woId=" + vm.workingWO.woId + '&folderParam=' + Constant.UPLOAD_FOLDER_IMAGES
//            		+ '&userCreate=' + $scope.loggedInUser
//            		+ '&nameFile=' + selectedFileUpload.name
//            		+ '&woCode=' + vm.workingWO.woCode,
//                type: "POST",
//                data: formData,
//                enctype: 'multipart/form-data',
//                processData: false,
//                contentType: false,
//                cache: false,
//                success: function (data) {
//                    if (data && data.statusCode == 1) {
//                        toastr.success("Import thành công! ");
//                        $("#tthqDataTblId").data("kendoGrid").dataSource.data(data.data);
//                        $("#tthqDataTblId").data("kendoGrid").refresh();
//
//
//                        var formDataTTHQ = new FormData();
//                        var selectedFileUploadTTHQ = $("#fileImporttthq")[0].files[0];
//                        formDataTTHQ.append('multipartFile', selectedFileUploadTTHQ);
//                        var uploadFileService = Constant.BASE_SERVICE_URL + 'fileService/uploadWoFileAndSaveWoMappingAttach?folder=' + Constant.UPLOAD_FOLDER_IMAGES
//                        		+ '&woId=' + vm.workingWO.woId
//                        		+ '&userCreate=' + $scope.loggedInUser
//                        		+ '&nameFile=' + selectedFileUploadTTHQ.name;
//                        woDetailsService.uploadAttachment(uploadFileService, formDataTTHQ)
//                            .success(function (resp) {
//                                console.log(resp);
//                                toastr.success("Thêm mới file thành công");
//                                vm.fileAttachDataTbl.dataSource.read();
//                            }).error(function (err) {
//                                console.log(err);
//                                toastr.error("Có lỗi xảy ra khi upload file! Vui lòng thử lại sau.");
//                            });
//                        cancelImport();
//                    } else {
//                        toastr.error("Import thất bại! " + data.message ? data.message : '');
//                        cancelImport();
//                    }
//                    cancelImport();
//                    $scope.$apply();
//                }
//            });
        }

        // Huy-end

        // Huypq-05072021-start
        vm.approvedOkWoTthq = function(){
//        	var grid = $("#tthqDataTblId").data("kendoGrid").dataSource.data();
//        	if(grid.length==0) {
//        		toastr.warning("Chưa import dữ liệu Tính toán hiệu quả !");
//        		return;
//        	}
        	confirm("Xác nhận đóng WO ?", function(){
        		var obj = {
                        woId: vm.workingWO.woId,
                        loggedInUser: $scope.loggedInUser,
                        woTypeCode: vm.workingWO.woTypeCode, //Huypq-29112021-add
                        // effectiveDTO: grid[0],
                    };

                    woDetailsService.approvedWoOkTthq(obj).then(
                        function (res) {
                            if (res && res.statusCode == 1) {
                                toastr.success("Đóng WO thành công !");
                                getWODetails(vm.workingWO.woId, true);
                                reloadTables();
                                publishChange();
                            } else {
                                toastr.error("Có lỗi xảy ra!");
// vm.workingWO.finishDate = oldDueDate;
                            }
                        }
                    )
        	});
        }
        //Huy-end

        //Huypq-22102021-start
        vm.changeStateWaitPqt = changeStateWaitPqt
        function changeStateWaitPqt(newMoneyValue) {
            var obj = {
                ftId: vm.workingWO.ftId,
                woId: vm.workingWO.woId,
                loggedInUser: $scope.loggedInUser,
                contractId: vm.workingWO.contractId,
                moneyValue: newMoneyValue,
                woTypeCode: vm.workingWO.woTypeCode,
                state: vm.workingWO.state,
                finishDate: $scope.confirmWO.finishDate,
                catWorkItemTypeName: vm.label.catWorkName
            };

            woDetailsService.changeStateWaitPqt(obj).then(
                function (resp) {
                    if (resp && resp.statusCode == 1) {
                        publishChange();
                        checkWoCompleteToUpdateTr();
                        toastr.success('Đánh giá thành công.');
                    } else {
                    	if(resp.data && resp.data.customField){
                    		toastr.error(resp.data.customField);
                            return;
                    	}
                        if (resp.message) {
                            toastr.error(resp.message);
                            return;
                        } else {
                            toastr.error('Có lỗi xảy ra!');
                            return;
                        }
                    }
                    var woId = vm.workingWO.woId;
                    getWODetails(woId, true);
                },
                function (error) {
                    toastr.error('Có lỗi xảy ra!');
                }
            )
        }

        vm.changeStateWaitTtDtht = changeStateWaitTtDtht
        function changeStateWaitTtDtht(newMoneyValue) {
            var obj = {
                ftId: vm.workingWO.ftId,
                woId: vm.workingWO.woId,
                loggedInUser: $scope.loggedInUser,
                contractId: vm.workingWO.contractId,
                moneyValueHtct: newMoneyValue,
                woTypeCode: vm.workingWO.woTypeCode,
                state: vm.workingWO.state,
                finishDate: $scope.confirmWO.finishDate,
                catWorkItemTypeName: vm.label.catWorkName
            };

            woDetailsService.changeStateWaitTtDtht(obj).then(
                function (resp) {
                    if (resp && resp.statusCode == 1) {
                        publishChange();
                        checkWoCompleteToUpdateTr();
                        toastr.success('Đánh giá thành công.');
                    } else {
                    	if(resp.data && resp.data.customField){
                    		toastr.error(resp.data.customField);
                            return;
                    	}
                        if (resp.message) {
                            toastr.error(resp.message);
                            return;
                        } else {
                            toastr.error('Có lỗi xảy ra!');
                            return;
                        }
                    }
                    var woId = vm.workingWO.woId;
                    getWODetails(woId, true);
                },
                function (error) {
                    toastr.error('Có lỗi xảy ra!');
                }
            )
        }

        vm.changeStateWaitTcTct = changeStateWaitTcTct
        function changeStateWaitTcTct(newMoneyValue) {
            var obj = {
                ftId: vm.workingWO.ftId,
                woId: vm.workingWO.woId,
                loggedInUser: $scope.loggedInUser,
                contractId: vm.workingWO.contractId,
                moneyValueHtct: newMoneyValue,
                woTypeCode: vm.workingWO.woTypeCode,
                state: vm.workingWO.state,
                finishDate: $scope.confirmWO.finishDate,
                catWorkItemTypeName: vm.label.catWorkName,
                emailTcTct: $scope.confirmWO.emailTcTct
            };

            woDetailsService.changeStateWaitTcTct(obj).then(
                function (resp) {
                    if (resp && resp.statusCode == 1) {
                        publishChange();
                        checkWoCompleteToUpdateTr();
                        toastr.success('Đánh giá thành công.');
                    } else {
                    	if(resp.data && resp.data.customField){
                    		toastr.error(resp.data.customField);
                            return;
                    	}
                        if (resp.message) {
                            toastr.error(resp.message);
                            return;
                        } else {
                            toastr.error('Có lỗi xảy ra!');
                            return;
                        }
                    }
                    var woId = vm.workingWO.woId;
                    getWODetails(woId, true);
                },
                function (error) {
                    toastr.error('Có lỗi xảy ra!');
                }
            )
        }

        vm.workItemLst = [];
        function getWorkItemLstMap(){
    		if(vm.isCreateNew){
    			vm.workItemLst = [];
    		}
      		return vm.workItemLst;
      	}

        function dataGridWorkItemMap(data) {
        	vm.workItemOptionsMap = new kendoConfig.getGridOptions({
				autoBind: true,
				resizable: true,
				editable: false,
				scrollable: true,
				dataSource: {
					data: getWorkItemLstMap(),
					schema: {
                        model: {
                            fields: {
                            	stt: { editable: false, nullable: true },
                            	name: { editable: false, nullable: true },
                            	workItemValue: { editable: true, nullable: true, type: 'number'},
                            	action: { editable: false, nullable: true }
                            }
                        }
                    },
				},
				noRecords: true,
				scrollable:true,
				messages: {
					noRecords : "Không có kết quả hiển thị"
				},
				pageSize:10,
				pageable: {
					refresh: false,
					pageSize:10,
					 pageSizes: [10, 15, 20, 25],
					messages: {
		                display: "{0}-{1} của {2} kết quả",
		                itemsPerPage: "kết quả/trang",
		                empty: "Không có kết quả hiển thị"
		            }
				},
				columns:[{
					title: "TT",
					field: "stt",
					width:'40px',
					template:dataItem => $("#workItemGridContract").data('kendoGrid').dataSource.indexOf(dataItem) + 1,
					headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:center;"
						},
					},
					{
						title: "ID Hạng mục",
						field: "catWorkItemTypeId",
						headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:left;"
						},
						width:'130px',
						hidden: true,
					},
					{
						title: "Mã hạng mục",
						field: "code",
						headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:left;"
						},
						width:'150px',
						hidden: true,
			        },
			        {
						title: "Hạng mục",
						field: "name",
						headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:left;"
						},
						width:'150px',
			        },
			        {
						title: "Giá trị (VNĐ)",
						field: "workItemValue",
						headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:left;"
						},
						width:'60px',
			        },
			        {
						title: "Thao tác",
				        template: dataItem =>
						'<div class="text-center #=catWorkItemTypeId#"">'+
						'<a type="button" class="#=catWorkItemTypeId# icon_table" uib-tooltip="Xóa" translate ng-click="vm.removeWi(dataItem)">'+
							'<i class="fa fa-trash" aria-hidden="true"></i>'+
						'</a>'+
						'</div>',
				        width: '60px',
				        field:"action",
			        }
				]
			});
        }

        vm.removeWi = function(dataItem) {
        	for(var i=0; i<vm.workItemLst.length; i++) {
        		if(vm.workItemLst[i].catWorkItemTypeId == dataItem.catWorkItemTypeId){
        			vm.workItemLst.splice(i, 1);
        			break;
        		}
        	}
        	$('#workItemGridContract').data('kendoGrid').refresh();
        }

    		function checkDupsMapWI(purchase){
    			var isExisted = false;
    			var mapGrid = $('#workItemGridContract').data('kendoGrid');
    			var dataGridLength = mapGrid.dataSource.data().length;

    			if(dataGridLength!=0){
    				var dataItem = mapGrid.dataSource._data;
    				for(var i=0;i<dataGridLength;i++){
    					if(purchase.code == dataItem[i].code){
    						return isExisted = true;
    					}
    				}
    			}
    	        return isExisted;
    		}

    		vm.isSelect = false;
    		vm.workItemComplete={
    				dataTextField: "name", placeholder:"Nhập tên hạng mục",
    				open: function(e) {
    						vm.isSelect = false;
    					},
    		            select: function(e) {
//    		            	vm.workItemLst = [];
    		            	var grid = $("#workItemGridContract").data("kendoGrid").dataSource.data();
    		            	vm.workItemLst = angular.copy(grid);
    		            	vm.isSelect = true;
    		            	var dataItem = this.dataItem(e.item.index());
    		            	if(!checkDupsMapWI(dataItem)){
    		            		vm.workItemLst.push(dataItem);
    		            	}

    		            	$("#workItemGridContract").data("kendoGrid").dataSource.data(vm.workItemLst);
    		            	$("#workItemGridContract").data("kendoGrid").refresh();
    		            },
    	            pageSize: 10,
    	            dataSource: {
    	                serverFiltering: true,
    	                transport: {
    	                    read: function(options) {
    	                        	return Restangular.all("woService/workItem/getDataWorkItemByConsTypeId").post({
    	                        		pageSize:10,
    	                        		page:1,
    	                        		name:vm.searchForm.workItemMap,
    	                        		catConstructionTypeId: vm.searchForm.catConstructionTypeId}).then(function(response){
    	                            options.success(response);
    	                            var check = response == [] || $("#code").val() == "";
    	                            if(response == [] || $("#code").val() == ""){
    	                            	$("#workItemMap").val(null);
    	                            }
    	                        }).catch(function (err) {
    	                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
    	                        });
    	                    }
    	                }
    	            },
    	            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
    	            '<p class="col-md-6 text-header-auto">Tên hạng mục</p>' +
    	            	'</div>',
    	            template:'<div class="row" ><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
    	            change: function(e) {
    	            	if(processSearch("code",vm.selectedPrpject)){
    	            		$("#workItemMap").val(null);
    						  vm.selectedPrpject=false;
    	            	}
    	            },
    	            close: function(e) {
    	              }
    			};
        //Huy-end

    		//Huypq-30102021-start
            var recordContract = 0;
            function fillDataTthqContract(data){
            	var dataSource = new kendo.data.DataSource({
        			data: data,
        			pageSize: 10,
        			autoSync: false,
        			schema: {
        				model: {
        					id: "stationCodeVcc",
        					fields: {
        						stt: {type: "text",editable: false },
        						stationType: {type: "text",editable: false  },
        						maiDat: {type: "text",editable: false },
        						doCaoCot: {type: "number",editable: false },
        						loaiCot: {type: "text",editable: false },
        						mongCo: {type: "text",editable: false },
        						loaiNha: {type: "text",editable: false },
        						tiepDia: {type: "text",editable: false  },
        						dienCnkt: {type: "text",editable: false },
        						soCotDien: {type: "text",editable: false },
        						vanChuyenBo: {type: "text",editable: false },
        						thueAcquy: {type: "text",editable: false },
        						giaThueMbThucTe: {type: "number",editable: false },
        					}
        				}
        			}
        		});
            	vm.tthqDataContractTblOptions = kendoConfig.getGridOptions({
                    autoBind: true,
                    scrollable: true,
                    resizable: true,
                    dataBinding: function () {
                    	recordContract = (this.dataSource.page() - 1) * this.dataSource.pageSize();
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
                                return ++recordContract;
                            },
                            width: '80px',
                            columnMenu: false,
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:center;"
                            }
                        },
                        {
                            title: "Loại trạm",
                            field: 'stationType',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                        },
                        {
                            title: "Mái/đất",
                            field: 'maiDat',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                        },
                        {
                            title: "Độ cao cột",
                            field: 'doCaoCot',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                        },
                        {
                            title: "Loại cột",
                            field: 'loaiCot',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                        },
                        {
                            title: "Móng co",
                            field: 'mongCo',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                        },
                        {
                            title: "Loại nhà",
                            field: 'loaiNha',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                        },
                        {
                            title: "Tiếp địa",
                            field: 'tiepDia',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                        },
                        {
                            title: "Điện CNKT",
                            field: 'dienCnkt',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                        },
                        {
                            title: "Số cột điện",
                            field: 'soCotDien',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                        },
                        {
                            title: "Vận chuyển bộ",
                            field: 'vanChuyenBo',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                        },
                        {
                            title: "Thuê Acquy",
                            field: 'thueAcquy',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                        },
                        {
                            title: "Giá thuê MB thực tế",
                            field: 'giaThueMbThucTe',
                            width: '150px',
                            headerAttributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                            attributes: {
                                style: "text-align:center;white-space: normal;"
                            },
                        },
                    ]
                });
            }

            function checkPause(wo) {
                if (wo.state == "PAUSE") return true;
                return false;
            }

            function changeStateCdPause(opinionType) {
            	var obj = {
                        ftId: vm.workingWO.ftId,
                        woId: vm.workingWO.woId,
                        loggedInUser: $scope.loggedInUser,
                        moneyValue: vm.label.moneyValueMil,
                        woTypeCode: vm.workingWO.woTypeCode,
                        state: vm.workingWO.state,
                        opinionType: opinionType
                    };

                    woDetailsService.changeStateCdPause(obj).then(
                        function (resp) {
                            if (resp && resp.statusCode == 1) {
                                publishChange();
                                toastr.success("Đánh giá thành công.");
                                kendo.ui.progress($(".tab-content"), false);
                            } else {
                            	kendo.ui.progress($(".tab-content"), false);
                                if (resp.message) {
                                    var errorMsg = resp.message;
                                    if (errorMsg == 'ERROR') errorMsg = 'Có lỗi xảy ra! ';
                                    toastr.error(resp.message + ' ' + (resp.data != null ? resp.data.customField : ""));
                                    return;
                                } else {
                                    toastr.error("Có lỗi xảy ra!");
                                    return;
                                }
                            }
                            var woId = vm.workingWO.woId;
                            getWODetails(woId);
                            vm.woHistory.dataSource.read();
                            vm.woCheckList.dataSource.read();
                        },
                        function (error) {
                            toastr.error("Có lỗi xảy ra!");
                        }
                    )
            }

            function changeStateTthtPause(request) {
                var obj = {
                    ftId: vm.workingWO.ftId,
                    woId: vm.workingWO.woId,
                    loggedInUser: $scope.loggedInUser,
                    contractId: vm.workingWO.contractId,
                    moneyValue: request.moneyValueMil,
                    woTypeCode: vm.workingWO.woTypeCode,
                    state: vm.workingWO.state,
                    catWorkItemTypeName: vm.workingWO.catWorkItemTypeName,
                    finishDate: $scope.confirmWO.finishDate
                };

                woDetailsService.changeStateTthtPause(obj).then(
                    function (resp) {
                        if (resp && resp.statusCode == 1) {
                            publishChange();
                            checkWoCompleteToUpdateTr();
                            toastr.success('Đánh giá thành công.');
                        } else {
                        	if(resp.data && resp.data.customField){
                        		toastr.error(resp.data.customField);
                                return;
                        	}
                            if (resp.message) {
                                toastr.error(resp.message);
                                return;
                            } else {
                                toastr.error('Có lỗi xảy ra!');
                                return;
                            }
                        }
                        var woId = vm.workingWO.woId;
                        getWODetails(woId, true);
                    },
                    function (error) {
                        toastr.error('Có lỗi xảy ra!');
                    }
                )
            }

            function changeStateDthtPause(request) {
                var obj = {
                    ftId: vm.workingWO.ftId,
                    woId: vm.workingWO.woId,
                    loggedInUser: $scope.loggedInUser,
                    contractId: vm.workingWO.contractId,
                    woTypeCode: vm.workingWO.woTypeCode,
                    state: vm.workingWO.state,
                    finishDate: request.finishDate,
                    moneyValue: request.moneyValueMil,
                    catWorkItemTypeName: vm.workingWO.catWorkItemTypeName
                };

                woDetailsService.changeStateDthtPause(obj).then(
                    function (resp) {
                        if (resp && resp.statusCode == 1) {
                            publishChange();
                            checkWoCompleteToUpdateTr();
                            toastr.success('Đánh giá thành công.');
                        } else {
                        	if(resp.data && resp.data.customField){
                        		toastr.error(resp.data.customField);
                                return;
                        	}
                            if (resp.message) {
                                toastr.error(resp.message);
                                return;
                            } else {
                                toastr.error('Có lỗi xảy ra!');
                                return;
                            }
                        }
                        var woId = vm.workingWO.woId;
                        getWODetails(woId, true);
                    },
                    function (error) {
                        toastr.error('Có lỗi xảy ra!');
                    }
                )
            }

            $scope.validateWoVuong = function () {
            	if ($scope.confirmWO.finishDate == undefined || $scope.confirmWO.finishDate==null) {
                    toastr.error("Chưa chọn ngày kết thúc!");
                    kendo.ui.progress($(".tab-content"), false);
                    return;
                }
                return true;
            };

            vm.approvedOkWoVuong = function(){
            	$modal.open({
                    templateUrl: 'coms/wo_xl/common/approveOkWoVuong.html',
                    controller: null,
                    windowClass: 'app-modal-window',
                    scope: $scope
                })
                    .result.then(
                    function () {
//                        changeStateCdPause();
                        kendo.ui.progress($(".tab-content"), false);
                    },
                    function () {
                        kendo.ui.progress($(".tab-content"), false);
                    }
                )
            }

            function changeStateCdPauseReject() {

                var obj = {
                    ftId: vm.workingWO.ftId,
                    woId: vm.workingWO.woId,
                    loggedInUser: $scope.loggedInUser,
                    text: $scope.rejectComment.text,
                    woTypeCode: vm.workingWO.woTypeCode,
                };

                woDetailsService.changeStateCdPauseReject(obj).then(
                    function (resp) {
                        if (resp && resp.statusCode == 1) {
                            publishChange();
                            toastr.success("Đánh giá thành công.");
                        } else toastr.error("Có lỗi xảy ra!");
                        var woId = vm.workingWO.woId;
                        getWODetails(woId);
                        vm.woHistory.dataSource.read();
                        vm.woCheckList.dataSource.read();
                    },
                    function (error) {
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            }

            function changeStateTthtPauseReject(id, woState) {
                var obj = {
                    id: id,
                    ftId: vm.workingWO.ftId,
                    woId: vm.workingWO.woId,
                    loggedInUser: $scope.loggedInUser,
                    state: woState,
                    text: $scope.rejectComment.text,
                    woTypeCode: vm.workingWO.woTypeCode,
                };

                woDetailsService.changeStateTthtPauseReject(obj).then(
                    function (resp) {
                        if (resp && resp.statusCode == 1) {
                            publishChange();
                            toastr.success("Đã từ chối WO !");
                        } else {
                            toastr.error("Có lỗi xảy ra!");
                        }
                        getWODetails(vm.workingWO.woId, true);
                    },
                    function (error) {
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            }

            function changeStateDthtPauseReject(id, woState) {
                var obj = {
                    id: id,
                    ftId: vm.workingWO.ftId,
                    woId: vm.workingWO.woId,
                    loggedInUser: $scope.loggedInUser,
                    state: woState,
                    text: $scope.rejectComment.text,
                    woTypeCode: vm.workingWO.woTypeCode,
                };

                woDetailsService.changeStateDthtPauseReject(obj).then(
                    function (resp) {
                        if (resp && resp.statusCode == 1) {
                            publishChange();
                            toastr.success("Đã từ chối WO !");
                        } else {
                            toastr.error("Có lỗi xảy ra!");
                        }
                        getWODetails(vm.workingWO.woId, true);
                    },
                    function (error) {
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            }


            vm.changeStateReceivedPqt = changeStateReceivedPqt
            function changeStateReceivedPqt(newMoneyValue) {
                var obj = {
                    ftId: vm.workingWO.ftId,
                    woId: vm.workingWO.woId,
                    loggedInUser: $scope.loggedInUser,
                    contractId: vm.workingWO.contractId,
                    moneyValueHtct: newMoneyValue,
                    woTypeCode: vm.workingWO.woTypeCode,
                    state: 'RECEIVED_PQT',
                    finishDate: $scope.confirmWO.finishDate,
                    catWorkItemTypeName: vm.label.catWorkName
                };

                woDetailsService.changeStateApprovedOrRejectWoHtctPQT(obj).then(
                    function (resp) {
                        if (resp && resp.statusCode == 1) {
                            publishChange();
                            checkWoCompleteToUpdateTr();

                            toastr.success('Đánh giá thành công.');
                        } else {
                        	if(resp.data && resp.data.customField){
                        		toastr.error(resp.data.customField);
                                return;
                        	}
                            if (resp.message) {
                                toastr.error(resp.message);
                                return;
                            } else {
                                toastr.error('Có lỗi xảy ra!');
                                return;
                            }
                        }
                        var woId = vm.workingWO.woId;
                        getWODetails(woId, true);
                    },
                    function (error) {
                        toastr.error('Có lỗi xảy ra!');
                    }
                )
            }

            vm.changeStateReceivedTtDtht = changeStateReceivedTtDtht
            function changeStateReceivedTtDtht(newMoneyValue) {
                var obj = {
                    ftId: vm.workingWO.ftId,
                    woId: vm.workingWO.woId,
                    loggedInUser: $scope.loggedInUser,
                    contractId: vm.workingWO.contractId,
                    moneyValueHtct: newMoneyValue,
                    woTypeCode: vm.workingWO.woTypeCode,
                    state: 'RECEIVED_TTDTHT',
                    finishDate: $scope.confirmWO.finishDate,
                    catWorkItemTypeName: vm.label.catWorkName
                };

                woDetailsService.changeStateApprovedOrRejectWoHtctTtDtht(obj).then(
                    function (resp) {
                        if (resp && resp.statusCode == 1) {
                            publishChange();
                            checkWoCompleteToUpdateTr();
                            toastr.success('Đánh giá thành công.');
                        } else {
                        	if(resp.data && resp.data.customField){
                        		toastr.error(resp.data.customField);
                                return;
                        	}
                            if (resp.message) {
                                toastr.error(resp.message);
                                return;
                            } else {
                                toastr.error('Có lỗi xảy ra!');
                                return;
                            }
                        }
                        var woId = vm.workingWO.woId;
                        getWODetails(woId, true);
                    },
                    function (error) {
                        toastr.error('Có lỗi xảy ra!');
                    }
                )
            }

            function changeStateWoHtctPQTReject(id, woState) {
                var obj = {
                    id: id,
                    ftId: vm.workingWO.ftId,
                    woId: vm.workingWO.woId,
                    loggedInUser: $scope.loggedInUser,
                    state: "REJECT_PQT",
                    text: $scope.rejectComment.text,
                    woTypeCode: vm.workingWO.woTypeCode,
                };

                woDetailsService.changeStateApprovedOrRejectWoHtctPQT(obj).then(
                    function (resp) {
                        if (resp && resp.statusCode == 1) {
                            publishChange();
                            toastr.success("Đã từ chối WO !");
                        } else {
                            toastr.error("Có lỗi xảy ra!");
                        }
                        getWODetails(vm.workingWO.woId, true);
                    },
                    function (error) {
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            }

            function changeStateWoHtctTtDthtReject(id, woState) {
                var obj = {
                    id: id,
                    ftId: vm.workingWO.ftId,
                    woId: vm.workingWO.woId,
                    loggedInUser: $scope.loggedInUser,
                    state: "REJECT_TTDTHT",
                    text: $scope.rejectComment.text,
                    woTypeCode: vm.workingWO.woTypeCode,
                };

                woDetailsService.changeStateApprovedOrRejectWoHtctTtDtht(obj).then(
                    function (resp) {
                        if (resp && resp.statusCode == 1) {
                            publishChange();
                            toastr.success("Đã từ chối WO !");
                        } else {
                            toastr.error("Có lỗi xảy ra!");
                        }
                        getWODetails(vm.workingWO.woId, true);
                    },
                    function (error) {
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            }

            var woCompleteRecord = 0;

            function fillDataTableWoComplete() {
                vm.woCompleteDataTblOptions = kendoConfig.getGridOptions({
                    autoBind: true,
                    scrollable: false,
                    resizable: true,
                    editable: false,
                    save: function () {
                        vm.woCompleteDataTbl.dataSource.read();
                    },
                    dataBinding: function () {
                    	woCompleteRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    reorderable: true,
                    toolbar: null,
                    dataSource: {
                        serverPaging: false,
                        schema: {
                            data: function (response) {
                                return response.data;
                            }
                        },
                        transport: {
                            read: {
                                url: Constant.BASE_SERVICE_URL + "woService/workItem/getWorkItemCompleteByConstruction",
                                contentType: "application/json; charset=utf-8",
                                type: "POST"
                            },
                            parameterMap: function (options, type) {
                                var searchOptions = {constructionId: vm.workingWO.constructionId}
                                searchOptions.woType = vm.workingWO.woTypeCode;
                                return JSON.stringify(searchOptions)
                            }
                        },
                        pageSize: 10,
                    },
                    noRecords: true,
                    columnMenu: false,
                    messages: {
                        noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                    },
                    pageable: false,
                    columns: [
                        {
                            title: "STT",
                            field: "stt",
                            template: function () {
                                return ++woCompleteRecord;
                            },
                            width: '5%',
                            columnMenu: false,
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:center;"
                            },
                            type: 'text',

                        },
                        {
                            title: "Mã WO",
                            field: 'woCode',
                            width: '30%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            type: 'text',
                            template: dataItem => '<a style="text-decoration: none;" class="#=woId#" href="javascript:void(0);" ng-click=vm.showPopupDetail(dataItem)> {{dataItem.woCode}} </a>',
                        },
                        {
                            title: "Hạng mục",
                            field: 'catWorkItemTypeName',
                            width: '30%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            type: 'text'
                        },
                        {
                            title: "Giá trị (triệu VND)",
                            field: 'moneyValue',
                            width: '15%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:center;"
                            },
                            type: 'number',
                        },
                        {
                            title: "Trạng thái",
                            field: 'state',
                            width: '20%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            type: 'text',
                            template: function(dataItem) {
                            	let state = null;
                            	Object.keys(Constant.WO_XL_STATE).forEach(
                                        function (key, index) {
                                            if (Constant.WO_XL_STATE[key].stateCode == dataItem.state) {
                                            	state = Constant.WO_XL_STATE[key].stateText;
                                            }
                                        }
                                    );
                            	return state;
                            }
                        },
                        {
    						title: "Thao tác",
    				        template: dataItem =>
    						'<div class="text-center #=woCode#"">'+
    						'<a type="button" class="#=woCode# icon_table" uib-tooltip="Từ chối WO" translate ng-click="vm.rejectWoCompleted(dataItem)">'+
    							'<i class="fa fa-reply" aria-hidden="true"></i>'+
    						'</a>'+
    						'</div>',
    				        width: '10%',
    				        field:"action",
    			        },
                    ]
                });
            }

            vm.showPopupDetail = function showPopupDetail(dataItem){
    			var dataItem = dataItem;
    	        var teamplateUrl="coms/wo_xl/common/popupDetailWo.html";
    	        var title="Thông tin chi tiết WO";
    	        var windowId="DETAIL_WI";
    	        var obj = {woId: dataItem.woId};
    	        woDetailsService.getOneWODetails(obj).then(
                        function (resp) {
                            console.log(resp);
                            if (resp && resp.data) {
                            	resp.data.moneyValue =CommonService.removeTrailingZero((resp.data.moneyValue / 1000000).toFixed(3));
                            	vm.workingWODetail = angular.copy(resp.data);
                            }
                            CommonService.populatePopupCreate(teamplateUrl,title,dataItem,vm,windowId,false,'80%','80%',"code123");
                        },
                        function (error) {
                            console.log(error);
                            toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                        }
                    )
    		}

            vm.validateRejectWoComplete = validateRejectWoComplete;
            function validateRejectWoComplete() {
                if (!$scope.rejectWoCompleted.text || $scope.rejectWoCompleted.text == '') {
                    toastr.error('Chưa nhập lý do từ chối!');
                    return false;
                }
                return true;
            }

            $scope.rejectWoCompleted = {};
            vm.rejectWoCompleted = function (dataItem) {
            	$scope.rejectWoCompleted = angular.copy(dataItem);
                $modal.open({
                    templateUrl: 'coms/wo_xl/common/rejectWoCompleted.html',
                    controller: null,
                    windowClass: 'app-modal-window',
                    scope: $scope
                })
                    .result.then(
                    function () {
                    	var obj = {};
                    	obj = angular.copy($scope.rejectWoCompleted);
                    	obj.woParentId = vm.workingWO.woId;
                    	woDetailsService.changeStateWoReject(obj).then(
                                function (resp) {
                                	if (resp && resp.statusCode == 1) {
                                        publishChange();
                                        checkWoCompleteToUpdateTr();
                                        toastr.success('Từ chối thành công.');
                                    } else {
                                    	if(resp.data && resp.data.customField){
                                    		toastr.error(resp.data.customField);
                                            return;
                                    	}
                                        if (resp.message) {
                                            toastr.error(resp.message);
                                            return;
                                        } else {
                                            toastr.error('Có lỗi xảy ra!');
                                            return;
                                        }
                                    }
                                    var woId = vm.workingWO.woId;
                                    getWODetails(woId, true);
                                },
                                function (error) {
                                    console.log(error);
                                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                                }
                            )
                    },
                    function () {
                    	$scope.rejectWoCompleted.text = null;
                    }
                )
            };

            //Huy-end

            vm.reProcessWo = function(){
            	confirm("Xác nhận thực hiện lại WO ?", function(){
            		var obj = {
                            woId: vm.workingWO.woId,
                            loggedInUser: $scope.loggedInUser,
                            woTypeCode: vm.workingWO.woTypeCode
                        };

                        woDetailsService.changeStateReProcessWoDoanhThuDTHT(obj).then(
                            function (res) {
                                if (res && res.statusCode == 1) {
                                    toastr.success("Thực hiện WO thành công !");
                                    getWODetails(vm.workingWO.woId, true);
                                    reloadTables();
                                    publishChange();
                                } else {
                                    toastr.error("Có lỗi xảy ra!");
                                }
                            }
                        )
            	});
            }

            //Huypq-20012022-start
            vm.acceptHardFile = function () {
                $scope.isAcceptRejectQuantity = true;
                $scope.rejectComment = {};
                $scope.confirmWO.woTypeCode = vm.workingWO.woTypeCode;
                $scope.confirmWO.trTypeCode = vm.workingWO.trTypeCode;
                $scope.confirmWO.state = vm.workingWO.state;
                $scope.confirmWO.isAcceptRejectQuantity = true;
                $modal.open({
                    templateUrl: 'coms/wo_xl/common/approvedOrRejectHardFile.html',
                    controller: null,
                    windowClass: 'app-modal-window',
                    scope: $scope
                }).result.then(
                    function () {
                        if ($scope.confirmWO.isWoDoneOk == undefined) {
                            toastr.error("Chưa chọn đánh giá!");
                        } else {
                            if ($scope.confirmWO.isWoDoneOk) {
                            	if(vm.workingWO.state=="WAIT_PQT") {
                    				changeStateReceivedPqt($scope.confirmWO.moneyValueMil);
                    			}

                        		if(vm.workingWO.state=="WAIT_TTDTHT") {
                    				changeStateReceivedTtDtht($scope.confirmWO.moneyValueMil);
                    			}
                            } else {
                            	if(vm.workingWO.state=="WAIT_PQT") {
                    				changeStateRejectHardFile("PQT_NG");
                    			}

                        		if(vm.workingWO.state=="WAIT_TTDTHT") {
                        			changeStateRejectHardFile("TTDTHT_NG");
                    			}
                            }
                        }
                    }
                )
            };

            vm.validateRejectHardFile = validateRejectHardFile;
            function validateRejectHardFile() {
                if (!$scope.confirmWO.isWoDoneOk && (!$scope.rejectComment.text || $scope.rejectComment.text == '')) {
                    toastr.error('Chưa nhập lý do từ chối!');
                    return false;
                }
                return true;
            }

            vm.changeStateRejectHardFile = changeStateRejectHardFile
            function changeStateRejectHardFile(state) {
                var obj = {
                    ftId: vm.workingWO.ftId,
                    woId: vm.workingWO.woId,
                    loggedInUser: $scope.loggedInUser,
                    contractId: vm.workingWO.contractId,
                    woTypeCode: vm.workingWO.woTypeCode,
                    state: state,
                    finishDate: $scope.confirmWO.finishDate,
                    catWorkItemTypeName: vm.label.catWorkName
                };

                woDetailsService.changeStateApprovedOrRejectWoHtctPQT(obj).then(
                    function (resp) {
                        if (resp && resp.statusCode == 1) {
                            publishChange();
                            checkWoCompleteToUpdateTr();
                            toastr.success('Đánh giá thành công.');
                        } else {
                        	if(resp.data && resp.data.customField){
                        		toastr.error(resp.data.customField);
                                return;
                        	}
                            if (resp.message) {
                                toastr.error(resp.message);
                                return;
                            } else {
                                toastr.error('Có lỗi xảy ra!');
                                return;
                            }
                        }
                        var woId = vm.workingWO.woId;
                        getWODetails(woId, true);
                    },
                    function (error) {
                        toastr.error('Có lỗi xảy ra!');
                    }
                )
            }
            //Huy-end
            vm.fileLst = null;
            $('#attachFile').on('change', () => {
            	console.log("sdfdsafds")
            })
            vm.submitAttachFile = submitAttachFile;
            function submitAttachFile() {
    			sendFile("attachFile", callback);
    		}

            function sendFile(id, callback) {

    			if(!htmlCommonService.checkFileExtension(id)){
    				toastr.warning("Định dạng file không hợp lệ");
    				return;
    			}

    			var formData = new FormData();
    	        jQuery.each($("#attachFile")[0].files, function (i, files) {
    		        formData.append('multipartFile' + i, files);
    	        });
    	        var files = $("#attachFile")[0].files;
    	        return $.ajax({
                    url: Constant.BASE_SERVICE_URL + "fileservice/uploadATTTInput",
                    type: "POST",
                    data: formData,
                    enctype: 'multipart/form-data',
                    processData: false,
                    contentType: false,
                    cache: false,
                    success: function (data) {
                        callback(data, files)
                    }
                });

    		}

            vm.callback = callback;
            function callback(data, files) {
            	 var file = {};
                 file.name = files[0].name;
                 file.createdDate = htmlCommonService.getFullDate();
                 file.createdUserName = Constant.userInfo.vpsUserToken.fullName;
                 file.createdUserId = Constant.userInfo.vpsUserToken.sysUserId;
                 file.filePath = data[0];
                 file.type = 0;
                 vm.fileLst = file;
    		}
            $scope.fileNameChanged = () => {
            	 vm.fileLst = null;
            	 console.log("Is gpth: ", vm.isGpxd)
            	 submitAttachFile();
            }

        <!-- ThanhPT - Danh sach vat tu KTTS - start-->
        var woGoodsRecord = 0;
        function fillDataTableWoGoods() {
            vm.woGoodsTblOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: false,
                editable: false,
                save: function () {
                    vm.woGoodsTbl.dataSource.read();
                },
                dataBinding: function () {
                    woGoodsRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: false,
                    schema: {
                        total: function (response) {},
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/wo/getStationResource",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var searchOptions = {woId: vm.workingWO.woId}
                            return JSON.stringify(searchOptions)
                        }
                    },
                    pageSize: 10,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: false,
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++woGoodsRecord;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',

                    },
                    {
                        title: "Tên tài sản",
                        field: 'assetName',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text'
                    },
                    {
                        title: "Mã tài sản",
                        field: 'assetCode',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text'
                    },
                    {
                        title: "Serial",
                        field: 'serial',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text'
                    },
                    {
                        title: "Số lượng",
                        field: 'amount',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'number',
                        format: '{0:n0}'
                    },
                    {
                        title: "Đơn giá",
                        field: 'unitPrice',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'number',
                        format: '{0:n0}'
                    },
                    {
                        title: "Thành tiền",
                        field: 'intoMoney',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'number',
                        format: '{0:n0}'
                    },
                    {
                        title: "Tình trạng",
                        field: 'reportStatus',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text'
                    },
                    {
                        title: "Đơn vị quản lý",
                        field: 'managementUnit',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text'
                    },
                    {
                        title: "Người quản lý",
                        field: 'personManager',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text'
                    },
                    {
                        title: "Nhân viên",
                        field: 'employeeCode',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text'
                    },
                ]
            });
        }
        <!-- ThanhPT - Danh sach vat tu KTTS - end-->

// end controller
    }
})();
