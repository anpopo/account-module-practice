package io.account.management.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.account.management.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Entity
public class Users extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles role;

    @Builder
    public Users(String name, String email, String imageUrl, Boolean emailVerified, String password, AuthProvider provider, String providerId, Roles role) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.emailVerified = emailVerified;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
    }

    public void updateNameAndImageUrl(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
