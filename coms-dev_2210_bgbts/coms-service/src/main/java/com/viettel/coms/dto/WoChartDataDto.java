package com.viettel.coms.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.utils.CustomJsonDateDeserializer;
import com.viettel.utils.CustomJsonDateSerializer;

public class WoChartDataDto {
    private String maTinh;
    private BigDecimal keHoachVal;
    private BigDecimal thucHienVal;
    private BigDecimal tyLeVal;
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private Date dateFrom;
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private Date dateTo;
	private List<String> woTypes;
	private List<String> lstProvinceCode;
    
    public String getMaTinh() {
        return maTinh;
    }

    public void setMaTinh(String maTinh) {
        this.maTinh = maTinh;
    }

    public BigDecimal getKeHoachVal() {
        return keHoachVal;
    }

    public void setKeHoachVal(BigDecimal keHoachVal) {
        this.keHoachVal = keHoachVal;
    }

    public BigDecimal getThucHienVal() {
        return thucHienVal;
    }

    public void setThucHienVal(BigDecimal thucHienVal) {
        this.thucHienVal = thucHienVal;
    }

    public BigDecimal getTyLeVal() {
        return tyLeVal;
    }

    public void setTyLeVal(BigDecimal tyLeVal) {
        this.tyLeVal = tyLeVal;
    }

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public List<String> getWoTypes() {
		return woTypes;
	}

	public void setWoTypes(List<String> woTypes) {
		this.woTypes = woTypes;
	}

	public List<String> getLstProvinceCode() {
		return lstProvinceCode;
	}

	public void setLstProvinceCode(List<String> lstProvinceCode) {
		this.lstProvinceCode = lstProvinceCode;
	}

    
}
