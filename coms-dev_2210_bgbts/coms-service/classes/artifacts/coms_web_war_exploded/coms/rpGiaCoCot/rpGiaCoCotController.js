(function () {
    'use strict';
    var controllerId = 'rpGiaCoCotController';

    angular.module('MetronicApp').controller(controllerId, rpGiaCoCotController);

    function rpGiaCoCotController($scope, $rootScope, $timeout, gettextCatalog,
                                kendoConfig, $kWindow, rpGiaCoCotService,
                                CommonService, PopupConst, Restangular, RestEndpoint, Constant, htmlCommonService) {
        var vm = this;
        vm.showSearch = true;
        vm.isCreateNew = false;
        vm.showDetail = false;
        vm.rpGiaCoCotSearch = {};
        vm.String = "Quản lý công trình > Báo cáo > Báo cáo gia cố cột";
        
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
            fillDataTable([]);
            vm.rpGiaCoCotSearch.fullYear = kendo.toString(new Date(), "MM/yyyy");
        }
        
        vm.validatorOptions = kendoConfig.get('validatorOptions');
        
        var record = 0;
        
        function fillDataTable(data) {
        	vm.rpGiaCoCotGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save : function(){
                    vm.rpGiaCoCotGrid.refresh();
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
                        '<label ng-repeat="column in vm.rpGiaCoCotGrid.columns.slice(1,vm.rpGiaCoCotGrid.columns.length)| filter: vm.gridColumnShowHideFilter">' +

                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#appCountGiaCoCot").text("" + response.total);
                            vm.countGiaCoCot = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "rpGiaCoCotService/doSearchGiaCoCot",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                        	
                            vm.rpGiaCoCotSearch.page = options.page
                            vm.rpGiaCoCotSearch.pageSize = options.pageSize
                            if(vm.rpGiaCoCotSearch.fullYear != null){
                                vm.rpGiaCoCotSearch.month = vm.rpGiaCoCotSearch.fullYear.split("/")[0];
                                vm.rpGiaCoCotSearch.year = vm.rpGiaCoCotSearch.fullYear.split("/")[1];
                            }
                            return JSON.stringify(vm.rpGiaCoCotSearch)
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
                        width: '30px',
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
            					field: 'donVi',
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
                                
                            },
                            ] 
                    },
                    {
                        title: "Tổng KH",
                        width: '100px',
                        field: 'tongKH',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        
                    },
                    {
                        title: "Chưa đủ ĐK nhận BGMB",
                        width: '200px',
                        field: 'chuaDuDKnhanBGMB',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        
                    },
                    {
                        title: "Đủ điều kiện(đã có TK)nhận BGMB",
                        width: '200px',
                        field: 'duDKNhanBGMB',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        
                    },
                    {
                        title: "KẾT QUẢ THỰC HIỆN",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        columns: [
                            {
                                title: "Đã nhận BGMB",
            					field: 'daNhanBGMB',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            }, {
                                title: "Đã triển khai",
            					field: 'daTrienKhai',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            },
                            {
                                title: "Xong XD",
            					field: 'xongXD',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            },
                            {
                                title: "Xong LD",
            					field: 'xongLD',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            } 
                        ]
                    },
                    {
                        title: "LL XÂY DỰNG",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        columns: [
                            {
                                title: "Đội đang XD",
            					field: 'doiDangXD',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            }
                        ]
                    },
                    {
                        title: "CHƯA TK XÂY DỰNG",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        columns: [
                            {
                                title: "Tổng",
            					field: 'tong',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            }, {
                                title: "Chưa nhận BGMB",
            					field: 'chuaNhanBGMB',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            },
                            {
                                title: "Nhận MB chưa TK",
            					field: 'nhanMBChuaTk',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            }
                        ]
                    },
                    {
                        title: "LẮP DỰNG",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        columns: [
                            {
                                title: "Cấp lên tỉnh",
            					field: 'capLenTinh',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            }, {
                                title: "Xong móng->chưa cấp",
            					field: 'xongMongChuaCap',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            },
                            {
                                title: "Cấp nhưng chưa xong móng",
            					field: 'capNhungChuaXongMong',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            },
                            {
                                title: "Cấp chưa lắp",
            					field: 'capChuaLap',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            },
                            {
                                title: "Đang có đội lắp",
            					field: 'dangCoDoiLap',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            }
                        ]
                    },
                    {
                        title: "KH "+"<span>{{vm.rpGiaCoCotSearch.fullYearOption}}</span>",                     	
                        headerAttributes: {style: "text-align:center;font-weight: bold;" },
                        columns: [
                            {
                                title: "Xây dựng",
            					field: 'khxayDung',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            }, {
                                title: "KH lắp dựng",
            					field: 'khlapDung',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            },
                            {
                                title: "KH ĐC",
            					field: 'khdc',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            },
                            {
                                title: "Vật tư đã đảm bảo",
            					field: 'vatTuDaDamBao',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            }
                        ]
                    },
                    {
                        title: "KẾT QUẢ THỰC HIỆN "+"<span>{{vm.rpGiaCoCotSearch.fullYearOption}}</span>",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        columns: [
                            {
                                title: "Xây dựng",
            					field: 'kqxayDung',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            }, {
                                title: "%TH",
                                field: 'kqth1',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                format: "{0:n2}%"
                            },
                            {
                                title: "Lắp cột",
            					field: 'lapCot',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                
                            },
                            {
                                title: "%TH",
            					field: 'kqth2',
                                width: '100px',
                                headerAttributes: { style: "text-align:center;font-weight: bold;" },
                                attributes: {
                                    style: "text-align:center;"
                                },
                                format: "{0:n2}%"
                            }
                        ]
                    },         
    ]
    });
}

