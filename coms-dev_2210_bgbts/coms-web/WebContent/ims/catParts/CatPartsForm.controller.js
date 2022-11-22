(function () {
    'use strict';

    var controllerId = 'CatPartsFormController';

    angular.module('MetronicApp').controller(controllerId, CatPartsFormController);
    
    /* @ngInject */
    function CatPartsFormController($scope, $rootScope, $timeout, Constant, kendoConfig, WindowService, 
    		CatPartsService, $kWindow, CommonService, Restangular, $localStorage, $compile, RestEndpoint) {    	
        var vm = this;
	
			var message = {
			noDataGrid: CommonService.translate("Không có dữ liệu trên trang hiện tại"),
			lineRequired: CommonService.translate("Bạn cần chọn một dòng trước"),
			recordRequired: CommonService.translate("Bạn cần chọn một dòng trước"),
			needShowDetail : CommonService.translate("Cần hiển thị ở chế độ Chi Tiết!"),
			positionLast: CommonService.translate("Đã ở bản ghi cuối"),
			positionFirst: CommonService.translate("Đã ở bản ghi đầu"),
			duplicateValue: CommonService.translate("Mã danh mục đã tồn tại"),
			createError: CommonService.translate("Lỗi khi tạo mới bản ghi"),
			createSuccess: CommonService.translate("Tạo mới thành công"),
			updateError: CommonService.translate("Lỗi khi cập nhật bản ghi"),
			updateSuccess: CommonService.translate("Cập nhật thành công"),
			deleteConfirm: CommonService.translate("Bạn thực sự muốn xóa bản ghi ?"),
			deleteError: CommonService.translate("Xóa không thành công"),
			deleteSuccess: CommonService.translate("Xóa thành công"),
			usedRecord: CommonService.translate("Bản ghi đã được sử dụng"),
			getDataError: CommonService.translate("Lỗi xảy ra khi lấy dữ liệu"),
			adOrgIdRequired: CommonService.translate("Đơn vị không được để trống"),
			partnerNameRequired: CommonService.translate("partnerName không được để trống"),			
			addressRequired: CommonService.translate("address không được để trống"),			
			phoneRequired: CommonService.translate("phone không được để trống"),			
			descriptionRequired: CommonService.translate("description không được để trống"),			
			faxRequired: CommonService.translate("fax không được để trống"),			
			taxCodeRequired: CommonService.translate("taxCode không được để trống"),			
			codeRequired: CommonService.translate("code không được để trống"),			
			refIdRequired: CommonService.translate("refId không được để trống"),			
			typeRequired: CommonService.translate("type không được để trống"),			
			abbNameRequired: CommonService.translate("abbName không được để trống"),			
			groupIdRequired: CommonService.translate("groupId không được để trống"),			
			delegatePersonRequired: CommonService.translate("delegatePerson không được để trống"),			
			titleRequired: CommonService.translate("title không được để trống"),			
			accountNumberRequired: CommonService.translate("accountNumber không được để trống"),			
			bussinessNumberRequired: CommonService.translate("bussinessNumber không được để trống"),			
			statusRequired: CommonService.translate("status không được để trống"),			
			createdDateRequired: CommonService.translate("createdDate không được để trống"),			
			proposalNoteRequired: CommonService.translate("proposalNote không được để trống"),			
			acceptorIdRequired: CommonService.translate("acceptorId không được để trống"),			
			acceptDateRequired: CommonService.translate("acceptDate không được để trống"),			
			acceptNoteRequired: CommonService.translate("acceptNote không được để trống"),			
			rejectNoteRequired: CommonService.translate("rejectNote không được để trống"),			
			numDispathRequired: CommonService.translate("numDispath không được để trống"),			
			numSubmitsionRequired: CommonService.translate("numSubmitsion không được để trống"),			
			catPartnerTypeIdRequired: CommonService.translate("catPartnerTypeId không được để trống"),			
			creatPartnerDateRequired: CommonService.translate("creatPartnerDate không được để trống"),			
			reasonChangePartnerRequired: CommonService.translate("reasonChangePartner không được để trống"),			
			delegatePositionRequired: CommonService.translate("delegatePosition không được để trống"),			
			accountHolderRequired: CommonService.translate("accountHolder không được để trống"),			
			identityCardRequired: CommonService.translate("identityCard không được để trống"),			
			bankNameRequired: CommonService.translate("bankName không được để trống"),			
			stationCodeExpectedRequired: CommonService.translate("stationCodeExpected không được để trống"),			
			stationIdExpectedRequired: CommonService.translate("stationIdExpected không được để trống"),			
			modifieldDateRequired: CommonService.translate("modifieldDate không được để trống"),			
			catBankBranchIdRequired: CommonService.translate("catBankBranchId không được để trống"),			
			isLockRequired: CommonService.translate("isLock không được để trống"),			
			issueByRequired: CommonService.translate("issueBy không được để trống"),			
			dateRangeRequired: CommonService.translate("dateRange không được để trống"),			
			checkPartnerRequired: CommonService.translate("checkPartner không được để trống"),			
			dateUpdatePartnersContractRequired: CommonService.translate("dateUpdatePartnersContract không được để trống"),			
			representationNameRequired: CommonService.translate("representationName không được để trống"),			
			representationPositionRequired: CommonService.translate("representationPosition không được để trống"),			
			noteRequired: CommonService.translate("note không được để trống"),			
			accountRequired: CommonService.translate("account không được để trống"),			
			transactionNameRequired: CommonService.translate("transactionName không được để trống"),			
			representativeNameRequired: CommonService.translate("representativeName không được để trống"),			
			representativePositionRequired: CommonService.translate("representativePosition không được để trống"),			
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
		
		
		//check redirect from where
        $scope.$watch($rootScope.RedirectTocatPartsFormEvent, function (newValue) {
        	if ($rootScope.RedirectToCatPartsFormEvent != null) {
				if ($rootScope.RedirectToCatPartsFormEvent.type == 'edit') {
					vm.data=$rootScope.RedirectToCatPartsFormEvent.data;
					vm.isCreateNew=false;
					vm.readonly=false;
				}else if($rootScope.RedirectToCatPartsFormEvent.type == 'view'){					
					vm.data=$rootScope.RedirectToCatPartsFormEvent.data;											
					vm.isCreateNew=false;
					vm.readonly=true;
				}else if($rootScope.RedirectToCatPartsFormEvent.type == 'add'){																			
					vm.isCreateNew=true;
					vm.readonly=false;
				}
				$rootScope.RedirectToCatPartsFormEvent = null;
			}
        });
        
		
        function save() {            
			if(formValidate()){				
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
					//End trường hợp thêm mới
				} else {  // Trường hợp update              		
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
					//End trường hợp update
				}
			}
            

        }	

		function formValidate() {
        	
			 if (vm.data.partnerName == null || vm.data.partnerName == undefined || vm.data.partnerName == '') {
        		toastr.warning(CommonService.translate(message.partnerNameRequired));
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