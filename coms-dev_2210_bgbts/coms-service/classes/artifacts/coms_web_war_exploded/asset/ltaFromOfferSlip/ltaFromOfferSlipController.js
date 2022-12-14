(function() {
	'use strict';
	var controllerId = 'ltaFromOfferSclipController';
	
	angular.module('MetronicApp').controller(controllerId, catAssetDetailUpdateController);
	
	function catAssetDetailUpdateController($scope, $rootScope, $timeout, $compile, gettextCatalog, kendoConfig, $kWindow, CommonService, PopupConst, Restangular, RestEndpoint, detailCatAssetUpdateService,Constant) {
		var vm = this;
		var callFromOtherForm=false;
		vm.validatorOptions = kendoConfig.get('validatorOptions');
		initFormData();
		
		//Danh sach catAssetCost
		vm.listCatAssetCost=[];	
		vm.danhMucHangMucChiPhi=[
		     {id:1, name:'Chi phí vật tư A cấp'},
			 {id:2, name:'Chi phí xây dựng/lắp đặt'},			 
			 {id:3,name:'Chi phí tư vấn đầu tư xây dựng'},
			 {id:4,name:'Chi phí QLDA'},
			 {id:5,name:'Chi phí bồi thường tái định cư'},			 		
			 {id:6, name:'Chi phí thiết bị'},
			 {id:10,name:'Chi phí khác'}		 		
        ];
		vm.getTotalChiPhi=function(id){
			var total=0;
			for(var i=0;i<vm.listCatAssetCost.length;i++){
				if(vm.listCatAssetCost[i].loacType==id){
					if(vm.listCatAssetCost[i].loacValue!=null){
						total+=parseInt(vm.listCatAssetCost[i].loacValue);
					}
					
				}
			}
			return total;
		}
		
		//Template cuar man hinh chi phi
		vm.catAssetCostItemTemplate=function(){
			var config={					
					loacType:3,
					loacValue:null,
					voucherCode:'',
					voucherStatus:1,
					voucherDate:kendo.toString(new Date(), "dd/MM/yyyy"),
					attachName:'',
					attachId:null,
					attachIdEncrypted:null,
					hasUpload:'N',
					longTermAssetCostId:null
			};
			config.copy=function(item){			
				config.loacType=item.loacType;
				config.loacValue=item.loacValue;
				config.voucherCode=item.voucherCode;
				config.voucherDate=item.voucherDate;
				config.attachName=item.attachName;
				config.attachId=item.attachId;
				config.hasUpload=item.hasUpload;
				config.attachIdEncrypted=item.attachIdEncrypted;
				config.longTermAssetCostId=item.longTermAssetCostId;
				return config;
				
			};
			
			config.changeAttachFile=function(event){
				vm.changeAttachCostFile(event,config);
			};
			return config;
		}
		//extend ds chi phi
		vm.extendListCatAssetCost =function(){
			vm.listCatAssetCost.push(vm.catAssetCostItemTemplate());
		}
		
		//voi case khoi tao thi thuc hien init ban dau//TODO: day vao ham init
		vm.extendListCatAssetCost();
		/*
		 * remove CostItem
		 */
		vm.removeCostItem=function(index){
			if(vm.listCatAssetCost.length>1){
				vm.listCatAssetCost.splice(index,1);
			}
		}
		
		vm.listFileDinhKemTemplate=function(){
			return {
				"content":'',
				"attachName":'Test.xml',
				"attachIdEncrypted":null,
				"fileName":"FileName"
			};
			
		}
		
		//Danh sách file đính kèm ở task thứ 3
		vm.listFileDinhKem=[vm.listFileDinhKemTemplate()];
		
		//vm.listFileDinhKem.push(vm.listFileDinhKemTemplate());
		//
		vm.extendListDinhKem=function(){
			vm.listFileDinhKem.push({});
		}
		vm.removeFileDinhKem=function(index){
			if(vm.listFileDinhKem.length==1){
				return;
			}else{
				vm.listFileDinhKem.splice(index,1);
			}
			
		}
		

		function initFormData() {
			vm.detail = {
				stationId: '',
				stationName: '',
				constructId: '',
				constructName: '',
				groupId: '',
				groupName: '',
				useGroupId: '',
				useGroupName: '',
				assetGroupId: '',
				assetGroupName: '',
				assetGroupCode: '',
				assetTypeId: '',
				assetTypeName: '',
				assetTypeCode: '',
				assetSourceId: '',
				assetSourceName: '',
				assetSourceCode: '',
				catAssetCodeId: '',
				assetNameName: '',
				assetNameCode: '',
				lotaCode: '',
				originalPrice: 0,
				handoverCode: '',
				voucherDate: '',
				createdSource: 1,
				lotaStatus: 0,
				depreciationStartDate: '',
				isSentErp: 0,
				
				lotaIndex: '',
				listAssetEntities: [],
				listAssetCost: [],
				longTermAssetId: 0,
				isActive: 1,
				lotaType: '',
				voucherType: 1,
				attachName: '',
				hasUpload: '',
				listAssetCostDetail:[{voucherDate:new Date(),
					loacType:'',
					longTermAssetCostId:''
				}]

			}
			
			vm.merEntityValue = 0;
			vm.dataMerEntity = [];
			vm.constructionAcceptanceValue = 0;
			vm.dataConstructionAcceptance = [];
			vm.totalCost = 0;
			vm.totalDis = 0;
			vm.lotaType = [{id: 1, name: 'TSCĐ'}, {id: 2, name: 'CCDC'}];
			vm.voucherType = [
				{id: 1, name: 'Xây dựng cơ bản'}
				//,{id: 2, name: 'Mua sắm'}
			];
			vm.createdSource = [
    			/*{id: 1, name: 'Đầu tư xây dựng'},
    			{id: 2, name: 'Khoa học công nghệ'},
    			{id: 3, name: 'Chi phí sản xuất kinh doanh'},
    			{id: 4, name: 'Quỹ phúc lợi'},			
    			{id: 5, name: 'Khác'}*/
    			{id: 1, name: 'Vốn đầu tư của chủ sở hữu'},
    			{id: 2, name: 'Vốn ngân sách NN cấp'},
    			{id: 3, name: 'Vốn vay'},
    			{id: 4, name: 'Quỹ Phúc lợi'},
    			{id: 5, name: 'Quỹ phát triển khoa học và công nghệ'},			
    			{id: 6, name: 'Nguồn vốn huy động khác'}
			];
			
			vm.commonSearch = {
				keySearch: '',
				pageSize: '',
				level: '',
				parentId: ''
			};
			vm.searchConstruct = {
				keySearch: '',
				pageSize: '',
				level: '',
				parentId: ''
			};
			vm.useGroupSearch = {
				keySearch: '',
				pageSize: '',
				level: '',
				parentId: ''
			};
			
			vm.assetGroupSearch = {caacName: '', caacCode: '', caacLevel: 1};
			vm.assetTypeSearch = {caacName: '', caacCode: '', caacLevel: 2};
			vm.catAssetSourceSearch = {caacName: '', caacCode: '', caacLevel: 3};
			vm.catAssetCodeSearch = {caacName: '', caacCode: '', caacLevel: 4};
			
			vm.gridCommon = [{
				title: "Mã", 
				field: "constrtCode", 
				width: 120
			}, {
				title: "Tên", 
				field: "constrtName", 
				width: 120
			}];
			
			vm.files = [];
			var list = [];
			vm.folder = '';
			detailCatAssetUpdateService.getCatAssetFolder().then(function(data) {
				vm.folder = data.folder;
			}, function(errResponse){
				console.error('Error while getCatAssetFolder');
			});
			
			vm.notChange = true;
			vm.countRow = 1;
			vm.itemTemp = {};
			vm.tempStr = "";
		}
	
		vm.autocompleteConstr= {
	            dataTextField: "constructName",
	            select: function(e) {
	                var dataItem = this.dataItem(e.item.index());
	                vm.detail.constructId = dataItem.constructId; // thành id
	                vm.detail.constructName = dataItem.constructName;//thành name
	                vm.detail.stationCode=dataItem.stationCode;//mã trạm
	                vm.detail.investProjectCode=dataItem.investProjectCode;//mã dự án
	                vm.detail.contractCode=dataItem.contractCode;// mã hwpj đồng
	            },
	            pageSize: 10,
	            dataSource: {
	                serverFiltering: true,
	                transport: {
	                    read: function(options) {
	                        return detailCatAssetUpdateService.autocompleteConstrFromOfferSlip({keySearch:vm.detail.constructName,pageSize:vm.autocompleteConstr.pageSize }).then(function(response){
	                            options.success(response);
	                        }).catch(function (err) {
	                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
	                        });
	                    }
	                }
	            },
	            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px;float:left">#: data.constructCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.constructName #</div> </div>',
	            change: function(e) {
	                if (e.sender.value() === '') {
	                    vm.detail.constructId = null; // thành id
	                    vm.detail.constructName = null;//thành name
	                }
	            },
	            ignoreCase: false
	        };
		
		 vm.sysGroupOptions = {
		            dataTextField: "name",
		            select: function(e) {
		                var dataItem = this.dataItem(e.item.index());
		                vm.detail.groupId = dataItem.groupId; // thành id
		                vm.detail.groupName = dataItem.groupCode;//thành name
		            },
		            pageSize: 10,
		            dataSource: {
		                serverFiltering: true,
		                transport: {
		                    read: function(options) {
		                        return Restangular.all("assetReportServiceRest/" + 'filterSysGroup').post({keySearch:vm.detail.groupName,pageSize:vm.sysGroupOptions.pageSize }).then(function(response){
		                            options.success(response);
		                        }).catch(function (err) {
		                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
		                        });
		                    }
		                }
		            },
		            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px;float:left">#: data.groupCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
		            change: function(e) {
		                if (e.sender.value() === '') {
		                    vm.detail.groupId = null; // thành id
		                    vm.detail.groupName = null;//thành name
		                }
		            },
		            ignoreCase: false
		        };
		 	vm.sysUserGroupOptions = {
		            dataTextField: "name",
		            select: function(e) {
		                var dataItem = this.dataItem(e.item.index());
		                vm.detail.useGroupId = dataItem.groupId; // thành id
		                vm.detail.useGroupName = dataItem.groupCode;//thành name
		            },
		            pageSize: 10,
		            dataSource: {
		                serverFiltering: true,
		                transport: {
		                    read: function(options) {
		                        return Restangular.all("assetReportServiceRest/" + 'filterSysGroup').post({keySearch:vm.detail.useGroupName,pageSize:vm.sysUserGroupOptions.pageSize }).then(function(response){
		                            options.success(response);
		                        }).catch(function (err) {
		                            console.log('Không thể kết nối để lấy dữ liệu: ' + err.message);
		                        });
		                    }
		                }
		            },
		            template:'<div class="row" ><div class="col-xs-5" style="padding: 0px;float:left">#: data.groupCode #</div><div  style="padding-left: 5px;width:auto;overflow: hidden"> #: data.name #</div> </div>',
		            change: function(e) {
		                if (e.sender.value() === '') {
		                    vm.detail.useGroupId = null; // thành id
		                    vm.detail.useGroupName = null;//thành name
		                }
		            },
		            ignoreCase: false
		        };
		vm.cancel = cancel;
		function cancel(){
			if (vm.detail.longTermAssetId !== 0){
				vm.detail = vm.itemTemp;
			}
			else{
				vm.totalDis = 0;

				vm.detail = {
					stationId: vm.detail.stationId,
					stationName: vm.detail.stationName,
					stationCode: vm.detail.stationCode,
					constructId: vm.detail.constructId,
					constructName: vm.detail.constructName,
					groupId: CommonService.getUserInfo().groupId,
					groupCode:CommonService.getUserInfo().groupCode,
					groupName: CommonService.getUserInfo().groupName,
					useGroupId:  CommonService.getUserInfo().groupId,
					useGroupName: CommonService.getUserInfo().groupName,
					useGroupCode:CommonService.getUserInfo().groupCode,
					handoverCode:vm.detail.handoverCode,
					depreciationStartDate:vm.detail.depreciationStartDate,
					dataMerEntity:vm.detail.dataMerEntity,
					assetGroupId: '',
					assetGroupName: '',
					assetGroupCode: '',
					assetTypeId: '',
					assetTypeName: '',
					assetTypeCode: '',
					assetSourceId: '',
					assetSourceName: '',
					assetSourceCode: '',
					catAssetCodeId: '',
					assetNameName: '',
					assetNameCode: '',

					lotaCode: '',
					originalPrice: 0,
					voucherDate: '',
					createdSource: '',
					lotaStatus: '',
					
					lotaIndex: '',
					listAssetEntities: [],
					listAssetCost: [],
					lstAttachDetailDto:[],
					longTermAssetId: 0,
					isActive: 1,
					lotaType: '',
					
					attachName: '',
					hasUpload: '',
					costDate:[new Date()],
					listAssetCostDetail:[{voucherDate:new Date(),
						loacType:'',
						longTermAssetCostId:''
					}]
				};
				vm.countRow=1;
			}
			var str=templateCost(vm.countRow);
			var compiledeHTML = $compile(str)($scope);
			$("#tblDistribution tbody").html(compiledeHTML)
			var compiledeTempHTML = $compile(vm.tempStr)($scope);
			$("#tblDistribution tbody tr:last").before(compiledeTempHTML);
			caculateCost();
		}
		
		vm.save = save;
		function save(){	
			if (vm.validator.validate()) {
//				if ($("#filePath").val() != ""){
					$("#detailAssetForm #btnSave").css("pointer-events", "none");
					
					// Thong tin cho bang LONG_TERM_ASSET_ENTITIES 
					vm.detail.listAssetCost = [];
					vm.detail.listAssetEntities = vm.dataMerEntity;
					vm.detail.lstAttachDetailDto=vm.listFileDinhKem;
					
					
					// Them thong tin nguoi dung nhap tu bang phan bo vao
					for(var i=0;i<vm.listCatAssetCost.length;i++){
						var item=vm.listCatAssetCost[i];						
						if(item.attachName!=null&&item.attachName.trim()!=''){
							if(item.loacValue!=''&&item.voucherCode!=''){
								vm.detail.listAssetCost.push(vm.listCatAssetCost[i]);
							}else{
								toastr.error(CommonService.translate("Chưa nhập đủ thông tin"));
								return;
							}
							if(item.fileSize>20){
								toastr.error(gettextCatalog.getString("File " + item.fileName + " có dung lượng quá lớn!"));
								return;
							}
						}
						
					}
						
					vm.detail.originalPrice=Math.round(vm.detail.originalPrice);
					vm.detail.lotaStatus = $("#detailAssetForm input[name='lotaStatus']:checked").val();
					//Kiem tra xem ma tai san co phai bat buoc hinh thanh tai san khong
				
					
					if(vm.detail.isRequiredLTA==true){
						vm.detail.lotaType = 1;
					}else{
						if (vm.detail.originalPrice > 30000000){
							vm.detail.lotaType = 1;
						}
						else{
							vm.detail.lotaType = 2;
						}
					}
					

					if (!vm.isCreatAsset){
						// Sua thong tin
						detailCatAssetUpdateService.updateLtaFromOfferSlip(vm.detail).then(function(result){
							toastr.success(gettextCatalog.getString("Lưu thay đổi thành công!"));
							$("#detailAssetForm #btnSave").removeAttr('style');
						}, function(errResponse) {
							$("#detailAssetForm #btnSave").removeAttr('style');
							toastr.error(gettextCatalog.getString("Lỗi xuất hiện!"));
						});
					}
					else{
						// Them moi thong tin
						detailCatAssetUpdateService.createLtaFromOfferSlip(vm.detail).then(function(result){
							vm.isCreatAsset = false;
							toastr.success(gettextCatalog.getString("Lưu thay đổi thành công!"));
							vm.detail.longTermAssetId = result.longTermAssetId;
							$("#detailAssetForm #btnSave").removeAttr('style');

						}, function(errResponse) {
							$("#detailAssetForm #btnSave").removeAttr('style');
							toastr.error(gettextCatalog.getString("Lỗi xuất hiện!"));
						});
					}
					
//				}
//				else{
//					toastr.error(gettextCatalog.getString("File đình kèm BBBG không được để trống"));
//				}
			}
			else{
				$("#detailAssetForm #btnSave").removeAttr('style');
			}
		}
		
		vm.choseFile = choseFile;
		function choseFile(){
			$("#detailAssetForm #fileBBBG").click();
		}
		
		$scope.changeFile = function(){
			if (typeof $("#detailAssetForm #fileBBBG")[0].files[0] !== "undefined"){
				$("#detailAssetForm #filePath").val($("#detailAssetForm #fileBBBG")[0].files[0].name);
				
				
				var formData = new FormData();
				var assetUpload = $("#fileBBBG")[0].files[0];
				var filename=$("#fileBBBG")[0].files[0].name;
				formData.append('assetUpload', assetUpload);
				Restangular.one(Constant.UPLOAD_RS_SERVICE).withHttpConfig(
						{
							transformRequest : angular.identity
						}).customPOST(formData, '', undefined, {
					'Content-Type' : 'multipart/form-data'
				}).then(function(successResponse) {
					console.log(successResponse);
					if(successResponse.length>0){						
						vm.detail.attachDisplayName=filename;
						vm.detail.attachName=successResponse[0];
						
						vm.detail.attachEncryptedPath =successResponse[0];
						vm.detail.hasUpload = 'Y';
					}
				});
			}
		}
		
		/* 
		 * Thay doi file
		 */
		vm.changeAttachCostFile=function(event,item){	
				var filename = event.target.files[0].name;				
		        var formData = new FormData();
				var assetUpload = event.target.files[0];
				formData.append('assetUpload', assetUpload);
				var filesize = ((assetUpload.size/1024)/1024).toFixed(4);
				if(filesize<20){
					//TODO:Hanhls1 thong bao uploadfile
				}
				Restangular.one(Constant.UPLOAD_RS_SERVICE).withHttpConfig(
						{
							transformRequest : angular.identity
						}).customPOST(formData, '', undefined, {
					'Content-Type' : 'multipart/form-data'
				}).then(function(successResponse) {					
					if(successResponse.length>0){						
						item.attachDisplayName=filename;
						item.attachName=successResponse[0];	
						item.fileSize=filesize;
						item.attachIdEncrypted=null;
					}
				});
		};
		
		
		$scope.changeDisFile = function(element){
			if (typeof $(element)[0].files[0] !== "undefined"){
				var fileName=$(element)[0].files[0].name;				
				
				//begin upload
				var formData = new FormData();
				var assetUpload = $(element)[0].files[0];
				formData.append('assetUpload', assetUpload);
				Restangular.one(Constant.UPLOAD_RS_SERVICE).withHttpConfig(
						{
							transformRequest : angular.identity
						}).customPOST(formData, '', undefined, {
					'Content-Type' : 'multipart/form-data'
				}).then(function(successResponse) {					
					if(successResponse.length>0){
						//vm.termassetentity.attachName = $("#changeBBBG")[0].files[0].name;
					//	$(element).val(successResponse[0]);
						$(element).next().val(fileName);
						$(element).next().next().next().val(successResponse[0]);
						//đẩy file đã mã hóa
						$(element).next().attr('fileEncryptedPath',successResponse[0]);						
					}
				});
			}
		}
		
		$scope.choseDisFile = function(element){
			$(element).parent().prev().prev().click();
		}
		
		$scope.txtChoseDisFile = function(element){
			$(element).prev().click();
		}
		

	
		
		vm.showPopup = showPopup;
		function showPopup(popupId){
			if (popupId === 'merEntity'){
				var templateUrl = 'asset/catAssetList/popup.html';
				var title = "Danh sách vật tư thiết bị";
				vm.merEntityGridOptions = kendoConfig.getGridOptions({
					autoBind: true,
					dataSource: vm.dataMerEntity,
					noRecords: true,
                    resizable: true,                    
					messages: {
						noRecords: gettextCatalog.getString("There is no data on current page")
					},
					columns: [{
						title: gettextCatalog.getString("STT"),
						field: 'lineNo',
						width: 68,
					}, {
						title: gettextCatalog.getString("Mã"),
						field: "code",
						width: 68
					}, {
						title: gettextCatalog.getString("Tên"),
						field: "name",
						width: 120
					}, {
						title: gettextCatalog.getString("SN"),
						field: "serialNumber",
						template:'# if(serialNumber>0){#'+'#=serialNumber#' +'#} else {#'+'#="N/A"#'+'#}#',
						width: 68
					}, {
						title: gettextCatalog.getString("Đơn vị"),
						field: "unitName",						
						width: 80
					}, {
						title: gettextCatalog.getString("S/L"),
						field: "merWeight",
						width: 70
					}, {
						title: gettextCatalog.getString("Đơn giá"),
						field: "vndUnitPrice",
						
						attributes: {
							style: "text-align:right;"
						},
						width: 90
					}, {
						title: gettextCatalog.getString("Tổng tiền(vnd)"),
						field: "total",
						attributes: {
							style: "text-align:right;"
						},
						width: 110
					}]
				});
				CommonService.populateDataToGrid(templateUrl, title, vm.merEntityGridOptions, vm, 'merEntity');
			}
			else{
				var templateUrl = 'asset/catAssetList/popup.html';
				var title = "Danh sách chi phí xây lắp";
				vm.constructionAcceptance = kendoConfig.getGridOptions({
					autoBind: true,
					dataSource: vm.dataConstructionAcceptance,
					noRecords: true,
					messages: {
						noRecords: gettextCatalog.getString("There is no data on current page")
					},
					columns: [{
						title: gettextCatalog.getString("STT"),
						field: "lineNo",
						width: 50,
					}, {
						title: gettextCatalog.getString("Mã đầu việc"),
						field: "code",
						width: 100
					}, {
						title: gettextCatalog.getString("Tên đầu việc"),
						field: "name",
						width: 150
					}, {
						title: gettextCatalog.getString("Khối lượng"),
						field: "merWeight",
						
						width: 150
					}, {
						title: gettextCatalog.getString("Đơn giá"),
						field: "vndUnitPrice",
						width: 150
					}, {
						title: gettextCatalog.getString("Thành tiền (VNĐ)"),
						field: "total",						
						width: 150
					}]
				});
				CommonService.populateDataToGrid(templateUrl, title, vm.constructionAcceptance, vm, 'constructionAcceptance');
			}
		}
		vm.onCancel =function (){

		};

		
		$scope.$watch('vm.detail.assetGroupId',function(id){
			if (vm.detail.longTermAssetId !== 0){
				// Sua thong tin
				vm.assetTypeSearch.superParentId = id;
				
				if (!vm.notChange){
					vm.detail.assetTypeId = '';
					vm.detail.assetTypeName = '';
					vm.detail.assetSourceId = '';
					vm.detail.assetSourceName = '';
					vm.detail.catAssetCodeId = '';
					vm.detail.assetNameName = '';
					vm.detail.lotaIndex = '';
					vm.detail.lotaCode = '';
				}
				//vm.notChange = false;
			}
			else{
				vm.detail.assetTypeId = '';
				vm.detail.assetTypeName = '';
				vm.detail.assetSourceId = '';
				vm.detail.assetSourceName = '';
				vm.detail.catAssetCodeId = '';
				vm.detail.assetNameName = '';
				vm.detail.lotaIndex = '';
				vm.detail.lotaCode = '';
					
				vm.assetTypeSearch.superParentId = id;
			}
		});
		
		$scope.$watch('vm.detail.assetTypeId',function(id){
			if (vm.detail.longTermAssetId !== 0){
				// Sua thong tin
				vm.catAssetSourceSearch.superParentId = id;
				
				if (!vm.notChange){
					vm.detail.assetSourceId = '';
					vm.detail.assetSourceName = '';
					vm.detail.catAssetCodeId = '';
					vm.detail.assetNameName = '';
					vm.detail.lotaIndex = '';
					vm.detail.lotaCode = '';
				}
				//vm.notChange = false;
			}
			else{
				// Them moi
				vm.detail.assetSourceId = '';
				vm.detail.assetSourceName = '';
				vm.detail.catAssetCodeId = '';
				vm.detail.assetNameName = '';
				vm.detail.lotaIndex = '';
				vm.detail.lotaCode = '';
				
				vm.catAssetSourceSearch.superParentId = id;
			}
		});
		
		$scope.$watch('vm.detail.assetSourceId',function(id){
			if (vm.detail.longTermAssetId !== 0){
				// Sua thong tin
				vm.catAssetCodeSearch.superParentId = id;
				
				if (!vm.notChange){
					vm.detail.catAssetCodeId = '';
					vm.detail.assetNameName = '';
					vm.detail.lotaIndex = '';
					vm.detail.lotaCode = '';
				}
				//vm.notChange = false;
			}
			else{
				// Them moi
				vm.detail.catAssetCodeId = '';
				vm.detail.assetNameName = '';
				vm.detail.lotaIndex = '';
				vm.detail.lotaCode = '';
				
				vm.catAssetCodeSearch.superParentId = id;
			}
		});
		
		$scope.$watch('vm.detail.catAssetCodeId', function(id){
			if (vm.detail.longTermAssetId !== 0){
				// Sua thong tin
				if (!vm.notChange){
					if (typeof id !== "undefined" && id != null && id.toString() != ""){
						detailCatAssetUpdateService.genAssetCode({catAssetCodeId: id}).then(function(d) {
							vm.detail.lotaIndex = (d.plain().lotaIndex);
							vm.detail.lotaCode = d.plain().assetGroupCode+vm.detail.lotaIndex ;
						}, function(errResponse){
							console.error('Error while genAssetCode');
						});						
					}
					else{
						vm.detail.lotaIndex = '';
						vm.detail.lotaCode = '';
					}
				}
				//vm.notChange = false;
			}
			else{
				if (typeof id !== "undefined" && id != null && id.toString() != ""){
					detailCatAssetUpdateService.genAssetCode({catAssetCodeId: id}).then(function(d) {
						vm.detail.lotaIndex = d.plain().lotaIndex;
						vm.detail.lotaCode = d.plain().assetGroupCode+vm.detail.lotaIndex ;
					}, function(errResponse){
						console.error('Error while genAssetCode');
					});
				}
				else{
					vm.detail.lotaIndex = '';
					vm.detail.lotaCode = '';
				}
			}
			if(vm.detail.catAssetCodeId!=null&&vm.detail.catAssetCodeId>0){
				var objCheckRequiredToConstrucLTA={
						catAssetCodeId:vm.detail.catAssetCodeId,
						caacLevel:4
				}
				
				detailCatAssetUpdateService.isRequiredLTA(objCheckRequiredToConstrucLTA).then(function(result){
					
					vm.detail.isRequiredLTA = result.plain().data;
					
				},function(erprResponse){
					console.warn("Loi khi goi den service kiem tra nhom ts la bat buoc len tscd ");
				});
			}
		
		});

		
		$scope.$watch('vm.detail',function(obj){
			if (obj.longTermAssetId != 0){
				vm.notChange = false;
			}
		})
		
		
		
		
		$scope.changeCurrency = function(element){
			if ($(element).val() != ""){
				caculateCost();
			}
		}
		
		vm.isCreatAsset=true;
		//Su kien khi re-load den trang nay
		$scope.$watch($rootScope.Asset_CatAssetDetail_FromOfferSlip, function() {
			if($rootScope.Asset_CatAssetDetail_FromOfferSlip!=null){
				if($rootScope.Asset_CatAssetDetail_FromOfferSlip.type==1){
					vm.isCreatAsset=true;
					//vm.loadConstructionAcceptainInfo($rootScope.Asset_CatAssetDetail_init.value);
				}else if($rootScope.Asset_CatAssetDetail_FromOfferSlip.type==2){// Trường hợp update
					vm.isCreatAsset=false;
					vm.loadDetailFromLtaId($rootScope.Asset_CatAssetDetail_FromOfferSlip.value);
				}
				$rootScope.Asset_CatAssetDetail_FromOfferSlip=null;
			}
			
		});
		
		//Lay thong tin lien quan den bien ban ban giao
		vm.loadConstructionAcceptainInfo =function(constructionAcceptainId){
			Restangular.one("longTermAssetServiceRest/getConstructionAcceptanceById",constructionAcceptainId).get().then(function(successResponse){
				//cac thong tin tren form
				vm.detail.stationId=successResponse.stationId;
				vm.detail.stationCode=successResponse.stationCode;
				vm.detail.constructId=successResponse.constructId;
				vm.detail.constructName=successResponse.constrCode;
				vm.detail.groupName=CommonService.getUserInfo().groupName;
				vm.detail.groupCode=CommonService.getUserInfo().groupCode;
				vm.detail.groupId = CommonService.getUserInfo().groupId; // thành id
				vm.detail.useGroupName=CommonService.getUserInfo().groupName;
				vm.detail.useGroupCode=CommonService.getUserInfo().groupCode;
				vm.detail.useGroupId = CommonService.getUserInfo().groupId; // thành id
				vm.detail.investProjectCode=successResponse.investProjectCode;
				vm.detail.handoverCode=successResponse.handoverCode;
				vm.detail.depreciationStartDate=successResponse.handoverDate;
				var date=new Date();
				vm.dataMerEntity = successResponse.lstMerEntity;
				//Tai san
				for(var i = 0; i < vm.dataMerEntity.length; i++) {
					var obj = vm.dataMerEntity[i];
					vm.merEntityValue += (obj.merWeight != null && obj.vndUnitPrice != null ? obj.merWeight * obj.vndUnitPrice : 0);
					vm.dataMerEntity[i].total = (obj.merWeight != null && obj.vndUnitPrice != null ? obj.merWeight * obj.vndUnitPrice : 0);
					vm.dataMerEntity[i].lineNo = i + 1;
				}

				//chi phi
				vm.dataConstructionAcceptance = successResponse.lstDataConstructionAcceptance;
				for(var j = 0; j < vm.dataConstructionAcceptance.length; j++){
					var obj = vm.dataConstructionAcceptance[j];
					vm.constructionAcceptanceValue += (obj.merWeight != null && obj.vndUnitPrice != null ? obj.merWeight * obj.vndUnitPrice : 0);
					//vm.dataConstructionAcceptance.total = (obj.merWeight != null && obj.vndUnitPrice != null ? obj.merWeight * obj.vndUnitPrice : 0);
					vm.dataConstructionAcceptance[j].total=(obj.merWeight != null && obj.vndUnitPrice != null ? obj.merWeight * obj.vndUnitPrice : 0);
					vm.dataConstructionAcceptance[j].lineNo = j + 1;
				}

				vm.totalCost=vm.merEntityValue+vm.constructionAcceptanceValue;
//				
				vm.detail.originalPrice = vm.totalCost + vm.totalDis;
				
				$("div.loading-effect").hide();
			}, function(errorResponse){
				console.log(errorResponse);
			});
		};
		vm.loadDetailFromLtaId=function(ltaId){
			vm.objSearch = {longTermAssetId:ltaId};
			detailCatAssetUpdateService.getDetail(vm.objSearch).then(function(d) {
				vm.detail = d;
				vm.itemTemp = angular.copy(vm.detail, vm.itemTemp);
				vm.detail.attachDisplayName=vm.detail.attachName;
				//vm.detail.
			
				/** 
				 * Search AssetCost
				 */
				detailCatAssetUpdateService.searchAssetCost({longTermAssetId: vm.detail.longTermAssetId}).then(function(result) {
										
					var str = "";
					var currencyString = 0;
					vm.listCatAssetCost=[];
					for(var i=0;i<result.plain().length;i++){
						var item = result.plain()[i];					
							var assetCostItem=vm.catAssetCostItemTemplate().copy(item);
							assetCostItem.attachDisplayName=assetCostItem.attachName;
							vm.listCatAssetCost.push(assetCostItem);							
							currencyString+= assetCostItem.loacValue;					
					}
					if(vm.listCatAssetCost.length==0){
						vm.listCatAssetCost.push(vm.catAssetCostItemTemplate());
					}
				
					vm.totalDis=currencyString;					
					vm.tempStr = str;
				}, function(errResponse){
					console.error('Error while get list AssetCost');
				});
				/**
				 * Search AssetAttachDetail
				 * 
				 */
				detailCatAssetUpdateService.getLtaAttachDetail({longTermAssetId: vm.detail.longTermAssetId}).then(function(result) {
					
					vm.listFileDinhKem=[];
					
					for(var i=0;i<result.plain().length;i++){
						var item = result.plain()[i];
						vm.listFileDinhKem.push(item);
					}
					if(vm.listFileDinhKem.length==0){
						vm.listFileDinhKem.push({});
					}				
				}, function(errResponse){
					console.error('Error while getLtaAttachDetail');
				}
				);
			}, function(errResponse){
				console.error('Error while getDetail');
			});
		};
		
		
		
		
		vm.caculateCost =caculateCost;
		function caculateCost(){
			vm.totalDis = 0;
			/*var disItem = $("#tblDistribution tbody tr");
			for (var i = 0; i < disItem.length; i++) {
				if ($(disItem[i]).find(".value").val() != "" && $(disItem[i]).find(".value").val() != "0"){
//					vm.totalDis += parseFloat($(disItem[i]).find(".value").val().replaceAll(".", ""));
					vm.totalDis += parseFloat($(disItem[i]).find(".value").val());
				}
			}*/
			vm.totalCost=vm.merEntityValue+vm.constructionAcceptanceValue;
			for(var i=0;i<vm.listCatAssetCost.length;i++){
				vm.totalDis+=parseFloat(vm.listCatAssetCost[i].loacValue);
			}
			vm.detail.originalPrice = vm.totalDis + vm.totalCost;

		}
		

	}
})();



function addDisRow(element){
	var scope = angular.element(document.getElementById("formDetail")).scope();
	scope.$apply(function () {
		scope.addDisRow(element);
    });
}
function removeDisRow(element){
	var scope = angular.element(document.getElementById("formDetail")).scope();
	scope.$apply(function () {
		scope.removeDisRow(element);
    });
}
function changeCurrency(element){
	var scope = angular.element(document.getElementById("formDetail")).scope();
	scope.$apply(function () {
		scope.changeCurrency(element);
    });
}

String.prototype.replaceAll = function (strTarget, strSubString) {
	var strText = this;
	strText = strText.toLowerCase();
	var intIndexOfMatch = strText.indexOf(strTarget);
	while (intIndexOfMatch != -1) {
		strText = strText.replace(strTarget, strSubString)
		intIndexOfMatch = strText.indexOf(strTarget);
	}
	return strText.replace('"', '');
}
String.prototype.replaceAt = function(index, character) {
    return this.substring(0, index) + character + this.substring((index + 1) + character.length);
}