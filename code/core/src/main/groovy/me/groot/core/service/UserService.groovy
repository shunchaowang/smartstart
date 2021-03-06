package me.groot.core.service

import me.groot.core.domain.Permission
import me.groot.core.domain.Role
import me.groot.core.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification

/**
 * The user management service is responsible for all user related apis, its scope includes user, role and permission.
 * This is an encapsulation layer to operate user.
 */
interface UserService {

    // User apis
    User saveUser(User user)

    User updateUser(User user)

    void deleteUser(long id)

    User activateUser(long id)

    User deactivateUser(long id)

    Long countUser()

    User getUser(long id)

    User findUserByUsername(String username)

    Page<User> findUsers(Specification<User> specification, Pageable pageable)

    long countUser(Specification<User> specification)

    // Permission apis
    Permission savePermission(Permission permission)

    Permission getPermission(long id)

    Permission findPermissionByName(String name)

    Page<Permission> findPermissions(Pageable pageable)

    // Role apis
    Role saveRole(Role role)

    Role getRole(long id)

    Role findRoleByName(String name)

    Page<Role> findRoles(Pageable pageable)

    List<Role> getAllRoles()
}