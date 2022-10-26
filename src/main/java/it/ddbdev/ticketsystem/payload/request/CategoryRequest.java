package it.ddbdev.ticketsystem.payload.request;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
public class CategoryRequest {

    @NotBlank
    @Length(min = 3 , max = 15)
    private String id;
}


