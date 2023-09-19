package com.dorukt.service;

import com.dorukt.dto.request.UserProfileUpdateRequestDto;
import com.dorukt.dto.request.UserSaveRequestDto;
import com.dorukt.exception.ErrorType;
import com.dorukt.exception.UserManagerException;
import com.dorukt.manager.IAuthManager;
import com.dorukt.mapper.IUserMapper;
import com.dorukt.rabbitmq.model.ActivateModel;
import com.dorukt.rabbitmq.model.RegisterModel;
import com.dorukt.rabbitmq.producer.RegisterElasticProducer;
import com.dorukt.repository.IUserProfileRepository;
import com.dorukt.repository.entity.UserProfile;
import com.dorukt.repository.enums.EStatus;
import com.dorukt.utility.JwtTokenManager;
import com.dorukt.utility.ServiceManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/*
 *findByUsername metodu yazalım kullanıcı ismine göre bir userprofile donsun
 * bu işlemi cache kullnarak tasarlayalım.
 *
 * findbystatus
 */
@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {


    private final IUserProfileRepository userProfileRepository;
    private final JwtTokenManager jwtTokenManager;
    private final IAuthManager authManager;
    private final CacheManager cacheManager;
    private final RegisterElasticProducer registerElasticProducer;

    public UserProfileService(IUserProfileRepository userProfileRepository, JwtTokenManager jwtTokenManager, IAuthManager authManager, CacheManager cacheManager, RegisterElasticProducer registerElasticProducer) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.authManager = authManager;
        this.cacheManager = cacheManager;
        this.registerElasticProducer = registerElasticProducer;
    }


    public Boolean createNewUser(UserSaveRequestDto dto) {
        try {
            save(IUserMapper.INSTANCE.toUserProfile(dto));
            return true;
        } catch (Exception e) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }

    }

    public String activateUser(Long authId) {

        Optional<UserProfile> profile = userProfileRepository.findByAuthId(authId);
        if (profile.isEmpty())
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        cacheManager.getCache("findbystatus2").evict(profile.get().getStatus());
        cacheManager.getCache("findbystatus").evict(profile.get().getStatus());
        profile.get().setStatus(EStatus.ACTIVE);

        update(profile.get());
        cacheManager.getCache("findbystatus2").evict(profile.get().getStatus());
        cacheManager.getCache("findbystatus").evict(profile.get().getStatus());
        return "Hesap başarıyla aktif edilmiştir.";
    }

    @Transactional
    public String updateUserInformation(UserProfileUpdateRequestDto dto) {
        Optional<Long> authId = jwtTokenManager.getIdFromToken(dto.getToken());
        if (authId.isEmpty())
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        Optional<UserProfile> userProfile = userProfileRepository.findByAuthId(authId.get());
        if (userProfile.isEmpty())
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        if (!userProfile.get().getEmail().equals(dto.getEmail()) || !userProfile.get().getUsername().equals(dto.getUsername())) {
            userProfile.get().setEmail(dto.getEmail());
            userProfile.get().setUsername(dto.getUsername());
            //Auth-micro serviceine ulşaştığımız istek attığımız kısım.
            authManager.updateAuthWithUserProfileInformation(IUserMapper.INSTANCE.toUpdateRequestDto(userProfile.get()),"Bearer "+dto.getToken());
        }
        userProfile.get().setAvatar(dto.getAvatar());
        userProfile.get().setAbout(dto.getAbout());
        userProfile.get().setPhone(dto.getPhone());
        userProfile.get().setAddress(dto.getAddress());
        update(userProfile.get());
        return "Güncelleme başarılı.";
    }

    public Boolean createNewUserWithRabbitmq(RegisterModel registerModel) {
        try {
            UserProfile profile = save(IUserMapper.INSTANCE.toUserProfile(registerModel));
            registerElasticProducer.saveNewUser(IUserMapper.INSTANCE.toRegisterElasticModel(profile));
            return true;
        } catch (Exception e) {
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }


    @Cacheable(value = "findbyusername", key = "#username.toLowerCase()")
    public UserProfile findByUsername(String username) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Optional<UserProfile> username1 = userProfileRepository.findByUsername(username.toLowerCase());
        if (username1.isEmpty())
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        return username1.get();
    }

    @CacheEvict(value = "findbyusername", allEntries = true)
    public void clearfbuCache() {
    }

    @Cacheable(value = "findbystatus")
    public List<UserProfile> findByStatus(EStatus status) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<UserProfile> profiles = userProfileRepository.findByStatus(status);
        if (profiles.isEmpty())
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        return profiles;
    }

    @Cacheable(value = "findbystatus2", key = "#status.toUpperCase()")
    public List<UserProfile> findByStatus2(String status) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<UserProfile> profiles = null;
        try {
            profiles = userProfileRepository.findByStatus(EStatus.valueOf(status.toUpperCase(Locale.ENGLISH)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserManagerException(ErrorType.STATUS_NOT_FOUND);
        }
        if (profiles.isEmpty())
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        return profiles;
    }

    public Page<UserProfile> findAllByPageable(int pageSize, int pageNumber, String direction, String sortParameter) {
        Sort sort= Sort.by(Sort.Direction.fromString(direction),sortParameter);
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        return userProfileRepository.findAll(pageable);

    }

    public Slice<UserProfile> findAllBySlice(int pageSize, int pageNumber, String direction, String sortParameter) {
        Sort sort= Sort.by(Sort.Direction.fromString(direction),sortParameter);
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        return userProfileRepository.findAll(pageable);
    }

    public UserProfile getUser(String username){
        return userProfileRepository.getUser(username).get();
    }
    public List<UserProfile> findAllActiveProfile(){
        return userProfileRepository.findAllActiveProfile();
    }

   public List<UserProfile> findUserGtId(Long authId){
        return userProfileRepository.findUserGtId(authId);
   }
   public   List<UserProfile> findGtIdOrStatus(Long authId,EStatus status){
        return userProfileRepository.findGtIdOrStatus(authId,status);
   }

    public UserProfile findByAuthId(Long id) {
        Optional<UserProfile> userProfile = userProfileRepository.findByAuthId(id);
        if(userProfile.isPresent())
            return userProfile.get();
        throw new UserManagerException(ErrorType.USER_NOT_FOUND);
    }
}
