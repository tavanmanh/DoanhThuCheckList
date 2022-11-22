
angular.module('MetronicApp').factory('RP_TON_HSHCService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'RP_TON_HSHCService';
    var factory = {
        saveCancelConfirmPopup:saveCancelConfirmPopup,
        doSearch : doSearch,
        updateYearPlan:updateYearPlan,
        getAll: getAll,
        exportpdf:exportpdf,
        downloadTemplate:downloadTemplate,
        getListImageById:getListImageById,
        getListImageWorkItemId:getListImageWorkItemId,
        getWorkItemById:getWorkItemById
    };

    return factory;
    function downloadTemplate(obj) {
        return Restangular
            .all(
            serviceUrl
            + "/exportExcelTemplate")
            .post(obj);
    }

    function getListImageById(obj) {
        return Restangular.all(serviceUrl+"/getListImageById").post(obj);
    }
    function getListImageWorkItemId(id) {
        return Restangular.all(serviceUrl+"/getListImageWorkItemId").post(id);
    }
    function getWorkItemById(id) {
        return Restangular.all(serviceUrl+"/getById").post(id);
    }
    function saveCancelConfirmPopup(obj) {
        return Restangular.all(serviceUrl+"/saveCancelConfirmPopup").post(obj);
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

}]);
