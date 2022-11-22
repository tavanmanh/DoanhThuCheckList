
angular.module('MetronicApp').factory('stationDetailService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = RestEndpoint.MANAGE_ME_SERVICE_URL;
	    var factory = {
	        create:create,
	        update: update,
	        doSearch : doSearch,
	        getResultFileByStationId: getResultFileByStationId,
	        getEquipments: getEquipments,
	        saveDevice: saveDevice,
	        getDeviceDetails: getDeviceDetails,
	        approve: approve,
	        reject: reject,
	        updateDevice: updateDevice,
	        saveDeviceLD: saveDeviceLD,
	        saveDeviceTuNguonAC: saveDeviceTuNguonAC,
	        saveDeviceTuNguonDC: saveDeviceTuNguonDC,
	        saveDeviceNHIET: saveDeviceNHIET,
	        saveDeviceDHAC: saveDeviceDHAC,
					updateBroken: updateBroken
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
	    function getEquipments(obj) {
	        return Restangular.all(serviceUrl+"/getEquipments").post(obj);
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
	    
	    function saveDeviceTuNguonAC(obj) {
    		return Restangular.all(serviceUrl+"/saveDeviceTuNguonAC").post(obj); 
	    }
	    
	    function saveDeviceTuNguonDC(obj) {
    		return Restangular.all(serviceUrl+"/saveDeviceTuNguonDC").post(obj); 
	    }
	    
	    function saveDeviceNHIET(obj) {
    		return Restangular.all(serviceUrl+"/saveDeviceNHIET").post(obj); 
	    }
	    
	    function saveDeviceDHAC(obj) {
    		return Restangular.all(serviceUrl+"/saveDeviceDHAC").post(obj); 
	    }
	    
	    function saveDeviceTG(obj) {
    		return Restangular.all(serviceUrl+"/saveDeviceTG").post(obj); 
	    }

		function updateBroken(obj) {
			return Restangular.all(serviceUrl+"/updateBroken").post(obj);
		}
	    
	}]);
