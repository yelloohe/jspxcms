package com.jspxcms.core.service;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.GlobalMail;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * UserService
 *
 * @author liufang
 */
public interface UserService {
    public Page<User> findPage(Integer rank, Integer[] type, String orgTreeNumber, Map<String, String[]> params,
                               Pageable pageable);

    public RowSide<User> findSide(Integer rank, Integer[] type, String orgTreeNumber, Map<String, String[]> params,
                                  User bean, Integer position, Sort sort);

    public List<User> findByUsername(String[] usernames);

    public User findByUsername(String username);

    public User findByMobile(String mobile);

    public User findByEmail(String email);

    public User findByValidation(String type, String key);

    public long countByDate(Date beginDate);

    public boolean usernameExist(String username);

    public User getAnonymous();

    public Integer getAnonymousId();

    public User get(Integer id);

    public void updatePassword(Integer userId, String rawPassword);

    public void updateEmail(Integer userId, String email);

    public void sendVerifyEmail(Site site, User user, GlobalMail mail, String subject, String text);

    public User verifyMember(User user);

    public void sendPasswordEmail(Site site, User user, GlobalMail mail, String subject, String text);

    public User passwordChange(User user, String rawPassword);

    public User save(User bean, UserDetail detail, Integer[] roleIds, Integer[] orgIds, Integer[] groupIds,
                     Map<String, String> customs, Map<String, String> clobs, Integer orgId, Integer groupId, String ip);

    public User register(String ip, int groupId, int orgId, int status, String username, String password,String mobile, String email,
                         String qqOpenid, String weiboUid, String weixinOpenid, String gender, Date birthDate, String bio,
                         String comeFrom, String qq, String msn, String weixin);

    public User update(User bean, UserDetail detail, Integer[] roleIds, Integer[] orgIds, Integer[] groupIds,
                       Map<String, String> customs, Map<String, String> clobs, Integer orgId, Integer groupId, Integer topOrgId,
                       Integer siteId);

    public User update(User user, UserDetail detail);

    public User deletePassword(Integer id);

    public User[] deletePassword(Integer[] ids);

    public User check(Integer id);

    public User[] check(Integer[] ids);

    public User lock(Integer id);

    public User[] lock(Integer[] ids);

    public User unlock(Integer id);

    public User[] unlock(Integer[] ids);

    public User delete(Integer id);

    public User[] delete(Integer[] ids);
}
