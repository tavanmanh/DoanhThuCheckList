(function () {
    'use strict';
    var controllerId = 'stationDetailController';

    angular.module('MetronicApp').controller(controllerId, stationDetailController);

    function stationDetailController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter, $modal, $sce, $compile,
                                 kendoConfig, $kWindow, manageMEService, htmlCommonService, vpsPermissionService,stationDetailService,
                                 CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modalStack) {

        var vm = this;
        var today = new Date();
        
        // variables
        vm.String = "Quản lý Cơ điện > Danh sách trạm> Chi tiết trạm cơ điện";
        vm.workingStation = {};
        vm.lstDevice = {};
        vm.label = {};
        $scope.workingStation = [];
        $scope.loggedInUser = $rootScope.casUser.userName;
        $scope.sysUserId = $rootScope.casUser.sysUserId;
        $scope.sysUserGroup = {};
        $scope.enable = {};
        $scope.headerColor = {'background-color': '#33CCCC'};
        
        vm.fileLst = [];
       
        vm.file = {};
        vm.folder='';
        vm.modalOpened = false;
        vm.isCreateNew = false;
        vm.showDetail = false;
        vm.viewPopupDetail = false;
        vm.addForm = {};
        init();
        function init() {
            	$scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
            	var obj = {};
            	stationDetailService.getEquipments(obj).then(function(result){
	        		if(result && result.data){
	        			vm.workingStation.listEquipmentAll = result.data;
					}else toast.error("Chưa có dữ liệu cấu hình loại thiết bị")
			  });
              if ($rootScope.viewDetailStationId) getStationDetails($rootScope.viewDetailStationId);
            
        }
        
//        function subStation() {
//            postal.subscribe({
//                channel: "Tab",
//                topic: "action",
//                callback: function (data) {
//                    if (data.action == 'refresh' &&
//                        vm.workingStation.stationId &&
//                        data.stationId != vm.workingStation.stationId
//                    ) {
//                    	getStationDetails(data.stationId, true);
//                    }
//                }
//            });
//        }

        function getStationDetails(stationId, refresh) {
            var obj = {stationId: stationId};
            vm.workingStation = $rootScope.workingStation;
            vm.stationSearch= {};
            vm.stationSearch.type = 'LUOI_DIEN';
            vm.stationSearch.stationId = stationId;
            fillDataEquipment();
			if (refresh == true) {
				if (vm.EquipmentHistoryOptions) {
					vm.EquipmentHistory.dataSource.read();
				}
	        }
            $rootScope.viewDetailStationId = null;
        };
        
        vm.addEquipment = function () {
        	vm.fileLst = [];
            var teamplateUrl = "coms/manageME/stationDetail/addNewDevicePopup.html";
            var title = "Thêm mới thiết bị";
            var windowId = "DEVICE";
            vm.addForm.type = vm.stationSearch.type;
            vm.addForm.stationId = vm.stationSearch.stationId;
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1100', '500', null);
        }
        
        vm.updateDevice = function(dataItem) {
        	var teamplateUrl = "coms/manageME/stationDetail/addNewDevicePopup.html";
            var title = "Update thiết bị";
            var windowId = "DEVICE";
            vm.addForm = dataItem;
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1100', '500', null);
		}
			vm.updateBroken = function(dataItem) {
				var teamplateUrl = "coms/manageME/stationDetail/updateBroken.html";
				var title = "Ghi nhận sản lượng";
				var windowId = "DEVICE";
				vm.addForm = JSON.parse(JSON.stringify(dataItem));
				vm.addForm.descriptionFailure = vm.addForm.failureString || vm.addForm.descriptionFailure
				CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '500', '300', null);
			}

        
        vm.submitAttachFile = submitAttachFile;
        function submitAttachFile() {
            sendFile("attachfiles", callback);
        }
        
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
//            for (var i = 0; i < data.length; i++) {
                var file = {};
                file.name = files[0].name;
                file.createdDate = htmlCommonService.getFullDate();
                file.createdUserName = Constant.userInfo.vpsUserToken.fullName;
                file.createdUserId = Constant.userInfo.vpsUserToken.sysUserId;
                file.filePath = data[0];
                file.type = 0;
                vm.fileName = file.name;
                vm.fileLst = [];
                vm.fileLst.push(file);
//            }
//            var grid = !!vm.contractFileGrid ? vm.contractFileGrid : vm.contractFileEditGrid;
//            refreshGrid(grid, vm.fileLst);
            //      	vm.fileLst = $("#contractFileGrid").data("kendoGrid").dataSource.data();
//            vm.fileLst = grid.dataSource.data();
//            $('#attachfiles').val(null);
//            $('#attachfilesEdit').val(null);
        }
        
