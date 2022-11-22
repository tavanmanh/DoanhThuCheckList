(function() {
	'use strict';
	var controllerId = 'constructionCompleteManageController';
	
	angular.module('MetronicApp').controller(controllerId, constructionCompleteManageController);
	
	function constructionCompleteManageController($scope, $rootScope, $timeout, gettextCatalog, 
			kendoConfig, $kWindow,constructionCompleteManageService,$filter,
			CommonService, PopupConst, Restangular, RestEndpoint,Constant, htmlCommonService) {
		var vm = this;
		vm.showSearch = true;
		vm.isCreateNew = false;
        vm.showDetail = false;
        
        vm.searchForm = {};
        
        vm.typeList = [
        	{
        		id: 1,
        		name: "Thi công"
        	},
        	{
        		id: 2,
        		name: "Quỹ lương"
        	},
        	{
        		id: 3,
        		name: "Doanh thu"
        	}
        ];
        
        init();
        function init(){
        	fillDataTable([]);
        }
        
      //-----------------Hợp đồng--------------------//
	    vm.patternContractOutOptions={
	    		dataTextField: "code", 
	    		placeholder:"Mã hợp đồng",
	    		open: function(e) {
	    			vm.isSelect = false;
	    			
	    		},
	            select: function(e) {
	            	vm.isSelect = true;
	            	data = this.dataItem(e.item.index());
	            	vm.searchForm.cntContractCode = data.code;
	            },
	            pageSize: 10,
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function(options) {
	                    	var objSearch = {isSize: true, code:$('#cntContractOut').val(), contractType:0};
	                        return Restangular.all(RestEndpoint.CNT_CONTRACT_SERVICE_URL+"/cntContract/doSearch").post(objSearch).then(function(response){
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
	            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.code #</div></div>',
	            change: function(e) {	
	            	if (!vm.isSelect) {
	            		vm.searchForm.cntContractCode=null;
	                }
	            },
	            close: function (e) {
	                if (!vm.isSelect) {
	                	vm.searchForm.cntContractCode=null;
	                }
	            }
	    	};
	    
	    vm.openContractOut = function() {
	    	var templateUrl = 'coms/popup/findContractPopUp.html';
	    	var title = gettextCatalog.getString("Tìm kiếm hợp đồng");
	    	htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,null, vm, 'fff', 'ggfd', false, '85%','85%','contractSearchController');
	    }
	    
	    vm.onSaveContract = function(data) {
	    	vm.searchForm.cntContractCode = data.code;
	    }
	    
	  //------------------Đơn vị----------------//
		vm.selectedDept1 = false;
		vm.deprtOptions1 = {
		    dataTextField: "text",
		    dataValueField: "id",
			placeholder:"Nhập mã hoặc tên đơn vị",
		    select: function (e) {
		        vm.selectedDept1 = true;
		        var dataItem = this.dataItem(e.item.index());
		        vm.searchForm.sysGroupName = dataItem.text;
		        vm.searchForm.sysGroupId = dataItem.id;
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
		                    name: vm.searchForm.sysGroupName,
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
		            vm.searchForm.sysGroupName = null;// thành name
		            vm.searchForm.sysGroupId = null;
		        }
		    },
		    ignoreCase: false
		}
		
		vm.openDepartmentTo = openDepartmentTo
		function openDepartmentTo(popUp) {
		    vm.obj = {};
		    vm.departmentpopUp = popUp;
		    var templateUrl = 'coms/RPConstructionDK/findDepartmentPopUp.html';
		    var title = gettextCatalog.getString("Tìm kiếm đơn vị");
		    CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
		}
		
		vm.onSave = onSave;
		function onSave(data) {
		    if (vm.departmentpopUp === 'dept') {
		        vm.searchForm.sysGroupName = data.text;
		        vm.searchForm.sysGroupId = data.id;
		    }
		}
        
		//---------------------//
		
		vm.showHideColumn = function (column) {
		    if (angular.isUndefined(column.hidden)) {
		    	vm.completeManageGrid.hideColumn(column);
		    } else if (column.hidden) {
		        vm.completeManageGrid.showColumn(column);
		    } else {
		        vm.completeManageGrid.hideColumn(column);
		    }


		}
		
		vm.gridColumnShowHideFilter = function (item) {
		    return item.type == null || item.type !== 1;
		};
		
		var record = 0;
		function fillDataTable(data){
			vm.completeManageGridOptions = kendoConfig
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
					template : '<div class=" pull-left ">'+
		                    '<button class="btn btn-qlk padding-search-right padding-lable TkQLK width100"'+
		                    'ng-click="vm.importComplete()" uib-tooltip="Import hoàn thành" translate>Import hoàn thành</button>'+
		  					'</div>'
		    				+
							'<div class="btn-group pull-right margin_top_button ">'
							+ '<div class="btn-group margin_top_button unit_text"> Đơn vị tính: triệu VNĐ</div>'
							+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>'
							+ '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes workItemHide" role="menu">'
							+ '<label ng-repeat="column in vm.completeManageGrid.columns.slice(1,vm.completeManageGrid.columns.length)| filter: vm.gridColumnShowHideFilter">'
							+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideColumn(column)"> {{column.title}}'
							+ '</label>' + '</div></div>'
				} ],
				dataSource : {
					serverPaging : true,
					schema : {
						total : function(response) {
							$("#appCountComplete").text(
									"" + response.total);
							vm.countComplete = response.total;
							return response.total;
						},
						data : function(response) {
							return response.data;
						}
					},
					transport : {
						read : {
							// Thuc hien viec goi service
							url : Constant.BASE_SERVICE_URL
									+ "constructionTaskDailyService/doSearchCompleteManage",
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
				columns : [ {
					title : "TT",
					field : "stt",
					template : function() {
						return ++record;
					},
					width : '30px',
					headerAttributes : {
						style : "text-align:center;font-weight: bold;"
					},
					attributes : {
						style : "text-align:center;"
					},
					type : 'text',

				}, 
				{
					title : "Loại Import",
					field : 'typeImport',
					width : '120px',
					headerAttributes : {
						style : "text-align:center;font-weight: bold;"
					},
					attributes : {
						style : "text-align:center;"
					},
					template: function(dataItem){
						if(dataItem.typeImport==1){
							return "Thi công";
						} else if(dataItem.typeImport==2){
							return "Quỹ lương";
						} else if(dataItem.typeImport==3){
							return "Doanh thu";
						} else {
							return "";
						}
					}
				},
				{
					title : "Ngày thực hiện",
					field : 'updateDate',
					width : '120px',
					headerAttributes : {
						style : "text-align:center;font-weight: bold;"
					},
					attributes : {
						style : "text-align:center;"
					},

				},
				{
					title : "Đơn vị",
					field : 'sysGroupName',
					width : '300px',
					headerAttributes : {
						style : "text-align:center;font-weight: bold;"
					},
					attributes : {
						style : "text-align:center;"
					},

				},
				{
					title : "Mã trạm",
					width : '150px',
					field : 'catStationCode',
					headerAttributes : {
						style : "text-align:center;font-weight: bold;"
					},
					attributes : {
						style : "text-align:center;"
					},
					type : 'text',

				},
				{
					title : "Hợp đồng",
					width : '200px',
					field : 'cntContractCode',
					headerAttributes : {
						style : "text-align:center;font-weight: bold;"
					},
					attributes : {
						style : "text-align:center;"
					},
					type : 'text',

				}, 
				{
					title : "Công trình",
					width : '190px',
					field : 'constructionCode',
					headerAttributes : {
						style : "text-align:center;font-weight: bold;"
					},
					attributes : {
						style : "text-align:center;"
					},
					type : 'text',

				}, 
				{
					title : "Hạng mục",
					field : 'workItemName',
					width : '170px',
					headerAttributes : {
						style : "text-align:center;font-weight: bold;"
					},
					attributes : {
						style : "text-align:center;"
					},

				}, 
				{
					title : "Sản lượng",
					field : 'quantity',
					width : '120px',
					headerAttributes : {
						style : "text-align:center;font-weight: bold;"
					},
					attributes : {
						style : "text-align:center;"
					},

				},
				{
					title : "Người thực hiện",
					field : 'email',
					width : '200px',
					headerAttributes : {
						style : "text-align:center;font-weight: bold;"
					},
					attributes : {
						style : "text-align:center;"
					},

				},
				]
			});
		}
		
		
		vm.doSearch = doSearch;
		function doSearch(){
			var grid = vm.completeManageGrid;
			if(grid){
				grid.dataSource.query({
					page: 1,
					pageSize: 10
				});
			}
		}
		
		vm.exportFileCons = function() {
	    	vm.searchForm.page = null;
	    	vm.searchForm.pageSize = null;
			constructionCompleteManageService.doSearch(vm.searchForm).then(function(d){
				var list = d.data;
				for(var i=0;i<list.length;i++){
					if(list[i].typeImport==1){
						list[i].typeImport = "Thi công";
					} else if(list[i].typeImport==2){
						list[i].typeImport = "Quỹ lương";
					} else if(list[i].typeImport==3){
						list[i].typeImport = "Doanh thu";
					} else {
						list[i].typeImport = "";
					}
				}
				CommonService.exportFile(vm.completeManageGrid,d.data,[],[],"Quản lý hoàn thành công việc");
			});
		}
		
		vm.importComplete = function(){
			if(vm.searchForm.sysGroupName==null || $("#dept").val().trim()==""){
				toastr.warning("Chưa chọn đơn vị thi công");
				$("#dept").focus();
				return;
			}
			
			if(vm.searchForm.typeImport==null || $("#typeImport").val().trim()==""){
				toastr.warning("Chưa chọn loại Import");
				$("#typeImport").focus();
				return;
			}
			
			vm.popupName = 'CNTCONSTRUCTION';
			vm.fileImportData = false;
			 var teamplateUrl="coms/constructionCompleteManage/importConstruction.html";
			 var title="Import";
			 var windowId="IMPORT_CSTR";
			 CommonService.populatePopupCreate(teamplateUrl,title,null,vm,windowId,true,'1000','275');
		}
		
		vm.getExcelTemplate = function(){
				if(vm.searchForm.typeImport==1){
					var fileName="Import_Hoan_thanh_cong_viec_thang";
				} else {
					var fileName="Import_Hoan_thanh_cong_viec_thang_QL";
				}
				CommonService.downloadTemplate2(fileName).then(function(d) {
					data = d.plain();
					window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFile?" + data.fileName;
				}).catch( function(){
					toastr.error(gettextCatalog.getString("Lỗi khi export!"));
					return;
				});
        }
		
		vm.cancelInput = function(data){
			if(data=='dept'){
				vm.searchForm.sysGroupName = null;
				vm.searchForm.sysGroupId = null;
			}
			if(data=='cntContractCode'){
				vm.searchForm.cntContractCode = null;
			}
			if(data=='typeImport'){
				vm.searchForm.typeImport = null;
			}
		}
		
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
                url: Constant.BASE_SERVICE_URL + "constructionTaskDailyService/importCompleteWorkItem?folder=temp"
                	+"&sysGroupId="+vm.searchForm.sysGroupId
                	+"&sysGroupName="+vm.searchForm.sysGroupName
                	+"&typeImport="+vm.searchForm.typeImport,
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
                        $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
                        doSearch();
                    }
                    $scope.$apply();
                }
            });
        }
        
        vm.cancel = function(){
        	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }
        
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
        	constructionCompleteManageService.downloadErrorExcel(vm.objectErr).then(function (d) {
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
        
//end controller
}
})();
