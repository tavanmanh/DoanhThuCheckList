(function () {
    'use strict';

    var controllerId = 'CatPartsListController';

    angular.module('MetronicApp').controller(controllerId, CatPartsListController);
    
    /* @ngInject */
    function CatPartsListController($scope, $rootScope, $timeout, Constant, kendoConfig, WindowService, 
    		CatPartsService, $kWindow, CommonService, Restangular, $localStorage, $compile, RestEndpoint) {    	
        var vm = this;
		// vm.homeLoader = $('body').loadingIndicator({
    		// useImage: false,
    		// showOnInit: false
    	// }).data("loadingIndicator");
		vm.isShowAdvancedSearch=true;
        vm.data = {
			catPartsId: 0, 			
			partnerName: '',
			address: '',
			phone: '',
			description: '',
			fax: '',
			taxCode: '',
			code: '',
			refId: '',
			type: '',
			abbName: '',
			groupId: '',
			delegatePerson: '',
			title: '',
			accountNumber: '',
			bussinessNumber: '',
			status: '',
			createdDate: '',
			proposalNote: '',
			acceptorId: '',
			acceptDate: '',
			acceptNote: '',
			rejectNote: '',
			numDispath: '',
			numSubmitsion: '',
			catPartnerTypeId: '',
			creatPartnerDate: '',
			reasonChangePartner: '',
			delegatePosition: '',
			accountHolder: '',
			identityCard: '',
			bankName: '',
			stationCodeExpected: '',
			stationIdExpected: '',
			modifieldDate: '',
			catBankBranchId: '',
			isLock: '',
			issueBy: '',
			dateRange: '',
			checkPartner: '',
			dateUpdatePartnersContract: '',
			representationName: '',
			representationPosition: '',
			note: '',
			account: '',
			transactionName: '',
			representativeName: '',
			representativePosition: '',
			isActive: 1, 			
			creatorId:'',
			creatorGroupId:'',
			creatorDate:'',
			updatorId:'',
			updatorGroupId:'',
			updatorDate:'',
		};
		vm.data_temp = angular.copy(vm.data);
    	vm.criteriaSearch = {
			name: '', 
			value: '', 
			description: ''
		};
    
        vm.isCreateNew = false;
        vm.showDetail = false;//Kh�n d�ng
		vm.showSearch = false;// Kh�ng d�ng
        vm.add = add;
        vm.edit = edit;//Kh�ng d�ng
		vm.detail=detail;
        
        vm.remove = remove;
		
        vm.onCheckAll = onCheckAll;
        vm.onCheck = onCheck;
        vm.checkedItems = [];
        vm.doSearch = doSearch;
        vm.canceldoSearch = canceldoSearch;
		
		vm.rowIndex = 0;
		var message = {
			noDataGrid: CommonService.translate("Kh�ng c� d? li?u tr�n trang hi?n t?i"),
			lineRequired: CommonService.translate("B?n c?n ch?n m?t d�ng tr??c"),
			recordRequired: CommonService.translate("B?n c?n ch?n m?t d�ng tr??c"),
			needShowDetail : CommonService.translate("C?n hi?n th? ? ch? ?? Chi Ti?t!"),
			positionLast: CommonService.translate("?� ? b?n ghi cu?i"),
			positionFirst: CommonService.translate("?� ? b?n ghi ??u"),
			duplicateValue: CommonService.translate("M� danh m?c ?� t?n t?i"),
			createError: CommonService.translate("L?i khi t?o m?i b?n ghi"),
			createSuccess: CommonService.translate("T?o m?i th�nh c�ng"),
			updateError: CommonService.translate("L?i khi c?p nh?t b?n ghi"),
			updateSuccess: CommonService.translate("C?p nh?t th�nh c�ng"),
			deleteConfirm: CommonService.translate("B?n th?c s? mu?n x�a b?n ghi ?"),
			deleteError: CommonService.translate("X�a kh�ng th�nh c�ng"),
			deleteSuccess: CommonService.translate("X�a th�nh c�ng"),
			usedRecord: CommonService.translate("B?n ghi ?� ???c s? d?ng"),
			getDataError: CommonService.translate("L?i x?y ra khi l?y d? li?u"),
			adOrgIdRequired: CommonService.translate("??n v? kh�ng ???c ?? tr?ng"),
			partnerNameRequired: CommonService.translate("partnerName kh�ng ???c ?? tr?ng"),			
			addressRequired: CommonService.translate("address kh�ng ???c ?? tr?ng"),			
			phoneRequired: CommonService.translate("phone kh�ng ???c ?? tr?ng"),			
			descriptionRequired: CommonService.translate("description kh�ng ???c ?? tr?ng"),			
			faxRequired: CommonService.translate("fax kh�ng ???c ?? tr?ng"),			
			taxCodeRequired: CommonService.translate("taxCode kh�ng ???c ?? tr?ng"),			
			codeRequired: CommonService.translate("code kh�ng ???c ?? tr?ng"),			
			refIdRequired: CommonService.translate("refId kh�ng ???c ?? tr?ng"),			
			typeRequired: CommonService.translate("type kh�ng ???c ?? tr?ng"),			
			abbNameRequired: CommonService.translate("abbName kh�ng ???c ?? tr?ng"),			
			groupIdRequired: CommonService.translate("groupId kh�ng ???c ?? tr?ng"),			
			delegatePersonRequired: CommonService.translate("delegatePerson kh�ng ???c ?? tr?ng"),			
			titleRequired: CommonService.translate("title kh�ng ???c ?? tr?ng"),			
			accountNumberRequired: CommonService.translate("accountNumber kh�ng ???c ?? tr?ng"),			
			bussinessNumberRequired: CommonService.translate("bussinessNumber kh�ng ???c ?? tr?ng"),			
			statusRequired: CommonService.translate("status kh�ng ???c ?? tr?ng"),			
			createdDateRequired: CommonService.translate("createdDate kh�ng ???c ?? tr?ng"),			
			proposalNoteRequired: CommonService.translate("proposalNote kh�ng ???c ?? tr?ng"),			
			acceptorIdRequired: CommonService.translate("acceptorId kh�ng ???c ?? tr?ng"),			
			acceptDateRequired: CommonService.translate("acceptDate kh�ng ???c ?? tr?ng"),			
			acceptNoteRequired: CommonService.translate("acceptNote kh�ng ???c ?? tr?ng"),			
			rejectNoteRequired: CommonService.translate("rejectNote kh�ng ???c ?? tr?ng"),			
			numDispathRequired: CommonService.translate("numDispath kh�ng ???c ?? tr?ng"),			
			numSubmitsionRequired: CommonService.translate("numSubmitsion kh�ng ???c ?? tr?ng"),			
			catPartnerTypeIdRequired: CommonService.translate("catPartnerTypeId kh�ng ???c ?? tr?ng"),			
			creatPartnerDateRequired: CommonService.translate("creatPartnerDate kh�ng ???c ?? tr?ng"),			
			reasonChangePartnerRequired: CommonService.translate("reasonChangePartner kh�ng ???c ?? tr?ng"),			
			delegatePositionRequired: CommonService.translate("delegatePosition kh�ng ???c ?? tr?ng"),			
			accountHolderRequired: CommonService.translate("accountHolder kh�ng ???c ?? tr?ng"),			
			identityCardRequired: CommonService.translate("identityCard kh�ng ???c ?? tr?ng"),			
			bankNameRequired: CommonService.translate("bankName kh�ng ???c ?? tr?ng"),			
			stationCodeExpectedRequired: CommonService.translate("stationCodeExpected kh�ng ???c ?? tr?ng"),			
			stationIdExpectedRequired: CommonService.translate("stationIdExpected kh�ng ???c ?? tr?ng"),			
			modifieldDateRequired: CommonService.translate("modifieldDate kh�ng ???c ?? tr?ng"),			
			catBankBranchIdRequired: CommonService.translate("catBankBranchId kh�ng ???c ?? tr?ng"),			
			isLockRequired: CommonService.translate("isLock kh�ng ???c ?? tr?ng"),			
			issueByRequired: CommonService.translate("issueBy kh�ng ???c ?? tr?ng"),			
			dateRangeRequired: CommonService.translate("dateRange kh�ng ???c ?? tr?ng"),			
			checkPartnerRequired: CommonService.translate("checkPartner kh�ng ???c ?? tr?ng"),			
			dateUpdatePartnersContractRequired: CommonService.translate("dateUpdatePartnersContract kh�ng ???c ?? tr?ng"),			
			representationNameRequired: CommonService.translate("representationName kh�ng ???c ?? tr?ng"),			
			representationPositionRequired: CommonService.translate("representationPosition kh�ng ???c ?? tr?ng"),			
			noteRequired: CommonService.translate("note kh�ng ???c ?? tr?ng"),			
			accountRequired: CommonService.translate("account kh�ng ???c ?? tr?ng"),			
			transactionNameRequired: CommonService.translate("transactionName kh�ng ???c ?? tr?ng"),			
			representativeNameRequired: CommonService.translate("representativeName kh�ng ???c ?? tr?ng"),			
			representativePositionRequired: CommonService.translate("representativePosition kh�ng ???c ?? tr?ng"),			
        }
        
        vm.validatorOptions = kendoConfig.get('validatorOptions');
		//fetchAll();
        fillDataTable();       
        
        function getEmptyDataModel() {
        	var objReturn = {
				catPartsId: 0, 				
				partnerName: '',
				address: '',
				phone: '',
				description: '',
				fax: '',
				taxCode: '',
				code: '',
				refId: '',
				type: '',
				abbName: '',
				groupId: '',
				delegatePerson: '',
				title: '',
				accountNumber: '',
				bussinessNumber: '',
				status: '',
				createdDate: '',
				proposalNote: '',
				acceptorId: '',
				acceptDate: '',
				acceptNote: '',
				rejectNote: '',
				numDispath: '',
				numSubmitsion: '',
				catPartnerTypeId: '',
				creatPartnerDate: '',
				reasonChangePartner: '',
				delegatePosition: '',
				accountHolder: '',
				identityCard: '',
				bankName: '',
				stationCodeExpected: '',
				stationIdExpected: '',
				modifieldDate: '',
				catBankBranchId: '',
				isLock: '',
				issueBy: '',
				dateRange: '',
				checkPartner: '',
				dateUpdatePartnersContract: '',
				representationName: '',
				representationPosition: '',
				note: '',
				account: '',
				transactionName: '',
				representativeName: '',
				representativePosition: '',
								
				isActive: '', 			
				creatorId:CommonService.getUserInfo().userId,
				creatorGroupId:CommonService.getUserInfo().groupId,				
				updatorId:CommonService.getUserInfo().userId,
				updatorGroupId:CommonService.getUserInfo().groupId																																												
			};        
        	return objReturn;
		}
		
        vm.showHideColumn=function(column){
        	if (angular.isUndefined(column.hidden)) {
        		vm.catPartsGrid.hideColumn(column);
            } else if (column.hidden) {
            	vm.catPartsGrid.showColumn(column);
            } else {
            	vm.catPartsGrid.hideColumn(column);
            }
        	
        	
        }

		function buildParameters (params) {
                var _dataSearch = angular.copy(vm.criteriaSearch);
                if (params.sort && params.sort.length > 0) {
                    _dataSearch.orderBy = params.sort[0].field || "";
                    _dataSearch.orderType = params.sort[0].dir || "asc";
                }
                if (params.filter && params.filter.filters && params.filter.filters.length > 0) {
                    angular.forEach(params.filter.filters, function (value, key) {
                        _dataSearch[value.field] = value.value;
                    });
                }
                _dataSearch.page = params.page ;
                _dataSearch.size = params.pageSize;
                return _dataSearch;
            };
            
            vm.gridColumnFilter = function (item) { 
                return item.type==null||item.type !=1; 
            };
			
        function fillDataTable(data) {
			var query = '';
			var record = 0;
    		if ($rootScope.redirectRecordId !== undefined && $rootScope.redirectRecordId !== null && $rootScope.redirectRecordId > 0) {
    			query += 'redirectRecordId=' + $rootScope.redirectRecordId;
    		}    		
    		if (query !== '') {
    			query = '?' + query;
    		}
        	vm.gridOptions = kendoConfig.getGridOptions({
                autoBind: true,
                toolbar: [
                          {
                              name: "actions",
                              template: $scope.getTemplateBySelector("#CatPartsListAction")
                          }
                          ],
				
				dataBinding: function() {
                    record = (this.dataSource.page() -1) * this.dataSource.pageSize();
                    if(this.dataSource.total()<=1){
                    	this.pager.element.hide();
                    }
                   
                },

				dataSource: {
                    transport: {
                        // read: {
                           // url: RestEndpoint.BASE_SERVICE_URL + RestEndpoint.CAT_PARTS_SERVICE_URL + query,
                           // dataType: "json",
                           // type: "GET"
						   
                        // }
						read: function(e){
							var _dataSearch = buildParameters(e.data);
							CatPartsService.doSearch(_dataSearch).then(function(response){
								e.success(response.plain());
								}, function(err){
									if(err.data && err.data.status==Constant.http.BUSINESS_ERROR){
										toastr.warning(CommonService.translate(err.data.errorMessage));
									}
									e.success([]);
									console.err(err);
								}
							)
						}																		                         
                    },
					pageSize: Constant.pageSize,
                    schema: {
                        data: function(data){
							return data.rows
						},                         
						total: function (data) {
							return data.totalRow;
						},
                        model: {
                            fields: {
										createdDate: { type: "string" },										
										acceptDate: { type: "string" },										
										creatPartnerDate: { type: "string" },										
										modifieldDate: { type: "string" },										
										dateRange: { type: "string" },										
										dateUpdatePartnersContract: { type: "string" },										
                            	isActive: { type: "list" }
                           }
                        } 
                    },
                    serverPaging: true,
					serverSorting: true,
                    serverFiltering: true					
                },                				
				resizable: true,
				reorderable: true,
				editable: false,
                messages: {
                    noRecords: (message.noDataGrid)
                },
				pageable: {
					refresh: false,
					pageSizes: Constant.pageSizes,
					messages: {
						display: "{0}-{1}}" +CommonService.translate("c?a") +"{2}"+ CommonService.translate("k?t qu?"),
						itemsPerPage: CommonService.translate("k?t qu?/trang"),
						empty: CommonService.translate("Kh�ng c� k?t qu? hi?n th?")
					}
				},
                columns: [{
                	title: "STT",
                	template: function () {
                		return ++record;
                	},
                	locked:true,
                	width: 50,
                	 headerAttributes: {
 						style: "text-align:center;"
 					}
                },
				 {   
			    	title: "<input type='checkbox' name='gridchkselectall' ng-click='vm.onCheckAll($event);' ng-model='vm.chkAll' />",
			        template: "<input type='checkbox' name='gridcheckbox' ng-click='vm.onCheck($event)'/>",					
			        width: 35,
			        locked:true,
			        type:1,
			        headerAttributes: {
						style: "text-align:center;"
					}
			    },
				
				 {
					title: CommonService.translate("Tác vụ"),
			        template:dataItem =>
			        	'<div class="text-center">'
					+'<div class="btn-group btn-group-solid btn-action-group" >'
					+' <button type="button" title="Sửa"  ng-click="vm.edit(dataItem)" class="btn default padding-button box-shadows"> <i class="fa fa-pencil"></i></button>'	
					+' <button type="button" title="Xem"  ng-click="vm.detail(dataItem)" class="btn default padding-button box-shadow"> <i class="fa fa-eye"></i></button>'
					+' <button type="button" title="Xóa"  ng-click="vm.remove(dataItem)" class="btn default padding-button box-shadow"> <i class="fa fa-trash"></i></button>'
		            		+'</div>'                		               		                	
			        	
					+'</div>',
			        width: 100,	
			        locked:true,
			        type:1,
			        headerAttributes: {
						style: "text-align:center;"
					}
				},
				
				{
                    title: CommonService.translate("PARTNER_NAME"),
                    field: "partnerName",
                    width: 100
                },	
				{
                    title: CommonService.translate("ADDRESS"),
                    field: "address",
                    width: 100
                },	
				{
                    title: CommonService.translate("PHONE"),
                    field: "phone",
                    width: 100
                },	
				{
                    title: CommonService.translate("DESCRIPTION"),
                    field: "description",
                    width: 100
                },	
				{
                    title: CommonService.translate("FAX"),
                    field: "fax",
                    width: 100
                },	
				{
                    title: CommonService.translate("TAX_CODE"),
                    field: "taxCode",
                    width: 100
                },	
				{
                    title: CommonService.translate("CODE"),
                    field: "code",
                    width: 100
                },	
				{
                    title: CommonService.translate("refName"),
                    field: "refName",
                    width: 100,
                },				
				{
                    title: CommonService.translate("TYPE"),
                    field: "type",
                    width: 100
                },	
				{
                    title: CommonService.translate("ABB_NAME"),
                    field: "abbName",
                    width: 100
                },	
				{
                    title: CommonService.translate("groupName"),
                    field: "groupName",
                    width: 100,
                },				
				{
                    title: CommonService.translate("DELEGATE_PERSON"),
                    field: "delegatePerson",
                    width: 100
                },	
				{
                    title: CommonService.translate("TITLE"),
                    field: "title",
                    width: 100
                },	
				{
                    title: CommonService.translate("ACCOUNT_NUMBER"),
                    field: "accountNumber",
                    width: 100
                },	
				{
                    title: CommonService.translate("BUSSINESS_NUMBER"),
                    field: "bussinessNumber",
                    width: 100
                },	
				{
                    title: CommonService.translate("STATUS"),
                    field: "status",
                    width: 100
                },	
				{
                    title: CommonService.translate("CREATED_DATE"),
                    field: "createdDate",
                    width: 100,
                    type: 'date',
                    format: "{0:dd/MM/yyyy}",
                    parseFormats: "{0:dd/MM/yyyy}",                  
                },	
				{
                    title: CommonService.translate("PROPOSAL_NOTE"),
                    field: "proposalNote",
                    width: 100
                },	
				{
                    title: CommonService.translate("acceptorName"),
                    field: "acceptorName",
                    width: 100,
                },				
				{
                    title: CommonService.translate("ACCEPT_DATE"),
                    field: "acceptDate",
                    width: 100,
                    type: 'date',
                    format: "{0:dd/MM/yyyy}",
                    parseFormats: "{0:dd/MM/yyyy}",                  
                },	
				{
                    title: CommonService.translate("ACCEPT_NOTE"),
                    field: "acceptNote",
                    width: 100
                },	
				{
                    title: CommonService.translate("REJECT_NOTE"),
                    field: "rejectNote",
                    width: 100
                },	
				{
                    title: CommonService.translate("NUM_DISPATH"),
                    field: "numDispath",
                    width: 100
                },	
				{
                    title: CommonService.translate("NUM_SUBMITSION"),
                    field: "numSubmitsion",
                    width: 100
                },	
				{
                    title: CommonService.translate("catPartnerTypeName"),
                    field: "catPartnerTypeName",
                    width: 100,
                },				
				{
                    title: CommonService.translate("CREAT_PARTNER_DATE"),
                    field: "creatPartnerDate",
                    width: 100,
                    type: 'date',
                    format: "{0:dd/MM/yyyy}",
                    parseFormats: "{0:dd/MM/yyyy}",                  
                },	
				{
                    title: CommonService.translate("REASON_CHANGE_PARTNER"),
                    field: "reasonChangePartner",
                    width: 100
                },	
				{
                    title: CommonService.translate("DELEGATE_POSITION"),
                    field: "delegatePosition",
                    width: 100
                },	
				{
                    title: CommonService.translate("ACCOUNT_HOLDER"),
                    field: "accountHolder",
                    width: 100
                },	
				{
                    title: CommonService.translate("IDENTITY_CARD"),
                    field: "identityCard",
                    width: 100
                },	
				{
                    title: CommonService.translate("BANK_NAME"),
                    field: "bankName",
                    width: 100
                },	
				{
                    title: CommonService.translate("STATION_CODE_EXPECTED"),
                    field: "stationCodeExpected",
                    width: 100
                },	
				{
                    title: CommonService.translate("stationNameExpected"),
                    field: "stationNameExpected",
                    width: 100,
                },				
				{
                    title: CommonService.translate("MODIFIELD_DATE"),
                    field: "modifieldDate",
                    width: 100,
                    type: 'date',
                    format: "{0:dd/MM/yyyy}",
                    parseFormats: "{0:dd/MM/yyyy}",                  
                },	
				{
                    title: CommonService.translate("catBankBranchName"),
                    field: "catBankBranchName",
                    width: 100,
                },				
				{
                    title: CommonService.translate("IS_LOCK"),
                    field: "isLock",
                    width: 100
                },	
				{
                    title: CommonService.translate("ISSUE_BY"),
                    field: "issueBy",
                    width: 100
                },	
				{
                    title: CommonService.translate("DATE_RANGE"),
                    field: "dateRange",
                    width: 100,
                    type: 'date',
                    format: "{0:dd/MM/yyyy}",
                    parseFormats: "{0:dd/MM/yyyy}",                  
                },	
				{
                    title: CommonService.translate("CHECK_PARTNER"),
                    field: "checkPartner",
                    width: 100
                },	
				{
                    title: CommonService.translate("DATE_UPDATE_PARTNERS_CONTRACT"),
                    field: "dateUpdatePartnersContract",
                    width: 100,
                    type: 'date',
                    format: "{0:dd/MM/yyyy}",
                    parseFormats: "{0:dd/MM/yyyy}",                  
                },	
				{
                    title: CommonService.translate("REPRESENTATION_NAME"),
                    field: "representationName",
                    width: 100
                },	
				{
                    title: CommonService.translate("REPRESENTATION_POSITION"),
                    field: "representationPosition",
                    width: 100
                },	
				{
                    title: CommonService.translate("NOTE"),
                    field: "note",
                    width: 100
                },	
				{
                    title: CommonService.translate("ACCOUNT"),
                    field: "account",
                    width: 100
                },	
				{
                    title: CommonService.translate("TRANSACTION_NAME"),
                    field: "transactionName",
                    width: 100
                },	
				{
                    title: CommonService.translate("REPRESENTATIVE_NAME"),
                    field: "representativeName",
                    width: 100
                },	
				{
                    title: CommonService.translate("REPRESENTATIVE_POSITION"),
                    field: "representativePosition",
                    width: 100
                },	
				]
            });			
			
        }
        
    	function setHeight(){
		    var h = $(window).height();
		    angular.element("#catPartsGridId").height(h-120);  
		    window.setTimeout(function() {
		    	resizeGrid();
	    	}, 1);
		}
    	
    	
    	angular.element(document).ready(function () {
    		setHeight();
    	});    
		
    	angular.element(window).resize(function(){
			setHeight();
		});
                				       
		
        //Chuyener sang manf hinhf add        
        function add() {
			$(".k-invalid-msg").hide();        	           
            vm.data = getEmptyDataModel();
            $rootScope.RedirectToCAT_PARTSFormEvent={
            		data:vm.data,
    				type:'add'
            };            
			CommonService.goToMenu('CAT_PARTSForm',{})

        }
		
		//Chuyen sang man hinh edit
		function edit(dataItem){		
			$rootScope.RedirectToCAT_PARTSFormEvent={
            		data:dataItem,
    				type:'edit'
            };
			CommonService.goToMenu('CAT_PARTSForm',{})
		}
		//Chuyen sang man hinh view
		function detail(dataItem) {
			$rootScope.RedirectToCAT_PARTSFormEvent={
            		data:dataItem,
    				type:'view'
            };
			CommonService.goToMenu('CAT_PARTSForm',{})
        }
                                        
		
		function convertDate(dateString) {
			var parts = dateString.split("/");
		    return new Date(parts[2], parts[1] - 1, parts[0]);
		}
		
		
		
		function dateValidation(e) {
			var dateformat = /^(0[1-9]|[12][0-9]|3[01])[\- \/.](?:(0[1-9]|1[012])[\- \/.](201)[2-9]{1})$/;
		    if (e.match(dateformat)) {
		        var opera1 = e.split('/');
		        var lopera1 = opera1.length;
		        if (lopera1 > 1) {
		            var pdate = e.split('/');
		        }
		        var mm = parseInt(pdate[1]);
		        var dd = parseInt(pdate[0]);
		        var yy = parseInt(pdate[2]);
		        var ListofDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
		        if (mm == 1 || mm > 2) {
		            if (dd > ListofDays[mm - 1]) {
		                return true;
		            }
		        }
		        if (mm == 2) {
		            var lyear = false;
		            if ((!(yy % 4) && yy % 100) || !(yy % 400)) {
		                lyear = true;
		            }
		            if ((lyear == false) && (dd >= 29)) {
		                return true;
		            }
		            if ((lyear == true) && (dd > 29)) {
		                return true;
		            }
		        }
		    } else {
		        return true;
		    }
		    return false;
        }
						
		
		

		function onCheckAll(event) {
			var grid = vm.catPartsGrid;
			chkSelectAllBase(vm.chkAll, grid);
			
			var grid = $(event.target).closest("[kendo-grid]").data("kendoGrid");
            var items = grid.dataSource.data();
            vm.checkedItems = [];
            items.forEach(function(item){
                if (event.target.checked) {
                	vm.checkedItems.push(item.catPartsId);
                }                
            });
		}
		
		function onCheck(event) {
			var element = angular.element(event.currentTarget);
			var checked = element.is(':checked');
			var row = element.closest("tr");
			var gidContent = row.closest("div");
			var grid = gidContent.parent().getKendoGrid();
			var dataItem = grid.dataItem(row);
			if (checked) {				
				if (!contains(vm.checkedItems, dataItem.catPartsId)) {
					vm.checkedItems.push(dataItem.catPartsId);
				} 
			} else {
				var index = vm.checkedItems.indexOf(dataItem.catPartsId);
				if (index > -1) {
					vm.checkedItems.splice(index, 1);
				}
			}			
		} 
		
		
		vm.removeItem=function(){
			
		}
        function remove(){
			if (vm.checkedItems.length == 0) {
				var itemId;
				
				var grid = vm.catPartsGrid;
				if (grid.select() == null || grid.select().length === 0) {
					toastr.warning(CommonService.translate(message.recordRequired));
					return;
				}
				var tr = grid.select().closest("tr");
				var dataItem = grid.dataItem(tr);
					
				itemId = dataItem.partnerId;
				
				
				if (vm.catPartsGrid.select().length > 0 && confirm(CommonService.translate(message.deleteConfirm))) {
					CatPartsService.deleteObject(itemId).then(function(){
						
						toastr.success(CommonService.translate(message.deleteSuccess));
						doSearch();
					}, function(errResponse){
						
						if (errResponse.status === 302){
							toastr.error(CommonService.translate(message.usedRecord));
						} else {
							toastr.error(CommonService.translate(message.deleteError));
						}
					});
				}
			} else if(confirm(CommonService.translate(message.deleteConfirm))){
        		CatPartsService.deleteList(vm.checkedItems).then(function(){					
    				toastr.success(CommonService.translate(message.deleteSuccess));    			
					doSearch();
				
                }, function(errResponse){					
					if (errResponse.status === 302){
						toastr.error(CommonService.translate(message.usedRecord));
					} else {
						toastr.error(CommonService.translate(message.deleteError));
					}
				});
        	}        	
        }        

     // Export
		vm.exportFile=function exportFile() {
	    	CatPartsService.doSearch(vm.criteriaSearch).then(
				function(d) {
					var data = d.plain();
					var title = [
							CommonService.translate("??n v?")
							, CommonService.translate("PARTNER_NAME")
							, CommonService.translate("ADDRESS")
							, CommonService.translate("PHONE")
							, CommonService.translate("DESCRIPTION")
							, CommonService.translate("FAX")
							, CommonService.translate("TAX_CODE")
							, CommonService.translate("CODE")
							, CommonService.translate("REF_ID")
							, CommonService.translate("TYPE")
							, CommonService.translate("ABB_NAME")
							, CommonService.translate("GROUP_ID")
							, CommonService.translate("DELEGATE_PERSON")
							, CommonService.translate("TITLE")
							, CommonService.translate("ACCOUNT_NUMBER")
							, CommonService.translate("BUSSINESS_NUMBER")
							, CommonService.translate("STATUS")
							, CommonService.translate("CREATOR_ID")
							, CommonService.translate("PROPOSAL_NOTE")
							, CommonService.translate("ACCEPTOR_ID")
							, CommonService.translate("ACCEPT_DATE")
							, CommonService.translate("ACCEPT_NOTE")
							, CommonService.translate("REJECT_NOTE")
							, CommonService.translate("NUM_DISPATH")
							, CommonService.translate("NUM_SUBMITSION")
							, CommonService.translate("CAT_PARTNER_TYPE_ID")
							, CommonService.translate("CREAT_PARTNER_DATE")
							, CommonService.translate("REASON_CHANGE_PARTNER")
							, CommonService.translate("DELEGATE_POSITION")
							, CommonService.translate("ACCOUNT_HOLDER")
							, CommonService.translate("IDENTITY_CARD")
							, CommonService.translate("BANK_NAME")
							, CommonService.translate("STATION_CODE_EXPECTED")
							, CommonService.translate("STATION_ID_EXPECTED")
							, CommonService.translate("MODIFIELD_DATE")
							, CommonService.translate("CAT_BANK_BRANCH_ID")
							, CommonService.translate("IS_LOCK")
							, CommonService.translate("ISSUE_BY")
							, CommonService.translate("DATE_RANGE")
							, CommonService.translate("CHECK_PARTNER")
							, CommonService.translate("DATE_UPDATE_PARTNERS_CONTRACT")
							, CommonService.translate("REPRESENTATION_NAME")
							, CommonService.translate("REPRESENTATION_POSITION")
							, CommonService.translate("NOTE")
							, CommonService.translate("ACCOUNT")
							, CommonService.translate("TRANSACTION_NAME")
							, CommonService.translate("REPRESENTATIVE_NAME")
							, CommonService.translate("REPRESENTATIVE_POSITION")
							, CommonService.translate("UPDATOR_ID")
							, CommonService.translate("UPDATOR_GROUP_ID")
							, CommonService.translate("CREATOR_GROUP_ID")
							, CommonService.translate("CREATOR_DATE")
							, CommonService.translate("UPDATOR_DATE")
							, CommonService.translate("Hi?u l?c")
							, CommonService.translate("Ng??i t?o")
							, CommonService.translate("Ng�y c?p nh?t")
						];
					exportExcel(title, buildDataExport(data), "Export" + " catParts");
				},
				function(errResponse){
					toastr.error(CommonService.translate(message.getDataError));
				}
			);	    	
	    }
	    
	    function buildDataExport(data) {
	    	// Row content
	    	var rData = [];
	    	$.each( data, function( index, value){
	    		var item = { cells: [
	    		               { value: value.adOrgName }
								, { value: value.partnerName }
								, { value: value.address }
								, { value: value.phone }
								, { value: value.description }
								, { value: value.fax }
								, { value: value.taxCode }
								, { value: value.code }
								, { value: value.refName }					
								, { value: value.type }
								, { value: value.abbName }
								, { value: value.groupName }					
								, { value: value.delegatePerson }
								, { value: value.title }
								, { value: value.accountNumber }
								, { value: value.bussinessNumber }
								, { value: value.status }
								, { value: value.creatorName }					
								, { value: value.proposalNote }
								, { value: value.acceptorName }					
								, { value: value.acceptDate }
								, { value: value.acceptNote }
								, { value: value.rejectNote }
								, { value: value.numDispath }
								, { value: value.numSubmitsion }
								, { value: value.catPartnerTypeName }					
								, { value: value.creatPartnerDate }
								, { value: value.reasonChangePartner }
								, { value: value.delegatePosition }
								, { value: value.accountHolder }
								, { value: value.identityCard }
								, { value: value.bankName }
								, { value: value.stationCodeExpected }
								, { value: value.stationNameExpected }					
								, { value: value.modifieldDate }
								, { value: value.catBankBranchName }					
								, { value: value.isLock }
								, { value: value.issueBy }
								, { value: value.dateRange }
								, { value: value.checkPartner }
								, { value: value.dateUpdatePartnersContract }
								, { value: value.representationName }
								, { value: value.representationPosition }
								, { value: value.note }
								, { value: value.account }
								, { value: value.transactionName }
								, { value: value.representativeName }
								, { value: value.representativePosition }
								, { value: value.updatorName }					
								, { value: value.updatorGroupName }					
								, { value: value.creatorGroupName }					
								, { value: value.creatorDate }
								, { value: value.updatorDate }
								
	    		          ]};
	    		rData.push(item);
	    	});
	    	
	    	return rData;
	    }
		
   
        
        function canceldoSearch(){
			vm.criteriaSearch={}
			doSearch();
		}
		function doSearch(){
			vm.catPartsGrid.dataSource.page(1);
		}
				
		
		function resizeGrid() {
            var gridElement = angular.element("#catPartsGridId"),
             dataArea = gridElement.find(".k-grid-content");
            dataArea.height($(window).height() - 220);
        }                		            																		
					   
	 
    }
})();