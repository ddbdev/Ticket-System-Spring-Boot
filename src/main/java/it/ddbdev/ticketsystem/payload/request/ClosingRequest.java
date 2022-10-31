package it.ddbdev.ticketsystem.payload.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class ClosingRequest {

    private String uuid;
    private String status;
}
