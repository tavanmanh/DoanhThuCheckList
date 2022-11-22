angular.module('MetronicApp').factory('rpGeneralPaymentCtvService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'rpBTSService';
    var factory = {
        exportRpGeneralPaymentCtv:exportRpGeneralPaymentCtv
    };

    return factory;

    function exportRpGeneralPaymentCtv(obj) {
        return Restangular.all(serviceUrl+"/exportRpGeneralPaymentCtv").post(obj);
    }
    
}]);