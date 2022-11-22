(function () {
    'use strict';
    var controllerId = 'rpBTSController';

    angular.module('MetronicApp').controller(controllerId, rpBTSController);

    function rpBTSController($scope, $rootScope, $timeout, gettextCatalog,
                                kendoConfig, $kWindow, rpBTSService,
                                CommonService, PopupConst, Restangular, RestEndpoint, Constant, htmlCommonService) {
        var vm = this;
        vm.showSearch = true;
        vm.isCreateNew = false;
        vm.showDetail = false;
        vm.rpBTSSearch = {};
        vm.String = "Quản lý công trình > Báo cáo > Báo cáo BTS";
//        vm.readFileContract={};
//        vm.workItem = {};
        
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
            fillDataTable([]);
        }
        
        vm.validatorOptions = kendoConfig.get('validatorOptions');
        
        var record = 0;

        function fillDataTable(data) {
            vm.rpBTSGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save : function(){
                    vm.rpBTSGrid.refresh();
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: [
                    {
                        name: "actions",
                        template:'<div class="btn-group pull-right margin_top_button ">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">' +
                        '<label ng-repeat="column in vm.rpBTSGrid.columns.slice(1,vm.rpBTSGrid.columns.length)| filter: vm.gridColumnShowHideFilter">' +

                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#appCountBTS").text("" + response.total);
                            vm.countBTS = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "rpBTSService/doSearchBTS",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.rpBTSSearch.page = options.page
                            vm.rpBTSSearch.pageSize = options.pageSize
							
                            return JSON.stringify(vm.rpBTSSearch)
                        }
                    },
                    pageSize: 10,
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
                        width: '50px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                       

                    },
                    {
                        title: "Chi nhánh",
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                            	title: "Đơn vị",
                                field: 'chiNhanh',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            }, {
                            	title: "Mã tỉnh",
            					field: 'provinceCode',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            }, {
                            	title: "Mã nhà trạm",
            					field: 'cat_station_house_code',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            }, {
                            	title: "Mã hợp đồng",
            					field: 'cnt_contract_code',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                            }
                        ]
                    },
                    {
                        title: "Hạng  mục thi công xây dựng",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        columns: [{
                                title: "Đã có MB",
            					field: 'xddaCoMb',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                              
                            },
                            {
                                title: "Cần GPXD",
            					field: 'xdcanGPXD',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                            
                            },
                            {
                                title: "Đã có GPXD",
            					field: 'xddaCoGPXD',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                                
                            },
                            {
                                title: "Chưa có",
            					field: 'xdchuaCo',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                                
                            },
                            {
                                title: "Đủ đk nhận BGMB",
            					field: 'xdduDKNhanBGMB',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                                
                            },
                            {
                                title: "Đã nhận BGMB",
            					field: 'xddaNhanBGMB',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                                                            },
                            {
                                title: "Đủ đk chưa đi nhận",
            					field: 'xdduDKChuaDiNhan',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                               
                            },
							{
                                title: "Quá hạn BGMB",
            					field: 'quahan_BGMB_XD',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                               
                            },
                            {
                                title: "Đã vào KC",
            					field: 'xddaVaoTK',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                                
                            },
                            {
                                title: "Nhận chưa KC",
            					field: 'xdnhanChuaTK',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                               
                            },
							{
                                title: "Quá hạn KC",
            					field: 'quahan_khoicong_XD',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                               
                            },
                            {
                                title: "Đang TKXD dở dang",
            					field: 'xddangTKXDDoDang',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                               
                            },
                            {
                                title: "TC quá hạn",
            					field: 'xdtcquaHan',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                               
                            }
                        ]
                    },
                    {
                        title: "Hạng mục thi công điện",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        columns: [
                            {
                                title: "Nhận BG điểm đấu nối",
            					field: 'cdnhanBGDiemDauNoi',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                               
                            }, {
                                title: "Vướng",
            					field: 'cdvuong',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            },
                            {
                                title: "Đang TK",
            					field: 'cddangTK',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                            }, 
                            {
                                title: "Chưa TK",
            					field: 'cdchuaTK',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            },
                            {
                                title: "TC xong điện",
            					field: 'cdtcxongDien',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            }
                        ]
                    },
                    {
                        title: "Hạng mục thi công lắp dựng",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        columns: [
                            {
                                title: "Đủ ĐK chưa cấp",
            					field: 'ldduDKChuaCap',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                                
                            }, {
                                title: "Cấp chưa lắp",
            					field: 'ldcapChuaLap',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                                
                            },
                            {
                                title: "Vướng LD",
            					field: 'ldvuongLD',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                                
                            },
                            {
                                title: "Đang lắp",
            					field: 'lddangLap',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                              
                            },
                            {
                                title: "TC xong lắp dựng",
            					field: 'ldtcxongLapDung',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                                
                            }
                        ]
                    },
                    {
                        title: "Hạng mục thi công lắp BTS",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        columns: [
                            {
                                title: "Đủ ĐK chưa cấp BTS",
            					field: 'btsduDKChuaCapBTS',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                                
                            }, {
                                title: "Cấp chưa lắp",
            					field: 'btscapChuaLap',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                                
                            },
                            {
                                title: "Đang lắp",
            					field: 'btsdangLap',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                                
                            },
                            {
                                title: "TC xong BTS",
            					field: 'btstcxongBTS',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                }
                                
                            }
                        ]
                    },

                    {
                        title: "Trạm xong ĐB",
                        field: 'tramXongDB',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'number'
                    },{
                        title: "TC quá hạn",
                        field: 'tcQuahan',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'number'
                    }               
    ]
      
    });
}

