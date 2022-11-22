(function () {
    'use strict';
    var controllerId = 'assignHandoverTTKTController';

    angular.module('MetronicApp').controller(controllerId, assignHandoverTTKTController);

    function assignHandoverTTKTController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, htmlCommonService,
                                      kendoConfig, $kWindow,
                                      assignHandoverTTKTService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant) {

        var vm = this;
        vm.String = "Quản lý công trình > Quản lý công trình > Giao việc cho TTKT";
        vm.searchForm = {
        		isDelivered: "0"
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
        vm.remove = remove;
        vm.cancel = cancel;
        vm.exportFile = exportFile;
        vm.closeErrImportPopUp = closeErrImportPopUp;
        vm.showHideColumn = showHideColumn;
        vm.gridColumnShowHideFilter = gridColumnShowHideFilter;
        vm.resetFormFieldConsType = resetFormFieldConsType;
        vm.resetFormFieldDate = resetFormFieldDate;
        vm.resetFormFieldSysGroup = resetFormFieldSysGroup;
        vm.checkErrDate = checkErrDate;
        vm.disableAdd = true;

        vm.listDataCv = [];
        vm.lstDataAssignId=[];
        vm.listAssignIdCheck = [];
        

        function checkDoSearch() {
        	if (!vm.disableSearch) {
        		doSearch();
        	}
        }
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
            initCatConstructionType();
            initDateField();
// edit();
        }

        // init default data for search form
        function initCatConstructionType() {
            assignHandoverTTKTService.getconstructionType().then(function (data) {
                vm.catConstructionTypeDataList = data;
            });
        }

        function initDateField() {
            var date = new Date();
            var from = new Date(date.getFullYear(), date.getMonth() - 1, date.getDate());
            vm.searchForm.dateTo = htmlCommonService.formatDate(date);
            vm.searchForm.dateFrom = htmlCommonService.formatDate(from);
        }

//        vm.iconDiss = true;
//        vm.hidenIcon = hidenIcon;
//        function hidenIcon() {
//            vm.iconDiss = false;
//        }
//        vm.showIcon = function () {
//            vm.iconDiss = true;
//        }
//        
        function doSearch() {
            vm.showDetail = false;
            var grid = vm.assignHandoverTTKTGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
            // console.log(grid.dataSource.data());
        }

        function callbackDoSearch() {
            vm.assignHandoverTTKTGrid.dataSource.page(1);
        }

        var record = 0;
        vm.assignHandoverTTKTGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: true,
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
                        '<button class="approveQuantityDaily btn btn-qlk padding-search-right ng-scope" ng-click="vm.btnGiaoViec()" uib-tooltip="Giao việc" translate="">Giao việc</button>'+
                        '<button class="btn btn-qlk padding-search-right TkQLK " '+
                        'ng-click="vm.importAssignHandover()" uib-tooltip="Import dữ liệu giao việc" translate>Import</button>'+
                        '<div class="btn-group pull-right margin_top_button margin_right10">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()" aria-hidden="true"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.assignHandoverTTKTGrid.columns| filter: vm.gridColumnShowHideFilter">' +
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
                    	var list = response.data;
                    	for(var i=0;i<vm.listDataCv.length;i++){
                    		for(var j=0;j<list.length;j++){
                    			if(vm.listDataCv[i].assignHandoverId == list[j].assignHandoverId){
                    				list[j].selected = true;
                    			}
                    		}
                    	}
                        return response.data;
                    }
                },
                transport: {
                    read: {
                        url: Constant.BASE_SERVICE_URL + RestEndpoint.ASSIGN_HANDOVER_SERVICE_URL + "/doSearchTTKT",
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
            // TODO check field names, resize
            columns: [
            	{
    				title: "<input type='checkbox' id='checkallCv' name='gridchkselectall' ng-click='vm.chkSelectAllCv();' ng-model='vm.chkAllCv' />",
    				template: "<input type='checkbox' id='childcheckCv' name='gridcheckbox' ng-click='vm.handleCheckCv(dataItem)' ng-model='dataItem.selected' ng-hide='dataItem.isDelivered==1'/>",
    				width: "30px",
    				headerAttributes: { style: "text-align:center;" },
    				attributes: { style: "text-align:center;" }
    			},{
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
                    title: "Thao tác",
                    width: '100px',
                    field: "action",
                    template: dataItem =>
                '<div class="text-center">'
//                +
//                '<button class="btn btn-qlk padding-search-right ng-scope" id="attachEdit" ng-disabled="dataItem.isDelivered==1" ' +
//                'ng-click="vm.itemGiaoViec(dataItem)" uib-tooltip="Giao việc" translate="">Giao việc</button>'
//                +
                + '<button style=" border: none; background-color: white;" id="attachEdit" ng-click="vm.itemGiaoViec(dataItem)" ' +
                'ng-hide="dataItem.isDelivered==1" ' +
                'class="icon_table aria-hidden pull-left" ' +
                'uib-tooltip="Giao việc" translate>' +
                '<i class="fa fa-check-circle pull-right" style="color:#68b354" aria-hidden="true"></i>' +
                '</button>'
                +'<button style=" border: none; background-color: white;"' +
                'class="#=appParamId# icon_table"  ng-hide="dataItem.isDelivered==0"  ng-click="vm.remove(dataItem)"  uib-tooltip="Xóa" translate>' +
                '<i class="fa fa-trash" style="color: #337ab7;" aria-hidden="true"></i>' +
                '</button>'
                +
                '</div>'
                },
                {
                    title: "Đơn vị",
                    field: 'sysGroupName',
                    width: '150px',
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
                    width: '100px',
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
                    width: '100px',
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
                    width: '100px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Mã công trình",
                    field: 'constructionCode',
                    width: '150px',
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
                    width: '150px',
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
                    width: '80px',
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
                    title: "Ngày HT giao việc",
                    field: 'companyAssignDate',
                    width: '100px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        return !!dataItem.companyAssignDate
                            ? kendo.toString(kendo.parseDate(dataItem.companyAssignDate), 'dd/MM/yyyy')
                            : "";
                    }
                },
                {
                    title: "File thiết kế",
                    field: 'fileName',
                    width: '150px',
                    headerAttributes: {
                        style: "text-align:center;"
                    },
                    attributes: {
                        style: "text-align:left;white-space: normal"
                    },
                    template: function (dataItem){
                    	return !!dataItem.fileName ? "<a ng-click='vm.downloadFileDesign(dataItem)'>"+ dataItem.fileName +"</a>" : "";
                    }
                }
                
    ]
    });
        vm.importAssignHandover = importAssignHandover;
        function importAssignHandover() {
        	vm.fileImportData = null;
            var templateUrl = "coms/assignHandoverTTKT/assignHandoverImportTTKT.html";
            var title = "Tải lên file giao việc";
            var windowId = "IMPORT_ASSIGN_HANOVER_TTKT";
            CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, true, '1000', '275', null);
        }
        
        vm.downloadTemplateTTKT = downloadTemplateTTKT;
