
angular.module('MetronicApp').factory('constructionTaskService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'constructionTaskService';
    var factory = {
        getConstructionTask : getConstructionTask,
        getConstructionTaskById : getConstructionTaskById,
        updateConstructionTask : updateConstructionTask,
        addConstructionTask : addConstructionTask,
        deleteConstructionTask : deleteConstructionTask,
        getDataForGrant:getDataForGrant,
        getDataTaskGrant:getDataTaskGrant,
        downloadErrorExcel: downloadErrorExcel,
        getListImageById: getListImageById,
        getSysGroupInfo:getSysGroupInfo,
        getCatWorkItemType:getCatWorkItemType,
        updatePerformer:updatePerformer
    };

    function getConstructionTask() {
        return Restangular.all(serviceUrl+"/constructionTask/getConstructionTask").post();
    }
    function getConstructionTaskById(obj) {
        return Restangular.all(serviceUrl+"/constructionTask/getConstructionTaskById").post(obj);
    }
    function updateConstructionTask(obj) {
        return Restangular.all(serviceUrl+"/constructionTask/updateConstructionTask").post(obj);
    }
    function addConstructionTask(obj) {
        return Restangular.all(serviceUrl+"/constructionTask/addConstructionTask").post(obj);
    }
    function deleteConstructionTask(obj) {
        return Restangular.all(serviceUrl+"/constructionTask/deleteConstructionTask").post(obj);
    }
    function getDataForGrant(obj) {
        return Restangular.all(serviceUrl+"/constructionTask/getDataForGrant").post(obj);
    }
    function getDataTaskGrant() {
        return Restangular.all(serviceUrl+"/constructionTask/getDataTaskGrant").post();
    }
    function downloadErrorExcel(obj) {
    	return Restangular.all( "fileservice/exportExcelError").post(obj);
	}
    function getListImageById(obj) {
        return Restangular.all(serviceUrl+"/constructionTask/getListImageById").post(obj);
    }
    //nhantv - 180811
    function getSysGroupInfo(id) {
    	return Restangular.all("constructionService/getSysGroupInfo").post(id);
    }
    
    function getCatWorkItemType(id) {
        return Restangular.all(serviceUrl+"/getWorkItemForAssign").post(id);
    }
    
    function updatePerformer(obj) {
        return Restangular.all(serviceUrl+"/updatePerformer").post(obj);
    }
    return factory;


}]);
