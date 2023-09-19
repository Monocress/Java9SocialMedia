package com.dorukt.repository.entity;

import com.dorukt.repository.enums.ERole;
import com.dorukt.repository.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Entity
public class Auth extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    private String activationCode;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ERole role=ERole.USER;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private EStatus status=EStatus.PENDING;

    /*
    Username
    email
    password
    activationCode
    role -> User, Admin
    status -> Active, Deleted, Pending, Banned, Inactive
     */
}
