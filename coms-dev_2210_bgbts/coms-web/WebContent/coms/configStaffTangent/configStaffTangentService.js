
angular.module('MetronicApp').factory('configStaffTangentService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'configStaffTangentRestService';
    var factory = {
        doSearch : doSearch,
        saveConfig : saveConfig,
        updateConfig : updateConfig,
        removeConfig : removeConfig
    };

    return factory;

    function doSearch(obj) {
        return Restangular.all(serviceUrl+"/doSearch").post(obj);
    }
    
    function saveConfig(obj) {
        return Restangular.all(serviceUrl+"/saveConfig").post(obj);
    }
    
    function updateConfig(obj) {
        return Restangular.all(serviceUrl+"/updateConfig").post(obj);
    }
    
    function removeConfig(obj) {
        return Restangular.all(serviceUrl+"/removeConfig").post(obj);
    }
    
    
}]);
