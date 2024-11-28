package com.kadioglumf.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "channel")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Channel extends AbstractModel {

  @Column(unique = true, nullable = false)
  private String name;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "channel_roles", joinColumns = @JoinColumn(name = "channel_id"))
  private Set<String> roles;

  /*  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "channel_subscribers", joinColumns = @JoinColumn(name = "channel_id"))
  private Set<Long> subscribers = new HashSet<>();*/

  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<UserChannelPreferences> userChannelPreferences = new ArrayList<>();
}
