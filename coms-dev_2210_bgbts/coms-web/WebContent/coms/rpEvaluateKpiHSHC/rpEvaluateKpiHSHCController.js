(function() {
	'use strict';
	var controllerId = 'rpEvaluateKpiHSHCController';

	angular.module('MetronicApp').controller(controllerId,
			rpEvaluateKpiHSHCController);

	function rpEvaluateKpiHSHCController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, rpEvaluateKpiHSHCService,
			CommonService, PopupConst, Restangular, RestEndpoint, Constant,
			htmlCommonService) {
		var vm = this;

		vm.searchForm = {
				kpiState: ""
		};
		vm.String = "Quản lý công trình > Báo cáo > Báo cáo đánh giá KPI HSHC";
		vm.showSearch = true;
        vm.isCreateNew = false;
        vm.showDetail = false;
		vm.sysGroupLv2User = {};
		
		init();
		function init() {
			Restangular.all("rpQuantityService/getGroupLv2ByGroupUser").post($rootScope.authenticatedUser.VpsUserInfo.sysGroupId).then(function (response) {
            	vm.sysGroupIdLv2User = response.plain();
            }).catch(function (err) {
                console.log('Không thể lấy dữ liệu: ' + err.message);
            });
            
            Restangular.all("rpQuantityService/getSysGroupIdByTTKT").post().then(function (response) {
                var checkGroup = response.indexOf(vm.sysGroupIdLv2User.sysGroupId);
                if(checkGroup!=-1){
                	vm.searchForm.sysGroupId = vm.sysGroupIdLv2User.sysGroupId;
                	vm.searchForm.sysGroupName = vm.sysGroupIdLv2User.sysGroupCode + "-" +vm.sysGroupIdLv2User.sysGroupName;
                	doSearch();
                }
            }).catch(function (err) {
                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
            });
			
			var date = new Date();
			var start = new Date(date.getFullYear(), date.getMonth(), 1);
			var end = new Date(date.getFullYear(), date.getMonth() + 1, 0);
			vm.searchForm.dateTo = htmlCommonService.formatDate(end);
			vm.searchForm.dateFrom = htmlCommonService.formatDate(start);
			fillDataTable([]);
		}

		vm.showHideColumn = function (column) {
		    if (angular.isUndefined(column.hidden)) {
		    	vm.evaluateKpiHshcGrid.hideColumn(column);
		    } else if (column.hidden) {
		        vm.evaluateKpiHshcGrid.showColumn(column);
		    } else {
		        vm.evaluateKpiHshcGrid.hideColumn(column);
		    }


		}
		
		vm.gridColumnShowHideFilter = function (item) {
		    return item.type == null || item.type !== 1;
		};
		
		var record = 0;
		function fillDataTable(data) {
			vm.evaluateKpiHshcGridOptions = kendoConfig
					.getGridOptions({
						autoBind : true,
						scrollable : true,
						resizable : true,
						editable : false,
						dataBinding : function() {
							record = (this.dataSource.page() - 1)
									* this.dataSource.pageSize();
						},
						reorderable : true,
						toolbar : [ {
							name : "actions",
							template : '<div class="btn-group pull-right margin_top_button ">'
									+ '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: triệu VNĐ</div>'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
									+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
									+ '<label ng-repeat="column in vm.evaluateKpiHshcGrid.columns.slice(1,vm.evaluateKpiHshcGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'
									+ '</label>' + '</div></div>'
						} ],
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									$("#appCountKpiHshc").text(
											"" + response.total);
									vm.countWorkItem = response.total;
									return response.total;
								},
								data : function(response) {
									return response.data;
								}
							},
							transport : {
								read : {
									// Thuc hien viec goi service
									url : Constant.BASE_SERVICE_URL
											+ "rpQuantityService/doSearchEvaluateKpiHshc",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									vm.searchForm.page = options.page
									vm.searchForm.pageSize = options.pageSize
									return JSON.stringify(vm.searchForm)

								}
							},
							pageSize : 10,
						},
						noRecords : true,
						columnMenu : false,
						messages : {
							noRecords : gettextCatalog
									.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
						},
						pageable : {
							refresh : false,
							pageSizes : [ 10, 15, 20, 25 ],
							messages : {
								display : "{0}-{1} của {2} kết quả",
								itemsPerPage : "kết quả/trang",
								empty : "<div style='margin:5px'>Không có kết quả hiển thị</div>"
							}
						},
						columns : [ {
							title : "TT",
							field : "stt",
							template : function() {
								return ++record;
							},
							width : '30px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',

						}, {
							title : "Đơn vị",
							field : 'sysGroupCode',
							width : '300px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},

						}, {
							title : "Mã tỉnh",
							field : 'catProvinceCode',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},

						}, {
							title : "Mã nhà trạm",
							width : '120px',
							field : 'catstattionhousecode',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',

						},
						{
							title : "Mã trạm",
							width : '120px',
							field : 'catstationCode',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',

						}, {
							title : "Công trình",
							width : '150px',
							field : 'constructionCode',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',

						}, {
							title : "Loại công trình",
							width : '120px',
							field : 'constructionTypeName',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',

						}, {
							title : "Hợp Đồng",
							width : '300px',
							field : 'cntContractCode',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							type : 'text',
						}, {
							title : "Ngày xong đồng bộ",
							width : '120px',
							field : 'companyassigndate',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
						}, {
							title : "Giá trị HSHC",
							field : 'totalQuantity',
							width : '130px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
						}, {
							title : "HSHC được phê duyệt",
							field : 'approvedCompleteDate',
							width : '180px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
						}, {
							title : "KPI",
							field : 'kpiState',
							width : '130px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},
							template: function(dataItem){
								if(dataItem.kpiState==2){
									return "Sai KPI";
								} else if(dataItem.kpiState==1){
									return "Đúng KPI";
								} else {
									return "";
								}
							}
						},
						{
							title : "Ghi chú",
							field : 'description',
							width : '100px',
							headerAttributes : {
								style : "text-align:center;font-weight: bold;"
							},
							attributes : {
								style : "text-align:center;"
							},

						}, ]
					});
		}

		vm.doSearch = doSearch;
		function doSearch(){
			vm.showDetail = false;
		    var grid = vm.evaluateKpiHshcGrid;
		    if (grid) {
		        grid.dataSource.query({
		            page: 1,
		            pageSize: 10
		        });
		    }
		}
		
		vm.cancelInput = function(data){
			if(data=='cntContractCode'){
				vm.searchForm.cntContractCode=null;
			}
			if(data=='dept'){
				vm.searchForm.sysGroupName = null;
		        vm.searchForm.sysGroupId = null;
			}
			if(data=='date'){
				vm.searchForm.dateFrom = null;
				vm.searchForm.dateTo = null;
			}
			if(data=='province'){
				vm.searchForm.catProvinceId = null;
	            vm.searchForm.catProvinceCode = null;
				vm.searchForm.catProvinceName = null;
			}
			if(data=='stationHouse'){
				vm.searchForm.Catstattionhousecode = null;
			}
		}
		
		//------------------Đơn vị----------------//
		vm.selectedDept1 = false;
		vm.deprtOptions1 = {
		    dataTextField: "text",
		    dataValueField: "id",
			placeholder:"Nhập mã hoặc tên đơn vị",
		    select: function (e) {
		        vm.selectedDept1 = true;
		        var dataItem = this.dataItem(e.item.index());
		        vm.searchForm.sysGroupName = dataItem.text;
		        vm.searchForm.sysGroupId = dataItem.id;
		    },
		    pageSize: 10,
		    open: function (e) {
		        vm.selectedDept1 = false;
		    },
		    dataSource: {
		        serverFiltering: true,
		        transport: {
		            read: function (options) {
		                vm.selectedDept1 = false;
		                return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({
		                    name: vm.searchForm.sysGroupName,
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
		            vm.searchForm.sysGroupName = null;// thành name
		            vm.searchForm.sysGroupId = null;
		        }
		    },
		    ignoreCase: false
		}
		
		vm.openDepartmentTo = openDepartmentTo
		function openDepartmentTo(popUp) {
		    vm.obj = {};
		    vm.departmentpopUp = popUp;
		    var templateUrl = 'coms/RPConstructionDK/findDepartmentPopUp.html';
		    var title = gettextCatalog.getString("Tìm kiếm đơn vị");
		    CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
		}
		
		vm.onSave = onSave;
		function onSave(data) {
		    if (vm.departmentpopUp === 'dept') {
		        vm.searchForm.sysGroupName = data.text;
		        vm.searchForm.sysGroupId = data.id;
		    }
		}
		//------------------Tỉnh----------------//
		vm.provinceOptions = {
		        dataTextField: "name",
		        dataValueField: "id",
				placeholder:"Nhập mã hoặc tên tỉnh",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.searchForm.catProvinceId = dataItem.catProvinceId;
		            vm.searchForm.catProvinceCode = dataItem.code;
					vm.searchForm.catProvinceName = dataItem.name;
		        },
		        pageSize: 10,
		        open: function (e) {
		            vm.isSelect = false;
		        },
		        dataSource: {
		            serverFiltering: true,
		            transport: {
		                read: function (options) {
		                    vm.isSelect = false;
		                    return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
		                        name: vm.searchForm.catProvinceName,
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
		        	if (!vm.isSelect) {
		                vm.searchForm.catProvinceId = null;
		                vm.searchForm.catProvinceCode = null;
						vm.searchForm.catProvinceName = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		                vm.searchForm.catProvinceId = null;
		                vm.searchForm.catProvinceCode = null;
						vm.searchForm.catProvinceName = null;
		            }
		        }
		    }
		
		vm.openCatProvincePopup = openCatProvincePopup;
	    function openCatProvincePopup(){
			var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm tỉnh");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
	    }
	    function onSaveCatProvince(data){
	        vm.searchForm.catProvinceId = data.catProvinceId;
	        vm.searchForm.catProvinceCode = data.code;
			vm.searchForm.catProvinceName = data.name;
	        htmlCommonService.dismissPopup();
	        $("#provincename").focus();
	    };
	    //------------------Mã nhà trạm----------------//
	    vm.stationCodeOptions = {
	            dataTextField: "catStationHouseCode",
	            dataValueField: "catStationHouseId",
	            placeholder: "Nhập mã nhà trạm",
	            select: function (e) {
	                vm.isSelect = true;
	                var dataItem = this.dataItem(e.item.index());
	                vm.searchForm.Catstattionhousecode = dataItem.catStationHouseCode;
	            },
	            open: function (e) {
	                vm.isSelect = false;
	            },
	            pageSize: 10,
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function (options) {
	                        vm.isSelect = false;
	                        return Restangular.all("constructionService/getStationForAutoCompleteHouse").post({
	                            keySearch: vm.searchForm.Catstattionhousecode,
	                            pageSize: vm.stationCodeOptions.pageSize,
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
	                '<p class="col-md-12 text-header-auto border-right-ccc">Mã trạm</p>' +
	                '</div>',
	            template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.catStationHouseCode #</div></div>',
	            change: function (e) {
	                if (!vm.isSelect) {
	                    vm.searchForm.Catstattionhousecode = null;
	                }
	            },
	            close: function (e) {
	                if (!vm.isSelect) {
	                    vm.searchForm.Catstattionhousecode = null;
	                }
	            }
	        }
		
	    vm.openCatStationPopup= function() {
	    	var templateUrl = 'coms/popup/catStationSearchPopUp.html';
	    	var title = gettextCatalog.getString("Tìm kiếm mã trạm");
	    	htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catStationHouseSearchController');
	    }
	    
	    vm.onSaveCatStation = function (data) {
	        vm.searchForm.Catstattionhousecode = data.catStationHouseCode;
	    }
	    
	    //-----------------Hợp đồng--------------------//
	    vm.patternContractOutOptions={
	    		dataTextField: "code", 
	    		placeholder:"Mã hợp đồng",
	    		open: function(e) {
	    			vm.isSelect = false;
	    			
	    		},
	            select: function(e) {
	            	vm.isSelect = true;
	            	data = this.dataItem(e.item.index());
	            	vm.searchForm.cntContractCode = data.code;
	            },
	            pageSize: 10,
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function(options) {
	                    	var objSearch = {isSize: true, code:$('#cntContractOut').val(), contractType:0};
	                        return Restangular.all(RestEndpoint.CNT_CONTRACT_SERVICE_URL+"/cntContract/doSearch").post(objSearch).then(function(response){
	                            options.success(response.data);
	                        }).catch(function (err) {
	                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
	                        });
	                    }
	                }
	            },
	            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
	            '<p class="col-md-12 text-header-auto">Mã hợp đồng</p>' +

	            	'</div>',
	            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.code #</div></div>',
	            change: function(e) {	
	            	if (!vm.isSelect) {
	            		vm.searchForm.cntContractCode=null;
	                }
	            },
	            close: function (e) {
	                if (!vm.isSelect) {
	                	vm.searchForm.cntContractCode=null;
	                }
	            }
	    	};
	    
	    vm.openContractOut = function() {
	    	var templateUrl = 'coms/popup/findContractPopUp.html';
	    	var title = gettextCatalog.getString("Tìm kiếm hợp đồng");
	    	htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,null, vm, 'fff', 'ggfd', false, '85%','85%','contractSearchController');
	    }
	    
	    vm.onSaveContract = function(data) {
	    	vm.searchForm.cntContractCode = data.code;
	    }
	    
	    //---------Xuất excel--------------//
	    vm.exportFileCons = function(){
	    	kendo.ui.progress($("#evaluateKpiHshcGrid"),true);
	    	vm.searchForm.page = null;
	    	vm.searchForm.pageSize = null;
	    	
	    	rpEvaluateKpiHSHCService.doSearchEvaluateKpiHshc(vm.searchForm).then(function(d){
	    		var data = d.data;
	    		for(var i=0;i<data.length;i++){
	    			if(data[i].kpiState==1){
	    				data[i].kpiState = "Đúng KPI";
	    			} else {
	    				data[i].kpiState = "Sai KPI";
	    			}
	    		}
	    		CommonService.exportFile(vm.evaluateKpiHshcGrid,d.data,{},{},"Báo cáo đánh giá KPI HSHC");
				kendo.ui.progress($("#evaluateKpiHshcGrid"),false);
	    	});
	    }
	    
		//end controller
	}
})();