
angular.module('MetronicApp').factory('constructionCompleteManageService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
    var serviceUrl = 'constructionTaskDailyService';
    var factory = {
        doSearch : doSearch,
        downloadErrorExcel:downloadErrorExcel
    };

    return factory;

    function doSearch(obj) {
        return Restangular.all(serviceUrl+"/doSearchCompleteManage").post(obj);
    }

    function downloadErrorExcel(obj) {
		return Restangular
				.all(
						 "fileservice/exportExcelError")
				.post(obj);
	}
}]);
