(function() {
	'use strict';
	var controllerId = 'shipmentDetailController';
	
	angular.module('MetronicApp').controller(controllerId, shipmentDetailController);
	
	function shipmentDetailController($scope, $rootScope, $timeout, gettextCatalog, 
			kendoConfig, $kWindow,shipmentDetailService,
			CommonService, PopupConst, Restangular, RestEndpoint,Constant) {
		var vm = this;
		vm.tab1 = true;
		vm.tab2 = false;
		vm.shipmentGet = {};
		vm.shipmentDetail = {};
		vm.docso = {};
		vm.kcsSearch = {};
		vm.kcsSearch2 = {};
		vm.kcs = {};
		vm.kcs2 = {};
		vm.orderSreach ={};
		vm.order = {};
		vm.stockSearch={};
		vm.stock={};
		vm.btn1 = false; vm.btn2 = true; vm.btn3 = true; vm.btn4 = false;
		vm.doSearchFile=
		{
				type : 1
		}
		vm.doSearchMap = {};
		
		vm.gotoTaxPopUp = function(){
			vm.tab1 = true;
			vm.btn1 = false; vm.btn2 = true; vm.btn3 = true; vm.btn4 = false;
		}
		
		vm.gotoPricePopUp = function(){
			vm.tab1 = false;
			vm.btn1 = true; vm.btn2 = false; vm.btn3 = false; vm.btn4 = true;
		}
		
		
		
		initFormData();
		function initFormData() {
			fillFileTable([]);
			fillShipmentGoodsMapTable([]);
			fillGoodsTable([]);
			fillGoodsDetailTable([]);
			fillGoodsDetail1Table([]);
			
		}
		//begin
		vm.showTabOne = true;
		vm.showTabTwo = false;
		vm.showTabThree = false;
		vm.showTabFour = false;
		vm.showTabFive = false;
		vm.showTabSix = false;
		vm.gotoTabOnePopUp = function(){
			vm.showTabOne = true;
			vm.showTabTwo = false;
			vm.showTabThree = false;
			vm.showTabFour = false;
			vm.showTabFive = false;
			vm.showTabSix = false;
			$("#one").addClass("left right  active");
			$("#two").removeClass("right left active");
			$("#three").removeClass("right left active");
			$("#four").removeClass("right left active");
			$("#five").removeClass("right left active");
			$("#six").removeClass("right left active");
			$("#two").addClass("Left right");
			$("#three").addClass("Left right");
			$("#four").addClass("Left right");
			$("#five").addClass("Left right");
			$("#six").addClass("Left right");
		}
		vm.gotoTabOnePopUp = function(){
			vm.showTabOne = true;
			vm.showTabTwo = false;
			vm.showTabThree = false;
			vm.showTabFour = false;
			vm.showTabFive = false;
			vm.showTabSix = false;
			$("#one").addClass("right left active");
			$("#two").removeClass("right left active");
			$("#three").removeClass("right left active");
			$("#four").removeClass("right left active");
			$("#five").removeClass("right left active");
			$("#six").removeClass("right left active");
			$("#two").addClass("Left right");
			$("#three").addClass("Left right");
			$("#four").addClass("Left right");
			$("#five").addClass("Left right");
			$("#six").addClass("Left right");
		}
		vm.gotoTabTwoPopUp = function(){
			vm.showTabOne = false;
			vm.showTabTwo = true;
			vm.showTabThree = false;
			vm.showTabFour = false;
			vm.showTabFive = false;
			vm.showTabSix = false;
			$("#two").addClass("right left active");
			$("#one").removeClass("right left active");
			$("#three").removeClass("right left active");
			$("#four").removeClass("right left active");
			$("#five").removeClass("right left active");
			$("#six").removeClass("right left active");
			$("#one").addClass("Left right");
			$("#three").addClass("Left right");
			$("#four").addClass("Left right");
			$("#five").addClass("Left right");
			$("#six").addClass("Left right");
			//vm.bt1 = false; vm.bt2 = true; vm.bt3 = true; vm.bt4 = false;
		}
		vm.gotoTabThreePopUp = function(){
			vm.showTabOne = false;
			vm.showTabTwo = false;
			vm.showTabThree = true;
			vm.showTabFour = false;
			vm.showTabFive = false;
			vm.showTabSix = false;
			$("#three").addClass("right left active");
			$("#one").removeClass("right left active");
			$("#two").removeClass("right left active");
			$("#four").removeClass("right left active");
			$("#five").removeClass("right left active");
			$("#six").removeClass("right left active");
			$("#one").addClass("Left right");
			$("#two").addClass("Left right");
			$("#four").addClass("Left right");
			$("#five").addClass("Left right");
			$("#six").addClass("Left right");
			//vm.bt1 = false; vm.bt2 = true; vm.bt3 = true; vm.bt4 = false;
		}
		vm.gotoTabFourPopUp = function(){
			vm.showTabOne = false;
			vm.showTabTwo = false;
			vm.showTabThree = false;
			vm.showTabFour = true;
			vm.showTabFive = false;
			vm.showTabSix = false;
			$("#four").addClass("right left active");
			$("#one").removeClass("right left active");
			$("#three").removeClass("right left active");
			$("#two").removeClass("right left active");
			$("#five").removeClass("right left active");
			$("#six").removeClass("right left active");
			$("#one").addClass("Left right");
			$("#three").addClass("Left right");
			$("#two").addClass("Left right");
			$("#five").addClass("Left right");
			$("#six").addClass("Left right");
			//vm.bt1 = false; vm.bt2 = true; vm.bt3 = true; vm.bt4 = false;
		}
		vm.gotoTabFivePopUp = function(){
			vm.showTabOne = false;
			vm.showTabTwo = false;
			vm.showTabThree = false;
			vm.showTabFour = false;
			vm.showTabFive = true;
			vm.showTabSix = false;
			$("#five").addClass("right left active");
			$("#one").removeClass("right left active");
			$("#three").removeClass("right left active");
			$("#four").removeClass("right left active");
			$("#two").removeClass("right left active");
			$("#six").removeClass("right left active");
			$("#one").addClass("Left right");
			$("#three").addClass("Left right");
			$("#four").addClass("Left right");
			$("#two").addClass("Left right");
			$("#six").addClass("Left right");
			//vm.bt1 = false; vm.bt2 = true; vm.bt3 = true; vm.bt4 = false;
		}
		vm.gotoTabSixPopUp = function(){
			vm.showTabOne = false;
			vm.showTabTwo = false;
			vm.showTabThree = false;
			vm.showTabFour = false;
			vm.showTabFive = false;
			vm.showTabSix = true;
			$("#six").addClass("right left active");
			$("#one").removeClass("right left active");
			$("#three").removeClass("right left active");
			$("#four").removeClass("right left active");
			$("#five").removeClass("right left active");
			$("#two").removeClass("right left active");
			$("#one").addClass("Left right");
			$("#three").addClass("Left right");
			$("#four").addClass("Left right");
			$("#five").addClass("Left right");
			$("#two").addClass("Left right");
			//vm.bt1 = false; vm.bt2 = true; vm.bt3 = true; vm.bt4 = false;
		}
		//
		// lay danh sach file dinh kem
		var record=0;
		function fillFileTable(dataSource) {
			vm.gridFile1Options = kendoConfig.getGridOptions({
				autoBind: true,
				sortable: true,
				resizable: true,
				columnMenu: false,			 
				dataSource: data,
				noRecords: true,
				messages: {
					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
				},
				dataBound: function () {
					var GridDestination = $("#shipmentFile1Grid").data("kendoGrid");                
			                GridDestination.pager.element.hide();
                },
				columns: [{
					title: "TT",
					field:"stt",
					template: dataItem => $("#shipmentFile1Grid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
			        width: 30,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}
				,  {
					title: "Lo???i file ????nh k??m",
					field: 'appParamName',
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},  {
					title: "T??n file",
					field: 'name',
			        width: 150,
					template :  function(dataItem) {
						        	    	 return "<a href='' ng-click='caller.downloadFile(dataItem)'>" + dataItem.name + "</a>";
					},
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}
				
				,]
			});
		}//end
		
		
		//begin
		//goi service lay danh sach file
		function doSearchFile()
		{
			vm.doSearchFile.objectId = vm.shipmentDetail.shipmentId;
		   
		    shipmentDetailService.doSearchFile(vm.doSearchFile).then(function(d) {
				refreshGrid(d.plain(),vm.shipmentFile1Grid);
			}, function(errResponse) {
				console.error('Error T??m ki???m');
			});
		}//end
		
		//begin
		//d? li?u load v??o grid Map h??ng h??a
		function fillShipmentGoodsMapTable(data) {
			vm.optionsShipmentGoodsMap = kendoConfig.getGridOptions({
				autoBind: true,
				sortable: false,
				resizable: true,
				columnMenu: false,			 
				dataSource:data,
				noRecords: true,
				scrollable: false,
				messages: {
					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
				},
				pageable: {
					refresh: false,
					pageSize:10,
					 pageSizes: [10, 15, 20, 25],
					messages: {
		                display: "{0}-{1} c???a {2} k???t qu???",
		                itemsPerPage: "k???t qu???/trang",
		                empty: "Kh??ng c?? k???t qu??? hi???n th???"
		            }
				},
				dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
				columns: [{
					title: "TT",
					field:"stt",
					template: function () {
						  return ++record;
						 },
			        width: "5%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}
				, {
					title: "M?? h??ng",
					field: 'goodsCode',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},  {
					title: "T??n h??ng",
					field: 'goodsName',
			        width: "25%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}
				,{
					title: "????n v??? t??nh",
					field: 'unitTypeName',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "H??ng s???n xu???t",
					field: 'manufacturerName',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "N?????c s???n xu???t",
					field: 'producingCountryName',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "Partnumber",
					field: 'partNumber',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},
				{
					title: "Serial",
					field: 'serial',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},
				{
					title: "S??? l?????ng",
					field: 'amount',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				}
				,]
			});
		}
		//end
		
		//begin
		//g?i t?i service h??ng h??a
		function getGoodsMap()
		{
			
			vm.doSearchMap.shipmentId = vm.shipmentDetail.shipmentId;
			
		    shipmentDetailService.getMapGoodsDetail(vm.doSearchMap).then(function(d) {
				refreshGrid(d.plain(),vm.gridShipmentGoodsMap);
			}, function(errResponse) {
				console.error('Error T??m ki???m');
			});
		} 
		
		//end
		
		
		
		
		//begin
		//grid d?nh lu?ng k? thu?t
		function fillGoodsTable(data) {
			vm.gridGoodsOptions = kendoConfig.getGridOptions({
				autoBind: true,
				sortable: true,
				resizable: true,
				columnMenu: false,			 
				dataSource:data,
				noRecords: true,
				scrollable: false,
				messages: {
					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
				},
				pageable: {
					refresh: false,
					pageSize:10,
					 pageSizes: [10, 15, 20, 25],
					messages: {
		                display: "{0}-{1} c???a {2} k???t qu???",
		                itemsPerPage: "k???t qu???/trang",
		                empty: "Kh??ng c?? k???t qu??? hi???n th???"
		            }
				},
				dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
				columns: [{
					title: "TT",
					field:"stt",
					template: function () {
						  return ++record;
						 },
			        width: "5%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}
				,  {
					title: "M?? h??ng",
					field: 'goodsCode',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},  {
					title: "T??n h??ng",
					field: 'goodsName',
			        width: "20%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}
				,{
					title: "????n v???<br/> t??nh",
					field: 'unitTypeName',
			        width: "5%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "S??? l?????ng",
					field: 'amount',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				},{
					title: "?????nh l?????ng cho 1<br/> ????n v??? HH",
					field: 'estimatePrice',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
					format:"{0:n}",
				},{
					title: "?????nh l?????ng theo<br/> danh m???c",
					field: 'estimatePriceSum',
			        width: "10%",
					format:"{0:n}",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				},{
					title: "T??? l??? % theo<br> danh m???c",
					field: 'estimatePricePercent',
			        width: "10%",
					format:"{0:n}",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				},{
					title: "????n gi??",
					field: 'originPrice',
			        width: "10%",
					format:"{0:n}",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				},{
					title: "Th??nh ti???n",
					field: 'totalOriginPrice',
			        width: "10%",
			        format:"{0:n}",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				}
				,]
			});
		}//end
		
		
		//begin
		//thong tin gia
		function fillGoodsDetail1Table(data) {
			vm.gridShipmentsDetailOptions = kendoConfig.getGridOptions({
				autoBind: true,
				sortable: true,
				resizable: true,
				columnMenu: false,			 
				dataSource:data,
				noRecords: true,
				scrollable: false,
				messages: {
					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
				},
				pageable: {
					refresh: false,
					pageSize:10,
					 pageSizes: [10, 15, 20, 25],
					messages: {
		                display: "{0}-{1} c???a {2} k???t qu???",
		                itemsPerPage: "k???t qu???/trang",
		                empty: "Kh??ng c?? k???t qu??? hi???n th???"
		            }
				},
				dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
				columns: [{
					title: "TT",
					field:"stt",
					template: function () {
						  return ++record;
						 },
			        width: "5%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}
				,  {
					title: "M?? h??ng",
					field: 'goodsCode',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},  {
					title: "T??n h??ng",
					field: 'goodsName',
			        width: "25%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}
				,{
					title: "????n v??? t??nh",
					field: 'unitTypeName',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "S??? l?????ng",
					field: 'amount',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				},{
					title: "T??? l??? % theo<br/> danh m???c",
					field: 'estimatePricePercent',
			        width: "10%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				},{
					title: "????n gi?? ph??n b???",
					field: 'applyPrice',
			        width: "15%",
					format:"{0:n}",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				},{
					title: "Th??nh ti???n",
					field: 'applyTotalMoney',
			        width: "15%",
					format:"{0:n}",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				}
				,],
				dataBound: function(e) {
					var grid = $("#shipmentGoodsDetailGrid").data("kendoGrid");
                    var gridData =  grid.dataSource.view();
					if(gridData.length> 0){
                    	vm.applyTotalMoney = 0;
                    	for (var i = 0; i < gridData.length; i++) {
	                        vm.applyTotalMoney += gridData[i].applyTotalMoney ;
	                     }
                    	
                 
                    }
	                },
			});
		}//end
		
		//begin
		//thoong tin thue
		function fillGoodsDetailTable(data) {
			vm.gridGoodsDetailOptions = kendoConfig.getGridOptions({
				autoBind: true,
				sortable: true,
				resizable: true,
				columnMenu: false,			 
				dataSource: data,
				noRecords: true,
				scrollable: false,
				messages: {
					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
				},
				pageable: {
					refresh: false,
					pageSize:10,
					 pageSizes: [10, 15, 20, 25],
					messages: {
		                display: "{0}-{1} c???a {2} k???t qu???",
		                itemsPerPage: "k???t qu???/trang",
		                empty: "Kh??ng c?? k???t qu??? hi???n th???"
		            }
				},
				dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
				columns: [{
					title: "TT",
					field:"stt",
					template: function () {
						  return ++record;
						 },
			        width: "5%",
			        columnMenu: false,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}
				, {
					title: "T??n thu???",
			        field: 'name',
			        width: "25%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "<b>Thu??? xu???t <br> (%)</b>",
			        field: 'valuetax',
			        width: "15%",
					format:"{0:n}",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				}, {
					title: "Lo???i thu???",
					field: 'type',
					template : "# if(type == 1){ #" + "#= '?????u v??o' #" + "# } " + "else if (type == 2) { # " + "#= '?????u ra' #"+ "#} #",
			        
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "Ph??n b???",
			        field: 'apply',
			        template :  "# if(apply == 1){ #" + "#= 'C??' #" + "# } " + "else if (apply == 0) { # " + "#= 'Kh??ng' #"+ "#} #",
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "Mi???n thu???",
			        field: 'ignore',
			        template :  "# if(ignore == 1){ #" + "#= 'C??' #" + "# } " + "else if (ignore == 0) { # " + "#= 'Kh??ng' #"+ "#} #",
			        width: "15%",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "Ti???n thu??? (VN??)",
					field: 'valueshipmenttax',
			        width: "20%",
					format:"{0:n}",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:right;"
					},
				},]
			});
		}//end
		
		//begin
		//g?i t?i c??c h??m 
		$scope.$on("shipment.object.data", function(event, dataItem) {
			vm.shipmentGet = dataItem;
			vm.shipmentDetail = vm.shipmentGet;
			doSearchFile();
			doSearchMap();
		
			doSearchTAX();
		    getDataKCS1();
			getDataKCS2(); 
			getOrderByShipment();
			 getStockByOrder(); 
			getGoodsMap();
			DocTien(dataItem);
			if(vm.shipmentDetail.status=="1"){
			$("#map").removeClass('acceptQLK postion-icon');
			$("#quantity").removeClass('acceptQLK postion-icon');
			$("#price").removeClass('acceptQLK postion-icon');
			$("#kcs").removeClass('acceptQLK postion-icon');
			$("#import").removeClass('acceptQLK postion-icon');
			$("#map").addClass('declineQLK postion-icon');
			$("#quantity").addClass('declineQLK postion-icon');
			$("#price").addClass('declineQLK postion-icon');
			$("#kcs").addClass('declineQLK postion-icon');
			$("#import").addClass('declineQLK postion-icon');
			}
			if(vm.shipmentDetail.status=="2"){
			$("#quantity").removeClass('acceptQLK postion-icon');
			$("#price").removeClass('acceptQLK postion-icon');
			$("#kcs").removeClass('acceptQLK postion-icon');
			$("#import").removeClass('acceptQLK postion-icon');
			$("#map").addClass('declineQLK postion-icon');
			$("#quantity").addClass('declineQLK postion-icon');
			$("#price").addClass('declineQLK postion-icon');
			$("#kcs").addClass('declineQLK postion-icon');
			$("#import").addClass('declineQLK postion-icon');
			}
			if(vm.shipmentDetail.status=="3"){
					$("#quantity").removeClass('declineQLK postion-icon');
					$("#price").removeClass('acceptQLK postion-icon');
					$("#kcs").removeClass('acceptQLK postion-icon');
					$("#import").removeClass('acceptQLK postion-icon');
					$("#quantity").addClass('acceptQLK postion-icon');
					$("#price").addClass('declineQLK postion-icon');
					$("#kcs").addClass('declineQLK postion-icon');
					$("#import").addClass('declineQLK postion-icon');
			}
			if(vm.shipmentDetail.status=="4"){
					$("#quantity").removeClass('declineQLK postion-icon');
					$("#price").removeClass('declineQLK postion-icon');
					$("#kcs").removeClass('acceptQLK postion-icon');
					$("#import").removeClass('acceptQLK postion-icon');
					$("#quantity").addClass('acceptQLK postion-icon');
					$("#price").addClass('acceptQLK postion-icon');
					$("#kcs").addClass('declineQLK postion-icon');
					$("#import").addClass('declineQLK postion-icon');
			}

		});
		//end

		function getOrderByShipment()
		{
			vm.orderSreach.shipmentId = vm.shipmentDetail.shipmentId;
		    shipmentDetailService.getOderByShipmentId(vm.orderSreach).then(function(d) {
				vm.order=d.plain();
			}, function(errResponse) {
				console.error('Error T??m ki???m');
			});
		}
		
		function getStockByOrder()
		{
			vm.stockSearch.shipmentId = vm.shipmentDetail.shipmentId;
		    shipmentDetailService.getStockByOrder(vm.stockSearch).then(function(d) {
				vm.stock=d.plain();
			}, function(errResponse) {
				console.error('Error T??m ki???m');
			});
		} 
		//begin
		//g?i t?i service  KCS
		 function getDataKCS1()
		{
			vm.kcsSearch.objectId = vm.shipmentDetail.shipmentId;
		    vm.kcsSearch.type = 1;
		    shipmentDetailService.getDataKCS(vm.kcsSearch).then(function(d) {
				vm.kcs=d.plain();
			}, function(errResponse) {
				console.error('Error T??m ki???m');
			});
		} //end
		
		//begin
		//g?i t?i service nh?p kho
		 function getDataKCS2()
		{
			vm.kcsSearch2.objectId = vm.shipmentDetail.shipmentId;
		    vm.kcsSearch2.type = 2;
		    shipmentDetailService.getDataKCS(vm.kcsSearch2).then(function(d) {
				vm.kcs2=d.plain();
			}, function(errResponse) {
				console.error('Error T??m ki???m');
			});
		} //end
		
		
		
		//begin
		//ham dinh luong ky thuat
		function doSearchMap()
		{
			vm.doSearchMap.shipmentId = vm.shipmentDetail.shipmentId;
		    shipmentDetailService.doSearchMap(vm.doSearchMap).then(function(d) {
				refreshGrid(d.plain(),vm.shipmentGoodsGrid);
				refreshGrid(d.plain(),vm.shipmentGoodsDetailGrid);
			}, function(errResponse) {
				console.error('Error T??m ki???m');
			});
		}
		//end
		
	$(document).ready(function(){
		$("#cancel").click(function(){
	         // call 'close' method on nearest kendoWindow
	         $(this).closest("[data-role=window]").data("kendoWindow").close();
	         // the above is equivalent to:
	         //$(this).closest(".k-window-content").data("kendoWindow").close();
	       });	
	})
	 vm.taxSearch={

		};
		//begin
		//h??m thong tin thue
		function doSearchTAX()
		{
			vm.taxSearch.shipmentId = vm.shipmentDetail.shipmentId;
			shipmentDetailService.doSearchTAX(vm.taxSearch).then(function(d) {
				refreshGrid(d.plain(),vm.shipmentTaxGrid);
			}, function(errResponse) {
				console.error('Error T??m ki???m');
			});
			
		}//end
		
		function refreshGrid(d,grid) {
			var grid = grid;
			if(grid){
				grid.dataSource.data(d);
				grid.refresh();
			}
		}
		
		
		
		//tien
		var ChuSo=new Array(" kh??ng "," m???t "," hai "," ba "," b???n "," n??m "," s??u "," b???y "," t??m "," ch??n ");
		var Tien=new Array( "", " ngh??n", " tri???u", " t???", " ngh??n t???", " tri???u t???");

		//begin
		//1. H??m d?c s? c?? ba ch? s?;
		function DocSo3ChuSo(baso)
		{
		    var tram;
		    var chuc;
		    var donvi;
		    var KetQua="";
		    tram=parseInt(baso/100);
		    chuc=parseInt((baso%100)/10);
		    donvi=baso%10;
		    if(tram==0 && chuc==0 && donvi==0) return "";
		    if(tram!=0)
		    {
		        KetQua += ChuSo[tram] + " tr??m ";
		        if ((chuc == 0) && (donvi != 0)) KetQua += " linh ";
		    }
		    if ((chuc != 0) && (chuc != 1))
		    {
		            KetQua += ChuSo[chuc] + " m????i";
		            if ((chuc == 0) && (donvi != 0)) KetQua = KetQua + " linh ";
		    }
		    if (chuc == 1) KetQua += " m?????i ";
		    switch (donvi)
		    {
		        case 1:
		            if ((chuc != 0) && (chuc != 1))
		            {
		                KetQua += " m???t ";
		            }
		            else
		            {
		                KetQua += ChuSo[donvi];
		            }
		            break;
		        case 5:
		            if (chuc == 0)
		            {
		                KetQua += ChuSo[donvi];
		            }
		            else
		            {
		                KetQua += " l??m ";
		            }
		            break;
		        default:
		            if (donvi != 0)
		            {
		                KetQua += ChuSo[donvi];
		            }
		            break;
		        }
		    return KetQua;
		}//end
		
		
		
		//begin
		//2. H??m d?c s? th??nh ch? (S? d?ng h??m d?c s? c?? ba ch? s?)

		function DocTienBangChu(SoTien)
		{
		    var lan=0;
		    var i=0;
		    var so=0;
		    var KetQua="";
		    var tmp="";
		    var ViTri = new Array();
		    if(SoTien<0) return "S??? ti???n ??m !";
		    if(SoTien==0) return "Kh??ng ?????ng !";
		    if(SoTien>0)
		    {
		        so=SoTien;
		    }
		    else
		    {
		        so = -SoTien;
		    }
		    if (SoTien > 8999999999999999)
		    {
		        //SoTien = 0;
		    	return "S??? qu?? l???n!";
		    }
		    ViTri[5] = Math.floor(so / 1000000000000000);
		    if(isNaN(ViTri[5]))
		        ViTri[5] = "0";
		    so = so - parseFloat(ViTri[5].toString()) * 1000000000000000;
		    ViTri[4] = Math.floor(so / 1000000000000);
		     if(isNaN(ViTri[4]))
		        ViTri[4] = "0";
		    so = so - parseFloat(ViTri[4].toString()) * 1000000000000;
		    ViTri[3] = Math.floor(so / 1000000000);
		     if(isNaN(ViTri[3]))
		        ViTri[3] = "0";
		    so = so - parseFloat(ViTri[3].toString()) * 1000000000;
		    ViTri[2] = parseInt(so / 1000000);
		     if(isNaN(ViTri[2]))
		        ViTri[2] = "0";
		    ViTri[1] = parseInt((so % 1000000) / 1000);
		     if(isNaN(ViTri[1]))
		        ViTri[1] = "0";
		    ViTri[0] = parseInt(so % 1000);
		  if(isNaN(ViTri[0]))
		        ViTri[0] = "0";
		    if (ViTri[5] > 0)
		    {
		        lan = 5;
		    }
		    else if (ViTri[4] > 0)
		    {
		        lan = 4;
		    }
		    else if (ViTri[3] > 0)
		    {
		        lan = 3;
		    }
		    else if (ViTri[2] > 0)
		    {
		        lan = 2;
		    }
		    else if (ViTri[1] > 0)
		    {
		        lan = 1;
		    }
		    else
		    {
		        lan = 0;
		    }
		    for (i = lan; i >= 0; i--)
		    {
		       tmp = DocSo3ChuSo(ViTri[i]);
		       KetQua += tmp;
		       if (ViTri[i] > 0) KetQua += Tien[i];
		       if ((i > 0) && (tmp.length > 0)) KetQua += '';//&& (!string.IsNullOrEmpty(tmp))
		    }
		   if (KetQua.substring(KetQua.length - 1) == '')
		   {
		        KetQua = KetQua.substring(0, KetQua.length - 1);
		   }
		   KetQua = KetQua.substring(1,2).toUpperCase()+ KetQua.substring(2) + " ?????ng";
		   return KetQua;//.substring(0, 1);//.toUpperCase();// + KetQua.substring(1);
		}
		
		vm.docso={};
		vm.DocTien = DocTien(vm.docso);
		//begin
		//doc tien
		function DocTien(dataItem){
			if(dataItem.totalFee != undefined){
				vm.docso.totalFee = DocTienBangChu(dataItem.totalFee);
			}
			if(dataItem.totalOriginMoney != undefined){
				vm.docso.totalOriginMoney = DocTienBangChu(dataItem.totalOriginMoney);
			}
			if(dataItem.totalTax != undefined){
				vm.docso.totalTax = DocTienBangChu(dataItem.totalTax);
			}
			if(dataItem.totalMoney != undefined){
				vm.docso.totalMoney = DocTienBangChu(dataItem.totalMoney);
			}
		}//end
		
		//Export jasper excel
		vm.exportExcelGrid= function(){
			var obj={};
         
         	obj.shipmentId=vm.shipmentDetail.shipmentId;
         	obj.reportType="EXCEL_MUL";
         	obj.reportNames=["ThongTinChung_HangHoa","DinhLuongKyThuat","DinhGia"];
			obj.reportName="ThongTin_HangHoa";
         	CommonService.exportReport(obj).then(
					function(data) {
					var binarydata= new Blob([data],{ type:'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
			        kendo.saveAs({dataURI: binarydata, fileName: vm.shipmentDetail.code.replace("/","_") + '_' + sessionId() + '.xlsx'});
				}, function(errResponse) {
					toastr.error("L???i kh??ng export PDF ???????c!");
				});
		}
		// /END
		//H??m download file 
		vm.downloadFile = function(dataItem){
				window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + dataItem.filePath;	
		}
		//End
	}
	
	
})();