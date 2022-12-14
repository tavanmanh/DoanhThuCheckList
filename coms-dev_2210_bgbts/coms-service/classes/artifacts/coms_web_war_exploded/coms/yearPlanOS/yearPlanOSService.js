
angular.module('MetronicApp').factory('yearPlanOSService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = 'yearPlanOSRsService';
	    var factory = {
	        remove:remove,
	        createYearPlan:createYearPlan,
	        updateYearPlan:updateYearPlan,
	        doSearch : doSearch,
            getYearPlanById:getYearPlanById,
	        getAll: getAll,
	        exportpdf:exportpdf,
            getSequence:getSequence,
            downloadTemplate:downloadTemplate,
            updateRegistry:updateRegistry
	    };
	 
	     return factory;
    function downloadTemplate(obj) {
        return Restangular
            .all(
            serviceUrl
            + "/exportExcelTemplate")
            .post(obj);
    }
    function getSequence(obj) {
        return Restangular.all(serviceUrl+"/getSequence").post(obj);
    }

	    function getYearPlanById(id) {
            return Restangular.all(serviceUrl+"/getById").post(id);
        }
	    function remove(obj) {
            return Restangular.all(serviceUrl+"/remove").post(obj);
        }

	    function createYearPlan(obj) {
            return Restangular.all(serviceUrl+"/add").post(obj); 	 
        }
	    
	    function updateYearPlan(obj) {
            return Restangular.all(serviceUrl+"/update").post(obj); 	 
        }
	    
	    function doSearch(obj) {
            return Restangular.all(serviceUrl+"/doSearch").post(obj); 	 
        }
	    
	    function getAll(obj) {
            return Restangular.all(serviceUrl+"/getAll").post(obj); 	 
        }
	    
	    function exportpdf(obj) {
	    	var deferred = $q.defer();
             Restangular.all(serviceUrl+"/exportPdf").post(obj).then(
						function(data) {
							var binarydata= new Blob([data],{ type:'application/pdf'});
					        kendo.saveAs({dataURI: binarydata, fileName: "báo cáo" + '.pdf'});
						}, function(errResponse) {
							toastr.error("Lỗi không xóa được!");
						});
             
             return deferred.promise;
        }
	    function updateRegistry(obj) {
            return Restangular.all(serviceUrl+"/updateRegistry").post(obj);
        }

	}]);
