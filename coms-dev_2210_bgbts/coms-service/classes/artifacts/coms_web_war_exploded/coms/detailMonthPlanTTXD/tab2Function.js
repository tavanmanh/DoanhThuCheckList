/**
 * Created by pm1_os36 on 2/9/2018.
 */
function initTab2TTXDFunction($scope, $rootScope, $timeout, gettextCatalog,
                          kendoConfig, $kWindow, detailMonthPlanTTXDService,
                          CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm, htmlCommonService) {
    var THI_CONG = 1;

    vm.sumQuantity = 0;
    vm.searchForm = {};

    vm.searchFormHSHC = {};
    vm.searchFormForceMainTain = {};
    vm.searchFormBTS = {};
    vm.searchFormCashFlow = {};
    vm.searchFormOtherJobs = {};
    vm.searchFormRent={};
    vm.value = 1;
    $scope.fisrtElement = '#CC5424';
    vm.chooseTab = chooseTab;
//  hungnx 20180618 start
    vm.loadingUpdate = false;
  //hungnx 20180618 end
    vm.thueTramItem={};
    function chooseTab(value) {
        vm.detailMonthPlan.listConstrTaskIdDelete = [];
//        chinhpxn20180710_start
        vm.detailMonthPlan.constructionIdLst = [];
//        chinhpxn20180710_end
        
    }


    vm.editTab1 = function (dataItem,e) {
        var grid = vm.targetGrid;
        var row = $(e.target).closest("tr");
        vm.indexTab1 = row.index();
        var templateUrl = "coms/detailMonthPlanTTXD/popup-edit-thi-cong.html";
        var title = "Thông tin chi tiết thi công";
        var windowId = "THI_CONG";
        vm.thiCongItem = angular.copy(dataItem);
        vm.errMessageDateFrom=""
        vm.errMessageDateTo=""
        vm.constructionCode = vm.thiCongItem.constructionCode;
        vm.thiCongItem.quantity1 = vm.thiCongItem.quantity;
        CommonService.populatePopupCreate(templateUrl, title, vm.thiCongItem, vm, windowId, false, '1000', '600', "quantity");

    }

    vm.initAllTab = initAllTab
    function initAllTab() {
        vm.targetGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            dataSource: {
                serverPaging: true,
                schema: {
                    total: function (response) {
                        return response.total; // total is returned in
                        // the "total" field of
                        // the response
                    },
                    data: function (response) {
                        return response.data; // data is returned in
                        // the "data" field of
                        // the response
                    },
                },
                transport: {
                    read: {
                        // Thuc hien viec goi service
                        url: Constant.BASE_SERVICE_URL + "DetailMonthPlanOSRsService/constructionTask/doSearch",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.searchForm.page = options.page;
                        vm.searchForm.pageSize = options.pageSize;
                        vm.searchForm.detailMonthPlanId = vm.detailMonthPlan.detailMonthPlanId;
                        vm.searchForm.type = "8";
                        vm.detailMonthPlan.isTCImport = 0;
                        vm.detailMonthPlan.listConstrTaskIdDelete = [];
                        return JSON.stringify(vm.searchForm);


                    }
                },
                pageSize: 10
            },
            reorderable: true,
            noRecords: true,
            save: function () {
                var grid = this;
                setTimeout(function () {
                    grid.refresh();
                })
            },
            columnMenu: false,
            messages: {
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
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
                    width: '5%',
                    columnMenu: false,
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
                    editable: false
                },
                {
                    title: "Hợp đồng",
                    field: 'cntContractCode',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: false

                },
                {
                    title: "Giao chỉ tiêu",
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true,
                    columns: [
                    	{
                            title: "Sản lượng",
                            field: 'quantityTarget',
                            width: '10%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            editable: false,
                            format: "{0:n3}",
                        },
                        {
                            title: "Doanh thu",
                            field: 'revenueTarget',
                            width: '10%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            editable: false,
                            format: "{0:n3}",
                        },
                        {
                            title: "Yêu cầu khác",
                            field: 'otherTarget',
                            width: '10%',
                            headerAttributes: {style: "text-align:center;font-weight: bold;"},
                            attributes: {
                                style: "text-align:left;"
                            },
                            editable: false,
                        },
                    ]
                },
                {
                    title: "Người thực hiện",
                    field: 'performerName',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true,
                },
                {
                    title: "Người điều hành",
                    field: 'supervisorName',
                    width: '10%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    editable: true,
                },
                {
                    title: "Ngày hoàn thành",
                    field: 'endDate',
                    width: '15%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:right;"
                    },
                    editable: false,
                },
                {
                    title: "Thao tác",
                    template: dataItem =>
                    '<div class="text-center">'
                    + '<button style=" border: none; background-color: white;" id="updateId" ng-hide="dataItem.status==0" ng-click="vm.editTab1(dataItem,$event)" class=" icon_table "' +
                    '   uib-tooltip="Sửa" translate>' +
                    '<i class="fa fa-list-alt" style="color:#e0d014" ng-hide="dataItem.status==0"   aria-hidden="true"></i>' +
                    '</button>'
                    +
		        	'		<button ng-show="vm.showHideButtonRemove(dataItem)" style=" border: none; background-color: white;" class=" icon_table" uib-tooltip="Khóa" translate> '+
		        	'			<i style="color:grey" class="fa fa-trash" aria-hidden="true"></i>'+
		        	'		</button> '+
		        	'		<button ng-hide="vm.showHideButtonRemove(dataItem)" style=" border: none; background-color: white;" ng-click=vm.removeTargetRow(dataItem,$event) class=" icon_table" uib-tooltip="Xóa" translate>'+
		        	'			<i style="color:steelblue;" class="fa fa-trash" aria-hidden="true"></i> '+
		        	'		</button>'
                    + '</div>',
                    field: 'action',
                    width: '10%'

                }
            ],
        });
    }
        vm.showHideButtonRemove = function (dataItem) {
//			
				if(dataItem.completePercent > '0' ){
				return true;
			}
			else {
				
					return false;
				 
			}
		}

