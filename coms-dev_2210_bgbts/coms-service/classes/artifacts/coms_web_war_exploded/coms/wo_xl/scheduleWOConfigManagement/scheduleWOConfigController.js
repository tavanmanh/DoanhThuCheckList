(function () {
    'use strict';
    var controllerId = 'scheduleWOConfigController';

    angular.module('MetronicApp').controller(controllerId, scheduleWorkItemManagementController, '$scope', '$modal', '$rootScope');

    function scheduleWorkItemManagementController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                                  kendoConfig, $kWindow, scheduleWOConfigService, htmlCommonService,
                                                  CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modal) {

        var vm = this;
        vm.String = "Cấu hình > Cấu hình công việc định kỳ";
        vm.dataResult = [];
        vm.searchForm = {};
        $scope.workItemForm = {};
        $scope.workingWIConfig = {};
        $scope.loggedInUser = $rootScope.casUser.userName;
        $scope.date = new Date();
        vm.validateWorkItemName = null;
        vm.validateWorkItemCode = null;
        vm.state = 1;
        vm.cycleType = 1;
        vm.validateEndDate = null;
        var startDateNew = "";

        $scope.ctOrPj = {};
        $scope.workItemList = {};
        $scope.cdLv2List = {};
        $scope.permissions = {};
        $scope.isEditing = false;
        $scope.isDisable = false;
        $scope.cycleTypeList = [
            {value: 1, name: 'Ngày'},
            {value: 2, name: 'Tuần'},
            {value: 3, name: 'Tháng'},
            {value: 4, name: 'Năm'}];
        $scope.woTypes = [];
        
        $scope.statusList = [
            {value: 1, name: 'Bật'},
            {value: 0, name: 'Tắt'},];


        init();

        function init() {
            fillDataTable();
            getCdLv2List();
            $scope.ctOrPj.isState = true;
        }

        $scope.selectOn = function () {
            vm.state = '1';
        };

        $scope.selectOff = function () {
            vm.state = '0';
        };

        function getWorkItemList() {


            var objSearch = {};
            return Restangular.all("woService/wi/doSearchWorkItem").post(objSearch).then(
                function (response) {
                    $scope.workItemList = response.data;
                }
            ).catch(
                function (err) {
                    toastr.success("Có lỗi xảy ra!");
                }
            );
        }

        vm.doSearch = doSearch;

        function doSearch() {
            var grid = vm.workItemConfigListTable;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                })
            }
        }

        vm.createConfigWorkItem = createConfigWorkItem;

        function createConfigWorkItem() {
            $scope.workingWIConfig = {};
            $scope.isDisable = false;
            $scope.isEditing = false;
            $scope.workingWIConfig.cdLevel1Name = "TTVHKT - Trung tâm vận hành khai thác";
            var start = new Date($scope.date.getFullYear(), $scope.date.getMonth(), $scope.date.getDate());
            var end = new Date($scope.date.getFullYear(), $scope.date.getMonth(), $scope.date.getDate() + 1);
            $scope.workingWIConfig.endTime = htmlCommonService.formatDate(end);
            $scope.workingWIConfig.startTime = htmlCommonService.formatDate(start);
            getWorkItemList();
            getWoTypes();
            $modal.open({
                templateUrl: 'coms/wo_xl/scheduleWOConfigManagement/scheduleWOConfigCreateEditModal.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function () {
                    vm.createNewConfigWorkItem($scope.workingWIConfig);
                },
                function () {
                    $scope.workingWIConfig = {};
                }
            )
        }

        function validateCreateConfigWorkItem(workingWIConfig) {
            if (!workingWIConfig.endTime) {
                vm.validateEndDate = "Ngat ket thuc khong ok!";
            } else {
                vm.validateEndDate = null;
            }
        }

        vm.createNewConfigWorkItem = function (obj) {
            var model = {
                scheduleConfigId: 0,
                scheduleConfigName: obj.scheduleConfigName,
                scheduleConfigCode: obj.scheduleConfigCode,
                userCreated: $scope.loggedInUser,
                status: 1,
                startTime: obj.startTime,
                endTime: obj.endTime,
                cycleLength: obj.cycleLength,
                state: vm.state,
                cycleType: obj.value,
                scheduleWorkItemId: obj.woWorkItemId,
                quotaTime: obj.quotaTime,
                trId: obj.trId,
                woTime: obj.startTime,
                cdLevel1: 270120,
                cdLevel1Name: "TTVHKT - Trung tâm vận hành khai thác",
                cdLevel2: obj.cdLevel2,
                cdLevel2Name: obj.cdLevel2Name,
                woTRCode: obj.trCode,
                woTypeId: obj.woTypeId,
                woNameId: obj.woNameId
            };
            validateCreateConfigWorkItem(obj);
            // if(!vm.validateWorkItemName && !vm.validateWorkItemCode)
            scheduleWOConfigService.createNewConfigWorkItem(model).then(
                function (resp) {
                    if (resp && resp.statusCode == 1) {
                        toastr.success("Thêm mới thành công!");
                        vm.searchForm.scheduleConfigName = null;
                        vm.workItemConfigListTable.dataSource.read();
                    } else toastr.error("Thêm mới thất bại! " + resp.message);

                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        // lay ma yeu cau autoCompleteTr
        $scope.autoCompleteTrOptions = {
            dataTextField: "trCode", placeholder: "Thông tin TR",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                $scope.workingWIConfig.trId = data.trId;
                $scope.workingWIConfig.trCode = data.trCode;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = $scope.workingWIConfig.trCode;

                        if (keySearch == '') {
                            $scope.workingWIConfig.trId = null;
                            $scope.workingWIConfig.trCode = null;
                        }

                        $scope.workingWIConfig.trId = null;

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

        // lay cd level 2
        $scope.autoCompleteCdLevel2Options = {
            dataTextField: "code", placeholder: "Chọn cd level 2",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                $scope.workingWIConfig.cdLevel2Name = data.groupName;
                $scope.workingWIConfig.cdLevel2 = data.sysGroupId;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = $scope.workingWIConfig.cdLevel2Name;
                        if (keySearch == '') {
                            $scope.workingWIConfig.cdLevel2Name = null;
                            $scope.workingWIConfig.cdLevel2 = null;
                            return options.success([]);
                        }
                        $scope.workingWIConfig.cdLevel2 = null;
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
        
        $scope.autoCompleteCdLevel2OptionsSearch = {
                dataTextField: "code", placeholder: "Chọn cd level 2",
                open: function (e) {
                },
                select: function (e) {
                    data = this.dataItem(e.item.index());
                    $scope.vm.searchForm.cdLevel2Name = data.groupName;
                    $scope.vm.searchForm.cdLevel2 = data.sysGroupId;
                    $scope.$apply();
                },
                pageSize: 10,
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function (options) {
                            var keySearch = $scope.vm.searchForm.cdLevel2Name;
                            if(keySearch == ''){
                                $scope.vm.searchForm.cdLevel2Name = null;
                                $scope.vm.searchForm.cdLevel2 = null;
                                return options.success([]);
                            }
                            $scope.vm.searchForm.cdLevel2 = null;
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
                keySearch = keySearch.toUpperCase();
                var groupName = lv2.groupName.toUpperCase();
                if (groupName.includes(keySearch)) searchResult.push(lv2);
            }
            return searchResult;
        }

        function getCdLv2List() {
            var postData = {loggedInUser: $scope.loggedInUser};

            if ($scope.workingWIConfig.cdLevel1 == $scope.vhktSysGroupId) {
                scheduleWOConfigService.getVhktCdLv2VList(postData).then(
                    function (resp) {
                        if (resp.data) $scope.cdLv2List = resp.data;
                    },
                    function (error) {
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            } else {
                scheduleWOConfigService.getCdLv2List(postData).then(
                    function (resp) {
                        if (resp.data) $scope.cdLv2List = resp.data;
                    },
                    function (error) {
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            }
        }

        vm.searchWI = searchWI;

        function searchWI() {
            var grid = vm.workItemConfigListTable;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                })
            }
        }

        vm.exportexcel = function () {
            function displayLoading(target) {
                var element = $(target);
                kendo.ui.progress(element, true);
                vm.searchForm.userCreated = $scope.loggedInUser;
                vm.searchForm.cdLevel1 = $scope.cdLevel1Permission;
                vm.searchForm.cdLevel1Name = $scope.cdLevel1PermissionName;
                vm.searchForm.loggedInUser = $scope.loggedInUser;
                vm.searchForm.sysUserId = $scope.sysUserId;
                setTimeout(function () {
                    return Restangular.all("woService/exportexcelScheduleConfig").post(vm.searchForm).then(function (d) {
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
//      taotq start 05052021
        vm.exportConfigWorkItemList = function () {
            confirm("Xuất dữ liệu excel?", function () {
                $http({
                    url: Constant.BASE_SERVICE_URL + "woService/exportConfigWorkItemList",
                    method: "POST",
                    data: vm.searchForm,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function (data, status, headers, config) {
                	htmlCommonService.saveFile(data, "Export_WO_Config_Work_ItemS.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                }).error(function (data, status, headers, config) {
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                });
            })
        }
//      taotq end 05052021
        vm.importConfig = importConfig;

        function importConfig() {
            vm.fileImportData = false;
            vm.searchForm.type = 1;
            var teamplateUrl = "coms/wo_xl/scheduleWOConfigManagement/scheduleWOConfigImportModal.html";
            var title = "IMPORT";
            var windowId = "IMPORT_WO";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '275', null);
        }

        vm.onSelectFile = onSelectFile;

        function onSelectFile() {
            if ($("#fileImport")[0].files[0].name.split('.').pop() != 'xls' && $("#fileImport")[0].files[0].name.split('.').pop() != 'xlsx') {
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
                if (104857600 < $("#fileImport")[0].files[0].size) {
                    toastr.warning("Dung lượng file vượt quá 100MB! ");
                    return;
                }
                $("#fileName")[0].innerHTML = $("#fileImport")[0].files[0].name
            }
        }

        function getImportFTResult(obj) {
            $http({
                url: Constant.BASE_SERVICE_URL + "woService/wo/getImportScheduleConfigResult",
                method: "POST",
                data: obj,
                headers: {
                    'Content-type': 'application/json'
                },
                responseType: 'arraybuffer'
            }).success(function (data, status, headers, config) {
                htmlCommonService.saveFile(data, "Ket_qua_import.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            }).error(function (data, status, headers, config) {
                toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
            });
        }

        vm.cancelImport = cancelImport;

        function cancelImport() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

        vm.importFile = function () {
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

                    var woTypeHeader = ['woTypeId', 'woTypeCode', 'woTypeName'];
                    var woTypeList = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[1]], {
                        header: woTypeHeader,
                        range: 1
                    });

                    var workSrcHeader = ['apWorkSrc', 'apWorkSrcName'];
                    var workSrcList = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[2]], {
                        header: workSrcHeader,
                        range: 1
                    });

                    var apConsTypeHeader = ['apConstructionType', 'apConstructionTypeName'];
                    var apConsList = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[3]], {
                        header: apConsTypeHeader,
                        range: 1
                    });

                    var woNameHeader = ['woNameId', 'woName'];
                    var woNameList = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[4]], {
                        header: woNameHeader,
                        range: 1
                    });

                    var trHeader = ['trId', 'trCode'];
                    var trList = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[5]], {
                        header: trHeader,
                        range: 1
                    });

                    var workItemHeader = ['consTypeName', 'catWorkItemTypeName', 'catWorkItemTypeId', 'catWorkItemTypeCatCon'];
                    var workItemList = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[6]], {
                        header: workItemHeader,
                        range: 1
                    });

                    var cdLevel2Header = ['cdLevel2', 'cdLevel2Name'];
                    var cdLevel2List = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[7]], {
                        header: cdLevel2Header,
                        range: 1
                    });

                    var importHeader = ["woTypeName", "apWorkSrcName", "apConstructionTypeName", "woName", "totalMonthPlanId", "trCode", "projectCode", "contractCode", "constructionCode", "stationCode", "catWorkItemTypeName",
                        "moneyValue", "finishDate", "qoutaTime", "cdLevel2Name", "moneyFlowBill", "moneyFlowDate", "moneyFlowValue", "moneyFlowRequired", "moneyFlowContent"];

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[0]], {
                        header: importHeader,
                        range: 2
                    });

                    for (var i = 0; i < XL_row_object.length; i++) {
                        var wo = XL_row_object[i];

                        Object.keys(wo).forEach(function (key) {
                            if (key == 'undefined') {
                                toastr.error('Cấu trúc file excel không hợp lệ.');
                                return;
                            }
                        });


                        if (wo.woTypeName) {
                            wo.woTypeId = getValueByKeys(woTypeList, 'woTypeName', wo.woTypeName, 'woTypeId');
                            wo.woTypeCode = getValueByKeys(woTypeList, 'woTypeName', wo.woTypeName, 'woTypeCode');
                        }

                        if (wo.apWorkSrcName) {
                            wo.apWorkSrc = getValueByKeys(workSrcList, 'apWorkSrcName', wo.apWorkSrcName, 'apWorkSrc');
                        }

                        if (wo.apConstructionTypeName) {
                            wo.apConstructionType = getValueByKeys(apConsList, 'apConstructionTypeName', wo.apConstructionTypeName, 'apConstructionType');
                        }

                        if (wo.woName) {
                            wo.woNameId = getValueByKeys(woNameList, 'woName', wo.woName, 'woNameId');
                        }

                        if (wo.trCode) {
                            wo.trId = getValueByKeys(trList, 'trCode', wo.trCode, 'trId');
                        }

                        wo.totalMonthPlanId = wo.totalMonthPlanId ? wo.totalMonthPlanId.match(/\d+/)[0].trim() : null;

                        if (wo.catWorkItemTypeName) {
                            wo.catWorkItemTypeId = getValueByKeys(workItemList, 'catWorkItemTypeName', wo.catWorkItemTypeName, 'catWorkItemTypeId');
                            wo.catWorkItemTypeCatCon = getValueByKeys(workItemList, 'catWorkItemTypeName', wo.catWorkItemTypeName, 'catWorkItemTypeCatCon');
                        }

                        if (wo.cdLevel2Name) {
                            wo.cdLevel2 = getValueByKeys(cdLevel2List, 'cdLevel2Name', wo.cdLevel2Name, 'cdLevel2');
                        }

                        wo.userCreated = $scope.loggedInUser;
                        wo.cdLevel1 = $scope.cdLevel1Permission;
                        wo.cdLevel1Name = $scope.cdLevel1PermissionName;
                        wo.loggedInUser = $scope.loggedInUser;
                        wo.sysUserId = $scope.sysUserId;
                        wo.sysGroupId = $scope.sysUserGroup.sysGroupId;
                        wo.woId = 0;
                        wo.status = 1;
                        wo.woTypeCode = getWoTypeCode(wo.woTypeId);
                        wo.moneyValue = wo.moneyValue * 1000000;
                        wo.createdUserFullName = $rootScope.casUser.fullName;
                        wo.createdUserEmail = $rootScope.casUser.email;
                        if (wo.moneyFlowValue) wo.moneyFlowValue = wo.moneyFlowValue * 1000000;
                        if (wo.moneyFlowRequired) wo.moneyFlowRequired = wo.moneyFlowRequired * 1000000;

                        //wo từ cnkt tạo
                        if ($scope.permissions.crudWOCnkt) {
                            wo.type = 1;
                            wo.state = 'ACCEPT_CD';
                        }

                        wo = trimProps(wo);

                    }

                    //console.log(XL_row_object);
                    if (XL_row_object.length == 0) {
                        toastr.error('File excel không hợp lệ.');
                        return;
                    }

                    var json_object = JSON.stringify(XL_row_object);
                    addDataFormExcel(json_object);
                };
                reader.onerror = function (event) {
                    console.error("File could not be read! Code " + event.target.error.code);
                };
                reader.readAsBinaryString(selectedFile);
            });
        };

        vm.addDataFormExcel = addDataFormExcel;

        function addDataFormExcel(obj) {
            woManagementService.createManyWo(obj).then(function (res) {
                if (res && res.statusCode == 1) {
                    vm.dataWOListTable.dataSource.read();
                    toastr.success("Import dữ liệu thành công. Số bản ghi đã import: " + res.message);
                    confirm('Import dữ liệu thành công! Bạn có muốn xem chi tiết?', function () {
                        getImportExcelResult(res.data)
                    });
                } else {
                    toastr.error("Phát hiện dữ liệu không hợp lệ!");
                    confirm('Phát hiện dữ liệu không hợp lệ! Bạn có muốn xem chi tiết?', function () {
                        getImportExcelResult(res.data)
                    });
                }
            }, function (error) {
                toastr.error("Không thành công! ");
            })
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
            if ($("#fileImport")[0].files[0].name.split('.').pop() != 'xls' && $("#fileImport")[0].files[0].name.split('.').pop() != 'xlsx') {
                toastr.warning("Sai định dạng file");
                return;
            }
            var formData = new FormData();
            formData.append('multipartFile', $('#fileImport')[0].files[0]);
            $('#loadding').show();
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "woService/importFileScheduleWoConfig",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if (data && data.statusCode == 1) {
                        toastr.success("Import thành công! ");
                        doSearch();
                        cancelImport();
                    } else {
                        toastr.error("Import thất bại! " + data.message ? data.message : '');
                        if (data.data != null) {
                            confirm('Phát hiện dữ liệu không hợp lệ! Bạn có muốn xem chi tiết?', function () {
                                getImportFTResult(data.data)
                            });
                        }
                        cancelImport();
                    }
                    cancelImport();
                    $scope.$apply();
                }
            });
        }

        vm.deleteConfigWorkItem = deleteConfigWorkItem;

        function deleteConfigWorkItem(str) {
            var idDate = str.split('-');
            var scheduleConfigId = idDate[0];
            var startTime = idDate[1];
            var param = {
                scheduleConfigId: scheduleConfigId
            }
            var flag = false;
            var splitted = startTime.split('/');
            var start = splitted[1].concat('/').concat(splitted[0]).concat('/').concat(splitted[2]);
            var spl = htmlCommonService.formatDate($scope.date).split('/');
            var currentDate = spl[1].concat('/').concat(spl[0]).concat('/').concat(spl[2]);
            if (Date.parse(start) < Date.parse(currentDate)) {
                flag = true;
                toastr.error("Cấu hình công viếc đã bắt đầu không thể xóa");
            }
            if (flag == false) {
                confirm('Bạn có muốn xoá cấu hình không?', function () {
                    scheduleWOConfigService.deleteWIConfig(param).then(function (res) {

                        if (res && res.statusCode == 1) toastr.success("Xóa dữ liệu thành công!");
                        else toastr.error(res.message);
                        vm.workItemConfigListTable.dataSource.read();
                    }, function (errResponse) {
                        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi xoá!"));
                    });
                })
            }
        }

        vm.editConfigWorkItem = function (scheduleConfigId) {
            $scope.woTypes = [];
            $scope.isDisable = false;
            $scope.isEditing = true;
            getWoTypes();
            var param = {
                scheduleConfigId: scheduleConfigId
            }
            scheduleWOConfigService.getOneWIConfig(param).then(
                function (resp) {
                    if (resp && resp.data) {
                        getWorkItemList();
                        $scope.workingWIConfig = resp.data;
                        $scope.workingWIConfig.cdLevel1Name = "TTVHKT - Trung tâm vận hành khai thác";
                        $scope.workingWIConfig.woWorkItemId = resp.data.scheduleWorkItemId;
                        $scope.workingWIConfig.trId = resp.data.trId;
                        $scope.workingWIConfig.trCode = resp.data.woTRCode;
                        $scope.workingWIConfig.value = resp.data.cycleType;
                        $scope.workingWIConfig.woTypeId = resp.data.woTypeId;
                        selectWoType();
                        $scope.workingWIConfig.woNameId = resp.data.woNameId;

                        // Kiem tra neu cau hinh da chay roi thi ko duoc edit
                        var splitted = $scope.workingWIConfig.startTime.split(' ');
                        startDateNew = splitted[0];
//                        if (startDateNew <= htmlCommonService.formatDate($scope.date)) {
//                            $scope.isDisable = true;
//                        }
                    }
                    $modal.open({
                        templateUrl: 'coms/wo_xl/scheduleWOConfigManagement/scheduleWOConfigCreateEditModal.html',
                        controller: null,
                        windowClass: 'app-modal-window',
                        scope: $scope
                    })
                        .result.then(
                        function () {
                            vm.saveConfigWorkItem($scope.workingWIConfig);
                        },
                        function () {
                            $scope.workingWIConfig = {};
                        }
                    )
                },
                function (error) {
                    toastr.success("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )

        }

        vm.saveConfigWorkItem = function (obj) {
            var model = {
                scheduleConfigId: obj.scheduleConfigId,
                scheduleConfigName: obj.scheduleConfigName,
                scheduleConfigCode: obj.scheduleConfigCode,
                userCreated: $scope.loggedInUser,
                status: 1,
                startTime: obj.startTime,
                endTime: obj.endTime,
                createdDate: obj.createdDate,
                cycleLength: obj.cycleLength,
                state: vm.state,
                cycleType: obj.value,
                scheduleWorkItemId: obj.woWorkItemId,
                quotaTime: obj.quotaTime,
                trId: obj.trId,
                woTime: obj.startTime,
                cdLevel1: 270120,
                cdLevel1Name: "TTVHKT - Trung tâm vận hành khai thác",
                cdLevel2: obj.cdLevel2,
                cdLevel2Name: obj.cdLevel2Name,
                woTRCode: obj.trCode,
                woTypeId: obj.woTypeId,
                woNameId: obj.woNameId
            };
            scheduleWOConfigService.updateWIConfig(model).then(
                function (resp) {
                    if (resp && resp.statusCode == 1) toastr.success("Sửa thành công!");
                    else toastr.success(resp.message);

                    vm.workItemConfigListTable.dataSource.read();
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        var record = 0;

        vm.viewWIDetails = function (str) {
            var splitted = str.split('-');
            var id = splitted[0]
            var splitted1 = str.split((splitted[0].concat('-')));
            var name = splitted1[1]
            var template = Constant.getTemplateUrl('WO_XL_SCHEDULE_WORK_ITEM_DETAILS');
            $rootScope.viewDetailsWoId = id;
            $rootScope.viewDetailsWoCode = name;
            // template.woWorkItemId = id;
            // template.workItemCode = name;
            postal.publish({
                channel: "Tab",
                topic: "open",
                data: template
            });

            postal.publish({
                channel: "Tab",
                topic: "action",
                data: {action: 'refresh', workItemId: id}
            });
        };

        function createEditDeleteTemplate(dataItem) {
            var xx = dataItem.scheduleConfigId + '-' + dataItem.startTimeString;
            var template = '<div class="display-block cedtpl" style="text-align: center">' +
                // '<i class="fa fa-list-alt icon-table" ng-click="vm.viewWIDetails(\''+xxx+'\')"></i>' +
                '<i ng-click="vm.editConfigWorkItem(' + dataItem.scheduleConfigId + ')" class="fa fa-pencil icon-table"></i>';

            //không cho xóa config nữa - có thể tắt config đi = chức năng edit
            //'<i ng-click="vm.deleteConfigWorkItem(\''+xx+'\')" class="fa fa-trash-o icon-table"></i></div>';
            return template;
        }

        function fillDataTable() {

            vm.workItemConfigListTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                save: function () {
                    vm.workItemConfigListTable.refresh();
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
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service lay data tim kiem /wi/doSearchWIConfig
                            url: Constant.BASE_SERVICE_URL + "woService/wo/doSearchWIConfig",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page
                            vm.searchForm.pageSize = options.pageSize
                            return JSON.stringify(vm.searchForm)
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
                        empty: ''
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
                        title: "Mã cấu hình công việc định kỳ",
                        field: 'scheduleConfigCode',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.scheduleConfigCode;
                        },
                    },
                    {
                        title: "Tên cấu hình công việc định kỳ",
                        field: 'scheduleConfigName',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.scheduleConfigName;
                        },
                    },
                    {
                        title: "Mã TR",
                        field: 'woTRCode',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.woTRCode ? dataItem.woTRCode : ' ';
                        },
                    },
                    {
                        title: "Ngày bắt đầu",
                        field: 'startTime',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.startTimeString ? dataItem.startTimeString : '';
                        }
                    },
                    {
                        title: "Ngày kết thúc",
                        field: 'endTime',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.endTimeString ? dataItem.endTimeString : '';
                        }
                    },
                    {
                        title: "Trạng thái",
                        field: 'deadLine',
                        width: '10%',
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
                        title: "CD level 1",
                        field: 'cdLevel1Name',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.cdLevel1Name ? dataItem.cdLevel1Name : '';
                        }
                    },
                    {
                        title: "CD level 2",
                        field: 'cdLevel2Name',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.cdLevel2Name ? dataItem.cdLevel2Name : '';
                        }
                    },
                    {
                        title: "Thao tác",
                        // field: 'sysGroupName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return createEditDeleteTemplate(dataItem);
                        }
                    },
                ]
            });
        }

        function getTrStateText(state) {
            var text = '';
            if (state == 1)
                return text = 'Bật';
            else
                return text = 'Tắt';
        }

        vm.getTemplateFile = function () {
            confirm('Bạn có muốn tải file mẫu?', function () {
                var obj = {loggedInUser: $scope.loggedInUser}
                $http({
                    url: Constant.BASE_SERVICE_URL + "trService/trType/getTrTypeImportTemplate",
                    method: "POST",
                    data: obj,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                })
                    .success(function (data, status, headers, config) {
                        htmlCommonService.saveFile(data, "TrTypeCreateExample.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    })
                    .error(function (data, status, headers, config) {
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    });
            })
        }

        $scope.validateCreateNew = function () {
            var workingWIConfig = $scope.workingWIConfig;

            if (!workingWIConfig.scheduleConfigName) {
                toastr.error("Tên cấu hình công việc định kỳ không được để trống.");
                return false;
            }

            if (!workingWIConfig.scheduleConfigCode) {
                toastr.error("Mã cấu hình công công việc định kỳ không được để trống");
                return false;
            }

            if (!workingWIConfig.cycleLength) {
                toastr.error("Thời gian chu kỳ không được để trống");
                return false;
            }

            if (!workingWIConfig.startTime) {
                toastr.error("Ngày bắt đầu không được để trống");
                return false;
            }

            if (!workingWIConfig.endTime) {
                toastr.error("Ngày kết thúc không được để trống");
                return false;
            }

            if (!workingWIConfig.quotaTime) {
                toastr.error("Định mức thời gian được để trống");
                return false;
            }

            if (!workingWIConfig.woWorkItemId) {
                toastr.error("Công việc định kỳ không được để trống");
                return false;
            }

            if (!workingWIConfig.trCode) {
                toastr.error("Mã Yêu Cầu (TR) không được để trống");
                return false;
            }

            if (!workingWIConfig.cdLevel2Name) {
                toastr.error("CD Level 2 không được để trống");
                return false;
            }

            if (!workingWIConfig.value) {
                toastr.error("Loại chu kỳ không được để trống");
                return false;
            }

            if (!workingWIConfig.woTypeId) {
                toastr.error("Loại WO không được để trống.");
                return false;
            }

            if (!workingWIConfig.woNameId) {
                toastr.error("Tên WO không được để trống.");
                return false;
            }
            var splitted = workingWIConfig.startTime.split('/');
            var start = splitted[1].concat('/').concat(splitted[0]).concat('/').concat(splitted[2]);
            var spl = htmlCommonService.formatDate($scope.date).split('/');
            var currentDate = spl[1].concat('/').concat(spl[0]).concat('/').concat(spl[2]);
            if (Date.parse(start) < Date.parse(currentDate)) {
                toastr.error("Ngày bắt đầu không thể nhỏ hơn ngày hiện tại");
                return false;
            }
            var splitt = workingWIConfig.endTime.split('/');
            var end = splitt[1].concat('/').concat(splitt[0]).concat('/').concat(splitt[2]);
            if (Date.parse(end) <= Date.parse(currentDate)) {
                toastr.error("Ngày kết thúc không thể nhỏ hơn ngày hiện tại");
                return false;
            }
            if (Date.parse(start) > Date.parse(end)) {
                toastr.error("Ngày bắt đầu không thể lớn hơn ngày kết thúc");
                return false;
            }
            if ($scope.workingWIConfig.cdLevel2 == null) {
                toastr.error("Mã CD level 2 chọn không tương ứng dữ liệu");
                return false;
            }
            if ($scope.workingWIConfig.trId == null) {
                toastr.error("Mã TR chọn không tương ứng dữ liệu");
                return false;
            }

            return true;
        }

        vm.exportTrTypeList = function () {
            confirm("Xuất dữ liệu excel?", function () {
                $http({
                    url: Constant.BASE_SERVICE_URL + "trService/trType/exportTrTypeExcel",
                    method: "POST",
                    data: vm.searchForm,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function (data, status, headers, config) {
                    htmlCommonService.saveFile(data, "Danh_sach_loai_tr.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                }).error(function (data, status, headers, config) {
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                });
            })
        }

        function getWoTypes(callback) {
            $scope.woTypes = [];
            scheduleWOConfigService.getWOTypes().then(
                function (resp) {
                    if (resp.data) {
                        resp.data.filter(function (value) {
                            if (value.woTypeCode.includes('NLMT')|| value.woTypeCode.includes('BDTHTCT')) {//DUONGHV13 ADD 07122021
                                $scope.woTypes.push(value);
                            }
                        });
                        if (callback) callback();
                    }
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        $scope.selectWoType = selectWoType;

        function selectWoType() {
            $scope.selectedWoType = {};

            // Them moi thi reset form
            if (!$scope.isEditting) {
                // $scope.workingWO.apWorkSrc = null;
                // $scope.workingWO.apWorkSrcName = null;
                // $scope.workingWO.apConstructionType = null;
                // $scope.workingWO.apConstructionTypeNull = null;
                // if (!$scope.isWoFromTr) {
                //     $scope.workingWO.trId = null;
                //     $scope.workingWO.trCode = null;
                // }
            }

            if (!$scope.workingWIConfig.woTypeId || $scope.workingWIConfig.woTypeId == '') return;

            var obj = {page: 1, pageSize: 99999, woTypeId: $scope.workingWIConfig.woTypeId};

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

            for (var i = 0; i < $scope.woTypes.length; i++) {
                if ($scope.woTypes[i].woTypeId == $scope.workingWIConfig.woTypeId) {
                    $scope.workingWIConfig.woTypeCode = $scope.woTypes[i].woTypeCode;
                    $scope.selectedWoType = $scope.woTypes[i];
                }
            }
        }

        // end controller
    }
})();
