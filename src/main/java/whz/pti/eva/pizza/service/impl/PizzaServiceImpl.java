package whz.pti.eva.pizza.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import whz.pti.eva.files.service.impl.FilesStorageServiceImpl;
import whz.pti.eva.pizza.domain.dto.PizzaCreateDto;
import whz.pti.eva.pizza.domain.entity.Pizza;
import whz.pti.eva.pizza.domain.entity.PizzaPrice;
import whz.pti.eva.pizza.domain.enums.PizzaSize;
import whz.pti.eva.pizza.domain.enums.PizzaStatus;
import whz.pti.eva.pizza.repository.PizzaPriceRepository;
import whz.pti.eva.pizza.repository.PizzaRepository;
import whz.pti.eva.pizza.service.PizzaService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PizzaServiceImpl implements PizzaService {

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private PizzaPriceRepository pizzaPriceRepository;

    @Autowired
    private FilesStorageServiceImpl filesStorageService;

    @Override
    public void addPizza(PizzaCreateDto pizzaCreateDto) throws IOException {
        Pizza pizza = mapDtoToPizza(pizzaCreateDto);
        pizza.setStatus(PizzaStatus.ACTIVE);
        pizzaRepository.save(pizza);
    }

    @Override
    public void deletePizza(int pizzaId) {
        updatePizzaStatus(pizzaId, PizzaStatus.DELETED);
    }

    @Override
    public void updatePizza(int pizzaId, PizzaCreateDto pizzaCreateDto) {
        Pizza oldPizza = pizzaRepository.findById(pizzaId).orElseThrow();
        oldPizza.setStatus(PizzaStatus.ARCHIVE);
        pizzaRepository.save(oldPizza);

        Pizza newPizza = mapDtoToPizza(pizzaCreateDto);
        newPizza.setStatus(PizzaStatus.ACTIVE);
        pizzaRepository.save(newPizza);
    }

    @Override
    public void archivePizza(int pizzaId) {
        updatePizzaStatus(pizzaId, PizzaStatus.ARCHIVE);
    }

    @Override
    public void restorePizza(int pizzaId) {
        updatePizzaStatus(pizzaId, PizzaStatus.ACTIVE);
    }

    @Override
    public Pizza getPizzaById(int pizzaId) {
        return pizzaRepository.findById(pizzaId).orElse(null);
    }

    @Override
    public Page<Pizza> getAllPizzas(String search, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (isNotEmpty(search) && isNotEmpty(status)) {
            return pizzaRepository.findPizzasByStatusAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    PizzaStatus.valueOf(status), search, search, pageable);
        } else if (isNotEmpty(search)) {
            return pizzaRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, pageable);
        } else if (isNotEmpty(status)) {
            return pizzaRepository.findPizzasByStatus(PizzaStatus.valueOf(status), pageable);
        } else {
            return pizzaRepository.findAll(pageable);
        }
    }

    @Override
    public void recoverPizza(int pizzaId) {
        updatePizzaStatus(pizzaId, PizzaStatus.ACTIVE);
    }

    @Override
    public List<Pizza> getLastFourPizzas() {
        return pizzaRepository.findTop4ByStatusOrderByCreatedDate(PizzaStatus.ACTIVE);
    }


    private Pizza mapDtoToPizza(PizzaCreateDto pizzaCreateDto){
        Pizza pizza = new Pizza();
        pizza.setName(pizzaCreateDto.getName());
        pizza.setDescription(pizzaCreateDto.getDescription());
        pizza.setCreatedDate(LocalDateTime.now());

        List<PizzaPrice> pizzaPrices = List.of(
                new PizzaPrice(PizzaSize.SMALL, pizzaCreateDto.getSmallPrice()),
                new PizzaPrice(PizzaSize.MEDIUM, pizzaCreateDto.getMediumPrice()),
                new PizzaPrice(PizzaSize.LARGE, pizzaCreateDto.getLargePrice())
        );
        pizza.setPizzaPrices(pizzaPriceRepository.saveAll(pizzaPrices));

        if (!pizzaCreateDto.getImage().isEmpty()) {
            String imagePath = filesStorageService.save(pizzaCreateDto.getImage());
            pizza.setImage(imagePath);
        }

        return pizza;
    }

    private void updatePizzaStatus(int pizzaId, PizzaStatus status) {
        Pizza pizza = pizzaRepository.findById(pizzaId).orElseThrow();
        pizza.setStatus(status);
        pizzaRepository.save(pizza);
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }
}

