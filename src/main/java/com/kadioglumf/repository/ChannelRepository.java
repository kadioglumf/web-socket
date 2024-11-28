package com.kadioglumf.repository;

import com.kadioglumf.model.Channel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
  void deleteByName(String name);

  Optional<Channel> findByName(String name);

  @Query("SELECT c FROM Channel c JOIN c.roles r WHERE r IN :roles")
  List<Channel> findAllByRoles(@Param("roles") List<String> roles);
}
