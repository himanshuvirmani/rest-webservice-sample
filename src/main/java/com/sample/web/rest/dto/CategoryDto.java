package com.sample.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by himanshu.virmani on 04/09/16.
 */
@Data
public class CategoryDto {

    private String name;

    private String description;

    @JsonProperty(value = "parent_id")
    private Long parentId;

}
