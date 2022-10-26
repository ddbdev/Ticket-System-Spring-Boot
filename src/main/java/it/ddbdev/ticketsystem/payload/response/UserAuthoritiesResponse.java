package it.ddbdev.ticketsystem.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthoritiesResponse {

    private UserResponse userResponse;
    private List<AuthorityUserResponse> authorities;
}
