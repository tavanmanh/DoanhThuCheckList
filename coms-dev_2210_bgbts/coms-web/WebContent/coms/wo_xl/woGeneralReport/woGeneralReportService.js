// VietNT_20181205_created
angular.module('MetronicApp').factory('woGeneralReportService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "woService";
    var factory = {
        getCdLv2List:getCdLv2List,
        getCdLv4List:getCdLv4List,
    };
    return factory;

    function getCdLv2List(obj){
        return Restangular.all(serviceUrl+"/wo/getCdLv2List").post(obj);
    }

    function getCdLv4List(obj){
        return Restangular.all(serviceUrl+"/wo/getCdLv4List").post(obj);
    }

}]);
