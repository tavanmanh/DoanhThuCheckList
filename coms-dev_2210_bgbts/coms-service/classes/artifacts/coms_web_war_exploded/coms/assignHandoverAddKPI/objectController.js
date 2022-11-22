(function () {
    'use strict';
    var controllerId = 'assignHandoverController';

    angular.module('MetronicApp').controller(controllerId, assignHandoverController);

    function assignHandoverController($scope, $templateCache, $rootScope, $timeout, gettextCatalog,
                                      kendoConfig, $kWindow, assignHandoverService, htmlCommonService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant) {

        var vm = this;
        vm.String = "Quản lý công trình > Báo cáo > Đánh giá KPI triển khai";
        vm.searchForm = {
            status: 1
        };
        vm.assignHandoverGrid = {};
        vm.showSearch = true;
        vm.showDetail = false;
        vm.folder = '';
        vm.assignHandover = {
            isDesign: 0
        };
        vm.fileLst = [];
        vm.dataEdit = {};
        vm.disableSearch = false;

        vm.errDeptAddMsg = null;
        vm.errStationHouseMsg = null;
        vm.errStationMsg = null;
        vm.errConstructionMsg = null;

        // function list
        vm.doSearch = doSearch;
        vm.checkDoSearch = checkDoSearch;
        vm.callbackDoSearch = callbackDoSearch;
        vm.onSave = onSave;
        vm.openDepartmentTo = openDepartmentTo;
        vm.openCatStationHouseTo = openCatStationHouseTo;
        vm.onSaveCatStationHouse = onSaveCatStationHouse;
        vm.openPopupCatStation = openPopupCatStation;
        vm.onSaveCatStation = onSaveCatStation;
        vm.openPopupConstruction = openPopupConstruction;
        vm.onSaveComsConstruction = onSaveComsConstruction;
        vm.add = add;
        vm.save = save;
        vm.validateAdd = validateAdd;
        vm.resetInputAdd = resetInputAdd;
        vm.submitAttachFile = submitAttachFile;
        vm.attachFileEdit = attachFileEdit;
        vm.uploadAttachEdit = uploadAttachEdit;
        vm.submitAttachFileEdit = submitAttachFileEdit;
        vm.callback = callback;
        vm.downloadFile = downloadFile;
        vm.removeRowFile = removeRowFile;
        vm.refreshGrid = refreshGrid;
        vm.remove = remove;
        vm.cancel = cancel;
        vm.exportFile = exportFile;
        vm.importAssignHandover = importAssignHandover;
        vm.submitImport = submitImport;
        vm.closeErrImportPopUp = closeErrImportPopUp;
        vm.downloadTemplate = downloadTemplate;
        vm.showHideColumn = showHideColumn;
        vm.gridColumnShowHideFilter = gridColumnShowHideFilter;
        vm.resetFormFieldConsType = resetFormFieldConsType;
        vm.resetFormFieldDate = resetFormFieldDate;
        vm.resetFormFieldSysGroup = resetFormFieldSysGroup;
        vm.checkErrDate = checkErrDate;
        vm.validateDataAdd = validateDataAdd;
        vm.resetFormAdd = resetFormAdd;
        vm.resetConstructionAddForm = resetConstructionAddForm;
        vm.checkHave= false;

        vm.disableAdd = true;

        function resetFormAdd(inputName) {
            if (inputName === 'sysGroup') {
                vm.assignHandover.sysGroupCode = null;
                vm.assignHandover.sysGroupName = null;
                vm.assignHandover.sysGroupId = null;
                vm.assignHandover.sysGroupText = null;
                vm.isSelectDeptAdd = false;
                vm.errDeptAddMsg = null;
            } else {
                resetConstructionAddForm();
                vm.errConstructionMsg = null;

                if (inputName !== 'construction') {
                    // case 'catStation':
                    vm.assignHandover.catStationId = null;
                    vm.assignHandover.catStationCode = null;
                    vm.isSelectStation = false;
                    vm.errStationMsg = null;
                    if (inputName === 'catStationHouse') {
                        vm.assignHandover.catStationHouseCode = null;
                        vm.assignHandover.catStationHouseId = null;
                        vm.isSelectStationHouse = false;
                        vm.errStationHouseMsg = null;
                    }
                }
            }
            validateDataAdd();
        }

        function validateDataAdd() {
//        	vm.disableAdd = !(vm.isSelectDeptAdd && vm.isSelectConstruction && vm.isSelectStation && vm.isSelectStationHouse);
            vm.disableAdd = !(vm.isSelectDeptAdd && vm.isSelectConstruction);
        }

        function checkDoSearch() {
            if (!vm.disableSearch) {
                doSearch();
            }
        }
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {

            //hienvd: Start 20190806
            Restangular.all("rpQuantityService/getGroupLv2ByGroupUser").post($rootScope.authenticatedUser.VpsUserInfo.sysGroupId).then(function (response) {
                vm.sysGroupIdLv2User = response.plain();
            }).catch(function (err) {
                console.log('Không thể lấy dữ liệu: ' + err.message);
            });

            Restangular.all("rpQuantityService/getSysGroupIdByTTKT").post().then(function (response) {
                var checkGroup = response.indexOf(vm.sysGroupIdLv2User.sysGroupId);
                if(checkGroup!=-1){
                    vm.searchForm.sysGroupId = vm.sysGroupIdLv2User.sysGroupId;
                    vm.searchForm.sysGroupText = vm.sysGroupIdLv2User.sysGroupCode + "-" +vm.sysGroupIdLv2User.sysGroupName;
                    doSearch();
                }
            }).catch(function (err) {
                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
            });
            //hienvd: End 8/6/2019
            initCatConstructionType();
            initDateField();
//            edit();

        }

        //init default data for search form
        function initCatConstructionType() {
            assignHandoverService.getconstructionType().then(function (data) {
                vm.catConstructionTypeDataList = data;
            });
        }

        function initDateField() {
            var date = new Date();
            var from = new Date(date.getFullYear(), date.getMonth() - 1, date.getDate());
            vm.searchForm.dateTo = htmlCommonService.formatDate(date);
            vm.searchForm.dateFrom = htmlCommonService.formatDate(from);


        }

        function doSearch() {
            vm.showDetail = false;
            var grid = vm.assignHandoverGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
            //console.log(grid.dataSource.data());
        }

        function callbackDoSearch() {
            vm.assignHandoverGrid.dataSource.page(1);
        }

        var record = 0;
        vm.assignHandoverGridOptions = kendoConfig.getGridOptions({
            autoBind : true,
            scrollable : true,
            resizable : true,
            editable : false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            reorderable: true,

            toolbar: [
                {
                    name: "actions",
                    template:
                        '<div class="btn-group pull-right margin_top_button margin_right10">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.assignHandoverGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                }
            ],
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                        $timeout(function() {vm.count = response.total});
                        return response.total;
                    },
                    data: function (response) {
                        return response.data;
                    }
                },
                transport: {
                    read: {
                        url: Constant.BASE_SERVICE_URL + RestEndpoint.ASSIGN_HANDOVER_KPI_SERVICE_URL + "/doSearchKPI",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.searchForm.page = options.page;
                        vm.searchForm.pageSize = options.pageSize;
                        vm.searchForm.text = vm.searchForm.sysGroupText;
                        return JSON.stringify(vm.searchForm)
                    }
                },
                pageSize: 10
            },

            noRecords: true,
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },

            pageable: {
                refresh: false,
                pageSizes: [10, 15, 20, 25],
                messages: {
                    display: "{0}-{1} của {2} kết quả",
                    itemsPerPage: "kết quả/trang",
                    empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                }
            },
            //TODO check field names, resize
            columns: [
                {
                    title: "TT",
                    field: "stt",
                    width: '50px',
                    template: function () {
                        return ++record;
                    },
                    columnMenu: false,
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                {
                    title: "Đơn vị",
                    field: 'sysGroupName',
                    width: '300px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Tỉnh",
                    field: 'catProvinceCode',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                {
                    title: "Mã nhà trạm",
                    field: 'catStationHouseCode',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Mã trạm",
                    field: 'catStationCode',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Mã CT",
                    field: 'constructionCode',
                    width: '220px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Hợp đồng",
                    field: 'cntContractCode',
                    width: '280px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Thiết kế",
                    field: 'isDesign',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        if (dataItem.isDesign == 1) {
                            return "<span name='isDesign' font-weight: bold;'>Có</span>"
                        } else {
                            return "<span name='isDesign' font-weight: bold;'>Không</span>"
                        }
                    }
                },
                {
                    title: "Ngày giao việc",
                    field: 'companyAssignDate',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                },
                {
                    title: "Loại CT",
                    field: 'constructTypeName',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal"
                    },
                },
                {
                    title: "Ngày nhận MB",
                    field: 'handoverDate',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal"
                    },

                },

                {
                    title: "Ngày khởi công",
                    field: 'startDate',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal"
                    },
                },
                {
                    title: "Vi phạm KPI nhận mặt bằng",
                    field: 'viphamMB',
                    width: '180px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal"
                    },
                },
                {
                    title: "Vi phạm KPI khởi công",
                    field: 'viphamKC',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal"
                    },
                },
                {
                    title: "Vi phạm KPI thi công",
                    field: 'viphamTC',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal"
                    },
                },

                {
                    title: "Ghi chú",
                    field: 'description',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal"
                    },
                },
    ]
    });

        function onSave(data) {
            switch (vm.popupId) {
                case 'search':
                    // vm.searchForm.sysGroupCode = data.code;
                    vm.searchForm.sysGroupText = data.code + '-' + data.name;
                    vm.searchForm.text = data.code + '-' + data.name;
                    vm.searchForm.sysGroupId = data.id;
                    break;
                case 'add':
                    vm.isSelectDeptAdd = true;
                    vm.assignHandover.sysGroupText = data.code + '-' + data.name;
                    vm.assignHandover.sysGroupCode = data.code;
                    vm.assignHandover.sysGroupName = data.name;
                    vm.assignHandover.sysGroupId = data.id;
                    vm.validateAddDept();

                    //VietNT_20190122_start
                    // reset construction field if select after construction
                    if (vm.isSelectConstruction) {
                        resetConstructionAddForm();
                    }
                    //VietNT_end
                    break;
            }
        }

        //pop up for SysGroupField
        function openDepartmentTo(popUpId) {
            vm.obj = {};
            vm.popupId = popUpId;
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUpId, 'string', false, '92%', '89%');
        }

        // grid options for field SysGroup
        vm.deptOptions = {
            dataTextField: "text",
            placeholder: "Nhập mã hoặc tên đơn vị",
            select: function (e) {
                var dataItem = this.dataItem(e.item.index());
                vm.searchForm.sysGroupText = dataItem.text;
                vm.searchForm.sysGroupId = dataItem.id;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({
                            name: vm.searchForm.sysGroupText,
                            pageSize: vm.deptOptions.pageSize
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.searchForm.sysGroupText = null;
                    vm.searchForm.sysGroupId = null;
                }
            },
            ignoreCase: false
        };

// Begin Add form function
        // grid opts for SysGroup, popup & save choice use same func with Search
        vm.isSelectDeptAdd = false;
        vm.deptAddOptions = {
            dataTextField: "text",
            placeholder: "Nhập mã hoặc tên đơn vị",
            open: function (e) {
                vm.isSelectDeptAdd = false;
            },
            select: function (e) {
                var dataItem = this.dataItem(e.item.index());
                vm.assignHandover.sysGroupText = dataItem.text;
                vm.assignHandover.sysGroupCode = dataItem.code;
                vm.assignHandover.sysGroupName = dataItem.name;
                vm.assignHandover.sysGroupId = dataItem.id;
                vm.isSelectDeptAdd = true;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.isSelectDeptAdd = false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({
                            name: vm.assignHandover.sysGroupText,
                            pageSize: vm.deptAddOptions.pageSize
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (!vm.isSelectDeptAdd) {
                    vm.assignHandover.sysGroupText = null;
                    vm.assignHandover.sysGroupCode = null;
                    vm.assignHandover.sysGroupName = null;
                    vm.assignHandover.sysGroupId = null;
                }
            },
            close: function (e) {
//                if (!vm.isSelectDeptAdd) {
//                   vm.assignHandover.sysGroupText = null;
//                    vm.assignHandover.sysGroupCode = null;
//                    vm.assignHandover.sysGroupName = null;
//                    vm.assignHandover.sysGroupId = null;
//                }
            },
            ignoreCase: false
        };

        // begin catStationHouse
        // popup catStationHouse
        function openCatStationHouseTo() {
            var templateUrl = 'coms/popup/findCatStationHousePopup.html';
            var title = gettextCatalog.getString("Tìm kiếm nhà trạm");
            htmlCommonService.populatePopup(templateUrl, title, null, null, vm, null, 'string', false, '92%', '89%', 'findCatStationHousePopupController');
        }

        // save catStationHouse choice from popup
        function onSaveCatStationHouse(data) {
            vm.isSelectStationHouse = true;
            vm.assignHandover.catStationHouseCode = data.code;
            vm.assignHandover.catStationHouseId = data.catStationHouseId;
            //vm.validateAddStationHouse();

            //VietNT_20190122_start
            // reset construction field if select after construction
            if (vm.isSelectConstruction) {
                resetConstructionAddForm();
            }
            //VietNT_end
        }

        //grid options for catStationHouse
        vm.isSelectStationHouse = false;
        vm.catStationHouseOptions = {
            dataTextField: "code",
            dataValueField: "id",
            placeholder: "Nhập mã nhà trạm",
            select: function (e) {
                vm.isSelectStationHouse = true;
                var dataItem = this.dataItem(e.item.index());
                vm.assignHandover.catStationHouseCode = dataItem.code;
                vm.assignHandover.catStationHouseId = dataItem.catStationHouseId;

                // reset input station & construction
                vm.assignHandover.catStationCode = null;
                vm.assignHandover.catStationId = null;

                //VietNT_20190122_start
                resetConstructionAddForm();
                //VietNT_end
            },
            pageSize: 10,
            open: function (e) {
                vm.isSelectStationHouse = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.isSelectStationHouse = false;
                        return Restangular.all("constructionService/" + 'getStationHouseForAutoComplete').post({
                            keySearch: vm.assignHandover.catStationHouseCode,
                            pageSize: vm.catStationHouseOptions.pageSize,
                            page: 1
                        }).then(function (response) {
                            options.success(response.data);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.address #</div> </div>',
            change: function (e) {
                if (!vm.isSelectStationHouse) {
                    vm.assignHandover.catStationHouseCode = null;
                    vm.assignHandover.catStationHouseId = null;

                    //VietNT_20190122_start
                    resetConstructionAddForm();
                    //VietNT_end
                }
            },
            close: function (e) {
                if (!vm.isSelectStationHouse) {
                    vm.assignHandover.catStationHouseCode = null;
                    vm.assignHandover.catStationHouseId = null;

                    //VietNT_20190122_start
                    resetConstructionAddForm();
                    //VietNT_end
                }
            },
            ignoreCase: false
        };
        // End catStationHouse

        // Begin catStation
        // popup catStation
        function openPopupCatStation() {
            var templateUrl = 'coms/popup/catStationSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm trạm");
            htmlCommonService.populatePopup(templateUrl, title, null, null, vm, null, 'string', false, '92%', '89%', 'catStationSearchController');
        }

        // save catStation choice from popup
        function onSaveCatStation(data) {
            vm.isSelectStation = true;
            vm.assignHandover.catStationCode = data.code;
            vm.assignHandover.catStationId = data.catStationId;
            //vm.validateAddStation();

            //VietNT_20190122_start
            if (vm.isSelectConstruction) {
                resetConstructionAddForm();
            }
            //VietNT_end
        }


        // grid options for catStation
        vm.isSelectStation = false;
        vm.catStationOptions = {
            dataTextField: "code",
            dataValueField: "id",
            placeholder: "Nhập mã trạm (theo Mã nhà trạm)",
            select: function (e) {
                vm.isSelectStation = true;
                var dataItem = this.dataItem(e.item.index());
                vm.assignHandover.catStationCode = dataItem.code;
                vm.assignHandover.catStationId = dataItem.catStationId;

                //VietNT_20190122_start
                if (vm.isSelectConstruction) {
                    resetConstructionAddForm();
                }
                //VietNT_end
            },
            pageSize: 10,
            open: function (e) {
                vm.isSelectStation = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.isSelectStation = false;
                        return Restangular.all("constructionService/" + 'getStationByStationHouseIdForAutoComplete').post({
                            catStationHouseId: vm.assignHandover.catStationHouseId,
                            keySearch: vm.assignHandover.catStationCode,
                            pageSize: vm.catStationOptions.pageSize,
                            page: 1
                        }).then(function (response) {
                            options.success(response.data);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.address #</div> </div>',
            change: function (e) {
                if (!vm.isSelectStation) {
                    vm.assignHandover.catStationCode = null;
                    vm.assignHandover.catStationId = null;
                    resetConstructionAddForm();
                }
            },
            close: function (e) {
                if (!vm.isSelectStation) {
                    vm.assignHandover.catStationCode = null;
                    vm.assignHandover.catStationId = null;
                    resetConstructionAddForm();
                }
            },
            ignoreCase: false
        };
        // End catStation=

        // Start construction
        // reset construction field
        // VietNT_20190122_start
        function resetConstructionAddForm() {
            // reset construction field if select after construction
            vm.assignHandover.constructionCode = null;
            vm.assignHandover.constructionId = null;
            vm.isSelectConstruction = false;
            validateDataAdd();
        }
        // VietNT_end

        // popup construction
        function openPopupConstruction() {
            var templateUrl = 'coms/popup/comsConstructionSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm công trình");
            htmlCommonService.populatePopup(templateUrl, title, null, null, vm, null, 'string', false, '92%', '89%', 'comsConstructionSearchController');
        }

        // save construction choice from popup
        function onSaveComsConstruction(data) {
            vm.isSelectConstruction = true;
            vm.assignHandover.constructionCode = data.code;
            vm.assignHandover.constructionId = data.constructionId;

            //VietNT_20190122_start
            // select construction fill data to station & stationHouse fields
            vm.assignHandover.catStationHouseId = data.catStationHouseId;
            vm.assignHandover.catStationHouseCode = data.catStationHouseCode;
            vm.assignHandover.catStationId = data.catStationId;
            vm.assignHandover.catStationCode= data.catStationCode;

            // fill province data
            vm.assignHandover.catProvinceId = data.catProvinceId;
            vm.assignHandover.catProvinceCode = data.catProvinceCode;
            //VietNT_end
            vm.validateAddConstruction();
        }

        // grid options for construction
        vm.isSelectConstruction = false;
        vm.constructionPopupOptions = {
            dataTextField: "code",
            dataValueField: "id",
            placeholder: "Nhập mã công trình (theo Mã trạm)",
            select: function (e) {
                vm.isSelectConstruction = true;
                var dataItem = this.dataItem(e.item.index());
                vm.assignHandover.constructionCode = dataItem.code;
                vm.assignHandover.constructionId = dataItem.constructionId;

                //VietNT_20190122_start
                // select construction fill data to station & stationHouse fields
                vm.assignHandover.catStationHouseId = dataItem.catStationHouseId;
                vm.assignHandover.catStationHouseCode = dataItem.catStationHouseCode;
                vm.assignHandover.catStationId = dataItem.catStationId;
                vm.assignHandover.catStationCode= dataItem.catStationCode;

                // fill province data
                vm.assignHandover.catProvinceId = dataItem.catProvinceId;
                vm.assignHandover.catProvinceCode = dataItem.catProvinceCode;

                //VietNT_end
            },
            pageSize: 10,
            open: function (e) {
                vm.isSelectConstruction = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.isSelectConstruction = false;
                        return Restangular.all("constructionTaskService/" + 'getConstructionByStationId').post({
                            catStationId: vm.assignHandover.catStationId,
                            keySearch: vm.assignHandover.constructionCode,
                            pageSize: vm.constructionPopupOptions.pageSize,
                            page: 1
                        }).then(function (response) {
                            options.success(response.data);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (!vm.isSelectConstruction) {
                    vm.assignHandover.constructionCode = null;
                    vm.assignHandover.constructionId = null;
                    validateDataAdd();
                }
            },
            close: function (e) {
                if (!vm.isSelectConstruction) {
                    vm.assignHandover.constructionCode = null;
                    vm.assignHandover.constructionId = null;
                    validateDataAdd();
                }
            },
            ignoreCase: false
        };
        // End construction
// end Add form Functions

// begin main function
        // add
        function add() {
            resetInputAdd();
            var teamplateUrl = "coms/assignHandover/assignHandoverPopup.html";
            var title = "TTHT giao việc CN/TTKV";
            var windowId = "ASSIGN_HANDOVER";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '70%', '450', "deptAdd");
        }

        function save() {
            var fileList = !!$("#attachFileGrid").data("kendoGrid")
                ? $("#attachFileGrid").data("kendoGrid").dataSource.data()
                : null;

            var data = vm.assignHandover;
            data.fileDesign = fileList[0];
            if (data.isDesign === 1 && !data.fileDesign) {
                toastr.error("Chưa đính kèm File Thiết kế");
                return;
            } else if (data.isDesign === 0 && data.fileDesign) {
                toastr.error("Không thể đính kèm File Thiết kế");
                return;
            }
            if (!vm.validateAdd(data)) {
                toastr.error("Chưa nhập đủ thông tin");
                return;
            }

            assignHandoverService.addNewAssignHandover(data).then(function (data) {
                // close popup and refresh table list
                if (data.error) {
                    toastr.error(data.error);
                    return;
                }
                toastr.success("Thêm mới dữ liệu thành công.");
                CommonService.dismissPopup1();
                doSearch();
                vm.cancel();
            }, function (e) {
                toastr.error("Có lỗi trong quá trình xử lý dữ liệu.");
            });
        }

        function validateAdd(data) {
            return !!data.sysGroupText && !!data.constructionCode;
        }

        function resetInputAdd() {
            vm.assignHandover.sysGroupCode = null;
            vm.assignHandover.sysGroupName = null;
            vm.assignHandover.sysGroupId = null;
            vm.assignHandover.sysGroupText = null;
            vm.assignHandover.catStationHouseCode = null;
            vm.assignHandover.catStationHouseId = null;
            vm.assignHandover.catStationId = null;
            vm.assignHandover.catStationHouseId = null;
            vm.assignHandover.catStationCode = null;
            vm.assignHandover.constructionCode = null;
            vm.assignHandover.constructionId = null;
            vm.assignHandover.catStationId = null;
            vm.assignHandover.constructionCode = null;
            vm.assignHandover.isDesign = 0;
            vm.isSelectDeptAdd = false;
            vm.isSelectConstruction = false;
            vm.isSelectStation = false;
            vm.isSelectStationHouse = false;
            vm.disableAdd = true;

            vm.errDeptAddMsg = null;
            vm.errStationHouseMsg = null;
            vm.errStationMsg = null;
            vm.errConstructionMsg = null;
        }

        function submitAttachFile() {
            if (vm.assignHandover.isDesign === 0) {
                toastr.warning('Cần chọn "Có thiết kế" trước khi đính kèm file');
                return;
            }
            sendFile("attachfile", callback);
        }

        // edit
        function attachFileEdit(dataItem) {
            if (dataItem.isDesign === null || dataItem.isDesign === 0) {
                toastr.error("Không thể đính kèm File thiết kế");
                return;
            }
            vm.dataEdit.assignHandoverId = dataItem.assignHandoverId;

            var templateUrl = "coms/assignHandover/assignHandoverAttach.html";
            var title = "Chọn file Thiết kế";
            var windowId = "ATTACH_DESIGN_EDIT";
            CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, true, '1000', '300', null);
        }

        function uploadAttachEdit() {
            var fileList = !!$("#attachFileEditGrid").data("kendoGrid")
                ? $("#attachFileEditGrid").data("kendoGrid").dataSource.data()
                : null;
            var data = vm.dataEdit;
            if (!!fileList[0]) {
                data.fileDesign = fileList[0];
                assignHandoverService.attachDesignFileEdit(data).then(function (res) {
                    if (res.error) {
                        toastr.error(res.error);
                        return;
                    }
                    toastr.success("Thay đổi File Thiết kế thành công!");
                    // CommonService.dismissPopup1();
                    doSearch();
                    vm.cancel();
                }, function (e) {
                    toastr.error("Có lỗi trong quá trình xử lý dữ liệu.");
                });
            }
        }

        function submitAttachFileEdit() {
            sendFile("attachfileEdit", callback);
        }

        // callback sau khi upload file thành công
        function callback(data, fileReturn, id) {
            for (var i = 0; i < data.length; i++) {
                var file = {};
                file.name = fileReturn.name;
                file.createdDate = htmlCommonService.getFullDate();
                file.createdUserName = Constant.userInfo.VpsUserInfo.fullName;
                file.createdUserId = Constant.userInfo.VpsUserInfo.sysUserId;
                file.filePath = data[i];
                file.type = 0;

                vm.fileLst = [];
                vm.fileLst.push(file);
            }

            var grid = id === "attachfile" ? vm.attachFileGrid : vm.attachFileEditGrid;
            refreshGrid(grid, vm.fileLst);
            $('#attachfile').val(null);
            $('#attachfileEdit').val(null);
        }

        function downloadFile(dataItem) {
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
        }

        function removeRowFile(dataItem) {
            confirm('Xác nhận xóa', function () {
                var index = vm.fileLst.indexOf(dataItem);
                vm.fileLst.splice(index, 1);
                // var grid = !!vm.attachFileGrid ? vm.attachFileGrid : vm.attachFileEditGrid;
                var grid = vm.attachFileGrid;
                refreshGrid(grid, vm.fileLst);
            })
        }

        function refreshGrid(grid, fileLst) {
            grid.dataSource.data(fileLst);
            grid.refresh();
        }

        function sendFile(id, callback) {
            var file = null;
            try {
                file = $("#" + id)[0].files[0];
            } catch (err) {
                toastr.warning("Bạn chưa chọn file");
                return;
            }

            if (!htmlCommonService.checkFileExtension(id)) {
                toastr.warning("File không đúng định dạng cho phép");
                return;
            }
            var formData = new FormData();
            formData.append('multipartFile', file);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "fileservice/uploadATTTInput",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    callback(data, file, id)
                }
            });
        }

        //gridOpts for view edit attach file
        vm.attachFileEditGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            resizable: true,
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                        $timeout(function() {vm.countCons = response.total});
                        return response.total;
                    },
                    data: function (response) {
                        return response.data;
                    }
                },
                transport: {
                    read: {
                        url: Constant.BASE_SERVICE_URL + RestEndpoint.ASSIGN_HANDOVER_SERVICE_URL + "/getById",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        return JSON.stringify(vm.dataEdit.assignHandoverId)
                    }
                },
                pageSize: 1
            },
            noRecords: true,
            columnMenu: false,
            scrollable: false,
            editable: true,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            dataBound: function () {
                var GridDestination = $("#attachFileEditGrid").data("kendoGrid");
                GridDestination.pager.element.hide();
            },
            columns: [
                {
                    title: "TT",
                    field: "stt",
                    template: dataItem => $("#attachFileEditGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
                width: 20,
            headerAttributes: {
            style: "text-align:center;"
        },
        attributes: {
            style: "text-align:center;"
        }
    }, {
            title: "Tên file",
                field: 'name',
                width: 150,
                headerAttributes: {
                style: "text-align:center;"
            },
            attributes: {
                style: "text-align:left;"
            },
            template: function (dataItem) {
                return "<a href='' ng-click='caller.downloadFile(dataItem)'>" + dataItem.name + "</a>";
            }
        }, {
            title: "Ngày upload",
                field: 'createdDate',
                width: 150,
                headerAttributes: {
                style: "text-align:center;"
            },
            attributes: {
                style: "text-align:left;"
            }
        }, {
            title: "Người upload",
                field: 'createdUserName',
                width: 150,
                headerAttributes: {
                style: "text-align:center;"
            },
            attributes: {
                style: "text-align:left;"
            }
        }
    ]
    });


        // gridOpts for view add attach file
        vm.attachFileGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            resizable: true,
            dataSource: vm.fileLst,
            noRecords: true,
            columnMenu: false,
            scrollable: false,
            editable: true,
            messages: {
                noRecords: gettextCatalog.getString("Không có kết quả hiển thị")
            },
            dataBound: function () {
                var GridDestination = $("#attachFileGrid").data("kendoGrid");
                GridDestination.pager.element.hide();
            },
            columns: [
                {
                    title: "TT",
                    field: "stt",
                    template: dataItem => $("#attachFileGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
                width: 20,
            headerAttributes: {
            style: "text-align:center;"
        },
        attributes: {
            style: "text-align:center;"
        }
    }, {
            title: "Tên file",
                field: 'name',
                width: 150,
                headerAttributes: {
                style: "text-align:center;"
            },
            attributes: {
                style: "text-align:left;"
            },
            template: function (dataItem) {
                return "<a href='' ng-click='caller.downloadFile(dataItem)'>" + dataItem.name + "</a>";
            }
        }, {
            title: "Ngày upload",
                field: 'createdDate',
                width: 150,
                headerAttributes: {
                style: "text-align:center;"
            },
            attributes: {
                "id": "appFile",
                    style: "text-align:left;"
            }
        }, {
            title: "Người upload",
                field: 'createdUserName',
                width: 150,
                headerAttributes: {
                style: "text-align:center;"
            },
            attributes: {
                "id": "appFile",
                    style: "text-align:left;"
            }
        }, {
            title: "Xóa",
                template: dataItem =>
            '<div class="text-center #=attactmentId#""> ' +
            '<a type="button" class="#=attactmentId# icon_table" uib-tooltip="Xóa" translate> ' +
            '<i class="fa fa-trash" ng-click=caller.removeRowFile(dataItem) ria-hidden="true"></i>' +
            '</a>' +
            '</div>',
                width: 20,
                field: "acctions"
        }
    ]
    });

        // remove assign handover
        function remove(dataItem) {
//            if (!!dataItem.performentId) {
//                toastr.error("Chi nhánh/TTKT đã giao việc cho nhân viên, không thể xóa!");
//            } else {

            assignHandoverService.getCheckDataWorkItem(dataItem).then(function (d) {
                if(d.length>0){
                    toastr.warning("Đã có hạng mục ghi nhận sản lượng, không được xoá !");
                    return false;
                } else {
                    confirm('Xác nhận xóa', function () {
                        assignHandoverService.removeAssignHandover(dataItem).then(
                            function (d) {
                                var sizePage = $("#assignHandoverGrid").data("kendoGrid").dataSource.total();
                                var pageSize = $("#assignHandoverGrid").data("kendoGrid").dataSource.pageSize();

                                if (sizePage % pageSize === 1) {
                                    var currentPage = $("#assignHandoverGrid").data("kendoGrid").dataSource.page();
                                    if (currentPage > 1) {
                                        $("#assignHandoverGrid").data("kendoGrid").dataSource.page(currentPage - 1);
                                    }
                                }
                                $("#assignHandoverGrid").data('kendoGrid').dataSource.read();
                                $("#assignHandoverGrid").data('kendoGrid').refresh();
                                toastr.success("Xóa thành công!");

                            }, function (errResponse) {
                                toastr.error("Lỗi không xóa được!");
                            });
                    });
                }
            });
//            }
        }

        // close popup
        function cancel() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

        // export excel
        var listRemove = [{
            title: "Thao tác",
        }];
        var listConvert = [{
            field:"isDesign",
            data: {
                1: 'Có thiết kế',
                0: 'Không có thiết kế',
                null: 'Không có thiết kế'
            }
        }];

        function exportFile() {
            vm.searchForm.page = null;
            vm.searchForm.pageSize = null;
            var data = vm.searchForm;

            Restangular.all("assignHandoverKPIService" + "/doSearchKPI").post(data).then(function(d) {
                var data = d.data;
                for (var i = 0; i < data.length; i++) {
                    data[i].companyAssignDate = !!data[i].companyAssignDate
                        ? kendo.toString(kendo.parseDate(data[i].companyAssignDate), 'dd/MM/yyyy')
                        : "";
                }
                CommonService.exportFile(vm.assignHandoverGrid, d.data, listRemove, listConvert, "Đánh giá KPI triển khai");
            });
        }

        // import excel
        function importAssignHandover() {
            vm.fileImportData = null;
            var templateUrl = "coms/assignHandover/assignHandoverImport.html";
            var title = "Tải lên file giao việc";
            var windowId = "IMPORT_ASSIGN_HANOVER";
            CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, true, '1000', '275', null);
        }

        function submitImport() {
            console.log("submitImport body");
            $('#testSubmit').addClass('loadersmall');
            vm.disableSubmit = true;
            if (!vm.fileImportData) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Bạn chưa chọn file để import");
                return;
            }
            if ($('.k-invalid-msg').is(':visible')) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                return;
            }
            if (vm.fileImportData.name.split('.').pop() !== 'xls' && vm.fileImportData.name.split('.').pop() !== 'xlsx') {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Sai định dạng file");
                return;
            }
            var formData = new FormData();
            formData.append('multipartFile', vm.fileImportData);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "assignHandoverService/importExcel?folder=temp",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if (data == 'NO_CONTENT') {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.warning("File import không có nội dung");
                    }
                    else if (!!data.error) {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.error(data.error);
                    }
                    else if (data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length > 0) {
                        vm.lstErrImport = data[data.length - 1].errorList;
                        vm.objectErr = data[data.length - 1];
                        var templateUrl = "wms/createImportRequirementManagement/importResultPopUp.html";
                        var title = "Kết quả Import";
                        var windowId = "ERR_IMPORT";
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        CommonService.populatePopupCreate(templateUrl, title, vm.lstErrImport, vm, windowId, false, '80%', '420px');
                        fillDataImportErrTable(vm.lstErrImport);
                    }
                    else {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.success("Import thành công");
                        doSearch();
                        vm.cancel();
                    }
                    $scope.$apply();
                }
            });
        }

        vm.exportExcelErr = function () {
            Restangular.all("fileservice/exportExcelError").post(vm.objectErr).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
                return;
            });
        }

        function closeErrImportPopUp() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

