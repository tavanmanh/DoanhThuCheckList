(function () {
    'use strict';
    var controllerId = 'woCreateNewController';

    angular.module('MetronicApp').controller(controllerId, woCreateNewController);

    function woCreateNewController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, woCreateNewService, htmlCommonService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http) {

        var vm = this;
        // variables
        vm.breadcrumb = "Quản lý WO > Tạo mới WO";
        $scope.loggedInUser =  $rootScope.casUser.userName;
        $scope.loggedInUserFullName = $rootScope.casUser.fullName;
        $scope.loggedInUserEmail = $rootScope.casUser.email;
        $scope.sysUserId = $rootScope.casUser.sysUserId;
        $scope.sysUserGroup = {};

        $scope.isCreateNew = true;
        $scope.workingWO = {};
        $scope.apWorkSrcItem = {};
        $scope.versioning = 0;
        $scope.permissions = {};
        $scope.selectedWoType = {};
        $scope.vhktSysGroupId = '270120';
        $scope.isProcessing = false;
        
        init();
        function init(){
            getSysUserGroup();
            var d = new Date();
            $scope.versioning = d.getTime();
        }

        function getSysUserGroup(){
            var obj = {loggedInUser: $scope.loggedInUser}
            woCreateNewService.getSysUserGroup(obj).then(
                function(resp){
                    console.log(resp);
                    if(resp && resp.data) {
                        $scope.sysUserGroup = resp.data;
                    }
                },
                function(error){
                    console.log(error);
                    toastr.success("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )
        }

        $scope.doCreateNewWO = function(){

            $scope.isProcessing = true;
            if(this.workingWO.moneyValue && parseFloat(this.workingWO.moneyValue) > 100000000000){
            	toastr.warning("Giá trị không được vượt quá 100 tỷ VND");
            	$scope.isProcessing = false;
            	return;
            }
            
            this.workingWO.userCreated = this.workingWO.loggedInUser = $scope.loggedInUser;
            this.workingWO.state = Constant.WO_XL_STATE.unassign.stateCode;
            this.workingWO.status =1;
            this.workingWO.createdUserFullName = $scope.loggedInUserFullName;
            this.workingWO.createdUserEmail = $scope.loggedInUserEmail;

            if(this.workingWO.cdLevel1 == $scope.vhktSysGroupId) this.workingWO.type = 2;

            //checklist xây dựng dân dụng
            var checklist=[];
            var checklistNames=[];
            var checklistItemNames = '';
            var emptyName = false;
            if(this.workingWO.woTypeCode == 'THICONG' && this.workingWO.apWorkSrc == 6){
                $('#xddd_checklist').children('.xddd-checklist').each(function(index, element){
                    var itemName = $(element).find('.xddd-checklist-item-name').val();
                    if(!itemName || itemName == '') emptyName = true;
                    var obj = {name:itemName};
                    checklist.push(obj);
                    checklistNames.push(itemName);
                    checklistItemNames += itemName + '|';
                });
                this.workingWO.xdddChecklist = checklist;
                this.workingWO.checklistItemNames = checklistItemNames;
                this.workingWO.moneyValue = 0;

                var isDuplicate = (new Set(checklistNames)).size !== checklistNames.length;

                if(emptyName){
                    toastr.error('Tên đầu việc không được để trống! ');
                    $scope.isProcessing = false;
                    return;
                }

                if(isDuplicate){
                    toastr.error('Tên đầu việc không được trùng nhau! ');
                    $scope.isProcessing = false;
                    return;
                }

            }

            //console.log(this.workingWO);

            woCreateNewService.createNewWO(this.workingWO).then(
                function(resp){
                    if(resp && resp.statusCode == 1){
                        toastr.success("Thêm mới thành công!");
                        publishChange();
                    }
                    else toastr.error("Thêm mới thất bại! " + resp.message);
                    $scope.isProcessing = false;
                },
                function(error){
                    console.log(error);
                    toastr.error("Có lỗi xảy ra!");
                    $scope.isProcessing = false;
                }
            )
        };

        function publishChange(){
            postal.publish({
                channel : "Tab",
                topic   : "action",
                data    : {action:'refresh'}
            });
        }
        
// end controller
    }
})();