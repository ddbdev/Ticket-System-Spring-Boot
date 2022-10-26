package it.ddbdev.ticketsystem.payload.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UpdateMeRequest {

    @NotBlank
    @Size(max = 15, min=5)
    private String username;

    @NotBlank
    @Email
    private String email;

    private String bio;

}
