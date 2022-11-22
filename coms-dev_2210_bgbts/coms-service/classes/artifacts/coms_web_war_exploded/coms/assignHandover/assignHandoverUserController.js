/* Modal Controller */
MetronicApp.controller('assignHandoverUserController', [
    '$scope',
    // 'dataTree',
    'data',
    'caller',
    'modalInstance',
    // 'gridOptions',
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
    'gettextCatalog',
    function ($scope, data, caller, modalInstance, /*gridOptions,*/ popupId, isMultiSelect,
              CommonService, htmlCommonService, SearchService, PopupConst, RestEndpoint,
              $localStorage, $rootScope, Constant, gettextCatalog) {

        $rootScope.flag = false;

        $scope.modalInstance = modalInstance;
        $scope.popupId = popupId;
        $scope.caller = caller;
        $scope.cancel = cancel;
        $scope.save = save;
        $scope.isMultiSelect = isMultiSelect;
        var record = 0;

        function fillTable(result) {
            $scope.gridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "constructionService/doSearchPerformer",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var obj = {
                                page: options.page,
                                pageSize: options.pageSize,
                                sysGroupId: caller.sysGroupId
                            };

                            if (!!$scope.searchGrid) {
                                obj.keySearch = $scope.searchGrid.code;
                            }
                            return JSON.stringify(obj);
                        }
                    },
                    pageSize: 10
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                pageable: {
                    refresh: false,
                    pageSize: 10,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
                columns: [
                    {
                        title: "TT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '10%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }

                    },
                    {
                        title: "Tên đăng nhập",
                        field: 'loginName',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }

                    },
                    {
                        title: "Mã nhân viên",
                        field: 'employeeCode',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }

                    },
                    {
                        title: "Họ tên",
                        field: 'fullName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Email",
                        field: 'email',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        }
                    },
                    {
                        title: "Số điện thoại",
                        field: 'phoneNumber',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        }
                    },
                    {
                        title: "Chọn",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        template: function (dataItem) {
                            return '<div class="text-center">' +
                                '<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate >' +
                                '<i id="#=code#"  ng-click="save(dataItem)"  class="fa fa-check color-green" aria-hidden="true"></i>' +
                                '</a>'
                                + '</div>';
                        },
                        width: '10%'
                    }
                ]
            });

        }

        $(document).ready(function () {
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

        $scope.onRowChange = onRowChange;

        function onRowChange(dataItem) {
            $scope.dataItem = dataItem;
        }

        function cancel() {
            caller.listChecked = [];
            caller.listConstructionCodeChecked = [];
            // CommonService.dismissPopup();
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

        function save(dataItem) {
            if (dataItem) {
                var message = 'Bạn giao việc nhận bàn giao mặt bằng công trình: <br>' +
                    '<font color="red"><b>"' + data.join('", "') + '"</b></font>' +
                    '<br><br>' +
                    'cho nhân viên: ' +
                    '<font color="red"><b>"' + dataItem.fullName + '"</b></font>' +
                    '<br><br>' +
                    'Nhấn <b>"Xác nhận"</b> để thực hiện';


//                caller.chooseUserPopup.dismiss();
                confirm(message,
                    function () {
                        caller.doAssignHandoverUser(dataItem);
                    },
                    null,
                    null,
                    '500',
                    '180');
            }

        }

        $scope.filterTree = filterTree;

//			 $("#filterText").keyup(function (e) {
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
                dataSource.filter({field: "hidden", operator: "neq", value: true});
            }

            return hasVisibleChildren;
        }

        $scope.doSearch = function () {
            var grid = $scope.gridView;
            grid.dataSource.read();
            grid.dataSource.page(1);
            grid.refresh();
        }


    }]);