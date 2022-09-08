package cryptoConversion;

import java.math.BigDecimal;

public class CryptoExchangeDto {
	
	private String from;
	private String to;
	private BigDecimal conversionMultiple;
	
	public CryptoExchangeDto() {
		super();
	}

	public CryptoExchangeDto(String from, String to, BigDecimal conversionMultiple) {
		super();
		this.from = from;
		this.to = to;
		this.conversionMultiple = conversionMultiple;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public BigDecimal getConversionMultiple() {
		return conversionMultiple;
	}

	public void setConversionMultiple(BigDecimal conversionMultiple) {
		this.conversionMultiple = conversionMultiple;
	}
	
	

}
