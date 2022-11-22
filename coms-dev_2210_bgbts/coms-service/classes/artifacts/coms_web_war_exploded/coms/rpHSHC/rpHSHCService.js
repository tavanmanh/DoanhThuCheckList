
angular.module('MetronicApp').factory('rpHSHCService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){

    var item = {}
    var factory = {
        setItem:setItem,
        getItem:getItem

    };

    function setItem(item) {
        this.item = item;
    }
    function getItem(item) {
        return this.item ;
    }

    return factory;

}]);
