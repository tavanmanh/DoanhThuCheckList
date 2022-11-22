angular.module('MetronicApp').factory('rpEffectiveCalculateService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'rpBTSService';
    var factory = {
    	doSearchEffective : doSearchEffective
    };

    return factory;

    function doSearchEffective(obj) {
        return Restangular.all(serviceUrl+"/doSearchEffective").post(obj);
    }
    
}]);