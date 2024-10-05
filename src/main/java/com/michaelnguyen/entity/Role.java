package com.michaelnguyen.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Role {
	@Id
	private String name;

	private String description;

	@ManyToMany
	private Set<Permission> permissions;          

//	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinTable(name = "roles_permissions", joinColumns = @JoinColumn(name = "role_name", referencedColumnName = "name"), inverseJoinColumns = @JoinColumn(name = "permission_name", referencedColumnName = "name"))
//	private Set<Permission> permissions;
}
