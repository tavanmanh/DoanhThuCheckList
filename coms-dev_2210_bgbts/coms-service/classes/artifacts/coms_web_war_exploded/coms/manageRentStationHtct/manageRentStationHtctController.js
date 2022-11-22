(function() {
	'use strict';
	var controllerId = 'manageRentStationHtctController';

	angular.module('MetronicApp').controller(controllerId,
			manageRentStationHtctController);

	function manageRentStationHtctController($scope, $rootScope, $timeout,
			gettextCatalog, kendoConfig, $kWindow, manageRentStationHtctService,
			CommonService, PopupConst, Restangular, RestEndpoint, Constant,
			htmlCommonService) {
		var vm = this;
		vm.showSearch = true;
		vm.isCreateNew = false;
		vm.searchForm = {};
		vm.showDetail = false;
		vm.viewForm = {};
		vm.listImage = [];
		vm.imageSelected = null;
		vm.String = "Quản lý công trình > Quản lý giá trị công trình > Quản lý thuê mặt bằng trạm HTCT";

		initData();
		function initData(){
			fillDataTable([]);
		}
		
		vm.doSearch = doSearch;
		function doSearch() {
			vm.showDetail = false;
			var grid = vm.rentGroundTaskGrid;
			if (grid) {
				grid.dataSource.query({
					page : 1,
					pageSize : 10
				});
			}
		}

		vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.rentGroundTaskGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.rentGroundTaskGrid.showColumn(column);
			} else {
				vm.rentGroundTaskGrid.hideColumn(column);
			}

		}

		vm.gridColumnShowHideFilter = function(item) {

			return item.type == null || item.type !== 1;
		};

		var record = 0;
		function fillDataTable(data) {
			kendo.ui.progress($("#rentGroundTaskGrid"), true);
			vm.rentGroundTaskGridOptions = kendoConfig
					.getGridOptions({
						autoBind : true,
						scrollable : true,
						resizable : true,
						editable : false,
						dataBinding : function() {
							record = (this.dataSource.page() - 1)
									* this.dataSource.pageSize();
						},
						reorderable : true,
						toolbar : [ {
							name : "actions",
							template : '<div class=" pull-left ">'
									+ '</div>'
									+ '<div class="btn-group pull-right margin_top_button ">'
									+ '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: VNĐ</div>'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
									+ '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>'
									+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
									+ '<label ng-repeat="column in vm.rentGroundTaskGrid.columns.slice(1,vm.rentGroundTaskGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column)"> {{column.title}}'
									+ '</label>' + '</div></div>'
						} ],
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									$("#appCountRent").text(
											"" + response.total);
									vm.countRent = response.total;
									kendo.ui.progress($("#rentGroundTaskGrid"), false);
									return response.total;
								},
								data : function(response) {
									kendo.ui.progress($("#rentGroundTaskGrid"), false);
									return response.data;
								}
							},
							transport : {
								read : {
									// Thuc hien viec goi service
									url : Constant.BASE_SERVICE_URL
											+ "constructionTaskService/doSearchManageRent",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									vm.searchForm.page = options.page
									vm.searchForm.pageSize = options.pageSize
									return JSON.stringify(vm.searchForm)

								}
							},
							pageSize : 10,
						},
						noRecords : true,
						columnMenu : false,
						messages : {
							noRecords : gettextCatalog
									.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
						},
						pageable : {
							refresh : false,
							pageSizes : [ 10, 15, 20, 25 ],
							messages : {
								display : "{0}-{1} của {2} kết quả",
								itemsPerPage : "kết quả/trang",
								empty : "<div style='margin:5px'>Không có kết quả hiển thị</div>"
							}
						},
						columns : [

								{
									title : "TT",
									field : "stt",
									template : function() {
										return ++record;
									},
									width : '50px',
									columnMenu : false,
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
								},
								{
									title : "Thời gian hoàn thành",
									width : '100px',
									field : "completeDate",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
								},
								{
									title : "Đơn vị thực hiện",
									width : '150px',
									field : "sysGroupName",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
								},
								{
									title : "Mã tỉnh",
									width : '100px',
									field : "catProvinceCode",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
								},
								{
									title : "Mã trạm",
									width : '100px',
									field : "catStationCode",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
								}, 
								{
									title : "Người thực hiện",
									width : '100px',
									field : "performerName",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
								},
								{
									title : "Thời gian bắt đầu",
									width : '100px',
									field : "startDate",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
								},
								{
									title : "Thời gian kết thúc",
									width : '100px',
									field : "endDate",
									headerAttributes : {
										style : "text-align:center;font-weight: bold;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									type : 'text',
								},
								{
									title: "Thao tác",
									 headerAttributes: {style: "text-align:center;font-weight: bold;"},
							        template: dataItem =>
									'<div class="text-center">'
									 +'<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.view(dataItem)" class=" icon_table "'+
									 '   uib-tooltip="Xem chi tiết" translate>'+
									 '<i class="fa fa-eye" style="color:#e0d014"   aria-hidden="true"></i>'+
									 '</button>'
									+'</div>',
							        width: '100px'
								}
								]
					});
		}
		
		vm.provinceSearchOptions = {
		        dataTextField: "name",
		        dataValueField: "id",
				placeholder:"Nhập mã hoặc tên tỉnh",
		        select: function (e) {
		        		vm.isSelect = true;
			            var dataItem = this.dataItem(e.item.index());
			            vm.searchForm.catProvinceCode = dataItem.code;
						vm.searchForm.catProvinceName = dataItem.name;
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
		                    return Restangular.all("catProvinceServiceRest/catProvince/getProvinceByDomainInPopup").post({
		                        name: vm.searchForm.catProvinceName,
		                        pageSize: vm.provinceSearchOptions.pageSize,
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
		        		vm.searchForm.catProvinceCode = null;
						vm.searchForm.catProvinceName = null;
		            }
		        },
		        close: function (e) {
		            if (!vm.isSelect) {
		            	vm.searchForm.catProvinceCode = null;
						vm.searchForm.catProvinceName = null;
		            }
		        }
		    }
		
		vm.openCatProvinceSearch = openCatProvinceSearch;
		function openCatProvinceSearch(param){
			var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
			var title = gettextCatalog.getString("Tìm kiếm tỉnh");
			htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, param, 'ggfd', false, '75%','75%','catProvinceSearchController');
	    }
	    
		vm.onSaveCatProvinceSearch = onSaveCatProvinceSearch;
		function onSaveCatProvinceSearch(dataItem){
			vm.searchForm.catProvinceCode = dataItem.code;
			vm.searchForm.catProvinceName = dataItem.name;
	        htmlCommonService.dismissPopup();
	    };
		
	    //SysGroup
	    vm.selectedDept1 = false;
	    vm.deprtOptions1 = {
	        dataTextField: "name",
	        dataValueField: "id",
	    	placeholder:"Nhập mã hoặc tên đơn vị",
	        select: function (e) {
	            vm.selectedDept1 = true;
	            var dataItem = this.dataItem(e.item.index());
	            vm.searchForm.sysGroupName = dataItem.text;
	            vm.searchForm.sysGroupId = dataItem.id;
	            vm.sysGroupIdSearch = dataItem.id;
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
	                    return Restangular.all("departmentRsService/department/getForAutoCompleteDeptByDomain").post({
	                        name: vm.searchForm.sysGroupName,
	                        pageSize: vm.deprtOptions1.pageSize
	                    }).then(function (response) {
	                        options.success(response.data);
	                    }).catch(function (err) {
	                        console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
	                    });
	                }
	            }
	        },
	        template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.text #</div> </div>',
	        change: function (e) {
	            if (e.sender.value() === '') {
	                vm.searchForm.sysGroupName = null;
	                vm.searchForm.sysGroupId = null;
	                vm.sysGroupIdSearch = null;
	            }
	        },
	        ignoreCase: false
	    }
	    
	    vm.openDepartmentTo = openDepartmentTo
	    function openDepartmentTo(popUp) {
	        vm.obj = {};
	        vm.departmentpopUp = popUp;
	        var templateUrl = 'coms/popup/findDepartmentPopUp.html';
	        var title = gettextCatalog.getString("Tìm kiếm đơn vị");
	        htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, popUp, 'ggfd', false, '75%','75%','catSysGroupSearchController');
	    }

	    vm.onSave = onSave;
	    function onSave(data) {
	        if (vm.departmentpopUp === 'dept') {
	            vm.searchForm.sysGroupName = data.text;
	            vm.searchForm.sysGroupId = data.id;
	            vm.sysGroupIdSearch = data.id;
	            htmlCommonService.dismissPopup();
	        }
	    }

	    // clear data
	    vm.changeDataAuto = changeDataAuto
	    function changeDataAuto(id) {
	        switch (id) {
	            case 'dept':
	            {
	                if (processSearch(id, vm.selectedDept1)) {
	                    vm.searchForm.sysGroupId = null;
	                    vm.searchForm.sysGroupName = null;
	                    vm.selectedDept1 = false;
	                }
	                break;
	            }
	        }
	    }

	    
	    vm.clearInput = function(param) {
	    	if(param=='keySearch') {
	    		vm.searchForm.keySearch = null;
	    	}
	    	
	    	if(param=='catProvinceName') {
	    		vm.searchForm.catProvinceCode = null;
	    		vm.searchForm.catProvinceName = null;
	    	}
	    	
	    	if(param=='sysGroup') {
	    		vm.searchForm.sysGroupId = null;
                vm.searchForm.sysGroupName = null;
	    	}
	    	
	    	if(param=='date') {
	    		vm.searchForm.dateFrom = null;
                vm.searchForm.dateTo = null;
	    	}
	    }
	    
	    vm.view = view;
	    function view(dataItem){
			vm.viewForm = angular.copy(dataItem);
			var teamplateUrl = "coms/manageRentStationHtct/manageRentStationHtctPopup.html";
			var title = "Chi tiết thuê mặt bằng trạm";
			var windowId = "VIEW_RENT";
			manageRentStationHtctService.getListImageRentHtct(dataItem.constructionTaskId).then(function(data) {
				vm.listImage = data.plain();
				CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '85%', '85%', "deptAdd");
			});
	    }
	    
	    //Images
	    vm.changeImage = changeImage;
	    function changeImage(image) {
	        vm.imageSelected = image;
	    }

	    vm.removeImage = removeImage;
	    function removeImage(image, list){
	        list.splice(list.indexOf(image), 1);
	    }
	    
	    vm.downloadImage = downloadImage;
	    function downloadImage() {
	        var link = document.createElement('a');
	        var uri = 'data:image/jpeg;base64,' + vm.imageSelected.base64String;
	        if (typeof link.download === 'string') {
	            link.href = uri;
	            link.download = vm.imageSelected.name;

	            //Firefox requires the link to be in the body
	            document.body.appendChild(link);

	            //simulate click
	            link.click();

	            //remove the link when done
	            document.body.removeChild(link);
	        } else {
	            window.open(uri);
	        }
	    }
	    
	    vm.viewForm.listFile = [];
	    function readFile() {
	        var files = $("#aio_cm_add_file")[0].files;
	        var errors = "";

	        if (!files) {
	            errors += "Trình duyệt không hỗ trợ, vui lòng sử dụng Chrome hoặc Firefox!";
	        }

	        if (files.length > 1 || vm.viewForm.listFile.length > 1) {
	            toastr.error("Vượt quá số lượng file cho phép: 1");
	            $("#aio_cm_add_file").val("");
	            return;
	        }

	        if (files && files[0]) {
	            var file = files[0];

	            if ((/\.(pdf)$/i).test(file.name)) {
	                toUtilDto(file);
	            } else {
	                errors += file.name + ": Không hỗ trợ định dạng!";
	            }
	        }

	        // Handle errors
	        if (errors) {
	            toastr.error(errors);
	            $("#aio_cm_add_file").val("");
	        }

	        function toUtilDto(file) {
	            var reader = new FileReader();
	            reader.addEventListener("load", function () {
	                vm.viewForm.listFile = [];
	                vm.viewForm.listFile.push(toUtilObj(file.name, reader.result.substring(reader.result.indexOf(",") + 1)));
	                $scope.$apply();
	            });

	            reader.readAsDataURL(file);
	        }

	        function toUtilObj(name, base64String) {
	            return {
	                base64String: base64String,
	                name: name
	            }
	        }
	    }
	    
		// end controller
	}
})();