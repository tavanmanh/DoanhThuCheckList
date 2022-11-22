(function () {
    'use strict';
    var controllerId = 'rpWo5sController';

    angular.module('MetronicApp').controller(controllerId, rpWo5sController);

    function rpWo5sController($scope, $rootScope, $timeout, gettextCatalog,
                              kendoConfig, $kWindow, htmlCommonService, rpWO5SService,
                              CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http) {
        var vm = this;
        vm.searchForm = {
            obstructedState: 1,
            type: 1
        };
        vm.d = {};
        vm.String = "Quản lý công trình > Báo cáo > Báo cáo 5s ";
        vm.resultExport = [];
        $scope.loggedInUser = $rootScope.casUser.userName;
        console.log($rootScope.casUser);
        // Khoi tao du lieu cho form
        initFormData();

        function initFormData() {

            getCdLv2List();
            var date = new Date();
            var start = new Date(date.getFullYear(), date.getMonth(), 1);
            var end = new Date(date.getFullYear(), date.getMonth() + 1, 0);
            vm.searchForm.endTimeStr = htmlCommonService.formatDate(end);
            vm.searchForm.startTimeStr = htmlCommonService.formatDate(start);
            fillDataTable();
            fillDataTableApproved();
            fillDataTableDetail();
        }

        // lay cd level 2
        $scope.autoCompleteCdLevel2Options = {
            dataTextField: "code", placeholder: "Chọn chi nhánh kỹ thuật",
            open: function (e) {
            },
            select: function (e) {
                data = this.dataItem(e.item.index());
                vm.searchForm.cdLevel2Name = data.groupName;
                vm.searchForm.cdLevel2 = data.sysGroupId;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function (options) {
                        var keySearch = vm.searchForm.cdLevel2Name;
                        if (keySearch == '') {
                            vm.searchForm.cdLevel2Name = null;
                            vm.searchForm.cdLevel2 = null;
                            return options.success([]);
                        }
                        vm.searchForm.cdLevel2 = null;
                        return options.success(searchCdLevel2(keySearch));
                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-12 text-header-auto">Chọn CD level 2</p></div>',
            template: '<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.groupName #</div></div>',
            change: function (e) {
            },
            close: function (e) {
                // handle the event0
            }
        };

        function searchCdLevel2(keySearch) {
            var searchResult = [];
            for (var i = 0; i < $scope.cdLv2List.length; i++) {
                var lv2 = $scope.cdLv2List[i];
                keySearch = keySearch.toUpperCase();
                var groupName = lv2.groupName.toUpperCase();
                if (groupName.includes(keySearch)) searchResult.push(lv2);
            }
            return searchResult;
        }

        function getCdLv2List() {
            console.log($scope);
            console.log($scope.loggedInUser);
            var postData = {loggedInUser: $scope.loggedInUser};

            if (vm.searchForm.cdLevel1 == $scope.vhktSysGroupId) {
                rpWO5SService.getVhktCdLv2VList(postData).then(
                    function (resp) {
                        if (resp.data) $scope.cdLv2List = resp.data;
                    },
                    function (error) {
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            } else {
                rpWO5SService.getCdLv2List(postData).then(
                    function (resp) {
                        if (resp.data) $scope.cdLv2List = resp.data;
                    },
                    function (error) {
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            }
        }


        vm.validatorOptions = kendoConfig.get('validatorOptions');
        setTimeout(function () {
            $("#keySearch").focus();
        }, 15);

        vm.listMonthPlan = [];

        vm.validatorOptions = kendoConfig.get('validatorOptions');
        setTimeout(function () {
            $("#keySearch").focus();
        }, 15);
        var record = 0;

        vm.doSearch = function () {
            var grid = vm.gridWo5s;
            var type = vm.searchForm.type;
            if (type == "1") {
                grid = vm.gridWo5s;
            } else if (type == "2") {
                grid = vm.gridWo5sApproved;
            } else if (type == "3") {
                grid = vm.gridWo5sDetail;
            }
            if (grid) {
            	grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                });
            }
        };

        function fillDataTable() {
            vm.gridWo5sOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save: function () {
                    vm.gridWo5s.dataSource.read();
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#countRPMonthProgressOS").text("" + response.total);
                            vm.count = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            if (!response || response.status == -1) toastr.error('Có lỗi xảy ra!');
                            else {
                                vm.resultExport = response.data;
                                return response.data;
                            }
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service lay danh sach bao cao
                            url: Constant.BASE_SERVICE_URL + "woService/doSearchReportWo5s",
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
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: true,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: ''
                    }
                },
                columns: [
                    {
                        title: "TT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '40px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }
                    , {
                        title: "Chi nhánh kĩ thuật",
                        width: '270px',
                        field: "cdLevel2Name",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return data.cdLevel2Name ? data.cdLevel2Name : '';
                        }
                    }
                    , {
                        title: "FT",
                        width: '150px',
                        field: "ftName",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return data.ftName ? data.ftName : '';
                        }
                    }
                    , {
                        title: "Tổng số WO",
                        width: '150px',
                        field: "totalRecord5s",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function (data) {
                            //return (data.countDone +data.countDoneOver + data.countNotDone + data.countNotDoneOver ) ;
                            return data.totalRecord5s ? data.totalRecord5s : 0;
                        }
                    }
                    , {
                        title: "Số WO hoàn thành đúng hạn",
                        width: '150px',
                        field: "countDone",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function (data) {
                            return data.countDone;
                        }
                    }
                    , {
                        title: "Số WO hoàn thành quá hạn",
                        width: '150px',
                        field: "countDoneOver",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function (data) {
                            return data.countDoneOver;
                        }
                    }
                    , {
                        title: "Số WO chưa hoàn thành trong hạn",
                        width: '150px',
                        field: "countNotDone",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function (data) {
                            return data.countNotDone;
                        }
                    },
                    {
                        title: "Số WO chưa hoàn thành quá hạn",
                        width: '150px',
                        field: "countNotDoneOver",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function (data) {
                            return data.countNotDoneOver;
                        }
                    },

                ]
            });
        }

        function fillDataTableApproved() {
            vm.gridWo5sOptionsApproved = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save: function () {
                    vm.gridWo5s.dataSource.read();
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#countRPMonthProgressOS").text("" + response.total);
                            vm.count = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            if (!response || response.status == -1) toastr.error('Có lỗi xảy ra!');
                            else {
                                vm.resultExport = response.data;
                                return response.data;
                            }
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service lay danh sach bao cao
                            url: Constant.BASE_SERVICE_URL + "woService/doSearchReportWo5s",
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
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: true,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: ''
                    }
                },
                columns: [
                    {
                        title: "TT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '20px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }, {
                        title: "Chi nhánh kĩ thuật",
                        width: '200px',
                        field: "departmentName",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return data.departmentName ? data.departmentName : '';
                        }
                    }, {
                        title: "Loại công trình",
                        width: '200px',
                        field: "consTypeName",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return data.consTypeName ? data.consTypeName : '';
                        }
                    }, {
                        title: "Tổng WO đã giao CNKT",
                        width: '100px',
                        field: "totalWo",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function (data) {
                            return data.totalWo ? data.totalWo : '';
                        }
                    }, {
                        title: "Thực hiện đúng hạn",
                        width: '100px',
                        field: "executeInDeadline",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function (data) {
                            return data.executeInDeadline ? data.executeInDeadline : '0';
                        }
                    }, {
                        title: "Tỉ lệ",
                        width: '100px',
                        field: "executeInDeadlineRatio",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return data.executeInDeadlineRatio ? data.executeInDeadlineRatio + '%' : '0.0%';
                        }
                    }, {
                        title: "Phê duyệt đúng hạn",
                        width: '100px',
                        field: "approvedInDeadline",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        template: function (data) {
                            return data.approvedInDeadline ? data.approvedInDeadline : '0';
                        }
                    }, {
                        title: "Tỉ lệ",
                        width: '100px',
                        field: "approvedInDeadlineRatio",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return data.approvedInDeadlineRatio ? data.approvedInDeadlineRatio + '%' : '0.0%';
                        }
                    }
                ]
            });
        }

        function fillDataTableDetail() {
            vm.gridWo5sOptionsDetail = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save: function () {
                    vm.gridWo5s.dataSource.read();
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            $("#countRPMonthProgressOS").text("" + response.total);
                            vm.count = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            if (!response || response.status == -1) toastr.error('Có lỗi xảy ra!');
                            else {
                                vm.resultExport = response.data;
                                return response.data;
                            }
                        },
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service lay danh sach bao cao
                            url: Constant.BASE_SERVICE_URL + "woService/doSearchReportWo5s",
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
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: true,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: ''
                    }
                },
                columns: [
                    {
                        title: "TT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '40px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        }
                    }, {
                        title: "Đơn vị",
                        width: '200px',
                        field: "departmentName",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return data.departmentName ? data.departmentName : '';
                        }
                    }, {
                        title: "Mã WO",
                        width: '200px',
                        field: "woCode",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return data.woCode ? data.woCode : '';
                        }
                    }, {
                        title: "Trạng thái",
                        width: '100px',
                        field: "woState",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return data.woState ? data.woState : '';
                        }
                    }, {
                        title: "Loại công trình",
                        width: '150px',
                        field: "consTypeName",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return data.consTypeName ? data.consTypeName : '';
                        }
                    }, {
                        title: "Ngày giao WO",
                        width: '150px',
                        field: "createdDateStr",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function (data) {
                            return data.createdDateStr ? data.createdDateStr : '';
                        }
                    }, {
                        title: "Ngày CNKT nhận WO",
                        width: '150px',
                        field: "receiveDateStr",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function (data) {
                            return data.receiveDateStr ? data.receiveDateStr : '';
                        }
                    }, {
                        title: "Tình trạng thực hiện (FT)",
                        width: '150px',
                        field: "executeState",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return data.executeState ? data.executeState : '';
                        }
                    }, {
                        title: "Tên nhân viên FT",
                        width: '150px',
                        field: "ftName",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return data.ftName ? data.ftName : '';
                        }
                    }, {
                        title: "Ngày FT nhận WO",
                        width: '150px',
                        field: "updateFtReceiveWoStr",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        template: function (data) {
                            return data.updateFtReceiveWoStr ? data.updateFtReceiveWoStr : '';
                        }
                    }, {
                        title: "Tình trạng phê duyệt (CD)",
                        width: '150px',
                        field: "approvedState",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return data.approvedState ? data.approvedState : '';
                        }
                    }, {
                        title: "Tên người phê duyệt",
                        width: '150px',
                        field: "userCdApproveWo",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return data.userCdApproveWo ? data.userCdApproveWo : '';
                        }
                    }, {
                        title: "Ngày phê duyệt",
                        width: '150px',
                        field: "updateCdApproveWoStr",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        template: function (data) {
                            return data.updateCdApproveWoStr ? data.updateCdApproveWoStr : '';
                        }
                    }
                ]
            });
        }

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };


        vm.exportexcel = function () {
            $http({
                url: Constant.BASE_SERVICE_URL + "woService/exportFileReport5s",
                method: "POST",
                data: vm.resultExport,
                headers: {
                    'Content-type': 'application/json'
                },
                responseType: 'arraybuffer'
            }).success(function (data, status, headers, config) {
                htmlCommonService.saveFile(data, "Ket_qua_bao_cao.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            }).error(function (data, status, headers, config) {
                toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
            });

        }

        vm.onSave = onSave;

        function onSave(data) {
            if (vm.departmentpopUp === 'dept') {
                vm.searchForm.sysGroupName = data.text;
                vm.searchForm.sysGroupId = data.id;
            }
        }


    }
})();
