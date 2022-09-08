package cryptoConversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "crypto-exchange")
public interface CryptoExchangeProxy {
	
	
	@GetMapping("/crypto-exchange/from/{from}/to/{to}")
	CryptoExchangeDto getCryptoExchange(@PathVariable String from, @PathVariable String to);

}
