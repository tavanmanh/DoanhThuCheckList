package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.dto.ReportDTO;
import com.viettel.coms.dto.ReportErpAmsWoDTO;
import com.viettel.coms.dto.RpOrderlyWoDTO;
import com.viettel.coms.dto.SysGroupDTO;
import com.viettel.coms.utils.ValidateUtils;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@EnableTransactionManagement
@Transactional
@Repository("rpOrderlyWoDAO")
public class RpOrderlyWoDAO extends BaseFWDAOImpl<BaseFWModelImpl, Long> {

	public RpOrderlyWoDAO() {
        this.model = new BaseFWModelImpl() {
			
			@Override
			public BaseFWDTOImpl toDTO() {
				// TODO Auto-generated method stub
				return null;
			}
		};
    }
	
	public RpOrderlyWoDAO(Session session) {
		this.session = session;
	}

	// Báo cáo Tiếp nhận WO quá hạn - Tổng hợp
	String sqlTiepNhanWoTH = "with tbl as(select " + 
			"            SUBSTR(c.PATH, " + 
			"            9, " + 
			"            6) sysGroupLv2Id, " + 
			"            c.name cdLevel2Name, " + 
			"            count(*) tong, " + 
			"            sum(case  " + 
			"                when  (a.state  not in ('ASSIGN_CD')  " + 
			"                and (USER_CD_LEVEL2_RECEIVE_WO='-1'))  then 1  " + 
			"                else 0  " + 
			"            end) quahan, " + 
			"            round(100*sum(case  " + 
			"                when  (a.state  not in ('ASSIGN_CD')  " + 
			"                and (USER_CD_LEVEL2_RECEIVE_WO='-1')) then 1  " + 
			"                else 0  " + 
			"            end)/count(*), " + 
			"            2) tyle      " + 
			"        FROM " + 
			"            wo a      " + 
			"        left join " + 
			"            sys_group c  " + 
			"                on a.CD_LEVEL_2=c.sys_group_id      " + 
			"        WHERE " + 
			"            1 = 1      " + 
			"            AND a.status = 1  " + 
			"            and nvl(type,0) not in(2)      " + 
//			"            and a.wo_type_id not in(101) " + 
			"			 and a.wo_type_id not in(101,243) " + 
			"            and nvl(tr_id,0) not in( select id from wo_tr tr where tr.status=1 and tr.tr_type_id=31350)" +
			"            and  a.CD_LEVEL_1=242656   ";

	// Báo cáo Giao WO quá hạn - Tổng hợp
	String sqlGiaoWoTH = "with tbl as(select " + 
			"            SUBSTR(c.PATH, " + 
			"            9, " + 
			"            6) sysGroupLv2Id, " + 
			"            c.name cdLevel2Name, " + 
			"            count(*) tong, " + 
			"            sum(case                  " + 
			"    when  a.state  in ('ACCEPT_CD')   " + 
			"                and trunc(sysdate)-trunc((select " + 
			"                    max(log_time)  " + 
			"                from " + 
			"                    WO_WORKLOGS  logs  " + 
			"                where " + 
			"                    a.id=logs.wo_id  " + 
			"                    and (logs.content like '%Tự động tiếp nhận WO%'  " + 
			"                    or logs.content like '%CD đã tiếp nhận%'))) >2 then 1       " + 
			"    when a.state  in ('ASSIGN_FT','ACCEPT_FT','REJECT_FT','PROCESSING','DONE','OK','NG','OPINION_RQ_1','OPINION_RQ_2','OPINION_RQ_3','OPINION_RQ_4','CD_OK','CD_NG', " + 
			"    'WAIT_TC_BRANCH','WAIT_TC_TCT') " + 
			"     and trunc((select " + 
			"                    max(log_time)  " + 
			"                from " + 
			"                    WO_WORKLOGS  logs  " + 
			"                where " + 
			"                    a.id=logs.wo_id  " + 
			"                    and (logs.content like '%Chờ FT tiếp nhận%'))) -  " + 
			"    trunc((select " + 
			"                    min(log_time)  " + 
			"                from " + 
			"                    WO_WORKLOGS  logs  " + 
			"                where " + 
			"                    a.id=logs.wo_id  " + 
			"                    and (logs.content like '%Tự động tiếp nhận WO%'  " + 
			"                    or logs.content like '%CD đã tiếp nhận%')))  " + 
			"     >2 then 1 " + 
			"                else 0  " + 
			"            end) quahan, " + 
			"    " + 
			"            round(100*sum(case                  " + 
			"    when  a.state  in ('ACCEPT_CD')   " + 
			"                and trunc(sysdate)-trunc((select " + 
			"                    min(log_time)  " + 
			"                from " + 
			"                    WO_WORKLOGS  logs  " + 
			"                where " + 
			"                    a.id=logs.wo_id  " + 
			"                    and (logs.content like '%Tự động tiếp nhận WO%'  " + 
			"                    or logs.content like '%CD đã tiếp nhận%'))) >2 then 1       " + 
			"    when a.state  in ('ASSIGN_FT','ACCEPT_FT','REJECT_FT','PROCESSING','DONE','OK','NG','OPINION_RQ_1','OPINION_RQ_2','OPINION_RQ_3','OPINION_RQ_4','CD_OK','CD_NG', " + 
			"    'WAIT_TC_BRANCH','WAIT_TC_TCT') " + 
			"     and trunc((select " + 
			"                    min(log_time)  " + 
			"                from " + 
			"                    WO_WORKLOGS  logs  " + 
			"                where " + 
			"                    a.id=logs.wo_id  " + 
			"                    and (logs.content like '%Chờ FT tiếp nhận%'))) -  " + 
			"    trunc((select " + 
			"                    min(log_time)  " + 
			"                from " + 
			"                    WO_WORKLOGS  logs  " + 
			"                where " + 
			"                    a.id=logs.wo_id  " + 
			"                    and (logs.content like '%Tự động tiếp nhận WO%'  " + 
			"                    or logs.content like '%CD đã tiếp nhận%')))  " + 
			"     >2 then 1 " + 
			"                else 0  " + 
			"            end)/count(*), " + 
			"            2) tyle      " + 
			"        FROM " + 
			"            wo a      " + 
			"        left join " + 
			"            sys_group c  " + 
			"                on a.CD_LEVEL_2=c.sys_group_id      " + 
			"        WHERE " + 
			"            1 = 1      " + 
			"            AND a.status = 1  " + 
			"            and nvl(type,0) not in(2)      " + 
//			"            and a.wo_type_id not in(101) " + 
"			 and a.wo_type_id not in(101,243) " + 
"            and nvl(tr_id,0) not in( select id from wo_tr tr where tr.status=1 and tr.tr_type_id=31350)" +
			"            and  a.CD_LEVEL_1=242656      " + 
			"            and a.state  in( " + 
			"                'ACCEPT_CD','ASSIGN_FT','ACCEPT_FT','REJECT_FT','PROCESSING','DONE','OK','CD_OK','OPINION_RQ_1','OPINION_RQ_2','OPINION_RQ_3','OPINION_RQ_4', " + 
			"    'CD_NG','NG','WAIT_TC_BRANCH','WAIT_TC_TCT' " + 
			"            )  ";

