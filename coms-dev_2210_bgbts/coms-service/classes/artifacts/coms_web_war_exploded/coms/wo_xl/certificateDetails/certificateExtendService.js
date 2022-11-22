
angular.module('MetronicApp').factory('certificateExtendService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = RestEndpoint.CERTIFICATE_EXTEND_SERVICE_URL;
	    var factory = {
	    	remove: remove,
	        create:create,
	        update: update,
	        accept:accept,
	        reject:reject,
	        doSearch : doSearch,
	        getResultFileByExtendId: getResultFileByExtendId
	        
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
	    
	    function accept(obj) {
            return Restangular.all(serviceUrl+"/accept").post(obj); 	 
        }
	    
	    function reject(obj) {
            return Restangular.all(serviceUrl+"/reject").post(obj); 	 
        }
	     
	    function doSearch(obj) {
            return Restangular.all(serviceUrl+"/doSearch").post(obj); 	 
        }
	   
	    function getResultFileByExtendId(obj) {
	        return Restangular.all(serviceUrl+"/getResultFileByExtendId").post(obj);
	    }
	         
	}]);
