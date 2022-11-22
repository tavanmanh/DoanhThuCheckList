angular.module('MetronicApp').factory('woConfigContractService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "trService";
    var factory = {
        createNew:createNew,
        getOneWoConfigContract:getOneWoConfigContract,
        updateWoConfigContract:updateWoConfigContract,
        deleteWoConfigContract:deleteWoConfigContract,
    };
    return factory;

    function deleteWoConfigContract(obj){
        return Restangular.all(serviceUrl+"/woConfigContract/deleteWoConfigContract").post(obj);
    }

    function updateWoConfigContract(obj){
        return Restangular.all(serviceUrl+"/woConfigContract/updateWoConfigContract").post(obj);
    }

    function getOneWoConfigContract(obj){
        return Restangular.all(serviceUrl+"/woConfigContract/getOneWoConfigContract").post(obj);
    }
    function createNew(obj) {
        return Restangular.all(serviceUrl+"/woConfigContract/createWoConfigContract").post(obj);
    }

}]);
