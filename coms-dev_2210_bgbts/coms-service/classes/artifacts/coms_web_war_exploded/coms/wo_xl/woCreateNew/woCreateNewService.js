// picasso_2020jun02_created
angular.module('MetronicApp').factory('woCreateNewService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "woService";
    var factory = {
        createNewWO: createNewWO,
        getWOTypes: getWOTypes,
        getTotalMonthPlans: getTotalMonthPlans,
        getAppWorkSrcs: getAppWorkSrcs,
        getAppConstructionTypes: getAppConstructionTypes,
        getCdLv2List: getCdLv2List,
        getCdLv3List: getCdLv3List,
        getCatWorkTypes: getCatWorkTypes,
        getAvailableTR: getAvailableTR,
        getFtList: getFtList,
        getSysUserGroup: getSysUserGroup,
        getConstructionByCode: getConstructionByCode,
        getCdLv4List: getCdLv4List,
        getConstructionByContract: getConstructionByContract,
        getStationById: getStationById,
        getAIOWoInfo: getAIOWoInfo,
        getConstructionByProject: getConstructionByProject,
        getVhktCdLv2VList: getVhktCdLv2VList,
        autoSuggestMoneyValue: autoSuggestMoneyValue,
        getFtListCdLevel5: getFtListCdLevel5,
        getListCdLevel5: getListCdLevel5,
        getTcTctEmails: getTcTctEmails,
        getAppWorkSource: getAppWorkSource,
    };
    return factory;

    function createNewWO(obj) {
        return Restangular.all(serviceUrl + "/wo/createWO").post(obj);
    }

    function getWOTypes() {
        var obj = {page: 1, pageSize: 9999}
        return Restangular.all(serviceUrl + "/woType/doSearch").post(obj);
    }

    function getTotalMonthPlans() {
        return Restangular.all(serviceUrl + "/wo/getCurrentMonthPlan").post({});
    }

    function getAppWorkSrcs() {
        return Restangular.all(serviceUrl + "/wo/getAppWorkSrcs").post({});
    }

    function getAppConstructionTypes() {
        return Restangular.all(serviceUrl + "/wo/getAppConstructionTypes").post({});
    }

    function getCdLv1List(obj) {
        return Restangular.all(serviceUrl + "/wo/getCdLv1List").post(obj);
    }

    function getCdLv2List(obj) {
        return Restangular.all(serviceUrl + "/wo/getCdLv2List").post(obj);
    }

    function getCdLv3List(obj) {
        return Restangular.all(serviceUrl + "/wo/getCdLv3List").post(obj);
    }

    function getCdLv4List(obj) {
        return Restangular.all(serviceUrl + "/wo/getCdLv4List").post(obj);
    }

    function getCatWorkTypes(obj) {
        return Restangular.all(serviceUrl + "/wo/getCatWorkTypes").post(obj);
    }

    function getAvailableTR(obj) {
        return Restangular.all("trService/tr/doSearchAvailable").post(obj);
    }

    function getFtList(obj) {
        return Restangular.all(serviceUrl + "/wo/getFtList").post(obj);
    }

    function getSysUserGroup(obj) {
        return Restangular.all(serviceUrl + "/user/getSysUserGroup").post(obj);
    }

    function getConstructionByCode(obj) {
        return Restangular.all("trService/tr/getConstructionByCode").post(obj);
    }

    function getConstructionByContract(obj) {
        return Restangular.all("trService/tr/getConstructionByContract").post(obj);
    }

    function getStationById(obj) {
        return Restangular.all("trService/tr/getStationById").post(obj);
    }

    function getAIOWoInfo(obj) {
        return Restangular.all("woService/wo/getAIOWoInfo").post(obj);
    }

    function getConstructionByProject(obj) {
        return Restangular.all("trService/tr/getConstructionByProject").post(obj);
    }

    function getVhktCdLv2VList(obj) {
        return Restangular.all(serviceUrl + "/wo/getVhktCdLv2VList").post(obj);
    }

    function autoSuggestMoneyValue(obj) {
        return Restangular.all(serviceUrl + "/hshc/autoSuggestMoneyValue").post(obj);
    }

    function getFtListCdLevel5(obj) {
        return Restangular.all(serviceUrl + "/wo/getFtListCdLevel5").post(obj);
    }

    function getListCdLevel5(obj) {
        return Restangular.all("trService/woConfigContract/getListCD5ByContract").post(obj);
    }

    function getTcTctEmails(obj) {
        return Restangular.all(serviceUrl + "/tc/getTcTctEmails").post(obj);
    }
//    taotq start 23082021
    function getAppWorkSource() {
        return Restangular.all(serviceUrl + "/wo/getAppWorkSource").post({});
    }
//  taotq end 23082021
}]);
