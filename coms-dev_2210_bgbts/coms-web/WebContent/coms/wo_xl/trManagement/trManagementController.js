(function () {
    'use strict';
    var controllerId = 'trManagementController';

    angular.module('MetronicApp').controller(controllerId, trManagementController, '$scope', '$modal', '$rootScope');

    function trManagementController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                    kendoConfig, $kWindow, htmlCommonService, trManagementService, vpsPermissionService,
                                    CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modal) {

        var vm = this;
        // variables
        var record = 0;
        vm.breadcrumb = "Quản lý TR > Danh sách TR";
        vm.sectionHeader = "Chi tiết";
        vm.searchForm = {};
        $scope.loggedInUser = vm.searchForm.loggedInUser = $rootScope.casUser.userName;
        $scope.workingTR = {};
        $scope.selectedUnit = {};
        $scope.trTypes = [];
        $scope.cdLv2List = {};
        $scope.label = {};
        $scope.label.cdLevel1Name = 'Trung tâm hạ tầng';
        $scope.isCreateNew = true;
        $scope.fileList = [];
        $scope.cdLv1List = {};
        $scope.selectedCd = {};
        $scope.constructions = {};
        $scope.WOStates = {};
        vm.initDateField = {};
        $scope.contracts = {};
        $scope.ctOrPj = {};
        $scope.isCreateNew = true;
        $scope.role = {};
        $scope.role.trCreator = false;
        $scope.trStates = [
            {stateCode: 'UNASSIGN', stateText: 'Mới tạo'},
            {stateCode: 'ASSIGN_CD', stateText: 'Chờ CD tiếp nhận'},
            {stateCode: 'ACCEPT_CD', stateText: 'CD đã tiếp nhận'},
            {stateCode: 'REJECT_CD', stateText: 'CD đã từ chối'},
            {stateCode: 'PROCESSING', stateText: 'Đang thực hiện'},
            {stateCode: 'OPINION_RQ', stateText: 'Xin ý kiến'},
            {stateCode: 'DONE', stateText: 'Đã thực hiện'},
            {stateCode: 'OK', stateText: 'Đã hoàn thành'},
            {stateCode: 'NOK', stateText: 'Chưa hoàn thành'},
        ];

        $scope.permissions = {};
        var TTHT_SYS_GROUP_ID = '242656';
        var GPTH_SYS_GRPUP_ID = '280483';
        var XDDTHT_SYS_GROUP_ID = '166677';
        var VHKT_SYS_GROUP_ID = '270120';
        var TTXDD_SYS_GROUP_ID = '9006003';

        vm.catConstructionTypes = [];
        vm.catConstructionTypeId = null;
        vm.trBranch = null;
        vm.isAutoCreateWo = false;
        vm.workItemList = [];
        vm.selectedWorkItem = [];
        vm.inactiveWoList = [];

        vm.selectedStations = [];
        vm.lstStationCodes = '';

        init();

        function init() {
            $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
            //console.log($rootScope.casUser);
            checkIsAutoCreateWo();
            getGroup();
            getCdLevel1();
            fillDataTable();
            getTrTypes();
            getAppWorkSrcs();
            getStateText();
            getAvailableContracts();
            getAvailableProjects();
            getCatConstructionTypes();
            // getConstructionByContract();
            postal.subscribe({
                channel: "Tab",
                topic: "action",
                callback: function (data) {
                    if (data.action == 'refresh') vm.dataTRListTable.dataSource.read();
                }
            });
            $scope.ctOrPj.isContract = true;
            initDate();
        }

        function initDate() {
            var now = new Date();
            var startTime = now.getTime() - 90 * 24 * 60 * 60 * 1000;
            var startDate = new Date(startTime);
            vm.searchForm.createdDateFrom = htmlCommonService.formatDate(startDate);
            vm.searchForm.createdDateTo = htmlCommonService.formatDate(now);

        }

        function checkIsAutoCreateWo() {
            if ($scope.permissions.createTR && $scope.permissions.createTRDomainDataList.includes(XDDTHT_SYS_GROUP_ID)) {

                // Bỏ do xây dựng luồng DBHT mới -> mặc định sinh 7 WO
                // fillWorkItemDataTbl();

                vm.isAutoCreateWo = true;
                vm.trBranch = 3; // 3 là mã trụ trong hợp đồng của xd đt ht
            }
        }

        function getCatConstructionTypes() {
            trManagementService.getCatConstructionTypes().then(
                function (resp) {
                    if (resp && resp.data) $scope.catConstructionTypes = resp.data;
                },
                function (error) {
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        function getGroup() {

            var obj = {loggedInUser: $scope.loggedInUser};

            Restangular.all("woService/user/getSysUserGroup").post(obj).then(
                function (resp) {
                    console.log(resp);
                    if (resp && resp.data) {
                        $scope.sysUserGroup = resp.data;
                        $scope.role.trCreator = resp.data.trCreator;
                    }
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )

        }

        function getTrTypes() {
            trManagementService.getTrTypes().then(
                function (resp) {
                    //console.log(resp);
                    if (resp && resp.data) $scope.trTypes = resp.data;
                },
                function (error) {
                    //console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        function getAvailableContracts() {
            trManagementService.getAvailableContracts().then(
                function (resp) {
                    if (resp && resp.data) $scope.Constracts = resp.data;
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        function getAvailableProjects() {
            trManagementService.getAvailableProjects().then(
                function (resp) {
                    if (resp && resp.data) $scope.Projects = resp.data;
                    console.log($scope.trTypes);
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        function getStateText(state) {
            var text = 'null';
            $scope.WOStates.states = []
            Object.keys(Constant.WO_TR_XL_STATE).forEach(
                function (key, index) {
                    var obj = {};
                    obj.stateCode = Constant.WO_TR_XL_STATE[key].stateCode;
                    obj.stateName = Constant.WO_TR_XL_STATE[key].stateText;
                    $scope.WOStates.states.push(obj);
                }
            );
            // Object.values(Constant.WO_XL_STATE);
            // return text;
        }

        function getAppWorkSrcs() {
            trManagementService.getAppWorkSrcs().then(
                function (resp) {
                    console.log(resp);
                    if (resp.data) $scope.apWorkSrcs = resp.data;
                },
                function (error) {
                    //console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        function getCdLevel1() {
            trManagementService.getCdLevel1({}).then(
                function (resp) {
                    if (resp && resp.data) {
                    	 $scope.cdLv1List = resp.data;
                        if ($scope.isCreateNew) {
                            $scope.workingTR.cdLevel1 = resp.data[0].code;
                        }

                    }
                },
                function (error) {
                    //console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        vm.doSearch = doSearch;

        function doSearch() {
            var grid = vm.dataTRListTable;

            Object.keys(vm.searchForm).forEach(function (key, index) {
                if (vm.searchForm[key] == '') vm.searchForm[key] = null;
            })

            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                })
            }

        }

        vm.cancelInput = function (data) {
            if (data == 'dept') {
                vm.searchForm.sysGroupName = null;
                vm.searchForm.sysGroupId = null;
            }
        }

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };

        vm.viewTRDetails = function (trId) {
            var template = Constant.getTemplateUrl('WO_XL_TR_DETAILS');
            $rootScope.viewDetailsTrId = trId;
            template.trId = trId;
            postal.publish({
                channel: "Tab",
                topic: "open",
                data: template
            });

            postal.publish({
                channel: "Tab",
                topic: "action",
                data: {action: 'refresh', trId: trId}
            });
        };

        vm.prepareCreateNew = function () {
            $scope.workingTR = {};
            $scope.isCreateNew = true;

            $scope.contracts = {};
            $scope.constructions = {};
            $scope.label.contractInfo = '';
            $scope.label.projectInfo = '';
            $scope.label.stationCodeAndAddress = '';
            vm.selectedWorkItem = [];
            vm.catConstructionTypeId = null;
            vm.selectedStations = [];
            if ($scope.permissions.crudTRCnkt) {
                //nếu chi nhánh kt tạo TR thì cd level 1 là trung tâm hạ tầng và cd level 2 là phòng hạ tầng của CNKT
                $scope.workingTR.cdLevel1 = TTHT_SYS_GROUP_ID;
                $scope.workingTR.cdLevel1Name = getCdLevel1name($scope.workingTR.cdLevel1).split('-')[1].trim();
                $scope.workingTR.cdLevel2 = $scope.permissions.crudTRCnktDomainDataList.split(',')[0];

                var obj = {sysGroupId: $scope.workingTR.cdLevel2};
                trManagementService.getSysGroupById(obj).then(function (res) {
                    if (res && res.statusCode == 1) {
                        $scope.workingTR.cdLevel2Name = res.data.groupName;

                        //2 cai nay dung de sinh ma tr
                        $scope.workingTR.assignedCd = res.data.code + ' - ' + res.data.groupName;
                        $scope.workingTR.createTrDomainGroupId = $scope.sysUserGroup.sysGroupId;
                        //

                        //nếu là cnkt tạo tr thì lấy parent của phòng kinh doanh chính là cnkt
                        $scope.workingTR.groupCreated = $scope.sysUserGroup.parentGroupId;
                        $scope.workingTR.groupCreatedName = $scope.sysUserGroup.groupNameLevel2;

                        openModalCreateNewTR();
                    } else {
                        toastr.error('Miền dữ liệu tạo TR cho chi nhánh kỹ thuật không hợp lệ.');
                    }
                });
            } else {
                openModalCreateNewTR();
            }

        };

        function openModalCreateNewTR() {
            $scope.modalInstance = $modal.open({
                templateUrl: 'coms/wo_xl/trManagement/trEditModal.html?v=1003',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                    function () {
                        vm.createNewTR($scope.workingTR);
                    },
                    function () {
                        $scope.workingTR = {};
                        $scope.fileList = [];
                    }
                )
        }

        vm.createNewTR = function (obj) {
            var model = obj;
            model.status = 1;
            model.userCreated = $scope.loggedInUser;
            model.loggedInUser = $scope.loggedInUser;
            model.catWorkItemTypeList = vm.selectedWorkItem;
            if (!validateWorkItemList()) return;

            $scope.isCreateNew = true;
            if (!$scope.permissions.crudTRCnkt) {
                model.cdLevel1Name = getCdLevel1name(model.cdLevel1);
                //2 cai nay dung de sinh ma tr
                model.createTrDomainGroupId = $scope.permissions.createTRDomainDataList.split(",")[0];
                if (model.cdLevel1) model.assignedCd = model.cdLevel1Name;
                //nếu không phải cnkt tạo tr thì lấy theo quyền tạo TR
                model.groupCreated = $scope.permissions.createTRDomainDataList.split(',')[0];

                if (model.groupCreated == TTHT_SYS_GROUP_ID) model.groupCreatedName = 'Trung Tâm Hạ Tầng';
                if (model.groupCreated == GPTH_SYS_GRPUP_ID) model.groupCreatedName = 'Trung tâm Giải pháp tích hợp';
                if (model.groupCreated == XDDTHT_SYS_GROUP_ID) model.groupCreatedName = 'Trung tâm Đầu tư hạ tầng';
                if (model.groupCreated == VHKT_SYS_GROUP_ID) model.groupCreatedName = 'Trung Tâm Vận Hành Khai Thác';
                if (model.groupCreated == TTXDD_SYS_GROUP_ID) model.groupCreatedName = 'Trung tâm Xây dựng dân dụng';
            }

            if ($scope.workingTR.isTmbt) {
                if (!validateRentStationList()) return;

                // Set tmbtTargetDetail
                var tmbtTargetDetail = '';
                for (var i = 0; i < vm.rentStationList.length; i++) {
                    if (vm.rentStationList[i].rentTarget > 0) {
                        tmbtTargetDetail += '{"sysGroupId": ' + vm.rentStationList[i].sysGroupId + ', "rentTarget": ' + vm.rentStationList[i].rentTarget + '},';
                    }
                }
                if (tmbtTargetDetail.includes(",")) {
                    tmbtTargetDetail = '[' + tmbtTargetDetail.slice(0, -1) + ']';
                }
                model.tmbtTargetDetail = tmbtTargetDetail;
                model.selectedStations = vm.selectedStations;
            }

            trManagementService.createNewTR(model).then(
                function (resp) {
                    console.log(resp);

                    if (resp && resp.statusCode == 1) {
                        toastr.success("Thêm mới thành công!");
                        var trId = resp.data;
                        if ($scope.fileList.length > 0) {
                            $scope.fileList.forEach(function (fileObj) {
                                doUploadFile(fileObj.file, trId);
                            })
                            $scope.fileList = [];
                        }
                    } else {
                        toastr.error("Thêm mới thất bại! " + resp.message);
                        $scope.fileList = [];
                    }

                    vm.dataTRListTable.dataSource.read();
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        };

        function getCdLevel1name(code) {
            if ($scope.cdLv1List && $scope.cdLv1List.length > 0) {
                for (var i = 0; i < $scope.cdLv1List.length; i++) {
                    var item = $scope.cdLv1List[i];
                    if (item.code == code) return item.name
                }
                return '';
            } else return '';
        }

        vm.editTR = function (trId) {
            var obj = {trId: trId};
            $scope.isCreateNew = false;
            $scope.isEditingAIOTR = false;
            trManagementService.getOneTRDetails(obj).then(
                function (resp) {
                    console.log("EDIT TR");

                    if (resp && resp.data) $scope.workingTR = resp.data;
                    if ($scope.workingTR.customerType) $scope.isEditingAIOTR = true;
                    $scope.label.contractInfo = $scope.workingTR.contractCode;
                    $scope.label.projectInfo = $scope.workingTR.projectCode;
                    $scope.label.stationCodeAndAddress = $scope.workingTR.stationCode;
                    vm.catConstructionTypeId = $scope.workingTR.catConstructionTypeId;

                    if ($scope.workingTR.projectCode) {
                        $scope.ctOrPj.isContract = false;
                    } else {
                        $scope.ctOrPj.isContract = true;
                    }

                    trManagementService.getInactiveWoList({trId: $scope.workingTR.trId}).then(
                        function (resp) {
                            if (resp && resp.data) vm.inactiveWoList = resp.data;

                            $modal.open({
                                templateUrl: 'coms/wo_xl/trManagement/trEditModal.html?v=999',
                                controller: null,
                                windowClass: 'app-modal-window',
                                scope: $scope
                            })
                                .result.then(
                                function () {
                                    vm.saveTR($scope.workingTR);
                                },
                                function () {
                                    $scope.workingTR = {};
                                }
                            )
                        });
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )

        };

        vm.saveTR = function (obj) {
            obj.loggedInUser = $scope.loggedInUser;
            trManagementService.updateTR(obj).then(
                function (resp) {
                    console.log(resp);

                    if (resp && resp.statusCode == 1) toastr.success("Sửa thành công!");
                    else toastr.success("Đã xóa hoặc không tồn tại!");

                    vm.dataTRListTable.dataSource.read();
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        };

        function getValueByKeys(array, keySearch, valueSearch, keyReturn) {
            if (!array || array.length == 0) return null;

            for (var i = 0; i < array.length; i++) {
                var item = array[i];
                if (item[keySearch] == valueSearch) return item[keyReturn];
            }
            return null;
        }

        vm.importExcelFile = importExcelFile;

        function importExcelFile() {
            console.log("import excel");
            $("#files").val(null);
            $("#files").unbind().click(); //id của input file
            $("#files").change(function () {
                var selectedFile = $("#files")[0].files[0];

                if (!CommonService.isExcelFile(selectedFile.name)) {
                    toastr.error('File không đúng định dạng.');
                    return;
                }

                var reader = new FileReader();
                reader.onload = function (event) {
                    var data = event.target.result;
                    var workbook = XLSX.read(data, {
                        type: 'binary'
                    });
                    var trTypeHeader = ['trTypeId', 'trTypeName'];
                    var trTypeList = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[1]], {
                        header: trTypeHeader,
                        range: 1
                    });

                    var cdLevel1Header = ['cdLevel1', 'cdLevel1Name'];
                    var cdLv1List = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[2]], {
                        header: cdLevel1Header,
                        range: 1
                    });

                    var cdLevel2Header = ['cdLevel2', 'cdLevel2Code', 'cdLevel2Name'];
                    var cdLv2List = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[3]], {
                        header: cdLevel2Header,
                        range: 1
                    });

                    var header = ["trName", "trTypeName", "contractCode", "projectCode", "constructionCode", "finishDate", "qoutaTime", "cdLevel1Name", 'cdLevel2Name'];
                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[0]], {
                        header: header,
                        range: 2
                    });

                    for (var i = 0; i < XL_row_object.length; i++) {
                        var tr = XL_row_object[i];
                        Object.keys(tr).forEach(function (key) {
                            if (key == 'undefined') {
                                toastr.error('Cấu trúc file excel không hợp lệ.');
                                return;
                            }
                        });


                        if (tr.trTypeName) {
                            tr.trTypeId = getValueByKeys(trTypeList, 'trTypeName', tr.trTypeName, 'trTypeId');
                        }

                        if (tr.cdLevel1Name) {
                            tr.cdLevel1 = getValueByKeys(cdLv1List, 'cdLevel1Name', tr.cdLevel1Name, 'cdLevel1');
                        }

                        if (tr.cdLevel2Name) {
                            tr.cdLevel2 = getValueByKeys(cdLv2List, 'cdLevel2Name', tr.cdLevel2Name, 'cdLevel2');
                        }


                        tr.trId = 0;
                        tr.userCreated = $scope.loggedInUser;
                        tr.loggedInUser = $scope.loggedInUser;
                        tr.status = 1;

                        //these 2 properties used for created TR code
                        if ($scope.permissions.crudTRCnkt) {
                            tr.createTrDomainGroupId = $scope.sysUserGroup.sysGroupId;
                            if (tr.cdLevel2Name) {
                                var cd2Code = getValueByKeys(cdLv2List, 'cdLevel2Name', tr.cdLevel2Name, 'cdLevel2Code');
                                tr.assignedCd = cd2Code + '-' + tr.cdLevel2Name;
                            }
                        } else {
                            tr.assignedCd = tr.cdLevel1Name;
                            tr.createTrDomainGroupId = $scope.permissions.createTRDomainDataList.split(",")[0];
                        }


                        tr = trimProps(tr);

                    }

                    console.log(XL_row_object);

                    var json_object = JSON.stringify(XL_row_object);
                    addDataFormExcel(json_object);

                };
                reader.onerror = function (event) {
                    console.error("File could not be read! Code " + event.target.error.code);
                };
                reader.readAsBinaryString(selectedFile);
            });
        }

        function trimProps(obj) {
            Object.keys(obj).forEach(function (key) {
                if (obj[key] && typeof obj[key].trim === "function") obj[key] = obj[key].trim();
            })
            return obj;
        }

        function formatDate(date) {
            var d = new Date(date),
                month = '' + (d.getMonth() + 1),
                day = '' + d.getDate(),
                year = d.getFullYear();

            if (month.length < 2)
                month = '0' + month;
            if (day.length < 2)
                day = '0' + day;

            return [year, month, day].join('-');
        }

        vm.addDataFormExcel = addDataFormExcel;

        function addDataFormExcel(res) {
            trManagementService.createManyTR(res).then(function (res) {
                if (res && res.statusCode == 1) {
                    vm.dataTRListTable.dataSource.read();
                    toastr.success("Import thành công! ");
                    confirm('Import dữ liệu thành công! Bạn có muốn xem chi tiết?', function () {
                        getImportExcelResult(res.data)
                    });
                } else {
                    toastr.error("Import thất bại! " + res.message ? res.message : '');
                    confirm('Phát hiện dữ liệu không hợp lệ! Bạn có muốn xem chi tiết?', function () {
                        getImportExcelResult(res.data)
                    });
                }
            })
        }

        function getImportExcelResult(obj) {
            $http({
                url: Constant.BASE_SERVICE_URL + "trService/tr/createManyTRReport",
                method: "POST",
                data: obj,
                headers: {
                    'Content-type': 'application/json'
                },
                responseType: 'arraybuffer'
            }).success(function (data, status, headers, config) {
                htmlCommonService.saveFile(data, "Ket_qua_import_TR.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            }).error(function (data, status, headers, config) {
                toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
            });
        }

        vm.deleteTR = function (trId) {
            var obj = {trId: trId, loggedInUser: $scope.loggedInUser};
            confirm('Xác nhận xóa bản ghi đã chọn?',
                function () {
                    trManagementService.deleteTR(obj).then(
                        function (resp) {
                            console.log(resp);

                            if (resp && resp.statusCode == 1) toastr.success("Xóa thành công!");
                            else toastr.success("Đã xóa hoặc không tồn tại!");

                            console.log(vm)
                            vm.dataTRListTable.dataSource.read();
                        },
                        function (error) {
                            console.log(error);
                            toastr.success("Có lỗi xảy ra!");
                        }
                    )
                }
            );
        }

        function createActionTemplate(dataItem) {
            var isEditable = false;
            var isDeleteable = false;

            //debugger;

            if (dataItem.state == 'UNASSIGN' || dataItem.state == 'ASSIGN_CD' || dataItem.state == 'REJECT_CD') {
                if (dataItem.cdLevel2 == null || dataItem.cdLevel2 == '') {
                    if ($scope.permissions.updateTR) isEditable = true;
                    else isEditable = false;
                    if ($scope.permissions.deleteTR) isDeleteable = true;
                    else isDeleteable = false;
                } else {
                    if ($scope.permissions.crudTRCnkt) {
                        isEditable = true;
                        isDeleteable = true;
                    } else {
                        isEditable = false;
                        isDeleteable = false;
                    }
                }
            }


            var template = '<div class="display-block cedtpl" style="text-align: center">' +
                '<i class="fa fa-list-alt icon-table" ng-click="vm.viewTRDetails(' + dataItem.trId + ')"></i>' +
                '<i ng-if="' + isEditable + '" class="fa fa-pencil icon-table" data-toggle="modal" ng-click="vm.editTR(' + dataItem.trId + ')"></i>' +
                '<i ng-if="' + isDeleteable + '" class="fa fa-trash-o icon-table" ng-click="vm.deleteTR(' + dataItem.trId + ')"></i></div>'

            return template;
        }

        function fillDataTable() {

            vm.dataTRListTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save: function () {
                    vm.dataTRListTable.dataSource.read();
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
                            //$("#appCount").text("" + response.total);
                            vm.count = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            console.log(response);
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            //Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "trService/tr/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page;
                            vm.searchForm.pageSize = options.pageSize;
                            return JSON.stringify(vm.searchForm);
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
                    refresh: true,
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
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Mã Tr",
                        field: 'codeTr',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.trCode ? dataItem.trCode : '';
                        },
                    },
                    {
                        title: "Tên Tr",
                        field: 'nameTr',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            var trName = dataItem.trName
                            return trName ? trName : '';
                        },
                    },
                    {
                        title: "Người tạo",
                        field: 'userCreatedInfo',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            if (dataItem.userCreatedFullName && dataItem.userCreatedEmail) {
                                return dataItem.userCreatedFullName + ' - ' + dataItem.userCreatedEmail;
                            } else return dataItem.userCreatedFullName ? dataItem.userCreatedFullName : '';
                        }
                    },
                    {
                        title: "Mã dự án",
                        field: 'status',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.projectCode ? dataItem.projectCode : '';
                        }
                    },
                    {
                        title: "Mã hợp đồng",
                        field: 'status',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.contractCode ? dataItem.contractCode : '';
                        }
                    },
                    {
                        title: "Hạn hoàn thành",
                        field: 'deadLine',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.finishDate ? dataItem.finishDate : '';
                        }
                    },{
                        title: "Công việc chưa hoàn thành",
                        field: 'woCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text'
                    },
                    {
                        title: "Loại công việc",
                        field: 'woTypeName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text'
                    },
                    {
                        title: "Trạng thái",
                        field: 'deadLine',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return getTrStateText(dataItem.state) ? getTrStateText(dataItem.state) : '';
                        }
                    },
                    {
                        title: "Thao tác",
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return createActionTemplate(dataItem);
                        }
                    }
                ]
            });
        }

        function getTrStateText(state) {
            var text = '';
            var states = Constant.WO_TR_XL_STATE;
            Object.keys(states).forEach(
                function (key, index) {
                    var obj = {};
                    obj.stateCode = states[key].stateCode;
                    obj.stateName = states[key].stateText;

                    if (states[key].stateCode == state) {
                        text = states[key].stateText;
                    }
                }
            );
            return text;
        }

        vm.openAssignmentModal = function (trId) {
            trManagementService.getCdLv2List().then(
                function (resp) {
                    console.log(resp);
                    if (resp && resp.data) {
                        $scope.cdLv2List = resp.data;
                        $modal.open({
                            templateUrl: 'coms/wo_xl/trManagement/trAssignmentToCDModal.html',
                            controller: null,
                            windowClass: 'app-modal-window',
                            scope: $scope
                        })
                            .result.then(
                            function () {
                                console.log($scope.selectedUnit)
                                vm.giveAssignmentTR(trId, $scope.selectedUnit.sysGroupId)
                            },
                            function () {
                            }
                        )
                    } else {
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    }
                },
                function (error) {
                    console.log(error)
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )

        }

        vm.giveAssignmentTR = function (trId, cdName) {
            var obj = {
                trId: trId,
                assignedCd: cdName
            }

            trManagementService.giveAssignmentTRToCD(obj).then(
                function (resp) {
                    if (resp.statusCode > 0) {
                        toastr.success("Giao việc thành công.");
                        vm.dataTRListTable.dataSource.read();
                    } else toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                },
                function (error) {
                    console.log(error)
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )
        }

        vm.acceptTr = function (trId) {
            var obj = {
                trId: trId,
                loggedInUser: $scope.loggedInUser
            }
            trManagementService.acceptTr(obj).then(
                function (resp) {
                    console.log(resp);

                    if (resp && resp.statusCode == 1) toastr.success("Nhận thành công!");
                    else toastr.error("Có lỗi xảy ra!");

                    vm.dataTRListTable.dataSource.read();
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        vm.rejectTr = function (trId) {
            var obj = {
                trId: trId,
                loggedInUser: $scope.loggedInUser
            }
            trManagementService.rejectTr(obj).then(
                function (resp) {
                    console.log(resp);

                    if (resp && resp.statusCode == 1) toastr.success("Từ chối thành công!");
                    else toastr.error("Có lỗi xảy ra!");

                    vm.dataTRListTable.dataSource.read();
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        vm.downloadTemplate = function () {
            confirm('Bạn có muốn tải file mẫu?', function () {
                $http({
                    url: Constant.BASE_SERVICE_URL + "trService/tr/getImportExcelTemplate",
                    method: "POST",
                    data: {},
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function (data, status, headers, config) {
                    saveFile(data, "TrCreateExample.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                }).error(function (data, status, headers, config) {
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                });
            })
        }

        function saveFile(data, filename, type) {
            var file = new Blob([data], {type: type});
            if (window.navigator.msSaveOrOpenBlob) // IE10+
                window.navigator.msSaveOrOpenBlob(file, filename);
            else { // Others
                var a = document.createElement("a"),
                    url = URL.createObjectURL(file);
                a.href = url;
                a.download = filename;
                document.body.appendChild(a);
                a.click();
                setTimeout(function () {
                    document.body.removeChild(a);
                    window.URL.revokeObjectURL(url);
                }, 0);
            }
        }

        $scope.addAttachment = function () {

            if (document.getElementById('trAttachmentFileId') != null) {
                document.getElementById('trAttachmentFileId').addEventListener("click", function (evt) {
                    evt.stopPropagation();
                }, false);
            }

            $("#trAttachmentFileId").unbind().click();
            $("#trAttachmentFileId").change(function () {
                var selectedFile = $("#trAttachmentFileId")[0].files[0];
                if (selectedFile) {
                    var key = selectedFile.name + selectedFile.lastModified;
                    var isAdded = checkFileAlreadyAdded(key);
                    console.log(isAdded);
                    if (!isAdded) {
                        var obj = {
                            key: key,
                            file: selectedFile
                        }
                        $scope.fileList.push(obj);
                        $scope.$apply();
                    }
                }

                console.log($scope.fileList);
            });
            //
        };

        function checkFileAlreadyAdded(key) {
            for (var i = 0; i < $scope.fileList.length; i++) {
                if ($scope.fileList[i].key == key) return true;
            }

            return false;
        }

        function doUploadFile(file, trId) {
            var formData = new FormData();
            formData.append('multipartFile', file);

            //send file through fileService
            var uploadFileService = Constant.BASE_SERVICE_URL + 'fileService/uploadWoFileATTT?folder=' + Constant.UPLOAD_FOLDER_IMAGES;
            trManagementService.uploadAttachment(uploadFileService, formData)
                .success(function (resp) {
                    if (resp[0]) {
                        var filePath = resp[0];
                        var fileName = file.name;
                        var mappingObj = {
                            trId: trId,
                            fileName: fileName,
                            filePath: filePath,
                            userCreated: $scope.loggedInUser,
                            status: 1
                        }

                        trManagementService.mappingAttachmentToTR(mappingObj).then(
                            function (resp) {
                                console.log(resp);
                                toastr.success("Upload file " + fileName + " thành công");
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
        }

        function getConstructionByContract() {
            console.log($scope.workingTR.contractCode);

            if ($scope.workingTR.contractCode == null || $scope.workingTR.contractCode == '') return;
            var obj = {contractCode: $scope.workingTR.contractCode}

            trManagementService.getConstructionByContract(obj).then(
                function (resp) {
                    if (resp && resp.data) $scope.constructions = resp.data;
                    console.log($scope.constructions)
                },
                function (error) {
                }
            )
        }

        function getConstructionByProject() {

            if ($scope.workingTR.projectCode == null || $scope.workingTR.projectCode == '') return;
            var obj = {projectCode: $scope.workingTR.projectCode}

            trManagementService.getConstructionByProject(obj).then(
                function (resp) {
                    if (resp && resp.data) $scope.constructions = resp.data;
                    console.log($scope.constructions)
                },
                function (error) {
                }
            )
        }

        $scope.getRelatedInfo = function () {
            if ($scope.workingTR.isTmbt) {
                vm.isAutoCreateWo = false;
            } else if ($scope.permissions.createTR && $scope.permissions.createTRDomainDataList.includes(XDDTHT_SYS_GROUP_ID)) {
                vm.isAutoCreateWo = true;
            }
            if (!$scope.workingTR.construction) return;
            $scope.workingTR.constructionCode = $scope.workingTR.construction.constructionCode;
            $scope.workingTR.constructionId = $scope.workingTR.construction.constructionId;
            vm.catConstructionTypeId = $scope.workingTR.construction.catConstructionTypeId;

            if ($scope.workingTR.constructionCode == null || $scope.workingTR.constructionCode == '') return;

            var stationId = getStationId($scope.workingTR.constructionCode);

            if (stationId == null) return;

            var obj = {stationId: stationId};

            loadStation(obj);
            // if (vm.isAutoCreateWo) vm.workItemListTbl.dataSource.read();
        };

        function getStationId(code) {
            for (var i = 0; i < $scope.constructions.length; i++) {
                if ($scope.constructions[i].constructionCode == code) return $scope.constructions[i].stationId;
            }
            return null;
        }

        function loadStation(obj) {
            trManagementService.getStationById(obj).then(
                function (resp) {
                    if (resp && resp.data) {
                        console.log(resp.data)
                        $scope.workingTR.stationCode = resp.data.stationCode;
                        $scope.workingTR.executeLat = resp.data.latitude;
                        $scope.workingTR.executeLong = resp.data.longitude;
                        $scope.label.stationCodeAndAddress = $scope.workingTR.stationCode + ' - ' + resp.data.stationAddress;
                        if ($scope.workingTR.trTypeCode == 'TR_DONG_BO_HA_TANG') {
                            $scope.workingTR.cdLevel2Name = resp.data.sysGroupName;
                            $scope.workingTR.cdLevel2 = resp.data.sysGroupId;
                        }
                    }
                },
                function (error) {
                }
            )
        }

        $scope.autoCompleteStationOptions = {
            dataTextField: "stationCode", placeholder: "Thông tin trạm",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                vm.searchForm.stationCode = data.stationCode;
                $scope.label.stationName = data.stationCode + ' - ' + data.stationAddress;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = $('#autoCompleteStationId').val().trim();

                        var objSearch = {keySearch: keySearch};

                        return Restangular.all("trService/tr/doSearchStation").post(objSearch).then(
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
                '<p class="col-md-12 text-header-auto">Các trạm</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-9" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.stationCode # - #: data.stationAddress #</div></div>',
            change: function (e) {
            },
            close: function (e) {
                // handle the event0
            }
        };
        
        $scope.autoCompleteConstructionCode={
                dataTextField: "constructionCode", placeholder:"Mã công trình",
                open: function(e) {
                },
                select: function(e) {
                    data = this.dataItem(e.item.index());
                    $scope.vm.searchForm.constructionCode =" " +  data.code;
                    vm.searchForm.constructionCode = " " + data.code;
                    $scope.$apply();
                },
                pageSize: 10,
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function(options) {
                            var keySearch = $('#autoCompleteConstructionId').val().trim();

                            var objSearch = {keySearch:keySearch};

                            return Restangular.all("trService/tr/doSearchContruction").post(objSearch).then(
                                function(response){
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
                    '<p class="col-md-12 text-header-auto">Mã công trình</p></div>',
                template:'<div class="row" >' +
                    '<div class="col-xs-9" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.code #</div></div>',
                change: function(e) {
                },
                close: function(e) {
                    // handle the event0
                }
            };

        $scope.autoCompleteFilterProjectOptions={
            dataTextField: "projectCode", placeholder:"Thông tin dự án",
            open: function(e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                vm.searchForm.projectCode = data.projectCode;
                getConstructionByProject();
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = vm.searchForm.projectCode;

                        if (!keySearch || keySearch == '') {
                            vm.searchForm.projectCode = null;
                        }

                        var objSearch = {keySearch: keySearch, page: 1, pageSize: 20};

                        return Restangular.all("trService/tr/doSearchProjects").post(objSearch).then(
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
                '<p class="col-md-12 text-header-auto">Các dự án</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-9" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.projectCode #</div></div>',
            change: function (e) {
            },
            close: function (e) {
                // handle the event0
            }
        };

        $scope.autoCompleteFilterContractOptions = {
            dataTextField: "contractCode", placeholder: "Thông tin hợp đồng",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                vm.searchForm.contractCode = data.contractCode;
                getConstructionByContract();
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = vm.searchForm.contractCode;
                        if (!keySearch || keySearch == '') {
                            vm.searchForm.contractCode = null;
                        }

                        var objSearch = {keySearch: keySearch, page: 1, pageSize: 20};

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
                '<p class="col-md-12 text-header-auto">Các hợp đồng</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-9" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.contractCode #</div></div>',
            change: function (e) {
            },
            close: function (e) {
                // handle the event0
            }
        };

        $scope.autoCompleteProjectOptions = {
            dataTextField: "projectCode", placeholder: "Thông tin dự án",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                $scope.workingTR.projectCode = data.projectCode;
                $scope.workingTR.projectId = data.projectId;
                $scope.label.projectInfo = data.projectCode + ' - ' + data.projectName;
                getConstructionByProject();
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = $('#autoCompleteProjectId').val().trim();
                        resetContractProject();

                        var objSearch = {keySearch: keySearch, page: 1, pageSize: 20};

                        return Restangular.all("trService/tr/doSearchProjects").post(objSearch).then(
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
                '<p class="col-md-12 text-header-auto">Các dự án</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-9" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.projectCode # - #: data.projectName #</div></div>',
            change: function (e) {
            },
            close: function (e) {
                // handle the event0
            }
        };

        $scope.autoCompleteContractOptions = {
            dataTextField: "contractCode", placeholder: "Thông tin hợp đồng",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                $scope.workingTR.contractCode = data.contractCode;
                $scope.workingTR.contractId = data.contractId;
                $scope.label.contractInfo = data.contractCode;
                getConstructionByContract();
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = $scope.workingTR.contractCode;
                        resetContractProject();

                        var objSearch = {keySearch: keySearch, page: 1, pageSize: 20};
                        //duonghv13 add 08122021
                        if(($scope.workingTR.trTypeId != null || $scope.workingTR.trTypeId != undefined) && ($scope.workingTR.trTypeCode != null || $scope.workingTR.trTypeCode != undefined )){
                        	objSearch.trTypeId = $scope.workingTR.trTypeId;
                        	objSearch.trTypeCode = $scope.workingTR.trTypeCode;
                        }
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
                '<p class="col-md-12 text-header-auto">Các hợp đồng</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-9" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.contractCode #</div></div>',
            change: function (e) {
            },
            close: function (e) {
                // handle the event0
            }
        };

        $scope.autoCompleteFilterAIOContractOptions = {
            dataTextField: "contractCode", placeholder: "Thông tin hợp đồng AIO",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                vm.searchForm.aioContractId = data.contractId;
                vm.searchForm.aioContractCode = data.contractCode;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = vm.searchForm.aioContractCode;

                        if (!keySearch || keySearch == '') {
                            vm.searchForm.aioContractId = null;
                            vm.searchForm.aioContractCode = null;
                        }

                        var objSearch = {keySearch: keySearch, page: 1, pageSize: 20};

                        return Restangular.all("trService/tr/doSearchAIOContracts").post(objSearch).then(
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
                '<p class="col-md-12 text-header-auto">Các hợp đồng AIO</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-9" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.contractCode # </div></div>',
            change: function (e) {
            },
            close: function (e) {
                // handle the event0
            }
        }

        $scope.selectContract = function () {
            $scope.workingTR.projectCode = null;
            $scope.label.projectInfo = null;
            resetContractProject();
        };

        $scope.selectProject = function () {
            $scope.workingTR.contractCode = null;
            resetContractProject();
        };

        function resetContractProject() {
            $scope.label.stationCodeAndAddress = '';
            $scope.workingTR.executeLong = '';
            $scope.workingTR.executeLat = '';
            $scope.workingTR.stationCode = null;
            $scope.workingTR.construction = null;
            $scope.workingTR.contractId = null;
            $scope.workingTR.projectId = null;
        }

        $scope.validateCreateNew = validateCreateNew;

        function validateCreateNew(tr) {

            if (!tr) tr = $scope.workingTR;

            if (!tr.trName) {
                toastr.error("Tên TR không được để trống.");
                $('#trNameInput').focus();
                return false;
            }

            if (!tr.trTypeId) {
                toastr.error("Xin chọn loại TR.");
                return false;
            }

            if (!tr.cdLevel1) {
                toastr.error("Xin chọn giao CD level 1.");
                return false;
            }

            if (tr.trCode == "TR_THUE_MAT_BANG_TRAM") {
                if (!tr.contractCode && !tr.projectCode) {
                    toastr.error("Mã hợp đồng hoặc mã dự án không được để trống.");
                    return false;
                } else {
                    if (!tr.contractId && tr.contractCode) {
                        toastr.error("Mã hợp đồng không tồn tại trong hệ thống.");
                        return false;
                    }

                    if (tr.projectCode && !tr.projectId) {
                        toastr.error("Mã dự án không tồn tại trong hệ thống.");
                        return false;
                    }
                }

                if (!tr.constructionCode) {
                    toastr.error("Mã công trình không được để trống.");
                    return false;
                }
            }

            if (!tr.finishDate) {
                toastr.error("Hạn hoàn thành không được để trống.");
                return false;
            }

            if ((!$scope.fileList || $scope.fileList.length == 0) && $scope.isCreateNew) {
                toastr.error("Phải có tài liệu đính kèm.");
                return false;
            }

            if ($scope.workingTR.isDbht) {
                if (!tr.dbTkdaDate) {
                    toastr.error("Ngày đảm bảo thiết kế dự án không được để trống.");
                    return false;
                }

                if (!tr.dbTtkdtDate) {
                    toastr.error("Ngày đảm bảo thẩm thiết kế dự toán quyết định phê duyệt không được để trống.");
                    return false;
                }

                if (!tr.dbVtDate) {
                    toastr.error("Ngày đảm bảo vật tư không được để trống.");
                    return false;
                }
            }

            return true;
        };

        vm.showHideColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.dataTRListTable.hideColumn(column);
            } else if (column.hidden) {
                vm.dataTRListTable.showColumn(column);
            } else {
                vm.dataTRListTable.hideColumn(column);
            }
        }

        vm.exportTrList = function () {
            confirm("Xuất dữ liệu excel?", function () {
                $http({
                    url: Constant.BASE_SERVICE_URL + "trService/tr/exportTrExcel",
                    method: "POST",
                    data: vm.searchForm,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function (data, status, headers, config) {
                    saveFile(data, "Danh_sach_tr.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                }).error(function (data, status, headers, config) {
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                });
            })
        }

        vm.importZip = importZip;

        function importZip() {
            vm.fileImportData = false;
            var teamplateUrl = "coms/wo_xl/trManagement/importZip.html";
            var title = "Import TR";
            var windowId = "IMPORT_TR_ZIP";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '275', null);

        }

        vm.onSelectFile = onSelectFile;
        function onSelectFile() {
            if ($("#fileImport")[0].files[0].name.split('.').pop() != 'zip' && $("#fileImport")[0].files[0].name.split('.').pop() != 'zar') {
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
            	$("#fileName")[0].innerHTML = $("#fileImport")[0].files[0].name
            }
            
        }

        vm.submit = submit;

        function submit(data) {
            var ajax_sendding = false;
            if (ajax_sendding == true) {
                alert('Dang Load Ajax');
                return false;
            }
            if ($("#fileImport")[0].files[0] == null) {
                toastr.warning("Bạn chưa chọn file để import");
                return;
            }
            if ($("#fileImport")[0].files[0].name.split('.').pop() != 'zip' && $("#fileImport")[0].files[0].name.split('.').pop() != 'zar') {
                toastr.warning("Sai định dạng file");
                return;
            }
            var formData = new FormData();
            formData.append('multipartFile', $('#fileImport')[0].files[0]);
            $('#loadding').show();
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "trService/importFileZip",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if (data && data.statusCode == 1) {
                        toastr.success("Import thành công! ");
                        confirm('Import dữ liệu thành công! Bạn có muốn xem chi tiết?', function () {
                            getImportExcelResult(data.data)
                            doSearch();
                        });
                        cancelImport();
                    } else {
                        toastr.error("Import thất bại! " + data.message ? data.message : '');
                        if (data.data != null) {
                            confirm('Phát hiện dữ liệu không hợp lệ! Bạn có muốn xem chi tiết?', function () {
                                getImportExcelResult(data.data)
                            });
                        }
                        cancelImport();
                    }
                    cancelImport();
                    $scope.$apply();
                }
            }).always(function () {
                ajax_sendding = false;
                $('#loadding').hide();
            });
        }

        vm.cancelImport = cancelImport;

        function cancelImport() {
            $(".k-icon.k-i-close").click();
        };
        vm.exportFileZip = function () {
            function displayLoading(target) {
                var element = $(target);
                kendo.ui.progress(element, true);
                setTimeout(function () {
                    return Restangular.all("trService/exportFileZip").post(vm.searchForm).then(function (d) {
                        var data = d.plain();
                        window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                        kendo.ui.progress(element, false);
                    }).catch(function (e) {
                        kendo.ui.progress(element, false);
                        toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                        return;
                    });
                    ;

                });

            }

            displayLoading(".tab-content");
        }


        // Bỏ do xây dựng luồng DBHT mới -> mặc định sinh 7 WO
        // var workItemRecord = 0;
        //
        // function fillWorkItemDataTbl() {
        //     vm.workItemListOptions = kendoConfig.getGridOptions({
        //         autoBind: true,
        //         scrollable: false,
        //         resizable: true,
        //         editable: false,
        //         save: function () {
        //             vm.workItemListTbl.dataSource.read();
        //         },
        //         dataBinding: function () {
        //             workItemRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
        //         },
        //         reorderable: true,
        //         toolbar: null,
        //         dataSource: {
        //             serverPaging: false,
        //             schema: {
        //                 total: function (response) {
        //                     return response.total;
        //                 },
        //                 data: function (response) {
        //                     vm.workItemList = response.data;
        //                     if (!$scope.isCreateNew) vm.workItemList = getInactiveWoValue(response.data);
        //                     return response.data;
        //                 }
        //             },
        //             transport: {
        //                 read: {
        //                     //Thuc hien viec goi service
        //                     url: Constant.BASE_SERVICE_URL + "trService/workItem/getAutoWoWorkItems",
        //                     contentType: "application/json; charset=utf-8",
        //                     type: "POST"
        //                 },
        //                 parameterMap: function (options, type) {
        //                     var searchOptions = {
        //                         catConstructionTypeId: vm.catConstructionTypeId,
        //                         trBranch: vm.trBranch,
        //                     };
        //                     searchOptions.page = 1;
        //                     searchOptions.pageSize = 9999;
        //                     return JSON.stringify(searchOptions)
        //                 }
        //             },
        //             pageSize: 9999,
        //         },
        //         noRecords: true,
        //         columnMenu: false,
        //         messages: {
        //             noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
        //         },
        //         pageable: false,
        //         columns: [
        //             {
        //                 title: "STT", field: "stt",
        //                 template: function () {
        //                     return ++workItemRecord;
        //                 },
        //                 width: '5%',
        //                 columnMenu: false,
        //                 headerAttributes: {style: "text-align:center;font-weight: bold;"},
        //                 attributes: {style: "text-align:center;"},
        //                 type: 'text',
        //             },
        //             {
        //                 title: "Tên hạng mục",
        //                 field: 'name',
        //                 width: '30%',
        //                 headerAttributes: {style: "text-align:center;font-weight: bold;"},
        //                 attributes: {style: "text-align:left;"},
        //                 type: 'text'
        //             },
        //             {
        //                 title: "Loại đơn vị tính",
        //                 field: 'hmTypeValue',
        //                 width: '15%',
        //                 headerAttributes: {style: "text-align:center;font-weight: bold;"},
        //                 attributes: {style: "text-align:left;"},
        //                 type: 'text',
        //                 template: function (dataItem) {
        //                     if (dataItem.hmTypeValue == 1) return "Theo độ dài";
        //                     else return "Theo sản lượng (VND)";
        //                 }
        //             },
        //             {
        //                 title: "Giá trị",
        //                 field: 'hmValue',
        //                 width: '15%',
        //                 headerAttributes: {style: "text-align:center;font-weight: bold;"},
        //                 attributes: {style: "text-align:left;"},
        //                 type: 'text',
        //                 template: function (dataItem) {
        //                     var disabled = '';
        //                     if (!$scope.isCreateNew) disabled = 'disabled';
        //                     return '<input ' + disabled + ' id="hmValue' + dataItem.catWorkItemTypeId + '" class="form-control" ng-model="dataItem.hmValue" ng-change="vm.changeHmValue(' + dataItem.catWorkItemTypeId + ')" only-numbers/>'
        //                 }
        //             },
        //             {
        //                 title: "Định mức thời gian (số giờ)",
        //                 field: 'hmValue',
        //                 width: '20%',
        //                 headerAttributes: {style: "text-align:center;font-weight: bold;"},
        //                 attributes: {style: "text-align:left;"},
        //                 type: 'text',
        //                 template: function (dataItem) {
        //                     var disabled = '';
        //                     if (!$scope.isCreateNew) disabled = 'disabled';
        //                     return '<input ' + disabled + ' id="hmQuotaTime' + dataItem.catWorkItemTypeId + '" class="form-control" ng-model="dataItem.hmQuotaTime" ng-change="vm.changeHmQuotaTime(' + dataItem.catWorkItemTypeId + ')" only-numbers/>'
        //                 }
        //             },
        //             {
        //                 title: "Chọn",
        //                 field: 'state',
        //                 width: '10%',
        //                 headerAttributes: {style: "text-align:center;font-weight: bold;"},
        //                 attributes: {style: "text-align:center;"},
        //                 type: 'text',
        //                 template: function (dataItem) {
        //                     var checked = dataItem.isChecked;
        //                     var disabled = '';
        //                     if (!$scope.isCreateNew) disabled = 'disabled';
        //                     var selectCbx = '<input ' + checked + ' ' + disabled + ' type="checkbox" value="' + dataItem.id + '" class="auto-wo-work-item" ng-click="vm.toggleChooseItem($event,' + dataItem.catWorkItemTypeId + ')" only-numbers/>';
        //                     return selectCbx;
        //                 }
        //             },
        //         ]
        //     });
        // }

        function getInactiveWoValue(items) {
            if (vm.inactiveWoList.length == 0 || items.length == 0) return items;

            for (var i = 0; i < vm.inactiveWoList.length; i++) {
                for (var j = 0; j < items.length; j++) {
                    items[j].isChecked = '';
                    if (vm.inactiveWoList[i].catWorkItemTypeId == items[j].catWorkItemTypeId) {
                        items[j].hmValue = vm.inactiveWoList[i].moneyValue;
                        if (items[j].hmTypeValue == 1) items[j].hmValue = vm.inactiveWoList[i].quantityValue;

                        items[j].hmQuotaTime = vm.inactiveWoList[i].qoutaTime;
                        items[j].isChecked = 'checked';
                    }
                }
            }

            return items;
        }

        // Bỏ do xây dựng luồng DBHT mới -> mặc định sinh 7 WO
        // vm.toggleChooseItem = function (event, id) {
        //     if (event.target.checked == true) {
        //         var selected = vm.workItemList.filter(function (item) {
        //             return item.catWorkItemTypeId == id;
        //         });
        //         if (selected.length == 1) vm.selectedWorkItem.push(selected[0]);
        //     } else {
        //         // var index = vm.selectedWorkItem.indexOf(id);
        //         // if (index > -1) {
        //         //     vm.selectedWorkItem.splice(index, 1);
        //         // }
        //
        //         vm.workItemList = vm.workItemList.filter(function (item) {
        //             return item.catWorkItemTypeId != id;
        //         })
        //     }
        // }

        vm.changeHmQuotaTime = function (id) {
            var inputValue = $('#hmQuotaTime' + id).val();
            try {
                if (inputValue && parseInt(inputValue) > 0) {
                    for (var i = 0; i < vm.workItemList.length; i++) {
                        if (vm.workItemList[i].catWorkItemTypeId == id) vm.workItemList[i].hmQuotaTime = inputValue;
                    }

                    for (var j = 0; j < vm.selectedWorkItem.length; j++) {
                        if (vm.workItemList[j].catWorkItemTypeId == id) vm.workItemList[j].hmQuotaTime = inputValue;
                    }
                }
            } catch (e) {
                toastr.error('Có lỗi xảy ra');
            }
        };

        vm.changeHmValue = function (id) {
            var inputValue = $('#hmValue' + id).val();
            try {
                if (inputValue && parseInt(inputValue) > 0) {
                    for (var i = 0; i < vm.workItemList.length; i++) {
                        if (vm.workItemList[i].catWorkItemTypeId == id) vm.workItemList[i].hmValue = inputValue;
                    }

                    for (var j = 0; j < vm.selectedWorkItem.length; j++) {
                        if (vm.workItemList[j].catWorkItemTypeId == id) vm.workItemList[j].hmValue = inputValue;
                    }
                }
            } catch (e) {
                toastr.error('Có lỗi xảy ra');
            }
        };

        $scope.validateWorkItemList = validateWorkItemList;

        function validateWorkItemList() {
            if ($scope.permissions.createTR && $scope.permissions.createTRDomainDataList.includes(XDDTHT_SYS_GROUP_ID)) {
                if (vm.selectedWorkItem.length > 0) {
                    for (var i = 0; i < vm.selectedWorkItem.length; i++) {
                        var itemId = vm.selectedWorkItem[i].catWorkItemTypeId;
                        if (!vm.selectedWorkItem[i].hmValue || vm.selectedWorkItem[i].hmValue == '') {
                            toastr.error('Giá trị sản lượng không được để trống');
                            $('#hmValue' + itemId).focus();
                            return false;
                        }

                        if (!vm.selectedWorkItem[i].hmQuotaTime || vm.selectedWorkItem[i].hmQuotaTime == '') {
                            toastr.error('Định mức thời gian không được để trống');
                            $('#hmQuotaTime' + itemId).focus();
                            return false;
                        }
                    }
                } else if (!$scope.workingTR.isTmbt && !$scope.workingTR.isDbht && !$scope.workingTR.isVhkt) {
                    toastr.error('Bạn chưa chọn hạng mục !');
                    return false;
                }
            }

            return true;
        }

        $scope.selectTrType = function () {
            $scope.workingTR.isTmbt = false;
            $scope.workingTR.isDbht = false;
            $scope.workingTR.isVhkt = false;
            for (var i = 0; i < $scope.trTypes.length; i++) {
                if ($scope.trTypes[i].trTypeCode == 'TR_THUE_MAT_BANG_TRAM' && $scope.trTypes[i].woTrTypeId == $scope.workingTR.trTypeId) {
                    $scope.workingTR.isTmbt = true;
                    $scope.workingTR.trTypeCode = 'TR_THUE_MAT_BANG_TRAM';
                    fillRentStationDataTbl();
                    break;
                } else if ($scope.trTypes[i].trTypeCode == 'TR_DONG_BO_HA_TANG' && $scope.trTypes[i].woTrTypeId == $scope.workingTR.trTypeId) {
                    $scope.workingTR.isDbht = true;
                    $scope.workingTR.trTypeCode = 'TR_DONG_BO_HA_TANG';
                    break;
                } else if ($scope.trTypes[i].trTypeCode == 'TR_VHKT_NANG_LUONG_MAT_TROI' && $scope.trTypes[i].woTrTypeId == $scope.workingTR.trTypeId) {
                    $scope.workingTR.isVhkt = true;
                    $scope.workingTR.trTypeCode = 'TR_VHKT_NANG_LUONG_MAT_TROI';
                    break;
                }
                //duonghv13 add 08122021
                else if ($scope.trTypes[i].trTypeCode == 'Codien_HTCT' && $scope.trTypes[i].woTrTypeId == $scope.workingTR.trTypeId) {
                    $scope.workingTR.isVhkt = true;
                    $scope.workingTR.trTypeCode = 'Codien_HTCT';
                    break;
                }
                //duonghv13 end 08122021
            }
            if ($scope.workingTR.isTmbt) {
            	$scope.workingTR.cdLevel1 = $scope.cdLv1List[2].code;
                vm.isAutoCreateWo = false;
            } else if ($scope.workingTR.isDbht) {
                $scope.workingTR.cdLevel1 = TTHT_SYS_GROUP_ID;
                getCdLv2List();
                vm.isAutoCreateWo = false;
            } 
            //Huypq-10082021-start
            else if ($scope.workingTR.isVhkt) {
                $scope.workingTR.cdLevel1 = VHKT_SYS_GROUP_ID;
                vm.isAutoCreateWo = false;
            }
            //Huy-end
            else {
                $scope.workingTR.cdLevel1 = null;
                if ($scope.permissions.createTR && $scope.permissions.createTRDomainDataList.includes(XDDTHT_SYS_GROUP_ID)) {
                    vm.isAutoCreateWo = true;
                }
            }
        }

        function getCdLv2List() {
            var postData = {loggedInUser: $scope.loggedInUser};
            trManagementService.getCdLv2List(postData).then(
                function (resp) {
                    if (resp.data) $scope.cdLv2List = resp.data;
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function getCdLv2List() {
            var postData = {loggedInUser: $scope.loggedInUser};
            trManagementService.getCdLv2List(postData).then(
                function (resp) {
                    if (resp.data) $scope.cdLv2List = resp.data;
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        $scope.autoCompleteCdLevel2Options = {
            dataTextField: "code", placeholder: "Chọn cd level 2",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                $scope.workingTR.cdLevel2Name = data.groupName;
                $scope.workingTR.cdLevel2 = data.sysGroupId;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = $scope.workingTR.cdLevel2Name;
                        $scope.workingTR.cdLevel2 = null;
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
            }
        };

        function searchCdLevel2(keySearch) {
            var searchResult = [];
            for (var i = 0; i < $scope.cdLv2List.length; i++) {
                var lv2 = $scope.cdLv2List[i];
                //HienLT56 start 30102020
                var groupName = lv2.groupName.toUpperCase();
                if (keySearch == null) {
                    $scope.workingTR.cdLevel2Name = null;
                    $scope.workingTR.cdLevel2 = null;
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
                            var searchOptions = {};
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
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text',
                        template: function (dataItem) {
                            var branch = dataItem.branch
                            return branch ? branch : '';
                        }
                    },
                    {
                        title: "Tổng Số Trạm",
                        field: 'rentTotal',
                        editable: false,
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type: 'text',
                        template: function (dataItem) {
                            var rentTotal = dataItem.rentTotal
                            return rentTotal ? rentTotal : 0;
                        }
                    },
                    {
                        title: "Đã Thuê",
                        field: 'rentOk',
                        editable: false,
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type: 'text',
                        template: function (dataItem) {
                            var rentOk = dataItem.rentOk
                            return rentOk ? rentOk : 0;
                        }
                    },
                    {
                        title: "Không Thuê Được",
                        field: 'rentNg',
                        editable: false,
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type: 'text',
                        template: function (dataItem) {
                            var rentNg = dataItem.rentNg
                            return rentNg ? rentNg : 0;
                        }
                    },
                    {
                        title: "Đang Thuê",
                        field: 'rentProcessing',
                        editable: false,
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:right;"},
                        type: 'text',
                        template: function (dataItem) {
                            var rentProcessing = dataItem.rentProcessing
                            return rentProcessing ? rentProcessing : 0;
                        }
                    },
                    {
                        title: "Chưa Thuê",
                        field: 'rentNew',
                        editable: false,
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right; font-weight: bold;color: Blue;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            var rentNew = dataItem.rentNew!=null ? dataItem.rentNew : 0;
                            return '<div ng-click="vm.viewLstStations(\'' + dataItem.branch + '\')">' + rentNew + '</div>';
                        }
                    }
                    // ,
                    // {
                    //     title: "Trạm Giao Thuê",
                    //     field: 'rentTarget',
                    //     editable: true,
                    //     width: '15%',
                    //     headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    //     attributes: {style: "text-align:left;font-weight: bold;"},
                    //     template: function (dataItem) {
                    //         var rentTarget = dataItem.rentTarget
                    //         return rentTarget ? rentTarget : 0;
                    //     }
                    // }
                ]
            });
        }

        vm.changeRentTarget = function (id) {
            var inputValue = $('#rentTarget' + id).val();
            try {
                if (inputValue && parseInt(inputValue) > 0) {
                    for (var i = 0; i < vm.rentStationList.length; i++) {
                        if (vm.rentStationList[i].sysGroupId == id) {
                            vm.rentStationList[i].rentTarget = inputValue;
                            break;
                        }
                    }
                }
            } catch (e) {
                toastr.error('Có lỗi xảy ra');
            }
        };

        function validateRentStationList() {
            for (var i = 0; i < vm.rentStationList.length; i++) {
                if (vm.rentStationList[i].rentTarget > vm.rentStationList[i].rentNew) {
                    toastr.error('Số trạm giao chỉ tiêu không được lớn hơn số trạm chưa thuê !');
                    $('#rentTarget' + vm.rentStationList[i].sysGroupId).focus();
                    return false;
                }
            }
            return true;
        }

        vm.viewLstStations = viewLstStations;

        function viewLstStations(branch) {
            // vm.searchForm.status = status;
            // vm.searchForm.groupCreated = groupCreatedId;
        	vm.keySearchStation = null;
        	vm.branch = branch;
            $modal.open({
                templateUrl: 'coms/wo_xl/trManagement/listStations.html',
                controller: null,
                windowClass: '',
                scope: $scope
            })
                .result.then(
                function () {
                },
                function () {
                }
            )
            fillStationDataTbl(branch);
        }

        vm.doSearchStation = function doSearchStation() {
        	fillStationDataTbl(vm.branch);
        	$("#stationListTblId").data("kendoGrid").dataSource.read();
        }
        
        var stationRecord = 0;

        function fillStationDataTbl(branch) {
            vm.stationListOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: false,
                save: function () {
                    vm.stationListTbl.dataSource.read();
                },
                dataBinding: function () {
                    stationRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: false,
                    schema: {
                        data: function (response) {
                            vm.stationList = response.data;
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            //Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "trService/tmbt/getListStationByBranch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var searchOptions = {};
                            searchOptions.tmbtBranch = branch;
                            searchOptions.page = 1;
                            searchOptions.pageSize = 9999;
                            searchOptions.keySearch = vm.keySearchStation;
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
                            return ++stationRecord;
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
                        editable: false,
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text',
                        template: function (dataItem) {
                            var code = dataItem.code
                            return code ? code : '';
                        }
                    },
                    {
                        title: "Tên trạm",
                        field: 'name',
                        editable: false,
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text',
                        template: function (dataItem) {
                            var name = dataItem.name
                            return name ? name : '';
                        }
                    },
                    {
                        title: "Địa chỉ",
                        field: 'address',
                        editable: false,
                        width: '60%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text',
                        template: function (dataItem) {
                            var address = dataItem.address
                            return address ? address : '';
                        }
                    },
                    {
                        title: " ",
                        field: 'selected',
                        editable: false,
                        width: '5%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            var catStationId = dataItem.catStationId;
                            var tmbtSysGroupId = dataItem.tmbtSysGroupId;
                            var checked = dataItem.isCheck == 1 ? 'checked' : '';
                            return '<input type="checkbox" value="' + catStationId + '" ' + checked + ' ' +
                                'ng-click="vm.toggleSelectStation($event, ' + catStationId + ', ' + tmbtSysGroupId + ', \'' + dataItem.code + '\')">';
                        },
                    }
                ]
            });
        }

        vm.toggleSelectStation = function (e, catStationId, tmbtSysGroupId, code) {
            vm.lstStationCodes = '';
            var postData = {
                isCheck: e.currentTarget.checked ? 1 : 0
                , catStationId: catStationId
                , tmbtTrId: 123
                , tmbtSysGroupId: tmbtSysGroupId
                , code: code
            };

            if (e.currentTarget.checked) { // Add vào list
                vm.selectedStations.push(postData);
            } else { // Remove khỏi list
                for (var i = 0; i < vm.selectedStations.length; i++) {
                    if (vm.selectedStations[i].tmbtSysGroupId == tmbtSysGroupId && vm.selectedStations[i].catStationId == catStationId) {
                        vm.selectedStations.splice(i, 1);
                    }
                }
            }

            for (var i = 0; i < vm.selectedStations.length; i++) {
                vm.lstStationCodes += vm.selectedStations[i].code + ", ";
            }
        }

        //Huypq-090720210-start
        vm.importTrTMBT = importTrTMBT;
		function importTrTMBT(){
			var teamplateUrl="coms/wo_xl/trManagement/importTrTmbt.html";
			var title = "Import TR Thuê mặt bằng trạm";
			vm.fileImportDataWi = null;
			var windowId = "IMPORT_TR_TMBT";
			CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '275');
		}
		  
		vm.getExcelTemplateImportWi= function () {
			  kendo.ui.progress($("#shipmentWi"), true);
            return Restangular.all("trService/tmbt/getExcelTemplate").post({}).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                kendo.ui.progress($("#shipmentWi"), false);
            }).catch(function (e) {
          	  kendo.ui.progress($("#shipmentWi"), false);
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                return;
            });
		  }
		
		vm.fileImportDataWi = false;
		vm.submitImportTrTMBT = submitImportTrTMBT;
		  function submitImportTrTMBT() {
			  var groupCreated = $scope.permissions.createTRDomainDataList.split(',')[0];
			  var groupCreatedName = 'Trung tâm Đầu tư hạ tầng';
              if (groupCreated == TTHT_SYS_GROUP_ID) groupCreatedName = 'Trung Tâm Hạ Tầng';
              if (groupCreated == GPTH_SYS_GRPUP_ID) groupCreatedName = 'Trung tâm Giải pháp tích hợp';
              if (groupCreated == XDDTHT_SYS_GROUP_ID) groupCreatedName = 'Trung tâm Đầu tư hạ tầng';
              if (groupCreated == VHKT_SYS_GROUP_ID) groupCreatedName = 'Trung Tâm Vận Hành Khai Thác';
              if (groupCreated == TTXDD_SYS_GROUP_ID) groupCreatedName = 'Trung tâm Xây dựng dân dụng';
			  
	            $('#testSubmitWi').addClass('loadersmall');
	            vm.disableSubmit = true;
	            if (!vm.fileImportDataWi) {
	                $('#testSubmitStation').removeClass('loadersmall');
	                vm.disableSubmit = false;
	                toastr.warning("Bạn chưa chọn file để import");
	                return;
	            }
	            if ($('.k-invalid-msg').is(':visible')) {
	                $('#testSubmitWi').removeClass('loadersmall');
	                vm.disableSubmit = false;
	                return;
	            }
	            if (vm.fileImportDataWi.name.split('.').pop() !== 'xls' && vm.fileImportDataWi.name.split('.').pop() !== 'xlsx') {
	                $('#testSubmitWi').removeClass('loadersmall');
	                vm.disableSubmit = false;
	                toastr.warning("Sai định dạng file");
	                return;
	            }
	            var formData = new FormData();
	            formData.append('multipartFile', vm.fileImportDataWi);
	            return $.ajax({
	                url: Constant.BASE_SERVICE_URL + "trService/tmbt/importFileTrTmbt?folder=" + Constant.UPLOAD_FOLDER_TYPE_TEMP + 
	                "&createTRDomainDataList=" + $scope.permissions.createTRDomainDataList.split(",")[0],
	                type: "POST",
	                data: formData,
	                enctype: 'multipart/form-data',
	                processData: false,
	                contentType: false,
	                cache: false,
	                success: function (data) {
	                    if (data == 'NO_CONTENT') {
	                        toastr.warning("File import không có nội dung");
	                        $('#testSubmitWi').removeClass('loadersmall');
	                        vm.disableSubmit = false;
	                        return;
	                    }
	                    if (data == 'CONFLICT') {
	                        toastr.warning("File import chứa bản ghi đã tồn tại");
	                        $('#testSubmitWi').removeClass('loadersmall');
	                        vm.disableSubmit = false;
	                        return;
	                    }
	                    if (data == 'NOT_ACCEPTABLE') {
	                        toastr.error("Công trình đã thuộc hợp đồng đầu ra khác!");
	                        $('#testSubmitWi').removeClass('loadersmall');
	                        vm.disableSubmit = false;
	                    } else if (!!data.error) {
	                        $('#testSubmitWi').removeClass('loadersmall');
	                        vm.disableSubmit = false;
	                        toastr.error(data.error);
	                    } else if (data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length > 0) {
	                        vm.lstErrImport = data[data.length - 1].errorList;
	                        vm.objectErr = data[data.length - 1];
	                        var templateUrl = "wms/createImportRequirementManagement/importResultPopUp.html";
	                        var title = "Kết quả Import";
	                        var windowId = "ERR_IMPORT";
	                        $('#testSubmitWi').removeClass('loadersmall');
	                        vm.disableSubmit = false;
	                        CommonService.populatePopupCreate(templateUrl, title, vm.lstErrImport, vm, windowId, false, '80%', '420px');
	                        fillDataImportErrTable(vm.lstErrImport);
	                    } else {
	                        $('#testSubmitWi').removeClass('loadersmall');
	                        vm.disableSubmit = false;
	                        toastr.success("Import thành công")
	                        $("#appParamGridStation").data('kendoGrid').dataSource.read();
	                        $("#appParamGridStation").data('kendoGrid').refresh();
	                        $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
	                    }
	                }
	            });
	        }
		  
		  vm.exportExcelErr = function () {
			  trManagementService.downloadErrorExcel(vm.objectErr).then(function (d) {
	                data = d.plain();
	                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
	            }).catch(function () {
	                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
	                return;
	            });
	        }
	        vm.closeErrImportPopUp = function () {
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
	                    }, {
	                        title: "Dòng lỗi",
	                        field: 'lineError',
	                        width: 100,
	                        headerAttributes: {
	                            style: "text-align:center;"
	                        },
	                        attributes: {
	                            style: "text-align:center;"
	                        },
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

        //Huypq-end
        
        // end controller
    }
})();
