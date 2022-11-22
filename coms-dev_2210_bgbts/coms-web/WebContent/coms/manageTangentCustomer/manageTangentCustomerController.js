(function() {
	'use strict';
	var controllerId = 'manageTangentCustomerController';

	angular.module('MetronicApp').controller(controllerId,
			manageTangentCustomerController);

	function manageTangentCustomerController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, manageTangentCustomerService,
			CommonService, PopupConst, Restangular, RestEndpoint, Constant,
			htmlCommonService) {
		var vm = this;
		vm.showSearch = true;
		vm.isCreateNew = false;
		vm.isSearch = true;
		vm.showDetail = false;
		vm.isCustomers = false;
		vm.isEdit = false;
		vm.disabledPartnerType = false;
		vm.tab1 = true;
		vm.tab2 = false;
		vm.btn1 = false; vm.btn2 = true; vm.btn3 = true; vm.btn4 = false;
		vm.viewForm = {};
		vm.conversationContent = [];
		vm.conversationContentSolution = [];
		vm.searchForm = {
				type: ""
		};
		vm.resultForm = {};
		vm.resultYesForm = {};
		vm.resultNoForm = {};
		vm.solutionForm = {};
		vm.resultHisForm = {};
		vm.solutionHisForm = {};
		vm.fileLst = [];
		vm.fileLstSoDo = [];
		vm.fileLstDiaHinh = [];
		vm.fileLstGiaiPhap=[];
		vm.areaIdOfProvince = null;
		vm.validatePhone = false;
		vm.validatePhoneMsg = "";
		vm.resultSolutionTypeList = [
			{
				code: "1",
				name: "Đồng ý giải pháp"
			},
			{
				code: "2",
				name: "Từ chối giải pháp giải pháp"
			},
			{
				code: "3",
				name: "Bổ sung trình bày giải pháp"
			},
		];
		
		vm.checkPerform = true;
		vm.disabledFile = false;
		vm.checkCreatedYCTX = false;
		vm.statusList = [
			{
				code: 1,
				name: "Chờ tiếp nhận"
			},
			{
				code: 2,
				name: "Chờ tiếp xúc"
			},
			{
				code: 3,
				name: "Chờ trình bày giải pháp"
			},
//			{
//				code: 4,
//				name: "Từ chối tiếp xúc & chờ phê duyệt"
//			},
			{
				code: 4,
				name: "Không thành công"
		    },
			{
				code: 5,
				name: "Chờ ký hợp đồng"
			},
			{
				code: 6,
				name: "Bổ sung / chỉnh sửa giải pháp"
			},
//			{
//				code: 7,
//				name: "Từ chối giải pháp & chờ phê duyệt"
//			},
			{
				code: 7,
				name: "Không thành công"
			},
			{
				code: 8,
				name: "Hoàn thành tiếp xúc"
			},
			{
				code: 9,
				name: "Không thành công"
			},
			{
				code: 10,
				name: "Hoàn thành thanh toán hoa hồng"
			},
			{
				code: 0,
				name: "Đã hủy"
			},
		];
		
		vm.statusListSearch = [
			{
				code: 0,
				name: "Đã hủy"
			},
			{
				code: 1,
				name: "Chờ tiếp nhận"
			},
			{
				code: 2,
				name: "Chờ tiếp xúc"
			},
			{
				code: 3,
				name: "Chờ trình bày giải pháp"
			},

			{
				code: 5,
				name: "Chờ ký hợp đồng"
			},
			{
				code: 6,
				name: "Bổ sung / chỉnh sửa giải pháp"
			},
			{
				code: 8,
				name: "Hoàn thành tiếp xúc"
			},

			{
				code: 10,
				name: "Hoàn thành thanh toán hoa hồng"
			},
			{
				code: 11,
				name: "Không thành công"
			},
			
		];
		
		vm.overdueStatusList = [
			{
				code: 1,
				name: "Trong hạn"
			},
			{
				code: 2,
				name: "Sắp quá hạn"
			},
			{
				code: 3,
				name: "Quá hạn"
			}
		];
		vm.fieldWorkList = [
			{ code: '', name: ''}
		];
		vm.channelCustomerList = [
			{ code: '', name: ''}
		];

		vm.channelReceptionList = [
			{ code: '', name: ''}
		];
		vm.businessCareerList = [
			{ code: '', name: ''}
		];
		
		vm.geographicalAreaList = [
			{
				code: 1,
				name: "Đồng bằng"
			},
			{
				code: 2,
				name: "Trung du"
			},
			{
				code: 3,
				name: "Miền núi"
			},
			{
				code: 4,
				name: "Ven biển"
			},
			{
				code: 5,
				name: "Hải đảo"
			}
		];
		vm.customersList = [
			{
				code: 1,
				name: "KH ngoại giao"
			},
			{
				code: 2,
				name: "KH thường"
			}
		];
		
		vm.employeesList = [
			{
				code: 1,
				name: "Dưới 100 người"
			},
			{
				code: 2,
				name: "Từ  100 đến 500 người"
			},
			{
				code: 3,
				name: "Từ 500 đến 1000 người"
			},
			{
				code: 4,
				name: "Trên 1000 người"
			}
			
		];
		
		vm.positionsRepresentativeList = [
			{
				code: 1,
				name: "Tổng Giám đốc"
			},
			{
				code: 2,
				name: "Phó Tổng GĐ"
			},
			{
				code: 3,
				name: "Giám đốc"
			}
			
		];
		vm.positionsDirectContactList = [
			{
				code: 1,
				name: "Trưởng phòng/ Phó phòng"
			},
			{
				code: 2,
				name: "Nhân viên/ Chuyên viên"
			},
			{
				code: 3,
				name: "Khác"
			}
			
		];

		var modalInstance1 = null;
		var modalInstance2 = null;
		var modalInstance3 = null;
		
		vm.addForm = {};
		$scope.isInternalSource = 0;
		$scope.isSocialSource = 0;
		$scope.isCustomerServiceSource = 0;
		vm.showTabOne = true;
		vm.showTabTwo = false;

		vm.customer = function () {
			vm.showTabOne = true;
			vm.showTabTwo = false;
			vm.btn3=true;vm.btn2 = true;vm.btn1 = false;vm.btn4 = false;
			$("#one").addClass("left right  active");
			$("#two").removeClass("right left active");

			$("#two").addClass("Left right");
		}
		
		vm.design = function () {
			vm.showTabOne = false;
			vm.showTabTwo = true;
			vm.btn3=false;vm.btn2 = false;vm.btn1 = true;vm.btn4 = true;
			$("#two").addClass("right left active");
			$("#one").removeClass("right left active");
			
			$("#one").addClass("Left right");

		}

		vm.String = "Quản lý công trình > Tiếp xúc khách hàng > Quản lý yêu cầu tiếp xúc";
		vm.checkRoleApproved = false;
		vm.checkRoleUpdate = false;
		vm.contractRose = null;
		vm.showBtnSaveGiaiPhap = false;
		vm.isSelect = false;
		vm.checkRoleUserPgd = false;
		vm.checkRoleUserAssignYctx = false;
		$scope.loggedInUser = $rootScope.casUser.userName;
        $scope.sysUserId = $rootScope.casUser.sysUserId;
        
        vm.registeredCustomerServiceList = [];
        vm.modelOfTheBuilderList = [];
		init();
		function init() {
			vm.userLoginId = $rootScope.casUser.sysUserId;
			manageTangentCustomerService.checkRoleApproved().then(function(result){
				if(result==1){
					vm.checkRoleApproved = true;
				}
			});
			
			manageTangentCustomerService.checkRoleUpdate().then(function(result){
				if(result==1){
					vm.checkRoleUpdate = true;
				}
			});
			
			manageTangentCustomerService.getContractRose().then(function(result){
				if(result!=null){
					vm.contractRose = result;
				}
			});
			
			manageTangentCustomerService.checkRoleUserAssignYctx().then(function(result){
				if(result==1){
					vm.checkRoleUserPgd = true;
				}
			});
			
			var obj = {};
			
			manageTangentCustomerService.getChannel(obj).then(function(result){
				if(result && result.data){
					vm.channelCustomerList = result.data.filter(function(record) {
						  return record.parType == "CUSTOMER_RESOURCES";
					});
					vm.channelCustomerListCopy = angular.copy(vm.channelCustomerList);
					vm.channelReceptionList = result.data.filter(function(record) {
						  return record.parType == "RECEPTION_CHANNEL";
					});
					
					vm.fieldWorkList = result.data.filter(function(record) {
						  return record.parType == "FIELD_WORK";
					});
					vm.businessCareerList = angular.copy(vm.fieldWorkList);
					var otherCareer = {
						code: 	vm.fieldWorkList.length +1,
						name:   'Khác'
					};
					vm.businessCareerList.push(otherCareer);
					
					vm.registeredCustomerServiceList = result.data.filter(function(record) {
						  return record.parType == "REGISTERED_CUSTOMER_SERVICE";
					});
					
					vm.modelOfTheBuilderList = result.data.filter(function(record) {
						  return record.parType == "BUILDER_MODEL";
					});
					
				}else toast.error("Chưa có dữ liệu cấu hình kênh tiếp nhận/nguồn Khách hàng")
			});
			obj = {};
			obj.createdUser = $scope.sysUserId;
			manageTangentCustomerService.checkRoleSourceYCTX(obj).then(function(result){
				if(result!=null){
					if(result.error){
    					toastr.error(result.error);
            			return;
    				}
					vm.IDRole = result;
					if(vm.IDRole==1)	$scope.isInternalSource = 1;
					else if(vm.IDRole==2)	$scope.isSocialSource = 1;
					else $scope.isCustomerServiceSource = 1;

				}
			});
			// Duonghv13-start 11022022
			manageTangentCustomerService.checkRoleCreated().then(function(d){
            	if(d && d == 'OK'){
            		vm.checkCreatedYCTX = true;
            	} else{
            		vm.checkCreatedYCTX = false;
            	}
            }).catch(function () {
                vm.checkCreatedYCTX = false;
            });
            // Duong-end

			fillFileTable([]);
			fillDataTable([]);
		}

		vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.tangentCustomerGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.tangentCustomerGrid.showColumn(column);
			} else {
				vm.tangentCustomerGrid.hideColumn(column);
			}

		}

		vm.gridColumnShowHideFilter = function(item) {

			return item.type == null || item.type !== 1;
		};
		
		vm.doSearch = doSearch;
		function doSearch(){
			if ($('.k-invalid-msg').is(':visible')) {
                return;
            }
			var grid = vm.tangentCustomerGrid;
			if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 10
				});
			}
		}
		
		function checkChannelReceptionList() {
            if (vm.checkCreatedYCTX  == false) {
    			vm.channelReceptionList = [];
            }
            return vm.channelReceptionList;
        };
        
        vm.changeChannelReception=function(data) {
        	if(data != 'Hotline') {
        		vm.channelCustomerList = [];
        	}else vm.channelCustomerList = vm.channelCustomerListCopy;
        	return vm.showNote;
        };
        
        vm.changeInfor =function(data) {
        		if(data == 1) {
            		vm.resultYesForm.detailCustomer.emailB2C = vm.viewForm.customerEmail;
            		vm.resultYesForm.detailCustomer.phoneB2C = vm.viewForm.customerPhone;
            	}else{
            		vm.resultYesForm.detailCustomer.emailB2C = null;
            		vm.resultYesForm.detailCustomer.phoneB2C = null;
            		vm.resultYesForm.detailCustomer.business = 'Lĩnh vực Xây dựng';
            		vm.resultYesForm.detailCustomer.customers = 'KH thường';
            		vm.resultYesForm.detailCustomer.positionDirectContact = 'Nhân viên/ Chuyên viên';
            		
            	}
        }
		
		var validator = $("#realIeDateSynStockTrans").kendoValidator({
            rules: {
                dateValid: function(input) {
                    if (input.is("[data-role='datepicker']")) {
                    	if (!input.val()) {
                    		document.getElementById("errorDateFrom").style.display = "none";
                        	document.getElementById("errorDateTo").style.display = "none";
                    		return true;
                    	}
                        var date = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                        if (!date) {
                        	document.getElementById("errorDateFrom").style.display = "block";
                        	document.getElementById("errorDateTo").style.display = "block";
                            return false;
                        }
                    }
                    document.getElementById("errorDateFrom").style.display = "none";
                	document.getElementById("errorDateTo").style.display = "none";
                    return true;
                },
                dateFrom: function(input) {
                    if (input.is("#createdDateFrom")) {
                        var dateTo = kendo.parseDate($("#createdDateTo").val(), 'dd/MM/yyyy');
                        var dateFrom = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                        if (!dateTo || !dateFrom) {
                        	document.getElementById("errorDateFrom").style.display = "none";
                            return true;
                        }
                        if (dateTo < dateFrom) {
                        	document.getElementById("errorDateFrom").style.display = "block";
                            return false;
                        }
                    }
                    document.getElementById("errorDateFrom").style.display = "none";
                    return true;
                },
                dateTo: function(input) {
                    if (input.is("#createdDateTo")) {
                        var dateTo = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                        var dateFrom = kendo.parseDate($("#createdDateFrom").val(), 'dd/MM/yyyy');
                        if (!dateFrom || !dateTo) {
                        	document.getElementById("errorDateTo").style.display = "none";
                            return true;
                        }
                        if (dateTo < dateFrom) {
                        	document.getElementById("errorDateTo").style.display = "block";
                            return false;
                        }
                    }
                	document.getElementById("errorDateTo").style.display = "none";
                    return true;
                },
//                dateIn31: function (input, a) {
//                    if (input.is("#createdDateFrom") || input.is("#createdDateTo")) {
//                        var dateTo = kendo.parseDate($("#createdDateTo").val(), 'dd/MM/yyyy');
//                        var dateFrom = kendo.parseDate($("#createdDateFrom").val(), 'dd/MM/yyyy');
//                        if (!dateTo || !dateFrom) {
//                        	document.getElementById("errorDateFrom").style.display = "none";
//                        	document.getElementById("errorDateTo").style.display = "none";
//                        	return true;
//                        }
//                        var dayDiff = Math.floor((dateTo - dateFrom) / (1000*60*60*24));
//                        if (dayDiff > 31) {
//                        	document.getElementById("errorDateFrom").style.display = "block";
////                        	document.getElementById("errorDateTo").style.display = "block";
//                            return false;
//                        }
//                    }
//                    document.getElementById("errorDateFrom").style.display = "none";
//                    document.getElementById("errorDateTo").style.display = "none";
//                    return true;
//                }
            },
            messages: {
                // dateEmpty: "Ngày không được bỏ trống",
                dateValid: "Ngày không hợp lệ",
                dateTo: "Chọn \"Đến ngày\" lớn hơn",
                dateFrom: "Chọn \"Từ ngày\" nhỏ hơn",
//                dateIn31: "Chọn khoảng thời gian nhỏ hơn 31 ngày"
            }
        }).data("kendoValidator");
		
		var validator = $("#suggestTimeFormId").kendoValidator({
            rules: {
                dateValid: function(input) {
                    if (input.is("[data-role='datepicker']")) {
                    	if (!input.val()) {
                    		document.getElementById("errorTimeFrom").style.display = "none";
                        	document.getElementById("errorTimeTo").style.display = "none";
                    		return true;
                    	}
                        var date = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                        if (!date) {
                        	document.getElementById("errorTimeFrom").style.display = "block";
                        	document.getElementById("errorTimeTo").style.display = "block";
                            return false;
                        }
                    }
                    document.getElementById("errorTimeFrom").style.display = "none";
                	document.getElementById("errorTimeTo").style.display = "none";
                    return true;
                },
                dateFrom: function(input) {
                    if (input.is("#suggestTimeFrom")) {
                        var dateTo = kendo.parseDate($("#suggestTimeTo").val(), 'dd/MM/yyyy');
                        var dateFrom = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                        if (!dateTo || !dateFrom) {
                        	document.getElementById("errorTimeFrom").style.display = "none";
                            return true;
                        }
                        if (dateTo < dateFrom) {
                        	document.getElementById("errorTimeFrom").style.display = "block";
                            return false;
                        }
                    }
                    document.getElementById("errorTimeFrom").style.display = "none";
                    return true;
                },
                dateTo: function(input) {
                    if (input.is("#suggestTimeTo")) {
                        var dateTo = kendo.parseDate(input.val(), 'dd/MM/yyyy');
                        var dateFrom = kendo.parseDate($("#suggestTimeFrom").val(), 'dd/MM/yyyy');
                        if (!dateFrom || !dateTo) {
                        	document.getElementById("errorTimeTo").style.display = "none";
                            return true;
                        }
                        if (dateTo < dateFrom) {
                        	document.getElementById("errorTimeTo").style.display = "block";
                            return false;
                        }
                    }
                	document.getElementById("errorTimeTo").style.display = "none";
                    return true;
                },
            },
            messages: {
                // dateEmpty: "Ngày không được bỏ trống",
                dateValid: "Ngày không hợp lệ",
                dateTo: "Chọn \"Đến ngày\" lớn hơn",
                dateFrom: "Chọn \"Từ ngày\" nhỏ hơn",
            }
        }).data("kendoValidator");
		
		
		function fillFileTable(data) {
			var dataSource = new kendo.data.DataSource({
				data: data,
				autoSync: false,
				schema: {
					model: {
						id: "attachmentFileTableGrid",
						fields: {
							stt: { editable: false },
							name: { editable: false },
							acctions: { editable: false },
						}
					}
				}
			});

			vm.fileAttachDataTblOptions = kendoConfig.getGridOptions({
				autoBind: true,
				resizable: true,
				dataSource: dataSource,
				noRecords: true,
				columnMenu: false,
				scrollable: false,
				messages: {
					noRecords: CommonService.translate("Không có kết quả hiển thị")
				}, 
				dataBound: function () {
					var GridDestination = $("#attachmentFileTableGrid").data("kendoGrid");
					GridDestination.pager.element.hide();
				},
				columns: [{
					title: "TT",
					field: "stt",
					template: dataItem => $("#attachmentFileTableGrid").data("kendoGrid").dataSource.indexOf(dataItem) + 1,
					width: 20,
					headerAttributes: {
						style: "text-align:center; font-weight: bold",
						translate:""
					},
					attributes: {
						style: "text-align:center;"
					},
				}, {
					title: "Tên file",
					field: 'fileName',
					width: 150,
					headerAttributes: {
						style: "text-align:center; font-weight: bold",
						translate:""
					},
					attributes: {
						style: "text-align:left;"
					},
					template: function (dataItem) {
						return "<a href='' ng-click='caller.downloadFile(dataItem)'>" + dataItem.fileName + "</a>";
					}
				}, {
					title: "Xóa",
					headerAttributes: {
						style: "text-align:center; font-weight: bold",
						translate:""
					},
					template: dataItem =>
						'<div class="text-center #=utilAttachDocumentId#""> ' +
						'<button style=" border: none; "  class="#=utilAttachDocumentId# icon_table" uib-tooltip="Xóa" ng-click="caller.removeRowFile(dataItem)"  translate>  ' +
						'<i style="color: steelblue;" class="fa fa-trash" ria-hidden="true"></i>' +
						'</button>' +
						'</div>',
					width: 20,
					field: "acctions"
				}]
			});
		}
		
		vm.removeRowFile = removeRowFile;
		function removeRowFile(dataItem) {
			confirm('Xác nhận xóa', function () {
				$('#attachmentFileTableGrid').data('kendoGrid').dataSource.remove(dataItem);
				vm.resultYesForm.lstImageDesign.splice(dataItem, 1);
				if(vm.resultYesForm.lstImageDesign.length < 3){
                	vm.disabledFile = false;
                }
			})
		}
		
		vm.addAttachment = function (type) {
        	var idString = "#attachmentFile";
            $(idString).unbind().click();
            $(idString).change(function () {
                var selectedFile = $("#attachmentFile")[0].files[0];
                var reader = new FileReader();
                var formData = new FormData();
                formData.append('multipartFile', selectedFile);
                if(selectedFile.name.split('.').pop() !=='jpg' && selectedFile.name.split('.').pop() !=='png' ){
     				toastr.warning("Ảnh upload sai định dạng file");
     				return;
     			}
                var uploadFileService = Constant.BASE_SERVICE_URL + 'fileService/uploadWoFileATTT?folder=' + Constant.UPLOAD_FOLDER_IMAGES;
                manageTangentCustomerService.uploadAttachment(uploadFileService, formData)
                    .success(function (resp) {
                        if (resp[0]) {
                            var filePath = resp[0];
                            var fileName = selectedFile.name;
                            var mappingObj = {
                                fileName: fileName,
                                filePath: filePath,
                                userCreated: $scope.loggedInUser,
                                status: 1
                            }
                            vm.resultYesForm.lstImageDesign.push(mappingObj);
                            $('#attachmentFileTableGrid').data('kendoGrid').dataSource.data(vm.resultYesForm.lstImageDesign);
                            if(vm.resultYesForm.lstImageDesign.length >= 3){
                            	vm.disabledFile = true;
                            }

                        }
                    })
                    .error(function (err) {
                        console.log(err);
                        toastr.error("Có lỗi xảy ra khi tải lên file! Vui lòng thử lại sau.");
                    });
            });
            //
        };
		
		var record = 0;
		function fillDataTable(data){
			vm.tangentCustomerGridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
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
                        '<button class="btn btn-qlk padding-search addQLK" '
						+ 'ng-click="vm.add()" uib-tooltip="Tạo mới" translate style="width: 100px;white-space:normal;">Tạo mới</button>'
						+ '</div>'
                        +'<div class="btn-group pull-right margin_top_button ">' +
                        '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: triệu VNĐ</div>'+
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-disabled="vm.disableExportExcel" ng-click="vm.exportExcelqlyctx()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất excel</span></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">' +
                        '<label ng-repeat="column in vm.tangentCustomerGrid.columns.slice(1,vm.tangentCustomerGrid.columns.length)| filter: vm.gridColumnShowHideFilter">' +
                        '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}' +
                        '</label>' +
                        '</div></div>'
                    }
                ],
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                        	 console.log('1111111',response);
                            $("#appCountConfig").text("" + response.total);
                            vm.countConfig = response.total;
                            return response.total;
                        },
                        data: function (response) {
                        	 console.log('222222',response);
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            url: Constant.BASE_SERVICE_URL + "tangentCustomerRestService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                        	 console.log('33333',options);
                            vm.searchForm.page = options.page;
                            vm.searchForm.pageSize = options.pageSize;
                            return JSON.stringify(vm.searchForm);

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
                        width: '50px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Tỉnh",
                        width: '120px',
                        field:"provinceCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        editable:false,
                    },
                    {
                        title: "Quận/Huyện",
                        width: '120px',
                        field:"districtName",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        editable:false,
                    },
                    {
                        title: "Họ tên khách hàng",
                        field: 'customerName',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false,
                    },
                    {
                        title: "Năm sinh",
                        field: 'birthYear',
                        width: '80px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false,
                    },
                    {
                        title: "Số điện thoại",
                        field: 'customerPhone',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Địa chỉ",
                        field: 'address',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false
                    },{
                        title: "Nguồn",
                        field: 'source',
                        width: '150px',
                        template: "# if(source == 1){ #" + "#= 'Nội bộ' #" + "# } " + "else if (source == 2) { # " + "#= 'Xã hội hóa' #" + "#} "+ "else if (source == 3) { # " + "#= 'CSKH' #" + "#} "+ "else { # " + "#= '' #" + "#} #",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Kênh tiếp nhận",
                        field: 'receptionChannel',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Nguồn KH biết đến",
                        field: 'customerResources',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Loại khách hàng",
                        field: 'partnerType',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false,
                        template: function(dataItem) {
                        	if(dataItem.partnerType==null){
                        		return "";
                        	} else if(dataItem.partnerType==1) {
                        		return "Cá nhân";
                        	} else if(dataItem.partnerType==2) {
                        		return "Doanh nghiệp";
                        	}
                        }
                    },
                    {
                        title: "Người tạo",
                        field: 'createdUserName',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Ngày tạo",
                        field: 'createdDate',
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false
                    },
                    {
                        title: "Thời gian tiếp xúc tiếp theo",
                        field: 'nextTangentTime',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false,
                    },
                    {
                        title: "Nhân viên thực hiện",
                        field: 'performerName',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false,
                    },
                    {
                        title: "Hạn hoàn thành",
                        field: 'overdueStatus',
                        width: '100px',
                        template: "# if(overdueStatus == 1){ #" + "#= 'Trong hạn' #" + "# } " + "else if (overdueStatus == 2) { # " + "#= 'Sắp quá hạn' #" + "#} "+ "else if (overdueStatus == 3) { # " + "#= 'Quá hạn' #" + "#} "+ "else { # " + "#= '' #" + "#} #",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false,
                    },
                    {
                        title: "Trạng thái tiếp xúc",
                        width: '90px',
                        field:"status",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false,
                        template: function(dataItem){
                        	for(var i = 0; i<vm.statusList.length; i++){
                        		if(dataItem.status==vm.statusList[i].code){
                        			return vm.statusList[i].name;
                        		}
                        	}
                        }
                    },
                    {
                        title: "Nhu cầu khách hàng",
                        width: '150px',
                        field:"contentCustomer",
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;white-space:normal;"
                        },
                        type :'text',
                        editable:false,
                        hidden: true
                    },
                    {

                        title: "Thao tác",
                        type :'text',
                        editable:false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        template: function(dataItem) {
                        	var html =  '<div class="text-center">' +
                        	'<button style=" border: none; background-color: white;" id="approveRose"' +
                            'class=" icon_table" ng-click="vm.approveRose(dataItem)" ng-show="dataItem.status==8 && vm.checkRoleApproved"  uib-tooltip="Phê duyệt" translate' + '>' +
                            '<i class="fa fa-check-circle" style="color: #00FF00;" aria-hidden="true"></i>' +
                            '</button>' +
                                '<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.edit(dataItem)" class=" icon_table " ng-show="(dataItem.createdUser==vm.userLoginId || vm.checkRoleUpdate)" ng-hide="dataItem.status==9" ' +
                                '   uib-tooltip="Sửa" translate>' +
                                '<i class="fa fa-pencil" aria-hidden="true"></i>' +
                                '</button>' +
                                '<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.view(dataItem)" class=" icon_table ng-show="(dataItem.status==1 || dataItem.status==2) && dataItem.performerId==vm.userLoginId"" '+
            					'   uib-tooltip="Xem chi tiết" translate>'+
            					'<i class="fa fa-list-alt" style="color:#e0d014"   aria-hidden="true"></i>'+
            					'</button>' +
            					'<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.confirmReject(dataItem)" class=" icon_table " ng-show="(dataItem.status==4) && vm.checkRoleApproved" ng-disabled="dataItem.status==10" ' +
                                '   uib-tooltip="Kết quả tiếp xúc" translate>' +
                                '<i class="fa fa-check" style="color: #58c78a;" aria-hidden="true"></i>' +
                                '</button>' +
                                '<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.confirmRejectGiaiPhap(dataItem)" class=" icon_table " ng-show="(dataItem.status==7) && vm.checkRoleApproved" ng-disabled="dataItem.status==10" ' +
                                '   uib-tooltip="Kết quả giải pháp" translate>' +
                                '<i class="fa fa-check" style="color: #58c78a;" aria-hidden="true"></i>' +
                                '</button>' +
                                '<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.remove(dataItem)" class=" icon_table " ng-show="dataItem.status==1 && dataItem.createdUser==vm.userLoginId" ng-disabled="dataItem.status==10" ' +
                                '   uib-tooltip="Xoá" translate>' +
                                '<i class="fa fa-trash" style="color: #337ab7;" aria-hidden="true"></i>' +
                                '</button>' +
                                '</div>';
                        	return html;
						},
						width: '150px',
						field:    "action",
                    },
    ]
    });
		}
		
        vm.cancelConfirmPopup = cancelConfirmPopup;
        function cancelConfirmPopup(){
            CommonService.dismissPopup1();
            
        }
		
		vm.checkAll8 = function(){
			if(vm.resultForm.package8){
				vm.resultForm.package81 = true;
				vm.resultForm.package82 = true;
				vm.resultForm.package83 = true;
				vm.resultForm.package84 = true;
			} else {
				vm.resultForm.package81 = false;
				vm.resultForm.package82 = false;
				vm.resultForm.package83 = false;
				vm.resultForm.package84 = false;
			}
		}
		
		vm.checkKoThietKe2 = function(param){
			if(param == "information21"){
				vm.resultNoForm.information21=="1"
				vm.resultNoForm.information22 = "0";
				vm.resultNoForm.information23 = "0";
				
				vm.resultNoForm.information221 = null;
				vm.resultNoForm.information222 = null;
				
				vm.resultNoForm.information231 = null;
				vm.resultNoForm.information232 = null;
				vm.resultNoForm.information233 = null;
				vm.resultNoForm.information234 = null;
				vm.resultNoForm.information235 = null;
				
			} else if(param == "information22"){
				vm.resultNoForm.information22=="1"
				vm.resultNoForm.information21 = "0";
				vm.resultNoForm.information23 = "0";
				
				vm.resultNoForm.information211 = null;
				vm.resultNoForm.information212 = null;
				vm.resultNoForm.information213 = null;
				
				vm.resultNoForm.information231 = null;
				vm.resultNoForm.information232 = null;
				vm.resultNoForm.information233 = null;
				vm.resultNoForm.information234 = null;
				vm.resultNoForm.information235 = null;
				
			} else if(param == "information23"){
				vm.resultNoForm.information23=="1"
				vm.resultNoForm.information21 = "0";
				vm.resultNoForm.information22 = "0";
				
				vm.resultNoForm.information211 = null;
				vm.resultNoForm.information212 = null;
				vm.resultNoForm.information213 = null;
				
				vm.resultNoForm.information221 = null;
				vm.resultNoForm.information222 = null;
			}
		}
		
		vm.clearInput = function(param){
			if(param == 'keySearch'){
				vm.searchForm.keySearch = null;
			}
			
			if(param == 'status'){
				vm.searchForm.lstStatus = null;
			}
			
			if(param == 'createdDate'){
				vm.searchForm.createdDateFrom = null;
				vm.searchForm.createdDateTo = null;
			}
			
			if(param == 'performerName'){
				vm.searchForm.performerId = null;
	            vm.searchForm.performerName = null;
			}
			
			if(param == 'suggestTime'){
				vm.searchForm.suggestTimeFrom = null;
				vm.searchForm.suggestTimeTo = null;
			}
			
			if(param == 'provinceCode'){
				vm.searchForm.provinceCode = null;
				vm.searchForm.provinceId = null;
				vm.searchForm.provinceName = null;
			}
			if(param == 'districtName'){
				vm.searchForm.districtName = null;
			}
			if(param == 'communeName'){
				vm.searchForm.communeName = null;
			}
		}
		
	    //Autosuccess Nhân viên
		vm.staffOptions = {
		        dataTextField: "staffName",
		        dataValueField: "staffId",
				placeholder:"Nhập mã hoặc tên nhân viên",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.searchForm.performerId = dataItem.staffId;
		            vm.searchForm.performerName = dataItem.staffName;
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
		                    return Restangular.all("configStaffTangentRestService/catProvince/doSearchStaffByPopup").post({
		                        staffName: vm.searchForm.performerName,
		                        pageSize: vm.staffOptions.pageSize,
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
				'<p class="col-md-6 text-header-auto border-right-ccc">Mã NV</p>' +
				'<p class="col-md-6 text-header-auto">Tên NV</p>' +
				'</div>',
		        template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.staffCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.staffName #</div> </div>',
		        change: function (e) {
		        	if (!vm.isSelect) {
		        		vm.searchForm.performerId = null;
			            vm.searchForm.performerName = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.searchForm.performerId = null;
			            vm.searchForm.performerName = null;
		            }
		        }
		    }
	    
		//save Nhân viên popup tìm kiếm
	    vm.onSaveStaffSearch = onSaveStaffSearch;
		function onSaveStaffSearch(dataItem){
			vm.searchForm.performerId = dataItem.staffId;
        	vm.searchForm.performerName = dataItem.staffName;
	        htmlCommonService.dismissPopup();
	    };
		
		//Auto success mã nhân viên tiếp xúc
		vm.performerNameOptions = {
		        dataTextField: "staffName",
		        dataValueField: "staffId",
				placeholder:"Nhân viên tiếp xúc cấu hình theo tỉnh",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.addForm.performerId = dataItem.staffId;
		        	vm.addForm.performerName = dataItem.staffName;
		        	vm.addForm.performerPhoneNumber = dataItem.staffPhone;
		        	vm.addForm.performerEmail = dataItem.email;
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
		                    return Restangular.all("configStaffTangentRestService/catProvince/doSearchStaffByPopup").post({
		                        staffName: vm.addForm.performerName,
		                        pageSize: vm.performerNameOptions.pageSize,
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
				'<p class="col-md-6 text-header-auto border-right-ccc">Mã NV</p>' +
				'<p class="col-md-6 text-header-auto">Tên NV</p>' +
				'</div>',
		        template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.staffCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.staffName #</div> </div>',
		        change: function (e) {
		        	if (!vm.isSelect) {
		        		vm.addForm.performerId = null;
			        	vm.addForm.performerName = null;
			        	vm.addForm.performerPhoneNumber = null;
			        	vm.addForm.performerEmail = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.addForm.performerId = null;
			        	vm.addForm.performerName = null;
			        	vm.addForm.performerPhoneNumber = null;
			        	vm.addForm.performerEmail = null;
		            }
		        }
		    }
		
		//Auto success mã nhân viên giải pháp
		vm.performerSolutionNameOptions = {
		        dataTextField: "staffName",
		        dataValueField: "staffId",
				placeholder:"Nhân viên giải pháp cấu hình theo tỉnh",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.addForm.performerSolutionId = dataItem.staffId;
		        	vm.addForm.performerSolutionName = dataItem.staffName;
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
		                    return Restangular.all("configStaffTangentRestService/catProvince/doSearchStaffByPopup").post({
		                        staffName: vm.addForm.performerSolutionName,
		                        pageSize: vm.performerSolutionNameOptions.pageSize,
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
				'<p class="col-md-6 text-header-auto border-right-ccc">Mã NV</p>' +
				'<p class="col-md-6 text-header-auto">Tên NV</p>' +
				'</div>',
		        template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.staffCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.staffName #</div> </div>',
		        change: function (e) {
		        	if (!vm.isSelect) {
		        		vm.addForm.performerSolutionId = null;
			        	vm.addForm.performerSolutionName = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.addForm.performerSolutionId = null;
			        	vm.addForm.performerSolutionName = null;
		            }
		        }
		    }
		
		//open popup Nhân viên
		vm.openStaffPopup = openStaffPopup;
		function openStaffPopup(param){
			var templateUrl = 'coms/popup/staffSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm nhân viên");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, param, 'ggfd', false, '75%','75%','staffSearchController');
	    }
		
		//save Nhân viên tiếp xúc popup tạo mới
		vm.onSaveStaff = onSaveStaff;
		function onSaveStaff(dataItem){
			vm.addForm.performerId = dataItem.staffId;
        	vm.addForm.performerName = dataItem.staffName;
        	vm.addForm.performerPhoneNumber = dataItem.staffPhone;
        	vm.addForm.performerEmail = dataItem.email;
	        htmlCommonService.dismissPopup();
	    };
	    
	  //save Nhân viên chuẩn bị giải pháp popup tạo mới
		vm.onSaveStaffSolution = onSaveStaffSolution;
		function onSaveStaffSolution(dataItem){
			vm.addForm.performerSolutionId = dataItem.staffId;
        	vm.addForm.performerSolutionName = dataItem.staffName;
	        htmlCommonService.dismissPopup();
	    };
	    
	  //open popup Nhân viên được giao
		vm.openStaffConfirmPopup = openStaffConfirmPopup;
		function openStaffConfirmPopup(param){
			var templateUrl = 'coms/popup/staffSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm nhân viên");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, param, 'ggfd', false, '75%','75%','staffConfirmSearchController');
	    }
		
		//save Nhân viên tiếp xúc được giao tiếp
		vm.onSaveStaffTiepXuc = onSaveStaffTiepXuc;
		function onSaveStaffTiepXuc(dataItem){
			vm.resultForm.approvedPerformerId = dataItem.staffId;
        	vm.resultForm.approvedPerformerName = dataItem.staffName;
	        htmlCommonService.dismissPopup();
	    };
	    
	  //save Nhân viên giải pháp được giao tiếp
		vm.onSaveStaffGiaiPhap = onSaveStaffGiaiPhap;
		function onSaveStaffGiaiPhap(dataItem){
			vm.solutionForm.approvedPerformerId = dataItem.staffId;
        	vm.solutionForm.approvedPerformerName = dataItem.staffName;
        	vm.solutionForm.approvedPerformerEmail = dataItem.email;
        	vm.solutionForm.approvedPerformerPhone = dataItem.staffPhone;
	        htmlCommonService.dismissPopup();
	    };
	    
	  //Auto success mã nhân viên giải pháp tiếp theo
		vm.performerGiaiPhapNextOptions = {
		        dataTextField: "staffName",
		        dataValueField: "staffId",
				placeholder:"Nhập mã hoặc tên nhân viên",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.solutionForm.approvedPerformerId = dataItem.staffId;
		        	vm.solutionForm.approvedPerformerName = dataItem.staffName;
		        	vm.solutionForm.approvedPerformerEmail = dataItem.email;
		        	vm.solutionForm.approvedPerformerPhone = dataItem.staffPhone;
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
		                    return Restangular.all("configStaffTangentRestService/catProvince/doSearchStaffByConfigProvinceId").post({
		                        staffName: vm.solutionForm.approvedPerformerName,
		                        pageSize: vm.performerGiaiPhapNextOptions.pageSize,
		                        type: "1",
		                        catProvinceId: vm.provinceId,
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
				'<p class="col-md-6 text-header-auto border-right-ccc">Mã NV</p>' +
				'<p class="col-md-6 text-header-auto">Tên NV</p>' +
				'</div>',
		        template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.staffCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.staffName #</div> </div>',
		        change: function (e) {
		        	if (!vm.isSelect) {
		        		vm.solutionForm.approvedPerformerId = null;
			        	vm.solutionForm.approvedPerformerName = null;
			        	vm.solutionForm.approvedPerformerEmail = null;
			        	vm.solutionForm.approvedPerformerPhone = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.solutionForm.approvedPerformerId = null;
			        	vm.solutionForm.approvedPerformerName = null;
			        	vm.solutionForm.approvedPerformerEmail = null;
			        	vm.solutionForm.approvedPerformerPhone = null;
		            }
		        }
		    }
		
		//Auto success mã nhân viên tiếp xúc tiếp theo
		vm.performerTiepXucNextOptions = {
		        dataTextField: "staffName",
		        dataValueField: "staffId",
				placeholder:"Nhập mã hoặc tên nhân viên",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            vm.resultForm.approvedPerformerId = dataItem.staffId;
		        	vm.resultForm.approvedPerformerName = dataItem.staffName;
		        	vm.resultForm.approvedPerformerEmail = dataItem.email;
		        	vm.resultForm.approvedPerformerPhone = dataItem.staffPhone;
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
		                    return Restangular.all("configStaffTangentRestService/catProvince/doSearchStaffByConfigProvinceId").post({
		                        staffName: vm.resultForm.approvedPerformerName,
		                        pageSize: vm.performerTiepXucNextOptions.pageSize,
		                        page: 1,
		                        type: "1",
		                        catProvinceId: vm.provinceId,
		                    }).then(function (response) {
		                        options.success(response.data);
		                    }).catch(function (err) {
		                        console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
		                    });
		                }
		            }
		        },
				headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
				'<p class="col-md-6 text-header-auto border-right-ccc">Mã NV</p>' +
				'<p class="col-md-6 text-header-auto">Tên NV</p>' +
				'</div>',
		        template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.staffCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.staffName #</div> </div>',
		        change: function (e) {
		        	if (!vm.isSelect) {
		        		vm.resultForm.approvedPerformerId = null;
			        	vm.resultForm.approvedPerformerName = null;
			        	vm.resultForm.approvedPerformerEmail = null;
			        	vm.resultForm.approvedPerformerPhone = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.resultForm.approvedPerformerId = null;
			        	vm.resultForm.approvedPerformerName = null;
			        	vm.resultForm.approvedPerformerEmail = null;
			        	vm.resultForm.approvedPerformerPhone = null;
		            }
		        }
		    }
	    
	    vm.patternContractOptions={
	            dataTextField: "contractCode", 
	            placeholder:"Mã hợp đồng",
	            open: function(e) {
	                vm.isSelect = false;
	                
	            },
	            select: function(e) {
	                vm.isSelect = true;
	                data = this.dataItem(e.item.index());
	                vm.solutionForm.contractCode = data.contractCode;
	                vm.solutionForm.contractId = data.contractId;
	                vm.solutionForm.signDate = data.signDate;
	                vm.solutionForm.contractPrice = data.contractPrice;
	                vm.solutionForm.guaranteeTime = data.guaranteeTime;
	                vm.solutionForm.constructTime = data.constructTime;
	                vm.solutionForm.contractRose = parseFloat(vm.contractRose * data.contractPrice / 1.1).toFixed(2);
	            },
	            
	            pageSize: 10,
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function(options) {
	                    		return Restangular.all("tangentCustomerRestService/getAllContractXdddSuccess").post({
		                        	createdDate: vm.viewForm.createdDate,
		                        	contractCode: vm.solutionForm.contractCode,
		                        	source: vm.viewForm.source,
			                        pageSize: vm.patternContractOptions.pageSize,
			                        page: 1
		                        }).then(function(response){
		                            options.success(response.data);
		                        }).catch(function (err) {
		                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
		                        });
	                        
	                    }
	                }
	            },
	            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
	            '<p class="col-md-12 text-header-auto">Mã hợp đồng</p>' +
	    
	                '</div>',
	            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.contractCode #</div></div>',
	            change: function (e) {
	                if (!vm.isSelect) {
	                	vm.solutionForm.contractCode = null;
		                vm.solutionForm.contractId = null;
		                vm.solutionForm.signDate = null;
		                vm.solutionForm.guaranteeTime = null;
                		vm.solutionForm.contractRose = null;
                		vm.solutionForm.constructTime = null;
                		vm.solutionForm.price = null;
                		vm.solutionForm.signDate = "";
 		                vm.solutionForm.guaranteeTime = "";
                 		vm.solutionForm.contractRose = "";
                 		vm.solutionForm.constructTime = "";
                 		vm.solutionForm.price = "";
	                }
	            },
	            close: function (e) {
	                if (!vm.isSelect) {
	                	vm.solutionForm.contractCode = null;
		                vm.solutionForm.contractId = null;
		                vm.solutionForm.signDate = null;
		                vm.solutionForm.guaranteeTime = null;
                		vm.solutionForm.contractRose = null;
                		vm.solutionForm.constructTime = null;
                		vm.solutionForm.price = null;
                		vm.solutionForm.signDate = "";
		                vm.solutionForm.guaranteeTime = "";
                		vm.solutionForm.contractRose = "";
                		vm.solutionForm.constructTime = "";
                		vm.solutionForm.price = "";
	                }
	            }
	        };
	    
	    vm.openContract = function() {
        	var templateUrl = 'coms/popup/findContractPopUp.html';
        	var title = gettextCatalog.getString("Tìm kiếm hợp đồng");
        	htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,null, vm, 'fff', 'ggfd', false, '85%','85%','contractSearchController');
        }
	    
	    vm.onSaveContract = function(data){
	    	vm.solutionForm.contractCode = data.contractCode;
            vm.solutionForm.contractId = data.contractId;
            vm.solutionForm.signDate = data.signDate;
            vm.solutionForm.contractPrice = data.contractPrice;
            vm.solutionForm.guaranteeTime = data.guaranteeTime;
            vm.solutionForm.constructTime = data.constructTime;
            vm.solutionForm.contractRose = parseFloat(vm.contractRose * data.contractPrice / 1.1).toFixed(2);
	    }
	    
	  //Tạo mới
        vm.add = function(){
			vm.isCreateNew = true;
			vm.isSearch = false;
			vm.isEdit = false;
			vm.checkRoleUserAssignYctx = false;
			vm.showDetail = false;
			vm.addForm = {
				partnerType: 1,
				registeredCustomerService: '',
				modelOfTheBuilder: ''
			};
			vm.validatePhone = true;
			vm.addForm.isInternalSource = $scope.isInternalSource;
			vm.addForm.isSocialSource = $scope.isSocialSource;
			vm.addForm.isCustomerServiceSource = $scope.isCustomerServiceSource;
			if (vm.addForm.isCustomerServiceSource == 1)	vm.addForm.groupOrder = 1;
			else vm.addForm.groupOrder = 2;
			checkChannelReceptionList();
			
			var teamplateUrl = "coms/manageTangentCustomer/manageTangentCustomerPopup.html";
			var title = "Tạo mới quản lý yêu cầu tiếp xúc";
			var windowId = "ADD_MANAGE_CONFIG";
			CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '80%', '50%', "deptAdd");
		}
        
        //Chỉnh sửa
        vm.edit = function(dataItem){
			vm.isCreateNew = false;
			vm.isSearch = true;
			vm.addForm = angular.copy(dataItem);
			vm.createdUser = dataItem.createdUser;
			checkChannelReceptionList();
			var teamplateUrl = "coms/manageTangentCustomer/manageTangentCustomerPopup.html";
			var title = "Cập nhật quản lý yêu cầu tiếp xúc";
			var windowId = "EDIT_MANAGE_CONFIG";
			debugger;
			vm.addForm.isInternalSource = $scope.isInternalSource;
			vm.addForm.isSocialSource = $scope.isSocialSource;
			vm.addForm.isCustomerServiceSource = $scope.isCustomerServiceSource;
			vm.addForm.performerSolutionId = dataItem.performerSolutionId;
			vm.addForm.performerId = dataItem.performerId;
			vm.addForm.oldPhone = dataItem.customerPhone;
			vm.customer();
			if((vm.addForm.status=="1" || vm.addForm.status=="3" || vm.addForm.status=="5" || vm.addForm.status=="6" || vm.addForm.status=="7"|| vm.addForm.status=="11") && (vm.addForm.performerSolutionId == vm.userLoginId || vm.addForm.performerId == vm.userLoginId)){
				vm.isEdit = true;
			}else if(vm.userLoginId==vm.createdUser){
				vm.isEdit = true;
			}else if(vm.checkRoleUpdate == true){
				vm.isEdit = true;
			}else vm.isEdit = false;
			
			if((vm.addForm.status=="1" || vm.addForm.status=="2") && vm.checkRoleUserPgd) {
				vm.checkRoleUserAssignYctx = true;
			}
			
			var obj = angular.copy(dataItem);
			CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '80%', '80%', "deptAdd");
			
			
			
		}
        
        vm.downloadFile= downloadFile;
        function downloadFile(dataItem) {
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
        }
        
        vm.downloadFileHis1= downloadFileHis1;
        function downloadFileHis1(dataItem) {
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
        }
        
        vm.downloadFileHis2= downloadFileHis2;
        function downloadFileHis2(dataItem) {
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
        }
        
        vm.downloadFileHis3= downloadFileHis3;
        function downloadFileHis3(dataItem) {
            window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataItem.filePath;
        }
        
        //Chi tiết
        vm.view = function(dataItem){
        	vm.checkPerform = true;
			vm.isCreateNew = false;
			vm.isSearch = true;
			vm.showDetail=true;
			vm.viewForm = angular.copy(dataItem);
			vm.tangentCustomerId = dataItem.tangentCustomerId;
			vm.provinceId = dataItem.provinceId;
			vm.performerSolutionId = dataItem.performerSolutionId;
			vm.performerId = dataItem.performerId;
			vm.fileLst = [];
			vm.fileLstSoDo = [];
			vm.fileLstDiaHinh = [];
			vm.fileLstGiaiPhap=[];
			vm.showBtnSaveGiaiPhap = false;
			if(vm.userLoginId!=vm.viewForm.performerId){
				vm.checkPerform = false;
			}
			vm.customer();
			if((vm.viewForm.status=="3" || vm.viewForm.status=="5" || vm.viewForm.status=="6" || vm.viewForm.status=="7") && (vm.performerSolutionId == vm.userLoginId || vm.performerId == vm.userLoginId)){
				vm.showBtnSaveGiaiPhap = true;
			}
			console.log(vm.resultForm.status);
			viewData(dataItem);
        }
	    
	    //Chi tiết lịch sử
        vm.viewDetailHis = viewDetailHis;
        function viewDetailHis(dataItem){
        	var teamplateUrl = "coms/manageTangentCustomer/viewDetailTiepXucPopup.html";
			var title = "Chi tiết lịch sử quản lý yêu cầu tiếp xúc";
			var windowId = "DETAIL_HIS_MANAGE_CONFIG";
			vm.customer();
			manageTangentCustomerService.getListResultTangentByResultTangentId({resultTangentId: dataItem.resultTangentId,tangentCustomerId: dataItem.tangentCustomerId,partnerType:dataItem.partnerType}).then(function(data){
				if(data.plain()==null){
					vm.resultFormDetail = {};
				} else {
					vm.resultFormDetail = angular.copy(data.plain());
				}
				if(vm.resultFormDetail!=null){
//        			if(vm.resultFormDetail.package1=="1"){
//        				vm.resultFormDetail.package1 = true;
//        			} else {
//        				vm.resultFormDetail.package1 = false;
//        			}
//        			
//        			if(vm.resultFormDetail.package2=="1"){
//        				vm.resultFormDetail.package2 = true;
//        			} else {
//        				vm.resultFormDetail.package2 = false;
//        			}
//        			
//        			if(vm.resultFormDetail.package3=="1"){
//        				vm.resultFormDetail.package3 = true;
//        			} else {
//        				vm.resultFormDetail.package3 = false;
//        			}
//        			
//        			if(vm.resultFormDetail.package4=="1"){
//        				vm.resultFormDetail.package4 = true;
//        			} else {
//        				vm.resultFormDetail.package4 = false;
//        			}
//        			
//        			if(vm.resultFormDetail.package5=="1"){
//        				vm.resultFormDetail.package5 = true;
//        			} else {
//        				vm.resultFormDetail.package5 = false;
//        			}
//        			
//        			if(vm.resultFormDetail.package6=="1"){
//        				vm.resultFormDetail.package6 = true;
//        			} else {
//        				vm.resultFormDetail.package6 = false;
//        			}
//        			
//        			if(vm.resultFormDetail.package7=="1"){
//        				vm.resultFormDetail.package7 = true;
//        			} else {
//        				vm.resultFormDetail.package7 = false;
//        			}
//        			
//        			if(vm.resultFormDetail.package8!=null){
//            			var lstPackage = vm.resultFormDetail.package8.split(";");
//            			for(var i=0;i<lstPackage.length;i++){
//            				if(lstPackage[i]=="1"){
//            					vm.resultFormDetail.package81 = true;
//            				} else if(lstPackage[i]=="2"){
//            					vm.resultFormDetail.package82 = true;
//            				} else if(lstPackage[i]=="3"){
//            					vm.resultFormDetail.package83 = true;
//            				} else if(lstPackage[i]=="4"){
//            					vm.resultFormDetail.package84 = true;
//            				}
//            			}
//            			vm.resultFormDetail.package8=true;
//        			}
//        			
//        			if(vm.resultFormDetail.package9=="1"){
//        				vm.resultFormDetail.package9 = true;
//        			} else {
//        				vm.resultFormDetail.package9 = false;
//        			}
        			////////
        			vm.resultYesFormDetail = vm.resultFormDetail.resultTangentDetailYesDTO;
        			if(vm.resultYesFormDetail!=null){
        				if(vm.resultYesFormDetail.information42!=null){
            				vm.resultYesFormDetail.information421 = vm.resultYesFormDetail.information42.split(";")[0];
                			vm.resultYesFormDetail.information422 = vm.resultYesFormDetail.information42.split(";")[1];
                			vm.resultYesFormDetail.information423 = vm.resultYesFormDetail.information42.split(";")[2];
            			}
            			
            			if(vm.resultYesFormDetail.information53=="1"){
            				vm.resultYesFormDetail.information53 = true;
            			} else {
            				vm.resultYesFormDetail.information53 = false;
            			}
            			
            			if(vm.resultYesFormDetail.information54=="1"){
            				vm.resultYesFormDetail.information54 = true;
            			} else {
            				vm.resultYesFormDetail.information54 = false;
            			}
            			
            			if(vm.resultYesFormDetail.information55=="1"){
            				vm.resultYesFormDetail.information55 = true;
            			} else {
            				vm.resultYesFormDetail.information55 = false;
            			}
            			fillFileTable(vm.resultYesFormDetail.lstImageDesign);
        				vm.resultYesFormDetail.information511 = vm.resultYesFormDetail.information1_1;
            			vm.resultYesFormDetail.information521 = vm.resultYesFormDetail.information1_21;
            			vm.resultYesFormDetail.information522 = vm.resultYesFormDetail.information1_22;
            			vm.resultYesFormDetail.information523 = vm.resultYesFormDetail.information1_23;
            			vm.resultYesFormDetail.information531 = vm.resultYesFormDetail.information1_3;
            			vm.resultYesFormDetail.information4 = vm.resultYesFormDetail.information3;
            			vm.resultYesFormDetail.information71 = vm.resultYesFormDetail.information3_1;
            			vm.resultYesFormDetail.information72 = vm.resultYesFormDetail.information3_2;
            			vm.resultYesFormDetail.information73 = vm.resultYesFormDetail.information3_3;
            			vm.resultYesFormDetail.information74 = vm.resultYesFormDetail.information3_4;
            			vm.resultYesFormDetail.information75 = vm.resultYesFormDetail.information3_5;
            			vm.resultYesFormDetail.information76 = vm.resultYesFormDetail.information3_6;
            			vm.resultYesFormDetail.information77 = vm.resultYesFormDetail.information3_7;
            			vm.resultYesFormDetail.information78 = vm.resultYesFormDetail.information3_8;
            			vm.resultYesFormDetail.information79 = vm.resultYesFormDetail.information3_9;
        			}
        			
        			////////
					vm.resultNoFormDetail = vm.resultFormDetail.resultTangentDetailNoDTO;
					if(vm.resultNoFormDetail!=null){
						//Hình chữ nhật
//						if(vm.resultNoFormDetail.information21!=null){
//							vm.resultNoFormDetail.information211 = vm.resultNoFormDetail.information21.split(";")[0];
//							vm.resultNoFormDetail.information212 = vm.resultNoFormDetail.information21.split(";")[1];
//							vm.resultNoFormDetail.information213 = vm.resultNoFormDetail.information21.split(";")[2];
//						}
						//Hình vuông
//						if(vm.resultNoFormDetail.information22!=null){
//							vm.resultNoFormDetail.information221 = vm.resultNoFormDetail.information22.split(";")[0];
//							vm.resultNoFormDetail.information222 = vm.resultNoFormDetail.information22.split(";")[1];
//						}
						//Hình tứ giác
//						if(vm.resultNoFormDetail.information23!=null){
//							vm.resultNoFormDetail.information231 = vm.resultNoFormDetail.information23.split(";")[0];
//							vm.resultNoFormDetail.information232 = vm.resultNoFormDetail.information23.split(";")[1];
//							vm.resultNoFormDetail.information233 = vm.resultNoFormDetail.information23.split(";")[2];
//							vm.resultNoFormDetail.information234 = vm.resultNoFormDetail.information23.split(";")[3];
//							vm.resultNoFormDetail.information235 = vm.resultNoFormDetail.information23.split(";")[4];
//						}
						//Nhà xung quanh
//						if(vm.resultNoFormDetail.information52!=null){
//							vm.resultNoFormDetail.information521 = vm.resultNoFormDetail.information52.split(";")[0];
//							vm.resultNoFormDetail.information522 = vm.resultNoFormDetail.information52.split(";")[1];
//							vm.resultNoFormDetail.information523 = vm.resultNoFormDetail.information52.split(";")[2];
//						}
						//Đua ban công
						if(vm.resultNoFormDetail.information92!=null){
//							var lstInfo92 = vm.resultNoFormDetail.information92.split(";");
//							for(var i=0;i<lstInfo92.length;i++){
//								if(lstInfo92[i]=="1"){
//									vm.resultNoFormDetail.information921 = true;
//								}
//								if(lstInfo92[i]=="2"){
//									vm.resultNoFormDetail.information923 = true;
//								}
//								if(lstInfo92[i]=="3"){
//									vm.resultNoFormDetail.information922 = true;
//								}
//								if(lstInfo92[i]=="4"){
//									vm.resultNoFormDetail.information924 = true;
//								}
//							}
						}
						//Sân vườn
//						if(vm.resultNoFormDetail.information11!=null){
//							var lstInfo11 = vm.resultNoFormDetail.information11.split(";");
//							for(var i=0;i<lstInfo11.length;i++){
//								if(lstInfo11[i]=="1"){
//									vm.resultNoFormDetail.information111 = true;
//								}
//								if(lstInfo11[i]=="2"){
//									vm.resultNoFormDetail.information112 = true;
//								}
//								if(lstInfo11[i]=="3"){
//									vm.resultNoFormDetail.information113 = true;
//								}
//								
//							}
//						}
						//Xe lam
//						if(vm.resultNoFormDetail.information133=="1"){
//							vm.resultNoFormDetail.information133 = true;
//						} else {
//							vm.resultNoFormDetail.information133 = false;
//						}
//						
//						if(vm.resultNoFormDetail.information134=="1"){
//							vm.resultNoFormDetail.information134 = true;
//						} else {
//							vm.resultNoFormDetail.information134 = false;
//						}
//						
//						if(vm.resultNoFormDetail.information135=="1"){
//							vm.resultNoFormDetail.information135 = true;
//						} else {
//							vm.resultNoFormDetail.information135 = false;
//						}
						
						//Yêu cầu ngoài
//						if(vm.resultNoFormDetail.information151=="1"){
//							vm.resultNoFormDetail.information151 = true;
//						} else {
//							vm.resultNoFormDetail.information151 = false;
//						}
//						
//						if(vm.resultNoFormDetail.information152=="1"){
//							vm.resultNoFormDetail.information152 = true;
//						} else {
//							vm.resultNoFormDetail.information152 = false;
//						}
//						
//						if(vm.resultNoFormDetail.information153!=null){
//							var lstInfo153 = vm.resultNoFormDetail.information153.split(";");
//							for(var i=0;i<lstInfo153.length;i++){
//								if(lstInfo153[i]=="1"){
//									vm.resultNoFormDetail.information1531 = true;
//								}
//								if(lstInfo153[i]=="2"){
//									vm.resultNoFormDetail.information1532 = true;
//								}
//								if(lstInfo153[i]=="3"){
//									vm.resultNoFormDetail.information1533 = true;
//								}
//								if(lstInfo153[i]=="4"){
//									vm.resultNoFormDetail.information1534 = true;
//								}
//							}
//						}
//						
//						if(vm.resultNoFormDetail.information154!=null && vm.resultNoFormDetail.information154=="1"){
//							vm.resultNoFormDetail.information154 = true;
//						} else {
//							vm.resultNoFormDetail.information154 = false;
//						}
						//Nhân khẩu
//						if(vm.resultNoFormDetail.information62!=null){
//							var lstInfo62 = vm.resultNoFormDetail.information62.split(";");
//							if(lstInfo62.length==1){
//								vm.resultNoFormDetail.information621 = lstInfo62[0];
//							} else
//							if(lstInfo62.length==2){
//								vm.resultNoFormDetail.information622 = lstInfo62[1];
//							} else {
//								var year62 = [];
//								for(var i=0;i<lstInfo62.length;i++){
//									if(i>0){
//										year62.push(lstInfo62[i]);
//									} else {
//										
//									}
//								}
//								vm.resultNoFormDetail.information622 = (year62.size()>0) ? year62.join(";") : null;
//							}
//						}
//						
//						if(vm.resultNoFormDetail.information63!=null){
//							var lstInfo63 = vm.resultNoFormDetail.information63.split(";");
//							if(lstInfo63.length==1){
//								vm.resultNoFormDetail.information631 = lstInfo63[0];
//							} else
//							if(lstInfo63.length==2){
//								vm.resultNoFormDetail.information632 = lstInfo63[1];
//							} else {
//								var year63 = [];
//								for(var i=0;i<lstInfo63.length;i++){
//									if(i>0){
//										year63.push(lstInfo63[i]);
//									}
//								}
//								vm.resultNoFormDetail.information632 = (year63.size()>0) ? year63.join(";") : null;
//							}
//						}
//						
//						if(vm.resultNoFormDetail.information64!=null){
//							var lstInfo64 = vm.resultNoFormDetail.information64.split(";");
//							if(lstInfo64.length==1){
//								vm.resultNoFormDetail.information641 = lstInfo64[0];
//							} else
//							if(lstInfo64.length==2){
//								vm.resultNoFormDetail.information642 = lstInfo64[1];
//							} else {
//								var year64 = [];
//								for(var i=0;i<lstInfo64.length;i++){
//									if(i>0){
//										year64.push(lstInfo64[i]);
//									}
//								}
//								vm.resultNoFormDetail.information642 = (year64.size()>0) ? year64.join(";") : null;
//							}
//						}
//						
//						if(vm.resultNoFormDetail.information72=="1"){
//							vm.resultNoFormDetail.information72 = true;
//						} else {
//							vm.resultNoFormDetail.information72 = false;
//						}
						
					}
        			
        		}
				
				CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '90%', '90%', "deptAdd");
				setTimeout(function(){
					modalInstance2 = CommonService.getModalInstance1();
				},100);
			});
        }
        
        
        function viewData(dataItem){
				var teamplateUrl = "coms/manageTangentCustomer/manageTangentCustomerDetailPopup.html";
				var title = "Chi tiết quản lý yêu cầu tiếp xúc";
				var windowId = "DETAIL_MANAGE_CONFIG";
	        	for(var i=0; i<vm.statusList.length; i++){
					if(vm.statusList[i].code==dataItem.status){
						vm.statusName = vm.statusList[i].name;
					}
				}
	        	vm.viewForm = angular.copy(dataItem);
	        	vm.viewForm.oldStatus = dataItem.status;
	        	vm.viewForm.isInternalSource = $scope.isInternalSource;
				vm.viewForm.isSocialSource = $scope.isSocialSource;
				vm.viewForm.isCustomerServiceSource = $scope.isCustomerServiceSource;
				vm.performerName = (dataItem.performerCode!=null ? dataItem.performerCode : "") + " - " + (dataItem.performerName!=null ? dataItem.performerName : "");
				vm.performerSolutionName = (dataItem.performerSolutionCode!=null ? dataItem.performerSolutionCode : "") + " - " + (dataItem.performerSolutionName!=null ? dataItem.performerSolutionName : "");
				
				if((vm.viewForm.performerSolutionId == vm.userLoginId || vm.viewForm.performerId == vm.userLoginId))	vm.isEdit = true;
				else vm.isEdit = false;
				
				manageTangentCustomerService.getListResultTangentByTangentCustomerId({tangentCustomerId: dataItem.tangentCustomerId,partnerType:dataItem.partnerType}).then(function(data){
					if(data.plain().resultTangentDTO==null){
						vm.resultForm = {};
					} else {
						vm.resultForm = angular.copy(data.plain().resultTangentDTO);
					}
					if(vm.resultForm!=null){
//						if(vm.resultForm.fileReceipts!=null){
//							for(var i=0; i<vm.resultForm.fileReceipts.length; i++){
//								vm.fileLst.push(vm.resultForm.fileReceipts[i]);
//							}
//						}
//	        			if(vm.resultForm.package1=="1"){
//	        				vm.resultForm.package1 = true;
//	        			} else {
//	        				vm.resultForm.package1 = false;
//	        			}
//	        			
//	        			if(vm.resultForm.package2=="1"){
//	        				vm.resultForm.package2 = true;
//	        			} else {
//	        				vm.resultForm.package2 = false;
//	        			}
//	        			
//	        			if(vm.resultForm.package3=="1"){
//	        				vm.resultForm.package3 = true;
//	        			} else {
//	        				vm.resultForm.package3 = false;
//	        			}
//	        			
//	        			if(vm.resultForm.package4=="1"){
//	        				vm.resultForm.package4 = true;
//	        			} else {
//	        				vm.resultForm.package4 = false;
//	        			}
//	        			
//	        			if(vm.resultForm.package5=="1"){
//	        				vm.resultForm.package5 = true;
//	        			} else {
//	        				vm.resultForm.package5 = false;
//	        			}
//	        			
//	        			if(vm.resultForm.package6=="1"){
//	        				vm.resultForm.package6 = true;
//	        			} else {
//	        				vm.resultForm.package6 = false;
//	        			}
//	        			
//	        			if(vm.resultForm.package7=="1"){
//	        				vm.resultForm.package7 = true;
//	        			} else {
//	        				vm.resultForm.package7 = false;
//	        			}
//	        			
//	        			if(vm.resultForm.package8!=null){
//	            			var lstPackage = vm.resultForm.package8.split(";");
//	            			for(var i=0;i<lstPackage.length;i++){
//	            				if(lstPackage[i]=="1"){
//	            					vm.resultForm.package81 = true;
//	            				} else if(lstPackage[i]=="2"){
//	            					vm.resultForm.package82 = true;
//	            				} else if(lstPackage[i]=="3"){
//	            					vm.resultForm.package83 = true;
//	            				} else if(lstPackage[i]=="4"){
//	            					vm.resultForm.package84 = true;
//	            				}
//	            			}
//	            			vm.resultForm.package8=true;
//	        			}
//	        			
//	        			if(vm.resultForm.package9=="1"){
//	        				vm.resultForm.package9 = true;
//	        			} else {
//	        				vm.resultForm.package9 = false;
//	        			}
	        			////////
	        			if(vm.resultForm.resultTangentDetailYesDTO==null){
	        				vm.resultYesForm = {};
	        				vm.resultYesForm.yearDoB = vm.viewForm.birthYear;
	        				vm.resultYesForm.detailCustomer = {};
	        				vm.changeInfor(vm.viewForm.partnerType);
	        				vm.disabledPartnerType = false;
	        				vm.isInitialResultTangent = true;
	        			} else {
	        				vm.resultYesForm = vm.resultForm.resultTangentDetailYesDTO;
	        				fillFileTable(vm.resultYesForm.lstImageDesign);
	        				vm.isInitialResultTangent = false;
	        				vm.resultYesForm.yearDoB = vm.viewForm.birthYear;
	        				if(vm.resultYesForm.detailCustomer == null || vm.resultYesForm.detailCustomer == undefined){
	        					vm.resultYesForm.detailCustomer = {};
	        					vm.changeInfor(vm.viewForm.partnerType);
	        				}
	        				
	        				vm.disabledPartnerType = false;
	        				vm.resultYesForm.information511 = vm.resultYesForm.information1_1;
	            			vm.resultYesForm.information521 = vm.resultYesForm.information1_21;
	            			vm.resultYesForm.information522 = vm.resultYesForm.information1_22;
	            			vm.resultYesForm.information523 = vm.resultYesForm.information1_23;
	            			vm.resultYesForm.information531 = vm.resultYesForm.information1_3;
	            			vm.resultYesForm.information4 = vm.resultYesForm.information3;
	            			vm.resultYesForm.information71 = vm.resultYesForm.information3_1;
	            			vm.resultYesForm.information72 = vm.resultYesForm.information3_2;
	            			vm.resultYesForm.information73 = vm.resultYesForm.information3_3;
	            			vm.resultYesForm.information74 = vm.resultYesForm.information3_4;
	            			vm.resultYesForm.information75 = vm.resultYesForm.information3_5;
	            			vm.resultYesForm.information76 = vm.resultYesForm.information3_6;
	            			vm.resultYesForm.information77 = vm.resultYesForm.information3_7;
	            			vm.resultYesForm.information78 = vm.resultYesForm.information3_8;
	            			vm.resultYesForm.information79 = vm.resultYesForm.information3_9;
	            			
	        			}
//	        			if(vm.resultYesForm!=null){
//	        				if(vm.resultYesForm.information42!=null){
//	            				vm.resultYesForm.information421 = vm.resultYesForm.information42.split(";")[0];
//	                			vm.resultYesForm.information422 = vm.resultYesForm.information42.split(";")[1];
//	                			vm.resultYesForm.information423 = vm.resultYesForm.information42.split(";")[2];
//	            			}
//	            			
//	            			if(vm.resultYesForm.information53=="1"){
//	            				vm.resultYesForm.information53 = true;
//	            			} else {
//	            				vm.resultYesForm.information53 = false;
//	            			}
//	            			
//	            			if(vm.resultYesForm.information54=="1"){
//	            				vm.resultYesForm.information54 = true;
//	            			} else {
//	            				vm.resultYesForm.information54 = false;
//	            			}
//	            			
//	            			if(vm.resultYesForm.information55=="1"){
//	            				vm.resultYesForm.information55 = true;
//	            			} else {
//	            				vm.resultYesForm.information55 = false;
//	            			}
//	            			
//	            			if(vm.resultYesForm.information43=="1"){
//	            				vm.resultYesForm.information431 = true;
//	            			}
//	            			
//	            			if(vm.resultYesForm.information43=="2"){
//	            				vm.resultYesForm.information432 = true;
//	            			}
//	            			
//	            			if(vm.resultYesForm.information43=="3"){
//	            				vm.resultYesForm.information433 = true;
//	            			}
//	        			}
	        			
	        			////////
	        			if(vm.resultForm.resultTangentDetailNoDTO==null){
	        				vm.resultNoForm = {};
	        			} else {
	        				vm.resultNoForm = vm.resultForm.resultTangentDetailNoDTO;
	        			}
//						if(vm.resultNoForm!=null){
//							if(vm.resultForm.fileRedBook!=null){
//								for(var i=0; i<vm.resultForm.fileRedBook.length; i++){
//									vm.fileLstSoDo.push(vm.resultForm.fileRedBook[i]);
//								}
//							}
//							if(vm.resultForm.fileTopographic!=null){
//								for(var i=0; i<vm.resultForm.fileTopographic.length; i++){
//									vm.fileLstDiaHinh.push(vm.resultForm.fileTopographic[i]);
//								}
//							}
//							//Hình chữ nhật
//							if(vm.resultNoForm.information21!=null){
//								vm.resultNoForm.information211 = vm.resultNoForm.information21.split(";")[0];
//								vm.resultNoForm.information212 = vm.resultNoForm.information21.split(";")[1];
//								vm.resultNoForm.information213 = vm.resultNoForm.information21.split(";")[2];
//							}
//							//Hình vuông
//							if(vm.resultNoForm.information22!=null){
//								vm.resultNoForm.information221 = vm.resultNoForm.information22.split(";")[0];
//								vm.resultNoForm.information222 = vm.resultNoForm.information22.split(";")[1];
//							}
//							//Hình tứ giác
//							if(vm.resultNoForm.information23!=null){
//								vm.resultNoForm.information231 = vm.resultNoForm.information23.split(";")[0];
//								vm.resultNoForm.information232 = vm.resultNoForm.information23.split(";")[1];
//								vm.resultNoForm.information233 = vm.resultNoForm.information23.split(";")[2];
//								vm.resultNoForm.information234 = vm.resultNoForm.information23.split(";")[3];
//								vm.resultNoForm.information235 = vm.resultNoForm.information23.split(";")[4];
//							}
//							//File sổ đỏ
//							if(vm.resultForm.fileRedBook!=null){
//								vm.resultNoForm.information3 = "1";
//							} else {
//								vm.resultNoForm.information3 = "0";
//							}
//							
//							//Nhà xung quanh
//							if(vm.resultNoForm.information52!=null){
//								vm.resultNoForm.information521 = vm.resultNoForm.information52.split(";")[0];
//								vm.resultNoForm.information522 = vm.resultNoForm.information52.split(";")[1];
//								vm.resultNoForm.information523 = vm.resultNoForm.information52.split(";")[2];
//							}
//							//Đua ban công
//							if(vm.resultNoForm.information92!=null){
//								var lstInfo92 = vm.resultNoForm.information92.split(";");
//								for(var i=0;i<lstInfo92.length;i++){
//									if(lstInfo92[i]=="1"){
//										vm.resultNoForm.information921 = true;
//									}
//									if(lstInfo92[i]=="2"){
//										vm.resultNoForm.information923 = true;
//									}
//									if(lstInfo92[i]=="3"){
//										vm.resultNoForm.information922 = true;
//									}
//									if(lstInfo92[i]=="4"){
//										vm.resultNoForm.information924 = true;
//									}
//								}
//							}
//							//Sân vườn
//							if(vm.resultNoForm.information11!=null){
//								var lstInfo11 = vm.resultNoForm.information11.split(";");
//								for(var i=0;i<lstInfo11.length;i++){
//									if(lstInfo11[i]=="1"){
//										vm.resultNoForm.information111 = true;
//									}
//									if(lstInfo11[i]=="2"){
//										vm.resultNoForm.information112 = true;
//									}
//									if(lstInfo11[i]=="3"){
//										vm.resultNoForm.information113 = true;
//									}
//									
//								}
//							}
//							//Xe lam
//							if(vm.resultNoForm.information133=="1"){
//								vm.resultNoForm.information133 = true;
//							} else {
//								vm.resultNoForm.information133 = false;
//							}
//							
//							if(vm.resultNoForm.information134=="1"){
//								vm.resultNoForm.information134 = true;
//							} else {
//								vm.resultNoForm.information134 = false;
//							}
//							
//							if(vm.resultNoForm.information135=="1"){
//								vm.resultNoForm.information135 = true;
//							} else {
//								vm.resultNoForm.information135 = false;
//							}
//							
//							//Yêu cầu ngoài
//							if(vm.resultNoForm.information151=="1"){
//								vm.resultNoForm.information151 = true;
//							} else {
//								vm.resultNoForm.information151 = false;
//							}
//							
//							if(vm.resultNoForm.information152=="1"){
//								vm.resultNoForm.information152 = true;
//							} else {
//								vm.resultNoForm.information152 = false;
//							}
//							
//							if(vm.resultNoForm.information153!=null){
//								var lstInfo153 = vm.resultNoForm.information153.split(";");
//								for(var i=0;i<lstInfo153.length;i++){
//									if(lstInfo153[i]=="1"){
//										vm.resultNoForm.information1531 = true;
//									}
//									if(lstInfo153[i]=="2"){
//										vm.resultNoForm.information1532 = true;
//									}
//									if(lstInfo153[i]=="3"){
//										vm.resultNoForm.information1533 = true;
//									}
//									if(lstInfo153[i]=="4"){
//										vm.resultNoForm.information1534 = true;
//									}
//								}
//							}
//							
//							if(vm.resultNoForm.information154!=null && vm.resultNoForm.information154=="1"){
//								vm.resultNoForm.information154 = true;
//							} else {
//								vm.resultNoForm.information154 = false;
//							}
//							//Nhân khẩu
//							if(vm.resultNoForm.information62!=null){
//								var lstInfo62 = vm.resultNoForm.information62.split(";");
//								if(lstInfo62.length==1){
//									vm.resultNoForm.information621 = lstInfo62[0];
//								} else
//								if(lstInfo62.length==2){
//									vm.resultNoForm.information622 = lstInfo62[1];
//								} else {
//									var year62 = [];
//									for(var i=0;i<lstInfo62.length;i++){
//										if(i>0){
//											year62.push(lstInfo62[i]);
//										} else {
//											
//										}
//									}
//									vm.resultNoForm.information622 = (year62.size()>0) ? year62.join(";") : null;
//								}
//							}
//							
//							if(vm.resultNoForm.information63!=null){
//								var lstInfo63 = vm.resultNoForm.information63.split(";");
//								if(lstInfo63.length==1){
//									vm.resultNoForm.information631 = lstInfo63[0];
//								} else
//								if(lstInfo63.length==2){
//									vm.resultNoForm.information632 = lstInfo63[1];
//								} else {
//									var year63 = [];
//									for(var i=0;i<lstInfo63.length;i++){
//										if(i>0){
//											year63.push(lstInfo63[i]);
//										}
//									}
//									vm.resultNoForm.information632 = (year63.size()>0) ? year63.join(";") : null;
//								}
//							}
//							
//							if(vm.resultNoForm.information64!=null){
//								var lstInfo64 = vm.resultNoForm.information64.split(";");
//								if(lstInfo64.length==1){
//									vm.resultNoForm.information641 = lstInfo64[0];
//								} else
//								if(lstInfo64.length==2){
//									vm.resultNoForm.information642 = lstInfo64[1];
//								} else {
//									var year64 = [];
//									for(var i=0;i<lstInfo64.length;i++){
//										if(i>0){
//											year64.push(lstInfo64[i]);
//										}
//									}
//									vm.resultNoForm.information642 = (year64.size()>0) ? year64.join(";") : null;
//								}
//							}
//							
//							if(vm.resultNoForm.information72=="1"){
//								vm.resultNoForm.information72 = true;
//							} else {
//								vm.resultNoForm.information72 = false;
//							}
//							
//						}
	        			
	        		}
					
					if(data.plain().resultSolutionDTO==null){
						vm.solutionForm={};
					} else {
						vm.solutionForm = angular.copy(data.plain().resultSolutionDTO);
					}
					
					if(vm.solutionForm!=null){
						if(vm.solutionForm.fileResultSolution){
							for(var i=0; i<vm.solutionForm.fileResultSolution.length; i++){
								vm.fileLstGiaiPhap.push(vm.solutionForm.fileResultSolution[i]);
							}
						}
						
						if(vm.solutionForm.constructTime==null){
							vm.solutionForm.constructTime = "";
						}
						
						if(vm.solutionForm.signDate==null){
							vm.solutionForm.signDate = "";
						}
						
						if(vm.solutionForm.guaranteeTime==null){
							vm.solutionForm.guaranteeTime = "";
						}
						
						if(vm.solutionForm.contractRose==null){
							vm.solutionForm.contractRose = "";
						}
						
//						if(vm.solutionForm.approvedPerformerId!=null){
//							vm.showBtnSaveGiaiPhap = false;
//						} else {
//							vm.showBtnSaveGiaiPhap = true;
//						}
					}
					
					//Lịch sử tiếp xúc
					vm.countHistoryResultTagent = 0;
					vm.countHistoryResultSolution = 0;
					vm.countHistoryResultTagent = data.plain().listResultTangent.length;
					vm.countHistoryResultSolution = data.plain().listResultSolution.length;
					
					vm.resultHisForm1 = angular.copy(data.plain().listResultTangent[2]);
					if(vm.resultHisForm1!= undefined){
						if(vm.resultHisForm1.resultTangentType!=null){
							if(vm.resultHisForm1.resultTangentType=="1"){
								vm.resultHisForm1.resultTangentType = "Khách hàng có nhu cầu";
							} else {
								vm.resultHisForm1.resultTangentType = "Khách hàng không có nhu cầu";
							}
						} else {
							vm.resultHisForm1.resultTangentType = "";
						}
						
						if(vm.resultHisForm1.approvedStatus!=null){
							if(vm.resultHisForm1.approvedStatus=="0"){
								vm.resultHisForm1.approvedStatus = "Chưa phê duyệt";
							} else if(vm.resultHisForm1.approvedStatus=="1"){
								vm.resultHisForm1.approvedStatus = "Phê duyệt";
							} else if(vm.resultHisForm1.approvedStatus=="2"){
								vm.resultHisForm1.approvedStatus = "Từ chối";
							}
						} else {
							vm.resultHisForm1.approvedStatus = "";
						}
					}
					
					vm.resultHisForm2 = angular.copy(data.plain().listResultTangent[1]);
					if(vm.resultHisForm2!= undefined){
						if(vm.resultHisForm2.resultTangentType!=null){
							if(vm.resultHisForm2.resultTangentType=="1"){
								vm.resultHisForm2.resultTangentType = "Khách hàng có nhu cầu";
							} else {
								vm.resultHisForm2.resultTangentType = "Khách hàng không có nhu cầu";
							}
						}
						
						if(vm.resultHisForm2.approvedStatus!=null){
							if(vm.resultHisForm2.approvedStatus=="0"){
								vm.resultHisForm2.approvedStatus = "Chưa phê duyệt";
							} else if(vm.resultHisForm2.approvedStatus=="1"){
								vm.resultHisForm2.approvedStatus = "Phê duyệt";
							} else if(vm.resultHisForm2.approvedStatus=="2"){
								vm.resultHisForm2.approvedStatus = "Từ chối";
							}
						} else {
							vm.resultHisForm2.approvedStatus = "";
						}
					}
					
					vm.resultHisForm3 = angular.copy(data.plain().listResultTangent[0]);
					if(vm.resultHisForm3!= undefined){
						if(vm.resultHisForm3.resultTangentType!=null){
							if(vm.resultHisForm3.resultTangentType=="1"){
								vm.resultHisForm3.resultTangentType = "Khách hàng có nhu cầu";
							} else {
								vm.resultHisForm3.resultTangentType = "Khách hàng không có nhu cầu";
							}
						}
						
						if(vm.resultHisForm3.approvedStatus!=null){
							if(vm.resultHisForm3.approvedStatus=="0"){
								vm.resultHisForm3.approvedStatus = "Chưa phê duyệt";
							} else if(vm.resultHisForm3.approvedStatus=="1"){
								vm.resultHisForm3.approvedStatus = "Phê duyệt";
							} else if(vm.resultHisForm3.approvedStatus=="2"){
								vm.resultHisForm3.approvedStatus = "Từ chối";
							}
						} else {
							vm.resultHisForm3.approvedStatus = "";
						}
					}
					
					//Lịch sử giải pháp
					vm.solutionHisForm1 = angular.copy(data.plain().listResultSolution[2]);
					if(vm.solutionHisForm1!= undefined){
						if(vm.solutionHisForm1.resultSolutionType!=null){
							if(vm.solutionHisForm1.resultSolutionType=="1"){
								vm.solutionHisForm1.resultSolutionType = "Đồng ý giải pháp";
							} else if(vm.solutionHisForm1.resultSolutionType=="2") {
								vm.solutionHisForm1.resultSolutionType = "Từ chối giải pháp";
							} else {
								vm.solutionHisForm1.resultSolutionType = "Bổ sung trình bày giải pháp";
							}
						} else {
							vm.solutionHisForm1.resultSolutionType = "";
						}
						
						if(vm.solutionHisForm1.approvedStatus!=null){
							if(vm.solutionHisForm1.approvedStatus=="0"){
								vm.solutionHisForm1.approvedStatus = "Chưa phê duyệt";
							} else if(vm.solutionHisForm1.approvedStatus=="1"){
								vm.solutionHisForm1.approvedStatus = "Phê duyệt";
							} else if(vm.solutionHisForm1.approvedStatus=="2"){
								vm.solutionHisForm1.approvedStatus = "Từ chối";
							}
						} else {
							vm.solutionHisForm1.approvedStatus = "";
						}
						
						setValueHistoryGiaiPhap(vm.solutionHisForm1);
						
					}
					
					vm.solutionHisForm2 = angular.copy(data.plain().listResultSolution[1]);
					if(vm.solutionHisForm2!= undefined){
						if(vm.solutionHisForm2.resultSolutionType!=null){
							if(vm.solutionHisForm2.resultSolutionType=="1"){
								vm.solutionHisForm2.resultSolutionType = "Đồng ý giải pháp";
							} else if(vm.solutionHisForm2.resultSolutionType=="2") {
								vm.solutionHisForm2.resultSolutionType = "Từ chối giải pháp";
							} else {
								vm.solutionHisForm2.resultSolutionType = "Bổ sung trình bày giải pháp";
							}
						} else {
							vm.solutionHisForm2.resultSolutionType = "";
						}
						
						if(vm.solutionHisForm2.approvedStatus!=null){
							if(vm.solutionHisForm2.approvedStatus=="0"){
								vm.solutionHisForm2.approvedStatus = "Chưa phê duyệt";
							} else if(vm.solutionHisForm2.approvedStatus=="1"){
								vm.solutionHisForm2.approvedStatus = "Phê duyệt";
							} else if(vm.solutionHisForm2.approvedStatus=="2"){
								vm.solutionHisForm2.approvedStatus = "Từ chối";
							}
						} else {
							vm.solutionHisForm2.approvedStatus = "";
						}
						
						setValueHistoryGiaiPhap(vm.solutionHisForm2);
					}
					
					vm.solutionHisForm3 = angular.copy(data.plain().listResultSolution[0]);
					if(vm.solutionHisForm3!= undefined){
						if(vm.solutionHisForm3.resultSolutionType!=null){
							if(vm.solutionHisForm3.resultSolutionType=="1"){
								vm.solutionHisForm3.resultSolutionType = "Đồng ý giải pháp";
							} else if(vm.solutionHisForm3.resultSolutionType=="2") {
								vm.solutionHisForm3.resultSolutionType = "Từ chối giải pháp";
							} else {
								vm.solutionHisForm3.resultSolutionType = "Bổ sung trình bày giải pháp";
							}
						} else {
							vm.solutionHisForm3.resultSolutionType = "";
						}
						
						if(vm.solutionHisForm3.approvedStatus!=null){
							if(vm.solutionHisForm3.approvedStatus=="0"){
								vm.solutionHisForm3.approvedStatus = "Chưa phê duyệt";
							} else if(vm.solutionHisForm3.approvedStatus=="1"){
								vm.solutionHisForm3.approvedStatus = "Phê duyệt";
							} else if(vm.solutionHisForm3.approvedStatus=="2"){
								vm.solutionHisForm3.approvedStatus = "Từ chối";
							}
						} else {
							vm.solutionHisForm3.approvedStatus = "";
						}
						
						setValueHistoryGiaiPhap(vm.solutionHisForm3);
					}

					manageTangentCustomerService.findConversation({tangentCustomerId: dataItem.tangentCustomerId}).then(function(data) {
						let i = 1;
						let dataConversation = data.map(item => {
							item.contentString = item.content.map(contentItem => contentItem.message).join("");
							if (item.callAt > 0) item.callAtString = new Date(item.callAt + 60 * 60 * 7 * 1000 ).toISOString().substring(0, 10).split("-").reverse().join("-") + " " + new Date(item.callAt + 60 * 60 * 7 * 1000 ).toISOString().substring(11, 19);
							item.statusString = vm.statusList.find(statusItem => statusItem.code == item.tangentCustomerStatus).name
							return item;
						});
						i = 1;
						vm.conversationContent = dataConversation.filter(item => item.tangentCustomerStatus == 1 || item.tangentCustomerStatus == 2).map(item => {
							item.index = i;
							i ++;
							return item;
						})
						i = 1;
						vm.conversationContentSolution = dataConversation.filter(item => item.tangentCustomerStatus == 3).map(item => {
							item.index = i;
							i ++;
							return item;
						})
					});
					
					CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '90%', '90%', "deptAdd");
					setTimeout(function(){
						modalInstance1 = CommonService.getModalInstance1();
					},100);
				});
				
        	
        }
        
        function setValueHistoryGiaiPhap(data){
        	if(data.contractCode==null){
				data.contractCode="";
			}
			
			if(data.presentSolutionDate==null){
				data.presentSolutionDate="";
			}
			
			if(data.guaranteeTime==null){
				data.guaranteeTime="";
			}
			
			if(data.resultSolutionType==null){
				data.resultSolutionType="";
			}
			
			if(data.constructTime==null){
				data.constructTime="";
			}
			
			if(data.constructTime==null){
				data.constructTime="";
			}
			
			if(data.signDate==null){
				data.signDate="";
			}
			
			if(data.performerName==null){
				data.performerName="";
			}
			
			if(data.contractRose==null){
				data.contractRose="";
			}
			
			if(data.presentSolutionDate==null){
				data.presentSolutionDate="";
			}
			
			if(data.contentResultSolution==null){
				data.contentResultSolution="";
			}
			
			if(data.approvedDescription==null){
				data.approvedDescription="";
			}
			
			if(data.presentSolutionDateNext==null){
				data.presentSolutionDateNext="";
			}
			
			if(data.contentResultSolution==null){
				data.contentResultSolution="";
			}
			
			if(data.realitySolutionDate==null){
				data.realitySolutionDate="";
			}
        }
        
        //Xoá
        vm.remove = function(dataItem){
        	confirm("Xác nhận xoá bản ghi ?", function(){
        		manageTangentCustomerService.deleteRecord(dataItem).then(function(result){
        			if (result.error) {
                        toastr.error(result.error);
                        return;
                    }
        			toastr.success("Xoá bản ghi thành công !");
        			$(".k-icon.k-i-close").click();
        			doSearch();
        		});
        	});
        }
       
        vm.listRemove = [
        	{
        		title: "Thao tác",
        	}
        	];

        vm.listConvert = [
        	{
        		field: "status",
        		data: {
        			1: 'Hiệu lực',
        			0: 'Hết Hiệu lực'
            }
        	}, 
	        {
	            field: "type",
	            data: {
	                1: 'Quản lý',
	                2: 'Nhân viên',
	            }
	        }
        ];
        
        vm.listConvertTangen = [
        	{
        		field: "status",
        		data: {
        			0: 'Đã hủy',
        			1: 'Chờ tiếp nhận',
        			2: 'Chờ tiếp xúc',
        			3: 'Chờ trình bày giải pháp',
        			4: 'Không thành công',
        			5: 'Chờ ký hợp đồng',
        			6: 'Bổ sung / chỉnh sửa giải pháp',
        			7: 'Không thành công',
        			8: 'Hoàn thành tiếp xúc',
        			9: 'Không thành công',
        			10: 'Hoàn thành thanh toán hoa hồng',
        		}
        	},
        	{
        		field: "source",
        		data: {
        			null: '',
        			1: 'Nội bộ',
        			2: 'Xã hội hóa',
        			3: 'CSKH',
        		}
        	},
        	{
        		field: "overdueStatus",
        		data: {
        			null: '',
        			1: 'Trong hạn',
        			2: 'Sắp quá hạn',
        			3: 'Quá hạn',
        		}
        	}
        	
        ]
