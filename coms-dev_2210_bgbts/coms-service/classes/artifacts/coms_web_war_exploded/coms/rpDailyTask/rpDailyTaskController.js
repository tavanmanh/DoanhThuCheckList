(function () {
	'use strict';
	var controllerId = 'rpDailyTaskController';
	angular.module('MetronicApp').controller(controllerId, rpDailyTaskController);
	
	function rpDailyTaskController($scope, $rootScope, $timeout, gettextCatalog, $filter, kendoConfig, $kWindow, CommonService, 
			PopupConst, Restangular, RestEndpoint, Constant, workItemService, htmlCommonService, $http) {
		var vm = this;
		var record = 0;
		vm.rpDailyTaskSearch = {};
		vm.String = "Quản lý công trình > Báo cáo > Báo cáo thực hiện công việc theo ngày";
		
		vm.showImage = showImage;
		vm.imageSelected = {};
		vm.listImage = [];
		vm.doSearch = doSearch;
		vm.openUser = openUser;
		vm.onSaveSysUser = onSaveSysUser;
		vm.checkErrDate = checkErrDate;
		vm.openConstruction = openConstruction;
		vm.clearUser = clearUser;
		vm.clearSearchDate = clearSearchDate;
		vm.clearConstructionType = clearConstructionType;
		vm.clearProvince = clearProvince;
		vm.clearConstruction = clearConstruction;
		vm.clearWorkItem = clearWorkItem;
		vm.changeImage = changeImage;
		vm.exportFile = exportFile;
		vm.openCatProvincePopup = openCatProvincePopup;
		vm.onSaveCatProvince = onSaveCatProvince;
	    vm.openCatConstructionTypePopup = openCatConstructionTypePopup;
	    vm.onSaveCatConstructionType = onSaveCatConstructionType;
	    vm.openComsConstructionPopup = openComsConstructionPopup;
	    vm.onSaveComsConstruction = onSaveComsConstruction;
	    vm.openComsWorkItemPopup = openComsWorkItemPopup;
	    vm.onSaveComsWorkItem = onSaveComsWorkItem;
	    
		initFormData();
		function initFormData() {
			fillDataTable();
			vm.rpDailyTaskSearch.updateTime = kendo.toString(new Date(), "dd/MM/yyyy");
		}
		
		vm.patternSignerOptions = {
			dataTextField: "fullName",
			placeholder:"Nhập mã hoặc tên người thực hiện",
			open: function (e) {
				vm.isSelect = false;
			},
			select: function (e) {
				vm.isSelect = true;
				data = this.dataItem(e.item.index());
				vm.rpDailyTaskSearch.userName = data.fullName;
				vm.rpDailyTaskSearch.sysuserid = data.sysUserId;
			},
			pageSize: 10,
			dataSource: {
				serverFiltering: true,
				transport: {
					read: function (options) {
						vm.isSelect = false;
						return Restangular.all("sysUserCOMSRsService/getForAutoCompleteInSign").post({ pageSize: 10, page: 1, fullName: $("#signerGroup").val().trim() }).then(function (response) {
							options.success(response);
							if (response == [] || $("#signerGroup").val().trim() == "") {
								vm.rpDailyTaskSearch.userName = null;
								vm.rpDailyTaskSearch.sysuserid = null;
							}
						}).catch(function (err) {
							vm.rpDailyTaskSearch.userName = null;
							vm.rpDailyTaskSearch.sysuserid = null;
						});
					}
				}
			},
			headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
				'<p class="col-md-6 text-header-auto border-right-ccc">Mã nhân viên</p>' +
				'<p class="col-md-6 text-header-auto">Họ tên</p>' +
				'</div>',
			template: '<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.employeeCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
			change: function (e) {
				if (!vm.isSelect) {
					vm.rpDailyTaskSearch.userName = null;
					vm.rpDailyTaskSearch.sysuserid = null;
				}
			},
            close: function (e) {
                if (!vm.isSelect) {
					vm.rpDailyTaskSearch.userName = null;
					vm.rpDailyTaskSearch.sysuserid = null;
                }
            }
		};

		vm.patternConstructOptions={
				dataTextField: "name",
				placeholder:"Nhập mã hoặc tên loại công trình",
				open: function(e) {
					vm.isSelect = false;
				},
				select: function(e) {
					vm.isSelect = true; 
					data = this.dataItem(e.item.index());
					vm.rpDailyTaskSearch.constructiontypename = data.name;
					vm.rpDailyTaskSearch.catConstructionType = data.catConstructionTypeId;
				},
				pageSize: 10,
				dataSource: {
					serverFiltering: true,
					transport: {
						read: function(options) {
	                        vm.isSelect = false;
							return Restangular.all("kpiLogMobileService/kpiLogMobile/getConstrTypeForAutoComplete").post({pageSize:10, page:1, name:vm.rpDailyTaskSearch.constructiontypename}).then(function(response){
								options.success(response.data);
								var check = response == [] || $("#constructionType").val().trim() == "";
							}).catch(function (err) {
								console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
							});
						}
					}
				},
				headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
				'<p class="col-md-6 text-header-auto border-right-ccc">Mã loại công trình</p>' +
				'<p class="col-md-6 text-header-auto">Tên loại công trình</p>' +
				'</div>',
				template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.code #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
				change: function(e) {
					if(!vm.isSelect) {
						vm.rpDailyTaskSearch.constructiontypename  = null;
						vm.rpDailyTaskSearch.catConstructionType = null;
					}
				},
	            close: function (e) {
	                if (!vm.isSelect) {
						vm.rpDailyTaskSearch.constructiontypename  = null;
						vm.rpDailyTaskSearch.catConstructionType = null;
	                }
	            }
		};

        vm.provinceOptions = {
            dataTextField: "name",
            dataValueField: "id",
			placeholder:"Nhập mã hoặc tên tỉnh",
            select: function (e) {
                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.rpDailyTaskSearch.provincename = dataItem.name;
                vm.rpDailyTaskSearch.provincecode = dataItem.code;
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
                            name: vm.rpDailyTaskSearch.provincename,
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
                    vm.rpDailyTaskSearch.provincename = null;
                    vm.rpDailyTaskSearch.provincecode = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.rpDailyTaskSearch.provincename = null;
                    vm.rpDailyTaskSearch.provincecode = null;
                }
            }
        }

        vm.constructionOptions = {
            dataTextField: "code",
            dataValueField: "constructionId",
			placeholder:"Nhập mã hoặc tên công trình",
            select: function (e) {
                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.rpDailyTaskSearch.constructionName = dataItem.code;
                vm.rpDailyTaskSearch.constructionId = dataItem.constructionId;
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
                        return Restangular.all("constructionTaskService/rpDailyTaskConstruction").post({
                            keySearch: vm.rpDailyTaskSearch.constructionName,
                            catConstructionTypeId: vm.rpDailyTaskSearch.catConstructionType,
                            pageSize: vm.constructionOptions.pageSize,
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
			'<p class="col-md-6 text-header-auto border-right-ccc">Mã công trình</p>' +
			'<p class="col-md-6 text-header-auto">Tên công trình</p>' +
			'</div>',
            template: '<div class="row" ><div class="col-xs-5" style="word-wrap: break-word;float:left">#: data.code #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden"> #: data.name #</div> </div>',
            change: function (e) {
                if (!vm.isSelect) {
                    vm.rpDailyTaskSearch.constructionName = null;
                    vm.rpDailyTaskSearch.constructionId = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.rpDailyTaskSearch.constructionName = null;
                    vm.rpDailyTaskSearch.constructionId = null;
                }
            }
        }

        vm.workItemOptions = {
            dataTextField: "name",
            dataValueField: "workItemId",
			placeholder:"Nhập mã hoặc tên hạng mục",
            select: function (e) {
                vm.isSelect = true;
                var dataItem = this.dataItem(e.item.index());
                vm.rpDailyTaskSearch.workItemName = dataItem.code;
                vm.rpDailyTaskSearch.workItemId = dataItem.workItemId;
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
                        return Restangular.all("constructionTaskService/constructionTask/rpDailyTaskWorkItems").post({
                            keySearch: vm.rpDailyTaskSearch.workItemName,
                            pageSize: vm.workItemOptions.pageSize,
                            constructionId: vm.rpDailyTaskSearch.constructionId,
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
			'<p class="col-md-6 text-header-auto border-right-ccc">Mã hạng mục</p>' +
			'<p class="col-md-6 text-header-auto">Tên hạng mục</p>' +
			'</div>',
            template: '<div class="row" ><div class="col-xs-5" style="float:left;word-wrap: break-word;">#: data.code #</div><div  style="padding-left: 5px;word-wrap: break-word;width:auto;overflow: hidden">#: data.name # </div> </div>',
            change: function (e) {
                if (!vm.isSelect) {
                    vm.rpDailyTaskSearch.workItemName = null;
                    vm.rpDailyTaskSearch.workItemId = null;
                }
            },
            close: function (e) {
                if (!vm.isSelect) {
                    vm.rpDailyTaskSearch.workItemName = null;
                    vm.rpDailyTaskSearch.workItemId = null;
                }
            }
        }
        
        $(document).ready(function(){
        	kendo.ui.progress($("#constructionTaskGrid"), true);
        });
       function fillDataTable() {
		vm.constructionTaskGridOptions = kendoConfig.getGridOptions({
			autoBind: true,
			scrollable: true,
			resizable: true,
			editable: false,
			dataBinding: function () {
				record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
			},
			reorderable: true,
			dataSource: new kendo.data.DataSource({
				serverPaging: true,
				schema: {
					total: function (response) {
						$("#appCount").text("" + response.total);
						vm.count = response.total;
						if(vm.count){
							kendo.ui.progress($("#constructionTaskGrid"), false); 
						} else {
							kendo.ui.progress($("#constructionTaskGrid"), false);
						}
						return response.total;
					},
					data: function (response) {
						return response.data;
					},
				},
				transport: {
					read: {
						url: Constant.BASE_SERVICE_URL + "kpiLogMobileService/kpiLogMobile/rpDailyTask",
					    contentType: "application/json; charset=utf-8",
					    type: "POST",
					},
					parameterMap: function (options, type) {
						vm.rpDailyTaskSearch.page = options.page;
						vm.rpDailyTaskSearch.pageSize = options.pageSize;
						return JSON.stringify(vm.rpDailyTaskSearch);
					}
				},
				pageSize: 10 
			}),
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
					headerAttributes: { style: "text-align:center;font-weight: bold;" },
					attributes: {
						style: "text-align:center;"
					},
				}, {
					title: "Người thực hiện",
					field: 'fullname',
					width: '130px',
					headerAttributes: { style: "text-align:center;font-weight: bold;" },
					attributes: {
						style: "text-align:left;"
					},
				},{
					title: "SDT",
					field: 'phonenumber',
					width: '100px',
					headerAttributes: { style: "text-align:center;font-weight: bold;" },
					attributes: {
						style: "text-align:left;"
					},
				},	{
					title: "Email",
					field: 'email',
					width: '130px',
					headerAttributes: { style: "text-align:center;font-weight: bold;" },
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "Tỉnh",
					field: 'provincename',
					width: '100px',
					headerAttributes: { style: "text-align:center;font-weight: bold;" },
					attributes: {
						style: "text-align:left;"
					},
				}, {
					title: "Mã trạm",
					field: 'stationcode',
					width: '100px',
					headerAttributes: { style: "text-align:center;font-weight: bold;" },
					attributes: {
						style: "text-align:center;"
					},
				}, {
					title: "Mã công trình",
					field: 'constructionCode',
					width: '100px',
					headerAttributes: { style: "text-align:center;font-weight: bold;" },
					attributes: {
						style: "text-align:center;"
					},
				}, {
					title: "Loại công trình",
					field: 'constructiontypename',
					width: '110px',
					headerAttributes: { style: "text-align:center;font-weight: bold;" },
					attributes: {
						style: "text-align:left;"
					}
				}, {
					title: "Hạng mục",
					field: 'workItemName',
					width: '100px',
					headerAttributes: { style: "text-align:center;font-weight: bold;" },
					attributes: {
						style: "text-align:left;"
					}
				}, {
					title: "Ngày thực hiện",
					field: 'updateTime',
					width: '110px',
					headerAttributes: { style: "text-align:center;font-weight: bold;" },
					attributes: {
						style: "text-align:center;"
					},
				},{
					title: "Nội dung cập nhật",
					field: 'catTaskName',
					width: '200px',
					headerAttributes: { style: "text-align:center;font-weight: bold;" },
					attributes: {
						style: "text-align:left;"
					}
				}, {
					title: "Lũy kế thực hiện",
					field: 'luyKeThucHien',
					width: '130px',
					headerAttributes: { style: "text-align:center;font-weight: bold;" },
					attributes: {
						style: "text-align:center;"
					}
				}, {
					title: "Ghi chú",
					field: 'description',
					width: '150px',
					headerAttributes: { style: "text-align:center;font-weight: bold;" },
					attributes: {
						style: "text-align:left;"
					}
				}, {
					title: "Ảnh",
					type: 'text',
					width: '100px',
					editable: false,
					headerAttributes: { style: "text-align:center;font-weight: bold;" },
					template: function (dataItem) {
						var html = '<div class="text-center">' + 
							'<button ng-click="vm.showImage(dataItem)" style=" border: none; background-color: white;" id="updateId" class=" icon_table " translate>' +
							'<i class="fa fa-list-alt" style="color:#e0d014"     aria-hidden="true"></i>' +
							'</button>'
						html += '</div>';
						return html;
					},
				}
			]
		});
        }
        function showImage(dataItem) {
        	workItemService.getListImageById({tableId: dataItem.workItemId, tableName: 'KPI_LOG_MOBILE'}).then(function(data){
            	if (data.listImage.length > 0) {
                    vm.listImage = data.listImage;
                    vm.changeImage(vm.listImage[0]);
            	} else {
            		vm.listImage = [];
            	}
            }).catch(function (err) {
            	vm.listImage = [];
            });
            var templateUrl = "coms/popup/popup-images.html";
            var title = "Hình ảnh";
            var windowId = "DETAIL_WORKITEM_RPDAILYTASK";
            vm.popUpOpen = 'DETAIL_WORKITEM_RPDAILYTASK';
            CommonService.populatePopupCreate(templateUrl, title, data, vm, windowId, false, '1000', 'auto', "null");
        }
        
        function changeImage(image) {
        	vm.imageSelected = image;
		}

		function doSearch () {
			vm.constructionTaskGrid.dataSource.page(1);
		}
		
		function openUser() {
			var templateUrl = 'coms/popup/sysUserSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm nhân viên");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions, data, vm, 'fff', 'ggfd', false, '85%', '85%', 'sysUserSearchController');
		}
		
		function onSaveSysUser(data) {
			vm.rpDailyTaskSearch.userName = data.fullName;
			vm.rpDailyTaskSearch.sysuserid = data.sysUserId;
			htmlCommonService.dismissPopup();
			$("#signerGroup").focus();
		};
		
		function checkErrDate () {
			var startDate = $('#updateTime').val();
			vm.errMessage = '';
			vm.errMessage1 = '';
			var curDate = new Date();
			if (startDate == "") {
				vm.errMessage1 = CommonService.translate('Ngày tạo không được để trống');
				$("#updateTime").focus();
				vm.checkDateFrom = false;
				return vm.errMessage1;
			} else if (kendo.parseDate(startDate, "dd/MM/yyyy") == null) {
				vm.errMessage1 = CommonService.translate('Ngày tạo không hợp lệ');
				$("#updateTime").focus();
				vm.checkDateFrom = false;
				return vm.errMessage1;
			} else if (kendo.parseDate(startDate, "dd/MM/yyyy").getFullYear() > 9999 || kendo.parseDate(startDate, "dd/MM/yyyy").getFullYear() < 1000) {
				vm.errMessage1 = CommonService.translate('Ngày tạo không hợp lệ');
				$("#updateTime").focus();
				vm.checkDateFrom = false;
				return vm.errMessage1;
			}
		}
		
		function openConstruction(){
			var templateUrl = 'coms/popup/constructTypePopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm loại công trình");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions, null, vm, 'fff', 'ggfd', false, '85%','85%','constructTypeSearchController');
		}

		
		function clearUser () {
			vm.rpDailyTaskSearch.userName = null;
			vm.rpDailyTaskSearch.sysuserid = null;
			$("#signerGroup").focus();
		}
		
		function clearSearchDate () {
			vm.rpDailyTaskSearch.updateTime = null;
			$("#updateTime").focus();
		}
		
		function clearConstructionType (){
			vm.rpDailyTaskSearch.constructiontypename = null;
			vm.rpDailyTaskSearch.catConstructionType = null;
			$("#constructionType").focus();
		}
		
		function clearProvince (){
			vm.rpDailyTaskSearch.provincename = null;
			vm.rpDailyTaskSearch.provincecode = null;
			$("#provincename").focus();
		}
		
		function clearConstruction (){
			vm.rpDailyTaskSearch.constructionName = null;
			vm.rpDailyTaskSearch.constructionId = null;
			$("#constructionName").focus();
		}
		
		function clearWorkItem (){
			vm.rpDailyTaskSearch.workItemName = null;
			vm.rpDailyTaskSearch.workItemId = null;
			$("#workItemName").focus();
		}
		//HuyPQ-25/08/2018-edit-start
        function exportFile () {
        	function displayLoading(target) {
        	      var element = $(target);
        	      kendo.ui.progress(element, true);
        	      setTimeout(function(){

        	    	  delete vm.rpDailyTaskSearch.page;
        	        	delete vm.rpDailyTaskSearch.pageSize;
        	            return Restangular.all("kpiLogMobileService/kpiLogMobile/exportExcel").post(vm.rpDailyTaskSearch).then(function (d) {
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
        function openCatProvincePopup(){
    		var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm tỉnh");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
        }

        function onSaveCatProvince(data){
            vm.rpDailyTaskSearch.provincename = data.name;
            vm.rpDailyTaskSearch.provincecode = data.code;
            htmlCommonService.dismissPopup();
            $("#provincename").focus();
	    };

        function openCatConstructionTypePopup(){
        	var templateUrl = 'coms/popup/catConstructionTypeSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm loại công trình");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catConstructionTypeSearchController');
        }

        function onSaveCatConstructionType(data){
			vm.rpDailyTaskSearch.constructiontypename = data.name;
			vm.rpDailyTaskSearch.catConstructionType = data.catConstructionTypeId;
            htmlCommonService.dismissPopup();
            $("#constructionType").focus();
	    };

        function openComsConstructionPopup(){
        	var templateUrl = 'coms/popup/comsConstructionSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm công trình");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','comsConstructionSearchController');
        }

        function onSaveComsConstruction(data){
            vm.rpDailyTaskSearch.constructionName = data.code;
            vm.rpDailyTaskSearch.constructionId = data.constructionId;
            htmlCommonService.dismissPopup();
            $("#constructionName").focus();
	    };

        function openComsWorkItemPopup(){
        	var templateUrl = 'coms/popup/comsWorkItemSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm hạng mục");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','comsWorkItemSearchController');
        }

        function onSaveComsWorkItem(data){
            vm.rpDailyTaskSearch.workItemName = data.code;
            vm.rpDailyTaskSearch.workItemId = data.workItemId;
            htmlCommonService.dismissPopup();
            $("#workItemName").focus();
	    };

	}
})();
