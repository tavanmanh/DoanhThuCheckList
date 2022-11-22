(function () {
    'use strict';
    var controllerId = 'woConfigContractController';

    angular.module('MetronicApp').controller(controllerId, woConfigContractController, '$scope','$modal','$rootScope');

    function woConfigContractController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, woConfigContractService, htmlCommonService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modal) {

        var vm = this;
        vm.String = "Cấu hình > Cấu hình ban chỉ huy công trường";
        vm.dataResult  = [];
        vm.searchForm = {};
        $scope.woConfigContract = {};
        $scope.loggedInUser = $rootScope.casUser.userName;
        $scope.date = new Date();
        vm.userRole = 1;
        $scope.permissions = {};
        $scope.listUserRole = [
            {value: 1, name: 'CD'},
            {value: 2, name: 'FT'},];
        init();
        function init(){
        	fillDataTable();
        }

        $scope.selectOn = function(){
            vm.state = '1';
        };

        $scope.selectOff = function(){
            vm.state = '0';
        };
        vm.doSearch= doSearch;
        function doSearch(){
            var grid = vm.workItemConfigListTable;
            if(grid){
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                })
            }
        }
        vm.createConfigWorkItem = createConfigWorkItem;
        function createConfigWorkItem(){
            $scope.woConfigContract = {};
            $scope.isEditingAll = false;
            $scope.isEditing = false;
            $scope.disableContractCode= false;
            $modal.open({
                templateUrl: 'coms/wo_xl/woConfigContract/woConfigContractEditModal.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function() {
                    vm.createNewConfigWorkItem($scope.woConfigContract);
                },
                function() {
                    $scope.woConfigContract = {};
                }
            )
        }

        vm.createNewConfigWorkItem = function(obj){
            var postData = vm.dtoToModel(obj);
            woConfigContractService.createNew(postData).then(
                function(resp){
                    if(resp && resp.statusCode == 1){
                        toastr.success("Thêm mới thành công!");
                        vm.searchForm.contractCode=null;
                        vm.workItemConfigListTable.dataSource.read();
                    }
                    else toastr.error("Thêm mới thất bại! " + resp.message);
                },
                function(error){
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }
        vm.dtoToModel = function (obj) {
            var model = {}
            model.contractId = obj.contractId;
            model.contractCode = obj.contractCode;
            model.userId = obj.userId;
            model.userRole = obj.userRole;
            model.userPosition = obj.userPosition;
            model.status = 1;
            model.userName = obj.userName;
            model.userCode = obj.userCode;

            return model;
        }
        // lay ma hop dong
        $scope.autoCompleteContractOptions={
            dataTextField: "contractCode", placeholder:"Thông tin hợp đồng",
            open: function(e) {
            },
            select: function(e) {
                data = this.dataItem(e.item.index());
                $scope.woConfigContract.contractId = data.contractId;
                $scope.woConfigContract.contractCode = data.contractCode;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function(options) {
                        var keySearch = $scope.woConfigContract.contractCode.trim();

                        if(keySearch == ''){
                            $scope.woConfigContract.contractId = null;
                            $scope.woConfigContract.contractCode = null;
                        }

                        $scope.woConfigContract.contractId = null;

                        var objSearch = {keySearch:keySearch,page:1, pageSize: 10};

                        return Restangular.all("trService/woConfigContract/searchWoConfigContract").post(objSearch).then(
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
                '<p class="col-md-12 text-header-auto">Các Hợp đồng</p></div>',
            template:'<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.contractCode #</div></div>',
            change: function(e) {
            },
            close: function(e) {
            }
        };
        // chon nhan viên
        $scope.autoCompleteFTOptions={
            dataTextField: "userCode", placeholder:"Chọn FT",
            open: function(e) {
            },
            select: function(e) {
                data = this.dataItem(e.item.index());
                $scope.woConfigContract.userCode = data.userCode;
                $scope.woConfigContract.userName = data.userName;
                $scope.woConfigContract.userId = data.userId;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function(options) {
//                        var keySearch = $scope.woConfigContract.userName.trim();
//                        if(keySearch == ''){
                            $scope.woConfigContract.userCode = null;
//                            $scope.woConfigContract.userName = null;
                            $scope.woConfigContract.userId = null;
//                        }
//                        $scope.woConfigContract.userId = null;
                        var objSearch = {keySearch:$scope.woConfigContract.userName, page:1, pageSize: 10};

                        return Restangular.all("trService/woConfigContract/getFtList").post(objSearch).then(
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
                '<p class="col-md-12 text-header-auto">Nhân viên</p></div>',
            template:'<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.userCode # - #: data.userName #</div></div>',
            change: function(e) {
            	if(!$scope.woConfigContract.userId){
            		$scope.woConfigContract.userCode = null;
                    $scope.woConfigContract.userName = null;
                    $scope.woConfigContract.userId = null;
                    $scope.$apply();
            	}
            },
            close: function(e) {
            	if(!$scope.woConfigContract.userId){
            		$scope.woConfigContract.userCode = null;
                    $scope.woConfigContract.userName = null;
                    $scope.woConfigContract.userId = null;
                    $scope.$apply();
            	}
            }
        };
        vm.searchWoConfigContract= searchWoConfigContract;
        function searchWoConfigContract(){
        	var grid = vm.workItemConfigListTable;
        	if(grid){
        		grid.dataSource.query({
        			page: 1,
                    pageSize: 10
        		})
        	}
        }
        vm.exportexcel= function(){
            function displayLoading(target) {
                var element = $(target);
                kendo.ui.progress(element, true);
                setTimeout(function(){
                    return Restangular.all("trService/woConfigContract/exportExcelWoConfigContract").post(vm.searchForm).then(function (d) {
                        var data = d.plain();
                        window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                        kendo.ui.progress(element, false);
                    }).catch(function (e) {
                        kendo.ui.progress(element, false);
                        toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                        return;
                    });;

                });

            }
            displayLoading(".tab-content");
        }
        vm.importConfig = importConfig;
        function importConfig() {
            vm.fileImportData = false;
            vm.searchForm.type = 1;
            var teamplateUrl = "coms/wo_xl/woConfigContract/woConfigContractImportModal.html";
            var title = "Import cấu hình ban chỉ huy";
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
            }
            else {
                if (104857600 < $("#fileImport")[0].files[0].size) {
                    toastr.warning("Dung lượng file vượt quá 100MB! ");
                    return;
                }
                $("#fileName")[0].innerHTML = $("#fileImport")[0].files[0].name
            }
        }
        function getImportResult(obj){
            $http({
                url: Constant.BASE_SERVICE_URL + "woService/woConfigContract/getWoConfigContractResult",
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
        vm.submit = submit;
        function submit(data) {
            var ajax_sendding = false;
            if (ajax_sendding == true){
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
                url: Constant.BASE_SERVICE_URL + "woService/woConfigContract/importConfigContract" ,
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if(data && data.statusCode == 1){
                        toastr.success("Import thành công! ");
                        doSearch();
                        cancelImport();
                    } else {
                        toastr.error("Import thất bại! " + data.message?data.message:'');
                        if(data.data != null){
                            confirm('Phát hiện dữ liệu không hợp lệ! Bạn có muốn xem chi tiết?', function () {
                                getImportResult(data.data) });
                        }
                        cancelImport();
                    }
                    cancelImport();
                    $scope.$apply();
                }
            });
        }

        vm.deleteWoConfigContract = deleteWoConfigContract;
        function deleteWoConfigContract(id) {
            var idConfig = id;
            var param = {
                id: idConfig
            }
            confirm('Bạn có muốn xoá cấu hình không?', function () {
                    woConfigContractService.deleteWoConfigContract(param).then(function (res) {
                        if (res && res.statusCode == 1) toastr.success("Xóa dữ liệu thành công!");
                        else toastr.error(res.message);
                        vm.workItemConfigListTable.dataSource.read();
                    }, function (errResponse) {
                        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi xoá!"));
                    });
                })
        }

        vm.editWoConfigContract = function(id){
            $scope.isEditing = true;
            $scope.disableContractCode= true;
            var param = {
                id: id
            }
            woConfigContractService.getOneWoConfigContract(param).then(
                function(resp){
                    if(resp && resp.data){
                        $scope.woConfigContract = resp.data;
                    }
                    $modal.open({
                        templateUrl: 'coms/wo_xl/woConfigContract/woConfigContractEditModal.html',
                        controller: null,
                        windowClass: 'app-modal-window',
                        scope: $scope
                    })
                        .result.then(
                        function() {
                            vm.saveWoConfigContract($scope.woConfigContract);
                        },
                        function() {
                            $scope.woConfigContract = {};
                        }
                    )
                },
                function(error){
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )

        }

        vm.saveWoConfigContract = function(obj){
            var model = {
                id:obj.id,
                contractId : obj.contractId,
                contractCode : obj.contractCode,
                userId : obj.userId,
                userRole : obj.userRole,
                userPosition : obj.userPosition,
                status : obj.status,
                userCode : obj.userCode,
                userName : obj.userName,
                userCreated : obj.userCreated,
                userCreatedDate : obj.userCreatedDate,
                lastUpdateUser:obj.lastUpdateUser,
                lastUpdateDate:obj.lastUpdateDate
            };
            woConfigContractService.updateWoConfigContract(model).then(
                function(resp){
                    if(resp && resp.statusCode == 1) toastr.success("Sửa thành công!");
                    else toastr.error(resp.message);

                    vm.workItemConfigListTable.dataSource.read();
                },
                function(error){
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }
        
        var record = 0;
        function createActionTemplate(dataItem) {
            var template = '<div class="display-block cedtpl" style="text-align: center">' +
                '<i class="fa fa-pencil icon-table yellow" ng-click="vm.editWoConfigContract(' + dataItem.id + ')"></i>' +
                '<i class="fa fa-trash-o icon-table azure" ng-click="vm.deleteWoConfigContract(' + dataItem.id + ')"></i></div>'
            return template;
        }

        function fillDataTable(){
            vm.workItemConfigListTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable:false,
                save : function(){
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
                            url: Constant.BASE_SERVICE_URL + "trService/woConfigContract/doSearchWoConfigContract",
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
                        title: "Mã hợp đồng",
                        field: 'contractCode',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return dataItem.contractCode;
                        },
                    },
                    {
                        title: "Tên nhân viên",
                        field: 'userName',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return dataItem.userName;
                        },
                    },
                    {
                        title: "Quyền nhân viên",
                        field: 'userRole',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return getRoleText(dataItem.userRole) ? getRoleText(dataItem.userRole):'';
                        },
                    },
                    {
                        title: "Chức danh",
                        field: 'userPosition',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function(dataItem){
                            return dataItem.userPosition  ? dataItem.userPosition:'';
                        }
                    },
                    {
                        title: "Ngày tạo",
                        field: 'userCreatedDate',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        template: function(dataItem){
                            return dataItem.userCreatedDateStr ? dataItem.userCreatedDateStr:'';
                        }
                    },
                    {
                        title: "Trạng thái",
                        field: 'status',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function(dataItem){
                            return getStateText(dataItem.status) ? getStateText(dataItem.status):'';
                        }
                    }, {
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
                    }
            ]
            });
        }

        function getStateText(state){
            var text = '';
            if (state == 1)
                return text = 'Hiệu lực';
            else
                return text = 'Hết hiệu lực';
        }
        function getRoleText(state){
            var text = '';
            if (state == 1)
                return text = 'CD';
            else
                return text = 'FT';
        }
        $scope.validateCreateNew = function(){
            var woConfigContract = $scope.woConfigContract;

            if(!woConfigContract.contractCode){
                toastr.error("Mã hợp đồng không được để trống.");
                return false;
            }

            if(!woConfigContract.userName){
                toastr.error("Nhân viên không được để trống");
                return false;
            }

            if(!woConfigContract.userRole){
                toastr.error("Quyền nhân viên không được để trống");
                return false;
            }
            return true;
        }


    // end controller
    }
})();
