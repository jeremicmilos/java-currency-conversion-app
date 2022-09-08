package cryptoTrade;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency-conversion")
public interface CurrencyConversionProxy {
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}/user/{user}")
	BankAccountDto getCurrencyConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity, @PathVariable String user);

}
