(function () {
    'use strict';
    var controllerId = 'manageMEController';

    angular.module('MetronicApp').controller(controllerId, manageMEController);

    function manageMEController($scope, $rootScope, $timeout, gettextCatalog, $filter,
        kendoConfig, $kWindow, manageMEService, $http,
        CommonService, PopupConst, Restangular, RestEndpoint, Constant, htmlCommonService) {
        var vm = this;
        vm.countCons = 0;
        vm.showSearch = true;
        vm.isCreateNew = true;
        vm.showDetail = false;
        vm.checkRoleCD = false;
		vm.provinceId = null;
		vm.isUpdate = false;
       
    	var start = new Date();
    	start.setMonth(start.getMonth() - 6);
    	var end = new Date();
    	
    	
        vm.MESearch = {
        	status:1
        };
        
        vm.radio = 0;
        
        vm.position = [
        	{name: "Nhân viên"},
        	{name: "Quản lý"},
        	{name: "Quản trị hệ thống"}
        ]
        
        vm.optionList = [
        	{id: 0, name: "Danh mục người dùng"},
        	{id: 1, name: "Danh mục đơn vị"},
        	{id: 2, name: "Quản lý tài liệu"},
        ];
        
        vm.areaCode = [
        	{code: 1, name: "KV1"},
        	{code: 2, name: "KV2"},
        	{code: 3, name: "KV3"},
        ];
        
        vm.checkB2BB2C = false; 
        vm.String = "Quản lý WO > Quản lý cơ điện > Quản lý danh mục";
        
        vm.allowAction = false;

        // Khoi tao du lieu cho form
        vm.iconDiss = true;
        vm.hidenIcon = hidenIcon;
        function hidenIcon() {
            vm.iconDiss = false;
        }
        vm.showIcon = function () {
            vm.iconDiss = true;
        }
        
        initFormData();
        
        function initFormData() {
        	checkRoleCD();
        	fillDataUserDirectoryTable([]);
        	fillDataUnitListTable([]);
        	fillDataDocumentManagementTable([]);
        }
        
        $scope.changeImage = changeImage;
        $scope.imageSelected = {};
        
        function changeImage(image) {
            $scope.imageSelected = image;
        }

        vm.cancelImport = cancelImport;
        function cancelImport() {
            CommonService.dismissPopup1();
        }
        var record = 0;
        
        // bảng danh mục người dùng
        vm.userDirectorySearch = {
        	status: 1,
        }
        function fillDataUserDirectoryTable(data) {
        	kendo.ui.progress($("#userDirectoryGrid"), true);
        	vm.userDirectoryGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: true,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                toolbar: [
                    {
                        name: "actions",
                        template: 
                            '<div class="btn-group pull-right margin_top_button margin10">' +
                            '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                     //       '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                            '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                            '<label ng-repeat="column in vm.manageMEGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                            '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                            '</label>' +
                            '</div></div>'
                    }
                ],
                dataSource: {
                	serverPaging: true,
                    schema: {
                        total: function (response) {
                            $timeout( function(){ 
                            	vm.countUser = response.total;
                            } );                       
                            return response.total;
                        },
                        data: function (response) {
                        	kendo.ui.progress($("#userDirectoryGrid"), false);
                        	return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "manageMERsService/userDirectory/doSearchUserDirectory",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.userDirectorySearch.page = options.page
                            vm.userDirectorySearch.pageSize = options.pageSize
                            return JSON.stringify(vm.userDirectorySearch)
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
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: 10,
                        columnMenu: false,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Tên đăng nhập",
                        field: 'loginName',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Tên đầy đủ",
                        field: 'fullName',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Mã tỉnh thành",
                        field: 'provinceCode',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tên đơn vị",
                        field: 'unitName',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                    	 title: "Thao tác",
                         headerAttributes: {style: "text-align:center;font-weight: bold;"},
                         template: dataItem =>
                         '<div class="text-center">'
                         + '<button style=" border: none; background-color: white;" id="updateId" ng-click="vm.edit(dataItem)" class=" icon_table "' +
                         '   uib-tooltip="Sửa" translate>' +
                         '<i class="fa fa-pencil" style="color:#e0d014"  aria-hidden="true"></i>' +
                         '</button>'
                         + '<button style=" border: none; background-color: white;" id=""' +
                         'class=" icon_table" ng-click="vm.remove(dataItem)"  uib-tooltip="Xóa" translate' + '>' +
                         '<i class="fa fa-trash" style="color: #337ab7;" aria-hidden="true"></i>' +
                         '</button>'
                         + '</div>',
                         width: '20%'
                    }
                ]
            });
        }
        
        // Bảng danh mục đơn vị
        function fillDataUnitListTable(data) {
        	kendo.ui.progress($("#unitListGrid"), true);
        	vm.unitListGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: true,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                toolbar: [
                    {
                        name: "actions",
                        template: 
                            '<div class="btn-group pull-right margin_top_button margin10">' +
                            '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                            //'<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile(vm.radio)"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                            '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                            '<label ng-repeat="column in vm.manageMEGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                            '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                            '</label>' +
                            '</div></div>'
                    }
                ],
                dataSource: {
                	serverPaging: true,
                    schema: {
                        total: function (response) {
                            $timeout( function(){ 
                            	vm.countUnit = response.total;
                            } );                       
                            return response.total;
                        },
                        data: function (response) {
                        	kendo.ui.progress($("#unitListGrid"), false);
                        	return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "manageMERsService/unitList/doSearchUnitList",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.MESearch.page = options.page
                            vm.MESearch.pageSize = options.pageSize
                            return JSON.stringify(vm.MESearch)
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
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: 10,
                        columnMenu: false,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Mã đơn vị",
                        field: 'unitCode',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Tên đơn vị",
                        field: 'unitName',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                    	 title: "Thao tác",
                         headerAttributes: {style: "text-align:center;font-weight: bold;"},
                         template: dataItem =>
                         '<div class="text-center">'
                         + '<button style=" border: none; background-color: white;" id="updateId" ng-click="vm.edit(dataItem)" class=" icon_table "' +
                         '   uib-tooltip="Sửa" translate>' +
                         '<i class="fa fa-pencil" style="color:#e0d014"  aria-hidden="true"></i>' +
                         '</button>'
                         + '<button style=" border: none; background-color: white;" id=""' +
                         'class=" icon_table" ng-click="vm.remove(dataItem)"  uib-tooltip="Xóa" translate' + '>' +
                         '<i class="fa fa-trash" style="color: #337ab7;" aria-hidden="true"></i>' +
                         '</button>'
                         + '</div>',
                         width: '20%'
                    }
                ]
            });
        }
        
        // bảng quản lý tài liệu
        vm.docManagementSearch = {
        	status: 1
        }
        function fillDataDocumentManagementTable(data) {
        	kendo.ui.progress($("#docManagementGrid"), true);
        	vm.docManagementGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: true,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                toolbar: [
                    {
                        name: "actions",
                        template: 
                            '<div class="btn-group pull-right margin_top_button margin10">' +
                            '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                            //'<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                            '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                            '<label ng-repeat="column in vm.manageMEGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                            '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                            '</label>' +
                            '</div></div>'
                    }
                ],
                dataSource: {
                	serverPaging: true,
                    schema: {
                        total: function (response) {
                            $timeout( function(){ 
                            	vm.countDoc = response.total;
                            } );                       
                            return response.total;
                        },
                        data: function (response) {
                        	kendo.ui.progress($("#docManagementGrid"), false);
                        	return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "manageMERsService/documentManagement/getList",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.docManagementSearch.page = options.page
                            vm.docManagementSearch.pageSize = options.pageSize
                            return JSON.stringify(vm.docManagementSearch)
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
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: "50px",
                        columnMenu: false,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Hành động",
                        width: "70px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: dataItem =>
                        '<div class="text-center">'
                        + '<button style=" border: none; background-color: white;" id="updateId" ng-click="vm.edit(dataItem)" class=" icon_table "' +
                        '   uib-tooltip="Sửa" translate>' +
                        '<i class="fa fa-pencil" style="color:#e0d014"  aria-hidden="true"></i>' +
                        '</button>'
                        + '<button style=" border: none; background-color: white;" id=""' +
                        'class=" icon_table" ng-click="vm.remove(dataItem)"  uib-tooltip="Xóa" translate' + '>' +
                        '<i class="fa fa-trash" style="color: #337ab7;" aria-hidden="true"></i>' +
                        '</button>'
                        + '</div>',
                    },
                    {
                        title: "Lĩnh vực",
                        field: 'field',
                        width: "100px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Loại tài liệu",
                        field: 'documentType',
                        width: "100px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Mã tài liệu",
                        field: 'documentCode',
                        width: "70px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Ngày ban hành",
                        field: 'dateIssued',
                        width: "100px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: "#= kendo.toString(kendo.parseDate(dateIssued, 'yyyy-MM-dd'), 'dd/MM/yyyy') #"
                    },
                    {
                        title: "Đơn vị ban hành",
                        field: 'issuingUnit',
                        width: "120px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Mô tả tài liệu",
                        field: 'description',
                        width: "150px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "File đính kèm",
                        width: "150px",
                        //field: 'attachFileName',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                        template :  function(dataItem) {
                        	//console.log("dataItem ", dataItem);
							if(dataItem.attachFileName == null){
								return "";
							}else{
								return "<a href='' ng-click='vm.downloadFile(dataItem)'>" + dataItem.attachFileName + "</a>";
							}
						}
                    },
                ]
            });
        }
        
        function removeTab (title) {
            for(var i=0; i<$scope.tabs.length; i++){
            	if($scope.tabs[i].title==title){
            		$scope.tabs.splice(i, 1);
            		break;
            	}
            }
		};

        function refreshGrid(d) {
            var grid = vm.constructionGrid;
            if (grid) {
                grid.dataSource.data(d);
                grid.refresh();
            }
        }

        vm.showHideColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.manageMEGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.manageMEGrid.showColumn(column);
            } else {
                vm.manageMEGrid.hideColumn(column);
            }


        }
       
        vm.gridColumnShowHideFilter = function (item) {
            return item.type == null || item.type !== 1;
        };
        
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
            }
            else {
                $("#fileName").innerHTML = $("#files")[0].files[0].name
            }
        };

        function refreshGrid(grid, data) {
            grid.dataSource.data(data);
            grid.refresh();
        }
        
        function checkRoleCD() {
        	var obj={
        			sysGroupId: Constant.userInfo.VpsUserInfo.sysGroupId
        	}
        	return Restangular.all("manageMERsService/checkRoleCD").post(obj).then(
                    function (response) {
                    	vm.checkRoleCD = response.checkRoleCD;
						vm.provinceId = response.provinceId;
                    }
                ).catch(
                    function (err) {
                        console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                    }
                );
		}
        
        //ducpm23
        vm.userOptions = {
        		dataTextField: "loginName",
        		dataValueField: "loginName",
        		placeholder: "Nhập mã nhân viên hoặc email",
        		select : function(e) {
					var dataItem = this.dataItem(e.item.index());
					vm.userDirectorySearch.loginName = dataItem.loginName;
					vm.userDirectorySearch.fullName = dataItem.fullName;
					vm.userDirectorySearch.unitName = dataItem.unitName;
					vm.userDirectorySearch.phone = dataItem.phone;
					vm.userDirectorySearch.provinceCode = dataItem.provinceCode;
				},
				 pageSize: 10,
		         open: function (e) {
		         },
		         dataSource: {
		        	 serverFiltering: true,
		                transport: {
		                    read: function (options) {
		                        return Restangular.all("manageMERsService/userDirectory/doSearch").post({
		                            email: vm.userDirectorySearch.loginName,
		                            pageSize: 10,
		                            page: 1
		                        }).then(function (response) {
		                            options.success(response.data);
		                        }).catch(function (err) {
		                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
		                        });
		                    }
		                }
		         },
		         template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.loginName #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.email #</div> </div>',
		            change: function (e) {
		                if (e.sender.value() === '') {
		                	vm.userDirectorySearch.loginName = null;
		                }
		          },
		          ignoreCase: false
        }
        
        vm.unitOptions = {
        	dataTextField: "unitName",
        	dataValueField: "unitName",
        	placeholder: "Nhập mã hoặc tên đơn vị",    
        	pageSize: 10,
        	select : function(e) {
				var dataItem = this.dataItem(e.item.index());
				vm.MESearch.unitCode = dataItem.unitCode;
        	},
        	open: function(e){
        		
        	},
        	dataSource: {
        		 serverFiltering: true,
        		 transport: {
        			 read: function(options) {
        				 return Restangular.all("manageMERsService/unitList/doSearch").post({
	                            unitName: vm.MESearch.unitName,
	                            areaCode: vm.MESearch.areaCode,
	                            pageSize: 10,
	                            page: 1
	                        }).then(function (response) {
	                            options.success(response.data);
	                        }).catch(function (err) {
	                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
	                        });
					}
        		 }
        	},
        	template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.unitCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.unitName #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                	vm.MESearch.unitName = null;
                }
            },
            ignoreCase: false
        }
        
