angular.module('MetronicApp').factory('trTypeManagementService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "trService";
    var factory = {
            getListTrType:getListTrType,
            deleteTrType:deleteTrType,
            searchTrType:searchTrType,
        createManyTRType:createManyTRType,
      getOneTRTypeDetails:getOneTRTypeDetails,
        updateTRType:updateTRType,
        createNewTRType:createNewTRType
    };
    return factory;

    function createManyTRType(obj) {
        return Restangular.all(serviceUrl+"/trType/createManyTRType").post(obj);
    }

    function getListTrType(obj) {
        return Restangular.all(serviceUrl+"/trType/getListTRType").post(obj);
    }

    function deleteTrType(woTrTypeId) {
        return Restangular.all(serviceUrl+"/trType/deleteTRType").post(woTrTypeId);
    }

    function searchTrType(obj) {
        return Restangular.all(serviceUrl+"/trType/doSearchTRType").post(obj);
    }

    function getOneTRTypeDetails(obj) {
        return Restangular.all(serviceUrl+"/trType/getOneTRTypeDetails").post(obj);
    }

    function updateTRType(obj) {
        return Restangular.all(serviceUrl+"/trType/updateTRType").post(obj);
    }

    function createNewTRType(obj){
        return Restangular.all(serviceUrl+"/trType/createTRType").post(obj);
    }

}]);
