(function() {
    'use strict';
    var controllerId = 'rpBTSByDAController';

    angular.module('MetronicApp').controller(controllerId, rpBTSByDAController,'$modal');

    function rpBTSByDAController($scope, $rootScope, $timeout, gettextCatalog,
                                    kendoConfig, $kWindow,htmlCommonService,rpBTSByDAService,
                                    CommonService, PopupConst, Restangular, RestEndpoint,Constant,$http,$modal) {
        var vm = this;
        vm.searchForm={
            obstructedState:1,
            typeBc: 1
        };
        vm.searchFormS={
                obstructedState:1
            };
        vm.searchFormW={
                obstructedState:1
            };
        
        vm.d={};
        vm.String="Báo cáo > Báo cáo BTS theo dự án";
        $scope.modalTitle='';
        $scope.moadalP = null;
        
        initFormData();
        function initFormData() {
        	vm.searchForm.monthYearD = kendo.toString(new Date(), "MM/yyyy");
            fillDataTable();
        }
        vm.openCatProvincePopup = openCatProvincePopup;
        function openCatProvincePopup(){
    		var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm tỉnh");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
        }

        vm.onSaveCatProvince = onSaveCatProvince;
        var catProvinceName;
        function onSaveCatProvince(data){
            vm.searchForm.catProvince = data.name;
            vm.searchForm.provinceCodeD = data.code;
            htmlCommonService.dismissPopup();
            $("#province").focus();
	    };
	    
	    vm.provinceOptions = {
	            dataTextField: "name",
	            dataValueField: "id",
	            placeholder: "Nhập mã hoặc tên tỉnh",
	            select: function (e) {
	                vm.selectedDept1 = true;
	                var dataItem = this.dataItem(e.item.index());
	                vm.searchForm.catProvince = dataItem.name;
	                vm.searchForm.provinceCode = dataItem.code;
	            },
	            pageSize: 10,
	            open: function (e) {
	                vm.selectedDept1 = false;
	            },
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function (options) {
	                        vm.selectedDept1 = false;
	                        return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
	                            name: vm.searchForm.catProvince,
	                            pageSize: 10,
	                            page: 1
	                        }).then(function (response) {
	                            options.success(response.data);
	                        }).catch(function (err) {
	                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
	                        });
	                    }
	                }
	            },
	            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
	            change: function (e) {
	                if (e.sender.value() === '') {
	                    vm.searchForm.catProvince = null;
	                    vm.searchForm.provinceCode = null;
	                    vm.searchForm.provinceCodeD = null;
	                }
	            },
	            ignoreCase: false
	        };

        vm.listMonthPlan=[];

        vm.validatorOptions = kendoConfig.get('validatorOptions');
        setTimeout(function(){
            $("#keySearch").focus();
        },15);
        var record=0;
        vm.doSearch = function(){
        	if(vm.searchForm.monthYearD==undefined ||vm.searchForm.monthYearD == null||vm.searchForm.monthYearD==''){
                toastr.warning("Thời gian không được để trống!");
                return false;
            }
            var grid =vm.gridReportBTSByDA;
            if(grid){
                grid.dataSource.page(1);
            }
        };
        
        vm.doSearchStation = function(){
            var grid =vm.gridDetail;
            if(grid){
                grid.dataSource.page(1);
            }
        };
        
        function fillDataTable() {
            vm.gridReportBTSByDAOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
                dataSource:{
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#listdata").text(""+response.total);
                            vm.countRecord=response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                          url: Constant.BASE_SERVICE_URL + "rpBTSService/doSearchReportBTSByDA",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page
                            vm.searchForm.pageSize = options.pageSize
                            return JSON.stringify(vm.searchForm)
                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'></div>")
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
                columns : [
					{
						title: "TT",
                        field:"stt",
                        template: function () {
                            return ++record;
                        },
                        width: '40px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        locked: true,
					},
					{
						title : "Tỉnh",
						field : 'provinceCode',
						width : '150px',
						headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
                        attributes: {style: "text-align:left;"},
                        template: function(data){
                            return data.provinceCode;
                        },
                        footerTemplate: function(item) {
                            return kendo.toString("Tổng", "");
                        },
                        locked: true,
					},
					{
						title : "Trạm VCC",
						field : 'stationVccCode',
						width : '150px',
						headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
                        attributes: { style: "text-align:left;"},
                        hidden: true,
                        locked: true,
					},
					{
						title : "Trạm VTNET",
						field : 'stationVtNetCode',
						width : '150px',
						headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
                        attributes: {style: "text-align:left;"},
                        hidden: true,
                        locked: true,
					},
					{
						title : "Dự án",
						field : 'projectCode',
						width : '150px',
						headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
                        attributes: {style: "text-align:left;"},
                        hidden: true,
                        locked: true,
					},
					{
						title : "Tổng",
						field : 'count',
						width : '150px',
						headerAttributes : {style : "text-align:center;font-weight: bold;"},
						attributes : {style : "text-align:left;"},
                        footerTemplate: function(item) {
                            var data = vm.gridReportBTSByDA.dataSource.data();
                            var sum = 0;
                            for (var idx = 0; idx < data.length; idx++) {
                                if (idx == 0) {
                                    item = data[idx];
                                    sum =numberWithCommas(item.sumCount);
                                    break;
                                }
                            }
                            return kendo.toString(sum, "");
                        },
                        locked: true,
					},
					{
						title : "Thuê mặt bằng",
						field : 'tMB',
						width : '150px',
						headerAttributes : {style : "text-align:center;font-weight: bold;"},
						attributes : {style : "text-align:left;"},
						footerTemplate: function(item) {
                            var data = vm.gridReportBTSByDA.dataSource.data();
                            var sum = 0;
                            for (var idx = 0; idx < data.length; idx++) {
                                if (idx == 0) {
                                    item = data[idx];
                                    sum =numberWithCommas(item.sumTMB);
                                    break;
                                }
                            }
                            return kendo.toString(sum, "");
                        },
					},
					{
						title : "DK đảm bảo",
						width : '150px',
						headerAttributes : {style : "text-align:center;font-weight: bold;"},
						attributes : {style : "text-align:left;"},
						columns: [
							{
								title : "TK-DT",
								field : 'tKDT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: { style: "text-align:right;"},
	                            footerTemplate: function(item) {
	                                    var data = vm.gridReportBTSByDA.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumTKDT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Thẩm TK-DT",
								field : 'thamTKDT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {style: "text-align:right;"},
	                            footerTemplate: function(item) {
	                                    var data = vm.gridReportBTSByDA.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumThamTKDT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Tồn chưa có TK + DT Thẩm",
								field : 'tonChuaCoTKDTTham',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {style: "text-align:right;"},
		                        footerTemplate: function(item) {
	                                    var data = vm.gridReportBTSByDA.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumTonChuaCoTKDTTham);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},{
								title : "Vướng",
								field : 'vuong',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {style: "text-align:right;"},
		                        footerTemplate: function(item) {
	                                    var data = vm.gridReportBTSByDA.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumVuong);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},{
								title : "Đã khởi công",
								field : 'daKC',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {style: "text-align:right;"},
		                        footerTemplate: function(item) {
	                                    var data = vm.gridReportBTSByDA.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumDaKC);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},{
								title : "Đồng bộ hạ tầng",
								field : 'dBHT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {style: "text-align:right;"},
		                        footerTemplate: function(item) {
	                                    var data = vm.gridReportBTSByDA.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumDBHT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},{
								title : "Đã phát sóng",
								field : 'daPS',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {style: "text-align:right;"},
		                        footerTemplate: function(item) {
	                                    var data = vm.gridReportBTSByDA.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumDaPS);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},{
								title : "HSHC về TTHT",
								field : 'hSHCVeTTHT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {style: "text-align:right;"},
		                        footerTemplate: function(item) {
	                                    var data = vm.gridReportBTSByDA.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumHSHCVeTTHT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},{
								title : "HSHC về ĐTHT",
								field : 'hSHCVeDTHT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {style: "text-align:right;"},
		                        footerTemplate: function(item) {
	                                    var data = vm.gridReportBTSByDA.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumHSHCVeDTHT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},{
								title : "Tồn HSHC chưa về TTHT(KPI)",
								field : 'tonHSHCChuaVeTTHT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {style: "text-align:right;"},
		                        columns: [
									{
										title : "Tổng",
										field : 'countTonHSHCChuaVeTTHT',
										width : '150px',
										headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
				                        attributes: { style: "text-align:right;"},
			                            footerTemplate: function(item) {
			                                    var data = vm.gridReportBTSByDA.dataSource.data();
			                                    var sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                        if (idx == 0) {
			                                            item = data[idx];
			                                            sum =numberWithCommas(item.sumCountTonHSHCChuaVeTTHT);
			                                            break;
			                                        }
			                                    }
			                                    return kendo.toString(sum, "");
			                                },
									},
									{
										title : "N+1",
										field : 'n1TonHSHCChuaVeTTHT',
										width : '150px',
										headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
				                        attributes: {style: "text-align:right;"},
			                            footerTemplate: function(item) {
			                                    var data = vm.gridReportBTSByDA.dataSource.data();
			                                    var sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                        if (idx == 0) {
			                                            item = data[idx];
			                                            sum =numberWithCommas(item.sumN1TonHSHCChuaVeTTHT);
			                                            break;
			                                        }
			                                    }
			                                    return kendo.toString(sum, "");
			                                },
									},
									{
										title : "N+2",
										field : 'n2TonHSHCChuaVeTTHT',
										width : '150px',
										headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
				                        attributes: {style: "text-align:right;"},
				                        footerTemplate: function(item) {
			                                    var data = vm.gridReportBTSByDA.dataSource.data();
			                                    var sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                        if (idx == 0) {
			                                            item = data[idx];
			                                            sum =numberWithCommas(item.sumN2TonHSHCChuaVeTTHT);
			                                            break;
			                                        }
			                                    }
			                                    return kendo.toString(sum, "");
			                                },
									},{
										title : "N+3",
										field : 'n3TonHSHCChuaVeTTHT',
										width : '150px',
										headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
				                        attributes: {style: "text-align:right;"},
				                        footerTemplate: function(item) {
			                                    var data = vm.gridReportBTSByDA.dataSource.data();
			                                    var sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                        if (idx == 0) {
			                                            item = data[idx];
			                                            sum =numberWithCommas(item.sumN3TonHSHCChuaVeTTHT);
			                                            break;
			                                        }
			                                    }
			                                    return kendo.toString(sum, "");
			                                },
									},{
										title : "N+4",
										field : 'n4TonHSHCChuaVeTTHT',
										width : '150px',
										headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
				                        attributes: {style: "text-align:right;"},
				                        footerTemplate: function(item) {
			                                    var data = vm.gridReportBTSByDA.dataSource.data();
			                                    var sum = 0;
			                                    for (var idx = 0; idx < data.length; idx++) {
			                                        if (idx == 0) {
			                                            item = data[idx];
			                                            sum =numberWithCommas(item.sumN4TonHSHCChuaVeTTHT);
			                                            break;
			                                        }
			                                    }
			                                    return kendo.toString(sum, "");
			                                },
									},
								]
		                        
							},
						]
					},
					{
						title : "Tồn HSHC chưa về DHTH(KPI)",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;" },
						attributes : { style : "text-align:left;" },
						columns: [
							{
								title : "Tổng",
								field : 'countTonHSHCChuaVeDHTH',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: { style: "text-align:right;"},
	                            footerTemplate: function(item) {
	                                    var data = vm.gridReportBTSByDA.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumCountTonHSHCChuaVeDHTH);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "N+1",
								field : 'n1TonHSHCChuaVeDHTH',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {style: "text-align:right;"},
	                            footerTemplate: function(item) {
	                                    var data = vm.gridReportBTSByDA.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumN1TonHSHCChuaVeDHTH);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "N+2",
								field : 'n2TonHSHCChuaVeDHTH',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {style: "text-align:right;"},
		                        footerTemplate: function(item) {
	                                    var data = vm.gridReportBTSByDA.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumN2TonHSHCChuaVeDHTH);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},{
								title : "N+3",
								field : 'n3TonHSHCChuaVeDHTH',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {style: "text-align:right;"},
		                        footerTemplate: function(item) {
	                                    var data = vm.gridReportBTSByDA.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumN3TonHSHCChuaVeDHTH);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},{
								title : "N+4",
								field : 'n4TonHSHCChuaVeDHTH',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {style: "text-align:right;"},
		                        footerTemplate: function(item) {
	                                    var data = vm.gridReportBTSByDA.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumN4TonHSHCChuaVeDHTH);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
						]
					}
					]
            });
        }
        function numberWithCommas(x) {
            if(x == null || x == undefined){
                return '0';
            }
            var parts = x.toFixed(2).toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
        }
        
        

        
        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };
        vm.cancelInputProvince = function() {
        	vm.searchForm.catProvince = null;
        	vm.searchForm.provinceCode =  null;
   	    }

        function cancelInput(){
			vm.searchForm.monthYearD=null;
        }
        vm.monthSelectorOptions = {
                start: "year",
                depth: "year"
         };
        
        vm.exportFile= function(){
	    	vm.searchFormS.page = null;
			vm.searchFormS.pageSize = null;
			
			kendo.ui.progress($("#gridDetail"), true);
    		var obj = {};
    		obj = angular.copy(vm.searchFormS);
				return Restangular.all("rpBTSService/exportStation").post(obj).then(function (d){
	    		  var data = d.plain();
                  window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                  kendo.ui.progress("#gridDetail", false);
			});
        }
        
        vm.exportexcel= function(){
	    	vm.searchForm.page = null;
			vm.searchForm.pageSize = null;
			
			kendo.ui.progress($("#gridReportBTSByDA"), true);
    		var obj = {};
    		obj = angular.copy(vm.searchForm);
				return Restangular.all("rpBTSService/exportexcelRPBTSByDA").post(obj).then(function (d){
	    		  var data = d.plain();
                  window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                  kendo.ui.progress("#gridReportBTSByDA", false);
			});
        }
        
        vm.changeTypeBc = function() {
        	if(vm.searchForm.typeBc == 2){
        		$("#gridKHBTSId").data("kendoGrid").showColumn("stationVccCode");
        		$("#gridKHBTSId").data("kendoGrid").showColumn("stationVtNetCode");
        		$("#gridKHBTSId").data("kendoGrid").showColumn("projectCode");
        	} else {
        		$("#gridKHBTSId").data("kendoGrid").hideColumn("stationVccCode");
        		$("#gridKHBTSId").data("kendoGrid").hideColumn("stationVtNetCode");
        		$("#gridKHBTSId").data("kendoGrid").hideColumn("projectCode");
        	}
        	vm.doSearch();
        }
        
        vm.projectOptions = {
				dataTextField: "name",
				dataValueField: "name",
				placeholder: "Nhập mã hoặc tên tỉnh",
				select: function (e) {
					var dataItem = this.dataItem(e.item.index());
					vm.searchForm.projectName = dataItem.name;
					vm.searchForm.projectCode = dataItem.code;
				},
				pageSize: 10,
				open: function (e) {
					vm.selectedDept1 = false;
				},
				dataSource: {
					serverFiltering: true,
					transport: {
						read: function (options) {
							vm.selectedDept1 = false;
							return Restangular.all("rpBTSService/getProjectForAutocomplete").post({
								name: vm.searchForm.projectName,
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
				template: '<div class="row" ><div class="col-xs-5" style="float:left"> #: data.name #</div> </div>',
				change: function (e) {
					if (e.sender.value() === '') {
						vm.searchForm.projectName = null;
						vm.searchForm.projectCode = null;
					}
				},
				ignoreCase: false
			};
    }
})();
