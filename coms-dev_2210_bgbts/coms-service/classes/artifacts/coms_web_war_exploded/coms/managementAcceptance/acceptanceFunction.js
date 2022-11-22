/**
 * Created by pm1_os42 on 2/24/2018.
 */
function acceptanceFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
    kendoConfig, $kWindow, managementAcceptanceService,
    CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm){


    //Ben A
    vm.listDSVTA=[];
    vm.listDSTBA=[];
    //Ben B
    vm.listDSVTB=[];
    vm.listDSTBB=[];
    vm.listDSTP =[];
    vm.listStatus=[];
    vm.TBObj={};
    vm.showTabVT=1;
    vm.certObj={};
    vm.dataStatus={};
    vm.checkStatus = true;
    vm.showVTTB = function(value){
        vm.showTabVT = value;
    }
    vm.listDataMerchan=[];
    vm.slXuat = null;
    vm.slXuatB = null;
    vm.slThuHoi = null;
    vm.update = false;
    vm.returnMerchan = returnMerchan;
    vm.returnMerchanB = returnMerchanB;
    init();
    function init(){
    	fillDataAddition([]);
    	fillDataAdditionB([]);
//    	fillDataDSVTA();
//    	fillDataDSTBA();
//    	fillDataTBB([]);
    }
    
    vm.initDataNT = function(){
        Restangular.all("constructionService"+"/construction/listDataDSVTA").post(vm.acceptanceObj).then(function(data){
        	populateData(data);
            showVTTBA();
            showVTTBB();
        },function(err){
            
        });
    }

    function populateData(data){
        vm.listDSVTA = data.listDataVTA;
        vm.listDSTBA = data.listDataTBA;
        vm.listDSVTB = data.listDataVTB;
        vm.listDSTBB = data.listDataTBB;
    }

    function showVTTBA(){
        vm.DSVTAGrid.dataSource.data(vm.listDSVTA);
        vm.DSVTAGrid.refresh();
        vm.DSTBAGrid.dataSource.data(vm.listDSTBA);
        vm.DSTBAGrid.refresh();
    }

    function showVTTBB(){
        vm.DSVTBGrid.dataSource.data(vm.listDSVTB);
        vm.DSVTBGrid.refresh();
        vm.DSTBBGrid.dataSource.data(vm.listDSTBB);
        vm.DSTBBGrid.refresh();
    }
    
    function validateCert(){
        if(vm.certObj.importer==null ||vm.certObj.importer==''){
            toastr.warning("Chưa nhập tên người nhập thông tin!")
            return false;
        }
        if(vm.certObj.startingDate==null ||vm.certObj.startingDate==''){
            toastr.warning("Chưa nhập từ ngày !  ")
            return false;
        }
        return true
    }

    function numberWithCommas(x) {
        if(x == null || x == undefined){
            return '0';
        }
        var parts = x.toFixed(2).toString().split(".");
        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        return parts.join(".");
    }
//    function fillDataDSVTA(){
    vm.DSVTAGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
//                save : function(){
//                    vm.DSVTAGrid.refresh();
//                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                pageable: {
                    refresh: false,
                    pageSize: 5,
                    pageSizes: [5,10, 15, 20, 25],
                    messages: {
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
                        width: '32px',
                        editable: false,
                        type:'text',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }

                    },
                    {
                        title: "Mã VTTB",
                        field: 'maVttb',
                        width: '100px',
                        type:'text',
                        editable: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }

                    },
                    {
                        title: "Tên VTTB",
                        field: 'tenVttb',
                        type:'text',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        editable: false
                    },
                    {
                        title: "Đơn vị tính",
                        field: 'donViTinh',
                        width: '50px',
                        type:'text',
                        editable: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "SL xuất",
                        field: 'slXuat',
                        width: '50px',
                        type: 'number',
                        editable: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function(data){
                            return numberWithCommas(data.slXuat);
                        }
                    }, {
                        title: "SL nghiệm thu",
                        field: 'slNghiemThu',
                        width: '50px',
                        type: 'number',
                        editable: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function(data){
                            return numberWithCommas(data.slNghiemThu);
                        }
                    },
                    {
                        title: "SL đã thu hồi",
                        field: 'slThuHoi',
                        width: '50px',
                        type: 'number',
                        editable: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {	
                            style: "text-align:right;"
                        },
                        template: function(data){
                            return numberWithCommas(data.slThuHoi);
                        }
//                        template: function(data){
//                            if((data.quantity>data.numberXuat-data.numberThuhoi)||data.quantity==null){
//                                data.quantity= data.numberXuat - data.numberThuhoi;
//                                 return numberWithCommas(data.quantity);
//                            }
//                            if(data.quantity<0){
//                                data.quantity=0;
//                                return numberWithCommas(data.quantity);
//                            }
//                            else
//                                return numberWithCommas(data.quantity);
//                        }
                    },
                    {
                        title: "SL còn lại",
                        field: 'slConLai',
                        width: '50px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'number',
                        editable: false,
                        template: function(data){
                           return numberWithCommas(data.slConLai);
                        }
                    },
                    {
                        title: "Thao tác",
                        type :'text',
                        editable:false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        template: dataItem =>
                        '<div class="text-center">'
                        + '<button style=" border: none; background-color: white;" id="updateId" ng-click="caller.detailMerchandise(dataItem)" class=" icon_table "' +
                        '   uib-tooltip="Chi tiết" translate>' +
                        '<i class="fa fa-list-alt" style="color:#e0d014"  ng-hide="dataItem.status==0"   aria-hidden="true"></i>' +
                        '</button>'
                        + '</div>',
						width: '50px',
						field: "action",
                    }
                ]
            });
    vm.slXuat = null;
    vm.slThuHoi = null;
    vm.slNghiemThu=null;
    vm.detailMerchandise = function(dataItem){
    	vm.slXuat = dataItem.slXuat;
    	vm.slThuHoi = dataItem.slThuHoi;
    	vm.slNghiemThu = dataItem.slNghiemThu;
    	vm.acceptanceObj.maVttb=dataItem.maVttb;
    	vm.acceptanceObj.tenVttb=dataItem.tenVttb;
    	vm.acceptanceObj.donViTinh=dataItem.donViTinh;
    	vm.acceptanceObj.countAcceptance=dataItem.tongSlNghiemThu;
    	vm.acceptanceObj.goodsId=dataItem.goodsId;
    	vm.listOptionsWorkItem = [];
    	var templateUrl="coms/managementAcceptance/detailMerchandiseForm.html";
        var title="Thông tin chi tiết vật tư nghiệm thu";
        var windowId="DETAIL_MERCHANDISE";
        CommonService.populatePopupCreate(templateUrl,title,vm.acceptanceObj,vm,windowId,false,'80%','80%',"null");
        setTimeout(function(){
        	modalInstance2 = CommonService.getPopupClose();
        },10);
    }
    vm.slXuatB=null;
    vm.slThuHoiB = null;
    vm.slNghiemThuB = null;
    var modalInstance5 = null;
    vm.detailMerchandiseB = function(dataItem){
    	vm.slXuatB = dataItem.slXuat;
    	vm.slThuHoiB = dataItem.slThuHoi;
    	vm.slNghiemThuB = dataItem.slNghiemThu;
    	vm.acceptanceObj.maVttb=dataItem.maVttb;
    	vm.acceptanceObj.tenVttb=dataItem.tenVttb;
    	vm.acceptanceObj.donViTinh=dataItem.donViTinh;
    	vm.acceptanceObj.countAcceptance=dataItem.tongSlNghiemThu;
    	vm.acceptanceObj.goodsId=dataItem.goodsId;
    	vm.listOptionsWorkItemB = [];
    	var templateUrl="coms/managementAcceptance/detailMerchandiseFormB.html";
        var title="Thông tin chi tiết vật tư nghiệm thu";
        var windowId="DETAIL_MERCHANDISE_B";
        CommonService.populatePopupCreate(templateUrl,title,vm.acceptanceObj,vm,windowId,false,'80%','80%',"null");
        setTimeout(function(){
        	modalInstance5 = CommonService.getPopupClose();
        },10);
    }
    
    vm.additionMerchandise = function(dataItem){
    	managementAcceptanceService.getWorkItemByMerchandise(vm.acceptanceObj).then(function (d) {
    			vm.acceptanceObj.listWorkItem = d.plain().data;
    			var templateUrl="coms/managementAcceptance/findMerchandise.html";
    	        var title="Bổ sung hạng mục công trình";
    	        var windowId="ADD_MERCHANDISE";
    	        CommonService.populatePopupCreate(templateUrl,title,vm.acceptanceObj,vm,windowId,false,'80%','80%',"null");
    	        setTimeout(function(){
                	modalInstance4 = CommonService.getPopupClose();
                },10);
        	}, function (errResponse) {
                toastr.success("")
        	});
    }
    var modalInstance6 = null;
    vm.additionMerchandiseB = function(dataItem){
    	managementAcceptanceService.getWorkItemByMerchandiseB(vm.acceptanceObj).then(function (d) {
    			vm.acceptanceObj.listWorkItemB = d.plain().data;
    			var templateUrl="coms/managementAcceptance/findMerchandiseB.html";
    	        var title="Bổ sung hạng mục công trình";
    	        var windowId="ADD_MERCHANDISE_B";
    	        CommonService.populatePopupCreate(templateUrl,title,vm.acceptanceObj,vm,windowId,false,'80%','80%',"null");
    	        setTimeout(function(){
                	modalInstance6 = CommonService.getPopupClose();
                },10);
        	}, function (errResponse) {
                toastr.success("")
        	});
    }
    
    vm.gridMerchandiseOptions = kendoConfig.getGridOptions({
    	autoBind: true,
        scrollable: true,
        resizable: true,
        filterable: true,
        editable:false,
        dataBinding: function () {
            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
        },
        dataSource: {
//            serverPaging: true,
            schema: {
                total: function (response) {
                    return response.total;
                },
                data: function (response) {
                    return response.data;
                }
            },
            transport: {
                read: {
                    url: Constant.BASE_SERVICE_URL + "constructionService/getDataNotIn",
                    contentType: "application/json; charset=utf-8",
                    type: "POST"
                },
                parameterMap: function (options, type) {
                	vm.acceptanceObj.page = options.page
                    vm.acceptanceObj.pageSize = options.pageSize
                    vm.acceptanceObj.listWorkItem =vm.acceptanceObj.listWorkItem
                    vm.acceptanceObj.keySearch = vm.acceptanceObj.keySearch
                    return JSON.stringify(vm.acceptanceObj)

                }
            },
            pageSize: 10,
        },
        pageable: {
            refresh: false,
            pageSize: 5,
            pageSizes: [5,10, 15, 20, 25],
            messages: {
                display: "{0}-{1} của {2} kết quả",
                itemsPerPage: "kết quả/trang",
                empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
            }
        },
//             dataBound: onDataBound,
        columns: [
            {
                title: "TT",
                field: "stt",
                editable: false,
                template: function () {
                    return ++record;
                },
                width: '30px',
                columnMenu: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:center;"
                }
            },
            {
                title: "Mã công trình",
                field: 'constructionCode',
                width: '200px',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }
            },
            {
                title: "Tên hạng mục",
                field: 'workItemName',
                editable: true,
                width: '200px',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
            },
            {
				title: "Chọn",
				template:
					'<div class="text-center "> ' +
					'		<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate>' +
					'			<i id="#=code#" ng-click=caller.saveConstruction(dataItem) class="fa fa-check color-green" aria-hidden="true"></i> ' +
					'		</a>'
					+ '</div>',
				width: "50px",
				field: "stt"
			}
        ]
    });
    
    vm.gridWorkItemOptions = kendoConfig.getGridOptions({
    	autoBind: true,
        scrollable: true,
        resizable: true,
        filterable: true,
        editable:false,
        toolbar: [{
			name: "actions",
			template: '<div class=" pull-left ">' +
				'<button class="btn btn-qlk fa fa-check color-green ng-scope" style="line-height: 1 !important;"' +
				'ng-click="caller.saveWorkItemCheck(dataItem)" uib-tooltip="Chọn" translate><p class="action-button add" aria-hidden="true">Chọn</p></button>' +
				'</div>'
		}
		],
        dataBinding: function () {
            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
        },
        dataSource: {
//            serverPaging: true,
            schema: {
                total: function (response) {
                    return response.total;
                },
                data: function (response) {
                	var listWorkItem = response.data;
                	for(var i=0;i<listWorkItem.length;i++){
                		listWorkItem[i].synStockTransDetailId=vm.synStockTransDetailId;
                	}
                    return response.data;
                }
            },
            transport: {
                read: {
                    url: Constant.BASE_SERVICE_URL + "constructionService/getWorkItemByConsId",
                    contentType: "application/json; charset=utf-8",
                    type: "POST"
                },
                parameterMap: function (options, type) {
                	vm.acceptanceObj.page = options.page
                    vm.acceptanceObj.pageSize = options.pageSize
                    vm.acceptanceObj.keySearch = vm.acceptanceObj.keySearch
                    return JSON.stringify(vm.acceptanceObj)

                }
            },
            pageSize: 10,
        },
        pageable: {
            refresh: false,
            pageSize: 5,
            pageSizes: [5,10, 15, 20, 25],
            messages: {
                display: "{0}-{1} của {2} kết quả",
                itemsPerPage: "kết quả/trang",
                empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
            }
        },
//             dataBound: onDataBound,
        columns: [
        	{

                title : "Chọn",
                template: function(data){
                    return "<input type='checkbox' data-order = '"+record+"' id='childcheck' name='gridcheckbox' ng-click='caller.handleCheckWorkItem(dataItem)' ng-model='dataItem.selected'/>";
                },
                width: '20px',
                headerAttributes: {style:"text-align:center;"},
                attributes:{style:"text-align:center;"}
            },
            {
                title: "TT",
                field: "stt",
                editable: false,
                template: function () {
                    return ++record;
                },
                width: '30px',
                columnMenu: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:center;"
                }
            },
            {
                title: "Mã hạng mục",
                field: 'workItemCode',
                width: '200px',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }
            },
            {
                title: "Tên hạng mục",
                field: 'workItemName',
                editable: true,
                width: '200px',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
            },
            {
				title: "Chọn",
				template:
					'<div class="text-center "> ' +
					'		<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate>' +
					'			<i id="#=code#" ng-click=caller.saveWorkItemCheck(dataItem) class="fa fa-check color-green" aria-hidden="true"></i> ' +
					'		</a>'
					+ '</div>',
				width: "50px",
				field: "stt"
			}
        ]
    });
    
    vm.gridWorkItemTBBOptions = kendoConfig.getGridOptions({
    	autoBind: true,
        scrollable: true,
        resizable: true,
        filterable: true,
        editable:false,
        toolbar: [{
			name: "actions",
			template: '<div class=" pull-left ">' +
				'<button class="btn btn-qlk fa fa-check color-green ng-scope" style="line-height: 1 !important;"' +
				'ng-click="caller.saveWorkItemCheckTBB(dataItem)" uib-tooltip="Chọn" translate><p class="action-button add" aria-hidden="true">Chọn</p></button>' +
				'</div>'
		}
		],
        dataBinding: function () {
            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
        },
        dataSource: {
//            serverPaging: true,
            schema: {
                total: function (response) {
                    return response.total;
                },
                data: function (response) {
                	var listWorkItem = response.data;
                	for(var i=0;i<listWorkItem.length;i++){
                		listWorkItem[i].synStockTransDetailId=vm.synStockTransDetailId;
                	}
                    return response.data;
                }
            },
            transport: {
                read: {
                    url: Constant.BASE_SERVICE_URL + "constructionService/getWorkItemByConsId",
                    contentType: "application/json; charset=utf-8",
                    type: "POST"
                },
                parameterMap: function (options, type) {
                	vm.acceptanceObj.page = options.page
                    vm.acceptanceObj.pageSize = options.pageSize
                    vm.acceptanceObj.keySearch = vm.acceptanceObj.keySearch
                    return JSON.stringify(vm.acceptanceObj)

                }
            },
            pageSize: 10,
        },
        pageable: {
            refresh: false,
            pageSize: 5,
            pageSizes: [5,10, 15, 20, 25],
            messages: {
                display: "{0}-{1} của {2} kết quả",
                itemsPerPage: "kết quả/trang",
                empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
            }
        },
//             dataBound: onDataBound,
        columns: [
        	{

                title : "Chọn",
                template: function(data){
                    return "<input type='checkbox' data-order = '"+record+"' id='childcheck' name='gridcheckbox' ng-click='caller.handleCheckWorkItemTBB(dataItem)' ng-model='dataItem.selected'/>";
                },
                width: '5%',
                headerAttributes: {style:"text-align:center;"},
                attributes:{style:"text-align:center;"}
            },
            {
                title: "TT",
                field: "stt",
                editable: false,
                template: function () {
                    return ++record;
                },
                width: '30px',
                columnMenu: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:center;"
                }
            },
            {
                title: "Mã hạng mục",
                field: 'workItemCode',
                width: '200px',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }
            },
            {
                title: "Tên hạng mục",
                field: 'workItemName',
                editable: true,
                width: '200px',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
            },
            {
				title: "Chọn",
				template:
					'<div class="text-center "> ' +
					'		<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate>' +
					'			<i id="#=code#" ng-click=caller.saveWorkItemCheckTBB(dataItem) class="fa fa-check color-green" aria-hidden="true"></i> ' +
					'		</a>'
					+ '</div>',
				width: "50px",
				field: "stt"
			}
        ]
    });
    
    vm.listDataWorkItem = [];
    vm.handleCheckWorkItem= function(dataItem){
    	if(dataItem.selected){
    		vm.listDataWorkItem.push(dataItem);
    	}
    	else{
    		for (var i = 0; i < vm.listDataWorkItem.length; i++) {
                if (vm.listDataWorkItem[i].workItemId === dataItem.workItemId) {
                	vm.listDataWorkItem.splice(i, 1);
                }
            }
    	}
    }
    
    vm.listDataWorkItemTBB = [];
    vm.handleCheckWorkItemTBB= function(dataItem){
    	if(dataItem.selected){
    		vm.listDataWorkItemTBB.push(dataItem);
    	}
    	else{
    		for (var i = 0; i < vm.listDataWorkItemTBB.length; i++) {
                if (vm.listDataWorkItemTBB[i].workItemId === dataItem.workItemId) {
                	vm.listDataWorkItemTBB.splice(i, 1);
                }
            }
    	}
    }
    
    vm.dataItemCheck = [];
    vm.saveWorkItemCheck = function(dataItem){
    	vm.dataItemCheck = [];
    	var grid = $("#DSTBA").data("kendoGrid").dataSource.data();
    	if(dataItem!=undefined){
        	for(var i=0;i<grid.length;i++){
        		if(grid[i].synStockTransDetailId==dataItem.synStockTransDetailId){
        			vm.dataItemCheck.push(dataItem);
            		grid[i].listDataWorkItem = vm.dataItemCheck;
            	}
        	}
    	} else {
        	for(var i=0;i<grid.length;i++){
        		for(var j=0;j<vm.listDataWorkItem.length;j++){
        			if(grid[i].synStockTransDetailId==vm.listDataWorkItem[j].synStockTransDetailId){
            			grid[i].listDataWorkItem = vm.listDataWorkItem;
            		}
        		}
        	}
    	}
    	toastr.success("Đã gán vật tư cho hạng mục!");
//    	CommonService.dismissPopup();
    	vm.listDataWorkItem = [];
    	modalInstance3.dismiss();
    }
    
    vm.dataItemCheckTBB = [];
    vm.saveWorkItemCheckTBB = function(dataItem){
    	vm.dataItemCheckTBB = [];
    	if(dataItem!=undefined){
    		var grid = $("#DSTBB").data("kendoGrid").dataSource.data();
        	for(var i=0;i<grid.length;i++){
        		if(grid[i].synStockTransDetailId==dataItem.synStockTransDetailId){
        			vm.dataItemCheckTBB.push(dataItem);
            		grid[i].listDataWorkItemTBB = vm.dataItemCheckTBB;
            	}
        	}
    	} else {
    		var grid = $("#DSTBB").data("kendoGrid").dataSource.data();
        	for(var i=0;i<grid.length;i++){
        		for(var j=0;j<vm.listDataWorkItemTBB.length;j++){
        			if(grid[i].synStockTransDetailId==vm.listDataWorkItemTBB[j].synStockTransDetailId){
            			grid[i].listDataWorkItemTBB = vm.listDataWorkItemTBB;
            		}
        		}
        	}
    	}
    	toastr.success("Đã gán vật tư cho hạng mục!");
//    	CommonService.dismissPopup();
    	vm.listDataWorkItemTBB = [];
    	modalInstance7.dismiss();
    }
    
    vm.doSearchWorkItem = doSearchWorkItem;
    function doSearchWorkItem() {
      var grid = vm.gridWorkItemView;
      if (grid) {
          grid.dataSource.query({
              page: 1,
              pageSize: 10
          });
          grid.dataSource.read();
      }
    }
    
    vm.doSearchWorkItemTBB = doSearchWorkItemTBB;
    function doSearchWorkItemTBB() {
      var grid = vm.gridWorkItemTBBView;
      if (grid) {
          grid.dataSource.query({
              page: 1,
              pageSize: 10
          });
          grid.dataSource.read();
      }
    }
    
    vm.doSearchConstruction = doSearchConstruction;
    function doSearchConstruction() {
      var grid = vm.gridMerchandiseView;
      if (grid) {
          grid.dataSource.query({
              page: 1,
              pageSize: 10
          });
      }
    }
    
