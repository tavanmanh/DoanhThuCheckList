// VietNT_20181205_created
angular.module('MetronicApp').factory('woNameManagementService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "woService";
    var factory = {
        deleteWOName:deleteWOName,
        updateWOName:updateWOName,
        createManyWONames:createManyWONames,
        createNewWOName:createNewWOName,
        getWOTypes:getWOTypes,
    };
    return factory;

    function deleteWOName(obj) {
        return Restangular.all(serviceUrl+"/woName/delete").post(obj);
    }

    function updateWOName(obj) {
        return Restangular.all(serviceUrl+"/woName/update").post(obj);
    }

    function createManyWONames(obj) {
        return Restangular.all(serviceUrl+"/woName/createMany").post(obj);
    }

    function createNewWOName(obj){
        return Restangular.all(serviceUrl+"/woName/create").post(obj);
    }

    function getWOTypes(){
        var obj = {page:1, pageSize: 9999}
        return Restangular.all(serviceUrl+"/woType/doSearch").post(obj);
    }
}]);
