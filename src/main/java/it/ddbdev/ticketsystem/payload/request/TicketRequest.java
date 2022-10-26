package it.ddbdev.ticketsystem.payload.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TicketRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String category;

    @NotBlank
    private String content;
}
