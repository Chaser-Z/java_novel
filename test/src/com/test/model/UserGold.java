package com.test.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.miger.commons.dto.UUIDEntity;


@Entity
@Table(name = "t_user_gold")
public class UserGold extends UUIDEntity {

	private static final long serialVersionUID = 4152549718898004323L;

	
    // 用户id
	@Column(name = "userId")
    private String userId;

    // 创建时间
	@Column(name = "createDate")
    private Date createDate = new Date();

    // 根据真实的充值金额兑换后的金币的金额
	@Column(name = "exchangeGold")
    private Double exchangeGold;

    // 真实的充值金额
	@Column(name = "realmoney")
    private Double realmoney;

    // 兑换金币的类型  
	@Column(name = "exchangeGoldType")
    private Integer exchangeGoldType;

    // 应用内购买产品Id
	@Column(name = "productId")
    private String productId;

    // 应用内购买收据
	@Column(name = "receipt")
    private String receipt;

    // 交易Id
	@Column(name = "transactionId")
    private String transactionId;

    // 交易状态
	@Column(name = "transactionState")
    private String transactionState;

    //平台  1 android 2 ios
	@Column(name = "platform")
    private String platform;
    
    //平台 1 支付宝  2 微信 3 paypal
	@Column(name = "payPlatform")
    private String payPlatform;
 

    //货币类型 1人民币 2 美元
	@Column(name = "coinType")
    private String coinType;
    
    public String getCoinType() {
		return coinType;
	}

	public void setCoinType(String coinType) {
		this.coinType = coinType;
	}

	public String getPayPlatform() {
		return payPlatform;
	}

	public void setPayPlatform(String payPlatform) {
		this.payPlatform = payPlatform;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}



	public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Double getExchangeGold() {
        return exchangeGold;
    }

    public void setExchangeGold(Double exchangeGold) {
        this.exchangeGold = exchangeGold;
    }

    public Integer getExchangeGoldType() {
        return exchangeGoldType;
    }

    public void setExchangeGoldType(Integer exchangeGoldType) {
        this.exchangeGoldType = exchangeGoldType;
    }

    public Double getRealmoney() {
        return realmoney;
    }

    public void setRealmoney(Double realmoney) {
        this.realmoney = realmoney;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionState() {
        return transactionState;
    }

    public void setTransactionState(String transactionState) {
        this.transactionState = transactionState;
    }
}
