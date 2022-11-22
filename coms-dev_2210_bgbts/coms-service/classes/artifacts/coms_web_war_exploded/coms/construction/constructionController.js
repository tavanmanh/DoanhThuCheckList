(function () {
    'use strict';
    var controllerId = 'constructionController';

    angular.module('MetronicApp').controller(controllerId, constructionController);

    function constructionController($scope, $rootScope, $timeout, gettextCatalog, $filter,
        kendoConfig, $kWindow, constructionService, $http,
        CommonService, PopupConst, Restangular, RestEndpoint, Constant, htmlCommonService) {
        initCommonFunction($scope, $rootScope, $filter);
        var vm = this;
        vm.countCons = 0;
        vm.showSearch = true;
        vm.isCreateNew = true;
        vm.showDetail = false;
        vm.showDetailGpon = true;
        vm.completeSearch = {};
        vm.constrObj = {};
    	var start = new Date();
    	start.setMonth(start.getMonth() - 6);
    	var end = new Date();
        vm.constructionSearch = {
        	dateFrom: htmlCommonService.formatDate(start),
        	dateTo: htmlCommonService.formatDate(end),
        };
        vm.construction = {};
        vm.workItemGPon = {};
        vm.regionDataList = [];
        vm.locationDataList = [{name: "Trên mái", code: 1}, {name: "Dưới đất", code: 2}];
        vm.lstWorkItemGPon =[];
        vm.catConstructionDeployDataList = [];
        vm.workItemRecord = { workItemTypeList: [] };
        vm.workItemRecord.isInternal = 1;
        $scope.taskAll = 0;
        $scope.taskSlow = 0;
        $scope.taskPause = 0;
        $scope.taskUnfulfilled = 0;
        $scope.idLog = 0;
        vm.checkNode =true;
        vm.checkEditGPon = false;
        vm.workItemId = 0;
        vm.fileLst = [];
        vm.granttSearch = {
            keyword: undefined
        };
        
        vm.checkWorkItemHtct= false;
        vm.checkB2BB2C = false; // HienLT56 add 28012021
        vm.String = "Quản lý công trình > Quản lý công trình  > Thông tin công trình";
        vm.construction = {};
        vm.allowAction = false;
        vm.openCatProvincePopup = openCatProvincePopup;
        vm.onSaveCatProvince = onSaveCatProvince;

        // Khoi tao du lieu cho form
        vm.iconDiss = true;
        vm.hidenIcon = hidenIcon;
        function hidenIcon() {
            vm.iconDiss = false;
        }
        vm.showIcon = function () {
            vm.iconDiss = true;
        }
        
        vm.checkRoleMapSolar = false;
        vm.constrObj.disable = false;
        vm.roleEditConstructionAdmin = false;
        $scope.workItemIdGpon = 0;
        vm.checkRevenueBranch = 0;
        vm.checkRoleApprove = false;
        vm.roleXDDD = false;
        initFormData();
        function initFormData() {
        	constructionService.checkRoleMapSolar().then(function (data) {
                if(data!=undefined && data=="OK"){
                	vm.checkRoleMapSolar = true;
                	fillDataTable([]);
                } else {
                	vm.checkRoleMapSolar = false;
                	fillDataTable([]);
                }
            }, function (e) {
                toastr.error("Có lỗi trong quá trình lấy dữ liệu quyền thao tác.");
                fillDataTable([]);
            });
        	
        	constructionService.checkRoleApprove().then(function (data) {
                if(data!=undefined && data=="OK"){
                	vm.checkRoleApprove = true;
                } else {
                	vm.checkRoleApprove = false;
                }
            }, function (e) {
                toastr.error("Có lỗi trong quá trình lấy dữ liệu quyền thao tác.");
            });
        
        	constructionService.checkRoleConstruction().then(function (data) {
        		vm.roleEditConstructionAdmin = data.roleEditConstructionAdmin;
            }, function (e) {
                toastr.error("Có lỗi trong quá trình lấy dữ liệu quyền thao tác.");
            });
        	
        	constructionService.checkRoleConstructionXDDD().then(function (data) {
        		 if(data!=undefined && data=="OK"){
                 	vm.roleXDDD = true;
                 } else {
                 	vm.roleXDDD = false;
                 }
            }, function (e) {
                toastr.error("Có lỗi trong quá trình lấy dữ liệu quyền thao tác.");
            });
        	
            fillconstructionDetailTable();
            initDropDownList();
            initDataConstructionType();
            

        }

        
        covenantFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
            kendoConfig, $kWindow, constructionService,
            CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm, $http);
        constructionDetailFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
            kendoConfig, $kWindow, constructionService,
            CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm);
        constructionBGMBFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
            kendoConfig, $kWindow, constructionService,
            CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm);
        startConstructionFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
            kendoConfig, $kWindow, constructionService,
            CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm);
        constructionMerchandiseFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
            kendoConfig, $kWindow, constructionService,
            CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm);
        workItemDetailFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
            kendoConfig, $kWindow, constructionService,
            CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm);


        completeFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
            kendoConfig, $kWindow, constructionService,
            CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm);

        acceptanceFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
            kendoConfig, $kWindow, constructionService,
            CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm);

        constrTaskFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
            kendoConfig, $kWindow, constructionService,
            CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm);

        //nhantv 25092018
        constructionLicenceFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
                kendoConfig, $kWindow, constructionService,
                CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm);

        constructionDesignFunction($scope, $rootScope, $timeout, gettextCatalog, $filter,
                kendoConfig, $kWindow, constructionService,
                CommonService, PopupConst, Restangular, RestEndpoint, Constant, vm);
        
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
            for (var i = 1; i < 13; i++) {
                vm.monthDataList.push({
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
            vm.monthDownListOptions = {
                dataSource: vm.monthDataList,
                dataTextField: "name",
                dataValueField: "id",
                valuePrimitive: true
            }
            
            
            // get data for popup


            constructionService.getCatConstructionDeploy().then(function (data) {
                vm.catConstructionDeployDataList = data;
            }, function (e) {
                toastr.error("Có lỗi trong quá trình lấy dữ liệu.");
            });

        }

        //hienvd: start 1-7-2019
        $scope.changeImage = changeImage;
        $scope.imageSelected = {};
        function changeImage(image) {
            $scope.imageSelected = image;
        }
		//hoanm1_20190708_start
		$scope.changeImageBGMB = changeImageBGMB;
        $scope.imageSelectedBGMB = {};
        function changeImageBGMB(image) {
            $scope.imageSelectedBGMB = image;
        }
//hoanm1_20190708_end
        //hienvd: end
        function TypeWorkHm(x) {
//        	if(x.checkHTCT!=null && x.checkHTCT==1){
//        		constructionService.getWorkItemTypeHTCT(x.catContructionTypeId).then(function (data) {
//                    vm.catWorkItemTypeList = data;
//                }, function (e) {
//                    toastr.error("Có lỗi trong quá trình lấy dữ liệu.");
//                });
//        	} else {
        		constructionService.getCatWorkItemType(x.catContructionTypeId).then(function (data) {
                    vm.catWorkItemTypeList = data;
                }, function (e) {
                    toastr.error("Có lỗi trong quá trình lấy dữ liệu.");
                });
//        	}
        }

        //chinhpxn 20180605 start
        function getSysGroupInfo(id) {
            constructionService.getSysGroupInfo(id).then(function (data) {
                vm.workItemRecord.constructorName1 = data.name;
                vm.workItemRecord.constructorId = data.groupId;
                vm.workItemRecord.supervisorName = data.name;
                vm.workItemRecord.supervisorId = data.groupId;
            })
        }
        //chinhpxn 20180605 end
        vm.statusDataList = [
            { id: 1, name: 'Chờ bàn giao mặt bằng' },
            { id: 2, name: 'Chờ khởi công' },
            { id: 3, name: 'Đang thực hiện' },
            { id: 4, name: 'Đã tạm dừng' },
            { id: 5, name: 'Đã hoàn thành' },
            { id: 6, name: 'Đã nghiệm thu' },
            { id: 7, name: 'Đã hoàn công' },
            { id: 8, name: 'Đã quyết toán' },
            { id: 9, name: 'Đã phát sóng trạm' },
            { id: 10, name: 'Đang dở dang' },
            { id: 11, name: 'Chờ duyệt ĐBHT' },
            { id: 0, name: 'Đã hủy' }
        ];
        
        vm.b2bB2cList = [
			{
				code: "",
				name: "~~Lựa chọn~~"
			},
			{
				code: "1",
				name: "B2B"
			},
			{
				code: "2",
				name: "B2C"
			}
		]
        
        //Huypq-29112021-start
        vm.branchList = [
			{
				code: "280483",
				name: "TT GPTH"
			},
			{
				code: "242656",
				name: "TTHT"
			},
			{
				code: "166677",
				name: "TT ĐTHT"
			},
			{
				code: "270120",
				name: "TT VHKT"
			},
			{
				code: "280501",
				name: "TT CNTT"
			},
			{
				code: "9006003",
				name: "TT XDDD"
			}
        ];
        //Huy-end
        
        // Quangtao - start
        initPopup();
        function initPopup() {
            initConstructionDetailRadioList();
            vm.constrObj = {};
            vm.option1Show = true;
        }

        vm.constrObj.catConstructionTypeId = 1;

        //        vm.constrObj.returnDate = new Date();
        vm.checkboxAction = checkboxAction;
        function checkboxAction(e) {
            if (vm.isCreateNew) {
                vm.constrObj.catStationCode = null;
                vm.constrObj.code = null;
                vm.constrObj.name = null;
                vm.constrObj.catStationId = null;
                vm.constrObj.isStationHtct = null;
            }
            if (e == 1) {
                initConstructionDetailRadioList();
                vm.option1Show = true;
            } else if (e == 2) {
                initConstructionDetailRadioList();
                vm.option2Show = true;
            } else if (e == 3) {
                initConstructionDetailRadioList();
                vm.option3Show = true;
            } else if (e == 4) {
                initConstructionDetailRadioList();
                vm.option4Show = true;
            } else if (e == 5) {
                initConstructionDetailRadioList();
                vm.option5Show = true;
            } else if (e == 6) {
                initConstructionDetailRadioList();
                vm.option6Show = true;
            } else if (e == 7) {
                initConstructionDetailRadioList();
                vm.option7Show = true;
            } else if (e == 8) {
                initConstructionDetailRadioList();
                vm.option8Show = true;
            }
            vm.constrObj.catConstructionTypeId = e;
        }

        vm.checkCodeConstr = function(){
        	//HienLT56 start 09032021
        	if(vm.constrObj.catConstructionTypeId == 2){
        		var subCode = vm.constrObj.code.substring(0, 8); 
        		if(subCode=="HTCT_VCC"){
            		vm.constrObj.isStationHtct="1";
            		vm.constrObj.checkHTCT = true;
            	} else {
            		vm.constrObj.isStationHtct="0";
            		vm.constrObj.checkHTCT = false;
            	}
        	} else if(vm.constrObj.catConstructionTypeId == 1 || vm.constrObj.catConstructionTypeId == 3 || vm.constrObj.catConstructionTypeId == 4 || vm.constrObj.catConstructionTypeId == 5
        			||vm.constrObj.catConstructionTypeId == 6 || vm.constrObj.catConstructionTypeId == 7 || vm.constrObj.catConstructionTypeId == 8){
        		var subCode = vm.constrObj.code.substring(0, 7);
        		if(subCode=="HTCT_CT"){
            		vm.constrObj.isStationHtct="1";
            		vm.constrObj.checkHTCT = true;
            	} else {
            		vm.constrObj.isStationHtct="0";
            		vm.constrObj.checkHTCT = false;
            	}
        	}
        	//HienLT56 end 09032021
        }
        
        function validate() {
            var isValid = true;
            if (vm.totalMonthPlan.year == undefined || vm.totalMonthPlan.year == '') {
                toastr.warning("Năm không được để trống!");
                return false;
            }
            if (vm.totalMonthPlan.month == undefined || vm.totalMonthPlan.month == '') {
                toastr.warning("Tháng không được để trống!");
                return false;
            }
            if (vm.totalMonthPlan.code == undefined || vm.totalMonthPlan.code == '') {
                toastr.warning("Mã kế hoạch không được để trống!");
                return false;
            }
            if (vm.totalMonthPlan.name == undefined || vm.totalMonthPlan.name == '') {
                toastr.warning("Tên kế hoạch không được để trống!");
                return false;
            }
            return isValid;
        }

        vm.createConstruction = createConstruction;
        function createConstruction() {
            //hienvd: TEST
            vm.constrObj.fileLst = !!$("#contractFileGrid").data("kendoGrid") ? $("#contractFileGrid").data("kendoGrid").dataSource.data() : [];
            if(vm.constrObj.checkDVNTTC == true){
            	vm.constrObj.unitConstruction = 1;
            }
            if(vm.constrObj.checkDVNTTC == false){
            	vm.constrObj.unitConstructionName = null;
            	vm.constrObj.unitConstruction = null;
            }
            if (validateTab1()){
            	if(vm.constrObj.checkHTCT == true){
                    vm.constrObj.checkHTCT = 1;
                }else {
                    vm.constrObj.checkHTCT = 0;
                    vm.constrObj.locationHTCT = null;
                    vm.constrObj.highHTCT = null;
                    vm.constrObj.capexHTCT = null;
                }
                constructionService.createconstruction(vm.constrObj).then(function (data) {
                    // close popup and refresh table list
                    if (data.error) {
                        toastr.error(data.error);
                        return;
                    }
                    toastr.success("Thêm mới dữ liệu thành công.");
                    doSearchContruction();
                    $("#constructionGrid").data('kendoGrid').dataSource.read();
                    $("#constructionGrid").data('kendoGrid').refresh();
                    cancelImport();
                    CommonService.dismissPopup1();
                    $(".k-icon.k-i-close").click();

                }, function (e) {
                    toastr.error("Có lỗi trong quá trình xử lý dữ liệu.");
                });

            }else{
                if($("#locationPopup").is(":visible") && $("#highHTCTPopup").is(":visible")){
                    vm.constrObj.checkHTCT = true;
                }
            }

        }
        //hienvd: COMMENT update Construction
        vm.updateConstruction = updateConstruction;
        function updateConstruction() {
            vm.constrObj.fileLst = vm.fileLst;
            //HIENVD: COMMENT getDataUpdate in ConstructionDAO
            if(vm.constrObj.checkDVNTTC == true){
            	vm.constrObj.unitConstruction = 1;
            }
            if(vm.constrObj.checkDVNTTC == false){
            	vm.constrObj.unitConstructionName = null;
            	vm.constrObj.unitConstruction = null;
            }
            if (validateTab1()){
            	//hienvd: Add 19/7/2019
                if (!!vm.constrObj.amountCableLive) {
                    var n = vm.constrObj.amountCableLive.toString();
                    vm.constrObj.amountCableLive = n.replace(/,/g, '');
                }
                if (!!vm.constrObj.amountCableRent) {
                    var n = vm.constrObj.amountCableRent.toString();
                    vm.constrObj.amountCableRent = n.replace(/,/g, '');
                }
                //hienvd: START 8/6/2019
                if(vm.constrObj.checkHTCT == true){
                    vm.constrObj.checkHTCT = 1;
                }else {
                    vm.constrObj.checkHTCT = 0;
                    vm.constrObj.locationHTCT = null;
                    vm.constrObj.highHTCT = null;
                    vm.constrObj.capexHTCT = null;
                }
                //hienvd: END
                //HIENVD: CHECK DON VI
            	// constructionService.checkUnitConstruction(vm.constrObj).then(function (result){
                // 	if(result == undefined){
                // 		toastr.warning("Tỉnh trưởng chưa tồn tại!");
                // 	}
                // 		console.log(result.plain());
                // }, function (e) {
                //     toastr.error("Có lỗi trong quá trình xử lý dữ liệu.");
                // });

                /* HIENVD: COMMENT updateConstruction IN ConstructionDAO
                            COMMENT and getDataUpdate IN constructionDAO
                */
                constructionService.updateconstruction(vm.constrObj).then(function (data) {
                    // close popup and refresh table list
                    if (data.error) {
                        toastr.error(data.error);
                        return;
                    }
                    toastr.success("Ghi lại thành công.");
                    CommonService.dismissPopup1(); //Huypp-đóng po pup
                    doSearchContruction();			//Huypp-đóng popup
                    $("#constructionGrid").data('kendoGrid').dataSource.read();
                    $("#constructionGrid").data('kendoGrid').refresh();
                }, function (e) {
                    toastr.error("Có lỗi trong quá trình xử lý dữ liệu.");
                });
            }else {
                if($("#location").is(":visible") && $("#highHTCT").is(":visible") && $("#capexHTCT").is(":visible")){
                    vm.constrObj.checkHTCT = true;
                }
            }
        }

        vm.createStartConstruction = createStartConstruction;
        function createStartConstruction() {
            constructionService.createStartConstruction().then(function (data) {
            }, function (e) {
                toastr.error("Có lỗi trong quá trình xử lý dữ liệu.");
            });

        }

        vm.cancelStationInput = cancelStationInput;
        function cancelStationInput(e) {

        }

        // vm.constructDetailValue = 1;

        // function onDataBound() {
        // var dateNow = new Date();
        // var gantt = this;
        // gantt.element.find(".k-task").each(function(e) {
        // var dataItem = gantt.dataSource.getByUid($(this).attr("data-uid"));
        // var dataItemDateEnd = new Date(dataItem.end);
        // var flag = dataItemDateEnd.getTime() < dateNow.getTime();

        // // colorize task per business requirements
        // if (flag === true && dataItem.percentComplete <1) {
        // this.style.backgroundColor = "#FF3333";
        // } else {
        // this.style.backgroundColor = "#87BCEB";
        // }
        // });
        // }

        // function checkValidateTab(value){
        // if(value==22){
        // if(vm.constrObj.handoverDate==null ||vm.constrObj.handoverDate=='' ){
        // toastr.warning('Chưa nhập thông tin bàn gia mặt bằng !')
        // return false;
        // }
        // }else if( value == 23 || value == 24){
        // if((vm.constrObj.startingDate==null ||vm.constrObj.startingDate=='')
        // || (vm.constrObj.excpectedCompleteDate==null
        // ||vm.constrObj.excpectedCompleteDate=='')){
        // toastr.warning('Chưa nhập thông tin khởi công !')
        // return false;
        // }
        // }else if( value == 26) {
        // if ($("#ganttTC").data("kendoGantt").dataSource._data.length == 0 ||
        // typeof($("#ganttTC").data("kendoGantt"))== undefined {
        // toastr.warning('Chưa nhập thông tin thi công !')
        // return false;
        // }
        // }else if( value == 27) {
        // if (vm.constrObj.status!=6) {
        // toastr.warning('Công trình chưa nghiệm thu !')
        // return false;
        // }
        // }
        // return true;
        // }

        vm.chooseTab = chooseTab;
   	 vm.hideAddButton = false;
        function chooseTab(value) {
       //Huypq-20191126-start comment
//     	if(vm.constrObj.checkHTCT != null && vm.constrObj.checkHTCT == true){
//     	  vm.hideAddButton = true;
//     	}else{
//     		 vm.hideAddButton = false;
//     	}
        //Huy-end
        	
            // if(checkValidateTab(value)){
            vm.constructDetailValue = value;
            //          hungnx 20180712 start\
            if (value == 1 || value == 11) {
                vm.constrObj.price = !!vm.constrObj.price || vm.constrObj.price == 0 ? htmlCommonService.addThousandComma(vm.constrObj.price.toString()) : null;
                vm.constrObj.amount = !!vm.constrObj.amount || vm.constrObj.amount == 0 ? htmlCommonService.addThousandComma(vm.constrObj.amount.toString()) : null;
//				vm.constrObj.totalAmountChest = !!vm.constrObj.totalAmountChest || vm.constrObj.totalAmountChest == 0 ? htmlCommonService.addThousandComma(vm.constrObj.totalAmountChest.toString()) : null;
//                vm.constrObj.priceChest = !!vm.constrObj.priceChest || vm.constrObj.priceChest == 0 ? htmlCommonService.addThousandComma(vm.constrObj.priceChest.toString()) : null;
//                vm.constrObj.totalAmountGate = !!vm.constrObj.totalAmountGate || vm.constrObj.totalAmountGate == 0 ? htmlCommonService.addThousandComma(vm.constrObj.totalAmountGate.toString()) : null;
//                vm.constrObj.priceGate = !!vm.constrObj.priceGate || vm.constrObj.priceGate == 0 ? htmlCommonService.addThousandComma(vm.constrObj.priceGate.toString()) : null;
            } else {
                vm.replaceFormatNumber();
            }
            //          hungnx 20180621 end
            if (value == 11 || value == 12 || value == 13 || value == 14) {
                vm.isForce = true;
                getHangMucData();
            } else {
                vm.isForce = false;
            }
            if (value == 13) {
                if (vm.covenantSearchCout == 0) {
                    return toastr.warning(gettextCatalog.getString("Công trình đang không thuộc hợp đồng đầu ra nào. Yêu cầu nhập hợp đồng đầu ra cho công trình."));
                }
            }
            if (value == 21 || value == 22 || value == 23 || value == 24 || value == 25 || value == 26 || value == 27 || value == 28 | value == 29) {
                vm.isForce1 = true;
            } else {
                vm.isForce1 = false;
            }
            if (value == 21) {
                if (Number(vm.constrObj.status || 0) < 2) {
                    vm.infoDisable = true;
                } else if (Number(vm.constrObj.status || 0) >= 2) {
                    vm.infoDisable = false;
                };
            }
            if (value == 22) {
                if (vm.constrObj.status == '2') {
                    vm.construction = true;
                }
                if (vm.constrObj.status == '3') {
                    vm.construction = false;
                }
            }
            if (value == 23) {
                if (Number(vm.constrObj.status || 0) < 3) {
                    vm.construction = true;
                } else if (Number(vm.constrObj.status || 0) >= 3) {
                    vm.construction = false;
                }
            }
            if (value == 24) {
                if (vm.constrObj.status == '2' || vm.constrObj.status == '1' || vm.constrObj.status == '0' || vm.constrObj.status == '7' || vm.constrObj.status == '8' || vm.constrObj.status == '6') {
                    vm.construction1 = true;
                } else {
                    vm.construction1 = false;
                }
            }
            if (value == 26) {
                if (Number(vm.constrObj.status || 0) >= 5 || (Number(vm.constrObj.status || 0) == 4 && Number(vm.constrObj.obstructed_state || 0) == 2)) {
                    vm.acceptance = false;
                }
                else if (Number(vm.constrObj.status || 0) < 5 || Number(vm.constrObj.status || 0) < 4) {
                    vm.acceptance = true;
                } else if (Number(vm.constrObj.status || 0) == 7) {
                    vm.checkStatus = true;
                }
            }
            if (value == 11) {
                if (Number(vm.constrObj.status || 0) >= 5) {
                    vm.infomation = true;
                }
                else if (Number(vm.constrObj.status || 0) < 5) {
                    vm.infomation = false;
                }
            }

            if (Number(vm.constrObj.status || 0) > 5) {
                vm.allowAction = false;
                vm.construction = true;
                vm.handover = true;
                vm.infoDisable = true;
                vm.generalInfo = true;
                vm.Categories = true;
                vm.disablePxk = true;
                vm.infomation = true;
            }
            else if (Number(vm.constrObj.status || 0) <= 5) {
                vm.allowAction = true;
                vm.construction = false;
                vm.handover = false;
                vm.infoDisable = false;
                vm.generalInfo = false;
                vm.Categories = false;
                vm.disablePxk = false;
                vm.infomation = false;
            }
            else if (Number(vm.constrObj.status || 0) > 6) {
                vm.checkStatus = true;
            }


            if (value == 31 || value == 32) {
                vm.isForce2 = true;
            } else {
                vm.isForce2 = false;
            }
            if (value == 23) {
                // grantt
                vm.ganttOptions = {
                    dataSource: new kendo.data.GanttDataSource({
                        batch: false,
                        transport: {
                            read: {
                                url: Constant.BASE_SERVICE_URL + "constructionTaskService/constructionTask/getDataConstructionGrantt",
                                contentType: "application/json; charset=utf-8",
                                type: "POST"
                            },
                            update: {
                                url: Constant.BASE_SERVICE_URL + "constructionTaskService/constructionTask/updateCompletePercent",
                                contentType: "application/json; charset=utf-8",
                                type: "POST"
                            },
                            destroy: {
                                url: Constant.BASE_SERVICE_URL + "constructionTaskService/constructionTask/deleteGrantt",
                                contentType: "application/json; charset=utf-8",
                                type: "POST"
                            },
                            create: {
                                url: Constant.BASE_SERVICE_URL + "constructionTaskService/constructionTask/createTask",
                                contentType: "application/json; charset=utf-8",
                                type: "POST"
                            },
                            parameterMap: function (options, operation) {
                                if (operation !== "read") {
                                    // var objTask =
                                    // {id:options.id,parentID:options.parentID,orderID:options.orderID,title:options.title,start:options.start,end:options.end,percentComplete:options.percentComplete,summary:options.summary,expanded:options.expanded,levelId:options.levelId};
                                    // //var abc = {models:
                                    // kendo.stringify(options.models ||
                                    // [options])};
                                    // lstABC.push(objTask);
                                    var obj = { models: kendo.stringify(options.models || [options]) };
                                    var strObj = {};
                                    strObj.id = options.id;
                                    strObj.parentID = options.parentID;
                                    strObj.orderID = options.orderID;
                                    strObj.title = options.title;
                                    strObj.start = options.start;
                                    strObj.end = options.end;
                                    strObj.percentComplete = options.percentComplete;
                                    strObj.summary = options.summary;
                                    strObj.expanded = options.expanded;
                                    strObj.levelId = options.levelId;
                                    //                                  hoanm1_20180612_start
                                    strObj.type = options.type;
                                    strObj.constructionId = options.constructionId;
                                    strObj.quantity = options.quantity;
                                    strObj.workItemId = options.workItemId;
                                    strObj.taskOrder = options.taskOrder;
                                    //                                    hoanm1_20180612_end
                                    return JSON.stringify(strObj);
                                } else if (operation === "read") {
                                    var strObj = {};
                                    strObj.id = vm.constrObj.constructionId;
                                    strObj.keySearch = vm.granttSearch.keyword;
                                    strObj.status = $scope.status;
                                    strObj.complete_state = $scope.complete_state;
                                    return JSON.stringify(strObj);
                                }
                            }
                        },
                        schema: {
                            model: {
                                id: "id",
                                fields: {
                                    id: { from: "id", type: "number" },
                                    orderId: { from: "orderID", type: "number", validation: { required: true } },
                                    parentId: { from: "parentID", type: "number", defaultValue: null, validation: { required: true } },
                                    start: { from: "start", type: "date" },
                                    end: { from: "end", type: "date" },
                                    title: { from: "title", defaultValue: "", type: "string" },
                                    percentComplete: { from: "percentComplete", type: "number" },
                                    summary: { from: "summary", type: "boolean" },
                                    expanded: { from: "expanded", type: "boolean", defaultValue: true },
                                    checkProgress: { from: "checkProgress", hidden: "true", type: "number" },
                                    performerName: { from: "fullname", type: "string" }
                                    //                                  hoanm1_20180612_start
                                    , type: { from: "type", hidden: "true", type: "string" }
                                    , constructionId: { from: "constructionId", hidden: "true", type: "number" }
                                    , quantity: { from: "quantity", hidden: "true", type: "number" }
                                    , workItemId: { from: "workItemId", hidden: "true", type: "number" }
                                    , taskOrder: { from: "taskOrder", hidden: "true", type: "string" }
                                    //                                    hoanm1_20180612_end
                                }
                            },
                            data: function (response) {
                                return response;
                            }
                        }
                    }),
                    views: [
                        "day",
                        {
                            type: "week",
                            weekHeaderTemplate: "#=kendo.toString(start, 'ddd dd/MM')# - #=kendo.toString(kendo.date.addDays(end, -1), 'ddd dd/MM')#",
                            dayHeaderTemplate: kendo.template("#=kendo.toString(start, 'ddd dd/MM')#"),
                            selected: true
                        },
                        { type: "month", weekHeaderTemplate: "#=kendo.toString(start, 'ddd dd/MM')# - #=kendo.toString(kendo.date.addDays(end, -1), 'ddd dd/MM')#" }
                    ],
                    columns: [
                        { field: "id", title: "STT", width: 40 },
                        { field: "title", title: "Tên CV", editable: false, width: 200 },
                        { field: "percentComplete ", title: "Tiến độ (%)", editable: false, width: 60, format: "{0:n0}" },
                        { field: "performerName", title: "Người TH", editable: false, width: 140 },
                        { field: "start", title: "Dự kiến khởi công", format: "{0:dd/MM/yyyy}", width: 80 },
                        { field: "end", title: "Dự kiến hoàn thành", format: "{0:dd/MM/yyyy}", width: 80 }
                    ],
                    height: 900,
                    showWorkHours: false,
                    showWorkDays: false,
                    snap: false,
                    toolbar: [],
                    rowHeight: 35,
                    tooltip: {
                        visible: true,
                        template: $("#myTemplate").html()
                    },
                    taskTemplate: $("#template1").html(),
                    edit: onEdit,
                    editable: {
                        template: $("#editor1").html()
                    },
                    change: function (e) {
                        var gantt = e.sender;
                        var selection = gantt.select();

                        if (selection.length) {
                            vm.curentObj = gantt.dataItem(selection);
                        }
                    },
                    dataBound: onDataBound,
                    save: onSaveTask
                };
                getCountConstruction();
                // vm.isForce2 = true;
            } else {
                // vm.isForce2 = false;

            }

        }
        // }
        vm.viewGrantAll = function () {
            $scope.viewGrantAll = true;
            $scope.complete_state = null;
            $scope.status = null;
            vm.ganttOptions.dataSource.read();
            getCountConstruction();
        }

        vm.viewGrantUnfulfilled = function () {
            $scope.viewGrantUnfulfilled = true;
            $scope.status = 1;
            $scope.complete_state = null;

            vm.ganttOptions.dataSource.read();
            getCountConstruction();
        }

        vm.viewGrantProgress = function () {
            $scope.viewGrantProgress = true;
            $scope.complete_state = 2;
            $scope.status = null;

            vm.ganttOptions.dataSource.read();
            getCountConstruction();
        }

        vm.viewGrantStop = function () {
            $scope.viewGrantStop = true;
            $scope.status = 3;
            $scope.complete_state = null;

            vm.ganttOptions.dataSource.read();
            getCountConstruction();
        };
        function getCountConstruction() {
            var obj = {};
            obj.id = vm.constrObj.constructionId;
            return Restangular.all("constructionTaskService/construction/getCountConstructionForTc").post(obj).then(function (d) {
                $scope.taskAll = d.taskAll;
                $scope.taskUnfulfilled = d.taskUnfulfilled;
                $scope.taskSlow = d.taskSlow;
                $scope.taskPause = d.taskPause;

            }).catch(function (e) {
                return;
            });
        }
        function onSaveTask(e) {
            constructionService.getconstructionStatus(vm.constrObj.constructionId).then(
                function (d) {
                    vm.constrObj.status = Number(d);
                    toastr.error("Có lỗi xảy ra!")
                }, function (errResponse) {
                    toastr.success("Cập nhập thành công!")
                });
        }
        function onEdit(e) {
            $("#numerictextbox").kendoNumericTextBox({
                format: "##.## \\\%"
            });
            $(".k-gantt-delete").text('Xóa');
            $(".k-gantt-update").text('Lưu lại');
            $(".k-gantt-cancel").text('Hủy bỏ');
            $(".k-window-title").text('Cập nhật tiến độ');

            if (Number(vm.constrObj.status || 0) >= 5) {
                $('.k-gantt-update').css({ "pointer-events": "none", "color": "#AAA !important;", "background": "#F5F5F5;" });
                $('.k-gantt-delete').css({ "pointer-events": "none", "color": "#AAA !important;", "background": "#F5F5F5;" });
            }
            else if (Number(vm.constrObj.status || 0) < 5) {
                $('.k-gantt-update').css('pointer-events', "unset");
                $('.k-gantt-delete').css('pointer-events', "unset");
            }
        }

        function onDataBound(e) {


            $("[data-title='Tiến độ (%)']").css("white-space", "normal");
            $("[data-title='Tiến độ (%)']").css("text-align", "center");

            var ganttList = e.sender.list;
            var dataItems = ganttList.dataSource.view();
            var count = 0;
            for (var j = 0; j < dataItems.length; j++) {
                // $('#'+table).find('tr#'+5+count).find('td:eq(0)').html(count);

                var dataItem = dataItems[j];
                var row = $("[data-uid='" + dataItem.uid + "']");
                row.find("td").eq(3).unbind('dblclick');
                row.find("td").eq(3).dblclick(function (e) {
                    //                	chinhpxn20180720_start
                    if (vm.curentObj.workItemStatus == '3') {
                        toastr.error("Hạng mục đã hoàn thành không được phép chuyển người!")
                        return false;
                    } else {
                        vm.openPerformerPopup();
                        return false;
                    }
                    //                	chinhpxn20180720_end
                });
                row.find("td").eq(0).addClass("text-center");
                row.find("td").eq(2).addClass("text-center");

                //chinhpxn20180706_start
                if (dataItem.start == null) {
                    var start = row.find("td").eq(4);
                    start.html("");
                }
                if (dataItem.end == null) {
                    var end = row.find("td").eq(5);
                    end.html("");
                }
                //chinhpxn20180706_end

                //                chinhpxn20180704_start
                if (dataItem.levelId == 2) {
                    count = count + 1;
                    var stt = row.find("td").eq(0);
                    stt.html(count);
                    var nguoiThucHien = row.find("td").eq(3);
                    nguoiThucHien.html("");
                } else {
                    var stt = row.find("td").eq(0);
                    stt.html("");
                }
                //                chinhpxn20180704_end

                var span = row.find("td").eq(1).find("span").eq(dataItem.levelId - 2);
                if (dataItem.levelId == 1) {
                    if (dataItem.expanded) {
                        span.addClass("gantt-expanded-lv-1");
                    } else {
                        span.addClass("gantt-close-lv-1");
                    }
                } else if (dataItem.levelId == 2 || dataItem.levelId == 3) {
                    if (dataItem.expanded) {
                        span.addClass("gantt-expaned-lv2");
                    } else {
                        span.addClass("gantt-close-lv2");
                    }
                } else if (dataItem.levelId == 4) {
                    if (dataItem.checkProgress == 2) {
                        span.removeClass("k-i-none");
                        span.addClass("gantt-dot-red-lv4");
                    } else {
                        span.removeClass("k-i-none");
                        span.addClass("gantt-dot-lv4");
                    }
                }

            }
            var dateNow = new Date();
            var gantt = this;
            // thang fix cong tong
            //$scope.taskAll = 0;
            //$scope.taskSlow = 0;
            //$scope.taskPause = 0;
            //$scope.taskUnfulfilled=0;


            // gantt.element.find(".k-alt").each(function(e) {

            // var dataItem = gantt.dataSource.getByUid($(this).attr("data-uid"));
            // if(dataItem==null)
            // dataItem={}
            // var dataItemDateEnd = new Date(dataItem.end);
            // var flag = dataItemDateEnd.getTime() < dateNow.getTime();

            // // colorize task per business requirements
            // var levelId = $(this).attr("data-level");
            // var span = this.find("td").eq(1).find("span").eq(2);

            // span.css("background-color", "yellow");
            // if (levelId ==3 && flag === true && dataItem.percentComplete <1) {
            // this.style.backgroundColor = "#FF3333";
            // }

            // /* else {
            // this.style.backgroundColor = "#87BCEB";
            // }*/
            // });
            gantt.element.find(".k-task").each(function (e) {

                var dataItem = gantt.dataSource.getByUid($(this).attr("data-uid"));
                /*
                 * var dataItemDateEnd = new Date(dataItem.end); var flag =
                 * dataItemDateEnd.getTime() < dateNow.getTime();
                 */
                if (dataItem.levelId == 4) {
                    //$scope.taskAll++;
                    if (dataItem.status === 3) {
                        //$scope.taskPause++;
                    }
                    if (dataItem.status === 1) {
                        //$scope.taskUnfulfilled++;
                    }
                    // colorize task per business requirements
                    /*
					 * if (flag === true && dataItem.percentComplete <1) {
					 * this.style.backgroundColor = "#FF3333";
					 * $scope.taskSlow++; } else { //this.style.backgroundColor =
					 * "#87BCEB"; }
					 */
                    if (dataItem.checkProgress == 1 && dataItem.levelId == 4) {
                        //$scope.taskSlow++;
                        this.style.backgroundColor = "#FF2121";

                    }
                }
            });


            // change icon
            // var ganttList = e.sender.list;
            // var dataItems = ganttList.dataSource.view();
            // for (var j = 0; j < dataItems.length; j++) {
            // var dataItem = dataItems[j];
            // var row = $("[data-uid='" + dataItem.uid + "']");
            // var span = row.find("td").eq(1).find("span").last();
            // span
            // .parent()
            // .find("img,strong")
            // .remove();
            // span.prepend("<img
            // src='http://localhost:8084/qlhc-web/assets/global/grantt/iconOpen.png'
            // /> <strong>" + Math.ceil(dataItem.percentComplete * 100) +
            // "</strong>");
            // }

        }


        vm.cancelImport = cancelImport;
        function cancelImport() {
            CommonService.dismissPopup1();
        }


        function initConstructionDetailRadioList() {
            vm.option1Show = false;
            vm.option2Show = false;
            vm.option3Show = false;
            vm.option4Show = false;
            vm.option5Show = false;
            vm.option6Show = false;
            vm.option7Show = false;
            vm.option8Show = false;
        }

        vm.lineTypeDataList = [];
        vm.gponTypeDataList = [];
        vm.otherTypeDataList = [];
        function initDataConstructionType() {
            constructionService.getAppParamByType("CONSTRUCTION_LINE_TYPE").then(function (data) {
                vm.lineTypeDataList = data;
            }, function (e) {
                toastr("Có lỗi trong quá trình lấy dữ liệu.");
            });
            constructionService.getAppParamByType("CONSTRUCTION_GPON_TYPE").then(function (data) {
                vm.gponTypeDataList = data;
            }, function (e) {
                toastr("Có lỗi trong quá trình lấy dữ liệu.");
            });
            constructionService.getAppParamByType("CONSTRUCTION_OTHER_TYPE").then(function (data) {
                vm.otherTypeDataList = data;
            }, function (e) {
                toastr("Có lỗi trong quá trình lấy dữ liệu.");
            });
            constructionService.getAppParamByType("REGION").then(function (data) {
                vm.regionDataList = data;
            }, function (e) {
                toastr("Có lỗi trong quá trình lấy dữ liệu.");
            });
            constructionService.getAppParamByType("MONEY_TYPE_MILLION").then(function (data) {
                vm.moneyType = data;
            }, function (e) {
                toastr("Có lỗi trong quá trình lấy dữ liệu.");
            });
            
        }

        function getHangMucData() {
        	
        }

        vm.workItemSearch = {};



        // Quangtao - end
        initCatConstructionType();
        function initCatConstructionType() {
            constructionService.getconstructionType().then(function (data) {
                vm.catConstructionTypeDataList = data;
            });
        }


        vm.validatorOptions = kendoConfig.get('validatorOptions');
        vm.formatAction = function (dataItem) {
            var template =
                '<div class="text-center #=constructionId#"">'
            template += '<button type="button"' +
                'class="btn btn-default padding-button box-shadow  #=constructionId#"' +
                'disble="" ng-click=vm.edit(#=constructionId#)>' +
                '<div class="action-button edit" uib-tooltip="Sửa" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
                '</button>' +
                '<button type="button"' +
                'class="btn btn-default padding-button box-shadow #=constructionId#"' +
                'ng-click=vm.send(#=constructionId#)>' +
                '<div class="action-button export" uib-tooltip="Gửi tài chính" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
                '</button>' +
                '<button type="button"' +
                'class="btn btn-default padding-button box-shadow #=constructionId#"' +
                'ng-click=vm.remove(#=constructionId#)>' +
                '<div class="action-button del" uib-tooltip="Xóa" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
                '</button>'
                +
                '<button type="button" class="btn btn-default padding-button box-shadow #=constructionId#"' +
                'ng-click=vm.cancelUpgradeLta(#=constructionId#)>' +
                '<div class="action-button cancelUpgrade" uib-tooltip="Hủy nâng cấp" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
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
        	kendo.ui.progress($("#constructionGrid"), true);
            vm.gridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: true,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: [
                    {
                        name: "actions",
                        template: '<div class=" pull-left ">' +
                            '<button class="btn btn-qlk padding-search-right addQLK" ng-hide="vm.checkRoleMapSolar" ' +
                            'ng-click="vm.add()" uib-tooltip="Tạo mới" translate>Tạo mới</button>' +
                            '</div>'
                            +
                            '<button class="btn btn-qlk padding-search-right TkQLK " ng-hide="vm.checkRoleMapSolar" ' +
                            'ng-click="vm.importConstruction()" uib-tooltip="Import công trình" translate>Import</button>'
                            +
                            '<button style="width: 146px !important;" class="btn btn-qlk padding-search-right TkQLK " ng-hide="vm.checkRoleMapSolar" ' +
                            'ng-click="vm.importWorkItem()" uib-tooltip="Gán hạng mục" translate>Gán hạng mục</button>'
                            +
                            '<button style="width: 155px !important;" class="btn btn-qlk padding-search-right TkQLK " ng-hide="vm.checkRoleMapSolar"' + 
                            'ng-click="vm.importConstructionGPXD()" uib-tooltip="Import cần GPXD" translate>Import cần GPXD</button>' //Huypq-button import cần GPXD
                            +
                            '<button style="width: 225px" class="btn btn-qlk padding-search-right TkQLK " ng-show="vm.checkRoleMapSolar"' +
                           'ng-click="vm.importSystemSolar()" uib-tooltip="Import gán mã hệ thống ĐMT" translate>Import gán mã hệ thống ĐMT</button>'
                            +
                            // '<button style="width: 220px" class="btn btn-qlk padding-search-right TkQLK "' +
                            // 'ng-click="vm.importGC()" uib-tooltip="Import công trình" translate>&nbsp;Import CTGC/Nhà máy nổ</button>'
                            // +
                            '<div class="btn-group pull-right margin_top_button margin10">' +
                            '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                            '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                            '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                            '<label ng-repeat="column in vm.constructionGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                            '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}' +
                            '</label>' +
                            '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $timeout( function(){ vm.countCons = response.total; } );
                            return response.total;
                        },
                        data: function (response) {
                        	kendo.ui.progress($("#constructionGrid"), false);
                        	return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "constructionService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            // vm.constructionSearch.employeeId =
                            // Constant.user.srvUser.catEmployeeId;
                            vm.constructionSearch.page = options.page
                            vm.constructionSearch.pageSize = options.pageSize

                            return JSON.stringify(vm.constructionSearch)

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
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Mã công trình",
                        field: 'code',
                        width: '20%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tên công trình",
                        field: 'name',
                        width: '30%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Mã hệ thống ĐMT",
                        field: 'systemOriginalCode',
                        width: '25%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        hidden: !vm.checkRoleMapSolar,
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Loại công trình",
                        field: 'catContructionTypeName',
                        width: '15%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    // //HIENVD: START 2/7/2019
                    // {
                    //     title: "Ngày bàn giao mặt bằng",
                    //     field: 'handoverDate',
                    //     width: '10%',
                    //     hidden:true,
                    //     headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    //     attributes: {
                    //         style: "text-align:left;"
                    //     }
                    // },
                    // {
                    //     title: "Ngày khởi công",
                    //     field: 'startingDate',
                    //     width: '10%',
                    //     hidden:true,
                    //     headerAttributes: { style: "text-align:center;font-weight: bold;" },
                    //     attributes: {
                    //         style: "text-align:left;"
                    //     }
                    // },
                    //HIENVD: END 2/7/2019
                    {
                        title: "Mã trạm",
                        field: 'catStationCode',
                        width: '10%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
//                  Huypq-17052021-start
                    {
                        title: "Ngày tạo",
                        field: 'createdDate',
                        width: '10%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Người tạo",
                        field: 'createdUserName',
                        width: '10%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
//                  Huypq-17052021-end
                    {
                        title: "Trạng thái",
                        field: 'status',
                        width: '10%',
                        template: function (dataItem) {
                            if (dataItem.status == 1) {
                                return "<span name='status' font-weight: bold;'>Chờ bàn giao mặt bằng</span>"
                            } else if (dataItem.status == 2) {
                                return "<span name='status' font-weight: bold;'>Chờ khởi công</span>"
                            } else if (dataItem.status == 3) {
                                return "<span name='status' font-weight: bold;'>Đang thực hiện</span>"
                            } else if (dataItem.status == 4) {
                                return "<span name='status' font-weight: bold;'>Đã tạm dừng</span>"
                            } else if (dataItem.status == 5) {
                                return "<span name='status' font-weight: bold;'>Đã hoàn thành</span>"
                            } else if (dataItem.status == 6) {
                                return "<span name='status' font-weight: bold;'>Đã nghiệm thu</span>"
                            } else if (dataItem.status == 7) {
                                return "<span name='status' font-weight: bold;'>Đã hoàn công</span>"
                            } else if (dataItem.status == 8) {
                                return "<span name='status' font-weight: bold;'>Đã quyết toán</span>"
                            } else if (dataItem.status == 9) {
                                return "<span name='status' font-weight: bold;'>Đã phát sóng trạm</span>"
                            } else if (dataItem.status == 10) {
                                return "<span name='status' font-weight: bold;'>Đang dở dang</span>"
                            } else if (dataItem.status == 11) {
                                return "<span name='status' font-weight: bold;'>Chờ duyệt ĐBHT</span>"
                            } else if (dataItem.status == 0) {
                                return "<span name='status' font-weight: bold;'>Đã hủy</span>"
                            }else if (dataItem.status == -5) {
                                return "<span name='status' font-weight: bold;'>Chờ TTHT duyệt hoàn thành</span>"
                            }
                        },
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Thao tác",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        hidden: vm.checkRoleMapSolar,
                        template: dataItem =>
                            '<div class="text-center">' +
                            '<button style=" border: none; background-color: white;" id="updateId" ng-disabled="dataItem.status==0" ng-click="vm.exportConstruction(dataItem)" class=" icon_table "' +
                            '   uib-tooltip="Xuất hồ sơ công trình" translate>' +
                            '<i class="fa fa-file-word-o" style="color:#337ab7"    aria-hidden="true"></i>' +
                            '</button>'
                            + '<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.edit(dataItem)" class=" icon_table "' +
                            '   uib-tooltip="Xem chi tiết" translate>' +
                            '<i class="fa fa-list-alt" style="color:#e0d014"     aria-hidden="true"></i>' +
                            '</button>'
                            // +'<button style=" border: none; background-color:
                            // white;" id="updateId"
                            // ng-hide="dataItem.status==0"
                            // ng-click="vm.copy(dataItem)" class=" icon_table
                            // "'+
                            // ' uib-tooltip="Sao chép" translate>'+
                            // '<i class="fa fa-files-o"style="color: #337ab7;"
                            // ng-hide="dataItem.status==0"
                            // aria-hidden="true"></i>'
                            // + '</button>'
                            + '<button style=" border: none; background-color: white;" id=""' +
                            'class=" icon_table" ng-click="vm.approve(dataItem)" ng-show="vm.checkRoleApprove && dataItem.checkConstruction != null && dataItem.status == -5 && dataItem.catContructionTypeId == 8" uib-tooltip="Phê duyệt" translate' + '>' +
                            '<i class="fa fa-check" style="color: #00FF00;"   aria-hidden="true"></i>' +
                            '</button>'
                            + '<button style=" border: none; background-color: white;" id=""' +
                            'class=" icon_table" ng-click="vm.remove(dataItem)" ng-disabled="!(dataItem.status==1 || dataItem.status==0)" ng-show="dataItem.status!=0 && dataItem.status<3" uib-tooltip="Xóa" translate' + '>' +
                            '<i class="fa fa-trash" style="color: #337ab7;"   aria-hidden="true"></i>' +
                            '</button>'
                            + '</div>',
                        width: '15%'
                    }
                ]
            });
        }
        
        function nonEditor(container, options) {
            container.text(options.model[options.field]);
        }
        //chinhpxn 20180607 start

        vm.importWorkItem = importWorkItem;
        function importWorkItem() {
            //UNIkom gán hang mục công trình
        //Huypq-01082020-comment code check role gán wi
//            return Restangular.all("constructionService/checkAddWorkItem").post({}).then(function (d) {
//                if (d.error) {
//                    toastr.error(d.error);
//                    return;
//                }
//            }).catch(function (e) {
                vm.fileImportData = false;
                var teamplateUrl = "coms/construction/importWorkItem.html";
                var title = "Import hạng mục";
                var windowId = "IMPORT_WORK_ITEM";
                CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '275', null);
//            });


        }

        vm.importConstruction = importConstruction;
        function importConstruction() {
            vm.fileImportData = false;
            var teamplateUrl = "coms/construction/importConstruction.html";
            var title = "Import công trình";
            var windowId = "IMPORT_CONSTRUCTION";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '275', null);
        }

        //hienvd: START 22-7-2019
        vm.importGC = importGC;
        function importGC() {
            vm.fileImportData = false;
            var teamplateUrl = "coms/construction/importConstructionReinforced.html";
            var title = "Import công trình gia cố/Nhà máy nổ";
            var windowId = "IMPORT_CONSTRUCTION_GCNMN";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '275', null);
        }
        //hienvd: END 22-7-2019 END

        //-----Huypq-start-popup import-----
        vm.importConstructionGPXD = importConstructionGPXD;
        function importConstructionGPXD() {
            vm.fileImportDataGPXD = false;
            var teamplateUrl = "coms/construction/importConstructionGPXD.html";
            var title = "Import công trình";
            var windowId = "IMPORT_CONSTRUCTION_GPXD";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '275', null);
        }
        //--------Huypq-end--------
        vm.getWorkItemExcelTemplate = function () {
            var fileName = "FileBieuMau_HangMuc_CongTrinh";
            CommonService.downloadTemplate(fileName).then(function (d) {
                var data = d.plain();
                //			window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                window.location = Constant.BASE_SERVICE_URL + "constructionService/downloadFile?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
                return;
            });

        };

        //hienvd: START 23-7-2019 DOWNLOAD FILE BIEU MAU CONG TRINH GIA CO/NHA MAY NO
        vm.getConstructionGCNMNExcelTemplate = function () {
            var fileName = "FileBieuMau_CTGiaCoNhaMayNo";
            CommonService.downloadTemplate(fileName).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "constructionService/downloadFileForConstructionGiaCoNhaMayNo?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
                return;
            });
        }
        //hienvd: END

        vm.getConstructionExcelTemplate = function () {
            var fileName = "FileBieuMau_CongTrinh";
            CommonService.downloadTemplate(fileName).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "constructionService/downloadFileForConstruction?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
                return;
            });
        }
        //---------HuyPQ-20181010-start---------
        //--Tải biểu mẫu
        vm.getConstructionGPXDExcelTemplate = function () {
            var fileName = "FileBieuMau_ConstructionGPXD";
            CommonService.downloadTemplate(fileName).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "constructionService/downloadFileForConstructionGPXD?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
                return;
            });
        }
        //
        function callbackDoSearch() {
        	vm.constructionGrid.dataSource.page(1);
        	
        }
        //--Đọc file import
        vm.readFileConstruction = function() {
        	if (!vm.fileImportDataGPXD) {
                callbackDoSearch();
                return;
            }
        	else if ((vm.fileImportDataGPXD.name.split('.').pop() != 'xls' && vm.fileImportDataGPXD.name.split('.').pop() != 'xlsx')) {
        		toastr.warning("Sai định dạng file");
                callbackDoSearch();
                return;
        	}
            var formData = new FormData();
            formData.append('multipartFile', vm.fileImportDataGPXD);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "constructionService/readFileConstruction",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if (data.constructionCodeLst.length === 0) {
                        toastr.warning("File import không có dữ liệu");
                        $('#testSubmitGPXD').removeClass('loadersmall');
                        return;
                    } else {
                    	if (data.constructionCodeLst.length > 0 ) {
                    		
                    		vm.constructionSearch.constructionCodeLst = data.constructionCodeLst;
                    	} 
                    }
                    callbackDoSearch();
                    console.log(vm.constructionSearch.constructionCodeLst);
                }, 
                error: function(data) {
        			if (!!data.error) {
        				 toastr.error("File import phải có định dạng xlsx !");
        			}
        		}
                
            });
        }
        //
        //--Hàm update
        function getConstructionCodeList() {
            return Restangular.all("constructionService/updateIsBuildingPermit").post({
            		constructionCodeLst : vm.constructionSearch.constructionCodeLst
            }).then(function (d) {
                
            }).catch(function (e) {
                return;
            });
        }
        //
        //--Button ghi lại file import
        vm.submitConstructionGPXD = submitConstructionGPXD;
        function submitConstructionGPXD(data) {
        	vm.readFileConstruction();
            $('#testSubmitGPXD').addClass('loadersmall');
            vm.disableSubmitGPXD = true;
            if (!vm.fileImportDataGPXD) {
                $('#testSubmitGPXD').removeClass('loadersmall');
                vm.disableSubmitGPXD = false;
                toastr.warning("Bạn chưa chọn file để import");
                return;
            }
            if ($('.k-invalid-msg').is(':visible')) {
                $('#testSubmitGPXD').removeClass('loadersmall');
                vm.disableSubmitGPXD = false;
                return;
            }
            var formData = new FormData();
            formData.append('multipartFile', vm.fileImportDataGPXD);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "constructionService/importConstructionGPXD?folder=temp",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                	if (!!data.error) {
                        $('#testSubmitGPXD').removeClass('loadersmall');
                        vm.disableSubmitGPXD = false;
                        toastr.error(data.error);
                    }
                    else if (data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length > 0) {
                        vm.lstErrImport = data[data.length - 1].errorList;
                        vm.objectErr = data[data.length - 1];
                        var templateUrl = "wms/createImportRequirementManagement/importResultPopUp.html";
                        var title = "Kết quả Import";
                        var windowId = "ERR_IMPORT";
                        $('#testSubmitGPXD').removeClass('loadersmall');
                        vm.disableSubmitGPXD = false;
                        CommonService.populatePopupCreate(templateUrl, title, vm.lstErrImport, vm, windowId, false, '80%', '420px');
                        fillDataImportErrTable(vm.lstErrImport);

                    }
                    else if (vm.constructionSearch.constructionCodeLst.length===0) {
                        $('#testSubmitGPXD').removeClass('loadersmall');
                        vm.disableSubmitGPXD = false;
                        toastr.warning("File import không có nội dung");
                    } 
                    else {
                        	$('#testSubmitGPXD').removeClass('loadersmall');
                            callbackDoSearch();
                            vm.disableSubmitGPXD = false;
                            getConstructionCodeList();
                            toastr.success("Import thành công");
                            CommonService.dismissPopup1();
                            doSearchContruction();
                        
                    }
                    $scope.$apply();
                }
            });
        }
        //


        //--Sự kiện checkbox Cần GPXD trong popup sửa
        vm.handleCheck=handleCheck;
    	function handleCheck(dataItem){
    		
    		if(dataItem.selected){
    			vm.constrObj.isBuildingPermit=1;
    		} else {
    			vm.constrObj.isBuildingPermit=null;
            	$scope.listCheck.splice(i,1);
    		}
    	}
    	//
      //--------HuyPQ-20181010-end----------
        vm.cancelImport = cancelImport;
        function cancelImport() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }
        
        vm.disableSubmitGPXD = false;
        vm.disableSubmit = false;
        vm.fileImportData = false;
        vm.fileImportDataGPXD = false;
        vm.submit = submit;
        function submit(data) {
            $('#testSubmit').addClass('loadersmall');
            vm.disableSubmit = true;
            if (!vm.fileImportData) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Bạn chưa chọn file để import");
                return;
            }
            if ($('.k-invalid-msg').is(':visible')) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                return;
            }
            if (vm.fileImportData.name.split('.').pop() !== 'xls' && vm.fileImportData.name.split('.').pop() !== 'xlsx') {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Sai định dạng file");
                return;
            }
            var formData = new FormData();
            formData.append('multipartFile', vm.fileImportData);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "constructionService/importWorkItem?folder=temp",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if (data == 'NO_CONTENT') {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.warning("File import không có nội dung");
                    }
                    else if (!!data.error) {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.error(data.error);
                    }
                    else if (data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length > 0) {
                        vm.lstErrImport = data[data.length - 1].errorList;
                        vm.objectErr = data[data.length - 1];
                        var templateUrl = "wms/createImportRequirementManagement/importResultPopUp.html";
                        var title = "Kết quả Import";
                        var windowId = "ERR_IMPORT";
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        CommonService.populatePopupCreate(templateUrl, title, vm.lstErrImport, vm, windowId, false, '80%', '420px');
                        fillDataImportErrTable(vm.lstErrImport);

                    }
                    else {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.success("Import thành công");
                    }
                    $scope.$apply();
                }
            });
        }

        vm.submitConstruction = submitConstruction;
        function submitConstruction(data) {
            $('#testSubmit').addClass('loadersmall');
            vm.disableSubmit = true;
            if (!vm.fileImportData) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Bạn chưa chọn file để import");
                return;
            }
            if ($('.k-invalid-msg').is(':visible')) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                return;
            }
            if (vm.fileImportData.name.split('.').pop() !== 'xls' && vm.fileImportData.name.split('.').pop() !== 'xlsx') {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Sai định dạng file");
                return;
            }
            var formData = new FormData();
            formData.append('multipartFile', vm.fileImportData);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "constructionService/importConstruction?folder=temp",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if (data == 'NO_CONTENT') {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.warning("File import không có nội dung");
                    }
                    else if (!!data.error) {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.error(data.error);
                    }
                    else if (data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length > 0) {
                        vm.lstErrImport = data[data.length - 1].errorList;
                        vm.objectErr = data[data.length - 1];
                        var templateUrl = "wms/createImportRequirementManagement/importResultPopUp.html";
                        var title = "Kết quả Import";
                        var windowId = "ERR_IMPORT";
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        CommonService.populatePopupCreate(templateUrl, title, vm.lstErrImport, vm, windowId, false, '80%', '420px');
                        fillDataImportErrTable(vm.lstErrImport);

                    }
                    else {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.success("Import thành công");
                        doSearchContruction();
                    }
                    $scope.$apply();
                }
            });
        }

        //hienvd: START 23-7-2019 SUBMIT IMPORT
        vm.submitConstructionGiaCong = submitConstructionGiaCong;
        function submitConstructionGiaCong(data) {
            $('#testSubmit').addClass('loadersmall');
            vm.disableSubmit = true;
            if (!vm.fileImportData) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Bạn chưa chọn file để import");
                return;
            }
            if ($('.k-invalid-msg').is(':visible')) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                return;
            }
            if (vm.fileImportData.name.split('.').pop() !== 'xls' && vm.fileImportData.name.split('.').pop() !== 'xlsx') {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Sai định dạng file");
                return;
            }
            var formData = new FormData();
            formData.append('multipartFile', vm.fileImportData);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "constructionService/importConstructionGiaCong?folder=temp",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if (data == 'NO_CONTENT') {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.warning("File import không có nội dung");
                    }
                    else if (!!data.error) {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.error(data.error);
                    }
                    else if (data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length > 0) {
                        vm.lstErrImport = data[data.length - 1].errorList;
                        vm.objectErr = data[data.length - 1];
                        var templateUrl = "wms/createImportRequirementManagement/importResultPopUp.html";
                        var title = "Kết quả Import";
                        var windowId = "ERR_IMPORT";
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        CommonService.populatePopupCreate(templateUrl, title, vm.lstErrImport, vm, windowId, false, '80%', '420px');
                        fillDataImportErrTable(vm.lstErrImport);

                    }
                    else {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.success("Import thành công");
                        doSearchContruction();
                    }
                    $scope.$apply();
                }
            });
        }
        //hienvd: END 23-7-2019

        function fillDataImportErrTable(data) {
            vm.importGoodResultGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                resizable: true,
                dataSource: data,
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords: gettextCatalog.getString("Không có kết quả hiển thị")
                },
                pageSize: 10,
                pageable: {
                    pageSize: 10,
                    refresh: false,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: "Không có kết quả hiển thị"
                    }
                },
                columns: [
                    {
                        title: "TT",
                        field: "stt",
                        template: dataItem => $("#importGoodResultGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
                        width: 70,
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:center;"
                        },
                    }
                    , {
                        title: "Dòng lỗi",
                        field: 'lineError',
                        width: 100,
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:center;"
                        }
                    }, {
                        title: "Cột lỗi",
                        field: 'columnError',
                        width: 100,
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:center;"
                        },
                    }, {
                        title: "Nội dung lỗi",
                        field: 'detailError',
                        width: 250,
                        headerAttributes: {
                            style: "text-align:center;"
                        },
                        attributes: {
                            style: "text-align:left;"
                        },
                    }
                ]
            });
        }

        vm.exportExcelErr = function () {
            constructionService.downloadErrorExcel(vm.objectErr).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
                return;
            });
        }

        vm.closeErrImportPopUp = closeErrImportPopUp;
        function closeErrImportPopUp() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

        //chinhpxn 20180607 end

        // start data grid hạng mục
        function fillDataTableCategory(data) {

            vm.constructionCategorygridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
                editable: false,
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();

                }
            })
        };

        // start data grid hạng mục
        function fillDataTableCategory(data) {

            vm.constructionCategorygridOptions = kendoConfig.getGridOptions({
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
                            'ng-click="vm.add()" uib-tooltip="Tạo mới" translate>Tạo mới</button>' +
                            '</div>'
                            +
                            '<div class="btn-group pull-right margin_top_button margin10">' +
                            '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                            '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                            '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                            '<label ng-repeat="column in vm.constructionGrid.columns| filter: vm.gridColumnShowHideFilter">' +
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
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "constructionService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            // vm.constructionSearch.employeeId =
                            // Constant.user.srvUser.catEmployeeId;
                            vm.constructionSearch.page = options.page
                            vm.constructionSearch.pageSize = options.pageSize

                            return JSON.stringify(vm.constructionSearch)

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
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Mã công trình",
                        field: 'code',
                        width: '20%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tên công trình",
                        field: 'name',
                        width: '35%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Loại công trình",
                        field: 'catContructionTypeName',
                        width: '10%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Mã trạm",
                        field: 'catStationCode',
                        width: '10%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Trạng thái",
                        field: 'status',
                        width: '10%',
                        template: function (dataItem) {
                            if (dataItem.status == 1) {
                                return "<span name='status' font-weight: bold;'>Chờ bàn giao mặt bằng</span>"
                            } else if (dataItem.status == 2) {
                                return "<span name='status' font-weight: bold;'>Chờ khởi công</span>"
                            } else if (dataItem.status == 3) {
                                return "<span name='status' font-weight: bold;'>Đang thực hiện</span>"
                            } else if (dataItem.status == 4) {
                                return "<span name='status' font-weight: bold;'>Đã tạm dừng</span>"
                            } else if (dataItem.status == 5) {
                                return "<span name='status' font-weight: bold;'>Đã hoàn thành</span>"
                            } else if (dataItem.status == 6) {
                                return "<span name='status' font-weight: bold;'>Đã nghiệm thu</span>"
                            } else if (dataItem.status == 7) {
                                return "<span name='status' font-weight: bold;'>Đã hoàn công</span>"
                            } else if (dataItem.status == 8) {
                                return "<span name='status' font-weight: bold;'>Đã quyết toán</span>"
                            }  else if (dataItem.status == 10) {
                                return "<span name='status' font-weight: bold;'>Đang dở dang</span>"
                            } else if (dataItem.status == 0) {
                                return "<span name='status' font-weight: bold;'>Đã hủy</span>"
                            }
                        },
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Thao tác",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        template: dataItem =>
                            '<div class="text-center">'
                            + '<button style=" border: none; background-color: white;" id="updateId" ng-hide="dataItem.status==0" ng-click="vm.edit(dataItem)" class=" icon_table "' +
                            '   uib-tooltip="Sửa" translate>' +
                            '<i class="fa fa-list-alt" style="color:#e0d014"  ng-hide="dataItem.status==0"   aria-hidden="true"></i>' +
                            '</button>'
                            + '<button style=" border: none; background-color: white;" id="updateId" ng-hide="dataItem.status==0" ng-click="vm.copy(dataItem)" class=" icon_table "' +
                            '   uib-tooltip="Sao chép" translate>' +
                            '<i class="fa fa-files-o"style="color: #337ab7;"  ng-hide="dataItem.status==0"   aria-hidden="true"></i>'
                            + '</button>'

                            + '<button style=" border: none; background-color: white;" id=""' +
                            'class=" icon_table" ng-click="vm.remove(dataItem)" ng-hide="dataItem.status==0"  uib-tooltip="Xóa" translate' + '>' +
                            '<i class="fa fa-trash" style="color: #337ab7;" ng-hide="dataItem.status==0"  aria-hidden="true"></i>' +
                            '</button>'
                            + '</div>',
                        width: '10%'
                    }
                ]
            });
        }
		vm.exportFileDetailGpon = function exportFileDetailGpon() {
			vm.workItemSearch.page = null;
			vm.workItemSearch.pageSize = null;
			var data = constructionService.doSearchGpon(vm.workItemSearch);
			console.log(data);
			constructionService.doSearchGpon(vm.workItemSearch).then(function(d){
				CommonService.exportFile(vm.workItemDetailGpon,d.data,vm.listRemove,vm.listConvert,"Danh sách chi tiết hạng mục");
			});
				
		}
