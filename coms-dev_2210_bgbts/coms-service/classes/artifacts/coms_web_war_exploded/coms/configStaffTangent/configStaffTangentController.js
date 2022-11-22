(function() {
	'use strict';
	var controllerId = 'configStaffTangentController';

	angular.module('MetronicApp').controller(controllerId,
			configStaffTangentController);

	function configStaffTangentController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, configStaffTangentService,
			CommonService, PopupConst, Restangular, RestEndpoint, Constant,
			htmlCommonService) {
		var vm = this;
		vm.showSearch = true;
		vm.isCreateNew = false;
		vm.isSearch = true;
		vm.showDetail = false;
		vm.searchForm = {
				type: ""
		};
		vm.addForm = {};
		vm.String = "Quản lý công trình > Tiếp xúc khách hàng > Cấu hình nhân viên tiếp xúc khách hàng";
		init();
		function init() {
			fillDataTable([]);
		}

		vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.configStaffGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.configStaffGrid.showColumn(column);
			} else {
				vm.configStaffGrid.hideColumn(column);
			}

		}

		vm.gridColumnShowHideFilter = function(item) {

			return item.type == null || item.type !== 1;
		};

		vm.doSearch = doSearch;
		function doSearch(){
			var grid = vm.configStaffGrid;
			if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 10
				});
			}
		}
		
		vm.cancel = function(){
			$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
		}
		
		var record = 0;
		function fillDataTable(data){
			vm.configStaffGridOptions = kendoConfig.getGridOptions({
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
                        template: '<div class=" pull-left ">' +
                        '<button class="btn btn-qlk padding-search addQLK" '
						+ 'ng-click="vm.add()" uib-tooltip="Tạo mới" translate style="width: 100px;white-space:normal;">Tạo mới</button>'
//                      + '<button class="btn btn-qlk padding-search-right TkQLK "' +
//                        'ng-click="vm.importProject()" uib-tooltip="Import tiếp xúc" translate>Import</button>'
						+ '</div>'
                        +'<div class="btn-group pull-right margin_top_button ">' +
                        '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: triệu VNĐ</div>'+
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-disabled="vm.disableExportExcel" ng-click="vm.exportExcelConfig()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">' +
                        '<label ng-repeat="column in vm.configStaffGrid.columns.slice(1,vm.configStaffGrid.columns.length)| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#appCountConfig").text("" + response.total);
                            vm.countConfig = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            url: Constant.BASE_SERVICE_URL + "configStaffTangentRestService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page;
                            vm.searchForm.pageSize = options.pageSize;
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
                        width: '50px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Tỉnh",
                        width: '80px',
                        field:"provinceCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        editable:false,
                    },
                    {
                        title: "Cấp",
                        field: 'type',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false,
                        template: function(dataItem){
                        	if(dataItem.type==1){
                        		return "Quản lý";
                        	} else if(dataItem.type==2){
                        		return "Nhân viên";
                        	} else {
                        		return "";
                        	}
                        	
                        }
                    },
                    {
                        title: "Mã nhân viên",
                        field: 'staffCode',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Tên nhân viên",
                        field: 'staffName',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Số điện thoại",
                        field: 'staffPhone',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false,
                    },
                    {
                        title: "Trạng thái thực hiện",
                        width: '90px',
                        field:"status",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        editable:false,
                        template: function(dataItem){
                        	if(dataItem.status==1){
                        		return "Hiệu lực";
                        	} else if(dataItem.status==0){
                        		return "Hết hiệu lực";
                        	} else {
                        		return "";
                        	}
                        }
                    },
                    {
                        title: "Thao tác",
                        type :'text',
                        editable:false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        template: function(dataItem) {
                        	var html =  '<div class="text-center">' +
                                '<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.edit(dataItem)" class=" icon_table" ng-show="dataItem.status==1"' +
                                '   uib-tooltip="Sửa" translate>' +
                                '<i class="fa fa-pencil" aria-hidden="true"></i>' +
                                '</button>' +
                                '<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.remove(dataItem)" class=" icon_table" ng-show="dataItem.status==1"' +
                                '   uib-tooltip="Xoá" translate>' +
                                '<i class="fa fa-trash" aria-hidden="true" style="color: #337ab7;"></i>' +
                                '</button>' +
                                '</div>';
                        	return html;
						},
						width: '100px',
						field:    "action",
                    },
    ]
    });
		}
		
		vm.clearInput = function(param){
			if(param == 'provinceCode'){
				vm.searchForm.provinceCode = null;
				vm.searchForm.catProvinceId = null;
			}
			
			if(param == 'staffName'){
				vm.searchForm.staffName = null;
				vm.searchForm.staffId = null;
			}
		}
		
		vm.clearInputPopup = function(param){
			if(param == 'provinceCode'){
				vm.addForm.provinceCode = null;
				vm.addForm.catProvinceId = null;
			}
			
			if(param == 'staffCode'){
				vm.addForm.staffId = null;
	            vm.addForm.staffCode = null;
	            vm.addForm.staffName = null;
	            vm.addForm.staffPhone = null;
			}
			
			if(param == 'type'){
				vm.addForm.type = "";
			}
		}
		
		//Autosuccess tỉnh
		vm.provinceOptions = {
		        dataTextField: "code",
		        dataValueField: "id",
				placeholder:"Nhập mã hoặc tên tỉnh",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            if(vm.isCreateNew){
		            	vm.addForm.catProvinceId = dataItem.catProvinceId;
		            	vm.addForm.provinceCode = dataItem.code;
		            	vm.addForm.areaCode = dataItem.areaCode;
		            } else {
		            	vm.searchForm.provinceCode = dataItem.code;
		            	vm.searchForm.catProvinceId = dataItem.catProvinceId;
		            }
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
		                    return Restangular.all("configStaffTangentRestService/catProvince/doSearchProvinceInPopup").post({
		                        name: vm.isCreateNew ? vm.addForm.provinceCode : vm.searchForm.provinceCode,
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
		        		if(vm.isCreateNew){
		        			vm.addForm.catProvinceId = null;
			            	vm.addForm.provinceCode = null;
			            	vm.addForm.areaCode = null;
			            } else {
			            	vm.searchForm.provinceCode = null;
			            	vm.searchForm.catProvinceId = null;
			            }
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	if(vm.isCreateNew){
		            		vm.addForm.catProvinceId = null;
			            	vm.addForm.provinceCode = null;
			            	vm.addForm.areaCode = null;
			            } else {
			            	vm.searchForm.provinceCode = null;
			            	vm.searchForm.catProvinceId = null;
			            }
		            }
		        }
		    }
		
		//open popup tỉnh
		vm.openCatProvincePopup = openCatProvincePopup;
		function openCatProvincePopup(param){
			var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm tỉnh");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, param, 'ggfd', false, '75%','75%','catProvinceSearchController');
	    }
		
		//save tỉnh popup tạo mới
		vm.onSaveCatProvince = onSaveCatProvince;
		function onSaveCatProvince(dataItem){
			vm.addForm.catProvinceId = dataItem.catProvinceId;
			vm.addForm.provinceCode = dataItem.code;
	        htmlCommonService.dismissPopup();
	    };
	    
	    //save tỉnh popup tìm kiếm
	    vm.onSaveCatProvinceSearch = onSaveCatProvinceSearch;
		function onSaveCatProvinceSearch(dataItem){
			vm.searchForm.provinceCode = dataItem.code;
			vm.searchForm.catProvinceId = dataItem.catProvinceId;
	        htmlCommonService.dismissPopup();
	    };
		
	    //Autosuccess Nhân viên
		vm.staffOptions = {
		        dataTextField: "staffName",
		        dataValueField: "staffId",
				placeholder:"Nhập mã hoặc tên nhân viên",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.searchForm.staffId = dataItem.staffId;
		            vm.searchForm.staffName = dataItem.staffName;
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
		                    return Restangular.all("configStaffTangentRestService/catProvince/doSearchStaffByPopup").post({
		                        staffName: vm.searchForm.staffName,
		                        pageSize: vm.staffOptions.pageSize,
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
				'<p class="col-md-6 text-header-auto border-right-ccc">Mã NV</p>' +
				'<p class="col-md-6 text-header-auto">Tên NV</p>' +
				'</div>',
		        template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.staffCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.staffName #</div> </div>',
		        change: function (e) {
		        	if (!vm.isSelect) {
			            vm.searchForm.staffId = null;
			            vm.searchForm.staffName = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
			            vm.searchForm.staffId = null;
			            vm.searchForm.staffName = null;
		            }
		        }
		    }
	    
		//Auto success mã nhân viên
		vm.staffEditOptions = {
		        dataTextField: "staffCode",
		        dataValueField: "staffId",
				placeholder:"Nhập mã hoặc tên nhân viên",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.addForm.staffId = dataItem.staffId;
		            vm.addForm.staffCode = dataItem.staffCode;
		            vm.addForm.staffName = dataItem.staffName;
		            vm.addForm.staffPhone = dataItem.staffPhone;
		            vm.addForm.email = dataItem.email;
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
		                    return Restangular.all("configStaffTangentRestService/catProvince/doSearchStaffByPopup").post({
		                        staffName: vm.addForm.staffCode,
		                        pageSize: vm.staffOptions.pageSize,
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
				'<p class="col-md-6 text-header-auto border-right-ccc">Mã NV</p>' +
				'<p class="col-md-6 text-header-auto">Tên NV</p>' +
				'</div>',
		        template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.staffCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.staffName #</div> </div>',
		        change: function (e) {
		        	if (!vm.isSelect) {
		        		vm.addForm.staffId = null;
			            vm.addForm.staffCode = null;
			            vm.addForm.staffName = null;
			            vm.addForm.staffPhone = null;
			            vm.addForm.email = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		        		vm.addForm.staffId = null;
			            vm.addForm.staffCode = null;
			            vm.addForm.staffName = null;
			            vm.addForm.staffPhone = null;
			            vm.addForm.email = null;
		            }
		        }
		    }
		
		//open popup Nhân viên
		vm.openStaffPopup = openStaffPopup;
		function openStaffPopup(param){
			var templateUrl = 'coms/popup/staffSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm nhân viên");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, param, 'ggfd', false, '75%','75%','staffSearchController');
	    }
		
		//save Nhân viên popup tạo mới
		vm.onSaveStaff = onSaveStaff;
		function onSaveStaff(dataItem){
			vm.addForm.staffId = dataItem.staffId;
        	vm.addForm.staffCode = dataItem.staffCode;
        	vm.addForm.staffName = dataItem.staffName;
        	vm.addForm.staffPhone = dataItem.staffPhone;
        	vm.addForm.email = dataItem.email;
	        htmlCommonService.dismissPopup();
	    };
	    
	    //save Nhân viên popup tìm kiếm
	    vm.onSaveStaffSearch = onSaveStaffSearch;
		function onSaveStaffSearch(dataItem){
			vm.searchForm.staffId = dataItem.staffId;
        	vm.searchForm.staffName = dataItem.staffName;
	        htmlCommonService.dismissPopup();
	    };
		
	  //Tạo mới
        vm.add = function(){
			vm.isCreateNew = true;
			vm.isSearch = false;
			vm.addForm = {};
			var teamplateUrl = "coms/configStaffTangent/configStaffTangentPopup.html";
			var title = "Tạo mới cấu hình nhân viên tiếp xúc";
			var windowId = "ADD_CONFIG_STAFF";
			CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '45%', '45%', "deptAdd");
		}
        
        //Chỉnh sửa
        vm.edit = function(dataItem){
			vm.isCreateNew = false;
			vm.isSearch = true;
			vm.addForm = angular.copy(dataItem);
			var teamplateUrl = "coms/configStaffTangent/configStaffTangentPopup.html";
			var title = "Cập nhật cấu hình nhân viên tiếp xúc";
			var windowId = "EDIT_CONFIG_STAFF";
			CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '45%', '45%', "deptAdd");
		}
	    
        //Lưu dữ liệu
        vm.saveConfig = function(){
        	if(vm.addForm.provinceCode==null){
        		toastr.warning("Chưa chọn tỉnh !");
        		$("#provinceCode").focus();
        		return;
        	}
        	
        	if(vm.addForm.type==""){
        		toastr.warning("Phải chọn cấp Quản lý hoặc nhân viên !");
        		return;
        	}
        	if(vm.addForm.staffCode==null){
        		toastr.warning("Chưa chọn nhân viên !");
        		$("#staffCode").focus();
        		return;
        	}
        	
        	if(vm.isCreateNew){
        		configStaffTangentService.saveConfig(vm.addForm).then(function(result){
        			if (result!= undefined && result.error) {
                        toastr.error(result.error);
                        return;
                    }
        			toastr.success("Thêm mới thành công !");
        			$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        			doSearch();
        		}).catch(function(e){
        			toastr.error("Có lỗi xảy ra khi tạo mới !");
        			return;
        		});
        	} else {
        		configStaffTangentService.updateConfig(vm.addForm).then(function(result){
        			if (result!= undefined && result.error) {
                        toastr.error(result.error);
                        return;
                    }
        			toastr.success("Cập nhật thành công !");
        			$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        			doSearch();
        		}).catch(function(e){
        			toastr.error("Có lỗi xảy ra khi cập nhật !");
        			return;
        		});
        	}
        }
       
        vm.listRemove = [
        	{
        		title: "Thao tác",
        	}
        	];

        vm.listConvert = [
        	{
        		field: "status",
        		data: {
        			1: 'Hiệu lực',
        			0: 'Hết Hiệu lực'
            }
        	}, 
	        {
	            field: "type",
	            data: {
	                1: 'Quản lý',
	                2: 'Nhân viên',
	            }
	        }
        ]
        
        vm.exportExcelConfig = function(){
        	vm.searchForm.page = null;
        	vm.searchForm.pageSize = null;
        	kendo.ui.progress($("#configStaffGrid"), true);
        	configStaffTangentService.doSearch(vm.searchForm).then(function(data){
        		kendo.ui.progress($("#configStaffGrid"), false);
        		CommonService.exportFile(vm.configStaffGrid, data.data, vm.listRemove, vm.listConvert, "Cấu hình nhân viên tiếp xúc");
        	}).catch(function(e){
        		kendo.ui.progress($("#configStaffGrid"), false);
        		toastr.error("Có lỗi xảy ra khi xuất file !");
        	});
        }
        
        vm.remove = function(dataItem){
        	confirm("Xác nhận xoá bản ghi", function(){
        		configStaffTangentService.removeConfig(dataItem).then(function(result){
        			if (result!= undefined && result.error) {
                        toastr.error(result.error);
                        return;
                    }
        			toastr.success("Xoá bản ghi thành công !");
        			doSearch();
            	}).catch(function(e){
            		toastr.error("Có lỗi xảy ra !");
            		return;
            	});
        	});
        }
		// end controller
	}
})();