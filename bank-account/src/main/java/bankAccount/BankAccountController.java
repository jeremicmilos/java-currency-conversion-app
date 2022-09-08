package bankAccount;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bankAccount.errorHandling.BadRequestException;
import bankAccount.errorHandling.NotFoundException;


@RestController
public class BankAccountController {

	@Autowired
	private BankAccountRepository bankAccountRepository;
	
	@GetMapping("/bank-account/user/{user}")
	public BankAccount getBankAccountByEmail(@PathVariable("user") String user) {
		BankAccount bankAccount = bankAccountRepository.findByEmail(user);

		if (bankAccount == null) {
			throw new NotFoundException("Bank account does not exist with this email: " + user);
		}

		return bankAccount;
	}
	
	@PutMapping("/bank-account/from/{from}/to/{to}/quantity/{quantity}/user/{user}/total/{total}")
	public BankAccount updateBankAccount(@PathVariable("from") String from, @PathVariable("to") String to, @PathVariable("quantity") BigDecimal quantity, @PathVariable("user") String user, @PathVariable("total") BigDecimal total) {
		BankAccount bankAccount = bankAccountRepository.findByEmail(user);
		
		if(checkAccountMoney(bankAccount, from, quantity)) {
			calculateMoney(bankAccount, from, quantity.negate());
			calculateMoney(bankAccount, to, total);
		}
		
		bankAccountRepository.save(bankAccount);
		return bankAccount;
	}
	
	@PutMapping("/bank-account/from/{from}/to/{to}/quantity/{quantity}/user/{user}")
	public BankAccount updateBankAccountAfterTrade(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity, @PathVariable String user) {
		BankAccount bankAccount = bankAccountRepository.findByEmail(user);
		
		if(from.equals("RSD") || from.equals("EUR") || from.equals("USD") || from.equals("CHF") || from.equals("GBP")) {
			if(checkAccountMoney(bankAccount, from, quantity)) {
				calculateMoney(bankAccount, from, quantity.negate());
			}
		}
		
		if(to.equals("RSD") || to.equals("EUR") || to.equals("USD") || to.equals("CHF") || to.equals("GBP")) {
			calculateMoney(bankAccount, to, quantity);
		}
		
		return bankAccountRepository.save(bankAccount);
	}
	
	private boolean checkAccountMoney(BankAccount bankAccount, String currency, BigDecimal quantity) {
		switch (currency) {
		case "EUR":
			if(bankAccount.getEur_amount().compareTo(quantity) >= 0) {
				return true;
			}
			throw new BadRequestException("Not enough EUR on account");
		case "USD":	
			if(bankAccount.getUsd_amount().compareTo(quantity) >= 0) {
				return true;
			}
			throw new BadRequestException("Not enough USD on account");
		case "GBP":	
			if(bankAccount.getGbp_amount().compareTo(quantity) >= 0) {
				return true;
			}
			throw new BadRequestException("Not enough GBP on account");
		case "CHF":	
			if(bankAccount.getChf_amount().compareTo(quantity) >= 0) {
				return true;
			}
			throw new BadRequestException("Not enough CHF on account");
		case "RSD":	
			if(bankAccount.getRsd_amount().compareTo(quantity) >= 0) {
				return true;
			}
			throw new BadRequestException("Not enough RSD on account");
		default:
			throw new BadRequestException("Incorrect currency value parameter");
		}
	}
	
	private void calculateMoney(BankAccount bankAccount, String currency, BigDecimal amount) {
		switch (currency) {
		case "EUR":
			bankAccount.setEur_amount(bankAccount.getEur_amount().add(amount));
			break;
		case "USD":
			bankAccount.setUsd_amount(bankAccount.getUsd_amount().add(amount));
			break;
		case "RSD":
			bankAccount.setRsd_amount(bankAccount.getRsd_amount().add(amount));
		break;
		case "GBP":
			bankAccount.setGbp_amount(bankAccount.getGbp_amount().add(amount));
		break;
		case "CHF":
			bankAccount.setChf_amount(bankAccount.getChf_amount().add(amount));
		break;
		default:
			throw new BadRequestException("No currency during calculating bank account money");
		}
	}
}