angular.module('MetronicApp').factory('rpEvaluateKpiHSHCService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'rpQuantityService';
    var factory = {
    		doSearchEvaluateKpiHshc : doSearchEvaluateKpiHshc,
    };

    return factory;

    function doSearchEvaluateKpiHshc(obj) {
        return Restangular.all(serviceUrl+"/doSearchEvaluateKpiHshc").post(obj);
    }

    

}]);
