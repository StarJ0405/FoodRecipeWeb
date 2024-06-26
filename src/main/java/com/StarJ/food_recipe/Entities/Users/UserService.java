package com.StarJ.food_recipe.Entities.Users;

import com.StarJ.food_recipe.Email.EmailService;
import com.StarJ.food_recipe.FoodRecipeApplication;
import com.StarJ.food_recipe.Securities.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    public void reset(){
        userRepository.deleteAll();
    }
    public List<SiteUser> getUsers() {
        return userRepository.findAll();
    }
    public List<String> getUsersId() {
        return userRepository.getUsersId();
    }
    public Long getCount(){
        return userRepository.getCount();
    }
    public List<SiteUser> getUsers(String role) {
        return userRepository.findAllByRole(role);
    }

    public Page<SiteUser> getUsers(int page) {
        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, sort);
        return userRepository.findAll(pageable);
    }

    public boolean hasUser(String id) {
        return userRepository.findById(id).isPresent();
    }

    public boolean hasEmail(String email) {
        return userRepository.findAllByEmail(email).isPresent();
    }

    public SiteUser getUserbyID(String id) {
        Optional<SiteUser> optional = userRepository.findById(id);
        return optional.orElse(null);
//            throw new DataNotFoundException("유저를 찾을 수 없습니다.");
    }

    public SiteUser getUserbyEmail(String email) {
        Optional<SiteUser> optional = userRepository.findAllByEmail(email);
        //            throw new DataNotFoundException("유저를 찾을 수 없습니다.");
        return optional.orElse(null);
    }

    public SiteUser create(String id, String password, String nickname, String email, UserRole userRole) {
        SiteUser user = SiteUser.builder()
                .id(id)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .email(email)
                .role(userRole.getValue())
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
                String path = FoodRecipeApplication.getOS_TYPE().getPath();
                File userFolder = new File(path + "/users/" + user.getId());
                if (!userFolder.exists())
                    userFolder.mkdirs();
                String fileloc = "/users/" + user.getId() + "/temp_profile." + file.getContentType().split("/")[1];
                file.transferTo(new File(path + fileloc));
                return fileloc;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        return null;
    }

    public SiteUser changeProfile(SiteUser user, String url, String nickname) {
        if (!user.getIconUrl().equals(url)) {
            try {
                String path = FoodRecipeApplication.getOS_TYPE().getPath();
                Path oldPath = Paths.get(path + url);
                String newUrl = url.replace("temp_", "");
                Path newPath = Paths.get(path + newUrl);
                Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                user.setIconUrl(newUrl);
            } catch (IOException e) {
            }
        }
        user.setNickname(nickname);
        return userRepository.save(user);
    }

    public boolean isSamePassword(SiteUser user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public void changePassword(SiteUser user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void changeEmail(SiteUser user, String newEmail) {
        user.setEmail(newEmail);
        userRepository.save(user);
    }
}
