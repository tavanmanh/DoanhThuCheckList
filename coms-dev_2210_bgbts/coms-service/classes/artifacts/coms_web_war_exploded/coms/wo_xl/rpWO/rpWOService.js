// picasso_2020jun9_created
angular.module('MetronicApp').factory('rpWOService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "woService";
    var factory = {
        getWOTypes: getWOTypes,
        getListCDLevel2:getListCDLevel2,
    };
    return factory;
    function getWOTypes(){
        var obj = {page:1, pageSize: 9999}
        return Restangular.all(serviceUrl+"/woType/doSearch").post(obj);
    }
     function getListCDLevel2(){
         var obj = {page:1, pageSize: 9999}
        return Restangular.all(serviceUrl+"/getCDCnkt").post(obj);
     }

}]);
