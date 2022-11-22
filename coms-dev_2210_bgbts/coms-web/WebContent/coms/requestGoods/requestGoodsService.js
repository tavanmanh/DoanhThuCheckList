// VietNT_20181205_created
angular.module('MetronicApp').factory('requestGoodsService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = 'requestGoodsService';
    var factory = {
        addNewRequestGoods: addNewRequestGoods,
        doRequestGoods: doRequestGoods,
        downloadTemplate: downloadTemplate,
        deleteRg: deleteRg,
        getDetailsById: getDetailsById,
        editRequestGoods: editRequestGoods,
        getFileDrop:getFileDrop,
        doSearchFileTk:doSearchFileTk,
        deleteFileTk:deleteFileTk,
        checkSysGroupInLike:checkSysGroupInLike,
        getRoleByEmail:getRoleByEmail,
        signVofficeYCSXVT:signVofficeYCSXVT
    };
    return factory;

    function addNewRequestGoods(data) {
        return Restangular.all(serviceUrl + "/addNewRequestGoods").post(data);
    }

    function doRequestGoods(data) {
        return Restangular.all(serviceUrl + "/doRequestGoods").post(data);
    }

    function downloadTemplate(templateDetail) {
        return Restangular.all(serviceUrl + "/downloadTemplate").post(templateDetail);
    }

    function deleteRg(requestGoodsId) {
        return Restangular.all(serviceUrl + "/deleteRequest").post(requestGoodsId);
    }

    function getDetailsById(requestGoodsId) {
        return Restangular.all(serviceUrl + "/getDetailsById").post(requestGoodsId);
    }

    function editRequestGoods(obj) {
        return Restangular.all(serviceUrl + "/editRequestGoods").post(obj);
    }

    function doAssignHandover(data) {
        return Restangular.all(serviceUrl + "/doAssignHandover").post(data);
    }

    function getListImageHandover(handoverId) {
        return Restangular.all(serviceUrl + "/getListImageHandover").post(handoverId);
    }
    
    function getFileDrop() {
		return Restangular.all(serviceUrl + "/getFileDrop").getList();
	}
    
    function doSearchFileTk(obj) {
        return Restangular.all("attachmentRsServiceRest/attachment/doSearchFileTk").post(obj);
    }
    
    function deleteFileTk(id) {
        return Restangular.all(serviceUrl + "/deleteFileTk").post(id);
    }
    
    function checkSysGroupInLike(id) {
        return Restangular.all(serviceUrl + "/checkSysGroupInLike").post(id);
    }
    
    function getRoleByEmail(obj) {
        return Restangular.all("signVofficeRsServiceRest/signVoffice/getRoleByEmail").post(obj);
    }
    
    function signVofficeYCSXVT(List) {
        return Restangular.all("reportServiceRest/signVofficeYCSXVT").post(List); 	 
    };
    
}]);