vm.removeTargetRow=removeTargetRow;
function removeTargetRow(data,e){
	confirm('Bạn thật sự muốn xóa bản ghi đã chọn?',function (){
		if(data.constructionTaskId != null){
			detailMonthPlanTTXDService.removeRow(data).then(
					function(d) {
						toastr.success("Xóa thành công!");
						$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
		        		var sizePage = $("#targetGrid").data("kendoGrid").dataSource.total();
						var pageSize = $("#targetGrid").data("kendoGrid").dataSource.pageSize();
						if(sizePage % pageSize == 1){
							var currentPage = $("#targetGrid").data("kendoGrid").dataSource.page();
					        if (currentPage > 1) {
					            $("#targetGrid").data("kendoGrid").dataSource.page(currentPage - 1);
					        }
						}
						$('#targetGrid').data('kendoGrid').dataSource.read();
						$('#targetGrid').data('kendoGrid').refresh();
					}, function(errResponse) {
						toastr.error("Lỗi không xóa được!");
					});
		} else {
			var grid = vm.targetGrid;
	        var row = $(e.target).closest("tr");
	        var dataItem = grid.dataItem(row);
	        if(dataItem.constructionTaskId != null){
	        	vm.detailMonthPlan.listConstrTaskIdDelete.push(dataItem.constructionTaskId);
	        }
	        grid.removeRow(dataItem);
	        grid.dataSource.remove(dataItem);
	        grid.dataSource.sync();
	        grid.refresh();
	        toastr.success("Xóa thành công!");
		}
		} 
	)
}

vm.openDepartmentChoose = openDepartmentChoose
function openDepartmentChoose(popUp) {
    vm.obj = {};
    vm.departmentpopUp = popUp;
    var templateUrl = 'coms/popup/findDepartmentPopUp.html';
    var title = gettextCatalog.getString("Tìm kiếm đơn vị");
    CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
}

    vm.openDepartmentChoose = openDepartmentChoose
    function openDepartmentChoose(popUp) {
        vm.obj = {};
        vm.departmentpopUp = popUp;
        var templateUrl = 'coms/popup/findDepartmentPopUp.html';
        var title = gettextCatalog.getString("Tìm kiếm đơn vị");
        CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
    }

    vm.onSave = onSave;
    function onSave(data) {
        if (vm.departmentpopUp === 'dept') {
            vm.selectedDeptChoose = true;
            vm.detailMonthPlan.sysGroupName = data.text;
            vm.detailMonthPlan.sysGroupId = data.id;
            vm.sysGroupCode = data.code;
            vm.changeCodeName();
        }
        if (vm.departmentpopUp === 'dept1') {
            vm.detailMonthPlanSearch.sysGroupName = data.text;
            vm.detailMonthPlanSearch.sysGroupId = data.id;
        }
    }

