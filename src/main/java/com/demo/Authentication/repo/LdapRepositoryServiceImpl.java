package com.demo.Authentication.repo;

import com.demo.Authentication.config.LdapConfig;
import com.demo.Authentication.service.LdapRepositoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@SuppressWarnings("deprecation")
@Component
public class LdapRepositoryServiceImpl implements LdapRepositoryService {
////    private static final Logger LOG = LoggerFactory.getLogger(LdapRepositoryServiceImpl.class);
////    @Autowired
////    private LdapConfig ldapConstant;
////
////    @Override
////    public boolean authenticate(String base, String userName, String password, LdapTemplate ldapTemplete) {
////        LOG.info("executing {authenticate}");
////        if (ldapConstant.directorySyncSource.equalsIgnoreCase("active_directory")) {
////            Filter filter = new EqualsFilter(ldapConstant.userNameAttribute, userName);
////            return ldapTemplete.authenticate(LdapConfig.NLPGROUP + "=" + base, filter.encode(), password);
////        } else {
////            return ldapTemplete.authenticate(LdapConfig.NLPGROUP + "=" + base, "(" + ldapConstant.userNameAttribute + "=" + userName + ")", password);
////        }
////    }
////
////    @Override
//////    public boolean createUser(InternalLdapUser user, LdapTemplate ldapTemplete) {
//////        if (LOG.isDebugEnabled())
//////            LOG.debug("Sync External Directory User With Internal Directory " + user.getFullName());
//////        Attribute objectClass = new BasicAttribute("objectClass");
//////        if (LdapConfig.personObjects != null && LdapConfig.personObjects.length > 0) {
//////            for (int i = 0; i < LdapConfig.personObjects.length; i++) {
//////                objectClass.add(LdapConfig.personObjects[i]);
//////            }
//////        } else {
//////            if (LOG.isDebugEnabled()) {
//////                LOG.debug("personObjects not defined in LDAPConfig. Please contact vendor.");
//////            } else {
//////                LOG.info("System error. Please contact vendor");
//////            }
//////            LOG.error("personObjects not defined in LDAPConfig.");
//////            return false;
//////        }
//////        try {
//////            Attributes userAttributes = new BasicAttributes();
//////            userAttributes.put(objectClass);
//////            userAttributes.put(ldapConstant.groupNameAttribute, user.getFullName());
//////            userAttributes.put(LdapConfig.NLPGROUP, user.getCompany());
//////            userAttributes.put(LdapConfig.USERATTRIBUTESN, user.getLastName());
//////            userAttributes.put(ldapConstant.userNameAttribute, user.getUid());
//////            ////////////////////////
//////            if (user.getEmployeeNumber() > 0) userAttributes.put("employeeNumber", user.getEmployeeNumber() + "");
//////            if (user.getFirstName() != null && !user.getFirstName().trim().isEmpty())
//////                userAttributes.put("givenName", user.getFirstName());
//////            if (user.getTitle() != null && !user.getTitle().trim().isEmpty())
//////                userAttributes.put("title", user.getTitle());
//////            if (user.getEmail() != null && !user.getEmail().trim().isEmpty())
//////                userAttributes.put("mail", user.getEmail());
//////            if (user.getPhone() != null && !user.getPhone().trim().isEmpty())
//////                userAttributes.put("telephoneNumber", user.getPhone());
//////            if (user.getDescription() != null && !user.getDescription().trim().isEmpty())
//////                userAttributes.put("description", user.getDescription());
//////            if (user.getCountry() != null && !user.getCountry().trim().isEmpty())
//////                userAttributes.put("c", user.getCountry());
//////            if (user.getHomeDirectory() != null && !user.getHomeDirectory().trim().isEmpty())
//////                userAttributes.put("homeDirectory", user.getHomeDirectory());
//////            if (LOG.isDebugEnabled()) LOG.debug("Syncing user {}", user.getUid());
//////            if (user.getId().toString().startsWith("cn=") || user.getId().toString().startsWith("CN=")) {
//////                ldapTemplete.rebind(bindCN(user), null, userAttributes);
//////            } else {
//////                ldapTemplete.rebind(bindDN(user), null, userAttributes);
//////            }
//////        } catch (Exception e) {
//////            LOG.error(e.getMessage());
//////            return false;
//////        }
//////        return true;
//////    }
////
////    public static Name bindDN(InternalLdapUser internalLdapUser) {
////        Name name = new DistinguishedName(internalLdapUser.getId());
////        return name;
////    }
////
////    public static Name bindCN(InternalLdapUser internalLdapUser) {
////        Name name = new DistinguishedName(internalLdapUser.getId());
////        return name;
////    }
////
////    @Override
//////    public boolean createGroup(InternalLdapGroup internalLdapGroup, LdapTemplate ldapTemplate) {
//////        LOG.info("Sync External Directory OU with Internal Directory: " + internalLdapGroup.getName());
//////        Attribute objectClass = new BasicAttribute("objectClass");
//////        objectClass.add(LdapConfig.OBJECTCLASSFORUSERTOP);
//////        objectClass.add(LdapConfig.OBJECTCLASSFORGROUPORGANUNIT);
//////        try {
//////            Attributes userAttributes = new BasicAttributes();
//////            userAttributes.put(objectClass);
//////            userAttributes.put(LdapConfig.NLPGROUP, internalLdapGroup.getName());
//////            ldapTemplate.rebind(bindDNForGroup(internalLdapGroup), null, userAttributes);
//////        } catch (Exception e) {
//////            if (LOG.isDebugEnabled()) LOG.debug("Already Exist  :" + internalLdapGroup.getName());
//////            LOG.error("Error while syncing OUs with internal LDAP: {}", e.getMessage());
//////            return false;
//////        }
//////        return true;
//////    }
////
////    public static Name bindDNForGroup(InternalLdapGroup internalLdapGroup) {
////        Name name = new DistinguishedName(internalLdapGroup.getId());
////        return name;
////    }
////
////    @Override
////    public List<LdapGroupAndRoleDTO> getAllGroupsAndRolesOfUser(String base, String uid, LdapTemplate ldapTemplate) {
////        String objectClass = ldapConstant.userGroupObjectClass;
////        if (base == null || base.isEmpty()) {
////            List<LdapGroupAndRoleDTO> userGroups = new LinkedList<LdapGroupAndRoleDTO>();
////            List<LdapGroupAndRoleDTO> userRolesInLdap = ldapTemplate.search(
////                    query().where("objectclass").is(ldapConstant.userGroupObjectClass),
////                    (AttributesMapper<LdapGroupAndRoleDTO>) attrs -> {
////                        LdapGroupAndRoleDTO groupDTO = new LdapGroupAndRoleDTO();
////                        ////////////////////////////////////////////////////////
////                        for (int i = 0; i < attrs.get(ldapConstant.groupMemberAttribute).size(); i++) {
////                            if (attrs.get(ldapConstant.groupMemberAttribute).get(i) != null && !attrs.get(ldapConstant.groupMemberAttribute).get(i).toString().trim().isEmpty()) {
////                                //if(attrs.get(ldapConstant.groupMemberAttribute).get(i).toString().toLowerCase().indexOf(ldapConstant.userNameAttribute.toLowerCase()+"="+uid) >=0) {
////                                if (attrs.get(ldapConstant.groupMemberAttribute).get(i).toString().toLowerCase().split(",")[0].indexOf(ldapConstant.userNameAttribute.toLowerCase() + "=" + uid.toLowerCase()) >= 0) {
////                                    groupDTO.setName((String) attrs.get(ldapConstant.groupNameAttribute).get());
////                                    groupDTO.setDescription(attrs.get("description") == null ? "" : attrs.get("description").get().toString());
////                                }
////                            }
////                        }
////                        if (groupDTO.getName() == null || groupDTO.getName().trim().isEmpty()) return null;
////                        return groupDTO;
////                    });
////            userRolesInLdap.forEach(userGroup -> {
////                if (userGroup != null) userGroups.add(userGroup);
////            });
////            return userGroups;
////        } else {
////            String searchFilter = "(&(objectclass=" + objectClass + "))";
////            List<LdapGroupAndRoleDTO> userGroups = new LinkedList<LdapGroupAndRoleDTO>();
////            List<LdapGroupAndRoleDTO> userRolesInLdap =
////                    ldapTemplate.search(base, searchFilter, (AttributesMapper<LdapGroupAndRoleDTO>) attrs -> {
////                        LdapGroupAndRoleDTO groupDTO = new LdapGroupAndRoleDTO();
////                        for (int i = 0; i < attrs.get(ldapConstant.groupMemberAttribute).size(); i++) {
////                            if (attrs.get(ldapConstant.groupMemberAttribute).get(i) != null && !attrs.get(ldapConstant.groupMemberAttribute).get(i).toString().trim().isEmpty()) {
////                                //if(attrs.get(ldapConstant.groupMemberAttribute).get(i).toString().toLowerCase().indexOf(ldapConstant.userNameAttribute.toLowerCase()+"="+uid) >=0) {
////                                if (attrs.get(ldapConstant.groupMemberAttribute).get(i).toString().toLowerCase().split(",")[0].indexOf(ldapConstant.userNameAttribute.toLowerCase() + "=" + uid.toLowerCase()) >= 0) {
////                                    groupDTO.setName((String) attrs.get(ldapConstant.groupNameAttribute).get());
////                                    groupDTO.setDescription(attrs.get("description") == null ? "" : attrs.get("description").get().toString());
////                                }
////                            }
////                        }
////                        return groupDTO;
////                    });
////            userRolesInLdap.forEach(userGroup -> {
////                if (userGroup != null && userGroup.getName() != null && !userGroup.getName().trim().isEmpty())
////                    userGroups.add(userGroup);
////            });
////            return userGroups;
////        }
////    }
////
//////    @Override
//////    public List<LdapGroupAndRoleDTO> getAllGroupsAndRolesOfUser(String base, Name userDN, LdapTemplate ldapTemplate) {
//////        String objectClass = ldapConstant.userGroupObjectClass;
//////        String searchFilter = "(&(objectclass=" + objectClass + ")(" + ldapConstant.groupMemberAttribute + "=" + userDN.toString() + "," +
//////                ldapConstant.internalLdapBase + "))";
//////        return ldapTemplate.search(base, searchFilter, (AttributesMapper<LdapGroupAndRoleDTO>) attrs -> {
//////            LdapGroupAndRoleDTO groupDTO = new LdapGroupAndRoleDTO();
//////            groupDTO.setName((String) attrs.get(ldapConstant.groupNameAttribute).get());
//////            groupDTO.setDescription(attrs.get("description") == null ? "" : attrs.get("description").get().toString());
//////            return groupDTO;
//////        });
//////    }
////
//////    @Override
//////    public List<LdapUserDTO> searchUserFromLdap(String uid, String firstName, String lastName,
//////                                                LdapTemplate ldapTemplate) {
//////        return ldapTemplate.search(
//////                query().where("objectclass").is(LdapConfig.OBJECTCLASSFORUSERINETORG).and((query().where(ldapConstant.userNameAttribute).is(uid)).or(query().where("givenName").is(firstName))
//////                        .or(query().where("sn").is(lastName))),
//////                (AttributesMapper<LdapUserDTO>) attributes -> convertAttributeToLdapUserDTO.apply(attributes)
//////        );
//////    }
////
//////    Function<Attributes, LdapUserDTO> convertAttributeToLdapUserDTO = (final Attributes attributes) -> {
//////        LdapUserDTO userDTO = new LdapUserDTO();
//////        try {
//////            userDTO.setFullName(attributes.get(ldapConstant.groupNameAttribute).get().toString());
//////            if (attributes.get("userPassword") != null)
//////                userDTO.setUserPassword(attributes.get("userPassword").get().toString());
//////            userDTO.setUid(attributes.get(ldapConstant.userNameAttribute).get().toString());
//////            userDTO.setLastName(attributes.get("sn") == null ? "" : attributes.get("sn").get().toString());
//////            userDTO.setCompany(attributes.get(LdapConfig.NLPGROUP) == null ? "" : attributes.get(LdapConfig.NLPGROUP).get().toString());
//////            userDTO.setPhone(attributes.get("telephoneNumber") == null ? ""
//////                    : attributes.get("telephoneNumber").get().toString());
//////            userDTO.setFirstName(
//////                    attributes.get("givenName") == null ? "" : attributes.get("givenName").get().toString());
//////            userDTO.setEmail(attributes.get("mail") == null ? "" : attributes.get("mail").get().toString());
//////            userDTO.setDescription(
//////                    attributes.get("description") == null ? "" : attributes.get("description").get().toString());
//////            userDTO.setCountry(attributes.get("c") == null ? "" : attributes.get("c").get().toString());
//////            userDTO.setEmployeeNumber(attributes.get("employeeNumber") == null ? 0
//////                    : Integer.valueOf(attributes.get("employeeNumber").get().toString()));
//////            userDTO.setTitle(attributes.get("title") == null ? "" : attributes.get("title").get().toString());
//////        } catch (NamingException e) {
//////            LOG.error("Error: {}", e);
//////        }
//////        return userDTO;
//////    };
////
//////    @Override
//////    public List<String> getMemberFromGroup(String groupName, String id, LdapTemplate ldapTemplate) {
//////        if (id == null | id.isEmpty()) {
//////            List<String> groupMembers = new LinkedList<String>();
//////            List<Attribute> groupMembersAttrs = ldapTemplate.search(
//////                    query().where("objectclass").is(ldapConstant.userGroupObjectClass).and(query().where(ldapConstant.groupNameAttribute).is(groupName))/*
//////							.and(query().where("id").is(id))*/,
//////                    (AttributesMapper<Attribute>) attributes -> attributes.get(ldapConstant.groupMemberAttribute)
//////            );
//////            if (groupMembersAttrs.size() > 0) {
//////                Attribute attribute = groupMembersAttrs.get(0);
//////                try {
//////                    if (attribute != null && attribute.size() > 0) {
//////                        for (int i = 0; i < attribute.size(); i++) {
//////                            String theUniqueMember = attribute.get(i).toString();
//////                            if (theUniqueMember != null && !theUniqueMember.isEmpty() && !theUniqueMember.trim().isEmpty()) {
//////                                groupMembers.add(theUniqueMember);
//////                            }
//////                        }
//////                    }
//////                } catch (NamingException e) {
//////                    LOG.error("Error occured while fetching members from group {}. error {}", e.getMessage());
//////                    if (LOG.isDebugEnabled())
//////                        LOG.debug("Error while fetching members {}", e.getStackTrace().toString());
//////                    e.printStackTrace();
//////                }
//////                return groupMembers;
//////            } else {
//////                return null;
//////            }
//////        } else {
//////            return ldapTemplate.search(
//////                    query().where("objectclass").is(ldapConstant.userGroupObjectClass).and(query().where(ldapConstant.groupNameAttribute).is(groupName))
//////                            .and(query().where("id").is(id)),
//////                    (AttributesMapper<String>) attributes -> attributes.get(ldapConstant.groupMemberAttribute).get(0).toString()
//////            );
//////        }
//////    }
////
//////    @Override
//////    public List<LdapUserDTO> getUserById(String uid, LdapTemplate ldapTemplate) {
//////        return ldapTemplate.search(
//////                query().where("objectclass")
//////                                .is(ldapConstant.userObjectClass)
//////                        .and(query().where(ldapConstant.userNameAttribute).is(uid)),
//////                (AttributesMapper<LdapUserDTO>) attributes -> convertAttributeToLdapUserDTO.apply(attributes)
//////        );
//////    }
////
//////    @Override
//////    public Boolean addMemberToGroup(String uid, String roleName, String groupName, LdapTemplate ldapTempleteInternal) {
//////        try {
//////            //Find the User so as to derive it's OU. Remember, this method will have limitation that 2 users with same UID should not be there in directory.
//////            List<LdapUserDTO> userDTOList = getUserById(uid, ldapTempleteInternal);
//////            //String userOu = groupName;
//////            if (userDTOList.size() > 1) {
//////                LOG.error("found multiple users with same UID {}", uid,
//////                        " . Assigning role {} to user {}. Please contact administrator to correct LDAP data or"
//////                                + " contact product support team", roleName, userDTOList.get(0).getId());
//////            }
//////            if ((userDTOList.size() >= 1) && !groupName.equalsIgnoreCase(userDTOList.get(0).getCompany())) {
//////                String userOuName = userDTOList.get(0).getCompany();
//////                DirContextOperations ctx = ldapTempleteInternal
//////                        .lookupContext(ldapConstant.groupNameAttribute + "=" + roleName + "," + LdapConfig.NLPGROUP + "=" + groupName + "");
//////                ctx.addAttributeValue(ldapConstant.groupMemberAttribute,
//////                        ldapConstant.userNameAttribute + "=" + uid + "," + LdapConfig.NLPGROUP + "=" + userOuName + "," + ldapConstant.internalLdapBase + "");
//////                ldapTempleteInternal.modifyAttributes(ctx);
//////            } else {
//////                DirContextOperations ctx = ldapTempleteInternal
//////                        .lookupContext(ldapConstant.groupNameAttribute + "=" + roleName + "," + LdapConfig.NLPGROUP + "=" + groupName + "");
//////                ctx.addAttributeValue(ldapConstant.groupMemberAttribute,
//////                        ldapConstant.userNameAttribute + "=" + uid + "," + ldapConstant.ldapOU + "," + ldapConstant.internalLdapBase + "");
//////                ldapTempleteInternal.modifyAttributes(ctx);
//////            }
//////            if (LOG.isDebugEnabled())
//////                LOG.debug("Member " + uid + "Added to role " + roleName + " Successfully.");
//////        } catch (Exception e) {
//////            LOG.error("error while adding role {} of member {} : {}", roleName, uid, e.getMessage());
//////            if (LOG.isDebugEnabled())
//////                LOG.debug("error while adding role {} of member {} : {}", roleName, uid, e.getStackTrace().toString());
//////            return Boolean.FALSE;
//////        }
//////        return Boolean.TRUE;
//////    }
////
////
//////    @Override
//////    public Boolean deleteMemberFromGroup(String uid, String roleName, String groupName,
//////                                         LdapTemplate ldapTempleteInternal) {
//////        try {
//////            List<String> roleAssignees = getMemberFromGroup(roleName, "", ldapTempleteInternal);
//////            roleAssignees.forEach(roleAssignee -> {
//////                if (roleAssignee.indexOf(uid) >= 0) {
//////                    DirContextOperations ctx = ldapTempleteInternal
//////                            .lookupContext(ldapConstant.groupNameAttribute + "=" + roleName + "," + LdapConfig.NLPGROUP + "=" + groupName + "");
//////                    ctx.removeAttributeValue(ldapConstant.groupMemberAttribute, roleAssignee);
//////                    ldapTempleteInternal.modifyAttributes(ctx);
//////                    if (LOG.isDebugEnabled())
//////                        LOG.debug("Member " + uid + "Removed from role " + roleName + " Successfully.");
//////                }
//////            });
//////        } catch (Exception e) {
//////            LOG.error("error while removing role {} of member {} : {}", roleName, uid, e.getMessage());
//////            if (LOG.isDebugEnabled())
//////                LOG.debug("error while removing role {} of member {} : {}", roleName, uid, e.getStackTrace().toString());
//////            return Boolean.FALSE;
//////        }
//////        return Boolean.TRUE;
//////    }
////
////                                         @Override
////                                         public List<String> getRoleNamesFromLdap(String uid, String groupName, LdapTemplate ldapTempleteInternal) {
////        if (groupName != null && !groupName.isEmpty()) {
////            String searchFilter = "(&(objectclass=" + ldapConstant.userGroupObjectClass + ")("
////                    + ldapConstant.groupMemberAttribute + "=" + ldapConstant.userNameAttribute + "=" + uid + "," + LdapConfig.NLPGROUP + "="
////                    + groupName + "," + ldapConstant.internalLdapBase + "))";
////            return ldapTempleteInternal.search(LdapConfig.NLPGROUP + "=" + LdapConfig.NLPROLEBASEOU, searchFilter,
////                    (AttributesMapper<String>) attrs -> (String) attrs.get(ldapConstant.groupNameAttribute)
////                            .get());
////        } else {
////            List<String> userRoles = new LinkedList<String>();
////            List<String> userRolesInLdap = ldapTempleteInternal.search(
////                    query().where("objectclass").is(ldapConstant.userGroupObjectClass)
////                            .and(query().where(LdapConfig.NLPGROUP).is(LdapConfig.NLPROLEBASEOU)),
////                    (AttributesMapper<String>) attrs -> {
////                        String userRole = "";
////                        for (int i = 0; i < attrs.get(ldapConstant.groupMemberAttribute).size(); i++) {
////                            if (attrs.get(ldapConstant.groupMemberAttribute).get(i) != null && !attrs.get(ldapConstant.groupMemberAttribute).get(i).toString().trim().isEmpty()) {
////                                if (attrs.get(ldapConstant.groupMemberAttribute).get(i).toString().indexOf(ldapConstant.userNameAttribute + "=" + uid) >= 0) {
////                                    userRole = attrs.get(ldapConstant.groupNameAttribute).toString();
////                                    if (userRole.toLowerCase().startsWith("cn:") || userRole.toLowerCase().startsWith("uid:")) {
////                                        userRole = userRole.split(":")[1].trim();
////                                    }
////                                }
////                            }
////                        }
////                        return userRole;
////                    });
////            userRolesInLdap.forEach(role -> {
////                if (role != null && !role.trim().isEmpty()) userRoles.add(role);
////            });
////            return userRoles;
////        }
////    }
//////    @Override
//////    public List<String> getMemberFromRole(String roleName, LdapTemplate ldapTemplate) {
//////        List<String> roleMembers = new LinkedList<String>();
//////        List<Attribute> roleMembersAttrs = ldapTemplate.search(
//////                query().where("objectclass").is(ldapConstant.userGroupObjectClass).and(query().where(ldapConstant.groupNameAttribute).is(roleName))/*
//////							.and(query().where("id").is(id))*/,
//////                (AttributesMapper<Attribute>) attributes -> attributes.get(ldapConstant.groupMemberAttribute)
//////        );
//////        if (roleMembersAttrs.size() > 0) {
//////            Attribute attribute = roleMembersAttrs.get(0);
//////            try {
//////                if (attribute != null && attribute.size() > 0) {
//////                    for (int i = 0; i < attribute.size(); i++) {
//////                        String theUniqueMember = attribute.get(i).toString();
//////                        if (theUniqueMember != null && !theUniqueMember.isEmpty() && !theUniqueMember.trim().isEmpty()) {
//////                            roleMembers.add(theUniqueMember.split(",")[0].split("=")[1]); //only user id
//////                        }
//////                    }
//////                }
//////            } catch (NamingException e) {
//////                LOG.error("Error occured while fetching members from role {}. error {}", e.getMessage());
//////                if (LOG.isDebugEnabled()) LOG.debug("Error while fetching members {}", e.getStackTrace().toString());
//////                e.printStackTrace();
//////            }
//////            return roleMembers;
//////        } else {
//////            return null;
//////        }
//////    }
////
//////    @Override
//////    public List<String> getRoleNames(LdapTemplate ldapTempleteInternal) {
//////        return ldapTempleteInternal.search(
//////                query().where("objectclass").is(ldapConstant.userGroupObjectClass)
//////                        .and(query().where(LdapConfig.NLPGROUP).is(LdapConfig.NLPROLEBASEOU)),
//////                (AttributesMapper<String>) attrs -> (String) attrs.get(ldapConstant.groupNameAttribute)
//////                        .get()
//////        );
//////    }
////
//////    @Override
//////    public boolean createUserGroup(LdapUserGroupOfNames internalLdapUserGroup, LdapTemplate ldapTemplate) {
//////        LdapUserGroupOfUniqueNames groupOfUniqueNames = new LdapUserGroupOfUniqueNames();
//////        groupOfUniqueNames.setId(internalLdapUserGroup.getId());
//////        groupOfUniqueNames.setCnName(internalLdapUserGroup.getCnName());
//////        groupOfUniqueNames.setDescription(internalLdapUserGroup.getDescription());
//////        groupOfUniqueNames.setOu(internalLdapUserGroup.getOu());
//////        groupOfUniqueNames.setUniqueMember(internalLdapUserGroup.getUniqueMember());
//////        groupOfUniqueNames.setMembers(internalLdapUserGroup.getMembers());
//////        return createUserGroup(groupOfUniqueNames, ldapTemplate);
//////    }
////
//////    private Name bindDNForUserGroup(LdapUserGroupOfUniqueNames internalLdapUserGroup) {
//////        Name name = null;
//////        if (!(internalLdapUserGroup.getId().toString().indexOf("dc=") > 0)) {
//////            name = new DistinguishedName(internalLdapUserGroup.getId() /*+ "," + ldapConstant.internalLdapBase*/);
//////        } else {
//////            name = new DistinguishedName(internalLdapUserGroup.getId());
//////        }
//////        return name;
//////    }
////
//////    @Override
//////    public boolean createUserGroup(LdapUserGroupOfUniqueNames internalLdapUserGroup, LdapTemplate ldapTemplate) {
//////        LOG.info("Syncing External User Group {} with Internal Directory: ", internalLdapUserGroup.getCnName());
//////        Attribute objectClass = new BasicAttribute("objectClass");
//////        objectClass.add("top");
//////        objectClass.add(LdapConfig.USER_GROUP_OBJECT_CLASS_UNIQUENAMES);
//////        try {
//////            Attributes userAttributes = new BasicAttributes();
//////            userAttributes.put(objectClass);
//////            if (internalLdapUserGroup.getOu() == null || internalLdapUserGroup.getOu().isEmpty()) {
//////                if (internalLdapUserGroup.getId() != null && (internalLdapUserGroup.getId().toString().indexOf(",") > 0)) {
//////                    String ouStr = getOuFromDN(internalLdapUserGroup.getId());
//////                    userAttributes.put(LdapConfig.NLPGROUP, ouStr);
//////                }
//////            } else {
//////                userAttributes.put(LdapConfig.NLPGROUP, internalLdapUserGroup.getOu());
//////            }
//////            userAttributes.put(ldapConstant.groupNameAttribute, internalLdapUserGroup.getCnName());
//////            if (internalLdapUserGroup.getDescription() == null) {
//////                userAttributes.put("description", internalLdapUserGroup.getCnName());
//////            } else {
//////                userAttributes.put("description", internalLdapUserGroup.getDescription());
//////            }
//////            Set<Name> uniqueMemberDNs = internalLdapUserGroup.getUniqueMember();
//////            Set<Name> memberDNs = internalLdapUserGroup.getMembers();
//////            if (uniqueMemberDNs != null && uniqueMemberDNs.size() >= 1) {
//////                userAttributes.put("uniqueMember",
//////                        (uniqueMemberDNs.toArray()[0] == null || ((Name) uniqueMemberDNs.toArray()[0]).toString().isEmpty()) ?
//////                                ((uniqueMemberDNs.size() > 1 && !((Name) uniqueMemberDNs.toArray()[1]).toString().isEmpty()) ?
//////                                        (((Name) uniqueMemberDNs.toArray()[1]).toString()) :
//////                                        (ldapConstant.userNameAttribute + "=dummy,OU=dummy")) :
//////                                (((Name) uniqueMemberDNs.toArray()[0]).toString()));
//////            } else if (memberDNs != null && memberDNs.size() >= 1) {
//////                userAttributes.put("uniqueMember",
//////                        (memberDNs.toArray()[0] == null || ((Name) memberDNs.toArray()[0]).toString().isEmpty()) ?
//////                                ((memberDNs.size() > 1 && !((Name) memberDNs.toArray()[1]).toString().isEmpty()) ?
//////                                        (((Name) memberDNs.toArray()[1]).toString()) :
//////                                        (ldapConstant.userNameAttribute + "=dummy,OU=dummy")) :
//////                                (((Name) memberDNs.toArray()[0]).toString()));
//////            }
//////            //userAttributes.put("member", " ");
//////            LOG.info("Inserting group {} into internal LDAP", internalLdapUserGroup.getCnName());
//////            ldapTemplate.rebind(bindDNForUserGroup(internalLdapUserGroup), null, userAttributes);
//////            //inserting unique members for the group
//////            if (uniqueMemberDNs != null && uniqueMemberDNs.size() > 0) {
//////                LOG.info("Now Adding {} members to the group {}", uniqueMemberDNs.size(), internalLdapUserGroup.getId());
//////                uniqueMemberDNs.forEach(uniqueMemberDN -> {
//////                    Boolean memberAddedToGroup = addMemberToGroup(uniqueMemberDN, internalLdapUserGroup.getId(), ldapTemplate);
//////                    if (LOG.isDebugEnabled())
//////                        LOG.debug("member {} added to group? - ()", uniqueMemberDN, memberAddedToGroup);
//////                });
//////            }
//////
//////            /*Convert all members to unique members. No point in keeping two types in internal LDAP*/
//////            if (memberDNs != null && memberDNs.size() > 0) {
//////                memberDNs.forEach(memberDN -> {
//////                    Boolean memberAddedToGroup = addMemberToGroup(memberDN, internalLdapUserGroup.getId(), ldapTemplate);
//////                    if (LOG.isDebugEnabled() && memberAddedToGroup)
//////                        LOG.debug("member {} added to group? - ()", memberDN, memberAddedToGroup);
//////                });
//////            }
//////        } catch (Exception e) {
//////            LOG.error(e.getMessage());
//////            LOG.debug("Already Exist  :" + internalLdapUserGroup.getCnName());
//////            return false;
//////        }
//////        return true;
//////    }
////
//////    @Override
//////    public Boolean addMemberToGroup(Name groupMemberDN, Name roleGroupDN, LdapTemplate ldapTempleteInternal) {
//////        if (!checkUserDNFormat(groupMemberDN) || !checkUsergroupDNFormat(roleGroupDN)) return Boolean.FALSE;
//////        DirContextOperations ctx = ldapTempleteInternal.lookupContext(roleGroupDN);
//////        ctx.addAttributeValue(ldapConstant.groupMemberAttribute, groupMemberDN.toString());
//////        ldapTempleteInternal.modifyAttributes(ctx);
//////        if (LOG.isDebugEnabled())
//////            LOG.debug("Member " + groupMemberDN.toString() + " Added to role " + roleGroupDN.toString() + " Successfully.");
//////        return Boolean.TRUE;
//////    }
////
//////    private boolean checkUsergroupDNFormat(Name roleGroupDN) {
//////        boolean isFormatCorrect = false;
//////        if (roleGroupDN != null) {
//////            String[] dnSplit = roleGroupDN.toString().split(",");
//////            int correctParamsCount = 0;
//////            for (int i = 0; i < dnSplit.length; i++) {
//////                if (i == 0 && dnSplit[i] != null && (dnSplit[i].startsWith("cn=") || dnSplit[i].startsWith("CN=")))
//////                    correctParamsCount++;
//////                if ((dnSplit[i].startsWith("ou=") || dnSplit[i].startsWith("OU=")) && correctParamsCount < 2)
//////                    correctParamsCount++;
//////                if (dnSplit[i].startsWith("dc=") || dnSplit[i].startsWith("DC=")) correctParamsCount++;
//////            }
//////            if (correctParamsCount >= 2) isFormatCorrect = true;
//////        }
//////        return isFormatCorrect;
//////    }
////
////    private boolean checkUserDNFormat(Name groupMemberDN) {
////        boolean isFormatCorrect = false;
////        if (groupMemberDN != null) {
////            String[] dnSplit = groupMemberDN.toString().split(",");
////            int correctParamsCount = 0;
////            for (int i = 0; i < dnSplit.length; i++) {
////                if (i == 0 && dnSplit[i] != null && (((dnSplit[i].startsWith("uid=") || dnSplit[i].startsWith("UID=") ||
////                        dnSplit[i].startsWith("Uid=") || dnSplit[i].startsWith("uID="))) || (dnSplit[i].startsWith("cn=") ||
////                        dnSplit[i].startsWith("CN=")))) correctParamsCount++;
////                if (dnSplit[i].startsWith("ou=") || dnSplit[i].startsWith("OU=") && correctParamsCount < 2)
////                    correctParamsCount++;
////                if (dnSplit[i].startsWith("dc=") || dnSplit[i].startsWith("DC=")) correctParamsCount++;
////            }
////            if (correctParamsCount >= 3) isFormatCorrect = true;
////        }
////        return isFormatCorrect;
////    }
////
////    private String getOuFromDN(Name dn) {
////        String ou = null;
////        if (checkUsergroupDNFormat(dn)) {
////            String idTempSplit[] = dn.toString().split(",");
////            StringBuffer ouStr = new StringBuffer();
////            int ouCounter = 0;
////            for (int i = 0; i < idTempSplit.length; i++) {
////                if (idTempSplit[i].startsWith("ou=") || idTempSplit[i].startsWith("OU=")) {
////                    if (ouCounter == 0) {
////                        ouStr.append(idTempSplit[i]);
////                    } else {
////                        ouStr.append(",");
////                        ouStr.append(idTempSplit[i]);
////                    }
////                    ouCounter++;
////                }
////            }
////            ou = ouStr.toString();
////        }
////        return ou;
////    }
////
////    @SuppressWarnings("static-access")
////    @Override
////    public Boolean deleteUsersFromGroup(String ldapUserName) {
////        DirContext dirContext = null;
////        try {
////            String decryptLdapPassword = SecurityUtils.aesDecrypt(ldapConstant.internalPassword);
////            Hashtable<String, String> environmentVar = new Hashtable<>();
////            environmentVar.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
////            environmentVar.put(Context.PROVIDER_URL, ldapConstant.internalLdapUrl);
////            environmentVar.put(Context.SECURITY_AUTHENTICATION, ldapConstant.LDAPAUTHENTICATION);
////            environmentVar.put(Context.SECURITY_PRINCIPAL, ldapConstant.internalDirBindDn);
////            environmentVar.put(Context.SECURITY_CREDENTIALS, decryptLdapPassword);
////            dirContext = new InitialDirContext(environmentVar);
////            dirContext.destroySubcontext("uid=" + ldapUserName + "," + ldapConstant.ldapOU + "," + ldapConstant.internalLdapBase);
////        } catch (Exception e) {
////            LOG.error("error while removing User {} From ldap", ldapUserName, e);
////            return Boolean.FALSE;
////        }
////        return Boolean.TRUE;
////    }
////
//}


