package net.cyphoria.samples.repository;

import net.cyphoria.samples.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stefan Pennndorf <stefan@cyphoria.net>
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
