// VietNT_20181205_created
angular.module('MetronicApp').factory('assignHandoverTTKTService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function ($http, $q, RestEndpoint, Restangular, $kWindow) {
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
        updateSysGroupInAssignHandover:updateSysGroupInAssignHandover,
        removeAssignById:removeAssignById,
        downloadTemplateTTKT:downloadTemplateTTKT,
        getCheckDataWorkItem:getCheckDataWorkItem
    };
    return factory;

    function getCheckDataWorkItem(obj) {
        return Restangular.all(serviceUrl + "/getCheckDataWorkItem").post(obj);   //Huypq-add
    }
    
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

    function downloadTemplateTTKT() {
        return Restangular
            .all(
            serviceUrl
            + "/downloadTemplateTTKT")
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
    
    function updateSysGroupInAssignHandover(obj) {
        return Restangular.all(serviceUrl + "/updateSysGroupInAssignHandover").post(obj);
    }
    
    function removeAssignById(id) {
        return Restangular.all(serviceUrl + "/removeAssignById").post(id);
    }
}]);
