package com.issuetracker.issuetracker.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// It extends the JpaRepository interface, indicating that RoleRepository will inherit CRUD methods from JpaRepository.
public interface RoleRepository extends JpaRepository<Role, Integer> {

    //used to retrieve a Role entity by its name. It returns an Optional<Role>, which may contain either a non-null Role object if found or an empty Optional if not found.
    Optional <Role> findByName(String name);
}
