package cryptoWallet;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bank-acoount")
public interface BankAccountProxy {
	
	@GetMapping("/bank-account/user/{user}")
	BankAccountDto getBankAccountByEmail(@PathVariable("user") String user);

}
