
angular.module('MetronicApp').factory('effectiveCalculateService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrlDas = RestEndpoint.EFFECTIVE_CALCULATE_DAS_SERVICE_URL;
	 	var serviceUrlDasCapex = "effectiveCalculateDasCapexRestService";
	    var factory = {	    	
	    	getAssumptions: getAssumptions,
	        getAssumptionsCapex : getAssumptionsCapex,      
	        downloadErrorExcel: downloadErrorExcel
	    };
	 
	     return factory;
	     
	    function getAssumptions(obj) {
            return Restangular.all(serviceUrlDas+"/getAssumptions").post(obj); 	 
        };
	    
	    function getAssumptionsCapex(obj) {
            return Restangular.all(serviceUrlDasCapex+"/getAssumptionsCapex").post(obj); 	 
        };
	    
        function downloadErrorExcel(obj) {
			return Restangular
					.all(
							 "fileservice/exportExcelError")
					.post(obj);
		}
	   	   	
	}]);
