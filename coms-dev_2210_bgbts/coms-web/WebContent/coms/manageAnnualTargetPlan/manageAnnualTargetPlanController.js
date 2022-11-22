(function() {
	'use strict';
	var controllerId = 'manageAnnualTargetPlanController';
	
	angular.module('MetronicApp').controller(controllerId, manageAnnualTargetPlanController);
	
	function manageAnnualTargetPlanController($scope, $rootScope, gettextCatalog, 
			kendoConfig, manageAnnualTargetPlanService,$filter,
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
        	vm.monthList = [
            	{id:1, name: "Tháng 1"},
            	{id:2, name: "Tháng 2"},
            	{id:3, name: "Tháng 3"},
            	{id:4, name: "Tháng 4"},
            	{id:5, name: "Tháng 5"},
            	{id:6, name: "Tháng 6"},
            	{id:7, name: "Tháng 7"},
            	{id:8, name: "Tháng 8"},
            	{id:9, name: "Tháng 9"},
            	{id:10, name: "Tháng 10"},
            	{id:11, name: "Tháng 11"},
            	{id:12, name: "Tháng 12"}
            ];
        	
        	vm.yearList = [];
	        for(var i=(new Date().getFullYear() - 10);i<(new Date().getFullYear() + 10);i++){
	        	var yearls = {};
	        	yearls.code = i;
	        	yearls.name = i;
	        	vm.yearList.push(yearls);
	        }
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
        	kendo.ui.progress($("#manageAnnualTargetPlanGridId"), true);
        	vm.manageAnnualTargetPlanGridOptions = kendoConfig.getGridOptions({
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
                            	  '<button class="btn btn-qlk padding-search addQLK" '+
                                  'ng-click="vm.add()" uib-tooltip="Tạo mới" translate style="width: 100px;">Tạo mới</button>'+ 
                                  '<button class="btn btn-qlk padding-search TkQLK"'+
                                  'ng-click="vm.importFile()" uib-tooltip="Import" translate>Import</button>'+
                                  '</div>' +
                                
                              '<div class="btn-group pull-right margin_top_button margin_right10">'+
                              '<i data-toggle="dropdown" tooltip-placement="left" uib-tooltip="Cài đặt" aria-expanded="false"><i class="fa fa-cog" aria-hidden="true"></i></i>'+
                              '<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="vm.exportFile()" aria-hidden="true"></i>' +
                              '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
                              '<label ng-repeat="column in vm.manageAnnualTargetPlanGrid.columns| filter: vm.gridColumnShowHideFilter">'+
                              '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+
                              '</label>'+
                              '</div></div>'
                          }
                          ],
    			dataSource:{
    				serverPaging: true,
    				 schema: {
    					 total: function (response) {
    						    $("#manageAnnualTargetPlanCount").text(""+response.total);
    							return response.total; 
    						},
    						data: function (response) {	
    						kendo.ui.progress($("#manageAnnualTargetPlanGridId"), false);
    						return response.data; 
    						},
    	                },
    				transport: {
    					read: {
    	                        // Thuc hien viec goi service
    						url: Constant.BASE_SERVICE_URL + "manageAnnualTargetPlanService/doSearch",
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
    				title: "Năm",
    		        field: 'year',
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
    				title: "Tháng",
    		        field: 'month',
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
    				title: "Giá trị hợp đồng",
    		        field: 'contractValue',
    		        width: '10%',
    		        format:"{0:n3}",
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    			},
    			{
    				title: "Sản lượng thi công",
    		        field: 'tcValue',
    		        format:"{0:n3}",
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
    				title: "Doanh thu",
    		        field: 'doanhThu',
    		        format:"{0:n3}",
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
					title: "Thao tác",
					headerAttributes: {
			        	style: "text-align:center; font-weight: bold;",
						translate:"",
					},
			        template: dataItem =>
					'<div class="text-center">'
					+'<button style=" border: none; background-color: white;" id="updateId" ng-click="vm.edit(dataItem)" class=" icon_table "'+
					'   uib-tooltip="Sửa" translate>'+
					'<i class="fa fa-pencil" aria-hidden="true"></i>'
					+'</button>'
					
					+'<button style=" border: none; background-color: white;" id="removeId" ng-click="vm.remove(dataItem)" class=" icon_table "'+
					'   uib-tooltip="Xóa" translate>'+
					'<i class="fa fa-trash" aria-hidden="true"></i>'
					+'</button>'
					
					+'</div>',
			        width: '150px',
			        field:"action"
				}
    			
    			]
    		});
        }
        
        vm.showHideColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
            	vm.manageAnnualTargetPlanGrid.hideColumn(column);
            } else if (column.hidden) {
            	vm.manageAnnualTargetPlanGrid.showColumn(column);
            } else {
            	vm.manageAnnualTargetPlanGrid.hideColumn(column);
            }
        }
        
        vm.save= function save(){
			data=vm.addForm;
			data.createUserId = Constant.userInfo.VpsUserInfo.sysUserId ;
			if(vm.isCreate){
				manageAnnualTargetPlanService.save(data).then(function(result){
					$("#manageAnnualTargetPlanGridId").data('kendoGrid').dataSource.read();
					$("#manageAnnualTargetPlanGridId").data('kendoGrid').refresh();
	    			toastr.success("Thêm mới thành công!");
					$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
	    		}, function(errResponse){
	                	toastr.error(gettextCatalog.getString("Xảy ra lỗi khi cập nhật"));
		        });
			}else{
				manageAnnualTargetPlanService.update(data).then(function(result){
					$("#manageAnnualTargetPlanGridId").data('kendoGrid').dataSource.read();
					$("#manageAnnualTargetPlanGridId").data('kendoGrid').refresh();
	    			toastr.success("Cập nhật thành công!");
					$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
	    		}, function(errResponse){
	                	toastr.error(gettextCatalog.getString("Xảy ra lỗi khi cập nhật"));
		        });
			}
		}
        
        vm.remove = remove;
        function remove(dataItem) {
        	confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function () {
        		manageAnnualTargetPlanService.remove(dataItem).then(
                    function (da) {
                        var sizePage = $("#manageAnnualTargetPlanGridId").data("kendoGrid").dataSource.total();
                        var pageSize = $("#manageAnnualTargetPlanGridId").data("kendoGrid").dataSource.pageSize();
                        if (sizePage % pageSize === 1) {
                            var currentPage = $("#manageAnnualTargetPlanGridId").data("kendoGrid").dataSource.page();
                            if (currentPage > 1) {
                                $("#manageAnnualTargetPlanGridId").data("kendoGrid").dataSource.page(currentPage - 1);
                            }
                        }
                        $("#manageAnnualTargetPlanGridId").data('kendoGrid').dataSource.read();
                        $("#manageAnnualTargetPlanGridId").data('kendoGrid').refresh();
                        toastr.success("Xóa bản ghi thành công!");
                    }, function (errResponse) {
                        toastr.error("Lỗi không xóa được!");
                    });
            });
        }
        
        vm.add = function(){
        	vm.isCreate = true;
        	vm.addForm = {};
            var teamplateUrl = "coms/manageAnnualTargetPlan/popupEdit.html";
            var title = "Thêm mới";
            var windowId = "ADD_MANAGE_ANNUAL_TARGRT_PLAN";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '85%', '85%', "addManageTargetPlan");
        }
        
        vm.edit = function(dataItem){
        	vm.isCreate = false;
        	vm.addForm = {};
        	vm.addForm = angular.copy(dataItem);
            var teamplateUrl = "coms/manageAnnualTargetPlan/popupEdit.html";
            var title = "Sửa";
            var windowId = "EDIT_MANAGE_ANNUAL_TARGRT_PLAN";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '85%', '85%', "editManageTargetPlan");
        }
        
        
        vm.importFile = importFile;
		function importFile() {
			var teamplateUrl="coms/manageAnnualTargetPlan/importFileCons.html";
			var title="Import kế hoạch chỉ tiêu năm";
			var windowId="IMPORT_MANAGE_ANNUAL_TARGET_PLAN";
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','275');
		}
		
