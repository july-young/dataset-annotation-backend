package org.dubhe.biz.base.constant;

public final class Permissions {

    /**
     * 数据管理
     */
    public static final String DATA = "hasAuthority('ROLE_data')";
    public static final String DATA_DELETE = "hasAuthority('ROLE_data:delete')";

    /**
     * 控制台：用户组管理
     */
    public static final String USER_GROUP_CREATE = "hasAuthority('ROLE_system:userGroup:create')";
    public static final String USER_GROUP_EDIT = "hasAuthority('ROLE_system:userGroup:edit')";
    public static final String USER_GROUP_DELETE = "hasAuthority('ROLE_system:userGroup:delete')";
    public static final String USER_GROUP_EDIT_USER = "hasAuthority('ROLE_system:userGroup:editUser')";
    public static final String USER_GROUP_EDIT_USER_ROLE = "hasAuthority('ROLE_system:userGroup:editUserRole')";
    public static final String USER_GROUP_EDIT_USER_STATE = "hasAuthority('ROLE_system:userGroup:editUserState')";
    public static final String USER_GROUP_DELETE_USER = "hasAuthority('ROLE_system:userGroup:deleteUser')";

    /**
     * 控制台：用户管理
     */
    public static final String USER_CREATE = "hasAuthority('ROLE_system:user:create')";
    public static final String USER_EDIT = "hasAuthority('ROLE_system:user:edit')";
    public static final String USER_DELETE = "hasAuthority('ROLE_system:user:delete')";
    public static final String USER_DOWNLOAD = "hasAuthority('ROLE_system:user:download')";
    public static final String USER_CONFIG_EDIT = "hasAuthority('ROLE_system:user:configEdit')";
    public static final String USER_RESOURCE_INFO = "hasAuthority('ROLE_system:user:resourceInfo')";

    /**
     * 控制台：角色管理
     */
    public static final String ROLE = "hasAuthority('ROLE_system:role')";
    public static final String ROLE_CREATE = "hasAuthority('ROLE_system:role:create')";
    public static final String ROLE_EDIT = "hasAuthority('ROLE_system:role:edit')";
    public static final String ROLE_DELETE = "hasAuthority('ROLE_system:role:delete')";
    public static final String ROLE_DWONLOAD = "hasAuthority('ROLE_system:role:download')";
    public static final String ROLE_MENU = "hasAuthority('ROLE_system:role:menu')";
    public static final String ROLE_AUTH = "hasAuthority('ROLE_system:role:auth')";

    /**
     * 控制台：权限组管理
     */
    public static final String AUTH_CODE = "hasAuthority('ROLE_system:authCode')";
    public static final String AUTH_CODE_CREATE = "hasAuthority('ROLE_system:authCode:create')";
    public static final String AUTH_CODE_EDIT = "hasAuthority('ROLE_system:authCode:edit')";
    public static final String AUTH_CODE_DELETE = "hasAuthority('ROLE_system:authCode:delete')";

    /**
     * 控制台：权限管理
     */
    public static final String PERMISSION = "hasAuthority('ROLE_system:permission')";
    public static final String PERMISSION_CREATE = "hasAuthority('ROLE_system:permission:create')";
    public static final String PERMISSION_EDIT = "hasAuthority('ROLE_system:permission:edit')";
    public static final String PERMISSION_DELETE = "hasAuthority('ROLE_system:permission:delete')";

    /**
     * 控制台：菜单管理
     */
    public static final String MENU = "hasAuthority('ROLE_system:menu')";
    public static final String MENU_CREATE = "hasAuthority('ROLE_system:menu:create')";
    public static final String MENU_EDIT = "hasAuthority('ROLE_system:menu:edit')";
    public static final String MENU_DELETE = "hasAuthority('ROLE_system:menu:delete')";
    public static final String MENU_DOWNLOAD = "hasAuthority('ROLE_system:menu:download')";

    /**
     * 控制台：字典管理
     */
    public static final String DICT = "hasAuthority('ROLE_system:dict')";
    public static final String DICT_CREATE = "hasAuthority('ROLE_system:dict:create')";
    public static final String DICT_EDIT = "hasAuthority('ROLE_system:dict:edit')";
    public static final String DICT_DELETE = "hasAuthority('ROLE_system:dict:delete')";
    public static final String DICT_DOWNLOAD = "hasAuthority('ROLE_system:dict:download')";

    /**
     * 控制台：字典详情管理
     */
    public static final String DICT_DETAIL_CREATE = "hasAuthority('ROLE_system:dictDetail:create')";
    public static final String DICT_DETAIL_EDIT = "hasAuthority('ROLE_system:dictDetail:edit')";
    public static final String DICT_DETAIL_DELETE = "hasAuthority('ROLE_system:dictDetail:delete')";

    private Permissions() {
    }
}
