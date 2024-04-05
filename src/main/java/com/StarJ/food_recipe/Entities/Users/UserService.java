package com.StarJ.food_recipe.Entities.Users;

import com.StarJ.food_recipe.Email.EmailService;
import com.StarJ.food_recipe.Securities.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    @Autowired
    private ResourceLoader resourceLoader;

    public boolean hasUser(String id) {
        return userRepository.findById(id).isPresent();
    }

    public boolean hasEmail(String email) {
        return userRepository.findAllByEmail(email).isPresent();
    }

    public SiteUser getUserbyID(String id) {
        Optional<SiteUser> optional = userRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            return null;
//            throw new DataNotFoundException("유저를 찾을 수 없습니다.");
    }

    public SiteUser getUserbyEmail(String email) {
        Optional<SiteUser> optional = userRepository.findAllByEmail(email);
        if (optional.isPresent())
            return optional.get();
        else
//            throw new DataNotFoundException("유저를 찾을 수 없습니다.");
            return null;
    }

    public SiteUser create(String id, String password, String nickname, String email) {
        SiteUser user = SiteUser.builder()
                .id(id)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .email(email)
                .role(UserRole.USER.getValue())
                .build();
        userRepository.save(user);
        return user;
    }

    public void sendIDEmail(SiteUser user) {
        emailService.sendMailReject(user.getEmail(), "아이디 찾기 기능", "당신의 아이디는 " + user.getId() + "입니다.\nhttp://localhost:8080/user/login");
    }

    public void sendPWEmail(SiteUser user) {
        String password = getRandomPassword();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        emailService.sendMailReject(user.getEmail(), "임시 비밀번호", "당신의 임시 비밀번호는 " + password + "입니다.\nhttp://localhost:8080/user/login");
    }

    private String getRandomPassword() {
        StringBuilder builder = new StringBuilder();
        Random r = new Random();
        SecureRandom sr = new SecureRandom();
        char[] rndAllCharacters = new char[]{
                //number
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                //uppercase
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                //lowercase
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                //special symbols
                '@', '$', '!', '%', '*', '?', '&'};
        int length = rndAllCharacters.length;
        for (int i = 0; i < 6 + r.nextInt(10); i++)
            builder.append(rndAllCharacters[r.nextInt(length)]);
        String randomPassword = builder.toString();
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}";
        if (!Pattern.matches(pattern, randomPassword)) return getRandomPassword();
        return randomPassword;
    }

    public String saveTempImage(MultipartFile file, SiteUser user) {
        if (!file.isEmpty())
            try {
                String path = resourceLoader.getResource("classpath:/static").getFile().getPath();
                File userFolder = new File(path + "/users/" + user.getId());
                if (!userFolder.exists())
                    userFolder.mkdirs();
                String fileloc = "/users/" + user.getId() + "/temp_profile." + file.getContentType().split("/")[1];
                file.transferTo(new File(path + fileloc));
                return fileloc;
//                user.setIconUrl();
//                userRepository.save(user);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        return null;
    }

    public void changeProfile(SiteUser user, String url, String nickname) {
        if (!user.getIconUrl().equals(url)) {
            try {
                String path = resourceLoader.getResource("classpath:/static").getFile().getPath();
                Path oldPath = Paths.get(path + url);
                String newUrl = url.replace("temp_", "");
                Path newPath = Paths.get(path + newUrl);
                Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                user.setIconUrl(newUrl);
            } catch (IOException e) {
            }
        }
        user.setNickname(nickname);
        userRepository.save(user);
    }

    public boolean isSamePassword(SiteUser user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }
    public void changePassword(SiteUser user, String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    public void changeEmail(SiteUser user, String newEmail){
        user.setEmail(newEmail);
        userRepository.save(user);
    }
}
