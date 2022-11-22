package com.viettel.utils;

import java.time.LocalTime;
import java.util.*;

public class Constant {
    public static class OperationKey {

        public static String CREATE = "CREATE";
        public static String ESTIMATE = "ESTIMATE";
        public static String VALUE = "VALUE";
        public static String VIEW = "VIEW";
        public static String CONFIG = "CONFIG";
        public static String REPORT = "REPORT";
        public static String APPROVED = "APPROVED";
        public static String UNDO = "UNDO";
        public static String RECEIVE = "RECEIVE";
        public static String CRUD = "CRUD"; // picasso them moi

        public static final String PROCESS = "PROCESS";

		public static final String CREATED = "CREATED";// duonghv13 -them moi 25082021
		public static final String CD = "CD";

        //		public static final String CREATE="CREATE";
//		public static final String ESTIMATE="ESTIMATE";
//		public static final String VALUE="VALUE";
//		public static final String VIEW="VIEW";
//		public static final String CONFIG="CONFIG";
//		public static final String REPORT="REPORT";
//		hungnx 20180629 start
        public static String VIEW_QUANTITY_DAILY = "VIEW_QUANTITY_DAILY";
//		hungnx 20180629 end
        
        public static String MANAGE = "MANAGE";

        //VietNT_20181220_start
        public static String ASSIGN = "ASSIGN";
        //VietNT_end
		   //VietNT_20190105_start
        public static String REQUEST = "REQUEST";
        //VietNT_end
        //Huypq-20191112-start
        public static String RULE = "RULE";
        public static String UPDATE = "UPDATE";
        
        //Huy-end
        public static String MAP = "MAP";
        public static String EDIT = "EDIT"; //HienLT56 add 27052021
        public static String DELETE = "DELETE";//HienLT56 add 27052021
        public static String APPROVE_DTHT = "APPROVE_DTHT";
        //Duonghv13 add 08022022
        public static String APPROVE = "APPROVE"; 
        public static String DEPLOY = "DEPLOY";
        //Duonghv13 end 08022022
    }

    public static class AdResourceKey {
        public static final String PKTCNKT = "PKTCNKT";
		public static String FEEDBACK = "FEEDBACK";
        public static String STOCK_TRANS = "IE_TRANSACTION";
        public static String CHANGE_ORDER = "CHANGE_ORDER";
        public static String SHIPMENT = "SHIPMENT";
        public static String STOCK = "STOCK";
        public static String MONTHLY_DETAIL_PLAN = "MONTHLY_DETAIL_PLAN";
        public static String DATA = "DATA";
        public static String WORK_PROGRESS = "WORK_PROGRESS";
//        hoanm1_20180905_start
        public static String WORK_PROGRESS_TC = "WORK_PROGRESS_TC";
        public static String WORK_PROGRESS_HSHC = "WORK_PROGRESS_HSHC";
//        hoanm1_20180905_end
        //		chinhpxn_20180630_start
        public static String TASK = "TASK";
//		chinhpxn_20180630_end

        public static String CONSTRUCTION_PRICE = "CONSTRUCTION_PRICE";
//        hungtd_2018121_start
        public static String COMPLETE_CONSTRUCTION = "COMPLETE_CONSTRUCTION";
//        	hungtd_2018121_end
        public static String CONSTRUCTION = "CONSTRUCTION";
        public static String TOTAL_MONTH_PLAN = "TOTAL_MONTH_PLAN";
        public static String EQUIPMENT_RETURN = "EQUIPMENT_RETURN";
        public static String LAND_HANDOVER_PLAN = "LAND_HANDOVER_PLAN";
        //		hungnx 20180629 start
        public static String VIEW_QUANTITY_DAILY = "QUANTITY_DAILY";
//		hungnx 20180629 end
        
        public static String PLAN = "PLAN";
		//VietNT_20190105_start
        public static String GOODS = "GOODS";
        //VietNT_end
        //HuyPQ-start
        public static String ACCEPTANCE = "ACCEPTANCE";
        public static String TTKT = "TTKT";
        public static String TTKT_DV = "TTKT_DV";
        public static String SMS_HANDOVER = "SMS_HANDOVER";
        //HuyPQ-end
        
