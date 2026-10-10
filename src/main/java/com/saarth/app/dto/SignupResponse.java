package com.saarth.app.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SignupResponse {

    private UUID userId;

    private String userName;
}
