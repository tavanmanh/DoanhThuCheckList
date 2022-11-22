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
       
    	var start = new Date();
    	start.setMonth(start.getMonth() - 6);
    	var end = new Date();
    	
    	
        vm.MESearch = {
        	status:1
        };
        
        vm.fileLst = [];
        
        vm.radio = 0;
        
        vm.optionList = [
        	{id: 0, name: "Lịch sử thay đổi máy phát điện"},
        	{id: 1, name: "Lịch sử thay đổi ắc quy"},
        	{id: 2, name: "Lịch sử thay đổi thông tin"},
        ];
        vm.checkB2BB2C = false; 
        vm.String = "Quản lý WO > Quản lý cơ điện > Lịch sử thay đổi";
        
        vm.allowAction = false;
        vm.openCatProvincePopup = openCatProvincePopup;
        vm.onSaveCatProvince = onSaveCatProvince;
        

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
        	fillDataHistoryGeneratorTable([]);
        	fillDataHistoryBatteryTable([]);
        	fillDataHistoryInformationTable([]);
        }
        
        $scope.changeImage = changeImage;
        $scope.imageSelected = {};
        
        function changeImage(image) {
            $scope.imageSelected = image;
        }

       
// function validate() {
// var isValid = true;
// if (vm.totalMonthPlan.year == undefined || vm.totalMonthPlan.year == '') {
// toastr.warning("Năm không được để trống!");
// return false;
// }
// if (vm.totalMonthPlan.month == undefined || vm.totalMonthPlan.month == '') {
// toastr.warning("Tháng không được để trống!");
// return false;
// }
// if (vm.totalMonthPlan.code == undefined || vm.totalMonthPlan.code == '') {
// toastr.warning("Mã kế hoạch không được để trống!");
// return false;
// }
// if (vm.totalMonthPlan.name == undefined || vm.totalMonthPlan.name == '') {
// toastr.warning("Tên kế hoạch không được để trống!");
// return false;
// }
// return isValid;
// }

        vm.cancelImport = cancelImport;
        function cancelImport() {
            CommonService.dismissPopup1();
        }
        var record = 0;
        
        // bảng lịch sử thay đổi máy phát điện
        function fillDataHistoryGeneratorTable(data) {
        	kendo.ui.progress($("#manageMEGrid"), true);
        	vm.historyGeneratorOptions = kendoConfig.getGridOptions({
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
                            '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                            '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                            '<label ng-repeat="column in vm.manageMEGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                            '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                            '</label>' +
                            '</div></div>'
                    }
                ],
                dataSource: {
                   // chưa viết
                	//URL: manageMERsService/history/getGenerator
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
                        title: "Mã trạm",
                        field: 'stationCode',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(dataItem){
							return '<a href="javascript:void(0);" ng-click="vm.showDetailStation(dataItem)">' + dataItem.stationCode + '</a>';
						}
                    },
                    {
                        title: "Serial được gán",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Serial gỡ",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Trạng thái đặt",
                        field: 'manageUserName',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Thời gian thay đổi",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Người thay đổi",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Công suất",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Lý do thay đổi",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Loại máy phát điện",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    }
                ]
            });
        }
        
        // bảng lịch sử thay đổi ác quy
        function fillDataHistoryBatteryTable(data) {
        	kendo.ui.progress($("#manageMEGrid"), true);
        	vm.historyChangeBatteryOptions = kendoConfig.getGridOptions({
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
                            '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                            '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                            '<label ng-repeat="column in vm.manageMEGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                            '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                            '</label>' +
                            '</div></div>'
                    }
                ],
                dataSource: {
                   // chưa viết
                	//URL: manageMERsService/history/getBattery
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
                        title: "Mã trạm hiện tại",
                        field: 'stationCode',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(dataItem){
							return '<a href="javascript:void(0);" ng-click="vm.showDetailStation(dataItem)">' + dataItem.stationCode + '</a>';
						}
                    },
                    {
                        title: "Người thay đổi",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Thời gian thay đổi",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                    	title: "Thông tin thay đổi",
                    	width : '300px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
                    	columns: [
                    		{
                                title: "Mã trạm điều chuyển đến",
                                field: '',
                                width: 20,
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:left;"
                                }
                            },
                            {
                                title: "Mã kho",
                                field: '',
                                width: 20,
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:left;"
                                },
                            }
                    	]
                    },
                    
                    {
                        title: "Lý do thay đổi",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    }
                ]
            });
        }
        
        // bảng lịch sử thay đổi thông tin
        function fillDataHistoryInformationTable(data) {
        	kendo.ui.progress($("#manageMEGrid"), true);
        	vm.historyInformationOptions = kendoConfig.getGridOptions({
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
                            '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                            '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                            '<label ng-repeat="column in vm.manageMEGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                            '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                            '</label>' +
                            '</div></div>'
                    }
                ],
                dataSource: {
                	//URL: manageMERsService/history/getInformation
               /* 	 serverPaging: true,
                     schema: {
                         total: function (response) {
                             $timeout( function(){ 
                             	vm.countCons = response.total;
                             } );
                             //console.log(response.total)
                             return response.total;
                         },
                         data: function (response) {
                         	kendo.ui.progress($("#manageMEGrid"), false);
                         	return response.data;
                         }
                     },
                     transport: {
                         read: {
                             // Thuc hien viec goi service
                             url: Constant.BASE_SERVICE_URL + "manageMERsService/station/doSearch",
                             contentType: "application/json; charset=utf-8",
                             type: "POST"
                         },
                         parameterMap: function (options, type) {
                             vm.MESearch.page = options.page
                             vm.MESearch.pageSize = options.pageSize
                             return JSON.stringify(vm.MESearch)

                         }
                     },
                     pageSize: 10*/
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
                        title: "Người thay đổi",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(dataItem){
							return '<a href="javascript:void(0);" ng-click="vm.showDetailStation(dataItem)">' + dataItem.stationCode + '</a>';
						}
                    },
                    {
                        title: "Mã trạm",
                        field: 'stationCode',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Ngày tác động",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Loại phương thức",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Dữ liệu thay đổi",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Dữ liệu trước cập nhật",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Dữ liệu sau cập nhật",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Hình thức thay đổi",
                        field: '',
                        width: 20,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    }
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


		vm.exportFile = function exportFile() {
			vm.MESearch.page = null;
			vm.MESearch.pageSize = null;
			manageMEService.doSearch(vm.MESearch).then(function(d){
				var data = d.data;
				CommonService.exportFile(vm.manageMEGrid,data,vm.listRemove,vm.listConvert,"Danh sách các trạm cơ điện");
			}).catch(function(e) {
				toastr.error("Có lỗi xảy ra khi xuất file !!!");
			});	
		}

        vm.listRemove = [{
            title: "Thao tác"
        }]
        vm.listConvert = [
        	{
	            field: "status",
	            data: {
	                1: 'Hiệu lực',
	                0: 'Hết Hiệu lực'
	            }
        	}
        ]
       


        function refreshGrid(d) {
            var grid = vm.constructionGrid;
            if (grid) {
                grid.dataSource.data(d);
                grid.refresh();
            }
        }
       
        vm.cancelDoSearch = function () {
            vm.showDetail = false;
            vm.MESearch = {
                status: "1"
            };
            doSearch();
        };

        vm.doSearch = doSearch;
        function doSearch() {
            vm.showDetail = false;
            var grid = vm.manageMEGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
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

        vm.cancelInput = function (param, form) {
        	if (param == "province") {
                vm.MESearch.provinceId = null;
                vm.MESearch.catProvince = null;
             }
        }


        vm.cancelPopup = cancelPopup;
        function cancelPopup() {
            $rootScope.$broadcast("handlerReturnFromPopup", "ok");
            $(".k-icon.k-i-close").click();
            kendo.ui.progress($("#constructionGrid"), false);
        }
        
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

// vm.exportConstruction = exportConstruction;
// function exportConstruction(dataItem) {
// $http({
// url: RestEndpoint.BASE_SERVICE_URL + "constructionService" +
// "/exportConstruction",
// dataType: 'json',
// method: 'POST',
// data: dataItem,
// headers: {
// "Content-Type": "application/json"
// },
// responseType: 'arraybuffer'// THIS IS IMPORTANT
// }).success(function (data, status, headers, config) {
// if (data.error) {
// toastr.error(data.error);
// } else {
// var binarydata = new Blob([data], { type: 'application/*' });
// kendo.saveAs({ dataURI: binarydata, fileName: "HoSoCongTrinh.doc" });
//
// }
//
// })
// .catch(function (data) {
// toastr.error("Có lỗi xảy ra!");
// });
// }

       
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
            var grid = !!vm.contractFileGrid ? vm.contractFileGrid : vm.contractFileEditGrid;
            refreshGrid(grid, vm.fileLst);
            // vm.fileLst =
			// $("#contractFileGrid").data("kendoGrid").dataSource.data();
            vm.fileLst = grid.dataSource.data();
            $('#attachfiles').val(null);
            $('#attachfilesEdit').val(null);
        }


        vm.submitAttachFile = submitAttachFile;
        function submitAttachFile() {
            sendFile("attachfiles", callback);
        }
        vm.submitAttachFileEdit = submitAttachFileEdit;
        function submitAttachFileEdit() {
            sendFile("attachfilesEdit", callback);
        }
        function refreshGrid(grid, data) {
            grid.dataSource.data(data);
            grid.refresh();
        }
        
        
        
        vm.downloadFile = function (dataItem) {
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
        }
        
        vm.checkNum = checkNum;
        function checkNum() {
            $("#conHighHTCT, #conCapexHTCT").keypress(function (event) {
            	var $this = $(this);
                if (!htmlCommonService.validateIntKeydown(event, $this.val())) {
                    event.preventDefault();
                }
                var text = $(this).val().replace(/,/g, "");
                if ($(this).val() == 0) {
                    text = '';
                }
                if (text.length >= 19) {
                    event.preventDefault();
                }
            });
        }
        
        function openCatProvincePopup(){
    		var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm tỉnh");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
        }

        function onSaveCatProvince(data){
            vm.MESearch.catProvince = data.name;
            vm.MESearch.provinceId = data.catProvinceId;
            htmlCommonService.dismissPopup();
            $("#province").focus();
	    };
	    
	    vm.directional = function (dataItem) {
            var teamplateUrl = "coms/manageME/directionalPopup.html";
            var title = "Điều phối quản lý trạm";
            var windowId = "Directional";
            vm.addForm = dataItem;
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '800', '350', null);
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
        
        vm.saveManageStation = function() {
			if(vm.addForm.manageUserCode == null || vm.addForm.manageUserCode == ""){
				toastr.warning("Mã nhân viên quản lý không được để trống !");
            	return false;
			}
			manageMEService.saveManageStation(vm.addForm).then(
	                function (res) {
	                    if (res && res.statusCode != null) {
	                        toastr.success("Thành công !");
	                        cancelImport();
	                    } else {
	                        toastr.error("Có lỗi xảy ra!");
	                    }
	                }
	            )
		}
    }
})();
