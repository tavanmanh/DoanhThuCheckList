
angular.module('MetronicApp').factory('contructionDTOSService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){

    var item = {}
    var factory = {
        setItem:setItem,
        getItem:getItem,
        downloadErrorExcel:downloadErrorExcel,
        getAllSourceWork:getAllSourceWork
    };

    function setItem(item) {
        this.item = item;
    }
    function getItem() {
        return this.item ;
    }
    function downloadErrorExcel(obj) {
		return Restangular
				.all(
						 "fileservice/exportExcelError")
				.post(obj);
	}

    return factory;

    function getAllSourceWork(parType) {
        return Restangular.all("workItemService"+"/getAllSourceWork").post(parType);
    }
    
}]);