//        // end data grid hạng mục
//        function initWorkItemTable() {
            vm.workItemDetailGponGridOptions = kendoConfig.getGridOptions({
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
                        template: 
                          	  '<div class="btn-group pull-right margin_top_button margin_right10">'+
                        '<i class="action-button excelQLK" tooltip-placement="left" uib-tooltip="Xuất file excel" ng-click="caller.exportFileDetailGpon()" aria-hidden="true"></i>' +
                        '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'+
                        '<label ng-repeat="column in vm.projectManagerGrid.columns| filter: vm.gridColumnShowHideFilter">'+
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'+
                        '</label>'+
                        '</div></div>'
                    }
                    ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            vm.countWorkItem = response.total;
                            vm.checkNumberHangMuc = false;
                            if (response.total > 0) {
                                vm.checkNumberHangMuc = true;
                            }
                            return response.total; // total is returned in
                            // the "total" field of
                            // the response
                        },
                        data: function (response) {
                        	  $("#countDetailItemGpon").text(""+response.total);
                            return response.data; // data is returned in
                            // the "data" field of
                            // the response
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "workItemService/doSearchGpon",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                        	vm.workItemSearch.workItemId = $scope.workItemIdGpon ;
                        	vm.workItemSearch.constructionId = $scope.constrIdGpon ;
                            vm.workItemSearch.page = options.page
                            vm.workItemSearch.pageSize = options.pageSize
                            return JSON.stringify(vm.workItemSearch)

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
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Tên hạng mục",
                        field: 'workItemName',
                        width: '20%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Loại hạng mục",
                        field: 'name',
                        width: '15%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tên công viêc",
                        width: '10%',
                        field: 'taskName',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                        

                    },
                    {
                        title: "Khối lượng thi công",
                        width: '15%',
                        field: 'amount',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                        format: "{0:n0}"
                        

                    },
                    {
                        title: "Đơn giá thi công (Triệu đồng)",
                        width: '15%',
                        field: 'price',
                        format: "{0:n3}",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                        

                    },
