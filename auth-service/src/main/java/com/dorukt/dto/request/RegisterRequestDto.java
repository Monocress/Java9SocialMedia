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
@Data
@Builder
public class RegisterRequestDto {
    @NotBlank(message = "Kullanıcı adı boş geçilemez!")
    @Size(min = 2, max = 20, message = "Kullanıcı adı en az 2, en fazla 20 karakter olabilir!")
    private String username;
    @Email
    private String email;
    @NotBlank(message = "Şifre boş bırakılamaz!")
    @Size(min = 5, max = 32, message = "Şifreniz en az 5, en fazla 32 karakter olabilir!")
    private String password;
}
