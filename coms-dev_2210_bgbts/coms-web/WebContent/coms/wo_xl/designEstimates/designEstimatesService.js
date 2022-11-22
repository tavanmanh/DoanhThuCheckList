// picasso_2020jun9_created
angular.module('MetronicApp').factory('designEstimatesService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "designEstimatesService";
    var factory = {
    	save: save,
    	update: update,
    	remove: remove,
        
    };
    return factory;
    
    function save(obj){
        return Restangular.all(serviceUrl+"/save").post(obj);
    }
    function update(obj){
        return Restangular.all(serviceUrl+"/update").post(obj);
    }
    function doSearch(obj){
        return Restangular.all(serviceUrl+"/tr/doSearch").post(obj);
    }
    
    function remove(obj){
        return Restangular.all(serviceUrl+"/delete").post(obj);
    }

}]);
