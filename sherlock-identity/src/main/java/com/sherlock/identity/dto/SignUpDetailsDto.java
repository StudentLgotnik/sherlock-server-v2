package com.sherlock.identity.dto;

import com.sherlock.identity.persistance.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class SignUpDetailsDto extends  LoginDetailsDto {

    private String nickname;
    private String firstname;
    private String lastname;

    public User toUser() {
        return User.builder()
                .email(this.getEmail())
                .password(this.getPassword().toString())
                .nickname(this.getNickname())
                .firstname(this.getFirstname())
                .lastname(this.getLastname())
                .build();
    }
}
