(function () {
    'use strict';
    var controllerId = 'certificateCreateEditTemplateController';

    angular.module('MetronicApp').controller(controllerId, certificateCreateEditTemplateController);

    function certificateCreateEditTemplateController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                            kendoConfig, $kWindow, htmlCommonService, vpsPermissionService,certificateCreateNewService,
                                            CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $compile) {

        $scope.isDisable = {};
        $scope.label = {};
        $scope.isCreateNew = true;
        $scope.tthtSysGroupId = '242656';
        $scope.vhktSysGroupId = '270120';
        $scope.xdddSysGroupId = '9006003';
        $scope.gpthSysGroupId = '280483';
        $scope.xddthtSysGroupId = '166677';
        $scope.minDate = new Date();
        $scope.fileLst = [];        
        init();

        function init() {
            $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
            if ($scope.isEditting) {
                $scope.isCreateNew = false;
            }
            if($scope.isCreateNew == true){
            	$scope.workingCertificate.finishDate = new Date();
    			var currentDay = kendo.toString($scope.workingCertificate.finishDate, "dd/MM/yyyy");
    			$scope.workingCertificate.finishDate = currentDay;

            }
            	
            
        }
        
        
//        $scope.changeDataAuto=changeDataAuto;
//		function changeDataAuto(id){
//			switch(id){
//				case 'userlogin':{
//	        		if (processSearch(id, $scope.selectedDept2)) {
//	        			$scope.workingCertificate.sysUserId = null;
//		            	$scope.workingCertificate.loginName = null;
//		            	$scope.workingCertificate.fullName = null;
//		                $scope.workingCertificate.sysGroupName = null;
//		                $scope.workingCertificate.email = null;
//		                $scope.workingCertificate.phoneNumber = null;
//		                $scope.workingCertificate.positionName = null;
//		                $scope.selectedDept2 = false;
//	        		}
//	        	}
//				case 'career1':{
//	        		if (processSearch(id, $scope.selectedCareer1)) {
//	        			$scope.workingCertificate.careerId = null;
//	        			$scope.workingCertificate.careerName = null;
//	        			$scope.selectedCareer1 = false;
//	        		}
//	        	}
//				 
//			}
//		}
//        
        $scope.autoCompleteCareerOptions={
				dataTextField: "name", 
				placeholder:"Nhập mã hoặc tên ngành nghề",
				open: function(e) {
				},
	            select: function(e) {
	            	$scope.selectedCareer1 = true;
	            	data = this.dataItem(e.item.index());
	            	$scope.workingCertificate.careerId = data.careerId;
	            	$scope.workingCertificate.careerName = data.name;
	            	$scope.$apply();
	            },
	            pageSize: 10,
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function(options) {
	                    	var keySearch = $scope.workingCertificate.careerName;
	                        $scope.workingCertificate.careerId = null;
	                        if (keySearch == '') {
	                            $scope.selectedCareer1 = false;         
	                            $scope.workingCertificate.careerId = null;
	                            $scope.workingCertificate.careerName = null;
	                        }
	                        var objSearch = {
	                        	name: keySearch,
	                            page: 1,
	                            pageSize: 10
	                        };
	                        
	                    	$scope.selectedCareer1 = false;
	                        return Restangular.all("managementCareerRsService/getCareerPopup").post(objSearch).then(function(response){
	                            options.success(response);
	                        }).catch(function (err) {
	                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
	                        });
	                    }
	                }
	            },
	            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
	            '<p class="col-md-6 text-header-auto border-right-ccc">Mã ngành nghề</p>' +
	            '<p class="col-md-6 text-header-auto">Tên ngành nghề</p>' +
	            	'</div>',
	            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.code #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
		        change: function(e) {
		        	if ($scope.workingCertificate.careerId == null || $scope.workingCertificate.careerName == null) {
		        		  $scope.selectedCareer1 = false;
		        		  $scope.workingCertificate.careerId = null;
                    	  $scope.workingCertificate.careerName = null;
                    	  return;
	                }	
		        },
	            close: function(e) { 
	            	$scope.selectedCareer1 = true;
//	            	$scope.workingCertificate.careerId = null;
//                	  $scope.workingCertificate.careerName = null;
	            }
		};
        
        
		$scope.autoCompleteEmployeeOptions={
						dataTextField: "employeeCode", 
						placeholder:"Nhập mã hoặc tên nhân viên",
						open: function(e) {
						},
			            select: function(e) {
			            	$scope.selectedDept2 = true;
			            	data = this.dataItem(e.item.index());
			            	$scope.workingCertificate.sysUserId = data.sysUserId;
			            	$scope.workingCertificate.loginName = data.employeeCode;
			            	$scope.workingCertificate.fullName = data.fullName;
			                $scope.workingCertificate.sysGroupName = data.sysGroupName;
			                $scope.workingCertificate.email = data.email;
			                $scope.workingCertificate.phoneNumber = data.phoneNumber;
			                $scope.workingCertificate.positionName = data.position;              
		                    $scope.$apply();
			            },
			            pageSize: 10,
			            dataSource: {
			                serverFiltering: true,
			                transport: {
			                	read: function (options) {
			                        var keySearch = $scope.workingCertificate.loginName;
			                        $scope.workingCertificate.sysUserId = null;
			                        if (keySearch == '') {
			                            $scope.selectedDept2 = false;         
			                            $scope.workingCertificate.sysUserId = null;
		    			            	$scope.workingCertificate.loginName = null;
		    			            	$scope.workingCertificate.fullName = null;
		    			                $scope.workingCertificate.sysGroupName = null;
		    			                $scope.workingCertificate.email = null;
		    			                $scope.workingCertificate.phoneNumber = null;
		    			                $scope.workingCertificate.positionName = null;
			                        }
			                        var objSearch = {
			                        	loginName: keySearch,
			                            page: 1,
			                            pageSize: 10
			                        };
			                        
			                        return Restangular.all("sysUserCOMSRsService/getUserInforDetail").post(objSearch).then(
			                            function (response) {
			                                options.success(response);
			                            }
			                        ).catch(
			                            function (err) {
			                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
			                            }
			                        );

			                    }
			                	
			                }
			            },
			            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
			            '<p class="col-md-6 text-header-auto border-right-ccc">Mã nhân viên</p>' +
			            '<p class="col-md-6 text-header-auto">Họ tên</p>' +
			            	'</div>',
			            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.employeeCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
				        change: function(e) {
				        	if ($scope.workingCertificate.sysUserId == null || $scope.workingCertificate.loginName == null) {
			                    $scope.selectedDept2 = false;
			                    $scope.workingCertificate.loginName = null;
			                    $scope.workingCertificate.fullName = null;
				                $scope.workingCertificate.sysGroupName = null;
				                $scope.workingCertificate.email = null;
				                $scope.workingCertificate.phoneNumber = null;
				                $scope.workingCertificate.positionName = null;
			                    return;
			                }	
				        },
			            close: function(e) { 
			            	$scope.selectedDept2 = true;    
//                            $scope.workingCertificate.sysUserId = null;
//			            	$scope.workingCertificate.loginName = null;
//			            	$scope.workingCertificate.fullName = null;
//			                $scope.workingCertificate.sysGroupName = null;
//			                $scope.workingCertificate.email = null;
//			                $scope.workingCertificate.phoneNumber = null;
//			                $scope.workingCertificate.positionName = null;
			                  
			           }
		};

        $scope.resetNewCertificateForm = function () {
            $scope.workingCertificate = {};

        }
        
        $scope.validateCreateNew = validateCertificate;

        function validateCertificate() {
            var certificate = $scope.workingCertificate;
            if (!certificate.certificateCode) {
                toastr.error("Mã/Số chứng chỉ không được để trống.");
                return false;
            }

			if (!certificate.certificateName) {
			    toastr.error("Tên chứng chỉ không được để trống.");
			    return false;
			}

            if (!certificate.careerName) {
                toastr.error("Lĩnh vực/ngành nghề không được để trống.");
                return false;
            }

            if (!certificate.practicePoint) {
                toastr.error("Điểm thực hành không được để trống.");
                return false;
            }

            if (!certificate.theoreticalPoint) {
                toastr.error("Điểm lý thuyết không được để trống! ");
                return false;
            }

            if (!certificate.unitCreated) {
                toastr.error("Đơn vị cấp không được để trống.");
                return false;
            }

            if (!certificate.startDate) {
                toastr.error("Ngày cấp không được để trống.");
                return false;
            }

            if (!certificate.finishDate) {
                toastr.error("Ngày hết hạn không được để trống.");
                return false;
            }
            
            if (!certificate.loginName) { 
                toastr.error("Mã nhân viên không được để trống.");
                return false;
            }

            if (!certificate.fullName) {
                    toastr.error("Tên nhân viên không được để trống.");
                    return false;
            }

            if (!certificate.sysGroupName) {
                toastr.error("Chi nhánh kĩ thuật không được để trống.");
                return false;
            }

            if (!certificate.email) {
                toastr.error("Email không được để trống.");
                return false;
            }
            
            if (!certificate.phoneNumber) {
                toastr.error("Số điện thoại không được để trống.");
                return false;
            }

            if (!certificate.positionName) {
                toastr.error("Chức danh không được để trống.");
                return false;
            }
            
            var vnf_regex = /((09|03|07|08|05)+([0-9]{8})\b)/g;
			var phoneNumber = $('#phoneNumber').val();
			 if(phoneNumber !== ""){
				 if(vnf_regex.test(phoneNumber) == false){
					 $('#phoneNumber').focus();
					 toastr.warning("Số điện thoại của nhân viên này không hợp lệ!");
					 return false;
				 } 
			}
			var email_regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/
			var email = $('#email').val();
			if(email !== ""){
					 if(email_regex.test(email) == false){
						 $('#email').focus();
						 toastr.warning("Email này không hợp lệ!");
						 return false;
					 } 
			}
			if(kendo.parseDate(certificate.startDate, "dd/MM/yyyy") == null|| kendo.parseDate(certificate.finishDate, "dd/MM/yyyy") == null) {
				toastr.warning("Định dạng ngày không hợp lệ");
				return false;
			} 
			if (kendo.parseDate(certificate.startDate, "dd/MM/yyyy").getFullYear() > 9999 || kendo.parseDate(certificate.startDate, "dd/MM/yyyy").getFullYear() < 1000) {
				toastr.warning("Ngày bắt đầu không hợp lệ");
				return false;
			}
			if (kendo.parseDate(certificate.finishDate, "dd/MM/yyyy").getFullYear() > 9999 || kendo.parseDate(certificate.finishDate, "dd/MM/yyyy").getFullYear() < 1000) {
				toastr.warning("Ngày hết hạn không hợp lệ");
				return false;
			}
			
			
			if (certificate.finishDate != null && certificate.finishDate != "") {
				if (kendo.parseDate(certificate.startDate, "dd/MM/yyyy") > kendo.parseDate(certificate.finishDate, "dd/MM/yyyy")) {
					toastr.warning("Ngày bắt đầu không được lớn hơn ngày hết hạn!");
					return false;
				}			
			}
			
			var decimal_point_regex = /^\d+(?:\.\d{0,2})?$/
			var practicePoint = $('#practicePoint').val();
			var theoreticalPoint = $('#theoreticalPoint').val();
			if(practicePoint !== ""){
				if(decimal_point_regex.test(practicePoint) == false){
					$('#practicePoint').focus();
					toastr.warning("Điểm thực hành không hợp lệ.Vui lòng nhập số nguyên, số thập phân có 1 hoặc 2 chữ số.!");
					return false;
				} 
			}
			if(theoreticalPoint !== ""){
				if(decimal_point_regex.test(theoreticalPoint) == false){
					$('#theoreticalPoint').focus();
					toastr.warning("Điểm lí thuyết không hợp lệ.Vui lòng nhập số nguyên, số thập phân có 1 hoặc 2 chữ số.!");
					return false;
				} 
			}
		 practicePoint = practicePoint.replace(/^[0.]+/, "");
		 theoreticalPoint = theoreticalPoint.replace(/^[0.]+/, "");
		 var practice = (parseFloat(practicePoint)).toFixed(2) ;
       	 var theoretical =(parseFloat(theoreticalPoint)).toFixed(2);
       	 $scope.workingCertificate.practicePoint =    parseFloat(practice);
       	 $scope.workingCertificate.theoreticalPoint = parseFloat(theoretical);
         return true;
        }

        $scope.resetFormFile = resetFormFile;
        
        function resetFormFile() {
            $("#customerFile").val(null);
            $scope.fileLst = [];
        }
        
        $scope.downloadFile1 = function(dataItem){
			window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + dataItem.attachFilePath;	
    	}
        
    }
})();
