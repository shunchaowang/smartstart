package me.groot.app

import me.groot.app.vo.Menu
import me.groot.app.vo.SubMenu
import me.groot.core.domain.MenuCategory
import me.groot.core.domain.MenuItem
import me.groot.core.domain.Permission
import me.groot.core.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * The SecurityUser implements spring security UserDetails to support authentication.
 */
class UserDetailsImpl implements UserDetails {

    private User user
    HashSet<String> permissions

    List<Menu> menus

    UserDetailsImpl(User user) {
        this.user = user
        permissions = new HashSet<>()

        Set<Permission> permissionCombined = new HashSet<>()

        permissionCombined.addAll(user.permissions)

        user.roles.each {
            permissionCombined.addAll(it.permissions)
        }

        // get all menu items and categories from the user and map them to Navigation menu, they have
        // to be sorted to make sure they stay the same order every time
        HashMap<String, Menu> menuMap = new HashMap<>()

        permissionCombined.each {

            // put permission name into permissions to support permission evaluator
            permissions.add(it.name)

            // get all menus and submenus, sort as well
            MenuItem menuItem = it.menuItem
            if (!menuItem) return
            MenuCategory menuCategory = menuItem.menuCategory
            if (!menuCategory) return

            if (!menuMap.containsKey(menuCategory.name)) {
                // add a new key entry
                menuMap.put(menuCategory.name, new Menu(menuCategory))
            }

            SubMenu subMenu = new SubMenu(menuItem)
            if (menuMap.get(menuCategory.name).subMenus.contains(subMenu)) {
                return
            }
            menuMap.get(menuCategory.name).subMenus.add(subMenu)
        }

        menus = new ArrayList<>(menuMap.values())
        Collections.sort(menus)
        menus.each {
            Collections.sort(it.subMenus)
        }
    }
/**
 * Returns the password used to authenticate the user.
 *
 * @return the password
 */
    @Override
    String getPassword() {
        return user.password
    }

    /**
     * Returns the username used to authenticate the user. Cannot return <code>null</code>
     * .
     *
     * @return the username (never <code>null</code>)
     */
    @Override
    String getUsername() {
        return user.username
    }
/**
 * Returns the authorities granted to the user. Cannot return <code>null</code>.
 *
 * @return the authorities, sorted by natural key (never <code>null</code>)
 */
    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        HashSet<GrantedAuthority> authorities = new HashSet<>()
        user.roles.each {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(it.name)
            authorities.add(authority)
        }
        return authorities
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    boolean isAccountNonExpired() {
        return true
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    boolean isAccountNonLocked() {
        return true
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    @Override
    boolean isEnabled() {
        return user.active
    }
}
