(function() {
    'use strict';
    var controllerId = 'rpConstructionTaskController';

    angular.module('MetronicApp').controller(controllerId, rpConstructionTaskController);

    function rpConstructionTaskController($scope, $rootScope, $timeout, gettextCatalog,$filter,
                                kendoConfig, $kWindow,htmlCommonService,
                                CommonService, PopupConst, Restangular, RestEndpoint,Constant,$http) {

        var vm = this;
        vm.searchForm={};
        var record=0;
        vm.d={};
        vm.String = "Quản lý công trình > Báo cáo > Danh sách công việc";

        vm.monthListOptions={
            dataSource: {
                serverPaging: true,
                schema: {
                    data: function (response) {
                        return response.data; // data is returned in
                        // the "data" field of
                        // the response
                    }
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "DetailMonthPlanRsService/doSearch",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.d.page = 1;
                        vm.d.signStateList = ['3'];
                        vm.d.pageSize = 100;
                        return JSON.stringify(vm.d);

                    }
                }
            },
            dataValueField: "detailMonthPlanId",
            template: '<span>Tháng #:data.month#</span>'+'/'+'<span>#:data.year#</span>',
            tagTemplate: '<span>Tháng #:data.month#</span>'+'/'+'<span>#:data.year#</span>',
            valuePrimitive: true
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
                            $timeout( function(){ vm.count2 = response.total; } );
                            return response.total;
                        },
                        data: function (response) {
                            var list = response.data;
                            return response.data;
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "constructionTaskService/doSearchForReport",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page
                            vm.searchForm.pageSize = options.pageSize
                            if(vm.searchForm.fullYear != null){
                                vm.searchForm.month = vm.searchForm.fullYear.split("/")[0];
                                vm.searchForm.year = vm.searchForm.fullYear.split("/")[1];
                            }
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
                        title: "Kế hoạch tháng",
                        field: 'code',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template:function(dataItem){
                            return "Tháng "+dataItem.month +"/"+dataItem.year;
                        }
                    }, {
                        title: "Tên công việc",
                        field: 'taskName',
                        width: '14%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    }, {
                        title: "Người thực hiện",
                        field: 'performerName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    },  {
                        title: "Ngày bắt đầu",
                        field: 'startDate',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        //template : function(data){
                        //    return formatDate(data.startDate);
                        //}
                    }, {
                        title: "Ngày kết thúc",
                        field: 'endDate',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        //template : function(data){
                        //    if(data.endDate!=''||data.endDate==null){
                        //        return $filter('date')(data.endDate,'dd/MM/yyyy');
                        //    }
                        //    else
                        //        return ' ';
                        //}
                    }, {
                        title: "Mã công trình",
                        field: 'constructionCode',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    }, {
                        title: "Đơn vị thực hiện",
                        field: 'sysGroupName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    }, {
                        title: "Trạng thái công việc",
                        field: 'status',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: "# if(status == 1){ #" + "#= 'Chưa thực hiện' #" + "# } " + "else if (status == 2) { # " + "#= 'Đang thực hiện' #" + "#}" + "else if (status == 3) { # " + "#= 'Tạm dừng' #" + "#} " + "else if (status == 4) { # " + "#= 'Đã hoàn thành' #" + "#} #"
                    }, {
                        title: "Tiến độ",
                        field: 'completePercent',
                        width: '6%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },

                        template : function(data){

                            if(data.completePercent==null){
                                return 0+"%";
                            }else  return data.completePercent+"%";
                        }
                    }, {
                        title: "Tình trạng công việc",
                        field: 'completeState',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: "# if(completeState == 1){ #" + "#= 'Đúng tiến độ' #" + "# } " + "else if (completeState == 0) { # " + "#= 'Chậm tiến độ' #" + "#} #"
                    }
    ]
    });

        function refreshGrid(d) {
            var grid = vm.constructionTaskGrid;
            if (grid) {
                grid.dataSource.data(d);
                grid.refresh();
            }
        }

        function formatDate(date) {
            var newdate = new Date(date);
            return kendo.toString(newdate, "dd/MM/yyyy");
        }

        vm.openDepartmentTo1=openDepartmentTo1

        function openDepartmentTo1(popUp){
            vm.obj={};
            vm.departmentpopUp=popUp;
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null,null, vm, popUp, 'string', false, '92%','89%');
        }

        vm.doSearch = function(){
            vm.constructionTaskGrid.dataSource.page(1);
        }
        //HuyPQ-25082018-start
        vm.exportexcel= function(){
        	function displayLoading(target) {
        	      var element = $(target);
        	      kendo.ui.progress(element, true);
        	      setTimeout(function(){
        	    	  
        	    	  if(vm.searchForm.fullYear != null){
        	                vm.searchForm.month = vm.searchForm.fullYear.split("/")[0];
        	                vm.searchForm.year = vm.searchForm.fullYear.split("/")[1];
        	            }
        	            return Restangular.all("constructionTaskService/exportConstructionTask").post(vm.searchForm).then(function (d) {
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
        //HuyPQ-end
        /*vm.exportpdf= function(){
            var ds1=$("#rpConstructionTask").data("kendoGrid").dataSource.data();
            if (ds1.length === 0){
                toastr.error(gettextCatalog.getString("Không có dữ liệu xuất "));
                return ;
            }
            var binarydata= new Blob([ds1],{ type:'application/pdf'});
            kendo.saveAs({dataURI: binarydata, fileName: "a" + '.pdf'});
        }*/

        vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'dept') {
                vm.searchForm.sysGroupName = data.text;
                vm.searchForm.sysGroupId = data.id;
            }
        }

        vm.selectedDept1=false;
        vm.deprtOptions1 = {
            dataTextField: "text",
            dataValueField:"id",
        	placeholder:"Nhập mã hoặc tên đơn vị",
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
                vm.constructionTaskGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.constructionTaskGrid.showColumn(column);
            } else {
                vm.constructionTaskGrid.hideColumn(column);
            }


        }

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };

        vm.deleteListData = deleteListData;
    function deleteListData(x){
        if(x==1){
            vm.searchForm.sysGroupName = null;
            vm.searchForm.sysGroupId = null;
        }
        if(x==2){
            vm.searchForm.listStatus =null;
        }
        if(x==3){
            vm.searchForm.listCompleteState =null;
        }
        if(x==4){
            vm.searchForm.fullYear =null;
            vm.searchForm.month = null;
            vm.searchForm.year = null;
        }
    }
        vm.monthSelectorOptions = {
            start: "year",
            depth: "year"
        };
    // HuyPQ-27/08/2018-start
    vm.exportpdf = function(){
    	function displayLoading(target) {
    	      var element = $(target);
    	      kendo.ui.progress(element, true);
    	      setTimeout(function(){
    	    	  
    	    	  if(vm.searchForm.fullYear != null){
    	              vm.searchForm.month = vm.searchForm.fullYear.split("/")[0];
    	              vm.searchForm.year = vm.searchForm.fullYear.split("/")[1];
    	          }

    	          $http({url: RestEndpoint.BASE_SERVICE_URL + "constructionTaskService"+"/exportPdfService",
    	              dataType: 'json',
    	              method: 'POST',
    	              data: vm.searchForm,
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
    	          kendo.saveAs({dataURI: binarydata, fileName: "DanhSachCongViec" + '.pdf'});
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
    //HuyPQ-end
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
