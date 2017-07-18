package com.test.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.miger.commons.dto.UUIDEntity;

@Entity
@Table(name = "t_session")
public class Session extends UUIDEntity {

    private static final long serialVersionUID = -5170719628724565221L;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "login_date")
    private Long loginDate;

    @Column(name = "os_type")
    private String osType;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "sdk_version")
    private String sdkVersion;

    @Column(name = "release_version")
    private String releaseVersion;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "unique_id")
    private String uniqueId;

    @Column(name = "status")
    private Integer status = 1;

    @Column(name = "is_del")
    private Integer isDel = 0;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Long loginDate) {
        this.loginDate = loginDate;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getReleaseVersion() {
        return releaseVersion;
    }

    public void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}
