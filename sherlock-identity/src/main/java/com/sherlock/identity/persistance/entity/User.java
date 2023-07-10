package com.sherlock.identity.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;


@Entity
@Table(name = "users", schema = "sherlock_schema")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class User {

    @Id
    @GeneratedValue
    private UUID uuid;
    private String email;
    private String nickname;
    private String firstname;
    private String lastname;
    @ToString.Exclude
    private String password;
    private String oidcId;

}