//        	function downloadTemplateTTKT(){
//        		assignHandoverTTKTService.downloadTemplateTTKT({}).then(function (d) {
//                var data = d.plain();
//                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
//            }).catch(function () {
//                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
//            });
//        }
        
        	vm.disableExportExcel = false;
            function downloadTemplateTTKT(){
            	function displayLoading(target) {
            		var obj = {};
                  var element = $(target);
                  kendo.ui.progress(element, true);
                  setTimeout(function(){
                	  
            		if (vm.disableExportExcel)
                    		return;
                    	vm.disableExportExcel = true;
                    	return Restangular.all("assignHandoverService/exportAssignHandoverTTKT").post(obj).then(function (d) {
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
        
        vm.submitImportTTKT = submitImportTTKT;
        function submitImportTTKT() {
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
                url: Constant.BASE_SERVICE_URL + "assignHandoverService/importExcelTTKT?folder=temp",
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
        
        vm.exportExcelErr = function () {
        	Restangular.all("fileservice/exportExcelError").post(vm.objectErr).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
                return;
            });
        }
        
        // Download file đính kèm
        vm.downloadFileDesign = function(dataItem){
        	 window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.fileDesign.filePath;
        }
        
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

                    // VietNT_20190122_start
                    // reset construction field if select after construction
                    if (vm.isSelectConstruction) {
                        resetConstructionAddForm();
                    }
                    // VietNT_end
                    break;
            }
        }

        vm.handleCheckCv = function(dataItem){
        	if(dataItem.selected){
        		vm.listDataCv.push(dataItem);
        	} else {
        		for(var i = 0; i<vm.listDataCv.length;i++){
        			if(dataItem.assignHandoverId == vm.listDataCv[i].assignHandoverId){
        				vm.listDataCv.splice(i,1);
        			}
        		}
        	}
        }
        
        vm.chkSelectAllCv = function(){
        	var grid = $("#assignHandoverTTKTGrid").data("kendoGrid").dataSource.data();
        	if(vm.chkAllCv){
        		for(var k=0;k<grid.length;k++){
        			if(grid[k].isDelivered!=1){
        				grid[k].selected = true;
            			vm.listDataCv.push(grid[k]);
        			}
        		}
        	} else {
        		for(var k=0;k<grid.length;k++){
        			grid[k].selected = false;
        		}
        		vm.listDataCv = [];
        	}
        }
        
        vm.btnGiaoViec = btnGiaoViec;
        function btnGiaoViec(){
        	if(vm.listDataCv.length==0){
        		toastr.warning("Chưa chọn đơn vị giao việc !");
        		return;
        	}
        	vm.obj = {};
// vm.popupId = "POPUP_TTKT";
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupGroupTTKTDV(templateUrl, title, null, null, vm, null, 'string', false, '92%', '89%');
        }
        
        vm.itemGiaoViec = itemGiaoViec;
        function itemGiaoViec(dataItem){
        	vm.assignId = dataItem.assignHandoverId;
        	vm.lstDataAssignId.push(dataItem);
        	vm.obj = {};
// vm.popupId = "POPUP_TTKT";
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupGroupTTKTDV(templateUrl, title, null, null, vm, null, 'string', false, '92%', '89%');
        }
        
        vm.onSaveTTKTDV = function(data){
        	vm.listAssignIdCheck = [];
        	var obj = {};
        	confirm('Xác nhận giao việc cho đơn vị ' + data.text + ' ?', function(){
				obj.sysGroupId = data.sysGroupId;
				obj.sysGroupCode = data.code;
				obj.sysGroupName = data.name;
// obj.assignHandoverId = vm.assignId;
				if(vm.lstDataAssignId.length==0){
//					for(var j =0;j<vm.listDataCv.length;j++){
//						vm.listAssignIdCheck.push(vm.listDataCv[j].assignHandoverId);
//					}
					obj.lstAssignId = vm.listDataCv;
				} else {
					obj.lstAssignId = vm.lstDataAssignId;
				}
				assignHandoverTTKTService.updateSysGroupInAssignHandover(obj).then(
						function(result) {
							if(result.error){
		    				toastr.error(result.error);
		    				return;
		    			}
						doSearch();
						CommonService.dismissPopup1();
						toastr.success("Giao việc thành công !");
		    		}, function(errResponse){
		                	toastr.error(gettextCatalog.getString("Xảy ra lỗi khi giao việc !"));
		          });
			});
        }
        
        // pop up for SysGroupField
        function openDepartmentTo(popUpId) {
            vm.obj = {};
            vm.popupId = popUpId;
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupGroupTTKT(templateUrl, title, null, null, vm, popUpId, 'string', false, '92%', '89%');
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
                        return Restangular.all("departmentRsService/department/" + 'getSysGroupCheckTTKT').post({
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
                if (!vm.isSelectDeptAdd) {
                   vm.assignHandover.sysGroupText = null;
                    vm.assignHandover.sysGroupCode = null;
                    vm.assignHandover.sysGroupName = null;
                    vm.assignHandover.sysGroupId = null;
                }
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
            // vm.validateAddStationHouse();

            // VietNT_20190122_start
            // reset construction field if select after construction
            if (vm.isSelectConstruction) {
                resetConstructionAddForm();
            }
            // VietNT_end
        }

        // grid options for catStationHouse
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

                // VietNT_20190122_start
                resetConstructionAddForm();
                // VietNT_end
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

                    // VietNT_20190122_start
                    resetConstructionAddForm();
                    // VietNT_end
                }
            },
            close: function (e) {
                if (!vm.isSelectStationHouse) {
                    vm.assignHandover.catStationHouseCode = null;
                    vm.assignHandover.catStationHouseId = null;

                    // VietNT_20190122_start
                    resetConstructionAddForm();
                    // VietNT_end
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
            // vm.validateAddStation();

            // VietNT_20190122_start
            if (vm.isSelectConstruction) {
                resetConstructionAddForm();
            }
            // VietNT_end
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
        
        vm.openCatProvincePopup = openCatProvincePopup;
        function openCatProvincePopup() {
            var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm tỉnh");
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions, data, vm, 'fff', 'ggfd', false, '85%', '85%', 'catProvinceSearchController');
        }
        
        vm.onSaveCatProvince = onSaveCatProvince;
        function onSaveCatProvince(data) {
            vm.searchForm.catProvinceId = data.catProvinceId;
            vm.searchForm.catProvinceCode = data.code;
            vm.searchForm.catProvinceName = data.name;
            htmlCommonService.dismissPopup();
            $("#provincename").focus();
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

            // VietNT_20190122_start
            // select construction fill data to station & stationHouse fields
            vm.assignHandover.catStationHouseId = data.catStationHouseId;
            vm.assignHandover.catStationHouseCode = data.catStationHouseCode;
            vm.assignHandover.catStationId = data.catStationId;
            vm.assignHandover.catStationCode= data.catStationCode;

            // fill province data
            vm.assignHandover.catProvinceId = data.catProvinceId;
            vm.assignHandover.catProvinceCode = data.catProvinceCode;
            // VietNT_end
            vm.validateAddConstruction();
        }

        // remove assign handover
        function remove(dataItem) {
        	assignHandoverTTKTService.getCheckDataWorkItem(dataItem).then(function (d) {
    			if(d.length>0){
    				toastr.warning("Đã có hạng mục ghi nhận sản lượng, không được xoá !");
    				return false;
    			} else {
                confirm('Xác nhận xóa', function () {
                    assignHandoverTTKTService.removeAssignById(dataItem.assignHandoverId).then(
                        function (d) {
                            toastr.success("Xóa thành công!");
                            doSearch();
                        }, function (errResponse) {
                            toastr.error("Lỗi không xóa được!");
                        });
                });
    			}
        	});
        }

        // close popup
        function cancel() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

        // export excel
        var listRemove = [{
            title: "Thao tác",
        },{
        	title: "<input type='checkbox' id='checkallCv' name='gridchkselectall' ng-click='vm.chkSelectAllCv();' ng-model='vm.chkAllCv' />",
        },{
        	title: "File thiết kế",
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

			Restangular.all("assignHandoverService" + "/doSearchTTKT").post(data).then(function(d) {
                var data = d.data;
                for (var i = 0; i < data.length; i++) {
                    data[i].companyAssignDate = !!data[i].companyAssignDate
                        ? kendo.toString(kendo.parseDate(data[i].companyAssignDate), 'dd/MM/yyyy')
                        : "";
//                	data[i].companyAssignDate = !!data[i].companyAssignDate
//                    ? data[i].companyAssignDate
//                    : "";
                }
				CommonService.exportFile(vm.assignHandoverTTKTGrid, d.data, listRemove, listConvert, "Danh sách Giao việc TTKT");
			});
        }

        function closeErrImportPopUp() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

        // show hide column
        function showHideColumn(column) {
            if (angular.isUndefined(column.hidden)) {
                vm.assignHandoverTTKTGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.assignHandoverTTKTGrid.showColumn(column);
            } else {
                vm.assignHandoverTTKTGrid.hideColumn(column);
            }
        }

        function gridColumnShowHideFilter(item) {
            return item.type == null || item.type !== 1;
        }

        // reset form functions
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


        function checkErrDate(key) {
            var date;
            vm.disableSearch = false;
            if (key === 'dateFrom') {
                date = $('#dateFrom').val();
                vm.errMessage = '';
                vm.errMessage1 = '';
                var curDate = new Date();
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
                date = $('#dateTo').val();
                vm.errMessage = '';
                vm.errMessage2 = '';
                var curDate = new Date();
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
        
        vm.resetFormStatus = function(){
        	vm.searchForm.isDelivered = null;
        }
        
        vm.clearProvince = function(){
        	vm.searchForm.catProvinceName = null;
        	vm.searchForm.catProvinceCode = null;
        	vm.searchForm.catProvinceId = null;
        }
        
        // end controller
    }

})();