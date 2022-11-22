/**
 * Created by pm1_os49 on 5/7/2018.
 */
(function () {

    'use strict';
    var controllerId = 'issueController';
    angular.module('MetronicApp').controller(controllerId, issueController);
    function issueController($scope, $rootScope, $timeout, gettextCatalog,
                                kendoConfig, $kWindow, issueService,$filter,htmlCommonService,
                                CommonService, PopupConst, Restangular, RestEndpoint, Constant) {
        initCommonFunction($scope, $rootScope, $filter);
        var vm = this;
        vm.pageSizeDiscuss = 5;
        vm.pageSizeHistory = 5;
        vm.issueItem = {};
        vm.showDetail = false;
        vm.issueSearch = {
            status: -1,
            state: ''
        };
        vm.String = "Quản lý công trình > Quản lý công trình > Quản lý phản ánh";
        initFormData();
        function initFormData() {
            fillDataTable([]);
        }

        var record = 0;

        function fillDataTable(data) {
            vm.issueGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: [
                    {
                        name: "actions",
                        template: '<div class="btn-group pull-right margin_top_button margin10">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.issueGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#appCountIssue").text("" + response.total);
                            vm.count = response.total;
                            return response.total; // total is returned in
                            // the "total" field of
                            // the response
                        },
                        data: function (response) {
                            var list = response.data;
                            return response.data; // data is returned in
                            // the "data" field of
                            // the response
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "issueRsService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            // vm.yearPlanSearch.employeeId =
                            // Constant.user.srvUser.catEmployeeId;
                            vm.issueSearch.page = options.page
                            vm.issueSearch.pageSize = options.pageSize

                            return JSON.stringify(vm.issueSearch)

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
                        title: "Mã công trình",
                        field: 'constructionCode',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    }, {
                        title: "Nội dung phản ánh",
                        field: 'content',
                        width: '25%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    }, {
                        title: "Người phản ánh",
                        field: 'createdUserName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Ngày phản ánh",
                        field: 'createdDate',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Người xử lý",
                        field: 'currentHandingUserName',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Trạng thái phản ánh",
                        field: 'status',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: "# if(status == 1){ #" + "#= 'Mở' #" + "# } " + "else if (status == 0) { # " + "#= 'Đóng' #" + "#}" + " #"
                    },

                    {
                        title: "Tình trạng phản ánh",
                        field: 'state',
                        template: "# if(state == 1){ #" + "#= 'Chưa quá hạn' #" + "# } " + "else if (state == 2) { # " + "#= 'Quá hạn' #" + "#} #",
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    }, {
                        title: "Thao tác",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        width: '15%',
                        template: dataItem =>
                        '<div class="text-center">'
                        + '<button  style=" border: none; background-color: white;" id="updateId"  ng-click="vm.edit(dataItem)" class=" icon_table "' +
                        '   uib-tooltip="Xem chi tiết" translate>' +
                        '<i class="fa fa-list-alt" style="color:#e0d014"   aria-hidden="true"></i>' +
                        '</button>'
                        + '</div>'
                    }
                ]
            });
        }

        vm.doSearch = doSearch;
        function doSearch() {
            vm.showDetail = false;
            var grid = vm.issueGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        }

        vm.gridColumnShowHideFilter = function (item) {
            return item.type == null || item.type !== 1;
        };
        vm.showHideColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.issueGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.issueGrid.showColumn(column);
            } else {
                vm.issueGrid.hideColumn(column);
            }
        }
        vm.openDepartmentTo = openDepartmentTo

        function openDepartmentTo(popUp) {
            vm.departmentpopUp = popUp;
            var templateUrl = 'coms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
        }

        vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'dept') {
                vm.issueSearch.sysGroupName = data.text;
                vm.issueSearch.sysGroupId = data.id;
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
                vm.issueSearch.sysGroupName = dataItem.text;
                vm.issueSearch.sysGroupId = dataItem.id;
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
                            name: vm.issueSearch.sysGroupName,
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
                    vm.issueSearch.sysGroupName = null;// thành name
                    vm.issueSearch.sysGroupId = null;
                }
            },
            ignoreCase: false
        }
        vm.changeDataAuto = changeDataAuto
        function changeDataAuto(id) {
            switch (id) {
                case 'dept':
                {
                    if (processSearch(id, vm.selectedDept1)) {
                        vm.issueSearch.sysGroupId = null;
                        vm.issueSearch.sysGroupName = null;
                        vm.selectedDept1 = false;
                    }
                    break;
                }
                case 'handingUser':
                {
                    if (processSearch(id, vm.selectedHandingUser)) {
                        vm.issueItem.currentHandingUserNameNew = null;
                        vm.issueItem.currentHandingUserIdNew = null;
                        vm.selectedHandingUser = false;
                    }
                    break;
                }
            }
        }

        vm.cancelInput = function (param) {
            if (param == 'dept') {
                vm.issueSearch.sysGroupId = null;
                vm.issueSearch.sysGroupName = null;
            }
        }
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

        vm.cancelDateFrom = cancelDateFrom
        function cancelDateFrom(formFrom) {
            vm.issueSearch.createdFrom = undefined;
            $rootScope.validateDate(vm.issueSearch.createdFrom, null, null, formFrom)
        }

        vm.cancelDateTo = cancelDateTo
        function cancelDateTo(formTo) {
            vm.issueSearch.createdTo = undefined;
            $rootScope.validateDate(vm.issueSearch.createdTo, null, null, formTo)
        }

        vm.edit = edit;
        function edit(dataItem) {
            vm.pageSizeDiscuss = 5;
            vm.pageSizeHistory = 5;
            vm.issueDiscussList = []
            vm.issueHistoryList = []
            vm.tab1 = true;
            vm.tab2 = false;
            var teamplateUrl = "coms/issue/issuePopup.html";
            var title = "Thông tin phản ánh";
            var windowId = "POPUP_PHAN_ANH";
            vm.issueItem = angular.copy(dataItem);
            vm.issueItem.oldStatus = dataItem.status;
            CommonService.populatePopupCreate(teamplateUrl, title, dataItem, vm, windowId, true, '1200', '600', "content");
        }

        $scope.$on("Popup.open", function () {
            var obj = {
                issueId: vm.issueItem.issueId,
                page: 1,
                pageSize: vm.pageSizeDiscuss
            }
            issueService.getIssueDiscuss(obj).then(function (data) {
                vm.issueDiscussList = data;
            }, function (err) {
                vm.issueDiscussList = []
            });
            issueService.getIssueHistory(obj).then(function (issueHistoryList) {
                vm.issueHistoryList = issueHistoryList;
            }, function (err) {
                vm.issueHistoryList = []
            });
        });
        vm.loadMoreIssueDiscuss = loadMoreIssueDiscuss;
        function loadMoreIssueDiscuss() {
            var obj = {
                issueId: vm.issueItem.issueId,
                page: 1,
                pageSize: vm.pageSizeDiscuss += 5
            }
            issueService.getIssueDiscuss(obj).then(function (data) {
                vm.issueDiscussList = data;
            }, function (err) {
                vm.issueDiscussList = []
            });
        }
