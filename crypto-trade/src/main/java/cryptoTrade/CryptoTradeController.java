package cryptoTrade;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cryptoTrade.errorHandling.BadRequestException;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
public class CryptoTradeController {

	@Autowired
	private CryptoTradeRepository cryptoTradeRepository;

	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;

	@Autowired
	private CurrencyConversionProxy currencyConversionProxy;

	@Autowired
	private BankAccountProxy bankAccountProxy;
	
	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;

	@RateLimiter(name = "default", fallbackMethod ="getFallbackResponse")
	@GetMapping("/crypto-trade/from/{from}/to/{to}/quantity/{quantity}/user/{user}")
	public Object getCryptoTrade(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity, @PathVariable String user) {
		
		if (!isValidCurrency(from.toUpperCase()) && !isValidCrypto(from.toUpperCase())) {
			throw new BadRequestException("Not valid currency in FROM parameter");
		}

		if (!isValidCurrency(to.toUpperCase()) && !isValidCrypto(to.toUpperCase())) {
			throw new BadRequestException("Not valid currency in TO parameter");
		}
	
		if ((	from.toUpperCase().equals("RSD") || 
				from.toUpperCase().equals("EUR") || 
				from.toUpperCase().equals("USD") || 
				from.toUpperCase().equals("GBP") || 
				from.toUpperCase().equals("CHF")) 
				&& 
				(to.toUpperCase().equals("RSD") || 
				to.toUpperCase().equals("EUR") || 
				to.toUpperCase().equals("USD") || 
				to.toUpperCase().equals("GBP") || 
				to.toUpperCase().equals("CHF"))) 
		{
			throw new BadRequestException("This is crypto trade, one currency must be either crypto or fiat currency");
		}
		
		if ((	from.toUpperCase().equals("BTC") || 
				from.toUpperCase().equals("ETH") || 
				from.toUpperCase().equals("XRP")) 
				&&
				(to.toUpperCase().equals("BTC") || 
				to.toUpperCase().equals("ETH") || 
				to.toUpperCase().equals("XRP")))
		{
			throw new BadRequestException("This is crypto trade, one currency must be either crypto or fiat currency");
		}

		bankAccountProxy.getBankAccount(user);
		cryptoWalletProxy.getCryptoWallet(user);

		CryptoTrade cryptoTrade = cryptoTradeRepository.findByFromAndTo(from, to);

		CryptoWalletDto cryptoWallet;
		BankAccountDto bankAccount;
		CurrencyExchangeDto currencyExchange;
		
		bankAccount = bankAccountProxy.getBankAccount(user);
		cryptoWallet = cryptoWalletProxy.getCryptoWallet(user);
		
		BigDecimal oldAmount, newAmount, difference;
		
		if(from.equals("USD") || from.equals("EUR")) {	
			if(to.equals("BTC") || to.equals("ETH") || to.equals("XRP")) {
				bankAccountProxy.updateBankAccountAfterTrade(from, to, quantity, user);
				cryptoWallet = cryptoWalletProxy.updateCryptoWalletAfterTrade(from, to, quantity.multiply(cryptoTrade.getConversionMultiple()), user);
			}
			return cryptoWallet;
		} 
		
		if(from.equals("RSD") || from.equals("CHF") || from.equals("GBP")) {
			cryptoTrade = cryptoTradeRepository.findByFromAndTo("USD", to);
			
			oldAmount = bankAccount.getUsd_amount(); 
			
			currencyConversionProxy.getCurrencyConversion(from, "USD", quantity, user);
			
			newAmount = bankAccountProxy.getBankAccount(user).getUsd_amount();
			
			difference = newAmount.subtract(oldAmount);
			
			if(to.equals("BTC") || to.equals("ETH") || to.equals("XRP")) {
				bankAccountProxy.updateBankAccountAfterTrade("USD", to, difference, user);
				cryptoWallet = cryptoWalletProxy.updateCryptoWalletAfterTrade("USD", to, difference.multiply(cryptoTrade.getConversionMultiple()), user);
			}
			return cryptoWallet;
		}

		
		if(from.equals("BTC") || from.equals("ETH") || from.equals("XRP")) {	
			if(to.equals("EUR") || to.equals("USD")) {
				cryptoWalletProxy.updateCryptoWalletAfterTrade(from, to, quantity, user);
				bankAccount = bankAccountProxy.updateBankAccountAfterTrade(from, to, quantity.multiply(cryptoTrade.getConversionMultiple()), user);
			}
			
			if(to.equals("RSD") || to.equals("GBP") || to.equals("CHF")) {
				cryptoTrade = cryptoTradeRepository.findByFromAndTo(from, "USD");
				
				oldAmount = bankAccount.getUsd_amount(); // 50 
				
				bankAccount = bankAccountProxy.updateBankAccountAfterTrade(from, "USD", quantity.multiply(cryptoTrade.getConversionMultiple()), user);
				
				newAmount = bankAccountProxy.getBankAccount(user).getUsd_amount(); // 51
				
				difference = newAmount.subtract(oldAmount);		
				
				currencyExchange = currencyExchangeProxy.getExchange("USD", to);
				
				cryptoWalletProxy.updateCryptoWalletAfterTrade(from, to, quantity, user);
				
				bankAccountProxy.updateBankAccountAfterTrade("USD", "BTC", difference, user);
				
				bankAccount = bankAccountProxy.updateBankAccountAfterTrade(from, to, difference.multiply(currencyExchange.getConversionMultiple()), user);	
			}			
			
		}
		return bankAccount;
	}

	private boolean isValidCurrency(String currency) {
		return currency.equals("RSD") || currency.equals("EUR") || currency.equals("USD") || currency.equals("GBP")
				|| currency.equals("CHF");
	}

	private boolean isValidCrypto(String cryptoCurrency) {
		return cryptoCurrency.equals("BTC") || cryptoCurrency.equals("ETH") || cryptoCurrency.equals("XRP");
	}	
	
	public String getFallbackResponse(Exception ex) {
		return "You can send only 2 request withing 60 seconds";
	}
}
