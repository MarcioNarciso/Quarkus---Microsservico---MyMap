package br.dev.marcionarciso.entities.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.dev.marcionarciso.entities.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class CategoryDTO {
	
	@JsonIgnore
	private Long id;
	@NotBlank
	private String name;
	private String description;
	
//	@JsonIgnore
//	private List<MapDTO> mappedValues;
	
	public static CategoryDTO convert(Category category) {
		CategoryDTO categoryDTO = new CategoryDTO();
		
		categoryDTO.setName(category.getName());
		categoryDTO.setDescription(category.getDescription());
		
//		List<MapDTO> mappedValues = category.getMappedValues()
//				.stream()
//				.map(MapDTO::convert)
//				.collect(Collectors.toList());
		
//		categoryDTO.setMappedValues(mappedValues);
		
		return categoryDTO;
	}

}
