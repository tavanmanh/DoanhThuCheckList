/**
 * Created by pm1_os50 on 5/10/2018.
 */
MetronicApp.controller('deliveryUnitPopupController', [
    '$scope',
    'dataTree',
    'caller',
    'modalInstance1',
    'deliveryUnitOptions',
    'popupId',
    'isMultiSelect',
    'CommonService',
    'SearchService',
    'PopupConst',
    'RestEndpoint',
    '$localStorage',
    '$rootScope',
    'Constant',
    'kendoConfig',
    'gettextCatalog',
    function($scope, dataTree, caller, modalInstance1,deliveryUnitOptions, popupId, isMultiSelect,
             CommonService, SearchService, PopupConst, RestEndpoint,
             $localStorage, $rootScope,Constant,kendoConfig,gettextCatalog) {

        $rootScope.flag=false;
        $scope.searchItem={};
        $scope.modalInstance = modalInstance1;
        $scope.popupId = popupId;
        $scope.caller = caller;
        $scope.cancel = cancel;
        $scope.save = save;
        $scope.isMultiSelect = isMultiSelect;
        fillTable();
function fillTable(data) {
    var record = 0;
    $scope.deliveryUnitOptions = kendoConfig.getGridOptions({
        autoBind: true,
        scrollable: false,
        resizable: true,
        editable: false,
        dataBinding: function () {
            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
        },
        dataSource: {
            serverPaging: true,
            schema: {
                total: function (response) {
                    $("#appCount").text("" + response.total);
                    $scope.count = response.total;
                    return response.total; // total is returned in
                    // the "total" field of
                    // the response
                },
                data: function (response) {
                    var list = response.data;
                    return response.data; // data is returned in
                    // the "data" field of
                    // the response
                },
            },
            transport: {
                read: {
                    // Thuc hien viec goi service
                    url: Constant.BASE_SERVICE_URL + "contructionLandHandoverPlan/doSearchPartner",
                    contentType: "application/json; charset=utf-8",
                    type: "POST"
                },
                parameterMap: function (options, type) {
                    // vm.yearPlanSearch.employeeId =
                    // Constant.user.srvUser.catEmployeeId;
                    $scope.searchItem.page = options.page
                    $scope.searchItem.pageSize = options.pageSize

                    return JSON.stringify($scope.searchItem)

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
        columns:[{
            title: "TT",
            field: "stt",
            template: function () {
                return ++record;
            },
            width: '5%',
            columnMenu: false,
            headerAttributes: {style: "text-align:center;font-weight: bold;"},
            attributes: {
                style: "text-align:center;"
            }
        },
            {
                title: "Loại đối tác",
                field: "partnerType",
                width: "18%",
                headerAttributes: {
                    style: "text-align:center;"
                },
                attributes: {
                    style: "text-align:left;"
                },
            }, {
                title: "Mã đối tác",
                field: "code",
                width: "30%",
                headerAttributes: {
                    style: "text-align:center;"
                },
                attributes: {
                    style: "text-align:left;"
                },
            }, {
                title: "Tên dối tác",
                field: "name",
                width: "30%",
                headerAttributes: {
                    style: "text-align:center;"
                },
                attributes: {
                    style: "text-align:left;"
                },
            },{
                title: "Trạng thái",
                field: "status",
                width: "20%",
                headerAttributes: {
                    style: "text-align:center;"
                },
                attributes: {
                    style: "text-align:center;"
                },
            },{
                title: "Chọn",
                template:
                '<div class="text-center "> '	+
                '		<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate>'+
                '			<i id="#=code#" ng-click=caller.save(dataItem) class="fa fa-check color-green" aria-hidden="true"></i> '+
                '		</a>'
                +'</div>',
                width: "15%",
                field:"stt"
            }]
    });
}
    $scope.onRowChange=onRowChange;

    function onRowChange(dataItem){
        $scope.dataItem=dataItem;
    }
    function save(dataItem) {
        if(dataItem){
            caller.onSave(dataItem);
            CommonService.dismissPopup1();
        } else{
            if ($scope.dataItem) {
                caller.onSave($scope.dataItem, $scope.popupId);
                CommonService.dismissPopup1();
            } else {
                if($scope.name){
                    caller.onSave($scope.name);
                    CommonService.dismissPopup1();
                } else{
                    if(confirm('Chưa chọn bản ghi nào!')){
                        CommonService.dismissPopup1();
                    }
                }
            }
        }

    }
        function onChangeTree(dataItem){
            var obj={};
            obj.id=dataItem.id
            $scope.name=dataItem;
            CommonService.getDepartment(obj).then(function(result){
                var grid =	$scope.deliveryUnitGrid;
                grid.dataSource.data(result.plain());
                if(result.plain().length>0){
                    grid.dataSource.page(1);
                }
                grid.refresh();
            });
        }

        $scope.cancelHandover = function cancelHandover(type) {
            if(type=='text'){
                $scope.searchItem.keySearch = "";
            }
        }

    $scope.doSearchCatpartner= function(){
        trimSpace();
        $scope.searchItem.pageSize = 10;
        $scope.searchItem.page = 1;
        if($scope.keySearch){
            $scope.searchItem.id=$scope.keySearch.id;
        }
            CommonService.getCatPartner($scope.searchItem).then(function(result){
            var grid =	$scope.deliveryUnitGrid;
            grid.dataSource.data(result.data);
            grid.refresh();
        });
    }
    } ]);