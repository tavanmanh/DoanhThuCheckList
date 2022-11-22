(function() {
	'use strict';
	var controllerId = 'manageTangentCustomerGPTHController';

	angular.module('MetronicApp').controller(controllerId,
			manageTangentCustomerGPTHController);

	function manageTangentCustomerGPTHController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, manageTangentCustomerGPTHService,
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
		vm.detailTangentCustomerGPTH = {};
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
		
		vm.contactForm = [
			{
				code: "Email",
				name: "Email"
			},
			{
				code: "Điện thoại",
				name: "Điện thoại"
			},
			{
				code: "Gặp trực tiếp",
				name: "Gặp trực tiếp"
			}
		];
		
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
			manageTangentCustomerGPTHService.checkRoleApproved().then(function(result){
				if(result==1){
					vm.checkRoleApproved = true;
				}
			});
			
			manageTangentCustomerGPTHService.checkRoleUpdate().then(function(result){
				if(result==1){
					vm.checkRoleUpdate = true;
				}
			});
			
			manageTangentCustomerGPTHService.getContractRose().then(function(result){
				if(result!=null){
					vm.contractRose = result;
				}
			});
			
			manageTangentCustomerGPTHService.checkRoleUserAssignYctx().then(function(result){
				if(result==1){
					vm.checkRoleUserPgd = true;
				}
			});
			
			var obj = {};
			
			manageTangentCustomerGPTHService.getChannel(obj).then(function(result){
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
			manageTangentCustomerGPTHService.checkRoleSourceYCTX(obj).then(function(result){
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
			manageTangentCustomerGPTHService.checkRoleCreated().then(function(d){
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
//        		if(data == 1) {
//            		vm.detailTangentCustomerGPTH.detailCustomer.emailB2C = vm.viewForm.customerEmail;
//            		vm.detailTangentCustomerGPTH.detailCustomer.phoneB2C = vm.viewForm.customerPhone;
//            	}else{
//            		vm.detailTangentCustomerGPTH.detailCustomer.emailB2C = null;
//            		vm.detailTangentCustomerGPTH.detailCustomer.phoneB2C = null;
//            		vm.detailTangentCustomerGPTH.detailCustomer.business = 'Lĩnh vực Xây dựng';
//            		vm.detailTangentCustomerGPTH.detailCustomer.customers = 'KH thường';
//            		vm.detailTangentCustomerGPTH.detailCustomer.positionDirectContact = 'Nhân viên/ Chuyên viên';
//            		
//            	}
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
				vm.detailTangentCustomerGPTH.lstImageDesign.splice(dataItem, 1);
				if(vm.detailTangentCustomerGPTH.lstImageDesign.length < 3){
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
                manageTangentCustomerGPTHService.uploadAttachment(uploadFileService, formData)
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
                            vm.detailTangentCustomerGPTH.lstImageDesign.push(mappingObj);
                            $('#attachmentFileTableGrid').data('kendoGrid').dataSource.data(vm.detailTangentCustomerGPTH.lstImageDesign);
                            if(vm.detailTangentCustomerGPTH.lstImageDesign.length >= 3){
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
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-disabled="vm.disableExportExcel" ng-click="vm.exportExcel()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
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
                            url: Constant.BASE_SERVICE_URL + "tangentCustomerGPTHRestService/doSearch",
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
                        title: "Mã BCI",
                        field: 'bciCode',
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
	                    	if(vm.isInternalSource == 1){
	                    		return Restangular.all("tangentCustomerGPTHRestService/getAllContractXdddSuccessInternal").post({
		                        	createdDate: vm.viewForm.createdDate,
		                        	contractCode: vm.solutionForm.contractCode,
			                        pageSize: vm.patternContractOptions.pageSize,
			                        page: 1
		                        }).then(function(response){
		                            options.success(response.data);
		                        }).catch(function (err) {
		                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
		                        });
	                    	}else{
	                    		return Restangular.all("tangentCustomerGPTHRestService/getAllContractXdddSuccess").post({
		                        	createdDate: vm.viewForm.createdDate,
		                        	contractCode: vm.solutionForm.contractCode,
			                        pageSize: vm.patternContractOptions.pageSize,
			                        page: 1
		                        }).then(function(response){
		                            options.success(response.data);
		                        }).catch(function (err) {
		                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
		                        });
	                    	}
	                        
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
			
			var teamplateUrl = "coms/manageTangentCustomerGPTH/manageTangentCustomerPopup.html";
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
			var teamplateUrl = "coms/manageTangentCustomerGPTH/manageTangentCustomerPopup.html";
			var title = "Cập nhật quản lý yêu cầu tiếp xúc";
			var windowId = "EDIT_MANAGE_CONFIG";
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
			vm.tangentCustomerGPTHId = dataItem.tangentCustomerId;
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
			manageTangentCustomerGPTHService.getListResultTangentByResultTangentId({resultTangentId: dataItem.resultTangentId,tangentCustomerId: dataItem.tangentCustomerId,partnerType:dataItem.partnerType}).then(function(data){
				if(data==null){
					vm.resultFormDetail = {};
				} else {
					vm.resultFormDetail = angular.copy(data);
				}
				if(vm.resultFormDetail!=null){
        			vm.detailTangentCustomerGPTHDetail = vm.resultFormDetail.resultTangentDetailYesDTO;
        			if(vm.detailTangentCustomerGPTHDetail!=null){
        				if(vm.detailTangentCustomerGPTHDetail.information42!=null){
            				vm.detailTangentCustomerGPTHDetail.information421 = vm.detailTangentCustomerGPTHDetail.information42.split(";")[0];
                			vm.detailTangentCustomerGPTHDetail.information422 = vm.detailTangentCustomerGPTHDetail.information42.split(";")[1];
                			vm.detailTangentCustomerGPTHDetail.information423 = vm.detailTangentCustomerGPTHDetail.information42.split(";")[2];
            			}
            			
            			if(vm.detailTangentCustomerGPTHDetail.information53=="1"){
            				vm.detailTangentCustomerGPTHDetail.information53 = true;
            			} else {
            				vm.detailTangentCustomerGPTHDetail.information53 = false;
            			}
            			
            			if(vm.detailTangentCustomerGPTHDetail.information54=="1"){
            				vm.detailTangentCustomerGPTHDetail.information54 = true;
            			} else {
            				vm.detailTangentCustomerGPTHDetail.information54 = false;
            			}
            			
            			if(vm.detailTangentCustomerGPTHDetail.information55=="1"){
            				vm.detailTangentCustomerGPTHDetail.information55 = true;
            			} else {
            				vm.detailTangentCustomerGPTHDetail.information55 = false;
            			}
            			fillFileTable(vm.detailTangentCustomerGPTHDetail.lstImageDesign);
        				vm.detailTangentCustomerGPTHDetail.information511 = vm.detailTangentCustomerGPTHDetail.information1_1;
            			vm.detailTangentCustomerGPTHDetail.information521 = vm.detailTangentCustomerGPTHDetail.information1_21;
            			vm.detailTangentCustomerGPTHDetail.information522 = vm.detailTangentCustomerGPTHDetail.information1_22;
            			vm.detailTangentCustomerGPTHDetail.information523 = vm.detailTangentCustomerGPTHDetail.information1_23;
            			vm.detailTangentCustomerGPTHDetail.information531 = vm.detailTangentCustomerGPTHDetail.information1_3;
            			vm.detailTangentCustomerGPTHDetail.information4 = vm.detailTangentCustomerGPTHDetail.information3;
            			vm.detailTangentCustomerGPTHDetail.information71 = vm.detailTangentCustomerGPTHDetail.information3_1;
            			vm.detailTangentCustomerGPTHDetail.information72 = vm.detailTangentCustomerGPTHDetail.information3_2;
            			vm.detailTangentCustomerGPTHDetail.information73 = vm.detailTangentCustomerGPTHDetail.information3_3;
            			vm.detailTangentCustomerGPTHDetail.information74 = vm.detailTangentCustomerGPTHDetail.information3_4;
            			vm.detailTangentCustomerGPTHDetail.information75 = vm.detailTangentCustomerGPTHDetail.information3_5;
            			vm.detailTangentCustomerGPTHDetail.information76 = vm.detailTangentCustomerGPTHDetail.information3_6;
            			vm.detailTangentCustomerGPTHDetail.information77 = vm.detailTangentCustomerGPTHDetail.information3_7;
            			vm.detailTangentCustomerGPTHDetail.information78 = vm.detailTangentCustomerGPTHDetail.information3_8;
            			vm.detailTangentCustomerGPTHDetail.information79 = vm.detailTangentCustomerGPTHDetail.information3_9;
        			}
					vm.resultNoFormDetail = vm.resultFormDetail.resultTangentDetailNoDTO;
					if(vm.resultNoFormDetail!=null){
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
						
					}
        			
        		}
				
				CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '90%', '90%', "deptAdd");
				setTimeout(function(){
					modalInstance2 = CommonService.getModalInstance1();
				},100);
			});
        }
        
        
        function viewData(dataItem){
				var teamplateUrl = "coms/manageTangentCustomerGPTH/manageTangentCustomerDetailPopup.html";
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
				
				manageTangentCustomerGPTHService.getListResultTangentByTangentCustomerId({tangentCustomerGPTHId: dataItem.tangentCustomerGPTHId,partnerType:dataItem.partnerType}).then(function(data){
					if(data.resultTangentGPTHDTO==null){
						vm.resultForm = {};
					} else {
						vm.resultForm = angular.copy(data.resultTangentGPTHDTO);
						vm.resultSolutionGPTHDTO = data.resultTangentGPTHDTO.resultSolutionGPTHDTO;
						vm.detailTangentCustomerGPTH = data.detailTangentCustomerGPTH;
					}
					if(vm.resultForm!=null){
	        			if(vm.resultForm.resultTangentDetailYesDTO==null){
	        				vm.changeInfor(vm.viewForm.partnerType);
	        				vm.disabledPartnerType = false;
	        			} else {
	        				vm.detailTangentCustomerGPTH = vm.resultForm.resultTangentDetailYesDTO;
	        				vm.detailTangentCustomerGPTH.yearDoB = vm.viewForm.bciCode;
	        				if(vm.detailTangentCustomerGPTH.detailCustomer == null || vm.detailTangentCustomerGPTH.detailCustomer == undefined){
	        					vm.detailTangentCustomerGPTH.detailCustomer = {};
	        					vm.changeInfor(vm.viewForm.partnerType);
	        				}
	        				
	        				vm.disabledPartnerType = false;
	        				
	        			}
	        			if(vm.resultForm.resultTangentDetailNoDTO==null){
	        				vm.resultNoForm = {};
	        			} else {
	        				vm.resultNoForm = vm.resultForm.resultTangentDetailNoDTO;
	        			}
	        			
	        		}
					
					if(data.resultSolutionGPTHDTO==null){
						vm.solutionForm={};
					} else {
						vm.solutionForm = angular.copy(data.resultSolutionGPTHDTO);
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
					}
					
					//Lịch sử tiếp xúc
					vm.countHistoryResultTagent = 0;
					vm.countHistoryResultSolution = 0;
					vm.countHistoryResultTagent = data.listResultTangentGPTH.length;
					vm.countHistoryResultSolution = data.listResultSolutionGPTH.length;
					
					vm.resultHisForm1 = angular.copy(data.listResultTangentGPTH[2]);
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
					
					vm.resultHisForm2 = angular.copy(data.listResultTangentGPTH[1]);
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
					
					vm.resultHisForm3 = angular.copy(data.listResultTangentGPTH[0]);
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
					vm.solutionHisForm1 = angular.copy(data.listResultSolutionGPTH[2]);
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
					
					vm.solutionHisForm2 = angular.copy(data.listResultSolutionGPTH[1]);
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
					
					vm.solutionHisForm3 = angular.copy(data.listResultSolutionGPTH[0]);
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

					manageTangentCustomerGPTHService.findConversation({tangentCustomerGPTHId: dataItem.tangentCustomerGPTHId}).then(function(data) {
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
        		manageTangentCustomerGPTHService.deleteRecord(dataItem).then(function(result){
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
        
        vm.exportExcel = function(){
        	vm.searchForm.page = null;
        	vm.searchForm.pageSize = null;
        	var obj = {};
        	obj = angular.copy(vm.searchForm);
        	kendo.ui.progress($("#tangentCustomerGrid"), true);
        	return Restangular.all("tangentCustomerGPTHRestService/exportFile").post(obj).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                kendo.ui.progress($("#tangentCustomerGrid"), false);
            }).catch(function (e) {
            	kendo.ui.progress($("#tangentCustomerGrid"), false);
                toastr.error(gettextCatalog.getString("Lỗi khi tải xuống file!"));
                return;
            });
        }
        
       
        
        vm.exportExcelDetailData = function(obj){     	
        	if(obj.status > 2){
        		obj.resultSolutionGPTHDTO = vm.solutionForm;
        		if(obj.resultTangentGPTHDTO.resultTangentType == 1)	obj.resultTangentGPTHDTO.resultTangentDetailYesDTO = vm.detailTangentCustomerGPTH;
            	else if  (obj.resultTangentGPTHDTO.resultTangentType == 2) obj.resultTangentGPTHDTO.resultTangentDetailNoDTO = vm.resultNoForm;
        	}
     	
        	kendo.ui.progress($("#tangentCustomerGrid"), true);
        	return Restangular.all("tangentCustomerGPTHRestService/exportExcelDetailData").post(obj).then(function (d) {
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
		            	vm.detailTangentCustomerGPTH.provinceConstruction = dataItem.code;
		            	vm.areaIdOfProvince = dataItem.areaId;
		            	vm.detailTangentCustomerGPTH.provinceId = dataItem.catProvinceId;
		            }else{
		            	 vm.addForm.provinceId = dataItem.catProvinceId;
				         vm.addForm.provinceCode = dataItem.code;
				         vm.areaIdOfProvince = dataItem.areaId;
		            }
		            var obj={
		            	catProvinceId: dataItem.catProvinceId
		            };
		            //Huypq-21032022-edit
		            manageTangentCustomerGPTHService.getUserConfigTagentByProvince(obj).then(function(data){
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
		                        name: vm.showDetail==false ? vm.addForm.provinceCode : vm.detailTangentCustomerGPTH.provinceConstruction,
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
			            	vm.detailTangentCustomerGPTH.provinceConstruction = null;
			            	vm.areaIdOfProvince = null;
			            	vm.detailTangentCustomerGPTH.provinceId = null;
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
			            	vm.detailTangentCustomerGPTH.provinceConstruction = null;
			            	vm.areaIdOfProvince = null;
			            	vm.detailTangentCustomerGPTH.provinceId = null;
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
		            	vm.detailTangentCustomerGPTH.districtConstruction = dataItem.districtName;
		            	vm.detailTangentCustomerGPTH.districtId = dataItem.districtId;
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
			                    if(vm.detailTangentCustomerGPTH.provinceId ==null){
			                    	toastr.warning("Chưa chọn quận/huyện");
			                    	return;
			                    }
		                    }
		                    return Restangular.all("tangentCustomerGPTHRestService/doSearchDistrictByProvinceCode").post({
		                    	districtName: vm.showDetail==false ? vm.addForm.districtName : vm.detailTangentCustomerGPTH.districtConstruction,
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
			            	vm.detailTangentCustomerGPTH.districtConstruction = null;
			            	vm.detailTangentCustomerGPTH.districtId = null;
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
			            	vm.detailTangentCustomerGPTH.districtConstruction = null;
			            	vm.detailTangentCustomerGPTH.districtId = null;
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
		            	vm.detailTangentCustomerGPTH.communeConstruction = dataItem.communeName;
		            	vm.detailTangentCustomerGPTH.areaId = dataItem.communeId;
		            	vm.detailTangentCustomerGPTH.communeId = dataItem.communeId;
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
			                    if(vm.detailTangentCustomerGPTH.districtId==null){
			                    	toastr.warning("Chưa chọn quận/huyện");
			                    	options.success([]);
			                    	return;
			                    } 
		                    }
		                    return Restangular.all("tangentCustomerGPTHRestService/doSearchCommunneByDistrict").post({
			                    	communeName: vm.showDetail==false ? vm.addForm.communeName : vm.detailTangentCustomerGPTH.communeConstruction,
			                        pageSize: vm.communeNameOptions.pageSize,
			                        districtId:   vm.showDetail==false ? vm.addForm.districtId : vm.detailTangentCustomerGPTH.districtId,
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
				            vm.detailTangentCustomerGPTH.communeConstruction = null;
				            vm.detailTangentCustomerGPTH.areaId = null;
				            vm.detailTangentCustomerGPTH.communeId = null;
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
				            vm.detailTangentCustomerGPTH.communeConstruction = null;
				            vm.detailTangentCustomerGPTH.areaId = null;
				            vm.detailTangentCustomerGPTH.communeId = null;
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
        			if(vm.detailTangentCustomerGPTH.provinceId == null ){
	        			vm.detailTangentCustomerGPTH.provinceConstruction = null;
		            	vm.detailTangentCustomerGPTH.provinceId = null;
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
        			if(vm.detailTangentCustomerGPTH.districtId == null ){
        				vm.detailTangentCustomerGPTH.districtConstruction = null;
    	            	vm.detailTangentCustomerGPTH.districtId = null;
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
        			
        			if(vm.detailTangentCustomerGPTH.communeId == null ){
        				vm.detailTangentCustomerGPTH.communeConstruction = null;
    		            vm.detailTangentCustomerGPTH.areaId = null;
    		            vm.detailTangentCustomerGPTH.communeId = null;
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
        	var today = new Date();

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
        	var dateArr = vm.addForm.suggestTime.split("/");
        	if(today.getFullYear() >  dateArr[2] || (today.getFullYear() ==  dateArr[2] && today.getMonth() + 1 > dateArr[1]) 
        			|| (today.getFullYear() ==  dateArr[2] && today.getMonth() + 1 == dateArr[1] && (today.getDate() >= dateArr[0]))){
        		toastr.warning("Thời gian đề nghị tư vấn phải lớn hơn hiện tại ít nhất 1 ngày!");
        		$("#suggestTime").focus();
        		return;
        	}
        	if(vm.addForm.bciCode==null){
        		toastr.warning("Mã BCI không được để trống !");
        		$("#bciCode").focus();
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
        	
        	if(vm.addForm.customerPhone==null){
        		toastr.warning("Số điện thoại không được để trống !");
        		$("#customerPhone").focus();
        		return;
        	}
        	
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
    		
        	vm.addForm.bciCode = $("#bciCode").val();
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
        			manageTangentCustomerGPTHService.save(vm.addForm).then(function(result){
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
        		confirm("Xác nhận cập nhật yêu cầu tiếp xúc ?", function(){
        			manageTangentCustomerGPTHService.update(vm.addForm).then(function(result){
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
        	vm.tangentCustomerGPTHId = dataItem.tangentCustomerId;
        	vm.customerName = dataItem.customerName;
        	var teamplateUrl = "coms/manageTangentCustomer/confirmReasonRejectPopup.html";
			var title = "Xác nhận lý do khách hàng từ chối";
			var windowId = "CONFIRM_REASON_REJECT";
			manageTangentCustomerGPTHService.getListResultTangentJoinSysUserByTangentCustomerId({tangentCustomerId : vm.tangentCustomerGPTHId}).then(function(data){
				vm.resultForm = angular.copy(data[0]);
				CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '60%', '60%', "deptAdd");
	    		setTimeout(function(){
					modalInstance1 = CommonService.getModalInstance1();
				},100);
			});
        }
        
        //Kết quả giải pháp
        vm.confirmRejectGiaiPhap = function(dataItem){
        	vm.provinceId = dataItem.provinceId;
        	vm.tangentCustomerGPTHId = dataItem.tangentCustomerId;
        	vm.customerName = dataItem.customerName;
        	var teamplateUrl = "coms/manageTangentCustomer/confirmReasonRejectGiaiPhapPopup.html";
			var title = "Xác nhận lý do khách hàng từ chối";
			var windowId = "CONFIRM_REASON_REJECT_GP";
			
			manageTangentCustomerGPTHService.getResultSolutionJoinSysUserByTangentCustomerId(vm.tangentCustomerGPTHId).then(function(data){
				vm.solutionForm = angular.copy(data[0]);
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
//			vm.viewForm.tangentCustomerGPTHId = vm.tangentCustomerGPTHId;
        	if(vm.resultForm.resultTangentType==0){
    			var teamplateUrl = "coms/manageTangentCustomer/reasonRejectPopup.html";
    			var title = "Lý do khách hàng từ chối";
    			var windowId = "REASON_REJECT";
    			CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '60%', '45%', "deptAdd");
        		setTimeout(function(){
    				modalInstance2 = CommonService.getModalInstance1();
    			},100);
        	} else{
            		if(vm.viewForm.status >= 2){
            			if(vm.resultForm.resultTangentType!=null && vm.resultForm.resultTangentType == "1"){
            				if(vm.viewForm.detailTangentCustomerGPTH == null){
            					vm.viewForm.detailTangentCustomerGPTH = {};
            				}
            				vm.viewForm.detailTangentCustomerGPTH.partnerType =  vm.viewForm.partnerType;
                    			if(vm.detailTangentCustomerGPTH.presentSolutionDate != null){
                    				var presentSolutionDate = kendo.parseDate(vm.detailTangentCustomerGPTH.presentSolutionDate, "dd/MM/yyyy");
                        			var current = kendo.parseDate(new Date(), "dd/MM/yyyy");
                            		if(presentSolutionDate < current){
                                		toastr.warning("Ngày hẹn trình bày giải pháp không hợp lệ.Vui lòng kiểm tra lại");
                        	    		$("#presentSolutionDate").focus();
                        	    		return;
                                	}
                    			}
                    			if(vm.detailTangentCustomerGPTH.presentSolutionDate != null){
                        			var presentDate = kendo.parseDate(vm.detailTangentCustomerGPTH.presentSolutionDate, 'dd/MM/yyyy');vm.detailTangentCustomerGPTH.presentSolutionDate = kendo.toString(presentDate, 'dd/MM/yyyy');
                        		}
                    			if (!validateSaveTangent(vm.detailTangentCustomerGPTH)) {
                                    return;
                                }
                    			if(vm.viewForm.partnerType ==1){
                    				vm.viewForm.partnerCode = "KHCN-" + ""+ vm.viewForm.provinceCode + "-" + vm.viewForm.customerPhone;
                    				var dateStr = kendo.toString($('#bciCodeB2C').val(), "dd/MM/yyyy");
                    				var dateParts = dateStr.split("/");
                    				vm.viewForm.bciCode = dateParts[2];
                    			}else vm.viewForm.partnerCode = "KHDN- " + ""+ vm.viewForm.provinceCode + "-" + vm.viewForm.customerPhone;
                    			
                        		vm.viewForm.resultTangentGPTHDTO = vm.resultForm;
                        		vm.viewForm.resultTangentDetailYesDTO = vm.detailTangentCustomerGPTH;
                    			
                		}
            		
            	}
            	vm.viewForm.resultTangentGPTHDTO = vm.resultForm;
            	vm.viewForm.detailTangentCustomerGPTH = vm.detailTangentCustomerGPTH;
            	if(vm.viewForm.partnerType)
            		vm.solutionForm = {};
            	if(vm.viewForm.status==2){
            		vm.solutionForm.presentSolutionDate = vm.detailTangentCustomerGPTH.presentSolutionDate;
            		vm.viewForm.resultSolutionGPTHDTO = vm.solutionForm;
            	}
            	
            	manageTangentCustomerGPTHService.saveDetail(vm.viewForm).then(function(result){
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
            if(vm.viewForm.partnerType==undefined || vm.viewForm.partnerType==null){
            	result = false;
				toastr.warning("Chưa chọn loại khách hàng !");
    			return;
			}else{
				if(vm.viewForm.partnerType==1){
				}else{
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
		        	
				}
				
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
        	var obj = {};
        	obj = angular.copy(vm.resultForm);
        	obj.approvedStatus = "1";
        	obj.tangentCustomerGPTHId = vm.tangentCustomerGPTHId;
        	obj.customerName = vm.customerName;
        	manageTangentCustomerGPTHService.saveApproveOrReject(obj).then(function(result){
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
        	var obj = {};
        	obj = angular.copy(vm.resultForm);
        	obj.approvedStatus = "2";
        	obj.tangentCustomerGPTHId = vm.tangentCustomerGPTHId;
        	obj.customerName = vm.customerName;
        	manageTangentCustomerGPTHService.saveApproveOrReject(obj).then(function(result){
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
        	obj.tangentCustomerGPTHDTO = angular.copy(vm.viewForm);
        	manageTangentCustomerGPTHService.saveNotDemain(obj).then(function(result){
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
//        	vm.viewForm.tangentCustomerGPTHId = vm.tangentCustomerGPTHId;
        	vm.solutionForm.fileResultSolution = vm.fileLstGiaiPhap;
        	vm.viewForm.resultSolutionGPTHDTO = vm.solutionForm;
        	
        	manageTangentCustomerGPTHService.saveResultSolution(vm.viewForm).then(function(result){
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
        	obj.tangentCustomerGPTHId = vm.tangentCustomerGPTHId;
        	obj.customerName = vm.customerName;
        	obj.provinceId = vm.provinceId;
        	manageTangentCustomerGPTHService.saveApproveOrRejectGiaiPhap(obj).then(function(result){
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
        	obj.tangentCustomerGPTHId = vm.tangentCustomerGPTHId;
        	obj.customerName = vm.customerName;
        	obj.provinceId = vm.provinceId;
        	manageTangentCustomerGPTHService.saveApproveOrRejectGiaiPhap(obj).then(function(result){
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
        		manageTangentCustomerGPTHService.approveRose(dataItem).then(function(result){
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