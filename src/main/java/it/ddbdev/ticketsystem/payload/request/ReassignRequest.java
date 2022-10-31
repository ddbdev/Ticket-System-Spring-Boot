package it.ddbdev.ticketsystem.payload.request;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ReassignRequest {

    @NotNull
    @Min(1)
    private Long ticketId;

    private Long userId;
}
