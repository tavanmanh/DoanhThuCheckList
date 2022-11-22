
angular.module('MetronicApp').factory('manageProjectIBSService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'progressPlanProjectRestService';
    var factory = {
        doSearch : doSearch,
        saveProject : saveProject,
        updateProject : updateProject,
        saveImportProject : saveImportProject,
        checkDomainUser : checkDomainUser,
        getListFile : getListFile
    };

    return factory;

    function doSearch(obj) {
        return Restangular.all(serviceUrl+"/doSearch").post(obj);
    }
    
    function saveProject(obj) {
        return Restangular.all(serviceUrl+"/saveProject").post(obj);
    }
    
    function updateProject(obj) {
        return Restangular.all(serviceUrl+"/updateProject").post(obj);
    }
    
    function saveImportProject(obj) {
        return Restangular.all(serviceUrl+"/saveImportProject").post(obj);
    }

    function checkDomainUser() {
        return Restangular.all(serviceUrl+"/checkDomainUser").post();
    }
    
    function getListFile(id) {
        return Restangular.all(serviceUrl+"/getListFile").post(id);
    }
    
}]);
