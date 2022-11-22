(function() {
    'use strict';
    var controllerId = 'rpDashboard';
    angular.module('MetronicApp').filter('startFrom', function() {
        return function(input, start) {
            start = +start; // parse to int
            return input.slice(start);
        }


    });


    angular.module('MetronicApp').controller(controllerId, function($rootScope, $scope, $http, $timeout,$kWindow,$filter, CommonService, Constant,Restangular,RestEndpoint,gettextCatalog,kendoConfig,htmlCommonService) {

        var vm = this;
        vm.chartRpSearch={};
        var date = new Date();
        var start = new Date(date.getFullYear(), date.getMonth(), 1);
        var end = new Date(date.getFullYear(), date.getMonth() +1, 0);
        vm.chartRpSearch.dateTo = htmlCommonService.formatDate(end);
        vm.chartRpSearch.dateFrom = htmlCommonService.formatDate(start);

        init();
        function init(){
        	doSearchRp();
        }

        vm.doSearchRp= doSearchRp;
        function doSearchRp(){
        	if(vm.chartRpSearch.dateFrom==null || vm.chartRpSearch.dateFrom==""){
        		toastr.warning("Chưa nhập Từ ngày ");
        		$("dateFromId").focus();
        		return;
        	}
        	
        	if(vm.chartRpSearch.dateTo==null || vm.chartRpSearch.dateTo==""){
        		toastr.warning("Chưa nhập đến ngày ");
        		$("dateToId").focus();
        		return;
        	}
            const chartTypes = ['THUE_MAT_BANG','KHOI_CONG','DONG_BO','PHAT_SONG','HSHC'];
            vm.chartRpSearch.woTypes = chartTypes;
//            Restangular.all("woService").customPOST('getWoDataForChart', vm.chartRpSearch).then(function (response) {
            Restangular.all("woService/" + 'getWoDataForChart').post(vm.chartRpSearch).then(function (response) {
                if(response && response.data && response.data.THUE_MAT_BANG) {
                    chartCompleteThueMatBang(response.data.THUE_MAT_BANG);
                }
                if(response && response.data && response.data.KHOI_CONG) {
                    chartCompleteKhoiCong(response.data.KHOI_CONG);
                }
                if(response && response.data && response.data.DONG_BO) {
                    chartCompleteDongBo(response.data.DONG_BO);
                }
                if(response && response.data && response.data.PHAT_SONG) {
                    chartCompletePhatSong(response.data.PHAT_SONG);
                }
                if(response && response.data && response.data.HSHC) {
                    chartCompleteHshc(response.data.HSHC);
                }
                if(response && response.data && response.data.HSHC) {
                    chartCompleteTienDoTrienKhai(response.data.HSHC, response.data);
                }
            });
        }

        function chartCompleteThueMatBang(data){
            //            Restangular.all("constructionTaskService/" + 'getDataChart').post(vm.chartRpSearch).then(function (response) {
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
            console.log("Make chart");
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
                    "title": "Trạm",
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
                        "balloonText": "Kế hoạch: [[value]]",
                        "bullet": "round",
                        "title": "KH",
                        "valueField": "chitieusl",
                        "fillAlphas": 0,
                        "dashLength": 6,
                        "bulletColor": "#1f60ff",
                        "lineColor": "#1f60ff",
                        "lineThickness": 3
                    },
                    {
                        "balloonText": "Thực hiện: [[value]]",
                        "bullet": "round",
                        "title": "TH",
                        "valueField": "thuctesl",
                        "fillAlphas": 0,
                        "bulletColor": "#fd7d11",
                        "lineColor": "#fd7d11",
                        "lineThickness": 3
                    }
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
            //            Restangular.all("constructionTaskService/" + 'getDataChart').post(vm.chartRpSearch).then(function (response) {
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
                    "title": "Trạm",
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
                        "balloonText": "Kế hoạch: [[value]]",
                        "bullet": "round",
                        "title": "KH",
                        "valueField": "chitieusl",
                        "fillAlphas": 0,
                        "dashLength": 6,
                        "bulletColor": "#1f60ff",
                        "lineColor": "#1f60ff",
                        "lineThickness": 3
                    }, {
                        "balloonText": "Thực hiện: [[value]]",
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
            //            Restangular.all("constructionTaskService/" + 'getDataChart').post(vm.chartRpSearch).then(function (response) {
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
                    "title": "Trạm",
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
                        "balloonText": "Kế hoạch: [[value]]",
                        "bullet": "round",
                        "title": "KH",
                        "valueField": "chitieusl",
                        "fillAlphas": 0,
                        "dashLength": 6,
                        "bulletColor": "#9237fc",
                        "lineColor": "#9237fc",
                        "lineThickness": 3

                    }, {
                        "balloonText": "Thực hiện: [[value]]",
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
            //            Restangular.all("constructionTaskService/" + 'getDataChart').post(vm.chartRpSearch).then(function (response) {
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
                    "title": "Trạm",
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
                        "balloonText": "Kế hoạch: [[value]]",
                        "bullet": "round",
                        "title": "KH",
                        "valueField": "chitieusl",
                        "fillAlphas": 0,
                        "dashLength": 6,
                        "bulletColor": "#9237fc",
                        "lineColor": "#9237fc",
                        "lineThickness": 3

                    }, {
                        "balloonText": "Thực hiện: [[value]]",
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
            //            Restangular.all("constructionTaskService/" + 'getDataChart').post(vm.chartRpSearch).then(function (response) {
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
                    "title": "Trạm",
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
                        "balloonText": "Kế hoạch: [[value]]",
                        "bullet": "round",
                        "title": "KH",
                        "valueField": "chitieusl",
                        "fillAlphas": 0,
                        "dashLength": 6,
                        "bulletColor": "#9237fc",
                        "lineColor": "#9237fc",
                        "lineThickness": 3

                    }, {
                        "balloonText": "Thực hiện: [[value]]",
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

        function chartCompleteTienDoTrienKhai(data, dataAll){
            //            Restangular.all("constructionTaskService/" + 'getDataChart').post(vm.chartRpSearch).then(function (response) {
            var list = data;
            var listAll = [];
            var length = 0;
            console.log(dataAll);
            function getMaxOfArray() {
                // return 30;
                return Math.max.apply(null, listAll.map(obj => {
                    if(obj.keHoachThang > obj.thucHienThang) {
                        return obj.keHoachThang;
                    } else {
                        return obj.thucHienThang;
                    }
                }));
            }

            function getMaxOfArrayPhanTram() {
                // return 30;
                return Math.max.apply(null, listAll.map(obj => obj.phanTramThang));
            }

            function generateChartData(){
                var dataMatBang = dataAll.THUE_MAT_BANG;
                var dataKhoiCong = dataAll.KHOI_CONG;
                var dataDongBo = dataAll.DONG_BO;
                var dataPhatSong = dataAll.PHAT_SONG;
                var dataHsHc = dataAll.HSHC;
                var chartData =[];
                var keHoachThangMatBang = 0;
                var thucHienThangMatBang = 0;
                var i = 0;
                for (i = 0; i < dataMatBang.length; i++ ) {
                    keHoachThangMatBang += dataMatBang[i].keHoachVal;
                    thucHienThangMatBang += dataMatBang[i].thucHienVal;
                }
                chartData.push({
                    title: 'Thuê mặt bằng',
                    keHoachThang: keHoachThangMatBang,
                    thucHienThang: thucHienThangMatBang,
                    phanTramThang: keHoachThangMatBang ? ((thucHienThangMatBang/keHoachThangMatBang) * 100).toFixed(3) : 0
                });

                var keHoachThangKhoiCong = 0;
                var thucHienThangKhoiCong = 0;
                i = 0;
                for (i = 0; i < dataKhoiCong.length; i++ ) {
                    keHoachThangKhoiCong += dataKhoiCong[i].keHoachVal;
                    thucHienThangKhoiCong += dataKhoiCong[i].thucHienVal;
                }
                chartData.push({
                    title: 'Khởi công',
                    keHoachThang: keHoachThangKhoiCong,
                    thucHienThang: thucHienThangKhoiCong,
                    phanTramThang: keHoachThangKhoiCong ? ((thucHienThangKhoiCong/keHoachThangKhoiCong) * 100).toFixed(3) : 0
                });

                var keHoachThangDongBo = 0;
                var thucHienThangDongBo = 0;
                i = 0;
                for (i = 0; i < dataDongBo.length; i++ ) {
                    keHoachThangDongBo += dataDongBo[i].keHoachVal;
                    thucHienThangDongBo += dataDongBo[i].thucHienVal;
                }
                chartData.push({
                    title: 'Đồng bộ',
                    keHoachThang: keHoachThangDongBo,
                    thucHienThang: thucHienThangDongBo,
                    phanTramThang: keHoachThangDongBo ? ((thucHienThangDongBo/keHoachThangDongBo) * 100).toFixed(3) : 0
                });

                var keHoachThangPhatSong = 0;
                var thucHienThangPhatSong = 0;
                i = 0;
                for (i = 0; i < dataPhatSong.length; i++ ) {
                    keHoachThangPhatSong += dataPhatSong[i].keHoachVal;
                    thucHienThangPhatSong += dataPhatSong[i].thucHienVal;
                }
                chartData.push({
                    title: 'Phát sóng',
                    keHoachThang: keHoachThangPhatSong,
                    thucHienThang: thucHienThangPhatSong,
                    phanTramThang: keHoachThangPhatSong ? ((thucHienThangPhatSong/keHoachThangPhatSong) * 100).toFixed(3) : 0
                });

                var keHoachThangHsHc = 0;
                var thucHienThangHsHc = 0;
                i = 0;
                for (i = 0; i < dataHsHc.length; i++ ) {
                    keHoachThangHsHc += dataHsHc[i].keHoachVal;
                    thucHienThangHsHc += dataHsHc[i].thucHienVal;
                }
                chartData.push({
                    title: 'Hồ sơ hoàn công',
                    keHoachThang: keHoachThangHsHc,
                    thucHienThang: thucHienThangHsHc,
                    phanTramThang: keHoachThangHsHc ? ((thucHienThangHsHc/keHoachThangHsHc) * 100).toFixed(3) : 0
                });
                length = chartData.length;
                listAll = chartData;
                return chartData;
            }

            var chart = AmCharts.makeChart("chartDivTDTK", {

                "type": "serial",
                "dataDateFormat": "DD-MM-YYYY",
                "theme": "light",
                "legend": {
                    "useGraphSettings": true
                },
                "dataProvider": generateChartData(),
                "valueAxes": [{
                    "integersOnly": true,
                    "title": "Trạm",
                    "titleColor": "gray",
                    "maximum": getMaxOfArray(),
                    "minimum": 0,
                    "axisAlpha": 0,
                    "dashLength": 5,
                    "gridCount": 10,
                    "position": "left",
                },
                    {
                        "id": "valueAxis2",
                        "integersOnly": true,
                        // "title": "Trạm",
                        "titleColor": "gray",
                        "maximum": getMaxOfArrayPhanTram(),
                        "minimum": 0,
                        "axisAlpha": 0,
                        "dashLength": 5,
                        "gridCount": 10,
                        "position": "right",
                    }
                ],
                "startDuration": 0.5,
                "graphs": [
                    {
                        "type": "column",
                        "balloonText": "Kế hoạch: [[value]]",
                        "bullet": "round",
                        "title": "KH",
                        "valueField": "keHoachThang",
                        "fillAlphas": 1,
                        "fillColors": "#fad8ae",
                        "lineAlpha": 0.0,
                        // "color": "#000000",
                    }, {
                        "type": "column",
                        "balloonText": "Thực hiện: [[value]]",
                        "bullet": "round",
                        "title": "TH",
                        "valueField": "thucHienThang",
                        "fillAlphas": 1,
                        "fillColors": "#fd7d11",
                        "lineAlpha": 0.0,
                        // "color": "#000000",
                    }, 
                    {
                        "balloonText": "Tỷ lệ : [[value]]%",
                        "bullet": "round",
                        "title": "TH",
                        "valueField": "phanTramThang",
                        "fillAlphas": 0,
                        "bulletColor": "#fd7d11",
//                        "lineColor": "#fd7d11",
                        "lineThickness": 3,
                        "valueAxis": "valueAxis2"
                    }
                    ],
                "balloon":{
                    "maxWidth":500
                },
                "chartCursor": {
                    "cursorAlpha": 0,
                    "zoomable": false
                },
                "categoryField": "title",
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
        
        vm.provinceOptions={
    			dataSource: {
                    serverPaging: true,
                    schema: {
                        data: function (response) {
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            url: Constant.BASE_SERVICE_URL + "catProvinceServiceRest/catProvince/doSearchProvinceInPopup",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var obj = {};
                            return JSON.stringify(obj);

                        }
                    }
                },
    	        dataTextField: "code",
    	        dataValueField: "code",
    	        valuePrimitive: true
    	    }
        
        // end
    });

})();
