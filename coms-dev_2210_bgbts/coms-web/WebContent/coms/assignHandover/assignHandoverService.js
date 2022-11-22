// VietNT_20181205_created
angular.module('MetronicApp').factory('assignHandoverService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
    var serviceUrl = 'assignHandoverService';
    var constructServiceUrl = 'constructionService';
    var factory = {
        getconstructionType: getconstructionType,
        addNewAssignHandover: addNewAssignHandover,
        removeAssignHandover: removeAssignHandover,
        attachDesignFileEdit: attachDesignFileEdit,
        downloadTemplate: downloadTemplate,
        getById: getById,
        doAssignHandover: doAssignHandover,
        getListImageHandover: getListImageHandover,
        getConstructionProvinceByCode: getConstructionProvinceByCode,
        exportHandoverNV: exportHandoverNV,
        //Huypq-Start
        getCheckDataWorkItem:getCheckDataWorkItem,
        checkStationContract:checkStationContract,
        removeAssignHandoverById:removeAssignHandoverById,
        getListHouseType:getListHouseType,
        getListGroundingType:getListGroundingType,
        updateAssignHandover:updateAssignHandover,
        updateAssignHandoverVuong:updateAssignHandoverVuong,
        updateAssignHandoverVtmd:updateAssignHandoverVtmd,
        updateAssignHandoverVuongVtmd:updateAssignHandoverVuongVtmd,
        updateAssignHandoverNotVuongVtmd:updateAssignHandoverNotVuongVtmd,
        checkStationContractBGMB:checkStationContractBGMB,
        checkWorkItemConsTask:checkWorkItemConsTask,
        updateWorkItem:updateWorkItem,
        checkStationBGMB:checkStationBGMB,
        downloadTemplateBGMB:downloadTemplateBGMB,
        checkHaveStationContract:checkHaveStationContract,
        findSignStateGoodsPlanByConsId:findSignStateGoodsPlanByConsId,
        updateConstructionTuyen:updateConstructionTuyen
        //Huypq-end
    };
    return factory;

    function getConstructionProvinceByCode(consCode) {
        return Restangular.all(serviceUrl + "/getConstructionProvinceByCode").post(consCode);
    }

    function getconstructionType() {
        return Restangular.all(constructServiceUrl + "/construction/getCatConstructionType").post();
    }

    function addNewAssignHandover(obj) {
        return Restangular.all(serviceUrl + "/addNewAssignHandover").post(obj);
    }

    function removeAssignHandover(obj) {
        return Restangular.all(serviceUrl + "/removeAssignHandover").post(obj);
    }
    
    function attachDesignFileEdit(obj) {
    	return Restangular.all(serviceUrl + "/attachDesignFileEdit").post(obj);
    }

    function downloadTemplate() {
        return Restangular
            .all(
            serviceUrl
            + "/downloadTemplate")
            .post();
    }

    function getById(id) {
        return Restangular.all(serviceUrl+"/getById").post(id);
    }

    function doAssignHandover(data) {
        return Restangular.all(serviceUrl + "/doAssignHandover").post(data);
    }
    
    function getListImageHandover(handoverId) {
        return Restangular.all(serviceUrl + "/getListImageHandover").post(handoverId);
    }

    function exportHandoverNV(obj) {
        return Restangular.all(serviceUrl + "/exportHandoverNV").post(obj);
    }
    //Huypq-start
    function getCheckDataWorkItem(obj) {
        return Restangular.all(serviceUrl + "/getCheckDataWorkItem").post(obj);   //Huypq-add
    }
    
    function checkStationContract(obj) {
        return Restangular.all(serviceUrl + "/checkStationContract").post(obj);   //Huypq-add
    }
    
    function removeAssignHandoverById(obj) {
        return Restangular.all(serviceUrl + "/removeAssignHandoverById").post(obj);
    }
    
    function getListHouseType(obj) {
        return Restangular.all(serviceUrl + "/getListHouseType").post(obj);
    }
    
    function getListGroundingType(obj) {
        return Restangular.all(serviceUrl + "/getListGroundingType").post(obj);
    }
    
    function updateAssignHandover(obj) {
        return Restangular.all(serviceUrl + "/updateAssignHandover").post(obj);
    }
    
    function updateAssignHandoverVuong(obj) {
        return Restangular.all(serviceUrl + "/updateAssignHandoverVuong").post(obj);
    }
    
    function updateAssignHandoverVtmd(obj) {
        return Restangular.all(serviceUrl + "/updateAssignHandoverVtmd").post(obj);
    }
    
    function updateAssignHandoverVuongVtmd(obj) {
        return Restangular.all(serviceUrl + "/updateAssignHandoverVuongVtmd").post(obj);
    }
    
    function updateAssignHandoverNotVuongVtmd(obj) {
        return Restangular.all(serviceUrl + "/updateAssignHandoverNotVuongVtmd").post(obj);
    }
    
    function checkStationContractBGMB(obj) {
        return Restangular.all(serviceUrl + "/checkStationContractBGMB").post(obj);
    }
    
    function checkWorkItemConsTask(obj) {
        return Restangular.all(serviceUrl + "/checkWorkItemConsTask").post(obj);
    }
    
    function updateWorkItem(obj) {
        return Restangular.all(serviceUrl + "/updateWorkItem").post(obj);
    }
    
    function checkStationBGMB(obj) {
        return Restangular.all(serviceUrl + "/checkStationBGMB").post(obj);
    }
    
    function downloadTemplateBGMB() {
        return Restangular
            .all(
            serviceUrl
            + "/downloadTemplateBGMB")
            .post();
    }
    
    function checkHaveStationContract(obj) {
        return Restangular.all(serviceUrl + "/checkHaveStationContract").post(obj);
    }
    
    function findSignStateGoodsPlanByConsId(id) {
        return Restangular.all(serviceUrl + "/findSignStateGoodsPlanByConsId").post(id);
    }
    
    function updateConstructionTuyen(obj) {
        return Restangular.all(serviceUrl + "/updateConstructionTuyen").post(obj);
    }
    //HuyPQ-end
}]);
