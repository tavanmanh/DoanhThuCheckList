
angular.module('MetronicApp').factory('managUsedMaterialService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = "manageVttbService";
	    var factory = {
	    		doSearchManageValue : doSearchManageValue,
	    		saveUsedMaterial : saveUsedMaterial,
	    		getExcelTemplate : getExcelTemplate 
	    };
	    
	     return factory;
	 
	    function doSearchManageValue(obj) {
            return Restangular.all(serviceUrl+"/doSearch").post(obj); 	 
        }
	    function saveUsedMaterial(obj) {
            return Restangular.all(serviceUrl+"/saveUsedMaterial").post(obj); 	 
        }
	    function getExcelTemplate(obj) {
            return Restangular.all(serviceUrl+"/getExcelTemplate").get(obj); 	 
        }
	    
	}]);
