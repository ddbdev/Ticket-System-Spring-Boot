package it.ddbdev.ticketsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.ddbdev.ticketsystem.entity.common.CreationUpdate;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length =15)
    private String username;

    @NaturalId(mutable = true)
    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private String bio;

    private boolean enabled = false;

    private String confirm; //Code UUID

    @ManyToMany
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = {
                @JoinColumn(name = "user_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                @JoinColumn(name = "authority_id", referencedColumnName = "id")
            }
    )
    private Set<Authority> authorities;

    @Column(name = "last_signin")
    private LocalDateTime lastSignin;


    @ManyToOne
    @JoinColumn(name = "avatar")
    private Avatar avatar;


    public User(String username, String email, String password, Set<Authority> authorities, String confirm) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.confirm = confirm;
    }

    public User(Long id) {
        this.id = id;
    }

    public User(long id, String username, String email, Set<Authority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
