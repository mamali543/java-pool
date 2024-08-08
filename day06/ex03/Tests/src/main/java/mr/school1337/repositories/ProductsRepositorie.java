package main.java.mr.school1337.repositories;

import java.util.List;
import java.util.Optional;
import main.java.mr.school1337.models.Product;

public interface ProductsRepositorie {
    List<Product> findAll();

    Optional<Product> findById(Long id);

    void update(Product product);

    void save(Product product);

    void delete(Long id);
}
