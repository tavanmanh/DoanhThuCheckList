(function() {'use strict';var controllerId = 'distanceUnloadedMaterialsList';
	angular.module('MetronicApp').controller(controllerId,distanceUnloadedMaterialsList);
	function distanceUnloadedMaterialsList($scope,$rootScope,$timeout,Constant,kendoConfig,gettextCatalog,$kWindow,CommonService,theApproval,PopupConst,inspectionService,Restangular,RestEndpoint,ProposalEvaluation,distanceUnloadedMaterialsList)
	{
		var vm = this;			// Khai bao them cac bien
		vm.goTo = goTo;			
		vm.validatorOptions = kendoConfig.get('validatorOptions');
		vm.criteria = {};
		vm.chkSelectAll = chkSelectAll;
		vm.showAdd = true;
		vm.role = [];
		vm.showDeleteAll = true;
		vm.showExportPDF = true;
		vm.showExportDOC = true;
		vm.showSignCA = true;
		vm.showAppro = true;
		vm.showSave = false;
		vm.showDetailList = true;
		vm.showDetailForm = false;
		vm.showApproval = false;
		vm.disabledNoi = false;
		vm.disableAll = false;
		var checkonchange  = 0;
		var checkESTMA = 0;
		var getMultiDeleteList=[];
		vm.item = {};
		vm.item = ProposalEvaluation.getItem();
		if(vm.item == null) {
			vm.item = CommonService.getItem();
		}
		// khai bao du lieu truyen sang phe duyet
		vm.appro = {statusCa: '',employeeId:'',comments:'',disUnloadConsMinId:'',constrCompReMapId:''};
		// khai bao du lieu trinh duyet
		vm.theApproval = {
				code:'',constructId:'',constrCompReMapId: '',stringEmployee:'',isSign:'',
				path:'',nameFile:'',roleId: [],roleName: []}
		// function khong duoc sua trong grid
		function nonEditor(container, options) {
			container.text(options.model[options.field]);
		}
		
		// ////////////////////////Combobox thanh phan tham gia//////////////////////////////////
		vm.role=[];
		var totalListEmployee = [], Director = [], InChargeMonitor = [], BBDirector = [], BBInChargeConstruct = [];
		vm.aDirector=[];
		vm.aInChargeMonitor=[];
		vm.bDirector=[];
		vm.bInChargeConstruct=[];
		angular.element(document).ready(function () {
		distanceUnloadedMaterialsList.getListEmployeeByConstruction(vm.item.constructId).then(function(data) {
			totalListEmployee = data.plain();
			distanceUnloadedMaterialsList.getRoleId().then(function(data) {
				//vm.role = data;
				for (var i = 0; i < totalListEmployee.length; i++) {
					if(totalListEmployee[i].roleid == Constant.ROLE_ID["employee_roleID_CDT_DDPN"]){//2
						Director.push(totalListEmployee[i]);
					}
					if(totalListEmployee[i].roleid == Constant.ROLE_ID["employee_roleID_CDT_PTGST"]){//4
						InChargeMonitor.push(totalListEmployee[i]);
					}
					if(totalListEmployee[i].roleid == Constant.ROLE_ID["employee_roleID_DT_GDNT"]){//3
						BBDirector.push(totalListEmployee[i]);
					}
					if(totalListEmployee[i].roleid == Constant.ROLE_ID["employee_roleID_DT_PTTC"]){//1
						BBInChargeConstruct.push(totalListEmployee[i]);
					}
				}
				vm.aDirector= Director;
				vm.aInChargeMonitor= InChargeMonitor;
				vm.bDirector= BBDirector;
				vm.bInChargeConstruct= BBInChargeConstruct;
				
				if(Director.length > 0) {
					vm.criteria.adirectorId = Director[0].userId;
				}
				if(InChargeMonitor.length > 0) {
					vm.criteria.ainChargeMonitorId = InChargeMonitor[0].userId;
				}
				if(BBDirector.length > 0) {
					vm.criteria.bdirectorId = BBDirector[0].userId;
				}
				if(BBInChargeConstruct.length > 0) {
					vm.criteria.binChargeConstructId = BBInChargeConstruct[0].userId;
				}
				
			})
			.catch(function() {
				console.error('getRoleId error');
			});
		  })
		  .catch(function() {
		    console.error('Gists error');
		});
		});
		// ////////////////////////END//////////////////////////////////
		
		
		// di toi 1 tab moi
		function goTo(menuKey) {
			   var hasPerm = true;
			   if (hasPerm) {
			    var template = Constant.getTemplateUrl(menuKey);
			    postal.publish({
			     channel : "Tab",
			     topic   : "open",
			     data    : template
			    });
			   }
		}
		// goto
		vm.signCA = function() {
			if( vm.criteria.disUnloadConsMinId > 0)
			{
				if(vm.criteria.statusCa == 0){
					distanceUnloadedMaterialsList.exportDistanceUnloadConstrMinutes(vm.criteria).then(function(data){
		    			vm.theApproval.path= data.fileName;
		    			vm.theApproval.nameFile= 'BM-TCNT-12.pdf';
		    			theApproval.setItem(vm.theApproval);
		    			goTo('THE_APPROVAL');
		        	}).catch( function(){
		        		toastr.error(gettextCatalog.getString("L???i khi export, kh??ng th??? tr??nh duy???t!"));
		        		return;
		        	}).finally(function(){
		        		$('#loading').hide();
		        	});
		        	$('#loading').show();
				}else
				{
					toastr.error(gettextCatalog.getString("Tr???ng th??i n??y kh??ng th??? tr??nh duy???t"));
				}
			}else{toastr.warning("B???n ch??a ch???n b???n ghi n??o ????? th???c hi???n !");}
		}
		// add
		vm.add = function(){
			if(vm.showDetailList || vm.showDetailForm){
				vm.showDetailForm = true;
				vm.showDetailList = false;
				vm.showSignCA = false;
				vm.showAppro = false;
				vm.showDeleteAll = false;
				vm.showSave = true;
				vm.disabledNoi = false;
				vm.criteria = {};	
				
				distanceUnloadedMaterialsList.getListEmployeeByConstruction(vm.item.constructId).then(function(data) {
					totalListEmployee = data.plain();
					distanceUnloadedMaterialsList.getRoleId().then(function(data) {
//						vm.role = data;
						for (var i = 0; i < totalListEmployee.length; i++) {
							if(totalListEmployee[i].roleid == Constant.ROLE_ID["employee_roleID_CDT_DDPN"]){//2
								Director.push(totalListEmployee[i]);
							}
							if(totalListEmployee[i].roleid == Constant.ROLE_ID["employee_roleID_CDT_PTGST"]){//4
								InChargeMonitor.push(totalListEmployee[i]);
							}
							if(totalListEmployee[i].roleid == Constant.ROLE_ID["employee_roleID_DT_GDNT"]){//3
								BBDirector.push(totalListEmployee[i]);
							}
							if(totalListEmployee[i].roleid == Constant.ROLE_ID["employee_roleID_DT_PTTC"]){//1
								BBInChargeConstruct.push(totalListEmployee[i]);
							}
						}
						
						vm.aDirector= Director;
						vm.aInChargeMonitor= InChargeMonitor;
						vm.bDirector= BBDirector;
						vm.bInChargeConstruct= BBInChargeConstruct;
						
						if(Director.length > 0) {
							vm.criteria.adirectorId = Director[0].userId;
						}
						if(InChargeMonitor.length > 0) {
							vm.criteria.ainChargeMonitorId = InChargeMonitor[0].userId;
						}
						if(BBDirector.length > 0) {
							vm.criteria.bdirectorId = BBDirector[0].userId;
						}
						if(BBInChargeConstruct.length > 0) {
							vm.criteria.binChargeConstructId = BBInChargeConstruct[0].userId;
						}
						
					})
					.catch(function() {
						console.error('getRoleId error');
					});
				  })
				  .catch(function() {
				    console.error('Gists error');
				});
				for (var i = 0; i < vm.aDirector.length; i++) {
					if(vm.aDirector[i].isCurrent === 1){
						vm.criteria.adirectorId = vm.aDirector[i].userId;
						break;
					} else {
						vm.criteria.adirectorId = vm.aDirector[0].userId;
						break;
					}
				}
				for (var i = 0; i < vm.aInChargeMonitor.length; i++) {
					if(vm.aDirector[i].isCurrent === 1){
						vm.criteria.ainChargeMonitorId = vm.aInChargeMonitor[i].userId;
						break;
					} else {
						vm.criteria.ainChargeMonitorId = vm.aInChargeMonitor[0].userId;
						break;
					}
				}
				for (var i = 0; i < vm.bDirector.length; i++) {
					if(vm.aDirector[i].isCurrent === 1){
						vm.criteria.bdirectorId = vm.bDirector[i].userId;
						break;
					} else {
						vm.criteria.bdirectorId = vm.bDirector[0].userId;
						break;
					}
				}
				for (var i = 0; i < vm.bInChargeConstruct.length; i++) {
					if(vm.aDirector[i].isCurrent === 1){
						vm.criteria.binChargeConstructId = vm.bInChargeConstruct[i].userId;
						break;
					} else {
						vm.criteria.binChargeConstructId = vm.bInChargeConstruct[0].userId;
						break;
					}
				}
				
				
				cutkitDataTable([]);
				OtoDataTable([]);
				bocVacDataTable([]);
				refreshGrid([], vm.OtogridName);
				refreshGrid([], vm.cutKitgridName);
				refreshGrid([], vm.bocVacgridName);
				vm.criteria.constructId = vm.item.constructId;
				$(".k-invalid-msg").hide();
			}
		}
		// EDIT
		vm.showGrid = function (){
			if(vm.showDetailList)
			{
				if( vm.criteria.disUnloadConsMinId > 0)
				{
						vm.showDetailForm = true;
						vm.showDetailList = false;
						vm.showApproval = false;
						vm.showSignCA = true;
						vm.showAppro = true;
						vm.showDeleteAll = false;
						vm.showSave = true;
						vm.disabledNoi = true;
						vm.criteria.constructId = vm.item.constructId;
						$(".k-invalid-msg").hide();
						if(vm.criteria.createdUserId != Constant.user.srvUser.catEmployeeId)
						{
							toastr.warning("B???n kh??ng ???????c s???a b???n ghi ng?????i kh??c t???o !");
							vm.disableAll = true;
						}else{
							vm.disableAll = false;
						}
						if(vm.criteria.statusCa==1||vm.criteria.statusCa==2){
							vm.disableAll = true;
						}else{
							vm.disableAll = false;
						}
				}
				else
				{
					toastr.warning("B???n ch??a ch???n b???n ghi n??o ????? th???c hi???n !");
				}
			}
			else
			{
				vm.showDetailList = true;
				vm.showDetailForm = false;
				vm.showApproval = false;
				vm.showAdd = true;
				vm.showDeleteAll = true;
				vm.showExportPDF = true;
				vm.showExportDOC = true;
				vm.showSignCA = true;
				vm.showAppro = true;
				vm.showSave = false;
				reloadInspectionList();
			}
		}
		// ph?? duy???t
		vm.approval = function (){
			if(!vm.showApproval)
			{
				if( vm.criteria.disUnloadConsMinId > 0)
				{
					if(vm.criteria.statusCa == 1){
							vm.showApproval = true;
							vm.showDetailList = false;
							vm.showDetailForm = false;
							vm.showAdd = false;
							vm.showDeleteAll = false;
							vm.showExportPDF = false;
							vm.showExportDOC = false;
							vm.showSignCA = false;
							vm.showAppro = false;
							vm.showSave = true;
							vm.criteria.constructId = vm.item.constructId;
							vm.appro.statusCa = 2;
							if(vm.criteria.adirectorId != Constant.user.srvUser.catEmployeeId && vm.criteria.ainChargeMonitorId != Constant.user.srvUser.catEmployeeId &&
								vm.criteria.bdirectorId != Constant.user.srvUser.catEmployeeId && vm.criteria.binChargeConstructId != Constant.user.srvUser.catEmployeeId )
							{
								toastr.warning(gettextCatalog.getString("B???n kh??ng ???????c quy???n ph?? duy???t !"));
							}
					}else if(vm.criteria.statusCa == 2)
					{
						toastr.warning(gettextCatalog.getString("B???n ghi n??y ???? ???????c ph?? duy???t !"));
					}
					else
					{
						toastr.warning(gettextCatalog.getString("Tr???ng th??i n??y kh??ng th??? ph?? duy???t !"));
					}
				}
				else
				{
					toastr.warning(gettextCatalog.getString("B???n ch??a ch???n b???n ghi n??o ????? th???c hi???n !"));
				}
			}
			else
			{
				vm.showDetailList = true;
				vm.showDetailForm = false;
				vm.showApproval = false;
				vm.showAdd = true;
				vm.showDeleteAll = true;
				vm.showExportPDF = true;
				vm.showExportDOC = true;
				vm.showSignCA = true;
				vm.showAppro = true;
				vm.showSave = false;
				reloadInspectionList();
			}
		}
		// ngoccx
	      // huy trinh duyet
			vm.cancelApprovalBtn= function(){
				var grid = vm.shipmentGrid;
				if (grid.select() == null || grid.select().length == 0) {
					toastr.warning("C???n ch???n b???n ghi tr?????c khi th???c hi???n thao t??c n??y!");
					return;
				}
				vm.criteria.tableName = 'DISTANCE_UNLOAD_CONSTR_MINUTES';
				vm.criteria.tableId = vm.criteria.disUnloadConsMinId;
				vm.criteria.tableIdField = 'DIS_UNLOAD_CONS_MIN_ID';
				vm.criteria.constrCompReMapId = vm.criteria.constrcompleterecordsmap.constrCompReMapId;
				if(vm.criteria.statusCa == 1){
					if(vm.criteria.createdUserId != Constant.user.srvUser.catEmployeeId){
						toastr.warning(gettextCatalog.getString("Ng?????i t???o m???i c?? quy???n h???y tr??nh duy???t!"));
						return;
					}else{
						if(confirm('X??c nh???n h???y tr??nh duy???t')){
						distanceUnloadedMaterialsList.cancelAprroval(vm.criteria).then(function() {
							status = true;
							$rootScope.$broadcast("ChangeStatusHuyDuyet", status);
							reloadInspectionList();
					toastr.success(gettextCatalog.getString("???? h???y tr??nh duy???t !"));
				}, function(){
				toastr.error(gettextCatalog.getString("L???i khi h???y tr??nh duy???t!"));
				return;
			});}}
				}else{
					toastr.warning("Ch??? tr??nh duy???t m???i ???????c h???y tr??nh duy???t");
				}
			}
			
			$scope.$on("ChangeStatusApproval", function(event, result) {
	            if (result) {
	            	vm.showDetail = false;
	            	reloadInspectionList();
	            }
	        });
		// exportPDF
		vm.exportFile = function (){
			var selectedRow = [];
			var grid = vm.shipmentGrid;
			grid.table.find("tr").each(function(idx, item) {
			var row = $(item);
			var checkbox = $('[name="gridcheckbox"]', row);
				if (checkbox.is(':checked')){
					var dataItem = grid.dataItem(item);
					selectedRow.push(dataItem.disUnloadConsMinId);
				}
			});
			if(selectedRow.length == 0 && checkonchange == 0 && vm.criteria.disUnloadConsMinId == null )
			{
			    toastr.warning("B???n c???n ch???n b???n ghi ????? export !");
			}else{
				if(checkonchange == 1 && vm.showDetailList == false)
				{
			       	$('#loading').show();
			       	distanceUnloadedMaterialsList.exportDistanceUnloadConstrMinutes(vm.criteria).then(function(data){
			       		window.location = RestEndpoint.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
			    		toastr.success(gettextCatalog.getString("Export file .PDF th??nh c??ng!"));
			        }).catch( function(){
			        	toastr.error(gettextCatalog.getString("L???i khi export!"));
			        	return;
			        }).finally(function(){
						$('#loading').hide();
					});
				}
				else if(selectedRow.length > 0)
				{
					$('#loading').show();
			    	    distanceUnloadedMaterialsList.exportListDistanceUnloadConstrMinutes(selectedRow).then(function(data){
			    	    window.location = RestEndpoint.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
			    	     toastr.success(gettextCatalog.getString("Export file .PDF th??nh c??ng!"));
			    	}).catch( function(){
			    	    toastr.error(gettextCatalog.getString("L???i khi export!"));
			    	    return;
			    	}).finally(function(){
			    	    $('#loading').hide();
			    	});
				}
				else if(checkonchange == 1 && selectedRow.length > 0)
				{
					$('#loading').show();
			    	    distanceUnloadedMaterialsList.exportListDistanceUnloadConstrMinutes(selectedRow).then(function(data){
			    	    window.location = RestEndpoint.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
			    	     toastr.success(gettextCatalog.getString("Export file .PDF th??nh c??ng!"));
			    	}).catch( function(){
			    	    toastr.error(gettextCatalog.getString("L???i khi export!"));
			    	    return;
			    	}).finally(function(){
			    	    $('#loading').hide();
			    	});
				}
				else
				{
					$('#loading').show();
			       	distanceUnloadedMaterialsList.exportDistanceUnloadConstrMinutes(vm.criteria).then(function(data){
			       		window.location = RestEndpoint.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
			    		toastr.success(gettextCatalog.getString("Export file .PDF th??nh c??ng!"));
			        }).catch( function(){
			        	toastr.error(gettextCatalog.getString("L???i khi export!"));
			        	return;
			        }).finally(function(){
						$('#loading').hide();
					});
				}
			}
		}
		// exportDOCX
		vm.exportFileDOC = function(){
			var selectedRow = [];
			var grid = vm.shipmentGrid;
			grid.table.find("tr").each(function(idx, item) {
			var row = $(item);
			var checkbox = $('[name="gridcheckbox"]', row);
				if (checkbox.is(':checked')){
					var dataItem = grid.dataItem(item);
					selectedRow.push(dataItem.disUnloadConsMinId);
				}
			});
			if(selectedRow.length == 0 && checkonchange == 0 && vm.criteria.disUnloadConsMinId == null )
			{
			    toastr.warning("B???n c???n ch???n b???n ghi ????? export !");
			}else{
				if(checkonchange == 1 && vm.showDetailList == false)
				{
			       	$('#loading').show();
			       	distanceUnloadedMaterialsList.exportDocDistanceUnloadConstrMinutes(vm.criteria).then(function(data){
			       		window.location = RestEndpoint.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
			    		toastr.success(gettextCatalog.getString("Export file .DOCX th??nh c??ng!"));
			        }).catch( function(){
			        	toastr.error(gettextCatalog.getString("L???i khi export!"));
			        	return;
			        }).finally(function(){
						$('#loading').hide();
					});
				}
				else if(selectedRow.length > 0)
				{
					$('#loading').show();
			    	    distanceUnloadedMaterialsList.exportListDocDistanceUnloadConstrMinutes(selectedRow).then(function(data){
			    	    window.location = RestEndpoint.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
			    	     toastr.success(gettextCatalog.getString("Export file .DOCX th??nh c??ng!"));
			    	}).catch( function(){
			    	    toastr.error(gettextCatalog.getString("L???i khi export!"));
			    	    return;
			    	}).finally(function(){
			    	    $('#loading').hide();
			    	});
				}
				else if(checkonchange == 1 && selectedRow.length > 0)
				{
					$('#loading').show();
			    	    distanceUnloadedMaterialsList.exportListDocDistanceUnloadConstrMinutes(selectedRow).then(function(data){
			    	    window.location = RestEndpoint.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
			    	     toastr.success(gettextCatalog.getString("Export file .DOCX th??nh c??ng!"));
			    	}).catch( function(){
			    	    toastr.error(gettextCatalog.getString("L???i khi export!"));
			    	    return;
			    	}).finally(function(){
			    	    $('#loading').hide();
			    	});
				}
				else
				{
					$('#loading').show();
			       	distanceUnloadedMaterialsList.exportDocDistanceUnloadConstrMinutes(vm.criteria).then(function(data){
			       		window.location = RestEndpoint.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
			    		toastr.success(gettextCatalog.getString("Export file .DOCX th??nh c??ng!"));
			        }).catch( function(){
			        	toastr.error(gettextCatalog.getString("L???i khi export!"));
			        	return;
			        }).finally(function(){
						$('#loading').hide();
					});
				}
			}
		}
		// validate b???ng
		var mess = "";
		var messs = "";
		var messss = "";
		var mess1 = "Lo???i VT v???n chuy???n";
		var mess2 = "????n v??? t??nh";
		var mess3 = "Kh???i l?????ng ph???i l?? s??? v?? l???n h??n 0 ";
		var mess4 = "C??? ly v???n chuy???n ph???i l?? s??? v?? l???n h??n 0" ;
		var mess5 = "Lo???i ???????ng";
		var mess6 = "Tr???ng t???i xe ph???i l?? s??? v?? l???n h??n 0";
		var mess7 = "????? d???c";
		function checkValidateListOto(list){
			for (var i = 0; i < list.length; i++) {
				mess = "";
				if(list[i].materialType == 0 || list[i].materialType == "" || list[i].materialType == undefined || list[i].materialType == null)
				{
					mess = mess + mess1;
					return false;
				}
				if(list[i].unit == 0 || list[i].unit == "" || list[i].unit == undefined || list[i].unit == null){
					mess = mess + mess2;
					return false;
				}
				if(list[i].quantity <= 0 || list[i].quantity == null){
					mess = mess + mess3;
					return false;
				}
				if(list[i].transportDistance <= 0 || list[i].transportDistance == null){
					mess = mess + mess4;
					return false;
				}
				if(list[i].truckLoad <= 0 || list[i].truckLoad == null){
					mess = mess + mess6;
					return false;
				}
			}
			mess = "";
			return true;
		}
		function checkValidateListCutKit(list){
			for (var i = 0; i < list.length; i++) {
				messs = "";
				if(list[i].materialType == 0 || list[i].materialType == "" || list[i].materialType == undefined || list[i].materialType == null)
				{
					messs = messs + mess1;
					return false;
				}
				if(list[i].unit == 0 || list[i].unit == "" || list[i].unit == undefined || list[i].unit == null){
					messs = messs + mess2;
					return false;
				}
				if(list[i].quantity <= 0 || list[i].quantity == null){
					messs = messs + mess3;
					return false;
				}
				if(list[i].transportDistance <= 0 || list[i].transportDistance == null){
					messs = messs + mess4;
					return false;
				}
				if(list[i].roadTypeStr == 0 || list[i].roadTypeStr == "" || list[i].roadTypeStr == undefined || list[i].roadTypeStr == null){
					messs = messs + mess5;
					return false;
				}
				if(list[i].slopeDegree == 0 || list[i].slopeDegree == "" || list[i].slopeDegree == undefined || list[i].slopeDegree == null){
					messs = messs + mess7;
					return false;
				}
			}
			messs = "";
			return true;
		}
		function checkValidateListBocVac(list){
			for (var i = 0; i < list.length; i++) {
				messss = "";
				if(list[i].materialType == 0 || list[i].materialType == "" || list[i].materialType == undefined || list[i].materialType == null)
				{
					messss = messss + mess1;
					return false;
				}
				if(list[i].unit == 0 || list[i].unit == "" || list[i].unit == undefined || list[i].unit == null){
					messss = messss + mess2;
					return false;
				}
				if(list[i].quantity <= 0 || list[i].quantity == null){
					messss = messss + mess3;
					return false;
				}
			}
			messss = "";
			return true;
		}
		// Ch???n item trong b???ng b???ng checkbox
		function chkSelectAll() {
	    	var grid = vm.shipmentGrid;
	    	chkSelectAllBase(vm.chkAll, grid);
	    }
		// chon item popup
		vm.popupTemp = {};
		vm.onRowChange = onRowChange;
		vm.onSave = onSave;
		function onRowChange(dataItem, popupId) {
			switch (popupId){
				case 'showNoiDungPS':
					vm.popupTemp.estimatesWorkItemId = dataItem.estimatesWorkItemId;
	            	vm.popupTemp.estimatesWorkItemName = dataItem.workItemName;
					break;
				default:
					toastr.warning("L???i ch???n item popup");
				    break;
			}
		}
		// luu item popup
		function onSave(popupId) {
			switch (popupId){
				case 'showNoiDungPS':
					vm.criteria.estimatesWorkItemId = vm.popupTemp.estimatesWorkItemId;
					vm.criteria.estimatesWorkItemName = vm.popupTemp.estimatesWorkItemName;
					$(".k-invalid-msg").hide();
					break;
				default:
					toastr.warning("L???i l??u item popup");
				    break;	
			}
		}
		// SAVE
		vm.dataSourceOto={};
		vm.dataSourceCutKit={};
		vm.dataSourceBocVac={};
		vm.save = save;
		function save() {
			if(!vm.showDetailList){
				if(vm.showApproval == true){
					vm.appro.disUnloadConsMinId = vm.criteria.disUnloadConsMinId;
					vm.appro.estimatesWorkItemId = vm.criteria.estimatesWorkItemId;
					distanceUnloadedMaterialsList.appro(vm.appro).then(function (x) {
						if(x == '1'){
							toastr.warning("Ch??a ?????n l?????t duy???t");
						}
						if(x == '2'){
							toastr.warning("???? duy???t r???i");
						}
						if(x == '5'){
							toastr.warning("B???n kh??ng ???????c ph??p duy???t");
						}
						if(x == '4'){
							vm.showAdd = true;
							vm.showDeleteAll = true;
							vm.showExportPDF = true;
							vm.showExportDOC = true;
							vm.showSignCA = true;
							vm.showAppro = true;
							vm.showSave = false;
							vm.showDetailList = true;
							vm.showDetailForm = false;
							vm.showApproval = false;
							reloadInspectionList();
							toastr.success("T??? ch???i duy???t th??nh c??ng");
						}
						if(x == '3'){
							vm.showAdd = true;
							vm.showDeleteAll = true;
							vm.showExportPDF = true;
							vm.showExportDOC = true;
							vm.showSignCA = true;
							vm.showAppro = true;
							vm.showSave = false;
							vm.showDetailList = true;
							vm.showDetailForm = false;
							vm.showApproval = false;
							reloadInspectionList();
							toastr.success("Duy???t th??nh c??ng");
						}
					}, function () {
						toastr.error("Duy???t l???i");
					});
				}
				else
				{
					var list=[];
					var dataSourceOto = $("#OtogridName").data().kendoGrid.dataSource.view();
					var dataSourceCutKit = $("#cutKitgridName").data().kendoGrid.dataSource.view();
					var dataSourceBocVac = $("#bocVacgridName").data().kendoGrid.dataSource.view();
					for(var i=0; i<dataSourceOto.length;i++){
						vm.dataSourceOto=dataSourceOto[i];
						vm.dataSourceOto.transportType=1;
						list.push(vm.dataSourceOto);
					}
					for(var j=0; j<dataSourceCutKit.length;j++){
						vm.dataSourceCutKit=dataSourceCutKit[j];
						vm.dataSourceCutKit.transportType=2;
						list.push(vm.dataSourceCutKit);
					}
					for(var k=0; k<dataSourceBocVac.length;k++){
						vm.dataSourceBocVac=dataSourceBocVac[k];
						vm.dataSourceBocVac.transportType=3;
						list.push(vm.dataSourceBocVac);
					}
					vm.criteria.isActive= 1;
					vm.criteria.distanceUnloadList = list;
					if (dataSourceOto.length > 0){
					    document.getElementById("carGatherPlace").required = true;
					    document.getElementById("carDesPlace").required = true;
					}
					else{
					    if (document.getElementById("carGatherPlace") != null)
					     document.getElementById("carGatherPlace").required = false;
					    if (document.getElementById("carDesPlace") != null)
					     document.getElementById("carDesPlace").required = false;
					}
					if (dataSourceCutKit.length > 0){
					    document.getElementById("wheelbarrowGatherPlace").required = true;
					    document.getElementById("wheelbarrowDesPlace").required = true;
					}else{
					    if (document.getElementById("wheelbarrowGatherPlace") != null)
					     document.getElementById("wheelbarrowGatherPlace").required = false;
					    if (document.getElementById("wheelbarrowDesPlace") != null)
					     document.getElementById("wheelbarrowDesPlace").required = false;
					}
					if (dataSourceBocVac.length > 0){
					    document.getElementById("handmadeGatherPlace").required = true;
					    document.getElementById("handmadeDesPlace").required = true;
					}
					else{
					    if (document.getElementById("handmadeGatherPlace") != null)
					     document.getElementById("handmadeGatherPlace").required = false;
					    if (document.getElementById("handmadeDesPlace") != null)
					     document.getElementById("handmadeDesPlace").required = false;
					}
					if(vm.criteria.statusCa == 1)
					{
						toastr.warning("???? tr??nh duy???t kh??ng th??? s???a");
					}
					else if(vm.criteria.statusCa == 2)
					{
						toastr.warning("???? ph?? duy???t kh??ng th??? s???a");
					}
					else
					{
						if (vm.validator.validate()) {
							if (vm.criteria.estimatesWorkItemId != null || vm.criteria.estimatesWorkItemId != undefined) {
							if(vm.criteria.distanceUnloadList.length > 0){
							if(checkValidateListOto(dataSourceOto)){
								if(checkValidateListCutKit(dataSourceCutKit)){
									if(checkValidateListBocVac(dataSourceBocVac)){	
								if(!vm.showDetailList && !vm.showApproval){
										if(getMultiDeleteList.length > 0){
											for (var z = 0; z < getMultiDeleteList.length; z++) {
												distanceUnloadedMaterialsList.deleteDistanceUnloadList(getMultiDeleteList[z]).then(function() {
											    },function(){
											    	toastr.warning("L???i");
											    });}
										}
										for(var i=0;i<vm.criteria.distanceUnloadList.length;i++){
											if(vm.criteria.distanceUnloadList[i].transportType == 1){
												vm.criteria.distanceUnloadList[i].roadType = vm.criteria.distanceUnloadList[i].roadTypeDetail.id;
											}
											if(vm.criteria.distanceUnloadList[i].transportType == 1){
												vm.criteria.distanceUnloadList[i].roadType = vm.criteria.distanceUnloadList[i].roadTypeDetail.id;
											}
										}
										if (vm.criteria.disUnloadConsMinId != undefined && vm.criteria.disUnloadConsMinId != 0)
										{
											if(vm.criteria.createdUserId == Constant.user.srvUser.catEmployeeId){
												if(vm.criteria.estimatesWorkItemId == checkESTMA){
													if(vm.criteria.statusCa == 3)
													{
														vm.criteria.statusCa = 0;
													}
													distanceUnloadedMaterialsList.updateConstructionacceptance(vm.criteria).then(function()
															{  
																vm.showDetailList = true;
																vm.showDetailForm = false;
																vm.showApproval = false;
																vm.showSignCA = true;
																vm.showSave = false;
																vm.showAppro = true;
																vm.showDeleteAll = true;
																reloadInspectionList();
																toastr.success(gettextCatalog.getString("L??u th??nh c??ng!"));
															}, function() {
												                toastr.error(gettextCatalog.getString("L???i xu???t hi???n !"));
															})
												}else{toastr.warning(gettextCatalog.getString("B???n kh??ng ???????c s???a c??ng vi???c v???n chuy???n !"));}
											}else{toastr.warning(gettextCatalog.getString("B???n kh??ng ph???i ng?????i t???o kh??ng ???????c s???a !"));}
										} else {
											vm.criteria.disUnloadConsMinId = 0;
											vm.criteria.statusCa= 0;
											vm.criteria.createdUserId = Constant.user.srvUser.catEmployeeId;
											distanceUnloadedMaterialsList.createDistanceUnloadConstrMinuteService(vm.criteria).then(
													function(){
														//distanceUnloadedMaterialsList.updateStatus(vm.criteria.sceneGenWorkListId);
														vm.showDetailList = true;
														vm.showDetailForm = false;
														vm.showApproval = false;
														vm.showSignCA = true;
														vm.showAppro = true;
														vm.showSave = false;
														vm.showDeleteAll = true;
														reloadInspectionList();
														toastr.success(gettextCatalog.getString("L??u th??nh c??ng!"));
													}, function() {
										                toastr.error(gettextCatalog.getString("L???i xu???t hi???n !"));
													}
													)}
												}
											}else {toastr.warning(gettextCatalog.getString("B???ng b???c v??c d??? li???u kh??ng ?????y ????? kh??ng ???????c l??u !"+ messss));}
										}else {toastr.warning(gettextCatalog.getString("B???ng xe c??t k??t d??? li???u kh??ng ?????y ????? kh??ng ???????c l??u !"+ messs));}
									}else {toastr.warning(gettextCatalog.getString("B???ng v???n chuy???n b???ng ?? t?? d??? li???u kh??ng ?????y ????? kh??ng ???????c l??u !"+ mess));}
								}else {toastr.warning(gettextCatalog.getString("B???ng v???n chuy???n, b???c d??? kh??ng c?? d??? li???u kh??ng ???????c l??u !"));}
							}else {toastr.warning(gettextCatalog.getString("B???n ch??a ch???n C??ng vi???c v???n chuy???n t??? popup !"));}
		}}}}}
		// khai bao loai duong
		function categoryDropDownEditor(container, options) {
			$('<input required name="' + options.field + '"/>').appendTo(container).kendoDropDownList({
				autoBind: false,
				dataTextField: "value",
				dataValueField: "id",
				dataSource: [{ id: 1, value: "Lo???i 1" }, { id: 2, value: "Lo???i 2" }, { id: 3, value: "Lo???i 3" }, { id: 4, value: "Lo???i 4" }, { id: 5, value: "Lo???i 5" }, { id: 6, value: "Kh??c" }]
			});
		}
		// ch???n item b???ng b???n ghi onchange
		vm.onChange = onChange;
		function onChange() {
			checkonchange  = 0;
			checkESTMA = 0;
        	if (vm.shipmentGrid.select().length > 0) {
        		var tr = vm.shipmentGrid.select().closest("tr");
            	var dataItem = vm.shipmentGrid.dataItem(tr);
            	checkESTMA = dataItem.estimatesWorkItemId;
            	vm.criteria = dataItem;
            	var	array = dataItem.distanceUnloadList;	
              	 var oToGird = [];
            	 var cutKitGird = [];
            	 var bocVacGird = [];
                 for(var i = 0;i <array.length;i++){
    				 if(array[i].transportType == 1){
    					oToGird.push(array[i]);
    				 }else if(array[i].transportType == 2){
    					cutKitGird.push(array[i]);
    				 }
                     else if(array[i].transportType == 3){
                    	bocVacGird.push(array[i]);
                     }
    				}
       				cutkitDataTable(cutKitGird);
       				OtoDataTable(oToGird);
       				bocVacDataTable(bocVacGird);
       				
       				vm.theApproval.code = vm.criteria.code;
    				vm.theApproval.constructId = vm.criteria.constructId;
    				vm.appro.employeeId = vm.item.getcatEmployeeId;
    				vm.theApproval.constrCompReMapId = vm.criteria.constrcompleterecordsmap.constrCompReMapId;
    				vm.appro.constrCompReMapId = vm.criteria.constrcompleterecordsmap.constrCompReMapId;
    				vm.theApproval.stringEmployee  = vm.criteria.binChargeConstructId + ',' +vm.criteria.ainChargeMonitorId + ',' + vm.criteria.bdirectorId + ',' + vm.criteria.adirectorId;
    				vm.theApproval.isSign ='theApproval';
    				//1-4-3-2
    				vm.theApproval.roleId = [Constant.ROLE_ID["employee_roleID_DT_PTTC"],Constant.ROLE_ID["employee_roleID_CDT_PTGST"],
    				                         Constant.ROLE_ID["employee_roleID_DT_GDNT"],Constant.ROLE_ID["employee_roleID_CDT_DDPN"]];
    				vm.theApproval.roleName = ["Ph??? tr??ch thi c??ng tr???c ti???p","Ph??? tr??ch gi??m s??t thi c??ng","Gi??m ?????c","Th??? tr?????ng ????n v???"]
    				theApproval.setItem(vm.theApproval);
    				checkonchange = checkonchange + 1;
       				
    				refreshGrid(oToGird, vm.OtogridName);
    				refreshGrid(cutKitGird, vm.cutKitgridName);
    				refreshGrid(bocVacGird, vm.bocVacgridName);
            }
        }
		// goi form
		initFormData();
		function initFormData()
		{
			distanceUnloadedMaterialsList.getListCR(vm.item).then(function (d){
			fillDataTable(d.plain());
			
			},function (){
				toastr.warning("L???i");
			});
		}
		// reload b???ng form
		function reloadInspectionList(){
			distanceUnloadedMaterialsList.getListCR(vm.item).then(function (d){
				refreshGrid(d , vm.shipmentGrid);
				$('[name="gridchkselectall"]').prop( "checked", false );
			},function (){
				toastr.warning("L???i");
			});
		}
		// reload tong
		function refreshGrid(d , grid) {
			if(grid){
				grid.dataSource.data(d);
	        	grid.refresh();	
			}
        }
		// xoa
		vm.multiDelete = multiDelete;
        function multiDelete() {
			var selectedRow = [];
			var checkuser = [];
			var selectedRowDele = 0;
			var selectedRowDeOk = 0;
			var grid = vm.shipmentGrid;
			grid.table.find("tr").each(function(idx, item) {
				var row = $(item);
				var checkbox = $('[name="gridcheckbox"]', row);
				if (checkbox.is(':checked')) {
					var dataItem = grid.dataItem(item);
					if(dataItem.statusCa == 0 || dataItem.statusCa == 3)
					{
						selectedRow.push(dataItem);
						checkuser.push(dataItem.createdUserId);
						selectedRowDeOk = selectedRowDeOk + 1;
					}else
					{
						selectedRowDele = selectedRowDele + 1;
					}
				}
			});
			if (selectedRowDele > 0 && selectedRowDeOk == 0) {
				toastr.warning(gettextCatalog.getString("Tr???ng th??i Tr??nh Duy???t v?? ???? Duy???t kh??ng ???????c x??a"));
			}
			else if (selectedRow.length > 0) {
				vm.xoa = false;
				vm.xoaFalse = false;
				vm.xoaNotUser = false;
				for (var i = 0; i < selectedRow.length; i++) {
							if(checkuser[i] == Constant.user.srvUser.catEmployeeId){
								if(confirm('X??c nh???n x??a')){
								distanceUnloadedMaterialsList.updateIsActiveDistanceUnloadConstrMinutes(selectedRow[i].disUnloadConsMinId).then(function() {
									if(vm.xoa == false)
									{
										toastr.success("???? x??a th??nh c??ng");
										vm.xoa = true;
									}
									if(selectedRowDele > 0 && vm.xoaFalse == false)
						    		{
						    			toastr.warning("Tr???ng th??i tr??nh duy???t v?? ???? duy???t kh??ng ???????c x??a.");
						    			vm.xoaFalse = true;
						    		}
									reloadInspectionList();
									}, function(errResponse) {
									if (errResponse.status == 302){
						    		 toastr.error(gettextCatalog.getString("B???n ghi ??ang ???????c s??? d???ng"));
									}else{
						    		 toastr.error(gettextCatalog.getString("C?? l???i x???y ra trong qu?? tr??nh x??a!"));
									} 
						     });
						    }
						    }else if(!vm.xoaNotUser){
						    	toastr.warning(gettextCatalog.getString("B???n kh??ng ph???i ng?????i t???o kh??ng ???????c x??a !"));
						    	vm.xoaNotUser = true;
						}
				}
			}else{toastr.warning(gettextCatalog.getString("B???n c???n ch???n m???t b???n ghi tr?????c"));}
		}
		// Begin add + xoa grid con tabOTO
		vm.add1 = add1;
		function add1()
		{
			  var grid = $("#OtogridName").data("kendoGrid");
			  grid.dataSource.insert({
				  "distanceUnloadListId" : "",
				  "materialType": "",
					"unit": "",
					"quantity": "",
					"transportDistance": "",
					"roadTypeDetail": { id: "1", value: "Lo???i 1" },
					"truckLoad":""
				});
		}
		vm.remove1 = remove1;
		function remove1(){
			var grid1 = vm.OtogridName;
			var ck = false;
			$("#OtogridName").data("kendoGrid").table.find("tr").each(
						function(idx, item) {
						      var dataItem = grid1.dataItem(item);
						      $(item).find('input[type="checkbox"]').each(
						        function(idx, item){
						         if ($(item).is(':checked')){
						        	 ck = true;
						        	 if(dataItem.distanceUnloadListId > 0)
						        	 {
						        		 getMultiDeleteList.push(dataItem.distanceUnloadListId);
						        	 }
			}})})
			 $("#OtogridName").data("kendoGrid").table.find("tr").each(function(idx, item) {
			if($(item).find('input[type="checkbox"]').is(':checked')) {$("#OtogridName").data("kendoGrid").removeRow($(item))}})
			if(ck == false){
				toastr.warning(gettextCatalog.getString("B???n ch??a ch???n b???n ghi n??o ????? x??a !"));
			}
	    }
		// Begin add + xoa grid con tabCutKit
		vm.add2 = add2;
		function add2() {
				  var grid = $("#cutKitgridName").data("kendoGrid");
				  grid.dataSource.add({
					  "distanceUnloadListId" : "",
					  "materialType": "",
						"unit": "",
						"quantity": "",
						"transportDistance": "",
						"roadTypeStr": "",
						"slopeDegree":""
					});}
		vm.remove2 = remove2;
		function remove2(){
			var gridCutKit = vm.cutKitgridName;
			var ckCutKit = false;
			$("#cutKitgridName").data("kendoGrid").table.find("tr").each(
						function(idx, item) {
						      var dataItem = gridCutKit.dataItem(item);
						      $(item).find('input[type="checkbox"]').each(
						        function(idx, item){
						         if ($(item).is(':checked')){
						        	 ckCutKit = true;
						        	 if(dataItem.distanceUnloadListId > 0)
						        	 {
						        		 getMultiDeleteList.push(dataItem.distanceUnloadListId);
						        	 }
			}})})
			 $("#cutKitgridName").data("kendoGrid").table.find("tr").each(function(idx, item) {
			if($(item).find('input[type="checkbox"]').is(':checked')) {$("#cutKitgridName").data("kendoGrid").removeRow($(item))}})
			if(ckCutKit == false){
				toastr.warning("B???n ch??a ch???n b???n ghi n??o ????? x??a !");
        		return;
			}
	    }
		// Begin add + xoa grid con tabBocVac
		vm.add3 = add3;
		function add3(){
			  var grid = $("#bocVacgridName").data("kendoGrid");
			  grid.dataSource.add({
				  "distanceUnloadListId" : "",
				  "materialType": "",
					"unit": "",
					"quantity": "",
					"note": ""
				} );}
		vm.remove3 = remove3;
		function remove3(){
			var gridBocVac = vm.bocVacgridName;
			var ckBocVac = false;
			$("#bocVacgridName").data("kendoGrid").table.find("tr").each(
					function(idx, item) {
					      var dataItem = gridBocVac.dataItem(item);
					      $(item).find('input[type="checkbox"]').each(
					        function(idx, item){
					         if ($(item).is(':checked')){
					        	 ckBocVac = true;
					        	 if(dataItem.distanceUnloadListId > 0)
					        	 {
					        		 getMultiDeleteList.push(dataItem.distanceUnloadListId);
					        	 }
			}})})
		 $("#bocVacgridName").data("kendoGrid").table.find("tr").each(function(idx, item) {
		if($(item).find('input[type="checkbox"]').is(':checked')) {$("#bocVacgridName").data("kendoGrid").removeRow($(item))}})
			if(ckBocVac == false){
				toastr.warning(gettextCatalog.getString("B???n ch??a ch???n b???n ghi n??o ????? x??a !"));
			}
	    }
		vm.handleCheck = function(item){
			if(document.getElementById("checkalllist4").checked == true){
			    document.getElementById("checkalllist4").checked = false;
			}
		}
		// B???ng danh s??ch
			function fillDataTable(data) {
				vm.gridOptions = kendoConfig.getGridOptions({
					autoBind : true,
					dataSource : data,
					change: onChange,
					editable: false,
					noRecords : true,
					messages : {noRecords : gettextCatalog.getString("B???ng kh??ng c?? d??? li???u.")},
					columns : [{
					         field: "rowNumber",
					         title: "STT",
					         headerAttributes: {style:"text-align:center;"},
					         template: dataItem => $("#mainGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
					         attributes:{style:"text-align:center;"},
					         width : 60 },
							{title : "<input type='checkbox' id='checkalllist4' name='gridchkselectall' ng-click='vm.chkSelectAll();' ng-model='vm.chkAll' />",
							 headerAttributes: {style:"text-align:center;"},
							 template : "<input type='checkbox' name='gridcheckbox' ng-click='vm.handleCheck();' />",
							 attributes:{style:"text-align:center;"},
							 width : 20 },
							{title : gettextCatalog.getString("M?? phi???u"),
							 headerAttributes: {style:"text-align:center;"},
							 field : "code",
							 width : 100},
							{title : gettextCatalog.getString("M?? c??ng tr??nh"),
							 headerAttributes: {style:"text-align:center;"},
							 field :"constrtCode",
							 width : 100 },
							{title : gettextCatalog.getString("M?? h???p ?????ng"),
							 headerAttributes: {style:"text-align:center;"},
							 field : "contractCode",
							 width : 150 },
							{title : gettextCatalog.getString("T??n h???p ?????ng"),
							 headerAttributes: {style:"text-align:center;"},
							 field : "contractName",
							 width : 150 },
							{
							title : gettextCatalog.getString("Tr???ng th??i"),
							field : "statusCa",
							template : function (data) {
								if(data.statusCa == 0)
									return "So???n th???o";
								if(data.statusCa == 1)
									return "Tr??nh duy???t";
								if(data.statusCa == 2)
									return "???? duy???t";
								if(data.statusCa == 3)
									return "T??? ch???i duy???t";
								return '';
							},
							width : 100,
							headerAttributes: {style:"text-align:center;"},
							attributes: { style: "text-align:left;", class:"statusColumn"}
						}]
					});
		}
			$("#mainGrid").kendoTooltip({
				filter: "td",
	            content: function (et) {
	              var target = et.target; // element for which the tooltip is shown
	              return $(target).text();
	            }
		    }).data("kendoTooltip");	
			vm.handleCheck1 = function(item){
				if(document.getElementById("checkalllist41").checked == true){
				    document.getElementById("checkalllist41").checked = false;
				}
			}
		// B???ng ?? t??
		function OtoDataTable(data) {
			if (data && data.length > 0 ) { 
				for (var i = 0; i < data.length; i++) {
					switch(data[i].roadType){
						case 1 :  { data[i].roadTypeDetail = { id: "1", value: "Lo???i 1" }; break;}
						case 2 :  { data[i].roadTypeDetail = { id: "2", value: "Lo???i 2" }; break;}
						case 3 :  { data[i].roadTypeDetail = { id: "3", value: "Lo???i 3" }; break;}
						case 4 :  { data[i].roadTypeDetail = { id: "4", value: "Lo???i 4" }; break;}
						case 5 :  { data[i].roadTypeDetail = { id: "5", value: "Lo???i 5" }; break;}
						case 6 :  { data[i].roadTypeDetail = { id: "6", value: "Lo???i Kh??c" }; break;}
						default:  { toastr.warning("L???i lo???i ???????ng");break;}
					}
				}
			}
			
			var dataSource = new kendo.data.DataSource({
                pageSize: 20,
                data: data,
                autoSync: false,
                schema: {
                    model: {
                        id: "OtogridName",
                    	fields: 
                    	{
                    		STT: {editable: false},
                    		quantity:{ validation: { min: 0,  required: { message: "Kh???i l?????ng kh??ng ???????c ????? tr???ng" }} },
                    		transportDistance:{ validation: { min: 0,  required: { message: "C??? ly v???n chuy???n kh??ng ???????c ????? tr???ng" }} },
                    		truckLoad:{ validation: { min: 0,  required: { message: "Tr???ng t???i xe kh??ng ???????c ????? tr???ng" } } },
                    		materialType: { validation: {  required: { message: "Lo???i VT v???n chuy???n kh??ng ???????c ????? tr???ng" } } },
                    		unit: { validation: {  required: { message: "????n v??? t??nh kh??ng ???????c ????? tr???ng" } } },
                    		roadTypeDetail: { defaultValue: { id: 1, value: "Lo???i 1" } },
                    	}
                    }
                }
            });
			vm.Otogrid = kendoConfig.getGridOptions({
				autoBind : true,
				dataSource : dataSource,
				navigatable: true,
				noRecords : true,
				messages : {noRecords : gettextCatalog .getString("B???ng kh??ng c?? d??? li???u.")},
				edit: function(e) {
			         e.container.find("input[name=materialType]").attr("maxlength", 200);
			         e.container.find("input[name=unit]").attr("maxlength", 200);
			         e.container.find("input[name=quantity]").attr("maxlength", 34);
			         e.container.find("input[name=transportDistance]").attr("maxlength", 34);
			         e.container.find("input[name=roadTypeDetail]").attr("maxlength", 15);
			         e.container.find("input[name=truckLoad]").attr("maxlength", 38);
			    },
				columns : [{
						 field: "STT",
						 title: gettextCatalog.getString("STT"),
						 headerAttributes: {style:"text-align:center;"},
						 template: dataItem =>$("#OtogridName").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
						 attributes:{style:"text-align:center;"},
						 width : 70 },
						{title : "<input type='checkbox' name='gridchkselectall' id='checkalllist41' ng-click='vm.chkAllOto();' ng-model='vm.chkAOto' />",
						 headerAttributes: {style:"text-align:center;"},
						 template : "<input type='checkbox' ng-checked=\"vm.chkAOto\" ng-click='vm.handleCheck1();' name='gridcheckbox' />",
						 attributes:{style:"text-align:center;"},
						 width : 60 },
						{title : gettextCatalog.getString("Lo???i VT v???n chuy???n"),
						 headerAttributes: {style:"text-align:center;"},
						 field : "materialType",
						 attributes:{style:"text-align:left;"},
						 width : 200 },
						{title : gettextCatalog.getString("????n v??? t??nh"),
						 headerAttributes: {style:"text-align:center;"},
						 field :"unit",
						 attributes:{style:"text-align:left;"},
						 width : 150 },
						{title : gettextCatalog.getString("Kh???i l?????ng"),
							 headerAttributes: {style:"text-align:center;"},
							 field : "quantity",
							 template: function(dataItem) {
								 if ($.isNumeric(dataItem.quantity) && dataItem.quantity >= 0) {
									 dataItem.quantity = parseFloat(Number(dataItem.quantity).toFixed(3));
		                    	 return parseFloat(Number(dataItem.quantity).toFixed(3));
		                     	 }else
		                     	 {
		                     		 dataItem.quantity = 0;
		                     		 return 0;
		                     	 }
								 },
		                     format: "{0:n3}",
		                     decimals: 3,
		                     
							 attributes:{style:"text-align:right;"},
							 width : 150 },
						{title : gettextCatalog.getString("C??? ly v???n chuy???n (km)"),
						 headerAttributes: {style:"text-align:center;"},
						 field : "transportDistance",
						 template: function(dataItem) {
							 if ($.isNumeric(dataItem.transportDistance) && dataItem.transportDistance >= 0) {
								 dataItem.transportDistance = parseFloat(Number(dataItem.transportDistance).toFixed(3));
	                    	 return parseFloat(Number(dataItem.transportDistance).toFixed(3));
	                     	 }	
							 dataItem.transportDistance = 0;
	                     		return 0;
	                     	 },
	                     format: "{0:n3}",
	                     decimals: 3,
	                     
						 attributes:{style:"text-align:right;"},
						 width : 250 },
						{title: gettextCatalog.getString("Lo???i ???????ng"),
						 headerAttributes: {style:"text-align:center;"},
						 field: "roadTypeDetail",
						 attributes: { style: "padding:0" },
						 editor: categoryDropDownEditor,
						 template: "#=roadTypeDetail.value#",
						 width: 150 },
						{title : gettextCatalog.getString("Tr???ng t???i xe (t???n)"),
						 headerAttributes: {style:"text-align:center;"},
						 field : "truckLoad",
						 template: function(dataItem) {
							 if ($.isNumeric(dataItem.truckLoad) && dataItem.truckLoad >= 0) {
								 dataItem.truckLoad = parseFloat(Number(dataItem.truckLoad).toFixed(3));
	                    	 return parseFloat(Number(dataItem.truckLoad).toFixed(3));
	                     	 }else
							 {
	                     		dataItem.truckLoad  = 0;
							 	return 0;
							 }
	                     	 },
	                     format: "{0:n3}",
	                     decimals: 3,
						 attributes:{style:"text-align:right;"},
						 width : 150 }]
				});
	}
		
		// B???ng C??t K??t
		function cutkitDataTable(data) {
			var dataSource = new kendo.data.DataSource({
                pageSize: 20,
                data: data,
                autoSync: false,        
                schema: {
                    model: {
                        id: "cutKitgridName",
                    	fields: {
                    		STT: {editable: false},
                    		quantity:{ validation: { min: 0,  required: { message: "Kh???i l?????ng kh??ng ???????c ????? tr???ng" }} },
                    		transportDistance:{ validation: { min: 0,  required: { message: "C??? ly v???n chuy???n kh??ng ???????c ????? tr???ng" }} },
                    		roadTypeStr:{ validation: { required: { message: "Lo???i ???????ng kh??ng ???????c ????? tr???ng" } } },
                    		materialType: { validation: { required: { message: "Lo???i VT v???n chuy???n kh??ng ???????c ????? tr???ng" } } },
                    		unit: { validation: { required: { message: "????n v??? t??nh kh??ng ???????c ????? tr???ng" } } },
                    		slopeDegree: { validation: {  required: { message: "????? d???c kh??ng ???????c ????? tr???ng" } } },
                    	}
                    }
                }
            });
			vm.cutKitgrid = kendoConfig.getGridOptions({
				autoBind : true,
				dataSource : dataSource,
				navigatable: true,
				noRecords : true,
				messages : {noRecords : gettextCatalog.getString("B???ng kh??ng c?? d??? li???u.")},
				edit: function(e) {
			         e.container.find("input[name=materialType]").attr("maxlength", 200);
			         e.container.find("input[name=unit]").attr("maxlength", 200);
			         e.container.find("input[name=quantity]").attr("maxlength", 34);
			         e.container.find("input[name=transportDistance]").attr("maxlength", 34);
			         e.container.find("input[name=roadTypeStr]").attr("maxlength", 15);
			         e.container.find("input[name=slopeDegree]").attr("maxlength", 200);
			},
				columns : [{
						 field: "STT",
						 title: gettextCatalog.getString("STT"),
						 headerAttributes: {style:"text-align:center;"},
						 template: dataItem =>$("#cutKitgridName").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
						 attributes:{style:"text-align:center;"},
						 width : 70 },
						{title : "<input type='checkbox' name='gridchkselectall' ng-click='vm.chkAllCut();' ng-model='vm.chkACut' />",
						 headerAttributes: {style:"text-align:center;"},
						 template : "<input type='checkbox' ng-checked=\"vm.chkACut\" name='gridcheckbox' />",
						 attributes:{style:"text-align:center;"},
						 width : 60 },
						{title : gettextCatalog.getString("Lo???i VT v???n chuy???n"),
						 headerAttributes: {style:"text-align:center;"},
						 field : "materialType",
						 attributes:{style:"text-align:left;"},
						 width : 200 },
						{title : gettextCatalog.getString("????n v??? t??nh"),
						 headerAttributes: {style:"text-align:center;"},
						 field :"unit",
						 attributes:{style:"text-align:left;"},
						 width : 150 },
						{title : gettextCatalog.getString("Kh???i l?????ng"),
							 headerAttributes: {style:"text-align:center;"},
							 field : "quantity",
							 template: function(dataItem) {
								 if ($.isNumeric(dataItem.quantity) && dataItem.quantity >= 0 ) {
									 dataItem.quantity = parseFloat(Number(dataItem.quantity).toFixed(3));
									return parseFloat(Number(dataItem.quantity).toFixed(3));
		                     	 }else 
		                     	 {
		                     		dataItem.quantity = 0;
		                     		return 0;
		                     	 }
		                     	 },
		                     format: "{0:n3}",
		                     decimals: 3,
		                     
							 attributes:{style:"text-align:right;"},
							 width : 150 },
						{title : gettextCatalog.getString("C??? ly v???n chuy???n (km)"),
						 headerAttributes: {style:"text-align:center;"},
						 field : "transportDistance",
						 template: function(dataItem) {
							 if ($.isNumeric(dataItem.transportDistance) && dataItem.transportDistance >= 0 ) {
								 dataItem.transportDistance = parseFloat(Number(dataItem.transportDistance).toFixed(3));
								 return parseFloat(Number(dataItem.transportDistance).toFixed(3));
	                     	 }else{dataItem.transportDistance = 0;
	                     		return 0;}
	                     	 },
	                     format: "{0:n3}",
	                     decimals: 3,
						 attributes:{style:"text-align:right;"},
						 width : 250 },
						{title : gettextCatalog.getString("Lo???i ???????ng"),
						 headerAttributes: {style:"text-align:center;"},
						 field : "roadTypeStr",
						 attributes:{style:"text-align:right;"},
						 width : 150 },
						{title : gettextCatalog.getString("????? d???c (?????)"),
						 headerAttributes: {style:"text-align:center;"},
						 attributes:{style:"text-align:right;"},
						 field : "slopeDegree",
						 width : 150}]
				});
	}
		// B???ng B???c V??c
		function bocVacDataTable(data) {
			var dataSource = new kendo.data.DataSource({
                pageSize: 20,
                data: data,
                autoSync: false,
                schema: {
                    model: {
                        id: "bocVacgridName",
                    	fields: {
                    		STT: {editable: false},
                    		quantity:{ validation: { min: 0,  required: { message: "Kh???i l?????ng kh??ng ???????c ????? tr???ng" }} },
                    		materialType: { validation: {  required: { message: "Lo???i VT v???n chuy???n kh??ng ???????c ????? tr???ng" } } },
                    		unit: { validation: {  required: { message: "????n v??? t??nh kh??ng ???????c ????? tr???ng" } } },
                    		note: {},
                    	}}}
            });
			vm.bocVacgrid = kendoConfig.getGridOptions({
				autoBind : true,
				dataSource : dataSource,
				noRecords : true,
				navigatable: true,
				messages : {noRecords : gettextCatalog.getString("B???ng kh??ng c?? d??? li???u.")},
				edit: function(e) {
			         e.container.find("input[name=materialType]").attr("maxlength", 200);
			         e.container.find("input[name=unit]").attr("maxlength", 200);
			         e.container.find("input[name=quantity]").attr("maxlength", 34);
			         e.container.find("input[name=note]").attr("maxlength", 1000);
			},
				columns : [{
						field: "STT",
						 title: gettextCatalog.getString("STT"),
						 headerAttributes: {style:"text-align:center;"},
						 template: dataItem => $("#bocVacgridName").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
						 attributes:{style:"text-align:center;"},
						 width : 70 },
						{title : "<input type='checkbox' name='gridchkselectall' ng-click='vm.chkAllBoc();' ng-model='vm.chkABoc' />",
						 headerAttributes: {style:"text-align:center;"},
						 template : "<input type='checkbox' name='gridcheckbox' ng-checked=\"vm.chkABoc\"/>",
						 attributes:{style:"text-align:center;"},
						 width : 60 },
						{title : gettextCatalog.getString("Lo???i VT v???n chuy???n"),
						 headerAttributes: {style:"text-align:center;"},
						 field : "materialType",
						 attributes:{style:"text-align:left;"},
						 width : 200 },
						{title : gettextCatalog.getString("????n v??? t??nh"),
						 headerAttributes: {style:"text-align:center;"},
						 field :"unit",
						 attributes:{style:"text-align:left;"},
						 width : 150 },
						{title : gettextCatalog.getString("Kh???i l?????ng"),
						 headerAttributes: {style:"text-align:center;"},
						 field : "quantity",
						 template: function(dataItem) {
							 if ($.isNumeric(dataItem.quantity) && dataItem.quantity >= 0 ) {
								 dataItem.quantity = parseFloat(Number(dataItem.quantity).toFixed(3));
								 return parseFloat(Number(dataItem.quantity).toFixed(3));
	                     	 }else{dataItem.quantity = 0;
	                     		return 0;}
	                     	 },
	                     format: "{0:n3}",
	                     decimals: 3,
						 attributes:{style:"text-align:right;"},
						 width : 150 },
						{title : gettextCatalog.getString("Ghi ch??"),
						 headerAttributes: {style:"text-align:center;"},
						 field :"note",
						 attributes:{style:"text-align:left;"},
						 width : 150 }]
				});
	}
		// Hi???n th??? T??m ki???m c??ng vi???c v???n chuy???n ph??t sinh
		vm.showNoiDungPS = function() {
			distanceUnloadedMaterialsList.showNoiDungPS(vm.item.constructId).then(
					function(result) {
					var templateUrl = 'views/popup/gridViewSearchWork.html';
					var title = "T??m ki???m c??ng vi???c v???n chuy???n";
					vm.constructionGroupGridOptions = kendoConfig
							.getGridOptions({
								autoBind : true,
								editable: false,
								dataSource : result.plain(),
								noRecords : true,
								messages : {
									noRecords : gettextCatalog
											.getString("Kh??ng c?? k???t qu??? trong trang")
								},
								columns : [
										{
											title : gettextCatalog.getString("STT"),
											field : "STT",
											template: dataItem => $("#gridView").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
											width : 100,
											headerAttributes: {style:"text-align:center;"},
											attributes:{style:"text-align:center;"}
										},
										{
											title : gettextCatalog
													.getString("M?? h???ng m???c"),
											field : "itemsCode",
											width : 150,
											headerAttributes: {style:"text-align:center;"}
										},
										{
											title : gettextCatalog
													.getString("T??n h???ng m???c"),
											field : "itemName",
											width : 150,
											headerAttributes: {style:"text-align:center;"}
										},
										{
											title : gettextCatalog
													.getString("M?? ?????u vi???c"),
											field : "workItemCode",
											width : 150,
											headerAttributes: {style:"text-align:center;"}
										},
										{
											title : gettextCatalog
													.getString("T??n ?????u vi???c"),
											field : "workItemName",
											width : 200,
											headerAttributes: {style:"text-align:center;"}
										},
										{
											title : gettextCatalog
													.getString("Lo???i c??ng vi???c"),
											field : "type",
											template : function (data) {
												if(data.type === 1)
													return "C??ng vi???c trong h???p ?????ng";
												if(data.type === 2)
													return "C??ng vi???c ph??t sinh ngo??i h???p ?????ng";
											},
											width : 200,
											headerAttributes: {style:"text-align:center;"}
										} ]
							});

						CommonService.populateDataToGrid(templateUrl,
								title, vm.constructionGroupGridOptions,
								vm,
								PopupConst.distanceUnloadedMaterialsList['showNoiDungPS'],false,true);
						},
						function() {
							console.error('Error while fetching locatorbank');
						});

			}
		
		
	// END FUNCTION T???NG
	}
})();