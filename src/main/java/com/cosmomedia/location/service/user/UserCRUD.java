package com.cosmomedia.location.service.user;

import com.cosmomedia.location.dto.UsersDto;
import com.cosmomedia.location.entities.Message;
import com.cosmomedia.location.entities.Users;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Date;

public interface UserCRUD {

    /**
     * Retrieves a single user by Customer Identification Number (CIN).
     *
     * @param email Customer Identification Number.
     * @return The user with the specified email.
     */
    UsersDto getOneUser(String email);
    /**
     * Retrieves a single user by Customer Identification Number (CIN).
     *
     * @param id Customer Identification Number.
     * @return The user with the specified email.
     */
    UsersDto getOneUserByID(Long id);

    /**
     * Retrieves a paginated list of users.
     *
     * @param pageable Pagination information.
     * @return A Page containing the list of users.
     */
    Page<UsersDto> getUsersList(Pageable pageable);

    /**
     * Adds a new user based on the provided user entity.
     *
     * @param user User entity containing data for the new user.
     * @return A message indicating the success or failure of the operation.
     * @throws MessagingException If an error occurs during email communication.
     * @throws IOException        If an error occurs during file I/O.
     */
    Message addUser(Users user) throws MessagingException, IOException;

    /**.
     *
     * @param email Customer Identification Number of the user to be soft-deleted.
     * @return A message indicating the success or failure of the operation.
     */
    Message softDeleteUser(String email);

    /**.
     *
     * @param email Customer Identification Number of the user to be soft-deleted.
     * @return A message indicating the success or failure of the operation.
     */
    Message hardDeleteUser(String email);
    /**
     * Updates an existing user.
     *
     * @param user The user entity with updated information.
     * @return A message indicating the success or failure of the operation.
     */
    Message updateUser(Users user,String email);

    Page<UsersDto> getDeletedUsersList(Pageable pageable);
    Page<UsersDto> getPersonnel(Pageable pageable);

    Message restoreDeletedUser(String email);

    Page<Users> filterUsers(
            String firstName,
            String lastName,
            String email,
            String role,
            String deletedBy,
            Pageable pageable
    );

}
