package com.test.webservice.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;


public class UserGoldDTO extends BaseDTO{

	private String id;

    // 创建时间
    private Date createDate = new Date();

    // 根据真实的充值金额兑换后的金币的金额
    private Double exchangeGold;

    // 真实的充值金额
    private Double realmoney;

    // 兑换金币的类型
    private Integer exchangeGoldType;

    // 应用内购买产品Id
    private String productId;

    // 应用内购买收据
    private String receipt;
    
    // 交易Id
    private String transactionId;

    // 交易状态
    private String transactionState;
    
    //平台  1 android 2 ios
    private String platform;
    
    //平台 0 ios商店充值 1 支付宝  2 微信 3 paypal 
    private String payPlatform;
    
    //货币类型 1人民币 2 美元
    private String coinType;
    
    

    public String getCoinType() {
		return coinType;
	}

	public void setCoinType(String coinType) {
		this.coinType = coinType;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPayPlatform() {
		return payPlatform;
	}

	public void setPayPlatform(String payPlatform) {
		this.payPlatform = payPlatform;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //@JsonSerialize(using = CustomDateSerializer.class)
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

    public Double getRealmoney() {
        return realmoney;
    }

    public void setRealmoney(Double realmoney) {
        this.realmoney = realmoney;
    }

    public Integer getExchangeGoldType() {
        return exchangeGoldType;
    }

    public void setExchangeGoldType(Integer exchangeGoldType) {
        this.exchangeGoldType = exchangeGoldType;
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
