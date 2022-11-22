
angular.module('MetronicApp').factory('quantityService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'completedWorkRsService';
    var factory = {
        saveCancelConfirmPopup:saveCancelConfirmPopup,
        doSearch : doSearch,
        updateYearPlan:updateYearPlan,
        getAll: getAll,
        exportpdf:exportpdf,
        downloadTemplate:downloadTemplate,
        getListImage:getListImage,
        getWorkItemById:getWorkItemById,
        rejectTaskDaily: rejectTaskDaily,
        validPriceConstruction: validPriceConstruction,
        getDetailTaskDaily: getDetailTaskDaily,
    };

    return factory;
    function downloadTemplate(obj) {
        return Restangular
            .all(
            serviceUrl
            + "/exportExcelTemplate")
            .post(obj);
    }

    function getListImage(obj) {
        return Restangular.all(serviceUrl+"/getListImage").post(obj);
    }
    function getWorkItemById(id) {
        return Restangular.all(serviceUrl+"/getById").post(id);
    }
    function saveCancelConfirmPopup(obj) {
        return Restangular.all(serviceUrl+"/cancelApproveQuantityByDay").post(obj);
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
    function rejectTaskDaily(obj) {
    	return Restangular.all(serviceUrl+"/rejectQuantityByDay").post(obj);
	}
    function validPriceConstruction(obj) {
    	return Restangular.all(serviceUrl+"/validPriceConstruction").post(obj);
	}
    function getDetailTaskDaily(obj) {
    	return Restangular.all(serviceUrl+"/getDetailTaskDaily").post(obj);
	}
}]);
