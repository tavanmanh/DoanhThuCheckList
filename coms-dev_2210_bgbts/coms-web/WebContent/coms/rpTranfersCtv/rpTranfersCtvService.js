angular.module('MetronicApp').factory('rpTranfersCtvService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'rpBTSService';
    var factory = {
    	exportRpTranfersCtv : exportRpTranfersCtv
    };

    return factory;

    function exportRpTranfersCtv(obj) {
        return Restangular.all(serviceUrl+"/exportRpTranfersCtv").post(obj);
    }
    
}]);