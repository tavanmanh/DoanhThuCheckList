// VietNT_20181205_created
angular.module('MetronicApp').factory('reportKpi60DaysService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "rpBTSService";
    var factory = {
    		doSearchChart:doSearchChart
    };
    return factory;

    function doSearchChart(obj){
    	return Restangular.all(serviceUrl+"/doSearchChart60days").post(obj);
    }
}]);
