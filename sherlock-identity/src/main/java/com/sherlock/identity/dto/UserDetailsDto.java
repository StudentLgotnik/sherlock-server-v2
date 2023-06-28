package com.sherlock.identity.dto;

import com.sherlock.identity.persistance.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserDetailsDto {
    @NotBlank
    private String uuid;
    @NotBlank
    private String email;
    private String nickname;
    private String firstname;
    private String lastname;

    public static UserDetailsDto fromUser(User user) {
        return UserDetailsDto.builder()
                .uuid(user.getUuid().toString())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .build();
    }
}
