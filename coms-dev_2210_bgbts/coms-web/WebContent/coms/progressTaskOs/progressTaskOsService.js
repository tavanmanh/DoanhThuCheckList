
angular.module('MetronicApp').factory('progressTaskOsService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'progressTaskOsRsService';
    var factory = {
        doSearch : doSearch,
        saveAdd:saveAdd,
        saveUpdate:saveUpdate,
        deleteRecord:deleteRecord,
        getDataTaskByProvince:getDataTaskByProvince,
        getById:getById,
        updateProgressTaskOs:updateProgressTaskOs
    };

    return factory;
    
    function doSearch(obj) {
        return Restangular.all(serviceUrl+"/doSearchMain").post(obj);
    }
    
    function saveAdd(obj) {
        return Restangular.all(serviceUrl+"/saveAdd").post(obj);
    }
    
    function saveUpdate(obj) {
        return Restangular.all(serviceUrl+"/saveUpdate").post(obj);
    }
    
    function deleteRecord(obj) {
        return Restangular.all(serviceUrl+"/deleteRecord").post(obj);
    }
    
    function getDataTaskByProvince(obj) {
        return Restangular.all(serviceUrl+"/getDataTaskByProvince").post(obj);
    }
    
    function getById(obj) {
        return Restangular.all(serviceUrl+"/getById").post(obj);
    }
    
    function updateProgressTaskOs(obj) {
        return Restangular.all(serviceUrl+"/updateProgressTaskOs").post(obj);
    }
}]);
