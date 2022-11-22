(function() {
	'use strict';
	var controllerId = 'manageRegisterCtvController';

	angular.module('MetronicApp').controller(controllerId,
			manageRegisterCtvController);

	function manageRegisterCtvController($scope, $templateCache, $rootScope,
			$timeout, gettextCatalog, $filter, kendoConfig, $kWindow,
			manageRegisterCtvService, htmlCommonService, CommonService,
			PopupConst, Restangular, RestEndpoint, Constant, $http) {

		var vm = this;
		vm.String = "Quản lý công trình > Tiếp xúc khách hàng > Đăng ký tài khoản CTV";
		vm.searchForm = {
			status: ""
		};
		vm.isCreateNew = false;
		vm.addForm = {};
		vm.fileLstCmt = [];
		vm.fileLstHk = [];
		vm.fileLstHd = [];
		
		vm.listStatus = [{text: "Hết hiệu lực", value: 0},
            {text: "Chờ công ty phê duyệt", value: 1},
            {text: "Chờ CNKT phê duyệt", value: 2},
            {text: "CNKT từ chối", value: 3},
            {text: "Công ty phê duyệt", value: 4},
            {text: "Công ty từ chối", value: 5},
            {text: "Tất cả", value: ""}
            ];

		initData();
		function initData() {
			fillDataTable([]);
		}

		vm.showHideWorkItemColumn = function(column) {
			if (angular.isUndefined(column.hidden)) {
				vm.manageRegisterCtvGrid.hideColumn(column);
			} else if (column.hidden) {
				vm.manageRegisterCtvGrid.showColumn(column);
			} else {
				vm.manageRegisterCtvGrid.hideColumn(column);
			}

		}

		vm.gridColumnShowHideFilter = function(item) {

			return item.type == null || item.type !== 1;
		};

		vm.doSearch = doSearch;
		function doSearch() {
			var grid = vm.manageRegisterCtvGrid;
			CommonService.doSearchGrid(grid, {
				pageSize : grid.dataSource.pageSize()
			});
		}

		vm.resetFormField = resetFormField;
        function resetFormField(fieldName) {
            switch (fieldName) {
                case 'keySearch':
                    vm.searchForm.keySearch = null;
                    break;
                case 'status':
                    vm.searchForm.status = 6;
                    break;
                case 'group':
                    vm.searchForm.sysGroupId = null;
                    vm.searchForm.sysGroupText = null;
                    break;
                case 'isConfirm':
                    vm.searchForm.isConfirm = 3;
                    break;
                case 'date':
                    vm.searchForm.startDate = null;
                    vm.searchForm.endDate = null;
                    break;
            }
        }
		
		vm.cancel = function(){
			$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
		}
		
		var record = 0;
		function fillDataTable(data) {
			kendo.ui.progress($("#manageRegisterCtvGrid"), true);
			vm.manageRegisterCtvGridOptions = kendoConfig
					.getGridOptions({
						autoBind : true,
						scrollable : true,
						resizable : true,
						editable : false,
						dataBinding : function() {
							record = (this.dataSource.page() - 1)
									* this.dataSource.pageSize();
						},
						reorderable : true,
						dataSource : {
							serverPaging : true,
							schema : {
								total : function(response) {
									$("#appCountRegisterCtv").text(
											"" + response.total);
									vm.countRegisterCtv = response.total;
									return response.total;
								},
								data : function(response) {
									kendo.ui.progress($("#manageRegisterCtvGrid"), false);
									return response.data;
								}
							},
							transport : {
								read : {
									url : Constant.BASE_SERVICE_URL
											+ RestEndpoint.AIO_SYS_USER_SERVICE_URL
											+ "/doSearchRegisterCtv",
									contentType : "application/json; charset=utf-8",
									type : "POST"
								},
								parameterMap : function(options, type) {
									var obj = angular.copy(vm.searchForm);
									obj.page = options.page;
									obj.pageSize = options.pageSize;
									return JSON.stringify(obj);
								}
							},
							pageSize : 10
						},
						toolbar : [ {
							name : "actions",
							template : '<div class=" pull-left ">'
//									+ '<button class="btn btn-qlk padding-search addQLK" '
//									+ 'ng-click="vm.add()" uib-tooltip="Tạo mới" translate style="width: 100px;white-space:normal;">Tạo mới</button>'
//									+ '<button class="btn btn-qlk padding-search-right TkQLK "'
//									+ 'ng-click="vm.importProject()" uib-tooltip="Import tiếp xúc" translate>Import</button>'
									+ '</div>'
									+ '<div class="btn-group pull-right margin_top_button margin_right10">'
									+ '<i data-toggle="dropdown" class="tooltip1" aria-expanded="false">'
									+ '<span class="tooltipArrow"></span>'
									+ '<span class="tooltiptext">Cài đặt</span>'
									+ '<i class="fa fa-cog" aria-hidden="true"></i>'
									+ '</i>'
									+ '<i class="tooltip1 action-button excelQLK" ng-click="vm.exportFile()" aria-hidden="true"><span class="tooltipArrow"></span><span class="tooltiptext">Xuất Excel</span></i>'
									+ '<div class="dropdown-menu hold-on-click dropdown-checkboxes" role="menu">'
									+ '<label ng-repeat="column in vm.manageRegisterCtvGrid.columns | filter: vm.gridColumnShowHideFilter">'
									+ '<input type="checkbox" checked="column.hidden" ng-click="vm.showHideWorkItemColumn(column, 1)"> {{column.title}}'
									+ '</label>' + '</div>' + '</div>'
						} ],
						columnMenu : false,
						noRecords : true,
						messages : {
							noRecords : gettextCatalog
									.getString("<div style='margin:5px'>Không có kết quả hiển thị</div>")
						},
						pageable : {
							refresh : false,
							pageSizes : [ 10, 15, 20, 25 ],
							messages : {
								display : "{0}-{1} của {2} kết quả",
								itemsPerPage : "kết quả/trang",
								empty : "<div style='margin:5px'>Không có kết quả hiển thị</div>"
							}
						},
						columns : [
								{
									title : "STT",
									field : "stt",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									template : function() {
										return ++record;
									}
								},
								{
									title : "Ngày đăng ký",
									field : "createdDate",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									}
								},
								{
									title : "Đơn vị",
									field : "sysGroupLv2Code",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:left;white-space:normal;"
									}
								},
								{
									title : "Mã nhân viên",
									field : "loginName",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									}
								},
								{
									title : "Họ và tên",
									field : "fullName",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:right;white-space:normal;"
									}
								},
								{
									title : "Tỉnh/Thành phố",
									field : "provinceNameCtvXddd",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:right;white-space:normal;"
									}
								},
								{
									title : "Quận/Huyện",
									field : "districtName",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:right;white-space:normal;"
									}
								},
								{
									title : "Phường/Xã",
									field : "communeName",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:right;white-space:normal;"
									}
								},
								{
									title : "Địa chỉ",
									field : "address",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
//									template: function(dataItem){
//										if(dataItem.communeName){
//											return dataItem.address + ", " + dataItem.communeName + ", " + dataItem.districtName + ", " + dataItem.provinceNameCtvXddd;
//										} else {
//											return dataItem.address;
//										}
//									}
								},
								{
									title : "Số điện thoại",
									field : "phoneNumber",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									}
								},
								{
									title : "Email",
									field : "email",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									}
								},
								{
									title : "Ngày sinh",
									field : "userBirthday",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									}
								},
								{
									title : "Số CMT/ĐKKD",
									field : "taxCode",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									}
								},
								{
									title : "Số điện thoại TK ViettelPay",
									field : "accountNumber",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									}
								},
								{
									title : "Số hợp đồng",
									field : "contractCode",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									}
								},
								{
									title : "Trạng thái",
									field : "status",
									width : '140px',
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:left;white-space:normal;"
									},
									template : function(dataItem) {
										var text = '';
										vm.listStatus.some(function(e) {
											if (e.value == dataItem.status) {
												text = e.text;
												return true;
											}
										});
										return text;
									}
								},
								{
									title : "Thao tác",
									field : "action",
									headerAttributes : {
										style : "text-align:center;white-space:normal;"
									},
									attributes : {
										style : "text-align:center;white-space:normal;"
									},
									template : function(dataItem) {
										var html = '<div class="text-center">'
												+ '<button style=" border: none; background-color: white;" id="updateId"  ng-click="vm.edit(dataItem)" class=" icon_table "'
												+ 'uib-tooltip="Sửa" translate>'
												+ '<i class="fa fa-pencil" aria-hidden="true" style="color:#e0d014"></i>'
												+ '</button>'
												+ '<button style=" border: none; background-color: white;" id="removeId"  ng-click="vm.remove(dataItem)" class=" icon_table "'
												+ 'uib-tooltip="Xoá" translate ng-hide="dataItem.status!=2">'
												+ '<i class="fa fa-trash" aria-hidden="true" style="color: #337ab7;" ></i>'
												+ '</button>' + '</div>';
										return html;
									}
								} ]
					});
		}
		
		vm.add = function(){
			vm.isCreateNew = true;
			vm.addForm = {};
			vm.addForm.listImageCmt = [];
			vm.addForm.listImageHk = [];
			vm.addForm.listImageHd = [];
			vm.fileLstCmt = [];
			vm.fileLstHk = [];
			vm.fileLstHd = [];
			vm.provinceIdXddd = null;
			var teamplateUrl = "coms/manageRegisterCtv/manageRegisterCtvAddPopup.html";
			var title = "Tạo mới tài khoản CTV";
			var windowId = "ADD_REGISTER_CTV";
			CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '85%', '85%', "deptAdd");
		}
		
		vm.edit = function(dataItem){
			vm.isCreateNew = false;
			vm.status = dataItem.status;
			vm.addForm = angular.copy(dataItem);
			vm.addForm.listImageCmt = [];
			vm.addForm.listImageHk = [];
			vm.addForm.listImageHd = [];
			vm.fileLstCmt = [];
			vm.fileLstHk = [];
			vm.fileLstHd = [];
			vm.provinceIdXddd = null;
			var teamplateUrl = "coms/manageRegisterCtv/manageRegisterCtvPopup.html";
			var title = "Cập nhật tài khoản CTV";
			var windowId = "UPDATE_REGISTER_CTV";
			manageRegisterCtvService.getImageById(vm.addForm).then(function(data){
				if(data){
					vm.fileLstCmt = angular.copy(data.listImageCmt);
					vm.fileLstHk =  angular.copy(data.listImageHk);
					vm.fileLstHd =  angular.copy(data.listImageHd);
					vm.addForm.listImageCmt =  angular.copy(vm.fileLstCmt);
					vm.addForm.listImageHk =  angular.copy(vm.fileLstHk);
					vm.addForm.listImageHd =  angular.copy(vm.fileLstHd);
				}
				CommonService.populatePopupCreate(teamplateUrl, title, null, vm, windowId, true, '85%', '85%', "deptAdd");
			}).catch(function(e){
				toastr.error("Có lỗi xảy ra !");
				return;
			});
		}
		
		vm.submitAttachFileCmt = submitAttachFileCmt;
		function submitAttachFileCmt(){
	    	sendFile("fileCmt",callback);
	    }
		
		vm.submitAttachFileHk = submitAttachFileHk;
		function submitAttachFileHk(){
	    	sendFile("fileHk",callback);
	    }
		
		vm.submitAttachFileHd = submitAttachFileHd;
		function submitAttachFileHd(){
	    	sendFile("fileHd",callback);
	    }
		
	    function sendFile(id, callback){
	    	var files = $('#'+id)[0].files;
	    	if(!$("#"+id)[0].files[0]){
	    		toastr.warning("Bạn chưa chọn file");
	    		setTimeout(function () {
					$(".k-upload-files.k-reset").find("li").remove();
					$(".k-upload-files").remove();
					$(".k-upload-status").remove();
					$(".k-upload.k-header").addClass("k-upload-empty");
					$(".k-upload-button").removeClass("k-state-focused");
				}, 10);
	    		return;
	    	}
	    	if(!htmlCommonService.checkFileExtension(id)){
				toastr.warning("File không đúng định dạng cho phép");
				setTimeout(function () {
					$(".k-upload-files.k-reset").find("li").remove();
					$(".k-upload-files").remove();
					$(".k-upload-status").remove();
					$(".k-upload.k-header").addClass("k-upload-empty");
					$(".k-upload-button").removeClass("k-state-focused");
				}, 10);
				return;
			}
			var formData = new FormData();
			jQuery.each($("#"+id)[0].files, function(i, file) {
					 formData.append('multipartFile'+i, file);
			});
			
			return   $.ajax({
	            url:Constant.BASE_SERVICE_URL+"fileservice/uploadATTTImageAIO?folder="+ Constant.UPLOAD_FOLDER_IMAGES,
	            type: "POST",
	            data: formData,
	            enctype: 'multipart/form-data',
	            processData: false,
	            contentType: false,
	            cache: false,
	            success:function(data) {
	            	readImages(files, id);
	            	callback(data, files, id);
	            }	 
	        });
	    };
	    
	    vm.callback = callback;
		function callback(data, files, id){
			for(var i = 0; i< data.length; i++){
				var file = {};
	    		file.name=files[i].name;
	        	file.createdDate = htmlCommonService.getFullDate();
	        	file.createdUserName = Constant.userInfo.VpsUserInfo.fullName;
	        	file.createdUserId = Constant.userInfo.VpsUserInfo.sysUserId;
	        	file.filePath = data[i];
	        	if(id=="fileCmt"){
	        		file.type="121";
		        	vm.fileLstCmt.push(file);
	        	} 
	        	
	        	if(id=="fileHk"){
	        		file.type="HK";
		        	vm.fileLstHk.push(file);
	        	}
	        	
	        	if(id=="fileHd"){
	        		file.type="HĐ";
		        	vm.fileLstHd.push(file);
	        	}
	        	setTimeout(function () {
					$(".k-upload-files.k-reset").find("li").remove();
					$(".k-upload-files").remove();
					$(".k-upload-status").remove();
					$(".k-upload.k-header").addClass("k-upload-empty");
					$(".k-upload-button").removeClass("k-state-focused");
				}, 10);
			}

//	    	$('#filePackageMaterial').val(null);
//	    	saveImage();
		};
	    
