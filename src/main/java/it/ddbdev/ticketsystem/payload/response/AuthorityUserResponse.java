package it.ddbdev.ticketsystem.payload.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityUserResponse {
    private Long id;
    private String authorityName;
    private boolean enabled;
}
