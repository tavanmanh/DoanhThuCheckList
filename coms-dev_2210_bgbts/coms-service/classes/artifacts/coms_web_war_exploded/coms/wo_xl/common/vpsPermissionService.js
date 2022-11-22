// picasso_2020jul20_created
angular.module('MetronicApp').factory('vpsPermissionService', ['Constant', function (Constant) {

    var factory = {
        getPermissions:getPermissions,
    };
    return factory;

    function getPermissions(casUser){
        var permissions = {};
        permissions.viewWO = false;
        permissions.createWO = false;
        permissions.updateWO = false;
        permissions.deleteWO = false;
        permissions.cdWO = false;
        permissions.viewTR = false;
        permissions.createTR = false;
        permissions.updateTR = false;
        permissions.deleteTR = false;
        permissions.cdTR = false;
        permissions.crudTRCnkt = false;
        permissions.createWOCnkt = false;
        permissions.createWOHcqt = false;
        permissions.updateWOHcqt = false;
        permissions.viewWOHcqt = false;
        permissions.approveRevenueSalary = false;
        permissions.approveTcTct = false;
        permissions.approveTcBranch = false;
        permissions.update5S = false;
        permissions.cdTrDomainDataList = '';
        permissions.createWODomainDataList = '';
        permissions.createTRDomainDataList = '';
        permissions.cdDomainDataList = '';
        permissions.crudTRCnktDomainDataList = '';
        permissions.createWODOANHTHU = false;
        permissions.createWODOANHTHUDomainDataList = '';
        permissions.approveTcBranchDataList = '';
        permissions.crudWOCnkt = false;
        permissions.crudWOCnktDomainDataList = '';
        permissions.approveOverdue = false;

        permissions.createdWoUctt = false;
        permissions.createdWoHshc = false;
        permissions.approveHtctHshc = false;
        permissions.approveDthtHtctHshc = false;
        
         permissions.approveDeviceElectricCNKT = false;
         permissions.approveDeviceElectric = false;

        var vpsPermissions = casUser.authorizedData.businessUserPermissions;
        for(var i = 0; i<vpsPermissions.length; i++){
            var pmCode = vpsPermissions[i].permissionCode;
            if(pmCode.includes('WO') || pmCode.includes('TRXL') || pmCode.includes('HCQT') || pmCode.includes('TC_TCT') || pmCode.includes('TC_BRANCH') || pmCode.includes('OVERDUE')){
                console.log(pmCode);
                console.log(vpsPermissions[i]);
            }

            if( pmCode == Constant.PERMISSIONS.VIEW_WOXL) permissions.viewWO = true;
            if( pmCode == Constant.PERMISSIONS.CREATE_WOXL){
                permissions.createWO = true;
                permissions.createWODomainDataList = vpsPermissions[i].domainDataList;
            }

            if( pmCode == Constant.PERMISSIONS.UPDATE_WOXL) permissions.updateWO = true;
            if( pmCode == Constant.PERMISSIONS.DELETE_WOXL) permissions.deleteWO = true;
            if( pmCode == Constant.PERMISSIONS.VIEW_WOXL_TR) permissions.viewTR = true;
            if( pmCode == Constant.PERMISSIONS.CREATE_WOXL_TR){
                permissions.createTR = true;
                permissions.createTRDomainDataList = vpsPermissions[i].domainDataList;
            }

            if( pmCode == Constant.PERMISSIONS.UPDATE_WOXL_TR) permissions.updateTR = true;
            if( pmCode == Constant.PERMISSIONS.DELETE_WOXL_TR) permissions.deleteTR = true;
            if( pmCode == Constant.PERMISSIONS.CD_WOXL_TR){
                permissions.cdTR = true;
                permissions.cdTrDomainDataList = vpsPermissions[i].domainDataList;
            }

            if( pmCode == Constant.PERMISSIONS.CD_WOXL){
                permissions.cdWO = true;
                permissions.cdDomainDataList = vpsPermissions[i].domainDataList;
            }

            if( pmCode == Constant.PERMISSIONS.CRUD_CNKT_TRXL){
                permissions.crudTRCnkt = true;
                permissions.crudTRCnktDomainDataList = vpsPermissions[i].domainDataList;
            }

            if( pmCode == Constant.PERMISSIONS.CRUD_CNKT_WOXL){
                permissions.crudWOCnkt = true;
                permissions.crudWOCnktDomainDataList = vpsPermissions[i].domainDataList;
            }

            if( pmCode == Constant.PERMISSIONS.CREATE_WO_HCQT){
                permissions.createWOHcqt = true;
                permissions.createWODomainDataList = vpsPermissions[i].domainDataList;
            }

            if( pmCode == Constant.PERMISSIONS.CREATE_WO_DOANHTHU){
                permissions.createWODOANHTHU = true;
                permissions.createWODOANHTHUDomainDataList = vpsPermissions[i].domainDataList;
            }
            if( pmCode == Constant.PERMISSIONS.APPROVED_TC_BRANCH) {
                permissions.approveTcBranch = true;
                permissions.approveTcBranchDataList = vpsPermissions[i].domainDataList;
            }

            if( pmCode == Constant.PERMISSIONS.UPDATE_WO_HCQT) permissions.updateWOHcqt = true;

            if( pmCode == Constant.PERMISSIONS.VIEW_WO_HCQT) permissions.viewWOHcqt = true;

            if( pmCode == Constant.PERMISSIONS.APPROVED_REVENUE_SALARY) permissions.approveRevenueSalary = true;

            if( pmCode == Constant.PERMISSIONS.APPROVED_TC_TCT) permissions.approveTcTct = true;

            if( pmCode == Constant.PERMISSIONS.UPDATE_WOXL_5S) permissions.update5S = true;

            if( pmCode == Constant.PERMISSIONS.APPROVED_OVERDUE_REASON) permissions.approveOverdue = true;

            if( pmCode == Constant.PERMISSIONS.CREATED_WO_UCTT) permissions.createdWoUctt = true;

            if( pmCode == Constant.PERMISSIONS.CREATED_WO_HSHC) permissions.createdWoHshc = true;
            //HienLT56 start 27052021
            if( pmCode == Constant.PERMISSIONS.EDIT_ADMIN_WO){
                permissions.editAdminWo = true;
                permissions.editAdminWoDomainDataList = vpsPermissions[i].domainDataList;
            }
            //HienLT56 end 27052021
            //Huypq-01112021-start
            if( pmCode == Constant.PERMISSIONS.APPROVED_HTCT_HSHC) permissions.approveHtctHshc = true;
            if( pmCode == Constant.PERMISSIONS.APPROVE_DTHT_HTCT_HSHC) permissions.approveDthtHtctHshc = true;
            //Huy-end
            
            if( pmCode == Constant.PERMISSIONS.APPROVED_DEVICE_ELECTRICT_CNKT) permissions.approveDeviceElectricCNKT = true;
            if( pmCode == Constant.PERMISSIONS.APPROVED_DEVICE_ELECTRICT) permissions.approveDeviceElectric = true;
        }

        return permissions;
    }
}]);
