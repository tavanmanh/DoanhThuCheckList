(function () {
    'use strict';
    var controllerId = 'hcqtProjectManagementController';

    angular.module('MetronicApp').controller(controllerId, hcqtProjectManagementController, '$scope','$modal','$rootScope');

    function hcqtProjectManagementController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, hcqtProjectManagementService, htmlCommonService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modal) {

        var vm = this;
        vm.breadcrumb = "Cấu hình > Cấu hình dự án Hoàn công quyết toán";
        vm.dataResult  = [];
        vm.searchForm = {};
        $scope.hcqtProject ={};
        $scope.loggedInUser = $rootScope.casUser.userName;
        $scope.permissions = {};
        
        init();
        function init(){
        	fillDataTable();
        }

        vm.createHcqtProject = createHcqtProject;
        function createHcqtProject(){
            $scope.hcqtProject ={};
            $modal.open({
                templateUrl: 'coms/wo_xl/hcqtProjectManagement/hcqtProjectCreateEditModal.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function() {
                    vm.createNewHcqtProject($scope.hcqtProject);
                },
                function() {
                    $scope.hcqtProject = {};
                }
            )
        }

        vm.createNewHcqtProject = function(obj){
            obj.hcqtProjectId = 0;
            obj.status = 1;
            obj.userCreated = $scope.loggedInUser;
            hcqtProjectManagementService.createNewHcqtProject(obj).then(
                function(resp){
                    if(resp && resp.statusCode == 1){
                        toastr.success("Thêm thành công!");
                        vm.hcqtProjectListTable.dataSource.read();
                    }
                    else toastr.error("Thêm mới thất bại! " + resp.message);

                },
                function(error){
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        };
        
        vm.searchHcqtProject= searchHcqtProject;
        function searchHcqtProject(){
        	var grid = vm.hcqtProjectListTable;
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
                    vm.hcqtProjectListTable.dataSource.read();
                }
            })
        }

        vm.deleteHcqtProject = deleteHcqtProject;
        function deleteHcqtProject(id) {
            var param = {
                hcqtProjectId: id
            }
            confirm('Bạn có muốn xoá dự án hcqt không?', function () {
                hcqtProjectManagementService.deleteHcqtProject(param).then(function(res){

                    if(res && res.statusCode == 1) toastr.success("Xóa dữ liệu thành công!");
                    else toastr.error(res.message);

                    vm.hcqtProjectListTable.dataSource.read();
                }, function(errResponse){
                    toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi xoá!"));
                });
            })
        }

        vm.editHcqtProject = function(hcqtProjectId){
            var param = {
                hcqtProjectId: hcqtProjectId
            }
            hcqtProjectManagementService.getOneHcqtProjectDetails(param).then(
                function(resp){
                    if(resp && resp.data) $scope.hcqtProject = resp.data;

                    $modal.open({
                        templateUrl: 'coms/wo_xl/hcqtProjectManagement/hcqtProjectCreateEditModal.html',
                        controller: null,
                        windowClass: 'app-modal-window',
                        scope: $scope
                    })
                        .result.then(
                        function() {
                            vm.saveHcqtProject($scope.hcqtProject);
                        },
                        function() {
                            $scope.hcqtProject = {};
                        }
                    )
                },
                function(error){
                    toastr.success("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )

        }

        vm.saveHcqtProject = function(obj){
            hcqtProjectManagementService.updateHcqtProject(obj).then(
                function(resp){
                    if(resp && resp.statusCode == 1) toastr.success("Sửa thành công!");
                    else toastr.success("Có lỗi xảy ra!");

                    vm.hcqtProjectListTable.dataSource.read();
                },
                function(error){
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        };
        
        var record = 0;

        function createEditDeleteTemplate(dataItem){
            var template = '<div class="display-block cedtpl" style="text-align: center">' +
                '<i ng-click="vm.editHcqtProject('+dataItem.hcqtProjectId+')" class="fa fa-pencil icon-table"></i>' +
                '<i ng-click="vm.deleteHcqtProject('+dataItem.hcqtProjectId+')" class="fa fa-trash-o icon-table"></i></div>';
            return template;
        }

        function fillDataTable(){

            vm.hcqtProjectListTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable:false,
                save : function(){
                    vm.hcqtProjectListTable.refresh();
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
                            url: Constant.BASE_SERVICE_URL + "woService/hcqtProject/doSearchHcqtProject",
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
                        title: "Mã dự án HCQT",
                        field: 'code',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return dataItem.code;
                        }
                    },
                    {
                        title: "Tên dự án HCQT",
                        field: 'name',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return dataItem.name;
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
                        type :'text',
                        template: function (dataItem) {
                            return createEditDeleteTemplate(dataItem);
                        }
                    },
            ]
            });
        }

        // vm.getTemplateFile = function(){
        //     confirm('Bạn có muốn tải file mẫu?', function() {
        //         var obj = {loggedInUser: $scope.loggedInUser}
        //         $http({
        //             url: Constant.BASE_SERVICE_URL + "trService/trType/getTrTypeImportTemplate",
        //             method: "POST",
        //             data: obj,
        //             headers: {
        //                 'Content-type': 'application/json'
        //             },
        //             responseType: 'arraybuffer'
        //         })
        //             .success(function (data, status, headers, config) {
        //                 htmlCommonService.saveFile(data, "TrTypeCreateExample.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        //             })
        //             .error(function (data, status, headers, config) {
        //                 toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
        //             });
        //     })
        // };

        $scope.validateCreateNew = function(){
            var project = $scope.hcqtProject;

            if(!project.name){
                toastr.error("Tên không được để trống.");
                return false;
            }

            if(!project.code){
                toastr.error("Mã không được để trống");
                return false;
            }

            return true;
        }

        // vm.exportTrTypeList = function () {
        //     confirm("Xuất dữ liệu excel?", function () {
        //         $http({
        //             url: Constant.BASE_SERVICE_URL + "trService/trType/exportTrTypeExcel",
        //             method: "POST",
        //             data: vm.searchForm,
        //             headers: {
        //                 'Content-type': 'application/json'
        //             },
        //             responseType: 'arraybuffer'
        //         }).success(function (data, status, headers, config) {
        //             htmlCommonService.saveFile(data, "Danh_sach_loai_tr.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        //         }).error(function (data, status, headers, config) {
        //             toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
        //         });
        //     })
        // }

    // end controller
    }
})();