//        function refreshGrid(d) {
//            var grid = vm.constructionGrid;
//            if (grid) {
//                grid.dataSource.data(d);
//                grid.refresh();
//            }
//        }
        
        vm.saveDevice = function() {
        	vm.addForm.createUser = $scope.sysUserId;
        	var data = angular.copy(vm.addForm);
        	if(vm.fileLst.length == 0)  {
        		toastr.warning("Vui lòng tải lên file!");
        		return;
        	}
        	else data.attachFile = angular.copy(vm.fileLst[0]);
        	if(data.deviceId == null){
        		stationDetailService.saveDevice(data).then(function (result) {
                    if (result && result.error) {
                        toastr.error(result.error);
                        return;
                    }
                    toastr.success("Thêm mới thành công!");
                    vm.addForm = [];
                    vm.cancel();
	                  fillDataEquipment();
	                  if (vm.EquipmentHistoryOptions) {
	      				vm.EquipmentHistory.dataSource.read();
	      			}
                    
                }, function (errResponse) {
                	if (errResponse.status === 409) {
	                	toastr.error(gettextCatalog.getString("Mã thiết bị đã tồn tại !"));
	                } else {
	                	 toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi thêm mới"));
	                }
                   
                });
        	}else{
        		vm.device = {
        				deviceId: data.deviceId,
        				deviceName: data.deviceName,
        				type: data.type,
        				status: data.status,
        				deviceCode: data.deviceCode,
        				stationId: data.stationId,
        				state: data.state,
        				serial: data.serial,
        				createDate: data.createDate,
        				reason: data.reason,
        				createUser: data.createUser,
        				attachFile: data.attachFile
        		}
        		stationDetailService.updateDevice(vm.device).then(function (result) {
                    if (result && result.error) {
                        toastr.error(result.error);
                        return;
                    }
                    toastr.success("Cập nhật thành công!");
                    vm.addForm = [];
                    vm.cancel();
	                  fillDataEquipment();
	                  if (vm.EquipmentHistoryOptions) {
	      				vm.EquipmentHistory.dataSource.read();
	      			}
                }, function (errResponse) {
                	if (errResponse.status === 409) {
	                	toastr.error(gettextCatalog.getString("Mã thiết bị đã tồn tại !"));
	                } else {
	                	toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi cập nhật"));
	                }
                    
                });
        	}
        	
		}

			vm.approveBroken = function() {
				vm.addForm.createUser = $scope.sysUserId;
				var data = angular.copy(vm.addForm);
				if (data.failureStatus != 2 && data.failureStatus != 3) {
					toastr.error("Yêu cầu chọn kết quả phê duyệt");
					return;
				}

				if (data.failureStatus  == 2 && (data.qoutaTime === null || data.qoutaTime === undefined)) {
					toastr.error("Yêu cầu nhập định mức hoàn thành");
					return;
				}
				if (data.failureStatus  == 2 && data.qoutaTime < 0) {
					toastr.error("Định mức hoàn thành phải >= 0");
					return;
				}
				if (data.failureStatus  == 2 && !data.finishDate) {
					toastr.error("Yêu cầu nhập ngày hoàn thành");
					return;
				}
				// if (!data.describeAfterMath) {
				// 	toastr.error("Yêu cầu nhập hậu nghiệm");
				// 	return;
				// }

				stationDetailService.updateBroken(data).then(function (result) {
					if (result && result.error) {
						toastr.error(result.error);
						return;
					}
					toastr.success("Cập nhật thành công!");
					vm.addForm = [];
					vm.cancel();
					fillDataEquipment();
					if (vm.EquipmentHistoryOptions) {
						vm.EquipmentHistory.dataSource.read();
					}

				}, function (errResponse) {
						toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi cập nhật"));

				});


			}


