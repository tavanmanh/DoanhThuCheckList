(function() {
	'use strict';
	var controllerId = 'totalMonthPlanController';
	
	angular.module('MetronicApp').controller(controllerId, totalMonthPlanController);
	
	function totalMonthPlanController($scope, $rootScope, $timeout, gettextCatalog, 
			kendoConfig, $kWindow,totalMonthPlanService,$filter,
			CommonService, PopupConst, Restangular, RestEndpoint,Constant, htmlCommonService) {
        var vm = this;
        vm.showSearch = true;
        vm.isCreateNew = false;
        vm.showDetail = false;
        vm.tab1 = true;
        vm.tab2 = false;
        vm.tab3 = false;
        vm.currentYear = (new Date()).getFullYear();
        vm.nextStep2 = function () {
            if (validateTotalMonthDetail()) {
                vm.tab1 = false;
                vm.tab2 = true;
                vm.tab3 = false;
                $("#tab1TotalMonth").removeClass("acceptQLK postion-icon");
                $("#tab1TotalMonth").addClass("declineQLK postion-icon");
                $("#DivTab1TotalMonth").removeClass("active");
                $("#DivTab3TotalMonth").removeClass("active");
                $("#DivTab2TotalMonth").addClass("active");
                $("#tab2TotalMonth").removeClass("declineQLK postion-icon");
                $("#tab2TotalMonth").addClass("acceptQLK postion-icon");
                $("#tab3TotalMonth").removeClass("acceptQLK postion-icon");
                $("#tab3TotalMonth").addClass("declineQLK postion-icon");
            }
        }

        vm.prevStep = function () {
            vm.tab1 = true;
            vm.tab2 = false;
            vm.tab3 = false;
            $("#tab2TotalMonth").removeClass("acceptQLK postion-icon");
            $("#tab2TotalMonth").addClass("declineQLK postion-icon");
            $("#DivTab2TotalMonth").removeClass("active");
            $("#DivTab1TotalMonth").addClass("active");
            $("#DivTab3TotalMonth").removeClass("active");
            $("#tab1TotalMonth").removeClass("declineQLK postion-icon");
            $("#tab1TotalMonth").addClass("acceptQLK postion-icon");
            $("#tab3TotalMonth").removeClass("acceptQLK postion-icon");
            $("#tab3TotalMonth").addClass("declineQLK postion-icon");
        }
        vm.nextStep3 = function () {
            vm.tab1 = false;
            vm.tab2 = false;
            vm.tab3 = true;
            $("#tab2TotalMonth").removeClass("acceptQLK postion-icon");
            $("#tab2TotalMonth").addClass("declineQLK postion-icon");
            $("#DivTab2TotalMonth").removeClass("active");
            $("#DivTab1TotalMonth").removeClass("active");
            $("#DivTab3TotalMonth").addClass("active");
            $("#tab1TotalMonth").removeClass("acceptQLK postion-icon");
            $("#tab1TotalMonth").addClass("declineQLK postion-icon");
            $("#tab3TotalMonth").removeClass("declineQLK postion-icon");
            $("#tab3TotalMonth").addClass("acceptQLK postion-icon");
        }
        vm.totalMonthPlanSearch = {
            status: 1
        };
        vm.String = "Qu???n l?? c??ng tr??nh > Qu???n l?? k??? ho???ch > K??? ho???ch th??ng t???ng th???";
        vm.totalMonthPlan = {

        };
        vm.cancelListMonth = function () {
            vm.totalMonthPlanSearch.monthList = [];
        }
        vm.cancelListStatus = function () {
            vm.totalMonthPlanSearch.signStateList = [];
        }
        vm.cancelListYear = function () {
            vm.totalMonthPlanSearch.yearList = [];
        }
        vm.dataRropFile = [];
        function loadFileData(){
            totalMonthPlanService.getFileAppendixParam().then(function(result){
                vm.dataRropFile  = result.plain();
            }, function(errResponse){
            });
        }
        loadFileData();
        // Khoi tao du lieu cho form
        initTab2FunctionTotal($scope, $rootScope, $timeout, gettextCatalog,
            kendoConfig, $kWindow, totalMonthPlanService,
            CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm, htmlCommonService);
        function initTab2Table() {
            vm.fillTargetTable();
            vm.fillSourceTable();
            vm.fillForceMaintainTable();
            vm.fillBTSTable();
            //vm.fillMaterialTable();
            vm.fillForceNewTable();
            vm.fillFinanceTable();
            vm.fillContractTable();
            vm.fillMaterialTable();
            //vm.fillYearPlanDetailTable();
            vm.fillAppendixTable();
        }

        initFormData();
        function initFormData() {
            fillDataTable([]);
            initDropDownList();
            //initTab2Table();
        }

        function initDropDownList() {
            vm.yearDataList = [];
            vm.monthDataList = [];
            var currentYear = (new Date()).getFullYear();
            for (var i = currentYear - 2; i < currentYear + 19; i++) {
                vm.yearDataList.push({
                    id: i,
                    name: i

                })
            }
            /*for (var i = 1; i < 13; i++) {
                vm.monthDataList.push({
                    id: i,
                    name: i

                })
            }*/
            vm.monthDataList = [
                {id:1,name:'01'},
                {id:2,name:'02'},
                {id:3,name:'03'},
                {id:4,name:'04'},
                {id:5,name:'05'},
                {id:6,name:'06'},
                {id:7,name:'07'},
                {id:8,name:'08'},
                {id:9,name:'09'},
                {id:10,name:'10'},
                {id:11,name:'11'},
                {id:12,name:'12'}

            ];
            vm.yearDownListOptions = {
                dataSource: vm.yearDataList,
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true
            }
            vm.monthDownListOptions = {
                dataSource: vm.monthDataList,
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true
            }
        }

        vm.validatorOptions = kendoConfig.get('validatorOptions');
        vm.formatAction = function (dataItem) {
            var template =
                '<div class="text-center #=totalMonthPlanId#"">'
            template += '<button type="button"' +
            'class="btn btn-default padding-button box-shadow  #=totalMonthPlanId#"' +
            'disble="" ng-click=vm.edit(#=totalMonthPlanId#)>' +
            '<div class="action-button edit" uib-tooltip="S???a" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
            '</button>' +
            '<button type="button"' +
            'class="btn btn-default padding-button box-shadow #=totalMonthPlanId#"' +
            'ng-click=vm.send(#=totalMonthPlanId#)>' +
            '<div class="action-button export" uib-tooltip="G???i t??i ch??nh" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
            '</button>' +
            '<button type="button"' +
            'class="btn btn-default padding-button box-shadow #=totalMonthPlanId#"' +
            'ng-click=vm.remove(#=totalMonthPlanId#)>' +
            '<div class="action-button del" uib-tooltip="X??a" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
            '</button>'
            +
            '<button type="button" class="btn btn-default padding-button box-shadow #=totalMonthPlanId#"' +
            'ng-click=vm.cancelUpgradeLta(#=totalMonthPlanId#)>' +
            '<div class="action-button cancelUpgrade" uib-tooltip="H???y n??ng c???p" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
            '</button>';
            template += '</div>';
            return dataItem.groupId;
        }
        setTimeout(function () {
            $("#keySearch").focus();
        }, 15);
        /*
         * setTimeout(function(){ $("#appIds1").focus(); },15);
         */
        var record = 0;

        function fillDataTable(data) {
            vm.gridOptions = kendoConfig.getGridOptions({
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
                        template: '<div class=" pull-left ">' +
                        '<button class="btn btn-qlk padding-search-right addQLK"' +
                        'ng-click="vm.add()" uib-tooltip="T???o m???i" translate>T???o m???i</button>' +
                        '</div>'
                        +
                        '<div class="btn-group pull-right margin_top_button margin10">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">C??i ?????t</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xu???t Excel</span></i>' +
                        '<div uib-tooltip="???n/hi???n c???t" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.totalMonthPlanGrid.columns.slice(1,vm.totalMonthPlanGrid.columns.length)| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#appCount").text("" + response.total);
                            vm.count = response.total;
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
                            url: Constant.BASE_SERVICE_URL + "totalMonthPlanRsService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            // vm.totalMonthPlanSearch.employeeId =
                            // Constant.user.srvUser.catEmployeeId;
                            vm.totalMonthPlanSearch.page = options.page
                            vm.totalMonthPlanSearch.pageSize = options.pageSize

                            return JSON.stringify(vm.totalMonthPlanSearch)

                        }
                    },
                    pageSize: 10
                },
                // dataSource: data,
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Kh??ng c?? k???t qu??? hi???n th???</div>")
                },
                pageable: {
                    refresh: false,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} c???a {2} k???t qu???",
                        itemsPerPage: "k???t qu???/trang",
                        empty: "<div style='margin:5px'>Kh??ng c?? k???t qu??? hi???n th???</div>"
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
                        title: "M?? k??? ho???ch",
                        field: 'code',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    }, {
                        title: "T??n k??? ho???ch",
                        field: 'name',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    }, {
                        title: "Th??ng th???c hi???n ",
                        width: '10%',
                        field: 'month',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function (dataItem) {
                            if (dataItem.month != null && dataItem.year != null) {
                                return dataItem.month + "/" + dataItem.year
                            } else return ''
                        }
                    }, {
                        title: "T??nh tr???ng k?? CA",
                        field: 'signState',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: "# if(signState == 1){ #" + "#= 'Ch??a tr??nh k??' #" + "# } " + "else if (signState == 2) { # " + "#= '??ang tr??nh k??' #" + "#}" + "else if (signState == 3) { # " + "#= '???? k?? duy???t' #" + "#} " + "else if (signState == 4) { # " + "#= 'T??? ch???i k?? duy???t' #" + "#} #"
                    },

                    {
                        title: "Tr???ng th??i",
                        field: 'status',
                        template: "# if(status == 0){ #" + "#= 'H???t hi???u l???c' #" + "# } " + "else if (status == 1) { # " + "#= 'Hi???u l???c' #" + "#} #",
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    }, {
                        title: "Thao t??c",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        template: dataItem =>
                        '<div class="text-center">'
                        + '<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.edit(dataItem)" class=" icon_table "' +
                        '   uib-tooltip="Xem chi ti???t" translate>' +
                        '<i class="fa fa-list-alt" style="color:#e0d014" ng-disabled="dataItem.status==0||dataItem.signState!=1"   aria-hidden="true"></i>' +
                        '</button>'
                        + '<button style=" border: none; background-color: white;" id="updateId" ng-hide="dataItem.status==0" ng-click="vm.copy(dataItem)" class=" icon_table "' +
                        '   uib-tooltip="Sao ch??p" translate>' +
                        '<i class="fa fa-files-o"style="color: #337ab7;"  ng-hide="dataItem.status==0"   aria-hidden="true"></i>' +
                        '</button>'
                        // hungtd_20181213_start
                        + '<button style=" border: none; background-color: white;" id="sign" ng-hide="dataItem.status==0||dataItem.signState==3" ng-click="vm.updateRegistry(dataItem)" class=" icon_table"' +
                        '   uib-tooltip="Tr??nh k??" translate>' +
                        '<i class="fa fa-arrow-up" style="color: #337ab7;"  ng-hide="dataItem.status==0||dataItem.signState==3"   aria-hidden="true"></i>'
                        + '</button>'
                        // hungtd_20181213_end
                        + '<button style=" border: none; background-color: white;" id="sign" ng-show="dataItem.signState==3" ng-click="vm.viewSignedDoc(dataItem)" class=" icon_table"' +
                        '   uib-tooltip="Xem v??n b???n ???? k??" translate>' +
                        '<i class="fa fa-file-text-o"style="color: #337ab7; ng-show="dataItem.signState==3"   aria-hidden="true"></i>'
                        + '</button>'+
                        '<button style=" border: none; background-color: white;" id="removeId"' +
                        'class="#=appParamId# icon_table" ng-click="vm.remove(dataItem)" ng-hide="dataItem.status==0"  uib-tooltip="X??a" translate' +
                        '>' +
                        '<i class="fa fa-trash" style="color: #337ab7;" ng-hide="dataItem.status==0"  aria-hidden="true"></i>' +
                        '</button>'
                        + '</div>',
                        width: '20%'
                    }
                ]
            });
        }

        function pushDataTototalMonthPlanTable(data) {
            var grid = vm.totalMonthPlanDetailGrid;
            if (grid) {
                grid.dataSource.data(data);
                grid.refresh();
            }
        }

        function filltotalMonthPlanDetailTable() {
            vm.totalMonthPlanDetailGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                dataSource: {
                    pageSize: 10,
                    group: {
                        field: "sysGroupId",
                        dir: "asc"
                    }
                },
                selectable: "multiple row",
                pageable: {
                    refresh: false,
                    pageSize: 10,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} c???a {2} k???t qu???",
                        itemsPerPage: "k???t qu???/trang",
                        empty: "Kh??ng c?? k???t qu??? hi???n th???"
                    }
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Kh??ng c?? k???t qu??? hi???n th???</div>")
                },
                scrollable: false,
                groupable: true,
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
                    },
                    {
                        title: "????n v???",
                        field: 'sysGroupName',
                        width: '25%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    },
                    {
                        title: "Ngu???n vi???c",
                        field: 'source',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                    },
                    {
                        title: "S???n l?????ng",
                        field: 'quantity',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                    },
                    {
                        title: "HSHC",
                        field: 'complete',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                    },
                    {
                        title: "Doanh thu",
                        field: 'revenue',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        }, ex
                    },
                    {
                        title: "Thao t??c",
                        template: dataItem =>
                        '<div class="text-center">'
                        +
                        '<button style=" border: none; background-color: white;" id="removeId"' +
                        'class="#=totalMonthPlanId# icon_table" ng-click="vm.removetotalMonthPlanDetail(dataItem)" uib-tooltip="X??a" translate' +
                        '>' +
                        '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
                        '</button>'
                        + '</div>',
                        width: '10%',
                        field: "action"
                    }
                ]
            });
        }

        vm.listRemove = [{
            title: "Thao t??c",
        },
            {
                title: "<input type='checkbox' id='checkalllistImpReq' name='gridchkselectall' ng-click='vm.chkSelectAll();' ng-model='vm.chkAll' />",
            }]

        vm.listConvert = [{
            field: "status",
            data: {
                1: 'Hi???u l???c',
                0: 'H???t Hi???u l???c'
            }
        }, {
            field: "signState",
            data: {
                1: 'Ch??a tr??nh k??',
                2: '??ang tr??nh k??',
                3: '???? k?? duy???t',
                4: 'T??? ch???i k?? duy???t'

            }
        }
        ]

	
		//vm.exportFile = function exportFile() {
		//	var data = vm.totalMonthPlanGrid.dataSource.data();
		//		CommonService.exportFile(vm.totalMonthPlanGrid,data,vm.listRemove,vm.listConvert);
		//}

        vm.exportFile= function(){
        	function displayLoading(target) {
        	      var element = $(target);
        	      kendo.ui.progress(element, true);
        	      setTimeout(function(){
        	    	  
        	    	  return Restangular.all("totalMonthPlanRsService/exportTotalMonthPlan").post(vm.totalMonthPlanSearch).then(function (d) {
        	                var data = d.plain();
        	                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	                kendo.ui.progress(element, false);
        	            }).catch(function (e) {
        	            	kendo.ui.progress(element, false);
        	                toastr.error(gettextCatalog.getString("L???i khi t???i bi???u m???u!"));
        	                return;
        	            });
        		});
        			
        	  }
        		displayLoading(".tab-content");
            

        }
