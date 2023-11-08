package br.dev.marcionarciso.services;

import static br.dev.marcionarciso.utils.StringUtil.isEmpty;

import java.util.List;

import br.dev.marcionarciso.entities.Category;
import br.dev.marcionarciso.entities.Map;
import br.dev.marcionarciso.repositories.MapRepository;
import br.dev.marcionarciso.services.interfaces.MapServiceInterface;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MapService implements MapServiceInterface{
	
	@Inject
	private MapRepository repository;
	
	public Uni<List<Map>> listAllByCategory(Category category) {
		return this.repository.list("category", category);
	}

	public Uni<Map> save(Map map) {
		return Panache.withTransaction(map::persistAndFlush);
	}
	
	@Override
	public Uni<Map> getById(Long id) {
		return this.repository.findById(id);
	}
	
	@Override
	public Uni<Map> deleteById(Long id) {

		return Panache.withTransaction(() -> {
			return this.getById(id)
					.call(Map::delete)
					.onFailure().recoverWithNull();
		});
		
	}
	
	/**
	 * Exclui todos os mapeamentos de determinada categoria.
	 * @return Retorna a quantidade de itens excluídos.
	 */
	@Override
	public Uni<Long> deleteByCategory(Category category) {
		return Panache.withTransaction(() -> {
			return this.repository.delete("category", category);
		});
	}
	
	@Override
	public Uni<Map> update(Map map) {
		
		return this.getById(map.getId())
			.chain(existingMap -> {
				String description = map.getDescription();
				
				if (! isEmpty(description))  {
					existingMap.setDescription(description);
				}
				
				String key = map.getKey();
				
				if (! isEmpty(key)) {
					existingMap.setKey(key);
				}
				
				String value = map.getValue();
				
				if (! isEmpty(value)) {
					existingMap.setValue(value);
				}
				
				return this.save(existingMap);
			})
			.onFailure().recoverWithNull();
	}
}
