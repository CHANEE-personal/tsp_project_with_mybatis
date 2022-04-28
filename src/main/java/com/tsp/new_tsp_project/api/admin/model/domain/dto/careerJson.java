package com.tsp.new_tsp_project.api.admin.model.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class careerJson {

    private String title;
    private String text;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