function refreshGrid(d) {
    var grid = vm.rpBTSGrid;
    if (grid) {
        grid.dataSource.data(d);
        grid.refresh();
    }
}

function numberWithCommas(x) {
    if(x == null || x == undefined){
    return '0';
    }
    var parts = x.toFixed(2).toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
}

vm.doSearch = function(){
		vm.rpBTSGrid.dataSource.read();
		return;		
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

vm.getContractTemplate = function(){
	var fileName="Contract_Code";
	CommonService.downloadTemplate2(fileName).then(function(d) {
	data = d.plain();
	window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
	}).catch( function(){
		toastr.error(gettextCatalog.getString("Lỗi khi export!"));
		return;
	});

}

function callbackDoSearch() {
	vm.rpBTSGrid.dataSource.page(1);
	
}

vm.readFileContractBTS = function() {
	//if (!$("#fileSearchContractBTS")[0].files[0]) {
	//	toastr.error(gettextCatalog.getString("File hợp đồng không được để trống"));
	//	callbackDoSearch();
    //    return;
    //} else
	 //if (($("#fileSearchContractBTS")[0].files[0].name.split('.').pop() != 'xls' && $("#fileSearchContractBTS")[0].files[0].name.split('.').pop() != 'xlsx')) {
	//	toastr.warning("Sai định dạng file");
	//	callbackDoSearch();
    //    return;
	//}
    var formData = new FormData();
    //formData.append('multipartFile', $('#fileSearchContractBTS')[0].files[0]);
    return $.ajax({
        url: Constant.BASE_SERVICE_URL + "rpBTSService/readFileContractReport",
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
            	if (data.contractCodeLst.length > 0 ) {
            		vm.rpBTSSearch.contractCodeLst = data.contractCodeLst;
            	} 
            }
            callbackDoSearch();
        }, 
       // error: function(data) {
		//	if (!!data.error) {
		//		 toastr.error("File import phải có định dạng xlsx !");
		//	}
		//}
    });
}

vm.readFileStationBTS = function() {
	//if (!$("#fileSearchStationBTS")[0].files[0]) {
//        callbackDoSearch();
      //  return;
    //}
	//else if (($("#fileSearchStationBTS")[0].files[0].name.split('.').pop() != 'xls' && $("#fileSearchStationBTS")[0].files[0].name.split('.').pop() != 'xlsx')) {
		//toastr.warning("Sai định dạng file");
        //callbackDoSearch();
        //return;
	//}
    var formData = new FormData();
   // formData.append('multipartFile', $('#fileSearchStationBTS')[0].files[0]);
    return $.ajax({
        url: Constant.BASE_SERVICE_URL + "rpBTSService/readFileStationReport",
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
            		vm.rpBTSSearch.stationCodeLst = data.stationCodeLst;
            	} 
            }
            callbackDoSearch();
        }, 
       // error: function(data) {
		//	if (!!data.error) {
		//		 toastr.error("File import phải có định dạng xlsx !");
		//	}
		//}
    });
}


