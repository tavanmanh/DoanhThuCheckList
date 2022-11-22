package com.viettel.coms.dto;


import com.viettel.coms.bo.WO_DOANHTHU_GPTH_CHECKLIST_BO;
import com.viettel.coms.bo.WoBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WO_DOANHTHU_GPTH_CHECKLIST_DTO extends ComsBaseFWDTO<WO_DOANHTHU_GPTH_CHECKLIST_BO>{
    private Long id;
    private Long WO_ID;
    private Long CONFIRM;
    private String CD_CONFIRM_BY;
    private String NAME;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date CD_CONFIRM_DATE;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date START_DATE_PLAN;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date END_DATE_PLAN;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date START_DATE_ACTUAL;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
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
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date CREATE_DATE;
    private String USER_CREATED;
    private String TRU_CONFIRM_BY;
    private String TC_TRU_CONFIRM_BY;
    private String TC_TCT_CONFIRM_BY;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date TRU_CONFIRM_DATE;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date TC_TRU_CONFIRM_DATE;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWO_ID() {
        return WO_ID;
    }

    public void setWO_ID(Long WO_ID) {
        this.WO_ID = WO_ID;
    }

    public Long getCONFIRM() {
        return CONFIRM;
    }

    public void setCONFIRM(Long CONFIRM) {
        this.CONFIRM = CONFIRM;
    }

    public String getCD_CONFIRM_BY() {
        return CD_CONFIRM_BY;
    }

    public void setCD_CONFIRM_BY(String CD_CONFIRM_BY) {
        this.CD_CONFIRM_BY = CD_CONFIRM_BY;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Date getCD_CONFIRM_DATE() {
        return CD_CONFIRM_DATE;
    }

    public void setCD_CONFIRM_DATE(Date CD_CONFIRM_DATE) {
        this.CD_CONFIRM_DATE = CD_CONFIRM_DATE;
    }

    public Date getSTART_DATE_PLAN() {
        return START_DATE_PLAN;
    }

    public void setSTART_DATE_PLAN(Date START_DATE_PLAN) {
        this.START_DATE_PLAN = START_DATE_PLAN;
    }

    public Date getEND_DATE_PLAN() {
        return END_DATE_PLAN;
    }

    public void setEND_DATE_PLAN(Date END_DATE_PLAN) {
        this.END_DATE_PLAN = END_DATE_PLAN;
    }

    public Date getSTART_DATE_ACTUAL() {
        return START_DATE_ACTUAL;
    }

    public void setSTART_DATE_ACTUAL(Date START_DATE_ACTUAL) {
        this.START_DATE_ACTUAL = START_DATE_ACTUAL;
    }

    public Date getEND_DATE_ACTUAL() {
        return END_DATE_ACTUAL;
    }

    public void setEND_DATE_ACTUAL(Date END_DATE_ACTUAL) {
        this.END_DATE_ACTUAL = END_DATE_ACTUAL;
    }

    public Long getREVENUE_BF_VAT_PLAN() {
        return REVENUE_BF_VAT_PLAN;
    }

    public void setREVENUE_BF_VAT_PLAN(Long REVENUE_BF_VAT_PLAN) {
        this.REVENUE_BF_VAT_PLAN = REVENUE_BF_VAT_PLAN;
    }

    public Long getREVENUE_BF_VAT_ACTUAL() {
        return REVENUE_BF_VAT_ACTUAL;
    }

    public void setREVENUE_BF_VAT_ACTUAL(Long REVENUE_BF_VAT_ACTUAL) {
        this.REVENUE_BF_VAT_ACTUAL = REVENUE_BF_VAT_ACTUAL;
    }

    public Long getREVENUE_BF_VAT_APPROVE() {
        return REVENUE_BF_VAT_APPROVE;
    }

    public void setREVENUE_BF_VAT_APPROVE(Long REVENUE_BF_VAT_APPROVE) {
        this.REVENUE_BF_VAT_APPROVE = REVENUE_BF_VAT_APPROVE;
    }

    public Long getREVENUE_AT_VAT_APPROVE() {
        return REVENUE_AT_VAT_APPROVE;
    }

    public void setREVENUE_AT_VAT_APPROVE(Long REVENUE_AT_VAT_APPROVE) {
        this.REVENUE_AT_VAT_APPROVE = REVENUE_AT_VAT_APPROVE;
    }

    public Long getWAGE() {
        return WAGE;
    }

    public void setWAGE(Long WAGE) {
        this.WAGE = WAGE;
    }

    public Long getMATERIAL_TCT() {
        return MATERIAL_TCT;
    }

    public void setMATERIAL_TCT(Long MATERIAL_TCT) {
        this.MATERIAL_TCT = MATERIAL_TCT;
    }

    public Long getMATERIAL_CNCT() {
        return MATERIAL_CNCT;
    }

    public void setMATERIAL_CNCT(Long MATERIAL_CNCT) {
        this.MATERIAL_CNCT = MATERIAL_CNCT;
    }

    public Long getLABOR() {
        return LABOR;
    }

    public void setLABOR(Long LABOR) {
        this.LABOR = LABOR;
    }

    public Long getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(Long STATUS) {
        this.STATUS = STATUS;
    }

    public Date getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(Date CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public String getUSER_CREATED() {
        return USER_CREATED;
    }

    public void setUSER_CREATED(String USER_CREATED) {
        this.USER_CREATED = USER_CREATED;
    }

    public String getTRU_CONFIRM_BY() {
        return TRU_CONFIRM_BY;
    }

    public void setTRU_CONFIRM_BY(String TRU_CONFIRM_BY) {
        this.TRU_CONFIRM_BY = TRU_CONFIRM_BY;
    }

    public String getTC_TRU_CONFIRM_BY() {
        return TC_TRU_CONFIRM_BY;
    }

    public void setTC_TRU_CONFIRM_BY(String TC_TRU_CONFIRM_BY) {
        this.TC_TRU_CONFIRM_BY = TC_TRU_CONFIRM_BY;
    }

    public String getTC_TCT_CONFIRM_BY() {
        return TC_TCT_CONFIRM_BY;
    }

    public void setTC_TCT_CONFIRM_BY(String TC_TCT_CONFIRM_BY) {
        this.TC_TCT_CONFIRM_BY = TC_TCT_CONFIRM_BY;
    }

    public Date getTRU_CONFIRM_DATE() {
        return TRU_CONFIRM_DATE;
    }

    public void setTRU_CONFIRM_DATE(Date TRU_CONFIRM_DATE) {
        this.TRU_CONFIRM_DATE = TRU_CONFIRM_DATE;
    }

    public Date getTC_TRU_CONFIRM_DATE() {
        return TC_TRU_CONFIRM_DATE;
    }

    public void setTC_TRU_CONFIRM_DATE(Date TC_TRU_CONFIRM_DATE) {
        this.TC_TRU_CONFIRM_DATE = TC_TRU_CONFIRM_DATE;
    }

    public Date getTC_TCT_CONFIRM_DATE() {
        return TC_TCT_CONFIRM_DATE;
    }

    public void setTC_TCT_CONFIRM_DATE(Date TC_TCT_CONFIRM_DATE) {
        this.TC_TCT_CONFIRM_DATE = TC_TCT_CONFIRM_DATE;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public Long getREVENUE_BF_VAT_SUGGEST() {
        return REVENUE_BF_VAT_SUGGEST;
    }

    public void setREVENUE_BF_VAT_SUGGEST(Long REVENUE_BF_VAT_SUGGEST) {
        this.REVENUE_BF_VAT_SUGGEST = REVENUE_BF_VAT_SUGGEST;
    }

    public Long getREVENUE_AT_VAT_SUGGEST() {
        return REVENUE_AT_VAT_SUGGEST;
    }

    public void setREVENUE_AT_VAT_SUGGEST(Long REVENUE_AT_VAT_SUGGEST) {
        this.REVENUE_AT_VAT_SUGGEST = REVENUE_AT_VAT_SUGGEST;
    }

    public Long getWAGE_SUGGEST() {
        return WAGE_SUGGEST;
    }

    public void setWAGE_SUGGEST(Long WAGE_SUGGEST) {
        this.WAGE_SUGGEST = WAGE_SUGGEST;
    }

    public Long getMATERIAL_TCT_SUGGEST() {
        return MATERIAL_TCT_SUGGEST;
    }

    public void setMATERIAL_TCT_SUGGEST(Long MATERIAL_TCT_SUGGEST) {
        this.MATERIAL_TCT_SUGGEST = MATERIAL_TCT_SUGGEST;
    }

    public Long getMATERIAL_CNCT_SUGGEST() {
        return MATERIAL_CNCT_SUGGEST;
    }

    public void setMATERIAL_CNCT_SUGGEST(Long MATERIAL_CNCT_SUGGEST) {
        this.MATERIAL_CNCT_SUGGEST = MATERIAL_CNCT_SUGGEST;
    }

    public Long getLABOR_SUGGEST() {
        return LABOR_SUGGEST;
    }

    public void setLABOR_SUGGEST(Long LABOR_SUGGEST) {
        this.LABOR_SUGGEST = LABOR_SUGGEST;
    }

    public Long getHSHC_COST_SUGGEST() {
        return HSHC_COST_SUGGEST;
    }

    public void setHSHC_COST_SUGGEST(Long HSHC_COST_SUGGEST) {
        this.HSHC_COST_SUGGEST = HSHC_COST_SUGGEST;
    }

    public Long getCOMMISSION_COST_SUGGEST() {
        return COMMISSION_COST_SUGGEST;
    }

    public void setCOMMISSION_COST_SUGGEST(Long COMMISSION_COST_SUGGEST) {
        this.COMMISSION_COST_SUGGEST = COMMISSION_COST_SUGGEST;
    }

    public Long getOTHER_COST_SUGGEST() {
        return OTHER_COST_SUGGEST;
    }

    public void setOTHER_COST_SUGGEST(Long OTHER_COST_SUGGEST) {
        this.OTHER_COST_SUGGEST = OTHER_COST_SUGGEST;
    }

    public Long getVAT_SUGGEST() {
        return VAT_SUGGEST;
    }

    public void setVAT_SUGGEST(Long VAT_SUGGEST) {
        this.VAT_SUGGEST = VAT_SUGGEST;
    }

    public Long getPERCENT_SUGGEST() {
        return PERCENT_SUGGEST;
    }

    public void setPERCENT_SUGGEST(Long PERCENT_SUGGEST) {
        this.PERCENT_SUGGEST = PERCENT_SUGGEST;
    }

    public String getDOC_NUMBER_SUGGEST() {
        return DOC_NUMBER_SUGGEST;
    }

    public void setDOC_NUMBER_SUGGEST(String DOC_NUMBER_SUGGEST) {
        this.DOC_NUMBER_SUGGEST = DOC_NUMBER_SUGGEST;
    }

    public Long getTOTAL_COST_SUGGEST() {
        return TOTAL_COST_SUGGEST;
    }

    public void setTOTAL_COST_SUGGEST(Long TOTAL_COST_SUGGEST) {
        this.TOTAL_COST_SUGGEST = TOTAL_COST_SUGGEST;
    }

    public Long getHSHC_COST() {
        return HSHC_COST;
    }

    public void setHSHC_COST(Long HSHC_COST) {
        this.HSHC_COST = HSHC_COST;
    }

    public Long getCOMMISSION_COST() {
        return COMMISSION_COST;
    }

    public void setCOMMISSION_COST(Long COMMISSION_COST) {
        this.COMMISSION_COST = COMMISSION_COST;
    }

    public Long getOTHER_COST() {
        return OTHER_COST;
    }

    public void setOTHER_COST(Long OTHER_COST) {
        this.OTHER_COST = OTHER_COST;
    }

    public Long getVAT() {
        return VAT;
    }

    public void setVAT(Long VAT) {
        this.VAT = VAT;
    }

    public Long getPERCENT() {
        return PERCENT;
    }

    public void setPERCENT(Long PERCENT) {
        this.PERCENT = PERCENT;
    }

    public Long getTOTAL_COST() {
        return TOTAL_COST;
    }

    public void setTOTAL_COST(Long TOTAL_COST) {
        this.TOTAL_COST = TOTAL_COST;
    }

    public String getDOC_NUMBER() {
        return DOC_NUMBER;
    }

    public void setDOC_NUMBER(String DOC_NUMBER) {
        this.DOC_NUMBER = DOC_NUMBER;
    }

    public Long getMACHINE() {
        return MACHINE;
    }

    public void setMACHINE(Long MACHINE) {
        this.MACHINE = MACHINE;
    }

    public Long getMACHINE_SUGGEST() {
        return MACHINE_SUGGEST;
    }

    public void setMACHINE_SUGGEST(Long MACHINE_SUGGEST) {
        this.MACHINE_SUGGEST = MACHINE_SUGGEST;
    }

    public String getBBBG_PATH() {
        return BBBG_PATH;
    }

    public void setBBBG_PATH(String BBBG_PATH) {
        this.BBBG_PATH = BBBG_PATH;
    }

    public String getBBBG_NAME() {
        return BBBG_NAME;
    }

    public void setBBBG_NAME(String BBBG_NAME) {
        this.BBBG_NAME = BBBG_NAME;
    }

    public String getBBQTCDT_PATH() {
        return BBQTCDT_PATH;
    }

    public void setBBQTCDT_PATH(String BBQTCDT_PATH) {
        this.BBQTCDT_PATH = BBQTCDT_PATH;
    }

    public String getBBQTCDT_NAME() {
        return BBQTCDT_NAME;
    }

    public void setBBQTCDT_NAME(String BBQTCDT_NAME) {
        this.BBQTCDT_NAME = BBQTCDT_NAME;
    }

    public String getTT_NAME() {
        return TT_NAME;
    }

    public void setTT_NAME(String TT_NAME) {
        this.TT_NAME = TT_NAME;
    }

    public String getTT_PATH() {
        return TT_PATH;
    }

    public void setTT_PATH(String TT_PATH) {
        this.TT_PATH = TT_PATH;
    }

    @Override
    public WO_DOANHTHU_GPTH_CHECKLIST_BO toModel() {
        WO_DOANHTHU_GPTH_CHECKLIST_BO DoanhThuCheckListBO = new WO_DOANHTHU_GPTH_CHECKLIST_BO();
        DoanhThuCheckListBO.setId(this.id);
        DoanhThuCheckListBO.setWO_ID(this.WO_ID);
        DoanhThuCheckListBO.setCONFIRM(this.CONFIRM);
        DoanhThuCheckListBO.setCD_CONFIRM_BY(this.CD_CONFIRM_BY);
        DoanhThuCheckListBO.setNAME(this.NAME);
        DoanhThuCheckListBO.setCD_CONFIRM_DATE(this.CD_CONFIRM_DATE);
        DoanhThuCheckListBO.setSTART_DATE_PLAN(this.START_DATE_PLAN);
        DoanhThuCheckListBO.setEND_DATE_PLAN(this.END_DATE_PLAN);
        DoanhThuCheckListBO.setSTART_DATE_ACTUAL(this.START_DATE_ACTUAL);
        DoanhThuCheckListBO.setEND_DATE_ACTUAL(this.END_DATE_ACTUAL);
        DoanhThuCheckListBO.setREVENUE_BF_VAT_PLAN(this.REVENUE_BF_VAT_PLAN);
        DoanhThuCheckListBO.setREVENUE_BF_VAT_ACTUAL(this.REVENUE_BF_VAT_ACTUAL);
        DoanhThuCheckListBO.setREVENUE_BF_VAT_APPROVE(this.REVENUE_BF_VAT_APPROVE);
        DoanhThuCheckListBO.setREVENUE_AT_VAT_APPROVE(this.REVENUE_AT_VAT_APPROVE);
        DoanhThuCheckListBO.setWAGE(this.WAGE);
        DoanhThuCheckListBO.setMATERIAL_TCT(this.MATERIAL_TCT);
        DoanhThuCheckListBO.setMATERIAL_CNCT(this.MATERIAL_CNCT);
        DoanhThuCheckListBO.setLABOR(this.LABOR);
        DoanhThuCheckListBO.setSTATUS(this.STATUS);
        DoanhThuCheckListBO.setCREATE_DATE(this.CREATE_DATE);
        DoanhThuCheckListBO.setUSER_CREATED(this.USER_CREATED);
        DoanhThuCheckListBO.setTRU_CONFIRM_BY(this.TRU_CONFIRM_BY);
        DoanhThuCheckListBO.setTC_TRU_CONFIRM_BY(this.TC_TRU_CONFIRM_BY);
        DoanhThuCheckListBO.setTC_TCT_CONFIRM_BY(this.TC_TCT_CONFIRM_BY);
        DoanhThuCheckListBO.setTRU_CONFIRM_DATE(this.TRU_CONFIRM_DATE);
        DoanhThuCheckListBO.setTC_TRU_CONFIRM_DATE(this.TC_TRU_CONFIRM_DATE);
        DoanhThuCheckListBO.setTC_TCT_CONFIRM_DATE(this.TC_TCT_CONFIRM_DATE);
        DoanhThuCheckListBO.setSTATE(this.STATE);
        DoanhThuCheckListBO.setREVENUE_BF_VAT_SUGGEST(this.REVENUE_BF_VAT_SUGGEST);
        DoanhThuCheckListBO.setREVENUE_AT_VAT_SUGGEST(this.REVENUE_AT_VAT_SUGGEST);
        DoanhThuCheckListBO.setWAGE_SUGGEST(this.WAGE_SUGGEST);
        DoanhThuCheckListBO.setMATERIAL_TCT_SUGGEST(this.MATERIAL_TCT_SUGGEST);
        DoanhThuCheckListBO.setMATERIAL_CNCT_SUGGEST(this.MATERIAL_CNCT_SUGGEST);
        DoanhThuCheckListBO.setLABOR_SUGGEST(this.LABOR_SUGGEST);
        DoanhThuCheckListBO.setHSHC_COST_SUGGEST(this.HSHC_COST_SUGGEST);
        DoanhThuCheckListBO.setCOMMISSION_COST_SUGGEST(this.COMMISSION_COST_SUGGEST);
        DoanhThuCheckListBO.setOTHER_COST_SUGGEST(this.OTHER_COST_SUGGEST);
        DoanhThuCheckListBO.setVAT_SUGGEST(this.VAT_SUGGEST);
        DoanhThuCheckListBO.setPERCENT_SUGGEST(this.PERCENT_SUGGEST);
        DoanhThuCheckListBO.setDOC_NUMBER_SUGGEST(this.DOC_NUMBER_SUGGEST);
        DoanhThuCheckListBO.setTOTAL_COST_SUGGEST(this.TOTAL_COST_SUGGEST);
        DoanhThuCheckListBO.setHSHC_COST(this.HSHC_COST);
        DoanhThuCheckListBO.setCOMMISSION_COST(this.COMMISSION_COST);
        DoanhThuCheckListBO.setOTHER_COST(this.OTHER_COST);
        DoanhThuCheckListBO.setVAT(this.VAT);
        DoanhThuCheckListBO.setPERCENT(this.PERCENT);
        DoanhThuCheckListBO.setTOTAL_COST(this.TOTAL_COST);
        DoanhThuCheckListBO.setDOC_NUMBER(this.DOC_NUMBER);
        DoanhThuCheckListBO.setMACHINE(this.MACHINE);
        DoanhThuCheckListBO.setMACHINE_SUGGEST(this.MACHINE_SUGGEST);
        DoanhThuCheckListBO.setBBBG_PATH(this.BBBG_PATH);
        DoanhThuCheckListBO.setBBBG_NAME(this.BBBG_NAME);
        DoanhThuCheckListBO.setBBQTCDT_PATH(this.BBQTCDT_PATH);
        DoanhThuCheckListBO.setBBQTCDT_NAME(this.BBQTCDT_NAME);
        DoanhThuCheckListBO.setTT_NAME(this.TT_NAME);
        DoanhThuCheckListBO.setTT_PATH(this.TT_PATH);
        return DoanhThuCheckListBO;
    }

    @Override
    public Long getFWModelId() {
        return null;
    }

    @Override
    public String catchName() {
        return null;
    }
}
