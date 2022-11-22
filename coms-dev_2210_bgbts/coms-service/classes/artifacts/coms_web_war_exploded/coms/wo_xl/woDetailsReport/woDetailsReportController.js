(function () {
    'use strict';
    var controllerId = 'woDetailsReportController';

    angular.module('MetronicApp').controller(controllerId, woDetailsReportController, '$scope','$modal','$rootScope');

    function woDetailsReportController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, woDetailsReportService, htmlCommonService, woCreateNewService, vpsPermissionService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modal) {

        var vm = this;
        vm.disableBtnExcel = false;
        vm.String = "Báo cáo > Báo cáo chi tiết sản lượng đã được ghi nhận";
        vm.dataResult  = [];
        vm.searchForm = {};
        vm.label = {};
        vm.workingWO = {};
        $scope.trTypeForm ={};
        $scope.cdLv2List = {};
        $scope.cdLv3List = {};
        $scope.ftList = {};
        $scope.permissions = {};
        $scope.advanceSearch = false;
        $scope.woDetailsReports = [];
        $scope.states = [
            {code:'PROCESSING', name:'Đang thực hiện'},
            {code:'DONE', name:'Đã thực hiện'},
            {code:'OK', name:'Hoàn thành'},
            {code:'NG', name:'Chưa hoàn thành'},
            {code:'OPINION_RQ_1', name:'Xin ý kiến CD Level 1'},
            {code:'OPINION_RQ_2', name:'Xin ý kiến CD Level 2'},
            {code:'OPINION_RQ_3', name:'Xin ý kiến CD Level 3'},
            {code:'OPINION_RQ_4', name:'Xin ý kiến CD Level 4'},
            {code:'CD_OK', name:'Điều phối duyệt OK'},
            {code:'CD_NG', name:'Điều phối duyệt chưa OK'},
        ];

        $scope.originTypes = [
//            {code:0, name: 'Trung tâm Hạ tầng'},
//            {code:1, name: 'Các đơn vị chi nhánh kỹ thuật'},
//            {code:2, name: 'Trung tâm Vận hành khai thác'},
        	{
                code: "280483",
                name: "Trung tâm Giải pháp tích hợp"
            }, {
                code: "166677",
                name: "Trung tâm Đầu tư hạ tầng"
            }, {
                code: "242656",
                name: "Trung tâm hạ tầng"
            }, {
                code: "270120",
                name: "Trung tâm vận hành khai thác"
            }, {
                code: "9006003",
                name: "Trung tâm Xây dựng dân dụng"
            }, {
                code: "280501",
                name: "Trung tâm Công nghệ thông tin"
            }
        ];

        init();
        function init(){
        	var date = new Date();
        	var start = new Date(date.getFullYear(), date.getMonth() - 1, 1);
        	var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
        	vm.searchForm.toDateRaw = htmlCommonService.formatDate(end);
        	vm.searchForm.fromDate = htmlCommonService.formatDate(start);
            $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
        	fillDataTable();
            getCdLv2List();
            getWoTypes();
            getAppWorkSrcs();
            getAppConstructionTypes();
            //addKpiLog();
        }

        function addKpiLog(){
      	   var obj = {
  					"appCode" :"COMS",
  					"funcCode":"QUAN_LY_SAN_LUONG_NGOAI_OS",
  					"serviceCode":"DO_SEARCH",
  					"userName" : Constant.userInfo.VpsUserInfo.employeeCode
  			};
  			return Restangular.all("service/SynStockTransRestService/service/saveKpiLogTimeProcess").post(obj).then(function(d){
         		if(d){
         		 $scope.idLog =  d.kpiLogTimeProcessDTO.id;
         		 fillDataTable([]);
         		}
         	}).catch(function(e){
         		toastr.error("Có lỗi khi thêm mới Log");
         	});

         }

         function updateKpiLog(){
      	   var obj = {
  					"kpiLogTimeProcessId" : $scope.idLog	,
  					"appCode" : "COMS"
  			};
      	   return Restangular.all("service/SynStockTransRestService/service/updateKpiLogTimeProcess").post(obj).then(function(d){
            		if(d){
            		}
            	}).catch(function(e){
            		toastr.error("Có lỗi khi update Log");
            	});

         }

        function getWoTypes() {
            woCreateNewService.getWOTypes().then(
                function (resp) {
                    //console.log(resp);
                    if(resp.data) $scope.woTypes = resp.data;
                },
                function (error) {
                    //console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function getAppWorkSrcs(){
            woCreateNewService.getAppWorkSrcs().then(
                function (resp) {
                    console.log(resp);
                    if(resp.data) $scope.apWorkSrcs = resp.data;

                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function getAppConstructionTypes(){
            woCreateNewService.getAppConstructionTypes().then(
                function (resp) {
                    console.log(resp);
                    if(resp.data) $scope.apConstructionTypes = resp.data;

                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                }
            )
        }

        function getCdLv2List(){
            woDetailsReportService.getCdLv2List({}).then(
                function (resp) {
                    console.log(resp);
                    if(resp.data) $scope.cdLv2List = resp.data;
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        function getCdLv4List(){

            if(!vm.searchForm.provinceId || vm.searchForm.provinceId == '') return;

            var postData = {provinceId: vm.searchForm.provinceId}
            woDetailsReportService.getCdLv4List(postData).then(
                function (resp) {
                    console.log(resp);
                    if(resp.data) $scope.cdLv4List = resp.data;
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        $scope.getFtList = function(level){
            if(level == 2){
                var postData = {cdLevel2: vm.searchForm.cdLevel2}
            }
            else if(level == 4){
                var postData = {cdLevel4: vm.searchForm.cdLevel4}
            }

            woDetailsReportService.getFtList(postData).then(
                function (resp) {
                    console.log(resp);
                    if(resp.data) $scope.ftList = resp.data;
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        };

        vm.genReport = function(){
        	if($scope.idLog){
        		addKpiLog();
        	}
//            vm.detailsReportTable.dataSource.query({
//                page: 1,
//                pageSize: 10
//            });
            vm.showDetail = false;
            var grid = vm.detailsReportTable;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        };

        vm.setToDate = function(){
            if(!vm.searchForm.toDateRaw || vm.searchForm.toDateRaw =='') vm.searchForm.toDate = null;
            else{
                var splitted = vm.searchForm.toDateRaw.split('/');
                var day = splitted[0];
                var month = splitted[1];
                var year = splitted[2];
                var addedOneDay = parseInt(day) + 1;
                vm.searchForm.toDate = addedOneDay + '/' + month + '/' + year;
            }
        }

        var record = 0;
        function fillDataTable(){
        	kendo.ui.progress($("#detailsReportTable"), true);
            vm.detailsReportTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable:false,
                save : function(){
                    vm.detailsReportTable.refresh();
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            // // $("#appCount").text("" + response.total);
                            vm.count = response.total;
                            return response.total;
                        },
                        data: function (response) {
                        	if($scope.idLog){
                        		updateKpiLog();
                        	}
                        	$scope.woDetailsReports = response.data;
                        	kendo.ui.progress($("#detailsReportTable"), false);
                          return response.data;
                        }
                    },
                    transport: {
                        read: {
                            //Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/report/woDetailsReport",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page
                            vm.searchForm.pageSize = options.pageSize
                            return JSON.stringify(vm.searchForm)
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
                    refresh: true,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: ''
                        // empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '40px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                    },
                    {
                        title: "Xem",
                        field: "stt",
                        template: function (dataItem) {
                            return '<i class="fa fa-list-alt icon-table" ng-click="vm.viewWODetails('+dataItem.woId+')"></i>';
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
                        field: 'cdLevel2Name',
                        width: '120px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template : function(dataItem){
                            var text = valueOrEmptyStr(dataItem.cdLevel2Name);
                            return text.split('-')[0].trim();
                        },
                    },
//                    {
//                        title: "Đơn vị thực hiện",
//                        field: 'cdLevel4Name',
//                        width: '80px',
//                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
//                        attributes: {
//                            style: "text-align:left;"
//                        },
//                        type :'text',
//                        template: function (dataItem) {
//                            return valueOrEmptyStr(dataItem.cdLevel4Name);
//                        }
//                    },
                    {
                        title: "Trụ",
                        field: 'cdLevel1Name',
                        width: '180px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.cdLevel1Name);
                        }
                    },
                    {
                        title: "Nhân viên thực hiện",
                        field: 'ftName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.ftName);
                        }
                    },
                    {
                        title: "Mã TR",
                        field: 'ftName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.trCode);
                        }
                    },
                    {
                        title: "Tên WO",
                        field: 'ftName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.woName);
                        }
                    },
                    {
                        title: "Mã WO",
                        field: 'ftName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.woCode);
                        }
                    },
                    {
                        title: "Trạng thái",
                        field: 'ftName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            var txt = getStateText(dataItem.state);
                            return valueOrEmptyStr(txt);
                        }
                    },
                    {
                        title: "Hợp đồng",
                        field: 'contractCode',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.contractCode);
                        }
                    },
                    {
                        title: "Mã nhà trạm",
                        field: 'catStationHouseTxt',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function(dataItem){
                            if(dataItem.woTypeId!=null && dataItem.woTypeId==1){
                            	return dataItem.catStationHouseTxt!=null ? dataItem.catStationHouseTxt : "";
                            } else {
                            	return "";
                            }
                        },
                    },
                    {
                        title: "Mã trạm",
                        field: 'ftName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.stationCode);
                        }
                    },
                    {
                        title: "Mã công trình",
                        field: 'ftName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.constructionCode);
                        }
                    },
                    {
                        title: "Hạng mục",
                        field: 'catWorkItemTypeName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.catWorkItemTypeName);
                        }
                    },
                    {
                        title: "Các đầu việc",
                        field: 'checklistItemNames',
                        width: '300px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text'
                    },
                    {
                        title: "Loại công trình",
                        field: 'ftName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.apConstructionTypeName);
                        }
                    },
                    {
                        title: "Nguồn việc",
                        field: 'ftName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.apWorkSrcName);
                        }
                    },
                    {
                        title: "Ngày hoàn thành theo kế hoạch",
                        field: 'finishDateStr',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
//                        template: function (dataItem) {
//                            return valueOrEmptyStr(dataItem.startTime);
//                        }
                    },
                    {
                        title: "Ngày FT hoàn thành thực tế",
                        field: 'endTimeStr',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
//                        template: function (dataItem) {
//                            return valueOrEmptyStr(dataItem.endTime);
//                        }
                    },
                    {
                        title: "Ngày phê duyệt WO",
                        field: 'userTthtApproveWoStr',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
//                        template: function (dataItem) {
//                            return valueOrEmptyStr(dataItem.updateTthtApproveWo);
//                        }
                    },
                    {
                        title: "Người ghi nhận P.QT-TTHT",
                        field: 'userTthtApproveWo',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function(dataItem){
                        	if(dataItem.woTypeId!=null && dataItem.woTypeId==3){
                        		return dataItem.userTthtApproveWo!=null ? dataItem.userTthtApproveWo : "";
                        	} else {
                        		return "";
                        	}
                        }
//                        hidden: function(dataItem) {
//                        	if(vm.searchForm.listWoTypeId.indexOf("3")<0){
//                        		return true;
//                        	} else {
//                        		return false;
//                        	}
//                        }
                    },
                    {
                        title: "Tháng ghi nhận WO",
                        field: 'approveDateReportWoStr',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
//                        template: function (dataItem) {
//                            return valueOrEmptyStr(dataItem.approveDateReportWo);
//                        }
                    },
                    {
                        title: "Định mức hoàn thành (Giờ)",
                        field: 'ftName',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.qoutaTime);
                        }
                    },
                    {
                        title: "Ghi nhận theo",
                        field: 'calculateMethod',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            return valueOrEmptyStr(dataItem.calculateMethod);
                        }
                    },
                    {
                        title: "Giá trị sản lượng đầu vào WO (triệu VND)",
                        field: 'moneyValue',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            if(!dataItem.moneyValue || dataItem.moneyValue == 0) return 0;
                            var moneyM = dataItem.moneyValue/1000000;
                            moneyM = moneyM.toFixed(3);
                            moneyM = CommonService.removeTrailingZero(moneyM);
                            return CommonService.numberWithCommas(moneyM)? CommonService.numberWithCommas(moneyM) : 0;
                        }
                    },
                    {
                        title: "Sản lượng đã ghi nhận (triệu VND)",
                        field: 'acceptedMoneyValue',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            if(!dataItem.acceptedMoneyValue || dataItem.acceptedMoneyValue == 0) return 0;
                            var moneyM = dataItem.acceptedMoneyValue/1000000;
                            moneyM = moneyM.toFixed(3);
                            moneyM = CommonService.removeTrailingZero(moneyM);
                            return valueOrEmptyStr(moneyM);
                        }
                    },
                    {
                        title: "Ghi nhận ngày (triệu VND)",
                        field: 'acceptedMoneyValueDaily',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            if(!dataItem.acceptedMoneyValueDaily || dataItem.acceptedMoneyValueDaily == 0) return 0;
                            var moneyM = dataItem.acceptedMoneyValueDaily/1000000;
                            moneyM = moneyM.toFixed(3);
                            moneyM = CommonService.removeTrailingZero(moneyM);
                            return valueOrEmptyStr(moneyM);
                        }
                    },
                    {
                        title: "Ghi nhận tháng (triệu VND)",
                        field: 'acceptedMoneyValueMonthly',
                        width: '200px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function (dataItem) {
                            if(!dataItem.acceptedMoneyValueMonthly || dataItem.acceptedMoneyValueMonthly == 0) return 0;
                            var moneyM = dataItem.acceptedMoneyValueMonthly/1000000;
                            moneyM = moneyM.toFixed(3);
                            moneyM = CommonService.removeTrailingZero(moneyM);
                            return valueOrEmptyStr(moneyM);
                        }
                    }
            ]
            });
        }

        function getStateText(state){
            var text = '';
            Object.keys(Constant.WO_XL_STATE).forEach(
                function(key,index){
                    if(Constant.WO_XL_STATE[key].stateCode == state){
                        text = Constant.WO_XL_STATE[key].stateText;
                    }
                }
            );
            return text;
        }

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };


        vm.exportpdf = function () {
            var obj = {};
            woDetailsReportService.exportpdf(obj);
        }

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
            if (vm.departmentpopUp === 'dept') {
                vm.searchForm.cdLevel4 = data.id;
            }
        }


        function valueOrEmptyStr(value) {
            return value? value:'';
        }

        vm.changeDataAuto = changeDataAuto
        function changeDataAuto(id) {
            switch (id) {
                case 'dept':
                {
                    if (processSearch(id, vm.selectedDept1)) {
                        vm.searchForm.cdLevel4 = null;
                        vm.selectedDept1 = false;
                    }
                    break;
                }
            }
        }

        vm.cancelInput = function (param) {
            if (param == 'dept') {
                vm.searchForm.cdLevel4 = null;
                vm.label.depName = null;
            }
        }
        vm.cancelListYear= cancelListYear;
        function cancelListYear(){
            vm.searchForm.toDate = null;
            vm.searchForm.fromDate = null;
            vm.searchForm.toDateRaw = null;
        }

        vm.selectedDept1 = false;
        vm.deprtOptions1 = {
            dataTextField: "text",
            dataValueField: "id",
            placeholder:"Nhập mã hoặc tên đơn vị",
            select: function (e) {
                vm.selectedDept1 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.searchForm.cdLevel4 = dataItem.id;
                vm.label.depName = data.sysGroupName;
                $scope.getFtList(4);
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
                            name: vm.label.depName,
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
                    vm.searchForm.cdLevel4 = null;
                }
            },
            ignoreCase: false
        };

        vm.getExcelTemplate = function () {
            workItemService.downloadTemplate({}).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                return;
            });
        };

        vm.cancelImport = cancelImport;
        function cancelImport() {
            CommonService.dismissPopup1();
        }
        vm.downloadFile = downloadFile;
        function downloadFile(path){
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + path;
        }
        vm.removeImage=removeImage
        function removeImage(image,list){
            list.splice(list.indexOf(image), 1);
        }

        function changeImage(image) {
            vm.imageSelected = image;
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
            vm.searchForm.provinceId = data.catProvinceId;
            vm.searchForm.provinceCode = data.code;
            vm.searchForm.provinceName = data.name;
            htmlCommonService.dismissPopup();
            $("#provincename").focus();
        };

        function clearProvince (){
            vm.searchForm.provinceId = null;
            vm.searchForm.provinceCode = null;
            vm.searchForm.provinceName = null;
            $("#provincename").focus();
        }

        vm.provinceOptions = {
            dataTextField: "name",
            dataValueField: "id",
            placeholder:"Nhập mã hoặc tên tỉnh",
            select: function (e) {
                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.searchForm.provinceId = dataItem.catProvinceId;
                vm.searchForm.provinceCode = dataItem.code;
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
                            name: vm.searchForm.provinceName,
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
                    vm.searchForm.provinceId = null;
                    vm.searchForm.provinceCode = null;
                    vm.searchForm.provinceName = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.searchForm.provinceId = null;
                    vm.searchForm.provinceCode = null;
                    vm.searchForm.provinceName = null;
                }
            }
        };

        vm.viewWODetails = viewWODetails;
        function viewWODetails(woId){
            var reportRecord = null;

            for(var i = 0; i< $scope.woDetailsReports.length; i++){
                if($scope.woDetailsReports[i].woId == woId){
                    reportRecord = $scope.woDetailsReports[i];
                    break;
                }
            }

            if(!reportRecord) return;
            vm.workingWO = reportRecord;

            $modal.open({
                templateUrl: 'coms/wo_xl/woDetailsReport/checklistModal.html',
                controller: null,
                windowClass: '',
                scope: $scope
            })
                .result.then(
                function() {
                },
                function() {
                }
            )

            if(vm.workingWO.woTypeCode == 'HCQT') fillHCQTChecklist();
            else fillNormalChecklist();
        }

        var checkListRecord = 0;
        function fillNormalChecklist(){
            $scope.remainQuantityByDate = false;
            vm.woCheckListOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable:false,
                save : function(){
                    vm.woCheckList.dataSource.read();
                },
                dataBinding: function () {
                    checkListRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            // $("#appCount").text("" + response.total);
                            // vm.count = response.total;
                            // return response.total;
                        },
                        data: function (response) {
                            $scope.checklists = response.data;
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/wo/getCheckList",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var obj ={
                                woId: vm.workingWO.woId,
                                constructionCode: vm.workingWO.constructionCode,
                                woTypeCode: vm.workingWO.woTypeCode
                            };
                            return JSON.stringify(obj)

                        }
                    },
                    pageSize: 10,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: false,
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++checkListRecord;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',

                    },
                    {
                        title: "Tên công việc",
                        field: 'constructionCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: { style: "text-align:left;" },
                        type :'text',
                        template : function(dataItem){
                            if(dataItem.checklistName) return dataItem.checklistName;
                            else if (dataItem.name) return dataItem.name;
                            else return vm.workingWO.woName;
                        },

                    },
                    {
                        title: "Trạng thái",
                        field: 'valueConstruction',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return dataItem.state;;
                        },

                    },
                    {
                        title: "Sản lượng theo ngày/tháng",
                        field: 'quantityByDate',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'text',
                        template : function(dataItem){
                            if(dataItem.quantityByDate == 1 || dataItem.quantityByDate == 2) return 'Có';
                            return 'Không';
                        },

                    },
                    {
                        title: "Sản lượng đã ghi nhận",
                        field: 'quantityLength',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'text',
                        template : function(dataItem){
                            var returnValue = '';
                            if(dataItem.confirm == 1) returnValue = dataItem.value;
                            else returnValue = dataItem.quantityLength?dataItem.quantityLength:'';
                            if(returnValue!= ''){
                                returnValue = CommonService.numberWithCommas(returnValue);
                            }
                            return returnValue;
                        },

                    },
                    {
                        title: "Sản lượng thêm mới",
                        field: 'unapprovedQuantity',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'text',
                        template : function(dataItem){
                            var returnValue = dataItem.unapprovedQuantity?dataItem.unapprovedQuantity:'';
                            if(returnValue != '') returnValue = CommonService.numberWithCommas(returnValue);
                            return returnValue;
                        },
                        hidden: vm.workingWO.woTypeCode == 'THICONG' && vm.workingWO.apWorkSrc == 6
                    },
                    {
                        title: "Giá trị sản lượng",
                        field: 'addedQuantityLength',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'text',
                        template : function(dataItem){
                            var returnValue = dataItem.value?dataItem.value:'';
                            if(returnValue != '') returnValue = CommonService.numberWithCommas(returnValue);
                            return returnValue;
                        },
                        hidden: vm.workingWO.woTypeCode != 'THICONG' || vm.workingWO.apWorkSrc != 6
                    },
                    {
                        title: "Ảnh thực hiện",
                        field: 'sysGroupName',
                        width: '25%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function(dataItem){
                            var template = ''
                            if(dataItem.lstImgs){
                                for(var i = 0; i<dataItem.lstImgs.length; i++){
                                    template += '<div><img style="width: 150px;" src="data:image/png;base64,'+dataItem.lstImgs[i].imgBase64+'"></div>';
                                }
                            }
                            return template;
                        }
                    },
                ]
            });
        }

        var hcqtCheckListRecord = 0;
        function fillHCQTChecklist(){
            vm.woHCQTCheckListOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable:false,
                save : function(){
                    vm.woHCQTCheckList.dataSource.read();
                },
                dataBinding: function () {
                    hcqtCheckListRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            // $("#appCount").text("" + response.total);
                            // vm.count = response.total;
                            // return response.total;
                        },
                        data: function (response) {
                            if(response.data[0]) $scope.dnqtValue = response.data[0].dnqtValue;
                            if(response.data[2]) $scope.vtnetConfirmValue = response.data[2].vtnetConfirmValue;
                            if(response.data[3]) $scope.aprovedDocValue = response.data[3].aprovedDocValue;
                            $scope.hcqtChecklist = response.data;
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "woService/hcqtChecklist/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var obj ={
                                woId: vm.workingWO.woId
                            };
                            return JSON.stringify(obj)

                        }
                    },
                    pageSize: 10,
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: false,
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++hcqtCheckListRecord;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',

                    },
                    {
                        title: "Tên công việc",
                        field: 'constructionCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: { style: "text-align:left;" },
                        type :'text',
                        template : function(dataItem){
                            if(dataItem.checklistName)
                                return dataItem.checklistName;
                            else return vm.workingWO.woName;
                        },

                    },
                    {
                        title: "Trạng thái",
                        field: 'state',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return dataItem.state;
                        },

                    },
                    {
                        title: "Giá trị",
                        field: 'state',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return getHcqtChecklistValue(dataItem);
                        },

                    },
                    {
                        title: "Ngày thực hiện",
                        field: 'state',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type :'text',
                        template : function(dataItem){
                            return getHcqtChecklistDate(dataItem);
                        },

                    },
                    {
                        title: "Ảnh thực hiện",
                        field: 'sysGroupName',
                        width: '25%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                        template: function(dataItem){
                            var template = '';
                            for(var i = 0; i<dataItem.lstImgs.length; i++){
                                template += '<div><img style="width: 150px;" src="data:image/png;base64,'+dataItem.lstImgs[i].imgBase64+'"></div>';
                            }
                            return template;
                        }
                    },
                ]
            });

        }

        function getHcqtChecklistValue(dataItem){
            if (dataItem.checklistOrder == 1) return dataItem.dnqtValue?dataItem.dnqtValue:'';
            if (dataItem.checklistOrder == 2) return dataItem.vtnetSentValue?dataItem.vtnetSentValue:'';
            if (dataItem.checklistOrder == 3) return dataItem.vtnetConfirmValue?dataItem.vtnetConfirmValue:'';
            if (dataItem.checklistOrder == 4) return dataItem.aprovedDocValue?dataItem.aprovedDocValue:'';
            if (dataItem.checklistOrder == 5) return dataItem.finalValue?dataItem.finalValue:'';
            return '';
        }

        function getHcqtChecklistDate(dataItem){
            if (dataItem.checklistOrder == 1) return dataItem.dnqtDate?dataItem.dnqtDate:'';
            if (dataItem.checklistOrder == 2) return dataItem.vtnetSendDate?dataItem.vtnetSendDate:'';
            if (dataItem.checklistOrder == 3) return dataItem.vtnetConfirmDate?dataItem.vtnetConfirmDate:'';
            if (dataItem.checklistOrder == 4) return dataItem.aprovedDocDate?dataItem.aprovedDocDate:'';
            if (dataItem.checklistOrder == 5) return dataItem.finalDate?dataItem.finalDate:'';
            return '';
        }

//        vm.toExcelReport = function(){
//           var obj = vm.searchForm;
//           obj.page = 1;
//           obj.pageSize = 999999;
//           $http({
//               url: Constant.BASE_SERVICE_URL + "woService/report/getExcelDetailsReport",
//               method: "POST",
//               data: obj,
//               headers: {
//                   'Content-type': 'application/json'
//               },
//               responseType: 'arraybuffer'
//           })
//               .success(function (data, status, headers, config) {
//                   htmlCommonService.saveFile(data,"DetailsReport.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
//               })
//               .error(function (data, status, headers, config) {
//                   toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
//               });
//        }

//       //HuyPQ-25082020-start
         vm.toExcelReport= function(){
         	var obj = vm.searchForm;
         	obj.page = 1;
         	obj.pageSize = 999999;
         	kendo.ui.progress($("#detailsReportTable"), true);
             return Restangular.all("woService/report/exportReportDetailWo").post(obj).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                kendo.ui.progress($("#detailsReportTable"), false);
             }).catch(function (e) {
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                kendo.ui.progress($("#detailsReportTable"), false);
                return;
             });
         }
//         //HuyPQ-edit-end
// end controller
    }
})();