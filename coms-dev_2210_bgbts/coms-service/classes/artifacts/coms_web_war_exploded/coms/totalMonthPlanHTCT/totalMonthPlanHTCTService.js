
angular.module('MetronicApp').factory('totalMonthPlanHTCTService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = RestEndpoint.TOTAL_MONTH_PLAN_RENTAL_INFRASTRUCTURE_SERVICE_URL;
	    var factory = {
	        remove:remove,
	        createReport:createReport,
	        updateReport:updateReport,
	        doSearch : doSearch,
	        exportPDFReport: exportPDFReport,
	        downloadErrorExcel: downloadErrorExcel,
	        checkRoleCreateMonthPlan: checkRoleCreateMonthPlan
	    };
	 
	     return factory;
	 
	    function remove(obj) {
            return Restangular.all(serviceUrl+"/remove").post(obj); 	 
        }
	    
	    function createReport(obj) {
            return Restangular.all(serviceUrl+"/add").post(obj); 	 
        }
	    
	    function updateReport(obj) {
            return Restangular.all(serviceUrl+"/update").post(obj); 	 
        }
	    
	    function doSearch(obj) {
            return Restangular.all(serviceUrl+"/doSearch").post(obj); 	 
        }
	       
	    function exportPDFReport(obj){
	    	 return Restangular.all(serviceUrl+"/exportPDFReport").post(obj);
	    }
	   
	    function downloadErrorExcel(obj) {
			return Restangular
				.all(
					"fileservice/exportExcelError")
				.post(obj);
		}
	    
	  //Duonghv13 start 17092021
	    function checkRoleCreateMonthPlan(obj) {
	        return Restangular.all(serviceUrl + "/checkRoleCreateMonthPlan").post(obj);
	    }
	    //Duong end//

	    
	}]);
