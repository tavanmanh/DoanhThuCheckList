(function() {
	'use strict';
	var controllerId = 'rpEffectiveCalculateController';

	angular.module('MetronicApp').controller(controllerId,
			rpEffectiveCalculateController);

	function rpEffectiveCalculateController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, rpEffectiveCalculateService,
			CommonService, PopupConst, Restangular, RestEndpoint, Constant,
			htmlCommonService) {
		var vm = this;
		vm.showSearch = true;
		vm.showDetail = false;
		vm.searchForm = {
			type: 1
		};
		vm.String = $rootScope.stringtile;
		
		init();
		function init(){
			fillDataTableDas([]);
			fillDataTableBts([]);
		}
		
		vm.listRemove=[{
			title: "Thao tác",
		}]
		vm.listConvert=[
			{
				field:"dasType",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"cdbrType",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"engineRoomDas",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"feederAntenDas",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"costOtherDas",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"axisCdbr",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"apartmentsAllCdbr",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"apartmentsCdbr",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"costOtherCdbr",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"engineRoomCdbr",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"engineRoomCableCdbr",
				data:{
					1:'Có',
					0:'Không'
				}
			},
			{
				field:"effective",
				data:{
					1:'Có',
					0:'Không'
				}
			}
			
		]
		
		vm.showHideWorkItemColumnDas = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.rpEffectiveCalculateDasGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.rpEffectiveCalculateDasGrid.showColumn(column);
			} else {
				vm.rpEffectiveCalculateDasGrid.hideColumn(column);
			}

		}

		vm.gridColumnShowHideFilterDas = function(item) {
			return item.type == null || item.type !== 1;
		};
		
		vm.showHideWorkItemColumnBts = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.rpEffectiveCalculateBtsGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.rpEffectiveCalculateBtsGrid.showColumn(column);
			} else {
				vm.rpEffectiveCalculateBtsGrid.hideColumn(column);
			}

		}

		vm.gridColumnShowHideFilterBts = function(item) {
			return item.type == null || item.type !== 1;
		};
		
		vm.doSearch = doSearch;
		function doSearch(){
			var grid = null;
			if(vm.searchForm.type=="1"){
				grid = vm.rpEffectiveCalculateDasGrid;
			} else {
				grid = vm.rpEffectiveCalculateBtsGrid;
			}
			
			if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 10
				});
			}
		}
		
		var record = 0;
		function fillDataTableDas(data) {
			vm.rpEffectiveCalculateDasGridOptions = kendoConfig
			.getGridOptions({
				autoBind : true,
				scrollable : true,
				resizable : true,
				editable : false,
				dataBinding : function() {
					record = (this.dataSource.page() - 1)
							* this.dataSource.pageSize();
				},
				reorderable : true,
				toolbar : [ {
					name : "actions",
					template : '<div class=" pull-left ">'
							+ '</div>'
							+ '<div class="btn-group pull-right margin_top_button ">'
							+ '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: VNĐ</div>'
							+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
							+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
							+ '<label ng-repeat="column in vm.rpEffectiveCalculateDasGrid.columns.slice(1,vm.rpEffectiveCalculateDasGrid.columns.length)| filter: vm.gridColumnShowHideFilterDas">'
							+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumnDas(column)"> {{column.title}}'
							+ '</label>' + '</div></div>'
				} ],
				dataSource : {
					serverPaging : true,
					schema : {
						total : function(response) {
							$("#appRpEffectiveCalculateDas").text(
									"" + response.total);
							vm.countRpEffectiveCalculateDas = response.total;
							return response.total;
						},
						data : function(response) {
							return response.data;
						}
					},
					transport : {
						read : {
							url : Constant.BASE_SERVICE_URL
									+ "rpBTSService/doSearchEffective",
							contentType : "application/json; charset=utf-8",
							type : "POST"
						},
						parameterMap : function(options, type) {
							var obj = {};
							vm.searchForm.page = options.page;
							vm.searchForm.pageSize = options.pageSize;
							obj = angular.copy(vm.searchForm);
							obj.type = 1;
							return JSON.stringify(obj)
						}
					},
					pageSize : 10,
				},
				noRecords : true,
				columnMenu : false,
				messages : {
					noRecords : gettextCatalog
							.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
				},
				pageable : {
					refresh : false,
					pageSizes : [ 10, 15, 20, 25 ],
					messages : {
						display : "{0}-{1} của {2} kết quả",
						itemsPerPage : "kết quả/trang",
						empty : "<div style='margin:5px'>Không có kết quả hiển thị</div>"
					}
				},
				columns : [
						{
							title : "TT",
							field : "stt",
							template : function() {
								return ++record;
							},
							width : '50px',
							columnMenu : false,
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Danh mục đầu tư DAS",
							field : 'dasType',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false,
							template: function(dataItem){
								if(dataItem.dasType=="1"){
									return "Có";
								} else if(dataItem.dasType=="0"){
									return "Không";
								} else {
									return "";
								}
							}
						},
						{
							title : "Danh mục đầu tư CDBR",
							field : 'cdbrType',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false,
							template: function(dataItem){
								if(dataItem.cdbrType=="1"){
									return "Có";
								} else if(dataItem.cdbrType=="0"){
									return "Không";
								} else {
									return "";
								}
							}
						},
						{
							title : "Thông tin toàn nhà",
							field : 'houseName',
							width : '200px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Tổng diện tích",
							field : 'totalArea',
							width : '200px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Tổng căn hộ",
							field : 'totalApartment',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'number',
							editable : false,
						},
						{
							title : "Chi phí thuê phòng máy DAS",
							field : 'costDas',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false
						},
						
						{
							title : "Chi phí thuê phòng máy CĐBR",
							field : 'costEngineRoomCDBR',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Chi phí điện CĐBR",
							field : 'costCDBR',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Tỉ lệ chia sẻ doanh thu CĐBR cho chủ đầu tư",
							field : 'ratioRate',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Suất đầu tư phòng máy",
							field : 'engineRoomDas',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false,
							template: function(dataItem){
								if(dataItem.engineRoomDas=="1"){
									return "Có";
								} else if(dataItem.engineRoomDas=="0"){
									return "Không";
								} else {
									return "";
								}
							}
						},
						{
							title : "Suất đầu tư feeder/anten",
							field : 'feederAntenDas',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false,
							template: function(dataItem){
								if(dataItem.feederAntenDas=="1"){
									return "Có";
								} else if(dataItem.feederAntenDas=="0"){
									return "Không";
								} else {
									return "";
								}
							}
						},
						{
							title : "Chi phí khác (Chi phí QLDA, CP tư vấn,…)",
							field : 'costOtherDas',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false,
							template: function(dataItem){
								if(dataItem.costOtherDas=="1"){
									return "Có";
								} else if(dataItem.costOtherDas=="0"){
									return "Không";
								} else {
									return "";
								}
							}
						},
						{
							title : "Đầu tư phần trục",
							field : 'axisCdbr',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false,
							template: function(dataItem){
								if(dataItem.axisCdbr=="1"){
									return "Có";
								} else if(dataItem.axisCdbr=="0"){
									return "Không";
								} else {
									return "";
								}
							}
						},
						{
							title : "Đầu tư phần trong căn hộ ( Đầu tư toàn bộ-1 điểm đấu nối, mặt hạt)",
							field : 'apartmentsAllCdbr',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false,
							template: function(dataItem){
								if(dataItem.apartmentsAllCdbr=="1"){
									return "Có";
								} else if(dataItem.apartmentsAllCdbr=="0"){
									return "Không";
								} else {
									return "";
								}
							}
						},
						{
							title : "Đầu tư phần trong căn hộ ( Đầu tư mỗi dây cáp)",
							field : 'apartmentsCdbr',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false,
							template: function(dataItem){
								if(dataItem.apartmentsCdbr=="1"){
									return "Có";
								} else if(dataItem.apartmentsCdbr=="0"){
									return "Không";
								} else {
									return "";
								}
							}
						},
						{
							title : "Chi phí khác (Chi phí QLDA, CP tư vấn,…)",
							field : 'costOtherCdbr',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false,
							template: function(dataItem){
								if(dataItem.costOtherCdbr=="1"){
									return "Có";
								} else if(dataItem.costOtherCdbr=="0"){
									return "Không";
								} else {
									return "";
								}
							}
						},
						
						{
							title : "Chi phí phòng máy (rack, ODF + OLT)",
							field : 'engineRoomCdbr',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false,
							template: function(dataItem){
								if(dataItem.engineRoomCdbr=="1"){
									return "Có";
								} else if(dataItem.engineRoomCdbr=="0"){
									return "Không";
								} else {
									return "";
								}
							}
						},
						{
							title : "Đầu tư thang máng cáp",
							field : 'engineRoomCableCdbr',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false,
							template: function(dataItem){
								if(dataItem.engineRoomCableCdbr=="1"){
									return "Có";
								} else if(dataItem.engineRoomCableCdbr=="0"){
									return "Không";
								} else {
									return "";
								}
							}
						},
						{
							title : "Hiệu quả",
							field : 'effective',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;white-space:normal;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false,
							template: function(dataItem){
								if(dataItem.effective=="1"){
									return "Có";
								} else if(dataItem.effective=="0"){
									return "Không";
								} else {
									return "";
								}
							}
						},
						]
			});
		}
		
		function fillDataTableBts(data) {
			vm.rpEffectiveCalculateBtsGridOptions = kendoConfig
			.getGridOptions({
				autoBind : true,
				scrollable : true,
				resizable : true,
				editable : false,
				dataBinding : function() {
					record = (this.dataSource.page() - 1)
							* this.dataSource.pageSize();
				},
				reorderable : true,
				toolbar : [ {
					name : "actions",
					template : '<div class=" pull-left ">'
							+ '</div>'
							+ '<div class="btn-group pull-right margin_top_button ">'
							+ '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: VNĐ</div>'
							+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
							+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
							+ '<label ng-repeat="column in vm.rpEffectiveCalculateBtsGrid.columns.slice(1,vm.rpEffectiveCalculateBtsGrid.columns.length)| filter: vm.gridColumnShowHideFilterBts">'
							+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumnBts(column)"> {{column.title}}'
							+ '</label>' + '</div></div>'
				} ],
				dataSource : {
					serverPaging : true,
					schema : {
						total : function(response) {
							$("#appRpEffectiveCalculateBts").text(
									"" + response.total);
							vm.countRpEffectiveCalculateBts = response.total;
							return response.total;
						},
						data : function(response) {
							return response.data;
						}
					},
					transport : {
						read : {
							url : Constant.BASE_SERVICE_URL
									+ "rpBTSService/doSearchEffective",
							contentType : "application/json; charset=utf-8",
							type : "POST"
						},
						parameterMap : function(options, type) {
							var obj = {};
							vm.searchForm.page = options.page;
							vm.searchForm.pageSize = options.pageSize;
							obj = angular.copy(vm.searchForm);
							obj.type = 2;
							return JSON.stringify(obj)
						}
					},
					pageSize : 10,
				},
				noRecords : true,
				columnMenu : false,
				messages : {
					noRecords : gettextCatalog
							.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
				},
				pageable : {
					refresh : false,
					pageSizes : [ 10, 15, 20, 25 ],
					messages : {
						display : "{0}-{1} của {2} kết quả",
						itemsPerPage : "kết quả/trang",
						empty : "<div style='margin:5px'>Không có kết quả hiển thị</div>"
					}
				},
				columns : [
						{
							title : "TT",
							field : "stt",
							template : function() {
								return ++record;
							},
							width : '50px',
							columnMenu : false,
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Mã tỉnh",
							field : 'catProvinceCode',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false,
						},
						{
							title : "Tên tỉnh",
							field : 'catProvinceName',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "LAT",
							field : 'latitude',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "LONG",
							field : 'longitude',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Độ cao cột",
							field : 'hightBts',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'number',
							editable : false,
						},
						{
							title : "Loại cột",
							field : 'columnType',
							width : '200px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						////
						{
							title : "Địa hình",
							field : 'topographic',
							width : '200px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Vị trí",
							field : 'location',
							width : '200px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Địa điểm cụ thể",
							field : 'address',
							width : '200px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Loại trạm",
							field : 'typeStation',
							width : '200px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Đảm bảo số nhà mạng viettel",
							field : 'mNOViettel',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Đảm bảo số nhà mạng vina",
							field : 'mNOVina',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Đảm bảo số nhà mạng mobi",
							field : 'mNOMobi',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Triển khai nguồn",
							field : 'ourceDeployment',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Lựa chọn thời gian khấu hao",
							field : 'lectDepreciationPeriod',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Lựa chọn chào giá",
							field : 'silkEnterPrice',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Giá thuê",
							field : 'price',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Đơn giá mặt bằng",
							field : 'costMB',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Tên móng cột",
							field : 'columnFoundationItems',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Giá móng cột",
							field : 'costColumnFoundationItems',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Tên móng nhà",
							field : 'houseFoundationItems',
							width : '200px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Giá móng nhà",
							field : 'costHouseFoundationItems',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Tên thân cột",
							field : 'columnBodyCategory',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Giá thân cột",
							field : 'costColumnBodyCategory',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Tên phòng máy",
							field : 'machineRoomItems',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Giá phòng máy",
							field : 'costMachineRoomItems',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Tên tiếp địa",
							field : 'groundingItems',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Giá tiếp địa",
							field : 'costGroundingItems',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Tên kéo điện",
							field : 'electricTowingItems',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Giá kéo điện",
							field : 'costElectricTowingItems',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Tên lắp cột",
							field : 'columnMountingItem',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Giá lắp cột",
							field : 'costColumnMountingItem',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Tên lắp nhà",
							field : 'installationHouses',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Giá lắp nhà",
							field : 'costInstallationHouses',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Tên đấu điện",
							field : 'electricalItems',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Giá đầu điện",
							field : 'costElectricalItems',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Vận chuyển cơ giới",
							field : 'motorizedTransportItems',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Giá vận chuyển cơ giới",
							field : 'costMotorizedTransportItems',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Vận chuyển thủ công",
							field : 'itemManualShipping',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Giá vận chuyển thủ công",
							field : 'costItemManualShipping',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Vận chuyển",
							field : 'itemShipping',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Giá vận chuyển",
							field : 'costItemShipping',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Chi phí khác",
							field : 'costItemsOtherExpenses',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Số lượng tủ nguồn",
							field : 'owerCabinetCoolingSystem',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Số lượng rectifier",
							field : 'rectifier3000',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Số lượng batteryLithium",
							field : 'batteryLithium',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Số lượng máy phát điện dầu",
							field : 'oilGenerator',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Số lượng ATS",
							field : 'ats',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Số lượng giám sát điều khiển",
							field : 'supervisionControl',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Số lượng hệ thống phụ trợ khác",
							field : 'otherAuxiliarySystem',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Số lượng công lắp đặt hệ thống",
							field : 'publicInstallationPower',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false
						},
						{
							title : "Hiệu quả",
							field : 'effective',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:left;"
							},
							type : 'text',
							editable : false,
							template: function(dataItem){
								if(dataItem.effective=="1"){
									return "Có";
								} else if(dataItem.effective=="0"){
									return "Không";
								} else {
									return "";
								}
							}
						},
						]
			});
		}
		
		vm.provinceSearchOptions = {
		        dataTextField: "name",
		        dataValueField: "id",
				placeholder:"Nhập mã hoặc tên tỉnh",
		        select: function (e) {
		        		vm.isSelect = true;
			            var dataItem = this.dataItem(e.item.index());
			            vm.searchForm.catProvinceCode = dataItem.code;
			            vm.searchForm.catProvinceName = dataItem.name;
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
		                        name: vm.searchForm.catProvinceName,
		                        pageSize: vm.provinceSearchOptions.pageSize,
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
		        		vm.searchForm.catProvinceCode = null;
		        		vm.searchForm.catProvinceName = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.searchForm.catProvinceCode = null;
		        		vm.searchForm.catProvinceName = null;
		            }
		        }
		    }
		
		 vm.openCatProvinceSearch = openCatProvinceSearch;
		function openCatProvinceSearch(param){
			var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm tỉnh");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, param, 'ggfd', false, '75%','75%','catProvinceSearchController');
	    }
	    
		vm.onSaveCatProvinceSearch = onSaveCatProvinceSearch;
		function onSaveCatProvinceSearch(dataItem){
			vm.searchForm.catProvinceCode = dataItem.code;
			vm.searchForm.catProvinceName = dataItem.name;
	        htmlCommonService.dismissPopup();
	    };
		
	    vm.exportFile= function(){
	    	vm.searchForm.page = null;
			vm.searchForm.pageSize = null;
	    	if(vm.searchForm.type=="1"){
	    		kendo.ui.progress($("#rpEffectiveCalculateDasGrid"), true);
				var obj = {};
				obj = angular.copy(vm.searchForm);
				obj.type="1";
				rpEffectiveCalculateService.doSearchEffective(obj).then(function(d){
					CommonService.exportFile(vm.rpEffectiveCalculateDasGrid,d.data,vm.listRemove,vm.listConvert,"Báo cáo tính toán hiệu quả DAS+CDBR");
					kendo.ui.progress($("#rpEffectiveCalculateDasGrid"), false);
				});   
	    	} else {
	    		kendo.ui.progress($("#rpEffectiveCalculateBtsGrid"), true);
	    		var obj = {};
	    		obj = angular.copy(vm.searchForm);
				obj.type="2";
				rpEffectiveCalculateService.doSearchEffective(obj).then(function(d){
					CommonService.exportFile(vm.rpEffectiveCalculateBtsGrid,d.data,vm.listRemove,vm.listConvert,"Báo cáo tính toán hiệu quả BTS");
					kendo.ui.progress($("#rpEffectiveCalculateBtsGrid"), false);
				});   
	    	}

	    }
	    
		// end controller
	}
})();