(function () {
    'use strict';
    var controllerId = 'goodsPlanController';

    angular.module('MetronicApp').controller(controllerId, goodsPlanController);

    function goodsPlanController($scope, $rootScope, $timeout, gettextCatalog, goodsPlanService, $filter,
        kendoConfig, $kWindow, htmlCommonService, CommonService, PopupConst, Restangular, RestEndpoint, Constant) {

        var vm = this;
        vm.searchForm = {};
        vm.searchFormPopup = {};
        var record = 0;
        vm.d = {};
        vm.obj1 = {};
        vm.obj = {};
        vm.tab1 = true;
        vm.tab2 = false;
        vm.isAddNew = false;
        vm.showDetail = false;
        vm.catReason = [];
        vm.showDetailAdd = true;
        vm.showDSVTGrid = true;
        vm.showDetailDSVTTGrid = false;
        vm.listDataObj =[];
        vm.listOrder=[];
        vm.formUpdate={};
        vm.listDataCheckAll=[];
        vm.checkListDataCheckAll= false;
        vm.checkUpdate =false;
        vm.listCheck =[];
        vm.inputSign={};
        vm.codeSign =[];
        vm.rowData = {};
        vm.catUnitList=[];
        vm.String = "Quản lý công trình > Quản lý công trình > Kế hoạch sản xuất vật tư";
//		Set ngày mặc định lùi 1 tháng
        vm.searchForm.startDate=kendo.toString(new Date((new Date()).getTime()-30*24*3600*1000),"dd/MM/yyyy");
        vm.searchForm.endDate=null;

        initDropDown();
        function initDropDown() {
        	fillDataTablePattern([]);
        	fillDataTablePatternOption(vm.formUpdate);
        	fillDataTable([]);
        	
        	Restangular.all(RestEndpoint.REQUEST_GOODS_SERVICE_URL + "/getCatUnit").getList()
            .then(function (d) {
                vm.catUnitList = d.plain();
            }).catch(function (e) {
            console.log("Không thể kết nối để lấy dữ liệu: " + e.message);
        });
        }

        vm.cancelAdd = cancelAdd;
        function cancelAdd() {
            vm.showDetail = false;
            vm.prevStep();
            vm.DSVTGrid.dataSource.data([]);
            vm.DSVTGrid.refresh();
            vm.DSTBGrid.dataSource.data([]);
            vm.DSTBGrid.refresh();
            vm.doSearchVT();
            vm.doSearchetail();
            vm.String = "Quản lý công trình > Quản lý công trình > Kế hoạch sản xuất vật tư";
        }

        vm.cancelDate= cancelDate;
        function cancelDate(){
        	vm.searchForm.startDate = null;
        	vm.searchForm.endDate = null;
        }
        vm.cancelSignVO= cancelSignVO;
        function cancelSignVO(){
        	vm.searchForm.signVO = [];
        }

        vm.add = add;
        function add(){
//        	vm.listOrder=[];
        	vm.searchFormData.code ="";
        	vm.searchFormData.name ="";
        	vm.searchFormData.baseContent="";
        	vm.searchFormData.performContent="";
            var data = vm.goodsPlan;
                var teamplateUrl = "coms/goodsPlan/goodsPlanAddPopup.html";
                var title = "Thêm mới vật tư";
                vm.goodsPlan = {};
                var windowId = "CONSTRUCTION_LAND_HANDOVER_PLAN";
                vm.checkUpdate =false;
                CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '550', "code");
        }
        
        vm.cancelPopup=cancelPopup;
        function cancelPopup(){
            $(".k-icon.k-i-close").click();
        }
        vm.cancelPopup1=cancelPopup1;
        function cancelPopup1(){
        	CommonService.dismissPopup1();
        }

        vm.openTaskPopup = openTaskPopup

        function openTaskPopup(popUp) {
            vm.keySearch = undefined;
            vm.departmentpopUp = popUp;
            var title = "Danh sách công trình";
            vm.labelSearch = "Mã/Tên công trình";

            var teamplateUrl = "coms/retrievalManagement/popupConstruction.html";

            var windowId = "RETRIEVAL_MANAGEMENT";
            CommonService.populatePopupOnPopup(teamplateUrl, title, null, vm, windowId, true, '950', '450', null);

        }
        
        vm.chooseConstruction = function (data) {
            vm.obj.constructionName = data.name;
            vm.obj.constructionCode = data.code;
            vm.obj.constructionId = data.constructionId;
            vm.obj.receiveGroupName = data.receiveGroupName;
            vm.obj.receiveGroupId = data.receiveGroupId;
            vm.obj.stationId = data.catStationId;
            vm.obj.stationCode = data.catStationCode;
            CommonService.dismissPopup1();
        }
        
        vm.clearInput = function (data) {
            switch (data) {
                case '0':
                    vm.obj.constructionName = null;
                    vm.obj.constructionCode = null;
                    vm.obj.constructionId = null;
                    vm.obj.receiveGroupName = null;
                    break;
                case '1':
                    vm.obj.requestGroupName = null;
                    vm.obj.requestGroupId = null;

                    break;
                case '2':
                    vm.obj.receiveGroupName = null;
                    vm.obj.receiveGroupId = null;
                    break;
                default: break;
            }
        }

        vm.selectedDept11 = false;
        vm.workCode = [];
        vm.workCode1 = [];
        vm.constructionOptions = {
            dataTextField: "code",
            dataValueField: "constructionId",
            select: function (e) {
                vm.selectedDept11 = true;
                var data = this.dataItem(e.item.index());

                vm.obj.constructionName = data.name;
                vm.obj.constructionCode = data.code;
                vm.obj.constructionId = data.constructionId;

                vm.obj.receiveGroupName = data.receiveGroupName;
                vm.obj.receiveGroupId = data.receiveGroupId;

                vm.obj.stationId = data.catStationId;
                vm.obj.stationCode = data.catStationCode;
                
                if (vm.obj.receiveGroupId == null) {
                    vm.obj.receiveGroupId = null;
                }
                if (vm.obj.receiveGroupName == null) {
                    vm.obj.receiveGroupName = null;
                }
            },
            pageSize: 10,
            open: function (e) {
                vm.selectedDept11 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedDept11 = false;
                        return Restangular.all("constructionTaskService/construction/searchConstructionDSTH").post({
                            keySearch: vm.obj.constructionCode,
                            pageSize: vm.constructionOptions.pageSize,
                            page: 1
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.code #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.obj.constructionName = null;
                    vm.obj.constructionCode = null;
                    vm.obj.constructionId = null;

                }
            },
            ignoreCase: false
        }
        vm.closePopupOnPopup = function () {
            CommonService.dismissPopup1();
        }
        function fillDataTable(data) {
        vm.retrievalGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            toolbar: [
                {
                    name: "actions",
                    template: '<div class=" pull-left ">' +
                        '<button class="btn btn-qlk padding-search-right addQLK"' +
                        'ng-click="vm.add()"  uib-tooltip="Tạo mới" translate>Tạo mới</button>' +
                        '<button class="btn btn-qlk padding-search-right TkQLK"' +
                        'ng-click="vm.sendToSign()"  uib-tooltip="Trình ký" translate>Trình ký</button>' +
                        '</div>'
                        +
                        '<div class="btn-group pull-right margin_top_button margin_right10">' +
                        
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFileCons()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.retrievalGrid.columns.slice(1,vm.retrievalGrid.columns.length)| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                }
            ],
            reorderable: true,
            dataSource: {
                serverPaging: false,
                schema: {
                    total: function (response) {
                        $("#goodsPlanCount").text("" + response.total);
                        vm.count = response.total;
                        return response.total;
                    },
                    data: function (response) {
                        var list = response.data;
                        for(var i=0; vm.listCheck.length < 0; i++){
                        	for(var k=0; response.data.length < 0; k++){
                        		if(vm.listCheck[i].goodsPlanId == response.data[k].goodsPlanId){
                        			response.data[k].selected = true;
                        		}
                        	}
                        }
                        return response.data;
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "goodsPlanService/doSearchAll",
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
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
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
                	
                    title: "<input type='checkbox' id='checkalllistchange' name='gridchkselectalllistchange' ng-click='vm.signCheckAll();' ng-model='vm.signAll' />",
                    template: "<input type='checkbox' id='signCheck' name='gridcheckbox' ng-click='vm.signCheck(dataItem)' ng-model='dataItem.selected' />",
                    width: '5%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes:{style:"text-align:center;"},

                },{
                    title: "Mã kế hoạch",
                    field: 'code',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                }, {
                    title: "Tên kế hoạch",
                    field: 'name',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }

                },
                {
                    title: "Ngày tạo",
                    field: 'createdDate',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                }, {
                    title: "Trạng thái trình ký",
                    field: 'signState',
                    width: '10%',
                    headerAttributes: { style: "text-align:left;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: "# if(signState == 1){ #" + "#= 'Chưa trình ký' #" + "# } " + "else if (signState == 2) { # " + "#= 'Đang trình ký' #" + "#}" + "else if (signState == 3) { # " + "#= 'Đã ký' #" + "#} " + "else if (signState == 4) { # " + "#= 'Đã từ chối' #" + "#} #"
                }, {
                    title: "Thao tác",
                    field: 'actions',
                    width: '10%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: dataItem =>
                        '<div class="text-center">'
                        +'<a class="a-img"><img ng-click="vm.viewSignedDoc(dataItem)" uib-tooltip="Xuất Pdf" src="assets/layouts/layout/img/input/pdf.png"></a>'
                        +'<a class="a-img"><img ng-click="vm.exportGridDoc(dataItem)" uib-tooltip="Xuất Doc" src="assets/layouts/layout/img/input/doc.png"></a>'
                    	
                    	+'<button ng-if="vm.showHideButtonDS(dataItem)" ng-click="vm.edit(dataItem)" style=" border: none; background-color: white;" id="updateId" class=" icon_table "'+
						'   uib-tooltip="Sửa" translate>'+
						'<i class="fa fa-pencil" aria-hidden="true"></i>'+
                        '</button>'
                        + '<button ng-if="vm.showHideButtonDS(dataItem)" style=" border: none; background-color: white;" id=""' +
                        'class=" icon_table" ng-click="vm.remove(dataItem)"   uib-tooltip="Xóa" translate' + '>' +
                        '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
                        '</button>'
                        + '<button ng-if="!vm.showHideButtonDS(dataItem)" style=" border: none; background-color: white;" id=""' +
                        'class=" icon_table" uib-tooltip="Khóa" translate' + '>' +
                        '<i class="fa fa-trash" style="color: gray;"  aria-hidden="true"></i>' +
                        '</button>'
                        + '</div>'
                }
            ]
        });
        }
        
        vm.showHideButtonDS = function (dataItem) {
			if (dataItem.signState != '1' && dataItem.signState != '4') {
				return false;
			} else {
				return true;
			}
		}
        
        vm.showHideButtonEdit = function (dataItem) {
			if (dataItem.status === '0') {
				return false;
			} else {
				return true;
			}
		}
        
        vm.showHideButtonAdd = function (dataItem) {
			if (dataItem.status === '0') {
				return false;
			} else {
				return true;
			}
		}

        vm.showHideColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.retrievalGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.retrievalGrid.showColumn(column);
            } else {
                vm.retrievalGrid.hideColumn(column);
            }
        }
        /*
        * * Filter các cột của select
        */

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };

        //xuat excel
        vm.exportFile = function () {
        	function displayLoading(target) {
      	      var element = $(target);
      	      kendo.ui.progress(element, true);
      	      setTimeout(function(){
      	    	  
      	    	var a = vm.searchForm;
                var b = vm.obj;
                var obj = retrievalManagementService.getItem();

                return Restangular.all("assetManagementService/exportRetrievalTask").post(vm.searchForm).then(function (d) {
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
        function refreshGrid(d) {
            var grid = vm.retrievalGrid;
            if (grid) {
                grid.dataSource.data(d);
                grid.refresh();
            }
        }

        vm.doSearchPopup = doSearchPopup;
        function doSearchPopup(data) {
            var grid;

            grid = vm.requestGoodsGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        }

        vm.requestGoodsGridOptions = kendoConfig.getGridOptions({
        	autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            reorderable: true,
             dataSource: {
                 serverPaging: true,
                 schema: {
                     total: function (response) {
                         $("#requestGoodsCount").text("" + response.total);
                         vm.count = response.total;
                         return response.total;
                     },
                     data: function (response) {
                         var list = response.data;
                         for(var i =0; i < vm.listOrder.length ;i++){
                        	 for(var k=0; k < list.length; k++){
                        		 if(vm.listOrder[i].requestGoodsId == list[k].requestGoodsId){
                        			 list[k].selected = true;
                        		 }
                        	 }
                         }
                         return response.data;
                     }
                 },
                 transport: {
                     read: {
                         // Thuc hien viec goi service
                         url: Constant.BASE_SERVICE_URL + "goodsPlanService/doSearchPopupGoodsPlan",
                         contentType: "application/json; charset=utf-8",
                         type: "POST"
                     },
                     parameterMap: function (options, type) {
                         vm.searchFormPopup.page = options.page
                         vm.searchFormPopup.pageSize = options.pageSize
                         return JSON.stringify(vm.searchFormPopup)

                     }
                 },
                 pageSize: 10
             },
            noRecords: true,
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
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
                	
            		title: "<input type='checkbox' id='checkalllistchange' name='gridchkselectalllistchange' ng-click='caller.chkSelectAll1();' ng-model='caller.chkAll1' />",
                    template: "<input type='checkbox' id='childcheckHC' name='gridcheckbox' ng-click='caller.handleCheck(dataItem)' ng-model='dataItem.selected' />",
                    width: '5%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes:{style:"text-align:center;"},

                }, {
                    title: "Đơn vị",
                    field: 'sysGroupName',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                }, {
                    title: "Công trình",
                    field: 'constructionCode',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }

                },
                {
                    title: "Hợp đồng",
                    field: 'cntContractCode',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                
                {
                    title: "Ngày CN tạo yêu cầu",
                    field: 'createdDate',
                    width: '10%',
                    headerAttributes: { style: "text-align:left;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: "#= kendo.toString(kendo.parseDate(createdDate, 'yyyy-MM-dd'), 'dd/MM/yyyy') #"
                },
                {
                    title: "Ghi chú",
                    field: 'description',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Thao tác",
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    template: dataItem =>
                        '<div class="text-center">'
                        + '<button style=" border: none; background-color: white;" id=""' +
                        'class=" icon_table" ng-click="caller.saveOpen(dataItem)"  uib-tooltip="Mở" translate' + '>' +
                        '<i class=" fa fa-check color-green ng-scope" style="color: #337ab7;" aria-hidden="true"></i>' +
                        '</button>'
                        + '</div>',
                    width: '15%'
                }
            ]
        });
        
        /**hoangnh start 10012019**/
        vm.chkSelectAll1 = chkSelectAll1;
		$scope.checkSearch = false;
		function chkSelectAll1() {
			vm.listDataCheckAll =[];
			var grid = vm.requestGoodsGrid;
			chkSelectAllBase(vm.chkAll1, grid);
			if (vm.chkAll1) {
				grid.table.find("tr").each(function(idx, item) {
					var row = $(item);
					var dataItem = grid.dataItem(item);
					if(vm.listDataCheckAll.length > 0){
						for(var i=0; i < vm.listDataCheckAll.length;i++){
//							if(vm.listDataCheckAll[i].requestGoodsId != dataItem.requestGoodsId){
//								vm.listDataCheckAll.push(dataItem);
//								vm.checkListDataCheckAll= true;
//							} else 
//								if(vm.listDataCheckAll[i].requestGoodsId == dataItem.requestGoodsId){
//								vm.listDataCheckAll.splice(i, 1);
//								vm.listDataCheckAll.push(dataItem);
//								vm.checkListDataCheckAll= true;
//							}
						CommonService.getallData("goodsPlanService/doSearchPopupGoodsPlan", vm.searchFormPopup).then(function (data) {
								vm.listCheck = data.plain();
							})
						}
					} else {
						vm.listDataCheckAll.push(dataItem);
						vm.checkListDataCheckAll= true;
					}
				});
			} else {
				vm.checkListDataCheckAll= false;
			}
		};
		
		vm.signCheckAll = signCheckAll;
		function signCheckAll(){
			vm.listCheck =[];
			var grid = vm.retrievalGrid;
			chkSelectAllBase(vm.signAll, grid);
			if (vm.signAll) {
				grid.table.find("tr").each(function(idx, item) {
					var row = $(item);
					var dataItem = grid.dataItem(item);
					if(vm.listCheck.length > 0){
							CommonService.getallData("goodsPlanService/doSearchAll", vm.searchForm).then(function (data) {
								vm.listCheck = data.plain();
							})
					} else {
						vm.listCheck.push(dataItem);
					}
				});
			} else {
				vm.listCheck = [];
			}
		}
		
		vm.preview = preview;
		function preview() {
			var obj = {};
			for(var i=0; i < vm.listCheck.length; i++){
				obj.goodsPlanId=vm.listCheck[i].goodsPlanId;
			}
			obj.reportType="PDF";
	        obj.reportName="KeHoachSanXuatVatTu";
	        CommonService.previewDocSign(obj);
		}
		
		vm.viewSignedDoc = viewSignedDoc;
	    function viewSignedDoc(dataItem){
	        var obj={
	            objectId:dataItem.goodsPlanId,
	            type:"50"
	        }
	        CommonService.viewSignedDoc(obj);
	    }
		/**hoangnh end 10012019**/
		
		/**hoangnh start 14012019 -- autoSearch**/
		var changeReqGroupTrinhky = "";
    	vm.trinhkyReqGroupNameOptions = {
			dataTextField: "fullName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
				vm.inputSign.reqGroupId = dataItem.sysUserId;
				vm.inputSign.reqGroupName = dataItem.fullName;
				vm.inputSign.email = dataItem.email;
				vm.trinhkyReqGroupName = dataItem.fullName;
				changeReqGroupTrinhky = dataItem.fullName;
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("goodsPlanService/filterSysUser").post(
                            {keySearch: vm.trinhkyReqGroupName,
                                pageSize: vm.trinhkyReqGroupNameOptions.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:50%;">Mã người ký</span>' +
			'<span style="display:inline-block;width:50%;">Tên người ký</span>' +
			'</div>',
			template: '<div class="row" ><div class="col-xs-5" style="padding: 0px;float:left">#: data.employeeCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != changeReqGroupTrinhky) {
					vm.inputSign.reqGroupId = null;
					vm.inputSign.reqGroupName = null;
					vm.inputSign.email = null;
					$(".trinhky-approveGroup").val("");
				}
			},
			ignoreCase: false
		};
    	
    	
    	var changeReqGroupTrinhky1= "";
    	vm.trinhkyReqGroupNameOptions1 = {
			dataTextField: "fullName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
				vm.inputSign.reqGroupId1 = dataItem.sysUserId;
				vm.inputSign.reqGroupName1 = dataItem.fullName;
				vm.inputSign.email1 = dataItem.email;
				vm.trinhkyReqGroupName1 = dataItem.fullName;
				changeReqGroupTrinhky1 = dataItem.fullName;
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("goodsPlanService/filterSysUser").post(
                            {keySearch: vm.trinhkyReqGroupName1,
                                pageSize: vm.trinhkyReqGroupNameOptions1.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:50%;">Mã người ký</span>' +
			'<span style="display:inline-block;width:50%;">Tên người ký</span>' +
			'</div>',
			template: '<div class="row" ><div class="col-xs-5" style="padding: 0px;float:left">#: data.employeeCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != changeReqGroupTrinhky1) {
					vm.inputSign.reqGroupId1 = null;
					vm.inputSign.reqGroupName1 = null;
					vm.inputSign.email1 = null;
					$(".trinhky-approveGroup1").val("");
				}
			},
			ignoreCase: false
		};
    	
    	var changeReqGroupTrinhky2= "";
    	vm.trinhkyReqGroupNameOptions2 = {
			dataTextField: "fullName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
				vm.inputSign.reqGroupId2 = dataItem.sysUserId;
				vm.inputSign.reqGroupName2 = dataItem.fullName;
				vm.inputSign.email2 = dataItem.email;
				vm.trinhkyReqGroupName2 = dataItem.fullName;
				changeReqGroupTrinhky2 = dataItem.fullName;
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("goodsPlanService/filterSysUser").post(
                            {keySearch: vm.trinhkyReqGroupName2,
                                pageSize: vm.trinhkyReqGroupNameOptions2.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:50%;">Mã người ký</span>' +
			'<span style="display:inline-block;width:50%;">Tên người ký</span>' +
			'</div>',
			template: '<div class="row" ><div class="col-xs-5" style="padding: 0px;float:left">#: data.employeeCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != changeReqGroupTrinhky2) {
					vm.inputSign.reqGroupId2 = null;
					vm.inputSign.reqGroupName2 = null;
					vm.inputSign.email1 = null;
					$(".trinhky-approveGroup2").val("");
				}
			},
			ignoreCase: false
		};
    	
    	var changeReqGroupTrinhky3= "";
    	vm.trinhkyReqGroupNameOptions3 = {
			dataTextField: "fullName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
				vm.inputSign.reqGroupId3 = dataItem.sysUserId;
				vm.inputSign.reqGroupName3 = dataItem.fullName;
				vm.inputSign.email3 = dataItem.email;
				vm.trinhkyReqGroupName3 = dataItem.fullName;
				changeReqGroupTrinhky3 = dataItem.fullName;
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("goodsPlanService/filterSysUser").post(
                            {keySearch: vm.trinhkyReqGroupName3,
                                pageSize: vm.trinhkyReqGroupNameOptions3.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:50%;">Mã người ký</span>' +
			'<span style="display:inline-block;width:50%;">Tên người ký</span>' +
			'</div>',
			template: '<div class="row" ><div class="col-xs-5" style="padding: 0px;float:left">#: data.employeeCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != changeReqGroupTrinhky3) {
					vm.inputSign.reqGroupId3 = null;
					vm.inputSign.reqGroupName3 = null;
					vm.inputSign.email1 = null;
					$(".trinhky-approveGroup3").val("");
				}
			},
			ignoreCase: false
		};
    	
    	/**hoangnh start 14012019 -- Vai tro**/
    	var changeAshes = "";
    	vm.ashesNameOptions = {
			dataTextField: "adOrgName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
//				vm.inputSign.reqGroupId = dataItem.adOrgId;
				vm.inputSign.reqGroupName = dataItem.adOrgName;
				vm.inputSign.email = dataItem.strEmail;
				vm.inputSign.sysRoleId = dataItem.sysRoleId;
				vm.inputSign.sysRoleName = dataItem.sysRoleName;
				vm.inputSign.adOrgId = dataItem.adOrgId;
				dataItem.fullName = vm.trinhkyReqGroupName;
				
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("signVofficeRsServiceRest/signVoffice/getRoleByEmail").post(
                            {keySearch: vm.inputSign.email,
                                pageSize: vm.ashesNameOptions.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:100%;text-align:center;">Vai trò người ký</span>' +
			'</div>',
			template: '<div class="row" >#: data.adOrgName #</div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != vm.inputSign.email) {
				}
			},
		};
    	
    	var changeAshes1 = "";
    	vm.ashesNameOptions1 = {
			dataTextField: "adOrgName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
//				vm.inputSign.reqGroupId1 = dataItem.adOrgId;
				vm.inputSign.reqGroupName1 = dataItem.adOrgName;
				vm.inputSign.email1 = dataItem.strEmail;
				vm.inputSign.sysRoleId1 = dataItem.sysRoleId;
				vm.inputSign.sysRoleName1 = dataItem.sysRoleName;
				vm.inputSign.adOrgId1 = dataItem.adOrgId;
				dataItem.fullName1 = vm.trinhkyReqGroupName;
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("signVofficeRsServiceRest/signVoffice/getRoleByEmail").post(
                            {keySearch: vm.inputSign.email1,
                                pageSize: vm.ashesNameOptions1.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:100%;text-align:center;">Vai trò người ký</span>' +
			'</div>',
			template: '<div class="row" >#: data.adOrgName #</div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != vm.inputSign.email1) {
				}
			},
		};
    	
    	var changeAshes2 = "";
    	vm.ashesNameOptions2 = {
			dataTextField: "adOrgName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
//				vm.inputSign.reqGroupId2 = dataItem.adOrgId;
				vm.inputSign.reqGroupName2 = dataItem.adOrgName;
				vm.inputSign.email2 = dataItem.strEmail;
				vm.inputSign.sysRoleId2 = dataItem.sysRoleId;
				vm.inputSign.sysRoleName2 = dataItem.sysRoleName;
				vm.inputSign.adOrgId2 = dataItem.adOrgId;
				dataItem.fullName2 = vm.trinhkyReqGroupName;
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("signVofficeRsServiceRest/signVoffice/getRoleByEmail").post(
                            {keySearch: vm.inputSign.email2,
                                pageSize: vm.ashesNameOptions2.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:100%;text-align:center;">Vai trò người ký</span>' +
			'</div>',
			template: '<div class="row" >#: data.adOrgName #</div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != vm.inputSign.email2) {
				}
			},
		};
    	
    	var changeAshes3 = "";
    	vm.ashesNameOptions3 = {
			dataTextField: "adOrgName",
			select: function(e) {
				var dataItem = this.dataItem(e.item.index());
//				vm.inputSign.reqGroupId3 = dataItem.adOrgId;
				vm.inputSign.reqGroupName3 = dataItem.adOrgName;
				vm.inputSign.email3 = dataItem.strEmail;
				vm.inputSign.sysRoleId3 = dataItem.sysRoleId;
				vm.inputSign.sysRoleName3 = dataItem.sysRoleName;
				vm.inputSign.adOrgId3 = dataItem.adOrgId;
				dataItem.fullName3 = vm.trinhkyReqGroupName;
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function(options) {
						return Restangular.all("signVofficeRsServiceRest/signVoffice/getRoleByEmail").post(
                            {keySearch: vm.inputSign.email3,
                                pageSize: vm.ashesNameOptions3.pageSize}).then(function(response){
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
					}
				}
			},
			headerTemplate: '<div class="dropdown-header k-widget k-header">' +
			'<span style="display:inline-block;width:100%;text-align:center;">Vai trò người ký</span>' +
			'</div>',
			template: '<div class="row" >#: data.adOrgName #</div>',
			change: function(e) {
				if (e.sender.value() === '' || e.sender.value() != vm.inputSign.email3) {
				}
			},
		};
    	/**hoangnh -- end vai tro**/
    	/**hoangnh -- start 14012019- signVO**/
    	vm.signSave = signSave;
    	function signSave() {
    		
           console.log(vm.inputSign);
           
         if(vm.inputSign.reqGroupId == "" || vm.inputSign.reqGroupId== null){
   	        $(".trinhky-approveGroup").focus();
   	        toastr.warning("Người phê duyệt trung tâm hạ tầng không được để trống !");
   	        return;
   	     }
   		 if(vm.inputSign.reqGroupId1 == "" || vm.inputSign.reqGroupId1== null){
   			$(".trinhky-approveGroup1").focus();
   			toastr.warning("Người phê duyệt phòng đầu tư không được để trống !");
   			return;
   		  }
   		 if(vm.inputSign.reqGroupId2 == "" || vm.inputSign.reqGroupId2== null){
   	        $(".trinhky-approveGroup2").focus();
   	        toastr.warning("Người phê duyệt phòng quản lý tài sản không được để trống !");
   	        return;
   	     }
   		 if(vm.inputSign.reqGroupId3 == "" || vm.inputSign.reqGroupId3== null){
   			$(".trinhky-approveGroup3").focus();
   			toastr.warning("Người phê duyệt ban giám đốc không được để trống !");
   			return;
   		  }
   		 
   		 if(vm.ashesName == "" || vm.ashesName== null){
   			 $(".ashes-approveGroup").focus();
   			 toastr.warning("Vai trò không được để trống !");
   			 return;
   		 }
   		 if(vm.ashesName1 == "" || vm.ashesName1== null){
   			 $(".ashes-approveGroup1").focus();
   			 toastr.warning("Vai trò không được để trống !");
   			 return;
   		 }
   		if(vm.ashesName2 == "" || vm.ashesName2== null){
  			 $(".ashes-approveGroup2").focus();
  			 toastr.warning("Vai trò không được để trống !");
  			 return;
  		 }
  		 if(vm.ashesName3 == "" || vm.ashesName3== null){
  			 $(".ashes-approveGroup3").focus();
  			 toastr.warning("Vai trò không được để trống !");
  			 return;
  		 }
   		for(var i=0; i< vm.listCheck.length; i++){
   			vm.listCheck[i].listSignVoffice = [];
   			vm.listCheck[i].reportName = "KeHoachSanXuatVatTu";
   			vm.listCheck[i].objectCode = vm.listCheck[i].code;
   			vm.listCheck[i].objectId = vm.listCheck[i].goodsPlanId;
   			vm.listCheck[i].type = "50";
   	  		vm.listCheck[i].listSignVoffice.push({
   	        	sysUserId : vm.inputSign.reqGroupId,
   	        	fullName : vm.inputSign.reqGroupName,
   	        	email : vm.inputSign.email,
   	        	sysRoleId : vm.inputSign.sysRoleId,
   	        	sysRoleName : vm.inputSign.sysRoleName,
   	        	adOrgId :  vm.inputSign.adOrgId,
   	        	createdUserId : vm.listCheck[i].createdUserId
   	        });
   	  		vm.listCheck[i].listSignVoffice.push({
   	        	sysUserId : vm.inputSign.reqGroupId1,
   	        	fullName : vm.inputSign.reqGroupName1,
   	        	email : vm.inputSign.email1,
   	        	sysRoleId : vm.inputSign.sysRoleId1,
   	        	sysRoleName : vm.inputSign.sysRoleName1,
   	        	adOrgId :  vm.inputSign.adOrgId1
   	        });
   	  		vm.listCheck[i].listSignVoffice.push({
   	        	sysUserId : vm.inputSign.reqGroupId2,
   	        	fullName : vm.inputSign.reqGroupName2,
   	        	email : vm.inputSign.email2,
   	        	sysRoleId : vm.inputSign.sysRoleId2,
   	        	sysRoleName : vm.inputSign.sysRoleName2,
   	        	adOrgId :  vm.inputSign.adOrgId2
   	        });
   	  		vm.listCheck[i].listSignVoffice.push({
   	        	sysUserId : vm.inputSign.reqGroupId3,
   	        	fullName : vm.inputSign.reqGroupName3,
   	        	email : vm.inputSign.email3,
   	        	sysRoleId : vm.inputSign.sysRoleId3,
   	        	sysRoleName : vm.inputSign.sysRoleName3,
   	        	adOrgId :  vm.inputSign.adOrgId3
   	        });
   		 }

  		goodsPlanService.signVofficeKHSXVT(vm.listCheck).then(function(d){
			if(d.error){
				toastr.error(d.error);
				return;
				
			}
			toastr.success("Trình ký thành công!");
			CommonService.dismissPopup();
			vm.doSearchVT();
			vm.listCheck =[];
		}, function() {
			toastr.error('Error');
            $scope.isProcessing = false;
		});
			
		}
    	/**hoangnh -- end 14012019- signVO**/
		
//		function remove(dataItem){
//			confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function(){
//				goodsPlanService.remove(dataItem).then(
//						function(d) {
//							toastr.success("Xóa thành công!");
//							var sizePage = $("#listConstructionPopupGrid").data("listConstructionPopupGrid").dataSource.total();
//							var pageSize = $("#listConstructionPopupGrid").data("kendoGrid").dataSource.pageSize();
//							if(sizePage % pageSize === 1){
//								var currentPage = $("#listConstructionPopupGrid").data("kendoGrid").dataSource.page();
//								if (currentPage > 1) {
//									$("#listConstructionPopupGrid").data("kendoGrid").dataSource.page(currentPage - 1);
//								}
//							}
//							 $("#listConstructionPopupGrid").data('kendoGrid').dataSource.read();
//							 $("#listConstructionPopupGrid").data('kendoGrid').refresh();
//
//						}, function(errResponse) {
//							toastr.error("Lỗi không xóa được!");
//						});
//			} )
//	}
        //phuc_0706
        
        function getDSTBTask() {
            var list = [];
            var data = vm.DSTBGrid.dataSource._data;
            vm.DSTBGrid.table.find("tr").each(function (idx, data) {
                var row = $(data);
                var checkbox = $('[name="gridcheckbox"]', row);
                if (checkbox.is(':checked')) {
                    var tr = vm.DSTBGrid.select().closest("tr");
                    var dataItem = vm.DSTBGrid.dataItem(data);
                    dataItem.employ = 1;
                    list.push(dataItem);
                } else {
                    var tr = vm.DSTBGrid.select().closest("tr");
                    var dataItem = vm.DSTBGrid.dataItem(data);
                    dataItem.employ = 0;
                }
            });
            return list;
        }
        
        vm.removeDSFgridItemFile = removeDSFItemFile
        function removeDSFItemFile(e) {
            var grid = vm.DSFgrid;
            var row = $(e.target).closest("tr");
            var dataItem = grid.dataItem(row);
            grid.removeRow(dataItem); //just gives alert message
            grid.dataSource.remove(dataItem); //removes it actually from the grid
            grid.dataSource.sync();
            grid.refresh();
        }

        function formatDate(date) {
            var newdate = new Date(date);
            return kendo.toString(newdate, "dd/MM/yyyy");
        }

        vm.openDepartmentTo1 = openDepartmentTo1

        function openDepartmentTo1(popUp) {
            //vm.obj={};
            vm.departmentpopUp = popUp;
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
        }
        
        vm.doSearchVT = doSearchVT;
        function doSearchVT() {
        	
        	vm.listCheck=[];
            var startDate = $("#startDate").val();
        	var checkDate = new Date();
			if (startDate != "" || startDate != null) {
				if (kendo.parseDate(startDate, "dd/MM/yyyy") > kendo.parseDate(checkDate, "dd/MM/yyyy")) {
					toastr.warning("Từ ngày không được lớn hơn ngày hiện tại.");
					return;
				}
			}
			
            $("#retrievalGrid").data('kendoGrid').dataSource.read();
            
        }

        vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'req') {
                vm.searchForm.requestGroupName = data.text;
                vm.searchForm.requestGroupId = data.id;
            } else if (vm.departmentpopUp === 'rec') {
                vm.searchForm.receiveGroupName = data.text;
                vm.searchForm.receiveGroupId = data.id;
            } else if (vm.departmentpopUp === 'reqAdd') {
                vm.obj.requestGroupName = data.text;
                vm.obj.requestGroupId = data.id;
            } else if (vm.departmentpopUp === 'recAdd') {
                vm.obj.receiveGroupName = data.text;
                vm.obj.receiveGroupId = data.id;
            } else if (vm.departmentpopUp === 'search') {
                vm.obj.receiveGroupName = data.receiveGroupName;
                vm.obj.receiveGroupId = data.receiveGroupId;
            }

        }

        vm.selectedDept1 = false;
        vm.deprtOptions1 = {
            dataTextField: "name",
            dataValueField: "groupId",
            placeholder: "Nhập mã hoặc tên đơn vị",
            select: function (e) {
                vm.selectedDept1 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.searchFormPopup.sysGroupName = dataItem.text;
//                vm.searchForm.constructionCode = dataItem.id;
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
                        return Restangular.all("goodsPlanService/doSearchSysGroup").post({ keySearch: vm.searchFormPopup.sysGroupName, pageSize: vm.deprtOptions1.pageSize }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.groupCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.searchFormPopup.sysGroupName = null;// thành name
//                    vm.searchForm.requestGroupId = null;
                }
            },
            ignoreCase: false
        }

        vm.selectedDept2 = false;
        vm.deprtOptions2 = {
            dataTextField: "text",
            dataValueField: "id",
            placeholder: "Nhập mã hoặc tên đơn vị nhập",
            select: function (e) {
                vm.selectedDept2 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.searchForm.constructionCode = dataItem.text;
//                vm.searchForm.receiveGroupId = dataItem.id;
            },
            pageSize: 10,
            open: function (e) {
                vm.selectedDept2 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedDept2 = false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({ name: vm.searchForm.receiveGroupName, pageSize: vm.deprtOptions2.pageSize }).then(function (response) {
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
                    vm.searchForm.constructionCode = null;// thành name
//                    vm.searchForm.receiveGroupId = null;
                }
            },
            ignoreCase: false
        }

        vm.selectedDept3 = false;
        vm.deprtOptions3 = {
            dataTextField: "text",
            dataValueField: "id",
            select: function (e) {
                vm.selectedDept3 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.obj.receiveGroupName = dataItem.text;
                vm.obj.receiveGroupId = dataItem.id;
            },
            pageSize: 10,
            open: function (e) {
                vm.selectedDept3 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedDept3 = false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({ name: vm.obj.receiveGroupName, pageSize: vm.deprtOptions3.pageSize }).then(function (response) {
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
                    vm.obj.receiveGroupName = null;// thành name
                    vm.obj.receiveGroupId = null;
                }
            },
            ignoreCase: false
        }

        vm.selectedDept4 = false;
        vm.deprtOptions4 = {
            dataTextField: "text",
            dataValueFie: "id",
            select: function (e) {
                vm.selectedDept4 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.obj.requestGroupName = dataItem.text;
                vm.obj.requestGroupId = dataItem.id;

            },
            pageSize: 10,
            open: function (e) {
                vm.selectedDept4 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedDept4 = false;
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({ name: vm.obj.requestGroupName, pageSize: vm.deprtOptions4.pageSize }).then(function (response) {
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
                    vm.obj.requestGroupName = null;// thành name
                    vm.obj.requestGroupId = null;
                }
            },
            ignoreCase: false
        }

        vm.deleteListData = deleteListData;
        function deleteListData(x) {
            if (x == 1) {
                vm.searchForm.constructionCode = null;
//                vm.searchForm.requestGroupId = null;
            }
            if (x == 2) {
                vm.searchForm.listStatus = null;
            }
            if (x == 3) {
                vm.searchForm.constructionCode = null;// thành name
//                vm.searchForm.receiveGroupId = null;
            }

        }

        function numberWithCommas(x) {
            if (x == null || x == undefined) {
                return '0';
            }
            var parts = x.toFixed(2).toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
        }
        
        function fillDataTablePattern(data) {
			/*var dataSource = new kendo.data.DataSource({
                pageSize: 10,
                data: data,
                autoSync: false,        
                schema: {
                    model: {
                        id: "patternGrid",
                    	fields: {
                    		stt: {editable: false},
                    		constructionId: {editable: false},
                    		cntContractId: {editable: false},
                    		goodsName: {editable: false},
                    		amount:  { type: "number"},
                    		choose: {editable: false},
                    	}
                    }
                }
            });*/
        vm.retrievalGridOptions1 = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            navigatable: true, 
            sortable: false,
            scrollable: false,
            columnMenu: false,
            serverPaging: false,
            dataBinding: function () {
//                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            	record=0;
            },
            toolbar: [
                {
                    name: "actions",
                    template:
                        '<button class="btn btn-qlk padding-search-right addQLK" style="width:14%"' +
                        'ng-click="vm.Popup()"  uib-tooltip="Chọn yêu cầu" translate>Chọn yêu cầu</button>'
                        +'<button class="btn btn-qlk padding-search-right addQLK" style="width:14%"' +
                        'ng-click="caller.addRow()"  uib-tooltip="Bổ sung" translate>Bổ sung</button>'
                }
            ],
            reorderable: true,
            noRecords: true,
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
            },
            pageable: {
                refresh: false,
                pageSizes: [10, 15, 20, 25],
                messages: {
                    display: "",
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
                    width: '5%',
                    columnMenu: false,
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
                    editor: nonEditor
                },
            	/*{
                    title: "TT",
                    field: 'record',
                    width: '5%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
                    editable: false
                },*/
                {
                    title: "Mã công trình",
                    field: 'constructionCode',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editor: nonEditor
                },
                {
                    title: "CV/HD",
                    field: 'cntContractCode',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editor: nonEditor
                },
                {
                    title: "Tên vật tư thiết bị",
                    field: 'goodsName',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
//                    editor: nonEditor
                    editable: true,
                },
                {
                    title: "ĐVT",
                    field: 'catUnitName',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editor: nonEditor
                },
                {
                    title: "Số lượng",
                    field: 'quantity',
                    width: '7%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editor: nonEditor
                },
                {
                    title: "Ngày đảm bảo theo Kpi",
                    field: 'expectedDate',
                    width: '12%',
                    template: dataItem =>!!dataItem.expectedDate ? kendo.toString(dataItem.expectedDate, 'dd/MM/yyyy') : "",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    editor: function (container, options) {
                    	
    				    var input = $("<input id = 'kendoDate'  ng-field = 'expectedDate'/>");
    				    input.attr("name", options.field);
    				    input.appendTo(container);
    				    input.kendoDatePicker({format:"dd/MM/yyyy"});
    			    },
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Ghi chú",
                    field: 'description',
                    width: '18%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true
                },{
                    title: "Thao tác",
                    field: 'actions',
                    width: '8%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    },
                    editor: nonEditor,
                    template: dataItem =>
                        '<div class="text-center">'
	                    +'<button ng-if="dataItem.constructionCode==null" ng-click="caller.editRowNewPopup(dataItem)" style=" border: none; background-color: white;" id="updateId" class=" icon_table "'+
						'   uib-tooltip="Sửa" translate > '+
						'<i class="fa fa-pencil" aria-hidden="true"></i>'+
	                    '</button>'
                        + '<button ng-if="vm.showHideButtonAdd(dataItem)" style=" border: none; background-color: white;" id=""' +
                        'class=" icon_table" ng-click="vm.removeGoodsPlan($event)"   uib-tooltip="Xóa" translate' + '>' +
                        '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
                        '</button>'
                        + '<button ng-if="!vm.showHideButtonAdd(dataItem)" style=" border: none; background-color: white;" id=""' +
                        'class=" icon_table" uib-tooltip="Khóa" translate' + '>' +
                        '<i class="fa fa-trash" style="color: gray;"  aria-hidden="true"></i>' +
                        '</button>'
                        + '</div>'
                }
            ]
        });
        }
        
        function dateTimeEditor(container, options) {
            $('<input type="text" />')
                    .appendTo(container)
                    .kendoDateTimePicker({
        				      format:"dd/MM/yyyy",
                    	value: kendo.toString(new Date(options.model.dateTime), 'dd/MM/yyyy')
                    });
        }
        
        /**hoangnh start 12012019 -- SignVO**/
        
        vm.sendToSign = function () {
			var teamplateUrl = "coms/goodsPlan/signVOPopup.html";
			var title = CommonService.translate("Trình ký kế hoạch sản xuất vật tư");
			var windowId = "GOODS_PLAN";
			vm.trinhkyReqGroupName = null;
			vm.trinhkyReqGroupName1 = null;
			vm.trinhkyReqGroupName2 = null;
			vm.trinhkyReqGroupName3 = null;
			vm.ashesName = null;
			vm.ashesName1 = null;
			vm.ashesName2 = null;
			vm.ashesName3 = null;
			vm.nameToSign = CommonService.translate("Danh sách yêu cầu trình ký") + "(" + vm.listCheck.length + ")";
			var selectedRow = [];
			for (var i in $scope.listCheck) {
				selectedRow.push(vm.listCheck[i].goodsPlanId);
			}
			if (vm.listCheck.length === 0) {
				toastr.warning(CommonService.translate("Cần chọn bản ghi trước khi thực hiện!"));
				return;
			} else {
				var check = true;
				vm.inputSign.codeSign='';
				for (var i = 0; i < vm.listCheck.length; i++){
					
					if(vm.inputSign.codeSign != ''){
						vm.inputSign.codeSign = vm.inputSign.codeSign + "," + vm.listCheck[i].code;
					} else {
						vm.inputSign.codeSign = vm.listCheck[i].code;
					}
					if (vm.listCheck[i].signState !== "1" && vm.listCheck[i].signState !== "4"){
						toastr.warning(CommonService.translate("Yêu cầu có mã " + vm.listCheck[i].code + " không đúng trạng thái để trình ký!"));
						check = false;
						return;
					}
				}
				if (check) {
					var obj = {};
					obj.listId = selectedRow;
					obj.reportName = "KeHoachSanXuatVatTu";
//					CommonService.getDataSign(obj).then(function (data) {
//						if (data.error) {
//							toastr.error(data.error);
//							return;
//						}
//						var dataList = data.plain();
					var dataList =vm.listCheck;
						CommonService.populatePopupVofice(teamplateUrl, title, '01', dataList, vm, windowId, false, '75%', '75%');
//					});
				}
			}
		}
        /**hoangnh end 12012019**/
        
        /**hoangnh start 09012019**/
        function fillDataTablePatternOption(data) {
        vm.retrievalGridOpEditOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            navigatable: true, 
            sortable: false,
            scrollable: false,
            columnMenu: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            toolbar: [
                {
                    name: "actions",
                    template:
                        '<button class="btn btn-qlk padding-search-right addQLK" style="width:14%"' +
                        'ng-click="caller.Popup()"  uib-tooltip="Chọn yêu cầu" translate>Chọn yêu cầu</button>'
                }
            ],
            reorderable: true,
            dataSource: {
                serverPaging: false,
                schema: {
                    total: function (response) {
                    	 $("#goodsPlanCount").text("" + response.total);
                         vm.count = response.total;
                         return response.total;
                    },
                    data: function (response) {
                    	for(var i=0; i< response.data.length; i++){
                    		vm.listDataObj.splice(i, 1);
                    		vm.listDataObj.push(response.data[i]);
                    	}
                        var list = response.data;
                        return vm.listDataObj;
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "goodsPlanService/doSearch",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.formUpdate.page = options.page
                        vm.formUpdate.pageSize = options.pageSize
                        return JSON.stringify(vm.formUpdate)

                    }
                },
                pageSize: 10
            },
//            editable:false,
            noRecords: true,
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
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
                    field: "stt",
                    template: function () {
                        return ++record;
                    },
                    width: '5%',
                    columnMenu: false,
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    }
                },
                {
                    title: "Mã công trình",
                    field: 'constructionCode',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editor: nonEditor
                },
                {
                    title: "CV/HD",
                    field: 'cntContractCode',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editor: nonEditor
                },
                {
                    title: "Tên vật tư thiết bị",
                    field: 'goodsName',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editor: nonEditor
                },
                {
                    title: "ĐVT",
                    field: 'catUnitName',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editor: nonEditor
                },
                {
                    title: "Số lượng",
                    field: 'quantity',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editor: nonEditor
                },
                {
                    title: "Ngày đảm bảo theo Kpi",
                    field: 'expectedDate',
                    width: '10%',
                    editable: true,
                    template: dataItem =>!!dataItem.expectedDate ? kendo.toString(dataItem.expectedDate, 'dd/MM/yyyy') : "",
                    editor: function (container, options) {
    				    var input = $("<input id = 'kendoDate'  ng-field = 'expectedDate'/>");
    				    input.attr("name", options.field);
    				    input.appendTo(container);
    				    input.kendoDatePicker({format:"dd/MM/yyyy"});
    			    },
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Ghi chú",
                    field: 'description',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true
                },{
                    title: "Thao tác",
                    field: 'actions',
                    width: '8%',
                    headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    attributes: {
                        style: "text-align:left;"
                    },
                    editor: nonEditor,
                    template: dataItem =>
                        '<div class="text-center">'
                        + '<button ng-if="caller.showHideButtonEdit(dataItem)" style=" border: none; background-color: white;" id=""' +
                        'class=" icon_table" ng-click="caller.removeGoodsPlan($event)"   uib-tooltip="Xóa" translate' + '>' +
                        '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
                        '</button>'
                        + '<button ng-if="!caller.showHideButtonEdit(dataItem)" style=" border: none; background-color: white;" id=""' +
                        'class=" icon_table" uib-tooltip="Khóa" translate' + '>' +
                        '<i class="fa fa-trash" style="color: gray;"  aria-hidden="true"></i>' +
                        '</button>'
                        + '</div>'
                }
            ]
        });
        }
        
        vm.exportGridPdf=function(){
			var obj={};
         	obj.goodsPlanId=vm.formUpdate.goodsPlanId;
         	obj.reportType="PDF";
         	obj.reportName="KeHoachSanXuatVatTu";
         	var date = kendo.toString(new Date((new Date()).getTime()),"dd-MM-yyyy");
         	CommonService.exportReport(obj).then(
					function(data) {
					var binarydata= new Blob([data],{ type:'application/pdf'});
			        kendo.saveAs({dataURI: binarydata, fileName: date + "_KeHoachSanXuatVatTu" + '.pdf'});
				}, function(errResponse) {
					toastr.error("Lỗi không export pdf được!");
				});
		}
        
        vm.exportGridDoc=function(dataItem){
			var obj={};
         	obj.goodsPlanId=dataItem.goodsPlanId;
         	obj.reportType="DOC";
         	obj.reportName="KeHoachSanXuatVatTu";
         	var date = kendo.toString(new Date((new Date()).getTime()),"dd-MM-yyyy");
         	CommonService.exportReport(obj).then(
         			function(data) {
    				var binarydata= new Blob([data],{type: "text/plain;charset=utf-8"});
    			    kendo.saveAs({dataURI: binarydata, fileName: date + "_KeHoachSanXuatVatTu" + '.docx'});
				}, function(errResponse) {
					toastr.error("Lỗi không export DOC được!");
				});
		}
        
        function nonEditor(container, options) {
            container.text(options.model[options.field]);
        }
        
        vm.removeGoodsPlan = removeGoodsPlan;
        function removeGoodsPlan(e) {
        	
        	if(vm.checkUpdate != true){
        		var grid = vm.retrievalGridOp;
        	} else {
        		var grid = vm.retrievalGridOpEdit;
        	}
        		
                var row = $(e.target).closest("tr");
                var dataItem = grid.dataItem(row);
                grid.removeRow(dataItem); // just gives alert message
                grid.dataSource.remove(dataItem); // removes it actually from the
                // grid
                grid.dataSource.sync();
                grid.refresh();
                for(var i =0; i< vm.listDataObj.length ;i++){
                	if(vm.listDataObj[i].requestGoodsDetailId == dataItem.requestGoodsDetailId){
                		vm.listDataObj.splice(i, 1);
                	}
                }
        }
        
        vm.remove = remove;
        function remove(dataItem) {
        	
        	confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function(){
        		dataItem.status =0;
        		goodsPlanService.remove(dataItem).then(function (result) {
        			
                    if (result.error) {
                        toastr.error(result.error);
                        return;
                    }
                    toastr.success("Xóa thành công!");
                    vm.doSearchVT();
                }, function (errResponse) {
                    toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi xóa"));
                });
        	});
        }
     // Hàm check xem mặt hàng đã tồn tại trong lưới chưa
        function checkDups(goodsItem){
        	
			var isExisted = false;
			if(vm.checkUpdate != true){
				var goodsGrid = vm.retrievalGridOp;
				var ds = $("#retrievalGridOp").data("kendoGrid").dataSource.data();
			} else {
				var goodsGrid = vm.retrievalGridOpEdit;
				var ds = $("#retrievalGridOpEdit").data("kendoGrid").dataSource.data();
			}
				if(ds.length!=0 && goodsItem != null){
					goodsGrid.table.find("tr").each(function(idx, item) {
						
    					var row = $(item);
    					var dataItem = goodsGrid.dataItem(item);
    					if(dataItem != null){
    						for(var i=0; i < goodsItem.length; i++){
        						if(goodsItem[i].requestGoodsId == dataItem.requestGoodsId){
            						isExisted = true;
            					}
        					}
    					}
					});
				}
            return isExisted;
		}
        //End
        vm.Popup = Popup;
        function Popup(dataItem) {
            var data = vm.goodsPlan;
                var teamplateUrl = "coms/goodsPlan/goodsPlanPopup.html";
                var title = "Tìm kiếm";
                vm.goodsPlan = {};
                var windowId = "CONSTRUCTION_LAND_HANDOVER_PLAN";
                CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '950', '500', "code");
    }
        /**hoangnh start 09012019**/
        vm.edit = edit;
        function edit(dataItem) {
        	
            vm.searchFormUpdate=dataItem;
            vm.formUpdate = dataItem;
            vm.listDataObj=[];
            var teamplateUrl = "coms/goodsPlan/goodsPlanEditPopup.html";
            var title = "Chỉnh sửa vật tư";
            vm.goodsPlan = {};
            var windowId = "CONSTRUCTION_LAND_HANDOVER_PLAN";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '500', "code");
            fillDataTablePatternOption(vm.searchFormUpdate);
            vm.checkUpdate =true;
        }
        /**hoangnh end 09012019**/
        
        function refreshAllGridTab2(data) {
            var DSFgrid = vm.DSFgrid;
            var DSVTGrid = vm.DSVTGrid;
            if (DSFgrid) {
                DSFgrid.dataSource.data(data.listFileTHVTTB);
                DSFgrid.refresh();
            }
        }
        vm.save = function () {
        	//hoangnh 09012019
            var data = populateDataToSaveVT();
            if(data != null){
            	 goodsPlanService.createYearPlan(data).then(function (result) {
                     if (result.error) {
                         toastr.error(result.error);
                         return;
                     }
                     toastr.success("Thêm mới thành công!");
                     cancelPopup();
                     vm.doSearchVT();
                 }, function (errResponse) {
                     toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi thêm"));
                 });
            }
    }
        
        vm.updateVT = function () {
            var data = populateDataToSaveVT();
            if(data != null){
            	goodsPlanService.updateGoodsPlan(data).then(function (result) {
                    if (result.error) {
                        $('#year').focus();
                        toastr.error(result.error);
                        return;
                    }
                    toastr.success("Cập nhật thành công!");
                    cancelPopup();
                    vm.doSearchVT();
                }, function (errResponse) {
                    toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi cập nhật"));
                });
            }
    }
        
        function populateDataToSave() {
            var data = vm.yearPlan;
            var detailList = []
            var yearPlanDetailGrid = vm.yearPlanDetailGrid;
            if (yearPlanDetailGrid != undefined && yearPlanDetailGrid.dataSource != undefined)
                data.detailList = yearPlanDetailGrid.dataSource._data;
            return data;
        }
        /**hoangnh start 09012019**/
        vm.saveOpen = function(data) {
        	data.createdDate =null;
        	goodsPlanService.doSearchReqGoodsDetail(data).then(function (dataItem) {
                	if(dataItem.length >0){
                		var check = checkDups(dataItem);
                		if(check){
        					toastr.warning(CommonService.translate("Mã hàng hóa đã tồn tại trong lưới!"));
        				}else {
        					for(var k=0; k < dataItem.length; k++){
        						dataItem[k].record = k + 1;
        						vm.listDataObj.push(dataItem[k]);
        					}
        					if(vm.checkUpdate != true){
        						$("#retrievalGridOp").data("kendoGrid").dataSource.data(vm.listDataObj);
    		                    fillDataTablePattern(vm.listDataObj);
        					} else {
        						$("#retrievalGridOpEdit").data("kendoGrid").dataSource.data(vm.listDataObj);
        						fillDataTablePatternOption(vm.listDataObj);
        					}
		                    
		                    CommonService.dismissPopup1();
        				}
                	}
                }, function (error) {
                    toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
                });
        }
        
        vm.saveCheck=function() {
        	
//            $("#retrievalGridOp").data("kendoGrid").dataSource.data(vm.listDataObj);
//            fillDataTablePattern(vm.listDataObj);
//            CommonService.dismissPopup1();
        	if(vm.checkUpdate != true){
        		if(vm.checkListDataCheckAll != true){
            		if(vm.listOrder.length > 0)
            		for(var i=0; i < vm.listOrder.length ;i++){
                		vm.listOrder[i].createdDate =null;
                    	goodsPlanService.doSearchReqGoodsDetail(vm.listOrder[i]).then(function (dataItem) {
                            	if(dataItem.length >0){
                            		var check = checkDups(dataItem);
                            		if(check){
                    					toastr.warning(CommonService.translate("Mã tồn tại trong lưới!"));
                    				}else {
                    					for(var k=0; k < dataItem.length; k++){
                    						dataItem[k].record = k+1;
                    						vm.listDataObj.push(dataItem[k]);
                    						$("#retrievalGridOp").data("kendoGrid").dataSource.data(vm.listDataObj);
                    		                fillDataTablePattern(vm.listDataObj);
                    		                CommonService.dismissPopup1();
                    					}
                    				}
                            	}
                            }, function (error) {
                                toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
                            });
                	}
            	} else {
            		if(vm.listDataCheckAll != null)
            		for(var i=0; i < vm.listDataCheckAll.length;i++){
            			vm.listDataCheckAll[i].createdDate =null;
                    	goodsPlanService.doSearchReqGoodsDetail(vm.listDataCheckAll[i]).then(function (dataItem) {
                            	if(dataItem.length >0){
                            		var check = checkDups(dataItem);
                            		if(check){
                    					toastr.warning(CommonService.translate("Mã tồn tại trong lưới!"));
                    				}else {
                    					for(var k=0; k < dataItem.length; k++){
                    						dataItem[k].record = k+1;
                    						vm.listDataObj.push(dataItem[k]);
                    						$("#retrievalGridOp").data("kendoGrid").dataSource.data(vm.listDataObj);
                    						fillDataTablePatternOption(vm.listDataObj);
                    		                CommonService.dismissPopup1();
                    					}
                    				}
                            	}
                            }, function (error) {
                                toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
                            });
            		}
            	}
        	} else {
        		if(vm.checkListDataCheckAll != true){
            		if(vm.listOrder.length > 0)
            		for(var i=0; i < vm.listOrder.length ;i++){
                		vm.listOrder[i].createdDate =null;
                    	goodsPlanService.doSearchReqGoodsDetail(vm.listOrder[i]).then(function (dataItem) {
                            	if(dataItem.length >0){
                            		var check = checkDups(dataItem);
                            		if(check){
                    					toastr.warning(CommonService.translate("Mã tồn tại trong lưới!"));
                    				}else {
                    					for(var k=0; k < dataItem.length; k++){
                    						dataItem[k].record = k+1;
                    						vm.listDataObj.push(dataItem[k]);
                    						$("#retrievalGridOpEdit").data("kendoGrid").dataSource.data(vm.listDataObj);
                    						fillDataTablePatternOption(vm.listDataObj);
                    		                CommonService.dismissPopup1();
                    					}
                    				}
                            	}
                            }, function (error) {
                                toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
                            });
                	}
            	} else {
            		if(vm.listDataCheckAll != null)
            		for(var i=0; i < vm.listDataCheckAll.length;i++){
            			vm.listDataCheckAll[i].createdDate =null;
                    	goodsPlanService.doSearchReqGoodsDetail(vm.listDataCheckAll[i]).then(function (dataItem) {
                            	if(dataItem.length >0){
                            		var check = checkDups(dataItem);
                            		if(check){
                    					toastr.warning(CommonService.translate("Mã tồn tại trong lưới!"));
                    				}else {
                    					for(var k=0; k < dataItem.length; k++){
                    						dataItem[k].record = k+1;
                    						vm.listDataObj.push(dataItem[k]);
                    						$("#retrievalGridOpEdit").data("kendoGrid").dataSource.data(vm.listDataObj);
                    		                fillDataTablePattern(vm.listDataObj);
                    		                CommonService.dismissPopup1();
                    					}
                    				}
                            	}
                            }, function (error) {
                                toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
                            });
            		}
            	}
        	}
        	
        	
        }
        vm.searchFormData={};
        function populateDataToSaveVT() {
        	if(vm.checkUpdate != true){
        		var dataGoodCheckFromGrid = $('#retrievalGridOp').data("kendoGrid").dataSource.data();
            	var list = $("#retrievalGridOp").data("kendoGrid");
//            	vm.searchFormData.listData = list._data;
            	vm.searchFormData.listData = dataGoodCheckFromGrid;
                var data = vm.searchFormData;
        	} else {
        		var dataGoodCheckFromGrid = $('#retrievalGridOpEdit').data("kendoGrid").dataSource.data();
            	var list = $("#retrievalGridOpEdit").data("kendoGrid");
//            	vm.searchFormUpdate.listData = list._data;
            	vm.searchFormUpdate.listData = dataGoodCheckFromGrid;
                var data = vm.searchFormUpdate;
        	}
        	
            if(data.code == "" ||  data.code==null){
            	toastr.warning("Mã kế hoạch không được để trống!");
            	$("#codePlan").focus();
            	return;
            }
            if(data.name =="" || data.name ==null){
            	toastr.warning("Tên kế hoạch không được để trống!");
            	$("#namePlan").focus();
            	return;
            }
            if(data.baseContent =="" || data.baseContent ==null){
            	toastr.warning("Căn cứ không được để trống!");
            	$("#baseContentPlan").focus();
            	return;
            }
            if(data.performContent =="" || data.performContent ==null){
            	toastr.warning("Thực hiện không được để trống!");
            	$("#performContentPlan").focus();
            	return;
            }
            if(!data.listData.length > 0){
            	toastr.warning("Vui lòng thêm dữ liệu trên lưới!");
            	return;
            }
            for (var i = 0; i < dataGoodCheckFromGrid.length; i++) {
        		if (dataGoodCheckFromGrid[i].expectedDate == null || dataGoodCheckFromGrid[i].expectedDate == "") {
					toastr.warning("Ngày đảm bảo Kpi không được để trống!");
					list.editCell(list.tbody.find('tr:eq(' + i + ')').find("td").eq(6));
					return;
					break;
				} 
        	}
            return data;
        }
        
        // clear data
        vm.selectedDeptChoose = false;
        vm.changeDataAuto = changeDataAuto
        function changeDataAuto(id) {
            switch (id) {
                case 'req':
                    {
                        if (processSearch(id, vm.selectedDept1)) {
                            vm.searchForm.constructionCode = null;
                            vm.selectedDept1 = false;
                        }
                        break;
                    }
                case 'rec':
                    {
                        if (processSearch(id, vm.selectedDept2)) {
                            vm.searchForm.constructionCode = null;
                            vm.selectedDept2 = false;
                        }
                        break;
                    }
            }
        }
        vm.exportFileCons = function exportFile() {
        	vm.searchForm.page = null;
        	vm.searchForm.pageSize = null;
    		var data = goodsPlanService.doSearchAll(vm.searchForm);
    		console.log(data);
    		goodsPlanService.doSearchAll(vm.searchForm).then(function(d){
    			CommonService.exportFile(vm.retrievalGrid,d.data,vm.listRemove,vm.listConvert,"Danh sách kế hoạch vật tư");
    		});
    	}
        vm.listRemove=[{
    		title: "Thao tác",
    	},{
    		title:"<input type='checkbox' id='checkalllistchange' name='gridchkselectalllistchange' ng-click='vm.signCheckAll();' ng-model='vm.signAll' />"
    	}]
        
        vm.listConvert=[{
			field:"signState",
			data:{
				1:'Hiệu lực',
				2: 'Đang trình ký',
                3: 'Đã trình ký',
                4: 'Đã từ chối'
			}
		}]
        
        vm.changeDataDept = changeDataDept
        function changeDataDept(id) {
            switch (id) {
                case 'dept':
                    {
                        if (processSearch(id, vm.selectedDept11)) {

                            vm.obj.constructionName = null;
                            vm.obj.constructionCode = null;
                            vm.obj.constructionId = null;
                            vm.selectedDept11 = false;
                        }
                        break;
                    }
                case 'dept4':
                    {
                        if (processSearch(id, vm.selectedDept4)) {

                            vm.obj.requestGroupName = null;
                            vm.obj.requestGroupCode = null;
                            vm.obj.requestGroupId = null;
                            vm.selectedDept4 = false;
                        }
                        break;
                    }
                case 'dept3':
                    {
                        if (processSearch(id, vm.selectedDept3)) {

                            vm.obj.receiveGroupName = null;
                            vm.obj.receiveGroupCode = null;
                            vm.obj.receiveGroupId = null;
                            vm.selectedDept3 = false;

                        }
                        break;
                    }
            }
        }
        
        
        vm.handleCheck = handleCheck;
        function handleCheck(dataItem) {
        	if (dataItem.selected) {
        		vm.listOrder.push(dataItem);
        	} else {
        		if(vm.listOrder.length > 0){
        			for(var i=0; i< vm.listOrder.length; i++){
        				if((vm.listOrder[i].requestGoodsId == dataItem.requestGoodsId)){
        					vm.listOrder.splice(i, 1);
        				} else {
        					vm.listOrder[i].selected = true;
        				}
        			}
        		}
        	}
		}
        
        vm.signCheck = signCheck;
        function signCheck(dataItem) {
        	if (dataItem.selected) {
        		vm.listCheck.push(dataItem);
        		} else {
        			if(vm.listCheck.length > 0){
            			for(var i=0; i< vm.listCheck.length; i++){
            				if((vm.listCheck[i].goodsPlanId == dataItem.goodsPlanId)){
            					vm.listCheck.splice(i, 1);
            				}
            			}
        			}
        			if(vm.listCheck.data.length > 0){
            			for(var i=0; i< vm.listCheck.data.length; i++){
            				if((vm.listCheck.data[i].goodsPlanId == dataItem.goodsPlanId)){
            					vm.listCheck.data.splice(i, 1);
            				}
            			}
        			}
        			
        	}
		}
        vm.openDepartmentTo = openDepartmentTo

        function openDepartmentTo(popUp) {
            vm.obj = {};
            vm.departmentpopUp = popUp;
            var templateUrl = 'coms/RPConstructionDK/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
        }
        vm.openCatProvincePopup = openCatProvincePopup;
        vm.onSaveCatProvince = onSaveCatProvince;