//        vm.userManagerOptions = {
//        		dataTextField: "loginName",
//        		dataValueField: "loginName",
//        		placeholder: "Nhập mã nhân viên hoặc email",
//				 pageSize: 10,
//		         open: function (e) {
//		         },
//		         dataSource: {
//		        	 serverFiltering: true,
//		                transport: {
//		                    read: function (options) {
//		                        return Restangular.all("manageMERsService/userDirectory/doSearch").post({
//		                            email: vm.userDirectorySearch.managerName,
//		                            pageSize: 10,
//		                            page: 1
//		                        }).then(function (response) {
//		                            options.success(response.data);
//		                        }).catch(function (err) {
//		                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
//		                        });
//		                    }
//		                }
//		         },
//		         template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.loginName #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.email #</div> </div>',
//		            change: function (e) {
//		                if (e.sender.value() === '') {
//		                	vm.userDirectorySearch.loginName = null;
//		                }
//		            },
//		            ignoreCase: false
//        }
       
        vm.save = save;
        function save() {
        	//save trong bảng danh mục người dùng
        	if(vm.radio === 0){
        		// Kiểm tra điều kiện các trường
        		if(vm.userDirectorySearch.loginName === undefined || vm.userDirectorySearch.loginName === ""){
    				toastr.warning("Tên đăng nhập không được để trống");
            		return;
            	}
//    			if(vm.userDirectorySearch.managerName === undefined || vm.userDirectorySearch.managerName === ""){
//    				toastr.warning("Nhân viên quản lý không được để trống");
//    				return;
//    			}
        		// Thực hiện
    			if(!vm.isUpdate){
    				manageMEService.save(vm.userDirectorySearch, vm.radio).then(function(data) {
    					toastr.success("Lưu lại thành công");
    					vm.cleanAll();
    				}).catch(function(error) {
    					if (error.status === 409) {
    	                	toastr.error(gettextCatalog.getString("Người dùng đã tồn tại !"));
    	                } else {
    	                	toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi lưu tài liệu"));
    	                }
    				})
    			}else {
    				manageMEService.update(vm.userDirectorySearch, vm.radio).then(function(data) {
    					toastr.success("Thay đổi thông tin thành công !");
    					vm.cleanAll();
    				}).catch(function(error) {
    					toastr.warning("Lỗi khi ghi dữ liệu");
    				})
    			}
        	}
        	//save bảng danh mục đơn vị
        	if(vm.radio === 1){
        		// Kiểm tra điều kiện các trường
        		if(vm.MESearch.unitName === undefined || vm.MESearch.unitName === ""){
    				toastr.warning("Tên đơn vị không được để trống");
            		return;
            	}
        		// Thực hiện
    			if(!vm.isUpdate){
    				manageMEService.save(vm.MESearch, vm.radio).then(function(data) {
    					toastr.success("Lưu lại thành công");
    					vm.cleanAll();
    				}).catch(function(error) {
    					if (error.status === 409) {
    	                	toastr.error(gettextCatalog.getString("Mã đơn vị đã tồn tại !"));
    	                } else {
    	                	toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi lưu tài liệu"));
    	                }
    				})
    			}else {
    				manageMEService.update(vm.MESearch, vm.radio).then(function(data) {
    					toastr.success("Thay đổi thông tin thành công !");
    					vm.cleanAll();
    				}).catch(function(error) {
    					toastr.warning("Lỗi khi ghi dữ liệu");
    				})
    			}
        	}
        	// save bảng quản lý tài liệu
        	if(vm.radio === 2){
        		//test
        		console.log("List file upload", vm.fileLst);
        		if(vm.checkValue(vm.docManagementSearch.field) && vm.checkValue(vm.docManagementSearch.documentType) && vm.checkValue(vm.docManagementSearch.documentCode)
        				&& vm.checkValue(vm.docManagementSearch.issuingUnit)){
        			if(!vm.isUpdate){
                		// add fileAttach
                		//submitAttachFile();
        				if(vm.fileLst.length <= 0){
                			toastr.warning("Bạn chưa tải file lên");
                			return ;
                		}
                		vm.docManagementSearch.createUser = Constant.userInfo.vpsUserToken.sysUserId;
                		vm.docManagementSearch.attachFile = angular.copy(vm.fileLst);
                		manageMEService.save(vm.docManagementSearch, vm.radio).then(function(data) {
        					toastr.success("Lưu lại thành công");
        					vm.cleanAll();
        				}).catch(function(error) {
        					if (error.status === 409) {
        	                	toastr.error(gettextCatalog.getString("Mã tài liệu đã tồn tại !"));
        	                } else {
        	                	toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi lưu tài liệu"));
        	                }
        				})
                	}else{
                		//Thêm các trường cần thiết để cập nhật file
                		if(vm.fileLst != null){
                			vm.docManagementSearch.attachFile = {
                        			...vm.docManagementSearch.attachFile,
                        			name: vm.fileLst.name,
                        			filePath: vm.fileLst.filePath,
                        	}
                			vm.docManagementSearch.attachFileName = vm.fileLst.name;
                		}
                	
                		manageMEService.update(vm.docManagementSearch, vm.radio).then(function(data) {
        					toastr.success("Cập nhật thành công");
        					vm.cleanAll();
        				}).catch(function(error) {
        					toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi lưu tài liệu"));
        				})
                	}
        		}
        	}
        	
		}
        vm.checkValue = function(value) {
			if(value === undefined || value === "") return false;
			return true;
		}
        vm.remove = remove;
        function remove(dataItem) {
        	
        	confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function () {
        		manageMEService.remove(dataItem, vm.radio).then(function(data) {
        			toastr.success("Xóa thành công!");
        			vm.cleanAll();
				})
            });
		}
        
        vm.doSearch = doSearch;
        function doSearch() {
            vm.showDetail = false;
            if(vm.radio === 0){
            	 vm.exportInfor = vm.userDirectorySearch;
            	 console.log(vm.userDirectorySearch)
            	 var grid = vm.userDirectoryGrid;
                 if (grid) {
                     grid.dataSource.query({
                         page: 1,
                         pageSize: 10
                     });
                     vm.userDirectorySearch = {};
                 }
            }
            if(vm.radio === 1){
           	 vm.exportInfor = vm.MESearch;
           	 var grid = vm.unitListGrid;
                if (grid) {
                    grid.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });
                    vm.MESearch = {};
                }
           }
            if(vm.radio === 2){
              	 var grid = vm.docManagementGrid;
              	 vm.docManagementSearch = {};
                   if (grid) {
                       grid.dataSource.query({
                           page: 1,
                           pageSize: 10
                       });
                   }
              }
        }
        
        vm.edit = edit;
        function edit(dataItem) {
        	vm.isUpdate = true;
        	//Luồng sửa danh mục người dùng
        	if(vm.radio === 0){
				vm.userDirectorySearch = {
					...dataItem
				}
			}
        	
        	//Luồng sửa danh mục đơn vị
        	if(vm.radio === 1){
				vm.MESearch = {
					...dataItem
				}
			}
        	//Luồng sửa danh mục đơn vị
        	if(vm.radio === 2){
				vm.docManagementSearch = {
					...dataItem
				}
				if(dataItem.dateIssued != null){
					var dateSplit = dataItem.dateIssued.split('-');
					console.log(parseInt(dateSplit[0]), parseInt(dateSplit[1]), parseInt(dateSplit[2]))
					vm.docManagementSearch.dateIssued = new Date(parseInt(dateSplit[0]), parseInt(dateSplit[1]-1), parseInt(dateSplit[2]));
				}
				vm.docManagementSearch.attachFile = {
						...dataItem.attachFile
				};
				$('#fileName').text(dataItem.attachFile.name);
			}
		}
        vm.exportInfor = {};
        vm.exportFile = function exportFile() {
			if(vm.radio === 0){
				vm.userDirectorySearch.page = null;
				vm.userDirectorySearch.pageSize = null;
				manageMEService.doSearch(vm.exportInfor, vm.radio).then(function(d){
					var data = d.data;
					CommonService.exportFile(vm.userDirectoryGrid, data, vm.listRemove,vm.listConvert,"Danh mục đơn vị");
				});
			}	
			
			if(vm.radio === 1){
				vm.MESearch.page = null;
				vm.MESearch.pageSize = null;
				manageMEService.doSearch(vm.exportInfor, vm.radio).then(function(d){
					var data = d.data;
					CommonService.exportFile(vm.unitListGrid, data, vm.listRemove,vm.listConvert,"Danh mục người dùng");
				});
			}	
		}
        
        vm.listRemove = [{
            title: "Thao tác"
        }]
        vm.listConvert = [
        	
        ]
        
        vm.cleanAll = cleanAll;
        function cleanAll() {
        	vm.userDirectorySearch = {};
        	vm.MESearch = {};
        	vm.docManagementSearch = {};
        	vm.doSearch();
        	vm.isUpdate = false;
        	vm.exportInfor = {};
        	vm.fileLst = [];
        	$("#attachFile").val(null);
        	$('#fileName').text("Chưa có file tải lên")
		}
        
