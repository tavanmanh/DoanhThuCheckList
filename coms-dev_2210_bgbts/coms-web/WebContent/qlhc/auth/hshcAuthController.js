(function() {
	'use strict';

	var controllerId = 'HshcAuthController';

	angular.module('MetronicApp').controller(controllerId, HshcAuthController);

	/* @ngInject */
	function HshcAuthController($scope, $rootScope, $timeout, Constant,
			kendoConfig, gettextCatalog, $kWindow,WindowService, $q, hshcAuthService, dsbgvtService, ProposalEvaluation) {
		
		
		var vm = this;
		vm.employee = [];
		vm.chucvu = [];
		vm.donvi = [];
		vm.settlementObj = {};
		vm.hideAButton = hideAButton;
		vm.onChange = onChange;
        vm.changeSite = changeSite;
		vm.showAdd = false;
		vm.showDetail = true;
		vm.isMornitor = false;
		vm.chkSelectAll = chkSelectAll;
		vm.showGrid = showGrid;
		vm.boxung = boxung;
		vm.save = save;
		vm.multiDelete = multiDelete;
		vm.closeModal = closeModal;
		vm.changeChucVu = changeChucVu;
		
		vm.DoSearch = DoSearch;
		vm.openSearch = openSearch;
		vm.autoData = [];
		vm.isAddnew = false;
		vm.adding = false;
		vm.dataSave = [];
		vm.role =[];
		vm.complateId = 0;
		vm.invalid = true;
		
		vm.settlementObjNew = {};
		
		
		//dodt_edit_search employee
		vm.criteriaEmployee ={}; 
		//dodt
		
		loadData();
		function boxung() {
			var datasource = vm.settlementGrid.dataSource;
				vm.isAddnew = true;
				 vm.adding = true;
				 selectedRowIndex = 0;
				
				vm.settlementObjNew = { 
						groupSide: 0, 
						roleid: 0, 
						fullName:'',
						isCurrent:0
					};
				var grid = vm.settlementGrid;
				if(grid){
					var datatable = grid.dataSource;
					grid.dataSource.insert(0,vm.settlementObjNew);
					grid.refresh();
				}
			 
			
	    }
	    
	    function loadData(){  	
	    	vm.donvi.id = 1;
	    	vm.emObject = {
					type : '1'
				};
	    	// get data nhan su
	    	getListEmployee(vm.emObject);
	    	
	    	// get data chuc vu
	    	vm.objRole = {
	    			groupSide: '1'
	    	}
	    	getListChucVu(vm.objRole);
	    }
	    
	    function changeChucVu() {
	    	vm.objRole.groupSide = document.getElementById("unit").value;
	    	getListChucVu(vm.objRole);
	    	var grid = vm.settlementGrid;
			if(grid){
				grid.refresh();
			}
	    }
	    

		function getListEmployee(object) {
			hshcAuthService.getListEmployee(object).then(function(data) {

				vm.employee = data.plain();
				if(vm.employee.length > 0) {
					vm.employee.id = vm.employee[0].id;;
				}
				
			  });
		}
		
		function getListChucVu() {
			var roleCaDTO = {
					groupSide:vm.settlementObj.groupSide
			};
			hshcAuthService.getListChucVu(roleCaDTO).then(function(data) {
				vm.settlementObj.listRole = data.plain();
				
			  });
			
		}
		
		 
		vm.constrObj = {
				constructId : '',
				contractCode : '',
				doitac : '',
				mact : '',
				tenct : '',
				loaict : '',
				tinhthanh: ''
			}
		
		vm.item = ProposalEvaluation.getItem();
		
		if(vm.item == null) {
			alert("Ch??a ch???n b???n ghi n??o.");
			return;
		}
		
		vm.donvi = [
		             { groupSide: 1, value: "Ch??? ?????u t??" }, 
		             { groupSide: 2, value: "?????i t??c" },
		             { groupSide: 3, value: "T?? v???n thi???t k???" }, 
		             { groupSide: 4, value: "T?? v???n gi??m s??t" }
		            ]
		
		
		fillData(vm.item);
		
		function fillData(object) {
			hshcAuthService.getConstructions(object).then(function(data) {
				vm.constrObj.constructId = data.constructId;
				vm.constrObj.contractCode = data.contractCode;
				vm.constrObj.doitac = data.partnerName;
				vm.constrObj.mact = data.constrtCode;
				vm.constrObj.tenct = data.constrtName;
				vm.constrObj.tinhthanh = data.provinceName;
				vm.constrObj.loaict = data.constrTypeName;
				
				if(data.hiringMonitorConsult == 1){
					vm.isMornitor = true;
				}
				
				hshcAuthService.fetchAll(object).then(function(data) {
					fillDataTable(data.plain()) ;
				  });
				
			  });
			
		}

		hshcAuthService.getRoleId().then(function(data) {
			vm.role = data;
		})
		.catch(function(data, status) {
			console.error('getRoleId error', response.status, response.data);
		});
		
		
		function fillDataTable(data) {
			var deferred = $q.defer();
		   	vm.options = kendoConfig.getGridOptions({
		   		autoBind : true,
		   		change:onChangeGrid,
		   		dataSource : data,
		   		noRecords : true,
		   		messages : {
		   			noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? n??o")
		   		},
				columns : [
						{
							title : gettextCatalog.getString("STT"),
							field : "index",
							template: dataItem => $("#settlementGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
							headerAttributes: {
						      	"class": "color-black",
						      	style: "text-align: center"
						    	},
				   			attributes: { style: "text-align:center;" },
				   			editable:false,
				   			editor: nonEditor,
							width : 80
						},
						{
							title : "<input type='checkbox' name='gridchkselectall' ng-click='vm.chkSelectAll();' ng-model='vm.chkAll' />",
							template : "<input type='checkbox' name='gridcheckbox' />",
							headerAttributes: {
						      	"class": "color-black",
						      	style: "text-align: center"
						    	},
				   			attributes: { style: "text-align:center;" },
							width : 35
						},
						{
							title : gettextCatalog
									.getString("????n v???"),
							field : "groupSide",
							template : function(data) {
								if(data.groupSide == '1') {
									return "Ch??? ?????u t??";
								}else if (data.groupSide == '2') {
									return "?????i t??c";
								}else if (data.groupSide == '3') {
									return "T?? v???n thi???t k???";
								}else if (data.groupSide == '4') {
									return "T?? v???n gi??m s??t";
								}else {
									return "Ch???n ????n v???";
								}
							},
							editor:categoryDropDownEditor,
							width : 200
						},
						{
							title : gettextCatalog
									.getString("Ch???c v???"),
							field : "roleid",
							template : function(data) {
								if(data.roleid == Constant.ROLE_ID["employee_roleID_CDT_GSTC"]) {
									return "Gi??m s??t thi c??ng";
								}else if (data.roleid == Constant.ROLE_ID["employee_roleID_CDT_PTGST"]) {
									return "Ph??? tr??ch gi??m s??t thi c??ng";
								}else if (data.roleid == Constant.ROLE_ID["employee_roleID_CDT_DDPN"]) {
									return "Th??? tr?????ng ????n v???";
								}else if (data.roleid == Constant.ROLE_ID["employee_roleID_DT_KTTC"]) {
									return "K??? thu???t thi c??ng";
								}else if (data.roleid == Constant.ROLE_ID["employee_roleID_DT_GDNT"]) {
									return "Gi??m ?????c nh?? th???u";
								}else if (data.roleid == Constant.ROLE_ID["employee_roleID_DT_PTTC"]) {
									return "Ph??? tr??ch thi c??ng";
								}else if (data.roleid == Constant.ROLE_ID["employee_roleID_TVTK_DDTV"]) {
									return "?????i di???n t?? v???n";
								}else if (data.roleid == Constant.ROLE_ID["employee_roleID_TVTK_CNTK"]) {
									return "Ch??? nhi???m thi???t k???";
								}else if (data.roleid == Constant.ROLE_ID["employee_roleID_TVGS_GSTC"]) {
									return "Gi??m s??t thi c??ng";
								}else if (data.roleid == Constant.ROLE_ID["employee_roleID_TVGS_PTGST"]) {
									return "Ph??? tr??ch gi??m s??t";
								}else if (data.roleid == Constant.ROLE_ID["employee_roleID_TVGS_DDTVGS"]) {
									return "?????i di???n t?? v???n gi??m s??t (k?? v?? ????ng d???u)";
								}else if (data.roleid == Constant.ROLE_ID["employee_roleID_CDT_DDDVSDCT"]) {
									return "?????i di???n ????n v??? qu???n l?? s??? d???ng c??ng tr??nh";
								}else {
									return "Ch???n ch???c v???";
								}
							},
							headerAttributes: {
						      	"class": "color-black",
						      	style: "text-align: center"
						    	},
							editor:categoryDropDownEditorChucVu,
							width : 200
						},
						{
							title : gettextCatalog
									.getString("Nh??n s???"),
							field : "fullName",
							editor:buttonSearchEditor,
							template : function(data) {
									return data.fullName;
							},
							headerAttributes: {
						      	"class": "color-black",
						      	style: "text-align: center"
						    	},
							width : 200
						},
						{
							title : gettextCatalog
									.getString("T??i kho???n HCQT"),
							field : "loginName",
							headerAttributes: {
						      	"class": "color-black",
						      	style: "text-align: center"
						    	},
						    	editable:false,
					   			editor: nonEditor,
							width : 150
						},
						{
							title : gettextCatalog
									.getString("<b>??u ti??n</b>"),
							template: "<input name='gridIscurrent' type='checkbox' #if(isCurrent == '1'){#checked# }# />",
							headerAttributes: {
						      	"class": "color-black",
						      	style: "text-align: center; vertical-align: middle;"
						    	},
				   			attributes: { style: "text-align:center; " },
				   			editable:false,
				   			editor: nonEditor,
							width : 150
						} ]
			});
			deferred.resolve('done');
		   	return deferred.promise;
		}
		
		function buttonSearchEditor(container, options) {
			var editor = $('<div style=\"text-align:center\" class=\"action-button search\" id=\"btnsearch\" ng-click=\"vm.openSearch()\" ></div>')
	        .appendTo(container);
			
		}
		
		
		function categoryDropDownEditor(container, options) {
			$('<input required name="' + options.field + '" id="unit"/>')
			.appendTo(container)
			.kendoDropDownList({
				autoBind: false,
				change:onChange,
				dataTextField: "value",
				dataValueField: "groupSide",
				dataSource: [
				             { groupSide: 1, value: "Ch??? ?????u t??" }, 
				             { groupSide: 2, value: "?????i t??c" },
				             { groupSide: 3, value: "T?? v???n thi???t k???" }, 
				             { groupSide: 4, value: "T?? v???n gi??m s??t" }
				            ]
			});
			
		}
		
		function categoryDropDownEditorChucVu(container, options) {
			var dataSource = vm.settlementObj.listRole;
			$('<input name="' + options.field + '"/>')
			.appendTo(container)
			.kendoDropDownList({
				autoBind: false,
				change:onChangeChucVu,
				focusout:refreshGridSettlement,
				dataTextField: "rolename",
				dataValueField: "roleid",
				dataSource: dataSource
			});
		}



		// Error handler
		function errorHandle(errResponse) {
			toastr.warning(saveWarningMsg);
			console.error('Error while creating object: ' + errResponse);
		}
		
		function chkSelectAll(item) {
			console.log('chkSelectAll');
	    	var grid = vm.settlementGrid;
	    	chkSelectAllBase(vm.chkAll, grid);
		}

		function add() {
			vm.showDetail = false;
			vm.showAdd = true;
		}
		
		function showGrid() {
			vm.showDetail = true;
			vm.showAdd = false;
	
		}
		
		function onChangeChucVu(){
			if (vm.settlementGrid.select().length > 0) {
				var datasource = vm.settlementGrid.dataSource;
				if(vm.isAddnew) {
					selectedRowIndex = 0;
		    	}
				if(vm.settlementObj.roleid.roleid != null) {
					datasource._data[selectedRowIndex].roleid = vm.settlementObj.roleid.roleid;
				}else {
					datasource._data[selectedRowIndex].roleid = vm.settlementObj.roleid;
				}
				
				vm.dataSave.push(datasource._data[selectedRowIndex]);
				vm.settlementGrid.refresh();
			}
			
		}
		
		
		function onChange(){
			if (vm.settlementGrid.select().length > 0) {
				var tr = vm.settlementGrid.select().closest("tr");
				var dataItem = vm.settlementGrid.dataItem(tr);
				vm.settlementObj = dataItem;
				var roleCaDTO = {
						groupSide:dataItem.groupSide
				};
				hshcAuthService.getListChucVu(roleCaDTO).then(function(data) {
					vm.settlementObj.listRole = data.plain();
				  });
				
				var grid = vm.settlementGrid;
				if(grid){
					grid.refresh();
				}
				
				var datasource = vm.settlementGrid.dataSource;
				datasource._data[selectedRowIndex].roleid = null;
				vm.settlementGrid.refresh();
			}
			
		}
		
		var selectedRowIndex = -1;
		
		function onChangeGrid(){
			if (vm.settlementGrid.select().length > 0 && !openning) {
				var tr = vm.settlementGrid.select().closest("tr");
				var dataItem = vm.settlementGrid.dataItem(tr);
				vm.settlementObj = dataItem;
				var roleCaDTO = {
						groupSide:dataItem.groupSide
				};
				
				var grid = $('#settlementGrid').data("kendoGrid");
				var selectedRow = grid.select();
				selectedRowIndex = selectedRow.index();
			   
				
				hshcAuthService.getListChucVu(roleCaDTO).then(function(data) {
					vm.settlementObj.listRole = data.plain();
				  });
				
			}
			
		}
		  	
		    function changeSite(key) {
		        findConstruction30Service.goTo(key);
		    }
		    
		    function hideAButton(){
		     	var mapHideButton = new Map();
		     	mapHideButton.set("X??a nhi???u","");
		     	mapHideButton.set("S???a","");
		     	$('#topBarContract').find('div').each(function(){
		     		var nameHideButton =$(this).attr('uib-tooltip');
		     		if(mapHideButton.has(nameHideButton)){
		     			var hideButton = $(this).closest("button");
		     			hideButton.addClass("ng-hide");
		     			if(hideButton.hasClass("border-radius-right")){
		     				hideButton.prev().addClass("border-radius-right");
		     			}else{
		     				if(hideButton.hasClass("border-radius-left")){
		     					hideButton.next().addClass("border-radius-left");
		     				}
		     			}
		     		}
		     	})
		     }
		    
		    
		    
		    
		    
		    function save(){
		    	
		    	if(selectedRow.length > 0) {
		    		hshcAuthService.deleteSettlementRight(selectedRow).then(function() {
		    			//toastr.success(gettextCatalog.getString("x??a th??nh c??ng"));
					});
		    	}
		    	var isMornitor = 0;
		    	
		    	var isMornitorChk = document.getElementById("isMornitor");
		    	if(isMornitorChk.checked) {
		    		isMornitor  = 1;
		    	}
		    	vm.listSetlement = [];
		    	
		    	var grid = vm.settlementGrid;
				vm.settlementGrid.select("tr:eq(1)");
				grid.table.find("tr").each(function(idx, item) {
					var row = $(item);
					var isCurrent = 0;
					var checkbox = $('[name="gridIscurrent"]', row);
					if (checkbox.is(':checked')) {
						isCurrent = 1;
					}
					
						// Push id into selectedRow
						var tr = grid.select().closest("tr");
						var dataItem = grid.dataItem(item);
						/*if(dataItem.roleid == null || dataItem.employeeId == null) {
							invalid = true;
						}*/
						if(dataItem.groupSide != null && dataItem.groupSide != 0){
							vm.invalid = false;
							if(dataItem.roleid != null && dataItem.roleid != 0){
								vm.invalid = false;
								if(dataItem.employeeId != null && dataItem.employeeId != 0){
									vm.invalid = false;
									}else{
										toastr.warning(gettextCatalog.getString("B???n ch??a ch???n nh??n s???"));
										vm.invalid = true;
									}
							}else{
								toastr.warning(gettextCatalog.getString("B???n ch??a ch???n ch???c v???"));
								vm.invalid = true;
							}
						}else{
							toastr.warning(gettextCatalog.getString("B???n ch??a ch???n ????n v???"));
							vm.invalid = true;
						} 
						var setlement = {
								isMornitor:isMornitor,
								settlementRightId:dataItem.settlementRightId,
								employeeId : dataItem.employeeId,
								roleid : dataItem.roleid,
								isCurrent : isCurrent,
								constructId : vm.item.constructId,
						}
						if(vm.invalid==false){
						vm.listSetlement.push(setlement);
						}
				});
				if(vm.invalid==true) {
					//toastr.warning(gettextCatalog.getString("Invalid data!"));
					return;
				}
				if($("#settlementGrid").data("kendoGrid").dataSource.view().length == vm.listSetlement.length){
					hshcAuthService.saveSettlementRight(vm.listSetlement).then(function(data) {
						 if(data[2] == 0) {
								toastr.warning(gettextCatalog.getString("???? t???n t???i!"));
								refreshGrid(vm.item);
								return;
						}
							// reload danh sach
							refreshGrid(vm.item);
							toastr.success(gettextCatalog.getString("L??u th??nh c??ng"));
							
							// reload danh sach
							refreshGrid(vm.item);
							vm.dataSave = [];
							vm.listSetlement = [];
					  }).catch(function(data, status) {
						  //toastr.warning(gettextCatalog.getString("Invalid data!"));
					  });
				}
				 
				
		    }
		    
		    function refreshGrid(object) {
				hshcAuthService.fetchAll(object).then(function(data) {
					var grid = vm.settlementGrid;
					if(grid){
						grid.dataSource.data(data.plain());
						grid.refresh();
					}
				  });
				
			}
		    
		    function refreshGridSettlement(object) {
		    	vm.settlementGrid.refresh();
				
			}
		    
		    var selectedRow = [];
		    function multiDelete() {
		    	var selectedRowItem = [];
			 var grid = vm.settlementGrid;
			 vm.settlementGrid.select("tr:eq(1)");
			 grid.table.find("tr").each(function(idx, item) {
			 var row = $(item);
			 var checkbox = $('[name="gridcheckbox"]', row);
			
			 if (checkbox.is(':checked')) {
				 var dataItem = grid.dataItem(item);
				 selectedRowItem.push(dataItem);
				 selectedRow.push(dataItem.settlementRightId);
	             vm.adding =false;
			 }
			 });
			 
			 var dataSource = $("#settlementGrid").data("kendoGrid").dataSource;
			 for(var i = 0; i < selectedRowItem.length; i++) {
	             dataSource.remove(selectedRowItem[i]);
			 }
			
			 if (selectedRow.length == 0) {
				 toastr.warning(gettextCatalog.getString("Ch??a ch???n b???n ghi n??o ????? x??a!"));
				 return;
			 }
			
			 
			 
		}
		    
	    function closeModal() {
	    	// var modal = document.getElementById('myModal');
	    	// modal.style.display = "none";
	    	 $('#myModal').modal('toggle');
	    	window.focus();
	    	
	    }
	    
	    
	    vm.search={
	    		fullName: $("#input").val(),
	    		email: $("#inputEmail").val(),
	    		groupName: $("#inputUnit").val(),
	    		partnerName: $("#inputPartner").val()
	    }
	 // end auto complate partner
	    
	    function DoSearch(objSearch) {
	    	var grid = vm.userInfo;
	    	if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 20
				});
			}else {
				fillDataTableUserInfo([]);
			}
	    	//dodt_end
	    }
	    
	    // open modal seach
	    var openning = false;
	    function openSearch() {
	    	openning = true;
	    	// fillDataTableUserInfo(null);
	    	var objSearch = {
	    			fullName: '',
	    			email: '',
	    			groupName:'',
	    			partnerName:'',
	    			
	    	};
	    	
	    	
	    	/*var grid = vm.userInfo;
	    	
			if(grid){
				grid.refresh();
			}*/
			//vm.DoSearch(objSearch);
			 vm.windowInstance = WindowService.open({
                options: {
                    modal: true,
                    title: "Nh??n s???",
                    visible: false,
                    resizable: true,
                    width: '650',
                    height: '800',
                    actions: ["Minimize", "Maximize", "Close"],
                    activate: function() {
                        // open animation has finished playing
                    	document.getElementById("findButton").click();
                      }
			
                },
                templateUrl: "qlhc/auth/hshcFindEmployeePopup.html"

            });
			
	    	//$('#myModal').modal();
			vm.DoSearch(objSearch);
	    }
	    
	    vm.closePopup = function closePopup() {
	    	//$('#myModal').modal('toggle');
	    	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
	    	openning = true;
	    }
	    
	    vm.done=function() {
	    	var grid = vm.userInfo;
	    	$("#settlementGrid").select("tr:eq(1)");
	    	selectedRowIndex = $("#settlementGrid").data("kendoGrid").select().index();
			grid.table.find("tr").each(function(idx, item) {
				var row = $(item);
				var checkbox = $('[name="gridcheckbox"]', row);

				if (checkbox.is(':checked')) {
					var dataItem = grid.dataItem(item);
					var datasource = $("#settlementGrid").data("kendoGrid").dataSource;
					if($("#settlementGrid").data("kendoGrid").dataSource.data()[0].fullName =="" || $("#settlementGrid").data("kendoGrid").dataSource.data()[0].fullName == null) {
						selectedRowIndex = 0;
			    	}
					datasource._data[selectedRowIndex].fullName = dataItem.fullName;
					datasource._data[selectedRowIndex].employeeId = dataItem.id;
					vm.dataSave.push(datasource._data[selectedRowIndex]);
					$("#settlementGrid").data('kendoGrid').refresh();
					$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
					vm.isAddnew = false;
					openning = false;
					vm.adding =false;
				}
			});
	    	
	    	
	    }
	    var record = 0;
	    function fillDataTableUserInfo(data) {
			var deferred = $q.defer();
		   	vm.optionsUserInfo = kendoConfig.getGridOptions({
		   		autoBind : true,
				dataSource: {

					serverPaging: true,

					transport: {
						read: {
							url: Constant.BASE_SERVICE_URL + "catEmployeeServiceRest/DoSearch",
							contentType: "application/json; charset=utf-8",
							type: "POST"
						},
						parameterMap: function (options, type) {
                              //  vm.criteria.employeeId = Constant.user.srvUser.catEmployeeId;
							vm.criteriaEmployee.page = options.page
							vm.criteriaEmployee.pageSize = options.pageSize

								return JSON.stringify(vm.criteriaEmployee)

						}
					},
					schema: {

						total: function (response) {
							console.log(response);
							return response.total; // total is returned in the "total" field of the response
						},
						data: function (response) {
							console.log(response);
							return response.data; // data is returned in the "data" field of the response
						},
					},

					pageSize: 20

				},
				pageable: {
					refresh: true,
					pageSizes: [10, 15, 20, 25],
					messages: {
		                display: " {0}-{1} c???a {2} k???t qu???",
		                itemsPerPage: "k???t qu???/trang"

		            }
				},

				dataBinding: function () {
					record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
				},
		   		noRecords : true,
		   		messages : {
		   			noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? n??o")
		   		},
				columns : [
						{
							title : gettextCatalog.getString("<b>STT</b>"),
							template: function (item) {
								return ++record;
							},
							headerAttributes: {
						      	"class": "color-black",
						      	style: "text-align: center; vertical-align: middle;"
						    	},
				   			attributes: { style: "text-align:center; " },
				   			editable:false,
				   			editor: nonEditor,
							width : 70
						},
						{
							title : gettextCatalog.getString("<b>Ch???n</b>"),
							template : "<input type='radio' name='gridcheckbox' />",
							headerAttributes: {
						      	"class": "color-black",
						      	style: "text-align: center; vertical-align: middle;"
						    	},
				   			attributes: { style: "text-align:center;" },
				   			editable:false,
				   			editor: nonEditor,
							width : 80
						},
						{
							title : gettextCatalog
									.getString("H??? T??n"),
							field : "fullName",
							headerAttributes: {
						      	"class": "color-black",
						      	style: "text-align: center"
						    	},
						    	editable:false,
					   			editor: nonEditor,
							width : 150
						},
						{
							title : gettextCatalog
									.getString("Email"),
							field : "email",
							headerAttributes: {
						      	"class": "color-black",
						      	style: "text-align: center"
						    	},
						    	editable:false,
					   			editor: nonEditor,
							width : 150
						},
						{
							title : gettextCatalog
									.getString("????n v???"),
							field : "groupName",
							headerAttributes: {
						      	"class": "color-black",
						      	style: "text-align: center"
						    	},
						    	editable:false,
					   			editor: nonEditor,
							width : 150
						}
						,
						{
							title : gettextCatalog
									.getString("?????i t??c"),
							field : "partnerName",
							headerAttributes: {
						      	"class": "color-black",
						      	style: "text-align: center"
						    	},
						    	editable:false,
					   			editor: nonEditor,
							width : 150
						}
						
				,
						{
				   			title : gettextCatalog.getString("Lo???i nh??n vi??n"),
				   			field : "type",
				   			template : function(data) {
								if(data.type == 1) {
									return "viettel";
								}else if (data.type == 2) {
									return "Ngo??i viettel";
								}else {
									return data.type;
								}
							},
							editable:false,
				   			editor: nonEditor,
							width : 150
						}
						]
			});
			deferred.resolve('done');
		   	return deferred.promise;
		}
	    
	 // khong cho edit trong grid
		function nonEditor(container, options) {
	         container.text(options.model[options.field]);
	     }
		
		vm.keydown = function($event){
			 if (event.keyCode == 13) {
				 vm.DoSearch();
			    }
		        
		    }

 
	}
})();