(function() {
	'use strict';
	var controllerId = 'manageValueController';
	
	angular.module('MetronicApp').controller(controllerId, manageValueController);
	
	function manageValueController($scope, $rootScope, gettextCatalog, 
			kendoConfig, manageValueService,
			CommonService,htmlCommonService, Restangular, RestEndpoint, Constant, $timeout) {
		var vm = this;
		
		vm.showSearch = true;
        vm.showDetail = false;
        vm.searchForm = {};
        vm.cntContractSearch = {};
        vm.addForm = {};
        vm.isCreate = false;
        vm.checkTTHT = false;
        vm.isReject = false;
        vm.String = "Quản lý công trình > Quản lý giá trị công trình > Quản lý giá trị thu hồi dòng tiền";
        init();
        function init(){
        	
        	fillDataTable([]);
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
	        				1: 'Chưa duyệt',
	        				2: 'Đã duyệt',
							3: 'Từ chối duyệt'
	        			}
	        		},
	        		]
        	
        	manageValueService.checkRoleTTHT().then(function(result){
        		if(result=='OK'){
        			vm.checkTTHT = true;
        		} else {
        			vm.checkTTHT = false;
        		}
        	});
        	
        	}
        
       
        //tatph -end 13/11/2019
        
        var record = 0;
        function fillDataTable(data){
        	kendo.ui.progress($("#manageValueGridId"), true);
        	vm.gridOptions = kendoConfig.getGridOptions({
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
                                  '<button class="btn btn-qlk padding-search TkQLK"'+
                                  'ng-click="vm.importFile()" uib-tooltip="Import" translate>Import</button>'+
                					'</div>'
                                 +
                              '<div class="btn-group pull-right margin_top_button margin_right10">'+
                              '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: Triệu VNĐ</div>'+
                              '<i data-toggle="dropdown" tooltip-placement="left" uib-tooltip="Cài đặt" aria-expanded="false"><i class="fa fa-cog" aria-hidden="true"></i></i>'+
                              '<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="vm.exportFile()" aria-hidden="true"></i>' +
                              '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
                              '<label ng-repeat="column in vm.manageValueGrid.columns| filter: vm.gridColumnShowHideFilter">'+
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
    						kendo.ui.progress($("#manageValueGridId"), false);
    						return response.data; 
    						},
    	                },
    				transport: {
    					read: {
    	                        // Thuc hien viec goi service
    						url: Constant.BASE_SERVICE_URL + "DetailMonthPlanOSRsService/doSearchManageValue",
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
    				title: "Khu vực",
    		        field: 'areaCode',
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
    				title: "Tỉnh",
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
    				title: "Hợp đồng",
    		        field: 'cntContractCode',
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
    				title: "Mã công trình",
    		        field: 'constructionCode',
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
    				title: "Số hóa đơn",
    		        field: 'billCode',
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
    				title: "Ngày hóa đơn",
    		        field: 'createdBillDate',
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
    				title: "Giá trị hóa đơn",
    		        field: 'billValue',
    		        width: '10%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    				template: function (dataItem) {
                        return dataItem.billValue !== 0 ? CommonService.numberWithCommas(dataItem.billValue) : dataItem.billValue;
                    }
    			},
    			{
    				title: "Người thực hiện",
    		        field: 'performerName',
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
    				title: "Thời gian bắt đầu",
    		        field: 'startDate',
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
    				title: "Thời gian hoàn thành thu tiền",
    		        field: 'endDate',
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
    				title: "Trạng thái",
    		        field: 'status',
    		        width: '10%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    				template: function(dataItem){
    					if(dataItem.status==1){
    						return "Chưa duyệt";
    					} else if(dataItem.status==2){
    						return "Đã duyệt";
    					} else if(dataItem.status==3){
    						return "Từ chối duyệt";
    					}
    				}
    			},
    			{
    				title: "Ghi chú",
    		        field: 'description',
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
					title: "Thao tác",
					headerAttributes: {
			        	style: "text-align:center; font-weight: bold;",
						translate:"",
					},
			        template: dataItem =>
					'<div class="text-center">'
					+'<button style=" border: none; background-color: white;" id="updateId" ng-click="vm.edit(dataItem)" ng-hide="(dataItem.status==2) || (!dataItem.performerId && !dataItem.startDate && !dataItem.endDate && vm.checkTTHT)" class=" icon_table "'+
					'   uib-tooltip="Sửa" translate>'+
					'<i class="fa fa-pencil" aria-hidden="true"></i>'
					+'</button>'
					+'<button style=" border: none; background-color: white;" id="sign" ng-hide="(dataItem.status==2 || dataItem.status==3 || !vm.checkTTHT)" ng-click="vm.updateRegistry(dataItem)" class=" icon_table "'+
					'   uib-tooltip="Phê duyệt" translate>'+
					'<i class="fa fa-arrow-up" style="color: #337ab7;" aria-hidden="true"></i>'
					+'</button>'
					+'<button style=" border: none; background-color: white;" id="sign" ng-hide="(dataItem.status==2 || dataItem.status==3 || !vm.checkTTHT)" ng-click="vm.rejectRegistry(dataItem)" class=" icon_table "'+
					'   uib-tooltip="Từ chối" translate>'+
					'<i class="fa fa-times" style="color: red;" aria-hidden="true"></i>'
					+'</button>'
					+'</div>',
			        width: '150px',
			        field:"action"
				}
    			////////////////////////
    			
    			////////////////////////
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
        	var grid = vm.manageValueGrid;
        	if(grid){
        		grid.dataSource.query({
    				page: 1,
    				pageSize: 10
    			});
        	}
        }
        
        

        vm.catTaskOptions1 = {
        		
                dataTextField: "fullName",
                dataValueField: "sysUserId",
                open: function(e) {
    				vm.isSelect = false;

    			},
                select: function (e) {
                	  vm.isSelect = true;
                    var data = this.dataItem(e.item.index());
                    vm.addForm.performerName = data.fullName;
                    vm.addForm.performerId = data.sysUserId;
                    $scope.sysUserId = data.sysUserId;
                },
                pageSize: 10,
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function (options) {
                            return Restangular.all("constructionTaskService/construction/searchPerformerV2").post({
                                keySearch: vm.addForm.performerName,
                                pageSize: 10,
                                page: 1
                            }).then(function (response) {
                                options.success(response);
                            }).catch(function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            });
                        }
                    }
                },
                template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.loginName #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
                change: function (e) {
                    
                    if(vm.isSelect) {
	        			if(vm.addForm.performerId!=  $scope.sysUserId) {
	        				vm.addForm.performerId = null;
	        				vm.addForm.performerName = null;
	        			}
	        		}
	        		if(!vm.isSelect) {
	        			vm.addForm.performerId = null;
	        			vm.addForm.performerName = null;
	        		}
                },
                ignoreCase: false
            }

        vm.edit = function(dataItem){
        	vm.isCreate = false;
        	vm.isReject = false;
        	vm.addForm = angular.copy(dataItem);
        	var title = "Cập nhật giá trị thu hồi dòng tiền" ;
        	openPopup(title);
        }
        
        vm.rejectRegistry = function(dataItem){
        	vm.isCreate = false;
        	vm.isReject = true;
        	vm.addForm = angular.copy(dataItem);
        	var title = "Từ chối giá trị thu hồi dòng tiền" ;
        	openPopup(title);

        }
        
        vm.reject = function (){
        	if(vm.addForm.reasonReject == null || vm.addForm.reasonReject == '' || vm.addForm.reasonReject == undefined){
        		toastr.error("Lý do không được để trống !");
				return;
        	}
        	confirm("Xác nhận từ chối duyệt bản ghi ?", function(){
    		manageValueService.updateRejectRevokeCash(vm.addForm).then(function(result){
    			if(result.error){
    				toastr.error("Có lỗi xảy ra khi từ chối");
    				return;
    			}
    			toastr.success("Từ chối thành công !");
    			vm.cancel();
    			doSearch();
    		});
    	});	
        }
        
        function openPopup(title){
        	  var teamplateUrl = "coms/manageValue/popupEditManageValue.html";
              var title = title;
              var windowId = "EDIT_MANAGE_VALUE";
              CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '85%', '85%', "deptAdd");
        }
        
        
        
        
       
        vm.importFile = importFile;
		function importFile() {
			var teamplateUrl="coms/manageValue/importFileCons.html";
			var title="Import giá trị thu hồi dòng tiển";
			var windowId="IMPORT_MANAGE_VALUE";
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','275');
		}
     
        vm.cancelInput = function(data){
        	if(data=='status'){
        		vm.searchForm.status = null;
        	}
        	if(data=='keySearch'){
        		vm.searchForm.keySearch = null;
        	}
        }
        
        vm.resetFormAdd = function(data){
        	if(data=='performer'){
        		vm.addForm.performerId = null;
        		vm.addForm.performerName = null;
        	}
        	if(data=='startDate'){
        		vm.addForm.startDate = null;
        	}
        	if(data=='endDate'){
        		vm.addForm.endDate = null;
        	}
        }
        
    	//Them moi, cap nhat lo hang
		vm.save= function save(){
			if(vm.addForm.cntContractCode == null){
				toastr.error("Mã hợp đồng không được để trống!");
				return;
			}
			if(vm.addForm.constructionCode == null){
				toastr.error("Mã công trình không được để trống!");
				return;
			}
			if(vm.addForm.billCode == null){
				toastr.error("Số hóa đơn không được để trống!");
				return;
			}
			if(vm.addForm.createdBillDate == null){
				toastr.error("Ngày hóa đơn không được để trống!");
				return;
			}
			if(vm.addForm.billValue == null){
				toastr.error("Giá trị hóa đơn không được để trống!");
				return;
			}
			if(vm.addForm.performerName == null || vm.addForm.performerId == null){
				toastr.error("Người thực hiện không được để trống!");
				return;
			}
			if(vm.addForm.startDate == null ){
				toastr.error("Thời gian bắt đầu không được để trống!");
				return;
			}
			if(vm.addForm.endDate == null ){
				toastr.error("Thời gian hoàn thành thu tiền không được để trống!");
				return;
			}
			data=vm.addForm;
			data.updatedUserId = Constant.userInfo.VpsUserInfo.sysUserId ;
			manageValueService.updateManageValue(data).then(function(result){
					$("#manageValueGridId").data('kendoGrid').dataSource.read();
					$("#manageValueGridId").data('kendoGrid').refresh();
	    			toastr.success("Cập nhật thành công!");
					$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
	    		}, function(errResponse){
	                	toastr.error(gettextCatalog.getString("Xảy ra lỗi khi cập nhật"));
		        });
	    	
		}
		
		vm.updateRegistry= function updateRegistry(data){
			if(data.cntContractCode == null){
				toastr.warning("Mã hợp đồng không được để trống!");
				return;
			}
			if(data.constructionCode == null){
				toastr.warning("Mã công trình không được để trống!");
				return;
			}
			if(data.billCode == null){
				toastr.warning("Số hóa đơn không được để trống!");
				return;
			}
			if(data.createdBillDate == null){
				toastr.warning("Ngày hóa đơn không được để trống!");
				return;
			}
			if(data.billValue == null){
				toastr.warning("Giá trị hóa đơn không được để trống!");
				return;
			}
			if(data.performerName == null || data.performerId == null){
				toastr.warning("Người thực hiện không được để trống!");
				return;
			}
			if(data.startDate == null ){
				toastr.warning("Thời gian bắt đầu không được để trống!");
				return;
			}
			if(data.endDate == null ){
				toastr.warning("Thời gian hoàn thành thu tiền không được để trống!");
				return;
			}
//			data.performerId = Constant.userInfo.VpsUserInfo.sysUserId ;
			confirm('Bạn thật sự muốn ký duyệt bản ghi đã chọn?', function () {
				manageValueService.approve(data).then(function(result){
					$("#manageValueGridId").data('kendoGrid').dataSource.read();
					$("#manageValueGridId").data('kendoGrid').refresh();
	    			toastr.success("Ký duyệt thành công!");
					$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
	    		}, function(errResponse){
	                	toastr.error(gettextCatalog.getString("Xảy ra lỗi khi Ký duyệt"));
		        });
			});
			
			
	    	
		}
        
        
		 
		 vm.exportFile = function () {
                 // close popup and refresh table list
			 	vm.cntContractSearch.page = null;
				vm.cntContractSearch.pageSize = null;
				var data = manageValueService.doSearchManageValue(vm.cntContractSearch);
				console.log(data);
				manageValueService.doSearchManageValue(vm.cntContractSearch).then(function(d){
					CommonService.exportFile(vm.manageValueGrid,d.data,vm.listRemove,vm.listConvert,"Danh sách giá trị thu hồi dòng tiền");
				});
                
			 
	            
	        }
		 vm.getExcelTemplateImport = function () {
			 return Restangular.all("DetailMonthPlanOSRsService/getExcelTemplate").post(vm.searchForm).then(function (d) {
	        	    var data = d.plain();
	        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
	        	    kendo.ui.progress($("#shipment"), false);
	        	}).catch(function (e) {
	        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
	        	    kendo.ui.progress($("#shipment"), false);
	        	    return;
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
				    	 url: Constant.BASE_SERVICE_URL+"DetailMonthPlanOSRsService"+"/importManageValue?folder="+Constant.UPLOAD_FOLDER_TYPE_TEMP,
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
				            		$("#manageValueGridId").data('kendoGrid').dataSource.read();
				       			 	$("#manageValueGridId").data('kendoGrid').refresh();
				       			 	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
				            	}
				            }
				        });
	        }
	        vm.exportExcelErr = function(){
	        	manageValueService.downloadErrorExcel(vm.objectErr).then(function(d) {
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