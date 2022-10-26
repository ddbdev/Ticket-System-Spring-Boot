package it.ddbdev.ticketsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@RequiredArgsConstructor
@Data
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "authority_name", length = 20, nullable = false, unique = true)
    private String authorityName;

    private boolean visible = true;

    @Column(name = "default_authority")
    private boolean defaultAuthority = false;

    @JsonIgnore
    @ManyToMany(mappedBy = "authorities")
    private List<User> users = new ArrayList<>();

    public Authority(String authorityName) {
        this.authorityName = authorityName;
    }
}