//        
//        vm.exportExcelConfig = function(){
//        	vm.searchForm.page = null;
//        	vm.searchForm.pageSize = null;
//        	kendo.ui.progress($("#tangentCustomerGrid"), true);
//        	configStaffTangentService.doSearch(vm.searchForm).then(function(data){
//        		kendo.ui.progress($("#tangentCustomerGrid"), false);
//        		CommonService.exportFile(vm.tangentCustomerGrid, data.data, vm.listRemove, vm.listConvert, "Cấu hình nhân viên tiếp xúc");
//        	}).catch(function(e){
//        		kendo.ui.progress($("#tangentCustomerGrid"), false);
//        		toastr.error("Có lỗi xảy ra khi xuất file !");
//        	});
//        	
//        	 
//        }
        
        vm.exportExcelqlyctx = function(){
        	vm.searchForm.page = null;
        	vm.searchForm.pageSize = null;
        	var obj = {};
        	obj = angular.copy(vm.searchForm);
        	kendo.ui.progress($("#tangentCustomerGrid"), true);
        	return Restangular.all("tangentCustomerRestService/exportExcelqlyctx").post(obj).then(function (d) {
                var dataqlyctx = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + dataqlyctx.fileName;
                kendo.ui.progress($("#tangentCustomerGrid"), false);
            }).catch(function (e) {
            	kendo.ui.progress($("#tangentCustomerGrid"), false);
                toastr.error(gettextCatalog.getString("Lỗi khi tải xuống file!"));
                return;
            });
        }
        
       
        
        vm.exportExcelDetailData = function(obj){     	
        	if(obj.status > 2){
        		obj.resultSolutionDTO = vm.solutionForm;
        		if(obj.resultTangentDTO.resultTangentType == 1)	obj.resultTangentDTO.resultTangentDetailYesDTO = vm.resultYesForm;
            	else if  (obj.resultTangentDTO.resultTangentType == 2) obj.resultTangentDTO.resultTangentDetailNoDTO = vm.resultNoForm;
        	}
     	
        	kendo.ui.progress($("#tangentCustomerGrid"), true);
        	return Restangular.all("tangentCustomerRestService/exportExcelDetailData").post(obj).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                kendo.ui.progress($("#tangentCustomerGrid"), false);
            }).catch(function (e) {
            	kendo.ui.progress($("#tangentCustomerGrid"), false);
                toastr.error(gettextCatalog.getString("Lỗi khi tải xuống file!"));
                return;
            });
        }
        
        //Tỉnh
        vm.provinceCodeOptions = {
		        dataTextField: "name",
		        dataValueField: "id",
				placeholder:"Nhập mã hoặc tên tỉnh",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            if(vm.showDetail==true){
		            	vm.resultYesForm.provinceConstruction = dataItem.code;
		            	vm.areaIdOfProvince = dataItem.areaId;
		            	vm.resultYesForm.provinceId = dataItem.catProvinceId;
		            }else{
		            	 vm.addForm.provinceId = dataItem.catProvinceId;
				         vm.addForm.provinceCode = dataItem.code;
				         vm.areaIdOfProvince = dataItem.areaId;
		            }
		            var obj={
		            	catProvinceId: dataItem.catProvinceId
		            };
		            //Huypq-21032022-edit
		            manageTangentCustomerService.getUserConfigTagentByProvince(obj).then(function(data){
		            	if(data!=null){
		            		vm.addForm.performerId = data.staffId;
		            		vm.addForm.performerName = data.staffName;
		            		vm.addForm.performerSolutionId = data.staffId;
		            		vm.addForm.performerSolutionName = data.staffName;
		            		vm.addForm.performerPhoneNumber = data.staffPhone;
		            		vm.addForm.performerEmail = data.email;
		            	}
		            });
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
		                    return Restangular.all("configStaffTangentRestService/catProvince/doSearchProvinceInPopupByRole").post({
		                        name: vm.showDetail==false ? vm.addForm.provinceCode : vm.resultYesForm.provinceConstruction,
		                        pageSize: vm.provinceCodeOptions.pageSize,
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
		        	if (e.sender.value() === '') {
		        		if(vm.showDetail==true){
			            	vm.resultYesForm.provinceConstruction = null;
			            	vm.areaIdOfProvince = null;
			            	vm.resultYesForm.provinceId = null;
			            }else{
			            	vm.addForm.provinceId = null;
				            vm.addForm.provinceCode = null;
				            vm.addForm.performerId = null;
		            		vm.addForm.performerName = null;
		            		vm.addForm.performerSolutionId = null;
		            		vm.addForm.performerSolutionName = null;
		            		vm.addForm.performerPhoneNumber = null;
		            		vm.addForm.performerEmail = null;
		            		vm.addForm.districtId = null;
				            vm.addForm.districtName = null;
				            vm.addForm.communeId = null;
				            vm.addForm.communeName = null;
				            vm.addForm.areaId = null;
				            vm.addForm.apartmentNumber = null;
				            vm.areaIdOfProvince = null;
			            }
		        		
		            }
		        },
		        close: function (e) {
		        	if (e.sender.value() === '') {
		            	if(vm.showDetail==true){
			            	vm.resultYesForm.provinceConstruction = null;
			            	vm.areaIdOfProvince = null;
			            	vm.resultYesForm.provinceId = null;
			            }else{
			            	vm.addForm.provinceId = null;
				            vm.addForm.provinceCode = null;
				            vm.addForm.performerId = null;
		            		vm.addForm.performerName = null;
		            		vm.addForm.performerSolutionId = null;
		            		vm.addForm.performerSolutionName = null;
		            		vm.addForm.performerPhoneNumber = null;
		            		vm.addForm.performerEmail = null;
		            		vm.addForm.districtId = null;
				            vm.addForm.districtName = null;
				            vm.addForm.communeId = null;
				            vm.addForm.communeName = null;
				            vm.addForm.areaId = null;
				            vm.addForm.apartmentNumber = null;
				            vm.areaIdOfProvince = null;
			            }
		            }
		        }
		    }
        
        //Quận huyện
        vm.districtNameOptions = {
		        dataTextField: "districtName",
		        dataValueField: "districtId",
				placeholder:"Nhập mã hoặc tên quận/huyện",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            if(vm.showDetail==true){
		            	vm.resultYesForm.districtConstruction = dataItem.districtName;
		            	vm.resultYesForm.districtId = dataItem.districtId;
		            }else{
			            vm.addForm.districtId = dataItem.districtId;
			            vm.addForm.districtName = dataItem.districtName;
		            }
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
		                    if(vm.showDetail==false){
			                    if(vm.addForm.provinceId==null){
			                    	toastr.warning("Chưa chọn quận/huyện");
			                    	return;
			                    }
		                    }else if(vm.showDetail==true){
			                    if(vm.resultYesForm.provinceId ==null){
			                    	toastr.warning("Chưa chọn quận/huyện");
			                    	return;
			                    }
		                    }
		                    return Restangular.all("tangentCustomerRestService/doSearchDistrictByProvinceCode").post({
		                    	districtName: vm.showDetail==false ? vm.addForm.districtName : vm.resultYesForm.districtConstruction,
		                        pageSize: vm.districtNameOptions.pageSize,
		                        provinceId: vm.areaIdOfProvince,
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
//				'<p class="col-md-6 text-header-auto border-right-ccc">Mã tỉnh</p>' +
				'<p class="col-md-6 text-header-auto">Tên quận/huyện</p>' +
				'</div>',
		        template: '<div class="row" ><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.districtName #</div> </div>',
		        change: function (e) {
		        	if (e.sender.value() === '') {
		        		if(vm.showDetail==true){
			            	vm.resultYesForm.districtConstruction = null;
			            	vm.resultYesForm.districtId = null;
			            }else{
			        		vm.addForm.districtId = null;
				            vm.addForm.districtName = null;
				            vm.addForm.communeId = null;
				            vm.addForm.communeName = null;
				            vm.addForm.areaId = null;
				            vm.addForm.apartmentNumber = null;
			            }
		            }
		        },
		        close: function (e) {
		        	if (e.sender.value() === '') {
		            	if(vm.showDetail==true){
			            	vm.resultYesForm.districtConstruction = null;
			            	vm.resultYesForm.districtId = null;
			            }else{
			            	vm.addForm.districtId = null;
				            vm.addForm.districtName = null;
				            vm.addForm.communeId = null;
				            vm.addForm.communeName = null;
				            vm.addForm.areaId = null;
				            vm.addForm.apartmentNumber = null;
			            }
		            }
		        }
		    }
        
        //Xã phường
        vm.communeNameOptions = {
		        dataTextField: "communeName",
		        dataValueField: "communeId",
				placeholder:"Nhập mã hoặc tên xã/phường",
		        select: function (e) {
		            vm.isSelect = true;
		            var dataItem = this.dataItem(e.item.index());
		            if(vm.showDetail==true){
		            	vm.resultYesForm.communeConstruction = dataItem.communeName;
		            	vm.resultYesForm.areaId = dataItem.communeId;
		            	vm.resultYesForm.communeId = dataItem.communeId;
		            }else{
		            	 vm.addForm.communeId = dataItem.communeId;
				         vm.addForm.communeName = dataItem.communeName;
				         vm.addForm.areaId = dataItem.communeId;
		            }
		           
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
		                    if(vm.showDetail==false){
			                    if(vm.addForm.districtId==null){
			                    	toastr.warning("Chưa chọn quận/huyện");
			                    	options.success([]);
			                    	return;
			                    } 
		                    }
		                    else if(vm.showDetail==true){
			                    if(vm.resultYesForm.districtId==null){
			                    	toastr.warning("Chưa chọn quận/huyện");
			                    	options.success([]);
			                    	return;
			                    } 
		                    }
		                    return Restangular.all("tangentCustomerRestService/doSearchCommunneByDistrict").post({
			                    	communeName: vm.showDetail==false ? vm.addForm.communeName : vm.resultYesForm.communeConstruction,
			                        pageSize: vm.communeNameOptions.pageSize,
			                        districtId:   vm.showDetail==false ? vm.addForm.districtId : vm.resultYesForm.districtId,
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
//				'<p class="col-md-6 text-header-auto border-right-ccc">Mã tỉnh</p>' +
				'<p class="col-md-6 text-header-auto">Tên xã phường</p>' +
				'</div>',
		        template: '<div class="row" ><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.communeName #</div> </div>',
		        change: function (e) {
		        	if (e.sender.value() === '') {
		        		if(vm.showDetail==true){
				            vm.resultYesForm.communeConstruction = null;
				            vm.resultYesForm.areaId = null;
				            vm.resultYesForm.communeId = null;
				       }else{
			        		vm.addForm.communeId = null;
				            vm.addForm.communeName = null;
				            vm.addForm.areaId = null;
				       }
		            }
		        },
		        close: function (e) {
		        	if (e.sender.value() === '') {
		            	if(vm.showDetail==true){
				            vm.resultYesForm.communeConstruction = null;
				            vm.resultYesForm.areaId = null;
				            vm.resultYesForm.communeId = null;
				       }else{
			            	vm.addForm.communeId = null;
				            vm.addForm.communeName = null;
				            vm.addForm.areaId = null;
				       }
		            }
		        }
		 }
        
        vm.changeBlur = function(param) {
        	if(param=='provinceCode' ) {
        		if (vm.showDetail == false){
        			if(vm.addForm.provinceId == null ){
        				vm.addForm.provinceId = null;
        	            vm.addForm.provinceCode = null;
        	            vm.addForm.performerId = null;
                		vm.addForm.performerName = null;
                		vm.addForm.performerSolutionId = null;
                		vm.addForm.performerSolutionName = null;
                		vm.addForm.performerPhoneNumber = null;
                		vm.addForm.performerEmail = null;
                		vm.addForm.districtId = null;
        	            vm.addForm.districtName = null;
        	            vm.addForm.communeId = null;
        	            vm.addForm.communeName = null;
        	            vm.addForm.areaId = null;
        	            vm.addForm.apartmentNumber = null;
        	            vm.areaIdOfProvince = null;
        	            $("#provinceCode").val(null);
        	            $("#districtName").val(null);
        	            $("#communeName").val(null);
        			}
        			
        		}else{
        			if(vm.resultYesForm.provinceId == null ){
	        			vm.resultYesForm.provinceConstruction = null;
		            	vm.resultYesForm.provinceId = null;
		            	 $("#provinceConstruction").val(null);
		    	         $("#districtConstruction").val(null);
		    	         $("#communeConstruction").val(null);
        			}
        		}
        		
        	}
        	
        	if(param=='districtName') {
        		if (vm.showDetail == false){
        			if (vm.addForm.districtId == null){
        				vm.addForm.districtId = null;
        	            vm.addForm.districtName = null;
        	            vm.addForm.communeId = null;
        	            vm.addForm.communeName = null;
        	            vm.addForm.areaId = null;
        	            vm.addForm.apartmentNumber = null;
        	            $("#districtName").val(null);
        	            $("#communeName").val(null);
        			}
        			
        		}else{
        			if(vm.resultYesForm.districtId == null ){
        				vm.resultYesForm.districtConstruction = null;
    	            	vm.resultYesForm.districtId = null;
    	    	        $("#districtConstruction").val(null);
    	    	        $("#communeConstruction").val(null);
        			}
        			
        		}
        	}
        	
        	if(param=='communeName') {
        		if (vm.showDetail == false){
        			if(vm.addForm.communeId == null ){
        				vm.addForm.communeId = null;
        	            vm.addForm.communeName = null;
        	            vm.addForm.areaId = null;
        	            $("#communeName").val(null);
        			}
        			
        		}else{
        			
        			if(vm.resultYesForm.communeId == null ){
        				vm.resultYesForm.communeConstruction = null;
    		            vm.resultYesForm.areaId = null;
    		            vm.resultYesForm.communeId = null;
    	    	        $("#communeConstruction").val(null);
        			}
        			
        		}
        		
        	}
        }

        
        vm.cancel = function(){
        	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        	vm.isEdit = false;
        	vm.customer();
        }
        
        function validateEmail($email) {
	    	var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	    	return emailReg.test( $email );
	    }
        
        //Lưu lúc tạo mới/sửa
        vm.save = function(){
        	if(vm.addForm.customerName==null || $("#customerName").val()==""){
        		toastr.warning("Họ tên khách hàng không được để trống !");
        		$("#customerName").focus();
        		return;
        	}
        	
        	if(vm.addForm.suggestTime==null){
        		toastr.warning("Thời gian đề nghị tư vấn không được để trống !");
        		$("#suggestTime").focus();
        		return;
        	} 
        	
        	if(vm.addForm.birthYear==null){
        		toastr.warning("Năm sinh không được để trống !");
        		$("#birthYear").focus();
        		return;
        	}
        	
        	if(vm.addForm.provinceCode==null){
        		toastr.warning("Tỉnh thành phố không được để trống !");
        		$("#provinceCode").focus();
        		return;
        	}
        	
        	if(vm.addForm.districtName==null){
        		toastr.warning("Quận huyện không được để trống !");
        		$("#districtName").focus();
        		return;
        	}
        	
        	if(vm.addForm.communeName==null){
        		toastr.warning("Phường xã không được để trống !");
        		$("#communeName").focus();
        		return;
        	}
        	
//        	if(vm.addForm.apartmentNumber==null){
//        		toastr.warning("Số nhà, tên đường/phố không được để trống !");
//        		$("#apartmentNumber").focus();
//        		return;
//        	}
        	
        	if(vm.addForm.customerPhone==null){
        		toastr.warning("Số điện thoại không được để trống !");
        		$("#customerPhone").focus();
        		return;
        	} 
//        	else {
//        		var vnf_regex = /((09|03|07|08|05)+([0-9]{8})\b)/g;
//	        	if(kendo.parseFloat(vm.addForm.customerPhone)<0){
//	        		toastr.warning("Số điện thoại sai định dạng");
//	        		$("#customerPhone").focus();
//	            	return;
//	        	} else {
//	        		if(!vnf_regex.test(vm.addForm.customerPhone)) {
//	        			toastr.warning("Số điện thoại sai định dạng");
//	                	 $("#customerPhone").focus();
//	                	 return;
//	                }
//	        	}
//        	}
        	
        	if(vm.addForm.customerEmail!=null){
        		if(!validateEmail(vm.addForm.customerEmail)){
	    			toastr.warning("Email không đúng định dạng");
		    		$("#customerEmail").focus();
		    		return;
	    		}
        	}
        	
        	if(vm.addForm.partnerType==null){
	    		toastr.warning("Loại khách hàng không được để trống");
		    	return;
        	}
    		if(vm.addForm.isCustomerServiceSource ==1){
    			if(vm.addForm.receptionChannel==null){
    	    		toastr.warning("Kênh tiếp nhận không được để trống");
    		    	return;
            	}else{
            		if(vm.addForm.receptionChannel=='Khác' || vm.addForm.receptionChannel=='Hotline'){
            			if(vm.addForm.customerResources==null){
            	    		toastr.warning("Nguồn Khách hàng biết đến không được để trống");
            		    	return;
                    	}
            		}
            	}
        	}
    		
        	vm.addForm.birthYear = $("#birthYear").val();
        	vm.addForm.contentCustomer = $("#contentCustomer").val();
        	if(vm.isCreateNew){
        		var suggestTime = kendo.parseDate(vm.addForm.suggestTime, "dd/MM/yyyy");
        		suggestTime.setHours(0);
        		suggestTime.setMinutes(0);
        		suggestTime.setSeconds(0);
        		suggestTime.setMilliseconds(0);
        		var currentDate = kendo.parseDate(new Date(), "dd/MM/yyyy");
        		currentDate.setHours(0);
        		currentDate.setMinutes(0);
        		currentDate.setSeconds(0);
        		currentDate.setMilliseconds(0);
        		if(suggestTime < currentDate){
            		toastr.warning("Thời gian đề nghị tư vấn phải lớn hơn ngày hiện tại");
    	    		$("#suggestTime").focus();
    	    		return;
            	}

        		confirm("Xác nhận tạo mới yêu cầu tiếp xúc ?", function(){
        			manageTangentCustomerService.save(vm.addForm).then(function(result){
        				if(result.error){
        					toastr.error(result.error);
                			return;
        				}
            			toastr.success("Thêm mới thành công");
            			$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
            			doSearch();
            		}).catch(function(e){
            			toastr.error("Có lỗi xảy ra khi tạo mới !");
            			return;
            		});
        		});
        	} else {
//        		if(vm.addForm.createdUser == vm.userLoginId){
//        			var suggestTime = kendo.parseDate(vm.addForm.suggestTime, "dd/MM/yyyy");
//            		suggestTime.setHours(0);
//            		suggestTime.setMinutes(0);
//            		suggestTime.setSeconds(0);
//            		var currentDate = kendo.parseDate(new Date(), "dd/MM/yyyy");
//            		currentDate.setHours(0);
//            		currentDate.setMinutes(0);
//            		currentDate.setSeconds(0);
//            		if(suggestTime < currentDate){
//                		toastr.warning("Thời gian đề nghị tư vấn phải lớn hơn ngày hiện tại");
//        	    		$("#suggestTime").focus();
//        	    		return;
//                	}
//        		}
        		confirm("Xác nhận cập nhật yêu cầu tiếp xúc ?", function(){
        			manageTangentCustomerService.update(vm.addForm).then(function(result){
        				if(result.error){
        					toastr.error(result.error);
                			return;
        				}
    					toastr.success("Cập nhật thành công");
    					$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
            			doSearch();
            		}).catch(function(e){
            			toastr.error("Có lỗi xảy ra khi cập nhật !");
            			return;
            		});
        		});
        	}
        	
        }
        
        //Kết quả tiếp xúc
        vm.confirmReject = function(dataItem){
        	vm.provinceId = dataItem.provinceId;
        	vm.tangentCustomerId = dataItem.tangentCustomerId;
        	vm.customerName = dataItem.customerName;
        	var teamplateUrl = "coms/manageTangentCustomer/confirmReasonRejectPopup.html";
			var title = "Xác nhận lý do khách hàng từ chối";
			var windowId = "CONFIRM_REASON_REJECT";
			manageTangentCustomerService.getListResultTangentJoinSysUserByTangentCustomerId({tangentCustomerId : vm.tangentCustomerId}).then(function(data){
				vm.resultForm = angular.copy(data.plain()[0]);
				CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '60%', '60%', "deptAdd");
	    		setTimeout(function(){
					modalInstance1 = CommonService.getModalInstance1();
				},100);
			});
        }
        
        //Kết quả giải pháp
        vm.confirmRejectGiaiPhap = function(dataItem){
        	vm.provinceId = dataItem.provinceId;
        	vm.tangentCustomerId = dataItem.tangentCustomerId;
        	vm.customerName = dataItem.customerName;
        	var teamplateUrl = "coms/manageTangentCustomer/confirmReasonRejectGiaiPhapPopup.html";
			var title = "Xác nhận lý do khách hàng từ chối";
			var windowId = "CONFIRM_REASON_REJECT_GP";
			
			manageTangentCustomerService.getResultSolutionJoinSysUserByTangentCustomerId(vm.tangentCustomerId).then(function(data){
				vm.solutionForm = angular.copy(data.plain()[0]);
				CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '60%', '60%', "deptAdd");
	    		setTimeout(function(){
					modalInstance1 = CommonService.getModalInstance1();
				},100);
			});
        }
        
        //File đính kèm phiếu thu thập thông tin khách hàng
        vm.submitAttachFile = submitAttachFile;
        function submitAttachFile() {
            sendFile("customerFile", callback);
        }
        
        //File sổ đỏ
        vm.submitAttachFileSoDo = submitAttachFileSoDo;
        function submitAttachFileSoDo() {
            sendFile("soDoFile", callback);
        }
        
        //File địa hinh
        vm.submitAttachFileDiaHinh = submitAttachFileDiaHinh;
        function submitAttachFileDiaHinh() {
            sendFile("diaHinhFile", callback);
        }
        
        //File giải pháp
        vm.submitAttachFileGiaiPhap = submitAttachFileGiaiPhap;
        function submitAttachFileGiaiPhap() {
            sendFile("giaiPhapFile", callback);
        }
        
        function sendFile(id, callback) {
//        	if(id=="customerFile"){
//        		vm.fileLst = [];
//            } else if(id=="soDoFile"){
//            	vm.fileLstSoDo = [];
//            } else if(id=="diaHinhFile"){
//            	vm.fileLstDiaHinh = [];
//            } else if(id=="giaiPhapFile"){
//            	vm.fileLstGiaiPhap = [];
//            }
        	
        	var file = null;
        	try {
        		file = $("#" + id)[0].files[0];
        	} catch (err) {
                toastr.warning("Bạn chưa chọn file");
                setTimeout(function() {
	               	  $(".k-upload-files.k-reset").find("li").remove();
	    			     $(".k-upload-files").remove();
	    				 $(".k-upload-status").remove();
	    				 $(".k-upload.k-header").addClass("k-upload-empty");
	    				 $(".k-upload-button").removeClass("k-state-focused");
                },10);
        		return;
        	}

            if (!htmlCommonService.checkFileExtension(id)) {
                toastr.warning("File không đúng định dạng cho phép");
                setTimeout(function() {
	               	  $(".k-upload-files.k-reset").find("li").remove();
	    			     $(".k-upload-files").remove();
	    				 $(".k-upload-status").remove();
	    				 $(".k-upload.k-header").addClass("k-upload-empty");
	    				 $(".k-upload-button").removeClass("k-state-focused");
                },10);
                return;
            }
            
            var formData = new FormData();
            formData.append('multipartFile', file);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "fileservice/uploadATTTInput",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    callback(data, file, id);
                    toastr.success("Tải file lên thành công");
                }
            });
        }
        
        function callback(data, fileReturn, id) {
            for (var i = 0; i < data.length; i++) {
                 var file = {};
                file.name = fileReturn.name;
                file.createdDate = htmlCommonService.getFullDate();
                file.createdUserName = Constant.userInfo.VpsUserInfo.fullName;
                file.createdUserId = Constant.userInfo.VpsUserInfo.sysUserId;
                file.filePath = data[i];
                file.type = 0;
            }
            callbackPushList(file,id,callbackPushList);
        }
        
        function callbackPushList(file, id,callbackPushList){
        	if(id=="customerFile"){
            	vm.fileLst.push(file);
            	$("#btnShowFile").click();
//            	$("#customerFile").val(null);
            } else if(id=="soDoFile"){
            	vm.fileLstSoDo.push(file);
            	$("#btnShowFile").click();
//            	$("#soDoFile").val(null);
            } else if(id=="diaHinhFile"){
            	vm.fileLstDiaHinh.push(file);
            	$("#btnShowFile").click();
//            	$("#diaHinhFile").val(null);
            } else if(id=="giaiPhapFile"){
            	vm.fileLstGiaiPhap = [];
            	vm.fileLstGiaiPhap.push(file);
            	$("#btnShowFile").click();
            }
        }
        
        vm.removeFile = function(data, list){
        	for(var i=0; i<list.length; i++){
        		if(data.name = list[i].name){
        			list.splice(i,1);
        		}
        	}
        }
        
        //Lưu popup xem chi tiết Tiếp xúc
        vm.savePopupTiepXuc = function(){
        	if(vm.resultForm.appointmentDate==null || $("#appointmentDate").val()==""){
    			toastr.warning("Ngày hẹn tiếp xúc khách hàng không được để trống !");
    			$("#appointmentDate").focus();
    			return;
    		}
			vm.viewForm.tangentCustomerId = vm.tangentCustomerId;
        	if(vm.resultForm.resultTangentType==0){
    			var teamplateUrl = "coms/manageTangentCustomer/reasonRejectPopup.html";
    			var title = "Lý do khách hàng từ chối";
    			var windowId = "REASON_REJECT";
    			CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '60%', '45%', "deptAdd");
        		setTimeout(function(){
    				modalInstance2 = CommonService.getModalInstance1();
    			},100);
        	} else{
//        		if(vm.checkPerform){
//            		if(vm.resultForm.appointmentDate==null || $("#appointmentDate").val()==""){
//            			toastr.warning("Ngày hẹn tiếp xúc khách hàng không được để trống !");
//            			$("#appointmentDate").focus();
//            			return;
//            		}
            		
//            		if(vm.resultForm.resultTangentType==null || $("#resultTangentType").val()==""){
//            			toastr.warning("Chưa chọn Kết quả tư vấn khách hàng !");
//            			return;
//            		}

            		if(vm.viewForm.status >= 2){
            			if(vm.resultForm.resultTangentType!=null && vm.resultForm.resultTangentType == "1"){
//                			if(vm.fileLst!=null && vm.fileLst.length==0){
//                				toastr.warning("Chưa tải lên File đính kèm (phiếu thu thập thông tin khách hàng) !");
//                    			return;
//                			}
            				
//                		if(vm.resultForm!=null){
//                			vm.resultForm.package8 = null;
//                			if(vm.resultForm.package1==true){
//                				vm.resultForm.package1 = "1";
//                			} else {
//                				vm.resultForm.package1 = null;
//                			}
//                			
//                			if(vm.resultForm.package2==true){
//                				vm.resultForm.package2 = "1";
//                			} else {
//                				vm.resultForm.package2 = null;
//                			}
//                			
//                			if(vm.resultForm.package3==true){
//                				vm.resultForm.package3 = "1";
//                			} else {
//                				vm.resultForm.package3 = null;
//                			}
//                			
//                			if(vm.resultForm.package4==true){
//                				vm.resultForm.package4 = "1";
//                			} else {
//                				vm.resultForm.package4 = null;
//                			}
//                			
//                			if(vm.resultForm.package5==true){
//                				vm.resultForm.package5 = "1";
//                			} else {
//                				vm.resultForm.package5 = null;
//                			}
//                			
//                			if(vm.resultForm.package6==true){
//                				vm.resultForm.package6 = "1";
//                			} else {
//                				vm.resultForm.package6 = null;
//                			}
//                			
                			/*if(vm.resultForm.package7==true){
                				vm.resultForm.package7 = "1";
                			} else {
                				vm.resultForm.package7 = null;
                			}
                			
                			if(vm.resultForm.package81==true){
                				vm.resultForm.package8 = "1";
                			} 
                			
                			if(vm.resultForm.package82==true){
                				if(vm.resultForm.package8==null){
                					vm.resultForm.package8 = "2"
                				} else {
                					vm.resultForm.package8 = vm.resultForm.package8 + ";" + "2";
                				}
                			}
                			
                			if(vm.resultForm.package83==true){
                				if(vm.resultForm.package8==null){
                					vm.resultForm.package8 = "3"
                				} else {
                					vm.resultForm.package8 = vm.resultForm.package8 + ";" + "3";
                				}
                			}
                			
                			if(vm.resultForm.package84==true){
                				if(vm.resultForm.package8==null){
                					vm.resultForm.package8 = "4"
                				} else {
                					vm.resultForm.package8 = vm.resultForm.package8 + ";" + "4";
                				}
                			}
                			
                			if(vm.resultForm.package9==true){
                				vm.resultForm.package9 = "1";
                			} else {
                				vm.resultForm.package9 = null;
                			}*/
                    		
                    		if(vm.resultYesForm!=null){
                    			vm.resultYesForm.detailCustomer.partnerType =  vm.viewForm.partnerType;
                    			if(vm.resultYesForm.presentSolutionDate != null){
                    				var presentSolutionDate = kendo.parseDate(vm.resultYesForm.presentSolutionDate, "dd/MM/yyyy");
                        			var current = kendo.parseDate(new Date(), "dd/MM/yyyy");
                            		if(presentSolutionDate < current){
                                		toastr.warning("Ngày hẹn trình bày giải pháp không hợp lệ.Vui lòng kiểm tra lại");
                        	    		$("#presentSolutionDate").focus();
                        	    		return;
                                	}
                    			}
                    			if(vm.resultYesForm.presentSolutionDate != null){
                        			var presentDate = kendo.parseDate(vm.resultYesForm.presentSolutionDate, 'dd/MM/yyyy');vm.resultYesForm.presentSolutionDate = kendo.toString(presentDate, 'dd/MM/yyyy');
                        		}
//                    			if(vm.resultYesForm.jobCustomer==null){
//                    				toastr.warning("Chưa nhập thông tin nghề nghiệp khách hàng !");
//                        			return;
//                    			}
//                				if(vm.resultYesForm.dateOfBirth==null){
//                    				toastr.warning("Chưa nhập thông tin ngày sinh khách hàng !");
//                        			return;
//                    			}
//                				var dateOfBirth = kendo.parseDate(vm.resultYesForm.dateOfBirth, "dd/MM/yyyy");
//                        		var currentDate = kendo.parseDate(new Date(), "dd/MM/yyyy");
//                        		if(dateOfBirth > currentDate){
//                            		toastr.warning("Ngày sinh khách hàng không hợp lệ.Vui lòng kiểm tra lại");
//                    	    		$("#dateOfBirth").focus();
//                    	    		return;
//                            	}
//                        		
//                        		var DOB = kendo.parseDate(vm.resultYesForm.dateOfBirth, 'dd/MM/yyyy');vm.resultYesForm.dateOfBirth = kendo.toString(DOB, 'dd/MM/yyyy');
//                        		
//                        		var regex = /^0*?[1-9]\d*$/;
//                        		if($("#information511").val().trim()!="" && !regex.test(vm.resultYesForm.information511)){
//                					toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số tầng).Vui lòng nhập đúng định dạng."));
//                					$("#information511").focus();
//                					return;
//                				}
//                        		
//                        		if($("#information71").val().trim()!="" && !regex.test(vm.resultYesForm.information71)){
//                					toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số tầng hầm).Vui lòng nhập đúng định dạng."));
//                					$("#information71").focus();
//                					return;
//                				}
//                        		if($("#information72").val().trim()!="" && !regex.test(vm.resultYesForm.information72)){
//                					toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Gara).Vui lòng nhập đúng định dạng."));
//                					$("#information72").focus();
//                					return;
//                				}
//                        		if($("#information73").val().trim()!="" && !regex.test(vm.resultYesForm.information73)){
//                					toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số phòng khách).Vui lòng nhập đúng định dạng."));
//                					$("#information73").focus();
//                					return;
//                				}
//                        		if($("#information74").val().trim()!="" && !regex.test(vm.resultYesForm.information74)){
//                					toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số phòng bếp).Vui lòng nhập đúng định dạng."));
//                					$("#information74").focus();
//                					return;
//                				}
//                        		if($("#information75").val().trim()!="" && !regex.test(vm.resultYesForm.information75)){
//                					toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số phòng ngủ).Vui lòng nhập đúng định dạng."));
//                					$("#information75").focus();
//                					return;
//                				}
//                        		if($("#information76").val().trim()!="" && !regex.test(vm.resultYesForm.information76)){
//                					toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số phòng thờ).Vui lòng nhập đúng định dạng."));
//                					$("#information77").focus();
//                					return;
//                				}
//                        		if($("#information77").val().trim()!="" && !regex.test(vm.resultYesForm.information77)){
//                					toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số phòng ăn).Vui lòng nhập đúng định dạng."));
//                					$("#information77").focus();
//                					return;
//                				}
//                        		if($("#information78").val().trim()!="" && !regex.test(vm.resultYesForm.information78)){
//                					toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số phòng vệ sinh).Vui lòng nhập đúng định dạng."));
//                					$("#information78").focus();
//                					return;
//                				}
//                        		if($("#information79").val().trim()!="" && !regex.test(vm.resultYesForm.information79)){
//                					toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số sân vườn).Vui lòng nhập đúng định dạng."));
//                					$("#information79").focus();
//                					return;
//                				}
                    			if (!validateSaveTangent(vm.resultYesForm)) {
                                    return;
                                }
                    			vm.resultYesForm.information1_1 = vm.resultYesForm.information511;
                    			vm.resultYesForm.information1_21 = vm.resultYesForm.information521;
                    			vm.resultYesForm.information1_22 = vm.resultYesForm.information522;
                    			vm.resultYesForm.information1_23 = vm.resultYesForm.information523;
                    			vm.resultYesForm.information1_3 = vm.resultYesForm.information531;
                    			vm.resultYesForm.information3 = vm.resultYesForm.information4;
                    			vm.resultYesForm.information3_1 = vm.resultYesForm.information71;
                    			vm.resultYesForm.information3_2 = vm.resultYesForm.information72;
                    			vm.resultYesForm.information3_3 = vm.resultYesForm.information73;
                    			vm.resultYesForm.information3_4 = vm.resultYesForm.information74;
                    			vm.resultYesForm.information3_5 = vm.resultYesForm.information75;
                    			vm.resultYesForm.information3_6 = vm.resultYesForm.information76;
                    			vm.resultYesForm.information3_7 = vm.resultYesForm.information77;
                    			vm.resultYesForm.information3_8 = vm.resultYesForm.information78;
                    			vm.resultYesForm.information3_9 = vm.resultYesForm.information79;
                    			
//                    			vm.resultYesForm.information43 = null;
//                    			vm.resultYesForm.information42 = null;
//                    			if(vm.resultYesForm.information51!=null){
//                    				vm.resultYesForm.information51 = vm.resultYesForm.information51!=null ? vm.resultYesForm.information51 : 0;
//                    			}
//                    			if(vm.resultYesForm.information421!=null){
//                    				vm.resultYesForm.information42 = vm.resultYesForm.information421!=null ? vm.resultYesForm.information421 : 0;
//                    			}
                    			
//                    			if(vm.resultYesForm.information421!=null){
//                    				vm.resultYesForm.information42 = vm.resultYesForm.information421!=null ? vm.resultYesForm.information421 : 0;
//                    			}
//                    			
//                    			if(vm.resultYesForm.information422!=null){
//                    				vm.resultYesForm.information42 = vm.resultYesForm.information42 + ";" + (vm.resultYesForm.information422 !=null ? vm.resultYesForm.information422 : 0);
//                    			}
//                    			
//                    			if(vm.resultYesForm.information423!=null){
//                    				vm.resultYesForm.information42 = vm.resultYesForm.information42 + ";" + (vm.resultYesForm.information423 !=null ? vm.resultYesForm.information423 : 0);
//                    			}
                    			
//                    			if(vm.resultYesForm.information431==true){
//                    				vm.resultYesForm.package43 = "1";
//                    			}
//                    			
//                    			if(vm.resultYesForm.package432==true){
//                    				vm.resultYesForm.package43 = vm.resultYesForm.package43 + ";" + "2";
//                    			}
//                    			
//                    			if(vm.resultYesForm.package433==true){
//                    				vm.resultYesForm.package43 = vm.resultYesForm.package43 + ";" + "3";
//                    			}
                    			
//                    			if(vm.resultYesForm.information53==true){
//                    				vm.resultYesForm.information53 = "1";
//                    			} else {
//                    				vm.resultYesForm.information53 = "0";
//                    			}
//                    			
//                    			if(vm.resultYesForm.information54==true){
//                    				vm.resultYesForm.information54 = "1";
//                    			} else {
//                    				vm.resultYesForm.information54 = "0";
//                    			}
//                    			
//                    			if(vm.resultYesForm.information55==true){
//                    				vm.resultYesForm.information55 = "1";
//                    			} else {
//                    				vm.resultYesForm.information55 = "0";
//                    			}
//                    		
//                    			if(vm.resultYesForm.information431){
//                    				vm.resultYesForm.information43 = "1";
//                    			}
//                    			
//                    			if(vm.resultYesForm.information432){
//                    				vm.resultYesForm.information43 = "2";
//                    			}
//                    			
//                    			if(vm.resultYesForm.information433){
//                    				vm.resultYesForm.information43 = "3";
//                    			}
                    			if(vm.viewForm.partnerType ==1){
                    				vm.viewForm.partnerCode = "KHCN-" + ""+ vm.viewForm.provinceCode + "-" + vm.viewForm.customerPhone;
                    				var dateStr = kendo.toString($('#birthYearB2C').val(), "dd/MM/yyyy");
                    				var dateParts = dateStr.split("/");
                    				vm.viewForm.birthYear = dateParts[2];
                    			}else vm.viewForm.partnerCode = "KHDN- " + ""+ vm.viewForm.provinceCode + "-" + vm.viewForm.customerPhone;
                    			
                        		vm.viewForm.resultTangentDTO = vm.resultForm;
                        		vm.viewForm.resultTangentDetailYesDTO = vm.resultYesForm;
                    			
                    		}
                		}
//                		if(vm.resultNoForm!=null){
//                			vm.resultNoForm.information92 = null
                			//Hình chữ nhật
//                			if(vm.resultNoForm.information21=="1"){
//                				vm.resultNoForm.information21 = (vm.resultNoForm.information211 !=null ? vm.resultNoForm.information211 : 0) + ";" 
//    	            			+ (vm.resultNoForm.information212 !=null ? vm.resultNoForm.information212 : 0) + ";" 
//    	            			+ (vm.resultNoForm.information213 !=null ? vm.resultNoForm.information213 : 0);
//                			}
                			//Hình vuông
//                			if(vm.resultNoForm.information22=="1"){
//                				vm.resultNoForm.information22 = (vm.resultNoForm.information221 !=null ? vm.resultNoForm.information221 : 0) + ";" 
//                				+ (vm.resultNoForm.information222 !=null ? vm.resultNoForm.information222 : 0);
//                			}
                			//Hình tứ giác
//                			if(vm.resultNoForm.information23=="1"){
//                				vm.resultNoForm.information23 = (vm.resultNoForm.information231 !=null ? vm.resultNoForm.information231 : 0) + ";" 
//    	            			+ (vm.resultNoForm.information232 !=null ? vm.resultNoForm.information232 : 0) + ";" 
//    	            			+ (vm.resultNoForm.information233 !=null ? vm.resultNoForm.information233 : 0) + ";" 
//    	            			+ (vm.resultNoForm.information234 !=null ? vm.resultNoForm.information234 : 0) + ";" 
//    	            			+ (vm.resultNoForm.information235 !=null ? vm.resultNoForm.information235 : 0);
//                			}
                			//Xung quanh nhà
//                			vm.resultNoForm.information52 = (vm.resultNoForm.information521 !=null ? vm.resultNoForm.information521 : 0) + ";" 
//    	            			+ (vm.resultNoForm.information522 !=null ? vm.resultNoForm.information522 : 0) + ";" 
//    	            			+ (vm.resultNoForm.information523 !=null ? vm.resultNoForm.information523 : 0);
                			//Đua ban công
//                			if(vm.resultNoForm.information921 !=null){
//                				vm.resultNoForm.information92 = "1";
//                			}
//                			
//                			if(vm.resultNoForm.information922!=null){
//                				if(vm.resultNoForm.information92!=null){
//                					vm.resultNoForm.information92 = vm.resultNoForm.information92 + ";" + "3";
//                				} else {
//                					vm.resultNoForm.information92 = "3";
//                				}
//                			}
//                			
//                			if(vm.resultNoForm.information923!=null){
//                				if(vm.resultNoForm.information92!=null){
//                					vm.resultNoForm.information92 = vm.resultNoForm.information92 + ";" + "2";
//                				} else {
//                					vm.resultNoForm.information92 = "2";
//                				}
//                			}
//                			
//                			if(vm.resultNoForm.information924!=null){
//                				if(vm.resultNoForm.information92!=null){
//                					vm.resultNoForm.information92 = vm.resultNoForm.information94 + ";" + "4";
//                				} else {
//                					vm.resultNoForm.information92 = "4";
//                				}
//                			}
//                			
//                			//Sân vườn
//                			if(vm.resultNoForm.information111 !=null){
//                				vm.resultNoForm.information11 = "1";
//                			}
//                			
//                			if(vm.resultNoForm.information112!=null){
//                				if(vm.resultNoForm.information11!=null){
//                					vm.resultNoForm.information11 = vm.resultNoForm.information11 + ";" + "2";
//                				} else {
//                					vm.resultNoForm.information11 = "2";
//                				}
//                			}
//                			
//                			if(vm.resultNoForm.information113!=null){
//                				if(vm.resultNoForm.information11!=null){
//                					vm.resultNoForm.information11 = vm.resultNoForm.information11 + ";" + "3";
//                				} else {
//                					vm.resultNoForm.information11 = "3";
//                				}
//                			}
//                			
//                			//Giao thông
//                			if(vm.resultNoForm.information133){
//                				vm.resultNoForm.information133 = "1";
//                			} else {
//                				vm.resultNoForm.information133 = "0";
//                			}
//                			
//                			if(vm.resultNoForm.information134){
//                				vm.resultNoForm.information134 = "1";
//                			} else {
//                				vm.resultNoForm.information134 = "0";
//                			}
//                			
//                			if(vm.resultNoForm.information135){
//                				vm.resultNoForm.information135 = "1";
//                			} else {
//                				vm.resultNoForm.information135 = "0";
//                			}
                			
                			//Yêu cầu ngoài
//                			if(vm.resultNoForm.information151){
//                				vm.resultNoForm.information151 = "1";
//                			} else {
//                				vm.resultNoForm.information151 = "0";
//                			}
//                			
//                			if(vm.resultNoForm.information152){
//                				vm.resultNoForm.information152 = "1";
//                			} else {
//                				vm.resultNoForm.information152 = "0";
//                			}
//                			
//                			if(vm.resultNoForm.information1531 !=null){
//                				vm.resultNoForm.information153 = "1";
//                			}
//                			
//                			if(vm.resultNoForm.information1532!=null){
//                				if(vm.resultNoForm.information153!=null){
//                					vm.resultNoForm.information153 = vm.resultNoForm.information153 + ";" + "2";
//                				} else {
//                					vm.resultNoForm.information153 = "2";
//                				}
//                			}
//                			
//                			if(vm.resultNoForm.information1533!=null){
//                				if(vm.resultNoForm.information153!=null){
//                					vm.resultNoForm.information153 = vm.resultNoForm.information153 + ";" + "3";
//                				} else {
//                					vm.resultNoForm.information153 = "3";
//                				}
//                			}
//                			
//                			if(vm.resultNoForm.information1534!=null){
//                				if(vm.resultNoForm.information153!=null){
//                					vm.resultNoForm.information153 = vm.resultNoForm.information153 + ";" + "4";
//                				} else {
//                					vm.resultNoForm.information153 = "4";
//                				}
//                			}
//                			
//                			if(vm.resultNoForm.information154){
//                				vm.resultNoForm.information154 = "1";
//                			} else {
//                				vm.resultNoForm.information154 = "0";
//                			}
//                			
//                			vm.resultNoForm.information62= null;
//    						if(vm.resultNoForm.information621!=null){
//    							vm.resultNoForm.information62 = vm.resultNoForm.information621;
//    						}
//    						
//    						if(vm.resultNoForm.information622!=null){
//    							if(vm.resultNoForm.information62!=null){
//    								vm.resultNoForm.information62 = vm.resultNoForm.information62 + ";" + vm.resultNoForm.information622;
//    							} else {
//    								vm.resultNoForm.information62 = vm.resultNoForm.information622;
//    							}
//    						}
//    						
//    						vm.resultNoForm.information63= null;
//    						if(vm.resultNoForm.information631!=null){
//    							vm.resultNoForm.information63 = vm.resultNoForm.information631;
//    						}
//    						
//    						if(vm.resultNoForm.information632!=null){
//    							if(vm.resultNoForm.information63!=null){
//    								vm.resultNoForm.information63 = vm.resultNoForm.information63 + ";" + vm.resultNoForm.information632;
//    							} else {
//    								vm.resultNoForm.information63 = vm.resultNoForm.information632;
//    							}
//    						}
//    						
//    						vm.resultNoForm.information64= null;
//    						if(vm.resultNoForm.information641!=null){
//    							vm.resultNoForm.information64 = vm.resultNoForm.information641;
//    						}
//    						
//    						if(vm.resultNoForm.information642!=null){
//    							if(vm.resultNoForm.information64!=null){
//    								vm.resultNoForm.information64 = vm.resultNoForm.information64 + ";" + vm.resultNoForm.information642;
//    							} else {
//    								vm.resultNoForm.information64 = vm.resultNoForm.information642;
//    							}
//    						}
//    						
//    						if(vm.resultNoForm.information72==true){
//                				vm.resultNoForm.information72 = "1";
//                			} else {
//                				vm.resultNoForm.information72 = "0";
//                			}
//                			
//                		}
//                		
//                		if(vm.fileLst.length>0){
//                			vm.resultForm.fileReceipts = vm.fileLst;
//                		} else {
//                			vm.resultForm.fileReceipts = null;
//                		}
//                		
//                		if(vm.fileLstSoDo.length>0){
//                			vm.resultForm.fileRedBook = vm.fileLstSoDo;
//                		} else {
//                			vm.resultForm.fileRedBook = null;
//                		}
//                		
//                		if(vm.fileLstDiaHinh.length>0){
//                			vm.resultForm.fileTopographic = vm.fileLstDiaHinh;
//                		} else {
//                			vm.resultForm.fileTopographic = null;
//                		}
                		
//                		vm.viewForm.resultTangentDTO = vm.resultForm;
//                		if(vm.resultForm.contructionDesign=="1"){
//                			vm.viewForm.resultTangentDetailYesDTO = vm.resultYesForm;
//                		} else if(vm.resultForm.contructionDesign=="0"){
//                			vm.viewForm.resultTangentDetailNoDTO = vm.resultNoForm;
//                		}
//            		}
            		
            	}
            	vm.viewForm.resultTangentDTO = vm.resultForm;
            	if(vm.viewForm.partnerType)
            		vm.solutionForm = {};
            	if(vm.viewForm.status==2){
            		vm.solutionForm.presentSolutionDate = vm.resultYesForm.presentSolutionDate;
            		vm.viewForm.resultSolutionDTO = vm.solutionForm;
            	}
            	
            	manageTangentCustomerService.saveDetail(vm.viewForm).then(function(result){
                		if(result.error){
                			toastr.error(result.error);
                			return;
                		}
                		
                		toastr.success("Cập nhật chi tiết thành công !");
                		modalInstance1.dismiss();
                		$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
                		doSearch();
                });
        	}
        }
        
        function validateSaveTangent(obj) {
            var result = true;
            if(obj.provinceConstruction == null){
            	result = false;
				toastr.warning("Chưa nhập tỉnh thi công !");
    			return;
            }
            if(obj.districtConstruction == null){
            	result = false;
				toastr.warning("Chưa nhập quận/huyện thi công !");
    			return;
            }
            if(obj.communeConstruction == null){
            	result = false;
				toastr.warning("Chưa nhập xã thi công !");
    			return;
            }
            if(obj.detailAddressConstruction == null){
            	result = false;
				toastr.warning("Chưa nhập địa chỉ chi tiết thi công !");
    			return;
            }
            if(obj.detailCustomer.partnerType==undefined || obj.detailCustomer.partnerType==null){
            	result = false;
				toastr.warning("Chưa chọn loại khách hàng !");
    			return;
			}else{
				if(obj.detailCustomer.partnerType==1){
					if(obj.detailCustomer.sexB2C==null){
						result = false;
						toastr.warning("Chưa chọn giới tính khách hàng !");
		    			return;
					}
					if(obj.detailCustomer.birthYearB2C==null){
						result = false;
						toastr.warning("Chưa nhập thông tin ngày sinh khách hàng !");
						$("#birthYearB2C").focus();
		    			return;
					}else{
						var birthYearB2C = kendo.parseDate(obj.detailCustomer.birthYearB2C, "dd/MM/yyyy");
			    		var currentDate = kendo.parseDate(new Date(), "dd/MM/yyyy");
			    		if(birthYearB2C > currentDate){
			    			result = false;
			        		toastr.warning("Ngày sinh khách hàng không hợp lệ.Vui lòng kiểm tra lại");
				    		$("#birthYearB2C").focus();
				    		return;
			        	}
					}
//					var DOB = kendo.parseDate(obj.detailCustomer.dateOfBirth, 'dd/MM/yyyy');vm.resultYesForm.detailCustomer.dateOfBirth = kendo.toString(DOB, 'dd/MM/yyyy');
					vm.resultYesForm.detailCustomer.birthYearB2C = $("#birthYearB2C").val();

					if(obj.detailCustomer.fieldWork==null){
						result = false;
						toastr.warning("Chưa chọn lĩnh vực làm việc !");
						$("#fieldWork").focus();
		    			return;
					}
					if(obj.detailCustomer.customers=='KH ngoại giao'){
						if(obj.detailCustomer.position==null){
							result = false;
							toastr.warning("Chưa nhập chức vụ khách hàng !");
							$("#position").focus();
			    			return;
						}
					}
				}else{
					if(obj.detailCustomer.unitName==null){
						result = false;
						toastr.warning("Chưa nhập tên đơn vị !");
						$("#unitName").focus();
		    			return;
					}
					if(obj.detailCustomer.taxCode==null){
						result = false;
						toastr.warning("Chưa nhập thông tin mã số thuế đơn vị !");
						$("#taxCode").focus();
		    			return;
					}
					if(obj.detailCustomer.business==null){
						result = false;
						toastr.warning("Chưa chọn ngành nghề kinh doanh !");
						$("#business").focus();
		    			return;
					}
					
					if(obj.detailCustomer.customers=='KH ngoại giao'){
						if(obj.detailCustomer.position==null){
							result = false;
							toastr.warning("Chưa nhập chức vụ khách hàng !");
							$("#position").focus();
			    			return;
						}
					}
					if(obj.detailCustomer.representative==null){
						result = false;
						toastr.warning("Chưa nhập tên chủ doanh nghiệp !");
						$("#representative").focus();
		    			return;
					}
					if(obj.detailCustomer.sexRepresentative==null){
						result = false;
						toastr.warning("Chưa chọn giới tính chủ doanh nghiệp !");
						$("#sexRepresentative").focus();
		    			return;
					}
					if(obj.detailCustomer.phoneRepresentative!=null){
		        		var vnf_regex = /((09|03|07|08|05)+([0-9]{8})\b)/g;
			        	if(kendo.parseFloat(obj.detailCustomer.phoneRepresentative)<0){
			        		result = false;
			        		toastr.warning("Số điện thoại chủ doanh nghiệp sai định dạng");
			        		$("#phoneRepresentative").focus();
			            	return;
			        	} else {
			        		if(!vnf_regex.test(obj.detailCustomer.phoneRepresentative)) {
			        			result = false;
			        			toastr.warning("Số điện thoại chủ doanh nghiệp sai định dạng");
			        			$("#phoneRepresentative").focus();
			                	 return;
			                }
			        	}
		        	}
		        	
		        	if(obj.detailCustomer.emailRepresentative!=null){
		        		if(!validateEmail(obj.detailCustomer.emailRepresentative)){
		        			result = false;
			    			toastr.warning("Email chủ doanh nghiệp không đúng định dạng");
				    		$("#emailRepresentative").focus();
				    		return;
			    		}
		        	}
		        	
		        	if(obj.detailCustomer.birthYearRepresentative!=null){
						var birthYearRepresentative = kendo.parseDate(obj.detailCustomer.birthYearRepresentative, "dd/MM/yyyy");
			    		var currentDate = kendo.parseDate(new Date(), "dd/MM/yyyy");
			    		if(birthYearRepresentative > currentDate){
			    			result = false;
			        		toastr.warning("Ngày sinh chủ doanh nghiệp không hợp lệ.Vui lòng kiểm tra lại");
				    		$("#birthYearRepresentative").focus();
				    		return;
			        	}
					}
		        	vm.resultYesForm.detailCustomer.birthYearRepresentative = $("#birthYearRepresentative").val();
		        	
		        	
		        	if(obj.detailCustomer.directContact==null){
						result = false;
						toastr.warning("Chưa nhập tên người đại diện trực tiếp !");
						$("#directContact").focus();
		    			return;
					}
					if(obj.detailCustomer.sexDirectContact==null){
						result = false;
						toastr.warning("Chưa chọn giới tính người đại diện trực tiếp !");
		    			return;
					}
					if(obj.detailCustomer.phoneDirectContact!=null){
		        		var vnf_regex = /((09|03|07|08|05)+([0-9]{8})\b)/g;
			        	if(kendo.parseFloat(obj.detailCustomer.phoneDirectContact)<0){
			        		result = false;
			        		toastr.warning("Số điện thoại người đại diện trực tiếp sai định dạng");
			        		$("#phoneDirectContact").focus();
			            	return;
			        	} else {
			        		if(!vnf_regex.test(obj.detailCustomer.phoneDirectContact)) {
			        			result = false;
			        			toastr.warning("Số điện thoại người đại diện trực tiếp sai định dạng");
			        			$("#phoneDirectContact").focus();
			                	 return;
			                }
			        	}
		        	}
		        	
		        	if(obj.detailCustomer.emailDirectContact!=null){
		        		if(!validateEmail(obj.detailCustomer.emailDirectContact)){
			    			toastr.warning("Email người đại diện trực tiếp không đúng định dạng");
				    		$("#emailDirectContact").focus();
				    		return;
			    		}
		        	}
		        	
		        	if(obj.detailCustomer.birthYearDirectContact!=null){
						var birthYearDirectContact = kendo.parseDate(obj.detailCustomer.birthYearDirectContact, "dd/MM/yyyy");
			    		var currentDate = kendo.parseDate(new Date(), "dd/MM/yyyy");
			    		if(birthYearDirectContact > currentDate){
			    			result = false;
			        		toastr.warning("Ngày sinh người đại diện trực tiếp không hợp lệ.Vui lòng kiểm tra lại");
				    		$("#birthYearDirectContact").focus();
				    		return;
			        	}
					}
		        	vm.resultYesForm.detailCustomer.birthYearDirectContact = $("#birthYearDirectContact").val();
				}
				
			}
				

    		var regex = /^0*?[1-9]\d*$/;
    		if($("#information511").val().trim()!="" && !regex.test(obj.information511)){
    			result = false;
				toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số tầng).Vui lòng nhập đúng định dạng."));
				$("#information511").focus();
				return;
			}
    		
    		if($("#information71").val().trim()!="" && !regex.test(obj.information71)){
    			result = false;
				toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số tầng hầm).Vui lòng nhập đúng định dạng."));
				$("#information71").focus();
				return;
			}
    		if($("#information72").val().trim()!="" && !regex.test(obj.information72)){
    			result = false;
				toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Gara).Vui lòng nhập đúng định dạng."));
				$("#information72").focus();
				return;
			}
    		if($("#information73").val().trim()!="" && !regex.test(obj.information73)){
    			result = false;
				toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số phòng khách).Vui lòng nhập đúng định dạng."));
				$("#information73").focus();
				return;
			}
    		if($("#information74").val().trim()!="" && !regex.test(obj.information74)){
    			result = false;
				toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số phòng bếp).Vui lòng nhập đúng định dạng."));
				$("#information74").focus();
				return;
			}
    		if($("#information75").val().trim()!="" && !regex.test(obj.information75)){
    			result = false;
				toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số phòng ngủ).Vui lòng nhập đúng định dạng."));
				$("#information75").focus();
				return;
			}
    		if($("#information76").val().trim()!="" && !regex.test(obj.information76)){
    			result = false;
				toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số phòng thờ).Vui lòng nhập đúng định dạng."));
				$("#information77").focus();
				return;
			}
    		if($("#information77").val().trim()!="" && !regex.test(obj.information77)){
    			result = false;
				toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số phòng ăn).Vui lòng nhập đúng định dạng."));
				$("#information77").focus();
				return;
			}
    		if($("#information78").val().trim()!="" && !regex.test(obj.information78)){
    			result = false;
				toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số phòng vệ sinh).Vui lòng nhập đúng định dạng."));
				$("#information78").focus();
				return;
			}
    		if($("#information79").val().trim()!="" && !regex.test(obj.information79)){
    			result = false;
				toastr.warning(gettextCatalog.getString("Sai định dạng thông tin (Số sân vườn).Vui lòng nhập đúng định dạng."));
				$("#information79").focus();
				return;
			}
    		if(obj.scheduleBuild != null){
    			if(vm.isInitialResultTangent == true){
    				var scheduleTimeBuild = kendo.parseDate(obj.scheduleBuild, "dd/MM/yyyy");
        			var currentDay = kendo.parseDate(new Date(), "dd/MM/yyyy");
            		if(scheduleTimeBuild < currentDay){
            			result = false;
                		toastr.warning("Ngày dự kiến khởi công không hợp lệ.Vui lòng kiểm tra lại");
        	    		$("#scheduleBuild").focus();
        	    		return;
                	}
    			}
    			vm.resultYesForm.scheduleBuild = $("#scheduleBuild").val();
			}
			
            
            return result;
        };
        
        vm.chooseCustomers = function (data) {
        	if(data == 'KH ngoại giao'){
        		vm.isCustomers = true;
        	}else vm.isCustomers = false;
        }
        
        //Lưu lúc phê duyệt kết quả tiếp xúc
        vm.saveConfirmApprove = function(){
//        	if(vm.resultForm.approvedDescription==null || $("#approvedDescription").val()==""){
//        		toastr.warning("Ghi chú phê duyệt không được để trống !");
//    			$("#approvedDescription").focus();
//    			return;
//        	}
        	var obj = {};
        	obj = angular.copy(vm.resultForm);
        	obj.approvedStatus = "1";
        	obj.tangentCustomerId = vm.tangentCustomerId;
        	obj.customerName = vm.customerName;
        	manageTangentCustomerService.saveApproveOrReject(obj).then(function(result){
        		if(result.error){
        			toastr.error(result.error);
        			return;
        		}
        		toastr.success("Phê duyệt thành công !");
        		modalInstance1.dismiss();
        		doSearch();
        	});
        }
        
      //Lưu lúc từ chối kết quả tiếp xúc
        vm.saveConfirmReject = function(){
        	if(vm.resultForm.approvedPerformerName==null || $("#approvedPerformerName").val()==""){
        		toastr.warning("Giao lại người tiếp xúc không được để trống !");
    			$("#approvedPerformerName").focus();
    			return;
        	}
//        	
//        	if(vm.resultForm.approvedDescription==null || $("#approvedDescription").val()==""){
//        		toastr.warning("Ghi chú phê duyệt không được để trống !");
//    			$("#approvedDescription").focus();
//    			return;
//        	}
        	var obj = {};
        	obj = angular.copy(vm.resultForm);
        	obj.approvedStatus = "2";
        	obj.tangentCustomerId = vm.tangentCustomerId;
        	obj.customerName = vm.customerName;
        	manageTangentCustomerService.saveApproveOrReject(obj).then(function(result){
        		if(result.error){
        			toastr.error(result.error);
        			return;
        		}
        		toastr.success("Từ chối thành công !");
        		modalInstance1.dismiss();
        		doSearch();
        	});
        	
        }
        
        vm.saveNotDemain = function(){
        	if(vm.resultForm.reasonRejection==null || $("#reasonRejection").val()==""){
        		toastr.warning("Lý do khách hàng từ chối không được để trống !");
        		$("#reasonRejection").focus();
        		return;
        	}
        	var obj = {};
        	if(vm.resultForm!=null){
        		obj = angular.copy(vm.resultForm);
        	}
        	obj.approvedStatus = "2";
        	obj.tangentCustomerId = vm.tangentCustomerId;
        	obj.package1 = null;
        	obj.package2 = null;
        	obj.package3 = null;
        	obj.package4 = null;
        	obj.package5 = null;
        	obj.package6 = null;
        	obj.package7 = null;
        	obj.package8 = null;
        	obj.package9 = null;
        	obj.tangentCustomerDTO = angular.copy(vm.viewForm);
        	manageTangentCustomerService.saveNotDemain(obj).then(function(result){
        		if(result.error){
        			toastr.error(result.error);
        			return;
        		}
        		toastr.success("Từ chối thành công !");
        		modalInstance2.dismiss();
        		$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        		doSearch();
        	});
        }
        
        //Save giaỉ pháp
        vm.savePopupGiaiPhap = function(){
        	if(vm.viewForm.status > 2){
            	if(vm.performerId == vm.userLoginId){
            		if(vm.solutionForm.presentSolutionDate==null){
                		toastr.warning("Ngày hẹn trình bày giải pháp không được để trống !");
                		$("#presentSolutionDate").focus();
                		return;
                	}
            		var presentSolutionDate = kendo.parseDate(vm.solutionForm.presentSolutionDate, "dd/MM/yyyy");
            		var currentDate = kendo.parseDate(new Date(), "dd/MM/yyyy");
            		if(presentSolutionDate < currentDate){
                		toastr.warning("Ngày hẹn trình bày giải pháp không hợp lệ.Vui lòng kiểm tra lại");
        	    		$("#presentSolutionDate").focus();
        	    		return;
                	}
            		if(vm.solutionForm.resultSolutionType!=null){
            			if(vm.solutionForm.resultSolutionType==1){
                			if(vm.fileLstGiaiPhap.length==0 && vm.performerSolutionId == vm.userLoginId){
                    			toastr.warning("Chưa có file giải pháp/ không được cập nhật trình bày giải pháp !");
                        		return;
                    		}
                			if(vm.solutionForm.signStatus==0){
                				if(vm.solutionForm.unsuccessfullReason==null){
	                    			toastr.warning("Chưa có ghi lí do không ký được hợp đồng !");
	                    			$("#unsuccessfullReason").focus();
	                        		return;
                				}
                			}
                		}
                		
            			else if(vm.solutionForm.resultSolutionType==2){
            				vm.solutionForm.contractCode = null;
    		                vm.solutionForm.contractId = null;
    		                vm.solutionForm.signDate = null;
    		                vm.solutionForm.guaranteeTime = null;
                    		vm.solutionForm.contractRose = null;
                    		vm.solutionForm.constructTime = null;
                    		vm.solutionForm.price = null;
            				if(vm.solutionForm.contentResultSolution==null || $("#contentResultSolution").val()==""){
                				toastr.warning("Lý do khách hàng từ chối giải pháp không được để trống !");
                				$("#contentResultSolution").focus();
                        		return;
                			}
            			}
            			
            			else if(vm.solutionForm.resultSolutionType==3){
            				vm.solutionForm.contractCode = null;
    		                vm.solutionForm.contractId = null;
    		                vm.solutionForm.signDate = null;
    		                vm.solutionForm.guaranteeTime = null;
                    		vm.solutionForm.contractRose = null;
                    		vm.solutionForm.constructTime = null;
                    		vm.solutionForm.price = null;
            				if(vm.solutionForm.contentResultSolution==null || $("#contentResultSolution").val()==""){
                				toastr.warning("Nội dung cần chỉnh sửa không được để trống !");
                				$("#contentResultSolution").focus();
                        		return;
                			}
            				
            				if(vm.solutionForm.presentSolutionDateNext==null || $("#presentSolutionDateNext").val()==""){
                				toastr.warning("Ngày hẹn trình bày giải pháp tiếp theo không được để trống !");
                				$("#presentSolutionDateNext").focus();
                        		return;
                			}
            			}
            		}
            	}
        	}
//        	}else{
//        		if(vm.solutionForm.presentSolutionDate!=null || $("#presentSolutionDate").val()!=""){
//        			var presentSolutionDate = kendo.parseDate(vm.solutionForm.presentSolutionDate, "dd/MM/yyyy");
//            		var currentDate = kendo.parseDate(new Date(), "dd/MM/yyyy");
//            		if(presentSolutionDate < currentDate){
//                		toastr.warning("Ngày hẹn trình bày giải pháp không hợp lệ.Vui lòng kiểm tra lại");
//        	    		$("#presentSolutionDate").focus();
//        	    		return;
//                	}
//            	}
//        		
//        	}
//        	
        	vm.viewForm.tangentCustomerId = vm.tangentCustomerId;
        	vm.solutionForm.fileResultSolution = vm.fileLstGiaiPhap;
        	vm.viewForm.resultSolutionDTO = vm.solutionForm;
        	
        	manageTangentCustomerService.saveResultSolution(vm.viewForm).then(function(result){
        		if(result.error){
        			toastr.error(result.error);
        			return;
        		}
        		toastr.success("Cập nhật giải pháp thành công !");
        		modalInstance1.dismiss();
        		doSearch();
        	});
        }
        
      //Lưu lúc phê duyệt kết quả giaỉ pháp
        vm.saveConfirmApproveGiaiPhap = function(){
//        	if(vm.solutionForm.approvedDescription==null || $("#approvedDescription").val()==""){
//        		toastr.warning("Ghi chú phê duyệt không được để trống !");
//    			$("#approvedDescription").focus();
//    			return;
//        	}
        	var obj = {};
        	obj = angular.copy(vm.solutionForm);
        	obj.approvedStatus = "1";
        	obj.tangentCustomerId = vm.tangentCustomerId;
        	obj.customerName = vm.customerName;
        	obj.provinceId = vm.provinceId;
        	manageTangentCustomerService.saveApproveOrRejectGiaiPhap(obj).then(function(result){
        		if(result.error){
        			toastr.error(result.error);
        			return;
        		}
        		toastr.success("Phê duyệt thành công !");
        		modalInstance1.dismiss();
        		doSearch();
        	});
        }
        
      //Lưu lúc từ chối kết quả giải pháp
        vm.saveConfirmRejectGiaiPhap = function(){
        	if(vm.solutionForm.approvedPerformerName==null || $("#approvedPerformerName").val()==""){
        		toastr.warning("Giao lại người tiếp xúc không được để trống !");
    			$("#approvedPerformerName").focus();
    			return;
        	}
//        	
//        	if(vm.solutionForm.approvedDescription==null || $("#approvedDescription").val()==""){
//        		toastr.warning("Ghi chú phê duyệt không được để trống !");
//    			$("#approvedDescription").focus();
//    			return;
//        	}
        	var obj = {};
        	obj = angular.copy(vm.solutionForm);
        	obj.approvedStatus = "2";
        	obj.tangentCustomerId = vm.tangentCustomerId;
        	obj.customerName = vm.customerName;
        	obj.provinceId = vm.provinceId;
        	manageTangentCustomerService.saveApproveOrRejectGiaiPhap(obj).then(function(result){
        		if(result.error){
        			toastr.error(result.error);
        			return;
        		}
        		toastr.success("Từ chối thành công !");
        		modalInstance1.dismiss();
        		doSearch();
        	});
        	
        }
        
        vm.approveRose = approveRose;
        function approveRose(dataItem){
        	confirm("Xác nhận phê duyệt?", function(){
        		manageTangentCustomerService.approveRose(dataItem).then(function(result){
                    if (data.error) {
                        toastr.error(data.error);
                        return;
                    }
                    toastr.success("Hoàn thành thanh toán hoa hồng!");
                    doSearch();
            	}).catch(function (e) {
            		toastr.error(gettextCatalog.getString("Lỗi khi phê duyệt"));
                    return;
                });
        	});
        }
        
        vm.provinceOptions = {
                dataTextField: "name",
                dataValueField: "id",
                placeholder: "Nhập mã hoặc tên tỉnh",
                select: function (e) {
                    vm.isSelect = true;
                    var dataItem = this.dataItem(e.item.index());
                    vm.searchForm.provinceId = dataItem.catProvinceId;
                    vm.searchForm.provinceCode = dataItem.code;
                    vm.searchForm.provinceName = dataItem.name;
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
                                name: vm.searchForm.provinceCode,
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
         }
        
        vm.checkPhoneNumber = checkPhoneNumber;
        function checkPhoneNumber(phoneN){
        	vm.validatePhoneMsg = null;
        	var phoneNumberRegexB2C = /(0[3|5|7|8|9])+([0-9]{8})\b/;
        	var phoneNumberRegexB2B = /(0[2])+([0-9]{9})\b/;
        	if(phoneN!=null && phoneN !='') {
        		if(phoneN.length<10 || phoneN.length>11) {
        			vm.validatePhoneMsg = 'Số điện thoại không đúng định dạng!';
        			vm.validatePhone = false;
        		} else {
        			if(vm.addForm.partnerType==1){
        				if (phoneNumberRegexB2C.test(phoneN) == false) 
                        {
                        	vm.validatePhoneMsg = 'Số điện thoại không đúng định dạng!';
                        	vm.validatePhone = false;
                        }else{
                       	 	vm.validatePhone = true;
                        }
        			} else {
        				if (phoneN.length == 10 && phoneNumberRegexB2C.test(phoneN) == false) {
                        	vm.validatePhoneMsg = 'Số điện thoại không đúng định dạng!';
                        	vm.validatePhone = false;
                        }if (phoneN.length == 11 && phoneNumberRegexB2B.test(phoneN) == false) {
                        	vm.validatePhoneMsg = 'Số điện thoại không đúng định dạng!';
                        	vm.validatePhone = false;
                        }
        			
        				else{
                       	 	vm.validatePhone = true;
                        }
        			}
        		}
        	} else{
            	vm.validatePhoneMsg = 'Số điện thoại không được để trống!';
            	vm.validatePhone = false;
            }
        }
		// end controller
	}
})();