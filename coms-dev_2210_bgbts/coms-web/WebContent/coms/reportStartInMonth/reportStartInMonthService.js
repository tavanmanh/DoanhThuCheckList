// hienvd: created 3/7/2019
angular.module('MetronicApp').factory('reportStartInMonthService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = 'assignHandoverService';
    var constructServiceUrl = 'constructionService';
    var factory = {
    		getconstructionType: getconstructionType,
    };
    return factory;

    function getconstructionType() {
        return Restangular.all(constructServiceUrl + "/construction/getCatConstructionType").post();
    }
}]);
