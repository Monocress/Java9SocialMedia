package com.dorukt.repository;

import com.dorukt.repository.entity.UserProfile;
import com.dorukt.repository.enums.EStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserProfileRepository extends ElasticsearchRepository<UserProfile,String> {
    List<UserProfile> findAllByEmailContainingIgnoreCase(String value);

    List<UserProfile> findAllByStatus(EStatus status);
    List<UserProfile> findAllByStatusOrAddress(EStatus status,String address);

    Optional<UserProfile> findByUsername(String username);
}
