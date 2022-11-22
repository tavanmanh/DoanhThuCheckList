(function () {
    'use strict';
    var controllerId = 'manageController';

    angular.module('MetronicApp').controller(controllerId, manageController);

    function manageController($scope, $rootScope, $timeout, gettextCatalog, $filter,
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
    
        vm.managerList = [
        	{id: 1, name: "Quản lý bảo dưỡng"}, 
        	{id: 2, name: "Quản lý sửa chữa"},
        ];
        
        vm.managerSearch = {
        	status: 1
        };
        
        vm.fileLst = [];
       
        vm.checkB2BB2C = false; 
        vm.String = "Quản lý WO > Quản lý cơ điện > Quản lý";
        
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
        	fillDataTable([]);
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
        
        function fillDataTable(data) {
        	kendo.ui.progress($("#manageMEGrid"), true);
        	vm.managerGridOptions = kendoConfig.getGridOptions({
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
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "manageMERsService/getManager",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                           vm.managerSearch.page = options.page;
                           vm.managerSearch.pageSize = options.pageSize;
                           return JSON.stringify(vm.managerSearch);
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
                        title: "Mã trạm",
                        field: 'stationCode',
                        width: "100px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Địa chỉ trạm",
                        field: 'address',
                        width: "150px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
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
                        title: "Quản lý",
                        field: 'managerUserName',
                        width: "150px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Mã WO",
                        field: 'woCode',
                        width: "100px",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        },
                    },
//                    {
//                        title: "WO",
//                        field: '',
//                        width: "150px",
//                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
//                        attributes: {
//                            style: "text-align:left;"
//                        }
//                    },
                    {
                        title: "Thời gian WO",
                        field: 'createdDate',
                        width: "150px",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    },
                    {
                        title: "Thời gian kết thúc WO",
                        field: 'finishDate',
                        width: "200px",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    },
                    {
                        title: "Thời gian CD tiếp nhận WO",
                        field: 'updateCdLevel3ReceiveWo',
                        width: "200px",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    },
                    {
                        title: "Thời gian FT tiếp nhận WO",
                        field: 'updateFtReceiveWo',
                        width: "200px",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    },
                    {
                        title: "Thời gian FT đóng WO",
                        field: 'endTime',
                        width: "200px",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    },
                    {
                        title: "Thời gian CD đóng WO",
                        field: 'updateCdApproveWo',
                        width: "200px",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    },
                    {
                        title: "Tình trạng",
                        field: 'state',
                        width: "100px",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    },
                    {
                        title: "Nguyên nhân từ chối",
                        field: 'overdueReason',
                        width: "200px",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    },
                    {
                        title: "File hình ảnh",
                        field: '',
                        width: "150px",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
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

		// export file
		vm.exportFile = function exportFile() {
			vm.managerSearch.page = null;
			vm.managerSearch.pageSize = null;
			manageMEService.doSearch(vm.managerSearch).then(function(data){
				CommonService.exportFile(vm.manageMEGrid, data.data, vm.listRemove, vm.listConvert, "Danh sách quản lý")
			}).catch(function(e){
				toastr.error("Có lỗi xảy ra khi xuất file ");
			})
		}

        vm.listRemove = [
        	{
        		title: "File hình ảnh"
        	}
        ]
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

//        vm.exportConstruction = exportConstruction;
//        function exportConstruction(dataItem) {
//            $http({
//                url: RestEndpoint.BASE_SERVICE_URL + "constructionService" + "/exportConstruction",
//                dataType: 'json',
//                method: 'POST',
//                data: dataItem,
//                headers: {
//                    "Content-Type": "application/json"
//                },
//                responseType: 'arraybuffer'// THIS IS IMPORTANT
//            }).success(function (data, status, headers, config) {
//                if (data.error) {
//                    toastr.error(data.error);
//                } else {
//                    var binarydata = new Blob([data], { type: 'application/*' });
//                    kendo.saveAs({ dataURI: binarydata, fileName: "HoSoCongTrinh.doc" });
//
//                }
//
//            })
//                .catch(function (data) {
//                    toastr.error("Có lỗi xảy ra!");
//                });
//        }

       
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
