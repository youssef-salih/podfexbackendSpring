package com.cosmomedia.location.dto;

import com.cosmomedia.location.enums.Roles;
import com.cosmomedia.location.enums.Status;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersDto {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Date createdAt;
    private Date updatedAt;
    private Status status;
    private Roles role;
    private Date modifiedAt;
    private Date deletedAt;
    private String deletedBy;

}
