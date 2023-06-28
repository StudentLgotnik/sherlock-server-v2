package com.sherlock.gateway.dto;

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
public class UserDetailsDto {

    @NotBlank
    private String uuid;
    @NotBlank
    private String email;
    private String nickname;
    private String firstname;
    private String lastname;
}
