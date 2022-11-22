/**
 * Created by pm1_os36 on 2/26/2018.
 */
function completeFunction($scope, $rootScope, $timeout, gettextCatalog,$filter,
    kendoConfig, $kWindow,constructionService,
    CommonService, PopupConst, Restangular, RestEndpoint,Constant,vm){
    
    //vm.completeSearch={};
    vm.completeSearch={};
    vm.initComplete = initComplete;
    function initComplete() {
        vm.completeGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            reorderable: true,
            toolbar: [
                {
                    name: "actions",
                    template:
                    '<div class="btn-group pull-right margin_top_button margin10">' +
                    '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                    '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportcompleteFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                    '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                    '<label ng-repeat="column in vm.completeGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                    '<input type="checkbox" checked="column.hidden" ng-click="vm.showHidecompleteColumn(column)"> {{column.title}}' +
                    '</label>' +
                    '</div></div>'
                }
            ],
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                        vm.CompleteSearchCount = response.total;
                        return response.total; // total is returned in
                        // the "total" field of
                        // the response
                    },
                    data: function (response) {
                        return response.data; // data is returned in
                        // the "data" field of
                        // the response
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "workItemService/doSearchComplete",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        // vm.constructionSearch.employeeId =
                        // Constant.user.srvUser.catEmployeeId;
                        vm.completeSearch.page = options.page
                        vm.completeSearch.pageSize = options.pageSize
                        vm.completeSearch.constructionId=vm.constrObj.constructionId
                        return JSON.stringify(vm.completeSearch)

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
            columns: [
                {
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
                    title: "Mã hạng mục",
                    field: 'code',
                    width: '20%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Loại hạng mục",
                    field: 'catWorkItemTypeName',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Đơn vị thực hiện",
                    field: 'constructorName',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Đợn vị giám sát",
                    field: 'supervisorName',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Trạng thái",
                    field: 'status',
                    width: '10%',
                    template: function (dataItem) {
                        if (dataItem.status == 1) {
                            return "<span name='status' font-weight: bold;'>Chưa thực hiện</span>"
                        } else if (dataItem.status == 2) {
                            return "<span name='status' font-weight: bold;'>Đang thực hiện</span>"
                        } else if (dataItem.status == 3) {
                            return "<span name='status' font-weight: bold;'>Đã hoàn thành</span>"
                        }
                    },
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Ngày hoàn thành",
                    field: 'completeDate',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        if (dataItem.status == 1) {
                            return "<span name='status' font-weight: bold;'></span>"
                        } else if (dataItem.status == 2) {
                            return "<span name='status' font-weight: bold;'></span>"
                        } else if (dataItem.status == 3) {
                            return "<span name='status' font-weight: bold;'>"+kendo.toString(new Date(dataItem.completeDate), "dd/MM/yyyy")+"</span>"
                        }
                    }
                    //template: function (dataItem) {
                    //    return kendo.toString(new Date(dataItem.startDate), "dd/MM/yyyy");
                    //}

                }

		]
		});
    };
 //an cot hoan thanh
        vm.showHidecompleteColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.completeGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.completeGrid.showColumn(column);
            } else {
                vm.completeGrid.hideColumn(column);
            }


        }

        
    //xuat excel
    vm.listCompleteRemove=[{

    }]
    vm.listCompleteConvert=[{
        field:"status",
        data:{
            1:'Chưa thực hiện',
            2:'Đang thực hiện',
            3:'Đã hoàn thành'
        }
    }]

        ///xuat excell
    vm.exportcompleteFile= function(){
    	function displayLoading(target) {
  	      var element = $(target);
  	      kendo.ui.progress(element, true);
  	      setTimeout(function(){
  	    	  
  	    	return Restangular.all("workItemService/exportCompleteProgress").post(vm.completeSearch).then(function (d) {
  	            var data = d.plain();
  	            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
  	            kendo.ui.progress(element, false);
  	        }).catch(function (e) {
  	        	kendo.ui.progress(element, false);
  	            toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
  	            return;
  	        });
  		});
  			
  	  }
  		displayLoading(".tab-content");
        

    }


    vm.doSearchComplete={};
         vm.doSearchComplete=doSearchComplete;
        function doSearchComplete(){
            var grid =vm.completeGrid;
            if(grid){
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        }



     
}