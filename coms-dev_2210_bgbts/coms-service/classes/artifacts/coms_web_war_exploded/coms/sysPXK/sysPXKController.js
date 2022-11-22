(function () {
    'use strict';
    var controllerId = 'rpCouponExportController';

    angular.module('MetronicApp').controller(controllerId, rpCouponExportController);

    function rpCouponExportController($scope, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, rpCouponExportService, rpQuantityService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, htmlCommonService, $http) {
        var vm = this;
        vm.showSearch = true;
        vm.isCreateNew = false;
        vm.showDetail = false;
//        vm.cancelListYear= function()
//        {
//            vm.workItemSearch.yearList = [];
//        }
        $scope.listCheck = [];
        vm.workItemSearch = {};
        vm.listDataObj = [];
        vm.String = "Quản lý công trình > Báo cáo > Báo cáo PXK A quá hạn 7 ngày";
        vm.workItemSearch.dateFrom = kendo.toString(new Date((new Date()).getTime() - 30 * 24 * 3600 * 1000), "dd/MM/yyyy");
        vm.workItem = {};
        vm.changeImage = changeImage;
        vm.imageSelected = {};


        // Khoi tao du lieu cho form
        initFormData();

        function initFormData() {
//         hungnx 270718 start init date in month
            var date = new Date();
            var start = new Date(date.getFullYear(), date.getMonth(), 1);
            var end = new Date(date.getFullYear(), date.getMonth() + 1, 0);
            vm.workItemSearch.dateTo = htmlCommonService.formatDate(end);
            // vm.workItemSearch.dateTo = null;
            // vm.workItemSearch.realIeTransDate = htmlCommonService.formatDate(start);
//         hungnx 270718 end
            fillDataTable([]);
            initDropDownList();
            initMonthYear();
        }

        function initMonthYear() {

            $scope.monthSelectorOptions = {
                start: "year",
                depth: "year"
            };

        }

        function initDropDownList() {
            vm.yearDataList = [];
            vm.monthDataList = [];
            var currentYear = (new Date()).getFullYear();
            for (var i = currentYear - 2; i < currentYear + 19; i++) {
                vm.yearDataList.push({
                    id: i,
                    name: i

                })
            }

            for (var i = 1; i < 13; i++) {
                vm.monthDataList.push({

                    id: i,
                    name: i + '/' + (currentYear)

                })
            }
            vm.yearDownListOptions = {
                dataSource: vm.yearDataList,
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true
            }
            vm.monthDownListOptions = {
                dataSource: vm.monthDataList,
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true
            }
        }

        vm.d = {}
        vm.validatorOptions = kendoConfig.get('validatorOptions');
        vm.formatAction = function (dataItem) {
            var template =
                '<div class="text-center #=workItemId#"">'
            template += '<button type="button"' +
                'class="btn btn-default padding-button box-shadow  #=workItemId#"' +
                'disble="" ng-click=vm.edit(#=workItemId#)>' +
                '<div class="action-button edit" uib-tooltip="Sửa" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
                '</button>' +
                '<button type="button"' +
                'class="btn btn-default padding-button box-shadow #=workItemId#"' +
                'ng-click=vm.send(#=workItemId#)>' +
                '<div class="action-button export" uib-tooltip="Gửi tài chính" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
                '</button>' +
                '<button type="button"' +
                'class="btn btn-default padding-button box-shadow #=workItemId#"' +
                'ng-click=vm.remove(#=workItemId#)>' +
                '<div class="action-button del" uib-tooltip="Xóa" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
                '</button>'
                +
                '<button type="button" class="btn btn-default padding-button box-shadow #=workItemId#"' +
                'ng-click=vm.cancelUpgradeLta(#=workItemId#)>' +
                '<div class="action-button cancelUpgrade" uib-tooltip="Hủy nâng cấp" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
                '</button>';
            template += '</div>';
            return dataItem.groupId;
        }
        setTimeout(function () {
            $("#keySearch").focus();
        }, 15);
        /*
         * setTimeout(function(){ $("#appIds1").focus(); },15);
         */
        var record = 0;

        function fillDataTable(data) {
            vm.workItemGridOptionsSeven = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save: function () {
                    vm.workItemGridSevenHienFixPXK.refresh();
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: [
                    {
                        name: "actions",
                        template:
                            '<div class="btn-group pull-right margin_top_button ">'+
                            '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                            //                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-disabled="vm.disableExportExcel" ng-click="vm.exportFileCons()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                            '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">' +
                            //'<label ng-repeat="column in vm.workItemGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                            '<label ng-repeat="column in vm.workItemGridSevenHienFixPXK.columns.slice(1,vm.workItemGridSevenHienFixPXK.columns.length)| filter: vm.gridColumnShowHideFilter">' +

                            '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}' +
                            '</label>' +
                            '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#appCountWorkItemSeven").text("" + response.total);
                            vm.countWorkItemSeven = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "rpQuantityService/doSearchSysPXK",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            // vm.constructionSearch.employeeId =
                            // Constant.user.srvUser.catEmployeeId;
                            vm.workItemSearch.page = options.page;
                            vm.workItemSearch.pageSize = options.pageSize;
                            return JSON.stringify(vm.workItemSearch)

                        }
                    },
                    pageSize: 10,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                edit: function (e) {

                    if (e.model.statusConstruction == "6" || e.model.statusConstruction == "7" || e.model.statusConstruction == "8") {

                        e.sender.closeCell();

                    }

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
                edit: function (dataItem) {
                    dataItem.model.oldQuantity = dataItem.model.quantity;
                },
                columns: [
                    {
                        title: "TT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '50px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',
                    }, {
                        title: "Kho xuất",
                        field: 'stockCode',
                        width: '130px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    }, {
                        title: "Mã phiếu xuất",
                        field: 'code',
                        width: '200px',
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function (dataItem) {
                            return "<a href='#' ng-click='vm.viewDetailPXK(dataItem)'>" + dataItem.code + "</a>";
                        }

                    }, {
                        title: "Ngày thực xuất",
                        width: '130px',
                        field: 'realIeTransDate',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',

                    }, {
                        title: "Mã hợp đồng",
                        width: '200px',
                        field: 'cntContractCode',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',

                    }, {
                        title: "Mã công trình",
                        width: '200px',
                        field: 'constructionCode',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',

                    }, {
                        title: "Đơn vị nhận",
                        width: '300px',
                        field: 'name',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',

                    }, {
                        title: "Ngày quá hạn",
                        field: 'daysOverdue',
                        width: '130px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    }, {
                        title: "Người nhận",
                        field: 'fullName',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },

                    }, {
                        title: "Email người nhận",
                        field: 'email',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    }, {
                        title: "SDT Người nhận",
                        field: 'phoneNumber',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    }, {
                        title: "Trạng thái",
                        field: 'comfirmExcel',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',
                        editable: false,
                        template: "# if(comfirm == 0 ){ #" + "#= 'Chờ xác nhận' #" + "# } " + "else if (comfirm == 1) { # " + "#= 'Đã xác nhận' #" + "#}" + "else if (comfirm == 2) { # " + "#= 'Đã từ chối' #" + "#} #"
                    },
                ]
            });
        }

        function quantityEdited(dataItem) {
            dataItem.isQuantityEdited = 1;
        }

        vm.disableExportExcel = false;
        //HuyPQ-25/08/2018-edit-start
        vm.exportWorkItemFile = function () {
            function displayLoading(target) {
                var element = $(target);
                kendo.ui.progress(element, true);
                setTimeout(function () {
                    if (vm.disableExportExcel)
                        return;
                    vm.disableExportExcel = true;
                    return Restangular.all("rpQuantityService/exportWorkItemServiceTask").post(vm.workItemSearch).then(function (d) {
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

            displayLoading("#workItemGridSevenHienFixPXK");
        }
//HuyPQ-edit-end

        vm.import = function () {
            var teamplateUrl = "coms/workItem/workItemPopup.html";
            var title = "Import từ file excel";
            var windowId = "YEAR_PLAN";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '700', 'auto', "files");
        }


        function refreshGrid(d) {
            var grid = vm.workItemGridSevenHienFixPXK;
            if (grid) {
                grid.dataSource.data(d);
                grid.refresh();
            }
        }


        vm.cancel = cancel;

        function cancel() {
            vm.showDetail = false;
            vm.workItemSearch = {}
            vm.workItem = {};
            vm.doSearchHSHC();
        }

        function getConstructionTask() {
            var list = [];
            var data = vm.workItemGridSevenHienFixPXK.dataSource._data;
            vm.workItemGridSevenHienFixPXK.table.find("tr").each(function (idx, data) {
                var row = $(data);
                var checkbox = $('[name="gridcheckbox"]', row);
                if (checkbox.is(':checked')) {
                    var tr = vm.workItemGridSevenHienFixPXK.select().closest("tr");
                    var dataItem = vm.workItemGridSevenHienFixPXK.dataItem(data);
                    list.push(dataItem.workItemId);
                }
            });
            return list;
        }

        function numberWithCommas(x) {
            if (x == null || x == undefined) {
                return '0';
            }
            var parts = x.toFixed(2).toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
        }

        vm.cancelConfirmPopup = cancelConfirmPopup;

        function cancelConfirmPopup() {
            CommonService.dismissPopup1();
            //vm.showDetail=false;
        }


        vm.cancelDoSearch = function () {
            vm.showDetail = false;
            vm.workItemSearch = {
                status: "1"
            };
            doSearchHSHC();
        }

        vm.doSearchHSHC = doSearchHSHC;

        function doSearchHSHC() {
            vm.showDetail = false;
            var grid = vm.workItemGridSevenHienFixPXK;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        }


        vm.showHideWorkItemColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.workItemGridSevenHienFixPXK.hideColumn(column);
            } else if (column.hidden) {
                vm.workItemGridSevenHienFixPXK.showColumn(column);
            } else {
                vm.workItemGridSevenHienFixPXK.hideColumn(column);
            }


        }
        /*
         * * Filter các cột của select
         */

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };


        vm.exportpdf = function () {
            var obj = {};
            workItemService.exportpdf(obj);
        }

        vm.errNumber = "";
        vm.checkNumber = checkNumber;

        function checkNumber() {
            var val = $('#parOder').val();
            if (val === 0) {
                if (val === 0) {
                    if (val === "") {
                        vm.errNumber = "";
                    } else {
                        vm.errNumber = "Phải nhập kiểu số nguyên từ 1-99";
                        return false;
                    }

                }
            } else {
                var isNaN = function (val) {
                    if (Number.isNaN(Number(val))) {
                        return false;
                    }
                    return true;
                }
                if (isNaN(val) === false) {
                    vm.errNumber = "Phải nhập kiểu số nguyên từ 1-99";
                } else {
                    vm.errNumber = "";
                }

            }
        }

        vm.handleCheck = function(dataItem) {
            var index = indexInArraySst(vm.listChecked, dataItem);
            if (index === -1) {
                // vm.listChecked.push(dataItem);
                vm.listChecked.push(dataItem);
            } else {
                vm.listChecked.splice(index, 1);
            }
            console.log(vm.listChecked);
        };

        function indexInArraySst(arr, item) {
            for (var i = 0; i < arr.length; i++) {
                if (arr[i].synStockTransId === item.synStockTransId) {
                    return i;
                }
            }
            return -1;
        }

        vm.selectAll = function() {
            var dataList = $('#synStockTransGrid').data("kendoGrid").dataSource.data();
            var index;
            for (var i = 0; i < dataList.length; i++) {
                index = indexInArraySst(vm.listChecked, dataList[i]);
                if (index !== -1) {
                    vm.listChecked.splice(index, 1);
                }
                dataList[i].checked = vm.checkedAll;
                if (vm.checkedAll) {
                    vm.listChecked.push(dataList[i]);
                }
            }
        };

        // ----- viewDetail start
        vm.synStockTransView = true;
        covenantSysPXKFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
            kendoConfig, $kWindow, rpCouponExportService,
            CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm, $http);

        vm.viewDetailPXK = viewDetailPXK;

        function viewDetailPXK(dataItem) {
            if (!!!dataItem.buss) {
                dataItem.buss = dataItem.bussinessTypeName;
            }
            vm.detaillPXK(dataItem);
        }

        // viewDetail end -----

        vm.openDepartmentTo = openDepartmentTo;

        function openDepartmentTo(popUp) {
            vm.obj = {};
            vm.departmentpopUp = popUp;
            var templateUrl = 'coms/RPConstructionDK/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
        }

        vm.couponPopup = function (popUp) {
            vm.obj = {};
            vm.listDataObj = [];
            vm.departmentpopUp = popUp;
            var templateUrl = 'coms/rpCouponExport/couponPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm Người nhận");
            CommonService.populatePopupcoupon(templateUrl, title, null, null, vm, popUp, 'string', false, '80%', '70%');
//    htmlCommonService.populatePopup(templateUrl, title, null, null, vm, 'fff', 'string', false, '80%', '70%','populatePopupcouponController');
        }
        vm.openCatStationPopup = function () {
            var templateUrl = 'coms/popup/catStationSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm mã trạm");
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions, data, vm, 'fff', 'ggfd', false, '85%', '85%', 'catStationSearchController');
        }
        vm.constructionOptions = {
            dataTextField: "code",
            dataValueField: "constructionId",
            placeholder: "Nhập mã hoặc tên công trình",
            select: function (e) {
                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.workItemSearch.constructionName = dataItem.code;
                vm.workItemSearch.constructionId = dataItem.constructionId;
            },
            open: function (e) {
                vm.isSelect = false;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.isSelect = false;
                        return Restangular.all("constructionTaskService/rpDailyTaskConstruction").post({
                            keySearch: vm.rpSumTaskSearch.constructionName,
                            catConstructionTypeId: vm.rpSumTaskSearch.catConstructionTypeId,
                            pageSize: vm.constructionOptions.pageSize,
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
                '<p class="col-md-6 text-header-auto border-right-ccc">Mã công trình</p>' +
                '<p class="col-md-6 text-header-auto">Tên công trình</p>' +
                '</div>',
            template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.code #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (!vm.isSelect) {
                    vm.workItemSearch.constructionName = null;
                    vm.workItemSearch.constructionId = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.workItemSearch.constructionName = null;
                    vm.workItemSearch.constructionId = null;
                }
            }
        }
        vm.patternContractOutOptions = {
            dataTextField: "code", placeholder: "Mã hợp đồng",
            open: function (e) {
                vm.isSelect = false;

            },
            select: function (e) {
                vm.isSelect = true;
                data = this.dataItem(e.item.index());
                vm.workItemSearch.cntContractCode = data.code;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var objSearch = {isSize: true, code: $('#cntContractOut').val(), contractType: 0};
                        return Restangular.all(RestEndpoint.CNT_CONTRACT_SERVICE_URL + "/cntContract/doSearch").post(objSearch).then(function (response) {
                            options.success(response.data);
//                        var check = response == [] || $("#"+proccessFormId("name")).val().trim() == "";
//                        if(response == [] || $("#"+proccessFormId("name")).val().trim() == ""){
//                          vm.cntContractMap.cntContractId =null;
//                        }
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-12 text-header-auto">Mã hợp đồng</p>' +

                '</div>',
            template: '<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.code #</div></div>',
            change: function (e) {
                if (!vm.isSelect) {
                    vm.workItemSearch.cntContractCode = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.workItemSearch.cntContractCode = null;
                }
            }
//         if(vm.isSelect) {
//            if((vm.searchForm.cntContractCode != data.code)) {
//               $('#'+proccessFormId("cntContractOut")).val(null);
//               vm.searchForm.cntContractCode = null;
//            }
//               if(vm.searchForm.cntContractCode != data.code){
//                  $('#'+proccessFormId("cntContractOut")).val(null);
//                  vm.searchForm.cntContractCode  = null;
//               }
//         }
//        if(!vm.isSelect) {
//           vm.searchForm.cntContractCode = null;
//           $('#'+proccessFormId("cntContractOut")).val(null);
//        }
//        close: function(e) {
//            // handle the event0
//          }
        };
        vm.stationCodeOptions = {
            dataTextField: "email",
            dataValueField: "employeeCode",
            placeholder: "Nhập vào Email",
            select: function (e) {

                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.workItemSearch.email = dataItem.email;
//            vm.workItemSearch.email = dataItem.email;
            },
            open: function (e) {
                vm.isSelect = false;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        debugger;
                        vm.isSelect = false;
                        return Restangular.all("rpQuantityService/doSearchSysPXK").post({
                            email: vm.workItemSearch.email,
                            pageSize: vm.stationCodeOptions.pageSize,
                            page: 1
                        }).then(function (response) {
                            options.success(response.data);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-left k-widget k-header">' +
                '<p class="col-md-6 text-header-auto">Mã NV</p>' +
                '<p class="col-md-6 text-header-auto">Email</p>' +
                '</div>',
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.employeeCode #</div><div  style="padding-right: 5px;width:auto;overflow: hidden"> #: data.email #</div> </div>',
            change: function (e) {
                if (!vm.isSelect) {
                    vm.workItemSearch.fullName = null;
                    vm.workItemSearch.email = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.workItemSearch.fullName = null;
                    vm.workItemSearch.email = null;
                }
            }
        }
        vm.onSave = onSave;

        function onSave(data) {
            debugger;
            if (vm.departmentpopUp === 'dept') {
                vm.workItemSearch.sysGroupName = data.text;
                vm.workItemSearch.sysGroupId = data.id;
            }

        }

        function save(dataItem) {
            debugger;
            if (dataItem) {
                caller.onSaveContract(dataItem);
                htmlCommonService.dismissPopup();
            } else {
                if ($scope.dataItem) {
                    caller.onSave($scope.dataItem, $scope.popupId);
                    htmlCommonService.dismissPopup();
                } else {
                    if ($scope.parent) {
                        caller.onSave($scope.parent);
                        htmlCommonService.dismissPopup();
                    } else {
                        if (confirm('Chưa chọn bản ghi nào!')) {
                            htmlCommonService.dismissPopup();
                        }
                    }
                }
            }

        }

        vm.onSaveContract = function (data) {
            vm.workItemSearch.constructiontypename = data.code;
        }

        vm.onSaveStationCode = onSaveStationCode;

        function onSaveStationCode(data) {
            debugger;
//     if (vm.departmentpopUp === 'dept') {
            vm.workItemSearch.email = data.email;
            vm.workItemSearch.employeeCode = data.email;
//    CommonService.dismissPopup1();
// }
        }

// clear data
        vm.changeDataAuto = changeDataAuto

        function changeDataAuto(id) {
            switch (id) {
                case 'dept': {
                    if (processSearch(id, vm.selectedDept1)) {
                        vm.workItemSearch.sysGroupId = null;
                        vm.workItemSearch.sysGroupName = null;
                        vm.selectedDept1 = false;
                    }
                    break;
                }
            }
        }

        function checkErrDate() {
            var realIeTransDate = $('#realIeTransDate').val();
            vm.errMessage = '';
            vm.errMessage1 = '';
            var curDate = new Date();
            if (realIeTransDate == "") {
                vm.errMessage1 = CommonService.translate('Ngày tạo không được để trống');
                $("#realIeTransDate").focus();
                vm.checkDateFrom = false;
                return vm.errMessage1;
            } else if (kendo.parseDate(realIeTransDate, "dd/MM/yyyy") == null) {
                vm.errMessage1 = CommonService.translate('Ngày tạo không hợp lệ');
                $("#realIeTransDate").focus();
                vm.checkDateFrom = false;
                return vm.errMessage1;
            } else if (kendo.parseDate(realIeTransDate, "dd/MM/yyyy").getFullYear() > 9999 || kendo.parseDate(startDate, "dd/MM/yyyy").getFullYear() < 1000) {
                vm.errMessage1 = CommonService.translate('Ngày tạo không hợp lệ');
                $("#realIeTransDate").focus();
                vm.checkDateFrom = false;
                return vm.errMessage1;
            }
        }

        vm.cancelInput = function (param) {
            if (param == 'dept') {
                vm.workItemSearch.sysGroupId = null;
                vm.workItemSearch.sysGroupName = null;
            }
            if (param == 'status') {
                vm.workItemSearch.listConfirm = []
            }
        }
        vm.cancelListYear = cancelListYear;

        function cancelListYear() {
//    vm.workItemSearch.monthYear = null;
// hungnx 20170703 start
            vm.workItemSearch.dateTo = null;
            vm.workItemSearch.dateFrom = null;
// hungnx 20170703 end
        }

// 8.2 Search SysGroup
        vm.selectedDept1 = false;
        vm.deprtOptions1 = {
            dataTextField: "text",
            dataValueField: "id",
            placeholder: "Nhập mã hoặc tên đơn vị",
            select: function (e) {
                vm.selectedDept1 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.workItemSearch.sysGroupName = dataItem.text;
                vm.workItemSearch.sysGroupId = dataItem.id;
            },
            pageSize: 10,
            open: function (e) {
                vm.selectedDept1 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        debugger;
                        vm.selectedDept1 = false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({
                            name: vm.workItemSearch.sysGroupName,
                            pageSize: vm.deprtOptions1.pageSize
                        }).then(function (response) {
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
                    vm.workItemSearch.sysGroupName = null;// thành name
                    vm.workItemSearch.sysGroupId = null;
                }
            },
            ignoreCase: false
        }

        vm.getExcelTemplate = function () {
            workItemService.downloadTemplate({}).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                return;
            });
        }

        vm.cancelImport = cancelImport;

        function cancelImport() {
            CommonService.dismissPopup1();
        }

        vm.downloadFile = downloadFile;

        function downloadFile(path) {
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + path;
        }

        vm.removeImage = removeImage

        function removeImage(image, list) {
            list.splice(list.indexOf(image), 1);
        }

        function changeImage(image) {
            vm.imageSelected = image;
        }

        vm.openCatProvincePopup = openCatProvincePopup;
        vm.onSaveCatProvince = onSaveCatProvince;
        vm.clearProvince = clearProvince;
        vm.clearStationCode = clearStationCode;
        vm.clearConstructionType = clearConstructionType;

        function openCatProvincePopup() {
            var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm tỉnh");
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions, data, vm, 'fff', 'ggfd', false, '85%', '85%', 'catProvinceSearchController');
        }

        function onSaveCatProvince(data) {
            vm.workItemSearch.catProvinceId = data.catProvinceId;
            vm.workItemSearch.catProvinceCode = data.code;
            vm.workItemSearch.catProvinceName = data.name;
            htmlCommonService.dismissPopup();
            $("#provincename").focus();
        };

        function clearProvince() {
            vm.workItemSearch.catProvinceId = null;
            vm.workItemSearch.catProvinceCode = null;
            vm.workItemSearch.catProvinceName = null;
            $("#provincename").focus();
        }

        function clearStationCode() {
            vm.workItemSearch.email = null;
            vm.workItemSearch.employeeCode = null;
            $("#fullName").focus();
        }

        function clearConstructionType() {
            vm.workItemSearch.constructiontypename = null;
            vm.workItemSearch.catConstructionTypeId = null;
            vm.workItemSearch.cntContractCode = null;
            $("#constructionType").focus();
        }
        //hienvd: Excel
        vm.exportFileCons = function exportFile() {
            // vm.workItemSearch.page = null;
            // vm.workItemSearch.pageSize = null;
            // var data = rpQuantityService.doSearchSysPXK(vm.workItemSearch);
            // console.log(data);
            // rpQuantityService.doSearchSysPXK(vm.workItemSearch).then(function (d) {
            //     CommonService.exportFile(vm.workItemGrid, d.data, vm.listRemove, vm.listConvert, "Báo cáo phiếu xuất kho quá hạn 7 ngày");
            // });

            vm.workItemSearch.page = null;
            vm.workItemSearch.pageSize = null;
            var data = rpQuantityService.doSearchSysPXK(vm.workItemSearch);
            rpQuantityService.doSearchSysPXK(vm.workItemSearch).then(function (d) {
                CommonService.exportFile(vm.workItemGridSevenHienFixPXK, d.data, vm.listRemove, vm.listConvert, "Báo cáo phiếu xuất kho A cấp quá hạn 7 ngày");
            });

        }
        vm.listRemove = [{
            title: "Thao tác",
        }]
        vm.listConvert = [];
        vm.provinceOptions = {
            dataTextField: "name",
            dataValueField: "id",
            placeholder: "Nhập mã hoặc tên tỉnh",
            select: function (e) {
                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.workItemSearch.catProvinceId = dataItem.catProvinceId;
                vm.workItemSearch.catProvinceCode = dataItem.code;
                vm.workItemSearch.catProvinceName = dataItem.name;
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
                            name: vm.workItemSearch.catProvinceName,
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
                    vm.workItemSearch.catProvinceId = null;
                    vm.workItemSearch.catProvinceCode = null;
                    vm.workItemSearch.catProvinceName = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.workItemSearch.catProvinceId = null;
                    vm.workItemSearch.catProvinceCode = null;
                    vm.workItemSearch.catProvinceName = null;
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
            var startDate = $('#workItemSearchDateFrom').val();
            var endDate = $('#workItemSearchDateTo').val();

            // kiem tra field nao dc nhap
            if(startDate !== "") {
                if(kendo.parseDate(startDate,"dd/MM/yyyy") == null){
                    vm.errMessage1 = "Ngày bắt đầu không hợp lệ";
                    $("#workItemSearchDateFrom").focus();
                    vm.checkStartDate = false;
                }else if(kendo.parseDate(startDate,"dd/MM/yyyy").getFullYear() > 2099 || kendo.parseDate(startDate,"dd/MM/yyyy").getFullYear() < 1900){
                    vm.errMessage1 = 'Ngày bắt đầu không hợp lệ';
                    $("#workItemSearchDateFrom").focus();
                    vm.checkStartDate = false;
                } else {
                    vm.errMessage1 = null;
                    vm.checkStartDate = true;
                }
            }

            if(endDate !== "") {
                if ( kendo.parseDate(endDate,"dd/MM/yyyy") == null) {
                    vm.errMessage = "Ngày kết thúc không hợp lệ";
                    $("#workItemSearchDateTo").focus();
                    vm.checkEndDate = false;
                }else if(kendo.parseDate(endDate,"dd/MM/yyyy").getFullYear() > 2099 || kendo.parseDate(endDate,"dd/MM/yyyy").getFullYear() < 1900){
                    vm.errMessage = 'Ngày kết thúc không hợp lệ';
                    $("#workItemSearchDateTo").focus();
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
            vm.workItemSearch.dateFrom = null;
            vm.workItemSearch.dateTo = null;
            vm.errMessage1 = null;
            vm.errMessage = null;
        }

        vm.patternSignerOptions={
            dataTextField: "email",
            placeholder:"Nhập mã hoặc tên người thực hiện",
            open: function(e) {
                vm.isSelect = false;
                vm.selectedDept1 = false;
            },
            select: function(e) {
                vm.isSelect = true;
                vm.selectedDept1 = true;
                data = this.dataItem(e.item.index());
                vm.workItemSearch.userName = data.fullName;
                vm.workItemSearch.email = data.email;
                vm.workItemSearch.sysUserId = data.sysUserId;
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
                                vm.workItemSearch.email = null;
                                vm.workItemSearch.userName = null;
                                vm.workItemSearch.sysUserId = null;
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
                    vm.workItemSearch.email = null;
                    vm.workItemSearch.userName = null;
                    vm.workItemSearch.sysUserId = null;
                }
            },
            close: function(e) {
            }
        };

        vm.openUser = function openUser(){

            var templateUrl = 'coms/popup/sysUserSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm nhân viên");
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','sysUserSearchController');

        };
        vm.onSaveSysUser = function onSaveSysUser(data){
            vm.workItemSearch.email = data.email;
            vm.workItemSearch.userName = data.fullName;
            vm.workItemSearch.sysUserId = data.sysUserId;
            console.log(vm.workItemSearch);
            htmlCommonService.dismissPopup();
            $("#signerGroup").focus();
        };

        vm.clearUser = function() {
            vm.workItemSearch.email = null;
            vm.workItemSearch.userName = null;
            vm.workItemSearch.sysUserId = null;
        }
    }
})();

