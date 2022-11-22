angular.module('MetronicApp').factory('reportAcceptWoHshcService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = RestEndpoint.API_REPORT_SERVICE;
    var factory = {
        doSearch: doSearch,
    };
    return factory;

    function doSearch(data) {
        return Restangular.all(serviceUrl + "/doSearchReportAcceptHSHC").post(data);
    }
    
}]);