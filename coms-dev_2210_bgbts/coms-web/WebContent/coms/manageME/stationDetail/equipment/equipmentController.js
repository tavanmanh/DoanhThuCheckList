(function () {
    'use strict';
    var controllerId = 'equipmentController';

    angular.module('MetronicApp').controller(controllerId, equipmentController);

    function equipmentController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter, $modal, $sce, $compile,
                                 kendoConfig, $kWindow, manageMEService, htmlCommonService, vpsPermissionService,stationDetailService,
                                 CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modalStack) {

        var vm = this;
        var today = new Date();
        vm.typeCheck = $rootScope.type;
        vm.deviceId = $rootScope.deviceId;
        vm.createdUser = $rootScope.createdUser;
        vm.createdDate = $rootScope.createdDate;
        vm.stationId = $rootScope.stationId;
        vm.type = $rootScope.type;
        vm.isShow = true;
        // variables
        vm.String = "Quản lý Cơ điện > Danh sách trạm > Chi tiết trạm cơ điện > Chi tiết thiết bị";
        vm.workingEquipment = {};
        vm.label = {};vm.labelSpan ={};
        vm.fileLst = [];
        vm.file = {};
        vm.folder='';
        vm.modalOpened = false;
        vm.isCreateNew = false;
        vm.showDetail = false;
        vm.viewPopupDetail = false;
        $scope.permissions = {};
        
        vm.electricalList = [
        	{id: 1, name: 'Lưới điện 1 pha'},
        	{id: 2, name: 'Lưới điện 3 pha'},
        	{id: 3, name: 'Trạm không điện'}
        ];
        
        vm.suppilerlist = [
        	{id: 1, name: 'EVN'},
        	{id: 2, name: 'Ngoài EVN'}
        ];
        
        vm.condutorQualityList = [
        	{id: 1, name: 'Tốt'},
        	{id: 2, name: 'Hỏng'}
        ];
        
        vm.electricalNetworkList = [
        	{id: 1, name: '2x2,5mm2'},
        	{id: 2, name: '2x4mm2'},
        	{id: 3, name: '2x6mm2'},
        	{id: 4, name: '2x10mm2'},
        	{id: 5, name: '2x16mm2'},
        	{id: 6, name: '2x25mm2'},
        	{id: 7, name: '2x35mm2'},
        	{id: 8, name: '2x50mm2'},
        	{id: 9, name: '2x70mm2'},
        	{id: 10, name: '3x10+6mm2'},
        	{id: 11, name: '3x16+10mm2'},
        	{id: 12, name: '3x25+16mm2'},
        	{id: 13, name: '3x35+16mm2'}, 
        	{id: 14, name: '3x50+25mm2'}, 
        	{id: 15, name: '3x70+35mm2'}, 
        	{id: 16, name: '4x10mm2'},
        	{id: 17, name: '4x11mm2'}, 
        	{id: 18, name: '4x16mm2'}, 
        	{id: 19, name: '4x25mm2'}, 
        	{id: 20, name: '4x35mm2'}, 
        	{id: 21, name: '4x50mm2'},
        	{id: 22, name: '4x70mm2'}
        ];
        
        vm.ideaList = [
        	  {id: 0, name: 'Không'},
              {id: 1, name: 'Có'}
        ]
        
        vm.wireTypelist = [
        	{id: 1, name: 'Nhôm'},
            {id: 2, name: 'Đồng'}
        ]
        vm.statuslist = [
        	{id: 2, name: 'Hỏng'},
            {id: 1, name: 'Tốt'}
        ]
        vm.ideaList2 = [
            {id: 1, name: 'Có'},
            {id: 2, name: 'Không'}
      ]
        vm.statelist = [
            {id: 1, name: 'Tốt'},
            {id: 2, name: 'Hỏng'}
        ]
        vm.statuslist2 = [
        	{id: 1, name: 'Cố định'},
            {id: 2, name: 'Lưu động'}
        ]
        vm.typeCodeUnitList=[
        	{id: 1, name: 'Inventer'},
            {id: 2, name: 'Thường'}
        ]
        vm.batteryName=[
        	{id: 1, name: 'ACCU1'},
        	{id: 2, name: 'ACCU2'},
        	{id: 3, name: 'ACCU3'},
        	{id: 4, name: 'ACCU4'}
        ]
        vm.goodCodeList=[
        	{id: 1, name: 'Lithium_ZTT_4850_P_82018'},
        	{id: 2, name: 'Lithium_ZTT_4850'},
        	{id: 3, name: 'Khác'}
        ]
        vm.electricFireExtinguisherList=[
        	{id: 1, name: 'Bình CO2 MT2'},
        	{id: 2, name: 'Bình CO2 MT24'},
        	{id: 3, name: 'Bình CO2 MT3'},
        	{id: 4, name: 'Bình CO2 MT5'},
        	{id: 5, name: 'Bình bột MFZ1'},
        	{id: 6, name: 'Bình bột MFZ2'},
        	{id: 7, name: 'Bình bột MFZ3'},
        	{id: 8, name: 'Bình bột MFZ4'},
        	{id: 9, name: 'Bình bột MFZ5'},
        	{id: 10, name: 'Quả cầu chữa cháy'}
        ]
        
        vm.boxNameList=[
        	{id: 1, name: 'AC box-1P'},
        	{id: 2, name: 'AC box-2P'},
        	{id: 3, name: 'AC box-3P'},
        	{id: 4, name: 'Cầu dao đảo-1P'},
        	{id: 5, name: 'Cầu dao đảo-3P'},
        	{id: 6, name: 'Tủ outdoor-1P'},
        	{id: 7, name: 'Tủ outdoor-3P'},
        	{id: 8, name: 'V1-1P'},
        	{id: 9, name: 'V1-3P'},
        	{id: 10, name: 'V2-1P'},
        	{id: 11, name: 'V2-3P'},
        	{id: 12, name: 'V3-1P'},
        	{id: 13, name: 'V3-3P'},
        	{id: 14, name: 'V5-1P'},
        	{id: 15, name: 'V5-3P'},
            {id: 16, name: 'Khác'}
        ]
        vm.nameDCList=[
        	{id: 1, name: 'DC1'},
            {id: 2, name: 'DC2'}
        ]
        vm.houseTypeList=[
        	{id: 1, name: 'Bệ bê tông'},
        	{id: 2, name: 'Cabin'},
        	{id: 3, name: 'Cải tạo'},
        	{id: 4, name: 'Nhà B40'},
        	{id: 5, name: 'Nhà máy nổ thiết kế chung với trạm'},
        	{id: 6, name: 'Nhà vượt lũ'},
            {id: 7, name: 'Xây độc lập'},
            {id: 8, name: 'Nhà trạm'}
        ]
        vm.stateExplosionFactory=[
        	{id: 1, name: 'Không đặt nhà máy nổ ( do NMN trên đồi cao, sông nước)'},
        	{id: 2, name: 'Không đặt nhà máy nổ ( do hạ tầng NMN không đảm bảo)'},
        	{id: 3, name: 'Không đặt nhà máy nổ ( do không có máy nổ)'},
        	{id: 4, name: 'Không đặt nhà máy nổ do nguyên nhân khác'},
        	{id: 5, name: 'Đã đặt nhà máy nổ'}
        ]
        vm.chungloaiSoCapList=[
        	{id: 1, name: 'GZ220'},
        	{id: 2, name: 'GZ250'},
        	{id: 3, name: 'GZ50'},
        	{id: 4, name: 'GZ500'},
        	{id: 5, name: 'SPARK_GAP_DEHN'},
        	{id: 6, name: 'SPARK_GAP_POUSURGE'},
            {id: 7, name: 'SPARK_GAP_POUSURGE'},
            {id: 8, name: 'Khác'}
        ]
        vm.chungloaiThuCapList=[
        	{id: 1, name: 'M3C85'},
        	{id: 2, name: 'MH2200'},
        	{id: 3, name: 'Khác'}
        ]
        vm.stateTiepDiaList=[
        	{id: 1, name: 'Bị hỏng, đứt gãy tiếp địa'},
        	{id: 2, name: 'Bị mất trộm dây tiếp địa'},
        	{id: 3, name: 'Trạm không có bãi tiếp địa'},
        	{id: 4, name: 'Trạm tiếp địa đảm bảo'}
        ]
        vm.groundResistanceList=[
        	{id: 1, name: '0-4'},
        	{id: 2, name: '> 4-10'},
        	{id: 3, name: '> 10-15'},
        	{id: 4, name: '> 15-20'},
        	{id: 5, name: '> 20'},
        	{id: 6, name: 'Điện trở không xác định'}
        ]
        vm.productionTechnologyList=[
        	{id: 1, name: 'Chì'},
        	{id: 2, name: 'Lithium'}
        ]
        init();
        function init() {
        	$scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
        	getDeviceDetails();
            vm.labelSpan = $rootScope.labelSpan;vm.label = $rootScope.label;
           
        }
        
        function getDeviceDetails() {
            var obj = {
            		type: vm.typeCheck,
            		deviceId: vm.deviceId
            		};
            
            stationDetailService.getDeviceDetails(obj).then(function(result){
        		if(result && result.data){
        			vm.device = result.data[0];
//        			vm.device.listImage1 = [];
				}
        		else toastr.error("Chưa có dữ liệu cấu hình loại thiết bị")
		  });
            
        };
        
        vm.submitAttachFile1 = submitAttachFile1;
		function submitAttachFile1(){
	    	sendFile("file1",callback);
	    }
		
		vm.submitAttachFile2 = submitAttachFile2;
		function submitAttachFile2(){
	    	sendFile("file2",callback);
	    }
		
		vm.submitAttachFile3 = submitAttachFile3;
		function submitAttachFile3(){
	    	sendFile("file3",callback);
	    }
		
		vm.submitAttachFile4 = submitAttachFile4;
		function submitAttachFile4(){
	    	sendFile("file4",callback);
	    }
		vm.submitAttachFile5 = submitAttachFile2;
		function submitAttachFile5(){
	    	sendFile("file5",callback);
	    }
		
		vm.submitAttachFile6 = submitAttachFile6;
		function submitAttachFile6(){
	    	sendFile("file6",callback);
	    }
		
		vm.submitAttachFile7 = submitAttachFile7;
		function submitAttachFile7(){
	    	sendFile("file7",callback);
	    }
		 function sendFile(id, callback){
		    	var files = $('#'+id)[0].files;
		    	if(!$("#"+id)[0].files[0]){
		    		toastr.warning("Bạn chưa chọn file");
		    		setTimeout(function () {
						$(".k-upload-files.k-reset").find("li").remove();
						$(".k-upload-files").remove();
						$(".k-upload-status").remove();
						$(".k-upload.k-header").addClass("k-upload-empty");
						$(".k-upload-button").removeClass("k-state-focused");
					}, 10);
		    		return;
		    	}
		    	if(!htmlCommonService.checkFileExtension(id)){
					toastr.warning("File không đúng định dạng cho phép");
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
				jQuery.each($("#"+id)[0].files, function(i, file) {
						 formData.append('multipartFile'+i, file);
				});
				
				return   $.ajax({
		            url:Constant.BASE_SERVICE_URL+"fileservice/uploadATTTImageAIO?folder="+ Constant.UPLOAD_FOLDER_IMAGES,
		            type: "POST",
		            data: formData,
		            enctype: 'multipart/form-data',
		            processData: false,
		            contentType: false,
		            cache: false,
		            success:function(data) {
		            	readImages(files, id);
		            	callback(data, files, id);
		            }	 
		        });
		    };
		    
		    vm.callback = callback;
			function callback(data, files, id){
				for(var i = 0; i< data.length; i++){
					var file = {};
		    		file.name=files[i].name;
		        	file.createdDate = htmlCommonService.getFullDate();
		        	file.createdUserName = Constant.userInfo.VpsUserInfo.fullName;
		        	file.createdUserId = Constant.userInfo.VpsUserInfo.sysUserId;
		        	file.filePath = data[i];
		        	if(id=="file1"){
		        		file.type="1";
//			        	vm.fileLstCmt.push(file);
		        	} 
		        	
		        	if(id=="file2"){
		        		file.type="2";
//			        	vm.fileLstHk.push(file);
		        	}
		        	
		        	if(id=="file3"){
		        		file.type="3";
//			        	vm.fileLstHd.push(file);
		        	}
		        	if(id=="file4"){
		        		file.type="4";
//			        	vm.fileLstHd.push(file);
		        	}
		        	if(id=="file5"){
		        		file.type="5";
//			        	vm.fileLstHd.push(file);
		        	}
		        	if(id=="file6"){
		        		file.type="6";
//			        	vm.fileLstHd.push(file);
		        	}
		        	if(id=="file7"){
		        		file.type="7";
//			        	vm.fileLstHd.push(file);
		        	}
		        	setTimeout(function () {
						$(".k-upload-files.k-reset").find("li").remove();
						$(".k-upload-files").remove();
						$(".k-upload-status").remove();
						$(".k-upload.k-header").addClass("k-upload-empty");
						$(".k-upload-button").removeClass("k-state-focused");
					}, 10);
				}

//		    	$('#filePackageMaterial').val(null);
//		    	saveImage();
			};
			
			function readImages(files, id) {
		        var errors = "";

		        if (!files) {
		            errors += "Trình duyệt không hỗ trợ, vui lòng sử dụng Chrome hoặc Firefox!";
		        }

		        if (files && files[0]) {
		            for (var i = 0; i < files.length; i++) {
		                var file = files[i];

		                if ((/\.(png|jpeg|jpg)$/i).test(file.name)) {
		                    readImage(file);
		                } else {
		                    errors += file.name + ": Không hỗ trợ định dạng!";
		                }

		            }
		        }

		        // Handle errors
		        if (errors) {
		            toastr.error(errors);
		        }

		        function readImage(file) {
		            var reader = new FileReader();
		            reader.addEventListener("load", function () {
		            	if(id=="file1"){
		            		vm.device.listImage1.push(toImageUtilObj(file.name, reader.result.substring(reader.result.indexOf(",") + 1)));
			        	} 
			        	
			        	if(id=="file2"){
			        		vm.device.listImage2.push(toImageUtilObj(file.name, reader.result.substring(reader.result.indexOf(",") + 1)));
			        	}
			        	
			        	if(id=="file3"){
			        		vm.device.listImage3.push(toImageUtilObj(file.name, reader.result.substring(reader.result.indexOf(",") + 1)));
			        	}

			        	if(id=="file4"){
			        		vm.device.listImage4.push(toImageUtilObj(file.name, reader.result.substring(reader.result.indexOf(",") + 1)));
			        	}
			        	if(id=="file5"){
			        		vm.device.listImage5.push(toImageUtilObj(file.name, reader.result.substring(reader.result.indexOf(",") + 1)));
			        	}
			        	
			        	if(id=="file6"){
			        		vm.device.listImage6.push(toImageUtilObj(file.name, reader.result.substring(reader.result.indexOf(",") + 1)));
			        	}

			        	if(id=="file7"){
			        		vm.device.listImage7.push(toImageUtilObj(file.name, reader.result.substring(reader.result.indexOf(",") + 1)));
			        	}
		                $scope.$apply();
		            });

		            reader.readAsDataURL(file);
		        }

		        function toImageUtilObj(name, base64String) {
		            return {
		                base64String: base64String,
		                name: name
		            }
		        }
		    }
			
        vm.saveDevice = saveDevice;
        function saveDevice() {
        	if(vm.device.electric != null){
        		vm.device.electric = parseInt(vm.device.electric);
        	}
        	
        	if(vm.typeCheck == 'LUOI_DIEN'){
        		vm.device = {
            			electricDetailId: vm.device.electricDetailId,
                		deviceId: vm.device.deviceId,
                		electric: vm.device.electric,
                		distance: vm.device.distance,
                		electricQuotaCBElectricMeterA: vm.device.electricQuotaCBElectricMeterA,
                		electricQuotaCBStationA: vm.device.electricQuotaCBStationA,
                		wireType: vm.device.wireType,
                		suppiler: vm.device.suppiler,
                		voltageAC: vm.device.voltageAC,
                		hostOpinion: vm.device.hostOpinion,
                		rateCapacityStation: vm.device.rateCapacityStation,
                		price: vm.device.price,
                		section: vm.device.section,
                		condutorQuality: vm.device.condutorQuality,
                		superficies: vm.device.superficies
                		};
                
            	stationDetailService.saveDeviceLD(vm.device).then(
                        function(resp){
                            if(resp && resp.statusCode == 1){
                                toastr.success("Update thành công!");
//                                publishChange();
                            }
                            else toastr.error("Update thất bại! " + resp.message);
                            $scope.isProcessing = false;
                        },
                        function(error){
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                            $scope.isProcessing = false;
                        }
                    )
        	}else if (vm.typeCheck == 'TU_NGUON_AC') {
        		vm.device = {
        				id: vm.device.id,
                		cabinetsSourceName: vm.device.cabinetsSourceName,
                		phaseNumber: vm.device.phaseNumber,
                		deviceId: vm.device.deviceId,
                		status: vm.device.status,
        		}
        		stationDetailService.saveDeviceTuNguonAC(vm.device).then(
                        function(resp){
                            if(resp && resp.statusCode == 1){
                                toastr.success("Update thành công!");
//                                publishChange();
                            }
                            else toastr.error("Update thất bại! " + resp.message);
                            $scope.isProcessing = false;
                        },
                        function(error){
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                            $scope.isProcessing = false;
                        }
                    )
			}else if (vm.typeCheck == 'TU_NGUON_DC') {
        		vm.device = {
        				cabinetsDCId: vm.device.cabinetsDCId,
                		deviceId: vm.device.deviceId,
                		preventtive: vm.device.preventtive,
                		cabinetsSourceDCName: vm.device.cabinetsSourceDCName,
                		stateCabinetsSourceDC: vm.device.stateCabinetsSourceDC,
                		powerCabinetMonitoring: vm.device.powerCabinetMonitoring ,
                		notChargeTheBattery: vm.device.notChargeTheBattery ,
                		chargeTheBattery: vm.device.chargeTheBattery ,
                		cbNumberLessThan30AUnused: vm.device.cbNumberLessThan30AUnused ,
                		cbNumberGreaterThan30AUnused: vm.device.cbNumberGreaterThan30AUnused ,
                		cbNymberAddition: vm.device.cbNymberAddition ,
                		stateRectifer: vm.device.stateRectifer ,
                		quantityUse: vm.device.quantityUse ,
                		quantityAddition: vm.device.quantityAddition ,
                		sireal: vm.device.sireal ,
                		numberDeviceModel: vm.device.numberDeviceModel ,
                		stateModule: vm.device.stateModule ,
                		state: vm.device.state ,
                		recfiterNumber: vm.device.recfiterNumber
        		}
        		stationDetailService.saveDeviceTuNguonDC(vm.device).then(
                        function(resp){
                            if(resp && resp.statusCode == 1){
                                toastr.success("Update thành công!");
//                                publishChange();
                            }
                            else toastr.error("Update thất bại! " + resp.message);
                            $scope.isProcessing = false;
                        },
                        function(error){
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                            $scope.isProcessing = false;
                        }
                    )
			}else if (vm.typeCheck == 'NHIET') {
        		stationDetailService.saveDeviceNHIET(vm.device).then(
                        function(resp){
                            if(resp && resp.statusCode == 1){
                                toastr.success("Update thành công!");
//                                publishChange();
                            }
                            else toastr.error("Update thất bại! " + resp.message);
                            $scope.isProcessing = false;
                        },
                        function(error){
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                            $scope.isProcessing = false;
                        }
                    )
			}else if (vm.typeCheck == 'DIEU_HOA_AC') {
        		stationDetailService.saveDeviceDHAC(vm.device).then(
                        function(resp){
                            if(resp && resp.statusCode == 1){
                                toastr.success("Update thành công!");
//                                publishChange();
                            }
                            else toastr.error("Update thất bại! " + resp.message);
                            $scope.isProcessing = false;
                        },
                        function(error){
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                            $scope.isProcessing = false;
                        }
                    )
			}else if (vm.typeCheck == 'THONG_GIO') {
        		stationDetailService.saveDeviceTG(vm.device).then(
                        function(resp){
                            if(resp && resp.statusCode == 1){
                                toastr.success("Update thành công!");
//                                publishChange();
                            }
                            else toastr.error("Update thất bại! " + resp.message);
                            $scope.isProcessing = false;
                        },
                        function(error){
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                            $scope.isProcessing = false;
                        }
                    )
			}else if (vm.typeCheck == 'DIEU_HOA_DC') {
        		stationDetailService.saveDeviceDHDC(vm.device).then(
                        function(resp){
                            if(resp && resp.statusCode == 1){
                                toastr.success("Update thành công!");
//                                publishChange();
                            }
                            else toastr.error("Update thất bại! " + resp.message);
                            $scope.isProcessing = false;
                        },
                        function(error){
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                            $scope.isProcessing = false;
                        }
                    )
			}
        	
        };
        
        
        vm.approveDevice = approveDevice;
        function approveDevice() {
            var obj = {
            		deviceId: vm.deviceId,
            		type: vm.type
            		};
            
            stationDetailService.approve(obj).then(function(result){
		  });
            vm.isShow = false;
            toastr.success("Phê duyệt thành công")
        };
        
        
        vm.reject=reject;
		function reject(dataItem){
			 vm.modalOpened = true;
	         $modal.open({
	        	   options: {
	                    modal: true,
	                    visible: false,
	                    width: '900',
	                    height: '500',
	                    actions: ["Minimize", "Maximize", "close"],
	                    open: function () {
	                        this.wrapper.children('.k-window-content').addClass("fix-footer");
	                    }
	                },
	                templateUrl: 'coms/wo_xl/certificateDetails/rejectReason.html',
	                controller: null,
	                windowClass: 'app-modal-window',
	                scope: $scope
	         }).result.then(
	              function () {
	            	  updateReason();
	                    vm.modalOpened = false;
	         },
	         function () {
	                    vm.modalOpened = false;
	         }
	       )
		};
		
		 vm.validateRejectReason = function () {
	            if (!vm.certificateExtend.rejectReason || vm.certificateExtend.rejectReason.trim() == '') {
	                toastr.error('Chưa nhập lý do từ chối!');
	                return false;
	            }
	            vm.device.reason = vm.certificateExtend.rejectReason;
	            return true;
	        };
	        
	        function updateReason() {
	        	var obj = {
	        			deviceId: vm.deviceId,
	        			reason:  vm.device.reason,
	        			createdUser: vm.createdUser,
	        			createdDate: vm.createdDate,
	        			stationId: vm.stationId,
	        			type: vm.type
	        	};
				obj.status = 3 ;
				obj.reason  = vm.certificateExtend.rejectReason;
				stationDetailService.reject(obj).then(
						function (resp) {
	                        toastr.success("Xác nhận cập nhật thành công");
	                        vm.isShow = false;
	                    },
	                    function (err) {
	                        console.log(err);
	                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại .");
	                    }
	            );
	        }
        
        vm.chooseTypeDevice = function chooseTypeDevice(data) {
        	vm.stationSearch.type = data;
        	fillDataEquipment();
			
        };

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
