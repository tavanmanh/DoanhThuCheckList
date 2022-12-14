(function() {
    'use strict';

    var controllerId = 'groundhandoverController';

    angular.module('MetronicApp').controller(controllerId, groundhandoverController);

    /* @ngInject */
    function groundhandoverController($scope, RestEndpoint, $rootScope, $timeout,
        PopupConst, Constant, kendoConfig, gettextCatalog, $kWindow,
        CommonService, $q, groundhandoverservices, ProposalEvaluation, theApproval) {
        var vm = this;
        vm.validatorOptions = kendoConfig.get('validatorOptions');
        vm.focus = false;
        vm.save = save;
        vm.showDetail = false;
        vm.disabledPheDuyet = false;
        vm.hideDelete = false;
        vm.hideSave = false;
        vm.disableCreatOne = false;
        vm.disableCreatTwo = false;
        vm.disableAll = false;
        // vm.dopheDuyet = dopheDuyet;
        vm.add = add;
        vm.showGrid = showGrid;
        vm.remove = remove;
        vm.exportFile = exportFile;
        vm.ExportDOC = ExportDOC;
        vm.onChange = onChange;
        // vm.multiDelete =multiDelete;
        vm.chkSelectAll = chkSelectAll;
        vm.listDataFillTable = [];
        vm.isCreateNew = false;
        vm.showHandoverDate = false;
        vm.data = {};
        vm.item = {};
        if(ProposalEvaluation.getItem()!=null){
        vm.item.constructId = ProposalEvaluation.getItem().constructId;
        vm.item.constrtCode = ProposalEvaluation.getItem().constrtCode;
        vm.item.constrtName = ProposalEvaluation.getItem().constrtName;
        vm.item.contractCode = ProposalEvaluation.getItem().contractCode;
        vm.item.contractName = ProposalEvaluation.getItem().contractName;
        vm.item.constrtAddress = ProposalEvaluation.getItem().constrtAddress;
        vm.item.stationCode = ProposalEvaluation.getItem().stationCode;
        vm.item.contractId = ProposalEvaluation.getItem().contractId;}
		if(vm.item == null) {
			vm.item = CommonService.getItem();
		}
        vm.groundhandover = {};
        vm.item.constructId = vm.item.constructId;
        vm.item.contractCode = vm.item.contractCode; 
        // TH??? tr?????ng
        vm.getroledirectora = {};
        vm.getroledirectora.roleid = Constant.ROLE_ID["employee_roleID_CDT_DDPN"];
        vm.getroledirectora.constructId = vm.item.constructId;
        vm.getroledirectora.contractCode = vm.item.contractCode;
        // Gi??m s??t
        vm.getrolemonitor = {};
        vm.getrolemonitor.roleid = Constant.ROLE_ID["employee_roleID_CDT_GSTC"];
        vm.getrolemonitor.constructId = vm.item.constructId;
        vm.getrolemonitor.contractCode = vm.item.contractCode;
        // Ph??? tr??ch gi??m s??t
        vm.getroleinChargeMonitor = {};
        vm.getroleinChargeMonitor.roleid = Constant.ROLE_ID["employee_roleID_CDT_PTGST"];
        vm.getroleinChargeMonitor.constructId = vm.item.constructId;
        vm.getroleinChargeMonitor.contractCode = vm.item.contractCode;
        // Gi??m ?????c
        vm.getroledirectorb = {};
        vm.getroledirectorb.roleid = Constant.ROLE_ID["employee_roleID_DT_GDNT"];
        vm.getroledirectorb.constructId = vm.item.constructId;
        vm.getroledirectorb.contractCode = vm.item.contractCode;
        // Ph??? tr??ch thi c??ng
        vm.getrolebinChargeConstruct = {};
        vm.getrolebinChargeConstruct.roleid = Constant.ROLE_ID["employee_roleID_DT_KTTC"];
        vm.getrolebinChargeConstruct.constructId = vm.item.constructId;
        vm.getrolebinChargeConstruct.contractCode = vm.item.contractCode;
        vm.gridCommon = [{
            title: "M??",
            field: "code",
            width: 120
        }, {
            title: "T??n",
            field: "fullName",
            width: 120
        }];

        vm.theApproval = {
            code: '',
            constructId: '',
            constrCompReMapId: '',
            stringEmployee: '',
            isSign: '',
            path: '',
            nameFile: '',
            roleName: []
        }


        vm.approDTO = {
            statusCa: '',
            employeeId: '',
            comments: '',
            constrGroundHandoverId: '',
            constrCompReMapId: ''
        }

        function mapdata() {
            vm.groundhandover.constructId = vm.item.constructId;
            vm.groundhandover.contractCode = vm.item.contractCode;
            vm.groundhandover.contractName = vm.item.contractName;
            vm.groundhandover.constrtCode = vm.item.constrtCode;
            vm.groundhandover.constrtName = vm.item.constrtName;
            vm.groundhandover.constrtAddress = vm.item.constrtAddress;
            vm.groundhandover.signed_date = vm.item.signed_date;
        }

//        fillDataTable([]);
        reloadGroundHandover();
//        fetchAllGroundHandover();
        mapdata();

        function fillDataTable(data) {
        	vm.reportgridOptions = kendoConfig
                .getGridOptions({
                    autoBind: true,
//                    dataSource: dataFill,
                    dataSource: data,
                    editable: false,
                    noRecords: true,
                    change: onChange,
                    dataBound: function(e) {
                        var grid = $("#reportGrid").data("kendoGrid");
                        if (!vm.rowIndex) {
                            grid.select("tr:eq(0)");
                        } else {
                            grid.select("tr:eq(" + vm.rowIndex + ")");
                        }
                    },
                    messages: {
                        noRecords: gettextCatalog
                            .getString("Kh??ng c?? d??? li???u cho trang hi???n t???i ")
                    },
                    columns: [{
                            title: gettextCatalog.getString("STT"),
                            field: "stt",
                            template: dataItem => $("#reportGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
                            width: 70,
                            editor: nonEditor,
                            headerAttributes: { style: "text-align:center;" },
                            attributes: { style: "text-align:center;" },
                        },
                        {
                            title: gettextCatalog
                                .getString("M?? phi???u"),
                            field: "code",
                            width: 180,
                            editor: nonEditor,
                            headerAttributes: { style: "text-align:center;" },
                            attributes: { style: "text-align:left;" },
                        },
                        {
                            title: gettextCatalog
                                .getString("M?? c??ng tr??nh"),
                            field: "constrtCode",
                            width: 180,
                            editor: nonEditor,
                            headerAttributes: { style: "text-align:center;" },
                            attributes: { style: "text-align:left;" },
                        },
                        {
                            title: gettextCatalog
                                .getString("M?? h???p ?????ng"),
                            field: "contractCode",
                            width: 180,
                            editor: nonEditor,
                            headerAttributes: { style: "text-align:center;" },
                            attributes: { style: "text-align:left;" },
                        },
                        {
                            title: gettextCatalog
                                .getString("Tr???ng th??i"),
                            field: "statusCa",
                            template: "# if(statusCa == 0){ #" +
                                "#= 'So???n th???o' #" +
                                "# } else if (statusCa==1) { # " +
                                "#= 'Tr??nh duy???t' #" +
                                "# } else if (statusCa==2) { # " +
                                "#= '???? duy???t' #" +
                                "# } else if (statusCa==3) {#" +
                                "#=  'T??? ch???i duy???t' #" +
                                "#} #",
                            width: 80,
                            editor: nonEditor,
                            headerAttributes: { style: "text-align:center;" },
                            attributes: { style: "text-align:left;", class:"statusColumn" },
                        }
                    ]
                });
        }
        //ngoccx
      //huy trinh duyet
		vm.cancelApprovalBtn= function(){
//			var grid = vm.reportGrid;
			var grid = $("#reportGrid").data("kendoGrid");
			if (grid.select() == null || grid.select().length == 0) {
				toastr.warning("C???n ch???n b???n ghi tr?????c khi th???c hi???n thao t??c n??y!");
				return;
			}
			vm.groundhandover.tableName = 'CONSTR_GROUND_HANDOVER';
			vm.groundhandover.tableId = vm.groundhandover.constrGroundHandoverId;
			vm.groundhandover.tableIdField = 'CONSTR_GROUND_HANDOVER_ID';
			if(vm.groundhandover.statusCa == 1){
				if(vm.groundhandover.createdUserId != Constant.user.srvUser.userId){
					toastr.warning(gettextCatalog.getString("Ng?????i t???o m???i c?? quy???n h???y tr??nh duy???t!"));
					return;
				}else{
					if(confirm('X??c nh???n h???y tr??nh duy???t')){
					groundhandoverservices.cancelAprroval(vm.groundhandover).then(function() {
						status = true;
						$rootScope.$broadcast("ChangeStatusHuyDuyet", status);
					reloadGroundHandover();
				toastr.success(gettextCatalog.getString("???? h???y tr??nh duy???t !"));
			}, function(){
			toastr.error(gettextCatalog.getString("L???i khi h???y tr??nh duy???t!"));
			return;
		});}
					}
			}else{
				toastr.warning("Ch??? tr??nh duy???t m???i ???????c h???y tr??nh duy???t");
			}
		}
        function fetchAllGroundHandover() {
            groundhandoverservices.fetchAllGroundHandover(vm.item).then(
                function(d) {
                    if (d.plain().length === 0) {
                        vm.hide = false;
                    } else {
                        vm.hide = true;
                    }
                    refreshGrid(d.plain());
                    fillDataTable(d.plain());
                },
                function(errResponse) {
                    toastr.error('Error while fetching bankAccount');
                });
        }

        function reloadGroundHandover() {
            groundhandoverservices.fetchAllGroundHandover(vm.item).then(function(d) {
                if (d.plain().length === 0) {
                    vm.hide = false;
                } else {
                    vm.hide = true;
                }
                fillDataTable(d.plain());
                refreshGrid(d.plain());
            }, function(errResponse) {
                toastr.error('Error while fetching bankAccount');
            });
        }

        function refreshGrid(d) {
            var grid =  $("#reportGrid").data("kendoGrid");
            grid.dataSource.data(d);
            grid.refresh();
            mapdata();
        }

        function onChange(e) {
        	var grid = $("#reportGrid").data("kendoGrid");
            if (grid.select().length > 0) {
                var tr = grid.select().closest("tr");
                var dataItem = grid.dataItem(tr);
                vm.groundhandover = dataItem;

                vm.theApproval.code = dataItem.code;
                vm.theApproval.constructId = dataItem.constructId;
                vm.theApproval.constrCompReMapId = dataItem.constrCompReMapId;
                vm.theApproval.stringEmployee = dataItem.amonitorId + ',' + dataItem.binChargeConstructId + ',' + dataItem.adirectorId + ',' + dataItem.bdirectorId;
                vm.theApproval.roleName = ["Gi??m s??t","K??? thu???t thi c??ng","Th??? tr?????ng","Gi??m ?????c"];
                vm.theApproval.roleId = [vm.getrolemonitor.roleid,vm.getrolebinChargeConstruct.roleid, vm.getroledirectora.roleid,vm.getroledirectorb.roleid];
                vm.theApproval.isSign = 'theApproval';
                // theApproval.setItem(vm.theApproval);
                vm.approDTO.employeeId = Constant.getUser().srvUser.catEmployeeId;
                vm.approDTO.constrGroundHandoverId = dataItem.constrGroundHandoverId
                vm.approDTO.constrCompReMapId = dataItem.constrCompReMapId;
                vm.approDTO.statusCa = 2;
            }
        }

        vm.onChangeGrid = function(e, sel, dataItem) {
            vm.bean = dataItem;
            vm.rowIndex = sel.selected.rowIndex;
            onChange();
        }

        function save() {
            if (vm.disabledPheDuyet) {
                dopheDuyet();
                vm.disabledPheDuyet = false;
            } else {
                if (vm.showDetail) {
                    if (vm.isCreateNew) {
                        if (vm.validator.validate()) {
                            if (!vm.groundhandover.constrGroundHandoverId) {
                                vm.groundhandover.createdUserId = Constant.user.srvUser.userId;
                            }
                            if(vm.groundhandover.code == '' || vm.groundhandover.code == null){
                            	groundhandoverservices.saveGroundHandover(vm.groundhandover).then(
                                        function(result) {
                                            toastr.success("L??u th??nh c??ng!");
                                            reloadGroundHandover();
                                            showGrid();
                                            vm.groundhandover.constrGroundHandoverId = result;
                                        },
                                        function(errResponse) {
                                            if (errResponse.status == 409) {
                                                toastr.error("Ch??? ???????c ph??p t???o 1 b???n ghi cho 1 c??ng tr??nh");
                                            } else {
                                                toastr.error("L???i khi l??u");
                                            }
                                            // Config
                                        });
                            }else if(vm.groundhandover.code != null && vm.groundhandover.createdUserId == Constant.user.srvUser.userId){
                            	groundhandoverservices.saveGroundHandover(vm.groundhandover).then(
                                        function(result) {
                                            toastr.success("L??u th??nh c??ng!");
                                            reloadGroundHandover();
                                            showGrid();
                                            vm.groundhandover.constrGroundHandoverId = result;
                                        },
                                        function(errResponse) {
                                            if (errResponse.status == 409) {
                                                toastr.error("Ch??? ???????c ph??p t???o 1 b???n ghi cho 1 c??ng tr??nh");
                                            } else {
                                                toastr.error("L???i khi l??u");
                                            }
                                            // Config
                                        });
                            }else{
                            	toastr.error("B???n kh??ng ???????c quy???n s???a b???n ghi!");
                            }
                            	
                        }
                    } else {

                    }

                }
            }
        }

        function add() {
            $(".k-invalid-msg").hide();
            vm.showDetail = true;
            vm.disabledPheDuyet = false;
            vm.showHandoverDate = true;
            vm.isCreateNew = true;
            vm.disableCreatOne = false;
            vm.disableCreatTwo = false;
            addRequired();
            // th??? tr?????ng
            groundhandoverservices.fetchEmployeeByRole(vm.getroledirectora).then(function(data) {
                if (typeof data != undefined && data.length > 0) {
                    vm.groundhandover.adirectorId = data.plain()[0].id;
                    vm.groundhandover.adirectorName = data.plain()[0].fullName;
                }
            }, function(errResponse) {

            });

            // gi??m s??t
            groundhandoverservices.fetchEmployeeByRole(vm.getrolemonitor).then(function(data) {
                if (typeof data != undefined && data.length > 0) {
                    vm.groundhandover.amonitorId = data.plain()[0].id;
                    vm.groundhandover.amonitorName = data.plain()[0].fullName;
                }
            }, function(errResponse) {

            });

            // Ph??? tr??ch gi??m s??t
            groundhandoverservices.fetchEmployeeByRole(vm.getroleinChargeMonitor).then(function(data) {
                if (typeof data != undefined && data.length > 0) {
                    vm.groundhandover.ainChargeMonitorId = data.plain()[0].id;
                    vm.groundhandover.ainChargeMonitorName = data.plain()[0].fullName;
                }
            }, function(errResponse) {

            });

            // Gi??m ?????c
            groundhandoverservices.fetchEmployeeByRole(vm.getroledirectorb).then(function(data) {
                if (typeof data != undefined && data.length > 0) {
                    vm.groundhandover.bdirectorId = data.plain()[0].id;
                    vm.groundhandover.bdirectorName = data.plain()[0].fullName;
                }
            }, function(errResponse) {

            });
            // Ph??? tr??ch thi c??ng
            groundhandoverservices.fetchEmployeeByRole(vm.getrolebinChargeConstruct).then(function(data) {
                if (typeof data != undefined && data.length > 0) {
                    vm.groundhandover.binChargeConstructId = data.plain()[0].id;
                    vm.groundhandover.binChargeConstructName = data.plain()[0].fullName;
                }
            }, function(errResponse) {

            });
            vm.groundhandover = {};
            mapdata();

        }

        function showGrid() {
            $(".k-invalid-msg").hide();

            if (vm.showDetail == false) {
                if (vm.groundhandover.statusCa == 1 || vm.groundhandover.statusCa == 2) {
//                    vm.disableCreatOne = true;
                    vm.showHandoverDate = true;
                    vm.showDetail = true;
                    vm.disableCreatTwo = true;
                } else {
                    if (vm.reportGrid.select().length > 0) {
                        vm.showHandoverDate = true;
                        vm.showDetail = true;
                        vm.isCreateNew = true;
                        $(".k-invalid-msg").hide();
                    } else {
                        toastr.warning("B???n c???n ch???n m???t b???n ghi tr?????c");
                    }
                }
            } else {
                vm.showDetail = false;
                vm.showHandoverDate = false;
                vm.isCreateNew = false;
                vm.disabledPheDuyet = false;
            }
        }

        vm.update = function() {
            if (vm.showDetail == false) {
                if (vm.reportGrid.select().length > 0) {
                    vm.showDetail = true;
                    $(".k-invalid-msg").hide();
                } else {
                    toastr.warning("B???n c???n ch???n m???t b???n ghi tr?????c");
                }
            }
        }

        function remove() {
            // TODO
            if (vm.reportGrid.select().length > 0) {
            	if(vm.groundhandover.statusCa == 0 || vm.groundhandover.statusCa == 3){
            		if( vm.groundhandover.createdUserId == Constant.user.srvUser.userId){
            			if(confirm('X??c nh???n x??a')){
            			groundhandoverservices.deleteOne(vm.groundhandover.constrGroundHandoverId).then(function() {
                          toastr.success("X??a th??nh c??ng!");
                          reloadGroundHandover();
                          vm.showDetail = false;
                      }, function(errResponse) {
                          if (errResponse.status == 302) {
                              toastr.error("Kh??ng th??? x??a b???n ghi ??ang duy???t ho???c ???? duy???t");
                          } else {
                              toastr.error("L???i khi export");
                          }
                      });}
            		}else{
            			toastr.error("Kh??ng th??? x??a b???n ghi, ng?????i t???o m???i ???????c x??a!");
            		}
            		  
            	}else{
            		toastr.warning("B???n ghi tr??nh duy???t ho???c ???? duy???t kh??ng ???????c x??a!");
            		return;
            	}
              
            }
        }

        function exportFile() {
            vm.groundhandover.docOrPdf = false;
            if (vm.reportGrid.select().length > 0) {
                var tr = vm.reportGrid.select().closest("tr");
                var dataItem = vm.reportGrid.dataItem(tr);
                vm.groundhandover = dataItem;
                $('#loading').show();
                groundhandoverservices.exportFile(vm.groundhandover).then(function(data) {
                    toastr.success("Export th??nh c??ng");
                    window.location = RestEndpoint.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                }).catch( function(errResponse) {
                    if (errResponse.status == 302) {
                        toastr.error("Kh??ng export b???n ghi ??ang duy???t ho???c ???? duy???t");
                    } else {
                        toastr.error("L???i khi export");
                    }
                }).finally(function(){
                	$('#loading').hide();
                });   
            }else{
            	toastr.error(gettextCatalog.getString("Kh??ng c?? b???n ghi export!"));
            	return;
            }
        }

        function ExportDOC() {
            vm.groundhandover.docOrPdf = true;
            if (vm.reportGrid.select().length > 0) {
                var tr = vm.reportGrid.select().closest("tr");
                var dataItem = vm.reportGrid.dataItem(tr);
                vm.groundhandover = dataItem;
                $('#loading').show();
                groundhandoverservices.exportFile(vm.groundhandover).then(function(data) {
                    toastr.success("Export th??nh c??ng");
                    window.location = RestEndpoint.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                }).catch( function(errResponse) {
                    if (errResponse.status == 302) {
                        toastr.error("Kh??ng export b???n ghi ??ang duy???t ho???c ???? duy???t");
                    } else {
                        toastr.error("L???i khi export");
                    }
                }).finally(function(){
                	$('#loading').hide();
                });  
            }else{
            	toastr.error(gettextCatalog.getString("Kh??ng c?? b???n ghi export!"));
            	return;
            }
        }

        function nonEditor(container, options) {
            container.text(options.model[options.field]);
        }

        vm.pheDuyet = function pheDuyet() {
            if (vm.reportGrid.select().length > 0) {
                if (vm.groundhandover.statusCa == 1) {
//                    showGrid();
//                    vm.disableCreatOne = true;
                	vm.showDetail = true;
                    vm.disableCreatTwo = true;
                    vm.disabledPheDuyet = true;
                    vm.approDTO.comments = '';
                    vm.showHandoverDate = true;
                } else if (vm.groundhandover.statusCa == 0){
                    toastr.error(gettextCatalog.getString("B???n ghi ch??a ???????c tr??nh duy???t!"));
                }else if(vm.groundhandover.statusCa == 2){
                	toastr.warning("B???n ghi ???? ???????c ph?? duy???t!");
                }
            } else {
                toastr.warning("B???n c???n ch???n m???t b???n ghi tr?????c");
            }
        }

        function dopheDuyet() {
            groundhandoverservices.appro(vm.approDTO).then(function(d) {
                var x = d;
                if (x == '1') {
                    toastr.warning("Ch??a ?????n l?????t duy???t");
                    return;
                }
                if (x == '2') {
                    toastr.warning("???? duy???t r???i");
                    return;
                }
                if (x == '4') {
                    toastr.success("T??? ch???i duy???t th??nh c??ng");
                }
                if (x == '3') {
                    toastr.success("Duy???t th??nh c??ng");
                }
                if (x == '5') {
                    toastr.error("B???n kh??ng c?? quy???n duy???t");
                }
                fetchAllGroundHandover();
                vm.showDetail = false;
            }, function(e) {
                toastr.error(gettextCatalog.getString("L???i khi ph?? duy???t!"));
                return;
            });
        }

        vm.sendBrowse = sendBrowse;

        function sendBrowse() {
            if (vm.reportGrid.select().length > 0) {
                if (vm.groundhandover.statusCa == 0) {
                    vm.groundhandover.docOrPdf = false;
                    $('#loading').show();
                    groundhandoverservices.exportFile(vm.groundhandover).then(function(data) {
                        vm.theApproval.path = data.fileName;
                        vm.theApproval.nameFile = 'BM-TCNT-06.pdf';
                        theApproval.setItem(vm.theApproval);
                        goTo('THE_APPROVAL');
                    }).catch( function(){
                    	toastr.error(gettextCatalog.getString("L???i khi export!"));
                    	return;
                    }).finally(function(){
                    	$('#loading').hide();
                    });
                    
                } else {
                	toastr.warning("Tr???ng th??i so???n th???o m???i ???????c tr??nh duy???t!");
                }
            } else {
                toastr.warning("B???n c???n ch???n m???t b???n ghi tr?????c");
            }


        }

        $scope.$on("ChangeStatusApproval", function(event, result) {
            if (result) {
            	vm.showDetail = false;
                reloadGroundHandover();
            }
        });

        // ??i t???i trang ti???p
        function goTo(menuKey) {
            var hasPerm = true;
            if (hasPerm) {
                var template = Constant.getTemplateUrl(menuKey);
                postal.publish({
                    channel: "Tab",
                    topic: "open",
                    data: template
                });
            } else {
                // toastr.error(gettextCatalog.getString("T??i kho???n ????ng
                // nh???p
                // hi???n t???i kh??ng ???????c ph??p truy c???p v??o ch???c n??ng n??y!"));
            }
        }

        function chkSelectAll(item) {
            console.log('chkSelectAll');
//            var grid = vm.reportGrid;
            var grid = $("#reportGrid").data("kendoGrid");
            chkSelectAllBase(vm.chkAll, grid);
        }

        function nonEditor(container, options) {
            container.text(options.model[options.field]);
        }

        vm.commonSearch = { name: '', value: '' };
        vm.gridCommon = [{
            title: "M??",
            field: "id",
            width: 120
        }, {
            title: "T??n",
            field: "fullName",
            width: 120
        }];
        vm.objTypeSearch = { id: '', fullName: '' };

//        function addRequired() {
//            // TODO
//        }

        function setValue() {
            // TODO
        }

        function resetValue() {
            // TODO
        }
        //minhpvn
    	function addRequired(){			   
       	 document.getElementById("ground_handover_txtAdirectorId").required = true;
    	 document.getElementById("ground_handover_txtAinChargeMonitorId").required = true;
    	 document.getElementById("ground_handover_txtAmonitorId").required = true;
    	 document.getElementById("ground_handover_txtBdirectorId").required = true;
    	 document.getElementById("ground_handover_txtBinChargeConstructId").required = true;
		}
    }
})();