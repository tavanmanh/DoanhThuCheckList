(function () {
    'use strict';
    var controllerId = 'certificateDetailsController';

    angular.module('MetronicApp').controller(controllerId, certificateDetailsController);

    function certificateDetailsController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter, $modal, $sce, $compile,
                                 kendoConfig, $kWindow, managementCertificateService, htmlCommonService, vpsPermissionService,woManagementService,certificateExtendService,woDetailsService,
                                 CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $modalStack) {

        var vm = this;
        var today = new Date();
        // variables
        vm.String = "Quản lý chứng chỉ > Danh sách chứng chỉ> Chi tiết chứng chỉ";
        vm.workingCertificate = {};
        vm.label = {};
        $scope.workingCertificateDetail = [];
        $scope.loggedInUser = $rootScope.casUser.userName;
        $scope.sysUserId = $rootScope.casUser.sysUserId;
        $scope.sysUserGroup = {};
        $scope.enable = {};
        $scope.headerColor = {'background-color': '#33CCCC'};
        $scope.checklists = [];
        $scope.permissions = {};
        vm.fileLst = [];
        vm.canExtendDueDate = false;
        vm.isOverdue = false;
        vm.checkRoleVHKTApprove = false;      
        vm.checkSysAdminCNKT = false; 
        vm.file = {};
        vm.folder='';
        vm.modalOpened = false;
        vm.isCreateNew = false;
        vm.showDetail = false;
        vm.viewPopupDetail = false;
        vm.isProposeExtend = false;
        vm.minDate = null;
        init();
        function init() {
//        	 managementCertificateService.checkRoleVHKTApprove().then(function (d) {
//			 if (d && d == 'OK') {
//				 vm.checkRoleVHKTApprove = true;
//			 } else {
//				 vm.checkRoleVHKTApprove = false;
//			 	}
//			 }).catch(function () {
//				 vm.checkRoleVHKTApprove = false;
//			 });
//        	 managementCertificateService.checkRoleCNKT().then(function(d){
//			 if(d && d == 'OK'){
//				 vm.checkSysAdminCNKT = true;
//			 } else{
//				 vm.checkSysAdminCNKT = false;
//			 }
//			 }).catch(function () {
//				 vm.checkSysAdminCNKT = false;
//			 });
        	
        	
//          
        	var currentDay = kendo.toString(new Date(), "dd/MM/yyyy");
			vm.minDate = currentDay;
            $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
            if ($rootScope.viewDetailsCertificateId) 
            	getCertificateDetails($rootScope.viewDetailsCertificateId);
            subCertificate();
            
        }
        
        function subCertificate() {
            postal.subscribe({
                channel: "Tab",
                topic: "action",
                callback: function (data) {
                    if (data.action == 'refresh' &&
                        vm.workingCertificate.certificateId &&
                        data.certificateId != vm.workingCertificate.certificateId
                    ) {
                    	getCertificateDetails(data.certificateId, true);
                    }
                }
            });
        }

        function getCertificateDetails(certificateId, refresh) {
            var obj = {certificateId: certificateId};
            managementCertificateService.getOneCertificateDetails(obj).then(
                function (resp) {
                    console.log(resp);
                    vm.workingCertificate = resp.data;
                    if (resp && resp.data) {
                          vm.workingCertificate = resp.data;
                          checkOverdue();
                          checkStatusApprove();
                          fillDataCheckList();
						  if (refresh == true) {
							  if (vm.CertificateExtendHistoryOptions) {
								 vm.CertificateExtendHistory.dataSource.read();
							  }
						  }
                    }
                    $rootScope.viewDetailsCertificateId = null;
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                    $rootScope.viewDetailsCertificateId = null;
                }
            )

        };
        

        function strToDate(dtStr) {
            var dateParts = dtStr.split("/");
            var timeParts = dateParts[2].split(" ")[1].split(":");
            dateParts[2] = dateParts[2].split(" ")[0];
            return new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0], timeParts[0], timeParts[1], timeParts[2]);
        }

        function strToDateNoTime(dateStr) {
            if (!dateStr) return null;
            var dateOnly = dateStr.split(" ")[0];
            var dateParts = dateOnly.split("/");

            return new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0],);
        }

        function checkOverdue() {
            var finishDate = kendo.parseDate(vm.workingCertificate.finishDate, "dd/MM/yyyy");
            var centerTime = kendo.parseDate(new Date(), "dd/MM/yyyy");
            var currTime = kendo.parseDate(new Date(), "dd/MM/yyyy");  
            centerTime.setDate( currTime.getDate() + 30);
            
            if (currTime   <= finishDate && centerTime <= finishDate) {
            	vm.workingCertificate.certificateStatus = 1;
            	vm.label.certificateStatus = 'Còn hạn';
            }else if ( currTime < finishDate && centerTime > finishDate  ) {
            	vm.workingCertificate.certificateStatus = 2;
            	vm.label.certificateStatus = 'Sắp hết hạn';
            }else{
            	vm.workingCertificate.certificateStatus = 3;
            	vm.label.certificateStatus = 'Hết hạn';
            	vm.isOverdue = true;
            }
        }
        function checkStatusApprove() {
            var finishDate = strToDateNoTime(vm.workingCertificate.finishDate);
            var currTime = new Date();          
            if (vm.workingCertificate.approveStatus ==1 ) {
            	vm.label.approveStatus = 'Chờ phê duyệt';
            }else if ( vm.workingCertificate.approveStatus==2) {
            	vm.label.approveStatus = 'Đã phê duyệt';
            }else{
            	vm.label.approveStatus = 'Đã từ chối phê duyệt';
            	vm.canExtendDueDate = true;
            }
        }

        vm.openModalEditExtend = openModalEditExtend;
        function openModalEditExtend(dataItem) {
        	vm.showDetail = true;
			vm.isCreateNew = false;
            vm.certificateExtend = dataItem;
            vm.fileLst = [];
            var teamplateUrl="coms/wo_xl/certificateDetails/certificateExtendDueDate.html";
			var title="Sửa thông tin gia hạn chứng chỉ";
			var windowId="CERTFICATE_EXTEND";
			certificateExtendService.getResultFileByExtendId({certificateExtendId: dataItem.certificateExtendId}).then(function(data){
				if(data.plain()==null){
					vm.fileResult = {};
				} else {
					vm.fileResult = angular.copy(data.plain());
				}
				if(vm.fileResult!=null){
					vm.fileLst.push(vm.fileResult);
				}
				
			});
			CommonService.populatePopupCreate(teamplateUrl,title,vm.certificateExtend,vm,windowId,true,'600','400',"code");
            
        }

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };

        vm.deleteCheckList = function (dataItem) {
            confirm('Xác nhận xóa yêu cầu gia hạn trên.Bạn có chắc chắn muốn xóa không?',
                function () {
            	certificateExtendService.remove(dataItem).then(
                        function (resp) {
                            toastr.success("Xóa thành công!");
                            getCertificateDetails(vm.vm.workingCertificate.certificateId, true);
                            reloadTables();
                            publishChange();
                        	
                        },
                        function (error) {
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                        }
                    )
                }
            );
        };

        function reloadTables() {
        	if (vm.CertificateExtendHistory) {
        		var sizePage = $("#CertificateExtendHistory").data("kendoGrid").dataSource.total();
				var pageSize = $("#CertificateExtendHistory").data("kendoGrid").dataSource.pageSize();
				
				if(sizePage % pageSize === 1){
					var currentPage = $("#CertificateExtendHistory").data("kendoGrid").dataSource.page();
					if (currentPage > 1) {
						$("#CertificateExtendHistory").data("kendoGrid").dataSource.page(currentPage - 1);
					}
				 }
				 $("#CertificateExtendHistory").data('kendoGrid').dataSource.read();
				 $("#CertificateExtendHistory").data('kendoGrid').refresh();
				 
        	}
        }

        function createActionTemplate(dataItem) {
            var template = '<div class="display-block cedtpl" style="text-align: center">' +
             '<i ng-if="permissions.createdWoUctt && dataItem.status != 2 " class="fa fa-pencil icon-table" uib-tooltip="Sửa gia hạn" ng-click="vm.openModalEditExtend(dataItem)" translate></i>'+
             '<i ng-if="dataItem.status  == 2 || dataItem.status  == 3" class="fa fa-list-alt icon-table" uib-tooltip="Xem chi tiết"  ng-click="vm.viewDetail(dataItem)" translate></i>' +
			 '<i ng-if="permissions.createWO && dataItem.status  == 1 " class="fa fa-check-circle" style="color: #00FF00;" uib-tooltip="Phê duyệt" ng-click="vm.accept(dataItem)" translate></i>'+
			 '<i ng-if="permissions.createWO && dataItem.status  == 1 " class="fa fa-close" style="color: red; position: relative; left: 10px; " uib-tooltip="Từ chối" ng-click="vm.reject(dataItem)" translate></i></div>'
            return template;
        }

        

        function fillDataCheckList() {
            fillNormalChecklist();
        }

        var checkListRecord = 0;

        function fillNormalChecklist() {
            vm.CertificateExtendHistoryOptions = kendoConfig.getGridOptions({
                autoBind: true,
                scrollable: true,
                resizable: true,
                editable: false,
                save: function () {
                    vm.CertificateExtendHistory.dataSource.read();
                },
                dataBinding: function () {
                    checkListRecord = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                             return response.total;
                        },
                        data: function (response) {
                        	 if (response.data[0].status == 2) vm.isProposeExtend = true;
                        	 else vm.isProposeExtend = false;
//                        	
//                        	if($("#CertificateExtendHistory").data("kendoGrid").dataSource.data()[0].status == 2){
//                               	vm.isProposeExtend = true;
//            				 }
                            $scope.checklists = response.data;
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            // Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "certificateExtendRsService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var obj = {
                            	certificateId: vm.workingCertificate.certificateId,
                            	page : options.page,
                                pageSize : options.pageSize
                            };
                            return JSON.stringify(obj)

                        }
                    },
                    pageSize: 10,
                },
  
                noRecords: true,
                columnMenu: false,
                
                messages: {
                    noRecords: gettextCatalog.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
                },
                pageable: {
                    refresh: true,
                    pageSizes: [10, 15, 20, 25],
                    messages: {
                        display: "{0}-{1} của {2} kết quả",
                        itemsPerPage: "kết quả/trang",
                        empty: ''
                    }
                },
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++checkListRecord;
                        },
                        width: '8%',
                        columnMenu: false,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',

                    },
                    {
                        title: "Ngày gia hạn mới",
                        field: 'finishDate',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;"},
                        attributes: {style: "text-align:center;overflow: visible;white-space: normal;"},
                        type: 'text',

                    },
                    {
                        title: "Trạng thái",
                        field: 'status',
                        width: '30%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                        	if (dataItem.status == 1 ) return 'Chờ phê duyệt';
                        	else if (dataItem.status ==  2) return 'Đã phê duyệt';
                        	else if (dataItem.status ==  3) return 'Đã từ chối phê duyệt';
                        },

                    },
                    {
    					title: "Ngày tạo",
    			        field: 'createdDate',
    			        width: '12%',
    			        headerAttributes: {
    						style: "text-align:center;"
    					},
    					attributes: {
    						style: "text-align:left;"
    					},
    				},{
    					title: "Người tác động",
    			        field: 'updatedUserName',
    			        width: '20%',
    			        headerAttributes: {
    						style: "text-align:center;overflow: visible;white-space: normal;"
    					},
    					attributes: {
    						style: "text-align:center;"
    					},
    				},{
    					title: "Lí do từ chối(nếu có)",
    			        field: 'reason',
    			        width: '80%',
    			        headerAttributes: {
    						style: "text-align:center;overflow: visible;white-space: normal;"
    					},
    					attributes: {
    						style: "text-align:left;"
    					},
    				},
    				{
    					title: "File đính kèm",
    			        field: 'attachFileName',
    			        width: '30%',
    			        headerAttributes: {
    						style: "text-align:center;overflow: visible;white-space: normal;"
    					},
    					attributes: {
    						style: "text-align:center;"
    					},
						template :  function(dataItem) {
							return "<a href='' ng-click='vm.downloadFile(dataItem)'>" + dataItem.attachFileName + "</a>";
						}
    				},
                    {
                        title: "Thao tác",
                        field: 'action',
                        width: '215px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center; min-width: 210px !important;"
                        },
                        type: 'text',
                        template: function (dataItem) {
                            return createActionTemplate(dataItem);
                        }
                    }
                ]
            });
        }

        vm.downloadFile = function(dataItem){
			window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + dataItem.attachFileExtendPath;	
    	}
        vm.downloadFile1 = function(dataItem){
			window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + dataItem.attachFilePath;	
    	}
        vm.downloadFile2 = function(dataItem){
			window.location = Constant.BASE_SERVICE_URL+"fileservice/downloadFileATTT?" + dataItem.filePath;	
    	}

        vm.accept=accept;
		function accept(dataItem){
			confirm('Xác nhận phê duyệt gia hạn',function(){
				var obj = angular.copy(dataItem);
				obj.status = 2 ;
				certificateExtendService.accept(obj).then(
                    function (resp) {
                    	toastr.success("Phê duyệt thành công");
                        publishChange();
                        getCertificateDetails(dataItem.certificateId, true);
                        reloadTables(); 
                    },
                    function (err) {
                        console.log(err);
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại .");
                    }
                )
            });
        };
        

        vm.validateRejectReason = function () {
            if (!vm.certificateExtend.rejectReason || vm.certificateExtend.rejectReason.trim() == '') {
                toastr.error('Chưa nhập lý do từ chối!');
                return false;
            }
            return true;
        };

        function postOverdueReason(dataItem) {
        	var obj = angular.copy(dataItem);
			obj.status = 3 ;
			obj.reason  = vm.certificateExtend.rejectReason;
			certificateExtendService.reject(obj).then(
					function (resp) {
                        toastr.success("Xác nhận cập nhật thành công");
                        publishChange();
                        getCertificateDetails(dataItem.certificateId, true);
                        reloadTables(); 
                    },
                    function (err) {
                        console.log(err);
                        toastr.error("Có lỗi xảy ra! Vui lòng thử lại .");
                    }
            );
        }
        
        vm.reject=reject;
		function reject(dataItem){
			 vm.modalOpened = true;
	         $modal.open({
	        	   options: {
	                    modal: true,
	                    visible: false,
	                    width: '900',
	                    height: '500',
	                    actions: ["Minimize", "Maximize", "close"],
	                    open: function () {
	                        this.wrapper.children('.k-window-content').addClass("fix-footer");
	                    }
	                },
	                templateUrl: 'coms/wo_xl/certificateDetails/rejectReason.html',
	                controller: null,
	                windowClass: 'app-modal-window',
	                scope: $scope
	         }).result.then(
	              function () {
	                    postOverdueReason(dataItem);
	                    vm.modalOpened = false;
	         },
	         function () {
	                    vm.modalOpened = false;
	         }
	       )
		};
		
		
		vm.viewDetail=viewDetail;
		function viewDetail(dataItem){
				vm.showDetail = false;
				vm.isCreateNew = false;
				vm.viewPopupDetail = true;
				vm.certificateExtend =dataItem;
				var teamplateUrl="coms/wo_xl/certificateDetails/certificateExtendDueDate.html";
				var title="Chi tiết bản ghi gia hạn";
				var windowId="CERTFICATE_EXTEND";
				certificateExtendService.getResultFileByExtendId({certificateExtendId: dataItem.certificateExtendId}).then(function(data){
					if(data.plain()==null){
						vm.fileResult = {};
					} else {
						vm.fileResult = angular.copy(data.plain());
					}
					if(vm.fileResult!=null){
						vm.fileLst.push(vm.fileResult);
					}
					
				});
				CommonService.populatePopupCreate(teamplateUrl,title,vm.certificateExtend,vm,windowId,true,'600','400',"code"); 
		}
			
        function publishChange() {
            postal.publish({
                channel: "Tab",
                topic: "action",
                data: {action: 'refresh'}
            });
        }

        vm.extendDueDate = extendDueDate;

        function extendDueDate() {
            var oldDueDate = vm.workingCertificate.finishDate;
            var tempId = vm.workingCertificate.certificateId;
            vm.certificateExtend ={};
            vm.certificateExtend.finishDate = oldDueDate;
            vm.certificateExtend.certificateId = tempId;
            vm.showDetail = false;
			vm.isCreateNew = true;
            var teamplateUrl="coms/wo_xl/certificateDetails/certificateExtendDueDate.html";
			var title="Xin gia hạn chứng chỉ";
			var windowId="CERTFICATE_EXTEND";
			CommonService.populatePopupCreate(teamplateUrl,title,vm.certificateExtend,vm,windowId,true,'600','400',"code");
        }
   
        vm.cancel = function(){
        	$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }
        
        function submitAttachFile() {
            sendFile("customerFile", callback);
        }
        vm.resetFormFile = resetFormFile;
        
        function resetFormFile() {
            $("#customerFile").val(null);
            if(vm.isCreateNew ==true)
            	vm.fileLst = [];
        }
        
        function sendFile(id, callback) {
        	var file = null;
        	if( $("#customerFile")[0].files[0] == null || $("#customerFile")[0].files[0] == undefined  ){
        		 file =  vm.fileLst[0];
        	}
        	else{
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
        		file = $("#" + id)[0].files[0];
        	}
        	
            var formData = new FormData();
            formData.append('multipartFile', file);
            return $.ajax({
                url: Constant.BASE_SERVICE_URL + "fileservice/uploadATTTInput?folder="+ vm.folder,                
                type: "POST",
                data: formData,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    callback(data, file, id);
                    var obj = {
                    	certificateId : vm.certificateExtend.certificateId,
                        finishDate: vm.certificateExtend.finishDate,
                        description: vm.certificateExtend.description.trim(),
                        utilAttachDocumentDTO:{
                        	type: 'QLCC',
                        	name: vm.fileLst[0].name,
                            status: '1',
                            filePath: vm.fileLst[0].filePath,
                            createdDate: vm.fileLst[0].createdDate ,
                    		createdUserName: vm.fileLst[0].createdUserName,
                    		createdUserId: vm.fileLst[0].createdUserId,
                        },
                    };
                    if(vm.isCreateNew == true){
                    	certificateExtendService.create(obj).then(function (response) {
                            if (!!response.error) {
                                toastr.error(response.error);
                                return;
                            }  
                            toastr.success("Yêu cầu gia hạn được gửi thành công");
                            cancelImport();
                            publishChange();
                            reloadTables();
                            getCertificateDetails(vm.workingCertificate.certificateId, true);
                            

                            
                        }, function (err) {
                            toastr.error("Yêu cầu không thành công");
                            console.log(err);
                        });
                    }
                    if(vm.showDetail == true){
                    	obj.certificateExtendId = vm.certificateExtend.certificateExtendId;
                    	obj.createdDate = vm.certificateExtend.createdDate;
                    	obj.createdUserId = vm.certificateExtend.createdUserId;
                    	obj.status = 1;
                    	certificateExtendService.update(obj).then(function (response) {
                            if (!!response.error) {
                                toastr.error(response.error);
                                return;
                            }
                            toastr.success("Sửa thông tin gia hạn thành công");
                            cancelImport();
                            publishChange();
                            getCertificateDetails(vm.workingCertificate.certificateId);
                            reloadTables();
                            
                        }, function (err) {
                            toastr.error("Có lỗi khi sửa. Vui lòng thử lại!");
                            console.log(err);
                        });
                    } 
                }
            
            });
            
            
        }
        
        function callback(data, fileReturn, id) {
            for (var i = 0; i < data.length; i++) {
            	fileReturn.createdDate = htmlCommonService.getFullDate();
            	fileReturn.createdUserName = Constant.userInfo.VpsUserInfo.fullName;
            	fileReturn.createdUserId = Constant.userInfo.VpsUserInfo.sysUserId;
            	fileReturn.filePath = data[i];
            }
            callbackPushList(fileReturn,id,callbackPushList);
        }
        
        function callbackPushList(file, id,callbackPushList){
        	if(id=="customerFile"){
        		if(vm.isCreateNew == false){
        			vm.fileLst =[];
        		}
            	vm.fileLst.push(file);
        	}
        }
        
        vm.extendFinishDate = function extendFinishDate() {
        	var tempId= vm.certificateExtend.certificateId;
        	if (!vm.certificateExtend.finishDate || vm.certificateExtend.finishDate == '') {
                toastr.error("Chưa chọn ngày gia hạn!");
                return;
            } 
        	var newDueDate = vm.certificateExtend.finishDate;

            if (!CommonService.isAValidDate(newDueDate, true)) {
                toastr.success("Định dạng ngày tháng không đúng hoặc nhỏ hơn ngày hiện tại!");
                return;
            }
            if (!vm.certificateExtend.description || vm.certificateExtend.description == '') {
                    toastr.error("Chưa nhập lý do!");
                    return;
            }
            if(vm.isCreateNew == true){
	            if (!$("#customerFile")[0].files[0]) {
	        		toastr.warning("Bạn chưa chọn file");
	                return;
	        	}
            }
//            if (!htmlCommonService.checkFileExtension("customerFile")) {
//                toastr.warning("File không đúng định dạng cho phép");
//                setTimeout(function() {
//	               	  $(".k-upload-files.k-reset").find("li").remove();
//	    			     $(".k-upload-files").remove();
//	    				 $(".k-upload-status").remove();
//	    				 $(".k-upload.k-header").addClass("k-upload-empty");
//	    				 $(".k-upload-button").removeClass("k-state-focused");
//                },10);
//                return;
//            }  
            submitAttachFile();

        }
        
        function publishChange() {
            postal.publish({
                channel: "Tab",
                topic: "action",
                data: {action: 'refresh'}
            });
        }


        vm.cancelImport = cancelImport;
        function cancelImport() {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }
        
//        	else {
//                if (104857600 < $("#fileImporttthq")[0].files[0].size) {
//                    toastr.warning("Dung lượng file vượt quá 100MB! ");
//                    return;
//                }
//                $("#fileNameTthq")[0].innerHTML = $("#fileImporttthq")[0].files[0].name
//            }

    }
})();
