package com.dorukt.service;

import com.dorukt.dto.reponse.RegisterResponseDto;
import com.dorukt.dto.request.ActivationRequestDto;
import com.dorukt.dto.request.UpdateRequestDto;
import com.dorukt.dto.request.LoginRequestDto;
import com.dorukt.dto.request.RegisterRequestDto;
import com.dorukt.exception.AuthManagerException;
import com.dorukt.exception.ErrorType;
import com.dorukt.manager.IUserManager;
import com.dorukt.mapper.IAuthMapper;
import com.dorukt.rabbitmq.producer.ActivationProducer;
import com.dorukt.rabbitmq.producer.MailProducer;
import com.dorukt.rabbitmq.producer.RegisterProducer;
import com.dorukt.repository.IAuthRepository;
import com.dorukt.repository.entity.Auth;
import com.dorukt.repository.enums.EStatus;
import com.dorukt.utility.CodeGenerator;
import com.dorukt.utility.JwtTokenManager;
import com.dorukt.utility.ServiceManager;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth, Long> {

    private final IAuthRepository authRepository;
    private final JwtTokenManager jwtTokenManager;

    private final IUserManager userManager;

    private final RegisterProducer registerProducer;

    private final ActivationProducer activationProducer;
    private final MailProducer mailProducer;
    private final CacheManager cacheManager;

    private final PasswordEncoder passwordEncoder;

    public AuthService(IAuthRepository authRepository, JwtTokenManager jwtTokenManager, IUserManager userManager, RegisterProducer registerProducer, ActivationProducer activationProducer, MailProducer mailProducer, CacheManager cacheManager, PasswordEncoder passwordEncoder) {
        super(authRepository);
        this.authRepository = authRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.userManager = userManager;
        this.registerProducer = registerProducer;
        this.activationProducer = activationProducer;
        this.mailProducer = mailProducer;
        this.cacheManager = cacheManager;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponseDto registerWithRabbitMq(RegisterRequestDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);
        auth.setActivationCode(CodeGenerator.genarateCode());
        try {
            save(auth);
            cacheManager.getCache("findbystatus2").evict(auth.getStatus());
            cacheManager.getCache("findbystatus").evict(auth.getStatus());
            mailProducer.sendMail(IAuthMapper.INSTANCE.toMailModel(auth));
            registerProducer.sendNewUser(IAuthMapper.INSTANCE.toRegisterModel(auth));
            return IAuthMapper.INSTANCE.toRegisterResponseDto(auth);
        } catch (DataIntegrityViolationException e) {
            throw new AuthManagerException(ErrorType.USERNAME_EXIST);
        } catch (Exception e) {
            throw new AuthManagerException(ErrorType.USER_NOT_CREATED);
        }
    }


    @Transactional
    public RegisterResponseDto register(RegisterRequestDto dto) {
        //firma fildedı dolu mu
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);

        auth.setActivationCode(CodeGenerator.genarateCode());
        try {
            save(auth);
            String token = "Bearer " + jwtTokenManager.createToken(auth.getId(), auth.getRole()).get();
            userManager.createNewUser(IAuthMapper.INSTANCE.toUserSaveRequestDto(auth), token);
            return IAuthMapper.INSTANCE.toRegisterResponseDto(auth);
        } catch (DataIntegrityViolationException e) {
            throw new AuthManagerException(ErrorType.USERNAME_EXIST);
        } catch (Exception e) {
            throw new AuthManagerException(ErrorType.USER_NOT_CREATED);
        }
    }

    public String login(LoginRequestDto dto) {
        Optional<Auth> auth = authRepository.findOptionalByUsername(dto.getUsername());
        if (auth.isEmpty() || !passwordEncoder.matches(dto.getPassword(), auth.get().getPassword()))
            throw new AuthManagerException(ErrorType.LOGIN_ERROR);
        if (!auth.get().getStatus().equals(EStatus.ACTIVE)) {
            throw new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
        Optional<String> token = jwtTokenManager.createToken(auth.get().getId(), auth.get().getRole());
        if (token.isEmpty())
            throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
        // return IAuthMapper.INSTANCE.toLoginResponseDto(auth.get());
        return token.get();
    }


    @Transactional
    public String activateStatus(ActivationRequestDto dto) {
        Optional<Auth> auth = findById(dto.getId());
        if (auth.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        if (!auth.get().getActivationCode().equals(dto.getActivationCode()))
            throw new AuthManagerException(ErrorType.ACTIVATION_CODE_MISMATCH);
        return statusControl(auth.get());
    }


    private String statusControl(Auth auth) {
        switch (auth.getStatus()) {
            case ACTIVE -> {
                return "Hesap Zaten aktif";
            }
            case PENDING, INACTIVE -> {
                auth.setStatus(EStatus.ACTIVE);
                update(auth);
                // activationProducer.activateNewUser(IAuthMapper.INSTANCE.toActivateModel(auth));
                activationProducer.activation(auth.getId());
                // userManager.activateUser2(auth.getId());
                return "Hesabınız aktif edilmiştir.";
            }
            case BANNED -> {
                return "Engellenmiş Hesap";
            }
            case DELETED -> {
                return "Silinmiş hesap";
            }
            default -> throw new AuthManagerException(ErrorType.BAD_REQUEST);
        }
    }

    public String deleteByAuthId(Long id) {
        Optional<Auth> auth = findById(id);
        if (auth.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        if (auth.get().getStatus().equals(EStatus.DELETED))
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND, "Hesap zaten silinmiş!");
        auth.get().setStatus(EStatus.DELETED);
        update(auth.get());
        return id + " ID'li Hesap silindi.";

    }


    public String updateDto(UpdateRequestDto dto) {
        Optional<Auth> auth = authRepository.findById(dto.getId());
        if (auth.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        if (!auth.get().getUsername().equals(dto.getUsername()) && authRepository.existsByUsername(dto.getUsername()))
            throw new AuthManagerException(ErrorType.USERNAME_EXIST);
        auth.get().setUsername(dto.getUsername());
        auth.get().setEmail(dto.getEmail());
        update(auth.get());
        return "Güncelleme başarılı";
    }
}
