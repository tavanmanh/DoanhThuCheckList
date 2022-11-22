// VietNT_20181205_created
angular.module('MetronicApp').factory('synStockTransService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = RestEndpoint.SYN_STOCK_TRANS_URL;
    var factory = {
        doForwardGroup: doForwardGroup
    };
    return factory;

    function doForwardGroup(data) {
        return Restangular.all(serviceUrl + "/doForwardGroup").post(data);
    }
}]);
