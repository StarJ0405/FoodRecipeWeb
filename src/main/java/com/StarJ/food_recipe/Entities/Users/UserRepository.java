package com.StarJ.food_recipe.Entities.Users;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<SiteUser, String> {
    @Query("select "
            + "distinct q "
            + "from SiteUser q "
            + "where "
            + "   q.email = :email")
    Optional<SiteUser> findAllByEmail(@Param("email") String email);

}
