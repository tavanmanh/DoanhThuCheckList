/**
 * Created by pm1_os40 on 4/23/2018.
 */

angular.module('MetronicApp').factory('manufacturingVTService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {

    var item = {}
    var serviceUrl = 'assetManagementService';
    var factory = {
        setItem: setItem,
        getItem: getItem,
        getSequence: getSequence,
        getSequenceOrders: getSequenceOrders,
        getLstConstruction: getLstConstruction,
        createYearPlan:createYearPlan,
        updateYearPlan:updateYearPlan,
        exportFileCons:exportFileCons,
        openYearPlan:openYearPlan,
    };

    function setItem(item) {
        this.item = item;
    }
    function getItem(item) {
        return this.item;
    }
    function getSequence() {
        return Restangular.all(serviceUrl + "/getSequence").post();
    };
    function getSequenceOrders() {
        return Restangular.all(serviceUrl + "/getSequenceOrders").post();
    };
    function getLstConstruction(obj) {
        return Restangular.all(serviceUrl + "/getLstConstruction").post(obj);
    };
    function createYearPlan(obj) {
        return Restangular.all(serviceUrl + "/addVT").post(obj);
    };
    function exportFileCons(obj) {
        return Restangular.all(serviceUrl + "/doSearchVT").post(obj);
    };
    function updateYearPlan(obj) {
        return Restangular.all(serviceUrl + "/updateVT").post(obj);
    };
    function openYearPlan(obj) {
        return Restangular.all(serviceUrl + "/openVT").post(obj);
    };
    
    return factory;

}]);
//angular.module('MetronicApp').factory('manufacturingVTService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
// 	var serviceUrl = RestEndpoint.MANUFACTURING_VT_SERVICE_URL;
//    var factory = {
//        remove:remove,
//        createAppLogin:createAppLogin,
//        updateAppLogin:updateAppLogin,
//        doSearch : doSearch,
//        getAll: getAll,
//        exportpdf:exportpdf
//    };
// 
//     return factory;
// 
//    function remove(obj) {
//        return Restangular.all(serviceUrl+"/remove").post(obj); 	 
//    }
//    
//    function createAppLogin(obj) {
//        return Restangular.all(serviceUrl+"/add").post(obj); 	 
//    }
//    
//    function updateAppLogin(obj) {
//        return Restangular.all(serviceUrl+"/update").post(obj); 	 
//    }
//    
//    function doSearch(obj) {
//        return Restangular.all(serviceUrl+"/doSearch").post(obj); 	 
//    }
//    
//    function getAll(obj) {
//        return Restangular.all(serviceUrl+"/getAll").post(obj); 	 
//    }
//    
//    function exportpdf(obj) {
//    	var deferred = $q.defer();
//         Restangular.all(serviceUrl+"/exportPdf").post(obj).then(
//					function(data) {
//						var binarydata= new Blob([data],{ type:'application/pdf'});
//				        kendo.saveAs({dataURI: binarydata, fileName: "báo cáo" + '.pdf'});
//					}, function(errResponse) {
//						toastr.error("Lỗi không xóa được!");
//					});
//         
//         return deferred.promise;
//    }
//
//}]);

