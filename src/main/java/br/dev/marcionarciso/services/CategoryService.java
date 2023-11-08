package br.dev.marcionarciso.services;

import java.util.List;
import java.util.Objects;

import br.dev.marcionarciso.entities.Category;
import br.dev.marcionarciso.repositories.CategoryRepository;
import br.dev.marcionarciso.repositories.MapRepository;
import br.dev.marcionarciso.services.interfaces.CategoryServiceInterface;
import br.dev.marcionarciso.services.interfaces.MapServiceInterface;
import br.dev.marcionarciso.utils.StringUtil;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CategoryService implements CategoryServiceInterface{
	
	@Inject
	private CategoryRepository repository;
	
	@Inject
	private MapServiceInterface mapService;
	
	@WithSession
	public Uni<List<Category>> listAll() {
		return this.repository.listAll();
	}

	@Override
	public Uni<Category> save(Category category) {
		return Panache.withTransaction(category::persistAndFlush);
	}

	@Override
	public Uni<Category> getById(Long id) {
		return this.repository.findById(id);
	}
	
	@Override
	public Uni<Category> deleteById(Long id) {
		
		return Panache.withTransaction(() -> {
			return this.getById(id)
					.call(this.mapService::deleteByCategory)
					.call(this.repository::delete)
					.onFailure().recoverWithNull();
		});
		
	}
	
	@Override
	public Uni<Category> update(Category category) {
		
		return this.getById(category.getId())
				.chain(existingCategory -> {
					String name = category.getName();
					
					if (! StringUtil.isEmpty(name)) {
						existingCategory.setName(name);
					}
					
					String description = category.getDescription();
					
					if (! StringUtil.isEmpty(description)) {
						existingCategory.setDescription(description);
					}
					
					return this.save(existingCategory);
				})
				// Recuperar do erro faz diferença para quem invocar esse método.
				.onFailure().recoverWithNull();  
	}
}
