package com.StarJ.food_recipe.Entities.Users;

import com.StarJ.food_recipe.Exceptions.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public SiteUser getUser(String id) {
        Optional<SiteUser> optional = userRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            throw new DataNotFoundException("유저를 찾을 수 없습니다.");
    }

    public SiteUser create(String id,String password, String email, String role, String provider, String providerId) {
        SiteUser user = SiteUser.builder()
                .id(id)
                .email(email)
                .role(role)
                .provider(provider)
                .providerId(providerId)
                .build();
        userRepository.save(user);
        return user;
    }

}
