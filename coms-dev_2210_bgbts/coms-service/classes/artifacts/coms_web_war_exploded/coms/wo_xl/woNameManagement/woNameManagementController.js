(function () {
    'use strict';
    var controllerId = 'woNameManagementController';

    angular.module('MetronicApp').controller(controllerId, woNameManagementController,'$modal');

    function woNameManagementController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, woNameManagementService, htmlCommonService, vpsPermissionService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http,$modal) {

        var vm = this;
        // variables
        vm.String = "Cấu hình > Cấu hình tên WO";
        vm.searchForm = {};
        $scope.workingWOName ={};
        $scope.woTypes = {};
        $scope.permissions = {};

        init();
        function init(){
            $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
            getWoTypes()
        	fillDataTable();

            postal.subscribe({
                channel  : "Tab",
                topic    : "action",
                callback:function(data){
                    if(data.action == 'refresh') vm.dataListWONameTable.dataSource.read();
                }
            });
        }

        function getWoTypes() {
            woNameManagementService.getWOTypes().then(
                function (resp) {
                    //console.log(resp);
                    if(resp.data) $scope.woTypes = resp.data;
                },
                function (error) {
                    //console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        vm.openCreateWoModal = function(){
            $scope.workingWOName={}
            $modal.open({
                templateUrl: 'coms/wo_xl/woNameManagement/woNameEditModal.html?v=aaa',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function() {
                    vm.createNewWOName($scope.workingWOName);
                },
                function() {
                    $scope.workingWOName = {};
                }
            )
        }

        vm.createNewWOName = function(obj){
            var postData = vm.dtoToModel(obj);
            postData.status = 1;
            woNameManagementService.createNewWOName(postData).then(
                function(resp){
                    console.log(resp);
                    if(resp && resp.statusCode == 1) toastr.success("Thêm mới thành công!");
                    else toastr.success("Đã xóa hoặc không tồn tại!");
                    vm.dataListWONameTable.dataSource.read();
                },
                function(error){
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        vm.deleteWOName = function(woNameId){
            var obj = {id:woNameId};
            confirm('Xác nhận xóa bản ghi đã chọn?',
                function(){
                    woNameManagementService.deleteWOName(obj).then(
                        function(resp){
                            console.log(resp);
                            if(resp && resp.statusCode == 1) toastr.success("Xóa thành công!");
                            else toastr.success("Đã xóa hoặc không tồn tại!");
                            vm.dataListWONameTable.dataSource.read();
                        },
                        function(error){
                            console.log(error);
                            toastr.success("Có lỗi xảy ra!");
                        }
                    )
                }
            );
        }

        vm.doSearch= doSearch;
        function doSearch(){
            var grid = vm.dataListWONameTable;
            console.log(grid);
        	if(grid){
        		grid.dataSource.query({
        			page: 1,
                    pageSize: 10
        		})
            }
            return  vm.dataListWONameTable.dataSource.read();
        }

        vm.editWOName = function(woNameId){

            for(var i = 0; i < $scope.woNameList.length; i++){
                if($scope.woNameList[i].id == woNameId) $scope.workingWOName = $scope.woNameList[i];
            }

            $modal.open({
                templateUrl: 'coms/wo_xl/woNameManagement/woNameEditModal.html?v=aaa',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function() {
                    vm.saveWOName($scope.workingWOName);
                },
                function() {
                    $scope.workingWOName = {};
                }
            )
        }

        vm.dtoToModel = function(obj){
            var model = {}
            model.id = obj.id;
            model.name = obj.name;
            model.woTypeId = obj.woTypeId;
            model.status = obj.status;
            return model;
        }

        vm.saveWOName= function(obj){
            var model = vm.dtoToModel(obj);
            woNameManagementService.updateWOName(model).then(
                function(resp){
                    console.log(resp);
                    if(resp && resp.statusCode == 1) toastr.success("Sửa thành công!");
                    else toastr.error("Đã xóa hoặc không tồn tại!");
                    vm.dataListWONameTable.dataSource.read();
                },
                function(error){
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
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
                    var header = ["woTypeId", "name"];
                    var XL_row_object = XLSX.utils.sheet_to_json(workbook.Sheets[workbook.SheetNames[0]], {header: header, range:1});

                    var isValidDataStruct = true;
                    XL_row_object.forEach(function (data, index) {

                        Object.keys(data).forEach(function (key) {
                            if(key == 'undefined') isValidDataStruct = false;
                        });

                        data.status = 1;
                        data.id = 0;
                        data.woTypeId = data.woTypeId?.split("-")[0].trim();
                    });
                    if(!isValidDataStruct){
                        toastr.error('Cấu trúc file excel không hợp lệ.');
                        return;
                    }
                    var json_object = JSON.stringify(XL_row_object);
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
            woNameManagementService.createManyWONames(obj).then(function (res) {
                if(res && res.statusCode == 1){
                    vm.dataListWONameTable.dataSource.read();
                    toastr.success("Import thành công.");
                }
                else{
                    toastr.error("Import thất bại!")
                }
            },
            function (err) {
                toastr.error("Có lỗi xảy ra! " + err.message);
            })
        }

        
        var record = 0;
        function fillDataTable(){
            // var createEditDeleteTemplate = '<div class="display-block cedtpl"><i class="fa fa-list-alt yellow" style:"margin-right:10%"></i>&nbsp;<i class="fa fa-pencil yellow" style:"margin-right:10%"></i>&nbsp;<i class="fa fa-trash-o azure"></i></div>';
            function createActionTemplate(dataItem){
                var template = '<div class="display-block cedtpl" style="text-align: center">' +
                    // '<i class="fa fa-list-alt icon-table yellow" ng-click="vm.viewWODetails('+dataItem.woTypeId+')"></i>' +
                    '<i class="fa fa-pencil icon-table yellow" ng-click="vm.editWOName('+dataItem.id+')"></i>' +
                    '<i class="fa fa-trash-o icon-table azure" ng-click="vm.deleteWOName('+dataItem.id+')"></i></div>'
                return template;
            }
            vm.dataListWONameTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable:false,
                save : function(){
                    vm.dataListWONameTable.dataSource.read();
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
                            $scope.woNameList = response.data;
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/woName/doSearch",
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
                        type :'text',
                    },
                    {
                        title: "Tên WO",
                        field: 'woTypeName',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return dataItem.name;
                        },
                    },
                    {
                        title: "Loại WO",
                        field: 'woTypeCode',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        //format: '{0:n3}',
                        template : function(dataItem){
                            return dataItem.woTypeName;
                        },
                    },
                    {
                        title: "Mã loại WO",
                        field: 'sysGroupName',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function(dataItem){
                            return dataItem.woTypeCode;
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
                        type :'text',
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
                    url: Constant.BASE_SERVICE_URL + "woService/woName/getWoNameImportTemplate",
                    method: "POST",
                    data: obj,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                })
                    .success(function (data, status, headers, config) {
                        htmlCommonService.saveFile(data, "WoNameCreateExample.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    })
                    .error(function (data, status, headers, config) {
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    });
            })
        }

        $scope.validateCreateNew = function(){
            var woName = $scope.workingWOName;

            if(!woName.woTypeId){
                toastr.error("Mời chọn loại WO.");
                return false;
            }

            if(!woName.name){
                toastr.error("Tên WO không được để trống");
                return false;
            }

            return true;
        }

        vm.exportWoNameList = function () {
            confirm("Xuất dữ liệu excel?", function () {
                $http({
                    url: Constant.BASE_SERVICE_URL + "woService/woName/exportWoNameExcel",
                    method: "POST",
                    data: vm.searchForm,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function (data, status, headers, config) {
                    htmlCommonService.saveFile(data, "Danh_sach_ten_wo.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                }).error(function (data, status, headers, config) {
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                });
            })
        }

        // end controller
    }
})();
