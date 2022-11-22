(function() {
	'use strict';
	var controllerId = 'recommendContactUnitLibraryController';
	
	angular.module('MetronicApp').controller(controllerId, recommendContactUnitController);
	
	function recommendContactUnitController($scope, $rootScope, gettextCatalog, 
			kendoConfig, recommendContactUnitLibraryService,$filter,
			CommonService,htmlCommonService, Restangular, RestEndpoint, Constant, $timeout) {
		var vm = this;
		vm.cntContractSearch = {};
		vm.showSearch = true;
        vm.searchForm = {};
        vm.addForm = {};
        vm.isCreate = false;
        vm.fileLst = [];
        vm.listDescription = [];
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
        	kendo.ui.progress($("#recommendContactUnitLibraryGridId"), true);
        	vm.recommendContactUnitLibraryGridOptions = kendoConfig.getGridOptions({
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
                                  '</div>' +
                                
                              '<div class="btn-group pull-right margin_top_button margin_right10">'+
                              '<i data-toggle="dropdown" tooltip-placement="left" uib-tooltip="Cài đặt" aria-expanded="false"><i class="fa fa-cog" aria-hidden="true"></i></i>'+
                              '<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="vm.exportFile()" aria-hidden="true"></i>' +
                              '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
                              '<label ng-repeat="column in vm.recommendContactUnitLibraryGrid.columns| filter: vm.gridColumnShowHideFilter">'+
                              '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+
                              '</label>'+
                              '</div></div>'
                          }
                          ],
    			dataSource:{
    				serverPaging: true,
    				 schema: {
    					 total: function (response) {
    						    $("#countContactUnitLibrary").text(""+response.total);
    							return response.total; 
    						},
    						data: function (response) {	
    						kendo.ui.progress($("#recommendContactUnitLibraryGridId"), false);
    						return response.data; 
    						},
    	                },
    				transport: {
    					read: {
    	                        // Thuc hien viec goi service
    						url: Constant.BASE_SERVICE_URL + "recommendContactUnitService/doSearchContactUnitLibrary",
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
    				title: "Tên đơn vị hoặc chủ đầu tư đến tiếp xúc ",
    		        field: 'unitName',
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
    				title: "Địa chỉ của đơn vị hoặc chủ đầu tư đến tiếp xúc",
    		        field: 'unitAddress',
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
    				title: "Lĩnh vực hoạt động của đơn vị hoặc chủ đầu tư đến tiếp xúc",
    		        field: 'unitField',
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
    				title: "Chủ đầu tư nhận được Công văn",
    		        field: 'unitBoss',
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
    				title: "Hạn hoàn thành tiếp xúc",
    		        field: 'deadlineDateCompleteS',
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
    				title: "Thuộc loại",
    		        field: 'typeS',
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
    				title: "Kết quả đến tiếp xúc và tiếp nhận nhu cầu từ chủ đầu tư									",
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
					title: "Ghi Chú",
    				field:"description",
    				width: '5%',
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
        
        
        
        vm.importFile = importFile;
		function importFile() {
			var teamplateUrl="coms/recommendContactUnitLibrary/importFileCons.html";
			var title="Import quản lý tiếp xúc đơn vị";
			var windowId="IMPORT_MANAGE_RECOMMEND_CONTACT_UNIT";
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','275');
		}
		
		 vm.getExcelTemplateImport = function () {
			 kendo.ui.progress($("#shipment"), true);
				return Restangular.all("recommendContactUnitService/getExcelTemplateManageContactUnit").post(vm.searchForm).then(function (d) {
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
				    	 url: Constant.BASE_SERVICE_URL+"recommendContactUnitService"+"/importRecommendContactUnitLibrary?folder="+Constant.UPLOAD_FOLDER_TYPE_TEMP,
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
				            		$("#recommendContactUnitLibraryGridId").data('kendoGrid').dataSource.read();
				       			 	$("#recommendContactUnitLibraryGridId").data('kendoGrid').refresh();
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
					$('#shipmentFileGrid').data('kendoGrid').dataSource.remove(dataItem);
					 vm.fileLst.splice(dataItem,1);
				})
			}
        vm.cancelInput = function(data){
        	if(data=='date'){
        		vm.searchForm.endDate = null;
        		vm.searchForm.startDate = null;
        	}
        	if(data=='keySearch'){
        		vm.searchForm.keySearch = null;
        	}
        	if(data=='type'){
        		vm.searchForm.type = null;
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
        	if (vm.searchForm.startDate != null && vm.searchForm.endDate != null) {
                var fromDate = [];
                var toDate = [];
                fromDate = vm.searchForm.startDate.split('/');
                toDate = vm.searchForm.endDate.split('/');
                if (fromDate[2] > toDate[2]) {
                    toastr.error('Đến ngày phải lớn hơn từ ngày ');
                    $("#endDate").focus();
                    return;
                } else if (fromDate[2] == toDate[2]) {
                    if (fromDate[1] > toDate[1]) {
                        toastr.error('Đến ngày phải lớn hơn từ ngày ');
                        $("#endDate").focus();
                        return;
                    } else if (fromDate[1] == toDate[1]) {
                        if (fromDate[0] > toDate[0]) {
                            toastr.error('Đến ngày phải lớn hơn từ ngày ');
                            $("#endDate").focus();
                            return;
                        }
                    }
                }
                vm.searchForm.startDate = vm.searchForm.startDate.split("/").reverse().join("-");;
            	vm.searchForm.endDate = vm.searchForm.endDate.split("/").reverse().join("-");;
            }
        	
        	
        	
        	var grid = vm.recommendContactUnitLibraryGrid;
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
		 
// end controller
       }
})();