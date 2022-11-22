angular.module('MetronicApp').factory('reportCostOfSalesService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'reportCostOfSalesService';
    var factory = {
    		exportFile : exportFile
    };

    return factory;

    function exportFile(obj) {
        return Restangular.all(serviceUrl+"/exportFile").post(obj);
    }
    
}]);