
angular.module('MetronicApp').factory('recommendContactUnitLibraryService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = "recommendContactUnitService";
	    var factory = {
	    		doSearchDetailById : doSearchDetailById,
	    		doSearch : doSearch,
	    		update : update,
	    		save: save,
	    		getExcelTemplate : getExcelTemplate ,
	    		downloadErrorExcel:downloadErrorExcel
	    };
	    
	     return factory;
	 
	    function doSearchDetailById(obj) {
            return Restangular.all(serviceUrl+"/doSearchDetailById").post(obj); 	 
        }
	    function doSearch(obj) {
            return Restangular.all(serviceUrl+"/doSearch").post(obj); 	 
        }
	    
	    function update(obj) {
            return Restangular.all(serviceUrl+"/update").post(obj); 	 
        }
	    
	    function save(obj) {
            return Restangular.all(serviceUrl+"/save").post(obj); 	 
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
