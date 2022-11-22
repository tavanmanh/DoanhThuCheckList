
angular.module('MetronicApp').factory('constructionService', ['$http', '$q', 'RestEndpoint', 'Restangular', '$kWindow', function($http, $q, RestEndpoint, Restangular, $kWindow){
	 	var serviceUrl = 'constructionService';
	    var factory = {
	        remove:remove,
	        createconstruction:createconstruction,
            updateBGMBItem:updateBGMBItem,
            updateMerchandiseItem:updateMerchandiseItem,
            updateStartItem:updateStartItem,
            searchMerchandise:searchMerchandise,
	        updateconstruction:updateconstruction,
            exportConstruction:exportConstruction,
	        doSearch : doSearch,
            getconstructionById:getconstructionById,
            getconstructionType:getconstructionType,
	        getAll: getAll,
	        exportpdf:exportpdf,
            getSequence:getSequence,
            getAppParamByType:getAppParamByType,
            getCatConstructionDeploy : getCatConstructionDeploy,
            downloadTemplate:downloadTemplate,
            updateVuongItem:updateVuongItem,
            getCatWorkItemType:getCatWorkItemType,
            confirmPkx:confirmPkx,
            detaillPhieu:detaillPhieu,
            downloadFileImportTP:downloadFileImportTP,
            getconstructionStatus:getconstructionStatus,
            getCatProvinCodePxk:getCatProvinCodePxk,
            getSysGroupInfo: getSysGroupInfo,
            downloadErrorExcel: downloadErrorExcel,
            updateConstrLicence:updateConstrLicence,
            updateConstrDesign:updateConstrDesign,
            updateIsBuildingPermit:updateIsBuildingPermit,
            getCheckCodeList:getCheckCodeList,
            checkUnitConstruction:checkUnitConstruction,
            checkContructionType:checkContructionType,
            addWorkItemGPon:addWorkItemGPon,
            /** hienvd: START 1/7/2019 **/
            getListImageById: getListImageById,
            getDateConstruction: getDateConstruction,
			getListImageByIdBGMB: getListImageByIdBGMB,
            /**    hienvd: END **/
			editDetailGpon : editDetailGpon,
			removeGpon : removeGpon,
			removeDetailitemGpon : removeDetailitemGpon ,
			doSearchGpon : doSearchGpon ,
			getWorkItemTypeHTCT:getWorkItemTypeHTCT,
			checkRoleMapSolar:checkRoleMapSolar,
			downloadFileImportSolar:downloadFileImportSolar,
			checkRoleConstruction:checkRoleConstruction,
			approve:approve,
			checkRoleApprove:checkRoleApprove,
			checkRoleConstructionXDDD: checkRoleConstructionXDDD,
	    };
	 
	     return factory;
    function getconstructionStatus(id) {
        return Restangular.all(serviceUrl+"/getconstructionStatus").post(id);
    }
    function getCatWorkItemType(id) {
        return Restangular.all(serviceUrl+"/getWorkItemType").post(id);
    }
    function updateVuongItem(obj) {
        return Restangular.all(serviceUrl+"/updateVuongItem").post(obj);
    }
    function updateBGMBItem(obj) {
        return Restangular.all(serviceUrl+"/updateBGMBItem").post(obj);
    }
    function updateMerchandiseItem(obj) {
        return Restangular.all(serviceUrl+"/updateMerchandiseItem").post(obj);
    }
    function updateStartItem(obj) {
        return Restangular.all(serviceUrl+"/updateStartItem").post(obj);
    }
    //Huypq-start
    function updateIsBuildingPermit(obj) {
        return Restangular.all(serviceUrl+"/updateIsBuildingPermit").post(obj);
    }
    function getCheckCodeList(obj) {
        return Restangular.all(serviceUrl+"/getCheckCodeList").post(obj);
    }
    //Huypq-end
    function exportConstruction(obj) {
        return Restangular.all(serviceUrl+"/exportConstruction").post(obj);
    }
    function downloadTemplate(obj) {
        return Restangular
            .all(
            serviceUrl
            + "/exportExcelTemplate")
            .post(obj);
    }
    function getSequence(obj) {
        return Restangular.all(serviceUrl+"/getSequence").post(obj);
    }

	    function getconstructionById(id) {
            return Restangular.all(serviceUrl+"/getById").post(id);
        }
	    function remove(obj) {
            return Restangular.all(serviceUrl+"/remove").post(obj);
        }

	    function createconstruction(obj) {
            console.log(obj);
            return Restangular.all(serviceUrl+"/add").post(obj);
        }

	    function getconstructionType() {
            return Restangular.all(serviceUrl+"/construction/getCatConstructionType").post();
        }

	    function updateconstruction(obj) {
            return Restangular.all(serviceUrl+"/update").post(obj);
        }
        //TungTT 25/1/2019_start
	    function checkUnitConstruction(obj) {
            return Restangular.all(serviceUrl+"/getDataUpdate").post(obj);
        }
	    //TungTT 25/1/2019_end
	    function doSearch(obj) {
            return Restangular.all(serviceUrl+"/doSearch").post(obj); 	 
        }

        function searchMerchandise(obj) {
            return Restangular.all(serviceUrl + "/searchMerchandise").post(obj);
        }
	    
	    function getAll(obj) {
            return Restangular.all(serviceUrl+"/getAll").post(obj); 	 
        }
	    function getAppParamByType(obj) {
            return Restangular.all(serviceUrl+"/construction/getAppParamByType").post(obj);
        }
        function getCatConstructionDeploy() {
            return Restangular.all(serviceUrl+"/construction/getCatConstructionDeploy").post();

        }

	    function exportpdf(obj) {
	    	var deferred = $q.defer();
             Restangular.all(serviceUrl+"/exportPdf").post(obj).then(
						function(data) {
							var binarydata= new Blob([data],{ type:'application/pdf'});
					        kendo.saveAs({dataURI: binarydata, fileName: "báo cáo" + '.pdf'});
						}, function(errResponse) {
							toastr.error("Lỗi không xóa được!");
						});
             
             return deferred.promise;
        }
        function confirmPkx(obj) {
            return Restangular.all(serviceUrl+"/confirmPkx").post(obj);
        }
        function detaillPhieu(obj) {
            return Restangular.all(serviceUrl+"/detaillPhieu").post(obj);
        }
        function downloadFileImportTP(obj){
            return Restangular.all(serviceUrl + "/downloadFileImportTP").post(obj);
        }
        function getCatProvinCodePxk(id) {
        	return Restangular.all(serviceUrl+"/getCatProvinCode").post(id);
        }
        
        //chinhpxn 20180605 start
        function getSysGroupInfo(id) {
        	return Restangular.all(serviceUrl+"/getSysGroupInfo").post(id);
        }
        
        function downloadErrorExcel(obj) {
			return Restangular
					.all(
							 "fileservice/exportExcelError")
					.post(obj);
		}
        //chinhpxn 20180605 end
        
        //nhantv 25092018
        function updateConstrLicence(obj) {
            return Restangular.all(serviceUrl+"/updateConstrLicence").post(obj);
        }
        function updateConstrDesign(obj) {
            return Restangular.all(serviceUrl+"/updateConstrDesign").post(obj);
        }
        
        /**Hoangnh start 06032019**/
        function checkContructionType(obj) {
        	return Restangular.all(serviceUrl+"/checkContructionType").post(obj);
        }
        function addWorkItemGPon(obj) {
            return Restangular.all("workItemService/addWorkItemGPon").post(obj);
        }
        /**Hoangnh start 06032019**/

        /** hienvd: START 01/07/2019 **/
        function getListImageById(object) {
            return Restangular.all(serviceUrl+"/getListImageById").post(object);
        }
		function getListImageByIdBGMB(object) {
            return Restangular.all(serviceUrl+"/getListImageByIdBGMB").post(object);
        }

        function getDateConstruction(object) {
            return Restangular.all(serviceUrl+"/getDateConstruction").post(object);
        }
        /** hienvd: END **/
        function removeGpon(obj) {
            return Restangular.all("workItemService/removeGpon").post(obj);
        }
        function removeDetailitemGpon(obj) {
            return Restangular.all("workItemService/removeDetailitemGpon").post(obj);
        }
        
        function doSearchGpon(obj) {
            return Restangular.all("workItemService/doSearchGpon").post(obj);
        }
        function editDetailGpon(obj) {
            return Restangular.all("workItemService/editGpon").post(obj);
        }
        function getWorkItemTypeHTCT(id) {
            return Restangular.all(serviceUrl+"/construction/getWorkItemTypeHTCT").post(id);
        }
        function checkRoleMapSolar() {
            return Restangular.all(serviceUrl+"/checkRoleMapSolar").post();
        }
        function downloadFileImportSolar(obj){
            return Restangular.all(serviceUrl + "/downloadFileImportSolar").post(obj);
        }
        function checkRoleConstruction() {
            return Restangular.all(serviceUrl+"/checkRoleConstruction").post();
        }
        function approve(obj) {
            return Restangular.all(serviceUrl+"/approve").post(obj);
        }
        function checkRoleApprove() {
            return Restangular.all(serviceUrl+"/checkRoleApprove").post();
        }
        function checkRoleConstructionXDDD() {
            return Restangular.all(serviceUrl+"/checkRoleConstructionXDDD").post();
        }
	}]);