//        function downloadTemplate() {
//            assignHandoverService.downloadTemplate({}).then(function (d) {
//                var data = d.plain();
//                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
//            }).catch(function () {
//                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
//            });
//        }

        vm.disableExportExcel = false;
        //HuyPQ-start
        function downloadTemplate(){
            function displayLoading(target) {
                var obj = {};
                var element = $(target);
                kendo.ui.progress(element, true);
                setTimeout(function(){

                    if (vm.disableExportExcel)
                        return;
                    vm.disableExportExcel = true;
                    return Restangular.all("assignHandoverService/exportAssignHandoverCN").post(obj).then(function (d) {
                        var data = d.plain();
                        window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                        kendo.ui.progress(element, false);
                        vm.disableExportExcel = false;
                    }).catch(function (e) {
                        kendo.ui.progress(element, false);
                        toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                        vm.disableExportExcel = false;
                        return;
                    });
                });

            }
            displayLoading("#shipment");
        }
        //HuyPQ-edit-end

        // reuse func
        function fillDataImportErrTable(data) {
            vm.importGoodResultGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                resizable: true,
                dataSource: data,
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("Không có kết quả hiển thị")
                },
                pageSize: 10,
                pageable: {
                    pageSize: 10,
                    refresh: false,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "Không có kết quả hiển thị"
                    }
                },
                columns: [
                    {
                        title: "TT",
                        field: "stt",
                        template: dataItem => $("#importGoodResultGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
                    width: 70,
                headerAttributes: {
                style: "text-align:center;"
            },
            attributes: {
                style: "text-align:center;"
            },
        }, {
                title: "Dòng lỗi",
                    field: 'lineError',
                    width: 100,
                    headerAttributes: {
                    style: "text-align:center;"
                },
                attributes: {
                    style: "text-align:center;"
                }
            }, {
                title: "Cột lỗi",
                    field: 'columnError',
                    width: 100,
                    headerAttributes: {
                    style: "text-align:center;"
                },
                attributes: {
                    style: "text-align:center;"
                }
            }, {
                title: "Nội dung lỗi",
                    field: 'detailError',
                    width: 250,
                    headerAttributes: {
                    style: "text-align:center;"
                },
                attributes: {
                    style: "text-align:left;"
                }
            }
        ]
        });
        }

        // show hide column
        function showHideColumn(column) {
            if (angular.isUndefined(column.hidden)) {
                vm.assignHandoverGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.assignHandoverGrid.showColumn(column);
            } else {
                vm.assignHandoverGrid.hideColumn(column);
            }
        }

        function gridColumnShowHideFilter(item) {
            return item.type == null || item.type !== 1;
        }

        //reset form functions
        function resetFormFieldConsType() {
            vm.searchForm.listCatConstructionType = null;
        }

        function resetFormFieldDate() {
            vm.searchForm.dateFrom = null;
            vm.searchForm.dateTo = null;
            vm.disableSearch = false;
            vm.errMessage = null;
            vm.errMessage1 = null;
            vm.errMessage2 = null;
        }

        function resetFormFieldSysGroup() {
            vm.searchForm.sysGroupId = null;
            vm.searchForm.sysGroupName = null;
            vm.searchForm.sysGroupCode = null;
            vm.searchForm.sysGroupText = null;
        }

        // validate
        vm.validateAddDept = function () {
            if (!!!vm.assignHandover.sysGroupText) {
                vm.errDeptAddMsg = "Đơn vị không để trống";
            } else if (!vm.isSelectDeptAdd) {
                vm.errDeptAddMsg = "Đơn vị không hợp lệ";
            } else {
                vm.errDeptAddMsg = null;
            }
            validateDataAdd();
        };
        /*
        vm.validateAddStationHouse = function () {
        	if (!!!vm.assignHandover.catStationHouseCode) {
                vm.errStationHouseMsg = "Mã nhà trạm không được để trống";
        	} else if (!vm.isSelectStationHouse) {
        		vm.errStationHouseMsg = "Mã nhà trạm không hợp lệ";
        	} else {
        		vm.errStationHouseMsg = null;
        	}
            validateDataAdd();
        };

        vm.validateAddStation = function () {
            if (!!!vm.assignHandover.catStationCode) {
                vm.errStationMsg = "Mã trạm không dược để trống";
            } else if (!vm.isSelectStationHouse) {
                vm.errStationMsg = "Mã trạm không hợp lệ";
        	} else {
        		vm.errStationMsg = null;
        	}
            validateDataAdd();
        };
        */

        vm.validateAddConstruction = function () {
            if (!!!vm.assignHandover.constructionCode) {
                vm.errConstructionMsg = "Mã công trình không được để trống"
            } else if (!vm.isSelectConstruction) {
                vm.errConstructionMsg = "Mã công trình không hợp lệ";
            } else {
                vm.errConstructionMsg = null;
            }
            validateDataAdd();
        };

        function checkErrDate(key) {
            var date;
            vm.disableSearch = false;
            if (key === 'dateFrom') {
                date = vm.searchForm.dateFrom;
                vm.errMessage = '';
                vm.errMessage1 = '';
                var curDate = new Date();
                // if (date == "") {
                //     vm.errMessage1 = CommonService.translate('Ngày không được để trống');
                //     $("#dateFrom").focus();
                //     vm.checkDateFrom = false;
                //     return vm.errMessage1;
                // } else
                if (kendo.parseDate(date, "dd/MM/yyyy") == null) {
                    vm.errMessage1 = CommonService.translate('Ngày không hợp lệ');
                    $("#dateFrom").focus();
                    vm.disableSearch = true;
                    return vm.errMessage1;
                } else if (kendo.parseDate(date, "dd/MM/yyyy").getFullYear() > 9999 || kendo.parseDate(date, "dd/MM/yyyy").getFullYear() < 1000) {
                    vm.errMessage1 = CommonService.translate('Ngày không hợp lệ');
                    $("#dateFrom").focus();
                    vm.disableSearch = true;
                    return vm.errMessage1;
                }
            } else {
                date = vm.searchForm.dateTo;
                vm.errMessage = '';
                vm.errMessage2 = '';
                var curDate = new Date();
                // if (date == "") {
                //     vm.errMessage2 = CommonService.translate('Ngày không được để trống');
                //     $("#dateTo").focus();
                //     vm.checkDateFrom = false;
                //     return vm.errMessage2;
                // } else
                if (kendo.parseDate(date, "dd/MM/yyyy") == null) {
                    vm.errMessage2 = CommonService.translate('Ngày không hợp lệ');
                    $("#dateTo").focus();
                    vm.disableSearch = true;
                    return vm.errMessage2;
                } else if (kendo.parseDate(date, "dd/MM/yyyy").getFullYear() > 9999 || kendo.parseDate(date, "dd/MM/yyyy").getFullYear() < 1000) {
                    vm.errMessage2 = CommonService.translate('Ngày không hợp lệ');
                    $("#dateTo").focus();
                    vm.disableSearch = true;
                    return vm.errMessage2;
                }
            }
        }

        // validate date man hinh tim kiem
        vm.validateDate = validateDate;
        function validateDate(){
            vm.errMessage = null;
            vm.errMessage1 = null;

            vm.checkStartDate = false;
            vm.checkEndDate = false;

            var startDate = $('#dateFrom').val();
            var endDate = $('#dateTo').val();

            // kiem tra field nao dc nhap
            if(startDate !== "") {
                if(kendo.parseDate(startDate,"dd/MM/yyyy") == null){
                    vm.errMessage1 = "Ngày bắt đầu không hợp lệ";
                    $("#dateFrom").focus();
                    vm.checkStartDate = false;
                }else if(kendo.parseDate(startDate,"dd/MM/yyyy").getFullYear() > 2099 || kendo.parseDate(startDate,"dd/MM/yyyy").getFullYear() < 1900){
                    vm.errMessage1 = 'Ngày bắt đầu không hợp lệ';
                    $("#dateFrom").focus();
                    vm.checkStartDate = false;
                } else {
                    vm.errMessage1 = null;
                    vm.checkStartDate = true;
                }
            }

            if(endDate !== "") {
                if ( kendo.parseDate(endDate,"dd/MM/yyyy") == null) {
                    vm.errMessage = "Ngày kết thúc không hợp lệ";
                    $("#dateTo").focus();
                    vm.checkEndDate = false;
                }else if(kendo.parseDate(endDate,"dd/MM/yyyy").getFullYear() > 2099 || kendo.parseDate(endDate,"dd/MM/yyyy").getFullYear() < 1900){
                    vm.errMessage = 'Ngày kết thúc không hợp lệ';
                    $("#dateTo").focus();
                    vm.checkEndDate = false;
                } else {
                    vm.errMessage = null;
                    vm.checkEndDate = true;
                }
            }
            if(!!vm.checkStartDate && !!vm.checkEndDate) {
                if(kendo.parseDate(startDate,"dd/MM/yyyy") > kendo.parseDate(endDate,"dd/MM/yyyy")) {
                    vm.errMessage = 'Giá trị Từ ngày phải nhỏ hơn hoặc bằng giá trị Đến ngày';
                    $("#workItemSearchDateFrom").focus();
                }else {
                    vm.errMessage = null;
                    vm.errMessage1 = null;
                }
                return;
            }
        }

        vm.clearSearchDate = function(){
            vm.searchForm.dateFrom = null;
            vm.searchForm.dateTo = null;
            vm.errMessage1 = null;
            vm.errMessage = null;
        }

    }

})();