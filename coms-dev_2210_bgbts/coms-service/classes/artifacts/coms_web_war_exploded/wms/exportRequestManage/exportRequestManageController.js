// controller

(function() {
	'use strict';
	var controllerId = 'exportRequestManageController';
	
	angular.module('MetronicApp').controller(controllerId, exportRequestManageController);
	
	function exportRequestManageController($scope, $rootScope, $timeout, gettextCatalog, 
			kendoConfig, $kWindow,
			CommonService, PopupConst, Restangular, RestEndpoint,Constant, exReqManaService,
			createExportRequestManageService
			) {
		var vm = this;
		
		vm.validatorOptions = kendoConfig.get('validatorOptions');
		vm.stockList = {};vm.dupOrder = {};
		vm.bt1 = false; vm.bt2 = true; vm.bt3 = true; vm.bt4 = false;
		vm.businessTypes = [];
		vm.userSearch  = {};
		vm.order={}; 
		vm.errMessage2 = '';
		vm.orderGoodsList = [];
		vm.orderGoodsSerialList = [];
		vm.isUpdate = false;
		vm.dataReasonDelete = [];

		vm.reason1 = {
					status : '1',
					apply : '2'
			}
			exReqManaService.getReasonForComboBox(vm.reason1).then(function(d){
				vm.dataReasonDelete =  d.plain();
			});
		vm.orderPattern={
				status:'1'
		};
		vm.orderPatternGood={};
		
				//OnkeyDown 
	$(document).on("keydown", function (e) {
        if (e.keyCode == 13) {
        	$("#findExt").click();
        }
        });
		$(document).on("click", ".k-overlay", function () {
			// $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
			CommonService.closePopup1();
			});
					
		setTimeout(function(){
			  $("#extCode").focus();
			},15);
			
		setTimeout(function(){
			  $("#orderExportStock").focus();
			},15);
			
			vm.close = function(){
				CommonService.closePopup1();
			}
		 vm.cancelOrder = {};
		vm.orderSearch ={ 
		createdBy:Constant.user.vpsUserToken.sysUserId, 
		createdByName:Constant.user.vpsUserToken.fullName,
		createdDeptedId:Constant.user.VpsUserInfo.departmentId,
		createdDeptedName:Constant.user.VpsUserInfo.departmentName
		};
		vm.orderSearch.listBussinessType = [];
		vm.orderSearch.listStockId = [];
		vm.orderSearch.departmentpopUp ={};
//		vm.orderSearch.listStatus = {};
		vm.orderGoodsSearch ={};
		vm.orderGoodsDetailSearch ={};
		vm.showSearch = true;
		vm.showTabOne = true;
		vm.showAdvancedSearch = false;
		
		//vm.orderSearch.createdBy=Constant.user.vpsUserToken.sysUserId; 
		//vm.orderSearch.createdByName=Constant.user.vpsUserToken.fullName;
		   vm.template='<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;float:left">#: data.code #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
			vm.headerTemplate='<div class="dropdown-header row text-center k-widget k-header">' +
	      '<p class="col-md-6 text-header-auto border-right-ccc">M?? kho</p>' +
	      '<p class="col-md-6 text-header-auto">T??n kho</p>' +
	      	'</div>';
		vm.userTemplate='<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;float:left">#: data.employeeCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
		vm.userHeaderTemplate='<div class="dropdown-header row text-center k-widget k-header">' +
      '<p class="col-md-6 text-header-auto border-right-ccc">M?? nh??n vi??n</p>' +
      '<p class="col-md-6 text-header-auto">T??n nh??n vi??n</p>' +
      	'</div>';
		vm.commonSearch = {name: '', code: ''};
		
		vm.gridCreator = [  {
			title: "T??n ????ng nh???p",
			field: "loginName",
			width: 120
		},{
			title: "M?? nh??n vi??n",
			field: "employeeCode",
			width: 120
		}, {
			title: "H??? t??n",
			field: "fullName",
			width: 120
		}, {
			title: "Email",
			field: "email",
			width: 120
		}, {
			title: "S??T",
			field: "phoneNumber",
			width: 120
		}];
		
		vm.gridCommon = [ {
			title: "M?? kho",
			field: "code",
			width: 120
		}, {
			title: "T??n kho",
			field: "name",
			width: 120
		}];

		vm.orderSearch.listStatus = ['1'];
		$scope.$on("backFromCreate", function(event, result){ 
		      /*if(result){
		    	  vm.orderSearch.page = result.page;
					vm.orderSearch.pageSize = result.pageSize;
					var grid = $("#exRegManaGrid").data("kendoGrid");	
					if(grid){
						grid.dataSource.query({
							page: result.page,
							pageSize: result.pageSize
						});
					}
		      }*/
			  vm.doSearch();
		     });		 
		$scope.$on("backUpDate", function(event, result){ 
		      if(result){
		    	  vm.orderSearch.page = result.page;
					vm.orderSearch.pageSize = result.pageSize;
					// vm.isUpdate = true;
			var grid = $("#exRegManaGrid").data("kendoGrid");	
			if(grid){
				grid.dataSource.query({
					page: vm.orderSearch.page,
					pageSize: vm.orderSearch.pageSize
				});
			}
		      }
			  // refreshGrid();
		     });
			 
			 
			$scope.$on("backFromCancelUpdate", function(event, result){ 
		      if(result){
		    	  vm.orderSearch.page = result.page;
					vm.orderSearch.pageSize = result.pageSize;
					// vm.isUpdate = true;
			var grid = $("#exRegManaGrid").data("kendoGrid");	
			if(grid){
				grid.dataSource.query({
					page: vm.orderSearch.page,
					pageSize: vm.orderSearch.pageSize
				});
			}
		      }
			  // refreshGrid();
		     });
			 
			 $scope.$on("backFromCancelCopy", function(event, result){ 
		      if(result){
		    	  vm.orderSearch.page = result.page;
					vm.orderSearch.pageSize = result.pageSize;
					// vm.isUpdate = true;
			var grid = $("#exRegManaGrid").data("kendoGrid");	
			if(grid){
				grid.dataSource.query({
					page: vm.orderSearch.page,
					pageSize: vm.orderSearch.pageSize
				});
			}
		      }
			  // refreshGrid();
		     });
			 
			 if(exReqManaService.getData()){
			vm.result = exReqManaService.getData();
			var grid = $("#exRegManaGrid").data("kendoGrid");	
			if(grid){
				grid.dataSource.query({
					page: vm.result.page,
					pageSize: vm.result.pageSize
				});
			}
		}
		
		//datetime
/* 		var d = new Date();
		var datestring = d.getDate()  + "/" + d.getMonth() + "/" + d.getFullYear() ;
		vm.orderSearch.createdDateFrom = datestring;
		vm.orderSearch.createdDateTo = null; */
	vm.orderSearch.createdDateFrom=kendo.toString(new Date((new Date()).getTime()-30*24*3600*1000),"dd/MM/yyyy")
		
		initFormData();
		function initFormData() {
			fillDataTableGoodsDetailEditable([]);
			fillDataTableGoodsListForEditable([]);
			fillListGoods([]);
			fillListPatternGoods([]);
			//fillDetailTable([]);
			erroTable([]);
			if($rootScope.stringtile){
				vm.String=$rootScope.stringtile;
				}
		}
		
		//autocomplete
		/** 1. Import notes* */
		vm.importNotecheck=false;
		vm.ImportNoteOptions ={
				   dataTextField: "code", 
			            pageSize: 10,
			            select: function(e) {
						vm.importNotecheck = true;
			                var dataItem = this.dataItem(e.item.index());
			                vm.dupOrder.ieStockTransId = dataItem.stockTransId;
			                vm.dupOrder.stockTransCode = dataItem.code;
			            },
						open: function(e) {
	                        vm.importNotecheck = false;
	                    },  
						dataSource: {
			                serverFiltering: true,
			                transport: {
			                    read: function(options) {
								vm.importNotecheck=false;
			                    	createExportRequestManageService.doSearchImportNote({code: vm.dupOrder.stockTransCode}).then(function(response){ 
			                    			options.success(response.data);
			                        }).catch(function (err) {
			                            console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
			                        });
			                    },
			                }
			            },
			            template:'<div class="row" ><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.code #</div> </div>',
			        change: function(e) {
                    if (e.sender.value() === '') {
                        vvm.dupOrder.ieStockTransId = null;
                        vm.dupOrder.stockTransCode = null;
                    }
                },
			            ignoreCase: false
				
		};
		
		/*2.Contracts**/
	vm.selectedContract=false;
		vm.contractOptions = {
				 select: function(e) {
				 vm.selectedContract=true;
		                var dataItem = this.dataItem(e.item.index());
		                vm.dupOrder.contractCode = dataItem.contractCode
		            },
				dataTextField: "contractCode", 
				open: function(e) {
	                        vm.selectedContract = false;
	                    },  
	            dataSource: {
	                serverFiltering: true,
	                	transport: {
	                    	read: function(options) {
							vm.selectedContract = false;
	                    		createExportRequestManageService.getContracts({contractCode:vm.dupOrder.contractCode}).then(function(response){ 
		                    		options.success(response);
		                        }).catch(function (err) {
		                            console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
		                        });
		                    }
	                    }
	            }, change: function(e) {
  	                if (e.sender.value() === '') {
  	                	vm.dupOrder.contractCode = null;
 	                }
 	            },
	        };
		
		
		/*3. Construction**/
		vm.selectedconstrCode=false;
	    vm.extForConstructCodeOptions = {
			            dataTextField: "code", 
			            select: function(e) {
			                var dataItem = this.dataItem(e.item.index());
			                vm.dupOrder.constrCode = dataItem.code;
							vm.selectedconstrCode=true;
			            },
			            pageSize: 10,
						open: function(e) {
	                        vm.selectedconstrCode = false;
	                    },
			            dataSource: {
			                serverFiltering: true,
			                transport: {
			                    read: function(options) {
								vm.selectedconstrCode=false;
			                    	createExportRequestManageService.getConstructionForAutoComplete({code:vm.dupOrder.constrCode}).then(function(response){ 
			                    		options.success(response);
			                        }).catch(function (err) {
			                            console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
			                        });
			                    },
			                }
			            },
			            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
			            '<p class="col-md-6 text-header-auto border-right-ccc">M??</p>' +
			            '<p class="col-md-6 text-header-auto">T??n</p>' +
			            	'</div>',
			            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;float:left">#: data.code #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
			            change: function(e) {
		  	                if (e.sender.value() === '') {
		  	                	vm.dupOrder.constrCode = null;
		  	                }
		  	            },
			            ignoreCase: false
		}
		
		/*4. Partners**/
		vm.selectedpartnerName = false;
		vm.extForPartnerCodeOptions = {
			            dataTextField: "code", // 
			            select: function(e) {
			                var dataItem = this.dataItem(e.item.index());
			                vm.dupOrder.partnerId = dataItem.partnerId;
			                vm.dupOrder.partnerName = dataItem.partnerName;
							vm.selectedpartnerName = true;
			                 },
			            pageSize: 10,
					open: function(e) {
	                        vm.selectedpartnerName = false;
	                    },	
		            dataSource: {
			                serverFiltering: true,
			                transport: {
			                    read: function(options) {
								vm.selectedpartnerName = false;
			                    	createExportRequestManageService.getPartnerForAutoComplete({code:vm.dupOrder.partnerName}).then(function(response){ 
			                    		options.success(response);
			                        }).catch(function (err) {
			                            console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
			                        });
			                    },
			                }
			            },
			            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
			            '<p class="col-md-6 text-header-auto border-right-ccc">M??</p>' +
			            '<p class="col-md-6 text-header-auto">T??n</p>' +
			            	'</div>',
			            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;float:left">#: data.code #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
			            change: function(e) {
		  	                if (e.sender.value() === '') {
		  	                	 vm.dupOrder.partnerName = null;
		  	                }
		  	            },
			            ignoreCase: false
		}
	    
	    /*5. Projects**/
		vm.selectedPrpject=false;
	    vm.extForProjCodeOptions = {
	            dataTextField: "projectCode", 
	            pageSize: 10,
	            select: function(e) {
				vm.selectedPrpject = true;
	                var dataItem = this.dataItem(e.item.index());
	                vm.dupOrder.projectCode = dataItem.projectCode
	               },
				  open: function(e) {
	                        vm.selectedPrpject = false;
	                    },  
            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function(options) {
						 vm.selectedPrpject = false;
	                    	createExportRequestManageService.getProjects({projectCode:vm.dupOrder.projectCode}).then(function(response){ 
	                    		options.success(response);
	                        }).catch(function (err) {
	                            console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
	                        });
	                    },
	                }
	            },
	            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
	            '<p class="col-md-6 text-header-auto border-right-ccc">M??</p>' +
	            '<p class="col-md-6 text-header-auto">T??n</p>' +
	            	'</div>',
	            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;float:left">#: data.projectCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
	            change: function(e) {
  	                if (e.sender.value() === '') {
					 vm.selectedPrpject = false;
  	                	 vm.dupOrder.projectCode = null;
  	                }
  	            },
	            ignoreCase: false
}
	    
	    /**8. Departments**/
	    // 8.1 For searching (CREATED DEPARTMENT NAME)
		vm.selectedDept = false;
	    vm.deprtOptions = {
  	            dataTextField: "text",
				dataValueField:"id",
  	            select: function(e) {
				vm.selectedDept = true;
  	                var dataItem = this.dataItem(e.item.index());
  	                	vm.orderSearch.createdDeptedName = dataItem.text;
  	     	            vm.orderSearch.createdDeptedId = dataItem.id;
  	            },
  	            pageSize: 10,
				open: function(e) {
	                        vm.selectedDept = false;
	                    }, 
  	            dataSource: {
  	                serverFiltering: true,
  	                transport: {
  	                    read: function(options) {
						vm.selectedDept = false;
  	                        return Restangular.all("departmentServiceRest/department/" + 'getForAutocompleteDept').post({name:vm.orderSearch.createdDeptedName,pageSize:vm.deprtOptions.pageSize}).then(function(response){    
  	                        	options.success(response);
  	                        }).catch(function (err) {
  	                            console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
  	                        });
  	                    }
  	                }
  	            },
  	            template:'<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
  	            change: function(e) {
  	                if (e.sender.value() === '') {
  	                	vm.orderSearch.createdDeptedName = null;
  	     	            vm.orderSearch.createdDeptedId = null;
  	                }
  	            },
  	            ignoreCase: false
  	        };
	    // 8.2 For copying
		vm.selectedDept1=false;
	    vm.deprtOptions1 = {
  	           dataTextField: "text",
				dataValueField:"id",
  	            select: function(e) {
				vm.selectedDept1=true;
  	                var dataItem = this.dataItem(e.item.index());
	                	vm.dupOrder.deptReceiveName = dataItem.text;;
	     	            vm.dupOrder.deptReceiveId = dataItem.id;
  	            },
  	            pageSize: 10,
				open: function(e) {
	                        vm.selectedDept1 = false;
	                    },  
  	            dataSource: {
  	                serverFiltering: true,
  	                transport: {
  	                    read: function(options) {
						vm.selectedDept1=false;
  	                        return Restangular.all("departmentServiceRest/department/" + 'getForAutocompleteDept').post({name:vm.dupOrder.deptReceiveName,pageSize:vm.deprtOptions1.pageSize}).then(function(response){    
  	                        	options.success(response);
  	                        }).catch(function (err) {
  	                            console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
  	                        });
  	                    }
  	                }
  	            },
  	            template:'<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
  	            change: function(e) {
  	                if (e.sender.value() === '') {
  	  	                	vm.dupOrder.deptReceiveName = null;//th??nh name
  	  	     	            vm.dupOrder.deptReceiveId = null;
  	  	                }
  	            },
  	            ignoreCase: false
  	        };
	    //8.2 For searching (RECIEVED DEPARTMENT NAME)
		vm.selectedDept2=false;
	    vm.deprtOptions2 = {
  	             dataTextField: "text",
				dataValueField:"id",
  	            select: function(e) {
				vm.selectedDept2=true;
  	                var dataItem = this.dataItem(e.item.index());
  	                	vm.orderSearch.deptReceiveName = dataItem.text;
  	     	            vm.orderSearch.deptReceiveId = dataItem.id;
  	            },
  	            pageSize: 10,
				open: function(e) {
	                        vm.selectedDept2 = false;
	                    },  
  	            dataSource: {
  	                serverFiltering: true,
  	                transport: {
  	                    read: function(options) {
						vm.selectedDept2=false;
  	                        return Restangular.all("departmentServiceRest/department/" + 'getForAutocompleteDept').post({name:vm.orderSearch.deptReceiveName,pageSize:vm.deprtOptions2.pageSize}).then(function(response){    
  	                        	options.success(response);
  	                        }).catch(function (err) {
  	                            console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
  	                        });
  	                    }
  	                }
  	            },
  	            template:'<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
  	            change: function(e) {
  	                if (e.sender.value() === '') {
  	                	vm.orderSearch.deptReceiveName = null;
  	     	            vm.orderSearch.deptReceiveId = null;
  	                }
  	            },
  	            ignoreCase: false
  	        };
		
	    //clear data
			vm.changeDataAuto=changeDataAuto
		function changeDataAuto(id){
		
		switch(id){
			case 'extForProjectCode':{
			if(processSearch(id,vm.selectedPrpject)){
				vm.dupOrder.projectCode = null;
			  vm.selectedPrpject=false;	
			 }
					break;
					}
				
			case 'exportCopyContract':{
			if(processSearch(id,vm.selectedContract)){
		
	               vm.dupOrder.contractCode = null;
				   vm.selectedContract=false;
				}
					break;
					}
				
				
			case 'id="exportCopyDept"':{
			if(processSearch(id,vm.selectedDept1)){	
					vm.dupOrder.deptReceiveName = null;//th??nh name
					vm.dupOrder.deptReceiveId = null;
					 vm.selectedDept1=false;
			 }
					break;
					}
					
			case 'exportcreatedDeptedName':{
			if(processSearch(id,vm.selectedDept)){	
					vm.orderSearch.createdDeptedName = null;
					 vm.orderSearch.createdDeptedId = null;
					 vm.selectedDept=false;
			 }
					break;
					}
			case 'exportdeptReceiveName':{
			if(processSearch(id,vm.selectedDept2)){	
					vm.orderSearch.deptReceiveName = null;
  	     	            vm.orderSearch.deptReceiveId = null;
					 vm.selectedDept2=false;
			 }
					break;
					}	

			case 'exportconstrCode':{
			if(processSearch(id,vm.selectedconstrCode)){	
					vm.dupOrder.constrCode = null;
					 vm.selectedconstrCode=false;
			 }
					break;
					}
			case 'extForGiftCode':{
			if(processSearch(id,vm.selectedpartnerName)){	
					vm.dupOrder.partnerName = null;
					 vm.selectedpartnerName=false;
			 }
					break;
					}
			case 'importNote':{
			if(processSearch(id,vm.importNotecheck)){	
					vm.dupOrder.ieStockTransId = null;
			        vm.dupOrder.stockTransCode = null;
					vm.importNotecheck=false;
			 }
					break;
					}			
					
						}
		
			
		}
		
	    
	    // var reason = {};vm.dataReasonDelete = [];
	    // exReqManaService.getReasonForComboBox(reason).then(function(d){
			// vm.dataReasonDelete =  d.plain();
		// });
	    
		function erroTable(data) {
			vm.errGridOptions = kendoConfig.getGridOptions({
				autoBind: true,
				resizable: true,			 
				dataSource:data,
				noRecords: true,
				columnMenu: false,
				messages: {
					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
				},
				pageable: {
					refresh: false,
					 pageSizes: [10, 15, 20, 25],
					 pageSize : 10,
					messages: {
		                display: "{0}-{1} c???a {2} k???t qu???",
		                itemsPerPage: "k???t qu???/trang",
		                empty: "Kh??ng c?? k???t qu??? hi???n th???"
		            }
				},
				columns: [{
					title: "TT",
					field:"stt",
			        template: dataItem => $("#detailErrGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}
				,  {
					title: "D??ng l???i",
					field: 'lineError',
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				},  {
					title: "C???t l???i ",
					field: 'columnError',
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				},{
					title: "N???i dung l???i ",
					field: 'detailError',
			        width: "55%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}
				
				]
			});
		}
		
		vm.doSearch= doSearch;
		function doSearch(){
					
			if(!vm.validator2.validate()){
				$("#exReqManaCreateFromDate").focus();
				return;
			}
			trimSpace();
			vm.orderSearch.listStatus = $("#listStatus").data("kendoMultiSelect").value();
			vm.orderSearch.listBussinessType = $("#requTypeCreExpNote").data("kendoMultiSelect").value();
			vm.orderSearch.listSignState = $("#signCA").data("kendoMultiSelect").value();
			if(vm.orderSearch.listBussinessType && vm.orderSearch.listBussinessType.length>0){
				for(var i = 0;i<vm.orderSearch.listBussinessType.length;i++){
					if(vm.orderSearch.listBussinessType[i]=="1"){
						vm.orderSearch.listBussinessType[i] = vm.businessTypes[0].code;
					}
					if(vm.orderSearch.listBussinessType[i]=="2"){
						vm.orderSearch.listBussinessType[i] = vm.businessTypes[1].code;
					}
					if(vm.orderSearch.listBussinessType[i]=="3"){
						vm.orderSearch.listBussinessType[i] = vm.businessTypes[2].code;
					}
					if(vm.orderSearch.listBussinessType[i]=="4"){
						vm.orderSearch.listBussinessType[i] = vm.businessTypes[3].code;
					}
					if(vm.orderSearch.listBussinessType[i]=="5"){
						vm.orderSearch.listBussinessType[i] = vm.businessTypes[4].code;
					}
					if(vm.orderSearch.listBussinessType[i]=="6"){
						vm.orderSearch.listBussinessType[i] = vm.businessTypes[5].code;
					}
					if(vm.orderSearch.listBussinessType[i]=="7"){
						vm.orderSearch.listBussinessType[i] = vm.businessTypes[6].code;
					}
					if(vm.orderSearch.listBussinessType[i]=="8"){
						vm.orderSearch.listBussinessType[i] = vm.businessTypes[7].code;
					}
					if(vm.orderSearch.listBussinessType[i]=="9"){
						vm.orderSearch.listBussinessType[i] = vm.businessTypes[8].code;
					}
					if(vm.orderSearch.listBussinessType[i]=="10"){
						vm.orderSearch.listBussinessType[i] = vm.businessTypes[9].code;
					}
				}
			}
			
			var grid =vm.exRegManaGrid;	
			if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 10
				});
			}              
		}
var record=0;
		function fillDataTableExt(data) {
			vm.gridOptions = kendoConfig.getGridOptions({
				autoBind: true,
				resizable: true,	
				columnMenu: false,
				scrollable: false,
				toolbar: [
		                    {
		                    	name: "actions",
		                        template: '<div class=" pull-left">'+
		                        '<button class="btn btn-qlk padding-search-right addQLK margin_right10"'+
		      					'ng-click="vm.createNew()" uib-tooltip="T???o m???i" translate>T???o m???i</button>'+
		      					'</div>'	+
		        				'<button class="btn btn-qlk padding-search-right TkQLK"'+
		      					'ng-click="vm.sendToSign()" uib-tooltip="Tr??nh k??" translate>Tr??nh k??</button>'+
		      					'</div>'	
		      					+
		      					 /* '<div class="btn-group pull-right margin_top_button margin10">'+
		                         '<i data-toggle="dropdown" aria-expanded="false"><i uib-tooltip="C??i ?????t hi???n th???" class="fa fa-cog" aria-hidden="true"></i></i>'+
		                         '<i uib-tooltip="Xu???t ra Excel" class="action-button excelQLK" ng-click="vm.exportExcelGrid()" aria-hidden="true"></i>'+  
		                         '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
		                    '<label ng-repeat="column in vm.exRegManaGrid.columns.slice(1,vm.exRegManaGrid.columns.length) | filter: vm.gridColumnShowHideFilter">'+
				               '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+ */
							'<div class="btn-group pull-right margin_top_button margin_right10">'+
	                      	 '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">C??i ?????t</span><i class="fa fa-cog" aria-hidden="true"></i></i>'+
	                      	'<i class="tooltip1 action-button excelQLK" ng-click="vm.exportExcelGrid()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xu???t Excel</span></i>'+
		                    '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
		                    '<label ng-repeat="column in vm.exRegManaGrid.columns.slice(1,vm.exRegManaGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'+
		                    '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+
		                    '</label>'+
		                    '</div></div>'
		                    
		                    }
		                    ],
		                    dataBound: function (e) {
		    				    var grid = vm.exRegManaGrid;
		    				    grid.tbody.find("tr").dblclick(function (e) {
		    				        var dataItem = grid.dataItem(this);
		    				        vm.showDetail(dataItem)
		    				    });
		    				},
							dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
				dataSource: {
					serverPaging: true,
					 schema: {
						 total: function (response) {
						 
						 $("#extRegCount").text(""+response.total);
							 	vm.count = response.total;
								return response.total; // total is returned in
														// the "total" field of
														// the response
							},
							data: function (response) {
								var list=response.data;
				        		for(var x in list){
				        			for(var i in $scope.listCheck){
				        				if(list[x].orderId===$scope.listCheck[i].orderId){
				        					list[x].selected=true;
				        				}
				        			}
				        		}
				        		return list;// data is returned in
														// the "data" field of
														// the response
							},
		                },
					transport: {
						read: {
		                        // Thuc hien viec goi service
							url: Constant.BASE_SERVICE_URL + "orderServiceRest/order/doSearchExportRequirement",
							contentType: "application/json; charset=utf-8",
							type: "POST"
						},					
						parameterMap: function (options, type) {
						
					if(vm.orderSearch.listBussinessType == undefined || vm.orderSearch.listBussinessType == null){
					vm.orderSearch.listBussinessType = [];
					}
					/*if(vm.orderSearch.createdDateTo == undefined || vm.orderSearch.createdDateTo == ""){
					vm.orderSearch.createdDateTo = null;
					} */
						vm.orderSearch.page = options.page
						vm.orderSearch.pageSize = options.pageSize                               

								return JSON.stringify(vm.orderSearch)
						}
					},					 
					pageSize: 10
				} ,
				noRecords: true,
				messages: {
					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
				},
				pageable: {
					refresh: false,
					 pageSizes: [10, 15, 20, 25],
					messages: {
		                display: "{0}-{1} c???a {2} k???t qu???",
		                itemsPerPage: "k???t qu???/trang",
		                empty: "Kh??ng c?? k???t qu??? hi???n th???"
		            }
				},
				columns: [{
					title : "<input type='checkbox' id='checkalllistEx' name='gridchkselectall' ng-click='vm.chkSelectAllForExq();' ng-model='vm.chkAllForExReq' />",
					template: "<input type='checkbox' id='childcheckInExReq' name='gridcheckbox' ng-click='vm.handleCheckForExq(dataItem)' ng-model='dataItem.selected'/>",
			        width: "5%",
			        headerAttributes: {style:"text-align:center;"},
					attributes:{style:"text-align:center;"}
				},
				{
					title: "TT",
					field:"stt",
			        template: function () {
					  return ++record;
					 },
			        width: "5%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}
				,  {
					title: "M?? y??u c???u",
					field: "code",
			        width: "15%",
					 template:  '<a class="#=orderId#" href="javascript:void(0);" ng-click=vm.showDetail(dataItem)>#=code#</a>',
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "Lo???i y??u c???u",
			        field: "bussinessType",
			        template: function($scope){
						if($scope.bussinessType == vm.businessTypes[0].code){
							return vm.businessTypes[0].name;
						}else if($scope.bussinessType == vm.businessTypes[1].code){
							return vm.businessTypes[1].name;
						}else if($scope.bussinessType == vm.businessTypes[2].code){
							return vm.businessTypes[2].name;
						}else if($scope.bussinessType == vm.businessTypes[3].code){
							return vm.businessTypes[3].name;
						}else if($scope.bussinessType == vm.businessTypes[4].code){
							return vm.businessTypes[4].name;
						}else if($scope.bussinessType == vm.businessTypes[5].code){
							return vm.businessTypes[5].name;
						}else if($scope.bussinessType == vm.businessTypes[6].code){
							return vm.businessTypes[6].name;
						}else if($scope.bussinessType == vm.businessTypes[7].code){
							return vm.businessTypes[7].name;
						}else if($scope.bussinessType == vm.businessTypes[8].code){
							return vm.businessTypes[8].name;
						}else{
							return vm.businessTypes[9].name;
						}
					},
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "Kho xu???t",
			        field: "stockName",
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "Ng?????i t???o",
			        field: "createdByName",
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},
				/*{
					title: "????n v??? t???o",
			        field: "createdDeptedName",
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},*/  
				{
					title: "K?? CA",
					 field: "signState",
			        template :   "# if(signState == 1){ #" + "#= 'Ch??a tr??nh k??' #" + "# } " +
			        "else if (signState == 2) { # " + "#= '???? tr??nh k??' #"+ "#} " +
			        "else if (signState == 3) { # " + "#= '???? k??' #"+ "#} " +
			        "else if (signState == 4) { # " + "#= '???? t??? ch???i' #"+ "#} " +

			        		"#",
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},  {
					title: "Tr???ng th??i",
					 field: "status",
				        template :  "# if(status == 1){ #" + "#= 'Ch??a t???o phi???u' #" + "# } " +
				        "else if (status == 2) { # " + "#= '???? t???o phi???u' #"+ "#} " +
				        "else if (status == 3) { # " + "#= '???? nh???p/xu???t' #"+ "#} " +
				        "else if (status == 4) { # " + "#= '???? h???y' #"+ "#} " +
				        "else if (status == 5) { # " + "#= '???? t??? ch???i' #"+ "#} " +
				        		"#",
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "Thao t??c",
			        template: 
					'<div class="text-center #=orderId#""> '	+
		        	// '		<a   type="button"  class="#=orderId# icon_table" uib-tooltip="Kh??a" translate>'+
		        	// '			<i  style="color:grey" ng-hide="dataItem.status == 1 " class="fa fa-files-o" aria-hidden="true"></i> '+
		        	// '		</a> '+
				'		<button  style=" border: none; background-color: white;" ng-click=vm.copyzzz(dataItem)   class="icon_table" uib-tooltip="Sao ch??p" translate>'+
	        	'			<i style="color:steelblue;" class="fa fa-files-o" aria-hidden="true"></i> '+
	        	'		</button> '+
	        	'		<button  style=" border: none; background-color: white;" ng-show="dataItem.status != 1 || dataItem.signState != 1" class="#=orderId# icon_table" uib-tooltip="Kh??a" translate> '+
	        	'			<i style="color:grey"  ng-show="dataItem.status != 1 || dataItem.signState != 1" class="fa fa-pencil"  aria-hidden="true"></i>'+
	        	'		</button> '+
	        	'		<button  style=" border: none; background-color: white;" ng-click=vm.update(dataItem) ng-show="dataItem.status == 1 && dataItem.signState == 1"  class="#=orderId# icon_table" uib-tooltip="C???p nh???t" translate>'+
	        	'			<i ng-show="dataItem.status == 1 && dataItem.signState == 1" class="fa fa-pencil" aria-hidden="true"></i> '+
	        	'		</button>' +
	        	'		<button  style=" border: none; background-color: white;" ng-show="dataItem.status != 1 || dataItem.signState != 1" class="#=orderId# icon_table" uib-tooltip="Kh??a" translate> '+
	        	'			<i style="color:grey"  ng-show="dataItem.status != 1 || dataItem.signState != 1" class="fa fa-trash" aria-hidden="true"></i>'+
	        	'		</button> '+
	        	'		<button  style=" border: none; background-color: white;" ng-click=vm.openRemovePopup(dataItem) ng-show="dataItem.status == 1 && dataItem.signState == 1" class="#=orderId# icon_table" uib-tooltip="H???y b???" translate>'+
	        	'			<i ng-show="dataItem.status == 1 && dataItem.signState == 1"  style="color:steelblue;" class="fa fa-trash" aria-hidden="true"></i> '+
	        	'		</button>'
					+'</div>', 
			        width: "15%",
			        field:"stt"
				}]
			});
		}
		
		$scope.listCheck=[];
		vm.handleCheckForExq=handleCheckForExq;
    	function handleCheckForExq(dataItem){
    		if(dataItem.selected){
    		$scope.listCheck.push(dataItem);
    		} else {
    			for(var i=0;i<$scope.listCheck.length;i++){
    				if($scope.listCheck[i].orderId===dataItem.orderId){
    				$scope.listCheck.splice(i,1);
    				}
    			}
				$('[name="gridchkselectall"]').prop('checked', false);
    		}
    		}
    	vm.chkSelectAllForExq=chkSelectAllForExq;
		$scope.checkSearch=false;
		function chkSelectAllForExq(){
			var grid = vm.exRegManaGrid;
	    		chkSelectAllBase(vm.chkAllForExReq, grid);
			if(vm.chkAllForExReq){
				if( $scope.checkSearch && $scope.dataSearch.length >0){
				$scope.listCheck=$scope.dataSearch;
				} else {
				
					CommonService.getallData("orderServiceRest/order/getAllExportStatement",vm.orderSearch).then(function(data){
						$scope.listCheck=data.plain();
						})
					}
				} else {
					$scope.listCheck=[];
				}
		};
	    
		function fillDataTableGoodsList(data) {
			vm.goodsListGridOptions = kendoConfig.getGridOptions({
				autoBind: true,
				resizable: true,	
				columnMenu: false,
				scrollable: false,
				  change: onChangeGoodList,
				 /*  {
			          var gridDetails = $("#goodsDetailGrid").data("kendoGrid");
			          var dataItem = this.dataItem(this.select());
			          gridDetails.dataSource.filter({ field: "orderGoodsId", value: dataItem.orderGoodsId, operator: "eq" });
			          document.getElementById('gDs').innerHTML = dataItem.goodsName;
					  document.getElementById('gDsCode').innerHTML = dataItem.goodsCode;
					  	 document.getElementById('status').innerHTML = dataItem.goodsStateName;
				  }, */
			    	dataBound: function(e) {		
			    		 var grid = $("#goodsListGrid").data("kendoGrid");
                         
						 grid.select("tr:eq(1)");
				   	},
				dataSource: {
					serverPaging: true,
					 schema: {
						 total: function (response) {
								return response.total; // total is returned in
														// the "total" field of
														// the response
							},
							data: function (response) {
								return response.data; // data is returned in
														// the "data" field of
														// the response
							},
		                },
					transport: {
						read: {
		                        // Thuc hien viec goi service
							url: Constant.BASE_SERVICE_URL + "orderGoodsServiceRest/orderGoods/doSearchGoodsForExportOrder",
							contentType: "application/json; charset=utf-8",
							type: "POST"
						},					
						parameterMap: function (options, type) {
                             // vm.appParamSearch.employeeId =
								// Constant.user.srvUser.catEmployeeId;
							    vm.orderGoodsSearch.page = options.page
								vm.orderGoodsSearch.pageSize = options.pageSize
								
								return JSON.stringify(vm.orderGoodsSearch)
						}
					},					 
					pageSize: 10
				},
				noRecords: true,
				messages: {
					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
				},
				pageable: {
					refresh: false,
					 pageSizes: [10, 15, 20, 25],
					messages: {
		                display: "{0}-{1} c???a {2} k???t qu???",
		                itemsPerPage: "k???t qu???/trang",
		                empty: "Kh??ng c?? k???t qu??? hi???n th???"
		            }
				},
				columns: [
				{
					title: "TT",
					field:"stt",
			        template: dataItem => $("#goodsListGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}
				,  {
					title: "M?? h??ng",
					field: 'goodsCode',
			        width: "25%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "T??n h??ng",
			        field: 'goodsName',
			        width: "25%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "????n v??? t??nh",
			        field: 'goodsUnitName',
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},  {
					title: "S??? l?????ng",
					 field: 'amount',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				},  {
					title: "T??nh tr???ng",
					 field: 'goodsStateName',
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}]
			});
		}
		//change list good detai when select good
		function onChangeGoodList(){
			
			if ($("#goodsListGrid").data("kendoGrid").select().length > 0){
				var tr = $("#goodsListGrid").data("kendoGrid").select().closest("tr");
    			var dataItem = $("#goodsListGrid").data("kendoGrid").dataItem(tr);
    			
    			vm.orderGoodsDetailSearch = dataItem;
    			$("#goodsCodeAndName").text(vm.orderGoodsDetailSearch.goodsCode+" "+vm.orderGoodsDetailSearch.goodsName);
				/* document.getElementById('gDs').innerHTML = dataItem.goodsName;
					  document.getElementById('gDsCode').innerHTML = dataItem.goodsCode;
					  	 document.getElementById('status').innerHTML = dataItem.goodsStateName; */
    			var grid = $("#goodsDetailGrid").data("kendoGrid");	
    			if(grid){
    				grid.dataSource.query({
    					page: 1,
    					pageSize: 10
    				});
    			} else {
    			fillDataTableGoodsDetail([]);
    			}
    			
    			console.log(vm.orderGoodsDetailSearch.goodsName);
			}
			
		}
		
		//editable data grid
		function fillDataTableGoodsListForEditable(data) {
					var dataSource = new kendo.data.DataSource({
                pageSize: 10,
                data: data,
                autoSync: true,    
                schema: {
						 model: {
		                        id: "goodsCode",
		                    	fields: {
		                    		stt: {editable: false},goodsCode: {editable: false},
		                    		goodsUnitName:{editable: false},
		                    		amount:  { type: "number", format: "{0:c}" },
		                    		goodsName:{editable: false},
									isSerial:{editable: false},
		                    		action:{editable: false}
		                    	}
						 }
                }
            });
			vm.goodsListGridEditableOptions = kendoConfig.getGridOptions({
				autoBind: true,
				resizable: true,
				columnMenu: false,
				scrollable:false,
				dataSource : dataSource,
				noRecords: true,
				messages: {
					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
				},
				pageable: {
					refresh: false,
					 pageSizes: [10, 15, 20, 25],
					 pageSize: 10,

					messages: {
		                display: "{0}-{1} c???a {2} k???t qu???",
		                itemsPerPage: "k???t qu???/trang",
		                empty: "Kh??ng c?? k???t qu??? hi???n th???"
		            }
				},
				columns: [
				{
					title: "TT",
					field:"stt",
			        template: dataItem => $("#goodsListGridEditable").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}
				,  {
					title: "M?? h??ng",
					field: 'goodsCode',
			        width: "20%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "T??n h??ng",
			        field: 'goodsName',
			        width: "20%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "????n v??? t??nh",
			        field: 'goodsUnitName',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},  {
					title: "S??? l?????ng",
					 field: 'amount',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				},  {
					title: "T??nh tr???ng",
					 field: 'goodsState',
					 editor: categoryDropDownEditor,
				     template :  "# if(goodsState == 1){ #" + "#= 'B??nh th?????ng' #" + "# } " + "else if (goodsState == 2) { # " + "#= 'H???ng' #"+ "#} #", 
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "Chi ti???t h??ng h??a",
					 field: 'isSerial',
					 template: "# if(isSerial == 1){ #" + "<a  ng-click=caller.seeDetail(dataItem)>#= 'Chi ti???t' #</a>" + "# } " + "else if (isSerial == 0) { # " + "#= '' #"+ "#} #",
							   
							//'<a class="#=id#" href="javascript:void(0);" ng-click=caller.seeDetail(dataItem)>Chi ti???t</a>',	
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				},{
					title: "Thao t??c",
					field:"action",
			        template: dataItem =>
					'<div class="text-center #=goodsCode#"">'+
						'<a type="button" class="#=goodsCode# icon_table" ng-hide="vm.hideDeleteRow" uib-tooltip="X??a" translate ng-click="caller.removeLine(dataItem)">'+
							'<i class="fa fa-trash" aria-hidden="true"></i>'+
						'</a>'+
					'</div>',
			        width: "10%",
			        field:"action"
				}]
			});
		}
		
		vm.status = [
			 {id: 1, name: 'B??nh th?????ng'}
			,{id: 2, name: 'H???ng'}
		]
		function categoryDropDownEditor(container, options) {
           $('<input required name="' + options.field + '"/>')
               .appendTo(container)
               .kendoDropDownList({
                   autoBind: true,
                   dataTextField: "name",
                   dataValueField: "id",
                   dataSource: vm.status
               });
		}
		
		vm.seeDetail = function(dataItem)
		{
		vm.ordergood = [];
		vm.listOrderGoodsDetailDTO = [];
			    var teamplateUrl="wms/exportRequestManage/viewDetailOrderGood.html";
			    var title="Th??ng tin chi ti???t h??ng h??a";
			    var windowId="DETAIL";
			    vm.listOrderGoodsDetailDTO.push(dataItem);
			    vm.ordergoodId = dataItem.orderGoodsId;
				vm.ordergood.goodsCode = dataItem.goodsCode;
				vm.ordergood.goodsName = dataItem.goodsName;
				vm.ordergood.stockName = vm.dupOrder.stockName;
				vm.ordergood.stockCode = vm.dupOrder.stockId;

				fillDetailTable(vm.listOrderGoodsDetailDTO);
			    CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,false,'60%','40%');
		}
		// SignVoffice
		vm.rowIndex=null;
		vm.sendToSign = function(){
			var teamplateUrl="wms/popup/SignVofficePopup.html";
			 var title="Tr??nh k?? y??u c???u xu???t kho";
			 var windowId="ORDER";
			 vm.nameToSign="Danh s??ch y??u c???u tr??nh k??"+"("+$scope.listCheck.length+")";
			 var selectedRow = [];
				/* for(var i in $scope.listCheck){
						selectedRow.push($scope.listCheck[i].orderId);
						}
				
				if ($scope.listCheck.length === 0) {
					//if (grid.select() == null || grid.select().length === 0) {
						toastr.warning("C???n ch???n b???n ghi tr?????c khi th???c hi???n!");
						return;
					//}
				} */
				
				var err="";
							for(var i =0;i< $scope.listCheck.length;i++){
								if(Constant.userInfo.VpsUserInfo.sysUserId !== $scope.listCheck[i].createdBy){
										if(err===""){
											err="B???n kh??ng c?? quy???n tr??nh k?? v??n b???n c?? m?? :" +$scope.listCheck[i].code;
										} else {
										err=err+", "+$scope.listCheck[i].code;
										}
								} else {
									selectedRow.push($scope.listCheck[i].orderId);
								}
								
							}
							if(err!==""){
								toastr.error(err);
									return;
							}
							if ($scope.listCheck.length === 0) {
								//if (grid.select() == null || grid.select().length === 0) {
									toastr.warning("C???n ch???n b???n ghi tr?????c khi th???c hi???n!");
									return;
								//}
							}
				var obj={};
				obj.listId=selectedRow;
				obj.type="02";
				obj.reportName="ThongTinYeuCauXuat_KhongSerial";
				CommonService.getDataSign(obj).then(function(data){
							if(data.error){
								toastr.error(data.error);
								return;
								}
					
				

					var dataList=data.plain();

				
			 
				CommonService.populatePopupVofice(teamplateUrl,title,'01',dataList,vm,windowId,false,'85%','85%');
		
				
		});
		}
		
		
		//editable details grid
		function fillDataTableGoodsDetailEditable(data) {
			vm.goodsDetailGrid10Options = kendoConfig.getGridOptions({
				autoBind: false,
				columnMenu: false,
				 toolbar: [
						 { name: "actions",
			                     template: '<div class=" pull-left button-radio">'+
			                     '<button class="btn btn-qlk padding-search-right addQLK"'+
			   					'ng-click="caller.addRow()" uib-tooltip="Th??m d??ng m???i" translate></button>'+
			   					'</div>'
						 }
					  ],
				resizable: true,			 
				dataSource: {
					serverPaging: true,
					 schema: {
						 total: function (response) {
								return response.total; // total is returned in
														// the "total" field of
														// the response
							},
							data: function (response) {
								return response.data; // data is returned in
														// the "data" field of
														// the response
							},
		                },
					transport: {
						read: {
		                        // Thuc hien viec goi service
							url: Constant.BASE_SERVICE_URL + "orderGoodsDetailServiceRest/orderGoodsDetail/doSearchGoodsDetailForExportReq",
							contentType: "application/json; charset=utf-8",
							type: "POST"
						},					
						parameterMap: function (options, type) {
						    vm.orderGoodsDetailSearch.page = options.page
							vm.orderGoodsDetailSearch.pageSize = options.pageSize
							
							return JSON.stringify(vm.orderGoodsDetailSearch)
						}
					},					 
					pageSize: 10
				},				
				noRecords: true,
				messages: {
					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
				},
				pageable: {
					refresh: false,
					 pageSizes: [10, 15, 20, 25],
					messages: {
		                display: "{0}-{1} c???a {2} k???t qu???",
		                itemsPerPage: "k???t qu???/trang",
		                empty: "Kh??ng c?? k???t qu??? hi???n th???"
		            }
				},
				columns: [
				{
					title: "TT",
					field:"stt",
			        template: dataItem => $("#goodsDetailGrid10").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
			        width: 70,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}
				,  {
					title: "Serial",
					field: 'serial',
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "M?? h???p ?????ng",
			        field: 'contractCode',
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "Part number",
			        field: 'partNumber',
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},  {
					title: "H??ng s???n xu???t",
					 field: 'manufacturerName',
			        width: 200,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},  {
					title: "N?????c s???n xu???t",
					 field: 'producingCountryName',
			        width: 200,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}]
			});
		}
		
		 vm.goods ={};
		 
		 vm.showPatternGoods = showPatternGoods;
			function showPatternGoods()
			{
				 var teamplateUrl="wms/exportRequestManage/listPatternGood.html";
				    var title="Danh s??ch m???u h??ng h??a trong y??u c???u xu???t kho";
				    var windowId="PATTERN";
					vm.chkAll = false;
				    CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,false,'90%','75%');
			}
			
		
		function fillDataTableGoodsDetail(data) {
			vm.goodsDetailGridOptions = kendoConfig.getGridOptions({
				autoBind: true,
				resizable: true,	
				columnMenu: false,
				scrollable:false,
				dataSource: {
					serverPaging: true,
					 schema: {
						 total: function (response) {
								return response.total; // total is returned in
														// the "total" field of
														// the response
							},
							data: function (response) {
								return response.data; // data is returned in
														// the "data" field of
														// the response
							},
		                },
					transport: {
						read: {
		                        // Thuc hien viec goi service
							url: Constant.BASE_SERVICE_URL + "orderGoodsDetailServiceRest/orderGoodsDetail/doSearchGoodsDetailForImportReq",
							contentType: "application/json; charset=utf-8",
							type: "POST"
						},					
						parameterMap: function (options, type) {
						    vm.orderGoodsDetailSearch.page = options.page
							vm.orderGoodsDetailSearch.pageSize = options.pageSize
							
							return JSON.stringify(vm.orderGoodsDetailSearch)
						}
					},					 
					pageSize: 10
				},				
				noRecords: true,
				messages: {
					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
				},
				pageable: {
					refresh: false,
					 pageSizes: [10, 15, 20, 25],
					messages: {
		                display: "{0}-{1} c???a {2} k???t qu???",
		                itemsPerPage: "k???t qu???/trang",
		                empty: "Kh??ng c?? k???t qu??? hi???n th???"
		            }
				},
				columns: [
				{
					title: "TT",
					field:"stt",
			        template: dataItem => $("#goodsDetailGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}
				,  {
					title: "Serial",
					field: 'serial',
			        width: "20%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "M?? h???p ?????ng",
			        field: 'contractCode',
			        width: "20%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "Part number",
			        field: 'partNumber',
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},  {
					title: "H??ng s???n xu???t",
					 field: 'manufacturerName',
			        width: "20%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},  {
					title: "N?????c s???n xu???t",
					 field: 'producingCountryName',
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}]
			});
		}

		/* vm.sign = function(){
			//var teamplateUrl = Constant.URL_POPUP['VOFICE_POPUP'];
			var teamplateUrl = "wms/exportRequestManage/signPopup.html";
			 var title="Tr??nh k?? y??u c???u xu???t kho";
			 var windowId="EX_SIGN";
			 CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'70%','48%'); 
		 } */
		
		vm.cancel= cancel ;
		function cancel(){
				vm.appParam={};
				vm.isCreateNew = false;
							confirm('B???n c?? ch???c mu???n h???y b??? thao t??c n??y', function(){
							vm.copySearch = {};
				vm.copySearch.page = vm.dupOrder.page;
				vm.copySearch.pageSize = vm.dupOrder.pageSize;
				$rootScope.$broadcast("backFromCancelCopy", vm.copySearch);
							//CommonService.dismissPopup();
							$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
			});
		}
		function refreshGrid(d) {
			var grid = vm.exReqManaGrid;
			if(grid){
				grid.dataSource.data(d);
				grid.refresh();
			}
		}
		
		
		
		vm.showDetail=function(item){
			var teamplateUrl="wms/exportRequestManage/detailsPopup.html";
			 var windowId="DETAIL_POPUP";
				 vm.order = item;
				 var title="Chi ti???t y??u c???u xu???t kho " + vm.order.code;
				 vm.checkBoxAutoLoad(vm.order);
				 vm.orderGoodsSearch.orderId = vm.order.orderId;
				 fillDataTableGoodsList([]);
				vm.gotoTabOnePopUp();
				 if(vm.order.status == '4' || vm.order.status == '5'){
					 vm.terminatedOrder = true;
					 CommonService.populatePopupCreate(teamplateUrl,title,vm.order,vm,windowId,false,'95%','90%'); 
				 }else{
					 vm.terminatedOrder = false;
					 CommonService.populatePopupCreate(teamplateUrl,title,vm.order,vm,windowId,false,'85%','90%');  
				 }
		}
		
		vm.exportOrder={};
		
		
		vm.update=function(item){
				 vm.exportOrder = item;
				 vm.exportOrder.title = "C???p nh???t y??u c???u xu???t kho";
				 vm.exportOrder.page = vm.orderSearch.page;
				 vm.exportOrder.pageSize = vm.orderSearch.pageSize;
				 vm.exportOrder.dataReasonDelete = vm.dataReasonDelete;
				 
exReqManaService.getGoodsDetailByOrderId(vm.exportOrder).then(function(d) {
				vm.exportOrder.listOrderGoodsDetailDTO = d.plain();
exReqManaService.setData(vm.exportOrder);
				 $rootScope.$broadcast("ezOrderDetail", vm.exportOrder);
				 CommonService.goTo('UPDATE_EX_REQ_MANA');	
				//vm.goodsDataforCopy(vm.listOrderGoodsDetailDTO);
				//fillDataTableGoodsListForEditable(vm.dupOrder.listOrderGoodsDTO);
			//CommonService.populatePopupCreate(teamplateUrl,title,vm.dupOrder,vm,windowId,false,'88%','89%', "orderExportStock"); 	
				}, function() {
					console.error('Error');
				});
			 }
		vm.copyzzz=function(item){
				 vm.dupOrder = item;
				 vm.dupOrder.title = "Sao ch??p y??u c???u xu???t kho";
				vm.dupOrder.page = vm.orderSearch.page;
				 vm.dupOrder.pageSize = vm.orderSearch.pageSize;
				 vm.dupOrder.dataReasonDelete = vm.dataReasonDelete;
				 var teamplateUrl="wms/exportRequestManage/detailPopupCopy.html";
			 var windowId="DETAIL_POPUP";
			 //vm.dupOrder = dataItem;
				 var title="Sao ch??p y??u c???u xu???t kho: " + vm.dupOrder.code;
				 $scope.$watch('vm.dupOrder.stockId', function() {
						var obj={};
						obj.value="YCXK";
						obj.orgValue="VTM";
						obj.stockId=vm.dupOrder.stockId;
						CommonService.genCode(obj).then(
							function(d) {
								vm.dupOrder.code = d;
							});
				    });
					//for (int i; i<=)
				 vm.orderGoodsSearch.orderId = vm.dupOrder.orderId;

			vm.showTabOne = true;
			vm.exportOrder.createdDate = kendo.toString(new Date(),"dd/MM/yyyy");
			vm.checkBoxAutoLoad(vm.dupOrder);
			//fillDataTableGoodsListForEditable(vm.dupOrder);
			exReqManaService.getGoodsDetailByOrderId(vm.orderGoodsSearch).then(function(d) {
				vm.listOrderGoodsDetailDTO = d.plain();
				vm.goodsDataforCopy(vm.listOrderGoodsDetailDTO);
				fillDataTableGoodsListForEditable(vm.dupOrder.listOrderGoodsDTO);
			CommonService.populatePopupCreate(teamplateUrl,title,vm.dupOrder,vm,windowId,false,'88%','89%', "orderExportStock"); 	
			$('#exRegManaGrid').data('kendoGrid').dataSource.read();
			$('#exRegManaGrid').data('kendoGrid').refresh();
				}, function() {
					console.error('Error');
				});
				
			//CommonService.populatePopupCreate(teamplateUrl,title,vm.dupOrder,vm,windowId,false,'88%','89%'); 	
	
			 }
		
		vm.createNew = function(){
			vm.exportOrder={};
			vm.exportOrder.title = "T???o m???i y??u c???u xu???t kho";
			vm.exportOrder.dataReasonDelete = vm.dataReasonDelete;
			vm.exportOrder.page = vm.orderSearch.page;
			vm.exportOrder.pageSize = vm.orderSearch.pageSize;			
			exReqManaService.setData(vm.exportOrder);
			$rootScope.$broadcast("ezOrderDetail", vm.exportOrder);
			CommonService.goTo('CREATE_EX_REQ_MANA');
		}
		
		vm.gotoTabOnePopUp = function(){
			vm.showTabOne = true;
			vm.bt1 = false; vm.bt2 = true; vm.bt3 = true; vm.bt4 = false;

		}
		
		vm.gotoTabTwoPopUp = function(){
			vm.showTabOne = false;
			vm.bt1 = true; vm.bt2 = false; vm.bt3 =false ; vm.bt4 = true;

		}
		
		vm.advancedSearch =function(){
			vm.showAdvancedSearch = !vm.showAdvancedSearch;
		}
		vm.dataReasonDelete = [];

		vm.openRemovePopup=openRemovePopup;
		function openRemovePopup(dataItem){
			var reason = {
					status : '1',
					apply : '5'
			}
			exReqManaService.getReasonForComboBox(reason).then(function(d){
				vm.dataReasonDelete =  d.plain();
				var teamplateUrl = "wms/exportRequestManage/reqCancelPopup.html";
				var title="H???y y??u c???u xu???t kho";
				var windowId="EXT";
				dataItem.cancelByName = Constant.user.vpsUserToken.fullName;
				dataItem.cancelBy = Constant.user.vpsUserToken.sysUserId;
				dataItem.cancelDate=kendo.toString(new Date(),"dd/MM/yyyy");
				 CommonService.populatePopupCreate(teamplateUrl,title,dataItem,vm,windowId,false,'55%','35%'); 
			});
		}
		
		vm.remove=remove;
		function remove(data){
			confirm('X??c nh???n x??a',function (){
				data.cancelReasonApply = "5";
			/* 	exReqManaService.remove(data).then(
						function(d) {
							toastr.success("X??a th??nh c??ng!");
							$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
							var sizePage = $("#exRegManaGrid").data("kendoGrid").dataSource.total();
							if(sizePage % 10 == 1){
								var currentPage = $("#exRegManaGrid").data("kendoGrid").dataSource.page();
								if (currentPage > 1) {
								$("#exRegManaGrid").data("kendoGrid").dataSource.page(currentPage - 1);
								}
							}
			        		$("#exRegManaGrid").data("kendoGrid").dataSource.read();
			        		$("#exRegManaGrid").data("kendoGrid").refresh();
						}, function(errResponse) {
							toastr.error("L???i kh??ng x??a ???????c!");
						}); */
exReqManaService.remove(data).then(function(mess){
				if(mess.error){
					toastr.error(mess.error);
					return;
				}
        		toastr.success("X??a th??nh c??ng!");
        		
        		$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        		var sizePage = $("#exRegManaGrid").data("kendoGrid").dataSource.total();
				var pageSize = $("#exRegManaGrid").data("kendoGrid").dataSource.pageSize();
				if(sizePage % pageSize == 1){
					var currentPage = $("#exRegManaGrid").data("kendoGrid").dataSource.page();
			        if (currentPage > 1) {
			            $("#exRegManaGrid").data("kendoGrid").dataSource.page(currentPage - 1);
			        }
				}
				$('#exRegManaGrid').data('kendoGrid').dataSource.read();
				$('#exRegManaGrid').data('kendoGrid').refresh();
            }, function(errResponse) {
            	if (errResponse.status == 406) {
                    toastr.error("Ng?????i d??ng hi???n t???i kh??ng c?? quy???n x??a b???n ghi n??y!");
                } else {
               	 toastr.error("L???i khi l??u");
                }
			});
			} 
			)
		}
		
		vm.openStocks = openStocks
		vm.labelsName ={};
		function openStocks(fromLabel){
			vm.stocks ={};
				var templateUrl = 'wms/popup/findStockPopup.html';
				var title = gettextCatalog.getString("Danh s??ch kho h??ng");
				vm.data1 = [];
				vm.gridStockOptions = kendoConfig.getGridOptions({
					autoBind: true,
					resizable: true,
					columnMenu: false,
					dataSource:{
						serverPaging: true,
						 schema: {
							 total: function (response) {
									return response.total; // total is returned
															// in
															// the "total" field
															// of
															// the response
								},
								data: function (response) {
									return response;
								}
			                },
						transport: {
							 read:  {
								  url: Constant.BASE_SERVICE_URL +"stockRsServiceRest/getStocksForAutocompleteDropDown", 
								  contentType:"application/json; charset=utf-8",
								  type: "POST"
			                    },
							parameterMap: function (options, type) {
								    vm.stocks.page = options.page;
									vm.stocks.pageSize = options.pageSize;
									return JSON.stringify(vm.stocks);
							}
						},					 
						pageSize: 10
					},
					noRecords: true,
					messages: {
						noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
					},
					pageSize: 10,
					pageable: {
						refresh: false,
						 pageSizes: [10, 15, 20, 25],
						messages: {
			                display: "{0}-{1} c???a {2} k???t qu???",
			                itemsPerPage: "k???t qu???/trang",
			                empty: "Kh??ng c?? k???t qu??? hi???n th???"
			            }
					},
					columns: [ {
						title : "<input type='checkbox' id='checkalllistdraw' name='gridchkselectall' ng-click='vm.chkSelectAllForExReq();' ng-model='vm.chkAllInExreq' />",
						template: "<input type='checkbox' id='childcheckInExReqGrid' name='gridcheckbox' ng-click='vm.onCheck($event)'/>",
				        width: 25,
				        headerAttributes: {style:"text-align:center;"},
						attributes:{style:"text-align:center;"}
					}
					,{
						title: "TT",
						field: "stt",
						template: dataItem => $("#gridView").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
						width: 30
					}, {
						title: "M?? kho",
						field: "code",
						width: 70
					},{
						title: "T??n kho",
						field: "name",
						width: 147
					}, {
						title: "????n v??? qu???n l??",
						field: "departmentName",
						width: 145
					}, {
						title: "Ch???n",
						field: "action",
						width: 50
					}]
				});
				vm.labelsName.value = fromLabel;
				CommonService.populatePopup(templateUrl,vm.gridStockOptions,title,vm.data1 ,vm,null,false,'85%','62%'); 		
		}
		
		//...................
		
		//vm.chkSelectAllForExq = function(item) {
	    	//var grid = vm.imReqMaGrid;
	    	//chkSelectAllBase(vm.chkAll, grid);
	    //};
	   /* vm.handleCheckForExq = function(item){
			if(document.getElementById("checkalllistImpReq").checked == true){
				document.getElementById("checkalllistImpReq").checked = false;
			}
		}*/
		////.......................
	
		vm.openUsers = openUsers
		function openUsers(){
				vm.user = {};
				var templateUrl = 'wms/popup/findUserPopup.html';
				var title = gettextCatalog.getString("Danh s??ch ng?????i d??ng");
				vm.data1 = [];
				vm.gridUserOptions = kendoConfig.getGridOptions({
					autoBind: true,
					resizable: true,
					columnMenu: false,
					dataSource:{
						serverPaging: true,
						 schema: {
							 total: function (response) {
									return response.total; // total is returned in
															// the "total" field of
															// the response
								},
								data: function (response) {
									return response.data;
								}
			                },
						transport: {
							 read:  {
								  url: Constant.BASE_SERVICE_URL +"sysUserServiceRest/user/sysAllUserswms", 
								  contentType:"application/json; charset=utf-8",
								  type: "POST"
			                    },
							parameterMap: function (options, type) {
								    vm.user.page = options.page;
									vm.user.pageSize = options.pageSize;
									return JSON.stringify(vm.user);
							}
						},					 
						pageSize: 10
					},
					noRecords: true,
					messages: {
						noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
					},
					pageSize: 10,
					pageable: {
						refresh: false,
						 pageSizes: [10, 15, 20, 25],
						messages: {
			                display: "{0}-{1} c???a {2} k???t qu???",
			                itemsPerPage: "k???t qu???/trang",
			                empty: "Kh??ng c?? k???t qu??? hi???n th???"
			            }
					},
					columns: [ {
						title: "TT",
						field: "stt",
						template: dataItem => $("#gridView").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
						width: 40
					}, {
						title: "T??n ????ng nh???p",
						field: "loginName",
						width: 120
					},{
						title: "M?? nh??n vi??n",
						field: "employeeCode",
						width: 120
					}, {
						title: "H??? t??n",
						field: "fullName",
						width: 120
					}, {
						title: "Email",
						field: "email",
						width: 120
					}, {
						title: "S??T",
						field: "phoneNumber",
						width: 120
					}]
				});
				CommonService.populatePopup(templateUrl,vm.gridUserOptions,title,vm.data1 ,vm,null,false,'81%','82%'); 
		}
		
		vm.openDepartmentCreate=openDepartmentCreate
		vm.orderSearch.departmentpopUp ={};
		function openDepartmentCreate(popUp){
			vm.orderSearch.departmentpopUp = popUp;
			vm.obj={};
			CommonService.getDepartment(vm.obj).then(function(result){
				var templateUrl = 'wms/popup/findDepartmentPopUp.html';
				var title = gettextCatalog.getString("T??m ki???m ????n v???");
				var data=result.plain();
				vm.gridOptions = kendoConfig.getGridOptions({
					autoBind: true,
					resizable: true,
					dataSource: result,
					noRecords: true,
					columnMenu: false,
					messages: {
						noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
					},
					pageSize: 10,
					pageable: {
						refresh: false,
						 pageSizes: [10, 15, 20, 25],
						messages: {
			                display: "{0}-{1} c???a {2} k???t qu???",
			                itemsPerPage: "k???t qu???/trang",
			                empty: "Kh??ng c?? k???t qu??? hi???n th???"
			            }
					},
					columns:[{
						title: "TT",
						field: "#",
						template: dataItem => $("#gridView").data("kendoGrid").dataSource.indexOf(dataItem) + 1 ,
						width: "12%",
						 headerAttributes: {
								style: "text-align:center;"
							},
							attributes: {
								style: "text-align:center;"
							},
					}, 
					         {
						title: "M?? ph??ng<br> ban",
						field: "code",
						width: "18%",
						headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:right;"
						},
					}, {
						title: "T??n ph??ng ban",
						field: "text",
						width: "30%",
						 headerAttributes: {
								style: "text-align:center;"
							},
							attributes: {
								style: "text-align:left;"
							},
					}, {
						title: "????n v??? cha",
						field: "parentName",
						width: "30%",
						 headerAttributes: {
								style: "text-align:center;"
							},
							attributes: {
								style: "text-align:left;"
							},
					},{
						title: "Ng??y hi???u l???c",
						field: "effectDate",
						width: "20%",
						 headerAttributes: {
								style: "text-align:center;"
							},
							attributes: {
								style: "text-align:left;"
							},
					},{
						title: "Ng??y h???t hi???u l???c",
						field: "endDate",
						width: "25%",
						 headerAttributes: {
								style: "text-align:center;"
							},
							attributes: {
								style: "text-align:left;"
							},
					},{
						title: "Ch???n",
						 template: 
					        	'<div class="text-center "> '	+
				        	'		<a  type="button" class=" icon_table" uib-tooltip="Ch???n" translate>'+
				        	'			<i id="#=departmentId#" ng-click=save(dataItem) class="fa fa-check color-green" aria-hidden="true"></i> '+
				        	'		</a>'
								+'</div>',  
				        width: "15%",
				        field:"stt"
					}]
				});
				
				CommonService.populatePopupDept(templateUrl, title, vm.gridOptions,data, vm, popUp , 'string', false, '91%','89%');
			});
		}
		
		
		vm.openDepartmentTo=openDepartmentTo
		
		function openDepartmentTo(popUp){
			vm.obj={};
			vm.orderSearch.departmentpopUp = popUp;
			CommonService.getDepartment(vm.obj).then(function(result){
				var templateUrl = 'wms/popup/findDepartmentPopUp.html';
				var title = gettextCatalog.getString("T??m ki???m ????n v???");
				var data=result.plain();
				vm.gridOptions1 = kendoConfig.getGridOptions({
					autoBind: true,
					resizable: true,
					columnMenu: false,
					dataSource: result,
					noRecords: true,
					pageSize: 10,
					messages: {
						noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
					},
					pageable: {
						refresh: false,
						pageSizes: [10, 15, 20, 25],
						messages: {
			                display: "{0}-{1} c???a {2} k???t qu???",
			                itemsPerPage: "k???t qu???/trang",
			                empty: "Kh??ng c?? k???t qu??? hi???n th???"
			            }
					},
					columns:[{
						title: "TT",
						field: "#",
						template: dataItem => $("#gridView").data("kendoGrid").dataSource.indexOf(dataItem) + 1 ,
						width: "12%",
						 headerAttributes: {
								style: "text-align:center;"
							},
							attributes: {
								style: "text-align:center;"
							},
					}, 
					         {
						title: "M?? ph??ng<br> ban",
						field: "code",
						width: "18%",
						headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:right;"
						},
					}, {
						title: "T??n ph??ng ban",
						field: "text",
						width: "30%",
						 headerAttributes: {
								style: "text-align:center;"
							},
							attributes: {
								style: "text-align:left;"
							},
					}, {
						title: "????n v??? cha",
						field: "parentName",
						width: "30%",
						 headerAttributes: {
								style: "text-align:center;"
							},
							attributes: {
								style: "text-align:left;"
							},
					},{
						title: "Ng??y hi???u l???c",
						field: "effectDate",
						width: "20%",
						 headerAttributes: {
								style: "text-align:center;"
							},
							attributes: {
								style: "text-align:left;"
							},
					},{
						title: "Ng??y h???t hi???u l???c",
						field: "endDate",
						width: "25%",
						 headerAttributes: {
								style: "text-align:center;"
							},
							attributes: {
								style: "text-align:left;"
							},
					},{
						title: "Ch???n",
						 template: 
					        	'<div class="text-center "> '	+
				        	'		<a  type="button" class=" icon_table" uib-tooltip="Ch???n" translate>'+
				        	'			<i id="#=departmentId#" ng-click=save(dataItem) class="fa fa-check color-green" aria-hidden="true"></i> '+
				        	'		</a>'
								+'</div>',  
				        width: "15%",
				        field:"stt"
					}]
				});
				CommonService.populatePopupDept(templateUrl, title, vm.gridOptions1,data, vm, popUp, 'string', false, '92%','89%');
			});
		}

		vm.onSave=onSave;
		function onSave(data){
			if(vm.orderSearch.departmentpopUp == 'depOpenCreateCP'){
				vm.dupOrder.createdDeptedName = data.text;
				vm.dupOrder.createdDeptedId = data.id;
			}
			else if(vm.orderSearch.departmentpopUp == 'depOpenToCP'){
				vm.dupOrder.deptReceiveName = data.text;
				vm.dupOrder.deptReceiveId = data.id;
			}else if(vm.orderSearch.departmentpopUp == 'depOpenCreate'){
				vm.orderSearch.createdDeptedName = data.text;
				vm.orderSearch.createdDeptedId = data.id;
			}else if(vm.orderSearch.departmentpopUp  == 'depOpenTo'){
				vm.orderSearch.deptReceiveName = data.text;
				vm.orderSearch.deptReceiveId = data.id;
			}else{
				
			}
		}
 
		vm.showHideColumn=function(column){
        	if (angular.isUndefined(column.hidden)) {
        		vm.exRegManaGrid.hideColumn(column);
            } else if (column.hidden) {
            	vm.exRegManaGrid.showColumn(column);
            } else {
            	vm.exRegManaGrid.hideColumn(column);
            }
        }
		
		vm.gridColumnShowHideFilter = function (item) { 
            return item.type==null||item.type !=1; 
        };

        
        vm.cancelStockName  = function(pram)
		{
        	if(pram =='1')
        		vm.stockList.name = undefined;
        	else
        		vm.stockList.name2 = undefined;
		}
		
		vm.cancelTime = function(param)
		{
		if(param == 'search'){
			vm.orderSearch.createdDateFrom = undefined;
			vm.orderSearch.createdDateTo = undefined;
			vm.errMessage="";
			vm.errMessage1="";
			}
			
		}
		vm.cancelListStatus = function()
		{
			vm.orderSearch.listStatus = [];
		}
		vm.cancelListBussinessType = function()
		{
			vm.orderSearch.listBussinessType = [];
		}
		vm.cancelListSignState = function()
		{
			vm.orderSearch.listSignState = [];
		}

		vm.cancelCreptedDepted = function(screen){
			if(screen == 'search'){
				vm.orderSearch.createdDeptedName = undefined;
				vm.orderSearch.createdDeptedId = undefined;
			}	else{
				vm.order.createdDeptedName = undefined;
				vm.order.createdDeptedId = undefined;
				}
			}
			vm.cancelDeptReceive = function(screen)
			{
				if(screen == 'search'){
					vm.orderSearch.deptReceiveName = undefined;
					vm.orderSearch.deptReceiveId = undefined;
				}else{
					vm.order.deptReceiveName = undefined;
					vm.order.deptReceiveId = undefined;
				}
			}
			
			vm.cancelInput = function(param){
				if(param == 'cons'){
					vm.dupOrder.constrCode = undefined;
				}
				if(param == 'proj'){
					vm.dupOrder.projectCode = undefined;
				}
				if(param == 'partner')
				 {
					 vm.dupOrder.partnerName = undefined;
				 }
				if (param == 'date'){
				vm.dupOrder.expectedRecievedDate = null;
				}
				if (param == 'contract'){
				vm.dupOrder.contractCode = undefined;
				}if (param == 'impNote'){
				vm.dupOrder.stockTransCode = undefined;
				}
				if (param == 'deptReceive'){
				vm.dupOrder.deptReceiveName = undefined;
				}
			}
			
			
			vm.cancelCreatedByName = function()
			{
				vm.orderSearch.createrSearch = undefined;
			}
			
		vm.onCheck = function(item){
			if(document.getElementById("chkSelectAll").checked == true){
				document.getElementById("chkSelectAll").checked = false;
			}
		}
		
		vm.chkSelectAll = function(item) {
	    	var grid = vm.listGoodsGrid;
	    	chkSelectAllBase(vm.chkAll, grid);
	    }
		
		vm.selectPatternGood = selectPatternGood;
		function selectPatternGood()
		{
			var selectedItem = [];
			var lstGood = [];
			var dataItem=[];
			 var gridgoods = $("#goodsListGridEditable").data("kendoGrid");
			var grid = vm.listGoodsGrid;
			var rowList = grid.lockedTable == undefined ? grid.table.find("tr")
					: grid.lockedTable.find("tr");
			rowList.each(function(idx, item) {
			var row = $(item);
			var checkbox = $('[name="gridcheckbox"]', row);
				if (checkbox.is(':checked')){
					 dataItem = grid.dataItem(item);
					 dataItem.orderGoodsId  = null;
					 dataItem.goodsState = '1';
					 lstGood.push(dataItem);
				}
			});
			if(lstGood.length==0)
				{
				toastr.warning("Ch??a c?? b???n ghi n??o ???????c ch???n");
				}
			else
				{
				var checkInsert = false;
				for(var i=0;i<lstGood.length;i++)
					{
					var check = checkDups(lstGood[i]);
					 //var check = checkExistGoodPattern(lstGood[i]);
		                if(!check){
		                	checkInsert = true;
		                	gridgoods.dataSource.insert(lstGood[i]);
		                }  
					}
				if(checkInsert)
					{
					toastr.success("Thao t??c th??nh c??ng!");
					CommonService.closePopup1();
					}
				else
					{
					toastr.warning("H??ng h??a ???? t???n t???i tr??n l?????i");
					}
				
				}
		}
		
		function checkExistGoodPattern(goodsItem){
			var isExisted = false;
			var goodsGrid = vm.goodsListGridEditable;
            goodsGrid.table.find("tr").each(function(idx, item) {
    					var row = $(item);
    					var dataItem = goodsGrid.dataItem(item);
    						if(goodsItem.orderPatternGoodsId == dataItem.orderPatternGoodsId ){
        						isExisted = true;
    					}
    			});
            return isExisted;
		}
		
		//duplicate
		vm.copy = function(dataItem){
			 var teamplateUrl="wms/exportRequestManage/detailPopupCopy.html";
			 var windowId="DETAIL_POPUP";
			 vm.dupOrder = dataItem;
				 var title="Sao ch??p y??u c???u xu???t kho: " + vm.dupOrder.code;
				 vm.dupOrder.code = null;
				 $scope.$watch('vm.dupOrder.stockId', function() {
						var obj={};
						obj.value="YCXK";
						obj.orgValue="VTM";
						obj.stockId=vm.dupOrder.stockId;
						CommonService.genCode(obj).then(
							function(d) {
								vm.dupOrder.code = d;
							});
				    });
				 vm.orderGoodsSearch.orderId = vm.dupOrder.orderId;

			vm.showTabOne = true;
			vm.dupOrder.createdDate = kendo.toString(new Date(),"dd/MM/yyyy");
			vm.checkBoxAutoLoad(vm.dupOrder);
			fillDataTableGoodsListForEditable([]);
			CommonService.populatePopupCreate(teamplateUrl,title,vm.dupOrder,vm,windowId,false,'88%','89%'); 
			
		}
		
		vm.listRemove=[{
			title: "Thao t??c",
		},
		{
			title : "<input type='checkbox' id='checkalllistdraw' name='gridchkselectall' ng-click='vm.chkSelectAll();' ng-model='vm.chkAll' />"
		}];
		
		vm.listConvert=[{
			field:"status",
			data:{
				1:'Ch??a t???o phi???u',
				2:'???? t???o phi???u',
				3:'???? nh???p/xu???t',
				4:'???? h???y',
				5:'???? t??? ch???i',
			}
		},{
			field:"signState",
			data:{
				1:'Ch??a tr??nh k??',
				2:'???? tr??nh k??',
				3:'???? k??',
				4:'???? t??? ch???i'
			}},
			{
			    field: "bussinessType",
			    data:{
			    	
			    	1:'Xu???t cho ????n v??? s??? d???ng',
					2:'Xu???t ra c??ng tr??nh',
					3:'Xu???t ??i BHSC',
					4:'Xu???t lu??n chuy???n kho',
					
					5:'Xu???t t???ng ?????i t??c',
					6:'Xu???t cho ?????i t??c m?????n',
					7:'Xu???t tr??? ?????i t??c',
					8:' Xu???t thanh l??',
					
					9:'Xu???t cho ????? t??i/d??? ??n',
					10:'Xu???t b??n'
			    }
			}
		];
		
		//save copied export request
		vm.saveExportReq = function(){
			var isOK = false;
			var dataGoodsGrid = $('#goodsListGridEditable').data("kendoGrid").dataSource.data();
			var grid = $("#goodsListGridEditable").data("kendoGrid");
			if(dataGoodsGrid.length != 0){
				for(var i = 0; i < dataGoodsGrid.length;i++){
					if(dataGoodsGrid[i].goodsCode == null || dataGoodsGrid[i].goodsName == null 
						 || dataGoodsGrid[i].amount == null || dataGoodsGrid[i].goodsState == null )
						{
							if(dataGoodsGrid[i].goodsCode == null){
								toastr.warning("Ch???a h??ng h??a kh??ng c?? m?? h??ng");
								break;
							}/*else if(dataGoodsGrid[i].goodsName == null){
								toastr.warning("Ch???a h??ng h??a kh??ng c?? t??n c???a m???t h??ng");
								break;
							}else if(dataGoodsGrid[i].goodsUnitName == null){
								toastr.warning("Ch???a h??ng h??a kh??ng c?? t??n ????n v??? t??nh");
								break;
							}*/else if(dataGoodsGrid[i].amount == null){
								toastr.warning("Tr?????ng s??? l?????ng kh??ng ???????c ????? tr???ng");
								grid.editCell(grid.tbody.find('tr:eq(' + i + ')').find("td").eq(4));
								break;
							}else{
								toastr.warning("Ch???a h??ng h??a kh??ng c?? t??nh tr???ng");
								break;
							} 
						}
						
						else{
							if((dataGoodsGrid[i].amount - kendo.parseInt(dataGoodsGrid[i].amount)) != 0){
								toastr.warning("Ch???a h??ng h??a c?? s??? l?????ng y??u c???u kh??ng h???p l???");
								grid.editCell(grid.tbody.find('tr:eq(' + i + ')').find("td").eq(4));
								break;
							}else if(dataGoodsGrid[i].amount <= 0){
										toastr.warning("Tr?????ng s??? l?????ng ph???i l?? s??? nguy??n d????ng");
										grid.editCell(grid.tbody.find('tr:eq(' + i + ')').find("td").eq(4));
										break;
									}else{
								if(i == dataGoodsGrid.length - 1)
										isOK = true;
							}
								
						}
						
				}
				if(isOK == false){
					return;
				}else{
					vm.goodsDataforSaving();	
					createExportRequestManageService.saveImportReq(vm.dupOrder).then(function(d){
						// save goods of order		
					if(d.error){
					toastr.error(d.error);
					return;
					}						
						toastr.success("Sao ch??p th??nh c??ng!");
						vm.doSearch();
						CommonService.closePopup1();
		            }, function(errResponse) {
		            	toastr.error("Sao ch??p th???t b???i");
					});
				}
			}	else{
				/* confirm('Th??m m???i y??u c???u ngay c??? khi kh??ng c?? h??ng h??a b??n trong. B???n ch???c mu???n ti???p t???c thao t??c n??y?', function(){
					exReqManaService.dulplicate(vm.dupOrder).then(function(d){
						// save goods of order
						vm.goodsDataforSaving(d);	
						CommonService.dismissPopup();
		            }, function(errResponse) {
		            	toastr.error("Th??m m???i th???t b???i");
					});
				}); */
				toastr.error("Kh??ng c?? h??ng h??a ????? t???o y??u c???u");
				
			}	
		}
		
		
		
		vm.goodsDataforSaving = function(){
		vm.goodsList = {};
		vm.dupOrder.listOrderGoodsDTO = [];
				var dataGoodsGrid = $('#goodsListGridEditable').data("kendoGrid").dataSource.data();
					for(var i = 0; i<dataGoodsGrid.length;i++){
						 vm.goodsList = {};
						 vm.goodsList.listOrderGoodsDetailDTO = [];
						 var orderGoodDetailCopy = {};
						dataGoodsGrid[i].id=null;
						if(dataGoodsGrid[i].goodsState==1)
							{
							dataGoodsGrid[i].goodsStateName="B??nh th?????ng";
							}
						else
							{
								dataGoodsGrid[i].goodsState=2;
							dataGoodsGrid[i].goodsStateName="H???ng";
							}
						orderGoodDetailCopy.manufacturerName = dataGoodsGrid[i].manufacturerName;
						orderGoodDetailCopy.manufacturerId = dataGoodsGrid[i].manufacturerId;
						orderGoodDetailCopy.producingCountryName = dataGoodsGrid[i].producingCountryName;
						orderGoodDetailCopy.producingCountryId = dataGoodsGrid[i].producingCountryId;
						orderGoodDetailCopy.contractCode  = vm.dupOrder.contractCode;
						orderGoodDetailCopy.serial = dataGoodsGrid[i].serial;
						vm.goodsList = dataGoodsGrid[i];
						
						vm.goodsList.listOrderGoodsDetailDTO = orderGoodDetailCopy;
						
						vm.dupOrder.listOrderGoodsDTO.push(vm.goodsList);
					}
			
		}
		
		
		vm.goodsDataforCopy = function(dataGoods){
		vm.goodsList = {};
		vm.dupOrder.listOrderGoodsDTO = [];
				//var dataGoods = $('#goodsListGridEditable').data("kendoGrid").dataSource.data();
					for(var i = 0; i<dataGoods.length;i++){
						 vm.goodsList = {};
						 vm.goodsList.listOrderGoodsDetailDTO = [];
						 var orderGoodDetailCopy = {};
						dataGoods[i].id=null;
						if(dataGoods[i].goodsState==1)
							{
							dataGoods[i].goodsStateName="B??nh th?????ng";
							}
						else
							{
								dataGoods[i].goodsState=2;
							dataGoods[i].goodsStateName="H???ng";
							}
						orderGoodDetailCopy.manufacturerName = dataGoods[i].manufacturerName;
						orderGoodDetailCopy.manufacturerId = dataGoods[i].manufacturerId;
						orderGoodDetailCopy.producingCountryName = dataGoods[i].producingCountryName;
						orderGoodDetailCopy.producingCountryId = dataGoods[i].producingCountryId;
						orderGoodDetailCopy.contractCode  = vm.dupOrder.contractCode;
						orderGoodDetailCopy.serial = dataGoods[i].serial;
						vm.goodsList = dataGoods[i];
						
						vm.goodsList.listOrderGoodsDetailDTO = orderGoodDetailCopy;
						
						vm.dupOrder.listOrderGoodsDTO.push(vm.goodsList);
					}
			
		}
		
		//export
		vm.listRemove=[{
			title: "Thao t??c",
		},
		{
			title : "<input type='checkbox' id='checkalllistEx' name='gridchkselectall' ng-click='vm.chkSelectAllForExq();' ng-model='vm.chkAllForExReq' />",
		}]
		
		vm.exportExcelGrid = function(){
			vm.orderSearch.page = null;
			vm.orderSearch.pageSize = null;
			exReqManaService.getForExportGrid(vm.orderSearch).then(function(d) {
				CommonService.exportFile(vm.exRegManaGrid,d.data,vm.listRemove,vm.listConvert,"QuanLyYeuCauXuatKho");
			});
			
		}
		
		//7.cancel input data
        vm.cancelest = function(id){
				if(id==="listStatus")
					{
					 $('#listStatus').data("kendoMultiSelect").value([]);
					}
				
        }
  	
		//autoCompleteGoods
		vm.patternOptions1={
			dataTextField: "name",
			placeholder: "Nh???p m?? h??ng, t??n h??ng b??? sung v??o l?????i",
            select: function(e) {
                var dataItem = this.dataItem(e.item.index());
                vm.goods.id = dataItem.goodsId; // th??nh id
                vm.goods.goodsCode = dataItem.code;
                vm.goods.goodsName = dataItem.name;
                vm.goods.goodsUnitName = dataItem.goodsUnitName;
                vm.goods.goodsState = 1;
                vm.goods.goodsStateName = dataItem.goodsStateName;
                vm.goods.orderGoodsId = null;
				vm.goods.manufacturerName = dataItem.manufacturerName;
                vm.goods.manufacturerId = dataItem.manufacturerId;
                vm.goods.producingCountryName= dataItem.producingCountryName;
                vm.goods.producingCountryId = dataItem.producingCountryId;
				vm.goods.isSerial = parseInt(dataItem.isSerial);
                var grid = $("#goodsListGridEditable").data("kendoGrid");
                var gridToCheck =  vm.goodsListGridEditable;
                var check = checkDups( vm.goods);
                if(check){
                	toastr.warning("M???t h??ng ???? t???n t???i trong l?????i!");
                }else{
                	 //grid.dataSource.add(vm.goods);
					 var listGoodsInsert = [];
                	listGoodsInsert.push(vm.goods)
                	 grid.dataSource.insert(listGoodsInsert[0]);
                } 
                
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function(options) {
                        return Restangular.all("goodsRsServiceRest/" + 'getGoodsForOrder').post({pageSize:20, page:1, keySearch:$("#orderGoods2").val()}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
                        });
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
            '<p class="col-md-6 text-header-auto border-right-ccc">M?? h??ng</p>' +
            '<p class="col-md-6 text-header-auto">T??n h??ng</p>' +
            	'</div>',
            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;float:left">#: data.code #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function(e) {
                if (e.sender.value() === '') {
                	vm.goods.goodsId = null; // th??nh id
	                vm.goods.code = null;
	                vm.goods.name = null;
                }
            },
            close: function(e) {
                // handle the event
            	document.getElementById("orderGoods2").value = "";
              }
		};
        
		vm.folder = '';
		
    	function getFolder() {
			Restangular.one(RestEndpoint.ORDER_GOOD_UPLOAD_FOLDER_URL+"/folder").get().then(function(data) {
				vm.folder = data.folder;
			  });
		}
		getFolder();
		
		 vm.submit=submit;
	        function submit(){
        	
        	if($("#file")[0].files[0] == null){
        		toastr.warning("B???n ch??a ch???n file ????? import");
        		return;
        	}
        	if($("#file")[0].files[0].name.split('.').pop() !='xls' && $("#file")[0].files[0].name.split('.').pop() !='xlsx' ){
        		toastr.warning("Sai ?????nh d???ng file");
        		return;
        	}
	        var formData = new FormData();
			formData.append('multipartFile', $('#file')[0].files[0]); 
	     return   $.ajax({
	            url: Constant.BASE_SERVICE_URL +"orderGoodsServiceRest/orderGoods/importGoods?folder="+ vm.folder,
	            type: "POST",
	            data: formData,
	            enctype: 'multipart/form-data',
	            processData: false,
	            contentType: false,
	            cache: false,
	            success:function(data) {
	            	if(data[data.length - 1].lstErrorGoods.length > 0){
	            		vm.lstErrImport = data[data.length - 1].lstErrorGoods;
	            		vm.objectErr = data[data.length - 1];
	            		var teamplateUrl="wms/exportRequestManage/errList.html";
	       			 var title="K???t qu??? Import";
	       			 var windowId="ERR_IMPORT";
	       			
	       			 CommonService.populatePopupCreate(teamplateUrl,title,vm.lstErrImport,vm,windowId,false,'50%','50%');
	       			erroTable(vm.lstErrImport);
	            	}else 
	            	{
	            		data.splice(data.length - 1, 1);
	            		vm.dataRemain = $("#goodsListGridEditable").data().kendoGrid.dataSource.view();
	            		vm.countRemain = vm.dataRemain.length;
		            	var grid = $("#goodsListGridEditable").data("kendoGrid");
		            	grid.dataSource.data([]);
		            	for(var i = 0; i<data.length;i++){
							data[i].isSerial = parseInt(data[i].goodsIsSerial);
		            		data[i].id = i+1;
		            		
		            		data[i].goodsCode = data[i].goodsCode;
		            		data[i].goodsName = data[i].goodsName;
		            		if(data[i].goodsState == "1"){
		            			data[i].goodsStateName = "B??nh th?????ng";
		            		}else if(data[i].goodsState == "2"){
		            			data[i].goodsStateName = "H???ng";
		            		}
		            		
		            			grid.dataSource.add(data[i]);
		            		
		            	}
		            	for(var k = 0; k<data.length;k++){
        					if(data[k].used== 0){
        						grid.dataSource.add(data[k]);
        					}
        				}
        					            		toastr.success("Import th??nh c??ng!");

	            	}
	                
	            }
	        });
	      
        
        }
	       
vm.exportExcelErr =function() {
	        //$("#detailErrGrid").getKendoGrid().saveAsExcel();
				createExportRequestManageService.downloadErrorExcel(vm.objectErr).then(function(d) {
				data = d.plain();
				window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + data.fileName;
			}).catch( function(){
				toastr.error(gettextCatalog.getString("L???i khi export!"));
				return;
			});
	    } 
	    	function checkDups(goodsItem){
				var goodsGrid = $("#goodsListGridEditable").data("kendoGrid");;
	    		var dataItem = null;
	    		var isExisted = false;
	            goodsGrid.table.find("tr").each(function(idx, item) {
	    					var row = $(item);
	    					dataItem = goodsGrid.dataItem(item);
	    					if(goodsItem.goodsCode == dataItem.goodsCode && goodsItem.goodsState == dataItem.goodsState ){
	    						isExisted = true;
	    					}
	    			});
	            if(isExisted == true) return dataItem;
	            else return  null;
			}
	        
	        
	    	var par = {
					parType : "EXPORT_ORDER_TYPE"
			}
	    	
	        exReqManaService.getForExtOrderCheckboxes(par).then(function(d) {
	        	vm.businessTypes = d;
	        	fillDataTableExt([]);
				var obj={};
				obj.data={};
				obj.data[d[0].code]=d[0].name;
				obj.data[d[1].code]=d[1].name;
				obj.data[d[2].code]=d[2].name;
				obj.data[d[3].code]=d[3].name;
				obj.data[d[4].code]=d[4].name;
				obj.data[d[5].code]=d[5].name;
				obj.data[d[6].code]=d[6].name;
				obj.data[d[7].code]=d[7].name;
				obj.data[d[8].code]=d[8].name;
				obj.data[d[9].code]=d[9].name;
				obj.field="bussinessType";
				vm.listConvert.push(obj);
				
				
			});
	  
	    	
	    	   vm.checkBoxAutoLoad = function checkBoxAutoLoad(item){
					vm.showExtForDepartment = false;
					vm.showExtForConstruct = false;
					vm.showExtForBHSC = false;
					vm.showExtAlternativeStock = false;
					vm.showExtForGift = false;
					vm.showExtForBorrow = false;
					vm.showExtForPay = false;
					vm.showExtForSale = false;
					vm.showExtForSell = false;
					vm.showExtForProject = false;
					
					if (item.bussinessType == vm.businessTypes[0].code ) {
				    	vm.showExtForDepartment =  true;
				    	vm.value=0;
				    	$("#extForDepartment").prop("checked", true);
				    }
					if (item.bussinessType == vm.businessTypes[1].code) {
						vm.value=1;
						vm.showExtForConstruct = true;
						$("#extForConstruct").prop("checked", true);
				    }
					if (item.bussinessType == vm.businessTypes[2].code ) {
				  
						vm.showExtForBHSC = true;
						vm.value=2;
						$("#extForBHSC").prop("checked", true);
				    }
					if (item.bussinessType == vm.businessTypes[3].code ) {
				
						vm.showExtAlternativeStock = true;
						vm.value=3;
						$("#extAlternativeStock").prop("checked", true);
				    }
					if (item.bussinessType == vm.businessTypes[4].code ) {
					     
						vm.showExtForGift  = true;
						vm.value=4;
						$("#extForGift").prop("checked", true);
					}
					if (item.bussinessType == vm.businessTypes[5].code ) {
				    
				    	vm.showExtForBorrow = true;
				    	vm.value=5;
				    	$("#extForBorrow").prop("checked", true);
				    }
					if (item.bussinessType == vm.businessTypes[6].code ) {
						vm.showExtForPay = true;
						vm.value=6;
						$("#extForPay").prop("checked", true);
				    }
					if (item.bussinessType == vm.businessTypes[7].code ) {
						vm.showExtForSale = true;
						vm.value=7;
						$("#extForSale").prop("checked", true);
				    }
					if (item.bussinessType == vm.businessTypes[8].code ) {
				        vm.showExtForProject = true;
				        vm.value=8;
				        $("#extForProject").prop("checked", true);
				    }
					if (item.bussinessType == vm.businessTypes[9].code ) {
						vm.showExtForSell = true;
						vm.value=9;
						$("#extForSell").prop("checked", true);
				    }
	}	
	        
	        vm.checkbox = function(){
				vm.showExtForDepartment = false;
				vm.showExtForConstruct = false;
				vm.showExtForBHSC = false;
				vm.showExtAlternativeStock = false;
				vm.showExtForGift = false;
				vm.showExtForBorrow = false;
				vm.showExtForPay = false;
				vm.showExtForSale = false;
				vm.showExtForProject = false;
				vm.showExtForSell = false;

				if (document.getElementById("extForDepartment").checked == true) {
			        // Checkbox has been checked
			    	vm.showExtForDepartment = true;
			    	vm.dupOrder.bussinessType = vm.businessTypes[0].code;
			    }
				if (document.getElementById("extForConstruct").checked == true) {
			        // Checkbox has been checked
			    	vm.showExtForConstruct = true;
			    	vm.dupOrder.bussinessType = vm.businessTypes[1].code;
			    }
				if (document.getElementById("extForBHSC").checked == true) {
			        // Checkbox has been checked
			    	vm.showExtForBHSC = true;
			    	vm.dupOrder.bussinessType = vm.businessTypes[2].code;
			    }
				if (document.getElementById("extAlternativeStock").checked == true) {
			        // Checkbox has been checked
			    	vm.showExtAlternativeStock = true;
			    	vm.dupOrder.bussinessType = vm.businessTypes[3].code;
			    }
				if (document.getElementById("extForGift").checked == true) {
				        // Checkbox has been checked
				    vm.showExtForGift = true;
				    vm.dupOrder.bussinessType = vm.businessTypes[4].code;
				}
				if (document.getElementById("extForBorrow").checked == true) {
			        // Checkbox has been checked
			    	vm.showExtForBorrow = true;
			    	vm.dupOrder.bussinessType = vm.businessTypes[5].code;
			    }
				if (document.getElementById("extForPay").checked == true) {
			        // Checkbox has been checked
			    	vm.showExtForPay = true;
			    	vm.dupOrder.bussinessType = vm.businessTypes[6].code;
			    }
				if (document.getElementById("extForSale").checked == true) {
			        // Checkbox has been checked
			    	vm.showExtForSale = true;
			    	vm.dupOrder.bussinessType = vm.businessTypes[7].code;
			    }
				if (document.getElementById("extForProject").checked == true) {
			    	vm.showExtForProject = true;
			    	vm.dupOrder.bussinessType = vm.businessTypes[8].code;
			    }
				if (document.getElementById("extForSell").checked == true) {
			    	vm.showExtForSell = true;
			    	vm.dupOrder.bussinessType = vm.businessTypes[9].code;
			    }
			}
	        
	        vm.nextStep = function() {
vm.validator.validate();
				vm.isValidInfo = false;
				// trimSpace();
					 if(!vm.validator.validate()){
					 $("#copyExtReqBCIconOne").removeClass("acceptQLK postion-icon");
					 $("#copyExtReqBCIconOne").addClass("declineQLK postion-icon");
					 $("#copyExtReqBCIconThree").removeClass("acceptQLK postion-icon");
					 $("#copyExtReqBCIconThree").addClass("declineQLK postion-icon");
					 if(vm.dupOrder.stockId == null || vm.dupOrder.stockId == undefined){
											$("#orderExportStock").focus();
						}else if(vm.dupOrder.recieverId == null || vm.dupOrder.recieverId == undefined){
											$("#recCUCOP").focus();
						}else if(vm.showExtForDepartment){
							if(vm.dupOrder.deptReceiveName == "" || vm.dupOrder.deptReceiveName == null)
							$("#exportCopyDeptExtForDepartment").focus();
						}else if(vm.showExtForConstruct){
							if(vm.dupOrder.constrCode == "" || vm.dupOrder.constrCode == null)
							$("#exportconstrCodeExtForConstruct").focus();
							else if(vm.dupOrder.reasonId == "" || vm.dupOrder.deptReceiveName == null)
							//$("#cancelReasonName").focus();
							$("#cancelReasonName").data("kendoDropDownList").focus();
							else if(vm.dupOrder.deptReceiveName == "" || vm.dupOrder.deptReceiveName == null)
							$("#exportCopyDeptExtForConstruct").focus();

						}else if(vm.showExtForBHSC){
							if(vm.dupOrder.deptReceiveName == "" || vm.dupOrder.deptReceiveName == null)
							$("#exportCopyDeptExtForBHSC").focus();
						}else if(vm.showExtAlternativeStock){
							if(vm.dupOrder.ieStockName == "" || vm.dupOrder.ieStockName == null)
							$("#orderIEStockExtAlternativeStock").focus();
						}else if(vm.showExtForGift){
							if(vm.dupOrder.partnerName == "" || vm.dupOrder.partnerName == null)
							$("#extForGiftCodeExtForGift").focus();
						}else if(vm.showExtForBorrow){
							if(vm.dupOrder.partnerName == "" || vm.dupOrder.partnerName == null)
							$("#extForGiftCodeExtForBorrow").focus();
						}else if(vm.showExtForPay){
							if(vm.dupOrder.partnerName == "" || vm.dupOrder.partnerName == null)
							$("#extForGiftCodeExtForPay").focus();
						}else if(vm.showExtForSale){
							if(vm.dupOrder.deptReceiveName == "" || vm.dupOrder.deptReceiveName == null)
							$("#exportCopyDeptExtForSale").focus();
						}else if(vm.showExtForProject){
							if(vm.dupOrder.projectCode == "" || vm.dupOrder.projectCode == null)
							$("#extForProjectCodeExtForProject").focus();
						}else if(vm.showExtForSell){
							if(vm.dupOrder.deptReceiveName == "" || vm.dupOrder.deptReceiveName == null)
							$("#exportCopyDeptExtForSell").focus();
						}
					 return;
				}
				$("#copyExtReqBCIconOne").removeClass("declineQLK postion-icon");
				 $("#copyExtReqBCIconOne").addClass("acceptQLK postion-icon");
				 $("#copyExtReqBCIconThree").removeClass("declineQLK postion-icon");
				 $("#copyExtReqBCIconThree").addClass("acceptQLK postion-icon");
				if(vm.dupOrder.stockName.length != 0 && vm.dupOrder.recieverName.length != 0 &&  vm.errMessage2 ==  ''){
					if(vm.dupOrder.bussinessType == vm.businessTypes[0].code ||
							vm.dupOrder.bussinessType == vm.businessTypes[2].code ||
							vm.dupOrder.bussinessType == vm.businessTypes[7].code ||
							vm.dupOrder.bussinessType == vm.businessTypes[9].code ){
						
						if(vm.dupOrder.deptReceiveName.length != 0 || vm.dupOrder.deptReceiveName != null)
							{
								vm.isValidInfo = true;
							}
						
					}else if(vm.dupOrder.bussinessType == vm.businessTypes[4].code ||
							vm.dupOrder.bussinessType == vm.businessTypes[5].code ||
							vm.dupOrder.bussinessType == vm.businessTypes[6].code){
						
						if(vm.dupOrder.partnerName.length != 0)
						{
								vm.isValidInfo = true;
						}
						
					}else if(vm.dupOrder.bussinessType == vm.businessTypes[3].code){
						if(vm.dupOrder.ieStockName.length != 0)
						{	
							vm.isValidInfo = true;
						}
					}else if(vm.dupOrder.bussinessType == vm.businessTypes[8].code){
						if(vm.dupOrder.projectCode.length != 0)
						{	
							vm.isValidInfo = true;
						}
					}else{
						if((vm.dupOrder.constrCode.length != 0) 
								&& (vm.dupOrder.reasonId.length != 0) && 
								(vm.dupOrder.deptReceiveName.length != 0))
						{
							vm.isValidInfo = true;
						}
					}
				}
					
				if(vm.isValidInfo == false )	{
					
					vm.showTabOne = true;
				}
				else{
						vm.showTabOne = false;
						vm.fixedTableShow = true;vm.editTableShow = false;
						vm.orderGoodsSearch.orderId = vm.dupOrder.orderId;
						fillDataTableGoodsList([]);
						vm.disableImport = false;
						vm.disableOptions = false;
						//document.getElementById("goods").readOnly = true;
				
				}
				
			}
			
			vm.prevStep = function() {
				vm.showTabOne = true;
			}
			
	        function fillListGoods(data)
			{
				vm.listGoodGridOptions = kendoConfig.getGridOptions({
					sortable: true,	                
					dataSource:{
						serverPaging: true,
						error: function(e) {
						    toastr.warning("Kh??ng t??m th???y h??ng h??a t????ng ???ng");
						  },
						 schema: {
							 total: function (response) {
									return response.total; // total is returned in
															// the "total" field of
															// the response
								},
								data: function (response) {
						for(var i = 0; i<response.data.length;i++){
							response.data[i].isSerial = parseInt(response.data[i].goodsIsSerial);
		            	}
									return response.data;
								},
								errors: "error" 
			                },
						transport: {
							read:  {
			                  // Thuc hien viec goi service
							  url: Constant.BASE_SERVICE_URL +"orderPatternGoodServiceRest/getPatternGoodsByOrderPatternId", 
						  contentType:"application/json; charset=utf-8",
						  type: "POST"
						 
							},					
							parameterMap: function (options, type) {
								
								    vm.orderPatternGood.page = options.page;
									vm.orderPatternGood.pageSize = options.pageSize;
									return JSON.stringify(vm.orderPatternGood);

							}
						},					 
						pageSize: 10
					},
					// dataSource: data,
					noRecords: true,
					columnMenu: false,
					scrollable:false,
					messages: {
						noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
					},
					pageable: {
						refresh: false,
						 pageSizes: [10, 15, 20, 25],
						messages: {
			                display: "{0}-{1} c???a {2} k???t qu???",
			                itemsPerPage: "k???t qu???/trang",
			                empty: "Kh??ng c?? k???t qu??? hi???n th???"
			            }
					},
					columns: [
						{
							title : "<input type='checkbox' id='checkalllistdraw' name='gridchkselectall' ng-click='caller.chkSelectAll();' ng-model='caller.chkAll' />",
							template: "<input type='checkbox' id='childcheck' name='gridcheckbox' ng-click='vm.onCheckForExReq($event)'/>",
					        width: 35,
					        headerAttributes: {style:"text-align:center;"},
							attributes:{style:"text-align:center;"}
						}
						,{
						title: "TT",
						field:"stt",
				        template: dataItem => $("#listGoods").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
				        width: 70,
				        headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:center;"
						},
					}
					,  {
						title: "M?? h??ng",
						field: 'goodsCode',
				        width: 100,
				        headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:left;"
						},
					}, {
						title: "T??n h??ng",
				        field: 'goodsName',
				        width: 300,
				        headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:left;"
						},
					}, {
						title: "????n v??? t??nh",
				        field: 'goodsUnitName',
				        width: 100,
				        headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:left;"
						},
					}, {
						title: "S??? l?????ng",
				        field: 'amount',
				        width: 100,
				        headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:right;"
						},
					},]
				});
			}
			vm.orderPatternUser = {};
			function fillListPatternGoods(data)
			{
				vm.patternGoodGridOptions = kendoConfig.getGridOptions({
					
					change: function() {
				          //var gridDetails = $("#listGoods").data("kendoGrid");
			        var dataItem = this.dataItem(this.select());
					vm.orderPatternGood.orderPatternId = dataItem.orderPatternId;
					var grid = $("#listGoods").data("kendoGrid");	
			if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 10
				});
			} 
				         // gridDetails.dataSource.filter({ field: "orderPatternId", value: dataItem.orderPatternId, operator: "eq" });
					  		  //document.getElementById('patterName').value = dataItem.name;
					  		 //document.getElementById('personCreate').value = dataItem.createdUserName;
					  		//document.getElementById('notes').innerHTML = dataItem.description;
vm.orderPatternUser.name = dataItem.name;
vm.orderPatternUser.createdUserName = dataItem.createdUserName;
vm.orderPatternUser.description = dataItem.description;
				    },
				    	dataBound: function(e) {
								    					
				    								   var grid = $("#listGoods").data("kendoGrid");
					    							   var tr = $("#listDetailGoods").data("kendoGrid").select("tr:eq(0)");
					    							   var dataItem = $("#listDetailGoods").data("kendoGrid").dataItem(tr);
					    							    /* grid.dataSource.filter({ field: "orderPatternId", value: dataItem.orderPatternId, operator: "eq" });  
					    							    document.getElementById('patterName').value = dataItem.name;
					    						  		 document.getElementById('personCreate').value = dataItem.createdUserName;
					    						  		document.getElementById('notes').innerHTML = dataItem.description; */
				    	},
					autoBind: true,
					resizable: true,			 
					dataSource:{
						serverPaging: true,
						 schema: {
							 total: function (response) {
									vm.totalSample = response.total;
									return response.total;
								},
								data: function (response) {
									return response.data;
								},
			                },
						transport: {
							read: {
							url: Constant.BASE_SERVICE_URL +"orderPatternServiceRest/doSearch", 
							contentType:"application/json; charset=utf-8",
							type: "POST"
						 
							},					
							parameterMap: function (options, type) {
								
								    vm.orderPattern.page = options.page;
									vm.orderPattern.pageSize = options.pageSize;
									return JSON.stringify(vm.orderPattern);
							}
						},	
						pageSize: 10				 
					},
					columnMenu: false,
					noRecords: true,
					pageable: {
					refresh: false,
					 pageSizes: [10, 15, 20, 25],
					messages: {
		                display: "{0}-{1} c???a {2} k???t qu???",
		                itemsPerPage: "k???t qu???/trang",
		                empty: "Kh??ng c?? k???t qu??? hi???n th???"
		            }
				},
					messages: {
						noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
					},
					
					columns: [{
						title: "TT",
						field:"stt",
				        template: dataItem => $("#listDetailGoods").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
				        width: 40,
				        headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:center;"
						},
					}
					,  {
						title: "T??n m???u",
						field: 'name',
				        width: 150,
				        headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:left;"
						},
					}]
				});
			}
			
			vm.removeLine = removeLine;
			function removeLine(dataItem)
			{
				 var grid = $('#goodsListGridEditable')
					.data("kendoGrid").dataSource
					.data();
					     for(var i=0;i<grid.length;i++)
						    	 {
						    	   if(grid[i].goodsCode==dataItem.goodsCode && grid[i].goodsId==dataItem.goodsId)
						    		   {
						    		   grid.splice(i,1);
						    		   
										return;
						    		   }
						    	 }
					
			}
			
			function fillDetailTable(data) {
 			var dataSource = new kendo.data.DataSource({
                pageSize: 10,
                data: data,
                autoSync: true,    
                
            });
				vm.detailGridOptions = kendoConfig.getGridOptions({
					autoBind: true,
					resizable: true,	
					columnMenu: false,		 
					dataSource: dataSource,
					noRecords: true,
					messages: {
						noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
					},
					pageable: {
						refresh: false,
						 pageSizes: [10, 15, 20, 25],
						messages: {
			                display: "{0}-{1} c???a {2} k???t qu???",
			                itemsPerPage: "k???t qu???/trang",
			                empty: "Kh??ng c?? k???t qu??? hi???n th???"
			            }
					},
					columns: [
							{
								title: "TT",
								field:"stt",
						        template: dataItem => $("#viewDetailGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
						        width: "10%",
						        headerAttributes: {
									style: "text-align:center;"
								},
								attributes: {
									style: "text-align:center;"
								},
							}
							, {
								title: "Serial",
								field: 'serial',
						        width: "20%",
						        headerAttributes: {
									style: "text-align:center;"
								},
								attributes: {
									style: "text-align:left;"
								},
							}, {
								title: "M?? h???p ?????ng",
						        field: 'contractCode',
						        width: "20%",
						        headerAttributes: {
									style: "text-align:center;"
								},
								attributes: {
									style: "text-align:left;"
								},
							}, {
								title: "Part number",
						        field: 'partNumber',
						        width: "10%",
						        headerAttributes: {
									style: "text-align:center;"
								},
								attributes: {
									style: "text-align:left;"
								},
							},  {
								title: "H??ng s???n xu???t",
								 field: 'manufacturerName',
						        width: "15%",
						        headerAttributes: {
									style: "text-align:center;"
								},
								attributes: {
									style: "text-align:left;"
								},
							},  {
								title: "N?????c s???n xu???t",
								 field: 'producingCountryName',
						        width: "15%",
						        headerAttributes: {
									style: "text-align:center;"
								},
								attributes: {
									style: "text-align:left;"
								},
							},  {
								title: "V??? tr??",
								 field: 'cellCode',
						        width: "10%",
						        headerAttributes: {
									style: "text-align:center;"
								},
								attributes: {
									style: "text-align:left;"
								},
							}]
					});
				}	
			
			vm.showPatternGoods = showPatternGoods;
			function showPatternGoods()
			{
				 var teamplateUrl="wms/exportRequestManage/listPatternGood.html";
				    var title="Danh s??ch m???u h??ng h??a trong y??u c???u xu???t kho";
				    var windowId="PATTERN";
				    CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,false,'90%','75%');
			}
			
			vm.validateDate = validateDate;
			function validateDate(id){
					if(id == 'checkErr1'){
					var startDate = $('#exReqManaCreateFromDate').val();  
					var endDate = $('#exReqManaCreateToDate').val();
					vm.errMessage = '';
					vm.errMessage1 = '';
					var curDate = new Date();
					if(kendo.parseDate(startDate,"dd/MM/yyyy") > curDate){
				           vm.errMessage1 = 'Ng??y t???o ph???i nh??? h??n b???ng ng??y hi???n t???i';
				           $("#exReqManaCreateFromDate").focus();
				           return vm.errMessage1;
				        }
			        if(endDate !== ""){
					if(kendo.parseDate(startDate,"dd/MM/yyyy") > kendo.parseDate(endDate,"dd/MM/yyyy")){
			          vm.errMessage1 = 'Ng??y t???o ph???i nh??? h??n b???ng ng??y ?????n';
			          $("#exReqManaCreateFromDate").focus();
			          return vm.errMessage1;
			        }
					}
			        
				        else if(startDate <= curDate){
					           vm.errMessage1 = '';
							   vm.errMessage = '';
					           return vm.errMessage1;
					        }
if(!vm.validator2.validate()){
			$("#exReqManaCreateFromDate").focus();
			return;
		}
				}
				else if(id == 'checkErr'){
					var startDate = $('#exReqManaCreateFromDate').val();
					var endDate = $('#exReqManaCreateToDate').val();
					var curDate = new Date();
					if(endDate == undefined){
					endDate = null;
					}
					if(startDate == undefined){
					startDate = null;
					}
					vm.errMessage = '';
					vm.errMessage1 = '';
					// var d = new Date();
					// var curDate = d.getDate()  + "/" + d.getMonth() + "/" + d.getFullYear() ;
			        if(endDate !== ""){
			        if(kendo.parseDate(startDate,"dd/MM/yyyy") > kendo.parseDate(endDate,"dd/MM/yyyy")){
			          vm.errMessage = 'Ng??y t???o ph???i nh??? h??n b???ng ng??y ?????n';
			          $("#exReqManaCreateToDate").focus();
			          return vm.errMessage;
			        }if(kendo.parseDate(endDate,"dd/MM/yyyy") > curDate){
				           vm.errMessage = 'Ng??y ?????n ph???i nh??? h??n ng??y hi???n t???i';
				           $("#exReqManaCreateToDate").focus();
				           return vm.errMessage;
				        }
}
			        else if(kendo.parseDate(startDate,"dd/MM/yyyy") <= kendo.parseDate(endDate,"dd/MM/yyyy")){
			          vm.errMessage = '';
					  vm.errMessage1 = '';
			          return vm.errMessage;
			        }
				} else{
					var recDate = $('#receiveDate').val();
					if(recDate.length != 0){
						var curDate = new Date();
				        
				        if(kendo.parseDate(recDate,"dd/MM/yyyy") == null){
				        	
				        	vm.errMessage2 = 'Ng??y nh???n h??ng d??? ki???n kh??ng h???p l???';
				        	$('#receiveDate').focus();
				        	return vm.errMessage2;
				        }
				        	
				        else {
				        	if(kendo.parseDate(recDate,"dd/MM/yyyy") > (curDate - 1)){	
				        		vm.errMessage2 = '';
				        		$('#receiveDate').focus();return vm.errMessage2;}
				        	else{
				        		vm.errMessage2 = 'Ng??y nh???n h??ng d??? ki???n ph???i l???y t??? ng??y hi???n t???i tr??? ??i';
					        	$('#receiveDate').focus();
					        	return vm.errMessage2;
				        	}
				        }
					}else{
						vm.errMessage2 = '';
						return vm.errMessage2;
					}
				}
			}
			
				
			
			
			vm.exportOrderWithSerial=function(){
				var obj={};
	         	obj.orderId=vm.order.orderId;
	         	obj.reportType="DOC";
	         	obj.reportName="ThongTinYeuCauXuat_CoSerial";
	         	CommonService.exportReport(obj).then(
						function(data) {
						var binarydata= new Blob([data],{type: "text/plain;charset=utf-8"});
				        kendo.saveAs({dataURI: binarydata, fileName: "YeuCauCoSerial" + '.docx'});
					}, function(errResponse) {
						toastr.error("L???i kh??ng export DOC ???????c!");
					});
			}
			
			vm.exportOrderWithoutNonSerial=function(){
				var obj={};
	         	obj.orderId=vm.order.orderId;
	         	obj.reportType="DOC";
	         	obj.reportName="ThongTinYeuCauXuat_KhongSerial";
	         	CommonService.exportReport(obj).then(
						function(data) {
						var binarydata= new Blob([data],{type: "text/plain;charset=utf-8"});
				        kendo.saveAs({dataURI: binarydata, fileName: "YeuCauKhongSerial" + '.docx'});
					}, function(errResponse) {
						toastr.error("L???i kh??ng export DOC ???????c!");
					});
			}
			
			vm.downloadImportTemplate = function(){
	        	CommonService.downloadTemplate('XK_Import_HangHoa').then(function(d) {
					data = d.plain();
					window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + data.fileName;
				}).catch( function(){
					toastr.error(gettextCatalog.getString("L???i khi export!"));
					return;
				});;
	        }

		vm.onClear = function(comboId){
		switch(comboId){
		case 'orderStock':{
				vm.orderSearch.listStockId=[];
				vm.orderSearch.stockName=null;
				 $('#' + comboId).val("");
				break;
				}
			
		case 'orderCreator10':{
				vm.orderSearch.createdBy=null;
				vm.orderSearch.createdByName=null;
				 $('#' + comboId).val("");
				break;
				}
case 'orderExportStock':{
				vm.dupOrder.stockId=null;
				vm.dupOrder.stockName=null;
				 $('#' + comboId).val("");
				break;
				}
case 'recCUCOP':{
				vm.dupOrder.recieverId=null;
				vm.dupOrder.recieverName=null;
				 $('#' + comboId).val("");
				break;
				}
			}
		
		}
        
	}
})();
									
				
