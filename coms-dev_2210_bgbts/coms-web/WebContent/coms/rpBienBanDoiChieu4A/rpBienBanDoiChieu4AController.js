(function () {
    'use strict';
    var controllerId = 'rpBienBanDoiChieu4AController';

    angular.module('MetronicApp').controller(controllerId, rpBienBanDoiChieu4AController);

    function rpBienBanDoiChieu4AController($scope, $rootScope, $timeout, gettextCatalog,
                                kendoConfig, $kWindow, rpBienBanDoiChieu4AService,
                                CommonService, PopupConst, Restangular, RestEndpoint, Constant, htmlCommonService) {
        var vm = this;
        vm.compareSearch = {};
        vm.String = "Quản lý công trình > Báo cáo > Biên bản đối chiếu vât tư xuất thi công (4A)";
        //VietNT_20190301_start
        vm.sumHolder = {};
        vm.lastSearch = {};

        var today;
        var dateToMax;

        initSumValue();
        init();
        function init(){
            // date field
            today = new Date();
            dateToMax = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 1);
            var from = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
            vm.compareSearch.dateTo = htmlCommonService.formatDate(dateToMax);
            vm.compareSearch.dateFrom = htmlCommonService.formatDate(from);

        	fillDataTable([]);
            doSearchWithSum();
            vm.lastSearch = angular.copy(vm.compareSearch);
        }

        function initSumValue() {
            vm.sumHolder.numberXuatKho = 0;
            vm.sumHolder.totalMoneyXuatKho = 0;
            vm.sumHolder.numberThucTeThiCong = 0;
            vm.sumHolder.totalMoneyThucTeThiCong = 0;
            vm.sumHolder.numberThuHoi = 0;
            vm.sumHolder.totalMoneyThuHoi = 0;
            vm.sumHolder.numberChuaThuHoi = 0;
            vm.sumHolder.totalMoneyChuaThuHoi = 0;
        }

        function isSameSearch() {
            return (vm.compareSearch.name === vm.lastSearch.name);
        }

        vm.doSearchNoSum = function() {
            var grid = vm.compareGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
                grid.dataSource.read();
                grid.refresh();
            }
        };

        function doSearchWithSum() {
            console.log("dosearch with sum");
            // reinit sum value
            initSumValue();

            // make copy of search obj
            var obj = angular.copy(vm.compareSearch);
            // set no page size to get all data
            obj.page = null;
            obj.pageSize = null;

            Restangular.all("synStockDailyIeService/doSearchCompareReport").post(obj).then(function (d) {
                var data = d.data;
                $.each(data, function (index, record) {
                    vm.sumHolder.numberXuatKho += record.numberXuatKho;
                    vm.sumHolder.totalMoneyXuatKho += record.totalMoneyXuatKho;
                    vm.sumHolder.numberThucTeThiCong += record.numberThucTeThiCong;
                    vm.sumHolder.totalMoneyThucTeThiCong += record.totalMoneyThucTeThiCong;
                    vm.sumHolder.numberThuHoi += record.numberThuHoi;
                    vm.sumHolder.totalMoneyThuHoi += record.totalMoneyThuHoi;
                    vm.sumHolder.numberChuaThuHoi += record.numberChuaThuHoi;
                    vm.sumHolder.totalMoneyChuaThuHoi += record.totalMoneyChuaThuHoi;
                });

                vm.doSearchNoSum();
            });
        }

        vm.doSearch = doSearch;
        function doSearch() {
            if (!isSameSearch()) {
                // save to compare next time
                vm.lastSearch = angular.copy(vm.compareSearch);
                doSearchWithSum();
            } else {
                vm.doSearchNoSum();
            }
        }
        //VietNT_end

        vm.deprtOptions1 = {
        	    dataTextField: "name",
        	    dataValueField: "id",
        		placeholder:"Nhập mã hoặc tên công trình",
        	    select: function (e) {
        	    	vm.isSelect = true;
        	        var dataItem = this.dataItem(e.item.index());
        	        vm.compareSearch.name = dataItem.name;
        	        vm.compareSearch.constructionCode = dataItem.code;
        	        vm.compareSearch.constructionId = dataItem.id;
        	    },
        	    pageSize: 10,
        	    open: function (e) {
                    vm.isSelect = false;
                },
        	    dataSource: {
        	        serverFiltering: true,
        	        transport: {
        	            read: function (options) {
        	            	vm.isSelect = false;
        	                return Restangular.all("synStockDailyIeService/" + 'getForCompleteConstruction').post({
        	                    name: vm.compareSearch.name,
        	                    pageSize: vm.deprtOptions1.pageSize,
        	                    page: 1
        	                }).then(function (response) {
        	                    options.success(response);
        	                }).catch(function (err) {
        	                    console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
        	                });
        	            }
        	        }
        	    },
        	    template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
        	    change: function (e) {
        	        if (e.sender.value() === '') {
        	            vm.compareSearch.name = null;// thành name
        	            vm.compareSearch.constructionId = null;
        	            vm.compareSearch.constructionCode = null;
        	        }
        	    },
        	    close: function (e) {
                    if (!vm.isSelect) {
                    	vm.compareSearch.constructionId = null;
                    	vm.compareSearch.name = null;
                    	vm.compareSearch.constructionCode = null;
                    }
                },
        	    ignoreCase: false
        	}
        
        vm.openDepartmentTo = openDepartmentTo

        function openDepartmentTo(popUp) {
            vm.obj = {};
            vm.departmentpopUp = popUp;
            var templateUrl = 'coms/popup/comsConstructionSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','comsConstructionSearchController');
        }
        
        vm.onSaveComsConstruction = onSaveComsConstruction;
        function onSaveComsConstruction(data) {
             vm.compareSearch.name = data.name;
             vm.compareSearch.constructionCode = data.code;
             vm.compareSearch.constructionId = data.id;
        }
        
        var record = 0;
        function fillDataTable(data) {
        	vm.compareGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save : function(){
                    vm.compareGrid.refresh();
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: [
                    {
                        name: "actions",
                        template:'<div class="btn-group pull-right margin_top_button ">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">' +
                        '<label ng-repeat="column in vm.compareGrid.columns.slice(1,vm.compareGrid.columns.length)| filter: vm.gridColumnShowHideFilter">' +

                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#appCountWorkItem").text("" + response.total);
                            vm.countCompare = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "synStockDailyIeService/doSearchCompareReport",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.compareSearch.page = options.page
                            vm.compareSearch.pageSize = options.pageSize
                            return JSON.stringify(vm.compareSearch)
                        }
                    },
                    pageSize: 10,
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
                        width: '50px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                       

                    },
                    {
                        title: "Số PXK",
                        field: 'synStockTransCode',
                        width: '180px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        footerTemplate: function(item) {
                        	return "Tổng cộng";
						},
                    },
                    {
                        title: "Ngày xuất kho",
                        field: 'realIeTransDate',
                        width: '180px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                    },
                    {
                        title: "Tên VTTB",
                        field: 'goodsName',
                        width: '180px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Mã vật tư",
                        field: 'goodsCode',
                        width: '180px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "ĐVT",
                        field: 'goodsUnitName',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                    },
                    {
                        title: "Mã công trình xuất kho",
                        field: 'constructionCode',
                        width: '180px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Mã trạm/tuyến",
                        field: 'catStationCode',
                        width: '180px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Xuất kho",
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                            	title: "Số lượng",
                                field: 'numberXuatKho',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                format: "{0:n}",
                                type :'number',
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.numberXuatKho, "n");
        						},
                            }, {
                            	title: "Thành tiền",
            					field: 'totalMoneyXuatKho',
                                width: '250px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                format: "{0:n}",
                                type :'number',
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.totalMoneyXuatKho, "n");
        						},
                            }
                        ]
                        
                    },
                    {
                        title: "Số thực tế thi công",
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                            	title: "Số lượng",
                                field: 'numberThucTeThiCong',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                type :'number',
                                format: "{0:n}",
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.numberThucTeThiCong, "n");
        						},
                            }, {
                            	title: "Thành tiền",
            					field: 'totalMoneyThucTeThiCong',
                                width: '250px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                type :'number',
                                format: "{0:n}",
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.totalMoneyThucTeThiCong, "n");
        						},
                            }
                        ]
                        
                    },
                    {
                        title: "Đã thu hồi",
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                            	title: "Số lượng",
                                field: 'numberThuHoi',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                type :'number',
                                format: "{0:n}",
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.numberThuHoi, "n");
        						},
                            }, {
                            	title: "Thành tiền",
            					field: 'totalMoneyThuHoi',
                                width: '250px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                type :'number',
                                format: "{0:n}",
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.totalMoneyThuHoi, "n");
        						},
                            }
                        ]
                        
                    },
                    {
                        title: "Số còn lại chưa thu hồi",
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                            	title: "Số lượng",
                                field: 'numberChuaThuHoi',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                type :'number',
                                format: "{0:n}",
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.numberChuaThuHoi, "n");
        						},
                            }, {
                            	title: "Thành tiền",
            					field: 'totalMoneyChuaThuHoi',
                                width: '250px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:right;"
                                },
                                type :'number',
                                format: "{0:n}",
                                footerTemplate: function(item) {
                                    return kendo.toString(vm.sumHolder.totalMoneyChuaThuHoi, "n");
        						},
                            }
                        ]
                        
                    },
                    {
                        title: "Số PNK thu hồi",
                        field: 'fromSynStockTransCode',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text'
                    }