//         
                    {
                        title: "Thao tác",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: dataItem =>
                            '<div class="text-center">'
                            + '<button style=" border: none; background-color: white;"  id="updateId" ng-click="caller.editWorkItemDetailGpon(dataItem)" class=" icon_table "' +
                            '   uib-tooltip="Sửa" translate>' +
                            '<i class="fa fa-pencil" style="color:#e0d014"    aria-hidden="true"></i>' +
                            '</button>'+


                             '<button style=" border: none; background-color: white;" id=""' +
                            'class=" icon_table" ng-click="caller.removeWorkItemDetailGpon(dataItem)"   uib-tooltip="Xóa" translate' + '>' +
                            '<i class="fa fa-trash" style="color: #337ab7;"   aria-hidden="true"></i>' +
                            '</button>'
                            + '</div>',
                            
                        width: '10%'
                    }
                ]
            });
//        }
        // end data grid hạng mục
        function initWorkItemTable() {
            vm.workItemGridOptions = kendoConfig.getGridOptions({
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
                            'ng-click="vm.addWorkItem()" ng-disabled="vm.disabled ||vm.Categories" ng-hide = "vm.hideAddButton || vm.completeConstruction==5" uib-tooltip="Tạo mới" translate>Tạo mới</button>' +
                            '</div>'
                            +
                            '<div class="btn-group pull-right margin_top_button margin10">' +
                            '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                            '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportWorkItemFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                            '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                            '<label ng-repeat="column in vm.workItemGrid.columns| filter: vm.gridColumnShowHideFilter">' +
                            '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}' +
                            '</label>' +
                            '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            vm.countWorkItem = response.total;
                            vm.checkNumberHangMuc = false;
                            if (response.total > 0) {
                                vm.checkNumberHangMuc = true;
                            }
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
                            url: Constant.BASE_SERVICE_URL + "workItemService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            // vm.constructionSearch.employeeId =
                            // Constant.user.srvUser.catEmployeeId;
                            vm.workItemSearch.page = options.page
                            vm.workItemSearch.pageSize = options.pageSize
                            return JSON.stringify(vm.workItemSearch)

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
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        }
                    },
                    {
                        title: "Mã hạng mục",
                        field: 'code',
                        width: '20%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tên hạng mục",
                        field: 'name',
                        width: '20%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Loại hạng mục",
                        field: 'catWorkItemType',
                        width: '15%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Đơn vị thực hiện",
                        width: '10%',
                        field: 'constructorName',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                        // template: function(dataItem){
                        // if(dataItem.contructorName2==undefined)
                        // return dataItem.contructorName2
                        // else return dataItem.contructorName1
                        // }
                    },
                    {
                        title: "Đơn vị giám sát",
                        field: 'supervisorName',
                        width: '10%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Trụ",
                        field: 'branch',
                        width: '10%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function(dataItem) {
                        	if(dataItem.branch=="280483"){
                        		return "TT GPTH";
                        	} else if(dataItem.branch=="242656") {
                        		return "TTHT";
                        	} else if(dataItem.branch=="166677") {
                        		return "TT ĐTHT";
                        	} else if(dataItem.branch=="270120") {
                        		return "TT VHKT";
                        	} else if(dataItem.branch=="280501") {
                        		return "TT CNTT";
                        	} else if(dataItem.branch=="9006003") {
                        		return "TT XDDD";
                        	} else {
                        		return "";
                        	}
                        }
                    },
                    {
                        title: "Trạng thái",
                        field: 'status',
                        width: '10%',
                        template: function (dataItem) {
                            if (dataItem.status == 1) {
                                return "<span name='status' font-weight: bold;'>Chưa thực hiện</span>"
                            } else if (dataItem.status == 2) {
                                return "<span name='status' font-weight: bold;'>Đang thực hiện</span>"
                            } else if (dataItem.status == 3) {
                                return "<span name='status' font-weight: bold;'>Đã hoàn thành</span>"
                            }else if (dataItem.status == 4) {
                                return "<span name='status' font-weight: bold;'>Vướng</span>"
                            }
                        },
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Thao tác",
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: dataItem =>
                            '<div class="text-center">'
                            + '<button style=" border: none; background-color: white;"  id="updateId" ng-click="vm.editWorkItem(dataItem)" class=" icon_table "' +
                            '   uib-tooltip="Thông tin chi tiết hạng mục" translate>' +
                            '<i class="fa fa-list-alt" style="color:#e0d014"    aria-hidden="true"></i>' +
                            '</button>' +


                             '<button style=" border: none; background-color: white;" id="" ng-hide = "vm.completeConstruction==5 || (dataItem.status!=1 && dataItem.status!=4)"' +
                            'class=" icon_table" ng-click="vm.removeWorkItem(dataItem)"   uib-tooltip="Xóa" translate' + '>' +
                            '<i class="fa fa-trash" style="color: #337ab7;"   aria-hidden="true"></i>' +
                            '</button>'
                            + '</div>',
                        width: '10%'
                    }
                ]
            });
        }
        
        vm.editWorkItem = function editWorkItem(dataItem){
			
//			vm.mess="";
//			vm.projExpense =dataItem;
			$scope.workItemIdGpon = dataItem.workItemId;
			$scope.constrIdGpon = dataItem.constructionId;
			var teamplateUrl="coms/construction/popupDetailWI.html";
			var title="Xem chi tiết hạng mục ";
			var windowId="DETAIL_WI"
			CommonService.populatePopupCreate(teamplateUrl,title,dataItem,vm,windowId,false,'50%','50%',"code123");
//			vm.clearPayment();
				
				
	}
        
        
 vm.editWorkItemDetailGpon = function editWorkItemDetailGpon(dataItem){
			
//			vm.mess="";
			vm.projExpense =dataItem;
			var teamplateUrl="coms/construction/popupEditWIDetailGpon.html";
			var title="Sửa Công việc";
			var windowId="DETAIL_WI"
			CommonService.populatePopupCreate(teamplateUrl,title,vm.projExpense,vm,windowId,false,'50%','50%',"code123");
//			vm.clearPayment();
				
				
	}
        //tatph 11/11/2019 - start
      //Huy thong tin lo hang
		vm.removeWorkItemGpon=removeWorkItemGpon;
		function removeWorkItemGpon(dataItem){
			confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function(){
				constructionService.removeGpon(dataItem).then(
					function(d) {
						toastr.success("Xóa dự án thành công!");
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
        //tatph 11/11/2019 - end
		vm.removeWorkItemDetailGpon=removeWorkItemDetailGpon;
		function removeWorkItemDetailGpon(dataItem){
			confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function(){
				constructionService.removeDetailitemGpon(dataItem).then(
					function(d) {
						toastr.success("Xóa dự án thành công!");
						var sizePage = $("#workItemDetailGpon").data("kendoGrid").dataSource.total();
						var pageSize = $("#workItemDetailGpon").data("kendoGrid").dataSource.pageSize();
						if(sizePage % pageSize === 1){
							var currentPage = $("#workItemTable").data("kendoGrid").dataSource.page();
							if (currentPage > 1) {
								$("#workItemDetailGpon").data("kendoGrid").dataSource.page(currentPage - 1);
							}
						}
						 $("#workItemDetailGpon").data('kendoGrid').dataSource.read();
						 $("#workItemDetailGpon").data('kendoGrid').refresh();

					}, function(errResponse) {
						toastr.error("Lỗi không xóa được!");
					});
			} )
		}
		
		 //tatph 11/11/2019 - end
		vm.saveProjExpense=saveProjExpense;
		function saveProjExpense(dataItem){
			if(vm.projExpense.amount == null || vm.projExpense.amount == "" || vm.projExpense.amount == undefined){
				toastr.warning("Hãy nhập khối lượng thi công!");
				return;
			}
			if(vm.projExpense.price == null || vm.projExpense.price == "" || vm.projExpense.price == undefined){
				toastr.warning("Hãy nhập đơn giá thi công!");
				return;
			}
				constructionService.editDetailGpon(dataItem).then(
					function(d) {
						toastr.success("Sửa thành công!");
						var sizePage = $("#workItemDetailGpon").data("kendoGrid").dataSource.total();
						var pageSize = $("#workItemDetailGpon").data("kendoGrid").dataSource.pageSize();
						 $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
						if(sizePage % pageSize === 1){
							var currentPage = $("#workItemDetailGpon").data("kendoGrid").dataSource.page();
							if (currentPage > 1) {
								$("#workItemDetailGpon").data("kendoGrid").dataSource.page(currentPage - 1);
							}
						}
						 $("#workItemDetailGpon").data('kendoGrid').dataSource.read();
						 $("#workItemDetailGpon").data('kendoGrid').refresh();

					}, function(errResponse) {
						toastr.error("Lỗi không sửa được!");
					});
		}
        function pushDataToconstructionTable(data) {
            var grid = vm.constructionDetailGrid;
            if (grid) {
                grid.dataSource.data(data);
                grid.refresh();
            }
        }

        function fillconstructionDetailTable() {
            vm.constructionDetailGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: false,
                resizable: true,
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
                        field: "sysGroupId",
                        // footerTemplate: "#=aggregates.source.sum#",
                        hidden: true,
                        groupHeaderTemplate: '{{dataItem.items[0].sysGroupName}}</td>' +
                            '<td class="text-right">#= aggregates.source.sum  #</td>' +
                            '<td class="text-right">#= aggregates.quantity.sum #</td>' +
                            '<td class="text-right">#= aggregates.complete.sum #</td>' +
                            '<td class="text-right">#= aggregates.revenue.sum #</td>' +
                            '<td class="text-center"><button class="icon_table text-center" style=" margin-left:6px;border: none; background-color: white;" ng-click="vm.removeconstructionPerSysGroup(#= value #)" uib-tooltip="Xóa" translate> <i style="color: darkblue" aria-hidden="true" class="fa fa-trash"></i>' +
                            '</button>'
                    },
                    {
                        title: "TT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '5%',
                        columnMenu: false,
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:center;"
                        },
                        editable: false
                    },
                    {
                        title: "Đơn vị",
                        field: 'month',
                        width: '25%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                        editable: false

                    },
                    {
                        title: "Nguồn việc",
                        field: 'source',
                        width: '15%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: "#=sum#"
                    },
                    {
                        title: "Sản lượng",
                        field: 'quantity',
                        width: '15%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: "#=sum#"
                    },
                    {
                        title: "HSHC",
                        field: 'complete',
                        width: '15%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: "#=sum#"
                    },
                    {
                        title: "Doanh thu",
                        field: 'revenue',
                        width: '15%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: "#=sum#"
                    },
                    {
                        title: "Thao tác",
                        template: dataItem =>
                            '<div class="text-center">'
                            +
                            '<button style=" border: none; background-color: white;" id="removeId"' +
                            'class=" icon_table" ng-click="vm.removeconstructionDetail($event)" uib-tooltip="Xóa" translate' +
                            '>' +
                            '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
                            '</button>'
                            + '</div>',
                        width: '10%',
                        field: "action"
                    }
                ],
                dataSource: {
                    schema: {
                        model: {
                            id: "constructionId",
                            fields: {
                                stt: { editable: false, nullable: true },
                                month: { editable: false, nullable: true },
                                source: { editable: true, type: "number", nullable: true },
                                complete: { editable: true, type: "number", nullable: true },
                                revenue: { editable: true, type: "number", nullable: true },
                                quantity: { editable: true, type: "number", nullable: true },
                                action: { editable: false, nullable: true }
                            }
                        }
                    }, sort: { field: "month", dir: "asc" },
                    group: {
                        field: "sysGroupId", aggregates: [
                            { field: "sysGroupName", aggregate: "count" },
                            { field: "source", aggregate: "sum" },
                            { field: "quantity", aggregate: "sum" },
                            { field: "complete", aggregate: "sum" },
                            { field: "revenue", aggregate: "sum" }
                        ]
                    },
                    aggregate: [
                        { field: "source", aggregate: "sum" },
                        { field: "quantity", aggregate: "sum" },
                        { field: "complete", aggregate: "sum" },
                        { field: "revenue", aggregate: "sum" }]
                },
                dataBound: function (e) {
                    var firstCell = e.element.find(".k-grouping-row td:first-child");
                    firstCell.attr("colspan", 3);
                }
            });


        }

        vm.listRemove = [{
            title: "Thao tác"
        }]
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
        vm.listConvertWorkItem = [{
            field: "status",
            data: {
                1: 'Chưa thực hiện',
                2: 'Đang thực hiện',
                3: 'Đã hoàn thành'
            }
        }
        ]
        //HuyPQ-start
        vm.exportFile = function () {
        	function displayLoading(target) {
        	      var element = $(target);
        	      kendo.ui.progress(element, true);
        	      setTimeout(function(){
        	    	  
        	    	  return Restangular.all("constructionService/exportExcelConstruction").post(vm.constructionSearch).then(function (d) {
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
        //vm.exportWorkItemFile = function exportWorkItemFile() {
        //    var data = vm.workItemGrid.dataSource.data();
        //    CommonService.exportFile(vm.workItemGrid, data, vm.listRemove, vm.listConvertWorkItem, "Danh sách tra cứu hạng muc");
        //}

        vm.exportWorkItemFile = exportWorkItemFile;
        function exportWorkItemFile() {
        	function displayLoading(target) {
      	      var element = $(target);
      	      kendo.ui.progress(element, true);
      	      setTimeout(function(){
      	    	  
      	    	return Restangular.all("constructionService/exportExcelHm").post(vm.workItemSearch).then(function (d) {
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
//        vm.exportWorkItemFile = exportWorkItemFile;
//        function exportWorkItemFile() {
//			vm.workItemSearch.page = null;
//			vm.workItemSearch.pageSize = null;
//			var data = constructionService.doSearchGpon(vm.workItemSearch);
//			console.log(data);
//			constructionService.doSearchGpon(vm.workItemSearch).then(function(d){
//				CommonService.exportFile(vm.workItemGrid,d.data,vm.listRemove,vm.listConvert,"Danh sách hạng mục");
//			});
//				
//		}
        
        vm.import = function () {
            var teamplateUrl = "coms/construction/constructionPopup.html";
            var title = "Import từ file excel";
            var windowId = "YEAR_PLAN";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '700', 'auto', "code");
        }

        vm.saveImportfile = saveImportfile;
        function saveImportfile(data) {
            if ($("#files")[0].files[0] == null) {
                toastr.warning("Bạn chưa chọn file để import");
                return;
            }
            if ($("#files")[0].files[0].name.split('.').pop() != 'xls' && $("#files")[0].files[0].name.split('.').pop() != 'xlsx') {
                toastr.warning("Sai định dạng file");
                return;
            }
            var formData = new FormData();
            formData.append('multipartFile', $('#files')[0].files[0]);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "constructionDetailRsServiceRest/constructionDetail/importGoods?folder=" + vm.folder,
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    var dem = 0;
                    if (data[data.length - 1].lstErrorGoods != null && data[data.length - 1].lstErrorGoods.length != 0) {
                        vm.lstErrImport = data[data.length - 1].lstErrorGoods;
                        vm.objectErr = data[data.length - 1];
                        var teamplateUrl = "coms/construction/constructionPopUp.html";
                        var title = "Kết quả Import";
                        var windowId = "ERR_IMPORT";

                        CommonService.populatePopupCreate(teamplateUrl, title, vm.lstErrImport, vm, windowId, false, '60%', '60%');
                        fillDataImportErrTable(vm.lstErrImport);
                        return;
                    }
                    else if (data.length === 1) {
                        toastr.warning("File imp" +
                            "ort không có dữ liệu");
                        return;
                    } else {

                        // danglam
                        $("#constructionDetail").data("kendoGrid").dataSource.read();
                        $("#constructionDetail").data("kendoGrid").refresh();
                        data.splice(data.length - 1, 1);
                        var grid = $("#constructionDetail").data("kendoGrid");
                        for (var i = 0; i < data.length; i++) {
                            data[i].id = i + 1;
                            data[i].sysGroupId = data[i].sysGroupId;
                            data[i].month = data[i].month;
                            data[i].source = data[i].source;
                            data[i].complete = data[i].complete;
                            data[i].revenue = data[i].revenue;

                            /*
							 * data[i].partNumber = data[i].partNumber;
							 * data[i].serial = data[i].serial; data[i].isSerial =
							 * data[i].isSerial; data[i].unitTypeName =
							 * data[i].goodsUnitName; data[i].unitTypeId =
							 * data[i].unitType; data[i].originPrice =
							 * data[i].originPrice;
							 * data[i].amount=data[i].amount;
							 */
                            if (data[i].partNumber.length > 100) {
                                toastr.warning("PartNumber không được vượt quá maxlength!");
                                return;
                            }
                            if (data[i].serial.length > 100) {
                                toastr.warning("Serial không được vượt quá maxlength!");
                                return;
                            }
                            if (data[i].amount.length > 10) {
                                toastr.warning("Số lượng không vượt quá maxlength!");
                                return;
                            }
                            else {
                                grid.dataSource.add(data[i]);
                            }
                        }
                        toastr.success("Import thành công!");
                    }
                }
            });
        }

        function refreshGrid(d) {
            var grid = vm.constructionGrid;
            if (grid) {
                grid.dataSource.data(d);
                grid.refresh();
            }
        }
        function refreshCustomGrid(d, grid) {
            if (grid) {
                grid.dataSource.data(d);
                grid.refresh();
            }
        }

        vm.add = add;
        function add() {
//            return Restangular.all("constructionService/checkAdd").post({}).then(function (d) {
//                if (d.error) {
//                    toastr.error(d.error);
//                    return;
//                }
//            }).catch(function (e) {
                vm.fileLst = [];
                vm.isCreateNew = true;
                vm.checkWorkItemHtct = false;
                vm.constrObj = {};
                vm.constrObj.catConstructionTypeId = 1;
                var teamplateUrl = "coms/construction/constructionPopup.html";
                var title = "Thêm mới công trình";
                var windowId = "CONSTRUCTION";
                vm.constrObj.checkHTCT=false;
                CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1100', '500', null);
//            });
        }
       
        vm.addWorkItem = addWorkItem;
        function addWorkItem() {
	//UNIKOM check quyên gan hang muc
     //Huypq-01082020-comment phần check quyền
//            return Restangular.all("constructionService/checkAddWorkItem").post({}).then(function (d) {
//                if (d.error) {
//                    toastr.error(d.error);
//                    return;
//                }
//            }).catch(function (e) {
                if(vm.status==4){
                    toastr.error("Công trình đã tạm dừng không được gán thêm hạng mục !");
                    return;
                }
                vm.checkNode = true;
                vm.checkEditGPon = false;
                vm.lstWorkItemGPon = [];
                fillDataGrid(vm.lstWorkItemGPon);
                vm.workItemRecord = { workItemTypeList: [] };
                vm.workItemRecord.isInternal = 1;
                //chinhpxn 20180605 start
                getSysGroupInfo(Constant.userInfo.vpsUserToken.sysUserId);
                //chinhpxn 20180605 end
                vm.showHm = true;
                vm.showHm1 = false;
                vm.disabledHangMuc = false;
                var obj = {};
                obj.constructionId = vm.workItemSearch.constructionId;
                constructionService.checkContructionType(obj).then(function (data) {
                    if(data.catConstructionTypeId == 3){
                        vm.workItemGPon.constructionCode = data.code;
                        var teamplateUrl = "coms/construction/workItemPopupGPonDetail.html";
                        var title = "Thêm mới hạng mục";
                        var windowId = "addWorkItem";
                        CommonService.populatePopupPartner(teamplateUrl, title, null, vm, windowId, true, '500', '500', "isInternal1");
                    } else {
                        var teamplateUrl = "coms/construction/workItemPopupDetail.html";
                        var title = "Thêm mới hạng mục";
                        var windowId = "addWorkItem";
                        CommonService.populatePopupPartner(teamplateUrl, title, null, vm, windowId, true, '500', '500', "isInternal1");
                    }

                })
//            });
        	

        }
        //Manhvv-star
        function validatekendodatepicker() {
            $('#handoverDate1').click(function () {
                var todayDate = kendo.toString(kendo.parseDate(new Date()), 'dd/MM/yyyy');
                $("#handoverDate").data("kendoDatePicker").value(todayDate);
            });
            $('#startingDate1').click(function () {
                var todayDate = kendo.toString(kendo.parseDate(new Date()), 'dd/MM/yyyy');
                $("#startingDate").data("kendoDatePicker").value(todayDate);
            });
            $('#excpectedCompleteDate1').click(function () {
                var todayDate = kendo.toString(kendo.parseDate(new Date()), 'dd/MM/yyyy');
                $("#excpectedCompleteDate").data("kendoDatePicker").value(todayDate);
            });
        }
        //Manhvv-end
        /*if(vm.checkEdit == false){
        	$scope.$watch('vm.constrObj.amountCableLive', function () {
            	
    			if(vm.constrObj.amount != "" || vm.constrObj.amount != null){
    				var amount = vm.constrObj.amount.toString().replace(/,/g, '');
    				var amountCableLive = vm.constrObj.amountCableLive.toString().replace(/,/g, '');
    				vm.constrObj.amountCableRent = amount - amountCableLive;
    			}
    		});
            $scope.$watch('vm.constrObj.amountCableRent', function () {
            	
    			if(vm.constrObj.amount != "" || vm.constrObj.amount != null){
    				var amount = vm.constrObj.amount.toString().replace(/,/g, '');
    				var amountCableRent = vm.constrObj.amountCableRent.toString().replace(/,/g, '');
    				vm.constrObj.amountCableLive = amount - amountCableRent;
    			}
    		});
        }*/
     
        vm.edit = edit;
       
        vm.checkNumberHangMuc = false;
        function edit(dataItem) {
        	kendo.ui.progress($("#tabDetail"), true); //HienLT56 add 22032021
            validatekendodatepicker();
            vm.disabled = dataItem.status == 0;
            vm.completeConstruction = dataItem.status;
            vm.isCreateNew = false;
            vm.String = "Quản lý công trình > Thông tin công trình > Chỉnh sửa thông tin công trình";
            vm.showDetail = true;
            vm.chooseTab(1);
            vm.constrObj.isBuildingPermit=dataItem.isBuildingPermit; //Huypq-20181011
            vm.workItemSearch.constructionId = dataItem.constructionId;
            vm.workItemSearch.sysGroupId = dataItem.sysGroupId;
            
            TypeWorkHm(dataItem);
            getSupperVisor(dataItem.constructionId);
            vm.deliveryBillSearch.typeExport = '';
            refreshGrid(vm.contractFileEditGrid, []);
            // selectCheckRental();
            vm.checkWorkItemHtct = true;
            
            constructionService.getconstructionById(dataItem.constructionId).then(function (data) {
            	vm.checkRevenueBranch = data.checkRevenueBranch;
                vm.constrObj = angular.copy(data);
                vm.constrObj.handoverType = !vm.constrObj.handoverDateElectric ? 1 : 2;
                vm.constrObj.status = Number(vm.constrObj.status);
                vm.constrObj.isStationHtct=dataItem.checkHTCT;
                //taotq start 11052021
                vm.constrObj.unitConstructionName = data.unitConstructionName
                if(data.unitConstruction == 1){
                	vm.constrObj.checkDVNTTC = true;
                	vm.constrObj.disable = true;
                }else{
                	vm.constrObj.checkDVNTTC = false;
                	vm.constrObj.disable = false;
                }
                //taotq start 11052021
                //HienLT56 start 28012021
                if((data.listDataWorkItem != null && data.listDataWorkItem.length >  0) || (data.lstDataWO != null && data.lstDataWO.length > 0)){
                	vm.checkB2BB2C = true;
                } else{
                	vm.checkB2BB2C = false;
                }
                //HienLT56 end 28012021
                //Huypq-start
                if(vm.constrObj.isBuildingPermit==1){
                	vm.constrObj.isBuildingPermit=1;
                } else {
                	vm.constrObj.isBuildingPermit=null;
                }
                vm.status=dataItem.status;
              //Huypq-end
                //                hungnx 080618 start
                vm.fileLst = vm.constrObj.fileLst;
                vm.contractFileGrid = null;
                refreshGrid(vm.contractFileEditGrid, vm.fileLst);
                vm.constrObj.price = !!vm.constrObj.price || vm.constrObj.price == 0 ? htmlCommonService.addThousandComma(vm.constrObj.price.toString()) : null;
                vm.constrObj.amount = !!vm.constrObj.amount || vm.constrObj.amount == 0 ? htmlCommonService.addThousandComma(vm.constrObj.amount.toString()) : null;
				/**hoangnh 171218 start**/
                vm.constrObj.totalAmountChest = !!vm.constrObj.totalAmountChest || vm.constrObj.totalAmountChest == 0 ? htmlCommonService.addThousandComma(vm.constrObj.totalAmountChest.toString()) : null;
                vm.constrObj.priceChest = !!vm.constrObj.priceChest || vm.constrObj.priceChest == 0 ? htmlCommonService.addThousandComma(vm.constrObj.priceChest.toString()) : null;
                vm.constrObj.totalAmountGate = !!vm.constrObj.totalAmountGate || vm.constrObj.totalAmountGate == 0 ? htmlCommonService.addThousandComma(vm.constrObj.totalAmountGate.toString()) : null;
                vm.constrObj.priceGate = !!vm.constrObj.priceGate || vm.constrObj.priceGate == 0 ? htmlCommonService.addThousandComma(vm.constrObj.priceGate.toString()) : null;
                vm.constrObj.amountCableLive = !!vm.constrObj.amountCableLive || vm.constrObj.amountCableLive == 0 ? htmlCommonService.addThousandComma(vm.constrObj.amountCableLive.toString()) : null;
                vm.constrObj.amountCableRent = !!vm.constrObj.amountCableRent || vm.constrObj.amountCableRent == 0 ? htmlCommonService.addThousandComma(vm.constrObj.amountCableRent.toString()) : null;
                /**hoangnh 171218 end**/
                vm.constrObj.moneyType = !!vm.constrObj.moneyType ? vm.constrObj.moneyType.toString() : '1';
				//hoanm1_20181031_start
                //if (data.quantityByDate == null)
                  //  $("#amount").attr('disabled', false);
                //else
                  //  $("#amount").attr('disabled', true);
				//hoanm1_20181031_end
                if (data.countTaskDailyConfirmed > 0) {
                    $('#price').attr('disabled', true);
                    $('#moneyType').attr('disabled', true);
                } else {
                    $('#price').attr('disabled', false);
                    $('#moneyType').attr('disabled', false);
                }
                //              hungnx 080618 end
                initWorkItemTable();
                vm.initDataNT();
                vm.initDataMer();
                vm.initConvenant();
                vm.initComplete();
                vm.checkboxAction(vm.constrObj.catConstructionTypeId);
                vm.checkboxAction(vm.constrObj.catConstructionTypeId);

                //hienvd: end 1/7/2019

                var grid = vm.covenantGrid;
                if (grid) {
                    grid.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });

                };
                var grid2 = vm.completeGrid;
                if (grid2) {
                    grid2.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });
                };
                vm.deliveryBill();
                var gridpxk = vm.deliveryBillTable;
                if (gridpxk) {
                    gridpxk.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });
                };
                var gridhddv = vm.contractInput;
                if (gridhddv) {
                    gridhddv.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });
                }
                var gridVu = vm.entangledGrid1;
                if (gridVu) {
                    gridVu.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });
                }
                var gridHM = vm.workItemGrid;
                if (gridHM) {
                    gridHM.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });
                }
                var gridBGMB = vm.BGMBgrid;
                if (gridBGMB) {
                    gridBGMB.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });
                }
                var gridKC = vm.startgrid;
                if (gridKC) {
                    gridKC.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });
                }
                var gridDSVTA = vm.DSVTAGrid;
                if (gridDSVTA) {
                    gridDSVTA.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });
                }
                var gridDSTBA = vm.DSTBAGrid;
                if (gridDSTBA) {
                    gridDSTBA.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });
                }
                var gridMerc = vm.merchandisegrid;
                if (gridMerc) {
                    gridMerc.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });
                }
                var gridMerc1 = vm.merchandisegrid1;
                if (gridMerc1) {
                    gridMerc1.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });
                }
                var gridMerc2 = vm.merchandisegrid2;
                if (gridMerc2) {
                    gridMerc2.dataSource.query({
                        page: 1,
                        pageSize: 10
                    });
                }
                vm.DSVTAGrid.dataSource.page(1);
                vm.DSTBAGrid.dataSource.page(1);
                vm.DSVTBGrid.dataSource.page(1);
                vm.DSTBBGrid.dataSource.page(1);
                vm.TPGrid.dataSource.page(1);
                vm.contractInputGrid();
                vm.entangledGrid();
                refreshCustomGrid(data.listFileVuong, vm.vuongGrid);
                refreshCustomGrid(data.listFileBGMB, vm.BGMBgrid);
                refreshCustomGrid(data.listFileStart, vm.startgrid);
                refreshCustomGrid(data.listFileMerchandise, vm.merchandisegrid);
                
                //nhantv 25092018
                refreshCustomGrid(data.listFileConstrLicence, vm.ConstrLicencegrid);
                refreshCustomGrid(data.listFileConstrDesign, vm.constrDesignGrid);

