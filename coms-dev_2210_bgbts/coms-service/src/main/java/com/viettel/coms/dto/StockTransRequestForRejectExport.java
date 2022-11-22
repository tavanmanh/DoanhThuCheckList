package com.viettel.coms.dto;

import java.util.List;

public class StockTransRequestForRejectExport {
	private Long sysUserId;
	private Long stockId;
	private String stockCode;
	private String stockTransCode;

	public Long getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getStockTransCode() {
		return stockTransCode;
	}

	public void setStockTransCode(String stockTransCode) {
		this.stockTransCode = stockTransCode;
	}
}
