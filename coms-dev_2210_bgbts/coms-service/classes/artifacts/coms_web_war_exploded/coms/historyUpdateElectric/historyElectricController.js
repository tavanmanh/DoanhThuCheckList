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
       
        vm.checkB2BB2C = false; 
        vm.String = "Quản lý WO > Quản lý cơ điện > Lịch sử sửa chữa";
        
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
        	fillDataTable([]);
        }
        
        $scope.changeImage = changeImage;
        $scope.imageSelected = {};
        
        function changeImage(image) {
            $scope.imageSelected = image;
        }

       
//        function validate() {
//            var isValid = true;
//            if (vm.totalMonthPlan.year == undefined || vm.totalMonthPlan.year == '') {
//                toastr.warning("Năm không được để trống!");
//                return false;
//            }
//            if (vm.totalMonthPlan.month == undefined || vm.totalMonthPlan.month == '') {
//                toastr.warning("Tháng không được để trống!");
//                return false;
//            }
//            if (vm.totalMonthPlan.code == undefined || vm.totalMonthPlan.code == '') {
//                toastr.warning("Mã kế hoạch không được để trống!");
//                return false;
//            }
//            if (vm.totalMonthPlan.name == undefined || vm.totalMonthPlan.name == '') {
//                toastr.warning("Tên kế hoạch không được để trống!");
//                return false;
//            }
//            return isValid;
//        }

        vm.cancelImport = cancelImport;
        function cancelImport() {
            CommonService.dismissPopup1();
        }

        var record = 0;
        function fillDataTable(data) {
        	kendo.ui.progress($("#manageMEGrid"), true);
        	vm.historyElectricOptions = kendoConfig.getGridOptions({
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
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $timeout( function(){ 
                            	vm.countCons = response.total;
                            } );
                            return response.total;
                        },
                        data: function (response) {
                        	kendo.ui.progress($("#manageMEGrid"), false);
                        	return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service lấy danh sách wo
                        	url: Constant.BASE_SERVICE_URL + "manageMERsService/history/getUpdateElectric",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.MESearch.page = options.page
                            vm.MESearch.pageSize = options.pageSize
                            console.log((vm.MESearch))
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
                        width: "60px",
                        columnMenu: false,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Chi tiết ảnh",
                        field: '',
                        width: "150px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
//                       
                    },
                    {
                        title: "Mã trạm",
                        field: 'stationCode',
                        width: "200px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Mã WO",
                        field: 'woCode',
                        width: "200px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tên thiết bị",
                        field: 'deviceName',
                        width: "150px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Serial",
                        field: 'serial',
                        width: "100px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tình trạng thiết bị",
                        field: 'status',
                        width: "150px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        },
//                        template: function(dataItem) {
//                            if(dataItem.status === 1) return "Hiệu lực";
//                            else return "No";
//                         }
                    },
                    {
                        title: "Trạng thái",
                        field: 'state',
                        width: "100px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Người tạo",
                        field: 'manageUserName',
                        width: "150px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Ngày tạo",
                        field: 'createdDate',
                        width: "150px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                        
                    },
                    {
                        title: "Từ ngày",
                        field: 'startTime',
                        width: "150px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Đến ngày",
                        field: 'endTime',
                        width: "150px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Người phê duyệt",
                        field: 'userTthtApproveWo',
                        width: "150px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Ngày phê duyệt",
                        field: 'updateTthtApproveWo',
                        width: "150px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Mô tả hư hỏng",
                        field: 'descriptionFailure',
                        width: "200px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Mô tả hậu nghiệm",
                        field: 'describeAftemath',
                        width: "200px",
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
				CommonService.exportFile(vm.manageMEGrid,data,vm.listRemove,vm.listConvert,"Lịch sử sửa chữa");
			}).catch(function(e) {
				toastr.error("Lỗi khi xuất flie !!!");
			});
				
		}
		
		vm.listRemove = [
			{ title: "Chi tiết hình ảnh" }
		];
		
		vm.listConvert = [
			{
				field: "status",
				data: {
					0: "Không hiệu lực",
					1: "Hiệu lực"
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
//        function validatekendodatepicker() {
//            $('#handoverDate1').click(function () {
//                var todayDate = kendo.toString(kendo.parseDate(new Date()), 'dd/MM/yyyy');
//                $("#handoverDate").data("kendoDatePicker").value(todayDate);
//            });
//            $('#startingDate1').click(function () {
//                var todayDate = kendo.toString(kendo.parseDate(new Date()), 'dd/MM/yyyy');
//                $("#startingDate").data("kendoDatePicker").value(todayDate);
//            });
//            $('#excpectedCompleteDate1').click(function () {
//                var todayDate = kendo.toString(kendo.parseDate(new Date()), 'dd/MM/yyyy');
//                $("#excpectedCompleteDate").data("kendoDatePicker").value(todayDate);
//            });
//        }
       
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

        
      

        
//        vm.getExcelTemplate = function () {
//            constructionService.downloadTemplate({}).then(function (d) {
//                var data = d.plain();
//                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
//            }).catch(function () {
//                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
//                return;
//            });
//        };



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
            //      	vm.fileLst = $("#contractFileGrid").data("kendoGrid").dataSource.data();
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
        
        function onSaveCatProvince(data){
            vm.MESearch.catProvince = data.name;
            vm.MESearch.provinceId = data.catProvinceId;
            htmlCommonService.dismissPopup();
            $("#province").focus();
	    };
	    
	    vm.cancelListYear = function(){
	    	vm.MESearch.toDate = null;
	    	vm.MESearch.fromDate = null;
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
        
    }
})();
