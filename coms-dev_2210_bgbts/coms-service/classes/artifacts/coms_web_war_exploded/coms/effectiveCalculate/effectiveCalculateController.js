(function() {
	'use strict';
	var controllerId = 'effectiveCalculateController';

	angular.module('MetronicApp').controller(controllerId,
			effectiveCalculateController);

	function effectiveCalculateController($scope, $templateCache,$rootScope,
			gettextCatalog, $kWindow, kendoConfig, effectiveCalculateService,
			CommonService, htmlCommonService,PopupConst, Restangular, RestEndpoint,
			Constant, $timeout) { 
		var vm = this;
		vm.showSearch = true;
		vm.String = "Quản lý công trình > Quản lý kế hoạch > Tính toán hiệu quả"
		init();
		vm.searchForm = {};
		vm.searchFormDas = {};
		vm.searchFormDasCapex = {};
		vm.value = null;
		vm.folder='';
        function init() {
            vm.option1Show = true;
//            vm.value=1;
//            chooseTab(1);
        }
        
        vm.initClick = initClick;
        function initClick(){
        	chooseTab(1);
        }
//        vm.initClickDas = initClickDas;
//        function initClickDas(){
//        	chooseTabDAS(10);
//        }
        
        var record = 0;
        vm.chooseTab = chooseTab;
        
        function chooseTab(e){
        	vm.value = e;
        	if(e == 1){
        		vm.targetGridOptions1 = kendoConfig.getGridOptions({
        			autoBind : true,
        			scrollable: false,
                    resizable: true,
                    editable: false,
                    dataBinding: function(){
                    	record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    reorderable: true,
                    toolbar: [
                    	{
                    		name: "actions",
                    		template: 
                    			'<div>'+
                    			'<div class="btn-group pull-right margin_top_button margin_right10">'+
                    			'<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="vm.exportFile1()" aria-hidden="true"></i>' +
                    			'</div>'
                    	}
                    ],
                    dataSource: {
                    	serverPaging: true,
                    	schema: {
                    		total: function(response){
                    			return response.total;
                    		},
                    		data: function(response){
                    			return response.data;
                    		},
                    	},
                    	transport: {
                    		read: {
                    			url: Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/getCapexSource",
                    			contentType: "application/json; charset=utf-8",
                                type: "POST"
                    		},
                    		parameterMap: function (options, type){
                    			vm.searchForm.page = options.page
                    			vm.searchForm.pageSize = options.pageSize
                    			return JSON.stringify(vm.searchForm)
                    		}
                    	},
                    	pageSize: 10
                    },
                    
                    
                    noRecords: true,
//                    save: function(){
//                    	var grid = vm.capexSource;
//                    	setTimeout(function(){
//                    		grid.refresh();
//                    	})
//                    },
                    columnMenu: false,
                    messages: {
                    	 noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>") 
                    },
                    pageable: {
                    	refresh: false,
                    	pageSize: 10,
                    	pageSizes: [10,15,20,25],
                    	messages:{
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
                    	},
                    	{
                   		    title: "Nội dung",
                            field: "contentCapex",
                            width: '5%',
                            headerAttributes: {
                            	style: "text-align:center;font-weight: bold;",
                            	translate:"",
                            },
                            attributes: {
                            		style: "text-align:center;"
                            },
                    	},
                    	{
                    		   title: "Đơn vị tính (Đồng)",
                               field: "unit",
                               width: '5%',
                               headerAttributes: {
                            	   style: "text-align:center;font-weight: bold;",
                            	   translate:"",
                               },
                               attributes: {
                                   style: "text-align:center;"
                               },
                               editable: false
                       	},
                       	{
                      		 title: "Giá trị (Đơn vị: Đồng)",
                               field: "costCapex",
                               width: '5%',
                               columnMenu: false,
                               headerAttributes: {
                            	   style: "text-align:center;font-weight: bold;",
                            	   translate:"",	
                            },
                               attributes: {
                                   style: "text-align:center;"
                               },
//                               format: "{0:n0}"
                       	},
                    	
                    ]
        		})
        	}
        	if(e==2){
        		vm.targetGridOptions2 = kendoConfig.getGridOptions({
        			autoBind : true,
        			scrollable: false,
                    resizable: true,
                    editable: false,
                    dataBinding: function(){
                    	record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    reorderable: true,
                    toolbar: [
                    	{
                    		name: "actions",
                    		template: 
                    			'<div>'+
                    			'<div class="btn-group pull-right margin_top_button margin_right10">'+
                    			'<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="vm.exportFile2()" aria-hidden="true"></i>' +
                    			'</div>'
                    	}
                    ],
                    dataSource: {
                    	serverPaging: true,
                    	schema: {
                    		total: function(response){
                    			return response.total;
                    		},
                    		data: function(response){
                    			return response.data;
                    		},
                    	},
                    	transport: {
                    		read: {
                    			url: Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/getData2",
                    			contentType: "application/json; charset=utf-8",
                                type: "POST"
                    		},
                    		parameterMap: function (options, type){
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
                    	 noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>") 
                    },
                    pageable: {
                    	refresh: false,
                    	pageSize: 10,
                    	pageSizes: [10,15,20,25],
                    	messages:{
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
                    	},
                    	{
                   		    title: "Danh mục",
                            field: "waccName",
                            width: '5%',
                            headerAttributes: {
                            	style: "text-align:center;font-weight: bold;",
//                            	translate:"",
                            },
                            attributes: {
                            		style: "text-align:center;"
                            },
                    	},
                    	{
                    		   title: "Tỷ lệ (%)",
                               field: "waccRex",
                               width: '5%',
                               headerAttributes: {
                            	   style: "text-align:center;font-weight: bold;",
//                            	   translate:"",
                               },
                               attributes: {
                                   style: "text-align:center;"
                               },
//                               format: "{0:n0}"
                       	},
                       	
                    	
                    ]
        		})
        	}
        	if(e==3){
        		vm.targetGridOptions3 = kendoConfig.getGridOptions({
        			autoBind : true,
        			scrollable: false,
                    resizable: true,
                    editable: false,
                    dataBinding: function(){
                    	record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    reorderable: true,
                    toolbar: [
                    	{
                    		name: "actions",
                    		template: 
                    			'<div>'+
                    			'<div class="btn-group pull-right margin_top_button margin_right10">'+
                    			'<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="vm.exportFile3()" aria-hidden="true"></i>' +
                    			'</div>'
                    	}
                    ],
                    dataSource: {
                    	serverPaging: true,
                    	schema: {
                    		total: function(response){
                    			return response.total;
                    		},
                    		data: function(response){
                    			return response.data;
                    		},
                    	},
                    	transport: {
                    		read: {
                    			url: Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/getData3",
                    			contentType: "application/json; charset=utf-8",
                                type: "POST"
                    		},
                    		parameterMap: function (options, type){
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
                    	 noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>") 
                    },
                    pageable: {
                    	refresh: false,
                    	pageSize: 10,
                    	pageSizes: [10,15,20,25],
                    	messages:{
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
                    	},
                    	{
                   		    title: "Tỉnh",
                            field: "catProvinceCode",
                            width: '5%',
                            headerAttributes: {
                            	style: "text-align:center;font-weight: bold;",
//                            	translate:"",
                            },
                            attributes: {
                            		style: "text-align:center;"
                            },
                    	},
                    	{
            				title: "Giá trị giao khoán TCTT trạm Macro",
            		        width: '10%',
            		        headerAttributes: {
            		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//            					translate:"",
            				},
            				attributes: {
            					style: "text-align:center;white-space: normal;"
            				},
            				columns: [{
                				title: "Đồng bằng (%)",
                				field:"costDeliveryBts",
                				width: '5%',
                		        columnMenu: false,
                		        headerAttributes: {
                		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//                					translate:"",
                				},
                				attributes: {
                					style: "text-align:center;white-space: normal;"
                				},
//                				format: "{0:n0}"
                			},

                			{
                				title: "Miền núi (%)",
                		        field: 'costMountainsBts',
                		        width: '5%',
                		        headerAttributes: {
                		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//                					translate:"",
                				},
                				attributes: {
                					style: "text-align:center;white-space: normal;"
                				},
//                				format: "{0:n0}"
                			},
                			{
                				title: "Trên mái (%)",
                				field:"costRoofBts",
                				width: '5%',
                		        columnMenu: false,
                		        headerAttributes: {
                		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//                					translate:"",
                				},
                				attributes: {
                					style: "text-align:center;white-space: normal;"
                				},
//                				format: "{0:n0}"
                			}]
            				
            			},
                    	{

            				title: "Giá trị giao khoán TCTT trạm RRU",
            		        width: '10%',
            		        headerAttributes: {
            		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//            					translate:"",
            				},
            				attributes: {
            					style: "text-align:center;white-space: normal;"
            				},
            				columns: [{
                				title: "Đồng bằng (%)",
                				field:"costDeliveryPru",
                				width: '5%',
                		        columnMenu: false,
                		        headerAttributes: {
                		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//                					translate:"",
                				},
                				attributes: {
                					style: "text-align:center;white-space: normal;"
                				},
//                				format: "{0:n0}"
                			},

                			{
                				title: "Miền núi (%)",
                		        field: 'costMountainsPru',
                		        width: '5%',
                		        headerAttributes: {
                		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//                					translate:"",
                				},
                				attributes: {
                					style: "text-align:center;white-space: normal;"
                				},
//                				format: "{0:n0}"
                			},
                			{
                				title: "Trên mái (%)",
                				field:"costRoofPru",
                				width: '5%',
                		        columnMenu: false,
                		        headerAttributes: {
                		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//                					translate:"",
                				},
                				attributes: {
                					style: "text-align:center;white-space: normal;"
                				},
//                				format: "{0:n0}"
                			}]
            				
                    	},
                    	{

            				title: "Giá trị giao khoán TCTT trạm Smallcell",
            		        width: '10%',
            		        headerAttributes: {
            		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//            					translate:"",
            				},
            				attributes: {
            					style: "text-align:center;white-space: normal;"
            				},
            				columns: [{
                				title: "Đồng bằng (%)",
                				field:"costDeliverySmallcell",
                				width: '5%',
                		        columnMenu: false,
                		        headerAttributes: {
                		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//                					translate:"",
                				},
                				attributes: {
                					style: "text-align:center;white-space: normal;"
                				},
//                				format: "{0:n0}"
                			},

                			{
                				title: "Miền núi (%)",
                		        field: 'costMountainsSmallcell',
                		        width: '5%',
                		        headerAttributes: {
                		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//                					translate:"",
                				},
                				attributes: {
                					style: "text-align:center;white-space: normal;"
                				},
//                				format: "{0:n0}"
                			},
                			{
                				title: "Trên mái (%)",
                				field:"costRoofSmallcell",
                				width: '5%',
                		        columnMenu: false,
                		        headerAttributes: {
                		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//                					translate:"",
                				},
                				attributes: {
                					style: "text-align:center;white-space: normal;"
                				},
//                				format: "{0:n0}"
                			}]
            				
                    	}	
                    ]
        		})
        	}
        	if(e==4){
        		vm.targetGridOptions4 = kendoConfig.getGridOptions({
        			autoBind : true,
        			scrollable: false,
                    resizable: true,
                    editable: false,
                    dataBinding: function(){
                    	record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    reorderable: true,
                    toolbar: [
                    	{
                    		name: "actions",
                    		template: 
                    			'<div>'+
                    			'<div class="btn-group pull-right margin_top_button margin_right10">'+
                    			'<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="vm.exportFile4()" aria-hidden="true"></i>' +
                    			'</div>'
                    	}
                    ],
                    dataSource: {
                    	serverPaging: true,
                    	schema: {
                    		total: function(response){
                    			return response.total;
                    		},
                    		data: function(response){
                    			return response.data;
                    		},
                    	},
                    	transport: {
                    		read: {
                    			url: Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/getData4",
                    			contentType: "application/json; charset=utf-8",
                                type: "POST"
                    		},
                    		parameterMap: function (options, type){
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
                    	 noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>") 
                    },
                    pageable: {
                    	refresh: false,
                    	pageSize: 10,
                    	pageSizes: [10,15,20,25],
                    	messages:{
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
                    	},
                    	{
                   		    title: "Tỉnh",
                            field: "provinceCode",
                            width: '5%',
                            headerAttributes: {
                            	style: "text-align:center;font-weight: bold;",
//                            	translate:"",
                            },
                            attributes: {
                            		style: "text-align:center;"
                            },
                    	},
                    	{
                    		   title: "Số lượng trạm",
                               field: "amountBts",
                               width: '5%',
                               headerAttributes: {
                            	   style: "text-align:center;font-weight: bold;",
//                            	   translate:"",
                               },
                               attributes: {
                                   style: "text-align:center;"
                               },
                               editable: false
                       	},
                       	{
                 		   title: "Chi phí nhân công giải phóng mặt bằng (Đơn vị: Đồng)",
                            field: "costGpmb",
                            width: '5%',
                            headerAttributes: {
                         	   style: "text-align:center;font-weight: bold;",
//                         	   translate:"",
                            },
                            attributes: {
                                style: "text-align:center;"
                            },
//                            format: "{0:n0}"
                    	},
                    	{
                 		   title: "Chi phí nhân công đấu nối điện (Đơn vị: Đồng)",
                            field: "costNcdn",
                            width: '5%',
                            headerAttributes: {
                         	   style: "text-align:center;font-weight: bold;",
//                         	   translate:"",
                            },
                            attributes: {
                                style: "text-align:center;"
                            },
//                            format: "{0:n0}"
                    	},
                    	
                    ]
        		})
        		
        	}
        	if(e==5){
        		vm.targetGridOptions5 = kendoConfig.getGridOptions({
        			autoBind : true,
        			scrollable: false,
                    resizable: true,
                    editable: false,
                    dataBinding: function(){
                    	record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    reorderable: true,
                    toolbar: [
                    	{
                    		name: "actions",
                    		template: 
                    			'<div>'+
                    			'<div class="btn-group pull-right margin_top_button margin_right10">'+
                    			'<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="vm.exportFile5()" aria-hidden="true"></i>' +
                    			'</div>'
                    	}
                    ],
                    dataSource: {
                    	serverPaging: true,
                    	schema: {
                    		total: function(response){
                    			return response.total;
                    		},
                    		data: function(response){
                    			return response.data;
                    		},
                    	},
                    	transport: {
                    		read: {
                    			url: Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/getData5",
                    			contentType: "application/json; charset=utf-8",
                                type: "POST"
                    		},
                    		parameterMap: function (options, type){
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
                    	 noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>") 
                    },
                    pageable: {
                    	refresh: false,
                    	pageSize: 10,
                    	pageSizes: [10,15,20,25],
                    	messages:{
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
                    	},
                    	{
                   		    title: "Loại hình đơn giá",
                            width: '5%',
                            headerAttributes: {
                            	style: "text-align:center;font-weight: bold;",
//                            	translate:"",
                            },
                            attributes: {
                            		style: "text-align:center;"
                            },
                            columns:[{
                				title: "Loại trạm",
                				field:"stationType",
                				width: '5%',
                		        columnMenu: false,
                		        headerAttributes: {
                		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//                					translate:"",
                				},
                				attributes: {
                					style: "text-align:center;white-space: normal;"
                				},
                            	
                            }]
                    	},
                    	{	
            				title: "Chỉ có cột - Chưa bao gồm",
            		        width: '10%',
            		        headerAttributes: {
            		        	style: "text-align:center; font-weight: bold;white-space: normal;",
            					translate:"",
            				},
            				attributes: {
            					style: "text-align:center;white-space: normal;"
            				},
            				columns: [{
                				title: "HNI,HCM (Đơn vị: Đồng)",
                				field:"notSourceHniHcm",
                				width: '5%',
                		        columnMenu: false,
                		        headerAttributes: {
                		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//                					translate:"",
                				},
                				attributes: {
                					style: "text-align:center;white-space: normal;"
                				},
//                				format: "{0:n0}"
                			},

                			{
                				title: "61 Tỉnh/TP khác (Đơn vị: Đồng)",
                		        field: 'notSource61Province',
                		        width: '5%',
                		        headerAttributes: {
                		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//                					translate:"",
                				},
                				attributes: {
                					style: "text-align:center;white-space: normal;"
                				},
//                				format: "{0:n0}"
                			}]
            				
            			},
                    	{

            				title: "Cột + hệ thống nguồn - chưa nhiên liệu/điện",
            		        width: '10%',
            		        headerAttributes: {
            		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//            					translate:"",
            				},
            				attributes: {
            					style: "text-align:center;white-space: normal;"
            				},
            				columns: [{
                				title: "HNI,HCM (Đơn vị: Đồng)",
                				field:"sourceHniHcm",
                				width: '5%',
                		        columnMenu: false,
                		        headerAttributes: {
                		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//                					translate:"",
                				},
                				attributes: {
                					style: "text-align:center;white-space: normal;"
                				},
//                				format: "{0:n0}"
                			},

                			{
                				title: "61 tỉnh/TP khác (Đơn vị: Đồng)",
                		        field: 'source61Province',
                		        width: '5%',
                		        headerAttributes: {
                		        	style: "text-align:center; font-weight: bold;white-space: normal;",
//                					translate:"",
                				},
                				attributes: {
                					style: "text-align:center;white-space: normal;"
                				},
//                				format: "{0:n0}"
                			}]
            				
                    	},
                    ]
        		})
        		
        	}
        	if(e==6){
        		vm.targetGridOptions6 = kendoConfig.getGridOptions({
        			autoBind : true,
        			scrollable: false,
                    resizable: true,
                    editable: false,
                    dataBinding: function(){
                    	record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    reorderable: true,
                    toolbar: [
                    	{
                    		name: "actions",
                    		template: 
                    			'<div>'+
                    			'<div class="btn-group pull-right margin_top_button margin_right10">'+
                    			'<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="vm.exportFile6()" aria-hidden="true"></i>' +
                    			'</div>'
                    	}
                    ],
                    dataSource: {
                    	serverPaging: true,
                    	schema: {
                    		total: function(response){
                    			return response.total;
                    		},
                    		data: function(response){
                    			return response.data;
                    		},
                    	},
                    	transport: {
                    		read: {
                    			url: Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/getData6",
                    			contentType: "application/json; charset=utf-8",
                                type: "POST"
                    		},
                    		parameterMap: function (options, type){
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
                    	 noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>") 
                    },
                    pageable: {
                    	refresh: false,
                    	pageSize: 10,
                    	pageSizes: [10,15,20,25],
                    	messages:{
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
                    	},
                    	{
                   		    title: "Nhóm",
                            field: "typeGroup",
                            width: '5%',
                            headerAttributes: {
                            	style: "text-align:center;font-weight: bold;",
//                            	translate:"",
                            },
                            attributes: {
                            		style: "text-align:center;"
                            },
                    	},
                    	{
                    		   title: "Địa điểm cụ thể",
                               field: "address",
                               width: '5%',
                               headerAttributes: {
                            	   style: "text-align:center;font-weight: bold;",
//                            	   translate:"",
                               },
                               attributes: {
                                   style: "text-align:center;"
                               },
                               editable: false
                       	},
                       	{
                 		   title: "Địa hình",
                            field: "topographic",
                            width: '5%',
                            headerAttributes: {
                         	   style: "text-align:center;font-weight: bold;",
//                         	   translate:"",
                            },
                            attributes: {
                                style: "text-align:center;"
                            },
//                            format: "{0:n0}"
                    	},
                    	{
                 		   title: "Loại trạm",
                            field: "stationType",
                            width: '5%',
                            headerAttributes: {
                         	   style: "text-align:center;font-weight: bold;",
//                         	   translate:"",
                            },
                            attributes: {
                                style: "text-align:center;"
                            },
//                            format: "{0:n0}"
                    	},
                    	{
                  		   title: "Giá trị (Đơn vị: Đồng)",
                             field: "cost1477",
                             width: '5%',
                             headerAttributes: {
                          	   style: "text-align:center;font-weight: bold;",
//                          	   translate:"",
                             },
                             attributes: {
                                 style: "text-align:center;"
                             },
//                             format: "{0:n0}"
                     	},
                    ]
        		})
        	}
        	if(e==7){
        		vm.targetGridOptions7 = kendoConfig.getGridOptions({
        			autoBind : true,
        			scrollable: false,
                    resizable: true,
                    editable: false,
                    dataBinding: function(){
                    	record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    reorderable: true,
                    toolbar: [
                    	{
                    		name: "actions",
                    		template: 
                    			'<div>'+
                    			'<div class="btn-group pull-right margin_top_button margin_right10">'+
                    			'<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="vm.exportFile7()" aria-hidden="true"></i>' +
                    			'</div>'
                    	}
                    ],
                    dataSource: {
                    	serverPaging: true,
                    	schema: {
                    		total: function(response){
                    			return response.total;
                    		},
                    		data: function(response){
                    			return response.data;
                    		},
                    	},
                    	transport: {
                    		read: {
                    			url: Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/getData7",
                    			contentType: "application/json; charset=utf-8",
                                type: "POST"
                    		},
                    		parameterMap: function (options, type){
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
                    	 noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>") 
                    },
                    pageable: {
                    	refresh: false,
                    	pageSize: 10,
                    	pageSizes: [10,15,20,25],
                    	messages:{
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
                    	},
                    	{
                   		    title: "Loại hạng mục",
                            field: "itemType",
                            width: '5%',
                            headerAttributes: {
                            	style: "text-align:center;font-weight: bold;",
//                            	translate:"",
                            },
                            attributes: {
                            		style: "text-align:center;"
                            },
                    	},
                    	{
                    		   title: "Hạng mục",
                               field: "item",
                               width: '5%',
                               headerAttributes: {
                            	   style: "text-align:center;font-weight: bold;",
//                            	   translate:"",
                               },
                               attributes: {
                                   style: "text-align:center;"
                               },
                               editable: false
                       	},
                       	{
                 		   title: "Công việc",
                            field: "workCapex",
                            width: '5%',
                            headerAttributes: {
                         	   style: "text-align:center;font-weight: bold;",
//                         	   translate:"",
                            },
                            attributes: {
                                style: "text-align:center;"
                            },
//                            format: "{0:n0}"
                    	},
                    	{
                 		   title: "Tỉnh",
                            field: "provinceCode",
                            width: '5%',
                            headerAttributes: {
                         	   style: "text-align:center;font-weight: bold;",
//                         	   translate:"",
                            },
                            attributes: {
                                style: "text-align:center;"
                            },
//                            format: "{0:n0}"
                    	},
                    	{
                  		   title: "Giá trị (Đơn vị: Đồng)",
                             field: "costCapexBts",
                             width: '5%',
                             headerAttributes: {
                          	   style: "text-align:center;font-weight: bold;",
//                          	   translate:"",
                             },
                             attributes: {
                                 style: "text-align:center;"
                             },
//                             format: "{0:n0}"
                     	},
                    ]
        		})
        		
        	}
        	if(e==8){
        		vm.targetGridOptions8 = kendoConfig.getGridOptions({
        			autoBind : true,
        			scrollable: false,
                    resizable: true,
                    editable: false,
                    dataBinding: function(){
                    	record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    reorderable: true,
                    toolbar: [
                    	{
                    		name: "actions",
                    		template: 
                    			'<div>'+
                    			'<div class="btn-group pull-right margin_top_button margin_right10">'+
                    			'<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="vm.exportFile8()" aria-hidden="true"></i>' +
                    			'</div>'
                    	}
                    ],
                    dataSource: {
                    	serverPaging: true,
                    	schema: {
                    		total: function(response){
                    			return response.total;
                    		},
                    		data: function(response){
                    			return response.data;
                    		},
                    	},
                    	transport: {
                    		read: {
                    			url: Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/getData8",
                    			contentType: "application/json; charset=utf-8",
                                type: "POST"
                    		},
                    		parameterMap: function (options, type){
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
                    	 noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>") 
                    },
                    pageable: {
                    	refresh: false,
                    	pageSize: 10,
                    	pageSizes: [10,15,20,25],
                    	messages:{
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
                    	},
                    	{
                   		    title: "Danh mục",
                            field: "categoryOffer",
                            width: '5%',
                            headerAttributes: {
                            	style: "text-align:center;font-weight: bold;",
//                            	translate:"",
                            },
                            attributes: {
                            		style: "text-align:center;"
                            },
                    	},
                    	{
                    		   title: "Ký hiệu",
                               field: "symbol",
                               width: '5%',
                               headerAttributes: {
                            	   style: "text-align:center;font-weight: bold;",
//                            	   translate:"",
                               },
                               attributes: {
                                   style: "text-align:center;"
                               },
                               editable: false
                       	},
                       	{
                 		   title: "ĐVT (VNĐ, VNĐ/năm)",
                            field: "unit",
                            width: '5%',
                            headerAttributes: {
                         	   style: "text-align:center;font-weight: bold;",
//                         	   translate:"",
                            },
                            attributes: {
                                style: "text-align:center;"
                            },
//                            format: "{0:n0}"
                    	},
                    ]
        		})
        	}
        	if(e==9){
        		vm.targetGridOptions9 = kendoConfig.getGridOptions({
        			autoBind : true,
        			scrollable: false,
                    resizable: true,
                    editable: false,
                    dataBinding: function(){
                    	record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    reorderable: true,
                    toolbar: [
                    	{
                    		name: "actions",
                    		template: 
                    			'<div>'+
                    			'<div class="btn-group pull-right margin_top_button margin_right10">'+
                    			'<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="vm.exportFile9()" aria-hidden="true"></i>' +
                    			'</div>'
                    	}
                    ],
                    dataSource: {
                    	serverPaging: true,
                    	schema: {
                    		total: function(response){
                    			return response.total;
                    		},
                    		data: function(response){
                    			return response.data;
                    		},
                    	},
                    	transport: {
                    		read: {
                    			url: Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/getData9",
                    			contentType: "application/json; charset=utf-8",
                                type: "POST"
                    		},
                    		parameterMap: function (options, type){
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
                    	 noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>") 
                    },
                    pageable: {
                    	refresh: false,
                    	pageSize: 10,
                    	pageSizes: [10,15,20,25],
                    	messages:{
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
                    	},
                    	{
                   		    title: "Nội dung",
                            field: "contentCalEff",
                            width: '5%',
                            headerAttributes: {
                            	style: "text-align:center;font-weight: bold;",
//                            	translate:"",
                            },
                            attributes: {
                            		style: "text-align:center;"
                            },
                    	},
                    	{
                    		   title: "Đơn vị tính (Đồng,VNĐ,%,Năm,Đồng/Tháng)",
                               field: "unit",
                               width: '5%',
                               headerAttributes: {
                            	   style: "text-align:center;font-weight: bold;",
//                            	   translate:"",
                               },
                               attributes: {
                                   style: "text-align:center;"
                               },
                               editable: false
                       	},
                       	{
                 		   title: "Giá trị ",
                            field: "costCalEff",
                            width: '5%',
                            headerAttributes: {
                         	   style: "text-align:center;font-weight: bold;",
//                         	   translate:"",
                            },
                            attributes: {
                                style: "text-align:center;"
                            },
//                            format: "{0:n0}"
                    	},
                    	{
                  		   title: "Không nguồn",
                             field: "costNotSource",
                             width: '5%',
                             headerAttributes: {
                          	   style: "text-align:center;font-weight: bold;",
//                          	   translate:"",
                             },
                             attributes: {
                                 style: "text-align:center;"
                             },
//                             format: "{0:n0}"
                     	},
                     	{
                  		   title: "Có nguồn",
                             field: "costSource",
                             width: '5%',
                             headerAttributes: {
                          	   style: "text-align:center;font-weight: bold;",
//                          	   translate:"",
                             },
                             attributes: {
                                 style: "text-align:center;"
                             },
//                             format: "{0:n0}"
                     	},
                    ]
        		})
        	}
        	
        	
        }
        //PhucPV start 08072020 
        vm.chooseTabDAS = chooseTabDAS;
        
        function chooseTabDAS(e){
        	vm.value = e;
        	if(e == 10){        		
        		vm.targetGridOptions10 = kendoConfig.getGridOptions({
        			autoBind : true,
        			scrollable: false,
                    resizable: true,
                    editable: false,
                    dataBinding: function(){
                    	record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    dataSource: {
                    	serverPaging: true,
                    	schema: {
                    		total: function(response){
                    			return response.total;
                    		},
                    		data: function(response){
                    			return response.data;
                    		},
                    	},
                    	transport: {
                    		read: {
                    			url: Constant.BASE_SERVICE_URL + "effectiveCalculateDasRestService/getAssumptions",
                    			contentType: "application/json; charset=utf-8",
                                type: "POST"
                    		},
                    		parameterMap: function (options, type){
                    			vm.searchFormDas.page = options.page
                    			vm.searchFormDas.pageSize = options.pageSize
                    			return JSON.stringify(vm.searchFormDas)
                    		}
                    	},
                    	pageSize: 10,
                    },
                    reorderable: true,
                    toolbar: [
                    	{
                    		name: "actions",
                    		template: 
                    			'<div class="btn-group pull-right margin_top_button margin10">'+
                              	'<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'+
                              	'<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFileDas()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>'+
        	                    '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
        	                    '<label ng-repeat="column in vm.targetGrid10.columns| filter: vm.gridColumnShowHideFilter">'+
        	                    '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumnDas(column)"> {{column.title}}'+
        	                    '</label>'+                    
        	                    '</div></div>'
                    	}
                    ],
                    
                    noRecords: true,
                    save: function(){
                    	var grid = this;
                    	setTimeout(function(){
                    		grid.refresh();
                    	})
                    },
                    columnMenu: false,
                    messages: {
                    	 noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>") 
                    },
                    pageable: {
                    	refresh: false,                 	
                    	pageSizes: [10,15,20,25],
                    	messages:{
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
                             editable: false
                    	},
                    	{
                   		 title: "Nội dung",
                            field: "contentAssumptions",
                            width: '5%',
                            columnMenu: false,
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:center;"
                            },
                            editable: false
                    	},
                    	{
                      		 title: "Đơn vị tính",
                               field: "unit",
                               width: '5%',
                               columnMenu: false,
                               headerAttributes: {style: "text-align:center;font-weight: bold;"},
                               attributes: {
                                   style: "text-align:center;"
                               },
                               editable: false
                       	},
                       	{
                      		 title: "Giá trị",
                               field: "costAssumptions",
                               width: '5%',
                               columnMenu: false,
                               headerAttributes: {style: "text-align:center;font-weight: bold;"},
                               attributes: {
                                   style: "text-align:center;"
                               },
                               editable: false
                       	},
                       	{
                     		 title: "Ghi chú",
                              field: "noteAssumptions",
                              width: '5%',
                              columnMenu: false,
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:center;"
                              },
                              editable: false
                      	},
                    	
                    ]
        		})
        	}
        	
        	if(e == 11){
        		vm.targetGridOptions11 = kendoConfig.getGridOptions({
        			autoBind : true,
        			scrollable: false,
                    resizable: true,
                    editable: false,
                    dataBinding: function(){
                    	record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                    },
                    dataSource: {
                    	serverPaging: true,
                    	schema: {
                    		total: function(response){
                    			return response.total;
                    		},
                    		data: function(response){
                    			return response.data;
                    		},
                    	},
                    	transport: {
                    		read: {
                    			url: Constant.BASE_SERVICE_URL + "effectiveCalculateDasCapexRestService/getAssumptionsCapex",
                    			contentType: "application/json; charset=utf-8",
                                type: "POST"
                    		},
                    		parameterMap: function (options, type){
                    			vm.searchFormDasCapex.page = options.page
                    			vm.searchFormDasCapex.pageSize = options.pageSize
                    			return JSON.stringify(vm.searchFormDasCapex)
                    		}
                    	},
                    	pageSize: 10
                    },
                    reorderable: true,
                    toolbar: [
                    	{
                    		name: "actions",
                    		template: 
                    			'<div class="btn-group pull-right margin_top_button margin10">'+
                              	'<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'+
                              	'<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFileDasCapex()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>'+
        	                    '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
        	                    '<label ng-repeat="column in vm.targetGrid11.columns| filter: vm.gridColumnShowHideFilter">'+
        	                    '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumnDasCapex(column)"> {{column.title}}'+
        	                    '</label>'+                    
        	                    '</div></div>'
                    	}
                    ],
                    
                    noRecords: true,
                    save: function(){
                    	var grid = this;
                    	setTimeout(function(){
                    		grid.refresh();
                    	})
                    },
                    columnMenu: false,
                    messages: {
                    	 noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>") 
                    },
                    pageable: {
                    	refresh: false,
                    	pageSize: 10,
                    	pageSizes: [10,15,20,25],
                    	messages:{
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
                             editable: false
                    	},
                    	{
                   		 title: "Loại hạng mục",
                            field: "itemType",                         
                            width: '5%',
                            columnMenu: false,
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:center;"
                            },
                            editable: false
                    	},
                    	{
                      		 title: "Hạng mục",
                               field: "item",                             
                               width: '5%',
                               columnMenu: false,
                               headerAttributes: {style: "text-align:center;font-weight: bold;"},
                               attributes: {
                                   style: "text-align:center;"
                               },
                               editable: false
                       	},
                       	{
                      		 title: "ĐVT",
                               field: "unit",                           
                               width: '5%',
                               columnMenu: false,
                               headerAttributes: {style: "text-align:center;font-weight: bold;"},
                               attributes: {
                                   style: "text-align:center;"
                               },
                               editable: false
                       	},                  	
                    	{
                     		 title: "Đơn giá",
                              field: "cost",                             
                              width: '5%',
                              columnMenu: false,
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:center;"
                              },
                              editable: false
                      	},                     
                     	{
                    		 title: "Ghi chú",
                             field: "note",                            
                             width: '5%',
                             columnMenu: false,
                             headerAttributes: {style: "text-align:center;font-weight: bold;"},
                             attributes: {
                                 style: "text-align:center;"
                             },
                             editable: false
                     	},
                    	
                    ]
        		})
        	}	
        	
        }
        
        vm.exportFileDas = function exportFileDas() {			
			vm.searchForm.page = null;
			vm.searchForm.pageSize = null;			
			 effectiveCalculateService.getAssumptions(vm.searchForm).then(function(d){		
				console.log(d.data);
				CommonService.exportFile(vm.targetGrid10, d.data,"","","Danh sách gia dinh");
			});	
		}
        
        vm.exportFileDasCapex = function exportFileDasCapex() {			
			vm.searchForm.page = null;
			vm.searchForm.pageSize = null;			
			 effectiveCalculateService.getAssumptionsCapex(vm.searchForm).then(function(d){		
				console.log(d.data);
				CommonService.exportFile(vm.targetGrid11, d.data,"","","Danh sách gia dinh capex");
			});		
		}
        
        vm.showHideColumnDas=function(column){
        	if (angular.isUndefined(column.hidden)) {
        		vm.targetGrid10.hideColumn(column);
            } else if (column.hidden) {
            	vm.targetGrid10.showColumn(column);
            } else {
            	vm.targetGrid10.hideColumn(column);
            }    	       	
        }
        
        vm.showHideColumnDasCapex=function(column){
        	if (angular.isUndefined(column.hidden)) {
        		vm.targetGrid11.hideColumn(column);
            } else if (column.hidden) {
            	vm.targetGrid11.showColumn(column);
            } else {
            	vm.targetGrid11.hideColumn(column);
            }      	
        }
        
        vm.importFileDAS = function(){
        	 var teamplateUrl="coms/effectiveCalculate/importFileDas.html";
			 var title="Import TTHQ CDBR + DAS";
			 var windowId="EFFECTIVE_CALCULATE";
			 htmlCommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','200');     	
        }
        
        vm.submitDas=submitDas;
        function submitDas(data){
        	vm.value=10;
		    chooseTabDAS(vm.value);
        	if(!$("#filePackage")[0].files[0]){
	    		toastr.warning("Bạn chưa chọn file để import");
	    		return;
	    	}
        	console.log("2");
			if($('.k-invalid-msg').is(':visible')){
				return;
			}
			console.log("3");
			if($("#filePackage")[0].files[0].name.split('.').pop() !=='xls' && $("#filePackage")[0].files[0].name.split('.').pop() !=='xlsx' ){
				toastr.warning("Sai định dạng file");
				return;
			}
			
    		 var formData = new FormData();
				formData.append('multipartFile', $('#filePackage')[0].files[0]); 
		     return   $.ajax({
	            url: Constant.BASE_SERVICE_URL+RestEndpoint.EFFECTIVE_CALCULATE_DAS_SERVICE_URL+"/importDas?folder="+ vm.folder,
	            type: "POST",
	            data: formData,
	            enctype: 'multipart/form-data',
	            
	            processData: false,
	            contentType: false,
	            cache: false,
	            success:function(data) {
	            	if(!!data.error){
	            		toastr.error(data.error);
	            		return;
	            	}
	            	else if (data == 'NO_CONTENT') {                       
                        toastr.warning("File import không có nội dung");
                        return;
                    }
	            	else if(data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length>0){
	            		console.log(data);
	            		vm.lstErrImport = data[data.length - 1].errorList;
	            		vm.objectErr = data[data.length - 1];
	            		var teamplateUrl="wms/createImportRequirementManagement/importResultPopUp.html";
	       			 	var title="Kết quả Import";
	       			 	var windowId="ERR_IMPORT";
	       			 	CommonService.populatePopupCreate(teamplateUrl,title,vm.lstErrImport,vm,windowId,false,'80%','80%');
	       			 	fillDataImportErrTable(vm.lstErrImport);
	            	}
	            	else{
	            		toastr.success("import thành công") 
	            		$("#targetGrid10").data('kendoGrid').dataSource.read();
						$("#targetGrid10").data('kendoGrid').refresh();
						htmlCommonService.dismissPopup();
	            	}
						  
	            }
	      });     
     }   
	
        vm.exportFile1 = function(){
			vm.searchForm.page = null;
			vm.searchForm.pageSize = null;
			return Restangular.all("EffectiveCalculateRsService/getCapexSource").post(vm.searchForm).then(function(d){
			CommonService.exportFile(vm.targetGrid, d.data, vm.listRemove,vm.listConvert, "Báo Cáo Capex Nguồn");
			});
		}
        vm.exportFile2 = function(){
			vm.searchForm.page = null;
			vm.searchForm.pageSize = null;
			return Restangular.all("EffectiveCalculateRsService/getData2").post(vm.searchForm).then(function(d){
			CommonService.exportFile(vm.targetGrid2, d.data, vm.listRemove,vm.listConvert, "Báo Cáo WACC");
			});
		}
        vm.exportFile3 = function(){
//			vm.searchForm.page = null;
//			vm.searchForm.pageSize = null;
			return Restangular.all("EffectiveCalculateRsService/exportData3").post(vm.searchForm).then(function(d){
				var data = d.plain();
				window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
			}).catch(function (e){
				toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
				return;
			});
		}
        vm.exportFile4 = function(){
			vm.searchForm.page = null;
			vm.searchForm.pageSize = null;
			return Restangular.all("EffectiveCalculateRsService/getData4").post(vm.searchForm).then(function(d){
			CommonService.exportFile(vm.targetGrid4, d.data, vm.listRemove,vm.listConvert, "Báo Cáo Chi phí đấu nối điện + GPMB");
			});
		}
        vm.exportFile5 = function(){
			vm.searchForm.page = null;
			vm.searchForm.pageSize = null;
			return Restangular.all("EffectiveCalculateRsService/exportData5").post(vm.searchForm).then(function(d){
				var data = d.plain();
				window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
			}).catch(function (e){
				toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
				return;
			});
		}
        vm.exportFile6 = function(){
			vm.searchForm.page = null;
			vm.searchForm.pageSize = null;
			return Restangular.all("EffectiveCalculateRsService/getData6").post(vm.searchForm).then(function(d){
			CommonService.exportFile(vm.targetGrid6, d.data, vm.listRemove,vm.listConvert, "Báo Cáo Giá thuê TTR theo 1477");
			});
		}
        vm.exportFile7 = function(){
			vm.searchForm.page = null;
			vm.searchForm.pageSize = null;
			return Restangular.all("EffectiveCalculateRsService/getData7").post(vm.searchForm).then(function(d){
			CommonService.exportFile(vm.targetGrid7, d.data, vm.listRemove,vm.listConvert, "Báo Cáo Capex");
			});
		}
        vm.exportFile8 = function(){
			vm.searchForm.page = null;
			vm.searchForm.pageSize = null;
			return Restangular.all("EffectiveCalculateRsService/getData8").post(vm.searchForm).then(function(d){
			CommonService.exportFile(vm.targetGrid8, d.data, vm.listRemove,vm.listConvert, "Báo Cáo Chào giá");
			});
		}
        vm.exportFile9 = function(){
			vm.searchForm.page = null;
			vm.searchForm.pageSize = null;
			return Restangular.all("EffectiveCalculateRsService/getData9").post(vm.searchForm).then(function(d){
			CommonService.exportFile(vm.targetGrid9, d.data, vm.listRemove,vm.listConvert, "Báo Cáo Tính toán hiệu quả");
			});
		}
        
        vm.listRemove = [];
        vm.listConvert = [];
        
        vm.importFileBTS = function(){
        	vm.fileImportData = false;
        	var teamplateUrl = "coms/effectiveCalculate/importBTS.html";
        	var title = "Import BTS từ file excel";
            var windowId = "IMPORT_BTS";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '350', null);
        }
        
        vm.getBTSExcelTemplate = function(){
        	var fileName;
        	var obj = {};
        	if(vm.actionType == 1){
        		fileName = 'Import_Capex_Nguon';
        		obj.request = 'downloadTemplateCapexNguon';
        	}
        	else if(vm.actionType ==7){
        		fileName = 'Import_Capex';
        		obj.request = 'downloadTemplateCapex';
        	}
        	else if(vm.actionType == 8){
        		fileName = 'Import_Chao_Gia';
        		obj.request = 'downloadTemplateChaoGia'
        	}
        	else if(vm.actionType == 2){
        		fileName = 'Import_WACC';
        		obj.request = 'downloadTemplateWACC';
        	}
        	else if(vm.actionType == 3){
        		fileName = 'Import_Ti_le_giao_khoan';
        		obj.request = 'downloadTemplateTLGK';
        	}
        	else if(vm.actionType == 6){
        		fileName = 'Import_Gia_Thue_TTR_Theo_1477';
        		obj.request = 'downloadTemplateGiaThue1477';
        	}
        	else if(vm.actionType == 9){
        		fileName = 'Import_Effective_Calculate';
        		obj.request = 'downloadTemplateEffectiveCalculate';
        	}
        	else if(vm.actionType == 5){
        		fileName = 'Import_Dgia_thue_VTNet';
        		obj.request = 'downloadTemplateDgiaThueVTNet';
        	}
        	else if(vm.actionType == 4){
        		fileName = 'Import_ChiPhi_DauNoi_GPMB';
        		obj.request = 'downloadTemplateGPMB';
        	} else{
        		toastr.error(gettextCatalog.getString("Bạn chưa chọn loại BTS"));
        	    return;
        	}
        	
        	return Restangular.all("EffectiveCalculateRsService/"+obj.request).post(vm.searchForm).then(function (d) {
				kendo.ui.progress($("#shipment"), false);
        	    var data = d.plain();
        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	}).catch(function (e) {
        		kendo.ui.progress($("#shipment"), false);
        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        	    return;
        	});
        }
        
        vm.cancelImportBTS = cancelImportBTS;
        function cancelImportBTS() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
            vm.disableSubmit = false;

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
        vm.exportExcelErr = function(){
        	return Restangular.all("fileservice/exportExcelError").post(vm.objectErr).then(function(d) {
				data = d.plain();
				window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + data.fileName;
			}).catch( function(){
				toastr.error(gettextCatalog.getString("Lỗi khi export!"));
				return;
			});
        	
		}
        vm.closeErrImportPopUp = function(){
        	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }
        
        vm.submitImportBTS = submitImportBTS;
        function submitImportBTS(){
        	var url;
        	if(vm.actionType == 1){
        		url = Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/importCapexNguon";
        	} else if (vm.actionType ==7){
        		url = Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/importCapex";
        	} else if (vm.actionType ==8){
        		url = Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/importChaoGia";
        	} else if (vm.actionType ==2){
        		url = Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/importWACC";
        	} else if (vm.actionType ==3){
        		url = Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/importTLGK";
        	} else if (vm.actionType ==6){
        		url = Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/importGiaThue1477";
        	} else if (vm.actionType ==9){
        		url = Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/importEffectiveCalculate";
        	} else if (vm.actionType ==5){
        		url = Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/importDgiaThueVTNet";
        	} else if (vm.actionType ==4){
        		url = Constant.BASE_SERVICE_URL + "EffectiveCalculateRsService/importGPMB";
        	} else{
        		toastr.error(gettextCatalog.getString("Bạn cần chọn loại BTS để import"));
				return;
        	}
        	chooseTab(vm.actionType);
        	 $('#testSubmit').addClass('loadersmall');
             vm.disableSubmit = true;
             if (!vm.fileImportData) {
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
             if (vm.fileImportData.name.split('.').pop() !== 'xls' && vm.fileImportData.name.split('.').pop() !== 'xlsx') {
                 $('#testSubmit').removeClass('loadersmall');
                 vm.disableSubmit = false;
                 toastr.warning("Sai định dạng file");
                 return;
             }
             var formData = new FormData();
             formData.append('multipartFile', vm.fileImportData);
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
                     } else if (data == 'UNAUTHORIZED') {
                         $('#testSubmit').removeClass('loadersmall');
                         vm.disableSubmit = false;
                         toastr.error("Không thể import do người dùng chưa được phân quyền miền dữ liệu!");
                     } else if (data == 'NOT_ACCEPTABLE') {
                         $('#testSubmit').removeClass('loadersmall');
                         vm.disableSubmit = false;
                         toastr.error("Không thể import do người dùng được phân quyền nhiều miền dữ liệu!");
                     }
//                     else if (data == 'METHOD_NOT_ALLOWED') {
//                         $('#testSubmit').removeClass('loadersmall');
//                         vm.disableSubmit = false;
//                         toastr.error("Không thể tạo công việc do chưa tồn tại kế hoạch tháng!");
//                     }
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
                     else if (data[data.length - 1].errorFilePath != null) {
                         window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                         toastr.warning("File import không hợp lệ");
                         return;
                     }
                     else {
                         $('#testSubmit').removeClass('loadersmall');
                         vm.disableSubmit = false;
                         toastr.success("Import thành công");
                         cancelImportBTS();
                         if(vm.actionType == 1){
                        	 $("#targetGrid").data('kendoGrid').dataSource.read();
			       			 $("#targetGrid").data('kendoGrid').refresh();
                         }
                         if(vm.actionType == 2){
                        	 $("#targetGrid2").data('kendoGrid').dataSource.read();
			       			 $("#targetGrid2").data('kendoGrid').refresh();
                         }
                         if(vm.actionType == 3){
                        	 $("#targetGrid3").data('kendoGrid').dataSource.read();
			       			 $("#targetGrid3").data('kendoGrid').refresh();
                         }
                         if(vm.actionType == 4){
                        	 $("#targetGrid4").data('kendoGrid').dataSource.read();
			       			 $("#targetGrid4").data('kendoGrid').refresh();
                         }
                         if(vm.actionType == 5){
                        	 $("#targetGrid5").data('kendoGrid').dataSource.read();
			       			 $("#targetGrid5").data('kendoGrid').refresh();
                         }
                         if(vm.actionType == 6){
                        	 $("#targetGrid6").data('kendoGrid').dataSource.read();
			       			 $("#targetGrid6").data('kendoGrid').refresh();
                         }
                         if(vm.actionType == 7){
                        	 $("#targetGrid7").data('kendoGrid').dataSource.read();
			       			 $("#targetGrid7").data('kendoGrid').refresh();
                         }
                         if(vm.actionType == 8){
                        	 $("#targetGrid8").data('kendoGrid').dataSource.read();
			       			 $("#targetGrid8").data('kendoGrid').refresh();
                         }
                         if(vm.actionType == 9){
                        	 $("#targetGrid9").data('kendoGrid').dataSource.read();
			       			 $("#targetGrid9").data('kendoGrid').refresh();
                         }
                     }
                     $scope.$apply();
                 }
             });
             
        }
		// end controller
        vm.submitCapex=submitCapex;
        function submitCapex(data){
        	vm.value=11;
		    chooseTabDAS(vm.value);
        	if(!$("#filePackageCapex")[0].files[0]){
	    		toastr.warning("Bạn chưa chọn file để import");
	    		return;
	    	}
			if($('.k-invalid-msg').is(':visible')){
				return;
			}
			if($("#filePackageCapex")[0].files[0].name.split('.').pop() !=='xls' && $("#filePackageCapex")[0].files[0].name.split('.').pop() !=='xlsx' ){
				toastr.warning("Sai định dạng file");
				return;
			}
    		 var formData = new FormData();
				formData.append('multipartFile', $('#filePackageCapex')[0].files[0]); 
		     return   $.ajax({
	            url: Constant.BASE_SERVICE_URL+RestEndpoint.EFFECTIVE_CALCULATE_DAS_CAPEX_SERVICE_URL+"/importDasCapex?folder="+ vm.folder,
	            type: "POST",
	            data: formData,
	            enctype: 'multipart/form-data',
	            
	            processData: false,
	            contentType: false,
	            cache: false,
	            success:function(data) {
	            	if(!!data.error){
	            		toastr.error(data.error);
	            		return;
	            	}
	            	 if (data == 'NO_CONTENT') {                       
                         toastr.warning("File import không có nội dung");
                         return;
                     }
	            	if(data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length>0){
	            		console.log(data);
	            		vm.lstErrImport = data[data.length - 1].errorList;
	            		vm.objectErr = data[data.length - 1];
	            		var teamplateUrl="wms/createImportRequirementManagement/importResultPopUp.html";
	       			 	var title="Kết quả Import";
	       			 	var windowId="ERR_IMPORT";
	       			 	CommonService.populatePopupCreate(teamplateUrl,title,vm.lstErrImport,vm,windowId,false,'80%','80%');
	       			 	fillDataImportErrTable(vm.lstErrImport);
	            	}
	            	else{	            		
	            		toastr.success("import thành công") 	            		
	            		$("#targetGrid11").data('kendoGrid').dataSource.read();
						$("#targetGrid11").data('kendoGrid').refresh();						
						htmlCommonService.dismissPopup();
	            	}
						  
	            }
	        });  
		    vm.value=11;
	    	chooseTabDAS(vm.value);
        }   
	
        vm.getExcelTemplateDas = function(){
			var fileName="Import_GiaDinh";
			CommonService.downloadTemplate2(fileName).then(function(d) {
				data = d.plain();
				window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + data.fileName;
			}).catch( function(){
				toastr.error(gettextCatalog.getString("Lỗi khi export!"));
				return;
			});
        }
        
        vm.getExcelTemplateCapex = function(){
			var fileName="Import_GiaDinh_Capex";
			CommonService.downloadTemplate2(fileName).then(function(d) {
				data = d.plain();
				window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + data.fileName;
			}).catch( function(){
				toastr.error(gettextCatalog.getString("Lỗi khi export!"));
				return;
			});
		
        }
        //PhucPV end 13072020 
        
	}
})();