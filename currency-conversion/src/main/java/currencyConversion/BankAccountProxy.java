package currencyConversion;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bank-account")
public interface BankAccountProxy {
	
	@GetMapping("/bank-account/user/{user}")
	BankAccountDto getBankAccountByEmail(@PathVariable String user);
	
	@PutMapping("/bank-account/from/{from}/to/{to}/quantity/{quantity}/user/{user}/total/{total}")
	BankAccountDto updateBankAccount(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity, @PathVariable String user, @PathVariable BigDecimal total);
}