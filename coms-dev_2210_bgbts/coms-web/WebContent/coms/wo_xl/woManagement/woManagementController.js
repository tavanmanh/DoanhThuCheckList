(function () {
    'use strict';
    var controllerId = 'woManagementController';

    angular.module('MetronicApp').controller(controllerId, woManagementController);

    function woManagementController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                    kendoConfig, $kWindow, htmlCommonService, woManagementService, $modal, vpsPermissionService,
                                    CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $compile) {

        var vm = this;
        // variables
        var record = 0;
        vm.breadcrumb = "Quản lý WO > Danh sách WO";
        $scope.workingWO = {}
        vm.searchForm = {};
        $scope.loggedInUser = vm.searchForm.loggedInUser = $rootScope.casUser.userName;
        $scope.sysUserId = $rootScope.casUser.sysUserId;
        $scope.sysUserGroup = {};
        $scope.enable = {};
        $scope.unassignState = 'UNASSIGN';
        $scope.WOStates = {};
        $scope.apWorkSrcs = {};
        $scope.states = {};
        $scope.trs = {};
        $scope.cTypes = {};
        $scope.permissions = {};
        $scope.isDisable = {};
        $scope.label = {};
        $scope.templateData = {};
        $scope.woTypes = [];
        $scope.selectedWoType = {};
        $scope.cdLevel1Permission = null;
        $scope.cdLevel1PermissionName = null;
        $scope.woNameList = {};
        $scope.vhktSysGroupId = '270120';
        $scope.currentPageWoList = [];
        $scope.selectedWoList = [];
        $scope.massBtnDisable = false;
        $scope.massModalType = '';
        $rootScope.hshcVhkt = false;
        vm.checkRoleDeleteTTHT = false; //HienLT56 add 27052021
        var dateRegex = /(^(((0[1-9]|1[0-9]|2[0-8])[\/](0[1-9]|1[012]))|((29|30|31)[\/](0[13578]|1[02]))|((29|30)[\/](0[4,6,9]|11)))[\/](19|[2-9][0-9])\d\d$)|(^29[\/]02[\/](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)/;

        $scope.overdueApproveState = [
            {code: 'WAIT', value: 'Chờ duyệt'},
            {code: 'APPROVED', value: 'Được chấp thuận'},
            {code: 'REJECTED', value: 'Bị từ chối'},
        ]
        $scope.tcTctEmails = [];

        init();

        function init() {
            $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
            fillDataTable();
            getSysUserGroup();
            getAppWorkSrcs();
            getStates();
            getCdLevel1();
            getWoTypes();
            getApConstructionTypes();
            getCdLv2List();
            getWoNameList();
            getTcTctEmails();
            initDate();
            //HienLT56 start 27052021
            woManagementService.checkRoleDeleteTTHT().then(function(d){
            	if(d && d == 'OK'){
            		vm.checkRoleDeleteTTHT = true;
            	} else{
            		vm.checkRoleDeleteTTHT = false;
            	}
            }).catch(function () {
                vm.checkRoleDeleteTTHT = false;
            });
            //HienLT56 end 27052021
            
            postal.subscribe({
                channel: "Tab",
                topic: "action",
                callback: function (data) {
                    if (data.action == 'refresh') vm.dataWOListTable.dataSource.read();
                }
            });
        }

        function initDate(){
            var now = new Date();
            var startTime = now.getTime() - 30*24*60*60*1000;
            var startDate = new Date(startTime);
            vm.searchForm.createdDateFrom = htmlCommonService.formatDate(startDate);
            vm.searchForm.createdDateTo = htmlCommonService.formatDate(now);

        }

        function getWoNameList() {
            woManagementService.getWoNameList().then(
                function (resp) {
                    if (resp.data && resp.statusCode == 1) {
                        $scope.woNameList = resp.data;
                    }

                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function getCdLevel1() {
            if ($scope.permissions.createWODomainDataList && $scope.permissions.createWODomainDataList != '') {
                var cdLevel1Permission = $scope.permissions.createWODomainDataList.split(",")[0];
                woManagementService.getSysGroup({sysGroupId: cdLevel1Permission}).then(
                    function (resp) {
                        console.log(resp);
                        if (resp.data) {
                            $scope.cdLevel1Permission = cdLevel1Permission;
                            $scope.cdLevel1PermissionName = resp.data.groupName;
                        }

                    },
                    function (error) {
                        console.log(error);
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            } else if ($scope.permissions.createWODOANHTHU && $scope.permissions.createWODOANHTHUDomainDataList != '') {
                var cdLevel1Permission = $scope.permissions.createWODOANHTHUDomainDataList.split(",")[0];
                woManagementService.getSysGroup({sysGroupId: cdLevel1Permission}).then(
                    function (resp) {
                        console.log(resp);
                        if (resp.data) {
                            $scope.cdLevel1Permission = cdLevel1Permission;
                            $scope.cdLevel1PermissionName = resp.data.groupName;
                        }

                    },
                    function (error) {
                        console.log(error);
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            } else if ($scope.permissions.crudWOCnktDomainDataList && $scope.permissions.crudWOCnktDomainDataList != '') {
                //nếu cnkt tạo wo, fix cứng cd Level 1 là TTHT
                $scope.cdLevel1Permission = '242656';
                $scope.cdLevel1PermissionName = 'Trung tâm hạ tầng';
            }
        }

        function getCdLv2List() {
            var postData = {loggedInUser: $scope.loggedInUser}
            woManagementService.getCdLv2List(postData).then(
                function (resp) {
                    console.log(resp);
                    if (resp.data) $scope.cdLv2List = resp.data;

                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function getWoTypes() {
            woManagementService.getWOTypes().then(
                function (resp) {
                    if (resp.data) $scope.woTypes = resp.data;
                    $scope.woTypes = $scope.woTypes.filter(function (item) {
                        return item.woTypeCode != 'HCQT';
                    })
                },
                function (error) {
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function getApConstructionTypes() {
            woManagementService.getApConstructionTypes().then(
                function (resp) {
                    console.log(resp);
                    if (resp.data) $scope.cTypes = resp.data;

                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        };

        function getSysUserGroup() {
            var obj = {loggedInUser: $scope.loggedInUser}
            woManagementService.getSysUserGroup(obj).then(
                function (resp) {
                    console.log(resp);
                    if (resp && resp.data) {
                        $scope.sysUserGroup = resp.data;
                        if ($scope.sysUserGroup.cdLevel1 == true) {
                            $scope.enable.createWO = true;
                            $scope.enable.deleteWO = true;
                        }
                    }
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )
        }
        vm.changeItem = changeItem;
        function changeItem(code){
			if(code == null || code == ""){
				/*vm.listItem = [];
				vm.searchForm.item = null;*/
				if(vm.searchForm.apWorkSrc == 6){
	        		woManagementService.getListItemByWorkSrc().then(
			                function (resp) {
			                	vm.listItem = resp.data;
			                })
	        	}else{
	        		vm.searchForm.item = null;
	        		vm.listItem = [];
	        	}
			}
		}

        vm.doSearch = doSearch;

        function doSearch() {
            var grid = vm.dataWOListTable;
        	if(vm.searchForm.listWoTypeId != undefined){
        		var checkShow = vm.searchForm.listWoTypeId.includes("261",0);
        		if(checkShow == true){
        			$("#dataWOListTable").data("kendoGrid").showColumn("jobUnfinished");
        		}else{
        			$("#dataWOListTable").data("kendoGrid").hideColumn("jobUnfinished");
        		}
        	}

            if (vm.searchForm.toDate != null && vm.searchForm.toDate !== '') {
                var date = new Date();
                var from = new Date(date.getFullYear(), date.getMonth(), date.getDate());
                vm.searchForm.toDate = htmlCommonService.formatDate(from);
                var day = vm.searchForm.toDate.substring(0, 2);
                var month = vm.searchForm.toDate.substring(3, 5);
                var year = vm.searchForm.toDate.substring(6, 10);
                vm.searchForm.toDate = year + '-' + month + '-' + day;
            }

            if (vm.searchForm.dateFrom != null && vm.searchForm.dateFrom !== '') {
                var date = new Date();
                var from = new Date(date.getFullYear(), date.getMonth(), date.getDate());
                vm.searchForm.dateFrom = htmlCommonService.formatDate(from);
                var day = vm.searchForm.dateFrom.substring(0, 2);
                var month = vm.searchForm.dateFrom.substring(3, 5);
                var year = vm.searchForm.dateFrom.substring(6, 10);
                vm.searchForm.dateFrom = year + '-' + month + '-' + day;
            }
            if (grid) {
                Object.keys(vm.searchForm).forEach(function (key) {
                    if (vm.searchForm[key] == "") vm.searchForm[key] = null;
                });
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                })
            }
        }

        vm.viewWODetails = function (woId) {
        	
        	removeTab("Chi tiết WO"); //Huypq-22072021 code remove tab before open new tab
        	
            var template = Constant.getTemplateUrl('WO_XL_WO_DETAILS');
            $rootScope.viewDetailsWoId = woId;
            template.woId = woId;
            postal.publish({
                channel: "Tab",
                topic: "open",
                data: template
            });

            postal.publish({
                channel: "Tab",
                topic: "action",
                data: {action: 'refresh', woId: woId}
            });
        };

        //Huypq-22072021 code remove tab before open new tab
        function removeTab (title) {
            for(var i=0; i<$scope.tabs.length; i++){
            	if($scope.tabs[i].title==title){
            		$scope.tabs.splice(i, 1);
            		break;
            	}
            }
		};
        //Huypq-22072021-end
        
        vm.editWO = function (woId) {
            var obj = {woId: woId};
            woManagementService.getOneWODetails(obj).then(
                function (resp) {
                    if (resp && resp.data) $scope.workingWO = resp.data;
                    $scope.isEditting = true;
                    $scope.isCreateNew = false;
                    $scope.isDisable.trChoose = true;
                    $scope.label.contractInfo = $scope.workingWO.contractCode;
                    $scope.label.constructionCode = $scope.workingWO.constructionCode;
                    if ($scope.workingWO.apWorkSrc) $scope.workingWO.apWorkSrc = $scope.workingWO.apWorkSrc + '';
                    if ($scope.workingWO.apConstructionType) $scope.workingWO.apConstructionType = $scope.workingWO.apConstructionType + '';
                    $modal.open({
                        templateUrl: 'coms/wo_xl/woManagement/woEditModal.html',
                        controller: 'woCreateEditTemplateController',
                        windowClass: 'app-modal-window',
                        scope: $scope
                    })
                        .result.then(
                        function () {
                            // console.log('b4 edit');
                            // console.log($scope.workingWO);
                            vm.saveWO($scope.workingWO);
                        },
                        function () {
                            $scope.workingWO = {};
                        }
                    )
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )

        }

        vm.saveWO = function (obj) {
            //var model = vm.dtoToModel(obj);
            obj.loggedInUser = $scope.loggedInUser;
            if ($scope.workingWO.woTypeCode == 'DOANHTHU') obj.state = 'PROCESSING';
            woManagementService.updateWO(obj).then(
                function (resp) {
                    console.log(resp);

                    if (resp && resp.statusCode == 1) toastr.success("Sửa thành công!");
                    else toastr.error("Đã xóa hoặc không tồn tại!");

                    vm.dataWOListTable.dataSource.read();
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        vm.dtoToModel = function (obj) {
            var model = {}
            model.woId = obj.woId;
            model.trId = obj.trId;
            model.woName = obj.woName;
            model.woCode = obj.woCode;
            model.apWorkSrc = obj.apWorkSrc;
            model.woTypeId = obj.woTypeId;
            model.constructionId = obj.constructionId;
            model.stationCode = obj.stationCode;
            model.quantityValue = obj.quantityValue;
            model.executeMethod = obj.executeMethod;
            model.state = obj.state;
            model.ftId = obj.ftId;
            model.finishDate = obj.finishDate;
            model.qoutaTime = obj.qoutaTime;
            model.executeLat = obj.executeLat;
            model.executeLong = obj.executeLong;
            model.userCreated = obj.userCreated;
            model.cdLevel1 = obj.cdLevel1;
            model.cdLevel2 = obj.cdLevel2;
            model.cdLevel3 = obj.cdLevel3;
            model.createdDate = obj.createdDate;
            model.finishDate = obj.finishDate;
            model.acceptTime = obj.acceptTime;
            model.endTime = obj.endTime;
            model.apConstructionType = obj.apConstructionType;
            model.catWorkItemTypeId = obj.catWorkItemTypeId;
            model.startTime = obj.startTime;
            model.opinionResult = obj.opinionResult;
            model.status = obj.status;
            model.woNameId = obj.woNameId;
            model.totalMonthPlanId = obj.totalMonthPlanId;
            model.moneyValue = obj.moneyValue;
            return model;
        }

        vm.deleteWO = function (woId) {
            var obj = {woId: woId, loggedInUser: $scope.loggedInUser};
            confirm('Xác nhận xóa bản ghi đã chọn?',
                function () {
                    woManagementService.deleteWO(obj).then(
                        function (resp) {
                            if (resp && resp.statusCode == 1) toastr.success("Xóa thành công!");
                            else toastr.success("Đã xóa hoặc không tồn tại!");

                            vm.dataWOListTable.dataSource.read();
                        },
                        function (error) {
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                        }
                    )
                }
            );
        }

        function getStateText(state) {
            var text = '';
            Object.keys(Constant.WO_XL_STATE).forEach(
                function (key, index) {
                    var obj = {};
                    obj.stateCode = Constant.WO_XL_STATE[key].stateCode;
                    obj.stateName = Constant.WO_XL_STATE[key].stateText;

                    if (Constant.WO_XL_STATE[key].stateCode == state) {
                        text = Constant.WO_XL_STATE[key].stateText;
                    }
                }
            );
            return text;
        }

        function getStates() {
            var result = [];
            Object.keys(Constant.WO_XL_STATE).forEach(function (key, index) {
                result.push(Constant.WO_XL_STATE[key]);
            });
            $scope.states = result;
        }

        function getAppWorkSrcs() {
            woManagementService.getAppWorkSrcs().then(
                function (resp) {
                    console.log(resp);
                    if (resp.data) $scope.apWorkSrcs = resp.data;

                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function createActionTemplate(woItem) {
            var isEditable = false;
            var isDeleteable = false;
            //HienLT56 start 31052021
            //Xóa WO với miền trụ TTHT:
            if(vm.checkRoleDeleteTTHT && (woItem.state == 'ASSIGN_CD' || woItem.state == 'ASSIGN_FT' || woItem.state == 'ACCEPT_FT' || woItem.state == 'ACCEPT_CD' 
            	|| woItem.state == 'REJECT_FT' || woItem.state == 'PROCESSING' || woItem.state == 'DONE')){
            	isDeleteable = true;
            }
            if(!vm.checkRoleDeleteTTHT){
            //HienLT56 end 31052021
            	if (woItem.state == 'UNASSIGN' || woItem.state == 'ASSIGN_CD' || woItem.state == 'ACCEPT_CD' || woItem.state == 'ASSIGN_FT' || woItem.state == 'REJECT_FT') {
                    //type = 1 là wo của cnkt cần có quyền crud cnkt
                    if (woItem.type == 1) {
                        if ($scope.permissions.crudWOCnkt) {
                            isEditable = true;
                            isDeleteable = true;
                        }
                    } else if (($scope.permissions.createdWoUctt && woItem.woTypeCode == 'UCTT') || ($scope.permissions.createdWoHshc && woItem.woTypeCode == 'HSHC')) {
                        // Neu P.KT tao wo UCTT, hoac P.KD tao wo HSHC thi cung co quyen sua xoa
                        isEditable = true;
                        isDeleteable = true;
                    } else {
                        if ($scope.permissions.updateWO && woItem.woTypeCode != 'BDDK') isEditable = true;
                        if ($scope.permissions.deleteWO) isDeleteable = true;
                    }
                }
            }
            if (woItem.woTypeCode == "5S") isDeleteable = false; //không cho xóa wo 5s

            if ($scope.permissions.createWODOANHTHU && woItem.woTypeCode == "DOANHTHU" && (woItem.state == 'WAIT_TC_TCT' || woItem.state == 'NG')) isEditable = true;


            var template = '<div class="display-block cedtpl" style="text-align: center">' +
                '<i class="fa fa-list-alt icon-table" ng-click="vm.viewWODetails(' + woItem.woId + ')"></i>' +
                '<i ng-if="' + isEditable + '" class="fa fa-pencil icon-table" ng-click="vm.editWO(' + woItem.woId + ')"></i>' +
                '<i ng-if="' + isDeleteable + '" class="fa fa-trash-o icon-table" ng-click="vm.deleteWO(' + woItem.woId + ')"></i></div>'

            return template;
        }

        function isHiddenCheckbox() {
            var isHidden = true;
            if ($scope.permissions.approveTcBranch || $scope.permissions.approveTcTct) isHidden = false;
            return isHidden;
        }

        vm.toggleSelectWo = function (e, id) {
            //console.log(e.currentTarget.checked + '-' + id);
            var checked = e.currentTarget.checked;
            if (checked) {
                for (var i = 0; i < $scope.currentPageWoList.length; i++) {
                    if ($scope.currentPageWoList[i].woId == id) {
                        $scope.selectedWoList.push($scope.currentPageWoList[i]);
                        break;
                    }
                }
            } else {
                $scope.selectedWoList = $scope.selectedWoList.filter(function (item) {
                    if (item.woId != id) return item;
                })
            }

            console.log($scope.selectedWoList);
        }

        function checkIfChecked(woId) {
            for (var i = 0; i < $scope.selectedWoList.length; i++) {
                if ($scope.selectedWoList[i].woId == woId) return 'checked';
            }
        }

        function fillDataTable() {
            vm.dataWOListTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                save: function () {
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
                            $scope.currentPageWoList = response.data;
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            //Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/wo/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page
                            vm.searchForm.pageSize = options.pageSize
                            vm.searchForm.sysUserId = $scope.sysUserId;
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
                        type: 'text',
                    },
                    {
                        title: " ",
                        field: 'selected',
                        width: '30px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        hidden: isHiddenCheckbox(),
                        template: function (dataItem) {
                            var woId = dataItem.woId;
                            var checked = checkIfChecked(woId);
                            var content = '';
                            if (($scope.permissions.approveTcBranch && (dataItem.state == 'WAIT_TC_BRANCH' || dataItem.state == 'TC_TCT_REJECTED'))
                                || ($scope.permissions.approveTcTct && dataItem.state == 'WAIT_TC_TCT')) {
                                content = '<input type="checkbox" value="' + woId + '" ' + checked + ' ng-click="vm.toggleSelectWo($event, ' + woId + ')">';
                            }
                            return content;
                        },
                    },
                    {
                        title: "Tên WO",
                        field: 'nameWo',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.woName);
                        },
                    },
                    {
                        title: "Mã Wo",
                        field: 'codeWo',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.woCode);
                        },
                    },
                    {
                        title: "Mã Tr",
                        field: "trCode",
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.trCode);
                        },
                        hidden: true,
                    },
                    {
                        title: "Nguồn việc",
                        field: 'sourceWork',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return htmlCommonService.apWorkSrcShortName(valueOrEmptyStr(dataItem.apWorkSrcName));
                        }
                    },
                    //Huypq-01092020-start
                    {
                        title: "Hợp đồng",
                        field: 'contractCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            if (dataItem.hcqtContractCode) return valueOrEmptyStr(dataItem.hcqtContractCode);
                            else return valueOrEmptyStr(dataItem.contractCode);
                        }
                    },
                    {
                        title: "Mã trạm",
                        field: 'stationCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Công trình",
                        field: 'constructionCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    //huy-end
                    {
                        title: "Giá trị (Triệu VND)",
                        field: 'moneyValue',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            if (!dataItem.moneyValue || dataItem.moneyValue == 0) return 0;
                            var moneyM = dataItem.moneyValue / 1000000;
                            moneyM = moneyM.toFixed(3);
                            moneyM = CommonService.removeTrailingZero(moneyM);
                            return !!CommonService.numberWithCommas(moneyM) ? CommonService.numberWithCommas(moneyM) : 0;
                        }
                    },{
                        title: "Hạng mục",
                        field: 'itemName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Tên FT",
                        field: 'ftName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Email FT",
                        field: 'ftEmailSysUser',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Chức vụ FT",
                        field: 'ftPositionName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Trạng thái",
                        field: 'status',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                        	var txt = '';
                        	if(dataItem.opinionType!=null && dataItem.state!='PAUSE'){
                        		txt = CommonService.getStateText(dataItem.stateVuong);
                        	} else {
                        		txt = CommonService.getStateText(dataItem.state);
                                if (
                                    dataItem.state == Constant.WO_XL_STATE.opinionRq1.stateCode ||
                                    dataItem.state == Constant.WO_XL_STATE.opinionRq2.stateCode ||
                                    dataItem.state == Constant.WO_XL_STATE.opinionRq3.stateCode ||
                                    dataItem.state == Constant.WO_XL_STATE.opinionRq4.stateCode
                                ) {
                                    if (dataItem.opinionResult != '' && dataItem.opinionResult == 'ACCEPTED') txt += ' - Đã chấp thuận ';
                                    else if (dataItem.opinionResult != '' && dataItem.opinionResult == 'REJECTED') txt += ' - Đã từ chối ';
                                }
                        	}
                            return txt;
                        }
                    },
                    {
                    	title : "Việc chưa hoàn thành",
						field : 'jobUnfinished',
						width : '20%',
						headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
                        attributes: { style: "text-align:left;"},
                        hidden: true,
                    },
                    {
                        title: "Hạn hoàn thành",
                        field: 'dateComplete',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.finishDate);
                        }
                    },
                    {
                        title: "Thao tác",
                        field: 'dateComplete',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return createActionTemplate(dataItem);
                        }
                    }
                ]
            });
        }

        vm.openTabCreateNewWO = function () {
        	if($scope.permissions.createdWoHshc){
        		$rootScope.hshcVhkt = true;
        	}
            var template = Constant.getTemplateUrl('WO_XL_WO_CREATE_NEW');
            postal.publish({
                channel: "Tab",
                topic: "open",
                data: template
            });
        };

        function getValueByKeys(array, keySearch, valueSearch, keyReturn) {
            if (!array || array.length == 0) return null;

            for (var i = 0; i < array.length; i++) {
                var item = array[i];
                if (item[keySearch] == valueSearch) return item[keyReturn];
            }
            return null;
        }

        vm.importWOFromFile = function () {
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
                        "moneyValue", "finishDate", "qoutaTime","cdLevel2Name", "moneyFlowBill", "moneyFlowDate", "moneyFlowValue", "moneyFlowRequired", "moneyFlowContent", "checklistItemNames", "cdLevel5Email", "description", "vtnetWoCode", "partner"];

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[0]], {
                        header: importHeader,
                        range: 2
                    });

                    var validDate = true;
                    for (var i = 0; i < XL_row_object.length; i++) {
                        var wo = XL_row_object[i];
                        wo.xdddChecklist = [];

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

                        if (wo.cdLevel1 == $scope.vhktSysGroupId) {
                            wo.type = 2;
                        }

                        var moneyValue = wo.moneyValue;
                        wo = trimProps(wo);
                        if (moneyValue == 0) wo.moneyValue = 0;

                        // if(wo.checklistItems && wo.checklistItems != '' && wo.woTypeCode == 'THICONG' && wo.apWorkSrc == 6){
                        //     wo.moneyValue = 0;
                        //     var checklistItems = wo.checklistItems.split('|');
                        //     for(var i = 0; i<checklistItems.length; i++){
                        //         if(checklistItems[i] && checklistItems[i] != ''){
                        //             var obj = {name:checklistItems[i]};
                        //             wo.xdddChecklist.push(obj);
                        //         }
                        //     }
                        // }

                        if (!CommonService.isAValidDate(wo.finishDate, true)) {
                            validDate = false;
                            wo.customField = "Hạn hoàn thành không đúng định dạng hoặc nhỏ hơn ngày hiện tại; ";
                        }
                        
                        if (wo.moneyFlowDate && wo.moneyFlowDate.trim() != '' && !CommonService.isAValidDate(wo.moneyFlowDate, false)) {
                            validDate = false;
                            wo.customField = "Ngày hóa đơn không hợp lệ; ";
                        }

                        if (wo.cdLevel2 && wo.cdLevel2 != '' && wo.cdLevel5Email && wo.cdLevel5Email != '') {
                            validDate = false;
                            wo.customField = "Wo giao cho trưởng công trình CD lv5 không có CD lv2; ";
                        }
                    }
                    kendo.ui.progress($("#woManagementContainer"), true); //Huypq-25052021-add load
                    //console.log(XL_row_object);
                    if (XL_row_object.length == 0) {
                        toastr.error('File excel không hợp lệ.');
                        kendo.ui.progress($("#woManagementContainer"), false);
                        return;
                    }
                    
                    if (!validDate) {
                        //toastr.error('Ngày tháng không hợp lệ.');
                        confirm('Phát hiện dữ liệu không hợp lệ! Bạn có muốn xem chi tiết?', function () {
                            getImportExcelResult(XL_row_object);
                        });
                        kendo.ui.progress($("#woManagementContainer"), false);
                        return;
                    }

                    var json_object = JSON.stringify(XL_row_object);
                    addDataFormExcel(json_object);
                };
                reader.onerror = function (event) {
                    console.error("File could not be read! Code " + event.target.error.code);
                    kendo.ui.progress($("#woManagementContainer"), false);
                };
                reader.readAsBinaryString(selectedFile);
            });
        };

        function trimProps(obj) {
            Object.keys(obj).forEach(function (key) {
                if (obj[key] && typeof obj[key].trim === "function") obj[key] = obj[key].trim();
                if (obj[key] == '') obj[key] = null;
            })
            return obj;
        }

        vm.addDataFormExcel = addDataFormExcel;

        function addDataFormExcel(obj) {
            woManagementService.createManyWo(obj).then(function (res) {
                if (res && res.statusCode == 1) {
                    vm.dataWOListTable.dataSource.read();
                    kendo.ui.progress($("#woManagementContainer"), false);
                    toastr.success("Import dữ liệu thành công. Số bản ghi đã import: " + res.message);
                    confirm('Import dữ liệu thành công! Bạn có muốn xem chi tiết?', function () {
                        getImportExcelResult(res.data)
                    });
                } else {
                	kendo.ui.progress($("#woManagementContainer"), false);
                    toastr.error("Phát hiện dữ liệu không hợp lệ!");
                    confirm('Phát hiện dữ liệu không hợp lệ! Bạn có muốn xem chi tiết?', function () {
                        getImportExcelResult(res.data)
                    });
                }
            }, function (error) {
            	kendo.ui.progress($("#woManagementContainer"), false);
                toastr.error("Không thành công! ");
            })
        }

        function getImportExcelResult(obj) {
            $http({
                url: Constant.BASE_SERVICE_URL + "woService/wo/createManyWOReport",
                method: "POST",
                data: obj,
                headers: {
                    'Content-type': 'application/json'
                },
                responseType: 'arraybuffer'
            }).success(function (data, status, headers, config) {
            	kendo.ui.progress($("#woManagementContainer"), false);
                htmlCommonService.saveFile(data, "Ket_qua_import.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            }).error(function (data, status, headers, config) {
                toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                kendo.ui.progress($("#woManagementContainer"), false);
            });
        }

        function getWoTypeCode(woTypeId) {
            for (var i = 0; i < $scope.woTypes.length; i++) {
                if ($scope.woTypes[i].woTypeId == woTypeId) return $scope.woTypes[i].woTypeCode;
            }
        }

        function getWoTypeId(woTypeCode) {
            for (var i = 0; i < $scope.woTypes.length; i++) {
                if ($scope.woTypes[i].woTypeCode == woTypeCode) return $scope.woTypes[i].woTypeId;
            }
        }

        vm.downloadHCQTTemplate = function () {
            var obj = {};
            confirm('Bạn có muốn tải file mẫu?', function () {
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
                        saveFile(data, "WoHcqtCreateExample.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    }).error(function (data, status, headers, config) {
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                });
            })
        };

        vm.downloadTemplate = function () {
            var obj = {};
            confirm('Bạn có muốn tải file mẫu?', function () {
                obj.loggedInUser = $scope.sysUserId;
                $http({
                    url: Constant.BASE_SERVICE_URL + "woService/wo/getImportExcelTemplate",
                    method: "POST",
                    data: obj,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                })
                    .success(function (data, status, headers, config) {
                        saveFile(data, "WoCreateExample.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
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

        function valueOrEmptyStr(value) {
            return value ? value : '';
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
                        var keySearch = vm.searchForm.stationCode;

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
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.stationCode # - #: data.stationAddress #</div></div>',
            change: function (e) {
            },
            close: function (e) {
                // handle the event0
            }
        };

        vm.showHideColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.dataWOListTable.hideColumn(column);
            } else if (column.hidden) {
                vm.dataWOListTable.showColumn(column);
            } else {
                vm.dataWOListTable.hideColumn(column);
            }
        }

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };
        
        $scope.autoCompleteTrOptions = {
            dataTextField: "trCode", placeholder: "Thông tin TR",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                vm.searchForm.trId = data.trId;
                vm.searchForm.trCode = data.trCode;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {

                        var objSearch = {
                            keySearch: vm.searchForm.trCode,
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

        vm.openSelectTrModal = function () {
            $modal.open({
                templateUrl: 'coms/wo_xl/woManagement/prepareModal.html',
                controller: null,
                windowClass: 'app-modal-window',
                scope: $scope
            })
                .result.then(
                function () {
                    var woTypeName = "";
                    for (var i = 0; i < $scope.woTypes.length; i++) {
                        if ($scope.woTypes[i].woTypeId == $scope.templateData.woTypeId) woTypeName = $scope.woTypes[i].woTypeName;
                    }

                    var obj = {
                        trId: $scope.templateData.trId,
                        woTypeId: $scope.templateData.woTypeId,
                        woTypeName: woTypeName
                    };

                    vm.downloadTemplate(obj)
                },
                function () {
                    $scope.workingWO = {};
                }
            )

        };

        $scope.autoCompleteAvailableTrOptions = {
            dataTextField: "trCode", placeholder: "Thông tin TR",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                $scope.templateData.trId = data.trId;
                $scope.templateData.trCode = data.trCode;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {

                        var objSearch = {
                            keySearch: $scope.templateData.trCode,
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

        vm.exportWoExcel = function () {
            confirm("Xuất dữ liệu excel?", function () {
                $http({
                    url: Constant.BASE_SERVICE_URL + "woService/wo/exportWoExcel",
                    method: "POST",
                    data: vm.searchForm,
                    headers: {
                        'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function (data, status, headers, config) {
                    htmlCommonService.saveFile(data, "Danh_sach_wo.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                }).error(function (data, status, headers, config) {
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                });
            })
        }

        $scope.apWorkSrcShortName = function (name) {
            return htmlCommonService.apWorkSrcShortName(name);
        }

        $scope.autoCompleteFilterContractOptions = {
            dataTextField: "contractCode", placeholder: "Thông tin hợp đồng",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                vm.searchForm.contractCode = data.contractCode;
                vm.searchForm.contractId = data.contractId;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = vm.searchForm.contractCode;

                        if (keySearch == '') vm.searchForm.contractId = null;

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

                        if (keySearch == '') vm.searchForm.aioContractId = null;

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

        function validateWO(wo) {
            var woType = {};
            for (var i = 0; i < $scope.woTypes.length; i++) {
                if (wo.woTypeId == $scope.woTypes[i].woTypeId) woType = $scope.woTypes[i];
            }

            if (!wo.woTypeId) {
                toastr.error("Loại work order không được để trống.");
                return false;
            }

            if (!wo.apWorkSrc && woType.hasApWorkSrc == 1) {
                toastr.error("Nguồn việc không được để trống.");
                return false;
            }

            if (!wo.woNameId) {
                toastr.error("Tên work order không được để trống.");
                return false;
            }

            if (!wo.apConstructionType && woType.hasConstruction == 1) {
                toastr.error("Loại công trình không được để trống.");
                return false;
            }

            if (!wo.constructionId && woType.hasWorkItem == 1) {
                toastr.error("Mã công trình không được để trống.");
                return false;
            }

            if (!wo.catWorkItemTypeId && woType.hasWorkItem == 1) {
                toastr.error("Hạng mục không được để trống.");
                return false;
            }

            if (!wo.moneyValue || wo.moneyValue <= 0) {
                toastr.error("Giá trị sản lượng không được để trống.");
                return false;
            }

            if (!wo.cdLevel2) {
                toastr.error("CD level 2 không được để trống.");
                return false;
            }

            if (!wo.finishDate) {
                $('#inputFinishDate').focus();
                toastr.error("Hạn hoàn thành không được để trống.");
                return false;
            }

            if (!wo.qoutaTime) {
                $('#inputQuotaTime').focus();
                toastr.error("Định mức thành không được để trống.");
                return false;
            } else {
                if (wo.qoutaTime > 10000) {
                    toastr.error("Định mức hoàn thành không được quá 10000 giờ.");
                    return false;
                }
            }

            return true;
        }

        $scope.autoCompleteCdLevel2Options = {
            dataTextField: "code", placeholder: "Chọn cd level 2",
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
                        if (keySearch == '') {
                            vm.searchForm.cdLevel2Name = null;
                            vm.searchForm.cdLevel2 = null;
                            return options.success([]);
                        }

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

        $scope.autoCompleteConstructionOptions = {
            dataTextField: "code", placeholder: "Chọn công trình",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                vm.searchForm.constructionCode = data.constructionCode;
                $scope.$apply();
                vm.listItem = [];
				woManagementService.getListItem(vm.searchForm.constructionCode).then(
		                function (resp) {
		                	vm.listItem = resp.data;
		                })
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = vm.searchForm.constructionCode;
                        if (keySearch == '') {
                            vm.searchForm.constructionCode = null;
                            return options.success([]);
                        }
                        var obj = {keySearch: keySearch}

                        return Restangular.all("trService/tr/doSearchConstruction").post(obj).then(
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
                '<p class="col-md-12 text-header-auto">Chọn Mã công trình</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.constructionCode #</div></div>',
            change: function (e) {
            	/*debugger
            	if(vm.searchForm.constructionCode == null && searchForm.apWorkSrc == 6){
            		selectApworkSrc();
            	}*/
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

        vm.importHCQTWOFromFile = function () {
            $("#hcqtFile").val(null);
            $("#hcqtFile").unbind().click(); //id của input file
            $("#hcqtFile").change(function () {
                var selectedFile = $("#hcqtFile")[0].files[0];
                var prepareWoName = 'Hoàn công quyết toán';

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

                    var hcqtFtHeader = ['sysUserId', 'employeeCode', 'fullName', 'email'];
                    var hcqtFtList = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[1]], {
                        header: hcqtFtHeader,
                        range: 1
                    });

                    var hcqtProjectHeader = ['hcqtProjectId', 'hcqtProjectName'];
                    var hcqtProjectList = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[2]], {
                        header: hcqtProjectHeader,
                        range: 1
                    });

                    var importHeader = ["ftName", "stationCode", "catProvinceCode", "hcqtProjectName", "cnkv", "hcqtContractCode", "moneyValue", "hshcReceiveDate", "finishDate", "dnqtDate", "dnqtValue", "dnqtPerson",
                        "vtnetSendDate", "vtnetConfirmDate", "vtnetConfirmPerson", "aprovedDocDate", "vtnetConfirmValue", "aprovedPerson"];

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[0]], {
                        header: importHeader,
                        range: 3
                    });

                    var validDate = true;
                    for (var i = 0; i < XL_row_object.length; i++) {
                        var wo = XL_row_object[i];

                        Object.keys(wo).forEach(function (key) {
                            if (key == 'undefined') {
                                toastr.error('Cấu trúc file excel không hợp lệ.');
                                return;
                            }
                        });

                        if (wo.ftName) {
                            wo.ftId = getValueByKeys(hcqtFtList, 'fullName', wo.ftName, 'sysUserId');
                            wo.ftEmail = getValueByKeys(hcqtFtList, 'fullName', wo.ftName, 'email');
                        }

                        if (wo.hcqtProjectName) {
                            wo.hcqtProjectId = getValueByKeys(hcqtProjectList, 'hcqtProjectName', wo.hcqtProjectName, 'hcqtProjectId');
                        }

                        if (wo.dnqtPerson) {
                            wo.dnqtPersonId = getValueByKeys(hcqtFtList, 'fullName', wo.dnqtPerson, 'sysUserId');
                        }

                        if (wo.vtnetConfirmPerson) {
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

                        wo.createdUserFullName = $rootScope.casUser.fullName;
                        wo.createdUserEmail = $rootScope.casUser.email;

                        wo = trimProps(wo);

                        if (wo.finishDate && wo.finishDate != '') {
                            if (!dateRegex.test(wo.finishDate)) {
                                validDate = false;
                                wo.finishDateStr = wo.finishDate;
                                wo.finishDate = null;
                                wo.customField = "Ngày tháng không đúng định dạng dd/MM/yyyy;";
                            }
                        }

                        if (wo.hshcReceiveDate && wo.hshcReceiveDate != '') {
                            if (!dateRegex.test(wo.hshcReceiveDate)) {
                                validDate = false;
                                wo.hshcReceiveDateStr = wo.hshcReceiveDate;
                                wo.hshcReceiveDate = null;
                                wo.customField = "Ngày tháng không đúng định dạng dd/MM/yyyy;";
                            }
                        }

                        if (wo.dnqtDate && wo.dnqtDate != '') {
                            if (!dateRegex.test(wo.dnqtDate)) {
                                validDate = false;
                                wo.dnqtDateStr = wo.dnqtDate;
                                wo.dnqtDate = null;
                                wo.customField = "Ngày tháng không đúng định dạng dd/MM/yyyy;";
                            }
                        }

                        if (wo.vtnetSendDate && wo.vtnetSendDate != '') {
                            if (!dateRegex.test(wo.vtnetSendDate)) {
                                validDate = false;
                                wo.vtnetSendDateStr = wo.vtnetSendDate;
                                wo.vtnetSendDate = null;
                                wo.customField = "Ngày tháng không đúng định dạng dd/MM/yyyy;";
                            }
                        }

                        if (wo.vtnetConfirmDate && wo.vtnetConfirmDate != '') {
                            if (!dateRegex.test(wo.vtnetConfirmDate)) {
                                validDate = false;
                                wo.vtnetConfirmDateStr = wo.vtnetConfirmDate;
                                wo.vtnetConfirmDate = null;
                                wo.customField = "Ngày tháng không đúng định dạng dd/MM/yyyy;";
                            }
                        }
                        if (wo.aprovedDocDate && wo.aprovedDocDate != '') {
                            if (!dateRegex.test(wo.aprovedDocDate)) {
                                validDate = false;
                                wo.aprovedDocDateStr = wo.aprovedDocDate;
                                wo.aprovedDocDate = null;
                                wo.customField = "Ngày tháng không đúng định dạng dd/MM/yyyy;";
                            }
                        }

                    }

                    //console.log(XL_row_object);
                    if (XL_row_object.length == 0) {
                        toastr.error('File excel không hợp lệ.');
                        return;
                    }

                    if (!validDate) {
                        toastr.error('Ngày tháng không hợp lệ, không thể gửi!');
                        confirm('Phát hiện dữ liệu không hợp lệ! Bạn có muốn xem chi tiết?', function () {
                            getHCQTImportExcelResult(XL_row_object);
                        });
                        return;
                    }

                    var json_object = JSON.stringify(XL_row_object);
                    addDataHCQTFromExcel(json_object);
                };
                reader.onerror = function (event) {
                    console.error("File could not be read! Code " + event.target.error.code);
                };
                reader.readAsBinaryString(selectedFile);
            });
        };

        vm.deliveryWo = deliveryWo;

        function deliveryWo() {
            vm.fileImportData = false;
            vm.searchForm.type = 1;
            var teamplateUrl = "coms/wo_xl/woManagement/DeliveryWoModal.html";
            var title = "Giao FT";
            var windowId = "IMPORT_WO";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '275', null);
        }

        vm.ApproveWo = ApproveWo;

        function ApproveWo() {
            vm.fileImportData = false;
            vm.searchForm.type = 1;
            var teamplateUrl = "coms/wo_xl/woManagement/AproveWo.html";
            var title = "Ghi nhận sản lượng";
            var windowId = "APPROVE_WO";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '275', null);
        }

        vm.onSelect = function (e) {
            if ($("#files")[0].files[0].name.split('.').pop() != 'xls' && $("#files")[0].files[0].name.split('.').pop() != 'xlsx') {
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
                $("#fileName").innerHTML = $("#files")[0].files[0].name
            }
        };

        vm.exportexcelDeliveryWo = function () {
            function displayLoading(target) {
                var element = $(target);
                kendo.ui.progress(element, true);
                vm.searchForm.userCreated = $scope.loggedInUser;
                vm.searchForm.cdLevel1 = $scope.cdLevel1Permission;
                vm.searchForm.cdLevel1Name = $scope.cdLevel1PermissionName;
                vm.searchForm.loggedInUser = $scope.loggedInUser;
                vm.searchForm.sysUserId = $scope.sysUserId;
                vm.searchForm.sysGroupId = $scope.sysUserGroup.sysGroupId;
                vm.searchForm.cdDomainDataList = $scope.permissions.cdDomainDataList;
                setTimeout(function () {
                    return Restangular.all("woService/exportFileDelivery").post(vm.searchForm).then(function (d) {
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
                url: Constant.BASE_SERVICE_URL + "woService/importFileDelivery",
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

        vm.cancel = function () {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

        vm.addDataHCQTFromExcel = addDataHCQTFromExcel;

        function addDataHCQTFromExcel(obj) {
            woManagementService.createManyHCQTWo(obj).then(function (res) {
                if (res && res.statusCode == 1) {
                    vm.dataWOListTable.dataSource.read();
                    toastr.success("Import dữ liệu thành công. " + res.message);
                    confirm('Import dữ liệu thành công! Bạn có muốn xem chi tiết?', function () {
                        getHCQTImportExcelResult(res.data)
                    });
                } else {
                    toastr.error("Phát hiện dữ liệu không hợp lệ!");
                    confirm('Phát hiện dữ liệu không hợp lệ! Bạn có muốn xem chi tiết?', function () {
                        getHCQTImportExcelResult(res.data)
                    });
                }
            }, function (error) {
                toastr.error("Không thành công! ");
            })
        }

        function getHCQTImportExcelResult(obj) {
            $http({
                url: Constant.BASE_SERVICE_URL + "woService/wo/createManyHCQTWOReport",
                method: "POST",
                data: obj,
                headers: {
                    'Content-type': 'application/json'
                },
                responseType: 'arraybuffer'
            }).success(function (data, status, headers, config) {
                htmlCommonService.saveFile(data, "Ket_qua_import_hcqt.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            }).error(function (data, status, headers, config) {
                toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
            });
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

        vm.cancelImport = cancelImport;

        function cancelImport() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

        function getImportFTResult(obj) {
            $http({
                url: Constant.BASE_SERVICE_URL + "woService/wo/getImportFTResult",
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

        vm.approveSubmit = approveSubmit;

        function approveSubmit(data) {
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

            var listGroupdata = $scope.permissions.cdDomainDataList;
            if ($scope.permissions.approveTcBranch == true) {
                listGroupdata = $scope.permissions.approveTcBranchDataList;
            }
            formData.append('multipartFile', $('#fileImport')[0].files[0]);
            $('#loadding').show();
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "woService/importApproveWo?" + "cdDomainDataList=" + listGroupdata,
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
                                getImportApprovreResult(data.data)
                            });
                        }
                        cancelImport();
                    }
                    cancelImport();
                    $scope.$apply();
                }
            });
        }

        vm.exportApproveWo = function () {
            function displayLoading(target) {
                var element = $(target);
                kendo.ui.progress(element, true);
                vm.searchForm.userCreated = $scope.loggedInUser;
                vm.searchForm.loggedInUser = $scope.loggedInUser;
                vm.searchForm.sysUserId = $scope.sysUserId;
                vm.searchForm.sysGroupId = $scope.sysUserGroup.sysGroupId;
                vm.searchForm.cdDomainDataList = $scope.permissions.cdDomainDataList;
                if ($scope.permissions.approveTcBranch == true) {
                    vm.searchForm.cdDomainDataList = $scope.permissions.approveTcBranchDataList;
                }
                setTimeout(function () {
                    return Restangular.all("woService/exportApproveWo").post(vm.searchForm).then(function (d) {
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

        function getImportApprovreResult(obj) {
            $http({
                url: Constant.BASE_SERVICE_URL + "woService/getImportApprovreResult",
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

        //Huypq-24112020-start
        vm.importDoneWo = importDoneWo;

        function importDoneWo() {
            vm.fileImportData = false;
            vm.searchForm.type = 1;
            var teamplateUrl = "coms/wo_xl/woManagement/importDoneWo.html";
            var title = "Đóng WO thi công (trừ ct tuyến & GPON), HSHC nguồn chi phí";
            var windowId = "DONE_WO_TC";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '275', null);
        }

        vm.exportDoneWo = function () {
            kendo.ui.progress($("#doneWo"), true);
            vm.searchForm.userCreated = $scope.loggedInUser;
            vm.searchForm.loggedInUser = $scope.loggedInUser;
            vm.searchForm.sysUserId = $scope.sysUserId;
            vm.searchForm.sysGroupId = $scope.sysUserGroup.sysGroupId;
            vm.searchForm.cdDomainDataList = $scope.permissions.cdDomainDataList;
            return Restangular.all("woService/exportDoneWo").post(vm.searchForm).then(function (d) {
                kendo.ui.progress($("#doneWo"), false);
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function (e) {
                kendo.ui.progress($("#doneWo"), false);
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                return;
            });
        }


        vm.doneWoSubmit = doneWoSubmit;

        function doneWoSubmit(item) {
//        	kendo.ui.progress($("#doneWo"), true);
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
                url: Constant.BASE_SERVICE_URL + "woService/importDoneWo?" + "cdDomainDataList=" + $scope.permissions.cdDomainDataList,
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if (data && data.statusCode == 1) {
//                    	kendo.ui.progress($("#doneWo"), false);
                        toastr.success("Import thành công! ");
                        doSearch();
                        cancelImport();
                    } else {
//                    	kendo.ui.progress($("#doneWo"), false);
                        toastr.error("Import thất bại! " + data.message ? data.message : '');
                        if (data.data != null) {
                            confirm('Phát hiện dữ liệu không hợp lệ! Bạn có muốn xem chi tiết?', function () {
                                getImportDoneResult(data.data)
                            });
                        }
                        cancelImport();
                    }
                    cancelImport();
                    $scope.$apply();
                }
            });
        }

        function getImportDoneResult(obj) {
            var lstObj = [];
            lstObj = angular.copy(obj);
            return Restangular.all("woService/getImportDoneResult").post(lstObj).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function (e) {
                toastr.error(gettextCatalog.getString("Lỗi khi tải file lỗi!"));
                return;
            });
        }

        //Huy-end

        //HienLT56 start 01122020
        vm.groupCreateWOList = [
            {
                code: "TTGPTH",
                name: "TTGPTH"
            }, {
                code: "TT ĐTHT",
                name: "TT ĐTHT"
            }, {
                code: "TTHT",
                name: "TTHT"
            }, {
                code: "TTVHKT",
                name: "TTVHKT"
            }, {
                code: "TT XDDD",
                name: "TT XDDD"
            }, {
                code: "TT CNTT",
                name: "TT CNTT"
            }, {
                code: "CNKT",
                name: "CNKT"
            },
        ]
        //HienLT56 end 01122020

        vm.massApproveRejectTcWO = function (type) {
            if ($scope.selectedWoList.length == 0) {
                toastr.warning('Chưa chọn WO để phê duyệt!');
                return;
            }
            $scope.massBtnDisable = true;
            $scope.massModalType = type;
            $modal.open({
                templateUrl: 'coms/wo_xl/common/tcApproveRejectModal.html',
                controller: 'tcApproveRejectModalController',
                windowClass: 'app-modal-window',
                scope: $scope
            }).result.then(
                function () {
                    if (type == 'accept') {
                        tcAcceptAllSelected();
                    } else {
                        tcRejectAllSelected();
                    }
                },
                function () {
                    $scope.massBtnDisable = false;
                }
            )
        }

        function tcAcceptAllSelected() {
            var newState = 'WAIT_TC_TCT';
            if ($scope.permissions.approveTcTct) newState = 'OK';

            var obj = {
                newState: newState,
                loggedInUser: $scope.loggedInUser,
                listWo: $scope.selectedWoList
            }

            woManagementService.tcAcceptAllSelected(obj).then(
                function (resp) {
                    toastr.success("Thực hiện thành công");
                    $scope.massBtnDisable = false;
                    $scope.selectedWoList = [];
                    vm.dataWOListTable.dataSource.read();
                },
                function (err) {
                    console.log(err);
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    $scope.massBtnDisable = false;
                }
            )
        }

        function tcRejectAllSelected() {
            var newState = 'TC_TCT_REJECTED';
            if ($scope.permissions.approveTcBranch) { // Tai chinh tru tu choi
                newState = 'TC_BRANCH_REJECTED';
            }

            var obj = {
                newState: newState,
                loggedInUser: $scope.loggedInUser,
                listWo: $scope.selectedWoList
            }

            woManagementService.tcRejectAllSelected(obj).then(
                function (resp) {
                    toastr.success("Thực hiện thành công");
                    $scope.massBtnDisable = false;
                    $scope.selectedWoList = [];
                    vm.dataWOListTable.dataSource.read();
                },
                function (err) {
                    console.log(err);
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    $scope.massBtnDisable = false;
                }
            )
        }

        function getTcTctEmails() {
            woManagementService.getTcTctEmails().then(
                function (resp) {
                    if (resp) {
                        $scope.tcTctEmails = resp.data;
                    }
                },
                function (error) {
                    toastr.error("Không thể kết nối để lấy dữ liệu email TC TCT !");
                }
            )
        }
//        taotq start 27082021
        vm.selectApworkSrc = selectApworkSrc;
        function selectApworkSrc() {
        	if(vm.searchForm.apWorkSrc == 6 && (vm.searchForm.constructionCode === undefined || vm.searchForm.constructionCode === null)){
        		woManagementService.getListItemByWorkSrc().then(
		                function (resp) {
		                	vm.listItem = resp.data;
		                })
        	}else{
        		vm.searchForm.item = null;
        	}
        }
//      taotq end 27082021
// end controller
    }
})();
