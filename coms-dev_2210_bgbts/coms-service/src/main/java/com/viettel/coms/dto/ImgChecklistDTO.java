package com.viettel.coms.dto;

public class ImgChecklistDTO {
    private Long id;
    private String imgBase64;
    private Double latitude;
    private Double longtitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }
}
