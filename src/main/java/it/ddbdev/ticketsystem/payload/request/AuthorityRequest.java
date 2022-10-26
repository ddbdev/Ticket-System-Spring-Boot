package it.ddbdev.ticketsystem.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class AuthorityRequest {

    @NotBlank
    @Size(min = 6, max = 20)
    private String authorityName;

}
