package it.ddbdev.ticketsystem.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.ddbdev.ticketsystem.entity.Avatar;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetMeResponse {
    private Long id;
    private String username;
    private String email;
    private String bio;
    private Avatar avatar;
    private long days;
    @JsonIgnore
    private LocalDateTime createdAt;
}
