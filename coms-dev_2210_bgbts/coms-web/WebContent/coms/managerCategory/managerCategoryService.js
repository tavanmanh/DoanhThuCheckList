
angular.module('MetronicApp').factory('manageMEService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = 'manageMERsService';
	    var factory = {
	        doSearch : doSearch,
	        doSearchUser: doSearchUser,
	        save : save,
	        remove: remove,
	        update: update,
	        doSearchUnit: doSearchUnit
	    };
	 
	     return factory;
    
	    function doSearch(obj, options) {
	    	if(options === 0){
	    		 return Restangular.all(serviceUrl+"/userDirectory/doSearchUserDirectory").post(obj); 	 
	    	}
	    	
	    	if(options === 1){
	    		return Restangular.all(serviceUrl+"/unitList/doSearchUnitList").post(obj); 	 
	    	}
	    	if(options === 2){
	    		return Restangular.all(serviceUrl+"/documentManagement/getList").post(obj); 	 
	    	}
           
        }
	    
	    function doSearchUser(obj){
	    	return Restangular.all(serviceUrl+"/userDirectory/doSearch").post(obj);
	    }
	    
	    function doSearchUnit(obj){
	    	return Restangular.all(serviceUrl+"/unitList/doSearch").post(obj);
	    }
	    
	    function save(obj, options){
	    	if(options != null){
	    		if(options === 0){
		    		return Restangular.all(serviceUrl+"/userDirectory/save").post(obj);
		    	}
		    	
		    	if(options === 1){
		    		return Restangular.all(serviceUrl+"/unitList/save").post(obj);
		    	}
		    	
		    	if(options === 2){
		    		return Restangular.all(serviceUrl+"/documentManagement/save").post(obj);
		    	}
	    	}
	    }
	    
	    function remove(obj, options){
	    	if(options != null){
	    		if(options === 0){
	    			return Restangular.all(serviceUrl+"/userDirectory/remove").post(obj);
	    		}
	    		if(options === 1){
	    			return Restangular.all(serviceUrl+"/unitList/remove").post(obj);
	    		}
	    		if(options === 2){
	    			return Restangular.all(serviceUrl+"/documentManagement/remove").post(obj);
	    		}
	    	}
	    	
	    }
	    
	    function update(obj, options){
	    	if(options != null){
	    		if(options === 0){
	    			return Restangular.all(serviceUrl+"/userDirectory/update").post(obj);
	    		}
	    		if(options === 1){
	    			return Restangular.all(serviceUrl+"/unitList/update").post(obj);
	    		}
	    		if(options === 2){
	    			return Restangular.all(serviceUrl+"/documentManagement/update").post(obj);
	    		}
	    	}
	    }
        
	}]);
