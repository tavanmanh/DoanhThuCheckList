
angular.module('MetronicApp').factory('resultMonthPlanTTXDService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'DetailMonthPlanOSRsService';
    var factory = {
        doSearch : doSearch,
        getListAttachmentByIdAndType:getListAttachmentByIdAndType
    };

    return factory;

    function doSearch(obj) {
        return Restangular.all(serviceUrl+"/doSearch").post(obj);
    }
    
    function getListAttachmentByIdAndType(id) {
        return Restangular.all(serviceUrl+"/getListAttachmentByIdAndType").post(id);
    }

}]);
