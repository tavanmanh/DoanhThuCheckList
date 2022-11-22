//Duonghv13 -start 17092021
angular.module('MetronicApp').factory('managementCertificateService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = RestEndpoint.MANAGEMENT_CERTIFICATE_SERVICE_URL;
	    var factory = {
	    	remove: remove,
	        create:create,
	        update:update,
	        doSearch : doSearch,
	        getOneCertificateDetails: getOneCertificateDetails,
	        checkRoleVHKTApprove: checkRoleVHKTApprove,
	        checkRoleCNKT: checkRoleCNKT
	        
	    };
	 
	    return factory;
	    function remove(obj) {
            return Restangular.all(serviceUrl+"/delete").post(obj); 	 
        }
	    
	    function create(obj) {
            return Restangular.all(serviceUrl+"/create").post(obj); 	 
        }
	    
	    function update(obj) {
            return Restangular.all(serviceUrl+"/update").post(obj); 	 
        }
	    	    
	    function doSearch(obj) {
            return Restangular.all(serviceUrl+"/doSearch").post(obj); 	 
        }
	    
	    function getOneCertificateDetails(obj) {
            return Restangular.all(serviceUrl+"/getOneCertificateDetails").post(obj); 	 
        }
	    
	    function checkRoleVHKTApprove(obj) {
            return Restangular.all(serviceUrl+"/checkRoleVHKTApprove").post(obj); 	 
        }
	    
	    function checkRoleCNKT(obj) {
            return Restangular.all(serviceUrl+"/checkRoleCNKT").post(obj); 	 
        }
	    //Duong-end -01102021
	          
	}]);
