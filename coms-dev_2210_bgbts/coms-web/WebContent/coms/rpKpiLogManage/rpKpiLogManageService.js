angular.module('MetronicApp').factory('rpKpiLogManageService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "kpiLogRsService";
    var factory = {
    		doSearch: doSearch,
    };
    return factory;

    function doSearch(data) {
        return Restangular.all(serviceUrl + "/doSearch").post(data);
    }
    
}]);
