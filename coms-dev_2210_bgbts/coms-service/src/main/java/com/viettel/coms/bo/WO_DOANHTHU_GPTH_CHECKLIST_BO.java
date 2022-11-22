package com.viettel.coms.bo;

import com.viettel.coms.dto.WO_DOANHTHU_GPTH_CHECKLIST_DTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="WO_DOANHTHU_GPTH_CHECKLIST")
public class WO_DOANHTHU_GPTH_CHECKLIST_BO extends BaseFWModelImpl {
    private Long id;
    private Long WO_ID;
    private Long CONFIRM;
    private String CD_CONFIRM_BY;
    private String NAME;
    private Date CD_CONFIRM_DATE;
    private Date START_DATE_PLAN;
    private Date END_DATE_PLAN;
    private Date START_DATE_ACTUAL;
    private Date END_DATE_ACTUAL;
    private Long REVENUE_BF_VAT_PLAN;
    private Long REVENUE_BF_VAT_ACTUAL;
    private Long REVENUE_BF_VAT_APPROVE;
    private Long REVENUE_AT_VAT_APPROVE;
    private Long WAGE;
    private Long MATERIAL_TCT;
    private Long MATERIAL_CNCT;
    private Long LABOR;
    private Long STATUS;
    private Date CREATE_DATE;
    private String USER_CREATED;
    private String TRU_CONFIRM_BY;
    private String TC_TRU_CONFIRM_BY;
    private String TC_TCT_CONFIRM_BY;
    private Date TRU_CONFIRM_DATE;
    private Date TC_TRU_CONFIRM_DATE;
    private Date TC_TCT_CONFIRM_DATE;
    private String STATE;
    private Long REVENUE_BF_VAT_SUGGEST;
    private Long REVENUE_AT_VAT_SUGGEST;
    private Long WAGE_SUGGEST;
    private Long MATERIAL_TCT_SUGGEST;
    private Long MATERIAL_CNCT_SUGGEST;
    private Long LABOR_SUGGEST;
    private Long HSHC_COST_SUGGEST;
    private Long COMMISSION_COST_SUGGEST;
    private Long OTHER_COST_SUGGEST;
    private Long VAT_SUGGEST;
    private Long PERCENT_SUGGEST;
    private String DOC_NUMBER_SUGGEST;
    private Long TOTAL_COST_SUGGEST;
    private Long HSHC_COST;
    private Long COMMISSION_COST;
    private Long OTHER_COST;
    private Long VAT;
    private Long PERCENT;
    private Long TOTAL_COST;
    private String DOC_NUMBER;
    private Long MACHINE;
    private Long MACHINE_SUGGEST;
    private String BBBG_PATH;
    private String BBBG_NAME;
    private String BBQTCDT_PATH;
    private String BBQTCDT_NAME;
    private String TT_NAME;
    private String TT_PATH;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Column(name = "WO_ID")
    public Long getWO_ID() {
        return WO_ID;
    }

    public void setWO_ID(Long WO_ID) {
        this.WO_ID = WO_ID;
    }
    @Column(name = "CONFIRM")
    public Long getCONFIRM() {
        return CONFIRM;
    }

    public void setCONFIRM(Long CONFIRM) {
        this.CONFIRM = CONFIRM;
    }
    @Column(name = "CD_CONFIRM_BY")
    public String getCD_CONFIRM_BY() {
        return CD_CONFIRM_BY;
    }

    public void setCD_CONFIRM_BY(String CD_CONFIRM_BY) {
        this.CD_CONFIRM_BY = CD_CONFIRM_BY;
    }
    @Column(name = "NAME")
    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
    @Column(name = "CD_CONFIRM_DATE")
    public Date getCD_CONFIRM_DATE() {
        return CD_CONFIRM_DATE;
    }

    public void setCD_CONFIRM_DATE(Date CD_CONFIRM_DATE) {
        this.CD_CONFIRM_DATE = CD_CONFIRM_DATE;
    }
    @Column(name = "START_DATE_PLAN")
    public Date getSTART_DATE_PLAN() {
        return START_DATE_PLAN;
    }

    public void setSTART_DATE_PLAN(Date START_DATE_PLAN) {
        this.START_DATE_PLAN = START_DATE_PLAN;
    }
    @Column(name = "END_DATE_PLAN")
    public Date getEND_DATE_PLAN() {
        return END_DATE_PLAN;
    }

