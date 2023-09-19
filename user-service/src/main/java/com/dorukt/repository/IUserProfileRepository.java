package com.dorukt.repository;

import com.dorukt.repository.entity.UserProfile;
import com.dorukt.repository.enums.EStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserProfileRepository extends MongoRepository<UserProfile,String> {

    Optional<UserProfile> findByAuthId(Long authId);

    Optional<UserProfile> findByUsername(String username);

    List<UserProfile> findByStatus(EStatus status);

    @Query("{ 'username': ?0 }")
    Optional<UserProfile> getUser(String username);

    @Query("{ status: 'ACTIVE' }")
    List<UserProfile> findAllActiveProfile();

    @Query("{ authId: {$gt: ?0}}")
    List<UserProfile> findUserGtId(Long authId);

    @Query("{ $or:[{authId: {$gt: ?0}},{status: ?1}]}")
    List<UserProfile> findGtIdOrStatus(Long authId,EStatus status);



}
