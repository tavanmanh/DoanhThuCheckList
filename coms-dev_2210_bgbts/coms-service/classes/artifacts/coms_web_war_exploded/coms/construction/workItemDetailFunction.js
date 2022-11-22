/**
 * Created by pm1_os42 on 2/24/2018.
 */
function workItemDetailFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
    kendoConfig, $kWindow, constructionService,
    CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm){
    vm.openDepartmentChoose = openDepartmentChoose
    vm.workItemSearch={}
    vm.partnerSearch={}
    vm.userTemplate='<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;float:left">#: data.employeeCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
        vm.userHeaderTemplate='<div class="dropdown-header row text-center k-widget k-header">' +
        '<p class="col-md-6 text-header-auto border-right-ccc">Mã nhân viên</p>' +
        '<p class="col-md-6 text-header-auto">Tên nhân viên</p>' +
        '</div>';
    function openDepartmentChoose(popUp) {
        vm.obj = {};
        vm.departmentpopUp = popUp;
        var templateUrl = 'coms/popup/findDepartmentPopUp.html';
        var title = gettextCatalog.getString("Tìm kiếm đơn vị");
        CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
    }
    vm.openPartnerChoose  = openPartnerChoose
    function openPartnerChoose(popUp) {
        var templateUrl = 'coms/construction/popupPartner.html';
        var title = gettextCatalog.getString("Tìm kiếm đối tác");
        CommonService.populatePopupPartner(templateUrl, title, null, vm, "partner", true, '800', '600', null);
    }

    vm.selectedDeptWorkChoose = false;
    vm.deptWorkOptions = {
        dataTextField: "text",
        dataValueField: "id",
        select: function (e) {
            vm.selectedDeptWorkChoose = true;
            var dataItem = this.dataItem(e.item.index());
            vm.workItemRecord.constructorName1 = dataItem.text;
            vm.workItemRecord.constructorId = dataItem.id;
        },
        pageSize: 10,
        open: function (e) {
            vm.selectedDeptWorkChoose = false;
        },
        dataSource: {
            serverFiltering: true,
            transport: {
                read: function (options) {
                    vm.selectedDeptWorkChoose = false;
                    return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({
                        name: vm.workItemRecord.constructorName1,
                        pageSize: vm.deptWorkOptions.pageSize
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
                vm.workItemRecord.constructorName1 = null;// thành name
                vm.workItemRecord.constructorId = null;
            }
        },
        ignoreCase: false
    }
    vm.selectedPartChoose = false;
    vm.partOptions = {
        dataTextField: "text",
        dataValueField: "id",
        select: function (e) {
            vm.selectedPartChoose = true;
            var dataItem = this.dataItem(e.item.index());
            vm.workItemRecord.constructorName2 = dataItem.text;
            vm.workItemRecord.constructorId = dataItem.id;
        },
        pageSize: 10,
        open: function (e) {
            vm.selectedPartChoose = false;
        },
        dataSource: {
            serverFiltering: true,
            transport: {
                read: function (options) {
                    vm.selectedPartChoose = false;
                    return Restangular.all("departmentRsService/catPartner/" + 'getForAutocompleteDept').post({
                        name: vm.workItemRecord.constructorName2,
                        pageSize: vm.partOptions.pageSize
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
                vm.workItemRecord.constructorName2 = null;// thành name
                vm.workItemRecord.constructorId = null;
            }
        },
        ignoreCase: false
    }
    vm.selectedSupChoose = false;
    vm.supOptions = {
        dataTextField: "text",
        dataValueField: "id",
        select: function (e) {
            vm.selectedSupChoose = true;
            var dataItem = this.dataItem(e.item.index());
            vm.workItemRecord.supervisorName = dataItem.text;
            vm.workItemRecord.supervisorId = dataItem.id;
        },
        pageSize: 10,
        open: function (e) {
            vm.selectedSupChoose = false;
        },
        dataSource: {
            serverFiltering: true,
            transport: {
                read: function (options) {
                    vm.selectedSupChoose = false;
                    return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({
                        name: vm.workItemRecord.supervisorName,
                        pageSize: vm.supOptions.pageSize,
                        page: 1
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
                vm.workItemRecord.supervisorName = null;// thành name
                vm.workItemRecord.supervisorId = null;
            }
        },
        ignoreCase: false
    }
    vm.doSearchWorkItem=doSearchWorkItem;
    function doSearchWorkItem(){
        var grid =vm.workItemGrid;
//        grid.dataSource.refresh();
        if(grid){        	
            grid.dataSource.query({
                page: grid.dataSource.page(),
                pageSize: 10
            });
        }
    }
    vm.cancelWorkItemPopup=cancelWorkItemPopup;
    function cancelWorkItemPopup(){
        vm.doSearchWorkItem();
        CommonService.dismissPopup();
//        $(".k-icon.k-i-close").click();
        vm.workItemRecord={isInternal:1};
        if(!!vm.assignCallback){
        	vm.assignCallback();
        } else
        	vm.obj={type:1};
    }
    vm.saveWorkItem = saveWorkItem;
    function saveWorkItem(data){
        //VietNT_20190104_start
        //vm.disabledHangMuc = true;
        //VietNT_end
		kendo.ui.progress($(".formWork"), true);
		if(vm.checkRevenueBranch=="1" && (vm.workItemRecord.branch==undefined || vm.workItemRecord.branch=="")) {
			toastr.warning("Trụ không được để trống do công trình thuộc hợp đồng chia sẻ doanh thu !")
            return;
		}
        if(vm.workItemRecord.workItemId==null){
            if((vm.workItemRecord.constructorId==undefined||vm.workItemRecord.constructorId=='')&& vm.workItemRecord.isInternal==1){
                toastr.warning("Đơn vị không được để trống!")
                return;
            }
            if((vm.workItemRecord.constructorName2 == undefined ||vm.workItemRecord.constructorName2=='') && vm.workItemRecord.isInternal==2){
                toastr.warning("Đối tác không được để trống!")
                return;
            }
            if(vm.workItemRecord.workItemTypeList.length==0){
                toastr.warning("Loại hạng mục không được để trống!")
                return;
            }
           
            vm.workItemRecord.constructionId=vm.constrObj.constructionId;
            vm.workItemRecord.constructionCode=vm.constrObj.code
                Restangular.all("workItemService"+"/add").post(vm.workItemRecord).then(function(result){
                    if (result.error) {
                        toastr.error(result.error);
						kendo.ui.progress($(".formWork"), false);
                        return;
                    }
                    toastr.success("Thêm mới thành công!");
					kendo.ui.progress($(".formWork"), false);
                    cancelWorkItemPopup();
//                vm.doSearchWorkItem();
            },function(error){
					kendo.ui.progress($(".formWork"), false);
            })


        }else{
            if((vm.workItemRecord.constructorId==undefined||vm.workItemRecord.constructorId=='')&& vm.workItemRecord.isInternal==1){
                toastr.warning("Đơn vị/đối tác không được để trống!")
                return;
            }
/*            if(vm.workItemRecord.performerId==undefined||vm.workItemRecord.performerId==''){
                toastr.warning("Nhân viên phụ trách hạng mục không được để trống!")
                return;
            }*/
            if(vm.workItemRecord.supervisorName==null||vm.workItemRecord.supervisorName==''){
            	toastr.warning("Đơn vị giám sát không được để trống!")
                return;
            }
            if((vm.workItemRecord.constructorName2 == undefined ||vm.workItemRecord.constructorName2=='') && vm.workItemRecord.isInternal==2){
                toastr.warning("Đối tác không được để trống!")
                return;
            }
             Restangular.all("workItemService"+"/updateInConstruction").post(vm.workItemRecord).then(function(result){
                if (result.error) {
                    toastr.error(result.error);
					kendo.ui.progress($(".formWork"), false);
                    return;
                }
                toastr.success("Chỉnh sửa thành công!");
				kendo.ui.progress($(".formWork"), false);
                cancelWorkItemPopup();
//                vm.doSearchWorkItem();
            },function(error){
				kendo.ui.progress($(".formWork"), false);
            })
        }
    }
  //Huy thong tin lo hang
	vm.removeWorkItem=removeWorkItem;
	function removeWorkItem(dataItem){
		dataItem.catConstructionTypeId = vm.constrObj.catConstructionTypeId;
		confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function(){
			constructionService.removeGpon(dataItem).then(
				function(d) {
					if(d && d.error){
						toastr.error(d.error);
						return;
					}
					toastr.success("Xóa hạng mục thành công!");
					var sizePage = $("#workItemTable").data("kendoGrid").dataSource.total();
					var pageSize = $("#workItemTable").data("kendoGrid").dataSource.pageSize();
					if(sizePage % pageSize === 1){
						var currentPage = $("#workItemTable").data("kendoGrid").dataSource.page();
						if (currentPage > 1) {
							$("#workItemTable").data("kendoGrid").dataSource.page(currentPage - 1);
						}
					}
					 $("#workItemTable").data('kendoGrid').dataSource.read();
					 $("#projectManagerGrid").data('kendoGrid').refresh();

				}, function(errResponse) {
					toastr.error("Lỗi không xóa được!");
				});
		} )
	}
    vm.closePartner = closePartner;
    function closePartner(){
        CommonService.dismissPopup();
        vm.partnerSearch={};
    }
    vm.choosePartner = function(data){
        vm.workItemRecord.constructorName2 = data.text;
        vm.workItemRecord.constructorId = data.id;
        CommonService.dismissPopup();
    }
    vm.doPartnerSearch = function(){
        vm.listPartnerPopup.dataSource.page(1);
    }
    vm.listPartnerPopupOptions = kendoConfig.getGridOptions({
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
                    vm.countCons = response.total;
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
                    url: Constant.BASE_SERVICE_URL + "departmentRsService/catPartner/" + "doSearch",
                    contentType: "application/json; charset=utf-8",
                    type: "POST"
                },
                parameterMap: function (options, type) {
                    // vm.constructionSearch.employeeId =
                    // Constant.user.srvUser.catEmployeeId;
                    vm.partnerSearch.page = options.page
                    vm.partnerSearch.pageSize = options.pageSize

                    return JSON.stringify(vm.partnerSearch)

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
                width: '10%',
                columnMenu: false,
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:center;"
                }

            },
            {
                title: "Loại đối tác",
                field: 'partnerType',
                width: '20%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }, template: function (dataItem) {
                if (dataItem.partnerType == 1) {
                    return "<span name='status' font-weight: bold;'>Đối tác ngoài Viettel</span>"
                } else if (dataItem.partnerType == 0) {
                    return "<span name='status' font-weight: bold;'>Đối tác trong Viettel</span>"
                }
            }

            },
            {
                title: "Mã đối tác",
                field: 'code',
                width: '15%',
                headerAttributes: {style: "text-align:center;font-weight: bold;"},
                attributes: {
                    style: "text-align:left;"
                }

            },
            {
                title: "Tên đối tác",
                field: 'text',
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
                template:
                '<div class="text-center "> '	+
                '		<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate>'+
                '			<i id="#=code#" ng-click=caller.choosePartner(dataItem) class="fa fa-check color-green" aria-hidden="true"></i> '+
                '		</a>'
                +'</div>',
                width: "10%"
            }
        ]
    });
    vm.selectedperformerItem = false;
    vm.performerItemOptions = {
        dataTextField: "fullName",
        dataValueField: "sysUserId",
        select: function (e) {
            vm.selectedperformerItem = true;
            var data = this.dataItem(e.item.index());
            vm.workItemRecord.performerName = data.fullName;
            vm.workItemRecord.performerId = data.sysUserId;
        },
        pageSize: 10,
        open: function (e) {
            vm.selectedperformerItem = false;
        },
        dataSource: {
            serverFiltering: true,
            transport: {
                read: function (options) {
                    return Restangular.all("constructionTaskService/construction/searchPerformer").post({
                        keySearch: vm.workItemRecord.performerName,
                        pageSize: vm.performerItemOptions.pageSize,
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
        template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.loginName #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
        change: function (e) {
            if (e.sender.value() === '') {
                vm.workItemRecord.performerName = null;
                vm.workItemRecord.performerId = null;
            }
        },
        ignoreCase: false
    }
}