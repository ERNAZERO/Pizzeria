package whz.pti.eva.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whz.pti.eva.user.domain.entity.Users;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID>{
    Optional<Users> findByEmail(String email);
}
