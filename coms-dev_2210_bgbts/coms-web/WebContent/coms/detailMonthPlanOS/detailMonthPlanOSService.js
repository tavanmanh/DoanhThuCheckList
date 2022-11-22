
angular.module('MetronicApp').factory('detailMonthPlanOSService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'DetailMonthPlanOSRsService';
    var factory = {
        remove:remove,
        removeRow: removeRow,
        createdetailMonthPlan:createdetailMonthPlan,
        updatedetailMonthPlan:updatedetailMonthPlan,
        updateListTC:updateListTC,
        updateListHSHC:updateListHSHC,
        updateListLDT:updateListLDT,
        updateListDT:updateListDT,
        updateListCVK:updateListCVK,
        updateListDmpnOrder:updateListDmpnOrder,
        downloadTemplate:downloadTemplate,
        doSearch : doSearch,
        getAll: getAll,
        getById: getById,
        exportpdf:exportpdf,
        getYearPlanDetailTarget:getYearPlanDetailTarget,
        getForceMaintainDetail:getForceMaintainDetail,
        getWorkItemDetail:getWorkItemDetail,
		updateTC:updateTC,
        updateDT:updateDT,
        updateDTI:updateDTI,
        getSequence:getSequence,
        updateRegistry:updateRegistry,
        getRevokeCashMonthPlanByPlanId:getRevokeCashMonthPlanByPlanId,
        doSearchRent:doSearchRent
    };

    return factory;
    
    function getRevokeCashMonthPlanByPlanId(obj) {
        return Restangular.all(serviceUrl+"/getRevokeCashMonthPlanByPlanId").post(obj);
    };
    
    function getSequence() {
        return Restangular.all(serviceUrl+"/getSequence").post();
    };
    function getWorkItemDetail(obj) {
        return Restangular.all(serviceUrl+"/getWorkItemDetail").post(obj);
    };
    function getForceMaintainDetail(obj) {
        return Restangular.all(serviceUrl+"/getForceMaintainDetail").post(obj);
    };
    function updateTC(obj) {
        return Restangular.all(serviceUrl+"/updateTC").post(obj);
    }
    function updateDT(obj) {
        return Restangular.all(serviceUrl+"/updateDT").post(obj);
    }
    function updateDTI(obj) {
        return Restangular.all(serviceUrl+"/updateDTI").post(obj);
    }
    function getYearPlanDetailTarget(obj) {
        return Restangular.all(serviceUrl+"/getYearPlanDetailTarget").post(obj);
    }
    function remove(obj) {
        return Restangular.all(serviceUrl+"/remove").post(obj);
    }

    function createdetailMonthPlan(obj) {
        return Restangular.all(serviceUrl+"/add").post(obj);
    }

    function getById(id) {
        return Restangular.all(serviceUrl+"/getById").post(id);
    }

    function updatedetailMonthPlan(obj) {
        return Restangular.all(serviceUrl+"/update").post(obj);
    }

    function doSearch(obj) {
        return Restangular.all(serviceUrl+"/doSearch").post(obj);
    }

    function getAll(obj) {
        return Restangular.all(serviceUrl+"/getAll").post(obj);
    }

    function updateListTC(obj) {
        return Restangular.all(serviceUrl+"/updateListTC").post(obj);
    }

    function updateListHSHC(obj) {
        return Restangular.all(serviceUrl+"/updateListHSHC").post(obj);
    }

    function updateListLDT(obj) {
        return Restangular.all(serviceUrl+"/updateListLDT").post(obj);
    }

    function updateListDT(obj) {
        return Restangular.all(serviceUrl+"/updateListDT").post(obj);
    }

    function updateListCVK(obj) {
        return Restangular.all(serviceUrl+"/updateListCVK").post(obj);
    }

    function updateListDmpnOrder(obj) {
        return Restangular.all(serviceUrl+"/updateListDmpnOrder").post(obj);
    }

    function downloadTemplate(obj) {
        return Restangular.all(serviceUrl + "/"+obj.request).post(obj);
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
    function removeRow(obj) {
        return Restangular.all(serviceUrl+"/removeRow").post(obj);
    }
    function updateRegistry(obj) {
        return Restangular.all(serviceUrl+"/updateRegistry").post(obj);
    }

    function doSearchRent(obj) {
        return Restangular.all(serviceUrl+"/constructionTask/doSearch").post(obj);
    };
}]);