    public void setEND_DATE_PLAN(Date END_DATE_PLAN) {
        this.END_DATE_PLAN = END_DATE_PLAN;
    }
    @Column(name = "START_DATE_ACTUAL")
    public Date getSTART_DATE_ACTUAL() {
        return START_DATE_ACTUAL;
    }

    public void setSTART_DATE_ACTUAL(Date START_DATE_ACTUAL) {
        this.START_DATE_ACTUAL = START_DATE_ACTUAL;
    }
    @Column(name = "END_DATE_ACTUAL")
    public Date getEND_DATE_ACTUAL() {
        return END_DATE_ACTUAL;
    }

    public void setEND_DATE_ACTUAL(Date END_DATE_ACTUAL) {
        this.END_DATE_ACTUAL = END_DATE_ACTUAL;
    }
    @Column(name = "REVENUE_BF_VAT_PLAN")
    public Long getREVENUE_BF_VAT_PLAN() {
        return REVENUE_BF_VAT_PLAN;
    }

    public void setREVENUE_BF_VAT_PLAN(Long REVENUE_BF_VAT_PLAN) {
        this.REVENUE_BF_VAT_PLAN = REVENUE_BF_VAT_PLAN;
    }
    @Column(name = "REVENUE_BF_VAT_ACTUAL")
    public Long getREVENUE_BF_VAT_ACTUAL() {
        return REVENUE_BF_VAT_ACTUAL;
    }

    public void setREVENUE_BF_VAT_ACTUAL(Long REVENUE_BF_VAT_ACTUAL) {
        this.REVENUE_BF_VAT_ACTUAL = REVENUE_BF_VAT_ACTUAL;
    }
    @Column(name = "REVENUE_BF_VAT_APPROVE")
    public Long getREVENUE_BF_VAT_APPROVE() {
        return REVENUE_BF_VAT_APPROVE;
    }

    public void setREVENUE_BF_VAT_APPROVE(Long REVENUE_BF_VAT_APPROVE) {
        this.REVENUE_BF_VAT_APPROVE = REVENUE_BF_VAT_APPROVE;
    }
    @Column(name = "REVENUE_AT_VAT_APPROVE")
    public Long getREVENUE_AT_VAT_APPROVE() {
        return REVENUE_AT_VAT_APPROVE;
    }

    public void setREVENUE_AT_VAT_APPROVE(Long REVENUE_AT_VAT_APPROVE) {
        this.REVENUE_AT_VAT_APPROVE = REVENUE_AT_VAT_APPROVE;
    }
    @Column(name = "WAGE")
    public Long getWAGE() {
        return WAGE;
    }

    public void setWAGE(Long WAGE) {
        this.WAGE = WAGE;
    }
    @Column(name = "MATERIAL_TCT")
    public Long getMATERIAL_TCT() {
        return MATERIAL_TCT;
    }

    public void setMATERIAL_TCT(Long MATERIAL_TCT) {
        this.MATERIAL_TCT = MATERIAL_TCT;
    }
    @Column(name = "MATERIAL_CNCT")
    public Long getMATERIAL_CNCT() {
        return MATERIAL_CNCT;
    }

    public void setMATERIAL_CNCT(Long MATERIAL_CNCT) {
        this.MATERIAL_CNCT = MATERIAL_CNCT;
    }
    @Column(name = "LABOR")
    public Long getLABOR() {
        return LABOR;
    }

    public void setLABOR(Long LABOR) {
        this.LABOR = LABOR;
    }
    @Column(name = "STATUS")
    public Long getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(Long STATUS) {
        this.STATUS = STATUS;
    }
    @Column(name = "CREATE_DATE")
    public Date getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(Date CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }
    @Column(name = "USER_CREATED")
    public String getUSER_CREATED() {
        return USER_CREATED;
    }

    public void setUSER_CREATED(String USER_CREATED) {
        this.USER_CREATED = USER_CREATED;
    }
    @Column(name = "TRU_CONFIRM_BY")
    public String getTRU_CONFIRM_BY() {
        return TRU_CONFIRM_BY;
    }

