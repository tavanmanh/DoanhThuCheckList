(function() {
    'use strict';
    var controllerId = 'rpYearProgressController';

    angular.module('MetronicApp').controller(controllerId, rpYearProgressController);

    function rpYearProgressController($scope, $rootScope, $timeout, gettextCatalog,
                                kendoConfig, $kWindow,
                                CommonService, PopupConst, Restangular, RestEndpoint,Constant,$http) {
        var vm = this;
        vm.searchForm={};
        vm.String="Quản lý công trình > Báo cáo > Tiến độ KH năm";

        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
            fillDataTable();
            initDropDownList();
        }
        function initDropDownList(){
            vm.progressOptions={
                dataSource:[
                    {id:1,name:"Vượt chỉ tiêu"},
                    {id:0,name:"Chưa vượt chỉ tiêu"}
                ],
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true
            }
            vm.yearDataList = [];
            var currentYear = (new Date()).getFullYear();
            for (var i = currentYear - 2; i < currentYear + 19; i++) {
                vm.yearDataList.push({
                    id: i,
                    name: i

                })
            }
            vm.yearDownListOptions = {
                dataSource: vm.yearDataList,
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
        /*
         * setTimeout(function(){ $("#appIds1").focus(); },15);
         */
        var record=0;

        vm.doSearch = function(){
            vm.rpYearPlanProgressGrid.dataSource.page(1);
        };

        //Xuất PDF
        vm.exportpdf= function(){
            var ds1=$("#rpYearPlanProgress").data("kendoGrid").dataSource.data();
            if (ds1.length === 0){
                toastr.error(gettextCatalog.getString("Không có dữ liệu xuất "));
                return ;
            }
            var binarydata= new Blob([ds1],{ type:'application/pdf'});
            kendo.saveAs({dataURI: binarydata, fileName: "a" + '.pdf'});
        }

        vm.listRemove = [{
            title: "Thao tác"
        },
            {
                title: "<input type='checkbox' id='checkalllistImpReq' name='gridchkselectall' ng-click='vm.chkSelectAll();' ng-model='vm.chkAll' />",
            }
        ]
        vm.listConvert = [{
            field: "status",
            data: {
                1: 'Hiệu lực',
                0: 'Hết Hiệu lực'
            }
        }, {
            field: "signState",
            data: {
                1: 'Chưa trình ký',
                2: 'Đã trình ký',
                3: 'Đã ký duyệt',
                4: 'Từ chối ký duyệt'

            }
        }
        ]

        
        vm.exportexcel= function(){
        	function displayLoading(target) {
        	      var element = $(target);
        	      kendo.ui.progress(element, true);
        	      setTimeout(function(){
        	    	  
        	    	  return Restangular.all("yearPlanDetailRsServiceRest/exportYearPlanProgress").post(vm.searchForm).then(function (d) {
        	                var data = d.plain();
        	                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	                kendo.ui.progress(element, false);
        	    	  }).catch(function (e) {
        	    		  	kendo.ui.progress(element, false);
        	                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        	                return;
        	            });;
        		});
        			
        	  }
        		displayLoading(".tab-content");
        }

        function fillDataTable() {
            vm.gridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,

                dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
                dataSource:{
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                        	$("#countRPYearProgress").text(""+response.total);
                            vm.count=response.total;
                            return response.total;
                        },
                        data: function (response) {
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "yearPlanDetailRsServiceRest/yearPlanDetail/reportProgress",
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
                    noRecords : gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
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
                        field:"stt",
                        template: function () {
                            return ++record;
                        },
                        width: '4%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    ,  {
                        title: "KH năm",
                        field: 'year',
                        width: '6%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },{
                        title: "Đơn vị",
                        field: 'sysGroupName',
                        width: '18%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Sản lượng",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Chỉ tiêu",
                                field: 'quantity',
                                width: '8%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                format: "{0:n3}"
                            },{
                                title: "Hiện trạng",
                                field: 'currentQuantity',
                                width: '8%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                format: "{0:n3}"
                            },{
                                title: "% Tiến độ",
                                field: 'progressQuantity',
                                width: '8%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.progressQuantity) + '%';
                                },
                                format: "{0:n3}"
                            }
                        ]
                    },
                    {
                        title: "HSHC",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Chỉ tiêu",
                                field: 'complete',
                                width: '8%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                format: "{0:n3}"
                            },{
                                title: "Hiện trạng",
                                field: 'currentComplete',
                                width: '8%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                format: "{0:n3}"
                            },{
                                title: "% Tiến độ",
                                field: 'progressComplete',
                                width: '8%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.progressComplete) + '%';
                                },
                                format: "{0:n3}"
                            }
                        ]
                    },
                    {
                        title: "Doanh thu",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        columns: [
                            {
                                title: "Chỉ tiêu",
                                field: 'revenue',
                                width: '8%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                format: "{0:n3}"
                            },{
                                title: "Hiện trạng",
                                field: 'currentRevenue',
                                width: '8%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                format: "{0:n3}"
                            },{
                                title: "% Tiến độ",
                                field: 'progressRevenue',
                                width: '8%',
                                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                                attributes: {
                                    style: "text-align:right;"
                                },
                                template: function(data){
                                    return numberWithCommas(data.progressRevenue) + '%';
                                },
                                format: "{0:n3}"
                            }
                        ]
                    }
                ]
            });
        }
        vm.exportpdf = function(){
        	function displayLoading(target) {
        	      var element = $(target);
        	      kendo.ui.progress(element, true);
        	      setTimeout(function(){
        	    	  
        	    	  var obj={};
        	            obj = vm.searchForm;
        	            obj.reportType = "DOC";
        	            obj.reportName = "Export_Tien_Do_Ke_Hoach_Nam";

        	            $http({url: RestEndpoint.BASE_SERVICE_URL + "yearPlanDetailRsServiceRest"+"/rpYearProgress",
        	                dataType: 'json',
        	                method: 'POST',
        	                data: obj,
        	                headers: {
        	                    "Content-Type": "application/json"
        	                },
        	                responseType : 'arraybuffer'//THIS IS IMPORTANT
        	            }).success(function(data, status, headers, config){
        	                if(data.error){
        	                	kendo.ui.progress(element, false);
        	                    toastr.error(data.error);
        	                } else {
        	                	kendo.ui.progress(element, false);
        	                    var binarydata= new Blob([data],{type: "text/plain;charset=utf-8"});
        	                    kendo.saveAs({dataURI: binarydata, fileName: "Export_Tien_Do_Ke_Hoach_Nam" + '.pdf'});
        	                }

        	            })
        	                .error(function(data){
        	                	kendo.ui.progress(element, false);
        	                    toastr.error("Có lỗi xảy ra!");
        	                });
        		});
        			
        	  }
        		displayLoading(".tab-content");
            
        }

        function numberWithCommas(x) {
            if(x == null || x == undefined){
            return '0';
            }
            var parts = x.toFixed(2).toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
        }


        vm.openDepartmentTo1=openDepartmentTo1

        function openDepartmentTo1(popUp){
            vm.obj={};
            vm.departmentpopUp=popUp;
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null,null, vm, popUp, 'string', false, '92%','89%');
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
        vm.showHideEntangledGrid1Column = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.rpYearPlanProgressGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.rpYearPlanProgressGrid.showColumn(column);
            } else {
                vm.rpYearPlanProgressGrid.hideColumn(column);
            }


        }

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };
        vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'dept') {
                vm.searchForm.sysGroupName = data.text;
                vm.searchForm.sysGroupId = data.id;
            }
        }
        vm.xoaDV = xoaDV;
        function xoaDV(x) {
        	if(x==1){
            vm.searchForm.sysGroupName=null;
            vm.searchForm.sysGroupId=null;
        	}
        	if(x==2){
        		vm.searchForm.progress=null;
        	}if(x==3){
        		vm.searchForm.listYear=null;
        	}
        }
    }
})();
