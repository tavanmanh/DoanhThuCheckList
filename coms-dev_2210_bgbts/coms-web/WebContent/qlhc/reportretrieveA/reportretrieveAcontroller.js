(function() {
	'use strict';

	var controllerId = 'listController';

	angular.module('MetronicApp').controller(controllerId, listController);

	/* @ngInject */
	function listController($scope, $rootScope, $timeout, Constant,
			kendoConfig, gettextCatalog, $kWindow, $q, list_report_A_services,ProposalEvaluation) {
		var vm = this;
		vm.removereport = removereport;
		vm.changeSite = changeSite;
		vm.goToAdd = goToAdd;
		vm.showGrid = showGrid;
		vm.save = save;
		vm.goToUpdate = goToUpdate;
		vm.showDetail = false;
		vm.item = {
				partnerName:'',
				contractCode:'',
				investProjectName:'',
				constrtCode:'',
				constrtName:'',
				constrType:'',
				provinceId:'',
				constrtAddress:'',
				constructId:'',
				contractName:''
			}
		vm.item = ProposalEvaluation.getItem();
		vm.objectDTO =[];
		vm.monitoring=[];
		vm.technical=[];
		vm.monitoringA=[];
		vm.technicalA=[];
		vm.resultMonitoring = {
				constructId: '',
		};
		vm.resultMonitoringA = {
				constructId: '',
				
		};
		vm.resultMonitoring.constructId= vm.item.constructId;
		vm.resultMonitoringA.constructId= vm.item.constructId;
		vm.itemID = {
				amaterialRecoveryMinutesId :'',
				adirectorId :'',
				ahandoverPersonId :'',
				bdirectorId: '',
				breceivePersonId: '',
				isActive:''	,
				statusCa: '',
				code : '',
				constructId:''
		}
		vm.template = {
				amaterialRecoveryMinutesId :'',
				adirectorId :'',
				ahandoverPersonId :'',
				bdirectorId: '',
				breceivePersonId: '',
				isActive:''	,
				statusCa: '',
				code : '',
				constructId:'',
				signPlace: ''
		}
		loadDataTable();
		
		initdata();
		function initdata(){
			list_report_A_services.getListEmployeeByRole(vm.resultMonitoring ).then(function(data) {
				vm.monitoring = data.plain();
			});

			list_report_A_services.getListEmployeeByRole(vm.resultMonitoringA).then(function(data) {
				vm.monitoringA = data.plain();
			});		
			
			list_report_A_services.getAllListEmployeeByRole(vm.item.constructId).then(function(data) {
				vm.technical = data.plain();
			});		
			
			list_report_A_services.getAllListEmployeeByRole(vm.item.constructId).then(function(data) {
				vm.technicalA = data.plain();
			});	
			
			}
		
		function fillDataTable(data) {
			var deferred = $q.defer();
			vm.gridOptions = kendoConfig
					.getGridOptions({
						autoBind : true,
						dataSource : data,
						change : onChange,
						noRecords : true,
						messages : {
							noRecords : gettextCatalog
									.getString("Kh??ng c?? k???t qu??? n??o")
						},
						columns : [
								{
						            field: "STT",
						            title: "STT",
						            template: dataItem => $("#reportGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
						            width : 180
								},
								{
									title : gettextCatalog.getString("Ch???n"),
									template : "<input type='checkbox' name='gridcheckbox' />",
									width : 200
								},
								{
									title : gettextCatalog.getString("M?? bi??n b???n"),
									field : "code",
									width : 200
								},
								{
									title : gettextCatalog
											.getString("M?? c??ng tr??nh"),
									field : "constrtCode",
									width : 200
								},
								{
									title : gettextCatalog
											.getString("M?? h???p ?????ng"),
									field : "contractCode",
									width : 200
								},
								{
									title : gettextCatalog
											.getString("T??n h???p ?????ng"),
									field : "contractName",
									width : 200
								},
								{
									title : gettextCatalog
											.getString("Tr???ng th??i"),
									field : "statusCa",
									 attributes: { style: "text-align:left;" , class:"statusColumn"},
									template: function($scope){
									      if($scope.statusCa == 0)
									      {
									       return "So???n Th???o";
									      }
									      if($scope.statusCa == 1)
									      {
									    	  return "Ch??? K??";
									      }
									      if($scope.statusCa == 2)
									      {
									    	  return "???? K??";
									      }
									      if($scope.statusCa == 3)
									      {
									    	  return "T??? ch???i K??";
									      }
									      },
									width : 200
								},
								{
									title : gettextCatalog.getString("K?? CA"),
									template : '<button class="btn btn-primary" style="margin-left: 35%;"><span class="glyphicon glyphicon-play"></span></button>',
									width : 200
								} ]
					});
			deferred.resolve('done');
			return deferred.promise;
		}
// ///////////////LoadData////////////////////////////////////
		
		function loadDataTable() {
				list_report_A_services.findByConstructId(vm.item.constructId).then(function(d) {
					fillDataTable(d.plain());
					refreshGrid(d.plain());
				}, function(e) {
					toastr.success(gettextCatalog.getString("Co loi xay ra trong qua trinh xay ra qua trinh load du lieu!"));
				});
		}
		function refreshGrid(d) {
			var grid = vm.reportGrid;
			if(grid){
				grid.dataSource.data(d);
				grid.refresh();
			}
		}
		
		function showGrid(){
			if(vm.showDetail == false){
				vm.showDetail = true;
			}else{
				vm.showDetail = false;
			}
		}
		
		
		// ///////////// th??ng tin chung////////////////
		$scope.$on("ProposalEvaluation.detail", function(event, item) {
			if (item != undefined) {
				vm.item = item;
				loadDataTable();
			} else {
				console.error("kh??ng nh???n ???????c d??? li???u!");
				toastr.warning(gettextCatalog.getString("B???n c???n ch???n m???t b???n ghi tr?????c"));
			}
		});
		

		function changeSite() {
			list_report_A_services.goTo();
		}
		
		// //////Remove///////////
		function onChange() {
			if (vm.reportGrid.select().length > 0) {
                var tr = vm.reportGrid.select().closest("tr");
                var dataItem = vm.reportGrid.dataItem(tr);
                vm.itemID = dataItem;
            }
		}
		
	    function getDataRowSelect(){
	        var demopromise = $q.defer();
	        var grid = vm.reportGrid;
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
	    
	    
	    
	    
	    
	    vm.multiDelete = function() {
	    	   var selectedRow = [];
	    	   var grid = vm.reportGrid;
	    	   grid.table.find("tr").each(function(idx, item) {
	    	    var row = $(item);
	    	    var checkbox = $('[name="gridcheckbox"]', row);

	    	    if (checkbox.is(':checked')) {
	    	     // Push id into selectedRow
	    	     var tr = grid.select().closest("tr");
	    	     var dataItem = grid.dataItem(item);
	    	     console.log('dataItem ----');
	    	     selectedRow.push(dataItem.aMaterialRecoveryMinutesId);
	    	    }
	    	   });

	    	   if (selectedRow.length == 0) {
	    	    toastr.warning(message.recordRequired);
	    	    return;
	    	   }

	    	   console.log(selectedRow);
	    	   if (confirm('X??c nh???n x??a')) { 
	    		   list_report_A_services.deleteAMaterialMinutes(selectedRow).then(function() {
	    			     toastr.success("X??a b???n ghi th??nh c??ng");
	    			     loadDataTable();
	    			    }, function(errResponse) {
	    			            if (errResponse.status == 302){
	    			                toastr.error("B???n ghi ??ang ???????c s??? d???ng");
	    			               }else{
	    			                toastr.error(message.deleteError);
	    			            }
	    			           });
	    			   }
	    	   }
		
		function removereport() {
			getDataRowSelect().then(function(dataItem){
                if(vm.reportGrid.select().length > 0 && confirm("Xac Nhan Xoa")){
                	list_report_A_services.removeMaterial(dataItem.aMaterialRecoveryMinutesId).then(function(){
                		loadDataTable();
                        toastr.success("Xoa thanh cong");
                        refreshDataTable();
                    }, function(errResponse){
                        toastr.success(gettextCatalog.getString("C?? l???i x???y ra trong qu?? tr??nh x??a!"));
                    });
                }
            });
		}
		// Chuyen trang/////////////////
		

		/* Handle action client on a menu item */
		function goToAdd() {
			
		vm.showDetail = true;
		vm.itemID.constructId = vm.item.constructId;
		vm.itemID.amaterialRecoveryMinutesId = null;
		
		vm.item.adirectorId = vm.monitoring[0].id;
		vm.item.bdirectorId = vm.technical[0].id;
		
		vm.item.ahandoverPersonId = vm.monitoringA[0].id;
		vm.item.breceivePersonId = vm.technicalA[0].id;
		
		loadDataTableA();
		}
		
		

		/* Handle action client on a menu item */
		function goToUpdate(menuKey) {
			
		
		}
		/////////////////////////////////////////////////////////////////////
//		loadDataTableA();

		function fillDataTableA(data) {
			var deferred = $q.defer();
		     if (data && data.length > 0 && !data[0].qualityStatus) {  // n???u data kh??ng c?? tr?????ng c??ntruction
	                for (var i = 0; i < data.length; i++) {
	                    if (data[i].qualityStatus === 0) {
	                        data[i].qualityStatus = { id: "0", value: "Tot" }
	                    } else {
	                        data[i].qualityStatus = { id: "1", value: "Hong" }
	                    }
	                }
	            }
	            var dataSource = new kendo.data.DataSource({
	                pageSize: 20,
	                data: data,
	                autoSync: false,
	                schema: {
	                    model: {
	                        fields: {
	                        	qualityStatus: { defaultValue: { id: 1, value: "Tot" } },
//	                            quantity: { type: "number", validation: { min: 0, required: true } },
//	                            unit: { validation: { required: true } },
//	                            currentState: { validation: { required: true, message: "N???i dung kh??ng ???????c ????? tr???ng" } },
//	                            handoverContent: { validation: { required: true } },
	                        }
	                    }
	                }
	            });
			vm.gridOptionsA = kendoConfig
					.getGridOptions({
						autoBind : true,
						 dataSource : data,
						 change : onChangeA,
						noRecords : true,
						messages : {
							noRecords : gettextCatalog
									.getString("Kh??ng c?? k???t qu??? n??o")
						},
						columns : [
								{
									field: "STT",
									title: "TT",
									template: dataItem => $("#reportGridA").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
									width : 180
								},
								{
									title : "<input type='checkbox' name='gridchkselectall' ng-click='vm.chkSelectAll();' ng-model='vm.chkAll' />",
									template : "<input type='checkbox' name='gridcheckbox' />",
									width : 35
								},
								{
									title : gettextCatalog.getString("T??n VT TB"),
									field : "merName",
									width : 200
								},
								{
									title : gettextCatalog.getString("Serial number"),
									field : "serialNumber",
									width : 200
								},
								{
									title : gettextCatalog.getString("????n v??? t??nh"),
									field : "name",
									width : 200
								},
								{
									title : gettextCatalog.getString("S??? l?????ng b??n giao"),
									field : "actualReceiveQuantity",
									width : 200
								},
								{
									title : gettextCatalog.getString("S??? l?????ng nghi???m thu"),
									field : "acceptanceNumber",
									width : 200
								}
								,
								{
									title : gettextCatalog.getString("S??? l?????ng thu h???i"),
									field : "enough",
									width : 200
								},
								   {
			                        title: gettextCatalog.getString("Y??u c???u thi c??ng"),
			                        field: "qualityStatus",
			                        attributes: { style: "padding:0" },
			                        editor: categoryDropDownEditor,
			                        template: "#=qualityStatus.value#",
			                        width: 120
			                    },
								{
									title : gettextCatalog.getString("Ghi ch??"),
									field : "comments",
									width : 200
								} 
			                    ]
					});
			deferred.resolve('done');
			return deferred.promise;
		}
		
		function categoryDropDownEditor(container, options) {
            $('<input required name="' + options.field + '"/>')
                .appendTo(container)
                .kendoDropDownList({
                    autoBind: false,
                    dataTextField: "value",
                    dataValueField: "id",
                    dataSource: [{ id: 0, value: "Hong" }, { id: 1, value: " Tot" }]
                });
        }
		
// /////////////////////////
		function loadDataTableA() {
			if(vm.itemID.amaterialRecoveryMinutesId == null){
				list_report_A_services.getTwoList(vm.itemID.constructId).then(function(d) {
					fillDataTableA(d.plain());
					refreshGridA(d.plain());
					}, function(e) {
					toastr.error(gettextCatalog.getString("Co loi xay ra trong qua trinh xay ra qua trinh load du lieu!"));
					});
			console.log("BBBBBB"+vm.itemID.constructId);
			}else{
				
			}
			
		}
		
		function refreshGridA(d) {
			var grid = vm.reportGridA;
			if(grid){
				grid.dataSource.data(d);
				grid.refresh();
			}
		}
		
		function onChangeA(){
			if (vm.reportGridA.select().length > 0) {
                var tr = vm.reportGridA.select().closest("tr");
                var dataItem = vm.reportGridA.dataItem(tr);
                vm.itemID = dataItem;
            }
		}
		
		function save(){
			if(vm.itemID.aMaterialRecoveryMinutesId == null){
				var data = vm.reportGridA.dataSource.data();
				var objectDTO = [];
			}
			var data = vm.reportGridA.dataSource.data();
			var objectDTO = [];
			console.log("du lieu bang nho" + JSON.stringify(data));
		}
		
		
	}
})();