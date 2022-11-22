
angular.module('MetronicApp').factory('managementCareerService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = RestEndpoint.MANAGEMENT_CAREER_SERVICE_URL;
	    var factory = {
	    	remove: remove,
	        create:create,
	        update:update,
	        doSearch : doSearch
	    };
	 
	    return factory;
	    function remove(obj) {
            return Restangular.all(serviceUrl+"/remove").post(obj); 	 
        }
	    
	    function create(obj) {
            return Restangular.all(serviceUrl+"/add").post(obj); 	 
        }
	    
	    function update(obj) {
            return Restangular.all(serviceUrl+"/update").post(obj); 	 
        }
	    
	    function doSearch(obj) {
            return Restangular.all(serviceUrl+"/doSearch").post(obj); 	 
        }
	         
	}]);