vm.listRemove1 = [{
    title: "Thao t??c"
}]
vm.listConvert1 = [{
    field: "status"

}]


        vm.exportFileChiTieu= function(){
			function displayLoading(target) {
				var element = $(target);
				kendo.ui.progress(element, true);
				setTimeout(function(){
	    	  
					return Restangular.all("totalMonthPlanRsService/exportDetailTargetTotalMonthPlan").post(vm.target).then(function (d) {
						var data = d.plain();
						window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
						kendo.ui.progress(element, false);
					}).catch(function (e) {
						kendo.ui.progress(element, false);
						toastr.error(gettextCatalog.getString("L???i khi t???i bi???u m???u!"));
						return;
					});
				});
			
	  }
		displayLoading(".tab-content");
            
        }
        vm.exportFileDamBaoNguonViec= function() {
        	function displayLoading(target) {
				var element = $(target);
				kendo.ui.progress(element, true);
				setTimeout(function(){
	    	  
					return Restangular.all("totalMonthPlanRsService/exportDetailSourceTotalMonthPlan").post(vm.source).then(function (d) {
		                var data = d.plain();
		                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
		                kendo.ui.progress(element, false);
		            }).catch(function (e) {
		            	kendo.ui.progress(element, false);
		                toastr.error(gettextCatalog.getString("L???i khi t???i bi???u m???u!"));
		                return;
		            });
				});
			
	  }
		displayLoading(".tab-content");
            
        }
        vm.exportFileForceMaintain= function(){
        	function displayLoading(target) {
				var element = $(target);
				kendo.ui.progress(element, true);
				setTimeout(function(){
	    	  
					return Restangular.all("totalMonthPlanRsService/exportDetailForceMaintainTotalMonthPlan").post(vm.forceMaintain).then(function (d) {
		                var data = d.plain();
		                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
		                kendo.ui.progress(element, false);
		            }).catch(function (e) {
		            	kendo.ui.progress(element, false);
		                toastr.error(gettextCatalog.getString("L???i khi t???i bi???u m???u!"));
		                return;
		            });
				});
			
	  }
		displayLoading(".tab-content");
            
        }
        vm.exportFileBTSG= function(){
        	function displayLoading(target) {
				var element = $(target);
				kendo.ui.progress(element, true);
				setTimeout(function(){
	    	  
					vm.totalMonthPlanId= vm.totalMonthPlanId==null?-1:vm.totalMonthPlanId
				            return Restangular.all("totalMonthPlanRsService/exportDetailBTSGTotalMonthPlan").post(vm.totalMonthPlanId).then(function (d) {
				                var data = d.plain();
				                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
				                kendo.ui.progress(element, false);
				            }).catch(function (e) {
				            	kendo.ui.progress(element, false);
				                toastr.error(gettextCatalog.getString("L???i khi t???i bi???u m???u!"));
				                return;
				            });
				});
			
	  }
		displayLoading(".tab-content");  
        }
        vm.exportFileForceNew= function(){
        	function displayLoading(target) {
				var element = $(target);
				kendo.ui.progress(element, true);
				setTimeout(function(){
	    	  
					vm.totalMonthPlanId= vm.totalMonthPlanId==null?-1:vm.totalMonthPlanId
				            return Restangular.all("totalMonthPlanRsService/exportDetailForceNewTotalMonthPlan").post(vm.totalMonthPlanId).then(function (d) {
				                var data = d.plain();
				                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
				                kendo.ui.progress(element, false);
				            }).catch(function (e) {
				            	kendo.ui.progress(element, false);
				                toastr.error(gettextCatalog.getString("L???i khi t???i bi???u m???u!"));
				                return;
				            });
				});
			
	  }
		displayLoading(".tab-content");
            
        }

        vm.exportFileMaterial= function(){
        	function displayLoading(target) {
				var element = $(target);
				kendo.ui.progress(element, true);
				setTimeout(function(){
	    	  
					return Restangular.all("totalMonthPlanRsService/exportDetailMaterialTotalMonthPlan").post(vm.material).then(function (d) {
		                var data = d.plain();
		                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
		                kendo.ui.progress(element, false);
		            }).catch(function (e) {
		            	kendo.ui.progress(element, false);
		                toastr.error(gettextCatalog.getString("L???i khi t???i bi???u m???u!"));
		                return;
		            });
				});
			
	  }
		displayLoading(".tab-content");
            
        }
        vm.exportFileFinance= function(){
        	function displayLoading(target) {
				var element = $(target);
				kendo.ui.progress(element, true);
				setTimeout(function(){
	    	  
					return Restangular.all("totalMonthPlanRsService/exportDetailFinanceTotalMonthPlan").post(vm.finance).then(function (d) {
		                var data = d.plain();
		                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
		                kendo.ui.progress(element, false);
		            }).catch(function (e) {
		            	kendo.ui.progress(element, false);
		                toastr.error(gettextCatalog.getString("L???i khi t???i bi???u m???u!"));
		                return;
		            });
				});
			
	  }
		displayLoading(".tab-content");
            
        }
        vm.exportFileContract= function(){
        	function displayLoading(target) {
				var element = $(target);
				kendo.ui.progress(element, true);
				setTimeout(function(){
	    	  
					return Restangular.all("totalMonthPlanRsService/exportDetailContractTotalMonthPlan").post(vm.contract).then(function (d) {
		                var data = d.plain();
		                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
		                kendo.ui.progress(element, false);
		            }).catch(function (e) {
		            	kendo.ui.progress(element, false);
		                toastr.error(gettextCatalog.getString("L???i khi t???i bi???u m???u!"));
		                return;
		            });
				});
			
	  }
		displayLoading(".tab-content");
            
        }

		function refreshGrid(d) {
			var grid = vm.totalMonthPlanGrid;
			if(grid){
				grid.dataSource.data(d);
				grid.refresh();
			}
		}




        vm.add = add;
        function add() {
            return Restangular.all("totalMonthPlanRsService/checkPermissionsAdd").post().then(function (d) {
                if(d.error){
                    toastr.error(d.error);
                    return;
                }
            }).catch(function (e) {
            initTab2Table();
        	vm.attachFileGrid.dataSource.data([]);
            vm.showDetail = true;
            vm.action = "Th??m m???i k??? ho???ch th??ng";
            vm.String = "Qu???n l?? c??ng tr??nh > Qu???n l?? k??? ho???ch > Th??m m???i k??? ho???ch th??ng";
            vm.chooseTab(1);
            var year = (new Date()).getFullYear();
            var month = (new Date()).getMonth()+1;
            vm.totalMonthPlan = {
                year: year,
                month: month,
                conclution:'Ph??ng H??? T???ng:\nPh??ng Thanh Kho???n:\nPh??ng ?????u t??:\nKinh Doanh :\nPh??ng T??i ch??nh :\nPh??ng Qu???n l?? t??i s???n:\nPh??ng k??? ho???ch:\nC??c Chi Nh??nh khu v???c:'

            }
            if(vm.totalMonthPlan.month>=10){
                vm.totalMonthPlan.name = 'K??? ho???ch t???ng th??? th???c hi???n th??ng ' + vm.totalMonthPlan.month + "/" + year
            }else{
                vm.totalMonthPlan.name = 'K??? ho???ch t???ng th??? th???c hi???n th??ng ' +'0'+ vm.totalMonthPlan.month + "/" + year
            }
  //          vm.totalMonthPlan.name = 'K??? ho???ch t???ng th??? th???c hi???n th??ng ' + vm.totalMonthPlan.month + "/" + year
            totalMonthPlanService.getSequence({}).then(function (seq) {
                vm.seq = seq;
                if(vm.totalMonthPlan.month>=10){
                    vm.totalMonthPlan.code = seq + '/KHTTT-CTCT/XL' + year + vm.totalMonthPlan.month;
                }else{
                    vm.totalMonthPlan.code = seq + '/KHTTT-CTCT/XL' + year +'0'+ vm.totalMonthPlan.month;
                }
     //           vm.totalMonthPlan.code = seq + '/KHTTT-CTCT/XL' + year + vm.totalMonthPlan.month;
            }, function (err) {

            });
            });
        }

        vm.changeCodeName = function () {
            if(vm.totalMonthPlan.month>=10){
                vm.totalMonthPlan.name = 'K??? ho???ch t???ng th??? th???c hi???n th??ng ' + vm.totalMonthPlan.month + "/" + vm.totalMonthPlan.year;
            }else{
                vm.totalMonthPlan.name = 'K??? ho???ch t???ng th??? th???c hi???n th??ng ' +'0'+ vm.totalMonthPlan.month + "/" + vm.totalMonthPlan.year;
            }
   //         vm.totalMonthPlan.name = 'K??? ho???ch t???ng th??? th???c hi???n th??ng ' + vm.totalMonthPlan.month + "/" + vm.totalMonthPlan.year;
            totalMonthPlanService.getSequence({}).then(function (seq) {
                vm.seq = seq;
                if(vm.totalMonthPlan.month>=10){
                    vm.totalMonthPlan.code = seq + '/KHTTT-CTCT/XL' + vm.totalMonthPlan.year + vm.totalMonthPlan.month;
                }else{
                    vm.totalMonthPlan.code = seq + '/KHTTT-CTCT/XL' + vm.totalMonthPlan.year +'0'+ vm.totalMonthPlan.month;
                }
     //           vm.totalMonthPlan.code = seq + '/KHTTT-CTCT/XL' + vm.totalMonthPlan.year + vm.totalMonthPlan.month;
            }, function (err) {

            });
        }

        vm.edit = edit;
        function edit(dataItem) {
            //debugger;
            vm.action = "Ch???nh s???a  k??? ho???ch th??ng";
            vm.String = "Qu???n l?? c??ng tr??nh > Qu???n l?? k??? ho???ch > Chi ti???t k??? ho???ch th??ng";
            vm.showDetail = true;
            vm.diss = dataItem.signState!=1 ;
            vm.totalMonthPlan.totalMonthPlanId= dataItem.totalMonthPlanId;
            //   vm.fillTargetTable();
            //vm.fillAppendixTable();

            totalMonthPlanService.getById(dataItem.totalMonthPlanId).then(function (data) {
                    vm.totalMonthPlan = data;
                    refreshappendixGrid(data);
                    //refreshGrid(data.listAttachFile,vm.attachFileGrid);
                 refreshAttachFileGrid(data);
                refreshAllGridTab2(data);
                    $timeout(function(){
                        vm.targetGrid.dataSource.page(1);
                        vm.sourceGrid.dataSource.page(1);
                        vm.forceMaintainGrid.dataSource.page(1);
                        vm.BTSGrid.dataSource.page(1);
                        vm.forceNewGrid.dataSource.page(1);
                        vm.financeGrid.dataSource.page(1);
                        vm.contractGrid.dataSource.page(1);
                        vm.materialGrid.dataSource.page(1);
                    },1000)
                return Restangular.all("totalMonthPlanRsService/checkPermissionsUpdate").post().then(function (d) {
                    if(d==2){
                        vm.disableUpdate = true;
                    }
                    else if(d == 0){
                        vm.disableUpdate = false;
                    }
                })
                }, function (error) {
                    toastr.error(gettextCatalog.getString("C?? l???i x???y ra"));
            });
            initTab2Table();

        }

        vm.copy = copy;
        function copy(dataItem) {
            return Restangular.all("totalMonthPlanRsService/checkPermissionsCopy").post().then(function (d) {
                if(d.error){
                    toastr.error(d.error);
                    return;
                }
            }).catch(function (e) {
                vm.String = "Qu???n l?? c??ng tr??nh > Qu???n l?? k??? ho???ch > Sao ch??p k??? ho???ch th??ng";
                vm.action = "Th??m m???i k??? ho???ch th??ng";
                vm.showDetail = true;
                totalMonthPlanService.getByIdCopy(dataItem.totalMonthPlanId).then(function (data) {
                    vm.totalMonthPlan = data;
                    refreshAllGridTab2(data);
                    refreshGrid(data.listAttachFile, vm.attachFileGrid);
                    vm.totalMonthPlan.totalMonthPlanId = undefined;


                }, function (error) {
                    toastr.error(gettextCatalog.getString("C?? l???i x???y ra"));
                });
            })
        }

