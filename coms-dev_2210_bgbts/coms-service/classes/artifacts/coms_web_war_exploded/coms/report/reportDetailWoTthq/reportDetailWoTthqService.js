
angular.module('MetronicApp').factory('reportDetailWoTthqService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'woService';
    var factory = {
        doSearch : doSearch,
    };

    return factory;

    function doSearch(obj) {
        return Restangular.all(serviceUrl+"/tthqService/getDataTableTTTHQ").post(obj);
    }

}]);
