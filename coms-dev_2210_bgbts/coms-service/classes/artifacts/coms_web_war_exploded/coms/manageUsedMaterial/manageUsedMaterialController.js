(function() {
	'use strict';
	var controllerId = 'manageUsedMaterialController';
	
	angular.module('MetronicApp').controller(controllerId, manageUsedMaterialController);
	
	function manageUsedMaterialController($scope, $rootScope, gettextCatalog, 
			kendoConfig, managUsedMaterialService,
			CommonService,htmlCommonService, Restangular, RestEndpoint, Constant, $timeout) {
		var vm = this;
		vm.provinceList = [];
		vm.showSearch = true;
        vm.searchForm = {};
        vm.addForm = {};
        vm.isCreate = false;
        vm.String = "Quản lý công trình > Nghiệm thu khối lượng vật tư đã sử dụng";
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
	        		]
        	
        	
        	}
        
       
        // tatph -end 13/11/2019
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
    						url: Constant.BASE_SERVICE_URL + "manageVttbService/doSearchUsedMaterial",
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
    				title: "Phiếu xuất kho",
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
    				title: "Mã VTTB",
    		        field: 'vttbCode',
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
    				title: "Số liệu theo PXK",
    		        width: '10%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    				columns: [{
        				title: "Số lượng",
        				field:"soLuongPxk",
        				width: '5%',
        		        columnMenu: false,
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
        				title: "Thành tiền",
        		        field: 'giaTriPxk',
        		        width: '5%',
        		        headerAttributes: {
        		        	style: "text-align:center; font-weight: bold;white-space: normal;",
        					translate:"",
        				},
        				attributes: {
        					style: "text-align:right;white-space: normal;"
        				},
        				format: "{0:n0}"
        			}]
    				
    			},
    			{
    				title: "Số thực tế thi công",
    		        width: '10%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    				columns: [{
        				title: "Số lượng",
        				field:"soLuongSuDung",
        				width: '5%',
        		        columnMenu: false,
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
        				title: "Thành tiền",
        		        field: 'giaTriSuDung',
        		        width: '5%',
        		        headerAttributes: {
        		        	style: "text-align:center; font-weight: bold;white-space: normal;",
        					translate:"",
        				},
        				attributes: {
        					style: "text-align:right;white-space: normal;"
        				},
        				format: "{0:n0}"
        			}]
    			},
    			{
    				title: "Số dư thừa thu hồi nhập kho",
    		        width: '10%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    				columns: [{
        				title: "Số lượng",
        				field:"soLuongThuHoi",
        				width: '5%',
        		        columnMenu: false,
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
        				title: "Thành tiền",
        		        field: 'giaTriThuHoi',
        		        width: '5%',
        		        headerAttributes: {
        		        	style: "text-align:center; font-weight: bold;white-space: normal;",
        					translate:"",
        				},
        				attributes: {
        					style: "text-align:right;white-space: normal;"
        				},
        				format: "{0:n0}"
        			}]
    			},
    			{
    				title: "Số liệu chênh lệch",
    		        width: '10%',
    		        headerAttributes: {
    		        	style: "text-align:center; font-weight: bold;white-space: normal;",
    					translate:"",
    				},
    				attributes: {
    					style: "text-align:right;white-space: normal;"
    				},
    				columns: [{
        				title: "Số lượng",
        				field:"soLuongDuThua",
        				width: '5%',
        		        columnMenu: false,
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
        				title: "Thành tiền",
        		        field: 'giaTriDuThua',
        		        width: '5%',
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
    					title: "Thao tác",
    					headerAttributes: {
    			        	style: "text-align:center; font-weight: bold;",
    						translate:"",
    					},
    			        template: dataItem =>
    					'<div class="text-center">'
    					+'<button style=" border: none; background-color: white;" id="updateId" ng-click="vm.edit(dataItem)"  class=" icon_table "'+
    					'   uib-tooltip="Sửa" translate>'+
    					'<i class="fa fa-pencil" aria-hidden="true"></i>'
    					+'</button>'
    					+'</div>',
    			        width: '150px',
    			        field:"action"
    				}]
    			}
    			]
    		});
        }
        
        vm.edit = function(dataItem){
        	vm.isCreate = false;
        	vm.isReject = false;
        	vm.addForm = angular.copy(dataItem);
        	var title = "Cập nhật dữ liệu thực tế thi công" ;
        	openPopup(title);
        }
        
        function openPopup(title){
      	  var teamplateUrl = "coms/manageUsedMaterial/popupEditManageUsedMaterial.html";
            var title = title;
            var windowId = "EDIT_MANAGE_VALUE";
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
        
        vm.cancelInput = function(data){
        	if(data=='status'){
        		vm.searchForm.status = null;
        	}
        	if(data=='keySearch'){
        		vm.searchForm.keySearch = null;
        	}
        }

        vm.save= function save(){
			data=vm.addForm;
			data.crateUserId = Constant.userInfo.VpsUserInfo.sysUserId ;
			managUsedMaterialService.saveUsedMaterial(data).then(function(result){
					$("#manageVttbGridId").data('kendoGrid').dataSource.read();
					$("#manageVttbGridId").data('kendoGrid').refresh();
	    			toastr.success("Cập nhật thành công!");
					$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
	    		}, function(errResponse){
	                	toastr.error(gettextCatalog.getString("Xảy ra lỗi khi cập nhật"));
		        });
	    	
		}
        
        
		 
		vm.exportFile= function exportFile(){
				return Restangular.all("manageVttbService/getExcelTemplate").post(vm.searchForm).then(function (d) {
	        	    var data = d.plain();
	        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
	        	}).catch(function (e) {
	        	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
	        	    return;
	        	});
		}
		 
// end controller
       }
})();