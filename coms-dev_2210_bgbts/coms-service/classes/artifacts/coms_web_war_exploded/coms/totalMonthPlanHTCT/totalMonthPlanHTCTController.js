(function() {
	'use strict';
	var controllerId = 'totalMonthPlanHTCTController';
	
	angular.module('MetronicApp').controller(controllerId, totalMonthPlanHTCTController);
	
	function totalMonthPlanHTCTController($scope,$templateCache, $rootScope, $timeout, gettextCatalog, 
			kendoConfig, $kWindow,totalMonthPlanHTCTService,
			CommonService,htmlCommonService, PopupConst, Restangular, RestEndpoint, Constant) {
		var vm = this;
		
		vm.showSearch = true;
		vm.isCreateNew = false;
        vm.showDetail = false;
        vm.viewDetail = false;
        vm.disableBtnExcel = false;
        vm.disableBtnPDF = false;
        
		vm.totalMonthPlanHTCTSearch={
			status:'1',
		};
		vm.isLive = false;
		vm.folder='';
		vm.totalMonthPlanHTCT={};
		
		vm.selectedProvince=false;
		vm.selectedUser=false;
		// Khoi tao du lieu cho form
		vm.checkCreateMonthPlan = false; // Duonghv13 add 17092021
		
		initPopup();
		function initPopup(){
	        vm.totalMonthPlanHTCT.monthYear = new Date();
			$scope.monthSelectorOptions = {
				dateInput : true,
				format : "MM/yyyy",
				parseFormat : "MM/yyyy",
				start : "year",
				depth : "year",
				date : true,
				
			};
			vm.totalMonthPlanHTCTSearch.monthYear = new Date();
			var strMonthYear = kendo.toString(vm.totalMonthPlanHTCTSearch.monthYear, "MM/yyyy");
			vm.totalMonthPlanHTCTSearch.monthYear = strMonthYear;
			// Duonghv13-start 17092021
			totalMonthPlanHTCTService.checkRoleCreateMonthPlan().then(function(d){
            	if(d && d == 'OK'){
            		vm.checkCreateMonthPlan = true;
            	} else{
            		vm.checkCreateMonthPlan = false;
            	}
            }).catch(function () {
                vm.checkCreateMonthPlan = false;
            });
            // Duong-end
		 }
		
		initFormData();
		
		
		
		
		function initFormData() {
			fillDataTable([]);
		}
		     		
		if($rootScope.stringtile){
			vm.String=$rootScope.stringtile;
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
		function fillDataTable(data) {
				totalMonthPlanHTCTService.checkRoleCreateMonthPlan().then(function(d){
	            	if(d && d == 'OK'){
	            		vm.checkCreateMonthPlan = true;
	            	} else{
	            		vm.checkCreateMonthPlan = false;
	            	}
	            }).catch(function () {
	                vm.checkCreateMonthPlan = false;
	            });
                vm.gridOptions = kendoConfig.getGridOptions({
				autoBind: true,
				scrollable: true, 
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
                                	  
                                      '<button class="btn btn-qlk padding-search-right addQLK" ng-show ="vm.checkCreateMonthPlan==true" '+
                                      'ng-click="vm.add()" uib-tooltip="T???o m???i" translate>T???o m???i</button>'+
                                      '<button class="btn btn-qlk padding-search-right TkQLK" ng-show ="vm.checkCreateMonthPlan==true" '+
                                      'ng-click="vm.import()" uib-tooltip="Nh???p file excel" translate>Import</button>'+
                                      
                                      '</div>'	
                      				+
                                	  '<div class="btn-group pull-right margin_top_button margin_right10">'+
                                	  '<span>????n v??? ch??? ti??u: Tr???m</span>'+
    		                      	'<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">C??i ?????t</span><i class="fa fa-cog" aria-hidden="true"></i></i>'+
    		                      '<i class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()" aria-hidden="true"><span class="tooltipArrow"></span><span class="tooltiptext">Xu???t Excel</span></i>'+
    		                     
    		                      '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
    		                      '<label ng-repeat="column in vm.totalMonthPlanHTCTGrid.columns| filter: vm.gridColumnShowHideFilter">'+
    		                      '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+
    		                    '</label>'+
    		                    '</div></div>'
                              }
                ],
				dataSource:{
					serverPaging: true,
					 schema: {
						 total: function (response) {
							    $("#reportCount").text(""+response.total);
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
							url: Constant.BASE_SERVICE_URL + RestEndpoint.TOTAL_MONTH_PLAN_RENTAL_INFRASTRUCTURE_SERVICE_URL+ "/doSearch",
							contentType: "application/json; charset=utf-8",
							type: "POST"
						},					
						parameterMap: function (options, type) {
                           
								vm.totalMonthPlanHTCTSearch.page = options.page;
								vm.totalMonthPlanHTCTSearch.pageSize = options.pageSize;
								var obj = {};
								obj = angular.copy(vm.totalMonthPlanHTCTSearch);
								return JSON.stringify(obj);
								
						}
					},					 
					pageSize: 10
				},

				noRecords: true,
				columnMenu: false,
				messages: {
					noRecords : gettextCatalog.getString("<div style='margin:5px'>Kh??ng c?? k???t qu??? hi???n th???</div>")
				},
				pageable: {
					refresh: false,
					 pageSizes: [10, 15, 20, 25],
					messages: {
		                display: "{0}-{1} c???a {2} k???t qu???",
		                itemsPerPage: "k???t qu???/trang",
		                empty: "<div style='margin:5px'>Kh??ng c?? k???t qu??? hi???n th???</div>"
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
					title: "T??n t???nh",
					field: 'provinceCode',
					width: '15%',
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},
				{
					title: "K??? giao",
			        field: 'monthYear',
			        width: '12%',
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},
				{
					title: "M?? tr???m VCC",
			        field: 'stationCodeVCC',
			        width: '20%',
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},
				{
					title: "M?? tr???m VTNET",
			        field: 'stationCodeVTNET',
			        width: '20%',
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
                    footerTemplate: function(item) {
                        return kendo.toString("T???ng ch??? ti??u", "");
                    },
				}, 
				{
					title: "Ch??? ti??u th??ng",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    columns: [
                    	{
							title: "T???ng thu?? m???t b???ng(tr???m)",
					        field: 'soluong_TMB',
					        width: '15%',
					        headerAttributes: {
								style: "text-align:center;overflow: visible;white-space: normal;"
							},
							attributes: {
								style: "text-align:right;"
							},
							type :'number',
		//                  format: '{0:n3}',
							template : function(dataItem){
								return kendo.toString(parseInt(dataItem.soluong_TMB).toLocaleString(), "");
							},
							footerTemplate: function(item) {
                                var data = vm.totalMonthPlanHTCTGrid.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =item.sumsoluong_TMB;
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						},{
							title: "T???ng thi???t k??? d??? to??n(tr???m)",
					        field: 'soluong_TKDT',
					        width: '15%',
					        headerAttributes: {
								style: "text-align:center;overflow: visible;white-space: normal;"
							},
							attributes: {
								style: "text-align:right;"
							},
							type :'number',
		//                  format: '{0:n3}',
							template : function(dataItem){
								return kendo.toString(parseInt(dataItem.soluong_TKDT).toLocaleString(), "");
							},
							footerTemplate: function(item) {
                                var data = vm.totalMonthPlanHTCTGrid.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =item.sumsoluong_TKDT;
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						},{
							title: "T???ng kh???i c??ng(tr???m)",
					        field: 'soluong_KC',
					        width: '15%',
					        headerAttributes: {
								style: "text-align:center;overflow: visible;white-space: normal;"
							},
							attributes: {
								style: "text-align:right;"
							},
							type :'number',
		//                  format: '{0:n3}',
							template : function(dataItem){
								return kendo.toString(parseInt(dataItem.soluong_KC).toLocaleString(), "");
							},
							footerTemplate: function(item) {
                                var data = vm.totalMonthPlanHTCTGrid.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =item.sumsoluong_KC;
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						},{
							title: "T???ng ?????ng b??? h??? t???ng(tr???m)",
					        field: 'soluong_DB',
							width: '15%',
					        headerAttributes: {
								style: "text-align:center;overflow: visible;white-space: normal;"
							},
							attributes: {
								style: "text-align:right;"
							},
							type :'number',
		//                  format: '{0:n3}',
							template : function(dataItem){
								return kendo.toString(parseInt(dataItem.soluong_DB).toLocaleString(), "");
							},
							footerTemplate: function(item) {
                                var data = vm.totalMonthPlanHTCTGrid.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =item.sumsoluong_DB;
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						},{
							title: "T???ng ph??t s??ng(tr???m)",
					        field: 'soluong_PS',
					        width: '15%',
					        headerAttributes: {
								style: "text-align:center;overflow: visible;white-space: normal;"
							},
							attributes: {
								style: "text-align:right;"
							},
							type :'number',
		//                  format: '{0:n3}',
							template : function(dataItem){
								return kendo.toString(parseInt(dataItem.soluong_PS).toLocaleString(), "");
							},
							footerTemplate: function(item) {
                                var data = vm.totalMonthPlanHTCTGrid.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =item.sumsoluong_PS;
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
							
						},{
							title: "T???ng h??? s?? ho??n c??ng v??? TTHT(tr???m)",
					        field: 'soluong_HSHC',
					        width: '15%',
					        headerAttributes: {
								style: "text-align:center;overflow: visible;white-space: normal;"
							},
							attributes: {
								style: "text-align:right;"
							},
							type :'number',
		//                  format: '{0:n3}',
							template : function(dataItem){
								return kendo.toString(parseInt(dataItem.soluong_HSHC).toLocaleString(), "");
							},
							footerTemplate: function(item) {
                                var data = vm.totalMonthPlanHTCTGrid.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =item.sumsoluong_HSHC;
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						},{
							title: "T???ng h??? s?? ho??n c??ng v??? DTHT(tr???m)",
					        field: 'soluong_HSHC_DTHT',
					        width: '15%',
					        headerAttributes: {
								style: "text-align:center;overflow: visible;white-space: normal;"
							},
							attributes: {
								style: "text-align:right;"
							},
							type :'number',
		//                  format: '{0:n3}',
							template : function(dataItem){
								return kendo.toString(parseInt(dataItem.soluong_HSHC_DTHT).toLocaleString(), "");
							},
							footerTemplate: function(item) {
                                var data = vm.totalMonthPlanHTCTGrid.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =item.sumsoluong_HSHC_DTHT;
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						}
						
						,{
							title: "Tr???m l??n doanh thu(tr???m)",
					        field: 'tram_toDoanhThu',
					        width: '15%',
					        headerAttributes: {
								style: "text-align:center;overflow: visible;white-space: normal;"
							},
							attributes: {
								style: "text-align:right;"
							},
							type :'number',
		//                  format: '{0:n3}',
							template : function(dataItem){
								return kendo.toString(parseInt(dataItem.tram_toDoanhThu).toLocaleString(), "");
							},
							footerTemplate: function(item) {
                                var data = vm.totalMonthPlanHTCTGrid.dataSource.data();
                                var sum = 0;
                                for (var idx = 0; idx < data.length; idx++) {
                                    if (idx == 0) {
                                        item = data[idx];
                                        sum =item.sumtram_toDoanhThu;
                                        break;
                                    }
                                }
                                return kendo.toString(sum, "");
                            },
						 },
					]
                },
                {
					title: "Ng??y t???o",
			        field: 'insertTime',
			        width: '18%',
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},
				{
					title: "Ng?????i t???o",
			        field: 'createdByName',
			        width: '15%',
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
					
					template : function(dataItem){
						if(dataItem.createdByName!= null){
							var createUser = dataItem.createdByName;
							var positionbreak = createUser.indexOf('@');
							dataItem.createdByName = createUser.slice(0, positionbreak);
							return kendo.toString((dataItem.createdByName).toLocaleString(), "");
						}else{
							dataItem.createdByName="";
							return kendo.toString((dataItem.createdByName).toLocaleString(), "");
						}
					},
					
				},
				{
					title: "Ng??y c???p nh???t",
			        field: 'updatedTime',
			        width: '18%',
			        headerAttributes: {
						style: "text-align:center;overflow: visible;white-space: normal;"
					},
					attributes: {
						style: "text-align:left;"
					},
				},
				{
					title: "Ng?????i c???p nh???t",
			        field: 'updatedByName',
			        width: '23%',
			        headerAttributes: {
						style: "text-align:center;"
					},
					attributes: {
						style: "text-align:center;"
					},
					template : function(dataItem){
						if(dataItem.updatedByName!= null){
							var updateUser = dataItem.updatedByName;
							var positionbreak = updateUser.indexOf('@');
							dataItem.updatedByName = updateUser.slice(0, positionbreak);
							return kendo.toString((dataItem.updatedByName).toLocaleString(), "");
						}else{
							dataItem.updatedByName="";
							return kendo.toString((dataItem.updatedByName).toLocaleString(), "");
						}
						
					},
					
				},{
					title: "Thao t??c",
					template: dataItem =>
					'<div class="text-center""><button style=" border: none; background-color: white;" '+
					' class=" icon_table " ng-show ="vm.checkCreateMonthPlan==true"'+
					'   uib-tooltip="S???a" " ng-click="vm.edit(dataItem)" translate>'+
					'<i class="fa fa-pencil" aria-hidden="true"></i>'+
				'</button>'+
				'<button style=" border: none; background-color: white;"'+
				'class="#=appParamId# icon_table" ng-show ="vm.checkCreateMonthPlan==true" ng-click="vm.remove(dataItem)"  uib-tooltip="X??a" translate'+
					'>'+
					'<i class="fa fa-trash" style="color: #337ab7;" aria-hidden="true"></i>'+
				'</button>'+
				'<button style=" border: none; background-color: white;" '+
				'class=" icon_table"'+
				'  uib-tooltip= "Xem chi ti???t" ng-click="vm.view(dataItem)" ng-show="vm.checkCreateMonthPlan==false"  translate>'+
				'<i class="fa fa-list-alt" style="color:#e0d014;" aria-hidden="true"></i>'+
				'</button>'+
				'</div>',
			      width: '15%',
			      field:"action"
				}
			]
			});
            
		}
		
		vm.listRemove=[{
			title: "Thao t??c",
		}]
		
		
		vm.listConvert=[{
			field:"status",
			data:{
				'1':'???? duy???t',
				'0':'???? h???y'
			}
		}]
		
		
		vm.exportFile = function () {
			if (vm.disableBtnPDF)
	    		return;
	    	vm.disableBtnExcel = true;
	    	$("#loadingReportPlan").addClass('loadersmall');
			return Restangular.all("totalMonthPlanHTCTRsService/monthReport/exportMonthPlan").post(vm.totalMonthPlanHTCTSearch).then(function (d) {
        	                var data = d.plain();
        	                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	                vm.disableBtnExcel = false;
        	                $("#loadingReportPlan").removeClass('loadersmall');
        	}).catch(function (e) {
        	            	vm.disableBtnExcel = false;
        	                toastr.error(gettextCatalog.getString("L???i khi t???i xu???ng file Excel !"));
        	                $("#loadingReportPlan").removeClass('loadersmall');
            });
        }
		
		
		vm.add = function add(){
			vm.isCreateNew = true;
			vm.showDetail = false;
			vm.totalMonthPlanHTCT ={};
			initPopup();
			var teamplateUrl="coms/totalMonthPlanHTCT/totalMonthPlanHTCTPopup.html";
			var title="Th??m m???i k??? ho???ch th??ng";
			var windowId="COMS_TOTAL_MONTH_PLAN";
			CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'900','380',"code"); 
			 
		 }

		vm.edit=edit;
		function edit(dataItem){
				vm.showDetail = true;
				vm.isCreateNew = false;
				vm.totalMonthPlanHTCT =dataItem;
				var teamplateUrl="coms/totalMonthPlanHTCT/totalMonthPlanHTCTPopup.html";
				var title="C???p nh???t k??? ho???ch th??ng";
				var windowId="COMS_TOTAL_MONTH_PLAN";
				CommonService.populatePopupCreate(teamplateUrl,title,vm.totalMonthPlanHTCT,vm,windowId,false,'900','380',"code"); 
				doSearch();
		}
		
		vm.view=view;
		function view(dataItem){
				vm.showDetail = false;
				vm.isCreateNew = false;
				vm.viewDetail = true;
				vm.totalMonthPlanHTCT =dataItem;
				var teamplateUrl="coms/totalMonthPlanHTCT/totalMonthPlanHTCTPopup.html";
				var title="Chi ti???t k??? ho???ch th??ng";
				var windowId="COMS_TOTAL_MONTH_PLAN";
				CommonService.populatePopupCreate(teamplateUrl,title,vm.totalMonthPlanHTCT,vm,windowId,false,'900','350',"code"); 
				doSearch();
		}
		
		vm.remove=remove;
		function remove(dataItem){
			confirm('X??c nh???n x??a',function(){
				totalMonthPlanHTCTService.remove(dataItem).then(
						function(d) {
							var sizePage = $("#totalMonthPlanHTCTGrid").data("kendoGrid").dataSource.total();
							var pageSize = $("#totalMonthPlanHTCTGrid").data("kendoGrid").dataSource.pageSize();
							
							if(sizePage % pageSize === 1){
								var currentPage = $("#totalMonthPlanHTCTGrid").data("kendoGrid").dataSource.page();
								if (currentPage > 1) {
									$("#totalMonthPlanHTCTGrid").data("kendoGrid").dataSource.page(currentPage - 1);
								}
							}
							 $("#totalMonthPlanHTCTGrid").data('kendoGrid').dataSource.read();
							 $("#totalMonthPlanHTCTGrid").data('kendoGrid').refresh();
							 toastr.success("X??a th??nh c??ng!");
							
						}, function(errResponse) {
							toastr.error("L???i kh??ng x??a ???????c!");
						});
				})
		}
		
		vm.save= function save(data,isCreateNew){
			// chua validate cac truong
				var msg = " kh??ng ???????c ????? tr???ng !";
				if(vm.totalMonthPlanHTCT.provinceCode == null ){
					toastr.warning("L???i sai ?????nh d???ng ho???c b??? tr???ng m?? t???nh");
					$("#provinceCode").focus();
					return;
				}
				var regex1 = /(0[1-9]|1[0-2])\/(\d{4}[^0-9])/g;
				var strMonthYear = kendo.toString(vm.totalMonthPlanHTCT.monthYear);
				
				
				if(vm.totalMonthPlanHTCT.monthYear == null ){
					toastr.warning("L???i sai ?????nh d???ng ho???c b??? tr???ng th??ng tin");
					$("#monthYear1").val('');
					return;
					
				} else {
					if(kendo.parseDate(vm.totalMonthPlanHTCT.monthYear, "MM/yyyy")== null){
	    				toastr.warning("Th???i gian kh??ng ????ng ?????nh d???ng!");
	    				$("#monthYear1").focus();
						return;
	    			} else if (kendo.parseDate(vm.totalMonthPlanHTCT.monthYear, "MM/yyyy").getFullYear() > 9999 || kendo.parseDate(vm.totalMonthPlanHTCT.monthYear, "MM/yyyy").getFullYear() < 1000) {
	    				toastr.warning("Th???i gian kh??ng h???p l???!");
	    				$("#monthYear1").focus();
						return;
	    			}
	    			
		    	}
				var strMonthYear = kendo.toString(vm.totalMonthPlanHTCT.monthYear, "MM/yyyy");
				vm.totalMonthPlanHTCT.monthYear = strMonthYear;
				
				if(vm.totalMonthPlanHTCT.stationCodeVCC == null ){
					toastr.warning("L???i sai ?????nh d???ng ho???c b??? tr???ng m?? tr???m VCC.");
					$("#stationCodeVCC").focus();
					return;
				}
				
				if(vm.totalMonthPlanHTCT.stationCodeVTNET == null ){
					toastr.warning("L???i b??? tr???ng m?? tr???m VTNET.");
					$("#stationCodeVTNET").focus();
					return;
				}
				
				var regex = /^[01]$/;
				if($("#soluong_TMB").val().trim()!="" && !regex.test(vm.totalMonthPlanHTCT.soluong_TMB) ){
					toastr.warning(gettextCatalog.getString("Sai ?????nh d???ng.Vui l??ng nh???p 0 ho???c 1.Tr?????ng h???p ????? tr???ng m???c ?????nh l?? 0."));
					$("#soluong_TMB").focus();
					return;
				}
				

				if($("#soluong_TKDT").val().trim()!="" && !regex.test(vm.totalMonthPlanHTCT.soluong_TKDT) ){
					toastr.warning(gettextCatalog.getString("Sai ?????nh d???ng.Vui l??ng nh???p 0 ho???c 1.Tr?????ng h???p ????? tr???ng m???c ?????nh l?? 0."));
					$("#soluong_TKDT").focus();
					return;
				}
				
				
				if($("#soluong_KC").val().trim()!="" && !regex.test(vm.totalMonthPlanHTCT.soluong_KC) ){
					toastr.warning(gettextCatalog.getString("Sai ?????nh d???ng.Vui l??ng nh???p 0 ho???c 1.Tr?????ng h???p ????? tr???ng m???c ?????nh l?? 0."));
					$("#soluong_KC").focus();
					return;
				}
				
				if($("#soluong_DB").val().trim()!="" && !regex.test(vm.totalMonthPlanHTCT.soluong_DB) ){
					toastr.warning(gettextCatalog.getString("Sai ?????nh d???ng.Vui l??ng nh???p 0 ho???c 1.Tr?????ng h???p ????? tr???ng m???c ?????nh l?? 0."));
					$("#soluong_DB").focus();
					return;
				}
				
				if($("#soluong_PS").val().trim()!="" && !regex.test(vm.totalMonthPlanHTCT.soluong_PS) ){
					toastr.warning(gettextCatalog.getString("Sai ?????nh d???ng.Vui l??ng nh???p 0 ho???c 1.Tr?????ng h???p ????? tr???ng m???c ?????nh l?? 0."));
					$("#soluong_PS").focus();
					return;
				}
				if($("#soluong_HSHC").val().trim()!="" && !regex.test(vm.totalMonthPlanHTCT.soluong_HSHC) ){
					toastr.warning(gettextCatalog.getString("Sai ?????nh d???ng.Vui l??ng nh???p 0 ho???c 1.Tr?????ng h???p ????? tr???ng m???c ?????nh l?? 0."));
					$("#soluong_HSHC").focus();
					return;
				}
				
				if($("#soluong_HSHC_DTHT").val().trim()!="" && !regex.test(vm.totalMonthPlanHTCT.soluong_HSHC_DTHT) ){
					toastr.warning(gettextCatalog.getString("Sai ?????nh d???ng.Vui l??ng nh???p 0 ho???c 1.Tr?????ng h???p ????? tr???ng m???c ?????nh l?? 0."));
					$("#soluong_HSHC_DTHT").focus();
					return;
				}
				
				if($("#tram_toDoanhThu").val().trim()!="" && !regex.test(vm.totalMonthPlanHTCT.tram_toDoanhThu) ){
					toastr.warning(gettextCatalog.getString("Sai ?????nh d???ng.Vui l??ng nh???p 0 ho???c 1.Tr?????ng h???p ????? tr???ng m???c ?????nh l?? 0."));
					$("#tram_toDoanhThu").focus();
					return;
				}
				var obj = {};
				obj.provinceCode = vm.totalMonthPlanHTCT.provinceCode;
				obj.month = strMonthYear.slice(0,2);
				obj.year = Number(strMonthYear.slice(3,7));
				obj.stationCodeVCC = vm.totalMonthPlanHTCT.stationCodeVCC;
				obj.stationCodeVTNET = vm.totalMonthPlanHTCT.stationCodeVTNET;
				if(vm.totalMonthPlanHTCT.soluong_TMB == null) vm.totalMonthPlanHTCT.soluong_TMB = 0;
				if(vm.totalMonthPlanHTCT.soluong_KC == null) vm.totalMonthPlanHTCT.soluong_KC = 0;
				if(vm.totalMonthPlanHTCT.soluong_DB == null) vm.totalMonthPlanHTCT.soluong_DB = 0;
				if(vm.totalMonthPlanHTCT.soluong_PS == null) vm.totalMonthPlanHTCT.soluong_PS = 0;
				if(vm.totalMonthPlanHTCT.soluong_HSHC == null) vm.totalMonthPlanHTCT.soluong_HSHC = 0;
				if(vm.totalMonthPlanHTCT.soluong_HSHC_DTHT == null) vm.totalMonthPlanHTCT.soluong_HSHC_DTHT = 0;
				if(vm.totalMonthPlanHTCT.soluong_TKDT == null) vm.totalMonthPlanHTCT.soluong_TKDT = 0;
				if(vm.totalMonthPlanHTCT.tram_toDoanhThu == null) vm.totalMonthPlanHTCT.tram_toDoanhThu = 0;
				
        		obj.soluong_TMB =  Number(vm.totalMonthPlanHTCT.soluong_TMB);
				obj.soluong_KC =  Number(vm.totalMonthPlanHTCT.soluong_KC);
				obj.soluong_DB =  Number(vm.totalMonthPlanHTCT.soluong_DB);
				obj.soluong_PS=  Number(vm.totalMonthPlanHTCT.soluong_PS);
				obj.soluong_HSHC =  Number(vm.totalMonthPlanHTCT.soluong_HSHC);
				obj.soluong_HSHC_DTHT =  Number(vm.totalMonthPlanHTCT.soluong_HSHC_DTHT);
				obj.soluong_TKDT =  Number(vm.totalMonthPlanHTCT.soluong_TKDT);
				obj.tram_toDoanhThu =  Number(vm.totalMonthPlanHTCT.tram_toDoanhThu);
				
			if(vm.isCreateNew==true) {	
				totalMonthPlanHTCTService.createReport(obj).then(function(result){
        			if(result.error){
						toastr.error(result.error);
						return;
					}
        			toastr.success("Th??m m???i th??nh c??ng!");
                    doSearch();
                    document.getElementById('reason').reset();
					$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
					
        		}, function(errResponse){
	                if (errResponse.status === 409) {
	                	toastr.error(gettextCatalog.getString("???? t???n t???i k??? ho???ch giao ch??? ti??u(tr???m) trong th??ng c???a t???nh tr??n trong c?? s??? d??? li???u. !"));
	                } else {
	                	toastr.error(gettextCatalog.getString("L???i xu???t hi???n khi th??m m???i k??? ho???ch th??ng !"));
	                }
		        });
        	}else{
        		obj = {};
        		obj.totalMonthPlanDTHTId =  Number(vm.totalMonthPlanHTCT.totalMonthPlanDTHTId);
				obj.provinceCode = vm.totalMonthPlanHTCT.provinceCode;
				obj.month = strMonthYear.slice(0,2);
				obj.year = Number(strMonthYear.slice(3,7));
				obj.stationCodeVCC = vm.totalMonthPlanHTCT.stationCodeVCC;
				obj.stationCodeVTNET = vm.totalMonthPlanHTCT.stationCodeVTNET;
				if(vm.totalMonthPlanHTCT.soluong_TMB == null) vm.totalMonthPlanHTCT.soluong_TMB = 0;
				if(vm.totalMonthPlanHTCT.soluong_KC == null) vm.totalMonthPlanHTCT.soluong_KC = 0;
				if(vm.totalMonthPlanHTCT.soluong_DB == null) vm.totalMonthPlanHTCT.soluong_DB = 0;
				if(vm.totalMonthPlanHTCT.soluong_PS == null) vm.totalMonthPlanHTCT.soluong_PS = 0;
				if(vm.totalMonthPlanHTCT.soluong_HSHC == null) vm.totalMonthPlanHTCT.soluong_HSHC = 0;
				if(vm.totalMonthPlanHTCT.soluong_HSHC_DTHT == null) vm.totalMonthPlanHTCT.soluong_HSHC_DTHT = 0;
				if(vm.totalMonthPlanHTCT.soluong_TKDT == null) vm.totalMonthPlanHTCT.soluong_TKDT = 0;
				if(vm.totalMonthPlanHTCT.tram_toDoanhThu == null) vm.totalMonthPlanHTCT.tram_toDoanhThu = 0;
        		obj.soluong_TMB =  Number(vm.totalMonthPlanHTCT.soluong_TMB);
				obj.soluong_KC =  Number(vm.totalMonthPlanHTCT.soluong_KC);
				obj.soluong_DB =  Number(vm.totalMonthPlanHTCT.soluong_DB);
				obj.soluong_PS=  Number(vm.totalMonthPlanHTCT.soluong_PS);
				obj.soluong_HSHC =  Number(vm.totalMonthPlanHTCT.soluong_HSHC);
				obj.soluong_HSHC_DTHT =  Number(vm.totalMonthPlanHTCT.soluong_HSHC_DTHT);
				obj.soluong_TKDT =  Number(vm.totalMonthPlanHTCT.soluong_TKDT);
				obj.tram_toDoanhThu =  Number(vm.totalMonthPlanHTCT.tram_toDoanhThu);
        		obj.insertTime = vm.totalMonthPlanHTCT.insertTime ;
        		obj.createdBy =  Number(vm.totalMonthPlanHTCT.createdBy);
				
        		totalMonthPlanHTCTService.updateReport(obj).then(function(result){
        			if(result.error){
						toastr.error(result.error);
						return;
					}
					$("#totalMonthPlanHTCTGrid").data('kendoGrid').dataSource.read();
					$("#totalMonthPlanHTCTGrid").data('kendoGrid').refresh();
        			toastr.success("C???p nh???t th??nh c??ng!");
        			doSearch();
        			$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        		}, function(errResponse){
        			if (errResponse.status === 409) {
	                	toastr.error(gettextCatalog.getString("???? t???n t???i k??? ho???ch giao ch??? ti??u(tr???m) trong th??ng c???a t???nh tr??n trong c?? s??? d??? li???u. !"));
	                	doSearch();
	                } else {
	                	toastr.error(gettextCatalog.getString("X???y ra l???i khi c???p nh???t"));
	                }
		        });
        	}
	}
		
		function refreshGrid(d) {
			var grid = vm.totalMonthPlanHTCTGrid;
			if(grid){
				grid.dataSource.data(d);
				grid.refresh();
			}
		}
		    
		
		
		vm.cancel= cancel ;
		function cancel(){
		/* confirm('B???n c?? mu???n h???y b??? thao t??c?', function(){ */
				$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
				/* }); */
		}
			
		vm.canceldoSearch= function (){
			 vm.showDetail = false;
			 vm.totalMonthPlanHTCTSearch={
				status:"1",
				
			};
			doSearch();
		}
		
		vm.doSearch= doSearch;
		function doSearch(){
			vm.showDetail = false;
			if(data.monthYear != null && data.monthYear != ""){
    			if(kendo.parseDate(data.monthYear, "MM/yyyy")== null){
    				toastr.warning("Th???i gian kh??ng ????ng ?????nh d???ng!");
					return;
    			} else if (kendo.parseDate(data.monthYear, "MM/yyyy").getFullYear() > 9999 || kendo.parseDate(data.monthYear, "dd/MM/yyyy").getFullYear() < 1000) {
    				toastr.warning("Th???i gian kh??ng h???p l???!");
					return;
    			}
    			else if(data.monthYear != ""){
					toastr.warning("Th???i gian kh??ng ????ng ?????nh d???ng!");
					return;
    			}
    		}
			var grid =vm.totalMonthPlanHTCTGrid;	
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
        		vm.totalMonthPlanHTCTGrid.hideColumn(column);
            } else if (column.hidden) {
            	vm.totalMonthPlanHTCTGrid.showColumn(column);
            } else {
            	vm.totalMonthPlanHTCTGrid.hideColumn(column);
            }
        	
        	
        }
		/*
		 * * Filter c??c c???t c???a select
		 */	
	
		vm.gridColumnShowHideFilter = function (item) { 
                return item.type==null||item.type !==1; 
         }
		
       
         vm.exportpdf= function(){
        	 if (vm.disableBtnExcel)
         		return;
         	vm.disableBtnPDF = true;
         	var obj = {};
			obj = angular.copy(vm.totalMonthPlanHTCTSearch);
			
         	$("#loadingReportPlan").addClass('loadersmall');
         	return totalMonthPlanHTCTService.exportPDFReport(obj).then(function (d) {
                 var data = d.plain();
                 window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                 vm.disableBtnPDF = false;
                 $("#loadingReportPlan").removeClass('loadersmall');
             }).catch(function (e) {
                 toastr.error(gettextCatalog.getString("L???i khi t???i file PDF!"));
                 vm.disableBtnPDF = false;
                 $("#loadingReportPlan").removeClass('loadersmall');
             });;
         }
         vm.import=function(){
         	 vm.totalMonthPlanHTCT={};
 			 var teamplateUrl="coms/totalMonthPlanHTCT/importTotalMonthPlanHTCTPopup.html";
 			 var title="Import danh s??ch k??? ho???ch th??ng";
 			 var windowId="COMS_TOTAL_MONTH_PLAN";
 			 htmlCommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','200'); 
         }
         
         vm.getExcelTemplate = function getExcelTemplate(){
				kendo.ui.progress($(".tab-content"), true);
	        	return Restangular.all("totalMonthPlanHTCTRsService/monthReport/exportExcelTemplate").post().then(function (d) {
	        	    var data = d.plain();
	        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
	        	    kendo.ui.progress($(".tab-content"), false);
	        	}).catch(function (e) {
	        		kendo.ui.progress($(".tab-content"), false);
	        	    toastr.error(gettextCatalog.getString("L???i khi t???i bi???u m???u g???c!"));
	        	    return;
	        	});
  		
         }
         
         vm.submit=submit;
         function submit(data){
         	console.log(data);
         	if(!$("#filePackage")[0].files[0]){
 	    		toastr.warning("B???n ch??a ch???n file ????? import");
 	    		return;
 	    	}
         	console.log("2");
 			if($('.k-invalid-msg').is(':visible')){
 				return;
 			}
 			console.log("3");
 			if($("#filePackage")[0].files[0].name.split('.').pop() !=='xls' && $("#filePackage")[0].files[0].name.split('.').pop() !=='xlsx' ){
 				toastr.warning("Sai ?????nh d???ng file");
 				return;
 			}
     		 var formData = new FormData();
 				formData.append('multipartFile', $('#filePackage')[0].files[0]); 
 		     return   $.ajax({
 	            url: Constant.BASE_SERVICE_URL+RestEndpoint.TOTAL_MONTH_PLAN_RENTAL_INFRASTRUCTURE_SERVICE_URL+"/importReport?folder="+ vm.folder,
 	            type: "POST",
 	            data: formData,
 	            enctype: 'multipart/form-data',
 	            
 	            processData: false,
 	            contentType: false,
 	            cache: false,
 	            success:function(data) {
 	            	if(data == 'NO_CONTENT') {
		            	toastr.warning("File import kh??ng c?? n???i dung");
			    		return;
		            }
 	            	if(!!data.error){
 	            		toastr.error(data.error);
 	            		return;
 	            	}
 	            	if(data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length>0){
 	            		
 	            		vm.lstErrImport = data[data.length - 1].errorList;
 	            		vm.objectErr = data[data.length - 1];
 	            		var teamplateUrl="wms/createImportRequirementManagement/importResultPopUp.html";
 	       			 	var title="K???t qu??? Import";
 	       			 	var windowId="ERR_IMPORT";
 	       			 	CommonService.populatePopupCreate(teamplateUrl,title,vm.lstErrImport,vm,windowId,false,'90%','50%');
 	       			 	fillDataImportErrTable(vm.lstErrImport);
 	       			 	
 	            	}
 	            	else{
 	            		toastr.success("Import th??nh c??ng") 
 	            		$("#totalMonthPlanHTCTGrid").data('kendoGrid').dataSource.read();
 						$("#totalMonthPlanHTCTGrid").data('kendoGrid').refresh();
 						htmlCommonService.dismissPopup();
 	            	}
 						  
 	            }
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
  					noRecords : gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
  				},
  				pageSize:10,
  				pageable: {
  					pageSize:10,
  					refresh: false,
  					 pageSizes: [10, 15, 20, 25],
  					messages: {
  		                display: "{0}-{1} c???a {2} k???t qu???",
  		                itemsPerPage: "k???t qu???/trang",
  		                empty: "Kh??ng c?? k???t qu??? hi???n th???"
  		            }
  				},
  				columns: [
  				{
  					title: "STT",
  					field:"stt",
  			        template: dataItem => $("#importGoodResultGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
  			        width: 70,
  			        headerAttributes: {
  						style: "text-align:center;"
  					},
  					attributes: {
  						style: "text-align:center;"
  					},
  				}
  				,  {
  					title: "D??ng l???i",
  					field: 'lineError',
  			        width: 150,
  			        headerAttributes: {
  						style: "text-align:center;"
  					},
  					attributes: {
  						style: "text-align:right;"
  					},
  				}, {
  					title: "C???t l???i",
  			        field: 'columnError',
  			        width: 150,
  			        headerAttributes: {
  						style: "text-align:center;"
  					},
  					attributes: {
  						style: "text-align:right;"
  					},
  				}, {
  					title: "N???i dung l???i",
  			        field: 'detailError',
  			        width: 150,
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
         
         vm.exportExcelErr = function(){
        	 totalMonthPlanHTCTService.downloadErrorExcel(vm.objectErr).then(function(d) {
  				data = d.plain();
					window.location = Constant.BASE_SERVICE_URL+"totalMonthPlanHTCTRsService/monthReport/downloadFileTempATTT?" + data.fileName;
				}).catch( function(){
					toastr.error(gettextCatalog.getString("L???i khi export!"));
					return;
				});
 		}
         
        vm.closeErrImportPopUp= closeErrImportPopUp ;
  		function closeErrImportPopUp(){
  		/* confirm('B???n c?? mu???n h???y b??? thao t??c?', function(){ */
  				$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
  				/* }); */
  		}
         
         vm.cancelImport = cancelImport;
         function cancelImport() {
        	 htmlCommonService.dismissPopup();
         }
         
         vm.openForm = function(){
 			vm.isLive=true;
// alert("open"+vm.isLive);
 		}
         vm.closeForm = function(){
 			vm.isLive=false;
 			vm.viewDetail =false;
 			vm.showDetail = false;
			vm.isCreateNew = false;
 			
 		}
         
        vm.provinceValueOptions
  		vm.selectedProvince = false;
        vm.provinceValueOptions= {
        	dataTextField: "name",
    		dataValueField:"id",
 			placeholder: "Nh???p m?? ho???c t??n t???nh",
 			select: function (e) {
 				vm.selectedProvince = true;
 				var dataItem = this.dataItem(e.item.index());
 				vm.totalMonthPlanHTCTSearch.provinceName = dataItem.name;
 				vm.totalMonthPlanHTCTSearch.provinceCode = dataItem.code;

 			},
 			
 			pageSize: 10,
 			open: function (e) {
 				vm.selectedProvince = false;
 			},
 			dataSource: {
 				serverFiltering: true,
 				transport: {
 					read: function (options) {
 						vm.selectedProvince = false;
 						return Restangular.all("catProvinceServiceRest/autoCompleteSearch")
 							.post({ name:vm.totalMonthPlanHTCTSearch.provinceName,
 								pageSize: vm.provinceValueOptions.pageSize,
 								page:1
 							})
 							.then(function (response) {
 								options.success(response.data);
 							}).catch(function (err) {
 								console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
 							});
 					}
 				}
 			},
 			headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
             '<p class="col-md-6 text-header-auto border-right-ccc">M?? t???nh</p>' +
             '<p class="col-md-6 text-header-auto">T??n </p>' +
             	'</div>',
            template:'<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
 			change: function(e) {
	                if (e.sender.value() === '') {
	                	vm.totalMonthPlanHTCTSearch.provinceName = null;
	     				vm.totalMonthPlanHTCTSearch.provinceCode = null;
	  	                }
	         },
	         ignoreCase: false,
 		};
        
        
        vm.changeDataAuto=changeDataAuto;
		function changeDataAuto(id){
			switch(id){
				case 'province1':{
					if(processSearch(id,vm.selectedProvince)){
					 vm.totalMonthPlanHTCTSearch.provinceName = null;
					 vm.totalMonthPlanHTCTSearch.provinceCode =null;
					 vm.selectedProvince=false;	
					}
					break;
				}
				case 'province2':{
					if(processSearch(id,vm.selectedProvince1)){
						 vm.totalMonthPlanHTCT.provinceCode =null;
						 vm.totalMonthPlanHTCT.provinceName = null;
						 vm.selectedProvince1=false;	
					}
					break;
				}
				case 'user':{
	        		if (processSearch(id, vm.selectedDept1)) {
	        			vm.totalMonthPlanHTCTSearch.createdByName = null;
	        			vm.totalMonthPlanHTCTSearch.createdEmail = null;
	    	            vm.selectedDept1 = false;
	        		}
	        	}
				
				case 'station':{
	        		if (processSearch(id, vm.selectedStation)) {
	        			vm.totalMonthPlanHTCTSearch.stationCodeVCC = null;
		 				vm.totalMonthPlanHTCTSearch.stationName = null;
	    	            vm.selectedStation = false;
	        		}
	        	}
				
				case 'station2':{
	        		if (processSearch(id, vm.selectedStation1)) {
	        			vm.totalMonthPlanHTCT.stationCodeVCC = null;
		 				vm.totalMonthPlanHTCT.stationName = null;
	    	            vm.selectedStation1 = false;
	        		}
	        	}
				 
			}
		}
		
		vm.cancelProvinces = function(){
			vm.totalMonthPlanHTCTSearch.provinceCode = null;
			vm.totalMonthPlanHTCTSearch.provinceName = null;
		}
		
		vm.selectedDept1 = false;

		vm.patternSignerOptions={
						dataTextField: "email", 
						placeholder:"Nh???p m?? ho???c t??n ng?????i th???c hi???n",
						open: function(e) {
							vm.isSelect = false;
							vm.selectedDept1 = false;
						},
			            select: function(e) {
			            	vm.isSelect = true;
			            	vm.selectedDept1 = true;
			            	data = this.dataItem(e.item.index());
			            	vm.totalMonthPlanHTCTSearch.createdByName = data.fullName;
			            	vm.totalMonthPlanHTCTSearch.createdEmail = data.email;
			            },
			            pageSize: 10,
			            dataSource: {
			                serverFiltering: true,
			                transport: {
			                    read: function(options) {
			                    	vm.selectedDept1 = false;
			                        return Restangular.all("sysUserCOMSRsService/getForAutoCompleteDetailInSign").post({pageSize:10, page:1, fullName:$("#createdByName").val().trim()}).then(function(response){
			                            options.success(response);
			                            if(response == [] || $("#createdByName").val().trim() == ""){
			                            	vm.totalMonthPlanHTCTSearch.createdByName = null;
			                            	vm.totalMonthPlanHTCTSearch.createdEmail = null;
			                            }
			                        }).catch(function (err) {
			                            console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
			                        });
			                    }
			                }
			            },
			            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
			            '<p class="col-md-6 text-header-auto border-right-ccc">T??n nh??n vi??n</p>' +
			            '<p class="col-md-6 text-header-auto">Email</p>' +
			            	'</div>',
			            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.fullName #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.email #</div> </div>',
			          change: function(e) {
			        	  if (e.sender.value() === '') {
			        		  vm.totalMonthPlanHTCTSearch.createdByName = null;
                          	  vm.totalMonthPlanHTCTSearch.createdEmail = null;
			              }
			        	},
			            close: function(e) { 
			              }
					};

		
//		vm.patternSignerOptions={
//				dataTextField: "createdByName", 
//				placeholder:"Nh???p t??n/email nh??n vi??n",
//				
//				open: function(e) {
//					vm.isSelect = false;
//					vm.selectedUser1 = false;
//				},
//	            select: function(e) {
//	            	vm.isSelect = true;
//	            	vm.selectedUser1 = true;
//	            	data = this.dataItem(e.item.index());
//	            	vm.totalMonthPlanHTCTSearch.createdByName =data.fullName;
//	            	vm.totalMonthPlanHTCTSearch.createdEmail = data.email;
//	            },
//	            pageSize: 10,
//	            dataSource: {
//	                serverFiltering: true,
//	                transport: {
//	                    read: function(options) {
//	                    	vm.selectedUser1 = false;
//	                        return Restangular.all("sysUserCOMSRsService/getForAutoCompleteInSign").post({pageSize:10, page:1, fullName:$("#createdByName").val().trim()}).then(function(response){
//	                            options.success(response);
//	                            if(response == [] || $("#createdByName").val().trim() == ""){
//	                            	vm.totalMonthPlanHTCTSearch.createdByName = null;
//	                            	vm.totalMonthPlanHTCTSearch.createdEmail = null;
//	                            }
//	                        }).catch(function (err) {
//	                            console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
//	                        });
//	                    }
//	                }
//	            },
//	            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
//	            '<p class="col-md-6 text-header-auto border-right-ccc">Email</p>' +
//	            '<p class="col-md-6 text-header-auto">H??? t??n</p>' +
//	            	'</div>',
//	            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.email #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
//	          change: function(e) {
//	        	  if (e.sender.value() === '') {
//	        		  vm.totalMonthPlanHTCTSearch.createdByName = null;
//                  	  vm.totalMonthPlanHTCTSearch.createdEmail = null;
//	              }
//	        	},
//	            close: function(e) { 
//	              }
//	    };
		
		vm.cancelInput = function (param) {
			if (param == 'createdByName'){
				vm.totalMonthPlanHTCTSearch.createdByName = null;
			}
			if (param == 'monthYear'){
				vm.totalMonthPlanHTCTSearch.monthYear = null;
				vm.totalMonthPlanHTCT.monthYear= null;
				$("#monthYear1").val('');
				
				
			}
			if (param == 'province2'){
				vm.totalMonthPlanHTCT.provinceCode = null;
				vm.totalMonthPlanHTCT.provinceName = null;
			}
			if (param == 'station'){
				vm.totalMonthPlanHTCTSearch.stationCodeVCC = null;
 				vm.totalMonthPlanHTCTSearch.stationName = null;
			}
			
			if (param == 'station2'){
				vm.totalMonthPlanHTCT.stationCodeVCC = null;
 				vm.totalMonthPlanHTCT.stationName = null;
			}
			
		}
		vm.provinceValueOptions2
  		vm.selectedProvince1 = false;
		vm.provinceValueOptions2= {
	 			dataTextField: "code",
	 			dataValueField: "id",
	 			placeholder: "Nh???p m?? ho???c t??n t???nh",
	 			select: function (e) {
	 				vm.selectedProvince1 = true;
	 				var dataItem = this.dataItem(e.item.index());
	 				vm.totalMonthPlanHTCT.provinceCode = dataItem.code;
	 				vm.totalMonthPlanHTCT.provinceName = dataItem.name;
	 				

	 			},
	 			pageSize: 10,
	 			open: function (e) {
	 				vm.selectedProvince1 = false;
	 			},
	 			dataSource: {
	 				serverFiltering: true,
	 				transport: {
	 					read: function (options) {
	 						vm.selectedProvince1 = false;
	 						return Restangular.all("catProvinceServiceRest/autoCompleteSearch")
	 							.post({ name:vm.totalMonthPlanHTCT.provinceCode,
	 								pageSize: vm.provinceValueOptions2.pageSize,
	 								page:1
	 							})
	 							.then(function (response) {
	 								options.success(response.data);
	 							}).catch(function (err) {
	 								console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
	 							});
	 					}
	 				}
	 			},
	 			headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
	             '<p class="col-md-6 text-header-auto border-right-ccc">M?? t???nh</p>' +
	             '<p class="col-md-6 text-header-auto">T??n </p>' +
	             	'</div>',
	 			template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
	 			change: function(e) {
		                if (e.sender.value() === '') {
		                	vm.totalMonthPlanHTCT.provinceName = null;
		     				vm.totalMonthPlanHTCT.provinceCode = null;
		  	                }
		            },
		       ignoreCase: false,
	 			close: function(e) {
	                 // handle the event
	               }
	 	};
		
		vm.stationValueOptions
  		vm.selectedStation = false;
		vm.stationValueOptions= {
	 			dataTextField: "code",
	 			placeholder: "Nh???p m?? ho???c t??n tr???m",
	 			select: function (e) {
	 				vm.selectedStation = true;
	 				var dataItem = this.dataItem(e.item.index());
	 				vm.totalMonthPlanHTCTSearch.stationCodeVCC = dataItem.code;

	 			},
	 			pageSize: 10,
	 			open: function (e) {
	 				vm.selectedStation = false;
	 			},
	 			dataSource: {
	 				serverFiltering: true,
	 				transport: {
	 					read: function (options) {
	 						vm.selectedStation = false;
	 						var obj ={stationCodeVCC:vm.totalMonthPlanHTCTSearch.stationCodeVCC,
 								    pageSize: vm.stationValueOptions.pageSize,
 								    page:1};
	 						return Restangular.all("totalMonthPlanHTCTRsService/monthReport/getAllStationVCCHTCT").post(obj)
	 							.then(function (response) {
	 								options.success(response.data);
	 							}).catch(function (err) {
	 								console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
	 							});
	 					}
	 				}
	 			},
	 			headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
	             '<p class="col-md-6 text-header-auto ">M?? tr???m VCC</p>' +
	             	'</div>',
	 			template: '<div class="row" ><div class="col-xs-5" style="align:center">#: data.code #</div>',
	 			change: function(e) {
		                if (e.sender.value() === '') {
		                	vm.totalMonthPlanHTCTSearch.stationCodeVCC = null;
		  	                }
		            },
		       ignoreCase: false,
	 			close: function(e) {
	                 // handle the event
	               }
	 	};
		
		
		vm.stationValueOptions2
  		vm.selectedStation1 = false;
		vm.stationValueOptions2= {
	 			dataTextField: "code",
	 			placeholder: "Nh???p m?? ho???c t??n tr???m",
	 			select: function (e) {
	 				vm.selectedStation1 = true;
	 				var dataItem = this.dataItem(e.item.index());
	 				vm.totalMonthPlanHTCT.stationCodeVCC = dataItem.code;
	 				

	 			},
	 			pageSize: 10,
	 			open: function (e) {
	 				vm.selectedStation1 = false;
	 			},
	 			dataSource: {
	 				serverFiltering: true,
	 				transport: {
	 					read: function (options) {
	 						vm.selectedStation1 = false;
	 						var obj ={stationCodeVCC:vm.totalMonthPlanHTCT.stationCodeVCC,
 								    pageSize: vm.stationValueOptions.pageSize,
 								    page:1};
	 						return Restangular.all("totalMonthPlanHTCTRsService/monthReport/getAllStationVCCHTCT")
	 							.post(obj)
	 							.then(function (response) {
	 								options.success(response.data);
	 							}).catch(function (err) {
	 								console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
	 							});
	 					}
	 				}
	 			},
	 			headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
	             '<p class="col-md-6 text-header-auto ">M?? tr???m VCC</p>' +
	             	'</div>',
	 			template: '<div class="row" ><div class="col-xs-5" style="align:center">#: data.code #</div>',
	 			change: function(e) {
		                if (e.sender.value() === '') {
		                	vm.totalMonthPlanHTCT.stationCodeVCC = null;
		  	                }
		            },
		       ignoreCase: false,
	 			close: function(e) {
	                 // handle the event
	               }
	 	};
		
		
		
		         
	}
	
})();
