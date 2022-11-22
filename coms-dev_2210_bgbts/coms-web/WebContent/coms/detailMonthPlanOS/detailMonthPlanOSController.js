(function() {
	'use strict';
	var controllerId = 'detailMonthPlanOSController';
	
	angular.module('MetronicApp').controller(controllerId, detailMonthPlanOSController);
	
	function detailMonthPlanOSController($scope, $rootScope, $timeout, gettextCatalog, 
			kendoConfig, $kWindow,detailMonthPlanOSService,$filter,
			CommonService, PopupConst, Restangular, RestEndpoint,Constant, htmlCommonService) {
        initCommonFunction($scope, $rootScope,$filter );
		var vm = this;
		vm.showSearch = true;
		vm.isCreateNew = false;
        vm.showDetail = false;
        vm.disabled = false;
        var year=(new Date()).getFullYear();
       
   

        vm.tab1 = true;
        vm.tab2 = false;
  
		vm.detailMonthPlanSearch={
				status			:1
		};
	
		vm.String="Quản lý công trình > Quản lý kế hoạch > Kế hoạch tháng chi tiết ngoài OS";
		vm.detailMonthPlan={};
        vm.detailMonthPlan.listConstrTaskIdDelete = [];
		vm.nextStep2 = function(){
            if(validateTotalMonthDetail()) {
                vm.detailMonthPlan.listConstrTaskIdDelete = [];
                vm.tab1 = false;
                vm.tab2 = true;
                $("#creImpReqBCIconOne").removeClass("acceptQLK postion-icon");
                $("#creImpReqBCIconOne").addClass("declineQLK postion-icon");
                $("#creImpReqBCone").removeClass("active");
                $("#creImpReqBCtwo").addClass("active");
                $("#creImpReqBCIconTwo").removeClass("declineQLK postion-icon");
                $("#creImpReqBCIconTwo").addClass("acceptQLK postion-icon");
            }

        } 

        vm.prevStep= function(){
        	vm.tab1 = true;
        	vm.tab2 = false;
        	$("#creImpReqBCIconTwo").removeClass("acceptQLK postion-icon");
			 $("#creImpReqBCIconTwo").addClass("declineQLK postion-icon");
			 $("#creImpReqBCtwo").removeClass("active");
			 $("#creImpReqBCone").addClass("active");
			 $("#creImpReqBCIconOne").removeClass("declineQLK postion-icon");
			 $("#creImpReqBCIconOne").addClass("acceptQLK postion-icon");

        }

		vm.cancelListMonth = function()
		{
			vm.detailMonthPlanSearch.monthList = [];
		}
		vm.cancelListStatus = function()
		{
			vm.detailMonthPlanSearch.signStateList = [];
		}
		vm.cancelListYear= function()
		{
			vm.detailMonthPlanSearch.yearList = [];
		}
		vm.cancelInput = function(param){
			if(param == 'dept1'){
				vm.detailMonthPlanSearch.sysGroupId= null;
				vm.detailMonthPlanSearch.sysGroupName= null;
			}
		}
		
		/*$(document).keyup(function(e){
			if(e.keyCode==13){
				doSearch();
			}
		})*/
		
		 vm.monthDownListOptions={
                dataSource:vm.monthDataList,
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true
          }
		//lua chon nam tim kiem
          
	    vm.yearDownListOptions={
            dataSource:vm.yearDataList,
            dataTextField: "name",
            dataValueField: "id",
            valuePrimitive: true
        }
		
		vm.openDepartmentTo1=openDepartmentTo1
  		
  		function openDepartmentTo1(popUp){
  			vm.obj={};
              vm.departmentpopUp=popUp;
  				var templateUrl = 'wms/popup/findDepartmentPopUp.html';
  				var title = gettextCatalog.getString("Tìm kiếm đơn vị");
  				CommonService.populatePopupDept(templateUrl, title, null,null, vm, popUp, 'string', false, '92%','89%');
  		}
        // clear data
		vm.changeDataAuto1=changeDataAuto1
		function changeDataAuto1(id){
			switch(id){
				case 'dept1':{
					if(processSearch(id,vm.selectedDept1)){
						vm.detailMonthPlanSearch.sysGroupId= null;
						vm.detailMonthPlanSearch.sysGroupName= null;
						vm.selectedDept1=false;
					}
					break;
				}
			}
		}
		
		// 8.2 Search SysGroup
		vm.selectedDept1=false;
	    vm.deprtOptions1 = {
  	           dataTextField: "text",
				dataValueField:"id",
  	            select: function(e) {
				vm.selectedDept1=true;
  	                var dataItem = this.dataItem(e.item.index());
  	              vm.detailMonthPlanSearch.sysGroupName = dataItem.text;;
  	            vm.detailMonthPlanSearch.sysGroupId = dataItem.id;
  	            },
  	            pageSize: 10,
				open: function(e) {
	                        vm.selectedDept1 = false;
	                    },  
  	            dataSource: {
  	                serverFiltering: true,
  	                transport: {
  	                    read: function(options) {
						vm.selectedDept1=false;
  	                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({name:vm.detailMonthPlanSearch.sysGroupName,pageSize:vm.deprtOptions1.pageSize}).then(function(response){
  	                        	options.success(response);
  	                        }).catch(function (err) {
  	                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
  	                        });
  	                    }
  	                }
  	            },
  	            template:'<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
  	            change: function(e) {
  	                if (e.sender.value() === '') {
  	                	vm.detailMonthPlanSearch.sysGroupName = null;// thành name
  	                	vm.detailMonthPlanSearch.sysGroupId = null;
  	  	                }
  	            },
  	            ignoreCase: false
  	        }
		// Khoi tao du lieu cho form
	    initTab2OSFunction($scope, $rootScope, $timeout, gettextCatalog,
            kendoConfig, $kWindow,detailMonthPlanOSService,
            CommonService, PopupConst, Restangular, RestEndpoint,Constant,vm, htmlCommonService);
		initFormData();
		function initFormData() {
			fillDataTable([]);
			initDropDownList();

		}
		// Khoi tao du lieu cho form
        function initTab2Table(){
            vm.initAllTab();
            vm.fillTotalMonthPlanDetailTable();
            vm.fillDetailsourceGridTable();
            vm.fillDetailforceMaintainGridTable();
            vm.fillDetailcashFlowDetailGridTable();
            vm.fillDataOtherJobsGrid();
            vm.fillDataRentGroundGrid();
        }
        function initDropDownList(){
            vm.yearDataList=[];
            vm.monthDataList=[];
            var currentYear = (new Date()).getFullYear();
            for(var i =currentYear-2;i<currentYear+19;i++){
                vm.yearDataList.push({
                    id:i,
                    name:i

                })
            }
           /* for(var i =1;i<13;i++){
                vm.monthDataList.push({
                    id:i,
                    name:i

                })
            }*/
            vm.monthDataList = [
                {id:1,name:'01'},
                {id:2,name:'02'},
                {id:3,name:'03'},
                {id:4,name:'04'},
                {id:5,name:'05'},
                {id:6,name:'06'},
                {id:7,name:'07'},
                {id:8,name:'08'},
                {id:9,name:'09'},
                {id:10,name:'10'},
                {id:11,name:'11'},
                {id:12,name:'12'}
            ];
            vm.yearDownListOptions={
                dataSource:vm.yearDataList,
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true
            }
            vm.monthDownListOptions={
                dataSource:vm.monthDataList,
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true
            }
        }
		vm.validatorOptions = kendoConfig.get('validatorOptions');
		 vm.formatAction=function(dataItem){
			 var template=
			 '<div class="text-center #=detailMonthPlanId#"">'				 
				 template+='<button type="button"'+
				'class="btn btn-default padding-button box-shadow  #=detailMonthPlanId#"'+
				'disble="" ng-click=vm.edit(#=detailMonthPlanId#)>'+
				'<div class="action-button edit" uib-tooltip="Sửa" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>'+
			'</button>'+
			'<button type="button"'+
			'class="btn btn-default padding-button box-shadow #=detailMonthPlanId#"'+
				'ng-click=vm.send(#=detailMonthPlanId#)>'+
				'<div class="action-button export" uib-tooltip="Gửi tài chính" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>'+
			'</button>'+
			'<button type="button"'+
			'class="btn btn-default padding-button box-shadow #=detailMonthPlanId#"'+
				'ng-click=vm.remove(#=detailMonthPlanId#)>'+
				'<div class="action-button del" uib-tooltip="Xóa" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>'+
			'</button>'
				+
				'<button type="button" class="btn btn-default padding-button box-shadow #=detailMonthPlanId#"'+
				'ng-click=vm.cancelUpgradeLta(#=detailMonthPlanId#)>'+
				'<div class="action-button cancelUpgrade" uib-tooltip="Hủy nâng cấp" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>'+
			'</button>';
				template+='</div>';
			 return dataItem.groupId;
		 }
		 setTimeout(function(){
			  $("#keySearch").focus();
			},15);
		/*
		 * setTimeout(function(){ $("#appIds1").focus(); },15);
		 */
		 var record=0;
		 
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
	                  				
	                  				'<div class="btn-group pull-right margin_top_button margin10">'+
	                              	'<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'+
	                              	'<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportExcelGrid()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>'+
	        	                    '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
	        	                    '<label ng-repeat="column in vm.detailMonthPlanGrid.columns| filter: vm.gridColumnShowHideFilter">'+
	        	                    '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+
	        	                    '</label>'+                    
	        	                    '</div></div>'
	                          }
	                          ],
					dataSource:{
						serverPaging: true,
						 schema: {
							 total: function (response) {
								    $("#appCountDetailMonth").text(""+response.total);
								    vm.countDetailMonth=response.total;
									return response.total; // total is returned in
															// the "total" field of
															// the response
								},
								data: function (response) {				
								return response.data; // data is returned in
															// the "data" field of
															// the response
								},
			                },
						transport: {
							read: {
			                        // Thuc hien viec goi service
								url: Constant.BASE_SERVICE_URL + "DetailMonthPlanOSRsService/doSearch",
								contentType: "application/json; charset=utf-8",
								type: "POST"
							},					
							parameterMap: function (options, type) {
	                              // vm.detailMonthPlanSearch.employeeId =
									// Constant.user.srvUser.catEmployeeId;
								    vm.detailMonthPlanSearch.page = options.page;
									vm.detailMonthPlanSearch.pageSize = options.pageSize;
									vm.detailMonthPlanSearch.type = "1";
									return JSON.stringify(vm.detailMonthPlanSearch);
							

							}
						},					 
						pageSize: 10
					},
	// dataSource: data,
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
					columns: [
					          {
						title: "TT",
						field:"stt",
				        template: function () {
						  return ++record;
						 },
				        width: '5%',
				        columnMenu: false,
				        headerAttributes: {style: "text-align:center;font-weight: bold;"},
						attributes: {
							style: "text-align:center;"
						},
					}
					,  {
						title: "Mã kế hoạch",
				        field: 'code',
				        width: '15%',
				        headerAttributes: {style: "text-align:center;font-weight: bold;"},
						attributes: {
							style: "text-align:left;"
						},
					},{
						title: "Tên kế hoạch",
						field: 'name',
				        width: '20%',
				        headerAttributes: {style: "text-align:center;font-weight: bold;"},
						attributes: {
							style: "text-align:left;"
						},
					}, {
						title: "Đơn vị thực hiện",
				        field: 'sysName',
				        width: '20%',
				        headerAttributes: {style: "text-align:center;font-weight: bold;"},
						attributes: {
							style: "text-align:left;"
						},
				        //template:function(dataItem){
				        //	return dataItem.sysName;
				        //}
				        
					}, 
					{
						title: "Ngày tạo",
				        field: 'createdDate',
				        width: '10%',
				        headerAttributes: {style: "text-align:center;font-weight: bold;"},
						attributes: {
							style: "text-align:center;"
						}
				        
					},
					{
						title: "Tháng thực hiện",
				        field: 'year',
				        width: '10%',
				        headerAttributes: {style: "text-align:center;font-weight: bold;"},
						attributes: {
							style: "text-align:center;"
						},
				        template:function(dataItem){
				        	return dataItem.month+"/"+dataItem.year;
				        }
				        
					},{
						title: "Tình trạng ký CA",
				        field: 'signState',
				        width: '10%',
				        headerAttributes: {style: "text-align:center;font-weight: bold;"},
						attributes: {
							style: "text-align:left;"
						},
						template :  "# if(signState == 1){ #" + "#= 'Chưa trình ký' #" + "# } " + "else if (signState == 2) { # " + "#= 'Đang trình ký' #"+ "#}" + "else if (signState == 3) { # " + "#= 'Đã ký duyệt' #"+ "#} " + "else if (signState == 4) { # " + "#= 'Từ chối ký duyệt' #"+ "#} #"
					}, 
					
					{
						title: "Trạng thái",
						 field: 'status',
				        template :  "# if(status == 0){ #" + "#= 'Hết hiệu lực' #" + "# } " + "else if (status == 1) { # " + "#= 'Hiệu lực' #"+ "#} #",
				        width: '10%',
				        headerAttributes: {style: "text-align:center;font-weight: bold;"},
						attributes: {
							style: "text-align:left;"
						},
					},{
						title: "Thao tác",
						 headerAttributes: {style: "text-align:center;font-weight: bold;"},
				        template: dataItem =>
						'<div class="text-center">'
					+'<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.edit(dataItem)" class=" icon_table "'+
					'   uib-tooltip="Xem chi tiết" translate>'+
					'<i class="fa fa-list-alt" style="color:#e0d014"   aria-hidden="true"></i>'+
				'</button>'
						+'<button style=" border: none; background-color: white;" id="updateId" ng-hide="dataItem.status==0" ng-click="vm.copy(dataItem)" class=" icon_table "'+
						'   uib-tooltip="Sao chép" translate>'+
						'<i class="fa fa-files-o"style="color: #337ab7;"  ng-hide="dataItem.status==0"   aria-hidden="true"></i>'+
					'</button>'
                        + '<button style=" border: none; background-color: white;" id="sign" ng-hide="dataItem.status==0||dataItem.signState==3" ng-click="vm.updateRegistry(dataItem)" class=" icon_table"' +
                        '   uib-tooltip="Trình ký" translate>' +
                        '<i class="fa fa-arrow-up" style="color: #337ab7;"  ng-hide="dataItem.status==0||dataItem.signState==3"   aria-hidden="true"></i>'
                        + '</button>'
                        + '<button style=" border: none; background-color: white;" id="sign" ng-show="dataItem.signState==3" ng-click="vm.viewSignedDoc(dataItem)" class=" icon_table"' +
                        '   uib-tooltip="Xem văn bản đã ký" translate>' +
                        '<i class="fa fa-file-text-o"style="color: #337ab7; ng-show="dataItem.signState==3"   aria-hidden="true"></i>'
                        + '</button>'+
						'<button style=" border: none; background-color: white;" id="removeId"'+
						'class="#=appParamId# icon_table" ng-click="vm.remove(dataItem)" ng-disabled="dataItem.status==0"  uib-tooltip="Xóa" translate'+
							'>'+
							'<i class="fa fa-trash" style="color: #337ab7;" ng-disabled="dataItem.status==0                               "  aria-hidden="true"></i>'+
						'</button>'
						+'</div>',
				        width: '20%'
					}
					]
				});
 
		 }

    vm.exportExcelGrid= function(){
    	function displayLoading(target) {
			var element = $(target);
			kendo.ui.progress(element, true);
			setTimeout(function(){
    	  
				return Restangular.all("DetailMonthPlanOSRsService/exportDetailMonthPlan").post(vm.detailMonthPlanSearch).then(function (d) {
			        var data = d.plain();
			        window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
			        kendo.ui.progress(element, false);
			    }).catch(function (e) {
			    	kendo.ui.progress(element, false);
			        toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
			        return;
			    });;
			});
		
  }
	displayLoading(".tab-content");
    

}
    
    vm.updateRegistry = updateRegistry;
	function updateRegistry(dataItem){
        return Restangular.all("DetailMonthPlanRsService/checkPermissionsRegistry").post(dataItem).then(function (d) {
            if(d.error){
                toastr.error(d.error);
                return;
            }
        }).catch(function (e) {
            confirm('Bạn thật sự muốn trình ký bản ghi đã chọn?', function () {
            	detailMonthPlanOSService.updateRegistry(dataItem).then(
                    function (d) {
                        toastr.success("Trình ký bản ghi thành công!");
                        var sizePage = $("#detailMonthPlanGrid").data("kendoGrid").dataSource.total();
                        var pageSize = $("#detailMonthPlanGrid").data("kendoGrid").dataSource.pageSize();
                        if (sizePage % pageSize === 1) {
                            var currentPage = $("#detailMonthPlanGrid").data("kendoGrid").dataSource.page();
                            if (currentPage > 1) {
                                $("#detailMonthPlanGrid").data("kendoGrid").dataSource.page(currentPage - 1);
                            }
                        }
                        $("#detailMonthPlanGrid").data('kendoGrid').dataSource.read();
                        $("#detailMonthPlanGrid").data('kendoGrid').refresh();

                    }, function (errResponse) {
                        toastr.error("Lỗi không trình ký được!");
                    });
            })
        })
}

    vm.exportExcelTab1= function(){
    	function displayLoading(target) {
			var element = $(target);
			kendo.ui.progress(element, true);
			setTimeout(function(){
    	  
				return Restangular.all("DetailMonthPlanOSRsService/exportDetailMonthPlanTab1").post( vm.searchForm).then(function (d) {
		            var data = d.plain();
		            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
		            kendo.ui.progress(element, false);
		        }).catch(function (e) {
		        	kendo.ui.progress(element, false);
		            toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
		            return;
		        });
			});
		
  }
	displayLoading(".tab-content");
        

    }
    vm.exportExcelTab2= function(){
    	function displayLoading(target) {
			var element = $(target);
			kendo.ui.progress(element, true);
			setTimeout(function(){
    	  
				return Restangular.all("DetailMonthPlanOSRsService/exportDetailMonthPlanTab2").post( vm.searchFormHSHC).then(function (d) {
		            var data = d.plain();
		            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
		            kendo.ui.progress(element, false);
		        }).catch(function (e) {
		        	kendo.ui.progress(element, false);
		        	toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
		            return;
		        });
			});
		
  }
	displayLoading(".tab-content");
        

    }
    vm.exportExcelTab3= function(){
    	function displayLoading(target) {
			var element = $(target);
			kendo.ui.progress(element, true);
			setTimeout(function(){
    	  
				return Restangular.all("DetailMonthPlanOSRsService/exportDetailMonthPlanTab3").post( vm.searchFormForceMainTain).then(function (d) {
		            var data = d.plain();
		            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
		            kendo.ui.progress(element, false);
		        }).catch(function (e) {
		        	kendo.ui.progress(element, false);
		            toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
		            return;
		        });
			});
		
  }
	displayLoading(".tab-content");
        

    }
    vm.exportExcelTab4= function(){
    	function displayLoading(target) {
			var element = $(target);
			kendo.ui.progress(element, true);
			setTimeout(function(){
    	  
				return Restangular.all("DetailMonthPlanOSRsService/exportDetailMonthPlanBTS").post( vm.searchFormBTS).then(function (d) {
		            var data = d.plain();
		            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
		            kendo.ui.progress(element, false);
		        }).catch(function (e) {
		        	kendo.ui.progress(element, false);
		            toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
		            return;
		        });
			});
		
  }
	displayLoading(".tab-content");
        

    }
    vm.exportExcelTab5= function(){
//    	function displayLoading(target) {
//			var element = $(target);
//			kendo.ui.progress(element, true);
//			setTimeout(function(){
//    	  
//				return Restangular.all("DetailMonthPlanOSRsService/exportDetailMonthPlanTab5").post( vm.searchFormCashFlow).then(function (d) {
//		            var data = d.plain();
//		            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
//		            kendo.ui.progress(element, false);
//		        }).catch(function (e) {
//		        	kendo.ui.progress(element, false);
//		            toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
//		            return;
//		        });
//			});
//		
//  }
//	displayLoading(".tab-content");
    	kendo.ui.progress($(".tab-content"), true);
    	vm.searchFormCashFlow.page = null;
		vm.searchFormCashFlow.pageSize = null;
		vm.searchFormCashFlow.detailMonthPlanId = vm.detailMonthPlan.detailMonthPlanId;
		detailMonthPlanOSService.getRevokeCashMonthPlanByPlanId(vm.searchFormCashFlow).then(function(d){
			CommonService.exportFile(vm.CashFlow,d.data,vm.listRemove,vm.listConvert,"Danh sách công trình thu hồi dòng tiền");
			kendo.ui.progress($(".tab-content"), false);
		});   

    }
    vm.exportExcelTab6= function(){
    	function displayLoading(target) {
			var element = $(target);
			kendo.ui.progress(element, true);
			setTimeout(function(){
    	  
				return Restangular.all("DetailMonthPlanOSRsService/exportDetailMonthPlanTab6").post( vm.searchFormOtherJobs).then(function (d) {
		            var data = d.plain();
		            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
		            kendo.ui.progress(element, false);
		        }).catch(function (e) {
		        	kendo.ui.progress(element, false);
		            toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
		            return;
		        });
			});
		
  }
	displayLoading(".tab-content");
        

    }

    
    vm.exportExcelTab7 = function exportFile() {
		vm.searchFormRent.page = null;
		vm.searchFormRent.pageSize = null;
		vm.searchFormRent.detailMonthPlanId = vm.detailMonthPlan.detailMonthPlanId;
		vm.searchFormRent.type = "4";
		detailMonthPlanOSService.doSearchRent(vm.searchFormRent).then(function(d){
			CommonService.exportFile(vm.rentGroundGrid, d.data,vm.listRemove,vm.listConvert,"Danh sách thuê mặt bằng trạm");
		});
	}