//    Thêm hạng mục VT B cấp
    vm.gridMerchandiseBOptions = kendoConfig.getGridOptions({
    	autoBind: true,
        scrollable: true,
        resizable: true,
        filterable: true,
        editable:false,
        dataBinding: function () {
            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
        },
        dataSource: {
//            serverPaging: true,
            schema: {
                total: function (response) {
                    return response.total;
                },
                data: function (response) {
                    return response.data;
                }
            },
            transport: {
                read: {
                    url: Constant.BASE_SERVICE_URL + "constructionService/getDataNotIn",
                    contentType: "application/json; charset=utf-8",
                    type: "POST"
                },
                parameterMap: function (options, type) {
                	vm.acceptanceObj.page = options.page
                    vm.acceptanceObj.pageSize = options.pageSize
                    vm.acceptanceObj.listWorkItem =vm.acceptanceObj.listWorkItemB
                    vm.acceptanceObj.keySearch = vm.acceptanceObj.keySearch
                    return JSON.stringify(vm.acceptanceObj)

                }
            },
            pageSize: 10,
        },
        pageable: {
            refresh: false,
            pageSize: 5,
            pageSizes: [5,10, 15, 20, 25],
            messages: {
                display: "{0}-{1} của {2} kết quả",
                itemsPerPage: "kết quả/trang",
                empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
            }
        },
//             dataBound: onDataBound,
        columns: [
            {
                title: "TT",
                field: "stt",
                editable: false,
                template: function () {
                    return ++record;
                },
                width: '30px',
                columnMenu: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:center;"
                }
            },
            {
                title: "Mã công trình",
                field: 'constructionCode',
                width: '200px',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }
            },
            {
                title: "Tên hạng mục",
                field: 'workItemName',
                editable: true,
                width: '200px',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
            },
            {
				title: "Chọn",
				template:
					'<div class="text-center "> ' +
					'		<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate>' +
					'			<i id="#=code#" ng-click=caller.saveConstructionB(dataItem) class="fa fa-check color-green" aria-hidden="true"></i> ' +
					'		</a>'
					+ '</div>',
				width: "50px",
				field: "stt"
			}
        ]
    });
    
    vm.doSearchConstructionB = doSearchConstructionB;
    function doSearchConstructionB() {
      var grid = vm.gridMerchandiseViewB;
      if (grid) {
          grid.dataSource.query({
              page: 1,
              pageSize: 10
          });
      }
    }
    
    //save add hạng mục B cấp
    function refreshDataAdditionB(data){
    	var grid = vm.merchandiseGridB;
    	if(grid){
    		grid.dataSource.data(data);
    		grid.refresh();
    	}
    }
    
    vm.listDataMerchanB = [];
    vm.saveConstructionB = function(dataItem){
    	vm.dataObj = {};
    	var grid = $("#merchandiseBId").data("kendoGrid");
    	vm.listDataMerchanB = grid.dataSource.data();
    	vm.dataObj.constructionId= dataItem.constructionId;
    	vm.dataObj.workItemId= dataItem.workItemId;
    	vm.dataObj.workItemName = dataItem.workItemName;
    	vm.listDataMerchanB.push(vm.dataObj);
    	modalInstance6.dismiss();
    	refreshDataAdditionB(vm.listDataMerchanB);
    }
    //
    function refreshDataAddition(data){
    	var grid = vm.merchandiseGrid;
    	if(grid){
    		grid.dataSource.data(data);
    		grid.refresh();
    	}
    }
    
    vm.saveConstruction = function(dataItem){
    	vm.update = true;
    	vm.dataObj = {};
    	var grid = $("#merchandiseId").data("kendoGrid");
    	vm.listDataMerchan = grid.dataSource.data();
    	vm.dataObj.constructionId= dataItem.constructionId;
    	vm.dataObj.workItemId= dataItem.workItemId;
    	vm.dataObj.workItemName = dataItem.workItemName;
    	vm.listDataMerchan.push(vm.dataObj);
    	modalInstance4.dismiss();
    	refreshDataAddition(vm.listDataMerchan);
    }
    //table thông tin chi tiết vật tư nghiệm thu
    
    function fillDataAddition(data){
	    vm.merchandiseGridOptions = kendoConfig.getGridOptions({
	    	autoBind: true,
	        scrollable: true,
	        resizable: true,
//	        filterable: true,
	        editable:false,
	        dataBinding: function () {
	            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
	        },
	        toolbar: [{
				name: "actions",
				template: '<div class=" pull-left ">' +
					'<button class="btn btn-qlk padding-search-right searchQLK"' +
					'ng-click="caller.additionMerchandise(dataItem)" uib-tooltip="Bổ sung" translate><p class="action-button add" aria-hidden="true">Bổ sung</p></button>' +
					'</div>'
			}
			],
	        dataSource: {
//	            serverPaging: true,
	            schema: {
	                total: function (response) {
	                    return response.total;
	                },
	                data: function (response) {
	                	vm.listDataMerchan = response.data;
	                    return response.data;
	                }
	            },
	            transport: {
	                read: {
	                    url: Constant.BASE_SERVICE_URL + "constructionService/getWorkItemByMerchandise",
	                    contentType: "application/json; charset=utf-8",
	                    type: "POST"
	                },
	                parameterMap: function (options, type) {
	                	vm.acceptanceObj.page = options.page
	                    vm.acceptanceObj.pageSize = options.pageSize
	                    return JSON.stringify(vm.acceptanceObj)
	
	                }
	            },
	            pageSize: 10,
	        },
	        pageable: {
	            refresh: false,
	            pageSize: 5,
	            pageSizes: [5,10, 15, 20, 25],
	            messages: {
	                display: "{0}-{1} của {2} kết quả",
	                itemsPerPage: "kết quả/trang",
	                empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
	            }
	        },
	//             dataBound: onDataBound,
	        columns: [
	            {
	                title: "TT",
	                field: "stt",
	                editable: false,
	                template: function () {
	                    return ++record;
	                },
	                width: '50px',
	                columnMenu: false,
	                headerAttributes: {style: "text-align:center;font-weight: bold;"},
	                attributes: {
	                    style: "text-align:center;"
	                }
	            },
	            {
	                title: "Hạng mục",
	                field: 'workItemName',
	                width: '200px',
	                headerAttributes: {style: "text-align:center;font-weight: bold;"},
	                attributes: {
	                    style: "text-align:left;"
	                }
	            },
	            {
	                title: "SL nghiệm thu",
	                field: 'merchandiseQuantity',
	                editable: true,
	                width: '100px',
	                headerAttributes: {style: "text-align:center;font-weight: bold;"},
	                attributes: {
	                    style: "text-align:left;"
	                },
	                template: "<input type='number' id='merchandiseQuantityId' class='form-control' ng-model='dataItem.merchandiseQuantity'/>",
	            },
	           
	        ]
	    });
    }
    
    //table chi tiết vật tư B cấp
    function fillDataAdditionB(data){
	    vm.merchandiseBGridOptions = kendoConfig.getGridOptions({
	    	autoBind: true,
	        scrollable: true,
	        resizable: true,
//	        filterable: true,
	        editable:false,
	        dataBinding: function () {
	            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
	        },
	        toolbar: [{
				name: "actions",
				template: '<div class=" pull-left ">' +
					'<button class="btn btn-qlk padding-search-right searchQLK"' +
					'ng-click="caller.additionMerchandiseB(dataItem)" uib-tooltip="Bổ sung" translate><p class="action-button add" aria-hidden="true">Bổ sung</p></button>' +
					'</div>'
			}
			],
	        dataSource: {
//	            serverPaging: true,
	            schema: {
	                total: function (response) {
	                    return response.total;
	                },
	                data: function (response) {
	                	vm.listDataMerchanB = response.data;
	                    return response.data;
	                }
	            },
	            transport: {
	                read: {
	                    url: Constant.BASE_SERVICE_URL + "constructionService/getWorkItemByMerchandiseB",
	                    contentType: "application/json; charset=utf-8",
	                    type: "POST"
	                },
	                parameterMap: function (options, type) {
	                	vm.acceptanceObj.page = options.page
	                    vm.acceptanceObj.pageSize = options.pageSize
	                    return JSON.stringify(vm.acceptanceObj)
	
	                }
	            },
	            pageSize: 10,
	        },
	        pageable: {
	            refresh: false,
	            pageSize: 5,
	            pageSizes: [5,10, 15, 20, 25],
	            messages: {
	                display: "{0}-{1} của {2} kết quả",
	                itemsPerPage: "kết quả/trang",
	                empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
	            }
	        },
	//             dataBound: onDataBound,
	        columns: [
	            {
	                title: "TT",
	                field: "stt",
	                editable: false,
	                template: function () {
	                    return ++record;
	                },
	                width: '50px',
	                columnMenu: false,
	                headerAttributes: {style: "text-align:center;font-weight: bold;"},
	                attributes: {
	                    style: "text-align:center;"
	                }
	            },
	            {
	                title: "Hạng mục",
	                field: 'workItemName',
	                width: '200px',
	                headerAttributes: {style: "text-align:center;font-weight: bold;"},
	                attributes: {
	                    style: "text-align:left;"
	                }
	            },
	            {
	                title: "SL nghiệm thu",
	                field: 'merchandiseQuantity',
	                editable: true,
	                width: '100px',
	                headerAttributes: {style: "text-align:center;font-weight: bold;"},
	                attributes: {
	                    style: "text-align:left;"
	                },
	                template: "<input type='number' id='merchandiseQuantityId' class='form-control' ng-model='dataItem.merchandiseQuantity'/>",
	            },
	           
	        ]
	    });
    }
    
    function refreshGridAcceptance(){
    	var grid = vm.DSVTAGrid;
    	if(grid){
    		grid.dataSource.query({
                page: 1,
                pageSize: 10
            });
            grid.refresh();
    	}
    }
    
    //Xác nhận thêm hạng mục vật tư A cấp
    vm.listOptionsWorkItem = [];
    vm.listSave = [];
    vm.saveWorkItem = function(dataItem){
    	var grid = $("#merchandiseId").data("kendoGrid").dataSource.data();
    	var gridAcceptance = $("#DSVTA").data("kendoGrid").dataSource.data();
    	for(var j=0;j<grid.length;j++){
    		grid[j].goodsCode = vm.acceptanceObj.maVttb;
        	grid[j].goodsName = vm.acceptanceObj.tenVttb;
        	grid[j].goodsUnitName = vm.acceptanceObj.donViTinh;
        	grid[j].goodsId = vm.acceptanceObj.goodsId;
    	}
    	var sumQuantity = 0;
    	for(var i=0;i<grid.length;i++){
    		sumQuantity += grid[i].merchandiseQuantity;
    	}
    	if(sumQuantity + vm.slThuHoi >vm.slXuat){
			toastr.warning("Tổng số lượng nghiệm thu và thu hồi phải nhỏ hơn số lượng xuất, thực hiện cập nhật lại thông tin nghiệm thu !");
			return;
		}
    	confirm('Xác nhận cập nhật vật tư ?', function () {
//        		managementAcceptanceService.updateWorkItemMerchan(grid).then(function (d) {
//        			toastr.success("Cập nhật thông tin VTTB nghiệm thu thành công !");
//        			modalInstance2.dismiss();
//        			refreshGridAcceptance();
//            	}, function (errResponse) {
//                    toastr.success("Có lỗi khi cập nhật!");
//            	});
    		for(var j=0;j<gridAcceptance.length;j++){
    			if(gridAcceptance[j].goodsId==vm.acceptanceObj.goodsId){
    				gridAcceptance[j].slNghiemThu = sumQuantity;
    				gridAcceptance[j].slConLai = numberWithCommas(gridAcceptance[j].slXuat) - numberWithCommas(gridAcceptance[j].slNghiemThu) - numberWithCommas(gridAcceptance[j].slThuHoi);
    			}
    		}
    		for(var k=0;k<grid.length;k++){
    			vm.listOptionsWorkItem.push(grid[k]);
    		}
    		vm.listSave.push(vm.listOptionsWorkItem);
    		modalInstance2.dismiss();
    		$("#DSVTA").data("kendoGrid").refresh();
        });
    }
    
  //Xác nhận thêm hạng mục vật tư B cấp
    vm.listOptionsWorkItemB = [];
    vm.listSaveB = [];
    vm.saveWorkItemB = function(dataItem){
    	var grid = $("#merchandiseBId").data("kendoGrid").dataSource.data();
    	var gridAcceptance = $("#DSVTB").data("kendoGrid").dataSource.data();
    	for(var j=0;j<grid.length;j++){
    		grid[j].goodsCode = vm.acceptanceObj.maVttb;
        	grid[j].goodsName = vm.acceptanceObj.tenVttb;
        	grid[j].goodsUnitName = vm.acceptanceObj.donViTinh;
        	grid[j].goodsId = vm.acceptanceObj.goodsId;
    	}
    	var sumQuantity = 0;
    	for(var i=0;i<grid.length;i++){
    		sumQuantity += grid[i].merchandiseQuantity;
    	}
    	if(sumQuantity + vm.slThuHoiB >vm.slXuatB){
			toastr.warning("Tổng số lượng nghiệm thu và thu hồi phải nhỏ hơn số lượng xuất, thực hiện cập nhật lại thông tin nghiệm thu !");
			return;
		}
    	confirm('Xác nhận cập nhật vật tư ?', function () {
//        		managementAcceptanceService.updateWorkItemMerchanB(grid).then(function (d) {
//        			toastr.success("Cập nhật thông tin VTTB nghiệm thu thành công !");
//        			modalInstance5.dismiss();
//            	}, function (errResponse) {
//                    toastr.error("Có lỗi khi cập nhật!");
//                    return;
//            	});
//        		fillDataAdditionB();
    		for(var j=0;j<gridAcceptance.length;j++){
    			if(gridAcceptance[j].goodsId==vm.acceptanceObj.goodsId){
    				gridAcceptance[j].slNghiemThu = sumQuantity;
    				gridAcceptance[j].slConLai = numberWithCommas(gridAcceptance[j].slXuat) - numberWithCommas(gridAcceptance[j].slNghiemThu) - numberWithCommas(gridAcceptance[j].slThuHoi);
    			}
    		}
    		for(var k=0;k<grid.length;k++){
    			vm.listOptionsWorkItemB.push(grid[k]);
    		}
    		vm.listSaveB.push(vm.listOptionsWorkItemB);
    		modalInstance5.dismiss();
    		$("#DSVTB").data("kendoGrid").refresh();
        });
    }
