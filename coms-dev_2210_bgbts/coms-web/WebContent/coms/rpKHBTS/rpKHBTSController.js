(function() {
    'use strict';
    var controllerId = 'rpKHBTSController';

    angular.module('MetronicApp').controller(controllerId, rpKHBTSController,'$modal');

    function rpKHBTSController($scope, $rootScope, $timeout, gettextCatalog,
                                    kendoConfig, $kWindow,htmlCommonService,rpKHBTSService,
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
        vm.String="Báo cáo > Báo cáo tiến độ kế hoạch HTCT";
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
	                vm.searchForm.provinceCodeD = dataItem.code;
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
            var grid =vm.gridKHBTS;
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
        
        vm.viewWo = viewWo;
        function viewWo(woTypeId,trTypeId,catWorkItemTypeId,vccCode,type) {
        	debugger
        	vm.searchFormW.woTypeId = woTypeId;
            vm.searchFormW.trTypeId=trTypeId;
            vm.searchFormW.catWorkItemTypeId= catWorkItemTypeId;
            vm.searchFormW.vccCode= vccCode;
            vm.searchFormW.type= type;
            $scope.moadalP = $modal.open({templateUrl: 'coms/rpKHBTS/detailWO.html',controller: null,windowClass: '',scope: $scope}).result.then(
                    function() {
                    },
                    function() {
                    }
                )
                fillDataWO();
//                fillDataStationDetail1();
		}

        vm.viewStationDetail = viewStationDetail;
        function viewStationDetail(types,provinceCode,time){
        	if(vm.searchForm.typeBc==2){
        		return;
        	}
        	vm.searchFormS.vccCode = "";
        	if(types == 1){
        		$scope.modalTitle = "Thuê mặt bằng"
        	}
        	if(types == 2){
        		$scope.modalTitle = "Thiết kế dự toán"
        	}
        	if(types == 3){
        		$scope.modalTitle = "Thẩm thiết kế dự toán"
        	}
        	if(types == 4){
        		$scope.modalTitle = "Khởi công"
        	}
        	if(types == 5){
        		$scope.modalTitle = "Đồng bộ hạ tầng"
        	}
        	if(types == 6){
        		$scope.modalTitle = "Phát sóng"
        	}
        	if(types == 7){
        		$scope.modalTitle = "Lên doanh thu"
        	}
        	if(types == 8){
        		$scope.modalTitle = "HSHC về TTHT"
        	}
        	vm.searchFormS.types = types;
            vm.searchFormS.provinceCode=provinceCode;
            vm.searchFormS.monthYear=time;
            $scope.moadalP = $modal.open({templateUrl: 'coms/rpKHBTS/detailStation.html',controller: null,windowClass: '',scope: $scope}).result.then(
                function() {
                },
                function() {
                }
            )
            /*var teamplateUrl = "coms/rpKHBTS/detailStation.html";
			var title = $scope.modalTitle;
			var windowId = "EDIT_CONFIG_STAFF";
			CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '45%', '45%', "deptAdd");*/
            
            if(types == 1){
            	fillDataStationDetail1();
        	}
        	if(types == 2){
        		fillDataStationDetail2();
        	}
        	if(types == 3){
        		fillDataStationDetail3();
        	}
        	if(types == 4){
        		fillDataStationDetail4();
        	}
        	if(types == 5){
        		fillDataStationDetail5();
        	}
        	if(types == 6){
        		fillDataStationDetail6();
        	}
        	if(types == 7){
        		fillDataStationDetail7();
        	}
        	if(types == 8){
        		fillDataStationDetail8();
        	}
            
        }
        function fillDataTable() {
            vm.gridKHBTSOptions = kendoConfig.getGridOptions({
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
                          url: Constant.BASE_SERVICE_URL + "rpBTSService/doSearchReportKHBTS",
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
                        }
					},
					{
						title : "Trạm VCC",
						field : 'stationVccCode',
						width : '150px',
						headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
                        attributes: {
                            style: "text-align:left;"
                        },
                        hidden: true
					},
					{
						title : "Trạm VTNET",
						field : 'stationVtNetCode',
						width : '150px',
						headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
                        attributes: {
                            style: "text-align:left;"
                        },
                        hidden: true
					},
					{
						title : "Tỉnh",
						field : 'provinceCode',
						width : '150px',
						headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(data){
                            return data.provinceCode;
                        },
                        footerTemplate: function(item) {
                            return kendo.toString("Tổng", "");
                        },
					},
					{
						title : "Thuê mặt bằng",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Kế hoạch (trạm)",
								field : 'planTMB',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                            var type = '1';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type +'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+data.planTMB+'</div>';
		                        },
                               footerTemplate: function(item) {
                                    var data = vm.gridKHBTS.dataSource.data();
                                    var sum = 0;
                                    for (var idx = 0; idx < data.length; idx++) {
                                        if (idx == 0) {
                                            item = data[idx];
                                            sum =numberWithCommas(item.sumPlanTMB);
                                            break;
                                        }
                                    }
                                    return kendo.toString(sum, "");
                                },
							},
							{
								title : "Thực hiện (trạm)",
								field : 'performTMB',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	 var type = '1';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type +'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+  data.performTMB +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPerformTMB);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Tỷ lệ",
								field : 'ratioTMB',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	 var type = '1';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type +'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.ratioTMB +'(%)'+'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumRatioTMB) + '%';
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
						]
					},{
						title : "TK-DT",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Kế hoạch (trạm)",
								field : 'planTKDT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	 var type = '2';
			                         return '<div ng-click="vm.viewStationDetail(\''+ type +'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.planTKDT +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPlanTKDT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Thực hiện (trạm)",
								field : 'performTKDT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	 var type = '2';
			                         return '<div ng-click="vm.viewStationDetail(\''+ type +'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.performTKDT +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPerformTKDT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Tỷ lệ",
								field : 'ratioTKDT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	 var type = '2';
			                          return '<div ng-click="vm.viewStationDetail(\''+ type +'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.ratioTKDT +'(%)'+'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumRatioTKDT) + '%';
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
						]
					},{
						title : "Thẩm TK-DT",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Kế hoạch (trạm)",
								field : 'planTTKDT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	 var type = '3';;
		                            return '<div ng-click="vm.viewStationDetail(\''+ type +'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.planTTKDT +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPlanTTKDT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Thực hiện (trạm)",
								field : 'performTTKDT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	 var type = '3';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type +'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.performTTKDT +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPerformTTKDT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Tỷ lệ",
								field : 'ratioTTKDT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	 var type = '3';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type +'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.ratioTTKDT +'(%)' +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumRatioTTKDT) + '%';
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
						]
					},
					{
						title : "Xong xây dựng",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Kế hoạch (trạm)",
								field : 'planKC',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '4';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.planKC +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPlanKC);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Thực hiện (trạm)",
								field : 'performKC',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '4';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.performKC +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPerformKC);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Tỷ lệ",
								field : 'ratioKC',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '4';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.ratioKC +'(%)' +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumRatioKC) + '%';
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
						]
					},
					{
						title : "Đồng bộ hạ tầng",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Kế hoạch (trạm)",
								field : 'planDBHT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '5';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.planDBHT +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPlanDBHT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Thực hiện (trạm)",
								field : 'performDBHT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '5';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.performDBHT +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPerformDBHT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Tỷ lệ",
								field : 'ratioDBHT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '5';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.ratioDBHT +'(%)' +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumRatioDBHT) + '%';
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
						]
					},
					{
						title : "Phát sóng",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Kế hoạch (trạm)",
								field : 'planPS',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '6';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.planPS +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPlanPS);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Thực hiện (trạm)",
								field : 'performPS',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '6';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.performPS +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPerformPS);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Tỷ lệ",
								field : 'ratioPS',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '6';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.ratioPS +'(%)' +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumRatioPS) + '%';
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
						]
					},{
						title : "Lên doanh thu",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Kế hoạch (trạm)",
								field : 'planDT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '7';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.planDT +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPlanDT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Thực hiện (trạm)",
								field : 'performDT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '7';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.performDT +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPerformDT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Tỷ lệ",
								field : 'ratioDT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '7';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.ratioDT +'(%)' +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumRatioDT) + '%';
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
						]
					},
					{
						title : "N+1 của CNKT về ĐTHT",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Kế hoạch (trạm)",
								field : 'planHSHCTTHT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '8';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.planHSHCTTHT +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPlanHSHCTTHT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Thực hiện (trạm)",
								field : 'performHSHCTTHT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '8';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.performHSHCTTHT +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPerformHSHCTTHT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Tỷ lệ",
								field : 'ratioHSHCTTHT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '8';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.ratioHSHCTTHT +'(%)' +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumRatioHSHCTTHT) + '%';
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
						]
					},{
						title : "HSHC về DTHT",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Kế hoạch (trạm)",
								field : 'planHSHCDTHT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '9';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.planHSHCDTHT +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPlanHSHCDTHT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Thực hiện (trạm)",
								field : 'performHSHCDTHT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '9';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.performHSHCDTHT +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumPerformHSHCDTHT);
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
							{
								title : "Tỷ lệ",
								field : 'ratioHSHCDTHT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
		                        template: function(data){
		                        	var type = '9';
		                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.ratioHSHCDTHT +'(%)' +'</div>';
		                        },
	                               footerTemplate: function(item) {
	                                    var data = vm.gridKHBTS.dataSource.data();
	                                    var sum = 0;
	                                    for (var idx = 0; idx < data.length; idx++) {
	                                        if (idx == 0) {
	                                            item = data[idx];
	                                            sum =numberWithCommas(item.sumRatioHSHCDTHT) + '%';
	                                            break;
	                                        }
	                                    }
	                                    return kendo.toString(sum, "");
	                                },
							},
						]
					}
					,{
						title : "Số trạm quá hạn KPI ",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [{
							title : "Quá hạn KPI thuê MB",
							field : 'overdueKPIRentMB',
							width : '150px',
							headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
	                        attributes: {
	                            style: "text-align:right;"
	                        },
	                        template: function(data){
	                        	var type = '1';
	                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.overdueKPIRentMB +'</div>';
	                        },
                            footerTemplate: function(item) {
                                var data = vm.gridKHBTS.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =numberWithCommas(item.sumOverdueKPIRentMB);
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						},{
							title : "Quá hạn KPI KC",
							field : 'overdueKPIKC',
							width : '150px',
							headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
	                        attributes: {
	                            style: "text-align:right;"
	                        },
	                        template: function(data){
	                        	var type = '4';
	                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.overdueKPIKC +'</div>';
	                        },
                            footerTemplate: function(item) {
                                var data = vm.gridKHBTS.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =numberWithCommas(item.sumOverdueKPIKC);
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						},{
							title : "Quá hạn KPI DBHT",
							field : 'overdueKPIDBHT',
							width : '150px',
							headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
	                        attributes: {
	                            style: "text-align:right;"
	                        },
	                        template: function(data){
	                        	var type = '5';
	                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.overdueKPIDBHT +'</div>';
	                        },
                            footerTemplate: function(item) {
                                var data = vm.gridKHBTS.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =numberWithCommas(item.sumOverdueKPIDBHT);
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						},{
							title : "Quá hạn KPI lên doanh thu",
							field : 'overdueKPILDT',
							width : '200px',
							headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
	                        attributes: {
	                            style: "text-align:right;"
	                        },
	                        template: function(data){
	                        	var type = '7';
	                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.overdueKPILDT +'</div>';
	                        },
                            footerTemplate: function(item) {
                                var data = vm.gridKHBTS.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =numberWithCommas(item.sumOverdueKPILDT);
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						},{
							title : "Quá hạn KPI HSHC về TTHT",
							field : 'overdueKPIHSHCTTHT',
							width : '200px',
							headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
	                        attributes: {
	                            style: "text-align:right;"
	                        },
	                        template: function(data){
	                        	var type = '8';
	                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.overdueKPIHSHCTTHT +'</div>';
	                        },
                            footerTemplate: function(item) {
                                var data = vm.gridKHBTS.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =numberWithCommas(item.sumOverdueKPIHSHCTTHT);
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						},{
							title : "Quá hạn KPI HSHC về ĐTHT",
							field : 'overdueKPIHSHCDTHT',
							width : '200px',
							headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
	                        attributes: {
	                            style: "text-align:right;"
	                        },
	                        template: function(data){
	                        	var type = '';
	                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.overdueKPIHSHCDTHT +'</div>';
	                        },
                            footerTemplate: function(item) {
                                var data = vm.gridKHBTS.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =numberWithCommas(item.sumOverdueKPIHSHCDTHT);
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						},{
							title : "Quá hạn thiết kế dự toán",
							field : 'overdueTKDT',
							width : '200px',
							headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
	                        attributes: {
	                            style: "text-align:right;"
	                        },
	                        template: function(data){
	                        	var type = '2';
	                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.overdueTKDT +'</div>';
	                        },
                            footerTemplate: function(item) {
                                var data = vm.gridKHBTS.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =numberWithCommas(item.sumOverdueTKDT);
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						}
						]
					},{
						title : "Số tiền phạt (triệu)",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [{
							title : "Thuê mặt bằng",
							field : 'rentMB',
							width : '150px',
							headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
	                        attributes: {
	                            style: "text-align:right;"
	                        },
	                        template: function(data){
	                        	var type = '1';
	                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.rentMB +'</div>';
	                        },
                            footerTemplate: function(item) {
                                var data = vm.gridKHBTS.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =numberWithCommas(item.sumRentMB);
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						},{
							title : "Khởi công",
							field : 'startUp',
							width : '150px',
							headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
	                        attributes: {
	                            style: "text-align:right;"
	                        },
	                        template: function(data){
	                        	var type = '4';
	                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.startUp +'</div>';
	                        },
                            footerTemplate: function(item) {
                                var data = vm.gridKHBTS.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =numberWithCommas(item.sumStartUp);
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						},{
							title : "Đồng bộ hạ tầng",
							field : 'startUp',
							width : '150px',
							headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
	                        attributes: {
	                            style: "text-align:right;"
	                        },
	                        template: function(data){
	                        	var type = '5';
	                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.rentDBHT +'</div>';
	                        },
                            footerTemplate: function(item) {
                                var data = vm.gridKHBTS.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =numberWithCommas(item.sumRentDBHT);
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						},{
							title : "Lên doanh thu",
							field : 'draftingRevenue',
							width : '150px',
							headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
	                        attributes: {
	                            style: "text-align:right;"
	                        },
	                        template: function(data){
	                        	var type = '7';
	                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.draftingRevenue +'</div>';
	                        },
                            footerTemplate: function(item) {
                                var data = vm.gridKHBTS.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =numberWithCommas(item.sumDraftingRevenue);
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						},{
							title : "HSHC về TTHT",
							field : 'hshcTTHT',
							width : '150px',
							headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
	                        attributes: {
	                            style: "text-align:right;"
	                        },
	                        template: function(data){
	                        	var type = '8';
	                            return '<div ng-click="vm.viewStationDetail(\''+ type+'\',\''+ data.provinceCode +'\',\''+data.monthYear+'\')">'+ data.hshcTTHT +'</div>';
	                        },
                            footerTemplate: function(item) {
                                var data = vm.gridKHBTS.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =numberWithCommas(item.sumHshcTTHT);
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						}]
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
        
        
        vm.viewWODetails = function (woId) {
//        	$(".k-icon.k-i-close").click();
//        	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
//        	$modal.dismiss();
//        	$modal.close();
//        	$scope.moadalP.Close();
            var template = Constant.getTemplateUrl('WO_XL_WO_DETAILS');
            $rootScope.viewDetailsWoId = woId;
            template.woId = woId;
            postal.publish({
                channel: "Tab",
                topic: "open",
                data: template
            });

            postal.publish({
                channel: "Tab",
                topic: "action",
                data: {action: 'refresh', woId: woId}
            });
        };

        function fillDataStationDetail1() {
            vm.gridByDetail = kendoConfig.getGridOptions({
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
                            vm.count=response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "rpBTSService/doSearchStation",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchFormS.page = options.page
                            vm.searchFormS.pageSize = options.pageSize
                            return JSON.stringify(vm.searchFormS)
                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: false,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'></div>"
                    }
                },
                columns: [
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
                        }
                    }
                    ,  {
                        title: "Tỉnh",
                        width: '120px',
                        field:"provinceCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,  {
                        title: "Mã VTNET",
                        width: '120px',
                        field:"vtnetCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },  {
                        title: "Mã VCC",
                        width: '120px',
                        field:"vccCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function(data){
                        	 
                            return '<div ng-click="vm.viewWo('+data.woTypeId+','+ data.trTypeId +','+data.catWorkItemTypeId+',\''+data.vccCode+'\','+'\'1\''+')">'+ data.vccCode +'</div>';
                        }
                    }
                    ,  {
                        title: "Thuê mặt bằng",
                        width: '120px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        columns: [
                        	{
                                title: "Kế hoạch",
                                width: '120px',
                                field:"planTMB",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Thực hiện",
                                width: '120px',
                                field:"performTMB",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Số ngày quá hạn KPI",
                                width: '150px',
                                field:"numberDateLimitKPI",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Số tiền phạt (triệu)",
                                width: '120px',
                                field:"numberFines",
                                headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            }
                        ]
                    }
                ]
            });
        }
        
        function fillDataStationDetail2() {
            vm.gridByDetail = kendoConfig.getGridOptions({
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
                            vm.count=response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "rpBTSService/doSearchStation",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                        	vm.searchFormS.page = options.page
                            vm.searchFormS.pageSize = options.pageSize
                            return JSON.stringify(vm.searchFormS)
                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: false,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'></div>"
                    }
                },
                columns: [
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
                        }
                    }
                    ,  {
                        title: "Tỉnh",
                        width: '120px',
                        field:"provinceCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,  {
                        title: "Mã VTNET",
                        width: '120px',
                        field:"vtnetCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },  {
                        title: "Mã VCC",
                        width: '120px',
                        field:"vccCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function(data){
                        	 
                            return '<div ng-click="vm.viewWo('+data.woTypeId+','+ data.trTypeId +','+data.catWorkItemTypeId+',\''+data.vccCode+'\','+'\'2\''+')">'+ data.vccCode +'</div>';
                        }
                    }
                    ,  {
                        title: "TKDT",
                        width: '120px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        columns: [
                        	{
                                title: "Kế hoạch",
                                width: '120px',
                                field:"planTKDT",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Thực hiện",
                                width: '120px',
                                field:"performTKDT",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Số ngày quá hạn KPI",
                                width: '150px',
                                field:"numberDateLimitKPI",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            }
                        ]
                    }
                ]
            });
        }
        
        function fillDataStationDetail3() {
            vm.gridByDetail = kendoConfig.getGridOptions({
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
                            vm.count=response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "rpBTSService/doSearchStation",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                        	vm.searchFormS.page = options.page
                            vm.searchFormS.pageSize = options.pageSize
                            return JSON.stringify(vm.searchFormS)
                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: false,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'></div>"
                    }
                },
                columns: [
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
                        }
                    }
                    ,  {
                        title: "Tỉnh",
                        width: '120px',
                        field:"provinceCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,  {
                        title: "Mã VTNET",
                        width: '120px',
                        field:"vtnetCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },  {
                        title: "Mã VCC",
                        width: '120px',
                        field:"vccCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function(data){
                        	 
                            return '<div ng-click="vm.viewWo('+data.woTypeId+','+ data.trTypeId +','+data.catWorkItemTypeId+',\''+data.vccCode+'\','+'\'3\''+')">'+ data.vccCode +'</div>';
                        }
                    }
                    ,  {
                        title: "Thẩm TKDT",
                        width: '120px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        columns: [
                        	{
                                title: "Kế hoạch",
                                width: '120px',
                                field:"planTKDT",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Thực hiện",
                                width: '120px',
                                field:"performTTKDT",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            }
                        ]
                    }
                ]
            });
        }
        
        function fillDataStationDetail4() {
            vm.gridByDetail = kendoConfig.getGridOptions({
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
                            vm.count=response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "rpBTSService/doSearchStation",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                        	vm.searchFormS.page = options.page
                            vm.searchFormS.pageSize = options.pageSize
                            return JSON.stringify(vm.searchFormS)
                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: false,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'></div>"
                    }
                },
                columns: [
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
                        }
                    }
                    ,  {
                        title: "Tỉnh",
                        width: '120px',
                        field:"provinceCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,  {
                        title: "Mã VTNET",
                        width: '120px',
                        field:"vtnetCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },  {
                        title: "Mã VCC",
                        width: '120px',
                        field:"vccCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function(data){
                        	 
                            return '<div ng-click="vm.viewWo('+data.woTypeId+','+ data.trTypeId +','+data.catWorkItemTypeId+',\''+data.vccCode+'\','+'\'4\''+')">'+ data.vccCode +'</div>';
                        }
                    }
                    ,  {
                        title: "Khởi công",
                        width: '120px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        columns: [
                        	{
                                title: "Kế hoạch",
                                width: '120px',
                                field:"planKC",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Thực hiện",
                                width: '120px',
                                field:"performKC",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Số ngày quá hạn KPI",
                                width: '150px',
                                field:"numberDateLimitKPI",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Số tiền phạt (triệu)",
                                width: '120px',
                                field:"numberFines",
                                headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            }
                        ]
                    }
                ]
            });
        }
        
        function fillDataStationDetail5() {
            vm.gridByDetail = kendoConfig.getGridOptions({
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
                            vm.count=response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "rpBTSService/doSearchStation",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                        	vm.searchFormS.page = options.page
                            vm.searchFormS.pageSize = options.pageSize
                            return JSON.stringify(vm.searchFormS)
                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: false,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'></div>"
                    }
                },
                columns: [
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
                        }
                    }
                    ,  {
                        title: "Tỉnh",
                        width: '120px',
                        field:"provinceCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,  {
                        title: "Mã VTNET",
                        width: '120px',
                        field:"vtnetCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },  {
                        title: "Mã VCC",
                        width: '120px',
                        field:"vccCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function(data){
                        	 
                            return '<div ng-click="vm.viewWo('+data.woTypeId+','+ data.trTypeId +','+data.catWorkItemTypeId+',\''+data.vccCode+'\','+'\'5\''+')">'+ data.vccCode +'</div>';
                        }
                    }
                    ,  {
                        title: "DBHT",
                        width: '120px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        columns: [
                        	{
                                title: "Kế hoạch",
                                width: '120px',
                                field:"planDBHT",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Thực hiện",
                                width: '120px',
                                field:"performDBHT",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Số ngày quá hạn KPI",
                                width: '150px',
                                field:"numberDateLimitKPI",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Số tiền phạt (triệu)",
                                width: '120px',
                                field:"numberFines",
                                headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            }
                        ]
                    }
                ]
            });
        }
        
        function fillDataStationDetail6() {
            vm.gridByDetail = kendoConfig.getGridOptions({
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
                            vm.count=response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "rpBTSService/doSearchStation",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                        	vm.searchFormS.page = options.page
                            vm.searchFormS.pageSize = options.pageSize
                            return JSON.stringify(vm.searchFormS)
                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: false,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'></div>"
                    }
                },
                columns: [
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
                        }
                    }
                    ,  {
                        title: "Tỉnh",
                        width: '120px',
                        field:"provinceCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,  {
                        title: "Mã VTNET",
                        width: '120px',
                        field:"vtnetCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },  {
                        title: "Mã VCC",
                        width: '120px',
                        field:"vccCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function(data){
                        	 
                            return '<div ng-click="vm.viewWo('+data.woTypeId+','+ data.trTypeId +','+data.catWorkItemTypeId+',\''+data.vccCode+'\','+'\'6\''+')">'+ data.vccCode +'</div>';
                        }
                    }
                    ,  {
                        title: "Phát sóng",
                        width: '120px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        columns: [
                        	{
                                title: "Kế hoạch",
                                width: '120px',
                                field:"planPS",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Thực hiện",
                                width: '120px',
                                field:"performPS",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            }
                        ]
                    }
                ]
            });
        }
        
        function fillDataStationDetail7() {
            vm.gridByDetail = kendoConfig.getGridOptions({
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
                            vm.count=response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "rpBTSService/doSearchStation",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                        	vm.searchFormS.page = options.page
                            vm.searchFormS.pageSize = options.pageSize
                            return JSON.stringify(vm.searchFormS)
                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: false,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'></div>"
                    }
                },
                columns: [
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
                        }
                    }
                    ,  {
                        title: "Tỉnh",
                        width: '120px',
                        field:"provinceCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,  {
                        title: "Mã VTNET",
                        width: '120px',
                        field:"vtnetCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },  {
                        title: "Mã VCC",
                        width: '120px',
                        field:"vccCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function(data){
                        	 
                            return '<div ng-click="vm.viewWo('+data.woTypeId+','+ data.trTypeId +','+data.catWorkItemTypeId+',\''+data.vccCode+'\','+'\'7\''+')">'+ data.vccCode +'</div>';
                        }
                    }
                    ,  {
                        title: "Doanh thu",
                        width: '120px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        columns: [
                        	{
                                title: "Kế hoạch",
                                width: '120px',
                                field:"planDT",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Thực hiện",
                                width: '120px',
                                field:"performDT",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Số ngày quá hạn KPI",
                                width: '150px',
                                field:"numberDateLimitKPI",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Số tiền phạt (triệu)",
                                width: '120px',
                                field:"numberFines",
                                headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            }
                        ]
                    }
                ]
            });
        }
        
        function fillDataStationDetail8() {
            vm.gridByDetail = kendoConfig.getGridOptions({
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
                            vm.count=response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "rpBTSService/doSearchStation",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                        	vm.searchFormS.page = options.page
                            vm.searchFormS.pageSize = options.pageSize
                            return JSON.stringify(vm.searchFormS)
                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: false,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'></div>"
                    }
                },
                columns: [
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
                        }
                    }
                    ,  {
                        title: "Tỉnh",
                        width: '120px',
                        field:"provinceCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,  {
                        title: "Mã VTNET",
                        width: '120px',
                        field:"vtnetCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },  {
                        title: "Mã VCC",
                        width: '120px',
                        field:"vccCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function(data){
                        	 
                            return '<div ng-click="vm.viewWo('+data.woTypeId+','+ data.trTypeId +','+data.catWorkItemTypeId+',\''+data.vccCode+'\','+'\'8\''+')">'+ data.vccCode +'</div>';
                        }
                    }
                    ,  {
                        title: "N+1 của CNKT về ĐTHT",
                        width: '120px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        columns: [
                        	{
                                title: "Kế hoạch",
                                width: '120px',
                                field:"planHSHCTTHT",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Thực hiện",
                                width: '120px',
                                field:"performHSHCTTHT",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Số ngày quá hạn KPI",
                                width: '150px',
                                field:"numberDateLimitKPI",
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            },{
                                title: "Số tiền phạt (triệu)",
                                width: '120px',
                                field:"numberFines",
                                headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                                attributes: {
                                    style: "text-align:center;"
                                }
                            }
                        ]
                    }
                ]
            });
        }
        
        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };
       /* vm.exportexcel= function(){
            function displayLoading(target) {
                var element = $(target);
                kendo.ui.progress(element, true);
                setTimeout(function(){
                    return Restangular.all("woService/exportFileTrWo").post(vm.searchForm).then(function (d) {
                        var data = d.plain();
                        window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                        kendo.ui.progress(element, false);
                    }).catch(function (e) {
                        kendo.ui.progress(element, false);
                        toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                        return;
                    });;
                });
            }
            displayLoading(".tab-content");
        }*/
        
        vm.cancelInputProvince = function() {
        	vm.searchForm.catProvince = null;
        	vm.searchForm.provinceCodeD =  null;
   	    }

        function cancelInput(){
			vm.searchForm.monthYearD=null;
        }
        vm.monthSelectorOptions = {
                start: "year",
                depth: "year"
         };
        
        function fillDataWO() {
            vm.gridWO = kendoConfig.getGridOptions({
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
                            vm.count=response.total;
                            return response.total;
                        },
                        data: function (response) {
                        	debugger
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "rpBTSService/doSearchWO",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchFormW.page = options.page
                            vm.searchFormW.pageSize = options.pageSize
                            return JSON.stringify(vm.searchFormW)
                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: false,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'></div>"
                    }
                },
                columns: [
                    {
                        title: "STT",
                        field:"stt",
                        template: function () {
                            return ++record;
                        },
                        width: '40px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },{
                    	title: "Thao tác",
                        template: dataItem =>
                        '<div class="text-center">' +
                    	'<i class="fa fa-list-alt icon-table" ng-click="vm.viewWODetails(dataItem.woId)"></i>' +
	                    '</div>',
	                    width: '80px',
	                    field: "action"
                    },{
                        title: "Mã Tr",
                        width: '120px',
                        field:"trCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },{
                        title: "Mã WO",
                        width: '120px',
                        field:"woCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },{
                        title: "Tên WO",
                        width: '120px',
                        field:"woName",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },{
                        title: "Trạng thái",
                        width: '120px',
                        field:"status",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },{
                        title: "Mã trạm",
                        width: '120px',
                        field:"stationCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },{
                        title: "Mã công trình",
                        width: '120px',
                        field:"constructionCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },{
                        title: "Hạng mục",
                        width: '120px',
                        field:"itemName",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },{
                        title: "Mã HĐ",
                        width: '120px',
                        field:"contractCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },{
                        title: "Ngày tạo",
                        width: '120px',
                        field:"createDate",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },{
                        title: "Hạn hoàn thành dự kiến",
                        width: '180px',
                        field:"finishDate",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },{
                        title: "Hạn hoàn thành thực tế",
                        width: '180px',
                        field:"approveDate",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },{
                        title: "Tên CD LV2",
                        width: '120px',
                        field:"cd2Name",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },{
                        title: "Tên FT",
                        width: '120px',
                        field:"ftName",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },{
                        title: "Giá trị",
                        width: '120px',
                        field:"cost",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                ]
            });
        }
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
        
        vm.exportexcelKHBTS= function(){
	    	vm.searchForm.page = null;
			vm.searchForm.pageSize = null;
			
			kendo.ui.progress($("#gridKHBTS"), true);
    		var obj = {};
    		obj = angular.copy(vm.searchForm);
				return Restangular.all("rpBTSService/exportexcelKHBTS").post(obj).then(function (d){
	    		  var data = d.plain();
                  window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                  kendo.ui.progress("#gridKHBTS", false);
			});
        }
        
        vm.changeTypeBc = function() {
        	if(vm.searchForm.typeBc == 2){
        		$("#gridKHBTSId").data("kendoGrid").showColumn("stationVccCode");
        		$("#gridKHBTSId").data("kendoGrid").showColumn("stationVtNetCode");
        	} else {
        		$("#gridKHBTSId").data("kendoGrid").hideColumn("stationVccCode");
        		$("#gridKHBTSId").data("kendoGrid").hideColumn("stationVtNetCode");
        	}
        	vm.doSearch();
        }
    }
})();
