package br.dev.marcionarciso;

import java.util.Objects;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.Status;

import br.dev.marcionarciso.entities.Map;
import br.dev.marcionarciso.entities.dtos.MapDTO;
import br.dev.marcionarciso.services.interfaces.MapServiceInterface;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/map")
public class MapResource {

	@Inject
	private MapServiceInterface service;

	/**
	 * Atualiza determinado mapeamento.
	 * @param id		Identificador do mapeamento.
	 * @param mapDto
	 * @return
	 */
	@PATCH
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Uni<RestResponse<MapDTO>> update(Long id, MapDTO mapDto) {
		Map receivedMap = Map.convert(mapDto);
		
		receivedMap.setId(id);
		
		return this.service.update(receivedMap).flatMap(this::convertMapToResponse);
	}

	/**
	 * Busca um determinado mapeamento.
	 * @param id	Identificador do mapeamento.
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Uni<RestResponse<MapDTO>> getById(Long id) {
		return this.service.getById(id).flatMap(this::convertMapToResponse);
	}
		
	/**
	 * Exclui um determinado mapeamento.
	 * @param id	Identificador do mapeamento.
	 * @return
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Uni<RestResponse<MapDTO>> delete(Long id) {
		return this.service.deleteById(id).flatMap(this::convertMapToResponse);
	}
	
	/**
	 * Faz o tratamento caso o mapeamento buscado não exista e o 
	 * transforma em um RestResponse
	 * @param map
	 * @return
	 */
	private Uni<RestResponse<MapDTO>> convertMapToResponse(Map map) {
		Status status = Status.NOT_FOUND;
		MapDTO mapDTO = null;
		
		if (Objects.nonNull(map)) {
			mapDTO = MapDTO.convert(map);
			status = Status.OK;
		}
		
		return Uni.createFrom().item(RestResponse.status(status, mapDTO));
	}
}
