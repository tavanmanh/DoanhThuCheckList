
angular.module('MetronicApp').factory('rpRevenueService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){

    var item = {}
    var factory = {
        setItem:setItem,
        getItem:getItem

    };

    function setItem(item) {
        this.item = item;
    }
    function getItem() {
        return this.item ;
    }

    return factory;

}]);
