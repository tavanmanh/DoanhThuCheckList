angular.module('MetronicApp').factory('surveyRequestService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = RestEndpoint.COMPLAIN_ORDERS_REQUEST_SERVICE_URL;
    var factory = {
        createComplainOrder: createComplainOrder,
        choosePerformer: choosePerformer,
        extendTicket: extendTicket,
        approve: approve,
        update: update,
        checkRoleTTHTView: checkRoleTTHTView,
        checkRoleDeployTicket: checkRoleDeployTicket,
        doSearch: doSearch,
    };
    return factory;

    function approve(data) {
        return Restangular.all(serviceUrl + "/update").post(data);
    }

    function extendTicket(data) {
        return Restangular.all(serviceUrl + "/extendTicket").post(data);
    }
    
    function update(data) {
        return Restangular.all(serviceUrl + "/update").post(data);
    }

    function createComplainOrder(data) {
        return Restangular.all(serviceUrl + "/add").post(data);
    }

    function choosePerformer(obj) {
        return Restangular.all(serviceUrl + "/choosePerformer").post(obj);
    }
    
    function checkRoleTTHTView(obj) {
        return Restangular.all(serviceUrl + "/checkRoleTTHTView").post(obj);
    }
    
    function checkRoleDeployTicket(obj) {
        return Restangular.all(serviceUrl + "/checkRoleDeployTicket").post(obj);
    }
    
    function doSearch(data) {
        return Restangular.all(serviceUrl + "/doSearch").post(data);
    }



}]);
