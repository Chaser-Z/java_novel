package com.test.webservice.param;


import com.test.model.NovelUser;
import com.test.model.Session;
import com.test.webservice.dto.BaseDTO;
import com.vdurmont.emoji.EmojiParser;

public class RegisterParam extends BaseDTO {

    // User Info

    private String identityType;

    private String identifier;

    private String credential;

    private Integer expiresIn;

    private String refreshToken;

    private String unionid;

    private Integer verified;

    private String email;

    private String nickname;

    private String avatar;

    private Integer nationalityId;

    private Integer hskId;

    private String interestedLangs;

    private String username;

    private Integer gender;

    private String phone;

    private String description;

    private String address;

    private String zipCode;

    private String city;

    private String province;

    private String country;

    private String pwdQuestion;

    private String pwdAnswer;

    private String randomCode;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(Integer nationalityId) {
        this.nationalityId = nationalityId;
    }

    public Integer getHskId() {
        return hskId;
    }

    public void setHskId(Integer hskId) {
        this.hskId = hskId;
    }

    public String getInterestedLangs() {
        return interestedLangs;
    }

    public void setInterestedLangs(String interestedLangs) {
        this.interestedLangs = interestedLangs;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public String getPwdQuestion() {
        return pwdQuestion;
    }

    public void setPwdQuestion(String pwdQuestion) {
        this.pwdQuestion = pwdQuestion;
    }

    public String getPwdAnswer() {
        return pwdAnswer;
    }

    public void setPwdAnswer(String pwdAnswer) {
        this.pwdAnswer = pwdAnswer;
    }

    public String getRandomCode() {
        return randomCode;
    }

    public void setRandomCode(String randomCode) {
        this.randomCode = randomCode;
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
        user.setVerified(verified);
        user.setEmail(email);
        user.setAvatar(avatar);
        user.setNationalityId(nationalityId);
        user.setHskId(hskId);
        user.setInterestedLangs(interestedLangs);
        user.setGender(gender == null ? 0 : gender);
        user.setPhone(phone);
        user.setAddress(address);
        user.setZipCode(zipCode);
        user.setCity(city);
        user.setPwdQuestion(pwdQuestion);
        user.setPwdAnswer(pwdAnswer);
        user.setRandomCode(randomCode);
        user.setRegPlatform(regPlatform);
        
        // Process emoji:
        if (this.nickname != null) {
            user.setNickname(EmojiParser.parseToAliases(this.nickname));
        }

        if (this.username != null) {
            user.setUsername(EmojiParser.parseToAliases(this.username));
        }

        if (this.description != null) {
            user.setDescription(EmojiParser.parseToAliases(this.description));
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

