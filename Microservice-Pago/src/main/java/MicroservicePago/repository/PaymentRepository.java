package MicroservicePago.repository;

import MicroservicePago.entity.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<OrderPayment, Long> {

    List<OrderPayment> findByOrderId(Long orderId);

}
