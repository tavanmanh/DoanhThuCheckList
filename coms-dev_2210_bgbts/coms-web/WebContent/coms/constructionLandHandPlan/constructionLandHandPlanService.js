
angular.module('MetronicApp').factory('constructionLandHandPlanService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = 'contructionLandHandoverPlan';
	    var factory = {
	        remove:remove,
	        createconstructionLandHandPlan:createconstructionLandHandPlan,
	        updateconstructionLandHandPlan:updateconstructionLandHandPlan,
	        doSearch : doSearch,
            getconstructionLandHandPlanById:getconstructionLandHandPlanById,
	        getAll: getAll,
            insertImport: insertImport,
	        exportpdf:exportpdf,
            getSequence:getSequence,
            downloadTemplate:downloadTemplate,
            getLstConstruction:getLstConstruction
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
	    function getconstructionLandHandPlanById(id) {
            return Restangular.all(serviceUrl+"/getById").post(id);
        }
	    function remove(obj) {
            return Restangular.all(serviceUrl+"/remove").post(obj);
        }

	    function createconstructionLandHandPlan(obj) {
                return Restangular.all(serviceUrl+"/add").post(obj);
        }
	    function insertImport(obj) {
                return Restangular.all(serviceUrl+"/addImport").post(obj);
        }

	    function updateconstructionLandHandPlan(obj) {
            return Restangular.all(serviceUrl+"/update").post(obj); 	 
        }
	    
	    function doSearch(obj) {
            return Restangular.all(serviceUrl+"/doSearch").post(obj); 	 
        }
	    
	    function getAll(obj) {
            return Restangular.all(serviceUrl+"/getAll").post(obj); 	 
        }
	    function getLstConstruction(code) {
            return Restangular.all("contructionLandHandoverPlan"+"/getLstConstruction").post(code);
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

	}]);
