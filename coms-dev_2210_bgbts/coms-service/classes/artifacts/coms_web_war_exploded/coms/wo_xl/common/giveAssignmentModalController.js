(function () {
    'use strict';
    var controllerId = 'giveAssignmentModalController';

    angular.module('MetronicApp').controller(controllerId, giveAssignmentModalController);

    function giveAssignmentModalController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                           kendoConfig, $kWindow, htmlCommonService, woCreateNewService, $modal,
                                           CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http) {

        var vm = this;
        $scope.cdLv2List = {};
        $scope.cdLv3List = {};
        $scope.ftList = {};
        $scope.loggedInUser = $rootScope.casUser.userName;
        $scope.disable = {};
        $scope.label ={};
        $scope.isFt2 = false;
        $scope.selectedFt = {};
        $scope.cdLv4List = {};
        $scope.cdLv5List = {};
        $scope.vhktSysGroupId = '270120';
        

        init();
        function init(){
            checkAssignmentRole();

            //getCdLv3List();
            //getFtList()
        }

        function checkAssignmentRole() {
            $scope.disable.cdLevel2 = false;
            $scope.disable.cdLevel3 = false;
            if($scope.workingWO.cdLevel2 == $scope.workingWO.cdLevel3) $scope.isVHKT = true;
            else $scope.isVHKT = false;
            if($scope.isReassigning){
                if ($scope.sysUserId == $scope.workingWO.cdLevel5){
                    $scope.sysUserGroup.isCdLevel5 = false;
                    $scope.sysUserGroup.isCdLevel4 = false;
                    $scope.sysUserGroup.isCdLevel3 = false;
                    $scope.sysUserGroup.isCdLevel2 = false;
                    $scope.sysUserGroup.isFtLevel5 = true;
                    $scope.isHCQT = false;
                    $scope.label.cdLevel2Name = $scope.workingWO.cdLevel2Name;
                    $scope.label.cdLevel3Name = $scope.workingWO.cdLevel3Name;
                    $scope.label.cdLevel4Name = $scope.workingWO.cdLevel4Name;
                    $scope.label.cdLevel5Name = $scope.workingWO.cdLevel5Name;
                    $scope.disable.cdLevel2 = true;
                    $scope.disable.cdLevel3 = true;
                    $scope.disable.cdLevel4 = true;
                    $scope.disable.cdLevel5=true;
                    getFtListCdLevel5($scope.workingWO.contractCode)
                    return;
                }
                if($scope.permissions.cdDomainDataList.includes($scope.workingWO.cdLevel1) && $scope.workingWO.cdLevel5){
                    $scope.sysUserGroup.isCdLevel5 = true;
                    $scope.sysUserGroup.isCdLevel4 = false;
                    $scope.sysUserGroup.isCdLevel3 = false;
                    $scope.sysUserGroup.isCdLevel2 = false;
                    $scope.sysUserGroup.isFtLevel5 = false;
                    $scope.isHCQT = false;
                    $scope.label.cdLevel2Name = $scope.workingWO.cdLevel2Name;
                    $scope.label.cdLevel3Name = $scope.workingWO.cdLevel3Name;
                    $scope.label.cdLevel4Name = $scope.workingWO.cdLevel4Name;
                    $scope.disable.cdLevel2 = true;
                    $scope.disable.cdLevel3 = true;
                    $scope.disable.cdLevel4 = true;
                    getListCdLevel5( $scope.workingWO.contractCode);
                    return;
                }
                if($scope.permissions.cdDomainDataList.includes($scope.workingWO.cdLevel4)){
                    $scope.sysUserGroup.isCdLevel4 = true;
                    $scope.sysUserGroup.isCdLevel3 = false;
                    $scope.sysUserGroup.isCdLevel2 = false;
                    $scope.sysUserGroup.isCdLevel5 = false;
                    $scope.isHCQT = false;
                    $scope.label.cdLevel2Name = $scope.workingWO.cdLevel2Name;
                    $scope.label.cdLevel3Name = $scope.workingWO.cdLevel3Name;
                    $scope.label.cdLevel4Name = $scope.workingWO.cdLevel4Name;
                    $scope.label.cdLevel5Name = $scope.workingWO.cdLevel5Name;
                    $scope.disable.cdLevel2 = true;
                    $scope.disable.cdLevel3 = true;
                    $scope. disable.cdLevel5=true;
                    getFtList(4, $scope.workingWO.cdLevel4)
                    return;
                }


                if($scope.permissions.cdDomainDataList.includes($scope.workingWO.cdLevel3) && $scope.workingWO.cdLevel4){
                    $scope.sysUserGroup.isCdLevel4 = false;
                    $scope.sysUserGroup.isCdLevel3 = true;
                    $scope.sysUserGroup.isCdLevel2 = false;
                    $scope.sysUserGroup.isCdLevel5 = false;
                    $scope.isHCQT = false;
                    $scope.label.cdLevel2Name = $scope.workingWO.cdLevel2Name;
                    $scope.label.cdLevel3Name = $scope.workingWO.cdLevel3Name;
                    $scope.disable.cdLevel2 = true;
                    $scope.disable.cdLevel3 = true;
                    $scope.disable.cdLevel5=true;
                    //Duonghv13-add-02102021
                    $scope.selectedUnit.isFt = null;
                    $scope.selectedUnit.cdLv4 = null;
                    //Duonghv13-end-02102021
                    getCdLv4List($scope.workingWO.cdLevel3);
                    return;
                }

                if($scope.permissions.cdDomainDataList.includes($scope.workingWO.cdLevel2) && ($scope.workingWO.cdLevel3 || $scope.workingWO.ftId)){
                    $scope.sysUserGroup.isCdLevel5 = false;
                    $scope.sysUserGroup.isCdLevel4 = false;
                    $scope.sysUserGroup.isCdLevel3 = false;
                    $scope.sysUserGroup.isCdLevel2 = true;
                    $scope.isHCQT = false;
                    $scope.label.cdLevel2Name = $scope.workingWO.cdLevel2Name;
                    $scope.disable.cdLevel2 = true;
                    $scope. disable.cdLevel5=true;
                    //Duonghv13-add-02102021
                    $scope.selectedUnit.isFt2 = $scope.isFt2;
                    $scope.selectedUnit.ft = null;
                    $scope.selectedUnit.cdLv3 = null;
                    $scope.selectedFt.ftName = '';
                  //Duonghv13-end-02102021
                    getCdLv3List($scope.workingWO.cdLevel2);
                    return;
                }

                if(($scope.permissions.cdDomainDataList.includes($scope.workingWO.cdLevel1) || $scope.workingWO.ftId == $scope.sysUserId) && $scope.workingWO.woTypeCode == 'HCQT'){
                    $scope.isHCQT = true;
                }

                getCdLv2List();
            }
            else{
                if ($scope.sysUserId == $scope.workingWO.cdLevel5){
                    $scope.sysUserGroup.isCdLevel5 = false;
                    $scope.sysUserGroup.isCdLevel4 = false;
                    $scope.sysUserGroup.isCdLevel3 = false;
                    $scope.sysUserGroup.isCdLevel2 = false;
                    $scope.sysUserGroup.isFtLevel5 = true;
                    $scope.isHCQT = false;
                    $scope.label.cdLevel2Name = $scope.workingWO.cdLevel2Name;
                    $scope.label.cdLevel3Name = $scope.workingWO.cdLevel3Name;
                    $scope.label.cdLevel4Name = $scope.workingWO.cdLevel4Name;
                    $scope.label.cdLevel5Name = $scope.workingWO.cdLevel5Name;
                    $scope.disable.cdLevel2 = true;
                    $scope.disable.cdLevel3 = true;
                    $scope.disable.cdLevel4 = true;
                    $scope.disable.cdLevel5=true;
                    getFtListCdLevel5($scope.workingWO.contractCode)
                    return;
                }
                if($scope.permissions.cdDomainDataList.includes($scope.workingWO.cdLevel1) && $scope.workingWO.cdLevel5){
                    $scope.sysUserGroup.isCdLevel5 = true;
                    $scope.sysUserGroup.isCdLevel4 = false;
                    $scope.sysUserGroup.isCdLevel3 = false;
                    $scope.sysUserGroup.isCdLevel2 = false;
                    $scope.sysUserGroup.isFtLevel5 = false;
                    $scope.isHCQT = false;
                    $scope.label.cdLevel2Name = $scope.workingWO.cdLevel2Name;
                    $scope.label.cdLevel3Name = $scope.workingWO.cdLevel3Name;
                    $scope.label.cdLevel4Name = $scope.workingWO.cdLevel4Name;
                    $scope.disable.cdLevel2 = true;
                    $scope.disable.cdLevel3 = true;
                    $scope.disable.cdLevel4 = true;
                    getListCdLevel5( $scope.workingWO.contractCode);
                    return;
                }

                if($scope.permissions.cdDomainDataList.includes($scope.workingWO.cdLevel4)){
                    $scope.sysUserGroup.isCdLevel5 = false;
                    $scope.sysUserGroup.isCdLevel4 = true;
                    $scope.sysUserGroup.isCdLevel3 = false;
                    $scope.sysUserGroup.isCdLevel2 = false;
                    $scope.isHCQT = false;
                    $scope.label.cdLevel2Name = $scope.workingWO.cdLevel2Name;
                    $scope.label.cdLevel3Name = $scope.workingWO.cdLevel3Name;
                    $scope.label.cdLevel4Name = $scope.workingWO.cdLevel4Name;
                    $scope.disable.cdLevel2 = true;
                    $scope.disable.cdLevel3 = true;
                    $scope. disable.cdLevel5=true;
                    getFtList(4, $scope.workingWO.cdLevel4)
                    return;
                }

                if($scope.permissions.cdDomainDataList.includes($scope.workingWO.cdLevel3) && !$scope.workingWO.cdLevel4){
                    $scope.sysUserGroup.isCdLevel5 = false;
                    $scope.sysUserGroup.isCdLevel4 = false;
                    $scope.sysUserGroup.isCdLevel3 = true;
                    $scope.sysUserGroup.isCdLevel2 = false;
                    $scope.isHCQT = false;
                    $scope.label.cdLevel2Name = $scope.workingWO.cdLevel2Name;
                    $scope.label.cdLevel3Name = $scope.workingWO.cdLevel3Name;
                    $scope.disable.cdLevel2 = true;
                    $scope.disable.cdLevel3 = true;
                    $scope.disable.cdLevel4=true;
                    getCdLv4List($scope.workingWO.cdLevel3);
                    return;
                }

                if($scope.permissions.cdDomainDataList.includes($scope.workingWO.cdLevel2) && (!$scope.workingWO.cdLevel3 && !$scope.workingWO.ftId)){
                    $scope.sysUserGroup.isCdLevel5 = false;
                    $scope.sysUserGroup.isCdLevel4 = false;
                    $scope.sysUserGroup.isCdLevel3 = false;
                    $scope.sysUserGroup.isCdLevel2 = true;
                    $scope.isHCQT = false;
                    $scope.label.cdLevel2Name = $scope.workingWO.cdLevel2Name;
                    $scope.disable.cdLevel2 = true;
                    $scope. disable.cdLevel5=true;
                    //Duonghv13-add-02102021
                    $scope.selectedUnit.isFt2 = $scope.isFt2;
                    $scope.selectedUnit.ft = null;
                    $scope.selectedUnit.cdLv3 = null;
                    $scope.selectedFt.ftName = '';
                    //Duonghv13-end-02102021
                    getCdLv3List($scope.workingWO.cdLevel2);
                    return;
                }


                if(($scope.permissions.cdDomainDataList.includes($scope.workingWO.cdLevel1) || $scope.workingWO.ftId == $scope.sysUserId) && $scope.workingWO.woTypeCode == 'HCQT'){
                    $scope.isHCQT = true;
                }

                getCdLv2List();
            }

            // ThanhPT - WO BGBTS_DTHT - Start
            if ($scope.workingWO.woTypeCode == 'BGBTS_DTHT') {
                $scope.isFt2 = true;
                $scope.selectedUnit.isFt2 = true;
            }
            // ThanhPT - WO BGBTS_DTHT - End
        }

        function getCdLv2List(){
            var postData = {loggedInUser: $scope.loggedInUser}

            if($scope.workingWO.cdLevel1 == $scope.vhktSysGroupId){
                woCreateNewService.getVhktCdLv2VList(postData).then(
                    function (resp) {
                        if (resp.data) $scope.cdLv2List = resp.data;
                    },
                    function (error) {
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
            }
            else{
                woCreateNewService.getCdLv2List(postData).then(
                    function (resp) {
                        console.log(resp);
                        if(resp.data) $scope.cdLv2List = resp.data;
                    },
                    function (error) {
                        console.log(error);
                        toastr.success("Có lỗi xảy ra!");
                    }
                )
            }

        }

        vm.getCdLv3List = $scope.getCdLv3List = getCdLv3List;
        function getCdLv3List(cdLv2){
            var lv2Id = cdLv2;
            if(!cdLv2){
                lv2Id = $scope.selectedUnit.cdLv2.sysGroupId;
            }
            var postData = {cdLevel2: lv2Id}
            woCreateNewService.getCdLv3List(postData).then(
                function (resp) {
                    console.log(resp);
                    if(resp.data) $scope.cdLv3List = resp.data;
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        vm.getCdLv4List = $scope.getCdLv4List = getCdLv4List;
        function getCdLv4List(cdLv3){
            var lv3Id = cdLv3;
            if(!cdLv3){
                lv3Id = $scope.selectedUnit.cdLv3.sysGroupId;
            }
            var postData = {cdLevel3: lv3Id}
            woCreateNewService.getCdLv4List(postData).then(
                function (resp) {
                    console.log(resp);
                    if(resp.data) $scope.cdLv4List = resp.data;
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }

        vm.getFtList = $scope.getFtList = getFtList;
        function getFtList(level, parentId){
            if(level == 2){
                var postData = {cdLevel2: parentId}
            }
            else if(level == 4){
                var postData = {cdLevel4: parentId}
            }

            woCreateNewService.getFtList(postData).then(
                function (resp) {
                    console.log(resp);
                    if(resp.data) $scope.ftList = resp.data;
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }
        vm.getFtListCdLevel5 = getFtListCdLevel5;
        function getFtListCdLevel5(contractCode){
            var postData = {woId:$scope.workingWO.woId,contractCode: contractCode}
            woCreateNewService.getFtListCdLevel5(postData).then(
                function (resp) {
                    console.log(resp);
                    if(resp.data) $scope.ftList = resp.data;
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }
        vm.getListCdLevel5 = getListCdLevel5;
        function getListCdLevel5(contractCode){
            var postData = {woId:$scope.workingWO.woId,contractCode: contractCode,}
            woCreateNewService.getListCdLevel5(postData).then(
                function (resp) {
                    console.log(resp);
                    if(resp.data) $scope.cdLv5List = resp.data;
                },
                function (error) {
                    console.log(error);
                    toastr.success("Có lỗi xảy ra!");
                }
            )
        }


        $scope.autoCompleteFt2Options={
            dataTextField: "code", placeholder:"Chọn FT2",
            open: function(e) {
            },
            select: function(e) {
                data = this.dataItem(e.item.index());
                $scope.selectedUnit.ft = data;
                $scope.selectedFt.ftName = data.fullName;
                chooseFt2();
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function(options) {
                        var keySearch = $('#autoCompleteFt2').val().trim();

                        var objSearch = {keySearch:keySearch, cdLevel2: $scope.workingWO.cdLevel2,contractCode:$scope.workingWO.contractCode};
                        return Restangular.all("woService/wo/getFtList").post(objSearch).then(
                            function(response){
                                options.success(response.data);
                            }
                        ).catch(
                            function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            }
                        );

                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-12 text-header-auto">Chọn FT2</p></div>',
            template:'<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.employeeCode # - #: data.fullName #</div></div>',
            change: function(e) {
            },
            close: function(e) {
                // handle the event0
            }
        };
        //duonghv13-start 14092021
        $scope.autoCompleteFt1Options={
                dataTextField: "code", placeholder:"Chọn FT",
                open: function(e) {
                },
                select: function(e) {
                    data = this.dataItem(e.item.index());
                    $scope.selectedUnit.ft = data;
                    $scope.selectedFt.ftName = data.fullName;
                    chooseFt();
                    $scope.$apply();
                },
                pageSize: 10,
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: function(options) {
                            var keySearch = $('#autoCompleteFt1').val().trim();

                            var objSearch = {keySearch:keySearch, cdLevel3: $scope.workingWO.cdLevel3,contractCode:$scope.workingWO.contractCode};
                            return Restangular.all("woService/wo/getFtList").post(objSearch).then( //getFtListFromLv2SysGroup//
                                function(response){
                                    options.success(response.data);
                                }
                            ).catch(
                                function (err) {
                                    console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                                }
                            );

                        }
                    }
                },
                headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                    '<p class="col-md-12 text-header-auto">Chọn FT1</p></div>',
                template:'<div class="row" >' +
                    '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.employeeCode # - #: data.fullName #</div></div>',
                change: function(e) {
                },
                close: function(e) {
                    // handle the event0
                }
            };
        
        //duonghv13-end

        $scope.chooseFt2 = chooseFt2;
        function chooseFt2() {
            $scope.selectedUnit.cdLv3 = null;
            $scope.isFt2 = true;
            $scope.selectedUnit.isFt2 = true; //duonghv13-add 30092021
        }
        
        $scope.chooseFt = chooseFt;
        function chooseFt() {
            $scope.selectedUnit.cdLv4 = null;
            $scope.isFt = true;
            $scope.selectedUnit.isFt = true;
        }

        $scope.chooseCd3 = function(){
            $scope.selectedUnit.ft = null;
            $scope.selectedFt.ftName = '';
            $scope.isFt2 = false;
            $scope.selectedUnit.isFt2 = false;//duonghv13-add 30092021
        }
        
        $scope.chooseCd4 = function(){
            $scope.selectedUnit.ft = null;
            $scope.selectedFt.ftName = '';
            $scope.isFt = false;
            $scope.selectedUnit.isFt = false;
        }

        $scope.autoCompleteHcqtFtOptions={
            dataTextField: "code", placeholder:"Chọn FT",
            open: function(e) {
            },
            select: function(e) {
                data = this.dataItem(e.item.index());
                $scope.selectedUnit.ft = data;
                $scope.selectedFt.ftName = data.fullName;
                $scope.$apply();
            },
            pageSize: 10,
            dataSource: {
                serverFiltering: true,
                transport: {
                    read: function(options) {
                        var keySearch = $scope.selectedFt.ftName;

                        var objSearch = {keySearch:keySearch, sysGroupId: $scope.workingWO.cdLevel1};

                        return Restangular.all("woService/wo/getFtListFromLv2SysGroup").post(objSearch).then(
                            function(response){
                                options.success(response.data);
                            }
                        ).catch(
                            function (err) {
                                console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                            }
                        );

                    }
                }
            },
            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                '<p class="col-md-12 text-header-auto">Chọn FT</p></div>',
            template:'<div class="row" >' +
                '<div class="col-xs-12" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.employeeCode # - #: data.fullName #</div></div>',
            change: function(e) {
            },
            close: function(e) {
                // handle the event0
            }
        };

    // end controller
    }
})();
