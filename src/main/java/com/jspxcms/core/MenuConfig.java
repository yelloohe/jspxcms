package com.jspxcms.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import com.jspxcms.core.holder.Menu;
import com.jspxcms.core.holder.MenuHolder;

/**
 * 读取后台功能菜单的配置类
 * <p>
 * 之前版本使用properties文件保存后台功能菜单信息，由于properties文件是无结构的，只能使用分号{@code ;}作为分隔符，这使得菜单信息难以阅读和维护。
 * <p>
 * 从8.0开始，菜单信息使用有结构的YAML文件存放，使得菜单结构更为清晰。
 * <p>
 * 系统会读取符合<code>classpath:conf/**&#47;menu*.yml</code>匹配规则的文件
 * 
 * @author liufang
 *
 */
@Configuration
public class MenuConfig {
	@Autowired
	private ApplicationContext appContext;

	@Bean
	@SuppressWarnings("unchecked")
	public MenuHolder menuProperties() throws IOException {
		Resource[] resources = appContext.getResources("classpath:conf/**/menu*.yml");
		HashSet<Menu> menus = new HashSet<Menu>();
		for (Resource resource : resources) {
			Yaml yaml = new Yaml();
			InputStream is = resource.getInputStream();
			Map<Object, Map<Object, Object>> map = (Map<Object, Map<Object, Object>>) yaml.load(is);
			for (Map.Entry<Object, Map<Object, Object>> entry : map.entrySet()) {
				menus.add(Menu.parse(entry.getKey(), entry.getValue()));
			}
		}
		TreeSet<Menu> sortedMenus = Menu.sort(menus);
		MenuHolder menuHolder = new MenuHolder(sortedMenus);
		return menuHolder;
	}
}
