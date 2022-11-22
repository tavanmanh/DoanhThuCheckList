(function() {
	'use strict';
	var controllerId = 'manageProjectIBSController';

	angular.module('MetronicApp').controller(controllerId,
			manageProjectIBSController);

	function manageProjectIBSController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, manageProjectIBSService,
			CommonService, PopupConst, Restangular, RestEndpoint, Constant,
			htmlCommonService) {
		var vm = this;
		vm.showSearch = true;
		vm.isCreateNew = false;
		vm.showDetail = false;
		vm.searchForm = {};
		vm.addForm = {};
		vm.String = $rootScope.stringtile;
		vm.checkRoleUpdate = false;
		vm.provinceRole = null;
		vm.dataFile = [];
		init();
		function init(){
			manageProjectIBSService.checkDomainUser().then(function(result){
				if(result.length > 1) {
					vm.checkRoleUpdate = true;
				} else if(result.length == 1) {
					vm.provinceRole = result[0];
				}
			});
			fillDataTable([]);
			fillFileTable([]);
		}
		
		vm.showHideWorkItemColumn = function (column) {
		    if (angular.isUndefined(column.hidden)) {
		        vm.projectIbsGrid.hideColumn(column);
		    } else if (column.hidden) {
		        vm.projectIbsGrid.showColumn(column);
		    } else {
		        vm.projectIbsGrid.hideColumn(column);
		    }


		}

		vm.gridColumnShowHideFilter = function (item) {

		    return item.type == null || item.type !== 1;
		};

		vm.contractingStatusList = [
			{
				code: "1",
				name: "Tiếp xúc thành công , đang thương thảo ký hợp đồng"
			},
			{
				code: "2",
				name: "Đã ký hợp đồng"
			}
		];
		
		vm.levelDeploymentList = [
			{
				code: "1",
				name: "Không thể triển khai"
			},
			{
				code: "2",
				name: "Có thể triển khai"
			}
		];
		
		var record = 0;
        function fillDataTable(data) {
            vm.projectIbsGridOptions = kendoConfig.getGridOptions({
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
						+
                        '<button class="btn btn-qlk padding-search-right TkQLK "' +
                        'ng-click="vm.importProject()" uib-tooltip="Import tiếp xúc" translate>Import</button>'
						+ '</div>'
                        +'<div class="btn-group pull-right margin_top_button ">' +
                        '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: triệu VNĐ</div>'+
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-disabled="vm.disableExportExcel" ng-click="vm.exportExcelProject()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">' +
                        '<label ng-repeat="column in vm.projectIbsGrid.columns.slice(1,vm.projectIbsGrid.columns.length)| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#appCountProjectIbs").text("" + response.total);
                            vm.countProjectIbs = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            url: Constant.BASE_SERVICE_URL + "progressPlanProjectRestService/doSearch",
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
                        title: "Khu vực",
                        width: '80px',
                        field:"areaCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        editable:false,
                    },
                    {
                        title: "Tỉnh",
                        field: 'provinceCode',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;white-space:normal;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Tên dự án",
                        field: 'projectName',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;white-space:normal;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Địa chỉ",
                        field: 'address',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;white-space:normal;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Tên chủ đầu tư",
                        field: 'investorName',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;white-space:normal;"
                        },
                        type :'text',
                        editable:false,
                    },
                    {
                        title: "Công năng dự án",
                        field: 'projectPerformance',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;white-space:normal;"
                        },
                        type :'text',
                        editable:false,
                    },
                    {
                        title: "Quy mô dự án",
                        field: 'name',
                        width: '250px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false,
                        columns: [
                        	{
                                title: "Số căn hộ",
                                width: '80px',
                                field:"numberHouse",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                },
                                type :'number',
                                editable:false,
                            },
                            {
                                title: "Số Block",
                                width: '80px',
                                field:"numberBlock",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                },
                                type :'number',
                                editable:false,
                            },
                            {
                                title: "Diện tích (m2)",
                                width: '80px',
                                field:"acreage",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                },
                                type :'number',
                                editable:false,
                            },
                         ]
                    },

                    {
                        title: "Tiến độ hiện tại dự án",
                        field: 'progressProject',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'text',
                        editable: true,
                    },
                    {
                        title: "Thông tin chủ đầu tư",
                        field: 'contactCus',
                        width: '300px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false,
                        columns: [
                        	{
                                title: "Đầu mối liên hệ",
                                width: '100px',
                                field:"contactCus",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                },
                                type :'text',
                                editable:false,
                            },
                            {
                                title: "Chức vụ",
                                width: '100px',
                                field:"positionCus",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                },
                                type :'text',
                                editable:false,
                            },
                            {
                                title: "Số điện thoại",
                                width: '100px',
                                field:"phoneNumberCus",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                },
                                type :'text',
                                editable:false,
                            },
                            {
                                title: "Email",
                                width: '100px',
                                field:"emailCus",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                },
                                type :'text',
                                editable:false,
                            },
                         ]
                    },
                    {
                        title: "Họ và tên người đi đàm phán, tiếp xúc",
                        field: 'contactCus',
                        width: '280px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false,
                        columns: [
                        	{
                                title: "Họ và tên",
                                width: '90px',
                                field:"contactEmploy",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                },
                                type :'text',
                                editable:false,
                            },
                            {
                                title: "Số điện thoại",
                                width: '90px',
                                field:"phoneNumberEmploy",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                },
                                type :'text',
                                editable:false,
                            },
                            {
                                title: "Email",
                                width: '90px',
                                field:"emailEmploy",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                },
                                type :'text',
                                editable:false,
                            },
                         ]
                    },
                    {
                        title: "Hạn hoàn thành tiếp xúc",
                        width: '100px',
                        field:"deadlineDateComplete",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        editable:false,
                    },
                    {
                        title: "Tiến độ tiếp xúc dự án",
                        field: 'contactCus',
                        width: '280px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        editable:false,
                        columns: [
                        	{
                                title: "Đã tiếp xúc",
                                width: '80px',
                                field:"dateExposed",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                },
                                type :'text',
                                editable:false,
                            },
                            {
                                title: "Tình trạng thương thảo, ký hợp đồng",
                                width: '80px',
                                field:"contractingStatus",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                },
                                type :'text',
                                editable:false,
                                template: function(dataItem){
                                	if(dataItem.contractingStatus==1){
                                		return "Tiếp xúc thành công , đang thương thảo ký hợp đồng";
                                	} else if(dataItem.contractingStatus==2){
                                		return "Đã ký hợp đồng";
                                	} else {
                                		return "";
                                	}
                                }
                            },
                            {
                                title: "Ghi chú",
                                width: '80px',
                                field:"note",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                },
                                type :'text',
                                editable:false,
                            },
                         ]
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
                        		return "Chưa thực hiện";
                        	} else if(dataItem.status==2){
                        		return "Đang thực hiện";
                        	} else if(dataItem.status==3){
                        		return "Đã hoàn thành";
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
                                '<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.edit(dataItem)" class=" icon_table "' +
                                ' ng-show="vm.checkRoleUpdate || (vm.provinceRole==dataItem.provinceId)"  uib-tooltip="Sửa" translate>' +
                                '<i class="fa fa-pencil" aria-hidden="true"></i>' +
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
        	if(param=='provinceCode'){
        		vm.searchForm.provinceCode = null;
        	}
        	
        	if(param=='projectName'){
        		vm.searchForm.projectName = null;
        	}
        }
        
        //Tạo mới
        vm.add = function(){
			vm.isCreateNew = true;
			vm.addForm = {};
			vm.dataFile = [];
			var teamplateUrl = "coms/manageProjectIBS/manageProjectIBSPopup.html";
			var title = "Tạo mới dự án IBS";
			var windowId = "ADD_PROJECT_IBS";
			CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '85%', '85%', "deptAdd");
		}
        
        //Chỉnh sửa
        vm.edit = function(dataItem){
			vm.isCreateNew = false;
			vm.addForm = angular.copy(dataItem);
			vm.dataFile = [];
			var teamplateUrl = "coms/manageProjectIBS/manageProjectIBSPopup.html";
			var title = "Cập nhật dự án IBS";
			var windowId = "EDIT_PROJECT_IBS";
			manageProjectIBSService.getListFile(dataItem.progressPlanProjectId).then(function(data){
				CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '85%', '85%', "deptAdd");
				vm.dataFile = data;
			});
		}
        
        //Import tiếp xúc
        vm.importProject = importProject;
        function importProject() {
            vm.fileImportData = false;
            var teamplateUrl = "coms/manageProjectIBS/importProjectIBS.html";
            var title = "Import tiếp xúc";
            var windowId = "IMPORT_PROJECT_IBS";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '275', null);
        }
        
        //Lấy biểu mẫu import
        vm.getExcelTemplate = function () {
            var fileName = "Import_DS_tiepxuc";
            CommonService.downloadTemplate2(fileName).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileService/downloadFileATTT?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
                return;
            });

        };
        
        //Save import
        vm.submitProject = submitProject;
        function submitProject(data) {
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
                url: Constant.BASE_SERVICE_URL + "progressPlanProjectRestService/importProject?folder=temp",
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
                        saveImportProject(data);
//                        toastr.success("Import thành công");
//                        doSearch();
                    }
                    $scope.$apply();
                }
            });
        }
        
      //Save import
        function saveImportProject(data){
        	var obj = {};
        	obj.lstDataImport = data;
        	manageProjectIBSService.saveImportProject(obj).then(function(result){
        		toastr.success("Import thành công !");
        		$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        		doSearch();
        	}).catch(function(){
        		toastr.error("Có lỗi xảy ra khi lưu dữ liệu !");
        		return;
        	});
        }
        
        vm.closeErrImportPopUp = closeErrImportPopUp;
        function closeErrImportPopUp() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
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
                    }
                    , {
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
                        },
                    }, {
                        title: "Nội dung lỗi",
                        field: 'detailError',
                        width: 250,
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:left;"
                        },
                    }
                ]
            });
        }

        vm.closeErrImportPopUp = closeErrImportPopUp;
        function closeErrImportPopUp() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
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
		            	vm.addForm.provinceId = dataItem.catProvinceId;
		            	vm.addForm.provinceCode = dataItem.code;
		            	vm.addForm.areaCode = dataItem.areaCode;
		            } else {
		            	vm.searchForm.provinceCode = dataItem.code;
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
		                    return Restangular.all("progressPlanProjectRestService/catProvince/doSearchProvinceInPopup").post({
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
		        			vm.addForm.provinceId = null;
			            	vm.addForm.provinceCode = null;
			            	vm.addForm.areaCode = null;
			            } else {
			            	vm.searchForm.provinceCode = null;
			            }
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	if(vm.isCreateNew){
		            		vm.addForm.provinceId = null;
			            	vm.addForm.provinceCode = null;
			            	vm.addForm.areaCode = null;
			            } else {
			            	vm.searchForm.provinceCode = null;
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
			vm.addForm.provinceId = dataItem.provinceId;
			vm.addForm.provinceCode = dataItem.code;
			vm.addForm.areaCode = dataItem.areaCode;
	        htmlCommonService.dismissPopup();
	    };
	    
	    //save tỉnh popup tìm kiếm
	    vm.onSaveCatProvinceSearch = onSaveCatProvinceSearch;
		function onSaveCatProvinceSearch(dataItem){
			vm.searchForm.provinceCode = dataItem.code;
	        htmlCommonService.dismissPopup();
	    };
		
	    //Autosuccess hợp đồng
	    vm.contractOptions = {
		        dataTextField: "contractCode",
		        dataValueField: "id",
				placeholder:"Nhập mã hoặc tên hợp đồng",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.addForm.contractCode = dataItem.contractCode;
		            vm.addForm.contractId = dataItem.contractId;
//		            vm.addForm.dateContract = dataItem.dateContract;
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
		                    return Restangular.all("progressPlanProjectRestService/getCntContractInHtct").post({
		                    	keySearch: vm.addForm.contractCode,
		                        pageSize: vm.contractOptions.pageSize,
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
				'<p class="col-md-6 text-header-auto border-right-ccc">Mã hợp đồng</p>' +
//				'<p class="col-md-6 text-header-auto">Tên tỉnh</p>' +
				'</div>',
		        template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.contractCode #</div>' +
//		        	'<div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div>'
		        '</div>',
		        change: function (e) {
		        	if (!vm.isSelect) {
		        		vm.addForm.contractCode = null;
			            vm.addForm.contractId = null;
//			            vm.addForm.dateContract = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.addForm.contractCode = null;
			            vm.addForm.contractId = null;
			            vm.addForm.dateContract = null;
		            }
		        }
		    }
	    
	    vm.openContract = openContract;
		function openContract(param){
			var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm hợp đồng");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, param, 'ggfd', false, '75%','75%','contractInHtctController');
	    }
		
		vm.onSaveContract = onSaveContract;
		function onSaveContract(dataItem){
			vm.addForm.contractCode = dataItem.contractCode;
            vm.addForm.contractId = dataItem.contractId;
//            vm.addForm.dateContract = dataItem.dateContract;
	        htmlCommonService.dismissPopup();
	    };
	    
	    vm.doSearch = doSearch;
		function doSearch() {
		    var grid = vm.projectIbsGrid;
		    if (grid) {
		        grid.dataSource.query({
		            page: 1,
		            pageSize: 10
		        });
		    }
		}
	    
	    vm.cancel = function(){
	    	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
	    }
	    
	    function validateEmail($email) {
	    	var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	    	return emailReg.test( $email );
	    }
	    
	    vm.saveProject = function(){
	    	if(vm.addForm.provinceCode==null && $("#provinceCode").val()==""){
	    		toastr.warning("Tỉnh không được để trống");
	    		$("#provinceCode").focus();
	    		return;
	    	}
	    	
	    	if(vm.addForm.projectName==null && $("#projectName").val()==""){
	    		toastr.warning("Tên dự án không được để trống");
	    		$("#projectName").focus();
	    		return;
	    	}
	    	
	    	if(vm.addForm.investorName==null && $("#investorName").val()==""){
	    		toastr.warning("Tên CĐT không được để trống");
	    		$("#investorName").focus();
	    		return;
	    	}
	    	///
	    	if(vm.addForm.numberHouse==null && $("#numberHouse").val()==""){
	    		toastr.warning("Số căn hộ không được để trống");
	    		$("#numberHouse").focus();
	    		return;
	    	}
	    	
	    	if(vm.addForm.acreage==null && $("#acreage").val()==""){
	    		toastr.warning("Diện tích không được để trống");
	    		$("#acreage").focus();
	    		return;
	    	}
	    	///
	    	if(vm.addForm.contactCus==null && $("#contactCus").val()==""){
	    		toastr.warning("Đầu mối liên hệ CĐT không được để trống");
	    		$("#contactCus").focus();
	    		return;
	    	}
	    	
	    	if(vm.addForm.phoneNumberCus==null && $("#phoneNumberCus").val()==""){
	    		toastr.warning("Số điện thoại CĐT không được để trống");
	    		$("#phoneNumberCus").focus();
	    		return;
	    	} else {
	    		var vnf_regex = /((09|03|07|08|05)+([0-9]{8})\b)/g;
	        	if(kendo.parseFloat($("#phoneNumberCus").val())<0){
	        		toastr.warning("Số điện thoại CĐT sai định dạng");
	        		$("#phoneNumberCus").focus();
	            	return;
	        	} else {
	        		if(!vnf_regex.test($("#phoneNumberCus").val())) {
	        			toastr.warning("Số điện thoại CĐT sai định dạng");
	                	 $("#phoneNumberCus").focus();
	                	 return;
	                }
	        	}
	    	} 
	    	
	    	if(vm.addForm.emailCus!=null && $("#emailCus").val()!=""){
	    		if(!validateEmail($("#emailCus").val())){
	    			toastr.warning("Email CĐT không đúng định dạng");
		    		$("#emailCus").focus();
		    		return;
	    		}
	    	}
	    	
//	    	if(vm.addForm.emailCus==null && $("#emailCus").val()==""){
//	    		toastr.warning("Email CĐT không được để trống");
//	    		$("#emailCus").focus();
//	    		return;
//	    	} else {
//	    		if(!validateEmail($("#emailCus").val())){
//	    			toastr.warning("Email CĐT không đúng định dạng");
//		    		$("#emailCus").focus();
//		    		return;
//	    		}
//	    	}
	    	///
	    	if(vm.addForm.contactEmploy==null && $("#contactEmploy").val()==""){
	    		toastr.warning("Họ tên người tiếp xúc không được để trống");
	    		$("#contactEmploy").focus();
	    		return;
	    	}
	    	
	    	if(vm.addForm.emailEmploy==null && $("#emailEmploy").val()==""){
	    		toastr.warning("Email người tiếp xúc không được để trống");
	    		$("#emailEmploy").focus();
	    		return;
	    	} else {
	    		if(!validateEmail($("#emailEmploy").val())){
	    			toastr.warning("Email người tiếp xúc không đúng định dạng");
		    		$("#emailEmploy").focus();
		    		return;
	    		}
	    	}
	    	
	    	if(vm.addForm.phoneNumberEmploy==null && $("#phoneNumberEmploy").val()==""){
	    		toastr.warning("Số điện thoại người tiếp xúc không được để trống");
	    		$("#phoneNumberEmploy").focus();
	    		return;
	    	} else {
	    		var vnf_regex = /((09|03|07|08|05)+([0-9]{8})\b)/g;
	        	if(kendo.parseFloat($("#phoneNumberEmploy").val())<0){
	        		toastr.warning("Số điện thoại người tiếp xúc sai định dạng");
	        		$("#phoneNumberEmploy").focus();
	            	return;
	        	} else {
	        		if(!vnf_regex.test($("#phoneNumberEmploy").val())) {
	        			 toastr.warning("Số điện thoại người tiếp xúc sai định dạng");
	                	 $("#phoneNumberEmploy").focus();
	                	 return;
	                }
	        	}
	    	} 
	    	
	    	if(vm.addForm.deadlineDateComplete==null && $("#deadlineDateComplete").val()==""){
	    		toastr.warning("Hạn hoàn thành tiếp xúc không được để trống");
	    		$("#deadlineDateComplete").focus();
	    		return;
	    	}
	    	
	    	if(vm.addForm.contractingStatus==2 && vm.addForm.levelDeployment!=1){
	    		if(vm.addForm.contractCode==null && $("#contractCode").val()==""){
		    		toastr.warning("Số hợp đồng không được để trống");
		    		$("#contractCode").focus();
		    		return;
		    	}
		    	
		    	if(vm.addForm.dateContract==null && $("#dateContract").val()==""){
		    		toastr.warning("Ngày ký không được để trống");
		    		$("#dateContract").focus();
		    		return;
		    	}
	    	}
	    	
	    	var gridFile = $("#contactIBSFileGrid").data("kendoGrid").dataSource.data();
	    	if(vm.addForm.levelDeployment=="1"){
	    		if(gridFile.length==0){
	    			toastr.warning("Chưa đính kèm file !");
		    		return;
	    		} 
	    		vm.addForm.listFile = gridFile;
	    	}
	    	if(vm.isCreateNew){
	    		manageProjectIBSService.saveProject(vm.addForm).then(function(result){
		    		if(result.error){
		    			toastr.error(result.error);
		    			return;
		    		}
		    		toastr.success("Thêm mới thành công !");
		    		$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
		    		doSearch();
		    	}).catch(function(e){
		    		toastr.error("Có lỗi xảy ra trong quá trình tạo mới !");
		    		return;
		    	});
	    	} else {
	    		manageProjectIBSService.updateProject(vm.addForm).then(function(result){
		    		if(result.error){
		    			toastr.error(result.error);
		    			return;
		    		}
		    		toastr.success("Cập nhật thành công !");
		    		$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
		    		doSearch();
		    	}).catch(function(e){
		    		toastr.error("Có lỗi xảy ra trong quá trình cập nhật !");
		    		return;
		    	});
	    	}
	    }
	    
	  //Xuất Excel
        vm.exportExcelProject = function(){
        	vm.searchForm.page = null;
        	vm.searchForm.pageSize = null;
        	kendo.ui.progress($("#projectIbsGrid"), true);
        	return Restangular.all("progressPlanProjectRestService/exportExcelProject").post(vm.searchForm).then(function (d) {
        	    var data = d.plain();
        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	    kendo.ui.progress($("#projectIbsGrid"), false);
        	}).catch(function (e) {
        		kendo.ui.progress($("#projectIbsGrid"), false);
        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        	    return;
        	});
        }
	    
        function checkValidatePhone(param){
        	var vnf_regex = /((09|03|07|08|05)+([0-9]{8})\b)/g;
        	if(param=='phoneNumberCus'){
        		if(kendo.parseInt($("#phoneNumberCus").val())<0){
        			toastr.warning("Số điện thoại CĐT sai định dạng");
        			$("#phoneNumberCus").focus();
            	    return;
        		} else {
        			if(vnf_regex.test($("#phoneNumberCus").val())) {
                	    return true;
                	} else {
                	    toastr.warning("Số điện thoại CĐT sai định dạng");
                	    $("#phoneNumberCus").focus();
                	    return;
                	}
        		}
        	}
        	if(param=='phoneNumberEmploy'){
        		if(kendo.parseInt($("#phoneNumberEmploy").val())<0){
        			toastr.warning("Số điện thoại người tiếp xúc sai định dạng");
            	    return false;
        		} else {
        			if(vnf_regex.test($("#phoneNumberEmploy").val())) {
                	    return true;
                	} else {
                		toastr.warning("Số điện thoại người tiếp xúc sai định dạng");
                		$("#phoneNumberEmploy").focus();
                	    return false;
                	}
        		}
        	}
        }
        
        //File đính kèm
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

			var gridFile = $('#contactIBSFileGrid').data('kendoGrid').dataSource.data();
			for(var j=0; j<gridFile.length; j++){
				if($("#files")[0].files[0].name == gridFile[j].name){
					toastr.warning("File đã tồn tại trên lưới !");
					setTimeout(function () {
						$(".k-upload-files.k-reset").find("li").remove();
						$(".k-upload-files").remove();
						$(".k-upload-status").remove();
						$(".k-upload.k-header").addClass("k-upload-empty");
						$(".k-upload-button").removeClass("k-state-focused");
					}, 10);
					return;
				}
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
						vm.dataFile = $("#contactIBSFileGrid").data().kendoGrid.dataSource.data();
						var obj = {};
						obj.name = file.name;
						obj.filePath = data[index];
//						obj.appParam = {
//							code: "choese",
//							name: "~~ Chọn ~~"
//						};
						obj.createdDate = htmlCommonService.getFullDate();
						obj.createdUserName = Constant.userInfo.VpsUserInfo.fullName;
						obj.createdUserId = Constant.userInfo.VpsUserInfo.sysUserId;
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
        
        $scope.$watchGroup(['vm.contactIBSFileGrid', 'vm.dataFile'], function (newVal, oldVal) {
			refreshGrid(vm.dataFile);
		});

		function refreshGrid(d) {
			var grid = vm.contactIBSFileGrid;
			if (grid) {
			grid.dataSource.data(d);
				grid.refresh();
			}
		}
		
		function fillFileTable(data) {
			var dataSource = new kendo.data.DataSource({
				data: data,
				autoSync: false,
				schema: {
					model: {
						id: "contactIBSFileGrid",
						fields: {
							stt: { editable: false },
							name: { editable: false },
//							appParam: { defaultValue: { name: "~~Chọn~~", code: "choese" } },
							createdDate: { editable: false },
							createdUserName: { editable: false },
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
				var GridDestination = $("#contactIBSFileGrid").data("kendoGrid");
				GridDestination.pager.element.hide();
			},
			columns: [{
				title: "TT",
				field: "stt",
				template: dataItem => $("#contactIBSFileGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
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
//			{
//				title: "Loại file",
//				field: 'appParam',
//				width: 150,
//				editor: categoryDropDownEditor,
//				template: "#=appParam.name#",
//				headerAttributes: {
//					style: "text-align:center; font-weight: bold",
//					translate:""
//				},
//				attributes: {
//					"id": "appFile",
//					style: "text-align:left;"
//				},
//			},
			{
				title: "Ngày upload",
				field: 'createdDate',
		        width: 150,
		        headerAttributes: {
					style: "text-align:center;"
				},
				attributes: {
					"id":"appFile",
					style: "text-align:left;"
				},
			},
			{
				title: "Người upload",
				field: 'createdUserName',
		        width: 150,
		        headerAttributes: {
					style: "text-align:center;"
				},
				attributes: {
					"id":"appFile",
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
		
		vm.downloadFile = downloadFile;
		function downloadFile(dataItem) {
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
        }
		
		vm.removeRowFile = removeRowFile;
		function removeRowFile(dataItem) {
			confirm('Xác nhận xóa', function () {
				$('#contactIBSFileGrid').data('kendoGrid').dataSource.remove(dataItem);
				vm.dataFile = $('#contactIBSFileGrid').data('kendoGrid').dataSource.data();
			});
		}
		// end controller
	}
})();