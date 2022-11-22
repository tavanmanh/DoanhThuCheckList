
angular.module('MetronicApp').factory('manageTangentCustomerService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'tangentCustomerRestService';
    var factory = {
        doSearch : doSearch,
        save : save,
        update : update,
        deleteRecord : deleteRecord,
        getUserConfigTagent : getUserConfigTagent,
        saveApproveOrReject : saveApproveOrReject,
        saveNotDemain : saveNotDemain,
        saveDetail : saveDetail,
        checkRoleApproved : checkRoleApproved,
        getListResultTangentByTangentCustomerId : getListResultTangentByTangentCustomerId,
        getResultSolutionByContractId : getResultSolutionByContractId,
        saveResultSolution : saveResultSolution,
        saveApproveOrRejectGiaiPhap : saveApproveOrRejectGiaiPhap,
        getListResultTangentJoinSysUserByTangentCustomerId:getListResultTangentJoinSysUserByTangentCustomerId,
        getResultSolutionJoinSysUserByTangentCustomerId : getResultSolutionJoinSysUserByTangentCustomerId,
        getListResultTangentByResultTangentId : getListResultTangentByResultTangentId,
        checkRoleUpdate:checkRoleUpdate,
        getContractRose:getContractRose,
        getUserConfigTagentByProvince:getUserConfigTagentByProvince,
        approveRose:approveRose,
        checkRoleSourceYCTX:checkRoleSourceYCTX,
        uploadAttachment:uploadAttachment,
        checkRoleCreated:checkRoleCreated,
        getChannel: getChannel,
        checkRoleUserAssignYctx:checkRoleUserAssignYctx,
        findConversation: findConversation
    }
    return factory;

    function doSearch(obj) {
        return Restangular.all(serviceUrl+"/doSearch").post(obj);
    }
    
    function save(obj) {
        return Restangular.all(serviceUrl+"/save").post(obj);
    }
    
    function update(obj) {
        return Restangular.all(serviceUrl+"/update").post(obj);
    }
    
    function getUserConfigTagent(obj) {
        return Restangular.all(serviceUrl+"/getUserConfigTagent").post(obj);
    }
    
    function deleteRecord(obj) {
        return Restangular.all(serviceUrl+"/deleteRecord").post(obj);
    }
    
    function saveApproveOrReject(obj) {
        return Restangular.all(serviceUrl+"/saveApproveOrReject").post(obj);
    }
    
    function saveNotDemain(obj) {
        return Restangular.all(serviceUrl+"/saveNotDemain").post(obj);
    }
    
    function saveDetail(obj) {
        return Restangular.all(serviceUrl+"/saveDetail").post(obj);
    }
    
    function checkRoleApproved() {
        return Restangular.all(serviceUrl+"/checkRoleApproved").post();
    }
    
    function getListResultTangentByTangentCustomerId(obj) {
        return Restangular.all(serviceUrl+"/getListResultTangentByTangentCustomerId").post(obj);
    }
    
    function getResultSolutionByContractId(id) {
        return Restangular.all(serviceUrl+"/getResultSolutionByContractId").post(id);
    }
    
    function saveResultSolution(obj) {
        return Restangular.all(serviceUrl+"/saveResultSolution").post(obj);
    }
    
    function saveApproveOrRejectGiaiPhap(obj) {
        return Restangular.all(serviceUrl+"/saveApproveOrRejectGiaiPhap").post(obj);
    }
    
    function getListResultTangentJoinSysUserByTangentCustomerId(id) {
        return Restangular.all(serviceUrl+"/getListResultTangentJoinSysUserByTangentCustomerId").post(id);
    }
    
    function getResultSolutionJoinSysUserByTangentCustomerId(id) {
        return Restangular.all(serviceUrl+"/getResultSolutionJoinSysUserByTangentCustomerId").post(id);
    }
    
    function getListResultTangentByResultTangentId(id) {
        return Restangular.all(serviceUrl+"/getListResultTangentByResultTangentId").post(id);
    }
    
    function checkRoleUpdate() {
        return Restangular.all(serviceUrl+"/checkRoleUpdate").post();
    }
    
    function getContractRose() {
        return Restangular.all(serviceUrl+"/getContractRose").post();
    }
    function approveRose(obj) {
        return Restangular.all(serviceUrl+"/approveRose").post(obj);
    }
    
    function getUserConfigTagentByProvince(obj) {
        return Restangular.all(serviceUrl+"/getUserConfigTagentByProvince").post(obj);
    }
    
    function checkRoleSourceYCTX(obj) {
        return Restangular.all(serviceUrl+"/checkRoleSourceYCTX").post(obj);
    }
    
    function uploadAttachment(apiEndpoint, formData) {
        return $http.post(apiEndpoint, formData, {
            transformRequest: angular.identity,
            headers: {'Content-Type': 'multipart/form-data'}
        });
    }
    
    function checkRoleCreated() {
        return Restangular.all(serviceUrl+"/checkRoleCreated").post();
    }
    
    function getChannel(obj) {
        return Restangular.all(serviceUrl+"/getChannel").post(obj);
    }
    
    function checkRoleUserAssignYctx() {
        return Restangular.all(serviceUrl+"/checkRoleUserAssignYctx").post();
    }

    function findConversation(obj) {
        return Restangular.all(serviceUrl+"/getCallbotConversation").post(obj);
    }
}]);