vm.handleCheck=handleCheck;
		 function handleCheck(dataItem){
		     if(dataItem.selected){
		         $scope.listCheck.push(dataItem);
		     } else {
		         for(var i=0;i<$scope.listCheck.length;i++){
		             if($scope.listCheck[i].detailMonthPlanId===dataItem.detailMonthPlanId){
		                 $scope.listCheck.splice(i,1);
		             }
		         }
		         $('[name="gridchkselectall"]').prop('checked', false);
		     }
		 }
		 vm.chkSelectAll=chkSelectAll;
		 $scope.checkSearch=false;
		 function chkSelectAll(){
		     var grid = vm.detailMonthPlanGrid;
		     chkSelectAllBase(vm.chkAll, grid);
		     if(vm.chkAll){
		         if( $scope.checkSearch && $scope.dataSearch.length >0){
		             $scope.listCheck=$scope.dataSearch;
		         } else {

		             CommonService.getallData("DetailMonthPlanOSRsService/doSearch",vm.detailMonthPlanSearch).then(function(data){
		                 $scope.listCheck=data.plain().data;
		             })
		         }
		     } else {
		         $scope.listCheck=[];
		     }
		 };

		vm.listRemove=[{
			title: "Thao tác",
		}]
		vm.listConvert=[{
			field:"status",
			data:{
				1:'Hiệu lực',
				0:'Hết Hiệu lực'
			}
		}]
		
		
		vm.exportFile = function exportFile() {
			var data = vm.detailMonthPlanGrid.dataSource.data();
				CommonService.exportFile(vm.detailMonthPlanGrid,data,vm.listRemove,vm.listConvert);
		}
		
		
		function refreshGrid(d) {
			var grid = vm.detailMonthPlanGrid;
			if(grid){
				grid.dataSource.data(d);
				grid.refresh();
			}
		}
		vm.add=add;
		function add(){
			vm.isCreateNew = true;
            return Restangular.all("DetailMonthPlanOSRsService/checkPermissionsAdd").post().then(function (d) {
                if(d.error){
                    toastr.error(d.error);
                    return;
                }
            }).catch(function (e) {
                initTab2Table();
                vm.showDetail = true;
                vm.sysGroupCode = '';
                vm.String = "Quản lý công trình > Quản lý kế hoạch > Thêm mới kế hoạch tháng chi tiết ngoài OS";
                var year = (new Date()).getFullYear();
                var month = (new Date()).getMonth() + 1;
                vm.detailMonthPlan = {
                    year: year,
                    month: month
                }
                vm.detailMonthPlan.name = 'Kế hoạch chi tiết thực hiện tháng ' + vm.detailMonthPlan.month + "/" + year + '-' + vm.sysGroupCode;
                detailMonthPlanOSService.getSequence({}).then(function (seq) {
                    vm.seq = seq;
                    vm.detailMonthPlan.code = seq + '/KHTCT/' + vm.sysGroupCode + '-CTCT/XL' + year + vm.detailMonthPlan.month;
                }, function (err) {

                });
            })
		}
		vm.changeCodeName = changeCodeName
        function changeCodeName(){
            vm.detailMonthPlan.name='Kế hoạch chi tiết thực hiện tháng '+vm.detailMonthPlan.month+"/"+vm.detailMonthPlan.year+'-'+vm.sysGroupCode;
            detailMonthPlanOSService.getSequence({}).then(function(seq){
                vm.detailMonthPlan.code=seq+'/KHTCT/'+vm.sysGroupCode+'-CTCT/XL'+vm.detailMonthPlan.year+vm.detailMonthPlan.month;
            },function(err){

            });
        }
		vm.edit=edit;
		function edit(dataItem){
			vm.isCreateNew = false;
            vm.disabled = dataItem.signState!=1||dataItem.status ==0;
            initTab2Table();
            detailMonthPlanOSService.getById(dataItem.detailMonthPlanId).then(function(result){
                vm.String="Quản lý công trình > Quản lý kế hoạch > Chỉnh sửa kế hoạch tháng chi tiết ngoài OS";
                vm.showDetail = true;
                vm.detailMonthPlan =result;
                vm.selectedDeptChoose=true;
                vm.sysGroupCode = result.sysGroupCode;
                $timeout(function(){
                    vm.targetGrid.dataSource.page(1);
                    vm.OtherJobs.dataSource.page(1);
                    vm.sourceGrid.dataSource.page(1);
                    vm.forceMaintainGrid.dataSource.page(1);
                    vm.BTSGrid.dataSource.page(1);
                    vm.CashFlow.dataSource.page(1);
                    vm.rentGroundGrid.dataSource.page(1);
                },2000)
                return Restangular.all("DetailMonthPlanOSRsService/checkPermissionsUpdate").post(dataItem).then(function (d) {
                    if(d==2){
                        vm.disableUpdateDetail = true;
                    }
                    else if(d == 0){
                        vm.disableUpdateDetail = false;
                    }
                })
            }, function(errResponse){
                    toastr.error("Có lỗi xảy ra!");
            });

		}
		vm.copy=copy;
		function copy(dataItem){
            return Restangular.all("DetailMonthPlanOSRsService/checkPermissionsCopy").post().then(function (d) {
                if(d.error){
                    toastr.error(d.error);
                    return;
                }
            }).catch(function (e) {
                if (vm.detailMonthPlan.detailMonthPlanId == "" || vm.detailMonthPlan.detailMonthPlanId == undefined) {
                	   initTab2Table();
                    detailMonthPlanOSService.getById(dataItem.detailMonthPlanId).then(function (result) {
                        vm.String = "Quản lý công trình > Quản lý kế hoạch > Sao chép kế hoạch tháng chi tiết";
                        vm.showDetail = true;
                        vm.detailMonthPlan = result;
                        vm.selectedDeptChoose = true;
                        vm.sysGroupCode = result.sysGroupCode;

                        $timeout(function(){
                            vm.targetGrid.dataSource.page(1);
                            vm.OtherJobs.dataSource.page(1);
                            vm.sourceGrid.dataSource.page(1);
                            vm.forceMaintainGrid.dataSource.page(1);
                            vm.BTSGrid.dataSource.page(1);
                            vm.CashFlow.dataSource.page(1);
                            vm.detailMonthPlan.detailMonthPlanId = "";
                        },2000)
                    }, function (errResponse) {
                        toastr.error("Có lỗi xảy ra!");
                    });
						vm.detailMonthPlan.detailMonthPlanId = "";
                }

            })
		}


        function fillGrid(id,data){
            var grid = $(id).data("kendoGrid");
            grid.dataSource.data(data);
            grid.refresh();
        }

        function getDataGrid(id){
            var grid = $(id).data("kendoGrid");
            if(grid){
                return grid.dataSource._data;
            }
            return null;
        }

		

		vm.cancel= cancel ;
		function cancel(){
				$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
				/* }); */
		}
