(function () {
    'use strict';
    var controllerId = 'tongHopPXKController';

    angular.module('MetronicApp').controller(controllerId, tongHopPXKController);

    function tongHopPXKController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, tongHopPXKService, htmlCommonService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http) {

        var vm = this;
        // variables
        vm.String = "Quản lý công trình > Báo cáo > Báo cáo tổng hợp PXK ra công trình trong tháng";
        vm.provinceList = [];
        vm.searchForm = {};
        
        init();
        function init(){
        	//
        	var endDate = new Date();
        	var previousMonth = new Date();
        	previousMonth.setMonth(previousMonth.getMonth()-1);
        	vm.searchForm.startDate = htmlCommonService.formatDate(previousMonth);
        	vm.searchForm.endDate = htmlCommonService.formatDate(endDate);
        	//
        	fillDataTable();
        	loadProvinceList();
        }
        function loadProvinceList(){
        	return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
        		page : 1,
        		pageSize : 65
            }).then(function (response) {
                vm.provinceList = response.data;
            }).catch(function (err) {
                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
            });
        }
        vm.doSearch= doSearch;
        function doSearch(){
        	if(vm.searchForm.startDate =='' || vm.searchForm.endDate == ''){
        		toastr.error(gettextCatalog.getString("Từ ngày ,  đến ngày bắt buộc nhập"));
        		$('#startDate').focus();
        		return;
        	}
        	var grid = vm.reportKpi45DaysGrid;
        	if(grid){
        		grid.dataSource.query({
        			page: 1,
                    pageSize: 10
        		})
        	}
        }
        
        vm.cancelInput = function(data){
        	if(data=='dept'){
        		vm.searchForm.keySearch = null;
        	}
        }
        
        vm.openDepartmentTo = openDepartmentTo
        function openDepartmentTo(popUp) {
            vm.obj = {};
            vm.departmentpopUp = popUp;
            var templateUrl = 'coms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
        }

        vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'dept') {
                vm.searchForm.sysGroupName = data.text;
                vm.searchForm.sysGroupId = data.id;
            }
        }
        
        vm.selectedDept2 = false;
        vm.deprtOptions2 = {
            dataTextField: "text",
            dataValueField: "id",
        	placeholder:"Nhập mã hoặc tên đơn vị",
            select: function (e) {
                vm.selectedDept2 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.searchForm.sysGroupName = dataItem.text;
                vm.searchForm.sysGroupId = dataItem.id;
            },
            pageSize: 10,
            open: function (e) {
                vm.selectedDept2 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedDept2 = false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({
                            name: vm.searchForm.sysGroupName,
                            pageSize: vm.deprtOptions2.pageSize
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.searchForm.sysGroupName = null;// thành name
                    vm.searchForm.sysGroupId = null;
                }
            },
            ignoreCase: false
        }
        
        vm.showHideWorkItemColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.reportKpi45DaysGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.reportKpi45DaysGrid.showColumn(column);
            } else {
                vm.reportKpi45DaysGrid.hideColumn(column);
            }


        }
        /*
		 * * Filter các cột của select
		 */

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };
        
        var record = 0;
        function fillDataTable(){
        	vm.reportKpi45DaysGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable:false,
                save : function(){
                    vm.workItemGrid.refresh();
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: [
                    {
                        name: "actions",
                        template: '<div class=" pull-left ">' +
// '<button id="cancelAppQuantity" class="btn btn-qlk padding-search-left
// cancel_confirm_quantity"' +
// 'ng-click="vm.removeFillterWorkItem()" uib-tooltip="Hủy xác nhận"
// translate>Hủy xác nhận</button>' +
                        '</div>'
                        +
                        '<div class="btn-group pull-left margin_top_button ">' +
                        '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: triệu VNĐ</div>'+

                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-disabled="vm.disableExportExcel" ng-click="vm.exportWorkItemFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">' +
                        '<label ng-repeat="column in vm.reportKpi45DaysGrid.columns.slice(1,vm.reportKpi45DaysGrid.columns.length)| filter: vm.gridColumnShowHideFilter">' +

                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#appCount").text("" + response.total);
                            vm.count = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "rpBTSService/doSearchTongHopPXK",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page
                            vm.searchForm.pageSize = options.pageSize
                            return JSON.stringify(vm.searchForm)

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
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                    },
                    {
                        title: "Mã phiếu xuất",
                        field: 'pxkCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Ngày xuất",
                        field: 'pxkDateS',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Mã kho xuất",
                        field: 'stockCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        
                    },
                    {
                        title: "Công trình",
                        field: 'constructionCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Đơn vị",
                        field: 'sysGroupName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Tỉnh",
                        field: 'provinceCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Mã nhà trạm",
                        field: 'catStationHouseCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Mã trạm",
                        field: 'stationCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Hợp đồng",
                        field: 'contractCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Tên vật tư thiết bị",
                        field: 'vttbName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Mã vật tư thiết bị",
                        field: 'vttbCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Đơn vị tính",
                        field: 'dvt',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Serial",
                        field: 'serial',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },
                    {
                        title: "Yêu cầu",
                        field: 'soLuongPxk',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        format: "{0:n0}"
                    },
                    {
                        title: "Thực xuất",
                        field: 'soLuongSuDung',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        format: "{0:n0}"
                    },
                    {
                        title: "Đơn giá",
                        field: 'donGia',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        format: "{0:n0}"
                    },
                    {
                        title: "Thành tiền",
                        field: 'giaTriSuDung',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        format: "{0:n0}"
                    },
                    {
                        title: "Trạng thái PXK",
                        field: 'status',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    }
                    
    ]
    });
        }
     
        function numberWithCommas(x) {
            if(x == null || x == undefined){
            return '0';
            }
            x=x/1000000;
            var parts = x.toFixed(2).toString().split(".");	
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
        }
        
    vm.exportWorkItemFile = function(){
    	kendo.ui.progress($(".tab-content"), true);
    	vm.searchForm.page = null;
    	vm.searchForm.pageSize = null;
    	return Restangular.all("rpBTSService/exportFileTongHopPXK").post(vm.searchForm).then(function (d) {
    	    var data = d.plain();
    	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
    	    kendo.ui.progress($(".tab-content"), false);
    	}).catch(function (e) {
    		kendo.ui.progress($(".tab-content"), false);
    	    toastr.error(gettextCatalog.getString("Lỗi khi tải báo cáo!"));
    	    return;
    	});
    }
        
// end controller
    }
})();