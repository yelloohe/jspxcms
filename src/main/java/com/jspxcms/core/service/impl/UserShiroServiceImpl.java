package com.jspxcms.core.service.impl;

import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserDetail;
import com.jspxcms.core.repository.UserDao;
import com.jspxcms.core.service.UserShiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class UserShiroServiceImpl implements UserShiroService {
    public User findByUsername(String username) {
        return dao.findByUsername(username);
    }

    @Override
    public User findByWeixinOpenid(String weixinOpenid) {
        return dao.findByWeixinOpenid(weixinOpenid);
    }

    public User findByQqOpenid(String qqOpenid) {
        return dao.findByQqOpenid(qqOpenid);
    }

    public User findByWeiboUid(String weiboUid) {
        return dao.findByWeiboUid(weiboUid);
    }

    public User get(Integer id) {
        return dao.findOne(id);
    }

    public User getAnonymous() {
        return dao.findOne(0);
    }

    public Integer getAnonymousId() {
        return 0;
    }

    @Transactional
    public User updateLoginSuccess(Integer userId, String loginIp) {
        User user = get(userId);
        UserDetail detail = user.getDetail();
        detail.setLoginErrorDate(null);
        detail.setLoginErrorCount(0);
        detail.setPrevLoginIp(detail.getLastLoginIp());
        detail.setPrevLoginDate(detail.getLastLoginDate());
        detail.setLastLoginIp(loginIp);
        detail.setLastLoginDate(new Timestamp(System.currentTimeMillis()));
        detail.setLogins(detail.getLogins() + 1);
        dao.save(user);
        return user;
    }

    @Transactional
    public User updateLoginFailure(String username) {
        User user = findByUsername(username);
        if (user != null) {
            UserDetail detail = user.getDetail();
            Date date = detail.getLoginErrorDate();
            if (date != null
                    && System.currentTimeMillis() - date.getTime() < User.LOGIN_ERROR_MILLIS) {
                detail.setLoginErrorCount(detail.getLoginErrorCount() + 1);
            } else {
                detail.setLoginErrorCount(1);
            }
            detail.setLoginErrorDate(new Timestamp(System.currentTimeMillis()));
        }
        return user;
    }

    private UserDao dao;

    @Autowired
    public void setDao(UserDao dao) {
        this.dao = dao;
    }
}
