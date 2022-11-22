(function () {
    'use strict';
    var controllerId = 'requestGoodsController';

    angular.module('MetronicApp').controller(controllerId, requestGoodsController);

    function requestGoodsController($scope, $templateCache, $rootScope, $timeout, gettextCatalog,
                                    kendoConfig, $kWindow, requestGoodsService, htmlCommonService,
                                    CommonService, PopupConst, Restangular, RestEndpoint, Constant) {

        var vm = this;

        //var
        var today = new Date();
        vm.String = "Quản lý công trình > Quản lý công trình > Yêu cầu sản xuất vật tư";
        vm.searchForm = {};
        vm.statusList = [{
            "id": 0,
            "name": "Chưa gửi"
        }, {
            "id": 1,
            "name": "Đã gửi"
        }];
        vm.disableSearch = false;
        vm.editPopupOpen = false;
        vm.importRequestGoodsDetail = false;
        vm.rg = {
            status: 0
        };
        vm.catUnitList = [];
        vm.listChecked = [];
        vm.listConstructionCodeChecked = [];
        
        vm.dataRropFile=[];
        vm.dataFile=[];
        vm.doSearchFile={};
        vm.inputSign={};
        vm.checkInLike=false;
        vm.dataCheck = [];
        vm.listGroupCheckId=[];
        vm.showView = true;
        
        var editPopupHeight = [530, 530, 530, 570, 610, 650, 700];
        var viewPopupHeight = [430, 430, 460, 490, 520, 550, 580];
        var modalInstanceImport;
        var modalInstanceError;

        $scope.$watchGroup(['vm.requestGoodsFileGrid', 'vm.dataFile'], function (newVal, oldVal) {
			refreshGrid(vm.dataFile);
		});

		function refreshGrid(d) {
			var grid = vm.requestGoodsFileGrid;
			if (grid) {
			grid.dataSource.data(d);
				grid.refresh();
			}
		}
        
        initDataSearchForm();

        function initDataSearchForm() {
            // date field
            var from = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
            vm.searchForm.dateTo = htmlCommonService.formatDate(today);
            vm.searchForm.dateFrom = htmlCommonService.formatDate(from);

            // init dropDown add
            Restangular.all(RestEndpoint.REQUEST_GOODS_SERVICE_URL + "/getCatUnit").getList()
                .then(function (d) {
                    vm.catUnitList = d.plain();
                }).catch(function (e) {
                console.log("Không thể kết nối để lấy dữ liệu: " + e.message);
            });
            
            fillFileTable([]);
            loadFileData();
			requestGoodsService.checkSysGroupInLike({}).then(function (result) {
				vm.dataCheck = result.plain();
			}, function (errResponse) {
				toastr.error(gettextCatalog.getString("Xảy ra lỗi khi cập nhật trạng thái"));
			});
        }

        function doSearch() {
//            if (vm.disableSearch) {
//                return;
//            }
            var grid = vm.requestGoodsGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        }

        function cancel() {
//            CommonService.dismissPopup1();
            modalInstanceImport.dismiss();
        }

        function closePopupAll() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

        function callBackdoSearch() {
            vm.requestGoodsGrid.dataSource.page(1);
        }

        //function
        vm.validateDateField = validateDateField;
        vm.doSearch = doSearch;
        vm.callBackdoSearch = callBackdoSearch;
        vm.showHideColumn = showHideColumn;
        vm.gridColumnShowHideFilter = gridColumnShowHideFilter;
        vm.cancel = cancel;
        vm.closePopupAll = closePopupAll;
        vm.exportFile = exportFile;

        // ----- Add form start
        vm.add = add;
        vm.getAttachFile = getAttachFile;
        vm.disableAdd = true;

        function validateDataAdd() {
            vm.disableAdd = !(vm.isSelectConstruction);
        }

        function add() {
            resetInputAdd();
            var teamplateUrl = "coms/requestGoods/requestGoodsAddPopup.html";
            var title = "Tạo mới yêu cầu sản xuất vật tư";
            var windowId = "rg-add-window";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '70%', '570', null);
            initGridDetail();
        }

        function resetInputAdd() {
            vm.rg.catProvinceCode = null;
            vm.rg.catProvinceId = null;
            vm.rg.constructionCode = null;
            vm.rg.catStationCode = null;
            vm.rg.catStationId = null;
            vm.rg.requestContent = null;
            vm.rg.assignHandoverId = null;
            vm.rg.isDesign = null;
            vm.rg.status = 0;
            vm.errConstructionMsg = null;
            vm.disableFileDesign = true;
            vm.editPopupOpen = false;
            vm.isSelectStation = false;
            vm.isSelectProvince = false;
            vm.isSelectConstruction = false;
            vm.dataFile = [];
            vm.listFileData = [];
        }

        // --- File design start
        vm.getAttachFile = getAttachFile;
        vm.downloadFile = downloadFile;
        vm.getFileDesign = getFileDesign;

        function getAttachFile() {
            if (!!!vm.rg.constructionCode) {
                toastr.error("Chưa nhập mã công trình");
                return;
            }

            var templateUrl = "coms/assignHandover/assignHandoverAttachNV.html";
            var title = "File Thiết kế";
            var windowId = "rg-ATTACH_DESIGN_VIEW";
            CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, true, '1000', '200', null);
        }

        function downloadFile(dataItem) {
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
        }

        function getFileDesign(assignHandoverId) {
            vm.rg.assignHandoverId = assignHandoverId;
            var templateUrl = "coms/assignHandover/assignHandoverAttachNV.html";
            var title = "File Thiết kế";
            var windowId = "rg-ATTACH_DESIGN_VIEW";
            CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, true, '1000', '200', null);
        }

        // options
        vm.attachFileEditGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            resizable: true,
            pageable: false,
            dataSource: {
                serverPaging: false,
                schema: {
                    total: function (response) {
                        $timeout(function () {
                            vm.countCons = response.total
                        });
                        return response.total;
                    },
                    data: function (response) {
                        return response.data;
                    }
                },
                transport: {
                    read: {
                        url: Constant.BASE_SERVICE_URL + "assignHandoverService" + "/getById",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        var assignHandoverId = angular.copy(vm.rg.assignHandoverId);
                        return JSON.stringify(assignHandoverId);
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
//            dataBound: function () {
//                var GridDestination = $("#attachFileEditGridNV").data("kendoGrid");
//                GridDestination.pager.element.hide();
//            },
            columns: [
                {
                    title: "TT",
                    field: "stt",
                    template: function (dataItem) {
                        return $("#attachFileEditGridNV").data("kendoGrid").dataSource.indexOf(dataItem) + 1;
                    },
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
        // File design end ---

        // --- province
        vm.openCatProvincePopup = openCatProvincePopup;
        vm.onSaveCatProvince = onSaveCatProvince;
        vm.clearProvince = clearProvince;

        function openCatProvincePopup() {
            var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm tỉnh");
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions, data, vm, 'fff', 'ggfd', false, '85%', '85%', 'catProvinceSearchController');
        }

        function onSaveCatProvince(data) {
            vm.rg.catProvinceId = data.catProvinceId;
            vm.rg.catProvinceCode = data.code;
            htmlCommonService.dismissPopup();
        }

        function clearProvince() {
            vm.rg.catProvinceId = null;
            vm.rg.catProvinceCode = null;
        }

        vm.isSelectProvince = false;
        vm.provinceOptions = {
            dataTextField: "code",
            placeholder: "Nhập mã hoặc tên tỉnh",
            select: function (e) {
                vm.isSelectProvince = true;
                var dataItem = this.dataItem(e.item.index());
                vm.rg.catProvinceId = dataItem.catProvinceId;
                vm.rg.catProvinceCode = dataItem.code;
            },
            pageSize: 10,
            open: function (e) {
                vm.isSelectProvince = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.isSelectProvince = false;
                        return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
                            name: vm.rg.catProvinceCode,
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
                if (!vm.isSelectProvince) {
                    clearProvince();
                }
            },
            close: function (e) {
                if (!vm.isSelectProvince) {
                    clearProvince();
                }
            }
        };
        // province end ---

        // --- construction start
        vm.openPopupConstruction = openPopupConstruction;
        vm.onSaveComsConstruction = onSaveComsConstruction;
        vm.contructionSearchUrl = Constant.BASE_SERVICE_URL + RestEndpoint.REQUEST_GOODS_SERVICE_URL + "/searchConstruction";

        function clearConstruction() {
            vm.rg.constructionCode = null;
            vm.rg.constructionId = null;
        }

        function openPopupConstruction() {
            var templateUrl = 'coms/popup/comsConstructionSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm công trình");
            htmlCommonService.populatePopup(templateUrl, title, null, null, vm, null, 'string', false, '92%', '89%', 'searchConstructionController');
        }

        // save construction choice from popup
        function onSaveComsConstruction(data) {
            vm.isSelectConstruction = true;
            vm.rg.constructionCode = data.code;
            vm.rg.constructionId = data.constructionId;
            validateDataAdd();

            // set catStation & catProvince
            vm.rg.catStationCode = data.catStationCode;
            vm.rg.catStationId = data.catStationId;
            vm.isSelectStation = true;

            vm.rg.catProvinceCode = data.catProvinceCode;
            vm.rg.catProvinceId = data.catProvinceId;
            vm.isSelectProvince = true;

            vm.validateAddConstruction();
            updateFileDesignButton(data.assignHandoverId, data.isDesign);
        }

        vm.validateAddConstruction = function () {
            if (!!!vm.rg.constructionCode) {
                vm.errConstructionMsg = "Mã công trình không được để trống";
                return false;
            } else if (!vm.isSelectConstruction) {
                vm.errConstructionMsg = "Mã công trình không hợp lệ";
                return false;
            } else {
                vm.errConstructionMsg = null;
            }
            validateDataAdd();
        };

        function updateFileDesignButton(assignHandoverId, isDesign) {
            if (!!isDesign && isDesign === 1) {
                vm.rg.assignHandoverId = assignHandoverId;
                vm.disableFileDesign = false;
            } else {
                vm.rg.assignHandoverId = null;
                vm.disableFileDesign = true;
            }
        }

        // grid options for construction
        vm.isSelectConstruction = false;
        vm.constructionPopupOptions = {
            dataTextField: "code",
            dataValueField: "id",
            placeholder: "Nhập mã công trình",
            select: function (e) {
                vm.isSelectConstruction = true;
                var dataItem = this.dataItem(e.item.index());
                vm.rg.constructionCode = dataItem.code;
                vm.rg.constructionId = dataItem.constructionId;
                vm.isSelectStation = true;

                // set catStation & catProvince
                vm.rg.catStationCode = dataItem.catStationCode;
                vm.rg.catStationId = dataItem.catStationId;
                vm.isSelectStation = true;

                vm.rg.catProvinceCode = dataItem.catProvinceCode;
                vm.rg.catProvinceId = dataItem.catProvinceId;
                vm.isSelectProvince = true;

                vm.validateAddConstruction();
                updateFileDesignButton(dataItem.assignHandoverId, dataItem.isDesign);
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
                        return Restangular.all(RestEndpoint.REQUEST_GOODS_SERVICE_URL + '/searchConstruction').post({
                            catProvinceId: vm.rg.catProvinceId,
                            catStationId: vm.rg.catStationId,
                            keySearch: vm.rg.constructionCode,
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
                    // vm.assignHandover.constructionCode = null;
                    clearConstruction();
                    updateFileDesignButton();
                }
            },
            close: function (e) {
                if (!vm.isSelectConstruction) {
                    // vm.assignHandover.constructionCode = null;
                    clearConstruction();
                    updateFileDesignButton();
                }
            },
            ignoreCase: false
        };
        // construction end ---

        // --- catStation start
        // popup catStation
        vm.openPopupCatStation = openPopupCatStation;
        vm.onSaveCatStation = onSaveCatStation;

        function openPopupCatStation() {
            var templateUrl = 'coms/popup/catStationSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm trạm");
            htmlCommonService.populatePopup(templateUrl, title, null, null, vm, null, 'string', false, '92%', '89%', 'catStationSearchController');
        }

        // save catStation choice from popup
        function onSaveCatStation(data) {
            vm.isSelectStation = true;
            vm.rg.catStationCode = data.code;
            vm.rg.catStationId = data.catStationId;
        }


        // grid options for catStation
        vm.isSelectStation = false;
        vm.catStationOptions = {
            dataTextField: "code",
            placeholder: "Nhập mã trạm",
            select: function (e) {
                vm.isSelectStation = true;
                var dataItem = this.dataItem(e.item.index());
                vm.rg.catStationCode = dataItem.code;
                vm.rg.catStationId = dataItem.catStationId;
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
                        return Restangular.all("constructionService/" + 'getStationForAutoComplete').post({
                            catProvinceId: vm.rg.catProvinceId,
                            keySearch: vm.rg.catStationCode,
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
                    // vm.assignHandover.catStationCode = null;
                    vm.rg.catStationId = null;
                }
            },
            close: function (e) {
                if (!vm.isSelectStation) {
                    // vm.assignHandover.catStationCode = null;
                    vm.rg.catStationId = null;
                }
            },
            ignoreCase: false
        };
        // catStation end ---

        // --- Grid requestGoodsDetail start
        vm.disableDeleteGoodsDetail = true;
        vm.initGridDetail = initGridDetail;
        vm.initGridDetailView = initGridDetailView;
        vm.deleteGoodsDetail = deleteGoodsDetail;

        function dropDownCatUnitEditor(container, options) {
            container.css("overflow", "visible");
            $('<input name="' + options.field + '"/>')
                .appendTo(container)
                .kendoDropDownList({
                    autoBind: false,
                    dataTextField: "catUnitName",
                    dataValueField: "catUnitName",
                    dataSource: {
                        transport: {
                            // read: Constant.BASE_SERVICE_URL + RestEndpoint.REQUEST_GOODS_SERVICE_URL + "/getCatUnit"
                            read: function (options) {
                                options.success(vm.catUnitList);
                            }
                        }
                    },
                    select: function (e) {
                        var grid = e.sender.element.closest(".k-grid").data("kendoGrid");
                        var row = e.sender.element.closest("tr");
                        var rowData = grid.dataItem(row);

                        var dataItem = this.dataItem(e.item.index());
                        rowData.catUnitName = dataItem.catUnitName;
                        rowData.catUnitId = dataItem.catUnitId;
                    }
                });
            container.css("overflow", "visible");
        }

        function initGridDetail(data) {

            // reset grid data
            vm.gridDetailData = [];
            if (!!data) {
                for (var i = 0; i < data.length; i++) {
                    data[i].tempId = i + 1;
                    vm.gridDetailData.push(data[i]);
                }
//                vm.gridDetailData = data;
            }

            // init grid datasource: validation, inline edit
            var dataSourceGridDetail = new kendo.data.DataSource({
                transport: {
                    create: function (options) {
                        if (!!vm.gridDetailData && vm.gridDetailData.length > 0) {
                            options.data.tempId = vm.gridDetailData[vm.gridDetailData.length - 1].tempId + 1;
                        } else {
                            options.data.tempId = 1;
                        }
                        vm.gridDetailData.push(options.data);
                        vm.enableClickEdit = true;
                        options.success(options.data);
                    },
                    read: function (options) {
                        options.success(vm.gridDetailData);
                    },
                    update: function (options) {
                        for (var i = 0; i < vm.gridDetailData.length; i++) {
                            if (vm.gridDetailData[i].tempId === options.data.tempId) {
                                vm.gridDetailData[i].goodsName = options.data.goodsName;
                                vm.gridDetailData[i].suggestDate = options.data.suggestDate;
                                vm.gridDetailData[i].catUnitName = options.data.catUnitName;
                                vm.gridDetailData[i].quantity = options.data.quantity;
                                vm.gridDetailData[i].description = options.data.description;
                            }
                        }
                        vm.enableClickEdit = true;
                        options.success(options.data);
                    }
                },
                sync: function (e) {
                    console.log("sync event");
                    $("#requestGoodsDetailGrid").data("kendoGrid").addRow();
                },
                schema: {
                    model: {
                        id: "tempId",
                        fields: {
                            goodsName: {
                                type: "string", validation: {
                                    goodsNameValidation: function (input) {
                                        if (input.is("[name='goodsName']")) {
                                            if (input.val() === "" || input.val() === null) {
                                                input.attr("data-goodsNameValidation-msg", "Tên thiết bị không được để trống");
                                                vm.enableClickEdit = false;
                                                return false;
                                            }
                                        }
                                        return true;
                                    }
                                }
                            },
                            suggestDate: {
                                validation: {
                                    suggestDateValidation: function (input) {
                                        if (input.is("[name='suggestDate']")) {
                                            // if (input.val() === "" || input.val() === null) {
                                            // 	input.attr("data-suggestDateValidation-msg", "Chọn Ngày cấp");
                                            //     return false;
                                            // }
                                            if (input.val() === "" || input.val() === null) {
                                                return true;
                                            }
                                            var date = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                                            if (!!!date || date === "" || date.getFullYear() < 1970) {
                                                input.attr("data-suggestDateValidation-msg", "Ngày cấp không hợp lệ");
                                                vm.enableClickEdit = false;
                                                return false;
                                            }
                                        }
                                        return true;
                                    }
                                }, defaultValue: new Date()
                            },
                            catUnitId: {type: "number"},
                            catUnitName: {
                                type: "string", validation: {
                                    catUnitNameValidation: function (input) {
                                        if (input.is("[name='catUnitName']")) {
                                            if (input.val() === "" || input.val() === null) {
                                                input.attr("data-catUnitNameValidation-msg", "Chọn Đơn vị tính");
                                                vm.enableClickEdit = false;
                                                return false;
                                            }
                                        }
                                        return true;
                                    }
                                }
                            },
                            quantity: {
                                type: "number", validation: {
                                    quantityValidation: function (input) {
                                        if (input.is("[name='quantity']")) {
                                            if (input.val() === "" || input.val() === null) {
                                                input.attr("data-quantityValidation-msg", "Nhập Số lượng");
                                                vm.enableClickEdit = false;
                                                return false;
                                            }

                                            if (input.val() <= 0) {
                                                input.attr("data-quantityValidation-msg", "Nhập số lớn hơn 0");
                                                vm.enableClickEdit = false;
                                                return false;
                                            }
                                        }
                                        return true;
                                    }
                                }
                            },
                            description: {type: "string"}
                        }
                    }
                },
                pageSize: 10
            });
            vm.enableClickEdit = false;
            // grid options
            vm.requestGoodsDetailGridOptions = kendoConfig.getGridOptions({
                dataSource: dataSourceGridDetail,
                edit: function (e) {
                    console.log("edit");
                    vm.enableClickEdit = false;
                },
                save: function (e) {
                    console.log("save");
                    vm.enableClickEdit = true;
                },
                change: function (e) {
                    console.log("change");
                    if (vm.enableClickEdit) {
                        $("#requestGoodsDetailGrid").data("kendoGrid").editRow(this.select());
                    }
                },
                cancel: function (e) {
                    console.log("cancel");
                    vm.enableClickEdit = true;
                },
                selectable: "cell",
                editable: "inline",
                columnMenu: false,
                reorderable: false,
                resizable: true,
                toolbar: ["create",
                    {
                        name: "actions",
                        template: '<button class="btn btn-qlk padding-search-right TkQLK " ng-click="caller.importExcel()" uib-tooltip="Import dữ liệu giao việc" translate>Import</button>'
                    }],
                pageable: {
                    refresh: false,
                    pageSizes: [10, 15, 20],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
                messages: {
                    commands: {
                        cancel: "Hủy thay đổi",
                        create: "Tạo mới yêu cầu",
                        update: "OK",
                        edit: "Sửa",
                        canceledit: "Hủy"
                    },
                    noRecords: "<div style='margin: 5px 0 5px 10px;width: auto;'>Thêm mới chi tiết yêu cầu</div>"
                    // noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                noRecords: true,
                scrollable: true,
                columns: [
                    // {
                    //     title: "#",
                    //     width: '5%',
                    //     columnMenu: false,
                    //     headerAttributes: {
                    //         style: "text-align:center;"
                    //     },
                    //     attributes: {
                    //         style: "text-align:center;"
                    //     },
                    //     template: function() {
                    //     	return ++recordGoodsDetail;
                    //     }
                    // },
                    {
                        title: "Thao tác",
                        field: "action",
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:left;"
                        },
                        width: "155px",
                        command: [
                            "edit",
                            {
                                name: "remove",
                                text: "Xóa",
                                click: vm.deleteGoodsDetail
                            }]
                    },
                    {
                        title: "Tên thiết bị <span style='color:red'>*</span>",
                        field: 'goodsName',
                        width: '200px',
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        }
                    },
                    {
                        title: "Ngày ĐN cấp",
                        field: 'suggestDate',
                        width: '100px',
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:center;"
                        },
                        editor: function (container, options) {
                            container.css("overflow", "visible");
                            var input = $("<input name='" + options.field + "'/>");
                            input.appendTo(container);
                            input.kendoDatePicker({
                                format: "dd/MM/yyyy",
                                value: new Date()
                            });
                        },
                        template: function (dataItem) {
                            return !!dataItem.suggestDate
                                ? kendo.toString(kendo.parseDate(dataItem.suggestDate), 'dd/MM/yyyy')
                                : "";
                        }
                    },
                    {
                        title: "Đơn vị tính <span style='color:red'>*</span>",
                        field: 'catUnitName',
                        width: '100px',
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:left;"
                        },
                        editor: dropDownCatUnitEditor
                    },
                    {
                        title: "Số lượng <span style='color:red'>*</span>",
                        field: 'quantity',
                        width: '85px',
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:left;"
                        },
                        editor: function (container, options) {
                            var input = $("<input name='" + options.field + "'/>");
                            input.attr("type", "number");
                            input.attr("style", "width:100%");
                            input.on("click", function () {
                                input.select();
                            });
                            input.appendTo(container);
                        }
                    },
                    {
                        title: "Ghi chú",
                        field: 'description',
                        width: '220px',
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        }
                    }
                ]
            });
        }

        function initGridDetailView(data) {
            // reset grid data
            var gridData = [];
            if (!!data) {
                for (var i = 0; i < data.length; i++) {
                    data[i].tempId = i + 1;
                    gridData.push(data[i]);
                }
            }

            // init grid datasource: validation, inline edit
            var dataSourceGridDetail = new kendo.data.DataSource({
                transport: {
                    read: function (options) {
                        options.success(gridData);
                    }
                },
                pageSize: 10
            });

            // grid options
            vm.requestGoodsDetailViewGridOptions = kendoConfig.getGridOptions({
                dataSource: dataSourceGridDetail,
                editable: false,
                columnMenu: false,
                reorderable: false,
                resizable: true,
                pageable: {
                    refresh: false,
                    pageSizes: [10, 15, 20],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
                messages: {
                    noRecords: "<div style='margin: 5px 0 5px 10px;width: auto;'>Không có kết quả hiển thị</div>"
                },
                noRecords: true,
                scrollable: true,
                columns: [
                    {
                        title: "STT",
                        field: 'tempId',
                        width: '40px',
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Tên thiết bị <span style='color:red'>*</span>",
                        field: 'goodsName',
                        width: '200px',
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        }
                    },
                    {
                        title: "Ngày ĐN cấp",
                        field: 'suggestDate',
                        width: '100px',
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function (dataItem) {
                            return !!dataItem.suggestDate
                                ? kendo.toString(kendo.parseDate(dataItem.suggestDate), 'dd/MM/yyyy')
                                : "";
                        }
                    },
                    {
                        title: "Đơn vị tính <span style='color:red'>*</span>",
                        field: 'catUnitName',
                        width: '100px',
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Số lượng <span style='color:red'>*</span>",
                        field: 'quantity',
                        width: '85px',
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Ghi chú",
                        field: 'description',
                        width: '220px',
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        }
                    }
                ]
            });
        }

        function deleteGoodsDetail(e) {
            e.preventDefault();
            var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
            confirm("Xác nhận xóa bản ghi?", function () {
                for (var i = 0; i < vm.gridDetailData.length; i++) {
                    if (vm.gridDetailData[i].tempId === dataItem.tempId) {
                        vm.gridDetailData.splice(i, 1);
                    }
                }
                vm.enableClickEdit = true;
                vm.requestGoodsDetailGrid.dataSource.read();
            });
        }

        // Grid requestGoodsDetail end ---
        // --- save Request start
        vm.listFileData = [];
        vm.save = save;

        function save() {
            // requestGoods data: vm.rg, requestGoodsDetail data vm.gridDetailData
        	if (checkNotCompleteGridDetail()) return;
        	
        	if(vm.dataFile.length<2){
        		toastr.warning("Phải có ít nhất 2 file đính kèm !");
        		return;
        	} else {
        		for(var i=0;i<vm.dataFile.length;i++){
        			if(vm.dataFile[i].appParam.code=="choese" && vm.dataFile[i].appParam.name=="~~ Chọn ~~"){
        				toastr.warning("Chưa chọn loại file !");
                		return;
        			}
        		}
        	}

            vm.rg.requestGoodsDetailList = vm.gridDetailData;
            var gridFile = $("#requestGoodsFileGrid").data("kendoGrid").dataSource.data();
            for(var i=0;i<vm.dataFile.length;i++){
            	var obj = {};
            	obj.appParamId = vm.dataFile[i].appParam.appParamId;
            	obj.code = vm.dataFile[i].appParam.code;
            	obj.name = vm.dataFile[i].appParam.name;
            	obj.parOrder = vm.dataFile[i].appParam.parOrder;
            	vm.dataFile[i].appParam = obj;
            }
            vm.rg.listFileData = vm.dataFile;
            requestGoodsService.addNewRequestGoods(vm.rg).then(function (response) {
                    if (!!response.error) {
                        toastr.error(response.error);
                        return;
                    }
                    toastr.success("Thêm yêu cầu thành công");
                    closePopupAll();
                    doSearch();
                }, function (err) {
                    toastr.error("Yêu cầu không thành công");
                    console.log(err);
                });
        }

        // save Request end ---

        // --- resetInput start
        vm.resetInputDescription = resetInputDescription;
        vm.resetInputConstruction = resetInputConstruction;
        vm.resetInputStation = resetInputStation;
        vm.resetInputProvince = resetInputProvince;

        function resetInputDescription() {
            vm.rg.requestContent = null;
        }

        function resetInputConstruction() {
            vm.rg.constructionCode = null;
            vm.rg.constructionId = null;
            vm.isSelectConstruction = false;
            validateDataAdd();
        }

        function resetInputStation() {
            vm.rg.catStationCode = null;
            vm.rg.catStationId = null;
            vm.isSelectStation = false;
        }

        function resetInputProvince() {
            vm.rg.catProvinceCode = null;
            vm.rg.catProvinceId = null;
            vm.isSelectProvince = false;
        }

        // resetInput end ---
// Add form end -----
// ----- Action start
        // --- doRequestGoods start
        vm.doRequestGoods = doRequestGoods;
        vm.handleCheck = handleCheck;

        // handle checkbox send request
        vm.listCheck=[];
        function handleCheck(dataItem) {
            var index = vm.listChecked.indexOf(dataItem.requestGoodsId);
            if (index === -1) {
                // vm.listChecked.push(dataItem);
                vm.listChecked.push(dataItem.requestGoodsId);
                vm.listConstructionCodeChecked.push(dataItem.constructionCode);
                vm.listCheck.push(dataItem);
            } else {
                vm.listChecked.splice(index, 1);
                vm.listConstructionCodeChecked.splice(index, 1);
                vm.listCheck.splice(index, 1);
            }
        }

        function doRequestGoods(dataItem) {
            var listRequests;
            var listConsCode;
            if (!!dataItem) {
            	if(dataItem.signState!=3){
            		toastr.warning("Bản ghi phải được trình ký trước khi gửi yêu cầu !");
            		return;
            	} else {
            		listRequests = [dataItem.requestGoodsId];
                    listConsCode = [dataItem.constructionCode];
            	}
                // vm.listChecked = [dataItem.requestGoodsId];
            } else if (vm.listChecked.length < 1) {
                toastr.error("Chọn ít nhất 1 yêu cầu!");
                return;
            } else {
            	for(var i=0;i<vm.listCheck.length;i++){
            		if(vm.listCheck[i].signState!=3){
            			toastr.warning("Bản ghi phải được trình ký trước khi gửi yêu cầu !");
            			return;
            		}
            	}
                listRequests = angular.copy(vm.listChecked);
                listConsCode = angular.copy(vm.listConstructionCodeChecked);
            }

            var message = 'Thực hiện gửi yêu cầu sản xuất vật tư cho công trình: ' +
                '<br>' +
                '<span style="color: red;font-weight: bold;">"' + listConsCode.join('", "') + '"</span>' + ' ?';

            confirm(message,
                function () {
                    //confirm
                    requestGoodsService.doRequestGoods(listRequests)
                        .then(function (response) {
                            if (!!response.error) {
                                toastr.error(response.error);
                                return;
                            }
                            toastr.success("Gửi yêu cầu thành công");
                            vm.listChecked = [];
                            vm.listConstructionCodeChecked = [];
                            // cancel();
                            doSearch();
                        }, function (err) {
                            toastr.error("Có lỗi xảy ra");
                            console.log(err);
                        })
                })
        }

        // requestGoods end ---

        // --- importExcel start

        // import excel
        vm.importExcel = importExcel;
        vm.submitImport = submitImport;
        vm.closeErrImportPopUp = closeErrImportPopUp;
        vm.downloadTemplate = downloadTemplate;

        function importExcel(whichGrid) {
            var title;
            var windowId;
            if (!!whichGrid && whichGrid === "main") {
                vm.importRequestGoodsDetail = false;
                title = "Tải lên Yêu cầu sản xuất vật tư";
                windowId = "rg-import";
            } else {
                vm.importRequestGoodsDetail = true;
                title = "Tải lên Chi tiết vật tư";
                windowId = "rg-import-detail";
            }

            vm.fileImportData = null;
            var templateUrl = "coms/requestGoods/requestGoodsImport.html";
            CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, true, '1000', '275', null);
            setTimeout(function () {
                modalInstanceImport = CommonService.getModalInstance1();
            }, 100);
        }

        function validateFileImport() {
            $('#testSubmit').addClass('loadersmall');
            vm.disableSubmit = true;
            if (!vm.fileImportData) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Bạn chưa chọn file để import");
                return false;
            }
            if ($('.k-invalid-msg').is(':visible')) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                return false;
            }
            if (vm.fileImportData.name.split('.').pop() !== 'xls' && vm.fileImportData.name.split('.').pop() !== 'xlsx') {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Sai định dạng file");
                return false;
            }
            return true;
        }

        function submitImport() {
            if (!validateFileImport())
                return;

            var formData = new FormData();
            formData.append('multipartFile', vm.fileImportData);
            var url = Constant.BASE_SERVICE_URL + RestEndpoint.REQUEST_GOODS_SERVICE_URL + "/importExcel?folder=temp";
            if (vm.importRequestGoodsDetail) {
                url = Constant.BASE_SERVICE_URL + RestEndpoint.REQUEST_GOODS_SERVICE_URL + "/importExcelDetail?folder=temp";
            }

            return $.ajax({
                url: url,
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
                    } else if (!!data.error) {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.error(data.error);
                    } else if (data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length > 0) {
                        vm.lstErrImport = data[data.length - 1].errorList;
                        vm.objectErr = data[data.length - 1];
                        var templateUrl = "wms/createImportRequirementManagement/importResultPopUp.html";
                        var title = "Kết quả Import";
                        var windowId = "ERR_IMPORT";
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        CommonService.populatePopupCreate(templateUrl, title, vm.lstErrImport, vm, windowId, false, '80%', '420px');
                        fillDataImportErrTable(vm.lstErrImport);
                        setTimeout(function () {
                            modalInstanceError = CommonService.getModalInstance1();
                        }, 100);
                    } else {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.success("Import thành công");
                        if (vm.importRequestGoodsDetail) {
                            for (var i = 0; i < data.length; i++) {
                                data[i].tempId = vm.gridDetailData.length + 1;
                                data[i].suggestDate = !!data[i].suggestDate ? new Date(data[i].suggestDate) : null;
                                vm.gridDetailData.push(data[i]);
                            }
                            $('#requestGoodsDetailGrid').data('kendoGrid').dataSource.read();
                            cancel();
                        } else {
                            doSearch();
                            cancel();
                        }
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
            });
        };

        function closeErrImportPopUp() {
            // $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
            modalInstanceError.dismiss();
        }

        function downloadTemplate() {
            var postBody = vm.importRequestGoodsDetail ? 1 : 2;
            requestGoodsService.downloadTemplate(postBody).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function (err) {
                console.log(err);
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
            });
            /*
            requestGoodsService.downloadTemplate().then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + RestEndpoint.REQUEST_GOODS_SERVICE_URL + "/downloadFileForConstruction?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
                return;
            });
            */
        }

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
                        // template: dataItem => $("#importGoodResultGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
                        template: function (dataItem) {
                            return $("#importGoodResultGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1;
                        },
                        width: 70,
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:center;"
                        }
                    }, {
                        title: "Dòng lỗi",
                        field:
                            'lineError',
                        width:
                            100,
                        headerAttributes:
                            {
                                style: "text-align:center;"
                            }
                        ,
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,
                    {
                        title: "Cột lỗi",
                        field:
                            'columnError',
                        width:
                            100,
                        headerAttributes:
                            {
                                style: "text-align:center;"
                            }
                        ,
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,
                    {
                        title: "Nội dung lỗi",
                        field:
                            'detailError',
                        width:
                            250,
                        headerAttributes:
                            {
                                style: "text-align:center;"
                            }
                        ,
                        attributes: {
                            style: "text-align:left;"
                        }
                    }
                ]
            })
            ;
        }

        // importExcel end ---
        // --- edit start
        vm.edit = edit;
        vm.submitEdit = submitEdit;

