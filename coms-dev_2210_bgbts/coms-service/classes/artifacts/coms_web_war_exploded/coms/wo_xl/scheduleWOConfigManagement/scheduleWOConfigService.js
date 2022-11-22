angular.module('MetronicApp').factory('scheduleWOConfigService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "woService";
    var factory = {
        getCdLv2List:getCdLv2List,
        getVhktCdLv2VList:getVhktCdLv2VList,
        getWorkItemList:getWorkItemList,
        createNewConfigWorkItem:createNewConfigWorkItem,
        getOneWIConfig:getOneWIConfig,
        updateWIConfig:updateWIConfig,
        deleteWIConfig:deleteWIConfig,
        getWOTypes: getWOTypes
    };
    return factory;

    function deleteWIConfig(obj){
        return Restangular.all(serviceUrl+"/wi/deleteWIConfig").post(obj);
    }

    function updateWIConfig(obj){
        return Restangular.all(serviceUrl+"/wi/updateWIConfig").post(obj);
    }

    function getOneWIConfig(obj){
        return Restangular.all(serviceUrl+"/wi/getOneWIConfig").post(obj);
    }

    function getCdLv2List(obj){
        return Restangular.all(serviceUrl+"/wo/getCdLv2List").post(obj);
    }

    function getVhktCdLv2VList(obj) {
        return Restangular.all(serviceUrl+"/wo/getVhktCdLv2VList").post(obj);
    }

    function getWorkItemList(obj) {
        return Restangular.all(serviceUrl+"/wi/doSearchWorkItem").post(obj);
    }

    function createNewConfigWorkItem(obj) {
        return Restangular.all(serviceUrl+"/wi/createWIConfig").post(obj);
    }

    function getWOTypes(){
        var obj = {page:1, pageSize: 9999}
        return Restangular.all(serviceUrl+"/woType/doSearch").post(obj);
    }

}]);
