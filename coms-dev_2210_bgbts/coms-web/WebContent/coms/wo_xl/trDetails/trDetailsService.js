// picasso_2020jun02_created
angular.module('MetronicApp').factory('trDetailsService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "trService";
    var factory = {
        getOneTRDetails: getOneTRDetails,
        uploadAttachment: uploadAttachment,
        mappingAttachmentToTR: mappingAttachmentToTR,
        removeAtachment: removeAtachment,
        doSearchAtachment: doSearchAtachment,
        getSysUserGroup: getSysUserGroup,
        getOneWODetails: getOneWODetails,
        updateWO: updateWO,
        deleteWO: deleteWO,
        changeStateTr: changeStateTr,
        getCdLevel1: getCdLevel1,
        giveAssignment: giveAssignment,
        getAIOWoInfo: getAIOWoInfo,
        createNewWO: createNewWO,
        removeInactiveWo: removeInactiveWo,
        createNewTmbtWO: createNewTmbtWO,
        createNewDbhtWO: createNewDbhtWO,
    };
    return factory;

    function getOneTRDetails(obj) {
        return Restangular.all(serviceUrl + "/tr/getOneTRDetails").post(obj);
    }

    function uploadAttachment(apiEndpoint, formData) {
        return $http.post(apiEndpoint, formData, {
            transformRequest: angular.identity,
            headers: {'Content-Type': 'multipart/form-data'}
        });
    }

    function mappingAttachmentToTR(obj) {
        return Restangular.all("woService/fileAttach/createWOMappingAttach").post(obj);
    }

    function doSearchAtachment(obj) {
        return Restangular.all("woService/fileAttach/doSearch").post(obj);
    }

    function removeAtachment(obj) {
        return Restangular.all("woService/fileAttach/deleteWOMappingAttach").post(obj);
    }

    function getSysUserGroup(obj) {
        return Restangular.all("woService/user/getSysUserGroup").post(obj);
    }

    function getOneWODetails(obj) {
        return Restangular.all("woService/wo/getOneWODetails").post(obj);
    }

    function deleteWO(obj) {
        return Restangular.all("woService/wo/deleteWO").post(obj);
    }

    function updateWO(obj) {
        return Restangular.all("woService/wo/updateWO").post(obj);
    }

    function changeStateTr(obj) {
        return Restangular.all(serviceUrl + "/tr/changeStateTr").post(obj);
    }

    function getCdLevel1(obj) {
        return Restangular.all(serviceUrl + "/tr/getCdLevel1").post(obj);
    }

    function giveAssignment(obj) {
        return Restangular.all(serviceUrl + "/tr/giveAssignmentToCD").post(obj);
    }

    function getAIOWoInfo(obj) {
        return Restangular.all("woService/wo/getAIOWoInfo").post(obj);
    }

    function createNewWO(obj) {
        return Restangular.all("woService/wo/createWO").post(obj);
    }

    function removeInactiveWo(obj) {
        return Restangular.all("woService/wo/removeInactiveWo").post(obj);
    }

    function createNewTmbtWO(obj) {
        return Restangular.all("woService/wo/createNewTmbtWO").post(obj);
    }

    function createNewDbhtWO(obj) {
        return Restangular.all("woService/wo/createNewDbhtWO").post(obj);
    }
}]);