//        $scope.$on('Popup.CloseClick', function() {
//            vm.editPopupOpen = false;
//        });
        function getEditPopupHeight(dataSize, status) {
            var index = dataSize > 6 ? 6 : dataSize;
            if (status === 1) {
                return viewPopupHeight[index];
            } else {
                return editPopupHeight[index];
            }
        }

        function edit(dataItem) {
            var detailData = [];
            vm.listGroupCheckId=[];
            vm.signState = dataItem.signState;
            requestGoodsService.getDetailsById(dataItem.requestGoodsId)
                .then(function (response) {
                    resetInputAdd();
                    detailData = response.data;
                    vm.editPopupOpen = true;
                    initEditPopup(dataItem);
                    var teamplateUrl = "coms/requestGoods/requestGoodsAddPopup.html";
                    var title = "Chỉnh sửa yêu cầu sản xuất vật tư";
                    var windowId = "rg-edit-window";
                    var height = getEditPopupHeight(detailData.length, dataItem.status);
                    vm.doSearchFile.objectId = dataItem.requestGoodsId;
					vm.doSearchFile.status = '1';
					requestGoodsService.doSearchFileTk(vm.doSearchFile).then(function (result) {
						CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '70%', height, null);
						vm.dataFile = result.plain();
					});
					
                    if (dataItem.status === 0) {
                        initGridDetail(detailData);
                    } else {
                        initGridDetailView(detailData);
                    }
                    vm.enableClickEdit = true;
                }, function (err) {
                    toastr.error("Không lấy được dữ liệu chi tiết yêu cầu");
                    console.log(err);
                    return;
                });
        }

        function initEditPopup(dataItem) {
            vm.rg.catProvinceCode = dataItem.catProvinceCode;
            vm.rg.catProvinceId = dataItem.catProvinceId;
            vm.rg.constructionCode = dataItem.constructionCode;
            vm.rg.catStationCode = dataItem.catStationCode;
            vm.rg.catStationId = dataItem.catStationId;
            vm.rg.requestContent = dataItem.requestContent;

            vm.rg.requestGoodsId = dataItem.requestGoodsId;
            vm.rg.assignHandoverId = dataItem.assignHandoverId;
            vm.rg.status = dataItem.status;

            updateFileDesignButton(dataItem.assignHandoverId, dataItem.isDesign);
            // vm.rg.isDesign = dataItem.isDesign;
        }
        
        function checkNotCompleteGridDetail() {
            $("#requestGoodsDetailGrid").data("kendoGrid").saveRow();

            if (vm.gridDetailData.length < 1) {
                toastr.error("Nhập ít nhất một yêu cầu");
                return true;
            }

            var validateMsg = $(".k-widget.k-tooltip.k-tooltip-validation.k-invalid-msg");
            if (validateMsg.is(":visible")) {
                toastr.error("Hoàn thành yêu cầu trước khi ghi lại");
                return true;
            }

            return false;
        }
        

        function submitEdit(andSendRequest) {
        	if (checkNotCompleteGridDetail()) return;
        	//Huypq-start
        	var gridFile = $("#requestGoodsFileGrid").data("kendoGrid").dataSource.data();
        	if(gridFile.length<2){
        		toastr.warning("Phải đính kèm 2 file trở lên !");
        		return;
        	} else {
        		for(var i=0;i<vm.dataFile.length;i++){
        			if(vm.dataFile[i].appParam.code=="choese" && vm.dataFile[i].appParam.name=="~~ Chọn ~~"){
        				toastr.warning("Chưa chọn loại file !");
                		return;
        			}
        		}
        	}
            for(var i=0;i<gridFile.length;i++){
            	var obj = {};
            	obj.appParamId = gridFile[i].appParam.appParamId;
            	obj.code = gridFile[i].appParam.code;
            	obj.name = gridFile[i].appParam.name;
            	if(gridFile[i].appParam.parOrder==null){
            		obj.parOrder = gridFile[i].parOrder;
            	} else {
            		obj.parOrder = gridFile[i].appParam.parOrder;
            	}
            	
            	gridFile[i].appParam = obj;
            }
        	//Huy-end
        	
            var rqBody = {
                requestGoodsId: vm.rg.requestGoodsId,
                requestContent: vm.rg.requestContent,
                requestGoodsDetailList: vm.gridDetailData,
                listFileData : gridFile,
                signState : vm.signState
            };

            if (andSendRequest) {
            	if(vm.signState!=3){
            		toastr.warning("Bản ghi không đúng trạng thái để gửi yêu cầu !");
            		return;
            	}
                rqBody.status = 1;
            }

            requestGoodsService.deleteFileTk(vm.rg.requestGoodsId)
            .then(function (response) {
                if (!!response.error) {
                    toastr.error(response.error);
                    return;
                }
            }, function (err) {
                toastr.error("Có lỗi xảy ra");
            });
            
            requestGoodsService.editRequestGoods(rqBody)
                .then(function (response) {
                    if (!!response.error) {
                        toastr.error(response.error);
                        return;
                    }
                    toastr.success("Chỉnh sửa yêu cầu thành công!");
                    closePopupAll();
                    doSearch();
                }, function (err) {
                    toastr.error("Có lỗi xảy ra");
                    console.log(err);
                });
        }

        // edit end ---

        // --- delete start
        vm.deleteRg = deleteRg;

        function deleteRg(dataItem) {
            var requestGoodsId = dataItem.requestGoodsId;

            confirm("Xóa yêu cầu vật tư?", function () {
                requestGoodsService.deleteRg(requestGoodsId)
                    .then(function (response) {
                        if (!!response.error) {
                            toastr.error(response.error);
                            return;
                        }
                        toastr.success("Xóa yêu cầu thành công");
                        doSearch();
                    }, function (err) {
                        toastr.error("Có lỗi xảy ra");
                        console.log(err);
                    })
            });
        }

        // delete end ---

