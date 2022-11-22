(function() {
	'use strict';
	var controllerId = 'managementCareerController';
	
	angular.module('MetronicApp').controller(controllerId, managementCareerController);
	
	function managementCareerController($scope,$templateCache, $rootScope, $timeout, gettextCatalog, 
			kendoConfig, $kWindow,managementCareerService, woManagementService,
			CommonService,htmlCommonService, PopupConst, Restangular, RestEndpoint, Constant) {
		var vm = this;
		
		vm.showSearch = true;
		vm.isCreateNew = false;
        vm.showDetail = false;
        vm.viewDetail = false;
        vm.disableBtnExcel = false;
        vm.disableBtnPDF = false;
        vm.breadcrumb = "WO xây lắp > Quản lý ngành nghề";
		vm.managementCareerSearch={
			status:'1',
		};
		vm.isLive = false;
		vm.folder='';
		vm.managementCareer={};
		
		vm.selectedCareer=false;
		
        $scope.woTypes = [];
        $scope.woNameList = {};
		// Khoi tao du lieu cho form
		
		initFormData();
		
		function initFormData() {
			fillDataTable([]);
			getWoTypes();
            getWoNameList();
		}
		
		console.log(Constant.userInfo);
		
		vm.validatorOptions = kendoConfig.get('validatorOptions');
		 setTimeout(function(){
			  $("#keySearch").focus();
			 
			},15);
		/*
		 * setTimeout(function(){ $("#appIds1").focus(); },15);
		 */
		 var record=0;
		 
	   function getWoNameList() {getWoNameList
	            woManagementService.getWoNameList().then(
	                function (resp) {
	                    if (resp.data && resp.statusCode == 1) {
	                        $scope.woTypeName = resp.data;
	                    }

	                },
	                function (error) {
	                    console.log(error);
	                    toastr.error("Có lỗi xảy ra!");
	                }
	            )
	    }
	   
	   function getWoTypes() {
           woManagementService.getWOTypes().then(
               function (resp) {
                   if (resp.data) {
                	   $scope.woTypes = resp.data;
                	   vm.woTypes = resp.data;
                   }
                   
               },
               function (error) {
                   toastr.error("Có lỗi xảy ra!");
               }
           )
       }
	   
	   function getWoTypeName(woTypeId) {
           for (var i = 0; i < $scope.woTypes.length; i++) {
               if ($scope.woTypes[i].woTypeId == woTypeId) return $scope.woTypes[i].woTypeName;
           }
       }

       function getWoTypeId(woTypeName) {
           for (var i = 0; i < $scope.woTypes.length; i++) {
               if ($scope.woTypes[i].woTypeName == woTypeName) return $scope.woTypes[i].woTypeId;
           }
       }
	   
		function fillDataTable(data) {
                vm.gridOptions = kendoConfig.getGridOptions({
				autoBind: true,
				scrollable: false, 
				resizable: true,
				editable: false,
				dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
                reorderable: true,
                          
                toolbar: [
                              {
                                  name: "actions",
                                  template:
                                	  '<div class=" pull-left ">'+
                                	  
                                      '<button class="btn btn-qlk padding-search-right addQLK"'+
                                      'ng-click="vm.add()" uib-tooltip="Tạo mới" translate>Tạo mới</button>'+
                                      '</div>'	
                      				+
                                	  '<div class="btn-group pull-right margin_top_button margin_right10">'+
    		                      	'<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'+
    		                      '<i class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()" aria-hidden="true"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>'+
    		                      
    		                      '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
    		                      '<label ng-repeat="column in vm.managementCareerGrid.columns| filter: vm.gridColumnShowHideFilter">'+
    		                      '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+
    		                    '</label>'+
    		                    '</div></div>'
                              }
                ],
				dataSource:{
					serverPaging: true,
					 schema: {
						 total: function (response) {
							    $("#careerCount").text(""+response.total);
							    vm.count=response.total;
								return response.total; // total is returned in
														// the "total" field of
														// the response
							},
							data: function (response) {				
							for(var x in response.data){
								response.data[x].sysUserId=Constant.userInfo.vpsUserToken.sysUserId
							}
							return response.data; // data is returned in
														// the "data" field of
														// the response
							},
		                },
					transport: {
						read: {
		                        // Thuc hien viec goi service
							url: Constant.BASE_SERVICE_URL + RestEndpoint.MANAGEMENT_CAREER_SERVICE_URL+ "/doSearch",
							contentType: "application/json; charset=utf-8",
							type: "POST"
						},					
						parameterMap: function (options, type) {
                           
								vm.managementCareerSearch.page = options.page;
								vm.managementCareerSearch.pageSize = options.pageSize;
								var obj = {};
								obj = angular.copy(vm.managementCareerSearch);
								if(!!obj.woIdList){
									var temp = obj.woIdList.join();
									obj.woIdList = temp;
								}
								return JSON.stringify(obj);
								
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
		                itemsPerPage: "kết quả/trang",
		                empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
		            }
				},
				columns: [{
					title: "TT",
					field:"stt",
					width: '10%',
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
				}
				,  
				{
					title: "Mã ngành nghề ",
					field: 'code',
					width: '30%',
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				},{
					title: "Tên ngành nghề",
			        field: 'name',
			        width: '50%',
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				},{
					title: "Danh sách WO áp dụng",
			        field: 'woListName',
			        width: '100%',
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
					
				},{
                    title: "Trạng thái",
                    field: 'status',
                    template: "# if(status == '0'){ #" + "#= 'Hết hiệu lực' #" + "# } " + "else if (status == '1') { # " + "#= 'Hiệu lực' #" + "#} #",
                    width: '30%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
                }
				
				,{
					title: "Thao tác",
					editable:false,
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    template: function(dataItem) {
                    var html =  '<div class="text-center">' +
                    '<button style=" border: none; background-color: white;" '+
					' class=" icon_table "'+
					'   uib-tooltip="Sửa" " ng-click="vm.edit(dataItem)" ng-show="dataItem.status==1" translate>'+
					'<i class="fa fa-pencil" aria-hidden="true"></i>'+
					'</button>'+
					'<button style=" border: none; background-color: white;"'+
					'class="#=appParamId# icon_table"  ng-click="vm.remove(dataItem)" ng-show="dataItem.status==1"  uib-tooltip="Xóa" translate'+
						'>'+
						'<i class="fa fa-trash" style="color: #337ab7;" aria-hidden="true"></i>'+
					'</button>'+
					'<button style=" border: none; background-color: white;" '+
					'class=" icon_table"'+
					'  uib-tooltip= "Xem chi tiết" ng-click="vm.view(dataItem)" ng-show="dataItem.status==0"  translate>'+
					'<i class="fa fa-list-alt" style="color:#e0d014;" aria-hidden="true"></i>'+
					'</button>'+
					'</div>';
                    return html;
				},
			     width: '20%',
			     field:"stt",
                },
            ]
        });
      }
		
		vm.listRemove=[{
			title: "Thao tác",
		}]
		
		
		vm.listConvert=[{
			field:"status",
			data:{
				'1':'Hiệu lực',
				'0':'Hết hiệu lực'
			}
		}]
		
		
		vm.exportFile = function () {
			if (vm.disableBtnPDF)
	    		return;
	    	vm.disableBtnExcel = true;
	    	$("#loadingCareer").addClass('loadersmall');
	    	//call api//
			return Restangular.all("managementCareerRsService/exportCareer").post(vm.managementCareerSearch).then(function (d) {
        	                var data = d.plain();
        	                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	                vm.disableBtnExcel = false;
        	                $("#loadingCareer").removeClass('loadersmall');
        	}).catch(function (e) {
        	            	vm.disableBtnExcel = false;
        	                toastr.error(gettextCatalog.getString("Lỗi khi tải xuống file  !"));
        	                $("#loadingCareer").removeClass('loadersmall');
            });
        }
		
		function initPopup() {
            vm.managementCareer = {};
            vm.isCreateNew = true;
        }
		
		
		vm.add = function add(){
			vm.showDetail = false;
			initPopup();
			var teamplateUrl="coms/wo_xl/managementCareer/managementCareerPopup.html";
			var title="Thêm mới ngành nghề";
			var windowId="COMS_WO_XL_MANAGEMENT_CAREER";
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'600','300',"code"); 
			 
		 }

		vm.edit=edit;
		function edit(dataItem){
				vm.showDetail = true;
				vm.isCreateNew = false;
				vm.viewDetail = false;
				vm.managementCareer = dataItem;
				var string = vm.managementCareer.woIdList;
				var array = string.split(",");
				vm.managementCareer.woIdList = array;
				var teamplateUrl="coms/wo_xl/managementCareer/managementCareerPopup.html";
				var title="Sửa đổi thông tin ngành nghề";
				var windowId="COMS_WO_XL_MANAGEMENT_CAREER";
				CommonService.populatePopupCreate(teamplateUrl,title,vm.managementCareer,vm,windowId,false,'600','300',"code"); 
				doSearch();
		}
		
		vm.view=view;
		function view(dataItem){
				vm.showDetail = false;
				vm.isCreateNew = false;
				vm.viewDetail = true;
				vm.managementCareer = dataItem;
				var string = vm.managementCareer.woIdList;
				var array = string.split(",");
				vm.managementCareer.woIdList = array;
				var teamplateUrl="coms/wo_xl/managementCareer/managementCareerPopup.html";
				var title="Chi tiết ngành nghề";
				var windowId="COMS_WO_XL_MANAGEMENT_CAREER";
				CommonService.populatePopupCreate(teamplateUrl,title,vm.managementCareer,vm,windowId,false,'600','300',"code"); 
				doSearch();
		}
		
		vm.remove=remove;
		function remove(dataItem){
			confirm('Xác nhận xóa',function(){
				managementCareerService.remove(dataItem).then(
						function(d) {
							if(d && d.error){
		                		toastr.error(d.error);
		                		return;
		                	}
							var sizePage = $("#managementCareerGrid").data("kendoGrid").dataSource.total();
							var pageSize = $("#managementCareerGrid").data("kendoGrid").dataSource.pageSize();
							
							if(sizePage % pageSize === 1){
								var currentPage = $("#managementCareerGrid").data("kendoGrid").dataSource.page();
								if (currentPage > 1) {
									$("#managementCareerGrid").data("kendoGrid").dataSource.page(currentPage - 1);
								}
							}
							 $("#managementCareerGrid").data('kendoGrid').dataSource.read();
							 $("#managementCareerGrid").data('kendoGrid').refresh();
							 toastr.success("Xóa thành công!");
							
						}, function(errResponse) {
							toastr.error("Lỗi không xóa được!");
						});
				})
		}
		
		
		
		
		
		vm.save= function save(data,isCreateNew){
			// chua validate cac truong

				var msg = " không được để trống !";
				if(vm.managementCareer.code == null ){
					toastr.warning("Mã ngành nghề" + msg);
					$("#code").focus();
					return;
				}
				
				if(vm.managementCareer.name==null || $("#name").val().trim()==""){
					toastr.warning("Tên ngành nghề" + msg);
					$("#name").focus();
					return;
				}
			
				
				if(vm.managementCareer.woIdList==null ){
					toastr.warning("Vui lòng chọn loại WO áp dụng. " );
					return;
				}
				
				var obj = {};
				obj.code = vm.managementCareer.code;
        		obj.name = vm.managementCareer.name;
        		console.log(vm.managementCareer.woIdList);
        		var temp = vm.managementCareer.woIdList.join();
				obj.woIdList = temp;	
				if(vm.isCreateNew==true) {	
					managementCareerService.create(obj).then(function(result){
	        			if(result.error){
							toastr.error(result.error);
							return;
						}
	        			toastr.success("Thêm mới thành công!");
	                    doSearch();
	                    document.getElementById('managementCareer').reset();
						$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
						
	        		}, function(errResponse){
		                if (errResponse.status === 409) {
		                	toastr.error(gettextCatalog.getString("Đã tồn tại mã ngành nghề này !"));
		                } else {
		                	toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi thêm mới ngành nghề !"));
		                }
			        });
	        	}else{
	        		var obj = {};
	        		obj.careerId = vm.managementCareer.careerId;
					obj.code = vm.managementCareer.code;
	        		obj.name = vm.managementCareer.name;
	        		console.log(vm.managementCareer.woIdList);
	        		var temp = vm.managementCareer.woIdList.join();
					obj.woIdList = temp;	
					
					managementCareerService.update(obj).then(function(result){
	        			if(result.error){
							toastr.error(result.error);
							return;
						}
						$("#managementCareerGrid").data('kendoGrid').dataSource.read();
						$("#managementCareerGrid").data('kendoGrid').refresh();
	        			toastr.success("Cập nhật thành công!");
	        			doSearch();
	        			$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
	        		}, function(errResponse){
	        			if (errResponse.status === 409) {
		                	toastr.error(gettextCatalog.getString("Đã tồn tại ngành nghề này !"));
		                	doSearch();
		                } else {
		                	toastr.error(gettextCatalog.getString("Xảy ra lỗi khi cập nhật"));
		                }
			        });
	        	}
		}
		
		function refreshGrid(d) {
			var grid = vm.managementCareerGrid;
			if(grid){
				grid.dataSource.data(d);
				grid.refresh();
			}
		}
		    
		
		
		vm.cancel= cancel ;
		function cancel(){
		/* confirm('Bạn có muốn hủy bỏ thao tác?', function(){ */
				$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
				/* }); */
		}
			
		vm.canceldoSearch= function (){
			 vm.showDetail = false;
			 vm.managementCareerSearch={
				status:"1",
				
			};
			doSearch();
		}
		
		vm.doSearch= doSearch;
		function doSearch(){
			vm.showDetail = false;
			var grid =vm.managementCareerGrid;	
			if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 10
				});
			}

			console.log(grid.dataSource.data());
		}
		
		
		vm.showHideColumn=function(column){
        	if (angular.isUndefined(column.hidden)) {
        		vm.managementCareerGrid.hideColumn(column);
            } else if (column.hidden) {
            	vm.managementCareerGrid.showColumn(column);
            } else {
            	vm.managementCareerGrid.hideColumn(column);
            }
        	
        	
        }
		/*
		 * * Filter các cột của select
		 */	
	
		vm.gridColumnShowHideFilter = function (item) { 
                return item.type==null||item.type !==1; 
         }
		

         vm.openForm = function(){
 			vm.isLive=true;
 		}
         vm.closeForm = function(){
 			vm.isLive=false;
 		}
                 
        vm.isSelect = false;
       
		vm.cancelCareers = function(){
			vm.managementCareerSearch.name = null;
		}
			
		vm.cancelInput = function (param) {
			if (param == 'woTypeName'){
				vm.managementCareerSearch.woIdList = null;
				vm.managementCareer.woIdList = null;
			}
			
		}
	}
})();
