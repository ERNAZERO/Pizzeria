package whz.pti.eva.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import whz.pti.eva.user.domain.entity.DeliveryAddress;
import whz.pti.eva.user.domain.entity.Users;

import java.util.UUID;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {

}
