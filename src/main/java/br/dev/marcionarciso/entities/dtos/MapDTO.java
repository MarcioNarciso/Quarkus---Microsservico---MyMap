package br.dev.marcionarciso.entities.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.dev.marcionarciso.entities.Map;
import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties // Indica que se no JSON tiver campos não especificado nesta classe, eles são ignorados
public class MapDTO {

//	private Long id;
	@NotBlank
	private String key;
	@NotBlank
	private String value;
	private String description;
	
	@NotNull
	@JsonProperty("category")
	@JsonIgnore
	// @Valid // Instrui ao Bean Validation que a entidade aninhada deve ser validada
	private CategoryDTO categoryDTO;
	
	public static MapDTO convert(Map map) {
		MapDTO mapDTO = new MapDTO();
		
//		mapDTO.setId(map.getId());
		mapDTO.setKey(map.getKey());
		mapDTO.setValue(map.getValue());
		mapDTO.setDescription(map.getDescription());
		mapDTO.setCategoryDTO(CategoryDTO.convert(map.getCategory()));
		
		return mapDTO;
	}
}
