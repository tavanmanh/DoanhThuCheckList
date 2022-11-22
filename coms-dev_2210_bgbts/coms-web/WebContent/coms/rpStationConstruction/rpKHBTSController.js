(function() {
    'use strict';
    var controllerId = 'rpKHBTSController';

    angular.module('MetronicApp').controller(controllerId, rpKHBTSController,'$modal');

    function rpKHBTSController($scope, $rootScope, $timeout, gettextCatalog,
                                    kendoConfig, $kWindow,htmlCommonService,rpKHBTSService,
                                    CommonService, PopupConst, Restangular, RestEndpoint,Constant,$http,$modal) {
        var vm = this;
        vm.searchForm={
            typeBc: '1'
        };
        vm.searchFormDetail={

        };

        vm.String="Báo cáo > Báo cáo danh mục nhà trạm - công trình";

        initFormData();
        function initFormData() {
            fillDataTable();
        }
        vm.openCatProvincePopup = openCatProvincePopup;
        function openCatProvincePopup(){
    		var templateUrl = 'coms/popup/catProvinceSearchPopUp.html';
    		var title = gettextCatalog.getString("Tìm kiếm tỉnh");
    		htmlCommonService.populatePopup(templateUrl, title, vm.gridOptions,data, vm, 'fff', 'ggfd', false, '85%','85%','catProvinceSearchController');
        }

        vm.onSaveCatProvince = onSaveCatProvince;
        var catProvinceName;
        function onSaveCatProvince(data){
            vm.searchForm.catProvince = data.name;
            vm.searchForm.provinceCode = data.code;
            htmlCommonService.dismissPopup();
            $("#province").focus();
	    };
	    
	    vm.provinceOptions = {
	            dataTextField: "name",
	            dataValueField: "id",
	            placeholder: "Nhập mã hoặc tên tỉnh",
	            select: function (e) {
	                vm.selectedDept1 = true;
	                var dataItem = this.dataItem(e.item.index());
	                vm.searchForm.catProvince = dataItem.name;
	                vm.searchForm.provinceCode = dataItem.code;
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
	                            name: vm.searchForm.catProvince,
	                            pageSize: 10,
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
	                    vm.searchForm.catProvince = null;
	                    vm.searchForm.provinceCode = null;
	                }
	            },
	            ignoreCase: false
	        };

        vm.listMonthPlan=[];

        vm.validatorOptions = kendoConfig.get('validatorOptions');
        setTimeout(function(){
            $("#keySearch").focus();
        },15);
        var record=0;
        
        vm.doSearch = function(){
            var grid = vm.gridStationConstruction;
            if(grid){
                grid.dataSource.page(1);
            }
        };
        
        vm.doSearchDetail = function(){
            var grid =vm.gridStationConstructionDetail;
            if(grid){
                grid.dataSource.page(1);
            }
        };

        vm.viewDetail = viewDetail;
        function viewDetail(types,projectCode){
        	vm.searchFormDetail.typeBc = '1';
            vm.searchFormDetail.projectCode=projectCode;
            var templateUrl = "coms/rpStationConstruction/detailStation.html";
            var title = gettextCatalog.getString("Xem thống kê chi tiết trạm");
            CommonService.populatePopupCreate(templateUrl, title, null, vm, 'POPUP_DETAIL_STATION_CONSTRUCTION', false, '90%', '90%', "code");
            fillDataStationDetail();
        }
        
        
        function fillDataTable() {
            vm.gridStationConstructionOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
                dataSource:{
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                        	vm.countRecord=response.total;
                            $("#listdata").text(""+vm.countRecord);
                            return response.total;
                        },
                        data: function (response) {
                        	if(vm.searchForm.typeBc == '2') return response.data
                        	else return response.data[0].listStation;
                        },
                    },
                    transport: {
                        read: {
                          url: Constant.BASE_SERVICE_URL + "stationConstructionRsService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchForm.page = options.page
                            vm.searchForm.pageSize = options.pageSize
                            return JSON.stringify(vm.searchForm)
                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'></div>")
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
                columns : [
					{
						title: "TT",
                        field:"stt",
                        template: function () {
                            return ++record;
                        },
                        width: '100px',
                        columnMenu: false,
                        hidden: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        }
					},
					{
						title: "Thao tác",
						template: dataItem =>
							'<div class="text-center">'
							+ '<button style=" border: none; background-color: white;" id="updateId" ' +
							' ng-click="vm.viewDetail(vm.searchForm.typeBc,dataItem.projectCode)" class="btn-mofify icon_table "uib-tooltip="Chi tiết"' +
							' translate>' +
							'<i class="fa fa-list-alt icon-table" aria-hidden="true"></i>' +
							'</button>' +
							'</div>',
						width:'200px',
						field: "action",
						hidden: true
					},
					{
						title : "Khu vực",
						field : 'area',
						width : '200px',
						headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
                        attributes: {
                            style: "text-align:left;"
                        }
                        
					},
					{
						title: "Thông tin công trình",
				        headerAttributes: {
							style: "text-align:center; font-weight: bold;",
							translate:"",
						},
						attributes: {
							style: "text-align:center;"
						},
						width: "150px",
	    				columns:[
							{
								title : "CNKT",
								field : 'provinceCode',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:left;"
		                        },
		                        template: function(data){
		                            return data.provinceCode;
		                        },
		                        footerTemplate: function(item) {
		                            return kendo.toString("Tổng", "");
		                        },
							},
							
							{
								title : "Trạm VTNET",
								field : 'stationVtNetCode',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:left;"
		                        },
		                        footerTemplate: function(item) {
		                        	var data = vm.gridStationConstruction.dataSource.data();
	                                var sum = 0;
	                                for (var idx = 0; idx < data.length; idx++) {
	                                    if (idx == 0) {
	                                        item = data[idx];
	                                        sum =item.sumStationVtNet;
	                                        break;
	                                    }
	                                }
	                                return kendo.toString(sum, "");
	                             }
							},
							
							{
								title : "Trạm VCC",
								field : 'stationVccCode',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:left;"
		                        },
		                        footerTemplate: function(item) {
		                        	var data = vm.gridStationConstruction.dataSource.data();
	                                var sum = 0;
	                                for (var idx = 0; idx < data.length; idx++) {
	                                    if (idx == 0) {
	                                        item = data[idx];
	                                        sum =item.sumStationVcc;
	                                        break;
	                                    }
	                                }
	                                return kendo.toString(sum, "");
	                             },
							},

							{
								title : "Mã khác",
								field : 'otherCode',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
							},
							{
								title : "Mã dự án",
								field : 'projectCode',
								hidden: false,
								width : '200px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:left;"
		                        },
		                        footerTemplate: function(item) {
		                        	var data = vm.gridStationConstruction.dataSource.data();
	                                var sum = 0;
	                                for (var idx = 0; idx < data.length; idx++) {
	                                    if (idx == 0) {
	                                        item = data[idx];
	                                        sum =item.sumProject;
	                                        break;
	                                    }
	                                }
	                                return kendo.toString(sum, "");
	                             },
							},
							{
								title : "Tên dự án",
								field : 'projectName',
								hidden: false,
								width : '300px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
							},
							{
								title : "Tạo mã",
								field : 'sumTaoMa',
								width : '150px',
								hidden: true,
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
								footerTemplate: function(item) {
		                        	var data = vm.gridStationConstruction.dataSource.data();
	                                var sum = 0;
	                                for (var idx = 0; idx < data.length; idx++) {
	                                    if (idx == 0) {
	                                        item = data[idx];
	                                        sum =item.allTaoMa;
	                                        break;
	                                    }
	                                }
	                                return kendo.toString(sum, "");
	                             }
							},
							{
								title : "Thuê MB",
								field : 'sumThueMB',
								width : '150px',
								hidden: true,
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
								footerTemplate: function(item) {
		                        	var data = vm.gridStationConstruction.dataSource.data();
	                                var sum = 0;
	                                for (var idx = 0; idx < data.length; idx++) {
	                                    if (idx == 0) {
	                                        item = data[idx];
	                                        sum =item.allThueMB;
	                                        break;
	                                    }
	                                }
	                                return kendo.toString(sum, "");
	                             }
							},
							{
								title : "Khởi công",
								field : 'sumKC',
								width : '150px',
								hidden: true,
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
								footerTemplate: function(item) {
		                        	var data = vm.gridStationConstruction.dataSource.data();
	                                var sum = 0;
	                                for (var idx = 0; idx < data.length; idx++) {
	                                    if (idx == 0) {
	                                        item = data[idx];
	                                        sum =item.allKC;
	                                        break;
	                                    }
	                                }
	                                return kendo.toString(sum, "");
	                             }
							},
							{
								title : "Đồng bộ",
								field : 'sumDB',
								width : '150px',
								hidden: true,
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
								footerTemplate: function(item) {
		                        	var data = vm.gridStationConstruction.dataSource.data();
	                                var sum = 0;
	                                for (var idx = 0; idx < data.length; idx++) {
	                                    if (idx == 0) {
	                                        item = data[idx];
	                                        sum =item.allDB;
	                                        break;
	                                    }
	                                }
	                                return kendo.toString(sum, "");
	                             }
							},
							{
								title : "Phát sóng",
								field : 'sumPS',
								width : '150px',
								hidden: true,
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
								footerTemplate: function(item) {
		                        	var data = vm.gridStationConstruction.dataSource.data();
	                                var sum = 0;
	                                for (var idx = 0; idx < data.length; idx++) {
	                                    if (idx == 0) {
	                                        item = data[idx];
	                                        sum =item.allPS;
	                                        break;
	                                    }
	                                }
	                                return kendo.toString(sum, "");
	                             }
							},
							{
								title : "Lên dự toán",
								field : 'sumDT',
								width : '150px',
								hidden: true,
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:right;"
		                        },
								footerTemplate: function(item) {
		                        	var data = vm.gridStationConstruction.dataSource.data();
	                                var sum = 0;
	                                for (var idx = 0; idx < data.length; idx++) {
	                                    if (idx == 0) {
	                                        item = data[idx];
	                                        sum =item.allDT;
	                                        break;
	                                    }
	                                }
	                                return kendo.toString(sum, "");
	                             }
							},
							{
								title : "Loại trạm",
								field : 'stationType',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
							},
							{
								title : "Long",
								field : 'longitude',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:right;"
		                        }
							},
							{
								title : "Lat",
								field : 'latitude',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:right;"
		                        }
							}
						],
					},
					{
						title: "Thông tin Call of trạm",
				        headerAttributes: {
				        	style: "text-align:center; font-weight: bold;",
							translate:"",
						},
						attributes: {
							style: "text-align:center;"
						},
						width: "1230px",
						columns: [
							{
								title : "Mái đất",
								field : 'maiDat',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        },
							},
							{
								title : "Địa chỉ",
								field : 'address',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
							},
							{
		                        title: "Độ cao cột",
		                        field: 'doCaoCot',
		                        width: '100px',
		                        headerAttributes: {
		                            style: "text-align:center;white-space: normal;"
		                        },
		                        attributes: {
		                            style: "text-align:right;white-space: normal;"
		                        },
		                    },
		                    {
		                        title: "Loại cột",
		                        field: 'loaiCot',
		                        width: '150px',
		                        headerAttributes: {
		                            style: "text-align:center;white-space: normal;"
		                        },
		                        attributes: {
		                            style: "text-align:left;white-space: normal;"
		                        },
		                    },
		                    {
		                        title: "Móng co",
		                        field: 'mongCo',
		                        width: '100px',
		                        headerAttributes: {
		                            style: "text-align:center;white-space: normal;"
		                        },
		                        attributes: {
		                            style: "text-align:left;white-space: normal;"
		                        },
		                    },
							{
								title : "Phòng máy",
								field : 'phongMay',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }  
							},
							{
								title : "Tiếp địa",
								field : 'tiepDia',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }  
							},
							{
								title : "Điện AC",
								field : 'dienAC',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }  
							},
							{
								title : "Cột trồng mới",
								field : 'cotTrongMoi',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }  
							},
							{
								title : "Vận chuyển bộ",
								field : 'vanChuyenBo',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }  
							},
							{
								title : "Thuê nguồn",
								field : 'thueNguon',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
		                        
							},
							{
								title : "Gia cố đặc biệt",
								field : 'giaCoDacBiet',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
		                        
							},
						]
					},
					{
						title : "Thông tin TK-DT",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Đơn vị thiết kế",
								field : 'donviThietKe',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
		                        
							},
						
							{
								title : "Người thiết kế",
								field : 'nguoiThietKe',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
							},
							{
								title : "Có TK",
								field : 'coTK',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
		                        
							},
							{
								title : "Có dự toán",
								field : 'coDuToan',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
							}, 
							{
								title : "Thẩm",
								field : 'Tham',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
							}, 
							{
								title : "Bàn giao TTHT",
								field : 'bangiaoTTHT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        }
							}
						]
					},
					{
						title : "Tiến độ thi công",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Thuê mặt bằng",
								field : 'ngayThueMB',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
		                       
							},
							{
								title : "Khởi công",
								field : 'ngayKC',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        } 
							},
							{
								title : "Đồng bộ",
								field : 'ngayDB',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
		                        
							},
							{
								title : "Phát sóng",
								field : 'ngayPS',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
		                        
							},
							{
								title : "Vướng",
								field : 'Vuong',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
		                        
							},
							{
								title : "Nguyên nhân vướng",
								field : 'nguyenNhanVuong',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
		                        
							},
						]
					},
					{
						title : "Thông tin XHĐ",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Kì XHĐ lần 1",
								field : 'kyXHDLan1',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
							},
							{
								title : "Ngày DT trạm",
								field : 'ngayDTTram',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
							},
							{
								title : "Ngày DT nguồn",
								field : 'ngayDTNguon',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
							},
							{
								title : "Dừng XHĐ",
								field : 'dungXHD',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
							}

						]
					},
					{
						title : "Giá trị DT-CP",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "DT trạm",
								field : 'DTHaTang',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        }
							},
							{
								title : "DT nguồn",
								field : 'DTNguon',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        }
							},
							{
								title : "CP thuê MB",
								field : 'CPThueMB',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        }
							}
						]
					},{
						title : "Tiến độ HCQT",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Hồ sơ về TTHT",
								field : 'ngayTTHTPheDuyet',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
							},
							{
								title : "Hồ sơ về DTHT",
								field : 'ngayNhanHSHC',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
							},
							{
								title : "Hồ sơ về PTC",
								field : 'ngayTTDTHTDuyetWO',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
							},
							{
								title : "Giá trị QT",
								field : 'giatriQT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        }
							}
						]
					}
				]
            });
        }
        function numberWithCommas(x) {
            if(x == null || x == undefined){
                return '0';
            }
            var parts = x.toFixed(2).toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            return parts.join(".");
        }
        

        function fillDataStationDetail() {
            vm.gridStationConstructionDetail = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                },
                dataSource:{
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            return response.total;
                        },
                        data: function (response) {
                        	return response.data[0].listStation;
                        },
                    },
                    transport: {
                        read: {
                          url: Constant.BASE_SERVICE_URL + "stationConstructionRsService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.searchFormDetail.page = options.page
                            vm.searchFormDetail.pageSize = options.pageSize
                            return JSON.stringify(vm.searchFormDetail)
                        }
                    },
                    pageSize: 10
                },
                noRecords: true,
                columnMenu: false,
                messages: {
                    noRecords : gettextCatalog.getString("<div style='margin:5px'></div>")
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
                columns : [
					{
						title: "TT",
                        field:"stt",
                        template: function () {
                            return ++record;
                        },
                        width: '100px',
                        columnMenu: false,
                        hidden: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
                        attributes: {
                            style: "text-align:center;"
                        }
					},
					{
						title : "Khu vực",
						field : 'area',
						width : '200px',
						headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
                        attributes: {
                            style: "text-align:left;"
                        }
                        
					},
					{
						title: "Thông tin công trình",
				        headerAttributes: {
							style: "text-align:center; font-weight: bold;",
							translate:"",
						},
						attributes: {
							style: "text-align:center;"
						},
						width: "150px",
	    				columns:[
							{
								title : "CNKT",
								field : 'provinceCode',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:left;"
		                        },
		                        template: function(data){
		                            return data.provinceCode;
		                        },
		                        footerTemplate: function(item) {
		                            return kendo.toString("Tổng", "");
		                        },
							},
							
							{
								title : "Trạm VTNET",
								field : 'stationVtNetCode',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:left;"
		                        },
		                        footerTemplate: function(item) {
		                        	var data = vm.gridStationConstructionDetail.dataSource.data();
	                                var sum = 0;
	                                for (var idx = 0; idx < data.length; idx++) {
	                                    if (idx == 0) {
	                                        item = data[idx];
	                                        sum =item.sumStationVtNet;
	                                        break;
	                                    }
	                                }
	                                return kendo.toString(sum, "");
	                             }
							},
							
							{
								title : "Trạm VCC",
								field : 'stationVccCode',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:left;"
		                        },
		                        footerTemplate: function(item) {
		                        	var data = vm.gridStationConstructionDetail.dataSource.data();
	                                var sum = 0;
	                                for (var idx = 0; idx < data.length; idx++) {
	                                    if (idx == 0) {
	                                        item = data[idx];
	                                        sum =item.sumStationVcc;
	                                        break;
	                                    }
	                                }
	                                return kendo.toString(sum, "");
	                             },
							},

							{
								title : "Mã khác",
								field : 'otherCode',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
							},
							{
								title : "Tên dự án",
								field : 'projectName',
								width : '300px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
							},
							{
								title : "Loại trạm",
								field : 'stationType',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
							},
							{
								title : "Long",
								field : 'longitude',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:right;"
		                        }
							},
							{
								title : "Lat",
								field : 'latitude',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;",},
		                        attributes: {
		                            style: "text-align:right;"
		                        }
							}
						],
					},
					{
						title: "Thông tin Call of trạm",
				        headerAttributes: {
				        	style: "text-align:center; font-weight: bold;",
							translate:"",
						},
						attributes: {
							style: "text-align:center;"
						},
						width: "1230px",
						columns: [
							{
								title : "Mái đất",
								field : 'maiDat',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        },
							},
							{
								title : "Địa chỉ",
								field : 'address',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
							},
							{
		                        title: "Độ cao cột",
		                        field: 'doCaoCot',
		                        width: '100px',
		                        headerAttributes: {
		                            style: "text-align:center;white-space: normal;"
		                        },
		                        attributes: {
		                            style: "text-align:right;white-space: normal;"
		                        },
		                    },
		                    {
		                        title: "Loại cột",
		                        field: 'loaiCot',
		                        width: '150px',
		                        headerAttributes: {
		                            style: "text-align:center;white-space: normal;"
		                        },
		                        attributes: {
		                            style: "text-align:left;white-space: normal;"
		                        },
		                    },
		                    {
		                        title: "Móng co",
		                        field: 'mongCo',
		                        width: '100px',
		                        headerAttributes: {
		                            style: "text-align:center;white-space: normal;"
		                        },
		                        attributes: {
		                            style: "text-align:left;white-space: normal;"
		                        },
		                    },
							{
								title : "Phòng máy",
								field : 'phongMay',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }  
							},
							{
								title : "Tiếp địa",
								field : 'tiepDia',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }  
							},
							{
								title : "Điện AC",
								field : 'dienAC',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }  
							},
							{
								title : "Cột trồng mới",
								field : 'cotTrongMoi',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }  
							},
							{
								title : "Vận chuyển bộ",
								field : 'vanChuyenBo',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }  
							},
							{
								title : "Thuê nguồn",
								field : 'thueNguon',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
		                        
							},
							{
								title : "Gia cố đặc biệt",
								field : 'giaCoDacBiet',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
		                        
							},
						]
					},
					{
						title : "Thông tin TK-DT",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Đơn vị thiết kế",
								field : 'donviThietKe',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
		                        
							},
							{
								title : "Người thiết kế",
								field : 'nguoiThietKe',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
							},
							{
								title : "Có dự toán",
								field : 'coDuToan',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
							}, 
							{
								title : "Thẩm",
								field : 'Tham',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
							}, 
							{
								title : "Bàn giao TTHT",
								field : 'bangiaoTTHT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        }
							}
						]
					},
					{
						title : "Tiến độ thi công",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Thuê mặt bằng",
								field : 'ngayThueMB',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
		                       
							},
							{
								title : "Khởi công",
								field : 'ngayKC',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        } 
							},
							{
								title : "Đồng bộ",
								field : 'ngayDB',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
		                        
							},
							{
								title : "Phát sóng",
								field : 'ngayPS',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
		                        
							},
							{
								title : "Vướng",
								field : 'Vuong',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
		                        
							},
							{
								title : "Nguyên nhân vướng",
								field : 'nguyenNhanVuong',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
		                        
							},
						]
					},
					{
						title : "Tiến độ lên DT",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Kì XHĐ lần 1",
								field : 'kyXHDLan1',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:left;"
		                        }
							},
							{
								title : "Ngày DT trạm",
								field : 'ngayDTTram',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
							}
						]
					},
					{
						title : "Giá trị DT-CP",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "DT hạ tầng",
								field : 'DTHaTang',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        }
							},
							{
								title : "DT nguồn",
								field : 'DTNguon',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        }
							},
							{
								title : "CP thuê MB",
								field : 'CPThueMB',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        }
							}
						]
					},{
						title : "Tiến độ HCQT",
						width : '150px',
						headerAttributes : {
							style : "text-align:center;font-weight: bold;"
						},
						attributes : {
							style : "text-align:left;"
						},
						columns: [
							{
								title : "Hồ sơ về TTHT",
								field : 'ngayTTHTPheDuyet',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
							},
							{
								title : "Hồ sơ về DTHT",
								field : 'ngayNhanHSHC',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
							},
							{
								title : "Hồ sơ về PTC",
								field : 'ngayTTDTHTDuyetWO',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:center;"
		                        }
							},
							{
								title : "Giá trị QT",
								field : 'giatriQT',
								width : '150px',
								headerAttributes: {style: "text-align:center;font-weight: bold;white-space:normal;"},
		                        attributes: {
		                            style: "text-align:right;"
		                        }
							}
						]
					}
				]
            });
        }
        
        
        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };
        
        vm.cancelInputProvince = function() {
        	vm.searchForm.catProvince = null;
        	vm.searchForm.provinceCode =  null;
   	   }

        
        vm.exportExcel= function(){
	    	vm.searchForm.page = null;
			vm.searchForm.pageSize = null;
			kendo.ui.progress($(".tab-content"), true);
    		var obj = {};
    		obj = angular.copy(vm.searchForm);
    		
    		return Restangular.all("stationConstructionRsService/exportExcel").post(obj).then(function (d){
        	    var data = d.plain();
        	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	    kendo.ui.progress($(".tab-content"), false);
        	}).catch(function (e) {
        		kendo.ui.progress($(".tab-content"), false);
        	    toastr.error(gettextCatalog.getString("Lỗi khi tải báo cáo!"));
        	    return;
        	});
        }
        
        vm.changeTypeBc = function() {
        	var grid =$("#gridStationConstruction").data("kendoGrid"); 
        	var columns = grid.getOptions().columns; 
        	if(vm.searchForm.typeBc == '2'){
                columns.forEach(function(me){
                	if(me.columns == undefined ){
                		if(me.hidden == undefined ){
                        	grid.hideColumn(me.field);
                        }
                        else if(me.hidden == false ||  me.hidden == true){
                            grid.showColumn(me.field); 
                        }
                    }
                	else{
                		var columnsIn = me.columns; var count = 0;
                		columnsIn.forEach(function(record){
                			if(record.hidden == undefined ){
                				grid.hideColumn(record.field);
                				count++;
                			}
		                    else if(record.hidden == false ||  record.hidden == true){
		                    	grid.showColumn(record.field); 
		                    }
                			
                		});
                		if(count == columnsIn.length)	grid.hideColumn(me.title);
                	}
                }); 
//        		
        	} else {
        		columns.forEach(function(me){
                	if(me.columns == undefined ){
                		if(me.hidden == undefined ||  me.hidden == true){
                        	grid.showColumn(me.field);
                        }
                        else if(me.hidden == false ){
                            grid.hideColumn(me.field); 
                        }
                    }
                	else{
                		var columnsIn = me.columns; var count = 0;
                		columnsIn.forEach(function(record){
                			if(record.hidden == undefined ||  record.hidden == true){
                				grid.showColumn(record.field);
                				count++;
                			}
		                    else if(record.hidden == false){
		                    	grid.hideColumn(record.field); 
		                    }
                			
                		});
                		if(count == columnsIn.length)	grid.showColumn(me.title);
                	}
                }); 
        		grid.showColumn(0);
        	}
        	vm.doSearch();
        }
    }
})();
