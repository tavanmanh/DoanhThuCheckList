/**
 * Created by pm1_os42 on 2/24/2018.
 */
function constrTaskFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
    kendoConfig, $kWindow, constructionService,
    CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm){

    vm.obj ={};
    vm.catTaskSearch={};
    vm.workItemSearch={};
    vm.performerSearch ={};
    vm.curentObj={};
    vm.obj.type=1;
    vm.obj.taskOrder=1;
    vm.workItemSearch.constructionId=vm.constrObj.constructionId;
    vm.workItemSearch.sysGroupId=vm.constrObj.sysGroupId;
    vm.performerSearch.sysGroupId=vm.constrObj.sysGroupId;
    vm.catTaskSearch.catWorkItemTypeId=1;



       

    vm.constrTaskTypeData=  [
        {id:1,name:"Thi công"},
        {id:2,name:"Làm HSHC"},
        {id:3,name:"Lên doanh thu"},
        {id:6,name:"Công việc khác"},
    ]

    vm.searchGrantt = function(){
            //$scope.taskAll = 0;
            //$scope.taskSlow = 0;
            //$scope.taskPause = 0;
            //$scope.taskUnfulfilled=0;
            vm.ganttOptions.dataSource.read();
        };

    vm.clearInput = function(data,form){
        switch (data){
            case '1': 
                vm.obj.workItemCode = null;
                vm.obj.workItemId = null;
                vm.obj.workItemName = null;
                vm.obj.taskName = null;
                vm.obj.catTaskCode = null;
                vm.obj.catTaskId = null;
                break;
            case '2': 
                vm.obj.taskName = null;
                vm.obj.catTaskCode = null;
                vm.obj.catTaskId = null;
                break;
            case '3': 
                vm.obj.performerName = null;
                vm.obj.performerId = null;
                break;
            case '4':
                vm.obj.startDate = null;
                $rootScope.validateDate(vm.obj.startDate,null,null,form)
                break;
            case '5':
                vm.obj.endDate = null;
                $rootScope.validateDate(vm.obj.endDate,null,null,form)
                break;
            //chinhpxn20180622_start
            case '6':
        	vm.obj.quantity = null;
        	break;
        	//chinhpxn20180622_end
            default : break;
        }
    }


        vm.constructionOptions = {
            dataTextField: "code",
            dataValueField: "constructionId",
            select: function (e) {
                vm.selectedDept1 = true;
                var data = this.dataItem(e.item.index());
                vm.obj.constructionName = data.name;
                vm.obj.constructionCode = data.code;
                vm.obj.constructionId = data.constructionId;
                vm.workItemSearch.constructionId = data.constructionId;
                vm.workItemSearch.sysGroupId = data.sysGroupId;
                vm.performerSearch.sysGroupId = data.sysGroupId;
                vm.obj.workItemName = null;
                vm.obj.workItemCode = null;
                vm.obj.workItemId = null;
                vm.catTaskSearch.catWorkItemTypeId=null;
                vm.obj.taskName = null;
                vm.obj.catTaskCode = null;
                vm.obj.catTaskId = null;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        return Restangular.all("constructionTaskService/construction/searchConstruction").post({
                            keySearch: vm.obj.constructionCode,
                            pageSize: vm.constructionOptions.pageSize,
                            page:1
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
                    vm.workItemSearch.constructionId = null;
                    vm.workItemSearch.sysGroupId = null;
                    vm.performerSearch.sysGroupId = null;
                }
            },
            ignoreCase: false
        }


        vm.workItemOptions = {
            dataTextField: "name",
            dataValueField: "workItemId",
            select: function (e) {
                vm.selectedDept1 = true;
                var data = this.dataItem(e.item.index());
                vm.obj.workItemName = data.name;
                vm.obj.workItemCode = data.code;
                vm.obj.workItemId = data.workItemId;
                vm.catTaskSearch.catWorkItemTypeId=data.catWorkItemTypeId;
                vm.obj.taskName = null;
                vm.obj.catTaskCode = null;
                vm.obj.catTaskId = null;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        return Restangular.all("constructionTaskService/construction/searchWorkItems").post({
                            keySearch: vm.obj.workItemName,
                            pageSize: vm.workItemOptions.pageSize,
                            sysGroupId:vm.constrObj.sysGroupId,
                            constructionId:vm.constrObj.constructionId,
                            page:1,
                            listStatus:[1,2]
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.code #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden">#: data.name # </div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.obj.workItemName = null;
                vm.obj.workItemCode = null;
                vm.obj.workItemId = null;
                vm.catTaskSearch.catWorkItemTypeId=null;
                    vm.obj.taskName = null;
                    vm.obj.catTaskCode = null;
                    vm.obj.catTaskId = null;
                }
            },
            ignoreCase: false
        }

        vm.catTaskOptions = {
            dataTextField: "name",
            dataValueField: "workItemId",
            select: function (e) {
                vm.selectedDept1 = true;
                var data = this.dataItem(e.item.index());
                vm.obj.taskName = data.name;
                vm.obj.catTaskCode = data.code;
                vm.obj.catTaskId = data.workItemId;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        return Restangular.all("constructionTaskService/construction/searchCatTask").post({
                            keySearch: vm.obj.taskName,
                            pageSize: vm.catTaskOptions.pageSize,
                            catWorkItemTypeId:vm.catTaskSearch.catWorkItemTypeId,
                            page:1
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.code #</div><div  style="word-wrap: break-word;padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.obj.taskName = null;
                    vm.obj.catTaskCode = null;
                    vm.obj.catTaskId = null;
                }
            },
            ignoreCase: false
        }

        vm.catTaskOptions1 = {
            dataTextField: "fullName",
            dataValueField: "sysUserId",
            select: function (e) {
                vm.selectedDept1 = true;
                var data = this.dataItem(e.item.index());
                vm.obj.performerName = data.fullName;
                vm.obj.performerIdDetail = data.sysUserId;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        return Restangular.all("constructionTaskService/construction/searchPerformer").post({
                            keySearch: vm.obj.performerName,
                            pageSize: vm.catTaskOptions1.pageSize,
                            sysGroupId:vm.constrObj.sysGroupId,
                            page:1
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.loginName #</div><div  style="word-wrap: break-word;padding-left: 5px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.obj.performerName = null;
                    vm.obj.performerIdDetail = null;
                }
            },
            ignoreCase: false
        }
    vm.changeType = function(){
        vm.clearInput('1');
        vm.clearInput('2');
    }
        
  //chinhpxn20180622_start
    vm.genTaskName = function() {
    	if(vm.obj.type==3) {
    		if(vm.obj.taskOrder == 1) {
        		vm.obj.taskName = "Tạo đề nghị quyết toán cho công trình " + vm.constrObj.code;
        	} else {
        		vm.obj.taskName = "Lên doanh thu cho công trình " + vm.constrObj.code;
        	}
    	} else if (vm.obj.type == 2) {
    		vm.obj.taskName = "Làm HSHC cho công trình " + vm.constrObj.code;
    	}
    }
    //chinhpxn20180622_end
    
    vm.openPopupTC =function(){
         vm.clearInput('1');
            vm.clearInput('2');
            vm.clearInput('3');
            vm.clearInput('4');
            vm.clearInput('5');
            //chinhpxn20180713_start
            vm.clearInput('6');
            //chinhpxn20180713_end
        $("#ganttTC").data("kendoGantt").dataSource.read();
        var teamplateUrl = "coms/construction/popupConstructionTask.html";
        var title = "Thêm mới công việc";
        var windowId = "CONSTRUCTION";
        CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '650', '500', null);
    }

    vm.openPerformerPopup = function(data){
            vm.performerSearch.sysGroupId=null;
            var teamplateUrl = "coms/construction/performerPopup.html";
            var title = "Danh sách người thực hiện";
            var windowId = "CONSTRUCTION_TASK";
            CommonService.populatePopupOnPopup(teamplateUrl, title, data, vm, windowId, true, '950', '450', null);
        }

     vm.choosePerformer1 = function(data){
     	vm.curentObj.taskName = vm.curentObj.title;
    	vm.curentObj.oldPerformerId = vm.curentObj.performerId;
        vm.curentObj.performerId=data.sysUserId;
        return Restangular.all("constructionTaskService/construction/updatePerfomer").post(vm.curentObj).then(function (d) {
            if(d.error){
                toastr.error(d.error);
                return;
            }
            toastr.success("Cập nhật dữ liệu thành công")
            $("#ganttTC").data("kendoGantt").dataSource.read();
            CommonService.dismissPopup1();
        }).catch(function (e) {
                toastr.error("Có lỗi xảy ra");

            return;
        });;
    }
    vm.closePopupOnPopup = function(){
        CommonService.dismissPopup1();
    }
    vm.doSearchPopup = doSearchPopup;
    function doSearchPopup(data){
        var grid;
        if(data=='1') {
            grid =vm.listWorkItemPopupGrid;
        }
        else if(data=='2') {
            grid =vm.listCatTaskPopupGrid;
        }
        else if(data='3') {
            grid =vm.performerPopupGrid;
        }
        else if(data='4') {
            grid =vm.performerPopupGrid;
        }
        if(grid){
            grid.dataSource.query({
                page: 1,
                pageSize: 10
            });
        }
    }
    vm.openTaskPopup = function(data){
        vm.keySearch=undefined;
        var teamplateUrl = "coms/construction/listWorkItemPopup.html";
        if(data=='1') {
            var title = "Danh sách hạng mục";
            vm.labelSearch="Mã/Tên hạng mục";
        }
        else if(data=='2') {
            var title = "Danh sách công việc";
            vm.labelSearch="Mã/Tên công việc";
        }
        else if(data=='3') {
            var title = "Danh sách người thực hiện";
            vm.labelSearch="Tên đăng nhập/Mã nhân viên";
        }else if(data=='4') {
            var title = "Danh sách người thực hiện";
            vm.windowId='workItemPerson';
            vm.labelSearch="Tên đăng nhập/Mã nhân viên";
        }
        var windowId = "CONSTRUCTION";
        CommonService.populatePopupOnPopup(teamplateUrl, title, data, vm, windowId, true, '950', '350', null);
    }

    vm.chooseWorkItem = function(data){
        vm.obj.workItemName = data.name;
        vm.obj.workItemCode = data.code;
        vm.obj.workItemId = data.workItemId;
        vm.catTaskSearch.catWorkItemTypeId=data.catWorkItemTypeId;
        CommonService.dismissPopup1();
        vm.listCatTaskPopupGrid.dataSource.page(1);
        vm.performerPopupGrid.dataSource.page(1);
        vm.obj.taskName = undefined;
        vm.obj.catTaskCode =undefined;
        vm.obj.catTaskId = undefined;
    }

    vm.choosePerformer = function(data){
        if(vm.windowId=='workItemPerson'){
            vm.workItemRecord.performerName = data.fullName;
            vm.workItemRecord.performerId = data.sysUserId;
        }else {
            vm.obj.performerName = data.fullName;
            vm.obj.performerIdDetail = data.sysUserId;
        }
        CommonService.dismissPopup1();
    }

    vm.chooseCatTask = function(data){
        vm.obj.taskName = data.name;
        vm.obj.catTaskCode = data.code;
        vm.obj.catTaskId = data.workItemId;
        CommonService.dismissPopup1();
    }

    vm.saveConstructionTask = function(obj){
        obj.constructionId=vm.constrObj.constructionId;
        obj.constructionCode=vm.constrObj.code;
        obj.constructionName=vm.constrObj.name;
//        chinhpxn20180711_start
        obj.sysGroupIdTC = vm.constrObj.sysGroupId;
//        chinhpxn20180711_end
        if(validateConstructionTask(obj))
        return Restangular.all("constructionTaskService/construction/addConstructionTaskTC").post(obj).then(function (d) {
            $("#ganttTC").data("kendoGantt").dataSource.read();
            if(d.error){
                toastr.error(d.error);
                return;
            }
            toastr.success("Thêm mới công việc thành công")
            //chinhpxn20180622
            vm.obj.workItemName = null;
            vm.obj.quantity = null;
            vm.obj.performerName = null;
            vm.obj.endDate = null;
            vm.obj.startDate = null;
            vm.obj.taskName = null;
            vm.obj.constructionCode = null;
            //chinhpxn20180622
            //CommonService.dismissPopup1();
        }).catch(function (e) {
            $("#ganttTC").data("kendoGantt").dataSource.read();
                toastr.error("Có lỗi xảy ra");
            return;
        });;
    }

    vm.listWorkItemPopupOptions = kendoConfig.getGridOptions({
        autoBind: true,
        scrollable: false,
        resizable: true,
        editable:false,
        dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            vm.countWorkItem = response.total;
                            return response.total; // total is returned in
                            // the "total" field of
                            // the response
                        },
                        data: function (response) {
                            return response.data; // data is returned in
                            // the "data" field of
                            // the response
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "workItemService/doSearchForTask",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            // vm.constructionSearch.employeeId =
                            // Constant.user.srvUser.catEmployeeId;
                            vm.workItemSearch.page = options.page
                            vm.workItemSearch.pageSize = options.pageSize
                            vm.workItemSearch.keySearch = vm.keySearch;
                            return JSON.stringify(vm.workItemSearch)

                        }
                    },
                    pageSize: 10
                },
        dataBinding: function () {
            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
        },
        pageable: {
            refresh: false,
            pageSize: 5,
            pageSizes: [5,10, 15, 20, 25],
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
                width: '10%',
                columnMenu: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:center;"
                }

            },
            {
                title: "Loại hạng mục",
                field: 'catWorkItemType',
                width: '15%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }

            },
            {
                title: "Mã hạng mục",
                field: 'code',
                width: '20%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }

            },
            {
                title: "Tên hạng mục",
                field: 'name',
                width: '30%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }
            },
            {
                title: "Trạng thái",
                field: 'status',
                width: '15%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                template: function (dataItem) {
                    if (dataItem.status == 1) {
                        return "<span name='status' font-weight: bold;'>Chưa thực hiện</span>"
                    } else if (dataItem.status == 2) {
                        return "<span name='status' font-weight: bold;'>Đang thực hiện</span>"
                    } else if (dataItem.status == 3) {
                        return "<span name='status' font-weight: bold;'>Đã hoàn thành</span>"
                    }
                }
            },
            {
                title: "Chọn",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                template: dataItem =>
                '<div class="text-center">'+
                '<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate >' +
                '<i id="#=code#"  ng-click="caller.chooseWorkItem(dataItem)"  class="fa fa-check color-green" aria-hidden="true"></i>' +
                '</a>'
                + '</div>',
                width: '10%'
            },
            {
                field : 'workItemId',
                hidden : true
            },
            {
                field : 'catWorkItemTypeId',
                hidden : true
            }
        ]
    });

    vm.listCatTaskPopupOptions = kendoConfig.getGridOptions({
        autoBind: true,
        scrollable: false,
        resizable: true,
        editable:false,
        dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            return response.total; 
                        },
                        data: function (response) {
                            return response.data; 
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "constructionService/doSearchCatTask",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.catTaskSearch.page = options.page
                            vm.catTaskSearch.pageSize = options.pageSize;
                            vm.catTaskSearch.keySearch = vm.keySearch;
                            return JSON.stringify(vm.catTaskSearch)

                        }
                    },
                    pageSize: 10
                },
        dataBinding: function () {
            record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
        },
        pageable: {
            refresh: false,
            pageSize: 5,
            pageSizes: [5,10, 15, 20, 25],
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
                width: '10%',
                columnMenu: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:center;"
                }

            },
            {
                title: "Hạng mục",
                field: 'catWorkItemType',
                width: '15%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }

            },
            {
                title: "Mã công việc",
                field: 'code',
                width: '20%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }

            },
            {
                title: "Tên công việc",
                field: 'name',
                width: '30%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }
            },
            {
                title: "Trạng thái",
                field: 'status',
                width: '15%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                },
                template: function (dataItem) {
                    if (dataItem.status == 1) {
                        return "<span name='status' font-weight: bold;'>Hiệu lực</span>"
                    } else if (dataItem.status == 0) {
                        return "<span name='status' font-weight: bold;'>Hết hiệu lực</span>"
                    }
                }
            },
            {
                title: "Chọn",
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                template: dataItem =>
                '<div class="text-center">'+
                '<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate >' +
                '<i id="#=code#"  ng-click="caller.chooseCatTask(dataItem)"  class="fa fa-check color-green" aria-hidden="true"></i>' +
                '</a>'
                + '</div>',
                width: '10%'
            },
            {
                field : 'workItemId',
                hidden : true
            }
        ]
    });
    vm.doSearchPerformer = function(){
        vm.performerPopupGrid1.dataSource.page(1);
    }
        
        vm.performerOptions1 = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable:false,
            dataSource: {
                        serverPaging: true,
                        schema: {
                            total: function (response) {
                                return response.total; 
                            },
                            data: function (response) {
                                return response.data; 
                            }
                        },
                        transport: {
                            read: {
                                // Thuc hien viec goi service
                                url: Constant.BASE_SERVICE_URL + "constructionService/doSearchPerformer",
                                contentType: "application/json; charset=utf-8",
                                type: "POST"
                            },
                            parameterMap: function (options, type) {
                                vm.performerSearch.page = options.page
                                vm.performerSearch.pageSize = options.pageSize
                                vm.performerSearch.sysGroupId = vm.constrObj.sysGroupId
                                //vm.performerSearch.keySearch = vm.keySearch;
                                return JSON.stringify(vm.performerSearch)

                            }
                        },
                        pageSize: 10
                    },
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            pageable: {
                refresh: false,
                pageSize: 10,
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
                    width: '10%',
                    columnMenu: false,
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    }

                },
                {
                    title: "Tên đăng nhập",
                    field: 'loginName',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }

                },
                {
                    title: "Mã nhân viên",
                    field: 'employeeCode',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }

                },
                {
                    title: "Họ tên",
                    field: 'fullName',
                    width: '20%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Email",
                    field: 'email',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    }
                },
                {
                    title: "Số điện thoại",
                    field: 'phoneNumber',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    }
                },
                {
                    title: "Chọn",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    template: dataItem =>
                    '<div class="text-center">'+
                    '<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate >' +
                    '<i id="#=code#"  ng-click="caller.choosePerformer1(dataItem)"  class="fa fa-check color-green" aria-hidden="true"></i>' +
                    '</a>'
                    + '</div>',
                    width: '10%'
                },
                {
                    field : 'sysUserId',
                    hidden : true
                }
            ]
        });

    vm.performerSearch ={};
    vm.performerOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable:false,
            dataSource: {
                        serverPaging: true,
                        schema: {
                            total: function (response) {
                                return response.total; 
                            },
                            data: function (response) {
                                return response.data; 
                            }
                        },
                        transport: {
                            read: {
                                // Thuc hien viec goi service
                                url: Constant.BASE_SERVICE_URL + "constructionService/doSearchPerformer",
                                contentType: "application/json; charset=utf-8",
                                type: "POST"
                            },
                            parameterMap: function (options, type) {
                                vm.performerSearch.page = options.page
                                vm.performerSearch.pageSize = options.pageSize
                                vm.performerSearch.sysGroupId = vm.constrObj.sysGroupId
                                vm.performerSearch.keySearch = vm.keySearch;
                                return JSON.stringify(vm.performerSearch)

                            }
                        },
                        pageSize: 10
                    },
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            pageable: {
                refresh: false,
                pageSize: 5,
                pageSizes: [5,10, 15, 20, 25],
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
                    width: '10%',
                    columnMenu: false,
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    }

                },
                {
                    title: "Tên đăng nhập",
                    field: 'loginName',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }

                },
                {
                    title: "Mã nhân viên",
                    field: 'employeeCode',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }

                },
                {
                    title: "Họ tên",
                    field: 'fullName',
                    width: '20%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "Email",
                    field: 'email',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    }
                },
                {
                    title: "Số điện thoại",
                    field: 'phoneNumber',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    }
                },
                {
                    title: "Chọn",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    template: dataItem =>
                    '<div class="text-center">'+
                    '<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate >' +
                    '<i id="#=code#"  ng-click="caller.choosePerformer(dataItem)"  class="fa fa-check color-green" aria-hidden="true"></i>' +
                    '</a>'
                    + '</div>',
                    width: '10%'
                },
                {
                    field : 'sysUserId',
                    hidden : true
                }
            ]
        });
    function validateConstructionTask(obj){
        if(obj.workItemId==undefined&&obj.type==1){
            toastr.warning("Hạng mục không được để trống!")
            return false;
        }else if((obj.quantity==undefined ||obj.quantity=='') && obj.type==1 ){
            toastr.warning("Sản lượng không được để trống!")
            return false;

        }else if(obj.performerIdDetail==undefined ||obj.performerIdDetail==''){
            toastr.warning("Người thực hiện không được để trống!")
            return false;

        }else if(obj.startDate==undefined ||obj.startDate==''){
            toastr.warning("Thời gian từ ngày không được để trống!")
            return false;

        }else if(obj.endDate==undefined ||obj.endDate==''){
            toastr.warning("Thời gian đến ngày không được để trống!")
            return false;
        }
        return true;
    }
    function changeTaskType(){
        //clear data when change type
        vm.obj.workItemName = null;
        vm.obj.workItemCode = null;
        vm.obj.workItemId = null;
        vm.catTaskSearch.catWorkItemTypeId=null;
        vm.obj.taskName = null;
        vm.obj.catTaskCode = null;
        vm.obj.catTaskId = null;
    }
    vm.typeConstruction = {
        dataSource: vm.constrTaskTypeData,
        dataTextField: "name",
        dataValueField: "id",
        valuePrimitive: true,
        select: changeTaskType
    }
    }