package com.viettel.coms.bo;

import com.viettel.coms.dto.WoOverdueReasonDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WO_OVERDUE_REASON")
/**
 *
 * @author: PicassoHoang
 * @version: 1.0
 * @since: 1.0
 */

public class WoOverdueReasonBO extends BaseFWModelImpl {

    private Long overdueReasonId;
    private Long woId;

    private String reasonLevel2;
    private Date reasonDateLevel2;
    private String approveStateLevel2;
    private Long approveUserIdLevel2;
    private Date approveDateLevel2;

    private String reasonLevel3;
    private Date reasonDateLevel3;
    private String approveStateLevel3;
    private Long approveUserIdLevel3;
    private Date approveDateLevel3;

    private String reasonLevel4;
    private Date reasonDateLevel4;
    private String approveStateLevel4;
    private Long approveUserIdLevel4;
    private Date approveDateLevel4;

    private String reasonLevel5;
    private Date reasonDateLevel5;
    private String approveStateLevel5;
    private Long approveUserIdLevel5;
    private Date approveDateLevel5;

    private String reasonLevelFt;
    private Date reasonDateLevelFt;
    private String approveStateLevelFt;
    private Long approveUserIdLevelFt;
    private Date approveDateLevelFt;

    private Date createdDate;
    private Long status;