// Action end -----

        vm.resetFormFieldStatus = function () {
            vm.searchForm.status = null;
        };

        function validateDateField(key) {
            var date;
            var element = $('#' + key).val();
            switch (key) {
                case 'dateFrom':
                    date = kendo.parseDate(element, "dd/MM/yyyy");
                    vm.dateFromErr = validateDate(date, element);
                    break;
                case 'dateTo':
                    date = kendo.parseDate(element, "dd/MM/yyyy");
                    vm.dateToErr = validateDate(date, element);
                    break;
                default:
                    vm.disableSearch = false;
                    break;
            }
        }

        function validateDate(date, element) {
            if (!!!element) {
                vm.disableSearch = false;
                return null;
            }
            if (date == null || date.getFullYear() > 9999 || date.getFullYear() < 1000) {
                vm.disableSearch = true;
                return 'Ngày không hợp lệ';
            } else {
                vm.disableSearch = false;
                return null;
            }
        }

        vm.resetFormFieldDate = function () {
            vm.searchForm.dateFrom = null;
            vm.searchForm.dateTo = null;
            vm.dateFromErr = null;
            vm.dateToErr = null;
            vm.disableSearch = false;
        };

// export excel
        function exportFile() {
            vm.searchForm.page = null;
            vm.searchForm.pageSize = null;
            var data = vm.searchForm;
            var listRemove = [{
                title: "Thao tác"
            }, {
                title: "File thiết kế"
            }, {
                title: ""
            }];

            Restangular.all("requestGoodsService" + "/doSearch").post(data).then(function (d) {
                var data = d.data;
                for (var i = 0; i < data.length; i++) {
                    data[i].receiveDate = !!data[i].receiveDate
                        ? kendo.toString(kendo.parseDate(data[i].receiveDate), 'dd/MM/yyyy')
                        : "";
                    if(data[i].signState==1){
                    	data[i].signState='Chưa trình ký';	
                    } else if(data[i].signState==2){
                    	data[i].signState='Đang trình ký';
                    } else if(data[i].signState==3){
                    	data[i].signState='Đã trình ký';
                    } else if(data[i].signState==4){
                    	data[i].signState='Từ chối ký';
                    }
                    
                    if(data[i].status==0){
                    	data[i].status = 'Chưa gửi yêu cầu';
                    } else if(data[i].status==1){
                    	data[i].status = 'Đã gửi yêu cầu';
                    }
                }
                CommonService.exportFile(vm.requestGoodsGrid, data, listRemove, [], "Danh sách yêu cầu");
            });
        }