        //Huypq-20191112-start
        public static String CNKT_OS = "CNKT_OS";
        public static String TTHT="TTHT";
        public static String WI_HTCT="WI_HTCT";
        public static String TANGENT_CUSTOMER="TANGENT_CUSTOMER";
        public static String REVENUE_SALARY="REVENUE_SALARY";
        //Huy-end
        //Unikom kiem tra hang muc
        public static String WORK_ITEM = "WORK_ITEM";
        public static String WOXL = "WOXL";
        public static String WOXL_TR = "WOXL_TR";
        public static String WOXL_TR_GPTH = "WOXL_TR_GPTH";
        public static String WOXL_TR_XDDTHT = "WOXL_TR_XDDTHT";
        public static String CNKT_WOXL = "CNKT_WOXL";
        public static String CNKT_WOXL_TR = "CNKT_WOXL_TR";
        public static String WO_HCQT = "WO_HCQT";
        public static String TC_BRANCH = "TC_BRANCH";
        public static String TC_TCT = "TC_TCT";
        public static String WOXL_DOANHTHU = "WOXL_DOANHTHU";
        
        //Huypq-26082020-start
        public static String WOXL_CHIPHI = "WOXL_CHIPHI";
        //Huy-end
        public static String WOXL_XDDTHT= "9006003";
        public static String SYSTEM_SOLAR = "SYSTEM_SOLAR";
        public static String ADMIN_WO="ADMIN_WO"; //HienLT56 add 27052021

        public static String CONSTRUCTION_ADMIN="CONSTRUCTION_ADMIN";
		
		public static final String MONTH_PLAN_HTCT = "MONTH_PLAN_HTCT"; // duonghv13 -them moi 25082021
        public static String WOXL_UCTTROLE="274966"; //DUONGHV13 add 08102021

		public static String WO_UCTT= "WO_UCTT"; //Duonghv13 add 01102021

		//Huypq-22102021-start
		public static final String HTCT_HSHC = "HTCT_HSHC";
		//Huy-end
		
		//Duonghv13 start 08022022
		public static String BUSINESS_TARGET = "BUSINESS_TARGET"; 
		public static String YCTX = "YCTX";
		//Duonghv13 add 08022022
		public static String DEVICE_ELECTRICT = "DEVICE_ELECTRICT"; 
		public static String DEVICE_ELECTRICT_CNKT = "DEVICE_ELECTRICT_CNKT";
        public static String BROCKEN_ELECTRIC="BROCKEN_ELECTRIC";
        public static final String CD_LEVEL5="CD_LEVEL5";
        
        public static String CONSTRUCTION_XDDD="CONSTRUCTION_XDDD";
        
    }
    
    public static final Map<String, String> TANGENT_STATUS;
	static {
		TANGENT_STATUS = new HashMap<String, String>();
		TANGENT_STATUS.put("0", "Đã hủy");
		TANGENT_STATUS.put("1", "Chờ tiếp nhận");
		TANGENT_STATUS.put("2", "Chờ tiếp xúc");
		TANGENT_STATUS.put("3", "Chờ trình bày giải pháp");
		TANGENT_STATUS.put("4", "Không thành công");
		TANGENT_STATUS.put("5", "Chờ ký hợp đồng");
		TANGENT_STATUS.put("6", "Bổ sung / chỉnh sửa giải pháp");
		TANGENT_STATUS.put("7", "Không thành công");
		TANGENT_STATUS.put("8", "Hoàn thành tiếp xúc");
		TANGENT_STATUS.put("9", "Không thành công"); 
		TANGENT_STATUS.put("10", "Hoàn thành thanh toán hoa hồng"); 
		TANGENT_STATUS.put("11", "Không thành công");
	}
	
	public static final Map<String, String> BRANCHS;
	static {
		BRANCHS = new HashMap<String, String>();
		BRANCHS.put("GPTH", "280483");
		BRANCHS.put("TTHT", "242656");
		BRANCHS.put("ĐTHT", "166677");
		BRANCHS.put("VHKT", "270120");
		BRANCHS.put("CNTT", "280501");
		BRANCHS.put("XDDD", "9006003");
	}
	
	public static class DeviceType {
		public static String LUOI_DIEN="LUOI_DIEN";
		public static String TU_NGUON_AC="TU_NGUON_AC";
		public static String TU_NGUON_DC="TU_NGUON_DC";
		public static String NHIET="NHIET";
		public static String DIEU_HOA_AC="DIEU_HOA_AC";
		public static String THONG_GIO="THONG_GIO";
		public static String DIEU_HOA_DC="DIEU_HOA_DC";
		public static String MAY_PHAT="MAY_PHAT";
		public static String AC_QUY="AC_QUY";
		public static String CUU_HOA="CUU_HOA";
		public static String CANH_BAO="CANH_BAO";
		public static String ATS="ATS";
		public static String MAY_NO="MAY_NO";
		public static String LOC_SET="LOC_SET";
		public static String TIEP_DIA="TIEP_DIA";
		public static String RECTIFITER="RECTIFITER";
		public static String NHA_TRAM="NHA_TRAM";

	}

    public static class CatWorkItemTypeId {
//        public static Long KHOI_CONG = 2563l;
        public static Long KHOI_CONG = 1983l;
    }