//                if(dataItem.checkHTCT==1){
                	vm.checkWorkItemHtct = false;
//                } else {
//                	vm.checkWorkItemHtct = true;
//                }
                
                //hienvd: START 8/6/2019
                kendo.ui.progress($("#tabDetail"), false); //HienLT56 add 22032021
                if(dataItem.checkHTCT == 1){
                    vm.constrObj.checkHTCT = true;
//                    document.getElementById("location").style.visibility = "visible";
//                    document.getElementById("highHTCT").style.visibility = "visible";
//                    document.getElementById("capexHTCT").style.visibility = "visible";
                    // console.log(dataItem);
                    // console.log(data);
//                    vm.constrObj.locationHTCT = data.locationHTCT;
//                    vm.constrObj.highHTCT = data.highHTCT;
//                    vm.constrObj.capexHTCT = data.capexHTCT;
                }else {
                    document.getElementById("location").style.visibility = "hidden";
                    document.getElementById("highHTCT").style.visibility = "hidden";
                    document.getElementById("capexHTCT").style.visibility = "hidden";
                }
                //hienvd: END 8/6/2019

                // resetForm();
            }, function (error) {
                toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
                kendo.ui.progress($("#tabDetail"), false); //HienLT56 add 22032021
            });

            /** hienvd: START 1/7/2019 **/
            $scope.listImage = {};
            constructionService.getListImageById(dataItem).then(function (data) {
                if (data.listImage.length > 0) {
                    $scope.listImage = data.listImage;
                    $scope.changeImage($scope.listImage[0]);
                } else {
                    $scope.listImage = [];
                }
            }, function (error) {
                $scope.listImage = [];
            });
			//hoanm1_20190708_start
			  $scope.listImageBGMB = {};
            constructionService.getListImageByIdBGMB(dataItem).then(function (data) {
                if (data.listImage.length > 0) {
                    $scope.listImageBGMB = data.listImage;
                    $scope.changeImageBGMB($scope.listImageBGMB[0]);
                } else {
                    $scope.listImageBGMB = [];
                }
            }, function (error) {
                $scope.listImageBGMB = [];
            });
			//hoanm1_20190708_end

            constructionService.getDateConstruction(dataItem).then(function (data) {
                console.log(data);
                if (data != null) {
                    if(data.startingDate != null){
                        $scope.vm.constrObj.startingDate = data.startingDate;
                    }
                    if(data.handoverDateBuild != null){
                        $scope.vm.constrObj.handoverDate = data.handoverDateBuild;
                    }
                } else {
                    $scope.vm.constrObj.startingDate = data.startingDate;
                    $scope.vm.constrObj.handoverDate = data.handoverDateBuild;
                }
            }, function (error) {

            });

            /** hienvd: END **/
        }

        vm.copy = copy;
        function copy(dataItem) {
            vm.String = "Quản lý công trình > Quản lý kế hoạch > Thêm mới kế hoạch năm";
            vm.showDetail = true;
            vm.construction = dataItem;
            constructionService.getconstructionById(dataItem.constructionId).then(function (data) {
                vm.construction = data;
                vm.constructionDetailList = data.detailList;
                vm.initConvenant();
                vm.deliveryBill();
                refreshconstructionDetailList(data.detailList);
                vm.construction.constructionId = undefined;
            }, function (error) {
                toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
            });
        }

        function refreshconstructionDetailList(data) {
            var grid = vm.constructionDetailGrid;
            if (grid) {
                grid.dataSource.data(data);
                grid.refresh();
            }
        }

        vm.removeconstructionDetail = removeconstructionDetail;
        function removeconstructionDetail(e) {
            var grid = vm.constructionDetailGrid;
            var row = $(e.target).closest("tr");
            var dataItem = grid.dataItem(row);
            grid.removeRow(dataItem); // just gives alert message
            grid.dataSource.remove(dataItem); // removes it actually from the
            // grid
            grid.dataSource.sync();
            grid.refresh();
        }

        vm.removeconstructionPerSysGroup = removeconstructionPerSysGroup;
        function removeconstructionPerSysGroup(sysGroupId) {
            var constructionDetailGrid = vm.constructionDetailGrid;
            var listRemove = [];
            if (constructionDetailGrid != undefined && constructionDetailGrid.dataSource != undefined) {
                var detailList = constructionDetailGrid.dataSource._data;
                angular.forEach(detailList, function (dataItem) {
                    if (sysGroupId == dataItem.sysGroupId) {
                        listRemove.push(dataItem);
                    }
                })
            }
            angular.forEach(listRemove, function (dataItem) {
                constructionDetailGrid.removeRow(dataItem); // just gives alert
                // message
                constructionDetailGrid.dataSource.remove(dataItem); // removes
                // it
                // actually
                // from the
                // grid
                constructionDetailGrid.dataSource.sync();
                constructionDetailGrid.refresh();
            })
            constructionDetailGrid.dataSource.page(1);
        }

        vm.addconstruction = addconstruction;
        function addconstruction() {
            if (validateTemp()) {
                var grid = vm.constructionDetailGrid;
                if (grid) {
                    var detailList = grid.dataSource._data;
                    var valid = true;
                    angular.forEach(detailList, function (dataItem) {
                        if (vm.construction.sysGroupId == dataItem.sysGroupId && vm.construction.month == dataItem.month) {
                            valid = false;
                        }
                    })
                    if (valid) {
                        grid.dataSource.add(vm.construction);
                        grid.dataSource.sync();
                        grid.refresh();
                    } else {
                        toastr.warning("Dữ liệu tháng " + vm.construction.month + " của đơn vị " + vm.construction + " đã tồn tại!");
                    }
                }
            }

        }

        function validateTemp() {
            if (vm.construction.sysGroupId == undefined) {
                toastr.warning("Bạn phải chọn đơn vị!");
                return false;
            }
            if (vm.construction.source == undefined) {
                toastr.warning("Bạn phải nhập nguồn việc!");
                return false;
            }
            if (vm.construction.quantity == undefined && vm.construction.type == 1) {
                toastr.warning("Bạn phải nhập sản lượng!");
                return false;
            }
            if (vm.construction.complete == undefined) {
                toastr.warning("Bạn phải nhập HSHC");
                return false;
            }
            if (vm.construction.revenue == undefined) {
                toastr.warning("Bạn phải nhập doanh thu");
                return false;
            }
            return true;
        }

        vm.save = function () {
            if (validateYearDetail()) {
                var data = populateDataToSave();
                if (data.constructionId == null || data.constructionId == '') {
                    constructionService.createconstruction(data).then(function (result) {
                        if (result.error) {
                            $('#year').focus();
                            toastr.error(result.error);
                            return;
                        }
                        toastr.success("Thêm mới thành công!");
//                        vm.cancel();
                        CommonService.dismissPopup();
                        doSearchContruction();
                    }, function (errResponse) {
                        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi thêm mới"));
                    });
                }
                else {
                    constructionService.updateconstruction(data).then(function (result) {
                        if (result.error) {
                            $('#year').focus();
                            toastr.error(result.error);
                            return;
                        }
                        toastr.success("Chỉnh sửa thành công!");
//                        vm.cancel();
                        CommonService.dismissPopup();
                        doSearchContruction();
                    }, function (errResponse) {
                        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi chỉnh sửa"));
                    });
                }
            }
        }
        // validate mỗi đơn vị phải đủ 12 tháng
        function validateYearDetail() {
            var isValid = true;
            var sysGroupName = '';
            var detailList = [];
            var constructionDetailGrid = vm.constructionDetailGrid;
            if (constructionDetailGrid != undefined && constructionDetailGrid.dataSource != undefined) {
                detailList = constructionDetailGrid.dataSource._data;
                var result = _.chain(detailList).groupBy("sysGroupId").map(function (v, i) {
                    return {
                        sysGroupId: i,
                        sysGroupName: (_.find(v, 'sysGroupName')).sysGroupName,
                        monthList: _.map(v, 'month')
                    }
                }).value();
                if (result != undefined && result.length > 0) {
                    angular.forEach(result, function (item) {
                        if (item.monthList == null || item.monthList.length != 12) {
                            isValid = false;
                            sysGroupName = item.sysGroupName;
                        }
                    })
                }
            }
            if (!isValid) {
                toastr.warning("Kế hoạch năm của đơn vị" + sysGroupName + " không hợp lệ!");
            }
            return isValid;
        }

        function populateDataToSave() {
            var data = vm.construction;
            var detailList = []
            var constructionDetailGrid = vm.constructionDetailGrid;
            if (constructionDetailGrid != undefined && constructionDetailGrid.dataSource != undefined)
                data.detailList = constructionDetailGrid.dataSource._data;
            return data;
        }

        vm.cancel = cancel;
        function cancel(form) {
            vm.String = 'Quản lý công trình > Thông tin công trình';
            vm.showDetail = false;
            vm.construction = {};
            vm.constrObj = {};
            vm.constructDetailValue == 1;
            vm.certObj = {};

            // vm.completeSearch={};
            // vm.completeGrid.dataSource.data([]);
            // vm.completeGrid.refresh();
            //chinhpxn 20180606 start
            //          vm.constructionGrid.dataSource.data([]);
            //            vm.doSearch();
            //chinhpxn 20180606 end
            $rootScope.isTrue = true;
            if (form != null) {
                form.$setPristine();
                form.$setUntouched();
            }
        }
        function resetForm() {
            blurInput('excpectedCompleteDate');
            blurInput('excpectedStartingDate1');
            blurInput('excpectedStartingDate2');
            blurInput('excpectedStartingDate3');
            blurInput('excpectedStartingDate4');
            blurInput('cCompleteDate');
            blurInput('cStartingDate');
            blurInput('startingDate');
            blurInput('returnDate');
            blurInput('handoverDate');
        }
        function blurInput(id) {
            if (document.getElementById(id) != null) {
                document.getElementById(id).focus();
                document.getElementById(id).blur();
            }
        }
        vm.remove = remove;
        function remove(dataItem) {
            return Restangular.all("constructionService/check").post(dataItem).then(function (d) {
                if (d.error) {
                    toastr.error(d.error);
                    return;
                }
            }).catch(function (e) {
                confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function () {
                    constructionService.remove(dataItem).then(
                        function (da) {
                            var sizePage = $("#constructionGrid").data("kendoGrid").dataSource.total();
                            var pageSize = $("#constructionGrid").data("kendoGrid").dataSource.pageSize();
                            if (sizePage % pageSize === 1) {
                                var currentPage = $("#constructionGrid").data("kendoGrid").dataSource.page();
                                if (currentPage > 1) {
                                    $("#constructionGrid").data("kendoGrid").dataSource.page(currentPage - 1);
                                }
                            }
                            $("#constructionGrid").data('kendoGrid').dataSource.read();
                            $("#constructionGrid").data('kendoGrid').refresh();
                            toastr.success("Xóa bản ghi thành công!");
                        }, function (errResponse) {
                            toastr.error("Lỗi không xóa được!");
                        });
                });
            });
        }


        vm.cancelDoSearch = function () {
            vm.showDetail = false;
            vm.constructionSearch = {
                status: "1"
            };
            doSearchContruction();
        };

        vm.doSearchContruction = doSearchContruction;
        function doSearchContruction() {
        	if(vm.constructionSearch.dateFrom==null || vm.constructionSearch.dateFrom == ""){
        		toastr.warning("Chưa nhập Ngày tạo từ ngày ! ");
        		$("#dateFrom").focus();
        		return;
        	}
        	if(vm.constructionSearch.dateTo==null || vm.constructionSearch.dateTo == ""){
        		toastr.warning("Chưa nhập Ngày tạo đến ngày ! ");
        		$("#dateTo").focus();
        		return;
        	}
            vm.showDetail = false;
            var grid = vm.constructionGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        }


        vm.showHideColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.constructionGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.constructionGrid.showColumn(column);
            } else {
                vm.constructionGrid.hideColumn(column);
            }


        }
        vm.showHideWorkItemColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.workItemGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.workItemGrid.showColumn(column);
            } else {
                vm.workItemGrid.hideColumn(column);
            }


        }
        /*
		 * * Filter các cột của select
		 */

        vm.gridColumnShowHideFilter = function (item) {
            return item.type == null || item.type !== 1;
        };


        vm.exportpdf = function () {
            var obj = {};
            constructionService.exportpdf(obj);
        }


        /*
		 * var specialChars = "<>@!#$%^&*()+[]{}?:;|'\"\\,./~`-="; var check =
		 * function(string){ for(var i = 0; i < specialChars.length;i++){
		 * if(string.indexOf(specialChars[i]) > -1){ return true; } }
		 * vm.listApp.mess=""; return false; } var check1 = function(string){
		 * for(var i = 0; i < specialChars.length;i++){
		 * if(string.indexOf(specialChars[i]) > -1){ return true; } }
		 * vm.listApp.mess1=""; return false; } var check2 = function(string){
		 * for(var i = 0; i < specialChars.length;i++){
		 * if(string.indexOf(specialChars[i]) > -1){ return true; } }
		 * vm.listApp.mess2=""; return false; }
		 * 
		 * vm.checkErr = checkErr; function checkErr() { var parType =
		 * $('#parType').val(); var code = $('#code').val(); var
		 * name=$('#name').val(); if(check(parType) === true){ vm.listApp.mess =
		 * "Loại tham số chứa ký tự đặc biệt"; } if(check1(code) === true){
		 * vm.listApp.mess1 = "Mã tham số chứa ký tự đặc biệt"; }
		 * if(check2(name) === true){ vm.listApp.mess2 = "Tên Tham số ký tự đặc
		 * biệt"; } return vm.listApp; }
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
            var templateUrl = 'wms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
        }

        vm.onSave = onSave;
        function onSave(data) {

            if (vm.departmentpopUp === 'dept') {
                vm.constructionSearch.sysGroupName = data.text;
                vm.constructionSearch.sysGroupId = data.id;
            } else
                if (vm.departmentpopUp === 'deptWork') {
                    vm.workItemRecord.constructorName1 = data.text;
                    vm.workItemRecord.constructorId = data.id;
                } else
                    if (vm.departmentpopUp === 'sup') {
                        vm.workItemRecord.supervisorName = data.text;
                        vm.workItemRecord.supervisorId = data.id;
                    } else
                        if (vm.departmentpopUp === 'part') {
                            vm.workItemRecord.constructorName2 = data.text;
                            vm.workItemRecord.constructorId = data.id;
                        } else if (vm.departmentpopUp === 'dept1') {
                            vm.constrObj.sysGroupName = data.text;
                            vm.constrObj.sysGroupId = data.id;
                        }
        }

        // clear data
        var constructionCode;
        var sysGroupCode;
        var catProvinceName;
        vm.changeDataAuto = changeDataAuto
        function changeDataAuto(id) {
            switch (id) {
                case 'dept':
                    {
                        if (processSearch(id, vm.selectedStation1)) {
                            if (vm.constrObj.catStationId == null) {
                                vm.constrObj.catStationCode = null;
                                vm.constrObj.name = null;
                            } else {
                                if (constructionCode) {
                                    if (vm.constrObj.catStationCode != constructionCode) {
                                        vm.constrObj.catStationId = null;
                                        vm.constrObj.catStationCode = null;
                                        vm.constrObj.name = null;
                                    }
                                }
                            }
                            //                       vm.constrObj.name = null;
                            vm.selectedStation1 = false;
                        }
                        vm.selectedStation1 = false;
                        break;
                    }
                case 'deptGroup':
                    {
                        if (processSearch(id, vm.selectedDept1)) {
                            if (vm.constrObj.sysGroupId == null) {
                                vm.constrObj.sysGroupName = null;
                            } else {
                                if (sysGroupCode) {
                                    if (vm.constrObj.sysGroupName != sysGroupCode) {
                                        vm.constrObj.sysGroupId = null;
                                        vm.constrObj.sysGroupName = null;
                                    }
                                }
                            }
                            vm.constructionSearch.sysGroupName = null;

                            vm.selectedDept1 = false;

                        }
                        break;

                    }
                case 'deptWork':
                    {
                        if (processSearch(id, vm.selectedDeptWorkChoose)) {
                            vm.selectedDeptWorkChoose = false;
                            vm.workItemRecord.constructorName1 = null;
                            vm.selectedDeptWorkChoose = false;
                        }
                        break;
                    }
                case 'sup':
                    {
                        if (processSearch(id, vm.selectedSupChoose)) {
                            vm.workItemRecord.supervisorName = null;
                            vm.selectedSupChoose = false;
                            vm.selectedSupChoose = false;
                        }
                        break;
                    }
                case 'sup':
                    {
                        if (processSearch(id, vm.selectedPartChoose)) {
                            vm.selectedPartChoose = false;
                            vm.workItemRecord.constructorName2 = null;
                            vm.selectedPartChoose = false;
                        }
                        break;
                    }
                case 'dept1':
                    {
                        if (processSearch(id, vm.selectedDept1)) {
                            if (vm.constrObj.sysGroupId == null) {
                                vm.constrObj.sysGroupName = null;
                            } else {
                                if (constructionCode) {
                                    if (vm.constrObj.sysGroupName != constructionCode) {
                                        vm.constrObj.sysGroupId = null;
                                        vm.constrObj.sysGroupName = null;
                                    }
                                }
                            }
                            vm.constrObj.sysGroupName = null;
                            vm.selectedDept1 = false;
                        }
                        vm.selectedDept1 = false;
                        break;
                    }
                case 'people':
                    {
                        if (processSearch(id, vm.selectedperformerItem)) {
                            if (vm.workItemRecord.performerId == null) {
                                vm.workItemRecord.performerName = null;
                            } else {
                                vm.selectedperformerItem = false;
                            }
                        } else {
                            vm.selectedperformerItem = false;
                            vm.selectedperformerItem = false;
                        }
                        break;
                    }
                case 'deptEdit':
                    {
                        if (processSearch(id, vm.selectedStation2)) {
                            if (vm.constrObj.catStationId == null) {
                                vm.constrObj.catStationCode = null;
                                vm.constrObj.name = null;
                            } else {
                                if (constructionCode) {
                                    if (vm.constrObj.catStationCode != constructionCode) {
                                        vm.constrObj.catStationId = null;
                                        vm.constrObj.catStationCode = null;
                                        vm.constrObj.name = null;
                                    }
                                }
                            }
                            vm.constrObj.name = null;
                            vm.selectedStation2 = false;
                        }
                        vm.selectedStation2 = false;
                        break;
                    }
                case 'province':
                {
                    if (processSearch(id, vm.selectedStation1)) {
                        if (vm.constructionSearch.catProvinceId == null) {
                        	vm.constructionSearch.catProvinceId = null;
                        	vm.constructionSearch.catProvince = null;
                        } else {
                            if (catProvinceName) {
                                if (vm.constructionSearch.catProvince != catProvinceName) {
                                	vm.constructionSearch.catProvinceId = null;
                                	vm.constructionSearch.catProvince = null;
                                }
                            }
                        }
                        vm.selectedStation1 = false;
                    }
                    vm.selectedStation1 = false;
                    break;
                }
            }
        }
        vm.cancelStationInput = function (param) {
            vm.constrObj.catStationCode = null;
            vm.constrObj.catStationId = null;
        }
        vm.cancelDateInput = function (param) {
            vm.constrObj.excpectedStartingDate = null;
        }
        vm.cancelInput = function (param, form) {
        	if(param=='catStationHouseCode'){
        		vm.constructionSearch.catStationHouseCode = null;
        		vm.constructionSearch.catStationHouseId = null;
        	}
            if (param == 'dept') {
                //vm.constructionSearch.sysGroupId = null;
                //vm.constructionSearch.sysGroupName = null;
                vm.constrObj.sysGroupName = null;
            } else
                if (param == 'deptWork') {
                    vm.workItemRecord.constructorName1 = null;
                    vm.workItemRecord.constructorId = null;
                } else
                    if (param == 'part') {
                        vm.workItemRecord.constructorName2 = null;
                        vm.workItemRecord.constructorId = null;
                    } else
                        if (param == 'sup') {
                            vm.workItemRecord.supervisorName = null;
                            vm.workItemRecord.supervisorId = null;
                        }
                        else if (param == 'dept1') {
                            vm.constrObj.sysGroupId = null;
                            vm.constrObj.sysGroupName = null;
                        }
                        else if (param == 'consType') {
                            vm.constructionSearch.listCatConstructionType = [];
                        }
                        else if (param == 'vuong') {
                            vm.constructionSearch.statuVuong = null;
                        }
                        else if (param == 'consStatus') {
                            vm.constructionSearch.listStatus = [];
                        }
                        else if (param == 'excpectedStartingDate') {
                            vm.constrObj.excpectedStartingDate = null;
                            $rootScope.validateDate(vm.constrObj.excpectedStartingDate, null, null, form)
                        }
                        else if (param == 'handoverDate') {
                            vm.constrObj.handoverDate = null;
                            $rootScope.validateDate(vm.constrObj.handoverDate, null, null, form, 21)
                        }
                        else if (param == 'startingDate') {
                            vm.constrObj.startingDate = null;
                            $rootScope.validateDate(vm.constrObj.startingDate, null, null, form, 22)
                        }
                        else if (param == 'excpectedCompleteDate') {
                            vm.constrObj.excpectedCompleteDate = null;
                            $rootScope.validateDate(vm.constrObj.excpectedCompleteDate, null, null, form, 22)
                        }
                        else if (param == 'cCompleteDate') {
                            vm.certObj.completeDate = null;
                            $rootScope.validateDate(vm.certObj.completeDate, null, vm.certObj.startingDate, form, 26)
                        }
                        else if (param == 'cStartingDate') {
                            vm.certObj.startingDate = null;
                            $rootScope.validateDate(vm.certObj.startingDate, vm.certObj.completeDate, null, form, 26)
                        }
                        else if (param == 'returnDate') {
                            vm.constrObj.returnDate = null;
                            $rootScope.validateDate(vm.constrObj.returnDate, null, null, form, 27)
                        } else if (param == 'createFrom') {
                            vm.entangledSearch.createdFrom = null;
                            $rootScope.validateDate(vm.entangledSearch.createdFrom, null, null, form)
                        } else if (param == 'createTo') {
                            vm.entangledSearch.createdTo = null;
                            $rootScope.validateDate(vm.entangledSearch.createdTo, null, null, form)
                        }
                        else if (param == "people") {
                            vm.workItemRecord.performerName = null;
                            vm.workItemRecord.performerId = null;
                        } else if (param == "deptClear") {
                            vm.constructionSearch.sysGroupId = null;
                            vm.constructionSearch.sysGroupName = null;
                        } else if (param == "province") {
                            vm.constructionSearch.catProvinceId = null;
                            vm.constructionSearch.catProvince = null;
                        } else if (param == "date"){
                        	vm.constructionSearch.dateFrom = null;
                            vm.constructionSearch.dateTo = null;
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
                vm.constrObj.sysGroupName = dataItem.text;
                vm.constrObj.sysGroupId = dataItem.id;
                sysGroupCode = vm.constrObj.sysGroupName;
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
                            name: vm.constrObj.sysGroupName,
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
                    vm.constrObj.sysGroupName = null;// thành name
                    vm.constrObj.sysGroupId = null;
                }
            },
            ignoreCase: false
        }

        vm.deprtOptions2 = {
            dataTextField: "text",
            dataValueField: "id",
            placeholder: "Nhập mã hoặc tên đơn vị",
            select: function (e) {
                vm.selectedDept1 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.constructionSearch.sysGroupName = dataItem.text;
                vm.constructionSearch.sysGroupId = dataItem.id;
                sysGroupCode = vm.constructionSearch.sysGroupName;
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
                            name: vm.constructionSearch.sysGroupName,
                            pageSize: vm.deprtOptions2.pageSize
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
                    vm.constructionSearch.sysGroupName = null;// thành name
                    vm.constructionSearch.sysGroupId = null;
                }
            },
            ignoreCase: false
        };

        vm.provinceOptions = {
            dataTextField: "name",
            dataValueField: "id",
            placeholder: "Nhập mã hoặc tên tỉnh",
            select: function (e) {
                vm.selectedDept1 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.constructionSearch.catProvince = dataItem.name;
                vm.constructionSearch.catProvinceId = dataItem.catProvinceId;
                catProvinceName = vm.constructionSearch.catProvince;
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
                        return Restangular.all("catProvinceServiceRest/catProvince/doSearchProvinceInPopup").post({
                            name: vm.constructionSearch.catProvince,
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
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.constructionSearch.catProvince = null;
                    vm.constructionSearch.catProvinceId = null;
                }
            },
            ignoreCase: false
        };

        vm.selectedStation1 = false;
        vm.catStationOptions1 = {
            dataTextField: "code",
            dataValueField: "id",
            select: function (e) {
                vm.selectedStation1 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.constrObj.catStationCode = dataItem.code;
                vm.constrObj.catStationId = dataItem.id;
                vm.constrObj.name = dataItem.address;
//                vm.constrObj.isStationHtct = dataItem.isStationHtct;
//                if(dataItem.isStationHtct!=null && dataItem.isStationHtct=="1"){
//                	vm.constrObj.checkHTCT = true;
//                } else {
//                	vm.constrObj.checkHTCT = false;
//                }
                constructionCode = vm.constrObj.catStationCode;
            },
            pageSize: 10,
            open: function (e) {
                vm.selectedStation1 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedDept1 = false;
                        return Restangular.all("constructionService/construction/" + 'getCatStation').post({
                            name: vm.constrObj.catStationCode,
                            pageSize: vm.catStationOptions1.pageSize,
                            type: 1
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div></div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.constrObj.catStationCode = null;// thành name
                    vm.constrObj.catStationId = null;
                    vm.constrObj.isStationHtct = null;
                }
            },
            ignoreCase: false
        }
        vm.selectedStation2 = false;
        vm.catStationOptions2 = {
            dataTextField: "code",
            dataValueField: "id",
            select: function (e) {
                vm.selectedStation2 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.constrObj.catStationCode = dataItem.code;
                vm.constrObj.catStationId = dataItem.id;
                vm.constrObj.name = dataItem.address;
//                vm.constrObj.isStationHtct = dataItem.isStationHtct;
//                if(dataItem.isStationHtct!=null && dataItem.isStationHtct=="1"){
//                	vm.constrObj.checkHTCT = true;
//                } else {
//                	vm.constrObj.checkHTCT = false;
//                }
                constructionCode = vm.constrObj.catStationCode;
            },
            pageSize: 10,
            open: function (e) {
                vm.selectedStation2 = false;
            },
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        vm.selectedDept1 = false;
                        return Restangular.all("constructionService/construction/" + 'getCatStation').post({
                            name: vm.constrObj.catStationCode,
                            pageSize: vm.catStationOptions2.pageSize,
                            type: 2
                        }).then(function (response) {
                            options.success(response);
                        }).catch(function (err) {
                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                        });
                    }
                }
            },
            template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div></div>',
            change: function (e) {
                if (e.sender.value() === '') {
                    vm.constrObj.catStationCode = null;// thành name
                    vm.constrObj.catStationId = null;
                    vm.constrObj.isStationHtct = null;
                }
            },
            ignoreCase: false
        };


        vm.getExcelTemplate = function () {
            constructionService.downloadTemplate({}).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
                return;
            });
        };



        vm.cancelPopup = cancelPopup;
        function cancelPopup() {
            $rootScope.$broadcast("handlerReturnFromPopup", "ok");
            $(".k-icon.k-i-close").click();
            kendo.ui.progress($("#constructionGrid"), false); //HienLT56 add for remove loading system - 12052021
        }
        vm.cancelImport = cancelImport;
        function cancelImport() {
            cancelPopup();
        };
        vm.onSelect = function (e) {
            if ($("#files")[0].files[0].name.split('.').pop() != 'xls' && $("#files")[0].files[0].name.split('.').pop() != 'xlsx') {
                toastr.warning("Sai định dạng file");
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
                $("#fileName").innerHTML = $("#files")[0].files[0].name
            }
        };


        function validateTab1() {
            if (vm.constrObj.catConstructionTypeId == "1" || vm.constrObj.catConstructionTypeId == "7" || vm.constrObj.catConstructionTypeId == "8") {
                if (vm.constrObj.catStationCode == null || vm.constrObj.catStationCode == '') {
                    toastr.warning("Mã trạm không được để trống");
                    return;
                }

                if (vm.constrObj.code == null || vm.constrObj.code == '') {
                    toastr.warning("Mã công trình không được để trống");
                    return;
                }
                if (vm.constrObj.name == null || vm.constrObj.name == '') {
                    toastr.warning("Tên công trình không được để trống");
                    return;
                }
                if (vm.constrObj.region == null || vm.constrObj.region == '') {
                    toastr.warning("Vùng miền không được để trống");
                    return;
                }
                if (vm.constrObj.sysGroupName == null || vm.constrObj.sysGroupName == '') {
                    toastr.warning("Đơn vị thi công không được để trống");
                    return;
                }

                if(vm.constrObj.catConstructionTypeId == "8" && !vm.constrObj.b2bB2c){
                	toastr.warning("B2B/B2C không được để trống");
                    return;
                }
                
                //hienvd: edit 9/3/2019
                if(vm.constrObj.checkHTCT == true || vm.constrObj.checkHTCT == 1){
                    var locationHTCT = vm.constrObj.locationHTCT + "";
                    var highHTCT = vm.constrObj.highHTCT + "";
                    var capexHTCT = vm.constrObj.capexHTCT + "";
                    // if(vm.constrObj.locationHTCT == null || vm.constrObj.locationHTCT == "---Chọn---" || locationHTCT == ""){
                    //     toastr.warning("Vị trí không được để trống");
                    //     return false;
                    // }
                    //
                    // if(vm.constrObj.highHTCT == null || highHTCT == ""){
                    //     toastr.warning("Chiều cao không được để trống");
                    //     return false;
                    // }
                    //
                    // if(vm.constrObj.capexHTCT == null || capexHTCT == ""){
                    //     toastr.warning("Tổng capex tạm tính không được để trống");
                    //     return false;
                    // }
                }

                //hienvd: end 9/3/2019

            } else if (vm.constrObj.catConstructionTypeId == "2") {
                    if (vm.constrObj.catStationCode == null || vm.constrObj.catStationCode == '') {
                        toastr.warning("Mã tuyến không được để trống");
                        return false;
                    }
                    if (vm.constrObj.code == null || vm.constrObj.code == '') {
                        toastr.warning("Mã công trình không được để trống");
                        return false;
                    }
                    if (vm.constrObj.name == null || vm.constrObj.name == '') {
                        toastr.warning("Tên công trình không được để trống");
                        return false;
                    }
                    if (vm.constrObj.deployType == null || vm.constrObj.deployType == '') {
                        toastr.warning("Loại tuyến không được để trống");
                        return false;
                    }
                    if (vm.constrObj.sysGroupName == null || vm.constrObj.sysGroupName == '') {
                        toastr.warning("Đơn vị thi công không được để trống");
                        return false;
                    }
                    //                hungnx 20180621 start
                    if (vm.constrObj.amount == null || vm.constrObj.amount == '') {
                        toastr.warning("Khối lượng không được bỏ trống");
                        return false;
                    } else if(kendo.parseFloat(vm.constrObj.amount)==0) {
                    	toastr.warning("Khối lượng phải lớn hơn 0");
                        return false;
                    }
                    if (vm.constrObj.price == null || vm.constrObj.price == '') {
                        toastr.warning("Giá trị không được bỏ trống");
                        return false;
                    } else if(kendo.parseFloat(vm.constrObj.price)==0) {
                    	toastr.warning("Giá trị phải lớn hơn 0");
                        return false;
                    }
//					if (vm.constrObj.amountCableLive == null || vm.constrObj.amountCableLive == '') {
//                        toastr.warning("Khối lượng cáp thi công TT không được bỏ trống");
//                        return false;
//                    }
//                    if (vm.constrObj.amountCableRent == null || vm.constrObj.amountCableRent == '') {
//                        toastr.warning("Khối lượng cáp thuê ngoài không được bỏ trống");
//                        return false;
//                    }
                    vm.replaceFormatNumber();
                    //              hungnx 20180621 end
                } else if (vm.constrObj.catConstructionTypeId == "3") {
                	
                    if (vm.constrObj.catStationCode == null || vm.constrObj.catStationCode == '') {
                        toastr.warning("Mã trạm không được để trống");
                        return false;
                    }

                    if (vm.constrObj.code == null || vm.constrObj.code == '') {
                        toastr.warning("Mã công trình không được để trống");
                        return false;
                    }
                    if (vm.constrObj.name == null || vm.constrObj.name == '') {
                        toastr.warning("Tên công trình không được để trống");
                        return false;
                    }

                    if (vm.constrObj.deployType == null || vm.constrObj.deployType == '') {
                        toastr.warning("Loại GPON không được để trống");
                        return false;
                    }
                    if (vm.constrObj.sysGroupName == null || vm.constrObj.sysGroupName == '') {
                        toastr.warning("Đơn vị thi công không được để trống");
                        return false;
                    }
                    if (vm.constrObj.amount == null || vm.constrObj.amount == '') {
                        toastr.warning("Khối lượng không được bỏ trống");
                        return false;
                    } else if(kendo.parseFloat(vm.constrObj.amount)==0) {
                    	toastr.warning("Khối lượng phải lớn hơn 0");
                        return false;
                    }
                    if (vm.constrObj.price == null || vm.constrObj.price == '') {
                        toastr.warning("Giá trị không được bỏ trống");
                        return false;
                    } else if(kendo.parseFloat(vm.constrObj.price)==0) {
                    	toastr.warning("Giá trị phải lớn hơn 0");
                        return false;
                    }
					//hoangnh 171218 start
//                    if (vm.constrObj.totalAmountChest == null || vm.constrObj.totalAmountChest == '') {
//                        toastr.warning("Tổng số tủ không được để trống");
//                        return false;
//                    }
//                    if (vm.constrObj.priceChest == null || vm.constrObj.priceChest == '') {
//                        toastr.warning("Đơn giá thi công tủ không được để trống");
//                        return false;
//                    }
//                    if (vm.constrObj.totalAmountGate == null || vm.constrObj.totalAmountGate == '') {
//                        toastr.warning("Tổng số cổng không được để trống");
//                        return false;
//                    }
//                    if (vm.constrObj.priceGate == null || vm.constrObj.priceGate == '') {
//                        toastr.warning("Đơn giá thi công cổng không được để trống");
//                        return false;
//                    }
//                    if (vm.constrObj.amountCableLive == null) {
//                        toastr.warning("Khối lượng cáp thi công TT không được để trống");
//                        return false;
//                    }
//                    if (vm.constrObj.amountCableRent == null) {
//                        toastr.warning("Khối lượng cáp thuê ngoài không được để trống");
//                        return false;
//                    }
//                    if ( vm.constrObj.amount == null ||  vm.constrObj.amount == '') {
//                        toastr.warning("Tổng khối lượng cáp toàn tuyến không được để trống");
//                        return false;
//                    }
//                    if ( vm.constrObj.price == null ||  vm.constrObj.price == '') {
//                        toastr.warning("Đơn giá thi công cáp không được để trống");
//                        return false;
//                    }
                    
                    
//                    if(vm.constrObj.amountCableLive != null){
//                    	var amountCableLive = vm.constrObj.amountCableLive.toString().replace(/,/g, '');
//                        var amount = vm.constrObj.amount.toString().replace(/,/g, '');
//                        if(parseFloat(amount) < parseFloat(amountCableLive)){
//                        	toastr.warning("Khối lượng cáp thi công TT không được lớn hơn Tổng khối lượng cáp toàn tuyến");
//                            return false;
//                        }
//                    }
//                    if(vm.constrObj.amountCableRent != null){
//                    	var amountCableRent = vm.constrObj.amountCableRent.toString().replace(/,/g, '');
//                        var amount = vm.constrObj.amount.toString().replace(/,/g, '');
//                        if(parseFloat(amount) < parseFloat(amountCableRent)){
//                        	toastr.warning("Khối lượng cáp thuê ngoài không được lớn hơn Tổng khối lượng cáp toàn tuyến");
//                            return false;
//                        }
//                    }
                    vm.replaceFormatNumber();
                    //hoangnh 171218 end
                } else if (vm.constrObj.catConstructionTypeId == "4") {
                    if (vm.constrObj.catStationCode == null || vm.constrObj.catStationCode == '') {
                        toastr.warning("Mã trạm không được để trống");
                        return false;
                    }

                    if (vm.constrObj.code == null || vm.constrObj.code == '') {
                        toastr.warning("Mã công trình không được để trống");
                        return false;
                    }
                    if (vm.constrObj.name == null || vm.constrObj.name == '') {
                        toastr.warning("Tên công trình không được để trống");
                        return false;
                    }

                    if (vm.constrObj.deployType == null || vm.constrObj.deployType == '') {
                        toastr.warning("Loại Công trình lẻ không được để trống");
                        return false;
                    }
                    if (vm.constrObj.sysGroupName == null || vm.constrObj.sysGroupName == '') {
                        toastr.warning("Đơn vị thi công không được để trống");
                        return false;
                    }
                }else if (vm.constrObj.catConstructionTypeId == "5") {
                    if (vm.constrObj.catStationCode == null || vm.constrObj.catStationCode == '') {
                        toastr.warning("Mã trạm không được để trống");
                        return false;
                    }

                    if (vm.constrObj.code == null || vm.constrObj.code == '') {
                        toastr.warning("Mã công trình không được để trống");
                        return false;
                    }
                    if (vm.constrObj.name == null || vm.constrObj.name == '') {
                        toastr.warning("Tên công trình không được để trống");
                        return false;
                    }

                    if (vm.constrObj.sysGroupName == null || vm.constrObj.sysGroupName == '') {
                        toastr.warning("Đơn vị thi công không được để trống");
                        return false;
                    }
                }else if (vm.constrObj.catConstructionTypeId == "6") {
                    if (vm.constrObj.catStationCode == null || vm.constrObj.catStationCode == '') {
                        toastr.warning("Mã trạm không được để trống");
                        return false;
                    }

                    if (vm.constrObj.code == null || vm.constrObj.code == '') {
                        toastr.warning("Mã công trình không được để trống");
                        return false;
                    }
                    if (vm.constrObj.name == null || vm.constrObj.name == '') {
                        toastr.warning("Tên công trình không được để trống");
                        return false;
                    }

                    if (vm.constrObj.sysGroupName == null || vm.constrObj.sysGroupName == '') {
                        toastr.warning("Đơn vị thi công không được để trống");
                        return false;
                    }
                }
            if (vm.constrObj.catConstructionTypeId != "2" && vm.constrObj.catConstructionTypeId != "3") {
                vm.constrObj.price = null;
                vm.constrObj.amount = null;
                vm.constrObj.moneyType = null;
            }
            if(vm.constrObj.checkDVNTTC==true && (vm.constrObj.unitConstructionName == null || vm.constrObj.unitConstructionName == "")){
            	toastr.warning("Tên đơn vị nhà thầu thi công không được để trống");
                return false;
            }
            return true;
        }
        vm.exportConstruction = exportConstruction;
        function exportConstruction(dataItem) {
            $http({
                url: RestEndpoint.BASE_SERVICE_URL + "constructionService" + "/exportConstruction",
                dataType: 'json',
                method: 'POST',
                data: dataItem,
                headers: {
                    "Content-Type": "application/json"
                },
                responseType: 'arraybuffer'// THIS IS IMPORTANT
            }).success(function (data, status, headers, config) {
                if (data.error) {
                    toastr.error(data.error);
                } else {
                    var binarydata = new Blob([data], { type: 'application/*' });
                    kendo.saveAs({ dataURI: binarydata, fileName: "HoSoCongTrinh.doc" });

                }

            })
                .catch(function (data) {
                    toastr.error("Có lỗi xảy ra!");
                });
        }

        vm.importThauPhu = function () {
            var teamplateUrl = "coms/construction/impportPopup.html";
            var title = "Import từ file excel";
            var windowId = "THUAU_PHU";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '700', 'auto', "code");
        }
        vm.getExcelTemplate = function () {
            var obj = {};
            constructionService.downloadFileImportTP(obj).then(function (d) {

                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function (e) {
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                return;
            });
        }

        vm.saveImportFileThauPhu = saveImportFileThauPhu;

        function saveImportFileThauPhu() {
            if ($("#fileImportThauPhu")[0].files[0] == null) {
                toastr.warning("Bạn chưa chọn file để import");
                return;
            }
            if ($("#fileImportThauPhu")[0].files[0].name.split('.').pop() != 'xls' && $("#fileImportThauPhu")[0].files[0].name.split('.').pop() != 'xlsx') {
                toastr.warning("Sai định dạng file");
                return;
            }
            //        	hungnx 20180618 start
            vm.disableSubmit = true;
            htmlCommonService.showLoading('#loadingIpThauPhu');
            //        	hungnx 20180618 end
            var formData = new FormData();
            formData.append('multipartFile', $('#fileImportThauPhu')[0].files[0]);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "constructionService/importFileThauPhu",
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
                    } else if (data[data.length - 1].errorFilePath != null) {
                        window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                        toastr.warning("File import không hợp lệ");
                        return;
                    }
                    else {
                        vm.checkSerial = true;
                        for (var i = 0; i < data.length; i++) {
                            for (var j = 1; j < data.length; j++) {
                                if (i != j) {
                                    if (data[i].serial == data[j].serial) {
                                        vm.checkSerial = false;
                                    }
                                }
                            }
                        }
                        if (vm.checkSerial == false) {
                            toastr.warning("File import có serial không hợp lệ");
                            return;
                        }
                        else {
                            var grid = $("#TP").data("kendoGrid");
                            if (grid != undefined) {
                                grid.dataSource.data(data);
                                grid.dataSource.sync();
                                grid.refresh();
                            }
                            toastr.success("Import dữ liệu thành công");
                            CommonService.dismissPopup1();
                        }

                    }
                }
            }).always(function () {
                vm.disableSubmit = false;
                htmlCommonService.hideLoading('#loadingIpThauPhu');
                $scope.$apply();
            });

        }
        vm.onSelectFile = onSelectFile;
        function onSelectFile() {
            if ($("#fileImportThauPhu")[0].files[0].name.split('.').pop() != 'xls' && $("#fileImportThauPhu")[0].files[0].name.split('.').pop() != 'xlsx') {
                toastr.warning("Sai định dạng file");
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
                if (104857600 < $("#fileImportThauPhu")[0].files[0].size) {
                    toastr.warning("Dung lượng file vượt quá 100MB! ");
                    return;
                }
                $("#fileName")[0].innerHTML = $("#fileImportThauPhu")[0].files[0].name
            }
        }
        //	hungnx 070618 start
        function sendFile(id, callback) {
            var files = $("#" + id)[0].files;
            if (!$("#" + id)[0].files[0]) {
                toastr.warning("Bạn chưa chọn file");
                return;
            }
            if (!htmlCommonService.checkFileExtension(id)) {
                toastr.warning("File không đúng định dạng cho phép");
                return;
            }
            var formData = new FormData();
            jQuery.each($("#" + id)[0].files, function (i, file) {
            	console.log(file);
                formData.append('multipartFile' + i, file);
            });
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "fileservice/uploadATTTInput",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    callback(data, files)
                }
            });
        }
        // callback sau khi upload file thành công
        vm.callback = callback;
        function callback(data, files) {
            for (var i = 0; i < data.length; i++) {
                var file = {};
                file.name = files[i].name;
                file.createdDate = htmlCommonService.getFullDate();
                file.createdUserName = Constant.userInfo.vpsUserToken.fullName;
                file.createdUserId = Constant.userInfo.vpsUserToken.sysUserId;
                file.filePath = data[i];
                file.type = 0;
                vm.fileLst.push(file);
            }
            var grid = !!vm.contractFileGrid ? vm.contractFileGrid : vm.contractFileEditGrid;
            refreshGrid(grid, vm.fileLst);
            //      	vm.fileLst = $("#contractFileGrid").data("kendoGrid").dataSource.data();
            vm.fileLst = grid.dataSource.data();
            $('#attachfiles').val(null);
            $('#attachfilesEdit').val(null);
        }


        vm.submitAttachFile = submitAttachFile;
        function submitAttachFile() {
            sendFile("attachfiles", callback);
        }
        vm.submitAttachFileEdit = submitAttachFileEdit;
        function submitAttachFileEdit() {
            sendFile("attachfilesEdit", callback);
        }
        function refreshGrid(grid, data) {
            grid.dataSource.data(data);
            grid.refresh();
        }
        vm.gridFileOptions = kendoConfig.getGridOptions({
            autoBind: true,
            resizable: true,
            dataSource: vm.fileLst,
            noRecords: true,
            columnMenu: false,
            scrollable: false,
            editable: true,
            messages: {
                noRecords: gettextCatalog.getString("Không có kết quả hiển thị")
            }, dataBound: function () {
                var GridDestination = $("#contractFileGrid").data("kendoGrid");
                GridDestination.pager.element.hide();
            },
            columns: [{
                title: "TT",
                field: "stt",
                template: dataItem => $("#contractFileGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
                width: 20,
                headerAttributes: {
                    style: "text-align:center;"
                },
                attributes: {
                    style: "text-align:center;"
                },
            }
                , {
                title: "Tên file",
                field: 'name',
                width: 150,
                headerAttributes: {
                    style: "text-align:center;"
                },
                attributes: {
                    style: "text-align:left;"
                },
                template: function (dataItem) {
                    return "<a href='' ng-click='caller.downloadFile(dataItem)'>" + dataItem.name + "</a>";
                }
            }, {
                title: "Ngày upload",
                field: 'createdDate',
                width: 150,
                headerAttributes: {
                    style: "text-align:center;"
                },
                attributes: {
                    "id": "appFile",
                    style: "text-align:left;"
                },
            },
            {
                title: "Người upload",
                field: 'createdUserName',
                width: 150,
                headerAttributes: {
                    style: "text-align:center;"
                },
                attributes: {
                    "id": "appFile",
                    style: "text-align:left;"
                },
            }, {
                title: "Xóa",
                template: dataItem =>
                    '<div class="text-center #=attactmentId#""> ' +
                    '<a type="button" class="#=attactmentId# icon_table" uib-tooltip="Xóa" translate> ' +
                    '<i class="fa fa-trash" ng-click=caller.removeRowFile(dataItem) ria-hidden="true"></i>' +
                    '</a>' +
                    '</div>',
                width: 20,
                field: "acctions"
            }
                ,],
        });
        vm.gridFileEditOptions = kendoConfig.getGridOptions({
            autoBind: true,
            resizable: true,
            //				dataSource: vm.fileLst,
            noRecords: true,
            columnMenu: false,
            scrollable: false,
            //				editable: true,
            reorderable: true,
            dataBinding: function () {
                record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
            },
            messages: {
                noRecords: gettextCatalog.getString("Không có kết quả hiển thị")
            },
            //				dataBound: function () {
            //					var GridDestination = $("#contractFileEditGrid").data("kendoGrid");                
            //			                GridDestination.pager.element.hide();
            //				},
            dataSource: {
                serverPaging: false,
                schema: {
                    total: function (response) {
                        return vm.fileLst.length; // total is returned in
                        // the "total" field of
                        // the response
                    },
                    data: function (response) {
                        return vm.fileLst; // data is returned in
                        // the "data" field of
                        // the response
                    }
                },
                pageSize: 10
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
            columns: [{
                title: "TT",
                field: "stt",
                template: dataItem => $("#contractFileEditGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
                width: 20,
                headerAttributes: {
                    style: "text-align:center;"
                },
                attributes: {
                    style: "text-align:center;"
                },
            }
                , {
                title: "Tên file",
                field: 'name',
                width: 150,
                headerAttributes: {
                    style: "text-align:center;"
                },
                attributes: {
                    style: "text-align:left;"
                },
                template: function (dataItem) {
                    return "<a href='' ng-click='vm.downloadFile(dataItem)'>" + dataItem.name + "</a>";
                }
            }, {
                title: "Ngày upload",
                field: 'createdDate',
                width: 150,
                headerAttributes: {
                    style: "text-align:center;"
                },
                attributes: {
                    "id": "appFile",
                    style: "text-align:left;"
                },
            },
            {
                title: "Người upload",
                field: 'createdUserName',
                width: 150,
                headerAttributes: {
                    style: "text-align:center;"
                },
                attributes: {
                    "id": "appFile",
                    style: "text-align:left;"
                },
            }, {
                title: "Xóa",
                template: dataItem =>
                    '<div class="text-center #=attactmentId#""> ' +
                    '<a type="button" class="#=attactmentId# icon_table" uib-tooltip="Xóa" translate> ' +
                    '<i class="fa fa-trash" ng-click=vm.removeRowFile(dataItem) ria-hidden="true"></i>' +
                    '</a>' +
                    '</div>',
                width: 20,
                field: "acctions"
            }
                ,],
        });
        vm.downloadFile = function (dataItem) {
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
        }
        vm.removeRowFile = removeRowFile;
        function removeRowFile(dataItem) {
            confirm('Xác nhận xóa', function () {
                var index = vm.fileLst.indexOf(dataItem);
                vm.fileLst.splice(index, 1);
                var grid = !!vm.contractFileGrid ? vm.contractFileGrid : vm.contractFileEditGrid;
                refreshGrid(grid, vm.fileLst);
            })
        }
        vm.checkNum = checkNum;
        function checkNum() {
            $("#conHighHTCT, #conCapexHTCT").keypress(function (event) {
            	var $this = $(this);
                if (!htmlCommonService.validateIntKeydown(event, $this.val())) {
                    event.preventDefault();
                }
                var text = $(this).val().replace(/,/g, "");
                if ($(this).val() == 0) {
                    text = '';
                }
                if (text.length >= 19) {
                    event.preventDefault();
                }
            });
        }
        vm.addComma = addComma;
        function addComma(model, attr) {
            vm[model][attr] = htmlCommonService.addThousandComma(vm[model][attr]);
        }
        vm.replaceFormatNumber = function () {
            if (!!vm.constrObj.price) {
                vm.constrObj.price = vm.constrObj.price.replace(/,/g, '');
            }
            if (!!vm.constrObj.amount) {
                vm.constrObj.amount = vm.constrObj.amount.replace(/,/g, '');
            }
			//hoangnh 171218 start
//            if (!!vm.constrObj.totalAmountChest) {
//                vm.constrObj.totalAmountChest = vm.constrObj.totalAmountChest.replace(/,/g, '');
//            }
//            if (!!vm.constrObj.priceChest) {
//                vm.constrObj.priceChest = vm.constrObj.priceChest.replace(/,/g, '');
//            }
//            if (!!vm.constrObj.totalAmountGate) {
//                vm.constrObj.totalAmountGate = vm.constrObj.totalAmountGate.replace(/,/g, '');
//            }
//            if (!!vm.constrObj.priceGate) {
//                vm.constrObj.priceGate = vm.constrObj.priceGate.replace(/,/g, '');
//            }
//            if (!!vm.constrObj.totalAmountGate) {
//                vm.constrObj.totalAmountGate = vm.constrObj.totalAmountGate.replace(/,/g, '');
//            }
//            if (!!vm.constrObj.priceGate) {
//                vm.constrObj.priceGate = vm.constrObj.priceGate.replace(/,/g, '');
//            }
            if (!!vm.constrObj.amountCableLive) {
            	var n = vm.constrObj.amountCableLive.toString();
            	vm.constrObj.amountCableLive = n.replace(/,/g, '');
            }
            if (!!vm.constrObj.amountCableRent) {
            	var n = vm.constrObj.amountCableRent.toString();
                vm.constrObj.amountCableRent = n.replace(/,/g, '');
            }
            //hoangnh 171218 end
        }
        //	    hungnx 070618 end

        $scope.$on("constructionEdit", function (event, dataItem) {
            vm.edit(dataItem);
        });

        function openCatProvincePopup(){
    		var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm tỉnh");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
        }

        function onSaveCatProvince(data){
            vm.constructionSearch.catProvince = data.name;
            vm.constructionSearch.catProvinceId = data.catProvinceId;
            catProvinceName = vm.constructionSearch.catProvince;
            htmlCommonService.dismissPopup();
            $("#province").focus();
	    };
	    
	    /**Hoangnh start 06032019**/
	    vm.checkNumGPon = checkNumGPon;
        function checkNumGPon() {
            $("#price, #amount,#totalAmount,#totalPrice,#amountCableLiveS,#amountCableRentS,#totalAmountChest,#priceChest,#totalAmountGate,#priceGate").keypress(function (event) {//hoangnh add #amountSandLive,#amountSandRent
            	var $this = $(this);
                if (!htmlCommonService.validateIntKeydown(event, $this.val())) {
                    event.preventDefault();
                }
                var text = $(this).val().replace(/,/g, "");
                if ($(this).val() == 0) {
                    text = '';
                }
                if (text.length >= 19) {
                    event.preventDefault();
                }
            });
        }