//    function fillDataDSTBA(){
    vm.DSTBAGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
//                filterable: true,
                editable: false,
//                save : function(){
//                    vm.DSTBAGrid.refresh();
//                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
//                dataBound: function(e) {
//                	debugger;
//                    var grid = this;
//                    var rows = grid.items();
//
//                    $(rows).each(function(e) {
//                        var row = this;
//                        var dataItem = grid.dataItem(row);
//
//                        if (dataItem.UnitPrice >= 22) {
//                            grid.select(row);
//                        }
//
//                    });
//                },
                pageable: {
                    refresh: false,
                    pageSize: 5,
                    pageSizes: [5,10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
  //             dataBound: onDataBound,
                columns: [
                    {
                        title: "TT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '30px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Mã VTTB",
                        field: 'maVttb',
                        width: '90px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tên VTTB",
                        field: 'tenVttb',
                        width: '90px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Đơn vị tính",
                        field: 'donViTinh',
                        width: '50px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Serial",
                        field: 'serial',
                        width: '70px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Nghiệm thu",
                        field: 'serial',
                        width: '70px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function(data){
                        	return "<input type='checkbox' data-order = '"+record+"' id='childcheckAcceptance'  ng-click='caller.handleCheck(dataItem)' ng-model='dataItem.selected' ng-disabled='dataItem.selectedThuHoi==true'/>";
                        }
                    },
                    {
                        title: "Đã thu hồi",
                        field: 'serial',
                        width: '70px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function(data){
                                return "<input type='checkbox' data-order = '"+record+"' id='childcheckThuHoi' ng-click='caller.handleCheckThuHoi(dataItem)' ng-model='dataItem.selectedThuHoi' disabled />";
                        },
                    },
                ]
            });
