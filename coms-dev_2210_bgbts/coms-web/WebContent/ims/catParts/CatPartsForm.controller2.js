(function () {
    'use strict';

    var controllerId = 'CatPartsFormController';

    angular.module('MetronicApp').controller(controllerId, CatPartsFormController);
    
    /* @ngInject */
    function CatPartsFormController($scope, $rootScope, $timeout, Constant, kendoConfig, WindowService, 
    		CatPartsService, $kWindow, CommonService, Restangular, $localStorage, $compile, RestEndpoint) {    	
        var vm = this;
	
			var message = {
			noDataGrid: CommonService.translate("Kh�ng c� d? li?u tr�n trang hi?n t?i"),
			lineRequired: CommonService.translate("B?n c?n ch?n m?t d�ng tr??c"),
			recordRequired: CommonService.translate("B?n c?n ch?n m?t d�ng tr??c"),
			needShowDetail : CommonService.translate("C?n hi?n th? ? ch? ?? Chi Ti?t!"),
			positionLast: CommonService.translate("?� ? b?n ghi cu?i"),
			positionFirst: CommonService.translate("?� ? b?n ghi ??u"),
			duplicateValue: CommonService.translate("M� danh m?c ?� t?n t?i"),
			createError: CommonService.translate("L?i khi t?o m?i b?n ghi"),
			createSuccess: CommonService.translate("T?o m?i th�nh c�ng"),
			updateError: CommonService.translate("L?i khi c?p nh?t b?n ghi"),
			updateSuccess: CommonService.translate("C?p nh?t th�nh c�ng"),
			deleteConfirm: CommonService.translate("B?n th?c s? mu?n x�a b?n ghi ?"),
			deleteError: CommonService.translate("X�a kh�ng th�nh c�ng"),
			deleteSuccess: CommonService.translate("X�a th�nh c�ng"),
			usedRecord: CommonService.translate("B?n ghi ?� ???c s? d?ng"),
			getDataError: CommonService.translate("L?i x?y ra khi l?y d? li?u"),
			adOrgIdRequired: CommonService.translate("??n v? kh�ng ???c ?? tr?ng"),
			partnerNameRequired: CommonService.translate("partnerName kh�ng ???c ?? tr?ng"),			
			addressRequired: CommonService.translate("address kh�ng ???c ?? tr?ng"),			
			phoneRequired: CommonService.translate("phone kh�ng ???c ?? tr?ng"),			
			descriptionRequired: CommonService.translate("description kh�ng ???c ?? tr?ng"),			
			faxRequired: CommonService.translate("fax kh�ng ???c ?? tr?ng"),			
			taxCodeRequired: CommonService.translate("taxCode kh�ng ???c ?? tr?ng"),			
			codeRequired: CommonService.translate("code kh�ng ???c ?? tr?ng"),			
			refIdRequired: CommonService.translate("refId kh�ng ???c ?? tr?ng"),			
			typeRequired: CommonService.translate("type kh�ng ???c ?? tr?ng"),			
			abbNameRequired: CommonService.translate("abbName kh�ng ???c ?? tr?ng"),			
			groupIdRequired: CommonService.translate("groupId kh�ng ???c ?? tr?ng"),			
			delegatePersonRequired: CommonService.translate("delegatePerson kh�ng ???c ?? tr?ng"),			
			titleRequired: CommonService.translate("title kh�ng ???c ?? tr?ng"),			
			accountNumberRequired: CommonService.translate("accountNumber kh�ng ???c ?? tr?ng"),			
			bussinessNumberRequired: CommonService.translate("bussinessNumber kh�ng ???c ?? tr?ng"),			
			statusRequired: CommonService.translate("status kh�ng ???c ?? tr?ng"),			
			createdDateRequired: CommonService.translate("createdDate kh�ng ???c ?? tr?ng"),			
			proposalNoteRequired: CommonService.translate("proposalNote kh�ng ???c ?? tr?ng"),			
			acceptorIdRequired: CommonService.translate("acceptorId kh�ng ???c ?? tr?ng"),			
			acceptDateRequired: CommonService.translate("acceptDate kh�ng ???c ?? tr?ng"),			
			acceptNoteRequired: CommonService.translate("acceptNote kh�ng ???c ?? tr?ng"),			
			rejectNoteRequired: CommonService.translate("rejectNote kh�ng ???c ?? tr?ng"),			
			numDispathRequired: CommonService.translate("numDispath kh�ng ???c ?? tr?ng"),			
			numSubmitsionRequired: CommonService.translate("numSubmitsion kh�ng ???c ?? tr?ng"),			
			catPartnerTypeIdRequired: CommonService.translate("catPartnerTypeId kh�ng ???c ?? tr?ng"),			
			creatPartnerDateRequired: CommonService.translate("creatPartnerDate kh�ng ???c ?? tr?ng"),			
			reasonChangePartnerRequired: CommonService.translate("reasonChangePartner kh�ng ???c ?? tr?ng"),			
			delegatePositionRequired: CommonService.translate("delegatePosition kh�ng ???c ?? tr?ng"),			
			accountHolderRequired: CommonService.translate("accountHolder kh�ng ???c ?? tr?ng"),			
			identityCardRequired: CommonService.translate("identityCard kh�ng ???c ?? tr?ng"),			
			bankNameRequired: CommonService.translate("bankName kh�ng ???c ?? tr?ng"),			
			stationCodeExpectedRequired: CommonService.translate("stationCodeExpected kh�ng ???c ?? tr?ng"),			
			stationIdExpectedRequired: CommonService.translate("stationIdExpected kh�ng ???c ?? tr?ng"),			
			modifieldDateRequired: CommonService.translate("modifieldDate kh�ng ???c ?? tr?ng"),			
			catBankBranchIdRequired: CommonService.translate("catBankBranchId kh�ng ???c ?? tr?ng"),			
			isLockRequired: CommonService.translate("isLock kh�ng ???c ?? tr?ng"),			
			issueByRequired: CommonService.translate("issueBy kh�ng ???c ?? tr?ng"),			
			dateRangeRequired: CommonService.translate("dateRange kh�ng ???c ?? tr?ng"),			
			checkPartnerRequired: CommonService.translate("checkPartner kh�ng ???c ?? tr?ng"),			
			dateUpdatePartnersContractRequired: CommonService.translate("dateUpdatePartnersContract kh�ng ???c ?? tr?ng"),			
			representationNameRequired: CommonService.translate("representationName kh�ng ???c ?? tr?ng"),			
			representationPositionRequired: CommonService.translate("representationPosition kh�ng ???c ?? tr?ng"),			
			noteRequired: CommonService.translate("note kh�ng ???c ?? tr?ng"),			
			accountRequired: CommonService.translate("account kh�ng ???c ?? tr?ng"),			
			transactionNameRequired: CommonService.translate("transactionName kh�ng ???c ?? tr?ng"),			
			representativeNameRequired: CommonService.translate("representativeName kh�ng ???c ?? tr?ng"),			
			representativePositionRequired: CommonService.translate("representativePosition kh�ng ???c ?? tr?ng"),			
        }
        vm.data = {
			catPartsId: 0, 			
			partnerId: 0,
			
			
			partnerName: '',
			
			
			address: '',
			
			
			phone: '',
			
			
			description: '',
			
			
			
			
			fax: '',
			
			
			taxCode: '',
			
			
			code: '',
			
			
			refId: '',
			
			
			type: '',
			
			
			abbName: '',
			
			
			groupId: '',
			
			
			delegatePerson: '',
			
			
			title: '',
			
			
			accountNumber: '',
			
			
			bussinessNumber: '',
			
			
			status: '',
			
			
			
			
			createdDate: '',
			
			
			proposalNote: '',
			
			
			acceptorId: '',
			
			
			acceptDate: '',
			
			
			acceptNote: '',
			
			
			rejectNote: '',
			
			
			numDispath: '',
			
			
			numSubmitsion: '',
			
			
			catPartnerTypeId: '',
			
			
			creatPartnerDate: '',
			
			
			reasonChangePartner: '',
			
			
			delegatePosition: '',
			
			
			accountHolder: '',
			
			
			identityCard: '',
			
			
			bankName: '',
			
			
			stationCodeExpected: '',
			
			
			stationIdExpected: '',
			
			
			modifieldDate: '',
			
			
			catBankBranchId: '',
			
			
			isLock: '',
			
			
			issueBy: '',
			
			
			dateRange: '',
			
			
			checkPartner: '',
			
			
			dateUpdatePartnersContract: '',
			
			
			representationName: '',
			
			
			representationPosition: '',
			
			
			note: '',
			
			
			account: '',
			
			
			transactionName: '',
			
			
			representativeName: '',
			
			
			representativePosition: '',
			updatorGroupId: '',
			isActive: 1, 			
			creatorId:'',
			creatorGroupId:'',
			creatorDate:'',
			updatorId:'',
			updatorGroupId:'',
			updatorDate:'',
		};
		vm.data_temp = angular.copy(vm.data);
    	vm.criteriaSearch = {
			name: '', 
			value: '', 
			description: ''
		};
    
        vm.isCreateNew = false;        
        vm.save = save;        		                                   
                											      
        vm.validatorOptions = kendoConfig.get('validatorOptions');
		//fetchAll();
        $scope.$watch($rootScope.RedirectToCAT_PARTSFormEvent, function (newValue) {
        	if ($rootScope.RedirectToCAT_PARTSFormEvent != null) {
				if ($rootScope.RedirectToCAT_PARTSFormEvent.type == 'edit') {
					vm.data=$rootScope.RedirectToCAT_PARTSFormEvent.data;
					vm.isCreateNew=false;
				}else if($rootScope.RedirectToCAT_PARTSFormEvent.type == 'view'){					
					vm.data=$rootScope.RedirectToCAT_PARTSFormEvent.data;		
					vm.isCreateNew=false;
				}else if($rootScope.RedirectToCAT_PARTSFormEvent.type == 'add'){
					vm.isCreateNew=true;
				}
				$rootScope.RedirectToCAT_PARTSFormEvent = null;
			}
        });
		

        function save() {            
//			if(formValidate()){
				if(1==1){
				if(vm.isCreateNew) {//Trwowg
					CatPartsService.create(vm.data).then(function(result){
						vm.data_temp = angular.copy(vm.data);
						vm.isCreateNew = false;
							vm.data.partnerId = result;
						//vm.data.catPartsId = result;							
						
						toastr.success(CommonService.translate(message.createSuccess));
					}, function(errResponse){
						
						if (errResponse.status === 409) {
							toastr.error(CommonService.translate(message.duplicateValue));
						} else {
							toastr.error(CommonService.translate(message.createError));
						}
						return;
					});
					//End tr??ng h?p th�m m?i
				} else {  // Tr??ng h?p update              		
					CatPartsService.update(vm.data).then(function(){
							vm.data.updatorId = CommonService.getUserInfo().userId;
							vm.data.updatorGroupId = CommonService.getUserInfo().groupId
							vm.data_temp = angular.copy(vm.data);								
							
							toastr.success(CommonService.translate(message.updateSuccess));
						}, function(errResponse){
							
							if (errResponse.status === 409) {
								toastr.error(CommonService.translate(message.duplicateValue));
							} else {
								toastr.error(CommonService.translate(message.updateError));
							}
							return;
					});
					//End tr??ng h?p update
				}
			}
            

        }	

		function formValidate() {
        	
			 if (vm.data.partnerName == null || vm.data.partnerName == undefined || vm.data.partnerName == '') {
        		toastr.warning(CommonService.translate(message.partnerNameRequired));
        		return false;
        	}				
			 if (vm.data.address == null || vm.data.address == undefined || vm.data.address == '') {
        		toastr.warning(CommonService.translate(message.addressRequired));
        		return false;
        	}				
			 if (vm.data.phone == null || vm.data.phone == undefined || vm.data.phone == '') {
        		toastr.warning(CommonService.translate(message.phoneRequired));
        		return false;
        	}				
			 if (vm.data.description == null || vm.data.description == undefined || vm.data.description == '') {
        		toastr.warning(CommonService.translate(message.descriptionRequired));
        		return false;
        	}				
			 if (vm.data.fax == null || vm.data.fax == undefined || vm.data.fax == '') {
        		toastr.warning(CommonService.translate(message.faxRequired));
        		return false;
        	}				
			 if (vm.data.taxCode == null || vm.data.taxCode == undefined || vm.data.taxCode == '') {
        		toastr.warning(CommonService.translate(message.taxCodeRequired));
        		return false;
        	}				
			 if (vm.data.code == null || vm.data.code == undefined || vm.data.code == '') {
        		toastr.warning(CommonService.translate(message.codeRequired));
        		return false;
        	}				
			 if (vm.data.refId == null || vm.data.refId == undefined || vm.data.refId == '') {
        		toastr.warning(CommonService.translate(message.refIdRequired));
        		return false;
        	}				
			 if (vm.data.type == null || vm.data.type == undefined || vm.data.type == '') {
        		toastr.warning(CommonService.translate(message.typeRequired));
        		return false;
        	}				
			 if (vm.data.abbName == null || vm.data.abbName == undefined || vm.data.abbName == '') {
        		toastr.warning(CommonService.translate(message.abbNameRequired));
        		return false;
        	}				
			 if (vm.data.groupId == null || vm.data.groupId == undefined || vm.data.groupId == '') {
        		toastr.warning(CommonService.translate(message.groupIdRequired));
        		return false;
        	}				
			 if (vm.data.delegatePerson == null || vm.data.delegatePerson == undefined || vm.data.delegatePerson == '') {
        		toastr.warning(CommonService.translate(message.delegatePersonRequired));
        		return false;
        	}				
			 if (vm.data.title == null || vm.data.title == undefined || vm.data.title == '') {
        		toastr.warning(CommonService.translate(message.titleRequired));
        		return false;
        	}				
			 if (vm.data.accountNumber == null || vm.data.accountNumber == undefined || vm.data.accountNumber == '') {
        		toastr.warning(CommonService.translate(message.accountNumberRequired));
        		return false;
        	}				
			 if (vm.data.bussinessNumber == null || vm.data.bussinessNumber == undefined || vm.data.bussinessNumber == '') {
        		toastr.warning(CommonService.translate(message.bussinessNumberRequired));
        		return false;
        	}				
			 if (vm.data.status == null || vm.data.status == undefined || vm.data.status == '') {
        		toastr.warning(CommonService.translate(message.statusRequired));
        		return false;
        	}				
			 if (vm.data.createdDate == null || vm.data.createdDate == undefined || vm.data.createdDate == '') {
        		toastr.warning(CommonService.translate(message.createdDateRequired));
        		return false;
        	}				
			 if (vm.data.proposalNote == null || vm.data.proposalNote == undefined || vm.data.proposalNote == '') {
        		toastr.warning(CommonService.translate(message.proposalNoteRequired));
        		return false;
        	}				
			 if (vm.data.acceptorId == null || vm.data.acceptorId == undefined || vm.data.acceptorId == '') {
        		toastr.warning(CommonService.translate(message.acceptorIdRequired));
        		return false;
        	}				
			 if (vm.data.acceptDate == null || vm.data.acceptDate == undefined || vm.data.acceptDate == '') {
        		toastr.warning(CommonService.translate(message.acceptDateRequired));
        		return false;
        	}				
			 if (vm.data.acceptNote == null || vm.data.acceptNote == undefined || vm.data.acceptNote == '') {
        		toastr.warning(CommonService.translate(message.acceptNoteRequired));
        		return false;
        	}				
			 if (vm.data.rejectNote == null || vm.data.rejectNote == undefined || vm.data.rejectNote == '') {
        		toastr.warning(CommonService.translate(message.rejectNoteRequired));
        		return false;
        	}				
			 if (vm.data.numDispath == null || vm.data.numDispath == undefined || vm.data.numDispath == '') {
        		toastr.warning(CommonService.translate(message.numDispathRequired));
        		return false;
        	}				
			 if (vm.data.numSubmitsion == null || vm.data.numSubmitsion == undefined || vm.data.numSubmitsion == '') {
        		toastr.warning(CommonService.translate(message.numSubmitsionRequired));
        		return false;
        	}				
			 if (vm.data.catPartnerTypeId == null || vm.data.catPartnerTypeId == undefined || vm.data.catPartnerTypeId == '') {
        		toastr.warning(CommonService.translate(message.catPartnerTypeIdRequired));
        		return false;
        	}				
			 if (vm.data.creatPartnerDate == null || vm.data.creatPartnerDate == undefined || vm.data.creatPartnerDate == '') {
        		toastr.warning(CommonService.translate(message.creatPartnerDateRequired));
        		return false;
        	}				
			 if (vm.data.reasonChangePartner == null || vm.data.reasonChangePartner == undefined || vm.data.reasonChangePartner == '') {
        		toastr.warning(CommonService.translate(message.reasonChangePartnerRequired));
        		return false;
        	}				
			 if (vm.data.delegatePosition == null || vm.data.delegatePosition == undefined || vm.data.delegatePosition == '') {
        		toastr.warning(CommonService.translate(message.delegatePositionRequired));
        		return false;
        	}				
			 if (vm.data.accountHolder == null || vm.data.accountHolder == undefined || vm.data.accountHolder == '') {
        		toastr.warning(CommonService.translate(message.accountHolderRequired));
        		return false;
        	}				
			 if (vm.data.identityCard == null || vm.data.identityCard == undefined || vm.data.identityCard == '') {
        		toastr.warning(CommonService.translate(message.identityCardRequired));
        		return false;
        	}				
			 if (vm.data.bankName == null || vm.data.bankName == undefined || vm.data.bankName == '') {
        		toastr.warning(CommonService.translate(message.bankNameRequired));
        		return false;
        	}				
			 if (vm.data.stationCodeExpected == null || vm.data.stationCodeExpected == undefined || vm.data.stationCodeExpected == '') {
        		toastr.warning(CommonService.translate(message.stationCodeExpectedRequired));
        		return false;
        	}				
			 if (vm.data.stationIdExpected == null || vm.data.stationIdExpected == undefined || vm.data.stationIdExpected == '') {
        		toastr.warning(CommonService.translate(message.stationIdExpectedRequired));
        		return false;
        	}				
			 if (vm.data.modifieldDate == null || vm.data.modifieldDate == undefined || vm.data.modifieldDate == '') {
        		toastr.warning(CommonService.translate(message.modifieldDateRequired));
        		return false;
        	}				
			 if (vm.data.catBankBranchId == null || vm.data.catBankBranchId == undefined || vm.data.catBankBranchId == '') {
        		toastr.warning(CommonService.translate(message.catBankBranchIdRequired));
        		return false;
        	}				
			 if (vm.data.isLock == null || vm.data.isLock == undefined || vm.data.isLock == '') {
        		toastr.warning(CommonService.translate(message.isLockRequired));
        		return false;
        	}				
			 if (vm.data.issueBy == null || vm.data.issueBy == undefined || vm.data.issueBy == '') {
        		toastr.warning(CommonService.translate(message.issueByRequired));
        		return false;
        	}				
			 if (vm.data.dateRange == null || vm.data.dateRange == undefined || vm.data.dateRange == '') {
        		toastr.warning(CommonService.translate(message.dateRangeRequired));
        		return false;
        	}				
			 if (vm.data.checkPartner == null || vm.data.checkPartner == undefined || vm.data.checkPartner == '') {
        		toastr.warning(CommonService.translate(message.checkPartnerRequired));
        		return false;
        	}				
			 if (vm.data.dateUpdatePartnersContract == null || vm.data.dateUpdatePartnersContract == undefined || vm.data.dateUpdatePartnersContract == '') {
        		toastr.warning(CommonService.translate(message.dateUpdatePartnersContractRequired));
        		return false;
        	}				
			 if (vm.data.representationName == null || vm.data.representationName == undefined || vm.data.representationName == '') {
        		toastr.warning(CommonService.translate(message.representationNameRequired));
        		return false;
        	}				
			 if (vm.data.representationPosition == null || vm.data.representationPosition == undefined || vm.data.representationPosition == '') {
        		toastr.warning(CommonService.translate(message.representationPositionRequired));
        		return false;
        	}				
			 if (vm.data.note == null || vm.data.note == undefined || vm.data.note == '') {
        		toastr.warning(CommonService.translate(message.noteRequired));
        		return false;
        	}				
			 if (vm.data.account == null || vm.data.account == undefined || vm.data.account == '') {
        		toastr.warning(CommonService.translate(message.accountRequired));
        		return false;
        	}				
			 if (vm.data.transactionName == null || vm.data.transactionName == undefined || vm.data.transactionName == '') {
        		toastr.warning(CommonService.translate(message.transactionNameRequired));
        		return false;
        	}				
			 if (vm.data.representativeName == null || vm.data.representativeName == undefined || vm.data.representativeName == '') {
        		toastr.warning(CommonService.translate(message.representativeNameRequired));
        		return false;
        	}				
			 if (vm.data.representativePosition == null || vm.data.representativePosition == undefined || vm.data.representativePosition == '') {
        		toastr.warning(CommonService.translate(message.representativePositionRequired));
        		return false;
        	}				
			 if (vm.data.updatorGroupId == null || vm.data.updatorGroupId == undefined || vm.data.updatorGroupId == '') {
        		toastr.warning(CommonService.translate(message.updatorGroupIdRequired));
        		return false;
        	}				
        	return true;
        }
		
		function convertDate(dateString) {
			var parts = dateString.split("/");
		    return new Date(parts[2], parts[1] - 1, parts[0]);
		}
		
		function dateValidation(e) {
			var dateformat = /^(0[1-9]|[12][0-9]|3[01])[\- \/.](?:(0[1-9]|1[012])[\- \/.](201)[2-9]{1})$/;
		    if (e.match(dateformat)) {
		        var opera1 = e.split('/');
		        var lopera1 = opera1.length;
		        if (lopera1 > 1) {
		            var pdate = e.split('/');
		        }
		        var mm = parseInt(pdate[1]);
		        var dd = parseInt(pdate[0]);
		        var yy = parseInt(pdate[2]);
		        var ListofDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
		        if (mm == 1 || mm > 2) {
		            if (dd > ListofDays[mm - 1]) {
		                return true;
		            }
		        }
		        if (mm == 2) {
		            var lyear = false;
		            if ((!(yy % 4) && yy % 100) || !(yy % 400)) {
		                lyear = true;
		            }
		            if ((lyear == false) && (dd >= 29)) {
		                return true;
		            }
		            if ((lyear == true) && (dd > 29)) {
		                return true;
		            }
		        }
		    } else {
		        return true;
		    }
		    return false;
        }
		
    }		
				                		            															
					   
	 
})();