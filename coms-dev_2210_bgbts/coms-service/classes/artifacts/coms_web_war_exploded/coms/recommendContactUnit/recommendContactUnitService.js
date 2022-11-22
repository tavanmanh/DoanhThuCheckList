
angular.module('MetronicApp').factory('recommendContactUnitService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = "recommendContactUnitService";
	    var factory = {
	    		doSearchDetailById : doSearchDetailById,
	    		doSearch : doSearch,
	    		update : update,
	    		save: save,
	    		getExcelTemplate : getExcelTemplate ,
	    		downloadErrorExcel:downloadErrorExcel,
	    		updateDescription:updateDescription,
	    		getForAutoCompleteProvince: getForAutoCompleteProvince, //HienLT56 add 01072020
	    		addContactt: addContactt
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
	    function updateDescription(obj) {
            return Restangular.all(serviceUrl+"/updateDescription").post(obj); 	 
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
	    //HienLT56 start 01072020
	    function getForAutoCompleteProvince(obj){
	    	return Restangular.all(serviceUrl+"/getForAutoCompleteProvince").post(obj);
	    }
	    function addContactt(obj){
	    	return Restangular.all(serviceUrl+"/addContactt").post(obj);
	    }
	    //HienLT56 end 01072020
	}]);
