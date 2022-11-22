(function () {
    'use strict';
    var controllerId = 'managementAcceptanceController';

    angular.module('MetronicApp').controller(controllerId, managementAcceptanceController);

    function managementAcceptanceController($scope, $rootScope, $timeout, gettextCatalog, $filter,managementAcceptanceService,
        kendoConfig, $kWindow, htmlCommonService, CommonService, PopupConst, Restangular, RestEndpoint, Constant) {
//    	initCommonFunction($scope, $rootScope, $filter);
    	var vm = this;
    	vm.acceptanceSearch = {};
    	vm.clearKeySearch = clearKeySearch;
    	vm.clearSysGroupName = clearSysGroupName;
    	vm.clearProvince = clearProvince;
    	vm.cancelListYear = cancelListYear;
    	vm.cancelStatus = cancelStatus;
//    	vm.showTabVT=1;
    	vm.showSave = false;
    	vm.disableWorkItem = false;
    	vm.acceptanceObj = {};
    	vm.slXuat = null;
    	vm.doSearch = doSearch;
    	var modalInstance1 = null;
    	var modalInstance2 = null;
    	var modalInstance3 = null;
    	var modalInstance4 = null;
    	var date = new Date();
    	date.setDate(date.getDate() - 30);
    	var dateString = date.toISOString().split('T')[0];
    	var dateSplit = dateString.split("-");
    	var dateSub = dateSplit[2] +'/'+ dateSplit[1] +'/'+ dateSplit[0];
//    	var daySub = kendo.parseDate(dateSub,"dd/MM/yyyy");
    	initFormData();
        function initFormData() {
        	
        	fillDataAcceptance([]);
        	vm.acceptanceObj = {};
        	vm.acceptanceSearch.dateFrom = dateSub;
        	var obj= {};
        	managementAcceptanceService.getSysGroupCheck(obj).then(function(d) {
            	if(d.length<2){
            		for(var i=0;i<d.length;i++){
            			vm.acceptanceSearch.sysGroupName= d[i].text;
            		}
            	}
              }, function(errResponse){
                 toastr.error(gettextCatalog.getString("Xảy ra lỗi khi cập nhật trạng thái thu hồi!"));
                   return;
              });
        }
//        vm.showVTTB = function(value){
//            vm.showTabVT = value;
//        }
        acceptanceFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
                kendoConfig, $kWindow, managementAcceptanceService,
                CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm);
        var record = 0;
        function fillDataAcceptance(data){
        	vm.acceptanceGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable:false,
                save : function(){
                    vm.workItemGrid.refresh();
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: [
                    {
                        name: "actions",
                        template:
                        	'<div class="btn-group pull-right margin_top_button margin10">'+
                            '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'+
//                            '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>'+
                            '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
                            '<label ng-repeat="column in vm.constructionLandHandPlanGrid.columns| filter: vm.gridColumnShowHideFilter">'+
                            '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+
                            '</label>'+
                            '</div></div>'
                    }
                ],
                dataSource: {
//                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#appCountAcceptance").text("" + response.total);
                            vm.countAcceptance = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "constructionService/doSearchAcceptance",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.acceptanceSearch.page = options.page;
                            vm.acceptanceSearch.pageSize = options.pageSize;
                            vm.acceptanceSearch.approvedAcceptanceLst=vm.acceptanceSearch.approvedAcceptanceLst;
                            return JSON.stringify(vm.acceptanceSearch)

                        }
                    },
                    pageSize: 10,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                edit: function(e) {

                    if(e.model.statusConstruction == "6" || e.model.statusConstruction == "7" || e.model.statusConstruction == "8"){

                        e.sender.closeCell();

                    }

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
                edit: function(dataItem) {
                	dataItem.model.oldQuantity = dataItem.model.quantity;
                },
                columns: [
                    {
                        title: "TT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '50px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text'

                    },
                    {
                        title: "Đơn vị thực hiện",
                        field: 'text',
                        width: '280px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text'
                    },
                    {
                        title: "Mã trạm",
                        field: 'catStationCode',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text'
                    },
                    {
                        title: "Mã công trình",
                        field: 'constructionCode',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text'
                    },
                    {
                        title: "Mã hợp đồng đầu ra",
                        field: 'cntContractCode',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text'
                    },
                    {
                        title: "Ngày nghiệm thu đồng bộ",
                        field: 'acceptanceDate',
                        width: '120px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text'
                    },
                    {
                        title: "Trạng thái xác nhận",
                        field: 'approvedAcceptance',
                        width: '120px',
                        template: function (dataItem) {
                            if (dataItem.approvedAcceptance == 1) {
                                return "<span name='approvedAcceptance' font-weight: bold;'>Chưa xác nhận</span>"
                            } else if (dataItem.approvedAcceptance == 2) {
                                return "<span name='approvedAcceptance' font-weight: bold;'>Đã xác nhận</span>"
                            } else {
                            	return "<span name='approvedAcceptance' font-weight: bold;'></span>"
                            }
                        },
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text'
                    },
                    {
                        title: "Thao tác",
                        type :'text',
                        editable:false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        template: dataItem =>
                        '<div class="text-center">'
                        + '<button style=" border: none; background-color: white;" id="updateId" ng-click="vm.detailAcceptance(dataItem)" class=" icon_table "' +
                        '   uib-tooltip="Chi tiết" translate>' +
                        '<i class="fa fa-list-alt" style="color:#e0d014"  ng-hide="dataItem.status==0"   aria-hidden="true"></i>' +
                        '</button>'
                        + '</div>',
						width: '100px',
						field: "action",
                    },
    ]
    });
        }
        //Auto complete đơn vị
        vm.deprtOptions1 = {
        	    dataTextField: "text",
        	    dataValueField: "id",
        		placeholder:"Nhập mã hoặc tên đơn vị",
        	    select: function (e) {
//        	        vm.selectedDept1 = true;
        	        var dataItem = this.dataItem(e.item.index());
        	        vm.acceptanceSearch.sysGroupName = dataItem.text;
        	        vm.acceptanceSearch.sysGroupId = dataItem.id;
        	    },
        	    pageSize: 10,
        	    open: function (e) {
//        	        vm.selectedDept1 = false;
        	    },
        	    dataSource: {
        	        serverFiltering: true,
        	        transport: {
        	            read: function (options) {
        	                vm.selectedDept1 = false;
        	                return Restangular.all("departmentRsService/department/" + 'getForAutoCompleteDeptCheck').post({
        	                    name: vm.acceptanceSearch.sysGroupName,
        	                    pageSize: vm.deprtOptions1.pageSize
        	                }).then(function (response) {
        	                    options.success(response);
        	                }).catch(function (err) {
        	                    console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
        	                });
        	            }
        	        }
        	    },
        	    template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
        	    change: function (e) {
        	        if (e.sender.value() === '') {
        	            vm.acceptanceSearch.sysGroupName = null;// thành name
        	            vm.acceptanceSearch.sysGroupId = null;
        	        }
        	    },
        	    ignoreCase: false
        	}
        
        vm.openDepartmentTo = openDepartmentTo

        function openDepartmentTo(popUp) {
            vm.obj = {};
            vm.departmentpopUp = popUp;
            var templateUrl = 'coms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupGroup(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
        }
        
        vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'dept') {
                vm.acceptanceSearch.sysGroupName = data.text;
                vm.acceptanceSearch.sysGroupId = data.id;
            }
        }
        
        vm.provinceOptions = {
                dataTextField: "name",
                dataValueField: "id",
        		placeholder:"Nhập mã hoặc tên tỉnh",
                select: function (e) {
//                    vm.isSelect = true;
                    var dataItem = this.dataItem(e.item.index());
                    vm.acceptanceSearch.catProvinceId = dataItem.catProvinceId;
                    vm.acceptanceSearch.catProvinceCode = dataItem.code;
        			vm.acceptanceSearch.catProvinceName = dataItem.name;
                },
                pageSize: 10,
                open: function (e) {
//                    vm.isSelect = false;
                },
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function (options) {
                            vm.isSelect = false;
                            return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
                                name: vm.acceptanceSearch.catProvinceName,
                                pageSize: vm.provinceOptions.pageSize,
                                page: 1
                            }).then(function (response) {
                                options.success(response.data);
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
                template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
                change: function (e) {
                	if (e.sender.value() === '') {
                		vm.acceptanceSearch.catProvinceId = null;
                        vm.acceptanceSearch.catProvinceCode = null;
        				vm.acceptanceSearch.catProvinceName = null;
        	        }
                }
        }
        
        vm.openCatProvincePopup = openCatProvincePopup;
    	vm.onSaveCatProvince = onSaveCatProvince;
        function openCatProvincePopup(){
    		var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm tỉnh");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
        }
        
        function onSaveCatProvince(data){
            vm.acceptanceSearch.catProvinceId = data.catProvinceId;
            vm.acceptanceSearch.catProvinceCode = data.code;
    		vm.acceptanceSearch.catProvinceName = data.name;
            htmlCommonService.dismissPopup();
            $("#provincename").focus();
        };
        //popup Chi tiết vật tư
        vm.detailAcceptance=function(dataItem){
        	vm.slXuat = null;
        	vm.listCheck = [];
        	vm.listCheckB = [];
        	if(dataItem.approvedAcceptance=='2'){
        		vm.showSave = true;
        		vm.acceptanceObj.acceptanceStatus = "Đã xác nhận";
        	} else if(dataItem.approvedAcceptance=='1'){
        		vm.showSave = false;
        		vm.acceptanceObj.acceptanceStatus = "Chưa xác nhận";
        	}
        	vm.acceptanceObj.constructionCode = dataItem.constructionCode;
        	vm.acceptanceObj.constructionId = dataItem.constructionId;
        	vm.acceptanceObj.contractCode = dataItem.cntContractCode;
        	vm.acceptanceObj.listDataMerByGoodsId = dataItem.listDataMerByGoodsId;
//        	dataItem.selectedThuHoi = false;
//        	dataItem.checkAcceptanceTrue = false;
        	vm.initDataNT();
        	dataItem.selected = false;
            var templateUrl="coms/managementAcceptance/detailAcceptanceForm.html";
            var title="Thông tin chi tiết nghiệm thu";
            var windowId="DETAIL_ACCEPTANCE";
            CommonService.populatePopupCreate(templateUrl,title,vm.acceptanceSearch,vm,windowId,false,'85%','85%',"null");
            setTimeout(function(){
            	modalInstance1 = CommonService.getPopupClose();
            	$("#DSTBA").data("kendoGrid").dataSource.data(vm.listDSTBA);
            	getAcceptance(dataItem);
            	getSerial(dataItem);
            },1400);
        }
        function getAcceptance(data){
        	var gridCheck = $("#DSTBA").data("kendoGrid").dataSource.data();
//        	var grid = vm.DSVTAGrid.dataSource.data();
        	for(var z=0;z<gridCheck.length;z++){
        		gridCheck[z].constructionId = data.constructionId;
        		gridCheck[z].constructionCode = data.constructionCode;
        	}
        	
            managementAcceptanceService.getSynStockTransBySerial(vm.acceptanceObj).then(function(d) {
            	for(var i=0;i<d.length;i++){
        			for(var j=0;j<gridCheck.length;j++){
        				if(d[i].constructionCode==gridCheck[j].constructionCode && d[i].serial==gridCheck[j].serial && d[i].goodsId==gridCheck[i].goodsId){
        					gridCheck[j].selectedThuHoi=true;
        				}
//        				else {
//        					gridCheck[j].selectedThuHoi=false;
//        				}
        			}
        		}
              }, function(errResponse){
                 toastr.error(gettextCatalog.getString("Xảy ra lỗi khi cập nhật trạng thái thu hồi!"));
                   return;
              });
            
            managementAcceptanceService.getConstructionAcceptanceByConsId(vm.acceptanceObj).then(function(d) {
				for(var i=0;i<d.length;i++){
					for(var j=0;j<gridCheck.length;j++){
						if(d[i].constructionId==gridCheck[j].constructionId && d[i].serialMerchan==gridCheck[j].serial){
							gridCheck[j].selected=true;
//							gridCheck[j].checkAcceptanceTrue = true;
//							gridCheck[j].listDataWorkItem=d[i].listDataWorkItem;
						}
					}
				}
        	}, function(errResponse){
            	toastr.error(gettextCatalog.getString("Xảy ra lỗi khi cập nhật trạng thái nghiệm thu!"));
            	return;
        	});
        }
        
        function getSerial(data){
        	var gridCheckB = $("#DSTBB").data("kendoGrid").dataSource.data();
        	for(var m=0;m<gridCheckB.length;m++){
        		gridCheckB[m].constructionId = data.constructionId;
        		gridCheckB[m].constructionCode = data.constructionCode;
        	}
            managementAcceptanceService.getConstructionAcceptanceByConsIdTBB(vm.acceptanceObj).then(function(d) {
				for(var i=0;i<d.length;i++){
					for(var j=0;j<gridCheckB.length;j++){
						if(d[i].constructionId==gridCheckB[j].constructionId && d[i].serialMerchan==gridCheckB[j].serial){
							gridCheckB[j].selected=true;
							gridCheckB[j].listDataWorkItemTBB=d[i].listDataWorkItemTBB;
						}
					}
				}
        	}, function(errResponse){
//            	toastr.error(gettextCatalog.getString("Xảy ra lỗi khi cập nhật nghiệm thu!"));
            	return;
        	});
        }
        
        vm.returnAcceptance = returnAcceptance;
        function returnAcceptance(){
//        	$("span.k-icon.k-i-close").click();
        	modalInstance1.dismiss();
    	}
        
        vm.doSearch = doSearch;
        function doSearch() {
          var grid = vm.acceptanceGrid;
          if (grid) {
              grid.dataSource.query({
                  page: 1,
                  pageSize: 10
              });
              grid.dataSource.read();
          }
      }
        
        vm.saveMerchandise = function(){
        	var grid = $("#DSTBA").data("kendoGrid").dataSource.data();
        	var gridB = $("#DSTBB").data("kendoGrid").dataSource.data();
//        	var gridMerchan = $("#DSVTA").data("kendoGrid").dataSource.data();
        	var obj ={};
        	for(var k=0;k<grid.length;k++){
        		if(grid[k].selected==true && vm.listCheck.length==0 && grid[k].listDataWorkItem==null){
        			grid[k].listDataWorkItem=grid[k].listDataMerByGoodsId;
        			vm.listCheck.push(grid[k]);
        		} else if(grid[k].selected==true && vm.listCheck.length==0 && grid[k].listDataWorkItem!=null){
        			vm.listCheck.push(grid[k]);
        		}
        	}
        	for(var h=0;h<gridB.length;h++){
        		if(gridB[h].selected==true && vm.listCheckB.length==0){
        			vm.listCheckB.push(gridB[h]);
        		}
        	}
        	confirm('Xác nhận cập nhật dữ liệu chi tiết nghiệm thu ?', function () {
        		managementAcceptanceService.deleteConstructionMerchanse(vm.acceptanceObj).then(function(result) {
						if(result.error){
	    				toastr.error(result.error);
	    				return;
	    			}
	    		}, function(errResponse){
	                	toastr.error(gettextCatalog.getString("Xảy ra lỗi khi cập nhật nghiệm thu!"));
	                	return;
	            });
        		for(var g=0;g<vm.listSave.length;g++){
        			managementAcceptanceService.updateWorkItemMerchan(vm.listSave[g]).then(function (d) {
        				
                	}, function (errResponse) {
                        toastr.error("Có lỗi khi cập nhật!");
                	});
        		}
        		
        		for(var v=0;v<vm.listSaveB.length;v++){
        			managementAcceptanceService.updateWorkItemMerchanB(vm.listSaveB[v]).then(function (d) {
        				
                	}, function (errResponse) {
                        toastr.error("Có lỗi khi cập nhật!");
                	});
        		}
        		obj.listDataMerchandise = vm.listCheck;
        		obj.listDataMerchandiseTBB = vm.listCheckB;
        		obj.constructionId = vm.acceptanceObj.constructionId;
        		obj.goodsId = vm.acceptanceObj.goodsId;
        		managementAcceptanceService.updateConstructionAcceptance(obj).then(function (d) {
        			toastr.success("Cập nhật thông tin nghiệm thu thành công !");
//        			CommonService.dismissPopup();
        			doSearch();
        			modalInstance1.dismiss();
            	}, function (errResponse) {
                    toastr.error("Có lỗi khi cập nhật!");
                    return;
            	});
        });
        }
        
        function clearKeySearch(){
    		vm.acceptanceSearch.keySearch = null;
    	}
    	
    	function clearSysGroupName(){
    		vm.acceptanceSearch.sysGroupName = null;
    		vm.acceptanceSearch.sysGroupId= null;
    	}
    	
    	function clearProvince(){
    		vm.acceptanceSearch.catProvinceName = null;
    		vm.acceptanceSearch.catProvinceId = null;
    		vm.acceptanceSearch.catProvinceCode = null;
    	}
    	
    	function cancelListYear(){
    		vm.acceptanceSearch.dateFrom = null;
    		vm.acceptanceSearch.dateTo = null;
    	}
    	
    	function cancelStatus(){
    		vm.acceptanceSearch.approvedAcceptanceLst = null;
    	}
        //end controller
 }
})();
