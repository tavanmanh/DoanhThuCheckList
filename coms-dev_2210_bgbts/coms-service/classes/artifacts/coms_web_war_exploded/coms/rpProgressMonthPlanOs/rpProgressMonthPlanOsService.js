
angular.module('MetronicApp').factory('rpProgressMonthPlanOsService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'progressPlanProjectRestService';
    var factory = {
        doSearch : doSearch
    };

    return factory;

    function doSearch(obj) {
        return Restangular.all(serviceUrl+"/doSearch").post(obj);
    }
    
}]);