function refreshGrid(d) {
    var grid = vm.rpGiaCoCotGrid;
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



vm.getStationCodeTemplate = function(){
	var fileName="Station_Code";
	CommonService.downloadTemplate2(fileName).then(function(d) {
	data = d.plain();
	window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
	}).catch( function(){
		toastr.error(gettextCatalog.getString("Lỗi khi export biểu mẫu!"));
		return;
	});

}

vm.getContractTemplate = function(){
	var fileName="Contract_Code";
	CommonService.downloadTemplate2(fileName).then(function(d) {
	data = d.plain();
	window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
	}).catch( function(){
		toastr.error(gettextCatalog.getString("Lỗi khi export biểu mẫu!"));
		return;
	});

}

function callbackDoSearch() {
	vm.rpGiaCoCotGrid.dataSource.page(1);
	
}

vm.readFileContract = function() {
	debugger;
	if (!$("#fileSearchContract")[0].files[0]) {
		toastr.error(gettextCatalog.getString("File hợp đồng không được để trống"));
        callbackDoSearch();
        return;
    }
	else if (($("#fileSearchContract")[0].files[0].name.split('.').pop() != 'xls' && $("#fileSearchContract")[0].files[0].name.split('.').pop() != 'xlsx')) {
		toastr.warning("Sai định dạng file");
        callbackDoSearch();
        return;
	}
    var formData = new FormData();
    formData.append('multipartFile', $('#fileSearchContract')[0].files[0]);
    return $.ajax({
        url: Constant.BASE_SERVICE_URL + "rpGiaCoCotService/readFileContractReport",
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
            		vm.rpGiaCoCotSearch.contractCodeLst = data.contractCodeLst;
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

vm.readFileStation = function() {
	debugger;
	if (!$("#fileSearchStation")[0].files[0]) {
//        callbackDoSearch();
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
        url: Constant.BASE_SERVICE_URL + "rpGiaCoCotService/readFileStationReport",
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
            		vm.rpGiaCoCotSearch.stationCodeLst = data.stationCodeLst;
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

vm.rpGiaCoCotSearch.fullYearOption = vm.rpGiaCoCotSearch.fullYear;
vm.doSearch = function(){
		vm.rpGiaCoCotSearch.fullYearOption = vm.rpGiaCoCotSearch.fullYear;
		vm.readFileContract();
		vm.readFileStation();
		callbackDoSearch();
		return;
}

vm.disableExportExcel = false;
//HuyPQ-25/08/2018-edit-start
vm.exportFile= function(){
	vm.doSearch();
	function displayLoading(target) {
  var element = $(target);
  kendo.ui.progress(element, true);
  setTimeout(function(){
	  var obj={};
	  if(vm.rpGiaCoCotSearch.fullYear != null){
          obj.month = vm.rpGiaCoCotSearch.fullYear.split("/")[0];
          obj.year = vm.rpGiaCoCotSearch.fullYear.split("/")[1];
      }
		if (vm.disableExportExcel)
    		return;
    	vm.disableExportExcel = true;
    	obj.contractCodeLst=vm.rpGiaCoCotSearch.contractCodeLst;
    	obj.stationCodeLst=vm.rpGiaCoCotSearch.stationCodeLst;
    	return Restangular.all("rpGiaCoCotService/exportCompleteProgress").post(obj).then(function (d) {
    	    var data = d.plain();
    	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
    	    kendo.ui.progress(element, false);
    	    vm.disableExportExcel = false;
    	}).catch(function (e) {
    		kendo.ui.progress(element, false);
    	    toastr.error(gettextCatalog.getString("Lỗi khi tải file excel!"));
    	    vm.disableExportExcel = false;
    	    return;
    	});
	});
		
}
	displayLoading("#rpGiaCoCotGrid");
}
//HuyPQ-edit-end


vm.deleteListData = deleteListData;
function deleteListData(x){
    if(x==1){
    	vm.rpGiaCoCotSearch.startingDateFrom = null;
        vm.rpGiaCoCotSearch.startingDateTo = null;
    } else
    if(x==2){
        vm.rpGiaCoCotSearch.listCatConstructionType = null;
    } else
    if(x==3){
    	vm.rpGiaCoCotSearch.fullYear =null;
    }
    else if(x==4){
    	vm.rpGiaCoCotSearch.listStatus =null;
    } else if (x == 5) {
    	$("#fileSearchStation").val(null);
    	vm.rpGiaCoCotSearch.stationCodeLst = null;
    } else if (x == 6) {
    	$("#fileSearchContract").val(null);
    	vm.rpGiaCoCotSearch.contractCodeLst = null;
    } else if (x == 7) {
    	
    }
}

vm.showHideWorkItemColumn = function (column) {
    if (angular.isUndefined(column.hidden)) {
        vm.rpGiaCoCotGrid.hideColumn(column);
    } else if (column.hidden) {
        vm.rpGiaCoCotGrid.showColumn(column);
    } else {
        vm.rpGiaCoCotGrid.hideColumn(column);
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

vm.monthSelectorOptions = {
        start: "year",
        depth: "year"
    };

vm.getExcelTemplate = function () {
    workItemService.downloadTemplate({}).then(function (d) {
        var data = d.plain();
        window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
    }).catch(function () {
        toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        return;
    });
}

vm.readFileContract = function() {
	if (!$("#fileSearchContract")[0].files[0]) {
        callbackDoSearch();
        return;
    }
	else if (($("#fileSearchContract")[0].files[0].name.split('.').pop() != 'xls' && $("#fileSearchContract")[0].files[0].name.split('.').pop() != 'xlsx')) {
		toastr.warning("Sai định dạng file");
        callbackDoSearch();
        return;
	}
    var formData = new FormData();
    formData.append('multipartFile', $('#fileSearchContract')[0].files[0]);
    return $.ajax({
        url: Constant.BASE_SERVICE_URL + "rpGiaCoCotService/readFileContractReport",
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
            		vm.rpGiaCoCotSearch.contractCodeLst = data.contractCodeLst;
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
        url: Constant.BASE_SERVICE_URL + "rpGiaCoCotService/readFileStationReport",
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
            		vm.rpGiaCoCotSearch.stationCodeLst = data.stationCodeLst;
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

    vm.downloadFile = downloadFile;
     function downloadFile(path){
         window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + path;
     }
}
})();
