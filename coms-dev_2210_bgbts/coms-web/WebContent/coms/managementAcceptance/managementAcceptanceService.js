
angular.module('MetronicApp').factory('managementAcceptanceService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    
		var serviceUrl = 'constructionService';
		var factory = {
			doSearchAcceptance:doSearchAcceptance,
			getWorkItemByMerchandise:getWorkItemByMerchandise,
			updateWorkItemMerchan:updateWorkItemMerchan,
			deleteConstructionMerchanse:deleteConstructionMerchanse,
			updateConstructionAcceptance:updateConstructionAcceptance,
			updateWorkItemMerchanB:updateWorkItemMerchanB,
			getWorkItemByMerchandiseB:getWorkItemByMerchandiseB,
			getSynStockTransBySerial:getSynStockTransBySerial,
			getConstructionAcceptanceByConsId:getConstructionAcceptanceByConsId,
			getConstructionAcceptanceByConsIdTBB:getConstructionAcceptanceByConsIdTBB,
      getConstructionAcceptanceByConsIdCheck:getConstructionAcceptanceByConsIdCheck,
			getDataMerByGoodsId:getDataMerByGoodsId,
			getSysGroupCheck:getSysGroupCheck
		}
		return factory;
		
		function doSearchAcceptance(obj) {
	        return Restangular.all(serviceUrl+"/doSearchAcceptance").post(obj);
	    }
		
		function getWorkItemByMerchandise(obj) {
	        return Restangular.all(serviceUrl+"/getWorkItemByMerchandise").post(obj);
	    }
		
		function updateWorkItemMerchan(obj) {
	        return Restangular.all(serviceUrl+"/updateWorkItemMerchan").post(obj);
	    }
		
		function deleteConstructionMerchanse(obj) {
	        return Restangular.all(serviceUrl+"/deleteConstructionMerchanse").post(obj);
	    }
		
		function updateConstructionAcceptance(obj) {
	        return Restangular.all(serviceUrl+"/updateConstructionAcceptance").post(obj);
	    }
		
		function updateWorkItemMerchanB(obj) {
	        return Restangular.all(serviceUrl+"/updateWorkItemMerchanB").post(obj);
	    }
		
		function getWorkItemByMerchandiseB(obj) {
	        return Restangular.all(serviceUrl+"/getWorkItemByMerchandiseB").post(obj);
	    }
		
		function getSynStockTransBySerial(obj) {
	        return Restangular.all(serviceUrl+"/getSynStockTransBySerial").post(obj);
	    }
		
		function getConstructionAcceptanceByConsId(obj) {
	        return Restangular.all(serviceUrl+"/getConstructionAcceptanceByConsId").post(obj);
	    }
		
		function getConstructionAcceptanceByConsIdTBB(obj) {
	        return Restangular.all(serviceUrl+"/getConstructionAcceptanceByConsIdTBB").post(obj);
	    }
		function getConstructionAcceptanceByConsIdCheck(obj) {
	        return Restangular.all(serviceUrl+"/getConstructionAcceptanceByConsIdCheck").post(obj);
	    }
    
		function getDataMerByGoodsId(obj) {
	        return Restangular.all(serviceUrl+"/getDataMerByGoodsId").post(obj);
	    }
		
		function getSysGroupCheck(obj) {
	        return Restangular.all(serviceUrl+"/getSysGroupCheck").post(obj);
	    }
}]);
