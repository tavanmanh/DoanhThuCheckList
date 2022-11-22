(function () {
    'use strict';
    var controllerId = 'woTypeManagementController';

    angular.module('MetronicApp').controller(controllerId, woTypeManagementController, '$modal');

    function woTypeManagementController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                    kendoConfig, $kWindow, woTypeManagementService, htmlCommonService, vpsPermissionService,
                                    CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modal) {

        var vm = this;
        // variables
        vm.String = "Cấu hình > Cấu hình loại WO";
        vm.searchForm = {};
        $scope.workingWOType = {};
        $scope.permissions = {};
        init();

        function init() {
            $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
            fillDataTable();

            postal.subscribe({
                channel: "Tab",
                topic: "action",
                callback: function (data) {
                    if (data.action == 'refresh') vm.dataListWOTable.dataSource.read();
                }
            });
        }

        vm.openCreateWoModal = function () {
            $scope.workingWOType = {}
            $modal.open({
                templateUrl: 'coms/wo_xl/woTypeManagement/woTypeEditModal.html?v=aaa',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function () {
                    vm.createNewWOType($scope.workingWOType);
                },
                function () {
                    $scope.workingWOType = {};
                }
            )
        }

        function validateCreateWoType(workingWoType) {
            if (!workingWoType.woTypeName) {
                vm.validateWoTypeName = "Thông tin WO không được phép bỏ trống";
            } else {
                vm.validateWoTypeName = null;
            }
            if (!workingWoType.woTypeCode) {
                vm.validateWoTypeCode = "Mã loại WO không được phép bỏ trống";
            } else {
                vm.validateWoTypeCode = null;
            }
        }

        vm.createNewWOType = function (obj) {
            var postData = vm.dtoToModel(obj);
            postData.status = 1;
            validateCreateWoType(obj);
            if (!vm.validateWoTypeName && !vm.validateWoTypeCode)
                woTypeManagementService.createNewWOType(postData).then(
                    function (resp) {
                        if (resp && resp.statusCode == 1){
                            toastr.success("Thêm mới thành công!");
                            vm.dataListWOTable.dataSource.read();
                        }
                        else toastr.error("Thêm mới thất bại! " + resp.message);

                    },
                    function (error) {
                        console.log(error);
                        toastr.success("Có lỗi xảy ra!");
                    }
                )
        }

        vm.deleteWOType = function (woTypeId) {
            var obj = {woTypeId: woTypeId};
            confirm('Xác nhận xóa bản ghi đã chọn?',
                function () {
                    woTypeManagementService.deleteWOType(obj).then(
                        function (resp) {
                            console.log(resp);
                            if(resp && resp.statusCode == 1) toastr.success("Xóa thành công!");
                            else toastr.error("Xóa không thành công! " + resp.message);
                            vm.dataListWOTable.dataSource.read();
                        },
                        function (error) {
                            console.log(error);
                            toastr.success("Có lỗi xảy ra!");
                        }
                    )
                }
            );
        }

        vm.doSearch = doSearch;

        function doSearch() {
            var grid = vm.dataListWOTable;
            console.log(grid);
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                })
            }
            return vm.dataListWOTable.dataSource.read();
        }

        vm.editWOType = function (woTypeId) {
            var obj = {woTypeId: woTypeId};
            woTypeManagementService.getOneWOTypeDetails(obj).then(
                function (resp) {

                    if (resp && resp.data) $scope.workingWOType = resp.data;

                    $modal.open({
                        templateUrl: 'coms/wo_xl/woTypeManagement/woTypeEditModal.html?v=aaa',
                        controller: null,
                        windowClass: 'app-modal-window',
                        scope: $scope
                    })
                        .result.then(
                        function () {
                            vm.saveWOType($scope.workingWOType);
                        },
                        function () {
                            $scope.workingWOType = {};
                        }
                    )
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )
        }

        vm.dtoToModel = function (obj) {
            var model = {}
            model.woTypeId = obj.woTypeId;
            model.woTypeName = obj.woTypeName;
            model.woTypeCode = obj.woTypeCode;
            model.status = obj.status;
            if(obj.hasApWorkSrc == true || obj.hasApWorkSrc == 1) model.hasApWorkSrc = 1 ;
            if(obj.hasConstruction == true || obj.hasConstruction == 1) model.hasConstruction = 1;
            if(obj.hasWorkItem == true || obj.hasWorkItem == 1) model.hasWorkItem = 1;
            return model;
        }

        vm.saveWOType = function (obj) {
            var model = vm.dtoToModel(obj);
            woTypeManagementService.updateWOType(model).then(
                function (resp) {
                    console.log(resp);
                    if (resp && resp.statusCode == 1){
                        toastr.success("Sửa thành công!");
                        vm.dataListWOTable.dataSource.read();
                    }
                    else toastr.error("Sửa không thành công!" + resp.message);
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        vm.importExcelFile = importExcelFile;

        function importExcelFile() {
            $("#files").unbind().click(); //id của input file
            $("#files").change(function () {
                var selectedFile = $("#files")[0].files[0];

                if(!CommonService.isExcelFile(selectedFile.name)){
                    toastr.error('File không đúng định dạng.');
                    return;
                }

                var reader = new FileReader();
                reader.onload = function (event) {
                    var data = event.target.result;
                    var workbook = XLSX.read(data, {
                        type: 'binary'
                    });
                    var header = ["woTypeName", "woTypeCode"];
                    var XL_row_object = XLSX.utils.sheet_to_json(workbook.Sheets[workbook.SheetNames[0]], {header: header, range:1});

                    var isValidDataStruct = true;
                    XL_row_object.forEach(function (data) {
                        Object.keys(data).forEach(function (key) {
                            if(key == 'undefined') isValidDataStruct = false;
                        })
                        data.status = 1;
                        data.woTypeId = 0;
                    });

                    if(!isValidDataStruct){
                        toastr.error('Cấu trúc file excel không hợp lệ.');
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
        }

        vm.addDataFormExcel = addDataFormExcel;
        function addDataFormExcel(obj){
            console.log(obj)
            woTypeManagementService.createManyWOType(obj).then(function (res) {
                if(res && res.statusCode == 1){
                    vm.dataListWOTable.dataSource.read();
                }
            })
        }


        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };

        var record = 0;

        function fillDataTable() {
            // var createEditDeleteTemplate = '<div class="display-block cedtpl"><i class="fa fa-list-alt yellow" style:"margin-right:10%"></i>&nbsp;<i class="fa fa-pencil yellow" style:"margin-right:10%"></i>&nbsp;<i class="fa fa-trash-o azure"></i></div>';
            function createActionTemplate(dataItem) {
                var template = '<div class="display-block cedtpl" style="text-align: center">' +
                    // '<i class="fa fa-list-alt icon-table yellow" ng-click="vm.viewWODetails('+dataItem.woTypeId+')"></i>' +
                    '<i class="fa fa-pencil icon-table yellow" ng-click="vm.editWOType(' + dataItem.woTypeId + ')"></i>' +
                    '<i class="fa fa-trash-o icon-table azure" ng-click="vm.deleteWOType(' + dataItem.woTypeId + ')"></i></div>'
                return template;
            }

            vm.dataListWOTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                save: function () {
                    vm.dataListWOTable.dataSource.read();
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
                            vm.count = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/woType/doSearch",
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
                        empty: 'Không có kết quả hiển thị'
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
                        title: "Mã loại",
                        field: 'woTypeCode',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        //format: '{0:n3}',
                        template: function (dataItem) {
                            return dataItem.woTypeCode;
                        },
                    },
                    {
                        title: "Tên loại WO",
                        field: 'woTypeName',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.woTypeName;
                        },
                    },
                    {
                        title: "Cần có nguồn việc",
                        field: 'hasApWorkSrc',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.hasApWorkSrc == 1?'Có':'Không';
                        },
                    },
                    {
                        title: "Cần có loại công trình",
                        field: 'hasConstruction',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.hasConstruction == 1?'Có':'Không';
                        },
                    },
                    {
                        title: "Cần có hạng mục",
                        field: 'hasWorkItem',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.hasWorkItem == 1?'Có':'Không';
                        },
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
                            return createActionTemplate(dataItem);
                        }
                    },
                ]
            });
        }

        vm.getTemplateFile = function(){
            confirm('Bạn có muốn tải file mẫu?', function() {
                var obj = {loggedInUser: $scope.loggedInUser}
                $http({
                    url: Constant.BASE_SERVICE_URL + "woService/woType/getWoTypeImportTemplate",
                    method: "POST",
                    data: obj,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                })
                    .success(function (data, status, headers, config) {
                        htmlCommonService.saveFile(data, "WoTypeCreateExample.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    })
                    .error(function (data, status, headers, config) {
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    });
            })
        }

        $scope.validateCreateNew = function(){
            var woType = $scope.workingWOType;

            if(!woType.woTypeName){
                toastr.error("Tên loại không được để trống.");
                return false;
            }

            if(!woType.woTypeCode){
                toastr.error("Mã loại không được để trống");
                return false;
            }

            return true;
        }

        $scope.checkHasConstruction = function () {
            if($scope.workingWOType.hasConstruction != 1) $scope.workingWOType.hasWorkItem = null;
        }

        vm.exportWoTypeList = function () {
            confirm("Xuất dữ liệu excel?", function () {
                $http({
                    url: Constant.BASE_SERVICE_URL + "woService/woType/exportWoTypeExcel",
                    method: "POST",
                    data: vm.searchForm,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function (data, status, headers, config) {
                    htmlCommonService.saveFile(data, "Danh_sach_loai_wo.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                }).error(function (data, status, headers, config) {
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                });
            })
        }

    // end controller
    }
})();
