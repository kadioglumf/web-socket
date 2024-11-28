package com.kadioglumf.repository;

import com.kadioglumf.model.User;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT c FROM User c JOIN c.roles r WHERE r IN :roles")
  List<User> findAllByRoles(@Param("roles") Set<String> roles);
}
