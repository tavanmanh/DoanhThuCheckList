(function () {
    'use strict';
    var controllerId = 'yearPlanOSController';

    angular.module('MetronicApp').controller(controllerId, yearPlanOSController);

    function yearPlanOSController($scope, $rootScope, $timeout, gettextCatalog,
                                kendoConfig, $kWindow, yearPlanOSService,
                                CommonService, PopupConst, Restangular, RestEndpoint, Constant, htmlCommonService) {
        var vm = this;
        vm.showSearch = true;
        vm.isCreateNew = false;
        vm.showDetail = false;
        vm.isDisabled = false;
        vm.yearPlanOSSearch = {
            status: 1
        };
        $scope.listCheck = [];
        vm.yearPlanDetailTemp = {};
        vm.String = "Quản lý công trình > Quản lý kế hoạch > Kế hoạch năm ngoài OS";
        vm.yearPlan = {};
        // Khoi tao du lieu cho form
        initFormData();
        function initFormData() {
            fillDataTable([]);
            fillYearPlanDetailTable();
            initDropDownList();
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
        }

        vm.validatorOptions = kendoConfig.get('validatorOptions');
        vm.formatAction = function (dataItem) {
            var template =
                '<div class="text-center #=yearPlanId#"">'
            template += '<button type="button"' +
            'class="btn btn-default padding-button box-shadow  #=yearPlanId#"' +
            'disble="" ng-click=vm.edit(#=yearPlanId#)>' +
            '<div class="action-button edit" uib-tooltip="Sửa" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
            '</button>' +
            '<button type="button"' +
            'class="btn btn-default padding-button box-shadow #=yearPlanId#"' +
            'ng-click=vm.send(#=yearPlanId#)>' +
            '<div class="action-button export" uib-tooltip="Gửi tài chính" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
            '</button>' +
            '<button type="button"' +
            'class="btn btn-default padding-button box-shadow #=yearPlanId#"' +
            'ng-click=vm.remove(#=yearPlanId#)>' +
            '<div class="action-button del" uib-tooltip="Xóa" translate>&nbsp;&nbsp;&nbsp;&nbsp;</div>' +
            '</button>'
            +
            '<button type="button" class="btn btn-default padding-button box-shadow #=yearPlanId#"' +
            'ng-click=vm.cancelUpgradeLta(#=yearPlanId#)>' +
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
                        'ng-click="vm.add()" uib-tooltip="Tạo mới" translate>Tạo mới</button>' +
                        '</div>'
                        +
                        '<div class="btn-group pull-right margin_top_button margin10">' +
                        '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Cài đặt</span><i class="fa fa-cog" aria-hidden="true"></i></i>' +
                        '<i id="xuatExcel" class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()"  aria-expanded="false"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>' +
                        '<div uib-tooltip="Ẩn/hiện cột" class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">' +
                        '<label ng-repeat="column in vm.yearPlanGrid.columns| filter: vm.gridColumnShowHideFilter">' +
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
                            var list = response.data;
                            return response.data; // data is returned in
                            // the "data" field of
                            // the response
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "yearPlanOSRsService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            // vm.yearPlanOSSearch.employeeId =
                            // Constant.user.srvUser.catEmployeeId;
                            vm.yearPlanOSSearch.page = options.page
                            vm.yearPlanOSSearch.pageSize = options.pageSize

                            return JSON.stringify(vm.yearPlanOSSearch)

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
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    }, {
                        title: "Mã kế hoạch",
                        field: 'code',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    }
                    , {
                        title: "Tên kế hoạch",
                        field: 'name',
                        width: '25%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    }, {
                        title: "Năm",
                        field: 'year',
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                    }, {
                        title: "Tình trạng ký CA",
                        field: 'signState',
                        width: '12%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: "# if(signState == 1){ #" + "#= 'Chưa trình ký' #" + "# } " + "else if (signState == 2) { # " + "#= 'Đang trình ký' #" + "#}" + "else if (signState == 3) { # " + "#= 'Đã ký duyệt' #" + "#} " + "else if (signState == 4) { # " + "#= 'Từ chối ký duyệt' #" + "#} #"
                    },

                    {
                        title: "Trạng thái",
                        field: 'status',
                        template: "# if(status == 0){ #" + "#= 'Hết hiệu lực' #" + "# } " + "else if (status == 1) { # " + "#= 'Hiệu lực' #" + "#} #",
                        width: '10%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                    }, {
                        title: "Thao tác",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        template: dataItem =>
                        '<div>'
                        + '<button  style=" border: none; background-color: white;" id="updateId" ng-click="vm.edit(dataItem)" class=" icon_table "' +
                        '   uib-tooltip="Xem chi tiết" translate>' +
                        '<i class="fa fa-list-alt" style="color:#e0d014"  ng-disabled="dataItem.status==0"   aria-hidden="true"></i>' +
                        '</button>'
                        + '<button style=" border: none; background-color: white;" id="updateId" ng-hide="dataItem.status==0" ng-click="vm.copy(dataItem)" class=" icon_table "' +
                        '   uib-tooltip="Sao chép" translate>' +
                        '<i class="fa fa-files-o"style="color: #337ab7;"  ng-hide="dataItem.status==0"   aria-hidden="true"></i>'
                        + '</button>'
                        + '<button style=" border: none; background-color: white;" id="sign" ng-hide="dataItem.status==0||dataItem.signState==3||dataItem.signState==4" ng-click="vm.updateRegistry(dataItem)" class=" icon_table"' +
                        '   uib-tooltip="Trình ký" translate>' +
                        '<i class="fa fa-arrow-up" style="color: #337ab7; ng-hide="dataItem.status==0||dataItem.signState==3||dataItem.signState==4"   aria-hidden="true"></i>'
                        + '</button>'
                        + '<button style=" border: none; background-color: white;" id="sign" ng-show="dataItem.signState==3||dataItem.signState==4" ng-click="vm.viewSignedDoc(dataItem)" class=" icon_table"' +
                        '   uib-tooltip="Xem văn bản đã ký" translate>' +
                        '<i class="fa fa-file-text-o"style="color: #337ab7; ng-show="dataItem.signState==3||dataItem.signState==4"   aria-hidden="true"></i>'
                        + '</button>'
                        + '<button style=" border: none; background-color: white;" id=""' +
                        'class=" icon_table" ng-click="vm.remove(dataItem)" ng-hide="dataItem.status==0"  uib-tooltip="Xóa" translate' + '>' +
                        '<i class="fa fa-trash" style="color: #337ab7;" ng-hide="dataItem.status==0"  aria-hidden="true"></i>' +
                        '</button>'
                        + '</div>',
                        width: '17%'
                    }
                ]
            });
        }

        vm.handleCheck = handleCheck;
        function handleCheck(dataItem) {
            if (dataItem.selected) {
                $scope.listCheck.push(dataItem);
            } else {
                for (var i = 0; i < $scope.listCheck.length; i++) {
                    if ($scope.listCheck[i].yearPlanId === dataItem.yearPlanId) {
                        $scope.listCheck.splice(i, 1);
                    }
                }
                $('[name="gridchkselectall"]').prop('checked', false);
            }
        }

        vm.chkSelectAll = chkSelectAll;
        $scope.checkSearch = false;
        function chkSelectAll() {
            var grid = vm.yearPlanGrid;
            chkSelectAllBase(vm.chkAll, grid);
            if (vm.chkAll) {
                if ($scope.checkSearch && $scope.dataSearch.length > 0) {
                    $scope.listCheck = $scope.dataSearch;
                } else {

                    CommonService.getallData("yearPlanOSRsService/doSearch", vm.yearPlanOSSearch).then(function (data) {
                        $scope.listCheck = data.plain().data;
                    })
                }
            } else {
                $scope.listCheck = [];
            }
        };
        function pushDataToYearPlanTable(data) {
            var grid = vm.yearPlanDetailGrid;
            if (grid) {
                grid.dataSource.data(data);
                grid.refresh();
            }
        }

        function fillYearPlanDetailTable() {
            vm.yearPlanDetailGridOptions = kendoConfig.getGridOptions({
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
                    pageSize: 12,
                    pageSizes: [12, 24, 36, 48],
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
                        '<td class="text-right">#= kendo.toString(kendo.parseFloat(aggregates.source.sum), "n3")  #</td>' +
                        '<td class="text-right">#= kendo.toString(kendo.parseFloat(aggregates.quantity.sum), "n3") #</td>' +
                        '<td class="text-right">#= kendo.toString(kendo.parseFloat(aggregates.complete.sum), "n3") #</td>' +
                        '<td class="text-right">#= kendo.toString(kendo.parseFloat(aggregates.revenue.sum), "n3") #</td>' +
                        '<td class="text-center"><button class="icon_table text-center" style=" margin-left:6px;border: none; background-color: white;" ng-click="vm.removeYearPlanPerSysGroup(#= value #)" uib-tooltip="Xóa" translate> <i style="color: darkblue" aria-hidden="true" class="fa fa-trash"></i>' +
                        '</button>',
                        groupHeaderAttributes: {style: "color:red;"}
                    },
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
                        editable: false
                    },
                    {
                        title: "Thời gian",
                        field: 'month',
                        width: '25%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        editable: false,
                        template: function (dataItem) {
                            return 'Tháng ' + dataItem.month + "/" + vm.yearPlan.year;
                        }

                    },
                    {
                        title: "Nguồn việc",
                        field: 'source',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        format: "{0:n3}",
                        aggregates: ["sum"],
                        footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
                    },
                    {
                        title: "Sản lượng",
                        field: 'quantity',
                        width: '15%',
                        format: "{0:n3}",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
                    },{
                        title: "Quỹ lương",
                        field: 'complete',
                        width: '15%',
                        format: "{0:n3}",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
                    },
                    {
                        title: "Doanh thu",
                        field: 'revenue',
                        width: '15%',
                        format: "{0:n3}",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        editable: true,
                        aggregates: ["sum"],
                        footerTemplate: '#=kendo.toString(kendo.parseFloat(sum), "n3")#'
                    },
                    {
                        title: "Thao tác",
                        template: dataItem =>
                        '<div class="text-center">'
                        +
                        '<button style=" border: none; background-color: white;" id="removeId"' +
                        'class=" icon_table" ng-click="vm.removeYearPlanDetail($event)" uib-tooltip="Xóa" translate' +
                        '>' +
                        '<i class="fa fa-trash" style="color: #337ab7;"  aria-hidden="true"></i>' +
                        '</button>'
                        + '</div>',
                        field: 'action',
                        width: '10%',
                    }
                ],
                dataSource: {
                    schema: {
                        model: {
                            id: "yearPlanId",
                            fields: {
                                stt: {editable: false, nullable: true},
                                month: {editable: false, nullable: true},
                                source: {editable: true, type: "number", nullable: true},
                                complete: {editable: true, type: "number", nullable: true},
                                revenue: {editable: true, type: "number", nullable: true},
                                quantity: {editable: true, type: "number", nullable: true},
                                action: {editable: false, nullable: true}
                            }
                        }
                    }, sort: {field: "month", dir: "asc", type: "number"},
                    group: {
                        field: "sysGroupId", aggregates: [
                            {field: "sysGroupName", aggregate: "count"},
                            {field: "source", aggregate: "sum"},
                            {field: "quantity", aggregate: "sum"},
                            {field: "complete", aggregate: "sum"},
                            {field: "revenue", aggregate: "sum"}
                        ]
                    },
                    aggregate: [
                        {field: "source", aggregate: "sum"},
                        {field: "quantity", aggregate: "sum"},
                        {field: "complete", aggregate: "sum"},
                        {field: "revenue", aggregate: "sum"}]
                },
                dataBound: function (e) {
                    var firstCell = e.element.find(".k-grouping-row td:first-child");
                    firstCell.attr("colspan", 3);
                }
            });


        }

        vm.listRemove = [{
            title: "Thao tác"
        }
        ]
        vm.listConvert = [{
            field: "status",
            data: {
                1: 'Hiệu lực',
                0: 'Hết hiệu lực'
            }
        }, {
            field: "signState",
            data: {
                1: 'Chưa trình ký',
                2: 'Đang trình ký',
                3: 'Đã ký duyệt',
                4: 'Từ chối ký duyệt'

            }
        }
        ]


        // vm.exportFile = function exportFile() {
        // var data = vm.yearPlanGrid.dataSource.data();
        // CommonService.exportFile(vm.yearPlanGrid, data, vm.listRemove,
        // vm.listConvert, "Danh sách tra cứu kế hoạch năm");
        // }

        // /xuat excell
        // vm.exportFile= function(){
        // return
        // Restangular.all("yearPlanOSRsService/exportYearPlan").post().then(function
        // (d) {
        // var data = d.plain();
        // window.location = Constant.BASE_SERVICE_URL +
        // "fileservice/downloadFileATTT?" + data.fileName;
        // }).catch(function (e) {
        // toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        // return;
        // });
        //
        // } yearPlanOSSearch
        vm.exportFile = function () {
        	function displayLoading(target) {
        	      var element = $(target);
        	      kendo.ui.progress(element, true);
        	      setTimeout(function(){
        	    	  
        	    	  return Restangular.all("yearPlanOSRsService/exportYearPlan").post(vm.yearPlanOSSearch).then(function (d) {
        	                var data = d.plain();
        	                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
        	                kendo.ui.progress(element, false);
        	            }).catch(function (e) {
        	            	kendo.ui.progress(element, false);
        	                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
        	                return;
        	            });
        		});
        			
        	  }
        		displayLoading("#yearPlanGrid");
        }

        vm.import = function () {
            var teamplateUrl = "coms/yearPlanOS/yearPlanOSPopup.html";
            var title = "Import từ file excel";
            var windowId = "YEAR_PLAN";
            CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '700', 'auto', "files");
        }

        vm.saveImportFile = saveImportFile;
        function saveImportFile(data) {
            if ($("#fileImport")[0].files[0] == null) {
                toastr.warning("Bạn chưa chọn file để import");
                return;
            }
            if ($("#fileImport")[0].files[0].name.split('.').pop() != 'xls' && $("#fileImport")[0].files[0].name.split('.').pop() != 'xlsx') {
                toastr.warning("Sai định dạng file");
                return;
            }
//          hungnx 20180618 start
        	$('#savepopupYear').prop('disabled', true);
        	htmlCommonService.showLoading('#loadingIpYear');
//          hungnx 20180618 end
            var formData = new FormData();
            formData.append('multipartFile', $('#fileImport')[0].files[0]);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "yearPlanDetailRsServiceRest/yearPlanDetail/importYearPlanDetail",
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if (data.length === 0) {
                        toastr.warning("File imp" +
                        "ort không có dữ liệu");
                        return;
                    } else if (data[data.length - 1].errorFilePath != null) {
                        window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data[data.length - 1].errorFilePath;
                        toastr.warning("File import không hợp lệ");
                        return;
                    }
                    else {
                        // add list import to grid table

                        var sysGroupName = '';
                        var detailList = [];
                        var yearPlanDetailGrid = vm.yearPlanDetailGrid;
                        if (yearPlanDetailGrid != undefined && yearPlanDetailGrid.dataSource != undefined) {
                            // detailList = yearPlanDetailGrid.dataSource._data;
                            // if (detailList != undefined && detailList.length
                            // > 0) {
                            // angular.forEach(data, function (importItem) {
                            // var isExist = true;
                            // for (var i = 0; i < detailList.length; i++) {
                            // var item = detailList[i];
                            // if (item.sysGroupId == importItem.sysGroupId &&
                            // item.month == importItem.month) {
                            // detailList[i] = importItem;
                            // isExist = false;
                            // }
                            // }
                            // if (isExist) {
                            // yearPlanDetailGrid.dataSource.add(importItem);
                            // }
                            // })
                            // } else {
                            yearPlanDetailGrid.dataSource.data(data);
                            // }
                            yearPlanDetailGrid.dataSource.sync();
                            yearPlanDetailGrid.refresh();
                        }
                    }
                    toastr.success("Import file thành công");
                    vm.cancelImport();
                }
            }).always(function(){
//              hungnx 20180618 start
              $('#savepopupYear').prop('disabled', false);
          	htmlCommonService.hideLoading('#loadingIpYear');
//          hungnx 20180618 end
          });
        }

        function refreshGrid(d) {
            var grid = vm.yearPlanGrid;
            if (grid) {
                grid.dataSource.data(d);
                grid.refresh();
            }
        }

        vm.add = add;
        function add() {
            var year = (new Date()).getFullYear();
            vm.yearPlan = {
                year: year
            }
            yearPlanOSService.getSequence({}).then(function (seq) {
                vm.yearPlan.code = (seq + 1) + '/KHN- CTCT/XL' + vm.yearPlan.year;
                vm.yearPlan.name = 'Kế hoạch thực hiện năm ' + vm.yearPlan.year;
            }, function (err) {

            });
            fillYearPlanDetailTable();

            vm.yearPlanDetailTemp = {}
            vm.showDetail = true;
            vm.String = "Quản lý công trình > Quản lý kế hoạch > Thêm mới kế hoạch năm ngoài OS";

        }

        vm.yearName = function () {
            vm.yearPlan.name = 'Kế hoạch thực hiện năm ' + vm.yearPlan.year;
            yearPlanOSService.getSequence({}).then(function (seq) {
                vm.yearPlan.code = seq + '/KHN- CTCT/XL' + vm.yearPlan.year

            }, function (err) {

            });
        }
        vm.edit = edit;
        function edit(dataItem) {
            vm.String = "Quản lý công trình > Quản lý kế hoạch > Chi tiết kế hoạch năm ngoài OS";
            vm.showDetail = true;
            vm.yearPlan = dataItem;
            vm.isDisabled = dataItem.signState != 1;
            yearPlanOSService.getYearPlanById(dataItem.yearPlanId).then(function (data) {
                vm.yearPlan = data;
                vm.yearPlanDetailList = data.detailList;
                refreshYearPlanDetailList(data.detailList);

            }, function (error) {
                toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
            });
        }

        vm.copy = copy;
        function copy(dataItem) {
            vm.String = "Quản lý công trình > Quản lý kế hoạch > Sao chép kế hoạch năm ngoài OS";
            vm.showDetail = true;
            vm.yearPlan = dataItem;
            yearPlanOSService.getYearPlanById(dataItem.yearPlanId).then(function (data) {
                vm.yearPlan = data;
                vm.yearPlanDetailList = data.detailList;
                refreshYearPlanDetailList(data.detailList);
                vm.yearPlan.yearPlanId = undefined;

            }, function (error) {
                toastr.error(gettextCatalog.getString("Có lỗi xảy ra"));
            });
        }

        function refreshYearPlanDetailList(data) {
            var grid = vm.yearPlanDetailGrid;
            if (grid) {
                grid.dataSource.data(data);
                grid.dataSource.page(1);
                grid.refresh();
            }
        }

        vm.removeYearPlanDetail = removeYearPlanDetail;
        function removeYearPlanDetail(e) {
            confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function () {
                toastr.success("Xóa thành công!");
                var grid = vm.yearPlanDetailGrid;
                var row = $(e.target).closest("tr");
                var dataItem = grid.dataItem(row);
                grid.removeRow(dataItem); // just gives alert message
                grid.dataSource.remove(dataItem); // removes it actually from
                // the grid
                grid.dataSource.sync();
                grid.refresh();
            });
        }

        vm.removeYearPlanPerSysGroup = removeYearPlanPerSysGroup;
        function removeYearPlanPerSysGroup(sysGroupId) {
            confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function () {
                toastr.success("Xóa thành công!");
                var yearPlanDetailGrid = vm.yearPlanDetailGrid;
                var listRemove = [];
                if (yearPlanDetailGrid != undefined && yearPlanDetailGrid.dataSource != undefined) {
                    var detailList = yearPlanDetailGrid.dataSource._data;
                    angular.forEach(detailList, function (dataItem) {
                        if (sysGroupId == dataItem.sysGroupId) {
                            listRemove.push(dataItem);
                        }
                    })
                }
                angular.forEach(listRemove, function (dataItem) {
                    yearPlanDetailGrid.removeRow(dataItem); // just gives alert
                                                            // message
                    yearPlanDetailGrid.dataSource.remove(dataItem); // removes
                                                                    // it
                                                                    // actually
                                                                    // from the
                                                                    // grid
                    yearPlanDetailGrid.dataSource.sync();
                    yearPlanDetailGrid.refresh();
                })
                yearPlanDetailGrid.dataSource.page(1);
            });
        }

        vm.addYearPlanDetailTemp = addYearPlanDetailTemp;
        function addYearPlanDetailTemp() {
            if (validateTemp()) {
                var grid = vm.yearPlanDetailGrid;
                if (grid) {
                    var detailList = grid.dataSource._data;
                    var valid = true;
                    angular.forEach(detailList, function (dataItem) {
                        if (vm.yearPlanDetailTemp.sysGroupId == dataItem.sysGroupId && vm.yearPlanDetailTemp.month == dataItem.month) {
                            valid = false;
                        }
                    })
                    if (valid) {
                        grid.dataSource.add(vm.yearPlanDetailTemp);
                        grid.dataSource.sync();
                        vm.yearPlanDetailTemp.quantity1 = null;
                        vm.yearPlanDetailTemp.source1 = null;
                        vm.yearPlanDetailTemp.complete1=null;
                        vm.yearPlanDetailTemp.revenue1 =null;

                        grid.refresh();
                    } else {
                        toastr.warning("Dữ liệu tháng " + vm.yearPlanDetailTemp.month + " của đơn vị " + vm.yearPlanDetailTemp.sysGroupName + " đã tồn tại!");
                    }
                }
            }

        }

        function validateTemp() {
            if (vm.yearPlanDetailTemp.sysGroupId == undefined) {
                toastr.warning("Bạn phải chọn đơn vị!");
                return false;
            }
            if (vm.yearPlanDetailTemp.source == undefined) {
                toastr.warning("Bạn phải nhập nguồn việc!");
                return false;
            }
            if (vm.yearPlanDetailTemp.quantity == undefined) {
                toastr.warning("Bạn phải nhập sản lượng!");
                return false;
            }
            if (vm.yearPlanDetailTemp.complete == undefined) {
                toastr.warning("Bạn phải nhập Quỹ lương");
                return false;
            }
            if (vm.yearPlanDetailTemp.revenue == undefined) {
                toastr.warning("Bạn phải nhập doanh thu");
                return false;
            }
            if (vm.yearPlanDetailTemp.month == undefined) {
                toastr.warning("Bạn phải chọn tháng");
                return false;
            }
            return true;
        }
