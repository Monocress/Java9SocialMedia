package com.dorukt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data //@Getter @Setter @ToString hepsini kapsıyor.
@Builder
public class UserProfileUpdateRequestDto {
    private String token;
    @NotBlank(message = "Kullanıcı adı boş geçilemez!")
    @Size(min = 2, max = 20, message = "Kullanıcı adı en az 2, en fazla 20 karakter olabilir!")
    private String username;
    @Email
    private String email;
    private String phone;
    private String avatar;
    private String address;
    private String about;
}
