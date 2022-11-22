(function () {
    'use strict';
    var controllerId = 'woHcqtManagementController';

    angular.module('MetronicApp').controller(controllerId, woHcqtManagementController);

    function woHcqtManagementController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                 kendoConfig, $kWindow, htmlCommonService, woManagementService, $modal, vpsPermissionService,
                                 CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http) {

        var vm = this;
        // variables
        var record = 0;
        vm.breadcrumb = "Quản lý WO > Danh sách WO";
        $scope.workingWO = {}
        vm.searchForm = {};
        vm.searchForm.woTypeCode = 'HCQT';
        $scope.loggedInUser = vm.searchForm.loggedInUser = $rootScope.casUser.userName;
        $scope.sysUserId = $rootScope.casUser.sysUserId;
        $scope.sysUserGroup = {};
        $scope.enable = {};
        $scope.WOStates={};
        $scope.apWorkSrcs = {};
        $scope.states = {};
        $scope.trs = {};
        $scope.cTypes = {};
        $scope.permissions = {};
        $scope.isDisable = {};
        $scope.label = {};
        $scope.templateData = {};
        $scope.hcqtProjects = {};
        $scope.cdLevel1Permission = null;
        $scope.cdLevel1PermissionName = null;
        $scope.woTypes = [];
        $scope.hcqtSteps = [
            {checklistStep:0, stepText: 'Chưa có ĐNQT'},
            {checklistStep:1, stepText: 'Đã có ĐNQT chưa gửi Vtnet'},
            {checklistStep:2, stepText: 'Đã gửi Vtnet chưa chốt'},
            {checklistStep:3, stepText: 'Đã chốt chưa có bảng thẩm'},
            {checklistStep:4, stepText: 'Đã có bảng thẩm PTC chưa duyệt'},
            {checklistStep:5, stepText: 'Phòng tài chính đã duyệt'},
            {checklistStep:-1, stepText: 'Phòng tài chính từ chối'},
        ];
        var dateRegex = /(^(((0[1-9]|1[0-9]|2[0-8])[\/](0[1-9]|1[012]))|((29|30|31)[\/](0[13578]|1[02]))|((29|30)[\/](0[4,6,9]|11)))[\/](19|[2-9][0-9])\d\d$)|(^29[\/]02[\/](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)/;
        init();
        function init(){
            $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
            if(!$scope.permissions.createWOHcqt && !$scope.permissions.viewWOHcqt){
                vm.searchForm.ftId = $scope.sysUserId;
            }
            fillDataTable();
            getSysUserGroup();
            getStates();
            getCdLevel1();
            getHcqtProjectList();
            getWoTypes();

            postal.subscribe({
                channel  : "Tab",
                topic    : "action",
                callback:function(data){
                    if(data.action == 'refresh') vm.dataWOListTable.dataSource.read();
                }
            });
        }

        function getWoTypes() {
            woManagementService.getWOTypes().then(
                function (resp) {
                    if (resp.data) $scope.woTypes = resp.data;
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function getCdLevel1() {
            if($scope.permissions.createWODomainDataList && $scope.permissions.createWODomainDataList != ''){
                var cdLevel1Permission = $scope.permissions.createWODomainDataList.split(",")[0];
                woManagementService.getSysGroup({sysGroupId:cdLevel1Permission}).then(
                    function (resp) {
                        console.log(resp);
                        if (resp.data){
                            $scope.cdLevel1Permission = cdLevel1Permission;
                            $scope.cdLevel1PermissionName = resp.data.groupName;
                        }

                    },
                    function (error) {
                        console.log(error);
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            }
            else if($scope.permissions.crudWOCnktDomainDataList && $scope.permissions.crudWOCnktDomainDataList != ''){
                //nếu cnkt tạo wo, fix cứng cd Level 1 là TTHT
                $scope.cdLevel1Permission = '242656';
                $scope.cdLevel1PermissionName = 'Trung tâm hạ tầng';
            }
        }

        function getHcqtProjectList(){
            woManagementService.getHcqtProjectList().then(
                function(resp){
                    console.log(resp);
                    if(resp && resp.data){
                        $scope.hcqtProjects = resp.data;
                    }
                },
                function(error){
                    console.log(error);
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )
        }

        function getSysUserGroup(){
            var obj = {loggedInUser: $scope.loggedInUser}
            woManagementService.getSysUserGroup(obj).then(
                function(resp){
                    console.log(resp);
                    if(resp && resp.data){
                        $scope.sysUserGroup = resp.data;
                        if($scope.sysUserGroup.cdLevel1 == true){
                            $scope.enable.createWO = true;
                            $scope.enable.deleteWO = true;
                        }
                    }
                },
                function(error){
                    console.log(error);
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )
        }

        vm.doSearch= doSearch;
        function doSearch(){
            var grid = vm.dataWOListTable;
            if(vm.searchForm.ftName == '') vm.searchForm.ftName = null;
            if(grid){
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                })
            }
        }

        vm.viewWODetails = function(woId){
            var template = Constant.getTemplateUrl('WO_XL_WO_DETAILS');
            $rootScope.viewDetailsWoId = woId;
            template.woId = woId;
            postal.publish({
                channel : "Tab",
                topic : "open",
                data : template
            });

            postal.publish({
                channel : "Tab",
                topic   : "action",
                data    : {action:'refresh', woId: woId}
            });
        };


        vm.editWO = function(woId){
            var obj = {woId:woId};
            woManagementService.getOneWODetails(obj).then(
                function(resp){
                    if(resp && resp.data) $scope.workingWO = resp.data;
                    $scope.isEditting = true;
                    $scope.isCreateNew = false;
                    $scope.isDisable.trChoose = true;
                    $scope.label.contractInfo = $scope.workingWO.contractCode;
                    $scope.label.constructionCode = $scope.workingWO.constructionCode;
                    if($scope.workingWO.apWorkSrc) $scope.workingWO.apWorkSrc = $scope.workingWO.apWorkSrc + '';
                    if($scope.workingWO.apConstructionType) $scope.workingWO.apConstructionType = $scope.workingWO.apConstructionType + '';
                    $modal.open({
                        templateUrl: 'coms/wo_xl/woHcqtManagement/woHcqtEditModal.html',
                        controller: this,
                        windowClass: 'app-modal-window',
                        scope: $scope
                    })
                        .result.then(
                        function() {
                            // console.log('b4 edit');
                            // console.log($scope.workingWO);
                            vm.saveWO($scope.workingWO);
                        },
                        function() {
                            $scope.workingWO = {};
                        }
                    )
                },
                function(error){
                    console.log(error);
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )

        }

        vm.saveWO = function(obj){
            obj.loggedInUser = $scope.loggedInUser;
            woManagementService.updateWO(obj).then(
                function(resp){
                    console.log(resp);

                    if(resp && resp.statusCode == 1) toastr.success("Sửa thành công!");
                    else toastr.error("Không thành công! + " + resp.message);

                    vm.dataWOListTable.dataSource.read();
                    $scope.isEditting = false;
                },
                function(error){
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        };

        vm.deleteWO = function(woId){
            var obj = {woId:woId, loggedInUser: $scope.loggedInUser};
            confirm('Xác nhận xóa bản ghi đã chọn?',
                function(){
                    woManagementService.deleteWO(obj).then(
                        function(resp){
                            if(resp && resp.statusCode == 1) toastr.success("Xóa thành công!");
                            else toastr.success("Đã xóa hoặc không tồn tại!");

                            vm.dataWOListTable.dataSource.read();
                        },
                        function(error){
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                        }
                    )
                }
            );
        };

        function getStateText(state){
            var text = '';
            $scope.WOStates.states = []
            Object.keys(Constant.WO_XL_STATE).forEach(
                function(key,index){
                    var obj = {};
                    obj.stateCode = Constant.WO_XL_STATE[key].stateCode;
                    obj.stateName = Constant.WO_XL_STATE[key].stateText;
                    $scope.WOStates.states.push(obj);

                    if(Constant.WO_XL_STATE[key].stateCode == state){
                        text = Constant.WO_XL_STATE[key].stateText;
                    }
                }
            );
            return text;
        }

        function getStates(){
            var result = [];
            Object.keys(Constant.WO_XL_STATE).forEach(function(key, index){
                result.push(Constant.WO_XL_STATE[key]);
            });
            $scope.states = result;
        }

        function createActionTemplate(woItem){

            var isEditable = false;
            var isDeleteable = false;


            if(woItem.checklistStep == 0 && $scope.permissions.createWOHcqt){
                isEditable = true;
                isDeleteable = true;
            }


            var template = '<div class="display-block cedtpl" style="text-align: center">' +
                '<i class="fa fa-list-alt icon-table" ng-click="vm.viewWODetails('+woItem.woId+')"></i>' +
                '<i ng-if="'+isEditable+'" class="fa fa-pencil icon-table" ng-click="vm.editWO('+woItem.woId+')"></i>' +
                '<i ng-if="'+isDeleteable+'" class="fa fa-trash-o icon-table" ng-click="vm.deleteWO('+woItem.woId+')"></i></div>'

            return template;
        }

        function fillDataTable(){
            vm.dataWOListTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable:false,
                save : function(){
                    vm.dataWOListTable.dataSource.read();
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
                            // // $("#appCount").text("" + response.total);
                            vm.count = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            //Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/wo/doSearchHcqtWo",
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
                        // empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
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
                        type :'text',
                    },
                    {
                        title: "Thao tác",
                        field: 'action',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        template: function(dataItem){
                            return createActionTemplate(dataItem);
                        },
                    },
                    {
                        title: "Tên WO",
                        field: 'nameWo',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return valueOrEmptyStr(dataItem.woName);
                        },
                    },
                    {
                        title: "Mã Wo",
                        field: 'codeWo',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return valueOrEmptyStr(dataItem.woCode);
                        },
                    },
                    {
                        title: "Mã trạm",
                        field: 'stationCode',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Mã tỉnh",
                        field: 'catProvinceCode',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Dự án",
                        field: 'hcqtProjectName',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "CNKV",
                        field: 'cnkv',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Hợp đồng",
                        field: 'hcqtContractCode',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text'
                    },
                    {
                        title: "Giá trị",
                        field: 'moneyValue',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'text',
                        template: function(dataItem){
                            if(!dataItem.moneyValue || dataItem.moneyValue == 0) return 0;
                            var moneyM = dataItem.moneyValue;
                            moneyM = moneyM.toFixed(3);
                            return CommonService.numberWithCommas(moneyM);
                        }
                    },
                    // {
                    //     title: "Trạng thái",
                    //     field: 'status',
                    //     width: '20%',
                    //     headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    //     attributes: {
                    //         style: "text-align:left;"
                    //     },
                    //     type :'text',
                    //     template: function(dataItem){
                    //         var txt = getStateText(dataItem.state);
                    //         if(
                    //             dataItem.state == Constant.WO_XL_STATE.opinionRq1.stateCode ||
                    //             dataItem.state == Constant.WO_XL_STATE.opinionRq2.stateCode ||
                    //             dataItem.state == Constant.WO_XL_STATE.opinionRq3.stateCode ||
                    //             dataItem.state == Constant.WO_XL_STATE.opinionRq4.stateCode
                    //         ){
                    //             if(dataItem.opinionResult != '' && dataItem.opinionResult == 'ACCEPTED') txt += ' - Đã chấp thuận ';
                    //             else if(dataItem.opinionResult != '' && dataItem.opinionResult == 'REJECTED') txt += ' - Đã từ chối ';
                    //         }
                    //
                    //         return txt
                    //     }
                    // },
                    {
                        title: "Ngày nhận HSHC",
                        field: 'hshcReceiveDate',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;" },
                        type :'text'
                    },
                    {
                        title: "Hạn hoàn thành",
                        field: 'finishDate',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text'
                    },
                    {
                        title: "Ngày DNQT",
                        field: 'dnqtDate',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;" },
                        type :'text'
                    },
                    {
                        title: "Giá trị DNQT",
                        field: 'dnqtValue',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;" },
                        type :'text'
                    },
                    {
                        title: "Người DNQT",
                        field: 'dnqtPerson',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;" },
                        type :'text'
                    },
                    {
                        title: "Chuyển VTNET",
                        field: 'vtnetSendDate',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;" },
                        type :'text'
                    },
                    {
                        title: "Chốt với VTNET",
                        field: 'vtnetConfirmDate',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;" },
                        type :'text'
                    },
                    {
                        title: "Người chốt",
                        field: 'vtnetConfirmPerson',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;" },
                        type :'text'
                    },
                    {
                        title: "Ngày bảng thẩm về",
                        field: 'aprovedDocDate',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;" },
                        type :'text'
                    },
                    {
                        title: "Giá trị chốt thẩm",
                        field: 'vtnetConfirmValue',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;" },
                        type :'text'
                    },
                    {
                        title: "Người thẩm (VTNET)",
                        field: 'aprovedPerson',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;" },
                        type :'text'
                    },
                    {
                        title: "Nội dung vướng",
                        field: 'problemContent',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;" },
                        type :'text'
                    },
                    {
                        title: "Loại vướng",
                        field: 'problemName',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;" },
                        type :'text'
                    },
                ]
            });
        }

        vm.openTabCreateNewWO = function () {
            var template = Constant.getTemplateUrl('WO_XL_WO_CREATE_NEW');
            for(var i = 0; i<$scope.woTypes.length; i++){
                if($scope.woTypes[i].woTypeCode == 'HCQT') $rootScope.selectedWoTypeForCreateNew = $scope.woTypes[i].woTypeId;
            }

            postal.publish({
                channel : "Tab",
                topic : "open",
                data : template
            });
        };

        function getValueByKeys(array, keySearch, valueSearch, keyReturn){
            if(!array || array.length == 0) return null;

            for(var i = 0; i<array.length; i++){
                var item = array[i];
                if(item[keySearch] == valueSearch) return item[keyReturn];
            }
            return null;
        }

        function trimProps(obj){
            Object.keys(obj).forEach(function (key) {
                if(obj[key] && typeof obj[key].trim === "function") obj[key] = obj[key].trim();
                if(obj[key] == '') obj[key] = null;
            });
            return obj;
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

        function getWoTypeCode(woTypeId){
            for (var i = 0; i < $scope.woTypes.length; i++) {
                if ($scope.woTypes[i].woTypeId == woTypeId) return $scope.woTypes[i].woTypeCode;
            }
        }

        function getWoTypeId(woTypeCode){
            for (var i = 0; i < $scope.woTypes.length; i++) {
                if ($scope.woTypes[i].woTypeCode == woTypeCode) return $scope.woTypes[i].woTypeId;
            }
        }

        vm.downloadHCQTTemplate = function () {
            var obj = {};
            confirm('Bạn có muốn tải file mẫu?', function(){
                obj.loggedInUser = $scope.sysUserId;
                $http({
                    url: Constant.BASE_SERVICE_URL + "woService/hcqt/getImportHCQTExcelTemplate",
                    method: "POST",
                    data: obj,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                })
                    .success(function (data, status, headers, config) {
                        saveFile(data,"WoHcqtCreateExample.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    }).error(function (data, status, headers, config) {
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                });
            })
        };


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
                setTimeout(function() {
                    document.body.removeChild(a);
                    window.URL.revokeObjectURL(url);
                }, 0);
            }
        }

        function valueOrEmptyStr(value){
            return value ? value : '';
        }

        vm.showHideColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.dataWOListTable.hideColumn(column);
            } else if (column.hidden) {
                vm.dataWOListTable.showColumn(column);
            } else {
                vm.dataWOListTable.hideColumn(column);
            }
        };

        vm.exportWoHcqtExcel = function(){
            var obj = vm.searchForm;
            obj.page = 1;
            obj.pageSize = 999999;

            confirm("Xuất dữ liệu excel?", function () {
                return Restangular.all("woService/wo/exportHcqtWo").post(obj).then(function (d) {
                    var data = d.plain();
                    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                }).catch(function (e) {
                    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                    return;
                });
            })
        }

        $scope.apWorkSrcShortName = function(name){
            return htmlCommonService.apWorkSrcShortName(name);
        };

        function validateWO(wo) {
            var woType = {};
            for(var i = 0; i<$scope.woTypes.length; i++){
                if(wo.woTypeId == $scope.woTypes[i].woTypeId) woType = $scope.woTypes[i];
            }

            if(!wo.woTypeId){
                toastr.error("Loại work order không được để trống.");
                return false;
            }

            if(!wo.apWorkSrc && woType.hasApWorkSrc == 1){
                toastr.error("Nguồn việc không được để trống.");
                return false;
            }

            if(!wo.woNameId){
                toastr.error("Tên work order không được để trống.");
                return false;
            }

            if(!wo.apConstructionType && woType.hasConstruction == 1){
                toastr.error("Loại công trình không được để trống.");
                return false;
            }

            if(!wo.constructionId && woType.hasWorkItem == 1){
                toastr.error("Mã công trình không được để trống.");
                return false;
            }

            if(!wo.catWorkItemTypeId && woType.hasWorkItem == 1){
                toastr.error("Hạng mục không được để trống.");
                return false;
            }

            if(!wo.moneyValue || wo.moneyValue <= 0){
                toastr.error("Giá trị sản lượng không được để trống.");
                return false;
            }

            if(!wo.cdLevel2){
                toastr.error("CD level 2 không được để trống.");
                return false;
            }

            if(!wo.finishDate){
                $('#inputFinishDate').focus();
                toastr.error("Hạn hoàn thành không được để trống.");
                return false;
            }

            if(!wo.qoutaTime){
                $('#inputQuotaTime').focus();
                toastr.error("Định mức thành không được để trống.");
                return false;
            }
            else{
                if(wo.qoutaTime > 10000){
                    toastr.error("Định mức hoàn thành không được quá 10000 giờ.");
                    return false;
                }
            }

            return true;
        }

        vm.importHCQTWOFromFile = function () {
            $("#hcqtFile").val(null);
            $("#hcqtFile").unbind().click(); //id của input file
            $("#hcqtFile").change(function(){
                var selectedFile = $("#hcqtFile")[0].files[0];

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

                    var hcqtFtHeader = ['sysUserId', 'employeeCode','fullName','email'];
                    var hcqtFtList = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[1]], {header: hcqtFtHeader, range:1});

                    var hcqtProjectHeader = ['hcqtProjectId', 'hcqtProjectName'];
                    var hcqtProjectList = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[2]], {header: hcqtProjectHeader, range:1});

                    var importHeader = ["ftName","apWorkSrc", "stationCode", "catProvinceCode", "hcqtProjectName", "cnkv", "contractCode", "moneyValue", "hshcReceiveDate", "finishDate", "dnqtDate", "dnqtValue", "dnqtPerson",
                        "vtnetSendDate", "vtnetConfirmDate", "vtnetConfirmPerson", "aprovedDocDate", "vtnetConfirmValue", "aprovedPerson","problemContent", "problemType"];

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[0]], {header: importHeader, range:3});

                    var validDate = true;
                    for(var i=0;i<XL_row_object.length;i++){
                        var wo = XL_row_object[i];

                        Object.keys(wo).forEach(function (key) {
                            if(key == 'undefined'){
                                toastr.error('Cấu trúc file excel không hợp lệ.');
                                return;
                            }
                        });

                        if(wo.ftName){
                            wo.ftId = getValueByKeys(hcqtFtList, 'fullName', wo.ftName, 'sysUserId');
                            wo.ftEmail = getValueByKeys(hcqtFtList, 'fullName', wo.ftName, 'email');
                        }

                        if(wo.hcqtProjectName){
                            wo.hcqtProjectId = getValueByKeys(hcqtProjectList, 'hcqtProjectName', wo.hcqtProjectName, 'hcqtProjectId');
                        }

                        if(wo.dnqtPerson){
                            wo.dnqtPersonId = getValueByKeys(hcqtFtList, 'fullName', wo.dnqtPerson, 'sysUserId');
                        }

                        if(wo.vtnetConfirmPerson){
                            wo.vtnetConfirmPersonId = getValueByKeys(hcqtFtList, 'fullName', wo.vtnetConfirmPerson, 'sysUserId');
                        }

                        wo.userCreated = $scope.loggedInUser;
                        wo.cdLevel1 = $scope.cdLevel1Permission;
                        wo.cdLevel1Name = $scope.cdLevel1PermissionName;
                        wo.loggedInUser = $scope.loggedInUser;
                        wo.sysUserId = $scope.sysUserId;
                        wo.sysGroupId = $scope.sysUserGroup.sysGroupId;
                        wo.woId = 0;
                        wo.status = 1;
                        wo.woTypeCode = 'HCQT';
                        wo.woTypeId = getWoTypeId('HCQT');
                        //wo.moneyValue = wo.moneyValue*1000000;
                        wo.woName = 'WO hoàn công quyết toán';
                        wo.woNameId = getValueByKeys($scope.woNameList, 'name', wo.woName, 'id');
                        wo.hcqtContractCode = wo.contractCode;

                        wo.createdUserFullName = $rootScope.casUser.fullName;
                        wo.createdUserEmail = $rootScope.casUser.email;

                        wo = trimProps(wo);
                        wo.apWorkSource = wo.apWorkSrc;
                        if(wo.apWorkSrc == null){
                        	 validDate = false;
                             wo.customField = "Nguồn việc không được để trống;";
                        }else{
                        	var ws = wo.apWorkSrc.trim().substring(0, 1);
                        	if(ws == "1" || ws == "2"){
                        		wo.apWorkSrc = ws;
                        	}else{
                        		wo.apWorkSrc = null;
                        		validDate = false;
                        		wo.customField = "Nguồn việc không hợp lệ;";
                        	}
                        }

                        if(wo.finishDate && wo.finishDate!= ''){
                            if(!dateRegex.test(wo.finishDate)){
                                validDate = false;
                                wo.finishDateStr = wo.finishDate;
                                wo.finishDate = null;
                                wo.customField = "Ngày tháng không đúng định dạng dd/MM/yyyy;";
                            }
                        }

                        if(wo.hshcReceiveDate && wo.hshcReceiveDate!= ''){
                            if(!dateRegex.test(wo.hshcReceiveDate)){
                                validDate = false;
                                wo.hshcReceiveDateStr = wo.hshcReceiveDate;
                                wo.hshcReceiveDate = null;
                                wo.customField = "Ngày tháng không đúng định dạng dd/MM/yyyy;";
                            }
                        }

                        if(wo.dnqtDate && wo.dnqtDate!= ''){
                            if(!dateRegex.test(wo.dnqtDate)){
                                validDate = false;
                                wo.dnqtDateStr = wo.dnqtDate;
                                wo.dnqtDate = null;
                                wo.customField = "Ngày tháng không đúng định dạng dd/MM/yyyy;";
                            }
                        }

                        if(wo.vtnetSendDate && wo.vtnetSendDate!= ''){
                            if(!dateRegex.test(wo.vtnetSendDate)){
                                validDate = false;
                                wo.vtnetSendDateStr = wo.vtnetSendDate;
                                wo.vtnetSendDate = null;
                                wo.customField = "Ngày tháng không đúng định dạng dd/MM/yyyy;";
                            }
                        }

                        if(wo.vtnetConfirmDate && wo.vtnetConfirmDate!= ''){
                            if(!dateRegex.test(wo.vtnetConfirmDate)){
                                validDate = false;
                                wo.vtnetConfirmDateStr = wo.vtnetConfirmDate;
                                wo.vtnetConfirmDate = null;
                                wo.customField = "Ngày tháng không đúng định dạng dd/MM/yyyy;";
                            }
                        }

                        if(wo.aprovedDocDate && wo.aprovedDocDate!= ''){
                            if(!dateRegex.test(wo.aprovedDocDate)){
                                validDate = false;
                                wo.aprovedDocDateStr = wo.aprovedDocDate;
                                wo.aprovedDocDate = null;
                                wo.customField = "Ngày tháng không đúng định dạng dd/MM/yyyy;";
                            }
                        }

                    }

                    //console.log(XL_row_object);
                    if(XL_row_object.length == 0){
                        toastr.error('File excel không có dữ liệu hoặc không hợp lệ.');
                        return;
                    }

                    if(!validDate){
                        toastr.error('Dữ liệu không hợp lệ, không thể gửi!');
                        confirm('Phát hiện dữ liệu không hợp lệ! Bạn có muốn xem chi tiết?', function () {
                            getHCQTImportExcelResult(XL_row_object);
                        });
                        return;
                    }

                    var json_object = JSON.stringify(XL_row_object);
                    addDataHCQTFromExcel(json_object);
                };
                reader.onerror = function(event) {
                    console.error("File could not be read! Code " + event.target.error.code);
                };
                reader.readAsBinaryString(selectedFile);
            });
        };

        vm.addDataHCQTFromExcel = addDataHCQTFromExcel;
        function addDataHCQTFromExcel(obj){
            //console.log(obj);
            woManagementService.createManyHCQTWo(obj).then(function (res) {
                if(res && res.statusCode == 1){
                    vm.dataWOListTable.dataSource.read();
                    toastr.success("Import dữ liệu thành công. " + res.message);
                    confirm('Import dữ liệu thành công! Bạn có muốn xem chi tiết?', function () {
                        getHCQTImportExcelResult(res.data)
                    });
                }
                else{
                    toastr.error("Phát hiện dữ liệu không hợp lệ!");
                    confirm('Phát hiện dữ liệu không hợp lệ! Bạn có muốn xem chi tiết?', function () {
                        getHCQTImportExcelResult(res.data)
                    });
                }
            }, function (error) {
                toastr.error("Không thành công! ");
            })
        }

        function getHCQTImportExcelResult(obj){
            $http({
                url: Constant.BASE_SERVICE_URL + "woService/wo/createManyHcqtWOReport",
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

        $scope.validateEdit = function(){
            var wo = $scope.workingWO;

            if(!wo.hcqtProjectId){
                toastr.error("Dự án HCQT không được để trống.");
                return false;
            }

            if(!wo.hshcReceiveDate){
                toastr.error("Ngày nhận HSHC không được để trống.");
                return false;
            }

            if(!wo.cnkv){
                toastr.error("Chi nhánh khu vực không được để trống.");
                return false;
            }

            if(!wo.catProvinceCode){
                toastr.error("Mã tỉnh không được để trống.");
                return false;
            }

            if(!wo.moneyValue){
                toastr.error("Giá trị sản lượng không được để trống.");
                return false;
            }

            if(!wo.finishDate){
                toastr.error("Hạn hoàn thành không được để trống.");
                return false;
            }

            return true;
        };

        $scope.autoCompleteFTOptions = {
            dataTextField: "code", placeholder: "Chọn người thực hiện",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                $scope.workingWO.ftName = vm.searchForm.ftName = data.fullName;
                $scope.workingWO.ftId = vm.searchForm.ftId = data.ftId;
                $scope.workingWO.ftEmail = data.email;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = '';

                        if($scope.isEditting) keySearch = $scope.workingWO.ftName;
                        else keySearch = vm.searchForm.ftName;

                        if(keySearch == ''){
                            $scope.workingWO.ftName = null;
                            $scope.workingWO.ftId = vm.searchForm.ftId = null;
                            $scope.workingWO.ftEmail = null;
                            return options.success([]);
                        }

                        var obj = {};
                        obj.keySearch = keySearch;
                        obj.sysGroupId = 242656;

                        return Restangular.all("woService/wo/getFtListFromLv2SysGroup").post(obj).then(
                            function (response) {
                                $scope.ftList = response.data;
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
                '<p class="col-md-12 text-header-auto">Chọn người thực hiện</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.employeeCode # - #: data.fullName #</div></div>',
            change: function (e) {
            },
            close: function (e) {
                // handle the event0
            }
        };

// end controller
    }
})();