//        hungnx 20180619 start
        vm.loadingSave = false;
//      hungnx 20180619 end
        vm.save = function () {
            if (validateYearDetail()) {
                var data = populateDataToSave();
//              hungnx 20180619 start
                vm.loadingSave = true;
//              hungnx 20180619 end
                if (data.yearPlanId == null || data.yearPlanId == '') {
                    yearPlanOSService.createYearPlan(data).then(function (result) {
                        if (result.error) {
                            $('#year').focus();
                            toastr.error(result.error);
//                          hungnx 20180619 start
                            vm.loadingSave = false;
//                          hungnx 20180619 end
                            return;
                        }
                        toastr.success("Thêm mới thành công!");
                        vm.cancel();
//                      hungnx 20180619 start
                        vm.loadingSave = false;
//                      hungnx 20180619 end
                    }, function (errResponse) {
                        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi thêm mới"));
//                      hungnx 20180619 start
                        vm.loadingSave = false;
//                      hungnx 20180619 end
                    });
                }
                else {
                    yearPlanOSService.updateYearPlan(data).then(function (result) {
                        if (result.error) {
                            $('#year').focus();
                            toastr.error(result.error);
//                          hungnx 20180619 start
                            vm.loadingSave = false;
//                          hungnx 20180619 end
                            return;
                        }
                        toastr.success("Chỉnh sửa thành công!");
                        vm.cancel();
//                      hungnx 20180619 start
                        vm.loadingSave = false;
//                      hungnx 20180619 end
                    }, function (errResponse) {
                        toastr.error(gettextCatalog.getString("Lỗi xuất hiện khi chỉnh sửa"));
//                      hungnx 20180619 start
                        vm.loadingSave = false;
//                      hungnx 20180619 end
                    });
                }
            }
        }
