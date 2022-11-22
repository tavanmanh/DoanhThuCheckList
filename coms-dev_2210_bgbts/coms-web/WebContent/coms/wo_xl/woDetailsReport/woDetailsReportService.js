// VietNT_20181205_created
angular.module('MetronicApp').factory('woDetailsReportService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "woService";
    var factory = {
        getCdLv2List:getCdLv2List,
        getCdLv4List:getCdLv4List,
        getFtList:getFtList
    };
    return factory;

    function getCdLv2List(obj){
        return Restangular.all(serviceUrl+"/wo/getCdLv2List").post(obj);
    }

    function getCdLv4List(obj){
        return Restangular.all(serviceUrl+"/wo/getCdLv4ListByProvince").post(obj);
    }

    function getFtList(obj){
        return Restangular.all(serviceUrl+"/wo/getFtList").post(obj);
    }

}]);
