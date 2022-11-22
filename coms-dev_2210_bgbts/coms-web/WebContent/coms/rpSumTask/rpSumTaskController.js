(function () {
    'use strict';
    var controllerId = 'rpSumTaskController';
    angular.module('MetronicApp').controller(controllerId, rpDailyTaskController);

    function rpDailyTaskController($scope, $rootScope, $timeout, gettextCatalog, 
    		$filter, kendoConfig, $kWindow, CommonService, 
    		PopupConst, Restangular, RestEndpoint, Constant, workItemService, htmlCommonService, $http) {
        var vm = this;

        var record = 0;
        vm.rpSumTaskSearch = {};
        vm.String = "Quản lý công trình > Báo cáo > Báo cáo tổng hợp";

        vm.showImage = showImage;
        vm.imageSelected = {};
        vm.listImage = [];
        vm.doSearch = doSearch;
        vm.openUser = openUser;
        vm.onSaveSysUser = onSaveSysUser;
        vm.checkErrDate = checkErrDate;
        vm.openConstruction = openConstruction;
        vm.clearUser = clearUser;
        vm.clearSearchDate = clearSearchDate;
        vm.clearConstructionType = clearConstructionType;
        vm.clearProvince = clearProvince;
        vm.clearConstruction = clearConstruction;
        vm.clearWorkItem = clearWorkItem;
        vm.changeImage = changeImage;
        vm.clearStationCode = clearStationCode;
        vm.iconDiss = true;
        vm.hidenIcon = hidenIcon;
        vm.showIcon = showIcon;
		vm.exportFile = exportFile;
		vm.openCatProvincePopup = openCatProvincePopup;
		vm.onSaveCatProvince = onSaveCatProvince;
	    vm.openCatConstructionTypePopup = openCatConstructionTypePopup;
	    vm.onSaveCatConstructionType = onSaveCatConstructionType;
	    vm.openCatStationPopup = openCatStationPopup;
	    vm.onSaveCatStation = onSaveCatStation;
	    vm.openComsConstructionPopup = openComsConstructionPopup;
	    vm.onSaveComsConstruction = onSaveComsConstruction;
	    vm.openComsWorkItemPopup = openComsWorkItemPopup;
	    vm.onSaveComsWorkItem = onSaveComsWorkItem;
	    
		function hidenIcon() {
            vm.iconDiss = false;
        }
        function showIcon() {
            vm.iconDiss = true;
        }
        
        initFormData();
        function initFormData() {
        	vm.newDate = new Date();
        	vm.newDate.setMonth(vm.newDate.getMonth() - 1);
        	var date = new Date();
        	var start = new Date(date.getFullYear(), date.getMonth(), 1);
        	var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
        	vm.rpSumTaskSearch.ketthuc_trienkhai_tu = htmlCommonService.formatDate(start);
        	vm.rpSumTaskSearch.ketthuc_trienkhai_den = htmlCommonService.formatDate(end);
        	
        	fillDataTable();
        }

        vm.patternSignerOptions = {
            dataTextField: "fullName",
            placeholder: "Nhập mã hoặc tên người thực hiện",
            open: function (e) {
                vm.isSelect = false;
            },
            select: function (e) {
                vm.isSelect = true;
                data = this.dataItem(e.item.index());
                vm.rpSumTaskSearch.userName = data.fullName;
                vm.rpSumTaskSearch.sysUserId = data.sysUserId;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.isSelect = false;
                        return Restangular.all("sysUserCOMSRsService/getForAutoCompleteInSign").post({ pageSize: 10, page: 1, fullName: $("#signerGroup").val().trim() }).then(function (response) {
                            options.success(response);
                            if (response == [] || $("#signerGroup").val().trim() == "") {
                                vm.rpSumTaskSearch.userName = null;
                                vm.rpSumTaskSearch.sysUserId = null;
                            }
                        }).catch(function (err) {
                            vm.rpSumTaskSearch.userName = null;
                            vm.rpSumTaskSearch.sysUserId = null;
                        });
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-6 text-header-auto border-right-ccc">Mã nhân viên</p>' +
                '<p class="col-md-6 text-header-auto">Họ tên</p>' +
                '</div>',
            template: '<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.employeeCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
            change: function (e) {
            	if (!vm.isSelect) {
                    vm.rpSumTaskSearch.userName = null;
                    vm.rpSumTaskSearch.sysUserId = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.rpSumTaskSearch.userName = null;
                    vm.rpSumTaskSearch.sysUserId = null;
                }
            }
        };

        vm.patternConstructOptions = {
            dataTextField: "name",
            placeholder: "Nhập mã hoặc tên loại công trình",
            open: function (e) {
                vm.isSelect = false;
            },
            select: function (e) {
                vm.isSelect = true;
                data = this.dataItem(e.item.index());
                vm.rpSumTaskSearch.constructiontypename = data.name;
                vm.rpSumTaskSearch.catConstructionTypeId = data.catConstructionTypeId;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.isSelect = false;
                        return Restangular.all("kpiLogMobileService/kpiLogMobile/getConstrTypeForAutoComplete").post({ pageSize: 10, page: 1, name:vm.rpSumTaskSearch.constructiontypename}).then(function (response) {
                            options.success(response.data);
                            var check = response == [] || $("#constructionType").val().trim() == "";
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-6 text-header-auto border-right-ccc">Mã loại công trình</p>' +
                '<p class="col-md-6 text-header-auto">Tên loại công trình</p>' +
                '</div>',
            template: '<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.code #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (!vm.isSelect) {
                    vm.rpSumTaskSearch.constructiontypename = null;
                    vm.rpSumTaskSearch.catConstructionTypeId = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.rpSumTaskSearch.constructiontypename = null;
                    vm.rpSumTaskSearch.catConstructionTypeId = null;
                }
            }
        };

        vm.provinceOptions = {
            dataTextField: "name",
            dataValueField: "id",
            placeholder: "Nhập mã hoặc tên tỉnh",
            select: function (e) {
                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.rpSumTaskSearch.provincename = dataItem.name;
                vm.rpSumTaskSearch.provinceCode = dataItem.code;
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
                            name: vm.rpSumTaskSearch.provincename,
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
                    vm.rpSumTaskSearch.provincename = null;
                    vm.rpSumTaskSearch.provinceCode = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.rpSumTaskSearch.provincename = null;
                    vm.rpSumTaskSearch.provinceCode = null;
                }
            }
        }

        vm.constructionOptions = {
            dataTextField: "code",
            dataValueField: "constructionId",
            placeholder: "Nhập mã hoặc tên công trình",
            select: function (e) {
                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.rpSumTaskSearch.constructionName = dataItem.code;
                vm.rpSumTaskSearch.constructionId = dataItem.constructionId;
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
                    vm.rpSumTaskSearch.constructionName = null;
                    vm.rpSumTaskSearch.constructionId = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.rpSumTaskSearch.constructionName = null;
                    vm.rpSumTaskSearch.constructionId = null;
                }
            }
        }

        vm.workItemOptions = {
            dataTextField: "name",
            dataValueField: "workItemId",
            placeholder: "Nhập mã hoặc tên hạng mục",
            select: function (e) {
                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.rpSumTaskSearch.workItemName = dataItem.code;
                vm.rpSumTaskSearch.workItemId = dataItem.workItemId;
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
                        return Restangular.all("constructionTaskService/constructionTask/rpDailyTaskWorkItems").post({
                            keySearch: vm.rpSumTaskSearch.workItemName,
                            pageSize: vm.workItemOptions.pageSize,
                            constructionId: vm.rpSumTaskSearch.constructionId,
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
                '<p class="col-md-6 text-header-auto border-right-ccc">Mã hạng mục</p>' +
                '<p class="col-md-6 text-header-auto">Tên hạng mục</p>' +
                '</div>',
            template: '<div class="row" ><div class="col-xs-5" style="float:left;word-wrap: break-word;">#: data.code #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden">#: data.name # </div> </div>',
            change: function (e) {
                if (!vm.isSelect) {
                    vm.rpSumTaskSearch.workItemName = null;
                    vm.rpSumTaskSearch.workItemId = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.rpSumTaskSearch.workItemName = null;
                    vm.rpSumTaskSearch.workItemId = null;
                }
            }
        }

        vm.stationCodeOptions = {
            dataTextField: "code",
            dataValueField: "stationId",
            placeholder: "Nhập mã trạm",
            select: function (e) {
                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.rpSumTaskSearch.stationCode = dataItem.code;
                vm.rpSumTaskSearch.stationId = dataItem.catStationId;
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
                        return Restangular.all("constructionService/getStationForAutoComplete").post({
                            keySearch: vm.rpSumTaskSearch.stationCode,
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
                '<p class="col-md-12 text-header-auto border-right-ccc">Mã trạm</p>' +
                '</div>',
            template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.code #</div></div>',
            change: function (e) {
            	if (!vm.isSelect) {
                    vm.rpSumTaskSearch.stationCode = null;
                    vm.rpSumTaskSearch.stationId = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.rpSumTaskSearch.stationCode = null;
                    vm.rpSumTaskSearch.stationId = null;
                }
            }
        }
        
        $(document).ready(function(){
        	kendo.ui.progress($("#rpSumTaskGrid"), true);
        });
        function fillDataTable(){
        vm.rpSumTaskGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: true,
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
                    	$("#appCount").text("" + response.total);
                        vm.count = response.total;
                        if(vm.count){
							kendo.ui.progress($("#rpSumTaskGrid"), false); 
						} else {
							kendo.ui.progress($("#rpSumTaskGrid"), false);
						}
                        return response.total;
                    },
                    data: function (response) {
                        var list = response.data;
                        return response.data;
                    },
                },
                transport: {
                    read: {
                        url: Constant.BASE_SERVICE_URL + "constructionTaskService/constructionTask/rpSumTask",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.rpSumTaskSearch.page = options.page;
                        vm.rpSumTaskSearch.pageSize = options.pageSize;
                        return JSON.stringify(vm.rpSumTaskSearch);
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
                    width: '50px',
                    template: function () {
                        return ++record;
                    },
                    columnMenu: false,
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    },
                },{
                    title: "Đơn vị thực hiện",
					field: 'sysGroupName',
                    width: '220px',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    },
                }, 
                {
                    title: "Người thực hiện",
					field: 'fullName',
                    width: '150px',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    },
                }, {
                    title: "Hợp đồng",
					field: 'contractCode',
                    width: '150px',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    },
                }, {
                    title: "Tỉnh",
					field: 'provinceCode',
                    width: '50px',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    },
                }, {
                    title: "Mã trạm",
					field: 'stationCode',
                    width: '200px',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    },
                }, {
                    title: "Mã công trình",
					field: 'constructionCode',
                    width: '200px',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    },
                }, {
                    title: "Loại công trình",
					field: 'constructionTypeName',
                    width: '130px',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    }
                }, {
                    title: "Hạng mục",
					field: 'workItemName',
                    width: '200px',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    },
                }, {
                    title: "Giá trị sản lượng",
					field: 'quantity',
                    width: '150px',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    },
                }, {
                    title: "Hình thức thi công",
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    columns: [
                        {
                            title: "TC TT",
        					field: 'tctt',
                            width: '50px',
                            headerAttributes: { style: "text-align:center;font-weight: bold;" },
                            attributes: {
                                style: "text-align:center;"
                            },
                            format: "{0:n2}"
                        }, {
                            title: "Giám sát",
        					field: 'giam_sat',
                            width: '80px',
                            headerAttributes: { style: "text-align:center;font-weight: bold;" },
                            attributes: {
                                style: "text-align:center;"
                            },
                            format: "{0:n2}"
                        }
                    ]
                }, {
                    title: "Ngày kết thúc triển khai",
					field: 'ketthuc_trienkhai',
                    width: '160px',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    },
                }, {
                    title: "Tình trạng thi công",
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    columns: [
                        {
                            title: "Thi công xong",
        					field: 'thicong_xong',
                            width: '100px',
                            headerAttributes: { style: "text-align:center;font-weight: bold;" },
                            attributes: {
                                style: "text-align:center;"
                            },
                            format: "{0:n5}"
                        }, {
                            title: "Đang thi công",
                            headerAttributes: { style: "text-align:center;font-weight: bold;" },
                            columns: [
                                {
                                    title: "Số hạng mục",
                					field: 'dang_thicong',
                                    width: '100px',
                                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                    attributes: {
                                        style: "text-align:center;"
                                    },
                                    format: "{0:n2}"
                                }, {
                                    title: "Lũy kế thực hiện",
                					field: 'luy_ke',
                                    width: '130px',
                                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                    attributes: {
                                        style: "text-align:center;"
                                    },
                                    format: "{0:n2}"
                                }]
                        }, {
                            title: "Chưa thi công",
        					field: 'chua_thicong',
                            width: '100px',
                            headerAttributes: { style: "text-align:center;font-weight: bold;" },
                            attributes: {
                                style: "text-align:center;"
                            },
                            format: "{0:n5}"
                        }, {
                            title: "Vướng",
        					field: 'vuong',
                            width: '100px',
                            headerAttributes: { style: "text-align:center;font-weight: bold;" },
                            attributes: {
                                style: "text-align:center;"
                            },
                            format: "{0:n5}"
                        }
                    ]
                }, {
                    title: "Thời gian thi công",
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    columns: [
                        {
                            title: "Ngày bắt đầu",
        					field: 'batdau_thicong',
                            width: '100px',
                            headerAttributes: { style: "text-align:center;font-weight: bold;" },
                            attributes: {
                                style: "text-align:center;"
                            },
                            format: "{0:n2}"
                        }, {
                            title: "Ngày kết thúc",
        					field: 'ketthuc_thicong',
                            width: '100px',
                            headerAttributes: { style: "text-align:center;font-weight: bold;" },
                            attributes: {
                                style: "text-align:center;"
                            },
                            format: "{0:n2}"
                        }
                    ]
                },
                {
                    title: "Ảnh thi công",
                    width: '100px',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:center;"
                    },
					template: function (dataItem) {
						var html = '<div class="text-center">' + 
							'<button ng-click="vm.showImage(dataItem)" style=" border: none; background-color: white;" id="updateId" class=" icon_table " translate>' +
							'<i class="fa fa-list-alt" style="color:#e0d014"     aria-hidden="true"></i>' +
							'</button>'
						html += '</div>';
						return html;
					},
                }
            ]
        });
        }
        function showImage(dataItem) {
            workItemService.getListImageById({tableId: dataItem.workItemId, tableName: 'CONSTRUCTION_TASK'}).then(function (data) {
                if (data.listImage.length > 0) {
                    vm.listImage = data.listImage;
                    vm.changeImage(vm.listImage[0]);
                } else {
                    vm.listImage = [];
                }
            }).catch(function (err) {
                vm.listImage = [];
            });
            var templateUrl = "coms/popup/popup-images.html";
            var title = "Hình ảnh";
            var windowId = "DETAIL_WORKITEM_RPDAILYTASK";
            vm.popUpOpen = 'DETAIL_WORKITEM_RPDAILYTASK';
            CommonService.populatePopupCreate(templateUrl, title, data, vm, windowId, false, '1000', 'auto', "null");
        }

        function changeImage(image) {
            vm.imageSelected = image;
        }

        function doSearch() {
            vm.rpSumTaskGrid.dataSource.page(1);
        }

        function openUser() {
            var templateUrl = 'coms/popup/sysUserSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm nhân viên");
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions, data, vm, 'fff', 'ggfd', false, '85%', '85%', 'sysUserSearchController');
        }

        function onSaveSysUser(data) {
            vm.rpSumTaskSearch.userName = data.fullName;
            vm.rpSumTaskSearch.sysUserId = data.sysUserId;
            htmlCommonService.dismissPopup();
            $("#signerGroup").focus();
        };

        function checkErrDate(key) {
        	if (key === 'startDate') {
                var startDate = $('#startDate').val();
                vm.errMessage = '';
                vm.errMessage1 = '';
                var curDate = new Date();
                if (startDate == "") {
                    vm.errMessage1 = CommonService.translate('Ngày không được để trống');
                    $("#startDate").focus();
                    vm.checkDateFrom = false;
                    return vm.errMessage1;
                } else if (kendo.parseDate(startDate, "dd/MM/yyyy") == null) {
                    vm.errMessage1 = CommonService.translate('Ngày không hợp lệ');
                    $("#startDate").focus();
                    vm.checkDateFrom = false;
                    return vm.errMessage1;
                } else if (kendo.parseDate(startDate, "dd/MM/yyyy").getFullYear() > 9999 || kendo.parseDate(startDate, "dd/MM/yyyy").getFullYear() < 1000) {
                    vm.errMessage1 = CommonService.translate('Ngày không hợp lệ');
                    $("#startDate").focus();
                    vm.checkDateFrom = false;
                    return vm.errMessage1;
                }
        	} else {
                var startDate = $('#endDate').val();
                vm.errMessage = '';
                vm.errMessage2 = '';
                var curDate = new Date();
                if (startDate == "") {
                    vm.errMessage2 = CommonService.translate('Ngày không được để trống');
                    $("#endDate").focus();
                    vm.checkDateFrom = false;
                    return vm.errMessage2;
                } else if (kendo.parseDate(startDate, "dd/MM/yyyy") == null) {
                    vm.errMessage2 = CommonService.translate('Ngày không hợp lệ');
                    $("#endDate").focus();
                    vm.checkDateFrom = false;
                    return vm.errMessage2;
                } else if (kendo.parseDate(startDate, "dd/MM/yyyy").getFullYear() > 9999 || kendo.parseDate(startDate, "dd/MM/yyyy").getFullYear() < 1000) {
                    vm.errMessage2 = CommonService.translate('Ngày không hợp lệ');
                    $("#endDate").focus();
                    vm.checkDateFrom = false;
                    return vm.errMessage2;
                }
        	}
        }

        function openConstruction() {
            var templateUrl = 'coms/popup/constructTypePopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm loại công trình");
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions, null, vm, 'fff', 'ggfd', false, '85%', '85%', 'constructTypeSearchController');
        }


        function clearUser() {
            vm.rpSumTaskSearch.userName = null;
            vm.rpSumTaskSearch.sysUserId = null;
            $("#signerGroup").focus();
        }

        function clearSearchDate() {
            vm.rpSumTaskSearch.updateTime = null;
            $("#updateTime").focus();
        }

        function clearConstructionType() {
            vm.rpSumTaskSearch.constructiontypename = null;
            vm.rpSumTaskSearch.catConstructionTypeId = null;
            $("#constructionType").focus();
        }

        function clearProvince() {
            vm.rpSumTaskSearch.provincename = null;
            vm.rpSumTaskSearch.provinceCode = null;
            $("#provincename").focus();
        }

        function clearConstruction() {
            vm.rpSumTaskSearch.constructionName = null;
            vm.rpSumTaskSearch.constructionId = null;
            $("#constructionName").focus();
        }

        function clearWorkItem() {
            vm.rpSumTaskSearch.workItemName = null;
            vm.rpSumTaskSearch.workItemId = null;
            $("#workItemName").focus();
        }

        function clearStationCode() {
            vm.rpSumTaskSearch.stationCode = null;
            vm.rpSumTaskSearch.stationId = null;
            $("#stationCode").focus();
        }
      //HuyPQ-25/08/2018-edit-start
        function exportFile () {
        	function displayLoading(target) {
        	      var element = $(target);
        	      kendo.ui.progress(element, true);
        	      setTimeout(function(){
        	    	  
        	    	  delete vm.rpSumTaskSearch.page;
        	            delete vm.rpSumTaskSearch.pageSize;
        	            return Restangular.all("constructionTaskService/constructionTask/exportExcelRpSumTask").post(vm.rpSumTaskSearch).then(function (d) {
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
      //HuyPQ-edit-end

        function openCatProvincePopup(){
        	var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm tỉnh");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
        }

        function onSaveCatProvince(data){
            vm.rpSumTaskSearch.provincename = data.name;
            vm.rpSumTaskSearch.provinceCode = data.code;
            htmlCommonService.dismissPopup();
            $("#provincename").focus();
	    };

        function openCatConstructionTypePopup(){
        	var templateUrl = 'coms/popup/catConstructionTypeSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm loại công trình");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catConstructionTypeSearchController');
        }

        function onSaveCatConstructionType(data){
            vm.rpSumTaskSearch.constructiontypename = data.name;
            vm.rpSumTaskSearch.catConstructionTypeId = data.catConstructionTypeId;
            htmlCommonService.dismissPopup();
            $("#constructionType").focus();
	    };

        function openCatStationPopup(){
        	var templateUrl = 'coms/popup/catStationSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm mã trạm");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catStationSearchController');
        }

        function onSaveCatStation(data){
            vm.rpSumTaskSearch.stationCode = data.code;
            vm.rpSumTaskSearch.stationId = data.catStationId;
            htmlCommonService.dismissPopup();
            $("#stationCode").focus();
	    };

        function openComsConstructionPopup(){
        	var templateUrl = 'coms/popup/comsConstructionSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm công trình");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','comsConstructionSearchController');
        }

        function onSaveComsConstruction(data){
            vm.rpSumTaskSearch.constructionName = data.code;
            vm.rpSumTaskSearch.constructionId = data.constructionId;
            htmlCommonService.dismissPopup();
            $("#constructionName").focus();
	    };

        function openComsWorkItemPopup(){
        	var templateUrl = 'coms/popup/comsWorkItemSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm hạng mục");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','comsWorkItemSearchController');
        }
		//hoanm1_20180926_start
		vm.openDepartmentTo1=openDepartmentTo1

		function openDepartmentTo1(popUp){
		vm.obj={};
		vm.departmentpopUp=popUp;
		var templateUrl = 'wms/popup/findDepartmentPopUp.html';
		var title = gettextCatalog.getString("Tìm kiếm đơn vị");
		CommonService.populatePopupDept(templateUrl, title, null,null, vm, popUp, 'string', false, '92%','89%');
		}
		
		vm.deprtOptions1 = {
    dataTextField: "text",
    dataValueField:"id",
	placeholder:"Nhập mã hoặc tên đơn vị",
    select: function(e) {
        vm.selectedDept1=true;
        var dataItem = this.dataItem(e.item.index());
        vm.rpSumTaskSearch.sysGroupName = dataItem.text;
        vm.rpSumTaskSearch.sysGroupId = dataItem.id;
    },
    pageSize: 10,
    open: function(e) {
        vm.selectedDept1 = false;
    },
    dataSource: {
        serverFiltering: true,
        transport: {
            read: function(options) {
                vm.selectedDept1=false;
                return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({name:vm.rpSumTaskSearch.sysGroupName,pageSize:vm.deprtOptions1.pageSize}).then(function(response){
                    options.success(response);
                }).catch(function (err) {
                    console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                });
            }
        }
    },
    template:'<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
    change: function(e) {
        if (e.sender.value() === '') {
            vm.rpSumTaskSearch.sysGroupName = null;// thành name
            vm.rpSumTaskSearch.sysGroupId = null;
        }
    },
    ignoreCase: false
}
		vm.onSave = onSave;
		function onSave(data) {
		    if (vm.departmentpopUp === 'dept') {
		        vm.rpSumTaskSearch.sysGroupName = data.text;
		        vm.rpSumTaskSearch.sysGroupId = data.id;
		    }
		}
	//hoanm1_20180926_end

        function onSaveComsWorkItem(data){
            vm.rpSumTaskSearch.workItemName = data.code;
            vm.rpSumTaskSearch.workItemId = data.workItemId;
            htmlCommonService.dismissPopup();
            $("#workItemName").focus();
	    };
		

    }
})();
