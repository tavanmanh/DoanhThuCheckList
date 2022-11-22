
(function() {
	'use strict';
	var controllerId = 'recommendContactUnitController';
	
	angular.module('MetronicApp').controller(controllerId, recommendContactUnitController);
	
	function recommendContactUnitController($scope, $rootScope, gettextCatalog, 
			kendoConfig, recommendContactUnitService,$filter,
			CommonService,htmlCommonService, Restangular, RestEndpoint, Constant, $timeout) {
		var vm = this;
		vm.showSearch = true;
        vm.searchForm = {};
        vm.addForm = {};
        vm.isSelect = false; //HienLT56 add 01072020
        vm.isCreate = false;
        vm.fileLst = [];
        vm.listDescription = [];
        vm.validateContactDateErr = '';
        vm.validatePhoneErr = '';
        vm.validatePhoneErr1 = '';
        
        vm.String="Quản lý công trình > Quản lý kế hoạch > Danh sách gợi ý tiếp xúc các đơn vị";
        init();
        function init(){
        	
        	fillDataTable([]);
        	vm.resultList = [
    			{name: "Có nhu cầu thuê nhà trạm + cột BTS", code: 1},
    			{name: "Có nhu cầu thuê truyền dẫn", code: 2},
    			{name: "Có nhu cầu triển khai DAS&CĐBR", code: 3},
    			{name: "Có nhu cầu triển khai thuê năng lượng mặt trời", code: 4},
    			{name: "Có nhu cầu khác ", code: 5},
    			{name: "Địa chỉ vị trí của đối tác cần thuê hoặc dự án của đối tác cần hợp tác", code: 6},
    			{name: "Không có nhu cầu", code: 7},
    			{name: "Đang đàm phán thương thảo cho thuê hoặc đầu tư cho thuê", code: 8},
    			{name: "Ký hợp đồng cho thuê hoặc đầu tư cho thuê", code: 9}];
        	
        	vm.typeList = [
    			{name: "Trạm + cột BTS", code: 1},
    			{name: "Truyền dẫn", code: 2},
    			{name: "DAS&CĐBR", code: 3},
    			{name: "Khác", code: 4}];
        	vm.isMessList = [
    			{name: "Tất cả", code: "2"},
    			{name: "Có", code: "1"},
    			{name: "Không", code: "0"}];
        	
        	vm.listRemove=[
	               {title: "Thao tác"},
	        	   ]
        	vm.listConvert=[
	               
	        		{
	        			field:"status",
	        			data:{
	        				0: 'Chậm tiến độ',
	        				1: 'Đúng tiến độ'
	        			}
	        		},
	        		
	        		]
        	
        	
        	}
        
       
        // tatph -end 13/11/2019
        
        var record = 0;
        function fillDataTable(data){
        	kendo.ui.progress($("#recommendContactUnitGridId"), true);
        	vm.recommendContactUnitGridOptions = kendoConfig.getGridOptions({
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
                            	  '<div>'+
                            	  '<div class=" pull-left ">'+
                                  '<button class="btn btn-qlk padding-search TkQLK"'+
                                  'ng-click="vm.importFile()" uib-tooltip="Import" translate>Import</button>'+
                                  //HienLT56 start 02072020
                                  '<button class="btn btn-qlk padding-search  addQLK"'+
                                  'ng-click="vm.add()" uib-tooltip="Thêm mới" translate>Thêm mới</button>'+
                                  '<button class="btn btn-qlk padding-search  TkQLK" style = "width: 220px;" '+
                                      'ng-click="vm.importFileContact()" uib-tooltip="Import hoàn thành tiếp xúc" translate>Import hoàn thành tiếp xúc</button>'+
                                  //HienLT56 end 02072020
                                  '</div>' +
                                  
                              '<div class="btn-group pull-right margin_top_button margin_right10">'+
                              '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'+
                              '<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="vm.exportFile()" aria-hidden="true"></i>' +
                              '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
                              '<label ng-repeat="column in vm.recommendContactUnitGrid.columns| filter: vm.gridColumnShowHideFilter">'+
                              '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+
                              '</label>'+
                              '</div></div>'
                          }
                          ],
    			dataSource:{
    				serverPaging: true,
    				 schema: {	
    					 total: function (response) {
    						    $("#countContactUnit").text(""+response.total);
    							return response.total; 
    						},
    						data: function (response) {	
    						kendo.ui.progress($("#recommendContactUnitGridId"), false);
    						return response.data; 
    						},
    	                },
    				transport: {
    					read: {
    	                        // Thuc hien viec goi service
    						url: Constant.BASE_SERVICE_URL + "recommendContactUnitService/doSearch",
    						contentType: "application/json; charset=utf-8",
    						type: "POST"
    					},					
    					parameterMap: function (options, type) {
    						    vm.searchForm.page = options.page
    							vm.searchForm.pageSize = options.pageSize
    							vm.searchForm.userLoginId = Constant.userInfo.VpsUserInfo.employeeCode ;
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
    			//HienLT56 start 1/7/2020
    			{
    				title: "Ký hiệu lĩnh vực của đơn vị đến tiếp xúc",
    		        field: 'unitCode',
    		        width: '100px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:center;white-space: normal;"
    				},
    			},
    			{
    				title: "Mã khu vực",
    		        field: 'areaCode',
    		        width: '100px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:center;white-space: normal;"
    				},
    			},
    			//HienLT56 end 1/7/2020
    			{
    				title: "Mã tỉnh",
    		        field: 'provinceCode',
    		        width: '100px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:center;white-space: normal;"
    				},
    			},
    			{
    				title: "Tên đơn vị hoặc chủ đầu tư đến tiếp xúc ",
    		        field: 'unitName',
    		        width: '100px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:center;white-space: normal;"
    				},
    			},
    			{
    				title: "Địa chỉ của đơn vị hoặc chủ đầu tư đến tiếp xúc",
    		        field: 'unitAddress',
    		        width: '200px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:center;white-space: normal;"
    				},
    			},
    			{
    				title: "Lĩnh vực hoạt động của đơn vị hoặc chủ đầu tư đến tiếp xúc",
    		        field: 'unitField',
    		        width: '200px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:center;white-space: normal;"
    				},
    			},
    			{
    				title: "Chủ đầu tư nhận được Công văn",
    		        field: 'unitBoss',
    		        width: '100px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:center;white-space: normal;"
    				},
    			},
    			{
    				title: "Hạn hoàn thành tiếp xúc",
    		        field: 'deadlineDateComplete',
    		        width: '100px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:center;white-space: normal;"
    				},
    			},
    			{
    				title: "Thuộc loại",
    		        field: 'typeS',
    		        width: '100px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:center;white-space: normal;"
    				},
    			},
    			//HienLT56 start 01072020
    			{
    				title: "Thời gian đến đơn vị hoặc chủ đầu tư xúc",
    		        width: '500px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				
    				columns :[
    					{
    						title: "Ngày đến tiếp xúc",
    	    				field:"contactDate",
    	    				width: '100px',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					},
    					{
    						title: "Họ và tên",
    	    				field:"fullNameCus",
    	    				width: '100px',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					},
    					{
    						title: "Chức vụ",
    	    				field:"positionCus",
    	    				width: '100px',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					},
    					{
    						title: "Số điện thoại ",
    	    				field:"phoneNumberCus",
    	    				width: '100px',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					},
    					{
    						title: "Mail",
    	    				field:"mailCus",
    	    				width: '100px',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					} 	
    					]
    			},
    			{
    				title: "Kết quả đến tiếp xúc và tiếp nhận nhu cầu từ chủ đầu tư									",
    		        width: '200px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				
    				columns :[
    					{
    						title: "Kết quả tiếp xúc",
    	    				field:"resultS",
    	    				width: '100px',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					},
    					
    					{
    						title: "Diễn giải tóm tắt nội dung đến tiếp xúc",
    	    				field:"shortContent",
    	    				width: '100px',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					} 
    					
    					]
    			},
    			{
    				title: "Nhân sự của CNKT đến tiếp xúc		",
    		        width: '300px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				columns :[
    					{
    						title: "Họ và Tên",
    	    				field:"fullNameEmploy",
    	    				width: '100px',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					},
    					{
    						title: "Số điện thoại",
    	    				field:"phoneNumberEmploy",
    	    				width: '100px',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					},
    					{
    						title: "Mail",
    	    				field:"mailEmploy",
    	    				width: '100px',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					} 
    					]
    			},
    		
    			{
					title: "Ghi Chú",
    				field:"description",
    				width: '100px',
    		        headerAttributes: {
    					style: "text-align:center;"
    				},
    				attributes: {
    					style: "text-align:left;"
    				},
				},
    			//HienLT56 end 01072020
    			{
    				title: "Thông báo mới",
    		        field: 'countMess',
    		        width: '100px',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:center;white-space: normal;"
    				},
    				template: "#if(countMess > 0) {#" +                                
  		          "<div ><span style='color: red;font-size:15px;'> Bạn có #=countMess# thông báo mới</span></div>#}" +
  		          "else {# <span>Bạn có #=countMess# thông báo</span> #}#"
  				,
    			},
    			{
					title: "Thao tác",
					headerAttributes: {
			        	style: "text-align:center; font-weight: bold;",
						translate:"",
					},
			        template: dataItem =>
					'<div class="text-center">'
					+'<button style=" border: none; background-color: white;" id="updateId" ng-show="dataItem.countDetail == 0" ng-click="vm.createContact(dataItem)"  class=" icon_table "'+
					'   uib-tooltip="Tạo mới lịch tiếp xúc" translate>'+
					'<i class="fa fa-plus" style = "color : green;" aria-hidden="true"></i>'
					+'</button>'
					
					+'<button style=" border: none; background-color: white;" id="updateId" ng-hide="dataItem.countDetail == 0" ng-click="vm.edit(dataItem)"  class=" icon_table "'+
					'   uib-tooltip="Sửa" translate>'+
					'<i class="fa fa-pencil" aria-hidden="true"></i>'
					+'</button>'
					
					+'</div>',
			        width: '150px',
			        field:"action"
				}
    			]
    		});
        }
        
        
        var records = 0;
        function fillDataTableDetail(data){
        	kendo.ui.progress($("#recommendContactUnitDetailId"), true);
        	vm.recommendContactUnitDetailGridOptions = kendoConfig.getGridOptions({
    			autoBind: true,
    			scrollable: true, 
    			resizable: true,
    			editable: true,
    			dataBinding: function() {
    				records = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
                reorderable: true,
    			
    			dataSource:{
    				serverPaging: true,
    				 schema: {
    					 total: function (response) {
    						    $("#counContactUnitDetailId").text(""+response.total);
    							return response.total; 
    						},
    						data: function (response) {	
    						kendo.ui.progress($("#recommendContactUnitDetailId"), false);
    						return response.data; 
    						},
    	                },
    				transport: {
    					read: {
    	                        // Thuc hien viec goi service
    						url: Constant.BASE_SERVICE_URL + "recommendContactUnitService/doSearchDetail",
    						contentType: "application/json; charset=utf-8",
    						type: "POST"
    					},					
    					parameterMap: function (options, type) {
    						    vm.searchForm.page = options.page
    							vm.searchForm.pageSize = options.pageSize
    							vm.searchForm.contactUnitId = data.contactUnitId
    							
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
    				  return ++records;
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
    				title: "Thời gian đến đơn vị hoặc chủ đầu tư xúc",
    		        width: '5%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				
    				columns :[
    					{
    						title: "Ngày đến tiếp xúc",
    	    				field:"contactDateS",
    	    				width: '5%',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					},
    					{
    						title: "Họ và tên",
    	    				field:"fullNameCus",
    	    				width: '5%',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					},
    					{
    						title: "Chức vụ",
    	    				field:"positionCus",
    	    				width: '5%',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					},
    					{
    						title: "Số điện thoại ",
    	    				field:"phoneNumberCus",
    	    				width: '5%',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					},
    					{
    						title: "Mail",
    	    				field:"mailCus",
    	    				width: '5%',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					} 	
    					]
    			},
    			{
    				title: "Kết quả đến tiếp xúc và tiếp nhận nhu cầu từ chủ đầu tư	",
    		        width: '5%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				
    				columns :[
    					{
    						title: "Kết quả tiếp xúc",
    	    				field:"resultS",
    	    				width: '10%',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					},
    					
    					{
    						title: "Diễn giải tóm tắt nội dung đến tiếp xúc",
    	    				field:"shortContent",
    	    				width: '10%',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					} 
    					
    					]
    			},
    			{
    				title: "Nhân sự của CNKT đến tiếp xúc		",
    		        width: '5%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				columns :[
    					{
    						title: "Họ và Tên",
    	    				field:"fullNameEmploy",
    	    				width: '5%',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					},
    					{
    						title: "Số điện thoại",
    	    				field:"phoneNumberEmploy",
    	    				width: '5%',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					},
    					{
    						title: "Mail",
    	    				field:"mailEmploy",
    	    				width: '5%',
    	    		        headerAttributes: {
    	    					style: "text-align:center;"
    	    				},
    	    				attributes: {
    	    					style: "text-align:left;"
    	    				},
    					} 
    					]
    			},
    			{
					title: "Thuộc loại",
    				field:"typeS",
    				width: '5%',
    		        headerAttributes: {
    					style: "text-align:center;"
    				},
    				attributes: {
    					style: "text-align:left;"
    				},
				} ,
    			{
					title: "Ghi Chú",
    				field:"description",
    				width: '5%',
    		        headerAttributes: {
    					style: "text-align:center;"
    				},
    				attributes: {
    					style: "text-align:left;"
    				},
				} ,
    			    {
					title: "Thao tác",
					headerAttributes: {
			        	style: "text-align:center; font-weight: bold;",
						translate:"",
					},
			        template: dataItem =>
					'<div class="text-center">'
					+'<button style=" border: none; background-color: white;" id="updateId" ng-click="caller.editDetail(dataItem)"  class=" icon_table "'+
					'   uib-tooltip="Sửa" translate>'+
					'<i class="fa fa-pencil" aria-hidden="true"></i>'
					+'</button>'
					+'</div>',
			        width: '150px',
			        field:"action"
				}
    			]
    		});
        }
        
        vm.importFile = importFile;
		function importFile() {
			var teamplateUrl="coms/recommendContactUnit/importFileCons.html";
			var title="Import tiếp xúc đơn vị";
			var windowId="IMPORT_RECOMMEND_CONTACT_UNIT";
			vm.fileImportData = null;
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','275');
		}
		
		 vm.getExcelTemplateImport = function () {
			 kendo.ui.progress($("#shipment"), true);
				return Restangular.all("recommendContactUnitService/getExcelTemplate").post(vm.searchForm).then(function (d) {
					kendo.ui.progress($("#shipment"), false);
	        	    var data = d.plain();
	        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
	        	}).catch(function (e) {
	        		kendo.ui.progress($("#shipment"), false);
	        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
	        	    return;
	        	});
	        }
		 
		 vm.fileImportData = false;
	        vm.submit=submit;
	        function submit(){
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
				    	 url: Constant.BASE_SERVICE_URL+"recommendContactUnitService"+"/importRecommendContactUnit?folder="+Constant.UPLOAD_FOLDER_TYPE_TEMP,
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
				            		$("#recommendContactUnitGridId").data('kendoGrid').dataSource.read();
				       			 	$("#recommendContactUnitGridId").data('kendoGrid').refresh();
				       			 	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
				            	}
				            }
				        });
	        }
	        
	        
	        
	        vm.exportExcelErr = function(){
	        	recommendContactUnitService.downloadErrorExcel(vm.objectErr).then(function(d) {
					data = d.plain();
					window.location = Constant.BASE_SERVICE_URL+"manageDataOutsideOsService/downloadFileTempATTT?" + data.fileName;
				}).catch( function(){
					toastr.error(gettextCatalog.getString("Lỗi khi export!"));
					return;
				});
			}
	        
	        vm.gridFileOptions = kendoConfig.getGridOptions({
				autoBind: true,
				resizable: true,			 
				dataSource: vm.fileLst,
				noRecords: true,
				columnMenu:false,
				scrollable:false,
				editable: false,
				messages: {
					noRecords : gettextCatalog.getString("Không có kết quả hiển thị")
				},dataBound: function () {
					var GridDestination = $("#shipmentFileGrid").data("kendoGrid");                
			                GridDestination.pager.element.hide();
                },
				columns: [{
					title: "TT",
					field:"stt",
			        template: dataItem => $("#shipmentFileGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
			        width: 20,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}
				, {
					title: "Tên file",
					field: 'name',
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
					template :  function(dataItem) {
						        	    	 return "<a href='' ng-click='caller.downloadFile(dataItem)'>" + dataItem.name + "</a>";
					}
				}, {
					title: "Ngày upload",
					field: 'createdDate',
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						"id":"appFile",
						style: "text-align:left;"
					},
				}  ,
				{
					title: "Người upload",
					field: 'createdUserName',
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						"id":"appFile",
						style: "text-align:left;"
					},
				}  ,{
				 title: "Xóa",
				 template: dataItem => 
					'<div class="text-center #=attactmentId#""> '+
						'<a type="button" class="#=attactmentId# icon_table" uib-tooltip="Xóa" translate> '+
							'<i class="fa fa-trash" ng-click=caller.removeRowFile(dataItem) ria-hidden="true"></i>'+
						'</a>'+
					'</div>' ,
				 width: 20,
				 field:"acctions"
				 }
				,],
			});
	        
	        
	        vm.onSelect = function(e) {
				var fileImportData = $("#shipmentFileGrid").data().kendoGrid.dataSource.view();
				for(var i = 0 ; i < fileImportData.length ; i ++){
					if($("#files")[0].files[0].name == fileImportData[i].name){
						toastr.error('Mỗi file chỉ được chọn 1 lần ');
						return;
					}
				}
			
				 if($("#files")[0].files[0].name.split('.').pop() !='xls' && $("#files")[0].files[0].name.split('.').pop() !='xlsx' && $("#files")[0].files[0].name.split('.').pop() !='doc'&& $("#files")[0].files[0].name.split('.').pop() !='docx'&& $("#files")[0].files[0].name.split('.').pop() !='pdf' ){
        		toastr.warning("Sai định dạng file");
        		setTimeout(function() {
              	  $(".k-upload-files.k-reset").find("li").remove();
   			     $(".k-upload-files").remove();
   				 $(".k-upload-status").remove();
   				 $(".k-upload.k-header").addClass("k-upload-empty");
   				 $(".k-upload-button").removeClass("k-state-focused");
         },10);
        		return;
        	}
			 var formData = new FormData();
			jQuery.each(jQuery('#files')[0].files, function(i, file) {
					 formData.append('multipartFile'+i, file);
				});
				 
				
	     return   $.ajax({
	            url:Constant.BASE_SERVICE_URL+"fileservice/uploadATTT",
	            type: "POST",
	            data: formData,
	            enctype: 'multipart/form-data',
	            processData: false,
	            contentType: false,
	            cache: false,
	            success:function(data) {
				 $.map(e.files, function(file,index) {
					 vm.fileLst = $("#shipmentFileGrid").data().kendoGrid.dataSource.data();
				 var obj={};
				 obj.name=file.name;
				 obj.filePath=data[index];
				 obj.createdDate = htmlCommonService.getFullDate();
				 obj.createdUserName = Constant.userInfo.VpsUserInfo.fullName;
				 obj.createdUserId = Constant.userInfo.VpsUserInfo.sysUserId;
				 vm.fileLst.push(obj);
				 })
            refreshGrid(vm.fileLst);
            setTimeout(function() {
            	  $(".k-upload-files.k-reset").find("li").remove();
 			     $(".k-upload-files").remove();
 				 $(".k-upload-status").remove();
 				 $(".k-upload.k-header").addClass("k-upload-empty");
 				 $(".k-upload-button").removeClass("k-state-focused");
       },10); 
	            }
	        });
	    }
	        vm.downloadFile = function(dataItem) {
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
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
		
	        function refreshGrid(d) {
				var grid = vm.shipmentFileGrid;
				if(grid){
					grid.dataSource.data(d);
					grid.refresh();
				}
			}
	        vm.removeRowFile = removeRowFile;
			function removeRowFile(dataItem) {
				confirm('Xác nhận xóa',function (){
//					$('#shipmentFileGrid').data('kendoGrid').dataSource.remove(dataItem);
					 vm.fileLst.splice(dataItem,1);
					 $('#shipmentFileGrid').data('kendoGrid').refresh();
				})
			}
        vm.cancelInput = function(data){
        	if(data=='date'){
        		vm.searchForm.endDate = null;
        		vm.searchForm.startDate = null;
        		vm.errMessage1 = null;
        		vm.errMessage = null;
        	}
        	if(data=='provinceCode'){
        		vm.searchForm.provinceCode = null;
        	}
        	if(data=='type'){
        		vm.searchForm.type = null;
        	}
        	if(data=='mess'){
        		vm.searchForm.isMess = null;
        	}
        }
        
        
        vm.edit = function(dataItem){
        	$scope.countMess = dataItem.countMess;
        	vm.isCreate = false;
        	vm.addForm = angular.copy(dataItem);
        	var title = "Lịch sử tiếp xúc đơn vị" ;
        	openPopup(title);
        	fillDataTableDetail(dataItem);
        }
        
        
        vm.createContact = function(dataItem){
        	vm.fileLst = [];
        	vm.listDescription = [];
        	vm.isCreate = true;
        	vm.addForm = dataItem;
        	vm.addForm.contactUnitId = dataItem.contactUnitId;
        	var title = "Tạo mới lịch tiếp xúc" ;
        	openPopupDetail(title);
        }
        
        $scope.$watchGroup(['vm.shipmentFileGrid', 'vm.fileLst'], function(newVal, oldVal) { 
			refreshGrid(vm.fileLst);						
		});
        function refreshGrid(d) {
			var grid = vm.shipmentFileGrid;
			if(grid){
				grid.dataSource.data(d);
				grid.refresh();
			}
		}
        vm.editDetail = function(dataItem){
        	var obj = {};
        	obj.contactUnitDetailId = dataItem.contactUnitDetailId;
        	obj.contactUnitId = dataItem.contactUnitId;
        	if($scope.countMess > 0){
        		recommendContactUnitService.updateDescription(obj).then(function (res) {
        			
        		}, function (errResponse) {
                    toastr.error("Có lỗi khi update Description!");
                });
        	}
        	vm.isCreate = false;
        	//HienLT56 start 30072020
        	vm.validateContactDateErr = null;
            vm.validatePhoneErr = null;
            vm.validatePhoneErr1 = null;
        	//HienLT56 end 30072020
        	recommendContactUnitService.doSearchDetailById(obj).then(function (result) {
                if (result != null) {
                	vm.addForm = result.data[0];
                	vm.fileLst = vm.addForm.utilAttachDocumentDTOs;
                	vm.listDescription = vm.addForm.contactUnitDetailDescriptionDTOs;
                	if(vm.listDescription != null){
                		for(var i = 0 ; i <vm.listDescription.length ; i++ ){
                    		vm.listDescription[i].data = vm.listDescription[i].createDateS + " : " +  vm.listDescription[i].description ;
                    	}
                	}
                }
                
                var title = "Chi tiết tiếp xúc đơn vị" ;
            	openPopupDetail(title);
            	
            	refreshGrid(vm.fileLst);
            }, function (errResponse) {
                toastr.error("Có lỗi xãy ra!");
            });
        }
        
        
        vm.save= function save(data){
        	
			var saveFile = [];
			var shipmentFileGridSave = $("#shipmentFileGrid").data("kendoGrid").dataSource.data();
			for (var i = 0; i < shipmentFileGridSave.length; i++) {
				saveFile.push(shipmentFileGridSave[i]);
			}
			
			vm.addForm.utilAttachDocumentDTOs= angular.copy(saveFile);
			if (vm.addForm.type == null ) {
				toastr.error("Bạn chưa chọn loại");
				$("#type").focus();
				return;
			}
			
			if (vm.addForm.contactDate == null || vm.addForm.contactDate == ""  ) {
				toastr.error("Ngày tiếp xúc không được để trống ");
				$("#contactDate").focus();
				return;
			}
			if(vm.addForm.contactDate != null && !vm.validateContactDate()){
				 toastr.error("Ngày đến tiếp xúc không hợp lệ. Hãy xem lại");
				 $("#contactDate").focus();
				 return;
			}
			if(vm.addForm.phoneNumberCus != "" && vm.addForm.phoneNumberCus != null && !vm.validatePhoneNumber()){
				 toastr.error("Số điện thoại của đơn vị hoặc chủ đầu tư đến tiếp xúc không hợp lệ!");
				 $("#phoneNumberCus").focus();
				 return;
			}
//			if(vm.addForm.mailCus != "" && vm.addForm.mailCus != null && !vm.validateEmail()){
//				toastr.error("Email của đơn vị hoặc chủ đầu tư đến tiếp xúc không hợp lệ!");
//				 $("#mailCus").focus();
//				 return;
//			}
			if(vm.addForm.phoneNumberEmploy != "" && vm.addForm.phoneNumberEmploy != null && !validatePhoneNumberCNKT()){
				toastr.error("Số điện thoại của nhân sự CNKT đến tiếp xúc không hợp lệ!");
				$("#phoneNumberEmploy").focus();
				 return;
			}
//			if(vm.addForm.mailEmploy != "" && vm.addForm.mailEmploy != null && !vm.validateEmail()){
//				toastr.error("Email của nhân sự CNKT đến tiếp xúc không hợp lệ!");
//				 $("#mailEmploy").focus();
//				 return;
//			}
			if (vm.addForm.utilAttachDocumentDTOs == null || vm.addForm.utilAttachDocumentDTOs.length == 0) {
				toastr.error("Phải có file đính kèm! ");
				
				return;
			}
			
			
			vm.addForm.userLogin = Constant.userInfo.VpsUserInfo.fullName ;
			vm.addForm.contactDate = vm.addForm.contactDate.split("/").reverse().join("-");
			vm.addForm.deadlineDateComplete = vm.addForm.deadlineDateComplete.split("/").reverse().join("-");
			vm.addForm.contactUnitLibraryDTO = {
					"contactDate":vm.addForm.contactDate,
					"contactUnitDetailId":vm.addForm.contactUnitDetailId,
					"contactUnitId":vm.addForm.contactUnitId,
					"customerAddress":vm.addForm.customerAddress,
					"deadlineDateComplete":vm.addForm.deadlineDateComplete,
					"description":vm.addForm.description,
					"fullNameCus":vm.addForm.fullNameCus,
					"fullNameEmploy":vm.addForm.fullNameEmploy,
					"mailCus":vm.addForm.mailCus,
					"mailEmploy":vm.addForm.mailEmploy,
					"phoneNumberCus":vm.addForm.phoneNumberCus,
					"phoneNumberEmploy":vm.addForm.phoneNumberEmploy,
					"positionCus":vm.addForm.positionCus,
					"provinceCode":vm.addForm.provinceCode,
					"provinceName":vm.addForm.provinceName,
					"result":vm.addForm.result,
					"shortContent":vm.addForm.shortContent,
					"type":vm.addForm.type,
					"unitAddress":vm.addForm.unitAddress,
					"unitBoss":vm.addForm.unitBoss,
					"unitField":vm.addForm.unitField,
					"unitName":vm.addForm.unitName,
					
			};
			vm.addForm.userLoginId = Constant.userInfo.VpsUserInfo.employeeCode ;
			vm.addForm.contactDate = vm.addForm.contactDate.split("/").reverse().join("-");;
			data=angular.copy(vm.addForm);
			if(vm.isCreate) {
				recommendContactUnitService.save(data).then(function(result){
	    			toastr.success("Thêm mới thành công!");
	    			$("#recommendContactUnitGridId").data('kendoGrid').dataSource.read();
					$("#recommendContactUnitGridId").data('kendoGrid').refresh();
					$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
	    		}, function(errResponse){
	                if (errResponse.status === 409) {
	                	toastr.error(gettextCatalog.getString("Lỗi!"));
	                } else {
	                	toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi tạo !"));
	                }
		        });
	    	} else {
	    		recommendContactUnitService.update(data).then(function(result){
					
					$("#recommendContactUnitGridId").data('kendoGrid').dataSource.read();
					$("#recommendContactUnitGridId").data('kendoGrid').refresh();
	    			toastr.success("Cập nhật thành công!");
					$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
	    		}, function(errResponse){
	                	toastr.error(gettextCatalog.getString("Xảy ra lỗi khi cập nhật"));
		        });
	    	}
		}
        
        function openPopup(title){
      	  var teamplateUrl = "coms/recommendContactUnit/popupRecommendContactUnitDetail.html";
            var title = title;
            var windowId = "EDIT_RECOMMEND_CONTACT_UNIT";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '85%', '85%', "deptAdd");
        }
        
        function openPopupDetail(title){
        	  var teamplateUrl = "coms/recommendContactUnit/popupEditRecommendContactUnitDetail.html";
              var title = title;
              var windowId = "EDIT_RECOMMEND_CONTACT_UNIT_DETAIL";
              CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '85%', '85%', "deptAdd");
          }
        vm.cancel = function(){
        	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }
        
        vm.closeErrImportPopUp = function(){
        	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }
        
        vm.doSearch = doSearch;
        function doSearch(){
//        	if (vm.searchForm.startDate != null && vm.searchForm.endDate != null) {
//                var fromDate = [];
//                var toDate = [];
//                fromDate = vm.searchForm.startDate.split('/');
//                toDate = vm.searchForm.endDate.split('/');
////                if (fromDate[2] > toDate[2]) {
////                    toastr.error('Đến ngày phải lớn hơn từ ngày ');
////                    $("#endDate").focus();
////                    return;
////                } else if (fromDate[2] == toDate[2]) {
////                    if (fromDate[1] > toDate[1]) {
////                        toastr.error('Đến ngày phải lớn hơn từ ngày ');
////                        $("#endDate").focus();
////                        return;
////                    } else if (fromDate[1] == toDate[1]) {
////                        if (fromDate[0] > toDate[0]) {
////                            toastr.error('Đến ngày phải lớn hơn từ ngày ');
////                            $("#endDate").focus();
////                            return;
////                        }
////                    }
////                }
//                vm.searchForm.startDate = vm.searchForm.startDate.split("/").reverse().join("-");;
//            	vm.searchForm.endDate = vm.searchForm.endDate.split("/").reverse().join("-");;
//            }
        	
        	
        	
        	var grid = vm.recommendContactUnitGrid;
        	if(grid){
        		grid.dataSource.query({
    				page: 1,
    				pageSize: 10
    			});
        	}
        }
        

     
        
        
        
        
       
     
    
        

		
        
        
		 
		 vm.exportFile = function () {
			 kendo.ui.progress($("#manageDataOSSearch"), true);
				return Restangular.all("recommendContactUnitService/exportListContact").post(vm.searchForm).then(function (d) {
					kendo.ui.progress($("#manageDataOSSearch"), false);
	        	    var data = d.plain();
	        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
	        	}).catch(function (e) {
	        		kendo.ui.progress($("#manageDataOSSearch"), false);
	        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
	        	    return;
	        	});
        }
		 
		//HienLT56 start 30062020
		 vm.validateDate = validateDate;
		 function validateDate(){
			 vm.errMessage = null;
			 vm.errMessage1 = null;
			 vm.checkStartDate = false;
			 vm.checkEndDate = false;
			 var startDatee = $('#startDate').val();
			 var endDatee = $('#endDate').val();
			 
			 //Kiem tra field duoc nhap
			 if(startDatee !== ""){
				 if(kendo.parseDate(startDatee,"dd/MM/yyyy") == null){
					 vm.errMessage1 = "Ngày hoàn thành từ ngày không hợp lệ";
					 $("#startDate").focus();
					 vm.checkStartDate = false;
				 } else if(kendo.parseDate(startDatee, "dd/MM/yyyy").getFullYear() > 2099 || kendo.parseDate(startDatee,"dd/MM/yyyy").getFullYear() < 1900){
					 vm.errMessage1 = "Ngày hoàn thành từ ngày không hợp lệ";
					 $("#startDate").focus();
					 vm.checkStartDate = false;
				 } else{
					 vm.errMessage1 = null;
					 vm.checkStartDate = true;
				 }
			 }
			 
			 if(endDatee !== ""){
				 if(kendo.parseDate(endDatee,"dd/MM/yyyy") == null){
					 vm.errMessage = "Ngày kết thúc không hợp lệ";
					 $('#endDate').focus();
					 vm.checkEndDate = false;
				 } else if(kendo.parseDate(endDatee,"dd/MM/yyyy").getFullYear() > 2099 || kendo.parseDate(endDatee,"dd/MM/yyyy").getFullYear() < 1900){
					 vm.errMessage = "Ngày kết thúc không hợp lệ";
					 $('#endDate').focus();
					 vm.checkEndDate = false;
				 } else{
					 vm.errMessage = null;
					 vm.checkEndDate = true;
				 }
			 }
			 if(!!vm.checkStartDate && !!vm.checkEndDate){
				 if(kendo.parseDate(startDatee,"dd/MM/yyyy") > kendo.parseDate(endDatee,"dd/MM/yyyy")) {
	        			vm.errMessage = 'Giá trị Từ ngày phải nhỏ hơn hoặc bằng giá trị Đến ngày';
						$('#startDate').focus();
	        		}else {
					vm.errMessage = null;
					vm.errMessage1 = null;
	        	 }
	        	 return;
			 }
			 
			 
			 
		 }
		 
		 vm.patternProvinceOptions={
					dataTextField: "code", 
					placeholder:"Nhập mã hoặc tên tỉnh",
					open: function(e) {
						vm.isSelect = false;

					},
		            select: function(e) {
		            	vm.isSelect = true;
		                data = this.dataItem(e.item.index());
		                vm.searchForm.provinceCode = data.code;
		                vm.searchForm.provinceName = data.name;
		                $scope.provinceCode = data.code;
		            },
		            pageSize: 10,
		            dataSource: {
		                serverFiltering: true,
		                transport: {
		                    read: function(options) {
		                    	return Restangular.all("recommendContactUnitService/getForAutoCompleteProvinceS").post({pageSize:10, 
		                    		page:1, 
		                    		keySearch: vm.searchForm.provinceCode}).then(function(response){
		                    		options.success(response);
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
		            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;word-wrap: break-word;float:left">#: data.code #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
		            change: function(e) {
		            	  if(vm.isSelect) {
		 	        		  if(vm.searchForm.provinceCode != $scope.provinceCode){
		 	        			 vm.searchForm.provinceCode = null;
		 		                 vm.searchForm.provinceName = null;
		 	        		  }
		 	     		}
		 	     		if(!vm.isSelect) {
		 	     			 vm.searchForm.provinceCode = null;
				             vm.searchForm.provinceName = null;
		 	     		}
		            },
		            close: function(e) {
		                // handle the event0
		              }
				};
		 vm.patternProvinceOptionsS={
					dataTextField: "code", 
					placeholder:"Nhập mã hoặc tên tỉnh",
					open: function(e) {
						vm.isSelect = false;

					},
		            select: function(e) {
		            	vm.isSelect = true;
		                data = this.dataItem(e.item.index());
		                vm.addFormContact.provinceCode = data.code;
		                vm.addFormContact.provinceName = data.name;
		                vm.addFormContact.areaCode = data.areaCode;
		                $scope.provinceCode = data.code;
		            },
		            pageSize: 10,
		            dataSource: {
		                serverFiltering: true,
		                transport: {
		                    read: function(options) {
		                    	return Restangular.all("recommendContactUnitService/getForAutoCompleteProvinceS").post({pageSize:10, 
		                    		page:1, 
		                    		keySearch: vm.addFormContact.provinceCode}).then(function(response){
		                    		options.success(response);
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
		            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;word-wrap: break-word;float:left">#: data.code #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
		            change: function(e) {
		            	  if(vm.isSelect) {
		 	        		  if(vm.addFormContact.provinceCode != $scope.provinceCode){
		 	        			 vm.addFormContact.provinceCode = null;
		 		                 vm.addFormContact.provinceName = null;
		 		                 vm.addFormContact.areaCode = null;
		 	        		  }
		 	     		}
		 	     		if(!vm.isSelect) {
		 	     			 vm.addFormContact.provinceCode = null;
				             vm.addFormContact.provinceName = null;
				             vm.addFormContact.areaCode = null;
		 	     		}
		            },
		            close: function(e) {
		                // handle the event0
		              }
				};
		 
		//HienLT56 end 30062020
		 
		//HienLT56 start 02072020
		 vm.add = add;
		 function add(){
			 vm.isCreateNew = true;
			 vm.addFormContact = {};
			 var teamplateUrl = "coms/recommendContactUnit/addContactPopup.html";
			 var title = "Thêm mới tiếp xúc đơn vị";
			 var windowId = "APP_PARAM";
			 CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','350',"code"); 
		 }
		 vm.cancelProvinceCode = function(){
			 vm.addFormContact.provinceCode = null;
		 }
		 vm.cancelAreaCode = function(){
			 vm.addFormContact.areaCode = null;
		 }
		 
		 vm.validateDeadlineDateComplete = validateDeadlineDateComplete; 
		 function validateDeadlineDateComplete(){
			 	vm.deadlineDateCompleteErr = '';
				
				vm.checkDeadlineDateComplete = false;
				var deadlineDateComplete = $('#deadlineDateComplete').val();
	    		
	    		if(deadlineDateComplete !== "") {
	    			if(kendo.parseDate(deadlineDateComplete,"dd/MM/yyyy") == null){
		    			$("#deadlineDateComplete").focus();
		        		vm.deadlineDateCompleteErr = "Hạn hoàn thành tiếp xúc không hợp lệ";
		        		vm.checkDeadlineDateComplete = false;
		        		
				    }else if (kendo.parseDate(deadlineDateComplete,"dd/MM/yyyy").getFullYear() > 2099 || kendo.parseDate(deadlineDateComplete,"dd/MM/yyyy").getFullYear() < 1900){
				    	$("#deadlineDateComplete").focus();
		        		vm.deadlineDateCompleteErr = "Hạn hoàn thành tiếp xúc không hợp lệ";
		        		vm.checkDeadlineDateComplete = false;
		        		
				    } else {
				    	vm.deadlineDateCompleteErr = null;
				    	vm.checkDeadlineDateComplete = true;
				    }
	    			return vm.checkDeadlineDateComplete;
	    		}
		 }
		 vm.cancelDeadlineDateComplete = function(){
			 vm.addFormContact.deadlineDateComplete = null;
		 }
		 //HienLT56 start 15072020
		 vm.validateContactDate = validateContactDate;
		 function validateContactDate(){
			 
			 vm.checkField = false;
			 var contactDate = $('#contactDate').val();
			 var deadlineDateComplete = $('#deadlineDateComplete').val();
			 if(contactDate !== ""){
				 if(kendo.parseDate(contactDate, "dd/MM/yyyy") == null){
					 $("#contactDate").focus();
					 vm.validateContactDateErr = "Ngày đến tiếp xúc không hợp lệ";
					 vm.checkField = false;
				 } else if(kendo.parseDate(contactDate,"dd/MM/yyyy").getFullYear() > 2099 || kendo.parseDate(contactDate,"dd/MM/yyyy").getFullYear() < 1900){
					 $("#contactDate").focus();
					 vm.validateContactDateErr = "Ngày đến tiếp xúc không hợp lệ";
					 vm.checkField = false;
				 } else if(kendo.parseDate(contactDate,"dd/MM/yyyy") >= kendo.parseDate(deadlineDateComplete,"dd/MM/yyyy")){
					 $("#contactDate").focus();
					 vm.validateContactDateErr = "Ngày đến tiếp xúc phải nhỏ hơn Hạn hoàn thành tiếp xúc";
					 vm.checkField = false;
				 } 
				 else{
					 vm.validateContactDateErr = null;
					 vm.checkField = true;
				 }
			 }
			 return  vm.checkField;
		 }
		 
		 vm.validatePhoneNumber = validatePhoneNumber;
		 function validatePhoneNumber(){
			 vm.validatePhoneErr = null;
			 var vnf_regex = /((09|03|07|08|05)+([0-9]{8})\b)/g;
			 vm.checkPhone = false;
			 var phoneNumberCus = $('#phoneNumberCus').val();
			 if(phoneNumberCus !== ""){
				 if(vnf_regex.test(phoneNumberCus) == false){
					 $('#phoneNumberCus').focus();
					 vm.validatePhoneErr = 'Số điện thoại của đơn vị hoặc chủ đầu tư đến tiếp xúc không hợp lệ!';
					 vm.checkPhone = false;
				 } 
				 else{
					 vm.validatePhoneErr = null;
					 vm.checkPhone = true;
				 }
			 }
			 return vm.checkPhone;
		 }
		 
		 vm.validatePhoneNumberCNKT = validatePhoneNumberCNKT;
		 function validatePhoneNumberCNKT(){
			 vm.validatePhoneErr1 = null;
			 var vnf_regex = /((09|03|07|08|05)+([0-9]{8})\b)/g;
			 vm.checkPhone = false;
			 var phoneNumberEmploy = $('#phoneNumberEmploy').val();
			 if(phoneNumberEmploy !== ""){
				 if(vnf_regex.test(phoneNumberEmploy) == false){
					 $('#phoneNumberEmploy').focus();
					 vm.validatePhoneErr1 = 'Số điện thoại của nhân sự CNKT đến tiếp xúc không hợp lệ';
					 vm.checkPhone = false;
				 } else{
					 vm.validatePhoneErr1 = null;
					 vm.checkPhone = true;
				 }
			 }
			 
			 return vm.checkPhone;
		 }
		 
		 vm.validateEmail = validateEmail;
		 function validateEmail(){
			 vm.validateEmailErr = '';
			 vm.validateEmailErr1 = '';
			 var vERegex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
			 vm.checkEmail = false;
			 var mailCus = $('#mailCus').val();
			 var mailEmploy = $('#mailEmploy').val();
			 if(mailCus !== ""){
				 if(vERegex.test(mailCus) == false){
					 $('#mailCus').focus();
					 vm.validateEmailErr = 'Email của đơn vị hoặc chủ đầu tư đến tiếp xúc không hợp lệ!';
					 vm.checkEmail = false;
				 } else{
					 vm.validateEmailErr = null;
					 vm.checkEmail = true; 
				 }
			 }
			 if(mailEmploy !== ""){
				 if(vERegex.test(mailEmploy) == false){
					 $('#mailEmploy').focus();
					 vm.validateEmailErr1 = 'Email của nhân sự CNKT đến tiếp xúc không hợp lệ!';
					 vm.checkEmail = false;
				 } else{
					 vm.validateEmailErr = null;
					 vm.checkEmail = true; 
				 }
			 }
			 return vm.checkEmail;
		 }
		 //HienLT56 end 15072020
		 vm.saveAddContact = function saveAddContact(){
			 if(vm.addFormContact.provinceCode == null || vm.addFormContact.provinceCode == ""){
				 toastr.error('Tỉnh không được để trống ');
				 $("#provinceCode").focus();
				 return;
			 }
			 if(vm.addFormContact.areaCode == null || vm.addFormContact.areaCode == ""){
				 toastr.error('Khu vực không được để trống ');
				 $("#areaCode").focus();
				 return;
			 }
			 if(vm.addFormContact.unitCode == null || vm.addFormContact.unitCode == ""){
				 toastr.error('Ký hiệu lĩnh vực của đơn vị đến tiếp xúc không được để trống ');
				 $("#unitCode").focus();
				 return;
			 }
			 if(vm.addFormContact.unitName == null || vm.addFormContact.unitName == ""){
				 toastr.error('Tên đơn vị hoặc chủ đầu tư đến tiếp xúc không được để trống ');
				 $("#unitName").focus();
				 return;
			 }
			 if(vm.addFormContact.unitAddress == null || vm.addFormContact.unitAddress == ""){
				 toastr.error('Địa chi của đơn vị hoặc chủ đầu tư đến tiếp xúc không được để trống ');
				 $("#unitAddress").focus();
				 return;
			 }
			 if(vm.addFormContact.unitField == null || vm.addFormContact.unitField == ""){
				 toastr.error('Lĩnh vực hoạt động của đơn vị hoặc chủ đầu tư đến tiếp xúc không được để trống ');
				 $("#unitField").focus();
				 return;
			 }
			 if(vm.addFormContact.deadlineDateComplete == null || vm.addFormContact.deadlineDateComplete == ""){
				 toastr.error('Hạn hoàn thành tiếp xúc không được để trống ');
				 $("#deadlineDateComplete").focus();
				 return;
			 }
			 if(!vm.validateDeadlineDateComplete()){
				 toastr.error('Hạn hoàn thành tiếp xúc không hợp lệ');
				 $("#deadlineDateComplete").focus();
				 return;
			 }
			 data = vm.addFormContact;
			 if(vm.isCreateNew){
				 recommendContactUnitService.addContactt(data).then(function(result){
					 console.log(result);
					 if(result.error){
						 toastr.error(result.error);
						 return;
					 }
					 toastr.success("Thêm mới thành công!");
					 vm.addFormContact = result;
					 doSearch();
//					 htmlCommonService.dismissPopup();
//						$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
					 $("#recommendContactUnitGridId").data('kendoGrid').dataSource.read();
	       			 	$("#recommendContactUnitGridId").data('kendoGrid').refresh();
	       			 	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
				 }, function(errResponse){
//					 if(errResponse.status === 409){
//						 	toastr.error(gettextCatalog.getString("Bản ghi với nội dung, thông tin dự án, chủ đầu tư đã tồn tại!"));
//							return;
//					 } else {
							toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi tạo tiếp xúc đơn vị!"));
							return;
//						}
				 });
			 }
		 }
		 vm.openProvince = function() {
				var templateUrl = 'coms/recommendContactUnit/openProvincePopUp.html';
				var title = gettextCatalog.getString("Tìm kiếm tỉnh");
				htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','addContactPopupController');
		}
		 vm.onSaveContractFrame = function(data) {
				vm.addFormContact.provinceCode = data.code;
	    		vm.addFormContact.provinceName = data.name;
	    		vm.addFormContact.areaCode = data.areaCode;
		}
		 
		 
		 vm.importFileContact = function(){
			 vm.importContactAll = {};
			 vm.fileImportData = false;
			 var teamplateUrl = "coms/recommendContactUnit/importFileContactAll.html";
			 var title = "Import tiếp xúc từ file excel";
			 var windowId="APP_PARAM";
			 vm.fileImportData = null;
		     CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','270');
		 }
		 
		 vm.fileImportData = false;
	        vm.submitImportAll=submitImportAll;
	        function submitImportAll(){
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
				    	 url: Constant.BASE_SERVICE_URL+"recommendContactUnitService"+"/importRecommendContactUnitAll?folder="+Constant.UPLOAD_FOLDER_TYPE_TEMP,
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
				            		$("#recommendContactUnitGridId").data('kendoGrid').dataSource.read();
				       			 	$("#recommendContactUnitGridId").data('kendoGrid').refresh();
				       			 	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
				            	}
				            }
				        });
	        }
	        
	        vm.getExcelTemplateImportAll = function () {
				 kendo.ui.progress($("#importContactAll"), true);
					return Restangular.all("recommendContactUnitService/getExcelTemplateAll").post(vm.searchForm).then(function (d) {
						kendo.ui.progress($("#importContactAll"), false);
		        	    var data = d.plain();
		        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
		        	}).catch(function (e) {
		        		kendo.ui.progress($("#importContactAll"), false);
		        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
		        	    return;
		        	});
		        }
	        vm.showHideColumn=function(column){
	        	if (angular.isUndefined(column.hidden)) {
	        		vm.recommendContactUnitGrid.hideColumn(column);
	            } else if (column.hidden) {
	            	vm.recommendContactUnitGrid.showColumn(column);
	            } else {
	            	vm.recommendContactUnitGrid.hideColumn(column);
	            }
	        	
	        	
	        }
		 
		//HienLT56 end 02072020
		 
// end controller
       }
})();