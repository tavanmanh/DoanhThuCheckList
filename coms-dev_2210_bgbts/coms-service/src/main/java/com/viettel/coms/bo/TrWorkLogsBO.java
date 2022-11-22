package com.viettel.coms.bo;

import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TR_WORKLOGS")
public class TrWorkLogsBO extends BaseFWModelImpl {
    private long id;
    private long trId;
    private Date logTime;
    private String logType;
    private String userCreated;
    private String content;
    private String contentDetail;
    private int status;

    @Transient
    private String logTimeStr;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "TR_WORKLOGS_SEQ")})
    @Column(name = "ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "TR_ID")
    public long getTrId() {
        return trId;
    }

    public void setTrId(long trId) {
        this.trId = trId;
    }

    @Column(name = "LOG_TIME")
    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    @Column(name = "LOG_TYPE")
    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    @Column(name = "USER_CREATED")
    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "CONTENT_DETAIL")
    public String getContentDetail() {
        return contentDetail;
    }

    public void setContentDetail(String contentDetail) {
        this.contentDetail = contentDetail;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Transient
    public String getLogTimeStr() {
        return logTimeStr;
    }

    public void setLogTimeStr(String logTimeStr) {
        this.logTimeStr = logTimeStr;
    }

    @Override
    public BaseFWDTOImpl toDTO() {
        return null;
    }
}
