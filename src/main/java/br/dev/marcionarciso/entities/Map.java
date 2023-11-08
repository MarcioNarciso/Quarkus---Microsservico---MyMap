package br.dev.marcionarciso.entities;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.dev.marcionarciso.entities.dtos.CategoryDTO;
import br.dev.marcionarciso.entities.dtos.MapDTO;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Map extends PanacheEntityBase{
//public class Map extends PanacheEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String key;
	private String value;
	private String description;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "category_id")
	private Category category;
			
	public static Map convert(MapDTO mapDto) {
		Map map = new Map();
		
		map.setKey(mapDto.getKey());
		map.setValue(mapDto.getValue());
		map.setDescription(mapDto.getDescription());
		
		CategoryDTO categoryDTO = mapDto.getCategoryDTO();
		
		if (Objects.nonNull(categoryDTO)) {
			map.setCategory(Category.convert(categoryDTO));
		}
		
		return map;
	}
	
}
