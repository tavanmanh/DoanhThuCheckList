// VietNT_20181205_created
angular.module('MetronicApp').factory('reportKpi135DaysService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "rpBTSService";
    var factory = {
    		doSearchChart:doSearchChart
    };
    return factory;

    function doSearchChart(obj){
    	return Restangular.all(serviceUrl+"/doSearchChart135days").post(obj);
    }
}]);