	// Báo cáo Xác nhận kết quả hoàn thành quá hạn - Tổng hợp
	String sqlXacNhanWoTH = "with tbl as(select " + 
			"            SUBSTR(c.PATH, " + 
			"            9, " + 
			"            6) sysGroupLv2Id, " + 
			"            c.name cdLevel2Name, " + 
			"            count(*) tong, " + 
			"            sum(case  " + 
			"    when a.state in ('DONE')  " + 
			"                and trunc(sysdate)-trunc(end_time) >2 then 1 " + 
			"    when a.state in ('CD_OK','OK','WAIT_TC_BRANCH','WAIT_TC_TCT','CD_NG','NG') " + 
			"    and trunc(UPDATE_CD_APPROVE_WO)-trunc(end_time) >2 then 1 " + 
			"                else 0 " + 
			"            end) quahan, " + 
			"            round(100* sum(case  " + 
			"    when a.state in ('DONE')  " + 
			"                and trunc(sysdate)-trunc(end_time) >2 then 1 " + 
			"    when a.state in ('CD_OK','OK','WAIT_TC_BRANCH','WAIT_TC_TCT','CD_NG','NG') " + 
			"    and trunc(UPDATE_CD_APPROVE_WO)-trunc(end_time) >2 then 1 " + 
			"                else 0 " + 
			"            end)/count(*), " + 
			"            2)tyle      " + 
			"        FROM " + 
			"            wo a      " + 
			"        left join " + 
			"            SYS_USER b  " + 
			"                on a.ft_id=b.SYS_USER_ID      " + 
			"        left join " + 
			"            sys_group c  " + 
			"                on a.CD_LEVEL_2=c.sys_group_id      " + 
			"        WHERE " + 
			"            1 = 1      " + 
			"            AND a.status = 1  " + 
			"            and nvl(type,0) not in(2)      " + 
//			"            and a.wo_type_id not in(101) " + 
"			 and a.wo_type_id not in(101,243) " + 
"            and nvl(tr_id,0) not in( select id from wo_tr tr where tr.status=1 and tr.tr_type_id=31350)" +
			"            and  a.CD_LEVEL_1=242656      " + 
			"            and a.state   in ('CD_OK','DONE','OK','WAIT_TC_BRANCH','WAIT_TC_TCT','CD_NG','NG') ";
	
	//Báo cáo Tiếp nhận WO quá hạn - Chi tiết
	String sqlTiepNhanWoCT = "select " + 
			"            a.id woId, " + 
			"            a.wo_code woCode, " + 
			"            a.wo_name woName, " + 
			"            a.construction_code constructionCode, " + 
			"            nvl(a.CONTRACT_CODE, " + 
			"            a.PROJECT_CODE) contractCode, " + 
			"            to_char(a.CREATED_DATE, " + 
			"            'dd/MM/yyyy hh24:mi:ss') ngayGiaoWo, " + 
			"            nvl(to_char(a.UPDATE_CD_LEVEL2_RECEIVE_WO, " + 
			"            'dd/MM/yyyy hh24:mi:ss'), " + 
			"            (select " + 
			"                to_char(min(log_time), " + 
			"                'dd/MM/yyyy hh24:mi:ss')  " + 
			"            from " + 
			"                WO_WORKLOGS  logs  " + 
			"            where " + 
			"                logs.wo_id=a.id  " + 
			"                and (logs.content like '%Tự động tiếp nhận WO%'  " + 
			"                or logs.content like '%CD đã tiếp nhận%'))) ngayNhanWo, " + 
			"            (select " + 
			"                to_char(max(log_time), " + 
			"                'dd/MM/yyyy')  " + 
			"            from " + 
			"                WO_WORKLOGS  logs  " + 
			"            where " + 
			"                a.id=logs.wo_id  " + 
			"                and logs.content like '%Chờ FT tiếp nhận%') ngayGiaoFt, " + 
			"            nvl(to_char(a.UPDATE_FT_RECEIVE_WO, " + 
			"            'dd/MM/yyyy hh24:mi:ss'), " + 
			"            (select " + 
			"                to_char(max(log_time), " + 
			"                'dd/MM/yyyy hh24:mi:ss')  " + 
			"            from " + 
			"                WO_WORKLOGS  logs  " + 
			"            where " + 
			"                logs.wo_id=a.id  " + 
			"                and (logs.content like '%Tự động tiếp nhận WO%'  " + 
			"                or logs.content like '%FT đã tiếp nhận%'))  ) ngayFtTiepNhan, " + 
			"            null ngayFtThucHienXong, " + 
			"            null ngayCdDuyet, " + 
			"            (select " + 
			"                sg.name  " + 
			"            from " + 
			"                sys_group sg  " + 
			"            where " + 
			"                sg.sys_group_id = SUBSTR(c.PATH,9,6)) cdLevel2Name, " + 
			"            b.full_name fullName, " + 
			"            b.email email, " + 
			"            b.phone_number phoneNumber, " + 
			"            round(a.money_value/1000000, " + 
			"            2) moneyValue, " + 
			"            case  " + 
			"                when a.state ='ASSIGN_CD' then 'Chờ CD tiếp nhận'  " + 
			"                when a.state ='ACCEPT_CD' then 'CD đã tiếp nhận'  " + 
			"                when a.state ='REJECT_CD' then 'CD từ chối'  " + 
			"                when a.state ='ASSIGN_FT' then 'Chờ FT tiếp nhận'  " + 
			"                when a.state ='ACCEPT_FT' then 'FT đã tiếp nhận'  " + 
			"                when a.state ='REJECT_FT' then 'FT từ chối'  " + 
			"                when a.state ='PROCESSING' then 'Đang thực hiện'  " + 
			"                when a.state ='CD_OK' then 'CD đã duyệt'  " + 
			"                when a.state ='DONE' then 'Đã thực hiện'  " + 
			"                when a.state ='WAIT_TC_BRANCH' then 'Chờ TC trụ duyệt'  " +
			"    when a.state ='WAIT_TC_TCT' then 'Chờ tài chính TCT duyệt'  " + 
			"    when a.state ='OK' then 'Đã hoàn thành'  " + 
			"                when a.state ='OPINION_RQ_1' then 'Xin ý kiến CD'  " + 
			"                when a.state ='OPINION_RQ_2' then 'Xin ý kiến CD'  " + 
			"                when a.state ='OPINION_RQ_3' then 'Xin ý kiến CD'  " + 
			"                when a.state ='OPINION_RQ_4' then 'Xin ý kiến CD'  " + 
			"                when a.state ='CD_NG' then 'Điều phối duyệt chưa OK'  " + 
			"                when a.state ='NG' then 'TTHT duyệt chưa OK'  " + 
			"            end state, " + 
			"            case  " + 
			"                when  (a.state  not in ('ASSIGN_CD')  " + 
			"                and (USER_CD_LEVEL2_RECEIVE_WO='-1')) then 'Hệ thống tự động nhận WO do quá hạn 24h mà đơn vị/FT chưa nhận'  " + 
			"                else 'Nhận WO đúng hạn'  " + 
			"            end trangThai      " + 
			"        FROM " + 
			"            wo a      " + 
			"        left join " + 
			"            SYS_USER b  " + 
			"                on a.ft_id=b.SYS_USER_ID   " + 
			"        left join " + 
			"            sys_group c  " + 
			"                on a.CD_LEVEL_2=c.sys_group_id      " + 
			"        WHERE " + 
			"            1 = 1      " + 
			"            AND a.status = 1  " + 
			"            and nvl(type,0) not in( " + 
			"                2 " + 
			"            )      " + 
//			"            and a.wo_type_id not in(101) " + 
"			 and a.wo_type_id not in(101,243) " + 
"            and nvl(tr_id,0) not in( select id from wo_tr tr where tr.status=1 and tr.tr_type_id=31350)" +
			"            and  a.CD_LEVEL_1=242656  ";

