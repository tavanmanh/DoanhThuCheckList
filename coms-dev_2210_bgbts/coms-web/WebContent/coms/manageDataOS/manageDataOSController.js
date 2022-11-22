(function() {
	'use strict';
	var controllerId = 'manageDataOSController';
	
	angular.module('MetronicApp').controller(controllerId, manageDataOSController);
	
	function manageDataOSController($scope, $rootScope, gettextCatalog, 
			kendoConfig, manageDataOSService,
			CommonService,htmlCommonService, Restangular, RestEndpoint, Constant, $timeout) {
		var vm = this;
		
		vm.showSearch = true;
        vm.showDetail = false;
        vm.searchForm = {};
        vm.addForm = {};
        vm.isCreate = false;
        
        vm.thongTinChungShow = false;
        vm.keHoachThiCongChiTietShow = false;
        vm.tamUngShow = false;
        vm.vatTuACapShow = false;
        vm.keHoachThiCongSanLuongShow = false;
        vm.tienDoNghiemThuShow = false;
        vm.deNghiQuyetToanShow = false;
        vm.thamDinhQuyetToanShow = false;
        vm.quyetToanshow = false;
        vm.xuatHoaDonShow = false;
        vm.thanhLyShow = false;
        vm.quyetToanNhanCongShow = false;
        
        vm.thongTinChung = false;
        vm.keHoachThiCongChiTiet = false;
        vm.tamUng = false;
        vm.vatTuACap = false;
        vm.keHoachThiCongSanLuong = false;
        vm.tienDoNghiemThu = false;
        vm.deNghiQuyetToan = false;
        vm.thamDinhQuyetToan = false;
        vm.quyetToan = false;
        vm.xuatHoaDon = false;
        vm.thanhLy = false;
        vm.quyetToanNhanCong = false;
        
        vm.constructionTypeList = [
        	{
        		code: 1,
        		name: "Công trình BTS, Costie, SWAP và các công trình nguồn đầu tư TCT ký"
        	},
        	{
        		code: 2,
        		name: "Công trình nguồn chi phí"
        	},
        	{
        		code: 3,
        		name: "Công trình Bảo dưỡng ĐH và MFĐ"
        	},
        	{
        		code: 4,
        		name: "Công trình Gpon"
        	},
        	{
        		code: 5,
        		name: "Công trình Hợp đồng 12 đầu việc"
        	},
        	{
        		code: 6,
        		name: "Công trình Ngoài Tập đoàn"
        	},
        ];
        
        vm.capitalNtdList = [
        	{
        		code: 1,
        		name: "Chi phí"
        	},
        	{
        		code: 2,
        		name: "Đầu tư"
        	},
        	{
        		code: 3,
        		name: "Ngoài tập đoàn"
        	},
        ];
        
        vm.httcTdtDataList = [
        	{
        		code: 1,
        		name: "Thuê đối tác"
        	},
        	{
        		code: 2,
        		name: "Thi công trực tiếp"
        	},
        	{
        		code: 3,
        		name: "Kẹp nách"
        	},
        ];
        
        init();
        function init(){
        	vm.checkRoleCNKT = false;
        	vm.checkRoleTTHT = false;
        	manageDataOSService.checkRoleCNKT().then(function(data){
        		if(data==1){
        			vm.checkRoleTTHT = true;
        		} else {
        			vm.checkRoleCNKT = true;
        		}
        		
        		var list = [];
            	if(vm.checkRoleCNKT){
            		list = [
                    	{
                    		code: 1,
                    		name: "Thi công"
                    	},
                    	{
                    		code: 2,
                    		name: "Lập HSHC"
                    	},
                    	{
                    		code: 3,
                    		name: "Đề nghị quyết toán"
                    	},
                    	{
                    		code: 8,
                    		name: "Vật tư A cấp và thu tiền"
                    	}
                    ];
            	} else {
            		list = [
            			{
                    		code: 1,
                    		name: "Thi công"
                    	},
                    	{
                    		code: 2,
                    		name: "Lập HSHC"
                    	},
                    	{
                    		code: 3,
                    		name: "Đề nghị quyết toán"
                    	},
                    	{
                    		code: 4,
                    		name: "Thẩm định quyết toán"
                    	},
                    	{
                    		code: 5,
                    		name: "Quyết toán CĐT"
                    	},
                    	{
                    		code: 6,
                    		name: "Xuất hóa đơn"
                    	},
                    	{
                    		code: 7,
                    		name: "Thanh lý"
                    	},
                    	{
                    		code: 8,
                    		name: "Vật tư A cấp và thu tiền"
                    	}
                    ];
            	}
            	
            	vm.statusList = list;
        	});
        	
        	fillDataTable([]);
        }
        
       
        //tatph -end 13/11/2019
        
        var record = 0;
        function fillDataTable(data){
        	kendo.ui.progress($("#manageDataOSGrid"), true);
        	vm.manageDataOSGridOptions = kendoConfig.getGridOptions({
    			autoBind: true,
    			scrollable: true, 
    			resizable: true,
    			editable: true,
    			dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
                reorderable: true,
    			toolbar: [
                          {
                              name: "actions",
                              template: 
                            	  '<div class=" pull-left ">'+
                            	  '<button class="btn btn-qlk padding-search addQLK" '+
                                  'ng-click="vm.add()" uib-tooltip="Tạo mới" translate style="width: 100px;">Tạo mới</button>'+ //CNKT
                                  '<button class="btn btn-qlk padding-search TkQLK"'+
                                  'ng-click="vm.importFile()" uib-tooltip="Import" translate>Import</button>'+
//                                  '<button class="btn btn-qlk padding-search TkQLK" ng-show="vm.checkRoleCNKT"'+
//                                  'ng-click="vm.importConstruction()" uib-tooltip="Import công trình hợp đồng" translate style="width: 150px;">Import Công trình</button>'+ //CNKT
//                                  '<button class="btn btn-qlk padding-search TkQLK" ng-show="vm.checkRoleCNKT"'+
//                                  'ng-click="vm.importSchedule()" uib-tooltip="Import lập tiến độ" translate style="width: 130px;">Import Lập TĐ</button>'+ //CNKT
//                                  '<button class="btn btn-qlk padding-search TkQLK" ng-show="vm.checkRoleCNKT"'+
//                                  'ng-click="vm.importSettlementProposal()" uib-tooltip="Import lập đề nghị quyết toán" translate style="width: 150px;">Import Lập ĐNQT</button>'+ //CNKT
//                                  '<button class="btn btn-qlk padding-search TkQLK" ng-show="vm.checkRoleTTHT"'+
//                                  'ng-click="vm.importExpertiseProposal()" uib-tooltip="Import thẩm định quyết toán" translate style="width: 170px;">Import Thẩm định QT</button>'+ //TTHT
//                                  '<button class="btn btn-qlk padding-search TkQLK" ng-show="vm.checkRoleTTHT"'+
//                                  'ng-click="vm.importProposalCDT()" uib-tooltip="Import quyết toán với CĐT" translate style="width: 150px;">Import QT với CĐT</button>'+ //TTHT
//                                  '<button class="btn btn-qlk padding-search TkQLK" ng-show="vm.checkRoleTTHT"'+
//                                  'ng-click="vm.importExportInvoice()" uib-tooltip="Import Xuất hóa đơn" translate style="width: 170px;">Import Xuất hóa đơn</button>'+ //TTHT
//                                  '<button class="btn btn-qlk padding-search TkQLK" ng-show="vm.checkRoleTTHT"'+
//                                  'ng-click="vm.importLiquidation()" uib-tooltip="Import Thanh lý hợp đồng" translate style="width: 150px;">Import Thanh lý</button>'+ //TTHT
//                                  '<button class="btn btn-qlk padding-search TkQLK" ng-show="vm.checkRoleCNKT"'+
//                                  'ng-click="vm.importProposalLabor()" uib-tooltip="Import Quyết toán nhân công" translate style="width: 170px;">Import QT nhân công</button>'+ //CNKT
                					'</div>'
                                 +
                              '<div class="btn-group pull-right margin_top_button margin_right10">'+
                              '<i data-toggle="dropdown" tooltip-placement="left" uib-tooltip="Cài đặt" aria-expanded="false"><i class="fa fa-cog" aria-hidden="true"></i></i>'+
                              '<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="vm.exportFile()" aria-hidden="true"></i>' +
                              '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
                              '<label ng-repeat="column in vm.manageDataOSGrid.columns| filter: vm.gridColumnShowHideFilter">'+
                              '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+
                              '</label>'+
                              '</div></div>'
                          }
                          ],
    			dataSource:{
    				serverPaging: true,
    				 schema: {
    					 total: function (response) {
    						    $("#countQuantity").text(""+response.total);
    							return response.total; 
    						},
    						data: function (response) {	
    						kendo.ui.progress($("#manageDataOSGrid"), false);
    						return response.data; 
    						},
    	                },
    				transport: {
    					read: {
    	                        // Thuc hien viec goi service
    						url: Constant.BASE_SERVICE_URL + "manageDataOutsideOsService/doSearchOS",
    						contentType: "application/json; charset=utf-8",
    						type: "POST"
    					},					
    					parameterMap: function (options, type) {
    						    vm.searchForm.page = options.page
    							vm.searchForm.pageSize = options.pageSize
    							vm.searchForm.listStatus = null;
    							return JSON.stringify(vm.searchForm)
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
    	                itemsPerPage: "<span translate>kết quả/trang</span>",
    	                empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
    	            }
    			},
    			columns: [{
    				title: "TT",
    				field:"stt",
    				width: '50px',
    		        template: function () {
    				  return ++record;
    				 },
    		       
    		        columnMenu: false,
    		        headerAttributes: {
    					style: "text-align:center;"
    				},
    				attributes: {
    					style: "text-align:center;"
    				},
    			},
    			////////////////////////
    			{
					title: "Thao tác",
					headerAttributes: {
			        	style: "text-align:center; font-weight: bold;",
						translate:"",
					},
			        template: dataItem =>
					'<div class="text-center">'
					+'<button style=" border: none; background-color: white;" id="updateId" ng-click="vm.edit(dataItem)" class=" icon_table "'+
					'   uib-tooltip="Sửa" translate>'+
					'<i class="fa fa-pencil" aria-hidden="true"></i>'+
					'</div>',
			        width: '150px',
			        field:"action"
				},
    			///////////////////////
    			{
    				title: "Kế hoạch chi tiết tháng",
    		        width: '400px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:left;white-space: normal;"
    				},
    				columns: [
    					{
    	    				title: "Hợp đồng",
    	    		        width: '400px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;white-space: normal;"
    	    				},
    	    				columns: [
    	    					{
    	    	    				title: "Ngày ký",
    	    	    		        field: 'hdSignDate',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Số hợp đồng",
    	    	    		        field: 'hdContractCode',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Giá trị (triệu VNĐ)",
    	    	    		        field: 'hdContractValue',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				format: "{0:n0}"
    	    	    			},
    	    	    			{
    	    	    				title: "Thời gian thực hiện (ngày)",
    	    	    		        field: 'hdPerformDay',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				format: "{0:n0}"
    	    	    			},
    	    				]
    	    			},
    	    			{
    	    				title: "Thông tin chung",
    	    		        width: '400px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    				columns: [
    	    					{
    	    	    				title: "Tỉnh",
    	    	    		        field: 'provinceCode',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Mã công trình",
    	    	    		        field: 'constructionCode',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Mã trạm tuyến",
    	    	    		        field: 'stationCode',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Loại công trình",
    	    	    		        field: 'constructionTypeName',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
//    	    	    				template: function(dataItem){
//    	    	    					if(dataItem.constructionType==null){
//	    	    							dataItem.constructionType = "";
//	    	    						} else {
//	    	    							for(var i=0;i<vm.constructionTypeList.length;i++){
//	    	    	    						if(vm.constructionTypeList[i].code == dataItem.constructionType){
//	    	    	    							dataItem.constructionType = vm.constructionTypeList[i].name;
//	    	    	    							break;
//	    	    	    						}
//	    	    	    					}
//	    	    						}
//    	    	    					return dataItem.constructionType;
//    	    	    				}
    	    	    			},
    	    	    			{
    	    	    				title: "Nội dung",
    	    	    		        field: 'content',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Nguồn vốn",
    	    	    		        field: 'capitalNtdName',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    				]
    	    			},
    	    			
    	    			{
    	    				title: "Kế hoạch thi công (triệu VNĐ)",
    	    		        width: '500px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    				columns: [
    	    					{
    	    	    				title: "Lương",
    	    	    		        field: 'khtcSalary',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				format: "{0:n0}"
    	    	    			},
    	    	    			{
    	    	    				title: "Nhân công thuê ngoài",
    	    	    		        field: 'khtcLaborOutsource',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				format: "{0:n0}"
    	    	    			},
    	    	    			{
    	    	    				title: "Chi phí vật liệu",
    	    	    		        field: 'khtcCostMaterial',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				format: "{0:n0}"
    	    	    			},
    	    	    			{
    	    	    				title: "Chi phí HSHC",
    	    	    		        field: 'khtcCostHshc',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				format: "{0:n0}"
    	    	    			},
    	    	    			{
    	    	    				title: "Chi phí vận chuyển",
    	    	    		        field: 'khtcCostTransport',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				format: "{0:n0}",
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Chi phí khác",
    	    	    		        field: 'khtcCostOrther',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				format: "{0:n0}"
    	    	    			},
    	    	    			{
    	    	    				title: "Tháng triển khai",
    	    	    		        field: 'khtcDeploymentMonth',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Tổng cộng",
    	    	    		        field: 'khtcTotalMoney',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				format: "{0:n0}"
    	    	    			},
    	    	    			{
    	    	    				title: "Hiệu quả (%)",
    	    	    		        field: 'khtcEffective',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Ghi chú",
    	    	    		        field: 'khtcDescription',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				format: "{0:n0}"
    	    	    			},
    	    				]
    	    			},
    				]
    			},
    			/////////////////////
    			{
    				title: "Giá trị sản lượng",
    		        width: '400px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:left;white-space: normal;"
    				},
    				columns: [
    					{
    	    				title: "Tạm ứng kinh phí (triệu VNĐ)",
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    				columns: [
    	    					{
    	    	    				title: "Ngày tạm ứng",
    	    	    		        field: 'tuAdvanceDate',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Nội dung vướng",
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				columns: [
    	    	    					{
    	    	    	    				title: "Nhân công",
    	    	    	    		        field: 'tuLabor',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    	    			{
    	    	    	    				title: "Vật liệu",
    	    	    	    		        field: 'tuMaterial',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    	    			{
    	    	    	    				title: "Hồ sơ hoàn công",
    	    	    	    		        field: 'tuHshc',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    	    			{
    	    	    	    				title: "Chi phí vận chuyển",
    	    	    	    		        field: 'tuCostTransport',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    	    			{
    	    	    	    				title: "Chi phí khác",
    	    	    	    		        field: 'tuCostOrther',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    				]
    	    	    			},
    	    				]
    	    			},
    	    			{
    	    				title: "Vật tư A cấp (triệu VNĐ)",
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    				columns: [
    	    					{
    	    	    				title: "Ngày nhận đồng bộ vật tư",
    	    	    		        field: 'vtaSynchronizeDate',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Giá trị",
    	    	    		        field: 'vtaValue',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    				]
    	    			},
    	    			{
    	    				title: "Kế hoạch thi công",
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    				columns: [
    	    					{
    	    	    				title: "Giá trị sản lượng (triệu VNĐ)",
    	    	    		        field: 'gtslQuantityValue',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Hình thức thi công",
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				columns: [
    	    	    					{
    	    	    	    				title: "Thuê đối tác",
    	    	    	    		        field: 'httcTdt',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    	    			{
    	    	    	    				title: "Thi công trực tiếp",
    	    	    	    		        field: 'httcTctt',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    	    			{
    	    	    	    				title: "Kẹp nách",
    	    	    	    		        field: 'httcKn',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    				]
    	    	    			},
    	    	    			{
    	    	    				title: "Tình trạng thi công",
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				columns: [
    	    	    					{
    	    	    	    				title: "Ngày bắt đầu",
    	    	    	    		        field: 'tttcStartDate',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    	    			{
    	    	    	    				title: "Ngày kết thúc",
    	    	    	    		        field: 'tttcEndDate',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    	    			{
    	    	    	    				title: "Vướng",
    	    	    	    		        field: 'tttcVuong',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    	    			{
    	    	    	    				title: "Hủy",
    	    	    	    		        field: 'tttcClose',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    				]
    	    	    			},
    	    	    			{
    	    	    				title: "Ngày dự kiến hoàn thành",
    	    	    		        field: 'gtslCompleteExpectedDate',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Ghi chú",
    	    	    		        field: 'gtslDescription',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    				]
    	    			},
    				]
    			},
    			////////////////////
    			{
    				title: "Tiến độ",
    		        width: '150px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    				columns: [
    					{
    	    				title: "Tiến độ nghiệm thu hoàn công, quyết toán và dự kiến hoàn thành",
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    				columns: [
    	    					{
    	    	    				title: "Ngày bắt đầu dựng HSHC",
    	    	    		        field: 'tdntHshcStartDate',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Ngày bắt đầu nghiệm thu",
    	    	    		        field: 'tdntAcceptanceStartDate',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Ngày gửi P.KTHT thẩm định",
    	    	    		        field: 'tdntKthtExpertiseDate',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Ngày bắt đầu làm đối soát 4A",
    	    	    		        field: 'tdnt4AControlStartDate',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Ngày trình ký giám đốc tỉnh",
    	    	    		        field: 'tdntSignProvinceDate',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Ngày gửi hồ sơ lên TCT",
    	    	    		        field: 'tdntSendTctDate',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Ngày dự kiến hoàn thành",
    	    	    		        field: 'tdntCompleteExpectedDate',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Vướng",
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				columns: [
    	    	    					{
    	    	    	    				title: "Ngày vướng",
    	    	    	    		        field: 'tdntVuongDate',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    	    			{
    	    	    	    				title: "Nguyên nhân vướng",
    	    	    	    		        field: 'tdntVuongReason',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    				]
    	    	    			},
    	    				]
    	    			},
    				]
    			},
    			////////////////////////
    			{
    				title: "Đề nghị quyết toán",
    		        width: '150px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    				columns: [
    					{
    	    				title: "TTKT đề nghị quyết toán",
    	    		        width: '800px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    				columns: [
    	    					{
    	    	    				title: "Giá trị quyết toán CĐT (chưa VAT)",
    	    	    		        field: 'dnqtQtCdtNotVat',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Giá trị quyết toán CĐT (có VAT)",
    	    	    		        field: 'dnqtQtCdtVat',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Chi phí nhân công",
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				columns: [
    	    	    					{
    	    	    	    				title: "Thủ tục điện lực (công trình GPON)",
    	    	    	    		        field: 'dnqtElectricalProcedures',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    	    			{
    	    	    	    				title: "Nhân công kéo cáp/Nhân công (công trình GPON)",
    	    	    	    		        field: 'dnqtPullCableLabor',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    				]
    	    	    			},
    	    	    			
    	    	    			{
    	    	    				title: "Chi phí vật liệu",
    	    	    		        field: 'dnqtCostMaterial',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Chi phí HSHC",
    	    	    		        field: 'dnqtCostHshc',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Chi phí vận chuyển kho bãi",
    	    	    		        field: 'dnqtCostTransportWarehouse',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Chi phí khác",
    	    	    		        field: 'dnqtCostOrther',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Chi phí lương",
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				columns: [
    	    	    					{
    	    	    	    				title: "Lương kéo cáp/Lương khác (công trình GPON)",
    	    	    	    		        field: 'dnqtSalaryCableOrther',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    	    			{
    	    	    	    				title: "Lương hàn nối",
    	    	    	    		        field: 'dnqtWeldingSalary',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    				]
    	    	    			},
    	    	    			{
    	    	    				title: "VAT",
    	    	    		        field: 'dnqtVat',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Tổng",
    	    	    		        field: 'dnqtTotalMoney',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    				]
    	    			},
    				]
    			},
    			////////////////////////
    			{
    				title: "Giá trị quyết toán sau thẩm định",
    		        width: '150px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    				columns: [
    					{
    	    				title: "Giá trị thẩm định chi phí cho CNKT tỉnh",
    	    		        width: '1500px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    				columns: [
    	    					{
    	    	    				title: "Ngày nhận HSHC bản cứng",
    	    	    		        field: 'gttdHshcHardDate',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Ngày thẩm định xong",
    	    	    		        field: 'gttdCompleteExpertiseDate',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Chi phí nhân công",
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				columns: [
    	    	    					{
    	    	    	    				title: "Thủ tục điện lực (công trình GPON)",
    	    	    	    		        field: 'gttdCompleteExpertiseDate',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    	    			{
    	    	    	    				title: "Nhân công kéo cáp/Nhân công (công trình GPON)",
    	    	    	    		        field: 'gttdPullCableLabor',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    				]
    	    	    			},
    	    	    			{
    	    	    				title: "Chi phí vật liệu",
    	    	    		        field: 'gttdCostMaterial',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Chi phí HSHC",
    	    	    		        field: 'gttdCostHshc',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Chi phí vận chuyển kho bãi",
    	    	    		        field: 'gttdCostTransportWarehouse',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Chi phí khác",
    	    	    		        field: 'gttdCostOrther',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Chi phí lương",
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				columns: [
    	    	    					{
    	    	    	    				title: "Lương kéo cáp/lương khác (công trình GPON)",
    	    	    	    		        field: 'gttdSalaryCableOrther',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    	    			{
    	    	    	    				title: "Lương hàn nối",
    	    	    	    		        field: 'gttdWeldingSalary',
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    			},
    	    	    				]
    	    	    			},
    	    	    			{
    	    	    				title: "VAT",
    	    	    		        field: 'gttdVat',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Tổng",
    	    	    		        field: 'gttdTotalMoney',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Giá trị thẩm định của PTK (chưa VAT)",
    	    	    		        field: 'gttdGttdPtk',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Tháng ghi nhận HSHC",
    	    	    		        field: 'gttdHshcMonth',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Tháng ghi nhận quỹ lương",
    	    	    		        field: 'gttdSalaryMonth',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Lương thực nhận",
    	    	    		        field: 'gttdSalaryReal',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Lỗi HSHC",
    	    	    		        field: 'gttdHshcError',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Nguyên nhân lỗi",
    	    	    		        field: 'gttdErrorReason',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    				]
    	    			},
    				]
    			},
    			////////////////////////
    			{
    				title: "Hóa đơn",
    		        width: '150px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    				columns: [
    					{
    	    				title: "Quyết toán",
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    				columns: [
    	    					{
    	    	    				title: "Hồ sơ quyết toán VTNet",
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    				columns: [
    	    	    					
    	    	    					{
    	    	    	    				title: "Lập đề nghị quyết toán VTNet",
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    				columns: [
    	    	    	    					{
    	    	    	    	    				title: "Ngày lập đề nghị",
    	    	    	    	    		        field: 'qtdnSuggestionsDate',
    	    	    	    	    		        width: '150px',
    	    	    	    	    		        headerAttributes: {
    	    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    	    					translate:"",
    	    	    	    	    				},
    	    	    	    	    				attributes: {
    	    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    	    				},
    	    	    	    	    			},
    	    	    	    	    			{
    	    	    	    	    				title: "Giá trị",
    	    	    	    	    		        field: 'qtdnValue',
    	    	    	    	    		        width: '150px',
    	    	    	    	    		        headerAttributes: {
    	    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    	    					translate:"",
    	    	    	    	    				},
    	    	    	    	    				attributes: {
    	    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    	    				},
    	    	    	    	    			},
    	    	    	    	    			{
    	    	    	    	    				title: "Ngày nộp VTNet",
    	    	    	    	    		        field: 'qtdnVtnetDate',
    	    	    	    	    		        width: '150px',
    	    	    	    	    		        headerAttributes: {
    	    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    	    					translate:"",
    	    	    	    	    				},
    	    	    	    	    				attributes: {
    	    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    	    				},
    	    	    	    	    			},
    	    	    	    	    			{
    	    	    	    	    				title: "Ghi chú",
    	    	    	    	    		        field: 'qtdnDescription',
    	    	    	    	    		        width: '150px',
    	    	    	    	    		        headerAttributes: {
    	    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    	    					translate:"",
    	    	    	    	    				},
    	    	    	    	    				attributes: {
    	    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    	    				},
    	    	    	    	    			},
    	    	    	    				]
    	    	    	    			},
    	    	    	    			{
    	    	    	    				title: "Thẩm định quyết toán VTNet",
    	    	    	    		        width: '150px',
    	    	    	    		        headerAttributes: {
    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    					translate:"",
    	    	    	    				},
    	    	    	    				attributes: {
    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    				},
    	    	    	    				columns: [
    	    	    	    					{
    	    	    	    	    				title: "Nhân viên thẩm định",
    	    	    	    	    		        field: 'qttdExpertiseEmployee',
    	    	    	    	    		        width: '150px',
    	    	    	    	    		        headerAttributes: {
    	    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    	    					translate:"",
    	    	    	    	    				},
    	    	    	    	    				attributes: {
    	    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    	    				},
    	    	    	    	    			},
    	    	    	    	    			{
    	    	    	    	    				title: "Ngày thẩm định xong",
    	    	    	    	    		        field: 'qttdExpertiseCompleteDate',
    	    	    	    	    		        width: '150px',
    	    	    	    	    		        headerAttributes: {
    	    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    	    					translate:"",
    	    	    	    	    				},
    	    	    	    	    				attributes: {
    	    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    	    				},
    	    	    	    	    			},
    	    	    	    	    			{
    	    	    	    	    				title: "Giá trị",
    	    	    	    	    		        field: 'qttdValue',
    	    	    	    	    		        width: '150px',
    	    	    	    	    		        headerAttributes: {
    	    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    	    					translate:"",
    	    	    	    	    				},
    	    	    	    	    				attributes: {
    	    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    	    				},
    	    	    	    	    			},
    	    	    	    	    			{
    	    	    	    	    				title: "Ghi chú",
    	    	    	    	    		        field: 'qttdDescription',
    	    	    	    	    		        width: '150px',
    	    	    	    	    		        headerAttributes: {
    	    	    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    	    	    					translate:"",
    	    	    	    	    				},
    	    	    	    	    				attributes: {
    	    	    	    	    					style: "text-align:right;white-space: normal;"
    	    	    	    	    				},
    	    	    	    	    			},
    	    	    	    				]
    	    	    	    			},
    	    	    				]
    	    	    			},
    	    				]
    	    			},
    	    			{
    	    				title: "Xuất hóa đơn",
    	    		        field: 'gttdHshcError',
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    				columns: [
    	    					{
    	    	    				title: "Ngày chuyển PTC",
    	    	    		        field: 'xhdPtcDate',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Ngày xuất hóa đơn",
    	    	    		        field: 'xhdXhdDate',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Số hóa đơn",
    	    	    		        field: 'xhdSoHd',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Tháng chốt doanh thu",
    	    	    		        field: 'xhdRevenueMonth',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    	    			{
    	    	    				title: "Ghi chú",
    	    	    		        field: 'xhdDescription',
    	    	    		        width: '150px',
    	    	    		        headerAttributes: {
    	    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    	    					translate:"",
    	    	    				},
    	    	    				attributes: {
    	    	    					style: "text-align:right;white-space: normal;"
    	    	    				},
    	    	    			},
    	    				]
    	    			},
    				]
    			},
    			////////////////////////
    			{
    				title: "Thanh lý",
    		        width: '150px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    				columns: [
    					{
    	    				title: "Ngày ký thanh lý",
    	    		        field: 'tlSignDate',
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    			},
    	    			{
    	    				title: "Giá trị",
    	    		        field: 'tlValue',
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    			},
    	    			{
    	    				title: "Ghi chú",
    	    		        field: 'tlDescription',
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    			},
    	    			{
    	    				title: "Chênh lệch sản lượng, thanh lý",
    	    		        field: 'tlDifferenceQuantity',
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    			},
    	    			{
    	    				title: "Tỷ lệ (%)",
    	    		        field: 'tlRate',
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    			},
    				]
    			},
    			////////////////////////
    			{
    				title: "Quyết toán nhân công, vật tư A cấp",
    		        width: '150px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    				columns: [
    					{
    	    				title: "Ngày chuyển hồ sơ lên PHT",
    	    		        field: 'qtncPhtDate',
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    			},
    	    			{
    	    				title: "Ngày chuyển hồ sơ lên PTC",
    	    		        field: 'qtncPtcDate',
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    			},
    	    			{
    	    				title: "Ngày hạch toán nhân công và vật tư A cấp",
    	    		        field: 'qtncVtaAccountDate',
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    			},
    	    			{
    	    				title: "Ngày nhận tiền",
    	    		        field: 'qtncTakeMoneyDate',
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    			},
    	    			{
    	    				title: "Vướng mắc",
    	    		        field: 'qtncVuong',
    	    		        width: '150px',
    	    		        headerAttributes: {
    	    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    	    					translate:"",
    	    				},
    	    				attributes: {
    	    					style: "text-align:right;white-space: normal;"
    	    				},
    	    			},
    				]
    			},
    			////////////////////////
    			{
    				title: "Trạng thái",
    		        field: 'statusName',
    		        width: '150px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    			},
    			////////////////////////
    			{
    				title: "Ghi chú",
    		        field: 'description',
    		        width: '150px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    			},
    			////////////////////////
    			]
    		});
        }
        
        vm.resetFormAdd = function(data){
        	if(data=='constructionCode'){
        		vm.addForm.hdSignDate = null;
	            vm.addForm.hdContractCode = null;
	            vm.addForm.hdContractValue = null;
	           	vm.addForm.hdPerformDay = null;
	           	vm.addForm.provinceCode = null;
	           	vm.addForm.constructionCode = null;
	           	vm.addForm.stationCode = null;
	           	vm.addForm.vtaSynchronizeDate = null;
	           	vm.addForm.vtaValue = null;
        	}
        	
        	if(data=='constructionType'){
        		vm.addForm.constructionType = null;
        	}
        	
        	if(data=='capitalNtd'){
        		vm.addForm.capitalNtd = null;
        	}
        }
        
        vm.cancel = function(){
        	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }
        
        vm.doSearch = doSearch;
        function doSearch(){
        	var grid = vm.manageDataOSGrid;
        	if(grid){
        		grid.dataSource.query({
    				page: 1,
    				pageSize: 10
    			});
        	}
        }
        
        function disableForm(dataItem, data){
        	if(data=='add'){
        		//show hide
        		vm.thongTinChungShow = true;
                vm.keHoachThiCongChiTietShow = true;
                vm.tamUngShow = true;
                vm.vatTuACapShow = true;
                vm.keHoachThiCongSanLuongShow = true;
                vm.tienDoNghiemThuShow = false;
                vm.deNghiQuyetToanShow = false;
                vm.thamDinhQuyetToanShow = false;
                vm.quyetToanshow = false;
                vm.xuatHoaDonShow = false;
                vm.thanhLyShow = false;
                vm.quyetToanNhanCongShow = false;
        		//disabled
        		vm.thongTinChung = false;
            	vm.keHoachThiCongChiTiet = false;
            	vm.tamUng = false;
            	vm.vatTuACap = false;
            	vm.keHoachThiCongSanLuong = false;
        	} else {
        		if(dataItem.status==1){ //Sửa thi công
        			//show hide
        			vm.thongTinChungShow = true;
                    vm.keHoachThiCongChiTietShow = true;
                    vm.tamUngShow = true;
                    vm.vatTuACapShow = true;
                    vm.keHoachThiCongSanLuongShow = true;
                    vm.tienDoNghiemThuShow = false;
                    vm.deNghiQuyetToanShow = false;
                    vm.thamDinhQuyetToanShow = false;
                    vm.quyetToanshow = false;
                    vm.xuatHoaDonShow = false;
                    vm.thanhLyShow = false;
                    vm.quyetToanNhanCongShow = false;
        			//disable
                    vm.thongTinChung = false;
                    vm.keHoachThiCongChiTiet = false;
                    vm.tamUng = false;
                    vm.vatTuACap = false;
                    vm.keHoachThiCongSanLuong = false;
                } else if(dataItem.status==2){ //Sửa lập tiến độ
                	//show hide
        			vm.thongTinChungShow = true;
                    vm.keHoachThiCongChiTietShow = true;
                    vm.tamUngShow = true;
                    vm.vatTuACapShow = true;
                    vm.keHoachThiCongSanLuongShow = true;
                    vm.tienDoNghiemThuShow = true;
                    vm.deNghiQuyetToanShow = false;
                    vm.thamDinhQuyetToanShow = false;
                    vm.quyetToanshow = false;
                    vm.xuatHoaDonShow = false;
                    vm.thanhLyShow = false;
                    vm.quyetToanNhanCongShow = false;
        			//disable
                	vm.thongTinChung = true;
                	vm.keHoachThiCongChiTiet = true;
                	vm.tamUng = true;
                	vm.vatTuACap = true;
                	vm.keHoachThiCongSanLuong = true;
                	vm.tienDoNghiemThu = false;
                } else if(dataItem.status==3) { //Sửa đề nghị quyết toán
                	//show hide
        			vm.thongTinChungShow = true;
                    vm.keHoachThiCongChiTietShow = true;
                    vm.tamUngShow = true;
                    vm.vatTuACapShow = true;
                    vm.keHoachThiCongSanLuongShow = true;
                    vm.tienDoNghiemThuShow = true;
                    vm.deNghiQuyetToanShow = true;
                    vm.thamDinhQuyetToanShow = false;
                    vm.quyetToanshow = false;
                    vm.xuatHoaDonShow = false;
                    vm.thanhLyShow = false;
                    vm.quyetToanNhanCongShow = false;
        			//disable
                	vm.thongTinChung = true;
                	vm.keHoachThiCongChiTiet = true;
                	vm.tamUng = true;
                	vm.vatTuACap = true;
                	vm.keHoachThiCongSanLuong = true;
                	vm.tienDoNghiemThu = true;
                	vm.deNghiQuyetToan = false;
                } else if(dataItem.status==4) { //sửa thẩm định quyết toán
                	//show hide
        			vm.thongTinChungShow = true;
                    vm.keHoachThiCongChiTietShow = true;
                    vm.tamUngShow = true;
                    vm.vatTuACapShow = true;
                    vm.keHoachThiCongSanLuongShow = true;
                    vm.tienDoNghiemThuShow = true;
                    vm.deNghiQuyetToanShow = true;
                    vm.thamDinhQuyetToanShow = true;
                    vm.quyetToanshow = false;
                    vm.xuatHoaDonShow = false;
                    vm.thanhLyShow = false;
                    vm.quyetToanNhanCongShow = false;
        			//disable
                	vm.thongTinChung = true;
                	vm.keHoachThiCongChiTiet = true;
                	vm.tamUng = true;
                	vm.vatTuACap = true;
                	vm.keHoachThiCongSanLuong = true;
                	vm.tienDoNghiemThu = true;
                	vm.deNghiQuyetToan = true;
                	vm.thamDinhQuyetToan = false;
                } else if(dataItem.status==5){ //sửa quyết toán CĐT
                	//show hide
        			vm.thongTinChungShow = true;
                    vm.keHoachThiCongChiTietShow = true;
                    vm.tamUngShow = true;
                    vm.vatTuACapShow = true;
                    vm.keHoachThiCongSanLuongShow = true;
                    vm.tienDoNghiemThuShow = true;
                    vm.deNghiQuyetToanShow = true;
                    vm.thamDinhQuyetToanShow = true;
                    vm.quyetToanshow = true;
                    vm.xuatHoaDonShow = false;
                    vm.thanhLyShow = false;
                    vm.quyetToanNhanCongShow = false;
        			//disable
                	vm.thongTinChung = true;
                	vm.keHoachThiCongChiTiet = true;
                	vm.tamUng = true;
                	vm.vatTuACap = true;
                	vm.keHoachThiCongSanLuong = true;
                	vm.tienDoNghiemThu = true;
                	vm.deNghiQuyetToan = true;
                	vm.thamDinhQuyetToan = true;
                	vm.quyetToan = false;
                } else if(dataItem.status==6){ //sửa Xuất hóa đơn
                	//show hide
        			vm.thongTinChungShow = true;
                    vm.keHoachThiCongChiTietShow = true;
                    vm.tamUngShow = true;
                    vm.vatTuACapShow = true;
                    vm.keHoachThiCongSanLuongShow = true;
                    vm.tienDoNghiemThuShow = true;
                    vm.deNghiQuyetToanShow = true;
                    vm.thamDinhQuyetToanShow = true;
                    vm.quyetToanshow = true;
                    vm.xuatHoaDonShow = true;
                    vm.thanhLyShow = false;
                    vm.quyetToanNhanCongShow = false;
        			//disable
                	vm.thongTinChung = true;
                	vm.keHoachThiCongChiTiet = true;
                	vm.tamUng = true;
                	vm.vatTuACap = true;
                	vm.keHoachThiCongSanLuong = true;
                	vm.tienDoNghiemThu = true;
                	vm.deNghiQuyetToan = true;
                	vm.thamDinhQuyetToan = true;
                	vm.quyetToan = true;
                	vm.xuatHoaDon = false;
                } else if(dataItem.status==7){ //sửa Thanh lý
                	//show hide
        			vm.thongTinChungShow = true;
                    vm.keHoachThiCongChiTietShow = true;
                    vm.tamUngShow = true;
                    vm.vatTuACapShow = true;
                    vm.keHoachThiCongSanLuongShow = true;
                    vm.tienDoNghiemThuShow = true;
                    vm.deNghiQuyetToanShow = true;
                    vm.thamDinhQuyetToanShow = true;
                    vm.quyetToanshow = true;
                    vm.xuatHoaDonShow = true;
                    vm.thanhLyShow = true;
                    vm.quyetToanNhanCongShow = false;
        			//disable
                	vm.thongTinChung = true;
                	vm.keHoachThiCongChiTiet = true;
                	vm.tamUng = true;
                	vm.vatTuACap = true;
                	vm.keHoachThiCongSanLuong = true;
                	vm.tienDoNghiemThu = true;
                	vm.deNghiQuyetToan = true;
                	vm.thamDinhQuyetToan = true;
                	vm.quyetToan = true;
                	vm.xuatHoaDon = true;
                	vm.thanhLy = false;
                } else if(dataItem.status==8){ //sửa Thanh lý
                	//show hide
        			vm.thongTinChungShow = true;
                    vm.keHoachThiCongChiTietShow = true;
                    vm.tamUngShow = true;
                    vm.vatTuACapShow = true;
                    vm.keHoachThiCongSanLuongShow = true;
                    vm.tienDoNghiemThuShow = true;
                    vm.deNghiQuyetToanShow = true;
                    vm.thamDinhQuyetToanShow = true;
                    vm.quyetToanshow = true;
                    vm.xuatHoaDonShow = true;
                    vm.thanhLyShow = true;
                    vm.quyetToanNhanCongShow = true;
        			//disable
                	vm.thongTinChung = true;
                	vm.keHoachThiCongChiTiet = true;
                	vm.tamUng = true;
                	vm.vatTuACap = true;
                	vm.keHoachThiCongSanLuong = true;
                	vm.tienDoNghiemThu = true;
                	vm.deNghiQuyetToan = true;
                	vm.thamDinhQuyetToan = true;
                	vm.quyetToan = true;
                	vm.xuatHoaDon = true;
                	vm.thanhLy = true;
                	vm.quyetToanNhanCong = false;
                }
        	}
        }
        
        //-----------Tạo mới công trình hợp đồng-------------//
        vm.add = function(){
        	vm.isCreate = true;
        	vm.addForm = {};
        	vm.checkStatus = false;
            var teamplateUrl = "coms/manageDataOS/popupAddNew.html";
            var title = "Khai báo công trình hợp đồng";
            var windowId = "ADD_CONS_CONTRACT";
            disableForm(null, 'add');
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '85%', '85%', "deptAdd");
        }
        
      //-----------Cập nhật công trình hợp đồng-------------//
        var arrData = [];
        var arrId = [];
        var arrName = [];
        vm.checkStatus = true;
        vm.edit = function(dataItem){
        	vm.checkStatus = true;
        	
        	if(dataItem.status==1){
        		vm.checkStatus = false;
        	} else {
        		vm.checkStatus = true;
        	}
        	
        	if(dataItem.status==4 || dataItem.status==5 || dataItem.status==6 || dataItem.status==7){
        		if(vm.checkRoleCNKT){
        			toastr.warning("Bạn không có quyền chỉnh sửa bản ghi");
        			return;
        		}
        	}
        	
        	var listHttc = [];
        	if(dataItem.httcTdt=="1") {
        		listHttc.push(dataItem.httcTdt);
        	}
        	
        	if(dataItem.httcTctt=="2") {
        		listHttc.push(dataItem.httcTctt);
        	}

        	if(dataItem.httcKn=="3") {
        		listHttc.push(dataItem.httcKn);
        	}
        	
        	dataItem.listHttc = listHttc;
        	vm.isCreate = false;
        	vm.addForm = angular.copy(dataItem);
        	
            var teamplateUrl = "coms/manageDataOS/popupAddNew.html";
            var title = "Cập nhật công trình hợp đồng";
            var windowId = "EDIT_CONS_CONTRACT";
            disableForm(dataItem, 'edit');
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '85%', '85%', "deptAdd");
        }
        
        //---------------Hàm tính toán------------------//
        vm.changeValue = changeValue;
        	function changeValue(){
	        	vm.addForm.khtcTotalMoney = vm.addForm.khtcSalary + vm.addForm.khtcLaborOutsource + vm.addForm.khtcCostMaterial
	        		+ vm.addForm.khtcCostHshc + vm.addForm.khtcCostTransport + vm.addForm.khtcCostOrther;
	        	vm.addForm.khtcEffective = Math.round(vm.addForm.khtcTotalMoney / vm.addForm.hdContractValue *100000)/1000;
        }
        
        //---------------AutoComplete Công trình--------------//
        vm.constructionOptions = {
 				dataTextField: "constructionCode", 
 				placeholder:"Nhập mã/tên công trình",
 				open: function(e) {
 					vm.isSelect = false;
 				},
 	            select: function(e) {
 	            	vm.isSelect = true; 
 	            	var dataItem = this.dataItem(e.item.index());
 	            		vm.addForm.hdSignDate = dataItem.hdSignDate;
 	 	            	vm.addForm.hdContractCode = dataItem.hdContractCode;
 	 	            	vm.addForm.hdContractValue = Math.round(dataItem.hdContractValue / 1000)/1000;
 	 	            	vm.addForm.hdPerformDay = dataItem.hdPerformDay;
 	 	            	vm.addForm.provinceCode = dataItem.provinceCode;
 	 	            	vm.addForm.constructionCode = dataItem.constructionCode;
 	 	            	vm.addForm.stationCode = dataItem.stationCode;
 	 	            	vm.addForm.vtaSynchronizeDate = dataItem.vtaSynchronizeDate;
 	 	            	vm.addForm.vtaValue = Math.round(dataItem.vtaValue / 1000)/1000;
 	 	            	changeValue();
 	            },
 	            pageSize: 10,
 	            dataSource: {
 	                serverFiltering: true,
 	                transport: {
 	                    read: function(options) {
 	                        return Restangular.all("manageDataOutsideOsService/getAutoCompleteConstruction").post({
 	                        	pageSize:10, 
 	                        	page:1, 
 	                        	keySearch:$("#constructionCode").val().trim()
 	                        	}).then(function(response){
 	                            options.success(response.data);
 	                        }).catch(function (err) {
 	                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
 	                        });
 	                    }
 	                }
 	            },
 	            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
 	            '<p class="col-md-6 text-header-auto border-right-ccc">Mã công trình</p>' +
 	            '<p class="col-md-6 text-header-auto">Tên công trình</p>' +
 	            	'</div>',
 	            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.constructionCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.constructionName #</div> </div>',
 	            change: function(e) {	
 	            	if(!vm.isSelect){
 						$("#constructionCode").val(null);
 						vm.addForm.hdSignDate = null;
 	 	            	vm.addForm.hdContractCode = null;
 	 	            	vm.addForm.hdContractValue = null;
 	 	            	vm.addForm.hdPerformDay = null;
 	 	            	vm.addForm.provinceCode = null;
 	 	            	vm.addForm.constructionCode = null;
 	 	            	vm.addForm.stationCode = null;
 	 	            	vm.addForm.vtaSynchronizeDate = null;
 	 	            	vm.addForm.vtaValue = null;
 	            	}
 	            },
 	            close: function(e) {
 	            	if(!vm.isSelect){
 	            		$("#constructionCode").val(null);
 	            		vm.addForm.hdSignDate = null;
 	 	            	vm.addForm.hdContractCode = null;
 	 	            	vm.addForm.hdContractValue = null;
 	 	            	vm.addForm.hdPerformDay = null;
 	 	            	vm.addForm.provinceCode = null;
 	 	            	vm.addForm.constructionCode = null;
 	 	            	vm.addForm.stationCode = null;
 	 	            	vm.addForm.vtaSynchronizeDate = null;
 	 	            	vm.addForm.vtaValue = null;
 	            	}
 	            }
 			};
        
        vm.contructionSearchUrl = Constant.BASE_SERVICE_URL + "manageDataOutsideOsService/getAutoCompleteConstruction";
        
        vm.openConstruction = openConstruction;
        function openConstruction(){
        	vm.isCreateNew = true;
        	var templateUrl = 'coms/manageDataOS/comsConstructionSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm công trình");
            htmlCommonService.populatePopup(templateUrl, title, null, null, vm, null, 'string', false, '92%', '89%', 'searchConstructionController');
		}
        
        vm.onSaveComsConstruction = onSaveComsConstruction;
        function onSaveComsConstruction(dataItem){
        	vm.addForm.hdSignDate = dataItem.hdSignDate;
          	vm.addForm.hdContractCode = dataItem.hdContractCode;
          	vm.addForm.hdContractValue = dataItem.hdContractValue;
          	vm.addForm.hdPerformDay = dataItem.hdPerformDay;
          	vm.addForm.provinceCode = dataItem.provinceCode;
          	vm.addForm.constructionCode = dataItem.constructionCode;
          	vm.addForm.stationCode = dataItem.stationCode;
          	vm.addForm.vtaSynchronizeDate = dataItem.vtaSynchronizeDate;
          	vm.addForm.vtaValue = dataItem.vtaValue;
        }
        
        //----------------Tạo mới - Cập nhật Công trình hợp đồng--------------//
        function validated(data, id, name){
        	if(data==null || $(id).val()==""){
        		toastr.warning(name + " không được để trống");
        		$(id).focus();
        		return false;
        	} else {
        		return true;
        	}
        }
        
        function validatedNumber(data, id, name){
        	if(data!=null && parseFloat(data)<0){
        		toastr.warning(name + " phải lớn hơn hoặc bằng 0");
        		$(id).focus();
        		return false;
        	}
        	return true;
        }
        
        function validatedMonth(data, id, name){
        	if(data!=null && kendo.parseDate(data, "MM/yyyy")==null){
        		toastr.warning(name + " sai định dạng MM/yyyy");
        		$(id).focus();
        		return false;
        	}
        	return true;
        }
        
        vm.saveAdd = function(){
        	
        	if(!validated(vm.addForm.constructionCode, "#constructionCode", "Mã công trình")){return;}
        	if(!validated(vm.addForm.constructionType, "#constructionType", "Loại công trình")){return;}
        	if(!validated(vm.addForm.content, "#content", "Nội dung")){return;}        	
        	if(!validated(vm.addForm.capitalNtd, "#capitalNtd", "Nguồn vốn")){return;}
        	if(!validated(vm.addForm.khtcSalary, "#khtcSalary", "Lương")){return;}
        	if(!validatedNumber(vm.addForm.khtcSalary, "#khtcSalary", "Lương")){return;}
        	if(!validated(vm.addForm.khtcLaborOutsource, "#khtcLaborOutsource", "Nhân công thuê ngoài")){return;}
        	if(!validatedNumber(vm.addForm.khtcLaborOutsource, "#khtcLaborOutsource", "Nhân công thuê ngoài")){return;}
        	if(!validated(vm.addForm.khtcCostMaterial, "#khtcCostMaterial", "Chi phí vật liệu")){return;}
        	if(!validatedNumber(vm.addForm.khtcCostMaterial, "#khtcCostMaterial", "Chi phí vật liệu")){return;}
        	if(!validated(vm.addForm.khtcCostHshc,"#khtcCostHshc", "Chi phí HSHC")){return;}
        	if(!validatedNumber(vm.addForm.khtcCostHshc, "#khtcCostHshc", "Chi phí HSHC")){return;}
        	if(!validated(vm.addForm.khtcCostTransport, "#khtcCostTransport", "Chi phí vận chuyển")){return;}
        	if(!validatedNumber(vm.addForm.khtcCostTransport, "#khtcCostTransport", "Chi phí vận chuyển")){return;}
        	if(!validated(vm.addForm.khtcCostOrther, "#khtcCostOrther", "Chi phí khác")){return;}
        	if(!validatedNumber(vm.addForm.khtcCostOrther, "#khtcCostOrther", "Chi phí khác")){return;}
        	if(!validated($("#khtcDeploymentMonth").val(), "#khtcDeploymentMonth", "Tháng triển khai")){return;}
        	if(!validatedMonth($("#khtcDeploymentMonth").val(), "#khtcDeploymentMonth", "Tháng triển khai")){return;}
        	if(!validated(vm.addForm.gtslQuantityValue, "#gtslQuantityValue", "Giá trị sản lượng")){return;}
        	if(!validatedNumber(vm.addForm.gtslQuantityValue, "#gtslQuantityValue", "Giá trị sản lượng")){return;}
        	if(!validated(vm.addForm.listHttc, "#listHttc", "Hình thức thi công")){return;}
        	if(!validated(vm.addForm.tttcStartDate, "#tttcStartDate", "Ngày bắt đầu")){return;}
        	
        	vm.addForm.khtcDeploymentMonth = $("#khtcDeploymentMonth").val();
        	
        	if(vm.isCreate){
        		manageDataOSService.saveAddNew(vm.addForm).then(function(d){
            		if(d.error){
            			toastr.error(d.error);
            			return;
            		}
            		toastr.success("Thêm mới thành công");
            		CommonService.dismissPopup1();
            		doSearch();
            	}).catch(function(e){
            		toastr.error("Có lỗi xảy ra khi thêm mới");
            		return;
            	});
        	} else {
        		if(vm.addForm.status=='2'){
        			if(!validated(vm.addForm.tdntHshcStartDate, "#tdntHshcStartDate", "Ngày bắt đầu dựng HSHC")){return;}
                	if(!validated(vm.addForm.tdntAcceptanceStartDate, "#tdntAcceptanceStartDate", "Ngày bắt đầu nghiệm thu")){return;}
                	if(!validated(vm.addForm.tdntKthtExpertiseDate, "#tdntKthtExpertiseDate", "Ngày gửi P.KTHT thẩm duyệt")){return;}
                	if(!validated(vm.addForm.tdntSignProvinceDate, "#tdntSignProvinceDate", "Ngày trình ký GĐ tỉnh")){return;}
                	if(!validated(vm.addForm.tdntSendTctDate, "#tdntSendTctDate", "Ngày gửi Hồ sơ lên TCT")){return;}
                	if(!validated(vm.addForm.tdntCompleteExpectedDate, "#tdntCompleteExpectedDate", "Ngày dự kiến hoàn thành")){return;}
        		}
        		
        		if(vm.addForm.status=='3'){
        			if(vm.addForm.constructionType=='2' || vm.addForm.constructionType=='4' || vm.addForm.constructionType=='6'){
        				if(!validated(vm.addForm.dnqtQtCdtNotVat, "#dnqtQtCdtNotVat", "Giá trị quyết toán CĐT(chưa VAT)")){return;}
        				if(!validatedNumber(vm.addForm.dnqtQtCdtNotVat, "#dnqtQtCdtNotVat", "Giá trị quyết toán CĐT(chưa VAT)")){return;}
        				if(!validated(vm.addForm.dnqtQtCdtVat, "#dnqtQtCdtVat", "Giá trị quyết toán CĐT(có VAT)")){return;}
        				if(!validatedNumber(vm.addForm.dnqtQtCdtVat, "#dnqtQtCdtVat", "Giá trị quyết toán CĐT(có VAT)")){return;}
        			}
        			if(vm.addForm.constructionType=='4'){
        				if(!validated(vm.addForm.dnqtElectricalProcedures, "#dnqtElectricalProcedures", "Thủ tục điện lực")){return;}
        				if(!validatedNumber(vm.addForm.dnqtElectricalProcedures, "#dnqtElectricalProcedures", "Thủ tục điện lực")){return;}
        				if(!validated(vm.addForm.dnqtWeldingSalary, "#dnqtWeldingSalary", "Lương hàn nối")){return;}
        				if(!validatedNumber(vm.addForm.dnqtWeldingSalary, "#dnqtWeldingSalary", "Lương hàn nối")){return;}
        			}
        			if(!validated(vm.addForm.dnqtPullCableLabor, "#dnqtPullCableLabor", "Nhân công kéo cáp/Nhân công")){return;}
        			if(!validatedNumber(vm.addForm.dnqtPullCableLabor, "#dnqtPullCableLabor", "Nhân công kéo cáp/Nhân công")){return;}
                	if(!validated(vm.addForm.dnqtCostMaterial, "#dnqtCostMaterial", "Chi phí vật liệu")){return;}
                	if(!validatedNumber(vm.addForm.dnqtCostMaterial, "#dnqtCostMaterial", "Chi phí vật liệu")){return;}
                	if(!validated(vm.addForm.dnqtCostHshc, "#dnqtCostHshc", "Chi phí HSHC")){return;}
                	if(!validatedNumber(vm.addForm.dnqtCostHshc, "#dnqtCostHshc", "Chi phí HSHC")){return;}
                	if(!validated(vm.addForm.dnqtCostTransportWarehouse, "#dnqtCostTransportWarehouse", "Chi phí vận chuyển kho bãi")){return;}
                	if(!validatedNumber(vm.addForm.dnqtCostTransportWarehouse, "#dnqtCostTransportWarehouse", "Chi phí vận chuyển kho bãi")){return;}
                	if(!validated(vm.addForm.dnqtCostOrther, "#dnqtCostOrther", "Chi phí khác")){return;}
                	if(!validatedNumber(vm.addForm.dnqtCostOrther, "#dnqtCostOrther", "Chi phí khác")){return;}
                	if(!validated(vm.addForm.dnqtSalaryCableOrther, "#dnqtSalaryCableOrther", "Lương kéo cáp/lương khác")){return;}
                	if(!validatedNumber(vm.addForm.dnqtSalaryCableOrther, "#dnqtSalaryCableOrther", "Lương kéo cáp/lương khác")){return;}
                	if(!validated(vm.addForm.dnqtVat, "#dnqtVat", "VAT")){return;}
                	if(!validatedNumber(vm.addForm.dnqtVat, "#dnqtVat", "VAT")){return;}
                	if(!validated(vm.addForm.dnqtTotalMoney, "#dnqtTotalMoney", "Tổng")){return;}
                	if(!validatedNumber(vm.addForm.dnqtTotalMoney, "#dnqtTotalMoney", "Tổng")){return;}
        		}
        		
        		if(vm.addForm.status=='4'){
        			if(vm.addForm.constructionType=='4'){
        				if(!validated(vm.addForm.gttdElectricalProcedures, "#gttdElectricalProcedures", "Thủ tục điện lực")){return;}
        				if(!validatedNumber(vm.addForm.gttdElectricalProcedures, "#gttdElectricalProcedures", "Thủ tục điện lực")){return;}
        				if(!validated(vm.addForm.gttdWeldingSalary, "#gttdWeldingSalary", "Lương hàn nối")){return;}
        				if(!validatedNumber(vm.addForm.gttdWeldingSalary, "#gttdWeldingSalary", "Lương hàn nối")){return;}
        			}
        			if(!validated(vm.addForm.gttdHshcHardDate, "#gttdHshcHardDate", "Ngày nhận HSHC bản cứng")){return;}
                	if(!validated(vm.addForm.gttdPullCableLabor, "#gttdPullCableLabor", "Nhân công kéo cáp/Nhân công")){return;}
                	if(!validatedNumber(vm.addForm.gttdPullCableLabor, "#gttdPullCableLabor", "Nhân công kéo cáp/Nhân công")){return;}
                	if(!validated(vm.addForm.gttdSalaryCableOrther, "#gttdSalaryCableOrther", "Lương kéo cáp/lương khác")){return;}
                	if(!validatedNumber(vm.addForm.gttdSalaryCableOrther, "#gttdSalaryCableOrther", "Lương kéo cáp/lương khác")){return;}
                	if(!validated(vm.addForm.gttdGttdPtk, "#gttdGttdPtk", "Giá trị thẩm định PTK")){return;}
                	if(!validatedNumber(vm.addForm.gttdGttdPtk, "#gttdGttdPtk", "Giá trị thẩm định PTK")){return;}
                	
                	if(!validated($("#gttdHshcMonth").val(), "#gttdHshcMonth", "Tháng ghi nhận HSHC")){return;}
                	if(!validatedMonth($("#gttdHshcMonth").val(), "#gttdHshcMonth", "Tháng ghi nhận HSHC")){return;}
                	if(!validated($("#gttdSalaryMonth").val(), "#gttdSalaryMonth", "Tháng ghi nhận quỹ lương")){return;}
                	if(!validatedMonth($("#gttdSalaryMonth").val(), "#gttdSalaryMonth", "Tháng ghi nhận quỹ lương")){return;}
                	if(!validated(vm.addForm.gttdSalaryReal, "#gttdSalaryReal", "Lương thực nhận")){return;}
                	if(!validatedNumber(vm.addForm.gttdSalaryReal, "#gttdSalaryReal", "Lương thực nhận")){return;}
        		}
        		
        		if(vm.addForm.status=='5'){
        			if(!validated(vm.addForm.qtdnSuggestionsDate, "#qtdnSuggestionsDate", "Ngày lập đề nghị quyết toán VTNet")){return;}
                	if(!validated(vm.addForm.qtdnValue, "#qtdnValue", "Giá trị đề nghị quyết toán VTNet")){return;}
                	if(!validatedNumber(vm.addForm.qtdnValue, "#qtdnValue", "Giá trị đề nghị quyết toán VTNet")){return;}
                	if(!validated(vm.addForm.qtdnVtnetDate, "#qtdnVtnetDate", "Ngày nộp VTNet")){return;}
                	if(!validated(vm.addForm.qttdExpertiseCompleteDate, "#qttdExpertiseCompleteDate", "Ngày thẩm định xong")){return;}
                	if(!validated(vm.addForm.qttdValue, "#qttdValue", "Giá trị thẩm định")){return;}
                	if(!validatedNumber(vm.addForm.qttdValue, "#qttdValue", "Giá trị thẩm định")){return;}
        		}
        		
        		if(vm.addForm.status=='6'){
        			if(!validated(vm.addForm.xhdPtcDate, "#xhdPtcDate", "Ngày chuyển PTC")){return;}
                	if(!validated(vm.addForm.xhdXhdDate, "#xhdXhdDate", "Ngày xuất hóa đơn")){return;}
                	if(!validated(vm.addForm.xhdSoHd, "#xhdSoHd", "Số hóa đơn")){return;}
                	if(!validated($("#xhdRevenueMonth").val(), "#xhdRevenueMonth", "Tháng chốt doanh thu")){return;}
                	if(!validatedMonth($("#xhdRevenueMonth").val(), "#xhdRevenueMonth", "Tháng chốt doanh thu")){return;}
        		}
        		
        		if(vm.addForm.status=='7'){
        			if(!validated(vm.addForm.tlSignDate, "#tlSignDate", "Ngày ký thanh lý")){return;}
                	if(!validated(vm.addForm.tlValue, "#tlValue", "Giá trị")){return;}
                	if(!validatedNumber(vm.addForm.tlValue, "#tlValue", "Giá trị")){return;}
        		}
        		
        		if(vm.addForm.status=='8'){
        			if(!validated(vm.addForm.qtncPhtDate, "#qtncPhtDate", "Ngày chuyển hồ sơ lên PHT")){return;}
                	if(!validated(vm.addForm.qtncPtcDate, "#qtncPtcDate", "Ngày chuyển hồ sơ lên PTC")){return;}
                	if(!validated(vm.addForm.qtncVtaAccountDate, "#qtncVtaAccountDate", "Ngày hạch toán nhân công và vật tư A cấp")){return;}
                	if(!validated(vm.addForm.qtncTakeMoneyDate, "#qtncTakeMoneyDate", "Ngày nhận tiền")){return;}
        		}
        		
        		vm.addForm.gttdHshcMonth = $("#gttdHshcMonth").val();
        		vm.addForm.gttdSalaryMonth = $("#gttdSalaryMonth").val();
        		vm.addForm.xhdRevenueMonth = $("#xhdRevenueMonth").val();
        		manageDataOSService.saveUpdateNew(vm.addForm).then(function(d){
            		if(d.error){
            			toastr.error(d.error);
            			return;
            		}
            		toastr.success("Cập nhật thành công");
            		CommonService.dismissPopup1();
            		doSearch();
            	}).catch(function(e){
            		toastr.error("Có lỗi xảy ra khi cập nhật");
            		return;
            	});
        	}
        	
        }
        
        //HuyPQ-20191114
                //------------Xóa ô nhập ở màn tìm kiếm----------//
        vm.cancelInput = function(data){
        	if(data=='status'){
        		vm.searchForm.status = null;
        	}
        	if(data=='keySearch'){
        		vm.searchForm.keySearch = null;
        	}
        }
        
        //-----------Mở popup import--------------//
        vm.importFile = importFile;
		function importFile() {
			if(vm.searchForm.status==null){
				toastr.warning("Chưa chọn trạng thái để import");
				return;
			}
			if(vm.searchForm.status==1){
				vm.checkImportConstruction = true;
				vm.checkImportSchedule = false;
				vm.checkImportSettlementProposal = false;
				vm.checkImportProposalLabor = false;
				var teamplateUrl="coms/manageDataOS/importFileCons.html";
				var title="Import công trình hợp đồng";
			} else if(vm.searchForm.status==2){
				vm.checkImportSchedule = true;
				vm.checkImportConstruction = false;
				vm.checkImportSettlementProposal = false;
				vm.checkImportProposalLabor = false;
				var teamplateUrl="coms/manageDataOS/importFileCons.html";
				var title="Import lập tiến độ";
			} else if(vm.searchForm.status==3){
				vm.checkImportSettlementProposal = true;
				vm.checkImportSchedule = false;
				vm.checkImportConstruction = false;
				vm.checkImportProposalLabor = false;
				var teamplateUrl="coms/manageDataOS/importFileCons.html";
				var title="Import lập đề nghị quyết toán";
			} else if(vm.searchForm.status==4){
				vm.fileImportData = false;
				var teamplateUrl="coms/manageDataOS/import/importExpertiseProposal.html";
				var title="Import thẩm định quyết toán";
			} else if(vm.searchForm.status==5){
				vm.fileImportDataCDT = false;
				var teamplateUrl="coms/manageDataOS/import/importExpertiseProposalCDT.html";
				var title="Import quyết toán chủ đầu tư";
			} else if(vm.searchForm.status==6){
				vm.fileImportDataInvoice = false;
				var teamplateUrl="coms/manageDataOS/import/importInvoice.html";
				var title="Import xuất hóa đơn";
			} else if(vm.searchForm.status==7){
				vm.fileImportDataInvoice = false;
				var teamplateUrl="coms/manageDataOS/import/importLiquidation.html";
				var title="Import thanh lý hợp đồng";
			} else if(vm.searchForm.status==8){
				vm.checkImportProposalLabor = true;
				vm.checkImportSettlementProposal = false;
				vm.checkImportSchedule = false;
				vm.checkImportConstruction = false;
				var teamplateUrl="coms/manageDataOS/importFileCons.html";
				var title="Import quyết toán nhân công";
			}
			var windowId="IMPORT_CONS_CONTRACT";
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','275');
		}
		vm.showHideColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.manageDataOSGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.manageDataOSGrid.showColumn(column);
            } else {
                vm.manageDataOSGrid.hideColumn(column);
            }


        }
        vm.importConstruction = importConstruction;
		function importConstruction() {
			vm.checkImportConstruction = true;
			vm.checkImportSchedule = false;
			vm.checkImportSettlementProposal = false;
			vm.checkImportProposalLabor = false;
			var teamplateUrl="coms/manageDataOS/importFileCons.html";
			var title="Import công trình hợp đồng";
			var windowId="IMPORT_CONS_CONTRACT";
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','275');
		}
		
		vm.importSchedule = importSchedule;
		function importSchedule() {
			if(vm.searchForm.status==null){
				toastr.warning("Chưa chọn trạng thái để import");
				return;
			}
			
			if($("#status").val()!="1" && $("#status").val()!="2"){
				toastr.warning("Chọn trạng thái Thi công để Lập tiến độ");
				return;
			}
			
			vm.checkImportSchedule = true;
			vm.checkImportConstruction = false;
			vm.checkImportSettlementProposal = false;
			vm.checkImportProposalLabor = false;
			var teamplateUrl="coms/manageDataOS/importFileCons.html";
			var title="Import lập tiến độ";
			var windowId="IMPORT_SCHEDULE";
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','275');
		}
		
		vm.importSettlementProposal = importSettlementProposal;
		function importSettlementProposal() {
			if(vm.searchForm.status==null){
				toastr.warning("Chưa chọn trạng thái để import");
				return;
			}
			
			if($("#status").val()!="2" && $("#status").val()!="3"){
				toastr.warning("Chọn trạng thái Lập HSHC để Lập đề nghị quyết toán");
				return;
			}
			
			vm.checkImportSettlementProposal = true;
			vm.checkImportSchedule = false;
			vm.checkImportConstruction = false;
			vm.checkImportProposalLabor = false;
			var teamplateUrl="coms/manageDataOS/importFileCons.html";
			var title="Import lập đề nghị quyết toán";
			var windowId="IMPORT_SETTLEMENT_PROPOSAL";
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','275');
		}
        
		vm.importProposalLabor = importProposalLabor;
		function importProposalLabor() {
			if(vm.searchForm.status==null){
				toastr.warning("Chưa chọn trạng thái để import");
				return;
			}
			
			if($("#status").val()!="6" && $("#status").val()!="7" && $("#status").val()!="8"){
				toastr.warning("Chọn trạng thái Xuất hóa đơn hoặc Thanh lý để Quyết toán nhân công");
				return;
			}
			
			vm.checkImportProposalLabor = true;
			vm.checkImportSettlementProposal = false;
			vm.checkImportSchedule = false;
			vm.checkImportConstruction = false;
			var teamplateUrl="coms/manageDataOS/importFileCons.html";
			var title="Import quyết toán nhân công";
			var windowId="IMPORT_PROPOSAL_LABOR";
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','275');
		}
		
		vm.getExcelTemplateImport = function(data){
			kendo.ui.progress($("#shipment"), true);
			if(data=='importConstruction'){
				return Restangular.all("manageDataOutsideOsService/exportTemplateConsContract").post(vm.searchForm).then(function (d) {
	        	    var data = d.plain();
	        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
	        	    kendo.ui.progress($("#shipment"), false);
	        	}).catch(function (e) {
	        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
	        	    kendo.ui.progress($("#shipment"), false);
	        	    return;
	        	});
			} else if(data=='importSchedule'){
				var obj = {};
				obj.status = "1";
				obj.keySearch = vm.searchForm.keySearch;
				return Restangular.all("manageDataOutsideOsService/exportTemplateLapTienDo").post(obj).then(function (d) {
	        	    var data = d.plain();
	        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
	        	    kendo.ui.progress($("#shipment"), false);
	        	}).catch(function (e) {
	        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
	        	    kendo.ui.progress($("#shipment"), false);
	        	    return;
	        	});
			} else if(data=='importSettlementProposal'){
				var obj = {};
				obj.status = "2";
				obj.keySearch = vm.searchForm.keySearch;
				return Restangular.all("manageDataOutsideOsService/exportTemplateDNQT").post(obj).then(function (d) {
	        	    var data = d.plain();
	        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
	        	    kendo.ui.progress($("#shipment"), false);
	        	}).catch(function (e) {
	        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
	        	    kendo.ui.progress($("#shipment"), false);
	        	    return;
	        	});
			} else if(data=='importProposalLabor'){
				var obj = {};
				obj.status = "6";
				obj.keySearch = vm.searchForm.keySearch;
				return Restangular.all("manageDataOutsideOsService/exportTemplateQuyetToanNC").post(obj).then(function (d) {
	        	    var data = d.plain();
	        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
	        	    kendo.ui.progress($("#shipment"), false);
	        	}).catch(function (e) {
	        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
	        	    kendo.ui.progress($("#shipment"), false);
	        	    return;
	        	});
			}
		}
		
		vm.disableSubmit = false;
	    vm.fileImportDataCons = false;
		vm.submitImportCons = function(data){
			var url = null;
			if(data=='importConstruction'){
				url = Constant.BASE_SERVICE_URL+"manageDataOutsideOsService/importConsContract?folder=temp";
			} else if(data=='importSchedule'){
				url = Constant.BASE_SERVICE_URL+"manageDataOutsideOsService/importSchedule?folder=temp";
			} else if(data=='importSettlementProposal'){
				url = Constant.BASE_SERVICE_URL+"manageDataOutsideOsService/importSettlementProposal?folder=temp";
			} else if(data=='importProposalLabor'){
				url = Constant.BASE_SERVICE_URL+"manageDataOutsideOsService/importProposalLabor?folder=temp";
			}
			
			$('#testSubmit').addClass('loadersmall');
            vm.disableSubmit = true;
            if (!vm.fileImportDataCons) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Bạn chưa chọn file để import");
                return;
            }
            if ($('.k-invalid-msg').is(':visible')) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                return;
            }
            if (vm.fileImportDataCons.name.split('.').pop() !== 'xls' && vm.fileImportDataCons.name.split('.').pop() !== 'xlsx') {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Sai định dạng file");
                return;
            }
            var formData = new FormData();
            formData.append('multipartFile', vm.fileImportDataCons);
            return $.ajax({
                url: url,
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if (data == 'NO_CONTENT') {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.warning("File import không có nội dung");
                    }
                    else if (!!data.error) {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.error(data.error);
                    }
                    else if (data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length > 0) {
                        vm.lstErrImport = data[data.length - 1].errorList;
                        vm.objectErr = data[data.length - 1];
                        var templateUrl = "wms/createImportRequirementManagement/importResultPopUp.html";
                        var title = "Kết quả Import";
                        var windowId = "ERR_IMPORT";
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        CommonService.populatePopupCreate(templateUrl, title, vm.lstErrImport, vm, windowId, false, '80%', '420px');
                        fillDataImportErrTable(vm.lstErrImport);
                    }
                    else {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.success("Import thành công");
                        $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
                        doSearch();
                    }
                    $scope.$apply();
                }
            });
			
		}
        //Huy-end
        
        //tatph -start 13/11/2019
        vm.importExpertiseProposal = importExpertiseProposal;
		function importExpertiseProposal() {
			
			vm.popupName = 'IMPORT_PROPOSAL';
			vm.fileImportData = false;
			var teamplateUrl="coms/manageDataOS/import/importExpertiseProposal.html";
			var title="Import";
			var windowId="IMPORT_PROPOSAL";
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','275','das');

		}
		
		  //tatph -start 13/11/2019
        vm.importProposalCDT = importProposalCDT;
		function importProposalCDT() {
			
			vm.popupName = 'IMPORT_PROPOSAL_CDT';
			vm.fileImportDataCDT = false;
			var teamplateUrl="coms/manageDataOS/import/importExpertiseProposalCDT.html";
			var title="Import";
			var windowId="IMPORT_PROPOSAL";
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','275','das');

		}
		
		  //tatph -start 13/11/2019
        vm.importExportInvoice = importExportInvoice;
		function importExportInvoice() {
			
			vm.popupName = 'IMPORT_INVOICE';
			vm.fileImportDataInvoice = false;
			var teamplateUrl="coms/manageDataOS/import/importInvoice.html";
			var title="Import";
			var windowId="IMPORT_INVOICE";
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','275','das');

		}
		
		  //tatph -start 13/11/2019
        vm.importLiquidation = importLiquidation;
		function importLiquidation() {
			
			vm.popupName = 'IMPORT_LIQUIDATION';
			vm.fileImportDataInvoice = false;
			var teamplateUrl="coms/manageDataOS/import/importLiquidation.html";
			var title="Import";
			var windowId="IMPORT_LIQUIDATION";
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','275','das');

		}
		
		 vm.getExcelTemplateExpertiseProposal = function () {
			 if(vm.searchForm.status == null || vm.searchForm.status == "" || vm.searchForm.status == undefined){
					toastr.warning("Hãy chọn trạng thái tìm kiếm để lấy mẫu");
	        		return;
				}
			 var obj = {};
			 obj.status = "3";
			 obj.keySearch = vm.searchForm.keySearch;
			 manageDataOSService.setStatus(obj).then(function (data) {
                 // close popup and refresh table list
                 if (data.error) {
                     toastr.error(data.error);
                     return;
                 }
                 var fileName = "Bieu_mau_tham_dinh_qt";
 	            CommonService.downloadTemplate(fileName).then(function (d) {
 	                var data = d.plain();
 	                window.location = Constant.BASE_SERVICE_URL + "manageDataOutsideOsService/getExcelTemplateExpertiseProposal?" + data.fileName;
 	            }).catch(function () {
 	                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
 	                return;
 	            });
             }, function (e) {
                 toastr.error("Có lỗi trong quá trình xử lý dữ liệu.");
             });
			 
	            
	        }
		 
		 vm.exportFile = function () {
                 // close popup and refresh table list
			 manageDataOSService.setStatus(vm.searchForm).then(function (data) {
				 if (data.error) {
                     toastr.error(data.error);
                     return;
                 }
                 var fileName = "Danh_sach_du_lieu_ngoai_os";
 	            CommonService.downloadTemplate(fileName).then(function (d) {
 	                var data = d.plain();
 	                window.location = Constant.BASE_SERVICE_URL + "manageDataOutsideOsService/exportFile?" + data.fileName;
 	            }).catch(function () {
 	                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
 	                return;
 	            });
			 });
                
			 
	            
	        }
		 
		 vm.getExcelTemplateLiquidation = function () {
			 if(vm.searchForm.status == null || vm.searchForm.status == "" || vm.searchForm.status == undefined){
					toastr.warning("Hãy chọn trạng thái tìm kiếm để lấy mẫu");
	        		return;
				}
			 var obj = {};
			 obj.status = "6";
			 obj.keySearch = vm.searchForm.keySearch;
			 manageDataOSService.setStatus(obj).then(function (data) {
                 // close popup and refresh table list
                 if (data.error) {
                     toastr.error(data.error);
                     return;
                 }
                 var fileName = "Bieu_mau_thanh_ly";
 	            CommonService.downloadTemplate(fileName).then(function (d) {
 	                var data = d.plain();
 	                window.location = Constant.BASE_SERVICE_URL + "manageDataOutsideOsService/getExcelTemplateLiquidation?" + data.fileName;
 	            }).catch(function () {
 	                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
 	                return;
 	            });
             }, function (e) {
                 toastr.error("Có lỗi trong quá trình xử lý dữ liệu.");
             });
			 
	            
	        }
		 
		 vm.getExcelTemplateExpertiseProposalCDT = function () {
			 if(vm.searchForm.status == null || vm.searchForm.status == "" || vm.searchForm.status == undefined){
					toastr.warning("Hãy chọn trạng thái tìm kiếm để lấy mẫu");
	        		return;
				}
			 var obj = {};
			 obj.status = "4";
			 obj.keySearch = vm.searchForm.keySearch;
			 manageDataOSService.setStatus(obj).then(function (data) {
                 // close popup and refresh table list
                 if (data.error) {
                     toastr.error(data.error);
                     return;
                 }
                 var fileName = "Bieu_mau_quyet_toan_cdt";
 	            CommonService.downloadTemplate(fileName).then(function (d) {
 	                var data = d.plain();
 	                window.location = Constant.BASE_SERVICE_URL + "manageDataOutsideOsService/getExcelTemplateExpertiseProposalCDT?" + data.fileName;
 	            }).catch(function () {
 	                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
 	                return;
 	            });
             }, function (e) {
                 toastr.error("Có lỗi trong quá trình xử lý dữ liệu.");
             });
			 
	            
	        }
		 
		 vm.getExcelTemplateInvoice = function () {
			 if(vm.searchForm.status == null || vm.searchForm.status == "" || vm.searchForm.status == undefined){
					toastr.warning("Hãy chọn trạng thái tìm kiếm để lấy mẫu");
	        		return;
				}
			 var obj = {};
			 obj.listStatus = ['4','5'];
			 obj.status = null;
			 obj.keySearch = vm.searchForm.keySearch;
			 manageDataOSService.setStatus(obj).then(function (data) {
                 // close popup and refresh table list
                 if (data.error) {
                     toastr.error(data.error);
                     return;
                 }
                 var fileName = "Bieu_mau_xuat_bao_cao";
 	            CommonService.downloadTemplate(fileName).then(function (d) {
 	                var data = d.plain();
 	                window.location = Constant.BASE_SERVICE_URL + "manageDataOutsideOsService/getExcelTemplateInvoice?" + data.fileName;
 	            }).catch(function () {
 	                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
 	                return;
 	            });
             }, function (e) {
                 toastr.error("Có lỗi trong quá trình xử lý dữ liệu.");
             });
			 
	            
	        }
		 
		 vm.fileImportData = false;
	        vm.submit=submit;
	        function submit(data){
	        	$('#testSubmit').addClass('loadersmall');
	        	vm.disableSubmit = true;
	        	if(!vm.fileImportData){
	        		$('#testSubmit').removeClass('loadersmall');
		    		vm.disableSubmit = false;
		    		toastr.warning("Bạn chưa chọn file để import");
		    		return;
		    	}
				if($('.k-invalid-msg').is(':visible')){
					$('#testSubmit').removeClass('loadersmall');
		    		vm.disableSubmit = false;
					return;
				}
				if(vm.fileImportData.name.split('.').pop() !=='xls' && vm.fileImportData.name.split('.').pop() !=='xlsx' ){
					$('#testSubmit').removeClass('loadersmall');
		    		vm.disableSubmit = false;
					toastr.warning("Sai định dạng file");
					return;
				}
		    		 var formData = new FormData();
						formData.append('multipartFile', vm.fileImportData);
				     return   $.ajax({
				    	 url: Constant.BASE_SERVICE_URL+"manageDataOutsideOsService"+"/importExpertiseProposal?folder="+Constant.UPLOAD_FOLDER_TYPE_TEMP,
				            type: "POST",
				            data: formData,
				            enctype: 'multipart/form-data',
				            processData: false,
				            contentType: false,
				            cache: false,
				            success:function(data) {
				            if(data == 'NO_CONTENT') {
				            	toastr.warning("File import không có nội dung");
				            	$('#testSubmit').removeClass('loadersmall');
					    		vm.disableSubmit = false;
					    		return;
				            }
				            if(data == 'NOT_ACCEPTABLE') {
				            	toastr.error("Công trình đã thuộc hợp đồng đầu ra khác!");
				            	$('#testSubmit').removeClass('loadersmall');
					    		vm.disableSubmit = false;
				            }
				            else if(!!data.error){
				            	$('#testSubmit').removeClass('loadersmall');
					    		vm.disableSubmit = false;
				            		toastr.error(data.error);
				            }
				            else if(data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length>0){
				            		vm.lstErrImport = data[data.length - 1].errorList;
				            		vm.objectErr = data[data.length - 1];
				            		var templateUrl="wms/createImportRequirementManagement/importResultPopUp.html";
				       			 	var title="Kết quả Import";
				       			 	var windowId="ERR_IMPORT";
				       			 $('#testSubmit').removeClass('loadersmall');
						    		vm.disableSubmit = false;
				       			 	CommonService.populatePopupCreate(templateUrl,title,vm.lstErrImport,vm,windowId,false,'80%','420px');
				       			 	fillDataImportErrTable(vm.lstErrImport);
				            	}
				            	else{
				            		$('#testSubmit').removeClass('loadersmall');
						    		vm.disableSubmit = false;
				            		toastr.success("Import thành công")
				            		$("#manageDataOSGrid").data('kendoGrid').dataSource.read();
				       			 	$("#manageDataOSGrid").data('kendoGrid').refresh();
				       			 	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
				            	}
				            }
				        });
	     }
	        
	        vm.fileImportDataLiquidation = false;
	        vm.submitLiquidation=submitLiquidation;
	        function submitLiquidation(data){
	        	$('#testSubmitLiquidation').addClass('loadersmall');
	        	vm.disableSubmit = true;
	        	if(!vm.fileImportDataLiquidation){
	        		$('#testSubmitLiquidation').removeClass('loadersmall');
		    		vm.disableSubmit = false;
		    		toastr.warning("Bạn chưa chọn file để import");
		    		return;
		    	}
				if($('.k-invalid-msg').is(':visible')){
					$('#testSubmitLiquidation').removeClass('loadersmall');
		    		vm.disableSubmit = false;
					return;
				}
				if(vm.fileImportDataLiquidation.name.split('.').pop() !=='xls' && vm.fileImportDataLiquidation.name.split('.').pop() !=='xlsx' ){
					$('#testSubmitLiquidation').removeClass('loadersmall');
		    		vm.disableSubmit = false;
					toastr.warning("Sai định dạng file");
					return;
				}
		    		 var formData = new FormData();
						formData.append('multipartFile', vm.fileImportDataLiquidation);
				     return   $.ajax({
				    	 url: Constant.BASE_SERVICE_URL+"manageDataOutsideOsService"+"/importLiquidation?folder="+Constant.UPLOAD_FOLDER_TYPE_TEMP,
				            type: "POST",
				            data: formData,
				            enctype: 'multipart/form-data',
				            processData: false,
				            contentType: false,
				            cache: false,
				            success:function(data) {
				            if(data == 'NO_CONTENT') {
				            	toastr.warning("File import không có nội dung");
				            	$('#testSubmitLiquidation').removeClass('loadersmall');
					    		vm.disableSubmit = false;
					    		return;
				            }
				            if(data == 'NOT_ACCEPTABLE') {
				            	toastr.error("Công trình đã thuộc hợp đồng đầu ra khác!");
				            	$('#testSubmitLiquidation').removeClass('loadersmall');
					    		vm.disableSubmit = false;
				            }
				            else if(!!data.error){
				            	$('#testSubmitLiquidation').removeClass('loadersmall');
					    		vm.disableSubmit = false;
				            		toastr.error(data.error);
				            }
				            else if(data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length>0){
				            		vm.lstErrImport = data[data.length - 1].errorList;
				            		vm.objectErr = data[data.length - 1];
				            		var templateUrl="wms/createImportRequirementManagement/importResultPopUp.html";
				       			 	var title="Kết quả Import";
				       			 	var windowId="ERR_IMPORT";
				       			 $('#testSubmitLiquidation').removeClass('loadersmall');
						    		vm.disableSubmit = false;
				       			 	CommonService.populatePopupCreate(templateUrl,title,vm.lstErrImport,vm,windowId,false,'80%','420px');
				       			 	fillDataImportErrTable(vm.lstErrImport);
				            	}
				            	else{
				            		$('#testSubmitLiquidation').removeClass('loadersmall');
						    		vm.disableSubmit = false;
				            		toastr.success("Import thành công")
				            		$("#manageDataOSGrid").data('kendoGrid').dataSource.read();
				       			 	$("#manageDataOSGrid").data('kendoGrid').refresh();
				       			 	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
				            	}
				            }
				        });
	     }
	        
	        
	        vm.fileImportDataCDT = false;
	        vm.submitCDT=submitCDT;
	        function submitCDT(data){
	        	$('#testSubmitCDT').addClass('loadersmall');
	        	vm.disableSubmit = true;
	        	if(!vm.fileImportDataCDT){
	        		$('#testSubmitCDT').removeClass('loadersmall');
		    		vm.disableSubmit = false;
		    		toastr.warning("Bạn chưa chọn file để import");
		    		return;
		    	}
				if($('.k-invalid-msg').is(':visible')){
					$('#testSubmitCDT').removeClass('loadersmall');
		    		vm.disableSubmit = false;
					return;
				}
				if(vm.fileImportDataCDT.name.split('.').pop() !=='xls' && vm.fileImportDataCDT.name.split('.').pop() !=='xlsx' ){
					$('#testSubmitCDT').removeClass('loadersmall');
		    		vm.disableSubmit = false;
					toastr.warning("Sai định dạng file");
					return;
				}
		    		 var formData = new FormData();
						formData.append('multipartFile', vm.fileImportDataCDT);
				     return   $.ajax({
				    	 url: Constant.BASE_SERVICE_URL+"manageDataOutsideOsService"+"/importExpertiseProposalCDT?folder="+Constant.UPLOAD_FOLDER_TYPE_TEMP,
				            type: "POST",
				            data: formData,
				            enctype: 'multipart/form-data',
				            processData: false,
				            contentType: false,
				            cache: false,
				            success:function(data) {
				            if(data == 'NO_CONTENT') {
				            	toastr.warning("File import không có nội dung");
				            	$('#testSubmitCDT').removeClass('loadersmall');
					    		vm.disableSubmit = false;
					    		return;
				            }
				            if(data == 'NOT_ACCEPTABLE') {
				            	toastr.error("Công trình đã thuộc hợp đồng đầu ra khác!");
				            	$('#testSubmitCDT').removeClass('loadersmall');
					    		vm.disableSubmit = false;
				            }
				            else if(!!data.error){
				            	$('#testSubmitCDT').removeClass('loadersmall');
					    		vm.disableSubmit = false;
				            		toastr.error(data.error);
				            }
				            else if(data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length>0){
				            		vm.lstErrImport = data[data.length - 1].errorList;
				            		vm.objectErr = data[data.length - 1];
				            		var templateUrl="wms/createImportRequirementManagement/importResultPopUp.html";
				       			 	var title="Kết quả Import";
				       			 	var windowId="ERR_IMPORT";
				       			 $('#testSubmitCDT').removeClass('loadersmall');
						    		vm.disableSubmit = false;
				       			 	CommonService.populatePopupCreate(templateUrl,title,vm.lstErrImport,vm,windowId,false,'80%','420px');
				       			 	fillDataImportErrTable(vm.lstErrImport);
				            	}
				            	else{
				            		$('#testSubmitCDT').removeClass('loadersmall');
						    		vm.disableSubmit = false;
				            		toastr.success("Import thành công")
				            		$("#manageDataOSGrid").data('kendoGrid').dataSource.read();
				       			 	$("#manageDataOSGrid").data('kendoGrid').refresh();
				       			 	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
				            	}
				            }
				        });
	     }
	        
	        vm.fileImportDataInvoice = false;
	        vm.submitInvoice=submitInvoice;
	        function submitInvoice(data){
	        	$('#testSubmitInvoice').addClass('loadersmall');
	        	vm.disableSubmit = true;
	        	if(!vm.fileImportDataInvoice){
	        		$('#testSubmitInvoice').removeClass('loadersmall');
		    		vm.disableSubmit = false;
		    		toastr.warning("Bạn chưa chọn file để import");
		    		return;
		    	}
				if($('.k-invalid-msg').is(':visible')){
					$('#testSubmitInvoice').removeClass('loadersmall');
		    		vm.disableSubmit = false;
					return;
				}
				if(vm.fileImportDataInvoice.name.split('.').pop() !=='xls' && vm.fileImportDataInvoice.name.split('.').pop() !=='xlsx' ){
					$('#testSubmitInvoice').removeClass('loadersmall');
		    		vm.disableSubmit = false;
					toastr.warning("Sai định dạng file");
					return;
				}
		    		 var formData = new FormData();
						formData.append('multipartFile', vm.fileImportDataInvoice);
				     return   $.ajax({
				    	 url: Constant.BASE_SERVICE_URL+"manageDataOutsideOsService"+"/importInvoice?folder="+Constant.UPLOAD_FOLDER_TYPE_TEMP,
				            type: "POST",
				            data: formData,
				            enctype: 'multipart/form-data',
				            processData: false,
				            contentType: false,
				            cache: false,
				            success:function(data) {
				            if(data == 'NO_CONTENT') {
				            	toastr.warning("File import không có nội dung");
				            	$('#testSubmitInvoice').removeClass('loadersmall');
					    		vm.disableSubmit = false;
					    		return;
				            }
				            if(data == 'NOT_ACCEPTABLE') {
				            	toastr.error("Công trình đã thuộc hợp đồng đầu ra khác!");
				            	$('#testSubmitInvoice').removeClass('loadersmall');
					    		vm.disableSubmit = false;
				            }
				            else if(!!data.error){
				            	$('#testSubmitInvoice').removeClass('loadersmall');
					    		vm.disableSubmit = false;
				            		toastr.error(data.error);
				            }
				            else if(data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length>0){
				            		vm.lstErrImport = data[data.length - 1].errorList;
				            		vm.objectErr = data[data.length - 1];
				            		var templateUrl="wms/createImportRequirementManagement/importResultPopUp.html";
				       			 	var title="Kết quả Import";
				       			 	var windowId="ERR_IMPORT";
				       			 $('#testSubmitInvoice').removeClass('loadersmall');
						    		vm.disableSubmit = false;
				       			 	CommonService.populatePopupCreate(templateUrl,title,vm.lstErrImport,vm,windowId,false,'80%','420px');
				       			 	fillDataImportErrTable(vm.lstErrImport);
				            	}
				            	else{
				            		$('#testSubmitInvoice').removeClass('loadersmall');
						    		vm.disableSubmit = false;
				            		toastr.success("Import thành công")
				            		$("#manageDataOSGrid").data('kendoGrid').dataSource.read();
				       			 	$("#manageDataOSGrid").data('kendoGrid').refresh();
				       			 	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
				            	}
				            }
				        });
	     }
	        vm.exportExcelErr = function(){
	        	manageDataOSService.downloadErrorExcel(vm.objectErr).then(function(d) {
					data = d.plain();
					window.location = Constant.BASE_SERVICE_URL+"manageDataOutsideOsService/downloadFileTempATTT?" + data.fileName;
				}).catch( function(){
					toastr.error(gettextCatalog.getString("Lỗi khi export!"));
					return;
				});
			}
        vm.closeErrImportPopUp = closeErrImportPopUp;
		function closeErrImportPopUp(){
			$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
		}

        function fillDataImportErrTable(data) {
			vm.importGoodResultGridOptions = kendoConfig.getGridOptions({
				autoBind: true,
				resizable: true,
				dataSource: data,
				noRecords: true,
				columnMenu: false,
				messages: {
					noRecords : gettextCatalog.getString("Không có kết quả hiển thị")
				},
				pageSize:10,
				pageable: {
					pageSize:10,
					refresh: false,
					 pageSizes: [10, 15, 20, 25],
					messages: {
		                display: "{0}-{1} của {2} kết quả",
		                itemsPerPage: "kết quả/trang",
		                empty: "Không có kết quả hiển thị"
		            }
				},
				columns: [
				{
					title: "TT",
					field:"stt",
			        template: dataItem => $("#importGoodResultGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
			        width: 70,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				},  {
					title: "Dòng lỗi",
					field: 'lineError',
			        width: 100,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}, {
					title: "Cột lỗi",
			        field: 'columnError',
			        width: 100,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}, {
					title: "Nội dung lỗi",
			        field: 'detailError',
			        width: 250,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}
				]
			});
		}
// end controller
       }
})();
