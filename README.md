# Jspxcms

本源码包采用maven结构，和普通eclipse的web项目结构不一样，且不包含lib目录下的第三方jar包。

## 环境要求

- JDK7或更高版本（支持JDK8）。建议使用JDK8，有更好的内存管理。更低版本的JDK7可能需要设置Java内存`-XX:PermSize=128M -XX:MaxPermSize=512M`，否则可能出现这种类型的内存溢出：`java.lang.OutOfMemoryError: PermGen space`。
- Servlet3.0或更高版本（如Tomcat7或更高版本）。
- MySQL5.5或更高版本（如需使用MySQL5.0，可将mysql驱动版本替换为5.1.24）；Oracle10g或更高版本；SQLServer2005或更高版本。
- Maven3.2或更高版本。
- 系统后台兼容的浏览器：IE9+、Edge、Firefox、Chrome。
- 前台页面兼容的浏览器取决于模板，使用者可以完全控制模板，理论上可以支持任何浏览器。自带的默认模板兼容的浏览器：IE8+、Edge、Firefox、Chrome。

## 搭建步骤

1. 创建数据库。如使用MySQL，字符集选择为`utf8`或者`utf8mb4`（支持更多特殊字符如表情字符emoji，推荐）。
2. 执行数据库脚本。数据库脚本在`database`目录下。
3. 在eclipse中导入maven项目。点击eclipse菜单`File` - `Import`，选择`Maven` - `Existing Maven Projects`。创建好maven项目后，会开始从maven服务器下载第三方jar包（如spring等），需要一定时间，请耐心等待。
4. 修改数据库连接。打开`/src/main/resources/application.propertis`文件，根据实际情况修改`spring.datasource.url`、`spring.datasource.username`、`spring.datasource.password`的值。
5. 启动程序。**请一定按照这里介绍的方式启动程序，否则可能因上下文路径（ContextPath）导致页面没有样式等问题**。在eclipse中，右键点击项目名，选择`Run as` - `Maven build...`，`Goals`填入`spring-boot:run`，`JRE`选项卡中可以选择JDK版本，`VM arguments`输入框中可以设置java内存参数，如使用JDK7，应在此处填入`-XX:PermSize=128M -XX:MaxPermSize=512M`，然后点击`Run`。
6. 访问系统。前台地址：[http://localhost:8080/](http://localhost:8080/)，使用手机访问前台或者使用浏览器模拟手机访问前台，系统会调用手机端模板，显示手机端的界面。后台地址：[http://localhost:8080/cmscp/index.do](http://localhost:8080/cmscp/index.do)，用户名：admin，密码：空。

## Maven镜像

使用国外的Maven仓库速度很慢、甚至连接不上，很容易出错。可以使用aliyun的Maven镜像仓库。

修改maven的settings.xml文件，mirrors部分使用以下配置：

	<mirrors>
	  <mirror>
	    <id>central</id>
	    <mirrorOf>*</mirrorOf>
	    <name>Nexus Aliyun</name>
	    <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
	  </mirror>
	</mirrors>

## 上下文路径

程序通常在部署在Tomcat根目录，首页访问地址类似`http://www.mysite.com/` `http://localhost/` `http://localhost:8080/`。在一些特殊的场合，如在Eclipse默认的Tomcat启动方式（非maven方式），可能将程序部署在某一个路径下，首页访问地址类似`http://www.mysite.com/jspxcms/` `http://localhost/mysite/` `http://localhost:8080/jspxcms/`。此时访问网站前台会出现样式不能正常显示的情况，可以到后台`系统 - 系统设置`中设置`上下文路径`，类似为`/jspxcms`，其中斜杠`/`不能省略，`jspxcms`为部署目录的路径，如在开发环境，则通常为项目名。

开发环境要避免使用上下文路径，除非网站正式部署时也要部署到相应的路径下，否则在开发环境下上传的图片部署到正式环境时，不能正常显示。因为上传图片时，图片地址会带有上下文路径的信息。

Eclipse默认的tomcat启动方式（非maven方式）会将程序部署到特定目录再启动，并不是直接在项目所在目录启动tomcat，这时上传的图片（包括通过系统后台新增和修改的模板）也保存在特定的部署目录，并不会保存在程序所在的目录。当修改了Eclipse源代码或文件，会自动重新部署程序，之前上传的图片会被清空。如果发现在开发环境下上传的图片突然都找不到了，很可能就是这个原因。

综上所述，强烈建议使用搭建步骤中介绍的方式启动程序。

## 后端技术

SpringBoot：提供了对Spring开箱即用的功能。简化了Spring配置，提供自动配置 auto-configuration功能。

Spring：是提供了IoC等功能，是目前最流行的Java企业级开发框架。

SpringMVC：MVC框架，使用方便，Bug较少。

JPA：持久化框架。属于JSR标准，JPA实现选择最常用的Hibernate。

SpringDataJPA：对JPA封装，大部分查询只需要在接口中写方法，而不需要实现改方法，极大开发效率。

QueryDSL：实现类型安全的JPA查询，使用对象及属性实现查询，避免编写jpql出现的拼错字符及属性名记忆负担。

FreeMarker：模板组件。

Shiro：安全组件。配置简便。

Lucene：全文检索组件。实现对中文的分词搜索。

Ehcache：缓存组件。主要用在JPA二级缓存、Shiro权限缓存。

Quartz：定时任务组件。

## 前端技术

jQuery：JavaScript库。

Bootstrap：响应式设计前端框架。

AdminLTE：后台管理平台开源框架。

jQuery UI：基于jQuery的UI框架。

jQuery Validation：基于jQuery的表单校验框架。

UEditor：Web富文本编辑器。

Editor.md：基于Markdown语法的Web文本编辑器。

ECharts：用于生成图标的组件。

My97DatePicker：日期组件。

zTree：树组件。

