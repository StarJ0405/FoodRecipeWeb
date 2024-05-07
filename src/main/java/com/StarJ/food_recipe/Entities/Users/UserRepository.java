package com.StarJ.food_recipe.Entities.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<SiteUser, String>, UserRepositoryCustom {
    @Query("select "
            + "distinct u "
            + "from SiteUser u "
            + "where "
            + "   u.provider is null "
            + "and "
            + "   u.email = :email ")
    Optional<SiteUser> findAllByEmail(@Param("email") String email);

    @Query("select " +
            "distinct u " +
            "from SiteUser u " +
            "where " +
            "   u.role = :role ")
    List<SiteUser> findAllByRole(@Param("role") String role);

}
