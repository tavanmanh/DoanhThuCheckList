// VietNT_20181205_created
angular.module('MetronicApp').factory('synStockDailyIeReportService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = RestEndpoint.SYN_STOCK_DAILY_IE_URL;
    var factory = {
        doSearchGoodsDebtConfirmDetail: doSearchGoodsDebtConfirmDetail,
        exportGoodsDebtConfirmDetail: exportGoodsDebtConfirmDetail,
        doSearchGoodsDebtConfirmGeneral: doSearchGoodsDebtConfirmGeneral,
        exportGoodsDebtConfirmGeneral: exportGoodsDebtConfirmGeneral,
        exportContractPerformance: exportContractPerformance,
        exportDetailIERGoods:exportDetailIERGoods
    };
    return factory;

    function doSearchGoodsDebtConfirmDetail(data) {
        return Restangular.all(serviceUrl + "/doSearchGoodsDebtConfirmDetail").post(data);
    }

    function doSearchGoodsDebtConfirmGeneral(data) {
        return Restangular.all(serviceUrl + "/doSearchGoodsDebtConfirmGeneral").post(data);
    }

    function exportGoodsDebtConfirmDetail(data) {
        return Restangular.all(serviceUrl + "/exportGoodsDebtConfirmDetail").post(data);
    }

    function exportGoodsDebtConfirmGeneral(data) {
        return Restangular.all(serviceUrl + "/exportGoodsDebtConfirmGeneral").post(data);
    }

    function exportContractPerformance(data) {
        return Restangular.all(serviceUrl + "/exportContractPerformance").post(data);
    }

    function exportDetailIERGoods(data) {
        return Restangular.all(serviceUrl + "/exportDetailIERGoods").post(data);
    }
}]);
