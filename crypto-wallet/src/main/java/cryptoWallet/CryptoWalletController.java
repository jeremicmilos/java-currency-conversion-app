package cryptoWallet;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import cryptoWallet.errorHandling.BadRequestException;
import cryptoWallet.errorHandling.NotFoundException;

@RestController
public class CryptoWalletController {

	@Autowired
	private CryptoWalletRepository cryptoWalletRepository;

	@GetMapping("/crypto-wallet/user/{user}")
	public CryptoWallet getCryptoWalletByEmail(@PathVariable("user") String user) {
		CryptoWallet cryptoWallet = cryptoWalletRepository.findByEmail(user);

		if (cryptoWallet == null) {
			throw new NotFoundException("Crypto wallet does not exist with this email: " + user);
		}

		return cryptoWallet;
	}

	@GetMapping("/crypto-wallet/{id}")
	public CryptoWallet getCryptoWalletById(@PathVariable("id") Long id) {
		return cryptoWalletRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Crypto wallet does not found with id: " + id));
	}

	@PutMapping("/crypto-wallet/from/{from}/to/{to}/quantity/{quantity}/user/{user}/total/{total}")
	public CryptoWallet updateCryptowallet(@PathVariable("from") String from, @PathVariable("to") String to,
			@PathVariable("quantity") BigDecimal quantity, @PathVariable("user") String user,
			@PathVariable("total") BigDecimal total) {
		CryptoWallet cryptoWallet = cryptoWalletRepository.findByEmail(user);

		if (checkCryptoWallet(cryptoWallet, from, quantity)) {
			calculateCryptoMoney(cryptoWallet, from, quantity.negate());
			calculateCryptoMoney(cryptoWallet, to, total);
		}

		return cryptoWalletRepository.save(cryptoWallet);
	}
	
	@PutMapping("/crypto-wallet/from/{from}/to/{to}/quantity/{quantity}/user/{user}")
	public CryptoWallet updateCryptoWalletAfterTrade(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity, @PathVariable String user) {
		CryptoWallet cryptoWallet = cryptoWalletRepository.findByEmail(user);
		
		if (from.equals("BTC") || from.equals("ETH") || from.equals("XRP")) {
			if (checkCryptoWallet(cryptoWallet, from, quantity)) {
				calculateCryptoMoney(cryptoWallet, from, quantity.negate());
			}
		}

		if (to.equals("BTC") || to.equals("ETH") || to.equals("XRP")) {
			calculateCryptoMoney(cryptoWallet, to, quantity);
		}

		return cryptoWalletRepository.save(cryptoWallet);
	}

	private boolean checkCryptoWallet(CryptoWallet cryptoWallet, String currency, BigDecimal quantity) {
		switch (currency) {
		case "BTC":
			if (cryptoWallet.getBtc_amount().compareTo(quantity) >= 0) {
				return true;
			}
			throw new BadRequestException("Not enough BTC in crypto wallet");
		case "ETH":
			if (cryptoWallet.getEth_amount().compareTo(quantity) >= 0) {
				return true;
			}
			throw new BadRequestException("Not enough ETH in crypto wallet");
		case "XRP":
			if (cryptoWallet.getXrp_amount().compareTo(quantity) >= 0) {
				return true;
			}
			throw new BadRequestException("Not enough XRP in crypto wallet");
		default:
			throw new BadRequestException("Incorrect crypto currency value parameter");
		}
	}

	private void calculateCryptoMoney(CryptoWallet cryptoWallet, String currency, BigDecimal quantity) {
		switch (currency) {
		case "BTC":
			cryptoWallet.setBtc_amount(cryptoWallet.getBtc_amount().add(quantity));
			break;
		case "ETH":
			cryptoWallet.setEth_amount(cryptoWallet.getEth_amount().add(quantity));
			break;
		case "XRP":
			cryptoWallet.setXrp_amount(cryptoWallet.getXrp_amount().add(quantity));
			break;
		default:
			throw new BadRequestException("No crypto currency during calculating crypto money");
		}
	}
}
