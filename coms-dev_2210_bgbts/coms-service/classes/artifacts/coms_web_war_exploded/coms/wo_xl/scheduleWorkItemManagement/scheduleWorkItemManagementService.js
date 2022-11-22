angular.module('MetronicApp').factory('workItemManagementService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "woService";
    var factory = {
            getListTrType:getListTrType,
        deleteWorkItem:deleteWorkItem,
            searchTrType:searchTrType,
        createManyTRType:createManyTRType,
        getOneInfoWorkItem:getOneInfoWorkItem,
        updateWorkItem:updateWorkItem,
        createNewWorkItem:createNewWorkItem
    };
    return factory;

    function createManyTRType(obj) {
        return Restangular.all(serviceUrl+"/trType/createManyTRType").post(obj);
    }

    function getListTrType(obj) {
        return Restangular.all(serviceUrl+"/trType/getListTRType").post(obj);
    }

    function deleteWorkItem(woTrTypeId) {
        return Restangular.all(serviceUrl+"/wi/deleteWorkItem").post(woTrTypeId);
    }

    function searchTrType(obj) {
        return Restangular.all(serviceUrl+"/trType/doSearchTRType").post(obj);
    }

    function getOneInfoWorkItem(obj) {
        return Restangular.all(serviceUrl+"/wi/getOneInfoWorkItem").post(obj);
    }

    function updateWorkItem(obj) {
        return Restangular.all(serviceUrl+"/wi/updateWorkItem").post(obj);
    }

    function createNewWorkItem(obj){
        return Restangular.all(serviceUrl+"/wi/createWorkItem").post(obj);
    }

}]);
