package com.ClientContactAPI.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data @NoArgsConstructor
@Entity
@Table(name = "CLIENTS")
public class Client {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private long id;
	@Column(name = "NAME")
    private String name;
}
