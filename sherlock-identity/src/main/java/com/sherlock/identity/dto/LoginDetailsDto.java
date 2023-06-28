package com.sherlock.identity.dto;

import com.sherlock.identity.persistance.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class LoginDetailsDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @ToString.Exclude
    private CharSequence password;

    public User toUser() {
        return User.builder()
                .email(this.getEmail())
                .password(this.getPassword().toString())
                .build();
    }
}
