(function () {
    'use strict';
    var controllerId = 'scheduleWorkItemManagementController';

    angular.module('MetronicApp').controller(controllerId, scheduleWorkItemManagementController, '$scope','$modal','$rootScope');

    function scheduleWorkItemManagementController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, workItemManagementService, htmlCommonService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modal) {

        var vm = this;
        vm.String = "Cấu hình > Cấu hình công việc định kỳ";
        vm.dataResult  = [];
        vm.searchForm = {};
        $scope.workItemForm ={};
        $scope.loggedInUser = $rootScope.casUser.userName;
        $scope.CurrentDate = new Date();
        vm.validateWorkItemName = null;
        vm.validateWorkItemCode = null;

        $scope.permissions = {};
        
        init();
        function init(){
        	fillDataTable();
        }

        vm.createWorkItem = createWorkItem;
        function createWorkItem(){
            $scope.workItemForm ={};
            $scope.isEditing = false;
            $modal.open({
                templateUrl: 'coms/wo_xl/scheduleWorkItemManagement/scheduleWorkItemCreateEditModal.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function() {
                    vm.createNewWorkItem($scope.workItemForm);
                },
                function() {
                    $scope.workItemForm = {};
                }
            )
        }

        function validateCreateWorkItem(workingWorkItem) {
            if(!workingWorkItem.workItemName){
                vm.validateWorkItemName = "Tên công việc định kỳ không được phép bỏ trống";
            }else{
                vm.validateWorkItemName = null;
            }
            if(!workingWorkItem.workItemCode){
                vm.validateWorkItemCode = "Mã công việc định kỳ không được phép bỏ trống";
            }else{
                vm.validateWorkItemCode = null;
            }
        }

        vm.createNewWorkItem = function(obj){
            var model = {
                woWorkItemId: 0,
                workItemName: obj.workItemName,
                workItemCode: obj.workItemCode,
                userCreated: $scope.loggedInUser,
                createdDate: $scope.CurrentDate,
                status: 1,
            };
            validateCreateWorkItem(obj);
            if(!vm.validateWorkItemName && !vm.validateWorkItemCode)
                workItemManagementService.createNewWorkItem(model).then(
                function(resp){
                    if(resp && resp.statusCode == 1){
                        toastr.success("Thêm mới thành công!");
                        vm.searchForm.workItemName=null;
                        vm.workItemListTable.dataSource.read();
                    }
                    else toastr.error("Thêm mới thất bại! " + resp.message);

                },
                function(error){
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }
        
        vm.searchWI= searchWI;
        function searchWI(){
        	var grid = vm.workItemListTable;
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
                woWorkItemId: id
            }
            confirm('Bạn có muốn xoá công việc định kỳ không?', function () {
                workItemManagementService.deleteWorkItem(param).then(function(res){

                    if(res && res.statusCode == 1) toastr.success("Xóa dữ liệu thành công!");
                    else toastr.error(res.message);
                    vm.workItemListTable.dataSource.read();
                }, function(errResponse){
                    toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi xoá công việc định kỳ!"));
                });
            })
        }

        vm.editWorkItem = function(workItemObj){
            $scope.isEditing = true;
            var param = {
                woWorkItemId: workItemObj
            }
            workItemManagementService.getOneInfoWorkItem(param).then(
                function(resp){
                    if(resp && resp.data) $scope.workItemForm = resp.data;

                    $modal.open({
                        templateUrl: 'coms/wo_xl/scheduleWorkItemManagement/scheduleWorkItemCreateEditModal.html',
                        controller: null,
                        windowClass: 'app-modal-window',
                        scope: $scope
                    })
                        .result.then(
                        function() {
                            vm.saveWorkItem($scope.workItemForm);
                        },
                        function() {
                            $scope.workItemForm = {};
                        }
                    )
                },
                function(error){
                    toastr.success("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )

        }

        vm.saveWorkItem = function(obj){
            var model = {
                woWorkItemId: obj.woWorkItemId,
                workItemCode: obj.workItemCode,
                workItemName: obj.workItemName,
                userCreated: obj.userCreated,
                createdDate: obj.createdDate,
                status: obj.status,
            };


            workItemManagementService.updateWorkItem(model).then(
                function(resp){
                    if(resp && resp.statusCode == 1) toastr.success("Sửa thành công!");
                    else toastr.success(resp.message);

                    vm.workItemListTable.dataSource.read();
                },
                function(error){
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }
        
        var record = 0;

        vm.viewWIDetails = function(str){
            var splitted = str.split('-');
            var id = splitted[0]
            var splitted1 = str.split((splitted[0].concat('-')));
            var name = splitted1[1]
            var template = Constant.getTemplateUrl('WO_XL_SCHEDULE_WORK_ITEM_DETAILS');
            $rootScope.scheduleWorkItemId = id;
            $rootScope.viewDetailsWoCode = name;
            postal.publish({
                channel : "Tab",
                topic : "open",
                data : template
            });

            postal.publish({
                channel : "Tab",
                topic   : "action",
                data    : {action:'refresh', workItemId: id}
            });
        };

        function createEditDeleteTemplate(dataItem){
            var  xxx = dataItem.woWorkItemId + '-' + dataItem.workItemCode;
            var template = '<div class="display-block cedtpl" style="text-align: center">' +
                '<i class="fa fa-list-alt icon-table" ng-click="vm.viewWIDetails(\''+xxx+'\')"></i>' +
                '<i ng-click="vm.editWorkItem('+dataItem.woWorkItemId+')" class="fa fa-pencil icon-table"></i>' +
                '<i ng-click="vm.deleteRecodeTable('+dataItem.woWorkItemId+')" class="fa fa-trash-o icon-table"></i></div>';
            return template;
        }

        function fillDataTable(){

            vm.workItemListTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable:false,
                save : function(){
                    vm.workItemListTable.refresh();
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
                            // Thuc hien viec goi service lay data tim kiem /wi/doSearchWorkItem
                            url: Constant.BASE_SERVICE_URL + "woService/wi/doSearchWorkItem",
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
                        title: "Mã công việc định kỳ",
                        field: 'workItemCode',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return dataItem.workItemCode;
                        },
                    },
                    {
                        title: "Tên công việc định kỳ",
                        field: 'workItemName',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return dataItem.workItemName;
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
            var workItemp = $scope.workItemForm;

            if(!workItemp.workItemName){
                toastr.error("Tên công việc định kỳ không được để trống.");
                return false;
            }

            if(!workItemp.workItemCode){
                toastr.error("Mã công việc định kỳ không được để trống");
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