// show hide column
        function showHideColumn(column) {
            if (angular.isUndefined(column.hidden)) {
                vm.requestGoodsGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.requestGoodsGrid.showColumn(column);
            } else {
                vm.requestGoodsGrid.hideColumn(column);
            }
        }

        function gridColumnShowHideFilter(item) {
            return item.type == null || item.type !== 1;
        }

// ---------- Grid Opts

//main grid
        var record = 0;
        vm.requestGoodsGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            reorderable: true,
            toolbar: [
                {
                    name: "actions",
                    template:
                        '<button class="btn btn-qlk padding-search-right addQLK ng-scope" ng-click="vm.add()" uib-tooltip="Tạo mới" style="margin-right: 16px;" translate="">Tạo mới</button>' +
                        '<button class="btn btn-qlk padding-search-right excelQLK " ng-click="vm.importExcel(\'main\')" uib-tooltip="Import dữ liệu giao việc" style="margin-right: 16px;" translate>Import</button>' +
                        '<button class="btn btn-qlk padding-search-right TkQLK"' +
                        'ng-click="vm.sendToSign()"  uib-tooltip="Trình ký" translate>Trình ký</button>' +
                        '</div>'+
                        '<span><i class="fa fa-arrow-right" style="color: deepskyblue; position: relative; left: 26px; top: 2px; pointer-events: none; font-size: 19px;"></i></span>' +
                        '<button class="btn btn-qlk padding-search-right" ng-click="vm.doRequestGoods()" uib-tooltip="Gửi yêu cầu" style="width: 140px;" translate>Gửi yêu cầu</button>'
                        +
                        '<div class="btn-group pull-right margin_top_button margin_right10">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()" aria-hidden="true"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.requestGoodsGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                }
            ],
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                        $timeout(function () {
                            vm.count = response.total
                        });
                        return response.total;
                    },
                    data: function (response) {
                        var gridData = response.data;
                        for (var i = 0; i < gridData.length; i++) {
                            for (var j = 0; j < vm.listChecked.length; j++) {
                                if (gridData[i].requestGoodsId === vm.listChecked[j]) {
                                    gridData[i].checked = true;
                                }
                            }
                        }

                        return response.data;
                    }
                },
                transport: {
                    read: {
                        url: Constant.BASE_SERVICE_URL + RestEndpoint.REQUEST_GOODS_SERVICE_URL + "/doSearch",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.searchForm.page = options.page;
                        vm.searchForm.pageSize = options.pageSize;
                        return JSON.stringify(vm.searchForm)
                    }
                },
                pageSize: 10
            },
            columnMenu: false,
            noRecords: true,
            messages: {
                noRecords: "<div style='margin: 5px 0 5px 10px;width: auto;'>Không có kết quả hiển thị</div>"
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
                    hidden: true
                },{
                    title: "",
                    field: "",
                    width: '40px',
                    columnMenu: false,
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        if ((dataItem.status === 0 || dataItem.signState === 3) && dataItem.status !=1) {
                            return "<input type='checkbox' id='childcheck' name='gridcheckbox' ng-model='dataItem.checked' ng-click='vm.handleCheck(dataItem)'/>";
                        } else {
                            return "<span></span>";
                        }
                    }
                },
                {
                    title: "Mã công trình",
                    field: 'constructionCode',
                    width: '19%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;height:44px;"
                    }
                },
                {
                    title: "Hợp đồng",
                    field: 'cntContractCode',
                    width: '19%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                
                {
                    title: "Nội dung xin cấp",
                    field: 'requestContent',
                    width: '35%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal;"
                    }
                },
                {
                    title: "Ngày nhận BGMB",
                    field: 'handoverDateBuild',
                    width: '15%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        return !!dataItem.receiveDate
                            ? kendo.toString(kendo.parseDate(dataItem.receiveDate), 'dd/MM/yyyy')
                            : "";
                    }
                },
                {
                    title: "File thiết kế",
                    field: 'fileThietKe',
                    width: '12%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: function (dataItem) {
                        return '<button class="btn btn-qlk width100" style="padding-right:15px;" id="rg-design" ' +
                            'ng-hide="!!!dataItem.isDesign || dataItem.isDesign === 0" ' +
                            'ng-click="vm.getFileDesign(dataItem.assignHandoverId)" >Tải File</button>'
                    }
                },
                {
                    title: "Trạng thái ký CA",
                    field: 'signState',
                    width: '12%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: function (dataItem) {
                        if(dataItem.signState==1){
                        	return "Chưa trình ký";
                        } else if(dataItem.signState==2){
                        	return "Đang trình ký";
                        } else if(dataItem.signState==3){
                        	return "Đã ký";
                        } else if(dataItem.signState==4){
                        	return "Từ chối ký";
                        } else {
                        	return "";
                        }
                    }
                },
                {
                    title: "Trạng thái",
                    field: 'status',
                    width: '12%',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: function (dataItem) {
                    	if(dataItem.status==0){
                    		return "Chưa gửi yêu cầu";
                    	} else if(dataItem.status==1){
                    		return "Đã gửi yêu cầu";
                    	} else {
                    		return "";
                    	}
                    }
                },
                {
                    title: "Thao tác",
                    width: '145px',
                    field: "action",
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        return '<div class="text-center">'
                        	+'<a class="a-img"><img ng-click="vm.viewSignedDoc(dataItem)" uib-tooltip="Xuất Pdf" src="assets/layouts/layout/img/input/pdf.png"></a>'
                            + '<button  style=" border: none; background-color: white;" id="rg-requestGoods" ng-click="vm.doRequestGoods(dataItem)" ' +
                            'class="icon_table aria-hidden" ng-hide="dataItem.status === 1 || dataItem.signState==2 || dataItem.signState==1"' +
                            'uib-tooltip="Gửi yêu cầu" translate>' +
                            '<i class="fa fa-arrow-right" style="color:deepskyblue"  ng-disabled="" aria-hidden="true"></i>' +
                            '</button>'

                            + '<button style=" border: none; background-color: white; height:36px" id="rg-edit" ng-click="vm.edit(dataItem)" ' +
                            'ng-hide="dataItem.signState == 2"' +
                            'class="icon_table aria-hidden" ' +
                            'uib-tooltip="Chỉnh sửa" translate>' +
                            '<i class="fa fa-pencil" style="color:orange;" aria-hidden="true"></i>' +
                            '</button>'

                            + '<button style=" border: none; background-color: white;" ng-hide="dataItem.status === 1 || dataItem.signState==2 || dataItem.signState==3"' +
                            'class="#=appParamId# icon_table aria-hidden" ng-click="vm.deleteRg(dataItem)" uib-tooltip="Xóa" translate>' +
                            '<i class="fa fa-trash" style="color: rgba(0,0,0,0.56);" aria-hidden="true"></i>' +
                            '</button>'
                            + '</div>';
                    }
                }
            ]
        });

        vm.viewSignedDoc = viewSignedDoc;
	    function viewSignedDoc(dataItem){
	        var obj={
	            objectId:dataItem.requestGoodsId,
	            type:"60"
	        }
	        CommonService.viewSignedDoc(obj);
	    }
        
        function categoryDropDownEditor(container, options) {
			$('<input required name="' + options.field + '"/>').appendTo(container).kendoDropDownList({
				autoBind: false,
				suggest: true,
				dataTextField: "name",
				dataValueField: "code",
				dataSource: vm.dataRropFile,
			});
		}
        
        //HuyPq-20190326-start
        function fillFileTable(data) {
			var dataSource = new kendo.data.DataSource({
				data: data,
				autoSync: false,
				schema: {
					model: {
						id: "requestGoodsFileGrid",
						fields: {
							stt: { editable: false },
							name: { editable: false },
							appParam: { defaultValue: { name: "~~Chọn~~", code: "choese" } },
							acctions: { editable: false },
						}
					}
				}
			});
        vm.gridFileOptions = kendoConfig.getGridOptions({
			autoBind: true,
			resizable: true,
			dataSource: dataSource,
			noRecords: true,
			columnMenu: false,
			scrollable: false,
			messages: {
				noRecords: CommonService.translate("Không có kết quả hiển thị")
			}, dataBound: function () {
				var GridDestination = $("#requestGoodsFileGrid").data("kendoGrid");
				GridDestination.pager.element.hide();
			},
			columns: [{
				title: "TT",
				field: "stt",
				template: dataItem => $("#requestGoodsFileGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
				width: 20,
				headerAttributes: {
					style: "text-align:center; font-weight: bold",
					translate:""
				},
				attributes: {
					style: "text-align:center;"
				},
			}, {
				title: "Tên file",
				field: 'name',
				width: 150,
				headerAttributes: {
					style: "text-align:center; font-weight: bold",
					translate:""
				},
				attributes: {
					style: "text-align:left;"
				},
				template: function (dataItem) {
					return "<a href='' ng-click='caller.downloadFile(dataItem)'>" + dataItem.name + "</a>";
				}
			},
			{
				title: "Loại file",
				field: 'appParam',
				width: 150,
				editor: categoryDropDownEditor,
				template: "#=appParam.name#",
				headerAttributes: {
					style: "text-align:center; font-weight: bold",
					translate:""
				},
				attributes: {
					"id": "appFile",
					style: "text-align:left;"
				},
			},
			{
				title: "Xóa",
				headerAttributes: {
					style: "text-align:center; font-weight: bold",
					translate:""
				},
				template: dataItem =>
					'<div class="text-center #=utilAttachDocumentId#""> ' +
					'<button style=" border: none; " class="#=utilAttachDocumentId# icon_table" uib-tooltip="Xóa" ng-click="caller.removeRowFile(dataItem)" translate>  ' +
					'<i style="color: steelblue;" class="fa fa-trash" ria-hidden="true"></i>' +
					'</button>' +
					'</div>',
				width: 20,
				field: "acctions"
			}]
		});
     }
        
		function loadFileData() {
			requestGoodsService.getFileDrop().then(function (result) {
				vm.dataRropFile = result.plain();
			}, function (errResponse) {
				if (errResponse.status === 409) {
					toastr.error(gettextCatalog.getString("Lỗi không thể xóa"));
				} else {
					toastr.error(gettextCatalog.getString("Xảy ra lỗi khi cập nhật trạng thái"));
				}
			});
		}
		
        vm.onSelect = function (e) {
			if ($("#files")[0].files[0].size > 10485760) {
				toastr.warning("Dung lượng file lớn hơn 10MB");
				setTimeout(function () {
					$(".k-upload-files.k-reset").find("li").remove();
					$(".k-upload-files").remove();
					$(".k-upload-status").remove();
					$(".k-upload.k-header").addClass("k-upload-empty");
					$(".k-upload-button").removeClass("k-state-focused");
				}, 10);
				return;
			}
			
			if ($("#files")[0].files[0].name.split('.').pop() != 'pdf') {
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

			var formData = new FormData();
			jQuery.each(jQuery('#files')[0].files, function (i, file) {
				formData.append('multipartFile' + i, file);
			});

			return $.ajax({
				url: Constant.BASE_SERVICE_URL + "fileservice/uploadATTT",
				type: "POST",
				data: formData,
				enctype: 'multipart/form-data',
				processData: false,
				contentType: false,
				cache: false,
				success: function (data) {
					$.map(e.files, function (file, index) {
						vm.dataFile = $("#requestGoodsFileGrid").data().kendoGrid.dataSource.data();
						var obj = {};
						obj.name = file.name;
						obj.filePath = data[index];
						obj.appParam = {
							code: "choese",
							name: "~~ Chọn ~~"
						};
						vm.dataFile.push(obj);
					})

					refreshGrid(vm.dataFile);

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
        vm.checkInLike=false;
        vm.sendToSign = function () {
        	vm.trinhkyReqGroupName = null;
			vm.trinhkyReqGroupName1 = null;
			vm.trinhkyReqGroupName2 = null;
			vm.trinhkyReqGroupName3 = null;
			vm.trinhkyReqGroupName4 = null;
			vm.ashesName = null;
			vm.ashesName1 = null;
			vm.ashesName2 = null;
			vm.ashesName3 = null;
			vm.ashesName4 = null;
			vm.showView = true;
        	var title = CommonService.translate("Trình ký yêu cầu sản xuất vật tư");
			var windowId = "ORDER_SXVT";
			vm.nameToSign = CommonService.translate("Danh sách yêu cầu trình ký") + "(" + vm.listCheck.length + ")";
			var selectedRow = [];
//			for (var i=0;i<$scope.listCheck.length;i++) {
//				selectedRow.push($scope.listCheck[i].sysGroupId);
//			}
			if(vm.listCheck.length>1){
				vm.showView = false;
			}
			if (vm.listCheck.length === 0) {
				toastr.warning(CommonService.translate("Cần chọn bản ghi trước khi thực hiện!"));
				return;
			} else {
				var check = true;
				for (var i = 0; i < vm.listCheck.length; i++){
					if ((vm.listCheck[i].signState != "1" && vm.listCheck[i].signState != "4")){
						toastr.warning(CommonService.translate("Mã công trình: " + vm.listCheck[i].constructionCode + " không đúng trạng thái để trình ký!"));
						check = false;
						return;
					}
				}
				if (check) {
//					var obj = vm.searchForm;
//					obj.listId = selectedRow;
//					obj.type = "01";
//					obj.reportName = "YeuCauNhapKho_KhongSerial";
					for(var k =0;k<vm.dataCheck.length;k++){
						vm.listGroupCheckId.push(vm.dataCheck[k].sysGroupId);
					}
					for(var m=0;m<vm.listCheck.length;m++){
						vm.listCheck[m].code = vm.listCheck[m].constructionCode;
						if(vm.listGroupCheckId.indexOf(vm.listCheck[m].sysGroupId)!=-1){
							vm.checkInLike=true;
						} else {
							vm.checkInLike=false;
						}
					}
					debugger;
					if(!!vm.checkInLike){
						var teamplateUrl = "coms/requestGoods/signVOCheckPopup.html";
						var dataList = vm.listCheck;
						CommonService.populatePopupVofice(teamplateUrl, title, '01', dataList, vm, windowId, false, '75%', '75%');
					} else {
						var teamplateUrl = "coms/requestGoods/signVOPopup.html";
						var dataList = vm.listCheck;
						CommonService.populatePopupVofice(teamplateUrl, title, '01', dataList, vm, windowId, false, '75%', '75%');
					}
				}
			}
		}
        
     // Xóa file đính kèm
		vm.removeRowFile = removeRowFile;
		function removeRowFile(dataItem) {
			confirm('Xác nhận xóa', function () {
//				$('#requestGoodsFileGrid').data('kendoGrid').dataSource.remove(dataItem);
				vm.dataFile.splice(dataItem, 1);
			})
		}
		
		//Trưởng ban ĐHTC
		var changeReqGroupTrinhky1= "";
    	vm.trinhkyReqGroupNameOptions1 = {
			dataTextField: "fullName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
				vm.inputSign.reqGroupId1 = dataItem.sysUserId;
				vm.inputSign.reqGroupName1 = dataItem.fullName;
				vm.inputSign.email1 = dataItem.email;
				vm.trinhkyReqGroupName1 = dataItem.fullName;
				changeReqGroupTrinhky1 = dataItem.fullName;
//				genAshename1(dataItem);
				dataItem.keySearch = dataItem.email;
	    		requestGoodsService.getRoleByEmail(dataItem).then(function(result){
	    			if (result.error) {
	                    toastr.error(result.error);
	                    return;
	                }
	    			vm.inputSign.reqGroupName1 = result[0].adOrgName;
					vm.inputSign.email1 = result[0].strEmail;
					vm.inputSign.sysRoleId1 = result[0].sysRoleId;
					vm.inputSign.sysRoleName1 = result[0].sysRoleName;
					vm.inputSign.adOrgId1 = result[0].adOrgId;
					vm.ashesName1 = result[0].sysRoleName;
	        	}, function() {
	    			toastr.error("Lỗi rồi !");
	    		});
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("goodsPlanService/filterSysUser").post(
                            {keySearch: vm.trinhkyReqGroupName1,
                                pageSize: vm.trinhkyReqGroupNameOptions1.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:50%;">Mã người ký</span>' +
			'<span style="display:inline-block;width:50%;">Tên người ký</span>' +
			'</div>',
			template: '<div class="row" ><div class="col-xs-5" style="padding: 0px;float:left">#: data.employeeCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != changeReqGroupTrinhky1) {
					vm.inputSign.reqGroupId1 = null;
					vm.inputSign.reqGroupName1 = null;
					vm.inputSign.email1 = null;
					$(".trinhky-approveGroup1").val("");
				}
			},
			ignoreCase: false
		};
    	
//    	function genAshename1(data){
//    		data.keySearch = data.email;
//    		requestGoodsService.getRoleByEmail(data).then(function(result){
//    			if (result.error) {
//                    toastr.error(result.error);
//                    return;
//                }
//    			vm.inputSign.reqGroupName1 = result[0].adOrgName;
//				vm.inputSign.email1 = result[0].strEmail;
//				vm.inputSign.sysRoleId1 = result[0].sysRoleId;
//				vm.inputSign.sysRoleName1 = result[0].sysRoleName;
//				vm.inputSign.adOrgId1 = result[0].adOrgId;
//				vm.ashesName1 = result[0].sysRoleName;
//        	}, function() {
//    			toastr.error("Lỗi rồi !");
//    		});
//    	}
    	
    	//Giám đốc chi nhánh
    	var changeReqGroupTrinhky2= "";
    	vm.trinhkyReqGroupNameOptions2 = {
			dataTextField: "fullName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
				vm.inputSign.reqGroupId2 = dataItem.sysUserId;
				vm.inputSign.reqGroupName2 = dataItem.fullName;
				vm.inputSign.email2 = dataItem.email;
				vm.trinhkyReqGroupName2 = dataItem.fullName;
				changeReqGroupTrinhky2 = dataItem.fullName;
//				genAshename2(dataItem);
				dataItem.keySearch = dataItem.email;
	    		requestGoodsService.getRoleByEmail(dataItem).then(function(result){
	    			if (result.error) {
	                    toastr.error(result.error);
	                    return;
	                }
	    			vm.inputSign.reqGroupName2 = result[0].adOrgName;
					vm.inputSign.email2 = result[0].strEmail;
					vm.inputSign.sysRoleId2= result[0].sysRoleId;
					vm.inputSign.sysRoleName2 = result[0].sysRoleName;
					vm.inputSign.adOrgId2 = result[0].adOrgId;
					vm.ashesName2 = result[0].sysRoleName;
	        	}, function() {
	    			toastr.error("Lỗi rồi !");
	    		});
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("goodsPlanService/filterSysUser").post(
                            {keySearch: vm.trinhkyReqGroupName2,
                                pageSize: vm.trinhkyReqGroupNameOptions2.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:50%;">Mã người ký</span>' +
			'<span style="display:inline-block;width:50%;">Tên người ký</span>' +
			'</div>',
			template: '<div class="row" ><div class="col-xs-5" style="padding: 0px;float:left">#: data.employeeCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != changeReqGroupTrinhky2) {
					vm.inputSign.reqGroupId2 = null;
					vm.inputSign.reqGroupName2 = null;
					vm.inputSign.email2 = null;
					$(".trinhky-approveGroup2").val("");
				}
			},
			ignoreCase: false
		};
    	
//    	function genAshename2(data){
//    		data.keySearch = data.email;
//    		requestGoodsService.getRoleByEmail(data).then(function(result){
//    			if (result.error) {
//                    toastr.error(result.error);
//                    return;
//                }
//    			vm.inputSign.reqGroupName2 = result[0].adOrgName;
//				vm.inputSign.email2 = result[0].strEmail;
//				vm.inputSign.sysRoleId2= result[0].sysRoleId;
//				vm.inputSign.sysRoleName2 = result[0].sysRoleName;
//				vm.inputSign.adOrgId2 = result[0].adOrgId;
//				vm.ashesName2 = result[0].sysRoleName;
//        	}, function() {
//    			toastr.error("Lỗi rồi !");
//    		});
//    	}
    	
    	//Giám đốc TTKT
    	var changeReqGroupTrinhky= "";
    	vm.trinhkyReqGroupNameOptions = {
			dataTextField: "fullName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
				vm.inputSign.reqGroupId = dataItem.sysUserId;
				vm.inputSign.reqGroupName = dataItem.fullName;
				vm.inputSign.email = dataItem.email;
				vm.trinhkyReqGroupName = dataItem.fullName;
				changeReqGroupTrinhky = dataItem.fullName;
//				genAshename(dataItem);
				dataItem.keySearch = dataItem.email;
	    		requestGoodsService.getRoleByEmail(dataItem).then(function(result){
	    			if (result.error) {
	                    toastr.error(result.error);
	                    return;
	                }
	    			vm.inputSign.reqGroupName = result[0].adOrgName;
					vm.inputSign.email = result[0].strEmail;
					vm.inputSign.sysRoleId= result[0].sysRoleId;
					vm.inputSign.sysRoleName = result[0].sysRoleName;
					vm.inputSign.adOrgId = result[0].adOrgId;
					vm.ashesName = result[0].sysRoleName;
	        	}, function() {
	    			toastr.error("Lỗi rồi !");
	    		});
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("goodsPlanService/filterSysUser").post(
                            {keySearch: vm.trinhkyReqGroupName,
                                pageSize: vm.trinhkyReqGroupNameOptions.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:50%;">Mã người ký</span>' +
			'<span style="display:inline-block;width:50%;">Tên người ký</span>' +
			'</div>',
			template: '<div class="row" ><div class="col-xs-5" style="padding: 0px;float:left">#: data.employeeCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != changeReqGroupTrinhky) {
					vm.inputSign.reqGroupId = null;
					vm.inputSign.reqGroupName = null;
					vm.inputSign.email = null;
					$(".trinhky-approveGroup").val("");
				}
			},
			ignoreCase: false
		};
    	
//    	function genAshename(data){
//    		data.keySearch = data.email;
//    		requestGoodsService.getRoleByEmail(data).then(function(result){
//    			if (result.error) {
//                    toastr.error(result.error);
//                    return;
//                }
//    			vm.inputSign.reqGroupName = result[0].adOrgName;
//				vm.inputSign.email = result[0].strEmail;
//				vm.inputSign.sysRoleId= result[0].sysRoleId;
//				vm.inputSign.sysRoleName = result[0].sysRoleName;
//				vm.inputSign.adOrgId = result[0].adOrgId;
//				vm.ashesName = result[0].sysRoleName;
//        	}, function() {
//    			toastr.error("Lỗi rồi !");
//    		});
//    	}
    	
    	//Giám đốc TTKV
    	var changeReqGroupTrinhky4= "";
    	vm.trinhkyReqGroupNameOptions4 = {
			dataTextField: "fullName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
				vm.inputSign.reqGroupId4 = dataItem.sysUserId;
				vm.inputSign.reqGroupName4 = dataItem.fullName;
				vm.inputSign.email4 = dataItem.email;
				vm.trinhkyReqGroupName4 = dataItem.fullName;
				changeReqGroupTrinhky4 = dataItem.fullName;
//				genAshename4(dataItem);
				dataItem.keySearch = dataItem.email;
	    		requestGoodsService.getRoleByEmail(dataItem).then(function(result){
	    			if (result.error) {
	                    toastr.error(result.error);
	                    return;
	                }
	    			vm.inputSign.reqGroupName4 = result[0].adOrgName;
					vm.inputSign.email4 = result[0].strEmail;
					vm.inputSign.sysRoleId4 = result[0].sysRoleId;
					vm.inputSign.sysRoleName4 = result[0].sysRoleName;
					vm.inputSign.adOrgId4 = result[0].adOrgId;
					vm.ashesName4 = result[0].sysRoleName;
	        	}, function() {
	    			toastr.error("Lỗi rồi !");
	    		});
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("goodsPlanService/filterSysUser").post(
                            {keySearch: vm.trinhkyReqGroupName4,
                                pageSize: vm.trinhkyReqGroupNameOptions4.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:50%;">Mã người ký</span>' +
			'<span style="display:inline-block;width:50%;">Tên người ký</span>' +
			'</div>',
			template: '<div class="row" ><div class="col-xs-5" style="padding: 0px;float:left">#: data.employeeCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != changeReqGroupTrinhky4) {
					vm.inputSign.reqGroupId4 = null;
					vm.inputSign.reqGroupName4 = null;
					vm.inputSign.email4 = null;
					$(".trinhky-approveGroup4").val("");
				}
			},
			ignoreCase: false
		};
    	
//    	function genAshename4(data){
//    		data.keySearch = data.email;
//    		requestGoodsService.getRoleByEmail(data).then(function(result){
//    			if (result.error) {
//                    toastr.error(result.error);
//                    return;
//                }
//    			vm.inputSign.reqGroupName4 = result[0].adOrgName;
//				vm.inputSign.email4 = result[0].strEmail;
//				vm.inputSign.sysRoleId4 = result[0].sysRoleId;
//				vm.inputSign.sysRoleName4 = result[0].sysRoleName;
//				vm.inputSign.adOrgId4 = result[0].adOrgId;
//				vm.ashesName4 = result[0].sysRoleName;
//        	}, function() {
//    			toastr.error("Lỗi rồi !");
//    		});
//    	}
    	
    	vm.signSave = signSave;
    	function signSave() {
    	var element = $(".frightdetail");
    	kendo.ui.progress(element,true);
    	if(vm.checkInLike){
    		if(vm.inputSign.reqGroupId1 == "" || vm.inputSign.reqGroupId1== null){
       			$(".trinhky-approveGroup1").focus();
       			kendo.ui.progress(element,false);
       			toastr.warning("Trưởng ban điều hành thi công không được để trống !");
       			return;
       		  }
       		 if(vm.inputSign.reqGroupId2 == "" || vm.inputSign.reqGroupId2== null){
       	        $(".trinhky-approveGroup2").focus();
       	        kendo.ui.progress(element,false);
       	        toastr.warning("Giám đốc chi nhánh không được để trống !");
       	        return;
       	     }
       		if(vm.ashesName1 == "" || vm.ashesName1== null){
      			 $(".ashes-approveGroup1").focus();
      			 kendo.ui.progress(element,false);
      			 toastr.warning("Vai trò không được để trống !");
      			 return;
      		 }
      		if(vm.ashesName2 == "" || vm.ashesName2== null){
     			 $(".ashes-approveGroup2").focus();
     			 kendo.ui.progress(element,false);
     			 toastr.warning("Vai trò không được để trống !");
     			 return;
     		 }
      		for(var i=0; i< vm.listCheck.length; i++){
       			vm.listCheck[i].listSignVoffice = [];
       			vm.listCheck[i].reportName = "File_trinh_ky_CN";
       			vm.listCheck[i].objectCode = vm.listCheck[i].code;
       			vm.listCheck[i].objectId = vm.listCheck[i].requestGoodsId;
       			vm.listCheck[i].type = "60";
       	  		vm.listCheck[i].listSignVoffice.push({
       	        	sysUserId : vm.inputSign.reqGroupId1,
       	        	fullName : vm.inputSign.reqGroupName1,
       	        	email : vm.inputSign.email1,
       	        	sysRoleId : vm.inputSign.sysRoleId1,
       	        	sysRoleName : vm.inputSign.sysRoleName1,
       	        	adOrgId :  vm.inputSign.adOrgId1
       	        });
       	  		vm.listCheck[i].listSignVoffice.push({
       	        	sysUserId : vm.inputSign.reqGroupId2,
       	        	fullName : vm.inputSign.reqGroupName2,
       	        	email : vm.inputSign.email2,
       	        	sysRoleId : vm.inputSign.sysRoleId2,
       	        	sysRoleName : vm.inputSign.sysRoleName2,
       	        	adOrgId :  vm.inputSign.adOrgId2
       	        });
       	  		
//	       	  	if(vm.listCheck[i].lstFileAttachDk.length!=0){
//	   	  			var listFile = vm.listCheck[i].lstFileAttachDk;
//	   	  			for(var j=0;j<listFile.length;j++){
//	   	  				vm.listCheck[i].lstFileAttach.push({
//	   	  					fileName : listFile[j].name,
//	//   	  					fileSign : 2,
//	   	  					path : listFile[j].filePath
//	   	  				});
//	   	  			}
//	   	  		}
       		 }
    	} else {
    		if(vm.inputSign.reqGroupId == "" || vm.inputSign.reqGroupId== null){
       	        $(".trinhky-approveGroup").focus();
       	        toastr.warning("Giám đốc trung tâm kỹ thuật không được để trống !");
       	        kendo.ui.progress(element,false);
       	        return;
       	     }
       		 if(vm.inputSign.reqGroupId4 == "" || vm.inputSign.reqGroupId4== null){
       			$(".trinhky-approveGroup4").focus();
       			kendo.ui.progress(element,false);
       			toastr.warning("Giám đốc trung tâm khu vực không được để trống !");
       			return;
       		  }
       		if(vm.ashesName == "" || vm.ashesName== null){
      			 $(".ashes-approveGroup").focus();
      			 kendo.ui.progress(element,false);
      			 toastr.warning("Vai trò không được để trống !");
      			 return;
      		 }
     		 if(vm.ashesName4 == "" || vm.ashesName4== null){
     			 $(".ashes-approveGroup4").focus();
     			 kendo.ui.progress(element,false);
     			 toastr.warning("Vai trò không được để trống !");
     			 return;
     		 }
     		for(var i=0; i< vm.listCheck.length; i++){
       			vm.listCheck[i].listSignVoffice = [];
       			vm.listCheck[i].reportName = "File_trinh_ky_TTKT";
       			vm.listCheck[i].objectCode = vm.listCheck[i].code;
       			vm.listCheck[i].objectId = vm.listCheck[i].requestGoodsId;
       			vm.listCheck[i].type = "60";
       	  		vm.listCheck[i].listSignVoffice.push({
       	        	sysUserId : vm.inputSign.reqGroupId,
       	        	fullName : vm.inputSign.reqGroupName,
       	        	email : vm.inputSign.email,
       	        	sysRoleId : vm.inputSign.sysRoleId,
       	        	sysRoleName : vm.inputSign.sysRoleName,
       	        	adOrgId :  vm.inputSign.adOrgId,
       	        	createdUserId : vm.listCheck[i].createdUserId
       	        });
       	  		vm.listCheck[i].listSignVoffice.push({
       	        	sysUserId : vm.inputSign.reqGroupId4,
       	        	fullName : vm.inputSign.reqGroupName4,
       	        	email : vm.inputSign.email4,
       	        	sysRoleId : vm.inputSign.sysRoleId4,
       	        	sysRoleName : vm.inputSign.sysRoleName4,
       	        	adOrgId :  vm.inputSign.adOrgId4
       	        });
       	  		
//       	  		if(vm.listCheck[i].lstFileAttachDk.length!=0){
//       	  			var listFile = vm.listCheck[i].lstFileAttachDk;
//       	  			for(var j=0;j<listFile.length;j++){
//       	  				vm.listCheck[i].lstFileAttach.push({
//       	  					fileName : listFile[j].name,
////       	  					fileSign : 2,
//       	  					path : listFile[j].filePath
//       	  				});
//       	  			}
//       	  		}
       	  		
       		 }
    	}
    	
    	requestGoodsService.signVofficeYCSXVT(vm.listCheck).then(function(d){
			if(d.error){
				toastr.error(d.error);
				return;
				
			}
			kendo.ui.progress(element,false);
			toastr.success("Trình ký thành công!");
			CommonService.dismissPopup();
			doSearch();
			vm.listCheck =[];
		}, function() {
			toastr.error('Error');
			kendo.ui.progress(element,false);
            $scope.isProcessing = false;
		});
			
		}
    	
    	vm.preview = preview;
		function preview() {
			var obj = {};
			for(var i=0; i < vm.listCheck.length; i++){
				obj.requestGoodsId = vm.listCheck[i].requestGoodsId;
			}
			if(vm.checkInLike){
				obj.reportName="File_trinh_ky_CN";
			} else {
				obj.reportName="File_trinh_ky_TTKT";
			}
			obj.reportType="PDF";
	        CommonService.previewDocSign(obj);
		}
        //Huy-end
		
		/**Hoangnh start 05082019**/
		var changeAshes1 = "";
    	vm.ashesNameOptions1 = {
			dataTextField: "adOrgName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
				vm.inputSign.reqGroupName1 = dataItem.adOrgName;
				vm.inputSign.email1 = dataItem.strEmail;
				vm.inputSign.sysRoleId1 = dataItem.sysRoleId;
				vm.inputSign.sysRoleName1 = dataItem.sysRoleName;
				vm.inputSign.adOrgId1 = dataItem.adOrgId;
				dataItem.fullName1 = vm.trinhkyReqGroupName;
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("signVofficeRsServiceRest/signVoffice/getRoleByEmail").post(
                            {keySearch: vm.inputSign.email1,
                                pageSize: vm.ashesNameOptions1.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:100%;text-align:center;">Vai trò người ký</span>' +
			'</div>',
			template: '<div class="row" >#: data.adOrgName #</div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != vm.inputSign.email1) {
				}
			},
		};
    	
    	var changeAshes2 = "";
    	vm.ashesNameOptions2 = {
			dataTextField: "adOrgName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
				vm.inputSign.reqGroupName2 = dataItem.adOrgName;
				vm.inputSign.email2 = dataItem.strEmail;
				vm.inputSign.sysRoleId2 = dataItem.sysRoleId;
				vm.inputSign.sysRoleName2 = dataItem.sysRoleName;
				vm.inputSign.adOrgId2 = dataItem.adOrgId;
				dataItem.fullName2 = vm.trinhkyReqGroupName;
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("signVofficeRsServiceRest/signVoffice/getRoleByEmail").post(
                            {keySearch: vm.inputSign.email2,
                                pageSize: vm.ashesNameOptions2.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:100%;text-align:center;">Vai trò người ký</span>' +
			'</div>',
			template: '<div class="row" >#: data.adOrgName #</div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != vm.inputSign.email2) {
				}
			},
		};
    	
    	var changeAshes = "";
    	vm.ashesNameOptions = {
			dataTextField: "adOrgName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
				vm.inputSign.reqGroupName = dataItem.adOrgName;
				vm.inputSign.email = dataItem.strEmail;
				vm.inputSign.sysRoleId = dataItem.sysRoleId;
				vm.inputSign.sysRoleName = dataItem.sysRoleName;
				vm.inputSign.adOrgId = dataItem.adOrgId;
				dataItem.fullName = vm.trinhkyReqGroupName;
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("signVofficeRsServiceRest/signVoffice/getRoleByEmail").post(
                            {keySearch: vm.inputSign.email,
                                pageSize: vm.ashesNameOptions.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:100%;text-align:center;">Vai trò người ký</span>' +
			'</div>',
			template: '<div class="row" >#: data.adOrgName #</div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != vm.inputSign.email) {
				}
			},
		};
    	
    	var changeAshes4 = "";
    	vm.ashesNameOptions4 = {
			dataTextField: "adOrgName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
				vm.inputSign.reqGroupName4 = dataItem.adOrgName;
				vm.inputSign.email4 = dataItem.strEmail;
				vm.inputSign.sysRoleId4 = dataItem.sysRoleId;
				vm.inputSign.sysRoleName4 = dataItem.sysRoleName;
				vm.inputSign.adOrgId4 = dataItem.adOrgId;
				dataItem.fullName4 = vm.trinhkyReqGroupName;
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("signVofficeRsServiceRest/signVoffice/getRoleByEmail").post(
                            {keySearch: vm.inputSign.email4,
                                pageSize: vm.ashesNameOptions4.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:100%;text-align:center;">Vai trò người ký</span>' +
			'</div>',
			template: '<div class="row" >#: data.adOrgName #</div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != vm.inputSign.email4) {
				}
			},
		};
    	
    	/**Hoangnh end 05082019**/
// end
    }
})
();