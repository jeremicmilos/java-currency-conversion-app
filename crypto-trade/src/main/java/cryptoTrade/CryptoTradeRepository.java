package cryptoTrade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoTradeRepository extends JpaRepository<CryptoTrade, Long>{
	CryptoTrade findByFromAndTo(String from, String to);

}
