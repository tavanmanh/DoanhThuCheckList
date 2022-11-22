(function () {
    'use strict';
    var controllerId = 'workItemDetailsController';

    angular.module('MetronicApp').controller(controllerId, trDetailsController, '$rootScope');

    function trDetailsController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                 kendoConfig, $kWindow, workItemCheckListManagementService, htmlCommonService, $modal,
                                 CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http) {

        var vm = this;
        // variables
        vm.breadcrumb = "Quản lý công việc định kỳ > Chi tiết công việc định kỳ";
        vm.workingWorkItem = {};
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
        vm.validateScheduleCheckListName = null;
        vm.validateScheduleCheckListCode = null;
        vm.workItemCode = $rootScope.viewDetailsWoCode;
        $scope.CurrentDate = new Date();
        vm.statusWorkItem = "";
        var seWorkItemId = $rootScope.scheduleWorkItemId;
        $rootScope.scheduleWorkItemId = null;
        init();

        function init() {
            getWorkItemDetails(seWorkItemId, true);
        }

        // function subWoList() {
        //     postal.subscribe({
        //         channel: "Tab",
        //         topic: "action",
        //         callback: function (data) {
        //             if (data.action == 'refresh') vm.dataWIListTable.dataSource.read();
        //         }
        //     });
        // }

        // function subTR() {
        //     postal.subscribe({
        //         channel: "Tab",
        //         topic: "action",
        //         callback: function (data) {
        //             if (data.action == 'refresh' &&
        //                 vm.workingTR.trId &&
        //                 data.trId != vm.workingTR.trId
        //             ) {
        //                 getTRDetails(data.trId, true);
        //             }
        //         }
        //     });
        // }

        // function getCdLevel1() {
        //     workItemDetailsService.getCdLevel1({}).then(
        //         function (resp) {
        //             if (resp && resp.data) {
        //                 $scope.cdLv1List = resp.data;
        //             }
        //         },
        //         function (error) {
        //             //console.log(error);
        //             toastr.success("Có lỗi xảy ra!");
        //         }
        //     )
        // }

        // function getSysUserGroup(trId) {
        //     var obj = {loggedInUser: $scope.loggedInUser}
        //         workItemDetailsService.getSysUserGroup(obj).then(
        //             function(resp){
        //                 console.log(resp);
        //                 if(resp && resp.data){
        //                     $scope.sysUserGroup = resp.data;
        //                     getTRDetails(trId);
        //                     if($scope.sysUserGroup.trCreator == true){
        //                         $scope.enable.removeAttachment = true;
        //                         $scope.role.isTrCreator = true;
        //                     }
        //                 }
        //             },
        //             function(error){
        //                 console.log(error);
        //                 toastr.success("Có lỗi xảy ra! Vui lòng thử lại sau.");
        //             }
        //         )
        //     }

        function checkIsAIO(code) {
            if (code && code.includes('AIO')) $scope.isAIO = true;
        }

        function getWorkItemDetails(id, refresh) {
            var obj = {
                woWorkItemId: id
            }
            workItemCheckListManagementService.getOneInfoWorkItem(obj).then(
                function (resp) {
                    if (resp && resp.data) {
                        vm.workingWorkItem = resp.data;
                        fillDataWOList();
                        vm.statusWorkItem = getWOStateText(vm.workingWorkItem.status);
                        if (refresh) {
                            vm.dataWIListTable.dataSource.read();
                            // vm.fileAttachments.dataSource.read();
                            // vm.trHistory.dataSource.read();
                        }
                    }
                    $rootScope.viewDetailsWoId = null;
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    $rootScope.viewDetailsWoId = null;
                }
            )
        }

        vm.deleteWICheckList = deleteWICheckList;

        function deleteWICheckList(param) {
            var split = param.split('-');
            var id = split[0]
            var idWI = split[1]

            var param = {
                scheduleCheckListId: id,
                scheduleWorkItemId: idWI
            }
            confirm('Bạn có muốn xoá đầu việc không?', function () {
                workItemCheckListManagementService.deleteWorkItemCheckList(param).then(function (res) {

                    if (res && res.statusCode == 1) toastr.success("Xóa dữ liệu thành công!");
                    else toastr.error(res.message);
                    vm.dataWIListTable.dataSource.read();
                }, function (errResponse) {
                    toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi xoá đầu việc!"));
                });
            })
        }

        var record = 0;

        function createActionTemplate(woItem) {
            var isEditable = false;
            var isDeleteable = false;

            // if(woItem.state == 'UNASSIGN' || woItem.state == 'ASSIGN_CD' || woItem.state == 'ACCEPT_CD' || woItem.state == 'ASSIGN_FT' || woItem.state == 'REJECT_FT'){
            //     if($scope.permissions.updateWO) isEditable = true;
            //     if($scope.permissions.deleteWO) isDeleteable = true;
            // }
            var  param = woItem.scheduleCheckListId + '-' + woItem.scheduleWorkItemId;
            var template = '<div class="display-block cedtpl" style="text-align: center">' +
                '<i class="fa fa-pencil icon-table" ng-click="vm.editWICheckList(' + woItem.scheduleCheckListId + ')"></i>' +
                '<i class="fa fa-trash-o icon-table" ng-click="vm.deleteWICheckList(\''+param+'\')"></i></div>'

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

        vm.editWICheckList = function (obj) {
            var param = {
                scheduleCheckListId: obj
            }
            workItemCheckListManagementService.getOneDetails(param).then(
                function (resp) {
                    if (resp && resp.data) $scope.wiCheckListForm = resp.data;

                    $modal.open({
                        templateUrl: 'coms/wo_xl/scheduleWorkItemDetails/scheduleWorkItemDetailsCreateEditModal.html',
                        controller: null,
                        windowClass: 'app-modal-window',
                        scope: $scope
                    })
                        .result.then(
                        function () {
                            vm.saveWorkItemCheckList($scope.wiCheckListForm);
                        },
                        function () {
                            $scope.wiCheckListForm = {};
                        }
                    )
                },
                function (error) {
                    toastr.success("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )

        }

        vm.saveWorkItemCheckList = function (obj) {
            var model = {
                scheduleCheckListId: obj.scheduleCheckListId,
                scheduleCheckListCode: obj.scheduleCheckListCode,
                scheduleCheckListName: obj.scheduleCheckListName,
                userCreated: obj.userCreated,
                createdDate: obj.createdDate,
                status: obj.status,
                scheduleWorkItemId: obj.scheduleWorkItemId,
            };

            workItemCheckListManagementService.updateWorkItemCheckList(model).then(
                function (resp) {
                    if (resp && resp.statusCode == 1) toastr.success("Sửa thành công!");
                    else toastr.error(resp.message);

                    vm.dataWIListTable.dataSource.read();
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
            return model;
        }

        vm.deleteRecodeTable = deleteRecodeTable;

        function deleteRecodeTable(id) {
            var param = {
                scheduleCheckListId: id
            }
            confirm('Bạn có muốn xoá loại công việc không?', function () {
                workItemCheckListManagementService.deleteWorkItemCheckList(param).then(function (res) {

                    if (res && res.statusCode == 1) toastr.success("Xóa dữ liệu thành công!");
                    else toastr.error(res.message);
                    vm.dataWIListTable.dataSource.read();
                }, function (errResponse) {
                    toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi xoá công việc định kỳ!"));
                });
            })
        }

        function valueOrEmptyStr(value) {
            return value ? value : '';
        }

        function fillDataWOList() {
            vm.dataWIListTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                save: function () {
                    vm.dataWIListTable.dataSource.read();
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
                            //Thuc hien viec goi service lay cong viec cua cong viec dinh ky
                            url: Constant.BASE_SERVICE_URL + "woService/wo/doSearchWICheckList",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var paramObj = {};
                            paramObj.page = options.page;
                            paramObj.pageSize = options.pageSize;
                            paramObj.woWorkItemId = seWorkItemId;
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
                        title: "Mã công việc",
                        field: 'scheduleCheckListCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.scheduleCheckListCode);
                        },
                    },
                    {
                        title: "Tên đầu việc",
                        field: 'scheduleCheckListName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.scheduleCheckListName);
                        },
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
                            return getWOStateText(dataItem.status);
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
            if (state === 1)
                return text = 'Hoạt động';
            else
                return text = 'Không hoạt động';
        }

        vm.openAssignmentModal = function () {
            var d = new Date();
            var t = d.getTime();
            $modal.open({
                templateUrl: 'coms/wo_xl/common/trAssignmentToCDModal.html?v=' + t,
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
        }

        vm.giveAssignment = function (selectedUnit) {

            if (vm.workingTR.cdLevel1 == selectedUnit.sysGroupId) {
                toastr.success("Không có sự thay đổi.");
                return;
            }

            var postData = {
                trId: vm.workingTR.trId,
                state: Constant.WO_XL_STATE.assignCd.stateCode, assignedCd:
                selectedUnit.sysGroupId,
                loggedInUser: $scope.loggedInUser,
            };

            workItemDetailsService.giveAssignment(postData).then(
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

        vm.add = function () {
            // $rootScope.trForNewWo = vm.workingTR;
            // var template = Constant.getTemplateUrl('WO_XL_WO_CREATE_NEW');
            // postal.publish({
            //     channel : "Tab",
            //     topic : "open",
            //     data : template
            // });

            $scope.wiCheckListForm = {};
            $modal.open({
                templateUrl: 'coms/wo_xl/scheduleWorkItemDetails/scheduleWorkItemDetailsCreateEditModal.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function () {
                    vm.createNewWorkItemCheckList($scope.wiCheckListForm);
                },
                function () {
                    $scope.wiCheckListForm = {};
                }
            )

        }

        function validateCreateSchedule(workingWorkItem) {
            if (!workingWorkItem.scheduleCheckListName) {
                vm.validateScheduleCheckListName = "Tên công việc không được phép bỏ trống";
            } else {
                vm.validateScheduleCheckListName = null;
            }
            if (!workingWorkItem.scheduleCheckListCode) {
                vm.validateScheduleCheckListCode = "Mã công việc không được phép bỏ trống";
            } else {
                vm.validateScheduleCheckListCode = null;
            }
        }

        $scope.validateCreateNew = function () {
            var workItempCheckList = $scope.wiCheckListForm;

            if (!workItempCheckList.scheduleCheckListName) {
                toastr.error("Tên công việc không được để trống.");
                return false;
            }

            if (!workItempCheckList.scheduleCheckListCode) {
                toastr.error("Mã công việc không được để trống");
                return false;
            }

            return true;
        }

        vm.createNewWorkItemCheckList = function (obj) {
            var model = {
                scheduleCheckListId: 0,
                scheduleCheckListName: obj.scheduleCheckListName,
                scheduleCheckListCode: obj.scheduleCheckListCode,
                userCreated: $scope.loggedInUser,
                createdDate: $scope.CurrentDate,
                status: 1,
                scheduleWorkItemId: seWorkItemId
            };
            validateCreateSchedule(obj);
            if (!vm.validateScheduleCheckListName && !vm.validateScheduleCheckListCode)
                workItemCheckListManagementService.createNewWorkItemCheckList(model).then(
                    function (resp) {
                        if (resp && resp.statusCode == 1) {
                            toastr.success("Thêm mới thành công!");
                            vm.dataWIListTable.dataSource.read();
                        } else toastr.error("Thêm mới thất bại! " + resp.message);

                    },
                    function (error) {
                        console.log(error);
                        toastr.success("Có lỗi xảy ra!");
                    }
                )
        }

        vm.changeStateTr = function (state, confirmTxt) {
            confirm(confirmTxt, function () {
                var param = {
                    state: state,
                    trId: vm.workingTR.trId,
                    text: $scope.rejectReason.text,
                    loggedInUser: $scope.loggedInUser
                }
                workItemDetailsService.changeStateTr(param).then(
                    function (res) {
                        if (res && res.statusCode == 1) {
                            toastr.success("Cập nhật thành công.");
                            getTRDetails(vm.workingTR.trId)
                            vm.trHistory.dataSource.read();
                            if (state == 'ACCEPT_CD' && $scope.isAIO == true && vm.workingTR.customerType == 1) createNewAIOWO();
                        } else toastr.error("Có lỗi xảy ra!");
                        getTRDetails(vm.workingTR.trId);
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
            workItemDetailsService.getAIOWoInfo(obj).then(
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
            workItemDetailsService.createNewWO(model).then(
                function (resp) {
                    console.log(resp);
                    if (resp && resp.statusCode == 1) {
                        toastr.success("Thêm mới công việc thành công!");
                        vm.dataWIListTable.dataSource.read();
                    } else toastr.error("Thêm mới công việc WO thất bại!");
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

// end controller
    }

})();