//      hungnx 20180619 start
        vm.loadingSave = false;
//      hungnx 20180619 end
		vm.save= function() {
			vm.disabled=true;
		    if (validateTotalMonthDetail()) {
                var data = populateDataToSave();
                
                data.listTC=vm.targetGrid.dataSource.data();
                data.listHSHC=getDataGrid("#sourceGrid");
                data.listLDT=getDataGrid("#forceMaintainGrid");
                data.listDmpnOrder=getDataGrid("#BTSGrid");
                data.listDT=getDataGrid("#CashFlow");
                data.listCVK=getDataGrid("#OtherJobs");
                data.listRevokeDT = getDataGrid("#CashFlow");
                data.listRentGround = getDataGrid("#rentGroundGrid");
                if(data.listTC.length==0 && data.listHSHC.length==0 && data.listLDT.length==0 && data.listRevokeDT.length==0 && data.listRentGround.length==0){
                	toastr.warning("Chưa có dữ liệu trong bảng Thi công, Quỹ lương,...");
                	vm.disabled=false;
                	return;
                }
//              hungnx 20180619 start
                vm.loadingSave = true;
//              hungnx 20180619 end
		        if (vm.detailMonthPlan.detailMonthPlanId == null || vm.detailMonthPlan.detailMonthPlanId == '') {

		            detailMonthPlanOSService.createdetailMonthPlan(data).then(function (result) {
		                if (result.error) {
		                    $('#year').focus();
		                    toastr.error(result.error);
//	                      hungnx 20180619 start
	                        vm.loadingSave = false;
//	                      hungnx 20180619 end
		                    return;
		                }
		                toastr.success("Thêm mới thành công!");
		                vm.cancelAdd();
//                      hungnx 20180619 start
                        vm.loadingSave = false;
//                      hungnx 20180619 end
		            }, function (errResponse) {
                        toastr.error("Có lỗi xãy ra!");
//                      hungnx 20180619 start
                        vm.loadingSave = false;
//                      hungnx 20180619 end
 //                       vm.cancelAdd()
		            });
		        }
		        else {
		            detailMonthPlanOSService.updatedetailMonthPlan(data).then(function (result) {
		                if (result.error) {
		                    $('#year').focus();
		                    toastr.error(result.error);
//	                      hungnx 20180619 start
	                        vm.loadingSave = false;
//	                      hungnx 20180619 end
		                    return;
		                }
		                toastr.success("Chỉnh sửa thành công!");
		                vm.cancelAdd();
//                      hungnx 20180619 start
                        vm.loadingSave = false;
//                      hungnx 20180619 end
		            }, function (errResponse) {
		                toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi chỉnh sửa"));
//                      hungnx 20180619 start
                        vm.loadingSave = false;
//                      hungnx 20180619 end
		            });
		        }

		    }
		    vm.disabled=false;
		}	

		function populateDataToSave(){
		    var data = vm.detailMonthPlan;
            data.listTC=vm.targetGrid.dataSource.data();
            data.listHSHC=getDataGrid("#sourceGrid");
            data.listLDT=getDataGrid("#forceMaintainGrid");
            data.listDmpnOrder=getDataGrid("#BTSGrid");
            data.listDT=getDataGrid("#CashFlow");
            data.listCVK=getDataGrid("#OtherJobs");
            data.listRevokeDT = getDataGrid("#CashFlow");
            data.listRentGround = getDataGrid("#rentGroundGrid");
		    return data;
		}

		function validateTotalMonthDetail(){
        var isValid = true;
        if(vm.detailMonthPlan.year==undefined||vm.detailMonthPlan.year==''){
            toastr.warning("Năm không được để trống!");
            return false;
        }
        if(vm.detailMonthPlan.month==undefined||vm.detailMonthPlan.month==''){
            toastr.warning("Tháng không được để trống!");
            return false;
        }
        if(vm.detailMonthPlan.code==undefined||vm.detailMonthPlan.code==''){
            toastr.warning("Mã kế hoạch không được để trống!");
            return false;
        }
        if(vm.detailMonthPlan.name==undefined||vm.detailMonthPlan.name==''){
            toastr.warning("Tên kế hoạch không được để trống!");
            return false;
        }
        if(vm.detailMonthPlan.sysGroupId==undefined||vm.detailMonthPlan.sysGroupId==''){
            toastr.warning("Đơn vị không được để trống!");
            return false;
        }
        return isValid;
    }
		
