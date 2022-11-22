/**
 * Created by pm1_os40 on 4/23/2018.
 */

angular.module('MetronicApp').factory('retrievalManagementService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {

    var item = {}
    var serviceUrl = 'assetManagementService';
    var factory = {
        setItem: setItem,
        getItem: getItem,
        getSequence: getSequence,
        getSequenceOrders: getSequenceOrders,
        getLstConstruction: getLstConstruction

    };

    function setItem(item) {
        this.item = item;
    }
    function getItem(item) {
        return this.item;
    }
    function getSequence() {
        return Restangular.all(serviceUrl + "/getSequence").post();
    };
    function getSequenceOrders() {
        return Restangular.all(serviceUrl + "/getSequenceOrders").post();
    };
    function getLstConstruction(obj) {
        return Restangular.all(serviceUrl + "/getLstConstruction").post(obj);
    };

    return factory;

}]);
