package ru.otus.spring.task.manager.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@NamedEntityGraph(
        name = "users-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("userRoles")
        }
)
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    public UserEntity(Long id, String login, String email) {
        this.id = id;
        this.login = login;
        this.email = email;
    }

    @OneToMany(targetEntity = UserRoleEntity.class)
    @JoinColumn(name = "user_id")
    private List<UserRoleEntity> userRoles = new ArrayList<>();

    public UserEntity(String login) {
        this.login = login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
