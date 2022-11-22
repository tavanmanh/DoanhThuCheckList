(function () {
    'use strict';
    var controllerId = 'certificateCreateNewController';

    angular.module('MetronicApp').controller(controllerId, certificateCreateNewController);

    function certificateCreateNewController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                      kendoConfig, $kWindow, certificateCreateNewService, htmlCommonService,
                                      CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http) {

        var vm = this;
        // variables
        vm.breadcrumb = "Quản lý chứng chỉ > Tạo mới chứng chỉ";
        $scope.loggedInUser =  $rootScope.casUser.userName;
        $scope.loggedInUserFullName = $rootScope.casUser.fullName;
        $scope.loggedInUserEmail = $rootScope.casUser.email;
        $scope.sysUserId = $rootScope.casUser.sysUserId;
        $scope.sysUserGroup = {};
        $scope.fileLst = [];
        $scope.isCreateNew = true;
        $scope.workingCertificate = {};
        $scope.permissions = {};
        $scope.vhktSysGroupId = '270120';
        $scope.folder='';
        console.log(this.workingCertificate);
        $scope.doCreateNewCertificate = function(){
            var obj = angular.copy(this.workingCertificate);
        	submitAttachFile(obj);
//			obj.utilAttachDocumentDTO ={
//            	type: 'QLCC',
//            	name: $scope.fileLst[0].name,
//                status: '1',
//                filePath: $scope.fileLst[0].filePath,
//                createdDate: $scope.fileLst[0].createdDate ,
//        		createdUserName: $scope.fileLst[0].createdUserName,
//        		createdUserId: $scope.fileLst[0].createdUserId,
//            },
//        	 certificateCreateNewService.createNewCertificate(obj).then(
//                function(resp){
//                    if(resp){
//                        toastr.success("Thêm mới thành công!");
//                        publishChange();
//                    }
//                    else toastr.error("Thêm mới thất bại! " + resp.message);
//                },
//                function(error){
//                    console.log(error);
//                    toastr.error("Có lỗi xảy ra!");
//                }
//            )

        };

        function saveAll(obj) {
        	obj.utilAttachDocumentDTO ={
                	type: 'QLCC',
                	name: $scope.fileLst[0].name,
                    status: '1',
                    filePath: $scope.fileLst[0].filePath,
                    createdDate: $scope.fileLst[0].createdDate ,
            		createdUserName: $scope.fileLst[0].createdUserName,
            		createdUserId: $scope.fileLst[0].createdUserId
                };
            	 certificateCreateNewService.createNewCertificate(obj).then(
                    function(resp){
                    	if(resp && resp.error){
                    		toastr.error(resp.error);
                    		return;
                    	}
                        if(resp){
                            toastr.success("Thêm mới thành công!");
                            publishChange();
                        }
                        else toastr.error("Thêm mới thất bại! " + resp.message);
                    },
                    function(error){
                        console.log(error);
                        toastr.error("Có lỗi xảy ra!");
                    }
                )
        }
        
        function publishChange(){
            postal.publish({
                channel : "Tab",
                topic   : "action",
                data    : {action:'refresh'}
            });
        }
        
        function submitAttachFile(obj) {
            sendFile("customerFile", callback,obj);
        }
        
        function sendFile(id, callback, obj) {
        	
        	var file = null;
        	try {
        		file = $("#" + id)[0].files[0];
        	} catch (err) {
                toastr.warning("Bạn chưa chọn file");
                setTimeout(function() {
	               	  $(".k-upload-files.k-reset").find("li").remove();
	    			     $(".k-upload-files").remove();
	    				 $(".k-upload-status").remove();
	    				 $(".k-upload.k-header").addClass("k-upload-empty");
	    				 $(".k-upload-button").removeClass("k-state-focused");
                },10);
        		return false;
        	}
        	if (file == null || file == undefined){
        		toastr.warning("Bạn chưa chọn file");
                setTimeout(function() {
	               	  $(".k-upload-files.k-reset").find("li").remove();
	    			     $(".k-upload-files").remove();
	    				 $(".k-upload-status").remove();
	    				 $(".k-upload.k-header").addClass("k-upload-empty");
	    				 $(".k-upload-button").removeClass("k-state-focused");
                },10);
        		return false;
        	}
            if (!htmlCommonService.checkFileExtension(id)) {
                toastr.warning("File không đúng định dạng cho phép");
                setTimeout(function() {
	               	  $(".k-upload-files.k-reset").find("li").remove();
	    			     $(".k-upload-files").remove();
	    				 $(".k-upload-status").remove();
	    				 $(".k-upload.k-header").addClass("k-upload-empty");
	    				 $(".k-upload-button").removeClass("k-state-focused");
                },10);
                return false;
            }
            
            var formData = new FormData();
            formData.append('multipartFile', file);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "fileservice/uploadATTTInput?folder="+ $scope.folder,                
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    callback(data, file, id, obj);
                }
            
            });
            
            
        }
      
        function callback(data, fileReturn, id, obj) {
            for (var i = 0; i < data.length; i++) {
            	fileReturn.createdDate = htmlCommonService.getFullDate();
            	fileReturn.createdUserName = Constant.userInfo.VpsUserInfo.fullName;
            	fileReturn.createdUserId = Constant.userInfo.VpsUserInfo.sysUserId;
            	fileReturn.filePath = data[i];
            }
            callbackPushList(fileReturn,id,callbackPushList, obj);
        }
        
        function callbackPushList(file, id,callbackPushList, obj){
        	if(id=="customerFile"){
        		$scope.fileLst.push(file);
        	}
        	saveAll(obj);
        }
// end controller
    }
})();