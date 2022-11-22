(function () {
    'use strict';
    var controllerId = 'reportDetailWoTthqController';

    angular.module('MetronicApp').controller(controllerId, reportDetailWoTthqController, '$scope','$modal','$rootScope');

    function reportDetailWoTthqController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, reportDetailWoTthqService, htmlCommonService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modal) {

        var vm = this;
        vm.String = "Quản lý công trình > Báo cáo > Báo cáo chi tiết WO TTHQ";
        vm.searchForm = {};
        
        init();
        function init(){
        	fillDataTable([]);
        }
        
        vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.detailsWoTthqGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.detailsWoTthqGrid.showColumn(column);
			} else {
				vm.detailsWoTthqGrid.hideColumn(column);
			}
		}

		vm.gridColumnShowHideFilter = function(item) {
			return item.type == null || item.type !== 1;
		};

		vm.doSearch = function doSearch() {
			var grid = vm.detailsWoTthqGrid;
			if (grid) {
				grid.dataSource.query({
					page : 1,
					pageSize : 10
				});
			}
		}

		vm.lstStatus = [
			{
				code: "",
				name: "Tất cả"
			},
			{
				code: "1",
				name: "Hiệu quả"
			},
			{
				code: "2",
				name: "Không hiệu quả"
			}
			];
		
        var record = 0;
        function fillDataTable(data){
        	vm.detailsWoTthqGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar : [ {
					name : "actions",
					template : '<div class="btn-group pull-right margin_top_button ">'
							+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
							+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'
							+ '<label ng-repeat="column in vm.detailsWoTthqGrid.columns.slice(1,vm.detailsWoTthqGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
							+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}'
							+ '</label>' + '</div>' + '</div>'
				} ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                             $("#appCountTthq").text("" + response.total);
                             vm.countTthq = response.total;
                             return response.total;
                        },
                        data: function (response) {
                            $scope.checklists = response.data;
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/tthqService/getDataTableTTTHQ",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page;
                            vm.searchForm.pageSize = options.pageSize;
                            return JSON.stringify(vm.searchForm);

                        }
                    },
                    pageSize: 10,
                },
                noRecords: true,
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
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '80px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Khu vực",
                        field: 'areaCode',
                        width: '100px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;height:44px;white-space: normal;"
                        }
                    },
                    {
                        title: "Tỉnh",
                        field: 'provinceCode',
                        width: '100px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Mã WO",
                        field: 'woCode',
                        width: '180px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Mã trạm (VTN)",
                        field: 'stationCodeVtn',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        }
                    },
    				{
                        title: "Mã trạm (VCC)",
                        field: 'stationCodeVcc',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        }
                    },
                    {
                        title: "Địa chỉ",
                        field: 'address',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        },
                    },
                    {
                        title: "Loại trạm",
                        field: 'stationType',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Mái/đất",
                        field: 'maiDat',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Độ cao cột",
                        field: 'doCaoCot',
                        width: '100px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Loại cột",
                        field: 'loaiCot',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        },
                    },
                    {
                        title: "Móng co",
                        field: 'mongCo',
                        width: '100px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Loại nhà",
                        field: 'loaiNha',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        },
                    },
                    {
                        title: "Tiếp địa",
                        field: 'tiepDia',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Điện CNKT",
                        field: 'dienCnkt',
                        width: '100px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Số cột điện",
                        field: 'soCotDien',
                        width: '100px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Vận chuyển bộ",
                        field: 'vanChuyenBo',
                        width: '100px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Thuê Acquy",
                        field: 'thueAcquy',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Giá thuê MB thực tế",
                        field: 'giaThueMbThucTe',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Giá thuê MB theo định mức",
                        field: 'giaThueMbTheoDinhMuc',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Capex cột",
                        field: 'capexCot',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Capex tiếp địa",
                        field: 'capexTiepDia',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Capex AC",
                        field: 'capexAc',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Capex phòng máy",
                        field: 'capexPhongMay',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Tổng Capex HT",
                        field: 'tongCapexHt',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: " VCC chào giá thuê hạ tầng",
                        field: 'vccChaoGiaHt',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "VCC chào giá thuê nguồn Accquy",
                        field: 'vccChaoGiaAcquy',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Tổng cộng (Chưa VAT)",
                        field: 'tongCong',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Ghi chú",
                        field: 'description',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:left;white-space: normal;"
                        },
                    },
                    {
                        title: "Ngày gửi",
                        field: 'ngayGui',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Hoàn thành",
                        field: 'ngayHoanThanh',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Người lập",
                        field: 'nguoiLap',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Capex HT VCC",
                        field: 'capexHtVcc',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "NPV",
                        field: 'npv',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "IRR",
                        field: 'irr',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Thời gian HV",
                        field: 'thoiGianHv',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "LNST/DT",
                        field: 'lnstDt',
                        width: '120px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                    {
                        title: "Kết luận",
                        field: 'conclude',
                        width: '150px',
                        headerAttributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                        attributes: {
                            style: "text-align:center;white-space: normal;"
                        },
                    },
                ]
            });
        }
        
        vm.provinceOptions = {
		        dataTextField: "name",
		        dataValueField: "id",
				placeholder:"Nhập mã hoặc tên tỉnh",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.searchForm.provinceCode = dataItem.code;
					vm.searchForm.provinceName = dataItem.name;
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
		                    return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
		                        name: vm.searchForm.provinceName,
		                        pageSize: vm.provinceOptions.pageSize,
		                        page: 1
		                    }).then(function (response) {
		                        options.success(response.data);
		                    }).catch(function (err) {
		                        console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
		                    });
		                }
		            }
		        },
				headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
				'<p class="col-md-6 text-header-auto border-right-ccc">Mã tỉnh</p>' +
				'<p class="col-md-6 text-header-auto">Tên tỉnh</p>' +
				'</div>',
		        template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
		        change: function (e) {
		        	if (!vm.isSelect) {
		                vm.searchForm.provinceCode = null;
						vm.searchForm.provinceName = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		                vm.searchForm.provinceCode = null;
						vm.searchForm.provinceName = null;
		            }
		        }
		    }
		
		vm.openCatProvincePopup = openCatProvincePopup;
		vm.onSaveCatProvince = onSaveCatProvince;
	    function openCatProvincePopup(){
			var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm tỉnh");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
	    }
	    function onSaveCatProvince(data){
	        vm.searchForm.provinceCode = data.code;
			vm.searchForm.provinceName = data.name;
	        htmlCommonService.dismissPopup();
	    };
	    
	    vm.excelReport = function(){
	    	kendo.ui.progress($("#detailsWoTthqGridId"), true);
			vm.searchForm.page = null;
            vm.searchForm.pageSize = null;
            vm.listRemove = [];
            vm.listConvert = [];
            reportDetailWoTthqService.doSearch(vm.searchForm).then(function (d) {
                CommonService.exportFile(vm.detailsWoTthqGrid, d.data, vm.listRemove, vm.listConvert, "Báo cáo chi tiết WO TTHQ");
                kendo.ui.progress($("#detailsWoTthqGridId"), false);
            });
	    }
	    
	    vm.cancelInput = function(param){
	    	if(param=='date'){
	    		vm.searchForm.fromDate = null;
	    		vm.searchForm.toDate = null;
	    	}
	    }
        // end controller
    }
})();