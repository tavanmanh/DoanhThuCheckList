(function() {
	'use strict';
	var controllerId = 'shipmentPriceController';
	
	angular.module('MetronicApp').controller(controllerId, shipmentPriceController);
	
	function shipmentPriceController($scope, $rootScope, $timeout, gettextCatalog, 
			kendoConfig, $kWindow,shipmentPriceService,shipmentDetailService,
			CommonService, PopupConst, Restangular, RestEndpoint,Constant) {
		var vm = this;
		vm.showSearch = true;
		vm.showDetail1 = true;
		vm.showDetail2 = false;
		initFormData();
		vm.taxSearch={
				status:1,
				type: 3
		
		};
		vm.shipmentSearch =
		{
				type : 0,
				listStatus : ['3']
		};
		function initFormData() {
			fillDataTable([]);
			fillData1Table([]);
			fillData2Table([]);
		}
		
		
		vm.doSearch= doSearch;
		function doSearch(){
			  vm.showDetail = false;
			var grid =vm.shipmentPriceGrid;
			if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 20
				});
			}
		}
		
		function fillDataTable(data) {
			vm.gridPriceOptions = kendoConfig.getGridOptions({
				autoBind: true,
				sortable: false,
				resizable: true,
				columnMenu: false,
				toolbar: [
                    {
                        name: "actions",
                        template: 
                        	 '<div class="btn-group pull-right margin-left10 margin_right10">'+
                             '<i data-toggle="dropdown" aria-expanded="false"><i class="fa fa-cog" aria-hidden="true"></i></i>'+
                             '<i class="action-button excelQLK" ng-click="vm.exportExcelGrid()" aria-hidden="true"></i>'+
                             '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
                             '<label ng-repeat="column in vm.shipmentPriceGrid.columns| filter: vm.gridColumnShowHideFilter">'+
                             '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+
                             '</label>'+
                             '</div></div>'              
                    }
                    ],
				dataBound: function (e) {
				    var grid = vm.shipmentPriceGrid;
				    grid.tbody.find("tr").dblclick(function (e) {
				        var dataItem = grid.dataItem(this);
				        var teamplateUrl="qlk/shipmentDetail/shipmentDetailList.html";
				        var title="Th??ng tin chi ti???t l?? h??ng";
						$kWindow.open({
							 options: {
								 modal: true,
								 title: title,
								 visible: false,
								 width: '88%',
								 height: '88%',
								 actions: ["Minimize", "Maximize", "Close"],
								 open: function () {
									 this.wrapper.children('.k-window-content').addClass("fix-footer");
									 $rootScope.$broadcast("shipment.object.data", dataItem);
								 }
							 },                
							 templateUrl: teamplateUrl
				});
						
				    });
				},
				dataSource:{
					serverPaging: true,
					 schema: {
						 total: function (response) {
								return response.total;
							},
							data: function (response) {
								return response.data;
							},
		                },
					transport: {
						read: {
		                        // Thuc hien viec goi service
							url: Constant.BASE_SERVICE_URL + "shipmentRsServiceRest/shipment/doSearch",
							contentType: "application/json; charset=utf-8",
							type: "POST"
						},					
						parameterMap: function (options, type) {
							vm.shipmentSearch.page = options.page
							vm.shipmentSearch.pageSize = options.pageSize
							return JSON.stringify(vm.shipmentSearch)
						}
					},					 
					pageSize: 20
				},
				noRecords: true,
				messages: {
					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
				},
				pageable: {
					refresh: false,
					 pageSizes: [10, 15, 20, 25],
					messages: {
		                display: "{0}-{1} c???a {2} k???t qu???",
		                itemsPerPage: "k???t qu???/trang",
		                empty: "Kh??ng c?? k???t qu??? hi???n th???"
		            }
				},
				columns: [{
					title: "TT",
					field:"stt",
			        template: dataItem => $("#shipmentPriceGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
			        width: 40,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}
				,  {
					title: "M?? l?? h??ng",
					field: 'code',
			        width: 120,
			        template: dataItem=> '<a class="#=shipmentId#" href="javascript:void(0);" ng-click=vm.showSix(dataItem)> {{dataItem.code}} </a>',
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, 
				 {
					title: "M?? h???p ?????ng",
					field: 'contractCode',
			        width: 110,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "Lo???i l?? h??ng",
			        field: 'type',
			        width: 90,
			        template :  
			        "# if(type == 1){ #" + "#= 'L?? h??ng n???i' #" + "# } " +
			        "else if (type == 2) { # " + "#= 'L?? h??ng ngo???i' #"+ "#} " +
			        "#",
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
					
				},
				{
					title: "Ng??y giao <br> h??ng",
			        field: 'shiperDate',
			        width: 95,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, 
				{
					title: "Ng?????i t???o",
			        field: 'createdBy',
			        width: 90,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},
				 
				{
					title: "????n v???",
			        field: 'projInvestProjectId',
			        width: 200,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "Tr???ng th??i",
					 field: 'status',
					 template :  
					        "# if(status == 01)" +
					        "{ #" + "#= 'M???i t???o' #" + "# } " +
					        "else if (status == 02) " +
					        "{ # " + "#= '???? c???p nh???t h??ng h??a' #"+ "#} " +
					        "else if (status == 03) " +
					        "{ # " + "#= '???? ?????nh l?????ng' #"+ "#} " +
					        "else if (status == 04) " +
					        "{ # " + "#= '???? ?????nh gi??' #"+ "#} " +
					        "else if (status == 05) " +
					        "{ # " + "#= '???? t???o YCKT' #"+ "#} " +
					        "else if (status == 06) " +
					        "{ # " + "#= '???? t???o BBKT' #"+ "#} " +
					        "else if (status == 07) " +
					        "{ # " + "#= '???? nh???p kho' #"+ "#} " +
					        "else if (status == 08) " +
					        "{ # " + "#= '???? h???y' #"+ "#} " +
					        "#",
			        width: 100,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "?????nh gi??",
			        template: dataItem =>			
						'<div class="text-center #=shipmentId#"">'+
						'<a class=" icon_table #=shipmentId#" uib-tooltip="?????nh gi??" translate>'+
						'<i ng-hide="dataItem.status != 3 && dataItem.status != 4" class="fa fa-usd" aria-hidden="true" ng-click=vm.edit(dataItem)></i>'+
						'</a>'+
						'<a class=" icon_table #=shipmentId#" uib-tooltip="?????nh gi??" translate>'+
						'<i ng-hide="dataItem.status == 3 || dataItem.status == 4" class="fa fa-trash" aria-hidden="true"></i>'+
						'</a>'
						+'</div>',
				width: 100,
			        field:"stt"
				}
				,]
			});
		}
		
		
		vm.showSix = function showSix(dataItem){
// var grid = vm.shipmentPriceGrid;
// var item=grid.table.find("tr div." +id);
// var uid=$(item).parent().parent().attr("data-uid");
	    	var dataItem=dataItem;
	        var teamplateUrl="qlk/shipmentDetail/shipmentDetailList.html";
	        var title="Th??ng tin chi ti???t l?? h??ng";
			$kWindow.open({
				 options: {
					 modal: true,
					 title: title,
					 visible: false,
					 width: '88%',
					 height: '88%',
					 actions: ["Minimize", "Maximize", "Close"],
					 open: function () {
						 this.wrapper.children('.k-window-content').addClass("fix-footer");
						 $rootScope.$broadcast("shipment.object.data", dataItem);
					 }
				 },                
				 templateUrl: teamplateUrl
			});
		}
		
		vm.exportExcelGrid = function(){
			var ds = $("#shipmentPriceGrid").data("kendoGrid").dataSource;
	    	var rows = [{
		        cells: [
		          { value: "STT", background: "#D8E4BC", bold: true, borderBottom: { color: "#000000", size: 1 }, borderTop: { color: "#000000", size: 1 }, borderRight: { color: "#000000", size: 1 },borderLeft: { color: "#000000", size: 1 } },
		          { value: "M?? l?? h??ng", background: "#D8E4BC", bold: true, borderBottom: { color: "#000000", size: 1 }, borderTop: { color: "#000000", size: 1 }, borderRight: { color: "#000000", size: 1 },borderLeft: { color: "#000000", size: 1 } },
		          { value: "M?? h???p ?????ng", background: "#D8E4BC", bold: true, borderBottom: { color: "#000000", size: 1 }, borderTop: { color: "#000000", size: 1 }, borderRight: { color: "#000000", size: 1 },borderLeft: { color: "#000000", size: 1 } },
		          { value: "Lo???i l?? h??ng", background: "#D8E4BC", bold: true, borderBottom: { color: "#000000", size: 1 }, borderTop: { color: "#000000", size: 1 }, borderRight: { color: "#000000", size: 1 },borderLeft: { color: "#000000", size: 1 } },
		          { value: "Ng??y giao h??ng", background: "#D8E4BC", bold: true, borderBottom: { color: "#000000", size: 1 }, borderTop: { color: "#000000", size: 1 }, borderRight: { color: "#000000", size: 1 },borderLeft: { color: "#000000", size: 1 } },
		          { value: "Ng?????i t???o", background: "#D8E4BC", bold: true, borderBottom: { color: "#000000", size: 1 }, borderTop: { color: "#000000", size: 1 }, borderRight: { color: "#000000", size: 1 },borderLeft: { color: "#000000", size: 1 } },
		          { value: "????n v???", background: "#D8E4BC", bold: true, borderBottom: { color: "#000000", size: 1 }, borderTop: { color: "#000000", size: 1 }, borderRight: { color: "#000000", size: 1 },borderLeft: { color: "#000000", size: 1 } },
		          { value: "Tr???ng th??i", background: "#D8E4BC", bold: true, borderBottom: { color: "#000000", size: 1 }, borderTop: { color: "#000000", size: 1 }, borderRight: { color: "#000000", size: 1 },borderLeft: { color: "#000000", size: 1 }}
		        ]
		      }];
	    	ds.fetch(function(){
	    		var data = this.data();
	    		for (var i = 0; i < data.length; i++){
			          //push single row for every record
			        	rows.push({
				            cells: [
				              { value: i+1 , textAlign: "center"},
				              { value: data[i].code },
				              { value: data[i].contractCode},
				              { value: TyoeOfShipment(data[i].type) },
				              { value: data[i].shiperDate },
				              { value: data[i].createdBy },
				              { value: data[i].projInvestProjectId},
				              { value: statusOfShipment(data[i].status)},
				            ]
			          })
			        }
	    		var workbook = new kendo.ooxml.Workbook({
			          sheets: [
			            {
			            	columns: [
			                // Column settings (width)
			                { autoWidth: true },
			                { autoWidth: true },
			                { autoWidth: true },
			                { autoWidth: true },
			                { autoWidth: true },
			                { autoWidth: true },
			                { autoWidth: true },
			                { autoWidth: true },
			              ],
			              
			              // Title of the sheet
			              title: "Danh s??ch l?? h??ng",
			              // Rows of the sheet
			              rows: rows
			            }
			          ]
			        });
	    		kendo.saveAs({dataURI: workbook.toDataURL(), fileName: "Danh s??ch l?? h??ng.xlsx"});
	    	});
		}
		
		function statusOfShipment(x){
			 if(x == 1)
			 { return "M???i t???o";}
			 else if (x == 2)
			 { return "???? c???p nh???t h??ng h??a";}
		     else if (x == 3)
		     { return "???? ?????nh l?????ng";}
		     else if (x == 4){ return "???? ?????nh gi??";}
		     else if (x == 5){ return "???? t???o YCKT";}
		     else if (x == 6){ return "???? t???o BBKT";}
		     else if (x == 7){ return "???? nh???p kho";}
		     else if (x == 8){ return "???? h???y";}
		}
		
		function TypeOfShipment(x){
			if(type == 1){ 
				return "L?? h??ng n???i";
			}else if (type == 2) {
				return "L?? h??ng ngo???i";
			}
			
		}
		
		
		function fillData1Table(data) {
			vm.gridPrice1Options = kendoConfig.getGridOptions({
				autoBind: true,
				resizable: true,			 
				//dataSource:data,
				dataSource:{
					serverPaging: true,
					 schema: {
						 total: function (response) {
								return response.total; // total is returned in the "total" field of the response
							},
							data: function (response) {
								return response.data; // data is returned in the "data" field of the response
							},
		                },
					transport: {
						read: {
		                        //Thuc hien viec goi service
							url: Constant.BASE_SERVICE_URL + "taxRsServiceRest/tax/doSearch",
							contentType: "application/json; charset=utf-8",
							type: "POST"
						},					
						parameterMap: function (options, type) {
                              //  vm.taxSearch.employeeId = Constant.user.srvUser.catEmployeeId;
							    vm.taxSearch.page = options.page
								vm.taxSearch.pageSize = options.pageSize

								return JSON.stringify(vm.taxSearch)

						}
					},					 
					pageSize: 20
				},
				noRecords: true,
				messages: {
					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
				},
				pageSizes: [10, 15, 20, 25],
				columns: [{
					title: "STT",
					field:"stt",
			        template: dataItem => $("#gridPrice1Options").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
			        width: 70,
			        columnMenu: false,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
				}
				,  {
					title: "M?? thu???",
					field: 'code',
			        width: 95,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "T??n thu???",
			        field: 'name',
			        width: 200,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "Thu??? xu???t \(%)",
			        field: 'value',
			        width: 130,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "Lo???i thu???",
					template :  "# if(type == 2){ #" + "#= '?????u ra' #" + "# } " + "else if (type == 1) { # " + "#= '?????u v??o' #"+ "#} #",
			        field: 'type',
			        width: 100,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "Ph??n b???",
					template :  "# if(apply == 0){ #" + "#= 'Kh??ng' #" + "# } " + "else if (apply == 1) { # " + "#= 'C??' #"+ "#} #",
			        field: 'apply',
			        width: 100,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "Mi???n thu???",
					template :  "# if(ignore == 0){ #" + "#= 'Kh??ng' #" + "# } " + "else if (ignore == 1) { # " + "#= 'C??' #"+ "#} #",
			        field: 'ignore',
			        width: 110,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "Ti???n thu??? (VN??)",
					template :  "# if(ignore == 0){ #" + "#= 'Kh??ng' #" + "# }",
			        width: 110,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},]
			});
		}
		
		function fillData2Table(data) {
			vm.gridPrice2Options = kendoConfig.getGridOptions({
				autoBind: true,
				resizable: true,			 
				dataSource:  data,
				noRecords: true,
				messages: {
					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
				},
				pageSizes: [10, 15, 20, 25],
				columns: [{
					title: "STT",
					field:"stt",
			        template: dataItem => $("#shipmentGoodsGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
			        width: 70,
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
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},  {
					title: "T??n h??ng",
					field: 'goodsName',
			        width: 150,
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
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "S??? l?????ng",
					field: 'amount',
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "?????nh l?????ng cho 1 ????n v??? HH",
					field: 'applyPrice',
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "?????nh l?????ng theo danh m???c",
					field: 'estimatePriceSum',
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "T??? l??? % theo danh m???c",
					field: 'estimatePricePercent',
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "????n gi??",
					field: 'applyPrice',
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "Th??nh ti???n",
					field: 'applyTotalMoney',
			        width: 150,
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				}
				,]
			});
		}
		vm.openDepartment=openDepartment
		function openDepartment(){
			var obj={};
// obj.page=1;
// obj.pageSize=20;
			CommonService.getDepartment(obj).then(function(result){
				var templateUrl = 'qlk/popup/findDepartmentPopUp.html';
				var title = gettextCatalog.getString("????n v???");
				var data=result.plain();
				vm.gridOptions = kendoConfig.getGridOptions({
					autoBind: true,
					resizable: true,
					dataSource: result,
					noRecords: true,
					messages: {
						noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
					},
					pageable: {
						refresh: false,
						 pageSizes: [10, 15, 20, 25],
						messages: {
			                display: "{0}-{1} c???a {2} k???t qu???",
			                itemsPerPage: "k???t qu???/trang",
			                empty: "Kh??ng c?? k???t qu??? hi???n th???"
			            }
					},
					columns:[ {
						title: "M??",
						field: "code",
						width: 120
					}, {
						title: "T??n",
						field: "text",
						width: 120
					}]
				});
				CommonService.populatePopupDept(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%');
			});
		}
		vm.commonSearch = {name: '', code: ''};
        vm.gridCommon = [ {
			title: "M?? xxx",
			field: "code",
			width: 120
		}, {
			title: "T??n",
			field: "name",
			width: 120
		}];
		
		vm.onSave=onSave;
		
		function onSave(data){
			vm.shipmentSearch.createdDeptName=data.text;
			vm.shipmentSearch.createdDeptId = data.code;
		}
		vm.cancel= cancel ;
		function cancel(){
				vm.shipmentGoods={};
				vm.showDetail = false;
				vm.isCreateNew = false;
		}
		vm.cancelDept = function()
		{
			vm.shipmentSearch.createdDeptName = undefined;
			vm.shipmentSearch.createdDeptId = undefined;
		}
		vm.cancelStatis = function()
		{
			vm.shipmentSearch.listStatus = ['03'];
		}
		vm.cancelCreatedBy = function()
		{
			vm.shipmentSearch.createName = undefined;
		}
		
		vm.edit = edit;
		function edit(dataItem){
			vm.showDetail1 = true;
			vm.showDetail2 = false;
			vm.shipment =dataItem;
			var teamplateUrl="qlk/shipmentPrice/shipmentPricePopup.html";
			var title="?????nh gi??";
			var windowId="SHIPMENTPRICE"
			CommonService.populatePopupCreate(teamplateUrl,title,vm.shipment,vm,windowId,false,'80%','80%');
			doSearchMap();
			DocTien(dataItem);
		}
		
		vm.nextStep = nextStep;
		function nextStep(){
			vm.showDetail1 = false;
			vm.showDetail2 = true;
		}
		
		vm.prevStep = prevStep;
		function prevStep(){
			vm.showDetail2 = false;
			vm.showDetail1 = true;
		}
		vm.doSearchMap = {};
		function doSearchMap()
		{
			vm.doSearchMap.shipmentId = vm.shipment.shipmentId;
			shipmentPriceService.doSearchMap(vm.doSearchMap).then(function(d) {
				refreshGrid(d.plain(),vm.shipmentPrice1Grid);
				refreshGrid(d.plain(),vm.shipmentPrice2Grid);
			}, function(errResponse) {
				console.error('Error T??m ki???m');
			});
		}
		
		function refreshGrid(d,grid) {
			var grid = grid;
			if(grid){
				grid.dataSource.data(d);
				grid.refresh();
			}
		}
		
		// tien
		var ChuSo=new Array(" kh??ng "," m???t "," hai "," ba "," b???n "," n??m "," s??u "," b???y "," t??m "," ch??n ");
		var Tien=new Array( "", " ngh??n", " tri???u", " t???", " ngh??n t???", " tri???u t???");

		// 1. H??m ?????c s??? c?? ba ch??? s???;
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
		}

		// 2. H??m ?????c s??? th??nh ch??? (S??? d???ng h??m ?????c s??? c?? ba ch??? s???)

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
		        // SoTien = 0;
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
		       if ((i > 0) && (tmp.length > 0)) KetQua += ',';// &&
																// (!string.IsNullOrEmpty(tmp))
		    }
		   if (KetQua.substring(KetQua.length - 1) == ',')
		   {
		        KetQua = KetQua.substring(0, KetQua.length - 1);
		   }
		   KetQua = KetQua.substring(1,2).toUpperCase()+ KetQua.substring(2);
		   return KetQua;// .substring(0, 1);//.toUpperCase();// +
							// KetQua.substring(1);
		}
		
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
		}
		
		vm.showHideColumn=function(column){
        	if (angular.isUndefined(column.hidden)) {
        		vm.shipmentPriceGrid.hideColumn(column);
            } else if (column.hidden) {
            	vm.shipmentPriceGrid.showColumn(column);
            } else {
            	vm.shipmentPriceGrid.hideColumn(column);
            }
        	
        	
        }
		/*
		** Filter c??c c???t c???a select 
		*/	
	
		vm.gridColumnShowHideFilter = function (item) { 
                return item.type==null||item.type !=1; 
            };
		// /END
	}
})();