//        taotq
        /*vm.addEquipment = function () {
            var template = Constant.getTemplateUrl('EQUIPMENT_DETAIL');
            bindInterfaceFollowTypeEquipment(vm.stationSearch.type);
            postal.publish({
                channel: "Tab",
                topic: "open",
                data: template
            });
        };*/
        
        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };
        
        vm.viewDetail = function (dataItem) {
        	var equipmentId = dataItem.equipmentId;
        	removeTab("Chi tiết thiết bị"); 
            var template = Constant.getTemplateUrl('EQUIPMENT_DETAIL');
            $rootScope.viewDetailsEquipmentId = equipmentId;
            template.equipmentId = equipmentId;
            bindInterfaceFollowTypeEquipment(vm.stationSearch.type);
            $rootScope.type = dataItem.type;
            
            $rootScope.type = dataItem.type;
            $rootScope.deviceId = dataItem.deviceId;
            $rootScope.createdUser = dataItem.createdUser;
            $rootScope.createdDate = dataItem.createdDate;
            $rootScope.stationId = dataItem.stationId;
            postal.publish({
                channel: "Tab",
                topic: "open",
                data: template
            });

            postal.publish({
                channel: "Tab",
                topic: "action",
                data: {action: 'refresh', equipmentId: equipmentId}
            });
        };

        function removeTab (title) {
            for(var i=0; i<$scope.tabs.length; i++){
            	if($scope.tabs[i].title==title){
            		$scope.tabs.splice(i, 1);
            		break;
            	}
            }
		};
		
		vm.bindInterfaceFollowTypeEquipment = bindInterfaceFollowTypeEquipment;

        function bindInterfaceFollowTypeEquipment(type) {
        	$rootScope.labelSpan ={};$rootScope.label ={};$rootScope.title = {};
            switch (type) {
            	//Duoc phep them moi
                case 'LUOI_DIEN':
                	$rootScope.title.title1 ='Thông tin chung';
                	$rootScope.labelSpan = {
                		span1 : 'Lưới điện',span2 : 'Khoảng cách từ công to đến tạm BTS(m)',
                		span3 : 'Dòng điện định mức của CB tại công tơ điện lực(A)',span4 : 'Dòng điện định mức của CB tại trạm(A)',
                		span5 : 'Loại dây',span6 : 'Đơn vị cung cấp',
                		span7 : 'Điện áp AC (do ở vi trí trạm thời điểm giờ cao điểm)',span8 : 'Chủ nhà đồng ý cho xây dựng nhà đặt máy phát điện',
                		span9 : 'Chi phí thuê đất/tháng(VND)',span10 : 'Công suất định danh của trạm (kW)',	
                		span11 : 'Tiết diện',span12 : 'Chất lượng của dây dẫn',	
                		
                	};
                	$rootScope.label = {
                		information1 : 'Loại',information2 : 'Khoảng cách',information3 : 'Giá trị dòng điện',information4 : 'Giá trị dòng điện',
                		information5 : 'Loại dây',information6 : 'Đơn vị',information7 : 'Điện áp AC',information8 : 'Ý kiến chủ nhà',
                		information9 : 'Chi phí thuê đất/tháng',information10 : 'Kích thước tối đa có thể xây dựng(dài x rộng)',
                		information11 : 'Công suất định danh của trạm (kW)',
                		information12 : 'Tiết diện',information13 : 'Chất lượng của dây dẫn'
                	};
                    break;
                case 'TU_NGUON_AC':
                	$rootScope.title.title1 ='Thông tin chung';
                	$rootScope.labelSpan = {
                		span1 : 'Loại',span2 : 'Số pha',span3 : 'Tình trạng'
                		
                	};
                	$rootScope.label = {
                		information1 : 'Loại',information2 : 'Loại',information3 : 'Tình trạng'
                	};
                    break;
                case 'CUU_HOA':
                	$rootScope.title.title1 ='Thông tin chung';
                	$rootScope.title.title2 ='Thông tin sử dụng';
                	$rootScope.labelSpan = {
                		span1 : 'Loại',span2 : 'Cân nặng',span3 : 'Tình trạng',
                		span4 : 'Vị trí bình cứu hỏa',span5 : 'Thời gian đưa vào sử dụng',span6 : 'Thời gian bảo dưỡng lần gần nhất'
                		
                	};
                	$rootScope.label = {
                		information1 : 'Loại',information2 : 'Cân nặng',information3 : 'Tình trạng',
                		information4 : 'Vị trí',information5 : 'Thời gian đưa vào sử dụng',information6 : 'Thời gian bảo dưỡng lần gần nhất'
                	};
                    break;
                case 'CANH_BAO':
                	$rootScope.title.title1 ='Thông tin sử dụng';
                	$rootScope.labelSpan = {
                		span1 : 'Hiện trạng loại AC box',span2 : 'Tình trạng loại AC box',
                		span3 : 'Hiện trạng cảnh báo ắc quy yếu',span4 : 'Tình trạng cảnh báo ắc quy yếu',
                		span5 : 'Hiện trạng cảnh báo nhiệt độ',span6 : 'Tình trạng cảnh báo nhiệt độ',
                		span7 : 'Hiện trạng loại khói',span8 : 'Tình trạng loại khói',
                		span9 : 'Hiện trạng cảnh báo sự cố tủ nguồn',span10 : 'Tình trạng cảnh báo sự cố tủ nguồn',
                		span11 : 'Hiện trạng cảnh báo mở cửa nhà máy nổ',span12 : 'Tình trạng cảnh báo mở cửa nhà máy nổ',
                		span13 : 'Hiện trạng cảnh báo mở cửa nhà trạm',span14 : 'Tình trạng cảnh báo mở cửa nhà trạm',
                		span15 : 'Hiện trạng cảnh báo AC yếu',span16 : 'Tình trạng cảnh báo AC yếu'
                		
                		
                	};
                	$rootScope.label = {
                		information1 : 'Hiện trạng',information2 : 'Tình trạng',information3 : 'Hiện trạng',information4 : 'Tình trạng',
                		information5 : 'Hiện trạng',information6 : 'Tình trạng',information7 : 'Hiện trạng',information8 : 'Tình trạng',
                		information9 : 'Hiện trạng',information10 : 'Tình trạng',information11 : 'Hiện trạng',information12 : 'Tình trạng',
                		information13 : 'Hiện trạng',information14 : 'Tình trạng',information15 : 'Hiện trạng',information16 : 'Tình trạng'
                	};
                    break;
                case 'ATS':
                	$rootScope.title.title1 ='Thông tin chung';
                	$rootScope.title.title2 ='Thông tin sử dụng';
                	$rootScope.labelSpan = {
                		span1 : 'Tên hàng hóa',span2 : 'Tên hàng hóa cơ điện',
                		span3 : 'Serial',span4 : 'Model',
                		span5 : 'Mã hàng hóa KTTS',span6 : 'Hãng sản xuất',
                		span7 : 'Số pha',span8 : 'Dòng điện định mức(A)',
                		span9 : 'Tình trạng',
                		span10 : 'Thời gian đưa vào sử dụng',span11 : 'Thời gian bảo dưỡng lần gần nhất'
                		
                		
                	};
                	$rootScope.label = {
                		information1 : 'Tên hàng hóa',information2 : 'Tên hàng hóa cơ điện',
                		information3 : 'Serial',information4 : 'Model',
                		information5 : 'Mã hàng hóa KTTS',information6 : 'Hãng sản xuất',
                		information7 : 'Số pha',information8 : 'Dòng điện định mức(A)',
                		information9 : 'Tình trạng',
                		information10 : 'Thời gian đưa vào sử dụng',information11 : 'Thời gian bảo dưỡng lần gần nhất'
                	};
                    break;
                case 'MAY_NO':
                	$rootScope.title.title1 ='Thông tin chung';
                	$rootScope.title.title2 ='Thông tin sử dụng';
                	$rootScope.labelSpan = {
                		span1 : 'Loại nhà',span2 : 'Tên',
                		span3 : 'Thời gian đặt máy nổ',span4 : 'Thời gian đưa vào sử dụng'
                		
                	};
                	$rootScope.label = {
                		information1 : 'Loại nhà',information2 : 'Tên',
                		information3 : 'Thời gian đặt máy nổ',information6 : 'Thời gian đưa vào sử dụng'
                	};
                    break;
                case 'LOC_SET':
                	$rootScope.title.title1 ='Thông tin chung';$rootScope.title.title2 ='Hiện trạng sơ cấp';
                	$rootScope.title.title3 ='Hiện trạng thứ cấp';$rootScope.title.title4 ='Hiện trạng bộ cắt lọc sét khác';
                	$rootScope.labelSpan = {
                		span1 : 'Tên',
                		span2 : 'Hiện trạng',span3 : 'Số lượng',span4 : 'Tình trạng',
                		span5 : 'Chủng loại',span6 : 'Điện trở',
                		span7 : 'Hiện trạng',span8 : 'Số lượng',
                		span9 : 'Tình trạng',span10 : 'Chủng loại',
                		span11 : 'Tên model',span12 : 'Tình trạng',
                		span13 : 'Thời gian đưa vào sử dụng',span14 : 'Thời gian bảo dưỡng lần gần nhất'
                		
                		
                	};
                	$rootScope.label = {
                		information1 : 'Tên',information2 : 'Hiện trạng',information3 : 'Số lượng',information4 : 'Tình trạng',
                		information5 : 'Chủng loại',information6 : 'Điện trở',
                		information7 : 'Hiện trạng',information8 : 'Số lượng',
                		information9 : 'Tình trạng',information10 : 'Chủng loại',
                		information11 : 'Tên model',information12 : 'Tình trạng',
                		information13 : 'Thời gian đưa vào sử dụng',information14 : 'Thời gian bảo dưỡng lần gần nhất'
                	};
                    break;
                case 'TIEP_DIA':
                	$rootScope.title.title1 ='Thông tin chung';
                	$rootScope.labelSpan = {
                		span1 : 'Điện trở tiếp địa',span2 : 'Tên hệ thống',span3 : 'Hiện trạng tiếp địa',span4 : 'Thời gian bảo dưỡng lần gần nhất'
                		
                	};
                	$rootScope.label = {
                		information1 : 'Điện trở',information2 : 'Tên',information3 : 'Hiện trạng',information4 : 'Thời gian'
                	};
                    break;
                //Dong bo
                case 'PHAT_DIEN':
                    $rootScope.title.title1 ='Thông tin chung';
                    $rootScope.title.title2 ='Thông tin sử dụng';
                	$rootScope.labelSpan = {
                		span1 : 'Tên',span2 : 'Model',
                		span3 : 'Công suất định danh(KVA)',span4 : 'Hãng sản xuất',
                		span5 : 'Loại nhiên liệu',span6 : 'Công suất max(KVA)',
                		span7 : 'Serial máy',
                		span8 : 'Tình trạng máy tại trạm',
                		
                		span9 : 'Khoảng cách từ vị trí đặt máy phát tới trạm',
                		span10 : 'Trạng thái',span11 : 'Mã trạm trước khi điều chuyển',
                		
                		span12 : 'Tổng thời gian chạy máy',
                		span13 : 'Thời gian đưa vào sử dụng',span14 : 'Thời gian sửa chữa gần nhất',
                		span15 : 'Tổng chi phí sửa chữa',span16 : 'Tổng số lần hỏng hóc',
                		span17 : 'Máy phát điện quá công suất',span18 : 'F1 xác nhận MPĐ quá công suất'
                		
                		
                	};
                	$rootScope.label = {
                		information1 : 'Tên',information2 : 'Model',information3 : 'Công suất định danh',information4 : 'Hãng sản xuất',
                		information5 : 'Loại nhiên liệu',information6 : 'Công suất max',information7 : 'Serial',information8 : 'Tình trạng',
                		information9 : 'Khoảng cách',information10 : 'Trạng thái',information11 : 'Mã trạm',information12 : 'Tổng thời gian',
                		information13 : 'Thời gian đưa vào sử dụng',information14 : 'Thời gian sửa chữa gần nhất',information15 : 'Tổng chi phí',
                		information16 : 'Tổng số lần hỏng hóc',information17 : 'Máy phát điện quá công suất',information15 : 'Xác nhận'
                	};
                    break;
               case 'TU_NGUON_DC':
                    $rootScope.title.title1 ='Dự phòng';
                    $rootScope.title.title2 ='Thông tin chung';
                    $rootScope.title.title3 ='Số CB DC';
                    $rootScope.title.title4 ='Rectifier';
                    $rootScope.title.title5 ='Module điều khiển';
                	$rootScope.labelSpan = {
                		span1 : 'Dự phòng',span2 : 'Tên tủ nguồn',
                		span3 : 'Tình trạng cắt lọc sét tủ nguồn DC',span4 : 'Giám sát tủ nguồn',
                		span5 : 'Không nạp ắc quy(A)',span6 : 'Nạp ắc quy(A)',
                		span7 : 'Số CB nhỏ hơn 30A chưa sử dụng',
                		span8 : 'Số CB lớn hơn 30A chưa sử dụng',
                		
                		span9 : 'Số CB có thể lắp thêm',
                		span10 : 'Số lượng đảm bảo dự phòng',span11 : 'Tình trạng',
                		
                		span12 : 'Số lượng đang sử dụng',
                		span13 : 'Số lượng có thể lắp thêm',span14 : 'Serial',
                		span15 : 'Tên thiết bị/Model',span16 : 'Tình trạng'
                		
                		
                		
                	};
                	$rootScope.label = {
                		information1 : 'Tình trạng dự phòng',information2 : 'Loại',information3 : 'Tình trạng',information4 : 'Tình trạng giám sát',
                		information5 : 'Dòng điện không nạp ắc quy',information6 : 'Dòng điện nạp ắc quy',information7 : 'Số CB',information8 : 'Số CB',
                		information9 : 'Số CB',information10 : 'Số lượng',information11 : 'Tình trạng',information12 : 'Số lượng',
                		information13 : 'Số lượng',information14 : 'Số Serial',information15 : 'Tên',
                		information16 : 'Tình trạng'
                	};
                    break;
               case 'RECTIFIER':
	               	$rootScope.title.title1 ='Thông tin chung';
	               	$rootScope.labelSpan = {
	               		span1 : 'Serial',span2 : 'Tình trạng KTTS',	
	               		span3 : 'Mã hàng hóa',span4 : 'Tên hàng hóa',
	               		span5 : 'Hãng sản xuất',span6 : 'Model',
	               		span7 : 'Số lượng đang sử dụng',span8 : 'Công suất định danh(A)',
	               		span9 : 'Số lượng có thể lắp thêm',
	               		span10 : 'Thời gian đưa vào sử dụng',span11 : 'Tình trạng'
	               		
	               		
	               	};
	               	$rootScope.label = {
	               		information1 : 'Serial',information2 : 'Tình trạng',
	               		information3 : 'Mã hàng hóa',information4 : 'Tên hàng hóa',
	               		information5 : 'Hãng',information6 : 'Model',
	               		information7 : 'Số lượng',information8 : 'Tình trạng KTTS',
	               		information9 : 'Số lượng',
	               		information10 : 'Thời gian',information11 : 'Tình trạng'
	               	};
                    break;
               case 'THONG_GIO':
	               	$rootScope.title.title1 ='Thông tin chung';
	               	$rootScope.labelSpan = {
	               		span1 : 'Tên',span2 : 'Serial',	
	               		span3 : 'Trạng thái Serial',span4 : 'Mã hàng hóa',
	               		span5 : 'Hãng sản xuất',span6 : 'Model',
	               		span7 : 'Công suất tối đa',span8 : 'Tình trạng KTTS',
	               		span9 : 'Trạng thái',span10 : 'Thời gian đưa vào sử dụng',
	               		span11 : 'Thời gian bảo dưỡng gần nhất',span12 : 'Thời gian sửa chữa gần nhất',
	               		span13 : 'Tổng chi phí sửa chữa',span14 : 'Tổng số lần hỏng hóc'
	               		
	               		
	               	};
	               	$rootScope.label = {
	               		information1 : 'Tên',information2 : 'Số Serial',
	               		information3 : 'Trạng thái Serial',information4 : 'Mã hàng hóa',
	               		information5 : 'Hãng',information6 : 'Model',
	               		information7 : 'Công suất tối đa',information8 : 'Tình trạng KTTS',
	               		information9 : 'Trạng thái',information10 : 'Thời gian đưa vào sử dụng',
	               		information11 : 'Thời gian bảo dưỡng gần nhất',information12 : 'Thời gian sửa chữa gần nhất',
	               		information13 : 'Tổng chi phí',information14 : 'Tổng số lần hỏng hóc'
	               	};
                    break;
            }
        }
        
        vm.chooseTypeDevice = function chooseTypeDevice(data,name) {
        	vm.stationSearch.type = data;
        	vm.stationSearch.typeName = name;
        	fillDataEquipment();
        	if (vm.EquipmentHistoryOptions) {
				vm.EquipmentHistory.dataSource.read();
			}
			
        };

        vm.deleteDevice = function (data) {
        	vm.device = {
    				deviceId: data.deviceId,
    				deviceName: data.deviceName,
    				type: data.type,
    				status: '2',
    				deviceCode: data.deviceCode,
    				stationId: data.stationId,
    				state: data.state,
    				serial: data.serial,
    				createDate: data.createDate,
    				reason: data.reason,
    				createUser: data.createUser,
    				attachFile: data.attachFile
    		}
            confirm('Xác nhận xóa thiết bị trên.Bạn có chắc chắn muốn xóa không?',
                function () {
            	stationDetailService.updateDevice(vm.device).then(function (resp) {
                            toastr.success("Xóa thành công!");
                            vm.cancel();
                            fillDataEquipment();
                            if (vm.EquipmentHistoryOptions) {
                				vm.EquipmentHistory.dataSource.read();
                			}
                        	
                        },
                        function (error) {
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                        }
                    )
                }
            );
        };

        function reloadTables() {
        	if (vm.EquipmentHistory) {
        		var sizePage = $("#equipmentHistory").data("kendoGrid").dataSource.total();
				var pageSize = $("#equipmentHistory").data("kendoGrid").dataSource.pageSize();
				
				if(sizePage % pageSize === 1){
					var currentPage = $("#equipmentHistory").data("kendoGrid").dataSource.page();
					if (currentPage > 1) {
						$("#equipmentHistory").data("kendoGrid").dataSource.page(currentPage - 1);
					}
				 }
				 $("#equipmentHistory").data('kendoGrid').dataSource.read();
				 $("#equipmentHistory").data('kendoGrid').refresh();
				 
        	}
        }