    public static class WOTypeCode {
	    public static String THI_CONG = "THICONG";
    }

    public static class ContactResult {
	    public static Long THANH_CONG = 1l;
	    public static Long KHONG_THANH_CONG = 0l;
    }

    public static class OrderResultTangent {
        public static String THANH_CONG = "1";
        public static String KHONG_THANH_CONG = "0";
	}

    public static class DiffLocation {
        public static Long SAME = 1l;
        public static Long DIFF = 2l;
    }

	public static final Map<String, String> MESSAGE_TICKET;
	static {
		MESSAGE_TICKET = new HashMap<String, String>();
		MESSAGE_TICKET.put("Sai định dạng", "Dữ liệu không đúng định dạng/ không hợp lệ/ quá kích thước,...");
		MESSAGE_TICKET.put("Ticket Đã hết hạn", "Ticket này đã hết hạn");
		MESSAGE_TICKET.put("completedTimeExpected Bigger Than StartDate And EndDate", "Ngày hoàn thành dự kiến lớn hơn ngày bắt đầu và ngày kết thúc");
		MESSAGE_TICKET.put("completedTimeExpected Format Error", "Ngày hoàn thành dự kiến sai định dạng");
		MESSAGE_TICKET.put("Trạng thái không đúng", "Trạng thái ticket truyền ra không đúng hoặc bất hợp lệ");
		MESSAGE_TICKET.put("Error Update Ticket", "CRM cập nhật ticket thất bại");
		MESSAGE_TICKET.put("Ticket not Exist", "Không tìm thấy dữ liệu ticket trên hệ thống CRM");
		MESSAGE_TICKET.put("Method not Found", "Không tìm thấy server");
	}

    public static class PARTNER_TYPE {
        public static final Long B2C =1L ;// cá nhân
        public static final Long B2B = 2L;// doanh nghiệp

        public static final String KHCN = "KHCN";

        public static final String KHDN = "KHDN";
    }

    public static class STATUS {
        public static final Long ACTIVE =1L ;
        public static final Long INACTIVE = 2L;

    }
    public static class TIME {
    public static final LocalTime localTime18h = LocalTime.of(18, 00, 00);
    }

    public static class VALIDATE_NAME {
        public static final String BROTHER ="anh" ;
        public static final String SISTER = "chị";
    }

    public static class SEX {
        public static final Long MALE =1L ;
        public static final Long FEMALE = 2L;
    }
    public static class STATUS_TANGENT_CUSTOMER {
        public static final String CANCEL ="0" ;
        public static final String WAITING_FOR_RECEIVED = "1";
        public static final String WAITING_FOR_CONTACTS = "2";
        public static final String UNSUCCESSFUL = "4";
        public static final String WAITING_FOR_PRESENTATION_OF_SOLUTION = "3";
        public static final String WAITING_FOR_CONTRACT = "5";
        public static final String CLOSE = "5";
        public static final String WAITING_FOR_RECONTACTS = "15";

        public static final String WATING_TTHT_APROVED = "12";

        public static final String TTHT_REJECT_INFO = "13";

        public static final String TTHT_REJECT_CUSTOMER_INFORMATION= "13";
        public static final String TANGENT_SUCCESS = "8";
    }

    public static class APPROVE_STATUS {
        public static final String APPROVE = "1";
        public static final String REJECT = "2";
        public static final String RECONTACTS = "3";
    }

    public static class SYS_GROUP_ID {
        public static final Long TTHT =242656L ;
    }

    public static class CUSTOMER_IDENTIFICATION_TYPE {
        public static final Long TAX_CODE =1L ;
        public static final Long CCCD = 2L;
        public static final Long CMND = 3L;

        public static final String CMND_TYPE = "CMND";
        public static final String CCCD_TYPE = "CCCD";
        public static final String MST_TYPE = "MST";
    }

    public static class GROUP_ORDER {
        public static final Long COMPANY =1L ;
        public static final Long CENTER = 2L;
        public static final Long CSKH = 3L;
        public static final Long CNKT = 4L;
        public static final Long CTV = 5L;
    }

    public static class CUSTOMER_RESOURCES {
        public static final String  A_RELATIVE ="người thân,người quen" ;

    }
    public static class PHONE_NUMBER {
        public static final String  VIETNAM ="+84" ;

    }

    public static class TYPE_LONG {
        public static final Long ONE =1L ;
    }

    public static class SOURCE {
        public static final String INTERNAL ="1" ;
        public static final String SOCIAL ="2" ;
        public static final String CUSTOMER_SERVICE ="3" ;
    }

