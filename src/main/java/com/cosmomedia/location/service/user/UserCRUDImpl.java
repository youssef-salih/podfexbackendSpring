package com.cosmomedia.location.service.user;

import com.cosmomedia.location.dto.UsersDto;
import com.cosmomedia.location.entities.Balance;
import com.cosmomedia.location.entities.Message;
import com.cosmomedia.location.entities.Users;
import com.cosmomedia.location.enums.Roles;
import com.cosmomedia.location.repositories.BalanceRepository;
import com.cosmomedia.location.repositories.UserRepository;
import com.cosmomedia.location.service.specifications.UserSpecifications;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCRUDImpl implements UserCRUD {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;

    private final PasswordEncoder passwordEncoder;


    private static byte[] convertStringToBytes(String byteString) {
        // Remove square brackets from the input string
        byteString = byteString.replaceAll("\\[|\\]", "");

        String[] byteValues = byteString.split(",");
        byte[] byteArray = new byte[byteValues.length];

        for (int i = 0; i < byteValues.length; i++) {
            byteArray[i] = Byte.parseByte(byteValues[i].trim());
        }

        return byteArray;
    }

    @Override
    public UsersDto getOneUser(String email) {
        Optional<Users> usersOptional = userRepository.findByEmail(email);
        return convertToUsersDto(usersOptional.get());
    }

    @Override
    public UsersDto getOneUserByID(Long id) {
        Optional<Users> usersOptional = userRepository.findById(id);
        return convertToUsersDto(usersOptional.get());
    }


    @Override
    public Page<UsersDto> getUsersList(Pageable pageable) {
        Page<Users> usersPage = userRepository.findAll(pageable);
        return usersPage.map(this::convertToUsersDto);
    }


    @Override
    public Message addUser(Users users) throws MessagingException, IOException {
        try {
            if (userRepository.findByEmail(users.getEmail()).isEmpty()) {
                saveUserAndSendWelcomeEmail(users);
                return new Message(true, "User added successfully, and welcome message sent.");
            } else {
                return new Message(false, "Email already used");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private UsersDto convertToUsersDto(Users users) {
        UsersDto usersDto = new UsersDto();
        usersDto.setId(users.getId());
        usersDto.setFirstName(users.getFirstName());
        usersDto.setLastName(users.getLastName());
        usersDto.setEmail(users.getEmail());
//        if (users.getPicture() != null) {
//            String pictureString = new String(users.getPicture(), StandardCharsets.UTF_8);
//
//            usersDto.setPicture(pictureString);
//        }

        // Set role enum directly
        usersDto.setRole(users.getRole());

        usersDto.setCreatedAt(users.getCreatedAt());
        usersDto.setModifiedAt(users.getModifiedAt());
        usersDto.setDeletedAt(users.getDeletedAt());
        usersDto.setDeletedBy(users.getDeletedBy());

        return usersDto;
    }

    @Transactional
    protected void saveUserAndSendWelcomeEmail(Users user) throws MessagingException, IOException {
        // Generate an 8-figure random password
        String randomPassword = generateRandomPassword(8);

        // Save user information to the database
        Users savedUser = Users.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(Roles.PERSONNEL)
                .createdAt(new Date())
                .modifiedAt(new Date())
                .password(passwordEncoder.encode(randomPassword))
                .build();
        userRepository.save(savedUser);

        // Send welcome email with the random password
        sendWelcomeEmail(savedUser, randomPassword);
    }

    private String generateRandomPassword(int length) {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characterSet.length());
            password.append(characterSet.charAt(index));
        }
        return password.toString();
    }

    private void sendWelcomeEmail(Users user, String password) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        // Set email details
        messageHelper.setFrom("d.youssefsalih@gmail.com");
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject("Welcome To Podfex");
        // Load email content from the HTML file in the resources/static directory
        ClassPathResource resource = new ClassPathResource("static/email.html");
        InputStream inputStream = resource.getInputStream();
        byte[] emailContentBytes = StreamUtils.copyToByteArray(inputStream);
        String emailContent = new String(emailContentBytes, StandardCharsets.UTF_8);

        // Replace placeholders in the email content
        if (user.getFirstName() != null) {
            emailContent = emailContent.replace("[NAME]", user.getFirstName());
        }
        if (user.getLastName() != null) {
            emailContent = emailContent.replace("[NAME]", user.getLastName());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(new Date());
        emailContent = emailContent.replace("[time here]", formattedDate);
        emailContent = emailContent.replace("[PASSWORD]", password); // Add the password to the email

        // Set the HTML content
        messageHelper.setText(emailContent, true);

        javaMailSender.send(mimeMessage);
    }

    private Message buildUpdateSuccessMessage(Users user) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Users currentUser) {
            if (currentUser.getRole() == Roles.ADMIN) {
                return new Message(true, "User updated successfully, (all fields).");
            } else {
                return new Message(true, "User updated successfully, (excluding email, role).");
            }
        }
        return new Message(true, "User updated successfully.");
    }

    @Override
    public Message updateUser(Users updatedUser, String email) {
        try {
            Optional<Users> existingUserOptional = userRepository.findByEmail(email);
            if (existingUserOptional.isPresent()) {
                Users existingUser = existingUserOptional.get();
                handleUpdatePermissions(existingUser, updatedUser);
                userRepository.save(existingUser);
                return buildUpdateSuccessMessage(existingUser);
            } else {
                return new Message(false, "User not found for the provided email.");
            }
        } catch (Exception e) {
            return new Message(false, "Error in updating the user: " + e.getMessage());
        }
    }

    private void handleUpdatePermissions(Users existingUser, Users updatedUser) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Users currentUser) {
//            if (currentUser.getRole() != Roles.ADMIN || currentUser.getId().equals(existingUser.getId())) {
//                // Update common fields
//                existingUser.setPicture(convertStringToBytes(Arrays.toString(updatedUser.getPicture())));
//
//            }
            if (currentUser.getRole() == Roles.ADMIN) {
                // Update additional fields if user is ADMIN
                existingUser.setFirstName(updatedUser.getFirstName());
                existingUser.setLastName(updatedUser.getLastName());
                existingUser.setEmail(updatedUser.getEmail());
//              existingUser.setPicture(convertStringToBytes(Arrays.toString(updatedUser.getPicture())));
//              existingUser.setRole(updatedUser.getRole());
            }
            existingUser.setModifiedAt(new Date()); // Update modifiedAt timestamp
        }
    }

    private void handleRestorePermissions(Users existingUser) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Users currentUser) {
            if (currentUser.getRole() == Roles.ADMIN) {
                existingUser.setDeletedAt(null);
                existingUser.setDeletedBy(null);
            }
        }
    }

    @Override
    public Message softDeleteUser(String email) {
        try {
            Optional<Users> existingUserOptional = userRepository.findByEmail(email);
            if (existingUserOptional.isPresent()) {
                Users existingUser = existingUserOptional.get();
                Users deletedUser = handleSoftDeletePermissions(existingUser);
                userRepository.save(deletedUser);
                return new Message(true, "User deleted successfully");
            } else {
                return new Message(false, "User not found");
            }
        } catch (Exception e) {
            return new Message(false, "Error in deleting the user: " + e.getMessage());
        }
    }

    @Override
    public Message hardDeleteUser(String email) {
        try {
            Optional<Users> existingUserOptional = userRepository.findByEmail(email);
            if (existingUserOptional.isPresent()) {
                userRepository.delete(existingUserOptional.get());
                return new Message(true, "User deleted successfully");
            } else {
                return new Message(false, "User not found");
            }
        } catch (Exception e) {
            return new Message(false, "Error in deleting the user: " + e.getMessage());
        }
    }

    private Users handleSoftDeletePermissions(Users existingUser) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Users currentUser) {
            if (currentUser.getRole() == Roles.ADMIN) {
                existingUser.setDeletedAt(new Date());
                existingUser.setDeletedBy(currentUser.getFirstName() + " " + currentUser.getLastName());
                return existingUser;
            }
        }
        return existingUser;
    }

    @Override
    public Page<UsersDto> getDeletedUsersList(Pageable pageable) {
        Page<Users> usersPage = userRepository.findByDeletedAtNotNullOrderByDeletedAtDesc(pageable);
        return usersPage.map(this::convertToUsersDto);
    }

    @Override
    public Page<UsersDto> getPersonnel(Pageable pageable) {
        Page<Users> personnelPage = userRepository.findByRole(Roles.PERSONNEL, pageable);
        return personnelPage.map(this::convertToUsersDto);
    }

    @Override
    public Message restoreDeletedUser(String email) {
        try {
            Optional<Users> existingUserOptional = userRepository.findByEmail(email);
            if (existingUserOptional.isPresent()) {
                Users existingUser = existingUserOptional.get();
                handleRestorePermissions(existingUser);
                userRepository.save(existingUser);
                return new Message(true, "User restored successfully");
            } else {
                return new Message(false, "User not found");
            }
        } catch (Exception e) {
            return new Message(false, "Error in restoring the user: " + e.getMessage());
        }
    }

    @Override
    public Page<Users> filterUsers(String firstName, String lastName, String email,
                                   String role, String deletedBy, Pageable pageable) {
        Specification<Users> specification = Specification.where(UserSpecifications.filterByFirstName(firstName))
                .and(UserSpecifications.filterByLastName(lastName))
                .and(UserSpecifications.filterByEmail(email))
                .and(UserSpecifications.filterByRole(role))
                .and(UserSpecifications.filterByDeletedBy(deletedBy));
        return userRepository.findAll(specification, pageable);
    }
}