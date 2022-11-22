
angular.module('MetronicApp').factory('manageMEService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = 'manageMERsService';
	    var factory = {
	        doSearch : doSearch,
	    };
	 
	     return factory;
    
	    function doSearch(obj) {
            return Restangular.all(serviceUrl+"/getManager").post(obj); 	 
        }
	    
	}]);
