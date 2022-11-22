angular.module('MetronicApp').factory('hcqtProjectManagementService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "woService";
    var factory = {
        deleteHcqtProject:deleteHcqtProject,
        searchHcqtProject:searchHcqtProject,
        getOneHcqtProjectDetails:getOneHcqtProjectDetails,
        updateHcqtProject:updateHcqtProject,
        createNewHcqtProject:createNewHcqtProject
    };
    return factory;

    function deleteHcqtProject(hcqtProjectID) {
        return Restangular.all(serviceUrl+"/hcqtProject/deleteHcqtProject").post(hcqtProjectID);
    }

    function searchHcqtProject(obj) {
        return Restangular.all(serviceUrl+"/hcqtProject/doSearchHcqtProject").post(obj);
    }

    function getOneHcqtProjectDetails(obj) {
        return Restangular.all(serviceUrl+"/hcqtProject/getOneHcqtProjectDetails").post(obj);
    }

    function updateHcqtProject(obj) {
        return Restangular.all(serviceUrl+"/hcqtProject/updateHcqtProject").post(obj);
    }

    function createNewHcqtProject(obj){
        return Restangular.all(serviceUrl+"/hcqtProject/createHcqtProject").post(obj);
    }

}]);
