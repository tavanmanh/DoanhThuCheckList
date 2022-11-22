(function() {
	'use strict';
	var controllerId = 'manageHcqtController';
	
	angular.module('MetronicApp').controller(controllerId, manageHcqtController);
	
	function manageHcqtController($scope, $rootScope, gettextCatalog, 
			kendoConfig, manageHcqtService,
			CommonService,htmlCommonService, Restangular, RestEndpoint, Constant, $timeout) {
		var vm = this;
		vm.provinceList = [];
		vm.showSearch = true;
        vm.showDetail = false;
        vm.searchForm = {};
        vm.cntContractSearch = {};
        vm.addForm = {};
        vm.isCreate = false;
        vm.checkTTHT = false;
        vm.isReject = false;
        vm.String = "Quản lý công trình > Quản lý hoàn công quyết toán công trình";
        init();
        function init(){
        	
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
        
       
        //tatph -end 13/11/2019
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
//                            	  '<div class=" pull-left ">'+
//                                  '<button class="btn btn-qlk padding-search TkQLK"'+
//                                  'ng-click="vm.importFile()" uib-tooltip="Import" translate>Import</button>'+
//                					'</div>'
//                                 +
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
    				serverPaging: false,
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
    						url: Constant.BASE_SERVICE_URL + "manageHcqtService/doSearchV2",
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
    			////////////////////////

    			///////////////////////
    				
    			////////////////////////
    			{
                    title: gettextCatalog.getString("Mã công trình"),
                    field: "constructionCode",
                    headerAttributes: { style: "text-align:center;" },
                    attributes: { style: "text-align:left; " },
                    width: '10%',
                },
                {
                    title: gettextCatalog.getString("Mã tỉnh"),
                    field: "provinceCode",
                    headerAttributes: { style: "text-align:center;" },
                    attributes: { style: "text-align:left; " },
                    width: '10%',
                },
                {
                    title: gettextCatalog.getString("Mã trạm"),
                    field: "stationCode",
                    headerAttributes: { style: "text-align:center;" },
                    attributes: { style: "text-align:left; " },
                    width: '10%',
                },
    			{
                    title: gettextCatalog.getString("Tên VT TB"),
                    field: "merName",
                    headerAttributes: { style: "text-align:center;" },
                    attributes: { style: "text-align:left; " },
                    width: '10%',
                },
                {
                    title: gettextCatalog.getString("Serial number"),
                    field: "serialNumber",
                    headerAttributes: { style: "text-align:center;" },
                    attributes: { style: "text-align:left; " },
                    width: '10%',
                },
                {
                    title: gettextCatalog.getString("Đơn vị tính"),
                    field: "unitName",
                    headerAttributes: { style: "text-align:center;" },
                    attributes: { style: "text-align:left;" },
                    width: '10%',
                },
                {
                    title: gettextCatalog.getString("Số lượng bàn giao"),
                    field: "handoverQuantity",
                    headerAttributes: { style: "text-align:center;" },
                    attributes: { style: "text-align:right;" },
                    width: '10%',
                },
                {
                    title: gettextCatalog.getString("Số lượng nghiệm thu"),
                    field: "acceptQuantity",
                    headerAttributes: { style: "text-align:center;" },
                    attributes: { style: "text-align:right;" },
                    width: '10%',
                    template: function(dataItem) {
                        if ($.isNumeric(dataItem.acceptQuantity)) {
                            dataItem.acceptQuantity = parseFloat(Number(dataItem.acceptQuantity).toFixed(3));
                            // return
							// parseFloat(Number(dataItem.recoveryQuantity).toFixed(3));
                            return kendo.toString(parseFloat(Number(dataItem.acceptQuantity).toFixed(3)), "n3");
                        } else {
                            dataItem.acceptQuantity = 0;
                            return 0;
                        }
                    },
                    format: "{0:n3}",
                    decimals: 3,
                },
                {
                    title: gettextCatalog.getString("Số lượng thu hồi"),
                    field: "recoveryQuantity",
                    headerAttributes: { style: "text-align:center;" },
                    attributes: { style: "text-align:right;" },
                    template: function(dataItem) {
                        if ($.isNumeric(dataItem.recoveryQuantity)) {
                            dataItem.recoveryQuantity = parseFloat(Number(dataItem.recoveryQuantity).toFixed(3));
                            // return
							// parseFloat(Number(dataItem.recoveryQuantity).toFixed(3));
                            return kendo.toString(parseFloat(Number(dataItem.recoveryQuantity).toFixed(3)), "n3");
                        } else {
                            dataItem.recoveryQuantity = 0;
                            return 0;
                        }
                    },
                    format: "{0:n3}",
                    decimals: 3,
                    width: '10%',
                },
                {
                    title: gettextCatalog.getString("Số lượng thu hồi"),
                    field: "originRecoveryQuantity",
                    template: function(dataItem) {
                        if ($.isNumeric(dataItem.originRecoveryQuantity)) {
                            dataItem.originRecoveryQuantity = parseFloat(Number(dataItem.originRecoveryQuantity).toFixed(3));
                            // return
							// parseFloat(Number(dataItem.originRecoveryQuantity).toFixed(3));
                            return kendo.toString(parseFloat(Number(dataItem.originRecoveryQuantity).toFixed(3)), "n3");
                        } else {
                            dataItem.originRecoveryQuantity
                            return 0;
                        }
                    },
                    format: "{0:n3}",
                    decimals: 3,
                    hidden: true,
                    width: '10%',
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

        vm.cancel = function(){
        	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }
        
        vm.closeErrImportPopUp = function(){
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
        
        vm.cancelInput = function(data){
        	if(data=='status'){
        		vm.searchForm.status = null;
        	}
        	if(data=='keySearch'){
        		vm.searchForm.keySearch = null;
        	}
        }
        
        vm.importFile = importFile;
		function importFile() {
			var teamplateUrl="coms/manageHcqt/importFileCons.html";
			var title="Import hoàn công quyết toán công trình";
			var windowId="IMPORT_MANAGE_HCQT";
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','275');
		}
     
		 vm.exportFile = function () {
                 // close popup and refresh table list
			 	vm.cntContractSearch.page = null;
				vm.cntContractSearch.pageSize = null;
				var data = manageHcqtService.doSearchV2(vm.cntContractSearch);
				console.log(data);
				manageHcqtService.doSearchV2(vm.cntContractSearch).then(function(d){
					CommonService.exportFile(vm.manageDataOSGrid,d.data,vm.listRemove,vm.listConvert,"Danh sách hoàn công quyêt toán công trình");
				});
	        }
		 
		 vm.getExcelTemplateImport = function () {
			 kendo.ui.progress($("#shipment"), true);
				return Restangular.all("manageHcqtService/getExcelTemplate").post(vm.searchForm).then(function (d) {
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
	        	debugger
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
				    	 url: Constant.BASE_SERVICE_URL+"manageHcqtService"+"/importManageHcqt?folder="+Constant.UPLOAD_FOLDER_TYPE_TEMP,
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
	        vm.exportExcelErr = function(){
	        	manageHcqtService.downloadErrorExcel(vm.objectErr).then(function(d) {
					data = d.plain();
					window.location = Constant.BASE_SERVICE_URL+"manageDataOutsideOsService/downloadFileTempATTT?" + data.fileName;
				}).catch( function(){
					toastr.error(gettextCatalog.getString("Lỗi khi export!"));
					return;
				});
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