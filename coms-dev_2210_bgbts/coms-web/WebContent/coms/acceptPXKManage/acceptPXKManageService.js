// VietNT_20181205_created
angular.module('MetronicApp').factory('acceptPXKManageService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = RestEndpoint.SYN_STOCK_TRANS_URL;
    var factory = {
        doForwardGroup: doForwardGroup,
		exportFileWordPXK : exportFileWordPXK,
		updateAcceptPXK: updateAcceptPXK,
		updateDenyPXK:updateDenyPXK,
		updateAssignPXK:updateAssignPXK,
		doSearchAcceptManage:doSearchAcceptManage,
		updateAcceptAssignPXK:updateAcceptAssignPXK,
		getMaterialInPxk:getMaterialInPxk,
		updateAcceptAssign:updateAcceptAssign,
		checkRolePGD:checkRolePGD
    };
    return factory;

    function doForwardGroup(data) {
        return Restangular.all(serviceUrl + "/doForwardGroup").post(data);
    }
    
    function exportFileWordPXK(object) {
        return Restangular.all("synStockTransService/exportFile").post(object);
    }
    
    function updateAcceptPXK(object) {
        return Restangular.all("synStockTransService/updateAcceptPXK").post(object);
    }
    
    function updateDenyPXK(object) {
        return Restangular.all("synStockTransService/updateDenyPXK").post(object);
    }
    
    function updateAssignPXK(object) {
        return Restangular.all("synStockTransService/updateAssignPXK").post(object);
    }
    
    function doSearchAcceptManage(object) {
        return Restangular.all("synStockTransService/doSearchAcceptManage").post(object);
    }
    
    function updateAcceptAssignPXK(object) {
        return Restangular.all("synStockTransService/updateAcceptAssignPXK").post(object);
    }
    
    function getMaterialInPxk(object) {
        return Restangular.all("workItemService/GoodsListTable").post(object);
    }
    
    function updateAcceptAssign(object) {
        return Restangular.all("synStockTransService/updateAcceptAssign").post(object);
    }
    
    function checkRolePGD() {
        return Restangular.all("synStockTransService/checkRolePGD").post();
    }
    
}]);