vm.loadMoreIssueHistory = loadMoreIssueHistory
        function loadMoreIssueHistory() {
            var obj = {
                issueId: vm.issueItem.issueId,
                page: 1,
                pageSize: vm.pageSizeHistory += 5
            }
            issueService.getIssueHistory(obj).then(function (data) {
                vm.issueHistoryList = data;
            }, function (err) {
                vm.issueHistoryList = []
            });
        }

        vm.gotoTabOnePopUp = function () {
            vm.tab2 = false;
            vm.tab1 = true;
        }
        vm.gotoTabTwoPopUp = function () {
            vm.tab2 = true;
            vm.tab1 = false;
        }
        vm.openPopup = function () {
            vm.keySearch = undefined;
            var teamplateUrl = "coms/issue/choosePopup.html";
            var title = "Danh sách nhân viên";
            vm.labelSearch = "Tên đăng nhập/Mã nhân viên";
            var windowId = "CONSTRUCTION";
            CommonService.populatePopupPartner(teamplateUrl, title, null, vm, windowId, true, '950', '350', null);
        }
        vm.performerSearch = {};
        vm.performerOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
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
                        url: Constant.BASE_SERVICE_URL + "sysUserCOMSRsService/doSearchUserInPopup",
                        contentType: "application/json; charset=utf-8",
                        type: "POST"
                    },
                    parameterMap: function (options, type) {
                        vm.performerSearch.page = options.page
                        vm.performerSearch.pageSize = options.pageSize
                        //vm.performerSearch.sysGroupId = vm.constrObj.sysGroupId
                        vm.performerSearch.name = vm.keySearch;
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
                pageSizes: [5, 10, 15, 20, 25],
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
                    '<div class="text-center">' +
                    '<a  type="button" class=" icon_table" uib-tooltip="Chọn" translate >' +
                    '<i id="#=code#"  ng-click="caller.choosePerformer(dataItem)"  class="fa fa-check color-green" aria-hidden="true"></i>' +
                    '</a>'
                    + '</div>',
                    width: '10%'
                },
                {
                    field: 'sysUserId',
                    hidden: true
                }
            ]
        });
        vm.doSearchPopup = doSearchPopup;
        function doSearchPopup(data) {
            var grid = vm.performerPopupGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        }

        vm.closePopupOnPopup = function () {
            CommonService.dismissPopup();
        }
        vm.choosePerformer = function (data) {
            vm.issueItem.currentHandingUserNameNew = data.fullName;
            vm.issueItem.currentHandingUserIdNew = data.sysUserId;
            vm.closePopupOnPopup();
        }
        vm.clearInput = function (data, form) {
            switch (data) {
                case '1':
                    vm.issueItem.currentHandingUserNameNew = null;
                    vm.issueItem.currentHandingUserIdNew = null;
                    break;
                default :
                    break;
            }
        }
        vm.selectedHandingUser = false;
        vm.handingUserOptions = {
            dataTextField: "fullName",
            dataValueField: "sysUserId",
            select: function (e) {
                vm.selectedHandingUser = true;
                var data = this.dataItem(e.item.index());
                vm.issueItem.currentHandingUserNameNew = data.fullName;
                vm.issueItem.currentHandingUserIdNew = data.sysUserId;
            },
            open: function (e) {
                vm.selectedHandingUser = false;
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedHandingUser = false;
                        return Restangular.all("sysUserCOMSRsService/getForAutoComplete").post({
                            name: vm.issueItem.currentHandingUserNameNew,
                            pageSize: vm.handingUserOptions.pageSize,
                            //sysGroupId:vm.constrObj.sysGroupId,
                            page: 1
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
                    vm.issueItem.currentHandingUserNameNew = null;
                    vm.issueItem.currentHandingUserIdNew = null;
                }
            },
            ignoreCase: false
        }
        vm.cancelPopupIssue = cancelPopupIssue;
        function cancelPopupIssue() {
            CommonService.dismissPopup1();
        }

        vm.savePopupIssue = savePopupIssue;
        function savePopupIssue() {
            issueService.save(vm.issueItem).then(function (result) {
                if (result.error) {
                    toastr.error(result.error);
                    return;
                }
                toastr.success("Ghi lại thành công");
                cancelPopupIssue();
                vm.doSearch();
            }).catch(function (err) {
                toastr.error("Có lỗi xảy ra khi ghi lại");
            });
        }
        // HuyPQ-start
        vm.exportFile = function exportFile() {
        	function displayLoading(target) {
      	      var element = $(target);
      	      kendo.ui.progress(element, true);
      	      setTimeout(function(){
      	    	  
      	    	var data = vm.issueGrid.dataSource.data();
                CommonService.exportFile(vm.issueGrid, data, vm.listRemove, vm.listConvert, "Danh sách tra cứu phản ánh");
                kendo.ui.progress(element, false);
      		});
      			
      	  }
      		displayLoading(".tab-content");
            
        }
        //HuyPQ-end
        vm.listRemove = [{
            title: "Thao tác"
        }
        ]
        vm.listConvert = [{
            field: "status",
            data: {
                1: 'Mở',
                0: 'Không'
            }
        }, {
            field: "state",
            data: {
                1: 'Chưa quá hạn',
                2: 'Quá hạn',
            }
        }
        ];

    	vm.openCatProvincePopup = openCatProvincePopup;
    	vm.onSaveCatProvince = onSaveCatProvince;
    	vm.clearProvince = clearProvince;
        function openCatProvincePopup(){
    		var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm tỉnh");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
        }
        function onSaveCatProvince(data){
            vm.issueSearch.catProvinceId = data.catProvinceId;
            vm.issueSearch.catProvinceCode = data.code;
    		vm.issueSearch.catProvinceName = data.name;
            htmlCommonService.dismissPopup();
            $("#provincename").focus();
        };
    	function clearProvince (){
    		vm.issueSearch.catProvinceId = null;
    		vm.issueSearch.catProvinceCode = null;
    		vm.issueSearch.catProvinceName = null;
    		$("#provincename").focus();
    	}
        vm.provinceOptions = {
            dataTextField: "name",
            dataValueField: "id",
    		placeholder:"Nhập mã hoặc tên tỉnh",
            select: function (e) {
                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.issueSearch.catProvinceId = dataItem.catProvinceId;
                vm.issueSearch.catProvinceCode = dataItem.code;
    			vm.issueSearch.catProvinceName = dataItem.name;
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
                            name: vm.issueSearch.catProvinceName,
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
                    vm.issueSearch.catProvinceId = null;
                    vm.issueSearch.catProvinceCode = null;
    				vm.issueSearch.catProvinceName = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.issueSearch.catProvinceId = null;
                    vm.issueSearch.catProvinceCode = null;
    				vm.issueSearch.catProvinceName = null;
                }
            }
        }





    }
})();