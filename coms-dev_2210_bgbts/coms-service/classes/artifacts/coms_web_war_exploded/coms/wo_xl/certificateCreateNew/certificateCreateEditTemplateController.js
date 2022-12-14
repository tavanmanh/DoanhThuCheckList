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
				placeholder:"Nh???p m?? ho???c t??n ng??nh ngh???",
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
	                            console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
	                        });
	                    }
	                }
	            },
	            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
	            '<p class="col-md-6 text-header-auto border-right-ccc">M?? ng??nh ngh???</p>' +
	            '<p class="col-md-6 text-header-auto">T??n ng??nh ngh???</p>' +
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
						placeholder:"Nh???p m?? ho???c t??n nh??n vi??n",
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
			                                console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
			                            }
			                        );

			                    }
			                	
			                }
			            },
			            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
			            '<p class="col-md-6 text-header-auto border-right-ccc">M?? nh??n vi??n</p>' +
			            '<p class="col-md-6 text-header-auto">H??? t??n</p>' +
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
                toastr.error("M??/S??? ch???ng ch??? kh??ng ???????c ????? tr???ng.");
                return false;
            }

			if (!certificate.certificateName) {
			    toastr.error("T??n ch???ng ch??? kh??ng ???????c ????? tr???ng.");
			    return false;
			}

            if (!certificate.careerName) {
                toastr.error("L??nh v???c/ng??nh ngh??? kh??ng ???????c ????? tr???ng.");
                return false;
            }

            if (!certificate.practicePoint) {
                toastr.error("??i???m th???c h??nh kh??ng ???????c ????? tr???ng.");
                return false;
            }

            if (!certificate.theoreticalPoint) {
                toastr.error("??i???m l?? thuy???t kh??ng ???????c ????? tr???ng! ");
                return false;
            }

            if (!certificate.unitCreated) {
                toastr.error("????n v??? c???p kh??ng ???????c ????? tr???ng.");
                return false;
            }

            if (!certificate.startDate) {
                toastr.error("Ng??y c???p kh??ng ???????c ????? tr???ng.");
                return false;
            }

            if (!certificate.finishDate) {
                toastr.error("Ng??y h???t h???n kh??ng ???????c ????? tr???ng.");
                return false;
            }
            
            if (!certificate.loginName) { 
                toastr.error("M?? nh??n vi??n kh??ng ???????c ????? tr???ng.");
                return false;
            }

            if (!certificate.fullName) {
                    toastr.error("T??n nh??n vi??n kh??ng ???????c ????? tr???ng.");
                    return false;
            }

            if (!certificate.sysGroupName) {
                toastr.error("Chi nh??nh k?? thu???t kh??ng ???????c ????? tr???ng.");
                return false;
            }

            if (!certificate.email) {
                toastr.error("Email kh??ng ???????c ????? tr???ng.");
                return false;
            }
            
            if (!certificate.phoneNumber) {
                toastr.error("S??? ??i???n tho???i kh??ng ???????c ????? tr???ng.");
                return false;
            }

            if (!certificate.positionName) {
                toastr.error("Ch???c danh kh??ng ???????c ????? tr???ng.");
                return false;
            }
            
            var vnf_regex = /((09|03|07|08|05)+([0-9]{8})\b)/g;
			var phoneNumber = $('#phoneNumber').val();
			 if(phoneNumber !== ""){
				 if(vnf_regex.test(phoneNumber) == false){
					 $('#phoneNumber').focus();
					 toastr.warning("S??? ??i???n tho???i c???a nh??n vi??n n??y kh??ng h???p l???!");
					 return false;
				 } 
			}
			var email_regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/
			var email = $('#email').val();
			if(email !== ""){
					 if(email_regex.test(email) == false){
						 $('#email').focus();
						 toastr.warning("Email n??y kh??ng h???p l???!");
						 return false;
					 } 
			}
			if(kendo.parseDate(certificate.startDate, "dd/MM/yyyy") == null|| kendo.parseDate(certificate.finishDate, "dd/MM/yyyy") == null) {
				toastr.warning("?????nh d???ng ng??y kh??ng h???p l???");
				return false;
			} 
			if (kendo.parseDate(certificate.startDate, "dd/MM/yyyy").getFullYear() > 9999 || kendo.parseDate(certificate.startDate, "dd/MM/yyyy").getFullYear() < 1000) {
				toastr.warning("Ng??y b???t ?????u kh??ng h???p l???");
				return false;
			}
			if (kendo.parseDate(certificate.finishDate, "dd/MM/yyyy").getFullYear() > 9999 || kendo.parseDate(certificate.finishDate, "dd/MM/yyyy").getFullYear() < 1000) {
				toastr.warning("Ng??y h???t h???n kh??ng h???p l???");
				return false;
			}
			
			
			if (certificate.finishDate != null && certificate.finishDate != "") {
				if (kendo.parseDate(certificate.startDate, "dd/MM/yyyy") > kendo.parseDate(certificate.finishDate, "dd/MM/yyyy")) {
					toastr.warning("Ng??y b???t ?????u kh??ng ???????c l???n h??n ng??y h???t h???n!");
					return false;
				}			
			}
			
			var decimal_point_regex = /^\d+(?:\.\d{0,2})?$/
			var practicePoint = $('#practicePoint').val();
			var theoreticalPoint = $('#theoreticalPoint').val();
			if(practicePoint !== ""){
				if(decimal_point_regex.test(practicePoint) == false){
					$('#practicePoint').focus();
					toastr.warning("??i???m th???c h??nh kh??ng h???p l???.Vui l??ng nh???p s??? nguy??n, s??? th???p ph??n c?? 1 ho???c 2 ch??? s???.!");
					return false;
				} 
			}
			if(theoreticalPoint !== ""){
				if(decimal_point_regex.test(theoreticalPoint) == false){
					$('#theoreticalPoint').focus();
					toastr.warning("??i???m l?? thuy???t kh??ng h???p l???.Vui l??ng nh???p s??? nguy??n, s??? th???p ph??n c?? 1 ho???c 2 ch??? s???.!");
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
