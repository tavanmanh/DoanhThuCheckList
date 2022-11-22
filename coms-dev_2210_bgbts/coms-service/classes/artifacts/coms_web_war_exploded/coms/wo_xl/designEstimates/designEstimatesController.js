(function () {
    'use strict';
    var controllerId = 'designEstimatesController';

    angular.module('MetronicApp').controller(controllerId, designEstimatesController, '$scope', '$modal', '$rootScope');

    function designEstimatesController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                    kendoConfig, $kWindow, htmlCommonService, designEstimatesService, vpsPermissionService,
                                    CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modal) {

        var vm = this;
        var record = 0;
        vm.breadcrumb = "Thẩm thiết kế dự toán > Danh sách";
        vm.sectionHeader = "Chi tiết";
        vm.searchForm = {};
        $scope.loggedInUser = vm.searchForm.loggedInUser = $rootScope.casUser.userName;
        vm.addForm = {};
        vm.isCreateNew = false;
        vm.fileLst = [];
        vm.file = {};
        vm.areaLocation = [
        	{ id: 0, name: '' },
			{ id: 1, name: 'KV1' },
			{ id: 2, name: 'KV2' },
			{ id: 3, name: 'KV3' }
		];
        vm.stationTypeList = [
        	{ id: 0, name: '' },
			{ id: 1, name: 'Macro' },
			{ id: 2, name: 'RRU' },
			{ id: 3, name: 'SMC' }
		];
        vm.locationList = [
			{ id: 0, name: '' },
			{ id: 1, name: 'Dưới đất' },
			{ id: 2, name: 'Trên mái' }
		];
        vm.levelRockEarthList = [
        	{ id: 0, name: '' },
			{ id: 1, name: 'Đất cấp 1' },
			{ id: 2, name: 'Đất cấp 2' },
			{ id: 3, name: 'Đất cấp 3' },
			{ id: 4, name: 'Đất cấp 4' },
			{ id: 5, name: 'Đất cấp 5' }
		];
        vm.pillarTypeList= [
        	{ id: 0, name: '' },
        	{ id: 1, name: 'Cột cóc' },
			{ id: 2, name: 'Cột ngụy trang' },
			{ id: 3, name: 'Cột dây co' },
			{ id: 4, name: 'Cột tự đứng' },
			{ id: 5, name: 'Cột tự đứng (đốt 600x600)' },
			{ id: 6, name: 'Cột monopole' },
			{ id: 7, name: 'Cột tự đứng thanh giằng' },
			{ id: 8, name: 'Cột thân thiện (cây dừa/cây thông)' },
			{ id: 9, name: 'Cột thân thiện (lồng đèn/cánh sen)' },
			{ id: 10, name: 'Cột Bê tông ly tâm (BTLT)' },
			{ id: 11, name: 'Cột có sẵn/Không có cột' }
        ];
        
        vm.terainList = [
        	{ id: 0, name: '' },
			{ id: 1, name: 'Đồng bằng' },
			{ id: 2, name: 'Miền núi' },
			{ id: 3, name: 'Hải đảo' }
		];
        
        $scope.permissions = {};
        var TTHT_SYS_GROUP_ID = '242656';
        var GPTH_SYS_GRPUP_ID = '280483';
        var XDDTHT_SYS_GROUP_ID = '166677';
        var VHKT_SYS_GROUP_ID = '270120';
        var TTXDD_SYS_GROUP_ID = '9006003';

        init();

        function init() {
            $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
            fillDataTable();
            initDate();
        }
        
        vm.provinceOptionsSearch = {
                dataTextField: "name",
                dataValueField: "id",
                placeholder: "Nhập mã hoặc tên tỉnh",
                select: function (e) {
                    vm.isSelect = true;
                    var dataItem = this.dataItem(e.item.index());
                    vm.searchForm.provinceId = dataItem.catProvinceId;
                    vm.searchForm.provinceName = dataItem.name;
                },
                pageSize: 10,
                open: function (e) {
                    vm.isSelect = false;
                },
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function (options) {
                            vm.isSelect = false;
                            return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
                                name: vm.searchForm.provinceName,
                                pageSize: vm.provinceOptions.pageSize,
                                page: 1
                            }).then(function (response) {
                                options.success(response.data);
                            }).catch(function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            });
                        }
                    }
                },
                headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                    '<p class="col-md-6 text-header-auto border-right-ccc">Mã tỉnh</p>' +
                    '<p class="col-md-6 text-header-auto">Tên tỉnh</p>' +
                    '</div>',
                template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
                change: function (e) {
                    if (!vm.isSelect) {
                        vm.searchForm.provinceId = null;
                        vm.searchForm.provinceName = null;
                    }
                },
                close: function (e) {
                    if (!vm.isSelect) {
                    	vm.searchForm.provinceId = null;
                        vm.searchForm.provinceName = null;
                    }
                }
         }
        
        vm.provinceOptions = {
                dataTextField: "name",
                dataValueField: "id",
                placeholder: "Nhập mã hoặc tên tỉnh",
                select: function (e) {
                    vm.isSelect = true;
                    var dataItem = this.dataItem(e.item.index());
                    vm.addForm.provinceId = dataItem.catProvinceId;
                    vm.addForm.provinceCode = dataItem.code;
                    vm.addForm.provinceName = dataItem.name;
                },
                pageSize: 10,
                open: function (e) {
                    vm.isSelect = false;
                },
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function (options) {
                            vm.isSelect = false;
                            return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
                                name: vm.addForm.provinceName,
                                pageSize: vm.provinceOptions.pageSize,
                                page: 1
                            }).then(function (response) {
                                options.success(response.data);
                            }).catch(function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            });
                        }
                    }
                },
                headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                    '<p class="col-md-6 text-header-auto border-right-ccc">Mã tỉnh</p>' +
                    '<p class="col-md-6 text-header-auto">Tên tỉnh</p>' +
                    '</div>',
                template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
                change: function (e) {
                    if (!vm.isSelect) {
                        vm.addForm.provinceId = null;
                        vm.addForm.provinceCode = null;
                        vm.addForm.provinceName = null;
                        vm.addForm.districtName = null;
                        vm.addForm.districtId = null;
                    }
                },
                close: function (e) {
                    if (!vm.isSelect) {
                    	vm.addForm.provinceId = null;
                        vm.addForm.provinceCode = null;
                        vm.addForm.provinceName = null;
                        vm.addForm.districtName = null;
                        vm.addForm.districtId = null;
                    }
                }
         }
        
        vm.districtOptions = {
                dataTextField: "districtName",
                dataValueField: "districtId",
                placeholder: "Nhập tên huyện",
                select: function (e) {
                    vm.isSelect = true;
                    var dataItem = this.dataItem(e.item.index());
                    vm.addForm.districtName = dataItem.districtName;
                    vm.addForm.districtId = dataItem.districtId;
                },
                pageSize: 10,
                open: function (e) {
                    vm.isSelect = false;
                },
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function (options) {
                            vm.isSelect = false;
                            return Restangular.all("designEstimatesService/doSearchArea").post({
                            	districtName: vm.addForm.districtName,
                                provinceId: vm.addForm.provinceId
                            }).then(function (response) {
                                options.success(response.data);
                            }).catch(function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            });
                        }
                    }
                },
                headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                    '<p class="text-header-auto border-right-ccc">Tên huyện</p>' +
                    '</div>',
                template: '<div class="row" ><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.districtName #</div> </div>',
                change: function (e) {
                    if (!vm.isSelect) {
                    	vm.addForm.districtName = null;
                        vm.addForm.districtId = null;
                    }
                },
                close: function (e) {
                    if (!vm.isSelect) {
                    	vm.addForm.districtName = null;
                        vm.addForm.districtId = null;
                    }
                }
         }
        
        vm.stationVCCOptions = {
                dataTextField: "code",
                dataValueField: "code",
                placeholder: "Nhập mã hoặc tên tỉnh",
                pageSize: 10,
                select: function (e) {
                    vm.isSelect = true;
                    var dataItem = this.dataItem(e.item.index());
                    vm.addForm.stationVCC = dataItem.code;
                    Restangular.all("designEstimatesService/getFile").post(vm.addForm.stationVCC).then(function (response) {
                    	if(response != undefined){
                    		vm.file.fileName = response.name;
                    		vm.file.filePath = response.filePath;
                    		vm.addForm.provinceName = response.provinceName;
                    		vm.addForm.districtName = response.districtName;
                    		vm.addForm.provinceId = response.provinceId;
                    		vm.addForm.area = response.area;
                    		vm.addForm.stationVTNET = response.stationVTNET;
                    		vm.addForm.stationType = response.stationType;
                    		vm.addForm.location = response.location;
                    		vm.addForm.pillarType = response.pillarType;
                    		vm.addForm.pillarHight = response.pillarHight;
                    		vm.addForm.terrain = response.terrain;
                    		vm.addForm.fundamental = response.fundamental;
                    		vm.addForm.stationAddress = response.stationAddress;
                    	}
                    })
                },
                open: function (e) {
                    vm.isSelect = false;
                },
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function (options) {
                            vm.isSelect = false;
                            return Restangular.all("designEstimatesService/doSearchStationVCC").post({
                                code: vm.addForm.stationVCC,
                                pageSize: vm.provinceOptions.pageSize,
                                page: 1
                            }).then(function (response) {
                                options.success(response.data);
                            }).catch(function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            });
                        }
                    }
                },
                headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                    '<p class="text-header-auto border-right-ccc">Mã trạm</p>' +
                    '</div>',
                template: '<div class="row" ><div  style="float:left">#: data.code #</div></div>',
                change: function (e) {
                    if (!vm.isSelect) {
                        vm.addForm.stationVCC = null;
                        vm.file.fileName = null;
                		vm.file.filePath = null;
                		vm.addForm.provinceName = null;
                		vm.addForm.districtName = null;
                		vm.addForm.provinceId = null;
                		vm.addForm.area = null;
                		vm.addForm.stationVTNET = null;
                		vm.addForm.stationType = null;
                    }
                },
                close: function (e) {
                    if (!vm.isSelect) {
                    	vm.addForm.stationVCC = null;
                        vm.file.fileName = null;
                		vm.file.filePath = null;
                		vm.addForm.provinceName = null;
                		vm.addForm.districtName = null;
                		vm.addForm.provinceId = null;
                		vm.addForm.area = null;
                		vm.addForm.stationVTNET = null;
                		vm.addForm.stationType = null;
                    }
                }
         }
        
        vm.stationVTNETOptions = {
                dataTextField: "code",
                dataValueField: "code",
                placeholder: "Nhập mã hoặc tên tỉnh",
                pageSize: 10,
                select: function (e) {
                    vm.isSelect = true;
                    var dataItem = this.dataItem(e.item.index());
                    vm.addForm.stationVTNET = dataItem.code;
                },
                open: function (e) {
                    vm.isSelect = false;
                },
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function (options) {
                            vm.isSelect = false;
                            return Restangular.all("designEstimatesService/doSearchStationVTNET").post({
                                code: vm.addForm.stationVTNET,
                                pageSize: vm.provinceOptions.pageSize,
                                page: 1
                            }).then(function (response) {
                                options.success(response.data);
                            }).catch(function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            });
                        }
                    }
                },
                headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                    '<p class="text-header-auto border-right-ccc">Mã trạm</p>' +
                    '</div>',
                template: '<div class="row" ><div style="float:left">#: data.code #</div></div>',
                change: function (e) {
                    if (!vm.isSelect) {
                        vm.addForm.stationVTNET = null;
                    }
                },
                close: function (e) {
                    if (!vm.isSelect) {
                    	vm.addForm.stationVTNET = null;
                    }
                }
         }
        
        vm.userOptions1 = {
                dataTextField: "fullName",
                dataValueField: "userId",
                placeholder: "Nhập mã hoặc tên",
                select: function (e) {
                    vm.isSelect = true;
                    var dataItem = this.dataItem(e.item.index());
                    	vm.addForm.designUserId = dataItem.userId;
                        vm.addForm.designUserName = dataItem.fullName;
                },
                pageSize: 10,
                open: function (e) {
                    vm.isSelect = false;
                },
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function (options) {
                            vm.isSelect = false;
                            return Restangular.all("designEstimatesService/doSearchUser").post({
                                name: vm.addForm.designUserName,
                            }).then(function (response) {
                                options.success(response.data);
                            }).catch(function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            });
                        }
                    }
                },
                headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                    '<p class="col-md-6 text-header-auto border-right-ccc">Mã NV</p>' +
                    '<p class="col-md-6 text-header-auto">Tên NV</p>' +
                    '</div>',
                template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.employeeCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
                change: function (e) {
                    if (!vm.isSelect) {
                        	vm.addForm.designUserId = null;
                            vm.addForm.designUserName = null;
                    }
                },
                close: function (e) {
                    if (!vm.isSelect) {
                        	vm.addForm.designUserId = null;
                            vm.addForm.designUserName = null;
                    }
                }
         }
        vm.userOptions2 = {
                dataTextField: "fullName",
                dataValueField: "userId",
                placeholder: "Nhập mã hoặc tên",
                select: function (e) {
                    vm.isSelect = true;
                    var dataItem = this.dataItem(e.item.index());
                    	vm.addForm.designUpdateUser = dataItem.userId;
                        vm.addForm.designUpdateUserName = dataItem.fullName;
                },
                pageSize: 10,
                open: function (e) {
                    vm.isSelect = false;
                },
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function (options) {
                            vm.isSelect = false;
                            return Restangular.all("designEstimatesService/doSearchUser").post({
                                name: vm.addForm.designUpdateUserName,
                            }).then(function (response) {
                                options.success(response.data);
                            }).catch(function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            });
                        }
                    }
                },
                headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                    '<p class="col-md-6 text-header-auto border-right-ccc">Mã NV</p>' +
                    '<p class="col-md-6 text-header-auto">Tên NV</p>' +
                    '</div>',
                template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.employeeCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
                change: function (e) {
                    if (!vm.isSelect) {
	                    	vm.addForm.designUpdateUser = null;
	                        vm.addForm.designUpdateUserName = null;
                    }
                },
                close: function (e) {
                    if (!vm.isSelect) {
	                    	vm.addForm.designUpdateUser = null;
	                        vm.addForm.designUpdateUserName = null;
                    }
                }
         }
        vm.userOptions3 = {
                dataTextField: "fullName",
                dataValueField: "userId",
                placeholder: "Nhập mã hoặc tên",
                select: function (e) {
                    vm.isSelect = true;
                    var dataItem = this.dataItem(e.item.index());
                    	vm.addForm.creatDesignEstimatesUser = dataItem.userId;
                        vm.addForm.creatDesignEstimatesUserName = dataItem.fullName;
                },
                pageSize: 10,
                open: function (e) {
                    vm.isSelect = false;
                },
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function (options) {
                            vm.isSelect = false;
                            return Restangular.all("designEstimatesService/doSearchUser").post({
                                name: vm.addForm.creatDesignEstimatesUserName,
                            }).then(function (response) {
                                options.success(response.data);
                            }).catch(function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            });
                        }
                    }
                },
                headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                    '<p class="col-md-6 text-header-auto border-right-ccc">Mã NV</p>' +
                    '<p class="col-md-6 text-header-auto">Tên NV</p>' +
                    '</div>',
                template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.employeeCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
                change: function (e) {
                    if (!vm.isSelect) {
	                    	vm.addForm.creatDesignEstimatesUser = null;
	                        vm.addForm.creatDesignEstimatesUserName = null;
                    }
                },
                close: function (e) {
                    if (!vm.isSelect) {
	                    	vm.addForm.creatDesignEstimatesUser = null;
	                        vm.addForm.creatDesignEstimatesUserName = null;
                    }
                }
         }
        
        
        vm.clearInput = function(param){
			if(param == 'provinceCode'){
				vm.addForm.provinceCode = null;
				vm.addForm.provinceId = null;
				vm.addForm.provinceName = null;
				vm.addForm.districtName = null;
                vm.addForm.districtId = null;
			}
		}

        function initDate() {
            var now = new Date();
            var startTime = now.getTime() - 90 * 24 * 60 * 60 * 1000;
            var startDate = new Date(startTime);
            vm.searchForm.createdDateFrom = htmlCommonService.formatDate(startDate);
//            vm.searchForm.createdDateTo = htmlCommonService.formatDate(now);

        }

        function checkIsAutoCreateWo() {
            if ($scope.permissions.createTR && $scope.permissions.createTRDomainDataList.includes(XDDTHT_SYS_GROUP_ID)) {
                // Bỏ do xây dựng luồng DBHT mới -> mặc định sinh 7 WO
                // fillWorkItemDataTbl();
                vm.isAutoCreateWo = true;
                vm.trBranch = 3; // 3 là mã trụ trong hợp đồng của xd đt ht
            }
        }

        vm.doSearch = doSearch;
        function doSearch() {
            var grid = vm.designEstimatesTable;

            Object.keys(vm.searchForm).forEach(function (key, index) {
                if (vm.searchForm[key] == '') vm.searchForm[key] = null;
            })
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                })
            }
        }

        vm.cancelInput = function (data) {
            if (data == 'dept') {
                vm.searchForm.sysGroupName = null;
                vm.searchForm.sysGroupId = null;
            }
        }

        vm.gridColumnShowHideFilter = function (item) {
            return item.type == null || item.type !== 1;
        };

        vm.add = function () {
        	vm.isCreateNew = true;
        	vm.addForm = {};
        	vm.fileLst = [];
        	var teamplateUrl = "coms/wo_xl/designEstimates/designEstimatesPopup.html";
			var title = "Thêm mới thẩm định";
			var windowId = "APP_PARAM";
			htmlCommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '80%', '80%', "code");

        };


        function trimProps(obj) {
            Object.keys(obj).forEach(function (key) {
                if (obj[key] && typeof obj[key].trim === "function") obj[key] = obj[key].trim();
            })
            return obj;
        }

        function formatDate(date) {
            var d = new Date(date),
                month = '' + (d.getMonth() + 1),
                day = '' + d.getDate(),
                year = d.getFullYear();

            if (month.length < 2)
                month = '0' + month;
            if (day.length < 2)
                day = '0' + day;

            return [year, month, day].join('-');
        }

        function fillDataTable() {

            vm.designEstimatesTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save: function () {
                vm.designEstimatesTable.dataSource.read();
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
                            console.log(response);
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            url: Constant.BASE_SERVICE_URL + "designEstimatesService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page;
                            vm.searchForm.pageSize = options.pageSize;
                            return JSON.stringify(vm.searchForm);
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
                        empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '8%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Khu vực",
                        field: 'area',
                        width: '12%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Tỉnh",
                        field: 'provinceName',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Mã trạm VTNET",
                        field: 'stationVTNET',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Mã trạm VCC",
                        field: 'stationVCC',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Địa chỉ",
                        field: 'stationAddress',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Huyện",
                        field: 'districtName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Người tạo",
                        field: 'createdUserName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Ngày tạo",
                        field: 'createdDate',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Thao tác",
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                        	var html =  '<div class="text-center">' +
                                '<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.edit(dataItem)" class=" icon_table " ' +
                                '   uib-tooltip="Sửa" translate>' +
                                '<i class="fa fa-pencil" aria-hidden="true"></i>' +
                                '</button>' +
                                '<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.remove(dataItem)" class=" icon_table " ' +
                                '   uib-tooltip="Xoá" translate>' +
                                '<i class="fa fa-trash" style="color: #337ab7;" aria-hidden="true"></i>' +
                                '</button>' +
                                '</div>';
                        	return html;
                        }
                    }
                ]
            });
        }

        vm.showHideColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.dataTRListTable.hideColumn(column);
            } else if (column.hidden) {
                vm.dataTRListTable.showColumn(column);
            } else {
                vm.dataTRListTable.hideColumn(column);
            }
        }

        vm.exportExcel = function exportFile() {
			kendo.ui.progress($("#designEstimatesTable"), true);
			vm.searchForm.page = null;
			vm.searchForm.pageSize = null;
//			return Restangular.all("tangentCustomerGPTHRestService/exportFileDesign").post(vm.searchForm).then(function (d) {
			return Restangular.all("designEstimatesService/exportExcel").post(vm.searchForm).then(function (d) {
//				var data = d.plain();
				window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + d;
				kendo.ui.progress($("#designEstimatesTable"), false);
			}).catch(function (e) {
				kendo.ui.progress($("#designEstimatesTable"), false);
				toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
				return;
			});
		}
        
        vm.save = save;
        function save() {
           if (vm.addForm.area == null || vm.addForm.area == "") {
                toastr.error("Khu vực không được để trống.");
                $('#area').focus();
                return false;
            }
           if (vm.addForm.provinceId == null) {
               toastr.error("Tỉnh không được để trống.");
               $('#provinceCode').focus();
               return false;
           }
           if (vm.addForm.stationVTNET == null) {
               toastr.error("Mã trạm VTNET không được để trống.");
               $('#stationVTNET').focus();
               return false;
           }
           if (vm.addForm.stationVCC == null) {
               toastr.error("Mã trạm không được để trống.");
               $('#stationVCC').focus();
               return false;
           }
           if (vm.addForm.districtName == null) {
               toastr.error("Huyện không được để trống.");
               $('#districtId').focus();
               return false;
           }
        	
           if(vm.addForm.designEstimatesId == null){
        	   if(vm.file.fileName != null && vm.file.fileName != ""){
        		   var file = {};
                   file.name = vm.file.fileName;
                   file.createdDate = htmlCommonService.getFullDate();
                   file.createdUserName = Constant.userInfo.vpsUserToken.fullName;
                   file.createdUserId = Constant.userInfo.vpsUserToken.sysUserId;
                   file.filePath = vm.file.filePath;
                   file.type = 0;
                   vm.addForm.fileLst = [];
                   vm.addForm.fileLst.push(file);
        	   }
        		designEstimatesService.save(vm.addForm).then(function (response){
        			 toastr.success("Thêm mới thành công");
        			 vm.addForm = {};
          			$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
          			doSearch();
            	});
        	}else{
        		vm.addForm.fileLst = vm.fileLst;
        		designEstimatesService.update(vm.addForm).then(function (response){
        			 toastr.success("Update thành công");
        			 vm.addForm = {};
         			$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
         			doSearch();
            	});
        	}
        };
        
        vm.edit = edit;
        function edit(dataItem) {
        	vm.isCreateNew = false;
        	vm.fileLst = dataItem.fileLst;
        	//Day du lieu file vao bang
        	fillDataFileTable();
        	vm.addForm = dataItem;
        	var teamplateUrl = "coms/wo_xl/designEstimates/designEstimatesPopup.html";
			var title = "Update thẩm định";
			var windowId = "APP_PARAM";
			htmlCommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '80%', '80%', "code");
		}
        
        vm.remove = remove;
        function remove(dataItem) {
            confirm('Xác nhận xóa bản ghi đã chọn?',
                function () {
                    designEstimatesService.remove(dataItem).then(
                        function (resp) {
                            console.log(resp);

                            if (resp && resp.statusCode == 1) toastr.success("Xóa thành công!");
                            else toastr.success("Đã xóa hoặc không tồn tại!");

                            console.log(vm)
                            vm.designEstimatesTable.dataSource.read();
                        },
                        function (error) {
                            console.log(error);
                            toastr.success("Có lỗi xảy ra!");
                        }
                    )
                }
            );
        }
        
        function fillDataFileTable() {
        
	        vm.gridFileOptions = kendoConfig.getGridOptions({
				autoBind: true,
				resizable: true,
				dataSource: vm.fileLst,
				noRecords: true,
				columnMenu: false,
				scrollable: false,
				editable: true,
				messages: {
					noRecords: gettextCatalog.getString("Không có kết quả hiển thị")
				}, dataBound: function () {
					var GridDestination = $("#designEstimatesFileGrid").data("kendoGrid");
					GridDestination.pager.element.hide();
				},
				columns: [{
					title: "TT",
					field: "stt",
					template: dataItem => $("#designEstimatesFileGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
					width
		:
			20,
				headerAttributes
		:
			{
				style: "text-align:center;"
			}
		,
			attributes: {
				style: "text-align:center;"
			}
		,
		}
		,
			{
				title: "Tên file",
					field
			:
				'name',
					width
			:
				150,
					headerAttributes
			:
				{
					style: "text-align:center;"
				}
			,
				attributes: {
					style: "text-align:left;"
				}
			,
				template :  function (dataItem) {
					return "<a href='' ng-click='caller.downloadFile(dataItem)'>" + dataItem.name + "</a>";
				}
			}
		,
			{
				title: "Ngày upload",
					field
			:
				'createdDate',
					width
			:
				150,
					headerAttributes
			:
				{
					style: "text-align:center;"
				}
			,
				attributes: {
					"id"
				:
					"appFile",
						style
				:
					"text-align:left;"
				}
			,
			}
		,
			{
				title: "Người upload",
					field
			:
				'createdUserName',
					width
			:
				150,
					headerAttributes
			:
				{
					style: "text-align:center;"
				}
			,
				attributes: {
					"id"
				:
					"appFile",
						style
				:
					"text-align:left;"
				}
			,
			}
		,
			{
				title: "Xóa",
					template
			:
				dataItem =>
				'<div class="text-center #=attactmentId#""> ' +
				'<a type="button" class="#=attactmentId# icon_table" uib-tooltip="Xóa" translate> ' +
				'<i class="fa fa-trash" ng-click=caller.removeRowFile(dataItem) ria-hidden="true"></i>' +
				'</a>' +
				'</div>' ,
					width
			:
				'50px',
					field
			:
				"acctions"
			}
		,],
		})
       };
        
       function refreshGrid(grid, data) {
				grid.dataSource.data(data);
				grid.refresh();
			}

        vm.submitAttachFile = submitAttachFile;
        function submitAttachFile() {
            sendFile("attachfiles", callback);
        }
        
        function sendFile(id, callback) {
            var files = $("#" + id)[0].files;
            if (!$("#" + id)[0].files[0]) {
                toastr.warning("Bạn chưa chọn file");
                return;
            }
            if (!htmlCommonService.checkFileExtension(id)) {
                toastr.warning("File không đúng định dạng cho phép");
                return;
            }
            var formData = new FormData();
            jQuery.each($("#" + id)[0].files, function (i, file) {
            	console.log(file);
                formData.append('multipartFile' + i, file);
            });
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "fileservice/uploadATTTInput",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    callback(data, files)
                }
            });
        }
        // callback sau khi upload file thành công
        vm.callback = callback;
        function callback(data, files) {
            for (var i = 0; i < data.length; i++) {
                var file = {};
                file.name = files[i].name;
                file.createdDate = htmlCommonService.getFullDate();
                file.createdUserName = Constant.userInfo.vpsUserToken.fullName;
                file.createdUserId = Constant.userInfo.vpsUserToken.sysUserId;
                file.filePath = data[i];
                file.type = 0;
                vm.fileLst.push(file);
            }
            
            setTimeout(function () {
				$(".k-upload-files.k-reset").find("li").remove();
				$(".k-upload-files").remove();
				$(".k-upload-status").remove();
				$(".k-upload.k-header").addClass("k-upload-empty");
				$(".k-upload-button").removeClass("k-state-focused");
			}, 10);
            
            var grid = !!vm.designEstimatesFileGrid ? vm.designEstimatesFileGrid : vm.contractFileEditGrid;
            refreshGrid(grid, vm.fileLst);
            //      	vm.fileLst = $("#designEstimatesFileGrid").data("kendoGrid").dataSource.data();
            vm.fileLst = grid.dataSource.data();
            $('#attachfiles').val(null);
            $('#attachfilesEdit').val(null);
        }
        
        vm.removeRowFile = removeRowFile;
        function removeRowFile(dataItem) {
            confirm('Xác nhận xóa', function () {
                var index = vm.fileLst.indexOf(dataItem);
                vm.fileLst.splice(index, 1);
                
            	Restangular.all("designEstimatesService/deleteFile").post({
            		utilAttachDocumentId: dataItem.utilAttachDocumentId
					}).then(function (response) {
					});
                var grid = !!vm.designEstimatesFileGrid ? vm.designEstimatesFileGrid : vm.contractFileEditGrid;
                refreshGrid(grid, vm.fileLst);
            })
        }
        
        vm.downloadFile = function (dataItem) {
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
        }
    }
})();