//    }
    
    //Table vật tư B
    vm.DSVTBGridOptions = kendoConfig.getGridOptions({
        autoBind: true,
        scrollable: true,
        resizable: true,
        save : function(){
            vm.DSVTBGrid.refresh();
        },
        dataBinding: function () {
            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
        },
        pageable: {
            refresh: false,
            pageSize: 5,
            pageSizes: [5,10, 15, 20, 25],
            messages: {
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
                width: '50px',
                editable: false,
                type:'text',
                columnMenu: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:center;"
                }

            },
            {
                title: "Mã VTTB",
                field: 'maVttb',
                width: '90px',
                type:'text',
                editable: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }

            },
            {
                title: "Tên VTTB",
                field: 'tenVttb',
                type:'text',
                width: '150px',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                editable: false
            },
            {
                title: "Đơn vị tính",
                field: 'donViTinh',
                width: '80px',
                type:'text',
                editable: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }
            },
            {
                title: "SL xuất",
                field: 'slXuat',
                width: '80px',
                type: 'number',
                editable: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                template: function(data){
                    return numberWithCommas(data.slXuat);
                }
            },
            {
                title: "SL nghiệm thu",
                field: 'slNghiemThu',
                width: '80px',
                type: 'number',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: false,
                template: function(data){
                    return numberWithCommas(data.slNghiemThu);
                }
            },
            {
                title: "SL đã thu hồi",
                field: 'slThuHoi',
                width: '80px',
                type: 'number',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                editable: false,
                template: function(data){
                    return numberWithCommas(data.slThuHoi);
                }
            },
            {
                title: "SL còn lại",
                field: 'slConLai',
                width: '80px',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:right;"
                },
                type: 'number',
                editable: false,
                template: function(data){
                    return numberWithCommas(data.slConLai);
                }
            },
            {
                title: "Thao tác",
                type :'text',
                editable:false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                template: dataItem =>
                '<div class="text-center">'
                + '<button style=" border: none; background-color: white;" id="updateId" ng-click="caller.detailMerchandiseB(dataItem)" class=" icon_table "' +
                '   uib-tooltip="Chi tiết" translate>' +
                '<i class="fa fa-list-alt" style="color:#e0d014"  ng-hide="dataItem.status==0"   aria-hidden="true"></i>' +
                '</button>'
                + '</div>',
				width: '50px',
				field: "action",
            }
        ]
    });
        
    //Table thiết bị B
