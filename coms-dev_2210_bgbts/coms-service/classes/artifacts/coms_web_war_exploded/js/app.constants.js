(function () {
	'use strict';

	angular.module('MetronicApp').constant('Constant', Config());

	/* @ngInject */
	function Config() {
		var config = {};
		config.authen = {
			LOGIN_URL: "auth/kttsAuthenRsService/login",
			LOGOUT_URL: "auth/kttsAuthenRsService/logout",
			GET_USER_INFO: 'auth/kttsAuthenRsService/getUserInfo'

		}
		/***********************************************************************
		 * HTTP STATUS
		 **********************************************************************/
		config.http = {
			SUCCESS: 0,
			ERROR: 1,
			BUSINESS_ERROR: 400
		};
		config.pageSize = 20,
			config.pageSizes = [10, 15, 20, 25],

			/**
			 * Thêm cấu hình các white list url không cần add version
			 */
			config.LIST_WHITE_LIST_VERSION_URL = [
				"template/tabs/tab.html", "template/tabs/tabset.html",
				"template/tooltip/tooltip-html-popup.html"],
			config.inWhiteListAddVersion = function (url) {
				if (url.startsWith(config.BASE_SERVICE_URL)) {
					return true;
				}
				if (url.startsWith('template/')) {
					return true;
				}
				if (url.indexOf('?tsVersion=')) {
					return true;
				}
				for (var str in config.LIST_WHITE_LIST_VERSION_URL) {
					if (url
						.indexOf(config.LIST_WHITE_LIST_VERSION_URL[str]) >= 0) {
						return true;
					}
				}
			}

		config.ROLE_ID = {
			employee_roleID_CDT_PTGST: 102,// 4 - RoleID 102
			employee_roleID_CDT_DDPN: 103,// 2 - RoleID 103
			employee_roleID_DT_KTTC: 104,// 5 - RoleID 104
			employee_roleID_DT_GDNT: 105,// 3 - RoleID 105
			employee_roleID_DT_PTTC: 106,// 1 - RoleID 106
			employee_roleID_TVTK_DDTV: 107,// 6 - RoleID 107
			employee_roleID_TVTK_CNTK: 108,// 7 - RoleID 108
			employee_roleID_TVGS_GSTC: 109, // 0 - RoleID 109
			employee_roleID_TVGS_PTGST: 110,// 8 - RoleID 110
			employee_roleID_TVGS_DDTVGS: 111,// 9 - RoleID 111
			employee_roleID_CDT_GSTC: 101,// 10 - RoleID 101
			employee_roleID_CDT_DDDVSDCT: 112
			// 11 - RoleID 112
		}

		// BUILD
		config.BASE_SERVICE_URL = API_URL;

		config.FILE_SERVICE_TEMP = 'fileservice/uploadTemp';
		config.UPLOAD_RS_SERVICE = 'fileservice/uploadATTT';
		config.FILE_SERVICE = 'fileservice/uploadATTT';
		config.DOWNLOAD_SERVICE = API_URL + 'fileservice/downloadFileATTT?';
		config.DOWNLOAD_SERVICE2 = API_URL + 'fileservice/downloadFileATTT2?';
		config.UPLOAD_FOLDER_TYPE_TEMP = 'temp';
		config.UPLOAD_FOLDER_IMAGES = 'input/mobile_images';
		config.contextPath = "wms-web";
		config.prefixLanguage = 'js/languages/',
			config.loginUrl = 'authenServiceRest/login';
		config.getUser = function () {
			return this.user;
		}



		config.setUser = function (user) {
			this.user = user;
			config.userInfo = this.user;
		}



		config.URL_POPUP = {
			DELETE_POPUP: 'wms/popup/Delete_Popup.html',
			VOFICE_POPUP: 'wms/popup/SignVofficePopup.html',
		}

		config.COLUMS_VALIDATE = {
			goods: [
				{
					colum: 'Mã hàng',
					field: 'code',
					dataType: "Text"
				},
				{
					colum: 'Tên hàng',
					field: 'name',
					dataType: "Text"
				},
				{
					colum: 'Đơn vị tính',
					field: 'unit',
					dataType: "Text"
				},
				{
					colum: 'Số lượng',
					field: 'qty',
					dataType: "Number"
				}
			]
		};
		config.TEMPLATE_URL = [
			{
				key: 'HCQT_CATEGORY',
				title: 'Danh mục'

			},
			{
				key: 'FINALIZATION_ASSET',
				title: 'Quyết toán tài sản',
				templateUrl: 'asset/finalizationasset/finalization_assetForm.html',
				lazyLoadFiles: [
					'asset/finalizationasset/FinalizationAssetController.js',
					'asset/finalizationasset/FinalizationAssetService.js',]
			},
			{
				key: 'DETAIL_ASSET',
				title: 'Chi tiết tài sản',
				templateUrl: 'asset/DetailAsset/detail_assetForm.html',
				lazyLoadFiles: [
					'asset/DetailAsset/DetailAssetController.js',
					'asset/DetailAsset/DetailAssetService.js',
					'asset/catAssetList/detailCatAsset_update.service.js',
					'asset/longTermAssetEntity/longTermAssetEntityService.js',
					'asset/tscd.constants.service.js']
			},
			{
				key: 'GROUND_HANDOVER',
				title: 'Biên bản bàn giao mặt bằng thi công',
				templateUrl: 'qlhc/GroundHandover/ground_handover_list.html',
				lazyLoadFiles: [
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/GroundHandover/GroundHandoverController.js',
					'qlhc/GroundHandover/GroundHandoverService.js',
					'qlhc/theapproval/theapprovalservice.js']
			},
			{
				key: 'LIST_ESTIMATE_WORK',
				title: 'Bảng thẩm định quyết toán chi tiết',
				templateUrl: 'qlhc/estimateTableDetail/estimateListTableDetail.html',
				lazyLoadFiles: [
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/estimateTableDetail/estimateTableDetailController.js',
					'qlhc/estimateTableDetail/estimateTableDetailService.js',
					'qlhc/estimateTableDetail/ExpertiseController.js',
					'qlhc/theapproval/theapprovalservice.js',
					'qlhc/estimateTableDetail/WorkSheetController.js',
					'qlhc/estimateTableDetail/ExpertiseImportController.js',
					'qlhc/inspection/inspectionService.js',]
			},
			{
				key: 'LISTING_CONSTRUCTION',
				// title: 'Tìm kiếm công trình',
				title: 'Quản lý bộ HSHC',
				templateUrl: 'qlhc/listingConstruction/view.html',
				lazyLoadFiles: [
					'qlhc/ProposalEvaluation/ProposalEvaluation.controller.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/catProvinces/service.js',
					'qlhc/catConstrType/service.js',
					'qlhc/ProposalEvaluation/WorkSheetController.js',
					'qlhc/ProposalEvaluation/ExpertiseImportController.js']
			},
			{
				key: 'ESTIMATE_WORK_TABLE_DETAIL',
				title: 'Bảng thẩm định quyết toán chi tiết',
				templateUrl: 'qlhc/estimateTableDetail/estimateTableDetail.html',
				lazyLoadFiles: [
					'qlhc/estimateTableDetail/estimateTableDetailController.js',
					'qlhc/estimateTableDetail/estimateTableDetailService.js',
					'qlhc/inspection/inspectionService.js',]
			},
			{
				key: 'EstablishDetailedSettlementProposalTable',
				title: 'Bảng đề nghị quyết toán chi tiết',
				templateUrl: 'qlhc/establishDetailedSettlementProposalTable/establishDetailedSettlementProposalList.html',
				lazyLoadFiles: [
					'qlhc/establishDetailedSettlementProposalTable/establishDetailedSettlementProposalController.js',
					'qlhc/establishDetailedSettlementProposalTable/establishDetailedSettlementProposalService.js',
					'qlhc/establishDetailedSettlementProposalTable/establishPopupController.js',
					'qlhc/theapproval/theapprovalservice.js',
					'qlhc/inspection/inspectionService.js',]
			},
			{
				key: 'TABLES_ESTIMATE_WORK',
				title: 'Bảng diễn giải khối lượng xây lắp',
				templateUrl: 'qlhc/estimatesworkTables/estimateWorkTables.html',
				lazyLoadFiles: [
					'qlhc/estimatesworkTables/estimateWorkTablesController.js',
					'qlhc/estimatesworkTables/estimateWorkTablesService.js',]
			},

			{
				key: 'ACCEPTANCE_B_LIST',
				title: 'Nghiệm thu chất lượng vật tư B cấp',
				templateUrl: 'qlhc/nghiemthuchatluongvattubenBcap/acceptanceBList.html',
				lazyLoadFiles: [
					'qlhc/nghiemthuchatluongvattubenBcap/acceptanceBList.controller.js',
					'qlhc/nghiemthuchatluongvattubenBcap/acceptanceBListService.js',
					'qlhc/nghiemthuchatluongvattubenBcap/ImportBmaterialController.js',
					// 'qlhc/nghiemthuchatluongvattubenBcap/acceptanceBDetail.controller.js',
					// 'qlhc/nghiemthuchatluongvattubenBcap/acceptanceBDetailService.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/theapproval/theapprovalservice.js',]
			},
			{
				key: 'CONSTR_COMPLETE_RECORD_MAP',
				title: 'Xem bộ HSHC',
				templateUrl: 'qlhc/constrCompleteRecordsMap/view.html',
				lazyLoadFiles: [
					'qlhc/constrCompleteRecordsMap/controller.js',
					'qlhc/constrCompleteRecordsMap/service.js',
					'qlhc/catFileInvoice/service.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/auth/hshcAuthService.js',
					'erp/common/component/ComboBox.js',
					'erp/common/component/SearchBox.js',
					'erp/common/component/TextBox.js'

				]
			},
			{
				key: 'GL_ALLOCATION',
				title: 'Phân bổ - kết chuyển',
				templateUrl: 'erp/glallocation/main.html',
				lazyLoadFiles: ['erp/glallocation/controller.js',
					'erp/glallocation/service.js',
					'erp/glfactlist/fact_list.controller.js',
					'erp/glfactlist/fact_list.service.js',]
			},
			/* dong */
			{
				key: 'CONSTRUCTION_TITAZI_LIST',
				title: 'Nghiệm thu hoàn thành công trình điều chỉnh Tilt và Azimuth',
				templateUrl: 'qlhc/constructionTitAzi/titAziConstrAccept.html',
				lazyLoadFiles: [
					'qlhc/constructionTitAzi/titAziConstrAcceptCtroller.js',
					'qlhc/constructionTitAzi/ImportTitAziController.js',
					'qlhc/constructionTitAzi/titAziConstrAcceptService.js',
					'qlhc/theapproval/theapprovalservice.js',

				]
			},
			{
				key: 'CONSTRUCTION_TITAZI_DETAIL',
				title: 'Nghiệm thu hoàn thành công trình hợp đồng chỉnh Title hoặc Azimuth',
				templateUrl: 'qlhc/constructionTitAzi/titAziConstrAcceptList.html',
				lazyLoadFiles: [
					'qlhc/constructionTitAzi/titAziConstrAcceptListCtroller.js',
					'qlhc/constructionTitAzi/titAziConstrAcceptListService.js',

				]
			},
			{
				key: 'MONITOR_MISSION_ASSIGN',
				title: 'Giao nhiệm vụ giám sát',
				templateUrl: 'qlhc/monitorMissionAssign/monitorMissionAssign.html',
				lazyLoadFiles: [
					'qlhc/monitorMissionAssign/monitorMissionAssignController.js',
					'qlhc/monitorMissionAssign/monitorMissionAssignService.js',
					'qlhc/theapproval/theapprovalservice.js',]
			},
			/* dong end */
//			hoanm1_20181019_comment
		{
				key: 'DASH_BOARD',
				title: 'Home',
//				templateUrl: 'views/catChuyen.html',
				templateUrl: 'views/dashboard.html',
				lazyLoadFiles: [
				      'erp/common/ChartController.js',

			]
			},
//			hoanm1_20181019_end
			// Dodt
			{
				key: 'REPORT_WORKLOAD_IN_PROGRESS_DETAIL',
				title: 'Biên bản hiện trường xác nhận khối lượng thi công dở dang',
				templateUrl: 'qlhc/reportWorkloadInProgress/reportDetail.html',
				lazyLoadFiles: [
					'qlhc/reportWorkloadInProgress/reportDetailCtroller.js',

				]
			},
			{
				key: 'REPORT_WORKLOAD_IN_PROGRESS_LIST',
				title: 'Danh sách Biên bản hiện trường thi công dở dang',
				templateUrl: 'qlhc/reportWorkloadInProgress/reportList.html',
				lazyLoadFiles: [
					'qlhc/reportWorkloadInProgress/reportListCtroller.js',
					/* 'qlhc/reportWorkloadInProgress/reportListservice.js', */

				]
			},
			{
				key: 'INV_CONTIGENCY_SALE',
				title: 'Dự phòng giảm giá hàng tồn kho',
				templateUrl: 'erp/invcontigency/invcontigency.html',
				lazyLoadFiles: [
					'erp/invcontigency/InvContigencyList.controller.js',
					'erp/invcontigency/InvContigencyDetail.service.js',
					'erp/invcontigency/InvContigencyDetail.controller.js',
					'erp/invcontigency/InvContigencyList.service.js',
					'erp/common/component/ComboBox.js',
					'erp/common/component/SearchBox.js',
					'erp/common/component/TextBox.js']
			},
			{
				key: 'ProposalSettlements',
				title: 'Bảng đề nghị quyết toán chi tiết',
				templateUrl: 'erp/proposalSettlement/proposalSettlementList.view.html',
				lazyLoadFiles: [
					'erp/proposalSettlement/invshipmentList.controller.js',
					'erp/proposalSettlement/invshipmentList.service.js',
					'erp/proposalSettlement/invShipmentDetail.service.js',
					'erp/proposalSettlement/invShipmentDetail.controller.js',
					'erp/common/component/ComboBox.js',
					'erp/common/component/SearchBox.js',
					'erp/common/component/TextBox.js'

				]
			},
			{
				key: 'ProposalEvaluation',
				// title: 'Tìm kiếm công trình',
				title: 'Quản lý đề nghị và thẩm định QT',
				templateUrl: 'qlhc/ProposalEvaluation/proposalSettlementList.view.html',
				lazyLoadFiles: [
					'qlhc/ProposalEvaluation/ProposalEvaluation.controller.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/catProvinces/service.js',
					'qlhc/catConstrType/service.js',

				]
			},
			// [Tuantm] start

			{
				key: 'BG_VT_TB_A_C',
				title: 'Bàn giao vật tư thiết bị A cấp',
				templateUrl: 'qlhc/bgvttbac/ban_giao_v_t_t__tb_a_c.html',
				lazyLoadFiles: ['qlhc/bgvttbac/bgvttbacService.js',
					'qlhc/bgvttbac/BgvttbacController.js']
			},
			{
				key: 'DS_BG_VT_TB_A_C',
				title: 'Danh sách bàn giao thiết bị A cấp',
				templateUrl: 'qlhc/bgvttbac/danh_sach_ban_giao_tb_a_c.html',
				lazyLoadFiles: [
					'qlhc/ProposalEvaluation/ProposalEvaluation.controller.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/bgvttbac/dsbgvtService.js',
					'qlhc/thesignca/thesigncaservice.js',
					'qlhc/bgvttbac/DsbgvtController.js',
					'qlhc/bgvttbac/bgvttbacService.js',

				]
			},
			{
				key: 'DANH_SACH_PHIEU_YC_NGHIEM_THU',
				title: 'Phiếu yêu cầu nghiệm thu đưa vào sử dụng công trình',
				templateUrl: 'qlhc/pycnt/danh_sach_phieu_yeu_cau_nghiem_thu.html',
				lazyLoadFiles: [
					'qlhc/ProposalEvaluation/ProposalEvaluation.controller.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/pycnt/dspycntService.js',
					'qlhc/thesignca/thesigncaservice.js',
					'qlhc/pycnt/DspycntController.js',
					'qlhc/pycnt/pycntService.js',
					'qlhc/theapproval/theapprovalservice.js',]
			},
			{
				key: 'PHIEU_YC_NGHIEM_THU',
				title: 'Phiếu yêu cầu nghiệm thu',
				templateUrl: 'qlhc/pycnt/phieu_yeu_cau_nghiem_thu.html',
				lazyLoadFiles: [
					'qlhc/pycnt/PycntController.js',
					'qlhc/pycnt/pycntService.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.controller.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js']
			},
			// [Tuantm] end
			{
				key: 'INSP_CONGTRINH',
				title: 'Nghiệm thu đưa vào sử dụng công trình',
				templateUrl: 'qlhc/inspection/main.html',
				lazyLoadFiles: [
					'qlhc/inspection/inspectionController.js',
					'qlhc/inspection/inspectionService.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/thesignca/thesigncaservice.js',]
			},
			{
				key: 'INSP_INCOMPLETE',
				title: 'Xác nhận thi công dở dang',
				templateUrl: 'qlhc/inspection/mainInspection.html',
				lazyLoadFiles: [
					'qlhc/inspection/inspectionController.js',
					'qlhc/inspection/inspectionService.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/thesignca/thesigncaservice.js',]
			},
			{
				key: 'INSP_List_CONGTRINH',
				title: 'Danh sách nghiệm thu hoàn thành CT',
				templateUrl: 'qlhc/inspectionList/inspectionList.html',
				lazyLoadFiles: [
					'qlhc/inspectionList/inspectionListController.js',
					'qlhc/inspectionList/inspectionListService.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',

				]
			},
			{
				key: 'distanceUnloadedMaterialsList',
				title: 'Nghiệm thu công việc vận chuyển',
				templateUrl: 'qlhc/distanceUnloadedMaterialsList/distanceUnloadedMaterialsList.view.html',
				lazyLoadFiles: [
					'qlhc/distanceUnloadedMaterialsList/distanceUnloadedMaterialsList.controller.js',
					'qlhc/distanceUnloadedMaterialsList/distanceUnloadedMaterialsList.service.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/inspection/inspectionService.js',
					'qlhc/theapproval/theapprovalservice.js',]
			},
			{
				key: 'INV_MARKET_PRICE',
				title: 'Giá thị trường ',
				templateUrl: 'erp/invmarketprice/inv_market_price.html',
				lazyLoadFiles: [
					'erp/invmarketprice/InvMarketPriceController.js',
					'erp/invmarketprice/InvMarketPriceService.js']
			},
			{
				key: 'INV_DETAIL_INOUT',
				title: 'Danh sách chi tiết nhập - xuất kho',
				templateUrl: 'erp/invdetailinout/invdetailinout.html',
				lazyLoadFiles: [
					'erp/invdetailinout/InvDetailInOutController.js',
					'erp/invdetailinout/InvDetailInOutService.js']
			},
			{
				key: 'Report_Result',
				title: 'Báo cáo kết quả đo chất lượng tuyến cáp',
				templateUrl: 'qlhc/reportresult/report_result.html',
				lazyLoadFiles: [
					'qlhc/reportresult/report_result_controller.js',
					'qlhc/reportresult/report_result_service.js',
					'qlhc/reportresult/list_report_result_controller.js',
					'qlhc/reportresult/list_report_result_service.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/reportresult/list_report_result_controller.js',]
			},
			{
				key: 'List_Report_Result',
				title: 'Danh sách báo cáo chất lượng tuyến cáp',
				templateUrl: 'qlhc/reportresult/list_report_result.html',
				lazyLoadFiles: [
					'qlhc/reportresult/list_report_result_controller.js',
					'qlhc/reportresult/list_report_result_service.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/theapproval/theapprovalservice.js']
			},
			{
				key: 'Report_Retrieve_A',
				title: 'Biên bản thu hồi VTTB A cấp thừa',
				templateUrl: 'qlhc/reportretrieveA/reportretrievelostA.html',
				lazyLoadFiles: [
					'qlhc/reportretrieveA/reportretrieveAcontroller.js',
					'qlhc/reportretrieveA/reportretrieveAservice.js',
					'qlhc/reportretrieveA/listreportretrieveAcontroller.js',
					'qlhc/reportretrieveA/list_report_retrieve_A_service.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/thesignca/thesigncaservice.js',]
			},
			{
				key: 'Report_Retrieve_List_A',
				title: 'Biên bản thu hồi VTTB A cấp thừa',
				templateUrl: 'qlhc/reportretrieveA/listreportretrieveA.html',
				lazyLoadFiles: [
					'qlhc/reportretrieveA/listreportretrieveAcontroller.js',
					'qlhc/reportretrieveA/list_report_retrieve_A_service.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/thesignca/thesigncaservice.js']
			},
			{
				key: 'FIND_CONSTRUCTION',
				title: 'Tìm kiếm công trình',
				templateUrl: 'qlhc/advancepayment30/find_construction.html',
				lazyLoadFiles: [
					'qlhc/advancepayment30/FindContructionController.js',
					'qlhc/advancepayment30/FindContruction30Service.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/catProvinces/service.js',
					'qlhc/catConstrType/service.js',
					'erp/common/component/ComboBox.js',
					'erp/common/component/SearchBox.js',
					'erp/common/component/TextBox.js']
			},
			{
				key: 'SUGGGET_ADPAYMENT',
				title: 'Đề nghị tạm ứng',
				templateUrl: 'qlhc/suggetadpayment/suggetAdPayment.html',
				lazyLoadFiles: [
					'qlhc/suggetadpayment/suggetAdPaymentController.js',
					'qlhc/suggetadpayment/suggetAdPaymentService.js',
					'qlhc/advancepayment30/FindContruction30Service.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',]

			},// ChuongNV
			{
				key: 'CREATE_MEMORY_WORK',
				title: 'Nhật ký công trình',
				templateUrl: 'qlhc/creatememorywork/creatememoryworkshow.html',
				lazyLoadFiles: [
					'qlhc/creatememorywork/creatememoryworkcontroller.js',
					'qlhc/creatememorywork/create_memory_work_service.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/theapproval/theapprovalservice.js',]
			},
			{
				key: 'CONSTR_WORK_LOGS_THE_APPROVAL',
				title: 'Bìa nhật ký công trình',
				templateUrl: 'qlhc/constrworklogstheapproval/constrworklogstheapprovalshow.html',
				lazyLoadFiles: [
					'qlhc/constrworklogstheapproval/constrworklogstheapprovalcontroller.js',
					'qlhc/constrworklogstheapproval/constrworklogstheapprovalservice.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/creatememorywork/create_memory_work_service.js',
					'qlhc/theapproval/theapprovalservice.js',]
			},
			{
				key: 'LIST_WORK_ORGANIZATION_PLAN',
				title: 'Phương án tổ chức thi công',
				templateUrl: 'qlhc/listconstructionorganizationplan/listConstructionOrganizationPlanShow.html',
				lazyLoadFiles: [
					'qlhc/listconstructionorganizationplan/listConstructionOrganizationPlanController.js',
					'qlhc/listconstructionorganizationplan/listConstructionOrganizationPlanService.js',
					'qlhc/thesignca/thesigncaservice.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',]
			},
			{
				key: 'THE_APPROVAL',
				title: 'Trình duyệt',
				templateUrl: 'qlhc/theapproval/theapprovalshow.html',
				lazyLoadFiles: [
					'qlhc/theapproval/theapprovalcontroller.js',
					'qlhc/theapproval/theapprovalservice.js',]
			},
			{
				key: 'THE_SIGN_CA',
				title: 'Trình ký CA',
				templateUrl: 'qlhc/thesignca/thesigncashow.html',
				lazyLoadFiles: ['qlhc/thesignca/thesigncacontroller.js',
					'qlhc/thesignca/thesigncaservice.js',]
			},// NgocCX
			{
				key: 'CONSTR_LIST',
				title: 'Danh sách công việc xây lắp',
				templateUrl: 'qlhc/constructionList/constructionList.html',
				lazyLoadFiles: [
					'qlhc/constructionList/constructionListController.js',
					'qlhc/constructionList/constructionListService.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/auth/hshcAuthService.js',]
			},
			{
				key: 'DRAWINGS_LIST',
				title: 'Bản vẽ hoàn công',
				templateUrl: 'qlhc/drawingsList/drawingsList.html',
				lazyLoadFiles: [
					'qlhc/drawingsList/drawingsListController.js',
					'qlhc/drawingsList/drawingsListService.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/reportresult/report_result_service.js',
					'qlhc/thesignca/thesigncaservice.js',
					'qlhc/drawingsList/createDrawingsEditController.js',]
			},
			{
				key: 'HSHC_AUTHENTICATION',
				title: 'Phân quyền HSHC công trình',
				templateUrl: 'qlhc/auth/hshcAuth.html',
				lazyLoadFiles: [
					'qlhc/auth/hshcAuthController.js',
					'qlhc/auth/hshcAuthService.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/bgvttbac/dsbgvtService.js']
			},
			{
				key: 'LIST_PARTNER_HR',
				title: 'Danh mục nhân sự đối tác',
				templateUrl: 'qlhc/partnerhr/partnerHrList.html',
				lazyLoadFiles: [
					'qlhc/partnerhr/partnerHrListController.js',
					'qlhc/partnerhr/partnerHrListService.js']
			},
			// tunpv
			{
				key: 'ACCEPTANCE_OF_CONSTRUCTION_JOBS',
				title: 'Nghiệm thu công việc xây dựng',
				templateUrl: 'qlhc/acceptanceOfConstructionJobs/acceptanceOfConstructionJobs.html',
				lazyLoadFiles: [
					'qlhc/acceptanceOfConstructionJobs/acceptanceOfConstructionJobsController.js',
					'qlhc/acceptanceOfConstructionJobs/acceptanceOfConstructionJobsService.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/theapproval/theapprovalservice.js',]
			},
			{
				key: 'SCENE_GENERATE_WORK',
				title: 'Biên bản hiện trường và phát sinh khối lượng công việc',
				templateUrl: 'qlhc/reportSceneAriseWeigh/reportSceneAriseWeigh.html',
				lazyLoadFiles: [
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/reportSceneAriseWeigh/reportSceneAriseWeighController.js',
					'qlhc/reportSceneAriseWeigh/reportSceneAriseWeighService.js',
					'qlhc/reportSceneAriseWeigh/importBieuMauController.js',
					'qlhc/theapproval/theapprovalservice.js',]
			},
			{
				key: 'SCENE_GENERATE_CONSTRUCT',
				title: 'Biên bản hiện trường và phát sinh của công trình',
				templateUrl: 'qlhc/reportscenegenerate/reportSceneGenerate.html',
				lazyLoadFiles: [
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/reportscenegenerate/reportSceneGenerateController.js',
					'qlhc/reportscenegenerate/reportSceneGenerateService.js',
					'qlhc/reportscenegenerate/importReportSceneGenerateController.js',
					'qlhc/theapproval/theapprovalservice.js',]
			},
			// end tungpv
			// thanghv
			{
				key: 'CR_LOOKING_WORK_TRANSPORTATION',
				title: 'Tìm kiếm công việc vận chuyển phát sinh để nghiệm thu',
				templateUrl: 'qlhc/TimKiemCvVanChuyenPhatSinhDeNt/cr_looking_work_transportation_view.html',
				lazyLoadFiles: ['qlhc/TimKiemCvVanChuyenPhatSinhDeNt/cr_looking_work_transportation.controller.js']
			},
			{
				key: 'CR_DISTANCE_UPLOAD_MATERIALS',
				title: 'Biên bản xác nhận cự ly và bốc dỡ vật liệu thi công',
				templateUrl: 'qlhc/BienBanXacNhanCuLyVaBocDoVlThiCong/cr_distance_upload_materials_view.html',
				lazyLoadFiles: [
					'qlhc/BienBanXacNhanCuLyVaBocDoVlThiCong/CrDistanceUploadMaterialsController.js',
					'qlhc/BienBanXacNhanCuLyVaBocDoVlThiCong/CrDistanceUploadMaterialsService.js'

				]
			},
			{
				key: 'CR_LIST_DISTANCE_UPLOAD_MATERIALS',
				title: 'Danh sách biên bản xác nhận cự ly và bốc dỡ vật liệu thi công',
				templateUrl: 'qlhc/DanhSachBienBanXacNhanCuLyVaBocDoVlThiCong/cr_list_distance_upload_materials_view.html',
				lazyLoadFiles: [
					'qlhc/DanhSachBienBanXacNhanCuLyVaBocDoVlThiCong/CrListDistanceUploadMaterialsController.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/DanhSachBienBanXacNhanCuLyVaBocDoVlThiCong/CrListDistanceUploadMaterialsService.js']
			},
			{ // linhnn
				key: 'FIND_CONSTRUCTION',
				title: 'Tìm kiếm công trình',
				templateUrl: 'qlhc/advancepayment30/find_construction.html',
				lazyLoadFiles: [
					'qlhc/advancepayment30/FindContructionController.js',
					'qlhc/advancepayment30/FindContruction30Service.js',]
			},
			{
				key: 'SUGGGET_ADPAYMENT',
				title: 'Đề nghị tạm ứng',
				templateUrl: 'erp/suggetadpayment/suggetAdPayment.html',
				lazyLoadFiles: [
					'qlhc/suggetadpayment/suggetAdPaymentController.js',
					'qlhc/suggetadpayment/suggetAdPaymentService.js',
					'qlhc/advancepayment30/FindContruction30Service.js',

				]

			},
			{
				key: 'LIST_CURRENT_STATE_HANDOVER',
				title: 'Biên bản bàn giao hiện trạng',
				templateUrl: 'qlhc/listReportCrStHandover/listReportCrStHandover.html',
				lazyLoadFiles: [
					'qlhc/listReportCrStHandover/listReportCrStHandoverController.js',
					'qlhc/listReportCrStHandover/listReportCrStHandoverService.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/reportCrStHandover/reportCrStHandoverController.js',
					'qlhc/reportCrStHandover/reportCrStHandoverService.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/theapproval/theapprovalservice.js'

				]
			}

			,
			{
				key: 'LIST_CWORK_COMPLETE',
				title: 'Biên bản xác nhận khối lượng hoàn thành CT',
				templateUrl: 'qlhc/listCWorkComplete/listCWorkComplete.html',
				lazyLoadFiles: [
					'qlhc/listCWorkComplete/listCWorkCompleteController.js',
					'qlhc/listCWorkComplete/listCWorkCompleteService.js',
					'qlhc/cWorkComplete/cWorkCompleteController.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/bgvttbac/bgvttbacService.js',
					'qlhc/thesignca/thesigncaservice.js',]
			},
			{
				key: 'CWORK_COMPLETE',
				title: 'Biên bản xác nhận khối lượng hoàn thành CT',
				templateUrl: 'qlhc/cWorkComplete/cWorkComplete.html',
				lazyLoadFiles: [
					'qlhc/cWorkComplete/cWorkCompleteController.js',
					'qlhc/cWorkComplete/cWorkCompleteService.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',]
			},
			{
				key: 'IMPORT_PAGE',
				title: 'Import Page',
				templateUrl: 'qlhc/submitforsign/importPage.html',
				lazyLoadFiles: [
					'qlhc/submitforsign/submitforsigncontroller.js',
					'qlhc/submitforsign/submit_for_sign_service.js',]
			},
			{
				key: 'SUBMIT_FOR_SIGN',
				title: 'Trình Ký CA',
				templateUrl: 'qlhc/submitforsign/submitForSign.html',
				lazyLoadFiles: [
					'qlhc/submitforsign/submitforsigncontroller.js',
					'qlhc/submitforsign/submit_for_sign_service.js',]
			},
			{
				key: 'SUBMIT_FOR_APPROVAL',
				title: 'Trình duyệt',
				templateUrl: 'qlhc/submitforsign/submitForApproval.html',
				lazyLoadFiles: [
					'qlhc/submitforsign/submitforapprovalcontroller.js',
					'qlhc/submitforsign/submit_for_approval_service.js',]
			},
			{
				key: 'SUBMIT_APPROVAL',
				title: 'Phê duyệt',
				templateUrl: 'qlhc/submitforsign/submitApproval.html',
				// lazyLoadFiles : [
				// 'qlhc/submitforsign/submitforapprovalcontroller.js',
				// 'qlhc/submitforsign/submit_for_approval_service.js',
				// ]
			},
			{
				key: 'Acceptance_Phase_Work',
				title: 'Nghiệm thu giai đoạn CT',
				templateUrl: 'qlhc/acceptancephasework/acceptancephase.html',
				lazyLoadFiles: [
					'qlhc/acceptancephasework/acceptancephaseworkcontroller.js',
					'qlhc/acceptancephasework/acceptance_phase_work_service.js',
					'qlhc/ProposalEvaluation/ProposalEvaluation.service.js',
					'qlhc/theapproval/theapprovalservice.js']
			},
			{
				key: 'ESTIMATES_AB',
				title: 'Quyết toán A-B',
				templateUrl: 'qlhc/estimatesAB/estimatesAB.html',
				lazyLoadFiles: [
					'qlhc/estimatesAB/estimatesABController.js',
					'qlhc/thesignca/thesigncaservice.js',
					'qlhc/estimatesAB/estimatesABsevice.js',
					'qlhc/thesignca/thesigncaservice.js',]
			},
			// Begin QLTS CD
			{
				key: 'Asset_CatAssetCode',
				title: 'Quản lý mã tài sản',
				templateUrl: 'asset/catAssetCode/listCatAssetCode.html',
				lazyLoadFiles: [
					'asset/catAssetCode/listCatAssetCodeController.js',]

			},
			{
				key: 'ASSET_TSCD',
				title: 'Quản lý giá trị tài sản'

			},
			{
				key: 'Asset_Report_Menu',
				title: 'Báo cáo giá trị tài sản'

			},
			{
				key: 'Asset_ReportS21',
				title: 'Sổ tài sản cố định (S21)',
				templateUrl: 'asset/assetReportS21/reportS21.html',
				lazyLoadFiles: ['asset/assetReportS21/reportS21Controller.js']

			},
			{
				key: 'Asset_ReportS22',
				title: 'Sổ Theo dõi TSCĐ và CDC tại nơi sử dụng (S22)',
				templateUrl: 'asset/assetReportS21/reportS22.html',
				lazyLoadFiles: ['asset/assetReportS21/reportS21Controller.js']

			},
			{
				key: 'Asset_ReportS23',
				title: 'Thẻ tài sản cố định (S23)',
				templateUrl: 'asset/assetReportS21/reportS23.html',
				lazyLoadFiles: ['asset/assetReportS21/reportS21Controller.js']

			},
			{
				key: 'Asset_Report_TSCD_TANG_GIAM_THEO_KY',
				title: 'Báo cáo tăng giảm tài sản trong kỳ',
				templateUrl: 'asset/assetReportS21/reportTSCDTheoDonvi.html',
				lazyLoadFiles: ['asset/assetReportS21/reportS21Controller.js']

			},
			{
				key: 'Asset_Report_TSCD_TANG_GIAM_THEO_DONVI',
				title: 'Báo cáo tăng giảm tài sản cố định theo đơn vị',
				templateUrl: 'asset/assetReportS21/reportTSCDAllDonvi.html',
				lazyLoadFiles: ['asset/assetReportS21/reportS21Controller.js']

			},
			{
				key: 'Asset_CatAssetList',
				title: 'Danh sách tài sản',
				templateUrl: 'asset/catAssetList/listCatAsset.view.html',
				lazyLoadFiles: [
					'asset/longTermAssetEntity/longTermAssetEntityService.js',
					'asset/catAssetList/listCatAsset.controller.js',
					'asset/catAssetList/detailCatAsset_update.service.js',
					'asset/catAssetList/detailCatAsset_update.controller.js',
					'asset/DetailAsset/DetailAssetService.js',
					'asset/tscd.constants.service.js',

				]
			},
			{
				key: 'Asset_TSCDDashBoard',
				title: 'Thông tin cảnh báo',
				templateUrl: 'asset/assetReportS21/tscdDashboard.html',
				lazyLoadFiles: [
					'asset/assetReportS21/tscdDashboardController.js',

				]
			},
			{
				key: 'Asset_CatAssetDetail',
				title: 'Hình thành tài sản qua xây lắp',
				templateUrl: 'asset/catAssetList/detailCatAsset.view.html',
				lazyLoadFiles: [
					'asset/catAssetList/detailCatAsset.controller.js',
					'asset/catAssetList/detailCatAsset.service.js']
			},

			{
				key: 'Asset_CatAssetDetail_update',
				title: 'Hình thành tài sản qua xây lắp',
				templateUrl: 'asset/catAssetList/detailCatAsset_update.view.html',
				lazyLoadFiles: [
					'asset/catAssetList/detailCatAsset_update.controller.js',
					'asset/catAssetList/detailCatAsset_update.service.js']
			},
			{
				// Màn hình tạo tài sản không qua xây lắp
				key: 'Asset_LongTermAsset_Update',
				title: 'Tài sản không qua xây lắp',
				templateUrl: 'asset/longTermAssetEntity/longTermAsset_Update.view.html',
				lazyLoadFiles: [
					'asset/longTermAssetEntity/longTermAsset_Update.Controller.js',
					'asset/longTermAssetEntity/longTermAssetEntityService.js']

			},
			{
				key: 'LONG_TERM_ASSET_ENTITY',
				title: 'Tài sản không qua xây lắp',
				templateUrl: 'asset/longTermAssetEntity/longTermAssetEntity.html',
				lazyLoadFiles: [
					'asset/longTermAssetEntity/longTermAssetEntityController.js',
					'asset/longTermAssetEntity/longTermAssetEntityService.js',
					'asset/longTermAssetEntity/longTermAssetEntityUpdate.js']

			},
			{
				key: 'LONG_TERM_ASSET_ENTITY_UPDATE',
				title: 'Tài sản không qua xây lắp',
				templateUrl: 'asset/longTermAssetEntity/longTermAssetEntityUpdate.html',
				lazyLoadFiles: [
					'asset/longTermAssetEntity/longTermAssetEntityController.js',
					'asset/longTermAssetEntity/longTermAssetEntityService.js',
					'asset/longTermAssetEntity/longTermAssetEntityUpdate.js']

			},
			{
				key: 'Asset_DS_BienbanbanGiao',
				title: 'Danh sách biên bản bàn giao',
				templateUrl: 'asset/listAssetHandOver/listAssetHandOver.html',
				lazyLoadFiles: [
					'asset/listAssetHandOver/listAssetHandOverController.js',]
			},
			{
				key: 'Asset_DS_BienbanbanGiao_QuaXayLap',
				title: 'Danh sách biên bản bàn giao qua xây lắp',
				templateUrl: 'asset/listAssetHandOver/listAssetHandOverByConstruction.html',
				lazyLoadFiles: ['asset/listAssetHandOver/listAssetHandOverByConstructionController.js'

				]
			},
			{
				key: 'Asset_DS_BienbanbanGiao_KoQuaXayLap',
				title: 'Danh sách biên bản bàn giao không qua xây lắp',
				templateUrl: 'asset/listAssetHandOver/listAssetHandOverNotByConstruction.html',
				lazyLoadFiles: [
					'asset/listAssetHandOver/listAssetHandOverNotByConstructionController.js',
					'asset/tscd.constants.service.js']
			},
			{
				key: 'Asset_View_BienbanbanGiao_KoQuaXayLap',
				title: 'Thông tin biên bản bàn giao không qua xây lắp',
				templateUrl: 'asset/listAssetHandOver/assetHandOverDetailNotByConstruction.html',
				lazyLoadFiles: [
					'asset/listAssetHandOver/assetHandOverDetailNotByConstruction.Controller.js',
					'asset/tscd.constants.service.js']
			},
			{
				key: 'Asset_NangCap_TS_KhongQuaXayLap',
				title: 'Nâng cấp trạm ',
				templateUrl: 'asset/upgrateLta/longTermAsset_Upgrade_ByConstr.view.html',
				lazyLoadFiles: [
					'asset/upgrateLta/longTermAsset_Upgrade_ByConstr.Controller.js',
					'asset/upgrateLta/longTermAsset_UpgradeService.js',
					'asset/tscd.constants.service.js',
					'asset/DetailAsset/DetailAssetService.js']
			},
			{
				key: 'Asset_DS_BienbanbanGiao_NangCapTram',
				title: 'Danh sách biên bản bàn giao nâng cấp trạm',
				templateUrl: 'asset/listAssetHandOver/listAssetHandOverUpgradeConstr.html',
				lazyLoadFiles: [
					'asset/listAssetHandOver/listAssetHandOverUpgradeConstrController.js',
					'asset/tscd.constants.service.js']

			},
			{
				key: 'Asset_HinhThanhTS_TuPhieuDeNghi',
				title: 'Danh sách biên bản bàn giao nâng cấp trạm',
				templateUrl: 'asset/ltaFromOfferSlip/ltaFromOfferSlipForm.view.html',
				lazyLoadFiles: [
					'asset/ltaFromOfferSlip/ltaFromOfferSlipController.js',
					'asset/tscd.constants.service.js',
					'asset/catAssetList/detailCatAsset_update.service.js']

			},
			{
				key: 'Test-Control',
				title: 'Test các chức năng',
				templateUrl: 'control/multipleCheckBox/multiCheckBoxSample.html',
				lazyLoadFiles: [
					'control/multipleCheckBox/multipleCheckboxController.js',
					'asset/tscd.constants.service.js']

			},
			{
				key: 'CAT_PARTSList', // Màn hình danh sách
				title: 'Danh sách CAT_PARTS',
				templateUrl: 'ims/catParts/CatPartsList.view.html',
				lazyLoadFiles: [
					'ims/catParts/CatPartsList.controller.js',
					'ims/catParts/CatParts.service.js']
			},
			{
				key: 'CAT_PARTSForm',
				title: 'Chi tiết CAT_PARTS', // Màn hình form
				templateUrl: 'ims/catParts/CatPartsForm.view.html',
				lazyLoadFiles: [
					'ims/catParts/CatPartsForm.controller.js',
					'ims/catParts/CatParts.service.js']
			},
			{
				key: 'CAT_BANK_BRANCH_BAKList', // Màn hình danh sách
				title: 'Danh sách CAT_BANK_BRANCH_BAK',
				templateUrl: 'ims/catBankBranchBak/CatBankBranchBakList.view.html',

				lazyLoadFiles: [
					'ims/catBankBranchBak/CatBankBranchBakList.controller.js',
					'ims/catBankBranchBak/CatBankBranchBak.service.js']
			},
			{
				key: 'CAT_BANK_BRANCH_BAKForm',
				title: 'Chi tiết CAT_BANK_BRANCH_BAK', // Màn hình form
				templateUrl: 'ims/catBankBranchBak/CatBankBranchBakForm.view.html',
				lazyLoadFiles: [
					'ims/catBankBranchBak/CatBankBranchBakForm.controller.js',
					'ims/catBankBranchBak/CatBankBranchBak.service.js']
			},
			{
				key: 'APP_PARAM',
				title: 'Danh mục tham số ',
				parent: 'Quản lý danh mục',
				templateUrl: 'wms/app_param/app_paramList.html',
				lazyLoadFiles: ['wms/app_param/app_paramController.js',
					'wms/app_param/app_paramService.js',]
			},

			{
				key: 'DASH_BOARD',
				title: 'Trang chủ',
				templateUrl: 'wms/dashbroad/dashbroad.html',
				lazyLoadFiles: [
					'wms/dashbroad/dashbroadController.js',
				]
			},
			{
				key: 'REASON',
				title: 'Danh mục lý do ',
				parent: 'Quản lý danh mục',
				templateUrl: 'wms/reason/reasonList.html',
				lazyLoadFiles: ['wms/reason/reasonController.js',
					'wms/reason/reasonService.js',]
			},

			{
				key: 'STOCK',
				title: 'Danh mục kho ',
				parent: 'Quản lý danh mục',
				templateUrl: 'wms/stock/stockList.html',
				lazyLoadFiles: ['js/jszip.min.js',
					'wms/stock/stockController.js',
					'wms/stock/stockService.js',
					'wms/app_param/app_paramController.js',
					'wms/app_param/app_paramService.js',
					'wms/common/popupDepartmentController.js']
			},

			{
				key: 'TAX',
				title: 'Danh mục loại thuế ',
				parent: 'Quản lý danh mục',
				templateUrl: 'wms/tax/taxList.html',
				lazyLoadFiles: ['wms/tax/taxController.js',
					'wms/tax/taxService.js',]
			},
			// quản lý lô hàng
			{
				key: 'SHIPMENT',
				title: 'Quản lý danh sách lô hàng',
				templateUrl: 'wms/shipment/shipmentList.html',
				parent: 'Quản lý lô hàng',
				lazyLoadFiles: ['js/jszip.min.js',
					'wms/shipment/shipmentController.js',
					'wms/shipment/shipmentService.js',
					'wms/shipmentDetail/shipmentDetailController.js',
					'wms/shipmentDetail/shipmentDetailService.js',
					'wms/shipmentGoods/shipmentGoodsService.js',
				]
			},
			{
				key: 'SHIPMENTGOODS',
				title: 'Định lượng tỉ trọng kỹ thuật',
				parent: 'Quản lý lô hàng',
				templateUrl: 'wms/shipmentGoods/shipmentGoodsList.html',
				lazyLoadFiles: ['js/jszip.min.js',
					'wms/shipmentGoods/shipmentGoodsController.js',
					'wms/shipmentGoods/shipmentGoodsService.js',
					'wms/shipmentDetail/shipmentDetailController.js',
					'wms/shipmentDetail/shipmentDetailService.js',
					'wms/shipment/shipmentService.js',]
			},
			{
				key: 'SHIPMENTPRICE',
				title: 'Định giá tài chính',
				parent: 'Quản lý lô hàng',
				templateUrl: 'wms/shipmentPrice/shipmentPricesList.html',
				lazyLoadFiles: ['js/jszip.min.js',
					'wms/shipmentPrice/shipmentPriceController.js',
					'wms/shipmentPrice/shipmentPriceService.js',
					'wms/shipmentDetail/shipmentDetailController.js',
					'wms/shipmentDetail/shipmentDetailService.js',
					'wms/shipment/shipmentService.js',]
			},
			// quản lý lô hàng end

			// báo cáo
			{
				key: 'STOCK_DAILY_IMPORT_EXPORT',
				title: 'Báo cáo xuất/nhập kho trong kỳ',
				parent: 'Báo cáo',
				templateUrl: 'wms/stockDailyImportExport/stockDailyImportExportList.html',
				lazyLoadFiles: [
					'wms/stockDailyImportExport/stockDailyImportExportController.js',
					'wms/stockDailyImportExport/stockDailyImportExportService.js',]
			},

			{
				key: 'STOCK_GOODS_TOTAL',
				title: 'Báo cáo tồn kho',
				parent: 'Báo cáo',
				templateUrl: 'wms/stockGoodsTotal/stockGoodsTotalList.html',
				lazyLoadFiles: [
					'wms/stockGoodsTotal/stockGoodsTotalController.js',
					'wms/stockGoodsTotal/stockGoodsTotalService.js',]
			},

			{
				key: 'STOCK_GOODS_TOTAL_REPONSE',
				title: 'Báo cáo khả năng đáp ứng',
				parent: 'Báo cáo',
				templateUrl: 'wms/stockGoodsTotalReponse/stockGoodsTotalReponseList.html',
				lazyLoadFiles: [
					'wms/stockGoodsTotalReponse/stockGoodsTotalReponseController.js',
					'wms/stockGoodsTotalReponse/stockGoodsTotalReponseService.js',]
			},

			{
				key: 'STOCK_GOODS_KPI',
				title: 'Báo cáo KPI tồn kho ',
				parent: 'Báo cáo',
				templateUrl: 'wms/stock_goods_kpi/stock_goods_kpiList.html',
				lazyLoadFiles: [
					'wms/stock_goods_kpi/stock_goods_kpiController.js',
					'wms/stock_goods_kpi/stock_goods_kpiService.js',]
			},

			{
				key: 'STOCK_TRANS',
				title: 'Báo cáo phiếu xuất kho đang đi đường ',
				parent: 'Báo cáo',
				templateUrl: 'wms/stock_trans/stock_transList.html',
				lazyLoadFiles: [
					'wms/stock_trans/stock_transController.js',
					'wms/stock_trans/stock_transService.js',]
			},
			// báo cáo end

			// quản lý xuất kho
			{
				key: 'DELIVERY_ORDER',
				title: 'Viết phiếu xuất kho',
				parent: 'Quản lý xuất kho',
				templateUrl: 'wms/createExportNote/createExportNoteList.html',
				lazyLoadFiles: ['js/jszip.min.js',
					'wms/createExportNote/createExportNoteController.js',
					'wms/createExportNote/createExportNoteService.js',
					'wms/exportRequestManage/createExportRequestManageService.js',
					'wms/exportRequestManage/exportRequestManageService.js',
				]
			},

			{
				key: 'EXPORT_STATEMENT_MAN',
				title: 'Quản lý phiếu xuất kho',
				parent: 'Quản lý xuất kho',
				templateUrl: 'wms/exportStatementManagement/exportStatementMan.html',
				lazyLoadFiles: [
					'js/jszip.min.js',
					'wms/exportStatementManagement/exportStatementController.js',
					'wms/exportStatementManagement/exportService.js',
					'wms/exportRequestManage/createExportRequestManageService.js',
				]
			},

			{
				key: 'EXPORT_STATEMENT_MANAGE',
				title: 'Quản lý yêu cầu xuất kho',
				parent: 'Quản lý xuất kho',
				templateUrl: 'wms/exportRequestManage/exportRequestManage.html',
				lazyLoadFiles: [
					'wms/exportRequestManage/exportRequestManageController.js',
					'wms/exportRequestManage/exportRequestManageService.js',
					'wms/exportRequestManage/createExportRequestManageService.js'
				]
			},
			{
				key: 'CREATE_EX_REQ_MANA',
				title: 'Tạo mới yêu cầu xuất kho',
				parent: 'Quản lý xuất kho',
				templateUrl: 'wms/exportRequestManage/createExportRequestManage.html',
				lazyLoadFiles: [
					'wms/exportRequestManage/createExportRequestManageController.js',
					'wms/exportRequestManage/createExportRequestManageService.js',
					'wms/importRequirementManagement/impRequirementManagementService.js',
					'wms/exportRequestManage/exportRequestManageService.js'


				]
			},
			{
				key: 'UPDATE_EX_REQ_MANA',
				title: 'Cập nhật yêu cầu xuất kho',
				parent: 'Quản lý xuất kho',
				templateUrl: 'wms/exportRequestManage/createExportRequestManage.html',
				lazyLoadFiles: [
					'wms/exportRequestManage/createExportRequestManageController.js',
					'wms/exportRequestManage/createExportRequestManageService.js',
					'wms/exportRequestManage/exportRequestManageService.js',
					'wms/importRequirementManagement/impRequirementManagementService.js',
				]
			},
			// quản lý xuất kho ends

			{
				key: 'IM_REQ_MANAGE',
				title: 'Quản lý yêu cầu nhập kho',
				parent: 'Quản lý nhập kho',
				templateUrl: 'wms/importRequirementManagement/importRequirementManagement.html',
				lazyLoadFiles: [
					'wms/importRequirementManagement/impRequirementManagementController.js',
					'wms/importRequirementManagement/impRequirementManagementService.js',
					'wms/createImportRequirementManagement/createImportRequirementManagementService.js',]
			},
			{
				key: 'CREATE_IM_REQ_MANAGE',
				title: 'Tạo mới yêu cầu nhập kho',
				parent: 'Quản lý nhập kho',
				templateUrl: 'wms/createImportRequirementManagement/createImportRequirementManagement.html',
				lazyLoadFiles: [
					'wms/createImportRequirementManagement/createImportRequirementManagementController.js',
					'wms/createImportRequirementManagement/createImportRequirementManagementService.js',
					'wms/importRequirementManagement/impRequirementManagementService.js',]
			},
			{
				key: 'UPDATE_IM_REQ_MANAGE',
				title: 'Cập nhật yêu cầu nhập kho',
				parent: 'Quản lý nhập kho',
				templateUrl: 'wms/createImportRequirementManagement/createImportRequirementManagement.html',
				lazyLoadFiles: [
					'wms/createImportRequirementManagement/createImportRequirementManagementController.js',
					'wms/createImportRequirementManagement/createImportRequirementManagementService.js',
					'wms/importRequirementManagement/impRequirementManagementService.js',]
			},
			{
				key: 'CREATE_IM_NOTE',
				title: 'Viết phiếu nhập kho',
				parent: 'Quản lý nhập kho',
				templateUrl: 'wms/createImportNote/createImportNote.html',
				lazyLoadFiles: [
					'wms/createImportNote/createImportNoteController.js',
					'wms/createImportNote/createImportNoteService.js',
					'wms/createImportRequirementManagement/createImportRequirementManagementService.js',
					'wms/importRequirementManagement/impRequirementManagementService.js',]
			},
			{
				key: 'IM_NOTE_MANAGE',
				title: 'Quản lý phiếu nhập kho',
				parent: 'Quản lý nhập kho',
				templateUrl: 'wms/importNoteManagement/importNoteManagement.html',
				lazyLoadFiles: [
					'wms/importNoteManagement/importNoteManagementController.js',
					'wms/importNoteManagement/importNoteManagementService.js',
					'wms/importRequirementManagement/impRequirementManagementService.js',
					'wms/createImportRequirementManagement/createImportRequirementManagementService.js',]
			},
			{
				key: 'STOCK_GOODS_SERIAL',
				title: 'Tra cứu serial',
				parent: 'Tiện ích cấu hình',
				templateUrl: 'wms/findSerial/findSerial.html',
				lazyLoadFiles: ['wms/findSerial/findSerialController.js',
					'wms/findSerial/findSerialService.js',]
			},
			{
				key: 'PATTERN_REQUIRE_MANAGEMENT',
				title: 'Quản lý mẫu yêu cầu',
				parent: 'Tiện ích cấu hình',
				templateUrl: 'wms/patternRequirementManagement/patternRequirementManagement.html',
				lazyLoadFiles: [
					'wms/patternRequirementManagement/patternRequirementManagementController.js',
					'wms/patternRequirementManagement/patternRequirementManagementService.js',]
			},
			{
				key: 'ODD_CABLE_MANAGEMENT',
				title: 'Quản lý cáp lẻ',
				parent: 'Tiện ích cấu hình',
				templateUrl: 'wms/oddCableManagement/oddCableManagement.html',
				lazyLoadFiles: [
					'wms/oddCableManagement/oddCableManagementController.js',
					'wms/oddCableManagement/oddCableManagementService.js',]
			},
			{
				key: 'STOCK_SIHN_CONFIGURATION',
				title: 'Cấu hình người ký theo kho',
				parent: 'Tiện ích cấu hình',
				templateUrl: 'wms/stockSignConfiguration/stockSignConfiguration.html',
				lazyLoadFiles: [
					'wms/stockSignConfiguration/stockSignConfigurationController.js',
					'wms/stockSignConfiguration/stockSignConfigurationService.js',]
			},

			{
				key: 'VIEW_STOCK_IN_TRADE',
				title: 'Xem thông tin hàng tồn kho',
				parent: 'Quản lý tồn kho',
				templateUrl: 'wms/stock_in_trade/view_stock_in_trade.html',
				lazyLoadFiles: ['js/jszip.min.js',
					'wms/stock_in_trade/viewstockInTradeController.js',
					'wms/stock_in_trade/viewstockInTradeService.js',
					'wms/stockGood/stockGoodController.js',
					'wms/stockGood/stockGoodService.js',]
			},
			{
				key: 'CHANGE_STOCK_IN_TRADE_EDIT',
				title: 'Quản lý thay đổi hàng hóa',
				parent: 'Quản lý tồn kho',
				templateUrl: 'wms/stock_in_trade/change_product.html',
				lazyLoadFiles: ['js/jszip.min.js',
					'wms/stock_in_trade/changeProductController.js',
					'wms/stock_in_trade/changeProductService.js',]
			},
			{
				key: 'CHANGE_STOCK_IN_TRADE_CREATE',
				title: 'Quản lý thay đổi hàng hóa',
				parent: 'Quản lý tồn kho',
				templateUrl: 'wms/stock_in_trade/change_product.html',
				lazyLoadFiles: ['js/jszip.min.js',
					'wms/stock_in_trade/changeProductController.js',
					'wms/stock_in_trade/changeProductService.js',
				]
			},
			{
				key: 'AUTHORIZED_MANAGE',
				title: 'Phân quyền quản lý kho',
				parent: 'Tiện ích cấu hình',
				templateUrl: 'wms/authorizedManage/authorizedManageList.html',
				lazyLoadFiles: [
					'js/jszip.min.js',
					'wms/authorizedManage/authorizedManageController.js',
					'wms/authorizedManage/authorizedManageService.js',]

			},



			// nhantv quan ly danh muc

			{
				key: 'CAT_PROVINCE',
				title: 'Danh mục tỉnh thành',
				parent: 'Danh mục chung',
				templateUrl: 'cat/province/cat_provinceList.html',
				lazyLoadFiles: [
					'cat/province/cat_provinceController.js',
					'cat/province/cat_provinceService.js',]

			},
			{
				key: 'CAT_PRODUCING_COUNTRY',
				title: 'Nước sản xuất',
				parent: 'Danh mục chung',
				templateUrl: 'cat/producingCountry/producingCountryList.html',
				lazyLoadFiles: [
					'cat/producingCountry/producingCountryController.js',
					'cat/producingCountry/producingCountryService.js',]

			},
			{
				key: 'CAT_MANUFACTURER',
				title: 'Nhà sản xuất',
				parent: 'Danh mục chung',
				templateUrl: 'cat/manufacturer/manufacturerList.html',
				lazyLoadFiles: [
					'cat/manufacturer/manufacturerController.js',
					'cat/manufacturer/manufacturerService.js',]

			},
			, {
				key: 'CAT_PARTNER',
				title: 'Đối tác',
				parent: 'Danh mục đầu tư',
				templateUrl: 'cat/partner/cat_partnerList.html',
				lazyLoadFiles: [
					'cat/partner/cat_partnerController.js',
					'cat/partner/cat_partnerService.js',]

			},

			// end
			//luanlv quản lý kế hoạch năm
			{
				key: 'YEAR_PLAN',
				title: 'Kế hoạch năm',
				parent: 'Quản lý kế hoạch',
				templateUrl: 'coms/yearPlan/yearPlan.html',
				lazyLoadFiles: [
					'coms/yearPlan/yearPlanController.js',
					'coms/yearPlan/yearPlanService.js',]

			},

			//end

			//quangnd quản lý kế hoạch năm
			{
				key: 'CONSTRUCTION_LAND_HANDOVER_PLAN',
				title: 'Quản lý kế hoạch BGMB',
				parent: 'Quản lý công trình',
				templateUrl: 'coms/constructionLandHandPlan/constructionLandHandPlan.html',
				lazyLoadFiles: [
					'coms/constructionLandHandPlan/constructionLandHandPlanController.js',
					'coms/constructionLandHandPlan/deliveryUnitPopupController.js',
					'coms/constructionLandHandPlan/constructionLandHandPlanService.js',
					'coms/construction/catProvinceSearchController.js']

			},

                {
                    key : 'CONTRUCTION_TASK',
                    title : 'Thông tin tiến độ công trình',
                    parent : 'Quản lý tiến độ công trình',
                    templateUrl : 'coms/constructionTask/constructionTask.html',
                    lazyLoadFiles : [
                        'coms/constructionTask/constructionTaskController.js',
                        'coms/constructionTask/constructionTaskService.js',
                        'coms/construction/workItemDetailFunction.js',
                        'coms/detailMonthPlan/detailMonthPlanService.js',
                        'coms/constructionTask/changePerformerFunction.js',
                        'coms/construction/catProvinceSearchController.js'
                         ]

			},
			{
				key: 'CONSTRUCTION',
				title: 'Thông tin công trình',
				parent: 'Quản lý công trình',
				templateUrl: 'coms/construction/construction.html',
				lazyLoadFiles: [
					'coms/construction/constructionController.js',
					'coms/construction/constructionDetailFunction.js',
					'coms/construction/workItemDetailFunction.js',
					'coms/construction/constructionBGMBFunction.js',
					'coms/construction/constructionLicenceFunction.js',
					'coms/construction/constructionDesignFunction.js',
					'coms/construction/startConstructionFunction.js',
					'coms/construction/constructionMerchandiseFunction.js',
					'coms/construction/acceptanceFunction.js',
					'coms/construction/covenantFunction.js',
					'coms/construction/constructionService.js',
					'coms/construction/constrTaskFunction.js',
					'coms/construction/completeFunction.js',
					'coms/construction/catProvinceSearchController.js',
					'coms/RPConstructionDK/catStationHouseSearchController.js' //huypq-20190910-add
					]
			},
			{

				key: 'CONTRUCTION_HSHC',
				title: 'Quản lý HSHC',
				templateUrl: 'coms/contructionHSHC/contructionHSHC.html',
				lazyLoadFiles: [
					'coms/contructionHSHC/contructionHSHCController.js',
					'coms/contructionHSHC/contructionHSHCService.js',
					'coms/construction/catProvinceSearchController.js'


				]
			},
			{

				key: 'CONTRUCTION_DT',
				title: 'Quản lý doanh thu',
				templateUrl: 'coms/contructionDT/contructionDT.html',
				lazyLoadFiles: [
					'coms/contructionDT/contructionDTController.js',
					'coms/contructionDT/contructionDTService.js',
					'coms/construction/catProvinceSearchController.js'
				]
			},

			{

				key: 'RP_YEAR_PROGRESS',
				title: 'Tiến độ KH năm',
				templateUrl: 'coms/rpYearProgress/rpYearProgress.html', lazyLoadFiles: [
					'coms/rpYearProgress/rpYearProgressController.js',
				]
			},
			{
				key: 'RP_MONTH_PROGRESS',
				title: 'Tiến độ KH tháng',
				templateUrl: 'coms/rpMonthProgress/rpMonthProgress.html',
				lazyLoadFiles: [
					'coms/rpMonthProgress/rpMonthProgressController.js',
				]
			},
			{

				key: 'RP_CONSTRUCTION_TASK',
				title: 'Danh sách công việc',
				templateUrl: 'coms/rpConstructionTask/rpConstructionTask.html',
				lazyLoadFiles: [
					'coms/rpConstructionTask/rpConstructionTaskController.js',
					'coms/construction/catProvinceSearchController.js'
				]

			},
			{
				key: 'RP_DAY_QUANTITY',
				title: 'Sản lượng theo ngày',
				templateUrl: 'coms/rpDayQuantity/rpDayQuantity.html',
				lazyLoadFiles: [
					'coms/rpDayQuantity/rpDayQuantityController.js',
				]

			},

			{
				key: 'RETRIEVAL_MANAGEMENT',
				title: 'Quản lý yêu cầu thu hồi VTTB',
				templateUrl: 'coms/retrievalManagement/retrievalManagement.html',
				lazyLoadFiles: [
					'coms/retrievalManagement/retrievalManagementController.js',
					'coms/retrievalManagement/retrievalManagementService.js',
					'coms/quantityByDay/sysUserSearchController.js',
				]

			},
//			hungtd_20181224_start
			{
				key: 'GOODS_PLAN',
				title: 'Kế hoạch sản xuất vật tư',
				parent: 'Quản lý công trình',
				templateUrl: 'coms/goodsPlan/goodsPlan.html',
				lazyLoadFiles: [

//					'coms/rpQuantity/rpQuantityService.js',
//					'coms/rpSumTask/rpSumTaskController.js',
					'coms/construction/catProvinceSearchController.js',
//					'coms/constructionTask/constructionTaskService.js',
					'coms/rpQuantity/rpQuantityService.js',
//					'coms/rpConstruction/contractSearchController.js',
					'coms/rpSumTask/catStationSearchController.js',
					'coms/rpConstruction/contractSearchController.js',
					'coms/rpSumTask/comsConstructionSearchController.js',
					'coms/workItem/workItemService.js',
//					'coms/goodsPlan/goodsPlanPopupController.js',
					'coms/goodsPlan/goodsPlanController.js',
					'coms/goodsPlan/goodsPlanService.js'
					]

			},
//			hungtd_20181224_end

			//end

			//luanlv quản lý kế hoạch tháng tổng thể
			{
				key: 'TOTAL_MONTH_PLAN',
				title: 'Kế hoạch tháng tổng thể',
				parent: 'Quản lý kế hoạch',
				templateUrl: 'coms/totalMonthPlan/totalMonthPlan.html',
				lazyLoadFiles: [
					'coms/totalMonthPlan/tab2Function.js',
					'coms/totalMonthPlan/totalMonthPlanController.js',
					'coms/totalMonthPlan/PopupYearPlanCtrl.js',
					'coms/totalMonthPlan/totalMonthPlanService.js']

			},
			//end
			//luanlv quản lý kế hoạch tháng tổng thể
			{
				key: 'DETAIL_MONTH_PLAN',
				title: 'Kế hoạch tháng chi tiết',
				parent: 'Quản lý kế hoạch',
				templateUrl: 'coms/detailMonthPlan/detailMonthPlan.html',
				lazyLoadFiles: [
					'coms/detailMonthPlan/detailMonthPlanController.js',
					'coms/detailMonthPlan/detailMonthPlanService.js',
					'coms/detailMonthPlan/tab2Function.js',]
			},
			//Chi tiết sản lượng
			{
				key: 'RP_QUANTITY',
				title: 'Chi tiết sản lượng theo ngày',
				templateUrl: 'coms/rpQuantity/rpQuantity.html',
				lazyLoadFiles: [
					'coms/rpQuantity/rpQuantityController.js',
					'coms/rpQuantity/rpQuantityService.js',
					'coms/construction/catProvinceSearchController.js',
					]
			},
			
			{
				key: 'RP_HSHC',
				title: 'Chi tiết hoàn công theo ngày',
				templateUrl: 'coms/rpHSHC/rpHSHC.html',
				lazyLoadFiles: [
					'coms/rpHSHC/rpHSHCController.js',
					'coms/rpHSHC/rpHSHCService.js',
					'coms/construction/catProvinceSearchController.js'
					]
			},
			
			{
				key: 'RP_REVENUE',
				title: 'Chi tiết doanh thu theo ngày',
				templateUrl: 'coms/rpRevenue/rpRevenue.html',
				lazyLoadFiles: [
					'coms/rpRevenue/rpRevenueController.js',
					'coms/rpRevenue/rpRevenueService.js',
					'coms/construction/catProvinceSearchController.js'
					]
			},
			//end
			//quản lý sản lượng
			{
				key: 'WORK_ITEM',
				title: 'Quản lý sản lượng',
				templateUrl: 'coms/workItem/workItem.html',
				lazyLoadFiles: [
					'coms/workItem/workItemController.js',
					'coms/workItem/workItemService.js',
					'coms/construction/catProvinceSearchController.js']
			},
			{
				key: 'ISSUE',
				title: 'Quản lý phản ánh',
				templateUrl: 'coms/issue/issue.html',
				lazyLoadFiles: [
					'coms/issue/issueController.js',
					'coms/issue/issueService.js',
					'coms/construction/catProvinceSearchController.js']
			},
			//end
			//cấu hình đơn vị tỉnh
			{
				key: 'CONFIG_GROUP_PROVINCE',
				title: 'Cấu hình đơn vị tỉnh',
				templateUrl: 'coms/configGroupProvince/configGroupProvince.html',
				lazyLoadFiles: [
					'coms/configGroupProvince/configGroupProvinceController.js',
					'coms/configGroupProvince/configGroupProvinceService.js'
				]
			},
			//end
			//				hungnx 130618 start
			{

				key: 'INFO_CONSTRUCTION',
				title: 'Chi tiết thông tin công trình',
				templateUrl: 'coms/rpConstruction/rpConstruction.html',
				lazyLoadFiles: [
					'coms/rpConstruction/rpConstructionController.js',
					'coms/construction/constructionService.js',
					'coms/rpConstruction/contractSearchController.js',
					'coms/construction/catProvinceSearchController.js'
				]

			}, {
				key: 'QUANTITY_BY_DAY',
				title: 'Phê duyệt sản lượng theo ngày',
				templateUrl: 'coms/quantityByDay/workItem.html',
				lazyLoadFiles: [
					'coms/quantityByDay/quantityController.js',
					'coms/quantityByDay/quantityService.js',
					'coms/quantityByDay/sysUserSearchController.js',
					'coms/construction/catProvinceSearchController.js'
				]
			},
			//				hungnx 130618 end
			{
				key: 'RP_DAILY_TASK',
				title: 'Báo cáo thực hiện công việc theo ngày ',
				templateUrl: 'coms/rpDailyTask/rpDailyTask.html',
				lazyLoadFiles: [
					'coms/rpDailyTask/rpDailyTaskController.js',
					'coms/rpDailyTask/constructTypeSearchController.js',
					'coms/quantityByDay/sysUserSearchController.js',
					'coms/workItem/workItemService.js',
					'coms/construction/catProvinceSearchController.js',
					'coms/rpSumTask/catConstructionTypeSearchController.js',
					'coms/rpSumTask/comsConstructionSearchController.js',
					'coms/rpSumTask/comsWorkItemSearchController.js'
				]
			},
			{
				key: 'RP_SUM_TASK',
				title: 'Báo cáo tổng thể hạ tầng',
				templateUrl: 'coms/rpSumTask/rpSumTask.html',
				lazyLoadFiles: [
					'coms/rpSumTask/rpSumTaskController.js',
					'coms/rpDailyTask/constructTypeSearchController.js',
					'coms/quantityByDay/sysUserSearchController.js',
					'coms/workItem/workItemService.js',
					'coms/construction/catProvinceSearchController.js',
					'coms/rpSumTask/catConstructionTypeSearchController.js',
					'coms/rpSumTask/catStationSearchController.js',
					'coms/rpSumTask/comsConstructionSearchController.js',
					'coms/rpSumTask/comsWorkItemSearchController.js'
				]
			} , {
				key: 'RP_DEBT_MATERIAL',
				title: 'Báo cáo công nợ vật tư',
				templateUrl: 'coms/materialDebtReport/materialDebtReport.html',
				lazyLoadFiles: [
					'coms/materialDebtReport/materialDebtReportController.js',
					'coms/construction/catProvinceSearchController.js',
					'coms/rpSumTask/comsConstructionSearchController.js',
					'coms/quantityByDay/sysUserSearchController.js',
				]

			},
			{
				key: 'RP_BTS',
				title: 'Báo cáo BTS ',
				templateUrl: 'coms/rpBTS/rpBTS.html',
				lazyLoadFiles: [
					'coms/rpBTS/rpBTSController.js',
					'coms/rpBTS/rpBTSService.js',	
					'coms/construction/catProvinceSearchController.js',
					'coms/rpConstruction/contractSearchController.js'
				]
			},
			{
				key: 'RP_GIACO',
				title: 'Báo cáo gia cố cột ',
				templateUrl: 'coms/rpGiaCoCot/rpGiaCoCot.html',
				lazyLoadFiles: [
					'coms/rpGiaCoCot/rpGiaCoCotController.js',
					'coms/rpGiaCoCot/rpGiaCoCotService.js'
				]
			},
//			vietnt_20190116_start
			{
                key: 'REQUEST_GOODS',
                title: 'Yêu cầu sản xuất vật tư',
                templateUrl: 'coms/requestGoods/requestGoods.html',
                lazyLoadFiles: [
                    'coms/requestGoods/requestGoodsController.js',
                    'coms/requestGoods/requestGoodsService.js',
                    'coms/requestGoods/searchConstructionController.js',
                    'coms/rpSumTask/catStationSearchController.js',
					'coms/construction/catProvinceSearchController.js'
                ]
            },
			{
				key: 'Assign_Handover_TTHT',
				title: 'Giao mặt bằng cho CN/TTKV', //HuyPQ-20190315-edit
				templateUrl: 'coms/assignHandover/assignHandover.html',
				lazyLoadFiles: [
					'coms/assignHandover/assignHandoverController.js',
					'coms/assignHandover/assignHandoverService.js',
					'coms/assignHandover/findCatStationHousePopupController.js',
					'coms/rpSumTask/catStationSearchController.js',
					'coms/rpSumTask/comsConstructionSearchController.js',
					'coms/assignHandover/popupDepartmentTTKVController.js'
				]
			},
            {
                key: 'Assign_Handover_CN',
                title: 'Giao mặt bằng cho nhân viên',
                templateUrl: 'coms/assignHandover/assignHandoverNV.html',
                lazyLoadFiles: [
					'coms/assignHandover/assignHandoverNVController.js',
                    'coms/assignHandover/assignHandoverService.js',
					'coms/quantityByDay/sysUserSearchController.js',
					'coms/assignHandover/assignHandoverUserController.js',
					'coms/construction/catProvinceSearchController.js',
                    'coms/construction/workItemDetailFunction.js',
					'coms/constructionTask/constructionTaskService.js',
                    'coms/assignHandover/assignHandoverUserNVController.js'
                ]
            },
	    //			vietnt_20190116_end
			//			hungtd_20181219_start
			{
				key: 'RP_CHUANHAN_BGMB',
				title: 'Báo cáo đủ điều kiện BGMB ',
				templateUrl: 'coms/RPConstructionDK/rpConstructinonDK.html',
				lazyLoadFiles: [
					
					'coms/construction/catProvinceSearchController.js',
					'coms/workItem/workItemService.js',
					'coms/rpSumTask/rpSumTaskController.js',
					'coms/constructionTask/constructionTaskService.js',
					'coms/rpQuantity/rpQuantityService.js',
					'coms/rpConstruction/contractSearchController.js',
					'coms/RPConstructionDK/catStationHouseSearchController.js', //Huy-edit
					'coms/RPConstructionDK/rpContsructionDKController.js',
					'coms/RPConstructionDK/rpConstructionDKService.js'
				]
			},
			{
				key: 'RP_NHAN_BGMB',
				title: 'Báo cáo thông tin nhận mặt bằng ',
				templateUrl: 'coms/RP_NHAN_BGMB/rpNHANBGMB.html',
				lazyLoadFiles: [
					'coms/construction/catProvinceSearchController.js',
					'coms/workItem/workItemService.js',
					'coms/rpSumTask/rpSumTaskController.js',
					'coms/constructionTask/constructionTaskService.js',
					'coms/rpQuantity/rpQuantityService.js',
					'coms/rpConstruction/contractSearchController.js',
					'coms/RPConstructionDK/catStationHouseSearchController.js', //Huy-edit
					'coms/RP_NHAN_BGMB/rpNHANBGMBController.js',
					'coms/RP_NHAN_BGMB/rpNHANBGMBService.js'
				]
			},
			{
				key: 'RP_TON_CHUA_KC',
				title: 'Báo cáo tồn chưa khởi công',
				templateUrl: 'coms/RP_TON_CHUA_KC/rpTONCHUAKC.html',
				lazyLoadFiles: [
					'coms/construction/catProvinceSearchController.js',
					'coms/workItem/workItemService.js',
					'coms/rpSumTask/rpSumTaskController.js',
					'coms/constructionTask/constructionTaskService.js',
					'coms/rpQuantity/rpQuantityService.js',
					'coms/rpConstruction/contractSearchController.js',
					'coms/RPConstructionDK/catStationHouseSearchController.js', //Huy-edit
					'coms/RP_TON_CHUA_KC/rpTONCHUAKCController.js',
					'coms/RP_TON_CHUA_KC/rpTONCHUAKCService.js'
				]
			},{
				key: 'RP_TON_THICONG',
				title: 'Báo cáo tồn thi công',
				templateUrl: 'coms/RP_TON_THICONG/rpTONTHICONG.html',
				lazyLoadFiles: [
					'coms/construction/catProvinceSearchController.js',
					'coms/workItem/workItemService.js',
					'coms/rpSumTask/rpSumTaskController.js',
					'coms/constructionTask/constructionTaskService.js',
					'coms/rpQuantity/rpQuantityService.js',
					'coms/rpConstruction/contractSearchController.js',
					'coms/RPConstructionDK/catStationHouseSearchController.js', //Huy-edit
					'coms/RP_TON_THICONG/rpTONTHICONGController.js',
					'coms/RP_TON_THICONG/rpTONTHICONGService.js'
				]
			},
			{
				key: 'RP_TON_HSHC',
				title: 'Báo cáo tồn hồ sơ hoàn công',
				templateUrl: 'coms/RP_TON_HSHC/RP_TON_HSHC.html',
				lazyLoadFiles: [
					'coms/rpQuantity/rpQuantityService.js',
					'coms/RP_TON_HSHC/RP_TON_HSHCController.js',
					'coms/RP_TON_HSHC/RP_TON_HSHCService.js',
					'coms/RPConstructionDK/catStationHouseSearchController.js' //Huy-edit
				]
			},
//			hungtd_20181219_end
			
			
//			hungtd_20192101_start
			{
				key: 'SYN_STOCK_NOT_RECEIVE',
				title: 'Phiếu xuất kho A cấp chưa nhận',
				templateUrl: 'coms/rpCouponExport/rpCouponExportList.html',
				lazyLoadFiles: [
					'coms/rpQuantity/rpQuantityService.js',
					'coms/rpCouponExport/rpCouponExportController.js',
					'coms/rpCouponExport/populatePopupcouponController.js',
					'coms/rpCouponExport/rpCouponExportService.js'
				]
			}
//			hungtd_20192101_end
	//VietNT_20190116_start
//			, {
//                key: 'SYN_STOCK_TRANS_A',
//                title: 'Điều phối PXK A cấp',
//                templateUrl: 'coms/synStockTrans/synStockTrans.html',
//                lazyLoadFiles: [
//                    'coms/construction/covenantFunction.js',
//                    'coms/synStockTrans/synStockTransController.js',
//                    'coms/synStockTrans/synStockTransService.js'
//                ]
//            }
//			VietNT_end
//HuyPQ-start
			,{
				key: 'MANAGEMENT_ACCEPTANCE',
				title: 'Quản lý nghiệm thu',
				parent: 'Quản lý công trình',
				templateUrl: 'coms/managementAcceptance/managementAcceptance.html',
				lazyLoadFiles: [
					'coms/managementAcceptance/managementAcceptanceController.js',
					'coms/managementAcceptance/managementAcceptanceService.js',
					'coms/managementAcceptance/popupSysGroupController.js',
					'coms/construction/catProvinceSearchController.js',
					'coms/managementAcceptance/acceptanceFunction.js'
					
				]
			},
			//HuyPQ-end
			//HuyPQ-20190214-start
			{
				key: 'REPORT_CONGNO_ACAP',
				title: 'Báo cáo tổng hợp công nợ vật tư A cấp',
	//			parent: 'Quản lý công trình',
				templateUrl: 'coms/rpTongHopCongNoACap/rpCongNoACap.html',
				lazyLoadFiles: [
					'coms/rpTongHopCongNoACap/rpCongNoACapController.js',
					'coms/rpTongHopCongNoACap/rpCongNoACapService.js',
					'coms/rpTongHopCongNoACap/popupDepartmentController.js'
				]
			},
			{
				key: 'REPORT_NXT_VATTU_ACAP',
				title: 'Báo cáo Nhập-Xuất-Tồn vật tư A cấp theo từng Đơn vị/Cá nhân',
	//			parent: 'Quản lý công trình',
				templateUrl: 'coms/rpNhapXuatTonACap/rpNhapXuatTonACap.html',
				lazyLoadFiles: [
					'coms/rpNhapXuatTonACap/rpNhapXuatTonACapController.js',
					'coms/rpNhapXuatTonACap/rpNhapXuatTonACapService.js',
					'coms/rpNhapXuatTonACap/goodsSearchController.js',
					'coms/rpNhapXuatTonACap/popupDepartmentController.js'
				]
			},{
				key: 'REPORT_DOICHIEU_VATTU_XUATTHICONG',
				title: 'Biên bản đối chiếu vật tư xuất thi công',
	//			parent: 'Quản lý công trình',
				templateUrl: 'coms/rpBienBanDoiChieu4A/rpBienBanDoiChieu4A.html',
				lazyLoadFiles: [
					'coms/rpBienBanDoiChieu4A/rpBienBanDoiChieu4AController.js',
					'coms/rpBienBanDoiChieu4A/rpBienBanDoiChieu4AService.js',
					'coms/rpSumTask/comsConstructionSearchController.js'
				]
			}
			//HuyPQ-end
						//VietNT_20190129_start
            ,{
                key: 'REPORT_SUM_SYN_INPUT',
                title: 'Biên bản tổng hợp xác nhận nợ VTTB nhận của chủ đầu tư',
                templateUrl: 'coms/synStockDailyIeReport/debtConfirmGeneralReport.html',
                lazyLoadFiles: [
                    'coms/synStockDailyIeReport/synStockDailyIeReportController.js',
                    'coms/synStockDailyIeReport/synStockDailyIeReportService.js',
                    'coms/construction/catProvinceSearchController.js'
                ]
            }
            ,{
                key: 'REPORT_DETAIL_SYN_INPUT',
                title: 'Biên bản chi tiết xác nhận nợ VTTB nhận của chủ đầu tư',
                templateUrl: 'coms/synStockDailyIeReport/debtConfirmDetailReport.html',
                lazyLoadFiles: [
                    'coms/synStockDailyIeReport/synStockDailyIeReportController.js',
                    'coms/synStockDailyIeReport/synStockDailyIeReportService.js',
                    'coms/construction/catProvinceSearchController.js'
                ]
            }
            ,{
                key: 'REPORT_SYN_CONTRACT',
                title: 'Báo cáo thực hiện hợp đồng',
                templateUrl: 'coms/synStockDailyIeReport/rpContractPerformance.html',
                lazyLoadFiles: [
                    'coms/synStockDailyIeReport/rpContractPerformanceController.js',
                    'coms/synStockDailyIeReport/synStockDailyIeReportService.js',
					'coms/rpConstruction/contractSearchController.js',
                    'coms/construction/catProvinceSearchController.js'
                ]
            }
			//VietNT_end
			//VietNT_20190215_start
			, {
                key: 'REPORT_NXT_VATTU_ACAP_THEO_MA_CONG_TRINH',
                title: 'Báo cáo chi tiết nhập - xuất - tồn vật tư A cấp theo từng đơn vị/cá nhân (theo công trình)',
                templateUrl: 'coms/synStockDailyIeReport/rpDetailIERByConstruction.html',
                lazyLoadFiles: [
                    'coms/synStockDailyIeReport/rpDetailIERByConstructionController.js',
                    'coms/synStockDailyIeReport/synStockDailyIeReportService.js',
                    'coms/rpConstruction/contractSearchController.js',
                    'coms/construction/catProvinceSearchController.js'
                ]
			}
			//VietNT_end
			/**Hoangnh start 16022019**/
			,{
				key: 'QUANTITY_BY_DAY_OS',
				title: 'Phê duyệt sản lượng theo ngày ngoài OS',
				templateUrl: 'coms/quantityByDayOS/constructionTaskDaily.html',
				lazyLoadFiles: [
					'coms/quantityByDayOS/constructionTaskDailyController.js',
					'coms/quantityByDayOS/constructionTaskDailyService.js',
					'coms/quantityByDay/sysUserSearchController.js',
					'coms/construction/catProvinceSearchController.js'
				]
			},
			{
				key: 'WORK_ITEM_OS',
				title: 'Quản lý sản lượng ngoài OS',
				templateUrl: 'coms/workItemOS/workItemOS.html',
				lazyLoadFiles: [
					'coms/workItemOS/workItemOSController.js',
					'coms/workItemOS/workItemOSService.js',
					'coms/construction/catProvinceSearchController.js']
			},
			{
				key: 'TOTAL_MONTH_PLAN_OS',
				title: 'Kế hoạch tháng tổng thể ngoài OS',
				parent: 'Quản lý kế hoạch',
				templateUrl: 'coms/totalMonthPlanOS/totalMonthPlanOS.html',
				lazyLoadFiles: [
					'coms/totalMonthPlanOS/tab2Function.js',
					'coms/totalMonthPlanOS/totalMonthPlanOSController.js',
					'coms/totalMonthPlanOS/PopupYearPlanOSCtrl.js',
					'coms/totalMonthPlanOS/totalMonthPlanOSService.js']
			},
			{
				key: 'YEAR_PLAN_OS',
				title: 'Kế hoạch năm ngoài OS',
				parent: 'Quản lý kế hoạch',
				templateUrl: 'coms/yearPlanOS/yearPlanOS.html',
				lazyLoadFiles: [
					'coms/yearPlanOS/yearPlanOSController.js',
					'coms/yearPlanOS/yearPlanOSService.js',]
			},
			{
				key: 'DETAIL_MONTH_PLAN_OS',
				title: 'Kế hoạch tháng chi tiết ngoài OS',
				parent: 'Quản lý kế hoạch',
				templateUrl: 'coms/detailMonthPlanOS/detailMonthPlanOS.html',
				lazyLoadFiles: [
					'coms/detailMonthPlanOS/detailMonthPlanOSController.js',
					'coms/detailMonthPlanOS/detailMonthPlanOSService.js',
					'coms/detailMonthPlanOS/tab2Function.js',]
			},
			{
				key: 'CONTRUCTION_DT_OS',
				title: 'Quản lý doanh thu ngoài OS',
				templateUrl: 'coms/contructionDTOS/contructionDTOS.html',
				lazyLoadFiles: [
					'coms/contructionDTOS/contructionDTOSController.js',
					'coms/contructionDTOS/contructionDTOSService.js',
					'coms/construction/catProvinceSearchController.js'
				]
			},
			{
				key: 'CONTRUCTION_QL_OS',
				title: 'Quản lý quỹ lương ngoài OS',
				templateUrl: 'coms/contructionQLOS/contructionQLOS.html',
				lazyLoadFiles: [
					'coms/contructionQLOS/contructionQLOSController.js',
					'coms/contructionQLOS/contructionQLOSService.js',
					'coms/construction/catProvinceSearchController.js'
				]
			},
			{
				key: 'RP_MONTH_PROGRESS_OS',
				title: 'Tiến độ KH tháng ngoài OS',
				templateUrl: 'coms/rpMonthProgressOS/rpMonthProgressOS.html',
				lazyLoadFiles: [
					'coms/rpMonthProgressOS/rpMonthProgressOSController.js',
				]
			}
			/**Hoangnh end 16022019**/
			//HuyPQ-08042019-start
			,
			//unikom_20200609_start
			{
				key: 'RP_WO_THDT',
				title: 'Thu hồi dòng tiền',
				templateUrl: 'coms/rpWoTHDT/rpWoTHDT.html',
				lazyLoadFiles: [
					'coms/rpWoTHDT/rpWoTHDTController.js'
				]
			},
			{
				key: 'RP_MONTH_WO',
				title: 'Tiến độ KH tháng theo WO',
				templateUrl: 'coms/rpMonthWo/rpMonthWo.html',
				lazyLoadFiles: [
					'coms/rpMonthWo/rpMonthWoController.js'
				]
			},{
				key: 'RP_WO_KPI',
				title: 'Đánh giá kết quả thực hiện chỉ tiêu WO tháng',
				parent: 'Quản lý công trình > Báo cáo',
				templateUrl: 'coms/rpMarkWoKPI/rpMarkWoKpiForm.html',
				lazyLoadFiles: [
					'coms/rpMarkWoKPI/rpMarkWoKpiController.js',
					'coms/rpMarkWoKPI/rpMarkWoKpiService.js'
				]
			},
			{
				key: 'RP_WO_AIO',
				title: 'Báo cáo quản trị hợp đồng AIO',
				parent: 'Quản lý công trình > Báo cáo',
				templateUrl: 'coms/rpWoAIO/rpWoAIO.html',
				lazyLoadFiles: [
					'coms/rpWoAIO/rpWoAIOController.js'
				]
			},{
				key: 'RP_WO_HSHC_STATUS',
				title: 'Báo cáo doanh thu theo các dạng vướng',
				parent: 'Quản lý công trình > Báo cáo',
				templateUrl: 'coms/rpHSHCStatus/rpHSHCStatus.html',
				lazyLoadFiles: [
					'coms/rpHSHCStatus/rpHSHCStatusController.js'
				]
			},{
				key: 'RP_HCQT_PROVINCE',
				title: 'Báo cáo HSHC chưa lên doanh thu theo tỉnh',
				parent: 'Quản lý công trình > Báo cáo',
				templateUrl: 'coms/rp_HCQT_Province/rpHCQTProvince.html',
				lazyLoadFiles: [
					'coms/rp_HCQT_Province/rpHCQTProvinceController.js'
				]
			}
			//unikom_20200609_end
      ,{
				key: 'Assign_Handover_TTKT',
				title: 'Giao mặt bằng cho TTKT', //HuyPQ-20190315-edit
				templateUrl: 'coms/assignHandoverTTKT/assignHandoverTTKT.html',
				lazyLoadFiles: [
					'coms/assignHandoverTTKT/assignHandoverTTKTController.js',
					'coms/assignHandoverTTKT/assignHandoverTTKTService.js',
					'coms/assignHandoverTTKT/findCatStationHousePopupController.js',
					'coms/rpSumTask/catStationSearchController.js',
					'coms/rpSumTask/comsConstructionSearchController.js',
					'coms/construction/catProvinceSearchController.js',
					'coms/assignHandoverTTKT/popupSysGroupController.js',
					'coms/assignHandoverTTKT/popupDepartmentController.js'
				]
			}
			,{
				key: 'RP_DETAIL_A_WAIT_RECEIVE',
				title: 'Chi tiết phiếu xuất kho A cấp chưa nhận',
				templateUrl: 'coms/rpDetailAWaitReceive/rpDetailAWaitReceiveForm.html',
				lazyLoadFiles: [
					'coms/rpDetailAWaitReceive/rpDetailAWaitReceiveController.js',
					'coms/rpDetailAWaitReceive/rpDetailAWaitReceiveService.js',
					'coms/rpCouponExport/populateSysPXK60Controller.js'
				]
			},
			{
				key: 'rp_vt_a_cap_qua_3_thang',
				title: 'Báo cáo vật tư A cấp quá hạn 135 ngày',
				templateUrl: 'coms/rpVtACapQuaHan3Thang/rpAGoodsOutOfThreeMonthForm.html',
				lazyLoadFiles: [
					'coms/rpVtACapQuaHan3Thang/rpAGoodsOutOfThreeMonthController.js'
				]
			},
			{
				key: 'rp_ton_cong_no_vt_a_cap',
				title: 'Báo cáo tồn công nợ vật tư A cấp',
				templateUrl: 'coms/rpTonCongNoVtACap/rpDebtBalanceAGoodsForm.html',
				lazyLoadFiles: [
					'coms/rpTonCongNoVtACap/rpDebtBalanceAGoodsController.js'
				]
			}
			//HuyPq-end

			//hienvd: srtart 3/7/2019
			,{
				key: 'assign_handover_addkpi',
				title: 'Đánh giá KPI triển khai',
				templateUrl: 'coms/assignHandoverAddKPI/objectHTML.html',
				lazyLoadFiles: [
					'coms/assignHandoverAddKPI/objectController.js',
					'coms/assignHandoverAddKPI/objectControllerService.js',
				]
			}
			//hienvd: end

			//hienvd: START 25/7/2019
			,{
				key: 'SYN_PXK_7_NGAY',
				title: 'Báo cáo PXK A cấp quá hạn 7 ngày',
				templateUrl: 'coms/sysPXK/sysPXKSeven.html',
				lazyLoadFiles: [
					'coms/sysPXK/covenantSysPxkFunction.js',
					'coms/sysPXK/sysUserSearchController.js',
					'coms/rpQuantity/rpQuantityService.js',
					'coms/sysPXK/sysPXKController.js',
					'coms/sysPXK/populateSysPXKController.js',
					'coms/sysPXK/sysPXKService.js'
				]
			}
			//hienvd: END

			//hienvd: START 29/7/2019
			,{
				key: 'PLK_60',
				title: 'Báo cáo PXK chậm hoàn công 60 ngày',
				templateUrl: 'coms/sysPXK60/sysPXKSixty.html',
				lazyLoadFiles: [
					'coms/sysPXK/sysUserSearchController.js',
					'coms/sysPXK60/covenantSysPxk60Function.js',
					'coms/rpQuantity/rpQuantityService.js',
					'coms/sysPXK60/sysPXK60Controller.js',
					'coms/sysPXK60/populateSysPXK60Controller.js',
					'coms/sysPXK60/sysPXK60Service.js'
				]
			}
			//hienvd: END
			//HuyPQ-20190827-start
			,{
				key: 'RP_START_IN_MONTH',
				title: 'Báo cáo khởi công trong tháng',
				templateUrl: 'coms/reportStartInMonth/reportStartInMonthForm.html',
				lazyLoadFiles: [
					'coms/reportStartInMonth/reportStartInMonthController.js',
					'coms/reportStartInMonth/reportStartInMonthService.js'
				]
			}
			//Huy-end

			//hienvd: START 3/9/2019
			,{
				key: 'works_completed_synchronously',
				title: 'Phê duyệt công trình kết thúc đồng bộ',
				// templateUrl: 'coms/worksCompleted/reportStartInMonthForm.html',
				// lazyLoadFiles: [
				// 	'coms/worksCompleted/reportStartInMonthController.js',
				// 	'coms/worksCompleted/reportStartInMonthService.js'
				// ]
				templateUrl: 'coms/worksCompleted/completedWork.html',
				lazyLoadFiles: [
					'coms/worksCompleted/completedWorkController.js',
					'coms/worksCompleted/completedWorkService.js',
					'coms/quantityByDay/sysUserSearchController.js',
					'coms/construction/catProvinceSearchController.js',
					'erp/common/CommonService.js',
				]
			}
			//hienvd: END
			
			//Huypq-20190909-start
			,{
				key: 'ACCEPT_PXK_A_CAP',
				title: 'Quản lý PXK vật tư A cấp',
				templateUrl: 'coms/acceptPXKManage/acceptPXKManageForm.html',
				lazyLoadFiles: [
					'coms/acceptPXKManage/acceptPXKManageController.js',
					'coms/acceptPXKManage/acceptPXKManageService.js',
					'coms/acceptPXKManage/covenantFunction.js',
					'coms/acceptPXKManage/sysUserSearchController.js',
				]
			}
			//huy-end
			//Huypq-20190926-start
			,{
				key: 'KPI_LOG_PXK_A_MANAGE',
				title: 'Thống kê truy cập chức năng PXK A cấp',
				templateUrl: 'coms/rpKpiLogManage/rpKpiLogManageForm.html',
				lazyLoadFiles: [
					'coms/rpKpiLogManage/rpKpiLogManageController.js',
					'coms/rpKpiLogManage/rpKpiLogManageService.js',
					'wms/common/popupDepartmentController.js'
				]
			}
			//Huy-end
			//Huypq-20191004-start
			,{
				key: 'RP_EVALUATE_KPI_HSHC',
				title: 'Báo cáo đánh giá KPI HSHC',
				templateUrl: 'coms/rpEvaluateKpiHSHC/rpEvaluateKpiHSHCForm.html',
				lazyLoadFiles: [
					'coms/rpEvaluateKpiHSHC/rpEvaluateKpiHSHCController.js',
					'coms/rpEvaluateKpiHSHC/rpEvaluateKpiHSHCService.js',
					'wms/common/popupDepartmentController.js',
					'coms/rpConstruction/contractSearchController.js',
					'coms/construction/catProvinceSearchController.js',
					'coms/RPConstructionDK/catStationHouseSearchController.js'
				]
			}
			//Huy-end
			//Huypq-20191004-start
			,{
				key: 'CONSTRUCTION_COMPLETE_MANAGE',
				title: 'Quản lý hoàn thành công việc trong kế hoạch tháng',
				templateUrl: 'coms/constructionCompleteManage/constructionCompleteManageForm.html',
				lazyLoadFiles: [
					'coms/constructionCompleteManage/constructionCompleteManageController.js',
					'coms/constructionCompleteManage/constructionCompleteManageService.js',
					'wms/common/popupDepartmentController.js',
					'coms/rpConstruction/contractSearchController.js',
				]
			}
			//Huy-end
			//Huypq-20191004-start
			,{
				key: 'MANAGE_DATA_NOS',
				title: 'Quản lý số liệu ngoài OS ',
				templateUrl: 'coms/manageDataOS/manageDataOSForm.html',
				lazyLoadFiles: [
					'coms/manageDataOS/manageDataOSController.js',
					'coms/manageDataOS/manageDataOSService.js',
					'coms/manageDataOS/searchConstructionController.js'
				]
			}
			//Huy-end
			//Huypq-20191126-start
			,{
				key: 'REPORT_KPI_OVER_45_DAY',
				title: 'Báo cáo KPI quá hạn 45 ngày',
				templateUrl: 'coms/reportKpi45Days/reportKpi45DaysForm.html',
				lazyLoadFiles: [
					'coms/reportKpi45Days/reportKpi45DaysController.js',
					'coms/reportKpi45Days/reportKpi45DaysService.js'
				]
			}
			//Huy-end
			//tatph-start-19/12/2019
			,{
				key: 'MANAGE_VALUE',
				title: 'Quản lý giá trị thu hồi dòng tiền',
				templateUrl: 'coms/manageValue/manageValueForm.html',
				lazyLoadFiles: [
					'coms/manageValue/manageValueController.js',
					'coms/manageValue/manageValueService.js'
				]
			}
			
			,{
				key: 'MANAGE_HCQT',
				title: 'Quản lý hoàn công quyết toán công trình',
				templateUrl: 'coms/manageHcqt/manageHcqtForm.html',
				lazyLoadFiles: [
					'coms/manageHcqt/manageHcqtController.js',
					'coms/manageHcqt/manageHcqtService.js'
				]
			}
			
			,{
				key: 'MANAGE_VTTB',
				title: 'Quản lý VTTB đã nhận, đang đưa vào thi công',
				templateUrl: 'coms/manageVttb/manageVttbForm.html',
				lazyLoadFiles: [
					'coms/manageVttb/manageVttbController.js',
					'coms/manageVttb/manageVttbService.js'
				]
			}
			
//			,{
//				key: 'MANAGE_USED_MATERIAL',
//				title: 'Nghiệm thu khối lượng vật tư đã sử dụng',
//				templateUrl: 'coms/manageUsedMaterial/manageUsedMaterialForm.html',
//				lazyLoadFiles: [
//					'coms/manageUsedMaterial/manageUsedMaterialController.js',
//					'coms/manageUsedMaterial/manageUsedMaterialService.js'
//				]
//			}
			,{
				key: 'REPORT_KPI_OVER_60_DAY',
				title: 'Báo cáo KPI quá hạn 60 ngày',
				templateUrl: 'coms/reportKpi60Days/reportKpi60DaysForm.html',
				lazyLoadFiles: [
					'coms/reportKpi60Days/reportKpi60DaysController.js',
					'coms/reportKpi60Days/reportKpi60DaysService.js'
				]
			}
			,{
				key: 'REPORT_KPI_OVER_135_DAY',
				title: 'Báo cáo KPI quá hạn 135 ngày',
				templateUrl: 'coms/reportKpi135Days/reportKpi135DaysForm.html',
				lazyLoadFiles: [
					'coms/reportKpi135Days/reportKpi135DaysController.js',
					'coms/reportKpi135Days/reportKpi135DaysService.js'
				]
			}
			
			//
			,{
				key: 'RP_NO_VTAC_CUOI_KY',
				title: '	Báo cáo công nợ tồn vật tư A cấp cuối kỳ',
				templateUrl: 'coms/congNoTonVTAC/congNoTonVTACForm.html',
				lazyLoadFiles: [
					'coms/congNoTonVTAC/congNoTonVTACController.js',
					'coms/congNoTonVTAC/congNoTonVTACService.js'
				]
			}
			//
			,{
				key: 'RP_TH_VTTB_THI_CONG',
				title: 'Báo cáo tổng hợp VTTB nhận thi công	',
				templateUrl: 'coms/tongHopVTTB/tongHopVTTBForm.html',
				lazyLoadFiles: [
					'coms/tongHopVTTB/tongHopVTTBController.js',
					'coms/tongHopVTTB/tongHopVTTBService.js'
				]
			}//
			,{
				key: 'RP_TH_VTTB_RA_CT',
				title: 'Báo cáo tổng hợp PXK ra công trình trong tháng',
				templateUrl: 'coms/tongHopPXK/tongHopPXKForm.html',
				lazyLoadFiles: [
					'coms/tongHopPXK/tongHopPXKController.js',
					'coms/tongHopPXK/tongHopPXKService.js'
				]
			}
//			,{
//				key: 'LIST_RECOMMEND_CONTACT_UNIT',
//				title: '	Danh sách gợi ý tiếp xúc các đơn vị',
//				templateUrl: 'coms/recommendContactUnit/recommendContactUnitForm.html',
//				lazyLoadFiles: [
//					'coms/recommendContactUnit/recommendContactUnitController.js',
//					'coms/recommendContactUnit/recommendContactUnitService.js',
//					'coms/recommendContactUnit/addContactPopupController.js'
//				]
//			}
			
//			,{
//				key: 'LIST_RECOMMEND_CONTACT_UNIT',
//				title: 'Danh sách gợi ý tiếp xúc các đơn vị',
//				templateUrl: 'coms/recommendContactUnit/recommendContactUnitForm.html',
//				lazyLoadFiles: [
//					'coms/recommendContactUnit/recommendContactUnitController.js',
//					'coms/recommendContactUnit/recommendContactUnitService.js'
//				]
//			}
			
			,{
				key: 'CONTACT_UNIT_LIBRARY',
				title: 'Quản lý danh sách gợi ý tiếp xúc các đơn vị',
				templateUrl: 'coms/recommendContactUnitLibrary/recommendContactUnitLibraryForm.html',
				lazyLoadFiles: [
					'coms/recommendContactUnitLibrary/recommendContactUnitLibraryController.js',
					'coms/recommendContactUnitLibrary/recommendContactUnitLibraryService.js'
				]
			}
			
			
			
			//tatph-end-19/12/2019
			//Huypq-20191230-start
			,{
				key: 'PROGRESS_TASK_OS',
				title: 'Tiến độ công việc ngoài OS',
				templateUrl: 'coms/progressTaskOs/progressTaskOsForm.html',
				lazyLoadFiles: [
					'coms/progressTaskOs/progressTaskOsController.js',
					'coms/progressTaskOs/progressTaskOsService.js',
					'coms/construction/catProvinceSearchController.js'
				]
			}
			//Huy-end
			//Huypq-25052020-start
			,{
				key: 'RP_MARK_RESULT_KPI',
				title: 'Đánh giá kết quả thực hiện chỉ tiêu tháng',
				parent: 'Quản lý công trình > Báo cáo',
				templateUrl: 'coms/rpMarkResultKpi/rpMarkResultKpiForm.html',
				lazyLoadFiles: [
					'coms/rpMarkResultKpi/rpMarkResultKpiController.js',
					'coms/rpMarkResultKpi/rpMarkResultKpiService.js',
//					'coms/construction/catProvinceSearchController.js'
				]
			}
			,{
				key: 'RP_PROGRESS_MONTH_PLAN_OS',
				title: 'Báo cáo tiến độ kế hoạch tháng ngoài OS',
				parent: 'Quản lý công trình > Báo cáo',
				templateUrl: 'coms/rpProgressMonthPlanOs/rpProgressMonthPlanOsForm.html',
				lazyLoadFiles: [
					'coms/rpProgressMonthPlanOs/rpProgressMonthPlanOsController.js',
					'coms/rpProgressMonthPlanOs/rpProgressMonthPlanOsService.js',
//					'coms/construction/catProvinceSearchController.js'
				]
			},
			//Huy-end
			
			//HienLT56 start 06072020
			{
				key: 'EFFECTIVE_CALCULATE',
				title: 'Tính toán hiệu quả',
				templateUrl: 'coms/effectiveCalculate/effectiveCalculateForm.html',
				lazyLoadFiles: [
					'coms/effectiveCalculate/effectiveCalculateController.js',
					'coms/effectiveCalculate/effectiveCalculateService.js',		// phucpv edit 09072020			
					]
			}
			//HienLT56 end 06072020
			//Huy-end
			
			//Huypq-29062020-start
			,{
				key: 'DETAIL_MONTH_TTXD',
				title: 'Kế hoạch tháng chi tiết trung tâm xây dựng',
				parent: 'Quản lý kế hoạch',
				templateUrl: 'coms/detailMonthPlanTTXD/detailMonthPlanTTXD.html',
				lazyLoadFiles: [
					'coms/detailMonthPlanTTXD/detailMonthPlanTTXDController.js',
					'coms/detailMonthPlanTTXD/detailMonthPlanTTXDService.js',
					'coms/detailMonthPlanTTXD/tab2Function.js',
					]
			}
			,{
				key: 'RESULT_MONTH_PLAN_TTXD',
				title: 'Kết quả thực hiện kế hoạch tháng TTXD',
				templateUrl: 'coms/resultMonthPlanTTXD/resultMonthPlanTTXD.html',
				lazyLoadFiles: [
					'coms/resultMonthPlanTTXD/resultMonthPlanTTXDController.js',
					'coms/resultMonthPlanTTXD/resultMonthPlanTTXDService.js',
					'coms/resultMonthPlanTTXD/staffSearchController.js'
					]
			},
			//Huy-end

			//HienLT56 start 06072020
			{
				key: 'EFFECTIVE_CALCULATE',
				title: 'Tính toán hiệu quả',
				templateUrl: 'coms/effectiveCalculate/effectiveCalculateForm.html',
				lazyLoadFiles: [
					'coms/effectiveCalculate/effectiveCalculateController.js',
					'coms/effectiveCalculate/effectiveCalculateService.js',		// phucpv edit 09072020			
					]
			}
			//HienLT56 end 06072020

			//Huy-end
			
			//Huypq-21052020-start-pro
			,{
				key: 'CONFIG_STAFF_TANGENT',
				title: 'Cấu hình nhân viên tiếp xúc',
				templateUrl: 'coms/configStaffTangent/configStaffTangentForm.html',
				lazyLoadFiles: [
					'coms/configStaffTangent/configStaffTangentController.js',
					'coms/configStaffTangent/configStaffTangentService.js',
					'coms/configStaffTangent/catProvinceSearchController.js',
					'coms/configStaffTangent/staffSearchController.js'
				]
			}
			,{
				key: 'MANAGE_TANGENT_CUSTOMER',
				title: 'Quản lý yêu cầu tiếp xúc',
//				parent: 'Quản lý công trình > Tiếp xúc khách hàng',
				templateUrl: 'coms/manageTangentCustomer/manageTangentCustomerForm.html',
				lazyLoadFiles: [
					'coms/manageTangentCustomer/manageTangentCustomerController.js',
					'coms/manageTangentCustomer/manageTangentCustomerService.js',
					'coms/configStaffTangent/staffSearchController.js',
					'coms/manageTangentCustomer/contractSearchController.js',
					'coms/manageTangentCustomer/staffConfirmSearchController.js',
					
				]
			}
			//Huy-end
			//Huypq-04072020-start
			,{
				key: 'RP_GENERAL_PAYMENT_CTV',
				title: 'Báo cáo tổng hợp thanh toán CTV',
				parent: 'Quản lý công trình > Báo cáo',
				templateUrl: 'coms/rpGeneralPaymentCtv/rpGeneralPaymentCtvForm.html',
				lazyLoadFiles: [
					'coms/rpGeneralPaymentCtv/rpGeneralPaymentCtvController.js',
					'coms/rpGeneralPaymentCtv/rpGeneralPaymentCtvService.js',
					'coms/construction/catProvinceSearchController.js'
				]
			}
			,{
				key: 'RP_DETAIL_CONTRACT_CTV',
				title: 'Danh sách hợp đồng thanh toán hoa hồng CTV',
				parent: 'Quản lý công trình > Báo cáo',
				templateUrl: 'coms/rpDetailContractCtv/rpDetailContractCtvForm.html',
				lazyLoadFiles: [
					'coms/rpDetailContractCtv/rpDetailContractCtvController.js',
					'coms/rpDetailContractCtv/rpDetailContractCtvService.js',
					'coms/construction/catProvinceSearchController.js'
				]
			}
			,{
				key: 'RP_TRANFERS_CTV',
				title: 'Đề nghị thanh toán hoa hồng CTV XDDD',
				parent: 'Quản lý công trình > Báo cáo',
				templateUrl: 'coms/rpTranfersCtv/rpTranfersCtvForm.html',
				lazyLoadFiles: [
					'coms/rpTranfersCtv/rpTranfersCtvController.js',
					'coms/rpTranfersCtv/rpTranfersCtvService.js',
					'coms/construction/catProvinceSearchController.js'
				]
			}
			//Huy-end
			
			//Huypq-18072020-start
			,{
				key: 'RP_EFFECTIVE_CALCULATE',
				title: 'Báo cáo tính toán hiệu quả',
				parent: 'Quản lý công trình > Báo cáo',
				templateUrl: 'coms/rpEffectiveCalculate/rpEffectiveCalculateForm.html',
				lazyLoadFiles: [
					'coms/rpEffectiveCalculate/rpEffectiveCalculateController.js',
					'coms/rpEffectiveCalculate/rpEffectiveCalculateService.js',
					'coms/construction/catProvinceSearchController.js'
				]
			}
			//Huy-end


			// Unikom - start
			,{
				key: 'WO_XL_TR_DETAILS',
				title: 'Chi tiết TR',
				templateUrl: 'coms/wo_xl/trDetails/trDetailsForm.html',
				lazyLoadFiles: [
					'coms/wo_xl/trDetails/trDetailsController.js',
					'coms/wo_xl/trDetails/trDetailsService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
					'coms/wo_xl/woCreateNew/woCreateEditTemplateController.js',
					'coms/wo_xl/woCreateNew/woCreateNewService.js',
				]
			}

			,{
				key: 'WO_XL_WO_DETAILS',
				title: 'Chi tiết WO',
				templateUrl: 'coms/wo_xl/woDetails/woDetailsForm.html',
				lazyLoadFiles: [
					'coms/wo_xl/common/giveAssignmentModalController.js',
					'coms/wo_xl/woDetails/woDetailsController.js',
					'coms/wo_xl/woDetails/woDetailsService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
				]
			}

			,{
				key: 'WO_XL_TR_TYPE_MNGT',
				title: 'Cấu hình loại TR',
				templateUrl: 'coms/wo_xl/trTypeManagement/trTypeManagementForm.html',
				lazyLoadFiles: [
					'coms/wo_xl/trTypeManagement/trTypeManagementController.js',
					'coms/wo_xl/trTypeManagement/trTypeManagementService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
				]
			}

			,{
				key: 'WO_XL_WO_TYPE_MNGT',
				title: 'Cấu hình loại WO',
				templateUrl: 'coms/wo_xl/woTypeManagement/woTypeManagementForm.html',
				lazyLoadFiles: [
					'coms/wo_xl/woTypeManagement/woTypeManagementController.js',
					'coms/wo_xl/woTypeManagement/woTypeManagementService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
				]
			}

			,{
				key: 'WO_XL_TR_MNGT',
				title: 'Quản lý TR',
				templateUrl: 'coms/wo_xl/trManagement/trManagement.html',
				lazyLoadFiles: [
					'coms/wo_xl/trManagement/trManagementController.js',
					'coms/wo_xl/trManagement/trManagementService.js',
					'coms/wo_xl/common/vpsPermissionService.js',

				]
			}

			,{
				key: 'WO_XL_WO_MNGT',
				title: 'Quản lý WO',
				templateUrl: 'coms/wo_xl/woManagement/woManagement.html',
				lazyLoadFiles: [
					'coms/wo_xl/woCreateNew/woCreateNewService.js',
					'coms/wo_xl/woCreateNew/woCreateEditTemplateController.js',
					'coms/wo_xl/woManagement/woManagementController.js',
					'coms/wo_xl/woManagement/woManagementService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
					'coms/wo_xl/common/tcApproveRejectModalController.js',
				]
			}

			,{
				key: 'WO_XL_WO_CREATE_NEW',
				title: 'Tạo mới WO',
				templateUrl: 'coms/wo_xl/woCreateNew/woCreateNewForm.html',
				lazyLoadFiles: [
					'coms/wo_xl/woCreateNew/woCreateEditTemplateController.js',
					'coms/wo_xl/woCreateNew/woCreateNewController.js',
					'coms/wo_xl/woCreateNew/woCreateNewService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
				]
			}

			,{
				key: 'WO_XL_TR_CREATE_NEW',
				title: 'Tạo mới TR',
				templateUrl: 'coms/wo_xl/trCreateNew/trCreateNewForm.html',
				lazyLoadFiles: [
					'coms/wo_xl/trCreateNew/trCreateNewController.js',
					'coms/wo_xl/trCreateNew/trCreateNewService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
				]
			}

			,{
				key: 'WO_XL_WO_GENERAL_REPORT',
				title: 'Báo cáo tổng thể WO',
				templateUrl: 'coms/wo_xl/woGeneralReport/woGeneralReport.html',
				lazyLoadFiles: [
					'coms/wo_xl/woGeneralReport/woGeneralReportController.js',
					'coms/wo_xl/woGeneralReport/woGeneralReportService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
				]
			}

			,{
				key: 'WO_XL_WO_DETAILS_REPORT',
				title: 'Báo cáo chi tiết WO',
				templateUrl: 'coms/wo_xl/woDetailsReport/woDetailsReport.html',
				lazyLoadFiles: [
					'coms/wo_xl/woDetailsReport/woDetailsReportController.js',
					'coms/wo_xl/woDetailsReport/woDetailsReportService.js',
					'coms/construction/catProvinceSearchController.js',
					'coms/wo_xl/woCreateNew/woCreateNewService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
				]
			}

			,{
				key: 'WO_XL_WO_NAME',
				title: 'Cấu hình tên WO',
				templateUrl: 'coms/wo_xl/woNameManagement/woNameManagementForm.html',
				lazyLoadFiles: [
					'coms/wo_xl/woNameManagement/woNameManagementController.js',
					'coms/wo_xl/woNameManagement/woNameManagementService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
				]
			}

			,{
				key: 'WO_XL_HCQT_FT_REPORT',
				title: 'Báo cáo theo cá nhân HCQT',
				templateUrl: 'coms/wo_xl/woHcqtFtReport/woHcqtFtReport.html',
				lazyLoadFiles: [
					'coms/wo_xl/woHcqtFtReport/woHcqtFtReportController.js',
					'coms/wo_xl/common/vpsPermissionService.js',
				]
			}

			,{
				key: 'WO_XL_SCHEDULE_CONFIG',
				title: 'Quản lý cấu hình công việc định kỳ',
				templateUrl: 'coms/wo_xl/scheduleWOConfigManagement/scheduleWOConfig.html',
				lazyLoadFiles: [
					'coms/wo_xl/scheduleWOConfigManagement/scheduleWOConfigController.js',
					'coms/wo_xl/scheduleWOConfigManagement/scheduleWOConfigService.js',
				]
			}

			,{
				key: 'WO_XL_SCHEDULE_WORK_ITEM',
				title: 'Quản lý công việc định kỳ',
				templateUrl: 'coms/wo_xl/scheduleWorkItemManagement/scheduleWorkItemManagement.html',
				lazyLoadFiles: [
					'coms/wo_xl/scheduleWorkItemManagement/scheduleWorkItemManagementController.js',
					'coms/wo_xl/scheduleWorkItemManagement/scheduleWorkItemManagementService.js',
				]
			}

			,{
				key: 'WO_XL_SCHEDULE_WORK_ITEM_DETAILS',
				title: 'Chi tiết công việc định kỳ',
				templateUrl: 'coms/wo_xl/scheduleWorkItemDetails/scheduleWorkItemDetails.html',
				lazyLoadFiles: [
					'coms/wo_xl/scheduleWorkItemDetails/scheduleWorkItemDetailsController.js',
					'coms/wo_xl/scheduleWorkItemDetails/scheduleWorkItemDetailsService.js',
				]
			}

			,{
				key: 'WO_XL_REPORT_WO_5S',
				title: 'Báo cáo 5s',
				templateUrl: 'coms/wo_xl/rpWO5S/rpWO5S.html',
				lazyLoadFiles: [
					'coms/wo_xl/rpWO5S/rpWO5SController.js',
					'coms/wo_xl/rpWO5S/rpWO5SService.js',
				]
			}

			,{
				key: 'WO_XL_5S_CONFIG',
				title: 'Cấu hình công việc 5s',
				templateUrl: 'coms/wo_xl/wO5sConfigManagement/wO5sConfig.html',
				lazyLoadFiles: [
					'coms/wo_xl/wO5sConfigManagement/wO5sConfigController.js',
					'coms/wo_xl/wO5sConfigManagement/wO5sConfigService.js',
				]
			},
//			taotq start 22092021
			{
				key: 'REPORT_KH_BTS',
				title: 'Báo cáo KH BTS',
				templateUrl: 'coms/rpKHBTS/rpKHBTS.html',
				lazyLoadFiles: [
					'coms/rpKHBTS/rpKHBTSController.js',
					'coms/rpKHBTS/rpKHBTSService.js',
					'coms/construction/catProvinceSearchController.js'
				]
			},
			{
				key: 'REPORT_KH_BTS_BY_DA',
				title: 'Báo cáo BTS theo DA',
				templateUrl: 'coms/rpBTSByDA/rpBTSByDA.html',
				lazyLoadFiles: [
					'coms/rpBTSByDA/rpBTSByDAController.js',
					'coms/rpBTSByDA/rpBTSByDAService.js',
					'coms/construction/catProvinceSearchController.js'
				]
			}
//			taotq end 22092021
			,{
				key: 'WO_XL_HCQT_PROJECT_MANAGEMENT',
				title: 'Quản lý dự án HCQT',
				templateUrl: 'coms/wo_xl/hcqtProjectManagement/hcqtProjectManagement.html',
				lazyLoadFiles: [
					'coms/wo_xl/hcqtProjectManagement/hcqtProjectManagementController.js',
					'coms/wo_xl/hcqtProjectManagement/hcqtProjectManagementService.js',
				]
			}

			,{
				key: 'WO_XL_HCQT_WO_MNGT',
				title: 'Quản lý WO HCQT',
				templateUrl: 'coms/wo_xl/woHcqtManagement/woHcqtManagement.html',
				lazyLoadFiles: [
					'coms/wo_xl/woCreateNew/woCreateNewService.js',
					'coms/wo_xl/woCreateNew/woCreateEditTemplateController.js',
					'coms/wo_xl/woHcqtManagement/woHcqtManagementController.js',
					'coms/wo_xl/woManagement/woManagementService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
				]
			}
			,{
				key: 'WO_REPORT',
				title: 'Báo cáo tổng hợp TR-WO',
				templateUrl: 'coms/wo_xl/rpWO/rpWO.html',
				lazyLoadFiles: [
					'coms/wo_xl/rpWO/rpWOController.js',
					'coms/wo_xl/rpWO/rpWOService.js',
				]
			},
			{
				key: 'RP_WO_XDD_DTHT',
				title: 'Báo cáo tổng hợp XDD-DTHT',
				templateUrl: 'coms/wo_xl/rpWOXDDTHT/rpWOXDDTHT.html',
				lazyLoadFiles: [
					'coms/wo_xl/rpWOXDDTHT/rpWOXDDTHTController.js',
					'coms/wo_xl/rpWOXDDTHT/rpWOXDDTHTService.js',
				]
			},
			{
				key: 'WO_CONFIG_CONTRACT_COMMITTEE',
				title: 'Cấu hình ban chỉ huy công trường',
				templateUrl: 'coms/wo_xl/woConfigContract/woConfigContract.html',
				lazyLoadFiles: [
					'coms/wo_xl/woConfigContract/woConfigContractController.js',
					'coms/wo_xl/woConfigContract/woConfigContractService.js',
				]
			}
			// Unikom - end

			//Huypq-19082020-start
			,{
				key: 'MANAGE_REGISTER_CTV',
				title: 'Đăng ký tài khoản CTV',
				templateUrl: 'coms/manageRegisterCtv/manageRegisterCtvForm.html',
				lazyLoadFiles: [
					'coms/manageRegisterCtv/manageRegisterCtvController.js',
					'coms/manageRegisterCtv/manageRegisterCtvService.js',
					'coms/manageRegisterCtv/popupSysGroupCtvController.js'
				]
			}
			//Huy-end

			//Huypq-26112020-start
			,{
				key: 'RP_ORDERLY_WO',
				title: 'Báo cáo nền nếp phần mềm giao WO',
				templateUrl: 'coms/rpOrderlyWO/rpOrderlyWOForm.html',
				lazyLoadFiles: [
					'coms/rpOrderlyWO/rpOrderlyWOController.js',
					'coms/rpOrderlyWO/rpOrderlyWOService.js',
					'coms/manageRegisterCtv/popupSysGroupCtvController.js'
				]
			}
			//Huy-end
			//Huypq-24052021-start
			,{
				key: 'RP_GENERAL_CTV',
				title: 'Báo cáo tổng hợp Cộng tác viên',
				templateUrl: 'coms/report/reportGeneralCtv/reportGeneralCtvForm.html',
				lazyLoadFiles: [
					'coms/report/reportGeneralCtv/reportGeneralCtvController.js',
					'coms/report/reportGeneralCtv/reportGeneralCtvService.js',
					'coms/manageRegisterCtv/popupSysGroupCtvController.js'
				]
			},{
				key: 'RP_ZONING_CTV',
				title: 'Báo cáo quy hoạch Cộng tác viên',
				templateUrl: 'coms/report/reportZoningCtv/reportZoningCtvForm.html',
				lazyLoadFiles: [
					'coms/report/reportZoningCtv/reportZoningCtvController.js',
					'coms/report/reportZoningCtv/reportZoningCtvService.js',
					'coms/manageRegisterCtv/popupSysGroupCtvController.js'
				]
			},{
				key: 'RP_DETAIL_CTV',
				title: 'Báo cáo chi tiết Cộng tác viên',
				templateUrl: 'coms/report/reportDetailCtv/reportDetailCtvForm.html',
				lazyLoadFiles: [
					'coms/report/reportDetailCtv/reportDetailCtvController.js',
					'coms/report/reportDetailCtv/reportDetailCtvService.js',
					'coms/manageRegisterCtv/popupSysGroupCtvController.js'
				]
			},{
				key: 'RP_REVENUE_CTV',
				title: 'Báo cáo doanh thu Cộng tác viên',
				templateUrl: 'coms/report/reportRevenueCtv/reportRevenueCtvForm.html',
				lazyLoadFiles: [
					'coms/report/reportRevenueCtv/reportRevenueCtvController.js',
					'coms/report/reportRevenueCtv/reportRevenueCtvService.js',
					'coms/manageRegisterCtv/popupSysGroupCtvController.js'
				]
			}
			//Huy-end
			//Huypq-18062021-start
			,{
				key: 'RP_MASS_SEARCH_CONSTRUCTION',
				title: 'Báo cáo tổng hợp tháng BTS',
				templateUrl: 'coms/report/reportMassSearchConstruction/reportMassSearchConstructionForm.html',
				lazyLoadFiles: [
					'coms/report/reportMassSearchConstruction/reportMassSearchConstructionController.js',
					'coms/report/reportMassSearchConstruction/reportMassSearchConstructionService.js',
					'coms/manageRegisterCtv/popupSysGroupCtvController.js'
				]
			},{
				key: 'RP_RESULT_DEPLOY_BTS',
				title: 'Báo cáo chi tiết triển khai BTS',
				templateUrl: 'coms/report/reportResultDeployBts/reportResultDeployBtsForm.html',
				lazyLoadFiles: [
					'coms/report/reportResultDeployBts/reportResultDeployBtsController.js',
					'coms/report/reportResultDeployBts/reportResultDeployBtsService.js',
					'coms/report/reportResultDeployBts/reportResultDeployBtsController.js'
				]
			}
			//Huy-end
			//Huypq-07062021-start
			,{
				key: 'RP_FOLLOW_ERP_AMS_WO',
				title: 'Báo cáo theo dõi Tài chính - Tài sản - WO',
				templateUrl: 'coms/report/reportFollowErpAmsWo/reportFollowErpAmsWoForm.html',
				lazyLoadFiles: [
					'coms/report/reportFollowErpAmsWo/reportFollowErpAmsWoController.js',
					'coms/report/reportFollowErpAmsWo/reportFollowErpAmsWoService.js',
					'coms/construction/catProvinceSearchController.js',
				]
			}
			//Huy-end
			//Code os-16072021-start
			,{
		        key: 'DASHBOARD_BAO_CAO',
		        title: 'Dashboard Báo Cáo',
		        templateUrl: 'coms/wo_xl/rpDashboard/rpDashboard.html',
		        lazyLoadFiles: [
		          'coms/wo_xl/rpDashboard/rpDashboard.js',
		        ]
		      }
			//os-end
			//Huypq-07062021-start
			,{
				key: 'REPORT_DETAIL_WO_TTHQ',
				title: 'Báo cáo chi tiết WO TTHQ',
				templateUrl: 'coms/report/reportDetailWoTthq/reportDetailWoTthqForm.html',
				lazyLoadFiles: [
					'coms/report/reportDetailWoTthq/reportDetailWoTthqController.js',
					'coms/report/reportDetailWoTthq/reportDetailWoTthqService.js',
					'coms/construction/catProvinceSearchController.js',
				]
			}
			//Huy-end
			//Huypq-07062021-start
			,{
				key: 'REPORT_ACCEPT_WO_HSHC',
				title: 'Báo cáo ghi nhận WO HSHC',
				templateUrl: 'coms/report/reportAcceptWoHshc/reportAcceptWoHshcForm.html',
				lazyLoadFiles: [
					'coms/report/reportAcceptWoHshc/reportAcceptWoHshcController.js',
					'coms/report/reportAcceptWoHshc/reportAcceptWoHshcService.js',
					'coms/construction/catProvinceSearchController.js',
				]
			}
			//Huy-end
			
			//Duonghv13-16082021-start
			,{
				key: 'TOTAL_MONTH_PLAN_RENTAL_INFRASTRUCTURE',
				title: 'Kế hoạch tháng tổng thể hạ tầng cho thuê',
				parent: 'Quản lý kế hoạch',
				templateUrl: 'coms/totalMonthPlanHTCT/totalMonthPlanHTCT.html',
				lazyLoadFiles: [
					'coms/totalMonthPlanHTCT/totalMonthPlanHTCTController.js',
					'coms/totalMonthPlanHTCT/totalMonthPlanHTCTService.js',
					]
			}
			//Duong-end

			,
			//Duonghv13-14092021-start
			{
				key: 'MANAGEMENT_CAREER',
				title: 'Quản lý ngành nghề',
				templateUrl: 'coms/wo_xl/managementCareer/managementCareer.html',
				lazyLoadFiles: [
					'coms/wo_xl/managementCareer/managementCareerController.js',
					'coms/wo_xl/managementCareer/managementCareerService.js',
					'coms/wo_xl/woManagement/woManagementService.js',
				]
			}
			//Duonghv13-end
			
			,
			//Duonghv13-21092021-start
			{
				key: 'QUAN_LY_CHUNG_CHI',
				title: 'Quản lý chứng chỉ',
				templateUrl: 'coms/wo_xl/managementCertificate/managementCertificate.html',
				lazyLoadFiles: [
					'coms/wo_xl/managementCertificate/managementCertificateController.js',
					'coms/wo_xl/certificateCreateNew/certificateCreateEditTemplateController.js',
					'coms/wo_xl/common/vpsPermissionService.js',
					'coms/wo_xl/managementCertificate/managementCertificateService.js',
					'coms/wo_xl/certificateCreateNew/certificateCreateNewController.js',
					'coms/wo_xl/certificateCreateNew/certificateCreateNewService.js',
					'coms/wo_xl/woManagement/woManagementService.js',
				]
			}
			,{
				key: 'CERTIFICATE_DETAILS',
				title: 'Chi tiết Chứng chỉ',
				templateUrl: 'coms/wo_xl/certificateDetails/certificateDetailsForm.html',
				lazyLoadFiles: [
					'coms/wo_xl/certificateDetails/certificateDetailsController.js',
					'coms/wo_xl/certificateDetails/certificateExtendService.js',
					'coms/wo_xl/certificateCreateNew/certificateCreateNewController.js',
					'coms/wo_xl/certificateCreateNew/certificateCreateNewService.js',
					'coms/wo_xl/woDetails/woDetailsService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
					'coms/wo_xl/woManagement/woManagementService.js',
					'coms/wo_xl/managementCertificate/managementCertificateService.js',
				]
			}
			,{
				key: 'CERTIFICATE_CREATE_NEW',
				title: 'Tạo mới chứng chỉ',
				templateUrl: 'coms/wo_xl/certificateCreateNew/certificateCreateNewForm.html',
				lazyLoadFiles: [
					'coms/wo_xl/certificateCreateNew/certificateCreateEditTemplateController.js',
					'coms/wo_xl/certificateCreateNew/certificateCreateNewController.js',
					'coms/wo_xl/certificateCreateNew/certificateCreateNewService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
				]
			},
			//Duonghv13-14012022-start
			{
				key: 'HANDLE_COMPLAIN_MANAGEMENT',
				title: 'Quản lý yêu cầu xử lý sự cố/khiếu nại ',
				templateUrl: 'coms/handleComplain/surveyRequest.html',
				lazyLoadFiles: [
					'coms/handleComplain/surveyRequestController.js',
					'coms/handleComplain/surveyRequestService.js',
					'coms/handleComplain/surveyRequestDetailService.js',
				]
			},
			//Duonghv13-end
			//Huypq-08012022-start
			,{
				key: 'REPORT_RESULT_PERFORM',
				title: 'Báo cáo BTS năm HTCT',
				templateUrl: 'coms/report/reportResultPerform/reportResultPerformForm.html',
				lazyLoadFiles: [
					'coms/report/reportResultPerform/reportResultPerformController.js',
					'coms/report/reportResultPerform/reportResultPerformService.js',
					'coms/construction/catProvinceSearchController.js'
				]
			}
			//Huy-end
			//Duonghv13-10032022-start
			,{
				key: 'PROVINCE_ELECTRICAL',
				title: 'Quản lý cơ điện',
				templateUrl: 'coms/manageME/manageMEList.html',
				lazyLoadFiles: [
					'coms/manageME/manageMEController.js',
					'coms/manageME/manageMEService.js',
					'coms/manageME/catProvinceSearchController.js',
					'coms/wo_xl/common/vpsPermissionService.js',
				]
			},
			{
				key: 'STATION_ELECTRICAL_DETAIL',
				title: 'Chi tiết trạm cơ điện',
				templateUrl: 'coms/manageME/stationDetail/stationDetailForm.html',
				lazyLoadFiles: [
					'coms/manageME/stationDetail/stationDetailController.js',
					'coms/manageME/stationDetail/stationDetailService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
					'coms/manageME/manageMEService.js',
				]
			},
			{
				key: 'EQUIPMENT_DETAIL',
				title: 'Chi tiết thiết bị',
				templateUrl: 'coms/manageME/stationDetail/equipment/EquipmentForm.html',
				lazyLoadFiles: [
					'coms/manageME/stationDetail/equipment/equipmentController.js',
					'coms/manageME/stationDetail/stationDetailService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
				]
			},
			{
				key: 'PROVINCE_ELECTRICAL_DASHBOARD',
				title: 'Dashboard',
				templateUrl: 'coms/report/reportME/manageMEList.html',
				lazyLoadFiles: [
					'coms/report/reportME/stationDetailController.js',
					'coms/report/reportME/stationDetailService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
				]
			},
			//Duonghv13-end
			{
				key: 'MANAGE_TANGENT_CUSTOMER_GPTH',
				title: 'Quản lý yêu cầu tiếp xúc GPTH',
				templateUrl: 'coms/manageTangentCustomerGPTH/manageTangentCustomerFormGPTH.html',
				lazyLoadFiles: [
					'coms/manageTangentCustomerGPTH/manageTangentCustomerGPTHController.js',
					'coms/manageTangentCustomerGPTH/manageTangentCustomerGPTHService.js',
					'coms/configStaffTangent/staffSearchController.js',
					'coms/manageTangentCustomerGPTH/contractSearchController.js',
					'coms/manageTangentCustomerGPTH/staffConfirmSearchController.js',
					
				]
			},
			{
				key: 'STATION_CONSTRUCTION_REPORT',
				title: 'Báo cáo danh mục nhà trạm - công trình',
//				templateUrl: 'coms/rpStationConstruction/rpStationConstruction.html',
				templateUrl: 'coms/rpStationConstruction/rpKHBTS.html',
				lazyLoadFiles: [
					'coms/rpStationConstruction/rpKHBTSController.js',
					'coms/rpStationConstruction/rpKHBTSService.js',
					'coms/construction/catProvinceSearchController.js'
				]
			},
			// ducpm
			{
				key : 'HISTORY_UPDATE_ELECTRIC',
				title : 'Lịch sử sửa chữa',
				templateUrl : 'coms/historyUpdateElectric/historyElectric.html',
				lazyLoadFiles : [ 'coms/historyUpdateElectric/historyElectricController.js',
						'coms/historyUpdateElectric/historyElectricService.js',
						'coms/wo_xl/common/vpsPermissionService.js', ]
			},				
			{
				key : 'MANAGER',
				title : 'Quản lý',
				templateUrl : 'coms/manager/manager.html',
				lazyLoadFiles : [ 'coms/manager/managerController.js',
						'coms/manager/managerService.js',
						'coms/wo_xl/common/vpsPermissionService.js', ]
			},
			{
				key : 'ELECTRICAL_HISTORY_CHANGE',
				title : 'Lịch sử thay đổi',
				templateUrl : 'coms/electricHistoryChange/electricHistoryChange.html',
				lazyLoadFiles : [ 'coms/electricHistoryChange/electricHistoryChangeController.js',
						'coms/electricHistoryChange/electricHistoryChangeService.js',
						'coms/wo_xl/common/vpsPermissionService.js', ]
			},
			{
				key : 'MANAGER_CATEGORY',
				title : 'Quản lý danh mục',
				templateUrl : 'coms/managerCategory/managerCategory.html',
				lazyLoadFiles : [ 'coms/managerCategory/managerCategoryController.js',
						'coms/managerCategory/managerCategoryService.js',
						'coms/wo_xl/common/vpsPermissionService.js', ]
			},
			
			// ducpm-end
			// taotq start 19072022
			{
				key: 'INTERNAL_SALES_EXPENSE_REPORT_XDDD',
				title: 'Báo cáo chi phí bán hàng nội bộ XDDD',
				parent: 'Quản lý công trình > Báo cáo',
				templateUrl: 'coms/reportCostOfSales/reportCostOfSalesForm.html',
				lazyLoadFiles: [
					'coms/reportCostOfSales/reportCostOfSalesController.js',
					'coms/reportCostOfSales/reportCostOfSalesService.js',
//					'searchPopup/catProvinceSearchController.js'
				]
			}
			,
			// taotq end 19072022
			{
				key: 'ESTIMATION_DESIGN',
				title: 'Thẩm thiết kế dự toán',
				templateUrl: 'coms/wo_xl/designEstimates/designEstimates.html',
				lazyLoadFiles: [
					'coms/wo_xl/designEstimates/designEstimatesController.js',
					'coms/wo_xl/designEstimates/designEstimatesService.js',
					'coms/wo_xl/common/vpsPermissionService.js',
				]
			},
		];

		// Unikom them: cac loai state cua wo_xl
		config.WO_XL_STATE = {
			unassign: {
				stateCode: 'UNASSIGN',
				stateText: 'Mới tạo'
			},
			assignCd: {
				stateCode: 'ASSIGN_CD',
				stateText: 'Chờ CD tiếp nhận'
			},
			acceptCd:{
				stateCode: 'ACCEPT_CD',
				stateText: 'CD đã tiếp nhận'
			},
			rejectCd:{
				stateCode: 'REJECT_CD',
				stateText: 'CD từ chối'
			},
			assignFt:{
				stateCode: 'ASSIGN_FT',
				stateText: 'Chờ FT tiếp nhận'
			},
			acceptFt:{
				stateCode: 'ACCEPT_FT',
				stateText: 'FT đã tiếp nhận'
			},
			rejectFt:{
				stateCode: 'REJECT_FT',
				stateText: 'FT từ chối'
			},
			processing:{
				stateCode: 'PROCESSING',
				stateText: 'Đang thực hiện'
			},
			done:{
				stateCode: 'DONE',
				stateText: 'Đã thực hiện'
			},
			ok:{
				stateCode: 'OK',
				stateText: 'Hoàn thành'
			},
			ng:{
				stateCode: 'NG',
				stateText: 'Chưa hoàn thành'
			},
			opinionRq1:{
				stateCode: 'OPINION_RQ_1',
				stateText: 'Xin ý kiến CD Level 1'
			},
			opinionRq2:{
				stateCode: 'OPINION_RQ_2',
				stateText: 'Xin ý kiến CD Level 2'
			},
			opinionRq3:{
				stateCode: 'OPINION_RQ_3',
				stateText: 'Xin ý kiến CD Level 3'
			},
			opinionRq4:{
				stateCode: 'OPINION_RQ_4',
				stateText: 'Xin ý kiến CD Level 4'
			},
			cdOk:{
				stateCode: 'CD_OK',
				stateText: 'Điều phối duyệt OK'
			},
			cdNg:{
				stateCode: 'CD_NG',
				stateText: 'Điều phối duyệt chưa OK'
			},
			waitTcBranch:{
				stateCode: 'WAIT_TC_BRANCH',
				stateText: 'Chờ TC trụ duyệt'
			},
			waitTcTct:{
				stateCode: 'WAIT_TC_TCT',
				stateText: 'Chờ TC TCT duyệt'
			},
			tcBranchRejected:{
				stateCode: 'TC_BRANCH_REJECTED',
				stateText: 'TC trụ từ chối'
			},
			tcTctRejected:{
				stateCode: 'TC_TCT_REJECTED',
				stateText: 'TC TCT từ chối'
			},
			waitPqt:{
				stateCode: 'WAIT_PQT',
				stateText: 'Chờ Phòng quyết toán TTHT nhận'
			},
			waitTtDtht:{
				stateCode: 'WAIT_TTDTHT',
				stateText: 'Chờ TT.ĐTHT nhận'
			},
			receivedPqt:{
				stateCode: 'RECEIVED_PQT',
				stateText: 'Phòng Quyết toán TTHT đã nhận'
			},
			pqtNg:{
				stateCode: 'PQT_NG',
				stateText: 'Phòng Quyết toán TTHT từ chối bản cứng'
			},
			receivedTtDtht:{
				stateCode: 'RECEIVED_TTDTHT',
				stateText: 'TT ĐTHT đã nhận'
			},
			ttDthtNg:{
				stateCode: 'TTDTHT_NG',
				stateText: 'TTĐTHT từ chối bản cứng'
			},
			rejectPqt:{
				stateCode: 'REJECT_PQT',
				stateText: 'Phòng Quyết toán TTHT từ chối sản lượng'
			},
			rejectTtDtht:{
				stateCode: 'REJECT_TTDTHT',
				stateText: 'TT.ĐTHT từ chối sản lượng'
			},
			pause:{
				stateCode: 'PAUSE',
				stateText: 'Đề xuất gia hạn/hủy'
			},
			cdPauseGh:{
				stateCode: 'CD_PAUSE-GH',
				stateText: 'Điều phối duyệt đề xuất gia hạn'
			},
			tthtPauseGh:{
				stateCode: 'TTHT_PAUSE-GH',
				stateText: 'TTHT duyệt đề xuất gia hạn'
			},
			dthtPauseGh:{
				stateCode: 'DTHT_PAUSE-GH',
				stateText: 'TT ĐTHT duyệt đề xuất gia hạn'
			},
			cdPauseRejectGh:{
				stateCode: 'CD_PAUSE_REJECT-GH',
				stateText: 'Điều phối từ chối đề xuất gia hạn'
			},
			tthtPauseRejectGh:{
				stateCode: 'TTHT_PAUSE_REJECT-GH',
				stateText: 'TTHT từ chối đề xuất gia hạn'
			},
			dthtPauseRejectGh:{
				stateCode: 'DTHT_PAUSE_REJECT-GH',
				stateText: 'TT ĐTHT từ chối đề xuất gia hạn'
			},
			cdPauseHuy:{
				stateCode: 'CD_PAUSE-HUY',
				stateText: 'Điều phối duyệt đề xuất hủy'
			},
			tthtPauseHuy:{
				stateCode: 'TTHT_PAUSE-HUY',
				stateText: 'TTHT duyệt đề xuất hủy'
			},
			dthtPauseHuy:{
				stateCode: 'DTHT_PAUSE-HUY',
				stateText: 'TT ĐTHT duyệt đề xuất hủy'
			},
			cdPauseRejectHuy:{
				stateCode: 'CD_PAUSE_REJECT-HUY',
				stateText: 'Điều phối từ chối đề xuất hủy'
			},
			tthtPauseRejectHuy:{
				stateCode: 'TTHT_PAUSE_REJECT-HUY',
				stateText: 'TTHT từ chối đề xuất hủy'
			},
			dthtPauseRejectHuy:{
				stateCode: 'DTHT_PAUSE_REJECT-HUY',
				stateText: 'TT ĐTHT từ chối đề xuất hủy'
			},
		};


		config.WO_TR_XL_STATE = {
			unassign: {
				stateCode: 'UNASSIGN',
				stateText: 'Mới tạo'
			},
			assignCd: {
				stateCode: 'ASSIGN_CD',
				stateText: 'Chờ CD tiếp nhận'
			},
			acceptCd: {
				stateCode: 'ACCEPT_CD',
				stateText: 'CD đã tiếp nhận'
			},
			rejectCd: {
				stateCode: 'REJECT_CD',
				stateText: 'CD đã từ chối'
			},
			done:{
				stateCode: 'DONE',
				stateText: 'Đã thực hiện'
			},
			ok:{
				stateCode: 'OK',
				stateText: 'Hoàn thành'
			},
			nOk:{
				stateCode: 'NOK',
				stateText: 'Chưa hoàn thành'
			},
			opinionRq1:{
				stateCode: 'OPINION_RQ',
				stateText: 'Xin ý kiến CD'
			},
			processing:{
				stateCode: 'PROCESSING',
				stateText: 'Đang thực hiện'
			},
			assignFt:{
				stateCode: 'ASSIGN_FT',
				stateText: 'Chờ FT tiếp nhận'
			},
			rejectFt:{
				stateCode: 'REJECT_FT',
				stateText: 'FT từ chối'
			},
		}

		config.PERMISSIONS = {
			VIEW_WOXL: 'VIEW WOXL',
			CREATE_WOXL: 'CREATE WOXL',
			UPDATE_WOXL: 'UPDATE WOXL',
			DELETE_WOXL: 'DELETE WOXL',
			CD_WOXL: 'CD WOXL',
			VIEW_WOXL_TR: 'VIEW WOXL_TR',
			CREATE_WOXL_TR: 'CREATE WOXL_TR',
			UPDATE_WOXL_TR: 'UPDATE WOXL_TR',
			DELETE_WOXL_TR: 'DELETE WOXL_TR',
			CD_WOXL_TR: 'CD WOXL_TR',
			CRUD_CNKT_TRXL: 'CRUD CNKT_TRXL',
			CRUD_CNKT_WOXL: 'CRUD CNKT_WOXL',
			CREATE_WO_HCQT: 'CREATE WO_HCQT',
			UPDATE_WO_HCQT: 'UPDATE WO_HCQT',
			VIEW_WO_HCQT: 'VIEW WO_HCQT',
			APPROVED_REVENUE_SALARY: 'APPROVED REVENUE_SALARY',
			APPROVED_TC_TCT: 'APPROVED TC_TCT',
			APPROVED_TC_BRANCH: 'APPROVED TC_BRANCH',
			UPDATE_WOXL_5S: 'UPDATE WOXL_5S',
			CREATE_WO_DOANHTHU: 'CREATE WOXL_DOANHTHU',
			APPROVED_OVERDUE_REASON: 'APPROVED OVERDUE_REASON',
			CREATED_WO_UCTT: 'CREATED WO_UCTT',
			CREATED_WO_HSHC: 'CREATED WO_HSHC',
			EDIT_ADMIN_WO: 'EDIT_ADMIN_WO', //HienLT5 add 29052021
			//Huypq-01112021-start
			APPROVED_HTCT_HSHC: 'APPROVED HTCT_HSHC',
			APPROVE_DTHT_HTCT_HSHC: 'APPROVE_DTHT HTCT_HSHC',
			APPROVED_DEVICE_ELECTRICT_CNKT:'APPROVED DEVICE_ELECTRICT_CNKT',
			APPROVED_DEVICE_ELECTRICT:'APPROVED DEVICE_ELECTRICT'	
			//Huy-end
			
		}

		// Unikom - end

		//duonghv13 -start 23092021
		

		config.getTemplateUrl = function (key) {
			for (var i in config.TEMPLATE_URL) {
				if (config.TEMPLATE_URL[i].key == key) {
					return config.TEMPLATE_URL[i];
				}
			}

			return null;
		}

		return config;
	}
	angular.module('MetronicApp').constant('PopupConst', {

	});

	angular.module('MetronicApp').constant('AppConst', {
		AR_INVOICE: {
			Invoice_Table_ID: 1000059,
			Tax_Account_ID: 1000027
		},
		AR_DEPOSITE_BROWSER: {
			document_type_id: 'D00001'
		},
		AR_REVALUATION: {
			Document_Type_Id: 17,
			Status: 'DR'
		},
		C_CONTIGENCY_SALE: {
			Status: 'DR',
			CurrencyName: '1000046'
		}
	});

})();
