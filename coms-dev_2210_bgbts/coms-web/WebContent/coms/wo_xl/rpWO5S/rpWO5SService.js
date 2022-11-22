// picasso_2020jun9_created
angular.module('MetronicApp').factory('rpWO5SService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "woService";
    var factory = {
        getCdLv2List:getCdLv2List,
        getVhktCdLv2VList:getVhktCdLv2VList,
        getWOTypes: getWOTypes,
    };
    return factory;
    function getWOTypes(){
        var obj = {page:1, pageSize: 9999}
        return Restangular.all(serviceUrl+"/woType/doSearch").post(obj);
    }

    function getCdLv2List(obj){
        return Restangular.all(serviceUrl+"/wo/getCdLv2List").post(obj);
    }

    function getVhktCdLv2VList(obj) {
        return Restangular.all(serviceUrl+"/wo/getCdLv2List").post(obj);
    }

}]);
