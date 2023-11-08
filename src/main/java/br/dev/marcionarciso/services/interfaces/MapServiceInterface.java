package br.dev.marcionarciso.services.interfaces;

import br.dev.marcionarciso.entities.Category;
import br.dev.marcionarciso.entities.Map;
import io.smallrye.mutiny.Uni;
import java.util.List;

public interface MapServiceInterface {
	
	public Uni<List<Map>> listAllByCategory(Category category);
	public Uni<Map> save(Map map);
	public Uni<Map> getById(Long id);
	public Uni<Map> deleteById(Long id);
	Uni<Map> update(Map map);
	Uni<Long> deleteByCategory(Category category);

}