vm.cancelAdd= cancelAdd ;
function cancelAdd(){
    vm.disabled = false;
    vm.showDetail = false;
    vm.value=1;
    vm.prevStep();
    vm.detailMonthPlanTemp={}
    vm.detailMonthPlan={};
    vm.status=1;
    vm.OtherJobs.dataSource.data([]);
    vm.OtherJobs.refresh();
    vm.sourceGrid.dataSource.data([]);
    vm.sourceGrid.refresh();
    vm.forceMaintainGrid.dataSource.data([]);
    vm.forceMaintainGrid.refresh();
    vm.BTSGrid.dataSource.data([]);
    vm.BTSGrid.refresh();
    vm.targetGrid.dataSource.data([]);
    vm.targetGrid.refresh();
    vm.CashFlow.dataSource.data([]);
    vm.CashFlow.refresh();
    vm.doSearch();
    vm.String="Quản lý công trình > Quản lý kế hoạch > Kế hoạch tháng chi tiết";
}
		vm.remove=remove;
		function remove(dataItem){
            return Restangular.all("DetailMonthPlanOSRsService/checkPermissionsDelete").post(dataItem).then(function (d) {
                if(d.error){
                    toastr.error(d.error);
                    return;
                }
            }).catch(function (e) {
// var grid=vm.detailMonthPlanGrid;
// var item=grid.table.find("tr div." +id);
// var uid=$(item).parent().parent().attr("data-uid");
// var dataItem = grid.dataSource.getByUid(uid);
                confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function () {
                    detailMonthPlanOSService.remove(dataItem).then(
                        function (data) {
                        	//Huypq-20190627-start
                        	if(data!=undefined){
                        		if(data.error){
                                    toastr.error(data.error);
                                    return;
                                }
                        	}
                        	//Huy-end
                            toastr.success("Xóa bản ghi thành công!");
                            var sizePage = $("#detailMonthPlanGrid").data("kendoGrid").dataSource.total();
                            var pageSize = $("#detailMonthPlanGrid").data("kendoGrid").dataSource.pageSize();
                            if (sizePage % pageSize === 1) {
                                var currentPage = $("#detailMonthPlanGrid").data("kendoGrid").dataSource.page();
                                if (currentPage > 1) {
                                    $("#detailMonthPlanGrid").data("kendoGrid").dataSource.page(currentPage - 1);
                                }
                            }
                            $("#detailMonthPlanGrid").data('kendoGrid').dataSource.read();
                            $("#detailMonthPlanGrid").data('kendoGrid').refresh();

                        }, function (errResponse) {
                            toastr.error("Lỗi không xóa được!");
                        });
                })
            })
	}
		
		
		
		vm.canceldoSearch= function (){
			 vm.showDetail = false;
			vm.detailMonthPlanSearch={
					status:"1",
			};
			doSearch();
		}
		
		vm.doSearch= doSearch;
		function doSearch(){
			  vm.showDetail = false;
			var grid =vm.detailMonthPlanGrid;	
			if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 10
				});
			}

		}
		
		
		vm.showHideColumn=function(column){
        	if (angular.isUndefined(column.hidden)) {
        		vm.detailMonthPlanGrid.hideColumn(column);
            } else if (column.hidden) {
            	vm.detailMonthPlanGrid.showColumn(column);
            } else {
            	vm.detailMonthPlanGrid.hideColumn(column);
            }
        	
        	
        }
		/*
		 * * Filter các cột của select
		 */	
	
		vm.gridColumnShowHideFilter = function (item) { 
                return item.type==null||item.type !==1; 
            };
		
            
            vm.exportpdf= function(){
            	var obj={};
            	detailMonthPlanOSService.exportpdf(obj);
            }
			
			
			/*
			 * var specialChars = "<>@!#$%^&*()+[]{}?:;|'\"\\,./~`-="; var
			 * check = function(string){ for(var i = 0; i <
			 * specialChars.length;i++){ if(string.indexOf(specialChars[i]) >
			 * -1){ return true; } } vm.listApp.mess=""; return false; } var
			 * check1 = function(string){ for(var i = 0; i <
			 * specialChars.length;i++){ if(string.indexOf(specialChars[i]) >
			 * -1){ return true; } } vm.listApp.mess1=""; return false; } var
			 * check2 = function(string){ for(var i = 0; i <
			 * specialChars.length;i++){ if(string.indexOf(specialChars[i]) >
			 * -1){ return true; } } vm.listApp.mess2=""; return false; }
			 * 
			 * vm.checkErr = checkErr; function checkErr() { var parType =
			 * $('#parType').val(); var code = $('#code').val(); var
			 * name=$('#name').val(); if(check(parType) === true){
			 * vm.listApp.mess = "Loại tham số chứa ký tự đặc biệt"; }
			 * if(check1(code) === true){ vm.listApp.mess1 = "Mã tham số chứa ký
			 * tự đặc biệt"; } if(check2(name) === true){ vm.listApp.mess2 =
			 * "Tên Tham số ký tự đặc biệt"; } return vm.listApp; }
			 */
			vm.errNumber="";
            vm.checkNumber=checkNumber;
            function checkNumber(){
            	var val=$('#parOder').val();
				if(val===0){
					if(val===0){
						if(val===""){
							vm.errNumber="";
						}else{
							vm.errNumber="Phải nhập kiểu số nguyên từ 1-99";
						return false;
						}
						
					}
				}else{
					var isNaN = function(val) {
    	    			if(Number.isNaN(Number(val))){
							return false;
    	    			}
						return true;
					}
					if(isNaN(val)===false){
						vm.errNumber="Phải nhập kiểu số nguyên từ 1-99";
					}else{
						vm.errNumber="";
					}
					
				}
				
				
            }
            vm.userTemplate='<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;float:left">#: data.employeeCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
            vm.userHeaderTemplate='<div class="dropdown-header row text-center k-widget k-header">' +
            '<p class="col-md-6 text-header-auto border-right-ccc">Mã nhân viên</p>' +
            '<p class="col-md-6 text-header-auto">Tên nhân viên</p>' +
            '</div>';

            vm.suppervisorTemplate='<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;float:left">#: data.employeeCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
                vm.suppervisorHeaderTemplate='<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-6 text-header-auto border-right-ccc">Mã nhân viên</p>' +
                '<p class="col-md-6 text-header-auto">Tên nhân viên</p>' +
                '</div>';

            vm.directorTemplate='<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;float:left">#: data.employeeCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
                vm.directorHeaderTemplate='<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-6 text-header-auto border-right-ccc">Mã nhân viên</p>' +
                '<p class="col-md-6 text-header-auto">Tên nhân viên</p>' +
                '</div>';

        vm.gridCreator = [  {
            title: "Tên đăng nhập",
            field: "loginName",
            width: 120
        },{
            title: "Mã nhân viên",
            field: "employeeCode",
            width: 120
        }, {
            title: "Họ tên",
            field: "fullName",
            width: 120
        }, {
            title: "Email",
            field: "email",
            width: 120
        }, {
            title: "SĐT",
            field: "phoneNumber",
            width: 120
        }];
        vm.commonSearch = {name: '', code: ''};
        vm.onSelectFile= onSelectFile;
        function onSelectFile() {
            if($("#fileImportDetailMonth")[0].files[0].name.split('.').pop() !='xls' && $("#fileImportDetailMonth")[0].files[0].name.split('.').pop() !='xlsx'){
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
            else{
                if(104857600<$("#fileImportDetailMonth")[0].files[0].size){
                    toastr.warning("Dung lượng file vượt quá 100MB! ");
                    return;
                }
                $("#fileName")[0].innerHTML=$("#fileImportDetailMonth")[0].files[0].name
            }
        }
        vm.sendToSign = sendToSign;
        function sendToSign(dataItem) {
            return Restangular.all("DetailMonthPlanOSRsService/checkPermissionsRegistry").post().then(function (d) {
                if(d.error){
                    toastr.error(d.error);
                    return;
                }
            }).catch(function (e) {
                if (dataItem.signState == 1 || dataItem.signState == 4) {
                    var teamplateUrl = "coms/popup/SignVofficePopup.html";
                    var title = "Trình ký kế hoạch tháng chi tiết";
                    var windowId = "ORDER";
                    vm.nameToSign = "Yêu cầu trình ký";
                    var obj = {};
                    obj = angular.copy(dataItem);
                    obj.type = "12";
                    obj.reportName = "KeHoachThangChiTiet";
                    obj.objectId = dataItem.detailMonthPlanId;
                    obj.objectCode = dataItem.code
                    var dataList = [];
                    obj.listSignVoffice = [];
                    Restangular.all("yearPlanRsService/getAppParamByType").post('MONTH_DETAIL_PLAN_VOFFICE').then(function (data) {
                        var i = 1;
                        angular.forEach(data, function (item) {
                            var dt = {};
                            dt.oderName = i + ". " + item.name;
                            dt.signLabelName = item.name;
                            dt.signOrder = item.code
                            obj.listSignVoffice.push(dt);
                            i++
                        })
                        dataList.push(obj);
                        CommonService.populatePopupVofice(teamplateUrl, title, '11', dataList, vm, windowId, false, '85%', '85%');
                    }, function (error) {
                        toastr.error("Lỗi khi lấy dữ liệu người ký!")
                    });
                } else {
                    toastr.error("Kế hoạch không đúng trạng thái ký!")
                }
            })
        }
        vm.viewSignedDoc = viewSignedDoc;
        function viewSignedDoc(dataItem){
            var obj={
                objectId:dataItem.detailMonthPlanId,
                type:"12"
            }
            CommonService.viewSignedDoc(obj);
        }
		}
})();