    public void setTRU_CONFIRM_BY(String TRU_CONFIRM_BY) {
        this.TRU_CONFIRM_BY = TRU_CONFIRM_BY;
    }
    @Column(name = "TC_TRU_CONFIRM_BY")
    public String getTC_TRU_CONFIRM_BY() {
        return TC_TRU_CONFIRM_BY;
    }

    public void setTC_TRU_CONFIRM_BY(String TC_TRU_CONFIRM_BY) {
        this.TC_TRU_CONFIRM_BY = TC_TRU_CONFIRM_BY;
    }
    @Column(name = "TC_TCT_CONFIRM_BY")
    public String getTC_TCT_CONFIRM_BY() {
        return TC_TCT_CONFIRM_BY;
    }

    public void setTC_TCT_CONFIRM_BY(String TC_TCT_CONFIRM_BY) {
        this.TC_TCT_CONFIRM_BY = TC_TCT_CONFIRM_BY;
    }
    @Column(name = "TRU_CONFIRM_DATE")
    public Date getTRU_CONFIRM_DATE() {
        return TRU_CONFIRM_DATE;
    }

    public void setTRU_CONFIRM_DATE(Date TRU_CONFIRM_DATE) {
        this.TRU_CONFIRM_DATE = TRU_CONFIRM_DATE;
    }
    @Column(name = "TC_TRU_CONFIRM_DATE")
    public Date getTC_TRU_CONFIRM_DATE() {
        return TC_TRU_CONFIRM_DATE;
    }

    public void setTC_TRU_CONFIRM_DATE(Date TC_TRU_CONFIRM_DATE) {
        this.TC_TRU_CONFIRM_DATE = TC_TRU_CONFIRM_DATE;
    }
    @Column(name = "TC_TCT_CONFIRM_DATE")
    public Date getTC_TCT_CONFIRM_DATE() {
        return TC_TCT_CONFIRM_DATE;
    }

