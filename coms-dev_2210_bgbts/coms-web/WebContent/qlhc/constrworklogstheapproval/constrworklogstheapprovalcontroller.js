(function() {'use strict';
	var controllerId = 'constrworklogstheapprovalcontroller';

	angular.module('MetronicApp').controller(controllerId, listController);

	/* @ngInject */
	function listController($scope, $rootScope, kendoConfig, gettextCatalog,
			$kWindow, $q, constrWorkLogsTheApproval, ProposalEvaluation,
			Constant, theApproval, createMemoryWork) {
		var vm = this;
		var statusBia = false;
		$rootScope.isDisabled = false;
		vm.disableAll = false;
		var creatOrUpdate;
		vm.role=[];
		vm.approDTO = {
			statusCa : '',
			employeeId : '',
			comments : '',
			constrWoLogsLabId : '',
			constrCompReMapId : ''
		};
		vm.approDTO.statusCa = 2;
		vm.validatorOptions = kendoConfig.get('validatorOptions');
		
   
		
		//3/3/2017 dodt edit: xu li bat dong bo 
		function getCreateMemoryWork(){
			var deferred = $q.defer();
			$rootScope.isAppro = createMemoryWork.getIsAppro();
			vm.aMonitorIdList = createMemoryWork.getaMonitorIdList();
			vm.bConstructIdList = createMemoryWork.getbConstructIdList();
			vm.aDirectorIdList = createMemoryWork.getaDirectorIdList();
			vm.bDirectorIdList = createMemoryWork.getbDirectorIdList();
			
            deferred.resolve('done');
            return deferred.promise;
		};
		
		
		getCreateMemoryWork().then(function(result) {

			vm.constrWorkLogsLabelDTO = createMemoryWork.getConstrWorkLogsLabelDTO();
			
			if(vm.constrWorkLogsLabelDTO != undefined){
				if(vm.constrWorkLogsLabelDTO.statusCa==1||vm.constrWorkLogsLabelDTO.statusCa==2){
					vm.disableAll = true;
				}else{
					vm.disableAll = false;
				}
				
				if(vm.constrWorkLogsLabelDTO.createdUserId != Constant.user.srvUser.userId){
					vm.disableAll = true;
				}else{
					vm.disableAll = false;
				}
			}
			if (vm.constrWorkLogsLabelDTO == undefined) {
				

				
				if(vm.aMonitorIdList.length != 0 && vm.bConstructIdList.length != 0 && vm.aDirectorIdList.length != 0  && vm.bDirectorIdList.length != 0){
					vm.constrWorkLogsLabelDTO = {};
					creatOrUpdate = "creat";
					vm.constrWorkLogsLabelDTO.amonitorId = vm.aMonitorIdList[0].id;
					vm.constrWorkLogsLabelDTO.bconstructId = vm.bConstructIdList[0].id;
					vm.constrWorkLogsLabelDTO.adirectorId = vm.aDirectorIdList[0].id;
					vm.constrWorkLogsLabelDTO.bdirectorId = vm.bDirectorIdList[0].id;
				}else{
					toastr.warning("Thi???u d??? li???u ?????u v??o");
				}
				
			}
        });
		
		//end 3/3/2017
		
		// Common
		vm.item = {
			partnerName : '',
			contractCode : '',
			investProjectName : '',
			constrtCode : '',
			constrtName : '',
			constrType : '',
			provinceId : '',
			constrtAddress : '',
			constructId : '',
			signed_date : '',
			provinceName : '',
			supervisorName : '',
			constructorName : ''
		}
		vm.item = ProposalEvaluation.getItem();
		if(vm.item == null) {
			vm.item = CommonService.getItem();
		}
		$scope.$on("ProposalEvaluation.detail", function(event, item) {
			if (item != undefined) {
				vm.item = item;
			} else {
				console.error("kh??ng nh???n ???????c d??? li???u!");
			}
		});

		vm.theApproval = {
			code : '',
			constructId : '',
			constrCompReMapId : '',
			stringEmployee : '',
			isSign : '',
			path : '',
			nameFile : '',
			roleId : [],
			roleName : []
		};
		constrWorkLogsTheApproval.getRoleId().then(function(data) {
			vm.role = data;
		})
		.catch(function(data, status) {
			console.error('getRoleId error', response.status, response.data);
		});
		vm.signCACreat = signCACreat;
		function signCACreat() {
			if (vm.constrWorkLogsLabelDTO == undefined || vm.constrWorkLogsLabelDTO.statusCa == undefined) {
				toastr.warning("B??a ch??a ???????c t???o");
				return;
			}
			if (vm.constrWorkLogsLabelDTO.statusCa != 0) {
				toastr.warning("B??a ???? tr??nh duy???t");
				return;
			}
			$('#loading').show();
			constrWorkLogsTheApproval
					.exportFilePdf(vm.constrWorkLogsLabelDTO)
					.then(
							function(data) {
								vm.theApproval.constrCompReMapId = vm.constrWorkLogsLabelDTO.constrCompReMapId;
								vm.theApproval.path = data.fileName;
								vm.theApproval.nameFile = 'BM-TCNT-04-BIA.pdf';
								vm.theApproval.constructId = vm.item.constructId;
								vm.theApproval.stringEmployee = vm.constrWorkLogsLabelDTO.bconstructId
										+ ","
										+vm.constrWorkLogsLabelDTO.amonitorId
										+ ","
										+ vm.constrWorkLogsLabelDTO.bdirectorId
										+ ","
										+ vm.constrWorkLogsLabelDTO.adirectorId;										
								vm.theApproval.isSign = 'theApproval';
								vm.theApproval.roleId = [Constant.ROLE_ID["employee_roleID_DT_KTTC"], Constant.ROLE_ID["employee_roleID_CDT_GSTC"], Constant.ROLE_ID["employee_roleID_DT_GDNT"], Constant.ROLE_ID["employee_roleID_CDT_DDPN"]];
								vm.theApproval.roleName = [
								        "K??? thu???t thi c??ng",										
										"Gi??m s??t thi c??ng",
										"Gi??m ?????c nh?? th???u thi c??ng",
										"Th??? tr?????ng ch??? ?????u t??"
										 ];
								vm.theApproval.code = 'sdfsdfsdf';
								theApproval.setItem(vm.theApproval);
								goTo('THE_APPROVAL');
							}).catch( function(){
				        		toastr.error(gettextCatalog.getString("L???i khi export!"));
				        		return;
				        	}).finally(function(){
				        		$('#loading').hide();
				        	});
		}

		vm.ChangeData = ChangeData;
		function ChangeData() {
			if (vm.constrWorkLogsLabelDTO.constrCompReMapId != null
					|| vm.constrWorkLogsLabelDTO.constrCompReMapId != undefined) {
				creatOrUpdate = "update";
			}
		}
		
		vm.save = save;
		function save() {
			if (!vm.validator.validate()){
				return;
			}
			if ($rootScope.isAppro) {
				vm.approDTO.employeeId = Constant.getUser().srvUser.catEmployeeId;
				vm.approDTO.constrWoLogsLabId = vm.constrWorkLogsLabelDTO.constrWoLogsLabId;
				vm.approDTO.constrCompReMapId = vm.constrWorkLogsLabelDTO.constrCompReMapId;
				constrWorkLogsTheApproval.appro(vm.approDTO).then(function(d) {
					var x = d;
					if (x == '1') {
						toastr.warning("Ch??a ?????n l?????t duy???t");
						return;
					}
					if (x == '2') {
						toastr.warning("???? duy???t r???i");
						return;
					}
					if (x == '4') {
						toastr.success("T??? ch???i duy???t th??nh c??ng");
					}
					if (x == '3') {
						toastr.success("Duy???t th??nh c??ng");
					}
					if (x == '5') {
						toastr.warning("B???n kh??ng ???????c ph??p ph?? duy???t");
					}
					statusBia = true;
					$rootScope.$broadcast("ChangeBia", statusBia);
					statusBia = false;
				}, function() {
					toastr.error("C?? s??? c??? x???y ra!");
				});
				return;
			} else if (creatOrUpdate == "creat") {
				vm.constrWorkLogsLabelDTO.constructId = vm.item.constructId;
				vm.constrWorkLogsLabelDTO.createdUserId = Constant.getUser().srvUser.userId;
				constrWorkLogsTheApproval.creat(vm.constrWorkLogsLabelDTO)
						.then(function(d) {
							if (d == -1) {
								toastr.warning("B??a ???? ???????c t???o!");
								return;
							}
							vm.theApproval.constrCompReMapId = d;
							vm.constrWorkLogsLabelDTO.constrCompReMapId = d;
							vm.constrWorkLogsLabelDTO.statusCa = 0;
							statusBia = true;
							toastr.success("T???o b??a th??nh c??ng");
							$rootScope.$broadcast("ChangeBia", statusBia);
							statusBia = false;
						}, function(errResponse) {
							toastr.error("C?? s??? c??? x???y ra!");
						});
			} else if (creatOrUpdate == "update") {
				if (vm.constrWorkLogsLabelDTO.statusCa != 0) {
					toastr
							.warning("Ch??? ???????c s???a khi ??ang ??? tr???ng th??i So???n Th???o");
					return;
				}
				constrWorkLogsTheApproval
						.updateLabel(vm.constrWorkLogsLabelDTO)
						.then(function(d) {
							if (d == -1) {
								toastr.warning("Kh??ng ???????c ph??p s???a B??a");
								return;
							}
							statusBia = true;
							toastr.success("Update th??nh c??ng");
							vm.constrWorkLogsLabelDTO.statusCa = 0;
							$rootScope.$broadcast("ChangeBia", statusBia);
							statusBia = false;
						}, function() {
							toastr.error("C?? s??? c??? x???y ra!");
						});

			}

		}

		vm.exportFilePdf = exportFilePdf;
		function exportFilePdf() {
			if (vm.constrWorkLogsLabelDTO ==  undefined) {
			toastr.warning("B??a ch??a ???????c t???o");
			return;
		}
			$('#loading').show();
			vm.constrWorkLogsLabelDTO.constructId = vm.item.constructId;
			vm.constrWorkLogsLabelDTO.contractCode=vm.item.contractCode;
			constrWorkLogsTheApproval
					.exportFilePdf(vm.constrWorkLogsLabelDTO)
					.then(
							function(data) {
								window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?"
										+ data.fileName;
							}).catch( function(){
				        		toastr.error(gettextCatalog.getString("L???i khi export!"));
				        		return;
				        	}).finally(function(){
				        		$('#loading').hide();
				        	});

		}

		vm.ExportDoc = exportDoc;
		function exportDoc() {
			if (vm.constrWorkLogsLabelDTO == undefined) {
			toastr.warning("B??a ch??a ???????c t???o");
			return;
		}
			$('#loading').show();
			vm.constrWorkLogsLabelDTO.constructId = vm.item.constructId;
			vm.constrWorkLogsLabelDTO.contractCode=vm.item.contractCode;
			constrWorkLogsTheApproval
					.exportFileDoc(vm.constrWorkLogsLabelDTO)
					.then(
							function(data) {
								window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?"
										+ data.fileName;
							}).catch( function(){
				        		toastr.error(gettextCatalog.getString("L???i khi export!"));
				        		return;
				        	}).finally(function(){
				        		$('#loading').hide();
				        	});

		}

		vm.ApproBia = approBia;
		function approBia() {
			if (vm.constrWorkLogsLabelDTO == undefined) {
				toastr.warning("B??a ch??a ???????c t???o");
				return;
			}
			if(vm.constrWorkLogsLabelDTO.statusCa != 1) {
				toastr.warning("Ch??? tr???ng th??i tr??nh duy???t m???i ???????c ph?? duy???t");
				return;
			}
			$rootScope.isDisabled = true;
			$rootScope.isAppro = true;
		}
		//ngoccx
	      //huy trinh duyet
			vm.cancelApprovalBtn= function(){
				vm.constrWorkLogsLabelDTO.tableName = 'CONSTR_WORK_LOGS_LABEL';
				vm.constrWorkLogsLabelDTO.tableId = vm.constrWorkLogsLabelDTO.constrWoLogsLabId;
				vm.constrWorkLogsLabelDTO.tableIdField = 'CONSTR_WO_LOGS_LAB_ID';
				if(vm.constrWorkLogsLabelDTO.statusCa == 1){
					if(vm.constrWorkLogsLabelDTO.createdUserId != Constant.user.srvUser.userId){
						toastr.warning(gettextCatalog.getString("Ng?????i t???o m???i c?? quy???n h???y tr??nh duy???t!"));
						return;
					}else{
						if(confirm('X??c nh???n h???y tr??nh duy???t')){
							createMemoryWork.cancelAprroval(vm.constrWorkLogsLabelDTO).then(function() {
								status = true;
								$rootScope.$broadcast("ChangeStatusHuyDuyet", status);
								statusBia = true;
								$rootScope.$broadcast("ChangeBia", statusBia);
								statusBia = false;
					toastr.success(gettextCatalog.getString("???? h???y tr??nh duy???t !"));
				}, function(){
				toastr.error(gettextCatalog.getString("L???i khi h???y tr??nh duy???t!"));
				return;
			});}
						}
				}else{
					toastr.warning("Ch??? tr??nh duy???t m???i ???????c h???y tr??nh duy???t");
				}
			}
		function goTo(menuKey) {
			var hasPerm = true;

			if (hasPerm) {
				var template = Constant.getTemplateUrl(menuKey);

				postal.publish({
					channel : "Tab",
					topic : "open",
					data : template
				});
			} else {
				// toastr.error(gettextCatalog.getString("T??i kho???n ????ng
				// nh???p hi???n t???i kh??ng ???????c ph??p truy c???p v??o ch???c n??ng
				// n??y!"));
			}
		}

	}
})();