// validate mỗi đơn vị phải đủ 12 tháng
        function validateYearDetail() {
            var isValid = true;
            var sysGroupName = '';
            var detailList = [];
            var yearPlanDetailGrid = vm.yearPlanDetailGrid;
            if (yearPlanDetailGrid != undefined && yearPlanDetailGrid.dataSource != undefined) {
                detailList = yearPlanDetailGrid.dataSource._data;
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
                toastr.warning("Kế hoạch năm của đơn vị " + sysGroupName + " không đủ 12 tháng!");
            }
            if (vm.yearPlan.year == undefined || vm.yearPlan.year == '') {
                toastr.warning("Năm không được để trống!");
                return false;
            }
            if (vm.yearPlan.code == undefined || vm.yearPlan.code == '') {
                toastr.warning("Mã kế hoạch không được để trống!");
                return false;
            }
            if (vm.yearPlan.name == undefined || vm.yearPlan.name == '') {
                toastr.warning("Tên kế hoạch không được để trống!");
                return false;
            }
            return isValid;
        }

        function populateDataToSave() {
            var data = vm.yearPlan;
            var detailList = []
            var yearPlanDetailGrid = vm.yearPlanDetailGrid;
            if (yearPlanDetailGrid != undefined && yearPlanDetailGrid.dataSource != undefined)
                data.detailList = yearPlanDetailGrid.dataSource._data;
            return data;
        }

        vm.cancel = cancel;
        function cancel() {
            vm.isDisabled = false;
            vm.showDetail = false;
            vm.yearPlanDetailTemp = {}
            vm.yearPlan = {};
            vm.clearAll();
            vm.doSearch();
            vm.String = "Quản lý công trình > Quản lý kế hoạch > Kế hoạch năm ";
        }

        vm.clearAll = clearAll;
        function clearAll() {
            vm.yearPlanDetailTemp.complete1 = null;
            vm.yearPlanDetailTemp.revenue1 = null;
            vm.yearPlanDetailTemp.source1 = null;
            vm.yearPlanDetailTemp.quantity1 = null;
            vm.yearPlanDetailGrid.dataSource.data([]);
            vm.yearPlanDetailGrid.refresh();
        }

        vm.remove = remove;
        function remove(dataItem) {
            confirm('Bạn thật sự muốn xóa bản ghi đã chọn?', function () {
                yearPlanOSService.remove(dataItem).then(
                    function (d) {
                        toastr.success("Xóa bản ghi thành công!");
                        var sizePage = $("#yearPlanGrid").data("kendoGrid").dataSource.total();
                        var pageSize = $("#yearPlanGrid").data("kendoGrid").dataSource.pageSize();
                        if (sizePage % pageSize === 1) {
                            var currentPage = $("#yearPlanGrid").data("kendoGrid").dataSource.page();
                            if (currentPage > 1) {
                                $("#yearPlanGrid").data("kendoGrid").dataSource.page(currentPage - 1);
                            }
                        }
                        $("#yearPlanGrid").data('kendoGrid').dataSource.read();
                        $("#yearPlanGrid").data('kendoGrid').refresh();

                    }, function (errResponse) {
                        toastr.error("Lỗi không xóa được!");
                    });
            })
        }

        vm.cancelDoSearch = function () {
            vm.showDetail = false;
            vm.yearPlanOSSearch = {
                status: "1"
            };
            doSearch();
        }

        vm.doSearch = doSearch;
        function doSearch() {
            vm.showDetail = false;
            var grid = vm.yearPlanGrid;
            if (grid) {
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        }


        vm.showHideColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.yearPlanGrid.hideColumn(column);
            } else if (column.hidden) {
                vm.yearPlanGrid.showColumn(column);
            } else {
                vm.yearPlanGrid.hideColumn(column);
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
            yearPlanOSService.exportpdf(obj);
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
            var templateUrl = 'coms/popup/findDepartmentPopUp.html';
            var title = gettextCatalog.getString("Tìm kiếm đơn vị");
            CommonService.populatePopupDept(templateUrl, title, null, null, vm, popUp, 'string', false, '92%', '89%');
        }

        vm.onSave = onSave;
        function onSave(data) {
            if (vm.departmentpopUp === 'dept') {
                vm.yearPlanDetailTemp.sysGroupName = data.text;
                vm.yearPlanDetailTemp.sysGroupId = data.id;
            }
        }

        // clear data
        vm.changeDataAuto = changeDataAuto
        function changeDataAuto(id) {
            switch (id) {
                case 'dept':
                {
                    if (processSearch(id, vm.selectedDept1)) {
                        vm.yearPlanDetailTemp.sysGroupId = null;
                        vm.yearPlanDetailTemp.sysGroupName = null;
                        vm.selectedDept1 = false;
                    }
                    break;
                }
            }
        }

        vm.cancelInput = function (param) {
            if (param == 'dept') {
                vm.yearPlanDetailTemp.sysGroupId = null;
                vm.yearPlanDetailTemp.sysGroupName = null;
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
                vm.yearPlanDetailTemp.sysGroupName = dataItem.text;
                vm.yearPlanDetailTemp.sysGroupId = dataItem.id;
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
                            name: vm.yearPlanDetailTemp.sysGroupName,
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
                    vm.yearPlanDetailTemp.sysGroupName = null;// thành name
                    vm.yearPlanDetailTemp.sysGroupId = null;
                }
            },
            ignoreCase: false
        }
        vm.getExcelTemplate = function () {
            yearPlanOSService.downloadTemplate({}).then(function (d) {
                var data = d.plain();
                window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
            }).catch(function () {
                toastr.error(gettextCatalog.getString("Lỗi khi tải biểu mẫu!"));
                return;
            });
        }
        vm.cancelImport = cancelImport;
        function cancelImport() {
            CommonService.dismissPopup1();
        }

        vm.onSelectFile = onSelectFile;
        function onSelectFile() {
            if ($("#fileImport")[0].files[0].name.split('.').pop() != 'xls' && $("#fileImport")[0].files[0].name.split('.').pop() != 'xlsx') {
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
                if (104857600 < $("#fileImport")[0].files[0].size) {
                    toastr.warning("Dung lượng file vượt quá 100MB! ");
                    return;
                }
                $("#fileName")[0].innerHTML = $("#fileImport")[0].files[0].name
            }
        }

        vm.sendToSign = sendToSign;
        function sendToSign(dataItem) {
            if (dataItem.signState == 1 || dataItem.signState == 2) {
                var teamplateUrl = "coms/popup/SignVofficePopup.html";
                var title = "Trình ký kế hoạch năm";
                var windowId = "ORDER";
                vm.nameToSign = "Yêu cầu trình ký";
                var obj = angular.copy(dataItem);
                var listYearPlanId = [];
                listYearPlanId.push(dataItem.yearPlanId);
                obj.listId = listYearPlanId;
                obj.type = "10";
                obj.reportName = "KeHoachNam";
                obj.objectId = dataItem.yearPlanId;
                obj.objectCode = dataItem.code
                obj.year = dataItem.year
                obj.month = dataItem.month
                obj.createdBy = dataItem.createdUserId

                obj.listSignVoffice = [];
                var dataList = [];
                Restangular.all("yearPlanOSRsService/getAppParamByType").post('YEAR_PLAN_VOFFICE').then(function (data) {
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
                    toastr.error("Lỗi khi lấy dữ liệu người ký!")
                });


            } else {
                toastr.error("Kế hoạch không đúng trạng thái ký!")
            }
        }
        
        vm.updateRegistry = updateRegistry;
        function updateRegistry(dataItem) {
            confirm('Bạn thật sự muốn trình ký bản ghi đã chọn?', function () {
            	yearPlanOSService.updateRegistry(dataItem).then(
                    function (d) {
                        toastr.success("Trình ký bản ghi thành công!");
                        var sizePage = $("#yearPlanGrid").data("kendoGrid").dataSource.total();
                        var pageSize = $("#yearPlanGrid").data("kendoGrid").dataSource.pageSize();
                        if (sizePage % pageSize === 1) {
                            var currentPage = $("#yearPlanGrid").data("kendoGrid").dataSource.page();
                            if (currentPage > 1) {
                                $("#yearPlanGrid").data("kendoGrid").dataSource.page(currentPage - 1);
                            }
                        }
                        $("#yearPlanGrid").data('kendoGrid').dataSource.read();
                        $("#yearPlanGrid").data('kendoGrid').refresh();

                    }, function (errResponse) {
                        toastr.error("Lỗi không trình ký được!");
                    });
            })
        }
        
        vm.cancelSingState = cancelSingState;
        function cancelSingState() {
            vm.yearPlanOSSearch.signStateList = []
        }

        vm.viewSignedDoc = viewSignedDoc;
        function viewSignedDoc(dataItem) {
            var obj = {
                objectId: dataItem.yearPlanId,
                type: "10"
            }
            CommonService.viewSignedDoc(obj);
        }
    }
})();
