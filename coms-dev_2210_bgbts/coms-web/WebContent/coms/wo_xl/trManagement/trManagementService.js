// picasso_2020jun9_created
angular.module('MetronicApp').factory('trManagementService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "trService";
    var factory = {
        doSearchCreated: doSearchCreated,
        deleteTR: deleteTR,
        updateTR:updateTR,
        getOneTRDetails:getOneTRDetails,
        giveAssignmentTRToCD:giveAssignmentTRToCD,
        createManyTR:createManyTR,
        createNewTR:createNewTR,
        getTrTypes:getTrTypes,
        getAvailableContracts:getAvailableContracts,
        getAvailableProjects:getAvailableProjects,
        getConstructionByContract:getConstructionByContract,
        getConstructionByProject:getConstructionByProject,
        getAppWorkSrcs:getAppWorkSrcs,
        getCdLv2List:getCdLv2List,
        acceptTr:acceptTr,
        rejectTr:rejectTr,
        uploadAttachment:uploadAttachment,
        mappingAttachmentToTR:mappingAttachmentToTR,
        getCdLevel1:getCdLevel1,
        getConstructionByProject:getConstructionByProject,
        getConstructionByContract:getConstructionByContract,
        getStationById:getStationById,
        getSysUserGroup:getSysUserGroup,
        getSysGroupById:getSysGroupById,
        getCatConstructionTypes:getCatConstructionTypes,
        getInactiveWoList:getInactiveWoList,
        changeTmbtStation:changeTmbtStation,
        downloadErrorExcel:downloadErrorExcel
    };
    return factory;

    function doSearchCreated(obj){
        return Restangular.all(serviceUrl+"/tr/doSearchCreated").post(obj);
    }
    function doSearch(obj){
        return Restangular.all(serviceUrl+"/tr/doSearch").post(obj);
    }

    function deleteTR(obj){
        return Restangular.all(serviceUrl+"/tr/deleteTR").post(obj);
    }

    function updateTR(obj){
        return Restangular.all(serviceUrl+"/tr/updateTR").post(obj);
    }

    function getOneTRDetails(obj){
        return Restangular.all(serviceUrl+"/tr/getOneTRDetails").post(obj);
    }

    function giveAssignmentTRToCD(obj){
        return Restangular.all(serviceUrl+"/tr/giveAssignmentToCD").post(obj);
    }

    function createManyTR(obj) {
        return Restangular.all(serviceUrl+"/tr/createManyTR").post(obj);
    }

    function createNewTR(obj){
        return Restangular.all(serviceUrl+"/tr/createTR").post(obj);
    }

    function getTrTypes(){
        return Restangular.all(serviceUrl+"/trType/getListTRType").post({});
    }

    function getAvailableContracts(){
        return Restangular.all(serviceUrl+"/tr/getAvailableContracts").post({});
    }

    function getConstructionByContract(obj){
        return Restangular.all(serviceUrl+"/tr/getConstructionByContract").post(obj);
    }
    function getConstructionByProject(obj){
        return Restangular.all(serviceUrl+"/tr/getConstructionByProject").post(obj);
    }

    function getAvailableProjects(){
        return Restangular.all(serviceUrl+"/tr/getAvailableProjects").post({});
    }

    function getAppWorkSrcs(){
        return Restangular.all("woService/wo/getAppWorkSrcs").post({});
    }
    function getCdLv2List(){
        return Restangular.all("woService/wo/getCdLv2List").post({});
    }

    function acceptTr(obj){
        return Restangular.all(serviceUrl+"/tr/cdAcceptAssignment").post(obj);
    }

    function rejectTr(obj){
        return Restangular.all(serviceUrl+"/tr/cdRejectAssignment").post(obj);
    }

    function uploadAttachment(apiEndpoint, formData){
        return $http.post(apiEndpoint, formData, {
            transformRequest: angular.identity,
            headers: {'Content-Type': 'multipart/form-data'}
        });
    }

    function mappingAttachmentToTR(obj) {
        return Restangular.all("woService/fileAttach/createWOMappingAttach").post(obj);
    }

    function getCdLevel1(obj){
        return Restangular.all(serviceUrl + "/tr/getCdLevel1").post(obj);
    }

    function getConstructionByProject(obj){
        return Restangular.all("trService/tr/getConstructionByProject").post(obj);
    }

    function getConstructionByContract(obj){
        return Restangular.all("trService/tr/getConstructionByContract").post(obj);
    }

    function getStationById(obj) {
        return Restangular.all("trService/tr/getStationById").post(obj);
    }

    function getSysUserGroup(obj){
        Restangular.all("woService/user/getSysUserGroup").post(obj);
    }

    function getSysGroupById(obj) {
        return Restangular.all("trService/group/getSysGroupById").post(obj);
    }

    function getCatConstructionTypes(){
        return Restangular.all("trService/workItem/getCatConstructionTypes").post();
    }

    function getInactiveWoList(obj){
        return Restangular.all("trService/workItem/getInactiveWoList").post(obj);
    }

    function changeTmbtStation(obj){
        return Restangular.all("trService/tmbt/changeTmbtStation").post(obj);
    }
    
    function downloadErrorExcel(obj) {
        return Restangular
            .all(
                "fileservice/exportExcelError")
            .post(obj);
    }
}]);