//        vm.onSaveContract= onSaveContract;
        vm.clearProvince = clearProvince;
        function openCatProvincePopup() {
            var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm tỉnh");
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions, data, vm, 'fff', 'ggfd', false, '85%', '85%', 'catProvinceSearchController');
        }
        vm.openContractOut = function() {
        	var templateUrl = 'coms/popup/findContractPopUp.html';
        	var title = gettextCatalog.getString("Tìm kiếm hợp đồng");
        	htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,null, vm, 'fff', 'ggfd', false, '85%','85%','contractSearchController');
        }
        vm.openComsConstructionPopup = openComsConstructionPopup;
        function openComsConstructionPopup(){
        	var templateUrl = 'coms/popup/comsConstructionSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm công trình");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','comsConstructionSearchController');
        }
        function onSaveCatProvince(data) {
            vm.searchFormPopup.catProvinceId = data.catProvinceId;
            vm.searchFormPopup.catProvinceCode = data.code;
            vm.searchFormPopup.catProvinceName = data.name;
            htmlCommonService.dismissPopup();
            $("#provincename").focus();
        };
        function clearProvince() {
            vm.searchFormPopup.catProvinceId = null;
            vm.searchFormPopup.catProvinceCode = null;
            vm.searchFormPopup.catProvinceName = null;
            $("#provincename").focus();
        }
        vm.onSaveContract = function(data) {
            vm.searchFormPopup.cntContractId = data.cntContractId;
            vm.searchFormPopup.cntContractCode = data.code;
            htmlCommonService.dismissPopup();
             $("#constructionType").focus();
        }
        
        vm.clearCntContract=clearCntContract;
        function clearCntContract() {
            vm.searchFormPopup.cntContractCode = null;
            vm.searchFormPopup.cntContractId = null;
            $("#cntContract").focus();
        }
        vm.clearDate =clearDate;
        function clearDate() {
            vm.searchFormPopup.startDate = null;
            vm.searchFormPopup.endDate = null;
            $("#startDate").focus();
        }
        
        vm.clearToStartDate=clearToStartDate;
        function clearToStartDate(){
        	 vm.searchFormPopup.toStartDate = null;
        }
        
        vm.clearToEndDate=clearToEndDate;
        function clearToEndDate(){
        	 vm.searchFormPopup.toEndDate = null;
        }
        
        vm.cancelInput = function (param) {
            if (param == 'dept') {
                vm.searchFormPopup.sysGroupId = null;
                vm.searchFormPopup.sysGroupName = null;
            }
        }
        vm.onSave = onSave;
