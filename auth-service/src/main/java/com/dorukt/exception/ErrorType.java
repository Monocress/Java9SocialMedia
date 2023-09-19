package com.dorukt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorType {

    INTERNAL_ERROR_SERVER(5100, "Sunucu Hatası", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4100, "Parametre hatası", HttpStatus.BAD_REQUEST),
    USERNAME_EXIST(4110, "Kullanıcı Adı Zaten Mevcut.", HttpStatus.BAD_REQUEST),
    USER_NOT_CREATED(4111, "Kullanıcı oluşturulamadı!", HttpStatus.BAD_REQUEST),
    LOGIN_ERROR(4112, "Kullanıcı adı veya şifre hatalı!", HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_ACTIVE(4113, "Aktive edilmemiş hesap. Lütfen hesabınızı aktive ettikten sonra tekrar deneyin.", HttpStatus.FORBIDDEN),
    USER_NOT_FOUND(4114, "Böyle bir kullanıcı bulunamadı!", HttpStatus.NOT_FOUND),
    ACTIVATION_CODE_MISMATCH(4115, "Aktivasyon kodu hatalı!", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4116, "Geçersiz token!", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_CREATED(4117, "Token oluşturulamadı", HttpStatus.BAD_REQUEST);

    private int code;
    private String message;
    private HttpStatus status;
}
