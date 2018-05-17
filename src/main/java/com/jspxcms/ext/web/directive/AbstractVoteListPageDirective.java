package com.jspxcms.ext.web.directive;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.ext.domain.Vote;
import com.jspxcms.ext.service.VoteService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * AbstractQuestionListPageDirective
 *
 * @author liufang
 */
public abstract class AbstractVoteListPageDirective implements TemplateDirectiveModel {
    public static final String SITE_ID = "siteId";
    public static final String NUMBER = "number";
    public static final String IN_PERIOD = "inPeriod";
    public static final String STATUS = "status";

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void doExecute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body,
                          boolean isPage) throws TemplateException, IOException {
        if (loopVars.length < 1) {
            throw new TemplateModelException("Loop variable is required.");
        }
        if (body == null) {
            throw new RuntimeException("missing body");
        }
        Integer[] siteId = Freemarkers.getIntegers(params, SITE_ID);
        if (siteId == null && params.get(SITE_ID) == null) {
            siteId = new Integer[]{ForeContext.getSiteId(env)};
        }
        String[] number = Freemarkers.getStrings(params, NUMBER);
        Boolean inPeriod = Freemarkers.getBoolean(params, IN_PERIOD);
        Integer[] status = Freemarkers.getIntegers(params, STATUS);
        if (status == null) {
            status = new Integer[]{Vote.NORMAL_STATUS};
        }
        Sort defSort = new Sort(Direction.DESC, "creationDate");

        if (isPage) {
            Pageable pageable = Freemarkers.getPageable(params, env, defSort);
            Page<Vote> pagedList = service.findPage(number, inPeriod, status, siteId, pageable);
            ForeContext.setTotalPages(pagedList.getTotalPages());
            loopVars[0] = env.getObjectWrapper().wrap(pagedList);
        } else {
            Limitable limitable = Freemarkers.getLimitable(params, defSort);
            List<Vote> list = service.findList(number, inPeriod, status, siteId, limitable);
            loopVars[0] = env.getObjectWrapper().wrap(list);
        }

        body.render(env.getOut());
    }

    @Autowired
    private VoteService service;
}
