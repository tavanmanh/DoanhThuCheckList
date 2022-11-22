// picasso_2020jun02_created
angular.module('MetronicApp').factory('woDetailsService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = "woService";
    var factory = {
        getOneWODetails: getOneWODetails,
        uploadAttachment: uploadAttachment,
        removeAtachment: removeAtachment,
        doSearchAtachment: doSearchAtachment,
        mappingAttachmentToWo: mappingAttachmentToWo,
        giveAssignment: giveAssignment,
        getSysUserGroup: getSysUserGroup,
        acceptWO: acceptWO,
        rejectWO: rejectWO,
        downloadAttachment: downloadAttachment,
        processOpinion: processOpinion,
        downloadAttachment: downloadAttachment,
        changeStateOk: changeStateOk,
        changeStateNotGood: changeStateNotGood,
        checkWoCompleteToUpdateTr: checkWoCompleteToUpdateTr,
        checkGpon: checkGpon,
        getCheckListNeedAdd: getCheckListNeedAdd,
        addCheckList: addCheckList,
        deleteCheckList: deleteCheckList,
        getCatWorkName: getCatWorkName,
        changeStateCdOk: changeStateCdOk,
        changeStateCdNg: changeStateCdNg,
        acceptQuantityLength: acceptQuantityLength,
        completeHcqtChecklist: completeHcqtChecklist,
        uploadHcqtImage: uploadHcqtImage,
        getHcqtIssueList: getHcqtIssueList,
        declareHcqtIssue: declareHcqtIssue,
        resolveHcqtIssue: resolveHcqtIssue,
        getHcqtProject: getHcqtProject,
        acceptXdddValue: acceptXdddValue,
        rejectXdddValue: rejectXdddValue,
        //Huypq-04092020-start
        checkRoleApproveHshc: checkRoleApproveHshc,
        //Huy-end
        getCheckViewHcqt: getCheckViewHcqt,  //Huypq-02112020-add
        acceptQuantityByDate: acceptQuantityByDate,
        rejectQuantityByDate: rejectQuantityByDate,
        extendFinishDate: extendFinishDate,
        changeStateAndAcceptTcTct: changeStateAndAcceptTcTct,
        rejectQuantityLength: rejectQuantityLength,
        postOverdueReason: postOverdueReason,
        getWoOverdueReason: getWoOverdueReason,
        acceptRejectOverdueReason: acceptRejectOverdueReason,
        getTcTctEmails: getTcTctEmails,
        completeTkdtChecklist: completeTkdtChecklist,
        approvedOkWoTkdt: approvedOkWoTkdt,
        getDataTableTTTHQ: getDataTableTTTHQ,
        approvedWoOkTthq: approvedWoOkTthq,
        checkRoleTTHT: checkRoleTTHT, //HienLT56 add 27052021
        getAppWorkSrcs: getAppWorkSrcs, //HienLT56 add 27052021
        getApConstructionTypes:getApConstructionTypes, //HienLT56 add 27052021
        saveChangeForTTHT: saveChangeForTTHT, //HienLT56 add 27052021
        getAppWorkSource: getAppWorkSource, //Taotq add
        createFileCheckList: createFileCheckList,
        //Huypq-22102021-start
        changeStateWaitPqt:changeStateWaitPqt,
        changeStateWaitTtDtht:changeStateWaitTtDtht,
        changeStateWaitTcTct:changeStateWaitTcTct,
        changeStateCdPause:changeStateCdPause,
        changeStateTthtPause:changeStateTthtPause,
        changeStateDthtPause:changeStateDthtPause,
        getDataConstructionContractByStationCode:getDataConstructionContractByStationCode,
        changeStateCdPauseReject:changeStateCdPauseReject,
        changeStateTthtPauseReject:changeStateTthtPauseReject,
        changeStateDthtPauseReject:changeStateDthtPauseReject,
        //Huy-end
        checkRoleCDPKTCNKT: checkRoleCDPKTCNKT,//Duonghv13 add
        checkConditionCertificate: checkConditionCertificate //Duonghv13 add 12102021
        ,changeStateApprovedOrRejectWoHtctPQT:changeStateApprovedOrRejectWoHtctPQT
        ,changeStateApprovedOrRejectWoHtctTtDtht:changeStateApprovedOrRejectWoHtctTtDtht
        ,changeStateWoReject:changeStateWoReject
        ,changeStateReProcessWoDoanhThuDTHT:changeStateReProcessWoDoanhThuDTHT,
        checkRoleApproveCDLV5: checkRoleApproveCDLV5,
        checkContractIsGpxd: checkContractIsGpxd,
        checkAsignAdminWo: checkAsignAdminWo,
        getChecklistByParType: getChecklistByParType, // ThanhPT - WO BGBTS_DTHT
        rejectWoBgbtsVhkt: rejectWoBgbtsVhkt, // ThanhPT - WO BGBTS_VHKT
    };
    return factory;

    function getOneWODetails(obj) {
        return Restangular.all(serviceUrl + "/wo/getOneWODetails").post(obj);
    }

    function uploadAttachment(apiEndpoint, formData) {
        return $http.post(apiEndpoint, formData, {
            transformRequest: angular.identity,
            headers: {'Content-Type': 'multipart/form-data'}
        });
    }

    function downloadAttachment(apiEndpoint, fileName) {
        return $http.get(apiEndpoint, {
            transformRequest: angular.identity,
            headers: {'Content-Type': 'multipart/form-data', 'Content-Disposition': 'attachment; filename=' + fileName}
        })
    }

    function removeAtachment(obj) {
        return Restangular.all(serviceUrl + "/fileAttach/deleteWOMappingAttach").post(obj);
    }

    function doSearchAtachment(obj) {
        return Restangular.all(serviceUrl + "/fileAttach/doSearch").post(obj);
    }

    function mappingAttachmentToWo(obj) {
        return Restangular.all(serviceUrl + "/fileAttach/createWOMappingAttach").post(obj);
    }

    function giveAssignment(obj) {
        return Restangular.all(serviceUrl + "/wo/giveWOAssignment").post(obj);
    }

    function getSysUserGroup(obj) {
        return Restangular.all(serviceUrl + "/user/getSysUserGroup").post(obj);
    }

    function acceptWO(obj) {
        return Restangular.all(serviceUrl + "/wo/acceptWO").post(obj);
    }

    function rejectWO(obj) {
        return Restangular.all(serviceUrl + "/wo/rejectWO").post(obj);
    }

    function processOpinion(obj) {
        return Restangular.all(serviceUrl + "/wo/processOpinion").post(obj);
    }

    function checkWoCompleteToUpdateTr(obj) {
        return Restangular.all(serviceUrl + "/wo/checkWoCompleteToUpdateTr").post(obj);
    }

    function changeStateCdOk(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateCdOk").post(obj);
    }

    function changeStateCdNg(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateCdNg").post(obj);
    }

    function changeStateOk(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateOk").post(obj);
    }

    function changeStateNotGood(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateNg").post(obj);
    }

    function checkGpon(obj) {
        return Restangular.all(serviceUrl + "/wo/checkGpon").post(obj);
    }

    function getCheckListNeedAdd(obj) {
        return Restangular.all(serviceUrl + "/wo/getCheckListNeedAdd").post(obj);
    }

    function addCheckList(obj) {
        return Restangular.all(serviceUrl + "/wo/addCheckList").post(obj);
    }

    function deleteCheckList(obj) {
        return Restangular.all(serviceUrl + "/wo/deleteCheckList").post(obj);
    }

    function getCatWorkName(obj) {
        return Restangular.all(serviceUrl + "/wo/getCatWorkName").post(obj);
    }

    function acceptQuantityLength(obj) {
        return Restangular.all(serviceUrl + "/checklist/acceptQuantity").post(obj);
    }

    function rejectQuantityLength(obj) {
        return Restangular.all(serviceUrl + "/checklist/rejectQuantity").post(obj);
    }

    function completeHcqtChecklist(obj) {
        return Restangular.all(serviceUrl + "/hcqtChecklist/completeChecklist").post(obj);
    }

    function uploadHcqtImage(obj) {
        return Restangular.all(serviceUrl + "/hcqtChecklist/addImage").post(obj);
    }

    function getHcqtIssueList(obj) {
        return Restangular.all(serviceUrl + "/hcqtChecklist/getHcqtIssueList").post(obj);
    }

    function declareHcqtIssue(obj) {
        return Restangular.all(serviceUrl + "/hcqtChecklist/declareHcqtIssue").post(obj);
    }

    function resolveHcqtIssue(obj) {
        return Restangular.all(serviceUrl + "/hcqtChecklist/resolveHcqtIssue").post(obj);
    }

    //Huypq-04092020-start
    function checkRoleApproveHshc(obj) {
        return Restangular.all(serviceUrl + "/wo/checkRoleApproveHshc").post(obj);
    }

    //Huy-end

    function getHcqtProject(obj) {
        return Restangular.all("woService/hcqtProject/getHcqtProject").post(obj);
    }

    //Huypq-02112020-start
    function getCheckViewHcqt() {
        return Restangular.all(serviceUrl + "/woHcqt/getCheckViewHcqt").post();
    }

    //Huy-end

    function acceptXdddValue(obj) {
        return Restangular.all("woService/xddd/acceptXdddValue").post(obj);
    }

    function rejectXdddValue(obj) {
        return Restangular.all("woService/xddd/rejectXdddValue").post(obj);
    }

    function acceptQuantityByDate(obj) {
        return Restangular.all("woService/woTaskDaily/acceptQuantityByDate").post(obj);
    }

    function rejectQuantityByDate(obj) {
        return Restangular.all("woService/woTaskDaily/rejectQuantityByDate").post(obj);
    }

    function extendFinishDate(obj) {
        return Restangular.all("woService/xddd/extendFinishDate").post(obj);
    }

    function changeStateAndAcceptTcTct(obj) {
        return Restangular.all("woService/taichinh/changeStateAndAcceptTcTct").post(obj);
    }

    function postOverdueReason(obj) {
        return Restangular.all("woService/overdue/postOverdueReason").post(obj);
    }

    function getWoOverdueReason(obj) {
        return Restangular.all("woService/overdue/getWoOverdueReason").post(obj);
    }

    function acceptRejectOverdueReason(obj) {
        return Restangular.all("woService/overdue/acceptRejectOverdueReason").post(obj);
    }

    function getTcTctEmails(obj) {
        return Restangular.all(serviceUrl + "/tc/getTcTctEmails").post(obj);
    }

    function completeTkdtChecklist(obj) {
        return Restangular.all(serviceUrl + "/tkdtChecklist/completeChecklist").post(obj);
    }

    function approvedOkWoTkdt(obj) {
        return Restangular.all(serviceUrl + "/tkdtChecklist/approvedWoOk").post(obj);
    }
    
    function getDataTableTTTHQ(obj) {
        return Restangular.all(serviceUrl + "/tthqService/getDataTableTTTHQ").post(obj);
    }
    
    function approvedWoOkTthq(obj) {
        return Restangular.all(serviceUrl + "/tthqService/approvedWoOkTthq").post(obj);
    }
    //HienLT56 start 27052021
    function checkRoleTTHT(obj) {
        return Restangular.all(serviceUrl + "/wo/checkRoleTTHT").post(obj);
    }
    function getAppWorkSrcs(){
        return Restangular.all(serviceUrl+"/wo/getAppWorkSrcs").post({});
    }
    function getApConstructionTypes(){
        return Restangular.all(serviceUrl+"/wo/getAppConstructionTypes").post({});
    }
    function saveChangeForTTHT(obj){
        return Restangular.all(serviceUrl+"/wo/saveChangeForTTHT").post(obj);
    }
    //HienLT56 end 27052021
//  taotq start 23082021
    function getAppWorkSource() {
        return Restangular.all(serviceUrl + "/wo/getAppWorkSource").post({});
    }
    
    function createFileCheckList(obj) {
        return Restangular.all(serviceUrl + "/fileAttach/createFileCheckList").post(obj);
    }
//  taotq end 23082021
    function changeStateWaitPqt(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateWaitPqt").post(obj);
    }
    function changeStateWaitTtDtht(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateWaitTtDtht").post(obj);
    }
    function changeStateWaitTcTct(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateWaitTcTct").post(obj);
    }
    function changeStateCdPause(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateCdPause").post(obj);
    }
    function changeStateTthtPause(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateTthtPause").post(obj);
    }
    function changeStateDthtPause(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateDthtPause").post(obj);
    }
    function getDataConstructionContractByStationCode(code) {
        return Restangular.all(serviceUrl + "/wo/getDataConstructionContractByStationCode").post(code);
    }
    function changeStateCdPauseReject(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateCdPauseReject").post(obj);
    }
    function changeStateTthtPauseReject(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateTthtPauseReject").post(obj);
    }
    function changeStateDthtPauseReject(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateDthtPauseReject").post(obj);
    }

    //Duonghv13 start 14092021
    function checkRoleCDPKTCNKT(obj) {
        return Restangular.all(serviceUrl + "/wo/checkRoleCDPKTCNKT").post(obj);
    }
    //Duong end//

//  duonghv13 start 12102021
    function checkConditionCertificate(obj) {
        return Restangular.all(serviceUrl + "/wo/checkConditionCertificate").post(obj);
    }
//  duonghv13 end 12102021

    //Huypq-22112021-start
    function changeStateApprovedOrRejectWoHtctPQT(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateApprovedOrRejectWoHtctPQT").post(obj);
    }
    function changeStateApprovedOrRejectWoHtctTtDtht(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateApprovedOrRejectWoHtctTtDtht").post(obj);
    }
    function changeStateWoReject(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateWoReject").post(obj);
    }
    function changeStateReProcessWoDoanhThuDTHT(obj) {
        return Restangular.all(serviceUrl + "/wo/changeStateReProcessWoDoanhThuDTHT").post(obj);
    }
    //Huy-end
    
    function checkRoleApproveCDLV5(obj) {
        return Restangular.all(serviceUrl + "/wo/checkRoleApproveCDLV5").post(obj);
    }
//  duonghv13 start 12102021
    function checkContractIsGpxd(id) {
        return Restangular.all(serviceUrl + "/wo/checkContractIsGpxd").post(id);
    }
//  duonghv13 end 12102021
//  taotq start 15092022
    function checkAsignAdminWo(id) {
        return Restangular.all(serviceUrl + "/wo/checkAsignAdminWo").post(id);
    }
//  taotq end

    function getChecklistByParType(id) {
        return Restangular.all(serviceUrl + "/wo/checkAsignAdminWo").post(id);
    }

    function rejectWoBgbtsVhkt(obj) {
        return Restangular.all(serviceUrl + "/wo/rejectWoBgbtsVhkt").post(obj);
    }
}]);