//HuyPQ-25/08/2018-edit-start
vm.exportFileBTS= function(){
//	vm.readFileContractBTS();
//	vm.readFileStationBTS();
	vm.doSearch();
	function displayLoading(target) {
    var element = $(target);
    kendo.ui.progress(element, true);
    setTimeout(function(){
    	var obj={};
    	obj.contractCodeLst=vm.rpBTSSearch.contractCodeLst;
    	obj.stationCodeLst=vm.rpBTSSearch.stationCodeLst;
		obj.catProvinceId = vm.rpBTSSearch.catProvinceId;
		obj.sysGroupId = vm.rpBTSSearch.sysGroupId;
		obj.sysGroupId = vm.rpBTSSearch.sysGroupId;
		obj.contractCode = vm.rpBTSSearch.contractCode;
		obj.keySearch = vm.rpBTSSearch.keySearch;
		
      	return Restangular.all("rpBTSService/exportCompleteProgressBTS").post(obj).then(function (d) {
      		var data = d.plain();
      	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
      	    kendo.ui.progress(element, false);
      	    console.log(vm.rpBTSSearch.contractCodeLst);
      	}).catch(function (e) {
      		kendo.ui.progress(element, false);
      	    toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
      	    return;
      	});
	});
		
}
	displayLoading("#rpBTSGrid");
}
//HuyPQ-edit-end

vm.deleteListData = deleteListData;
function deleteListData(x){
    if(x==1){
    	vm.rpBTSSearch.startingDateFrom = null;
        vm.rpBTSSearch.startingDateTo = null;
    } else
    if(x==2){
        vm.rpBTSSearch.listCatConstructionType = null;
    } else
    if(x==3){
    	vm.rpBTSSearch.completeDateFrom =null;
    	vm.rpBTSSearch.completeDateTo =null;
    }
    else if(x==4){
    	vm.rpBTSSearch.listStatus =null;
    } else if (x == 5) {
    	$("#fileSearchStationBTS").val(null);
    	vm.rpBTSSearch.stationCodeLst = null;
    } else if (x == 6) {
    	$("#fileSearchContractBTS").val(null);
    	vm.rpBTSSearch.contractCodeLst = null;
    } else if (x == 7) {
    	vm.rpBTSSearch.cntContractCode = null;
    	vm.rpBTSSearch.keySearch = null;
    }
}

vm.showHideWorkItemColumn = function (column) {
    if (angular.isUndefined(column.hidden)) {
        vm.rpBTSGrid.hideColumn(column);
    } else if (column.hidden) {
        vm.rpBTSGrid.showColumn(column);
    } else {
        vm.rpBTSGrid.hideColumn(column);
    }


}
/*
 * * Filter các cột của select
 */

vm.gridColumnShowHideFilter = function (item) {

    return item.type == null || item.type !== 1;
};

vm.errNumber = "";
vm.checkNumber = checkNumber;
function checkNumber() {
    var val = $('#parOder').val();
    if (val === 0) {
        if (val === 0) {
            if (val === "") {
                vm.errNumber = "";
            } else {
                vm.errNumber = "Phải nhập kiểu số nguyên từ 1-99";
                return false;
            }

        }
    } else {
        var isNaN = function (val) {
            if (Number.isNaN(Number(val))) {
                return false;
            }
            return true;
        }
        if (isNaN(val) === false) {
            vm.errNumber = "Phải nhập kiểu số nguyên từ 1-99";
        } else {
            vm.errNumber = "";
        }

    }


}
//hoanm1_20190312_start
vm.openDepartmentTo = openDepartmentTo

function openDepartmentTo(popUp) {
    vm.obj = {};
    vm.departmentpopUp = popUp;
    var templateUrl = 'coms/popup/findDepartmentPopUp.html';
    var title = gettextCatalog.getString("Tìm kiếm đơn vị");
    CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
}

vm.onSave = onSave;
function onSave(data) {
        vm.rpBTSSearch.sysGroupName = data.text;
        vm.rpBTSSearch.sysGroupId = data.id;
}

// clear data
vm.changeDataAuto = changeDataAuto
function changeDataAuto(id) {
    switch (id) {
        case 'dept':
        {
            if (processSearch(id, vm.selectedDept1)) {
                vm.rpBTSSearch.sysGroupId = null;
                vm.rpBTSSearch.sysGroupName = null;
                vm.selectedDept1 = false;
            }
            break;
        }
    }
}

vm.cancelInput = function (param) {
    if (param == 'dept') {
        vm.rpBTSSearch.sysGroupId = null;
        vm.rpBTSSearch.sysGroupName = null;
    }
}

