(function () {
    'use strict';
    var controllerId = 'woCreateEditTemplateController';

    angular.module('MetronicApp').controller(controllerId, woCreateEditTemplateController);

    function woCreateEditTemplateController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                            kendoConfig, $kWindow, woCreateNewService, htmlCommonService, vpsPermissionService,
                                            CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $compile) {

        var ctr = this;
        $scope.woTypes = [];
        $scope.totalMonthPlans = [{name: "Tháng 1", value: 1}, {name: "Tháng 2", value: 2}, {
            name: "Tháng 3",
            value: 3
        }, {name: "Tháng 4", value: 4},
            {name: "Tháng 5", value: 5}, {name: "Tháng 6", value: 6}, {name: "Tháng 7", value: 7}, {
                name: "Tháng 8",
                value: 8
            }, {name: "Tháng 9", value: 9},
            {name: "Tháng 10", value: 10}, {name: "Tháng 12", value: 12}, {name: "Tháng 12", value: 12}];
        $scope.apConstructionTypes = {};
        $scope.apWorkSrcs = {};
        $scope.apWorkSrcs1 = {};
        $scope.selectedConstruction = {};
        $scope.cdLv2List = {};
        $scope.cdLv3List = {};
        $scope.workItemTypes = {};
        $scope.trList = {};
        $scope.ftList = {};
        $scope.isDisable = {};
        $scope.label = {};
        $scope.isTrSelected = false;
        $scope.cdLv1List = {};
        $scope.isCreateNew = true;
        $scope.isFt1 = false;
        $scope.woNames = {};
        $scope.months = [
            {value: 1, text: 'Tháng 1'},
            {value: 2, text: 'Tháng 2'},
            {value: 3, text: 'Tháng 3'},
            {value: 4, text: 'Tháng 4'},
            {value: 5, text: 'Tháng 5'},
            {value: 6, text: 'Tháng 6'},
            {value: 7, text: 'Tháng 7'},
            {value: 8, text: 'Tháng 8'},
            {value: 9, text: 'Tháng 9'},
            {value: 10, text: 'Tháng 10'},
            {value: 11, text: 'Tháng 11'},
            {value: 12, text: 'Tháng 12'},
        ]

        $scope.partners = ['MobileFone', 'DAS', 'CMC'];

        $scope.AIO_AP_WORK_SRC = 8;
        $scope.isAIO = false;
        $scope.isProject = false;
        $scope.visibleThdt = false;
        $scope.label.THDT = "Thu tiền";
        $scope.label.woAIOName = "Hợp đồng AIO";
        $scope.selectedWoType = {};
        $scope.cdLevel1Group = {};
        $scope.isClonning = false;
        $scope.hcqtProjects = [];
        $scope.isWoFromTr = false;
        $scope.hshcChecklistItem = [];
        $scope.crudCnktCdLevel2 = '';
        $scope.crudCnktCdLevel2Name = '';

        $scope.checkRoleProvince = false;
        $scope.tthtSysGroupId = '242656';
        $scope.vhktSysGroupId = '270120';
        $scope.xdddSysGroupId = '9006003';
        $scope.gpthSysGroupId = '280483';
        $scope.xddthtSysGroupId = '166677';

        $scope.tcTctEmails = [];

        // Unikom_20210527_start
        $scope.assignBchCt = false;
        // Unikom_20210527_end

        init();

        function init() {
            $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
            $scope.selectedWoTypeForCreateNew = $rootScope.selectedWoTypeForCreateNew;
            $rootScope.selectedWoTypeForCreateNew = null;
            if ($rootScope.isClonning == 1) {
                $scope.workingWO = $rootScope.cloneWo;
                console.log("clonning");
                console.log($scope.workingWO);
                $scope.isDisable.trChoose = true;
                if ($scope.workingWO.apWorkSrc) $scope.workingWO.apWorkSrc = $scope.workingWO.apWorkSrc.toString();
                if ($scope.workingWO.apConstructionType) $scope.workingWO.apConstructionType = $scope.workingWO.apConstructionType.toString();
                if ($scope.workingWO.cdLevel2) $scope.workingWO.cdLevel2 = $scope.workingWO.cdLevel2.toString();


                if ($scope.workingWO.trId != null) {
                    $scope.workingWO.trId = parseInt($scope.workingWO.trId);
                }

                //getConstructionByCode($scope.workingWO.constructionCode);
                //getConstructionByProject();
                //getConstructionByContract();
                getCatWorkTypes();

                $rootScope.cloneWo = null;
                $rootScope.isClonning = 0;
                $scope.isClonning = true;
                getWoTypes(function () {
                    selectWoType();
                });

                $scope.isWoFromTr = false;
            } else if ($rootScope.trForNewWo && $rootScope.trForNewWo != undefined) {
                //console.log($rootScope.trForNewWo);
                getWoTypes();
                bindTrToWo($rootScope.trForNewWo);
                if ($rootScope.trForNewWo.projectCode) {
                    $scope.isProject = true;
                    $scope.label.projectCode = $rootScope.trForNewWo.projectCode;
                }
                $rootScope.trForNewWo = null;
                $scope.isWoFromTr = true;
                $scope.isClonning = false;
            } else {
                $scope.isClonning = false;
                // Unikom - create wo UCTT
                if ($scope.permissions.createdWoUctt || $scope.permissions.createdWoHshc) {
                    Restangular.all("trService/group/getSysGroupById").post({sysGroupId: 270120}).then(
                        function (resp) {
                            if (resp && resp.data) {
                                $scope.cdLevel1Group = resp.data;
                                $scope.workingWO.cdLevel1 = resp.data.sysGroupId;
                                $scope.workingWO.cdLevel1Name = resp.data.groupName;
                                getWoTypes();
                                getCdLv2List();
                            }
                        },
                        function (error) {
                            toastr.success("Có lỗi xảy ra!");
                        }
                    );

                    $scope.workingWO.state = 'ACCEPT_CD';
                } else if ($scope.permissions.createWODomainDataList || $scope.permissions.createWODOANHTHUDomainDataList) {
                    var cdLevel1Domain = $scope.permissions.createWODomainDataList.split(",")[0];
                    if ($scope.permissions.createWODOANHTHU) cdLevel1Domain = $scope.permissions.createWODOANHTHUDomainDataList.split(",")[0];
                    var obj = {sysGroupId: cdLevel1Domain};
                    Restangular.all("trService/group/getSysGroupById").post(obj).then(
                        function (resp) {
                            if (resp && resp.data) {
                                $scope.cdLevel1Group = resp.data;
                                $scope.workingWO.cdLevel1 = resp.data.sysGroupId;
                                $scope.workingWO.cdLevel1Name = resp.data.groupName;

                                if ($scope.permissions.crudWOCnkt) {
                                    $scope.workingWO.type = 1;
                                    $scope.workingWO.state = 'ACCEPT_CD';
                                } else if ($scope.workingWO.cdLevel1 == $scope.vhktSysGroupId) {
                                    $scope.workingWO.type = 2;
                                }

                                getWoTypes(function () {
                                    for (var i = 0; i < $scope.woTypes.length; i++) {
                                        if ($scope.woTypes[i].woTypeId == $scope.selectedWoTypeForCreateNew) $scope.workingWO.woTypeId = $scope.selectedWoTypeForCreateNew;
                                    }
                                    selectWoType();
                                });
                                getCdLv2List();
                            }
                        },
                        function (error) {
                            toastr.success("Có lỗi xảy ra!");
                        }
                    );
                }
                //Huypq-14102020-start
                else {
                    Restangular.all("trService/group/getSysGroupById").post({sysGroupId: 242656}).then(
                        function (resp) {
                            if (resp && resp.data) {
                                $scope.cdLevel1Group = resp.data;
                                $scope.workingWO.cdLevel1 = resp.data.sysGroupId;
                                $scope.workingWO.cdLevel1Name = resp.data.groupName;
                                getWoTypes();
                                getCdLv2List();
                            }
                        },
                        function (error) {
                            toastr.success("Có lỗi xảy ra!");
                        }
                    );

                    var vpsPermissions = $rootScope.casUser.authorizedData.businessUserPermissions;

                    for (var i = 0; i < vpsPermissions.length; i++) {
                        var pmCode = vpsPermissions[i].permissionCode;
                        if (pmCode == Constant.PERMISSIONS.CRUD_CNKT_WOXL) {
                            $scope.workingWO.cdLevel2 = vpsPermissions[i].domainDataList;
                            $scope.crudCnktCdLevel2 = $scope.workingWO.cdLevel2;
                            break;
                        }
                    }

                    if ($scope.workingWO.cdLevel2 && $scope.workingWO.cdLevel2 != '') {
                        Restangular.all("woService/wo/getSysGroupNameById").post($scope.workingWO.cdLevel2).then(
                            function (resp) {
                                if (resp && resp.data) {
                                    $scope.workingWO.cdLevel2Name = resp.data.name;
                                    $scope.crudCnktCdLevel2Name = $scope.workingWO.cdLevel2Name;
                                }
                            },
                            function (error) {
                                toastr.success("Có lỗi xảy ra khi lấy thông tin CD level 2!");
                            }
                        );
                    }
                }
                //Huypq-14102020-end
                if ($scope.isEditting) {
                    $scope.isCreateNew = false;
                    selectWoType();
                    $scope.label.moneyValueMil = (parseInt($scope.workingWO.moneyValue) / 1000000).toFixed(3);
                    $scope.label.moneyFlowValueMil = $scope.workingWO.moneyFlowValue ? ($scope.workingWO.moneyFlowValue / 1000000).toFixed(3) : '';
                    $scope.label.moneyFlowRequiredMil = $scope.workingWO.moneyFlowRequired ? ($scope.workingWO.moneyFlowRequired / 1000000).toFixed(3) : '';
                }
                $scope.isWoFromTr = false;
            }


            getAppWorkSrcs();
            getAppConstructionTypes();
            getSysUserGroup();
            //getAvailableTR();
            //getCdLevel1List();
            //getWoNames();
            getHcqtProjects();
            fillDataTableWorkItem();
            fillXdddChecklistDataTbl();
            getTcTctEmails();
        }

        function getHcqtProjects() {
            var obj = {
                page: 1, pageSize: 99999
            };
            Restangular.all("woService/hcqtProject/doSearchHcqtProject").post(obj).then(
                function (resp) {
                    if (resp && resp.data) {
                        $scope.hcqtProjects = resp.data;
                    }
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            );
        }

        function setCatWorkTypeAIO(contractId) {
            $scope.workingWO.catWorkItemTypeId = -9999;
            $scope.workingWO.contractId = contractId;
            $scope.isDisable.catWorkChoose = true;
            $scope.workingWO.catWorkItemTypeName = "Hạng mục theo hợp đồng AIO";
        }

        $scope.selectWoType = selectWoType;

        function selectWoType() {
            $scope.visibleThdt = false;
            $scope.selectedWoType = {};

            if (!$scope.isEditting) {
                $scope.workingWO.apWorkSrc = null;
                $scope.workingWO.apWorkSrcName = null;
                $scope.workingWO.apConstructionType = null;
                $scope.workingWO.apConstructionTypeNull = null;
                if (!$scope.isWoFromTr) {
                    $scope.workingWO.trId = null;
                    $scope.workingWO.trCode = null;
                }

            }

            if (!$scope.workingWO.woTypeId || $scope.workingWO.woTypeId == '') return;

            var obj = {page: 1, pageSize: 99999, woTypeId: $scope.workingWO.woTypeId};

            Restangular.all("woService/woName/doSearch").post(obj).then(
                function (resp) {
                    if (resp && resp.data) {
                        $scope.woNames = resp.data;
                    }
                },
                function (error) {
                    toastr.success("Có lỗi xảy ra!");
                }
            );

            // Neu la THDT thi hien thi gia tri phai thu

            for (var i = 0; i < $scope.woTypes.length; i++) {
                if ($scope.woTypes[i].woTypeId == $scope.workingWO.woTypeId) {
                    $scope.workingWO.woTypeCode = $scope.woTypes[i].woTypeCode;
                    $scope.selectedWoType = $scope.woTypes[i];
                }
            }

            if ("AIO" == $scope.workingWO.woTypeCode) {
                $scope.isAIO = true;
                $scope.visibleThdt = false;
                $scope.visibleHcqt = false;
                $scope.workingWO.catWorkItemTypeName = "Hạng mục theo hợp đồng AIO";
                $scope.workingWO.woName = "Hợp đồng AIO";
                $scope.workingWO.woNameId = 217; //hard code this
            } else {
                $scope.isAIO = false;
                if (!$scope.isEditting) {
                    $scope.workingWO.catWorkItemTypeName = null;
                    $scope.workingWO.catWorkItemTypeId = null;
                }

                if (!$scope.isEditting) {
                    $scope.workingWO.catWorkItemTypeName = null;
                    $scope.workingWO.catWorkItemTypeId = null;
                }

                if ("THDT" == $scope.workingWO.woTypeCode) {
                    $scope.visibleThdt = true;
                    $scope.visibleHcqt = false;
                }

                if ("HCQT" == $scope.workingWO.woTypeCode) {
                    $scope.visibleHcqt = true;
                    $scope.visibleThdt = false;
                } else {
                    $scope.visibleHcqt = false;
                    $scope.visibleThdt = false;
                }
            }


            if ($scope.selectedWoType.hasApWorkSrc != 1 && !$scope.isEditting && !$scope.isClonning) $scope.workingWO.apWorkSrc = null;
            if ($scope.selectedWoType.hasConstruction != 1 && !$scope.isEditting && !$scope.isClonning && !$scope.isWoFromTr) {
                $scope.workingWO.apConstructionType = null;
                $scope.workingWO.contractCode = null;
                $scope.workingWO.catWorkItemTypeId = null;
                $scope.workingWO.constructionId = null;
                $scope.workingWO.stationCode = null;
            }

            //Huypq-13012021-start
//            if($scope.workingWO.woTypeId=="1" || $scope.workingWO.woTypeId=="2"){
//            	if($scope.checkRoleProvince){
//            		$scope.apWorkSrcs = [
//            			{
//            				code: "2", name: "CP - Chi phí", parType: "AP_WORK_SRC", parOrder: 2, description: null
//            			}
//            		]
//            	}
//            }
            //Huy-end
        }

        function getCdLevel1List() {
            Restangular.all("trService/tr/getCdLevel1").post({}).then(
                function (resp) {
                    if (resp && resp.data) {
                        if ($scope.isCreateNew && !$scope.isTrSelected) {
                            $scope.workingWO.cdLevel1 = resp.data[0].code;
                        }
                        $scope.cdLv1List = resp.data;
                    }
                },
                function (error) {
                    //console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        $scope.selectTR = selectTR;

        function selectTR() {

            if ($scope.workingWO.trId == null || $scope.workingWO.trId == '') {
                $scope.workingWO.cdLevel1 = null;
                $scope.isDisable.constructionSelect = false;
                $scope.workingWO.constructionId = null;
                $scope.isTrSelected = false;
                $scope.isDisable.catWorkChoose = false;
                $scope.label.contractInfo = '';
                $scope.isAIO = false;
                return;
            }

            var obj = {trId: $scope.workingWO.trId};
            Restangular.all("trService/tr/getOneTRDetails").post(obj).then(
                function (resp) {
                    console.log(resp);
                    if (resp && resp.data) {
                        bindTrToWo(resp.data);
                    }
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )
        }

        $scope.selectWoName = function () {
            if (!$scope.workingWO.woNameId || $scope.workingWO.woNameId == '') {
                $scope.workingWO.woNameId = null;
                $scope.workingWO.woName = null;
                return;
            }

            for (var i = 0; i < $scope.woNames.length; i++) {
                if ($scope.woNames[i].id == $scope.workingWO.woNameId) {
                    $scope.workingWO.woName = $scope.woNames[i].name;
                    return;
                }
            }
        };

        function bindTrToWo(tr) {
            $scope.isAIO = false;

            $scope.workingWO.trId = tr.trId;
            $scope.workingWO.trName = tr.trName;
            $scope.workingWO.constructionId = tr.constructionId;
            $scope.workingWO.catConstructionTypeId = tr.catConstructionTypeId;
            $scope.workingWO.stationCode = tr.stationCode;
            $scope.workingWO.contractCode = tr.contractCode;
            $scope.workingWO.projectCode = tr.projectCode;
            $scope.workingWO.projectId = tr.projectId;
            $scope.workingWO.cdLevel1 = tr.cdLevel1;
            $scope.workingWO.cdLevel1Name = tr.cdLevel1Name;
            $scope.workingWO.cdLevel2 = tr.cdLevel2;
            $scope.workingWO.cdLevel2Name = tr.cdLevel2Name;
            $scope.workingWO.stationCode = tr.stationCode;
            $scope.selectedConstruction.constructionName = tr.constructionCode;
            $scope.workingWO.constructionCode = tr.constructionCode;
            $scope.label.contractInfo = tr.contractCode;
            $scope.workingWO.trCode = tr.trCode;
            $scope.workingWO.contractId = tr.contractId;
            $scope.workingWO.moneyValue = tr.quantityValue;
            $scope.label.moneyValueMil = tr.quantityValue ? tr.quantityValue / 1000000 : '';
            $scope.catConstructionTypeId = tr.catConstructionTypeId;
            $scope.constructionStatus = tr.constructionStatus;
            $scope.workingWO.provinceName = tr.provinceName;
            $scope.workingWO.sysGroupDistrictName = tr.sysGroupDistrictName;

            var consName = tr.constructionName;
            getCdLv2List();

            if (!tr.constructionId) {
                if (tr.trCode && tr.trCode.includes('AIO')) $scope.isAIO = true;
            }

            if ($scope.isAIO) {
                bindAIOValues(tr);
            } else {
                if ($scope.workingWO.cdLevel1 != $scope.vhktSysGroupId) {
                    autoAssignCdLevel2();
                }

                getCatWorkTypes();
            }

            if ($scope.workingWO.woTypeCode == 'HSHC') {
                $scope.label.moneyValueMil = null;
                $scope.workingWO.moneyValue = null;
                autoSuggestMoneyValue();
                ctr.workItemDataTbl.dataSource.read();
            }

            $scope.isTrSelected = true;
        }

        function bindAIOValues(tr) {
            setCatWorkTypeAIO(tr.contractId);
            var obj = {contractId: tr.contractId}
            woCreateNewService.getAIOWoInfo(obj).then(
                function (res) {
                    if (res && res.statusCode == 1) {
                        $scope.workingWO.woTypeId = res.data.woTypeId;
                        $scope.workingWO.woTypeCode = 'AIO';
                        $scope.workingWO.woNameId = res.data.woNameId;
                        $scope.workingWO.apWorkSrc = $scope.AIO_AP_WORK_SRC;
                        $scope.workingWO.cdLevel2 = res.data.cdLevel2;
                        $scope.workingWO.cdLevel2Name = res.data.cdLevel2Name;
                        $scope.workingWO.finishDate = tr.finishDate;
                        $scope.workingWO.userCreated = $scope.loggedInUser;
                        $scope.workingWO.moneyValue = tr.quantityValue;
                        $scope.workingWO.woName = res.data.woName;
                        $scope.label.woAIOName = res.data.woName;
                        $scope.label.apWorkSrcName = 'Hợp đồng AIO';
                    } else toastr.error("Có lỗi xảy ra!");
                }, function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function autoAssignCdLevel2() {
            if ($scope.workingWO.stationCode == null || $scope.workingWO.stationCode == '') return;

            var obj = {stationCode: $scope.workingWO.stationCode}
            Restangular.all("trService/tr/getCdLevel2FromStation").post(obj).then(
                function (resp) {
                    if (resp && resp.data) {
                        $scope.workingWO.cdLevel2 = resp.data.sysGroupId;
                        $scope.label.cdLevel2Name = resp.data.groupName;
                        $scope.getCdLv3List();
                        $scope.getFtList(2);
                    }
                },
                function (error) {
                    toastr.success("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )
        }

        function getSysUserGroup() {
            var obj = {loggedInUser: $scope.loggedInUser}
            Restangular.all("woService/user/getSysUserGroup").post(obj).then(
                function (resp) {
                    console.log(resp);
                    if (resp && resp.data) {
                        $scope.sysUserGroup = resp.data;
                        $scope.workingWO.sysGroupId = resp.data.sysGroupId;
                        $scope.workingWO.sysUserId = $scope.sysUserId;
                        //Huypq-13012021-start
                        if ($scope.sysUserGroup.parentGroupId != "166677" && $scope.sysUserGroup.parentGroupId != "242656" && $scope.sysUserGroup.parentGroupId != "280483") {
                            $scope.checkRoleProvince = true;
                        }
                        //Huy-end
                        // Unikom - create wo UCTT
                        getCdLv2List();
                        // Unikom - create wo UCTT
                    }
                },
                function (error) {
                    toastr.success("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )
        }

        function getConstructionByCode(code) {
            if (code == null || code == '') return;
            var obj = {constructionCode: code}
            woCreateNewService.getConstructionByCode(obj).then(
                function (resp) {
                    //console.log(resp);
                    if (resp.data) $scope.selectedConstruction.constructionName = resp.data.constructionName;
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )

        }

        function getWoTypes(callback) {
            woCreateNewService.getWOTypes().then(
                function (resp) {
                    if (resp.data) {
                        // Neu co quyen tao wo doanh thu thi them
                        if ($scope.permissions.createWODOANHTHU) {
                            resp.data.filter(function (value) {
                                if (value.woTypeCode == 'DOANHTHU') {
                                    $scope.woTypes.push(value);
                                }
                            });
                        }

                        // Neu co quyen tao wo HCQT thi them
                        if ($scope.permissions.createWOHcqt) {
                            resp.data.filter(function (value) {
                                if (value.woTypeCode == 'HCQT') {
                                    $scope.woTypes.push(value);
                                }
                            });
                        }

                        if ($scope.workingWO.cdLevel1 == $scope.vhktSysGroupId) {
                            resp.data.filter(function (value) {
                                if (value.woTypeCode == 'XLSC' || value.woTypeCode == 'UCTT'|| value.woTypeCode == 'Codien_HTCT' || value.woTypeCode == 'BDMPD') {
                                    pushToList(value);
                                } else if (value.woTypeCode == 'HSHC' && $scope.permissions.createdWoHshc) {
                                    pushToList(value);
                                }
                            });
                        } else if ($scope.permissions.crudWOCnkt) {
                            resp.data.filter(function (value) {
                                if (value.woTypeCode == 'QUYLUONG' || value.woTypeCode == 'THICONG' || value.woTypeCode == 'HSHC' || value.woTypeCode == 'THDT' || value.woTypeCode == 'TMBTHTTC') {
                                    pushToList(value);
                                }
                            });
                        } else if ($scope.workingWO.cdLevel1 == $scope.xdddSysGroupId) {
                            resp.data.filter(function (value) {
                                if (value.woTypeCode == 'DOANHTHU' || value.woTypeCode == 'THICONG' || value.woTypeCode == 'HSHC') {
                                    pushToList(value);
                                }
                            });
                        } else if ($scope.workingWO.cdLevel1 == $scope.gpthSysGroupId || $scope.workingWO.cdLevel1 == $scope.xddthtSysGroupId) {
                            resp.data.filter(function (value) {
                                if (value.woTypeCode == 'DOANHTHU') {
                                    pushToList(value);
                                }
                            });
                        } else {
                            resp.data.filter(function (value) {
                                if (value.woTypeCode != 'XLSC' && value.woTypeCode != 'BDDK' && value.woTypeCode != 'UCTT' && value.woTypeCode != 'DOANHTHU' && value.woTypeCode != 'HCQT') {
                                    pushToList(value);
                                }
                            });
                        }

                        // if (!$scope.permissions.createWO && !$scope.permissions.crudWOCnkt) $scope.woTypes = $scope.woTypes.filter(function (value) {
                        //     return (value.woTypeCode == 'DOANHTHU')
                        // });

                        // Unikom - create wo UCTT, HSHC -> fill san loai wo va disable
                        if ($scope.permissions.createdWoUctt) {
                            for (var i = 0; i < $scope.woTypes.length; i++) {
                                if ($scope.woTypes[i].woTypeCode == 'UCTT') {
                                    $scope.workingWO.woTypeId = $scope.woTypes[i].woTypeId;
                                    $scope.workingWO.woTypeCode = 'UCTT';
                                    $scope.selectedWoType = $scope.woTypes[i];
                                    break;
                                }
                            }
                        }

                        if ($scope.permissions.createdWoHshc) {
                            for (var i = 0; i < $scope.woTypes.length; i++) {
                                if ($scope.woTypes[i].woTypeCode == 'HSHC') {
                                    $scope.workingWO.woTypeId = $scope.woTypes[i].woTypeId;
                                    $scope.workingWO.woTypeCode = 'HSHC';
                                    $scope.selectedWoType = $scope.woTypes[i];
                                    break;
                                }
                            }
                        }

                        var obj = {page: 1, pageSize: 99999, woTypeId: $scope.workingWO.woTypeId};
                        Restangular.all("woService/woName/doSearch").post(obj).then(
                            function (resp) {
                                if (resp && resp.data) {
                                    $scope.woNames = resp.data;
                                }
                            },
                            function (error) {
                                toastr.success("Có lỗi xảy ra!");
                            }
                        );
                        // Unikom - create wo UCTT, HSHC
                        if (callback) callback();
                    }
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function pushToList(element) {
            if ($scope.woTypes.indexOf(element) === -1) {
                $scope.woTypes.push(element);
            }
        }

        $scope.finished = function () {
            var slb = $('#woTypeSelectBox');
            console.log(slb[0].children)
            for (var i = 0; i < slb[0].children.length; i++) {
                console.log(slb[0].children[i].getAttribute("value"))
                console.log($scope.workingWO.woTypeId)
                if (slb[0].children[i].value == $scope.workingWO.woTypeId) slb[0].children[i].selected = true;
            }
            console.log(slb[0].children)
        };

        function getAppWorkSrcs() {
            woCreateNewService.getAppWorkSrcs().then(
                function (resp) {
                    //console.log(resp);
                    if (resp.data) {
                        if ($scope.permissions.crudWOCnkt) $scope.apWorkSrcs = resp.data.filter(function (item) {
                            return item.name == 'XDDD - Xây dựng dân dụng';
                        })
                        else {
                            $scope.apWorkSrcs = resp.data;
                        }
                    }

                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
            woCreateNewService.getAppWorkSource().then(
                function (resp) {
                    //console.log(resp);
                    if (resp.data) {
                    	
                        if ($scope.permissions.crudWOCnkt) $scope.apWorkSrcs1 = resp.data.filter(function (item) {
                            return item.name == 'XDDD - Xây dựng dân dụng';
                        })
                        else {
                            $scope.apWorkSrcs1 = resp.data;
                        }
                    }

                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function getAppConstructionTypes() {
            woCreateNewService.getAppConstructionTypes().then(
                function (resp) {
                    console.log(resp);
                    if (resp.data) $scope.apConstructionTypes = resp.data;

                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function getCdLv2List() {
            var postData = {loggedInUser: $scope.loggedInUser};

            if ($scope.workingWO.cdLevel1 == $scope.vhktSysGroupId) {
                woCreateNewService.getVhktCdLv2VList(postData).then(
                    function (resp) {
                        if (resp.data) $scope.cdLv2List = resp.data;
                        // Unikom - create wo UCTT, HSHC of VHKT - Auto fill cd level 2
                        if ($scope.permissions.createdWoUctt || $scope.permissions.createdWoHshc) {
                            for (var i = 0; i < $scope.cdLv2List.length; i++) {
                                if ($scope.cdLv2List[i].groupNameLv2 != null && $scope.cdLv2List[i].groupNameLv2 == $scope.sysUserGroup.groupNameLevel2) {
                                    $scope.workingWO.cdLevel2 = $scope.cdLv2List[i].sysGroupId;
                                    $scope.workingWO.cdLevel2Name = $scope.cdLv2List[i].groupNameLv2;
                                    $scope.getCdLv3List();
                                    break;
                                }
                            }
                        }
                        // Unikom - create wo UCTT, HSHC of VHKT
                    },
                    function (error) {
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            } else {
                woCreateNewService.getCdLv2List(postData).then(
                    function (resp) {
                        if (resp.data) $scope.cdLv2List = resp.data;
                    },
                    function (error) {
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            }
        }

        $scope.getCdLv3List = function () {
            if (!$scope.workingWO.cdLevel2 || $scope.workingWO.cdLevel2 == '') {
                $scope.cdLv3List = {};
                $scope.workingWO.cdLevel3 = null;
                return;
            }

            var postData = {cdLevel2: $scope.workingWO.cdLevel2}
            woCreateNewService.getCdLv3List(postData).then(
                function (resp) {
                    console.log(resp);
                    if (resp.data) $scope.cdLv3List = resp.data;
                    // Unikom - create wo UCTT
                    // Auto fill cd level 3
                    if ($scope.permissions.createdWoUctt) {
                        for (var i = 0; i < $scope.cdLv3List.length; i++) {
                            if ($scope.cdLv3List[i].groupNameLv3 != null && $scope.cdLv3List[i].groupNameLv3 == $scope.sysUserGroup.groupNameLevel3) {
                                $scope.workingWO.cdLevel3 = $scope.cdLv3List[i].sysGroupId;
                                $scope.workingWO.cdLevel3Name = $scope.cdLv3List[i].groupNameLv3;
                                break;
                            }
                        }
                    } else if ($scope.permissions.createdWoHshc) {
                        $scope.workingWO.cdLevel2 = $scope.cdLv3List[0].sysGroupId;
                        $scope.workingWO.cdLevel2Name = $scope.cdLv3List[0].groupNameLv3;
                        $scope.workingWO.cdLevel3 = $scope.cdLv3List[0].sysGroupId;
                        $scope.workingWO.cdLevel3Name = $scope.cdLv3List[0].groupNameLv3;
                    }
                    // Unikom - create wo UCTT
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        };

        $scope.getFtList = function (level) {

            if (($scope.workingWO.cdLevel2 == null || $scope.workingWO.cdLevel2 == '') && level == 2) {
                $scope.ftList = {};
                $scope.workingWO.ftId = null;
                return;
            }

            if (($scope.workingWO.cdLevel3 == null || $scope.workingWO.cdLevel3 == '') && level == 3) {
                $scope.ftList = {};
                $scope.workingWO.ftId = null;
                return;
            }

            var postData = {}
            if (level == 2) {
                postData = {cdLevel2: $scope.workingWO.cdLevel2}
            } else if (level == 3) {
                postData = {cdLevel3: $scope.workingWO.cdLevel3}
            }

            woCreateNewService.getFtList(postData).then(
                function (resp) {
                    console.log(resp);
                    if (resp.data) $scope.ftList = resp.data;

                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        };

        // $scope.getCatWorkTypesByConstruction = getCatWorkTypesByConstruction;
        // function getCatWorkTypesByConstruction() {
        //     if(!$scope.workingWO.constructionId || $scope.workingWO.constructionId == '') return;
        //
        //     var obj = {constructionId: $scope.workingWO.constructionId}
        //     woCreateNewService.getCatWorkTypesByConstruction(obj).then(
        //         function (resp) {
        //             if (resp.data) $scope.workItemTypes = resp.data;
        //
        //         },
        //         function (error) {
        //             toastr.error("Có lỗi xảy ra!");
        //         }
        //     )
        // }

        function getAvailableTR() {
            var obj = {loggedInUser: $scope.loggedInUser};
            woCreateNewService.getAvailableTR(obj).then(
                function (resp) {
                    console.log(resp);
                    if (resp.data) $scope.trList = resp.data;

                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        $scope.autoCompleteContractOptions = {
            dataTextField: "code", placeholder: "Chọn hợp đồng",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                $scope.workingWO.contractId = data.contractId;
                $scope.workingWO.contractCode = data.contractCode;
                $scope.workingWO.constructionCode = null;
                $scope.workingWO.constructionId = null;
                $scope.workingWO.catConstructionTypeId = null;
                $scope.workingWO.catWorkItemTypeId = null;
                $scope.workingWO.stationCode = null;
                //getConstructionByContract();
                if ($scope.workingWO.woTypeCode == 'HCQT') $scope.workingWO.hcqtContractCode = data.contractCode;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = $scope.workingWO.contractCode;

                        if (keySearch == '') {
                            $scope.workingWO.contractCode = null;
                        }
                        $scope.workingWO.contractId = null;
                        $scope.workingWO.constructionCode = null;
                        $scope.workingWO.constructionId = null;
                        $scope.workingWO.catConstructionTypeId = null;
                        $scope.workingWO.catWorkItemTypeId = null;
                        $scope.workingWO.catWorkItemTypeName = null;
                        $scope.workingWO.stationCode = null;
                        $scope.workingWO.hcqtContractCode = null;
                        $scope.workingWO.cnkv = null;
                        $scope.workingWO.catProvinceCode = null;
                        $scope.workingWO.hcqtContractCode = null;

                        var objSearch = {
                            keySearch: keySearch,
                            page: 1,
                            pageSize: 20,
                            contractFilter: $scope.workingWO.woTypeCode,
                            apWorkSrc: $scope.workingWO.apWorkSrc, //Huypq-20052021-add
                            trCode: $scope.workingWO.trCode  //Huypq-20052021-add
                        };

                        return Restangular.all("trService/tr/doSearchContracts").post(objSearch).then(
                            function (response) {
                                options.success(response.data);
                            }
                        ).catch(
                            function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            }
                        );
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-12 text-header-auto">Chọn hợp đồng</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.contractCode #</div></div>',
            change: function (e) {
            	if($scope.workingWO.contractId==null){
            		$scope.workingWO.contractCode = null;
            	}
            },
            close: function (e) {
            	if($scope.workingWO.contractId==null){
            		$scope.workingWO.contractCode = null;
            	}
            }
        };

        $scope.blurContract = function(param){
        	if(param='contract'){
        		if($scope.workingWO.contractId==null){
            		$scope.workingWO.contractCode = null;
            	}
        	}
        	
        	if(param='construction'){
        		if($scope.workingWO.constructionId==null){
            		$scope.workingWO.constructionCode = null;
            	}
        	}
        	
        }
        
        function getConstructionByContract() {
            if ($scope.workingWO.contractCode == null || $scope.workingWO.contractCode == '') return;
            var obj = {contractCode: $scope.workingWO.contractCode}

            woCreateNewService.getConstructionByContract(obj).then(
                function (resp) {
                    if (resp && resp.data) $scope.constructions = resp.data;
                },
                function (error) {
                }
            )
        };

        function getConstructionByProject() {
            if ($scope.workingWO.projectCode == null || $scope.workingWO.projectCode == '') return;
            var obj = {projectCode: $scope.workingWO.projectCode}

            woCreateNewService.getConstructionByProject(obj).then(
                function (resp) {
                    if (resp && resp.data) $scope.constructions = resp.data;
                },
                function (error) {
                }
            )
        };

        function getStationId(id) {
            for (var i = 0; i < $scope.constructions.length; i++) {
                if ($scope.constructions[i].constructionId == id) {
                    $scope.workingWO.catConstructionTypeId = $scope.constructions[i].catConstructionTypeId;
                    return $scope.constructions[i].stationId;
                }
            }
            return null;
        }

        $scope.getStation = getStation;

        function getStation() {

            if ($scope.workingWO.constructionId == null || $scope.workingWO.constructionId == '') return;

            var stationId = getStationId($scope.workingWO.constructionId);

            if (stationId == null) return;

            var obj = {stationId: stationId}

            woCreateNewService.getStationById(obj).then(
                function (resp) {
                    if (resp && resp.data) {
                        console.log(resp.data)
                        $scope.workingWO.stationCode = resp.data.stationCode;
                    }
                },
                function (error) {
                }
            )
        }

        $scope.resetNewWOForm = function () {
            $scope.isTrSelected = false;
            $scope.isAIO = false;
            $scope.visibleThdt = false;
            ;
            $scope.selectedWoType = {};
            $scope.workingWO = {};
            $scope.workingWO.cdLevel1Name = $scope.cdLevel1Group.groupName;
            $scope.workingWO.cdLevel1 = $scope.cdLevel1Group.sysGroupId;

            if ($scope.permissions.crudWOCnkt) {
                $scope.workingWO.cdLevel2 = $scope.crudCnktCdLevel2;
                $scope.workingWO.cdLevel2Name = $scope.crudCnktCdLevel2Name;
            }

        };

        $scope.autoCompleteTrOptions = {
            dataTextField: "trCode", placeholder: "Thông tin TR",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                $scope.workingWO.trId = data.trId;
                $scope.workingWO.trCode = data.trCode;
                selectTR();
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = $scope.workingWO.trCode;
                        $scope.workingWO.trId = null;
                        if (keySearch == '') {
                            $scope.workingWO.trCode = null;
                            $scope.isTrSelected = false;
                            $scope.workingWO.trName = null;
                            $scope.workingWO.constructionId = null;
                            $scope.workingWO.catConstructionTypeId = null;
                            $scope.workingWO.stationCode = null;
                            $scope.workingWO.contractCode = null;
                            if ($scope.permissions.createWO) {
                                $scope.workingWO.cdLevel1 = $scope.permissions.createWODomainDataList.split(',')[0];
                                $scope.workingWO.cdLevel1Name = $scope.cdLevel1Group.groupName;
                            }
                            $scope.workingWO.stationCode = null;
                            $scope.selectedConstruction.constructionName = null;
                            $scope.label.contractInfo = null;
                            $scope.workingWO.trCode = null;
                            $scope.workingWO.contractId = null;
                        }
                        var objSearch = {
                            keySearch: keySearch,
                            loggedInUser: $scope.loggedInUser,
                            page: 1,
                            pageSize: 10
                        };

                        return Restangular.all("trService/tr/doSearchAvailable").post(objSearch).then(
                            function (response) {
                                options.success(response.data);
                            }
                        ).catch(
                            function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            }
                        );

                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-12 text-header-auto">Các TR</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.trCode #</div></div>',
            change: function (e) {
            },
            close: function (e) {
                // handle the event0
            }
        };

        $scope.validateCreateNew = validateWO;

        function validateWO() {
            var wo = $scope.workingWO;
            if (!wo.woTypeId) {
                toastr.error("Loại work order không được để trống.");
                return false;
            }
//         taotq start 23082021
			if (wo.woTypeId == 101 && wo.apWorkSrc == null) {
			    toastr.error("Nguồn việc không được để trống.");
			    return false;
			}
            /*if (!wo.apWorkSrc && $scope.selectedWoType.hasApWorkSrc == 1) {
                toastr.error("Nguồn việc không được để trống.");
                return false;
            }*/
//         taotq end 23082021
            if (!wo.woNameId) {
                toastr.error("Tên work order không được để trống.");
                return false;
            }

            if (!wo.apConstructionType && $scope.selectedWoType.hasConstruction == 1) {
                toastr.error("Loại công trình không được để trống.");
                return false;
            }

            // if(!wo.trId && $scope.permissions.crudWOCnkt){
            //     toastr.error("Mã TR không được để trống.");
            //     return false;
            // }
            if ($scope.permissions.crudWOCnkt) $scope.workingWO.type = 1;

            if (!wo.trId && wo.trCode && wo.trCode != '') {
                toastr.error("Mã TR không tồn tại, hoặc chưa chọn mã TR! ");
                return false;
            }

            if (!wo.contractId && wo.woTypeCode != 'HCQT' && wo.woTypeCode != 'TMBTHTTC' && !wo.trId) {
                toastr.error("Mã hợp đồng không được để trống hoặc không tồn tại.");
                return false;
            }
            
            if (wo.trId) {
                if (!wo.contractId && !wo.projectId) {
                    toastr.error("Mã hợp đồng/dự án không được để trống hoặc không tồn tại.");
                    return false;
                }
            }

            if (!wo.constructionId && $scope.selectedWoType.hasWorkItem == 1) {
                toastr.error("Mã công trình không được để trống hoặc không tồn tại.");
                return false;
            }

            if (!wo.constructionId && wo.woTypeCode != 'TMBTHTTC' && wo.woTypeCode != 'AIO' && wo.woTypeCode != 'THDT' && wo.woTypeCode != 'HCQT' && wo.woTypeCode != 'DOANHTHU') {
                $('#autoCompleteConstructionId').focus();
                toastr.error("Mã công trình không được để trống.");
                return false;
            }

            if (!wo.catWorkItemTypeId && $scope.selectedWoType.hasWorkItem == 1) { //Huypq-19022021-edit code a Phuong os
                toastr.error("Hạng mục không được để trống.");
                return false;
            }

            if (wo.moneyValue === null || wo.moneyValue === undefined || wo.moneyValue === '' || Number.isNaN(wo.moneyValue)) {
                if (wo.woTypeCode != 'THICONG' || wo.apWorkSrc != 6) {
                    toastr.error("Giá trị sản lượng không được để trống.");
                    return false;
                }
            }

            if (parseFloat(wo.moneyValue) < 0) {
                toastr.error("Giá trị sản lượng không được là số âm.");
                return false;
            }

            if (parseFloat(wo.moneyValue) > 100000000000) {
                toastr.error("Giá trị sản lượng không được lớn hơn 100 tỷ.");
                return false;
            }
            
            if (!wo.cdLevel2 && 'HCQT' != wo.woTypeCode && 'DOANHTHU' != wo.woTypeCode && wo.cdLevel1 != $scope.xdddSysGroupId) {
                toastr.error("CD level 2 không được để trống.");
                return false;
            }

            if (!wo.cdLevel5 && (('HSHC' == wo.woTypeCode || 'THICONG' == wo.woTypeCode) && wo.cdLevel1 == $scope.xdddSysGroupId) && $scope.assignBchCt) {
                toastr.error("CD level 5 không được để trống.");
                return false;
            }

            if (!wo.ftId && 'HCQT' == wo.woTypeCode) {
                toastr.error("FT không được để trống.");
                return false;
            }

            if (!wo.finishDate) {
                $('#inputFinishDate').focus();
                toastr.error("Hạn hoàn thành không được để trống.");
                return false;
            }

            if (!wo.qoutaTime) {
                $('#inputQuotaTime').focus();
                toastr.error("Định mức hoàn thành không được để trống.");
                return false;
            } else {
                if (wo.qoutaTime > 10000) {
                    toastr.error("Định mức hoàn thành không được quá 10000 giờ.");
                    return false;
                }
            }
            
            if ('XLSC' == wo.woTypeCode) {
            	if (!wo.description|| wo.description == '') {
            		wo.description = null; 
            	}else{
            		var content = wo.description;
                	var len = content.length;
                	if(len > 1000 ){
    	                toastr.error("Nội dung chi tiết không được lớn hơn 1000 ký tự.");
    	                return false;
                	}
            	}
            	
            }

            if (!wo.stationCode && $scope.selectedWoType.woTypeCode == 'TMBTHTTC') {
                $('#autoCompleteStationId').focus();
                toastr.error("Mã trạm không được để trống.");
                return false;
            }

            if (wo.woTypeCode == 'THDT') {
                if (!wo.moneyFlowBill || wo.moneyFlowBill == '') {
                    toastr.error("Số hóa đơn không được để trống.");
                    return false;
                }

                if (!wo.moneyFlowValue || wo.moneyFlowValue == '' || wo.moneyFlowValue == 0) {
                    toastr.error("Giá trị hóa đơn không được để trống.");
                    return false;
                }

                if (!wo.moneyFlowRequired || wo.moneyFlowRequired == '' || wo.moneyFlowRequired == 0) {
                    toastr.error("Giá trị phải thu không được để trống.");
                    return false;
                } else {
                    var moneyFlow = parseFloat(wo.moneyFlowValue);
                    var moneyRequired = parseFloat(wo.moneyFlowRequired);
                    if (moneyRequired > moneyFlow) {
                        toastr.error("Giá trị phải thu không được lớn hơn giá trị hóa đơn.");
                        return false;
                    }
                }

                if (!wo.moneyFlowContent || wo.moneyFlowContent == '') {
                    toastr.error("Nội dung thu tiền không được để trống.");
                    return false;
                }
            }

            if (wo.woTypeCode == 'HCQT') {
                if (!wo.hcqtProjectId) {
                    toastr.error("Dự án HCQT không được để trống.");
                    return false;
                }
//              taotq start 23082021

                /*if (!wo.hcqtContractCode) {
                    toastr.error("Hợp đồng HCQT không được để trống.");
                    return false;
                }*/
//              taotq end 23082021
                if (!wo.stationCode) {
                    toastr.error("Mã trạm không được để trống.");
                    return false;
                }

                if (!wo.hshcReceiveDate) {
                    toastr.error("Ngày nhận HSHC không được để trống.");
                    return false;
                }

                if (!wo.cnkv) {
                    toastr.error("Chi nhánh khu vực không được để trống.");
                    return false;
                }

                if (!wo.catProvinceCode) {
                    toastr.error("Mã tỉnh không được để trống.");
                    return false;
                }
            }

            if ($scope.workingWO.cdLevel1 == $scope.vhktSysGroupId) {
                if (wo.woTypeCode == 'BDDK') {
                    toastr.error("Không thể tạo! WO bảo dưỡng định kỳ sẽ được hệ thống tạo tự động.");
                    return false;
                }

                if (!wo.trId && wo.woTypeCode == 'XLSC') {
                    toastr.error("Mã TR không được để trống.");
                    return false;
                }
            }

            if (wo.woTypeCode == 'HSHC' && ($scope.constructionStatus != 5 && !$rootScope.hshcVhkt) && $scope.catConstructionTypeId != 8 && $scope.isCreateNew) {
                toastr.error("Công trình chưa hoàn thành. Không thể tạo WO Hồ sơ hoàn công! ");
                return false;
            }

            if (wo.woTypeCode == 'HSHC' && wo.apWorkSrc == 6) {
                if (!$scope.hshcChecklistItem || $scope.hshcChecklistItem.length == 0) {
                    toastr.error("Chưa chọn hạng mục và đâu việc XDDD, không thể tạo WO Hồ sơ hoàn công! ");
                    return false;
                } else {
                    $scope.workingWO.xdddHshcChecklistItem = $scope.hshcChecklistItem;
                }
            }

            if (wo.woTypeCode == 'UCTT') {
                if (!wo.description) {
                    toastr.error("Mô tả WO ứng cứu thông tin không được để trống! ");
                    return false;
                }

                if (!wo.vtnetWoCode) {
                    toastr.error("Mã WO GNOC không được để trống! ");
                    return false;
                }

                if (!wo.partner) {
                    toastr.error("Đối tác không được để trống! ");
                    return false;
                }
            }

            if (!CommonService.isAValidDate(wo.finishDate, true)) {
                toastr.error("Ngày tháng không hợp lệ hoặc nhỏ hơn ngày hiện tại! ");
                return false;
            }

            if (wo.woTypeCode == 'DOANHTHU' && !wo.emailTcTct) {
                toastr.error("Phải chọn người duyệt Tài chính Tổng công ty! ");
                return false;
            }

            return true;
        }

        $scope.apWorkSrcShortName = function (name) {
            return htmlCommonService.apWorkSrcShortName(name);
        };

        $scope.autoCompleteStationOptions = {
            dataTextField: "code", placeholder: "Chọn mã trạm",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                $scope.workingWO.stationCode = data.stationCode;
                $scope.workingWO.cnkv = data.areaCode;
                $scope.workingWO.catProvinceCode = data.catProvinceCode;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = $scope.workingWO.stationCode;

                        $scope.workingWO.cnkv = null;
                        $scope.workingWO.catProvinceCode = null;

                        var objSearch = {keySearch: keySearch};
                        if ($scope.workingWO.woTypeCode == 'HCQT') {
                            objSearch.contractId = $scope.workingWO.contractId;
                            return Restangular.all("trService/tr/doSearchStationByContract").post(objSearch).then(
                                function (response) {
                                    options.success(response.data);
                                }
                            ).catch(
                                function (err) {
                                    console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                                }
                            );
                        } else {
                            objSearch.contractId = null;
                            return Restangular.all("trService/tr/doSearchStations").post(objSearch).then(
                                function (response) {
                                    options.success(response.data);
                                }
                            ).catch(
                                function (err) {
                                    console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                                }
                            );
                        }
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-12 text-header-auto">Chọn mã trạm</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.stationCode #</div></div>',
            change: function (e) {
            },
            close: function (e) {
                // handle the event0
            }
        };

        $scope.autoCompleteConstructionOptions = {
            dataTextField: "code", placeholder: "Chọn mã công trình",
            open: function (e) {
            },
            select: function (e) {

                data = this.dataItem(e.item.index());
                $scope.workingWO.constructionCode = data.constructionCode;
                $scope.workingWO.constructionId = data.constructionId;
                $scope.catConstructionTypeId = data.catConstructionTypeId;
                $scope.constructionStatus = data.status;
                //Huypq-10082021-start
                $scope.workingWO.provinceName = data.provinceName;
                $scope.workingWO.sysGroupDistrictName = data.sysGroupDistrictName;
                //Huy-end
                $scope.workingWO.catWorkItemTypeId = null;
                $scope.workingWO.catWorkItemTypeName = null;
                $scope.workingWO.catWorkItemTypeCode = null;
                $scope.workingWO.catConstructionTypeId = data.catConstructionTypeId;
                getStation();
                getCatWorkTypes();
                if ($scope.workingWO.woTypeCode == 'HSHC') {
                    $scope.label.moneyValueMil = null;
                    $scope.workingWO.moneyValue = null;
                    if ($scope.catConstructionTypeId != 8) {
                        autoSuggestMoneyValue();
                        if (ctr.workItemDataTbl && ctr.workItemDataTbl.dataSource) ctr.workItemDataTbl.dataSource.read();
                        // fillDataTableWorkItem();
                    }
                }
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = $scope.workingWO.constructionCode;
                        $scope.workingWO.constructionId = null;
                        $scope.workingWO.catWorkItemTypeId = null;
                        $scope.workingWO.catWorkItemTypeName = null;
                        $scope.workingWO.catWorkItemTypeCode = null;
                        $scope.workingWO.stationCode = null;
                        $scope.catConstructionTypeId = null;
                        $scope.constructionStatus = null;
                        $scope.workingWO.provinceName = null;
                        $scope.workingWO.sysGroupDistrictName = null;

                        var obj = {keySearch: keySearch, contractCode: $scope.workingWO.contractCode};
                        return Restangular.all("trService/tr/getConstructionByContract").post(obj).then(
                            function (response) {
                                $scope.constructions = response.data;

                                if (!$scope.constructions) $scope.constructions = [];

                                //filter out construction type id = 8 if xddd
                                if ($scope.workingWO.apWorkSrc == 6) {
                                    $scope.constructions = $scope.constructions.filter(function (item) {
                                        return item.catConstructionTypeId == 8;
                                    })
                                } else {
                                    $scope.constructions = $scope.constructions.filter(function (item) {
                                        return item.catConstructionTypeId != 8;
                                    })
                                }

                                // options.success($scope.constructions);
                                options.success(response.data);
                            }
                        ).catch(
                            function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            }
                        );
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-12 text-header-auto">Chọn công trình</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.constructionCode #</div></div>',
            change: function (e) {
            },
            close: function (e) {
                // handle the event0
            }
        };

        function autoSuggestMoneyValue() {
            if (!$scope.workingWO.constructionId || $scope.workingWO.constructionId == '') {
                $scope.label.moneyValueMil = null;
                $scope.workingWO.moneyValue = null;
                return;
            }

            woCreateNewService.autoSuggestMoneyValue({constructionId: $scope.workingWO.constructionId}).then(
                function (resp) {
                    if (resp.data) {
                        $scope.workingWO.moneyValue = resp.data;
                        $scope.label.moneyValueMil = resp.data / 1000000;
                    }
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function searchConsInCt(keySearch) {
            var searchResult = [];
            if (!$scope.constructions) return searchResult;

            for (var i = 0; i < $scope.constructions.length; i++) {
                var cons = $scope.constructions[i];
                keySearch = keySearch.toUpperCase();
                var code = cons.constructionCode;
                if (code.includes(keySearch)) searchResult.push(cons);
            }
            return searchResult;
        }

        $scope.autoCompleteCdLevel2Options = {
            dataTextField: "code", placeholder: "Chọn cd level 2",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                $scope.workingWO.cdLevel2Name = data.groupName;
                $scope.workingWO.cdLevel2 = data.sysGroupId;
                if ($scope.workingWO.cdLevel1 == $scope.vhktSysGroupId) {
                    $scope.workingWO.cdLevel3Name = data.groupName;
                    $scope.workingWO.cdLevel3 = data.sysGroupId;
                }
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = $scope.workingWO.cdLevel2Name;
                        $scope.workingWO.cdLevel2 = null;
                        //HienLT56 start 30102020_comment
//                        if(keySearch == ''){
//                            $scope.workingWO.cdLevel2Name = null;
//                            $scope.workingWO.cdLevel2 = null;
//                            return options.success([]);
//                        }
                        //HienLT56 end 30102020
                        return options.success(searchCdLevel2(keySearch));
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-12 text-header-auto">Chọn CD level 2</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.groupName #</div></div>',
            change: function (e) {
            },
            close: function (e) {
                // handle the event0
            }
        };

        function searchCdLevel2(keySearch) {
            var searchResult = [];
            for (var i = 0; i < $scope.cdLv2List.length; i++) {
                var lv2 = $scope.cdLv2List[i];
                //HienLT56 start 30102020
                var groupName = lv2.groupName.toUpperCase();
                if (keySearch == null) {
                    $scope.workingWO.cdLevel2Name = null;
                    $scope.workingWO.cdLevel2 = null;
                    searchResult = [];
                } else if (keySearch == "") {
                    searchResult.push(lv2);
                } else {
                    keySearch = keySearch.toUpperCase();
                    if (groupName.includes(keySearch)) searchResult.push(lv2);
                }
                //HienLT56 end 30102020
            }
            return searchResult;
        }

        function getCatWorkTypes() {
            if (!$scope.workingWO.catConstructionTypeId || $scope.workingWO.catConstructionTypeId == '') return;

            var obj = {catConstructionTypeId: $scope.workingWO.catConstructionTypeId}
            woCreateNewService.getCatWorkTypes(obj).then(
                function (resp) {
                    if (resp.data) $scope.workItemTypes = resp.data;
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        $scope.selectCatWorkItemType = function () {
            if (!$scope.workingWO.catWorkItemTypeId || $scope.workingWO.catWorkItemTypeId == null) return;

            for (var i = 0; i < $scope.workItemTypes.length; i++) {
                var item = $scope.workItemTypes[i];
                if (item.catWorkItemTypeId == $scope.workingWO.catWorkItemTypeId) {
                    $scope.workingWO.catWorkItemTypeName = item.name;
                    $scope.workingWO.catWorkItemTypeCode = item.catWorkItemTypeCode;
                }
            }
        };

        $scope.autoCompleteWorkItemOptions = {
            dataTextField: "code", placeholder: "Chọn hạng mục",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                $scope.workingWO.catWorkItemTypeName = data.name;
                $scope.workingWO.catWorkItemTypeCode = data.catWorkItemTypeCode;
                $scope.workingWO.catWorkItemTypeId = data.catWorkItemTypeId;
                $scope.$apply();

                if ($scope.catConstructionTypeId == 8 && ctr.xdddChecklistDataTbl && ctr.xdddChecklistDataTbl.dataSource) {
                    ctr.xdddChecklistDataTbl.dataSource.read();
                }
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = $scope.workingWO.catWorkItemTypeName;
                        $scope.workingWO.moneyValue = 0;
                        $scope.label.moneyValueMil = 0;
                        $scope.workingWO.catWorkItemTypeId = null;
                        $scope.hshcChecklistItem = [];
                        if (keySearch == '') {
                            $scope.workingWO.catWorkItemTypeName = null;
                            return options.success([]);
                        }

                        return options.success(searchWorkItem(keySearch));
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-12 text-header-auto">Chọn hạng mục</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.name #</div></div>',
            change: function (e) {
            },
            close: function (e) {
                // handle the event0
            }
        };

        $scope.autoCompleteFTOptions = {
            dataTextField: "code", placeholder: "Chọn người thực hiện",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                $scope.workingWO.ftName = data.fullName;
                $scope.workingWO.ftId = data.ftId;
                $scope.workingWO.ftEmail = data.email;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = $scope.workingWO.ftName;
                        if (keySearch == '') {
                            $scope.workingWO.ftName = null;
                            $scope.workingWO.ftId = null;
                            $scope.workingWO.ftEmail = null;
                            return options.success([]);
                        }

                        var obj = {};
                        obj.keySearch = keySearch;
                        obj.sysGroupId = $scope.workingWO.cdLevel1; // parameter cdlv2 means search ft in all group has same parent of this group.

                        return Restangular.all("woService/wo/getFtListFromLv2SysGroup").post(obj).then(
                            function (response) {
                                $scope.ftList = response.data;
                                options.success(response.data);
                            }
                        ).catch(
                            function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            }
                        );
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-12 text-header-auto">Chọn người thực hiện</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.employeeCode # - #: data.fullName #</div></div>',
            change: function (e) {
            },
            close: function (e) {
                // handle the event0
            }
        };

        function searchWorkItem(keySearch) {
            if (!keySearch) return [];
            var searchResult = [];
            for (var i = 0; i < $scope.workItemTypes.length; i++) {
                var workItem = $scope.workItemTypes[i];
                keySearch = keySearch.toUpperCase();
                var name = workItem.name.toUpperCase();
                if (name.includes(keySearch)) searchResult.push(workItem);
            }
            return searchResult;
        }

        function mutiple1Mil(val) {
            if (val) {
                val = val * 1000000;
                val = val.toFixed(0);
            }
            return val;
        }

        $scope.setMoneyValue = function () {
            try {
                var money = parseFloat($scope.label.moneyValueMil);
                $scope.workingWO.moneyValue = mutiple1Mil(money)
            } catch (e) {
                $scope.workingWO.moneyValue = null;
            }
        };

        $scope.setMoneyFlowValue = function () {
            try {
                var money = parseFloat($scope.label.moneyFlowValueMil);
                $scope.workingWO.moneyFlowValue = mutiple1Mil(money)
            } catch (e) {
                $scope.workingWO.moneyFlowValue = null;
            }
        };

        $scope.setMoneyFlowRequired = function () {
            try {
                var money = parseFloat($scope.label.moneyFlowRequiredMil);
                $scope.label.moneyValueMil = money;
                $scope.workingWO.moneyFlowRequired = mutiple1Mil(money);
                $scope.workingWO.moneyValue = mutiple1Mil(money);
            } catch (e) {
                $scope.workingWO.moneyFlowRequired = null;
                $scope.label.moneyValueMil = null;
            }
        };

        var workItemRecord = 0;

        function fillDataTableWorkItem() {
            ctr.workItemDataTblOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                save: function () {
                    ctr.workItemDataTbl.dataSource.read();
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
                            //Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/workItem/getWorkItemByConstruction",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var searchOptions = {constructionId: $scope.workingWO.constructionId};
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
                //     {
                //     refresh: false,
                //     pageSizes: [10, 15, 20, 25],
                //     messages: {
                //         display: "{0}-{1} của {2} kết quả",
                //         itemsPerPage: "kết quả/trang",
                //         empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                //     }
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

        var xdddItemRecord = 0;

        function fillXdddChecklistDataTbl() {
            ctr.xdddChecklistDataTblOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                save: function () {
                    ctr.xdddChecklistDataTbl.dataSource.read();
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
                            //Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/xddd/getXdddChecklistByWorkItem",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var searchOptions = {
                                constructionId: $scope.workingWO.constructionId,
                                catWorkItemTypeId: $scope.workingWO.catWorkItemTypeId
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
                    {
                        title: "Chọn",
                        field: 'state',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:center;"},
                        type: 'text',
                        template: function (dataItem) {
                            var selectCbx = '<input type="checkbox" value="' + dataItem.id + '" class="xddd-checklist-item" ng-click="toggleXdddHshcItem($event,' + dataItem.id + ',' + dataItem.value + ')"/>';
                            if (dataItem.hshc && dataItem.hshc > 0) return "Đã có WO HSHC";
                            if (dataItem.confirm == 1) return selectCbx;

                            return '';
                        }
                    },
                ]
            });
        }

        $scope.toggleXdddHshcItem = toggleXdddHshcItem;

        function toggleXdddHshcItem(event, id, val) {

            if (event.target.checked == true) {
                $scope.hshcChecklistItem.push(id);
                $scope.workingWO.moneyValue += val;
            } else {
                var index = $scope.hshcChecklistItem.indexOf(id);
                if (index > -1) {
                    $scope.hshcChecklistItem.splice(index, 1);
                }
                $scope.workingWO.moneyValue -= val;
            }

            $scope.label.moneyValueMil = (parseInt($scope.workingWO.moneyValue) / 1000000).toFixed(3);

            //$scope.$apply();
        };

        var checklistIdNumber = 0;

        $scope.addXdddChecklistItem = function () {
            var newItem = $('#xddd_checklist_item').clone();
            newItem.attr('id', 'xddd_checklist_item_' + checklistIdNumber++);
            newItem.show();
            var compiled = $compile(newItem)($scope);
            $('#xddd_checklist').append(compiled);
        };

        $scope.removeXdddChecklistItem = function (e) {
            var delBtn = e.currentTarget;
            var itemCtn = delBtn.parentNode;
            var checklisItem = itemCtn.parentNode;
            $('#' + checklisItem.id).remove();
        };

        $scope.stackUpValue = function (e) {
            // var itemValue = e.currentTarget.value;
            // itemValue = itemValue* 1000000;
            // $scope.workingWO.moneyValue += itemValue;
        };
        $scope.autoCompleteCdLevel5Options = {
            dataTextField: "userCode", placeholder: "Chọn CD Level 5",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                $scope.workingWO.cdLevel5 = data.userId;
                $scope.workingWO.cdLevel5Name = data.userName;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = $scope.workingWO.cdLevel5Name;
                        var contractCode = $scope.workingWO.contractCode;
                        if (keySearch == '') {
                            $scope.workingWO.cdLevel5 = null;
                            $scope.workingWO.cdLevel5Name = null;
                        }
                        $scope.workingWO.cdLevel5 = null;
                        var objSearch = {keySearch: keySearch, contractCode: contractCode, page: 1, pageSize: 10};

                        return Restangular.all("trService/woConfigContract/getFtListByContract").post(objSearch).then(
                            function (response) {
                                options.success(response.data);
                            }
                        ).catch(
                            function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            }
                        );
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-12 text-header-auto">Nhân viên</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.userCode # - #: data.userName #</div></div>',
            change: function (e) {
            },
            close: function (e) {
            }
        };

        function getTcTctEmails() {
            woCreateNewService.getTcTctEmails().then(
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

        // Unikom_20210527_start
        ctr.onChangeAssignBchCt = function(){
            console.log($scope.assignBchCt)
            $scope.assignBchCt = !$scope.assignBchCt;
        };
        // Unikom_20210527_end
    }
})();
