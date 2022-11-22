
angular.module('MetronicApp').factory('manageMEService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = 'manageMERsService';
	    var factory = {
	        doSearch : doSearch,
	        saveManageStation: saveManageStation,

	    };
	 
	     return factory;
    
	    function doSearch(obj) {
            return Restangular.all(serviceUrl+"/history/getUpdateElectric").post(obj); 	 
        }
	    
	    function saveManageStation(obj) {
            return Restangular.all(serviceUrl+"/saveManageStation").post(obj); 	 
        }
        
	}]);