//		 vm.getExcelTemplateImport = function () {
//			 kendo.ui.progress($("#shipment"), true);
//				return Restangular.all("manageAnnualTargetPlanService/getExcelTemplate").post(vm.searchForm).then(function (d) {
//					kendo.ui.progress($("#shipment"), false);
//	        	    var data = d.plain();
//	        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
//	        	}).catch(function (e) {
//	        		kendo.ui.progress($("#shipment"), false);
//	        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
//	        	    return;
//	        	});
//	        }
		 
		 vm.getExcelTemplateImport = function () {
			 kendo.ui.progress($("#shipment"), true);
				return Restangular.all("manageAnnualTargetPlanService/getExcelTemplate").post(vm.searchForm).then(function (d) {
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
				    	 url: Constant.BASE_SERVICE_URL+"manageAnnualTargetPlanService"+"/importManageAnnualTargetPlan?folder="+Constant.UPLOAD_FOLDER_TYPE_TEMP,
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
				            		$("#manageAnnualTargetPlanGridId").data('kendoGrid').dataSource.read();
				       			 	$("#manageAnnualTargetPlanGridId").data('kendoGrid').refresh();
				       			 	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
				            	}
				            }
				        });
	        }
	        
	        
	        
	        vm.exportExcelErr = function(){
	        	manageAnnualTargetPlanService.downloadErrorExcel(vm.objectErr).then(function(d) {
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
//        	if (vm.searchForm.startDate != null && vm.searchForm.endDate != null) {
//                var fromDate = [];
//                var toDate = [];
//                fromDate = vm.searchForm.startDate.split('/');
//                toDate = vm.searchForm.endDate.split('/');
//                if (fromDate[2] > toDate[2]) {
//                    toastr.error('Đến ngày phải lớn hơn từ ngày ');
//                    $("#endDate").focus();
//                    return;
//                } else if (fromDate[2] == toDate[2]) {
//                    if (fromDate[1] > toDate[1]) {
//                        toastr.error('Đến ngày phải lớn hơn từ ngày ');
//                        $("#endDate").focus();
//                        return;
//                    } else if (fromDate[1] == toDate[1]) {
//                        if (fromDate[0] > toDate[0]) {
//                            toastr.error('Đến ngày phải lớn hơn từ ngày ');
//                            $("#endDate").focus();
//                            return;
//                        }
//                    }
//                }
//                vm.searchForm.startDate = vm.searchForm.startDate.split("/").reverse().join("-");;
//            	vm.searchForm.endDate = vm.searchForm.endDate.split("/").reverse().join("-");;
//            }
        	
        	
        	
        	var grid = vm.manageAnnualTargetPlanGrid;
        	if(grid){
        		grid.dataSource.query({
    				page: 1,
    				pageSize: 10
    			});
        	}
        }
		 
		 vm.exportFile = function () {
			 	vm.searchForm.page = null;
			 	vm.searchForm.pageSize = null;
				var data = manageAnnualTargetPlanService.doSearch(vm.searchForm);
				console.log(data);
				manageAnnualTargetPlanService.doSearch(vm.searchForm).then(function(d){
					CommonService.exportFile(vm.manageAnnualTargetPlanGrid,d.data,vm.listRemove,vm.listConvert,"Danh sách kế hoạch chỉ tiêu năm");
				});
        }
		 
// end controller
       }
})();