    public void setTC_TCT_CONFIRM_DATE(Date TC_TCT_CONFIRM_DATE) {
        this.TC_TCT_CONFIRM_DATE = TC_TCT_CONFIRM_DATE;
    }
    @Column(name = "STATE")
    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }
    @Column(name = "REVENUE_BF_VAT_SUGGEST")
    public Long getREVENUE_BF_VAT_SUGGEST() {
        return REVENUE_BF_VAT_SUGGEST;
    }

    public void setREVENUE_BF_VAT_SUGGEST(Long REVENUE_BF_VAT_SUGGEST) {
        this.REVENUE_BF_VAT_SUGGEST = REVENUE_BF_VAT_SUGGEST;
    }
    @Column(name = "REVENUE_AT_VAT_SUGGEST")
    public Long getREVENUE_AT_VAT_SUGGEST() {
        return REVENUE_AT_VAT_SUGGEST;
    }

    public void setREVENUE_AT_VAT_SUGGEST(Long REVENUE_AT_VAT_SUGGEST) {
        this.REVENUE_AT_VAT_SUGGEST = REVENUE_AT_VAT_SUGGEST;
    }
    @Column(name = "WAGE_SUGGEST")
    public Long getWAGE_SUGGEST() {
        return WAGE_SUGGEST;
    }

    public void setWAGE_SUGGEST(Long WAGE_SUGGEST) {
        this.WAGE_SUGGEST = WAGE_SUGGEST;
    }
    @Column(name = "MATERIAL_TCT_SUGGEST")
    public Long getMATERIAL_TCT_SUGGEST() {
        return MATERIAL_TCT_SUGGEST;
    }

    public void setMATERIAL_TCT_SUGGEST(Long MATERIAL_TCT_SUGGEST) {
        this.MATERIAL_TCT_SUGGEST = MATERIAL_TCT_SUGGEST;
    }
    @Column(name = "MATERIAL_CNCT_SUGGEST")
    public Long getMATERIAL_CNCT_SUGGEST() {
        return MATERIAL_CNCT_SUGGEST;
    }

    public void setMATERIAL_CNCT_SUGGEST(Long MATERIAL_CNCT_SUGGEST) {
        this.MATERIAL_CNCT_SUGGEST = MATERIAL_CNCT_SUGGEST;
    }
    @Column(name = "LABOR_SUGGEST")
    public Long getLABOR_SUGGEST() {
        return LABOR_SUGGEST;
    }

    public void setLABOR_SUGGEST(Long LABOR_SUGGEST) {
        this.LABOR_SUGGEST = LABOR_SUGGEST;
    }
    @Column(name = "HSHC_COST_SUGGEST")
    public Long getHSHC_COST_SUGGEST() {
        return HSHC_COST_SUGGEST;
    }

    public void setHSHC_COST_SUGGEST(Long HSHC_COST_SUGGEST) {
        this.HSHC_COST_SUGGEST = HSHC_COST_SUGGEST;
    }
    @Column(name = "COMMISSION_COST_SUGGEST")
    public Long getCOMMISSION_COST_SUGGEST() {
        return COMMISSION_COST_SUGGEST;
    }

    public void setCOMMISSION_COST_SUGGEST(Long COMMISSION_COST_SUGGEST) {
        this.COMMISSION_COST_SUGGEST = COMMISSION_COST_SUGGEST;
    }
    @Column(name = "OTHER_COST_SUGGEST")
    public Long getOTHER_COST_SUGGEST() {
        return OTHER_COST_SUGGEST;
    }

    public void setOTHER_COST_SUGGEST(Long OTHER_COST_SUGGEST) {
        this.OTHER_COST_SUGGEST = OTHER_COST_SUGGEST;
    }
    @Column(name = "VAT_SUGGEST")
    public Long getVAT_SUGGEST() {
        return VAT_SUGGEST;
    }

    public void setVAT_SUGGEST(Long VAT_SUGGEST) {
        this.VAT_SUGGEST = VAT_SUGGEST;
    }
    @Column(name = "PERCENT_SUGGEST")
    public Long getPERCENT_SUGGEST() {
        return PERCENT_SUGGEST;
    }

    public void setPERCENT_SUGGEST(Long PERCENT_SUGGEST) {
        this.PERCENT_SUGGEST = PERCENT_SUGGEST;
    }
    @Column(name = "DOC_NUMBER_SUGGEST")
    public String getDOC_NUMBER_SUGGEST() {
        return DOC_NUMBER_SUGGEST;
    }

    public void setDOC_NUMBER_SUGGEST(String DOC_NUMBER_SUGGEST) {
        this.DOC_NUMBER_SUGGEST = DOC_NUMBER_SUGGEST;
    }
    @Column(name = "TOTAL_COST_SUGGEST")
    public Long getTOTAL_COST_SUGGEST() {
        return TOTAL_COST_SUGGEST;
    }

    public void setTOTAL_COST_SUGGEST(Long TOTAL_COST_SUGGEST) {
        this.TOTAL_COST_SUGGEST = TOTAL_COST_SUGGEST;
    }
    @Column(name = "HSHC_COST")
    public Long getHSHC_COST() {
        return HSHC_COST;
    }

    public void setHSHC_COST(Long HSHC_COST) {
        this.HSHC_COST = HSHC_COST;
    }
    @Column(name = "COMMISSION_COST")
    public Long getCOMMISSION_COST() {
        return COMMISSION_COST;
    }

    public void setCOMMISSION_COST(Long COMMISSION_COST) {
        this.COMMISSION_COST = COMMISSION_COST;
    }
    @Column(name = "OTHER_COST")
    public Long getOTHER_COST() {
        return OTHER_COST;
    }

    public void setOTHER_COST(Long OTHER_COST) {
        this.OTHER_COST = OTHER_COST;
    }
    @Column(name = "VAT")
    public Long getVAT() {
        return VAT;
    }

    public void setVAT(Long VAT) {
        this.VAT = VAT;
    }
    @Column(name = "PERCENT")
    public Long getPERCENT() {
        return PERCENT;
    }

    public void setPERCENT(Long PERCENT) {
        this.PERCENT = PERCENT;
    }
    @Column(name = "TOTAL_COST")
    public Long getTOTAL_COST() {
        return TOTAL_COST;
    }

    public void setTOTAL_COST(Long TOTAL_COST) {
        this.TOTAL_COST = TOTAL_COST;
    }
    @Column(name = "DOC_NUMBER")
    public String getDOC_NUMBER() {
        return DOC_NUMBER;
    }

    public void setDOC_NUMBER(String DOC_NUMBER) {
        this.DOC_NUMBER = DOC_NUMBER;
    }
    @Column(name = "MACHINE")
    public Long getMACHINE() {
        return MACHINE;
    }

    public void setMACHINE(Long MACHINE) {
        this.MACHINE = MACHINE;
    }
    @Column(name = "MACHINE_SUGGEST")
    public Long getMACHINE_SUGGEST() {
        return MACHINE_SUGGEST;
    }

    public void setMACHINE_SUGGEST(Long MACHINE_SUGGEST) {
        this.MACHINE_SUGGEST = MACHINE_SUGGEST;
    }
    @Column(name = "BBBG_PATH")
    public String getBBBG_PATH() {
        return BBBG_PATH;
    }

    public void setBBBG_PATH(String BBBG_PATH) {
        this.BBBG_PATH = BBBG_PATH;
    }
    @Column(name = "BBBG_NAME")
    public String getBBBG_NAME() {
        return BBBG_NAME;
    }

    public void setBBBG_NAME(String BBBG_NAME) {
        this.BBBG_NAME = BBBG_NAME;
    }
    @Column(name = "BBQTCDT_PATH")
    public String getBBQTCDT_PATH() {
        return BBQTCDT_PATH;
    }

    public void setBBQTCDT_PATH(String BBQTCDT_PATH) {
        this.BBQTCDT_PATH = BBQTCDT_PATH;
    }
    @Column(name = "BBQTCDT_NAME")
    public String getBBQTCDT_NAME() {
        return BBQTCDT_NAME;
    }

    public void setBBQTCDT_NAME(String BBQTCDT_NAME) {
        this.BBQTCDT_NAME = BBQTCDT_NAME;
    }
    @Column(name = "TT_NAME")
    public String getTT_NAME() {
        return TT_NAME;
    }

    public void setTT_NAME(String TT_NAME) {
        this.TT_NAME = TT_NAME;
    }
    @Column(name = "TT_PATH")
    public String getTT_PATH() {
        return TT_PATH;
    }

    public void setTT_PATH(String TT_PATH) {
        this.TT_PATH = TT_PATH;
    }


    @Override
    public WO_DOANHTHU_GPTH_CHECKLIST_DTO toDTO() {
        WO_DOANHTHU_GPTH_CHECKLIST_DTO DoanhThuCheckListDTO = new WO_DOANHTHU_GPTH_CHECKLIST_DTO();
        DoanhThuCheckListDTO.setId(this.id);
        DoanhThuCheckListDTO.setWO_ID(this.WO_ID);
        DoanhThuCheckListDTO.setCONFIRM(this.CONFIRM);
        DoanhThuCheckListDTO.setCD_CONFIRM_BY(this.CD_CONFIRM_BY);
        DoanhThuCheckListDTO.setNAME(this.NAME);
        DoanhThuCheckListDTO.setCD_CONFIRM_DATE(this.CD_CONFIRM_DATE);
        DoanhThuCheckListDTO.setSTART_DATE_PLAN(this.START_DATE_PLAN);
        DoanhThuCheckListDTO.setEND_DATE_PLAN(this.END_DATE_PLAN);
        DoanhThuCheckListDTO.setSTART_DATE_ACTUAL(this.START_DATE_ACTUAL);
        DoanhThuCheckListDTO.setEND_DATE_ACTUAL(this.END_DATE_ACTUAL);
        DoanhThuCheckListDTO.setREVENUE_BF_VAT_PLAN(this.REVENUE_BF_VAT_PLAN);
        DoanhThuCheckListDTO.setREVENUE_BF_VAT_ACTUAL(this.REVENUE_BF_VAT_ACTUAL);
        DoanhThuCheckListDTO.setREVENUE_BF_VAT_APPROVE(this.REVENUE_BF_VAT_APPROVE);
        DoanhThuCheckListDTO.setREVENUE_AT_VAT_APPROVE(this.REVENUE_AT_VAT_APPROVE);
        DoanhThuCheckListDTO.setWAGE(this.WAGE);
        DoanhThuCheckListDTO.setMATERIAL_TCT(this.MATERIAL_TCT);
        DoanhThuCheckListDTO.setMATERIAL_CNCT(this.MATERIAL_CNCT);
        DoanhThuCheckListDTO.setLABOR(this.LABOR);
        DoanhThuCheckListDTO.setSTATUS(this.STATUS);
        DoanhThuCheckListDTO.setCREATE_DATE(this.CREATE_DATE);
        DoanhThuCheckListDTO.setUSER_CREATED(this.USER_CREATED);
        DoanhThuCheckListDTO.setTRU_CONFIRM_BY(this.TRU_CONFIRM_BY);
        DoanhThuCheckListDTO.setTC_TRU_CONFIRM_BY(this.TC_TRU_CONFIRM_BY);
        DoanhThuCheckListDTO.setTC_TCT_CONFIRM_BY(this.TC_TCT_CONFIRM_BY);
        DoanhThuCheckListDTO.setTRU_CONFIRM_DATE(this.TRU_CONFIRM_DATE);
        DoanhThuCheckListDTO.setTC_TRU_CONFIRM_DATE(this.TC_TRU_CONFIRM_DATE);
        DoanhThuCheckListDTO.setTC_TCT_CONFIRM_DATE(this.TC_TCT_CONFIRM_DATE);
        DoanhThuCheckListDTO.setSTATE(this.STATE);
        DoanhThuCheckListDTO.setREVENUE_BF_VAT_SUGGEST(this.REVENUE_BF_VAT_SUGGEST);
        DoanhThuCheckListDTO.setREVENUE_AT_VAT_SUGGEST(this.REVENUE_AT_VAT_SUGGEST);
        DoanhThuCheckListDTO.setWAGE_SUGGEST(this.WAGE_SUGGEST);
        DoanhThuCheckListDTO.setMATERIAL_TCT_SUGGEST(this.MATERIAL_TCT_SUGGEST);
        DoanhThuCheckListDTO.setMATERIAL_CNCT_SUGGEST(this.MATERIAL_CNCT_SUGGEST);
        DoanhThuCheckListDTO.setLABOR_SUGGEST(this.LABOR_SUGGEST);
        DoanhThuCheckListDTO.setHSHC_COST_SUGGEST(this.HSHC_COST_SUGGEST);
        DoanhThuCheckListDTO.setCOMMISSION_COST_SUGGEST(this.COMMISSION_COST_SUGGEST);
        DoanhThuCheckListDTO.setOTHER_COST_SUGGEST(this.OTHER_COST_SUGGEST);
        DoanhThuCheckListDTO.setVAT_SUGGEST(this.VAT_SUGGEST);
        DoanhThuCheckListDTO.setPERCENT_SUGGEST(this.PERCENT_SUGGEST);
        DoanhThuCheckListDTO.setDOC_NUMBER_SUGGEST(this.DOC_NUMBER_SUGGEST);
        DoanhThuCheckListDTO.setTOTAL_COST_SUGGEST(this.TOTAL_COST_SUGGEST);
        DoanhThuCheckListDTO.setHSHC_COST(this.HSHC_COST);
        DoanhThuCheckListDTO.setCOMMISSION_COST(this.COMMISSION_COST);
        DoanhThuCheckListDTO.setOTHER_COST(this.OTHER_COST);
        DoanhThuCheckListDTO.setVAT(this.VAT);
        DoanhThuCheckListDTO.setPERCENT(this.PERCENT);
        DoanhThuCheckListDTO.setTOTAL_COST(this.TOTAL_COST);
        DoanhThuCheckListDTO.setDOC_NUMBER(this.DOC_NUMBER);
        DoanhThuCheckListDTO.setMACHINE(this.MACHINE);
        DoanhThuCheckListDTO.setMACHINE_SUGGEST(this.MACHINE_SUGGEST);
        DoanhThuCheckListDTO.setBBBG_PATH(this.BBBG_PATH);
        DoanhThuCheckListDTO.setBBBG_NAME(this.BBBG_NAME);
        DoanhThuCheckListDTO.setBBQTCDT_PATH(this.BBQTCDT_PATH);
        DoanhThuCheckListDTO.setBBQTCDT_NAME(this.BBQTCDT_NAME);
        DoanhThuCheckListDTO.setTT_NAME(this.TT_NAME);
        DoanhThuCheckListDTO.setTT_PATH(this.TT_PATH);
        return DoanhThuCheckListDTO;
    }
}