function onSave(data) {
    if (vm.departmentpopUp === 'dept') {
        vm.searchFormPopup.sysGroupName = data.text;
        vm.searchFormPopup.sysGroupId = data.id;
    }
}
vm.onSaveCatConstructionType = onSaveCatConstructionType;
function onSaveCatConstructionType(data){
	vm.searchFormPopup.constructiontypename = data.name;
	vm.searchFormPopup.catConstructionType = data.catConstructionTypeId;
    htmlCommonService.dismissPopup();
    $("#constructionType").focus();
};
vm.clearConstruction1 = clearConstruction1;
function clearConstruction1() {
    vm.searchFormPopup.constructionName = null;
    vm.searchFormPopup.constructionId = null;
    $("#constructionName").focus();
}
vm.onSaveComsConstruction = onSaveComsConstruction;
function onSaveComsConstruction(data){
    vm.searchFormPopup.constructionName = data.code;
    vm.searchFormPopup.constructionId = data.constructionId;
    htmlCommonService.dismissPopup();
    $("#constructionName").focus();
};
//tim kiem cong trinh
vm.constructionOptions = {
        dataTextField: "code",
        dataValueField: "constructionId",
        placeholder: "Nhập mã hoặc tên công trình",
        select: function (e) {
            vm.isSelect = true;
            var dataItem = this.dataItem(e.item.index());
            vm.searchFormPopup.constructionName = dataItem.code;
            vm.searchFormPopup.constructionId = dataItem.constructionId;
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
                    return Restangular.all("constructionTaskService/rpDailyTaskConstruction").post({
                        keySearch: vm.searchFormPopup.constructionName,
                        catConstructionTypeId: vm.searchFormPopup.catConstructionTypeId,
                        pageSize: vm.constructionOptions.pageSize,
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
            '<p class="col-md-6 text-header-auto border-right-ccc">Mã công trình</p>' +
            '<p class="col-md-6 text-header-auto">Tên công trình</p>' +
            '</div>',
        template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.code #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden"> #: data.name #</div> </div>',
        change: function (e) {
        	if (!vm.isSelect) {
                vm.searchFormPopup.constructionName = null;
                vm.searchFormPopup.constructionId = null;
            }
        },
        close: function (e) {
            if (!vm.isSelect) {
                vm.searchFormPopup.constructionName = null;
                vm.searchFormPopup.constructionId = null;
            }
        }
    }
        vm.patternContractOutOptions={
            dataTextField: "code", placeholder:"Mã hợp đồng",
            open: function(e) {
                vm.isSelect = false;
                
            },
            select: function(e) {
                vm.isSelect = true;
                data = this.dataItem(e.item.index());
                vm.searchFormPopup.cntContractCode = data.code;
                vm.searchFormPopup.cntContractId = data.cntContractId;
            },
            
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function(options) {
                        var objSearch = {isSize: true, code:$('#cntContract').val(), contractType:0};
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
            change: function (e) {
                if (!vm.isSelect) {
                    vm.searchFormPopup.cntContractId = null;
                    vm.searchFormPopup.cntContractCode = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                	vm.searchFormPopup.cntContractId = null;
                    vm.searchFormPopup.cntContractCode = null;
                }
            }
        };
        vm.provinceOptions = {
            dataTextField: "name",
            dataValueField: "id",
            placeholder: "Nhập mã hoặc tên tỉnh",
            select: function (e) {
                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.searchFormPopup.catProvinceId = dataItem.catProvinceId;
                vm.searchFormPopup.catProvinceCode = dataItem.code;
                vm.searchFormPopup.catProvinceName = dataItem.name;
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
                            name: vm.searchFormPopup.catProvinceName,
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
                    vm.searchFormPopup.catProvinceId = null;
                    vm.searchFormPopup.catProvinceCode = null;
                    vm.searchFormPopup.catProvinceName = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.searchFormPopup.catProvinceId = null;
                    vm.searchFormPopup.catProvinceCode = null;
                    vm.searchFormPopup.catProvinceName = null;
                }
            }
        }
        //Huypq-20190326-start
        vm.addRow = function(){
        	var grid = $("#retrievalGridOp").data("kendoGrid");
        
            grid.dataSource.insert(0, { stt: "", constructionCode: null,cntContractCode:null,
            	goodsName:null,catUnitName:null,quantity:null,expectedDate:null,description:null,actions:"" });
            
                grid.editRow($("table[role='grid'] tbody tr:eq(0)"));
        }
        
        vm.editRowNewPopup = function(dataItem){
        	vm.rowData = dataItem;
        	vm.rowData.expectedDate = kendo.parseDate(dataItem.expectedDate,"dd/MM/yyyy");
        	var teamplateUrl = "coms/goodsPlan/popupEditAddNewRow.html";
            var title = "Cập nhật vật tư";
            var windowId = "CONSTRUCTION_LAND_HANDOVER_PLAN_ROW";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '500', "code");
        }
        
        vm.catUnitOptions={
				dataTextField: "catUnitName", placeholder:"Nhập mã đơn vị hoặc tên đơn vị tính",
	            select: function(e) {
	                var dataItem = this.dataItem(e.item.index());
	                vm.rowData.catUnitName = dataItem.catUnitName;
	                vm.rowData.catUnitId = dataItem.catUnitId;
	            },
	            pageSize: 10,
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function(options) {
	                        return Restangular.all("goodsPlanService/getForAutoCompleteUnit").post({pageSize:10, page:1, keySearch:$("#unitPlan").val().trim()}).then(function(response){
	                            options.success(response);
	                        }).catch(function (err) {
	                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
	                        });
	                    }
	                }
	            },
	            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
	            '<p class="col-md-12 text-header-auto">Tên đơn vị</p>' +
	            	'</div>',
	            template:'<div class="row" ><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.catUnitName #</div> </div>',
	            change: function(e) {
	            	console.log(  console.log(vm.rowData.catUnitName));
	            	if(processSearch('unitPlan',vm.selectedPrpject)){
						 $('#unitPlan').val(null);
						  vm.selectedPrpject=false;	
						  }
	            },
	            close: function(e) {
	                // handle the event0
	              }
			};
        
        function genAutoContractCode(data){
        	goodsPlanService.genDataContract({constructionCode: data.code}).then(function(result){
        		if (result.error) {
                    toastr.error(result.error);
                    return;
                }
        		vm.rowData.cntContractCode = result.cntContractCode;
        		vm.rowData.cntContractId = result.cntContractId;
        	}, function() {
    			toastr.error("Lỗi rồi !");
    		});
        	
        }
        
        vm.selectedDept11 = false;
        vm.constructionRowOptions = {
            dataTextField: "code",
            dataValueField: "constructionId",
            placeholder: "Nhập mã/tên công trình",
            select: function (e) {
                vm.selectedDept11 = true;
                var data = this.dataItem(e.item.index());
                vm.rowData.constructionCode = data.code;
                vm.rowData.constructionId = data.constructionId;
                genAutoContractCode(data);
            },
            pageSize: 10,
            open: function (e) {
                vm.selectedDept11 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedDept11 = false;
                        return Restangular.all("constructionTaskService/construction/searchConstructionDSTH").post({
                            keySearch: vm.rowData.constructionCode,
                            pageSize: vm.constructionRowOptions.pageSize,
                            page: 1
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.code #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                	vm.rowData.constructionCode = null;
                    vm.rowData.constructionId = null;

                }
            },
            ignoreCase: false
        }
        
        vm.saveRow = function(){
        	if(vm.rowData.constructionCode==null){
        		toastr.warning("Mã công trình không được để trống !");
        		$("#consPlan").focus();
        		return;
        	}
        	if(vm.rowData.cntContractCode==null){
        		toastr.warning("Mã CV/HD không được để trống !");
        		$("#contractPlan").focus();
        		return;
        	}
        	if(vm.rowData.goodsName==null){
        		toastr.warning("Tên vật tư thiết bị không được để trống !");
        		$("#namePlan").focus();
        		return;
        	}
        	if(vm.rowData.catUnitName==null){
        		toastr.warning("Đơn vị không được để trống !");
        		$("#unitPlan").focus();
        		return;
        	}
        	if(vm.rowData.quantity==null){
        		toastr.warning("Số lượng không được để trống !");
        		$("#quantityPlan").focus();
        		return;
        	}
        	if(vm.rowData.expectedDate==null){
        		toastr.warning("Ngày đảm bảo theo KPI không được để trống !");
        		$("#datePlan").focus();
        		return;
        	}
        	
        	CommonService.dismissPopup1();
        	$("#retrievalGridOp").data("kendoGrid").refresh();
        }
        
        vm.resetFormAdd = resetFormAdd;
        function resetFormAdd(data){
        	if(data=='constructionCode'){
        		vm.rowData.constructionCode=null;
        		vm.rowData.constructionId=null;
        	}
        	if(data=='cntContractCode'){
        		vm.rowData.cntContractCode = null;
        	}
        	if(data=='goodsName'){
        		vm.rowData.goodsName = null;
        	} 
        	if(data=='catUnitName'){
        		vm.rowData.catUnitName = null;
        		vm.rowData.catUnitId = null;
        	}
        	if(data=='quantity'){
        		vm.rowData.quantity = null;
        	}
        	if(data=='expectedDate'){
        		vm.rowData.expectedDate = null;
        	}
        	if(data=='description'){
        		vm.rowData.description = null;
        	}
        	
        	
        }
        //Huypq-end
    }
})();
