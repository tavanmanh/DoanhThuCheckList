(function() {
	'use strict';

	var controllerId = 'DsbgvtController';

	angular.module('MetronicApp').controller(controllerId, DsbgvtController);

	/* @ngInject */
	function DsbgvtController($scope, $rootScope, $timeout, Constant,
			gettextCatalog, kendoConfig, WindowService, dsbgvtService,bgvttbacService,
			CommonService, $q, RestEndpoint, $kWindow,ProposalEvaluation, theSignCA) {
		var vm = this;
		vm.hideAButton = hideAButton;
		vm.onChange = onChange;
        vm.changeSite = changeSite;
		vm.getDsbgvt = getDsbgvt;
		vm.chkSelectAll = chkSelectAll;
		vm.chkSelectAllBoxung = chkSelectAllBoxung;
		vm.chkSelectItemBoxung = chkSelectItemBoxung;
		vm.multiDelete = multiDelete;
		vm.showDetail = false;
		vm.isCreateNew = false;
		vm.boxung = boxung;
		vm.theSign = {};
		var checkonchange = 0;
		
		vm.validatorOptions = kendoConfig.get('validatorOptions');
		
		vm.fillData = fillData;
		vm.save = save;
		vm.exportFile = exportFile;
		vm.exportFileDoc = exportFileDoc;
		vm.contractGridItem = {};
		vm.objExport = {};
		vm.onSave=onSave;
		vm.item = {
				partnerName:'',
				contractCode:'',
				investProjectName:'',
				constrtCode:'',
				constrtName:'',
				constrType:'',
				provinceId:'',
				constructId:''
		};
		
		vm.item = ProposalEvaluation.getItem();
		if(vm.item == null) {
			vm.item = CommonService.getItem();
		}
		
		vm.roleThutruong = {
				constructId : vm.item.constructId,
				contractCode  : vm.item.contractCode,
				roleid:'',
			};
		
		vm.roleNguoigiao = {
				constructId : vm.item.constructId,
				role : '2',
				type : '1'
			};
		
		vm.roleGiamdoc = {
				constructId : vm.item.constructId,
				contractCode  : vm.item.contractCode,
				roleid:'',
			};
		
		vm.roleNguoinhan = {
				constructId : vm.item.constructId,
				role : '2',
				type : '2'
			};
		dsbgvtService.getRoleId().then(function(data) {
			vm.role = data;
			vm.roleThutruong.roleid = Constant.ROLE_ID["employee_roleID_CDT_DDPN"];//2
			vm.roleGiamdoc.roleid = Constant.ROLE_ID["employee_roleID_DT_GDNT"];//3
			getThuTruong(vm.roleThutruong);
			getNguoigiao(vm.roleNguoigiao);
			getGiamdoc(vm.roleGiamdoc);
			getNguoinhan(vm.roleNguoinhan);
		})
		.catch(function(data, status) {
			console.error('getRoleId error', response.status, response.data);
		});
		
		
		
		
		if(vm.item == null) {
			alert("Ch??a ch???n b???n ghi n??o.");
			return;
		}
		
		vm.materialObj = {
				code:'',
				constrtCode:'',
				contractCode:'',
				handoverFromDate:'',
				handoverToDate:'',
				statusCa:''
		};
		vm.dspxk = [];
		
		vm.dsbgvtObj = {
				constructId:'',
				shd : '',
				tenhd : '',
				mact : '',
				tenct : '',
				diachict : ''
			}
		vm.role=[];
		dsbgvtService.getRoleId().then(function(data) {
			vm.role = data;
		})
		.catch(function(data, status) {
			console.error('getRoleId error', response.status, response.data);
		});
		
		vm.thutruong = [];
		vm.nguoigiao = [];
		vm.giamdoc = [];
		vm.nguoinhan = [];
		
		fillData(vm.item);
		
		function fillData(object) {
			dsbgvtService.getConTruction(object).then(function(data) {
				vm.dsbgvtObj.constructId = data.constructId;
				dsbgvtService.setItem(data.constructId);
				vm.dsbgvtObj.shd = data.contractCode;
				vm.dsbgvtObj.tenhd = data.contractName;
				vm.dsbgvtObj.mact = data.constrtCode;
				vm.dsbgvtObj.tenct = data.constrtName;
				vm.dsbgvtObj.diachict = data.constrtAddress;
			  })
			  .catch(function(data, status) {
			    console.error('getConTruction error', response.status, response.data);
			  });
			
			vm.constrObject = {
					contractCode : vm.item.contractCode,
					constrtCode : vm.item.constrtCode,
					constructId: vm.item.constructId
				}
			getDsbgvt(vm.constrObject);
			
			
			
		}
		
		
		function getDsbgvt(object) {
			dsbgvtService.getDsbgvt(object).then(function(data) {
				vm.dspxk = data.plain();
				fillDataTableds(vm.dspxk) ;
			  })
		}

		function toDate(dateStr) {	
			var parts = dateStr.split("/");
			return new Date(parts[2], parts[1] - 1, parts[0]);
		}
		
		function chkSelectAll(item) {
	    	var grid = vm.contractGrid;
	    	chkSelectAllBase(vm.chkAll, grid);
		}
		
		function chkSelectAllBoxung(item) {
	    	var grid = vm.pxkGridBoxung;
	    	chkSelectAllBase(vm.chkAllBoxung, grid);
		}
		function chkSelectItemBoxung(item) {
	    	if($("#gridcheckbox").is(':checked')) {
	    		
	    	}else {
	    		$("#gridchkselectallBoxung").prop('checked', false);
	    	}
		}
		vm.handleCheck = function(item){
			if(document.getElementById("checkalllistacap").checked == true){
				document.getElementById("checkalllistacap").checked = false;
			}
		}
		
		function fillDataTableds(data) {
		   	vm.options = kendoConfig.getGridOptions({
		   		autoBind : true,
		   		dataSource : data,
		   		change : onChange,
		   		editable:false,
		   		noRecords : true,
		   		messages : {
		   			noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? n??o")
		   		},
		   		columns : [
		   		{
		   			title : gettextCatalog.getString("STT"),
					field : "index",
					headerAttributes: {
				      	"class": "color-black",
				      	style: "text-align: center"
				    	},
				    attributes: { style: "text-align:center;" },
					template: dataItem => $("#contractGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
					width : 40
		   		},
		   		{
		   			title : "<input type='checkbox' id='checkalllistacap' name='gridchkselectall' ng-click='vm.chkSelectAll();' ng-model='vm.chkAll' />",
		   			template : "<input type='checkbox' name='gridcheckbox' ng-click='vm.handleCheck()'/>",
		   			headerAttributes: {
				      	"class": "color-black",
				      	style: "text-align: center"
				    	},
				    attributes: { style: "text-align:center;" },
					width : 35
		   		},
		   		{
		   			title : gettextCatalog.getString("M?? Phi???u"),
		   			field : "code",
		   			editable:false,
		   			editor: nonEditor,
		   			headerAttributes: {
				      	"class": "color-black",
				      	style: "text-align: center"
				    	},
		   			width : 100
		   		},
		   		{
		   			title : gettextCatalog.getString("M?? c??ng tr??nh"),
		   			field : "constrtCode",
		   			editable:false,
		   			editor: nonEditor,
		   			headerAttributes: {
				      	"class": "color-black",
				      	style: "text-align: center"
				    	},
		   			width : 90
		   		},
		   		{
		   			title : gettextCatalog.getString("M?? h???p ?????ng"),
		   			field : "contractCode",
		   			editable:false,
		   			editor: nonEditor,
		   			headerAttributes: {
				      	"class": "color-black",
				      	style: "text-align: center"
				    	},
		   			width : 90
		   		},
		   		{
		   			title : gettextCatalog.getString("Ng??y b???t ?????u"),
		   			field : "handoverFromDate",
		   			editable:false,
		   			editor: nonEditor,
		   			headerAttributes: {
				      	"class": "color-black",
				      	style: "text-align: center"
				    	},
				    	attributes: { style: "text-align:center;" },
		   			width : 80
		   		},
		   		{
		   			title : gettextCatalog.getString("Ng??y k???t th??c"),
		   			field : "handoverToDate",
		   			editable:false,
		   			editor: nonEditor,
		   			headerAttributes: {
				      	"class": "color-black",
				      	style: "text-align: center"
				    	},
				    	attributes: { style: "text-align:center;" },
		   			width : 80
		   		},
		   		{
		   			title : gettextCatalog.getString("Tr???ng th??i"),
		   			field : "statusCa",
		   			template : function(data) {
						if(data.statusCa == 0) {
							return "So???n th???o";
						}else if (data.statusCa == 1) {
							return "Tr??nh k??";
						}else if (data.statusCa == 2) {
							return "???? k??";
						}else if (data.statusCa == 3) {
							return "T??? ch???i k??";
						}else {
							return data.statusCa;
						}
					},
					editable:false,
					editor: nonEditor,
					headerAttributes: {
				      	"class": "color-black",
				      	style: "text-align: center"
				    	},
				    	attributes: { style: "text-align:left;", class:"statusColumn"},
		   			width : 80
		   		}]
		   	});
		   }
		   function reloadTable() {
		   		fillDataTable(vm.dspxk).then(function(result){
		   			try {
		   				var grit = vm.contractGrid;
		   				if(grit){
		   					grit.dataSource.data(vm.dspxk.plain());
		   					vm.contractGrid.refresh();
		   				}
		   			} catch (e) {
		   				console.log(e);
		   			}
		   		});
		   	}

		  	function onChange(){
		  		checkonchange = 0;
		  		if (vm.contractGrid.select().length > 0) {
					var tr = vm.contractGrid.select().closest("tr");
					var dataItem = vm.contractGrid.dataItem(tr);
					vm.materialObj = dataItem;
					
					vm.materialObj.adirectorId = vm.materialObj.adirectorId;
					vm.materialObj.ahandoverPersonId = vm.materialObj.ahandoverPersonId;
					vm.materialObj.bdirectorId = vm.materialObj.bdirectorId;
					vm.materialObj.breceivePersonId = vm.materialObj.breceivePersonId;
					vm.materialObj.handoverPlace = vm.materialObj.handoverPlace;
					vm.materialObj.otherComment = vm.materialObj.otherComment;
					
					vm.materialObj.statusCa = dataItem.statusCa;
					vm.signPlace = vm.materialObj.signPlace;
					vm.signDate = vm.materialObj.signDate;
					
					vm.theSign.code = vm.materialObj.code; 
					vm.theSign.constructId=vm.item.constructId;
					vm.theSign.constrCompReMapId=vm.materialObj.constrCompReMapId;
					vm.theSign.stringEmployee=vm.materialObj.ahandoverPersonId+","+vm.materialObj.breceivePersonId+","+vm.materialObj.adirectorId +","+ vm.materialObj.bdirectorId;
					vm.theSign.isSign='theSignCA';
					
					checkonchange = checkonchange +1;
					vm.objExport = {
							amaterialHandoverId: dataItem.amaterialHandoverId,
					}
				}
		  	}
		  	
		  	vm.vttbItem = {};
		  	function onChangeVTTB(){
		  		if (vm.vttbGrid.select().length > 0) {
					var tr = vm.vttbGrid.select().closest("tr");
					var dataItem = vm.vttbGrid.dataItem(tr);
					vm.vttbItem = dataItem;
					
					var grid = $('#vttbGrid').data("kendoGrid");
					var selectedRow = grid.select();
					selectedRowIndex = selectedRow.index();
				}
		  	}
		  	
		  	vm.itempxk = {};
		  	function onChangePxk(){
		  		if (vm.pxkGrid.select().length > 0) {
					var tr = vm.pxkGrid.select().closest("tr");
					var dataItem = vm.pxkGrid.dataItem(tr);
					vm.itempxk = dataItem;
				}
		  	}
		  	
		  	vm.itempxkBoxung = {};
		  	function onChangePxkBoxung(){
		  		if (vm.pxkGridBoxung.select().length > 0) {
					var tr = vm.pxkGridBoxung.select().closest("tr");
					var dataItem = vm.pxkGridBoxung.dataItem(tr);
					vm.itempxkBoxung = dataItem;
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
		    
		    function multiDelete() {
		    	if($('#ds').css('display') == 'none') {
		    		var selectedRow = [];
					var grid = vm.pxkGrid;
					//vm.pxkGrid.select("tr:eq(1)");
					grid.table.find("tr").each(function(idx, item) {
						var row = $(item);
						var checkbox = $('[name="gridcheckbox"]', row);

						if (checkbox.is(':checked')) {
							// Push id into selectedRow
							var tr = grid.select().closest("tr");
							var dataItem = grid.dataItem(item);
							
							selectedRow.push(dataItem.amaterialHandoverId);
							
						}
					});

					if (selectedRow.length == 0) {
						toastr.warning(gettextCatalog.getString("Ch??a ch???n b???n ghi n??o ????? x??a!"));
						return;
					}
					
					if (confirm('X??c nh???n x??a')) {	
						toastr.success(gettextCatalog.getString("X??a t???  pxkGrid!"));
						var data = vm.vttbGrid.dataSource.data();
						
						refreshPxkGrid();
							
					}
		    	}else {
		    		var selectedRow = [];
		    		var selectedRowNotDelete = [];
					var grid = vm.contractGrid;
					//vm.contractGrid.select("tr:eq(1)");
					grid.table.find("tr").each(function(idx, item) {
						var row = $(item);
						var checkbox = $('[name="gridcheckbox"]', row);

						if (checkbox.is(':checked')) {
							// Push id into selectedRow
							//var tr = grid.select().closest("tr");
							var dataItem = grid.dataItem(item);
							if(dataItem.createdUserId == Constant.user.srvUser.userId) {
								selectedRow.push(dataItem.amaterialHandoverId);
							 }else {
								 selectedRowNotDelete.push(dataItem.amaterialHandoverId);
							 }
						}
					});

					if (selectedRowNotDelete.length > 0 && selectedRow.length == 0) {
						toastr.warning(gettextCatalog.getString("B???n kh??ng c?? quy???n x??a!"));
						return;
					}
					
					if (selectedRow.length == 0 && selectedRowNotDelete.length == 0) {
						toastr.warning(gettextCatalog.getString("Ch??a ch???n b???n ghi n??o ????? x??a!"));
						return;
					}

					
					
					if (confirm('X??c nh???n x??a')) {	
						dsbgvtService.deleteMaterial(selectedRow).then(function() {
							toastr.success(gettextCatalog.getString("X??a  th??nh c??ng!"));
							if(vm.obj != null) {
								vm.obj.signDate='';
			    			 	vm.obj.signPlace='';
							}
							vm.showDetail = false;
							$('#ds').show();
							// reload danh sach
							refreshGrid(vm.item);
							// reloadcrevaluation();
						}, function(errResponse) {
					         if (errResponse.status == 302){
					             toastr.warning("B???n ghi kh??ng ???????c x??a");
					            }else{
					             toastr.warning("B???n ghi ??ang tr??nh k?? ho???c ???? k?? kh??ng ???????c x??a");
					         }
				         });
					}
		    	}
		}
		    
		function reloadPxkGrid() {
			vm.listExpCode = [];
			var data = vm.pxkGrid.dataSource.data();
			for(var i = 0; i < data.length; i++) {
				vm.listExpCode.push(data[i].expNoteCode);
			}
			
			if(vm.listExpCode.length > 0) {
				var dataVTTB = vm.vttbGrid.dataSource.data();
				for(var i = 0; i < dataVTTB.length; i++) {
					var exist = false;
					if(vm.listExpCode.includes(dataVTTB[i].expNoteCode)) {
						 exist = true;
					}
					if(!exist) {
						dataVTTB.remove(dataVTTB[i]);
						i--;
						// dataVTTB.sync();
					}
				}
				
			}else {
				vm.vttbGrid.dataSource.data([]);
				vm.vttbGrid.refresh();
			}
		}
		    
	    function refreshGrid(object) {
	    	dsbgvtService.getDsbgvt(vm.constrObject).then(function(data) {
				var grid = vm.contractGrid;
				if(grid){
					grid.dataSource.data(data.plain());
					grid.refresh();
				}
			  });
		}
		    
		
	    function refreshPxkGrid() {
	    	vm.constrObj.constructId = vm.item.constructId;
			loadExpNote(vm.constrObj);
			var grid = vm.pxkGrid;
			if(grid){
				grid.dataSource.data(data.plain());
				grid.refresh();
			}
	    }
	    
		vm.goToAdd = goToAdd;

		  / Handle action client on a menu item /
		  function goToAdd(menuKey) {
			  dsbgvtService.setItem1(null);
			  
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

		vm.goToUpdate = goToUpdate;

		  / Handle action client on a menu item /
		  function goToUpdate(menuKey) {
			   getDataRowSelect().then(function(){
			      if(vm.contractGrid.select().length > 0){
			       $rootScope.$broadcast("bgvttbacService.detail");
			     }else{
			           return;
			     }
			   });
			   
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
		  
		  function getDataRowSelect(){
		         var demopromise = $q.defer();
		         var grid = vm.contractGrid;
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
		  
		// add new
			vm.add = function add(){
				
				vm.objExport = {};
				vm.isCreateNew = true;
				vm.showDetail = true;
				vm.constrObject.constructId = vm.item.constructId;
				vm.signPlace='' ;
		    	vm.signDate='';
				vm.materialObj.handoverFromDate = '';
				vm.materialObj.handoverToDate = '';
				vm.materialObj.handoverPlace = '';
				vm.materialObj.otherComment = '';
				
				if(vm.thutruong != null && vm.thutruong.length > 0)
				vm.materialObj.adirectorId = vm.thutruong[0].id;
				if(vm.nguoigiao != null && vm.nguoigiao.length > 0) 
				vm.materialObj.ahandoverPersonId = vm.nguoigiao[0].id;
				if(vm.giamdoc != null && vm.giamdoc.length > 0) 
				vm.materialObj.bdirectorId = vm.giamdoc[0].id;
				if(vm.nguoinhan != null && vm.nguoinhan.length > 0) {
					vm.materialObj.breceivePersonId = vm.nguoinhan[0].id;
				}else {
					getNguoinhan(vm.roleNguoinhan);
				}
					
				
				vm.code = '';
				vm.listVTTB = [];
				vm.listExpCode = [];
				vm.constrObj.constructId = vm.item.constructId;
				loadExpNote(vm.constrObj);
				$('#ds').hide();
				
				vm.materialObj.code = null;
				
			}
			
			// edit
			vm.edit = function edit(){
				$(".k-invalid-msg").hide();
				if(vm.materialObj.code == null && vm.materialObj.handoverFromDate != "" && vm.materialObj.handoverToDate != "" && !vm.isCreateNew) {
					toastr.warning(gettextCatalog.getString("Ch??a ch???n b???n ghi n??o!"));
					return;
				}else {
					if(!vm.showDetail) {
						if(vm.materialObj.statusCa == 1 || vm.materialObj.statusCa == 2){
							//toastr.warning("Kh??ng ???????c s???a nh???ng b???n ghi ??ang tr??nh k?? ho???c ???? k??");
							vm.disableAll = true;
						}else{
							vm.disableAll = false;
						}
						if(vm.materialObj.createdUserId != Constant.user.srvUser.userId){
							//toastr.warning("Kh??ng ???????c s???a nh???ng b???n ghi ??ang tr??nh k?? ho???c ???? k??");
							vm.disableAll = true;
						}else{
							vm.disableAll = false;
						}
						vm.isCreateNew = false;
						vm.showDetail = true;
						vm.code = '';
						vm.listVTTB = [];
						vm.listExpCode = [];
						vm.constrObj.constructId = vm.item.constructId;
						loadExpNote(vm.constrObj);
						
						vm.signPlace= vm.materialObj.signPlace;
						vm.signDate= vm.materialObj.signDate;
						
							if(vm.thutruong != null && vm.thutruong.length > 0)
							vm.materialObj.adirectorId = vm.thutruong[0].id;
							if(vm.nguoigiao != null && vm.nguoigiao.length > 0) 
							vm.materialObj.ahandoverPersonId = vm.nguoigiao[0].id;
							if(vm.giamdoc != null && vm.giamdoc.length > 0) 
							vm.materialObj.bdirectorId = vm.giamdoc[0].id;
							if(vm.nguoinhan != null && vm.nguoinhan.length > 0) {
								vm.materialObj.breceivePersonId = vm.nguoinhan[0].id;
							}else {
								getNguoinhan(vm.roleNguoinhan);
							}
						
						vm.materialObj.adirectorId = vm.materialObj.adirectorId;
						vm.materialObj.ahandoverPersonId = vm.materialObj.ahandoverPersonId;
						vm.materialObj.bdirectorId = vm.materialObj.bdirectorId;
						vm.materialObj.breceivePersonId = vm.materialObj.breceivePersonId;
						vm.materialObj.handoverPlace = vm.materialObj.handoverPlace;
						vm.materialObj.otherComment = vm.materialObj.otherComment;
						$('#ds').hide();
					}else {
						refreshGrid(vm.item);
						vm.showDetail = false;
						$('#ds').show();
					}
					
				}
				
			}
			
			// undo
			vm.undo = function undo(){
				refreshGrid(vm.item);
				vm.showDetail = false;
				$('#ds').show();
			}
			
			
			vm.datePickerConfig = {
			        format: "dd/MM/yyyy HH:mm:ss",
			        parseFormats: ["yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm:ss", "yyyy/MM/dd HH:mm:ss"],
			        footer: "Currently #: kendo.toString(data,'dd-MM-yyyy HH:mm:ss')#"
			    };
			
			vm.constrObj = {
					constructId: '',
					shd : '',
					ngaykyhd : '',
					mact : '',
					tenct : '',
					diachict : '',
					tai : '',
					stationCode:''
				};
			
			vm.materialObj = {
			};
			
			
			// minhpvn
			
			function UniqueArraybyId(collection, keyname) {
	            var output = [], 
	                keys = [];

	            angular.forEach(collection, function(item) {
	                var key = item[keyname];
	                if(keys.indexOf(key) === -1) {
	                    keys.push(key);
	                    output.push(item);
	                }
	            });
	            return output;
	        };
	        // ========
			// lay danh sach thu truong start
			
			
			function getThuTruong(object) {
				bgvttbacService.getEmployeeByRole(object).then(function(data) {
					vm.thutruong = data.plain();
					if(vm.thutruong.length > 0) {
						vm.thutruong = UniqueArraybyId(vm.thutruong ,"id");
						vm.thutruong.id = vm.thutruong[0].id;
						vm.materialObj.adirectorId = vm.thutruong[0].id;
					}
					
				  })
				  .catch(function(data, status) {
				    console.error('getThuTruong error', response.status, response.data);
				  })
				  .finally(function() {
				    console.log("finally finished getThuTruong");
				  });
			}
			// lay danh sach thu truong end
			
			// lay danh sach nguoi giao start
			
			
			function getNguoigiao(object) {
				bgvttbacService.getEmployeeByRoleShiper(object).then(function(data) {
					vm.nguoigiao = data.plain();
					if(vm.nguoigiao.length > 0) {
						vm.nguoigiao = UniqueArraybyId(vm.nguoigiao ,"id");
						vm.nguoigiao.id = vm.nguoigiao[0].id;
						vm.materialObj.ahandoverPersonId = vm.nguoigiao[0].id;
					}
					
				  })
				  .catch(function(data, status) {
				    console.error('getNguoigiao error', response.status, response.data);
				  })
				  .finally(function() {
				    console.log("finally finished getNguoigiao");
				  });
			}
			// lay danh sach nguoi giao end
			
			// lay danh sach giam doc start
			
			
			function getGiamdoc(object) {
				bgvttbacService.getEmployeeByRole(object).then(function(data) {
					vm.giamdoc = data.plain();
					if(vm.giamdoc.length > 0) {
						vm.giamdoc = UniqueArraybyId(vm.giamdoc ,"id");
						vm.giamdoc.id = vm.giamdoc[0].id;
						vm.materialObj.bdirectorId = vm.giamdoc[0].id;
					}
					
				  })
				  .catch(function(data, status) {
				    console.error('getGiamdoc error', response.status, response.data);
				  })
				  .finally(function() {
				    console.log("finally finished getGiamdoc");
				  });
			}
			// lay danh sach ngiam doc end
			
			// lay danh sach nguoi nhan start
			function getNguoinhan(object) {
				if(vm.nguoinhan == null || vm.nguoinhan.length == 0) {
					bgvttbacService.getEmployeeByRoleShiper(object).then(function(data) {
						vm.nguoinhan = data.plain();
						if(vm.nguoinhan.length > 0) {
							vm.nguoinhan = UniqueArraybyId(vm.nguoinhan ,"id");
							vm.nguoinhan.id = vm.nguoinhan[0].id;
							vm.materialObj.breceivePersonId = vm.nguoinhan[0].id;
						}
						
					  })
					  .catch(function(data, status) {
					    console.error('getNguoinhan error', response.status, response.data);
					  })
					  .finally(function() {
					    console.log("finally finished getNguoinhan");
					  });
				}
				
			}
			// lay danh sach nguoi nhan end
			
			// load danh sach phieu xuat kho da xuat ra cong trinh start
			
			function loadExpNote(object) {
				// load danh sach vat tu thiet bi them moi
				if(vm.isCreateNew == true) {
					// load bang rong
					var grid = vm.pxkGrid;
					if(grid){
						grid.dataSource.data([]);
						grid.refresh();
					}else {
						fillDataTable(null) ;
					}
					if(vm.vttbGrid){
						vm.vttbGrid.dataSource.data([]);
						vm.vttbGrid.refresh();
					}else {
						fillDataTableVTTB(null) ;
					}	
				}
				
				// load danh sach vat tu thiet bi edit
				if(vm.isCreateNew == false) {
					var listPxk = [];
					bgvttbacService.getListAMaterialHandOverMerList(vm.materialObj).then(function(dataVTTB){
						dataVTTB = dataVTTB.plain();
						
						$.each( dataVTTB, function( index, item ) {
							listPxk.push(item.expNoteCode);
						});
						
						if(vm.vttbGrid){
							vm.vttbGrid.dataSource.data(dataVTTB);
							vm.vttbGrid.refresh();
						}else {
							fillDataTableVTTB(dataVTTB) ;
						}
						
						if(listPxk.length > 0) {
							bgvttbacService.getwareExpCmdByPxk(listPxk).then(function(data) {
								
								if(vm.pxkGrid){
									vm.pxkGrid.dataSource.data(data.plain());
									vm.pxkGrid.refresh();
								}else {
									fillDataTable(data.plain()) ;
								}
							});
						}
					});
				}
			}
			
			function fillDataTable(data) {
				/*if(data != null) {
					for(var i = 0; i < data.length; i++) {
						if(data[i].status == '1') {
							data[i].status= "Ch??a b??n giao";
						}else if (data[i].status == '2') {
							data[i].status= "???? b??n giao";
						}else if (data[i].status == '3') {
							data[i].status= "???? h???y";
						}else if (data[i].status == '4') {
							data[i].status= "???? t???o l???nh nh???p";
						}
					
					}
				}*/
				
			   	var deferred = $q.defer();
			   	vm.options1 = kendoConfig.getGridOptions({
			   		autoBind : true,
			   		scrollable:true,
			   		dataSource : {data:data},
			   		change : onChangePxk,
			   		noRecords : true,
			   		editable: false,
			   		messages : {
			   			noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? n??o")
			   		},
			   		columns : [
			   		{
			   			title : gettextCatalog.getString("STT"),
						field : "index",
						template: dataItem => $("#pxkGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
						headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: center"
					    	},
					    attributes: { style: "text-align:center;" },
						width : 70
			   		},
			   		{
			   			title : "<b>X??a</b>",
			   			template: function (item){ 
			   				return "<div style=\"text-align:center\" class='action-button del' id='xoapxk' ng-mouseup='vm.deletePxk(this)' style='display:block' ></div>"
			   				}, 
			   			click: vm.deletePxk,
			   			headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: center"
					    	},
					    attributes: { style: "text-align:center;" },
						width : 70
			   		},
			   		{
			   			title : gettextCatalog.getString("M?? phi???u xu???t kho"),
			   			field : "expNoteCode",
			   			headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: center"
					    	},
					    	editable: false,
				   			editor: nonEditor,
			   			width : 160
			   		},
			   		{
			   			title : gettextCatalog.getString("Ng??y t???o"),
			   			field : "createdDateStr",
			   			headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: center"
					    	},
					    	attributes: { style: "text-align:center;" },
					    	editable: false,
				   			editor: nonEditor,
			   			width : 160
			   		},
			   		{
			   			title : gettextCatalog.getString("Ng??y th???c xu???t"),
			   			field : "actualExpDateStr",
			   			headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: center"
					    	},
					    	attributes: { style: "text-align:center;" },
					    	editable: false,
				   			editor: nonEditor,
			   			width : 160
			   		},
			   		{
			   			title : gettextCatalog.getString("Tr???ng th??i b??n giao"),
			   			field : "status",
						headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: center"
					    	},
						attributes: { style: "text-align:left;" },
						editable: false,
			   			template: function($scope){
			   				if($scope.status == 1) {
			   					return "Ch??a b??n giao";
							}else if ($scope.status == 2) {
								return "???? th???c xu???t";
							}else if ($scope.status == 3) {
								return "???? h???y";
							}else if ($scope.status == 4) {
								return "???? t???o l???nh nh???p";
							}
			   			},
			   			width : 130
			   		}]
			   	});
			}
			// load danh sach phieu xuat kho da xuat ra cong trinh end
			
			vm.deletePxk= function deletePxk (e) {
				var dataSource = $("#pxkGrid").data("kendoGrid").dataSource;
                // dataSource.remove(vm.itempxk);
                dataSource.remove(e.dataItem);
                dataSource.sync();
                reloadPxkGrid();
               // refreshPxkGrid();
            }
			
			// load danh sach thiet bi vat tu start
			function fillDataTableVTTB(data) {
				var dataSource = new kendo.data.DataSource({
					data:data,
					pageSize: 20,
					schema: {
	                    model: {
	                        id: "vttbGrid",
	                    	fields: {
	                    		handoverQuantity: { validation: { /*required: true , */required: { message: "Kh??ng b??? tr???ng ?????i t?????ng ki???m tra" } } },
	                    		actualReceiveQuantity: { validation: { /*required: true , */required: { message: "Kh??ng b??? tr???ng ?????i t?????ng ki???m tra" } } }
	                        }
	                    }
	                }
				});
				
				vm.optionsvttb = kendoConfig
						.getGridOptions({
							autoBind : true,
							dataSource :  dataSource,
							noRecords : true,
							messages : {
								noRecords : gettextCatalog
										.getString("Kh??ng c?? k???t qu??? n??o")
							},
							columns : [
									{
			   			title : gettextCatalog.getString("STT"),
						field : "indexh",
						editable: false,
						editor: nonEditor,
						template: dataItem => $("#vttbGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
						headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: center"
					    	},
					    	attributes: { style: "text-align:center;" },
						width : 80
			   		},
			   		{
			   			title : gettextCatalog.getString("M?? phi???u xu???t kho"),
			   			field : "expNoteCode",
			   			editable: false,
						editor: nonEditor,
			   			headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: center"
					    	},
			   			width : 160
			   		},
			   		{
			   			title : gettextCatalog.getString("T??n v???t t??, TB"),
			   			field : "merName",
			   			editable: false,
						editor: nonEditor,
			   			headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: center"
					    	},
			   			width : 160
			   		},
			   		{
			   			title : gettextCatalog.getString("Serial Number"),
			   			field : "serialNumber",
			   			editable: false,
						editor: nonEditor,
			   			headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: center"
					    	},
			   			width : 160
			   		},
			   		{
			   			title : gettextCatalog.getString("????n v??? t??nh"),
			   			field : "unitName",
			   			editable: false,
						editor: nonEditor,
			   			headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: center"
					    	},
					    	attributes: { style: "text-align:left;" },
			   			width : 160
			   		},
			   		{
			   			title : gettextCatalog.getString("S??? l?????ng b??n giao"),
			   			field : "handoverQuantity",
			   			editable: false,
						editor: nonEditor,
				   		 template: function(dataItem) {
	                         if ($.isNumeric(dataItem.handoverQuantity)) {
	                        	 dataItem.handoverQuantity = parseFloat(Number(dataItem.handoverQuantity).toFixed(3));
	                             return parseFloat(Number(dataItem.handoverQuantity).toFixed(3));
	                         }else
	                         {
	                        	 dataItem.handoverQuantity = 0;
	                        	 return 0;
	                         }
	                     },
	                     format: "{0:n3}",
	                     decimals: 3,
			   			headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: center"
					    	},
					    	attributes: { style: "text-align:right;" },
			   			width : 160
			   		},
			   		{
			   			title : gettextCatalog.getString("???? b??n giao"),
			   			field : "sldabangiao",
			   			editable: false,
						editor: nonEditor,
				   		 template: function(dataItem) {
	                         if ($.isNumeric(dataItem.sldabangiao)) {
	                             return parseFloat(Number(dataItem.sldabangiao).toFixed(3));
	                         }
	                         return 0;
	                     },
	                     format: "{0:n3}",
	                     decimals: 3,
			   			headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: center"
					    	},
					    	attributes: { style: "text-align:right;" },
			   			width : 160
			   		},
			   		{
			   			title : gettextCatalog.getString("S??? l?????ng th???c nh???n"),
			   			field : "actualReceiveQuantity",
			   			template: function(dataItem) {
	                         if ($.isNumeric(dataItem.actualReceiveQuantity)) {
	                        	 dataItem.actualReceiveQuantity = parseFloat(Number(dataItem.actualReceiveQuantity).toFixed(3));
	                             return parseFloat(Number(dataItem.actualReceiveQuantity).toFixed(3));
	                         }
	                         else
	                         {
	                        	 dataItem.actualReceiveQuantity = 0;
	                        	 return 0;
	                         }
	                     },
	                     format: "{0:n3}",
	                     decimals: 3,
			   			headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: center"
					    	},
					    	attributes: { style: "text-align:right;" },
			   			width : 200
			   		},{
			   			title : gettextCatalog.getString("Hi???n tr???ng"),
			   			field : "qualityStatus",
			   			template : function(data) {
							if(data.qualityStatus == 1) {
								return "T???t";
							}else if (data.qualityStatus == 0) {
								return "H???ng";
							}else {
								return "T???t";
							}
			   			},
			   			headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: center"
					    	},
					    	attributes: { style: "text-align:left;" },
			   			editor:categoryDropDownEditor,
			   			width : 160
			   		},{
			   			title : gettextCatalog.getString("Ghi ch??"),
			   			field : "comments",
			   			headerAttributes: {
					      	"class": "color-black",
					      	style: "text-align: left"
					    	},
			   			width : 160
			   		}]
						});
			}
			
			// load danh sach thiet bi vat tu end
			
			// khong cho edit trong grid
			function nonEditor(container, options) {
		         container.text(options.model[options.field]);
		     }
			
			function categoryDropDownEditor(container, options) {
				$('<input required name="' + options.field + '" id="qualityStatus"/>')
				.appendTo(container)
				.kendoDropDownList({
					autoBind: false,
					change:onChangeQualityStatus,
					dataTextField: "value",
					dataValueField: "qualityStatus",
					dataSource: [
					             { qualityStatus: 0, value: "H???ng" }, 
					             { qualityStatus: 1, value: "T???t" }
					            ]
				});
				
			}
			
			var selectedRowIndex = -1;
			function onChangeQualityStatus() {
				if (vm.vttbGrid.select().length > 0 && selectedRowIndex != -1) {
					var datasource = vm.vttbGrid.dataSource;
					if(vm.vttbItem.qualityStatus.qualityStatus != null) {
						vm.vttbGrid.dataSource._data[selectedRowIndex].qualityStatus = vm.vttbItem.qualityStatus.qualityStatus;
					}else {
						vm.vttbGrid.dataSource._data[selectedRowIndex].qualityStatus = vm.vttbItem.qualityStatus;
					}
					vm.vttbGrid.refresh();
				}
		     }
			
			// add material start
			
			 function save(){
				 var isShowMsg = true;
				 if(!vm.isCreateNew && vm.materialObj.createdUserId != Constant.user.srvUser.userId) {
					 toastr.warning(gettextCatalog.getString("B???n kh??ng c?? quy???n s???a b???n ghi n??y"));
		 				return;
				 }
				 vm.validator._inputSelector = $rootScope.formLevel2.INPUTSELECTOR;
				 if (!vm.validator.validate()) return;			 
				 
				 if(vm.materialObj.handoverFromDate != null && vm.materialObj.handoverFromDate.length > 9) {
						var arrDateFrom = vm.materialObj.handoverFromDate.split("/");
						var arryear = arrDateFrom[2].split(" ");
						var arrHms = arryear[1].split(":");
			         	var validFrom = new Date();
			         		validFrom.setFullYear(arryear[0]);
					   		validFrom.setMonth((arrDateFrom[1]) - 1);
					   		validFrom.setDate(arrDateFrom[0]);
					   		validFrom.setHours(arrHms[0]);
					   		validFrom.setMinutes(arrHms[1]);
					   		validFrom.setSeconds(arrHms[2]);
					   		
			         	var arrDateTo = vm.materialObj.handoverToDate.split("/");
			         	
			         	arryear = arrDateTo[2].split(" ");
						arrHms = arryear[1].split(":");
						var validTo = new Date();
							validTo.setFullYear(arryear[0]);
							validTo.setMonth((arrDateTo[1]) - 1);
							validTo.setDate(arrDateTo[0]);
							validTo.setHours(arrHms[0]);
							validTo.setMinutes(arrHms[1]);
							validTo.setSeconds(arrHms[2]);
			 			 if(validFrom <= validTo){
			 				
			 			 }else{
			 				toastr.warning(gettextCatalog.getString("Ng??y k???t th??c ph???i b???ng ho???c sau ng??y b???t ?????u"));
			 				return;
			 			 }
					}
				 
				 // get list object from table
				 var data = vm.vttbGrid.dataSource.data();
				 if(data.length <= 0) {
					 toastr.warning(gettextCatalog.getString("Ch??a c?? d??? li???u"));
		 				return;
				 }
				 
				 for(var i = 0; i < data.length; i++) {
					 if(data[i].handoverQuantity < 0 || data[i].actualReceiveQuantity < 0) {
						 toastr.warning(gettextCatalog.getString("S??? l?????ng kh??ng h???p l???"));
			 				return;
					 }
					 if(isShowMsg && data[i].amaterialHandoverId == null && data[i].sldabangiao + data[i].actualReceiveQuantity > data[i].handoverQuantity) {
						 isShowMsg = false;
						 if(!confirm("S??? l?????ng th???c nh???n v?????t qua s??? l?????ng b??n giao")) {
							 return;
						 }
					 }
					 if(isShowMsg && data[i].amaterialHandoverId != null && data[i].actualReceiveQuantity > data[i].handoverQuantity) {
						 isShowMsg = false;
						 if(!confirm("S??? l?????ng th???c nh???n v?????t qua s??? l?????ng b??n giao")) {
							 return;
						 }
					 }
						var VTTB = {
								aMateHandMerListId : data[i].aMateHandMerListId,
								expNoteCode : data[i].expNoteCode,
								handoverQuantity : data[i].handoverQuantity,
								merName : data[i].merName,
								unitId  : data[i].unitId,
								qualityStatus  : data[i].qualityStatus,
								serialNumber  : data[i].serialNumber,
								comments  : data[i].comments,
								merEntityId  : data[i].merEntityId,
								actualReceiveQuantity  : data[i].actualReceiveQuantity,
								isDevice  : data[i].isDevice,
								merId  : data[i].merId,
						}
						vm.listVTTB.push(VTTB);
				}
				
				 var aDirectorId = document.getElementById("thutruong").value ;
				 var aHandoverPersonId = document.getElementById("nguoigiao").value ;
				 var bReceivePersonId = document.getElementById("nguoinhan").value ;
				 var bDirectorId = document.getElementById("giamdoc").value ;
				 var handoverFromDate = vm.materialObj.handoverFromDate;
				 
		    	 var handoverToDate = vm.materialObj.handoverToDate;
		    	 
		    	 var handoverPlace = document.getElementById("tai").value;
		    	 var otherComments = document.getElementById("otherComment").value;
		    	 var statusCa = 0;
		    	 var documentCaId = 1;
		    	 var approvalDate = null;
		    	 var constrtCode = document.getElementById("shd").value;;
		    	 var constructId = vm.item.constructId;
		    	 var isActive = 1;
		    	 var signPlace =vm.signPlace ;
		    	 var signDate =vm.signDate;
		    	 vm.obj = {
		    			 amaterialHandoverId: vm.materialObj.amaterialHandoverId,
		    			 code: vm.materialObj.code,
		    			 adirectorId:aDirectorId,
		    			 ahandoverPersonId:aHandoverPersonId,
		    			 breceivePersonId:bReceivePersonId,
		    			 bdirectorId:bDirectorId,
		    			 handoverFromDate:handoverFromDate,
		    			 handoverToDate:handoverToDate,
		    			 handoverPlace:handoverPlace,
		    			 otherComments:otherComments,
		    			 statusCa:statusCa,
		    			 documentCaId:documentCaId,
		    			 approvalDate:approvalDate,
		    			 isActive:isActive,
		    			 constrtCode:constrtCode,
		    			 constructId:constructId,
		    			 listAmaterialMerlist:vm.listVTTB,
		    			 signDate:signDate,
		    			 createdUserId:Constant.user.srvUser.userId,
		    			 signPlace:signPlace
		    	 }
			    	
		    	 bgvttbacService.addAmaterial(vm.obj).then(function(data) {
		    		 if(data == -2){
		    			 toastr.warning(gettextCatalog.getString("Ch??? ???????c s???a nh???ng b???n ghi ??? tr???ng th??i So???n th???o"));
		    		 }else{
		    			 toastr.success(gettextCatalog.getString("X??? l?? th??nh c??ng!"));
		    		 }
	    			 	
						// reload danh sach
		    		 if(vm.obj != null) {
		    			 vm.obj.signDate='';
		    			 vm.obj.signPlace='';
		    		 }
	    			 	
					refreshGrid(vm.item);
					vm.showDetail = false;
					$('#ds').show();
				  }).catch(function(data, status) {
				    console.error('addAmaterial error', response.status, response.data);
				  }).finally(function() {
				    console.log("finally finished addAmaterial");
				  });
		    	 
		    	 vm.listVTTB = [];
		    	 vm.objExport = {
							amaterialHandoverId: vm.materialObj.amaterialHandoverId,
				 }
			 }
			// add material end
			
			function exportFile(){
				var selectedRow = [];
				var grid = vm.contractGrid;
				grid.table.find("tr").each(function(idx, item) {
				var row = $(item);
				var checkbox = $('[name="gridcheckbox"]', row);
					if (checkbox.is(':checked')){
						var dataItem = grid.dataItem(item);
						selectedRow.push(dataItem.amaterialHandoverId);
					}
				});
				if(selectedRow.length == 0 && checkonchange == 0 && vm.objExport.amaterialHandoverId == null )
				{
				    toastr.warning("B???n c???n ch???n b???n ghi ????? export !");
				}else{
					if(checkonchange == 1 && vm.showDetail == false && selectedRow.length == 0)
					{
				       	$('#loading').show();
				       	bgvttbacService.exportFile(vm.objExport).then(function(data){
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
				    	    bgvttbacService.exportList(selectedRow).then(function(data){
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
				    	    bgvttbacService.exportList(selectedRow).then(function(data){
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
				       	bgvttbacService.exportFile(vm.objExport).then(function(data){
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
			
			function exportFileDoc(){
				var selectedRow = [];
				var grid = vm.contractGrid;
				grid.table.find("tr").each(function(idx, item) {
				var row = $(item);
				var checkbox = $('[name="gridcheckbox"]', row);
					if (checkbox.is(':checked')){
						var dataItem = grid.dataItem(item);
						selectedRow.push(dataItem.amaterialHandoverId);
					}
				});
				if(selectedRow.length == 0 && checkonchange == 0 && vm.objExport.amaterialHandoverId == null )
				{
				    toastr.warning("B???n c???n ch???n b???n ghi ????? export !");
				}else{
					if(checkonchange == 1 && vm.showDetail == false)
					{
				       	$('#loading').show();
				       	bgvttbacService.exportFileDoc(vm.objExport).then(function(data){
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
				    	    bgvttbacService.exportListDoc(selectedRow).then(function(data){
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
				    	    bgvttbacService.exportListDoc(selectedRow).then(function(data){
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
				       	bgvttbacService.exportFileDoc(vm.objExport).then(function(data){
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
			
			function boxung() {
				if(vm.showDetail == true) {
					vm.constrObj.constructId = vm.item.constructId;
					bgvttbacService.getwareExpCmdByConstrt(vm.constrObj).then(function(result) {

	                    var templateUrl = 'qlhc/bgvttbac/AMaterialHandoverPopup.html';
	                    var title = gettextCatalog.getString("Danh s??ch phi???u ???? xu???t kho ra c??ng tr??nh");
	                    vm.gridData =result.plain();
	                    vm.optionsBoxung = kendoConfig.getGridOptions({
	                        autoBind: true,
	                        dataSource: vm.gridData,
	                        noRecords: true,
	                        messages: {
	                            noRecords: gettextCatalog.getString("Kh??ng c?? d??? li???u")
	                        },
	                    	columns : [
	               			   		{
	               			   			title : gettextCatalog.getString("STT"),
	               						field : "index",
	               						template: dataItem => $("#pxkGridBoxung").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
	               						headerAttributes: {
	               					      	"class": "color-black",
	               					      	style: "text-align: center"
	               					    	},
	               					    attributes: { style: "text-align:center;" },
	               						width : 70
	               			   		},
	               			   		{
	               			   			title : "<input type='checkbox' id='gridchkselectallBoxung' name='gridchkselectall' ng-click='chkSelectAll();' ng-model='vm.chkAllBoxung' />",
	               			   			template : "<input type='checkbox' name='gridcheckbox' ng-click='vm.chkSelectItemBoxung(this)' />",
	               			   			headerAttributes: {style:"text-align:center;"},
	               				        attributes:{style:"text-align:center;"},
	               						width : 50
	               			   		},
	               			   		{
	               			   			title : gettextCatalog.getString("M?? phi???u xu???t kho"),
	               			   			field : "expNoteCode",
	               			   			headerAttributes: {style:"text-align:center;"},
	               				        attributes:{style:"text-align:left;"},
	               			   			editable: false,
	               						editor: nonEditor,
	               			   			width : 180
	               			   		},
	               			   		{
	               			   			title : gettextCatalog.getString("Ng??y t???o"),
	               			   			field : "createdDateStr",
	               			   			headerAttributes: {style:"text-align:center;"},
	               				        attributes:{style:"text-align:center;"},
	               				        editable: false,
	               			   			editor: nonEditor,
	               			   			width : 120
	               			   		},
	               			   		{
	               			   			title : gettextCatalog.getString("Ng??y th???c xu???t"),
	               			   			field : "actualExpDateStr",
	               			   			headerAttributes: {style:"text-align:center;"},
	               				        attributes:{style:"text-align:center;"},
	               				        editable: false,
	               			   			editor: nonEditor,
	               			   			width : 180
	               			   		},
	               			   		{
	               			   			title : gettextCatalog.getString("Tr???ng th??i b??n giao"),
	               			   			field : "status",
	               						headerAttributes: {style:"text-align:center;"},
	               				        attributes:{style:"text-align:left;"},
	               				        editable: false,
	               						editor: nonEditor,
	               						template: function($scope){
	               			   				if($scope.status == 1) {
	               			   					return "Ch??a b??n giao";
	               							}else if ($scope.status == 2) {
	               								return "???? th???c xu???t";
	               							}else if ($scope.status == 3) {
	               								return "???? h???y";
	               							}else if ($scope.status == 4) {
	               								return "???? t???o l???nh nh???p";
	               							}
	               			   			},
	               			   			width : 180
	               			   		}]
	                    });
	                    CommonService.populateDataToGrid(templateUrl, title, vm.optionsBoxung, vm, 'boxung',null,true);
	                
					  });
					
				}
		    }
			
			  function onSave(popupId, dataSelect) {
				switch (popupId) {
				case 'boxung':{
				var data = vm.pxkGrid.dataSource.data();
				var selectedRow = [];
					for(var j = 0; j < dataSelect.length; j++){
						var flag = false;
						for(var i = 0; i < data.length; i++) {
						if(data[i].expNoteCode == dataSelect[j].expNoteCode) {
								 flag = true;
								 break;
							 }
						}
						if(flag == false) {
							data.push(dataSelect[j]);
							selectedRow.push(dataSelect[j].expNoteCode);
						}
					}
				if (selectedRow.length == 0) {
					toastr.warning(gettextCatalog.getString("Ch??a ch???n b???n ghi n??o!"));
					return;
				}else {
					bgvttbacService.getListAMaterial(selectedRow).then(function(dataVTTB){
						dataVTTB = dataVTTB.plain();
						var baseData = vm.vttbGrid.dataSource.data();
						for(var i = 0; i < dataVTTB.length; i++) {
							baseData.push(dataVTTB[i]);
						}
					});
				}
				
				break;
			}
				default:
					break;
			  }
			  }
			function onCancel(popupId) {
				switch (popupId) {
				case 'boxung':
					break;
				default:
					break;
				}
			}
			
			
			// start trinh k??
			vm.signCA = function signCA(){
				if(vm.materialObj.statusCa==1||vm.materialObj.statusCa==2){
					toastr.warning(gettextCatalog.getString("B???n ghi ???? tr??nh k??"));
					return;
				}
				if(vm.objExport != null && vm.objExport.amaterialHandoverId != null && vm.objExport.amaterialHandoverId != '') {
					$('#loading').show();
					bgvttbacService.exportFile(vm.objExport).then(function(data) {
						if(vm.role.length > 6) {
							vm.theSign.path=data.fileName;
							vm.theSign.nameFile='BM-TCNT-08.pdf';
							vm.theSign.roleName = ['Ng?????i giao','Ng?????i nh???n','Th??? tr?????ng', 'Gi??m ?????c'];
							// 4-5
							vm.theSign.roleId = ['0','0',Constant.ROLE_ID["employee_roleID_CDT_PTGST"],
							                     Constant.ROLE_ID["employee_roleID_DT_KTTC"]];
							theSignCA.setItem(vm.theSign);
							goTo('THE_SIGN_CA');
						}else {
							toastr.warning(gettextCatalog.getString("ROLEID NULL"));
						}
						  }).catch( function(){
								toastr.error(gettextCatalog.getString("L???i khi export!"));
								return;
							}).finally(function(){
								$('#loading').hide();
							});
							
				}else {
					toastr.warning(gettextCatalog.getString("Ch??a ch???n b???n ghi n??o!"));
					return;
				}
		     }
			
			$scope.$on("ChangeStatus", function(event, result){ 
				   if(result){
					   refreshGrid(vm.item);
				   }
				  });
			
			$scope.$on("ChangeTheSign", function(event, result){ 
				   if(result){
					   refreshGrid(vm.item);
				   }
				  });
			   
			function goTo(menuKey) {
			    var hasPerm = true;

			    if (hasPerm) {
			     var template = Constant.getTemplateUrl(menuKey);

			     postal.publish({
			      channel: "Tab",
			      topic: "open",
			      data: template
			     });
			    }
			}
			// end tr??nh k??

	}
	
})();