package org.barrikeit.repository;

import java.util.Optional;
import org.barrikeit.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends GenericRepository<User, Long> {

  Optional<User> findByUsernameEqualsIgnoreCase(String user);

  Optional<User> findByEmailEqualsIgnoreCase(String email);

  Optional<User> findByUsernameEqualsIgnoreCaseAndEmailEqualsIgnoreCase(String user, String email);
}
