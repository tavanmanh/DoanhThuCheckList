/* Modal Controller */
MetronicApp.controller('catProvinceSearchController', ['$scope', 'dataTree', 'caller', 'modalInstance1', 'gridOptions', 'popupId', 'isMultiSelect', 'CommonService', 'htmlCommonService', 'SearchService', 'PopupConst', 'RestEndpoint', '$localStorage', '$rootScope', 'Constant', 'gettextCatalog', '$timeout', 
	function ($scope, dataTree, caller, modalInstance1, gridOptions, popupId, isMultiSelect, CommonService, htmlCommonService, SearchService, PopupConst, RestEndpoint, $localStorage, $rootScope, Constant, gettextCatalog, $timeout) {

		var record = 0;
		$rootScope.flag = false;
		$scope.modalInstance = modalInstance1;
		$scope.popupId = popupId;
		$scope.caller = caller;
		$scope.cancel = cancel;
		$scope.save = save;
		$scope.isMultiSelect = isMultiSelect;
		$scope.onRowChange = onRowChange;
		$scope.filterTree = filterTree

		$(document).ready(function () {
			fillTable();
			$timeout(function (){angular.element('#provinceName').focus();}, 500);
		});

		$scope.doSearch = function () {
			var grid = $scope.gridView;
			grid.dataSource.page(1);
			grid.refresh();
		}

		function fillTable(result) {
			$scope.gridOptions = kendoConfig.getGridOptions({
				autoBind: true,
				scrollable: false,
				resizable: true,
				editable: true,
				dataBinding: function () {
					record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
				},
				reorderable: true,
				dataSource: {
					serverPaging: true,
					sort: {
						field: "name",
						dir: "asc"
					},
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
							url: Constant.BASE_SERVICE_URL + "progressPlanProjectRestService/catProvince/doSearchProvinceInPopup",
							contentType: "application/json; charset=utf-8",
							type: "POST"
						},
						parameterMap: function (options, type) {
							var obj = {};
							if (!!$scope.searchGrid)
								obj = {
									name: $scope.searchGrid.code,
								}
							obj.pageSize = options.pageSize;
							obj.page = options.page;
							return JSON.stringify(obj)
						}
					},
					pageSize: 10
				},
				noRecords: true,
				columnMenu: false,
				messages: {
					noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
				},
				pageable: {
					refresh: false,
					pageSizes: [10, 15, 20, 25],
					messages: {
						display: "{0}-{1} của {2} kết quả",
						itemsPerPage: "kết quả/trang",
						empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
					}
				},
				columns: [{
					title: "TT",
					field: "#",
					width: '6%',
					template: function () {
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
					title: "Tên tỉnh",
					field: "name",
					headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
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
					title: "Chọn",
					template:
						'<div class="text-center "> ' +
						'		<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate>' +
						'			<i id="#=code#" ng-click=save(dataItem) class="fa fa-check color-green" aria-hidden="true"></i> ' +
						'		</a>'
						+ '</div>',
					width: "15%",
					field: "stt"
				}
				]
			});
		}

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

		function onRowChange(dataItem) {
			$scope.dataItem = dataItem;
		}

		function cancel() {
			htmlCommonService.dismissPopup();
		}

		function save(dataItem) {
			if (dataItem) {
				if($scope.popupId=='searchForm'){
					caller.onSaveCatProvinceSearch(dataItem);
					htmlCommonService.dismissPopup();
				} else {
					caller.onSaveCatProvince(dataItem);
					htmlCommonService.dismissPopup();
				}
			}
		}

		function filterTree(keyEvent) {
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

	}]);
