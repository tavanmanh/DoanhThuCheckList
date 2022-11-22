// VietNT_20181205_created
angular.module('MetronicApp').factory('woTypeManagementService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "woService";
    var factory = {
        getOneWOTypeDetails:getOneWOTypeDetails,
        deleteWOType:deleteWOType,
        updateWOType:updateWOType,
        createManyWOType:createManyWOType,
        createNewWOType:createNewWOType,
    };
    return factory;

    function getOneWOTypeDetails(obj){
    	return Restangular.all(serviceUrl+"/woType/getById").post(obj);
    }

    function deleteWOType(obj) {
        return Restangular.all(serviceUrl+"/woType/delete").post(obj);
    }

    function updateWOType(obj) {
        return Restangular.all(serviceUrl+"/woType/update").post(obj);
    }

    function createManyWOType(obj) {
        return Restangular.all(serviceUrl+"/woType/createMany").post(obj);
    }

    function createNewWOType(obj){
        return Restangular.all(serviceUrl+"/woType/create").post(obj);
    }
}]);