    public static class FILE_TYPE {
        public static final String  ARCHITECTURE_DESIGN_FILE= "GP" ; // thiết kế kiến trúc
        public static final String ESTIMATE_DESIGN_FILE ="ED" ; //thiết kế dự toán
        public static final String TEXTURE_FORMAT_FILE ="TF" ; //định dạng kết cấu
        public static final String PERSPECTIVE_3D_FILE ="P3" ; //file phối cảnh 3d
        public static final String ATTACHED_FILE ="AF" ; //file đính kèm
        public static final String ALL_TYPE ="'GP','ED','TF','P3','AF'" ; //file đính kèm
    }
    public static class REJECT_RESULT_TANGENT {

        public static final List<String> LIST_CAUSE_BY_CUSTOMER = Arrays.asList("Tham khảo giá", "Không đồng ý gặp trực tiếp",
                "Không nghe máy", "Vướng thủ tục đất đai", "Chưa xây ngay", "Chưa đủ kinh phí", "Đã tìm được đối tác khác",
                "Yêu cầu đổi người tiếp xúc khác");

        public static final List<String> LIST_CAUSE_BY_VCC = Arrays.asList("Không đảm bảo lợi nhuận cho tổng công ty",
                "Tiếp xúc chậm so với lịch hẹn", "Không có nhân sự phụ trách", "Không tìm kiếm được tổ đội thi công",
                "Khách hàng chỉ cần cung cấp nhân công", "Khác");
    }

    public static class RESULT_TANGENT_STATUS{

        public static final String APPROVED_STATUS = "1";

        public static final String REJECTED_STATUS = "0";

        public static final String ORDER_RESULT_STATUS_NOK = "0";

        public static final String ORDER_RESULT_STATUS_OK = "1";

        public static final Long OBJECT_REFUSED_CUSTOMER = 1L;

        public static final Long OBJECT_REFUSED_VCC = 2L;



    }

    public interface  STATE_CHECK_LIST {
        public static String NEW = "NEW";
        public static String DONE = "DONE";
    }

    public interface  STATE_WORK_ITEM {
        public static String NEW = "1";
        public static String PROCESSING = "2";
        public static String DONE = "3";
    }

    public interface  STATUS_TANGENT{
        public static String DA_HUY  = "0";
        public static String CHO_TIEP_NHAN  = "1";
        public static String CHO_TIEP_XUC  = "2";
        public static String CHO_TRINH_BAY_GP  = "3";
        public static String TU_CHOI_TIEP_XUC_VA_CHO_PD  = "4";
        public static String CHO_KY_HD  = "5";
        public static String BO_SUNG_CHINH_SUA_GP  = "6";
        public static String TU_CHOI_GP_CHO_PD  = "7";
        public static String HOAN_THANH_TX  = "8";
        public static String DONG_YCTX  = "9";
        public static String HOAN_THANH_TT_HH  = "10";
        public static String CHO_TTHT_DUYET_TT_KH  = "12";
        public static String TTHT_TU_CHOI_TT  = "13";


    }

    public interface  IS_UPDATE{
        public static Long YES  = 1L;
    }

    public interface  REASON_CONTACT_NO{
        public static Long CLIENT  = 1L;
        public static Long VCC  = 2L;
    }

    public static class UTIL_ATTACH_DOCUMENT{
        public static final List<String> LIST_ATTACH_SOLUTION = Arrays.asList("GP", "ED", "TF", "P3", "AF");
        public static final String ATTACH_RESULT_TANGENT = "YCTX_DESIGN_CONSTRUCTION";
    }


    public interface  CHECK_LIST_STATUS{
        public static Long ACTIVE  = 1L;
    }
    public interface  CHECK_LIST_STATE{
        public static String NEW  = "NEW";
    }

    public interface  PAR_TYPE {
        public static String WO_BGBTS_CHECKLIST  = "WO_BGBTS_CHECKLIST";
    }

    public interface  WO_TYPE_CODE {
        public static String BGBTS_DTHT  = "BGBTS_DTHT";
        public static String BGBTS_VHKT  = "BGBTS_VHKT";


    }

    public interface  CHECK_LIST_ID {
        public static Long WO_BGBTS_1  = 1l;
        public static Long WO_BGBTS_2  = 2l;
        public static Long WO_BGBTS_3  = 3l;
        public static Long WO_BGBTS_4  = 4l;
        public static Long WO_BGBTS_5  = 5l;
        public static Long WO_BGBTS_6  = 6l;
    }
    public interface  WO_STATE {
        public static String PROCESSING  = "PROCESSING";
        public static String ACCEPT_CD  = "ACCEPT_CD";
        public static String DONE = "DONE";
        public static String ASSIGN_FT = "ASSIGN_FT";
    }

    public interface  BGBTS_RESULT {
        public static String MISSING_PHOTO= "1";
        public static String FAILED_CONSTRUCTION  = "2";
    }


}