vm.target={};
vm.source={};
vm.material={};
vm.finance={};
vm.contract={};
vm.forceMaintain={};
vm.BTSG={};
vm.forceNew={};
        function refreshappendixGrid(data) {
            var appendixGrid = vm.appendixGrid;
            if (appendixGrid) {
                angular.forEach(data.appendixFileDTOList,function(item){
                    item.appParam ={
                        code : item.appParamCode,
                        name : item.appParamName
                    };
                })
                appendixGrid.dataSource.data(data.appendixFileDTOList);
                appendixGrid.refresh();
            }
        }
        function refreshAttachFileGrid(data) {
            var attachFileGrid = vm.attachFileGrid;
            if (attachFileGrid) {

                attachFileGrid.dataSource.data(data.listAttachFile);
                attachFileGrid.refresh();
            }
        }
        function refreshAllGridTab2(data) {
            var targetGrid = vm.targetGrid;
            var sourceGrid = vm.sourceGrid;
            var forceMaintainGrid = vm.forceMaintainGrid;
            var BTSGrid = vm.BTSGrid;
            var forceNewGrid = vm.forceNewGrid;
            var materialGrid = vm.materialGrid;
            var financeGrid = vm.financeGrid;
            var contractGrid = vm.contractGrid;
            var appendixGrid = vm.appendixGrid;
            if (targetGrid) {
                vm.target.totalMonthPlanId=data.totalMonthPlanId ;
                targetGrid.dataSource.data(data.tmpnTargetDTOList);
                targetGrid.refresh();
            }
            if (sourceGrid) {
                vm.source.totalMonthPlanId=data.totalMonthPlanId ;
                sourceGrid.dataSource.data(data.tmpnSourceDTOList);
                sourceGrid.refresh();
            }
            if (forceMaintainGrid) {
                vm.forceMaintain.totalMonthPlanId=data.totalMonthPlanId ;
                forceMaintainGrid.dataSource.data(data.tmpnForceMaintainDTOList);
                forceMaintainGrid.refresh();
            }
            if (BTSGrid) {
                vm.BTSG.totalMonthPlanId=data.totalMonthPlanId ;
                BTSGrid.dataSource.data(data.tmpnForceNewBtsDTOList);
                BTSGrid.refresh();
            }
            if (forceNewGrid) {
                vm.forceNew.totalMonthPlanId=data.totalMonthPlanId ;
                forceNewGrid.dataSource.data(data.tmpnForceNewLineDTOList);
                forceNewGrid.refresh();
            }
            if (materialGrid) {
                vm.material.totalMonthPlanId=data.totalMonthPlanId ;
                materialGrid.dataSource.data(data.tmpnMaterialDTOList);
                materialGrid.refresh();
            }
            if (financeGrid) {
                vm.finance.totalMonthPlanId=data.totalMonthPlanId ;
                financeGrid.dataSource.data(data.tmpnFinanceDTOList);
                financeGrid.refresh();
            }
            if (contractGrid) {
                vm.contract.totalMonthPlanId=data.totalMonthPlanId ;
                contractGrid.dataSource.data(data.tmpnContractDTOList);
                contractGrid.refresh();
            }
            if (appendixGrid) {
                angular.forEach(data.appendixFileDTOList,function(item){
                    item.appParam ={
                        code : item.appParamCode,
                        name : item.appParamName
                    };
                })
                appendixGrid.dataSource.data(data.appendixFileDTOList);
                appendixGrid.refresh();
            }
        }
