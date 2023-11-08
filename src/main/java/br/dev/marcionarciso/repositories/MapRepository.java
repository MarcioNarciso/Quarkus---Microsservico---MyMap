package br.dev.marcionarciso.repositories;

import br.dev.marcionarciso.entities.Map;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MapRepository implements PanacheRepository<Map>{

}
