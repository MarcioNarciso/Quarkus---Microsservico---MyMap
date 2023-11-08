package br.dev.marcionarciso.services.interfaces;

import java.util.List;

import br.dev.marcionarciso.entities.Category;
import io.smallrye.mutiny.Uni;

public interface CategoryServiceInterface {

	public Uni<List<Category>> listAll();
	public Uni<Category> save(Category category);
	Uni<Category> getById(Long id);
	public Uni<Category> deleteById(Long id);
	public Uni<Category> update(Category category);
	
}
