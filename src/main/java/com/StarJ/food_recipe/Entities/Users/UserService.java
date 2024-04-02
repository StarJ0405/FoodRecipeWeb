package com.StarJ.food_recipe.Entities.Users;

import com.StarJ.food_recipe.Exceptions.DataNotFoundException;
import com.StarJ.food_recipe.Securities.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean hasUser(String id) {
        return userRepository.findById(id).isPresent();
    }

    public SiteUser getUserbyID(String id) {
        Optional<SiteUser> optional = userRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            throw new DataNotFoundException("유저를 찾을 수 없습니다.");
    }
    public SiteUser getUserbyEmail(String email){
        Optional<SiteUser> optional = userRepository.findAllByEmail(email);
        if(optional.isPresent())
            return optional.get();
        else
            throw new DataNotFoundException("");
    }

    public SiteUser create(String id, String password, String nickname, String email) {
        SiteUser user = SiteUser.builder()
                .id(id)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .email(email)
                .role(UserRole.USER.getValue())
                .createDate(LocalDateTime.now())
                .emailVerified(true)
                .build();
        userRepository.save(user);
        return user;
    }

}
