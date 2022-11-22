angular.module('MetronicApp').factory('reportResultDeployBtsService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "reportServiceRest";
    var factory = {
        doSearch: doSearch,
    };
    return factory;

    function doSearch(data) {
        return Restangular.all(serviceUrl + "/doSearchReportResultDeployBts").post(data);
    }
    
}]);