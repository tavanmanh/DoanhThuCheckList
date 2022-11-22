/**
 * Created by pm1_os49 on 5/7/2018.
 */
angular.module('MetronicApp').factory('issueService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'issueRsService';
    var factory = {
        getById:getById,
        save:save,
        getIssueDiscuss,
        getIssueHistory
    };

    return factory;
    function getIssueDiscuss(id) {
        return Restangular.all(serviceUrl+"/getIssueDiscuss").post(id);
    }
    function getIssueHistory(id) {
        return Restangular.all(serviceUrl+"/getIssueHistory").post(id);
    }
    function getById(id) {
        return Restangular.all(serviceUrl+"/getById").post(id);
    }
    function save(obj) {
        return Restangular.all(serviceUrl+"/save").post(obj);
    }
}]);