package com.tsp.new_tsp_project.api.admin.model.domain.dto;

import com.google.gson.Gson;
import lombok.Data;

@Data
public class careerJson {

    private String title;
    private String text;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
