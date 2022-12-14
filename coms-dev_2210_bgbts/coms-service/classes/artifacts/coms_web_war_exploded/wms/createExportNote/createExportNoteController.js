(function() {
	'use strict';
	var controllerId = 'deliveryorderController';
	
	angular.module('MetronicApp').controller(controllerId, deliveryorderController);

	
	
	// 1.show/hide column field
	
	// 2.choose file create order
	function deliveryorderController($scope, $rootScope, $timeout, gettextCatalog, 
			kendoConfig, $kWindow,
			CommonService, PopupConst, Restangular, RestEndpoint,Constant,createExNoteService,createExportRequestManageService) {
		var vm = this;
		 vm.order={};
		vm.orderSearch ={};
		//thong tin tim kiem khi khoi tao man hinh
		vm.orderSearch ={ 
		createdBy:Constant.user.vpsUserToken.sysUserId, 
		createdByName:Constant.user.vpsUserToken.fullName,
		createdDeptedId:Constant.user.VpsUserInfo.departmentId,
		createdDeptedName:Constant.user.VpsUserInfo.departmentName,
		listStockId : [],
		listStatus : ['1'],
		listSignState : ['1']
		};
		//vm.orderSearch.listStockId = [];
		vm.orderGoodsSearch ={};
		vm.orderGoodsDetailSearch ={};
		vm.showSearch = true;
		vm.showDetail = false;
		vm.showTabOne = true;
		vm.orderGoodsSerialSearch = {};
		vm.showAdvancedSearch = false;
		vm.dataList=[];
		//vm.orderSearch.listStatus = ['1'];
		//vm.orderSearch.listSignState = ['1'];
		vm.folder = '';
		vm.orderForNote ={};
		vm.showTabOne = true;
		vm.disableImport = false;
		vm.bt1 = false; vm.bt2 = true; vm.bt3 = true; vm.bt4 = false;
		// datetime
	/* 	var d = new Date();
		var datestring = d.getDate()  + "/" + d.getMonth() + "/" + d.getFullYear() ;
		vm.orderSearch.createdDateFrom = datestring;
		vm.orderSearch.createdDateTo = null; */
	vm.orderSearch.createdDateFrom=kendo.toString(new Date((new Date()).getTime()-30*24*3600*1000),"dd/MM/yyyy")
		vm.listGoodsAmountZero = [];
		/* vm.orderSearch.createdBy=Constant.user.vpsUserToken.sysUserId; 
		vm.orderSearch.createdByName=Constant.user.vpsUserToken.fullName;
		vm.orderSearch.createdDeptedId=Constant.user.VpsUserInfo.departmentId;
		vm.orderSearch.createdDeptedName=Constant.user.VpsUserInfo.departmentName; */

		vm.gotoTabOnePopUp = function(){
			vm.showTabOne = true;
			vm.bt1 = false; vm.bt2 = true; vm.bt3 = true; vm.bt4 = false;
			
		}
		vm.gotoTabTwoPopUp = function(){
			vm.showTabOne = false;
			vm.bt1 = true; vm.bt2 = false; vm.bt3 = false; vm.bt4 = true;
		}
		vm.advancedSearch =function(){
			vm.showAdvancedSearch = !vm.showAdvancedSearch;
		}
		
		vm.appParams={};
		vm.appParams.parType = 'EXPORT_ORDER_TYPE';
		vm.appParams.page = 1;
		vm.appParams.pageSize = 10;
		if($rootScope.stringtile){
			vm.String=$rootScope.stringtile;
			}
		
		// su kien an nut enter tim kiem	
		$(document).on("keydown", function (e) {
        if (e.keyCode === 13) {
        	$("#findCreExt").click();
        }
        });
    	setTimeout(function(){
			  $("#orderCodeExNote").focus();
			},15);
    	createExNoteService.getForExtOrderCheckboxes(vm.appParams).then(function(d) {
        	vm.businessTypes = d.data;
        	fillDataTable([]);
        
			var obj={};
			obj.data={};
			obj.data[d.data[0].code]=d.data[0].name;
			obj.data[d.data[1].code]=d.data[1].name;
			obj.data[d.data[2].code]=d.data[2].name;
			obj.data[d.data[3].code]=d.data[3].name;
			obj.data[d.data[4].code]=d.data[4].name;
			obj.data[d.data[5].code]=d.data[5].name;
			obj.data[d.data[6].code]=d.data[6].name;
			obj.data[d.data[7].code]=d.data[7].name;
			obj.data[d.data[8].code]=d.data[8].name;
			obj.field="bussinessType";
			vm.listConvert.push(obj);
			
			// if (item.bussinessType == vm.businessTypes[0].code ) {
		    	// $("#extForDepartment").prop("checked", true);
		    // }
			// if (item.bussinessType == vm.businessTypes[1].code) {
		    	// $("#extForConstruct").prop("checked", true);
		    // }
			// if (item.bussinessType == vm.businessTypes[2].code ) {
		    	// $("#extForBHSC").prop("checked", true);
		    // }
			// if (item.bussinessType == vm.businessTypes[3].code ) {
		    	// $("#extAlternativeStock").prop("checked", true);
		    // }
			// if (item.bussinessType == vm.businessTypes[4].code ) {
			    // $("#extForGift").prop("checked", true);
			// }
			// if (item.bussinessType == vm.businessTypes[5].code ) {
		    	// $("#extForBorrow").prop("checked", true);
		    // }
			// if (item.bussinessType == vm.businessTypes[6].code ) {
		    	// $("#extForPay").prop("checked", true);
		    // }
			// if (item.bussinessType == vm.businessTypes[7].code ) {
		    	// $("#extForSale").prop("checked", true);
		    // }
			// if (item.bussinessType == vm.businessTypes[8].code ) {
				// $("#extForProject").prop("checked", true);
		    // }
			// if (item.bussinessType == vm.businessTypes[9].code ) {
				// $("#extForSell").prop("checked", true);
		    // }
			
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
						
						if (item.bussinessType === vm.businessTypes[0].code ) {
					    	vm.showExtForDepartment =  true;vm.value=0;
					    	$("#extForDepartment").prop("checked", true);
					    }
						if (item.bussinessType === vm.businessTypes[1].code) {
					  
							vm.showExtForConstruct = true;vm.value=1;
					    	$("#extForConstruct").prop("checked", true);
					    }
						if (item.bussinessType === vm.businessTypes[2].code ) {
					  
							vm.showExtForBHSC = true;vm.value=2;
					    	$("#extForBHSC").prop("checked", true);
					    }
						if (item.bussinessType === vm.businessTypes[3].code ) {
					
							vm.showExtAlternativeStock = true;vm.value=3;
					    	$("#extAlternativeStock").prop("checked", true);
					    }
						if (item.bussinessType === vm.businessTypes[4].code ) {
						     
							vm.showExtForGift  = true;vm.value=4;
						    $("#extForGift").prop("checked", true);
						}
						if (item.bussinessType === vm.businessTypes[5].code ) {
					    
					    	vm.showExtForBorrow = true;vm.value=5;
					    	$("#extForBorrow").prop("checked", true);
					    }
						if (item.bussinessType === vm.businessTypes[6].code ) {
					     
							vm.showExtForPay = true;vm.value=6;
					    	$("#extForPay").prop("checked", true);
					    }
						if (item.bussinessType === vm.businessTypes[7].code ) {
							vm.showExtForSale = true;vm.value=7;
					    	$("#extForSale").prop("checked", true);
					    }
						if (item.bussinessType === vm.businessTypes[8].code ) {
					        vm.showExtForProject = true;vm.value=8;
							$("#extForProject").prop("checked", true);
					    }
						if (item.bussinessType === vm.businessTypes[9].code ) {
							vm.showExtForSell = true;vm.value=9;
							$("#extForSell").prop("checked", true);
					    }
		}	
        
	    vm.orderSearch.departmentpopUp = {};
		vm.openDepartmentCreate=openDepartmentCreate
		
		function openDepartmentCreate(popUpId){
			vm.obj={};vm.orderSearch.departmentpopUp = popUpId;
			CommonService.getDepartment(vm.obj).then(function(result){
				var templateUrl = 'wms/popup/findDepartmentPopUp.html';
				var title = gettextCatalog.getString("T??m ki???m ????n v???");
				var data1=result.plain();
				vm.gridOptions = kendoConfig.getGridOptions({
					autoBind: true,
					resizable: true,
					dataSource: result,
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
					columns:[ {
						title: "STT",
						field: "stt",
						template: dataItem => $("#gridView").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
						width: 70
						},{
						title: "M?? ph??ng ban",
						field: "code",
						width: 150
					}, {
						title: "T??n ph??ng ban",
						field: "text",
						width: 210
					}, {
						title: "????n v??? cha",
						field: "parentName",
						width: 210
					},{
						title: "Ng??y hi???u l???c",
						field: "effectDate",
						template: '#: kendo.toString(kendo.parseDate(effectDate), "dd/MM/yyyy") #',
						width: 140
					},{
						title: "Ng??y h???t hi???u l???c",
						field: "endDate",
						template: '#: kendo.toString(kendo.parseDate(endDate), "dd/MM/yyyy") #',
						width: 150
					}]
				});
				
				CommonService.populatePopupDept(templateUrl, title, vm.gridOptions,data1, vm, popUpId , 'string', false, '91%','89%');
			});
		}
		
		
		vm.openDepartmentTo=openDepartmentTo
		function openDepartmentTo(popUpId){
			vm.obj={};vm.orderSearch.departmentpopUp = popUpId;
			CommonService.getDepartment(vm.obj).then(function(result){
				var templateUrl = 'wms/popup/findDepartmentPopUp.html';
				var title = gettextCatalog.getString("T??m ki???m ????n v???");
				var data1=result.plain();
				vm.gridOptions1 = kendoConfig.getGridOptions({
					autoBind: true,
					resizable: true,
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
					columns:[ {
						title: "STT",
						field: "stt",
						template: dataItem => $("#gridView").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
						width: 70
						},{
						title: "M?? ph??ng ban",
						field: "code",
						width: 150
					}, {
						title: "T??n ph??ng ban",
						field: "text",
						width: 210
					}, {
						title: "????n v??? cha",
						field: "parentName",
						width: 210
					},{
						title: "Ng??y hi???u l???c",
						field: "effectDate",
						width: 140
					},{
						title: "Ng??y h???t hi???u l???c",
						field: "endDate",
						width: 150
					}]
				});
				CommonService.populatePopupDept(templateUrl, title, vm.gridOptions1,data1, vm, popUpId, 'string', false, '92%','89%');
			});
		}

		vm.onSave=onSave;
		function onSave(data){
			if(vm.orderSearch.departmentpopUp === 'depOpenCreate'){
				vm.orderSearch.createdDeptedName = data.text;
				vm.orderSearch.createdDeptedId = data.id;
			}		else{
				vm.orderSearch.deptReceiveName = data.text;
				vm.orderSearch.deptReceiveId = data.id;
			}
		}
		//autocomplete tim kiem don vi tao
		vm.selectedDept = false;
		vm.deprtOptions = {
  	            dataTextField: "text",
				dataValueField:"id",
  	            select: function(e) {
				vm.selectedDept = true;
  	                var dataItem = this.dataItem(e.item.index());
  	             // vm.shipmentSearch.createdDeptCode = dataItem.code; // th??nh id
  	            vm.orderSearch.createdDeptedName = dataItem.text;//th??nh name
  	          vm.orderSearch.createdDeptedId = dataItem.id;//th??nh name
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
				 headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
            '<p class="col-md-6 text-header-auto border-right-ccc">M?? ????n v???</p>' +
            '<p class="col-md-6 text-header-auto">T??n ????n v???</p>' +
            	'</div>',
  	            template:'<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
  	            change: function(e) {
  	                if (e.sender.value() === '') {
  	                	vm.orderSearch.createdDeptedName = null;
  	                	vm.orderSearch.createdDeptedId = null;
  	                }
  	            },
  	            ignoreCase: false
  	        };
	    vm.selectedDept2 = false;
		vm.deprtOptions1 = {
  	           dataTextField: "text",
				dataValueField:"id",
  	            select: function(e) {
				vm.selectedDept2 = true;
  	                var dataItem = this.dataItem(e.item.index());
  	             // vm.shipmentSearch.createdDeptCode = dataItem.code; // th??nh id
  	              vm.orderSearch.deptReceiveName = dataItem.text;//th??nh name
  	              vm.orderSearch.deptReceiveId = dataItem.id;//th??nh name
  	              
  	            },
  	            pageSize: 10,
				open: function(e) {
	                        vm.selectedDept2 = false;
	                    },
  	            dataSource: {
  	                serverFiltering: true,
  	                transport: {
  	                    read: function(options) {
						vm.selectedDept2 = false;
  	                        return Restangular.all("departmentServiceRest/department/" + 'getForAutocompleteDept').post({name:vm.orderSearch.deptReceiveName,pageSize:vm.deprtOptions1.pageSize}).then(function(response){
  	                            options.success(response);
  	                        }).catch(function (err) {
  	                            console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
  	                        });
  	                    }
  	                }
  	            },
				 headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
					'<p class="col-md-6 text-header-auto border-right-ccc">M?? ????n v???</p>' +
					'<p class="col-md-6 text-header-auto">T??n ????n v???</p>' +
            	'</div>',
  	            template:'<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
  	            change: function(e) {
  	                if (e.sender.value() === '') {
  	                	vm.orderSearch.deptReceiveName = null; vm.orderSearch.deptReceiveId = dataItem.id;//th??nh name
  	                }
  	            },
  	            ignoreCase: false
  	        };
	    
		//clear data if wrong input for autocomplete
			vm.changeDataAuto=changeDataAuto
		function changeDataAuto(id){
		switch(id){
		case 'creExtNoteCreDept':{
			if(processSearch(id,vm.selectedDept)){
				vm.orderSearch.createdDeptedName = null;
				vm.orderSearch.createdDeptedId = null;
			  vm.selectedDept=false;	
			 }
					break;
					}
					
		case 'creExtNoteToDept':{
			if(processSearch(id,vm.selectedDept2)){
				vm.orderSearch.deptReceiveName = null;
				vm.orderSearch.deptReceiveId = null;
			  vm.selectedDept2=false;	
			 }
					break;
					}
		
		}
		}
		
		vm.cancelCreatedByName = function()
		{
			vm.userSearch.loginName= undefined;
		}
		vm.cancelCreptedDepted = function(screen){
			vm.orderSearch.createdDeptedName = undefined;
			vm.orderSearch.createdDeptedId = undefined;
		}
		
		vm.cancelDeptReceive = function(screen)
		{
				vm.orderSearch.deptReceiveName = undefined;
				vm.orderSearch.deptReceiveId = undefined;
		}
		
		$scope.$watch('vm.orderForNote.stockId', function() {
			var obj={};
			obj.value="PXK";
			obj.orgValue="AAA";
			obj.stockId="";
			obj.stockId= vm.orderForNote.stockId;
				CommonService.genCode(obj).then(
						function(d) {
							vm.orderForNote.code = d;
						});
	    });

		vm.validatorOptions = kendoConfig.get('validatorOptions');
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
		
		vm.gridCommon = [{
			   title: "TT",
			   field: "#",
			         template: dataItem => $("#gridView").data("kendoGrid").dataSource.indexOf(dataItem) + 1 ,
			   width: 40,
			         filterable: true,
			         headerAttributes: {style: "text-align:center;"},
			   attributes: {style: "text-align:center;"},
			  }, {
			   title: "M?? kho",
			   field: "code",
			   headerAttributes: {style: "text-align:center;"},
			   attributes: {style: "text-align:left;"},
			   width: 80
			  }, {
			   title: "T??n kho",
			   field: "name",
			   headerAttributes: {style: "text-align:center;"},
			   attributes: {style: "text-align:left;"},
			   width: 150
			  },{
			   title: "????n v??? qu???n l??",
			   field: "name",
			   headerAttributes: {style: "text-align:center;"},
			   attributes: {style: "text-align:left;"},
			   width: 100
			  }];

		initFormData();
		function initFormData() {
			erroTable([]);
			//fillDataTableGoodsDetailPopUp([]);
			fillDataTableGoodsListPopup([]);
			// fillDataTableInfoDeliveryOrder([]);
			
		}
		var record=0;
		function fillDataTable(data) {
			vm.creExpNoteGridOptions = kendoConfig.getGridOptions({
				autoBind: true,
				resizable: true,	
				toolbar: [
                    {
                        name: "actions",
                        template:
                      	 /*  '<div class="btn-group pull-right margin_top_button margin10">'+
                    '<i data-toggle="dropdown" aria-expanded="false"><i uib-tooltip="C??i ?????t hi???n th???" class="fa fa-cog" aria-hidden="true"></i></i>'+
                    '<i uib-tooltip="Xu???t ra Excel" class="action-button excelQLK" ng-click="vm.exportExcelGrid()" aria-hidden="true"></i>'+  '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
                    '<label ng-repeat="column in vm.creExpNoteGrid.columns| filter: vm.gridColumnShowHideFilter">'+
                    '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+
                    '</label>'+                    
                    '</div></div>' */
					'<div class="btn-group pull-right margin_top_button margin_right10">'+
	                      	 '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">C??i ?????t</span><i class="fa fa-cog" aria-hidden="true"></i></i>'+
	                      	'<i class="tooltip1 action-button excelQLK" ng-click="vm.exportExcelGrid()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xu???t Excel</span></i>'+
		                    '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
		                    '<label ng-repeat="column in vm.creExpNoteGrid.columns | filter: vm.gridColumnShowHideFilter">'+
		                    '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+
		                    '</label>'+
		                    '</div></div>'
                    }
                    ],
                    dataBound: function (e) {
    				    var grid = vm.creExpNoteGrid;
    				    grid.tbody.find("tr").dblclick(function (e) {
    				        var dataItem = grid.dataItem(this);
    				        if(dataItem.status === 1)
    				        	vm.createNew(dataItem)
    				    });
    				},
				dataSource: {
					serverPaging: true,
					 schema: {
						 total: function (response) {
						 $("#creExtNoteCount").text(""+response.total);
							 vm.count = response.total;
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
						url: Constant.BASE_SERVICE_URL + "orderServiceRest/order/doSearchExportRequirement",
						contentType: "application/json; charset=utf-8",
						type: "POST"
						},						
						parameterMap: function (options, type) {
                             // vm.appParamSearch.employeeId =
								// Constant.user.srvUser.catEmployeeId;
							    vm.orderSearch.page = options.page
								vm.orderSearch.pageSize = options.pageSize

								return JSON.stringify(vm.orderSearch)
						}
					},					 
					pageSize: 10
				} ,
				noRecords: true,
				columnMenu: false,
				scrollable: false,
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
				dataBinding: function() {
								record = (this.dataSource.page() -1) * this.dataSource.pageSize();
							},
				columns: [
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
					field: 'code',
			        width: "15%",
			        template: '<a class="#=orderId#" href="javascript:void(0);" ng-click=vm.showDetail(dataItem)>#=code#</a>',
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
						if($scope.bussinessType === vm.businessTypes[0].code){
							return vm.businessTypes[0].name;
						}else if($scope.bussinessType === vm.businessTypes[1].code){
							return vm.businessTypes[1].name;
						}else if($scope.bussinessType === vm.businessTypes[2].code){
							return vm.businessTypes[2].name;
						}else if($scope.bussinessType === vm.businessTypes[3].code){
							return vm.businessTypes[3].name;
						}else if($scope.bussinessType === vm.businessTypes[4].code){
							return vm.businessTypes[4].name;
						}else if($scope.bussinessType === vm.businessTypes[5].code){
							return vm.businessTypes[5].name;
						}else if($scope.bussinessType === vm.businessTypes[6].code){
							return vm.businessTypes[6].name;
						}else if($scope.bussinessType === vm.businessTypes[7].code){
							return vm.businessTypes[7].name;
						}else if($scope.bussinessType === vm.businessTypes[8].code){
							return vm.businessTypes[8].name;
						}else{
							return vm.businessTypes[9].name;
						}
					},
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "Kho xu???t",
			        field: 'stockName',
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "Ng?????i t???o",
			        field: 'createdByName',
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},  {
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
				},
				{
					title: "Vi???t phi???u",
					field: "stt",
			        width: "15%",
			        template:
					'<div class="text-center #=orderId#""> '	+
		        	'		<button style=" border: none; background-color: white;" ng-hide="dataItem.status == 1 " class="#=orderId# icon_table" uib-tooltip="Kh??a" translate>'+
		        	'			<i  style="color:grey" ng-hide="dataItem.status == 1 " class="fa fa-files-o" aria-hidden="true"></i> '+
		        	'		</button> '+
				'		<button style=" border: none; background-color: white;" ng-show="dataItem.status == 1 " ng-click=vm.createNew(dataItem) class="#=orderId# icon_table" uib-tooltip="T???o phi???u" translate>'+
	        	'			<i  ng-show="dataItem.status == 1 " style="color:steelblue;" class="fa fa-file-text ng-scope" aria-hidden="true"></i> '+
	        	'		</button> '+
	        	'		<button style=" border: none; background-color: white;" ng-hide="dataItem.status == 1 " class="#=orderId# icon_table" uib-tooltip="Kh??a" translate> '+
	        	'			<i style="color:grey"  ng-hide="dataItem.status == 1 " class="fa btn stopQLK" aria-hidden="true"></i>'+
	        	'		</button> '+
	        	'		<button style=" border: none; background-color: white;" ng-show="dataItem.status == 1 " ng-click=vm.openRemovePopup(dataItem) class="#=orderId# icon_table " uib-tooltip="T??? ch???i y??u c???u xu???t kho" translate>'+
	        	'			<i ng-show="dataItem.status == 1 " class="fa btn stopQLK" aria-hidden="true"></i>  '+
	        	'		</button>' 
					+'</div>',
			        headerAttributes: {
			        style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					}
				}]
			});
		}
		
		function fillDataTableInforDeliveryOrder() {
			vm.addInforDeliveryOrderOptions = kendoConfig.getGridOptions({
				autoBind: true,
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
							url: Constant.BASE_SERVICE_URL + "orderGoodsDetailServiceRest/orderGoodsDetail/doSearchGoodsDetailForImportReq",
							contentType: "application/json; charset=utf-8",
							type: "POST"
						},					
						parameterMap: function (options, type) {
							    vm.orderGoodsSerialSearch.page = options.page
								vm.orderGoodsSerialSearch.pageSize = options.pageSize
								vm.orderGoodsSerialSearch.orderGoodsId = vm.orderGoodsSearch.orderGoodsId;
								return JSON.stringify(vm.orderGoodsSerialSearch)
						}
					},					 
					pageSize: 10
				},
				toolbar: [
		                    {
		                        name: "actions",
		                        template: 
		                      	  '<div class="btn-group pull-right margin_top_button margin_right10">'+
		                    '<i class="action-button excelQLK" ng-click="caller.exportExcelGridDetail()" aria-hidden="true"></i>'+
		                    '</div>'
		                    }
		                    ],
				noRecords: true,
				columnMenu: false,
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
			        template: dataItem => $("#addInforDeliveryOrder").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
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
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "H??ng s???n xu???t",
			        field: 'manufacturerName',
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}
				, {
					title: "N?????c s???n xu???t",
			        field: 'producingCountryName',
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
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
		var check =true;
		var focuscellgrid ={};
		function onChange(arg) {
			if(check)
				{
				 var selected = $.map(this.select(), function(item) {
		                return item.cells[4].innerText;
		            });
		            focuscellgrid = selected[0];
				}
           
        }
		
		function fillDataTableGoodsList(data) {
			vm.goodsListGridOptions = kendoConfig.getGridOptions({
				autoBind: true,
				resizable: true,	
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
							 for(var i=0;i<response.data.length;i++){
							if(response.data[i].goodsIsSerial == "1"){
									vm.disableImport = true;
									break;
							}
							}
								return response.data; // data is returned in
														// the "data" field of
														// the response
							},
							 model: {
			                        id: "goodsListGrid",
			                    	fields: {
			                    		stt: {editable: false},
			                    		goodsCode: {editable: false},
			                    		goodsName: {editable: false},
			                    		goodsUnitName: {editable: false},
			                    		amount: {editable: false},
			                    		goodsStateName :{editable: false},
			                    		/* amountReal:  { type: "string", validation: { required: true, min: 1,
			                    			validateAmountReal: function (input) {
			                    				if(focuscellgrid!="")
			                    					{
			                    					  if (input.is("[name='amountReal']") && parseInt(input.val())>parseInt(focuscellgrid)) {
		                                                   input.attr("data-validateAmountReal-msg", "S??? l?????ng xu???t ph???i nh??? h??n ho???c b???ng s??? l?????ng y??u c???u");
		                                                   check = false;
		                                                   return false;
		                                                }
			                    					  else
			                    						  {
			                    						  check = true;
			                    						  }
			                    					  return true;
			                    					}
                                            }} }, */
			                    		goodsIsSerial :{editable: false},
			                    	}
			                    }
		                },
					transport: {
						read: {
		                        // Thuc hien viec goi service
							url: Constant.BASE_SERVICE_URL + "orderGoodsServiceRest/orderGoods/doSearchGoodsForExportOrder",
							contentType: "application/json; charset=utf-8",
							type: "POST"
						},					
						parameterMap: function (options, type) {
							    vm.orderGoodsSearch.page = options.page
								vm.orderGoodsSearch.pageSize = options.pageSize
								vm.orderGoodsSearch.orderId = vm.order.orderId
								return JSON.stringify(vm.orderGoodsSearch)
						}
					},					 
					pageSize: 10
				},
				noRecords: true,
				columnMenu: false,
				messages: {
					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
				},
				pageable: {
					refresh: false,
					pageSize: 10,
					 pageSizes: [10, 15, 20, 25],
					messages: {
		                display: "{0}-{1} c???a {2} k???t qu???",
		                itemsPerPage: "k???t qu???/trang",
		                empty: "Kh??ng c?? k???t qu??? hi???n th???"
		            }
				},
				change: onChange,
				edit: function(e) {
				    if(vm.disableImport){
				      $('#goodsListGrid').data("kendoGrid").closeCell();
				    }},
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
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},  {
					title: "S??? l?????ng y??u c???u",
					 field: 'amount',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				},   {
					title: "S??? l?????ng xu???t",
					 field: 'amountReal',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				}, {
					title: "T??nh tr???ng",
					 field: 'goodsStateName',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},
					{
						title: "Chi ti???t h??ng h??a",
						field: 'goodsIsSerial',
						template:  "# if(goodsIsSerial == 1 && listDetailSerial.length > 0){ #" + "<a  ng-click=caller.showGoodsSerial(dataItem)>#= 'Chi ti???t' #</a>" + "# } " + "else if (goodsIsSerial == 0) { # " + "#= '' #"+ "#} #",
						
				        width: "10%",
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
			        width:"15%",
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
		
		vm.showGoodsSerial = function(item){
			 var teamplateUrl="wms/createExportNote/detailSerialsForGoods.html";
			 var title="Th??ng tin chi ti???t h??ng h??a";
			 var windowId="DETAIL_POPUP";
			 vm.orderGoodsSearch = item;
			 fillDataTableInforDeliveryOrder();
		      CommonService.populatePopupCreate(teamplateUrl,title,vm.orderGoodsSearch,vm,windowId,false,'75%','85%'); 
		}
		
		$scope.$watch('vm.order.stockId', function() {
			var obj={};
			obj.value="PXK";
			obj.orgValue="AAA";
			obj.stockId="";
			obj.stockId= vm.order.stockId;
				CommonService.genCode(obj).then(
						function(d) {
							vm.orderForNote.code = d;
						});
	    });
		
	
		vm.createNew = function(item){
			 	var teamplateUrl="wms/createExportNote/infordeliveryorder.html";
				 var title="Vi???t phi???u xu???t kho";
				 var windowId="DETAIL_POPUP";
				 vm.order = item;
				
				vm.order.orderCode = item.code; 
				vm.order.description=null;
				 var obj={};
					obj.value="PXK";
					obj.orgValue="AAA";
					obj.stockId="";
					obj.stockId= vm.order.stockId;
					if(vm.order.countSerial>0){
				 		vm.disbledSerial=true;
				 		} else {
				 			vm.disbledSerial=false;
				 		}
						CommonService.genCode(obj).then(
								function(d) {
									vm.order.code = d;
								});
								vm.disableImport = false;
				 fillDataTableGoodsList([]);
				CommonService.populatePopupCreate(teamplateUrl,title,vm.order,vm,windowId,false,'85%','85%',"recCUCOP2"); 
				$('#creExpNoteGrid').data('kendoGrid').dataSource.read();
			$('#creExpNoteGrid').data('kendoGrid').refresh();
			}
		
		function refreshGrid(d) {
			var grid = vm.appParamGrid;
			if(grid){
				grid.dataSource.data(d);
				grid.refresh();
			}
		}
			

		 vm.add = function(){ 
			 vm.isCreateNew = true;
	         vm.showDetail = true;
	         vm.appParam={};
	         vm.title="Th??m m???i tham s??? ???ng d???ng";
		 }

		
		 vm.showDetail=function(item){
				var teamplateUrl="wms/createExportNote/detailsPopup.html";
				 var windowId="DETAIL_POPUP";
					 vm.order = item;
					 vm.orderGoodsSearch.orderId = vm.order.orderId;
					 var title="Chi ti???t y??u c???u xu???t kho " + vm.order.code;
					 vm.checkBoxAutoLoad(vm.order);
					 //fillDataTableGoodsListPopup([]);
vm.gotoTabOnePopUp();
					 if(vm.order.constrCode != null){
						 createExportRequestManageService.getConstructionByCode(vm.order.constrCode).then(function(d){
							 	vm.consTrc = d.plain();	
							 	vm.order.constructionName = vm.consTrc.name;
						 });
						 
					 }
					 
					 if(vm.order.reasonId != null){
						 //var reasonToExport = {reasonId: vm.order.reasonId};
						 createExportRequestManageService.getReasonName(vm.order.reasonId).then(function(d){
							 	vm.rs = d.plain();	
							 	vm.order.reasonToExport = vm.rs.name;
						 });
					 }
					 
					 //show partner name
					 if(vm.order.partnerId != null){
						 createExportRequestManageService.getPartnerName(vm.order.partnerId).then(function(d){
							 	vm.ptn = d.plain();	
							 	vm.order.partnerName = vm.ptn.name;
						 });
					 }

					 
					 // show partner name
					 if(vm.order.status === '4' || vm.order.status === '5'){
						 vm.terminatedOrder = true;
					 }else{
						 vm.terminatedOrder = false;
					 }
					 fillDataTableGoodsListPopup([]);
						CommonService.populatePopupCreate(teamplateUrl,title,vm.order,vm,windowId,false,'85%','90%'); 
					
				 }
		 
		 function fillDataTableGoodsListPopup(data) {
				vm.goodsListPopupGridOptions = kendoConfig.getGridOptions({
					autoBind: true,
					resizable: true,	
					  change: onChangeGoodList,
					  /* function() {
				          var gridDetails = $("#goodsDetailGrid").data("kendoGrid");
				          var dataItem = this.dataItem(this.select());
				          gridDetails.dataSource.filter({ field: "orderGoodsId", value: dataItem.orderGoodsId, operator: "eq" });
				          vm.orderGoodsDetailSearch.goodsName =  dataItem.goodsName;
						  vm.orderGoodsDetailSearch.goodsCode = dataItem.goodsCode;
				          vm.orderGoodsDetailSearch.goodsStateName = dataItem.goodsStateName;
					  }, */
				    	dataBound: function(e) {		
				    	//var grid = $("#goodsDetailGrid").data("kendoGrid");
					    //var tr = $("#goodsListPopupGrid").data("kendoGrid").select("tr:eq(0)");
					    //var dataItem = $("#goodsListPopupGrid").data("kendoGrid").dataItem(tr);
					    //gridDetails.dataSource.filter({ field: "orderGoodsId", value: dataItem.orderGoodsId, operator: "eq" });
						var grid = $("#goodsListPopupGrid").data("kendoGrid");
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
					columnMenu: false,
					scrollable : false,
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
				        template: dataItem => $("#goodsListPopupGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
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
		 vm.showSerialDetail=function(dataItem){
			vm.orderGoodsDetailSearch.orderGooodsId=dataItem.orderGooodsId;
			//fillDataTableGoodsDetailPopUp([]);
		 }
		 
		 
		 //change list good detai when select good
		function onChangeGoodList(){
			
			if ($("#goodsListPopupGrid").data("kendoGrid").select().length > 0){
				var tr = $("#goodsListPopupGrid").data("kendoGrid").select().closest("tr");
    			var dataItem = $("#goodsListPopupGrid").data("kendoGrid").dataItem(tr);
    			
    			vm.orderGoodsDetailSearch = dataItem;
    			$("#goodsCodeAndNameCreExtNote").text(vm.orderGoodsDetailSearch.goodsCode+" "+vm.orderGoodsDetailSearch.goodsName);
				
    			var grid = $("#goodsDetailGrid").data("kendoGrid");	
    			if(grid){
    				grid.dataSource.query({
    					page: 1,
    					pageSize: 10
    				});
    			} else {
    			fillDataTableGoodsDetailPopUp([]);
    			}
    			
    			console.log(vm.orderGoodsDetailSearch.goodsName);
			}
			
		}
			function fillDataTableGoodsDetailPopUp(data) {
				vm.goodsDetailGridOptions = kendoConfig.getGridOptions({
					autoBind: true,
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
					columnMenu: false,
					scrollable : false,
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
				        width:"20%",
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

		vm.doSearch= doSearch;
		function doSearch(){
			if(vm.orderSearch.createdDateFrom == ""){
				vm.orderSearch.createdDateFrom = null;
			}
			if(vm.orderSearch.createdDateTo == ""){
				vm.orderSearch.createdDateTo = null;
			}

			if(!vm.validator.validate()){
			$("#creExtFromDate").focus();
				return;
			}
			trimSpace();
			vm.orderSearch.listStatus = $("#creExtNoteStatus").data("kendoMultiSelect").value();
			vm.orderSearch.listBussinessType = $("#requirementTypeCreExpNote").data("kendoMultiSelect").value();
			vm.orderSearch.listSignState = $("#creExtNoteSignCA").data("kendoMultiSelect").value();
			
			var grid =vm.creExpNoteGrid;	
			if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 10
				});
			}
		}
		
		
		// 1.show/hide column field
		// start
		vm.showHideColumn=function(column){
        	if (angular.isUndefined(column.hidden)) {
        		vm.creExpNoteGrid.hideColumn(column);
            } else if (column.hidden) {
            	vm.creExpNoteGrid.showColumn(column);
            } else {
            	vm.creExpNoteGrid.hideColumn(column);
            }
        }
		
		vm.gridColumnShowHideFilter = function (item) { 
            return item.type==null||item.type !=1; 
        };
		// end
		
        
     
        
        
        // 3.remove
            vm.openRemovePopup=openRemovePopup;
     		function openRemovePopup(dataItem){
     			var reason = {
     					status : '1',
     					apply : '7'
     			}
     			createExNoteService.getReasonForComboBox(reason).then(function(d){
     				vm.dataReasonDelete =  d.plain();
					//dataItem.cancelReasonName=vm.dataReasonDelete[0].name;
					dataItem.cancelByName = Constant.user.vpsUserToken.fullName;
					dataItem.cancelBy = Constant.user.vpsUserToken.sysUserId;
					//dataItem.cancelby = Constant.user.vpsUserToken.fullName;
     				var teamplateUrl = "wms/createExportNote/reqCancelPopup.html";
     				var title="T??? ch???i y??u c???u xu???t kho";
     				var windowId="EXT"
     				dataItem.cancelDate=kendo.toString(new Date(),"yyyy-MM-dd");
     				 CommonService.populatePopupCreate(teamplateUrl,title,dataItem,vm,windowId,false,'56%','38%'); 
     			});
     		}
     		
     		vm.remove=remove;
     		function remove(data){
			confirm('X??c nh???n x??a',function (){
     				data.cancelReasonApply = "7";data.cancelDate=null;
     				createExNoteService.remove(data).then(
     						function(d) {
     							toastr.success("X??a th??nh c??ng!");
     							/* var grid =vm.creExpNoteGrid;	
								if(grid){
									grid.dataSource.query({
										page: grid.dataSource.page(),
										pageSize: 10
									});
								}
     							CommonService.closePopup1(); */
								$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        		var sizePage = $("#creExpNoteGrid").data("kendoGrid").dataSource.total();
				var pageSize = $("#creExpNoteGrid").data("kendoGrid").dataSource.pageSize();
				if(sizePage % pageSize == 1){
					var currentPage = $("#creExpNoteGrid").data("kendoGrid").dataSource.page();
			        if (currentPage > 1) {
			            $("#creExpNoteGrid").data("kendoGrid").dataSource.page(currentPage - 1);
			        }
				}
				$('#creExpNoteGrid').data('kendoGrid').dataSource.read();
				$('#creExpNoteGrid').data('kendoGrid').refresh();
     						}, function(errResponse) {
     							toastr.error("L???i kh??ng x??a ???????c!");
     						});
     			 } 
     			 )
     		}
     		
     		vm.listRemove=[{
    			title: "Thao t??c",
    		},{
    			title: "Vi???t phi???u",
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
    				4:'???? t??? ch???i',
    			}
    		},{
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
			}];
    		
    		vm.addToExportNote = function(){
    			insertData();
    		}
    		
    		// remove input data value
    		vm.cancelTime = function(){
    			vm.orderSearch.createdDateFrom = null;
    			vm.orderSearch.createdDateTo = null;
    		}
     		vm.cancelInput = function(param){
				if(param === 'creDept'){// consider ng-model
					vm.orderSearch.createdDeptedName   = undefined;
				}else if(param === 'reqList'){
					vm.orderSearch.listBussinessType = [];
				}else if(param === 'recDept'){// consider ng-model
					vm.orderSearch.recDept = undefined;
				}else if(param === 'reqStatList'){	
					vm.orderSearch.listStatus = [];
				}else if(param === 'signStatusList'){	
					vm.orderSearch.listSignState = [];
				}
		}
    		
    		vm.noteFill = {};	// stocktrans detail
    		function insertData() {
    			var selectedRow = [];
    			var grid = vm.goodsListGrid;
    			grid.table.find("tr").each(function(idx, item) {
    			var row = $(item);
    					var dataItem = grid.dataItem(item);
    					vm.noteFill.goodsCode = dataItem.goodsCode
    					vm.noteFill.goodsName = dataItem.goodsName
    					vm.noteFill.goodsUnitName = dataItem.goodsUnitName
    					vm.noteFill.amountReal = dataItem.amountReal
    					selectedRow.push(noteFill);
    			});
    			createExNoteService.insertDataToNote(selectedRow).then(
    					function(d) {
    						toastr.success("T???o phi???u th??nh c??ng!");
    						vm.doSearch();
    						CommonService.closePopup1();
    					}, function(errResponse) {
    						toastr.error("L???i th??m phi???u m???i!");
    					});		
    		}
    		
    		// export
    		vm.exportExcelGrid = function(){
				vm.orderSearch.page = null;
				vm.orderSearch.pageSize = null;
    			createExNoteService.getForExportGrid(vm.orderSearch).then(function(d) {
    				CommonService.exportFile(vm.creExpNoteGrid,d.data,vm.listRemove,vm.listConvert,"VietPhieuXuatKho");
    			});	
    		}

			vm.exportExcelGridDetail = function(){
			var dataForExport = [];
			var dataFromTable = [];
			dataFromTable = $("#addInforDeliveryOrder").data("kendoGrid").dataSource.view();
			for(var i=0;i<dataFromTable.length;i++){
				dataForExport.push(dataFromTable[i]);
			}
			CommonService.exportFile(vm.addInforDeliveryOrder,dataForExport,[],[],"ChiTietHangHoa");
		}
    		
    		vm.getExcelTemplate = function(){
			if(!vm.disableImport){
    			createExNoteService.downloadTemplate(vm.order).then(function(d) {
    				data = d.plain();
    				window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + data.fileName;
    			}).catch( function(){
    				toastr.error(gettextCatalog.getString("L???i khi export!"));
    				return;
    			});
    		}
			}
        // end
    		
    		vm.create = create;
    		function create()
    		{
			if(vm.order.signState != '3'){
				toastr.error("Y??u c???u ch??a ???????c k?? duy???t !");
						return;
				}
    			//////////////////////
    			vm.order.listOrderGoodsDTO = [];
				var dataGoodFromGrid = $('#goodsListGrid').data("kendoGrid").dataSource.data();
				for(var i = 0; i<dataGoodFromGrid.length;i++){
					dataGoodFromGrid[i].id=null;
					if(dataGoodFromGrid[i].goodsState==1)
						{
						dataGoodFromGrid[i].goodsStateName="B??nh th?????ng";
						}
					else
						{
							dataGoodFromGrid[i].goodsState=0;
						dataGoodFromGrid[i].goodsStateName="H???ng";
						}
					vm.order.listOrderGoodsDTO.push(dataGoodFromGrid[i]);
				}
				
				for(var i = 0; i<vm.listGoodsAmountZero.length;i++){
					vm.listGoodsAmountZero[i].id=null;
					if(vm.listGoodsAmountZero[i].goodsState==1)
						{
						vm.listGoodsAmountZero[i].goodsStateName="B??nh th?????ng";
						}
					else
						{
							vm.listGoodsAmountZero[i].goodsState=0;
						vm.listGoodsAmountZero[i].goodsStateName="H???ng";
						}
					vm.order.listOrderGoodsDTO.push(vm.listGoodsAmountZero[i]);
				}
    			/////////////////////
    			var orderGoodsDTO={};
    			 orderGoodsDTO.orderId = vm.order.orderId;
    			vm.order.orderGoodsDTO=orderGoodsDTO;
				vm.order.createdDeptId = vm.order.createdDeptedId;
				vm.order.createdDeptName = vm.order.createdDeptedName;
				
    			createExNoteService.createExportNote(vm.order).then(function(d) {
    				if(d.error){
						toastr.error(d.error);
						return;
					}
    				toastr.success("T???o phi???u th??nh c??ng ");
    				vm.doSearch();
    				//CommonService.closePopup1();
					$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
    	     }, function(errResponse) {
    	    	 if (errResponse.status === 406) {
                     toastr.error("S??? l?????ng ????p ???ng trong kho c???a m?? h??ng kh??ng c??n ????? ????? xu???t");
                 } else {
                	 toastr.error("L???i khi l??u");
                 }
    	    		 
    	     });
    		}
    		
    		/*
			 * Import data
			   2.choose file create order
			 */
			 vm.objectErr={};
    		 vm.submit=submit;
    	        function submit(){
    	        	if($("#fileExNote")[0].files[0] === null){
    	        		toastr.warning("B???n ch??a ch???n file ????? import");
    	        		return;
    	        	}
    	        	
    		        var formData = new FormData();
    				formData.append('multipartFile', $('#fileExNote')[0].files[0]); 
    		     return   $.ajax({
    		    	 url: Constant.BASE_SERVICE_URL +"orderGoodsServiceRest/orderGoods/importOrderGood?folder="+ vm.folder+"&orderId="+vm.order.orderId,
    		            type: "POST",
    		            data: formData,
    		            enctype: 'multipart/form-data',
    		            processData: false,
    		            contentType: false,
    		            cache: false,
    		            success:function(data) {
    		            	/* var lstError = null;
    		            	lstError = data[data.length - 1].lstErrorOrderGoods; */
    		            	 if(data[data.length - 1].lstErrorGoods.length > 0){
    			            		vm.lstErrImport = data[data.length - 1].lstErrorGoods;
									vm.objectErr=data[data.length - 1];
									vm.objectErr.fileName="XK_TaoPhieuXuat_Error";
    			            		var teamplateUrl="wms/createExportNote/errList.html";
    			       			 var title="K???t qu??? Import";
    			       			 var windowId="ERR_IMPORT";
    			       			 CommonService.populatePopupCreate(teamplateUrl,title,vm.lstErrImport,vm,windowId,false,'50%','50%');
    			       			erroTable(vm.lstErrImport);
    			            	}else{
    			            		
    				            	var grid = vm.goodsListGrid;
    				            		var gridData = grid.dataSource.data();
										grid.dataSource.data([]);
										data.splice(data.length - 1, 1);
										/* for(var i=0;i<data.length;i++){
										data[i].id = i+1;
										grid.dataSource.add(data[i]);
										} */
										for(var i=0;i<gridData.length;i++){
										var check = false;
											for(var j=0;j<data.length;j++){
												if(gridData[i].goodsCode === data[j].goodsCode && gridData[i].goodsState === data[j].goodsState)
												{
												data[j].goodsState = gridData[i].goodsState;
												data[j].goodsStateName = gridData[i].goodsStateName;
												data[j].goodsIsSerial = gridData[i].goodsIsSerial;
												data[j].goodsId = gridData[i].goodsId;
												data[j].goodsUnitId = gridData[i].goodsUnitId;
												data[j].goodsUnitName = gridData[i].goodsUnitName;
												data[j].goodsType = gridData[i].goodsType;
												data[j].orderGoodsId = gridData[i].orderGoodsId;
												data[j].orderId = gridData[i].orderId;
												data[j].totalPrice = gridData[i].totalPrice;
												check = true;
												break;
												}
											}
											if(!check){
											gridData[i].amountReal = 0;
												vm.listGoodsAmountZero.push(gridData[i]);
											}
										}
										toastr.success("Import th??nh c??ng!");
										grid.dataSource.data(data);
										grid.refresh();
    			            	}
    		            }
    		        });
             }
    	         // xu???t file l???i
				vm.exportErrExcelGrid = function(){
	        	 createExNoteService.downloadStockTransErrorExcel(vm.objectErr).then(function(d) {
		 				data = d.plain();
		 				window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + data.fileName;
		 			}).catch( function(){
		 				toastr.error(gettextCatalog.getString("L???i khi export!"));
		 				return;
		 			})
	 		}
    	      //validate ngay thang
			
    	    	vm.validateDate = validateDate;
			
			function validateDate(id){
					if(id === 'checkErr1'){
					var startDate = $('#creExtFromDate').val();  
					var endDate = $('#creExtToDate').val();
					vm.errMessage = '';
					vm.errMessage1 = '';
					var curDate = new Date();
					if(kendo.parseDate(startDate,"dd/MM/yyyy") > curDate){
				           vm.errMessage1 = 'Ng??y t???o ph???i nh??? h??n b???ng ng??y hi???n t???i';
				           $("#creExtFromDate").focus();
				           return vm.errMessage1;
				        }
			        if(endDate !== ""){
					if(kendo.parseDate(startDate,"dd/MM/yyyy") > kendo.parseDate(endDate,"dd/MM/yyyy")){
			          vm.errMessage1 = 'Ng??y t???o ph???i nh??? h??n b???ng ng??y ?????n';
			          $("#creExtFromDate").focus();
			          return vm.errMessage1;
			        }
					}
			        
				        else if(startDate <= curDate){
					           vm.errMessage1 = '';
							   vm.errMessage = '';
					           return vm.errMessage1;
					        }
if(!vm.validator.validate()){
			$("#creExtFromDate").focus();
			return;
		}
				}
				else if(id === 'checkErr'){
					var startDate = $('#creExtFromDate').val();  
					var endDate = $('#creExtToDate').val();
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
			          $("#creExtToDate").focus();
			          return vm.errMessage;
			        }if(kendo.parseDate(endDate,"dd/MM/yyyy") > curDate){
				           vm.errMessage = 'Ng??y ?????n ph???i nh??? h??n ng??y hi???n t???i';
				           $("#creExtToDate").focus();
				           return vm.errMessage;
				        }
}
			        else if(kendo.parseDate(startDate,"dd/MM/yyyy") <= kendo.parseDate(endDate,"dd/MM/yyyy")){
			          vm.errMessage = '';
					  vm.errMessage1 = '';
			          return vm.errMessage;
			        }
				}
			}
			vm.close = function(){
							CommonService.closePopup1();
						}

			vm.cancel = cancel;
			function cancel(){
							vm.appParam={};
							vm.isCreateNew = false;
										confirm('B???n c?? ch???c mu???n h???y b??? thao t??c n??y', function(){
										//vm.copySearch = {};
							//vm.copySearch.page = vm.dupOrder.page;
							//vm.copySearch.pageSize = vm.dupOrder.pageSize;
							//$rootScope.$broadcast("backFromCancelCopy", vm.copySearch);
										//CommonService.dismissPopup();
										$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
						});
					}


            
	}
})();
