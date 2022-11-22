(function () {
    'use strict';
    var controllerId = 'tcApproveRejectModalController';

    angular.module('MetronicApp').controller(controllerId, tcApproveRejectModalController);

    function tcApproveRejectModalController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                            kendoConfig, $kWindow, htmlCommonService, woManagementService, $modal,
                                            CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $compile) {

        var caller = this;
        var sRecord = 0;
        caller.tcTctEmails = [];

        init();

        function init() {
            fillSelectedWoDataTable();
            getTcTctEmails();
        }

        function getTcTctEmails() {
            woManagementService.getTcTctEmails().then(
                function (resp) {
                    if (resp) {
                        caller.tcTctEmails = resp.data;
                    }
                },
                function (error) {
                    toastr.error("Không thể kết nối để lấy dữ liệu!");
                }
            )
        }

        function fillSelectedWoDataTable() {
            console.log($scope.$parent.selectedWoList)
            caller.selectedWoListTableOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: true,
                dataBinding: function () {
                },
                save: function (e) {
                    var itemIndex = getItemIndex(e.model);
                    $scope.$apply(function () {
                        for (var p in e.values) {
                            $scope.$parent.selectedWoList[itemIndex][p] = e.values[p];
                        }
                    });
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    data: $scope.$parent.selectedWoList,
                    schema: {
                        model: {
                            id: "woId",
                            fields: {
                                text: {editable: true},
                                finishDate: {editable: false},
                                status: {editable: false},
                                moneyValue: {editable: false},
                                constructionCode: {editable: false},
                                woName: {editable: false},
                                woCode: {editable: false},
                                trCode: {editable: false},
                                apWorkSrc: {editable: false},
                                contractCode: {editable: false},
                                stationCode: {editable: false},
                                emailTcTct: {editable: true, type: "string"},
                            }
                        }
                    }
                },
                noRecords: true,
                columnMenu: false,
                messages: {},
                pageable: false,
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++sRecord;
                        },
                        width: '25px',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Mã Wo",
                        field: 'codeWo',
                        width: '120px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return dataItem.woCode ? dataItem.woCode : '';
                        },
                    },
                    {
                        title: "Hợp đồng",
                        field: 'contractCode',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            if (dataItem.hcqtContractCode) return dataItem.hcqtContractCode ? dataItem.hcqtContractCode : '';
                            else return dataItem.contractCode ? dataItem.contractCode : '';
                        }
                    },
                    {
                        title: "Công trình",
                        field: 'constructionCode',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Giá trị (Triệu VND)",
                        field: 'moneyValue',
                        width: '50px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            if (!dataItem.moneyValue || dataItem.moneyValue == 0) return 0;
                            var moneyM = dataItem.moneyValue / 1000000;
                            moneyM = moneyM.toFixed(3);
                            moneyM = CommonService.removeTrailingZero(moneyM);
                            return !!CommonService.numberWithCommas(moneyM) ? CommonService.numberWithCommas(moneyM) : 0;
                        }
                    },
                    {
                        title: "Hạn hoàn thành",
                        field: 'finishDate',
                        width: '50px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {style: "text-align:left;"},
                        type: 'text'
                    },
                    {
                        title: "Nhập lý do",
                        field: 'text',
                        editable: true,
                        width: '300px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        hidden: $scope.$parent.massModalType != 'reject',
                    },
                    {
                        title: "Chọn nhân viên TC TCT",
                        field: 'emailTcTct',
                        editable: true,
                        width: '150px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        hidden: $scope.$parent.massModalType == 'reject'
                            || ($scope.$parent.selectedWoList[0].state != 'WAIT_TC_BRANCH' && $scope.$parent.selectedWoList[0].state != 'TC_TCT_REJECTED'),
                        editor: function (container, options) {
                            $('<input required  data-bind="value:' + options.field + '"/>')
                                .appendTo(container)
                                .kendoDropDownList({
                                    index: 0,
                                    dataTextField: "code",
                                    dataValueField: "code",
                                    autoBind: true,
                                    dataSource: caller.tcTctEmails,
                                    value: options.field,
                                    valuePrimitive: true
                                });
                        }
                    },
                ],

            });
        }

        caller.setEmailTcTct = function (woId, value) {
            console.log(value);
            console.log(woId);
        }

        var getItemIndex = function (item) {
            var index = -1;
            var primaryKey = caller.selectedWoListTable.dataSource.options.schema.model.id;

            for (var i = 0; i < $scope.$parent.selectedWoList.length; i++) {
                if ($scope.$parent.selectedWoList[i][primaryKey] === item[primaryKey]) {
                    index = i;
                    break;
                }
            }
            return index;
        };

        caller.validateInput = function () {
            console.log($scope.$parent.selectedWoList)
            if ($scope.$parent.massModalType == 'reject') {
                for (var i = 0; i < $scope.$parent.selectedWoList.length; i++) {
                    if ($scope.$parent.selectedWoList[i].text == null || $scope.$parent.selectedWoList[i].text == '') {
                        toastr.error('Lý do từ chối không được để trống!')
                        return false;
                    }
                }
            } else if ($scope.$parent.workingWO.state == 'WAIT_TC_BRANCH') {
                for (var i = 0; i < $scope.$parent.selectedWoList.length; i++) {
                    if ($scope.$parent.selectedWoList[i].emailTcTct == null || $scope.$parent.selectedWoList[i].emailTcTct == '') {
                        toastr.error('Chưa chọn nhân viên Tài chính tổng công ty')
                        return false;
                    }
                }
            }
            return true;
        }

        // end controller
    }
})();
