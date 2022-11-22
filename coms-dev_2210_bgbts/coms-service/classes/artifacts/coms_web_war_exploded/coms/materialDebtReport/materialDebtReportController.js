/**
 * Created by pm1_os24 on 3/1/2018.
 */
(function() {
    'use strict';
    var controllerId = 'materialDebtReportController';

    angular.module('MetronicApp').controller(controllerId, materialDebtReportController);

    function materialDebtReportController($scope, $rootScope, $timeout, gettextCatalog,
                                      kendoConfig, $kWindow,
                                      CommonService, PopupConst, Restangular, RestEndpoint,Constant,$http, htmlCommonService) {
        var vm = this;
        vm.openUser = openUser;
        vm.onSaveSysUser = onSaveSysUser;
//        vm.openConstruction = openConstruction;
        vm.clearUser = clearUser;
        vm.clearProvince = clearProvince;
        vm.clearConstruction = clearConstruction;
        vm.openCatProvincePopup = openCatProvincePopup;
		vm.onSaveCatProvince = onSaveCatProvince;
		vm.openComsConstructionPopup = openComsConstructionPopup;
		vm.onSaveComsConstruction = onSaveComsConstruction;
        vm.searchForm={
            obstructedState:1
        };
        vm.String="Quản lý công trình > Báo cáo > Công nợ vật tư";
        vm.searchForm.dateBD=setCurrentDay();
        function setCurrentDay(){
        	var today=new Date();
        	return today.getDate()+'/'+(today.getMonth()+1)+'/'+today.getFullYear();
        }
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
        	fillDataTable1();
//        	$("#searchForm1").data("kendoGrid").dataSource.read(); 
//        	$("#searchForm1").data("kendoGrid").refresh()
//            
//            fillDataTable2();
            initDropDownList();
            vm.rpDayQuantity = {};
        }


        function initDropDownList(){
            vm.progressOptions={
                dataSource:[
                    {id:1,name:"Vượt chỉ tiêu"},
                    {id:2,name:"Chưa vượt chỉ tiêu"}
                ],
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true
            }
        }

        vm.listYearPlan=[];

        vm.validatorOptions = kendoConfig.get('validatorOptions');
        setTimeout(function(){
            $("#keySearch").focus();
        },15);
        var record=0;

        vm.doSearch = function(){
        	if(vm.searchForm.dateBD==""){
        		vm.searchForm.dateBD=null;
        	} 
        	if(vm.searchForm.dateKT==""){
        		vm.searchForm.dateKT=null;
        	}
            vm.searchFormGrid1.dataSource.page(1);
            vm.searchFormGrid2.dataSource.page(1);
        	
        };

        vm.openSysGroup=openSysGroup

        function openSysGroup(popUp){
            vm.obj={};
            vm.departmentpopUp=popUp;
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null,null, vm, popUp, 'string', false, '92%','89%');
        }
        
        vm.provinceOptions = {
        	    dataTextField: "name",
        	    dataValueField: "id",
        		placeholder:"Nhập mã hoặc tên tỉnh",
        	    select: function (e) {
        	        vm.isSelect = true;
        	        var dataItem = this.dataItem(e.item.index());
        	        vm.searchForm.provinceId = dataItem.catProvinceId;
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
        	            vm.searchForm.provinceId = null;
        	            vm.searchForm.catProvinceCode = null;
        				vm.searchForm.catProvinceName = null;
        	        }
        	    },
        	    close: function (e) {
        	        if (!vm.isSelect) {
        	            vm.searchForm.provinceId = null;
        	            vm.searchForm.catProvinceCode = null;
        				vm.searchForm.catProvinceName = null;
        	        }
        	    }
        	}
        
        function openCatProvincePopup(){
        	var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
        	var title = gettextCatalog.getString("Tìm kiếm tỉnh");
        	htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
        }
        
        
        function onSaveCatProvince(data){
            vm.searchForm.provinceId = data.catProvinceId;
            vm.searchForm.catProvinceCode = data.code;
        	vm.searchForm.catProvinceName = data.name;
            htmlCommonService.dismissPopup();
            $("#provincename").focus();
        };
        
        function clearProvince (){
        	vm.searchForm.provinceId = null;
        	vm.searchForm.catProvinceCode = null;
        	vm.searchForm.catProvinceName = null;
        	$("#provincename").focus();
        }
        
        
        
        //tim kiem cong trinh
        vm.constructionOptions = {
                dataTextField: "code",
                dataValueField: "constructionId",
                placeholder: "Nhập mã hoặc tên công trình",
                select: function (e) {
                    vm.isSelect = true;
                    var dataItem = this.dataItem(e.item.index());
                    vm.searchForm.constructionName = dataItem.code;
                    vm.searchForm.constructionId = dataItem.constructionId;
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
                                keySearch: vm.searchForm.constructionName,
                                catConstructionTypeId: vm.searchForm.catConstructionTypeId,
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
                        vm.searchForm.constructionName = null;
                        vm.searchForm.constructionId = null;
                    }
                },
                close: function (e) {
                    if (!vm.isSelect) {
                        vm.searchForm.constructionName = null;
                        vm.searchForm.constructionId = null;
                    }
                }
            }
        
        function openComsConstructionPopup(){
        	var templateUrl = 'coms/popup/comsConstructionSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm công trình");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','comsConstructionSearchController');
        }
        
        function clearConstruction() {
            vm.searchForm.constructionName = null;
            vm.searchForm.constructionId = null;
            $("#constructionName").focus();
        }
        
        function onSaveComsConstruction(data){
            vm.searchForm.constructionName = data.code;
            vm.searchForm.constructionId = data.constructionId;
            htmlCommonService.dismissPopup();
            $("#constructionName").focus();
	    };
	    
	    //tim kiem nhan vien
	    vm.patternSignerOptions = {
	            dataTextField: "fullName",
	            placeholder: "Nhập mã hoặc tên người thực hiện",
	            open: function (e) {
	                vm.isSelect = false;
	            },
	            select: function (e) {
	                vm.isSelect = true;
	                data = this.dataItem(e.item.index());
	                vm.searchForm.userName = data.fullName;
	                vm.searchForm.sysUserId = data.sysUserId;
	            },
	            pageSize: 10,
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function (options) {
	                        vm.isSelect = false;
	                        return Restangular.all("sysUserCOMSRsService/getForAutoCompleteInSign").post({ pageSize: 10, page: 1, fullName: $("#signerGroup").val().trim() }).then(function (response) {
	                            options.success(response);
	                            if (response == [] || $("#signerGroup").val().trim() == "") {
	                                vm.searchForm.userName = null;
	                                vm.searchForm.sysUserId = null;
	                            }
	                        }).catch(function (err) {
	                            vm.searchForm.userName = null;
	                            vm.searchForm.sysUserId = null;
	                        });
	                    }
	                }
	            },
	            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
	                '<p class="col-md-6 text-header-auto border-right-ccc">Mã nhân viên</p>' +
	                '<p class="col-md-6 text-header-auto">Họ tên</p>' +
	                '</div>',
	            template: '<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.employeeCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
	            change: function (e) {
	            	if (!vm.isSelect) {
	                    vm.searchForm.userName = null;
	                    vm.searchForm.sysUserId = null;
	                }
	            },
	            close: function (e) {
	                if (!vm.isSelect) {
	                    vm.searchForm.userName = null;
	                    vm.searchForm.sysUserId = null;
	                }
	            }
	        };
	    
	    function openUser() {
            var templateUrl = 'coms/popup/sysUserSearchPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm nhân viên");
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions, data, vm, 'fff', 'ggfd', false, '85%', '85%', 'sysUserSearchController');
        }

        function onSaveSysUser(data) {
            vm.searchForm.userName = data.fullName;
            vm.searchForm.sysUserId = data.sysUserId;
            htmlCommonService.dismissPopup();
            $("#signerGroup").focus();
        };
        
        function clearUser() {
            vm.searchForm.userName = null;
            vm.searchForm.sysUserId = null;
            $("#signerGroup").focus();
        }
	    
      //HuyPQ-25/08/2018-edit-start
        vm.exportexcel= exportexcel;
        function exportexcel(dataSearch){
        	function displayLoading(target) {
        	      var element = $(target);
        	      kendo.ui.progress(element, true);
        	      setTimeout(function(){
        	    	  
        	    	  return Restangular.all("materialDebtReportService/exportReport").post(dataSearch).then(function (d) {
        	                var data = d.plain();
        	                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	                kendo.ui.progress(element, false);
        	    	  }).catch(function (e) {
        	    		  kendo.ui.progress(element, false);
        	                toastr.error(gettextCatalog.getString("Lỗi khi xuất file report!"));
        	                return;
        	            });
        		});
        			
        	  }
        		displayLoading(".tab-content");
        }
      //HuyPQ-edit-end

        vm.rpDayQuantity.obstructedState = 1;
        vm.option1Show = true;

        vm.checkboxAction = checkboxAction;

        function checkboxAction(e) {
            if (e == 1) {
                initRPDayQuantityRadioList();
                vm.option1Show = true;
            } else if (e == 2) {
                initRPDayQuantityRadioList();
                vm.option2Show = true;
            }
            vm.rpDayQuantity.obstructedState = e;
        }

        function initRPDayQuantityRadioList() {
            vm.option1Show = false;
            vm.option2Show = false;
        }

        function fillDataTable1() {
            vm.gridOptions1 = kendoConfig.getGridOptions({
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
                        template: '<div style="margin:0;" class="btn-group pull-right margin_top_button margin10 ">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.searchFormGrid1.columns| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideEntangledGrid1Column(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                        	$("#countRPMQuantity").text(""+response.total);
                        	
                            vm.count1 = response.total;                           
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        },
                    },

                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "materialDebtReportService/doSearchForReport",
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
                    }, {
                        title: "Đơn vị",
                        field: 'sysGroupName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },{
                        title: "Tỉnh",
                        field: 'provinceName',
                        width: '9%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },{
                        title: "Công trình",
                        field: 'constructionCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },{
                        title: "Nhân viên",
                        field: 'sysUserName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },{
                        title: "Nguồn hàng",
                        field: 'sourceType',
                        width: '9%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },{
                        title: "Mã VTTB",
                        field: 'goodsCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        }
                    },{
                        title: "Tên VTTB",
                        field: 'goodsName',
                        width: '12%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },{
                        title: "Tình trạng",
                        field: 'state',
                        width: '7%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },{
                        title: "Số lượng",
                        field: 'amount',
                        width: '8%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },{
                        title: "ĐVT",
                        field: 'catUnitName',
                        width: '5%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },{
                        title: "Giá trị",
                        field: 'totalMoney',
                        width: '10%',
                        format: "{0:n4}",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                ]
            });
        }

        function fillDataTable2() {
            vm.gridOptions2 = kendoConfig.getGridOptions({
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
                        template: '<div class="btn-group pull-right margin_top_button margin10 ">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.searchFormGrid2.columns| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideEntangledGrid2Column(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                        	$("#countRPMQuantityDetail").text(""+response.total);
                            vm.count2 = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "materialDebtReportService/doSearchDetailForReport",
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
                          }, {
                              title: "Đơn vị",
                              field: 'sysGroupName',
                              width: '10%',
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:left;"
                              }
                          },{
                              title: "Tỉnh",
                              field: 'provinceName',
                              width: '9%',
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:left;"
                              }
                          },{
                              title: "Công trình",
                              field: 'constructionName',
                              width: '10%',
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:left;"
                              }
                          },{
                              title: "Nhân viên",
                              field: 'sysUserName',
                              width: '10%',
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:left;"
                              }
                          },{
                              title: "Nguồn hàng",
                              field: 'sourceType',
                              width: '9%',
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:left;"
                              }
                          },{
                              title: "Mã VTTB",
                              field: 'goodsCode',
                              width: '10%',
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:right;"
                              }
                          },{
                              title: "Tên VTTB",
                              field: 'goodsName',
                              width: '12%',
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:left;"
                              }
                          },{
                              title: "Tình trạng",
                              field: 'status',
                              width: '7%',
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:left;"
                              }
                          },{
                              title: "Serial",
                              field: 'serial',
                              width: '8%',
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:left;"
                              }
                          },{
                              title: "Mã hợp đồng",
                              field: 'contractCode',
                              width: '8%',
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:left;"
                              }
                          },{
                              title: "Part number",
                              field: 'partNumber',
                              width: '8%',
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:left;"
                              }
                          },{
                              title: "Hãng sản xuất",
                              field: 'manufacturerName',
                              width: '8%',
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:left;"
                              }
                          },{
                              title: "Nước sản xuất",
                              field: 'producingCountryName',
                              width: '8%',
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:left;"
                              }
                          },{
                              title: "Số lượng",
                              field: 'amount',
                              width: '8%',
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:left;"
                              }
                          },{
                              title: "ĐVT",
                              field: 'unitName',
                              width: '5%',
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:left;"
                              }
                          },{
                              title: "Giá trị",
                              field: 'totalMoney',
                              width: '10%',
                              format: "{0:n4}",
                              headerAttributes: {style: "text-align:center;font-weight: bold;"},
                              attributes: {
                                  style: "text-align:left;"
                              }
                          },
                      ]
            });
        }
        vm.showHideEntangledGrid1Column = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.searchFormGrid1.hideColumn(column);
            } else if (column.hidden) {
                vm.searchFormGrid1.showColumn(column);
            } else {
                vm.searchFormGrid1.hideColumn(column);
            }


        }
        vm.showHideEntangledGrid2Column = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.searchFormGrid2.hideColumn(column);
            } else if (column.hidden) {
                vm.searchFormGrid2.showColumn(column);
            } else {
                vm.searchFormGrid2.hideColumn(column);
            }


        }
        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };
        function numberWithCommas(x) {
            if(x == null || x == undefined){
                return '';
            }
            x = x/1000000;
            var parts = x.toFixed(2).toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
        }

        vm.selectedDept1=false;
        vm.deprtOptions1 = {
            dataTextField: "text",
            dataValueField:"id",
            select: function(e) {
                vm.selectedDept1=true;
                var dataItem = this.dataItem(e.item.index());
                vm.searchForm.sysGroupName = dataItem.text;
                vm.searchForm.sysGroupId = dataItem.id;
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
                        return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({name:vm.searchForm.sysGroupName,pageSize:vm.deprtOptions1.pageSize}).then(function(response){
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
                    vm.searchForm.sysGroupName = null;// thành name
                    vm.searchForm.sysGroupId = null;
                }
            },
            ignoreCase: false
        }

        vm.openDepartmentTo1=openDepartmentTo1
        function openDepartmentTo1(popUp){
            vm.obj={};
            vm.departmentpopUp=popUp;
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null,null, vm, popUp, 'string', false, '92%','89%');
        }
        vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'dept') {
                vm.searchForm.sysGroupName = data.text;
                vm.searchForm.sysGroupId = data.id;
            }
        }
       vm.DeleteDB = DeleteDB;
       function DeleteDB(d){
                d.sysGroupName = null ;
                d.sysGroupId= null;
       }

        vm.deleteListData = deleteListData;
        function deleteListData(){           
                vm.searchForm.dateBD = null;           
                vm.searchForm.dateKT = null;            
        }
        // HuyPQ-25/08/2018-start
        vm.exportpdf = function(searchForm){
        	function displayLoading(target) {
        	      var element = $(target);
        	      kendo.ui.progress(element, true);
        	      setTimeout(function(){
        	    	  
        	    	  if(searchForm.obstructedState==1){
        	                var obj={};
        	                obj = searchForm;
        	                obj.reportType = "DOC";
        	                obj.reportName = "bao_cao_san_luong_theo_ngay";

        	                $http({url: RestEndpoint.BASE_SERVICE_URL + "materialDebtReportService"+"/exportPdfSLTN",
        	                    dataType: 'json',
        	                    method: 'POST',
        	                    data: obj,
        	                    headers: {
        	                        "Content-Type": "application/json"
        	                    },
        	                    responseType : 'arraybuffer'// THIS IS IMPORTANT
        	                }).success(function(data, status, headers, config){
        	                    if(data.error){
        	                    	kendo.ui.progress(element, false);
        	                        toastr.error(data.error);
        	                    } else {
        	                    	kendo.ui.progress(element, false);
        	                         var binarydata= new Blob([data],{type: "text/plain;charset=utf-8"});
        	                kendo.saveAs({dataURI: binarydata, fileName: "bao_cao_san_luong_theo_ngay" + '.pdf'});
        	                    }

        	                })
        	                    .error(function(data){
        	                    	kendo.ui.progress(element, false);
        	                        toastr.error("Có lỗi xảy ra!");
        	                    });
        	                
        	            }
        	        if(searchForm.obstructedState==2){
        	                var obj={};
        	                obj = searchForm;
        	                obj.reportType = "DOC";
        	                obj.reportName = "bao_cao_san_luong_theo_ngay";

        	                $http({url: RestEndpoint.BASE_SERVICE_URL + "materialDebtReportService"+"/exportPdfSLTNCT",
        	                    dataType: 'json',
        	                    method: 'POST',
        	                    data: obj,
        	                    headers: {
        	                        "Content-Type": "application/json"
        	                    },
        	                    responseType : 'arraybuffer'// THIS IS IMPORTANT
        	                }).success(function(data, status, headers, config){
        	                    if(data.error){
        	                        toastr.error(data.error);
        	                        kendo.ui.progress(element, false);
        	                    } else {
        	                    	kendo.ui.progress(element, false);
        	                         var binarydata= new Blob([data],{type: "text/plain;charset=utf-8"});
        	                kendo.saveAs({dataURI: binarydata, fileName: "bao_cao_san_luong_theo_ngay" + '.pdf'});
        	                    }
        	                    
        	                })
        	                    .error(function(data){
        	                    	kendo.ui.progress(element, false);
        	                        toastr.error("Có lỗi xảy ra!");
        	                    });
        	                
        	            }
        		});
        			
        	  }
        		displayLoading(".tab-content");
        }
        //HuyPQ-end
    }
})();