//		function saveImage(){
//			var obj = {};
//			obj.workItemId = vm.workItemOSSearch.workItemId;
//			obj.listImage = vm.fileLst;
//			workItemOSService.saveImage(obj).then(function(result){
//				toastr.success("Đính kèm ảnh thành công ");
//			});
//		};
		
		function readImages(files, id) {
	        var errors = "";

	        if (!files) {
	            errors += "Trình duyệt không hỗ trợ, vui lòng sử dụng Chrome hoặc Firefox!";
	        }

	        if (files && files[0]) {
	            for (var i = 0; i < files.length; i++) {
	                var file = files[i];

	                if ((/\.(png|jpeg|jpg)$/i).test(file.name)) {
	                    readImage(file);
	                } else {
	                    errors += file.name + ": Không hỗ trợ định dạng!";
	                }

	            }
	        }

	        // Handle errors
	        if (errors) {
	            toastr.error(errors);
	        }

	        function readImage(file) {
	            var reader = new FileReader();
	            reader.addEventListener("load", function () {
	            	if(id=="fileCmt"){
	            		vm.addForm.listImageCmt.push(toImageUtilObj(file.name, reader.result.substring(reader.result.indexOf(",") + 1)));
		        	} 
		        	
		        	if(id=="fileHk"){
		        		vm.addForm.listImageHk.push(toImageUtilObj(file.name, reader.result.substring(reader.result.indexOf(",") + 1)));
		        	}
		        	
		        	if(id=="fileHd"){
		        		vm.addForm.listImageHd.push(toImageUtilObj(file.name, reader.result.substring(reader.result.indexOf(",") + 1)));
		        	}
	                $scope.$apply();
	            });

	            reader.readAsDataURL(file);
	        }

	        function toImageUtilObj(name, base64String) {
	            return {
	                base64String: base64String,
	                name: name
	            }
	        }
	    }
		
		 function validateEmail($email) {
		    	var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
		    	return emailReg.test( $email );
		    }
		
		//Lưu lại
		vm.save = function(){
			var msg = " không được để trống !";
			if(vm.addForm.fullName==null || $("#fullName").val().trim()==""){
				toastr.warning("Họ và tên" + msg);
				$("#fullName").focus();
				return;
			}
			
			if(vm.addForm.provinceNameCtvXddd==null || $("#provinceName").val().trim()==""){
				toastr.warning("Tỉnh/thành phố" + msg);
				$("#provinceName").focus();
				return;
			}
			
			if(vm.addForm.districtName==null || $("#districtName").val().trim()==""){
				toastr.warning("Quận/huyện" + msg);
				$("#districtName").focus();
				return;
			}
			
			if(vm.addForm.communeName==null || $("#communeName").val().trim()==""){
				toastr.warning("Phường/xã" + msg);
				$("#communeName").focus();
				return;
			}
			
			if(vm.addForm.address==null || $("#address").val().trim()==""){
				toastr.warning("Địa chỉ" + msg);
				$("#address").focus();
				return;
			}
			
//			if(vm.addForm.email==null || $("#email").val().trim()==""){
//				toastr.warning("Email" + msg);
//				$("#email").focus();
//				return;
//			} else {
//	    		if(!validateEmail($("#email").val())){
//	    			toastr.warning("Email không đúng định dạng");
//		    		$("#email").focus();
//		    		return;
//	    		}
//	    	}
			
			if(vm.addForm.password==null || $("#password").val().trim()==""){
				toastr.warning("Password" + msg);
				$("#password").focus();
				return;
			}
			
			if(vm.addForm.phoneNumber==null || $("#phoneNumber").val().trim()==""){
				toastr.warning("Số điện thoại" + msg);
				$("#phoneNumber").focus();
				return;
			} else {
	    		var vnf_regex = /((09|03|07|08|05)+([0-9]{8})\b)/g;
	        	if(kendo.parseFloat($("#phoneNumber").val())<0){
	        		toastr.warning("Số điện thoại sai định dạng");
	        		$("#phoneNumber").focus();
	            	return;
	        	} else {
	        		if(!vnf_regex.test($("#phoneNumber").val())) {
	        			toastr.warning("Số điện thoại sai định dạng");
	                	 $("#phoneNumber").focus();
	                	 return;
	                }
	        	}
	    	} 
			
			if(vm.addForm.userBirthday==null || $("#userBirthday").val().trim()==""){
				toastr.warning("Ngày sinh" + msg);
				$("#userBirthday").focus();
				return;
			}
			
			if(vm.addForm.taxCode==null || $("#taxCode").val().trim()==""){
				toastr.warning("Số CMT/ĐKKD" + msg);
				$("#taxCode").focus();
				return;
			}
			
			if(vm.addForm.accountNumber==null || $("#accountNumber").val().trim()==""){
				toastr.warning("Số điện thoại TK ViettelPay" + msg);
				$("#accountNumber").focus();
				return;
			} else {
				var vnf_regex = /((09|03|07|08|05)+([0-9]{8})\b)/g;
	        	if(kendo.parseFloat($("#accountNumber").val())<0){
	        		toastr.warning("Số điện thoại TK ViettelPay sai định dạng");
	        		$("#accountNumber").focus();
	            	return;
	        	} else {
	        		if(!vnf_regex.test($("#accountNumber").val())) {
	        			toastr.warning("Số điện thoại TK ViettelPay sai định dạng");
	                	 $("#accountNumber").focus();
	                	 return;
	                }
	        	}
			}
			
//			if(vm.addForm.taxCodeUser==null || $("#taxCodeUser").val().trim()==""){
//				toastr.warning("Mã số thuế" + msg);
//				$("#taxCodeUser").focus();
//				return;
//			}
			
			if(vm.addForm.contractCode==null || $("#contractCode").val().trim()==""){
				toastr.warning("Số hợp đồng" + msg);
				$("#contractCode").focus();
				return;
			}
			
			if(vm.addForm.parentName==null || $("#parentName").val().trim()==""){
				toastr.warning("Tên người giới thiệu" + msg);
				$("#parentName").focus();
				return;
			}
			
			if(vm.addForm.parentPhone==null || $("#parentPhone").val().trim()==""){
				toastr.warning("Số điện thoại người giới thiệu" + msg);
				$("#parentPhone").focus();
				return;
			} else {
	    		var vnf_regex = /((09|03|07|08|05)+([0-9]{8})\b)/g;
	        	if(kendo.parseFloat($("#parentPhone").val())<0){
	        		toastr.warning("Số điện thoại người giới thiệu sai định dạng");
	        		$("#parentPhone").focus();
	            	return;
	        	} else {
	        		if(!vnf_regex.test($("#parentPhone").val())) {
	        			toastr.warning("Số điện thoại người giới thiệu sai định dạng");
	                	 $("#parentPhone").focus();
	                	 return;
	                }
	        	}
	    	} 
			
			if(vm.addForm.provinceIdXddd==null){
				toastr.warning("Chưa chọn đơn vị tỉnh quản lý");
				$("#provinceNameXddd").focus();
				return;
			}
			
			if(vm.addForm.sysGroupId==null){
				toastr.warning("Chưa chọn cụm đội huyện quản lý");
				$("#sysGroupName").focus();
				return;
			}
			
			if(vm.fileLstCmt.length==0){
				toastr.warning("Chưa đính kèm ảnh CMT");
				return;
			}
			
			if(vm.fileLstHk.length==0){
				toastr.warning("Chưa đính kèm ảnh hộ khẩu");
				return;
			}
			
			if(vm.fileLstHd.length==0){
				toastr.warning("Chưa đính kèm ảnh hợp đồng");
				return;
			}
			
			if(vm.fileLstCmt.length + vm.fileLstHk.length + vm.fileLstHd.length < 5){
				toastr.warning("Cần đính kèm ít nhất 5 ảnh");
				return;
			}
			
			var obj = {};
			obj = angular.copy(vm.addForm);
			obj.listImageCmt = angular.copy(vm.fileLstCmt);
			obj.listImageHk = angular.copy(vm.fileLstHk);
			obj.listImageHd = angular.copy(vm.fileLstHd);
			if(vm.isCreateNew){
				manageRegisterCtvService.saveRegisterCtv(obj).then(function(result){
					if(result!=undefined && result.error){
						toastr.error(result.error);
						return;
					}
					
					toastr.success("Tạo mới tài khoản CTV thành công");
					$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
					doSearch();
				}).catch(function(e){
					toastr.error("Có lỗi xảy ra !");
					return;
				});
			} else {
				manageRegisterCtvService.updateRegisterCtv(obj).then(function(result){
					if(result!=undefined && result.error){
						toastr.error(result.error);
						return;
					}
					
					toastr.success("Cập nhật tài khoản CTV thành công");
					$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
					doSearch();
				}).catch(function(e){
					toastr.error("Có lỗi xảy ra !");
					return;
				});
			}
		}
		
		vm.remove = function(dataItem){
			manageRegisterCtvService.removeRecord(dataItem).then(function(result){
				if(result!=undefined && result.error){
					toastr.error(result.error);
					return;
				}
				
				toastr.success("Xoá tài khoản CTV thành công");
				$("div.k-window-actions > a.k-window-action > span.k-i-close").click();
				doSearch();
			}).catch(function(e){
				toastr.error("Có lỗi xảy ra !");
				return;
			});
		}
		
		vm.listRemove = [
        	{
        		title: "Thao tác",
        	}
        	];

        vm.listConvert = [
        	{
        		field: "status",
        		data: {
        			0: 'Hết Hiệu lực',
        			1: 'Chờ công ty phê duyệt',
        			2: 'Chờ CNKT phê duyệt',
        			3: 'CNKT từ chối',
        			4: 'Công ty phê duyệt',
        			5: 'Công ty từ chối',
        			6: 'Tất cả',
        		}
        	},
	        
        ];
        
		vm.exportFile = function(){
			vm.searchForm.page = null;
        	vm.searchForm.pageSize = null;
        	kendo.ui.progress($("#manageRegisterCtvGrid"), true);
        	manageRegisterCtvService.doSearch(vm.searchForm).then(function(data){
        		kendo.ui.progress($("#manageRegisterCtvGrid"), false);
        		CommonService.exportFile(vm.manageRegisterCtvGrid, data.data, vm.listRemove, vm.listConvert, "Danh sách tài khoản CTV");
        	}).catch(function(e){
        		kendo.ui.progress($("#manageRegisterCtvGrid"), false);
        		toastr.error("Có lỗi xảy ra khi xuất file !");
        	});
		}
		
		vm.sysGroupACOptions = {
                dataTextField: "text",
                placeholder: "Nhập mã, tên của đơn vị hoặc cụm đội",
                select: function (e) {
                    var dataItem = this.dataItem(e.item.index());
                    vm.searchForm.sysGroupText = dataItem.text;
                    vm.searchForm.sysGroupId = dataItem.id;
                },
                pageSize: 10,
                dataSource: {
                    serverFiltering: true,
                    transport: {
                        read: {
                            url: Constant.BASE_SERVICE_URL + "departmentRsService/department/" + "getForAutocompleteDept",
                            contentType: "application/json; charset=utf-8",
                            type: "POST"
                        },
                        parameterMap: function (options, type) {
                            var obj = {};
                            obj.pageSize = 10;
                            obj.name = vm.searchForm.sysGroupText;

                            return JSON.stringify(obj);
                        }
                    }
                },
                headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                    '<p class="col-md-6 text-header-auto border-right-ccc">Mã đơn vị</p>' +
                    '<p class="col-md-6 text-header-auto">Tên đơn vị</p></div>',
                    template: '<div class="row" >' +
                	'<div class="col-xs-5" style="float:left">#: data.code #</div>' +
                	'<div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>', 
                change: function (e) {
                    if (e.sender.value() === '') {
                        vm.searchForm.sysGroupText = null;
                        vm.searchForm.sysGroupId = null;
                    }
                },
                ignoreCase: false
            };
		
            vm.openPopupGroup = function () {
                var templateUrl = 'wms/popup/findDepartmentPopUp.html';
                var title = gettextCatalog.getString("Tìm kiếm đơn vị");
                CommonService.populatePopupDept(templateUrl, title, null, null, vm, null, 'string', false, '92%', '89%');
            };
            
            vm.onSave = function (data) {
                vm.searchForm.sysGroupText = data.code + '-' + data.name;
                vm.searchForm.sysGroupId = data.id;
            };
            
            vm.selectedDept1 = false;
            vm.provinceIdXddd = null;
            vm.provinceXdddOptions = {
                    dataTextField: "name",
                    dataValueField: "sysGroupId",
                	placeholder:"Nhập mã hoặc tên đơn vị",
                    select: function (e) {
                        vm.selectedDept1 = true;
                        var dataItem = this.dataItem(e.item.index());
                        vm.addForm.provinceNameXddd = dataItem.name;
                        vm.addForm.provinceIdXddd = dataItem.sysGroupId;
                        vm.provinceIdXddd = dataItem.sysGroupId;
                        vm.addForm.sysGroupName = null;
                        vm.addForm.sysGroupId = null;
                    },
                    pageSize: 10,
                    open: function (e) {
                        vm.selectedDept1 = false;
                    },
                    dataSource: {
                        serverFiltering: true,
                        transport: {
                            read: function (options) {
                                vm.selectedDept1 = false;
                                return Restangular.all("sysUserRsService/" + "getSysGroupTree").post({
                                    name: vm.addForm.provinceNameXddd,
                                    pageSize: vm.provinceXdddOptions.pageSize
                                }).then(function (response) {
                                    options.success(response.data);
                                }).catch(function (err) {
                                    console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                                });
                            }
                        }
                    },
                    headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                        '<p class="col-md-6 text-header-auto border-right-ccc">Mã đơn vị</p>' +
                        '<p class="col-md-6 text-header-auto">Tên đơn vị</p></div>',
                    template: '<div class="row" >' +
                    	'<div class="col-xs-5" style="float:left">#: data.code #</div>' +
                    	'<div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>', 
                    change: function (e) {
                        if (e.sender.value() === '') {
                        	vm.addForm.provinceNameXddd = null;
                            vm.addForm.provinceIdXddd = null;
                            vm.addForm.sysGroupName = null;
                            vm.addForm.sysGroupId = null;
                            vm.provinceIdXddd = null;
                        }
                    },
                    ignoreCase: false
               };
            
            vm.sysGroupOptions = {
                    dataTextField: "name",
                    dataValueField: "sysGroupId",
                	placeholder:"Nhập mã hoặc tên cụm đội",
                    select: function (e) {
                        vm.selectedDept1 = true;
                        var dataItem = this.dataItem(e.item.index());
                        vm.addForm.sysGroupName = dataItem.name;
                        vm.addForm.sysGroupId = dataItem.sysGroupId;
                    },
                    pageSize: 10,
                    open: function (e) {
                        vm.selectedDept1 = false;
                    },
                    dataSource: {
                        serverFiltering: true,
                        transport: {
                            read: function (options) {
                            	if(vm.provinceIdXddd){
                            		vm.selectedDept1 = false;
                                    return Restangular.all("sysUserRsService/" + "getSysGroupTree").post({
                                        name: vm.addForm.sysGroupName,
                                        sysGroupId:  vm.provinceIdXddd,
                                        pageSize: vm.sysGroupOptions.pageSize
                                    }).then(function (response) {
                                        options.success(response.data);
                                    }).catch(function (err) {
                                        console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
                                    });
                            	} else {
                            		toastr.warning("Chưa chọn đơn vị tỉnh");
                            		options.success([]);
                            		return;
                            	}
                            }
                        }
                    },
                    headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
                        '<p class="col-md-6 text-header-auto border-right-ccc">Mã đơn vị</p>' +
                        '<p class="col-md-6 text-header-auto">Tên đơn vị</p></div>',
                    template: '<div class="row" >' +
                    	'<div class="col-xs-5" style="float:left">#: data.code #</div>' +
                    	'<div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>', 
                    change: function (e) {
                        if (e.sender.value() === '') {
                        	vm.addForm.sysGroupName = null;
                            vm.addForm.sysGroupId = null;
                        }
                    },
                    ignoreCase: false
               };
         
            vm.openPopupSysGroup = function (param) {
                var templateUrl = 'wms/popup/findDepartmentPopUp.html';
                var title = gettextCatalog.getString("Tìm kiếm đơn vị");
                CommonService.populatePopupGroupCtv(templateUrl, title, null, null, vm, param, 'string', false, '92%', '89%');
            };
            
            vm.onSaveGroupProvince = function (dataItem) {
            	vm.addForm.provinceNameXddd = dataItem.name;
                vm.addForm.provinceIdXddd = dataItem.sysGroupId;
                vm.provinceIdXddd = dataItem.sysGroupId;
                vm.addForm.sysGroupName = null;
                vm.addForm.sysGroupId = null;
            };
            
            vm.onSaveSysGroup = function (dataItem) {
            	vm.addForm.sysGroupName = dataItem.name;
                vm.addForm.sysGroupId = dataItem.sysGroupId;
            };
            
          // Tỉnh
            vm.isSelect = false;
            vm.provinceCodeOptions = {
    		        dataTextField: "name",
    		        dataValueField: "catProvinceId",
    				placeholder:"Nhập mã hoặc tên tỉnh",
    		        select: function (e) {
    		            vm.isSelect = true;
    		            var dataItem = this.dataItem(e.item.index());
    		            vm.addForm.provinceId = dataItem.catProvinceId;
    		            vm.addForm.provinceCode = dataItem.code;
    		            vm.addForm.areaId = dataItem.areaId;
    		            vm.addForm.provinceIdCtvXddd = dataItem.catProvinceId;
    		            vm.addForm.provinceNameCtvXddd = dataItem.name;
    		        },
    		        pageSize: 10,
    		        open: function (e) {
    		            vm.isSelect = false;
    		        },
    		        dataSource: {
    		            serverFiltering: true,
    		            transport: {
    		                read: function (options) {
    		                    vm.isSelect = false;
    		                    return Restangular.all("tangentCustomerRestService/doSearchProvince").post({
    		                        keySearch: vm.addForm.provinceNameCtvXddd,
    		                        pageSize: vm.provinceCodeOptions.pageSize,
    		                        page: 1
    		                    }).then(function (response) {
    		                        options.success(response.data);
    		                    }).catch(function (err) {
    		                        console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
    		                    });
    		                }
    		            }
    		        },
    				headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
    				'<p class="col-md-6 text-header-auto border-right-ccc">Mã tỉnh</p>' +
    				'<p class="col-md-6 text-header-auto">Tên tỉnh</p>' +
    				'</div>',
    		        template: '<div class="row" ><div class="col-xs-5" style="float:left">#: data.code #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
    		        change: function (e) {
    		        	if (!vm.isSelect || $("#provinceName").val()==null) {
    		        		vm.addForm.areaId = null;
    		        		vm.addForm.provinceId = null;
    			            vm.addForm.provinceCode = null;
    			            vm.addForm.districtId = null;
    			            vm.addForm.districtName = null;
    			            vm.addForm.communeId = null;
    			            vm.addForm.communeName = null;
    			            vm.addForm.provinceIdCtvXddd = null;
        		            vm.addForm.provinceNameCtvXddd = null;
    			            $("#provinceName").val(null);
    		            }
    		        },
    		        close: function (e) {
    		            if (!vm.isSelect || $("#provinceName").val()==null) {
    		            	vm.addForm.areaId = null;
    		            	vm.addForm.provinceId = null;
    			            vm.addForm.provinceCode = null;
    			            vm.addForm.districtId = null;
    			            vm.addForm.districtName = null;
    			            vm.addForm.communeId = null;
    			            vm.addForm.communeName = null;
    			            vm.addForm.provinceIdCtvXddd = null;
        		            vm.addForm.provinceNameCtvXddd = null;
    			            $("#provinceName").val(null);
    		            }
    		        }
    		    }
            
          //Quận huyện
            vm.isSelectDistrict = false;
            vm.districtNameOptions = {
    		        dataTextField: "districtName",
    		        dataValueField: "districtName",
    				placeholder:"Nhập mã hoặc tên quận/huyện",
    		        select: function (e) {
    		            vm.isSelectDistrict = true;
    		            var dataItem = this.dataItem(e.item.index());
    		            vm.addForm.districtId = dataItem.districtId;
    		            vm.addForm.districtName = dataItem.districtName;
    		        },
    		        pageSize: 10,
    		        open: function (e) {
    		            vm.isSelectDistrict = false;
    		        },
    		        dataSource: {
    		            serverFiltering: true,
    		            transport: {
    		                read: function (options) {
    		                    vm.isSelectDistrict = false;
    		                    return Restangular.all("tangentCustomerRestService/doSearchDistrictByProvinceCode").post({
    		                    	districtName: vm.addForm.districtName,
    		                        pageSize: vm.districtNameOptions.pageSize,
    		                        provinceId: vm.addForm.areaId,
    		                        page: 1
    		                    }).then(function (response) {
    		                        options.success(response.data);
    		                    }).catch(function (err) {
    		                        console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
    		                    });
    		                }
    		            }
    		        },
    				headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
//    				'<p class="col-md-6 text-header-auto border-right-ccc">Mã tỉnh</p>' +
    				'<p class="col-md-6 text-header-auto">Tên quận/huyện</p>' +
    				'</div>',
    		        template: '<div class="row" ><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.districtName #</div> </div>',
    		        change: function (e) {
    		        	if (!vm.isSelectDistrict || $("#districtName").val()==null) {
    		        		vm.addForm.districtId = null;
    			            vm.addForm.districtName = null;
    			            vm.addForm.communeId = null;
    			            vm.addForm.communeName = null;
    			            $("#districtName").val(null);
    		            }
    		        },
    		        close: function (e) {
    		            if (!vm.isSelectDistrict  || $("#districtName").val()==null) {
    		            	vm.addForm.districtId = null;
    			            vm.addForm.districtName = null;
    			            vm.addForm.communeId = null;
    			            vm.addForm.communeName = null;
    			            $("#districtName").val(null);
    		            }
    		        }
    		    }
            
          //Xã phường
            vm.communeNameOptions = {
    		        dataTextField: "communeName",
    		        dataValueField: "communeName",
    				placeholder:"Nhập mã hoặc tên xã/phường",
    		        select: function (e) {
    		            vm.isSelect = true;
    		            var dataItem = this.dataItem(e.item.index());
    		            vm.addForm.communeId = dataItem.communeId;
    		            vm.addForm.communeName = dataItem.communeName;
    		        },
    		        pageSize: 10,
    		        open: function (e) {
    		            vm.isSelect = false;
    		        },
    		        dataSource: {
    		            serverFiltering: true,
    		            transport: {
    		                read: function (options) {
    		                    vm.isSelect = false;
    		                    return Restangular.all("tangentCustomerRestService/doSearchCommunneByDistrict").post({
			                    	communeName: vm.addForm.communeName,
			                        pageSize: vm.communeNameOptions.pageSize,
			                        districtId: vm.addForm.districtId,
			                        page: 1
			                    }).then(function (response) {
			                        options.success(response.data);
			                    }).catch(function (err) {
			                        console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
			                    });
    		                 }
    		            }
    		        },
    				headerTemplate: '<div class="dropdown-header row text-center k-widget k-header">' +
//    				'<p class="col-md-6 text-header-auto border-right-ccc">Mã tỉnh</p>' +
    				'<p class="col-md-6 text-header-auto">Tên xã phường</p>' +
    				'</div>',
    		        template: '<div class="row" ><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.communeName #</div> </div>',
    		        change: function (e) {
    		        	if (!vm.isSelect || $("#communeName").val()==null) {
    		        		vm.addForm.communeId = null;
    			            vm.addForm.communeName = null;
    			            vm.addForm.areaId = null;
    			            $("#communeName").val(null);
    		            }
    		        },
    		        close: function (e) {
    		            if (!vm.isSelect || $("#communeName").val()==null) {
    		            	vm.addForm.communeId = null;
    			            vm.addForm.communeName = null;
    			            vm.addForm.areaId = null;
    			            $("#communeName").val(null);
    		            }
    		        }
    		    }
            
		// Controller end
	}
})();
