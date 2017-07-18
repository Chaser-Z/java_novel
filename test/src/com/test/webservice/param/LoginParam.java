package com.test.webservice.param;


import com.test.model.NovelUser;
import com.test.model.Session;
import com.test.webservice.dto.BaseDTO;
import com.vdurmont.emoji.EmojiParser;

public class LoginParam extends BaseDTO {

    // Login Info

    private String identityType;

    private String identifier;

    private String credential;

    private String credentialOld;

    private Integer expiresIn;

    private String refreshToken;

    private String unionid;
    
    private Integer verified;

    private String nickname;

    private String avatar;

    private String city;

    private String province;

    private String country;

    private Integer gender;
    
    private String regPlatform;

    // Session Info

    private String osType;

    private String brand;

    private String model;

    private String sdkVersion;

    private String releaseVersion;

    private String deviceToken;

    private String uniqueId;

    // Getters and Setters

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getCredentialOld() {
        return credentialOld;
    }

    public void setCredentialOld(String credentialOld) {
        this.credentialOld = credentialOld;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public Integer getVerified() {
        return verified;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getRegPlatform() {
        return regPlatform;
    }

    public void setRegPlatform(String regPlatform) {
        this.regPlatform = regPlatform;
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

    // Methods

    public NovelUser getUser() {
    	NovelUser user = new NovelUser();
        user.setId(userId);
        user.setIdentityType(identityType);
        user.setIdentifier(identifier);
        user.setCredential(credential);
        user.setExpiresIn(expiresIn);
        user.setRefreshToken(refreshToken);
        user.setUnionid(unionid);
        user.setVerified(verified);
        user.setAvatar(avatar);
        user.setCity(city);
        user.setProvince(province);
        user.setCountry(country);
        user.setGender(gender);
        user.setRegPlatform(regPlatform);
        
        // Process emoji
        if (this.nickname != null) {
            user.setNickname(EmojiParser.parseToAliases(this.nickname));
        }
        
        return user;
    }

    public Session getSession() {
        Session session = new Session();
        session.setUserId(userId);
        session.setOsType(osType);
        session.setBrand(brand);
        session.setModel(model);
        session.setSdkVersion(sdkVersion);
        session.setReleaseVersion(releaseVersion);
        session.setDeviceToken(deviceToken);
        session.setUniqueId(uniqueId);
        return session;
    }
}