	// Báo cáo Giao WO quá hạn - Chi tiết
		String sqlGiaoWoCT = "select " + 
				"            a.id woId, " + 
				"            a.wo_code woCode, " + 
				"            a.wo_name woName, " + 
				"            a.construction_code constructionCode, " + 
				"            nvl(a.CONTRACT_CODE, " + 
				"            a.PROJECT_CODE) contractCode, " + 
				"            to_char(a.CREATED_DATE, " + 
				"            'dd/MM/yyyy hh24:mi:ss') ngayGiaoWo, " + 
				"            (select " + 
				"                to_char(max(log_time), " + 
				"                'dd/MM/yyyy hh24:mi:ss')  " + 
				"            from " + 
				"                WO_WORKLOGS  logs  " + 
				"            where " + 
				"                a.id=logs.wo_id  " + 
				"                and ( " + 
				"                    logs.content like '%Tự động tiếp nhận WO%'  " + 
				"                    or logs.content like '%CD đã tiếp nhận%' " + 
				"                ) ) ngayNhanWo, " + 
				"            (select " + 
				"                to_char(max(log_time), " + 
				"                'dd/MM/yyyy hh24:mi:ss')  " + 
				"            from " + 
				"                WO_WORKLOGS  logs  " + 
				"            where " + 
				"                a.id=logs.wo_id  " + 
				"                and logs.content like '%Chờ FT tiếp nhận%') ngayGiaoFt, " + 
				"            null ngayFtTiepNhan, " + 
				"            null ngayFtThucHienXong, " + 
				"            null ngayCdDuyet, " + 
				"            (select " + 
				"                sg.name  " + 
				"            from " + 
				"                sys_group sg  " + 
				"            where " + 
				"                sg.sys_group_id = SUBSTR(c.PATH,9,6)) cdLevel2Name , " + 
				"            b.full_name fullName, " + 
				"            b.email email, " + 
				"            b.phone_number phoneNumber, " + 
				"            round(a.money_value/1000000, " + 
				"            2) moneyValue, " + 
				"            case  " + 
				"                when a.state ='ASSIGN_CD' then 'Chờ CD tiếp nhận'  " + 
				"                when a.state ='ACCEPT_CD' then 'CD đã tiếp nhận'  " + 
				"                when a.state ='REJECT_CD' then 'CD từ chối'  " + 
				"                when a.state ='ASSIGN_FT' then 'Chờ FT tiếp nhận'  " + 
				"                when a.state ='ACCEPT_FT' then 'FT đã tiếp nhận'  " + 
				"                when a.state ='REJECT_FT' then 'FT từ chối'  " + 
				"                when a.state ='PROCESSING' then 'Đang thực hiện'  " + 
				"                when a.state ='CD_OK' then 'CD đã duyệt'  " + 
				"                when a.state ='DONE' then 'Đã thực hiện'  " + 
				"                when a.state ='WAIT_TC_BRANCH' then 'Chờ TC trụ duyệt'  " +
				"    when a.state ='WAIT_TC_TCT' then 'Chờ tài chính TCT duyệt'  " + 
				"    when a.state ='OK' then 'Đã hoàn thành'  " + 
				"                when a.state ='OPINION_RQ_1' then 'Xin ý kiến CD'  " + 
				"                when a.state ='OPINION_RQ_2' then 'Xin ý kiến CD'  " + 
				"                when a.state ='OPINION_RQ_3' then 'Xin ý kiến CD'  " + 
				"                when a.state ='OPINION_RQ_4' then 'Xin ý kiến CD'  " + 
				"                when a.state ='CD_NG' then 'Điều phối duyệt chưa OK'  " + 
				"                when a.state ='NG' then 'TTHTduyệt chưa OK'  " + 
				"            end state, " + 
				"            case  " + 
				"                when  a.state  in ('ACCEPT_CD')   " + 
				"                and trunc(sysdate)-trunc((select " + 
				"                    min(log_time)  " + 
				"                from " + 
				"                    WO_WORKLOGS  logs  " + 
				"                where " + 
				"                    a.id=logs.wo_id  " + 
				"                    and (logs.content like '%Tự động tiếp nhận WO%'  " + 
				"                    or logs.content like '%CD đã tiếp nhận%'))) >2 then 'CNKT giao WO cho FT quá hạn 48h sau khi nhận WO từ TTHT'        " + 
				"    when a.state  in ('ASSIGN_FT','ACCEPT_FT','REJECT_FT','PROCESSING','DONE','OK','NG','OPINION_RQ_1','OPINION_RQ_2','OPINION_RQ_3','OPINION_RQ_4','CD_OK','CD_NG', " + 
				"    'WAIT_TC_BRANCH','WAIT_TC_TCT') " + 
				"     and trunc((select " + 
				"                    min(log_time)  " + 
				"                from " + 
				"                    WO_WORKLOGS  logs  " + 
				"                where " + 
				"                    a.id=logs.wo_id  " + 
				"                    and (logs.content like '%Chờ FT tiếp nhận%'))) -  " + 
				"    trunc((select " + 
				"                    min(log_time)  " + 
				"                from " + 
				"                    WO_WORKLOGS  logs  " + 
				"                where " + 
				"                    a.id=logs.wo_id  " + 
				"                    and (logs.content like '%Tự động tiếp nhận WO%'  " + 
				"                    or logs.content like '%CD đã tiếp nhận%')))  " + 
				"     >2 then 'CNKT giao WO cho FT quá hạn 48h sau khi nhận WO từ TTHT'  " + 
				"                else 'Giao FT đúng hạn'  " + 
				"            end trangThai      " + 
				"        FROM " + 
				"            wo a      " + 
				"        left join " + 
				"            SYS_USER b  " + 
				"                on a.ft_id=b.SYS_USER_ID   " + 
				"        left join " + 
				"            sys_group c  " + 
				"                on a.CD_LEVEL_2=c.sys_group_id      " + 
				"        WHERE " + 
				"            1 = 1      " + 
				"            AND a.status = 1  " + 
				"            and nvl(type,0) not in( " + 
				"                2 " + 
				"            )      " + 
//				"            and a.wo_type_id  not in(101) " + 
"			 and a.wo_type_id not in(101,243) " + 
"            and nvl(tr_id,0) not in( select id from wo_tr tr where tr.status=1 and tr.tr_type_id=31350)" +
				"            and  a.CD_LEVEL_1=242656    " + 
				"            and a.state  in( " + 
				"                'ACCEPT_CD','ASSIGN_FT','ACCEPT_FT','REJECT_FT','PROCESSING','DONE','OK','CD_OK','OPINION_RQ_1','OPINION_RQ_2','OPINION_RQ_3','OPINION_RQ_4', " + 
				"    'CD_NG','NG','WAIT_TC_BRANCH','WAIT_TC_TCT' " + 
				"            )  ";
	
		// Báo cáo Xác nhận kết quả hoàn thành quá hạn - Chi tiết
		String sqlXacNhanWoCT = "select " + 
				"            a.id woId, " + 
				"            a.wo_code woCode, " + 
				"            a.wo_name woName, " + 
				"            a.construction_code constructionCode, " + 
				"            nvl(a.CONTRACT_CODE, " + 
				"            a.PROJECT_CODE) contractCode, " + 
				"            to_char(a.CREATED_DATE, " + 
				"            'dd/MM/yyyy hh24:mi:ss') ngayGiaoWo, " + 
				"            null ngayNhanWo, " + 
				"            null ngayGiaoFt, " + 
				"            null ngayFtTiepNhan, " + 
				"            to_char(a.END_TIME, " + 
				"            'dd/MM/yyyy hh24:mi:ss') ngayFtThucHienXong, " + 
				"            to_char(a.UPDATE_CD_APPROVE_WO, " + 
				"            'dd/MM/yyyy') ngayCdDuyet, " + 
				"            (select " + 
				"                sg.name  " + 
				"            from " + 
				"                sys_group sg  " + 
				"            where " + 
				"                sg.sys_group_id = SUBSTR(c.PATH,9,6)) cdLevel2Name, " + 
				"            b.full_name fullName, " + 
				"            b.email email, " + 
				"            b.phone_number phoneNumber, " + 
				"            round(a.money_value/1000000, " + 
				"            2) moneyValue, " + 
				"            case  " + 
				"                when a.state ='ASSIGN_CD' then 'Chờ CD tiếp nhận'  " + 
				"                when a.state ='ACCEPT_CD' then 'CD đã tiếp nhận'  " + 
				"                when a.state ='REJECT_CD' then 'CD từ chối'  " + 
				"                when a.state ='ASSIGN_FT' then 'Chờ FT tiếp nhận'  " + 
				"                when a.state ='ACCEPT_FT' then 'FT đã tiếp nhận'  " + 
				"                when a.state ='REJECT_FT' then 'FT từ chối'  " + 
				"                when a.state ='PROCESSING' then 'Đang thực hiện'  " + 
				"                when a.state ='CD_OK' then 'CD đã duyệt'  " + 
				"                when a.state ='DONE' then 'Đã thực hiện'  " + 
				"                when a.state ='WAIT_TC_BRANCH' then 'Chờ TC trụ duyệt'  " +
				"    when a.state ='WAIT_TC_TCT' then 'Chờ tài chính TCT duyệt'  " + 
				"    when a.state ='OK' then 'Đã hoàn thành'  " + 
				"                when a.state ='OPINION_RQ_1' then 'Xin ý kiến CD'  " + 
				"                when a.state ='OPINION_RQ_2' then 'Xin ý kiến CD'  " + 
				"                when a.state ='OPINION_RQ_3' then 'Xin ý kiến CD'  " + 
				"                when a.state ='OPINION_RQ_4' then 'Xin ý kiến CD'  " + 
				"                when a.state ='CD_NG' then 'Điều phối duyệt chưa OK'  " + 
				"                when a.state ='NG' then 'TTHT duyệt chưa OK'  " + 
				"            end state, " + 
				"            case  " + 
				"                when a.state in ('DONE')  " + 
				"                and trunc(sysdate)-trunc(end_time) >2 then 'CNKT phê duyệt WO quá hạn 48h sau khi FT hoàn thành xong'  " + 
				"    when a.state in ('CD_OK','OK','WAIT_TC_BRANCH','WAIT_TC_TCT','CD_NG','NG') " + 
				"    and trunc(UPDATE_CD_APPROVE_WO)-trunc(end_time) >2 then 'CNKT phê duyệt WO quá hạn 48h sau khi FT hoàn thành xong'  " + 
				"                else 'Phê duyệt đúng hạn'  " + 
				"            end trangThai      " + 
				"        FROM " + 
				"            wo a      " + 
				"        left join " + 
				"            SYS_USER b  " + 
				"                on a.ft_id=b.SYS_USER_ID   " + 
				"        left join " + 
				"            sys_group c  " + 
				"                on a.CD_LEVEL_2=c.sys_group_id      " + 
				"        WHERE " + 
				"            1 = 1      " + 
				"            AND a.status = 1  " + 
				"            and nvl(type,0) not in( " + 
				"                2 " + 
				"            )      " + 
//				"            and a.wo_type_id  not in(101) " + 
"			 and a.wo_type_id not in(101,243) " + 
"            and nvl(tr_id,0) not in( select id from wo_tr tr where tr.status=1 and tr.tr_type_id=31350)" +
				"            and  a.CD_LEVEL_1=242656      " + 
				"            and a.state   in ( " + 
				"                'CD_OK','DONE','OK','WAIT_TC_BRANCH','WAIT_TC_TCT','CD_NG','NG' " + 
				"            )  ";
		