    private Long reasonUserIdLevel2;
    private Long reasonUserIdLevel3;
    private Long reasonUserIdLevel4;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
    @Parameter(name = "sequence", value = "WO_OVERDUE_REASON_SEQ")})
    @Column(name = "ID")
    public Long getOverdueReasonId() {
        return overdueReasonId;
    }

    public void setOverdueReasonId(Long overdueReasonId) {
        this.overdueReasonId = overdueReasonId;
    }

    @Column(name = "WO_ID")
    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    @Column(name = "REASON_LEVEL_2")
    public String getReasonLevel2() {
        return reasonLevel2;
    }

    public void setReasonLevel2(String reasonLevel2) {
        this.reasonLevel2 = reasonLevel2;
    }

    @Column(name = "REASON_DATE_LEVEL_2")
    public Date getReasonDateLevel2() {
        return reasonDateLevel2;
    }

    public void setReasonDateLevel2(Date reasonDateLevel2) {
        this.reasonDateLevel2 = reasonDateLevel2;
    }

    @Column(name = "APPROVE_STATE_LEVEL_2")
    public String getApproveStateLevel2() {
        return approveStateLevel2;
    }

    public void setApproveStateLevel2(String approveStateLevel2) {
        this.approveStateLevel2 = approveStateLevel2;
    }

    @Column(name = "APPROVE_USER_ID_LEVEL_2")
    public Long getApproveUserIdLevel2() {
        return approveUserIdLevel2;
    }

    public void setApproveUserIdLevel2(Long approveUserIdLevel2) {
        this.approveUserIdLevel2 = approveUserIdLevel2;
    }

    @Column(name = "APPROVE_DATE_LEVEL_2")
    public Date getApproveDateLevel2() {
        return approveDateLevel2;
    }

    public void setApproveDateLevel2(Date approveDateLevel2) {
        this.approveDateLevel2 = approveDateLevel2;
    }

    @Column(name = "REASON_LEVEL_3")
    public String getReasonLevel3() {
        return reasonLevel3;
    }

    public void setReasonLevel3(String reasonLevel3) {
        this.reasonLevel3 = reasonLevel3;
    }

    @Column(name = "REASON_DATE_LEVEL_3")
    public Date getReasonDateLevel3() {
        return reasonDateLevel3;
    }

    public void setReasonDateLevel3(Date reasonDateLevel3) {
        this.reasonDateLevel3 = reasonDateLevel3;
    }

    @Column(name = "APPROVE_STATE_LEVEL_3")
    public String getApproveStateLevel3() {
        return approveStateLevel3;
    }

    public void setApproveStateLevel3(String approveStateLevel3) {
        this.approveStateLevel3 = approveStateLevel3;
    }

    @Column(name = "APPROVE_USER_ID_LEVEL_3")
    public Long getApproveUserIdLevel3() {
        return approveUserIdLevel3;
    }

    public void setApproveUserIdLevel3(Long approveUserIdLevel3) {
        this.approveUserIdLevel3 = approveUserIdLevel3;
    }

    @Column(name = "APPROVE_DATE_LEVEL_3")
    public Date getApproveDateLevel3() {
        return approveDateLevel3;
    }

    public void setApproveDateLevel3(Date approveDateLevel3) {
        this.approveDateLevel3 = approveDateLevel3;
    }

    @Column(name = "REASON_LEVEL_4")
    public String getReasonLevel4() {
        return reasonLevel4;
    }

    public void setReasonLevel4(String reasonLevel4) {
        this.reasonLevel4 = reasonLevel4;
    }

    @Column(name = "REASON_DATE_LEVEL_4")
    public Date getReasonDateLevel4() {
        return reasonDateLevel4;
    }

    public void setReasonDateLevel4(Date reasonDateLevel4) {
        this.reasonDateLevel4 = reasonDateLevel4;
    }

    @Column(name = "APPROVE_STATE_LEVEL_4")
    public String getApproveStateLevel4() {
        return approveStateLevel4;
    }

    public void setApproveStateLevel4(String approveStateLevel4) {
        this.approveStateLevel4 = approveStateLevel4;
    }

    @Column(name = "APPROVE_USER_ID_LEVEL_4")
    public Long getApproveUserIdLevel4() {
        return approveUserIdLevel4;
    }

    public void setApproveUserIdLevel4(Long approveUserIdLevel4) {
        this.approveUserIdLevel4 = approveUserIdLevel4;
    }

    @Column(name = "APPROVE_DATE_LEVEL_4")
    public Date getApproveDateLevel4() {
        return approveDateLevel4;
    }

    public void setApproveDateLevel4(Date approveDateLevel4) {
        this.approveDateLevel4 = approveDateLevel4;
    }

    @Column(name = "REASON_LEVEL_5")
    public String getReasonLevel5() {
        return reasonLevel5;
    }

    public void setReasonLevel5(String reasonLevel5) {
        this.reasonLevel5 = reasonLevel5;
    }

    @Column(name = "REASON_DATE_LEVEL_5")
    public Date getReasonDateLevel5() {
        return reasonDateLevel5;
    }

    public void setReasonDateLevel5(Date reasonDateLevel5) {
        this.reasonDateLevel5 = reasonDateLevel5;
    }

    @Column(name = "APPROVE_STATE_LEVEL_5")
    public String getApproveStateLevel5() {
        return approveStateLevel5;
    }

    public void setApproveStateLevel5(String approveStateLevel5) {
        this.approveStateLevel5 = approveStateLevel5;
    }

    @Column(name = "APPROVE_USER_ID_LEVEL_5")
    public Long getApproveUserIdLevel5() {
        return approveUserIdLevel5;
    }

    public void setApproveUserIdLevel5(Long approveUserIdLevel5) {
        this.approveUserIdLevel5 = approveUserIdLevel5;
    }

    @Column(name = "APPROVE_DATE_LEVEL_5")
    public Date getApproveDateLevel5() {
        return approveDateLevel5;
    }

    public void setApproveDateLevel5(Date approveDateLevel5) {
        this.approveDateLevel5 = approveDateLevel5;
    }

    @Column(name = "REASON_FT")
    public String getReasonLevelFt() {
        return reasonLevelFt;
    }

    public void setReasonLevelFt(String reasonLevelFt) {
        this.reasonLevelFt = reasonLevelFt;
    }

    @Column(name = "REASON_DATE_FT")
    public Date getReasonDateLevelFt() {
        return reasonDateLevelFt;
    }

    public void setReasonDateLevelFt(Date reasonDateLevelFt) {
        this.reasonDateLevelFt = reasonDateLevelFt;
    }

    @Column(name = "APPROVE_STATE_FT")
    public String getApproveStateLevelFt() {
        return approveStateLevelFt;
    }

    public void setApproveStateLevelFt(String approveStateLevelFt) {
        this.approveStateLevelFt = approveStateLevelFt;
    }

    @Column(name = "APPROVE_USER_ID_FT")
    public Long getApproveUserIdLevelFt() {
        return approveUserIdLevelFt;
    }

    public void setApproveUserIdLevelFt(Long approveUserIdLevelFt) {
        this.approveUserIdLevelFt = approveUserIdLevelFt;
    }

    @Column(name = "APPROVE_DATE_FT")
    public Date getApproveDateLevelFt() {
        return approveDateLevelFt;
    }

    public void setApproveDateLevelFt(Date approveDateLevelFt) {
        this.approveDateLevelFt = approveDateLevelFt;
    }

    @Column(name = "CREATED_DATE")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "STATUS")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "REASON_USER_ID_LEVEL_2")
    public Long getReasonUserIdLevel2() {
        return reasonUserIdLevel2;
    }

    public void setReasonUserIdLevel2(Long reasonUserIdLevel2) {
        this.reasonUserIdLevel2 = reasonUserIdLevel2;
    }

    @Column(name = "REASON_USER_ID_LEVEL_3")
    public Long getReasonUserIdLevel3() {
        return reasonUserIdLevel3;
    }

    public void setReasonUserIdLevel3(Long reasonUserIdLevel3) {
        this.reasonUserIdLevel3 = reasonUserIdLevel3;
    }

    @Column(name = "REASON_USER_ID_LEVEL_4")
    public Long getReasonUserIdLevel4() {
        return reasonUserIdLevel4;
    }

    public void setReasonUserIdLevel4(Long reasonUserIdLevel4) {
        this.reasonUserIdLevel4 = reasonUserIdLevel4;
    }

    @Override
    public WoOverdueReasonDTO toDTO() {
        WoOverdueReasonDTO dto = new WoOverdueReasonDTO();

        dto.setOverdueReasonId(this.overdueReasonId);
        dto.setWoId(this.woId);

        dto.setReasonLevel2(this.reasonLevel2);
        dto.setReasonLevel3(this.reasonLevel3);
        dto.setReasonLevel4(this.reasonLevel4);
        dto.setReasonLevel5(this.reasonLevel5);
        dto.setReasonLevelFt(this.reasonLevelFt);

        dto.setReasonDateLevel2(this.reasonDateLevel2);
        dto.setReasonDateLevel3(this.reasonDateLevel3);
        dto.setReasonDateLevel4(this.reasonDateLevel4);
        dto.setReasonDateLevel5(this.reasonDateLevel5);
        dto.setReasonDateLevelFt(this.reasonDateLevelFt);

        dto.setApproveStateLevel2(this.approveStateLevel2);
        dto.setApproveStateLevel3(this.approveStateLevel3);
        dto.setApproveStateLevel4(this.approveStateLevel4);
        dto.setApproveStateLevel5(this.approveStateLevel5);
        dto.setApproveStateLevelFt(this.approveStateLevelFt);

        dto.setApproveUserIdLevel2(this.approveUserIdLevel2);
        dto.setApproveUserIdLevel3(this.approveUserIdLevel3);
        dto.setApproveUserIdLevel4(this.approveUserIdLevel4);
        dto.setApproveUserIdLevel5(this.approveUserIdLevel5);
        dto.setApproveUserIdLevelFt(this.approveUserIdLevelFt);

        dto.setApproveDateLevel2(this.approveDateLevel2);
        dto.setApproveDateLevel3(this.approveDateLevel3);
        dto.setApproveDateLevel4(this.approveDateLevel4);
        dto.setApproveDateLevel5(this.approveDateLevel5);
        dto.setApproveDateLevelFt(this.approveDateLevelFt);

        dto.setCreatedDate(this.createdDate);
        dto.setStatus(this.status);

        return dto;
    }
}
