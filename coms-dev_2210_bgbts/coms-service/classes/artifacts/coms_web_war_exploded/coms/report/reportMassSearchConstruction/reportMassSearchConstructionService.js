angular.module('MetronicApp').factory('reportMassSearchConstructionService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "reportServiceRest";
    var factory = {
        doSearch: doSearch,
    };
    return factory;

    function doSearch(data) {
        return Restangular.all(serviceUrl + "/doSearch").post(data);
    }
    
}]);