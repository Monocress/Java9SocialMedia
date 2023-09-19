package com.dorukt.controller;

import static com.dorukt.constant.EndPoints.*;

import com.dorukt.dto.reponse.RegisterResponseDto;
import com.dorukt.dto.request.ActivationRequestDto;
import com.dorukt.dto.request.UpdateRequestDto;
import com.dorukt.dto.request.LoginRequestDto;
import com.dorukt.dto.request.RegisterRequestDto;
import com.dorukt.repository.entity.Auth;
import com.dorukt.service.AuthService;
import com.dorukt.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenManager jwtTokenManager;
    private final CacheManager cacheManager;
    @PostMapping(REGISTER)
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto dto){
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(REGISTER+"withrabbit")
    public ResponseEntity<RegisterResponseDto> registerWithRabbitMq(@Valid @RequestBody RegisterRequestDto dto){
        return ResponseEntity.ok(authService.registerWithRabbitMq(dto));
    }


    @PostMapping(LOGIN)
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping(ACTIVATION)
    public ResponseEntity<String> activateStatus(@RequestBody ActivationRequestDto dto){
        return ResponseEntity.ok(authService.activateStatus(dto));
    }

    @DeleteMapping(DELETEBYID)
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        return ResponseEntity.ok(authService.deleteByAuthId(id));
    }

    @GetMapping("/gettoken")
    public ResponseEntity<String> gettoken(Long id){
        return ResponseEntity.ok(jwtTokenManager.createToken(id).get());
    }

    @GetMapping("/getidfromtoken")
    public ResponseEntity<Long> getIdFromToken(String token){
        return ResponseEntity.ok(jwtTokenManager.getIdFromToken(token).get());
    }

    @GetMapping("/getrolefromtoken")
    public ResponseEntity<String> getRoleFromToken(String token){
        return ResponseEntity.ok(jwtTokenManager.getRoleFromToken(token).get());
    }

    @PutMapping(UPDATE)
    public ResponseEntity<String> updateAuthWithUserProfileInformation(@RequestBody UpdateRequestDto dto,@RequestHeader(value = "Authorization") String token){
        return ResponseEntity.ok(authService.updateDto(dto));
    }


    @GetMapping(FINDALL)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<List<Auth>> findAll(){
        return ResponseEntity.ok(authService.findAll());
    }

    @GetMapping("redis")
    @Cacheable(value = "redisexample")
    public String redisExample(String value){
        try {
            Thread.sleep(2000);
            return value;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/redisdelete")
    @CacheEvict(cacheNames = "redisexample",allEntries = true)
    public void redisDelete(){
    }

    @GetMapping("/redisdelete2")
    public boolean redisDelete2(){
        try {
           // cacheManager.getCache("redisexample").clear(); //aynı isimle cachelenmiş bütün veriyi siler.
            cacheManager.getCache("redisexample").evict("mustafa");
            return true;
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

}
