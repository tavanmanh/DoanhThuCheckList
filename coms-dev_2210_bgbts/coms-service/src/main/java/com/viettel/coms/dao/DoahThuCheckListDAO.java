package com.viettel.coms.dao;

import com.viettel.coms.bo.WO_DOANHTHU_GPTH_CHECKLIST_BO;
import com.viettel.coms.bo.WoBO;
import com.viettel.coms.dto.WO_DOANHTHU_GPTH_CHECKLIST_DTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Repository
@Transactional
public class DoahThuCheckListDAO extends BaseFWDAOImpl<WO_DOANHTHU_GPTH_CHECKLIST_BO, Long> {
    private final String CREATE_NEW_MSG = "Đã tạo mới";
    private final String UPDATED_MSG = "Đã chỉnh sửa";

    private final String ALL_TYPE = "ALL TYPE";
    private final String REPORT_TYPE = "REPORT_TYPE";
    private final String OTHERS = "OTHERS";
    private static final String TYPE_THUE_MAT_BANG = "THUE_MAT_BANG";
    private static final String TYPE_KHOI_CONG = "KHOI_CONG";
    private static final String TYPE_DONG_BO = "DONG_BO";
    private static final String TYPE_PHAT_SONG = "PHAT_SONG";
    private static final String TYPE_HSHC = "HSHC";

    @Autowired
    private ConstructionTaskDAO constructionTaskDao;

    public DoahThuCheckListDAO() {
        this.model = new WO_DOANHTHU_GPTH_CHECKLIST_BO();
    }
    public DoahThuCheckListDAO(Session session) {
        this.session = session;
    }
    public String getNameProgress(long id) {
        StringBuilder sql = new StringBuilder("SELECT NAME FROM WO_DOANHTHU_GPTH_CHECKLIST WHERE ID = :id ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.setParameter("id", id);
        query.addScalar("NAME", new StringType());

        List<String> names = query.list();
        if (names.size() > 0)
            return names.get(0);
        else
            return null;
    }

    public boolean updateNameProgress(long id,String name) {
        StringBuilder sql = new StringBuilder("UPDATE WO_DOANHTHU_GPTH_CHECKLIST SET NAME = :name WHERE ID = :id AND STATUS = 1 ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);
        query.setParameter("name", ""+name+"");
        int result = query.executeUpdate();
        if(result>0){
            return true;
        }
        else return false;
    }

    public boolean deleteDoanhThuCheckList(long id) {
        StringBuilder sql = new StringBuilder("UPDATE WO_DOANHTHU_GPTH_CHECKLIST SET STATUS = 0 WHERE ID = :id AND STATUS = 1 ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);
        int result = query.executeUpdate();
        if(result>0){
            return true;
        }
        else return false;
    }
    public boolean createDoanhThuCheckList(long id,String name) {
        StringBuilder sql = new StringBuilder("INSERT INTO WO_DOANHTHU_GPTH_CHECKLIST(ID,NAME) VALUES(:id,:name)");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);
        query.setParameter("name", ""+name+"");
        int result = query.executeUpdate();
        if(result>0){
            return true;
        }
        else return false;
    }
    public WO_DOANHTHU_GPTH_CHECKLIST_BO getOneRaw(long woId) {
        return this.get(WO_DOANHTHU_GPTH_CHECKLIST_BO.class, woId);
    }
}