//        vm.uploadFile = uploadFile;
//        function uploadFile() {
//            var title="Chọn file upload";
//            var windowId="uploadFile";
//            var templateUrl = "coms/managerCategory/documentUploadFile.html";
//            CommonService.populatePopupCreate(templateUrl, title, null, vm, windowId, true, '1000', '275', null);
//        }
        vm.fileLst = null;
        vm.submitAttachFile = submitAttachFile;
        function submitAttachFile() {
			sendFile("attachFile", callback);
		}
        
        function sendFile(id, callback) {
			var files = $('#'+id)[0].files;
			if(files.length <= 0){
				toastr.warning("Bạn chưa chọn file tải lên");
				return;
			}
			if(!htmlCommonService.checkFileExtension(id)){
				toastr.warning("Định dạng file không hợp lệ");
				return;
			}
			
			var formData = new FormData();
	        jQuery.each($("#" + id)[0].files, function (i, files) {
		        formData.append('multipartFile' + i, files);
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
                vm.fileLst = file;
            }
            var grid = vm.docManagementGrid;
            $('#fileName').text( vm.fileLst.name);
            toastr.success("Tải lên thành công");
          
		}
 
    	vm.downloadFile = function (dataItem) {
			window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.attachFile.filePath;
		}
        
        //ducpm23-end
    }
})();
