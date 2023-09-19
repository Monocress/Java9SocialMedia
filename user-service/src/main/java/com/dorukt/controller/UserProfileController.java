package com.dorukt.controller;

import static com.dorukt.constant.EndPoints.*;

import com.dorukt.dto.reponse.UserProfileResponseDto;
import com.dorukt.dto.request.UserProfileUpdateRequestDto;
import com.dorukt.dto.request.UserSaveRequestDto;
import com.dorukt.mapper.IUserMapper;
import com.dorukt.repository.entity.UserProfile;
import com.dorukt.repository.enums.EStatus;
import com.dorukt.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/*
createNewUser metoduyla beraber gelen isteği karşılayıp yeni bir userprofile oluşturacağız.

Update metodu oluşturacagız login olduktan sonra gelen veri ile databaseden bir user profile bulup onu güncelleyeceğiz
eğer username veya emailden biri değişmiş ise auth servicede de ilişkili auth'u güncellesin.
Eğer değişmemiş ise auth service'e istek atmaya gerek yoktur
sadece user profileda güncelleme yapsın.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping(SAVE)
    public ResponseEntity<Boolean> createNewUser(@RequestBody UserSaveRequestDto dto,@RequestHeader(value = "Authorization") String token){
        return ResponseEntity.ok(userProfileService.createNewUser(dto));
    }

    @PostMapping(ACTIVATION + "/{authId}")
    public ResponseEntity<String> activateUser(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.activateUser(authId));
    }

    @PostMapping(ACTIVATION)
    public ResponseEntity<String> activateUser2(@RequestParam Long authId) {
        return ResponseEntity.ok(userProfileService.activateUser(authId));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<String> updateUserInformation(@Valid @RequestBody UserProfileUpdateRequestDto dto, @RequestHeader(value = "Authorization")String token) {
        return ResponseEntity.ok(userProfileService.updateUserInformation(dto));
    }

    @GetMapping(FINDALL)
    public ResponseEntity<List<UserProfile>> findAll() {
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @PostMapping(FINDBYUSERNAME)
    public ResponseEntity<UserProfile> findByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userProfileService.findByUsername(username));
    }

    @GetMapping("/clearfindbyUsername")
    public ResponseEntity<Void> clearfbuCache() {
        userProfileService.clearfbuCache();
        return ResponseEntity.ok().build();
    }

    @GetMapping(FINDBYSTATUS)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<UserProfile>> findByStatus(EStatus status) {
        return ResponseEntity.ok(userProfileService.findByStatus(status));
    }

    @GetMapping(FINDBYSTATUS + "2")
    public ResponseEntity<List<UserProfile>> findByStatus2(String status) {
        return ResponseEntity.ok(userProfileService.findByStatus2(status));
    }

    @GetMapping(FINDALL + "forelastic")
    public ResponseEntity<List<UserProfileResponseDto>> findAllforElasticService() {
        List<UserProfileResponseDto> list = userProfileService.findAll().stream()
                .map(x -> IUserMapper.INSTANCE.toUserProfileResponseDto(x)).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/findallbypageable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<UserProfile>> findAllByPageable(int pageSize, int pageNumber, @RequestParam(required = false, defaultValue = "ASC") String direction, @RequestParam(required = false, defaultValue = "id") String sortParameter) {
        return ResponseEntity.ok(userProfileService.findAllByPageable(pageSize, pageNumber, direction, sortParameter));
    }

    @GetMapping("/findallbyslice")
    public ResponseEntity<Slice<UserProfile>> findAllBySlice(int pageSize, int pageNumber, @RequestParam(required = false, defaultValue = "ASC") String direction, @RequestParam(required = false, defaultValue = "id") String sortParameter) {
        return ResponseEntity.ok(userProfileService.findAllBySlice(pageSize, pageNumber, direction, sortParameter));
    }

    @GetMapping("/getuser")
    public ResponseEntity<UserProfile> getUser(String username) {
        return ResponseEntity.ok(userProfileService.getUser(username));
    }

    @GetMapping("/findallactiveprofile")
    public ResponseEntity<List<UserProfile>> findAllActiveProfile() {
        return ResponseEntity.ok(userProfileService.findAllActiveProfile());
    }

    @GetMapping("/findusergtid")
    public ResponseEntity<List<UserProfile>> findUserGtId(Long authId) {
        return ResponseEntity.ok(userProfileService.findUserGtId(authId));
    }
    @GetMapping("/findusergtidorstatus")
    public ResponseEntity<List<UserProfile>> findGtIdOrStatus(Long authId,EStatus status) {
        return ResponseEntity.ok(userProfileService.findGtIdOrStatus(authId,status));
    }
}
