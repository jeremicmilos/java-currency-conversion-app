package cryptoConversion;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@FeignClient(name = "crypto-wallet")
public interface CryptoWalletProxy {
	
	@GetMapping("/crypto-wallet/user/{user}")
	CryptoWalletDto getCryptoWalletByEmail(@PathVariable String user);
	
	@PutMapping("/crypto-wallet/from/{from}/to/{to}/quantity/{quantity}/user/{user}/total/{total}")
	CryptoWalletDto updateCryptoWallet(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity, @PathVariable String user, @PathVariable BigDecimal total);
}