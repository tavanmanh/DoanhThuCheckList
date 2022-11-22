(function () {
    'use strict';
    var controllerId = 'retrievalManagementController';

    angular.module('MetronicApp').controller(controllerId, retrievalManagementController);

    function retrievalManagementController($scope, $rootScope, $timeout, gettextCatalog, retrievalManagementService, $filter,
                                           kendoConfig, $kWindow, htmlCommonService,
                                           CommonService, PopupConst, Restangular, RestEndpoint, Constant) {

        var vm = this;
        vm.searchForm = {};
        var record = 0;
        vm.d = {};
        vm.obj1 = {};
        vm.obj = {};
        vm.tab1 = true;
        vm.tab2 = false;
        vm.isAddNew = false;
        vm.showDetail = false;
        vm.catReason = [];
        vm.showDetailAdd = true;
        vm.showDSVTGrid = true;
        vm.showDetailDSVTTGrid = false;
        vm.stockId = null;
        vm.viewDetail = false;

        vm.String = "Quản lý công trình > Quản lý công trình > Danh sách yêu cầu thu hồi VTTB";

        initDropDown();
        function initDropDown() {
            Restangular.all("assetManagementService/getCatReason").post().then(function (data) {
                vm.catReason = data;
            });
        }

        vm.cancelAdd = cancelAdd;
        function cancelAdd() {
            vm.showDetail = false;
            vm.prevStep();
            vm.DSVTGrid.dataSource.data([]);
            vm.DSVTGrid.refresh();
            vm.DSTBGrid.dataSource.data([]);
            vm.DSTBGrid.refresh();
            vm.doSearch();
            vm.String = "Quản lý công trình > Quản lý yêu cầu thu hồi VTTB > Danh sách yêu cầu thu hồi VTTB";
        }

        vm.nextStep2 = function () {
            if (validateAsset()) {
                vm.tab1 = false;
                vm.tab2 = true;
                $("#creImpReqBCIconOne").removeClass("acceptQLK postion-icon");
                $("#creImpReqBCIconOne").addClass("declineQLK postion-icon");
                $("#creImpReqBCone").removeClass("active");
                $("#creImpReqBCtwo").addClass("active");
                $("#creImpReqBCIconTwo").removeClass("declineQLK postion-icon");
                $("#creImpReqBCIconTwo").addClass("acceptQLK postion-icon");
            }

            if(vm.viewDetail){
                vm.obj.stockId = vm.stockId;
            }
            //hienvd5: Comment trường hợp assetManagementRequestId != null có luồng thu hồi rồi
            if (vm.obj.assetManagementRequestId != null) {
            	// update
                vm.showDSVTGrid = false;
                vm.showDetailDSVTTGrid =true;
                Restangular.all('assetManagementService' + "/DSVT").post(vm.obj).then(function (data) {        //var grid = $("#yearPlanDetail").data("kendoGrid");
                    var grid = vm.detailDSVTGrid;
                    if (grid) {
                        grid.dataSource.data(data);
                        grid.dataSource.query({
                            page: 1,
                            pageSize: 10
                        });
                    }
                }, function (error) {

                });

                Restangular.all('assetManagementService' + "/DSTB").post(vm.obj).then(function (data) {        //var grid = $("#yearPlanDetail").data("kendoGrid");
                    var grid = vm.DSTBGrid;
                    if (grid) {
                        grid.dataSource.data(data);
                        grid.dataSource.query({
                            page: 1,
                            pageSize: 10
                        });
                    }
                }, function (error) {

                });

            } else {
            	// insert
                vm.showDSVTGrid = true;
                vm.showDetailDSVTTGrid =false;
                Restangular.all('assetManagementService' + "/DSVTT").post(vm.obj).then(function (data) {        //var grid = $("#yearPlanDetail").data("kendoGrid");
                    var grid = vm.DSVTGrid;
                    if (grid) {
                        grid.dataSource.data(data);
                        grid.dataSource.query({
                            page: 1,
                            pageSize: 10
                        });
                    }
                }, function (error) {

                });
                Restangular.all('assetManagementService' + "/DSTBT").post(vm.obj).then(function (data) {        //var grid = $("#yearPlanDetail").data("kendoGrid");
                    var grid = vm.DSTBGrid;
                    if (grid) {
                        grid.dataSource.data(data);
                        grid.dataSource.query({
                            page: 1,
                            pageSize: 10
                        });
                    }
                }, function (error) {

                });

            }
            if (vm.edit1) {
                $("#DSVT").data("kendoGrid").setOptions({ editable: true });
            } else {
                $("#DSVT").data("kendoGrid").setOptions({ editable: false });
            }
        }

        vm.prevStep = function () {
            vm.tab1 = true;
            vm.tab2 = false;
            $("#creImpReqBCIconTwo").removeClass("acceptQLK postion-icon");
            $("#creImpReqBCIconTwo").addClass("declineQLK postion-icon");
            $("#creImpReqBCtwo").removeClass("active");
            $("#creImpReqBCone").addClass("active");
            $("#creImpReqBCIconOne").removeClass("declineQLK postion-icon");
            $("#creImpReqBCIconOne").addClass("acceptQLK postion-icon");
        }

        function validateAsset() {

            if (vm.obj.content == null || vm.obj.content == '') {
                toastr.warning("Chưa nhập nội dung thu hồi");
                return;
            }
            if (vm.obj.code == null || vm.obj.code == '') {
                toastr.warning("Chưa nhập mã thu hồi");
                return;
            }
            if (vm.obj.constructionCode == null || vm.obj.constructionCode == '') {
                toastr.warning("Chưa nhập công trình thu hồi");
                return;
            } if (vm.obj.catReasonId == null || vm.obj.catReasonId == '') {
                toastr.warning("Chưa nhập lý do thu hồi");
                return;
            } if (vm.obj.requestGroupName == null || vm.obj.requestGroupName == '') {
                toastr.warning("Chưa nhập đơn vị yêu cầu");
                return;
            } if (vm.obj.receiveGroupName == null || vm.obj.receiveGroupName == '') {
                toastr.warning("Chưa nhập đơn vị nhận yêu cầu");
                return;
            }
            if((vm.obj.stockId == null || vm.obj.stockId == '') && !vm.viewDetail ){
                toastr.warning("Chưa chọn kho");
                return;
            }
            return true;
        }


        vm.add = add;
        function add() {

            vm.viewDetail = false;
            vm.obj = null;

            return Restangular.all("assetManagementService/checkPermissionsAdd").post().then(function (d) {
                if (d) {
                    if (d.error) {
                        toastr.error(d.error);
                        return;
                    }
                }
            }).catch(function (e) {
                vm.tab1 = true;
                vm.tab2 = false;
                vm.showDetail = true;
                vm.showDetailAdd = true;
                vm.disabled = false;
                vm.edit1 = true;
                vm.String = "Quản lý công trình > Quản lý yêu cầu thu hồi VTTB > Tạo mới yêu cầu thu hồi VTTB";
                vm.obj = {
                    amrdDSVTDTOList: [],
                    amrdDSTBDTOList: [],
                    listFileTHVTTB: []
                };
                vm.obj1 = {};
                vm.obj.codeOrdersHide = 1;
                vm.mycnk = false;
                vm.DSFgrid.dataSource.data([]);
                vm.DSFgrid.refresh();
                var year = (new Date()).getYear();
                var newYear = year % 100;

                retrievalManagementService.getSequence().then(function (seq) {
                    var b = "seq".length;
                    if (b == 1) {
                        seq = "00000" + seq;

                    }
                    if (b == 2) {
                        seq = "0000" + seq;
                    }
                    if (b == 3) {
                        seq = "000" + seq;
                    }
                    if (b == 4) {
                        seq = "00" + seq
                    }
                    if (b == 5) {
                        seq = "0" + seq;
                    }
                    vm.obj.code = 'YCTH_QLTS/' + newYear + '/' + seq;
                    //vm.yearPlan.name = 'Kế hoạch thực hiện năm ' + vm.yearPlan.year;
                }, function (err) {
                    toastr.warning("Lỗi hiện thị sequense mã thu hồi");

                });
            });


        }


        vm.openTaskPopup = openTaskPopup

        function openTaskPopup(popUp) {
            //vm.openTaskPopup = function(){
            vm.keySearch = undefined;
            vm.departmentpopUp = popUp;
            var title = "Danh sách công trình";
            vm.labelSearch = "Mã/Tên công trình";

            var teamplateUrl = "coms/retrievalManagement/popupConstruction.html";

            var windowId = "RETRIEVAL_MANAGEMENT";
            CommonService.populatePopupOnPopup(teamplateUrl, title, null, vm, windowId, true, '950', '450', null);

        }

        vm.chooseConstruction = function (data) {
            vm.obj.constructionName = data.name;
            vm.obj.constructionCode = data.code;
            vm.obj.constructionId = data.constructionId;
            vm.obj.receiveGroupName = data.receiveGroupName;
            vm.obj.receiveGroupId = data.receiveGroupId;
            vm.obj.stationId = data.catStationId;
            vm.obj.stationCode = data.catStationCode;
            CommonService.dismissPopup1();

            //hienvd5: Add 11032020
            if(vm.obj.constructionId){
                Restangular.all('assetManagementService' + "/getListStockByConstructionId").post(vm.obj).then(function (data) {
                    vm.listStock = data;
                }, function (error) {
                    toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
                });
            }
            //hienvd5: End 11032020
        }

        vm.clearInput = function (data) {
            switch (data) {
                case '0':
                    vm.obj.constructionName = null;
                    vm.obj.constructionCode = null;
                    vm.obj.constructionId = null;
                    vm.obj.receiveGroupName = null;
                    //hienvd5: Start 100302020
                    vm.listStock = [];
                    //hienvd5: End 10032020
                    break;
                case '1':
                    vm.obj.requestGroupName = null;
                    vm.obj.requestGroupId = null;

                    break;
                case '2':
                    vm.obj.receiveGroupName = null;
                    vm.obj.receiveGroupId = null;
                    break;
                default: break;
            }
        }

        vm.selectedDept11 = false;
        vm.workCode = [];
        vm.workCode1 = [];
        vm.constructionOptions = {
            dataTextField: "code",
            dataValueField: "constructionId",
            select: function (e) {
                vm.selectedDept11 = true;
                var data = this.dataItem(e.item.index());

                vm.obj.constructionName = data.name;
                vm.obj.constructionCode = data.code;
                vm.obj.constructionId = data.constructionId;


                vm.obj.receiveGroupName = data.receiveGroupName;
                vm.obj.receiveGroupId = data.receiveGroupId;

                vm.obj.stationId = data.catStationId;
                vm.obj.stationCode = data.catStationCode;

                if (vm.obj.receiveGroupId == null) {
                    vm.obj.receiveGroupId = null;
                }
                if (vm.obj.receiveGroupName == null) {
                    vm.obj.receiveGroupName = null;
                }
                //hienvd5: Start 10032020
                if(vm.obj.constructionId){
                    Restangular.all('assetManagementService' + "/getListStockByConstructionId").post(vm.obj).then(function (data) {
                        vm.listStock = data;
                    }, function (error) {
                        toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
                    });
                }
                //hienvd5: End 10032020
            },
            pageSize: 10,
            open: function (e) {
                vm.selectedDept11 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedDept11 = false;
                        return Restangular.all("constructionTaskService/construction/searchConstructionDSTH").post({
                            keySearch: vm.obj.constructionCode,
                            pageSize: vm.constructionOptions.pageSize,
                            page: 1
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.code #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.obj.constructionName = null;
                    vm.obj.constructionCode = null;
                    vm.obj.constructionId = null;
                    //hienvd5: Start 10032020
                    vm.listStock = [];
                    //hienvd5: End 10032020

                }
            },
            ignoreCase: false
        }
        vm.closePopupOnPopup = function () {
            CommonService.dismissPopup1();
        }
        vm.doSearchPopup = doSearchPopup;
        function doSearchPopup(data) {
            var grid;

            grid = vm.listConstructionPopupGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        }
        vm.constructionSearch = {}
        vm.listConstructionPopupOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            reorderable: true,
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                        vm.countCons = response.total;
                        return response.total;
                    },
                    data: function (response) {
                        return response.data;
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "constructionService/doSearchDSTH",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.searchForm.page = options.page;
                        vm.searchForm.pageSize = options.pageSize;
                        vm.searchForm.keySearch = vm.keySearch;
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
            columns: [
                {
                    title: "TT",
                    field: "stt",
                    template: function () {
                        return ++record;
                    },
                    width: '5%',
                    columnMenu: false,
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                {
                    title: "Loại công trình",
                    field: 'catContructionTypeName',
                    width: '10%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Mã công trình",
                    field: 'code',
                    width: '20%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Tên công trình",
                    field: 'name',
                    width: '35%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },

                {
                    title: "Trạng thái",
                    field: 'status',
                    width: '10%',
                    template: function (dataItem) {
                        if (dataItem.status == 1) {
                            return "<span name='status' font-weight: bold;'>Chờ bàn giao mặt bằng</span>"
                        } else if (dataItem.status == 2) {
                            return "<span name='status' font-weight: bold;'>Chờ khởi công</span>"
                        } else if (dataItem.status == 3) {
                            return "<span name='status' font-weight: bold;'>Đang thực hiện</span>"
                        } else if (dataItem.status == 4) {
                            return "<span name='status' font-weight: bold;'>Đã tạm dừng</span>"
                        } else if (dataItem.status == 5) {
                            return "<span name='status' font-weight: bold;'>Đã hoàn thành</span>"
                        } else if (dataItem.status == 6) {
                            return "<span name='status' font-weight: bold;'>Đã nghiệm thu</span>"
                        } else if (dataItem.status == 7) {
                            return "<span name='status' font-weight: bold;'>Đã hoàn công</span>"
                        } else if (dataItem.status == 8) {
                            return "<span name='status' font-weight: bold;'>Đã quyết toán</span>"
                        } else if (dataItem.status == 0) {
                            return "<span name='status' font-weight: bold;'>Đã hủy</span>"
                        }
                    },
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Chọn",
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    template: dataItem =>
                '<div class="text-center">' +
                '<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate >' +
                '<i id="#=code#"  ng-click="caller.chooseConstruction(dataItem)"  class="fa fa-check color-green" aria-hidden="true"></i>' +
                '</a>'
                + '</div>',
                width: '10%'
    },
    ]
    });


        vm.retrievalGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            toolbar: [
                {
                    name: "actions",
                    template: '<div class=" pull-left ">' +
                        '<button class="btn btn-qlk padding-search-right addQLK"' +
                        'ng-click="vm.add()"  uib-tooltip="Tạo mới" translate>Tạo mới</button>' +
                        '</div>'
                        +
                        '<div class="btn-group pull-right margin_top_button margin10">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.retrievalGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                }
            ],
            reorderable: true,
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                        $("#appCount").text("" + response.total);
                        vm.count = response.total;
                        return response.total;
                    },
                    data: function (response) {
                        var list = response.data;
                        return response.data;
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "assetManagementService/doSearch",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.searchForm.page = options.page
                        vm.searchForm.pageSize = options.pageSize

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
            columns: [
                {
                    title: "TT",
                    field: "stt",
                    template: function () {
                        return ++record;
                    },
                    width: '5%',
                    columnMenu: false,
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    }
                }, {
                    title: "Mã yêu cầu thu hồi",
                    field: 'code',
                    width: '15%',
                    headerAttributes: { style: "text-align:left;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                }, {
                    title: "Mã trạm",
                    field: 'catStationCode',
                    width: '8%',
                    headerAttributes: { style: "text-align:left;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                }, {
                    title: "Mã công trình",
                    field: 'constructionCode',
                    width: '15%',
                    headerAttributes: { style: "text-align:left;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                }, {
                    title: "Đơn vị yêu cầu",
                    field: 'requestGroupName',
                    width: '22%',
                    headerAttributes: { style: "text-align:left;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                }, {
                    title: "Đơn vị nhận yêu cầu",
                    field: 'receiveGroupName',
                    width: '20%',
                    headerAttributes: { style: "text-align:left;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },{
                    title: "Ngày tạo",
                    field: 'createdDate',
                    width: '10%',
                    headerAttributes: { style: "text-align:left;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: function (dataItem) {
                        return $filter('date')(dataItem.createdDate, 'dd/MM/yyyy');
                    }
                },{
                    title: "Người tạo",
                    field: 'userName',
                    width: '20%',
                    headerAttributes: { style: "text-align:left;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                }, {
                    title: "Trạng thái",
                    field: 'status',
                    width: '10%',
                    headerAttributes: { style: "text-align:left;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: "# if(status == 1){ #" + "#= 'Đã tạo yêu cầu nhập kho' #" + "# } " + "else if (status == 2) { # " + "#= 'Đã tạo phiếu nhập kho' #" + "#}" + "else if (status == 3) { # " + "#= 'Đã nhập kho' #" + "#} " + "else if (status == 4) { # " + "#= 'Đã hủy' #" + "#} #"
                }, {
                    title: "Thao tác",
                    field: 'actions',
                    width: '10%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: dataItem =>
                '<div class="text-center">'
                + '<button  style=" border: none; background-color: white;" id="updateId"  ng-click="vm.edit(dataItem)" class=" icon_table "' +
                '   uib-tooltip="Xem chi tiết" translate>' +
                '<i class="fa fa-list-alt" style="color:#e0d014"  aria-hidden="true"></i>' +
                '</button>'
                + '<button ng-if="vm.showHideButton(dataItem)" style=" border: none; background-color: white;" id=""' +
                'class=" icon_table" ng-click="vm.remove(dataItem)"   uib-tooltip="Xóa" translate' + '>' +
                '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
                '</button>'
                + '<button ng-if="!vm.showHideButton(dataItem)" style=" border: none; background-color: white;" id=""' +
                'class=" icon_table" uib-tooltip="Khóa" translate' + '>' +
                '<i class="fa fa-trash" style="color: gray;"  aria-hidden="true"></i>' +
                '</button>'
                + '</div>'
        }
    ]
    });

        vm.showHideButton = function (dataItem) {
            if (dataItem.status === '3' || dataItem.status === '4') {
                return false;
            } else {
                return true;
            }
        }

        vm.showHideColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.retrievalGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.retrievalGrid.showColumn(column);
            } else {
                vm.retrievalGrid.hideColumn(column);
            }
        }
        /*
        * * Filter các cột của select
        */

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };

        //xuat excel
        vm.exportFile = function () {
            function displayLoading(target) {
                var element = $(target);
                kendo.ui.progress(element, true);
                setTimeout(function(){

                    var a = vm.searchForm;
                    var b = vm.obj;
                    var obj = retrievalManagementService.getItem();

                    return Restangular.all("assetManagementService/exportRetrievalTask").post(vm.searchForm).then(function (d) {
                        var data = d.plain();
                        window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                        kendo.ui.progress(element, false);
                    }).catch(function (e) {
                        kendo.ui.progress(element, false);
                        toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                        return;
                    });
                });

            }
            displayLoading(".tab-content");


        }
        function refreshGrid(d) {
            var grid = vm.retrievalGrid;
            if (grid) {
                grid.dataSource.data(d);
                grid.refresh();
            }
        }



        vm.DSFgridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            reorderable: true,
            noRecords: true,
            save: function () {
                var grid = this;
                setTimeout(function () {
                    grid.refresh();
                })
            },
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            pageable: {
                refresh: false,
                pageSize: 10,
                pageSizes: [10, 15, 20, 25],
                messages: {
                    display: "{0}-{1} của {2} kết quả",
                    itemsPerPage: "kết quả/trang",
                    empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                }
            },
            columns: [
                {
                    title: "TT",
                    field: "stt",
                    template: function () {
                        return ++record;
                    },
                    width: '5%',
                    columnMenu: false,
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                {
                    title: "Tên file",
                    field: 'name',
                    width: '40%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: function (dataItem) {
                        return "<a href='' ng-click='vm.downloadFile(dataItem)'>" + dataItem.name + "</a>";
                    }
                },
                {
                    title: "Ngày upload",
                    field: 'createdDate',
                    width: '20%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        return $filter('date')(dataItem.createdDate, 'dd/MM/yyyy');
                    }
                },
                {
                    title: "Người upload",
                    field: 'createdUserName',
                    width: '20%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Xóa",
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    template: dataItem =>
                '<div class="text-center">'
                + '<button style=" border: none; background-color: white;" id=""' +
                'class=" icon_table" ng-click="vm.removeDSFgridItemFile($event)"  uib-tooltip="Xóa" translate' + '>' +
                '<i class="fa fa-trash" style="color: #337ab7;" aria-hidden="true"></i>' +
                '</button>'
                + '</div>',
                width: '15%'
    }
    ]
    });


        vm.remove = function (dataItem) {
            console.log(dataItem);

            var merEntityId = null;
            Restangular.all('assetManagementService' + "/getMerEntityToAssetManagementRequest").post(dataItem).then(function (data) {
                console.log(data);
                dataItem.merEntityId = data[0].merEntityId;
                dataItem.stockId = data[0].stockId;
                var listMerEntity = new Array();
                for(var i = 0; i < data.length; i++){
                    listMerEntity.splice(i, 1, data[i].merEntityId);

                }
                dataItem.listMerEntity = listMerEntity;

            }, function (error) {
                toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
            });
            return Restangular.all("assetManagementService/checkPermissionsRemove").post(dataItem).then(function (d) {
                if (d.error) {
                    toastr.error(d.error);
                    return;
                }
            }).catch(function (e) {
                if (dataItem.status == 3) {
                    toastr.error("Không được hủy yêu cầu đã nhập kho");

                } else if (dataItem.status == 4) {
                    toastr.error("Không được hủy yêu cầu đã hủy");

                }
                else {
                    confirm('Hủy  bản ghi đã chọn?', function () {
                        console.log(dataItem);
                        return Restangular.all("assetManagementService" + "/removeTHVT").post(dataItem).then(function (data) {
                            toastr.success("Hủy thành công!");
                            CommonService.dismissPopup1();
                            vm.cancelAdd();
                            vm.doSearch();
                        });

                    });
                }
            });
        }
        //phuc_0706
        vm.disabled = false;
        vm.updateDSFItem = updateDSFItem;
        function updateDSFItem() {
            vm.disabled = true;
            if (vm.obj.assetManagementRequestId == null || vm.obj.assetManagementRequestId == '') {
                /**Hoangnh add - start**/
                var dataGoodCheckFromGrid = $('#DSVT').data("kendoGrid").dataSource.data();
                var grid = $("#DSVT").data("kendoGrid");
                for (var i = 0; i < dataGoodCheckFromGrid.length; i++) {
                    if(dataGoodCheckFromGrid[i].consQuantity > dataGoodCheckFromGrid[i].amountPX ) {
                        toastr.warning(CommonService.translate("Không thể nhập số lượng thu hồi lớn hơn SL hiện có"));
                        grid.editCell(grid.tbody.find('tr:eq(' + i + ')').find("td").eq(6));
                        return;
                        break;
                    }
                }
                /**Hoangnh add - end**/
                vm.obj.amrdDSVTDTOList = [];
                vm.obj.listFileTHVTTB = [];
                vm.obj.listFileTHVTTB = vm.DSFgrid.dataSource._data;
                angular.forEach(vm.DSVTGrid.dataSource.data(), function (item) {
                    if (item.consQuantity > 0) {
                        vm.obj.amrdDSVTDTOList.push(item);
                    }
                });
                if (vm.obj.amrdDSVTDTOList.length > 0 || vm.obj.amrdDSTBDTOList.length > 0) {
                    return Restangular.all("assetManagementService" + "/add").post(vm.obj).then(function (data) {
                        if (data.error) {
                            toastr.error(data.error);
                            return;
                        }
                        toastr.success("Thêm mới thành công!");
                        vm.cancelAdd();
                        vm.doSearch();
                        vm.disabled = false;
                    }, function (errResponse) {
                        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi thêm mới"));
                        vm.disabled = false;
                    });
                } else {
                    toastr.warning("Không có vật tư thiết bị");
                    vm.disabled = false;
                    return;
                }
            } else {
                var objUpdate = vm.obj;
                objUpdate.code = vm.obj.code;
                objUpdate.content = vm.obj.content;
                objUpdate.constructionId = vm.obj.constructionId;
                objUpdate.catReasonId = vm.obj.catReasonId;
                objUpdate.assetManagementRequestId = vm.obj.assetManagementRequestId;
                objUpdate.listFileTHVTTB = vm.DSFgrid.dataSource._data;
                objUpdate.amrdDSTBDTOList = vm.DSVTGrid.dataSource.data();
                var data = vm.DSTBGrid.dataSource._data;
                if (data.length == 0) {
                    objUpdate.amrdDSTBDTOList = null;
                } else {
                    objUpdate.amrdDSTBDTOList = getDSTBTask();
                }
                if (vm.obj.status == 3 || vm.obj.status == 4 || vm.obj.status == 2) {
                    toastr.error("Chỉ sửa được danh sách thu hồi ở trạng thái tạo phiếu nhập kho");
                } else {
                    return Restangular.all("assetManagementService" + "/updateHSHCItem").post(objUpdate).then(function (data) {
                        if (data.error) {
                            toastr.error("Có lỗi xảy ra");
                            return;
                        }
                        toastr.success("Chỉnh sửa thành công!");
                        vm.cancelAdd();
                        vm.doSearch();
                        vm.disabled = false;
                    }, function (error) {
                        toastr.error("Có lỗi xảy ra");
                        vm.disabled = false;
                    });
                }
            }
        }

        function getDSTBTask() {
            var list = [];
            var data = vm.DSTBGrid.dataSource._data;
            vm.DSTBGrid.table.find("tr").each(function (idx, data) {
                var row = $(data);
                var checkbox = $('[name="gridcheckbox"]', row);
                if (checkbox.is(':checked')) {
                    var tr = vm.DSTBGrid.select().closest("tr");
                    var dataItem = vm.DSTBGrid.dataItem(data);
                    dataItem.employ = 1;
                    list.push(dataItem);
                } else {
                    var tr = vm.DSTBGrid.select().closest("tr");
                    var dataItem = vm.DSTBGrid.dataItem(data);
                    dataItem.employ = 0;
                }
            });
            return list;
        }

        vm.removeDSFgridItemFile = removeDSFItemFile
        function removeDSFItemFile(e) {
            var grid = vm.DSFgrid;
            var row = $(e.target).closest("tr");
            var dataItem = grid.dataItem(row);
            grid.removeRow(dataItem); //just gives alert message
            grid.dataSource.remove(dataItem); //removes it actually from the grid
            grid.dataSource.sync();
            grid.refresh();
        }

        vm.downloadFile = function (dataItem) {
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
        }

        $scope.onSelectDSFFile = function (e) {
            if ($("#fileDSF")[0].files[0].name.split('.').pop() != 'xls' && $("#fileDSF")[0].files[0].name.split('.').pop() != 'xlsx' && $("#fileDSF")[0].files[0].name.split('.').pop() != 'doc' && $("#fileDSF")[0].files[0].name.split('.').pop() != 'docx' && $("#fileDSF")[0].files[0].name.split('.').pop() != 'pdf') {
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
            if (104857600 < $("#fileDSF")[0].files[0].size) {
                toastr.warning("Dung lượng file vượt quá 100MB! ");
                return;
            }
            var formData = new FormData();
            jQuery.each(jQuery('#fileDSF')[0].files, function (i, file) {
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
                    var dataFile = []
                    jQuery.each(jQuery('#fileDSF')[0].files, function (index, file) {
                        var obj = {};
                        obj.name = file.name;
                        obj.filePath = data[index];
                        obj.createdDate = new Date();
                        obj.createdUserName = Constant.userInfo.casUser.fullName
                        obj.appParam = {
                            code: "choose",
                            name: "---Chọn---"
                        };
                        vm.DSFgrid.dataSource.insert(0, obj);
                    });
                    vm.DSFgrid.refresh();
                    setTimeout(function () {
                        $(".k-upload-files.k-reset").find("li").remove();
                        $(".k-upload-files").remove();
                        $(".k-upload-status").remove();
                        $(".k-upload.k-header").addClass("k-upload-empty");
                        $(".k-upload-button").removeClass("k-state-focused");
                    }, 10);
                }
            });
        }


        function formatDate(date) {
            var newdate = new Date(date);
            return kendo.toString(newdate, "dd/MM/yyyy");
        }

        vm.openDepartmentTo1 = openDepartmentTo1

        function openDepartmentTo1(popUp) {
            //vm.obj={};
            vm.departmentpopUp = popUp;
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
        }
        vm.doSearch = function () {
            console.log(vm.searchForm);
            vm.retrievalGrid.dataSource.page(1);
        }

        vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'req') {
                vm.searchForm.requestGroupName = data.text;
                vm.searchForm.requestGroupId = data.id;
            } else if (vm.departmentpopUp === 'rec') {
                vm.searchForm.receiveGroupName = data.text;
                vm.searchForm.receiveGroupId = data.id;
            } else if (vm.departmentpopUp === 'reqAdd') {
                vm.obj.requestGroupName = data.text;
                vm.obj.requestGroupId = data.id;
            } else if (vm.departmentpopUp === 'recAdd') {
                vm.obj.receiveGroupName = data.text;
                vm.obj.receiveGroupId = data.id;
            } else if (vm.departmentpopUp === 'search') {
                vm.obj.receiveGroupName = data.receiveGroupName;
                vm.obj.receiveGroupId = data.receiveGroupId;
            }

        }

        vm.selectedDept1 = false;
        vm.deprtOptions1 = {
            dataTextField: "text",
            dataValueField: "id",
            placeholder: "Nhập mã hoặc tên đơn vị",
            select: function (e) {
                vm.selectedDept1 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.searchForm.requestGroupName = dataItem.text;
                vm.searchForm.requestGroupId = dataItem.id;
            },
            pageSize: 10,
            open: function (e) {
                vm.selectedDept1 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedDept1 = false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({ name: vm.searchForm.requestGroupName, pageSize: vm.deprtOptions1.pageSize }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.searchForm.requestGroupName = null;// thành name
                    vm.searchForm.requestGroupId = null;
                }
            },
            ignoreCase: false
        }

        vm.selectedDept2 = false;
        vm.deprtOptions2 = {
            dataTextField: "text",
            dataValueField: "id",
            placeholder: "Nhập mã hoặc tên đơn vị nhập",
            select: function (e) {
                vm.selectedDept2 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.searchForm.receiveGroupName = dataItem.text;
                vm.searchForm.receiveGroupId = dataItem.id;
            },
            pageSize: 10,
            open: function (e) {
                vm.selectedDept2 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedDept2 = false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({ name: vm.searchForm.receiveGroupName, pageSize: vm.deprtOptions2.pageSize }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.searchForm.receiveGroupName = null;// thành name
                    vm.searchForm.receiveGroupId = null;
                }
            },
            ignoreCase: false
        }

        vm.selectedDept3 = false;
        vm.deprtOptions3 = {
            dataTextField: "text",
            dataValueField: "id",
            select: function (e) {
                vm.selectedDept3 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.obj.receiveGroupName = dataItem.text;
                vm.obj.receiveGroupId = dataItem.id;
            },
            pageSize: 10,
            open: function (e) {
                vm.selectedDept3 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedDept3 = false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({ name: vm.obj.receiveGroupName, pageSize: vm.deprtOptions3.pageSize }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.obj.receiveGroupName = null;// thành name
                    vm.obj.receiveGroupId = null;
                }
            },
            ignoreCase: false
        }

        vm.selectedDept4 = false;
        vm.deprtOptions4 = {
            dataTextField: "text",
            dataValueFie: "id",
            select: function (e) {
                vm.selectedDept4 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.obj.requestGroupName = dataItem.text;
                vm.obj.requestGroupId = dataItem.id;

            },
            pageSize: 10,
            open: function (e) {
                vm.selectedDept4 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedDept4 = false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({ name: vm.obj.requestGroupName, pageSize: vm.deprtOptions4.pageSize }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.obj.requestGroupName = null;// thành name
                    vm.obj.requestGroupId = null;
                }
            },
            ignoreCase: false
        }

        vm.deleteListData = deleteListData;
        function deleteListData(x) {
            if (x == 1) {
                vm.searchForm.requestGroupName = null;
                vm.searchForm.requestGroupId = null;
            }
            if (x == 2) {
                vm.searchForm.listStatus = null;
            }
            if (x == 3) {
                vm.searchForm.receiveGroupName = null;// thành name
                vm.searchForm.receiveGroupId = null;
            }

        }

        function numberWithCommas(x) {
            if (x == null || x == undefined) {
                return '0';
            }
            var parts = x.toFixed(2).toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
        }

        fillDSVTTable([]);
        function fillDSVTTable() {
            vm.DSVTGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                noRecords: true,
                save: function () {
                    var grid = this;
                    setTimeout(function () {
                        grid.refresh();
                    })
                },
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: false,
                    pageSize: 10,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
                columns: [
                    {
                        title: "TT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '5%',
                        editable: false,
                        type: 'text',
                        columnMenu: false,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Mã VTTB",
                        field: 'goodsCode',
                        width: '10%',
                        type: 'text',
                        editable: false,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tên VTTB",
                        field: 'goodsName',
                        type: 'text',
                        width: '15%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                        editable: false
                    },
                    {
                        title: "Đơn vị tính",
                        field: 'goodsUnitName',
                        width: '10%',
                        type: 'text',
                        editable: false,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "SL hiện có",
                        field: 'amountReal',
                        width: '10%',
                        type: 'number',
                        editable: false,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function (data) {
                            return numberWithCommas(data.amountPX - data.amountNT - data.amountDTH);
                        }
                    },
                    {
                        title: "SL đã yêu cầu thu hồi",
                        field: 'assetQuantity',
                        width: '10%',
                        type: 'number',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: false,
                        template: function (data) {
                            if (data.amountDYCTH != null) {
                                return numberWithCommas(data.amountDYCTH);
                            } else return 0;
                        }
                    },
                    {
                        title: "SL thu hồi",
                        field: 'consQuantity',
                        width: '10%',
                        type: 'number',
                        editable: true,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:right;"
                        },
//                        template: function (data) {
//                        	debugger;
//                        	if (vm.edit1) {
//                        		if (data.consQuantity > (data.amountPX - data.amountNT - data.amountDTH) - data.amountDYCTH) {
//                                    data.consQuantity = (data.amountPX - data.amountNT - data.amountDTH) - data.amountDYCTH;
//                                    if (data.consQuantity > 0) {
//                                    	return numberWithCommas(data.consQuantity);
//                                    } else {
//                                    	data.consQuantity = 0;
//                                		return 0;
//                                    }
//                                }
//                                if (data.consQuantity < 0) {
//                                    data.consQuantity = 0;
//                                    return numberWithCommas(data.consQuantity);
//                                }
//                                if (data.consQuantity == null || data.consQuantity == undefined) {
//                                    data.consQuantity = (data.amountPX - data.amountNT - data.amountDTH) - data.amountDYCTH;
//                                    if (data.consQuantity > 0) {
//                                    	return numberWithCommas(data.consQuantity);
//                                    } else {
//                                    	data.consQuantity = 0;
//                                		return 0;
//                                    }
//                                    //SL thu hoi bang so luong hien co - so luong da y/c thu hoi
//                                } else if (data.consQuantity < 0) {
//                                    data.consQuantity = 0;
//                                    return numberWithCommas(data.consQuantity);
//                                } else {
//                                	if (data.consQuantity > 0) {
//                                    	return numberWithCommas(data.consQuantity);
//                                    } else {
//                                    	data.consQuantity = 0;
//                                		return 0;
//                                    }
//                                }
//                            } else {
//                            	if (data.consQuantity > 0) {
//                                	return numberWithCommas(data.consQuantity);
//                                } else {
//                                	data.consQuantity = 0;
//                            		return 0;
//                                }
//                            }
//                        }
                    }
                    ,{
                        title: "SL còn lại",
                        field: 'remainQuantity',
                        width: '10%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'number',
                        editable: false,
                        template: function (data) {
                            debugger;
                            //so con lai = SLhienco - SLDYCTH - SL thu hoi
                            if (((data.amountPX - data.amountNT - data.amountDTH) - data.amountDYCTH - data.consQuantity) >= 0 && (data.amountPX >= data.consQuantity)) {
                                console.log(data);
                                return numberWithCommas(data.amountPX - data.amountNT - data.amountDTH - data.consQuantity);//- data.amountDYCTH
                            } if(data.amountPX < data.consQuantity){
                                toastr.warning("Không thể nhập số lượng thu hồi lớn hơn SL hiện có");
                                return 0;
                            }else {
                                return 0;
                            }
                        }
                    }
                ]
            });
        }



        vm.edit = edit;
        function edit(dataItem) {
            vm.obj = {};
            vm.showDetail = true;
            vm.showDetailAdd = false;
            vm.mycnk = true;
            //vm.disabled = true;
            vm.edit1 = false;

            vm.viewDetail = true;

            retrievalManagementService.setItem(dataItem);
            vm.String = "Quản lý công trình > Quản lý yêu cầu thu hồi VTTB > Sửa yêu cầu thu hồi VTTB";

            Restangular.all('assetManagementService' + "/getById").post(dataItem).then(function (data) {
                refreshAllGridTab2(data);
                vm.obj = data;

                console.log(vm.obj);
                //hienvd: Start 16-03-2020
                //
                // vm.listStock = [{"defaultSortField":"name","page":null,"id":null,"pathFile":null,"lstErrorOrder":null,"filePathError":null,"text":null,"isSize":false,"pageSize":null,"keySearch":null,"keySearchAction":null,"keySearch2":null,"totalRecord":null,"stockTransId":null,"orderId":null,"orderCode":null,"loginName":null,"code":null,"type":null,"stockId":data.catStockId,"status":null,"signState":null,"fromStockTransId":null,"description":null,"createdBy":null,"createdByName":null,"stockReceiveCode":null,"createdDate":null,"createdDeptId":null,"createdDeptName":null,"updatedBy":null,"updatedDate":null,"realIeTransDate":null,"realIeUserId":null,"realIeUserName":null,"shipperId":null,"shipperName":null,"cancelDate":null,"cancelBy":null,"cancelByName":null,"cancelReasonName":null,"cancelDescription":null,"vofficeTransactionCode":null,"shipmentCode":null,"contractCode":null,"projectCode":null,"cusId":null,"stockName":data.catName,"bussinessType":null,"goodsId":null,"serial":null,"in_roal":null,"deptReceiveId":null,"stockReceiveId":null,"deptReceiveName":null,"orderCodeSearch":null,"stockCode":data.catCode,"startDate":null,"endDate":null,"bussinessTypeName":null,"createrNoteSearch":null,"listNoteStatus":[],"listSignCAState":[],"listStatus":[],"listSignState":[],"createdDateFrom":null,"createdDateTo":null,"orderDTO":null,"orderGoodsDTO":null,"orderGoodsDetailDTO":null,"stockTransDetailDTO":null,"stockTransDetailSerialDTO":null,"listStockTransDetailDTO":null,"listStockTransDetailSerialDTO":null,"listOrderGoodsDetailDTO":null,"listOrderGoodsDTO":null,"listStockId":null,"shipmentId":null,"countSerial":null,"countSerialDetail":null,"receiverId":null,"receiverName":null,"fwmodelId":null}];
                vm.listStock = [{"stockId":data.catStockId,"stockName":data.catName,"stockCode":data.catCode}];

                vm.stockId = data.catStockId;
                vm.obj.stockId = data.catStockId;
                vm.obj.stockCode = data.catCode;
                vm.obj.stockName = data.catName;

                // hienvd: End 16-03-2020

                // if(vm.obj.constructionId && vm.obj.constructionId != ''){
                //     Restangular.all('assetManagementService' + "/getListStockByConstructionIdAndCodeStock").post(vm.obj).then(function (dataSearch) {
                //         // console.log(dataSearch);
                //         vm.listStock = dataSearch;
                //     }, function (error) {
                //         toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
                //     });
                // }

            }, function (error) {
                toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
            });


        }
        function refreshAllGridTab2(data) {
            var DSFgrid = vm.DSFgrid;
            var DSVTGrid = vm.DSVTGrid;
            if (DSFgrid) {
                DSFgrid.dataSource.data(data.listFileTHVTTB);
                DSFgrid.refresh();
            }
        }
        vm.save = function () {
            if (validateYearDetail()) {
                var data = populateDataToSave();
                if (data.yearPlanId == null || data.yearPlanId == '') {
                    yearPlanService.createYearPlan(data).then(function (result) {
                        if (result.error) {
                            $('#year').focus();
                            toastr.error(result.error);
                            return;
                        }
                        toastr.success("Thêm mới thành công!");
                        vm.cancel();
                    }, function (errResponse) {
                        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi thêm mới"));
                    });
                }
                else {
                    yearPlanService.updateYearPlan(data).then(function (result) {
                        if (result.error) {
                            $('#year').focus();
                            toastr.error(result.error);
                            return;
                        }
                        toastr.success("Chỉnh sửa thành công!");
                        vm.cancel();
                    }, function (errResponse) {
                        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi chỉnh sửa"));
                    });
                }
            }
        }
        function populateDataToSave() {
            var data = vm.yearPlan;
            var detailList = []
            var yearPlanDetailGrid = vm.yearPlanDetailGrid;
            if (yearPlanDetailGrid != undefined && yearPlanDetailGrid.dataSource != undefined)
                data.detailList = yearPlanDetailGrid.dataSource._data;
            return data;
        }
        // clear data
        vm.selectedDeptChoose = false;
        vm.changeDataAuto = changeDataAuto
        function changeDataAuto(id) {
            switch (id) {
                case 'req':
                {
                    if (processSearch(id, vm.selectedDept1)) {

                        vm.searchForm.requestGroupName = null;
                        vm.searchForm.requestGroupCode = null;
                        vm.searchForm.requestGroupId = null;
                        vm.selectedDept1 = false;
                    }
                    break;
                }
                case 'rec':
                {
                    if (processSearch(id, vm.selectedDept2)) {

                        vm.searchForm.receiveGroupName = null;
                        vm.searchForm.receiveGroupCode = null;
                        vm.searchForm.receiveGroupId = null;
                        vm.selectedDept2 = false;
                    }
                    break;
                }
            }
        }
        vm.changeDataDept = changeDataDept
        function changeDataDept(id) {
            switch (id) {
                case 'dept':
                {
                    if (processSearch(id, vm.selectedDept11)) {
                        vm.obj.constructionName = null;
                        vm.obj.constructionCode = null;
                        vm.obj.constructionId = null;
                        vm.selectedDept11 = false;
                    }
                    break;
                }
                case 'dept4':
                {
                    if (processSearch(id, vm.selectedDept4)) {

                        vm.obj.requestGroupName = null;
                        vm.obj.requestGroupCode = null;
                        vm.obj.requestGroupId = null;
                        vm.selectedDept4 = false;
                    }
                    break;
                }
                case 'dept3':
                {
                    if (processSearch(id, vm.selectedDept3)) {

                        vm.obj.receiveGroupName = null;
                        vm.obj.receiveGroupCode = null;
                        vm.obj.receiveGroupId = null;
                        vm.selectedDept3 = false;

                    }
                    break;
                }
            }
        };

        //hienvd5: Comment 10032020
        vm.DSTBGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            noRecords: true,
            columnMenu: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            pageable: {
                refresh: false,
                pageSize: 10,
                pageSizes: [5, 10, 15, 20, 25],
                messages: {
                    display: "{0}-{1} của {2} kết quả",
                    itemsPerPage: "kết quả/trang",
                    empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                }
            },
            columns: [
                {
                    title: "TT",
                    field: "stt",
                    template: function () {
                        return ++record;
                    },
                    width: '5%',
                    columnMenu: false,
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                {
                    title: "Mã VTTB",
                    field: 'goodsCode',
                    width: '10%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Tên VTTB",
                    field: 'goodsName',
                    width: '15%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Đơn vị tính",
                    field: 'goodsUnitName',
                    width: '10%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Serial",
                    field: 'serial',
                    width: '10%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Thu hồi",
                    width: '10%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        if (vm.showDetailAdd) {
                            return "<input type='checkbox' name='gridcheckbox' ng-click='vm.handleCheck(dataItem)' ng-model='dataItem.selected'/>";
                        } else {
                            if (dataItem.consQuantity === 1) {
                                return "<input type='checkbox' name='gridcheckbox' ng-checked='true'/>"
                            }
                            else if (dataItem.consQuantity === 2) {
                                return "<input type='checkbox' name='gridcheckbox' ng-checked='true'/>"
                            }
                            else if (dataItem.consQuantity === 0) {
                                return "<input type='checkbox' name='gridcheckbox'  />"
                            }
                            else if (dataItem.consQuantity === null) {
                                return "<input type='checkbox' name='gridcheckbox' ng-checked='true'/>"
                            }
                            else {
                                return "<input type='checkbox' name='gridcheckbox'  />"
                            }
                        }
                    }
                },
                {
                    field: 'consQuantity',
                    hidden: true
                },
                {
                    field: 'remainQuantity',
                    hidden: true
                },
                {
                    field: 'goodsIsSerial',
                    hidden: true
                },
                {
                    field: 'merEntityId',
                    hidden: true
                },
                {
                    field: 'goodsId',
                    hidden: true
                }
            ]
        });
        //hienvd5: Comment 10032020

        //hienvd5: Comment 10032020
        vm.detailDSVTTGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            reorderable: true,
            noRecords: true,
            save: function () {
                var grid = this;
                setTimeout(function () {
                    grid.refresh();
                })
            },
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            pageable: {
                refresh: false,
                pageSize: 10,
                pageSizes: [10, 15, 20, 25],
                messages: {
                    display: "{0}-{1} của {2} kết quả",
                    itemsPerPage: "kết quả/trang",
                    empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                }
            },
            columns: [
                {
                    title: "TT",
                    field: "stt",
                    template: function () {
                        return ++record;
                    },
                    width: '5%',
                    editable: false,
                    type: 'text',
                    columnMenu: false,
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                {
                    title: "Mã VTTB",
                    field: 'goodsCode',
                    width: '10%',
                    type: 'text',
                    editable: false,
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Tên VTTB",
                    field: 'goodsName',
                    type: 'text',
                    width: '15%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false
                },
                {
                    title: "Đơn vị tính",
                    field: 'goodsUnitName',
                    width: '10%',
                    type: 'text',
                    editable: false,
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "SL thu hồi",
                    field: 'consQuantity',
                    width: '10%',
                    type: 'number',
                    editable: false,
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:right;"
                    },
                    template: function (data) {
                        debugger;
                        if (vm.edit1) {
                            if (data.consQuantity > (data.amountPX - data.amountNT - data.amountDTH) - data.amountDYCTH) {
                                data.consQuantity = (data.amountPX - data.amountNT - data.amountDTH) - data.amountDYCTH;
                                if (data.consQuantity > 0) {
                                    return numberWithCommas(data.consQuantity);
                                } else {
                                    data.consQuantity = 0;
                                    return 0;
                                }
                            }
                            if (data.consQuantity < 0) {
                                data.consQuantity = 0;
                                return numberWithCommas(data.consQuantity);
                            }
                            if (data.consQuantity == null || data.consQuantity == undefined) {
                                data.consQuantity = (data.amountPX - data.amountNT - data.amountDTH) - data.amountDYCTH;
                                if (data.consQuantity > 0) {
                                    return numberWithCommas(data.consQuantity);
                                } else {
                                    data.consQuantity = 0;
                                    return 0;
                                }
                                //SL thu hoi bang so luong hien co - so luong da y/c thu hoi
                            } else if (data.consQuantity < 0) {
                                data.consQuantity = 0;
                                return numberWithCommas(data.consQuantity);
                            } else {
                                if (data.consQuantity > 0) {
                                    return numberWithCommas(data.consQuantity);
                                } else {
                                    data.consQuantity = 0;
                                    return 0;
                                }
                            }
                        } else {
                            if (data.consQuantity > 0) {
                                return numberWithCommas(data.consQuantity);
                            } else {
                                data.consQuantity = 0;
                                return 0;
                            }
                        }
                    }
                }
            ]
        });

        //hienvd5: Comment 10032020

        vm.handleCheck = handleCheck;
        function handleCheck(dataItem) {
            if (dataItem.selected) {
                vm.obj.amrdDSTBDTOList.push(dataItem);
            } else {
                for (var i = 0; i < vm.obj.amrdDSTBDTOList.length; i++) {
                    if (vm.obj.amrdDSTBDTOList[i].merentityId === dataItem.merentityId) {
                        vm.obj.amrdDSTBDTOList.splice(i, 1);
                    }
                }
            }
        }

        vm.openCatProvincePopup = openCatProvincePopup;
        vm.onSaveCatProvince = onSaveCatProvince;
        vm.clearProvince = clearProvince;
        function openCatProvincePopup() {
            var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm tỉnh");
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions, data, vm, 'fff', 'ggfd', false, '85%', '85%', 'catProvinceSearchController');
        }
        function onSaveCatProvince(data) {
            vm.searchForm.catProvinceId = data.catProvinceId;
            vm.searchForm.catProvinceCode = data.code;
            vm.searchForm.catProvinceName = data.name;
            htmlCommonService.dismissPopup();
            $("#provincename").focus();
        };
        function clearProvince() {
            vm.searchForm.catProvinceId = null;
            vm.searchForm.catProvinceCode = null;
            vm.searchForm.catProvinceName = null;
            $("#provincename").focus();
        }
        vm.provinceOptions = {
            dataTextField: "name",
            dataValueField: "id",
            placeholder: "Nhập mã hoặc tên tỉnh",
            select: function (e) {
                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.searchForm.catProvinceId = dataItem.catProvinceId;
                vm.searchForm.catProvinceCode = dataItem.code;
                vm.searchForm.catProvinceName = dataItem.name;
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
                            name: vm.searchForm.catProvinceName,
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
                    vm.searchForm.catProvinceId = null;
                    vm.searchForm.catProvinceCode = null;
                    vm.searchForm.catProvinceName = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.searchForm.catProvinceId = null;
                    vm.searchForm.catProvinceCode = null;
                    vm.searchForm.catProvinceName = null;
                }
            }
        }


        vm.patternSignerOptions={
            dataTextField: "fullName",
            placeholder:"Nhập mã hoặc tên người thực hiện",
            open: function(e) {
                vm.isSelect = false;
                vm.selectedDept1 = false;
            },
            select: function(e) {
                vm.isSelect = true;
                vm.selectedDept1 = true;
                data = this.dataItem(e.item.index());
                vm.searchForm.userName =data.fullName;
                vm.searchForm.sysUserId = data.sysUserId;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function(options) {
                        vm.selectedDept1 = false;
                        return Restangular.all("sysUserCOMSRsService/getForAutoCompleteInSign").post({pageSize:10, page:1, fullName:$("#signerGroup").val().trim()}).then(function(response){
                            options.success(response);
                            if(response == [] || $("#signerGroup").val().trim() == ""){
                                vm.searchForm.userName = null;
                                vm.searchForm.sysUserId = null;
                            }
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-6 text-header-auto border-right-ccc">Mã nhân viên</p>' +
                '<p class="col-md-6 text-header-auto">Họ tên</p>' +
                '</div>',
            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.employeeCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
            change: function(e) {
                if (e.sender.value() === '') {
                    vm.searchForm.userName = null;
                    vm.searchForm.sysUserId = null;
                }
            },
            close: function(e) {
            }
        };

        vm.openUser = function openUser(){
            var templateUrl = 'coms/popup/sysUserSearchPopUp.html';
            var title = CommonService.translate(gettextCatalog.getString("Tìm kiếm nhân viên"));
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','sysUserSearchController');

        }
        vm.onSaveSysUser = function onSaveSysUser(data){
            vm.searchForm.userName = data.fullName;
            vm.searchForm.sysUserId = data.sysUserId;
            htmlCommonService.dismissPopup();
            $("#signerGroup").focus();
        };

        vm.clearUser = function() {
            vm.searchForm.userName = null;
            vm.searchForm.sysUserId = null;
        }


        vm.cancelInput = function(data){
            if(data=='date'){
                vm.searchForm.startDate = null;
            }
        }


    }
})();
