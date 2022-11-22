// picasso_2020jun02_created
angular.module('MetronicApp').factory('certificateCreateNewService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "managementCertificateRsService";
    var factory = {
        createNewCertificate: createNewCertificate,
    };
    return factory;

    function createNewCertificate(obj) {
        return Restangular.all(serviceUrl + "/create").post(obj);
    }

}]);
