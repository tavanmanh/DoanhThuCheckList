
angular.module('MetronicApp').factory('manageHcqtService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = "manageHcqtService";
	    var factory = {
	    		doSearch : doSearch,
	    		doSearchV2 : doSearchV2,
	    		downloadErrorExcel:downloadErrorExcel
	    };
	    
	     return factory;
	 
	    function doSearch(obj) {
            return Restangular.all(serviceUrl+"/doSearch").post(obj); 	 
        }
	    function doSearchV2(obj) {
            return Restangular.all(serviceUrl+"/doSearchV2").post(obj); 	 
        }
	    
	    function downloadErrorExcel(obj) {
			return Restangular
					.all(
							 "fileservice/exportExcelError")
					.post(obj);
		}
	}]);
