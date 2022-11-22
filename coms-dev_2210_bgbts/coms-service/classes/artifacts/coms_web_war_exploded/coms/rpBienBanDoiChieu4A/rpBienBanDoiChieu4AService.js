
angular.module('MetronicApp').factory('rpBienBanDoiChieu4AService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'rpBienBanDoiChieu4AService';
    var factory = {
    		getForCompleteConstruction:getForCompleteConstruction
    };

    return factory;
    
    function getForCompleteConstruction(obj){
    	return Restangular.all(serviceUrl+"/getForCompleteConstruction").post(obj);
    }
}]);
