package com.cosmomedia.location.controllers;

import com.cosmomedia.location.dto.UsersDto;
import com.cosmomedia.location.entities.Message;
import com.cosmomedia.location.entities.Users;
import com.cosmomedia.location.service.user.UserCRUD;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class UserControllers {
    private final UserCRUD userCRUD;

    @QueryMapping
    public UsersDto getOneUser(@Argument(name = "email") String email) {
        return userCRUD.getOneUser(email);
    }


    @QueryMapping
    public Page<UsersDto> allUsers(@Argument(name = "page") int page, @Argument(name = "size") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userCRUD.getUsersList(pageable);
    }

    @MutationMapping
    public Message addUser(@Argument(name = "user") Users users) throws MessagingException, IOException {
        return userCRUD.addUser(users);
    }

    @MutationMapping
    public Message updateUser(@Argument(name = "user") Users users) {
        return userCRUD.updateUser(users);
    }

    @MutationMapping
    public Message deleteUser(@Argument(name = "email") String email) {
        return userCRUD.softDeleteUser(email);
    }

    @MutationMapping
    public Message restoreUser(@Argument String email) {
        return userCRUD.restoreDeletedUser(email);
    }

    @QueryMapping
    public Page<Users> filterUsers(
            @Argument String firstName,
            @Argument String lastName,
            @Argument String email,
            @Argument String role,
            @Argument String deletedBy,
            @Argument(name = "page") int page,
            @Argument(name = "size") int size)
    {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userCRUD.filterUsers(firstName, lastName, email, role, deletedBy, pageable);
    }
}
