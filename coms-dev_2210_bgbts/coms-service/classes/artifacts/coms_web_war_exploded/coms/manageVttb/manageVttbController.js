(function() {
	'use strict';
	var controllerId = 'manageVttbController';
	
	angular.module('MetronicApp').controller(controllerId, manageVttbController);
	
	function manageVttbController($scope, $rootScope, gettextCatalog, 
			kendoConfig, manageVttbService,
			CommonService,htmlCommonService, Restangular, RestEndpoint, Constant, $timeout) {
		var vm = this;
		vm.cntContractSearch = {};
		vm.showSearch = true;
        vm.searchForm = {};
        vm.addForm = {};
        vm.isCreate = false;
        vm.provinceList = [];
        vm.String = "Quản lý công trình > Quản lý VTTB đã nhận, đang đưa vào thi công";
        init();
        function init(){
        	//
        	var endDate = new Date();
        	var previousMonth = new Date();
        	previousMonth.setMonth(previousMonth.getMonth()-1);
        	vm.searchForm.startDate = htmlCommonService.formatDate(previousMonth);
        	vm.searchForm.endDate = htmlCommonService.formatDate(endDate);
        	//
        	
        	fillDataTable([]);
        	loadProvinceList();
        	vm.statusFormList = [
    			{
    				code:"",
    				name: "Tất cả"
    			},
    			{
    				code:1,
    				name: "Chờ bàn giao mặt bằng"
    			},
    			{
    				code:2,
    				name: "Chờ khởi công"
    			},
    			{
    				code:3,
    				name: "Đang thực hiện"
    			},
    			{
    				code:4,
    				name: "Đã tạm dừng"
    			},
    			{
    				code:5,
    				name: "Đã hoàn thành"
    			},
    			{
    				code:6,
    				name: "Đã nghiệm thu"
    			},
    			{
    				code:7,
    				name: "Đã hoàn công"
    			},
    			{
    				code:8,
    				name: "Đã quyết toán"
    			},
    			{
    				code:0,
    				name: "Đã hủy"
    			}
    		
    			];
        	vm.contractTypeList = [
    			{name: "BTS", code: 1},
    			{name: "Chi phí", code: 2},
    			{name: "HĐ 12 đầu việc", code: 3},
    			{name: "GPON", code: 4},
    			{name: "Đầu tư khác", code: 5},
    			{name: "Tết AL", code: 6},
    			{name: "CV lẻ", code: 7},
    			{name: "Truyền dẫn BTS", code: 8},
    			{name: "CQ - Cáp quang", code: 9},
    			{name: "Cột bao", code: 10}];
        	
        	vm.listRemove=[
	               {title: "Thao tác"},
	        	   ]
        	vm.listConvert=[
	                {
	        			field:"signState",
	        			data:{
	        				1: 'Chưa trình ký',
	        				2: 'Đang trình ký',
	        				3: 'Đã ký',
	        				4: "Từ chối ký"
	        			}
	        		},
	        		{
	        			field:"status",
	        			data:{
	        				0: 'Chậm tiến độ',
	        				1: 'Đúng tiến độ'
	        			}
	        		},
	        		{
	        			field:"contractType",
	        			data:{
	        				1: 'Hợp đồng xây lắp đầu vào',
	        				2: 'Thương mại đầu ra',
	        				3: 'Thương mại đầu vào',
	        				4: 'Hợp đồng vật tư',
	        				5: 'Hợp đồng vật tư',
	        				6: 'Hợp đồng vật tư',
	        				7: 'Hạ tâng cho thuê đầu ra',
	        				8: 'Hạ tầng cho thuê đầu vào',
	        				9: 'Xí nghiệp xây dựng',
	        				0:'Hợp đồng xây lắp đầu ra'
	        			}
	        		},
	        		{
	        			field:"constructionStatus",
	        			data:{
	        				0: 'Đã hủy',
	        				1: 'Chờ bàn giao mặt bằng',
	        				2: 'Chờ khởi công',
	        				3: 'Đang thực hiện',
	        				4: 'Đã tạm dừng',
	        				5: 'Đã hoàn thành',
	        				6: 'Đã nghiệm thu',
	        				7: 'Đã hoàn công',
	        				8: 'Đã quyết toán'
	        			}
	        		},
	        		]
        	
        	
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
        // tatph -end 13/11/2019
        
        var record = 0;
        function fillDataTable(data){
        	kendo.ui.progress($("#manageVttbGridId"), true);
        	vm.manageVttbGridOptions = kendoConfig.getGridOptions({
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
                              '<div class="btn-group pull-right margin_top_button margin_right10">'+
                              '<i data-toggle="dropdown" tooltip-placement="left" uib-tooltip="Cài đặt" aria-expanded="false"><i class="fa fa-cog" aria-hidden="true"></i></i>'+
                              '<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="vm.exportFile()" aria-hidden="true"></i>' +
                              '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
                              '<label ng-repeat="column in vm.manageVttbGrid.columns| filter: vm.gridColumnShowHideFilter">'+
                              '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+
                              '</label>'+
                              '</div></div>'
                          }
                          ],
    			dataSource:{
    				serverPaging: true,
    				 schema: {
    					 total: function (response) {
    						    $("#countManageVttb").text(""+response.total);
    							return response.total; 
    						},
    						data: function (response) {	
    						kendo.ui.progress($("#manageVttbGridId"), false);
    						return response.data; 
    						},
    	                },
    				transport: {
    					read: {
    	                        // Thuc hien viec goi service
    						url: Constant.BASE_SERVICE_URL + "manageVttbService/doSearch",
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
    				width: '5%',
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
    			// //////////////////////

    			// /////////////////////
    				
    			// //////////////////////
    			{
    				title: "Mã công trình",
    		        field: 'constructionCode',
    		        width: '5%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    			},
    			{
    				title: "Mã trạm",
    		        field: 'stationCode',
    		        width: '5%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    			},
    			{
    				title: "Mã hợp đồng",
    		        field: 'contractCode',
    		        width: '10%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    			},
    			{
    				title: "Loại hợp đồng",
    		        field: 'contractType',
    		        width: '10%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    				template: function (dataItem) {
    					if(dataItem.contractType == 1){
    						return "Hợp đồng xây lắp đầu vào";
    					}else if(dataItem.contractType == 2){
    						return "Thương mại đầu ra";
    					}else if(dataItem.contractType == 3){
    						return "Thương mại đầu vào";
    					}else if(dataItem.contractType == 4){
    						return "Hợp đồng vật tư";
    					}else if(dataItem.contractType == 5){
    						return "Hợp đồng dịch vụ";
    					}else if(dataItem.contractType == 6){
    						return "Hợp đồng khung";
    					}else if(dataItem.contractType == 7){
    						return "Hạ tâng cho thuê đầu ra";
    					}else if(dataItem.contractType == 8){
    						return "Hạ tầng cho thuê đầu vào";
    					}else if(dataItem.contractType == 9){
    						return "Xí nghiệp xây dựng";
    					}else if(dataItem.contractType == 0){
    						return "Hợp đồng xây lắp đầu ra";
    					}else{
    						return "";
    					}
                        
                    }
    			},
    			{
    				title: "Mã tỉnh",
    		        field: 'provinceCode',
    		        width: '5%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    			},
    			{
    				title: "Mã PXK",
    		        field: 'pxkCode',
    		        width: '10%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    			},
    			{
    				title: "Giá trị vật tư đã nhận",
    		        field: 'vttbValue',
    		        width: '10%',
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
    				title: "Trạng thái công trình",
    		        field: 'constructionStatus',
    		        width: '10%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    				template: function (dataItem) {
    					if(dataItem.constructionStatus == 1){
    						return "Chờ bàn giao mặt bằng";
    					}else if(dataItem.constructionStatus == 2){
    						return "Chờ khởi công";
    					}else if(dataItem.constructionStatus == 3){
    						return "Đang thực hiện";
    					}else if(dataItem.constructionStatus == 4){
    						return "Đã tạm dừng";
    					}else if(dataItem.constructionStatus == 5){
    						return "Đã hoàn thành";
    					}else if(dataItem.constructionStatus == 6){
    						return "Đã nghiệm thu";
    					}else if(dataItem.constructionStatus == 7){
    						return "Đã hoàn công";
    					}else if(dataItem.constructionStatus == 8){
    						return "Đã quyết toán";
    					}else if(dataItem.constructionStatus == 0){
    						return "Đã hủy";
    					}else{
    						return "";
    					}
    				}
    			}
    			
    			]
    		});
        }
        
        vm.cancelInput = function(data){
        	if(data=='status'){
        		vm.searchForm.status = null;
        	}
        	if(data=='keySearch'){
        		vm.searchForm.keySearch = null;
        	}
        }
        
        
        vm.cancel = function(){
        	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }
        
        vm.closeErrImportPopUp = function(){
        	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }
        
        vm.doSearch = doSearch;
        function doSearch(){
        	if(vm.searchForm.startDate =='' || vm.searchForm.endDate == ''){
        		toastr.error(gettextCatalog.getString("Từ ngày ,  đến ngày bắt buộc nhập"));
        		$('#startDate').focus();
        		return;
        	}
        	var grid = vm.manageVttbGrid;
        	if(grid){
        		grid.dataSource.query({
    				page: 1,
    				pageSize: 10
    			});
        	}
        }
        

     
        
        
        
        
       
     
    
        

		
        
        
		 
		 vm.exportFile = function () {
             // close popup and refresh table list
		 	vm.cntContractSearch.page = null;
			vm.cntContractSearch.pageSize = null;
			var data = manageVttbService.doSearch(vm.cntContractSearch);
			console.log(data);
			manageVttbService.doSearch(vm.cntContractSearch).then(function(d){
				CommonService.exportFile(vm.manageVttbGrid,d.data,vm.listRemove,vm.listConvert,"Danh sách VTTB đã nhận , đang đưa vào thi công");
			});
        }
		 
// end controller
       }
})();