//        hungnx 20180619 start
        vm.loadingSave = false;
//      hungnx 20180619 end
        vm.save = function () {        	        	
            if (validateTotalMonthDetail()) {
                if (vm.totalMonthPlan.totalMonthPlanId == null || vm.totalMonthPlan.totalMonthPlanId == '') {
                    var data = populateDataToSave();
                    if(vm.attachFileGrid!=undefined && vm.attachFileGrid.dataSource!=undefined)
                        data.listAttachFile = vm.attachFileGrid.dataSource._data
                    if(vm.appendixGrid!=undefined && vm.appendixGrid.dataSource!=null){
                        var list = vm.appendixGrid.dataSource.data();
                        for(var i = 0 ;i < list.length;i++) {
                            if (list[i].appParam.code === "choose" || list[i].appParam.name == "---Ch???n---") {
                                vm.appendixGrid.editCell(vm.appendixGrid.tbody.find('tr:eq(' + i + ')').find("td").eq(2));
                                toastr.warning("Ch??a ch???n lo???i file ph??? l???c!");
                                return;
                            }
                            list[i].appParamCode = list[i].appParam.code;
                            for(var j =i+1;j<list.length;j++){
                                if(list[i].appParam.code==list[j].appParam.code){
                                    vm.appendixGrid.editCell(vm.appendixGrid.tbody.find('tr:eq(' + j + ')').find("td").eq(2));
                                    toastr.warning("Ph??? l???c kh??ng h???p l??? do nhi???u b???n ghi c??ng m???t lo???i file!");
                                    return;
                                }
                            }
                        }
                        data.appendixFileDTOList=list;
                    }
//                  hungnx 20180619 start
                    vm.loadingSave = true;
//                  hungnx 20180619 end
                    totalMonthPlanService.createTotalMonthPlan(data).then(function (result) {
                        if (result.error) {
                            toastr.error(result.error);
//                          hungnx 20180619 start
                            vm.loadingSave = false;
//                          hungnx 20180619 end
                            return;
                        }
                        toastr.success("Th??m m???i th??nh c??ng!");
                        vm.cancel();
                        vm.totalMonthPlanGrid.dataSource.page(1);
//                      hungnx 20180619 start
                        vm.loadingSave = false;
//                      hungnx 20180619 end
                    }, function (errResponse) {
                        toastr.error(gettextCatalog.getString("L???i xu???t hi???n khi th??m m???i"));
//                      hungnx 20180619 start
                        vm.loadingSave = false;
//                      hungnx 20180619 end
                    });
                }
                else {
//                  hungnx 20180619 start
                    vm.loadingSave = true;
//                  hungnx 20180619 end
                    if(vm.attachFileGrid!=undefined && vm.attachFileGrid.dataSource!=undefined)
                        vm.totalMonthPlan.listAttachFile = vm.attachFileGrid.dataSource._data
                    totalMonthPlanService.updateTotalMonthPlan(vm.totalMonthPlan).then(function (result) {
                        if (result.error) {
                            toastr.error(result.error);
//                          hungnx 20180619 start
                            vm.loadingSave = false;
//                          hungnx 20180619 end
                            return;
                        }
                        toastr.success("Ch???nh s???a th??nh c??ng!");
                        vm.cancel();
                        vm.totalMonthPlanGrid.dataSource.page(1);
//                      hungnx 20180619 start
                        vm.loadingSave = false;
//                      hungnx 20180619 end
                    }, function (errResponse) {
                        toastr.error(gettextCatalog.getString("L???i xu???t hi???n khi ch???nh s???a"));
//                      hungnx 20180619 start
                        vm.loadingSave = false;
//                      hungnx 20180619 end
                    });
                }
            }
        }
        function populateDataToSave() {
            var data = vm.totalMonthPlan;
            var targetGrid = vm.targetGrid;
            var sourceGrid = vm.sourceGrid;
            var forceMaintainGrid = vm.forceMaintainGrid;
            var BTSGrid = vm.BTSGrid;
            var forceNewGrid = vm.forceNewGrid;
            var materialGrid = vm.materialGrid;
            var financeGrid = vm.financeGrid;
            var contractGrid = vm.contractGrid;
            var appendixGrid = vm.appendixGrid;
            if (targetGrid != undefined && targetGrid.dataSource != undefined)
                data.tmpnTargetDTOList = targetGrid.dataSource._data;
            if (sourceGrid != undefined && sourceGrid.dataSource != undefined)
                data.tmpnSourceDTOList = sourceGrid.dataSource._data;
            if (forceMaintainGrid != undefined && forceMaintainGrid.dataSource != undefined)
                data.tmpnForceMaintainDTOList = forceMaintainGrid.dataSource._data;
            if (BTSGrid != undefined && BTSGrid.dataSource != undefined)
                data.tmpnForceNewBtsDTOList = BTSGrid.dataSource._data;
            if (forceNewGrid != undefined && forceNewGrid.dataSource != undefined)
                data.tmpnForceNewLineDTOList = forceNewGrid.dataSource._data;
            if (materialGrid != undefined && materialGrid.dataSource != undefined)
                data.tmpnMaterialDTOList = materialGrid.dataSource._data;
            if (contractGrid != undefined && contractGrid.dataSource != undefined)
                data.tmpnContractDTOList = contractGrid.dataSource._data;
            if (financeGrid != undefined && financeGrid.dataSource != undefined)
                data.tmpnFinanceDTOList = financeGrid.dataSource._data;
            if (appendixGrid != undefined && appendixGrid.dataSource != undefined)
                data.appendixFileDTOList = appendixGrid.dataSource._data;
            return data;
        }

        function validateTotalMonthDetail() {
            var isValid = true;
            if (vm.totalMonthPlan.year == undefined || vm.totalMonthPlan.year == '') {
                toastr.warning("N??m kh??ng ???????c ????? tr???ng!");
                return false;
            }
            if (vm.totalMonthPlan.month == undefined || vm.totalMonthPlan.month == '') {
                toastr.warning("Th??ng kh??ng ???????c ????? tr???ng!");
                return false;
            }
            if (vm.totalMonthPlan.code == undefined || vm.totalMonthPlan.code == '') {
                toastr.warning("M?? k??? ho???ch kh??ng ???????c ????? tr???ng!");
                return false;
            }
            if (vm.totalMonthPlan.name == undefined || vm.totalMonthPlan.name == '') {
                toastr.warning("T??n k??? ho???ch kh??ng ???????c ????? tr???ng!");
                return false;
            }
            return isValid;
        }

        vm.cancel = cancel;
        function cancel() {
            vm.yearPlan={};
            vm.different={};
            vm.yearPlanSource={};
            vm.differentSource={};
            vm.value = 1;
            vm.showDetail = false;
            vm.prevStep();
            vm.totalMonthPlanTemp = {}
            vm.totalMonthPlan = {};
            vm.status = 1;
            vm.targetGrid.dataSource.data([]);
            vm.targetGrid.refresh();
            vm.sourceGrid.dataSource.data([]);
            vm.sourceGrid.refresh();
            vm.forceMaintainGrid.dataSource.data([]);
            vm.forceMaintainGrid.refresh();
            vm.BTSGrid.dataSource.data([]);
            vm.BTSGrid.refresh();
            vm.forceNewGrid.dataSource.data([]);
            vm.forceNewGrid.refresh();
            vm.materialGrid.dataSource.data([]);
            vm.materialGrid.refresh();
            vm.financeGrid.dataSource.data([]);
            vm.financeGrid.refresh();
            vm.appendixGrid.dataSource.data([]);
            vm.appendixGrid.refresh();
            vm.doSearch();
            vm.diss=false;
            vm.String="Qu???n l?? c??ng tr??nh > Qu???n l?? k??? ho???ch > K??? ho???ch th??ng t???ng th???";
        }

        vm.remove = remove;
        function remove(dataItem) {
            return Restangular.all("totalMonthPlanRsService/checkPermissionsDelete").post().then(function (d) {
                if(d.error){
                    toastr.error(d.error);
                    return;
                }
            }).catch(function (e) {
                confirm('B???n th???t s??? mu???n x??a b???n ghi ???? ch???n?', function () {
                    totalMonthPlanService.remove(dataItem).then(
                        function (d) {
                            if (d != undefined && d.error) {
                                toastr.error(d.error);
                                return;
                            }
                            toastr.success("X??a b???n ghi th??nh c??ng!");
                            vm.totalMonthPlanGrid.dataSource.page(1);
                            vm.totalMonthPlanGrid.refresh();
                            var sizePage = $("#totalMonthPlanGrid").data("kendoGrid").dataSource.total();
                            var pageSize = $("#totalMonthPlanGrid").data("kendoGrid").dataSource.pageSize();
                            if (sizePage % pageSize === 1) {
                                var currentPage = $("#totalMonthPlanGrid").data("kendoGrid").dataSource.page();
                                if (currentPage > 1) {
                                    $("#totalMonthPlanGrid").data("kendoGrid").dataSource.page(currentPage - 1);
                                }
                            }
                            /* $("#totalMonthPlanGrid").data('kendoGrid').dataSource.read();
                             $("#totalMonthPlanGrid").data('kendoGrid').refresh();*/

                        }, function (errResponse) {
                            toastr.error("L???i kh??ng x??a ???????c!");
                        });
                })
            })
        }

      //hungtd_20181213_start
        vm.updateRegistry = updateRegistry;
        function updateRegistry(dataItem) {
            return Restangular.all("totalMonthPlanRsService/checkPermissionsRegistry").post().then(function (d) {
                if(d.error){
                    toastr.error(d.error);
                    return;
                }
            }).catch(function (e) {
                confirm('B???n th???t s??? mu???n tr??nh k?? b???n ghi ???? ch???n?', function () {
                    totalMonthPlanService.updateRegistry(dataItem).then(
                        function (d) {
                            if (d != undefined && d.error) {
                                toastr.error(d.error);
                                return;
                            }
                            toastr.success("tr??nh k?? b???n ghi th??nh c??ng!");
                            vm.totalMonthPlanGrid.dataSource.page(1);
                            vm.totalMonthPlanGrid.refresh();
                            var sizePage = $("#totalMonthPlanGrid").data("kendoGrid").dataSource.total();
                            var pageSize = $("#totalMonthPlanGrid").data("kendoGrid").dataSource.pageSize();
                            if (sizePage % pageSize === 1) {
                                var currentPage = $("#totalMonthPlanGrid").data("kendoGrid").dataSource.page();
                                if (currentPage > 1) {
                                    $("#totalMonthPlanGrid").data("kendoGrid").dataSource.page(currentPage - 1);
                                }
                            }
                            /* $("#totalMonthPlanGrid").data('kendoGrid').dataSource.read();
                             $("#totalMonthPlanGrid").data('kendoGrid').refresh();*/

                        }, function (errResponse) {
                            toastr.error("L???i kh??ng tr??nh k?? ???????c!");
                        });
                })
            })
        }
