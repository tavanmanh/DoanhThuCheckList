(function() {
	'use strict';
	var controllerId = 'createDrawingsEditController';
	angular.module('MetronicApp').controller(controllerId,
			createDrawingsEditController);

	/* @ngInject */
	function createDrawingsEditController($scope, $rootScope, $timeout,
			Constant, gettextCatalog, kendoConfig, $kWindow, CommonService,
			PopupConst, Restangular, RestEndpoint , ProposalEvaluation,drawingsListService,theSignCA) {
		var vm = this;

		vm.change = {};
		vm.onChange = onChange;
		$rootScope.showEdit = false;
		vm.disableAll = false;
		
		vm.drawingsListService = drawingsListService;
		
		vm.folder = '';
		vm.item = {};
		vm.item = ProposalEvaluation.getItem();
		if(vm.item == null) {
			vm.item = CommonService.getItem();
		}
		vm.theSignCA = theSignCA;
		vm.mCompletionDrawing ={};
		vm.object = {};
		vm.edit = {};
					// //////////////////////////////////Dropdown
					// list////////////////////////////////
		vm.role=[];
		var totalListEmployee = [], MonitorId = [], DirectorId = [], CreatorId = [];
		vm.amonitorId = [];
		vm.bdirectorId = [];
		vm.creator =[];
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
		drawingsListService.getListEmployeeByConstruction(vm.item.constructId).then(function(data) {
			totalListEmployee = data.plain();
				for (var i = 0; i < totalListEmployee.length; i++) {
					if(totalListEmployee[i].roleid == Constant.ROLE_ID["employee_roleID_CDT_GSTC"]){
						MonitorId.push(totalListEmployee[i]);
					}
					if(totalListEmployee[i].roleid == Constant.ROLE_ID["employee_roleID_DT_GDNT"]){
						DirectorId.push(totalListEmployee[i]);
					}
					if(totalListEmployee[i].roleid == Constant.ROLE_ID["employee_roleID_DT_KTTC"]){
						CreatorId.push(totalListEmployee[i]);
					}
				}
				vm.amonitorId = MonitorId;
				$scope.$on("CompletionDrawingEdit", function(event, completiondrawing){
					vm.object = completiondrawing;
					vm.edit.amonitorId = vm.object.amonitorId;
					if(vm.object.statusCa==1||vm.object.statusCa==2){
						vm.disableAll = true;
					}else{
						vm.disableAll = false;
					}
					
					if(vm.object.createdUserId != Constant.user.srvUser.userId){
						vm.disableAll = true;
					}else{
						vm.disableAll = false;
					}
				});
				vm.bdirectorId = DirectorId;
				$scope.$on("CompletionDrawingEdit", function(event, completiondrawing){
					vm.object = completiondrawing;
					vm.edit.bdirectorId = vm.object.bdirectorId;
				});
				vm.creator = CreatorId;
				$scope.$on("CompletionDrawingEdit", function(event, completiondrawing){
					vm.object = completiondrawing;
					vm.edit.creatorId = vm.object.creatorId;
				});
				
		  })
		  .catch(function(data, status) {
		    console.error('Gists error', response.status, response.data);
		});
		
		// /////////////////////////////////////Handle
		// Event/////////////////////////////////////////////
		vm.goTo = goTo;
	    function goTo(menuKey) {
			
			var hasPerm = true;

			if (hasPerm) {
				var template = Constant.getTemplateUrl(menuKey);

				postal.publish({
					channel : "Tab",
					topic   : "open",
					data    : template
				});
			} else {
				// toastr.error(gettextCatalog.getString("T??i kho???n ????ng nh???p
				// hi???n t???i kh??ng ???????c ph??p truy c???p v??o ch???c n??ng n??y!"));
			}
			
		}
		function getFolder() {
			drawingsListService.getCompletionDrawingFolder().then(function(data) {
				vm.folder = data.folder;
			  })
			  .catch(function(data, status) {
			    console.error('Gists error', response.status, response.data);
			  })
			  .finally(function() {
			  });
		}
		function onChange(){
			if (vm.createDrawingEditGrid.select().length > 0){
				var tr = vm.createDrawingEditGrid.select().closest("tr");
    			var dataItem = vm.createDrawingEditGrid.dataItem(tr);
    			vm.mCompletionDrawing = dataItem;
    			vm.mCompletionDrawing.constructId = vm.item.constructId;
    			
			}
		}	
		
		initFormComplete();
		function initFormComplete(){
			$scope.$on("CompletionDrawingEdit", function(event, completiondrawing){
				vm.object = completiondrawing;
				console.log(vm.object);
				drawingsListService.openCompletionDrawingForm(vm.object).then(function(d) {
					fillDataEditTable(d.plain());
					reloadData();
				});
			});
			
		};
		function reloadData(){
			drawingsListService.openCompletionDrawingForm(vm.object).then(function(d) {
				refreshGrid(d.plain());
			});
		};
		
		function refreshGrid(d) {
			var grid = vm.createDrawingEditGrid;
			if(grid){
			grid.dataSource.data(d);
			grid.refresh();
			}
		};
		vm.editDraw = editDraw;
		function editDraw(){
			$rootScope.showEdit = true;
			$rootScope.showCreate = false;
		}
		// Ky CA
		vm.theSign = {
				code:'',
				constructId:'',
				constrCompReMapId: '',
				stringEmployee:'',
				isSign:'',
				path: '',
				nameFile: ''
			};
		vm.signCA = function() {
			console.log(vm.object);
			var grid = $("#createDrawingEditMainGrid").data("kendoGrid");
			vm.completiondrawing = grid.dataItem(grid.tbody.find(">tr:first"));
			
			vm.theSign.code = vm.completiondrawing.drawCode;
			vm.theSign.constructId = vm.completiondrawing.constructId;
			vm.theSign.constrCompReMapId = vm.completiondrawing.constrCompReMapId;
			vm.theSign.roleId = [Constant.ROLE_ID["employee_roleID_DT_KTTC"],Constant.ROLE_ID["employee_roleID_CDT_GSTC"],Constant.ROLE_ID["employee_roleID_DT_GDNT"]];
			vm.theSign.roleName = ["Ng?????i l???p","Gi??m s??t thi c??ng","?????i di???n ph??p lu???t c???a nh?? th???u thi c??ng"];
			vm.theSign.stringEmployee =vm.edit.creatorId+ ','+vm.edit.amonitorId+','+ vm.edit.bdirectorId;
			vm.theSign.isSign='theSignCA';
			
			vm.theSign.path = vm.completiondrawing.documentPath;
			vm.theSign.nameFile = vm.completiondrawing.documentName;
			
			theSignCA.setItem(vm.theSign);
			$rootScope.$broadcast("drawingList", vm.theSign);
			savebeforesign();
			goTo('THE_SIGN_CA');
		   };
		   
		$scope.$on("ChangeStatus", function(event, result){ 
			      if(result){
			    	  $rootScope.reload();
			    	  $rootScope.showEdit = true;
					$rootScope.showCreate = false;
			      }
			     });
		
		vm.savebeforesign = savebeforesign;
		function savebeforesign() {
			if(vm.object.createdUserId != Constant.user.srvUser.userId){
				return;
			}
			if('pdf' == $($('input[name="uploadfileedit"]')[0]).val().split('\\').pop().split('.').pop()){
			vm.uploadFile($(this).closest('tr').find('input[name="uploadfileedit"]').attr('id'));
			}
			var grid = $("#createDrawingEditMainGrid").data("kendoGrid");
			vm.mCompletionDrawing = grid.dataItem(grid.tbody.find(">tr:first"));
			vm.mCompletionDrawing.amonitorId = vm.edit.amonitorId;
			vm.mCompletionDrawing.bdirectorId = vm.edit.bdirectorId;
			vm.mCompletionDrawing.creatorId = vm.edit.creatorId;
			vm.mCompletionDrawing.createdDate = vm.object.createdDate;
			vm.mCompletionDrawing.createdUserId = vm.object.createdUserId;
			vm.mCompletionDrawing.statusCa = 0;
			vm.mCompletionDrawing.documentPath = drawingsListService.getData();
			vm.mCompletionDrawing.utilAttachedDocuments = [];
			var fileName = $($('input[name="uploadfileedit"]')[0]).val().split('\\').pop();
			
			if (fileName != null && typeof fileName != undefined && fileName != ""){
				if('pdf' == $($('input[name="uploadfileedit"]')[0]).val().split('\\').pop().split('.').pop()){
				vm.mCompletionDrawing.isActive = 1;
				vm.mCompletionDrawing.utilAttachedDocuments.push({documentName : $($('input[name="uploadfileedit"]')[0]).val().split('\\').pop()});
				$rootScope.reload();}else{
					toastr.warning("File import ????nh k??m ph???i l?? file PDF");
					return;
				}				
			}else{
				vm.mCompletionDrawing.isActive = 1;
				vm.mCompletionDrawing.documentName = vm.object.documentName;
				vm.mCompletionDrawing.documentPath = vm.object.documentPath;
			}
			vm.mCompletionDrawing.attachId = vm.object.attachId;
			if(vm.mCompletionDrawing.amonitorId != null && vm.mCompletionDrawing.bdirectorId != null && vm.mCompletionDrawing.creatorId != null){
			
			drawingsListService.updateCompletionDrawing(vm.mCompletionDrawing).then(
								function(){
									$rootScope.showEdit = true;
									$rootScope.showCreate = false;
									$rootScope.reload();
								}, function(errResponse) {
									if (errResponse.status == 409) {
		    		                	toastr.error(gettextCatalog.getString("M?? t???n t???i"));
		    		                } else {
		    		                	toastr.error(gettextCatalog.getString("L???i xu???t hi???n!"));
		    		                }
		                			return;
								}
						);
			
			$rootScope.reload();
			}else{
				toastr.error(gettextCatalog.getString("Ch??a ho??n th??nh th??nh ph???n k??"));
			}		
		}
		vm.save = save;
		function save() {
			if(vm.object.createdUserId != Constant.user.srvUser.userId){
				toastr.warning("B???n kh??ng c?? quy???n s???a b???n ghi n??y !");
				return;
			}
			if('pdf' == $($('input[name="uploadfileedit"]')[0]).val().split('\\').pop().split('.').pop()){
			vm.uploadFile($(this).closest('tr').find('input[name="uploadfileedit"]').attr('id'));
			}
			var grid = $("#createDrawingEditMainGrid").data("kendoGrid");
			vm.mCompletionDrawing = grid.dataItem(grid.tbody.find(">tr:first"));
			vm.mCompletionDrawing.amonitorId = vm.edit.amonitorId;
			vm.mCompletionDrawing.bdirectorId = vm.edit.bdirectorId;
			vm.mCompletionDrawing.creatorId = vm.edit.creatorId;
			vm.mCompletionDrawing.createdDate = vm.object.createdDate;
			vm.mCompletionDrawing.createdUserId = vm.object.createdUserId;
			vm.mCompletionDrawing.statusCa = 0;
			vm.mCompletionDrawing.documentPath = drawingsListService.getData();
			vm.mCompletionDrawing.utilAttachedDocuments = [];
			var fileName = $($('input[name="uploadfileedit"]')[0]).val().split('\\').pop();
			
			if (fileName != null && typeof fileName != undefined && fileName != ""){
				if('pdf' == $($('input[name="uploadfileedit"]')[0]).val().split('\\').pop().split('.').pop()){
				vm.mCompletionDrawing.isActive = 1;
				vm.mCompletionDrawing.utilAttachedDocuments.push({documentName : $($('input[name="uploadfileedit"]')[0]).val().split('\\').pop()});
				$rootScope.reload();}else{
					toastr.warning("File import ????nh k??m ph???i l?? file PDF");
					return;
				}				
			}else{
				vm.mCompletionDrawing.isActive = 1;
				vm.mCompletionDrawing.documentName = vm.object.documentName;
				vm.mCompletionDrawing.documentPath = vm.object.documentPath;
			}
			vm.mCompletionDrawing.attachId = vm.object.attachId;
			if(vm.mCompletionDrawing.amonitorId != null && vm.mCompletionDrawing.bdirectorId != null && vm.mCompletionDrawing.creatorId != null){
			
			drawingsListService.updateCompletionDrawing(vm.mCompletionDrawing).then(
								function(){
									toastr.success(gettextCatalog.getString("L??u thay ?????i th??nh c??ng!"));
									$rootScope.showEdit = true;
									$rootScope.showCreate = false;
									$rootScope.reload();
								}, function(errResponse) {
									if (errResponse.status == 409) {
		    		                	toastr.error(gettextCatalog.getString("M?? t???n t???i"));
		    		                } else {
		    		                	toastr.error(gettextCatalog.getString("L???i xu???t hi???n!"));
		    		                }
		                			return;
								}
						);
			
			$rootScope.reload();
			}else{
				toastr.error(gettextCatalog.getString("Ch??a ho??n th??nh th??nh ph???n k??"));
			}		
		}
				
		
		$scope.$on("ProposalEvaluation.detail", function (event, item) {
        	if(item != undefined){
        		vm.item = item;
        	}else{
        		console.error("kh??ng nh???n ???????c d??? li???u!");
        	}
        });
		getFolder();
		vm.uploadFile = function() {			
			var formData = new FormData();
			formData.append('tax_file', $('input[name="uploadfileedit"]')[0].files[0]); 
	        $.ajax({
	            url: Constant.BASE_SERVICE_URL+"fileservice/uploadATTT?folder="+ vm.folder,
	            type: "POST",
	            data: formData,
	            enctype: 'multipart/form-data',
	            processData: false,
	            contentType: false,
	            async: false,
	            cache: false,
	            success: function(data) {
	                drawingsListService.setData(data[0]);
	            },
	            error: function() {
	                // Handle upload error
	                // ...
	            }
	        });
	    };// function uploadFile
		// ////////////////////////////////////////////////Grid//////////////////////////////////////////

		function fillDataEditTable(data) {	
			vm.gridEditDrawingOptions = kendoConfig.getGridOptions({
			autoBind : true,
			dataSource : data,
			noRecords : true,
			navigatable: true,
			change : onChange,
			edit: function(e) {
		         e.container.find("input[name=drawCode]").attr("maxlength", 200);
		         e.container.find("input[name=drawName]").attr("maxlength", 500);
		     },
			pageable: {
	            pageSizes: true,
	            refresh: false,
	            pageSize: 20,
	            messages: {
	                display: " {0}-{1} c???a {2} k???t qu???",
	                itemsPerPage: "k???t qu???/trang"

	            }
	         },
			messages : {
				noRecords : gettextCatalog
						.getString("Kh??ng c?? k???t qu??? n??o")
			},
			columns : [ {
				title : gettextCatalog.getString("<b style='font-weight: 600;color: #000000;'>STT</b>"),
				template: dataItem => $("#createDrawingEditMainGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
				width : 50,
				editable: false,
				headerAttributes: {style:"text-align:center;"},
				attributes:{style:"text-align:center;"}
			}, {
				title : gettextCatalog.getString("<b style='font-weight: 600;color: #000000;'>M?? b???n v???</b>"),
				field: "drawCode",
				width : 150,
				headerAttributes: {style:"text-align:center;"},
			}, {
				title : gettextCatalog.getString("<b style='font-weight: 600;color: #000000;'>T??n b???n v???</b>"),
				field: "drawName",
				width : 150,
				headerAttributes: {style:"text-align:center;"},
			}, {
				title : gettextCatalog.getString("<b style='font-weight: 600;color: #000000;'>File m???m</b>"),
				field : "documentPath",
				template : function(dataItem) {
					return "<a href='"+Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + dataItem.documentPath + "'>" + dataItem.documentName + "</a>";
				},
				width : 150,
				editable: false,
				headerAttributes: {style:"text-align:center;"},
			},{
				title : gettextCatalog.getString("<b style='font-weight: 600;color: #000000;'>Attached File</b>"),
				template : function(dataItem) {
					return "<form id='upload-file-form'><input id='upload_file_" + dataItem.uid + "' type='file' name='uploadfileedit' accept='.pdf' /></form>";
				},
				width : 200,
				headerAttributes: {style:"text-align:center;"},
				attributes:{style:"text-align:right;"}
			}]
		});
	};
		
	}
})();