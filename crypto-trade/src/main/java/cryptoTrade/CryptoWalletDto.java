package cryptoTrade;

import java.math.BigDecimal;

public class CryptoWalletDto {
	
	private String email;
	private BigDecimal btc_amount;
	private BigDecimal eth_amount;
	private BigDecimal xrp_amount;
	
	public CryptoWalletDto() {

	}

	public CryptoWalletDto(String email, BigDecimal btc_amount, BigDecimal eth_amount, BigDecimal xrp_amount) {
		super();
		this.email = email;
		this.btc_amount = btc_amount;
		this.eth_amount = eth_amount;
		this.xrp_amount = xrp_amount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getBtc_amount() {
		return btc_amount;
	}

	public void setBtc_amount(BigDecimal btc_amount) {
		this.btc_amount = btc_amount;
	}

	public BigDecimal getEth_amount() {
		return eth_amount;
	}

	public void setEth_amount(BigDecimal eth_amount) {
		this.eth_amount = eth_amount;
	}

	public BigDecimal getXrp_amount() {
		return xrp_amount;
	}

	public void setXrp_amount(BigDecimal xrp_amount) {
		this.xrp_amount = xrp_amount;
	}
	
	

}
