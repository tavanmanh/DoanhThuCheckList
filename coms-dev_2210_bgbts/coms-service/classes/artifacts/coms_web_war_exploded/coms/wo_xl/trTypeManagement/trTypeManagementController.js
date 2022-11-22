(function () {
    'use strict';
    var controllerId = 'trTypeManagementController';

    angular.module('MetronicApp').controller(controllerId, trTypeManagementController, '$scope','$modal','$rootScope');

    function trTypeManagementController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, trTypeManagementService, htmlCommonService, vpsPermissionService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modal) {

        var vm = this;
        vm.String = "Cấu hình > Cấu hình loại TR";
        vm.dataResult  = [];
        vm.searchForm = {};
        $scope.trTypeForm ={};
        vm.validateTrTypeName = null;
        vm.validateTrTypeCode = null;

        $scope.permissions = {};
        
        init();
        function init(){
            $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
        	fillDataTable();
        }

        vm.createTrType = createTrType;
        function createTrType(){
            $scope.trTypeForm ={};
            $modal.open({
                templateUrl: 'coms/wo_xl/trTypeManagement/trTypeEditModal.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function() {
                    vm.createNewTRType($scope.trTypeForm);
                },
                function() {
                    $scope.trTypeForm = {};
                }
            )
        }

        function validateCreateTRType(workingTrType) {
            if(!workingTrType.trTypeName){
                vm.validateTrTypeName = "Thông tin TR không được phép bỏ trống";
            }else{
                vm.validateTrTypeName = null;
            }
            if(!workingTrType.trTypeCode){
                vm.validateTrTypeCode = "Mã loại TR không được phép bỏ trống";
            }else{
                vm.validateTrTypeCode = null;
            }
        }

        vm.createNewTRType = function(obj){
            var model = {
                woTrTypeId: 0,
                trTypeName: obj.trTypeName,
                trTypeCode: obj.trTypeCode,
                status: 1,
            };
            validateCreateTRType(obj);
            if(!vm.validateTrTypeName && !vm.validateTrTypeCode)
            trTypeManagementService.createNewTRType(model).then(
                function(resp){
                    if(resp && resp.statusCode == 1){
                        toastr.success("Thêm thành công!");
                        vm.trTypeListTable.dataSource.read();
                    }
                    else toastr.error("Thêm mới thất bại! " + resp.message);

                },
                function(error){
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }
        
        vm.searchTrType= searchTrType;
        function searchTrType(){
        	var grid = vm.trTypeListTable;
        	if(grid){
        		grid.dataSource.query({
        			page: 1,
                    pageSize: 10
        		})
        	}
        }

        vm.importExcelFile = importExcelFile;
        function importExcelFile(){
            $("#files").unbind().click(); //id của input file
            $("#files").change(function(){
                var selectedFile = $("#files")[0].files[0];

                if(!CommonService.isExcelFile(selectedFile.name)){
                    toastr.error('File không đúng định dạng.');
                    return;
                }

                var reader = new FileReader();
                reader.onload = function(event) {
                    var data = event.target.result;
                    var workbook = XLSX.read(data, {
                        type: 'binary'
                    });
                    var header = ["trTypeCode", "trTypeName"];
                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[0]], {header: header, range:1});
                    var isValidDataStruct = true;
                    XL_row_object.forEach(function (data) {

                        Object.keys(data).forEach(function (key) {
                            if(key == 'undefined') isValidDataStruct = false;
                        });

                        data.status = 1;
                        data.woTrTypeId = 0;
                    });

                    if(!isValidDataStruct){
                        toastr.error('Cấu trúc file excel không hợp lệ.');
                        return;
                    }

                    var json_object = JSON.stringify(XL_row_object);
                    console.log(json_object);
                    addDataFormExcel(json_object);
                };
                reader.onerror = function(event) {
                    console.error("File could not be read! Code " + event.target.error.code);
                };
                reader.readAsBinaryString(selectedFile);
            });
        }

        vm.addDataFormExcel = addDataFormExcel;
        function addDataFormExcel(obj){
            trTypeManagementService.createManyTRType(obj).then(function (res) {
                if(res && res.statusCode == 1){
                    vm.trTypeListTable.dataSource.read();
                }
            })
        }

        vm.deleteRecodeTable = deleteRecodeTable;
        function deleteRecodeTable(id) {
            var param = {
                woTrTypeId: id
            }
            confirm('Bạn có muốn xoá loại TR không?', function () {
                trTypeManagementService.deleteTrType(param).then(function(res){

                    if(res && res.statusCode == 1) toastr.success("Xóa dữ liệu thành công!");
                    else toastr.error(res.message);

                    vm.trTypeListTable.dataSource.read();
                }, function(errResponse){
                    toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi xoá loại TR!"));
                });
            })
        }

        vm.editTRType = function(trTypeObj){
            var param = {
                woTrTypeId: trTypeObj
            }
            trTypeManagementService.getOneTRTypeDetails(param).then(
                function(resp){
                    if(resp && resp.data) $scope.trTypeForm = resp.data;

                    $modal.open({
                        templateUrl: 'coms/wo_xl/trTypeManagement/trTypeEditModal.html',
                        controller: null,
                        windowClass: 'app-modal-window',
                        scope: $scope
                    })
                        .result.then(
                        function() {
                            vm.saveTRType($scope.trTypeForm);
                        },
                        function() {
                            $scope.trTypeForm = {};
                        }
                    )
                },
                function(error){
                    toastr.success("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )

        }

        vm.saveTRType = function(obj){
            var model = {
                woTrTypeId: obj.woTrTypeId,
                trTypeName: obj.trTypeName,
                trTypeCode: obj.trTypeCode,
                status: obj.status,
            };


            trTypeManagementService.updateTRType(model).then(
                function(resp){
                    if(resp && resp.statusCode == 1) toastr.success("Sửa thành công!");
                    else toastr.success("Có lỗi xảy ra!");

                    vm.trTypeListTable.dataSource.read();
                },
                function(error){
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }
        
        var record = 0;

        function createEditDeleteTemplate(dataItem){
            var template = '<div class="display-block cedtpl" style="text-align: center">' +
                '<i ng-click="vm.editTRType('+dataItem.woTrTypeId+')" class="fa fa-pencil icon-table"></i>' +
                '<i ng-click="vm.deleteRecodeTable('+dataItem.woTrTypeId+')" class="fa fa-trash-o icon-table"></i></div>';
            return template;
        }

        function fillDataTable(){

            vm.trTypeListTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable:false,
                save : function(){
                    vm.trTypeListTable.refresh();
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
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "trService/trType/doSearchTRType",
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
                        type :'text',
                    },
                    {
                        title: "Tên loại TR",
                        field: 'trTypeName',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return dataItem.trTypeName;
                        },
                    },
                    {
                        title: "Mã loại",
                        field: 'trTypeCode',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return dataItem.trTypeCode;
                        },
                    },
                    {
                        title: "Thao tác",
                        // field: 'sysGroupName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return createEditDeleteTemplate(dataItem);
                        }
                    },
            ]
            });
        }

        vm.getTemplateFile = function(){
            confirm('Bạn có muốn tải file mẫu?', function() {
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

        $scope.validateCreateNew = function(){
            var trType = $scope.trTypeForm;

            if(!trType.trTypeName){
                toastr.error("Tên loại không được để trống.");
                return false;
            }

            if(!trType.trTypeCode){
                toastr.error("Mã loại không được để trống");
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

    // end controller
    }
})();