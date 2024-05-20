//package com.projectA1;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import com.projectA1.model.Owner;
//import com.projectA1.model.User;
//import com.projectA1.repository.OwnerRepository;
//import com.projectA1.repository.UserRepository;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//
//@Component
//@RequiredArgsConstructor
//public class DataLoader {
//
//    private final UserRepository userRepository;
//    private final OwnerRepository ownerRepository;
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    @Transactional
//    public void loadData() {
//        // 데이터베이스에서 모든 사용자를 가져옴
//        List<String> userRole = new ArrayList<>();
//        userRole.add("ROLE_USER");
//
//        List<String> ownerRole = new ArrayList<>();
//        ownerRole.add("ROLE_OWNER");
//
//        List<User> users = userRepository.findAll();
//
//        // 각 사용자의 비밀번호를 암호화하여 저장
//        for (User user : users) {
//            System.out.println(user.getName());
//            String encryptedPassword = passwordEncoder.encode(user.getPassword());
//            user.setPassword(encryptedPassword);
//            user.setRole(userRole);
//            userRepository.save(user);
//        }
//
//        List<Owner> owners = ownerRepository.findAll();
//        // 각 사용자의 비밀번호를 암호화하여 저장
//        for (Owner owner : owners) {
//            String encryptedPassword = passwordEncoder.encode(owner.getPassword());
//            owner.setPassword(encryptedPassword);
//            owner.setRole(ownerRole);
//            ownerRepository.save(owner);
//        }
//    }
//}
