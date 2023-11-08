package br.dev.marcionarciso;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;
import java.util.stream.Collectors;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.Status;

import br.dev.marcionarciso.entities.Category;
import br.dev.marcionarciso.entities.Map;
import br.dev.marcionarciso.entities.dtos.CategoryDTO;
import br.dev.marcionarciso.entities.dtos.MapDTO;
import br.dev.marcionarciso.services.interfaces.CategoryServiceInterface;
import br.dev.marcionarciso.services.interfaces.MapServiceInterface;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/category")
public class CategoryResource {
	
	@Inject
	private CategoryServiceInterface categoryService;
	
	@Inject
	private MapServiceInterface mapService;

	/**
	 * Busca todas categorias.
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Uni<List<CategoryDTO>> getAll() {
		return this.categoryService.listAll()
				.flatMap(categories -> Uni
						.createFrom()
						.item(categories
								.stream()
								.map(CategoryDTO::convert)
								.collect(Collectors.toList())));
	}
	
	/**
	 * Cria uma nova categoria.
	 * @param categoryDto
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Uni<RestResponse<Category>> create(@Valid CategoryDTO categoryDto) {
		
		Category newCategory = Category.convert(categoryDto);
		
		return this.categoryService.save(newCategory)
			.flatMap(category -> Uni.createFrom().item(RestResponse.status(Status.CREATED, category)));
	}
	
	/**
	 * Atualiza uma determinada categoria.
	 * @param id			Identificador da categoria.
	 * @param categoryDto
	 * @return
	 */
	@PATCH
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Uni<RestResponse<CategoryDTO>> update(Long id, CategoryDTO categoryDto) {
		
		Category category = Category.convert(categoryDto);
		
		category.setId(id);
		
		return this.categoryService.update(category).flatMap(this::convertCategoryToResponse);
		
	}
	
	/**
	 * Exclui uma determinada categoria.
	 * @param id			Identificador da categoria.
	 * @return
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Uni<RestResponse<CategoryDTO>> delete(Long id) {
		return this.categoryService.deleteById(id).flatMap(this::convertCategoryToResponse);
	}
	
	/**
	 * Busca uma determinada categoria.
	 * @param id			Identificador da categoria.
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Uni<RestResponse<CategoryDTO>> getById(Long id) {
		return this.categoryService.getById(id).flatMap(this::convertCategoryToResponse);
	}

	/**
	 * Faz o tratamento caso a categoria buscada não exista.
	 * @param category
	 * @return
	 */
	private Uni<? extends RestResponse<CategoryDTO>> convertCategoryToResponse(Category category) {
		CategoryDTO dto = null;
		Status status = Status.NOT_FOUND;
		
		if (nonNull(category)) {
			dto = CategoryDTO.convert(category);
			status = Status.OK;
		}
		
		return Uni.createFrom().item(RestResponse.status(status, dto));
	}
	
	/**
	 * Adiciona um mapeamento novo à categoria.
	 * @param id		Identificador da categoria.
	 * @param mapDto
	 * @return
	 */
	/*
	 * @Valid Define que o parâmetro deve ser validado usando Bean Validation
	 */
	@POST
	@Path("/{id}/map")
	@Consumes(MediaType.APPLICATION_JSON) // Define o formato dos dados recebidos do cliente.
	@Produces(MediaType.APPLICATION_JSON) // Define o formato dos dados enviados para o cliente.
	public Uni<RestResponse<MapDTO>> addMap(Long id, @Valid MapDTO mapDto) {
		
		Map newMap = Map.convert(mapDto);
		
		return this.categoryService.getById(id)
			.chain(category -> {
				if (isNull(category)) {
					return Uni.createFrom().item(RestResponse.status(Status.NOT_FOUND));	 
				}
				
				newMap.setCategory(category);
				
				return this.mapService.save(newMap)
						.chain(map -> Uni
								.createFrom()
								.item(RestResponse.status(Status.CREATED, 
															MapDTO.convert(map))));
			});
	}
	
	/**
	 * Busca todos os mapeamentos da categoria.
	 * @param id	Identificador da categoria.
	 * @return
	 */
	@GET
	@Path("/{id}/map")
	@Produces(MediaType.APPLICATION_JSON)
	public Uni<Object> getAllMapsByCategory(Long id) { 
		
		return this.categoryService.getById(id)
			.chain(category -> {
				if (isNull(category)) {
					return Uni.createFrom().item(RestResponse.status(Status.NOT_FOUND));
				}
				
				return this.mapService.listAllByCategory(category)
						.flatMap(maps -> Uni.createFrom()
								.item(maps
										.stream()
										.map(MapDTO::convert)
										.collect(Collectors.toList())));
				
			});
		
	}
}
