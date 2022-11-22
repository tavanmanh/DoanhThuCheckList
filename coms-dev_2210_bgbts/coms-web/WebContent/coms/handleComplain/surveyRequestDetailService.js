angular.module('MetronicApp').factory('surveyRequestDetailService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = RestEndpoint.COMPLAIN_ORDERS_REQUEST_DETAIL_LOG_HISTORY_SERVICE_URL;
    var factory = {
        doSearch: doSearch,
        add: add,
    };
    return factory;

   
    function doSearch(data) {
        return Restangular.all(serviceUrl + "/doSearch").post(data);
    }

    function add(data) {
        return Restangular.all(serviceUrl + "/add").post(data);
    }

}]);
