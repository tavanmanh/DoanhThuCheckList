
angular.module('MetronicApp').factory('rpQuantityService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'rpQuantityService';
    var factory = {
        saveCancelConfirmPopup:saveCancelConfirmPopup,
        doSearch : doSearch,
    //  hungtd_20181220_start   
        doSearchNHAN : doSearchNHAN,
        doSearchKC : doSearchKC,
        doSearchTONTC:doSearchTONTC,
        doSearchHSHC:doSearchHSHC,
    //  hungtd_20181220_end
//        hungtd_20181228_start
        doSearchVT:doSearchVT,
//        hungtd_20181228_end
        
//        hungtd_20192101_start
        doSearchCoupon:doSearchCoupon,

        doSearchSysPXK:doSearchSysPXK,
//        hungtd_20192101_end
        
        updateYearPlan:updateYearPlan,
        getAll: getAll,
        exportpdf:exportpdf,
        downloadTemplate:downloadTemplate,
        getListImageById:getListImageById,
        getListImageWorkItemId:getListImageWorkItemId,
        getWorkItemById:getWorkItemById,
        //hienvd: START 29/7/2019
        doSearchSysPXK60:doSearchSysPXK60,
        //hienvd: End
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
//    hungtd_20192101_start
    function doSearchCoupon(obj) {
        return Restangular.all(serviceUrl+"/doSearchCoupon").post(obj);
    }
//    hungtd_20192101_end
    
//    hungtd_20181220_start
    function doSearchNHAN(obj) {
        return Restangular.all(serviceUrl+"/doSearchNHAN").post(obj);
    }
 
    function doSearchKC(obj) {
        return Restangular.all(serviceUrl+"/doSearchKC").post(obj);
    }
    function doSearchTONTC(obj) {
        return Restangular.all(serviceUrl+"/doSearchTONTC").post(obj);
    }
    function doSearchHSHC(obj) {
        return Restangular.all(serviceUrl+"/doSearchHSHC").post(obj);
    }
//  hungtd_20181220_end
//    hungtd_20181228_start
    function doSearchVT(obj) {
        return Restangular.all(serviceUrl+"/doSearchVT").post(obj);
    }
//    hungtd_20181228_end
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
    //hienvd: START 29/7/2019
    function doSearchSysPXK(obj) {
        return Restangular.all(serviceUrl+"/doSearchSysPXK").post(obj);
    }


    function doSearchSysPXK60(obj) {
        return Restangular.all(serviceUrl+"/doSearchSysPXK60").post(obj);
    }
    //hienvd: END

}]);
