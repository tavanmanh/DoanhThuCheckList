/* Modal Controller */
MetronicApp.controller('contractSearchController', [
		'$scope',
		'dataTree',
		'caller',
		'modalInstance1',
		'gridOptions',
		'popupId',
		'isMultiSelect',
		'CommonService',
		'htmlCommonService',
		'SearchService',
		'PopupConst',
		'RestEndpoint',
		'$localStorage',
		'$rootScope',
		'Constant',
		function($scope, dataTree, caller, modalInstance1,gridOptions, popupId, isMultiSelect,
				CommonService,htmlCommonService, SearchService, PopupConst, RestEndpoint,
				$localStorage, $rootScope, Constant) {
          
			$rootScope.flag=false;
			
			$scope.modalInstance = modalInstance1;
			$scope.popupId = popupId;
			$scope.caller = caller;
			$scope.cancel = cancel;
			$scope.save = save;
			$scope.isMultiSelect = isMultiSelect;
			var record = 0;
			function fillTable(result){
			$scope.gridOptions =  kendoConfig.getGridOptions({
					autoBind: true,
					resizable: true,
					dataBinding: function() {
	                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
	                },
					dataSource: {
						sort: {
				            field: "name",
				            dir: "asc"
				        },
				        serverPaging: true,
				        schema: {
							 total: function (response) {
									return response.total; 
								},
								data: function (response) {				
								return response.data; 
								},
			                },
						transport: {
							read: {
			                        // Thuc hien viec goi service
//								if($scope.isInternalSource == true){
//									url: Constant.BASE_SERVICE_URL + "tangentCustomerRestService/getAllContractXdddSuccessInternal",
//								}else 
								url: Constant.BASE_SERVICE_URL + "V2/tangentCustomerRestService/getAllContractXdddSuccess",
								
								contentType: "application/json; charset=utf-8",
								type: "POST"
							},					
							parameterMap: function (options, type) {
								var obj = {};
								if(!! $scope.searchGrid)
									 obj.contractCode = $scope.searchGrid.code;
								 	 obj.source = caller.viewForm.source;
									 obj.contractType = 0;		
									 obj.pageSize=options.pageSize;
									 obj.createdDate = $scope.caller.viewForm.createdDate;
									 obj.page=options.page
								return JSON.stringify(obj)
							}
						},					 
						pageSize: 10
					},
					noRecords: true,
					columnMenu:false,
					scrollable:false,
					messages: {
						noRecords : "Kh??ng c?? k???t qu??? hi???n th???"
					},
					pageSize:10,
					pageable: {
						refresh: false,
						pageSize:10,
						 pageSizes: [10, 15, 20, 25],
						messages: {
			                display: "{0}-{1} c???a {2} k???t qu???",
			                itemsPerPage: "k???t qu???/trang",
			                empty: "Kh??ng c?? k???t qu??? hi???n th???"
			            }
					},
					dataBound: function (e) {
					    	     var grid = $("#gridView").data("kendoGrid");
					    	    grid.tbody.find("tr").dblclick(function (e) {
					    	        var dataItem = grid.dataItem(this);
					    	        $('#'+dataItem.code).click();
					    	    });
					    	},
					columns:[{
						title: "TT",
						field: "#",
						width:'6%',
						template: function() {
							return ++record;
						},
						 headerAttributes: {
								style: "text-align:center;"
							},
							attributes: {
								style: "text-align:center;"
							},
					}, 
					         {
						title: "S??? hi???u h???p ?????ng",
						field: "contractCode",
						headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:left;"
						},
					},{
						title: "Ng??y k??",
						field: "signDate",
						headerAttributes: {
							style: "text-align:center;"
						},
						attributes: {
							style: "text-align:center;"
						},
					},{
						title: "Ch???n",
						 template: 
					        	'<div class="text-center "> '	+
				        	'		<a  type="button" class=" icon_table" uib-tooltip="Ch???n" translate>'+
				        	'			<i id="#=contractCode#" ng-click=save(dataItem) class="fa fa-check color-green" aria-hidden="true"></i> '+
				        	'		</a>'
								+'</div>',  
				        width: "15%",
				        field:"stt"
					}
					]
				});

				
				}

		$( document ).ready(function() {
					fillTable();
			});
			
			function genData(items, parent) {
	            var itemArr = [];
	            for (var i = 0; i < items.length; i++) {
	                if (items[i].parentId === parent) {
	                	var row = items[i];
	                	row.id = items[i].id;
	                	row.text = items[i].text;
	                    row.children = genData(items, items[i].id);
	                    itemArr.push(row);
	                }
	            }
	            return itemArr;
	        }
			
			$scope.onRowChange=onRowChange;
			
			function onRowChange(dataItem){
				$scope.dataItem=dataItem;
			}
			
			function cancel() {
				htmlCommonService.dismissPopup();
			}

			function save(dataItem) {
				if(dataItem){
					caller.onSaveContract(dataItem);
					htmlCommonService.dismissPopup();
				}
			}

			$scope.filterTree=filterTree
//			 $("#filterText").keyup(function (e) {
			function filterTree(keyEvent){
				filter($scope.treeView.dataSource, keyEvent.target.value.toLowerCase());
			}
			
			function filter(dataSource, query) {
                var hasVisibleChildren = false;
                var data = dataSource instanceof kendo.data.HierarchicalDataSource && dataSource.data();

                for (var i = 0; i < data.length; i++) {
                    var item = data[i];
                    var text = item.text.toLowerCase();
                    var itemVisible =
                        query === true // parent already matches
                        || query === "" // query is empty
                        || text.indexOf(query) >= 0; // item text matches query

                    var anyVisibleChildren = filter(item.children, itemVisible || query); // pass true if parent matches

                    hasVisibleChildren = hasVisibleChildren || anyVisibleChildren || itemVisible;

                    item.hidden = !itemVisible && !anyVisibleChildren;
                }

                if (data) {
                    // re-apply filter on children
                    dataSource.filter({ field: "hidden", operator: "neq", value: true });
                }

                return hasVisibleChildren;
            }

			$scope.doSearch= function(){
				var grid =	$scope.gridView;
//				grid.dataSource.read();
				grid.dataSource.page(1);
				grid.refresh();
			}
			
			
			
		} ]);
