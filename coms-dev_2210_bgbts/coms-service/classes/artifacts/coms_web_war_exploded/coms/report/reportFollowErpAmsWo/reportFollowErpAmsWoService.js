
angular.module('MetronicApp').factory('reportFollowErpAmsWoService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'rpOrderlyWoService';
    var factory = {
        doSearch : doSearch
    };

    return factory;
    
    function doSearch(obj) {
        return Restangular.all(serviceUrl+"/doSearchReportErpAmsWo").post(obj);
    }

}]);
