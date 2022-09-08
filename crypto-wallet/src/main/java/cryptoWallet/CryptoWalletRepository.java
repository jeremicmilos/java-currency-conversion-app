package cryptoWallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoWalletRepository extends JpaRepository<CryptoWallet, Long>{
	
	CryptoWallet findByEmail(String email);

}