//        function fillDataGrid(data){
//        	var dataSource = {
//        			data:data
////        			,schema: {
////        				model: {
////        					id: "workItemGPon",
////        					fields: {
////        						stt: { editable: false },
////        						code: { editable: true },
////        						amount:{ editable: true },
////        						totalAmountChest: { editable: true },
////        						priceChest:{ editable: true },
////        						totalAmountGate: { editable: true },
////        						priceGate:{ editable: true },
////        						acctions: { editable: false },
////        					}
////        				}
////        			}
//        	}
//        	vm.workItemGPonGridOptions = kendoConfig.getGridOptions({
//                autoBind: true,
//                editable:true,
//                scrollable: false,
//                resizable: true,
//                navigatable: true, 
//                sortable: false,
////                scrollable: false,
////                columnMenu: false,
////                serverPaging: false,
//                dataBinding: function () {
//                	record= 0;
//                },
//                noRecords: true,
//                columnMenu: false,
//                pageSize: 5,
//                messages: {
//                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
//                },
//                pageable: {
//                    refresh: false,
//                    pageSizes: [5, 10, 15, 20, 25],
//                    messages: {
//                        display: "",
//                        itemsPerPage: "kết quả/trang",
//                        empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
//                    }
//                },
//                dataSource :dataSource,
////                schema: {
////                    model: {
////                        id: "workItemGPon",
////                        fields: {
////                        	stt: { editable: false },
////    						code: { editable: true },
////    						amount:{ editable: true },
////    						totalAmountChest: { editable: true },
////    						priceChest:{ editable: true },
////    						totalAmountGate: { editable: true },
////    						priceGate:{ editable: true },
////    						acctions: { editable: false },
////                        }
////                    }
////                },
//                columns: [
//                    {
//                        title: "TT",
//                        field: "stt",
//                        template: function () {
//                            return ++record;
//                        },
//                        width: '5%',
//                        columnMenu: false,
//                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
//                        attributes: {
//                            style: "text-align:center;"
//                        }
//                    },
//                    {
//                        title: "Tên note",
//                        field: 'code',
//                        width: '15%',
//                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
//                        attributes: {
//                            style: "text-align:left;"
//                        }
//                    },
//                    {
//                        title: "Tổng khối lượng cáp toàn tuyến",
//                        field: 'amount',
//                        width: '10%',
//                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
//                        attributes: {
//                            style: "text-align:left;"
//                        }
//                    },
//                    {
//                        title: "Đơn giá thi công cáp",
//                        field: 'price',
//                        width: '10%',
//                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
//                        attributes: {
//                            style: "text-align:left;"
//                        }
//                    },
//                    {
//                        title: "Tổng số tủ",
//                        field: 'totalAmountChest',
//                        width: '10%',
//                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
//                        attributes: {
//                            style: "text-align:left;"
//                        }
//                    },
//                    {
//                        title: "Đơn giá thi công tủ",
//                        field: 'priceChest',
//                        width: '10%',
//                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
//                        attributes: {
//                            style: "text-align:left;"
//                        }
//                    },
//                    {
//                        title: "Tổng số cổng",
//                        field: 'totalAmountGate',
//                        width: '10%',
//                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
//                        attributes: {
//                            style: "text-align:left;"
//                        }
//                    },{
//                        title: "Đơn giá thi công cổng",
//                        field: 'priceGate',
//                        width: '10%',
//                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
//                        attributes: {
//                            style: "text-align:left;"
//                        }
//                    }
//                    ,
////                    { command: 
////                    	[ "edit", "destroy", ], 
////                    	title: " ", 
////                    	width: "300px" 
////                    },
//                    {
//                        title: "Thao tác",
//                        field: 'actions',
//                        width: '10%',
//                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
//                        attributes: {
//                            style: "text-align:left;"
//                        },
//                        template: dataItem => 
//                        	'<div class="text-center">'
//	                        + '<button  style=" border: none; background-color: white;" id=""' +
//	                        'class="icon_table  ng-scope" ng-click="caller.updateGPon(dataItem)" translate' + '>' +
//	                        '<i class="fa fa-pencil" style="color: #337ab7;"  aria-hidden="true"></i>' +
//	                        '</button>'
//                            + '<button  style=" border: none; background-color: white;" id=""' +
//                            'class=" icon_table" ng-click="caller.removeWorkItemGPon($event)" translate' + '>' +
//                            '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
//                            '</button>'
//                            + '</div>'
//                    }
//                ]
//            });
//        }
        vm.fillDataGrid =fillDataGrid;
        function fillDataGrid(data) {
    		var dataSource = new kendo.data.DataSource({
    			data: data,
    			autoSync: false,
    			schema: {
    				model: {
    					id: "workItemGPon",
    					fields: {
    						stt: { editable: false },
        						code: { editable: true },
        						amount:{ editable: true },
        						price:{ editable: true },
        						acctions: { editable: false },
    					}
    				}
    			}
    		});
        vm.workItemGPonGridOptions = kendoConfig.getGridOptions({
    		autoBind: true,
    		resizable: true,
    		dataSource: dataSource,
    		noRecords: true,
    		columnMenu: false,
    		scrollable: false,
			dataBinding: function () {
                	record= 0;
                },
    		messages: {
    			noRecords: CommonService.translate("Không có kết quả hiển thị")
    		},
			pageable: {
                    refresh: false,
                    messages: {
                        display: "",
                        itemsPerPage: "kết quả/trang",
                        empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
			
    		columns: [{
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
                        title: "Mã hạng mục",
                        field: 'catWorkItemTypeId',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        hidden: true
                    },
                    {
                        title: "Mã công trình",
                        field: 'constructionId',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        hidden: true
                    },
                    {
                        title: "Tên hạng mục",
                        field: 'workItemName',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
					{
                        title: "Tên note",
                        field: 'code',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tên công việc",
                        field: 'taskName',
                        width: '9%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        }
                    },
                    {
                        title: "Tổng khối lượng ",
                        field: 'amount',
                        width: '9%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        format: "{0:n0}"
                    },
                    {
                        title: "Đơn giá ",
                        field: 'price',
                        width: '9%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        format: "{0:n3}"
                    },
                
                  
                    {
                        title: "Thao tác",
                        field: 'actions',
                        width: '11%',
                        headerAttributes: { style: "text-align:center;font-weight: bold;" },
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: dataItem => 
                        	'<div class="text-center">'
//	                        + '<button  style=" border: none; background-color: white;" id=""' +
//	                        'class="icon_table  ng-scope" ng-click="caller.updateGPon(dataItem)" translate' + '>' +
//	                        '<i class="fa fa-pencil" style="color: #337ab7;"  aria-hidden="true"></i>' +
//	                        '</button>'
                            + '<button  style=" border: none; background-color: white;" id=""' +
                            'class=" icon_table" ng-click="caller.removeWorkItemGPon($event)" translate' + '>' +
                            '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
                            '</button>'
                            + '</div>'
                    }
					]
    	});
        }
        
        vm.addWorkItemGPon = addWorkItemGPon;
        function addWorkItemGPon(){
//        	if(vm.workItemRecord.nodeCode == "" || vm.workItemRecord.nodeCode == null){
//        		toastr.warning("Tên Node không được để trống");
//        		$("#noteCode").focus();
//        		return;
//        	}
        	
        	if(vm.workItemRecord.workItemTypeList == ""){
            	toastr.warning("Tên hạng mục không được để trống");
            	return;
        	}
        	if(vm.workItemRecord.workItemTypeList.length > 1){
        		toastr.warning("Chỉ được chọn một hạng mục.");
            	return;
        	}
        	
        	var object = [];
        	var obj = {};
        	obj.amount = vm.workItemRecord.amount;
        	obj.price = vm.workItemRecord.price;
        	obj.totalAmountChest = vm.workItemRecord.totalAmountChest;
        	obj.priceChest = vm.workItemRecord.priceChest;
        	obj.totalAmountGate = vm.workItemRecord.totalAmountGate ;
        	obj.priceGate = vm.workItemRecord.priceGate;
        	obj.constructorId = vm.workItemRecord.constructorId;
        	obj.constructorName = vm.workItemRecord.constructorName1;
        	obj.isInternal = vm.workItemRecord.isInternal;
        	obj.supervisorId = vm.workItemRecord.supervisorId;
//        	obj.nodeCode = vm.workItemRecord.nodeCode;
        	for(var i=0; i < vm.workItemRecord.workItemTypeList.length; i++){
        		var workItemTypeCode = vm.workItemRecord.workItemTypeList[i].code;
        		obj.name = vm.workItemRecord.workItemTypeList[i].name ;
        		$scope.objectName = obj.name ;
        		obj.catWorkItemTypeId = vm.workItemRecord.workItemTypeList[i].id;
        		$scope.objectCatWorkItemTypeId = obj.catWorkItemTypeId ;
        	}
        	obj.workItemTypeList = vm.workItemRecord.workItemTypeList;
        	obj.constructionId = vm.workItemSearch.constructionId;
        	obj.code = vm.workItemGPon.constructionCode + '_'+ workItemTypeCode ;
        	
        	
        	
        	if(vm.workItemRecord.keoCap96 != null && vm.workItemRecord.donGiaKeoCap96){
        		var childObject = {};
        		childObject.workItemName = $scope.objectName;
        		childObject.catWorkItemTypeId = $scope.objectCatWorkItemTypeId;
        		childObject.code =  vm.workItemGPon.constructionCode + '_'+ workItemTypeCode ;
        		childObject.taskName = 'Kéo cáp 96';
        		childObject.amount = vm.workItemRecord.keoCap96;
        		childObject.price = vm.workItemRecord.donGiaKeoCap96;
        		childObject.constructionId = vm.workItemSearch.constructionId;
        		object.push(childObject);
        	}
        	
        	if(vm.workItemRecord.keoCap48 != null && vm.workItemRecord.donGiaKeoCap48){
        		var childObject = {};
        		childObject.workItemName = $scope.objectName;
        		childObject.catWorkItemTypeId = $scope.objectCatWorkItemTypeId;
        		childObject.code =  vm.workItemGPon.constructionCode + '_'+ workItemTypeCode ;
        		childObject.taskName = 'Kéo cáp 48';
        		childObject.amount = vm.workItemRecord.keoCap48;
        		childObject.price = vm.workItemRecord.donGiaKeoCap48;
        		childObject.constructionId = vm.workItemSearch.constructionId;
        		object.push(childObject);
        	}
        	if(vm.workItemRecord.keoCap24 != null && vm.workItemRecord.donGiaKeoCap24){
        		var childObject = {};
        		childObject.workItemName = $scope.objectName;
        		childObject.catWorkItemTypeId = $scope.objectCatWorkItemTypeId;
        		childObject.code =  vm.workItemGPon.constructionCode + '_'+ workItemTypeCode ;
        		childObject.taskName = 'Kéo cáp 24';
        		childObject.amount = vm.workItemRecord.keoCap24;
        		childObject.price = vm.workItemRecord.donGiaKeoCap24;
        		childObject.constructionId = vm.workItemSearch.constructionId;
        		object.push(childObject);
        	}
        	if(vm.workItemRecord.keoCap12 != null && vm.workItemRecord.donGiaKeoCap12){
        		var childObject = {};
        		childObject.workItemName = $scope.objectName;
        		childObject.catWorkItemTypeId = $scope.objectCatWorkItemTypeId;
        		childObject.code =  vm.workItemGPon.constructionCode + '_'+ workItemTypeCode ;
        		childObject.taskName = 'Kéo cáp 12';
        		childObject.amount = vm.workItemRecord.keoCap12;
        		childObject.price = vm.workItemRecord.donGiaKeoCap12;
        		childObject.constructionId = vm.workItemSearch.constructionId;
        		object.push(childObject);
        	}
        	if(vm.workItemRecord.keoCap8 != null && vm.workItemRecord.donGiaKeoCap8){
        		var childObject = {};
        		childObject.workItemName = $scope.objectName;
        		childObject.catWorkItemTypeId = $scope.objectCatWorkItemTypeId;
        		childObject.code =  vm.workItemGPon.constructionCode + '_'+ workItemTypeCode ;
        		childObject.taskName = 'Kéo cáp 8';
        		childObject.amount = vm.workItemRecord.keoCap8;
        		childObject.price = vm.workItemRecord.donGiaKeoCap8;
        		childObject.constructionId = vm.workItemSearch.constructionId;
        		object.push(childObject);
        	}
        	if(vm.workItemRecord.keoCap4 != null && vm.workItemRecord.donGiaKeoCap4){
        		var childObject = {};
        		childObject.workItemName = $scope.objectName;
        		childObject.catWorkItemTypeId = $scope.objectCatWorkItemTypeId;
        		childObject.code =  vm.workItemGPon.constructionCode + '_'+ workItemTypeCode ;
        		childObject.taskName = 'Kéo cáp 4';
        		childObject.amount = vm.workItemRecord.keoCap4;
        		childObject.price = vm.workItemRecord.donGiaKeoCap4;
        		childObject.constructionId = vm.workItemSearch.constructionId;
        		object.push(childObject);
        	}
        	if(vm.workItemRecord.tuHop != null && vm.workItemRecord.donGiaTuHop){
        		var childObject = {};
        		childObject.workItemName = $scope.objectName;
        		childObject.catWorkItemTypeId = $scope.objectCatWorkItemTypeId;
        		childObject.code =  vm.workItemGPon.constructionCode + '_'+ workItemTypeCode ;
        		childObject.taskName = 'Lắp đặt tủ hộp';
        		childObject.amount = vm.workItemRecord.tuHop;
        		childObject.price = vm.workItemRecord.donGiaTuHop;
        		childObject.constructionId = vm.workItemSearch.constructionId;
        		object.push(childObject);
        	}
        	if(vm.workItemRecord.boChia != null && vm.workItemRecord.donGiaBoChia){
        		var childObject = {};
        		childObject.workItemName = $scope.objectName;
        		childObject.catWorkItemTypeId = $scope.objectCatWorkItemTypeId;
        		childObject.code =  vm.workItemGPon.constructionCode + '_'+ workItemTypeCode ;
        		childObject.taskName = 'Hàn nối bộ chia';
        		childObject.amount = vm.workItemRecord.boChia;
        		childObject.price = vm.workItemRecord.donGiaBoChia;
        		childObject.constructionId = vm.workItemSearch.constructionId;
        		object.push(childObject);
        	}
        	if(vm.workItemRecord.mangXong != null && vm.workItemRecord.donGiaMangXong){
        		var childObject = {};
        		childObject.workItemName = $scope.objectName;
        		childObject.catWorkItemTypeId = $scope.objectCatWorkItemTypeId;
        		childObject.code =  vm.workItemGPon.constructionCode + '_'+ workItemTypeCode ;
        		childObject.taskName = 'Hàn nối măng xông';
        		childObject.amount = vm.workItemRecord.mangXong;
        		childObject.price = vm.workItemRecord.donGiaMangXong;
        		childObject.constructionId = vm.workItemSearch.constructionId;
        		object.push(childObject);
        	}
        	if(vm.workItemRecord.odf != null && vm.workItemRecord.donGiaOdf){
        		var childObject = {};
        		childObject.workItemName = $scope.objectName;
        		childObject.catWorkItemTypeId = $scope.objectCatWorkItemTypeId;
        		childObject.code =  vm.workItemGPon.constructionCode + '_'+ workItemTypeCode ;
        		childObject.taskName = 'Hàn nối odf';
        		childObject.amount = vm.workItemRecord.odf;
        		childObject.price = vm.workItemRecord.donGiaOdf;
        		childObject.constructionId = vm.workItemSearch.constructionId;
        		object.push(childObject);
        	}
        	if(vm.workItemRecord.cot7m != null && vm.workItemRecord.donGiaCot7m){
        		var childObject = {};
        		childObject.workItemName = $scope.objectName;
        		childObject.catWorkItemTypeId = $scope.objectCatWorkItemTypeId;
        		childObject.code =  vm.workItemGPon.constructionCode + '_'+ workItemTypeCode ;
        		childObject.taskName = 'Trồng cột 7m';
        		childObject.amount = vm.workItemRecord.cot7m;
        		childObject.price = vm.workItemRecord.donGiaCot7m;
        		childObject.constructionId = vm.workItemSearch.constructionId;
        		object.push(childObject);
        	}
        	if(vm.workItemRecord.cot8m != null && vm.workItemRecord.donGiaCot8m){
        		var childObject = {};
        		childObject.workItemName = $scope.objectName;
        		childObject.catWorkItemTypeId = $scope.objectCatWorkItemTypeId;
        		childObject.code =  vm.workItemGPon.constructionCode + '_'+ workItemTypeCode ;
        		childObject.taskName = 'Trồng cột 8m';
        		childObject.amount = vm.workItemRecord.cot8m;
        		childObject.price = vm.workItemRecord.donGiaCot8m;
        		childObject.constructionId = vm.workItemSearch.constructionId;
        		object.push(childObject);
        	}
        	if(vm.workItemRecord.cot10m != null && vm.workItemRecord.donGiaCot10m){
        		var childObject = {};
        		childObject.workItemName = $scope.objectName;
        		childObject.catWorkItemTypeId = $scope.objectCatWorkItemTypeId;
        		childObject.code =  vm.workItemGPon.constructionCode + '_'+ workItemTypeCode ;
        		childObject.taskName = 'Trồng cột 10m';
        		childObject.amount = vm.workItemRecord.cot10m;
        		childObject.price = vm.workItemRecord.donGiaCot10m;
        		childObject.constructionId = vm.workItemSearch.constructionId;
        		object.push(childObject);
        	}
        	if(vm.workItemRecord.cot12m != null && vm.workItemRecord.donGiaCot12m){
        		var childObject = {};
        		childObject.workItemName = $scope.objectName;
        		childObject.catWorkItemTypeId = $scope.objectCatWorkItemTypeId;
        		childObject.code =  vm.workItemGPon.constructionCode + '_'+ workItemTypeCode ;
        		childObject.taskName = 'Trồng cột 12m';
        		childObject.amount = vm.workItemRecord.cot12m;
        		childObject.price = vm.workItemRecord.donGiaCot12m;
        		childObject.constructionId = vm.workItemSearch.constructionId;
        		object.push(childObject);
        	}
        	
        	for(var k=0; k < vm.lstWorkItemGPon.length ; k++){
        		if(vm.lstWorkItemGPon[k].code == obj.code){
        			toastr.warning("Tên node đã đã tồn tại trên lưới.");
        			$("#nodeCode").focus();
        			return;
        		}
        	}
        	var dataGrid = $("#workItemGPon").data().kendoGrid.dataSource.view()
        	for(var i = 0 ; i < dataGrid.length ; i ++){
        		for(var j = 0 ; j < object.length ; j ++){
        			if(dataGrid[i].taskName == object[j].taskName){
        				toastr.warning("Tên công việc '"+object[j].taskName+"' đã đã tồn tại trên lưới.");
            			return;
        			}
        		}
        	}
        	
        	for(var i = 0 ; i < object.length ; i ++){
        		var grid = $("#workItemGPon").data("kendoGrid");
    			grid.dataSource.insert(object[i]);
        	}
        	$scope.workItemGpon = obj;
//        	vm.workItemRecord.nodeCode ="";
        	vm.workItemRecord.amount ="";
        	vm.workItemRecord.price ="";
        	vm.workItemRecord.totalAmountChest ="";
        	vm.workItemRecord.priceChest ="";
        	vm.workItemRecord.totalAmountGate ="";
        	vm.workItemRecord.priceGate ="";
        	vm.workItemRecord.workItemTypeList =[];
        	vm.clearGpon();
        }
        
        vm.clearGpon = clearGpon;
        function clearGpon(){
        	vm.workItemRecord.keoCap96 = null;
        	vm.workItemRecord.donGiaKeoCap96 = null;
        	
        	vm.workItemRecord.keoCap48 = null;
        	vm.workItemRecord.donGiaKeoCap48 = null;
        	
        	vm.workItemRecord.keoCap24 = null;
        	vm.workItemRecord.donGiaKeoCap24 = null;
        	
        	vm.workItemRecord.keoCap12 = null;
        	vm.workItemRecord.donGiaKeoCap12 = null;
        	
        	vm.workItemRecord.keoCap8 = null;
        	vm.workItemRecord.donGiaKeoCap8 = null;
        	
        	vm.workItemRecord.keoCap4 = null;
        	vm.workItemRecord.donGiaKeoCap4 = null;
        	
        	vm.workItemRecord.tuHop = null;
        	vm.workItemRecord.donGiaTuHop = null;

        	vm.workItemRecord.boChia = null;
        	vm.workItemRecord.donGiaBoChia = null;
        	
        	vm.workItemRecord.mangXong = null;
        	vm.workItemRecord.donGiaMangXong = null;
        	
        	vm.workItemRecord.odf = null;
        	vm.workItemRecord.donGiaOdf = null;
        	
        	vm.workItemRecord.cot7m = null;
        	vm.workItemRecord.donGiaCot7m = null;
        	
        	vm.workItemRecord.cot8m = null;
        	vm.workItemRecord.donGiaCot8m = null;
        	
        	vm.workItemRecord.cot10m = null;
        	vm.workItemRecord.donGiaCot10m = null;
        	
        	vm.workItemRecord.cot12m = null;
        	vm.workItemRecord.donGiaCot12m = null;
        } 
        
        vm.editorkItemGPon = editorkItemGPon;
        function editorkItemGPon(){
        	if(vm.workItemRecord.nodeCode == "" || vm.workItemRecord.nodeCode == null){
        		toastr.warning("Tên Node không được để trống");
        		$("#noteCode").focus();
        		return;
        	}
        	if(vm.workItemRecord.amount == "" || vm.workItemRecord.amount == null){
        		$("#totalAmount").focus();
        		toastr.warning("Tổng khối lượng cáp toàn tuyến không được để trống");
        		return;
        	}
        	if(vm.workItemRecord.price == "" || vm.workItemRecord.price == null){
        		toastr.warning("Đơn giá thi công cáp không được để trống");
        		$("#priceGPon").focus();
        		return;
        	}
        	if(vm.workItemRecord.totalAmountChest == "" || vm.workItemRecord.totalAmountChest == null){
        		toastr.warning("Tổng số tủ không được để trống");
        		$("#totalAmountChest").focus();
        		return;
        	}
        	if(vm.workItemRecord.priceChest == "" || vm.workItemRecord.priceChest == null){
        		toastr.warning("Đơn giá thi công tủ không được để trống");
        		$("#priceChest").focus();
        		return;
        	}
        	if(vm.workItemRecord.totalAmountGate == "" || vm.workItemRecord.totalAmountGate == null){
        		toastr.warning("Tổng số cổng không được để trống");
        		$("#totalAmountGate").focus();
        		return;
        	}
        	if(vm.workItemRecord.priceGate == "" || vm.workItemRecord.priceGate == null){
        		toastr.warning("Đơn giá thi công cổng không được để trống");
        		$("#priceGate").focus();
        		return;
        	}
        	if (!!vm.workItemRecord.amount) {
                vm.workItemRecord.amount = vm.workItemRecord.amount.toString().replace(/,/g, '');
            }
        	if (!!vm.workItemRecord.price) {
                vm.workItemRecord.price = vm.workItemRecord.price.toString().replace(/,/g, '');
            }
        	if (!!vm.workItemRecord.totalAmountChest) {
                vm.workItemRecord.totalAmountChest = vm.workItemRecord.totalAmountChest.toString().replace(/,/g, '');
            }
        	if (!!vm.workItemRecord.priceChest) {
                vm.workItemRecord.priceChest = vm.workItemRecord.priceChest.toString().replace(/,/g, '');
            }
        	if (!!vm.workItemRecord.totalAmountGate) {
                vm.workItemRecord.totalAmountGate = vm.workItemRecord.totalAmountGate.toString().replace(/,/g, '');
            }
        	if (!!vm.workItemRecord.priceGate) {
                vm.workItemRecord.priceGate = vm.workItemRecord.priceGate.toString().replace(/,/g, '');
            }
        	if(vm.lstWorkItemGPon.length > 1){
        		toastr.warning("Bạn chỉ được thêm 1 bản ghi.");
        		return;
        	}
        	for(var i=0 ;i< vm.lstWorkItemGPon.length; i++){
        		var obj = {};
        		obj.amount = vm.workItemRecord.amount;
            	obj.price = vm.workItemRecord.price;
            	obj.totalAmountChest = vm.workItemRecord.totalAmountChest;
            	obj.priceChest = vm.workItemRecord.priceChest;
            	obj.totalAmountGate = vm.workItemRecord.totalAmountGate ;
            	obj.priceGate = vm.workItemRecord.priceGate;
            	obj.constructorId = vm.workItemRecord.constructorId;
            	obj.constructorName = vm.workItemRecord.constructorName1;
            	obj.isInternal = vm.workItemRecord.isInternal;
            	obj.supervisorId = vm.workItemRecord.supervisorId;
            	obj.nodeCode = vm.workItemRecord.nodeCode;
            	obj.constructionId = vm.workItemSearch.constructionId;
            	obj.code = vm.workItemRecord.code;
            	obj.name = vm.workItemRecord.name;
            	obj.workItemId = vm.workItemId;
        		vm.lstWorkItemGPon.splice(i, 1);
        		vm.lstWorkItemGPon.push(obj);
            	fillDataGrid(vm.lstWorkItemGPon);
            	setTimeout(function () {
                    $("#workItemGPon").data("kendoGrid").dataSource.read();
                }, 15);
        	}
        	
        }
        
        function validInput(){
        	if(vm.workItemRecord.nodeCode == "" || vm.workItemRecord.nodeCode == null){
        		toastr.warning("Tên Node không được để trống");
        		$("#noteCode").focus();
        		return;
        	}
        	if(vm.workItemRecord.amount == "" || vm.workItemRecord.amount == null){
        		$("#totalAmount").focus();
        		toastr.warning("Tổng khối lượng cáp toàn tuyến không được để trống");
        		return;
        	}
        	if(vm.workItemRecord.price == "" || vm.workItemRecord.price == null){
        		toastr.warning("Đơn giá thi công cáp không được để trống");
        		$("#priceGPon").focus();
        		return;
        	}
        	if(vm.workItemRecord.totalAmountChest == "" || vm.workItemRecord.totalAmountChest == null){
        		toastr.warning("Tổng số tủ không được để trống");
        		$("#totalAmountChest").focus();
        		return;
        	}
        	if(vm.workItemRecord.priceChest == "" || vm.workItemRecord.priceChest == null){
        		toastr.warning("Đơn giá thi công tủ không được để trống");
        		$("#priceChest").focus();
        		return;
        	}
        	if(vm.workItemRecord.totalAmountGate == "" || vm.workItemRecord.totalAmountGate == null){
        		toastr.warning("Tổng số cổng không được để trống");
        		$("#totalAmountGate").focus();
        		return;
        	}
        	if(vm.workItemRecord.priceGate == "" || vm.workItemRecord.priceGate == null){
        		toastr.warning("Đơn giá thi công cổng không được để trống");
        		$("#priceGate").focus();
        		return;
        	}
        	if (!!vm.workItemRecord.amount) {
                vm.workItemRecord.amount = vm.workItemRecord.amount.toString().replace(/,/g, '');
            }
        	if (!!vm.workItemRecord.price) {
                vm.workItemRecord.price = vm.workItemRecord.price.toString().replace(/,/g, '');
            }
        	if (!!vm.workItemRecord.totalAmountChest) {
                vm.workItemRecord.totalAmountChest = vm.workItemRecord.totalAmountChest.toString().replace(/,/g, '');
            }
        	if (!!vm.workItemRecord.priceChest) {
                vm.workItemRecord.priceChest = vm.workItemRecord.priceChest.toString().replace(/,/g, '');
            }
        	if (!!vm.workItemRecord.totalAmountGate) {
                vm.workItemRecord.totalAmountGate = vm.workItemRecord.totalAmountGate.toString().replace(/,/g, '');
            }
        	if (!!vm.workItemRecord.priceGate) {
                vm.workItemRecord.priceGate = vm.workItemRecord.priceGate.toString().replace(/,/g, '');
            }
        }
        
        vm.removeWorkItemGPon = removeWorkItemGPon;
        function removeWorkItemGPon(e) {
        	confirm('Bạn có chắc chắn muốn xóa?', function(){
        		var grid = vm.workItemGPonGrid;
                var row = $(e.target).closest("tr");
                var dataItem = grid.dataItem(row);
                grid.removeRow(dataItem);
                grid.dataSource.remove(dataItem);
                grid.dataSource.sync();
                grid.refresh();
                for(var i =0; i< vm.lstWorkItemGPon.length ;i++){
                	if(vm.lstWorkItemGPon[i].nodeCode == dataItem.nodeCode){
                		vm.lstWorkItemGPon.splice(i, 1);
                	}
                }
        	})
        }
//        vm.updateGPon = updateGPon;
//        function updateGPon(dataItem){
//        	vm.checkNode = false;
//        	vm.workItemRecord.nodeCode = dataItem.nodeCode;
//        	vm.workItemRecord.amount = dataItem.amount;
//        	vm.workItemRecord.price = dataItem.price;
//        	vm.workItemRecord.totalAmountChest = dataItem.totalAmountChest;
//        	vm.workItemRecord.priceChest = dataItem.priceChest;
//        	vm.workItemRecord.totalAmountGate = dataItem.totalAmountGate;
//        	vm.workItemRecord.priceGate = dataItem.priceGate;
//        	vm.workItemRecord.workItemTypeList = dataItem.workItemTypeList[0];
//        	vm.workItemRecord.selected =true;
//        }
        
        vm.saveWorkItemGPon = saveWorkItemGPon;
        function saveWorkItemGPon(){
//        	var data = $("#workItemGPon").data("kendoGrid").dataSource.view()
        	if(data.length < 1){
        		toastr.warning("Không có dữ liệu trên lưới.");
        		return;
        	}
        	var obj = {}
//        	obj.listGpon = data;
        	obj = angular.copy(vm.workItemRecord);
        	obj.workItemGpon = $scope.workItemGpon;
        	obj.workItemTypeList = vm.workItemRecord.workItemTypeList;
        	kendo.ui.progress($("#formWork"), true);
        	obj.constructionId=vm.constrObj.constructionId;
            obj.constructionCode=vm.constrObj.code;
        	constructionService.addWorkItemGPon(obj).then(function (data) {
        		if (data!=undefined && data.error) {
        			kendo.ui.progress($("#formWork"), false);
                    toastr.error(data.error);
                    return;
                }
        		kendo.ui.progress($("#formWork"), false);
                toastr.success("Thêm mới dữ liệu thành công.");
                CommonService.dismissPopup();
                $scope.vm.doSearchWorkItem();
                $("#workItemTable").data('kendoGrid').dataSource.read();
                $("#workItemTable").data('kendoGrid').refresh();
        	}, function (e) {
                toastr.error("Có lỗi trong quá trình xử lý dữ liệu.");
            });
        }
        
        vm.editWorkItemGPon = editWorkItemGPon;
        function editWorkItemGPon(){
        	if(vm.lstWorkItemGPon.length < 1){
        		toastr.warning("Không có dữ liệu trên lưới.");
        		return;
        	}
        	var obj = {}
        	obj.lstWorkItemGPon = vm.lstWorkItemGPon;
        	constructionService.addWorkItemGPon(obj).then(function (data) {
        		if (data.error) {
                    toastr.error(data.error);
                    return;
                }
                toastr.success("Cập nhật thành công.");
                CommonService.dismissPopup();
                $scope.vm.doSearchWorkItem();
                vm.checkNode = true;
                $("#workItemTable").data('kendoGrid').dataSource.read();
                $("#workItemTable").data('kendoGrid').refresh();
        	}, function (e) {
                toastr.error("Có lỗi trong quá trình xử lý dữ liệu.");
            });
        }
        
        function change_alias(alias) {
            var str = alias;
            str = str.toLowerCase();
            str = str.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g,"a"); 
            str = str.replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g,"e"); 
            str = str.replace(/ì|í|ị|ỉ|ĩ/g,"i"); 
            str = str.replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g,"o"); 
            str = str.replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g,"u"); 
            str = str.replace(/ỳ|ý|ỵ|ỷ|ỹ/g,"y"); 
            str = str.replace(/đ/g,"d");
            str = str.replace(/!|@|%|\^|\*|\(|\)|\+|\=|\<|\>|\?|\/|,|\.|\:|\;|\'|\"|\&|\#|\[|\]|~|\$|_|`|-|{|}|\||\\/g," ");
            str = str.replace(/ + /g," ");
            str = str.trim(); 
            return str.toUpperCase();
        }
        /**Hoangnh end 06032019**/

        //hienvd: Start 5-8-2019
        vm.constrObj.checkHTCT = false;
        vm.selectCheckRental = selectCheckRental;
        function selectCheckRental() {
            if(vm.constrObj.checkHTCT == true){
                document.getElementById("location").style.visibility = "visible";
                document.getElementById("highHTCT").style.visibility = "visible";
                document.getElementById("capexHTCT").style.visibility = "visible";
//                document.getElementById("projectCode").style.visibility = "visible";
            }else {
                document.getElementById("location").style.visibility = "hidden";
                document.getElementById("highHTCT").style.visibility = "hidden";
                document.getElementById("capexHTCT").style.visibility = "hidden";
//                document.getElementById("projectCode").style.visibility = "hidden";
                // vm.constrObj.locationHTCT = null;
                // vm.constrObj.highHTCT = null;
                // vm.constrObj.capexHTCT = null;
            }
        }

        vm.selectCheckRentalPopup = selectCheckRentalPopup;
        function selectCheckRentalPopup() {
            if(vm.constrObj.checkHTCT == true){
                document.getElementById("locationPopup").style.visibility = "visible";
                document.getElementById("highHTCTPopup").style.visibility = "visible";
            }else {
                document.getElementById("locationPopup").style.visibility = "hidden";
                document.getElementById("highHTCTPopup").style.visibility = "hidden";
                // vm.constrObj.locationHTCT = null;
                // vm.constrObj.highHTCT = null;
                // vm.constrObj.capexHTCT = null;
            }

        }

        vm.checkNum = checkNum;
        function checkNum(){
            $("#conHighHTCT, #conCapexHTCT, #highHTCT, #capexHTCT").keypress(function(event) {
                var $this = $(this);
                if (!htmlCommonService.validateIntKeydown(event, $this.val())) {
                    event.preventDefault();
                }
                var text = $(this).val().replace(/,/g, "");;
                if(text.length>=19){
                    event.preventDefault();
                }
            });
        }

        //thêm dấu phẩy phân cách hàng nghìn
        vm.addComma = addComma;
        function addComma(model, attr){
            vm[model][attr] = htmlCommonService.addThousandComma(vm[model][attr]);
        }

        //hienvd: End 5-8-2019
        
        //Huypq-20190910-start
        vm.openCatStationHousePopup= function() {
        	var templateUrl = 'coms/popup/catStationSearchPopUp.html';
        	var title = gettextCatalog.getString("Tìm kiếm mã trạm");
        	htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catStationHouseSearchController');
        }

        vm.onSaveCatStation = function(dataItem){
        	vm.constructionSearch.catStationHouseCode = dataItem.catStationHouseCode;
            vm.constructionSearch.catStationHouseId = dataItem.catStationHouseId;
        }
        
        vm.isSelect = false;
        vm.stationHouseCodeOptions = {
                dataTextField: "catStationHouseCode",
                dataValueField: "catStationHouseId",
                placeholder: "Nhập mã nhà trạm",
                select: function (e) {
                    vm.isSelect = true;
                    var dataItem = this.dataItem(e.item.index());
                    vm.constructionSearch.catStationHouseCode = dataItem.catStationHouseCode;
                    vm.constructionSearch.catStationHouseId = dataItem.catStationHouseId;
                },
                open: function (e) {
                    vm.isSelect = false;
                },
                pageSize: 10,
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function (options) {
                            vm.isSelect = false;
                            return Restangular.all("constructionService/getStationForAutoCompleteHouse").post({
                                keySearch: vm.constructionSearch.catStationHouseCode,
                                pageSize: vm.stationHouseCodeOptions.pageSize,
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
                    '<p class="col-md-12 text-header-auto border-right-ccc">Mã nhà trạm</p>' +
                    '</div>',
                template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.catStationHouseCode #</div></div>',
                change: function (e) {
                    if (!vm.isSelect) {
                        vm.constructionSearch.catStationHouseCode = null;
                        vm.constructionSearch.catStationHouseId = null;
                    }
                },
                close: function (e) {
                    if (!vm.isSelect) {
                        vm.constructionSearch.catStationHouseCode = null;
                        vm.constructionSearch.catStationHouseId = null;
                    }
                }
            }
        //Huy-end
        
        //Huypq-23022021-start
        vm.importSystemSolar = importSystemSolar;
        function importSystemSolar() {
            vm.fileImportData = false;
            var teamplateUrl = "coms/construction/importSystemSolar.html";
            var title = "Import gán mã hệ thống ĐMT";
            var windowId = "IMPORT_SYSTEM_SOLAR";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '1000', '275', null);
        }
        
        vm.getConstructionExcelTemplateSolar = function () {
        	var obj = {};
            constructionService.downloadFileImportSolar(obj).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi export!"));
                return;
            });
        }
        
        vm.submitSystemSolar = submitSystemSolar;
        function submitSystemSolar(data) {
            $('#testSubmit').addClass('loadersmall');
            vm.disableSubmit = true;
            if (!vm.fileImportData) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Bạn chưa chọn file để import");
                return;
            }
            if ($('.k-invalid-msg').is(':visible')) {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                return;
            }
            if (vm.fileImportData.name.split('.').pop() !== 'xls' && vm.fileImportData.name.split('.').pop() !== 'xlsx') {
                $('#testSubmit').removeClass('loadersmall');
                vm.disableSubmit = false;
                toastr.warning("Sai định dạng file");
                return;
            }
            var formData = new FormData();
            formData.append('multipartFile', vm.fileImportData);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "constructionService/importSystemSolar?folder=temp",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if (data == 'NO_CONTENT') {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.warning("File import không có nội dung");
                    }
                    else if (!!data.error) {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.error(data.error);
                    }
                    else if (data.length == 1 && !!data[data.length - 1].errorList && data[data.length - 1].errorList.length > 0) {
                        vm.lstErrImport = data[data.length - 1].errorList;
                        vm.objectErr = data[data.length - 1];
                        var templateUrl = "wms/createImportRequirementManagement/importResultPopUp.html";
                        var title = "Kết quả Import";
                        var windowId = "ERR_IMPORT";
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        CommonService.populatePopupCreate(templateUrl, title, vm.lstErrImport, vm, windowId, false, '80%', '420px');
                        fillDataImportErrTable(vm.lstErrImport);

                    }
                    else {
                        $('#testSubmit').removeClass('loadersmall');
                        vm.disableSubmit = false;
                        toastr.success("Import thành công");
                        CommonService.dismissPopup1();
                        doSearchContruction();
                    }
                    $scope.$apply();
                }
            });
        }
        //Huy-end
//      taotq start 01072022
        vm.approve = approve;
        function approve(dataItem) {
                confirm('Xác nhận duyệt hoàn thành công trình đã chọn?', function () {
                    constructionService.approve(dataItem).then(
                        function (da) {
                            var sizePage = $("#constructionGrid").data("kendoGrid").dataSource.total();
                            var pageSize = $("#constructionGrid").data("kendoGrid").dataSource.pageSize();
                            if (sizePage % pageSize === 1) {
                                var currentPage = $("#constructionGrid").data("kendoGrid").dataSource.page();
                                if (currentPage > 1) {
                                    $("#constructionGrid").data("kendoGrid").dataSource.page(currentPage - 1);
                                }
                            }
                            $("#constructionGrid").data('kendoGrid').dataSource.read();
                            $("#constructionGrid").data('kendoGrid').refresh();
                            toastr.success("Phê duyệt thành công!");
                        }, function (errResponse) {
                            toastr.error("Lỗi không phê duyệt được!");
                        });
                });
        }
//        taotq end 01072022
    }
})();
