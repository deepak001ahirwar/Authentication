package com.demo.Authentication.service;


import org.springframework.ldap.core.LdapTemplate;

import javax.naming.Name;
import java.util.List;

public interface LdapRepositoryService {
//    public boolean authenticate(String base, String userName, String password, LdapTemplate ldapTemplete);
//
////    public boolean createUser(InternalLdapUser internalLdapUser, LdapTemplate ldapTemplete);
//
////    public boolean createGroup(InternalLdapGroup internalLdapGroup, LdapTemplate ldapTemplate);
//
//    /*Method for getting all Groups and roles of User from internal LDAP
//     * Note: This method cannot be used for external LDAP*/
////    List<LdapGroupAndRoleDTO> getAllGroupsAndRolesOfUser(String base, String uid, LdapTemplate ldapTemplate);
//
////    List<LdapUserDTO> searchUserFromLdap(String uid, String firstName, String lastName, LdapTemplate ldapTemplate);
//
//    List<String> getMemberFromGroup(String groupName, String id, LdapTemplate ldapTemplate);
//
////    List<LdapUserDTO> getUserById(String uid, LdapTemplate ldapTemplate);
//
//    public Boolean addMemberToGroup(String uid, String roleName, String groupName, LdapTemplate ldapTempleteInternal);
//
//    public Boolean deleteMemberFromGroup(String uid, String roleName, String groupName, LdapTemplate ldapTempleteInternal);
//
//    public List<String> getRoleNamesFromLdap(String uid, String groupName, LdapTemplate ldapTemplete);
//
//    public List<String> getRoleNames(LdapTemplate ldapTempleteInternal);
////    boolean createUserGroup(LdapUserGroupOfNames internalLdapUserGroup, LdapTemplate ldapTemplate);
////
////    boolean createUserGroup(LdapUserGroupOfUniqueNames internalLdapUserGroup, LdapTemplate ldapTemplate);
//
//    public Boolean addMemberToGroup(Name groupMemberDN, Name roleGroupDN, LdapTemplate ldapTempleteInternal);
//
////    List<LdapGroupAndRoleDTO> getAllGroupsAndRolesOfUser(String base, Name userDN, LdapTemplate ldapTemplate);
//
//    public Boolean deleteUsersFromGroup(String ldapUserName);
//
//    public List<String> getMemberFromRole(String roleName, LdapTemplate ldapTemplate);





    public List<String> getRoleNamesFromLdap(String uid, String groupName, LdapTemplate ldapTemplete);

}
