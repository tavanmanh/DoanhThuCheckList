
angular.module('MetronicApp').factory('manageValueService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = "DetailMonthPlanOSRsService";
	    var factory = {
	    		doSearchManageValue : doSearchManageValue,
	    		updateManageValue : updateManageValue,
	    		approve : approve ,
	    		downloadErrorExcel:downloadErrorExcel,
	    		checkRoleTTHT:checkRoleTTHT,
	    		updateRejectRevokeCash:updateRejectRevokeCash
	    };
	    
	     return factory;
	 
	    function doSearchManageValue(obj) {
            return Restangular.all(serviceUrl+"/doSearchManageValue").post(obj); 	 
        }
	    
	    function approve(obj) {
            return Restangular.all(serviceUrl+"/approveRevokeCashMonthPlan").post(obj); 	 
        }
	    function updateManageValue(obj) {
            return Restangular.all(serviceUrl+"/updateRevokeCashMonthPlan").post(obj); 	 
        }
	    
	    function downloadErrorExcel(obj) {
			return Restangular.all("fileservice/exportExcelError").post(obj);
		}
	    function checkRoleTTHT() {
            return Restangular.all(serviceUrl+"/checkRoleTTHT").post(); 	 
        }
	    function updateRejectRevokeCash(obj) {
            return Restangular.all(serviceUrl+"/updateRejectRevokeCash").post(obj); 	 
        }
	}]);
