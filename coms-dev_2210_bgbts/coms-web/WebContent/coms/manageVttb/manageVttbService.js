
angular.module('MetronicApp').factory('manageVttbService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = "manageVttbService";
	    var factory = {
	    		doSearch : doSearch,
	    		update : update,
	    		getExcelTemplate : getExcelTemplate 
	    };
	    
	     return factory;
	 
	    function doSearch(obj) {
            return Restangular.all(serviceUrl+"/doSearch").post(obj); 	 
        }
	    function update(obj) {
            return Restangular.all(serviceUrl+"/update").post(obj); 	 
        }
	    function getExcelTemplate(obj) {
            return Restangular.all(serviceUrl+"/getExcelTemplate").get(obj); 	 
        }
	    
	}]);
