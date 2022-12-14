/* global toastr:false, moment:false */
(function() {
	'use strict';

	angular.module('MetronicApp').constant('RestEndpoint', RestEndpoint());

	/* @ngInject */
	function RestEndpoint() {
		var endpoints = {

			// BUILD
			BASE_SERVICE_URL : API_URL ,
			
			CAT_PARTS_SERVICE_URL : "catPartsServiceRest/catParts",
			CAT_BANK_BRANCH_BAK_SERVICE_URL : "catBankBranchBakServiceRest/catBankBranchBak",
			AB_COMPLEMENT_WORK_ESTIMATEAB: "AbComplementWorkRsServiceRest/abComplementWork",
			AB_DETAIL_PRICE_ESTIMATEAB:"AbDetailPriceRsServiceRest/abDetailPrice", 
			//VietDB
			DETAIL_SETTLEMENT_PROPOSAL_URL: "DetailSettlementProposalRsServiceRest/detailSettlementProposal",
			/*dong*/
			TITLE_AZIMU_LIST_SERVICE_URL: "TitAziConstrAcceptListRsServiceRest/titAziConstrAcceptList",	
			TITLE_AZIMU_LIST_SAVELIST_SERVICE_URL: "TitAziConstrAcceptListRsServiceRest/titAziConstrAcceptList/saveList",
			TITLE_AZIMU_SERVICE_URL: "TitAziConstrAcceptRsServiceRest/titAziConstrAccept",
			MONITOR_SERVICE_URL : 'MonitorMissionAssignRsService/monitorMissionAssign',
			/*dongend*/
			CURRENT_STATE_HANDOVER_SERVICE_Url : "currentStHandoverRest/currentStateHandover",
			CWORK_COMPLETE_SERVICE_Url : "cWorkCompleteRest/constrWorkCompConfirm",
			CURRENT_STATE_HANDOVER_LIST_SERVICE_Url : "currentStHandoverListRest/currentStateHandoverList",
			CONSTR_WORK_COMP_CONF_LIST_SERVICE_Url : "cWorkCompleteListRest/constrWorkCompConfList",
			CONSTR_WORK_COMP_CONFIRM_SERVICE_Url : "cWorkCompleteRest/constrWorkCompConfirm",
			/*ChuongNV*/
			CONSTR_WORK_LOGS_URL:"ConstrWorkLogsRsServiceRest/constrWorkLogs",
			CONSTR_WORK_LOGS_LABEL_URL:"ConstrWorkLogsLabelRsServiceRest/constrWorkLogsLabel",
			CONSTR_ORGANIZATION_PLAN_URL:"ConstrOrganizationPlanRsServiceRest/constrOrganizationPlan",
			THE_SIGN_CA_URL:"catEmployeeServiceRest/getListEmployee",
			/*EndChuongNV*/
			ADVANCE_PAYMENT_PROPOSAL_Url : "advancePaymentRest/advancePaymentProposal",
			ACCEPTANE_B_LIST_SERVCICE_URL:"bMaterialAcceptanceServiceRest/bMaterialAcceptance",
			AR_INVOICE_HEADER_SERVICE_URL : "arInvoiceHeaderServiceRest/arInvoiceHeader",
			/*Truong Report_Result*/
			REPORT_RESULT_QUALITY_URL : "qualityCableMeaResultRsServiceRest/qualityCableMeaResult",
			REPORT_RESULT_QUALITY_MONITORING_URL : "QualityCableMeaReportRsServiceRest/getListEmployeeByRole",
			LIST_REPORT_RESULT_QUALITY_URL : "QualityCableMeaReportRsServiceRest/qualityCableMeaReport",
			A_MATERIAL_RECOVERY_MINUTES_URL : "AMaterialRecoveryMinutesRsServiceRest/aMaterialRecoveryMinutes",
			A_MATERIAL_RECOVERY_LIST_URL :"AMaterialRecoveryListRsServiceRest/aMaterialRecoveryList",
			/*End Report_Result*/
			AR_INVOICE_SERVICE_URL : "arInvoiceServiceRest/arInvoice",
			AR_TRANSFER_SERVICE_URL : "arTransferServiceRest/arTransfer",
			AR_TRANSFER_LINE_SERVICE_URL : "arTransferLineServiceRest/arTransferLine",
			AR_INVOICE_LINE_SERVICE_URL : "arInvoiceLineServiceRest/arInvoiceLine",
			AR_AP_CLEARING_SERVICE_URL : "arApClearingServiceRest/arApClearing",
			CAT_EMPLOYEE_SERVICE_Url : "CatEmployeeRsServiceRest/catEmployee",
			C_BPARTNER_SERVICE_URL : "cBpartnerServiceRest/cBpartner",
			C_BPARTNER_DV_SERVICE_URL : "cBpartnerServiceRest/cBpartnerByDonVi",
			C_BPARTNER_BANK_SERVICE_URL : "cBpartnerBankServiceRest/cBpartnerBank",
			C_BPARTNER_GROUP_SERVICE_URL : "cBpartnerGroupServiceRest/cBpartnerGroup",
			C_SHARED_ORG_SERVICE_URL : "cSharedOrgServiceRest/cSharedOrg",
			C_SHARED_ORG_PARTNER_SERVICE_URL : "cSharedOrgServiceRest/cSharedOrgByPartner",
			C_BANK_ACCOUNT_SERVICE_URL : "cBankAccountServiceRest/cBankAccount",
			C_BANK_SERVICE_URL : "cBankServiceRest/cBank",
			C_BANK_SEARCH_SERVICE_URL : 'cBankServiceRest/cBankSearch',
			C_BUSINESS_AREA_SERVICE_URL : "cBusinessAreaServiceRest/cBusinessArea",
			C_CONTRACT_SERVICE_URL : "cContractServiceRest/cContract",
			C_DELIVERY_TYPE_SERVICE_URL : "cDeliveryTypeServiceRest/cDeliveryType",
			C_DEPARTMENT_TREE_SERVICE_URL : "cDepartmentServiceRest/departmentTree",
			C_DEPARTMENT_SERVICE_URL : "cDepartmentServiceRest/cDepartment",
			C_PROJECT_SERVICE_URL : "cProjectServiceRest/cProject",
			C_PROJECT_PHASE_SERVICE_URL : "cProjectPhaseServiceRest/cProjectPhase",
			C_GUARANTEE_SERVICE_URL : "cGuaranteeServiceRest/cGuarantee",
			C_IMPORT_TYPE_SERVICE_URL : "cImportTypeServiceRest/cImportType",
			C_TARIFF_CONTRACTOR_SERVICE_URL : "cTariffContractorServiceRest/cTariffContractor",
			C_TARIFF_CONTRACTOR_TYPE_SERVICE_URL : "cTariffContractorTypeServiceRest/cTariffContractorType",
			C_TASK_SERVICE_URL : "cTaskServiceRest/cTask",
			C_TASK_TYPE_SERVICE_URL : "cTaskTypeServiceRest/cTaskType",
			C_TAX_SERVICE_URL : "cTaxServiceRest/cTax",
			C_TAX_GROUP_SERVICE_URL : "cTaxGroupServiceRest/cTaxGroup",
			C_TYPE_OF_REVENUE_SERVICE_URL : "cTypeOfRevenueServiceRest/cTypeOfRevenue",
			C_REVENUE_TYPE_URL : "cRevenueTypeServiceRest/cRevenueType",
			C_TYPE_OF_REVENUE_SEARCH_SERVICE_URL : "cTypeOfRevenueServiceRest/cTypeOfRevenueSearch",
			C_KMP_BUDGET_GROUP_SERVICE_URL : "cKmpBudgetGroupServiceRest/cKmpBudgetGroup",
			C_STATEMENT_SERVICE_URL : "cStatementServiceRest/cStatement",
			
			C_STATEMENT_CATEGORY_SERVICE_URL : "cStatementCategoryServiceRest/cStatementCategory",
			/*C_STATEMENT_SERVICE_URL : "cStatementServiceRest/cStatement",*/
			C_CONSTRUCTION_GROUP_SERVICE_URL : "cConstructionGroupServiceRest/cConstructionGroup",
			C_CONSTRUCTION_PHASE_SERVICE_URL : "cConstructionPhaseServiceRest/cConstructionPhase",
			C_UOM_SERVICE_URL : "cUomServiceRest/cUom",
			C_VAT_SERVICE_URL : "cVatServiceRest/cVatService",
			C_ACCOUNT_SERVICE_URL : "cAccountServiceRest/cAccount",
			C_COST_TYPE_URL : "cCostTypeServiceRest/cCostType",
			C_DOCUMENT_TYPE_URL : "cDocumentTypeServiceRest/cDocumentType",
			C_PERIOD_URL : "cPeriodServiceRest/cPeriod",
			C_SALES_REGION_URL : "cSalesRegionServiceRest/cSalesRegion",
			C_PERIOD_SEARCH_URL : "cPeriodServiceRest/cPeriodSearch",
			C_SERVICE_URL : "cServiceServiceRest/cService",
			M_PRODUCT_CATEGORY_SERVICE_URL : "mProductCategoryServiceRest/mProductCategory",
			M_PRODUCT_SERVICE_URL : "mProductServiceRest/mProduct",
			M_PRODUCT_TYPE_SERVICE_URL : "mProductTypeServiceRest/mProductType",
			M_PRODUCT_TYPE_SEARCH_SERVICE_URL : "mProductTypeServiceRest/mProductTypeSearch",
			M_WAREHOUSE_SERVICE_URL : "mWarehouseServiceRest/mWarehouse",
			M_LOCATORTYPE_SERVICE_URL : "mLocatorTypeServiceRest/mLocatorType",
			AD_ORG_SERVICE_URL : "adOrgServiceRest/adOrg",
			C_STATION_SERVICE_URL : "cStationServiceRest/cStation",
			C_PAYMENT_TERM_SERVICE_URL : "cPaymentTermServiceRest/cPaymentTerm",
			C_CONSTRUCTION_SERVICE_URL : "cConstructionServiceRest/cConstruction",
			PLANPRICE_SERVICE_URL : "cCostDetailServiceRest/cCostDetail",
			C_SHARE_ORG_SERVICE_URL : "cSharedOrgServiceRest/cShareOrg",
			C_SITE_CODE_TYPE_SERVICE_URL : "cSiteCodeTypeServiceRest/cSiteCodeType",
			C_SITE_CODE_GROUP_SERVICE_URL : "cSiteCodeGroupServiceRest/cSiteCodeGroup",
			C_SITE_CODE_INFO_SERVICE_URL : "cSiteCodeInfoServiceRest/cSiteCodeInfo",
			C_ACCOUNT_SCHEMA_SERVICE_URL : "cAccountSchemaServiceRest/cAccountSchema",
			GL_FACT_LIST_SERVICE_URL : "glFactListServiceRest/glFactList",
			FACT_ACCT_SERVICE_URL : "factAcctServiceRest/glFactList",
			C_PROJECT_TYPE_SERVICE_URL : "cProjectTypeServiceRest/cProjectType",
			GL_PREPAID_ALLOCATION_ESTIMATE_SERVICE_URL : "glPrepaidAllocationEstimateServiceRest/glPrepaidAllocationEstimate",
			GL_PREPAID_ALLOCATION_ESTIMATE_SEARCH_SERVICE_URL : "glPrepaidAllocationEstimateServiceRest/glPrepaidAllocationEstimateSearch",
			C_LOCATION_GROUP_SERVICE_URL : "cLocationGroupServiceRest/cLocationGroup",
			C_WORK_UNIT_SERVICE_URL : "cWorkUnitServiceRest/cWorkUnit",
			C_TAX_CATEGORY_SERVICE_URL : "cTaxCategoryServiceRest/cTaxCategory",
			
			C_LOCATION_SERVICE_URL : "cLocationServiceRest/cLocation",
			C_TITLE_SERVICE_URL : "cTitleServiceRest/cTitle",
			C_KMP_BUDGET_SERVICE_URL : "cKmpBudgetServiceRest/cKmpBudget",
			C_PAYROLL_SERVICE_URL : "cPayrollServiceRest/cPayroll",
			C_CASH_FLOW_SERVICE_URL : "cCashFlowServiceRest/cCashFlow",
			
			AR_CLEARING_SERVICE_URL : 'arClearingServiceRest/arClearing',
			AP_CLEARING_SERVICE_URL : 'apClearingServiceRest/apClearing',
			C_BUDGET_SERVICE_URL : "cBudgetServiceRest/cBudget",
			C_SALARY_SERVICE_URL : "cSalaryServiceRest/cSalary",
			C_COST_TYPE_SERVICE_URL : "cCostTypeServiceRest/cCostType",
			C_SALARY_CONFIG_SERVICE_URL : "cSalaryConfigServiceRest/cSalaryConfig",
			C_CURRENCY_SERVICE_URL : "cCurrencyServiceRest/cCurrency",
			C_COST_CENTER_SERVICE_URL : "cCostCenterServiceRest/cCostCenter",
			C_PROFIT_CENTER_SERVICE_URL : "cProfitCenterServiceRest/cProfitCenter",
			AP_INVOICE_GROUP_SERVICE_URL : "apInvoiceGroupServiceRest/apInvoiceGroup",
			AP_INVOICE_SERVICE_URL : "apInvoiceServiceRest/apInvoice",
			AP_INVOICE_LINE_SERVICE_URL : "apInvoiceLineServiceRest/apInvoiceLine",
			C_STATEMENT_LINE_SERVICE_URL : "cStatementLineServiceRest/cStatementLine",
			GL_PREPAID_AL_LOCATION_ESTIMATE_SERVICE_URL : "glPrepaidAllocationEstimateServiceRest/glPrepaidAllocationEstimate",
			C_IN_OUTCOME_TYPE_SERVICE_URL : "cInOutcomeTypeServiceRest/cInOutcomeType",
			C_IN_OUTCOME_TYPE_MAP_SERVICE_URL : "cInOutcomeTypeMapServiceRest/cInOutcomeTypeMap",
			C_ADVANCE_REQ_SERVICE_URL : 'cAdvanceRequestServiceRest/cAdvanceRequest',
			C_ADVANCE_REQ_LINE_SERVICE_URL : 'cAdvanceRequestLineServiceRest/cAdvanceRequestLine',
			AR_CASH_SERVICE_URL : "arCashServiceRest/arCash",
			AR_CASH_LINE_SERVICE_URL : "arCashLineServiceRest/arCashLine",
			AR_REVALUATION_SERVICE_URL : "arRevaluationServiceRest/arRevaluation",
			AP_REVALUATION_SERVICE_URL : "apRevaluationServiceRest/apRevaluation",
			AP_CASH_SERVICE_URL : "apCashServiceRest/apCash",
			C_SALARY_SUMMARY_SERVICE_URL : "cSalarySummaryRsService/cSalarySumary",
			C_SALARY_SUMMARY_SEARCH_SERVICE_URL : "cSalarySummaryRsService/cSalarySumary/search",
			AP_CASH_LINE_SERVICE_URL : "apCashLineServiceRest/apCashLine",
			C_ADVANCE_REQUEST_LINE_URL : "cAdvanceRequestLineServiceRest/cAdvanceRequestLine",
			C_ACCOUNT_VALUE_URL : "cAccountValueServiceRest/cAccountValue",
			C_APPROVAL_ADVANCE_REQUEST_URL : "cApprovalAdvanceRequestServiceRest/cApprovalAdvanceRequest",
			GL_ALLOCATION_TEMPLATE_SERVICE_URL : "glAllocationTemplateServiceRest/glAllocationTemplate",
			GL_ALLOCATION_TEMPLATE_LINE_SERVICE_URL : "glAllocationTemplateLineServiceRest/glAllocationTemplateLine",
			GL_ALLOCATION_TEMPLATE_ORG_SERVICE_URL : "glAllocationTemplateOrgServiceRest/glAllocationTemplateOrg",
			C_CASH_IN_BANK_SERVICE_URL : "cCashInBankServiceRest/cCashInBank",
			C_CASH_IN_BANK_LINE_SERVICE_URL : "cCashInBankLineServiceRest/cCashInBankLine",
			C_DOCTYPE_ACCOUNT_SERVICE_URL : "cDoctypeAccountRsServiceRest/cDoctypeAccount",
			C_DOCTYPE_PARTNER_SERVICE_URL : "cDoctypePartnerRsServiceRest/cDoctypePartner",
			GL_ALLOCATION_PREPAID_URL : "glAllocationPrepaidServiceRest/glAllocationPrepaidId",
			GL_ALLOCATION_PREPAID_LINE_URL : "glAllocationPrepaidLineServiceRest/glAllocationPrepaidLine",
			M_INOUT_SERVICE_URL : "mInoutRsService/mInout",
			M_INOUT_LINE_SERVICE_URL : "mInoutLineRsService/mInoutLine",
			M_INOUT_LINE_SERVICE_URL_GET_ALL : "mInoutLineRsService/mInoutLine/all",
			M_INOUT_INVOICE_SERVICE_URL : "mInoutInvoiceRsService/mInoutInvoice",
			GL_ALLOCATION_SERVICE_URL : "glAllocationServiceRest/glAllocation",
			GL_ALLOCATION_DOCUMENT_SERVICE_URL : "glAllocationServiceRest/glAllocation/document",
			GL_ALLOCATION_LINE_SERVICE_URL : "glAllocationLineServiceRest/glAllocationLine",
			GL_ALLOCATION_LINE_FILTER_BY_ALLOCATION_ID_SERVICE_URL : "glAllocationLineServiceRest/glAllocationLine/allocation",
			GL_ALLOCATION_SEARCH_SERVICE_URL : "glAllocationServiceRest/glAllocation/search",
			C_MARKET_SERVICE_URL : "mMarketPriceRsService/cMarketPrice",
			GL_ALLOCATION_FORMULA_SERVICE_URL : "glAllocationFormulaServiceRest/glAllocationFormula",
			GL_ALLOCATION_FORMULA_LINE_SERVICE_URL : "glAllocationFormulaLineServiceRest/glAllocationFormulaLine",
			GL_ALLOCATION_FORMULA_RATE_SERVICE_URL : "glAllocationFormulaRateServiceRest/glAllocationFormulaRate",
			GL_ALLOCATION_FORMULA_ORG_SERVICE_URL : "glAllocationFormulaOrgServiceRest/glAllocationFormulaOrg",
			GL_ALLOCATE_SERVICE_URL : "glAllocationLineServiceRest/allocate",
			C_OBJECT_TYPE_SERVICE_URL : "CObjectTypeRsService/cObjectType",
			M_INOUT_SERVICE_URL_ADD : "mInoutRsService/mInout/add",
			C_CONTIGENCY_SALE_SERVICE_URL : "CContigencySaleRsService/cContigencySale",
			C_CONTIGENCY_SALE_LINE_SERVICE_URL : "CContigencySaleLineRsService/cContigencySaleLine",
			C_MAP_SERVICE_URL : "cMapServiceRest/cMap",
			C_PAYMENT_SCOPE_SERVICE_URL : "cPaymentScopeRsServiceRest/cPaymentScope",
			C_ADVANCE_REQUEST_SALARY_SEARCH_SERVICE_URL : "cAdvanceRequestSalaryRsServiceRest/cAdvanceRequestSalary/search",
			C_ADVANCE_REQUEST_SALARY_SERVICE_URL : "cAdvanceRequestSalaryRsServiceRest/cAdvanceRequestSalary",
			C_WORKTYPE_SERVICE_URL : "cWorkTypeRsServiceRest/cWorkType",
			
			//B??n giao m???t b???ng thi c??ng
			
			CONSTR_GROUND_HANDOVER_SERVICE_URL:"constrGroundHandoverServiceRest/constrGroundHandover",
			DETAIL_SETTLEMENT_EVALUATE_URL : "detailSettlementEvaluateServiceRest/detailSettlementEvaluate",
			
			
			// BEGIN TRANGDD Merge code KTQT
			A_ASSET_TYPE_SERVICE_URL : 'assetTypeServiceRest/aAssetType',
			A_ASSET_TYPE_SERVICE_SEARCH_URL : "assetTypeServiceRest/aAssetTypeSearch",
			A_ASSET_GROUP_SERVICE_URL : 'assetGroupServiceRest/aAssetGroup',
			A_ASSET_GROUP_SERVICE_SEARCH_URL : "assetGroupServiceRest/aAssetGroupSearch",
			A_ASSET_SERVICE_URL : 'assetServiceRest/aAsset',
			A_ASSET_SERVICE_SEARCH_URL : "assetServiceRest/aAssetSearch",

			C_GROUPHEADER_SERVICE_URL : 'cGroupheaderServiceRest/cGroupheader',
			C_GROUPHEADER_SERVICE_SEARCH_URL : "cGroupheaderServiceRest/cGroupheaderSearch",
			C_GROUP_SERVICE_URL : 'cGroupServiceRest/cGroup',
			C_GROUP_SERVICE_SEARCH_URL : "cGroupServiceRest/cGroupSearch",
			C_MAPGROUP_SERVICE_URL : 'cMapGroupServiceRest/cMapGroup',
			C_MAPGROUP_SERVICE_SEARCH_URL : "cMapGroupServiceRest/cMapGroupSearch",
			// END TRANGDD Merge code KTQT
				
			//BEGIN KTQT
			
			//BEGIN Phong
			CONSTRUCTION_ACCEPTANCE_URL : "ConstructionAcceptanceRsServiceRest/constructionAcceptance",
			DISTANCE_UNLOAD_CONSTR_MINUTE_SERVICE_URL : "DistanceUnloadConstrMinutesRsServiceRest/distanceUnloadConstrMinutes",
			DISTANCE_UNLOAD_LIST_URL : "DistanceUnloadListRsServiceRest/distanceUnloadList",
			//END Phong	
			
			//begin MinhPVN
			B_MATERIAL_ACCEPT_MER_LIST_SERVICE_URL :"bMaterialAcceptMerListServiceRest/bMaterialAcceptMerList",
			AB_COMPLEMENT_WORK_DESCRIBE_SERVICE_URL :"abComplementWorkDescribeServiceRest/abComplementWorkDescribe",
			//end MinhPVN
			//ngoccx
			//BEGIN CONSTRUCTIONLIST NGOCCX
			ESTIMATES_WORK_ITEMS_SERVICE_URL : "estimatesWorkItemsServiceRest/estimatesWorkItems",
			ESTIMATES_WORK_ITEMS_SERVICE_SEARCH_ACCEPT_URL: "estimatesWorkItemsServiceRest/estimatesWorkItems/SearchAccept/",
			CAT_EMPLOYEE_NAME_AND_ID: "catEmployeeServiceRest/getEmployeeByRole/NameAndId/",
			//END NGOCCX
			
			//ngoccx
			//BEGIN DRAWINGLIST NGOCCX
			COMPLETION_DRAWINGS_SEARCH_URL: "completionDrawingServiceRest/completionDrawing/Search/",
			COMPLETION_DRAWINGS_SERVICE_URL: "completionDrawingServiceRest/completionDrawing",
			COMPLETION_DRAWINGS_FOLDER_URL : "completionDrawingServiceRest/completionDrawing/folder",
			UTIL_ATTACHED_DOCUMENTS_SERVICE_URL : "utilAttachedDocumentsServiceRest/utilAttachedDocuments",
			AB_SETTLEMENT_WORK_SERVICE_URL : "AbSettlementWorkRsServiceRest/abSettlementWork",
			CONSTR_RE_MAP_SERVICE_URL :"ConstrCompleteRecordsMapRsService/constrCompleteRecordsMap",
			COMPLETION_DRAWINGS_SEARCH_BY_ID_URL: "completionDrawingServiceRest/completionDrawing/downloadFile/",
			APPR_SIGN_URL: "approvalSignManagementServiceRest/approvalSignManagement",
			GET_NOTIFY_SERVICE_URL :"ConstrCompleteRecordsMapRsService/getNotify",
			//BEGIN DRAWINGLIST NGOCCX

			//BEGIN TUNGPV
			WORK_ITEMS_ACCEPTANCE_SERVICE_URL : "workItemsAcceptanceServiceRest/workItemsAcceptance",
			
			SCENE_GENERATE_WORK_LIST_URL: "sceneGenerateWorkListServiceRest/sceneGenerateWorkList" ,
			SCENE_GENERATE_WORK_URL: "sceneGenerateWorkServiceRest/sceneGenerateWork" ,
			
			CAT_EMPLOYEE_BY_CONSTRID_SERVICE_URL : "catEmployeeServiceRest/getListEmployeeByRole",
			//END TUNGPV

			CAT_FILE_INVOICE_RS_SERVICE: 'CatFileInvoiceRsService/catFileInvoice',
			CONSTR_COMPLETE_RECORDS_MAP_RS_SERVICE: 'ConstrCompleteRecordsMapRsService/constrCompleteRecordsMap',
			CAT_PROVINCES_RS_SERVICE: 'catProvincesServiceRest/catProvinces',
			CAT_CONSTR_TYPE_RS_SERVICE: 'catConstrTypesServiceRest/catConstrTypes',
			CONSTR_COMPLETE_RECORDS_EXPORT_EXCELL_SERVICE: 'ConstrCompleteRecordsMapRsService/exportFileExcell',

			
			//DODT
			CONSTR_CONSTRUCTIONS_SERVICE_URL : "VConstructionHcqtRsService/vConstructionHcqt",
			PROJ_INVEST_PROJECT_SERVICE_URL : "ProjInvestProjectRsService/projInvestProject",
//			CAT_PARTNERS_SERVICE_URL :"CatPartnersRsService/catPartners",
			
			//BEGIN DSBGVT
			GETCONSTRT_SERVICE_URL : 'ConstrConstructionsRsService/getConstructions',
			EXPORT_PYCNT_SERVICE_URL:'ConstrConstructionsRsService/exportFile',
			EXPORT_LIST_PYCNT_SERVICE_URL:'ConstrConstructionsRsService/exportList',
			INTERGRATED_CONTRACT_NAME_SERVICE_URL : 'IntergratedContractRsServiceRest/intergratedContractConstrt',
			GET_PARTNER_SERVICE_URL : 'IntergratedContractRsServiceRest/getPartner',
			ADD_CONSTR_ACCEPTANCE_REQUEST_SERVICE_URL : 'ConstrAcceptanceRequestRsService/constrAcceptanceRequest',
			GET_EMPLOYEE_ROLL_SERVICE_URL : 'catEmployeeServiceRest/getEmployeeByRole',
			GET_EMPLOYEE_NAME_ROLL_SERVICE_URL : 'catEmployeeServiceRest/getEmployeeNameRole',
			LIST_MATERIALT_SERVICE_URL : 'AMaterialHandoverRsService/listMaterial',
			GET_EMPLOYEE_SERVICE_URL : 'catEmployeeServiceRest/getListEmployee',
			ADD_NEW_SETTLEMENT_SERVICE_URL : 'SettlementRightRsService/settlementRight',
			SAVE_SETTLEMENT_SERVICE_URL : 'SettlementRightRsService/saveSettlementRight',
			DELETE_SETTLEMENT_SERVICE_URL : 'SettlementRightRsService/deleteMultipleSettlement',
			DELETE_A_MATERIAL_SERVICE_URL : 'AMaterialHandoverRsService/deleteAMaterial',
			GET_WARE_EXP_SERVICE_URL : 'WareExpCmdRsService/getwareExpCmdByConstrt',
			GET_WARE_EXP_NOTE_CODE_SERVICE_URL : 'WareExpCmdRsService/getwareExpCmdByPxk',
			DELETE_WARE_EXP_SERVICE_URL : 'WareExpCmdRsService/deleteWareExpCmd',
			GET_LIST_MATERIAL_SERVICE_URL : 'AMaterialHandoverMerListRsService/getListAMaterial',
			ADD_LIST_MATERIAL_SERVICE_URL : 'AMaterialHandoverMerListRsService/addListAMaterial',
			ADD_MATERIAL_SERVICE_URL : 'AMaterialHandoverRsService/aMaterialHandover',
			GET_LIST_CHUCVU_SERVICE_URL : 'RoleCaRsService/getListChucVu',
			EXPORT_SERVICE_URL : 'AMaterialHandoverRsService/export',
			EXPORT_DOC_SERVICE_URL : 'AMaterialHandoverRsService/exportDoc',
			EXPORT_LIST_SERVICE_URL : 'AMaterialHandoverRsService/exportList',
			EXPORT_LIST_DOC_SERVICE_URL : 'AMaterialHandoverRsService/exportListDoc',
			GET_LIST_AMATERIAL_MER_LIST_SERVICE_URL : 'AMaterialHandoverMerListRsService/getListAMaterialHandOverMerList',
			GET_THOIGIAN_BANGIAO_SERVICE_URL : 'AMaterialHandoverRsService/getThoiGianBanGiao',
			HSHC_AUTH_SERVICE_URL: 'SettlementRightRsService/getSettlementRightByConstrt',	
			GET_AUTO_DATA_SERVICE_URL: 'catEmployeeServiceRest/getAutoData',	
			HSHC_SEARCH_SERVICE_URL: 'catEmployeeServiceRest/DoSearch',
			GET_AUTO_DATA_UNIT_SERVICE_URL: 'catEmployeeServiceRest/getAutoDataUnit',	
			GET_AUTO_DATA_EMAIL_SERVICE_URL: 'catEmployeeServiceRest/getAutoDataEmail',	
			GET_AUTO_DATA_PARTNER_SERVICE_URL: 'catEmployeeServiceRest/getAutoDataPartner',	
			LIST_CONSTR_ACCEPTANCE_REQ_SERVICE_URL : 'ConstrAcceptanceRequestRsService/listConstrAcceptanceReq',
			DELETE_CONSTR_ACCEPTANCE_REQ_SERVICE_URL : 'ConstrAcceptanceRequestRsService/deleteConstrAcceptanceReq',
			APPROVAL_CONSTR_ACCEPTANCE_REQ_SERVICE_URL : 'ConstrAcceptanceRequestRsService/appro',
			CONSTR_ACCEPTANCE_REQUEST_RS_SERVICE : 'ConstrAcceptanceRequestRsService/constrAcceptanceRequest',
			WORK_ITEMS_SERVICE_URL:"estimatesWorkItemsServiceRest/getWorkItemDone",
			WORK_ITEMS_NOT_DONE_SERVICE_URL:"estimatesWorkItemsServiceRest/getWorkItemNotDone",
			PAUSE_WORK_ITEMS_NOT_DONE_SERVICE_URL:"estimatesWorkItemsServiceRest/pauseWorkItem",
			GET_LIST_INVOICE_SERVICE_URL:"CatFileInvoiceRsService/getListInvoice",
			
			//minhpvn - nghiem thu vat tu B cap
			B_MATERIAL_ACCEPTANCE_SERVICE_URL:"bMaterialAcceptanceServiceRest/bMaterialAcceptance",	
			ESTIMATES_DETAIL_ANALYST_SERVICE_URL:"estimatesDetailAnalystServiceRest/estimatesDetailAnalyst",
			// end minhpvn
			//haibt
			ESTIMATES_ITEM_CHILD_URL:"EstimatesItemsChildRsServiceRest/estimatesItemsChild",
			CATEGORY_ACCEPTANCE_URL:"CategoryAcceptanceRsServiceRest/categoryAcceptance",
			SETTLEMENT_RIGHT_URL:"SettlementRightRsServiceRest/getAllAMonitorOrBInChargeByConstructId",
			//minhpvn - AB form 6
			SETTLEMENT_RIGHT_URL_ESTIMATE_AB_FORM_6_URL:"SettlementRightRsServiceRest/getAllAMonitorOrBInChargeByConstructIdForm6",
			//haibt end
			DEPARTMENT_SERVICE_URL : "departmentServiceRest/department",
			//DODT/
//			CAT_PARTNERS_SERVICE_URL :"CatPartnersRsService/catPartners",
//			ESTIMATES_WORK_ITEMS_SERVICE_URL : "estimatesWorkItemsRsService/estimatesWorkItems",
			CONSTR_ACCEPT_WORK_LIST_SERVICE_URL : "ConstrAcceptWorkListRsService/constrAcceptWorkList",
			A_MATERIAL_HANDOVER_MER_LIST_SERVICE_URL : "AMaterialHandoverMerListRsService/aMaterialHandoverMerList",
			COMPLETION_DRAWING_SERVICE_URL : "completionDrawingServiceRest/completionDrawing",
			CAT_EMPLOYEE_SERVICE_URL : "CatEmployeeRsService/catEmployee",
			CONSTRUCTION_ACCEPTANCE_SERVICE_URL : "ConstructionAcceptanceRsService/constructionAcceptance",
			
			CAT_PARTNERS_SERVICE_URL :"catPartnersServiceRest/catPartners",
			SYS_USER_SERVICE_URL : "SysUserServiceRest/sysUser",
			COMPLETION_PARTNER_FOLDER_URL : "CatEmployeeRsService/catEmployee/folder",
			CONSTR_MERCHANDISE_SERVICE_URL: "ConstrMerchandiseServiceRest/constrMerchandise",
			ASSET_MANAGER_REQ_SERVICE_URL: "AssetManageReqServiceRest/assetManageReq",
			CONSTR_ACCEPT_LOST_NOTE_SERVICE_URL: "ConstrAcceptLostNoteServiceRest/constrAcceptLostNote",
			AB_MATERIAL_COMPARE_SERVICE_URL: "AbMaterialCompareServiceRest/abMaterialCompare",
			AB_SETTLEMENT_VALUE_SERVICE_URL: "AbSettlementValueServiceRest/abSettlementValue",
			LONG_TERM_ASSET_URL: "longTermAssetServiceRest",
				
			MONITOR_MISSION_ASSIGN_URL:	"MonitorMissionAssignRsService/monitorMissionAssign",
			
			APPROVAL_MONITOR_MISSION_SERVICE_URL : 'MonitorMissionAssignRsService/appro',
			
			
			
			//QLK
			APP_PARAM_SERVICE_URL:"appParamServiceRest",
			REASON_URL:"reasonRsServiceRest/reason",
			STOCK_URL:"stockRsServiceRest",
			TAX_SERVICE_URL: "taxRsServiceRest",
			
			SHIPMENT_URL:"shipmentRsServiceRest/shipment",
			SHIPMENT_GOODS_URL:"shipmentGoodsRsServiceRest/shipmentGoods",
			SHIPMENT_GOODS_DETAIL_URL:"shipmentGoodsDetailRsServiceRest/shipmentGoodsDetail",
			SHIPMENT_TAX_URL:"shipmentTaxRsServiceRest/shipmentTax",
			OBJECT_REFERENCE_URL:"objectReferenceRsServiceRest/objectReference",
			I_PROJ_INVEST_PROJECT_URL :"iProjInvestProjectRsServiceRest/iProjInvestProject",
			I_CNT_CONTRACT:"iCntContractRsServiceRest/iCntContract",
			CAT_OWNER_CONSTRUCTION_URL:"catOwnerConstructionRsServiceRest",
			OBJECT_REFERENCE_DETAIL_URL:"objectReferenceDetailRsServiceRest/objectReferenceDetail",
			OBJECT_REFERENCE_GOODS_URL:"objectReferenceGoodsRsServiceRest/objectReferenceGoods",
			OBJECT_REFERENCE_GOODS_DETAIL_URL:"objectReferenceGoodsDetailRsServiceRest/objectReferenceGoodsDetail",
			
			STOCK_CELL_URL:"stockCellRsServiceRest",
			ATTCHMENT_URL:"attachmentRsServiceRest/attachment",
			ORDER_SERVICE_URL:"orderServiceRest/order",
			ORDER_GOOD_SERVICE_URL : "orderGoodsServiceRest/orderGoods",
			ORDER_GOOD_DETAIL_SERVICE_URL : "orderGoodsDetailServiceRest/orderGoodsDetail",
			ORDER_GOOD_UPLOAD_FOLDER_URL: "orderGoodsServiceRest/orderGoodsFolder",
			STOCK_GOODS_KPI_URL:"stockGoodsKpiServiceRest",
			STOCK_DAILY_IMPORT_EXPORT_URL: "stockDailyImportExportServiceRest",
			STOCK_GOODS_TOTAL_URL:"stockGoodsTotalServiceRest",
			STOCK_GOODS_TOTAL_REPONSE_URL:"stockGoodsTotalReponseRsServiceRest",
			STOCK_TRANS_SERVICE_URL:"stockTransServiceRest/stockTrans",
			STOCK_TRANS_DETAIL_SERVICE_URL:"stockTransDetailServiceRest/stockTransDetail",
			STOCK_TRANS_DETAIL_SERIAL_SERVICE_URL:"stockTransDetailSerialServiceRest/stockTransDetailSerial",
			STOC_DAILY_REMAIN_URL: "stockDailyRemainRsServiceRest",
			STOCK_GOODS_URL: "stockGoodsServiceRest",
			
			
			STOCK_DELIVERY_CONFIG_URL: "stockStockDeliveryConfigServiceRest",		

			ORDER_CHANGE_GOODS_URL: "orderChangeGoodsRsServiceRest",
			ORDER_CHANGE_GOODS_DETAIL_URL:"orderChangeGoodsDetailRsServiceRest",
			GOODS_TYPE_URL: "goodsTypeRsServiceRest",	
			GOODS_SERVICE_URL : "goodsRsServiceRest",
			
			//TienIchVaCauHinh
			
			ORDER_PATTERN_URL:"orderPatternServiceRest",
			ODD_CABLE:"oddCableServiceRest",
			USER_ROLE_SERVICE_URL:"userRoleServiceRest/userRole",
			SYS_ROLE_SERVICE_URL:"sysRoleServiceRest/sysRole",
			STOCK_SIGN_SERVICE_URL: "stockSignRsServiceRest",
			SYS_USER_SERVICE_URL:"sysUserServiceRest/sysUserwms",
			CONFIG_SIGN_VOFFICE_SERVICE_URL :"configSignVofficeRsServiceRest",
			CAT_OWNER_PARTNER_URL : "partnerRsServiceRest",
			
			
			CAT_PROVINCE_SERVICE_URL : "catProvinceServiceRest",
			CAT_MANUFACTURER_SERVICE_URL : "catManufacturerServiceRest",
			CAT_PARTNER_SERVICE_URL : "catPartnerServiceRest",
			PRODUCING_COUNTRY_SERVICE_URL : "catProducingCountryServiceRest",
//			hungnx 130618 start
			CNT_CONTRACT_SERVICE_URL : "CntContractRsService",
			QUANTITY_CONSTRUCTION_SERVICE_URL : "QuantityConstructionRsService",
//			hungnx 130618 end
			//VietNT_20181206_start
			ASSIGN_HANDOVER_SERVICE_URL : "assignHandoverService",
			//VietNT_end
			//VietNT_20190104_start
			REQUEST_GOODS_SERVICE_URL : "requestGoodsService",
			//VietNT_end
			//VietNT_20190116_start
			SYN_STOCK_TRANS_URL: "synStockTransService",
			//VietNT_end
			//VietNT_20190129_start
                        SYN_STOCK_DAILY_IE_URL : "synStockDailyIeService",
			//VietNT_end

			//hienvd: 3/7/109
			ASSIGN_HANDOVER_KPI_SERVICE_URL : "assignHandoverKPIService",
			//hienvd: end
			
			//HienLT56 start 02072020
			RECOMMEND_CONTACT_UNIT_SERVICE: "recommendContactUnitService",
			//HienLT56 end 02072020
			
			//Phucpv start 09072020
			EFFECTIVE_CALCULATE_DAS_SERVICE_URL: "effectiveCalculateDasRestService",
			//Phucpv end 09072020
			
			//Phucpv start 09072020
			EFFECTIVE_CALCULATE_DAS_CAPEX_SERVICE_URL: "effectiveCalculateDasCapexRestService",
			//Phucpv end 09072020
			//Huypq-19082020-start
			AIO_SYS_USER_SERVICE_URL: "sysUserRsService",
			//Huy-end
			REPORT_SERVICE: "rpOrderlyWoService",
			//Huypq-08072021-start
			API_REPORT_SERVICE: "reportServiceRest",
			//Huy-end

			//Duonghv13-16082021-start
			TOTAL_MONTH_PLAN_RENTAL_INFRASTRUCTURE_SERVICE_URL: "totalMonthPlanHTCTRsService/monthReport",
			
			//DUONGHV13-14092021-start
			MANAGEMENT_CAREER_SERVICE_URL: "managementCareerRsService",
			//Duong-end
			
			//DUONGHV13-21092021-start
			MANAGEMENT_CERTIFICATE_SERVICE_URL: "managementCertificateRsService",
			CERTIFICATE_EXTEND_SERVICE_URL: "certificateExtendRsService",
      //Duong-end
			
			//DUONGHV13-10032022-start
			MANAGE_ME_SERVICE_URL: "manageMERsService",
			STATION_ELECTRICAL_DETAIL_URL: "StationElectricalDetailRsService",
			COMPLAIN_ORDERS_REQUEST_SERVICE_URL: "complainOrdersRequestRsService",
			COMPLAIN_ORDERS_REQUEST_DETAIL_LOG_HISTORY_SERVICE_URL: "complainOrdersRequestDetailLogHistoryRsService",
			//Duong-end
			
			//DUONGHV13-02032022-start
			STATION_CONSTRUCTION_REPORT_SERVICE_URL: "stationConstructionRsService",
			//Duong-end
			DESIGN_ESTIMATES_SERVICE_URL: "designEstimatesService",
		};

		return endpoints;
	}
})();