//        hungtd_20181213_end
        vm.canceldoSearch = function () {
            vm.showDetail = false;
            vm.totalMonthPlanSearch = {
                status: "1",
            };
            doSearch();
        }

        vm.doSearch = doSearch;
        function doSearch() {
            vm.showDetail = false;
                vm.totalMonthPlanGrid.dataSource.page(1);
        }


        vm.showHideColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.totalMonthPlanGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.totalMonthPlanGrid.showColumn(column);
            } else {
                vm.totalMonthPlanGrid.hideColumn(column);
            }


        }
        /*
         * * Filter c??c c???t c???a select
         */

        vm.gridColumnShowHideFilter = function (item) {
            return item.type == null || item.type !== 1;
        };


        vm.exportpdf = function () {
            var obj = {};
            totalMonthPlanService.exportpdf(obj);
        }


        /*
         * var specialChars = "<>@!#$%^&*()+[]{}?:;|'\"\\,./~`-="; var
         * check = function(string){ for(var i = 0; i <
         * specialChars.length;i++){ if(string.indexOf(specialChars[i]) >
         * -1){ return true; } } vm.listApp.mess=""; return false; } var
         * check1 = function(string){ for(var i = 0; i <
         * specialChars.length;i++){ if(string.indexOf(specialChars[i]) >
         * -1){ return true; } } vm.listApp.mess1=""; return false; } var
         * check2 = function(string){ for(var i = 0; i <
         * specialChars.length;i++){ if(string.indexOf(specialChars[i]) >
         * -1){ return true; } } vm.listApp.mess2=""; return false; }
         *
         * vm.checkErr = checkErr; function checkErr() { var parType =
         * $('#parType').val(); var code = $('#code').val(); var
         * name=$('#name').val(); if(check(parType) === true){
         * vm.listApp.mess = "Lo???i tham s??? ch???a k?? t??? ?????c bi???t"; }
         * if(check1(code) === true){ vm.listApp.mess1 = "M?? tham s??? ch???a k??
         * t??? ?????c bi???t"; } if(check2(name) === true){ vm.listApp.mess2 =
         * "T??n Tham s??? k?? t??? ?????c bi???t"; } return vm.listApp; }
         */
        vm.errNumber = "";
        vm.checkNumber = checkNumber;
        function checkNumber() {
            var val = $('#parOder').val();
            if (val === 0) {
                if (val === 0) {
                    if (val === "") {
                        vm.errNumber = "";
                    } else {
                        vm.errNumber = "Ph???i nh???p ki???u s??? nguy??n t??? 1-99";
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
                    vm.errNumber = "Ph???i nh???p ki???u s??? nguy??n t??? 1-99";
                } else {
                    vm.errNumber = "";
                }

            }


        }

        vm.openDepartmentTo = openDepartmentTo

        function openDepartmentTo(popUp) {
            vm.obj = {};
            CommonService.getDepartment(vm.obj).then(function (result) {
                var templateUrl = 'wms/popup/findDepartmentPopUp.html';
                var title = gettextCatalog.getString("T??m ki???m ????n v???");
                var data = result.plain();
                vm.gridOptions1 = kendoConfig.getGridOptions({
                    autoBind: true,
                    resizable: true,
                    columnMenu: false,
                    dataSource: result,
                    noRecords: true,
                    pageSize: 10,
                    messages: {
                        noRecords: gettextCatalog.getString("Kh??ng c?? k???t qu??? hi???n th???")
                    },
                    pageable: {
                        refresh: false,
                        pageSizes: [10, 15, 20, 25],
                        messages: {
                            display: "{0}-{1} c???a {2} k???t qu???",
                            itemsPerPage: "k???t qu???/trang",
                            empty: "Kh??ng c?? k???t qu??? hi???n th???"
                        }
                    },
                    columns: [{
                        title: "TT",
                        field: "#",
                        template: dataItem => $("#gridView").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
                        width: "12%",
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:center;"
                        },
                    },
                        {
                            title: "M?? ph??ng<br> ban",
                            field: "code",
                            width: "18%",
                            headerAttributes: {
                                style: "text-align:center;"
                            },
                            attributes: {
                                style: "text-align:right;"
                            },
                        }, {
                            title: "T??n ph??ng ban",
                            field: "text",
                            width: "30%",
                            headerAttributes: {
                                style: "text-align:center;"
                            },
                            attributes: {
                                style: "text-align:left;"
                            },
                        }, {
                            title: "????n v??? cha",
                            field: "parentName",
                            width: "30%",
                            headerAttributes: {
                                style: "text-align:center;"
                            },
                            attributes: {
                                style: "text-align:left;"
                            },
                        }, {
                            title: "Ng??y hi???u l???c",
                            field: "effectDate",
                            width: "20%",
                            headerAttributes: {
                                style: "text-align:center;"
                            },
                            attributes: {
                                style: "text-align:left;"
                            },
                        }, {
                            title: "Ng??y h???t hi???u l???c",
                            field: "endDate",
                            width: "25%",
                            headerAttributes: {
                                style: "text-align:center;"
                            },
                            attributes: {
                                style: "text-align:left;"
                            },
                        }, {
                            title: "Ch???n",
                            template: '<div class="text-center "> ' +
                            '		<a  type="button" class=" icon_table" uib-tooltip="Ch???n" translate>' +
                            '			<i id="#=departmentId#" ng-click=save(dataItem) class="fa fa-check color-green" aria-hidden="true"></i> ' +
                            '		</a>'
                            + '</div>',
                            width: "15%",
                            field: "stt"
                        }]
                });
                CommonService.populatePopupDept(templateUrl, title, vm.gridOptions1, data, vm, popUp, 'string', false, '92%', '89%');
            });
        }

        // clear data
        vm.changeDataAuto = changeDataAuto
        function changeDataAuto(id) {
            switch (id) {
                case 'dept':
                {
                    if (processSearch(id, vm.selectedDept1)) {
                        vm.totalMonthPlanTemp.sysGroupId = null;
                        vm.totalMonthPlanTemp.sysGroupName = null;
                        vm.selectedDept1 = false;
                    }
                    break;
                }
            }
        }

        vm.cancelInput = function (param) {
            if (param == 'dept') {
                vm.totalMonthPlanTemp.sysGroupId = null;
                vm.totalMonthPlanTemp.sysGroupName = null;
            }
        }
        // 8.2 Search SysGroup
        vm.selectedDept1 = false;
        vm.deprtOptions1 = {
            dataTextField: "text",
            dataValueField: "id",
            select: function (e) {
                vm.selectedDept1 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.totalMonthPlanTemp.sysGroupName = dataItem.text;
                ;
                vm.totalMonthPlanTemp.sysGroupId = dataItem.id;
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
                        return Restangular.all("departmentServiceRest/department/" + 'getForAutocompleteDept').post({
                            name: vm.totalMonthPlanTemp.sysGroupName,
                            pageSize: vm.deprtOptions1.pageSize
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Kh??ng th??? k???t n???i ????? l???y d??? li???u: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.totalMonthPlanTemp.sysGroupName = null;// th??nh name
                    vm.totalMonthPlanTemp.sysGroupId = null;
                }
            },
            ignoreCase: false
        }
        vm.sendToSign = sendToSign;
        function sendToSign(dataItem) {
            return Restangular.all("totalMonthPlanRsService/checkPermissionsRegistry").post().then(function (d) {
                if(d.error){
                    toastr.error(d.error);
                    return;
                }
            }).catch(function (e) {
                if (dataItem.signState == 1 || dataItem.signState == 4) {
                    var teamplateUrl = "coms/popup/SignVofficePopup.html";
                    var title = "Tr??nh k?? k??? ho???ch th??ng t???ng th???";
                    var windowId = "ORDER";
                    vm.nameToSign = "Y??u c???u tr??nh k??";
                    var obj = angular.copy(dataItem);
                    obj.type = "11";
                    obj.reportName = "KeHoachThangTongThe";
                    obj.objectId = dataItem.totalMonthPlanId;
                    obj.objectCode = dataItem.code
                    obj.month = dataItem.month
                    obj.year = dataItem.year
                    obj.conclution = dataItem.conclution
                    obj.listSignVoffice = [];
                    var dataList = [];
                    Restangular.all("yearPlanRsService/getAppParamByType").post('MONTH_TOTAL_PLAN_VOFFICE').then(function (data) {
                        var i = 1;
                        angular.forEach(data, function (item) {
                            var dt = {};
                            dt.oderName = i + ". " + item.name;
                            dt.signLabelName = item.name;
                            dt.signOrder = item.code
                            obj.listSignVoffice.push(dt);
                            i++
                        })
                        dataList.push(obj);
                        CommonService.populatePopupVofice(teamplateUrl, title, '11', dataList, vm, windowId, false, '85%', '85%');
                    }, function (error) {
                        toastr.error("L???i khi l???y d??? li???u ng?????i k??!")
                    });
                    //obj.listSignVoffice.push({oderName: '1. Ph??ng h??? t???ng'});
                    //obj.listSignVoffice.push({oderName: '2. Ph??ng ?????u t??'});
                    //obj.listSignVoffice.push({oderName: '3. PG?? chuy??n tr??ch'});
                    //obj.listSignVoffice.push({oderName: '4. Gi??m ?????c'});
                    //dataList.push(obj);
                    //CommonService.populatePopupVofice(teamplateUrl, title, '11', dataList, vm, windowId, false, '85%', '85%');
                } else {
                    toastr.error("K??? ho???ch kh??ng ????ng tr???ng th??i k??!")
                }
            })
        }

        vm.onSelectFile = onSelectFile;
        function onSelectFile() {
            if ($("#fileImportTotal")[0].files[0].name.split('.').pop() != 'xls' && $("#fileImportTotal")[0].files[0].name.split('.').pop() != 'xlsx') {
                toastr.warning("Sai ?????nh d???ng file");
                setTimeout(function () {
                    $(".k-upload-files.k-reset").find("li").remove();
                    $(".k-upload-files").remove();
                    $(".k-upload-status").remove();
                    $(".k-upload.k-header").addClass("k-upload-empty");
                    $(".k-upload-button").removeClass("k-state-focused");
                }, 10);
                return;
            }
            else {
                if (104857600 < $("#fileImportTotal")[0].files[0].size) {
                    toastr.warning("Dung l?????ng file v?????t qu?? 100MB! ");
                    return;
                }
                $("#fileNameImport")[0].innerHTML = $("#fileImportTotal")[0].files[0].name
            }
        }

        $scope.onSelectAttachFile = function (e) {
            if ($("#fileVuong")[0].files[0].name.split('.').pop() != 'xls' && $("#fileVuong")[0].files[0].name.split('.').pop() != 'xlsx' && $("#fileVuong")[0].files[0].name.split('.').pop() != 'doc' && $("#fileVuong")[0].files[0].name.split('.').pop() != 'docx' && $("#fileVuong")[0].files[0].name.split('.').pop() != 'pdf') {
                toastr.warning("Sai ?????nh d???ng file");
                setTimeout(function () {
                    $(".k-upload-files.k-reset").find("li").remove();
                    $(".k-upload-files").remove();
                    $(".k-upload-status").remove();
                    $(".k-upload.k-header").addClass("k-upload-empty");
                    $(".k-upload-button").removeClass("k-state-focused");
                }, 10);
                return;
            }
            if (104857600 < $("#fileVuong")[0].files[0].size) {
                toastr.warning("Dung l?????ng file v?????t qu?? 100MB! ");
                return;
            }
            var formData = new FormData();
            jQuery.each(jQuery('#fileVuong')[0].files, function (i, file) {
                formData.append('multipartFile' + i, file);
            });
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "fileservice/uploadATTT",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    var dataFile = []
                    jQuery.each(jQuery('#fileVuong')[0].files, function (index, file) {
                        var obj = {};
                        obj.name = file.name;
                        obj.filePath = data[index];
                        obj.createdDate = new Date();
                        obj.createdUserName = Constant.userInfo.casUser.fullName
                        vm.attachFileGrid.dataSource.add(obj)
                    });
                    vm.attachFileGrid.refresh();
                    setTimeout(function () {
                        $(".k-upload-files.k-reset").find("li").remove();
                        $(".k-upload-files").remove();
                        $(".k-upload-status").remove();
                        $(".k-upload.k-header").addClass("k-upload-empty");
                        $(".k-upload-button").removeClass("k-state-focused");
                    }, 10);
                }
            });
        }
        vm.downloadFile = function (dataItem) {
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
        }
        vm.attachFileGridOptions = kendoConfig.getGridOptions({
            autoBind: true,
            scrollable: false,
            resizable: true,
            editable: false,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
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
                noRecords: gettextCatalog.getString("<div style='margin:5px'>Kh??ng c?? k???t qu??? hi???n th???</div>")
            },
            pageable: {
                refresh: false,
                pageSize: 10,
                pageSizes: [10, 15, 20, 25],
                messages: {
                    display: "{0}-{1} c???a {2} k???t qu???",
                    itemsPerPage: "k???t qu???/trang",
                    empty: "<div style='margin:5px'>Kh??ng c?? k???t qu??? hi???n th???</div>"
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
                },
                {
                    title: "T??n file",
                    field: 'name',
                    width: '40%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    },
                    template: function (dataItem) {
                        return "<a href='' ng-click='vm.downloadFile(dataItem)'>" + dataItem.name + "</a>";
                    }
                },
                {
                    title: "Ng??y upload",
                    field: 'createdDate',
                    width: '20%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:center;"
                    },
                    template: function (dataItem) {
                        return $filter('date')(dataItem.createdDate, 'dd/MM/yyyy');
                    }
                },
                {
                    title: "Ng?????i upload",
                    field: 'createdUserName',
                    width: '20%',
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    attributes: {
                        style: "text-align:left;"
                    }
                },
                {
                    title: "X??a",
                    headerAttributes: {style: "text-align:center;font-weight: bold;"},
                    template: dataItem =>
                    '<div class="text-center">'
                    + '<button style=" border: none; background-color: white;" id=""' +
                    'class=" icon_table" ng-click="vm.removeAttachFile($event)"  uib-tooltip="X??a" translate' + '>' +
                    '<i class="fa fa-trash" style="color: #337ab7;" aria-hidden="true"></i>' +
                    '</button>'
                    + '</div>',
                    width: '15%'
                }
            ]
        });
        vm.removeAttachFile = removeAttachFile
        function removeAttachFile(e) {
            var grid = vm.attachFileGrid;
            var row = $(e.target).closest("tr");
            var dataItem = grid.dataItem(row);
            grid.removeRow(dataItem); //just gives alert message
            grid.dataSource.remove(dataItem); //removes it actually from the grid
            grid.dataSource.sync();
            grid.refresh();
        }
        function refreshGrid(data,grid) {
            if(grid){
                grid.dataSource.data(data);
                grid.refresh();
            }
        }
        vm.viewSignedDoc = viewSignedDoc;
        function viewSignedDoc(dataItem){
            var obj={
                objectId:dataItem.totalMonthPlanId,
                type:"11"
            }
            CommonService.viewSignedDoc(obj);
        }
    }

})();
