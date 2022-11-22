/**
 * Created by pm1_os40 on 4/23/2018.
 */

angular.module('MetronicApp').factory('goodsPlanService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {

    var item = {}
    var serviceUrl = 'assetManagementService';
    var factory = {
        setItem: setItem,
        getItem: getItem,
        getSequence: getSequence,
        getSequenceOrders: getSequenceOrders,
        getLstConstruction: getLstConstruction,
        createYearPlan:createYearPlan,
        updateGoodsPlan:updateGoodsPlan,
        exportFileCons:exportFileCons,
        openYearPlan:openYearPlan,
        doSearch:doSearch,
        doSearchReqGoodsDetail:doSearchReqGoodsDetail,
        doSearchAll:doSearchAll,
        remove:remove,
        signVofficeKHSXVT:signVofficeKHSXVT,
        genDataContract:genDataContract
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
        return Restangular.all("goodsPlanService/add").post(obj);
    };
    function exportFileCons(obj) {
        return Restangular.all(serviceUrl + "/doSearchVT").post(obj);
    };
    function updateGoodsPlan(obj) {
        return Restangular.all("goodsPlanService/update").post(obj);
    };
    function remove(obj) {
        return Restangular.all("goodsPlanService/remove").post(obj);
    };
    function openYearPlan(obj) {
        return Restangular.all(serviceUrl + "/openVT").post(obj);
    };
    function doSearch(obj) {
        return Restangular.all("goodsPlanService/doSearch").post(obj);
    };
    function doSearchReqGoodsDetail(obj) {
        return Restangular.all("goodsPlanService/doSearchReqGoodsDetail").post(obj);
    };
    function doSearchAll(obj) {
        return Restangular.all("goodsPlanService/doSearchAll").post(obj);
    };
    function signVofficeKHSXVT(List) {
        return Restangular.all("reportServiceRest/signVofficeKHSXVT").post(List); 	 
    };
    
    function genDataContract(obj) {
        return Restangular.all("goodsPlanService/genDataContract").post(obj);
    };
    return factory;

}]);

