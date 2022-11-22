angular.module('MetronicApp').factory('reportGeneralCtvService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = RestEndpoint.REPORT_SERVICE;
    var factory = {
        doSearch: doSearch,
    };
    return factory;

    function doSearch(data) {
        return Restangular.all(serviceUrl + "/doSearchGeneralCtv").post(data);
    }
    
}]);