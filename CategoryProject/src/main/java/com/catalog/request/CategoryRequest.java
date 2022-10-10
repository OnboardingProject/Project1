package com.catalog.request;

import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
	//@NotEmpty(message="Level name cannot be empty")
	@Pattern(regexp="/^[a-zA-Z]+$/",message="Level name cannot be blank or it should contain only characters")  
	private String levelName;
	private String createdBy;
	private String lastUpdatedBy;
	private String hierarchyLevel;
}
