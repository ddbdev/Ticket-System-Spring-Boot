package it.ddbdev.ticketsystem.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class SigninRequest {

    @NotBlank
    @Size(min = 5)
    private String usernameOrEmail;

    @NotBlank

    private String password;

}
