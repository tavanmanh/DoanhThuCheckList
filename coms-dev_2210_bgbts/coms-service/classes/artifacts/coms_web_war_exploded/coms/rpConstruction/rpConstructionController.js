//hungnx 06062018 start
(function() {
    'use strict';
    var controllerId = 'rpConstructionController';

    angular.module('MetronicApp').controller(controllerId, rpConstructionTaskController);

    function rpConstructionTaskController($scope, $rootScope, $timeout, gettextCatalog,$filter,
                                kendoConfig, $kWindow,
                                CommonService, PopupConst, Restangular, RestEndpoint,Constant,$http, constructionService,htmlCommonService) {

        var vm = this;
        vm.searchForm={};
        var record=0;
        vm.statusDataList = [
                             {id: 1, name: 'Chờ bàn giao mặt bằng'},
                             {id: 2, name: 'Chờ khởi công'},
                             {id: 3, name: 'Đang thực hiện'},
                             {id: 4, name: 'Đã tạm dừng'},
                             {id: 5, name: 'Đã hoàn thành'},
                             {id: 6, name: 'Đã nghiệm thu'},
                             {id: 7, name: 'Đã hoàn công'},
                             {id: 8, name: 'Đã quyết toán'},
                             {id: 0, name: 'Đã hủy'}
                         ];
        vm.String = "Quản lý công trình > Báo cáo > Chi tiết công trình";
        vm.disableBtnPDF = false;
        vm.disableBtnExcel = false;
        initCatConstructionType();
        function initCatConstructionType() {
            constructionService.getconstructionType().then(function (data) {
                vm.catConstructionTypeDataList = data;
            });
        }
            vm.constructionTaskGridOptions = kendoConfig.getGridOptions({
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
                            $("#appCount").text("" + response.total);
                            $timeout( function(){ vm.count = response.total; } );
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "constructionService/reportConstruction",
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
                        },
                    }, {
                        title: "Mã công trình",
                        field: 'code',
//                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    }, {
                        title: "Địa chỉ",
                        field: 'name',
//                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Mã nhà trạm",
                        field: 'catStationHouseCode',
//                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Mã trạm",
                        field: 'catStationCode',
//                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Loại công trình",
                        field: 'catContructionTypeName',
//                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    },  {
                        title: "Ngày khởi công",
                        field: 'startingDate',
//                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    }, {
                        title: "Ngày hoàn thành",
                        field: 'completeDate',
//                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    }, 
					{
                        title: "Giá trị",
                        field: 'valueComplete',
//                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    },
					{
                        title: "HĐXL đầu ra",
                        field: 'cntContractCode',
//                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Đơn vị thực hiện",
                        field: 'sysGroupName',
//                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                  //  {
				//		title: "Ngày bàn giao MB dự kiến",
			     //		field: 'groundPlanDate',
                 //     headerAttributes: {style: "text-align:center;font-weight: bold;"},
                 //       attributes: {
                 //           style: "text-align:center;"
                 //       },
                //    },
                //    {
                 //       title: "Ngày bàn giao MB thực tế",
                 //       field: 'handoverDate',
                //        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                //        attributes: {
                 //           style: "text-align:center;"
                  //      },
                 //   },
					{
                        title: "Trạng thái",
                        field: 'status',
//                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(dataItem) {
                        	if (dataItem.status == 1) {
                                return "<span name='status' font-weight: bold;'>Chờ bàn giao mặt bằng</span>"
                            } else if (dataItem.status == 2) {
                                return "<span name='status' font-weight: bold;'>Chờ khởi công</span>"
                            } else if (dataItem.status == 3) {
                                return "<span name='status' font-weight: bold;'>Đang thực hiện</span>"
                            } else if (dataItem.status == 4) {
                                return "<span name='status' font-weight: bold;'>Đã tạm dừng</span>"
                            } else if (dataItem.status == 5) {
                                return "<span name='status' font-weight: bold;'>Đã hoàn thành</span>"
                            } else if (dataItem.status == 6) {
                                return "<span name='status' font-weight: bold;'>Đã nghiệm thu</span>"
                            } else if (dataItem.status == 7) {
                                return "<span name='status' font-weight: bold;'>Đã hoàn công</span>"
                            } else if (dataItem.status == 8) {
                                return "<span name='status' font-weight: bold;'>Đã quyết toán</span>"
                            } else if (dataItem.status == 0) {
                                return "<span name='status' font-weight: bold;'>Đã hủy</span>"
                            } else {
                            	return "<span name='status' font-weight: bold;'></span>"
                            }
						}
                    }
    ]
    });

        function refreshGrid(d) {
            var grid = vm.constructionReport;
            if (grid) {
                grid.dataSource.data(d);
                grid.refresh();
            }
        }

        function formatDate(date) {
            var newdate = new Date(date);
            return kendo.toString(newdate, "dd/MM/yyyy");
        }

//        vm.openDepartmentTo1=openDepartmentTo1
//
//        function openDepartmentTo1(popUp){
//            vm.obj={};
//            vm.departmentpopUp=popUp;
//            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
//            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
//            CommonService.populatePopupDept(templateUrl, title, null,null, vm, popUp, 'string', false, '92%','89%');
//        }

        vm.doSearch = function(){
//        	console.log('startFrom: ', vm.searchForm.startingDateFrom, '-- startTo: ', vm.searchForm.startingDateTo
//        			, '-- Complete From: '+vm.searchForm.completeDateFrom+ '-- Com To: '+vm.searchForm.completeDateTo);
        	if (!(htmlCommonService.validDate(vm.searchForm.startingDateFrom) || isStringEmpty(vm.searchForm.startingDateFrom))
        			|| !(htmlCommonService.validDate(vm.searchForm.startingDateTo) || isStringEmpty(vm.searchForm.startingDateTo))
        			|| !(htmlCommonService.validDate(vm.searchForm.completeDateFrom) || isStringEmpty(vm.searchForm.completeDateFrom))
        			|| !(htmlCommonService.validDate(vm.searchForm.completeDateTo) || isStringEmpty(vm.searchForm.completeDateTo)) ) {
        		toastr.error(gettextCatalog.getString("Định dạng ngày không đúng !"));
        	} else
        		vm.readFileStation();
        }

        vm.exportexcel= function(){
        	if (vm.disableBtnPDF)
        		return;
        	vm.disableBtnExcel = true;
        	$("#loadingReportConstr").addClass('loadersmall');
            return Restangular.all("constructionService/exportExcelConstructionReport").post(vm.searchForm).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                vm.disableBtnExcel = false;
                $("#loadingReportConstr").removeClass('loadersmall');
            }).catch(function (e) {
            	vm.disableBtnExcel = false;
                toastr.error(gettextCatalog.getString("Lỗi khi tải báo cáo !s"));
                $("#loadingReportConstr").removeClass('loadersmall');
            });

        }

        vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'dept') {
                vm.searchForm.sysGroupName = data.text;
                vm.searchForm.sysGroupId = data.id;
            }
        }

        vm.showHideEntangledGrid1Column = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.constructionReport.hideColumn(column);
            } else if (column.hidden) {
                vm.constructionReport.showColumn(column);
            } else {
                vm.constructionReport.hideColumn(column);
            }


        }

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };

        vm.deleteListData = deleteListData;
    function deleteListData(x){
        if(x==1){
        	vm.searchForm.startingDateFrom = null;
            vm.searchForm.startingDateTo = null;
        } else
        if(x==2){
            vm.searchForm.listCatConstructionType = null;
        } else
        if(x==3){
        	vm.searchForm.completeDateFrom =null;
        	vm.searchForm.completeDateTo =null;
        }
        else if(x==4){
        	vm.searchForm.listStatus =null;
        } else if (x == 5) {
        	$("#fileSearchStation").val(null);
        	vm.searchForm.stationCodeLst = null;
        } else if (x == 7) {
        	vm.searchForm.cntContractCode = null;
        	vm.searchForm.keySearch = null;
        }
    }
        vm.monthSelectorOptions = {
            start: "year",
            depth: "year"
        };
    vm.exportpdf = function(){
    	if (vm.disableBtnExcel)
    		return;
    	vm.disableBtnPDF = true;
    	$("#loadingReportConstr").addClass('loadersmall');
        return Restangular.all("constructionService/exportPDFConstructionReport").post(vm.searchForm).then(function (d) {
            var data = d.plain();
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            vm.disableBtnPDF = false;
            $("#loadingReportConstr").removeClass('loadersmall');
        }).catch(function (e) {
            toastr.error(gettextCatalog.getString("Lỗi khi tải báo cáo!s"));
            vm.disableBtnPDF = false;
            $("#loadingReportConstr").removeClass('loadersmall');
        });;
    }
    vm.getStationCodeTemplate = function(){
		var fileName="Station_Code";
		CommonService.downloadTemplate2(fileName).then(function(d) {
		data = d.plain();
		window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
		}).catch( function(){
			toastr.error(gettextCatalog.getString("Lỗi khi export!"));
			return;
		});

	}
    vm.readFileStation = function() {
    	if (!$("#fileSearchStation")[0].files[0]) {
            callbackDoSearch();
            return;
        }
    	else if (($("#fileSearchStation")[0].files[0].name.split('.').pop() != 'xls' && $("#fileSearchStation")[0].files[0].name.split('.').pop() != 'xlsx')) {
    		toastr.warning("Sai định dạng file");
            callbackDoSearch();
            return;
    	}
        var formData = new FormData();
        formData.append('multipartFile', $('#fileSearchStation')[0].files[0]);
        return $.ajax({
            url: Constant.BASE_SERVICE_URL + "constructionService/readFileStationReport",
            type: "POST",
            data: formData,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                if (data.length === 0) {
                    toastr.warning("File import không có dữ liệu");
                    return;
                } else {
                	if (data.stationCodeLst.length > 0 ) {
                		vm.searchForm.stationCodeLst = data.stationCodeLst;
                	} 
                }
                callbackDoSearch();
            }, 
            error: function(data) {
				if (!!data.error) {
					 toastr.error("File import phải có định dạng xlsx !");
				}
			}
        });
	}
    function callbackDoSearch() {
    	vm.constructionReport.dataSource.page(1);
	}
    vm.patternContractOutOptions={
				dataTextField: "code", placeholder:"Mã hợp đồng",
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
	                    	var objSearch = {isSize: true, code:$('#cntContractOut').val().trim(), contractType:0};
	                        return Restangular.all(RestEndpoint.CNT_CONTRACT_SERVICE_URL+"/cntContract/doSearch").post(objSearch).then(function(response){
	                            options.success(response.data);
//	                            var check = response == [] || $("#"+proccessFormId("name")).val().trim() == "";
//	                            if(response == [] || $("#"+proccessFormId("name")).val().trim() == ""){
//	                            	 vm.cntContractMap.cntContractId =null;
//	                            }
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

//	            	if(vm.isSelect) {
//	            		if((vm.searchForm.cntContractCode != data.code)) {
//	            			$('#'+proccessFormId("cntContractOut")).val(null);
//	            			vm.searchForm.cntContractCode = null;
//	            		}
//		        			if(vm.searchForm.cntContractCode != data.code){
//		        				$('#'+proccessFormId("cntContractOut")).val(null);
//		        				vm.searchForm.cntContractCode  = null;
//		        			}
//	            	}
//	        		if(!vm.isSelect) {
//	        			vm.searchForm.cntContractCode = null;
//	        			$('#'+proccessFormId("cntContractOut")).val(null);
//	        		}
	            },
	            close: function(e) { 
	                // handle the event0
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
    function isStringEmpty(target) {
    	if (target == null || typeof target == undefined || target == '')
    		return true;
    	return false;
	}
  //hungnx 08062018 end  

	vm.openCatProvincePopup = openCatProvincePopup;
	vm.onSaveCatProvince = onSaveCatProvince;
	vm.clearProvince = clearProvince;
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
	function clearProvince (){
		vm.searchForm.catProvinceId = null;
		vm.searchForm.catProvinceCode = null;
		vm.searchForm.catProvinceName = null;
		$("#provincename").focus();
	}
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

}
})();
