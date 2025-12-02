package whz.pti.eva.pizza.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whz.pti.eva.pizza.domain.entity.Pizza;
import whz.pti.eva.pizza.domain.enums.PizzaStatus;

import java.util.List;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Integer> {


    Page<Pizza> findPizzasByStatusAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(PizzaStatus status, String name, String description, Pageable pageable);

    Page<Pizza> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

    Page<Pizza> findPizzasByStatus(PizzaStatus status, Pageable pageable);

    Page<Pizza> findAll(Pageable pageable);

    List<Pizza> findTop4ByStatusOrderByCreatedDate(PizzaStatus status);

}
