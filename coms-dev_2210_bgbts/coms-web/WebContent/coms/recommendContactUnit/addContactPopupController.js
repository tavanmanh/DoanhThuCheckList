/* Modal Controller */
MetronicApp.controller('addContactPopupController', [
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
//                                                 'popupSearchService',
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
                                                						 url: Constant.BASE_SERVICE_URL + RestEndpoint.RECOMMEND_CONTACT_UNIT_SERVICE+"/getForAutoCompleteProvince",
                                                						 contentType: "application/json; charset=utf-8",
                                                						 type: "POST"
                                                					 },					
                                                					 parameterMap: function (options, type) {
                                                						 var obj = {};
                                                						 if(!! $scope.searchGrid)
                                                							 obj = {
                                                								 keySearch : $scope.searchGrid.code,
                                                						 }

                                                						 obj.pageSize=options.pageSize;
                                                						 obj.page=options.page;
                                                						 return JSON.stringify(obj)
                                                					 }
                                                				 },					 
                                                				 pageSize: 10
                                                			 },
                                                			 noRecords: true,
                                                			 columnMenu:false,
                                                			 scrollable:false,
                                                			 messages: {
                                                				 noRecords : "Không có kết quả hiển thị"
                                                			 },
                                                			 dataBinding: function() {
                                                				 record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                                                			 },
                                                			 pageSize:10,
                                                			 pageable: {
                                                				 refresh: false,
                                                				 pageSize:10,
                                                				 pageSizes: [10, 15, 20, 25],
                                                				 messages: {
                                                					 display: "{0}-{1} của {2} kết quả",
                                                					 itemsPerPage: "kết quả/trang",
                                                					 empty: "Không có kết quả hiển thị"
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
                                                			 }, {
                                                				 title: "Mã tỉnh",
                                                				 field: "code",
                                                				 headerAttributes: {
                                                					 style: "text-align:center;"
                                                				 },
                                                				 attributes: {
                                                					 style: "text-align:left;"
                                                				 },
                                                			 }, {
                                                				 title: "Tên Tỉnh",
                                                				 field: "name",
                                                				 headerAttributes: {
                                                					 style: "text-align:center;"
                                                				 },
                                                				 attributes: {
                                                					 style: "text-align:left;"
                                                				 },
                                                			 },{
                                                				 title: "Khu vực",
                                                				 field: "areaCode",
                                                				 headerAttributes: {
                                                					 style: "text-align:center;"
                                                				 },
                                                				 attributes: {
                                                					 style: "text-align:left;"
                                                				 },
                                                			 },
                                                			 {
                                                				 title: "Chọn",
                                                				 template: 
                                                					 '<div class="text-center "> '	+
                                                					 '		<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate>'+
                                                					 '			<i id="#=code#" ng-click=save(dataItem) class="fa fa-check color-green" aria-hidden="true"></i> '+
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
                                                		 caller.onSaveContractFrame(dataItem);
                                                		 htmlCommonService.dismissPopup();
                                                	 }

                                                	 $scope.filterTree=filterTree
//                                              	 $("#filterText").keyup(function (e) {
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
                                                		 grid.dataSource.read();
                                                		 grid.dataSource.page(1);
                                                		 grid.refresh();
                                                	 }



                                                 } ]);