    @Autowired
    private LdapConfig ldapConstant;
    @Override
    public List<String> getRoleNamesFromLdap(String uid, String groupName, LdapTemplate ldapTempleteInternal) {
    if (groupName != null && !groupName.isEmpty()) {
        String searchFilter = "(&(objectclass=" + ldapConstant.userGroupObjectClass + ")("
                + ldapConstant.groupMemberAttribute + "=" + ldapConstant.userNameAttribute + "=" + uid + "," + LdapConfig.NLPGROUP + "="
                + groupName + "," + ldapConstant.internalLdapBase + "))";
        return ldapTempleteInternal.search(LdapConfig.NLPGROUP + "=" + LdapConfig.NLPROLEBASEOU, searchFilter,
                (AttributesMapper<String>) attrs -> (String) attrs.get(ldapConstant.groupNameAttribute)
                        .get());
    } else {
        List<String> userRoles = new LinkedList<String>();
        List<String> userRolesInLdap = ldapTempleteInternal.search(
                query().where("objectclass").is(ldapConstant.userGroupObjectClass)
                        .and(query().where(LdapConfig.NLPGROUP).is(LdapConfig.NLPROLEBASEOU)),
                (AttributesMapper<String>) attrs -> {
                    String userRole = "";
                    for (int i = 0; i < attrs.get(ldapConstant.groupMemberAttribute).size(); i++) {
                        if (attrs.get(ldapConstant.groupMemberAttribute).get(i) != null && !attrs.get(ldapConstant.groupMemberAttribute).get(i).toString().trim().isEmpty()) {
                            if (attrs.get(ldapConstant.groupMemberAttribute).get(i).toString().indexOf(ldapConstant.userNameAttribute + "=" + uid) >= 0) {
                                userRole = attrs.get(ldapConstant.groupNameAttribute).toString();
                                if (userRole.toLowerCase().startsWith("cn:") || userRole.toLowerCase().startsWith("uid:")) {
                                    userRole = userRole.split(":")[1].trim();
                                }
                            }
                        }
                    }
                    return userRole;
                });
        userRolesInLdap.forEach(role -> {
            if (role != null && !role.trim().isEmpty()) userRoles.add(role);
        });
        return userRoles;
    }
}



}
