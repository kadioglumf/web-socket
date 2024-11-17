package com.kadioglumf.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "channel")
@Getter
@Setter
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="channel_roles"
            ,joinColumns=@JoinColumn(name="channel_id"))
    private Set<String> roles;
}
