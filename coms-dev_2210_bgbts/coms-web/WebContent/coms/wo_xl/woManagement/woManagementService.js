// picasso_2020jun9_created
angular.module('MetronicApp').factory('woManagementService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "woService";
    var factory = {
        doSearch: doSearch,
        deleteWO: deleteWO,
        updateWO:updateWO,
        getOneWODetails:getOneWODetails,
        giveAssignment:giveAssignment,
        getSysUserGroup: getSysUserGroup,
        acceptWO:acceptWO,
        rejectWO:rejectWO,
        createManyWo:createManyWo,
        getAppWorkSrcs: getAppWorkSrcs,
        getTrList:getTrList,
        getApConstructionTypes:getApConstructionTypes,
        getWOTypes: getWOTypes,
        getCdLv2List:getCdLv2List,
        getSysGroup: getSysGroup,
        createManyHCQTWo: createManyHCQTWo,
        getWoNameList:getWoNameList,
        getHcqtProjectList:getHcqtProjectList,
        tcAcceptAllSelected:tcAcceptAllSelected,
        tcRejectAllSelected:tcRejectAllSelected,
        getTcTctEmails:getTcTctEmails,
        getListItem: getListItem,
        checkRoleDeleteTTHT: checkRoleDeleteTTHT, //HienLT56 add 27052021
        getListItemByWorkSrc: getListItemByWorkSrc, //Taotq add 27082021
    };
    return factory;

    function doSearch(obj){
        return Restangular.all(serviceUrl+"/wo/doSearch").post(obj);
    }

    function deleteWO(obj){
        return Restangular.all(serviceUrl+"/wo/deleteWO").post(obj);
    }

    function updateWO(obj){
        return Restangular.all(serviceUrl+"/wo/updateWO").post(obj);
    }

    function getOneWODetails(obj){
        return Restangular.all(serviceUrl+"/wo/getOneWODetails").post(obj);
    }
    
    function getListItem(code){
        return Restangular.all(serviceUrl+"/wo/getListItemN").post(code);
    }

    function giveAssignment(obj){
        return Restangular.all(serviceUrl+"/wo/giveWOAssignment").post(obj);
    }

    function getSysUserGroup(obj){
        return Restangular.all(serviceUrl+"/user/getSysUserGroup").post(obj);
    }

    function acceptWO(obj){
        return Restangular.all(serviceUrl+"/wo/acceptWO").post(obj);
    }

    function rejectWO(obj){
        return Restangular.all(serviceUrl+"/wo/rejectWO").post(obj);
    }

    function createManyWo(obj) {
        return Restangular.all(serviceUrl+"/wo/createManyWO").post(obj);
    }

    function getAppWorkSrcs(){
        return Restangular.all(serviceUrl+"/wo/getAppWorkSrcs").post({});
    }

    function getTrList(obj){
        return Restangular.all("trService/tr/doSearch").post(obj);
    }

    function getApConstructionTypes(){
        return Restangular.all(serviceUrl+"/wo/getAppConstructionTypes").post({});
    }

    function getWOTypes(){
        var obj = {page:1, pageSize: 9999}
        return Restangular.all(serviceUrl+"/woType/doSearch").post(obj);
    }

    function getCdLv2List(obj){
        return Restangular.all(serviceUrl+"/wo/getCdLv2List").post(obj);
    }

    function getSysGroup(obj){
        return Restangular.all(serviceUrl+"/wo/getSysGroup").post(obj);
    }

    function createManyHCQTWo(obj) {
        var timeoutTime = 1000 * 60 * 5; //5min
        return Restangular.all(serviceUrl+"/wo/createManyHCQTWO").withHttpConfig({timeout: timeoutTime}).post(obj);
    }

    function getWoNameList(){
        var obj = {page:1, pageSize: 9999}
        return Restangular.all(serviceUrl+"/woName/doSearch").post(obj);
    }

    function getHcqtProjectList(){
        var obj = {page:1, pageSize: 9999}
        return Restangular.all(serviceUrl+"/hcqtProject/doSearchHcqtProject").post(obj);
    }

    function tcAcceptAllSelected(obj){
        return Restangular.all(serviceUrl+"/tc/tcAcceptAllSelected").post(obj);
    }

    function tcRejectAllSelected(obj){
        return Restangular.all(serviceUrl+"/tc/tcRejectAllSelected").post(obj);
    }

    function getTcTctEmails(obj){
        return Restangular.all(serviceUrl+"/tc/getTcTctEmails").post(obj);
    }
    //HienLT56 start 27052021
    function checkRoleDeleteTTHT(obj) {
        return Restangular.all(serviceUrl + "/wo/checkRoleDeleteTTHT").post(obj);
    }
    // Taotq start 27082021
    function getListItemByWorkSrc(){
    	return Restangular.all(serviceUrl+"/wo/getListItemByWorkSrc").post({});
    }
    // Taotq end 27082021
}]);
