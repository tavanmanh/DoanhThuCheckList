(function () {
        'use strict';
        var controllerId = 'trDetailsController';

        angular.module('MetronicApp').controller(controllerId, trDetailsController, '$rootScope');

        function trDetailsController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                     kendoConfig, $kWindow, trDetailsService, htmlCommonService, $modal, vpsPermissionService,
                                     CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http) {

            var vm = this;
            // variables
            vm.breadcrumb = "Quản lý TR > Chi tiết TR";
            vm.workingTR = {};
            vm.searchForm = {};
            $scope.selectedUnit = {};
            $scope.label = {};
            $scope.enable = {};
            $scope.loggedInUser = $rootScope.casUser.userName;
            $scope.sysUserId = $rootScope.casUser.sysUserId;
            $scope.stateCanCreateNewWo = false;
            $scope.role = {};
            $scope.cdLv1List = {};
            $scope.isAIO = false;
            $scope.AIO_AP_WORK_SRC = 8;
            $scope.rejectReason = {};
            $scope.permissions = {};
            $scope.isDisable = {};
            $scope.isCdTr = false;
            vm.label = {};

            init();

            function init() {
                $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
                if ($rootScope.viewDetailsTrId) getSysUserGroup($rootScope.viewDetailsTrId);
                //loadTR();
                getCdLevel1();
                subWoList();
                subTR();
            }

            function subWoList() {
                postal.subscribe({
                    channel: "Tab",
                    topic: "action",
                    callback: function (data) {
                        if (data.action == 'refresh') vm.dataWOListTable.dataSource.read();
                    }
                });
            }

            function subTR() {
                postal.subscribe({
                    channel: "Tab",
                    topic: "action",
                    callback: function (data) {
                        if (data.action == 'refresh' && vm.workingTR.trId &&
                            data.trId != vm.workingTR.trId && data.trId != null
                        ) {
                            getTRDetails(data.trId, true);
                        }
                    }
                });
            }

            function getCdLevel1() {
                trDetailsService.getCdLevel1({}).then(
                    function (resp) {
                        if (resp && resp.data) {
                            $scope.cdLv1List = resp.data;
                        }
                    },
                    function (error) {
                        //console.log(error);
                        toastr.success("Có lỗi xảy ra!");
                    }
                )
            }

            function getSysUserGroup(trId) {
                var obj = {loggedInUser: $scope.loggedInUser}
                trDetailsService.getSysUserGroup(obj).then(
                    function (resp) {
                        console.log(resp);
                        if (resp && resp.data) {
                            $scope.sysUserGroup = resp.data;
                            getTRDetails(trId);
                            if ($scope.sysUserGroup.trCreator == true) {
                                $scope.enable.removeAttachment = true;
                                $scope.role.isTrCreator = true;
                            }
                        }
                    },
                    function (error) {
                        console.log(error);
                        toastr.success("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    }
                )
            }

            function checkIsAIO(code) {
                if (code && code.includes('AIO')) $scope.isAIO = true;
            }

            function getTRDetails(trId, refresh) {
                var obj = {trId: trId};
                trDetailsService.getOneTRDetails(obj).then(
                    function (resp) {
                        if (resp && resp.data) {
                            vm.workingTR = resp.data;
                            fillDataWOList();
                            fillInactiveWoDataTbl();
                            fillDataFileAttachments();
                            fillRentStationDataTbl();
                            checkIsAIO(vm.workingTR.trCode);
                            if ($scope.isAIO) {
                                fillAIOPackages();
                            }
                            fillTrHistory();
                            $scope.stateCanCreateNewWo = checkStateCanCreateNewWO(vm.workingTR);
                            $scope.label.cdLevel1Name = vm.workingTR.cdLevel1Name;
                            getTRStateText(vm.workingTR.state);
                            detectIsCdTr();

                            if (vm.workingTR.cdLevel2 == null) {
                                if ($scope.permissions.cdTrDomainDataList && $scope.permissions.cdTrDomainDataList.includes(vm.workingTR.cdLevel1)) {
                                    $scope.enable.createNewWo = true;
                                } else $scope.enable.createNewWo = false;
                            } else {
                                if ($scope.permissions.crudWOCnkt) $scope.enable.createNewWo = true;
                                else $scope.enable.createNewWo = false;
                            }

                            vm.label.quantityValueMil = vm.workingTR.quantityValue ? (vm.workingTR.quantityValue / 1000000).toFixed(3) : '';
                            if (refresh) {
                                vm.dataWOListTable.dataSource.read();
                                vm.fileAttachments.dataSource.read();
                                vm.trHistory.dataSource.read();
                                vm.inactiveWoListTbl.dataSource.read();
                                vm.rentStationListTbl.dataSource.read();
                            }
                        }
                        $rootScope.viewDetailsTrId = null;
                    },
                    function (error) {
                        console.log(error);
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                        $rootScope.viewDetailsTrId = null;
                    }
                )
            }

            function detectIsCdTr() {
                if (vm.workingTR.state == 'ASSIGN_CD' && $scope.permissions.cdTR) {
                    if (vm.workingTR.cdLevel2 != null) {
                        if ($scope.permissions.cdTrDomainDataList.includes(vm.workingTR.cdLevel2)
                            || vm.workingTR.trTypeCode == 'TR_THUE_MAT_BANG_TRAM' || vm.workingTR.trTypeCode == 'TR_DONG_BO_HA_TANG') {
                            $scope.isCdTr = true;
                        } else {
                            $scope.isCdTr = false;
                        }
                    } else {
                        if ($scope.permissions.cdTrDomainDataList.includes(vm.workingTR.cdLevel1)) {
                            $scope.isCdTr = true;
                        } else {
                            $scope.isCdTr = false;
                        }
                    }
                } else $scope.isCdTr = false;
                //debugger;
            }

            function getTRStateText(state) {
                var states = Constant.WO_TR_XL_STATE;
                Object.keys(states).forEach(function (key, index) {
                        if (states[key].stateCode == state) {
                            $scope.label.stateText = states[key].stateText;
                        }
                    }
                );
            }

            function checkStateCanCreateNewWO(tr) {
                if (tr.state == 'OK' || tr.state == 'UNASSIGN' || tr.state == 'ASSIGN_CD' || tr.state == 'REJECT_CD') return false;
                return true;
            }

            var record = 0;

            function createActionTemplate(woItem) {
                var isEditable = false;
                var isDeleteable = false;


                if (woItem.state == 'UNASSIGN' || woItem.state == 'ASSIGN_CD' || woItem.state == 'ACCEPT_CD' || woItem.state == 'ASSIGN_FT' || woItem.state == 'REJECT_FT') {
                    if ($scope.permissions.updateWO) isEditable = true;
                    if ($scope.permissions.deleteWO) isDeleteable = true;
                }


                var template = '<div class="display-block cedtpl" style="text-align: center">' +
                    '<i class="fa fa-list-alt icon-table" ng-click="vm.viewWODetails(' + woItem.woId + ')"></i>' +
                    '<i ng-if="' + isEditable + '" class="fa fa-pencil icon-table" ng-click="vm.editWO(' + woItem.woId + ')"></i>' +
                    '<i ng-if="' + isDeleteable + '" class="fa fa-trash-o icon-table" ng-click="vm.deleteWO(' + woItem.woId + ')"></i></div>'

                return template;
            }

            vm.viewWODetails = function (woId) {
                var template = Constant.getTemplateUrl('WO_XL_WO_DETAILS');
                $rootScope.viewDetailsWoId = woId;
                template.woId = woId;
                postal.publish({
                    channel: "Tab",
                    topic: "open",
                    data: template
                });

                postal.publish({
                    channel: "Tab",
                    topic: "action",
                    data: {action: 'refresh', woId: woId}
                });
            };


            vm.editWO = function (woId) {
                var obj = {woId: woId};
                trDetailsService.getOneWODetails(obj).then(
                    function (resp) {
                        console.log(resp);
                        $scope.workingWO = resp.data;
                        $scope.isEditting = true;
                        $scope.isCreateNew = false;
                        $scope.isDisable.trChoose = true;
                        $scope.label.contractInfo = $scope.workingWO.contractCode;
                        $scope.label.constructionCode = $scope.workingWO.constructionCode;
                        if ($scope.workingWO.apWorkSrc) $scope.workingWO.apWorkSrc = $scope.workingWO.apWorkSrc.toString();
                        if ($scope.workingWO.apConstructionType) $scope.workingWO.apConstructionType = $scope.workingWO.apConstructionType.toString();

                        $modal.open({
                            templateUrl: 'coms/wo_xl/woManagement/woEditModal.html',
                            controller: 'woCreateEditTemplateController',
                            windowClass: 'app-modal-window',
                            scope: $scope
                        })
                            .result.then(
                            function () {
                                vm.saveWO($scope.workingWO);
                            },
                            function () {
                                $scope.workingWO = {};
                            }
                        )
                    },
                    function (error) {
                        console.log(error);
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    }
                )

            }

            vm.saveWO = function (obj) {
                var model = vm.dtoToModel(obj);
                trDetailsService.updateWO(model).then(
                    function (resp) {
                        console.log(resp);

                        if (resp && resp.statusCode == 1) toastr.success("Sửa thành công!");
                        else toastr.error("Đã xóa hoặc không tồn tại!");

                        vm.dataWOListTable.dataSource.read();
                    },
                    function (error) {
                        console.log(error);
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            }

            vm.dtoToModel = function (obj) {
                var model = {}
                model.woId = obj.woId;
                model.trId = obj.trId;
//                model.woName = obj.woName;
                model.woNameId = obj.woNameId;//duonghv13-add 20092021
                model.woName = obj.woName;
                model.woCode = obj.woCode;
                model.apWorkSrc = obj.apWorkSrc;
                model.woTypeId = obj.woTypeId;
                model.constructionId = obj.constructionId;
                model.stationCode = obj.stationCode;
                model.quantityValue = obj.quantityValue;
                model.executeMethod = obj.executeMethod;
                model.state = obj.state;
                model.ftId = obj.ftId;
                model.finishDate = obj.finishDate;
                model.qoutaTime = obj.qoutaTime;
                model.executeLat = obj.executeLat;
                model.executeLong = obj.executeLong;
                model.userCreate = obj.userCreate;
                model.status = obj.status;
                //duonghv13-add 20092021
                model.moneyValue = obj.moneyValue;
                model.description = obj.description;
                model.totalMonthPlanId = obj.totalMonthPlanId;
                //duong-end
                return model;
            }

            vm.deleteWO = function (woId) {
                var obj = {woId: woId, loggedInUser: $scope.loggedInUser};
                confirm('Xác nhận xóa bản ghi đã chọn?',
                    function () {
                        trDetailsService.deleteWO(obj).then(
                            function (resp) {
                                console.log(resp);

                                if (resp && resp.statusCode == 1) toastr.success("Xóa thành công!");
                                else toastr.success("Đã xóa hoặc không tồn tại!");

                                vm.dataWOListTable.dataSource.read();
                            },
                            function (error) {
                                console.log(error);
                                toastr.error("Có lỗi xảy ra!");
                            }
                        )
                    }
                );
            };

            function valueOrEmptyStr(value) {
                return value ? value : '';
            }

            function fillDataWOList() {
                vm.dataWOListTableOptions = kendoConfig.getGridOptions({
                    autoBind: true,
                    scrollable: false,
                    resizable: true,
                    editable: false,
                    save: function () {
                        vm.dataWOListTable.dataSource.read();
                    },
                    dataBinding: function () {
                        record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    reorderable: true,
                    toolbar: null,
                    dataSource: {
                        serverPaging: true,
                        schema: {
                            total: function (response) {
                                // $("#appCount").text("" + response.total);
                                vm.count = response.total;
                                return response.total;
                            },
                            data: function (response) {
                                return response.data;
                            }
                        },
                        transport: {
                            read: {
                                //Thuc hien viec goi service
                                url: Constant.BASE_SERVICE_URL + "woService/wo/doSearch",
                                contentType: "application/json; charset=utf-8",
                                type: "POST"
                            },
                            parameterMap: function (options, type) {
                                var paramObj = {};
                                paramObj.page = options.page;
                                paramObj.pageSize = options.pageSize;
                                paramObj.trId = vm.workingTR.trId;
                                return JSON.stringify(paramObj);
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
                                return ++record;
                            },
                            width: '10%',
                            columnMenu: false,
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:center;"
                            },
                            type: 'text',
                        },
                        {
                            title: "Tên WO",
                            field: 'nameWo',
                            width: '20%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            type: 'text',
                            template: function (dataItem) {
                                return valueOrEmptyStr(dataItem.woName);
                            },
                        },
                        {
                            title: "Mã Wo",
                            field: 'codeWo',
                            width: '20%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            type: 'text',
                            template: function (dataItem) {
                                return valueOrEmptyStr(dataItem.woCode);
                            },
                        },
                        {
                            title: "Tên Tr",
                            field: "nameTr",
                            width: '20%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            type: 'text',
                            template: function (dataItem) {
                                return valueOrEmptyStr(dataItem.trName);
                            }
                        },
                        {
                            title: "Nguồn việc",
                            field: 'sourceWork',
                            width: '20%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            type: 'text',
                            template: function (dataItem) {
                                return htmlCommonService.apWorkSrcShortName(valueOrEmptyStr(dataItem.apWorkSrcName));
                            }
                        },
                        {
                            title: "Trạng thái",
                            field: 'status',
                            width: '20%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            type: 'text',
                            template: function (dataItem) {
                                return getWOStateText(dataItem.state);
                            }
                        },
                        {
                            title: "Ngày tạo",
                            field: 'createDate',
                            width: '20%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            type: 'text',
                            template: function (dataItem) {
                                return valueOrEmptyStr(dataItem.createdDate);
                            }
                        },
                        {
                            title: "Ngày hoàn thành",
                            field: 'finishDate',
                            width: '20%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            type: 'text',
                            template: function (dataItem) {
                                return valueOrEmptyStr(dataItem.finishDate);
                            }
                        },
                        {
                            title: "Thao tác",
                            field: 'dateComplete',
                            width: '20%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:center;"
                            },
                            type: 'text',
                            template: function (dataItem) {
                                return createActionTemplate(dataItem);
                            }
                        }
                    ]
                });
            }

            function getWOStateText(state) {
                var text = '';
                Object.keys(Constant.WO_XL_STATE).forEach(
                    function (key, index) {
                        if (Constant.WO_XL_STATE[key].stateCode == state) {
                            text = Constant.WO_XL_STATE[key].stateText;
                        }
                    }
                );
                return text;
            }

            vm.openAssignmentModal = function () {

                $modal.open({
                    templateUrl: 'coms/wo_xl/common/trAssignmentToCDModal.html?v=' + Math.floor(Math.random() * 100000000) + 1,
                    controller: null,
                    windowClass: 'app-modal-window',
                    scope: $scope
                })
                    .result.then(
                    function () {
                        //console.log($scope.selectedUnit)
                        vm.giveAssignment($scope.selectedUnit)
                    },
                    function () {

                    }
                )
            };

            vm.giveAssignment = function (selectedUnit) {

                if (vm.workingTR.cdLevel1 == selectedUnit.sysGroupId) {
                    toastr.success("Không có sự thay đổi.");
                    return;
                }

                var postData = {
                    trId: vm.workingTR.trId,
                    state: Constant.WO_XL_STATE.assignCd.stateCode,
                    assignedCd: selectedUnit.sysGroupId,
                    loggedInUser: $scope.loggedInUser,
                };

                trDetailsService.giveAssignment(postData).then(
                    function (resp) {
                        //console.log(resp);
                        if (resp && resp.message === "SUCCESS") {
                            toastr.success("Giao việc thành công.");
                            getTRDetails(vm.workingTR.trId)
                            vm.trHistory.dataSource.read();
                        }
                    },
                    function (error) {
                        console.log(error);
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    }
                )

            }


            var fileRecord = 0;

            function fillDataFileAttachments() {
                var createEditDeleteTemplate = '<div class="display-block cedtpl"><i class="fa fa-trash-o azure"></i></div>';
                vm.fileAttachmentsOptions = kendoConfig.getGridOptions({
                    autoBind: true,
                    scrollable: false,
                    resizable: true,
                    editable: false,
                    save: function () {
                        vm.fileAttachments.dataSource.read();
                    },
                    dataBinding: function () {
                        fileRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
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
                                //return [{key:'value'}]
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
                                var paramObj = {};
                                paramObj.page = options.page;
                                paramObj.pageSize = options.pageSize;
                                paramObj.trId = vm.workingTR.trId;
                                console.log(paramObj);
                                return JSON.stringify(paramObj);

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
                                return ++fileRecord;
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
                            field: 'constructionCode',
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
                            field: 'valueConstruction',
                            width: '15%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:right;"
                            },
                            type: 'number',
                            //format: '{0:n3}',
                            template: function (dataItem) {
                                return dataItem.userCreated;
                            },

                        },
                        {
                            title: "Ngày tải lên",
                            field: 'createdDate',
                            width: '15%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            type: 'text',
                            template: function (dataItem) {
                                return dataItem.createdDate;
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

            vm.addAttachment = function () {
                $("#trAttachmentFileId").unbind().click();
                $("#trAttachmentFileId").change(function () {
                    var selectedFile = $("#trAttachmentFileId")[0].files[0];
                    var formData = new FormData();
                    formData.append('multipartFile', selectedFile);

                    //send file through fileService
                    var uploadFileService = Constant.BASE_SERVICE_URL + 'fileService/uploadWoFileATTT?folder=' + Constant.UPLOAD_FOLDER_IMAGES;
                    trDetailsService.uploadAttachment(uploadFileService, formData)
                        .success(function (resp) {
                            console.log(resp);

                            if (resp[0]) {
                                var filePath = resp[0];
                                var fileName = selectedFile.name;
                                var mappingObj = {
                                    trId: vm.workingTR.trId,
                                    fileName: fileName,
                                    filePath: filePath,
                                    userCreated: $scope.loggedInUser,
                                    status: 1
                                }

                                trDetailsService.mappingAttachmentToTR(mappingObj).then(
                                    function (resp) {
                                        console.log(resp);
                                        toastr.success("Thêm mới thành công");

                                        vm.fileAttachments.dataSource.read();
                                    },
                                    function (error) {
                                        console.log(error);
                                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                                    }
                                )
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
                confirm('Bạn có muốn xóa file đính kèm này?', function () {
                    trDetailsService.removeAtachment({id: attId}).then(
                        function (resp) {
                            console.log(resp);
                            toastr.success("Xóa thành công");

                            vm.fileAttachments.dataSource.read();
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
                    trDetailsService.doSearchAtachment(obj).then(
                        function (resp) {
                            console.log(resp);
                            if (resp && resp.data) {
                                var filePath = resp.data[0].filePath;
                                var fileName = resp.data[0].fileName;
                                //send file through fileService
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

            vm.addMoreWO = function () {
                $rootScope.trForNewWo = vm.workingTR;
                var template = Constant.getTemplateUrl('WO_XL_WO_CREATE_NEW');
                postal.publish({
                    channel: "Tab",
                    topic: "open",
                    data: template
                });
            };

            function validateInactiveWoList() {
                for (var i = 0; i < vm.inactiveWoList.length; i++) {
                    if (vm.inactiveWoList[i].hmType == 1 && !vm.inactiveWoList[i].quantityValue) {
                        toastr.error('Giá trị hạng mục không được để trống');
                        return false;
                    }

                    if (vm.inactiveWoList[i].hmType == 2 && !vm.inactiveWoList[i].moneyValue) {
                        toastr.error('Giá trị hạng mục không được để trống');
                        return false;
                    }

                    if (!vm.inactiveWoList[i].qoutaTime) {
                        toastr.error('Định mức thời gian không được để trống');
                        return false;
                    }
                }

                return true;
            }

            vm.changeStateTr = function (state, confirmTxt) {
                // if (!validateInactiveWoList()) return;

                confirm(confirmTxt, function () {
                    var param = {
                        state: state,
                        trId: vm.workingTR.trId,
                        text: $scope.rejectReason.text,
                        loggedInUser: $scope.loggedInUser,
                        inactiveWoList: vm.inactiveWoList,
                    };

                    trDetailsService.changeStateTr(param).then(
                        function (res) {
                            if (res && res.statusCode == 1) {
                                toastr.success("Cập nhật thành công.");
                                if (state == 'ACCEPT_CD') {
                                    if ($scope.isAIO == true && vm.workingTR.customerType == 1) {
                                        createNewAIOWO();
                                    }
                                    // else if (vm.workingTR.trTypeCode == 'TR_THUE_MAT_BANG_TRAM') {
                                    //     var obj = {
                                    //         trId: vm.workingTR.trId,
                                    //         tmbtTargetDetail: vm.workingTR.tmbtTargetDetail,
                                    //         loggedInUser: $scope.loggedInUser
                                    //     }
                                    //     trDetailsService.createNewTmbtWO(obj).then(
                                    //         function (res) {
                                    //             console.log(res)
                                    //             if (res && res.statusCode == 1) {
                                    //                 fillDataWOList();
                                    //             } else {
                                    //                 toastr.error("Có lỗi xảy ra!");
                                    //             }
                                    //         }, function (error) {
                                    //             toastr.error("Có lỗi xảy ra!");
                                    //         }
                                    //     )
                                    // }
                                    // else if (vm.workingTR.trTypeCode == 'TR_DONG_BO_HA_TANG') {
                                    //     var obj = {trId: vm.workingTR.trId}
                                    //     trDetailsService.createNewDbhtWO(obj).then(
                                    //         function (res) {
                                    //             console.log(res)
                                    //             if (res && res.statusCode == 1) {
                                    //                 toastr.success("Thêm mới WO đồng bộ hạ tầng công !");
                                    //             } else {
                                    //                 toastr.error("Có lỗi xảy ra!");
                                    //             }
                                    //         }, function (error) {
                                    //             toastr.error("Có lỗi xảy ra!");
                                    //         }
                                    //     )
                                    // }
                                }
                            } else toastr.error("Có lỗi xảy ra!");
                            getTRDetails(vm.workingTR.trId, true);
                        }, function (error) {
                            toastr.error("Có lỗi xảy ra!");
                        }
                    )
                });
            }

            function formatDate(dateStr) {
                var splitted = dateStr.split('-');
                return splitted[2] + '/' + splitted[1] + '/' + splitted[0];
            }

            function createNewAIOWO() {
                var obj = {contractId: vm.workingTR.contractId}
                trDetailsService.getAIOWoInfo(obj).then(
                    function (res) {
                        console.log(res)
                        if (res && res.statusCode == 1) {
                            console.log(vm.workingTR)
                            var aioWoType = res.data.woTypeId;
                            var aioWoNameId = res.data.woNameId;
                            var moneyValue = vm.workingTR.quantityValue;
                            var cdLevel2 = res.data.cdLevel2;
                            var cdLevel2Name = res.data.cdLevel2Name;
                            var finishDate = vm.workingTR.finishDate;
                            var cdLevel1 = vm.workingTR.cdLevel1;
                            var cdLevel1Name = vm.workingTR.cdLevel1Name;
                            var woNameA = res.data.woName;
                            var trCodex = vm.workingTR.trCode;
                            var model = {
                                trId: vm.workingTR.trId,
                                apWorkSrc: $scope.AIO_AP_WORK_SRC,
                                apConstructionType: 6,
                                woNameId: aioWoNameId,
                                woTypeId: aioWoType,
                                moneyValue: moneyValue,
                                finishDate: finishDate,
                                trCode: trCodex,
                                woName: woNameA,
                                catWorkItemTypeId: -9999, //negative means AIO contract
                                contractId: vm.workingTR.contractId,
                                userCreated: $scope.loggedInUser,
                                cdLevel1: cdLevel1,
                                cdLevel1Name: cdLevel1Name,
                                cdLevel2: cdLevel2,
                                cdLevel2Name: cdLevel2Name,
                                loggedInUser: $scope.loggedInUser,
                                qoutaTime: vm.workingTR.qoutaTime,
                                contractCode: vm.workingTR.contractCode,
                                createdUserFullName: $rootScope.casUser.fullName,
                                createdUserEmail: $rootScope.casUser.email,
                            }

                            saveAIOWo(model);
                        } else toastr.error("Có lỗi xảy ra!");
                    }, function (error) {
                        toastr.error("Có lỗi xảy ra!");
                    }
                )

            }

            function saveAIOWo(model) {
                trDetailsService.createNewWO(model).then(
                    function (resp) {
                        console.log(resp);
                        if (resp && resp.statusCode == 1) {
                            toastr.success("Thêm mới AIO WO thành công!");
                            vm.dataWOListTable.dataSource.read();
                        } else toastr.error("Thêm mới AIO WO thất bại!");
                    },
                    function (error) {
                        console.log(error);
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            }

            $scope.openRejectModal = function () {
                $scope.rejectReason.text = '';
                $scope.label.headerText = "Từ chối TR";
                $modal.open({
                    templateUrl: 'coms/wo_xl/common/rejectModal.html',
                    controller: null,
                    windowClass: 'app-modal-window',
                    scope: $scope
                })
                    .result.then(
                    function () {
                        vm.changeStateTr('REJECT_CD', 'Bạn có muốn từ chối TR? Lý do: ' + $scope.rejectReason.text);
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
            }


            var aioPackageRecord = 0;

            function fillAIOPackages() {

                vm.aioPackageOptions = kendoConfig.getGridOptions({
                    autoBind: true,
                    scrollable: false,
                    resizable: true,
                    editable: false,
                    save: function () {
                        vm.aioPackages.dataSource.read();
                    },
                    dataBinding: function () {
                        aioPackageRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    reorderable: true,
                    toolbar: null,
                    dataSource: {
                        serverPaging: true,
                        schema: {
                            total: function (response) {
                                // $("#appCount").text("" + response.total);
                                // vm.count = response.total;
                                return response.total;
                            },
                            data: function (response) {
                                return response.data;
                                //return [{key:'value'}]
                            }
                        },
                        transport: {
                            read: {
                                // Thuc hien viec goi service
                                url: Constant.BASE_SERVICE_URL + "trService/aioPackages/getByContract",
                                contentType: "application/json; charset=utf-8",
                                type: "POST"
                            },
                            parameterMap: function (options, type) {
                                var paramObj = {};
                                paramObj.page = options.page;
                                paramObj.pageSize = options.pageSize;
                                paramObj.contractId = vm.workingTR.contractId;
                                return JSON.stringify(paramObj);

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
                                return ++aioPackageRecord;
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
                            title: "Tên gói",
                            field: 'name',
                            width: '30%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            type: 'text',
                            template: function (dataItem) {
                                return dataItem.name ? dataItem.name : '';
                            },
                        },
                        {
                            title: "Cty mua hàng/Tỉnh tự mua hàng",
                            field: 'isProvinceBoughtName',
                            width: '30%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            type: 'text',
                            template: function (dataItem) {
                                return dataItem.isProvinceBoughtName ? dataItem.isProvinceBoughtName : '';
                            },
                        },
                        {
                            title: "Số tiền/Tỉ lệ giao khoán",
                            field: 'assignmentName',
                            width: '30%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            type: 'text',
                            template: function (dataItem) {
                                return dataItem.assignmentName ? dataItem.assignmentName : '';
                            },
                        },
                    ]
                });
            }

            var trHistoryRecord = 0;

            function fillTrHistory() {

                vm.trHistoryOptions = kendoConfig.getGridOptions({
                    autoBind: true,
                    scrollable: false,
                    resizable: true,
                    editable: false,
                    save: function () {
                        vm.trHistory.dataSource.read();
                    },
                    dataBinding: function () {
                        trHistoryRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    reorderable: true,
                    toolbar: null,
                    dataSource: {
                        serverPaging: true,
                        schema: {
                            total: function (response) {
                                // $("#appCount").text("" + response.total);
                                // vm.count = response.total;
                                //return response.total;
                            },
                            data: function (response) {
                                return response.data;
                                //return [{key:'value'}]
                            }
                        },
                        transport: {
                            read: {
                                // Thuc hien viec goi service
                                url: Constant.BASE_SERVICE_URL + "trService/tr/getHistory",
                                contentType: "application/json; charset=utf-8",
                                type: "POST"
                            },
                            parameterMap: function (options, type) {
                                var paramObj = {};
                                paramObj.page = options.page;
                                paramObj.pageSize = options.pageSize;
                                paramObj.trId = vm.workingTR.trId;
                                return JSON.stringify(paramObj);

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
                                return ++trHistoryRecord;
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
                            field: 'logTimeStr',
                            width: '20%',
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
                            field: 'logContent',
                            width: '40%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            type: 'text',
                            template: function (dataItem) {
                                return dataItem.content;
                            },
                        },
                        {
                            title: "Người tạo",
                            field: 'logContent',
                            width: '30%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            type: 'text',
                            template: function (dataItem) {
                                return dataItem.userCreated;
                            },
                        },
                    ]
                });
            }

            function addHmType() {
                for (var i = 0; i < vm.inactiveWoList.length; i++) {
                    if (vm.inactiveWoList[i].quantityValue && vm.inactiveWoList[i].quantityValue != '') {
                        vm.inactiveWoList[i].hmType = 1; //theo độ dài
                    } else vm.inactiveWoList[i].hmType = 2; //theo sản lượng
                }
            }

            var inactiveWoRecord = 0;

            function fillInactiveWoDataTbl() {
                vm.inactiveWoListOptions = kendoConfig.getGridOptions({
                    autoBind: true,
                    scrollable: false,
                    resizable: true,
                    editable: false,
                    save: function () {
                        vm.inactiveWoListTbl.dataSource.read();
                    },
                    dataBinding: function () {
                        inactiveWoRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
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
                                vm.inactiveWoList = response.data;
                                addHmType();
                                return response.data;
                            }
                        },
                        transport: {
                            read: {
                                //Thuc hien viec goi service
                                url: Constant.BASE_SERVICE_URL + "trService/workItem/getInactiveWoList",
                                contentType: "application/json; charset=utf-8",
                                type: "POST"
                            },
                            parameterMap: function (options, type) {
                                var searchOptions = {
                                    trId: vm.workingTR.trId
                                };
                                return JSON.stringify(searchOptions)
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
                            title: "STT", field: "stt",
                            template: function () {
                                return ++inactiveWoRecord;
                            },
                            width: '5%',
                            columnMenu: false,
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {style: "text-align:center;"},
                            type: 'text',
                        },
                        {
                            title: "Tên hạng mục",
                            field: 'catWorkItemTypeName',
                            width: '30%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {style: "text-align:left;"},
                            type: 'text'
                        },
                        {
                            title: "Loại đơn vị tính",
                            field: 'hmTypeValue',
                            width: '15%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {style: "text-align:left;"},
                            type: 'text',
                            template: function (dataItem) {
                                if (!dataItem.quantityValue) return "Theo sản lượng (VND)";
                                else return "Theo độ dài";
                            }
                        },
                        {
                            title: "Giá trị",
                            field: 'hmValue',
                            width: '15%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {style: "text-align:left;"},
                            type: 'text',
                            template: function (dataItem) {
                                dataItem.valueLabel = dataItem.moneyValue;
                                if (dataItem.quantityValue) dataItem.valueLabel = dataItem.quantityValue;

                                var disabled = 'disabled';
                                if (vm.workingTR.state == 'ASSIGN_CD' && $scope.permissions.cdTrDomainDataList.includes(vm.workingTR.cdLevel1)) disabled = '';

                                return '<input ' + disabled + ' id="hmValue' + dataItem.woId + '" class="form-control" ng-model="dataItem.valueLabel" ng-change="vm.changeHmValue(' + dataItem.woId + ')" only-numbers/>'
                            }
                        },
                        {
                            title: "Định mức thời gian (số giờ)",
                            field: 'hmValue',
                            width: '20%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {style: "text-align:left;"},
                            type: 'text',
                            template: function (dataItem) {
                                var disabled = 'disabled';
                                if (vm.workingTR.state == 'ASSIGN_CD' && $scope.permissions.cdTrDomainDataList.includes(vm.workingTR.cdLevel1)) disabled = '';
                                return '<input ' + disabled + ' id="hmQuotaTime' + dataItem.woId + '" class="form-control" ng-model="dataItem.qoutaTime" ng-change="vm.changeHmQuotaTime(' + dataItem.woId + ')" only-numbers/>'
                            }
                        },
                        {
                            title: "Thao tác",
                            field: 'hmValue',
                            width: '10%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {style: "text-align:center;"},
                            type: 'text',
                            template: function (dataItem) {
                                var isDeleteable = false;
                                if (vm.workingTR.state == 'ASSIGN_CD' && $scope.permissions.cdTrDomainDataList.includes(vm.workingTR.cdLevel1)) isDeleteable = true;
                                return '<i ng-if="' + isDeleteable + '" class="fa fa-trash-o icon-table" ng-click="vm.deleteAutoWorkItem(' + dataItem.woId + ')"></i></div>';
                            }
                        },
                    ]
                });
            }

            vm.deleteAutoWorkItem = function (woId) {
                confirm('Bạn có muốn xóa hạng mục này?', function () {
                    trDetailsService.removeInactiveWo({woId: woId}).then(
                        function (resp) {
                            if (resp && resp.statusCode == 1) toastr.success('Thực hiện thành công!');
                            else toastr.error(resp.message);
                            if (vm.inactiveWoListTbl) vm.inactiveWoListTbl.dataSource.read();
                        },
                        function (error) {
                            toastr.error('Có lỗi xảy ra!');
                        }
                    )
                });
            };

            vm.changeHmQuotaTime = function (id) {
                var inputValue = $('#hmQuotaTime' + id).val();
                try {
                    for (var i = 0; i < vm.inactiveWoList.length; i++) {
                        if (vm.inactiveWoList[i].woId == id) vm.inactiveWoList[i].qoutaTime = inputValue;
                    }
                } catch (e) {
                    toastr.error('Có lỗi xảy ra');
                }
            };

            vm.changeHmValue = function (id) {
                var inputValue = $('#hmValue' + id).val();
                try {
                    for (var i = 0; i < vm.inactiveWoList.length; i++) {
                        if (vm.inactiveWoList[i].woId == id) {
                            if (vm.inactiveWoList[i].hmType == 1) vm.inactiveWoList[i].quantityValue = inputValue;
                            else vm.inactiveWoList[i].moneyValue = inputValue;
                        }
                    }
                } catch (e) {
                    toastr.error('Có lỗi xảy ra');
                }
            };

            var rentStationRecord = 0;

            function fillRentStationDataTbl() {
                vm.rentStationListOptions = kendoConfig.getGridOptions({
                    autoBind: true,
                    scrollable: true,
                    resizable: false,
                    save: function () {
                        vm.rentStationListTbl.dataSource.read();
                    },
                    dataBinding: function () {
                        rentStationRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    reorderable: true,
                    toolbar: null,
                    dataSource: {
                        serverPaging: false,
                        schema: {
                            data: function (response) {
                                vm.rentStationList = response.data;
                                return response.data;
                            }
                        },
                        transport: {
                            read: {
                                //Thuc hien viec goi service
                                url: Constant.BASE_SERVICE_URL + "trService/tmbt/getRentStation",
                                contentType: "application/json; charset=utf-8",
                                type: "POST"
                            },
                            parameterMap: function (options, type) {
                                var searchOptions = {
                                    trId: vm.workingTR.trId
                                };
                                searchOptions.page = 1;
                                searchOptions.pageSize = 9999;
                                return JSON.stringify(searchOptions)
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
                            editable: false,
                            template: function () {
                                return ++rentStationRecord;
                            },
                            width: '5%',
                            columnMenu: false,
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {style: "text-align:center;"},
                            type: 'text',
                        },
                        {
                            title: "Chi Nhánh",
                            field: 'branch',
                            editable: false,
                            width: '30%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {style: "text-align:left;"},
                            type: 'text',
                            template: function (dataItem) {
                                var branch = dataItem.branch
                                return branch ? branch : '';
                            }
                        },
                        {
                            title: "Chỉ Tiêu",
                            field: 'rentTarget',
                            editable: false,
                            width: '15%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {style: "text-align:right;font-weight: bold;"},
                            template: function (dataItem) {
                                var rentTarget = dataItem.rentTarget
                                return rentTarget ? rentTarget : '';
                            }
                        },
                        {
                            title: "Danh Sách Trạm",
                            field: 'rentDetail',
                            editable: false,
                            width: '50%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {style: "text-align:left;font-weight: bold;"},
                            template: function (dataItem) {
                                var rentDetail = dataItem.rentDetail
                                return rentDetail ? rentDetail : '';
                            }
                        }
                    ]
                });
            }

// end controller
        }
    }

)();
