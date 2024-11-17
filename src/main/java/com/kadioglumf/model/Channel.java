package com.kadioglumf.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "channel")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="channel_roles"
            ,joinColumns=@JoinColumn(name="channel_id"))
    private Set<String> roles;
}
