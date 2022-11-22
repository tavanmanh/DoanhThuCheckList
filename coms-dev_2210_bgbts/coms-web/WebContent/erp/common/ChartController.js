(function() {
    'use strict';
    var controllerId = 'ChartController';
    angular.module('MetronicApp').filter('startFrom', function() {
        return function(input, start) {
            start = +start; // parse to int
            return input.slice(start);
        }


    });


    angular.module('MetronicApp').controller(controllerId, function($rootScope, $scope, $http, $timeout,$kWindow,$filter, CommonService, Constant,Restangular,RestEndpoint,gettextCatalog,kendoConfig,htmlCommonService) {

        var vm = this;
        vm.chartSearch={};
        var date = new Date();
        var start = new Date(date.getFullYear(), date.getMonth(), 1);
        var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
        vm.chartSearch.dateTo = htmlCommonService.formatDate(end);
        vm.chartSearch.dateFrom = htmlCommonService.formatDate(start);
        setTimeout(function(){
            doSearch();
        }, 1000);

        function chartComplete(data){
//            Restangular.all("constructionTaskService/" + 'getDataChart').post(vm.chartSearch).then(function (response) {
                var list=data;

                function getMaxOfArray() {
                    return Math.max.apply(null, list);
                }
                function generateChartData(){
                    var chartData =[];
                    for (var i = 0; i < list.length; i++ ) {
                        var startDate = list[i].startDateChart;
                        var quantityChartKhsl = list[i].quantityKhSl;
                        var quantityChartThsl = list[i].quantityThSl;
                        var quantityChartKhHc = list[i].quantityKhHc;
                        var quantityChartThHc = list[i].quantityThHc;
                        if(list[i].quantityThSl==null) {
                            var quantityChartThsl = 0;
                        }
                        if(list[i].quantityKhSl==null) {
                            var quantityChartKhsl = 0;
                        }
                        if(list[i].quantityKhHc==null) {
                            var quantityChartKhHc = 0;
                        }
                        if(list[i].quantityThHc==null) {
                            var quantityChartThHc = 0;
                        }
                        chartData.push({
                            "date": startDate,
                            "thuctesl": quantityChartThsl,
                            "chitieusl": quantityChartKhsl,
                            "thuctehc": quantityChartThHc,
                            "chitieuhc": quantityChartKhHc
                        });
                    }
                    return chartData;
                }

                var chart = AmCharts.makeChart("chartdiv", {

                    "type": "serial",
                    "dataDateFormat": "DD-MM-YYYY",
                    "theme": "light",
                    "legend": {
                        "useGraphSettings": true
                    },
                    "dataProvider": generateChartData(),
                    "valueAxes": [{
                        "integersOnly": true,
                        "title": "Triệu VND",
                        "titleColor": "gray",
                        "maximum": getMaxOfArray(),
                        "minimum": 0,
                        "axisAlpha": 0,
                        "dashLength": 5,
                        "gridCount": 10,
                        "position": "left",
                    }
                    ],
                    "startDuration": 0.5,
                    "graphs": [
                        {
                            "balloonText": "KH sản lượng ngày [[category]]: [[value]] Triệu VNĐ",
                            "bullet": "triangleUp",
                            "title": "KH sản lượng",
                            "valueField": "chitieusl",
                            "fillAlphas": 0,
                            "dashLength": 6

                        }, {
                            "balloonText": "Thực tế sản lượng ngày [[category]]: [[value]] Triệu VNĐ",
                            "bullet": "round",
                            "title": "Thực tế sản lượng",
                            "valueField": "thuctesl",
                            "fillAlphas": 0
                        },{
                            "balloonText": "KH hoàn công ngày [[category]]: [[value]] Triệu VNĐ",
                            "bullet": "triangleDown",
                            "title": "KH hoàn công",
                            "valueField": "chitieuhc",
                            "fillAlphas": 0,
                            "dashLength": 6
                        }, {
                            "balloonText": "Thực tế hoàn công ngày [[category]]: [[value]] Triệu VNĐ",
                            "bullet": "square",
                            "title": "Thực tế hoàn công",
                            "valueField": "thuctehc",
                            "fillAlphas": 0
                        }],
                    "balloon":{
                        "maxWidth":350
                    },
                    "chartCursor": {
                        "cursorAlpha": 0,
                        "zoomable": false
                    },
                    "categoryField": "date",
                    "categoryAxis": {
                        "gridPosition": "start",
                        "axisAlpha": 1,
                        "fillAlpha": 0.05,
                        "gridAlpha": 0
                    },
                    "export": {
                        "enabled": true,
                        "exportSelection": true,
                    }
                });
//            });
        }

        function chartCompleteAcc(data){
//            Restangular.all("constructionTaskService/" + 'getDataChartAcc').post(vm.chartSearch).then(function (response) {
                var list=data;

                function getMaxOfArrayAcc() {
                    return Math.max.apply(null, list);
                }
                function generateChartDataAcc(){
                    var chartData =[];

                    for (var i = 0; i < list.length; i++ ) {
                        var startDate = list[i].startDateChart;
                        var quantityChartKhsl = list[i].quantityKhSl;
                        var quantityChartThsl = list[i].quantityThSl;
                        var quantityChartKhHc = list[i].quantityKhHc;
                        var quantityChartThHc = list[i].quantityThHc;
                        if(list[i].quantityThSl==null) {
                            var quantityChartThsl = 0;
                        }
                        if(list[i].quantityKhSl==null) {
                            var quantityChartKhsl = 0;
                        }
                        if(list[i].quantityKhHc==null) {
                            var quantityChartKhHc = 0;
                        }
                        if(list[i].quantityThHc==null) {
                            var quantityChartThHc = 0;
                        }
                        chartData.push({
                            "date": startDate,
                            "thuctesl": quantityChartThsl,
                            "chitieusl": quantityChartKhsl,
                            "thuctehc": quantityChartThHc,
                            "chitieuhc": quantityChartKhHc
                        });
                    }
                    return chartData;
                }
                var chart2 = AmCharts.makeChart("chartdiv2", {
                    "type": "serial",
                    "dataDateFormat": "DD-MM-YYYY",
                    "theme": "light",
                    "legend": {
                        "useGraphSettings": true
                    },
                    "dataProvider": generateChartDataAcc(),
                    "valueAxes": [{
                        "integersOnly": true,
                        "title": "Triệu VND",
                        "titleColor": "gray",
                        "maximum": getMaxOfArrayAcc(),
                        "minimum": 0,
                        "axisAlpha": 0,
                        "dashLength": 5,
                        "gridCount": 10,
                        "position": "left",
                    }
                    ],
                    "startDuration": 0.5,
                    "graphs": [
                        {
                            "balloonText": "KH sản lượng ngày [[category]]: [[value]] Triệu VNĐ",
                            "bullet": "triangleUp",
                            "title": "KH sản lượng",
                            "valueField": "chitieusl",
                            "fillAlphas": 0,
                            "dashLength": 6

                        }, {
                            "balloonText": "Thực tế sản lượng ngày [[category]]: [[value]] Triệu VNĐ",
                            "bullet": "round",
                            "title": "Thực tế sản lượng",
                            "valueField": "thuctesl",
                            "fillAlphas": 0
                        },{
                            "balloonText": "KH hoàn công ngày [[category]]: [[value]] Triệu VNĐ",
                            "bullet": "triangleDown",
                            "title": "KH hoàn công",
                            "valueField": "chitieuhc",
                            "fillAlphas": 0,
                            "dashLength": 6
                        }, {
                            "balloonText": "Thực tế hoàn công ngày [[category]]: [[value]] Triệu VNĐ",
                            "bullet": "square",
                            "title": "Thực tế hoàn công",
                            "valueField": "thuctehc",
                            "fillAlphas": 0
                        }],
                    "balloon":{
                        "maxWidth":350
                    },
                    "chartCursor": {
                        "cursorAlpha": 0,
                        "zoomable": false
                    },
                    "categoryField": "date",
                    "categoryAxis": {
                        "gridPosition": "start",
                        "axisAlpha": 1,
                        "fillAlpha": 0.05,
                        "gridAlpha": 0,
                    },
                    "export": {
                        "enabled": true,
                        "exportSelection": true,
                    }
                });
//            });
        }
        function chartReport(){
            Restangular.all("rpBTSService/" + 'getDataChartPxk7days').post(vm.chartSearch).then(function (response) {
                var list=response;

                function getMaxOfArrayAcc() {
                    return Math.max.apply(null, list);
                }

                function generateChartDataAcc(){
                    var chartData =[];

                    for (var i = 0; i < list.length; i++ ) {
                        var sysGroupCode = list[i].sysGroupCode;
                        var countConstruction = list[i].countConstruction;
                        chartData.push({
                            "sysGroupCode": sysGroupCode,
                            "countConstruction": countConstruction,
                        });
                    }
                    return chartData;
                }
                var chart2 = AmCharts.makeChart("chartdiv3", {
                    "type": "serial",
// "dataDateFormat": "DD-MM-YYYY",
                    "theme": "light",
                    "legend": {
                        "useGraphSettings": true
                    },
                    "dataProvider": generateChartDataAcc(),
                    "valueAxes": [{
                        "integersOnly": true, // hiển thị trục y
                        "reversed": false, // Giá trị tăng dần từ dưới lên trên
                        "title": "Số PXK",
                        "titleColor": "gray",
// "maximum": getMaxOfArrayAcc(),
// "minimum": 0,
                        "axisAlpha": 0,
                        "dashLength": 5,
                        "gridCount": 10,
                        "position": "left",
                    }
                    ],
                    "startDuration": 0.5,
                    "graphs": [
                        {
                            "balloonText": "Đơn vị [[category]]: [[value]] PXK quá hạn",
                            "bullet": "triangleUp",
                            "title": "PXK 7 ngày",
                            "valueField": "countConstruction",
                            "fillAlphas": 0,
// "dashLength": 6 //nét đứt đường bđ

                        }],
                    "balloon":{
                        "maxWidth":350
                    },
                    "chartCursor": {
                        "cursorAlpha": 0,
                        "zoomable": false
                    },
                    "categoryField": "sysGroupCode",
                    "categoryAxis": {
                        "gridPosition": "start",
                        "axisAlpha": 1,
                        "fillAlpha": 0.05,
                        "gridAlpha": 0,
                        "labelRotation": 45,
                        "fillColor": "#000000"
                    },
//                    "export": {
//                        "enabled": true,
//                        "exportSelection": true,
//                    }
                    "export": {
                    	  "enabled": true,
                    	  "menu": [ {
                    	    "class": "export-main",
                    	    "menu": [ {
                    	      "label": "Tải ảnh",
                    	      "menu": [ "PNG", "JPG", "SVG" ]
                    	    }, {
                    	      "label": "Tải file",
                    	      "menu": [ "CSV",  { "label": "XLSX",
                    	          "click": function () {
                    	              kendo.ui.progress($(".tab-content"), true);
                    	          	vm.chartSearch.page = null;
                    	          	vm.chartSearch.pageSize = null;
                    	          	return Restangular.all("rpBTSService/exportChartPxk7Days").post(vm.chartSearch).then(function (d) {
                    	          	    var data = d.plain();
                    	          	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                    	          	    kendo.ui.progress($(".tab-content"), false);
                    	          	}).catch(function (e) {
                    	          		kendo.ui.progress($(".tab-content"), false);
                    	          	    toastr.error(gettextCatalog.getString("Lỗi khi tải báo cáo!"));
                    	          	    return;
                    	          	});
                    	            } }, ]
                    	    } ]
                    	  } ]
                    	}
                });
            });
        }
        
        function chartReport2(){
            Restangular.all("rpBTSService/" + 'getDataChartVttb45days').post(vm.chartSearch).then(function (response) {
                var list=response;

                function getMaxOfArrayAcc() {
                    return Math.max.apply(null, list);
                }

                function generateChartDataAcc(){
                    var chartData =[];

                    for (var i = 0; i < list.length; i++ ) {
                        var sysGroupCode = list[i].sysGroupCode;
                        var countConstruction = list[i].countConstruction;
                        chartData.push({
                            "sysGroupCode": sysGroupCode,
                            "countConstruction": countConstruction,
                        });
                    }
                    return chartData;
                }
                var chart2 = AmCharts.makeChart("chartdiv4", {
                    "type": "serial",
// "dataDateFormat": "DD-MM-YYYY",
                    "theme": "light",
                    "legend": {
                        "useGraphSettings": true
                    },
                    "dataProvider": generateChartDataAcc(),
                    "valueAxes": [{
                        "integersOnly": true, // hiển thị trục y
                        "reversed": false, // Giá trị tăng dần từ dưới lên trên
                        "title": "Số VTTB",
                        "titleColor": "gray",
// "maximum": getMaxOfArrayAcc(),
// "minimum": 0,
                        "axisAlpha": 0,
                        "dashLength": 5,
                        "gridCount": 10,
                        "position": "left",
                    }
                    ],
                    "startDuration": 0.5,
                    "graphs": [
                        {
                            "balloonText": "Đơn vị [[category]]: [[value]] VTTB quá hạn",
                            "bullet": "triangleUp",
                            "title": "VTTB 45 ngày",
                            "valueField": "countConstruction",
                            "fillAlphas": 0,
// "dashLength": 6 //nét đứt đường bđ

                        }],
                    "balloon":{
                        "maxWidth":350
                    },
                    "chartCursor": {
                        "cursorAlpha": 0,
                        "zoomable": false
                    },
                    "categoryField": "sysGroupCode",
                    "categoryAxis": {
                        "gridPosition": "start",
                        "axisAlpha": 1,
                        "fillAlpha": 0.05,
                        "gridAlpha": 0,
                        "labelRotation": 45,
                        "fillColor": "#000000"
                    },
//                    "export": {
//                        "enabled": true,
//                        "exportSelection": true,
//                    }
                    "export": {
                  	  "enabled": true,
                  	  "menu": [ {
                  	    "class": "export-main",
                  	    "menu": [ {
                  	      "label": "Tải ảnh",
                  	      "menu": [ "PNG", "JPG", "SVG" ]
                  	    }, {
                  	      "label": "Tải file",
                  	      "menu": [ "CSV",  { "label": "XLSX",
                  	          "click": function () {
                  	              kendo.ui.progress($(".tab-content"), true);
                  	          	vm.chartSearch.page = null;
                  	          	vm.chartSearch.pageSize = null;
                  	          	return Restangular.all("rpBTSService/exportChartVttb45Days").post(vm.chartSearch).then(function (d) {
                  	          	    var data = d.plain();
                  	          	    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                  	          	    kendo.ui.progress($(".tab-content"), false);
                  	          	}).catch(function (e) {
                  	          		kendo.ui.progress($(".tab-content"), false);
                  	          	    toastr.error(gettextCatalog.getString("Lỗi khi tải báo cáo!"));
                  	          	    return;
                  	          	});
                  	            } }, ]
                  	    } ]
                  	  } ]
                  	}
                });
            });
        }
        vm.doSearch= doSearch;
        function doSearch(){
        	Restangular.all("constructionTaskService/" + 'getDataChart').post(vm.chartSearch).then(function (response) {
              chartComplete(response.lstDataChart);
              chartCompleteAcc(response.lstDataChartAcc);
        	});
//            chartReport();
//            chartReport2();
//            chartPieReport();
//            woBarReport();
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
                vm.chartSearch.sysGroupName = data.text;
                vm.chartSearch.sysGroupId = data.id;
            }
        }

        // clear data
        vm.changeDataAuto = changeDataAuto
        function changeDataAuto(id) {
            switch (id) {
                case 'dept':
                {
                    if (processSearch(id, vm.selectedDept1)) {
                        vm.chartSearch.sysGroupId = null;
                        vm.chartSearch.sysGroupName = null;
                        vm.selectedDept1 = false;
                    }
                    break;
                }
            }
        }

        vm.cancelInput = function (param) {
            if (param == 'dept') {
                vm.chartSearch.sysGroupId = null;
                vm.chartSearch.sysGroupName = null;
            }
        }

        vm.selectedDept1 = false;
        vm.deprtOptions1 = {
            dataTextField: "text",
            dataValueField: "id",
            placeholder:"Nhập mã hoặc tên đơn vị",
            select: function (e) {
                vm.selectedDept1 = true;
                var dataItem = this.dataItem(e.item.index());
                vm.chartSearch.sysGroupName = dataItem.text;
                vm.chartSearch.sysGroupId = dataItem.id;
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
                            name: vm.chartSearch.sysGroupName,
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
                    vm.chartSearch.sysGroupName = null;// thành name
                    vm.chartSearch.sysGroupId = null;
                }
            },
            ignoreCase: false
        }

        vm.cancelListYear= cancelListYear;
        function cancelListYear(){
            vm.chartSearch.dateTo = null;
            vm.chartSearch.dateFrom = null;
        }

        //Biểu đồ WO
            function chartPieReport(){
                console.log("chartPieReport");
                var param = {};
                const dateFrom = vm.chartSearch.dateFrom.split("/")
                const dateTo = vm.chartSearch.dateTo.split("/")
                param.fromDate = `${dateFrom[2]}-${dateFrom[1]}-${dateFrom[0]}`
                param.toDate = `${dateTo[2]}-${dateTo[1]}-${dateTo[0]}`
                param.page = 1;
                param.pageSize = 99999;
                param.byLowerCd = 1;
                param.areaId = vm.chartSearch.sysGroupId;
                console.log(param)

                let data = [];
                let list = [];
                let sumUnassign = 0;
                let sumAssignCd = 0;
                let sumAcceptCd = 0;
                let sumRejectCd = 0;
                let sumAssignFt = 0;
                let sumAcceptFt = 0;
                let sumRejectFt = 0;
                let sumProcessing = 0;
                let sumDone = 0;
                Restangular.all("woService/report/woGeneralReport").post(param).then(function (response) {
                    data = response.data;
                    data.forEach(value => {
                        sumUnassign += value.totalUnassign;
                        sumAssignCd += value.totalAssignCd;
                        sumAcceptCd += value.totalAcceptCd;
                        sumRejectCd += value.totalRejectCd;
                        sumAssignFt += value.totalAssignFt;
                        sumAcceptFt += value.totalAcceptFt;
                        sumRejectFt += value.totalRejectFt;
                        sumProcessing += value.totalProcessing;
                        sumDone += value.totalDone;

                        list = [{"title": "Mới tạo","value": sumUnassign}, {"title": "Giao CD","value": sumAssignCd},
                            {"title": "CD tiếp nhận","value": sumAcceptCd}, {"title": "CD từ chối","value": sumRejectCd},
                            {"title": "Giao FT","value": sumAssignFt}, {"title": "FT tiếp nhận","value": sumAcceptFt},
                            {"title": "FT từ chối","value": sumRejectFt}, {"title": "Đang thực hiện","value": sumProcessing},
                            {"title": "Hoàn thành","value": sumDone}]
                    })

                    var chart = AmCharts.makeChart("chartdivWo1", {
                        "type": "pie",
                        "theme": "light",
                        "path": "../assets/global/plugins/amcharts/ammap/images/",
                        "dataProvider": list,
                        "valueField": "value",
                        "titleField": "title",
                        "balloonText": "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>",
                        "angle": 0,
                        "export": {
                            "enabled": true
                        },
                        "legend": {
                            "position": "right",
                            "marginRight": 100,
                            "autoMargins": false
                        },
                        "labelsEnabled": false
                    });

                }).catch(function (err) {
                    console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                });
        }

            function woBarReport() {
                let data = [];
                let listData = [];
                var param = {};
                const dateFrom = vm.chartSearch.dateFrom.split("/")
                const dateTo = vm.chartSearch.dateTo.split("/")
                param.fromDate = `${dateFrom[2]}-${dateFrom[1]}-${dateFrom[0]}`
                param.toDate = `${dateTo[2]}-${dateTo[1]}-${dateTo[0]}`
                param.page = 1;
                param.pageSize = 99999;
                param.byLowerCd = 1;
                param.areaId = vm.chartSearch.sysGroupId;
                Restangular.all("woService/report/woGeneralReport").post(param).then(function (response) {
                    data = response.data;
                    data.forEach(value => {
                        listData.push({"title": value.geoArea, "value": value.totalDone})
                    })

                    var chart2 = AmCharts.makeChart("chartdivWo2", {
                        "type": "serial",
                        "theme": "light",

                        "dataProvider": listData,
                        "valueAxes": [{
                            "integersOnly": true, // hiển thị trục y
                            "reversed": false, // Giá trị tăng dần từ dưới lên trên
                            "axisAlpha": 0,
                            "dashLength": 5,
                            "gridCount": 10,
                            "position": "left",
                        }
                        ],
                        "startDuration": 0.5,
                        "graphs": [{
                            "balloonText": "Đã hoàn thành [[value]]",
                            "fillColorsField": "color",
                            "fillAlphas": 0.9,
                            "lineAlpha": 0.2,
                            "type": "column",
                            "valueField": "value",
                        }],
                        "balloon":{
                            "maxWidth":100
                        },
                        "chartCursor": {
                            "fullWidth": true,
                            "cursorAlpha": 0.1,
                        },
                        "categoryField": "title",
                        "categoryAxis": {
                            "gridPosition": "start",
                            "axisAlpha": 1,
                            "fillAlpha": 0.05,
                            "gridAlpha": 0,
                            "labelRotation": 45,
                        },
                        "export": {
                            "enabled": true,
                            "exportSelection": true,
                        },
                    });
                })
            }

            const genData = [
                {
                    "maTinh": "HNI",
                    "keHoachVal": 25,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "BGG",
                    "keHoachVal": 6,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "BNH",
                    "keHoachVal": 14,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "BKN",
                    "keHoachVal": 1,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "HBH",
                    "keHoachVal": 4,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "HNM",
                    "keHoachVal": 4,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "HYN",
                    "keHoachVal": 6,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "LSN",
                    "keHoachVal": 4,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "NDH",
                    "keHoachVal": 6,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "TBH",
                    "keHoachVal": 6,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "TNN",
                    "keHoachVal": 11,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "VPC",
                    "keHoachVal": 4,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "QNH",
                    "keHoachVal": 5,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "HPG",
                    "keHoachVal": 15,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "HDG",
                    "keHoachVal": 15,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "CBG",
                    "keHoachVal": 3,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "YBI",
                    "keHoachVal": 4,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "HGG",
                    "keHoachVal": 3,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "LCU",
                    "keHoachVal": 3,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "TQG",
                    "keHoachVal": 3,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "LCI",
                    "keHoachVal": 0,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "DBN",
                    "keHoachVal": 3,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "PTO",
                    "keHoachVal": 3,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "SLA",
                    "keHoachVal": 8,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "NBH",
                    "keHoachVal": 1,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "THA",
                    "keHoachVal": 18,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "NAN",
                    "keHoachVal": 8,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "HTH",
                    "keHoachVal": 1,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "QBH",
                    "keHoachVal": 6,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "BDH",
                    "keHoachVal": 10,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "NTN",
                    "keHoachVal": 3,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "BTN",
                    "keHoachVal": 5,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "DLK",
                    "keHoachVal": 5,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "DNG",
                    "keHoachVal": 11,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "GLI",
                    "keHoachVal": 5,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "KHA",
                    "keHoachVal": 3,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "LDG",
                    "keHoachVal": 7,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "QNI",
                    "keHoachVal": 13,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "QNM",
                    "keHoachVal": 4,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "QTI",
                    "keHoachVal": 1,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "KTM",
                    "keHoachVal": 7,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "TTH",
                    "keHoachVal": 6,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "PYN",
                    "keHoachVal": 4,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "DCN",
                    "keHoachVal": 0,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "AGG",
                    "keHoachVal": 8,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "BDG",
                    "keHoachVal": 22,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "HUG",
                    "keHoachVal": 4,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "BPC",
                    "keHoachVal": 6,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "BLU",
                    "keHoachVal": 0,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "BTE",
                    "keHoachVal": 10,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "VTU",
                    "keHoachVal": 15,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "DTP",
                    "keHoachVal": 0,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "CMU",
                    "keHoachVal": 4,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "KGG",
                    "keHoachVal": 4,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "CTO",
                    "keHoachVal": 8,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "DNI",
                    "keHoachVal": 25,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "HCM",
                    "keHoachVal": 37,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "LAN",
                    "keHoachVal": 12,
                    "thucHienVal": 15,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "TGG",
                    "keHoachVal": 10,
                    "thucHienVal": 5,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "TNH",
                    "keHoachVal": 3,
                    "thucHienVal": 10,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "TVH",
                    "keHoachVal": 3,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "STG",
                    "keHoachVal": 3,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                },
                {
                    "maTinh": "VLG",
                    "keHoachVal": 4,
                    "thucHienVal": 0,
                    "tyLeVal": 0
                }
            ]
            function chartCompleteThueMatBang(data){
    //            Restangular.all("constructionTaskService/" + 'getDataChart').post(vm.chartSearch).then(function (response) {
                var list = data;
                var length = 0;

                function getMaxOfArray() {
                    // return 30;
                    return Math.max.apply(null, list);
                }
                function generateChartData(){
                    var chartData =[];
                    for (var i = 0; i < list.length; i++ ) {
                        var tinh = list[i].maTinh;
                        var keHoachVal = list[i].keHoachVal;
                        var thucHienVal = list[i].thucHienVal;
                        if(list[i].thucHienVal==null) {
                            thucHienVal = 0;
                        }
                        if(list[i].keHoachVal==null) {
                            keHoachVal = 0;
                        }
                        chartData.push({
                            "tinh": tinh,
                            "thuctesl": thucHienVal,
                            "chitieusl": keHoachVal
                        });
                    }
                    length = chartData.length;
                    return chartData;
                }

                var chart = AmCharts.makeChart("chartDivThueMatBang", {

                    "type": "serial",
                    "dataDateFormat": "DD-MM-YYYY",
                    "theme": "light",
                    "legend": {
                        "useGraphSettings": true
                    },
                    "dataProvider": generateChartData(),
                    "valueAxes": [{
                        "integersOnly": true,
                        "title": "Triệu VND",
                        "titleColor": "gray",
                        "maximum": getMaxOfArray(),
                        "minimum": 0,
                        "axisAlpha": 0,
                        "dashLength": 5,
                        "gridCount": 10,
                        "position": "left",
                    }
                    ],
                    "startDuration": 0.5,
                    "graphs": [
                        {
                            "balloonText": "Kế hoạch",
                            "bullet": "round",
                            "title": "KH",
                            "valueField": "chitieusl",
                            "fillAlphas": 0,
                            "dashLength": 6,
                            "bulletColor": "#1f60ff",
                            "lineColor": "#1f60ff",
                            "lineThickness": 3
                        },
                        // {
                        //     "balloonText": "Thực hiện",
                        //     "bullet": "round",
                        //     "title": "TH",
                        //     "valueField": "thuctesl",
                        //     "fillAlphas": 0
                        // }
                        ],
                    "balloon":{
                        "maxWidth":500
                    },
                    "chartCursor": {
                        "cursorAlpha": 0,
                        "zoomable": false
                    },
                    "categoryField": "tinh",
                    "categoryAxis": {
                        "gridPosition": "start",
                        "axisAlpha": 1,
                        "fillAlpha": 0.05,
                        "labelRotation": -45,
                        "minorGridEnabled": true,
                        "autoGridCount": false,
                        "gridCount": length
                    },
                    "export": {
                        "enabled": true,
                        "exportSelection": true,
                    }
                });
    //            });
            }

        function chartCompleteKhoiCong(data){
            //            Restangular.all("constructionTaskService/" + 'getDataChart').post(vm.chartSearch).then(function (response) {
            var list = data;
            var length = 0;

            function getMaxOfArray() {
                // return 30;
                return Math.max.apply(null, list);
            }
            function generateChartData(){
                var chartData =[];
                for (var i = 0; i < list.length; i++ ) {
                    var tinh = list[i].maTinh;
                    var keHoachVal = list[i].keHoachVal;
                    var thucHienVal = list[i].thucHienVal;
                    if(list[i].thucHienVal==null) {
                        thucHienVal = 0;
                    }
                    if(list[i].keHoachVal==null) {
                        keHoachVal = 0;
                    }
                    chartData.push({
                        "tinh": tinh,
                        "thuctesl": thucHienVal,
                        "chitieusl": keHoachVal
                    });
                }
                length = chartData.length;
                return chartData;
            }

            var chart = AmCharts.makeChart("chartDivKhoiCong", {

                "type": "serial",
                "dataDateFormat": "DD-MM-YYYY",
                "theme": "light",
                "legend": {
                    "useGraphSettings": true
                },
                "dataProvider": generateChartData(),
                "valueAxes": [{
                    "integersOnly": true,
                    "title": "Triệu VND",
                    "titleColor": "gray",
                    "maximum": getMaxOfArray(),
                    "minimum": 0,
                    "axisAlpha": 0,
                    "dashLength": 5,
                    "gridCount": 10,
                    "position": "left",
                }
                ],
                "startDuration": 0.5,
                "graphs": [
                    {
                        "balloonText": "Kế hoạch",
                        "bullet": "round",
                        "title": "KH",
                        "valueField": "chitieusl",
                        "fillAlphas": 0,
                        "dashLength": 6,
                        "bulletColor": "#1f60ff",
                        "lineColor": "#1f60ff",
                        "lineThickness": 3
                    }, {
                        "balloonText": "Thực hiện",
                        "bullet": "round",
                        "title": "TH",
                        "valueField": "thuctesl",
                        "fillAlphas": 0,
                        "bulletColor": "#fd7d11",
                        "lineColor": "#fd7d11",
                        "lineThickness": 3
                    }],
                "balloon":{
                    "maxWidth":500
                },
                "chartCursor": {
                    "cursorAlpha": 0,
                    "zoomable": false
                },
                "categoryField": "tinh",
                "categoryAxis": {
                    "gridPosition": "start",
                    "axisAlpha": 1,
                    "fillAlpha": 0.05,
                    "labelRotation": -45,
                    "minorGridEnabled": true,
                    "autoGridCount": false,
                    "gridCount": length
                },
                "export": {
                    "enabled": true,
                    "exportSelection": true,
                }
            });
            //            });
        }

        function chartCompletePhatSong(data){
            //            Restangular.all("constructionTaskService/" + 'getDataChart').post(vm.chartSearch).then(function (response) {
            var list = data;
            var length = 0;

            function getMaxOfArray() {
                // return 30;
                return Math.max.apply(null, list);
            }
            function generateChartData(){
                var chartData =[];
                for (var i = 0; i < list.length; i++ ) {
                    var tinh = list[i].maTinh;
                    var keHoachVal = list[i].keHoachVal;
                    var thucHienVal = list[i].thucHienVal;
                    if(list[i].thucHienVal==null) {
                        thucHienVal = 0;
                    }
                    if(list[i].keHoachVal==null) {
                        keHoachVal = 0;
                    }
                    chartData.push({
                        "tinh": tinh,
                        "thuctesl": thucHienVal,
                        "chitieusl": keHoachVal
                    });
                }
                length = chartData.length;
                return chartData;
            }

            var chart = AmCharts.makeChart("chartDivPhatSong", {

                "type": "serial",
                "dataDateFormat": "DD-MM-YYYY",
                "theme": "light",
                "legend": {
                    "useGraphSettings": true
                },
                "dataProvider": generateChartData(),
                "valueAxes": [{
                    "integersOnly": true,
                    "title": "Triệu VND",
                    "titleColor": "gray",
                    "maximum": getMaxOfArray(),
                    "minimum": 0,
                    "axisAlpha": 0,
                    "dashLength": 5,
                    "gridCount": 10,
                    "position": "left",
                }
                ],
                "startDuration": 0.5,
                "graphs": [
                    {
                        "balloonText": "Kế hoạch",
                        "bullet": "round",
                        "title": "KH",
                        "valueField": "chitieusl",
                        "fillAlphas": 0,
                        "dashLength": 6,
                        "bulletColor": "#9237fc",
                        "lineColor": "#9237fc",
                        "lineThickness": 3

                    }, {
                        "balloonText": "Thực hiện",
                        "bullet": "round",
                        "title": "TH",
                        "valueField": "thuctesl",
                        "fillAlphas": 0,
                        "bulletColor": "#fd7d11",
                        "lineColor": "#fd7d11",
                        "lineThickness": 3
                    }],
                "balloon":{
                    "maxWidth":500
                },
                "chartCursor": {
                    "cursorAlpha": 0,
                    "zoomable": false
                },
                "categoryField": "tinh",
                "categoryAxis": {
                    "gridPosition": "start",
                    "axisAlpha": 1,
                    "fillAlpha": 0.05,
                    "labelRotation": -45,
                    "minorGridEnabled": true,
                    "autoGridCount": false,
                    "gridCount": length
                },
                "export": {
                    "enabled": true,
                    "exportSelection": true,
                }
            });
            //            });
        }

        function chartCompleteDongBo(data){
            //            Restangular.all("constructionTaskService/" + 'getDataChart').post(vm.chartSearch).then(function (response) {
            var list = data;
            var length = 0;

            function getMaxOfArray() {
                // return 30;
                return Math.max.apply(null, list);
            }
            function generateChartData(){
                var chartData =[];
                for (var i = 0; i < list.length; i++ ) {
                    var tinh = list[i].maTinh;
                    var keHoachVal = list[i].keHoachVal;
                    var thucHienVal = list[i].thucHienVal;
                    if(list[i].thucHienVal==null) {
                        thucHienVal = 0;
                    }
                    if(list[i].keHoachVal==null) {
                        keHoachVal = 0;
                    }
                    chartData.push({
                        "tinh": tinh,
                        "thuctesl": thucHienVal,
                        "chitieusl": keHoachVal
                    });
                }
                length = chartData.length;
                return chartData;
            }

            var chart = AmCharts.makeChart("chartDivDongBo", {

                "type": "serial",
                "dataDateFormat": "DD-MM-YYYY",
                "theme": "light",
                "legend": {
                    "useGraphSettings": true
                },
                "dataProvider": generateChartData(),
                "valueAxes": [{
                    "integersOnly": true,
                    "title": "Triệu VND",
                    "titleColor": "gray",
                    "maximum": getMaxOfArray(),
                    "minimum": 0,
                    "axisAlpha": 0,
                    "dashLength": 5,
                    "gridCount": 10,
                    "position": "left",
                }
                ],
                "startDuration": 0.5,
                "graphs": [
                    {
                        "balloonText": "Kế hoạch",
                        "bullet": "round",
                        "title": "KH",
                        "valueField": "chitieusl",
                        "fillAlphas": 0,
                        "dashLength": 6,
                        "bulletColor": "#9237fc",
                        "lineColor": "#9237fc",
                        "lineThickness": 3

                    }, {
                        "balloonText": "Thực hiện",
                        "bullet": "round",
                        "title": "TH",
                        "valueField": "thuctesl",
                        "fillAlphas": 0,
                        "bulletColor": "#fd7d11",
                        "lineColor": "#fd7d11",
                        "lineThickness": 3
                    }],
                "balloon":{
                    "maxWidth":500
                },
                "chartCursor": {
                    "cursorAlpha": 0,
                    "zoomable": false
                },
                "categoryField": "tinh",
                "categoryAxis": {
                    "gridPosition": "start",
                    "axisAlpha": 1,
                    "fillAlpha": 0.05,
                    "labelRotation": -45,
                    "minorGridEnabled": true,
                    "autoGridCount": false,
                    "gridCount": length
                },
                "export": {
                    "enabled": true,
                    "exportSelection": true,
                }
            });
            //            });
        }

        function chartCompleteHshc(data){
            //            Restangular.all("constructionTaskService/" + 'getDataChart').post(vm.chartSearch).then(function (response) {
            var list = data;
            var length = 0;

            function getMaxOfArray() {
                // return 30;
                return Math.max.apply(null, list);
            }
            function generateChartData(){
                var chartData =[];
                for (var i = 0; i < list.length; i++ ) {
                    var tinh = list[i].maTinh;
                    var keHoachVal = list[i].keHoachVal;
                    var thucHienVal = list[i].thucHienVal;
                    if(list[i].thucHienVal==null) {
                        thucHienVal = 0;
                    }
                    if(list[i].keHoachVal==null) {
                        keHoachVal = 0;
                    }
                    chartData.push({
                        "tinh": tinh,
                        "thuctesl": thucHienVal,
                        "chitieusl": keHoachVal
                    });
                }
                length = chartData.length;
                return chartData;
            }

            var chart = AmCharts.makeChart("chartDivHshc", {

                "type": "serial",
                "dataDateFormat": "DD-MM-YYYY",
                "theme": "light",
                "legend": {
                    "useGraphSettings": true
                },
                "dataProvider": generateChartData(),
                "valueAxes": [{
                    "integersOnly": true,
                    "title": "Triệu VND",
                    "titleColor": "gray",
                    "maximum": getMaxOfArray(),
                    "minimum": 0,
                    "axisAlpha": 0,
                    "dashLength": 5,
                    "gridCount": 10,
                    "position": "left",
                }
                ],
                "startDuration": 0.5,
                "graphs": [
                    {
                        "balloonText": "Kế hoạch",
                        "bullet": "round",
                        "title": "KH",
                        "valueField": "chitieusl",
                        "fillAlphas": 0,
                        "dashLength": 6,
                        "bulletColor": "#9237fc",
                        "lineColor": "#9237fc",
                        "lineThickness": 3

                    }, {
                        "balloonText": "Thực hiện",
                        "bullet": "round",
                        "title": "TH",
                        "valueField": "thuctesl",
                        "fillAlphas": 0,
                        "bulletColor": "#fd7d11",
                        "lineColor": "#fd7d11",
                        "lineThickness": 3
                    }],
                "balloon":{
                    "maxWidth":500
                },
                "chartCursor": {
                    "cursorAlpha": 0,
                    "zoomable": false
                },
                "categoryField": "tinh",
                "categoryAxis": {
                    "gridPosition": "start",
                    "axisAlpha": 1,
                    "fillAlpha": 0.05,
                    "labelRotation": -45,
                    "minorGridEnabled": true,
                    "autoGridCount": false,
                    "gridCount": length
                },
                "export": {
                    "enabled": true,
                    "exportSelection": true,
                }
            });
            //            });
        }
    });

})();