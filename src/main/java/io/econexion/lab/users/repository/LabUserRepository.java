package io.econexion.lab.users.repository;



import io.econexion.lab.users.model.LabUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LabUserRepository extends MongoRepository<LabUser, String> {
    Optional<LabUser> findByEmail(String email);
}
