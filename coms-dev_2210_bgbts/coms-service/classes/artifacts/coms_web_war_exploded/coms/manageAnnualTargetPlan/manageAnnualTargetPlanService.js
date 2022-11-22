
angular.module('MetronicApp').factory('manageAnnualTargetPlanService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = "manageAnnualTargetPlanService";
	    var factory = {
	    		doSearch : doSearch,
	    		update : update,
	    		save: save,
	    		getById: getById,
	    		remove : remove ,
	    		getExcelTemplate : getExcelTemplate ,
	    		downloadErrorExcel:downloadErrorExcel
	    };
	    
	     return factory;
	 
	    function doSearch(obj) {
            return Restangular.all(serviceUrl+"/doSearch").post(obj); 	 
        }
	    
	    function update(obj) {
            return Restangular.all(serviceUrl+"/update").post(obj); 	 
        }
	    
	    function save(obj) {
            return Restangular.all(serviceUrl+"/save").post(obj); 	 
        } 
	    
	    function getById(obj) {
            return Restangular.all(serviceUrl+"/getById").post(obj); 	 
        }
	    
	    function remove(obj) {
            return Restangular.all(serviceUrl+"/remove").post(obj); 	 
        }
	    
	    
	    function getExcelTemplate(obj) {
            return Restangular.all(serviceUrl+"/getExcelTemplate").get(obj); 	 
        }
	    function downloadErrorExcel(obj) {
			return Restangular
					.all(
							 "fileservice/exportExcelError")
					.post(obj);
		}
	}]);
