(function () {
    'use strict';
    var controllerId = 'wO5sConfigController';

    angular.module('MetronicApp').controller(controllerId, wO5sConfigController, '$scope','$modal','$rootScope');

    function wO5sConfigController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, wO5sConfigConfigService, htmlCommonService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modal) {

        var vm = this;
        vm.String = "Cấu hình > Cấu hình 5s";
        vm.dataResult  = [];
        vm.searchForm = {};
        $scope.workItemForm ={};
        $scope.working5SConfig = {};
        $scope.loggedInUser = $rootScope.casUser.userName;
        $scope.loggedInUserName = $rootScope.casUser.fullName;
        console.log($rootScope.casUser);
        $scope.date = new Date();
        vm.validateWorkItemName = null;
        vm.validateWorkItemCode = null;
        vm.state = 1;
        vm.cycleType = 1;
        vm.validateEndDate = null;
        $scope.ctOrPj = {};
        $scope.workItemList = {};
        $scope.cdLv2List = {};
        $scope.FTList = {};
        $scope.permissions = {};
        $scope.isEditing = false;
        $scope.isEditingAll = false;
        $scope.sysUserGroup = {};

        init();
        function init(){
        	fillDataTable();
            // getSysUserGroup();
            getCdLv2List();
        }

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

        vm.createConfigWo5s = createConfigWo5s;
        function createConfigWo5s(){
            $scope.working5SConfig = {};
            // $scope.isEditingAll = false;
            $scope.isEditing = false;
            $modal.open({
                templateUrl: 'coms/wo_xl/wO5sConfigManagement/wO5sConfigCreateEditModal.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function() {
                    vm.createNewConfigWo5s($scope.working5SConfig);
                },
                function() {
                    $scope.working5SConfig = {};
                }
            )
        }

        vm.createNewConfigWo5s = function(obj){
            var model = {
                ftConfigId: 0,
                ftConfigName: obj.ftConfigName,
                ftConfigCode: obj.ftConfigCode,
                userCreated: $scope.loggedInUser,
                userNameCreated: $scope.loggedInUserName,
                status: 1,
                cdLevel2: obj.cdLevel2,
                cdLevel2Name: obj.cdLevel2Name,
                ftId: obj.ftId,
                ftName: obj.ftName
            };

            wO5sConfigConfigService.createNewConfigWO5s(model).then(
                function(resp){
                    if(resp && resp.statusCode == 1){
                        toastr.success("Thêm mới thành công!");
                        vm.woFTConfigListTable.dataSource.read();
                    }
                    else toastr.error("Thêm mới thất bại! " + resp.message);

                },
                function(error){
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        // lay cd level 2
        $scope.autoCompleteCdLevel2Options = {
            dataTextField: "code", placeholder: "Chọn chi nhánh kỹ thuật",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                $scope.working5SConfig.cdLevel2Name = data.groupName;
                $scope.working5SConfig.cdLevel2 = data.sysGroupId;
                //getFTList();
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = $scope.working5SConfig.cdLevel2Name;
                        if(keySearch == ''){
                            $scope.working5SConfig.cdLevel2Name = null;
                            $scope.working5SConfig.cdLevel2 = null;
                            return options.success([]);
                        }
                        $scope.working5SConfig.ftId = null;
                        $scope.working5SConfig.ftName = null;
                        $scope.working5SConfig.cdLevel2 = null;
                        return options.success(searchCdLevel2(keySearch));
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-12 text-header-auto">CHỌN CHI NHÁNH KỸ THUẬT</p></div>',
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
            for(var i = 0; i < $scope.cdLv2List.length; i++){
                var lv2 = $scope.cdLv2List[i];
                keySearch = keySearch.toUpperCase();
                var groupName = lv2.groupName.toUpperCase();
                if(groupName.includes(keySearch)) searchResult.push(lv2);
            }
            return searchResult;
        }

        function getCdLv2List() {
            var postData = {loggedInUser: $scope.loggedInUser};

            wO5sConfigConfigService.getCnktList(postData).then(
                    function (resp) {
                        if (resp.data) $scope.cdLv2List = resp.data;
                    },
                    function (error) {
                        toastr.error("Có lỗi xảy ra!");
                    }
                )

        }

        // lay ft
        // $scope.autoCompleteFTOptions = {
        //     dataTextField: "ftName", placeholder: "Chọn FT",
        //     open: function (e) {
        //     },
        //     select: function (e) {
        //         data = this.dataItem(e.item.index());
        //         $scope.working5SConfig.ftName = data.fullName;
        //         $scope.working5SConfig.ftId = data.ftId;
        //         $scope.$apply();
        //     },
        //     pageSize: 10,
        //     dataSource: {
        //         serverFiltering: true,
        //         transport: {
        //             read: function (options) {
        //                 var keySearch = $scope.working5SConfig.ftName;
        //                 $scope.working5SConfig.ftId = null;
        //                 if(keySearch == ''){
        //                     $scope.working5SConfig.ftName = null;
        //                     return options.success([]);
        //                 }
        //                 return options.success(searchFT(keySearch));
        //             }
        //         }
        //     },
        //     headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
        //         '<p class="col-md-12 text-header-auto">Chọn FT</p></div>',
        //     template: '<div class="row" >' +
        //         '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.fullName #</div></div>',
        //     change: function (e) {
        //     },
        //     close: function (e) {
        //         // handle the event0
        //     }
        // };

        $scope.autoCompleteFTOptions={
            dataTextField: "code", placeholder:"Chọn FT",
            open: function(e) {
            },
            select: function(e) {
                data = this.dataItem(e.item.index());
                $scope.working5SConfig.ftName = data.fullName;
                $scope.working5SConfig.ftId = data.ftId;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function(options) {
                        var keySearch = $scope.working5SConfig.ftName;
                        $scope.working5SConfig.ftId = null;
                        if(keySearch == ''){
                            $scope.working5SConfig.ftName = null;
                            return options.success([]);
                        }

                        var objSearch = {keySearch:keySearch, cdLevel2: $scope.working5SConfig.cdLevel2};

                        return Restangular.all("woService/wo/getFtList").post(objSearch).then(
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
                '<p class="col-md-12 text-header-auto">Chọn FT</p></div>',
            template:'<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.employeeCode # - #: data.fullName #</div></div>',
            change: function(e) {
            },
            close: function(e) {
                // handle the event0
            }
        };

        function searchFT(keySearch) {
            var searchResult = [];
            for(var i = 0; i < $scope.FTList.length; i++){
                var lv2 = $scope.FTList[i];
                keySearch = keySearch.toUpperCase();
                var fullName = lv2.fullName.toUpperCase();
                if(fullName.includes(keySearch)) searchResult.push(lv2);
            }
            return searchResult;
        }

        // function getSysUserGroup(){
        //     var obj = {loggedInUser: $scope.loggedInUser}
        //     wO5sConfigConfigService.getSysUserGroup(obj).then(
        //         function(resp){
        //             if(resp && resp.data){
        //                 $scope.sysUserGroup = resp.data;
        //                 debugger;
        //                 console.log($scope.sysUserGroup);
        //                 if($scope.sysUserGroup.cdLevel1 == true){
        //                     $scope.enable.createWO = true;
        //                     $scope.enable.deleteWO = true;
        //                 }
        //                 getFTList();
        //             }
        //         },
        //         function(error){
        //             console.log(error);
        //             toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
        //         }
        //     )
        // }

        function getFTList() {
            wO5sConfigConfigService.getFTList($scope.working5SConfig.cdLevel2).then(
                function (resp) {
                    if (resp.data) $scope.FTList = resp.data;
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )

        }

        vm.searchWI= searchWI;
        function searchWI(){
        	var grid = vm.woFTConfigListTable;
        	if(grid){
        		grid.dataSource.query({
        			page: 1,
                    pageSize: 10
        		})
        	}
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
                url: Constant.BASE_SERVICE_URL + "woService/importFileConfig" ,
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if(data && data.statusCode == 1){
                        toastr.success("Import thành công! ");
                        cancelImport();
                        doSearch();
                    } else {
                        toastr.error("Import thất bại! " + data.message?data.message:'');
                        if(data.data != null){
                            confirm('Phát hiện dữ liệu không hợp lệ! Bạn có muốn xem chi tiết?', function () {
                                getImportFTResult(data.data) });
                        }
                        cancelImport();
                    }
                    cancelImport();
                    $scope.$apply();
                }
            });
        }

        function getImportFTResult(obj){
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

        vm.addDataFormExcel = addDataFormExcel;
        function addDataFormExcel(obj){
            woManagementService.createManyWo(obj).then(function (res) {
                if(res && res.statusCode == 1){
                    vm.dataWOListTable.dataSource.read();
                    toastr.success("Import dữ liệu thành công. Số bản ghi đã import: " + res.message);
                    confirm('Import dữ liệu thành công! Bạn có muốn xem chi tiết?', function () {
                        getImportExcelResult(res.data)
                    });
                }
                else{
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
                url: Constant.BASE_SERVICE_URL + "woService/importFileScheduleWoConfig" ,
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
                                getImportFTResult(data.data) });
                        }
                        cancelImport();
                    }
                    cancelImport();
                    $scope.$apply();
                }
            });
        }

        vm.deleteConfigWO5s = deleteConfigWO5s;
        function deleteConfigWO5s(ftConfigId) {
            var param = {
                ftConfigId: ftConfigId,
                loggedInUser: $scope.loggedInUser
            }
                confirm('Bạn có muốn xoá cấu hình không?', function () {
                    wO5sConfigConfigService.deleteWO5SConfig(param).then(function (res) {

                        if (res && res.statusCode == 1) toastr.success("Xóa dữ liệu thành công!");
                        else toastr.error(res.message);
                        vm.woFTConfigListTable.dataSource.read();
                    }, function (errResponse) {
                        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi xoá!"));
                    });
                })
        }

        vm.editConfigWO5s = function(ftConfigId){
            $scope.isEditing = true;
            var param = {
                ftConfigId: ftConfigId
            }
            wO5sConfigConfigService.getOneWO5SConfig(param).then(
                function(resp){
                    if(resp && resp.data){
                        // getWorkItemList();
                        $scope.working5SConfig = resp.data;
                        $scope.working5SConfig.fullName = $scope.working5SConfig.ftName;
                        $scope.working5SConfig.loggedInUser = $scope.loggedInUser;
                        //getFTList();
                        // $scope.working5SConfig.cdLevel1Name = "TTVHKT - Trung tâm vận hành khai thác";
                        // $scope.working5SConfig.woWorkItemId = resp.data.scheduleWorkItemId;
                        // $scope.working5SConfig.trCode = resp.data.woTRCode;
                        // $scope.working5SConfig.value = resp.data.cycleType;
                        // if(resp.data.state==1) $scope.ctOrPj.isState = true;
                        // else $scope.ctOrPj.isState = false;
                        // var splitted = $scope.working5SConfig.startTime.split(' ');
                        // startDateNew = splitted[0];
                        // if(startDateNew <= htmlCommonService.formatDate($scope.date)){
                        //     $scope.isEditingAll = true;
                        // }

                    }
                    $modal.open({
                        templateUrl: 'coms/wo_xl/wO5sConfigManagement/wO5sConfigCreateEditModal.html',
                        controller: null,
                        windowClass: 'app-modal-window',
                        scope: $scope
                    })
                        .result.then(
                        function() {
                            vm.updateConfig5S($scope.working5SConfig);
                            $scope.isEditing = false;
                        },
                        function() {
                            $scope.working5SConfig = {};
                            $scope.isEditing = false;
                        }
                    )
                },
                function(error){
                    toastr.success("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )

        }

        vm.updateConfig5S = function(obj){
            wO5sConfigConfigService.updateWO5SConfig(obj).then(
                function(resp){
                    if(resp && resp.statusCode == 1) toastr.success("Sửa thành công!");
                    else toastr.success(resp.message);

                    vm.woFTConfigListTable.dataSource.read();
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
            $rootScope.viewDetailsWoId = id;
            $rootScope.viewDetailsWoCode = name;
            // template.woWorkItemId = id;
            // template.workItemCode = name;
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
            var template = '<div class="display-block cedtpl" style="text-align: center">' +
                '<i ng-click="vm.editConfigWO5s('+dataItem.ftConfigId+')" class="fa fa-pencil icon-table"></i>' +
                '<i ng-click="vm.deleteConfigWO5s('+dataItem.ftConfigId+')" class="fa fa-trash-o icon-table"></i></div>';
            return template;
        }

        function fillDataTable(){

            vm.woFTConfigListTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable:false,
                save : function(){
                    vm.woFTConfigListTable.refresh();
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
                            url: Constant.BASE_SERVICE_URL + "woService/wo/doSearchWoFTConfig",
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
                        title: "Mã cấu hình công việc 5s",
                        field: 'ftConfigCode',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return dataItem.ftConfigCode;
                        },
                    },
                    {
                        title: "Tên cấu hình công việc 5s",
                        field: 'ftConfigName',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return dataItem.ftConfigName;
                        },
                    },
                    {
                        title: "Chi nhánh kỹ thuật",
                        field: 'cdLevel2Name',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function(dataItem){
                            return dataItem.cdLevel2Name ? dataItem.cdLevel2Name:'';
                        }
                    },
                    {
                        title: "FT",
                        field: 'ftName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function(dataItem){
                            return dataItem.ftName ? dataItem.ftName:'';
                        }
                    },
                    {
                        title: "Người tạo",
                        field: 'userNameCreated',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function(dataItem){
                            return dataItem.userNameCreated ? dataItem.userNameCreated:'';
                        }
                    },
                    {
                        title: "Ngày tạo",
                        field: 'createdDateString',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function(dataItem){
                            return dataItem.createdDateString ? dataItem.createdDateString:'';
                        }
                    },
                    {
                        title: "Thao tác",
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

        $scope.validateCreateNew = function(){
            var working5SConfig = $scope.working5SConfig;

            if(!working5SConfig.ftConfigCode){
                toastr.error("Mã cấu hình công công việc 5s không được để trống");
                return false;
            }

            if(!working5SConfig.ftConfigName){
                toastr.error("Tên cấu hình công việc 5s không được để trống.");
                return false;
            }

            if(!working5SConfig.cdLevel2){
                toastr.error("Chưa chọn Chi nhanh kỹ thuật");
                return false;
            }

            if(!working5SConfig.ftId){
                toastr.error("Chưa chọn FT");
                return false;
            }

            return true;
        }

        $scope.autoCompleteCdLevel2SearchOptions = {
            dataTextField: "code", placeholder: "Chọn chi nhánh kỹ thuật",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                vm.searchForm.cdLevel2Name = data.groupName;
                vm.searchForm.cdLevel2 = data.sysGroupId;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = vm.searchForm.cdLevel2Name;
                        if(keySearch == ''){
                            vm.searchForm.cdLevel2Name = null;
                            vm.searchForm.cdLevel2 = null;
                            return options.success([]);
                        }
                        vm.searchForm.cdLevel2 = null;
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

    // end controller
    }
})();
