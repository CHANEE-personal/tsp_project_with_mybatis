package com.tsp.new_tsp_project.api.admin.model.domain.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Slf4j
public class careerJsonTypeHandler extends BaseTypeHandler<List<careerJson>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<careerJson> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, new Gson().toJson(parameter));
    }

    @Override
    public List<careerJson> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convertToList(rs.getString(columnName));
    }

    @Override
    public List<careerJson> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertToList(rs.getString(columnIndex));
    }

    @Override
    public List<careerJson> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convertToList(cs.getString(columnIndex));
    }

    private List<careerJson> convertToList(String s) {
        try {
            return new ObjectMapper().readValue(s, new TypeReference<List<careerJson>>() {});
        } catch (IOException e) {
            log.error("[ExampleJsonTypeHandler] failed to convert string to list");
        }
        return Collections.emptyList();
    }
}
