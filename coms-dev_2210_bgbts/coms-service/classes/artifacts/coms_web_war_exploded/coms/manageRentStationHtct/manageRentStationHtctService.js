
angular.module('MetronicApp').factory('manageRentStationHtctService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'constructionTaskService';
    var factory = {
    		getListImageRentHtct : getListImageRentHtct
    };

    return factory;
    
    function getListImageRentHtct(id) {
        return Restangular.all(serviceUrl+"/getListImageRentHtct").post(id);
    }

}]);
