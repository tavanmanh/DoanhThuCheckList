(function() {
	'use strict';
	var controllerId = 'listreportresultController';
	angular.module('MetronicApp').controller(controllerId,listreportresultController);
	/* @ngInject */
	function listreportresultController($scope, $rootScope, $timeout, Constant,
			kendoConfig, gettextCatalog, $kWindow, WindowService,$q,
			list_report_result_services, ProposalEvaluation,theApproval,CommonService) {
		var vm = this;
		vm.showDetail = false;
		vm.addA = addA;
		vm.iscreatNew = false;
		vm.save = save;
		vm.showImport = showImport;
		var getMultiDeleteList = [];
		$rootScope.excel =[];
		vm.showApproval = false;
		vm.checkDisable = false;
		vm.disabledButtom = false;
		vm.disabledImport = false;
		vm.disabledExport = false;
		vm.showImportButton = false;
		vm.showPD = true;
		vm.showTD = true;
		vm.chkSelectAll = chkSelectAll;
		vm.chkSelectAllSmall = chkSelectAllSmall;
		vm.checkApproval = checkApproval;
		vm.changeSite = changeSite;
		vm.item = {
				partnerName : '',
				contractCode : '',
				contractName : '',
				investProjectName : '',
				constrtCode : '',
				constrtName : '',
				constrType : '',
				provinceId : '',
				constrtAddress : '',
				constructId : '',
				signed_date :'',
				constructorName: ''
			}
		vm.itemID = {
				//qualityCableMeaReportId :'',
				aMonitorId :'',
				bConstructId :'',
				isActive:''	,
				code : '',
				createdDate: '',
				createdUserId: ''
		}
		// lay id thong tin chung cua cong trinh
		vm.item = ProposalEvaluation.getItem();
		if(vm.item == null) {
			vm.item = CommonService.getItem();
		}
//		initdata();
		
		//minhpvn begin
//		vm.downloadBieuMau = downloadBieuMau;
//		vm.importBieuMau = importBieuMau;
		vm.cancelImport =	function (){
			$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
		}
		
		 function downloadBieuMau(){
			 reportSceneAriseWeighService.downloadBieuMau(vm.item).then(function () {
				window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + data.fileName;
			}, function (e) {
				toastr.error(gettextCatalog.getString("L???i, kh??ng t???i ???????c bi???u m???u !"));
			});
		}
		 
		 
		 
		//minhpvn end
		 
		vm.showGrid = showGrid;
		vm.add = add;
		vm.exportFile = exportFile;
		vm.exportFileDoc = exportFileDoc;
		vm.remove = remove;
		vm.validatorOptions = kendoConfig.get('validatorOptions');
		vm.objectDTO =[];
		vm.monitoring=[];
		vm.technical=[];
		vm.resultMonitoring = {
				constructId: vm.item.constructId,
				contractCode : vm.item.contractCode
		};
		vm.resultTechnical = {
				constructId: vm.item.constructId,
				contractCode : vm.item.contractCode
		};
		vm.doc ={
				qualityCableMeaReportId :''
		}
		// lay id thong tin chung cua cong trinh
		vm.template = {
				constructId: '',
				amonitorId: '',
				bconstructId: '',
				qualityCableMeaReportId:'',
				isActive: '',
				statusCa: '',
				code: '',
				createdUserId: '',
				createdDate: '',
				signDate: '',
				signPlace: '',
				qualityCableMeaResult: '',
				afullName : '',
				bfullName : ''};
		vm.template.constructId = vm.item.constructId;
		vm.load={};
		// phe duyet/////////
		 vm.approver={ 
		 statusCa : '',
		 employeeId :'',
		 comments:'',
//		 qualityCableMeaReportId:''
		 }
		 loadDataTableA();
//			list_report_result_services.getRoleId().then(function(data) {
//	            vm.role = data;
//	            vm.resultMonitoring.roleid = vm.role[10];
//	            vm.resultTechnical.roleid = vm.role[5];
//	    		initdata();
//	        }, function() {
//				toastr.warning(gettextCatalog.getString("C?? l???i x???y ra trong qu?? tr??nh load d??? li???u!"));
//			});
//		function initdata(){
//			if((vm.monitoring == null || vm.monitoring.length == 0 )){
//				list_report_result_services.getListEmployeeByRole(vm.resultMonitoring ).then(function(data) {
//					if(data.plain().length > 0){
//						vm.monitoring = data.plain();
//					}else {
//						vm.monitoring = [];
//					}
//				});
//			}
//			
//			if((vm.technical == null || vm.technical.length == 0 )){
//				list_report_result_services.getListEmployeeByRole(vm.resultTechnical).then(function(data) {
//					if(data.plain().length > 0){
//						vm.technical = data.plain();
//					}else {
//						vm.technical = [];
//					}
//				});		
//			}
//			
//			}
			vm.role=[];
			var totalListEmployee = [], MonitorId = [], InChargeConstructId = [];
			vm.monitoring = [];
			vm.technical = [];
			angular.element(document).ready(function () {
			list_report_result_services.getListEmployeeByConstruction(vm.item.constructId).then(function(data) {
				totalListEmployee = data.plain();
				list_report_result_services.getRoleId().then(function(data) {
//					vm.role = data;
					for (var i = 0; i < totalListEmployee.length; i++) {
						if(totalListEmployee[i].roleid == Constant.ROLE_ID["employee_roleID_CDT_GSTC"]){//10
							MonitorId.push(totalListEmployee[i]);
						}
						if(totalListEmployee[i].roleid == Constant.ROLE_ID["employee_roleID_DT_KTTC"]){//5
							InChargeConstructId.push(totalListEmployee[i]);
						}
					}
					vm.monitoring = MonitorId;
					vm.technical = InChargeConstructId;
					
					if(MonitorId.length > 0) {
						vm.item.amonitorId = MonitorId[0].userId;
					}
					if(InChargeConstructId.length > 0) {
						vm.item.bconstructId = InChargeConstructId[0].userId;
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
		vm.handleCheck = function(item){
			if(document.getElementById("checkalllist3").checked == true){
			    document.getElementById("checkalllist3").checked = false;
			}
		}
		function fillDataTableA(data) {
			var deferred = $q.defer();
			vm.gridOptionsA = kendoConfig
					.getGridOptions({
						autoBind : true,
						dataSource : data,
						change : onChangeA,
						editable: false,
						noRecords : true,
						messages : {
							noRecords : gettextCatalog
									.getString("Kh??ng c?? k???t qu??? n??o")
						},
						columns : [
								{
						            field: "STT",
						            title: "STT",
						            template: dataItem => $("#mainGridReportRuslt").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
						            headerAttributes: {style:"text-align:center;"},
						            attributes:{style:"text-align:center;"},
						            width : 80
								},
								{
									title : "<input type='checkbox' id='checkalllist3' name='gridchkselectall' ng-click='vm.chkSelectAll();' ng-model='vm.chkAll' />",
									template : "<input type='checkbox' name='gridcheckbox' ng-click='vm.handleCheck();' />",
									headerAttributes: {style:"text-align:center;"},
									attributes:{style:"text-align:center;"},
									width : 50
								},
								{
									title : gettextCatalog.getString("M?? B??o c??o"),
									field : "code",
									headerAttributes: {style:"text-align:center;"},
									attributes:{style:"text-align:left;"},
									width : 200
								},
								{
									title : gettextCatalog.getString("M?? c??ng tr??nh"),
									field : "constrtCode",
									headerAttributes: {style:"text-align:center;"},
									attributes:{style:"text-align:left;"},
									width : 200
								},
								{
									title : gettextCatalog.getString("M?? h???p ?????ng"),
									field : "contractCode",
									headerAttributes: {style:"text-align:center;"},
									attributes:{style:"text-align:left;"},
									width : 200
								},
								{
									title : gettextCatalog.getString("T??n h???p ?????ng"),
									field : "contractName",
									headerAttributes: {style:"text-align:center;"},
									attributes:{style:"text-align:left;"},
									width : 200
								},
								{
									title : gettextCatalog.getString("Tr???ng th??i"),
									field : "statusCa",
									headerAttributes: {style:"text-align:center;"},
									attributes:{style:"text-align:left;", class:"statusColumn"},
									template: function($scope){
									      if($scope.statusCa == 0)
									      {
									       return "So???n Th???o";
									      }
									      if($scope.statusCa == 1)
									      {
									    	  return "Tr??nh duy???t";
									      }
									      if($scope.statusCa == 2)
									      {
									    	  return "???? Duy???t";
									      }
									      if($scope.statusCa == 3)
									      {
									    	  return "T??? ch???i Duy???t";
									      }
									      },
									width : 200
								}]
					});
			deferred.resolve('done');
			return deferred.promise;
		}
// ///////////////LoadData////////////////////////////////////
		function loadDataTableA() {
			vm.load.constructId = vm.item.constructId;
			vm.load.contractId = vm.item.contractId;
			list_report_result_services.findByConstructId(vm.load).then(function(d) {
				if(d.plain().length > 0){
					fillDataTableA(d.plain());
					refreshGridA(d.plain());
				}else{
					fillDataTableA(d.plain());
					refreshGridA(d.plain());
// toastr.warning(gettextCatalog.getString("D??? li???u tr???ng!"));
				}

			}, function() {
				toastr.warning(gettextCatalog.getString("C?? l???i x???y ra trong qu?? tr??nh load d??? li???u!"));
			});
		}
		function refreshGridA(d) {
			var grid = vm.reportGridA;
			if(grid){
				grid.dataSource.data(d);
				grid.refresh();
			}
		}	
		
// /////Chuyen trang////////////////////////
		function changeSite() {
			list_report_result_services.goTo();
		}

// //////////// th??ng tin chung////////////////
		$scope.$on("ProposalEvaluation.detail", function(event, item) {
			if (item != undefined) {
				vm.item = item;	
				vm.template.constructId = vm.item.constructId;
				loadDataTableA();
			} else {
				toastr.warning(gettextCatalog.getString("B???n c???n ch???n m???t b???n ghi tr?????c"));
			}
		});
// //Remove////////////
		function detail() {
			
			if (vm.reportGridA.select().length > 0) {
				vm.showDetail = true;
			} else {
				toastr.warning(gettextCatalog.getString("B???n c???n ch???n m???t b???n ghi tr?????c"));
			}
		}
		
		vm.fakeData=[{
			attenuationAverage:1,
			attenuationDegree:1,
			attenuationLength:1,
			attenuationSum:1,
			length:1,
			note:1,
			objectChecking:1
	}]
		
		vm.theApproval = {
				code:'',
				constructId:'',
				constrCompReMapId: '',
				stringEmployee:'',
				isSign:'',
				path : '',
				nameFile : '',
				roleId : [],
				roleName : []
			}
		
		function onChangeA() {
			if (vm.reportGridA.select().length > 0) {
                var tr = vm.reportGridA.select().closest("tr");
                var dataItem = vm.reportGridA.dataItem(tr);
                vm.itemID = dataItem;
                vm.theApproval.code = vm.itemID.code ;
                vm.theApproval.constructId = vm.item.constructId;
                vm.theApproval.constrCompReMapId = vm.itemID.constrCompReMapId;
                vm.theApproval.stringEmployee = dataItem.aMonitorId  + "," + dataItem.bConstructId ;
                vm.theApproval.roleName = ["Gi??m s??t ch??? ?????u t??","K??? thu???t nh?? th???u thi c??ng "];
                vm.theApproval.roleId = [vm.resultMonitoring.roleid,vm.resultTechnical.roleid];
                vm.theApproval.isSign= 'theApproval';
            }
		}
		
		function chkSelectAll() {
// item
			   var grid = vm.reportGridA;
			   chkSelectAllBase(vm.chkAll, grid);
			  }
		
	    function getDataRowSelect(){
	        var demopromise = $q.defer();
	        var grid = vm.reportGridA;
	            // Check select
	            if (grid.select() == null || grid.select().length == 0) {
	                toastr.warning("C???n ch???n b???n ghi tr?????c khi th???c hi???n!");
	                return;
	            }
	            var tr = grid.select().closest("tr");// output card tr
	            var dataItem = grid.dataItem(tr); // data card tr
	            demopromise.resolve(dataItem);
	            return demopromise.promise;
	        }
		// many remove
	    vm.multiDelete = function() {
	    	   var selectedRow = [];
	    	   var dem = 0;
	    	   var grid = vm.reportGridA;
	    	   grid.table.find("tr").each(function(idx, item) {
	    	    var row = $(item);
	    	    var checkbox = $('[name="gridcheckbox"]', row);

	    	    if (checkbox.is(':checked')) {
	    	     // Push id into selectedRow
	    	     //var tr = grid.select().closest("tr");
	    	     var dataItem = grid.dataItem(item);
	    	     if((dataItem.statusCa == 0 || dataItem.statusCa == 3)
	    	    		 && dataItem.createdUserId == Constant.user.srvUser.userId){
	    	     selectedRow.push(dataItem.qualityCableMeaReportId);
	    	     }
	    	     if((dataItem.statusCa == 0 || dataItem.statusCa == 3)
	    	    		 && dataItem.createdUserId != Constant.user.srvUser.userId){
	    	     dem++;
	    	     }
	    	     if((dataItem.statusCa != 0 && dataItem.statusCa != 3)
	    	    		 && dataItem.createdUserId != Constant.user.srvUser.userId){
	    	     dem++;
	    	     }
	    	    }
	    	   });
	    	   if( dem != 0){
	    		   toastr.warning("B???n kh??ng c?? quy???n x??a!");
		    	    return;
	    	   }
	    	   if (selectedRow.length == 0) {
	    	    toastr.warning("Ph???i t??ch ch???n b???n ghi");
	    	    return;
	    	   }
	    	   if (confirm('X??c nh???n x??a')) { 
	    		   list_report_result_services.deleteReport(selectedRow).then(function() {
	    			     toastr.success("X??a b???n ghi th??nh c??ng");
	    			     loadDataTableA();
	    			    }, function(errResponse) {
	    			            if (errResponse.status == 302){
	    			                toastr.error("B???n ghi ??ang ???????c s??? d???ng");
	    			               }else{
	    			                toastr.error(message.deleteError);
	    			            }
	    			           });
	    			   }
	    	   }
	    vm.handleCheck1 = function(item){
			if(document.getElementById("checkalllist31").checked == true){
			    document.getElementById("checkalllist31").checked = false;
			}
		}
		function fillDataTable(data) {
			var deferred = $q.defer();
			var dataSource = new kendo.data.DataSource({
                pageSize: 20,
                data: data,
                autoSync: false,
                schema: {
                    model: {
                        id: "reportGridDetail",
                    	fields: {
                    		objectChecking: { validation: { required: { message: "Kh??ng b??? tr???ng ?????i t?????ng ki???m tra" } } },
                    		length: {  validation: { min: 1,  required: { message: "Kh??ng b??? tr???ng chi???u d??i(Km)" } } },
                    		attenuationLength: {  validation: { min: 1, required: { message: "Kh??ng b??? tr???ng Chi???u d??i(Km)" } } },
                    		attenuationDegree: {  validation: { min: 1, required: { message: "Kh??ng b??? tr???ng suy hao(dB)" } } },
                    		attenuationSum: {  validation: { min: 1, required: { message: "Kh??ng b??? tr???ng t???ng suy hao(dB) " } } },
                    		attenuationAverage: {  validation: { min: 1, required: { message: "Kh??ng b??? tr???ng suy hao(dB/m) "} } },
                            note: {}
                        }
                    }
                }
            });
			vm.gridOptions = kendoConfig.getGridOptions({
				autoBind : true,
				dataSource : dataSource,
						change : onChange,
						noRecords : true,
						messages : {
							noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? n??o")
						},
						edit: function(e) {
					         e.container.find("input[name=objectChecking]").attr("maxlength", 200);
					         e.container.find("input[name=length]").attr("maxlength", 34);
					         e.container.find("input[name=attenuationLength]").attr("maxlength", 34);
					         e.container.find("input[name=attenuationDegree]").attr("maxlength", 34);
					         e.container.find("input[name=attenuationSum]").attr("maxlength", 34);
					         e.container.find("input[name=attenuationAverage]").attr("maxlength", 34);
					         e.container.find("input[name=note]").attr("maxlength", 500);	         
					},
						columns : [
						{
							field: "STT",
							title: "TT S???i Quang",
							template: dataItem => $("#reportGridDetail").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
							headerAttributes: {style:"text-align:center;"},
							attributes:{style:"text-align:center;"},
							width : 130
						},
						{
							title : "<input type='checkbox' id='checkalllist31' name='gridchkselectall' ng-click='vm.chkSelectAllSmall();' ng-model='vm.chkAllSmall' />",
							template : "<input type='checkbox' name='gridcheckbox' ng-click='vm.handleCheck1();' />",
							headerAttributes: {style:"text-align:center;"},
							attributes:{style:"text-align:center;"},
							width : 80
						},
						{
							title : gettextCatalog.getString("?????i t?????ng ki???m tra"),
							field : "objectChecking",
							headerAttributes: {style:"text-align:center;"},
							attributes:{style:"text-align:left;"},
							width : 170
						},
						{
							title : gettextCatalog.getString("Chi???u d??i(km)"),
							field : "length",
							  template: function(dataItem) {
		                            if ($.isNumeric(dataItem.length)) {
		                            	dataItem.length = parseFloat(Number(dataItem.length).toFixed(3));
		                                return parseFloat(Number(dataItem.length).toFixed(3));
		                            }else{dataItem.length = 0;
		                            return 0;}
		                        },
							format: "{0:n3}",
							decimals: 3,
							headerAttributes: {style:"text-align:center;"},
							attributes:{style:"text-align:right;"},
							width : 130
						},
						{
							title : gettextCatalog.getString("<b>Suy hao m???i n???i c??c ??i???m</b>"),
							headerAttributes: {style:"text-align:center;"},
							columns : [ {
								title : "<b>A-B</b>",
								headerAttributes: {style:"text-align:center;"},
								columns : [
								{
									title : gettextCatalog.getString("Chi???u d??i(km)"),
									headerAttributes: {style:"text-align:center;"},
									attributes:{style:"text-align:right;"},
									field : "attenuationLength",
									template: function(dataItem) {
			                            if ($.isNumeric(dataItem.attenuationLength)) {
			                            	dataItem.attenuationLength = parseFloat(Number(dataItem.attenuationLength).toFixed(3));
			                                return parseFloat(Number(dataItem.attenuationLength).toFixed(3));
			                            }else
			                            {dataItem.attenuationLength = 0;
			                            return 0;}
			                        },
									format: "{0:n3}",
									decimals: 3,
									width : 130
								},
								{
									title : gettextCatalog.getString("Suy hao(dB)"),
									headerAttributes: {style:"text-align:center;"},
									attributes:{style:"text-align:right;"},
									field : "attenuationDegree",
									template: function(dataItem) {
			                            if ($.isNumeric(dataItem.attenuationDegree)) {
			                            	dataItem.attenuationDegree = parseFloat(Number(dataItem.attenuationDegree).toFixed(3));
			                                return parseFloat(Number(dataItem.attenuationDegree).toFixed(3));
			                            }else{
			                            dataItem.attenuationDegree = 0 ;
			                            return 0;}
			                        },
									format: "{0:n3}",
									decimals: 3,
									width : 130
								} ]
							} ]
						},
						{
							title : gettextCatalog
							.getString("T???ng suy hao(dB)"),
							field : "attenuationSum",
							headerAttributes: {style:"text-align:center;"},
							attributes:{style:"text-align:right;"},
							template: function(dataItem) {
	                            if ($.isNumeric(dataItem.attenuationSum)) {
	                            	dataItem.attenuationSum = parseFloat(Number(dataItem.attenuationSum).toFixed(3));
	                                return parseFloat(Number(dataItem.attenuationSum).toFixed(3));
	                            }else{
	                            dataItem.attenuationSum = 0 ;
	                            return 0;}
	                        },
							format: "{0:n3}",
							decimals: 3,
							width : 150
						},
						{
							title : gettextCatalog
							.getString("Suy hao TB(dB/km)"),
							field : "attenuationAverage",
							headerAttributes: {style:"text-align:center;"},
							attributes:{style:"text-align:right;"},
							template: function(dataItem) {
	                            if ($.isNumeric(dataItem.attenuationAverage)) {
	                            	dataItem.attenuationAverage = parseFloat(Number(dataItem.attenuationAverage).toFixed(3));
	                                return parseFloat(Number(dataItem.attenuationAverage).toFixed(3));
	                            }else{
	                            dataItem.attenuationAverage = 0;
	                            return 0;}
	                        },
							format: "{0:n3}",
							decimals: 3,
							width : 160
						},
						{
							title : gettextCatalog.getString("Ghi ch??"),
							headerAttributes: {style:"text-align:center;"},
							field : "note",
							width : 120
						} ]
					});
					deferred.resolve('done');
					return deferred.promise;
			}

		function loadDataTable() {
		if(vm.itemID != null){
			list_report_result_services.getListQualityResualt(vm.itemID.qualityCableMeaReportId).then(function(d) {
						fillDataTable(d.plain());
						refreshGrid(d.plain());
					}, function() {
						toastr.error(gettextCatalog.getString("C?? l???i x???y ra trong qu?? tr??nh load d??? li???u!"));
					});
		} else if(vm.itemID == null){
			fillDataTable([]);
			refreshGrid([]);
		}
}
//	function refreshGrid(d) {
//			var grid = vm.reportGridDetail;
//			if(grid){
//				grid.dataSource.data(d);
//			 grid.refresh();
//	}
//}	
	
	function refreshGrid(d) {
        var grid = $("#reportGridDetail").data("kendoGrid");
        if (grid) {
            grid.dataSource.data(d);
            grid.refresh();
        }else{console.log("S??DADS");}
    }
	function onChange(){
		   if (vm.reportGridDetail.select().length > 0){
		    var tr = vm.reportGridDetail.select().closest("tr");
		       var dataItem = vm.reportGridDetail.dataItem(tr);
		   }
		  }
	// //////////////////////////////////////
	function addA(){
			vm.showDetail = true;
			vm.showApproval = false;
			vm.itemID = null;
			vm.checkDisable = false;
			vm.disabledImport = false;
			vm.showImportButton = true;
			vm.showPD = false;
			vm.showTD = false;
			vm.disabledExport = true;
			$($('input[name="xlfile"]')[0]).val('');
			list_report_result_services.getListEmployeeByConstruction(vm.item.constructId).then(function(data) {
				totalListEmployee = data.plain();
				list_report_result_services.getRoleId().then(function(data) {
//					vm.role = data;
					for (var i = 0; i < totalListEmployee.length; i++) {
						if(totalListEmployee[i].roleid == Constant.ROLE_ID["employee_roleID_CDT_GSTC"]){//10
							MonitorId.push(totalListEmployee[i]);
						}
						if(totalListEmployee[i].roleid == Constant.ROLE_ID["employee_roleID_DT_KTTC"]){//5
							InChargeConstructId.push(totalListEmployee[i]);
						}
					}
					vm.monitoring = MonitorId;
					vm.technical = InChargeConstructId;
					
					if(MonitorId.length > 0) {
						vm.item.amonitorId = MonitorId[0].userId;
					}
					if(InChargeConstructId.length > 0) {
						vm.item.bconstructId = InChargeConstructId[0].userId;
					}
					
				})
				.catch(function() {
					console.error('getRoleId error');
				});
			  })
			  .catch(function() {
			    console.error('Gists error');
			});
			
			for (var i = 0; i < vm.monitoring.length; i++) {
				if(vm.monitoring[i].isCurrent === 1){
					vm.item.amonitorId = vm.monitoring[i].userId;
					break;
				} else {
					vm.item.amonitorId = vm.monitoring[0].userId;
					break;
				}
			}
			for (var j = 0; j < vm.technical.length; j++) {
				if(vm.technical[j].isCurrent === 1){
					vm.item.bconstructId = vm.technical[j].userId;
					break;
				} else {
					vm.item.bconstructId = vm.technical[0].userId;
					break;
				}
			}
//			if(vm.monitoring.length > 0 ){
//				vm.item.amonitorId = vm.monitoring[0].id;
//			}else{
//				initdata();
//				if(vm.monitoring.length > 0 ){
//					vm.item.amonitorId = vm.monitoring[0].id;
//				}else {
//					toastr.error(gettextCatalog.getString("Ki???m tra d??? li???u gi??m s??t ch??? ?????u t??!"));
//				}
//			}
//			if(vm.technical.length > 0){
//				vm.item.bconstructId=vm.technical[0].id;
//			}else{
//				initdata();
//				if(vm.technical.length > 0 ){
//					vm.item.bconstructId = vm.technical[0].id;
//				}else {
//					toastr.error(gettextCatalog.getString("Ki???m tra d??? li???u nh?? th???u thi c??ng!"));
//				}
//				
//			}
			vm.item.signPlace = '';
			vm.item.signDate = '';
			fillDataTable([]);
			refreshGrid([]);
	}
	
	function showGrid(){
		vm.checkDisable = false;
		vm.showPD = true;
		vm.showTD = true;
		vm.disabledExport = false;
		if(vm.showDetail == false){
				  if(vm.reportGridA.select().length > 0){
					  if((vm.itemID.statusCa == 0 || vm.itemID.statusCa == 3)){
						  if(Constant.user.srvUser.userId == vm.itemID.createdUserId){
							  vm.showDetail = true;
							  vm.showApproval = false;
							  vm.checkDisable = false;
							  vm.disabledImport = true;
							  vm.showImportButton = false;
							  vm.item.amonitorId = vm.itemID.aMonitorId;
							  vm.item.bconstructId = vm.itemID.bConstructId;
							  vm.item.signPlace = vm.itemID.signPlace;
							  vm.item.signDate = chanceDateToString(vm.itemID.signDate, 'M_TO_DATE');
							  loadDataTable();
						  }else{
							  if(Constant.user.srvUser.userId != vm.itemID.createdUserId){
								  toastr.warning(gettextCatalog.getString("B???n kh??ng ???????c quy???n s???a!"));
							  }
							  vm.checkDisable = true;
							  vm.showDetail = true;
							  vm.showApproval = false;
							  vm.disabledButtom = true;
							  vm.disabledImport = true;
							  vm.showImportButton = false;
							  vm.item.amonitorId = vm.itemID.aMonitorId;
							  vm.item.bconstructId = vm.itemID.bConstructId;
							  vm.item.signPlace = vm.itemID.signPlace;
							  vm.item.signDate = chanceDateToString(vm.itemID.signDate, 'M_TO_DATE');
							  loadDataTable();
						  }
					  }else{
						  if(Constant.user.srvUser.userId != vm.itemID.createdUserId){
							  toastr.warning(gettextCatalog.getString("B???n kh??ng ???????c quy???n s???a!"));
						  }
						  vm.checkDisable = true;
						  vm.showDetail = true;
						  vm.showApproval = false;
						  vm.disabledButtom = true;
						  vm.disabledImport = true;
						  vm.showImportButton = false;
						  vm.item.amonitorId = vm.itemID.aMonitorId;
						  vm.item.bconstructId = vm.itemID.bConstructId;
						  vm.item.signPlace = vm.itemID.signPlace;
						  vm.item.signDate = chanceDateToString(vm.itemID.signDate, 'M_TO_DATE');
						  loadDataTable();
					  }
					
	                }else{
	                	toastr.error(gettextCatalog.getString("C???n ch???n b???n ghi tr?????c khi th???c hi???n!"));
	                	return;
	                }
		}else{
			vm.showImportValue = false;
			vm.showDetail = false;
			vm.showApproval = false;
			vm.checkDisable = false;
			vm.disabledButtom = false;
		}
	}
	
	function add(){
		var grid = $("#reportGridDetail").data("kendoGrid");
		grid.dataSource.insert( { qualityCableMeaResultId : '',objectChecking: '', length: '',attenuationLength: '',attenuationDegree: '',attenuationSum: '',attenuationAverage: '',note: '',qualityCableMeaReportId : '' } );
	}
	
	function checkValue(data){
		if(data.objectChecking){
			return true;
		}else{
			return false;
		}
	}
	
function remove(){
	var check = false;
   var grid = vm.reportGridDetail;
   getMultiDeleteList = [];
   var items = [];
   $("#reportGridDetail").data("kendoGrid").table.find("tr").each(
		   function(idx, item) {
			   var row = $(item);
			   var dataItem = grid.dataItem(item);
			   
			   $(item).find('input[type="checkbox"]').each(
					   function(idx, item){
						   if ($(item).is(':checked')){
							   getMultiDeleteList.push(dataItem.qualityCableMeaResultId);
							}
					   })
		   			}
   				);
   				
   			$("#reportGridDetail").data("kendoGrid").table.find("tr").each(
   				function(idx, item) {
   				if($(item).find('input[type="checkbox"]').is(':checked')) {
   					$("#reportGridDetail").data("kendoGrid").removeRow($(item))
   					check = true;
   					}else{
   						check = false;
   					}
   				});	
   				if(check == false && getMultiDeleteList.length < 1){
   					toastr.error("Ph???i t??ch ch???n b???n ghi ????? x??a");
   					return;
   				}
   				if(getMultiDeleteList.length > 0 ){
   					for(var i = getMultiDeleteList.length - 1; i >= 0; i--){
   						if(getMultiDeleteList[i] == "" || getMultiDeleteList[i] == null){
   							getMultiDeleteList.splice(i,1);
   						}
   					}	
   				}
   }
// export
function exportFile(){
	if(vm.showDetail == false){
		var selectedRow = [];
		var grid = vm.reportGridA;
		//vm.reportGridA.select("tr:eq(1)");
		grid.table.find("tr").each(function(idx, item) {
			var row = $(item);
			var checkbox = $('[name="gridcheckbox"]', row);
			if (checkbox.is(':checked')) {
				// Push id into selectedRow
				var tr = grid.select().closest("tr");
				var dataItem = grid.dataItem(item);
				selectedRow.push(dataItem.qualityCableMeaReportId);
			}
		});
		if (selectedRow.length == 0 && vm.itemID.qualityCableMeaReportId != null) {
			$('#loading').show();
			vm.doc.qualityCableMeaReportId = vm.itemID.qualityCableMeaReportId;
			vm.doc.contractId = vm.item.contractId;
			list_report_result_services.exportFile(vm.doc).then(function(data) {
			window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + data.fileName;
			toastr.success("export th??nh c??ng");
			}).catch( function(){
				toastr.error(gettextCatalog.getString("L???i khi export!"));
				return;
			}).finally(function(){
				$('#loading').hide();
			});
			
		}else if (selectedRow.length > 0) {
			$('#loading').show();
			list_report_result_services.exportList(selectedRow).then(function(data) {
				window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + data.fileName;
				toastr.success("export danh sach th??nh c??ng");
			}).catch( function(){
				toastr.error(gettextCatalog.getString("L???i khi export!"));
				return;
			}).finally(function(){
				$('#loading').hide();
			});
			
		}else{
			toastr.warning(gettextCatalog.getString("Ch??a t??ch ch???n b???n ghi export!"));
			return;
		}
	}else if(vm.itemID == null && vm.showDetail == true){
		toastr.error(gettextCatalog.getString("kh??ng c?? b???n ghi export!"));
		return;
	}else if(vm.showDetail == true && vm.itemID != null){
		$('#loading').show();
		vm.doc.qualityCableMeaReportId = vm.itemID.qualityCableMeaReportId;
		list_report_result_services.exportFile(vm.doc).then(function(data) {
		window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + data.fileName;
		toastr.success("export th??nh c??ng");
		}).catch( function(){
			toastr.error(gettextCatalog.getString("L???i khi export!"));
			return;
		}).finally(function(){
			$('#loading').hide();
		});
		
	}
}
// export doc
function exportFileDoc(){
	if(vm.showDetail == false){
		var selectedRow = [];
		var grid = vm.reportGridA;
		//vm.reportGridA.select("tr:eq(1)");
		grid.table.find("tr").each(function(idx, item) {
			var row = $(item);
			var checkbox = $('[name="gridcheckbox"]', row);

			if (checkbox.is(':checked')) {
				// Push id into selectedRow
				var tr = grid.select().closest("tr");
				var dataItem = grid.dataItem(item);
				selectedRow.push(dataItem.qualityCableMeaReportId);
			}
		});
		if (selectedRow.length == 0 && vm.itemID.qualityCableMeaReportId != null) {
			$('#loading').show();
			vm.doc.qualityCableMeaReportId = vm.itemID.qualityCableMeaReportId;
			vm.doc.contractId = vm.item.contractId;
			list_report_result_services.exportFileDoc(vm.doc).then(function(data) {
			window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + data.fileName;
			toastr.success("export doc th??nh c??ng");
			}).catch( function(){
				toastr.error(gettextCatalog.getString("L???i khi export!"));
				return;
			}).finally(function(){
				$('#loading').hide();
			});
			
		}else if (selectedRow.length > 0) {
			$('#loading').show();
			selectedRow.push(vm.item.contractId);
			list_report_result_services.exportListDoc(selectedRow).then(function(data) {
				window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + data.fileName;
				toastr.success("ExportDOC danh s??ch th??nh c??ng");
			}).catch( function(){
				toastr.error(gettextCatalog.getString("L???i khi export!"));
				return;
			}).finally(function(){
				$('#loading').hide();
			});
			
		}else{
			toastr.warning(gettextCatalog.getString("Ch??a t??ch ch???n b???n ghi export!"));
			return;
		}
	}else if(vm.itemID == null && vm.showDetail == true){
		toastr.error(gettextCatalog.getString("kh??ng c?? b???n ghi export!"));
		return;
	}else if(vm.showDetail == true && vm.itemID != null){
		$('#loading').show();
		vm.doc.qualityCableMeaReportId = vm.itemID.qualityCableMeaReportId;
		list_report_result_services.exportFileDoc(vm.doc).then(function(data) {
		window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + data.fileName;
		toastr.success("export doc th??nh c??ng");
		}).catch( function(){
			toastr.error(gettextCatalog.getString("L???i khi export!"));
			return;
		}).finally(function(){
			$('#loading').hide();
		});
	}
}

// /////////////insert date, get date//////////////////
function chanceDateToString(stringDate, mode) { // linhnn
	   if (stringDate != undefined && mode == 'M_TO_DATE') {
	    var date = new Date(stringDate);
	    return (date.getDate() + '/' + (date.getMonth()+1) + '/' + date.getFullYear());
	   }
	   if (stringDate != undefined && mode == 'DATE_TO_M') {
	    var arrDate = stringDate.split("/");
	    var date = new Date(arrDate[2], arrDate[1]-1, arrDate[0], 0, 0, 0, 0);
	    return date.getTime();l
	   }
	   if (stringDate != undefined && mode == 'DATEMONTH_TO_M') {  
	    var arrDate = stringDate.split(" ");
	    var DdMMYYYY = arrDate[0].split("/");
	    var HHMmSs = arrDate[1].split(":");
	    var date = new Date(DdMMYYYY[2], DdMMYYYY[1]-1, DdMMYYYY[0], HHMmSs[0], HHMmSs[1], HHMmSs[2], 0);
	    return date.getTime();
	   }
	   if (stringDate != undefined && mode == 'M_TO_DATEMONTH') {
	    var date = new Date(stringDate);
	    return (date.getDate() + '/' + (date.getMonth()+1) + '/' + date.getFullYear() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
	   }
	  }

	function save(){
		vm.template.amonitorId = vm.item.amonitorId;
		vm.template.bconstructId = vm.item.bconstructId;
//		if(getMultiDeleteList.length > 0){
//			var data ;
//			data = vm.reportGrid.dataSource.data();
//				 list_report_result_services.deleteResult(getMultiDeleteList).then(function() {
//				     toastr.success("X??a b???n ghi th??nh c??ng");
//				     loadDataTable();
//				     getMultiDeleteList = [];
//				    }, function(errResponse) {
//				            if (errResponse.status == 302){
//				                toastr.error("B???n ghi ??ang ???????c s??? d???ng");
//				               }else{
//				                toastr.error("X??a L???i");
//				            }
//				           });
//			}
		if(vm.showDetail == false){
			toastr.error(gettextCatalog.getString("Kh??ng c?? b???n ghi!"));
			return;
		}else{
			if(vm.itemID == null && vm.showApproval == false){
				var data = vm.reportGridDetail.dataSource.data();
				if(data.length == 0){
					toastr.error(gettextCatalog.getString("Kh??ng c?? k???t qu??? ??o !"));
					return;
				}else{
					if(vm.item.signPlace == "" || vm.item.signPlace == null ){
						toastr.error(gettextCatalog.getString("Kh??ng ????? tr???ng ?????a ??i???m!"));
						return;
					}
					if(vm.item.signDate == "" || vm.item.signDate == null){
						toastr.error(gettextCatalog.getString("kh??ng ????? tr???ng ng??y k?? bi??n b???n!"));
						return;
					}
					var objectDTOB =[];
					for(var i =0; i<data.length ; i++){
						if(data[i].length == null || data[i].length == "" || data[i].attenuationLength == null || data[i].attenuationLength == "" 
						   || data[i].attenuationSum == null  || data[i].attenuationSum == "" || data[i].attenuationAverage == null || data[i].attenuationAverage == ""  
						   || data[i].attenuationDegree == null || data[i].attenuationDegree == "" || data[i].objectChecking == ""|| data[i].objectChecking == null){
							toastr.error(gettextCatalog.getString("D??? li???u k???t qu??? ??o ??ang b??? tr???ng!"));
							return;
						}else if(data[i].length < 0 || data[i].attenuationLength < 0 || data[i].attenuationLength < 0 
							   || data[i].attenuationSum < 0  || data[i].attenuationAverage < 0 || data[i].attenuationAverage < 0  
								   || data[i].attenuationDegree < 0 ){
							toastr.error(gettextCatalog.getString("D??? li???u k???t qu??? ??o ??ang b??? ??m, ki???m tra l???i d??? li???u!"));
							return;
						}
						else{
							objectDTOB.push(data[i]);
						}
					}
						delete vm.template.isActive;
						delete vm.template.code;
						delete vm.template.afullName;
						delete vm.template.bfullName;
						vm.template.signPlace = vm.item.signPlace;
						vm.template.createdUserId = Constant.user.srvUser.userId;
						vm.template.signDate = chanceDateToString(vm.item.signDate,'DATE_TO_M');
						vm.template.qualityCableMeaResult = objectDTOB;				
						list_report_result_services.addQualityCableMeaReport(vm.template).then(function(){
						toastr.success("th??m th??nh c??ng");
						refreshGrid([]);
						vm.item.amonitorId = vm.monitoring[0].id;
						vm.item.bconstructId=vm.technical[0].id;
						fillDataTable([]);
						loadDataTableA();
						vm.showDetail = false;
						vm.showImportValue = false;
						vm.disabledExport = false;
						vm.showPD = true;
						vm.showTD = true;
					});
				}
			}else if(vm.itemID != null && vm.showApproval == false){
				var data = vm.reportGridDetail.dataSource.data();
				if(vm.item.signPlace == "" || vm.item.signPlace == null ){
					toastr.error(gettextCatalog.getString("Kh??ng ????? tr???ng ?????a ??i???m c???a bi??n b???n b??o c??o!"));
					return;
				}
				if(vm.item.signDate == "" || vm.item.signDate == null){
					toastr.error(gettextCatalog.getString("Kh??ng ????? tr???ng ng??y k?? bi??n b???n!"));
					return;
				}
				var objectDTOB =[];
				for(var i =0; i<data.length ; i++){
					delete data[i].defaultSortField;
					delete data[i].fwmodelId;
					if(data[i].length == null || data[i].length == "" || data[i].attenuationLength == null || data[i].attenuationLength == "" 
						   || data[i].attenuationSum == null  || data[i].attenuationSum == "" || data[i].attenuationAverage == null || data[i].attenuationAverage == ""  
						   || data[i].attenuationDegree == null || data[i].attenuationDegree == "" || data[i].objectChecking == ""|| data[i].objectChecking == null){
							toastr.error(gettextCatalog.getString("D??? li???u k???t qu??? ??o ??ang b??? tr???ng!"));
							return;
						}else if(data[i].length < 0 || data[i].attenuationLength < 0 || data[i].attenuationLength < 0 
							   || data[i].attenuationSum < 0  || data[i].attenuationAverage < 0 || data[i].attenuationAverage < 0  
								   || data[i].attenuationDegree < 0 ){
							toastr.error(gettextCatalog.getString("D??? li???u k???t qu??? ??o ??ang b??? ??m, ki???m tra l???i d??? li???u!"));
							return;
						}else{
							objectDTOB.push(data[i]);
						}
				}
				delete vm.template.afullName;
				delete vm.template.bfullName;
				vm.template.qualityCableMeaResult = objectDTOB;
				if(vm.itemID != null){
				vm.template.qualityCableMeaReportId = vm.itemID.qualityCableMeaReportId;
				vm.template.isActive = vm.itemID.isActive;
				vm.template.code = vm.itemID.code;
				vm.template.createdUserId = vm.itemID.createdUserId;
				vm.template.createdDate = vm.itemID.createdDate;
				if(vm.itemID.statusCa == 3){
					vm.template.statusCa = 0;
				}else{
					vm.template.statusCa = vm.itemID.statusCa;
				}
				vm.template.signPlace = vm.item.signPlace;
				vm.template.signDate = chanceDateToString(vm.item.signDate,'DATE_TO_M');
			}
				if(vm.template.qualityCableMeaResult.length == 0){
					toastr.error(gettextCatalog.getString("D??? li???u k???t qu??? ??o ??ang tr???ng, ki???m tra l???i d??? li???u!"));
					return;
				}
				list_report_result_services.updateQualityCableMeaReport(vm.template).then(function(){
					loadDataTableA();
					loadDataTable();
					toastr.success("C???p nh???t th??nh c??ng");
					vm.showDetail = false;
				});
			}else if(vm.showApproval == true){
					vm.checkDisable = true;
					vm.approver.statusCa = vm.appro.statusCa;
					vm.approver.comments = vm.appro.comments;
					vm.approver.employeeId = Constant.user.srvUser.catEmployeeId;
					vm.approver.qualityCableMeaReportId = vm.itemID.qualityCableMeaReportId;
					vm.approver.constrCompReMapId = vm.itemID.constrCompReMapId;
					list_report_result_services.appro(vm.approver).then(function(x){
						if(x == '1'){
							toastr.warning("B???n ch??a ?????n l?????t duy???t");
							return;
						}
						if(x == '2'){
							toastr.warning("B???n ???? duy???t b???n ghi n??y r???i");
							return;
						}
						if(x == '4'){
							toastr.success("T??? ch???i duy???t th??nh c??ng");
						}
						if(x == '3'){
							toastr.success("Duy???t th??nh c??ng");
						}
						if(x == '5'){
							toastr.warning("B???n kh??ng ???????c ph??p ph?? duy???t");
							return;
						}
						vm.appro.statusCa = "";
						vm.appro.comments = "";
						loadDataTableA();
						vm.showApproval = false;
						vm.showDetail = false;
						vm.showTD = true;
					}, function(){
						toastr.error(gettextCatalog.getString("L???i khi ph?? duy???t!"));
						return;
					});				
				}
		}
		if(getMultiDeleteList.length > 0){
			var data ;
			data = vm.reportGrid.dataSource.data();
				 list_report_result_services.deleteResult(getMultiDeleteList).then(function() {
				     toastr.success("X??a b???n ghi th??nh c??ng");
				     loadDataTable();
				     getMultiDeleteList = [];
				    }, function(errResponse) {
				            if (errResponse.status == 302){
				                toastr.error("B???n ghi ??ang ???????c s??? d???ng");
				               }else{
				                toastr.error("X??a L???i");
				            }
				           });
			}
	}
	// //check all///////////
	function chkSelectAllSmall(item) {
		   var grid = vm.reportGrid;
		   chkSelectAllBase(vm.chkAllSmall, grid);
		  }
	
	// ///////trinh duyet
	vm.signAppoval = function signAppoval(){
		vm.doc.qualityCableMeaReportId = vm.itemID.qualityCableMeaReportId;
		if(vm.doc != null && vm.doc.qualityCableMeaReportId != null && vm.doc.qualityCableMeaReportId != '') {
			if(vm.itemID.statusCa == 0){
				$('#loading').show();
				list_report_result_services.exportFile(vm.doc).then(function(data) {
					vm.theApproval.path= data.fileName;
					vm.theApproval.nameFile='BM-TCNT-26.pdf';
					theApproval.setItem(vm.theApproval);
					goTo('THE_APPROVAL');
				}).catch( function(){
					toastr.error(gettextCatalog.getString("L???i khi export!"));
					return;
				}).finally(function(){
					$('#loading').hide();
				});
				
			}else{
				toastr.warning(gettextCatalog.getString("Ch??? tr???ng th??i so???n th???o m???i ???????c tr??nh duy???t!"));
				return;
			}
		}else {
			toastr.warning(gettextCatalog.getString("Ch??a ch???n b???n ghi n??o!"));
			return;
		}
     }
	vm.appro={};
	// //phe Duyet
	function checkApproval(){
		var grid = vm.reportGridA;
		// Check select
    	if (grid.select() == null || grid.select().length == 0 ) {
    		toastr.warning("C???n ch???n b???n ghi tr?????c khi th???c hi???n!");
    		return;
    	}
		if(vm.itemID.statusCa == 1){
			if(vm.showDetail == false){
				vm.showTD = false;
				vm.showDetail = true;
				vm.showApproval = true;
				vm.checkDisable = true;
				vm.showImportButton = false;
				vm.appro.statusCa = 2;
				vm.item.amonitorId = vm.itemID.aMonitorId;
				vm.item.bconstructId = vm.itemID.bConstructId;
				vm.item.signPlace = vm.itemID.signPlace;
				vm.item.signDate = chanceDateToString(vm.itemID.signDate, 'M_TO_DATE');
				loadDataTable();
			}else{
// vm.showApproval = false;
				vm.showTD = false;
// vm.showDetail = true;
				vm.showApproval = true;
				vm.checkDisable = true;
				vm.showImportButton = false;
				vm.disabledButtom = false;
				vm.appro.statusCa = 2;
				vm.item.amonitorId = vm.itemID.aMonitorId;
				vm.item.bconstructId = vm.itemID.bConstructId;
				vm.item.signPlace = vm.itemID.signPlace;
				vm.item.signDate = chanceDateToString(vm.itemID.signDate, 'M_TO_DATE');
				loadDataTable();
			}
		} else {
			toastr.warning("Ch??? tr???ng th??i tr??nh duy???t m???i ???????c ph?? duy???t !");
		}
	}
	// ngoccx
    // huy trinh duyet
		vm.cancelApprovalBtn= function(){
			var grid = vm.reportGridA;
			if (grid.select() == null || grid.select().length == 0) {
				toastr.warning("C???n ch???n b???n ghi tr?????c khi th???c hi???n thao t??c n??y!");
				return;
			}
			vm.itemID.tableName = 'QUALITY_CABLE_MEA_REPORT';
			vm.itemID.tableId = vm.itemID.qualityCableMeaReportId;
			vm.itemID.tableIdField = 'QUALITY_CABLE_MEA_REPORT_ID';
			if(vm.itemID.statusCa == 1){
				if(vm.itemID.createdUserId != Constant.user.srvUser.userId){
					toastr.warning(gettextCatalog.getString("Ng?????i t???o m???i c?? quy???n h???y tr??nh duy???t!"));
					return;
				}else{
					if(confirm('X??c nh???n h???y tr??nh duy???t')){
					list_report_result_services.cancelAprroval(vm.itemID).then(function() {
						status = true;
						$rootScope.$broadcast("ChangeStatusHuyDuyet", status);
					loadDataTableA();
				toastr.success(gettextCatalog.getString("???? h???y tr??nh duy???t !"));
			}, function(){
			toastr.error(gettextCatalog.getString("L???i khi h???y tr??nh duy???t!"));
			return;
		});}}
			}else{
				toastr.warning("Ch??? tr??nh duy???t m???i ???????c h???y tr??nh duy???t");
			}
		}   
	function goTo(menuKey) {
	    var hasPerm = true;
	    if (hasPerm) {
	     var template = Constant.getTemplateUrl(menuKey);
	     postal.publish({
	      channel: "Tab",
	      topic: "open",
	      data: template});
	    }
	}
	
	 $scope.$on("ChangeStatusApproval", function(event, result){ 
	      if(result){ loadDataTableA(); 
	      vm.showDetail = false;}
	     });
	// /import excel
	 vm.Import = function () {
			var fileInput = $('#xlf');
			var filePart = fileInput.val();
            var extraNameFile = filePart.split("/(\\|\/)/g").pop().split(".").pop();
            if (extraNameFile !== "xlsx" && extraNameFile !== "xls") {
                toastr.warning("?????nh d???ng file import kh??ng h???p l???");
                return;
            }
			var input = fileInput.get(0);			
// var textFile = input.files[0];
			var reader = new FileReader();
			if (input.files.length) {
				var textFile = input.files[0];
				reader.readAsBinaryString(textFile);
				reader.onload = function (e) {
					var data = e.target.result;
					try{
					/* Call XLSX */
					var workbook = XLSX.read(data, { type: "binary" });
					/* DO SOMETHING WITH workbook HERE */
					var first_sheet_name = workbook.SheetNames[0];
					/* Get worksheet */
					var worksheet = workbook.Sheets[first_sheet_name];
					var array = XLSX.utils.sheet_to_json(worksheet, { raw: true });
					 }
			        catch(err){
			        	toastr.error("File kh??ng ????ng ?????nh d???ng!");
			        }
					var worklistexcell = [];
// var check = false;
					var dem = 0;
						for (var i = 0; i < array.length; i++) {
							if(typeof array[i].length =="number" && typeof array[i].attenuationLength =="number" 
								&& typeof array[i].attenuationDegree =="number" && typeof array[i].attenuationSum =="number"
								&& typeof array[i].attenuationAverage =="number"){
								worklistexcell.push(array[i]);
							}else{
								array[i].length = '';array[i].attenuationLength = '';
								array[i].attenuationDegree = '';array[i].attenuationSum = '';
								array[i].attenuationAverage = '';
								worklistexcell.push(array[i]);
								dem++;
							}
						}
						if(dem > 3){
							toastr.warning(gettextCatalog.getString("Ki???m tra l???i d??? li???u trong file excel"));
						}else{
							worklistexcell.splice(0,1);
							worklistexcell.splice(0,1);
							worklistexcell.splice(0,1);
//							$rootScope.excel=worklistexcell;
							$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
							//refreshGrid(worklistexcell);
							refreshGrid(worklistexcell);
							toastr.success("Import th??nh c??ng");
						}
					
				};
			} else {
				toastr.warning('H??y t???i l??n m???t t???p tin tr?????c khi ti???p t???c');
			};
		}	 	
	 
		  //vm.showImport = showImport;
		  vm.showImportValue = false;
		  function showImport() {
//			   vm.showImportValue = !vm.showImportValue;
			  WindowService.open({
	                options: {
	                    modal: true,
	                    title: "Import",
	                    visible: false,
	                    width: '650',
	                    height: '200',
	                    actions: ["Minimize", "Maximize", "Close"],
	                    open: function() {
	                        this.wrapper.children('.k-window-content').addClass("fix-footer");
	                    }
	                },
	                templateUrl: "qlhc/reportresult/reportResultImportTemplate.html"

	            });
			
		
		  }
		  
		  vm.exportExcel = exportExcel;
		  function exportExcel() {
			  list_report_result_services.exportExcel().then(function(data) {
					window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + data.fileName;
					toastr.success("export excel th??nh c??ng");
				}, function(){
				toastr.error(gettextCatalog.getString("L???i khi export!"));
				return;
			});

		  }
	}
})();