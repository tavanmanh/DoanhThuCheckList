// picasso_2020jun02_created
angular.module('MetronicApp').factory('workItemCheckListManagementService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "woService";
    var factory = {
        getOneDetails:getOneDetails,
        updateWorkItemCheckList:updateWorkItemCheckList,
        deleteWorkItemCheckList:deleteWorkItemCheckList,
        createNewWorkItemCheckList:createNewWorkItemCheckList,
        getOneInfoWorkItem:getOneInfoWorkItem,
    };
    return factory;

    function getOneDetails(obj){
        return Restangular.all(serviceUrl+"/wi/getOneDetailsCheckList").post(obj);
    }

    function getOneInfoWorkItem(obj) {
        return Restangular.all(serviceUrl+"/wi/getOneInfoWorkItem").post(obj);
    }

    function deleteWorkItemCheckList(obj){
        return Restangular.all(serviceUrl+"/wi/deleteWorkItemCheckList").post(obj);
    }

    function updateWorkItemCheckList(obj){
        return Restangular.all(serviceUrl+"/wi/updateWorkItemCheckList").post(obj);
    }

    function createNewWorkItemCheckList(obj){
        return Restangular.all(serviceUrl+"/wi/createNewWorkItemCheckList").post(obj);
    }
}]);
