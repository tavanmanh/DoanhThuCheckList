/**
 * Created by pm1_os24 on 3/1/2018.
 */
(function() {
    'use strict';
    var controllerId = 'rpDayQuantityController';

    angular.module('MetronicApp').controller(controllerId, rpDayQuantityController);

    function rpDayQuantityController($scope, $rootScope, $timeout, gettextCatalog,
                                      kendoConfig, $kWindow,
                                      CommonService, PopupConst, Restangular, RestEndpoint,Constant,$http) {
        var vm = this;
        vm.searchForm={
            obstructedState:1
        };
        vm.String="Quản lý công trình > Báo cáo > Sản lượng theo ngày";
        vm.searchForm.dateBD=setCurrentDay();
        function setCurrentDay(){
        	var today=new Date();
        	return today.getDate()+'/'+(today.getMonth()+1)+'/'+today.getFullYear();
        }
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
            fillDataTable1();
            fillDataTable2();
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
      //HuyPQ-25/08/2018-edit-start
        vm.exportexcel= exportexcel;
        function exportexcel(dataSearch){
        	function displayLoading(target) {
        	      var element = $(target);
        	      kendo.ui.progress(element, true);
        	      setTimeout(function(){
        	    	  
        	    	  return Restangular.all("workItemService/exportSLTN").post(dataSearch).then(function (d) {
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
                        	$("#countRPDayQuantity").text(""+response.total);
                        	
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
                            url: Constant.BASE_SERVICE_URL + "workItemService/doSearchForReport",
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
                        width: '50%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    }, {
                        title: "Sản lượng",
                        field: 'quantity',
                        width: '45%',
                        format: "{0:n4}",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        }

                    }
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
                        	$("#countRPDayQuantityDetail").text(""+response.total);
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
                            url: Constant.BASE_SERVICE_URL + "workItemService/doSearchDetailForReport",
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
                        width: '25%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    }, {
                        title: "Tỉnh",
                        field: 'provinceCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    }, {
                        title: "Trạm",
                        field: 'catStationCode',
                        width: '10%',
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
                    }, {
                        title: "Hạng mục",
                        field: 'name',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    }
                    , {
                        title: "Sản lượng",
                        field: 'approveQuantity',
                        width: '20%',
                        format: "{0:n3}",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        }
                    }
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

        	                $http({url: RestEndpoint.BASE_SERVICE_URL + "workItemService"+"/exportPdfSLTN",
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

        	                $http({url: RestEndpoint.BASE_SERVICE_URL + "workItemService"+"/exportPdfSLTNCT",
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
