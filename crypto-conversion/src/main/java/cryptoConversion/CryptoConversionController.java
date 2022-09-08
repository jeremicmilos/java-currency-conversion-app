package cryptoConversion;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoConversionController {
	
	@Autowired
	private CryptoExchangeProxy cryptoExchangeProxy;
	
	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;
	
	@GetMapping("/crypto-conversion/from/{from}/to/{to}/quantity/{quantity}/user/{user}")
	public CryptoWalletDto getConversionFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity, @PathVariable String user) {
		
		CryptoExchangeDto cryptoConversion = cryptoExchangeProxy.getCryptoExchange(from, to);
		
		cryptoWalletProxy.getCryptoWalletByEmail(user);
		
		return cryptoWalletProxy.updateCryptoWallet(from, to, quantity, user, quantity.multiply(cryptoConversion.getConversionMultiple()));
	}

}
