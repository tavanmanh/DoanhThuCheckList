(function () {
    'use strict';
    var controllerId = 'managementCertificateController';

    angular.module('MetronicApp').controller(controllerId, managementCertificateController);

    function managementCertificateController($scope, $templateCache, $rootScope, $timeout, gettextCatalog, $filter,
                                    kendoConfig, $kWindow, htmlCommonService, managementCertificateService, $modal, vpsPermissionService,woManagementService,
                                    CommonService, PopupConst, Restangular, RestEndpoint, Constant, $http, $compile) {

        var vm = this;
        // variables
        var record = 0;
        vm.breadcrumb = "Quản lý chứng chỉ > Danh sách chứng chỉ";
        $scope.managementCertificate = {}
        vm.managementCertificateSearch = {};
        $scope.loggedInUser = $rootScope.casUser.userName;
        $scope.sysUserId = $rootScope.casUser.sysUserId;
        $scope.sysGroupId = $rootScope.casUser.sysGroupId;
        $scope.sysUserGroup = {};
        $scope.enable = {};
        $scope.permissions = {};
        $scope.workingCertificate = {};
        $scope.label = {};
        vm.fileResult = {};
        vm.statusDataList = [
            {id: 1, name: 'Còn hạn'},
            {id: 2, name: 'Sắp hết hạn'},
            {id: 3, name: 'Hết hạn'}
        ];
        
        vm.statusDataList1 = [
            {id: 1, name: 'Chờ phê duyệt'},
            {id: 2, name: 'Đã phê duyệt'},
            {id: 3, name: 'Đã từ chối'},
        ];

        $scope.currentPageCertificateList = [];
        vm.checkRoleDeleteTTHT = false;
        vm.disableBtnPDF = false;
		vm.disableBtnExcel = false;
		$scope.checkRoleVHKTApprove = false;      
		$scope.checkSysAdminCNKT = false; 
        $scope.isEditting = false;
        $scope.isCreateNew = false;
        $scope.disableEdit = true;
        $scope.folder='';
        init();

        function init() {
            $scope.permissions = vpsPermissionService.getPermissions($rootScope.casUser);
            console.log($scope.permissions); 
            initDate();
           	managementCertificateService.checkRoleVHKTApprove().then(function (d) {
  			     if (d && d == 'OK') {
  			    	$scope.checkRoleVHKTApprove = true;
  			    	fillDataTable(); 
  			    } else {
  			    	$scope.checkRoleVHKTApprove = false;
  			 	}
  			   
  			}
           ).catch(function () {
        	   $scope.checkRoleVHKTApprove = false;
           });
           managementCertificateService.checkRoleCNKT().then(function(d){
	      			 if(d && d == 'OK'){
	      				$scope.checkSysAdminCNKT = true;
	      				fillDataTable(); 
	      			 } else{
	      				$scope.checkSysAdminCNKT = false;
	      			 }
  		   }).catch(function () {
  			   $scope.checkSysAdminCNKT = false;
  		   }); 
            
            postal.subscribe({
                   channel: "Tab",
                   topic: "action",
                   callback: function (data) {
                       if (data.action == 'refresh') vm.dataCertificateListTable.dataSource.read();
                   }
            });
        }

        function initDate(){
            var now = new Date();
        }
         
        vm.doSearch = doSearch;

        function doSearch() {
            var grid = vm.dataCertificateListTable;
            if (grid) {
                Object.keys(vm.managementCertificateSearch).forEach(function (key) {
                    if (vm.managementCertificateSearch[key] == "") vm.managementCertificateSearch[key] = null;
                });
                grid.dataSource.query({
                    page: 1,
                    pageSize: 10
                })
            }
        }

        vm.viewCertificateDetails = function (certificateId) {
        	
        	removeTab("Chi tiết Chứng chỉ"); 
        	
            var template = Constant.getTemplateUrl('CERTIFICATE_DETAILS');
            $rootScope.viewDetailsCertificateId = certificateId;
            template.certificateId = certificateId;
            postal.publish({
                channel: "Tab",
                topic: "open",
                data: template
            });

            postal.publish({
                channel: "Tab",
                topic: "action",
                data: {action: 'refresh', certificateId: certificateId}
            });
        };

        function removeTab (title) {
            for(var i=0; i<$scope.tabs.length; i++){
            	if($scope.tabs[i].title==title){
            		$scope.tabs.splice(i, 1);
            		break;
            	}
            }
		};
		

        vm.editCertificate = function (certificateId) {
            var obj = {certificateId: certificateId};
            managementCertificateService.getOneCertificateDetails(obj).then(
                function (resp) {
                    if (resp && resp.data) {
                    	$scope.workingCertificate = resp.data;
        				vm.fileResult = angular.copy($scope.workingCertificate.utilAttachDocumentDTO);
        				if($scope.fileLst == undefined || $scope.fileLst == null || $scope.fileLst == []){
        					$scope.fileLst = [];
        				}
        				if(vm.fileResult == undefined || vm.fileResult == null )	$scope.fileLst = [];
        				else $scope.fileLst.push(vm.fileResult);
        				
                    	if(resp.data.total>1)
                    	   $scope.disableEdit = true;
                    	else $scope.disableEdit = false;

                    }
                    	
                    $scope.isEditting = true;
                    $scope.isCreateNew = false;
                    $modal.open({
                        templateUrl: 'coms/wo_xl/managementCertificate/CertificateEditModal.html',
                        controller: 'certificateCreateEditTemplateController',
                        windowClass: 'app-modal-window',
                        scope: $scope
                    })
                        .result.then(
                        function () {
                            vm.saveCertificate($scope.workingCertificate);
                            
                        },
                        function () {
                            $scope.workingCertificate = {};
                        }
                    )
                },
                function (error) {
                    console.log(error);
                    toastr.error("Có lỗi xảy ra! Vui lòng thử lại sau.");
                }
            )

        }

        vm.saveCertificate = function (obj) {
            submitAttachFile(obj);
        }
         
        function saveAll(obj) {
          obj.utilAttachDocumentDTO ={
        				type : 'QLCC',
                    	name: $scope.fileLst[0].name,
                    	status : '1',
                        filePath: $scope.fileLst[0].filePath,
                        createdDate: $scope.fileLst[0].createdDate ,
                		createdUserName: $scope.fileLst[0].createdUserName,
                		createdUserId: $scope.fileLst[0].createdUserId
          };
        	
          managementCertificateService.update(obj).then(
                    function(resp){
                        if(resp){
                            toastr.success("Cập nhật thành công!");
                            doSearch();
                			$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
                        }
                        else toastr.error("Có lỗi xảy ra! " + resp.message);
                    },
                    function(error){
                        console.log(error);
                        toastr.error("Có lỗi xảy ra!");
                    }
             )
        }
        
        function submitAttachFile(obj) {
            sendFile("customerFile", callback,obj);
        }
        
        function sendFile(id, callback, obj) {
        	
        	var file = null;
        	if($scope.disableEdit == true || $("#customerFile")[0].files[0] == null || $("#customerFile")[0].files[0] == undefined  ){
        		 file =  $scope.fileLst[0];
        		 if(obj.utilAttachDocumentDTO == null || file ==null){
     	    		toastr.warning("Vui lòng chọn file");
     	    		setTimeout(function() {
     	               	  $(".k-upload-files.k-reset").find("li").remove();
     	    			     $(".k-upload-files").remove();
     	    				 $(".k-upload-status").remove();
     	    				 $(".k-upload.k-header").addClass("k-upload-empty");
     	    				 $(".k-upload-button").removeClass("k-state-focused");
     	            },10);
     	    		return false;
     	    	}
        	}
        	else{
        		if (!htmlCommonService.checkFileExtension("customerFile")) {
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
        
        vm.deleteCertificate = function (certificateId) {
            var obj = {
            		certificateId: certificateId,
            		loggedInUser: $scope.loggedInUser
            };
            confirm('Xác nhận xóa bản ghi đã chọn?',
                function () {
            		managementCertificateService.remove(obj).then(
                        function (resp) {
                        	if (resp && resp.statusCode == 1) toastr.success("Xóa thành công!");
                            else toastr.success("Đã xóa hoặc không tồn tại!");
                            vm.dataCertificateListTable.dataSource.read();
                        },
                        function (error) {
                            console.log(error);
                            toastr.error("Có lỗi xảy ra!");
                        }
                    )
                }
            );
        }

        function createActionTemplate(certificateItem){
     
//            var isEditable = true;
//            var isDeleteable = true;
//
//            if(vm.checkRoleVHKTApprove == true){
//            	isDeleteable = true;
//            	isEditable = true;
//            }
//            if(vm.checkSysAdminCNKT == true){
//                isEditable = false;
//                isDeleteable = false;
//
//            }
            
            var result = certificateItem.certificateId;
            var template = '<div class="display-block cedtpl" style="text-align: center">' +
                '<i class="fa fa-list-alt icon-table" ng-click="vm.viewCertificateDetails('+certificateItem.certificateId+')"></i>' +
                '<i ng-if="' + $scope.checkRoleVHKTApprove + '" class="fa fa-pencil icon-table" ng-click="vm.editCertificate('+certificateItem.certificateId+')"></i>' +
                '<i ng-if="' + $scope.checkRoleVHKTApprove + '" class="fa fa-trash-o icon-table" ng-click="vm.deleteCertificate('+certificateItem.certificateId+')"></i></div>'
            return template;
        }
        
        vm.openTabCreateNewCertificate = function () {
            var template = Constant.getTemplateUrl('CERTIFICATE_CREATE_NEW');
            postal.publish({
                channel: "Tab",
                topic: "open",
                data: template
            });
        };

        function fillDataTable() {
        	vm.dataCertificateListTableOptions = kendoConfig.getGridOptions({    
        		autoBind: true,
                scrollable: true,
                resizable: true,
                editable:false,
                save: function () {
                    vm.dataCertificateListTable.dataSource.read();
                },
                dataBinding: function () {
                    record = (this.dataSource.page() - 1) * this.dataSource.pageSize();
                },
                reorderable: true,
                toolbar: null,
                dataSource: {
                    serverPaging: true,
                    schema: {
                        total: function (response) {
                            vm.count = response.total;
                            return response.total;
                        },
                        data: function (response) {
                            $scope.currentPageCertificateList = response.data;
                            return response.data;
                        }
                    },
                    transport: {
                        read: {
                            //Thuc hien viec goi service
                            url: Constant.BASE_SERVICE_URL + "managementCertificateRsService/doSearch",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            vm.managementCertificateSearch.page = options.page
                            vm.managementCertificateSearch.pageSize = options.pageSize
                            var obj = angular.copy(vm.managementCertificateSearch);	
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
                        // empty: "<div style='margin:5px'>Không có kết quả hiển thị</div>"
                    }
                },
                columns: [
                    {
                        title: "STT",
                        field: "stt",
                        template: function () {
                            return ++record;
                        },
                        width: '10%',
                        columnMenu: false,
                        hidden: true,
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Thao tác",
                        field: 'action',
                        width: '100px',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:center;"
                        },
                        type :'text',
                        template: function(dataItem){
                            return createActionTemplate(dataItem);
                        },
                    },
                    
                    {
                        title: "Trạng thái phê duyệt",
                        field: 'approveStatus',
                        width: '20%',
                        template: "# if(approveStatus == 1){ #" + "#= 'Chờ phê duyệt' #" + "# } " + "else if (approveStatus == 2) { # " + "#= 'Đã phê duyệt' #" + "#} "+ "else { # " + "#= 'Đã từ chối' #" + "#} #",
                        headerAttributes: {style: "text-align:center;font-weight: bold;overflow: visible;white-space: normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Mã chứng chỉ",
                        field: 'certificateCode',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                        
                    },
                    {
                        title: "Tên chứng chỉ",
                        field: "certificateName",
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;overflow: visible;white-space: normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Lĩnh vực",
                        field: 'careerName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;overflow: visible;white-space: normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    //Huypq-01092020-start
                    {
                        title: " Tên nhân viên ",
                        field: 'fullName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;overflow: visible;white-space: normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Mã nhân viên",
                        field: 'loginName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Chi nhánh kĩ thuật",
                        field: 'sysGroupName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;overflow: visible;white-space: normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    //huy-end
                    {
                        title: "Email",
                        field: 'email',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;overflow: visible;white-space: normal;"},
                        attributes: {
                            style: "text-align:right;"
                        },
                        type: 'text',
                    },{
                        title: "Số điện thoại",
                        field: 'phoneNumber',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type :'text',
                    },{
                        title: "Chức danh",
                        field: 'positionName',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Điểm thực hành",
                        field: 'practicePoint',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;overflow: visible;white-space: normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Điểm lí thuyết",
                        field: 'theoreticalPoint',
                        width: '15%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;overflow: visible;white-space: normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Đơn vị cấp",
                        field: 'unitCreated',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;overflow: visible;white-space: normal;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Ngày cấp",
                        field: 'startDate',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Ngày hết hạn",
                        field: 'finishDate',
                        width: '20%',
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    },
                    {
                        title: "Trạng thái chứng chỉ",
                        field: 'certificateStatus',
                        width: '20%',
                        template: "# if(certificateStatus == 1){ #" + "#= 'Còn hạn' #" + "# } " + "else if (certificateStatus == 2) { # " + "#= 'Sắp hết hạn' #" + "#} "+ "else { # " + "#= 'Hết hạn' #" + "#} #",
                        headerAttributes: {style: "text-align:center;font-weight: bold;"},
                        attributes: {
                            style: "text-align:left;"
                        },
                        type: 'text',
                    }
                ]
            });
        }

        

        function getValueByKeys(array, keySearch, valueSearch, keyReturn) {
            if (!array || array.length == 0) return null;

            for (var i = 0; i < array.length; i++) {
                var item = array[i];
                if (item[keySearch] == valueSearch) return item[keyReturn];
            }
            return null;
        }

       
        vm.showHideColumn = function (column) {
            if (angular.isUndefined(column.hidden)) {
                vm.dataCertificateListTable.hideColumn(column);
            } else if (column.hidden) {
                vm.dataCertificateListTable.showColumn(column);
            } else {
                vm.dataCertificateListTable.hideColumn(column);
            }
        }
        
        function valueOrEmptyStr(value){
            return value ? value : '';
        }

        vm.gridColumnShowHideFilter = function (item) {

            return item.type == null || item.type !== 1;
        };
        
        vm.cancel = function () {
            $("div.k-window-actions > a.k-window-action > span.k-i-close").click();
        }

        vm.changeDataAuto=changeDataAuto;
		function changeDataAuto(id){
			switch(id){
				case 'user':{
	        		if (processSearch(id, vm.selectedDept1)) {
	        			vm.managementCertificateSearch.loginName = null;
		            	vm.managementCertificateSearch.fullName = null;
	    	            vm.selectedDept1 = false;
	        		}
	        	}
				case 'certificate':{
	        		if (processSearch(id, vm.selectedCareer1)) {
	        			vm.managementCertificateSearch.certificateCode = null;
		            	vm.managementCertificateSearch.certificateName = null;
	    	            vm.selectedCareer1 = false;
	        		}
	        	}
				 
			}
		}
		
		
		vm.selectedDept1 = false;

		vm.patternSignerOptions={
						dataTextField: "fullName", 
						placeholder:"Nhập mã hoặc tên người thực hiện",
						open: function(e) {
							vm.isSelect = false;
							vm.selectedDept1 = false;
						},
			            select: function(e) {
			            	vm.isSelect = true;
			            	vm.selectedDept1 = true;
			            	data = this.dataItem(e.item.index());
			            	vm.managementCertificateSearch.loginName = data.employeeCode;
			            	vm.managementCertificateSearch.fullName = data.fullName;
			            },
			            pageSize: 10,
			            dataSource: {
			                serverFiltering: true,
			                transport: {
			                    read: function(options) {
			                    	vm.selectedDept1 = false;
			                        return Restangular.all("sysUserCOMSRsService/getForAutoCompleteInSign").post({pageSize:10, page:1, fullName:$("#loginName").val().trim()}).then(function(response){
			                            options.success(response);
			                            if(response == [] || $("#loginName").val().trim() == ""){
			                            	vm.managementCertificateSearch.loginName = null;
			    			            	vm.managementCertificateSearch.fullName = null;
			                            }
			                        }).catch(function (err) {
			                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
			                        });
			                    }
			                }
			            },
			            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
			            '<p class="col-md-6 text-header-auto border-right-ccc">Mã nhân viên</p>' +
			            '<p class="col-md-6 text-header-auto">Họ tên</p>' +
			            	'</div>',
			            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.employeeCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.fullName #</div> </div>',
				          change: function(e) {
				        	  if (e.sender.value() === '') {
				        		  vm.managementCertificateSearch.loginName = null;
	  			            	  vm.managementCertificateSearch.fullName = null;
				              }
				        	},
			            close: function(e) { 
			              }
		};
		
		vm.exportCertificateExcel = function(){
            var obj = vm.managementCertificateSearch;
            obj.page = 1;
            obj.pageSize = 999999;

            confirm("Xuất dữ liệu excel?", function () {
                return Restangular.all("managementCertificateRsService/exportFile").post(obj).then(function (d) {
                    var data = d.plain();
                    window.location = Constant.BASE_SERVICE_URL + "fileservice/downloadFileATTT?" + data.fileName;
                }).catch(function (e) {
                    toastr.error(gettextCatalog.getString("Lỗi khi tải xuống file!"));
                    return;
                });
            })
        }
		
		vm.selectedCareer1 = false;
		vm.autoCompleteCertificateOptions={
				dataTextField: "name", 
				placeholder:"Nhập mã-số hoặc tên chứng chỉ",
				open: function(e) {
					vm.isSelect = false;
					vm.selectedCareer1 = false;
				},
	            select: function(e) {
	            	vm.isSelect = true;
	            	vm.selectedCareer1 = true;
	            	data = this.dataItem(e.item.index());
	            	vm.managementCertificateSearch.certificateCode = data.certificateCode;
	            	vm.managementCertificateSearch.certificateName = data.certificateName;
	            },
	            pageSize: 10,
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function(options) {
	                    	vm.selectedDept1 = false;
	                        return Restangular.all("managementCertificateRsService/getCertificatePopup").post({pageSize:10, page:1, certificateName:$("#certificateCode").val().trim()}).then(function(response){
	                            options.success(response);
	                            if(response == [] || $("#certificateCode").val().trim() == ""){
	                            	vm.managementCertificateSearch.certificateCode = null;
	            	            	vm.managementCertificateSearch.certificateName = null;
	                            }
	                        }).catch(function (err) {
	                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
	                        });
	                    }
	                }
	            },
	            headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
	            '<p class="col-md-6 text-header-auto border-right-ccc">Mã chứng chỉ</p>' +
	            '<p class="col-md-6 text-header-auto">Tên chứng chỉ</p>' +
	            	'</div>',
	            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px 32px 0 0;word-wrap: break-word;float:left">#: data.certificateCode #</div><div style="padding-left:10px;width:auto;overflow: hidden"> #: data.certificateName #</div> </div>',
		          change: function(e) {
		        	  if (e.sender.value() === '') {
		        		  vm.managementCertificateSearch.certificateCode = null;
      	            	  vm.managementCertificateSearch.certificateName = null;
		              }
		        	},
	            close: function(e) { 
	              }
			};


		vm.cancelInput = function (param) {
			if (param == 'loginName'){
				vm.managementCertificateSearch.loginName = null;
				vm.managementCertificateSearch.fullName = null;
			}
			if (param == 'certificateCode'){
				vm.managementCertificateSearch.certificateCode = null;
			}
			if (param == 'name'){
				vm.managementCertificateSearch.statusList = null;
//				$("#monthYear1").val('');
					
			}
			if (param == 'name1'){
				vm.managementCertificateSearch.approveList = null;
//				$("#monthYear1").val('');
				
			}
			
		}
        

// end controller
    }
})();
