
angular.module('MetronicApp').factory('stationDetailService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = RestEndpoint.MANAGE_ME_SERVICE_URL;
	    var factory = {
	        create:create,
	        update: update,
	        doSearch : doSearch,
	        getResultFileByStationId: getResultFileByStationId,
	        getInforDashboard: getInforDashboard,
	        saveDevice: saveDevice,
	        getDeviceDetails: getDeviceDetails,
	        approve: approve,
	        reject: reject,
	        saveDeviceLD: saveDeviceLD,
	        updateDevice: updateDevice,
	        exportWo: exportWo,
	        exportWo1: exportWo1,
	        checkUserKCQTD: checkUserKCQTD,
	    };
	 
	    return factory;
	    function create(obj) {
            return Restangular.all(serviceUrl+"/add").post(obj); 	 
        }
	    
	    function update(obj) {
            return Restangular.all(serviceUrl+"/update").post(obj); 	 
        }
	    function doSearch(obj) {
            return Restangular.all(serviceUrl+"/doSearch").post(obj); 	 
        }
	   
	    function getResultFileByStationId(obj) {
	        return Restangular.all(serviceUrl+"/getResultFileByStationId").post(obj);
	    }
	    function getInforDashboard(obj) {
	        return Restangular.all(serviceUrl+"/getInforDashboard").post(obj);
	    }
	    
	    function saveDevice(obj) {
            return Restangular.all(serviceUrl+"/saveDevice").post(obj); 	 
        }
	    function updateDevice(obj) {
            return Restangular.all(serviceUrl+"/updateDevice").post(obj); 	 
        }
	    
	    function getDeviceDetails(obj) {
            return Restangular.all(serviceUrl+"/getDeviceDetails").post(obj); 	 
        }
	    function approve(obj) {
            return Restangular.all(serviceUrl+"/approve").post(obj); 	 
        }
	    function reject(obj) {
            return Restangular.all(serviceUrl+"/reject").post(obj); 	 
        }
	    function saveDeviceLD(obj) {
	    		return Restangular.all(serviceUrl+"/saveDeviceLD").post(obj); 
		}
	    
	    function exportWo(obj) {
	        return Restangular.all(serviceUrl+"/wo/doSearch").post(obj);
	    }
	    
	    function exportWo1(obj) {
	        return Restangular.all(serviceUrl+"/station/doSearchDetail").post(obj);
	    }
	    
	    function checkUserKCQTD(employeeCode) {
	        return Restangular.all(serviceUrl+"/checkUserKCQTD").post(employeeCode);
	    }
	    
	    
	         
	}]);
