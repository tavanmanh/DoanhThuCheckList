angular.module('MetronicApp').factory('wO5sConfigConfigService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "woService";
    var factory = {
        getCnktList:getCnktList,
        getFTList:getFTList,
        getWorkItemList:getWorkItemList,
        createNewConfigWO5s:createNewConfigWO5s,
        getOneWO5SConfig:getOneWO5SConfig,
        updateWO5SConfig:updateWO5SConfig,
        deleteWO5SConfig:deleteWO5SConfig,
        getSysUserGroup: getSysUserGroup,
    };
    return factory;

    function deleteWO5SConfig(obj){
        return Restangular.all(serviceUrl+"/wo/deleteWO5SConfig").post(obj);
    }

    function updateWO5SConfig(obj){
        return Restangular.all(serviceUrl+"/wo/updateWO5SConfig").post(obj);
    }

    function getOneWO5SConfig(obj){
        return Restangular.all(serviceUrl+"/wo/getOneWO5SConfig").post(obj);
    }

    function getFTList(obj){
        return Restangular.all(serviceUrl+"/wo/getFTList").post(obj);
    }

    function getCnktList(obj){
        return Restangular.all(serviceUrl+"/wo/getCdLv2List").post(obj);
    }

    function getWorkItemList(obj) {
        return Restangular.all(serviceUrl+"/wi/doSearchWorkItem").post(obj);
    }

    function createNewConfigWO5s(obj) {
        return Restangular.all(serviceUrl+"/wo/createConfigWO5s").post(obj);
    }

    function getSysUserGroup(obj){
        return Restangular.all(serviceUrl+"/user/getSysUserGroup").post(obj);
    }

}]);
