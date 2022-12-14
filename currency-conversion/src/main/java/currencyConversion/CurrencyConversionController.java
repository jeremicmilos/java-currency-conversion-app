package currencyConversion;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {

	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;
	@Autowired
	private BankAccountProxy bankAccountProxy;
	
	/*@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion getConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		
		HashMap<String,String> uriVariables = new HashMap<>();
		uriVariables.put("from",from);
		uriVariables.put("to", to);
		
		ResponseEntity<CurrencyConversion> response = new RestTemplate().getForEntity
				("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);
		
		CurrencyConversion temp = response.getBody();
		
		return new CurrencyConversion(temp.getId(),from,to,temp.getConversionMultiple(),quantity,quantity.multiply(temp.getConversionMultiple()), temp.getEnvironment());
	}*/
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}/user/{user}")
	public BankAccountDto getConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity, @PathVariable String user) {
		
		CurrencyExchangeDto currencyConversion = currencyExchangeProxy.getExchange(from, to);

		bankAccountProxy.getBankAccountByEmail(user);

		return bankAccountProxy.updateBankAccount(from, to, quantity, user,
				quantity.multiply(currencyConversion.getConversionMultiple()));
	}
	
}