//    function fillDataTBB(data){
    	vm.DSTBBGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: true,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            pageable: {
                refresh: false,
                pageSize: 5,
                pageSizes: [5,10, 15, 20, 25],
                messages: {
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
                    width: '50px',
                    columnMenu: false,
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                {
                    title: "Mã VTTB",
                    field: 'maVttb',
                    width: '90px',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Tên VTTB",
                    field: 'tenVttb',
                    width: '150px',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Đơn vị tính",
                    field: 'donViTinh',
                    width: '80px',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Serial",
                    field: 'serial',
                    width: '150px',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Nghiệm thu",
                    field: 'serial',
                    editable: false,
                    width: '80px',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function(data){
                    	return "<input type='checkbox' data-order = '"+record+"' id='childcheckB' name='gridcheckboxB' ng-click='caller.handleCheckB(dataItem)' ng-model='dataItem.selected'/>";
                    }
                },
                {
                    title: "Đã thu hồi",
                    field: 'serial',
                    editable: false,
                    width: '80px',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function(data){
                        return "<input type='checkbox' data-order = '"+record+"' id='childcheckB' name='gridcheckboxB' ng-click='vm.handleCheckThuHoiB(dataItem)' disabled/>";
                    },
                },
            ]
        });
//    }
    
    vm.listCheck = [];
    vm.handleCheck = handleCheck;
    function handleCheck(dataItem) {
            if (dataItem.selected) {
            	vm.listCheck.push(dataItem);
            	 var templateUrl="coms/managementAcceptance/optionsWorkItem.html";
                 var title="Thông tin hạng mục";
                 var windowId="DETAIL_WORK_ITEM";
                 vm.synStockTransDetailId=dataItem.synStockTransDetailId;
                 CommonService.populatePopupCreate(templateUrl,title,vm.acceptanceSearch,vm,windowId,false,'85%','90%',"null");
                 setTimeout(function(){
                 	modalInstance3 = CommonService.getPopupClose();
                 },10);
            } else {
                    for (var i = 0; i < vm.listCheck.length; i++) {
                        if (vm.listCheck[i].workItemId === dataItem.workItemId) {
                        	vm.listCheck.splice(i, 1);
                        }
                    }
//                    $('[name="gridchkselectall"]').prop('checked', false);
            };

    }
    
    vm.listCheckB = [];
    var modalInstance7 = null;
    vm.handleCheckB = handleCheckB;
    function handleCheckB(dataItem) {
            if (dataItem.selected) {
            	vm.listCheckB.push(dataItem);
            	 var templateUrl="coms/managementAcceptance/optionsWorkItemTBB.html";
                 var title="Thông tin hạng mục";
                 var windowId="DETAIL_WORK_ITEM";
                 vm.synStockTransDetailId=dataItem.synStockTransDetailId;
                 CommonService.populatePopupCreate(templateUrl,title,vm.acceptanceSearch,vm,windowId,false,'85%','90%',"null");
                 setTimeout(function(){
                 	modalInstance7 = CommonService.getPopupClose();
                 },10);
            } else {
                    for (var i = 0; i < vm.listCheckB.length; i++) {
                        if (vm.listCheckB[i].workItemId === dataItem.workItemId) {
                        	vm.listCheckB.splice(i, 1);
                        }
                    }
//                    $('[name="gridchkselectall"]').prop('checked', false);
            };

    } 
    
    function returnMerchan(){
    	modalInstance2.dismiss();
    }
    function returnMerchanB(){
    	modalInstance5.dismiss();
//    	CommonService.dismissPopup1();
    }
    
}