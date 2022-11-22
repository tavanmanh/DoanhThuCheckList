
angular.module('MetronicApp').factory('totalMonthPlanService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'totalMonthPlanRsService';
    var factory = {
        remove:remove,
        createTotalMonthPlan:createTotalMonthPlan,
        updateTotalMonthPlan:updateTotalMonthPlan,
        doSearch : doSearch,
        getAll: getAll,
        downloadTemplate:downloadTemplate,
        getSequence:getSequence,
        exportpdf:exportpdf,
        getById:getById,
        getByIdCopy:getByIdCopy,
        saveTarget:saveTarget,
        saveSource:saveSource,
        saveForceMaintain:saveForceMaintain,
        saveForceNew:saveForceNew,
        saveForceNewBTS:saveForceNewBTS,
        saveMaterial:saveMaterial,
        saveFinance:saveFinance,
        saveContract:saveContract,
        getYearPlanDetail:getYearPlanDetail,
        exportDetailTargetTotalMonthPlan:exportDetailTargetTotalMonthPlan,
        getFileAppendixParam:getFileAppendixParam,
        saveAppendixFile:saveAppendixFile,
        doSearchForcemaintain : doSearchForcemaintain,
        doSearchSource : doSearchSource,
        doSearchTarget : doSearchTarget,
        doSearchMaterial : doSearchMaterial,
        doSearchFinance : doSearchFinance,
//      hungtd_20181213_start
        updateRegistry:updateRegistry,
//        hungtd_20181213_end
        doSearchContract : doSearchContract,
        doSearchAppendix : doSearchAppendix

    };
    return factory;
    function doSearchAppendix(obj) {
        return Restangular.all(serviceUrl+"/doSearchAppendix").post(obj);
    }
    function doSearchContract(obj) {
        return Restangular.all(serviceUrl+"/doSearchContract").post(obj);
    }
    function doSearchFinance(obj) {
        return Restangular.all(serviceUrl+"/doSearchFinance").post(obj);
    }
    function doSearchForcemaintain(obj) {
        return Restangular.all(serviceUrl+"/doSearchForcemaintain").post(obj);
    }
    function doSearchSource(obj) {
        return Restangular.all(serviceUrl+"/doSearchSource").post(obj);
    }
    function doSearchTarget(obj) {
        return Restangular.all(serviceUrl+"/doSearchTarget").post(obj);
    }
    function doSearchMaterial(obj) {
        return Restangular.all(serviceUrl+"/doSearchMaterial").post(obj);
    }
    function getFileAppendixParam() {
        return Restangular.all(serviceUrl+"/getFileAppendixParam").post();
    }
    function exportDetailTargetTotalMonthPlan(obj) {
        return Restangular.all(serviceUrl+"/exportDetailTargetTotalMonthPlan").post(obj);
    }
    function getYearPlanDetail(obj) {
        return Restangular.all(serviceUrl+"/getYearPlanDetail").post(obj);
    }
    function saveAppendixFile(obj) {
        return Restangular.all(serviceUrl+"/saveAppendixFile").post(obj);
    }
    function saveForceMaintain(obj) {
        return Restangular.all(serviceUrl+"/saveForceMaintain").post(obj);
    }
    function saveForceNew(obj) {
        return Restangular.all(serviceUrl+"/saveForceNew").post(obj);
    }
    function saveForceNewBTS(obj) {
        return Restangular.all(serviceUrl+"/saveForceNewBTS").post(obj);
    }
    function saveMaterial(obj) {
        return Restangular.all(serviceUrl+"/saveMaterial").post(obj);
    }
    function saveFinance(obj) {
        return Restangular.all(serviceUrl+"/saveFinance").post(obj);
    }
    function saveContract(obj) {
        return Restangular.all(serviceUrl+"/saveContract").post(obj);
    }
    function saveSource(obj) {
        return Restangular.all(serviceUrl+"/saveSource").post(obj);
    }
    function saveTarget(obj) {
        return Restangular.all(serviceUrl+"/saveTarget").post(obj);
    }

    function downloadTemplate(obj) {
        return Restangular
            .all(
            serviceUrl
            + "/"+obj.request)
            .post({});
    };
     function getSequence(obj) {
        return Restangular.all(serviceUrl+"/getSequence").post(obj);
    }
     function getById(id) {
        return Restangular.all(serviceUrl+"/getById").post(id);
    }
     function getByIdCopy(id) {
        return Restangular.all(serviceUrl+"/getByIdCopy").post(id);
    }
    function remove(obj) {
        return Restangular.all(serviceUrl+"/remove").post(obj);
    }
//    hungtd_20181213_start
    function updateRegistry(obj) {
        return Restangular.all(serviceUrl+"/updateRegistry").post(obj);
    }
//    hungtd_20181213_end

    function createTotalMonthPlan(obj) {
        return Restangular.all(serviceUrl+"/add").post(obj);
    }

    function updateTotalMonthPlan(obj) {
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
