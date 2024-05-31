package com.cosmomedia.location.service.specifications;

import com.cosmomedia.location.entities.Users;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class UserSpecifications {

    /**
     * Generates a Specification to filter users by first name.
     *
     * @param firstName The first name to filter by.
     * @return A Specification object for filtering by first name.
     */
    public static Specification<Users> filterByFirstName(String firstName) {
        return (root, query, builder) ->
                firstName == null ? null : builder.like(builder.function("LOWER", String.class, root.get("firstName")), "%" + firstName.toLowerCase() + "%");
        // The lambda function checks if the firstName is null, and if not, creates a LIKE clause to match the first name in a case-insensitive manner.
    }

    /**
     * Generates a Specification to filter users by last name.
     *
     * @param lastName The last name to filter by.
     * @return A Specification object for filtering by last name.
     */
    public static Specification<Users> filterByLastName(String lastName) {
        return (root, query, builder) ->
                lastName == null ? null : builder.like(builder.function("LOWER", String.class, root.get("lastName")), "%" + lastName.toLowerCase() + "%");
        // The lambda function checks if the lastName is null, and if not, creates a LIKE clause to match the last name in a case-insensitive manner.
    }


    /**
     * Generates a Specification to filter users by email.
     *
     * @param email The email to filter by.
     * @return A Specification object for filtering by email.
     */
    public static Specification<Users> filterByEmail(String email) {
        return (root, query, builder) ->
                email == null ? null : builder.like(builder.function("LOWER", String.class, root.get("email")), "%" + email.toLowerCase() + "%");
        // The lambda function checks if the email is null, and if not, creates a LIKE clause to match the email in a case-insensitive manner.
    }

    /**
     * Generates a Specification to filter users by role.
     *
     * @param role The role to filter by.
     * @return A Specification object for filtering by role.
     */
    public static Specification<Users> filterByRole(String role) {
        return (root, query, builder) ->
                role == null ? null : builder.equal(builder.function("LOWER", String.class, root.get("role")), role.toLowerCase());
        // The lambda function checks if the role is null, and if not, creates an EQUALS clause to match the role in a case-insensitive manner.
    }

    /**
     * Generates a Specification to filter users by the user who deleted them.
     *
     * @param deletedBy The user who deleted the users to filter by.
     * @return A Specification object for filtering by the user who deleted them.
     */
    public static Specification<Users> filterByDeletedBy(String deletedBy) {
        return (root, query, builder) ->
                deletedBy == null ? null : builder.equal(builder.function("LOWER", String.class, root.get("deletedBy")), deletedBy.toLowerCase());
        // The lambda function checks if the deletedBy value is null, and if not, creates an EQUALS clause to match the deletedBy user in a case-insensitive manner.
    }
}