//                    {
//                        title: "Ghi chú",
//                        field: '',
//                        width: '100px',
//                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
//                        attributes: {
//                            style: "text-align:right;"
//                        },
//                        type :'text'
//                    }             
    ]
      
    });
}

        /*
        function doSearch() {
          var grid = vm.compareGrid;
          if(vm.compareSearch.name==null){
        	  $('#constructionReq').css({"color":"red", "display":""});
        	  return false;
          } else {
        	  $('#constructionReq').css({"color":"red", "display":"none"});
          }
          if (grid) {
              grid.dataSource.query({
                  page: 1,
                  pageSize: 10
              });
              grid.dataSource.read();
          }
        }
        */
        
        vm.showHideWorkItemColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.compareGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.compareGrid.showColumn(column);
            } else {
                vm.compareGrid.hideColumn(column);
            }
        }
        
        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };
        
        vm.cancelInput=function(){
        	vm.compareSearch.name = null;
        	vm.compareSearch.code = null;
        	vm.compareSearch.constructionId = null;
        }
        
        vm.exportFile= function(){
        	function displayLoading(target) {
              var element = $(target);
              kendo.ui.progress(element, true);
              setTimeout(function(){
                	return Restangular.all("synStockDailyIeService/exportExcelCompare").post(vm.compareSearch).then(function (d) {
                	    var data = d.plain();
                	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                	    kendo.ui.progress(element, false);
                	    vm.disableExportExcel = false;
                	}).catch(function (e) {
                		kendo.ui.progress(element, false);
                	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                	    vm.disableExportExcel = false;
                	    return;
                	});
        	});
        		
          }
        	displayLoading(".tab-content");
        }
}
})();
