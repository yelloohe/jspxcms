package com.jspxcms.common.freemarker;

import java.math.BigDecimal;
import java.util.List;

import com.jspxcms.common.web.TimerInterceptor;

import freemarker.core.Environment;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 执行时间
 * 
 * @author liufang
 * 
 */
public class ProcessedMethod implements TemplateMethodModelEx {
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		Environment env = Environment.getCurrentEnvironment();
		TemplateModel beginModel = env
				.getGlobalVariable(TimerInterceptor.TIMER_BEGIN);
		Long begin = Freemarkers.getLong(beginModel, TimerInterceptor.TIMER_BEGIN);
		if (begin != null) {
			long end = System.currentTimeMillis();
			BigDecimal processed = new BigDecimal(end - begin)
					.divide(new BigDecimal(1000));
			return TimerInterceptor.FORMAT.format(processed);
		} else {
			return "0";
		}
	}

}
