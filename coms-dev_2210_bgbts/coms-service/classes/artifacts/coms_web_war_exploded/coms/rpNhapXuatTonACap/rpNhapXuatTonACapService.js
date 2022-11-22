
angular.module('MetronicApp').factory('rpNhapXuatTonACapService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'synStockDailyIeService';
    var factory = {
    		doSearchGoods:doSearchGoods
    };

    return factory;
    
    function doSearchGoods(obj) {
        return Restangular.all(serviceUrl+"/doSearchGoods").post(obj);
    }
}]);