// clear data
    vm.changeDataDept = changeDataDept
    function changeDataDept(id) {
        switch (id) {
            case 'dept':
            {
                if (processSearch(id, vm.selectedDeptChoose)) {
                    vm.detailMonthPlan.sysGroupId = null;
                    vm.detailMonthPlan.sysGroupName = null;
                    vm.selectedDeptChoose = false;
                }
                break;
            }
        }
    }

    vm.cancelInputDept = function (param) {
        if (param == 'dept') {
            vm.detailMonthPlan.sysGroupId = null;
            vm.detailMonthPlan.sysGroupName = null;
        }
    }
// 8.2 Search SysGroup
    vm.selectedDeptChoose = false;
    vm.deptOptions1 = {
        dataTextField: "text",
        dataValueField: "id",
        select: function (e) {
            vm.selectedDeptChoose = true;
            var dataItem = this.dataItem(e.item.index());
            vm.detailMonthPlan.sysGroupName = dataItem.text;
            vm.detailMonthPlan.sysGroupId = dataItem.id;
            vm.sysGroupCode = dataItem.code;
            vm.changeCodeName();
        },
        pageSize: 10,
        open: function (e) {
            vm.selectedDeptChoose = false;
        },
        dataSource: {
            serverFiltering: true,
            transport: {
                read: function (options) {
                    vm.selectedDeptChoose = false;
                    return Restangular.all("departmentRsService/department/" + 'getForAutocompleteDept').post({
                        name: vm.detailMonthPlan.sysGroupName,
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
                vm.detailMonthPlan.sysGroupName = null;// thành name
                vm.detailMonthPlan.sysGroupId = null;
            }
        },
        ignoreCase: false
    }
    vm.cancelImport = cancelImport;
    function cancelImport() {
        CommonService.dismissPopup1();
    }

    vm.importExcelTab2 = importExcelTab2;
    function importExcelTab2() {
        var teamplateUrl = "coms/detailMonthPlanTTXD/import-popup.html";
        var title = "Import từ file excel";
        var windowId = "DETAIL_MONTH_PLAN";
        CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '700', 'auto', "code");
    }

    vm.addTargetTemp = addTargetTemp;
    function addTargetTemp() {
        if (validateTargetTemp()) {
            var grid = vm.targetGrid;
            if (grid) {
                var detailList = grid.dataSource._data;
                var valid = true;
                angular.forEach(detailList, function (dataItem) {
                    if (vm.detailMonthPlan.sysGroupId == dataItem.sysGroupId) {
                        valid = false;
                    }
                })
                if (valid) {
                    grid.dataSource.add(vm.detailMonthPlan);
                    grid.dataSource.sync();
                    grid.refresh();
                } else {
                    toastr.warning("Đơn vị " + vm.detailMonthPlan.sysGroupName + " đã tồn tại!");
                }
            }
        }
    }

    function validateMaintainTemp() {
        if (vm.detailMonthPlan.sysGroupId == undefined) {
            toastr.warning("Bạn phải chọn đơn vị!");
            return false;
        }
        if (vm.detailMonthPlan.buildPlan == undefined || vm.detailMonthPlan.buildPlan == '') {
            toastr.warning("Bạn phải nhập xây dựng!");
            return false;
        }
        if (vm.detailMonthPlan.installPlan == undefined || vm.detailMonthPlan.installPlan == '') {
            toastr.warning("Bạn phải nhập KH lắp cột bao");
            return false;
        }
        if (vm.detailMonthPlan.replacePlan == undefined || vm.detailMonthPlan.buildPlan == '') {
            toastr.warning("Bạn phải nhập KH thay thân cột");
            return false;
        }
        if (vm.detailMonthPlan.teamBuildRequire == undefined || vm.detailMonthPlan.teamBuildRequire == '') {
            toastr.warning("Bạn phải nhập số đội cần  XD");
            return false;
        }
        if (vm.detailMonthPlan.teamBuildAvaiable == undefined || vm.detailMonthPlan.teamBuildAvaiable == '') {
            toastr.warning("Bạn phải nhập số đội đang XD");
            return false;
        }
        if (vm.detailMonthPlan.teamInstallRequire == undefined || vm.detailMonthPlan.teamInstallRequire == '') {
            toastr.warning("Bạn phải nhập số đội cần lắp dựng");
            return false;
        }
        if (vm.detailMonthPlan.teamInstallAvaiable == undefined || vm.detailMonthPlan.teamInstallAvaiable == '') {
            toastr.warning("Bạn phải nhập số đội đang lắp dựng");
            return false;
        }
        return true;
    }

    function validateTargetTemp() {
        if (vm.detailMonthPlan.sysGroupId == undefined) {
            toastr.warning("Bạn phải chọn đơn vị!");
            return false;
        }
        if (vm.detailMonthPlan.quantity == undefined) {
            toastr.warning("Bạn phải nhập sản lượng!");
            return false;
        }
        if (vm.detailMonthPlan.complete == undefined) {
            toastr.warning("Bạn phải nhập HSHC");
            return false;
        }
        if (vm.detailMonthPlan.revenue == undefined) {
            toastr.warning("Bạn phải nhập doanh thu");
            return false;
        }
        return true;
    }

    function validateSourceTemp() {
        if (vm.detailMonthPlan.sysGroupId == undefined) {
            toastr.warning("Bạn phải chọn đơn vị!");
            return false;
        }
        if (vm.detailMonthPlan.source == undefined) {
            toastr.warning("Bạn phải nhập nguồn việc!");
            return false;
        }
        return true;
    }

    vm.getExcelTemplate = function () {
        var obj = {};
        if (vm.value == THI_CONG) {
            obj.request = 'exportTemplateTargetTTXD';
        }
        
        obj.sysGroupId = vm.detailMonthPlan.sysGroupId;
        detailMonthPlanTTXDService.downloadTemplate(obj).then(function (d) {

            data = d.plain();
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        }).catch(function (e) {
            //toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
            return;
        });
    }
    vm.saveImportFile = saveImportFile;
    function saveImportFile() {
        if ($("#fileImportDetailMonth")[0].files[0] == null) {
            toastr.warning("Bạn chưa chọn file để import");
            return;
        }
        if ($("#fileImportDetailMonth")[0].files[0].name.split('.').pop() != 'xls' && $("#fileImportDetailMonth")[0].files[0].name.split('.').pop() != 'xlsx') {
            toastr.warning("Sai định dạng file");
            return;
        }
        var formData = new FormData();
        formData.append('multipartFile', $('#fileImportDetailMonth')[0].files[0]);
        vm.detailMonthPlan.listConstrTaskIdDelete = [];
        if (vm.value == "1") {
            importTargetFile(formData);
        } 
    }

    //Quangnd - start

    function getDataGrid(id) {
        var grid = $(id).data("kendoGrid");
        if (grid) {
            return grid.dataSource._data;
        }
        return null;
    }

    function fillDataGrid(data, id) {
        var grid = $(id).data("kendoGrid");
        if (grid) {
            grid.dataSource.data(data);
            grid.refresh();
        }
    }

    vm.updateListTC = function () {
    //  hungnx 20180618 start
        vm.loadingUpdate = true;
      //hungnx 20180618 end
        vm.detailMonthPlan.listTC = vm.targetGrid.dataSource._data;
        detailMonthPlanTTXDService.updateListTC(vm.detailMonthPlan).then(function (response) {
            toastr.success("Ghi lại thành công!");
        //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
        }, function (error) {
            toastr.error("Có lỗi xảy ra!");
        //  hungnx 20180618 start
            vm.loadingUpdate = false;
          //hungnx 20180618 end
        });
    }
  
    //end
    function importTargetFile(formData) {
    //  hungnx 20180618 start
    	$('#savepopupMonth').prop('disabled', true);
    	htmlCommonService.showLoading('#loadingIpMonth');
    //  hungnx 20180618 end
        return $.ajax({
            url: Constant.BASE_SERVICE_URL + "DetailMonthPlanOSRsService/importTargetTTXD",
            type: "POST",
            data: formData,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            success: function (data) {
                if (data.length === 0) {
                    toastr.warning("File imp" +
                    "ort không có dữ liệu");
                    return;
                } else if (data[data.length - 1].errorFilePath != null) {
                    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                    toastr.warning("File import không hợp lệ");
                    return;
                }
                else {
                    toastr.success("Import dữ liệu thành công");

                    var grid = vm.targetGrid;
                    if (grid != undefined) {
                        grid.dataSource.data(data);
                        grid.refresh();
                        vm.detailMonthPlan.isTCImport = 1;
                    }
                }
                vm.cancelImport();
            }
        }).always(function() {
//          hungnx 20180618 start
        	$('#savepopupMonth').prop('disabled', false);
        	htmlCommonService.hideLoading('#loadingIpMonth');
//          hungnx 20180618 end
		});

    }


    function fillGrid(id, data) {
        var grid = $(id).data("kendoGrid");
        grid.dataSource.data(data);
        grid.refresh();
    }

    //luanlv-thongtin-thang-tongthe
    vm.getTotalMonthPlanDetail = getTotalMonthPlanDetail;
    function getTotalMonthPlanDetail() {
        var teamplateUrl = "coms/detailMonthPlanTTXD/total-month-plan-detail-os-form.html";
        var title = "Thông tin kế hoạch tháng tổng thể ngoài OS";
        var windowId = "yearPlan1";
        vm.popUpOpen = 'detailMonth';
        CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '700', '', "");
    }

    vm.cancelPopupDetail = cancelPopupDetail;
    function cancelPopupDetail() {
        CommonService.dismissPopup1();
    }

    $scope.$on("Popup.open", function () {
        if (vm.popUpOpen == 'detailMonth') {
            detailMonthPlanTTXDService.getYearPlanDetailTarget(vm.detailMonthPlan).then(function (result) {
                //var grid = $("#yearPlanDetail").data("kendoGrid");
                var grid = vm.planDetailOSGrid;
                if (grid) {
                    grid.dataSource.data(result);
                    grid.refresh();
                }
            }, function (error) {

            });
        }
    });
    
    

    vm.checkDateFrom = checkDateFrom;
    function checkDateFrom() {
        var startDate = $('#createFrom').val();
        var endDate = $('#createTo').val();
        vm.errMessageDateFrom = '';
        var curDate = new Date();

        if (endDate !== "" && startDate !== "") {
            if (kendo.parseDate(startDate, "dd/MM/yyyy") > kendo.parseDate(endDate, "dd/MM/yyyy")) {
                vm.errMessageDateFrom = 'Ngày thực hiện từ phải nhỏ hơn bằng Ngày thực hiện đến';
                $("#createFrom").focus();
                vm.invalidDate = true;
                return vm.errMessageDateFrom;
            } else {
                vm.errMessageDateTo = '';
                vm.invalidDate = false;
            }

        }

    }

    vm.checkDateTo = checkDateTo;
    function checkDateTo() {
        var startDate = $('#createFrom').val();
        var endDate = $('#createTo').val();
        vm.errMessageDateTo = '';
        var curDate = new Date();

        if (startDate !== "" && endDate != "") {
            if (kendo.parseDate(startDate, "dd/MM/yyyy") > kendo.parseDate(endDate, "dd/MM/yyyy")) {
                vm.errMessageDateTo = 'Ngày thực hiện từ phải nhỏ hơn bằng Ngày thực hiện đến';
                $("#createTo").focus();
                vm.invalidDate = true;
                return vm.errMessageDateTo;
            } else {
                vm.errMessageDateFrom = '';
                vm.invalidDate = false;
            }
        }
    }

    vm.cancelEditPopup = cancelEditPopup;
    function cancelEditPopup() {
        CommonService.dismissPopup1();
    }

    vm.saveQuantityTTXDPopup = saveQuantityTTXDPopup;
    function saveQuantityTTXDPopup() {
        if (vm.thiCongItem.quantityTarget == undefined || vm.thiCongItem.quantityTarget == "") {
            toastr.warning("Giá trị sản lượng không được để trống")
            return;
        } else if (vm.thiCongItem.revenueTarget == undefined || vm.thiCongItem.revenueTarget == "") {
            toastr.warning("Giá trị doanh thu không được để trống")
            return;
        }
        else if (vm.thiCongItem.otherTarget == undefined || vm.thiCongItem.otherTarget == "") {
            toastr.warning("Giá trị yêu cầu khác không được để trống")
            return;
        }
        else if (vm.thiCongItem.performerName == undefined || vm.thiCongItem.performerName == '') {
            toastr.warning("Người thực hiện không được để trống")
            return;
        }
        else if (vm.thiCongItem.supervisorName == undefined || vm.thiCongItem.supervisorName == '') {
            toastr.warning("Người điều hành không được để trống")
            return;
        }
        vm.cancelEditPopup();
        toastr.success("Ghi lại thành công");
        var list= vm.targetGrid.dataSource.data();
        var dataItem = list[vm.indexTab1];
//        var quan = vm.thiCongItem.quantity.replace(/\,/g,'');
        dataItem.performerId = vm.thiCongItem.performerId;
        dataItem.performerName = vm.thiCongItem.performerName;
        dataItem.supervisorId = vm.thiCongItem.supervisorId;
        dataItem.supervisorName = vm.thiCongItem.supervisorName;
        dataItem.quantityTarget =vm.thiCongItem.quantityTarget;
        dataItem.revenueTarget =vm.thiCongItem.revenueTarget;
        dataItem.otherTarget =vm.thiCongItem.otherTarget;
        dataItem.endDate = vm.thiCongItem.endDate;
        vm.targetGrid.refresh();


    }

//luanlv-end

    function updateDataTable(x, id) {
        var listData = getDataGrid(id);
        for (var i = 0; i < listData.length; i++) {
            if (x.constructionCode == listData[i].constructionCode) {
                listData[i] = x;
            }
            ;
        }
        fillDataGrid(listData, id);
    }

//save popup dong tien
  vm.addComma = addComma;
  function addComma(model, attr){
    	vm[model][attr] = htmlCommonService.addThousandComma(vm[model][attr]);
  }

  
  vm.cancel = function(){
	  $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
  }
  
    vm.doSearchDetail = doSearchDetail
    function doSearchDetail(id) {
        vm.detailMonthPlan.listConstrTaskIdDelete = [];
        $('#' + id).data("kendoGrid").dataSource.page(1);
        if ('OtherJobs' == id) {
            vm.detailMonthPlan.isCVKImport = 0;
        } else if ('forceMaintainGrid' == id) {
            vm.detailMonthPlan.isLDTImport = 0;
        } else if ('BTSGrid' == id) {
            vm.detailMonthPlan.isYCVTImport = 0;
        } else if ('CashFlow' == id) {
            vm.detailMonthPlan.isDTImport = 0;
        } else if ('targetGrid' == id) {
            vm.detailMonthPlan.isTCImport = 0;
        } else if ('sourceGrid' == id) {
            vm.detailMonthPlan.isHSHCImport = 0;
        } else if ('rentGroundGrid' == id) {
            vm.detailMonthPlan.isRentImport = 0;
        }
    }
    vm.cancelDateFrom = cancelDateFrom
    function cancelDateFrom(id){
        if(id=='thicong')
            vm.thiCongItem.startDate = undefined;
        else if(id=='hshc')
            vm.hoanCongItem.startDate = undefined;
        else if (id=='ldt')
            vm.doanhThuItem.startDate = undefined;
        vm.checkDateFrom()
    }
    
    vm.cancelDateTo = cancelDateTo
    function cancelDateTo(id,formTo,formFrom){
        if(id=='thicong') {
            vm.thiCongItem.endDate = undefined;
            vm.thiCongItem.startDate = undefined;
            $rootScope.validateDate( vm.thiCongItem.endDate,null,null,formTo)
            $rootScope.validateDate( vm.thiCongItem.startDate,null,null,formFrom)
        }
        else if(id=='hshc') {
            vm.hoanCongItem.startDate = undefined;
            vm.hoanCongItem.endDate = undefined;
            $rootScope.validateDate( vm.hoanCongItem.endDate,null,null,formTo)
            $rootScope.validateDate( vm.hoanCongItem.startDate,null,null,formFrom)
        }
        else if (id=='ldt') {
            vm.doanhThuItem.endDate = undefined;
            vm.doanhThuItem.startDate = undefined;
            $rootScope.validateDate( vm.doanhThuItem.endDate,null,null,formTo)
            $rootScope.validateDate( vm.doanhThuItem.startDate,null,null,formFrom)
        }
        vm.checkDateTo();
        vm.checkDateFrom();
    }

    //Huy-end
}
