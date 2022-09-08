package cryptoExchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cryptoExchange.errorHandling.BadRequestException;


@RestController
public class CryptoExchangeController {
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private CryptoExchangeRepository cryptoExchangeRepository;
	
	@GetMapping("/crypto-exchange/from/{from}/to/{to}")
	public CryptoExchange getCryptoExchange(@PathVariable String from, @PathVariable String to) {
		String port = environment.getProperty("local.server.port");
		CryptoExchange cryptoExchange = cryptoExchangeRepository.findByFromAndTo(from, to);
		if (cryptoExchange == null)
		{
			throw new BadRequestException("Currency values not entered correctly");
		}
		return new CryptoExchange(cryptoExchange.getId(), from, to, cryptoExchange.getConversionMultiple(), port);
	}
}