//        function createActionTemplate(dataItem) {
//            var template = '<div class="display-block cedtpl" style="text-align: center">' +
//             '<i class="fa fa-list-alt icon-table" uib-tooltip="Chi tiết" ng-click="vm.viewDetail(dataItem)" translate></i>'+
//             '<i class="fa fa-pencil icon-table" uib-tooltip="Sửa" ng-click="vm.updateDevice(dataItem)" translate></i>'+
//			 '<i class="fa fa-close" style="color: red; position: relative; left: 10px; " uib-tooltip="Xóa" ng-click="vm.deleteDevice(dataItem)" translate></i></div>'
//            return template;
//        }
        function createActionTemplate(dataItem) {
            var template = '<div class="display-block cedtpl" style="text-align: center">' +
             '<i class="fa fa-list-alt icon-table" uib-tooltip="Chi tiết" ng-click="vm.viewDetail(dataItem)" translate></i>'+
             '<i class="fa fa-pencil icon-table" uib-tooltip="Sửa" ng-click="vm.updateDevice(dataItem)" translate></i>'+
							'<i class="fa fa-check icon-table" ng-if ="dataItem.failureStatus == 1" uib-tooltip="Duyệt báo hỏng" ng-click="vm.updateBroken(dataItem)" translate></i>'+
			 				'<i class="fa fa-close" style="color: red; position: relative; left: 10px; " uib-tooltip="Xóa" ng-click="vm.deleteDevice(dataItem)" translate></i>'
								+ '</div>'
            return template;
        }
        var record = 0;

        function fillDataEquipment() {
            vm.EquipmentHistoryOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save: function () {
                	vm.EquipmentHistory.dataSource.read();
                },
                dataBinding: function () {
                	record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                             return response.total;
                        },
                        data: function (response) {
//                        	if (response.data[0].status == 2) vm.isProposeExtend = true;
//                        	else vm.isProposeExtend = false;
//                          if($("#CertificateExtendHistory").data("kendoGrid").dataSource.data()[0].status == 2){
//                               	vm.isProposeExtend = true;
//            				}
//                           $scope.checklists = response.data;
                            return response.data;
                        }
                    },
                    
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "manageMERsService/station/device/getDevices",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var obj = {
                            	stationId: vm.stationSearch.stationId,
                            	type: vm.stationSearch.type,
                            	page : options.page,
                                pageSize : options.pageSize
                            };
                            return JSON.stringify(obj)

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
                    refresh: true,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: ''
                    }
                },
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '8%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',

                    },
                    {
                        title: "Tên thiết bị",
                        field: 'deviceName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;"},
                        attributes: {style: "text-align:center;overflow: visible;white-space: normal;"},
                        type: 'text',

                    },
                    {
                        title: "Serial",
                        field: 'serial',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;"},
                        attributes: {style: "text-align:center;overflow: visible;white-space: normal;"},
                        type: 'text',

                    },
                    {
    					title: "File đính kèm",
    			        field: 'attachFileName',
    			        width: '30%',
    			        headerAttributes: {
    						style: "text-align:center;overflow: visible;white-space: normal;"
    					},
    					attributes: {
    						style: "text-align:center;"
    					},
						template :  function(dataItem) {
							if(dataItem.attachFileName == null){
								return "";
							}else{
								return "<a href='' ng-click='vm.downloadFile(dataItem)'>" + dataItem.attachFileName + "</a>";
							}
						}
    				},
                    {
                        title: "Trạng thái",
                        field: 'state',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                        	if (dataItem.state == '1' ) return 'Mới tạo';
                        	else if (dataItem.state == '2' ) return 'Chờ phê duyệt';
                        	else if (dataItem.state ==  '3') return 'Đã phê duyệt';
                        	else if (dataItem.state ==  '4') return 'Từ chối';
                        },

                    },
									{
										title: "Trạng thái báo hỏng",
										field: 'failureStatus',
										width: '30%',
										headerAttributes: {style: "text-align:center;font-weight: bold;"},
										attributes: {
											style: "text-align:center;"
										},
										type: 'text',
										template: function (dataItem) {
											if (dataItem.failureStatus == '1' ) return 'Chờ duyệt';
											else if (dataItem.failureStatus == '2' ) return 'Đã duyệt';
											else if (dataItem.failureStatus ==  '3') return 'Từ chối duyệt';
											return "";
										},

									},
//                    {
//    					title: "Ngày tạo",
//    			        field: 'createdDate',
//    			        width: '12%',
//    			        headerAttributes: {
//    						style: "text-align:center;"
//    					},
//    					attributes: {
//    						style: "text-align:left;"
//    					},
//    				},
                    {
                        title: "Thao tác",
                        field: 'action',
                        width: '215px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center; min-width: 210px !important;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return createActionTemplate(dataItem);
                        }
                    }
                ]
            });
        }

        vm.downloadFile = function(dataItem){
			window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + dataItem.attachFileExtendPath;	
    	}
        
			
        function publishChange() {
            postal.publish({
                channel: "Tab",
                topic: "action",
                data: {action: 'refresh'}
            });
        }

        vm.cancel = function(){
        	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

        vm.cancelImport = cancelImport;
        function cancelImport() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }
        
    }
})();
