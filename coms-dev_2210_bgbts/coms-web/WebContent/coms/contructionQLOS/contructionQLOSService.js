
angular.module('MetronicApp').factory('contructionQLOSService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){

    var item = {}
    var factory = {
        setItem:setItem,
        getItem:getItem,
        downloadErrorExcel:downloadErrorExcel
    };

    function setItem(item) {
        this.item = item;
    }
    function getItem(item) {
        return this.item ;
    }

    return factory;

    function downloadErrorExcel(obj) {
		return Restangular.all("fileservice/exportExcelError").post(obj);
	}
}]);
