package com.tsp.new_tsp_project.api.jwt;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AuthenticationRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;
    private String userId;
    private String password;

}
