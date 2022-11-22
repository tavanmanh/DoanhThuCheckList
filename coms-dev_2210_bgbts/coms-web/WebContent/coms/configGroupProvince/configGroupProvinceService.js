
angular.module('MetronicApp').factory('configGroupProvinceService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = 'configGroupProvinceService';
	    var factory = {
	       getCatProvince:getCatProvince,
	       saveCatProvince:saveCatProvince,
	       removeCat:removeCat,
	       getListCode:getListCode

	    };
	 
	     return factory;
    function getCatProvince() {
        return Restangular.all(serviceUrl+"/getCatProvince").post();
    }
    function saveCatProvince(obj) {
        return Restangular.all(serviceUrl+"/saveCatProvince").post(obj);
    } 
    function removeCat(id) {
        return Restangular.all(serviceUrl+"/removeCat").post(id);
    }
    function getListCode(id) {
        return Restangular.all(serviceUrl+"/getListCode").post(id);
    }
        
	}]);
