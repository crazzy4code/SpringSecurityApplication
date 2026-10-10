package com.saarth.app.dto;

import lombok.Data;
import lombok.Getter;

@Getter
public class SignupRequest {
    private String firstName;
    private String lastName;
    private String username;
    private  String password;
    private String company;
}
