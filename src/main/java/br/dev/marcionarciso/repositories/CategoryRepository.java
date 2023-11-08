package br.dev.marcionarciso.repositories;

import br.dev.marcionarciso.entities.Category;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryRepository implements PanacheRepository<Category>{

}