vm.selectedDept1 = false;
vm.deprtOptions1 = {
    dataTextField: "text",
    dataValueField: "id",
	placeholder:"Nhập mã hoặc tên đơn vị",
    select: function (e) {
        vm.selectedDept1 = true;
        var dataItem = this.dataItem(e.item.index());
        vm.rpBTSSearch.sysGroupName = dataItem.text;
        vm.rpBTSSearch.sysGroupId = dataItem.id;
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
                    name: vm.rpBTSSearch.sysGroupName,
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
            vm.rpBTSSearch.sysGroupName = null;// thành name
            vm.rpBTSSearch.sysGroupId = null;
        }
    },
    ignoreCase: false
}

vm.openCatProvincePopup = openCatProvincePopup;
	vm.onSaveCatProvince = onSaveCatProvince;
	vm.clearProvince = clearProvince;
    function openCatProvincePopup(){
		var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
		var title = gettextCatalog.getString("Tìm kiếm tỉnh");
		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
    }
    function onSaveCatProvince(data){
        vm.rpBTSSearch.catProvinceId = data.catProvinceId;
        vm.rpBTSSearch.catProvinceCode = data.code;
		vm.rpBTSSearch.catProvinceName = data.name;
        htmlCommonService.dismissPopup();
        $("#provincename").focus();
    };
	function clearProvince (){
		vm.rpBTSSearch.catProvinceId = null;
		vm.rpBTSSearch.catProvinceCode = null;
		vm.rpBTSSearch.catProvinceName = null;
		$("#provincename").focus();
	}
    vm.provinceOptions = {
        dataTextField: "name",
        dataValueField: "id",
		placeholder:"Nhập mã hoặc tên tỉnh",
        select: function (e) {
            vm.isSelect = true;
            var dataItem = this.dataItem(e.item.index());
            vm.rpBTSSearch.catProvinceId = dataItem.catProvinceId;
            vm.rpBTSSearch.catProvinceCode = dataItem.code;
			vm.rpBTSSearch.catProvinceName = dataItem.name;
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
                        name: vm.rpBTSSearch.catProvinceName,
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
                vm.rpBTSSearch.catProvinceId = null;
                vm.rpBTSSearch.catProvinceCode = null;
				vm.rpBTSSearch.catProvinceName = null;
            }
        },
        close: function (e) {
            if (!vm.isSelect) {
                vm.rpBTSSearch.catProvinceId = null;
                vm.rpBTSSearch.catProvinceCode = null;
				vm.rpBTSSearch.catProvinceName = null;
            }
        }
    }
	
	//hop dong
	vm.openContractPopup = openContractPopup;
    vm.onSaveContract = onSaveContract;
	vm.clearContractCode = clearContractCode;

        function openContractPopup() {
            var templateUrl = 'coms/popup/findContractPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm hợp đồng");
            htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,null, vm, 'fff', 'ggfd', false, '85%','85%','contractSearchController');
        }

        function onSaveContract(data) {
            vm.rpBTSSearch.contractCode = data.code;
            htmlCommonService.dismissPopup();
        }
		function clearContractCode (){
		vm.rpBTSSearch.contractCode = null;
		$("#contractCode").focus();
	}

        vm.isSelectContract = false;
        vm.contractOptions = {
            dataTextField: "code",
            placeholder: "Mã hợp đồng",
            open: function(e) {
                vm.isSelectContract = false;
            },
            select: function(e) {
                vm.isSelectContract = true;
                var data = this.dataItem(e.item.index());
                vm.rpBTSSearch.contractCode = data.code;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function(options) {
                        vm.isSelectContract = false;
                        return Restangular.all(RestEndpoint.CNT_CONTRACT_SERVICE_URL + "/cntContract/doSearch").post({
                            isSize: true,
                            code: vm.rpBTSSearch.contractCode,
                            contractType: 0
                        }).then(function(response){
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
                if (!vm.isSelectContract) {
                    vm.rpBTSSearch.cntContractCode = null;
                }
            },
            close: function (e) {
                if (!vm.isSelectContract) {
                    vm.rpBTSSearch.cntContractCode = null;
                }
            }
        };
//hoanm1_20190312_end
vm.getExcelTemplate = function () {
    workItemService.downloadTemplate({}).then(function (d) {
        var data = d.plain();
        window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
    }).catch(function () {
        toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        return;
    });
}

    vm.downloadFile = downloadFile;
     function downloadFile(path){
         window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + path;
     }
}
})();
