
angular.module('MetronicApp').factory('manageDataOSService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = "manageDataOutsideOsService";
	    var factory = {
	    		doSearchOs : doSearchOs,
	    		checkRoleCNKT:checkRoleCNKT,
	    		saveAddNew:saveAddNew,
	    		saveUpdateNew:saveUpdateNew,
	    		setStatus : setStatus,
	    		downloadErrorExcel:downloadErrorExcel
//	    		exportExpertiseProposal : exportExpertiseProposal
	    };
	 
	     return factory;
	 
	    function doSearchOs(obj) {
            return Restangular.all(serviceUrl+"/doSearchOs").post(obj); 	 
        }
	    
	    function setStatus(obj) {
            return Restangular.all(serviceUrl+"/setStatus").post(obj); 	 
        }
	    
	    function checkRoleCNKT() {
            return Restangular.all(serviceUrl+"/checkRoleCNKT").post(); 	 
        }
	    
	    function saveAddNew(obj) {
            return Restangular.all(serviceUrl+"/saveAddNew").post(obj); 	 
        }
	    
	    function saveUpdateNew(obj) {
            return Restangular.all(serviceUrl+"/saveUpdateNew").post(obj); 	 
        }
	    function downloadErrorExcel(obj) {
			return Restangular
					.all(
							 "fileservice/exportExcelError")
					.post(obj);
		}
//	    function getTemplateFile(fileName) {
//            var result =  Restangular.all(serviceUrl+"/exportExpertiseProposal?").post(fileName); 	 
//            console.log(result);
//            return result;
//        }
	}]);