	//Báo cáo tổng hợp
	@SuppressWarnings("unchecked")
	public List<RpOrderlyWoDTO> getDataReceiveWoSynthetic(RpOrderlyWoDTO obj, Boolean isExport) {
		StringBuilder sql = new StringBuilder("");

		if(isExport) {
			if (obj.getStatus().equals("1")) {
				sql.append(sqlTiepNhanWoTH);
			} else if (obj.getStatus().equals("2")) {
				sql.append(sqlGiaoWoTH);
			} else if (obj.getStatus().equals("3")) {
				sql.append(sqlXacNhanWoTH);
			}
		} else {
			if (obj.getStatus().equals("1") && obj.getTypeBc().equals("1")) {
				sql.append(sqlTiepNhanWoTH);
			} else if (obj.getStatus().equals("2") && obj.getTypeBc().equals("1")) {
				sql.append(sqlGiaoWoTH);
			} else if (obj.getStatus().equals("3") && obj.getTypeBc().equals("1")) {
				sql.append(sqlXacNhanWoTH);
			}
		}
		
		if (StringUtils.isNotBlank(obj.getCdLevel1())) {
			sql.append(" AND SUBSTR(c.PATH,9,6) = :cdLevel1 ");
		}

		if (obj.getStartDate() != null) {
			sql.append(" AND a.CREATED_DATE  >= :startDate ");
		}

		if (obj.getEndDate() != null) {
			sql.append(" AND a.CREATED_DATE  <= :endDate ");
		}

		sql.append(" group by c.name, c.PATH) " + 
				"select sg.name cdLevel2Name, " + 
				"sum(tbl.tong) tong, " + 
				"sum(tbl.quahan) quahan, " + 
				"round((case when sum(tbl.tong)>0 then sum(tbl.quahan)/sum(tbl.tong) else 0 end) * 100) tyle " + 
				"from tbl " + 
				"left join SYS_GROUP sg on tbl.sysGroupLv2Id = sg.SYS_GROUP_ID " + 
				"group by sg.name " + 
				"ORDER by sg.name asc ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("cdLevel2Name", new StringType());
		query.addScalar("tong", new DoubleType());
		query.addScalar("quahan", new DoubleType());
		query.addScalar("tyle", new DoubleType());

		if (StringUtils.isNotBlank(obj.getCdLevel1())) {
			query.setParameter("cdLevel1", obj.getCdLevel1());
			queryCount.setParameter("cdLevel1", obj.getCdLevel1());
		}

		if (obj.getStartDate() != null) {
			query.setParameter("startDate", obj.getStartDate());
			queryCount.setParameter("startDate", obj.getStartDate());
		}

		if (obj.getEndDate() != null) {
			query.setParameter("endDate", obj.getEndDate());
			queryCount.setParameter("endDate", obj.getEndDate());
		}

		query.setResultTransformer(Transformers.aliasToBean(RpOrderlyWoDTO.class));

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}

		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	// Báo cáo Chi tiết
	@SuppressWarnings("unchecked")
	public List<RpOrderlyWoDTO> getDataReceiveWoDetail(RpOrderlyWoDTO obj, Boolean isExport) {
		StringBuilder sql = new StringBuilder("");

		if(isExport) {
			if (obj.getStatus().equals("1")) {
				sql.append(sqlTiepNhanWoCT);
			} else if (obj.getStatus().equals("2")) {
				sql.append(sqlGiaoWoCT);
			} else if (obj.getStatus().equals("3")) {
				sql.append(sqlXacNhanWoCT);
			}
		} else {
			if (obj.getStatus().equals("1") && obj.getTypeBc().equals("2")) {
				sql.append(sqlTiepNhanWoCT);
			} else if (obj.getStatus().equals("2") && obj.getTypeBc().equals("2")) {
				sql.append(sqlGiaoWoCT);
			} else if (obj.getStatus().equals("3") && obj.getTypeBc().equals("2")) {
				sql.append(sqlXacNhanWoCT);
			}
		}
		
		if (StringUtils.isNotBlank(obj.getCdLevel1())) {
			sql.append(" AND SUBSTR(c.PATH,9,6) = :cdLevel1 ");
		}

		if (obj.getStartDate() != null) {
			sql.append(" AND a.CREATED_DATE  >= :startDate ");
		}

		if (obj.getEndDate() != null) {
			sql.append(" AND a.CREATED_DATE  <= :endDate ");
		}

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("woId", new LongType());
		query.addScalar("woCode", new StringType());
		query.addScalar("woName", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("ngayGiaoWo", new StringType());
		query.addScalar("ngayNhanWo", new StringType());
		query.addScalar("ngayGiaoFt", new StringType());
		query.addScalar("ngayFtTiepNhan", new StringType());
		query.addScalar("fullName", new StringType());
		query.addScalar("email", new StringType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("state", new StringType());
		query.addScalar("trangThai", new StringType());
		query.addScalar("cdLevel2Name", new StringType());
		query.addScalar("ngayFtThucHienXong", new StringType());
		query.addScalar("ngayCdDuyet", new StringType());

		if (StringUtils.isNotBlank(obj.getCdLevel1())) {
			query.setParameter("cdLevel1", obj.getCdLevel1());
			queryCount.setParameter("cdLevel1", obj.getCdLevel1());
		}

		if (obj.getStartDate() != null) {
			query.setParameter("startDate", obj.getStartDate());
			queryCount.setParameter("startDate", obj.getStartDate());
		}

		if (obj.getEndDate() != null) {
			query.setParameter("endDate", obj.getEndDate());
			queryCount.setParameter("endDate", obj.getEndDate());
		}

		query.setResultTransformer(Transformers.aliasToBean(RpOrderlyWoDTO.class));

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}

		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}
	
	//Huypq-24052021-start
	StringBuilder getSelectAllQuery(){
    	StringBuilder stringBuilder = new StringBuilder("select ");
		stringBuilder.append("T1.SYS_GROUP_ID sysGroupId ");
		stringBuilder.append(",T1.CODE code ");
		stringBuilder.append(",T1.NAME name ");
		stringBuilder.append(",T1.PARENT_ID parentId ");
		stringBuilder.append(",(SELECT T2.NAME FROM CTCT_CAT_OWNER.SYS_GROUP T2 WHERE T2.SYS_GROUP_ID = T1.PARENT_ID) parentName ");   
		stringBuilder.append(",T1.STATUS status ");
		stringBuilder.append(",T1.PATH path ");
		stringBuilder.append(",T1.EFFECT_DATE effectDate ");
		stringBuilder.append(",T1.END_DATE endDate ");
		stringBuilder.append(",T1.GROUP_LEVEL groupLevel ");
		stringBuilder.append(",T1.AREA_CODE areaCode ");
		stringBuilder.append(",T1.PROVINCE_CODE catProvinceCode ");
    	stringBuilder.append("FROM SYS_GROUP T1  ");   
    	
    	return stringBuilder;
	}
	
	public List<SysGroupDTO> getForAutoCompleteByGroupLv2(SysGroupDTO obj) {
		StringBuilder stringBuilder = getSelectAllQuery();
		stringBuilder.append("Where T1.STATUS = 1 " + "AND T1.GROUP_LEVEL in (2) " + "and T1.path like '%166571%' "
				+ "and (T1.code like 'TT%' or T1.code like 'CNCT%' or T1.code like '%CQC%')");

		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			stringBuilder
					.append(" AND (UPPER(T1.NAME) like UPPER(:key) OR UPPER(T1.CODE) like UPPER(:key) escape '&')");
		}
		stringBuilder.append(" ORDER BY T1.CODE ASC");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(stringBuilder.toString());
		sqlCount.append(")");
		
		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("sysGroupId", new LongType());
		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.addScalar("parentId", new LongType());
		query.addScalar("parentName", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("path", new StringType());
		query.addScalar("effectDate", new DateType());
		query.addScalar("endDate", new DateType());
		query.addScalar("areaCode", new StringType());
		query.addScalar("catProvinceCode", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(SysGroupDTO.class));

		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("key", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("key", "%" + obj.getKeySearch() + "%");
		}

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1)
					* obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
    public List<CatProvinceDTO> doSearchProvince(CatProvinceDTO obj) {
        StringBuilder sql = new StringBuilder(
                "select cp.CAT_PROVINCE_ID catProvinceId,cp.code code, " + 
                "aa.name name " + 
                "from AIO_AREA aa " + 
                "LEFT JOIN CAT_PROVINCE cp " + 
                "on aa.PROVINCE_ID = cp.CAT_PROVINCE_ID ");
        sql.append(" WHERE 1=1 AND aa.AREA_LEVEL=2 "
        		);
        if (StringUtils.isNotEmpty(obj.getName())) {
            sql.append(" AND upper(aa.NAME) LIKE upper(:name) escape '&' ");
        }
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("catProvinceId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("code", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(CatProvinceDTO.class));
        if (StringUtils.isNotEmpty(obj.getName())) {
            query.setParameter("name", "%" + com.viettel.coms.utils.ValidateUtils.validateKeySearch(obj.getName()) + "%");
            queryCount.setParameter("name", "%" + com.viettel.coms.utils.ValidateUtils.validateKeySearch(obj.getName()) + "%");
        }
        if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }
	
	//Bc tổng hợp CTV
	@SuppressWarnings("unchecked")
	public List<ReportDTO> doSearchGeneralCtv(ReportDTO obj) {
		StringBuilder sql = new StringBuilder("with tbl as (SELECT sglv2.SYS_GROUP_ID sysGroupId, " + 
				"    sglv2.NAME chi_nhanh, " + 
				"    sg.NAME quan_huyen, " + 
				"    MAX(u.created_date) createdDate, " + 
				"    COUNT(u.sys_user_id) numberUserActive, " + 
				"    0 numberUserNotActive " + 
				"FROM " + 
				"    SYS_USER u " + 
				"    LEFT JOIN SYS_USER su1 ON u.ACTIVE_USER = su1.SYS_USER_ID " + 
				"    LEFT JOIN SYS_USER su2 ON u.APPROVED_USER = su2.SYS_USER_ID " + 
				"    LEFT JOIN SYS_USER su3 ON u.APPROVED_USER_TCT = su3.SYS_USER_ID " + 
				"    LEFT JOIN SYS_GROUP sg on u.SYS_GROUP_ID = sg.SYS_GROUP_ID " + 
				"    LEFT OUTER JOIN ( " + 
				"        SELECT " + 
				"            SYS_GROUP_ID, " + 
				"            CODE, " + 
				"            NAME " + 
				"        FROM " + 
				"            sys_group " + 
				"        WHERE " + 
				"            group_level = 2 " + 
				"    ) sglv2 ON instr(sg.PATH,sglv2.SYS_GROUP_ID) > 0 " + 
				"WHERE " + 
				"    1 = 1 " + 
				"    AND instr(sg.PATH,sglv2.SYS_GROUP_ID) > 0 " + 
				"    AND u.type_user IN ( " + 
				"        1 " + 
				"    ) " + 
				"    AND ( " + 
				"        1 = 1 " + 
				"        AND u.Field_Type LIKE '%2%' " + 
				"        AND u.status != 0 " + 
				"    ) " + 
				"    AND u.OCCUPATION IS NULL " + 
				"    group by sglv2.SYS_GROUP_ID, sglv2.NAME, " + 
				"    sg.NAME " + 
				"     " + 
				"    union all " + 
				"     " + 
				"    SELECT sglv2.SYS_GROUP_ID sysGroupId, " + 
				"    sglv2.NAME chi_nhanh, " + 
				"    sg.NAME quan_huyen, " + 
				"    null createdDate, " + 
				"    0 numberUserActive, " + 
				"    COUNT(u.sys_user_id) numberUserNotActive " + 
				"FROM " + 
				"    SYS_USER u " + 
				"    LEFT JOIN SYS_USER su1 ON u.ACTIVE_USER = su1.SYS_USER_ID " + 
				"    LEFT JOIN SYS_USER su2 ON u.APPROVED_USER = su2.SYS_USER_ID " + 
				"    LEFT JOIN SYS_USER su3 ON u.APPROVED_USER_TCT = su3.SYS_USER_ID " + 
				"    LEFT JOIN SYS_GROUP sg on u.SYS_GROUP_ID = sg.SYS_GROUP_ID " + 
				"    LEFT OUTER JOIN ( " + 
				"        SELECT " + 
				"            SYS_GROUP_ID, " + 
				"            CODE, " + 
				"            NAME " + 
				"        FROM " + 
				"            sys_group " + 
				"        WHERE " + 
				"            group_level = 2 " + 
				"    ) sglv2 ON instr(sg.PATH,sglv2.SYS_GROUP_ID) > 0 " + 
				"WHERE " + 
				"    1 = 1 " + 
				"    AND instr(sg.PATH,sglv2.SYS_GROUP_ID) > 0 " + 
				"    AND u.type_user IN ( " + 
				"        1 " + 
				"    ) " + 
				"    AND ( " + 
				"        1 = 1 " + 
				"        AND u.Field_Type LIKE '%2%' " + 
				"        AND u.status = 0 " + 
				"    ) " + 
				"    AND u.OCCUPATION IS NULL " + 
				"    group by sglv2.SYS_GROUP_ID, sglv2.NAME, " + 
				"    sg.NAME) " + 
				"    select sysGroupId sysGroupId, chi_nhanh sysGroupName, " + 
				"    quan_huyen districtName, " + 
				"    nvl((select SUM(tblCreate.numberUserActive) " + 
				"        from tbl tblCreate  " + 
				"        where tblCreate.chi_nhanh = tbl.chi_nhanh  " + 
				"        and tblCreate.quan_huyen = tbl.quan_huyen " + 
				"        and tblCreate.createdDate < :dateFrom " + 
				"        ),0) numberCtvActiveFirstStage, " + 
				"    nvl((select SUM(tblCreate.numberUserActive) " + 
				"        from tbl tblCreate  " + 
				"        where tblCreate.chi_nhanh = tbl.chi_nhanh  " + 
				"        and tblCreate.quan_huyen = tbl.quan_huyen " + 
				"        and tblCreate.createdDate >= :dateFrom " + 
				"        and tblCreate.createdDate <= :dateTo " + 
				"        ),0) numberCtvNewInsideStage, " + 
				"    nvl((select SUM(tblCreate.numberUserNotActive) " + 
				"        from tbl tblCreate  " + 
				"        where tblCreate.chi_nhanh = tbl.chi_nhanh  " + 
				"        and tblCreate.quan_huyen = tbl.quan_huyen " + 
				"        and tblCreate.createdDate >= :dateFrom " + 
				"        and tblCreate.createdDate <= :dateTo " + 
				"        ),0) numberCtvEndInsideStage " + 
				"    from tbl where 1=1 ");
			
				sql.append(" AND sysGroupId=:sysGroupId ");
		
				sql.append("  group by sysGroupId, chi_nhanh, quan_huyen ");
				
				sql.append("  order by chi_nhanh ASC, quan_huyen ASC ");
		
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		
		query.addScalar("sysGroupName", new StringType());
		query.addScalar("districtName", new StringType());
		query.addScalar("numberCtvActiveFirstStage", new LongType());
		query.addScalar("numberCtvNewInsideStage", new LongType());
		query.addScalar("numberCtvEndInsideStage", new LongType());
		
		query.setParameter("dateFrom", obj.getDateFrom());
		queryCount.setParameter("dateFrom", obj.getDateFrom());
		query.setParameter("dateTo", obj.getDateTo());
		queryCount.setParameter("dateTo", obj.getDateTo());
		query.setParameter("sysGroupId", obj.getSysGroupId());
		queryCount.setParameter("sysGroupId", obj.getSysGroupId());
		
		query.setResultTransformer(Transformers.aliasToBean(ReportDTO.class));

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}
	
	//Bc chi tiết CTV
	@SuppressWarnings("unchecked")
	public List<ReportDTO> doSearchDetailCtv(ReportDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT sglv2.SYS_GROUP_ID sysGroupId, " + 
				"    sglv2.CODE sysGroupCode, " + 
				"    sglv2.NAME sysGroupName, " + 
				"    sg.NAME teamCluster, " + 
				"    u.DISTRICT_NAME districtName, " + 
				"    u.COMMUNE_NAME communeName, " + 
				"    'Cộng tác viên' object, " + 
				"    '' industry, " + 
				"    u.LOGIN_NAME employeeCodeCTV, " + 
				"    u.FULL_NAME fullNameCTV, " + 
				"    u.PHONE_NUMBER phoneNumber, " + 
				"    case u.STATUS when 1 then 'Hoạt động' else 'Không hoạt động' end status " + 
				" FROM " + 
				"    SYS_USER u " + 
				"    LEFT JOIN SYS_USER su1 ON u.ACTIVE_USER = su1.SYS_USER_ID " + 
				"    LEFT JOIN SYS_USER su2 ON u.APPROVED_USER = su2.SYS_USER_ID " + 
				"    LEFT JOIN SYS_USER su3 ON u.APPROVED_USER_TCT = su3.SYS_USER_ID, SYS_GROUP sg " + 
				"    LEFT OUTER JOIN ( " + 
				"        SELECT " + 
				"            SYS_GROUP_ID, " + 
				"            CODE, " + 
				"            NAME " + 
				"        FROM " + 
				"            sys_group " + 
				"        WHERE " + 
				"            group_level = 2 " + 
				"    ) sglv2 ON instr(sg.PATH,sglv2.SYS_GROUP_ID) > 0 " + 
				" WHERE 1=1 " + 
				"    AND u.SYS_GROUP_ID = sg.SYS_GROUP_ID " + 
				"    AND instr(sg.PATH,sglv2.SYS_GROUP_ID) > 0 " + 
				"    AND u.type_user IN ( " + 
				"        1 " + 
				"    ) " + 
				"    AND ( " + 
				"        1 = 1 " + 
				"        AND u.Field_Type LIKE '%2%' " + 
				"    ) " + 
				"    AND u.OCCUPATION IS NULL and u.status != 0 ");
		
		sql.append(" AND sglv2.SYS_GROUP_ID = :sysGroupId ");
		
		sql.append(" ORDER BY sg.NAME DESC ");
		
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("sysGroupCode", new StringType());
		query.addScalar("sysGroupName", new StringType());
		query.addScalar("teamCluster", new StringType());
		query.addScalar("districtName", new StringType());
		query.addScalar("communeName", new StringType());
		query.addScalar("object", new StringType());
		query.addScalar("industry", new StringType());
		query.addScalar("employeeCodeCTV", new StringType());
		query.addScalar("fullNameCTV", new StringType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("status", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(ReportDTO.class));

		query.setParameter("sysGroupId", obj.getSysGroupId());
		queryCount.setParameter("sysGroupId", obj.getSysGroupId());
		
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}
	
	//Bc quy hoạch CTV
	String sqlZoningHuyen = "SELECT 'Hà Nội' sysGroupName, "
			+ "2 numberCommune, "
			+ "100 numberCtvCurrent, "
			+ "87.5 percentZoningCoverage, "
			+ "'' description "
			+ "from dual";
	
	String sqlZoningXa = "with tbl as(select CAST(Area_Name_Level2 AS VARCHAR2(500)) tinh, CAST(Area_Name_Level3 AS VARCHAR2(500)) huyen, count(area_id) soXa, 0 soCtv from CTCT_KCS_OWNER.Aio_Area where area_level = 4  " + 
			"group by Area_Name_Level2 , Area_Name_Level3 " + 
			"union all " + 
			"select PROVINCE_NAME_CTV_XDDD tinh, DISTRICT_NAME huyen, 0 soXa, count(sys_user_id) soCtv from  sys_user where  " + 
			"Field_Type like '%2%'   " + 
			"and status !=0 group by PROVINCE_NAME_CTV_XDDD , DISTRICT_NAME) " + 
			"select tinh sysGroupName, huyen districtName, MAX(soXa) numberCommune, Max(soCtv) numberCtvCurrent from tbl where 1=1 ";
	
	@SuppressWarnings("unchecked")
	public List<ReportDTO> doSearchZoningCtv(ReportDTO obj) {
		StringBuilder sql = new StringBuilder("");
		
		if(obj.getTypeBc().equals("1")) {
			sql.append(sqlZoningHuyen);
		} else {
			sql.append(sqlZoningXa);
		}
		
		sql.append(" AND tinh=:sysGroupName ");
		sql.append(" GROUP BY tinh,huyen ");
		sql.append(" ORDER BY tinh ASC, huyen ASC ");
		
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		
		query.addScalar("sysGroupName", new StringType());
		query.addScalar("districtName", new StringType());
		query.addScalar("numberCommune", new LongType());
		query.addScalar("numberCtvCurrent", new LongType());
		
		query.setResultTransformer(Transformers.aliasToBean(ReportDTO.class));

		query.setParameter("sysGroupName", obj.getSysGroupName());
		queryCount.setParameter("sysGroupName", obj.getSysGroupName());
		
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}
	
	//Bc doanh thu CTV
	String sqlRevenueHuyen = " WITH TBL AS (SELECT sglv2.SYS_GROUP_ID, " + 
			"    sglv2.NAME chi_nhanh, " + 
			"    sg.NAME quan_huyen, " + 
			"    COUNT(u.sys_user_id) numberCtvCurrent, " + 
			"    0 numberCtvRevenueIncurred, " + 
			"    0 sumRevenueXhh, " + 
			"    0 mediumRevenueXhh, " + 
			"    0 sumDiscountXhh, " + 
			"    0 mediumDiscountXhh " + 
			"FROM " + 
			"    SYS_USER u " + 
			"    LEFT JOIN SYS_USER su1 ON u.ACTIVE_USER = su1.SYS_USER_ID " + 
			"    LEFT JOIN SYS_USER su2 ON u.APPROVED_USER = su2.SYS_USER_ID " + 
			"    LEFT JOIN SYS_USER su3 ON u.APPROVED_USER_TCT = su3.SYS_USER_ID, SYS_GROUP sg " + 
			"    LEFT OUTER JOIN ( " + 
			"        SELECT " + 
			"            SYS_GROUP_ID, " + 
			"            CODE, " + 
			"            NAME " + 
			"        FROM " + 
			"            sys_group " + 
			"        WHERE " + 
			"            group_level = 2 " + 
			"    ) sglv2 ON instr(sg.PATH,sglv2.SYS_GROUP_ID) > 0 " + 
			"WHERE " + 
			"    1 = 1 " + 
			"    AND u.SYS_GROUP_ID = sg.SYS_GROUP_ID " + 
			"    AND instr(sg.PATH,sglv2.SYS_GROUP_ID) > 0 " + 
			"    AND u.type_user IN ( " + 
			"        1 " + 
			"    ) " + 
			"    AND ( " + 
			"        1 = 1 " + 
			"        AND u.Field_Type LIKE '%2%' " + 
			"        AND u.status != 0 " + 
			"    ) " + 
			"    AND u.OCCUPATION IS NULL " + 
			"GROUP BY sglv2.SYS_GROUP_ID, " + 
			"    sg.NAME, " + 
			"    sglv2.NAME " + 
			"UNION ALL " + 
			"SELECT " + 
			"sglv2.SYS_GROUP_ID, " + 
			"    sglv2.NAME chi_nhanh, " + 
			"    sg.NAME quan_huyen, " + 
			"    0 numberCtvCurrent, " + 
			"    COUNT(u.sys_user_id) numberCtvRevenueIncurred, " + 
			"    SUM(sl.CONTRACT_PRICE) sumRevenueXhh, " + 
			"    0 mediumRevenueXhh, " + 
			"    SUM(sl.CONTRACT_ROSE) sumDiscountXhh, " + 
			"    0 mediumDiscountXhh " + 
			"  FROM " + 
			"    SYS_USER u " + 
			"    JOIN Tangent_Customer cs ON u.Sys_User_Id = cs.CREATED_USER " + 
			"    JOIN Result_Solution sl ON cs.TANGENT_CUSTOMER_ID = sl.TANGENT_CUSTOMER_ID AND cs.STATUS = 8 " + 
			"    LEFT JOIN SYS_USER su1 ON u.ACTIVE_USER = su1.SYS_USER_ID " + 
			"    LEFT JOIN SYS_USER su2 ON u.APPROVED_USER = su2.SYS_USER_ID " + 
			"    LEFT JOIN SYS_USER su3 ON u.APPROVED_USER_TCT = su3.SYS_USER_ID, SYS_GROUP sg " + 
			"    LEFT OUTER JOIN ( " + 
			"        SELECT " + 
			"            SYS_GROUP_ID, " + 
			"            CODE, " + 
			"            NAME " + 
			"        FROM " + 
			"            sys_group " + 
			"        WHERE " + 
			"            group_level = 2 " + 
			"    ) sglv2 ON instr(sg.PATH,sglv2.SYS_GROUP_ID) > 0 " + 
			"  WHERE " + 
			"    1 = 1 " + 
			"    AND u.SYS_GROUP_ID = sg.SYS_GROUP_ID " + 
			"    AND instr(sg.PATH,sglv2.SYS_GROUP_ID) > 0 " + 
			"    AND u.type_user IN ( " + 
			"        1 " + 
			"    ) " + 
			"    AND ( " + 
			"        1 = 1 " + 
			"        AND u.Field_Type LIKE '%2%' " + 
			"        AND u.status != 0 " + 
			"    ) " + 
			"    AND u.OCCUPATION IS NULL " + 
			"  GROUP BY sglv2.SYS_GROUP_ID, " + 
			"    sg.NAME, " + 
			"    sglv2.NAME) " + 
			"    SELECT SYS_GROUP_ID, " + 
			"    chi_nhanh sysGroupName,  " + 
			"    quan_huyen districtName,  " + 
			"    MAX(numberCtvCurrent) numberCtvCurrent,  " + 
			"    MAX(numberCtvRevenueIncurred) numberCtvRevenueIncurred,  " + 
			"    MAX(sumRevenueXhh) sumRevenueXhh,  " + 
			"    (CASE MAX(numberCtvRevenueIncurred) WHEN 0 then 0 else MAX(sumRevenueXhh)/MAX(numberCtvRevenueIncurred) end) mediumRevenueXhh,  " + 
			"    MAX(sumDiscountXhh) sumDiscountXhh,  " + 
			"    (CASE MAX(numberCtvRevenueIncurred) WHEN 0 then 0 else MAX(sumDiscountXhh)/MAX(numberCtvRevenueIncurred) end) mediumDiscountXhh " + 
			"    ,'' communeName " +
			"    FROM TBL WHERE 1=1 ";
	String sqlRevenueXa = " WITH TBL AS (SELECT Sys.PROVINCE_ID_CTV_XDDD sysGroupId, " + 
			"    Sys.PROVINCE_NAME_CTV_XDDD sysGroupName, " + 
			"    Sys.DISTRICT_NAME districtName, " + 
			"    Sys.COMMUNE_NAME communeName, " + 
			"    COUNT(sys.sys_user_id) numberCtvCurrent, " + 
			"    0 numberCtvRevenueIncurred, " + 
			"    0 sumRevenueXhh, " + 
			"    0 mediumRevenueXhh, " + 
			"    0 sumDiscountXhh, " + 
			"    0 mediumDiscountXhh " + 
			"FROM " + 
			"    Sys_User sys " + 
			"WHERE " + 
			"    Sys.Type_User = 1 " + 
			"    AND Sys.Field_Type LIKE '%2%' " + 
			"    AND Sys.status != 0 " + 
			"    AND Sys.DISTRICT_NAME is not null " + 
			"GROUP BY Sys.PROVINCE_ID_CTV_XDDD, " + 
			"    Sys.PROVINCE_NAME_CTV_XDDD, " + 
			"    Sys.DISTRICT_NAME, " + 
			"    Sys.COMMUNE_NAME    " + 
			"UNION ALL " + 
			"SELECT  u.PROVINCE_ID_CTV_XDDD sysGroupId, " + 
			"    u.PROVINCE_NAME_CTV_XDDD sysGroupName, " + 
			"    u.DISTRICT_NAME districtName, " + 
			"    u.COMMUNE_NAME communeName, " + 
			"    0 numberCtvCurrent, " + 
			"    COUNT(u.sys_user_id) numberCtvRevenueIncurred, " + 
			"    SUM(sl.CONTRACT_PRICE) sumRevenueXhh, " + 
			"    0 mediumRevenueXhh, " + 
			"    SUM(sl.CONTRACT_ROSE) sumDiscountXhh, " + 
			"    0 mediumDiscountXhh " + 
			"FROM " + 
			"    Sys_User u " + 
			"    JOIN Tangent_Customer cs ON u.Sys_User_Id = cs.CREATED_USER " + 
			"    JOIN Result_Solution sl ON cs.TANGENT_CUSTOMER_ID = sl.TANGENT_CUSTOMER_ID " + 
			"                                               AND cs.STATUS = 8 " + 
			"WHERE " + 
			"    u.type_user IN ( " + 
			"        1 " + 
			"    ) " + 
			"    AND ( " + 
			"        1 = 1 " + 
			"        AND u.Field_Type LIKE '%2%' " + 
			"        AND u.status != 0 " + 
			"    ) " + 
			"    AND u.COMMUNE_NAME is not null " + 
			"GROUP BY u.PROVINCE_ID_CTV_XDDD, " + 
			"    u.PROVINCE_NAME_CTV_XDDD, " + 
			"    u.DISTRICT_NAME, " + 
			"    u.COMMUNE_NAME) " + 
			"    select sysGroupId, sysGroupName, " + 
			"    districtName, " + 
			"    communeName, " + 
			"    MAX(numberCtvCurrent) numberCtvCurrent, " + 
			"    MAX(numberCtvRevenueIncurred) numberCtvRevenueIncurred, " + 
			"    MAX(sumRevenueXhh) sumRevenueXhh, " + 
			"    case MAX(numberCtvRevenueIncurred) when 0 then 0 else round((MAX(sumRevenueXhh)/MAX(numberCtvRevenueIncurred)), 3) end mediumRevenueXhh, " + 
			"    MAX(sumDiscountXhh) sumDiscountXhh, " + 
			"    case MAX(numberCtvRevenueIncurred) when 0 then 0 else round((MAX(sumDiscountXhh)/MAX(numberCtvRevenueIncurred)), 3) end mediumDiscountXhh " + 
			"    from TBL WHERE 1=1 ";
	@SuppressWarnings("unchecked")
	public List<ReportDTO> doSearchRevenueCtv(ReportDTO obj) {
		StringBuilder sql = new StringBuilder("");
		
		if(obj.getTypeBc().equals("1")) {
			sql.append(sqlRevenueHuyen);
			
			if(obj.getSysGroupId()!=null) {
				sql.append(" AND SYS_GROUP_ID=:sysGroupId ");
			}
			sql.append(" GROUP BY SYS_GROUP_ID, chi_nhanh,  " + 
					"    quan_huyen " + 
					"    ORDER BY chi_nhanh ASC,  " + 
					"    quan_huyen ASC ");
		} else {
			sql.append(sqlRevenueXa);
			
			if(StringUtils.isNotBlank(obj.getSysGroupName())) {
				sql.append(" AND sysGroupName=:sysGroupName ");
			}
			sql.append(" GROUP BY sysGroupId, sysGroupName,  " + 
					"    districtName, communeName " + 
					"    ORDER BY sysGroupName ASC,  " + 
					"    districtName ASC, communeName ASC ");
		}
		
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		
		query.addScalar("districtName", new StringType());
		query.addScalar("communeName", new StringType());
		query.addScalar("numberCtvCurrent", new LongType());
		query.addScalar("numberCtvRevenueIncurred", new LongType());
		query.addScalar("sumRevenueXhh", new DoubleType());
		query.addScalar("mediumRevenueXhh", new DoubleType());
		query.addScalar("sumDiscountXhh", new DoubleType());
		query.addScalar("mediumDiscountXhh", new DoubleType());
		
		query.setResultTransformer(Transformers.aliasToBean(ReportDTO.class));

		if(obj.getTypeBc().equals("1")) {
			if(obj.getSysGroupId()!=null) {
				query.setParameter("sysGroupId", obj.getSysGroupId());
				queryCount.setParameter("sysGroupId", obj.getSysGroupId());
			}
		} else {
			if(StringUtils.isNotBlank(obj.getSysGroupName())) {
				query.setParameter("sysGroupName", obj.getSysGroupName());
				queryCount.setParameter("sysGroupName", obj.getSysGroupName());
			}
		}
		
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}
	//Huy-end

	// Huypq-07062021-start
	public List<ReportErpAmsWoDTO> doSearchReportErpAmsWo(ReportErpAmsWoDTO obj) {
		StringBuilder sql = new StringBuilder(" with ams as (SELECT " + 
				"            ccmf.Date_Payment datePayment, " + 
				"            nvl(ccmf.Amount,0) amount, " + 
				"            ccmf.Bill_Code billCode, " + 
				"            nvl(std.Total_Price,0) totalPrice, " + 
				"            nvl(std.Amount_Real,0) amountReal, " + 
				"            std.GOODS_NAME goodsName, " + 
				"            orders.STATION_CODE stationCode " + 
				"        FROM " + 
				"            Stock_Trans st " + 
				"        INNER JOIN " + 
				"            orders orders " + 
				"                on st.Order_Id = orders.Order_Id " + 
				"        INNER JOIN " + 
				"            Stock_Trans_Detail std " + 
				"                ON st.Stock_Trans_Id = std.Stock_Trans_Id " + 
				"        LEFT JOIN " + 
				"            Cnt_Contract_Map_Finance ccmf " + 
				"                ON orders.Station_Code = ccmf.Code_Station " + 
				"        WHERE " + 
				"            st.Business_Type IN (  2, 27) " + 
				"            AND st.type = 2 " + 
				"            AND st.status = 2 " + 
				"            AND orders.STATUS!=0 " + 
				"            AND orders.STATION_CODE is not null)     " + 
				"			 " + 
				"			SELECT " + 
				"           distinct station.CODE stationCode, " + 
				"            cons.code constructionCode, " + 
				"            cp.code provinceCode, " + 
				"            cp.name provinceName, " + 
				"            cc.code contractCode, " + 
				"            case " + 
				"                when  wo.wo_type_id=164 " + 
				"                and wo.state='OK' then wo.FT_NAME ||' ('|| wo.FT_EMAIL ||')' " + 
				"            end ftName, " + 
				"            case " + 
				"                when cons.BROADCASTING_DATE is not null then 'Đã phát sóng trạm' " + 
				"                else 'Chưa phát sóng' " + 
				"            end state, " + 
				"            to_char(cons.BROADCASTING_DATE,'dd/MM/yyyy') endTime, " + 
				"            ams.goodsName goodsName, " + 
				"            (ams.totalPrice) totalPrice, " + 
				"            (ams.amountReal) amountReal, " + 
				"            ams.datePayment datePayment, " + 
				"            ams.billCode billCode, " + 
				"            (ams.amount) amount " + 
				"        FROM " + 
				"            construction cons " + 
				"        LEFT JOIN " + 
				"            CAT_STATION station " + 
				"                on cons.CAT_STATION_ID = station.CAT_STATION_ID " + 
				"                and station.STATUS!=0 " + 
				"        LEFT JOIN " + 
				"            CAT_PROVINCE cp " + 
				"                on station.CAT_PROVINCE_ID = cp.CAT_PROVINCE_ID " + 
				"        LEFT join " + 
				"            CNT_CONSTR_WORK_ITEM_TASK ccwit " + 
				"                on cons.CONSTRUCTION_ID = ccwit.CONSTRUCTION_ID " + 
				"        LEFT JOIN " + 
				"            CNT_CONTRACT cc " + 
				"                on ccwit.CNT_CONTRACT_ID = cc.CNT_CONTRACT_ID " + 
				"                and cc.STATUS!=0 " + 
				"        left join " + 
				"            wo_mapping_station mapStation " + 
				"                on station.CAT_STATION_ID=mapStation.CAT_STATION_ID " + 
				"        left JOIN " + 
				"            WO wo " + 
				"                on mapStation.wo_id = wo.id " + 
				"        LEFT JOIN " + 
				"            ams ams " + 
				"                on station.code = ams.stationCode " + 
				"        WHERE " + 
				"            cons.CHECK_HTCT = 1 " + 
				"            and station.code is not null " + 
				"            and cc.code is not null ");

		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			sql.append(" AND (UPPER(cons.code) LIKE UPPER(:keySearch) escape '&' "
					+ "OR UPPER(cc.code) LIKE UPPER(:keySearch) escape '&' "
					+ "OR UPPER(station.CODE) LIKE UPPER(:keySearch) escape '&' " + ") ");
		}

		if (StringUtils.isNotBlank(obj.getProvinceCode())) {
			sql.append(" AND cp.code = :provinceCode ");
		}

		if (StringUtils.isNotBlank(obj.getFtName())) {
			sql.append(" AND wo.FT_EMAIL = :ftName ");
		}
		sql.append(" order by cp.code,cons.code ");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("stationCode", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("provinceName", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("ftName", new StringType());
		query.addScalar("state", new StringType());
		query.addScalar("endTime", new StringType());
		query.addScalar("datePayment", new StringType());
		query.addScalar("billCode", new StringType());
		query.addScalar("goodsName", new StringType());
		query.addScalar("totalPrice", new DoubleType());
		query.addScalar("amountReal", new DoubleType());
		query.addScalar("amount", new DoubleType());

		query.setResultTransformer(Transformers.aliasToBean(ReportErpAmsWoDTO.class));

		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
			queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
		}

		if (StringUtils.isNotBlank(obj.getProvinceCode())) {
			query.setParameter("provinceCode", obj.getProvinceCode());
			queryCount.setParameter("provinceCode", obj.getProvinceCode());
		}

		if (StringUtils.isNotBlank(obj.getFtName())) {
			query.setParameter("ftName", obj.getFtName());
			queryCount.setParameter("ftName", obj.getFtName());
		}

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}

		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}
	// Huypq-end
}
