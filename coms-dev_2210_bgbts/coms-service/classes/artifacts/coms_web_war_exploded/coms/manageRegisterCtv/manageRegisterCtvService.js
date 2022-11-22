angular.module('MetronicApp').factory('manageRegisterCtvService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = RestEndpoint.AIO_SYS_USER_SERVICE_URL;
    var factory = {
        doSearch: doSearch,
        saveRegisterCtv:saveRegisterCtv,
        getImageById:getImageById,
        updateRegisterCtv:updateRegisterCtv,
        removeRecord:removeRecord
    };
    return factory;

    function doSearch(data) {
        return Restangular.all(serviceUrl + "/doSearchRegisterCtv").post(data);
    }
    
    function saveRegisterCtv(data) {
        return Restangular.all(serviceUrl + "/saveRegisterCtv").post(data);
    }
    
    function getImageById(data) {
        return Restangular.all(serviceUrl + "/getImageById").post(data);
    }
    
    function updateRegisterCtv(data) {
        return Restangular.all(serviceUrl + "/updateRegisterCtv").post(data);
    }
    
    function removeRecord(data) {
        return Restangular.all(serviceUrl + "/removeRecord").post(data);
    }
    
}]);