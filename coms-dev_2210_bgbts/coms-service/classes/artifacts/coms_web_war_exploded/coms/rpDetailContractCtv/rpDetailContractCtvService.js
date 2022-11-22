angular.module('MetronicApp').factory('rpDetailContractCtvService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'rpBTSService';
    var factory = {
    		exportRpDetailContractCtv : exportRpDetailContractCtv
    };

    return factory;

    function exportRpDetailContractCtv(obj) {
        return Restangular.all(serviceUrl+"/exportRpDetailContractCtv").post(obj);
    }
    
}]);