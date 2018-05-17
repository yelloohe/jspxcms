/*==============================================================*/
/* Table: cms_ad                                                */
/*==============================================================*/
create table cms_ad
(
   f_ad_id              int not null,
   f_site_id            int not null,
   f_adslot_id          int not null,
   f_name               varchar(150) not null comment '名称',
   f_begin_date         datetime comment '开始时间',
   f_end_date           datetime comment '结束时间',
   f_url                varchar(255) comment '广告url',
   f_text               varchar(255) comment '文字',
   f_script             mediumtext comment '代码',
   f_image              varchar(255) comment '图片',
   f_flash              varchar(255) comment 'flash',
   f_seq                int not null default 1 comment '排序',
   primary key (f_ad_id)
)
engine = innodb;

alter table cms_ad comment '广告表';

/*==============================================================*/
/* Table: cms_ad_slot                                           */
/*==============================================================*/
create table cms_ad_slot
(
   f_adslot_id          int not null,
   f_site_id            int not null,
   f_name               varchar(100) not null comment '名称',
   f_number             varchar(100) comment '编码',
   f_description        varchar(255) comment '描述',
   f_type               int not null comment '类型(1:文字,2:图片,3:FLASH,4:代码)',
   f_template           varchar(255) not null comment '模板',
   f_width              int not null comment '宽度',
   f_height             int not null comment '高度',
   primary key (f_adslot_id)
)
engine = innodb;

alter table cms_ad_slot comment '广告版位表';

/*==============================================================*/
/* Table: cms_attachment                                        */
/*==============================================================*/
create table cms_attachment
(
   f_attachment_id      int not null,
   f_site_id            int not null comment '站点',
   f_user_id            int not null comment '上传人',
   f_name               varchar(150) not null comment '文件名',
   f_length             bigint comment '文件长度',
   f_ip                 varchar(100) comment 'IP',
   f_time               datetime not null comment '时间',
   primary key (f_attachment_id)
)
engine = innodb;

alter table cms_attachment comment '附件表';

/*==============================================================*/
/* Table: cms_attachment_ref                                    */
/*==============================================================*/
create table cms_attachment_ref
(
   f_attachementref_id  int not null,
   f_site_id            int not null,
   f_attachment_id      int not null,
   f_ftype              varchar(100) not null comment '外表标识',
   f_fid                int not null comment '外表ID',
   primary key (f_attachementref_id)
)
engine = innodb;

/*==============================================================*/
/* Table: cms_attribute                                         */
/*==============================================================*/
create table cms_attribute
(
   f_attribute_id       int not null,
   f_site_id            int not null comment '站点',
   f_number             varchar(20) not null comment '代码',
   f_name               varchar(50) not null comment '名称',
   f_seq                int not null default 2147483647 comment '排序',
   f_is_with_image      char(1) not null default '0' comment '是否包含图片',
   f_is_scale           char(1) not null default '0' comment '是否图片压缩',
   f_is_exact           char(1) not null default '0' comment '是否图片拉伸',
   f_is_watermark       char(1) not null default '0' comment '是否图片水印',
   f_image_width        int comment '图片宽度',
   f_image_height       int comment '图片高度',
   primary key (f_attribute_id)
)
engine = innodb;

alter table cms_attribute comment '属性表';

/*==============================================================*/
/* Table: cms_collect                                           */
/*==============================================================*/
create table cms_collect
(
   f_collect_id         int not null,
   f_node_id            int not null,
   f_site_id            int not null,
   f_user_id            int not null,
   f_name               varchar(100) not null comment '名称',
   f_charset            varchar(100) not null default 'UTF-8' comment '字符集',
   f_user_agent         varchar(255) not null default 'Mozilla/5.0' comment '用户代理',
   f_interval_min       int not null default 0 comment '最小间隔时间',
   f_interval_max       int not null default 0 comment '最大间隔时间',
   f_list_pattern       varchar(2000) not null comment '列表地址',
   f_list_next_pattern  varchar(255) comment '下一页列表地址',
   f_is_list_next_reg   char(1) not null default '0' comment '下一页列表地址是否正则',
   f_item_area_pattern  varchar(255) comment '文章地址区域',
   f_is_item_area_reg   char(1) not null default '0' comment '文章地址区域是否正则',
   f_item_pattern       varchar(255) not null comment '文章地址',
   f_is_item_reg        char(1) not null default '0' comment '文章地址是否正则',
   f_block_area_pattern varchar(255) comment '文章块区域',
   f_is_block_area_reg  char(1) not null default '0' comment '文章快区域是否正则',
   f_block_pattern      varchar(255) comment '文章块',
   f_is_block_reg       char(1) not null default '0' comment '文章块是否正则',
   f_page_begin         int not null default 2 comment '起始序号',
   f_page_end           int not null default 10 comment '结束序号',
   f_is_desc            char(1) not null default '1' comment '是否倒序',
   f_is_submit          char(1) not null default '0' comment '是否提交',
   f_is_allow_duplicate char(1) not null default '0' comment '是否允许重复标题',
   f_is_download_image  char(1) not null default '1' comment '是否采集正文中的图片',
   f_status             int not null default 0 comment '状态(0:就绪,1:运行中,2:暂停)',
   primary key (f_collect_id)
)
engine = innodb;

alter table cms_collect comment '采集表';

/*==============================================================*/
/* Table: cms_collect_field                                     */
/*==============================================================*/
create table cms_collect_field
(
   f_collectfield_id    int not null,
   f_collect_id         int not null,
   f_site_id            int not null,
   f_name               varchar(100) not null comment '名称',
   f_code               varchar(100) not null comment '代码',
   f_type               int not null default 1 comment '类型(1;系统字段,2:custom字段,3:clob字段)',
   f_source_type        int not null default 1 comment '来源(1:详细页,2:列表页,3:固定值,4:URL)',
   f_source_url         varchar(255) comment '来源URL',
   f_source_text        varchar(255) comment '来源文本',
   f_data_area_pattern  varchar(255) comment '数据区域',
   f_is_data_area_reg   char(1) not null default '0' comment '数据区域是否正则',
   f_data_pattern       varchar(255) comment '匹配规则',
   f_is_data_reg        char(1) not null default '0' comment '匹配规则是否正则',
   f_date_format        varchar(255) comment '日期格式',
   f_download_type      varchar(20) comment '下载类型(为空不下载)',
   f_image_param        varchar(255) comment '图片参数',
   f_filter             varchar(2000) comment '过滤规则',
   f_seq                int not null default 2147483647 comment '排列顺序',
   primary key (f_collectfield_id)
)
engine = innodb;

alter table cms_collect_field comment '采集字段表';

/*==============================================================*/
/* Table: cms_collect_log                                       */
/*==============================================================*/
create table cms_collect_log
(
   f_collectlog_id      int not null,
   f_site_id            int not null,
   f_url                varchar(255) not null comment '采集地址',
   f_title              varchar(255) comment '标题',
   f_message            varchar(255) comment '消息',
   f_time               datetime not null comment '时间',
   f_status             int not null default 0 comment '状态(0:成功,1:失败)',
   primary key (f_collectlog_id)
)
engine = innodb;

alter table cms_collect_log comment '采集日志表';

/*==============================================================*/
/* Table: cms_comment                                           */
/*==============================================================*/
create table cms_comment
(
   f_comment_id         int not null,
   f_parent_id          int comment '父评论ID',
   f_site_id            int not null comment '站点ID',
   f_creator_id         int not null comment '创建人',
   f_auditor_id         int comment '审核人',
   f_ftype              varchar(50) not null comment '外表标识',
   f_fid                int not null comment '外表ID',
   f_creation_date      datetime not null comment '评论时间',
   f_audit_date         datetime comment '审核时间',
   f_ip                 varchar(100) not null default 'localhost' comment 'IP地址',
   f_country            varchar(100) comment '国家',
   f_area               varchar(100) comment '地区',
   f_score              int not null default 0 comment '得分',
   f_status             int not null default 0 comment '0:未审核;1:已审核;2:推荐;3:屏蔽',
   f_text               mediumtext,
   primary key (f_comment_id)
)
engine = innodb;

alter table cms_comment comment '评论表';

/*==============================================================*/
/* Table: cms_favorite                                          */
/*==============================================================*/
create table cms_favorite
(
   favorite_id_         int not null,
   user_id_             int not null comment '收藏用户',
   dtype_               varchar(50) not null comment '收藏数据类型',
   did_                 int not null comment '收藏数据ID',
   created_             datetime not null comment '收藏时间',
   primary key (favorite_id_)
)
engine = innodb;

alter table cms_favorite comment '收藏表';

/*==============================================================*/
/* Table: cms_friendlink                                        */
/*==============================================================*/
create table cms_friendlink
(
   f_friendlink_id      int not null,
   f_friendlinktype_id  int not null,
   f_site_id            int not null,
   f_name               varchar(100) not null comment '网站名称',
   f_url                varchar(255) not null comment '网站地址',
   f_seq                int not null default 2147483647 comment '排序',
   f_logo               varchar(255) comment '网站Logo',
   f_description        varchar(255) comment '网站描述',
   f_email              varchar(100) comment '站长Email',
   f_is_with_logo       char(1) not null default '0' comment '是否带Logo',
   f_is_recommend       char(1) not null default '0' comment '是否推荐',
   f_status             int not null default 0 comment '状态(0:已审核,1:未审核)',
   primary key (f_friendlink_id)
)
engine = innodb;

alter table cms_friendlink comment '友情链接表';

/*==============================================================*/
/* Table: cms_friendlink_type                                   */
/*==============================================================*/
create table cms_friendlink_type
(
   f_friendlinktype_id  int not null,
   f_site_id            int not null,
   f_name               varchar(100) not null comment '名称',
   f_number             varchar(100) comment '编码',
   f_seq                int not null default 2147483647 comment '排序',
   primary key (f_friendlinktype_id)
)
engine = innodb;

alter table cms_friendlink_type comment '友情链接类型表';

/*==============================================================*/
/* Table: cms_global                                            */
/*==============================================================*/
create table cms_global
(
   f_global_id          int not null,
   f_protocol           varchar(50) not null default 'http' comment '协议',
   f_port               int comment '服务端口号',
   f_context_path       varchar(255) comment '上下文路径',
   f_uploads_publishpoint_id int comment '附件发布点',
   f_captcha_errors     int not null default 3 comment '需要验证码的错误次数(总是需要则为0)',
   f_version            varchar(50) not null comment 'jspxcms版本号',
   primary key (f_global_id)
)
engine = innodb;

alter table cms_global comment '全局表';

/*==============================================================*/
/* Table: cms_global_clob                                       */
/*==============================================================*/
create table cms_global_clob
(
   f_global_id          int not null,
   f_key                varchar(50) not null comment '键',
   f_value              mediumtext comment '值'
)
engine = innodb;

alter table cms_global_clob comment '全局大字段表';

/*==============================================================*/
/* Table: cms_global_custom                                     */
/*==============================================================*/
create table cms_global_custom
(
   f_global_id          int not null,
   f_key                varchar(50) not null comment '键',
   f_value              varchar(2000) comment '值'
)
engine = innodb;

alter table cms_global_custom comment '全局自定义表';

/*==============================================================*/
/* Table: cms_guestbook                                         */
/*==============================================================*/
create table cms_guestbook
(
   f_guestbook_id       int not null,
   f_site_id            int not null comment '站点',
   f_guestbooktype_id   int not null comment '留言类型',
   f_creator_id         int not null comment '创建者',
   f_replyer_id         int comment '回复者',
   f_title              varchar(150) comment '留言标题',
   f_text               mediumtext comment '留言内容',
   f_creation_date      datetime not null comment '留言日期',
   f_creation_ip        varchar(100) not null comment '留言IP',
   f_creation_country   varchar(100) comment '留言国家',
   f_creation_area      varchar(100) comment '留言地区',
   f_reply_text         mediumtext comment '回复内容',
   f_reply_date         datetime comment '回复日期',
   f_reply_ip           varchar(100) comment '回复IP',
   f_reply_country      varchar(100) comment '回复国家',
   f_reply_area         varchar(100) comment '回复地区',
   f_is_reply           char(1) not null default '0' comment '是否回复',
   f_is_recommend       char(1) not null default '0' comment '是否推荐',
   f_status             int not null default 0 comment '状态(0:已审核,1:未审核,2:屏蔽)',
   f_username           varchar(100) comment '用户名',
   f_gender             char(1) comment '性别',
   f_phone              varchar(100) comment '电话',
   f_mobile             varchar(100) comment '手机',
   f_qq                 varchar(100) comment 'QQ号码',
   f_email              varchar(100) comment '电子邮箱',
   primary key (f_guestbook_id)
)
engine = innodb;

alter table cms_guestbook comment '留言板表';

/*==============================================================*/
/* Table: cms_guestbook_type                                    */
/*==============================================================*/
create table cms_guestbook_type
(
   f_guestbooktype_id   int not null,
   f_site_id            int not null comment '站点',
   f_name               varchar(100) not null comment '名称',
   f_number             varchar(100) comment '编码',
   f_seq                int not null default 2147483647 comment '排序',
   f_description        varchar(255) comment '描述',
   primary key (f_guestbooktype_id)
)
engine = innodb;

alter table cms_guestbook_type comment '留言板类型表';

/*==============================================================*/
/* Table: cms_info                                              */
/*==============================================================*/
create table cms_info
(
   f_info_id            int not null,
   f_org_id             int not null comment '组织',
   f_creator_id         int not null comment '创建者',
   f_site_id            int not null comment '站点',
   f_node_id            int not null comment '栏目',
   f_from_site_id       int comment '推送来源站点',
   f_publish_date       datetime not null comment '发布日期',
   f_off_date           datetime comment '关闭日期',
   f_priority           tinyint not null default 0 comment '优先级',
   f_is_with_image      char(1) not null default '0' comment '是否包含图片',
   f_views              int not null default 0 comment '浏览总数',
   f_downloads          int not null default 0 comment '下载总数',
   f_comments           int not null default 0 comment '评论总数',
   f_diggs              int not null default 0 comment '顶',
   f_score              int not null default 0 comment '得分',
   f_favorites          int not null default 0 comment '收藏次数',
   f_status             char(1) not null default 'A' comment '状态(0:发起者,1-9:审核中,A:已发布,B:草稿,C:投稿,D:退稿,E:采集,F:待发布,G:已过期,X:回收站,Z:归档)',
   f_p0                 tinyint comment '自定义0',
   f_p1                 tinyint comment '自定义1',
   f_p2                 tinyint comment '自定义2',
   f_p3                 tinyint comment '自定义3',
   f_p4                 tinyint comment '自定义4',
   f_p5                 tinyint comment '自定义5',
   f_p6                 tinyint comment '自定义6',
   f_html_status        char(1) not null default '0' comment 'HTML状态(0:未开启,1:待生成,2:待更新,3:已生成)',
   primary key (f_info_id)
)
engine = innodb;

alter table cms_info comment '文档表';

/*==============================================================*/
/* Table: cms_info_attribute                                    */
/*==============================================================*/
create table cms_info_attribute
(
   f_info_id            int not null comment '文档',
   f_attribute_id       int not null comment '属性',
   f_image              varchar(255) comment '属性图片'
)
engine = innodb;

alter table cms_info_attribute comment '文档与属性关联表';

/*==============================================================*/
/* Table: cms_info_buffer                                       */
/*==============================================================*/
create table cms_info_buffer
(
   f_info_id            int not null,
   f_views              int not null default 0 comment '浏览次数',
   f_downloads          int not null default 0 comment '下载次数',
   f_comments           int not null default 0 comment '评论次数',
   f_involveds          int not null default 0 comment '评论参与人数',
   f_diggs              int not null default 0 comment '顶',
   f_burys              int not null default 0 comment '踩',
   f_score              int not null default 0 comment '得分',
   primary key (f_info_id)
)
engine = innodb;

alter table cms_info_buffer comment '文档缓冲表';

/*==============================================================*/
/* Table: cms_info_clob                                         */
/*==============================================================*/
create table cms_info_clob
(
   f_info_id            int not null,
   f_key                varchar(50) not null comment '键',
   f_value              mediumtext comment '值'
)
engine = innodb;

alter table cms_info_clob comment '文档大字段表';

/*==============================================================*/
/* Table: cms_info_custom                                       */
/*==============================================================*/
create table cms_info_custom
(
   f_info_id            int not null,
   f_key                varchar(50) not null comment '键',
   f_value              varchar(2000) comment '值'
)
engine = innodb;

alter table cms_info_custom comment '文档自定义表';

/*==============================================================*/
/* Table: cms_info_detail                                       */
/*==============================================================*/
create table cms_info_detail
(
   f_info_id            int not null,
   f_title              varchar(150) not null comment '主标题',
   f_html               varchar(255) comment 'HTML页面',
   f_mobile_html        varchar(255) comment '手机端HTML页面',
   f_subtitle           varchar(150) comment '副标题',
   f_full_title         varchar(150) comment '完整标题',
   f_link               varchar(255) comment '转向链接',
   f_is_new_window      char(1) comment '是否在新窗口打开',
   f_color              varchar(50) comment '颜色',
   f_is_strong          char(1) not null default '0' comment '是否粗体',
   f_is_em              char(1) not null default '0' comment '是否斜体',
   f_info_path          varchar(255) comment '文档路径',
   f_info_template      varchar(255) comment '文档模板',
   f_meta_description   varchar(450) comment 'meta描述',
   f_source             varchar(50) comment '来源名称',
   f_source_url         varchar(255) comment '来源url',
   f_author             varchar(50) comment '作者',
   f_small_image        varchar(255) comment '小图',
   f_large_image        varchar(255) comment '大图',
   f_video              varchar(255) comment '视频url',
   f_video_name         varchar(255) comment '视频名称',
   f_video_length       bigint comment '视频长度',
   f_video_time         varchar(100) comment '视频时间',
   f_file               varchar(255) comment '文件url',
   f_file_name          varchar(255) comment '文件名称',
   f_file_length        bigint comment '文件长度',
   f_doc                varchar(255) comment '文库url',
   f_doc_name           varchar(255) comment '文库名称',
   f_doc_length         varchar(255) comment '文库长度',
   f_doc_pdf            varchar(255) comment '文库PDF',
   f_doc_swf            varchar(255) comment '文库SWF',
   f_is_weixin_mass     char(1) not null default '0' comment '是否微信群发',
   f_is_allow_comment   char(1) comment '是否允许评论',
   f_step_name          varchar(100) comment '审核步骤名称',
   primary key (f_info_id)
)
engine = innodb;

alter table cms_info_detail comment '文档详细表';

/*==============================================================*/
/* Table: cms_info_file                                         */
/*==============================================================*/
create table cms_info_file
(
   f_info_id            int not null,
   f_name               varchar(150) not null comment '文件名称',
   f_length             bigint not null default 0 comment '文件长度',
   f_file               varchar(255) not null comment '文件地址',
   f_index              int not null default 0 comment '文件序号',
   f_downloads          int not null default 0 comment '下载次数'
)
engine = innodb;

alter table cms_info_file comment '文档附件集表';

/*==============================================================*/
/* Table: cms_info_image                                        */
/*==============================================================*/
create table cms_info_image
(
   f_info_id            int not null,
   f_name               varchar(150) comment '图片名称',
   f_image              varchar(255) comment '图片地址',
   f_index              int not null default 0 comment '图片序号',
   f_text               mediumtext comment '图片正文'
)
engine = innodb;

alter table cms_info_image comment '文档图片集表';

/*==============================================================*/
/* Table: cms_info_membergroup                                  */
/*==============================================================*/
create table cms_info_membergroup
(
   f_membergroup_id     int not null,
   f_info_id            int not null,
   f_is_view_perm       char(1) not null default '0' comment '是否有浏览权限'
)
engine = innodb;

alter table cms_info_membergroup comment '文档与会员组权限表';

/*==============================================================*/
/* Table: cms_info_node                                         */
/*==============================================================*/
create table cms_info_node
(
   f_info_id            int not null comment '文档',
   f_node_id            int not null comment '栏目',
   f_node_index         int default 0 comment '栏目顺序'
)
engine = innodb;

alter table cms_info_node comment '文档与栏目关联表';

/*==============================================================*/
/* Table: cms_info_org                                          */
/*==============================================================*/
create table cms_info_org
(
   f_info_id            int not null,
   f_org_id             int not null,
   f_is_view_perm       char(1) not null default '0' comment '是否有浏览权限'
)
engine = innodb;

alter table cms_info_org comment '文档与组织权限表';

/*==============================================================*/
/* Table: cms_info_push                                         */
/*==============================================================*/
create table cms_info_push
(
   infopush_id_         int not null,
   info_id_             int not null comment '推送文档',
   from_site_id_        int not null comment '推送源站点',
   to_site_id_          int not null comment '推送目标站点',
   user_id_             int not null comment '推送人',
   created_             datetime not null comment '推送时间',
   primary key (infopush_id_)
)
engine = innodb;

alter table cms_info_push comment '文档推送表';

/*==============================================================*/
/* Table: cms_info_special                                      */
/*==============================================================*/
create table cms_info_special
(
   f_info_id            int not null comment '文档',
   f_special_id         int not null comment '专题',
   f_special_index      int default 0 comment '专题序号'
)
engine = innodb;

alter table cms_info_special comment '文档与专题关联表';

/*==============================================================*/
/* Table: cms_info_tag                                          */
/*==============================================================*/
create table cms_info_tag
(
   f_info_id            int not null comment '文档',
   f_tag_id             int not null comment 'tag',
   f_tag_index          int default 0 comment 'tag序号'
)
engine = innodb;

alter table cms_info_tag comment '文档与tag关联表';

/*==============================================================*/
/* Table: cms_mail_inbox                                        */
/*==============================================================*/
create table cms_mail_inbox
(
   mailinbox_id_        int not null,
   mailoutbox_id_       int not null,
   mailtext_id_         int not null,
   sender_id_           int comment '发件人',
   receiver_id_         int not null comment '接收人',
   receive_time_        datetime not null comment '接收时间',
   is_unread_           int not null default 1 comment '是否未读',
   primary key (mailinbox_id_)
)
engine = innodb;

alter table cms_mail_inbox comment '系统消息收件表';

/*==============================================================*/
/* Table: cms_mail_outbox                                       */
/*==============================================================*/
create table cms_mail_outbox
(
   mailoutbox_id_       int not null,
   mailtext_id_         int not null,
   sender_id_           int comment '发送人',
   send_time_           datetime not null comment '发送时间',
   receiver_number_     int not null comment '接收人数',
   read_number_         int not null comment '已读人数',
   primary key (mailoutbox_id_)
)
engine = innodb;

alter table cms_mail_outbox comment '系统消息发件表';

/*==============================================================*/
/* Table: cms_mail_text                                         */
/*==============================================================*/
create table cms_mail_text
(
   mailtext_id_         int not null,
   subject_             varchar(150) comment '标题',
   text_                mediumtext not null comment '内容',
   primary key (mailtext_id_)
)
engine = innodb;

alter table cms_mail_text comment '系统消息正文表';

/*==============================================================*/
/* Table: cms_member_group                                      */
/*==============================================================*/
create table cms_member_group
(
   f_membergroup_id     int not null,
   f_name               varchar(100) not null,
   f_description        varchar(255),
   f_ip                 mediumtext comment 'IP',
   f_type               int not null default 0 comment '类型(0:普通组,1:游客组,2:IP组,3:待验证组)',
   f_seq                int not null default 2147483647 comment '排序',
   primary key (f_membergroup_id)
)
engine = innodb;

alter table cms_member_group comment '会员组表';

/*==============================================================*/
/* Table: cms_message                                           */
/*==============================================================*/
create table cms_message
(
   message_id_          int not null comment '私信',
   sender_id_           int not null comment '发送人',
   receiver_id_         int not null comment '收件人',
   send_time_           datetime not null comment '发送时间',
   deletion_flag_       int not null default 0 comment '删除标志(0:正常;1:发件人删除;2:收件人删除;)',
   is_unread_           int not null default 1 comment '是否未读',
   primary key (message_id_)
)
engine = innodb;

alter table cms_message comment '私信表';

/*==============================================================*/
/* Table: cms_message_text                                      */
/*==============================================================*/
create table cms_message_text
(
   message_id_          int not null comment '私信',
   subject_             varchar(150) comment '标题',
   text_                mediumtext not null comment '内容',
   primary key (message_id_)
)
engine = innodb;

alter table cms_message_text comment '私信正文表';

/*==============================================================*/
/* Table: cms_model                                             */
/*==============================================================*/
create table cms_model
(
   f_model_id           int not null,
   f_site_id            int not null comment '站点',
   f_type               varchar(100) not null comment '类型(info:文档,node:栏目,node_home:首页;special:专题)',
   f_name               varchar(50) not null comment '名称',
   f_number             varchar(100) comment '代码',
   f_seq                int not null default 10 comment '顺序',
   primary key (f_model_id)
)
engine = innodb;

alter table cms_model comment '模型表';

/*==============================================================*/
/* Table: cms_model_custom                                      */
/*==============================================================*/
create table cms_model_custom
(
   f_model_id           int not null,
   f_key                varchar(50) not null comment '键',
   f_value              varchar(2000) comment '值'
)
engine = innodb;

alter table cms_model_custom comment '模型自定义表';

/*==============================================================*/
/* Table: cms_model_field                                       */
/*==============================================================*/
create table cms_model_field
(
   f_modefiel_id        int not null,
   f_model_id           int not null comment '模型',
   f_type               int not null comment '输入类型',
   f_inner_type         int not null default 0 comment '内部类型(0:用户自定义字段;1:系统定义字段;2:预留大文本字段;3:预留可查询字段)',
   f_label              varchar(50) not null comment '字段标签',
   f_name               varchar(50) not null comment '字段名称',
   f_prompt             varchar(255) comment '提示信息',
   f_def_value          varchar(255) comment '默认值',
   f_is_required        char(1) not null default '0' comment '是否必填项',
   f_seq                int not null default 10 comment '顺序',
   f_is_dbl_column      char(1) not null default '0' comment '是否双列布局',
   f_is_disabled        char(1) not null default '0' comment '是否禁用',
   primary key (f_modefiel_id)
)
engine = innodb;

alter table cms_model_field comment '模型字段表';

/*==============================================================*/
/* Table: cms_model_field_custom                                */
/*==============================================================*/
create table cms_model_field_custom
(
   f_modefiel_id        int not null,
   f_key                varchar(50) not null comment '键',
   f_value              varchar(2000) comment '值'
)
engine = innodb;

alter table cms_model_field_custom comment '模型字段自定义信息表';

/*==============================================================*/
/* Table: cms_node                                              */
/*==============================================================*/
create table cms_node
(
   f_node_id            int not null,
   f_site_id            int not null comment '站点',
   f_parent_id          int comment '栏目点',
   f_creator_id         int not null comment '创建者',
   f_node_model_id      int not null comment '栏目模型',
   f_workflow_id        int comment '流程',
   f_info_model_id      int comment '文档模型',
   f_number             varchar(100) comment '代码',
   f_name               varchar(150) not null comment '名称',
   f_tree_number        varchar(100) not null default '0000' comment '树编码',
   f_tree_level         int not null default 0 comment '树级别',
   f_tree_max           varchar(10) not null default '0000' comment '树子节点最大编码',
   f_creation_date      datetime not null comment '创建时间',
   f_refers             int not null default 0 comment '文档数量',
   f_views              int not null default 0 comment '浏览总数',
   f_is_real_node       char(1) not null default '1' comment '是否真实栏目',
   f_is_hidden          char(1) not null default '0' comment '是否隐藏',
   f_html_status        char(1) not null default '0' comment 'HTML状态(0:未开启,1:待生成,2:待更新,3:已生成)',
   f_p0                 tinyint comment '自定义0',
   f_p1                 tinyint comment '自定义1',
   f_p2                 tinyint comment '自定义2',
   f_p3                 tinyint comment '自定义3',
   f_p4                 tinyint comment '自定义4',
   f_p5                 tinyint comment '自定义5',
   f_p6                 tinyint comment '自定义6',
   primary key (f_node_id)
)
engine = innodb;

alter table cms_node comment '栏目表';

/*==============================================================*/
/* Table: cms_node_buffer                                       */
/*==============================================================*/
create table cms_node_buffer
(
   f_node_id            int not null,
   f_views              int not null comment '浏览次数',
   primary key (f_node_id)
)
engine = innodb;

alter table cms_node_buffer comment '栏目缓冲表';

/*==============================================================*/
/* Table: cms_node_clob                                         */
/*==============================================================*/
create table cms_node_clob
(
   f_node_id            int not null,
   f_key                varchar(50) not null comment '键',
   f_value              mediumtext comment '值'
)
engine = innodb;

alter table cms_node_clob comment '栏目大字段表';

/*==============================================================*/
/* Table: cms_node_custom                                       */
/*==============================================================*/
create table cms_node_custom
(
   f_node_id            int not null,
   f_key                varchar(50) not null comment '键',
   f_value              varchar(2000) comment '值'
)
engine = innodb;

alter table cms_node_custom comment '栏目自定义表';

/*==============================================================*/
/* Table: cms_node_detail                                       */
/*==============================================================*/
create table cms_node_detail
(
   f_node_id            int not null,
   f_link               varchar(255) comment '转向链接',
   f_html               varchar(255) comment 'HTML页面',
   f_mobile_html        varchar(255) comment '手机端HTML页面',
   f_meta_keywords      varchar(150) comment '关键字',
   f_meta_description   varchar(450) comment '描述',
   f_is_new_window      char(1) comment '是否在新窗口打开',
   f_node_template      varchar(255) comment '栏目模板',
   f_info_template      varchar(255) comment '文档模板',
   f_is_generate_node   char(1) comment '是否生成栏目页',
   f_is_generate_info   char(1) comment '是否生成文档页',
   f_node_extension     varchar(10) comment '栏目页扩展名',
   f_info_extension     varchar(10) comment '文档页扩展名',
   f_node_path          varchar(100) comment '栏目路径',
   f_info_path          varchar(100) comment '文档路径',
   f_is_def_page        char(1) comment '是否默认页',
   f_static_method      int comment '静态页生成方式(0:手动生成;1:自动生成栏目页;2:自动生成文档页及栏目页;3:自动生成文档页、栏目页、父栏目页、首页)',
   f_static_page        int comment '栏目列表静态化页数',
   f_small_image        varchar(255) comment '小图',
   f_large_image        varchar(255) comment '大图',
   primary key (f_node_id)
)
engine = innodb;

alter table cms_node_detail comment '栏目详细表';

/*==============================================================*/
/* Table: cms_node_membergroup                                  */
/*==============================================================*/
create table cms_node_membergroup
(
   f_node_id            int not null,
   f_membergroup_id     int not null,
   f_is_view_perm       char(1) not null default '1' comment '是否有浏览权限',
   f_is_contri_perm     char(1) not null default '1' comment '是否有投稿权限',
   f_is_comment_perm    char(1) not null default '1' comment '是否有评论权限'
)
engine = innodb;

alter table cms_node_membergroup comment '栏目与用户组权限表';

/*==============================================================*/
/* Table: cms_node_org                                          */
/*==============================================================*/
create table cms_node_org
(
   f_org_id             int not null,
   f_node_id            int not null,
   f_is_view_perm       char(1) not null default '0' comment '是否有浏览权限'
)
engine = innodb;

alter table cms_node_org comment '栏目与组织权限表';

/*==============================================================*/
/* Table: cms_node_role                                         */
/*==============================================================*/
create table cms_node_role
(
   f_node_id            int not null,
   f_role_id            int not null,
   f_is_node_perm       char(1) not null default '1' comment '栏目权限',
   f_is_info_perm       char(1) not null default '1' comment '文档权限'
)
engine = innodb;

alter table cms_node_role comment '栏目与角色权限表';

/*==============================================================*/
/* Table: cms_notification                                      */
/*==============================================================*/
create table cms_notification
(
   notification_id_     int not null,
   receiver_id_         int not null comment '接收者',
   type_                varchar(50) not null comment '类别',
   key_                 int not null comment 'KEY',
   qty_                 int not null default 0 comment '数量',
   url_                 varchar(255) comment 'URL',
   backend_url_         varchar(255) comment '后端URL',
   text_                varchar(2000) not null comment '通知正文',
   send_time_           datetime not null comment '通知时间',
   primary key (notification_id_)
)
engine = innodb;

alter table cms_notification comment '通知表';

/*==============================================================*/
/* Table: cms_notification_source                               */
/*==============================================================*/
create table cms_notification_source
(
   notification_id_     int not null,
   source_              varchar(255) not null,
   source_order_        int not null
)
engine = innodb;

alter table cms_notification_source comment '通知来源表';

/*==============================================================*/
/* Table: cms_operation_log                                     */
/*==============================================================*/
create table cms_operation_log
(
   f_operation_id       int not null,
   f_user_id            int not null comment '操作人',
   f_site_id            int not null comment '站点',
   f_name               varchar(150) not null comment '名称',
   f_data_id            int comment '数据ID',
   f_description        varchar(255) comment '描述',
   f_text               mediumtext comment '详情',
   f_ip                 varchar(100) not null comment 'IP',
   f_country            varchar(100) comment '国家',
   f_area               varchar(100) comment '地区',
   f_time               datetime not null comment '时间',
   f_type               int not null default 1 comment '类型(1:操作日志,2:登录日志,3:登录失败)',
   primary key (f_operation_id)
)
engine = innodb;

alter table cms_operation_log comment '操作日志表';

/*==============================================================*/
/* Table: cms_org                                               */
/*==============================================================*/
create table cms_org
(
   f_org_id             int not null,
   f_parent_id          int comment '上级组织',
   f_name               varchar(150) not null comment '名称',
   f_full_name          varchar(150) comment '全称',
   f_description        varchar(255) comment '描述',
   f_contacts           varchar(100) comment '联系人',
   f_number             varchar(100) comment '编码',
   f_phone              varchar(100) comment '电话',
   f_fax                varchar(100) comment '传真',
   f_address            varchar(255) comment '地址',
   f_tree_number        varchar(100) not null default '0000' comment '树编码',
   f_tree_level         int not null default 0 comment '树级别',
   f_tree_max           varchar(10) not null default '0000' comment '树子节点最大编码',
   primary key (f_org_id)
)
engine = innodb;

alter table cms_org comment '组织表';

/*==============================================================*/
/* Table: cms_publish_point                                     */
/*==============================================================*/
create table cms_publish_point
(
   f_publishpoint_id    int not null,
   f_global_id          int not null,
   f_name               varchar(100) not null comment '名称',
   f_description        varchar(255) comment '描述',
   f_store_path         varchar(255) comment '保存路径',
   f_display_path       varchar(255) comment '显示路径',
   f_ftp_hostname       varchar(100) comment 'ftp服务器',
   f_ftp_port           int comment 'ftp端口',
   f_ftp_username       varchar(100) comment 'ftp用户名',
   f_ftp_password       varchar(100) comment 'ftp密码',
   f_seq                int not null default 2147483647 comment '排列顺序',
   f_method             int not null default 1 comment '方式(1:文件系统,2:FTP)',
   f_type               int not null default 1 comment '类型(1:HTML发布,2:附件发布)',
   primary key (f_publishpoint_id)
)
engine = innodb;

alter table cms_publish_point comment '发布点表';

/*==============================================================*/
/* Table: cms_question                                          */
/*==============================================================*/
create table cms_question
(
   f_question_id        int not null,
   f_site_id            int not null comment '站点ID',
   f_title              varchar(150) not null comment '标题',
   f_description        varchar(255) comment '描述',
   f_begin_date         datetime comment '开始日期',
   f_end_date           datetime comment '结束日期',
   f_creation_date      datetime not null comment '创建日期',
   f_mode               int not null default 1 comment '模式(1:独立访客,2:独立IP,3:独立用户)',
   f_interval           int not null default 0 comment '间隔时间（天）',
   f_total              int not null default 0 comment '总票数',
   f_status             int not null default 0 comment '状态(0:启用,1:禁用)',
   primary key (f_question_id)
)
engine = innodb;

alter table cms_question comment '调查问卷表';

/*==============================================================*/
/* Table: cms_question_item                                     */
/*==============================================================*/
create table cms_question_item
(
   f_questionitem_id    int not null,
   f_question_id        int not null,
   f_title              varchar(150) not null,
   f_max_selected       int not null default 1 comment '最多可选几项(0不限制)',
   f_seq                int not null default 2147483647 comment '排序',
   f_is_essay           char(1) not null default '0' comment '是否问答题',
   primary key (f_questionitem_id)
)
engine = innodb;

alter table cms_question_item comment '调查问卷项表';

/*==============================================================*/
/* Table: cms_question_item_rec                                 */
/*==============================================================*/
create table cms_question_item_rec
(
   f_questionrecord_id  int not null,
   f_questionitem_id    int not null,
   f_answer             mediumtext comment '回答'
)
engine = innodb;

alter table cms_question_item_rec comment '调查问卷项与调查问卷记录关联表';

/*==============================================================*/
/* Table: cms_question_opt_rec                                  */
/*==============================================================*/
create table cms_question_opt_rec
(
   f_questionrecord_id  int not null,
   f_questionoption_id  int not null
)
engine = innodb;

alter table cms_question_opt_rec comment '调查问卷选项与调查问卷记录关联表';

/*==============================================================*/
/* Table: cms_question_option                                   */
/*==============================================================*/
create table cms_question_option
(
   f_questionoption_id  int not null,
   f_questionitem_id    int not null,
   f_title              varchar(150) comment '标题',
   f_is_input           char(1) not null default '0' comment '是否输入框',
   f_count              int not null default 0 comment '得票数',
   f_seq                int not null default 2147483647 comment '排序',
   primary key (f_questionoption_id)
)
engine = innodb;

alter table cms_question_option comment '调查问卷选项表';

/*==============================================================*/
/* Table: cms_question_record                                   */
/*==============================================================*/
create table cms_question_record
(
   f_questionrecord_id  int not null,
   f_user_id            int comment '用户ID',
   f_question_id        int not null comment '调查问卷ID',
   f_date               datetime not null comment '日期',
   f_ip                 varchar(100) not null comment 'IP',
   f_cookie             varchar(100) not null comment 'Cookie',
   primary key (f_questionrecord_id)
)
engine = innodb;

alter table cms_question_record comment '调查问卷记录表';

/*==============================================================*/
/* Table: cms_role                                              */
/*==============================================================*/
create table cms_role
(
   f_role_id            int not null,
   f_site_id            int not null comment '站点',
   f_name               varchar(100) not null comment '名称',
   f_description        varchar(255) comment '描述',
   f_rank               int not null default 999 comment '等级',
   f_seq                int not null default 2147483647 comment '排序',
   f_perms              mediumtext comment '功能权限',
   f_is_all_perm        char(1) not null default '1' comment '是否拥有所有功能权限',
   f_is_all_info_perm   char(1) not null default '1' comment '是否拥有所有文档权限',
   f_is_all_node_perm   char(1) not null default '1' comment '是否拥有所有栏目权限',
   f_is_info_final_perm char(1) not null default '0' comment '是否拥有文档终审权限',
   f_info_perm_type     int not null default 1 comment '文档权限类型(1:所有;2:组织;3:自身)',
   primary key (f_role_id)
)
engine = innodb;

alter table cms_role comment '角色表';

/*==============================================================*/
/* Table: cms_schedule_job                                      */
/*==============================================================*/
create table cms_schedule_job
(
   f_schedulejob_id     int not null,
   f_site_id            int not null,
   f_user_id            int not null,
   f_name               varchar(100) not null comment '任务名称',
   f_group              varchar(100) comment '任务组',
   f_code               varchar(100) not null comment '任务代码',
   f_data               mediumtext comment '任务数据',
   f_description        varchar(255) comment '任务描述',
   f_cron_expression    varchar(100) comment 'Cron表达式',
   f_start_time         datetime comment '开始时间',
   f_end_time           datetime comment '结束时间',
   f_start_delay        bigint comment '首次延迟时间(分钟)',
   f_repeat_interval    bigint comment '间隔时间',
   f_unit               int comment '时间单位(1:毫秒,2:秒,3:分,4:时,5:天,6:周,7:月,8:年)',
   f_cycle              int not null default 1 comment '执行周期(1:cron,2:simple)',
   f_status             int not null default 0 comment '状态(0:启用;1:禁用)',
   primary key (f_schedulejob_id)
)
engine = innodb;

alter table cms_schedule_job comment '定时任务表';

/*==============================================================*/
/* Table: cms_score_board                                       */
/*==============================================================*/
create table cms_score_board
(
   f_scoreboard_id      int not null,
   f_scoreitem_id       int not null comment '记分项',
   f_ftype              varchar(50) not null comment '外表标识',
   f_fid                int not null comment '外表ID',
   f_votes              int not null default 0 comment '投票次数',
   primary key (f_scoreboard_id)
)
engine = innodb;

alter table cms_score_board comment '计分牌表';

/*==============================================================*/
/* Table: cms_score_group                                       */
/*==============================================================*/
create table cms_score_group
(
   f_scoregroup_id      int not null,
   f_site_id            int not null comment '站点',
   f_name               varchar(100) not null comment '名称',
   f_number             varchar(100) comment '代码',
   f_description        varchar(255) comment '描述',
   f_seq                int not null default 2147483647 comment '排序',
   primary key (f_scoregroup_id)
)
engine = innodb;

alter table cms_score_group comment '计分组表';

/*==============================================================*/
/* Table: cms_score_item                                        */
/*==============================================================*/
create table cms_score_item
(
   f_scoreitem_id       int not null,
   f_scoregroup_id      int not null comment '计分组',
   f_site_id            int not null comment '站点',
   f_name               varchar(100) not null comment '名称',
   f_score              int not null default 1 comment '分值',
   f_icon               varchar(255) comment '图标',
   f_seq                int not null default 2147483647 comment '排序',
   primary key (f_scoreitem_id)
)
engine = innodb;

alter table cms_score_item comment '计分项表';

/*==============================================================*/
/* Table: cms_sensitive_word                                    */
/*==============================================================*/
create table cms_sensitive_word
(
   f_sensitiveword_id   int not null,
   f_name               varchar(100) not null comment '敏感词',
   f_replacement        varchar(100) comment '替换词',
   f_status             int not null default 0 comment '状态(0:启用,1:禁用)',
   primary key (f_sensitiveword_id)
)
engine = innodb;

alter table cms_sensitive_word comment '敏感词表';

/*==============================================================*/
/* Table: cms_site                                              */
/*==============================================================*/
create table cms_site
(
   f_site_id            int not null,
   f_global_id          int not null comment '全局',
   f_org_id             int not null comment '组织',
   f_html_publishpoint_id int not null comment 'HTML发布点',
   f_mobile_publishpoint_id int comment '手机端发布点',
   f_parent_id          int comment '上级站点',
   f_name               varchar(100) not null comment '名称',
   f_number             varchar(100) not null comment '代码',
   f_full_name          varchar(100) comment '完整名称',
   f_no_picture         varchar(255) not null default '/img/no_picture.jpg' comment '暂无图片',
   f_template_theme     varchar(100) not null default 'default' comment '模板主题',
   f_domain             varchar(100) not null default 'localhost' comment '域名',
   f_is_identify_domain char(1) not null default '0' comment '是否识别域名',
   f_is_static_home     char(1) not null default '0' comment '是否静态首页',
   f_mobile_theme       varchar(100) default 'default' comment '手机端模板主题',
   f_mobile_domain      varchar(100) comment '手机端域名',
   f_status             int not null default 0 comment '状态(0:正常,1:禁用)',
   f_tree_number        varchar(100) not null default '0000' comment '树编码',
   f_tree_level         int not null default 0 comment '树级别',
   f_tree_max           varchar(10) not null default '0000' comment '树子节点最大编码',
   primary key (f_site_id),
   unique key ak_number (f_number)
)
engine = innodb;

alter table cms_site comment '站点表';

/*==============================================================*/
/* Table: cms_site_clob                                         */
/*==============================================================*/
create table cms_site_clob
(
   f_site_id            int not null,
   f_key                varchar(50) not null comment '键',
   f_value              mediumtext comment '值'
)
engine = innodb;

alter table cms_site_clob comment '站点大字段表';

/*==============================================================*/
/* Table: cms_site_custom                                       */
/*==============================================================*/
create table cms_site_custom
(
   f_site_id            int not null,
   f_key                varchar(50) not null comment '键',
   f_value              varchar(2000) comment '值'
)
engine = innodb;

alter table cms_site_custom comment '站点自定义表';

/*==============================================================*/
/* Table: cms_special                                           */
/*==============================================================*/
create table cms_special
(
   f_special_id         int not null,
   f_creator_id         int not null comment '创建者',
   f_model_id           int not null comment '模型',
   f_site_id            int not null comment '站点',
   f_speccate_id        int not null comment '专题类别',
   f_creation_date      datetime not null comment '创建日期',
   f_title              varchar(150) not null comment '标题',
   f_meta_keywords      varchar(150) comment '关键字',
   f_meta_description   varchar(450) comment '描述',
   f_special_template   varchar(255) comment '专题模板',
   f_small_image        varchar(255) comment '小图',
   f_large_image        varchar(255) comment '大图',
   f_video              varchar(255) comment '视频',
   f_video_name         varchar(255) comment '视频名称',
   f_video_length       bigint comment '视频长度',
   f_video_time         varchar(100) comment '视频时间',
   f_refers             int not null default 0 comment '文档数量',
   f_views              int not null default 0 comment '浏览总数',
   f_is_with_image      char(1) not null default '0' comment '是否有图片',
   f_is_recommend       char(1) not null default '0' comment '是否推荐',
   primary key (f_special_id)
)
engine = innodb;

alter table cms_special comment '专题表';

/*==============================================================*/
/* Table: cms_special_category                                  */
/*==============================================================*/
create table cms_special_category
(
   f_speccate_id        int not null,
   f_site_id            int not null comment '站点',
   f_name               varchar(50) not null comment '名称',
   f_seq                int not null default 2147483647 comment '排序',
   f_views              int not null default 0 comment '浏览总数',
   f_meta_keywords      varchar(150) comment '关键字',
   f_meta_description   varchar(450) comment '描述',
   f_creation_date      datetime not null comment '创建日期',
   primary key (f_speccate_id)
)
engine = innodb;

alter table cms_special_category comment '专题类别表';

/*==============================================================*/
/* Table: cms_special_clob                                      */
/*==============================================================*/
create table cms_special_clob
(
   f_special_id         int not null,
   f_key                varchar(50) not null comment '键',
   f_value              mediumtext comment '值'
)
engine = innodb;

alter table cms_special_clob comment '专题大字段表';

/*==============================================================*/
/* Table: cms_special_custom                                    */
/*==============================================================*/
create table cms_special_custom
(
   f_special_id         int not null,
   f_key                varchar(50) comment '键',
   f_value              varchar(2000) comment '值'
)
engine = innodb;

alter table cms_special_custom comment '专题自定义表';

/*==============================================================*/
/* Table: cms_special_file                                      */
/*==============================================================*/
create table cms_special_file
(
   f_special_id         int not null,
   f_name               varchar(150) not null comment '文件名称',
   f_length             bigint not null default 0 comment '文件长度',
   f_file               varchar(255) not null comment '文件地址',
   f_index              int not null default 0 comment '文件序号',
   f_downloads          int not null default 0 comment '下载次数'
)
engine = innodb;

alter table cms_special_file comment '专题附件集表';

/*==============================================================*/
/* Table: cms_special_image                                     */
/*==============================================================*/
create table cms_special_image
(
   f_special_id         int not null,
   f_name               varchar(150) comment '图片名称',
   f_image              varchar(255) comment '图片地址',
   f_index              int not null default 0 comment '图片序号',
   f_text               mediumtext comment '图片正文'
)
engine = innodb;

alter table cms_special_image comment '专题图片集表';

/*==============================================================*/
/* Table: cms_tag                                               */
/*==============================================================*/
create table cms_tag
(
   f_tag_id             int not null,
   f_site_id            int not null comment '站点',
   f_name               varchar(150) not null comment '名称',
   f_creation_date      datetime not null comment '创建日期',
   f_refers             int not null default 0 comment '文档数量',
   primary key (f_tag_id)
)
engine = innodb;

alter table cms_tag comment 'TAG表';

/*==============================================================*/
/* Table: cms_task                                              */
/*==============================================================*/
create table cms_task
(
   f_task_id            int not null,
   f_user_id            int not null,
   f_site_id            int not null,
   f_name               varchar(150) not null comment '名称',
   f_description        mediumtext comment '描述',
   f_begin_time         datetime not null comment '开始时间',
   f_end_time           datetime comment '结束时间',
   f_total              int not null default 0 comment '总完成数量',
   f_type               int not null default 1 comment '类型(1:栏目HTML生成,2:文档HTML生成,3:全文索引生成)',
   f_status             int not null default 0 comment '状态(0:运行中,1:完成,2:中止,3:停止)',
   primary key (f_task_id)
)
engine = innodb;

alter table cms_task comment '任务表';

/*==============================================================*/
/* Table: cms_user                                              */
/*==============================================================*/
create table cms_user
(
   f_user_id            int not null,
   f_org_id             int not null comment '组织',
   f_membergroup_id     int not null comment '会员组',
   f_global_id          int not null comment '全局',
   f_username           varchar(50) not null comment '用户名',
   f_password           varchar(128) comment '密码',
   f_salt               varchar(32) comment '加密混淆码',
   f_email              varchar(100) comment '电子邮箱',
   f_mobile             varchar(100) comment '手机',
   f_real_name          varchar(100) comment '用户实名',
   f_gender             char(1) comment '性别',
   f_birth_date         datetime comment '出生年月',
   f_validation_type    varchar(50) comment '验证类型(用户激活,重置密码,邮箱激活)',
   f_validation_key     varchar(100) comment '验证KEY',
   f_rank               int not null default 99999 comment '等级',
   f_type               int not null default 0 comment '类型(0:会员,1:管理员)',
   f_status             int not null default 0 comment '状态(0:正常,1:锁定,2:待验证)',
   f_qq_openid          varchar(64) comment 'qq openid',
   f_weibo_uid          varchar(64) comment 'weibo uid',
   f_weixin_openid      varchar(64) comment 'weixin openid',
   primary key (f_user_id),
   unique key ak_username (f_username)
)
engine = innodb;

alter table cms_user comment '用户表';

/*==============================================================*/
/* Table: cms_user_clob                                         */
/*==============================================================*/
create table cms_user_clob
(
   f_user_id            int,
   f_key                varchar(50) not null comment '键',
   f_value              mediumtext comment '值'
)
engine = innodb;

alter table cms_user_clob comment '用户大字段表';

/*==============================================================*/
/* Table: cms_user_custom                                       */
/*==============================================================*/
create table cms_user_custom
(
   f_user_id            int,
   f_key                varchar(50) not null comment '键',
   f_value              varchar(2000) comment '值'
)
engine = innodb;

alter table cms_user_custom comment '用户自定义表';

/*==============================================================*/
/* Table: cms_user_detail                                       */
/*==============================================================*/
create table cms_user_detail
(
   f_user_id            int not null,
   f_validation_date    datetime comment '验证生成时间',
   f_login_error_date   datetime comment '登录错误时间',
   f_login_error_count  int not null default 0 comment '登录错误次数',
   f_prev_login_date    datetime comment '上次登录日期',
   f_prev_login_ip      varchar(100) comment '上次登录IP',
   f_last_login_date    datetime comment '最后登录日期',
   f_last_login_ip      varchar(100) comment '最后登录IP',
   f_creation_date      datetime not null comment '加入日期',
   f_creation_ip        varchar(100) not null comment '加入IP',
   f_logins             int not null default 0 comment '登录次数',
   f_is_with_avatar     char(1) not null default '0' comment '是否有头像',
   f_bio                varchar(255) comment '自我介绍',
   f_come_from          varchar(100) comment '来自',
   f_qq                 varchar(100) comment 'QQ',
   f_msn                varchar(100) comment 'MSN',
   f_weixin             varchar(100) comment '微信',
   primary key (f_user_id)
)
engine = innodb;

alter table cms_user_detail comment '用户详细信息表';

/*==============================================================*/
/* Table: cms_user_membergroup                                  */
/*==============================================================*/
create table cms_user_membergroup
(
   f_user_id            int not null,
   f_membergroup_id     int not null,
   f_group_index        int default 0 comment '会员组排列顺序'
)
engine = innodb;

alter table cms_user_membergroup comment '用户与会员组关联表';

/*==============================================================*/
/* Table: cms_user_org                                          */
/*==============================================================*/
create table cms_user_org
(
   f_user_id            int not null,
   f_org_id             int not null,
   f_org_index          int default 0 comment '组织顺序'
)
engine = innodb;

alter table cms_user_org comment '用户与组织关联表';

/*==============================================================*/
/* Table: cms_user_role                                         */
/*==============================================================*/
create table cms_user_role
(
   f_user_id            int not null,
   f_role_id            int not null,
   f_role_index         int default 0 comment '角色顺序'
)
engine = innodb;

alter table cms_user_role comment '用户与角色关联表';

/*==============================================================*/
/* Table: cms_visit_log                                         */
/*==============================================================*/
create table cms_visit_log
(
   f_visitlog_id        int not null,
   f_site_id            int not null,
   f_user_id            int comment '访问用户',
   f_url                varchar(255) not null comment '页面URL',
   f_referrer           varchar(255) comment '来源URL',
   f_source             varchar(100) comment '来源域名',
   f_ip                 varchar(100) comment 'IP地址',
   f_cookie             varchar(100) comment 'COOKIE值',
   f_user_agent         varchar(450) comment '用户代理',
   f_browser            varchar(100) comment '浏览器',
   f_os                 varchar(100) comment '操作系统',
   f_device             varchar(100) comment '设备(COMPUTER,MOBILE,TABLET,等)',
   f_country            varchar(100) comment '国家',
   f_area               varchar(100) comment '地区',
   f_time_string        char(14) not null comment '访问时间（字符串格式yyyyMMddHHmmss）',
   f_time               datetime not null comment '访问时间',
   primary key (f_visitlog_id)
)
engine = innodb;

alter table cms_visit_log comment '访问日志表';

/*==============================================================*/
/* Table: cms_vote                                              */
/*==============================================================*/
create table cms_vote
(
   f_vote_id            int not null,
   f_site_id            int not null,
   f_title              varchar(150) not null comment '标题',
   f_number             varchar(100) comment '代码',
   f_description        varchar(255) comment '描述',
   f_creation_date      datetime not null comment '创建时间',
   f_begin_date         datetime comment '开始日期',
   f_end_date           datetime comment '结束日期',
   f_interval           int not null default 0 comment '间隔时间（天）',
   f_max_selected       int not null default 1 comment '最多可选几项(0不限制)',
   f_mode               int not null default 1 comment '模式(1:独立访客,2:独立IP,3:独立用户)',
   f_total              int not null default 0 comment '总票数',
   f_status             int not null default 0 comment '状态(0:启用,1:禁用)',
   primary key (f_vote_id)
)
engine = innodb;

alter table cms_vote comment '投票表';

/*==============================================================*/
/* Table: cms_vote_mark                                         */
/*==============================================================*/
create table cms_vote_mark
(
   f_votemark_id        int not null,
   f_ftype              varchar(50) not null comment '外表标识',
   f_fid                int not null comment '外表ID',
   f_date               datetime not null comment '日期',
   f_user_id            int comment '用户',
   f_ip                 varchar(100) not null comment 'IP',
   f_cookie             varchar(100) not null comment 'Cookie',
   primary key (f_votemark_id)
)
engine = innodb;

alter table cms_vote_mark comment '投票标记表';

/*==============================================================*/
/* Table: cms_vote_option                                       */
/*==============================================================*/
create table cms_vote_option
(
   f_voteoption_id      int not null,
   f_vote_id            int not null,
   f_title              varchar(150) not null comment '标题',
   f_count              int not null default 0 comment '得票数',
   f_seq                int not null default 2147483647 comment '排序',
   primary key (f_voteoption_id)
)
engine = innodb;

alter table cms_vote_option comment '投票项表';

/*==============================================================*/
/* Table: cms_workflow                                          */
/*==============================================================*/
create table cms_workflow
(
   f_workflow_id        int not null,
   f_workflowgroup_id   int not null comment '工作流',
   f_site_id            int not null comment '站点',
   f_name               varchar(100) not null comment '名称',
   f_description        varchar(255) comment '描述',
   f_seq                int not null default 2147483647 comment '排序',
   f_status             int not null default 1 comment '状态(1:启用;2:禁用)',
   primary key (f_workflow_id)
)
engine = innodb;

alter table cms_workflow comment '工作流表';

/*==============================================================*/
/* Table: cms_workflow_group                                    */
/*==============================================================*/
create table cms_workflow_group
(
   f_workflowgroup_id   int not null,
   f_site_id            int not null comment '站点',
   f_name               varchar(100) not null comment '名称',
   f_description        varchar(255) comment '描述',
   f_seq                int not null default 2147483647 comment '排序',
   primary key (f_workflowgroup_id)
)
engine = innodb;

alter table cms_workflow_group comment '工作流组表';

/*==============================================================*/
/* Table: cms_workflow_log                                      */
/*==============================================================*/
create table cms_workflow_log
(
   f_workflowlog_id     int not null,
   f_user_id            int not null comment '操作人',
   f_site_id            int not null comment '站点',
   f_workflowprocess_id int not null comment '过程',
   f_from               varchar(100) not null comment '从哪',
   f_to                 varchar(100) not null comment '到哪',
   f_creation_date      datetime not null comment '创建时间',
   f_opinion            varchar(255) comment '意见',
   f_type               int not null comment '类型(1:前进;2后退:;3:原地)',
   primary key (f_workflowlog_id)
)
engine = innodb;

alter table cms_workflow_log comment '工作流流程日志表';

/*==============================================================*/
/* Table: cms_workflow_process                                  */
/*==============================================================*/
create table cms_workflow_process
(
   f_workflowprocess_id int not null,
   f_workflowstep_id    int comment '步骤',
   f_site_id            int not null comment '站点',
   f_workflow_id        int not null comment '流程',
   f_user_id            int not null comment '发起人',
   f_data_id            int not null comment '数据ID',
   f_data_type          int not null comment '数据类型(1:文档)',
   f_begin_date         datetime not null comment '开始时间',
   f_end_date           datetime comment '结束时间',
   f_is_rejection       char(1) not null default '0' comment '是否退回',
   f_is_end             char(1) not null default '0' comment '是否结束',
   primary key (f_workflowprocess_id)
)
engine = innodb;

alter table cms_workflow_process comment '工作流过程表';

/*==============================================================*/
/* Table: cms_workflow_step                                     */
/*==============================================================*/
create table cms_workflow_step
(
   f_workflowstep_id    int not null,
   f_workflow_id        int not null comment '工作流',
   f_name               varchar(100) not null comment '名称',
   f_seq                int not null default 2147483647 comment '排序',
   primary key (f_workflowstep_id)
)
engine = innodb;

alter table cms_workflow_step comment '工作流步骤表';

/*==============================================================*/
/* Table: cms_workflowstep_role                                 */
/*==============================================================*/
create table cms_workflowstep_role
(
   f_role_id            int not null,
   f_workflowstep_id    int not null,
   f_role_index         int default 0 comment '角色排列顺序'
)
engine = innodb;

alter table cms_workflowstep_role comment '审核步骤与角色关联表';

/*==============================================================*/
/* Table: hibernate_sequences                                   */
/*==============================================================*/
create table hibernate_sequences
(
   sequence_name        varchar(100) not null comment '表名',
   next_val             bigint not null comment 'ID值',
   primary key (sequence_name)
)
engine = innodb;

alter table hibernate_sequences comment '主键表';

/*==============================================================*/
/* Table: plug_resume                                           */
/*==============================================================*/
create table plug_resume
(
   f_resume_id          int not null,
   f_site_id            int not null,
   f_name               varchar(100) not null comment '姓名',
   f_post               varchar(100) not null comment '应聘职位',
   f_creation_date      datetime not null comment '投递日期',
   f_gender             char(1) not null default 'M' comment '性别',
   f_birth_date         datetime comment '出生日期',
   f_mobile             varchar(100) comment '手机',
   f_email              varchar(100) comment '邮箱',
   f_expected_salary    int comment '期望薪水',
   f_education_experience mediumtext comment '教育经历',
   f_work_experience    mediumtext comment '工作经历',
   f_remark             mediumtext comment '备注',
   primary key (f_resume_id)
)
engine = innodb;

alter table plug_resume comment '简历表';
/* quartz-2.2.2 */

DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;

CREATE TABLE QRTZ_JOB_DETAILS(
SCHED_NAME VARCHAR(60) NOT NULL,
JOB_NAME VARCHAR(80) NOT NULL,
JOB_GROUP VARCHAR(80) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
JOB_CLASS_NAME VARCHAR(250) NOT NULL,
IS_DURABLE VARCHAR(1) NOT NULL,
IS_NONCONCURRENT VARCHAR(1) NOT NULL,
IS_UPDATE_DATA VARCHAR(1) NOT NULL,
REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_TRIGGERS (
SCHED_NAME VARCHAR(60) NOT NULL,
TRIGGER_NAME VARCHAR(80) NOT NULL,
TRIGGER_GROUP VARCHAR(80) NOT NULL,
JOB_NAME VARCHAR(80) NOT NULL,
JOB_GROUP VARCHAR(80) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
NEXT_FIRE_TIME BIGINT(13) NULL,
PREV_FIRE_TIME BIGINT(13) NULL,
PRIORITY INTEGER NULL,
TRIGGER_STATE VARCHAR(16) NOT NULL,
TRIGGER_TYPE VARCHAR(8) NOT NULL,
START_TIME BIGINT(13) NOT NULL,
END_TIME BIGINT(13) NULL,
CALENDAR_NAME VARCHAR(80) NULL,
MISFIRE_INSTR SMALLINT(2) NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_SIMPLE_TRIGGERS (
SCHED_NAME VARCHAR(60) NOT NULL,
TRIGGER_NAME VARCHAR(80) NOT NULL,
TRIGGER_GROUP VARCHAR(80) NOT NULL,
REPEAT_COUNT BIGINT(7) NOT NULL,
REPEAT_INTERVAL BIGINT(12) NOT NULL,
TIMES_TRIGGERED BIGINT(10) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_CRON_TRIGGERS (
SCHED_NAME VARCHAR(60) NOT NULL,
TRIGGER_NAME VARCHAR(80) NOT NULL,
TRIGGER_GROUP VARCHAR(80) NOT NULL,
CRON_EXPRESSION VARCHAR(120) NOT NULL,
TIME_ZONE_ID VARCHAR(80),
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
  (          
    SCHED_NAME VARCHAR(60) NOT NULL,
    TRIGGER_NAME VARCHAR(80) NOT NULL,
    TRIGGER_GROUP VARCHAR(80) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_BLOB_TRIGGERS (
SCHED_NAME VARCHAR(60) NOT NULL,
TRIGGER_NAME VARCHAR(80) NOT NULL,
TRIGGER_GROUP VARCHAR(80) NOT NULL,
BLOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
INDEX (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_CALENDARS (
SCHED_NAME VARCHAR(60) NOT NULL,
CALENDAR_NAME VARCHAR(80) NOT NULL,
CALENDAR BLOB NOT NULL,
PRIMARY KEY (SCHED_NAME,CALENDAR_NAME))
ENGINE=InnoDB;

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (
SCHED_NAME VARCHAR(60) NOT NULL,
TRIGGER_GROUP VARCHAR(80) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_FIRED_TRIGGERS (
SCHED_NAME VARCHAR(60) NOT NULL,
ENTRY_ID VARCHAR(95) NOT NULL,
TRIGGER_NAME VARCHAR(80) NOT NULL,
TRIGGER_GROUP VARCHAR(80) NOT NULL,
INSTANCE_NAME VARCHAR(80) NOT NULL,
FIRED_TIME BIGINT(13) NOT NULL,
SCHED_TIME BIGINT(13) NOT NULL,
PRIORITY INTEGER NOT NULL,
STATE VARCHAR(16) NOT NULL,
JOB_NAME VARCHAR(80) NULL,
JOB_GROUP VARCHAR(80) NULL,
IS_NONCONCURRENT VARCHAR(1) NULL,
REQUESTS_RECOVERY VARCHAR(1) NULL,
PRIMARY KEY (SCHED_NAME,ENTRY_ID))
ENGINE=InnoDB;

CREATE TABLE QRTZ_SCHEDULER_STATE (
SCHED_NAME VARCHAR(60) NOT NULL,
INSTANCE_NAME VARCHAR(80) NOT NULL,
LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
CHECKIN_INTERVAL BIGINT(13) NOT NULL,
PRIMARY KEY (SCHED_NAME,INSTANCE_NAME))
ENGINE=InnoDB;

CREATE TABLE QRTZ_LOCKS (
SCHED_NAME VARCHAR(60) NOT NULL,
LOCK_NAME VARCHAR(40) NOT NULL,
PRIMARY KEY (SCHED_NAME,LOCK_NAME))
ENGINE=InnoDB;

CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY ON QRTZ_JOB_DETAILS(SCHED_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_J_GRP ON QRTZ_JOB_DETAILS(SCHED_NAME,JOB_GROUP);

CREATE INDEX IDX_QRTZ_T_J ON QRTZ_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG ON QRTZ_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_C ON QRTZ_TRIGGERS(SCHED_NAME,CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME ON QRTZ_TRIGGERS(SCHED_NAME,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_J_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_JG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_T_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);

INSERT INTO cms_ad (f_ad_id, f_site_id, f_adslot_id, f_name, f_begin_date, f_end_date, f_url, f_text, f_script, f_image, f_flash, f_seq) VALUES (2,1,1,'jspxcms演示广告',NULL,NULL,'http://www.jspxcms.com/','jspxcms演示广告',NULL,'/template/1/default/_files/img/dd.jpg',NULL,50);
INSERT INTO cms_ad_slot (f_adslot_id, f_site_id, f_name, f_number, f_description, f_type, f_template, f_width, f_height) VALUES (1,1,'首页广告','homepage',NULL,2,'/sys_ad/ad_homepage.html',1000,80);
INSERT INTO cms_attribute (f_attribute_id, f_site_id, f_number, f_name, f_seq, f_is_with_image, f_image_width, f_image_height, f_is_exact, f_is_scale, f_is_watermark) VALUES (2,1,'headlines','头条',6,'0',NULL,NULL,'0','0','0');
INSERT INTO cms_attribute (f_attribute_id, f_site_id, f_number, f_name, f_seq, f_is_with_image, f_image_width, f_image_height, f_is_exact, f_is_scale, f_is_watermark) VALUES (5,1,'focusnews','新闻焦点',1,'1',660,250,'0','0','0');
INSERT INTO cms_attribute (f_attribute_id, f_site_id, f_number, f_name, f_seq, f_is_with_image, f_image_width, f_image_height, f_is_exact, f_is_scale, f_is_watermark) VALUES (6,1,'focusphoto','图片焦点',2,'1',1000,500,'0','0','0');
INSERT INTO cms_attribute (f_attribute_id, f_site_id, f_number, f_name, f_seq, f_is_with_image, f_image_width, f_image_height, f_is_exact, f_is_scale, f_is_watermark) VALUES (7,1,'focusvideo','视频焦点',3,'1',1000,410,'0','0','0');
INSERT INTO cms_attribute (f_attribute_id, f_site_id, f_number, f_name, f_seq, f_is_with_image, f_image_width, f_image_height, f_is_exact, f_is_scale, f_is_watermark) VALUES (8,1,'focusproduct','产品焦点',4,'1',770,190,'0','0','0');
INSERT INTO cms_attribute (f_attribute_id, f_site_id, f_number, f_name, f_seq, f_is_with_image, f_image_width, f_image_height, f_is_exact, f_is_scale, f_is_watermark) VALUES (9,1,'focusdownload','下载焦点',5,'1',310,210,'0','0','0');
INSERT INTO cms_attribute (f_attribute_id, f_site_id, f_number, f_name, f_seq, f_is_with_image, f_image_width, f_image_height, f_is_exact, f_is_scale, f_is_watermark) VALUES (10,1,'focusmobile','手机焦点',7,'1',640,330,'0','0','0');
INSERT INTO cms_attribute (f_attribute_id, f_site_id, f_number, f_name, f_seq, f_is_with_image, f_image_width, f_image_height, f_is_exact, f_is_scale, f_is_watermark) VALUES (12,1,'focus','焦点',0,'1',318,212,'0','0','0');
INSERT INTO cms_collect (f_collect_id, f_node_id, f_site_id, f_user_id, f_name, f_charset, f_list_pattern, f_page_begin, f_page_end, f_is_desc, f_item_area_pattern, f_item_pattern, f_status, f_interval_min, f_interval_max, f_list_next_pattern, f_is_list_next_reg, f_is_item_area_reg, f_is_item_reg, f_block_area_pattern, f_is_block_area_reg, f_block_pattern, f_is_block_reg, f_user_agent, f_is_submit, f_is_allow_duplicate, f_is_download_image) VALUES (1,42,1,1,'新浪新闻','UTF-8','http://roll.news.sina.com.cn/news/gnxw/gdxw1/index.shtml\r\nhttp://roll.news.sina.com.cn/news/gnxw/gdxw1/index_(*).shtml',2,10,'1','<ul class=\"list_009\">(*)</ul>','<li><a href=\"(*)\" target=\"_blank\">',0,0,0,NULL,'0','0','0',NULL,'0',NULL,'0','Mozilla/5.0','0','0','1');
INSERT INTO cms_collect_field (f_collectfield_id, f_collect_id, f_site_id, f_name, f_code, f_type, f_source_type, f_source_url, f_source_text, f_data_area_pattern, f_is_data_area_reg, f_data_pattern, f_is_data_reg, f_date_format, f_download_type, f_image_param, f_filter, f_seq) VALUES (2,1,1,'标题','title',1,1,NULL,NULL,NULL,'0','<meta property=\"og:title\" content=\"(*)\" />','0',NULL,NULL,NULL,NULL,2147483647);
INSERT INTO cms_collect_field (f_collectfield_id, f_collect_id, f_site_id, f_name, f_code, f_type, f_source_type, f_source_url, f_source_text, f_data_area_pattern, f_is_data_area_reg, f_data_pattern, f_is_data_reg, f_date_format, f_download_type, f_image_param, f_filter, f_seq) VALUES (3,1,1,'正文','text',1,1,NULL,NULL,NULL,'0','<div class=\"article article_16\" id=\"artibody\">\r\n(*)\r\n			\r\n        </div>','0',NULL,NULL,NULL,NULL,2147483647);
INSERT INTO cms_collect_field (f_collectfield_id, f_collect_id, f_site_id, f_name, f_code, f_type, f_source_type, f_source_url, f_source_text, f_data_area_pattern, f_is_data_area_reg, f_data_pattern, f_is_data_reg, f_date_format, f_download_type, f_image_param, f_filter, f_seq) VALUES (4,1,1,'发布时间','publishDate',1,1,NULL,NULL,NULL,'0','<span class=\"time-source\" id=\"navtimeSource\">(*)		<span>','0','yyyy年MM月dd日HH:mm',NULL,NULL,NULL,2147483647);
INSERT INTO cms_comment (f_comment_id, f_site_id, f_creator_id, f_auditor_id, f_ftype, f_fid, f_creation_date, f_audit_date, f_ip, f_score, f_status, f_text, f_parent_id, f_country, f_area) VALUES (16,1,1,NULL,'Info',28,'2014-02-22 12:40:48',NULL,'0:0:0:0:0:0:0:1',0,1,'很好看。我很喜欢。',NULL,NULL,NULL);
INSERT INTO cms_comment (f_comment_id, f_site_id, f_creator_id, f_auditor_id, f_ftype, f_fid, f_creation_date, f_audit_date, f_ip, f_score, f_status, f_text, f_parent_id, f_country, f_area) VALUES (17,1,1,NULL,'Info',35,'2014-04-04 20:35:51',NULL,'0:0:0:0:0:0:0:1',0,1,'现在除了情人节、七夕节等传统的结婚登记吉日，5.20、9.9等谐音吉日也越来越受到年轻人的追捧。',NULL,NULL,NULL);
INSERT INTO cms_comment (f_comment_id, f_site_id, f_creator_id, f_auditor_id, f_ftype, f_fid, f_creation_date, f_audit_date, f_ip, f_score, f_status, f_text, f_parent_id, f_country, f_area) VALUES (18,1,0,NULL,'Info',100,'2014-04-04 20:36:28',NULL,'0:0:0:0:0:0:0:1',0,1,'西班牙正考虑将直布罗陀争议诉至联合国与海牙国际法庭等国际机构，并考虑与阿根廷结成统一阵线',NULL,NULL,NULL);
INSERT INTO cms_comment (f_comment_id, f_site_id, f_creator_id, f_auditor_id, f_ftype, f_fid, f_creation_date, f_audit_date, f_ip, f_score, f_status, f_text, f_parent_id, f_country, f_area) VALUES (19,1,0,NULL,'Info',73,'2014-04-04 20:40:51',NULL,'0:0:0:0:0:0:0:1',0,1,'生活中不仅有幸福和快乐，更有悲伤和无奈，都要笑纳，因为它们都是生活的组成部分。',NULL,NULL,NULL);
INSERT INTO cms_comment (f_comment_id, f_site_id, f_creator_id, f_auditor_id, f_ftype, f_fid, f_creation_date, f_audit_date, f_ip, f_score, f_status, f_text, f_parent_id, f_country, f_area) VALUES (20,1,0,NULL,'Info',26,'2014-04-04 20:41:28',NULL,'0:0:0:0:0:0:0:1',0,1,'请市民减少出行，注意交通安全，山区防山洪、滑坡、泥石流地质灾害',NULL,NULL,NULL);
INSERT INTO cms_friendlink (f_friendlink_id, f_friendlinktype_id, f_site_id, f_name, f_url, f_seq, f_logo, f_description, f_email, f_is_with_logo, f_is_recommend, f_status) VALUES (7,1,1,'JSPXCMS官方','http://www.jspxcms.com/',2147483647,NULL,NULL,NULL,'0','0',0);
INSERT INTO cms_friendlink (f_friendlink_id, f_friendlinktype_id, f_site_id, f_name, f_url, f_seq, f_logo, f_description, f_email, f_is_with_logo, f_is_recommend, f_status) VALUES (8,3,1,'JAVA','http://www.java.com/',2147483647,NULL,NULL,NULL,'0','0',0);
INSERT INTO cms_friendlink (f_friendlink_id, f_friendlinktype_id, f_site_id, f_name, f_url, f_seq, f_logo, f_description, f_email, f_is_with_logo, f_is_recommend, f_status) VALUES (9,3,1,'TOMCAT','http://tomcat.apache.org/',2147483647,NULL,NULL,NULL,'0','0',0);
INSERT INTO cms_friendlink_type (f_friendlinktype_id, f_site_id, f_name, f_number, f_seq) VALUES (1,1,'综合类','zonghe',0);
INSERT INTO cms_friendlink_type (f_friendlinktype_id, f_site_id, f_name, f_number, f_seq) VALUES (3,1,'技术类','yule',1);
INSERT INTO cms_global (f_global_id, f_protocol, f_port, f_context_path, f_version, f_uploads_publishpoint_id, f_captcha_errors) VALUES (1,'http',8080,NULL,'8.5.0',1,3);
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'poweredby',' - Powered by Jspxcms');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_mail_smtpHost','smtp.126.com');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_mail_smtpPassword','123');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_mail_smtpAuth','true');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_mail_from','myemail@126.com');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_mail_smtpUsername','myemail@126.com');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_mail_smtpSsl','false');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_register_validCharacter','^[0-9a-zA-Z\\u4e00-\\u9fa5\\.\\-@_]+$');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_register_passwordEmailSubject','找回密码通知');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_register_verifyMode','0');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_register_groupId','1');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_register_minLength','3');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_register_passwordEmailText','亲爱的${username}:\r\n  请复制以下链接到浏览器中打开，进入密码修改页面。\r\n  ${url}\r\n\r\n${sitename}');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_register_verifyEmailSubject','帐号激活邮件');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_register_mode','1');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_register_verifyEmailText','亲爱的${username}:\r\n  请复制以下链接到浏览器中打开，以便激活您的帐号。\r\n  ${url}\r\n\r\n${sitename}');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_register_orgId','1');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_register_maxLength','15');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_register_userAgreement','    1、在本站注册的会员，必须遵守《互联网电子公告服务管理规定》，不得在本站发表诽谤他人，侵犯他人隐私，侵犯他人知识产权，传播病毒，政治言论，商业讯息等信息。\r\n\r\n    2、在所有在本站发表的文章，本站都具有最终编辑权，并且保留用于印刷或向第三方发表的权利，如果你的资料不齐全，我们将有权不作任何通知使用你在本站发布的作品。\r\n\r\n    3、在登记过程中，您将选择注册名和密码。注册名的选择应遵守法律法规及社会公德。您必须对您的密码保密，您将对您注册名和密码下发生的所有活动承担责任。');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'oauth','2');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_register_avatarLarge','120');
INSERT INTO cms_global_custom (f_global_id, f_key, f_value) VALUES (1,'sys_register_avatarSmall','50');
INSERT INTO cms_guestbook (f_guestbook_id, f_site_id, f_guestbooktype_id, f_creator_id, f_replyer_id, f_title, f_text, f_creation_date, f_creation_ip, f_reply_text, f_reply_date, f_reply_ip, f_is_reply, f_is_recommend, f_status, f_username, f_gender, f_phone, f_mobile, f_qq, f_email, f_creation_country, f_creation_area, f_reply_country, f_reply_area) VALUES (23,1,1,1,1,'jspxcms升级了','终于等到jspxcms升级了，期待很久了。\r\n呵呵！','2013-08-12 08:54:43','0:0:0:0:0:0:0:1','非常感谢大家长久以来的支持！','2013-08-12 08:56:00','0:0:0:0:0:0:0:1','1','0',0,NULL,'1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_guestbook (f_guestbook_id, f_site_id, f_guestbooktype_id, f_creator_id, f_replyer_id, f_title, f_text, f_creation_date, f_creation_ip, f_reply_text, f_reply_date, f_reply_ip, f_is_reply, f_is_recommend, f_status, f_username, f_gender, f_phone, f_mobile, f_qq, f_email, f_creation_country, f_creation_area, f_reply_country, f_reply_area) VALUES (28,1,8,1,NULL,'功能有改进','留言功能更好用了！','2014-03-18 13:56:44','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'0','0',0,NULL,'1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_guestbook_type (f_guestbooktype_id, f_site_id, f_name, f_number, f_seq, f_description) VALUES (1,1,'咨询','zixun',1,'咨询');
INSERT INTO cms_guestbook_type (f_guestbooktype_id, f_site_id, f_name, f_number, f_seq, f_description) VALUES (4,1,'投诉','tousu',2,'投诉');
INSERT INTO cms_guestbook_type (f_guestbooktype_id, f_site_id, f_name, f_number, f_seq, f_description) VALUES (5,1,'建议','jianyi',3,'建议');
INSERT INTO cms_guestbook_type (f_guestbooktype_id, f_site_id, f_name, f_number, f_seq, f_description) VALUES (6,1,'反馈','fankui',4,'反馈');
INSERT INTO cms_guestbook_type (f_guestbooktype_id, f_site_id, f_name, f_number, f_seq, f_description) VALUES (7,1,'其他','qita',5,'其他');
INSERT INTO cms_guestbook_type (f_guestbooktype_id, f_site_id, f_name, f_number, f_seq, f_description) VALUES (8,1,'普通','putong',0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (24,1,1,1,42,'2013-03-18 01:40:28',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (25,1,1,1,42,'2013-03-18 01:43:00',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (26,1,1,1,40,'2013-08-05 17:31:32',0,'0',0,0,1,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (27,1,1,1,42,'2013-03-18 01:46:31',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (28,1,1,1,42,'2013-08-05 17:31:36',0,'0',50,0,1,2,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (30,1,1,1,44,'2013-08-05 17:31:32',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (32,1,1,1,44,'2014-03-06 07:31:32',0,'1',60,0,0,1,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (33,1,1,1,42,'2013-08-05 17:31:32',0,'1',20,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (34,1,1,1,42,'2013-08-05 17:31:32',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (35,1,1,1,42,'2013-08-14 17:31:32',0,'0',10,0,1,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (36,1,1,1,42,'2013-08-06 17:31:32',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (43,1,1,1,38,'2013-08-11 00:55:52',0,'1',10,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (44,1,1,1,38,'2013-08-11 01:02:32',0,'1',10,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (45,1,1,1,38,'2013-03-11 01:06:31',0,'1',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (46,1,1,1,38,'2013-08-11 01:09:43',0,'0',10,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (47,1,1,1,40,'2013-03-19 01:16:20',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (48,1,1,1,40,'2013-03-19 01:17:39',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (49,1,1,1,40,'2013-03-19 01:20:15',0,'1',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (50,1,1,1,40,'2013-03-19 01:23:30',0,'1',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (52,1,1,1,42,'2013-03-19 01:31:00',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (53,1,1,1,44,'2013-03-19 01:32:45',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (54,1,1,1,42,'2013-03-19 01:36:50',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (73,1,1,1,38,'2013-08-07 04:07:07',0,'1',10,0,1,10,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (78,1,1,1,88,'2014-03-06 06:51:58',0,'1',10,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (80,1,1,1,90,'2013-08-06 08:26:57',0,'1',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (81,1,1,1,90,'2014-03-06 08:36:28',0,'1',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (83,1,1,1,79,'2013-08-06 09:32:47',0,'1',0,6,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (87,1,1,1,82,'2014-12-28 02:53:12',0,'1',100,0,0,3,0,'A',0,1,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (93,1,1,1,78,'2013-08-08 07:32:47',0,'1',0,5,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (94,1,1,1,84,'2014-03-05 07:35:46',0,'1',0,0,0,0,56,'A',7,0,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (95,1,1,1,85,'2014-03-21 09:50:20',0,'1',0,0,0,0,0,'A',0,0,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (97,1,1,1,83,'2013-08-08 09:26:09',0,'1',0,0,0,0,0,'A',0,0,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (98,1,1,1,83,'2013-08-12 01:33:02',0,'1',0,0,0,0,0,'A',0,0,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (99,1,1,1,44,'2013-08-13 08:02:06',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (100,1,1,1,44,'2013-08-13 08:03:20',0,'0',0,0,1,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (117,1,1,1,69,'2014-03-18 14:36:08',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (118,1,1,1,69,'2014-03-18 14:36:40',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (126,1,1,1,42,'2014-03-23 22:11:43',0,'0',240,0,3,1,2,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (127,1,1,1,42,'2014-03-23 22:11:44',0,'0',480,0,8,1,8,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (128,1,1,1,42,'2014-03-23 22:11:40',0,'0',50,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (129,1,1,1,84,'2014-04-01 09:42:55',0,'1',30,0,0,0,0,'A',0,0,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (130,1,1,1,83,'2014-04-01 14:26:53',0,'1',10,0,0,0,0,'A',0,0,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (131,1,1,1,96,'2014-03-06 06:14:31',0,'1',0,0,0,0,0,'A',0,0,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (132,1,1,1,95,'2014-04-01 10:17:56',0,'1',0,0,0,0,3,'A',0,0,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (133,1,1,1,79,'2014-04-01 16:17:47',0,'1',0,1,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (134,1,1,1,79,'2014-04-01 16:42:25',0,'1',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (135,1,1,1,77,'2014-04-01 16:44:23',0,'1',10,4,0,1,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (136,1,1,1,77,'2014-04-01 16:47:02',0,'1',0,1,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (137,1,1,1,80,'2012-04-02 20:54:50',0,'1',0,0,0,0,0,'A',5,2,99,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (138,1,1,1,80,'2009-04-02 21:07:34',0,'1',0,0,0,0,0,'A',0,9,11,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (139,1,1,1,81,'2010-04-02 21:14:10',0,'1',0,0,0,0,0,'A',0,0,100,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (140,1,1,1,81,'2013-08-08 02:47:18',0,'1',0,0,0,0,0,'A',0,0,100,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (141,1,1,1,88,'2014-03-07 22:15:56',0,'1',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (142,1,1,1,87,'2014-04-03 23:05:08',0,'1',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (143,1,1,1,88,'2014-04-03 23:11:05',0,'1',10,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (144,1,1,1,69,'2014-04-04 22:32:07',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (145,1,1,1,69,'2014-04-04 22:33:53',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (146,1,1,1,69,'2014-04-04 22:37:05',0,'0',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (148,1,1,1,78,'2014-04-05 13:40:11',0,'1',10,4,0,1,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (149,1,1,1,78,'2014-04-05 13:41:40',0,'1',0,1,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (150,1,1,1,79,'2014-04-05 13:43:23',0,'1',10,2,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (151,1,1,1,82,'2014-10-02 21:15:32',0,'1',0,0,0,0,0,'A',2,13,100,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (162,1,1,1,93,'2014-11-26 17:52:33',0,'1',0,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (163,1,1,1,94,'2014-11-26 17:52:43',0,'1',20,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (164,1,1,1,87,'2014-12-09 11:29:10',0,'1',180,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (165,1,1,1,88,'2014-12-09 11:34:38',0,'1',540,0,0,0,0,'A',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (166,1,1,1,82,'2014-12-12 22:43:36',0,'1',0,0,0,0,0,'A',0,1,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (167,1,1,1,82,'2014-12-12 12:42:55',0,'1',0,0,0,1,0,'A',0,1,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (168,1,1,1,80,'2014-12-12 17:28:21',0,'1',0,0,0,0,0,'A',0,0,99,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (169,1,1,1,80,'2014-10-01 17:40:22',0,'1',0,0,0,0,0,'A',3,5,99,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (170,1,1,1,81,'2014-12-12 14:25:30',0,'1',0,0,0,0,0,'A',1,1,99,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (172,1,1,1,82,'2014-12-17 11:05:19',0,'1',0,0,0,0,0,'A',0,1,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (173,1,1,1,82,'2014-12-17 11:09:02',0,'1',0,0,0,0,0,'A',0,1,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (174,1,1,1,81,'2014-12-17 11:41:40',0,'1',30,0,0,0,0,'A',0,0,100,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info (f_info_id, f_org_id, f_creator_id, f_site_id, f_node_id, f_publish_date, f_priority, f_is_with_image, f_views, f_downloads, f_comments, f_diggs, f_score, f_status, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_off_date, f_p0, f_favorites, f_from_site_id) VALUES (180,1,1,1,44,'2015-12-23 21:01:57',0,'0',0,0,0,0,0,'X',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,0,NULL);
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (99,2,NULL);
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (35,2,NULL);
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (128,5,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140406112839_t51ev5.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (126,2,NULL);
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (32,2,NULL);
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (100,2,NULL);
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (143,6,'http://demo.jspxcms.com/uploads/1/image/public/201409/20140929172732_j9o0oqnn2c.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (151,7,'http://demo.jspxcms.com/uploads/1/image/public/201410/20141002211526_iouofove3e.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (129,8,'http://demo.jspxcms.com/uploads/1/image/public/201411/20141117143045_2k8pdv6ke2.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (150,9,'http://demo.jspxcms.com/uploads/1/image/public/201411/20141126104341_820t7k7ni5.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (164,6,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209112843_bs0kabf7cu.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (165,6,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209113405_ryxq84anlk.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (166,7,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141212175444_45ewp2o3og.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (167,7,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141212172257_qjop1b4iy1.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (168,7,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141212173743_kf5xldg5ey.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (169,7,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141212173924_qhyubw7vm4.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (170,7,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141212174504_w5l14a260w.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (137,7,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141216175307_busyg9xjhl.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (139,7,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141216180114_mop3wba6kt.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (138,7,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141216180547_3pqu2edor6.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (174,7,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141217114102_s0eqf9gvnb.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (131,8,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141217121217_xygn4sr97r.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (95,8,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141217121228_v1xqvymjqm.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (94,8,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141217121239_rdsuk10yp3.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (149,9,'http://demo.jspxcms.com/uploads/1/image/public/201501/20150106121017_ddtjahwl4v.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (148,9,'http://demo.jspxcms.com/uploads/1/image/public/201501/20150106121145_0pkk7m2ona.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (134,9,'http://demo.jspxcms.com/uploads/1/image/public/201501/20150106121659_wngcl3a15m.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (133,9,'http://demo.jspxcms.com/uploads/1/image/public/201501/20150106181224_jcbv9dec2s.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (30,2,NULL);
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (44,10,'http://demo.jspxcms.com/uploads/1/image/public/201605/20160523164122_vq0y4w01dc.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (43,10,'http://demo.jspxcms.com/uploads/1/image/public/201605/20160531145609_wfmicbpnwd.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (73,10,'http://demo.jspxcms.com/uploads/1/image/public/201605/20160523163408_u5vnaw9end.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (32,12,'http://demo.jspxcms.com/uploads/1/image/public/201403/20140331113308_hiqudb.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (78,12,'http://demo.jspxcms.com/uploads/1/image/public/201403/20140331150128_ik6otg.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (141,12,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140402221553_6ywoe4.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (46,12,'http://demo.jspxcms.com/uploads/1/image/public/201403/20140331153023_s8c98q.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (44,12,'http://demo.jspxcms.com/uploads/1/image/public/201308/20130813072401_15qx9s.jpg');
INSERT INTO cms_info_attribute (f_info_id, f_attribute_id, f_image) VALUES (43,12,'http://demo.jspxcms.com/uploads/1/image/public/201303/20130319062425_2c9ht9.jpg');
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (24,4,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (26,8,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (27,1,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (28,2,0,0,0,0,0,2);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (30,1,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (32,9,0,0,0,0,0,1);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (33,6,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (34,1,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (35,4,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (36,4,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (43,5,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (44,7,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (45,8,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (46,2,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (48,5,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (49,8,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (50,5,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (52,1,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (53,3,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (54,8,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (73,2,0,0,0,3,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (78,4,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (80,9,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (81,7,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (83,7,0,0,0,0,1,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (87,4,0,0,0,0,0,2);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (93,3,0,0,0,0,0,9);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (94,7,0,0,0,0,0,9);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (95,7,0,0,0,0,0,3);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (97,5,0,0,0,0,0,8);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (98,7,0,0,0,0,0,2);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (99,0,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (100,2,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (117,1,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (118,5,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (126,1,0,0,0,0,1,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (127,3,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (128,2,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (129,6,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (130,6,0,0,0,0,0,9);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (131,1,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (132,6,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (133,1,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (134,3,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (135,0,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (136,1,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (137,4,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (138,3,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (139,3,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (140,1,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (141,9,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (142,9,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (143,7,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (144,3,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (145,2,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (146,2,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (148,1,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (149,3,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (150,1,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (151,2,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (162,3,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (163,3,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (164,1,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (165,7,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (166,4,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (167,3,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (168,3,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (169,1,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (170,7,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (172,6,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (173,8,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (174,1,0,0,0,0,0,0);
INSERT INTO cms_info_buffer (f_info_id, f_views, f_downloads, f_comments, f_involveds, f_diggs, f_burys, f_score) VALUES (180,0,0,0,0,0,0,0);
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (24,'text','<p>&nbsp;</p><p style=\"text-align:center;\"><img alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201308/20130813071000_kk4cm0.jpg\" style=\"width: 500px; height: 336px; \"/></p><p style=\"text-align:center;\"><span style=\"color: rgb(0, 0, 0); font-family: 宋体, Arial, sans-serif; font-size: 16px; line-height: 28px; \">少林足球</span></p><p>近日，有关登封武校将少林功夫融入足球，培训足球运动员的新鲜事儿在网上流传，引起社会各界的关注。</p><p>8月6日下午，记者电话采访了嵩山少林寺武僧团培训基地总教头释延鲁。释延鲁说，8月2日全国第六届高校足球教练培训研讨会在登封举行，他向与会的70多位国内各高校的足球教练员阐述了他的观点。释延鲁认为，武术和足球两者可&amp;ldquo;强强联合&amp;rdquo;，少林功夫中的腿法如十二路弹腿、桩功和其他拳法，对提高学生的脚法、抗撞击能力和身体协调性有很大帮助，可将这些技能融入足球之中，希望能对提升足球的训练水平有所帮助。</p><p>释延鲁说，他们已经就功夫与足球的结合，做了技术性的研究，并已付诸行动。早在2010年10月，少林寺武僧团培训基地就已开始组建青少年足球队。据介绍，目前少林武僧团培训基地与河南建业集团联合，规划建设少林建业国际足球学校。该项目今年2月26日奠基，计划投资20亿元，包括一个足球学校、一个体育场和两个体育馆等。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (25,'text','<p>&nbsp;</p><p>北京警方10日通报，8月7日警方出动300名警力，对藏匿于一栋大厦内侵害公民个人信息的犯罪团伙实施抓捕，299名嫌犯落网，其中刑事拘留95人、行政拘留204人，并缴获数十箱公民个人信息名单。目前，涉案公司总裁仍在逃。</p><p>北京市公安局丰台分局副局长郑威在10日的新闻发布会上介绍说，今年8月初，警方侦查发现，位于北京南三环一栋大厦内的某收藏品文化交流中心，涉嫌非法获取公民个人信息，实施违法犯罪活动。该公司机构庞大、组织严密。</p><p>据侦破此案的警方介绍，该公司拥有400多名员工，下设23个部门，其中技术部被称为公司的核心机密部门，有7名员工，他们每天的任务是通过互联网等途径盗取大量公民个人信息，再经过分析梳理汇总后，输送到公司本部，分发到各个营销部门。为逃避打击，技术部的办公地点远离公司本部。</p><p>警方调查发现，该公司推销的所谓收藏品均是从旧货市场、集币市场、纪念品制造厂家廉价收购的，这些低价的钱币、古玩，经过精美包装，配上公司自制的收藏证书，向客户高价销售。</p><p>该公司招聘的近300名话务员经过培训，按照盗取的公民信息，用掌握的话术技巧虚构藏品价值，诱骗客户购买。每名话务员每天至少拨打200个以上电话进行推销。从查获该公司相关数据显示，其交易量3万余笔，交易额上亿元人民币。</p><p>经审查嫌犯交代，涉案公司总裁为辽宁省人韩某，日常管理由张某等3个副总裁负责。目前，张某及400余名员工正在接受警方调查。警方对在逃的韩某展开抓捕。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (26,'text','<p>&nbsp;</p><p>11日凌晨开始，北京雷雨大作，持续一夜，至9时许雨量减弱，天气转阴。北京市气象台发布消息称，11日白天到夜间，北京将有大到暴雨，并伴有雷电。尤其是傍晚至夜间，雨势较大，山区须注意防范地质灾害。</p><p>降雨带来了些许清凉，一解前几日的持续暑热。11日一早，许多市民趁雨停了，纷纷出门采买，但整个京城仍然雾气朦胧，略显闷热。北京市气象台值班室相关人员表示，虽然已经立秋，但是三伏天还没结束，雨后的天气闷热感依旧较明显。</p><p>11日7时35分，北京市气象台发布雷电黄色预警信号称，预计大范围的降雨自午后开始，傍晚到夜间雨势更加明显，最低气温23摄氏度。</p><p>针对降雨可能带来的地质灾害，北京市国土局和气象局9时30分联合发布地质灾害气象风险黄色预警，预计11日白天到12日，北京市房山、门头沟、昌平、延庆、密云、怀柔、平谷等大部分山区，以及丰台、海淀、石景山部分浅山区发生地质灾害的气象风险较高，提示有关部门要加强监测、注意防范。</p><p>北京市防汛办发出重点提示，请市民减少出行，注意交通安全，山区防山洪、滑坡、泥石流地质灾害。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (27,'text','<p style=\"text-indent: 2em\">作为淘宝最知名的团购平台，2011年是聚划算的爆发年，而历经反腐门之后，重新出台的聚划算团购服务竞拍规则(俗称“坑位费”)出台至今就重创了不少中小商家。</p><p style=\"text-indent: 2em\">据聚划算官网显示，其竞拍时间为每天上午10：00-11：00，竞拍起拍价为人民币1000元，单次加价幅度为100元及其整数倍，参与聚划算竞拍的卖家应在竞拍前在支付宝账户中冻结1000元保证金。看似门栏低的竞价规则，在一轮轮商家疯狂竞价之后，从几万飙升至几十万，外加聚划算要收的1%到3%不等的佣金，中小商家叫苦不迭。</p><p style=\"text-indent: 2em\">“很多小卖家上了聚划算，但是卖了几十套、几百套的很多，付坑位费还不够。”上海柔仕个人护理用品有限公司王文彬对《投资者报》记者说，“聚划算流量是很大，但是类目细分的很厉害，分配给每个坑位的流量实际是有限的，其次聚划算的消费人群本身对价格敏感，如果价格比平时购买并无优势，消费者是不会买单的。”</p><p style=\"text-indent: 2em\">公开数据显示，2012年聚划算全年交易额207.5亿，是2011年2.03倍，但是分散到各个参与活动卖家却不甚乐观，加上不断上涨的高昂坑位费也影响了单品促销效果。2012年6月，聚划算在上线了品牌团、本地生活，每个品牌团商家平均销售额200万左右/期，最高达到700万(3天)，但品牌团销售额占整个聚划算的30%。</p><p style=\"text-indent: 2em\">而随着参与聚划算平台的卖家利润不断挤压，聚划算早已沦为商家劣质产品集散地。“据我认识的将近500个卖家，为了保持利润，基本都是压缩成本，在材料上偷换。做饰品的，基本都是拿好的去质检，然后</p><p style=\"text-indent: 2em\">用不好的材质去生产，聚划算基本都是问题产品。”义乌市朵拓贸易有限公司电子商务韩姓总监对《投资者报》记者说。</p><p style=\"text-indent: 2em\"><strong>聚划算成清仓专用</strong></p><p style=\"text-indent: 2em\">“现在的聚划算我的理解就是清库存才会去上。要不然有些商家竞价那么高，会愿意出的吗，而且谁也无法知道这个销量能否得到保障。”上海简恋家居用品有限公司电子商务部运营主管陈明顿对《投资者报》记者感叹到。</p><p style=\"text-indent: 2em\">2012年上半年，聚划算从原来免费的模式开始变为竞拍坑位。之前只要跟店小二合作就能开展团购，这导致了一定程度上的内部腐败，之后随着<!--keyword--><a class=\"a-tips-Article-QQ\" href=\"http://stockhtm.finance.qq.com/hk/ggcx/01688.htm\" target=\"_blank\"><!--/keyword--><span style=\"color:#00479d\"><span style=\"font-family:微软雅黑\">阿里巴巴<!--keyword--></span></span></a><!--/keyword-->反腐，把聚划算的游戏规则重新改写。不过，这个改写也屡遭诟病。</p><p style=\"text-indent: 2em\">“以前行贿小二，跟小二商量，比如给一万，再拿几个点的提成，如果销量不好，就不需要付那么多的钱。跟之前比，当然是坑位费成本高，现在搞得跟赌博一样。”上述韩姓总监愤慨到。</p><p style=\"text-indent: 2em\">坑位费的水涨船高，挫败了中小商家，王文斌表示，现在的坑位费导致的是小卖家基本都处于观望的状态，当然对运营能力较强的大卖家而言，几乎没有影响。“过去的坑位费大家都比较盲目，上什么都能赚钱，现在坑位费过高了，必须考虑投入产出，对商品销售做合理的预测。”电子商务观察员鲁振旺对记者解释道。</p><p style=\"text-indent: 2em\">收费竞拍坑位后，中小商家基本上不寄希望能赚钱，只是想处理自己推广不动的商品，而其实如果能做到清库存也不失为一件好事，但即使这样，也未必能如愿。如果上以前去聚划算是为了拉高销量，那现在的情境则完全会让商家绝望。</p><p style=\"text-indent: 2em\">“不是说销量提升，是销量比原来降了三分之一多，个别类目也有降了一半多。”对比竞拍之后的销量变化，上述韩姓总监如此说道。当然他不是最惨的，他认识的一个做化妆品的同行，做聚划算根本没有销量，最后被逼无奈只能自己在十一点刷了五百多单。</p><p style=\"text-indent: 2em\">2011年6月开始在聚划算做的韩总监见证了淘宝卖家在这个平台上的流失。他的一个淘宝卖家群有575个淘宝店主，覆盖将近两千个店面，但现在还依然做聚划算的寥寥无几了，除了清库存以外，卖家基本不会再去做聚划算了，而他负责做的几个店早已对聚划算敬而远之。</p><p style=\"text-indent: 2em\"><strong>平台急于变现堵死中小商家 </strong></p><p style=\"text-indent: 2em\">利用聚划算打造爆款的模式已经过去了，而现在，若想要把库存变现金，聚划算定是好平台，尤其对于有运营能力的卖家，聚划算几乎可以完美解决库存管理难题。</p><p style=\"text-indent: 2em\">目前，在淘宝上能给到的流量最多的入口依然是聚划算，而其他的入口几乎都没能转化那么多的存货，但现在单品销售数量跟以前差距太大，以前能上万销量出现的单品，现在已属罕见。</p><p style=\"text-indent: 2em\">并且昂贵的坑位费，还有三个点的佣金，加上第一次上聚划算还必须要如淘宝仓库发货，而这里比卖家自行合作的快递价格平均一件多2块左右。</p><p style=\"text-indent: 2em\">昂贵的成本，有限的入口，中小卖家生存艰难。“之前聚划算是一个快速回笼资金，打造爆款的平台，大家爱之不及，现在只要有正常商业头脑的中小商家对聚划算也逐渐恢复到了正常的心态吧。”王文斌说。</p><p style=\"text-indent: 2em\">取消坑位费的呼声一直都很高。王文斌认为，“淘宝可以转为提高销售提成的方式，这样卖家定价可以更低，且卖家也不存在上一次聚划算亏一次的情况。”</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (28,'text','<p>“这是今年最值得向大家推荐观测的流星雨”，北京天文馆馆长朱进前日发微博“推介”即将于13日凌晨迎来极大值的英仙座流星雨，是日恰逢七夕。但天气预报显示，届时北京将以阴雨天气为主，市民能否一睹年度最佳流星雨，还要看天公是否做美。</p><p>据国际流星组织(IMO)估计，今年英仙座流星雨的高峰期将在北京时间8月12日21：15至8月13日早晨9:45之间，极大峰值将出现在13日凌晨2:15至4:45之间，有望迎每小时约100颗流星。</p><p>此次英仙座流星雨大爆发恰逢“中国情人节”七夕当日，而中国正好位于最佳观测区，因此备受网友关注。北京天文馆馆长朱进昨日告诉本报记者，13日凌晨的郊外，是北京观测流星雨的最佳时间和地点，但关键是天气。朱进10日通过看晴天钟预测，北京西北方向、内蒙古中部、宁夏、山西北部、陕西北部等地天气或将不错，观测条件好的话每小时肉眼可看到50颗以上的流星。</p><p>但朱进表示，夏天天气多变，12日后半夜具体何地适合观测，还需根据今天最新的天气预报来判断，一般情况下北京地区总会有相对晴朗的地方可看到星空。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (30,'text','<p style=\"text-indent: 2em\">据美联社3月18日报道，瑞典智库斯德哥尔摩国际和平研究所(SIPRI)18日称，中国已取代英国成为世界第五大常规武器出口国。</p><p style=\"text-indent: 2em\">SIPRI在报告中称，中国在过去5年(2008-2012)中武器出口总量增长了163%，国际军火市场占有份额从2%增至5%，同时排名也从之前的第八升至第五。</p><p style=\"text-indent: 2em\">报告称，中国武器的最大买家来自巴基斯坦，其购买量占到中国常规武器总出口量的55%，紧随其后的是缅甸和孟加拉国，在中国武器出口中的占比分别为8%和7%。</p><p style=\"text-indent: 2em\">针对国际社会对近年来中国武器出口的关注，中国国防部发言人耿雁生曾表示，中国在武器出口问题上，一直严格遵循三项原则：一是有助于接收国的正当防卫能力;二是不损害有关地区和世界的和平、安全与稳定;三是不干涉接收国的内政。耿雁生表示，中国严格遵守<span class=\"infoMblog      \">联合国</span>有关决议，建立了一套完备的军品出口管制法规体系，武器出口都是合法的、负责任的。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (32,'text','<p>俄南部军区27日起在克拉斯诺达尔边疆区举行空中演习。据悉&#xff0c;在为期两天的时间里&#xff0c;苏&#xff0d;25SM3强击机机 组人员将完成约40架次的飞行&#xff0c;并在无线电干扰的情况下对假想敌进行约50次打击。俄国防部网站消息说&#xff0c;俄东部军区当天也开始了大规模空中演习。演习将在 哈巴罗夫斯克边疆区和滨海边疆区进行&#xff0c;将有超过30架包括苏&#xff0d;27、苏&#xff0d;30在内的战机参与。俄东部军区新闻发言人亚历山大戈尔杰耶夫说&#xff0c;如此多数量战机 参与的演习在该区尚属首次。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (33,'text','<p style=\"text-align: center; text-indent: 2em\"><img alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201303/20130318085043_oos13e.jpg\" /></p>\r\n<p style=\"text-align: center; text-indent: 2em\"><span style=\"font-size: 12px\">事故现场照片（翻拍自案卷</span></p>\r\n<p text-indent:=\"\">在高速上失控撞<a class=\"a-tips-Article-QQ\" href=\"http://data.auto.qq.com/car_serial/186/index.shtml\" target=\"_blank\">捷达</a> ，捷达车内一家四口身亡。司机和死者家属都认为事故系爆胎造成，肇事人月底前将来京申诉；死者家属近期也将起诉锦湖公司索赔。</p>\r\n<p style=\"text-indent: 2em\">此前，司机曾起诉锦湖轮胎公司，并申请鉴定轮胎质量。虽然3份证人证言、5份公检法出具的法律文书均证实爆胎引发车祸，鉴定机构却避开质量问题，推翻生效文书，称&ldquo;车祸导致爆胎&rdquo;，导致司机败诉。</p>\r\n<p style=\"text-indent: 2em\">肇事司机的律师质疑鉴定结论&ldquo;太荒唐&rdquo;。鉴定机构对此回应，&ldquo;对鉴定有异议，让法院来找我&rdquo;。</p>\r\n<p style=\"text-indent: 2em\"><strong>突发祸事出车祸一家四口全部身亡</strong></p>\r\n<p style=\"text-indent: 2em\">2010年8月11日晚上，74岁的胡振兴老人接到电话：大女儿胡彦军一家出车祸了。</p>\r\n<p style=\"text-indent: 2em\">京沈高速上，一辆车失控冲过隔离栏到马路对面，撞上胡彦军的车子，她的捷达车随后起火。44岁的胡彦军、她43岁的丈夫、她20岁的大女儿和17岁的小女儿都被烧死。</p>\r\n<p style=\"text-indent: 2em\">老人在房山区长沟镇东甘池村的家中接受了记者采访。他说，他有四个子女，大女儿一直是他的骄傲。&ldquo;女婿开了个石板厂，经济上挺富裕。夫妻俩感情也好，也孝顺，每周都给我来电话。两个孩子，从小就爱在我身旁撒娇。&rdquo;</p>\r\n<p style=\"text-indent: 2em\">老人说：&ldquo;那些天好难熬。早晨没等起床我就大哭一场，一整天眼泪围着眼眶转。猜测着事故的严重程度，我感觉床周围是万丈深渊，晚上靠安眠药才睡了半宿。&rdquo;</p>\r\n<p style=\"text-indent: 2em\">他把头仰在沙发上，睁大了眼睛叹气。&ldquo;女婿事业心强，想转行做更大的生意。转产之前想去外地放松一下，就去北戴河了。&rdquo;</p>\r\n<p style=\"text-indent: 2em\">&ldquo;俩孩子本来不想去，爹妈说光是大人没兴致，硬拉走了。玩了几天俩孩子觉得没意思，吵着要走，于是提前往回赶。谁承想&hellip;&hellip;&rdquo;</p>\r\n<p style=\"text-indent: 2em\"><strong>追忆经过15分钟行车逆转命运</strong></p>\r\n<p style=\"text-indent: 2em\">肇事司机苏琳是个1985年出生的兰州女孩。维权的不顺利已经让她对外界产生怀疑，坚持要求当面采访。3月8日晚，记者乘机飞抵兰州，次日上午与苏琳会面。</p>\r\n<p style=\"text-indent: 2em\">3月9日是个沙尘天，天色昏暗，路人戴着口罩来去匆匆。下午2点，一个一身黑衣的高个子女孩在朋友的陪同下，来到记者下榻的兰州航空大酒店一楼。这比约定的时间晚了一小时。苏琳不好意思地解释：为了多挣些钱，她周末也在加班，现在是请假出来的。</p>\r\n<p style=\"text-indent: 2em\">采访是在附近一家茶馆里进行的。苏琳拿着厚厚的茶水单看了很久，半天没说话。记者意会，随即主动选了一壶最便宜的茶水，并表示将会买单。苏琳的神色有些尴尬。她自嘲地说，车祸把她的命运逆转，她已经很久没过白领的生活了。</p>\r\n<p style=\"text-indent: 2em\">在北京读完大学，苏琳先在一家公司当白领，之后辞职经商。她说，出事前，她的生意挺红火。</p>\r\n<p style=\"text-indent: 2em\"><img alt=\"事故现场照片（翻拍自案卷\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201303/20130318084927_1siuwe.jpg\" /></p>\r\n<p style=\"text-align: center; text-indent: 2em\"><span style=\"font-size: 12px\">事故现场照片（翻拍自案卷</span></p>\r\n<p style=\"text-indent: 2em\">细细地喝了一口茶，她说出了当年的梦想：努力工作多挣钱，将来把兰州的房子卖掉，在北京买房，把父母接过来一起生活。</p>\r\n<p style=\"text-indent: 2em\">命运的转变发生在2010年8月11日。当天上午，苏琳的生意合伙人、东北女孩刘由迪开着她的华晨骏捷回抚顺老家。苏琳的姨家也在抚顺，于是苏琳和父母搭上刘由迪的车去串亲戚。</p>\r\n<p style=\"text-indent: 2em\">苏琳说：&ldquo;我们一路走京沈高速。开到第一个服务站后休息了会儿。之后我说一个人开车太累，就把刘由迪换下来了。结果，开了15分钟就出事了。&rdquo;</p>\r\n<p style=\"text-indent: 2em\"><strong>多方证实接受讯问司机坚称爆胎致祸</strong></p>\r\n<p style=\"text-indent: 2em\">苏琳说，当时自己已有两年驾龄。虽然车不是自己的，但因为刘由迪和自己一起租房生活，自己平时经常开她的车，对性能很熟悉。</p>\r\n<p style=\"text-indent: 2em\">讯问笔录显示，苏琳始终供述是爆胎导致车辆失控。她对警方表示：&ldquo;当时的车速大约是100迈。突然不知道怎么回事，感觉车跑偏了。当时刘由迪喊车偏了，车就撞到左侧护栏上了，之后我就什么都不知道了。&rdquo;</p>\r\n<p style=\"text-indent: 2em\">接受采访时，苏琳再次证实了这一点。</p>\r\n<p style=\"text-indent: 2em\">&ldquo;当时我们都晕过去了。我爸第一个醒过来，把我们救了出去。我妈大腿股骨头粉碎性骨折，我左脚根骨粉碎性骨折、脊椎第三节骨折，胸腔有三根骨头断了。&rdquo;尽管已经时隔几年，但说到这里，苏琳还是有些激动，语速也快了起来。</p>\r\n<p style=\"text-indent: 2em\">苏琳说，大家从车里出来后，发现前面有辆白色捷达着火了。</p>\r\n<p style=\"text-indent: 2em\">&ldquo;我爸打电话报了警。他想拿灭火器救火，但我们的车门已经打不开了。很快，那辆车的火势到了人没法接近的地步。&rdquo;她说。</p>\r\n<p style=\"text-indent: 2em\">苏琳说，她被送进医院、民警向她问话的时候，她才得知是自己的车撞了对方。</p>\r\n');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (34,'text','<p>6日晚，在昆明市白龙路一自助银行里，来取钱的刘老师站在柜员机前捣鼓了半天&amp;mdash;&amp;mdash;卡插不进去，他在柜员机上随便按了“5000”，谁知50张百元钞票就吐到了他面前。机器出了问题？他又按了“5000”，又有5000元吐到了手里。仔细一查，发现柜员机里原来已有一张银行卡，刘老师忙报了警。经过盘龙公安分局东华派出所民警的寻找，次日找到了卡的主人。</p><p>　　6日当晚10点30分许，刘老师来到自助银行取款。掏出卡往自动存取款机上塞却塞不进。再塞，还是一样。难道柜员机出问题了？他试着输入了5000，按照柜员机提示按下“取款”，随着机器“嗒嗒”的声音，钞票盖打开，一沓钞票就在眼前。这是不是假钞呢？刘老师愣了一会，再试，又出来5000元。难道天上真会掉馅饼，经过一番检查，原来是柜员机里有一张银行卡。取出卡后，他用自己的卡进行了存取款“试验”，发现机器没问题，而是粗心人忘记取走银行卡。</p><p>　　面对这个天上砸下的馅饼，刘老师没有犹豫，直接拨打110报警。当东华派出所民警赶到银行时，他把1万元现金和银行卡一并交给了警察，并请民警一定要找到失主。</p><p>　　次日下午，在昆明开火锅店的刁女士接到东华派出所打来的电话，说她的银行卡和1万元现金被好心人拾到，请到派出所核实领取时，她很惊讶，自己的卡怎么会到了派出所？前一晚，火锅店打烊后，她将银行卡交给两名店员，带着当天的1万元营业款去银行存起来。10点半时，到了白龙路自助银行，一名店员站在旁边玩着手机，另一名数着钱。存款完成后，两人拿了凭条就离开了自助银行。</p><p>　　15日下午，刁女士特地带着一封感谢信和一面锦旗，来到东华派出所，并从民警手中接过现金和银行卡。刁女士说：“银行卡上有近10万元存款，也怪员工太粗心大意了，太感谢刘老师了！”</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (35,'text','<p>&nbsp;</p><p>31省市男女最理想伴侣地图出炉</p><p>首选嫁京男&nbsp;最爱娶川妹</p><p>超五成的青年择偶时首选本地人；女性最想嫁的外省人中，北京男最受欢迎；男性则最想娶川妹子。</p><p>记者上午获悉，零点指标数据进行2013年七夕主题调查，对全国各地1074位18至45周岁的网民进行随机访问，绘出31省市青年男女最理想伴侣地图。调查还显示，八成人打算庆祝七夕，比例超过西方情人节。</p><p>半数择偶爱“窝边草”</p><p>对“您最想娶/嫁哪个地区的姑娘/男人”选项的统计结果显示，54.5%男性和57.7%女性首选与自己家乡省份相同的人。</p><p>在选择外省人作为理想伴侣的人中，选择南方姑娘和选择北方男人的比例相对较高；地理位置偏远的新疆、西藏、青海、甘肃、宁夏、贵州、广西等省区的女性和男性，被选中的比例最低。</p><p>北京爷们儿成首选老公</p><p>除去同乡人的吸引，最想嫁的男人当中，北京爷们儿排在第一位，并列第一的还有以“小男人”著称的上海男，都有15.2%的女青年首选。</p><p>接下来受欢迎的依次是四川、黑龙江和辽宁男人。</p><p>如果将女青年分成普通女青年和文艺女青年。那么其中，普通女青年偏爱北京爷们儿，占14.8%，文艺女青年更爱上海老公，占13%。</p><p>娶妻偏好“麻辣味”</p><p>男人最想娶的外省女性是四川妹子，占13.6%。</p><p>接下来受欢迎的依次是浙江、江苏的姑娘。</p><p>同属“麻辣味”的重庆姑娘，排名第四，占8.9%；北京姑娘居第五。</p><p>网传旺夫的山东妹子只排到了第11位，占3.3%，竟未进入前十，其受欢迎程度与东北三省中的辽宁姑娘相当。</p><p>怎么过七夕</p><p>八成不满意</p><p>伴侣表现怎么样？</p><p>今天是七夕，此次调查结果显示，超过八成城市居民表示，如果有另一半会共享七夕节，超过选择过2月14日西方情人节75.9%的比例。</p><p>不过，在有共度七夕节经历的情侣或夫妻中，却有高达79.8%的受访者对另一半在往年七夕当天的表现不满意。</p><p>整日陪伴</p><p>最想要什么礼物？</p><p>零点调查发现，七夕情侣间最期待惊喜榜榜首的是“整天时间陪伴在一起”，占18.5%。分列二三位的七夕惊喜是：爱人下厨，占14.8%；一次旅行，占11.6%。</p><p>而最多的惊喜礼物是“亲自下厨”，占20.2%，其次有13.5%的人选择送“衣服/鞋/包”，送“手工礼物”的占11.6%、“旅游”占10.7%。而最受期待的“在一起”并未得到人们的重视，仅有8.6%的人打算整天陪伴爱人。</p><p>文/记者王婷婷</p><p>中国情人节新人领证未见高峰</p><p>上午全市仅千对</p><p>七夕婚登门前“稀”</p><p>本报讯（记者陈斯夜线报道组实习生柯竞）今天是“中国情人节”七夕，恰逢8月13日谐音“不要散”，民政部门按照婚姻登记高峰做准备，却未见新人扎堆儿领证。</p><p>截至上午11点，全市婚登的新人约1000对，不及“2013.1.4”那天。</p><p>昨天下午，朝阳区民政局结婚登记处预计七夕会有500对新人前来领证，特意准备好千张证书，并为了加快新人领证的速度，提前给证书上加盖好婚姻登记专用章，盖好印章的证书堆成了小山。</p><p>今晨7点半，记者来到朝阳区婚姻登记处，已经有14对新人在门外分两队等待，右侧是已经在网上预约登记的，左侧则是未预约直接来的。</p><p>为了应对预计可能出现的登记高峰，工作人员提前一个小时开始办理。但此时在门口等待登记的新人，没有了往日排大队的强大阵容。</p><p>8点05分，第一对办好结婚证的闫先生和刘小姐微笑着携手而出，“我们大概提前半个月在网上预约的，今天早上五点多就来了。感觉特别快，不到2分钟就办完了”。</p><p>不到10分钟，所有排队等候的新人全部拿到了结婚证。</p><p>官方解析</p><p>对传统节日里领证不感冒？</p><p>朝阳区婚姻登记处负责人金先生表示，现在除了情人节、七夕节等传统的结婚登记吉日，5.20、9.9等谐音吉日也越来越受到年轻人的追捧。今年朝阳区在网上预约七夕当天登记结婚的新人有70多对，较往年有所减少。其主要原因是被其他好日子给分流了。</p><p>像上周的8月8日人就比较多，2013.1.4世纪结婚日，零点刚过就开始办理了，当日全市有6000对新人领证。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (36,'text','<p>根据中国社科院相关机构进行的“中国公民政治文化”问卷调查：90．03％被调查者对“作为中国人，我很自豪”持赞同态度；72．23％被调查者认同“中国传统文化对个人具有很大的影响”。该调查在全国10个省份进行，获得6159份有效样本。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (43,'text','<p style=\"text-align:center\"><img alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201303/20130319005332_lmwkqx.jpg\" /></p><p>近日&#xff0c;有明星经纪公司爆料&#xff0c;唱遍大小春晚的“农业重金属组合”凤凰传奇&#xff0c;出场费已经涨到了60万/场&#xff0c;若加上代言&#xff0c;2012年约有1亿进账。1亿这个数字也许略有夸张&#xff0c;但实际收入肯定也不会少。网友们一边感叹农业重金属的力量不可小觑&#xff0c;一边又开始琢磨这么多钱他们怎么分呢&#xff1f;凤凰传奇经纪人接受采访时表示&#xff0c;“玲花和曾毅早就签好协议&#xff0c;收入一人一半”&#xff0c;这样一来&#xff0c;网友又有疑问了--负责RAP部分的曾毅除了“嘿、吼、喔、哈、切克闹”就没别的词儿&#xff0c;难道也能分一半&#xff1f;这钱挣得也太轻松了吧&#xff1f;</p>[PageBreak][/PageBreak]<p><br /></p><p>其实在娱乐圈&#xff0c;像凤凰传奇这样的“人气搭档”并不少&#xff0c;有的情比金坚&#xff0c;有的钱字当头&#xff0c;有的好聚好散&#xff0c;也有的老死不再往来。为此&#xff0c;腾讯娱乐采访了多位圈中资深人士&#xff0c;为大家八一八人气搭档“分钱”那点事儿。<br /></p><p><br /></p>[PageBreak][/PageBreak]<p><br /></p><p>至于近几年大火的凤凰传奇&#xff0c;也是五五分成么&#xff1f;按网友的话说&#xff1a;“女的唱得声嘶力竭&#xff0c;男的出来切克闹几句就下去了&#xff0c;如果平分亿元年收入&#xff0c;会不会太不公平&#xff1f;”</p><p> 对此&#xff0c;凤凰传奇经纪公司总经理徐明朝回应称&#xff0c;“从演唱角度看&#xff0c;玲花确实是主唱&#xff0c;但是从组合角度看&#xff0c;这是一个整体&#xff0c;没有主次之分。早在成名之前&#xff0c;他俩就已签好合约&#xff0c;无论将来赚多少都对半分。”</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (44,'text','<p style=\"text-align:center\"><img alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201308/20130813072104_ur3e8y.jpg\" /></p><p style=\"text-align:center\"> </p><p>8月12日&#xff0c;羽泉(微博)(微信号&#xff1a;yuquanweixin )携专辑《拾伍》在京举办粉丝同乐会。而活动第二天“七夕节”恰好是胡海泉(微博)的生日&#xff0c;歌迷们不仅为海泉准备了礼物还送上蛋糕&#xff0c;倍受感动的海泉感慨道<span style=\"font-family:monospace;font-size:medium;white-space:pre-wrap\"><span style=\"font-family:monospace;font-size:medium;white-space:pre-wrap\">&#xff1a;</span></span>“对于羽泉来说&#xff0c;能在各位的陪伴和支持下一起走过的岁月就是最好的礼物。”为了纪念出道15周年&#xff0c;羽泉还现场宣布将于9月7日和21日在泉州和深圳接力开唱&#xff0c;以实际行动回馈每一位歌迷的支持与喜爱。</p><p style=\"margin:0px 0px 29px;padding:0px;font-size:16px;line-height:28px;font-family:, &#39;arial&#39; , sans-serif;text-indent:2em\"><strong>聊high了 哥儿俩抢爆《拾伍》幕后花絮</strong></p><p>现场&#xff0c;羽泉在主持人大鹏的介绍下出场&#xff0c;与大家分享了重唱集《拾伍》制作过程中的心路历程。“老歌新翻&#xff0c;真不是一件容易的事儿&#xff0c;跟制作老师讨论编曲熬通宵变熊猫眼是常事儿”&#xff0c;陈羽凡(微博)调侃道。</p><p>由于专辑里大部分歌曲都是羽泉的老歌&#xff0c;而且几乎首首曾红极一时&#xff0c;所以此次回炉再造&#xff0c;不只是对羽泉&#xff0c;对其他幕后工作人员的压力也不小。机缘巧合下&#xff0c;羽泉在参赛《我是歌手》(观看)期间接触了来自日本的镰田老师&#xff0c;当时简短的交流&#xff0c;却奠定了此次专辑的合作。镰田几乎对羽泉组合进行了一次“大手术”&#xff0c;老歌里嗓音“温润如水”的海泉在这张专辑里嘶吼了起来&#xff0c;而“高亢激进”的羽凡反而展露了嗓音中更为细腻的一面。除镰田老师之外&#xff0c;担任乐手以及后期制作人员也都是日本乐坛数一数二的“老江湖”&#xff0c;很多人都是某一个曲风领域的绝对专家。在这些音乐人的合力之下&#xff0c;羽泉《拾伍》重唱集平衡了两个人的嗓音特点、平衡了原曲应有的风格和气质&#xff0c;所有的编曲也符合当下的音乐潮流&#xff0c;并且让大部分歌曲都焕然一新。而此次专辑是否算作翻新成功&#xff0c;得到了当天全体歌迷的一直认同。</p><p style=\"margin:0px 0px 29px;padding:0px;font-size:16px;line-height:28px;font-family:, &#39;arial&#39; , sans-serif;text-indent:2em\"><strong>玩疯了 与歌迷玩游戏不手软&#xff1a;“吃苦瓜身体好”</strong></p><p>当天同乐会上的重头戏是玩游戏&#xff0c;经过前期活动选拔出来的十五对歌迷与哥俩分别成军大PK。成军十五年来&#xff0c;羽泉两人间的默契和感情一直为外界津津乐道&#xff0c;而此次活动设置的游戏也都是需要队员间默契协作方能完成的。无论是“两人三足”还是以羽泉为环绕点的“比手画脚”再加上行进的很艰难的“终极战车”&#xff0c;现场哥俩带领歌迷们玩疯了&#xff0c;两人对战起来毫不手软&#xff0c;互相放话互相拆台一点不客气&#xff0c;甚至不惜互相拆台以便“打击”对方的气势&#xff0c;比赛火热的同时笑声不断&#xff0c;熟男变“活宝”&#xff0c;最终输的一方接受了全队吃苦瓜的惩罚&#xff0c;“在炎炎夏日&#xff0c;吃个清热解毒去火的绿色食物对身体好!”</p><p>羽泉承诺“不离不弃” 9月将于深圳开唱</p><p>活动第二天是七夕情人节&#xff0c;正好是羽泉成员胡海泉的生日。所以&#xff0c;当天参加同乐会的歌迷也精心准备了礼物和蛋糕带到现场&#xff0c;并在最后的环节突然献上&#xff0c;疯狂的同乐会顿时变成了温馨的庆生会&#xff0c;海泉惊喜的同时也为歌迷的贴心举动感动不已。“对于羽泉来说&#xff0c;能在各位的陪伴和支持下一起走过的岁月就是最好的礼物&#xff0c;现在我们已经顺利地走过了十五年&#xff0c;我们希望&#xff0c;未来的每一个十五年都可以像现在这样走过。”当天&#xff0c;羽泉面对在场的歌迷郑重承诺&#xff1a;“未来&#xff0c;不离不弃&#xff01;”</p><p>在许下了生日的愿望之后&#xff0c;羽泉也为歌迷们送上了更大的“回礼”——9月7日和21日&#xff0c;羽泉将在泉州和深圳接力开唱&#xff0c;以实际行动回馈每一位歌迷的支持与喜爱。“用音乐给各位带来正能量是我们最擅长并会一直做下去的事&#xff0c;下一个十五年&#xff0c;请各位瞧好吧&#xff01;”</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (45,'text','<p style=\"text-align: left; text-indent: 2em;\"><span style=\"text-indent: 2em;\">“大家好，我是制片人杨幂。”一身干练的黄色风衣，一句霸气外露的自我介绍，升级当了制片人的杨幂果然显现出女强人的气质。昨日，由其担任监制的都市时尚偶像剧《微时代之恋》在沪举行开机发布会，杨幂带着她钦点的男主角余文乐，以及她花费一年选出的八位新人齐齐亮相。腾讯网娱乐中心总监常斌到场助阵，宣布该剧官网正式落户腾讯娱乐。</span><br/></p><p style=\"text-indent: 2em\">在接受腾讯娱乐独家专访时，“杨老板”坦言自己这个初出茅庐的制片，在男友刘恺威(<span class=\"infoMblog\">微博</span>)(<span class=\"infoMblog\">微信号：hawicklaw</span>) 身上学到了很多东西。虽然这次她没有找刘恺威当男一号，但为了给女友的制片处女作保驾护航，刘恺威还全程跟随剧组前往巴厘岛，在片场化身超级助理和替身，打杂跑腿兼打气，让杨幂甜在心里。</p><p style=\"text-indent: 2em\">虽说在事业上越做越大，杨幂却认为，自己离“事业有成”还很远。刘恺威年初已公开今年有迎娶杨幂的计划，她却不着急结婚，还想再谈谈恋爱。</p><p style=\"text-indent: 2em\"><strong>“事业有成”离我很远 今年不着急结婚</strong></p><p style=\"text-indent: 2em\"><span style=\"font-family: 楷体_GB2312\">腾讯娱乐：和林心如 (<span class=\"infoMblog\">微博</span>)、范冰冰(<span class=\"infoMblog\">微博</span>)等圈内“演而优则制”的女演员相比，你算是最年轻的一位女制片，为什么放着舒服的演员不当，要来挑制片重任？</span></p><p style=\"text-indent: 2em\">杨幂：两三年前我没想过自己会拍电影，结果拍了，一年前我从没想过自己会当制片，现在也做了，我感谢每一个不可能的可能性发生在我身上。正好有一个机会摆在我面前，有人问我要不要做，我想，那就做吧。很多东西，我不一定完全有能力去做，好在身边有很多人帮我一起做，大家一起把梦想完成好，这是一个梦想照进现实的过程，我们都在努力！</p><p style=\"text-indent: 2em\"><span style=\"font-family: 楷体_GB2312\">腾讯娱乐：有个情况挺有意思的，去年刘恺威担任制片人，邀请你做女主角的《盛夏晚晴天》去了法国拍，而你就去巴厘岛拍《微时代之恋》，是有心跟男友切磋还是别有苗头？</span></p><p style=\"text-indent: 2em\">杨幂：我没有攀比的心思，正因为有《盛夏晚晴天》的班底和经验，才让我这次担任《微时代之恋》的制片非常顺手。如果他制片人做得成功，我当然为他开心，但我也有我的工作要做。我们在工作上是互相支持鼓励，也是相对独立的。</p><p style=\"text-indent: 2em\"><span style=\"font-family: 楷体_GB2312\">腾讯娱乐：去年由刘恺威担任制片的《盛夏晚晴天》成绩喜人，他还筹备起第二部制片作品《一念向北》，你有向他取经吗？</span></p><p style=\"text-indent: 2em\">杨幂：恺威是个很棒的制片人，我从他上耳濡目染学到了不少当制片人的经验，比如如何跟工作人员沟通，学会观察潜在的问题，现场遇到突发事件如何处理等等。但如果我碰到难题，自己能解决的还是想自己解决，毕竟遇到同一件事情，在别人那儿行得通的方法，在你这儿却未必合适。</p><p style=\"text-indent: 2em\"><span style=\"font-family: 楷体_GB2312\">腾讯娱乐：为什么不找刘恺威主演，如果他演，一定愿意给你“感情价”的？</span></p><p style=\"text-indent: 2em\">杨幂：为什么大家不会认为，他来，我不会给他更高的片酬？（笑）他有他自己的工作，我也会选择合适的人演剧中的角色，这次没有适合他的，等下次有了，我就找他。其实，我们不一定所有的事情都要绑在一起，当然我当制作人，他也会给我鼓励，为我加油。</p><p style=\"text-indent: 2em\"><span style=\"font-family: 楷体_GB2312\">腾讯娱乐：身兼制片人和演员两职，应该挺辛苦吧，男友有没有来慰劳你？</span></p><p style=\"text-indent: 2em\">杨幂：探过班，之前在巴厘岛的时候他有来帮忙。慰劳啊？他有来帮我演了一个后辈，因为有一场戏，和我对戏的演员没有在巴厘岛，我本来要和空气演戏，正好他在，就拉来搭了一场戏，不过还是会把他的镜头剪掉。</p><p style=\"text-indent: 2em\"><span style=\"font-family: 楷体_GB2312\">腾讯娱乐：现在开起了工作室，还当了制片人，可说是“事业有成”了，想过结婚吗？</span></p><p style=\"text-indent: 2em\">杨幂：“事业有成”这个词离我还挺远的，我还有很多东西没做到。我自身还有很多需要学习和补充的地方，还有很多专业知识是我不懂、也没有意识到的。而且，事业有成与结婚没什么关系，我会完全把工作与感情分开。</p><p style=\"text-indent: 2em\"><span style=\"font-family: 楷体_GB2312\">腾讯娱乐：但是年初，刘恺威曾公开表示今年有迎娶你的计划，你今年没意向嫁他？</span></p><p style=\"text-indent: 2em\">杨幂：没有，我想再谈谈恋爱吧，到现在我们其实都没吵过架。我们都希望能再相处一下，等我再成熟一些，但我想如果有一天走到那一步，应该就是这个人了。因为现在大家相处很好，磨合得也越来越有信心。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (46,'text','<p>重庆&#xff0c;12日&#xff0c;《一夜惊喜》见面会。女神范冰冰(微博)(微信号&#xff1a;fbbstudio916 )携李治廷空降山城重庆&#xff0c;在重庆各大影院轮番宣传最新电影《一夜惊喜》。电影中范冰冰和李治廷上演了姐弟恋&#xff0c;见面会现场&#xff0c;范爷霸气地将李治廷抱起来秀“恩爱”。当媒体问及范爷七夕怎么过时&#xff0c;范冰冰表示明天将在北京和“那个他”度过&#xff0c;那个他还出在考验期。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (47,'text','<p style=\"text-indent: 2em\">在上周六武汉卓尔与北京国安的比赛中，主裁判王迪先后错判国安外援格隆禁区内假摔，接着又漏判给故意踢人的卓尔球员柯钊红牌。中国足协裁委会赛后确认，王迪两次判罚属严重错、漏判。王迪面临停哨3至6场的重罚。据悉，王迪通过抽签获本场比赛执法资格后，足协曾有人提议由经验丰富的老裁判取而代之，但提议未被采纳。抽签定哨能保证裁判选派公平，但由此引发的“昏、嫩哨”现象却屡禁不止，而除了加大失误裁判惩处力度外，中国足协似乎也找不到更好的办法。</p><p style=\"text-indent: 2em\"><strong>足协曾担心王迪成“定时炸弹”</strong></p><p style=\"text-indent: 2em\">新赛季中超开幕前，中国足协技术部、裁委会在香河基地完成了中超前6轮的裁判抽签仪式。按照内部工作流程，足协技术部在每轮联赛之前，都会请职业联赛部门对当轮裁判人选提意见。据了解，足协有关人士曾公开反对王迪执法武汉卓尔与北京国安的比赛，这是因为上赛季国安与辽足、2011赛季国安与深足比赛，王迪都曾漏判给国安点球。足协内部有人形容王迪，有可能成为比赛的“定时炸弹”，但这一建议最终被拒，其理由是，王迪是中国足协7名国际主裁中的一员，也是年轻裁判的佼佼者。而卓尔与国安的比赛是本轮一场重要比赛。一旦更换他，可能会在国内裁判界造成一些不良影响，同时也不利于年轻裁判的培养。然而提议的足协人士不幸言中。</p><p style=\"text-indent: 2em\"><strong>王迪受罚难阻“昏哨”继续现身</strong></p><p style=\"text-indent: 2em\">由于王迪错判造成了不小影响，裁委会昨天就形成了评议意见。一位足协人士透露，王迪判罚格隆禁区内假摔、漏判柯钊红牌都是严重的判罚失误，他将面临最低3 场、最高6场的处罚。王迪并非本赛季首位受内部处罚的裁判。执法中超首轮鲁能与阿尔滨比赛的前国际主裁陶然成、执法富力与辽足比赛的年轻主裁傅明、执法揭幕战恒大与申鑫的国际助理裁判穆宇新、国安与东亚比赛的助理裁判 王峰4 人，都因出现重大错、漏判被停赛3场。今年，裁委会对职业联赛裁判工作提出了新要求，管理制度及上岗考核，较往年也更为严格，国家级裁判张正平就因未通过体测而被直接降级。足协人士指出，“从前两轮中超执法情况看，年龄并不是裁判员犯错误的主要因素。我们感觉裁判错漏判，最大的问题还在于态度。如果准备不足、对执法工作的自律不够，王迪之后很可能还会出现其他&amp;lsquo;昏哨、嫩哨&amp;rsquo;。”</p><p style=\"text-indent: 2em\"><strong>抽签定哨 足协纠结 裁判埋怨</strong></p><p style=\"text-indent: 2em\">一位足协人士指出，王迪出现错漏判表面上看，是缘于裁判自身业务水平和临场应变能力不足，但其背后却反映出“抽签定哨”的呆板。有裁委会人士曾诉苦说，“假球、黑哨丑闻曝出后，中国足协在裁判选用上承受了巨大压力，抽签起码保证了裁判选用公平。”但从2010赛季起至今，类似王迪这样的“嫩哨”不止一个，同样在2010赛季被破格提拔为国际主裁的小张雷就因经不起实践考验，于第二年被取消国际执法资格；另一位争议“嫩哨”马宁也多次被裁委会临时取消中超执法机会。一系列事实证明，这些被中国足协寄予厚望的年轻裁判并未完全得到信任。同时，中国足协抽签后改派的举动反而引起裁判圈的不满。一位裁判专家曾这样抱怨，“安排抽签，却不按抽签结果派裁判，抽中的裁判不用，对裁判自身的心理打击很大。与其如此，还不如按国际惯例，完全指派裁判。”抽签定哨有无必要延续下去，中国足协也的确有必要深思熟虑一番。文/本报记者肖赧(<span class=\"infoMblog\">微博</span>)</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (48,'text','<p>3月18日体育专电（记者林德韧）NBA(<span class=\"infoMblog  \">微博</span>)官方18日公布了最新一期的实力榜，拿下22连胜的迈阿密热火队当之无愧地继续排在榜首，圣安东尼奥马刺队和俄克拉荷马雷鸣队分居二、三位。</p><p>　　在击败多伦多猛龙队之后，热火队保住了自己的不败金身，东区冠军的位置已经基本锁定，现在剩下的唯一悬念就是热火队的连胜还能持续多久。</p><p>　　此前排名实力榜第三的马刺队在上一周展现了稳定的表现，与此同时也赢得了与俄克拉荷马雷鸣队之间的强强对话，排名超越雷鸣队排在了第二位。状态起起伏伏的雷鸣队下降至第三。</p><p>　　排名本榜单第四至第十位的队伍分别是：掘金队、灰熊队、快船队、步行者队、凯尔特人队、湖人队、网队。</p><p>　　上一周的亮点是湖人队，“紫金军团”在常规赛末段打出高水准，战绩提升至36胜12负，距离西区第七的休斯敦火箭队仅差半场，实力榜排名也从上一期的第十一上升至第九。</p><p>　　休斯敦火箭队在一场关键对决中大比分负于竞争对手金州勇士队，西区前八的位置不再牢靠，实力榜排名从第十下降至第十一。</p><p>　　排名本榜单第十一至第三十位的球队分别是：火箭、尼克斯、老鹰、公牛、小牛、勇士、雄鹿、爵士、奇才、开拓者、猛龙、骑士、森林狼、国王、太阳、76人、黄蜂、活塞、魔术、山猫。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (49,'text','<p style=\"text-align:center\"><img alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201303/20130319011912_nqvbjh.jpg\" /></p><p style=\"text-indent:2em\">在上周六中超联赛卓尔与国安比赛下半时&#xff0c;卓尔球员柯钊暴踢国安外援马季奇的举动在足球界引起了不小反响。中国足协裁判委员会今天也出具报告提请纪律委员会对柯钊追罚。据了解&#xff0c;纪委会很可能将柯钊的犯规定性为暴力行为。由于他及其俱乐部认错态度良好&#xff0c;中国足协将按照底线处罚柯钊&#xff0c;柯钊面临停赛2场、1万元左右的追罚。</p><p style=\"text-indent:2em\">电视慢动作显示&#xff0c;柯钊连续3次踢中马季奇&#xff0c;结果当值主裁王迪仅仅向柯钊出示了1张黄牌。虽然汉军最终饮恨主场&#xff0c;但是俱乐部上下深明大义&#xff0c;俱乐部代表及柯钊本人也都通过微博等渠道向外界及被踢的马季奇道歉。中国足协今天上午在总结上轮职业联赛时&#xff0c;重点提到了此事。据悉&#xff0c;裁判委员会的报告上写明&#xff0c;柯钊的行为属于暴力行为&#xff0c;理应被纪律委员会追加处罚。不过&#xff0c;足协内部有人认为&#xff0c;柯钊并非恶意伤人&#xff0c;只是因为本队比分落后&#xff0c;加之比赛时间所剩无几&#xff0c;他心情急躁&#xff0c;行为失控。</p><p style=\"text-indent:2em\">今晚有消息显示&#xff0c;柯钊将面临停赛4场、罚款2万的处罚&#xff0c;但是从中国足协传来消息显示&#xff0c;纪律委员会今天并没有开会具体商议此事。目前此事还停留在听取各方意见的过程中。但可以肯定的是&#xff0c;柯钊被追罚在所难免。一位足协官员表示&#xff0c;“鉴于柯钊认错态度不错&#xff0c;他也是初犯&#xff0c;协会不会加重处罚他&#xff0c;但这个行为依然性质很严重&#xff0c;被停赛2场的可能性最大。”至于格隆&#xff0c;虽然国安已经上诉&#xff0c;但纪律委员会依据国际惯例维持原判&#xff0c;红牌停赛不变。纪律委员会预计于明后两天内作出最终处罚结果。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (50,'text','<p style=\"text-align: center\"><img alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201303/20130319012259_khrjth.jpg\" style=\"width: 550px; height: 299px\"/></p><p>北京时间3月15日晚，欧足联在瑞士尼翁总部进行了本赛季欧冠(<span class=\"infoMblog\">微博</span> 专题) 1/4决赛对阵形势抽签。杀进八强的三支西甲(<span class=\"infoMblog\">微博</span> 专题) 球队中，皇马(<span class=\"infoMblog\">官方微博</span> 数据) 抽中土超劲旅加拉塔萨雷，巴萨(<span class=\"infoMblog \">官方微博</span> 数据) 遭遇法甲大鳄巴黎圣日耳曼(<span class=\"infoMblog\">微博</span> 数据) ，马拉加(<span class=\"infoMblog\">官方微博</span> 数据) 则要迎接德甲球队多特蒙德的挑战。以下是部分西班牙媒体评论。</p><p>《马卡报》：“皇马的抽签结果相对来说不错，4月3日，主帅穆里尼奥将在伯纳乌球场迎来他的几位老朋友，他们分别是德罗巴(<span class=\"infoMblog\">微博</span>)、斯内德(<span class=\"infoMblog\">微博</span>)和大阿尔滕托普，德罗巴是穆里尼奥执教切尔西(<span class=\"infoMblog   \">官方微博</span> 数据) 期间亲自召入的爱将，斯内德是穆里尼奥率领国际米兰夺取三冠王时的关键球员，大阿尔滕托普也曾在皇马效力，这几人帮助加拉塔萨雷战胜了沙尔克04晋级，但面对皇马，他们的运气恐怕就没这么好了，唯一要小心的是他们的前锋伊尔马兹，这位土耳其人目前和C罗(<span class=\"infoMblog\">微博</span> 数据) 并列欧冠最佳射手。”</p><p>《世界体育报》：“巴塞罗那与巴黎圣日耳曼的较量将是一次王者之争，淘汰赛看实力也要看运气，两支球队曾在1994-95赛季有过交手，当时输球的一方是巴塞罗那，不过在1996-97赛季的欧洲优胜者杯决赛中，巴塞罗那在荷兰鹿特丹战胜了对手捧起冠军奖杯，当时为巴塞罗那进球的是罗纳尔多。”</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (52,'text','<p style=\"text-indent: 2em\">尚德电力昨日宣布，公司已经收到3%可转债托管人的通知，即2013年3月15日到期的可转债仍有5.41亿<a class=\"a-tips-Article-QQ\" href=\"http://finance.qq.com/money/forex/index.htm\" target=\"_blank\">美元</a>的未支付金额，已经违约并要求尽快付款。该违约事件同时还导致尚德对包括国际金融公司和一些国内银行在内的其他债权人的交叉违约。</p>\r\n<p style=\"text-indent: 2em\">尚德电力也因此成为了中国大陆首家出现公司债务违约的企业。</p>\r\n<p style=\"text-indent: 2em\">公司表示，将继续努力进行重组，提高运营成本效率，保持与现有客户和供应商的良好关系，寻找其他的资金来源，以满足运营和债务偿还资金需求。</p>\r\n<p style=\"text-indent: 2em\">尚德电力的违约将有望引发债券持有人对公司的诉讼。尚德电力在上周宣布获得了63%债券持有人的同意，将把债务偿付时间延长两个月至5月15日，以便管理层着手进行债务重组。部分债权人拒绝接受这个方案，并组成一个团体威胁发起诉讼。</p>\r\n<p style=\"text-indent: 2em\">同时，尚德的声明显示其对国内银行也已出现违约。截至2011年末，尚德银行融资高达17亿美元，国内银行为尚德最主要的债权人，其中<span onmouseover=\"ShowInfo(this,&quot;00939&quot;,&quot;100&quot;,&quot;-1&quot;,event);\">建设银行</span>、国家开发银行、<span onmouseover=\"ShowInfo(this,&quot;03988&quot;,&quot;100&quot;,&quot;-1&quot;,event);\">中国银行</span>、<span onmouseover=\"ShowInfo(this,&quot;01288&quot;,&quot;100&quot;,&quot;-1&quot;,event);\">农业银行</span>等几家大行在尚德贷款较多，且大部分为无抵押的信用贷款。</p>\r\n<p style=\"text-indent: 2em\">据经济参考报(<span class=\"infoMblog\">微博</span>)报道，目前，尚德电力破产已成定局，具体的破产重组方案已获江苏省政府批准，将在20日左右出台。</p>\r\n<p style=\"text-indent: 2em\">从目前的情况来看，&ldquo;国资介入&rdquo;无疑是目前<span onmouseover=\"ShowInfo(this,&quot;STP.N&quot;,&quot;200&quot;,&quot;-1&quot;,event);\">无锡尚德</span>的唯一活路&ldquo;对于尚德电力来说，最好的选择将是为某些资产申请破产保护，并让国有力量进入该企业从而保护特定利益。当然，尚德电力不会全面破产，它的品牌将会一直存在&rdquo;。中国可再生能源协会副理事长孟宪淦指出。</p>\r\n<p style=\"text-indent: 2em\">无锡市国联发展(集团)有限公司(简称&ldquo;无锡国联&rdquo;)将有望接受和主导之后的重组工作，以母公司或者旗下产业投资基金的形式全面接管无锡尚德。据了解，无锡国联成立于1999年5月8日，是无锡市人民政府出资设立并授予国有资产投资主体资格的国有独资企业集团。</p>\r\n<p style=\"text-indent: 2em\">尚德电力由施正荣于 2001年创立，2006年，公司赴美上市，施正荣借此成为中国新首富，并引发了光伏产业一场声势浩大的造富运动。自去年以来，尚德的形势急转直下，截至今年3月份，该公司的负债总额已达到35.82亿美元，资产负债率已高达81.8%，市值从上市之初的49.22亿美元跌到如今的1.49亿美元。</p>\r\n<p style=\"text-indent: 2em\">3月4日，尚德电力发出公告称，施正荣辞去公司董事长职务，由王珊接替公司董事长一职。但是第二天施正荣就发出声明称，董事会撤除其董事长职务是非法无效的，并表示自己以在尚德的股权向银行担保并获取贷款。</p>\r\n');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (53,'text','<p>上世纪30年代&#xff0c;为利用美国技术建造新型军舰&#xff0c;苏联领导人特批从国库划拨50万美元的“行政经费”(在当时是一笔不小的数目)&#xff0c;供一家苏联外贸公司使用&#xff0c;以便打通美国高层关节。但这笔钱最终打了水漂&#xff0c;并连累不少人获罪&#xff0c;俄罗斯《权力》杂志日前刊文披露了这段秘闻。</p><p>　　1924年&#xff0c;苏联在美国注册成立了阿姆外贸集团公司&#xff0c;专门从事苏美贸易。当时美苏尚未正式建交&#xff0c;阿姆外贸集团公司可以说垄断了两国的贸易活动。到了1933年&#xff0c;在苏联的要求下&#xff0c;同时也是为了迎合热心开拓苏联市场的美国工商界人士&#xff0c;美国新总统罗斯福正式承认苏联的合法性。</p><p>　　当时&#xff0c;美国经济仍然萧条。美国商人都清楚&#xff0c;阿姆外贸集团公司的订单就是苏联政府的订单&#xff0c;意味着苏联政府的财政支持。但从另一个角度看&#xff0c;美国人认为&#xff0c;与这家公司签订合同就意味着与一个最敌对的国家开展贸易活动&#xff0c;所以特别警惕向该公司提供先进敏感的军事技术。鉴于此&#xff0c;莫斯科决定建立一个纯美国式的公司&#xff0c;找一个可靠的代理人。</p><p>　　被选中的人叫卡尔普&#xff0c;一个有俄国血统的美国人。卡尔普出生在沙俄时期一个贫穷的裁缝家庭&#xff0c;1911年移居美国后&#xff0c;做过各种杂工&#xff0c;后做起石油生意。上世纪20年代末经济危机爆发后&#xff0c;卡尔普陷入困境&#xff0c;1933年&#xff0c;他不得不把自己的几个加油站低价处理。随后&#xff0c;他决定去莫斯科&#xff0c;看能否找到“在俄罗斯赚美元”的机会。正是在莫斯科&#xff0c;他结识了苏联对外贸易部官员&#xff0c;并受委托开始帮助苏联采购先进设备。</p><p>　　但有不少人对卡尔普的能力表示怀疑。在1936年8月2日向时任苏联国防人民委员伏罗希洛夫的汇报中&#xff0c;苏联驻美国的军事参赞布尔津尤说&#xff1a;“这个人(指卡尔普)行为举止欠稳妥&#xff0c;令人担忧。譬如&#xff0c;他到任何一个地方都随身携带虽说不是官方正式文件但都涉及我国采购设备的清单&#xff0c;甚至公开给一些有业务联系的企业和转售商看这些文件。这样做严重违反保密条款规定。总之&#xff0c;他一有机会就向人喋喋不休地介绍我国需求情况。”这名参赞还提到卡尔普的特别背景苏联人民委员会主席莫洛托夫妻子热姆丘任娜的弟弟。</p><p>　　尽管有人打了“黑报告”&#xff0c;卡尔普仍得到上面的信任。在与阿姆外贸集团公司达成划分采购权限的协议后&#xff0c;他顺利地完成了一些采购任务。卡尔普一有机会就向人们讲他与莫洛托夫的关系&#xff0c;并散布从苏联高层那儿得到的一些重要采购信息&#xff0c;去找他的客商很快便络绎不绝。卡尔普一下子成了一个“神奇”的商人。</p><p>　　1937年5月14日&#xff0c;阿姆外贸集团公司董事会主席罗佐夫向莫斯科发去一份紧急密码文件。文件称&#xff1a;“卡尔普今日向我报告&#xff0c;美国政府已拟订向我出售配置16英寸火炮系统战列舰以及火炮中央控制器的文件。为获得这份文件他需要50万美元用以补偿相关人士。”莫斯科经过反复研究&#xff0c;决定做这个交易。</p><p>　　苏联方面先划拨了30万美元&#xff0c;后来又增补了20万。这笔汇款的收款人一开始定为美国民主党全国委员会的一个重要人物以及总统的助手。此外&#xff0c;苏联人还想将这笔款项交到美国各部委员会负责军工技术产品审批事项的官员手中。事实上&#xff0c;这笔巨额资金最终到了罗斯福总统的儿子手里&#xff0c;为的是通过他将这笔钱送到罗斯福手中。</p><p>　　没想到&#xff0c;卡尔普所办的事情被新闻界曝光了&#xff0c;连续几天美国各大新闻社、报刊都报道说苏联将在美国采购巨额军事装备&#xff0c;很多报刊不惜笔墨将该事件描绘成已经开始执行的订单。此外&#xff0c;在美国国务院办妥向苏联发运军事装备的批文后&#xff0c;涉及采购战列舰相关技术设备的麻烦越来越多。1937年11月3日&#xff0c;莫斯科接到报告说&#xff1a;“卡尔普拿到的批文对采购战列舰专用的涡轮根本不起作用。通用电气作为海军部供应发动机的主要商家&#xff0c;根本不想接受我们的货物订单&#xff0c;而且还必须得到海军部的正式批文&amp;hellip;&amp;hellip;造船厂在媒体的一阵旋风式报道后&#xff0c;也想从苏联贸易代表处拿到战列舰设计费用&amp;hellip;&amp;hellip;”</p><p>　　据档案记载&#xff0c;莫斯科决定还是让卡尔普去做说客&#xff0c;并由苏联商贸代表特罗扬诺夫斯基约见美国总统。1937年11月27日&#xff0c;特罗扬诺夫斯基见到了罗斯福。据他事后向莫洛托夫报告&#xff0c;他向罗斯福抱怨美国海军部对苏方订单态度冷淡&#xff0c;罗斯福则表示会给负责海军事务的长官下指示&#xff0c;总统还建议直接在美国建船坞&#xff0c;并称会按照美国海军现役军舰标准为苏联设计建造战舰专用装甲钢板。</p><p>　　事实上&#xff0c;这件事没有任何转机&#xff0c;在长时间的摩擦后&#xff0c;苏联人既没有得到驱逐舰&#xff0c;也没得到有关驱逐舰的任何技术设计图纸。经过苦口婆心的工作&#xff0c;美国人最后同意苏方带走有关建造战列舰的技术图纸。但苏联专家鉴定后表示&#xff0c;这些图纸完全不符合苏方的技术要求。</p><p>　　1938年&#xff0c;美国国会成立了一个专门调查反美思潮的机构&#xff0c;卡尔普引起该机构的高度关注。他后来供出使用50万美元的细节&#xff0c;他本人从中擅自扣留了 10万美元。卡尔普的姐姐热姆丘任娜也未能幸免&#xff0c;1939年被控与“人民公敌”合作。1940年&#xff0c;阿姆外贸集团公司董事会主席罗佐夫被捕&#xff0c;被指控从事间谍活动&#xff0c;不断向卡尔普拨付苏共政治局决定的境外行政支出款项。1945年年底&#xff0c;美苏同盟关系名存实亡&#xff0c;这起让苏联人恼火的采购事件也被认为是源头之一。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (54,'text','<p style=\"text-indent: 2em\">北京时间3月19日凌晨消息，华尔街日报中国实时报栏目周一文章称，两名经济学家指出，美国房地产市场崩溃之前曾有过的三个警示性信号已经在中国出现，这意味着中国只有非常有限的时间来摆脱困境。</p>\r\n<p style=\"text-indent: 2em\">文章指出，在野村证券于上周六公布的一份报告中，经济学家张智威和陈家瑶指出，物业价格的上涨、杠杆化的快速积累和潜在增长率的下滑都可能导致系统性的危机。</p>\r\n<p style=\"text-indent: 2em\">这份报告引用凯斯席勒房价指数指出，美国的房价在2001年到2006年之间飙升了84%。而作为野村证券经济学家的张智威和陈家瑶对中国的官方指数提出了质疑，并认为这组数据指出的2004年到2012年之间主要大城市房价113%的&ldquo;良性增长&rdquo;并不准确。他们认为，这个数据过于宽泛，包括了全国范围内老旧和低品质的住房。与之相比，近期一份考虑到这种质量差异的学术报告认为，仅2004年到2009年之间，中国房价的涨幅就已经是250%。</p>\r\n<p style=\"text-indent: 2em\">张智威和陈家瑶在报告中写道，&ldquo;中国政府显然已经认识到房地产行业的风险，在过去多年推出了一系列逐渐收紧政策以控制物业价格的措施。市场的模式是，在紧缩政策推出之初下降，然后反弹，这意味着风险并没有得到缓解。&rdquo;依赖出售土地作为主要收入来源的地方政府可能在房地产开发商因为市场崩溃而受打击时遭受同等的重创。这些问题会轻易地在银行系统找到突破口&mdash;&mdash;估算显示，中国银行业14.1%的流通贷款发放给了地方政府的融资平台，6.2%授予了房地产开发商。</p>\r\n<p style=\"text-indent: 2em\">野村证券认为，中国还是有时间来避免系统性的金融危机，只要政府不畏惧马上启动政策紧缩。不过这一做法的代价将是2013年经济增长前景的受压制&mdash;&mdash;虽然最终的实际增长还是有望在2013年上半年达到8.1%，在下半年维持7.3%。报告强调，早期的紧缩可能引发可以控制的债务违约情况。</p>\r\n<p style=\"text-indent: 2em\">报告指出，另一个结果是继续目前的宽松政策，实现超过8%的2013年经济增长，然后面对可能在2014年初就出现了市场崩溃。而这种崩溃将会很快蔓延到整个系统，迫使政府介入对银行和地方政府进行救援，可能需要出售公共资产来解决混乱局面。</p>\r\n<p style=\"text-indent: 2em\">报告认为，最终导致中国出现危机可能性增加的因素是潜在增长率的下降。这并不是一个可以轻松计算的数字，它代表了经济在不产生额外通货膨胀情况下可以实现的最大增长速度。即使是最乐观的分析师也同意野村证券的判断，包括适龄劳动人口数量的萎缩等等因素都造成了这个指标的下降。这使得中国决策者刺激经济增长的回旋余地相比过去大大减少。</p>\r\n<p style=\"text-indent: 2em\">张智威和陈家瑶指出，&ldquo;金融危机通常跟随技术革命和所谓的经济奇迹，因为投资者和决策者开始过高估计经济的潜在增长能力。决策者可能错误解读潜在增长的结构性放缓，将它视作周期性的现象，并试图使用扩张性政策来刺激增长，这实际上为过热和最终痛苦的调整埋下了种子。&rdquo;</p>\r\n');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (73,'text','<p> </p><p style=\"text-align:center\"><img alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201308/20130813070348_9dcm1m.jpg\" /></p><p style=\"text-align:center\">杨钰莹</p><p>“女人不是因为美丽而可爱&#xff0c;是因为可爱而美丽。”俄罗斯文学家列夫&amp;middot;托尔斯泰在《战争与和平》中的这句话&#xff0c;用在杨钰莹身上正合适。白裙子&#xff0c;长头发&#xff0c;低声说&#xff0c;轻声笑&#xff0c;杨钰莹完全保留着少女的神态。10多年的岁月&#xff0c;惊涛骇浪的往事&#xff0c;在她身上仿佛没留下痕迹。</p><p>但交谈久了&#xff0c;还是不一样。你会发现&#xff0c;她从前接受采访时那些活泼的小动作&#xff0c;比如扬扬眉、撇撇嘴、甩甩头发&#xff0c;现在全都没有了。以前的她像一幅动态画&#xff0c;现在变成了一幅静物画。</p><p>在离开公众视线长达10年后&#xff0c;这次回归&#xff0c;她闯进了一个全新的领域&amp;mdash;&amp;mdash;主持人。公众和媒体对她的主持人形象充满好感&#xff0c;那些纠缠不放的负面消息也被人们淡忘了。</p><p>很难想象&#xff0c;她是怎么走过低谷和风浪的。她自己的解释是&#xff1a;“生活中不仅有幸福和快乐&#xff0c;更有悲伤和无奈&#xff0c;都要笑纳&#xff0c;因为它们都是生活的组成部分。”</p><p>“柔软”面对批评</p><p>2013年初&#xff0c;杨钰莹正筹备自己的演唱会&#xff0c;突然接到天津卫视新节目《天下无双》(在线观看)邀约&#xff1a;“来给我们做主持人吧。”对她而言&#xff0c;这是机会&#xff0c;也是挑战。考虑了将近一个月&#xff0c;反复看了这档节目的国外原版录像&#xff0c;终于&#xff0c;在节目录制的前一天&#xff0c;杨钰莹答应了。</p><p>谁都没有想到&#xff0c;这一次会反响这么好。第一期节目播出后&#xff0c;获得了全国收视率第四的好成绩。没多久&#xff0c;杨钰莹和主持界的老前辈赵忠祥同台&#xff0c;也没露怯。赵忠祥夸她“水平比央视有些女主持人都要好&#xff0c;可以推荐她去主持春晚了”。杨钰莹庆幸接了这个工作&#xff0c;“为自己打开了新的一扇窗”。</p><p>环球人物杂志&#xff1a;这3个月的节目&#xff0c;看得出你进步很大。有没有请老师来辅导发声、走台这些技巧&#xff1f;</p><p>杨钰莹&#xff1a;没有呀。我每每在电视上看到自己的不足&#xff0c;都会说&#xff1a;“哎呀&#xff01;怎么没有人教我一下呀&#xff01;”(笑)有时候发现自己在节目中又忘了说“硬口”(指主持人必须要讲的话)&#xff0c;就会特别着急。</p><p>环球人物杂志&#xff1a;现在给自己的主持工作打多少分&#xff1f;</p><p>杨钰莹&#xff1a;我还是多鼓励自己吧&#xff0c;打80分。不过&#xff0c;我给自己的态度打100分。任何时候&#xff0c;工作态度永远比工作能力更重要。</p><p>环球人物杂志&#xff1a;也有一些人批评你主持风格“太嗲”&#xff0c;“气质胜过技巧”。</p><p>杨钰莹&#xff1a;其实我这个人一点都不“嗲”&#xff0c;有时候还挺爷们儿的。至于技巧&#xff0c;我就是没有技巧&#xff0c;不过还算有点气质(笑)。</p><p>对于这些批评&#xff0c;我会非常柔软地去面对。我看到那些中肯的意见&#xff0c;会在心底说&#xff1a;“谢谢你&#xff0c;我会努力地改进。”</p><p>环球人物杂志&#xff1a;谁也没有想到你做主持人这么成功&#xff0c;以后还会唱歌吗&#xff1f;</p><p>杨钰莹&#xff1a;当然会。没有歌唱事业做基础&#xff0c;主持也就不必了。我首先还是歌手&#xff0c;而且说话和唱歌相比&#xff0c;我还是唱歌的水平更好。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (83,'text','<p>搜狗输入法作为国内汉字拼音输入法的领导者，搜狗输入法率先实现了输入法与互联网的结合。基于搜狗搜索引擎技术，对中文词库有突破性发展，开创了新一代中文输入法。即时高效地更新热门词库，大幅提升了输入效率，让输入速度产生了质的飞跃。在词库的广度、词语的准确度上，搜狗输入法都在行业内遥遥领先。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (93,'text','<p><span style=\"line-height: 22px; font-family: tahoma, Arial, 宋体, 微软雅黑; color: rgb(0,0,0)\">免费的即时聊天软件，给上网带来更多沟通乐趣。腾讯QQ2013年度皮肤呈现视觉盛宴，皮肤编辑器实现个性化面板创意；QQ应用盒子全新呈现，丰富应用满足多彩生活；QQ短信首度面世，畅享无处不在的沟通体验；炫彩字体，炫出聊天个性与风采；QQ支持自定义标签，标签顺序随心换。提示：如果您正在运行着腾讯QQ或者TM ,请退出后再进行一键安装。</span></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (95,'packingList','');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (95,'introduce','<p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img data-lazyload=\"done\" alt=\"\" id=\"a5693828a3a04b0d877ddd460bf53d88\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1747/1/1374760492/293014/1a379bbe/55e40ff2Neebb04f7.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img data-lazyload=\"done\" alt=\"\" id=\"c4d51a6689164e3dbadd563a223786ea\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1858/144/732419751/380880/dbbf7cc6/56278603N2a5330c0.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/>&nbsp;</p><p style=\"text-align: center;\"><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"2342f7f158c142cd9e728845d838bf84\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1711/224/1305174596/34549/c72fbea9/55e40ff1Nec82627c.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"4ed873071bd2447fbee4b3f3dab9b0be\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1822/167/1254639643/57081/66af1c0f/55e40ff1N3c0a09aa.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"698a373574754e0787bf298c8bce3f9d\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1720/207/1320749389/234778/eaeb703f/55e40ff3N0cfe09b2.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"4749569553af4adc82be75e63a1c6ed7\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1687/5/1334906465/209392/db60181b/55e40ff3N36ead1a6.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"d688008f69ce4387a2591c79469e2b64\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1660/31/1213006365/275884/a6aa7b5/55e40ff3N224dc718.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"2f4fafd11080484783550f9a5673420f\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1465/232/1193538325/301399/20f8d0d0/55e40ff4N9fc26363.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"ca84b367f26f4c6794df5af23d2b9be6\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1732/13/1297420571/216091/2f85644e/55e40ff4Nb9a5e3ea.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"435bd615683c4d018b704d26c58089d7\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1750/222/1320035840/234013/8db26305/55e40ff5Nf4667e39.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"55168fff0dad484ba50410a6ae6ac5f6\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1585/237/1153174124/194654/3b521bc6/55e40ff7N5b7596d8.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"bbd83d4dce5e48958b91d21de7b0c5a4\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1765/301/1220883678/196958/5f371155/55e40ff7N6903fdd9.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"0decaaaebeec4c138ecc2d50530f44e2\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1738/203/1304148672/274054/f87b8cc/55e40ff8N5402be3c.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/></p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img data-lazyload=\"done\" alt=\"\" id=\"86a8241acd714509a99918ba5703f35f\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1807/210/1267058754/256679/a1af9431/55e40ff8N3a22f7b9.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img data-lazyload=\"done\" alt=\"\" id=\"096c49821acd4d7ea02f7ba2eb3f1832\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1753/207/1299312810/232077/62834e48/55e40ff9N0bb2c530.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/>&nbsp;</p><p><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/></p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><br/></p><p style=\"text-align: center;\"><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"29f69787a7a74e6abcae802ec87d26f0\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1852/171/735451624/7840/45313c1a/56278603N7446cf7b.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"ca9ccdb796ab4665bdb14c5f7f710fb9\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2458/148/751567036/325430/4ec242af/56278604Naca09f59.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"344c14a0f0f64c6f866c4dc52e0bce7d\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2449/146/748205277/295252/c1d21f3a/56278604Ne074cb4f.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"c1ceec90bfb041439e9bd736c3316ef9\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2263/179/745449634/396704/3750b068/56278605N2e158391.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"41b48f853bbe473393139040f8838a09\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1867/150/765158623/367775/8b533886/56278605N89257074.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"aa4e3b7f266448869e6feeb38140b4ab\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2296/170/750507266/235562/b6c5ccc0/56278606Na7a4cbad.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"78a7a46230824f72a72f8c6525ecf8ec\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1777/211/2145478187/223967/f1ff1788/56278606Need11bcd.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"7fcd519bada54993967e1e57a724aae8\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1831/38/2151532147/347324/ef960c42/56278606N0bd39ccb.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"295a226bba7a4de9ae0297180f7f097e\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1786/184/2070965537/333458/64135c8c/56278607N7dd7ae04.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"ee4a4d01f82740c98cb693859738bb98\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1987/146/763521873/421265/2ecc8c47/56278607N6cbfed78.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"1f3d639ce86f4463bd8b71116a99c9f9\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1972/141/744585713/415267/4413905a/56278608Na56c31f9.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"a974062f93ad4addbfc9b71a9026373e\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1759/72/2164854068/7956/4fbf8627/56278608N146a1e9b.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"bab4caac25464ed4b413fd024097b07b\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2152/145/739094594/269195/434e09f1/56278609N96b575ac.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"5900dc230f7a4d7ca52788a766db9c67\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2407/154/756029562/339211/fdfbae06/56278609Nc60d0fd5.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"06149f9a31f74321b5010138008b4948\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1948/139/742183358/388001/5f46b68c/5627860aN26fb16ac.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"301fdba933ac4a1da7244085c15843db\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1996/137/746296342/432577/704c98c6/5627860aN82bd64e3.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"7ff6775a64a54e1db15ef24b75389485\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1954/141/720554933/309219/21691b0c/5627860bN4e7aa2f5.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"576c2503190e49ce86fc24d15f13d000\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1906/114/757577286/304887/a12ddbdf/5627860bN687a9a23.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"77f68ab2ce5b4c2cae466778fa6a7d2a\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2236/162/747953070/415370/3de34895/5627860cN2006c076.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"b56cff2df49640efa12b6b3bb403233a\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1891/149/747829170/8947/61d2d76/5627860cNe66cbf78.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"2c991bf43f8d4ba9926153694aac3f11\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2503/145/739347760/272866/efcc166a/5627860cNd1e69000.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"c0dfb488deb24ed9bd8656558986002c\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2191/178/739439528/325184/f03164c4/5627860dN75c90d6f.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"6e77bdef1c9746038b32857277b30eb8\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2221/148/762081824/377956/7b313620/5627860eN596aeb55.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"40b130bee2fd4d1cb5909e90ec79c60e\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2260/162/768897669/368479/4b717288/5627860eN9fe79ea0.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"a21d35cc33b0449697ba96e430c2f60f\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t1819/74/2122480230/271938/cb205546/5627860fNf9b2860a.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><img data-lazyload=\"done\" alt=\"\" id=\"d60b927d97bc4fcea301d1023bd200c5\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2062/157/752585558/204739/54f26a01/5627860fN2bb50e64.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/><span style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; background-color: rgb(255, 255, 255);\">&nbsp;</span><br style=\"color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"/></p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(102, 102, 102); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img data-lazyload=\"done\" alt=\"\" id=\"c8eb6bb03edd4dc19f1ed01491875484\r\n\" src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2284/172/761677196/383807/194ac595/5627860fNc35b502e.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (95,'services','');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (95,'specification','');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (94,'introduce','<p style=\"text-align: center;\"><img data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224141955_cvkonsqa3x.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Helvetica, sans-serif; font-size: 14px; line-height: 23px; text-align: center; white-space: normal; background-color: rgb(255, 255, 255);\"/></p><p style=\"text-align: center;\"><img data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224142225_djhxuqntku.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p><p style=\"text-align: center;\"><img data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224142227_3r33u7ul8x.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p><p style=\"text-align: center;\"><img data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224142231_nysw7hss1x.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p><p style=\"text-align: center;\"><img data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224142235_sddhjwhpyg.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (94,'packingList','<p>显示器x1 底座x1 DVI信号线x1 说明书x1 保修卡x1 合格证x1</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (94,'services','<p>本产品全国联保，享受三包服务，质保期为：三年质保<br/>如因质量问题或故障，凭厂商维修中心或特约维修点的质量检测证明，享受7日内退货，15日内换货，15日以上在质保期内享受免费保修等三包服务！<br/>(注:如厂家在商品介绍中有售后保障的说明,则此商品按照厂家说明执行售后保障服务。) 售后服务电话：400-0099-998</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (94,'specification','<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" class=\"Ptable\"><tbody><tr class=\"firstRow\"><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">主体</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">品牌</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">惠科 HKC</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">型号</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">Q320</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">外观颜色</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">白色</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">颜色</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">银色</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">显示</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">面板类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">AMVA面板</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">面板尺寸</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">32英寸</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">宽屏</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">是</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">屏幕比例</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">16:9</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">最佳分辨率</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">2560x1440</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">响应时间</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">GTG 5ms</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">点距</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">0.277mm</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">色数</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">10.7亿色</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">亮度</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">250cd/m2</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">对比度</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">50000000：1（DCR）</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">可视角度</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">H：176/V：176</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">LED背光</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">是</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">接口</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">VGA接口</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">有</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">DVI接口</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">有</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">HDMI接口</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">有</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">VGA</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">1个</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">DVI</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">1个</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">HDMI</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">1个</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">规格</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">电源</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">内置</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">尺寸</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">729x504x52mm 整机尺寸（不含包装）</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">重量</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">8.07kg</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">是否支持壁挂</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">是</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">壁挂规格</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">100x100mm</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">底座</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">普通</td></tr></tbody></table><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (97,'packingList','iPhone 6S Plus*1；具有线控功能和麦克风的 Apple EarPods*1；Lightning to USB 连接线*1；USB 电源适配器*1；资料');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (97,'introduce','<table width=\"750\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr class=\"firstRow\"><td><img data-lazyload=\"done\" width=\"750\" height=\"2113\" alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224122112_6ibijnem1j.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></td></tr></tbody></table><table width=\"750\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr class=\"firstRow\"><td><img data-lazyload=\"done\" width=\"750\" height=\"2001\" alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224122114_nf2xlg9cnm.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></td></tr></tbody></table><table width=\"750\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr class=\"firstRow\"><td><img data-lazyload=\"done\" width=\"750\" height=\"1824\" border=\"0\" usemap=\"#MapiPhone\" alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224122116_yejoosrut1.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/><br/><br/></td></tr></tbody></table><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (97,'services','本产品全国联保，享受三包服务，质保期为：一年质保<br/>如因质量问题或故障，凭厂商维修中心或特约维修点的质量检测证明，享受7日内退货，15日内换货，15日以上在质保期内享受免费保修等三包服务！<br/>(注:如厂家在商品介绍中有售后保障的说明,则此商品按照厂家说明执行售后保障服务。) 您可以查询本品牌在各地售后服务中心的联系方式，请点击这儿查询......<br/><br/>品牌官方网站：http://www.apple.com.cn/<br/>售后服务电话：4006668800');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (97,'specification','<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" class=\"Ptable\"><tbody><tr class=\"firstRow\"><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">主体</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">品牌</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">Apple</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">型号</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">iPhone 6S Plus</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">颜色</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">玫瑰金色</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">上市年份</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">2015年</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">上市月份</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">9月</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">输入方式</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">触控</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">智能机</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">是</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">操作系统</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">苹果（IOS）</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">操作系统版本</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">iOS</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">CPU品牌</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">苹果</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">CPU说明</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">64 位架构的 A9 芯片，嵌入式 M9 运动协处理器</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">运营商标志或内容</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">无</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">网络</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">4G网络制式</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">移动4G(TD-LTE)/联通4G(FDD-LTE)/电信4G(FDD-LTE)</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">3G网络制式</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">移动3G(TD-SCDMA)/联通3G(WCDMA)/电信3G（CDMA2000）</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">2G网络制式</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">移动2G/联通2G(GSM)/电信2G(CDMA)</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">网络频率</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">FDD-LTE (频段 1, 2, 3, 4, 5, 7, 8, 12, 13, 17, 18, 19, 20, 25, 26, 27, 28, 29)<br/>TD-LTE (频段 38, 39, 40, 41)<br/>TD-SCDMA 1900 (F), 2000 (A)<br/>CDMA EV-DO Rev. A (800, 1700/2100, 1900, 2100 MHz)<br/>UMTS(WCDMA)/HSPA+/DC-HSDPA (850, 900, 1700/2100, 1900, 2100 MHz)<br/>GSM/EDGE (850, 900, 1800, 1900 MHz)</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">存储</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">机身内存</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">64GB ROM</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">储存卡类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">不支持</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">显示</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">屏幕尺寸</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">5.5英寸</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">触摸屏</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">具备 3D Touch 技术的 Retina HD 显示屏<br/>1300：1 对比度 (标准)<br/>500 cd/m2 最大亮度 (标准)<br/>全 sRGB 标准<br/>支持广阔视角的双域像素<br/>正面采用防油渍防指纹涂层<br/>支持多种语言文字同时显示<br/>放大显示<br/>便捷访问功能</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">分辨率</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">1920 x 1080</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">PPI</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">401</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">感应器</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">GPS模块</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">重力感应</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">光线感应</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">距离感应</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">陀螺仪</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">摄像功能</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">后置摄像头</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">1200万像素</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">前置摄像头</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">500万像素</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">闪光灯</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">True Tone 闪光灯</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">自动对焦</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">Focus Pixels 自动对焦</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">其他性能</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">全新 1200 万像素 iSight 摄像头，单个像素尺寸为 1.22 微米<br/>Live Photos<br/>Focus Pixels 自动对焦<br/>光学图像防抖功能 (仅限于 iPhone 6s Plus)<br/>True Tone 闪光灯<br/>全景模式 (高达 6300 万像素)<br/>自动 HDR 照片<br/>曝光控制<br/>连拍快照模式<br/>计时模式<br/>F/2.2 光圈<br/>五镜式镜头<br/>混合红外线滤镜<br/>背照式感光元件<br/>蓝宝石水晶镜头表面<br/>自动图像防抖功能<br/>优化的局部色调映射功能<br/>优化的降噪功能<br/>面部识别功能<br/>照片地理标记功能</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">娱乐功能</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">音乐播放</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">AAC (8 至 320 Kbps)、Protected AAC (来自 iTunes Store)、HE-AAC、MP3 (8 至 320 Kbps)、MP3 VBR、Audible (格式 2、3、4，Audible Enhanced Audio、AAX 与 AAX+)、Apple Lossless、AIFF 与 WAV</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">视频播放</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">H.264 视频，最高可达 1080p，60 fps；High Profile level 4.2 和 AAC-LC 音频，最高可达 160 Kbps，48kHz；立体声音频，文件格式为 .m4v、.mp4 和 .mov；MPEG-4 视频，最高可达 2.5 Mbps，640 x 480 像素，30 fps；Simple Profile 和 AAC-LC 音频，每声道最高可达 160 Kbps，48kHz，立体声音频，文件格式为 .m4v, .mp4 和 .mov；Motion JPEG (M-JPEG)，最高可达 35 Mbps，1280 x 720 像素，30 fps；ulaw 音频和 PCM 立体声音频，文件格式为 .avi</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">传输功能</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">Wi-Fi</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">蓝牙</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">NFC(近场通讯)</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">WIFI热点</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">其他</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">SIM卡尺寸</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">Nano SIM卡</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">电池类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">锂电池</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">电池容量（mAh）</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">理论待机时间长达16天</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">电池更换</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">不支持</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">理论通话时间（小时）</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">使用 3G 网络时长达 24 小时</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">理论待机时间（小时）</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">长达 16 天 (384 小时)</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">数据线</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">Lightning</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">耳机接口</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">3.5mm</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">机身尺寸（mm）</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">158.2*77.9*7.3</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">机身重量（g）</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">192</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">其他</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">* 要识别你的 iPhone 的型号，请参阅 http：//support.apple.com/kb/HT3939。4G LTE Advanced 和 4G LTE 功能适用于特定国家或地区的特定运营商。有关 4G LTE 支持的详情，请联系你的运营商并查看 www.apple.com/iphone/LTE/。<br/>1. 1GB = 10 亿字节； 内置主内存容量 16GB / 64GB/ 128GB (依所购机型而定)；格式化之后的实际容量可能较小。<br/>2. 实际尺寸和重量依配置和制造工艺的不同可能有所差异。<br/>3. FaceTime 视频通话要求通话双方使用支持 FaceTime 的设备和无线网络连接。能否通过蜂窝网络使用此功能，取决于运营商政策；可能需要支付数据费用。<br/>4. Siri 可能仅支持部分语言或地区，并且功能可能因地区而异。需使用互联网接入。可能需要支付蜂窝网络数据费用。<br/>5. 所有电池性能信息取决于网络设置和许多其他因素，实际结果可能有所不同。电池充电周期次数有限，最终可能需由 Apple 服务提供商更换。 电池使用时间和充电周期次数依设置和使用情况的不同而可能有所差异。详情请参阅 http：//www.apple.com/cn/batteries/ 和 http：//www.apple.com/cn/iphone/battery.html。<br/>6. 2013 年 9 月 1 日或之后初次激活且符合条件，并兼容 iOS 9 的设备，可从 App Store 免费下载 iMovie、Pages、Numbers 和 Keynote。2014 年 9 月 1 日或之后初次激活且符合条件，并兼容 iOS 9 的设备，可从 App Store 免费下载 GarageBand。请参阅 www.apple.com/cn/ios/whats-new/ 了解兼容 iOS 9 的设备。下载 app 需要使用 Apple ID。<br/>7. 推荐使用无线宽带网络；可能需要付费。<br/>8. 基于接收方和 app 的自定义建议功能不适用于中文 (简体、繁体) 和日语。</td></tr></tbody></table><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (98,'packingList','<p>索尼RX100 M2 ×1、NP-BX1锂电池?×1、电源适配器 ×1、Micro USB线 ×1、使用说明书 ×1、保修卡 ×1</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (98,'introduce','<p style=\"text-align: center;\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224121248_wfr3bbl7c2.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Helvetica, sans-serif; font-size: 14px; line-height: 23px; text-align: center; white-space: normal; background-color: rgb(255, 255, 255);\"/></p><p style=\"white-space: normal; text-align: center;\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224121623_6guvkxkei5.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p><p style=\"white-space: normal; text-align: center;\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224121626_4rsrj1sbhg.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p><p style=\"white-space: normal; text-align: center;\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224121627_2sxs6tcwqv.png\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p><p style=\"white-space: normal; text-align: center;\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224121635_ioeks5jqwj.png\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (98,'services','<p>本产品全国联保，享受三包服务，质保期为：一年质保 品牌官方网站：http://www.sony.com.cn/ 售后服务电话：400-810-9000</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (98,'specification','<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" class=\"Ptable\"><tbody><tr class=\"firstRow\"><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">基本参数</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">品牌</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">索尼 SONY</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">系列</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">R</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">颜色</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">黑色</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">数码相机类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">卡片机</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">光学变焦倍数</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">0-4倍</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">机身材质</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">合成金属</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">总像素</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">1800万以上</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">有效像素</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">1400-1599万</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">数码变焦倍数</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">更高</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">操作模式</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">全自动</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">传感器类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">CMOS</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">传感器尺寸</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">其它</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">短片拍摄</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">1920x1080</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">屏幕参数</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">液晶屏尺寸</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">3.0英寸</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">液晶屏比例</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">其它</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">触摸屏</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">不支持</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">旋转屏</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">镜头参数</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">镜头类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">伸缩式</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">变焦方式</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">光学变焦,数码变焦</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">对焦方式</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">自动对焦</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">性能参数</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">连拍</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">自拍</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">闪光灯参数</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">机身闪光灯</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">内置闪光灯</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">外接闪光灯</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">存储及连接参数</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">存储介质</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">SD卡；SDHC卡；SDXC卡</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">附件及电源参数</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">电池类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">锂电池</td></tr></tbody></table><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (99,'text','<p>&nbsp;</p><p>8月12日电 (记者 王欢)美国联邦公报最新公布的数据显示，2013年第二季度放弃美国国籍的人数再创新高，暴增至1131人，比去年同期的189人多出5倍。美国媒体称，美国政府为了增加税收应对拮据的财政状况，准备实施更严格的资产申报规定，使得放弃美国公民身份或绿卡的人数持续增长。</p><p>　　报告显示，2013年到6月为止的3个月里，旅居海外的美国人向使馆申请放弃国籍、同时也放弃了美国政府税单的人数攀升到1131人，虽然在超过600万旅居海外的美国人中只占少数，但这是国税局自1998年开始公布名单以来放弃美国国籍人数最多的一个季度。今年上半年，共有1810人放弃美国国籍，是2008年全年235人的近8倍。照此速度发展，2013年可能成为“告别美国”具有里程碑意义的一年。</p><p>　　据彭博社报道，在经济合作与发展组织中，唯有美国规定无论居住地是何处，美国公民都需向国家缴税。国会数据指出，美国每年因公民逃税造成的损失估计高达一亿美元。致力削减预算赤字的奥巴马政府，希望通过追查纳税人利用海外资产逃税的行为，增加国库收入。</p><p>　　美国2010年通过的《外国账户税收遵从法》(FATCA)一旦实施，资产申报要求将会更严格。按照FATCA的条规，外国金融机构必须向美国税务局提供美国纳税人的资料，包括他们持有的外国公司实质性所有者权益。瑞士和英国等许多主要银行国家，已经同意配合美国政府披露这些资料。未向美国税务局申报足够资料的银行客户，银行可从他们账户中扣减“与美国相关付款”款额的30%。</p><p>　　美国国会税收联合委员会根据这项税务法追税，未来10年估计可为政府增加87亿美元的税收。</p><p>　　“随着2014年7月《外国账户税收遵从法》执行日期的临近，越来越多的美国人开始意识到他们有义务向美国纳税申报。”在瑞士苏黎世工作的美国税务律师马修(Matthew Ledvina)表示，“一旦知道了这一点，他们就会决定放弃美国国籍。”</p><p>　　2012年，共有包括社会名流兼词曲作家丹尼斯&amp;middot;里奇(Denise Rich)以及“脸书”(Facebook)创始人之一爱德华多&amp;middot;萨韦林(Eduardo Saverin)在内的932名美国人放弃了美国国籍。萨韦林搬到了不征收资本利得税的新加坡；而作为亿万富翁前妻的里奇则将节省数百万美元的税费。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (100,'text','<p style=\"text-align:center;\"><img alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201308/20130813080255_pyeqx6.jpg\" style=\"width: 550px; height: 439px; \" /></p>\r\n<p>8月12日电 (记者 周兆军)英国和西班牙围绕直布罗陀的主权争议升级，12日，英国皇家海军的的一支快速反应部队前往地中海进行年度演习，包括&ldquo;光辉&rdquo;号航母在内的多艘军舰将前往直布罗陀海域。</p>\r\n<p>英国国防部宣布，包括&ldquo;光辉&rdquo;号航空母舰、两艘护卫舰和辅助舰只在内的10艘军舰前往地中海进行演习。英国国防部强调这次演习&ldquo;早有计划&rdquo;，属于&ldquo;例行部署&rdquo;，其发言人表示，直布罗陀是英国防卫的战略基地，因此英国海军全年都有军舰前往直布罗陀海域，这属于例行部署的一部分，派出军舰的数量和级别以及海军士兵人数都与以往一样，演习与直布罗陀争端没有关联。</p>\r\n<p>西班牙外交部表示，英方事先曾向西班牙提出申请军舰在直布罗陀海峡附近的罗塔(Rota)海军基地停靠，并获得西班牙的许可。不过，西班牙媒体认为，这次演习是英国的恐吓行为。</p>\r\n<p>在四个月的演习中，英国军舰还会前往葡萄牙、西班牙、土耳其、马耳他等国的港口进行演练，最终目的地是中东海湾地区。</p>\r\n<p>当天，英国首相府发言人表示，西班牙卫兵执行严格的安全检查，导致西班牙与直布罗陀边境交通堵塞，是&ldquo;有政治动机的、完全不适当的行为&rdquo;。卡梅伦首相对西班牙未能取消多余的边境检查感到失望，英国正考虑采取法律行动。</p>\r\n<p>西班牙方面也针锋相对，外交部发言人表示，西班牙正考虑将直布罗陀争议诉至联合国与海牙国际法庭等国际机构，并考虑与阿根廷结成统一阵线。阿根廷与英国在马尔维纳斯群岛(英国称福克兰群岛)存在主权争议。</p>\r\n');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (126,'text','<p><!-- publish_helper name=\'原始正文\' p_id=\'1\' t_id=\'1\' d_id=\'29757426\' f_id=\'3\' --></p><p>　　每日经济新闻记者 黄俊玲 发自北京</p><p>　　千呼万唤，以房养老保险方案终于要成为现实。</p><p>　　《每日经济新闻》记者昨日(3月20日)获悉，为贯彻落实国务院《关于加快发展养老服务业的若干意见》有关要求，丰富养老保障方式的新途径，保监会决定开展老年人住房反向抵押养老保险试点。</p><p>　　同时，保监会近日还向各家人身保险公司下发了《关于开展老年人住房反向抵押养老保险试点的指导意见(征求意见稿)》(以下简称《征求意见稿》)，并拟在北京、上海、广州和武汉四地率先开展试点，对参与保险的人群和从事该业务的保险公司都有明确的要求。</p><p>　　<strong>从业险企注册资本不低于20亿/</strong></p><p>　　据记者了解，所谓&amp;ldquo;老年人住房反向抵押养老保险&amp;rdquo;，是一种将住房抵押与终身年金相结合的创新型商业养老保险业务，即拥有房屋完全产权的老年人，将其房屋抵押给保险公司，继续拥有房屋居住权，并按照约定条件领取养老金直至身故；老人身故后，保险公司获得抵押房屋处置权，处置所得将优先用于偿付养老保险相关费用。而这项业务的简称，就是通常说的以房养老。</p><p>　　保监会表示，当前我国缺少将社会存量资产转化为养老资源的有效手段。开展试点，盘活老年人房产，有利于丰富老年人的养老选择。据介绍，反向抵押养老保险属于商业保险范畴。开展试点，在不影响老年人既有养老福利的前提下，增加了一种新的养老方式，老年人可根据个人生活状况和养老需求自愿投保。</p><p>　　《征求意见稿》中明确表示，以房养老保险产品的投保人群应为60岁以上拥有房屋完全独立产权的老年人。同时，保险公司参与以房养老试点也是有要求的，即包括开业满5年，同时注册资本不低于20亿元，申请试点时上一年度末及最近季度末的偿付能力充足率不低于120%等。</p><p>　　根据保险公司对于投保人所抵押房屋增值的处理方式不同，试点产品将分为参与型反向抵押养老保险产品和非参与型反向抵押养老保险产品。其中，参与型产品是指保险公司可参与分享房屋增值收益，通过定期评估，对投保人所抵押房屋价值增长部分，依照合同约定在投保人和保险公司之间进行分配。而非参与型产品，则指保险公司不参与分享房屋增值收益，抵押房屋价值增长全部归属于投保人。</p><p>　　有不愿具名的险企人士向《每日经济新闻》记者透露，如果是参与型的话，假如以后房价上涨的话，老人也可以每个月多拿到一定的增值年金。如果是非参与型的话，每个月的领取金额将是固定的，在房产增值之后，其领取金额也不提高，而房产增值部分所带的金额就给到继承人了。非参与型从操作层面可能会更简单，更像一个非固定期限的贷款。对于具体的保险产品来说，各家保险公司可以根据这两个大的框架，来自主来设计产品。</p>[PageBreak][/PageBreak]<p>　　<strong>单笔合同初次贷款不超500万/</strong></p><p>　　此次《征求意见稿》对保险公司参与以房养老业务试点是有总量限制的。该《征求意见稿》要求试点期间，单个保险公司开展试点业务，接受抵押房屋的评估价值合计不得超过：4%&amp;times;上一年末总资产不超过200亿元的部分+0.2%&amp;times;上一年末总资产超过200亿元的部分。单个反向抵押养老保险合同的初次抵押贷款金额不得超过500万元。</p><p>　　若以一家保险公司的总资产为1000亿元为例，其试点以房养老接受抵押房屋的评估价值合计不得超过4%&amp;times;200亿元+0.2%&amp;times;800亿元=9.6亿元。事实上，寿险公司中总资产超过1000亿的公司并不多。</p><p>　　事实上，以房养老的开展作为一项新事务，对客户和保险公司而言，都面临挑战。</p><p>　　在客户层面，首先解决的是观念问题，反向抵押养老保险是一项新生事物，社会认可度和接受度有待提升，能否被老年人及其家人接受是一个重大挑战。为此，保监会要求保险公司在宣传该产品时候一定要如实介绍，明确提示消费者抵押房产的后续评估、管理和处置情况，不得夸大房产增值在提升养老金领取水平方面的作用；不得向不符合相关要求的客户推介业务。房产价值应当聘请具有一级资质房地产估价机构进行评估，费用由保险公司和消费者共同负担。</p><p>　　另外，对于该产品的销售，对保险营销员也有要求，需要持证上岗，经考核通过后才可取得反向抵押养老保险业务销售资格。而为了充分保护客户的&amp;ldquo;反悔权&amp;rdquo;，征求意见特别提出该产品的&amp;ldquo;犹豫期不得短于15个自然日&amp;rdquo;，比一般保险产品10天的犹豫期有所增加。</p><p>　　对于保险公司来说，也面临诸如房价涨跌，70年产权等诸多问题。上述险企人士认为，这个市场总量不是很大，这个业务也是特别复杂的，其面对的也是一些特殊人群。保险公司还需要考虑一些风险的问题，比如房价涨跌的风险等，总的来说，这个业务也不会做得特别大。保险公司这个业务量只要限制在一定的量，其风险还是可控的。</p><p>　　某险企的陈先生在与《每日经济新闻》记者交流时认为，这个业务面临很多风险，如70年产权的问题等等，预计保险公司的做法会比保守的。</p><p style=\"text-align: right;\">(原标题：以房养老保险有望4城市试点 保监会严控市场风险)</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (127,'text','<p><!-- publish_helper name=\'原始正文\' p_id=\'1\' t_id=\'1\' d_id=\'29757495\' f_id=\'3\' --></p><p>　　依法严惩暴力伤害医务人员犯罪，加大外逃职务犯罪嫌疑人追捕力度</p><p>　　据新华社电 最高检20日召开会议对重大案件公开、打击涉医犯罪、追逃等工作作出要求和部署。会议要求各级检察机关要积极参与维护医疗秩序打击涉医违法犯罪专项行动，依法严惩暴力伤害医务人员犯罪。会议透露，检察机关将加强反腐败国际合作，加大对外逃职务犯罪嫌疑人的追捕力度，并充分运用违法所得没收程序，尽最大努力挽回经济损失。</p><p>　　<strong>逐步实现终结性法律文书统一上网</strong></p><p>　　在20日召开的会议上，最高人民检察院检察长曹建明表示，检察机关将进一步落实新闻发言人制度，及时公开重大案件办理等情况，提高执法办案透明度。</p><p>　　会议指出，检察机关将积极主动推进司法公开进程，构建开放、动态、透明的阳光检察新机制。完善办案信息查询系统，逐步实现检察机关终结性法律文书统一上网，让司法公正成为老百姓看得见、感受得到的公正。落实新闻发言人制度，及时公开重大案件办理等情况，提高执法办案透明度。</p><p>　　<strong>依法打击职业“医闹”寻衅滋事</strong></p><p>　　针对当前反映强烈的涉医犯罪问题，会议要求各级检察机关要积极参与维护医疗秩序打击涉医违法犯罪专项行动，依法严惩暴力伤害医务人员犯罪。</p><p>　　最高检强调，检察机关要主动加强与相关部门沟通协调，加强立案监督，依法严惩暴力伤害医务人员和患者的犯罪活动，依法惩治聚众打砸等扰乱医疗机构正常秩序涉嫌犯罪的行为，依法打击职业&amp;ldquo;医闹&amp;rdquo;故意寻衅滋事、敲诈勒索等犯罪，妥善办理医患纠纷引发的民行申诉案件，保障医疗机构和医护人员合法权益。</p><p>　　<strong>严查以贿赂等手段破坏选举犯罪“”</strong></p><p>　　会议透露，检察机关将加强反腐败国际合作，加大对外逃职务犯罪嫌疑人的追捕力度，并充分运用违法所得没收程序，尽最大努力挽回经济损失。</p><p>　　会议强调，检察机关要坚持有案必查、有腐必惩，让人民群众切实感受到检察机关反腐败的成效。要进一步加大对行贿犯罪的打击力度，进一步完善行贿犯罪档案查询系统，促进社会征信体系建设。严肃查办以贿赂等手段破坏选举、侵犯公民民主权利的犯罪。要更加重视查办渎职侵权犯罪，促进国家工作人员勤政廉政，推动法治政府建设。</p><p>　　<strong>加大对危害药品安全犯罪打击力度</strong></p><p>　　记者从会议上获悉，为加大对危害药品安全犯罪的打击力度，最高检正在会同有关部门研究制定办理危害药品安全刑事案件适用法律若干问题的司法解释，进一步明确入罪和量刑标准，加大处罚力度、提高违法成本。</p><p>　　最高检要求，各级检察机关既要履行批捕、起诉职能，坚持以“零容忍”的态度，依法打击食品药品生产和流通领域制假售假、非法经营等犯罪，依法打击违法偷排、非法采矿、盗伐滥伐林木等破坏生态环境犯罪，又要积极履行监督职能，依法督促行政执法机关移送涉嫌犯罪案件、监督公安机关及时立案侦查，注意发现相关监管、执法、司法人员索贿受贿、徇私枉法等职务犯罪线索，打掉背后的“保护伞”，推动相关部门认真履行监管职责，保障“舌尖上的安全”和“蓝天碧水”。</p><p style=\"text-align: right;\">(原标题：最高检：及时公开重大案件办理情况)</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (128,'text','<p><!-- publish_helper name=\'原始正文\' p_id=\'1\' t_id=\'1\' d_id=\'29757583\' f_id=\'3\' --></p><p>　　新快报讯 近日，山东省曲阜市出台新规：所有市级领导干部一律取消秘书配备，其事务性活动由各办公室统一协调安排。其中，包括曲阜市委书记、市长在内的10位市级领导秘书配备取消。同时，曲阜市要求，领导外出不得讲求排场，让轻车简从成为常态。</p><p>　　今年3月初，曲阜市通过向群众、网民、人大代表、政协委员等各群体代表征求意见，梳理出了群众不满意的“四风”问题。其中，“脱离群众，‘官气’多、‘民气’少”、“坐车讲高档、进出讲排场”等被列入其中。曲阜市委书记李长胜说，领导干部配备秘书、公车等问题往往会成为群众关注的焦点，在一定程度上影响了干部整体形象，必须率先整改。</p><p>　　紧随其后，曲阜市委市政府果断将包括市委书记、市长在内的10位领导的秘书配备取消，秘书被安排回原岗位例行开展本职工作，如领导有工作需要可灵活安排。“由原来的一对一服务，变成了一对多服务。”曲阜市一相关负责人表示，按照规定，只有省部级以上领导才能够配备专职秘书，秘书的主要职责是协助领导开展日常工作，但因没有明确条文要求省部级以下领导不得配备专职秘书，所以一些县处级单位便跟风也为领导配备专职秘书，这种现象在全国2000多个县区中是广泛存在的。由于秘书队伍素质参差不齐，一些秘书打着领导旗号办私事的现象也出现过。</p><p>　　“目前，中央正在开展反‘四风’活动，曲阜取消领导秘书的根本目的也在于此。”曲阜市委书记李长胜说，取消专职秘书后，要求领导必须要身体力行，只有自己做得多了才能保证工作更好地开展。(据《齐鲁晚报》)</p><p style=\"text-align: right;\">(原标题：山东曲阜出台新规：市级领导不配秘书)</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (129,'introduce','<p style=\"text-align: center;\"><img data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224122450_e4mc64ckbn.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; color: rgb(102, 102, 102); font-family: Arial, Helvetica, sans-serif; font-size: 14px; line-height: 23px; text-align: center; white-space: normal; background-color: rgb(255, 255, 255);\"/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (129,'specification','<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" class=\"Ptable\"><tbody><tr class=\"firstRow\"><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">主体</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">系列</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">T 系列</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">型号</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">(20CKA00DCD)</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">颜色</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">黑色</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">平台</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">Intel</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">操作系统</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">操作系统</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">Windows 7 专业版</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">处理器</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">CPU类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">酷睿双核i7处理器</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">CPU速度</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">2.6GHz~3.2GHz</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">三级缓存</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">4M</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">核心</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">双核</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">内存</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">内存容量</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">8GB</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">内存类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">DDR3L-1600MHz</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">插槽数量</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">2 x SO-DIMM</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">硬盘</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">固态硬盘</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">256GB SSD</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">显卡</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">独立显卡</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">显示芯片</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">NVIDIA GeForce 940M</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">显存容量</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">独立1GB</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">双显卡</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">光驱</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">光驱类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">无光驱</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">显示器</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">屏幕规格</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">15.6英寸</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">显示比例</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">宽屏16：9</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">物理分辨率</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">2880x1620</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">屏幕类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">LED背光</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">通信</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">内置蓝牙</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">蓝牙4.0</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">局域网</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">10/100/1000Mbps</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">无线局域网</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">有</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">端口</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">音频端口</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">耳机、麦克风二合一接口</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">显示端口</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">VGA x 1/ mini DP x 1</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">RJ45</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">1个</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">USB3.0</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">3个</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">音效系统</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">扬声器</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">内置扬声器</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">杜比音效</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">内置麦克风</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">有</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">输入设备</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">键盘</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">全尺寸键盘</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">触摸板</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">多点触控</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">指点杆</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">有</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">其它设备</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">网络摄像头</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">有</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">摄像头像素</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">720P</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">指纹识别</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">无</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">读卡器</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">四合一读卡器</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">电源</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">电池</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">其它</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">续航时间</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">2-3小时, 具体时间视使用环境而定</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">电源适配器</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">100-240V自适应交流电源适配器</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">机器规格</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">尺寸</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">380mm*252mm*22mm</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">净重</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">2.04kg</td></tr></tbody></table><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (129,'services','<p>本产品全国联保，享受三包服务，质保期为：一年质保<br/>您可以查询本品牌在各地售后服务中心的联系方式，请点击这儿查询......<br/><br/>品牌官方网站：http://think.lenovo.com.cn<br/>售后服务电话：400-100-6000</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (129,'packingList','<p>笔记本 × 1 电池 × 1 电源适配器 × 1 装箱清单 × 1 产品保修卡 × 1</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (130,'introduce','<table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td height=\"210\" width=\"190\"><p><img data-lazyload=\"done\" target=\"_blank\" alt=\"\" height=\"120\" border=\"0\" width=\"180\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120606_2d4f6ferhd.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p></td><td height=\"210\" width=\"560\" style=\"border-bottom-color: rgb(204, 204, 204); border-bottom-style: dashed;\"><p>画质 - 新开发的全画幅CMOS图像感应器兼具高像素与低噪点</p><p oldoldoldoldoldclass=\"STYLE1\">●约2230万像素全画幅CMOS图像感应器的表现力&nbsp;<br/>●高性能DIGIC 5 数字影像处理器支持相机内多项复杂<br/>●图像处理&nbsp;<br/>●常用感光度高达ISO 25600，扩展时最高ISO 102400<br/>●高感光度也能实现清晰美丽的画质</p></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td height=\"212\" width=\"190\"><p><img data-lazyload=\"done\" target=\"_blank\" alt=\"\" height=\"120\" border=\"0\" width=\"180\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120606_jjei2p2dtf.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p></td><td height=\"212\" width=\"560\" style=\"border-bottom-color: rgb(204, 204, 204); border-bottom-style: dashed;\"><p>对焦 - 61点自动对焦。高精度自动对焦系统实现强大的被摄体捕捉能力</p><p>●搭载先进的高性能自动对焦感应器和<br/>●人工智能伺服自动对焦III代&nbsp;<br/>●61点高密度网状阵列自动对焦感应器带来多样化的<br/>●自动对焦点选择模式&nbsp;<br/>●61个自动对焦点中41点采用十字型自动对焦感应器&nbsp;<br/>●搭载先进的人工智能伺服自动对焦III代的新自动对焦系统<br/></p></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td height=\"210\" width=\"190\"><p><img alt=\"\" data-lazyload=\"done\" target=\"_blank\" height=\"120\" border=\"0\" width=\"180\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120606_55c58lblcc.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p></td><td height=\"210\" width=\"560\" style=\"border-bottom-color: rgb(204, 204, 204); border-bottom-style: dashed;\"><p>智慧 - 先进的自动曝光系统带来高精度曝光控制</p><p>●iFCL智能综合测光系统与63区双层测光感应器带来<br/>●高精度曝光控制&nbsp;<br/>●搭载EOS场景分析系统实现进化的全自动模式&nbsp;<br/>●强化的图像处理功能实现画质的提高&nbsp;<br/>●镜头像差校正有效抑制像差的影响&nbsp;<br/></p></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td height=\"210\" width=\"190\"><p><img alt=\"\" data-lazyload=\"done\" target=\"_blank\" height=\"120\" border=\"0\" width=\"180\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120606_tbge13q9vf.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p></td><td height=\"210\" width=\"560\" style=\"border-bottom-color: rgb(204, 204, 204); border-bottom-style: dashed;\"><p>机械 - 追求耐久性与操作性的高性能机身</p><p>●重视高级感、质感、操作性的设计&nbsp;<br/>●在严酷环境也能发挥威力的高坚固性&nbsp;<br/>●兼顾耐久性和高反应的快门单元&nbsp;<br/>●对应CF卡和SD卡两种存储卡的双卡槽<br/></p></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td height=\"210\" width=\"190\"><p><img alt=\"\" data-lazyload=\"done\" target=\"_blank\" height=\"120\" border=\"0\" width=\"180\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120606_ac2llnaiyl.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p></td><td height=\"210\" width=\"560\" style=\"border-bottom-color: rgb(204, 204, 204); border-bottom-style: dashed;\"><p>操作 - 搭载视野率约100%光学取景器和约104万点的大型液晶监视器&nbsp;<br/></p><p>●根据拍摄设置变换显示内容的智能信息显示光学取景器&nbsp;<br/>●活用高精细背面液晶监视器的多功能实时显示拍摄&nbsp;<br/>●辅助拍摄后工作的图像回放、管理功能&nbsp;<br/></p></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td height=\"210\" width=\"190\"><p><img data-lazyload=\"done\" target=\"_blank\" alt=\"\" height=\"120\" border=\"0\" width=\"180\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120606_b81yiovgks.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p></td><td height=\"210\" width=\"560\" style=\"border-bottom-color: rgb(204, 204, 204); border-bottom-style: dashed;\"><p>创意 - 多重曝光和HDR拍摄模式等激发创作欲望的多种功能&nbsp;<br/></p><p oldoldoldoldoldclass=\"STYLE2\">●实现戏剧性视觉效果的HDR（高动态范围）拍摄模式&nbsp;<br/>●拓展表现力的多重曝光功能&nbsp;<br/>●享受长宽比带来的视觉效果的多种长宽比功能&nbsp;<br/>●根据拍摄目的分别使用多种记录画质</p></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td height=\"210\" width=\"190\"><p><img data-lazyload=\"done\" target=\"_blank\" alt=\"\" height=\"120\" border=\"0\" width=\"180\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120606_7741q8r3dw.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p></td><td height=\"210\" width=\"560\" style=\"border-bottom-color: rgb(204, 204, 204); border-bottom-style: dashed;\"><p>短片 - 全画幅CMOS图像感应器的卓越短片表现</p><p oldoldoldoldoldclass=\"STYLE2\">●不断进化的EOS数码单反相机系列短片拍摄功能&nbsp;<br/>●进化了短片拍摄功能的基本性能&nbsp;<br/>●满足专业影像摄影师要求的高操作性</p></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td height=\"210\" width=\"190\"><p><img data-lazyload=\"done\" target=\"_blank\" alt=\"\" height=\"120\" border=\"0\" width=\"180\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120606_jk231ucu95.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p></td><td height=\"210\" width=\"560\" style=\"border-bottom-color: rgb(204, 204, 204); border-bottom-style: dashed;\"><p>扩展 - 搭配EOS 5D Mark III的高性能附件&nbsp;<br/></p><p oldoldoldoldoldclass=\"STYLE1\">●提高被摄体对应力拓展照片表现力的EOS 5D Mark III附件&nbsp;<br/>●多功能的附带软件支持影像表现&nbsp;<br/>●丰富的EF镜头阵容</p></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td width=\"414\" style=\"word-break: break-all;\"><img data-lazyload=\"done\" alt=\"\" height=\"276\" width=\"414\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120502_wk0uc1bg6v.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></td><td width=\"336\"><img data-lazyload=\"done\" alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120502_8xll83918a.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td><br/></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td width=\"414\"><img data-lazyload=\"done\" alt=\"\" height=\"276\" width=\"414\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120502_cbplksbors.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></td><td width=\"336\"><img data-lazyload=\"done\" alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120503_8sx49nb22m.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td><br/></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td><img data-lazyload=\"done\" alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120504_o06hnjk2ye.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></td><td><img data-lazyload=\"done\" alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120504_wedy4a98f6.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td><br/></td></tr></tbody></table><p><br/></p><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td><img data-lazyload=\"done\" alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120504_2tw8yebpgu.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></td><td><img data-lazyload=\"done\" alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120505_v3f655iaca.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/><br/><br/></td></tr></tbody></table><p><br/></p><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td height=\"270\" width=\"750\"><p style=\"text-align:center;\"><img data-lazyload=\"done\" target=\"_blank\" alt=\"\" height=\"320\" border=\"0\" width=\"529\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120524_d48i4h2pom.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td height=\"270\" width=\"750\"><p style=\"text-align:center;\"><img data-lazyload=\"done\" target=\"_blank\" alt=\"\" height=\"390\" border=\"0\" width=\"529\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120524_mvo0ppg2q9.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td height=\"270\" width=\"750\"><p style=\"text-align:center;\"><img data-lazyload=\"done\" target=\"_blank\" alt=\"\" height=\"310\" border=\"0\" width=\"529\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120524_k9r8cbxfpk.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td height=\"270\" width=\"750\"><p style=\"text-align:center;\"><img data-lazyload=\"done\" target=\"_blank\" alt=\"\" height=\"270\" border=\"0\" width=\"529\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120524_57i49iwbs8.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td height=\"233\" width=\"750\"><p style=\"text-align:center;\"><img data-lazyload=\"done\" target=\"_blank\" alt=\"\" height=\"300\" border=\"0\" width=\"529\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120524_59nimw8ch5.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p></td></tr></tbody></table><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"750\"><tbody><tr class=\"firstRow\"><td height=\"233\" width=\"750\"><p style=\"text-align:center;\"><img data-lazyload=\"done\" target=\"_blank\" alt=\"\" height=\"290\" border=\"0\" width=\"529\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120524_u38i1rta5t.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p></td></tr></tbody></table><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (130,'specification','<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" class=\"Ptable\"><tbody><tr class=\"firstRow\"><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">基本参数</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">品牌</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">佳能 Canon</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">型号</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">EOS 5D Mark III</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">相机类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">全画幅数码单反相机</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">总像素</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">约2340万</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">有效像素</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">约2230万像素</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">传感器类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">CMOS</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">传感器尺寸</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">约36x24mm</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">传感器描述</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">自动、手动、添加除尘数据</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">图像处理系统</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">DIGIC 5+</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">短片拍摄功能</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">1920x1080 （30p/25p/24p）/IPB ：约235MB/分钟1920x1080 （30p/25p/24p）/ALL-I ：约685MB/分钟1280x720 （60p/50p）/IPB ：约205MB/分钟1280x720 （60p/50p）</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">对焦系统</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">61点（最多41个十字型对焦点）* 可利用的自动对焦点和十字型对焦点数量根据镜头而不同。</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">屏幕参数</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">液晶屏尺寸</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">3.2英寸</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">液晶屏像素</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">约104万点</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">取景器类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">眼平五棱镜</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">液晶屏比例</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">3：2</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">屈光度条件及范围</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">约-3.0 - +1.0 m-1（dpt）</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">取景器描述</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">具备景深预览</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">镜头参数</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">镜头类型</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">标准变焦镜头</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">镜头描述</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">EF 24-105mm f/4L IS USM镜头</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">焦距</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">24-105mm</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">滤镜直径</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">77mm</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">镜头特性</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">F4恒定光圈标准变焦L镜头。使用了1片超级超低色散(超级UD)镜片和3片非球面镜片，能有效控制畸变和色差。内置影像稳定器</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">套机镜头防抖</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">IS光学防抖</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">曝光控制</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">曝光模式</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">程序自动曝光（场景智能自动、程序）、快门优先自动曝光、光圈优先自动曝光、手动曝光、B门曝光</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">曝光补偿</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">手动： 在±5级间以1/3或1/2级为单位调节自动包围曝光：在±3级间以1/3或1/2级为单位调节（可与手动曝光补偿组合使用）</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">测光方式</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">63区TTL全开光圈测光评价测光（与所有自动对焦点联动）局部测光（取景器中央约6.2%的面积）点测光（取景器中央约1.5%的面积）中央重点平均测光</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">ISO感光度</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">场景智能自动：在ISO 100 - 12800之间自动设置P、Tv、Av、M、B：自动ISO、ISO 100 - 25600（以1/3级或整级为单位）、或ISO感光度扩展到L（相当于ISO 50）、H1 （相当于ISO 51200）、H2 （相当于ISO 102400）</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">白平衡模式</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">可使用自动、预设（日光、阴影、阴天、钨丝灯、白色荧光灯、闪光灯）、用户自定义、色温设置 （约2500 - 10000K）白平衡矫正和白平衡包围曝光* 支持闪光色温信息传输</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">模式</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">HDR拍摄模式</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">曝光特性</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">自动曝光锁自动：在使用评价测光的单次自动对焦模式下合焦时应用手动：通过自动曝光锁定按钮</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">性能参数</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">快门</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">1/8000秒至1/60秒（场景智能自动模式）、闪光同步速度为1/200秒*.使用广角镜头时，快门速度可能为1/60秒或更慢。1/8000至30秒、B门（总快门速度范围。可用范围随拍摄模式各异。）</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">遥控功能</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">支持</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">连拍</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">单拍、高速连拍、低速连拍、静音单拍、静音连拍、</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">自拍</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">10秒自拍/遥控、2秒自拍/遥控</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">连拍速度</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">高速连拍：最高约6张/秒 低速连拍：最高约3张/秒 静音连拍：最高约3张/秒</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">闪光灯参数</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">闪光灯</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">外接闪光灯</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">外接闪光灯</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">EX系列闪光灯</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">闪光灯特性</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">E-TTL II自动闪光</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">存储及连接参数</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">存储介质</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">CF卡;SD卡</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">拍摄格式</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">照片格式：JPEG、RAW （14位佳能原创）、可以同时记录RAW+JPEG短片格式：MOV（MPEG-4 AVC/H.264记录压缩）音频格式：线性PCM</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">拍摄分辨率</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">L （大） ：约2210万像素（5760 x 3840）M （中） ：约980万像素（3840 x 2560）S1 （小1） ：约550万像素（2880 x 1920）</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">数据接口</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">音频/视频输出/数码端子、HDMI mini 输出端子、外接麦克风输入端子、耳机端子、遥控端子、无线遥控</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">电源参数</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">电池</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">LP-E6</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">电池续航时间</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">使用取景器拍摄：23℃/73 ℉时约950张、0℃/32 ℉时约850张</td></tr><tr><th class=\"tdTitle\" colspan=\"2\" style=\"text-align: center; padding-right: 5px; padding-left: 5px; width: 110px; background: rgb(245, 250, 254);\">外观参数</th></tr><tr><td><br/></td><td><br/></td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">颜色</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">黑色</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">尺寸</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">约152.0 x 116.4 x 76.4mm</td></tr><tr><td class=\"tdTitle\" style=\"padding: 2px 5px; text-align: right; background: rgb(245, 250, 254);\" width=\"110\">重量</td><td style=\"padding: 2px 5px; background: rgb(255, 255, 255);\">约950g（CIPA方针），约860g（仅机身）</td></tr></tbody></table><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (130,'services','本产品全国联保，享受三包服务，质保期为：一年质保<br/>如因质量问题或故障，凭厂商维修中心或特约维修点的质量检测证明，享受7日内退货，15日内换货，15日以上在质保期内享受免费保修等三包服务！<br/>(注:如厂家在商品介绍中有售后保障的说明,则此商品按照厂家说明执行售后保障服务。) 您可以查询本品牌在各地售后服务中心的联系方式，请点击这儿查询......<br/><br/>品牌官方网站：http://www.canon.com.cn/<br/>售后服务电话：4006-222-666');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (130,'packingList','A. EOS 5D Mark III机身 B. 锂电池 LP-E6 C. 电池充电器 LC-E6E（含电源线） D. USB接口连接线IFC-200U E. 立体声视频连接线 AVC-DC400ST F. 相机宽背带 EW-EOS5D MKIII G. 镜头软包 LP1219 H. EF 24-105mm f/4L IS USM I. 佳能镜头遮光罩 EW-83H ※另含相机使用说明书以及EOS数码解决方案光盘（EOS DIGITAL Solution Disk）');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (131,'introduce','<p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\" \" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145411_ju5gq1subq.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145411_05kq7q6cbj.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145412_n5cxlj98ig.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145414_k2b7ayx8em.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145415_j7sn66pou3.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145417_sc4buyr6ca.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145417_txwq6ubkbt.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145418_2gq4y0oyri.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145419_5fd5i611do.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145421_tswepj5937.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145422_6hgndl74vl.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145424_dwc8ypq9af.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145425_arbwtdencv.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145426_h7d2hwu918.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145428_c1hq3iyx57.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145429_l7sx80lnm7.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(119, 119, 119); font-family: Arial, Verdana, 宋体; font-size: 12px; line-height: 18px; white-space: normal; background-color: rgb(255, 255, 255);\"><img alt=\"\" data-lazyload=\"done\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145430_advuer2vsm.jpg\" class=\"\" style=\"border: 0px none; vertical-align: middle; margin: 0px; padding: 0px;\"/></p><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (131,'specification','');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (131,'services','');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (131,'packingList','');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (132,'introduce','<p style=\"text-align: center;\"><img alt=\"\" data-lazyload=\"done\" src=\"http://img30.360buyimg.com/popWaterMark/g10/M00/01/0B/rBEQWFEF724IAAAAAAC_NHzk3wkAAARLgCUu34AAL9M334.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; font-size: small;\"/>&nbsp; &nbsp;&nbsp;<br/></p><p style=\"text-align: center;\">&nbsp;</p><p style=\"text-align: center;\"><br/>&nbsp; &nbsp;&nbsp;<img alt=\"\" data-lazyload=\"done\" src=\"http://img30.360buyimg.com/popWaterMark/g10/M00/01/0B/rBEQWVEF72kIAAAAAADBUXye6RgAAARLgFA8Z8AAMFp177.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle; font-size: small;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px;\"><img alt=\"\" data-lazyload=\"done\" src=\"http://img30.360buyimg.com/popWaterMark/g10/M00/01/0B/rBEQWVEF73oIAAAAAACaeqTcpZwAAARLgI2ZCYAAJqS705.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px;\"><br/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px;\"><br/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px;\"><br/></p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px; text-align: center;\"><br/></p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px; text-align: center;\">&nbsp;</p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px;\"><br/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px;\"><br/><br/><img alt=\"\" data-lazyload=\"done\" src=\"http://img30.360buyimg.com/popWaterMark/g10/M00/01/0B/rBEQWFEF75cIAAAAAACWI-GDt74AAARLgNbxbEAAJY7509.jpg\" class=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p><p><br/></p><p style=\"text-align: center;\"><img src=\"http://img10.360buyimg.com/imgzone/jfs/t2407/301/1685884117/845350/85e733ac/566914daN6bbab797.jpg\" alt=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/><img src=\"http://img10.360buyimg.com/imgzone/jfs/t1993/108/1607721477/499530/70eda2fa/566914dbN63731dfd.jpg\" alt=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/><img src=\"http://img10.360buyimg.com/imgzone/jfs/t2131/161/1611434292/613388/529aeee3/566914dcNcbe318b0.jpg\" alt=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/><img src=\"http://img10.360buyimg.com/imgzone/jfs/t2152/2/1096247202/697732/4610f2ec/566914ddN5adfe66f.jpg\" alt=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/><img src=\"http://img10.360buyimg.com/imgzone/jfs/t2263/44/1733747474/273756/a883f440/56692a8fN7344d2d3.jpg\" alt=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/><img src=\"http://img10.360buyimg.com/imgzone/jfs/t2497/115/1664251927/388261/a099f777/56692a92N10dce920.jpg\" alt=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/><img src=\"http://img10.360buyimg.com/imgzone/jfs/t2458/326/1711675499/688548/7d6073b7/56692a96N95daaf8c.jpg\" alt=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/><img src=\"http://img10.360buyimg.com/imgzone/jfs/t2188/6/1619639445/716986/3229252c/56692a99N97b81a45.jpg\" alt=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/><img src=\"http://img10.360buyimg.com/imgzone/jfs/t1849/117/1655443679/504348/a854f70b/56692a9cN73dee406.jpg\" alt=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/><img src=\"http://img10.360buyimg.com/imgzone/jfs/t2551/329/784608418/447977/ef122f49/56692b3cNc7b6745f.jpg\" alt=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/><img src=\"http://img10.360buyimg.com/imgzone/jfs/t2278/280/1583296790/615380/69da20e1/56692b40N8fa40ba0.jpg\" alt=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/><img src=\"http://img10.360buyimg.com/imgzone/jfs/t2257/9/1645545756/1004566/849bb109/56692b45Nca5f97c0.jpg\" alt=\"\" style=\"margin: 0px; padding: 0px; border: 0px; vertical-align: middle;\"/></p><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (132,'specification','');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (132,'services','<p>1、购买米莱珠宝，即顺丰发货，全程保险承保！ 2、戒指尺寸不合适，我们免费为顾客修改，直到满意为止！商家承担快递费用！ 3、在收到商品无损坏的情况下，支持7天无条件退货，顾客承担退货费用！</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (132,'packingList','<p>商品*1 珠宝盒*1 权威证书*1</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (133,'text','WPS Office是由金山软件股份有限公司自主研发的一款办公软件套装，可以实现办公软件最常用的文字、表格、演示等多种功能。具有内存占用低、运行速度快、体积小巧、强大插件平台支持、免费提供海量在线存储空间及文档模板、支持阅读和输出PDF文件、全面兼容微软Office97-2010格式（doc/docx/xls/xlsx/ppt/pptx等）独特优势。覆盖Windows、Linux、Android、iOS等多个平台。');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (134,'text','<p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">好压压缩软件(HaoZip)是强大的压缩文件管理器，是完全免费的新一代压缩软件，相比其它压缩软件占用更少的系统资源用，有更好的兼容性，压缩率高！好压压缩软件(HaoZip)的功能包括强力压缩、分卷、加密、自解压模块、智能图片转换、智能媒体文件合并等功能。完美支持鼠标拖放及外壳扩展！</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224153437_8fa5s6j2yl.jpg\" style=\"border: 0px; display: block;\"/></p><p class=\"introTit\" style=\"margin-top: 10px; margin-bottom: 10px; padding: 0px; line-height: 24px; clear: both; font-family: 宋体, arial; color: rgb(51, 51, 51); position: relative; white-space: normal; background: url(http://www.pc6.com/style/css/images/ddd.gif) 0% 50% repeat-x rgb(255, 255, 255);\"><span style=\"padding: 0px 10px 0px 0px; font-weight: bold; font-family: 微软雅黑, arial; background-image: initial; background-attachment: initial; background-size: initial; background-origin: initial; background-clip: initial; background-position: initial; background-repeat: initial;\">功能介绍</span></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">支持rar5<br/>国内独家支持对rar5格式的解压</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224153437_kmhqt3p61e.jpg\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">永久免费<br/>传统压缩软件的共享版、试用期、购买许可、修正版，我们忍够了！2345好压对所有个人用户和企业用户永久免费，请放心使用！<br/>兼容性好<br/>国内独家完美支持包括Win8、Win7、Vista、WinXP和Win2003在内的所有WINDOWS系统，并且比传统压缩软件支持更多的压缩格式。只需安装一款软件，即可轻松解压超过50种常见压缩格式。最新完美支持ZIPX和ALZ格式解压，能够压缩LZH格式。</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224153437_xhmt6mfcs2.jpg\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">虚拟光驱<br/>小巧精悍的交互界面，提供多种加载入口，操作简单。支持挂载ISO、MDS、MDF、CCD、CUE、IMG、UDF、URG等通用光盘镜像为虚拟光驱，方便娱乐和学习光盘的读取。<br/>丰富扩展<br/>首创无需解压即可查看包内图片功能，提供MD5校验、批量替换字符、批量修改文件名等多种实用小工具，满足您的各种需求！</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224153437_nd4q2g7buh.jpg\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224153437_nwfwu5nr22.jpg\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">好压压缩软件(HaoZip)提供了对ZIP、7Z和TAR文件的完整支持，能解压RAR、JAR、XPI、BZ2、BZIP2、TBZ2、TBZ、GZ、GZIP、TGZ、TPZ、LZMA、Z、TAZ、LZH、LZA、WIM、SWM、CPIO、CAB、ISO、ARJ、XAR、RPM、DEB、DMG、HFS等多达45种格式文件，这是同类软件无法比拟的！</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224153438_dphuht0mc2.jpg\" style=\"border: 0px; display: block;\"/></p><p class=\"introTit\" style=\"margin-top: 10px; margin-bottom: 10px; padding: 0px; line-height: 24px; clear: both; font-family: 宋体, arial; color: rgb(51, 51, 51); position: relative; white-space: normal; background: url(http://www.pc6.com/style/css/images/ddd.gif) 0% 50% repeat-x rgb(255, 255, 255);\"><span style=\"padding: 0px 10px 0px 0px; font-weight: bold; font-family: 微软雅黑, arial; background-image: initial; background-attachment: initial; background-size: initial; background-origin: initial; background-clip: initial; background-position: initial; background-repeat: initial;\">使用方法</span></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\"><strong>1、什么是解压缩</strong><br/>解压缩就是将一个通过软件压缩的文档、文件等各种东西恢复到压缩之前的样子。<br/><strong>2.如何解压缩</strong><br/>方法一：通过右键菜单解压文件<br/>鼠标右键单击选中的一个或多个压缩文件，在弹出的右键菜单中，选择【解压文件…】，在新弹出的【解压文件】窗口中，设置好解压选项，点击【确定】即可进行解压操作。如果解压之后的文件保存在当前文件夹，也可通过选择【解压到当前文件夹】，实现快捷操作。</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224153438_9r6rvm3kv3.jpg\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">方法二：在2345好压软件界面解压文件<br/>打开2345好压，找到希望解压的压缩文件，点击选中后，点击软件主界面左上第二个【解压到】图标，并在新弹出的窗口中设置解压缩选项后，点击【确定】即可对文件进行解压。</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224153438_9e1d3gcy8e.jpg\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\"><strong>3.测试压缩文件</strong><br/>压缩一个文件时，在【压缩文件】界面，点击【密码】标签，然后输入密码即可。</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224153438_uhok79n0nq.jpg\" style=\"border: 0px; display: block;\"/></p><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (135,'text','<p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(51, 51, 51); font-family: &#39;Microsoft Yahei&#39;, Tahoma, Simsun; font-size: 14px; line-height: 24px; white-space: normal; background-color: rgb(255, 255, 255);\"><strong>PotPlayer播放器中文版</strong>是一款优秀的<strong>高清播放器</strong>，它的前身是著名的KMPlayer。它可以播放大多数主流的视频、音频文件，并不需要额外安装第三方解码器。它强大的定制性与扩展能力让它成为播放高清影片的不二之选。<br/><strong>PotPlayer播放器中文版</strong>是kmplayer的原作者姜龙喜先生进入daum公司后的新一代作品,目前正在全力开发中.由于采用delphi编译程序kmplayer的一些弊端，姜龙喜先生为改进播放器本身的一些性能而重新用vc++进行构架.</p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px; color: rgb(51, 51, 51); font-family: &#39;Microsoft Yahei&#39;, Tahoma, Simsun; font-size: 14px; line-height: 24px; white-space: normal; background-color: rgb(255, 255, 255);\"><strong>PotPlayer绿色纯净版</strong>，由PotPlayer论坛版主”闻雷”专注维护，完善简体中文语言，精简Live相关文件、安装临时文件及一些无用的多余文件。</p><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (136,'text','<p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\"><strong><span style=\"color: rgb(255, 0, 0);\">Adobe Reader X 11.0.6 官方中文版</span></strong>下载，支持打开和使用 Adobe PDF 的工具，可查看、打印和管理 PDF。<span style=\"color: rgb(255, 0, 0);\">若已经安装过之前的版本，请先卸载后再安装此版本！</span></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">在 Reader 中打开 PDF 后，可以使用多种工具快速查找信息。<br/>如果您收到一个 PDF 表单，则可以在线填写并以电子方式提交。<br/>如果收到审阅 PDF 的邀请，则可使用注释和标记工具为其添加批注。<br/>使用 Reader 的多媒体工具可以播放 PDF 中的视频和音乐。<br/>如果 PDF 包含敏感信息，则可利用数字身份证对文档进行签名或验证。</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img alt=\"\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224152623_5kfuecb6p6.jpg\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\"><span style=\"color: rgb(255, 0, 0);\">Adobe Reader XI新功能：<br/></span>新增了“评论”功能，所有用户都可以使用即时贴和高亮工具；<br/>移动设备也可以读取PDF文件了，Adobe面向Android、Windows Phone 7和黑莓Tablet OS均发布了免费的Reader X；<br/>采用了新的Protected Mode安全功能，保障用户浏览PDF文件的安全性；<br/>行业领先的安全性<br/>充分利用 Reader 中的受保护模式的安全性，它有助于保护计算机软件和数据免受恶意代码的侵扰。<br/>简化的用户界面<br/>更精确、高效地查看信息。选择阅读模式可在屏幕上显示更多内容，或选择双联模式查看跨页。在浏览器中使用打印、缩放和查找等键盘快捷键。</p><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (148,'text','Mozilla Firefox，中文名通常称为“火狐”或“火狐浏览器”，是一个开源网页浏览器，使用Gecko引擎（非ie内核），支持多种操作系统如Windows、Mac和linux。');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (149,'text','<p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">经过近9个月的研发，迅雷今天首度公开了7.9新版。迅雷7.9加快了启动速度，新增了一键立即下载、在开始前完成、批量任务分组、智能任务分类等功能，下面具体来看一下：</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224152012_yw0xxdjobo.jpg\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">　　——加快启动速度<br/>　　启动速度慢的罪魁祸首是“频繁、大量进行硬盘读写”，迅雷7.9从基础设计上追求最少、最精明的进行硬盘读写。<br/>　　——确保运行顺畅<br/>　　迅雷7.9避免了使用插件，并且极力推行了模块之间的异步协作。<br/>　　——从源头避免“卡顿”<br/>　　迅雷7.9加入了自我性能分析工具。深入剖析迅雷启动、运行过程中每个步骤的性能表现。任何可能导致“卡顿”的问题发生时，都能从根源上锁定并解决问题。<br/>　　——一键立即下载<br/>　　在迅雷7.9中，以往的复杂操作不复存在，即便是通过手动输入下载地址的方式建立任务，迅雷7.9也能让你一键立即下载！</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224152012_tbr5vluwwx.png\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">　　——在开始前完成<br/>　　迅雷7.9在你点击“立即下载”前，便已经开始下载，甚至下载完成。</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224152012_tltxdx81br.png\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">　　——批量任务分组<br/>　　迅雷7.9增加的“任务组”功能，可以将批量任务归纳为1个任务，即便再多批量任务，也能一目了然！</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224152012_31499ev791.png\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">　　——智能任务分类<br/>　　在迅雷7.9中，你不再需要亲自将任务归类了，当下载完成，迅雷7.9会根据文件类型自动分类。<br/>　　——探索下载奥秘<br/>　　迅雷是如何进行下载的？下载时发生了什么？迅雷7.9内置的“任务详情页”，让你清楚看到下载时的细节和事件。</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224152012_mmijco2j3x.png\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">　　——全新风格的界面设计，别出心裁的操作动画。迅雷7.9还提供了多款的动态皮肤。</p><p class=\"introTit\" style=\"margin-top: 10px; margin-bottom: 10px; padding: 0px; line-height: 24px; clear: both; font-family: 宋体, arial; color: rgb(51, 51, 51); position: relative; white-space: normal; background: url(http://www.pc6.com/style/css/images/ddd.gif) 0% 50% repeat-x rgb(255, 255, 255);\"><span style=\"padding: 0px 10px 0px 0px; font-weight: bold; font-family: 微软雅黑, arial; background-image: initial; background-attachment: initial; background-size: initial; background-origin: initial; background-clip: initial; background-position: initial; background-repeat: initial;\">使用方法</span></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">　　<strong>如何在浏览器中增加“使用迅雷下载”？</strong><br/>　　【解决方法】<br/>　　可以通过右上角的小工具---选择“浏览器支持”；</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224152012_ici7ncfwcg.jpg\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\"><br/>　　根据实际使用的浏览器，启动对应浏览器的支持，然后重新打开浏览器即可。</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224152012_ayskx5pwel.jpg\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">　　<strong>如何设置迅雷7最大任务数？</strong><br/>　　打开迅雷7 =》系统设置=》任务管理里面进行设置即可。</p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224152012_ifpto2mo4h.jpg\" style=\"border: 0px; display: block;\"/></p><p class=\"introTit\" style=\"margin-top: 10px; margin-bottom: 10px; padding: 0px; line-height: 24px; clear: both; font-family: 宋体, arial; color: rgb(51, 51, 51); position: relative; white-space: normal; background: url(http://www.pc6.com/style/css/images/ddd.gif) 0% 50% repeat-x rgb(255, 255, 255);\"><span style=\"padding: 0px 10px 0px 0px; font-weight: bold; font-family: 微软雅黑, arial; background-image: initial; background-attachment: initial; background-size: initial; background-origin: initial; background-clip: initial; background-position: initial; background-repeat: initial;\">更新日志</span></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">　　功能变化：<br/>　　“系统设置”面板中“监视浏览器”选项改为可按浏览器单独设置<br/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img title=\"\" border=\"0\" hspace=\"0\" vspace=\"0\" src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224152013_np95o5nsfs.png\" width=\"406\" height=\"57\" style=\"border: 0px; display: block; width: 406px; height: 57px;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\">　　细节改进：<br/>　　优化登录迅雷账号的响应速度<br/>　　当迅雷不处于窗口最前端时双击悬浮窗，迅雷将置于窗口最前端显示<br/>　　增加了任务组名称编辑框的宽度<br/>　　安装迅雷时，检测到迅雷正在运行的提示中，将“強”字改为“强”字（关爱处女座在行动！）<br/>　　部分文字按钮不再显示文字提示（关爱阅读障碍患者在行动！）<br/>　　增加“皮肤、主菜单”按钮的文字提示（关爱小学生在行动！）<br/>　　右下角的“右侧栏管理”按钮用鼠标右键点击也能弹出菜单了（关爱鼠标左键坏了的人在行动！）<br/>　　任务详情中的“注释”栏改为可编辑状态（关爱健忘症患者在行动！）<br/>　　任务详情中的各项参数文字已经对齐（关爱强迫症患者在行动！）<br/>　　迅雷账号信息面板在迅雷处于窗口化模式且左侧空间足够时，向左侧展开，不再遮挡任务分类区域<br/>　　迅雷账号信息面板的“成就”部分在没有“成就”时不展示<br/>　　“小工具”菜单中的“速度测试”改名为“宽带测速器”<br/>　　下载模式菜单中的“更多设置”更名为“网速保护设置”<br/>　　问题修正：<br/>　　修正开启主界面透明效果时，搜索栏显示异常的问题<br/>　　修正某些情况下，打开网页时会误触发下载请求的问题<br/>　　修正在BT任务文件列表中，展开或收起文件夹时，文件列表自动滚动至顶部的问题<br/>　　修正删除正在使用的代理时，代理设置无法同步生效的问题<br/>　　修正英文版Win10中IE浏览器的迅雷下载支持扩展名称显示乱码的问题<br/>　　修正某些情况下电驴任务名显示乱码的问题<br/>　　修正将“私人空间”任务拖动到“正在下载”时，无法正常拖动图标的问题</p><p class=\"introTit\" style=\"margin-top: 10px; margin-bottom: 10px; padding: 0px; line-height: 24px; clear: both; font-family: 宋体, arial; color: rgb(51, 51, 51); position: relative; white-space: normal; background: url(http://www.pc6.com/style/css/images/ddd.gif) 0% 50% repeat-x rgb(255, 255, 255);\"><span style=\"padding: 0px 10px 0px 0px; font-weight: bold; font-family: 微软雅黑, arial; background-image: initial; background-attachment: initial; background-size: initial; background-origin: initial; background-clip: initial; background-position: initial; background-repeat: initial;\">安装载图</span></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224152013_3xf33rrv37.jpg\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224152013_hxkiongwl0.jpg\" style=\"border: 0px; display: block;\"/></p><p style=\"margin-top: 0px; margin-bottom: 15px; padding: 0px; line-height: 24px; clear: both; color: rgb(102, 102, 102); font-family: 宋体, 微软雅黑, arial; font-size: 14px; white-space: normal; text-align: center; background-color: rgb(255, 255, 255);\"><img src=\"http://demo.jspxcms.com/uploads/1/image/public/201512/20151224152013_pscittllvb.jpg\" style=\"border: 0px; display: block;\"/></p><p><br/></p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (150,'text','<p><strong>360浏览器</strong>是一款小巧、快速、安全、功能强大的多窗口浏览器，它是完全免费，没有任何功能限制的绿色软件，最全的恶意网址库，最新的云安全引擎，“安全红绿灯”全面拦截木马病毒网站;“搜索引擎保护”自动标识搜索结果页中的风险网站，网必通是360极速浏览器推出的新功能，解决个别海外科技类网站打不开的问题。当开启网必通功能后，当访问此类网站时，网必通会绕开封锁加速打开。我们希望网必通是送给广大程序员、大学生的礼物，希望大家通过它学习国外最新技术。</p><p><img alt=\"360极速浏览器2015下载\" src=\"http://www.3987.com/uploadfile/2014/1115/20141115085031526.jpg\" style=\"width: 500px; height: 337px;\"/></p><p><strong>360极速浏览器功能如下：</strong></p><p>1、基础最扎实：极速浏览器功能全面而又不繁杂;双核切换最智能(得益于海量用户和长期数据运营);稳定性最好(专门团队负责崩溃、卡死、用户反馈等)。而国内其他同行要不为了升核功能简陋，要不功能繁杂胡乱创新，要不稳定性极差。</p><p>2、升核动作快：这是我们的优良传统，难得的是在我们这么大体量的情况下(功能多、稳定性要求高)还能快速升核，就是让大象快跑!这个得益于专业、成熟的团队和一致的目标!</p><p>3、界面最精致：界面审美虽然仁者见仁，但从皮肤的精致程度来说，国内绝对数一数二，真正要做到好看、耐看!最关键的是我们换壁纸就可以实现效果华丽丽的整体换肤，这个经过长期调教优化，加上新标签页的配合，效果绝对一流!既能让浏览器千变万化彰显个性，又能保证易用性。这要归功于成熟的设计师和精益求精的工程师，在长期的配合下，他们知道好的浏览器界面应该是什么样的，避免了同行们要么很浮夸要么不够精致的问题。</p><p><strong>360极速浏览器 v8.1.0.374 更新：</strong></p><p>优化：</p><p>优化新建下载窗口出现时的动画效果。</p><p>优化广告过滤加载策略，提升启动速度。</p><p>优化flash加载策略，npflash不可用时使用ppflash。</p><p>修复：</p><p>解决QQ空间中动画显示异常的问题。</p><p>解决部分flash上不能输入中文的问题。</p>');
INSERT INTO cms_info_clob (f_info_id, f_key, f_value) VALUES (180,'text','<p>　　【环球时报记者 赵倩】针对日前有报道称“中国中兴通讯股份有限公司在蒙古承建项目时涉嫌行贿，其管理人员已被蒙古国家反贪局逮捕”，中兴公司18日接受《环球时报》记者采访时予以否认。</p><p><br/></p><p>　　该报道称，本月13日，蒙古国家反贪局对中兴公司在当地的办事处、高管住处、私人汽车及车库进行搜查，将搜到的文件资料都带走，并将中兴高管羁押在监狱。</p><p><br/></p><p>　　针对此事，中兴公司18日给《环球时报》发来声明称，作为在中国深港两地上市的国际化通信企业，中兴通讯一直以来秉持守法经营的理念。中兴通讯在蒙古的业务开展，与其他国家一样，完全符合行业国际惯例要求和所在国法律的规定。目前，我们的业务遍及全球140 多个国家的500多个运营商，有着近30年良好的行业经历。</p><p><br/></p>');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (83,'system','winxp,win7,win8,win2003');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (93,'system','winxp,win7,win8,win2003');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (94,'stock','有货');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (95,'stock','有货');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (97,'stock','有货');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (98,'stock','有货');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (117,'location','南昌');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (117,'salary','面议');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (117,'number','5');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (117,'company','南昌蓝智科技有限公司');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (117,'qualifications','1.二年以上Java开发经验，Java基础知识扎实。\r\n2.熟悉JS，CSS，掌握常用的js框架，如JQuery，HTML。\r\n3.有企业级web应用开发经验，熟练掌握Web应用程序的开发调试技术。\r\n4.熟悉java开源框架，如spring，jpa，shiro等\r\n5.熟悉常用的设计模式、面向对象的分析和设计技术。\r\n6.学习能力强，沟通和表达能力良好，能迅速理解需求并给出解决方案。');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (117,'responsibilities','1.根据项目的需求，负责子模块或功能的设计与开发，以及后期的升级和维护工作。\r\n2.根据开发规范与流程独立完成模块的设计、编码、测试以及相关文档。');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (117,'dept','研发部');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (118,'location','南昌');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (118,'salary','面议');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (118,'number','4');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (118,'company','南昌蓝智科技有限公司');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (118,'qualifications','1、一年以上网页设计或UI设计经验；熟练使用photoshop、Dreamweaver、Flash网页设计软件，并能结合项目需求设计、制作与维护商业、企业网站；\r\n2、熟练掌握DIV+CSS；\r\n3、有扎实的美术功底、良好的创意思维和理解能力，能及时把握客户需求；\r\n4、能独立负责网站作品的设计及程序制作。\r\n5、善于沟通、有良好的团队合作精神和高度的责任感、能够承受压力、对设计充满激情。');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (118,'responsibilities','1、与客户进行充分沟通,进行网站视觉效果设计,并向客户进行提案取得客户认可；\r\n2、与客户建立良好关系；\r\n3、根据设计经理或组长的分配，完成其他设计任务。');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (118,'dept','设计部');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (129,'stock','有货');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (129,'marketPrice','12000.00');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (129,'price','6249.00');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (94,'marketPrice','2999');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (94,'price','1999');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (98,'marketPrice','4500.00');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (98,'price','3899.00');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (130,'stock','有货');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (130,'marketPrice','25880.00');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (130,'price','22599.00');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (97,'marketPrice','8888.00');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (97,'price','6666.00');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (95,'marketPrice','599.00');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (95,'price','179.00');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (131,'stock','有货');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (131,'marketPrice','1499.00');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (131,'price','681.00 ');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (132,'stock','有货');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (132,'marketPrice','25880.00');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (132,'price','13999.00');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (144,'responsibilities','1、负责安卓客户端软件的产品开发和维护； \r\n2、独立完成客户端软件需求的整理和软件设计、开发、调试、发布； \r\n3、根据系统中具体难点问题，有针对性的进行技术攻关； \r\n4、按照项目计划，撰写规范技术文档，按时提交高质量代码，完成开发任务； \r\n5、参与整体技术架构讨论，提供建设性意见。 ');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (144,'number','4');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (144,'dept','开发部');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (144,'location','南昌');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (144,'salary','面议');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (144,'qualifications','1、2年以上Android开发工作经验，大专或以上学历，计算机、通信或信息技术相关专业； \r\n2、了解SQLlite，熟悉基本的数据库操作； \r\n3、精通Java开发,对面向对象设计有深入的理解； \r\n4、精通Android开发平台的框架原理，能够熟练使用Android SDK，对Android应用结构有深刻的认识 ； \r\n5、熟悉各大Android智能手机平台的差异性，并有机型适配调优的经验； \r\n6、丰富的手机UI设计经验，熟悉网络编程、多线程、图形界面编程； \r\n7、有良好的代码习惯，要求结构清晰，命名规范，逻辑性强，代码冗余率低； \r\n8、已在安卓应用商店上发布应用或有安卓开发成功案例者优先； \r\n9、对技术热衷，勇于创新，工作认真负责，时间管理能力强，能承受较强工作压力，善于沟通协作，具备良好的团队合作精神。');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (144,'company','南昌蓝智科技有限公司');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (145,'location','南昌');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (145,'qualifications','1.本科以上学历，2年以上UI设计经验；\r\n2.具备良好的沟通能力，能清晰理解用户需求，熟悉网络用户审美趋向和界面使用习惯，并了解当前互联网发展趋势；\r\n3.具备良好的创意设计表现能力，能准确把握网站和软件界面的整体风格、页面布局、色彩等视觉表现，熟练掌握W3C相关互联网规范；\r\n4.能承受一定的工作强度和压力；\r\n5.具备了解Web2.0网站制作规范或有移动终端设计经验者优先。');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (145,'responsibilities','1.负责根据用户需求，与项目经理、产品经理共同定义网站和软件界面的整体风格；\r\n2.负责结合产品或项目需求，完成网站和软件界面主体设计，并完成banner、icon等设计；\r\n3.负责产品部门GUI设计工作，以及项目组实施GUI设计工作；\r\n4.协助开发人员完成UI产品实现；\r\n5.参与用户研究，参与设计体验，交互的呈现；\r\n6.参与设计流程的规范制定；');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (145,'number','3');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (145,'dept','设计部');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (145,'salary','面议');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (145,'company','南昌蓝智科技有限公司');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (146,'location','南昌');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (146,'qualifications','1、热爱软件测试工作，有一定的软件测试经验。\r\n2、熟悉测试的流程、步骤及规范，能够独立设计测试方案，编写测试计划和测试报告；\r\n3、分析问题所在并进行准确定位和验证，按照标准格式填写并提交Bug报告，跟踪并验证Bug，并确认问题得以解决；\r\n4、能够熟练使用测试需求、测试用例的管理工具及bug的管理工具；\r\n5、具备独立工作能力及团队合作精神，具有强烈的责任心和事业心，积极、主动、进取；\r\n6、良好的学习能力，较强的表达，性格积极主动具有良好的沟通能力（书面和口头）；\r\n7、流利的英文沟通能力优先\r\n8、有Andorid测试经验，有软件开发经验者优先考虑。');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (146,'responsibilities','1、根据需求和软件功能编写测试用例，编写测试计划\r\n2、执行软件测试，包括性能测试、功能测试和验收测试。\r\n3、Bug的提交和跟踪，编写测试报告。');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (146,'number','3');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (146,'dept','测试部');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (146,'salary','面议');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (146,'company','南昌蓝智科技有限公司');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (148,'system','winxp,win7,win8,win2003');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (149,'system','winxp,win7,win8,win2003');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (146,'education','大专');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (146,'experience','不限');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (145,'education','大专');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (145,'experience','1年以上');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (144,'education','本科');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (144,'experience','1年以上');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (118,'education','大专');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (118,'experience','1年以上');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (117,'education','本科');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (117,'experience','1年以上');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (146,'jobtype','全职');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (145,'jobtype','全职');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (144,'jobtype','全职');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (118,'jobtype','全职');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (117,'jobtype','全职');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (166,'flashaddr','http://player.video.qiyi.com/b2fd6fdd4933034d1b306995452f1c0d/0/0/v_19rrn7jmi0.swf-albumId=333180100-tvId=333180100-isPurchase=0-cnId=13');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (168,'flashaddr','http://player.video.qiyi.com/0d5f2406ef9b2b9347fdb2f3e80682c8/0/0/v_19rrn8kuv0.swf-albumId=330590800-tvId=330590800-isPurchase=2-cnId=1');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (170,'flashaddr','http://player.video.qiyi.com/8993dc5f5d0aee1981107b4597b3d644/0/0/v_19rrn6nkuc.swf-albumId=202324801-tvId=333634600-isPurchase=0-cnId=4');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (167,'flashaddr','http://player.video.qiyi.com/bc918848fe4a610e893d146ecb155657/0/0/v_19rrn6itu4.swf-albumId=333528800-tvId=333528800-isPurchase=0-cnId=6');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (151,'flashaddr','http://player.video.qiyi.com/32af11f16ca09aad9fbe9c8e1cb4b428/0/0/v_19rrnbefw0.swf-albumId=310131100-tvId=310131100-isPurchase=0-cnId=13');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (169,'flashaddr','http://player.video.qiyi.com/38cc04e1a4c14f9798051dfa7035fabf/0/0/v_19rrifwuck.swf-albumId=565556-tvId=700360-isPurchase=0-cnId=1');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (140,'flashaddr','http://i7.imgs.letv.com/player/swfPlayer.swf?id=2234148&autoplay=0');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (137,'flashaddr','http://player.youku.com/player.php/sid/XNjY2NDcxNDA4/v.swf');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (139,'flashaddr','http://player.youku.com/player.php/sid/XNjIwMjMxNTI0/v.swf');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (138,'flashaddr','http://player.youku.com/player.php/sid/XNjY4MTIxMTcy/v.swf');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (87,'flashaddr','');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (172,'flashaddr','');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (173,'flashaddr','');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (174,'flashaddr','http://player.video.qiyi.com/7198e2513cc561c9e2f8a21e38856d8c/0/0/v_19rrn6478o.swf-albumId=202244001-tvId=334792900-isPurchase=0-cnId=2');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (150,'license','免费软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (150,'system','winxp,win7,win8');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (150,'star','★★★★☆');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (150,'language','简体中文');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (150,'type','国产软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (149,'license','免费软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (149,'star','★★★★★');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (149,'language','简体中文');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (149,'type','国产软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (148,'license','免费软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (148,'star','★☆☆☆☆');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (148,'language','简体中文');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (148,'type','国产软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (136,'license','免费软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (136,'star','★★★☆☆');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (136,'language','简体中文');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (136,'type','国产软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (135,'license','免费软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (135,'system','winxp,win7,win8');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (135,'star','★★★★☆');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (135,'language','简体中文');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (135,'type','国产软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (136,'system','winxp,win7,win8');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (134,'license','免费软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (134,'system','winxp,win7,win8');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (134,'star','★★★★★');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (134,'language','简体中文');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (134,'type','国产软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (133,'license','免费软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (133,'system','winxp,win7,win8');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (133,'star','★★★★★');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (133,'language','简体中文');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (133,'type','国产软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (93,'license','免费软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (93,'star','★★★★★');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (93,'language','简体中文');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (93,'type','国产软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (83,'license','免费软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (83,'star','★★★☆☆');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (83,'language','简体中文');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (83,'type','国产软件');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (44,'text_editor_type','ueditor');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (43,'text_editor_type','ueditor');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (73,'text_editor_type','ueditor');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (49,'text_editor_type','ueditor');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (32,'text_editor_type','ueditor');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (46,'text_editor_type','ueditor');
INSERT INTO cms_info_custom (f_info_id, f_key, f_value) VALUES (53,'text_editor_type','ueditor');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (24,'少林寺欲将功夫融入足球培训运动员',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'少林足球近日，有关登封武校将少林功夫融入足球，培训足球运动员的新鲜事儿在网上流传，引起社会各界的关注。8月6日下午，记者电话采访了嵩山少林寺武僧团培训基地总教头释延鲁。释延鲁说，8月2日全国第六届高校足球教练培训研讨会在登封举行，他向与会的70多位国内各高校的足球教练员阐述了他的观点。释延鲁认为，','南方日报',NULL,'杨春',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (25,'北京警方捣毁侵害公民信息团伙 抓获299人',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,' 北京警方10日通报，8月7日警方出动300名警力，对藏匿于一栋大厦内侵害公民个人信息的犯罪团伙实施抓捕，299名嫌犯落网，其中刑事拘留95人、行政拘留204人，并缴获数十箱公民个人信息名单。目前，涉案公司总裁仍在逃。北京市公安局丰台分局副局长郑威在10日的新闻发布会上介绍说，今年8月初，警方侦查发','南方日报',NULL,'杨春',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (26,'北京发布雷电黄色预警 傍晚至夜间将有大到暴雨',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,' \r\n11日凌晨开始，北京雷雨大作，持续一夜，至9时许雨量减弱，天气转阴。北京市气象台发布消息称，11日白天到夜间，北京将有大到暴雨，并伴有雷电。尤其是傍晚至夜间，雨势较大，山区须注意防范地质灾害。\r\n降雨带来了些许清凉，一解前几日的持续暑热。11日一早，许多市民趁雨停了，纷纷出门采买，但整个京城仍','中国广播网',NULL,'王军',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (27,'聚划算成清仓专用 问题产品充斥',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'作为淘宝最知名的团购平台，2011年是聚划算的爆发年，而历经反腐门之后，重新出台的聚划算团购服务竞拍规则(俗称“坑位费”)出台至今就重创了不少中小商家。据聚划算官网显示，其竞拍时间为每天上午10：00-11：00，竞拍起拍价为人民币1000元，单次加价幅度为100元及其整数倍，参与聚划算竞拍的卖家应',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (28,'七夕前夜英仙座流星雨助兴 我国处于最佳观测区',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'“这是今年最值得向大家推荐观测的流星雨”，北京天文馆馆长朱进前日发微博“推介”即将于13日凌晨迎来极大值的英仙座流星雨，是日恰逢七夕。但天气预报显示，届时北京将以阴雨天气为主，市民能否一睹年度最佳流星雨，还要看天公是否做美。据国际流星组织(IMO)估计，今年英仙座流星雨的高峰期将在北京时间8月12日','京华时报',NULL,'商西',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (30,'报告称中国取代英国成世界第5大常规武器出口国',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'据美联社3月18日报道，瑞典智库斯德哥尔摩国际和平研究所(SIPRI)18日称，中国已取代英国成为世界第五大常规武器出口国。SIPRI在报告中称，中国在过去5年(2008-2012)中武器出口总量增长了163%，国际军火市场占有份额从2%增至5%，同时排名也从之前的第八升至第五。报告称，中国武器的最','环球时报',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (32,'俄军主力战机集群出动应对乌克兰',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'俄南部军区27日起在克拉斯诺达尔边疆区举行空中演习。据悉，在为期两天的时间里，苏－25SM3强击机机 组人员将完成约40架次的飞行，并在无线电干扰的情况下对假想敌进行约50次打击。俄国防部网站消息说，俄东部军区当天也开始了大规模空中演习。演习将在 哈巴罗夫斯克边疆区和滨海边疆区进行，将有超过30架包','环球时报',NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140404213603_k0a0bc.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (33,'汽车高速爆胎致车祸 鉴定机构回避轮胎质量问题',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,NULL,NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201303/20130318084854_x4wvqy.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (34,'市民未插卡取款机吐出一万元 不受诱惑忙报警',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'6日晚，在昆明市白龙路一自助银行里，来取钱的刘老师站在柜员机前捣鼓了半天&mdash;&mdash;卡插不进去，他在柜员机上随便按了“5000”，谁知50张百元钞票就吐到了他面前。机器出了问题？他又按了“5000”，又有5000元吐到了手里。仔细一查，发现柜员机里原来已有一张银行卡，刘老师忙报了警。','春城晚报',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (35,'31省市最理想伴侣地图出炉：男性最想娶川妹子',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'31省市男女最理想伴侣地图出炉\r\n首选嫁京男 最爱娶川妹\r\n超五成的青年择偶时首选本地人；女性最想嫁的外省人中，北京男最受欢迎；男性则最想娶川妹子。\r\n记者上午获悉，零点指标数据进行2013年七夕主题调查，对全国各地1074位18至45周岁的网民进行随机访问，绘出31省市青年男女最理想伴侣地图','中国新闻网',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (36,'调查称逾九成被调查者对中国人身份感到自豪',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'根据中国社科院相关机构进行的“中国公民政治文化”问卷调查：90．03％被调查者对“作为中国人，我很自豪”持赞同态度；72．23％被调查者认同“中国传统文化对个人具有很大的影响”。该调查在全国10个省份进行，获得6159份有效样本。','东方网',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (43,'凤凰传奇赚1亿 人气搭档怎么分钱',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'近日，有明星经纪公司爆料，唱遍大小春晚的“农业重金属组合”凤凰传奇，出场费已经涨到了60万/场，若加上代言，2012年约有1亿进账。1亿这个数字也许略有夸张，但实际收入肯定也不会少。网友们一边感叹农业重金属的力量不可小觑，一边又开始琢磨这么多钱他们怎么分呢？凤凰传奇经纪人接受采访时表示，“玲花和曾毅','腾讯娱乐',NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201303/20130319005453_gcalyk.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (44,'羽泉同乐会为海泉庆生 9月开唱回馈歌迷',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,' 8月12日，羽泉(微博)(微信号：yuquanweixin )携专辑《拾伍》在京举办粉丝同乐会。而活动第二天“七夕节”恰好是胡海泉(微博)的生日，歌迷们不仅为海泉准备了礼物还送上蛋糕，倍受感动的海泉感慨道：“对于羽泉来说，能在各位的陪伴和支持下一起走过的岁月就是最好的礼物。”为了纪念出道15周年，','腾讯娱乐',NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201308/20130813072417_4y837l.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (45,'专访杨幂：“事业有成”离我很远 不着急完婚',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'“大家好，我是制片人杨幂。”一身干练的黄色风衣，一句霸气外露的自我介绍，升级当了制片人的杨幂果然显现出女强人的气质。昨日，由其担任监制的都市时尚偶像剧《微时代之恋》在沪举行开机发布会，杨幂带着她钦点的男主角余文乐，以及她花费一年选出的八位新人齐齐亮相。腾讯网娱乐中心总监常斌到场助阵，宣布该剧官网正式','腾讯娱乐',NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201303/20130319010624_en4gsf.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (46,'终极格斗激情上演',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'重庆，12日，《一夜惊喜》见面会。女神范冰冰(微博)(微信号：fbbstudio916 )携李治廷空降山城重庆，在重庆各大影院轮番宣传最新电影《一夜惊喜》。电影中范冰冰和李治廷上演了姐弟恋，见面会现场，范爷霸气地将李治廷抱起来秀“恩爱”。当媒体问及范爷七夕怎么过时，范冰冰表示明天将在北京和“那个','腾讯娱乐',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (47,'足协曾忧王迪成\"定时炸弹\" 两次漏判国安点球',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'在上周六武汉卓尔与北京国安的比赛中，主裁判王迪先后错判国安外援格隆禁区内假摔，接着又漏判给故意踢人的卓尔球员柯钊红牌。中国足协裁委会赛后确认，王迪两次判罚属严重错、漏判。王迪面临停哨3至6场的重罚。据悉，王迪通过抽签获本场比赛执法资格后，足协曾有人提议由经验丰富的老裁判取而代之，但提议未被采纳。抽签','腾讯体育',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (48,'热火不败继续称霸实力榜 湖人升至第9火箭第11',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'3月18日体育专电（记者林德韧）NBA(微博)官方18日公布了最新一期的实力榜，拿下22连胜的迈阿密热火队当之无愧地继续排在榜首，圣安东尼奥马刺队和俄克拉荷马雷鸣队分居二、三位。\r\n　　在击败多伦多猛龙队之后，热火队保住了自己的不败金身，东区冠军的位置已经基本锁定，现在剩下的唯一悬念就是热火队的连胜','新华网',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (49,'足协或罚武汉暴力后卫停赛2场 格隆停赛不变',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'在上周六中超联赛卓尔与国安比赛下半时，卓尔球员柯钊暴踢国安外援马季奇的举动在足球界引起了不小反响。中国足协裁判委员会今天也出具报告提请纪律委员会对柯钊追罚。据了解，纪委会很可能将柯钊的犯规定性为暴力行为。由于他及其俱乐部认错态度良好，中国足协将按照底线处罚柯钊，柯钊面临停赛2场、1万元左右的追罚。电','腾讯体育',NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201303/20130319012002_dsuaw3.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (50,'西媒：巴萨巴黎巅峰对决 皇马上签穆帅会老友',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'北京时间3月15日晚，欧足联在瑞士尼翁总部进行了本赛季欧冠(微博 专题) 1/4决赛对阵形势抽签。杀进八强的三支西甲(微博 专题) 球队中，皇马(官方微博 数据) 抽中土超劲旅加拉塔萨雷，巴萨(官方微博 数据) 遭遇法甲大鳄巴黎圣日耳曼(微博 数据) ，马拉加(官方微博 数据) 则要迎接德甲球队','腾讯体育',NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140404215630_ielobo.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (52,'尚德宣布可转债违约 成大陆首家公司债违约企业',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,NULL,'腾讯财经',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (53,'俄媒：苏联为买军火曾贿赂罗斯福',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'上世纪30年代，为利用美国技术建造新型军舰，苏联领导人特批从国库划拨50万美元的“行政经费”(在当时是一笔不小的数目)，供一家苏联外贸公司使用，以便打通美国高层关节。但这笔钱最终打了水漂，并连累不少人获罪，俄罗斯《权力》杂志日前刊文披露了这段秘闻。　　1924年，苏联在美国注册成立了阿姆外贸集团公司','新华网',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (54,'WSJ：中国正在重蹈美国金融危机覆辙',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'北京时间3月19日凌晨消息，华尔街日报中国实时报栏目周一文章称，两名经济学家指出，美国房地产市场崩溃之前曾有过的三个警示性信号已经在中国出现，这意味着中国只有非常有限的时间来摆脱困境。\r\n文章指出，在野村证券于上周六公布的一份报告中，经济学家张智威和陈家瑶指出，物业价格的上涨、杠杆化的快速积累和潜在','东方网',NULL,'李军',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (73,'杨钰莹再谈与赖文峰恋情 面对爱情并不后悔',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,' 杨钰莹“女人不是因为美丽而可爱，是因为可爱而美丽。”俄罗斯文学家列夫&middot;托尔斯泰在《战争与和平》中的这句话，用在杨钰莹身上正合适。白裙子，长头发，低声说，轻声笑，杨钰莹完全保留着少女的神态。10多年的岁月，惊涛骇浪的往事，在她身上仿佛没留下痕迹。但交谈久了，还是不一样。你会发现，她从前',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201308/20130814133153_a6qtv0.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (78,'翘臀美女力挺开拓者 香艳宝贝支持灰熊',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'翘臀美女力挺开拓者 香艳宝贝支持灰熊',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140403224743_k5txx2.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (80,'第十二届全运会礼仪小姐训练实拍',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'第十二届全运会礼仪小姐训练实拍',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140403230102_j1ygy9.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (81,'两会上靓丽的礼仪小姐',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'两会上靓丽的礼仪小姐',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140403224051_vro62p.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (83,'搜狗输入法',NULL,'搜狗输入法(搜狗拼音) 7.1c',NULL,NULL,NULL,'0','0',NULL,NULL,'搜狗输入法作为国内汉字拼音输入法的领导者，搜狗输入法率先实现了输入法与互联网的结合。基于搜狗搜索引擎技术，对中文词库有突破性发展，开创了新一代中文输入法。即时高效地更新热门词库，大幅提升了输入效率，让输入速度产生了质的飞跃。在词库的广度、词语的准确度上，搜狗输入法都在行业内遥遥领先。',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140401163838_4g7x6o.jpg','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224154447_njmwafxvgl.jpg',NULL,NULL,'http://cdn2.ime.sogou.com/1ef422d9c97c66c1769eb774daa8644d/567ba27b/dl/index/1450769272/sogou_pinyin_78j.exe','sogou_pinyin_78j.exe',36194023,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (87,'台南资讯月曾甜热舞',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'台南资讯月曾甜热舞',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141213234730_7ylyoq6qne.jpg',NULL,'http://demo.jspxcms.com/uploads/1/video/public/201412/20141217105935_9qtt8vp9yj.mp4','台南资讯月曾甜热舞_超清.mp4',NULL,NULL,NULL,NULL,NULL,35252510,'3:53',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (93,'QQ7.9',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'免费的即时聊天软件，给上网带来更多沟通乐趣。腾讯QQ2013年度皮肤呈现视觉盛宴，皮肤编辑器实现个性化面板创意；QQ应用盒子全新呈现，丰富应用满足多彩生活；QQ短信首度面世，畅享无处不在的沟通体验；炫彩字体，炫出聊天个性与风采；QQ支持自定义标签，标签顺序随心换。提示：如果您正在运行着腾讯QQ或者T',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140401163625_s9usmk.jpg','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224154307_d6dwqq2yll.jpg',NULL,NULL,'http://dldir1.qq.com/qqfile/qq/QQ7.9/16638/QQ7.9.exe','QQ7.9.exe',59068088,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (94,'惠科（HKC）Q320 32英寸2K高分广视角护眼不闪LED背光液晶显示器',NULL,NULL,NULL,NULL,'#FF0000','0','0',NULL,NULL,'惠科（HKC）Q320 32英寸2K高分广视角护眼不闪LED背光液晶显示器',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151224141639_p5kfadfqal.jpg','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224141645_8fnjf4vtop.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (95,'苏醒的乐园 2014春装 新款女装 韩版修身 中长款 风衣外套女 FY164 驼色 M',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'苏醒的乐园 2014春装 新款女装 韩版修身 中长款 风衣外套女 FY164 驼色 M',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140401151029_uk6wwv.jpg','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224144649_90stgfll7g.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (97,'苹果（APPLE）iPhone 6s 64G版 4G手机（金色）WCDMA/GSM',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'苹果（APPLE）iPhone 6s 64G版 4G手机（金色）WCDMA/GSM',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140401144253_nnavj0.jpg','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224122043_yqh1n9nbrd.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (98,'索尼（SONY） DSC-RX100 M2 黑卡数码相机',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'索尼（SONY） DSC-RX100 M2 黑卡数码相机',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140401142459_82smxp.jpg','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224121205_23l6sjouvm.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (99,'美官方统计：为避税放弃美国国籍人数1年暴增5倍',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,' \r\n8月12日电 (记者 王欢)美国联邦公报最新公布的数据显示，2013年第二季度放弃美国国籍的人数再创新高，暴增至1131人，比去年同期的189人多出5倍。美国媒体称，美国政府为了增加税收应对拮据的财政状况，准备实施更严格的资产申报规定，使得放弃美国公民身份或绿卡的人数持续增长。\r\n　　报告显示',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (100,'英国派军舰赴地中海演习 西班牙称是恐吓行为',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'\r\n8月12日电 (记者 周兆军)英国和西班牙围绕直布罗陀的主权争议升级，12日，英国皇家海军的的一支快速反应部队前往地中海进行年度演习，包括“光辉”号航母在内的多艘军舰将前往直布罗陀海域。\r\n英国国防部宣布，包括“光辉”号航空母舰、两艘护卫舰和辅助舰只在内的10艘军舰前往地中海进行演习。英国国防部',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (117,'Java开发工程师',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'Java开发工程师',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (118,'网页设计师',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'网页设计师',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (126,'以房养老拟在北上广汉试点 初次贷款不超500万',NULL,'以房养老拟在北上广汉试点\r\n初次贷款不超500万',NULL,NULL,NULL,'0','0',NULL,NULL,'	　　每日经济新闻记者 黄俊玲 发自北京\r\n	　　千呼万唤，以房养老保险方案终于要成为现实。\r\n	　　《每日经济新闻》记者昨日(3月20日)获悉，为贯彻落实国务院《关于加快发展养老服务业的若干意见》有关要求，丰富养老保障方式的新途径，保监会决定开展老年人住房反向抵押养老保险试点。\r\n	　　同时，','中国新闻网','http://www.chinanews.com/gn/2014/05-30/6233523.shtml',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (127,'最高检:严查以贿赂等手段破坏选举犯罪',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'	　　依法严惩暴力伤害医务人员犯罪，加大外逃职务犯罪嫌疑人追捕力度\r\n	　　据新华社电 最高检20日召开会议对重大案件公开、打击涉医犯罪、追逃等工作作出要求和部署。会议要求各级检察机关要积极参与维护医疗秩序打击涉医违法犯罪专项行动，依法严惩暴力伤害医务人员犯罪。会议透露，检察机关将加强反腐败国际',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (128,'中方在南印度洋发现疑似MH370黑匣子信号',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'	　　新快报讯 近日，山东省曲阜市出台新规：所有市级领导干部一律取消秘书配备，其事务性活动由各办公室统一协调安排。其中，包括曲阜市委书记、市长在内的10位市级领导秘书配备取消。同时，曲阜市要求，领导外出不得讲求排场，让轻车简从成为常态。\r\n	　　今年3月初，曲阜市通过向群众、网民、人大代表、政协',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (129,'ThinkPad T550(20CKA00QCD) 15.6英寸笔记本电脑 (i7-5600U 8G 256GB 独显1G 蓝牙 指纹 WIN7Pro)',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'ThinkPad T550(20CKA00QCD) 15.6英寸笔记本电脑 (i7-5600U 8G 256GB 独显1G 蓝牙 指纹 WIN7Pro)',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140401141615_kq36r7.jpg','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224122723_7i7de3ddcs.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (130,'佳能（Canon） EOS 5D Mark III 单反套机（EF 24-105mm f/4L IS USM 镜头）',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'佳能（Canon） EOS 5D Mark III 单反套机（EF 24-105mm f/4L IS USM 镜头）',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140401142650_bac6rd.jpg','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224120231_65sse24nht.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (131,'乔丹男子新款2015夏季耐磨透气篮球战靴OM3540101鲜红/黑色',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'乔丹男子新款2015夏季耐磨透气篮球战靴OM3540101鲜红/黑色',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145341_9nq3mp9otv.jpg','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224145344_89bwi3rykk.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (132,'米莱珠宝 4.27克拉红碧玺戒指 18K白金戒指 结婚戒指 情侣戒指 品位女士首饰【10天定制+证书】',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'米莱珠宝 4.27克拉红碧玺戒指 18K白金戒指 结婚戒指 情侣戒指 品位女士首饰【10天定制+证书】',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140401151753_4dd63k.jpg','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224144736_ewd1t08u7m.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (133,'WPS 2013',NULL,'wps office 2013 个人版 9.1.0.4555',NULL,NULL,NULL,'0','0',NULL,NULL,'WPS Office是由金山软件股份有限公司自主研发的一款办公软件套装，可以实现办公软件最常用的文字、表格、演示等多种功能。具有内存占用低、运行速度快、体积小巧、强大插件平台支持、免费提供海量在线存储空间及文档模板、支持阅读和输出PDF文件、全面兼容微软Office97-2010格式（doc/doc',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140401162441_xft5mm.png','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224153817_vdahkda8my.jpg',NULL,NULL,'http://wdl1.cache.wps.cn/wps/download/W.P.S.5400.19.552.exe','W.P.S.5400.19.552.exe',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (134,'好压',NULL,'2345好压（HaoZip） 正式版 4.2.1.9445',NULL,NULL,NULL,'0','0',NULL,NULL,'好压压缩软件(HaoZip)是强大的压缩文件管理器，是完全免费的新一代压缩软件，相比其它压缩软件占用更少的系统资源用，有更好的兼容性，压缩率高！好压压缩软件(HaoZip)的功能包括强力压缩、分卷、加密、自解压模块、智能图片转换、智能媒体文件合并等功能。完美支持鼠标拖放及外壳扩展！功能介绍支持rar',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140401164223_bnlgl0.jpg','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224153551_2akebwrxnp.gif',NULL,NULL,'http://dl.2345.com/haozip/haozip_v5.5_jt.multi.exe','haozip_v5.5_jt.multi.exe',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (135,'PotPlayer1.6',NULL,'PotPlayer 正式版 1.6',NULL,NULL,NULL,'0','0',NULL,NULL,'PotPlayer播放器中文版是一款优秀的高清播放器，它的前身是著名的KMPlayer。它可以播放大多数主流的视频、音频文件，并不需要额外安装第三方解码器。它强大的定制性与扩展能力让它成为播放高清影片的不二之选。PotPlayer播放器中文版是kmplayer的原作者姜龙喜先生进入daum公司后的新',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223183234_meu89ooh4v.png','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224153038_hlpu3gentd.png',NULL,NULL,'http://demo.jspxcms.com/uploads/1/file/public/201512/20151224164602_edj1kwmpox.exe','PotPlayerSetup64.exe',19892992,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (136,'Adobe Reader',NULL,'Adobe Reader 11.0.00',NULL,NULL,NULL,'0','0',NULL,NULL,'Adobe Reader X 11.0.6 官方中文版下载，支持打开和使用 Adobe PDF 的工具，可查看、打印和管理 PDF。若已经安装过之前的版本，请先卸载后再安装此版本！在 Reader 中打开 PDF 后，可以使用多种工具快速查找信息。如果您收到一个 PDF 表单，则可以在线填写并以电子',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140401164659_g0lhr7.jpg','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224152444_6wle3i9xgp.png',NULL,NULL,'http://dlc2.pconline.com.cn/filedown_1322_6950695/Xsx70db2/AdbeRdr11000_zh_CN.exe','AdbeRdr11000_zh_CN.exe',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (137,'警察故事2013',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'警察故事2013',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141216175409_1302a0rob7.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (138,'私人订制',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'私人订制',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141216180617_rdipnf4txk.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (139,'爸爸去哪儿 第一季',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'爸爸去哪儿 第一季',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141216180232_fgmao9eja0.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (140,'来自星星的你',NULL,'来自星星的你 第1集',NULL,NULL,NULL,'0','0',NULL,NULL,'来自星星的你',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141213234745_y0bl80nkxj.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (141,'哈登参与林书豪与帕森斯的赛前仪式',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'哈登参与林书豪与帕森斯的赛前仪式',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140403225253_hc5wh6.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (142,'孤独星球公布世界十大必看奇迹',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'孤独星球公布世界十大必看奇迹',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140403230505_vxnp2f.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (143,'全能锋霸梅西的“七种武器”',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'全能锋霸梅西的“七种武器”',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140404215204_mnasf7.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (144,'安卓 ios 软件工程师',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'安卓 ios 软件工程师',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (145,'软件UI设计师',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'软件UI设计师',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (146,'软件测试工程师',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'软件测试工程师',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (148,'火狐浏览器',NULL,'Firefox火狐浏览器 正式版 28.0',NULL,NULL,NULL,'0','0',NULL,NULL,'Mozilla Firefox，中文名通常称为“火狐”或“火狐浏览器”，是一个开源网页浏览器，使用Gecko引擎（非ie内核），支持多种操作系统如Windows、Mac和linux。',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140405134003_12my1m.png','http://demo.jspxcms.com/uploads/1/image/public/201501/20150112165457_3bbqyk222b.jpg',NULL,NULL,'http://download.firefox.com.cn/releases-sha2/stub/official/zh-CN/Firefox-latest.exe','Firefox-latest.exe',544235,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (149,'迅雷7',NULL,'迅雷7 正式版 7.9.20.4754',NULL,NULL,NULL,'0','0',NULL,NULL,'经过近9个月的研发，迅雷今天首度公开了7.9新版。迅雷7.9加快了启动速度，新增了一键立即下载、在开始前完成、批量任务分组、智能任务分类等功能，下面具体来看一下：　　——加快启动速度　　启动速度慢的罪魁祸首是“频繁、大量进行硬盘读写”，迅雷7.9从基础设计上追求最少、最精明的进行硬盘读写。　　——确',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140405134137_xedtaq.jpg','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224151905_wtle7104a4.png',NULL,NULL,'http://down.sandai.net/thunder7/Thunder_dl_7.9.42.5050.exe','Thunder_dl_7.9.42.5050.exe',59068088,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (150,'360浏览器8.1',NULL,'360浏览器8.1官方下载 v8.1.0.396 网必通版',NULL,NULL,NULL,'0','0',NULL,NULL,'360浏览器是一款小巧、快速、安全、功能强大的多窗口浏览器，它是完全免费，没有任何功能限制的绿色软件，最全的恶意网址库，最新的云安全引擎，“安全红绿灯”全面拦截木马病毒网站;“搜索引擎保护”自动标识搜索结果页中的风险网站，网必通是360极速浏览器推出的新功能，解决个别海外科技类网站打不开的问题。当开',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201404/20140405134321_jknrl9.png','http://demo.jspxcms.com/uploads/1/image/public/201512/20151224150332_8wfoyk10nb.png',NULL,NULL,'http://down.360safe.com/se/360se8.1.1.118.exe',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (151,'江湖时尚：郭采洁隔空示爱杨幂','郭采洁隔空示爱杨幂',NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'江湖时尚：郭采洁隔空示爱杨幂',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141213234650_p4hjxsxoka.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'15:32',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (162,'Freemarker参考手册',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'Freemarker参考手册',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201501/20150114103540_4hhb9ohttu.png',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/doc/jspxcms_x.swf',NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (163,'Jspxcms安装手册',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'Jspxcms安装手册',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151224155649_y8bm7pb496.png',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/doc/jspxcms_x.swf',NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (164,'盘点2014年地球上任性的土豪们',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'盘点2014年地球上任性的土豪们',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209112942_fwf3ekao15.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (165,'2014年那些令人难忘的戎装丽人',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'2014年那些令人难忘的戎装丽人',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209113650_0lpn6qp59m.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (166,'维多利亚的秘密2014大秀',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'维多利亚的秘密2014大秀',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141213234524_lx9s8ywe4u.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (167,'两岸秘史（一）北平无战事​',NULL,'晓松奇谈：两岸秘史（一）北平无战事​',NULL,NULL,NULL,'0','0',NULL,NULL,'两岸秘史（一）北平无战事​',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141213234634_dpgi7u2xd1.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (168,'猩球崛起2：黎明之战',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'猩球崛起2：黎明之战',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141213234558_sgnxtct1sl.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (169,'搞怪3宝：狂飙记',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'搞怪3宝：狂飙记',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141213234703_t3r1hgrv7x.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (170,'妖怪名单：灵异校园中的恋情',NULL,'妖怪名单',NULL,NULL,NULL,'0','0',NULL,NULL,'妖怪名单：灵异校园中的恋情',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141213234616_h5kn46mysu.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (172,'台南资讯月XL ARMY热舞',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'台南资讯月XL ARMY热舞',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141217112855_w1e3qpt2iq.jpg',NULL,'http://demo.jspxcms.com/uploads/1/video/public/201412/20141217110506_muad4rbmwk.mp4','台南资讯月XL ARMY热舞_超清.mp4',NULL,NULL,NULL,NULL,NULL,30131589,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (173,'台南资讯月钢管舞',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'台南资讯月钢管舞',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141217113441_vsk6khemr3.jpg',NULL,'http://demo.jspxcms.com/uploads/1/video/public/201412/20141217110855_uvsybihp4c.mp4','台南资讯月钢管舞_超清.mp4',NULL,NULL,NULL,NULL,NULL,36169477,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (174,'四十九日祭',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'四十九日祭',NULL,NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141217114138_ucs3sshpc4.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_detail (f_info_id, f_title, f_subtitle, f_full_title, f_link, f_is_new_window, f_color, f_is_strong, f_is_em, f_info_path, f_info_template, f_meta_description, f_source, f_source_url, f_author, f_small_image, f_large_image, f_video, f_video_name, f_file, f_file_name, f_file_length, f_is_allow_comment, f_step_name, f_video_length, f_video_time, f_doc, f_doc_name, f_doc_length, f_doc_pdf, f_doc_swf, f_html, f_mobile_html, f_is_weixin_mass) VALUES (180,'传中兴管理人员被蒙古国反贪局逮捕 公司否认',NULL,NULL,NULL,NULL,NULL,'0','0',NULL,NULL,'　　【环球时报记者 赵倩】针对日前有报道称“中国中兴通讯股份有限公司在蒙古承建项目时涉嫌行贿，其管理人员已被蒙古国家反贪局逮捕”，中兴公司18日接受《环球时报》记者采访时予以否认。　　该报道称，本月13日，蒙古国家反贪局对中兴公司在当地的办事处、高管住处、私人汽车及车库进行搜查，将搜到的文件资料都带',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (78,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223174012_3ivdak9qaj.jpg',0,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (78,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223174012_h2v3qs5y5h.jpg',1,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (80,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201308/20130813063704_4gflo6.jpg',0,'8月11日，第十二届全运会（十二运）礼仪引导志愿者封闭训练。当日，在沈阳体育学院开放十二运礼仪引导志愿者媒体开放日活动。第十二届全运会将于2013年8月31日至9月12日在辽宁举行，历时13天。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (80,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201308/20130813064757_oppd1r.jpg',1,'8月11日，第十二届全运会（十二运）礼仪引导志愿者封闭训练。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (80,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201308/20130813064842_iey6ge.jpg',2,'8月11日，第十二届全运会（十二运）礼仪引导志愿者封闭训练。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (81,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223212828_4947knmw65.jpg',0,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (81,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223212828_10wbkq7qnq.jpg',1,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (164,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209115711_jftrfmqw5c.jpg',0,'盘点2014年地球上任性的土豪们');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (164,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209115712_bxg9djal0l.jpg',1,'【中东地区的土豪】中东土豪为12只鹰买机票【中东地区的土豪】中东土豪为12只鹰买机票 卡萨布兰卡飞阿布扎比的航班上，一个阿拉伯长袍男给他的12只老鹰买了票，坐经济舱，主人自己却坐在了头等舱。网友喊话：敢来中国，上飞机的是十二只鹰，下飞机的是12份鸡煲');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (164,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209115712_gipro2llj6.jpg',2,'【美国土豪】搬家后烧掉自家370平米豪宅据《每日邮报》报道，美国德克萨斯州一对夫妇烧掉了自己372平米的豪宅，这座豪宅坐落在75英尺(约合23米)的峭壁边。大约两周前，房屋的主人举家搬迁，并把所有能带走的东西全部带走，他联系了当地的工程部门，咨询了处置这处房屋的最佳方案。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (164,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209115712_7ep64uj61j.jpg',3,'【印度土豪】印度土豪130万定制4公斤重纯金上衣庆祝生日据《印度时报》报道，45岁的印度企业家潘卡伊·帕拉科（Pankaj Parakh）近日定制了一件4公斤重的纯金上衣，作为自己的生日礼物。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (164,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209115712_xa3hnn6qwo.jpg',4,'【俄罗斯土豪】一百万颗水晶打造“水晶大奔”据《每日邮报》报道，伦敦西部的骑士桥街道是著名的国际中心，每天停在这里的豪车都吸引很多人的目光。近日，一位俄罗斯美女Daria Radionova将自己的奔驰车身上镶嵌了100万颗施华洛水晶。网友喊话：不怕被人抠吗？');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (164,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209115712_247h3r62j7.jpg',5,'【土耳其土豪】珠宝商7.8万块金片制低胸短裙 已卖八件据外媒报道，土耳其珠宝商阿塔坎(Ahmet Atakan)设计出一款纯金打造的低胸背心短裙，短裙耗费7.8万块金片缝制，设计简约，在灯光照射下非常夺目。据报道，该短裙设计极为简约，短袖与低胸设计能让穿着者在舞会中吸引众人眼光。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (165,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209113549_st9mhlec4n.jpg',0,'当地时间2014年5月7日，俄罗斯圣彼得堡，俄罗斯警察学院的女学员们正在进行阅兵演练，为即将举行的抗击纳粹胜利纪念日做准备。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (165,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209113549_alq9g5niv1.jpg',1,'图为当地时间2014年9月15日，伊拉克库尔吉克市西北50公里外的底比斯镇，一名库尔德斯坦自由党的伊朗籍库尔德女兵在镜头前留影。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (165,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209113550_kqw9c337dx.jpg',2,'当地时间2014年4月5日，巴勒斯坦西岸的耶利哥，25名巴勒斯坦女兵组成的总统护卫队正在接受训练。她们是首批入选总统护卫队的女兵，总统护卫队共有士兵2600名，他们都是巴勒斯坦部队的精英份子。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (165,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209113550_r3od8ni3ju.jpg',3,'5月15日，中国国家主席习近平在北京人民大会堂东门外广场举行仪式欢迎葡萄牙总统席尔瓦访华期间，解放军三军仪仗队女仪仗兵一展风采。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (165,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209113550_poq1s013lw.jpg',4,'当地时间2014年5月7日，越南奠边府市，越南的退伍军人，政党领导以及外交大使们出席奠边府战役胜利60周年，图为越南女兵身着军装在纪念仪式上进行阅兵展示。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (165,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209113551_eh8tc6wyax.jpg',5,'9月12日，解放军某特战旅女子特战队员们在野外特训。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (165,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209113551_9trjftyh6i.jpg',6,'当地时间2014年4月6日，巴勒斯坦西岸城市耶利哥，巴勒斯坦总统护卫队的女兵正在观看训练课程。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (165,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209113552_16dqpddo9r.jpg',7,'当地时间2014年5月29日，以色列基布兹斯德博克附近的内盖夫沙漠，以色列“狞猫营”的士兵们刚完成一次20公里的比赛。“狞猫”营成立于2001年，是以色列军队仅有的一支男女混编的步兵营，其中女兵占总人数的70%。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (165,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209113552_bsodmncdlp.jpg',8,'当地时间2014年12月1日，罗马尼亚首府布加勒斯特，一队罗马尼亚女兵为参加罗马尼亚国庆阅兵做准备。她们冒着严寒，面带微笑。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (165,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201412/20141209113553_berjmcbpmn.jpg',9,'当地时间2014年5月28日，泰国曼谷，一名女兵和战友在胜利纪念碑前站岗。5月22日，泰国军方陆军总司令巴育·占奥差发动军事政变，他与军方官员通过电视向全国宣布接管政府。并向公众宣布，他的军事集团已经监禁前总理英拉及其多位家人，但他们会被善待。');
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (143,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223173420_049xocsg6y.jpg',0,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (143,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223173420_2i93y1uyed.jpg',1,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (143,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223173421_qur7bdmuj1.jpg',2,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (143,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223173421_le6dcs65fv.jpg',3,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (143,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223173422_wpg8hjbm0k.jpg',4,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (78,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223174011_muwkqsdgq4.jpg',2,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (78,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223174011_b0nfmrmf8x.jpg',3,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (78,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223174010_75e0xyhrfi.jpg',4,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (78,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223174011_pqlqyh8ye6.jpg',5,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (141,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223175840_8j58oihs4t.jpg',0,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (141,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223175844_6r3k9ot7s3.jpg',1,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (141,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223175839_71g880fxd8.jpg',2,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (141,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223175840_4brgfv8vbq.jpg',3,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (141,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223175843_cr45dc9tpl.jpg',4,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (141,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223175841_ttklfuq7i0.jpg',5,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (141,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223175841_35qnii2sbt.jpg',6,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (141,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223175843_6v6lrpc8yp.jpg',7,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (141,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223175841_xcj2e7kv64.jpg',8,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (141,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223175840_13yidjxqxg.jpg',9,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (142,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223211125_97q8mau5l8.jpg',0,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (142,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223211125_s49ca7d5ci.jpg',1,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (142,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223211126_k8c0bmguak.jpg',2,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (142,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223211126_x2tlg2bw2t.jpg',3,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (142,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223211126_moj3xt6tg2.jpg',4,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (142,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223211126_9q1k7us2gq.jpg',5,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (142,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223211127_9gb5tt1dne.jpg',6,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (142,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223211127_s9a4gtmnjq.jpg',7,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (142,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223211128_y3iojjppsb.jpg',8,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (142,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223211128_n6kwcner3u.jpg',9,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (81,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223212829_vyo79ru5fl.jpg',2,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (81,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223212829_s9umb80qtt.jpg',3,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (81,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223212829_lo0pmry8fs.jpg',4,NULL);
INSERT INTO cms_info_image (f_info_id, f_name, f_image, f_index, f_text) VALUES (81,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151223212830_yqc3a9h3q4.jpg',5,NULL);
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,93,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,93,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,83,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,83,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,117,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,117,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,118,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,118,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,126,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,126,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,127,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,127,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,128,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,128,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,32,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,32,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,78,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,78,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,81,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,81,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,46,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,46,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,54,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,54,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,48,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,48,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,129,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,129,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,94,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,94,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,98,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,98,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,130,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,130,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,97,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,97,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,95,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,95,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,131,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,131,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,132,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,132,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,133,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,133,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,134,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,134,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,135,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,135,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,136,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,136,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,87,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,87,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,137,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,137,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,138,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,138,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,139,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,139,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,140,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,140,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,99,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,99,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,35,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,35,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,141,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,141,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,80,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,80,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,142,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,142,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,143,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,143,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,26,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,26,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,50,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,50,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,144,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,144,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,145,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,145,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,146,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,146,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,148,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,148,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,149,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,149,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,150,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,150,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (0,100,'0');
INSERT INTO cms_info_membergroup (f_membergroup_id, f_info_id, f_is_view_perm) VALUES (1,100,'0');
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (47,40,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (28,42,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (25,42,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (24,42,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (30,44,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (44,38,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (43,38,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (73,38,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (45,38,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (49,40,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (117,69,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (118,69,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (52,42,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (27,42,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (36,42,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (33,42,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (34,42,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (46,38,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (54,42,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (48,40,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (135,77,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (134,79,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (133,79,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (93,78,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (83,79,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (137,80,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (138,80,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (127,42,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (99,44,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (35,42,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (140,81,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (139,81,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (26,40,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (50,40,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (144,69,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (145,69,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (146,69,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (148,78,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (149,78,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (150,79,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (128,42,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (126,42,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (32,44,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (100,44,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (143,88,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (142,87,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (78,88,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (141,88,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (81,90,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (87,82,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (130,83,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (95,85,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (98,83,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (97,83,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (163,94,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (166,82,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (167,82,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (168,80,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (169,80,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (165,88,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (151,82,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (170,81,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (172,82,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (173,82,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (174,81,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (180,44,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (80,90,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (164,87,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (129,84,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (131,96,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (132,95,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (94,84,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (136,77,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (162,93,0);
INSERT INTO cms_info_node (f_info_id, f_node_id, f_node_index) VALUES (53,44,0);
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (93,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (83,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (117,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (118,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (126,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (127,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (128,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (32,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (78,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (81,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (46,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (54,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (48,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (129,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (94,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (98,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (130,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (97,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (95,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (131,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (132,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (133,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (134,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (135,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (136,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (87,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (137,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (138,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (139,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (140,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (99,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (35,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (141,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (80,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (142,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (143,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (26,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (50,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (144,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (145,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (146,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (148,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (149,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (150,1,'0');
INSERT INTO cms_info_org (f_info_id, f_org_id, f_is_view_perm) VALUES (100,1,'0');
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (32,11,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (49,8,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (50,8,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (48,8,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (47,8,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (30,10,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (44,2,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (43,2,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (73,2,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (45,2,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (128,13,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (53,11,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (24,8,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (25,12,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (34,10,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (127,10,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (35,2,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (28,12,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (36,10,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (27,13,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (126,10,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (99,10,0);
INSERT INTO cms_info_special (f_info_id, f_special_id, f_special_index) VALUES (26,12,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (30,56,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (30,71,1);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (30,72,2);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (30,73,3);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (53,63,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (53,64,1);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (53,65,2);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (53,66,3);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (52,67,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (52,68,1);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (52,69,2);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (52,70,3);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (54,56,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (54,57,1);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (54,62,2);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (78,78,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (78,79,1);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (78,80,2);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (128,81,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (128,82,1);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (128,83,2);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (166,84,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (168,84,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (170,84,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (167,84,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (151,84,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (169,84,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (87,84,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (140,84,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (137,84,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (139,84,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (138,84,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (174,84,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (173,84,0);
INSERT INTO cms_info_tag (f_info_id, f_tag_id, f_tag_index) VALUES (172,84,0);
INSERT INTO cms_member_group (f_membergroup_id, f_name, f_description, f_ip, f_type, f_seq) VALUES (0,'游客组',NULL,NULL,1,0);
INSERT INTO cms_member_group (f_membergroup_id, f_name, f_description, f_ip, f_type, f_seq) VALUES (1,'普通会员',NULL,NULL,0,2147483647);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (1,1,'node_home','首页',0,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (2,1,'info','新闻',0,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (3,1,'node','新闻',0,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (4,1,'node','图集',1,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (5,1,'info','图集',1,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (6,1,'node','下载',2,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (7,1,'info','下载',2,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (8,1,'node','视频',3,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (9,1,'info','视频(电影)',3,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (10,1,'node','产品',4,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (11,1,'info','产品(服饰/内衣)',7,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (12,1,'site','站点模型',2147483647,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (13,1,'node','招聘',6,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (15,1,'global','系统',2147483647,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (16,1,'node','转向链接',7,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (17,1,'special','专题',10,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (19,1,'node','文库',5,'doc');
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (20,1,'info','文库',10,'doc');
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (21,1,'info','招聘',11,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (23,1,'info','视频(综艺)',4,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (24,1,'info','产品(手机数码)',5,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (25,1,'info','产品(电脑办公)',6,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (26,1,'info','产品(图书/音像)',8,NULL);
INSERT INTO cms_model (f_model_id, f_site_id, f_type, f_name, f_seq, f_number) VALUES (27,1,'info','产品(家用电器)',9,NULL);
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (2,'template','/info_news.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (3,'defPage','true');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (3,'nodeExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (3,'staticPage','1');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (3,'listTemplate','/list_news.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (3,'generateInfo','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (3,'coverTemplate','/cover_news.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (3,'generateNode','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (3,'infoPath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (3,'nodePath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (3,'infoExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (3,'staticMethod','3');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (1,'infoExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (1,'infoPath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (1,'defPage','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (1,'nodeExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (1,'template','/index.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (1,'nodePath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (1,'generateInfo','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (1,'staticPage','1');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (1,'staticMethod','3');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (1,'generateNode','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (4,'defPage','true');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (4,'nodeExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (4,'staticPage','1');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (4,'listTemplate','/list_photo.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (4,'generateInfo','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (4,'coverTemplate','/cover_photo.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (4,'generateNode','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (4,'infoPath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (4,'nodePath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (4,'infoExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (4,'staticMethod','3');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (5,'template','/info_photo.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (6,'defPage','true');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (6,'nodeExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (6,'staticPage','1');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (6,'listTemplate','/list_download.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (6,'generateInfo','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (6,'coverTemplate','/cover_download.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (6,'generateNode','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (6,'infoPath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (6,'nodePath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (6,'infoExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (6,'staticMethod','3');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (7,'template','/info_download.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (8,'defPage','true');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (8,'nodeExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (8,'staticPage','1');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (8,'listTemplate','/list_video.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (8,'generateInfo','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (8,'coverTemplate','/cover_video.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (8,'generateNode','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (8,'infoPath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (8,'nodePath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (8,'infoExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (8,'staticMethod','3');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (9,'template','/info_video.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (10,'defPage','true');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (10,'nodeExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (10,'staticPage','1');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (10,'listTemplate','/list_product.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (10,'generateInfo','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (10,'coverTemplate','/cover_product.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (10,'generateNode','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (10,'infoPath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (10,'nodePath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (10,'infoExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (10,'staticMethod','3');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (11,'template','/info_product.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (13,'defPage','true');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (13,'nodeExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (13,'staticPage','1');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (13,'listTemplate','/list_recruitment.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (13,'generateInfo','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (13,'coverTemplate','/list_recruitment.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (13,'generateNode','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (13,'infoPath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (13,'nodePath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (13,'infoExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (13,'staticMethod','4');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (16,'listTemplate','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (16,'defPage','true');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (16,'infoPath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (16,'coverTemplate','/list.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (16,'nodePath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (16,'nodeExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (16,'generateInfo','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (16,'generateNode','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (16,'staticMethod','4');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (16,'infoExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (16,'staticPage','1');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (19,'listTemplate','/list_doc.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (19,'infoPath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (19,'infoExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (19,'generateInfo','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (19,'nodePath','');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (19,'defPage','true');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (19,'staticPage','1');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (19,'nodeExtension','.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (19,'generateNode','false');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (19,'staticMethod','3');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (19,'coverTemplate','/list_doc.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (20,'template','/info_doc.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (21,'template','/info_recruitment.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (23,'template','/info_video.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (24,'template','/info_product.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (25,'template','/info_product.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (26,'template','/info_product.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (27,'template','/info_product.html');
INSERT INTO cms_model_custom (f_model_id, f_key, f_value) VALUES (17,'template','/sys_special.html');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (1,1,1,2,'名称','name',NULL,NULL,'0',0,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (2,1,1,2,'编码','number',NULL,NULL,'0',1,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (5,1,5,2,'栏目模型','nodeModel',NULL,NULL,'0',2,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (6,1,5,2,'文档模型','infoModel',NULL,NULL,'0',3,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (7,1,1,2,'栏目页模板','nodeTemplate',NULL,NULL,'0',4,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (8,1,1,2,'文档页模板','infoTemplate',NULL,NULL,'0',5,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (9,1,1,2,'栏目页静态化','generateNode',NULL,NULL,'0',6,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (10,1,1,2,'文档页静态化','generateInfo',NULL,NULL,'0',7,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (11,1,5,2,'静态化方式','staticMethod',NULL,NULL,'0',8,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (12,1,1,2,'静态化页数','staticPage',NULL,'1','1',9,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (199,4,1,2,'所属栏目','parent',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (200,4,1,2,'名称','name',NULL,NULL,'0',1,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (201,4,1,2,'编码','number',NULL,NULL,'0',2,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (202,4,1,2,'转向链接','link',NULL,NULL,'0',3,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (203,4,5,2,'新窗口打开','newWindow',NULL,NULL,'0',4,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (216,4,5,2,'栏目模型','nodeModel',NULL,NULL,'0',5,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (217,4,5,2,'文档模型','infoModel',NULL,NULL,'0',6,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (235,2,1,2,'栏目','node',NULL,NULL,'0',0,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (237,2,1,2,'专题','specials',NULL,NULL,'0',1,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (239,2,1,2,'颜色','color',NULL,NULL,'0',4,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (242,2,1,2,'关键词','tagKeywords',NULL,NULL,'0',5,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (243,2,6,2,'描述','metaDescription',NULL,NULL,'0',6,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (246,2,1,2,'优先级','priority',NULL,NULL,'0',7,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (247,2,2,2,'发布时间','publishDate',NULL,NULL,'0',8,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (248,2,1,2,'来源','source',NULL,NULL,'0',9,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (249,2,1,2,'作者','author',NULL,NULL,'0',10,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (250,2,1,2,'属性','attributes',NULL,NULL,'0',11,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (255,2,50,2,'正文','text',NULL,NULL,'0',13,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (256,3,1,2,'所属栏目','parent',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (258,3,1,2,'编码','number',NULL,NULL,'0',2,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (259,3,1,2,'转向链接','link',NULL,NULL,'0',3,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (260,3,5,2,'新窗口打开','newWindow',NULL,NULL,'0',4,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (261,3,1,2,'关键词','metaKeywords',NULL,NULL,'0',5,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (262,3,1,2,'描述','metaDescription',NULL,NULL,'0',6,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (263,3,5,2,'栏目模型','nodeModel',NULL,NULL,'0',8,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (264,3,5,2,'文档模型','infoModel',NULL,NULL,'0',9,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (265,3,1,2,'栏目页模板','nodeTemplate',NULL,NULL,'0',10,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (266,3,1,2,'文档页模板','infoTemplate',NULL,NULL,'0',11,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (267,3,1,2,'栏目页静态化','generateNode',NULL,NULL,'0',12,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (268,3,1,2,'文档页静态化','generateInfo',NULL,NULL,'0',13,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (269,3,5,2,'静态化方式','staticMethod',NULL,NULL,'0',14,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (270,3,1,2,'静态化页数','staticPage',NULL,'1','0',15,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (273,1,1,2,'关键词','metaKeywords',NULL,NULL,'0',10,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (274,1,1,2,'描述','metaDescription',NULL,NULL,'0',11,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (275,5,1,2,'栏目','node',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (276,5,1,2,'标题','title',NULL,NULL,'0',1,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (278,5,2,2,'发布时间','publishDate',NULL,NULL,'0',2,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (279,5,51,2,'图片集','images',NULL,NULL,'0',7,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (281,5,7,2,'标题图','smallImage',NULL,NULL,'0',6,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (282,6,1,2,'所属栏目','parent',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (283,6,1,2,'名称','name',NULL,NULL,'0',1,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (284,6,1,2,'编码','number',NULL,NULL,'0',2,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (287,6,1,2,'转向链接','link',NULL,NULL,'0',3,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (288,6,5,2,'新窗口打开','newWindow',NULL,NULL,'0',4,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (289,6,1,2,'关键词','metaKeywords',NULL,NULL,'0',5,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (290,6,1,2,'描述','metaDescription',NULL,NULL,'0',6,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (292,6,5,2,'栏目模型','nodeModel',NULL,NULL,'0',7,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (293,6,5,2,'文档模型','infoModel',NULL,NULL,'0',8,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (294,6,1,2,'栏目页模板','nodeTemplate',NULL,NULL,'0',9,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (295,6,1,2,'文档页模板','infoTemplate',NULL,NULL,'0',10,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (296,6,1,2,'栏目页静态化','generateNode',NULL,NULL,'0',11,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (297,6,1,2,'文档页静态化','generateInfo',NULL,NULL,'0',12,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (298,6,5,2,'静态化方式','staticMethod',NULL,NULL,'0',13,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (299,6,1,2,'静态化页数','staticPage',NULL,'1','0',14,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (303,7,1,2,'栏目','node',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (304,7,1,2,'标题','title',NULL,NULL,'0',1,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (305,7,50,2,'软件介绍','text',NULL,NULL,'0',12,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (306,7,4,0,'软件类型','type',NULL,'国产软件','0',3,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (307,7,4,0,'授权方式','license',NULL,'免费软件','0',4,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (308,7,4,0,'界面语言','language',NULL,'简体中文','0',5,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (311,7,9,2,'下载文件','file',NULL,NULL,'0',11,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (312,8,1,2,'所属栏目','parent',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (313,8,1,2,'名称','name',NULL,NULL,'0',1,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (314,8,1,2,'编码','number',NULL,NULL,'0',2,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (315,8,5,2,'栏目模型','nodeModel',NULL,NULL,'0',3,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (316,8,5,2,'文档模型','infoModel',NULL,NULL,'0',4,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (318,9,1,2,'栏目','node',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (319,9,1,2,'标题','title',NULL,NULL,'0',1,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (320,9,2,2,'发布时间','publishDate',NULL,NULL,'0',7,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (321,9,8,2,'视频','video',NULL,NULL,'0',11,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (322,9,7,2,'标题图','smallImage',NULL,NULL,'0',9,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (323,10,1,2,'所属栏目','parent',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (324,10,1,2,'名称','name',NULL,NULL,'0',1,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (325,10,1,2,'编码','number',NULL,NULL,'0',2,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (326,11,1,2,'栏目','node',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (327,11,1,2,'标题','title',NULL,NULL,'0',1,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (328,11,7,2,'标题图','smallImage',NULL,NULL,'0',8,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (329,10,5,2,'栏目模型','nodeModel',NULL,NULL,'0',3,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (330,10,5,2,'文档模型','infoModel',NULL,NULL,'0',4,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (331,11,1,0,'优惠价','price',NULL,NULL,'0',3,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (332,11,1,0,'市场价','marketPrice',NULL,NULL,'1',4,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (334,11,4,0,'商品库存','stock',NULL,'有货','0',10,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (335,11,50,1,'商品介绍','introduce',NULL,NULL,'0',11,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (336,11,50,1,'规格参数','specification',NULL,NULL,'0',12,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (337,11,50,1,'包装清单','packingList',NULL,NULL,'0',13,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (338,11,50,1,'售后服务','services',NULL,NULL,'0',14,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (339,7,3,0,'运行环境','system',NULL,'winxp,win7,win8','0',7,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (340,7,7,2,'标题图','smallImage',NULL,NULL,'0',9,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (343,3,5,2,'审核流程','workflow',NULL,NULL,'0',7,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (344,12,1,0,'公司名称','company',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (345,12,1,0,'备案号','icp',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (346,13,1,2,'所属栏目','parent',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (347,13,1,2,'名称','name',NULL,NULL,'0',2147483647,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (348,13,1,2,'编码','number',NULL,NULL,'0',2147483647,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (349,13,1,2,'关键词','metaKeywords',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (350,13,1,2,'描述','metaDescription',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (351,13,5,2,'栏目模型','nodeModel',NULL,NULL,'0',2147483647,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (352,13,5,2,'文档模型','infoModel',NULL,NULL,'0',2147483647,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (364,13,3,2,'浏览权限','viewGroups',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (365,1,3,2,'浏览权限','viewGroups',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (366,15,1,0,'版权信息','poweredby',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (367,16,1,2,'所属栏目','parent',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (368,16,1,2,'名称','name',NULL,NULL,'0',1,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (369,16,1,2,'转向链接','link',NULL,NULL,'0',3,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (370,16,5,2,'新窗口打开','newWindow',NULL,NULL,'0',4,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (371,16,5,2,'栏目模型','nodeModel',NULL,NULL,'0',2,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (372,17,1,2,'标题','title',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (373,17,1,2,'关键词','metaKeywords',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (374,17,6,2,'描述','metaDescription',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (375,17,1,2,'类别','category',NULL,NULL,'0',2147483647,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (376,17,2,2,'创建日期','creationDate',NULL,NULL,'0',2147483647,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (377,17,5,2,'模型','model',NULL,NULL,'0',2147483647,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (378,17,1,2,'独立模版','specialTemplate',NULL,NULL,'0',2147483647,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (379,17,4,2,'推荐','recommend',NULL,NULL,'0',2147483647,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (380,17,1,2,'浏览次数','views',NULL,NULL,'0',2147483647,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (381,17,7,2,'小图','smallImage',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (382,17,7,2,'大图','largeImage',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (383,17,52,2,'文件集','files',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (384,17,8,2,'视频','video',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (385,17,51,2,'图片集','images',NULL,NULL,'0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (386,5,1,2,'属性','attributes',NULL,NULL,'0',5,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (387,5,1,2,'关键词','tagKeywords',NULL,NULL,'0',3,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (388,5,6,2,'描述','metaDescription',NULL,NULL,'0',4,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (389,11,2,2,'发布时间','publishDate',NULL,NULL,'0',2,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (390,7,1,2,'完整标题','fullTitle',NULL,NULL,'0',2,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (392,2,7,2,'标题图','smallImage',NULL,NULL,'0',12,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (393,3,1,2,'名称','name',NULL,NULL,'1',1,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (394,2,1,2,'标题','title',NULL,NULL,'1',2,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (395,4,5,2,'审核流程','workflow',NULL,NULL,'0',7,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (396,2,6,2,'完整标题','fullTitle',NULL,NULL,'0',3,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (397,9,1,2,'属性','attributes',NULL,NULL,'0',8,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (399,11,1,2,'属性','attributes',NULL,NULL,'0',7,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (400,7,1,2,'属性','attributes',NULL,NULL,'0',8,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (401,19,1,2,'所属栏目','parent',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (402,19,1,2,'名称','name',NULL,NULL,'1',1,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (403,19,1,2,'编码','number',NULL,NULL,'0',2,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (404,19,1,2,'转向链接','link',NULL,NULL,'0',3,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (405,19,5,2,'新窗口打开','newWindow',NULL,NULL,'0',4,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (406,19,1,2,'关键词','metaKeywords',NULL,NULL,'0',5,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (407,19,1,2,'描述','metaDescription',NULL,NULL,'0',6,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (408,19,5,2,'审核流程','workflow',NULL,NULL,'0',7,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (409,19,5,2,'栏目模型','nodeModel',NULL,NULL,'0',8,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (410,19,5,2,'文档模型','infoModel',NULL,NULL,'0',9,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (411,19,1,2,'栏目页模板','nodeTemplate',NULL,NULL,'0',10,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (412,19,1,2,'文档页模板','infoTemplate',NULL,NULL,'0',11,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (413,19,1,2,'栏目页静态化','generateNode',NULL,NULL,'0',12,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (414,19,1,2,'文档页静态化','generateInfo',NULL,NULL,'0',13,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (415,19,5,2,'静态化方式','staticMethod',NULL,NULL,'0',14,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (416,19,1,2,'静态化页数','staticPage',NULL,'1','0',15,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (417,20,1,2,'栏目','node',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (419,20,1,2,'标题','title',NULL,NULL,'1',1,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (425,20,2,2,'发布时间','publishDate',NULL,NULL,'0',4,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (432,21,1,2,'栏目','node',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (434,21,1,2,'标题','title',NULL,NULL,'1',1,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (440,21,2,2,'发布时间','publishDate',NULL,NULL,'0',2,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (453,20,1,2,'关键词','tagKeywords',NULL,NULL,'0',2,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (454,20,6,2,'描述','metaDescription',NULL,NULL,'0',3,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (455,20,10,2,'文库','doc',NULL,NULL,'0',6,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (456,21,1,0,'公司名称','company',NULL,NULL,'0',3,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (457,21,1,0,'部门名称','dept',NULL,NULL,'0',4,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (458,21,1,0,'招聘人数','number',NULL,NULL,'0',5,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (459,21,1,0,'薪酬待遇','salary',NULL,NULL,'0',6,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (460,21,1,0,'工作地点','location',NULL,NULL,'0',9,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (461,21,6,0,'岗位职责','responsibilities',NULL,NULL,'0',11,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (462,21,6,0,'岗位要求','qualifications',NULL,NULL,'0',12,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (463,21,1,0,'工作经验','experience',NULL,NULL,'0',7,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (464,21,1,0,'最低学历','education',NULL,NULL,'0',8,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (465,21,5,0,'工作性质','jobtype',NULL,NULL,'0',10,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (466,9,6,2,'完整标题','fullTitle',NULL,NULL,'0',2,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (467,9,101,3,'地区','p1',NULL,NULL,'0',4,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (468,9,101,3,'类型','p2',NULL,NULL,'0',5,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (469,9,101,3,'年代','p3',NULL,NULL,'0',6,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (480,23,1,2,'栏目','node',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (481,23,1,2,'标题','title',NULL,NULL,'0',1,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (482,23,6,2,'完整标题','fullTitle',NULL,NULL,'0',2,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (483,23,101,3,'地区','p1',NULL,NULL,'0',4,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (484,23,101,3,'分类','p2',NULL,NULL,'0',5,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (487,23,1,2,'属性','attributes',NULL,NULL,'0',7,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (488,23,7,2,'标题图','smallImage',NULL,NULL,'0',8,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (489,23,8,2,'视频','video',NULL,NULL,'0',10,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (490,23,2,2,'发布时间','publishDate',NULL,NULL,'0',6,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (491,9,1,2,'关键词','tagKeywords',NULL,NULL,'0',3,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (492,23,1,2,'关键词','tagKeywords',NULL,NULL,'0',3,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (493,9,1,0,'flash地址','flashaddr',NULL,NULL,'0',10,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (494,23,1,0,'flash地址','flashaddr',NULL,NULL,'0',9,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (495,11,101,3,'面料','p1',NULL,NULL,'0',5,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (496,11,101,3,'适用季节','p2',NULL,NULL,'0',6,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (498,24,1,2,'栏目','node',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (499,24,1,2,'标题','title',NULL,NULL,'0',1,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (500,24,2,2,'发布时间','publishDate',NULL,NULL,'0',2,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (501,24,1,0,'优惠价','price',NULL,NULL,'0',3,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (502,24,1,0,'市场价','marketPrice',NULL,NULL,'1',4,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (503,24,101,3,'有效像素','p1',NULL,NULL,'0',5,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (504,24,101,3,'机身颜色','p2',NULL,NULL,'0',6,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (505,24,1,2,'属性','attributes',NULL,NULL,'0',7,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (506,24,7,2,'标题图','smallImage',NULL,NULL,'0',8,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (508,24,4,0,'商品库存','stock',NULL,'有货','0',11,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (509,24,50,1,'商品介绍','introduce',NULL,NULL,'0',12,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (510,24,50,1,'规格参数','specification',NULL,NULL,'0',13,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (511,24,50,1,'包装清单','packingList',NULL,NULL,'0',14,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (512,24,50,1,'售后服务','services',NULL,NULL,'0',15,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (513,25,1,2,'栏目','node',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (514,25,1,2,'标题','title',NULL,NULL,'0',1,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (515,25,2,2,'发布时间','publishDate',NULL,NULL,'0',2,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (516,25,1,0,'优惠价','price',NULL,NULL,'0',3,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (517,25,1,0,'市场价','marketPrice',NULL,NULL,'1',4,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (518,25,101,3,'尺寸','p1',NULL,NULL,'0',5,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (519,25,101,3,'处理器','p2',NULL,NULL,'0',6,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (520,25,1,2,'属性','attributes',NULL,NULL,'0',7,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (521,25,7,2,'标题图','smallImage',NULL,NULL,'0',8,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (523,25,4,0,'商品库存','stock',NULL,'有货','0',10,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (524,25,50,1,'商品介绍','introduce',NULL,NULL,'0',11,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (525,25,50,1,'规格参数','specification',NULL,NULL,'0',12,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (526,25,50,1,'包装清单','packingList',NULL,NULL,'0',13,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (527,25,50,1,'售后服务','services',NULL,NULL,'0',14,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (528,26,1,2,'栏目','node',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (529,26,1,2,'标题','title',NULL,NULL,'0',1,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (530,26,2,2,'发布时间','publishDate',NULL,NULL,'0',2,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (531,26,1,0,'优惠价','price',NULL,NULL,'0',3,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (532,26,1,0,'市场价','marketPrice',NULL,NULL,'1',4,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (533,26,101,3,'类别','p1',NULL,NULL,'0',5,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (535,26,1,2,'属性','attributes',NULL,NULL,'0',6,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (536,26,7,2,'标题图','smallImage',NULL,NULL,'0',7,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (538,26,4,0,'商品库存','stock',NULL,'有货','0',9,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (539,26,50,1,'商品介绍','introduce',NULL,NULL,'0',10,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (540,26,50,1,'规格参数','specification',NULL,NULL,'0',11,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (541,26,50,1,'包装清单','packingList',NULL,NULL,'0',12,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (542,26,50,1,'售后服务','services',NULL,NULL,'0',13,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (543,27,1,2,'栏目','node',NULL,NULL,'0',0,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (544,27,1,2,'标题','title',NULL,NULL,'0',1,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (545,27,2,2,'发布时间','publishDate',NULL,NULL,'0',2,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (546,27,1,0,'优惠价','price',NULL,NULL,'0',3,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (547,27,1,0,'市场价','marketPrice',NULL,NULL,'1',4,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (548,27,101,3,'尺寸','p1',NULL,NULL,'0',5,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (549,27,101,3,'分辨率','p2',NULL,NULL,'0',6,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (550,27,1,2,'属性','attributes',NULL,NULL,'0',7,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (551,27,7,2,'标题图','smallImage',NULL,NULL,'0',8,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (553,27,4,0,'商品库存','stock',NULL,'有货','0',10,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (554,27,50,1,'商品介绍','introduce',NULL,NULL,'0',11,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (555,27,50,1,'规格参数','specification',NULL,NULL,'0',12,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (556,27,50,1,'包装清单','packingList',NULL,NULL,'0',13,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (557,27,50,1,'售后服务','services',NULL,NULL,'0',14,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (558,24,7,2,'正文图','largeImage',NULL,NULL,'0',9,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (559,7,5,0,'推荐星级','star',NULL,'★★★★★','0',6,'1','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (560,7,7,2,'正文图','largeImage',NULL,NULL,'0',10,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (561,20,7,2,'标题图','smallImage',NULL,NULL,'0',5,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (562,15,5,0,'第三方登录','oauth',NULL,'0','0',2147483647,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (563,25,7,2,'正文图','largeImage',NULL,NULL,'0',9,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (564,11,7,2,'正文图','largeImage',NULL,NULL,'0',9,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (565,26,7,2,'正文图','largeImage',NULL,NULL,'0',8,'0','0');
INSERT INTO cms_model_field (f_modefiel_id, f_model_id, f_type, f_inner_type, f_label, f_name, f_prompt, f_def_value, f_is_required, f_seq, f_is_dbl_column, f_is_disabled) VALUES (566,27,7,2,'正文图','largeImage',NULL,NULL,'0',9,'0','0');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (201,'datePattern','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (201,'cols','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (201,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (201,'imageHeight','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (201,'rows','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (201,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (201,'maxlength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (201,'editorToolbar','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (201,'options','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (201,'imageWatermark','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (201,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (201,'imageWidth','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (201,'imageScale','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (279,'thumbnail','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (279,'thumbnailWidth','130');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (279,'imageWatermark','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (279,'imageHeight','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (279,'imageWidth','2000');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (279,'imageScale','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (279,'thumbnailHeight','80');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (281,'imageHeight','260');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (281,'imageWidth','390');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (281,'imageWatermark','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (281,'imageScale','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (306,'options','国产软件\r\n国外软件');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (307,'options','免费软件\r\n收费软件');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (308,'options','简体中文\r\n繁体中文\r\n英文');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (322,'imageHeight','180');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (322,'imageWidth','320');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (322,'imageWatermark','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (322,'imageScale','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (328,'imageHeight','200');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (328,'imageWidth','200');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (331,'width','250');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (331,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (331,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (332,'width','250');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (332,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (332,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (334,'options','有货\r\n缺货');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (335,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (335,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (335,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (336,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (336,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (336,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (337,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (337,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (337,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (338,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (338,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (338,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (328,'imageWatermark','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (328,'imageScale','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (339,'options','winxp\r\nwin7\r\nwin8\r\nwin2000\r\nwin2003\r\nwin2008\r\nlinux\r\nunix\r\nmac\r\n');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (340,'imageHeight','50');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (340,'imageWidth','50');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (344,'width','180');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (344,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (344,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (345,'width','180');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (345,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (345,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (366,'width','180');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (366,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (366,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (381,'imageHeight','120');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (381,'imageScale','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (381,'imageWidth','180');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (381,'imageWatermark','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (382,'imageHeight','480');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (382,'imageScale','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (382,'imageWidth','480');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (382,'imageWatermark','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (385,'thumbnail','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (385,'imageScale','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (385,'thumbnailHeight','77');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (385,'thumbnailWidth','116');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (385,'imageWidth','1500');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (385,'imageWatermark','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (381,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (382,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (385,'imageHeight','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (385,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (340,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (340,'imageScale','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (340,'imageWatermark','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (322,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (281,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (392,'imageHeight','120');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (392,'imageScale','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (392,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (392,'imageWidth','180');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (392,'imageWatermark','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (456,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (456,'width','180');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (456,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (457,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (457,'width','180');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (457,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (458,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (458,'width','180');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (458,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (459,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (459,'width','180');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (459,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (460,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (460,'width','180');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (460,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (461,'height','80');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (461,'width','500');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (461,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (462,'height','80');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (462,'width','500');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (462,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (434,'width','180');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (434,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (434,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (463,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (463,'width','180');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (463,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (464,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (464,'width','180');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (464,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (465,'width','120');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (465,'options','全职\r\n兼职');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (279,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (469,'options','100|2015\r\n99|2014\r\n11|2013-2011\r\n10|00年代\r\n9|90年代\r\n8|80年代\r\n7|更早');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (468,'options','0|喜剧\r\n1|悲剧\r\n2|爱情\r\n3|动作\r\n4|枪战\r\n5|犯罪\r\n6|惊悚\r\n7|恐怖\r\n8|悬疑\r\n9|动画\r\n10|家庭\r\n11|奇幻\r\n12|魔幻\r\n13|科幻\r\n14|战争\r\n15|武侠\r\n16|青春\r\n17|功夫\r\n18|灾难');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (467,'options','0|华语\r\n1|美国\r\n2|欧洲\r\n3|韩国\r\n4|日本\r\n5|泰国\r\n6|其它');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (483,'options','0|华语\r\n1|美国\r\n2|欧洲\r\n3|韩国\r\n4|日本\r\n5|泰国\r\n6|其它');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (484,'options','1|自制剧\r\n2|播报\r\n3|访谈\r\n4|搞笑\r\n5|游戏\r\n6|选秀\r\n7|时尚\r\n8|其它\r\n9|情感\r\n10|盛会\r\n11|曲艺\r\n12|粤语\r\n13|美食\r\n14|少儿\r\n15|脱口秀\r\n16|职场\r\n17|相亲\r\n18|歌舞\r\n19|伦理\r\n20|真人秀');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (488,'imageWidth','320');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (488,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (488,'imageWatermark','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (488,'imageHeight','180');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (488,'imageScale','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (493,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (493,'width','500');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (493,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (494,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (494,'width','500');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (494,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (495,'options','0|聚酯纤维\r\n1|锦纶\r\n2|竹浆纤维面料\r\n3|再生纤维面料\r\n4|棉质面料\r\n5|莫代尔面料\r\n6|真丝面料\r\n7|莱卡面料\r\n8|其它');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (496,'options','0|冬季\r\n1|夏季\r\n2|春秋季\r\n3|春季\r\n4|秋季\r\n5|秋冬季\r\n6|四季');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (501,'width','250');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (501,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (501,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (502,'width','250');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (502,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (502,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (503,'options','0|200-499万\r\n1|500-799万\r\n2|800-1200万\r\n3|1200-1599万\r\n4|1600万以上');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (504,'options','0|白色\r\n1|黑色\r\n2|灰色\r\n3|金色\r\n4|银色\r\n5|红色\r\n6|蓝色\r\n7|粉色\r\n8|黄色\r\n9|绿色\r\n10|橙色\r\n11|紫色\r\n12|其它');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (506,'imageWidth','200');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (506,'imageWatermark','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (506,'imageHeight','200');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (506,'imageScale','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (508,'options','有货\r\n缺货');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (509,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (509,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (509,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (510,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (510,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (510,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (511,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (511,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (511,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (512,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (512,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (512,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (516,'width','250');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (516,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (516,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (517,'width','250');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (517,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (517,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (518,'options','0|10.1英寸及以下\r\n1|11英寸\r\n2|12英寸\r\n3|13英寸\r\n4|14英寸\r\n5|15英寸\r\n6|16英寸-17英寸\r\n7|17英寸以上');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (519,'options','0|Intel i3\r\n1|Intel i5\r\n2|Intel i7\r\n3|AMD A6\r\n4|AMD A8\r\n5|AMD A10\r\n6|其他Intel平台\r\n7|其他AMD平台');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (521,'imageWidth','200');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (521,'imageWatermark','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (521,'imageHeight','200');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (521,'imageScale','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (523,'options','有货\r\n缺货');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (524,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (524,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (524,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (525,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (525,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (525,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (526,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (526,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (526,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (527,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (527,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (527,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (531,'width','250');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (531,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (531,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (532,'width','250');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (532,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (532,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (533,'options','0|文学\r\n1|少儿\r\n2|教育\r\n3|管理\r\n4|励志与成功\r\n5|人文社科\r\n6|生活\r\n7|艺术\r\n8|科技\r\n9|计算机与互联网');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (536,'imageWidth','200');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (536,'imageWatermark','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (536,'imageHeight','200');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (536,'imageScale','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (538,'options','有货\r\n缺货');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (539,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (539,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (539,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (540,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (540,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (540,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (541,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (541,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (541,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (542,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (542,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (542,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (546,'width','250');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (546,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (546,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (547,'width','250');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (547,'validation','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (547,'maxLength','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (548,'options','0|32英寸及以下\r\n1|32英寸及以下\r\n2|39-40英寸\r\n3|42-43英寸\r\n4|46-49英寸\r\n5|50-52英寸\r\n6|55英寸\r\n7|58-60英寸\r\n8|65英寸及以上\r\n');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (549,'options','0|超高清4K（3840*2160）\r\n1|全高清（1920*1080）\r\n2|高清（1366*768）\r\n3|其它');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (551,'imageWidth','200');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (551,'imageWatermark','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (551,'imageHeight','200');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (551,'imageScale','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (553,'options','有货\r\n缺货');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (554,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (554,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (554,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (555,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (555,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (555,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (556,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (556,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (556,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (557,'editorToolbar','Standard');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (557,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (557,'height','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (558,'imageWidth','480');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (558,'imageWatermark','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (558,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (558,'imageHeight','480');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (558,'imageScale','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (559,'options','★★★★★\r\n★★★★☆\r\n★★★☆☆\r\n★★☆☆☆\r\n★☆☆☆☆');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (559,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (560,'imageWidth','480');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (560,'imageWatermark','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (560,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (560,'imageHeight','480');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (560,'imageScale','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (561,'imageWidth','80');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (561,'imageWatermark','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (561,'imageHeight','113');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (561,'imageScale','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (561,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (562,'options','0|关闭\r\n1|未购买\r\n2|未设置\r\n3|开启');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (562,'width','');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (563,'imageWidth','480');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (563,'imageWatermark','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (563,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (563,'imageHeight','480');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (563,'imageScale','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (564,'imageWidth','480');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (564,'imageWatermark','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (564,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (564,'imageHeight','480');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (564,'imageScale','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (565,'imageWidth','480');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (565,'imageWatermark','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (565,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (565,'imageHeight','480');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (565,'imageScale','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (566,'imageWidth','480');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (566,'imageWatermark','true');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (566,'imageExact','false');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (566,'imageHeight','480');
INSERT INTO cms_model_field_custom (f_modefiel_id, f_key, f_value) VALUES (566,'imageScale','true');
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (1,1,NULL,1,1,NULL,2,'index','首页','0000',0,'000a','2013-02-21 20:59:27',-43,140,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (36,1,1,1,3,NULL,2,'news','新闻','0000-0000',1,'0005','2013-03-04 22:18:36',-9,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (38,1,36,1,3,NULL,2,'yule','娱乐','0000-0000-0003',2,'0000','2013-03-04 22:18:42',-7,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (40,1,36,1,3,NULL,2,'sport','体育','0000-0000-0004',2,'0000','2013-03-18 01:27:48',-2,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (42,1,36,1,3,NULL,2,'domestic','国内','0000-0000-0000',2,'0001','2013-03-18 01:30:03',-7,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (44,1,36,1,3,NULL,2,NULL,'国际','0000-0000-0001',2,'0000','2013-03-18 01:33:48',-19,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (48,1,1,1,4,NULL,5,'photo','图片','0000-0001',1,'0003','2013-08-06 02:11:53',2,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (49,1,1,1,6,NULL,7,'download','下载','0000-0004',1,'0003','2013-08-06 08:45:34',1,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (51,1,1,1,8,NULL,9,'video','视频','0000-0002',1,'0003','2013-08-08 01:42:51',0,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (52,1,1,1,10,NULL,11,'product','产品','0000-0003',1,'0005','2013-08-08 05:56:39',1,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (67,1,1,1,16,NULL,NULL,'guestbook','留言','0000-0008',1,'0000','2013-08-09 08:44:10',0,0,'0','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (68,1,1,1,16,NULL,NULL,'bbs','论坛','0000-0009',1,'0000','2013-08-14 06:41:58',0,0,'0','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (69,1,1,1,13,NULL,21,'jobs','招聘','0000-0007',1,'0000','2014-03-18 14:13:23',5,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (74,1,1,1,16,NULL,NULL,NULL,'专题','0000-0006',1,'0000','2014-03-26 11:56:45',0,0,'0','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (77,1,49,1,6,NULL,7,NULL,'媒体软件','0000-0004-0000',2,'0000','2014-04-01 16:58:31',2,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (78,1,49,1,6,NULL,7,NULL,'网络软件','0000-0004-0001',2,'0000','2014-04-01 16:59:19',3,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (79,1,49,1,6,NULL,7,NULL,'系统工具','0000-0004-0002',2,'0000','2014-04-01 16:59:24',4,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (80,1,51,1,8,NULL,9,NULL,'电影','0000-0002-0000',2,'0000','2014-04-02 16:54:36',4,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (81,1,51,1,8,NULL,9,NULL,'电视剧','0000-0002-0001',2,'0000','2014-04-02 16:54:42',4,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (82,1,51,1,8,NULL,23,NULL,'综艺','0000-0002-0002',2,'0000','2014-04-02 16:56:04',6,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (83,1,52,1,10,NULL,24,NULL,'手机/数码','0000-0003-0000',2,'0000','2014-04-02 16:59:26',3,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (84,1,52,1,10,NULL,25,NULL,'电脑办公','0000-0003-0001',2,'0000','2014-04-02 17:02:14',2,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (85,1,52,1,10,NULL,11,NULL,'服饰/内衣','0000-0003-0002',2,'0000','2014-04-02 17:02:26',1,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (87,1,48,1,4,NULL,5,NULL,'自然景观','0000-0001-0002',2,'0000','2014-06-01 18:54:54',2,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (88,1,48,1,4,NULL,5,NULL,'人物图库','0000-0001-0000',2,'0000','2014-06-01 18:55:10',4,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (90,1,48,1,4,NULL,5,NULL,'旅游摄影','0000-0001-0001',2,'0000','2014-06-01 18:58:27',2,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (92,1,1,1,19,NULL,20,'doc','文库','0000-0005',1,'0002','2014-06-01 19:01:26',0,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (93,1,92,1,19,NULL,20,NULL,'教育文库','0000-0005-0000',2,'0000','2014-06-01 19:02:34',1,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (94,1,92,1,19,NULL,20,NULL,'专业资料','0000-0005-0001',2,'0000','2014-06-01 19:02:48',1,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (95,1,52,1,10,NULL,26,NULL,'图书/音像','0000-0003-0003',2,'0000','2014-12-17 12:01:36',1,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node (f_node_id, f_site_id, f_parent_id, f_creator_id, f_node_model_id, f_workflow_id, f_info_model_id, f_number, f_name, f_tree_number, f_tree_level, f_tree_max, f_creation_date, f_refers, f_views, f_is_real_node, f_is_hidden, f_p1, f_p2, f_p3, f_p4, f_p5, f_p6, f_html_status, f_p0) VALUES (96,1,52,1,10,NULL,27,NULL,'家用电器','0000-0003-0004',2,'0000','2014-12-17 12:02:24',1,0,'1','0',NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (1,7);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (38,5);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (42,1);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (44,2);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (48,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (49,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (51,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (52,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (67,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (68,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (69,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (74,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (77,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (78,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (79,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (80,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (81,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (82,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (83,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (84,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (85,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (87,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (88,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (90,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (92,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (93,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (94,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (95,0);
INSERT INTO cms_node_buffer (f_node_id, f_views) VALUES (96,0);
INSERT INTO cms_node_clob (f_node_id, f_key, f_value) VALUES (49,'text','');
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'/index','/{node_number}/{info_id}','1',NULL,1,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (36,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (38,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (40,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (42,NULL,NULL,NULL,NULL,NULL,NULL,'0','0','.html','.html','/news/{node_number}/index','/news/{node_number}/{info_id}','1',4,1,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (44,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (48,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (49,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (51,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (52,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (67,'/guestbook',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (68,'http://bbs.jspxcms.com/',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (69,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (74,'/special_category',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (77,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (78,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (79,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (80,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (81,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (82,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (83,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (84,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (85,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (87,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (88,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (90,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (92,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (93,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (94,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (95,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_detail (f_node_id, f_link, f_meta_keywords, f_meta_description, f_is_new_window, f_node_template, f_info_template, f_is_generate_node, f_is_generate_info, f_node_extension, f_info_extension, f_node_path, f_info_path, f_is_def_page, f_static_method, f_static_page, f_small_image, f_large_image, f_html, f_mobile_html) VALUES (96,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (1,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (36,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (42,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (44,1,'1','1','0');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (48,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (51,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (38,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (40,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (52,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (49,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (68,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (67,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (1,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (36,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (42,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (44,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (48,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (51,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (38,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (40,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (52,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (49,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (68,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (67,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (69,0,'1','0','0');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (69,1,'1','0','0');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (74,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (74,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (77,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (77,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (78,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (78,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (79,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (79,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (80,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (80,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (81,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (81,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (82,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (82,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (83,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (83,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (84,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (84,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (85,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (85,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (87,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (87,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (88,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (88,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (90,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (90,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (92,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (92,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (93,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (93,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (94,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (94,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (95,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (95,1,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (96,0,'1','1','1');
INSERT INTO cms_node_membergroup (f_node_id, f_membergroup_id, f_is_view_perm, f_is_contri_perm, f_is_comment_perm) VALUES (96,1,'1','1','1');
INSERT INTO cms_node_org (f_org_id, f_node_id, f_is_view_perm) VALUES (1,69,'0');
INSERT INTO cms_node_org (f_org_id, f_node_id, f_is_view_perm) VALUES (1,67,'0');
INSERT INTO cms_node_org (f_org_id, f_node_id, f_is_view_perm) VALUES (1,68,'0');
INSERT INTO cms_node_org (f_org_id, f_node_id, f_is_view_perm) VALUES (1,48,'0');
INSERT INTO cms_node_org (f_org_id, f_node_id, f_is_view_perm) VALUES (1,74,'0');
INSERT INTO cms_node_org (f_org_id, f_node_id, f_is_view_perm) VALUES (1,77,'0');
INSERT INTO cms_node_org (f_org_id, f_node_id, f_is_view_perm) VALUES (1,78,'0');
INSERT INTO cms_node_org (f_org_id, f_node_id, f_is_view_perm) VALUES (1,79,'0');
INSERT INTO cms_node_org (f_org_id, f_node_id, f_is_view_perm) VALUES (1,80,'0');
INSERT INTO cms_node_org (f_org_id, f_node_id, f_is_view_perm) VALUES (1,81,'0');
INSERT INTO cms_node_org (f_org_id, f_node_id, f_is_view_perm) VALUES (1,82,'0');
INSERT INTO cms_node_org (f_org_id, f_node_id, f_is_view_perm) VALUES (1,83,'0');
INSERT INTO cms_node_org (f_org_id, f_node_id, f_is_view_perm) VALUES (1,84,'0');
INSERT INTO cms_node_org (f_org_id, f_node_id, f_is_view_perm) VALUES (1,85,'0');
INSERT INTO cms_node_org (f_org_id, f_node_id, f_is_view_perm) VALUES (1,1,'0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (1,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (36,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (42,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (44,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (48,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (51,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (38,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (40,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (52,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (49,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (68,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (67,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (69,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (74,1,'1','1');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (77,1,'1','1');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (78,1,'1','1');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (79,1,'1','1');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (80,1,'1','1');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (81,1,'1','1');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (82,1,'1','1');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (83,1,'1','1');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (84,1,'1','1');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (85,1,'1','1');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (87,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (88,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (90,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (92,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (93,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (94,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (95,1,'0','0');
INSERT INTO cms_node_role (f_node_id, f_role_id, f_is_node_perm, f_is_info_perm) VALUES (96,1,'0','0');
INSERT INTO cms_org (f_org_id, f_parent_id, f_name, f_full_name, f_description, f_number, f_phone, f_fax, f_address, f_tree_number, f_tree_level, f_tree_max, f_contacts) VALUES (1,NULL,'集团总部',NULL,NULL,'000',NULL,NULL,NULL,'0000',0,'0002',NULL);
INSERT INTO cms_publish_point (f_publishpoint_id, f_global_id, f_name, f_description, f_store_path, f_display_path, f_ftp_hostname, f_ftp_port, f_ftp_username, f_ftp_password, f_seq, f_method, f_type) VALUES (1,1,'附件默认发布点',NULL,'/uploads','/uploads',NULL,NULL,NULL,NULL,2147483647,1,2);
INSERT INTO cms_publish_point (f_publishpoint_id, f_global_id, f_name, f_description, f_store_path, f_display_path, f_ftp_hostname, f_ftp_port, f_ftp_username, f_ftp_password, f_seq, f_method, f_type) VALUES (2,1,'HTML默认发布点',NULL,NULL,NULL,NULL,NULL,NULL,NULL,2147483647,1,1);
INSERT INTO cms_question (f_question_id, f_site_id, f_title, f_description, f_begin_date, f_end_date, f_creation_date, f_mode, f_total, f_status, f_interval) VALUES (1,1,'广州恒大再夺亚冠，你怎么看','在刚刚结束的2015赛季亚冠决赛次回合比赛中，广州恒大凭借埃尔克森的进球， 1-0战胜迪拜阿赫利，总比分1-0获得本赛季亚冠联赛冠军，同时这也是恒大3年内两夺亚冠。',NULL,NULL,'2015-12-05 22:32:54',1,4,0,0);
INSERT INTO cms_question (f_question_id, f_site_id, f_title, f_description, f_begin_date, f_end_date, f_creation_date, f_mode, f_total, f_status, f_interval) VALUES (2,1,'南方供暖已成各界共识，你对江西供暖怎么看？','近年来，每逢供暖期，呼吁“南方集中供暖”的话题都会成为热点。清华大学建筑节能研究中心的调研报告显示，并非所有南方城市有供暖需求，真正需要的是国家划定的“夏热冬冷”地区，包括上海、重庆、湖北、湖南、安徽、江西、江苏、浙江、四川等。这些省份冬季室温远低于北方城市集中供热时的室内温度。作为一个江西人，你对江西供暖怎么看？',NULL,NULL,'2015-12-01 16:38:50',1,0,0,0);
INSERT INTO cms_question_item (f_questionitem_id, f_question_id, f_title, f_max_selected, f_seq, f_is_essay) VALUES (1,1,'广州恒大再夺亚冠主要原因是什么?',0,0,'0');
INSERT INTO cms_question_item (f_questionitem_id, f_question_id, f_title, f_max_selected, f_seq, f_is_essay) VALUES (2,1,'恒大本场决赛最佳球员是谁?',1,1,'0');
INSERT INTO cms_question_item (f_questionitem_id, f_question_id, f_title, f_max_selected, f_seq, f_is_essay) VALUES (4,2,'你支持江西供暖吗？',1,0,'0');
INSERT INTO cms_question_item (f_questionitem_id, f_question_id, f_title, f_max_selected, f_seq, f_is_essay) VALUES (5,2,'你觉得“集中供暖”和“分散供暖”哪个模式比较好？',1,1,'0');
INSERT INTO cms_question_item (f_questionitem_id, f_question_id, f_title, f_max_selected, f_seq, f_is_essay) VALUES (6,2,'如果采取供暖，你比较担心以下哪些问题？',1,2,'0');
INSERT INTO cms_question_item (f_questionitem_id, f_question_id, f_title, f_max_selected, f_seq, f_is_essay) VALUES (7,2,'您有什么建议？',1,3,'1');
INSERT INTO cms_question_opt_rec (f_questionrecord_id, f_questionoption_id) VALUES (3,1);
INSERT INTO cms_question_opt_rec (f_questionrecord_id, f_questionoption_id) VALUES (3,2);
INSERT INTO cms_question_opt_rec (f_questionrecord_id, f_questionoption_id) VALUES (3,7);
INSERT INTO cms_question_opt_rec (f_questionrecord_id, f_questionoption_id) VALUES (4,5);
INSERT INTO cms_question_opt_rec (f_questionrecord_id, f_questionoption_id) VALUES (4,7);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (1,1,'斯科拉里中途上任，对球队调教有方','0',1,0);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (2,1,'高拉特、保利尼奥加盟，成功补强球队','0',1,1);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (3,2,'埃尔克森','0',1,0);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (4,2,'郑龙','0',0,1);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (5,1,'郑智、黄博文、冯潇霆等国内球员给力','0',2,2);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (6,1,'其他','0',0,3);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (7,2,'金英权','0',2,2);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (8,2,'其他','0',1,3);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (9,4,'支持','0',0,0);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (10,4,'不支持','0',0,1);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (11,4,'无所谓','0',0,2);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (12,5,'集中供暖比较好','0',0,0);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (13,5,'分散供暖比较好','0',0,1);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (14,5,'无所谓','0',0,2);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (15,6,'花销','0',0,0);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (16,6,'环境污染','0',0,1);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (17,6,'冷热不均','0',0,2);
INSERT INTO cms_question_option (f_questionoption_id, f_questionitem_id, f_title, f_is_input, f_count, f_seq) VALUES (18,6,'生活习惯改变引起身体不适','0',0,3);
INSERT INTO cms_question_record (f_questionrecord_id, f_user_id, f_question_id, f_date, f_ip, f_cookie) VALUES (3,1,1,'2015-12-08 16:12:28','127.0.0.1','2dde68cd95f54781b2fde279de89cb2d');
INSERT INTO cms_question_record (f_questionrecord_id, f_user_id, f_question_id, f_date, f_ip, f_cookie) VALUES (4,NULL,1,'2016-03-22 17:39:56','127.0.0.1','c76e26ec3ed749e0b9bf4782ae50e4f5');
INSERT INTO cms_role (f_role_id, f_site_id, f_name, f_description, f_seq, f_perms, f_is_all_perm, f_is_all_info_perm, f_is_all_node_perm, f_is_info_final_perm, f_info_perm_type, f_rank) VALUES (1,1,'管理员',NULL,2147483647,NULL,'1','1','1','1',1,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (1,1,'InfoScore',97,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (2,1,'InfoScore',28,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (3,13,'InfoScore',94,4);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (4,11,'InfoScore',95,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (5,13,'InfoScore',93,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (6,2,'InfoScore',55,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (7,12,'InfoScore',93,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (8,10,'InfoScore',98,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (9,13,'InfoScore',97,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (10,3,'InfoScore',87,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (11,4,'InfoScore',87,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (12,5,'InfoScore',28,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (13,8,'InfoScore',126,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (14,2,'InfoScore',32,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (15,10,'InfoScore',94,3);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (16,9,'InfoScore',94,16);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (17,12,'InfoScore',94,2);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (18,11,'InfoScore',94,2);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (19,12,'InfoScore',130,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (20,13,'InfoScore',130,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (21,10,'InfoScore',97,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (22,6,'InfoScore',126,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (23,6,'InfoScore',127,3);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (24,5,'InfoScore',127,3);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (25,4,'InfoScore',127,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (26,7,'InfoScore',127,1);
INSERT INTO cms_score_board (f_scoreboard_id, f_scoreitem_id, f_ftype, f_fid, f_votes) VALUES (27,11,'InfoScore',132,1);
INSERT INTO cms_score_group (f_scoregroup_id, f_site_id, f_name, f_number, f_description, f_seq) VALUES (1,1,'心情评分','mood',NULL,0);
INSERT INTO cms_score_group (f_scoregroup_id, f_site_id, f_name, f_number, f_description, f_seq) VALUES (2,1,'星级评分','star',NULL,1);
INSERT INTO cms_score_item (f_scoreitem_id, f_scoregroup_id, f_site_id, f_name, f_score, f_icon, f_seq) VALUES (1,1,1,'感动',1,'/mood/0.gif',0);
INSERT INTO cms_score_item (f_scoreitem_id, f_scoregroup_id, f_site_id, f_name, f_score, f_icon, f_seq) VALUES (2,1,1,'路过',1,'/mood/1.gif',1);
INSERT INTO cms_score_item (f_scoreitem_id, f_scoregroup_id, f_site_id, f_name, f_score, f_icon, f_seq) VALUES (3,1,1,'高兴',1,'/mood/2.gif',2);
INSERT INTO cms_score_item (f_scoreitem_id, f_scoregroup_id, f_site_id, f_name, f_score, f_icon, f_seq) VALUES (4,1,1,'难过',1,'/mood/3.gif',3);
INSERT INTO cms_score_item (f_scoreitem_id, f_scoregroup_id, f_site_id, f_name, f_score, f_icon, f_seq) VALUES (5,1,1,'搞笑',1,'/mood/4.gif',4);
INSERT INTO cms_score_item (f_scoreitem_id, f_scoregroup_id, f_site_id, f_name, f_score, f_icon, f_seq) VALUES (6,1,1,'无聊',1,'/mood/5.gif',5);
INSERT INTO cms_score_item (f_scoreitem_id, f_scoregroup_id, f_site_id, f_name, f_score, f_icon, f_seq) VALUES (7,1,1,'愤怒',1,'/mood/6.gif',6);
INSERT INTO cms_score_item (f_scoreitem_id, f_scoregroup_id, f_site_id, f_name, f_score, f_icon, f_seq) VALUES (8,1,1,'同情',1,'/mood/7.gif',7);
INSERT INTO cms_score_item (f_scoreitem_id, f_scoregroup_id, f_site_id, f_name, f_score, f_icon, f_seq) VALUES (9,2,1,'一星',1,NULL,0);
INSERT INTO cms_score_item (f_scoreitem_id, f_scoregroup_id, f_site_id, f_name, f_score, f_icon, f_seq) VALUES (10,2,1,'二星',2,NULL,1);
INSERT INTO cms_score_item (f_scoreitem_id, f_scoregroup_id, f_site_id, f_name, f_score, f_icon, f_seq) VALUES (11,2,1,'三星',3,NULL,2);
INSERT INTO cms_score_item (f_scoreitem_id, f_scoregroup_id, f_site_id, f_name, f_score, f_icon, f_seq) VALUES (12,2,1,'四星',4,NULL,3);
INSERT INTO cms_score_item (f_scoreitem_id, f_scoregroup_id, f_site_id, f_name, f_score, f_icon, f_seq) VALUES (13,2,1,'五星',5,NULL,4);
INSERT INTO cms_site (f_site_id, f_global_id, f_org_id, f_parent_id, f_name, f_number, f_full_name, f_no_picture, f_template_theme, f_domain, f_is_identify_domain, f_status, f_tree_number, f_tree_level, f_tree_max, f_is_static_home, f_html_publishpoint_id, f_mobile_theme, f_mobile_domain, f_mobile_publishpoint_id) VALUES (1,1,1,NULL,'Jspxcms演示主站','1','Jspxcms演示主站','/img/nopic.jpg','default','localhost','0',0,'0000',0,'0000','0',2,'m','localhost',2);
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'company','南昌蓝智科技有限公司');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'icp','赣ICP备12001124号');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_watermark_mode','1');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_watermark_alpha','50');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_watermark_padding_x','20');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_watermark_image','/img/watermark.png');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_watermark_min_height','300');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_watermark_padding_y','20');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_watermark_position','9');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_watermark_min_width','300');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_comment_captchaMode','3');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_comment_auditMode','0');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_comment_maxLength','2147483647');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_comment_mode','1');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_guestbook_mode','1');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_guestbook_auditMode','0');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_guestbook_captchaMode','3');
INSERT INTO cms_site_custom (f_site_id, f_key, f_value) VALUES (1,'sys_guestbook_maxLength','2147483647');
INSERT INTO cms_special (f_special_id, f_creator_id, f_site_id, f_speccate_id, f_creation_date, f_title, f_meta_keywords, f_meta_description, f_small_image, f_large_image, f_video, f_refers, f_views, f_is_with_image, f_is_recommend, f_special_template, f_video_name, f_model_id, f_video_length, f_video_time) VALUES (2,1,1,5,'2013-02-27 17:10:53','文章姚笛出轨被曝',NULL,NULL,NULL,NULL,NULL,8,0,'0','1',NULL,NULL,17,NULL,NULL);
INSERT INTO cms_special (f_special_id, f_creator_id, f_site_id, f_speccate_id, f_creation_date, f_title, f_meta_keywords, f_meta_description, f_small_image, f_large_image, f_video, f_refers, f_views, f_is_with_image, f_is_recommend, f_special_template, f_video_name, f_model_id, f_video_length, f_video_time) VALUES (8,1,1,2,'2014-03-31 10:46:17','2014索契冬奥会','冬奥会, 2014冬奥会,索契冬奥会,2014索契冬奥会, 第22届冬奥会, 冬季奥林匹克运动会','第22届冬季奥林匹克运动会于2014年2月7日～23日召开','http://demo.jspxcms.com/uploads/1/image/public/201403/20140331105551_obxbxw.jpg',NULL,NULL,5,1,'1','1',NULL,NULL,17,NULL,NULL);
INSERT INTO cms_special (f_special_id, f_creator_id, f_site_id, f_speccate_id, f_creation_date, f_title, f_meta_keywords, f_meta_description, f_small_image, f_large_image, f_video, f_refers, f_views, f_is_with_image, f_is_recommend, f_special_template, f_video_name, f_model_id, f_video_length, f_video_time) VALUES (10,1,1,1,'2013-03-19 02:58:35','美国网络监控“棱镜”项目曝光',NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151224161821_1fftmaayvv.jpg',NULL,NULL,6,0,'1','1',NULL,NULL,17,NULL,NULL);
INSERT INTO cms_special (f_special_id, f_creator_id, f_site_id, f_speccate_id, f_creation_date, f_title, f_meta_keywords, f_meta_description, f_small_image, f_large_image, f_video, f_refers, f_views, f_is_with_image, f_is_recommend, f_special_template, f_video_name, f_model_id, f_video_length, f_video_time) VALUES (11,1,1,6,'2013-03-19 03:00:11','乌克兰局势动荡',NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151224161637_b6hopxqp3l.jpg',NULL,NULL,2,0,'1','1',NULL,NULL,17,NULL,NULL);
INSERT INTO cms_special (f_special_id, f_creator_id, f_site_id, f_speccate_id, f_creation_date, f_title, f_meta_keywords, f_meta_description, f_small_image, f_large_image, f_video, f_refers, f_views, f_is_with_image, f_is_recommend, f_special_template, f_video_name, f_model_id, f_video_length, f_video_time) VALUES (12,1,1,1,'2013-03-19 03:00:39','央视暗拍曝光东莞色情业',NULL,NULL,'http://demo.jspxcms.com/uploads/1/image/public/201512/20151224161539_509ekqycst.jpg',NULL,NULL,3,0,'1','1',NULL,NULL,17,NULL,NULL);
INSERT INTO cms_special (f_special_id, f_creator_id, f_site_id, f_speccate_id, f_creation_date, f_title, f_meta_keywords, f_meta_description, f_small_image, f_large_image, f_video, f_refers, f_views, f_is_with_image, f_is_recommend, f_special_template, f_video_name, f_model_id, f_video_length, f_video_time) VALUES (13,1,1,2,'2014-03-18 00:00:02','马航客机失联','马航客机失联,MH370,马来西亚,飞机,北京,失联','马来西亚航空公司8日凌晨，与一架载有239人的飞机失去联系。这架航班上共载227名乘客，含154名中国人。客机系波音777-200型号，计划于北京时间6：30分抵达北京。','http://demo.jspxcms.com/uploads/1/image/public/201403/20140330221351_nkb5c4.jpg','http://demo.jspxcms.com/uploads/1/image/public/201403/20140330221023_nrob4d.jpg',NULL,2,0,'1','1',NULL,NULL,17,NULL,NULL);
INSERT INTO cms_special_category (f_speccate_id, f_site_id, f_name, f_seq, f_views, f_meta_keywords, f_meta_description, f_creation_date) VALUES (1,1,'国内',0,0,NULL,NULL,'2013-02-28 17:09:49');
INSERT INTO cms_special_category (f_speccate_id, f_site_id, f_name, f_seq, f_views, f_meta_keywords, f_meta_description, f_creation_date) VALUES (2,1,'国际',1,0,NULL,NULL,'2013-03-18 02:22:45');
INSERT INTO cms_special_category (f_speccate_id, f_site_id, f_name, f_seq, f_views, f_meta_keywords, f_meta_description, f_creation_date) VALUES (5,1,'娱乐',2147483647,0,NULL,NULL,'2014-11-27 20:47:03');
INSERT INTO cms_special_category (f_speccate_id, f_site_id, f_name, f_seq, f_views, f_meta_keywords, f_meta_description, f_creation_date) VALUES (6,1,'体育',2147483647,0,NULL,NULL,'2014-11-27 20:47:09');
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (56,1,'中国','2013-03-11 12:00:12',3);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (57,1,'美国','2013-03-11 12:00:12',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (58,1,'中兴','2013-03-19 02:08:21',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (59,1,'蒙古国','2013-03-19 02:08:21',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (60,1,'反贪局','2013-03-19 02:08:21',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (61,1,'逮捕','2013-03-19 02:08:21',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (62,1,'金融危机','2013-03-19 02:08:58',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (63,1,'苏联','2013-03-19 02:09:23',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (64,1,'军火','2013-03-19 02:09:23',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (65,1,'贿赂','2013-03-19 02:09:23',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (66,1,'罗斯福','2013-03-19 02:09:23',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (67,1,'可转债','2013-03-19 02:10:00',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (68,1,'违约','2013-03-19 02:10:00',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (69,1,'首家','2013-03-19 02:10:00',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (70,1,'公司债','2013-03-19 02:10:00',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (71,1,'英国','2013-03-19 02:27:44',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (72,1,'常规武器','2013-03-19 02:27:44',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (73,1,'出口国','2013-03-19 02:27:44',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (78,1,'美女','2014-03-31 15:03:17',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (79,1,'宝贝','2014-03-31 15:03:17',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (80,1,'灰熊','2014-03-31 15:03:18',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (81,1,'南印度洋','2014-06-14 10:27:25',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (82,1,'MH370','2014-06-14 10:27:26',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (83,1,'黑匣子','2014-06-14 10:27:26',1);
INSERT INTO cms_tag (f_tag_id, f_site_id, f_name, f_creation_date, f_refers) VALUES (84,1,'视频','2014-12-16 15:25:53',14);
INSERT INTO cms_user (f_user_id, f_org_id, f_membergroup_id, f_username, f_password, f_salt, f_email, f_mobile, f_real_name, f_gender, f_birth_date, f_validation_type, f_validation_key, f_rank, f_type, f_status, f_global_id, f_qq_openid, f_weibo_uid, f_weixin_openid) VALUES (0,1,1,'anonymous',NULL,NULL,NULL,NULL,NULL,'M',NULL,NULL,NULL,1,1,1,1,NULL,NULL,NULL);
INSERT INTO cms_user (f_user_id, f_org_id, f_membergroup_id, f_username, f_password, f_salt, f_email, f_mobile, f_real_name, f_gender, f_birth_date, f_validation_type, f_validation_key, f_rank, f_type, f_status, f_global_id, f_qq_openid, f_weibo_uid, f_weixin_openid) VALUES (1,1,1,'admin',NULL,NULL,NULL,NULL,'管理员',NULL,NULL,NULL,NULL,0,1,0,1,NULL,NULL,NULL);
INSERT INTO cms_user_detail (f_user_id, f_validation_date, f_login_error_date, f_login_error_count, f_prev_login_date, f_prev_login_ip, f_last_login_date, f_last_login_ip, f_creation_date, f_creation_ip, f_logins, f_bio, f_come_from, f_qq, f_msn, f_weixin, f_is_with_avatar) VALUES (0,NULL,NULL,0,NULL,NULL,NULL,NULL,'2013-03-09 22:18:56','127.0.0.1',0,NULL,NULL,NULL,NULL,NULL,'0');
INSERT INTO cms_user_detail (f_user_id, f_validation_date, f_login_error_date, f_login_error_count, f_prev_login_date, f_prev_login_ip, f_last_login_date, f_last_login_ip, f_creation_date, f_creation_ip, f_logins, f_bio, f_come_from, f_qq, f_msn, f_weixin, f_is_with_avatar) VALUES (1,'2015-04-12 10:27:43',NULL,0,'2017-01-13 14:24:47','0:0:0:0:0:0:0:1','2017-01-13 14:27:24','0:0:0:0:0:0:0:1','2013-02-21 20:59:27','127.0.0.1',663,NULL,NULL,NULL,NULL,NULL,'1');
INSERT INTO cms_user_membergroup (f_user_id, f_membergroup_id, f_group_index) VALUES (1,1,0);
INSERT INTO cms_user_membergroup (f_user_id, f_membergroup_id, f_group_index) VALUES (0,1,0);
INSERT INTO cms_user_org (f_user_id, f_org_id, f_org_index) VALUES (1,1,0);
INSERT INTO cms_user_org (f_user_id, f_org_id, f_org_index) VALUES (0,1,0);
INSERT INTO cms_user_role (f_user_id, f_role_id, f_role_index) VALUES (1,1,0);
INSERT INTO cms_vote (f_vote_id, f_site_id, f_title, f_number, f_description, f_begin_date, f_end_date, f_interval, f_max_selected, f_mode, f_total, f_status, f_creation_date) VALUES (1,1,'您从哪里知道本网站的',NULL,'当中超进入足彩，你会投注么？中国足球顶级联赛进入彩票的距离，从来没有像今年那样离彩民如此之近。体育总局也多次发文称，支持中超进入竞彩，可以说，中超进入体彩只剩时间问题。',NULL,NULL,0,1,1,6,0,'2017-07-18 11:23:46');
INSERT INTO cms_vote_option (f_voteoption_id, f_vote_id, f_title, f_count, f_seq) VALUES (1,1,'朋友或同事介绍的',0,0);
INSERT INTO cms_vote_option (f_voteoption_id, f_vote_id, f_title, f_count, f_seq) VALUES (2,1,'在技术网站中看到',2,1);
INSERT INTO cms_vote_option (f_voteoption_id, f_vote_id, f_title, f_count, f_seq) VALUES (4,1,'通过搜索引擎',1,2);
INSERT INTO cms_vote_option (f_voteoption_id, f_vote_id, f_title, f_count, f_seq) VALUES (6,1,'其它途径',3,3);
INSERT INTO cms_workflow_group (f_workflowgroup_id, f_site_id, f_name, f_description, f_seq) VALUES (2,1,'文档审核',NULL,2147483647);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_ad',23);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_ad_slot',22);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_attachment',375);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_attachment_ref',277);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_attribute',33);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_collect',22);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_collect_field',25);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_comment',52);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_friendlink',31);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_friendlinktype',24);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_guestbook',52);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_guestbooktype',29);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_info',230);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_member_group',22);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_model',52);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_model_field',632);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_node',119);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_operation_log',1756);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_org',24);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_publish_point',33);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_question',23);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_question_item',28);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_question_option',39);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_question_record',25);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_role',26);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_rolenode_info',21);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_rolenode_node',21);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_role_site',22);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_schedule_job',25);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_scoreboard',48);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_scoregroup',23);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_scoreitem',34);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_site',24);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_special',34);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_special_category',27);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_tag',105);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_task',46);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_user',40);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_visit_log',4325);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_vote',22);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_vote_mark',116);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_vote_option',27);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_workflow',24);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_workflowprocess_user',21);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_workflow_group',23);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('cms_workflow_step',25);
INSERT INTO hibernate_sequences (sequence_name, next_val) VALUES ('plug_resume',24);
INSERT INTO plug_resume (f_resume_id, f_site_id, f_name, f_post, f_creation_date, f_gender, f_birth_date, f_mobile, f_email, f_expected_salary, f_education_experience, f_work_experience, f_remark) VALUES (3,1,'123','软件UI设计师','2014-12-08 17:38:37','M',NULL,NULL,NULL,NULL,NULL,NULL,NULL);

commit;

alter table cms_ad add constraint fk_cms_ad_adslot foreign key (f_adslot_id)
references cms_ad_slot (f_adslot_id) on delete restrict on update restrict;

alter table cms_ad add constraint fk_cms_ad_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_ad_slot add constraint fk_cms_adslot_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_attachment add constraint fk_cms_attachement_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_attachment add constraint fk_cms_attachment_user foreign key (f_user_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_attachment_ref add constraint fk_cms_attachmentref_attach foreign key (f_attachment_id)
references cms_attachment (f_attachment_id) on delete restrict on update restrict;

alter table cms_attachment_ref add constraint fk_cms_attachmentref_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_attribute add constraint fk_cms_attribute_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_collect add constraint fk_cms_collect_node foreign key (f_node_id)
references cms_node (f_node_id) on delete restrict on update restrict;

alter table cms_collect add constraint fk_cms_collect_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_collect add constraint fk_cms_collect_user foreign key (f_user_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_collect_field add constraint fk_cms_collectfield_collect foreign key (f_collect_id)
references cms_collect (f_collect_id) on delete restrict on update restrict;

alter table cms_collect_field add constraint fk_cms_collectfield_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_collect_log add constraint fk_cms_collectlog_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_comment add constraint fk_cms_comment_auditor foreign key (f_auditor_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_comment add constraint fk_cms_comment_creator foreign key (f_creator_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_comment add constraint fk_cms_comment_parent foreign key (f_parent_id)
references cms_comment (f_comment_id) on delete restrict on update restrict;

alter table cms_comment add constraint fk_cms_comment_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_favorite add constraint fk_reference_148 foreign key (user_id_)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_friendlink add constraint fk_cms_friendlink_fltype foreign key (f_friendlinktype_id)
references cms_friendlink_type (f_friendlinktype_id) on delete restrict on update restrict;

alter table cms_friendlink add constraint fk_cms_friendlink_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_friendlink_type add constraint fk_cms_friendlinktype_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_global_clob add constraint fk_cms_globalclob_global foreign key (f_global_id)
references cms_global (f_global_id) on delete restrict on update restrict;

alter table cms_global_custom add constraint fk_cms_globalcustom_global foreign key (f_global_id)
references cms_global (f_global_id) on delete restrict on update restrict;

alter table cms_guestbook add constraint fk_cms_guestbook_creator foreign key (f_creator_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_guestbook add constraint fk_cms_guestbook_guestbooktype foreign key (f_guestbooktype_id)
references cms_guestbook_type (f_guestbooktype_id) on delete restrict on update restrict;

alter table cms_guestbook add constraint fk_cms_guestbook_replyer foreign key (f_replyer_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_guestbook add constraint fk_cms_guestbook_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_guestbook_type add constraint fk_cms_guestbooktype_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_info add constraint fk_cms_info_node foreign key (f_node_id)
references cms_node (f_node_id) on delete restrict on update restrict;

alter table cms_info add constraint fk_cms_info_org foreign key (f_org_id)
references cms_org (f_org_id) on delete restrict on update restrict;

alter table cms_info add constraint fk_cms_info_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_info add constraint fk_cms_info_site_from foreign key (f_from_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_info add constraint fk_cms_info_user_creator foreign key (f_creator_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_info_attribute add constraint fk_cms_infoattr_attribute foreign key (f_attribute_id)
references cms_attribute (f_attribute_id) on delete restrict on update restrict;

alter table cms_info_attribute add constraint fk_cms_infoattr_info foreign key (f_info_id)
references cms_info (f_info_id) on delete restrict on update restrict;

alter table cms_info_buffer add constraint fk_cms_infobuffer_info foreign key (f_info_id)
references cms_info (f_info_id) on delete restrict on update restrict;

alter table cms_info_clob add constraint fk_cms_infoclob_info foreign key (f_info_id)
references cms_info (f_info_id) on delete restrict on update restrict;

alter table cms_info_custom add constraint fk_cms_infocustom_info foreign key (f_info_id)
references cms_info (f_info_id) on delete restrict on update restrict;

alter table cms_info_detail add constraint fk_cms_infodetail_info foreign key (f_info_id)
references cms_info (f_info_id) on delete restrict on update restrict;

alter table cms_info_file add constraint fk_cms_infofile_info foreign key (f_info_id)
references cms_info (f_info_id) on delete restrict on update restrict;

alter table cms_info_image add constraint fk_cms_infoimage_info foreign key (f_info_id)
references cms_info (f_info_id) on delete restrict on update restrict;

alter table cms_info_membergroup add constraint fk_cms_infomgroup_info foreign key (f_info_id)
references cms_info (f_info_id) on delete restrict on update restrict;

alter table cms_info_membergroup add constraint fk_cms_infomgroup_mgroup foreign key (f_membergroup_id)
references cms_member_group (f_membergroup_id) on delete restrict on update restrict;

alter table cms_info_node add constraint fk_cms_infonode_info foreign key (f_info_id)
references cms_info (f_info_id) on delete restrict on update restrict;

alter table cms_info_node add constraint fk_cms_infonode_node foreign key (f_node_id)
references cms_node (f_node_id) on delete restrict on update restrict;

alter table cms_info_org add constraint fk_cms_infoorg_info foreign key (f_info_id)
references cms_info (f_info_id) on delete restrict on update restrict;

alter table cms_info_org add constraint fk_cms_infoorg_org foreign key (f_org_id)
references cms_org (f_org_id) on delete restrict on update restrict;

alter table cms_info_push add constraint fk_cms_infopush_info foreign key (info_id_)
references cms_info (f_info_id) on delete restrict on update restrict;

alter table cms_info_push add constraint fk_cms_infopush_site_from foreign key (from_site_id_)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_info_push add constraint fk_cms_infopush_site_to foreign key (to_site_id_)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_info_push add constraint fk_cms_infopush_user foreign key (user_id_)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_info_special add constraint fk_cms_infospecial_info foreign key (f_info_id)
references cms_info (f_info_id) on delete restrict on update restrict;

alter table cms_info_special add constraint fk_cms_infospecial_special foreign key (f_special_id)
references cms_special (f_special_id) on delete restrict on update restrict;

alter table cms_info_tag add constraint fk_cms_infotag_info foreign key (f_info_id)
references cms_info (f_info_id) on delete restrict on update restrict;

alter table cms_info_tag add constraint fk_cms_infotag_tag foreign key (f_tag_id)
references cms_tag (f_tag_id) on delete restrict on update restrict;

alter table cms_mail_inbox add constraint fk_cms_mailinbox_outbox foreign key (mailoutbox_id_)
references cms_mail_outbox (mailoutbox_id_) on delete restrict on update restrict;

alter table cms_mail_inbox add constraint fk_cms_mailinbox_receiver foreign key (receiver_id_)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_mail_inbox add constraint fk_cms_mailinbox_sender foreign key (sender_id_)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_mail_inbox add constraint fk_cms_mailinbox_text foreign key (mailtext_id_)
references cms_mail_text (mailtext_id_) on delete restrict on update restrict;

alter table cms_mail_outbox add constraint fk_cms_mailoutbox_sender foreign key (sender_id_)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_mail_outbox add constraint fk_cms_mailoutbox_text foreign key (mailtext_id_)
references cms_mail_text (mailtext_id_) on delete restrict on update restrict;

alter table cms_message add constraint fk_cms_message_receiver foreign key (receiver_id_)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_message add constraint fk_cms_message_sender foreign key (sender_id_)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_message_text add constraint fk_cms_messagetext_message foreign key (message_id_)
references cms_message (message_id_) on delete restrict on update restrict;

alter table cms_model add constraint fk_cms_model_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_model_custom add constraint fk_cms_modelcustom_model foreign key (f_model_id)
references cms_model (f_model_id) on delete restrict on update restrict;

alter table cms_model_field add constraint fk_cms_modefiel_model foreign key (f_model_id)
references cms_model (f_model_id) on delete restrict on update restrict;

alter table cms_model_field_custom add constraint fk_cms_modfiecus_modefiel foreign key (f_modefiel_id)
references cms_model_field (f_modefiel_id) on delete restrict on update restrict;

alter table cms_node add constraint fk_cms_node_model_info foreign key (f_info_model_id)
references cms_model (f_model_id) on delete restrict on update restrict;

alter table cms_node add constraint fk_cms_node_model_node foreign key (f_node_model_id)
references cms_model (f_model_id) on delete restrict on update restrict;

alter table cms_node add constraint fk_cms_node_parent foreign key (f_parent_id)
references cms_node (f_node_id) on delete restrict on update restrict;

alter table cms_node add constraint fk_cms_node_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_node add constraint fk_cms_node_user_creator foreign key (f_creator_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_node add constraint fk_cms_node_workflow foreign key (f_workflow_id)
references cms_workflow (f_workflow_id) on delete restrict on update restrict;

alter table cms_node_buffer add constraint fk_cms_nodebuffer_node foreign key (f_node_id)
references cms_node (f_node_id) on delete restrict on update restrict;

alter table cms_node_clob add constraint fk_cms_nodeclob_node foreign key (f_node_id)
references cms_node (f_node_id) on delete restrict on update restrict;

alter table cms_node_custom add constraint fk_cms_nodecustom_node foreign key (f_node_id)
references cms_node (f_node_id) on delete restrict on update restrict;

alter table cms_node_detail add constraint fk_cms_nodedetail_node foreign key (f_node_id)
references cms_node (f_node_id) on delete restrict on update restrict;

alter table cms_node_membergroup add constraint fk_cms_nodemgroup_group foreign key (f_membergroup_id)
references cms_member_group (f_membergroup_id) on delete restrict on update restrict;

alter table cms_node_membergroup add constraint fk_cms_nodemgroup_node foreign key (f_node_id)
references cms_node (f_node_id) on delete restrict on update restrict;

alter table cms_node_org add constraint fk_cms_nodeorg_node foreign key (f_node_id)
references cms_node (f_node_id) on delete restrict on update restrict;

alter table cms_node_org add constraint fk_cms_nodeorg_org foreign key (f_org_id)
references cms_org (f_org_id) on delete restrict on update restrict;

alter table cms_node_role add constraint fk_cms_noderole_node foreign key (f_node_id)
references cms_node (f_node_id) on delete restrict on update restrict;

alter table cms_node_role add constraint fk_cms_noderole_role foreign key (f_role_id)
references cms_role (f_role_id) on delete restrict on update restrict;

alter table cms_notification add constraint fk_cms_notification_receiver foreign key (receiver_id_)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_notification_source add constraint fk_cms_notificationkey_notifi foreign key (notification_id_)
references cms_notification (notification_id_) on delete restrict on update restrict;

alter table cms_operation_log add constraint fk_cms_operationlog_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_operation_log add constraint fk_cms_operationlog_user foreign key (f_user_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_org add constraint fk_cms_org_parent foreign key (f_parent_id)
references cms_org (f_org_id) on delete restrict on update restrict;

alter table cms_publish_point add constraint fk_cms_publishpoint_global foreign key (f_global_id)
references cms_global (f_global_id) on delete restrict on update restrict;

alter table cms_question add constraint fk_cms_question_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_question_item add constraint fk_cms_questionitem_question foreign key (f_question_id)
references cms_question (f_question_id) on delete restrict on update restrict;

alter table cms_question_item_rec add constraint fk_cms_questionitemrec_item foreign key (f_questionitem_id)
references cms_question_item (f_questionitem_id) on delete restrict on update restrict;

alter table cms_question_item_rec add constraint fk_cms_questionitemrec_rec foreign key (f_questionrecord_id)
references cms_question_record (f_questionrecord_id) on delete restrict on update restrict;

alter table cms_question_opt_rec add constraint fk_cms_questionoptrec_opt foreign key (f_questionoption_id)
references cms_question_option (f_questionoption_id) on delete restrict on update restrict;

alter table cms_question_opt_rec add constraint fk_cms_questionoptrec_rec foreign key (f_questionrecord_id)
references cms_question_record (f_questionrecord_id) on delete restrict on update restrict;

alter table cms_question_option add constraint fk_cms_questionoption_item foreign key (f_questionitem_id)
references cms_question_item (f_questionitem_id) on delete restrict on update restrict;

alter table cms_question_record add constraint fk_cms_questionrecord_question foreign key (f_question_id)
references cms_question (f_question_id) on delete restrict on update restrict;

alter table cms_question_record add constraint fk_cms_questionrecord_user foreign key (f_user_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_role add constraint fk_cms_role_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_schedule_job add constraint fk_cms_schedulejob_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_schedule_job add constraint fk_cms_schedulejob_user foreign key (f_user_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_score_board add constraint fk_cms_scoreboard_scoreitem foreign key (f_scoreitem_id)
references cms_score_item (f_scoreitem_id) on delete restrict on update restrict;

alter table cms_score_group add constraint fk_cms_scoregroup_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_score_item add constraint fk_cms_scoreitem_scoregroup foreign key (f_scoregroup_id)
references cms_score_group (f_scoregroup_id) on delete restrict on update restrict;

alter table cms_score_item add constraint fk_cms_scoreitem_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_site add constraint fk_cms_site_global foreign key (f_global_id)
references cms_global (f_global_id) on delete restrict on update restrict;

alter table cms_site add constraint fk_cms_site_org foreign key (f_org_id)
references cms_org (f_org_id) on delete restrict on update restrict;

alter table cms_site add constraint fk_cms_site_parent foreign key (f_parent_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_site add constraint fk_cms_site_publishpoint foreign key (f_html_publishpoint_id)
references cms_publish_point (f_publishpoint_id) on delete restrict on update restrict;

alter table cms_site add constraint fk_cms_site_publishpoint_m foreign key (f_mobile_publishpoint_id)
references cms_publish_point (f_publishpoint_id) on delete restrict on update restrict;

alter table cms_site_clob add constraint fk_cms_siteclob_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_site_custom add constraint fk_cms_sitecustom_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_special add constraint fk_cms_special_creator foreign key (f_creator_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_special add constraint fk_cms_special_model foreign key (f_model_id)
references cms_model (f_model_id) on delete restrict on update restrict;

alter table cms_special add constraint fk_cms_special_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_special add constraint fk_cms_special_speccate foreign key (f_speccate_id)
references cms_special_category (f_speccate_id) on delete restrict on update restrict;

alter table cms_special_category add constraint fk_cms_speccategory_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_special_clob add constraint fk_cms_specialclob_special foreign key (f_special_id)
references cms_special (f_special_id) on delete restrict on update restrict;

alter table cms_special_custom add constraint fk_cms_specialcustom_special foreign key (f_special_id)
references cms_special (f_special_id) on delete restrict on update restrict;

alter table cms_special_file add constraint fk_cms_specialfile_special foreign key (f_special_id)
references cms_special (f_special_id) on delete restrict on update restrict;

alter table cms_special_image add constraint fk_cms_specialimage_special foreign key (f_special_id)
references cms_special (f_special_id) on delete restrict on update restrict;

alter table cms_tag add constraint fk_cms_tag_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_task add constraint fk_cms_task_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_task add constraint fk_cms_task_user foreign key (f_user_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_user add constraint fk_cms_user_global foreign key (f_global_id)
references cms_global (f_global_id) on delete restrict on update restrict;

alter table cms_user add constraint fk_cms_user_membergroup foreign key (f_membergroup_id)
references cms_member_group (f_membergroup_id) on delete restrict on update restrict;

alter table cms_user add constraint fk_cms_user_org foreign key (f_org_id)
references cms_org (f_org_id) on delete restrict on update restrict;

alter table cms_user_clob add constraint fk_cms_userclob_user foreign key (f_user_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_user_custom add constraint fk_cms_usercustom_user foreign key (f_user_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_user_detail add constraint fk_cms_userdetail_user foreign key (f_user_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_user_membergroup add constraint fk_cms_usermgroup_mgroup foreign key (f_membergroup_id)
references cms_member_group (f_membergroup_id) on delete restrict on update restrict;

alter table cms_user_membergroup add constraint fk_cms_usermgroup_user foreign key (f_user_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_user_org add constraint fk_cms_userorg_org foreign key (f_org_id)
references cms_org (f_org_id) on delete restrict on update restrict;

alter table cms_user_org add constraint fk_cms_userorg_user foreign key (f_user_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_user_role add constraint fk_cms_userrole_role foreign key (f_role_id)
references cms_role (f_role_id) on delete restrict on update restrict;

alter table cms_user_role add constraint fk_cms_userrole_user foreign key (f_user_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_visit_log add constraint fk_cms_visitlog_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_visit_log add constraint fk_cms_visitlog_user foreign key (f_user_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_vote add constraint fk_cms_vote_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_vote_option add constraint fk_cms_voteoption_vote foreign key (f_vote_id)
references cms_vote (f_vote_id) on delete restrict on update restrict;

alter table cms_workflow add constraint fk_cms_workflow_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_workflow add constraint fk_cms_workflow_workflowgroup foreign key (f_workflowgroup_id)
references cms_workflow_group (f_workflowgroup_id) on delete restrict on update restrict;

alter table cms_workflow_group add constraint fk_cms_workflowgroup_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_workflow_log add constraint fk_cms_workflowlog_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_workflow_log add constraint fk_cms_workflowlog_user foreign key (f_user_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_workflow_log add constraint fk_cms_workflowlog_wfprocess foreign key (f_workflowprocess_id)
references cms_workflow_process (f_workflowprocess_id) on delete restrict on update restrict;

alter table cms_workflow_process add constraint fk_cms_workflowproc_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;

alter table cms_workflow_process add constraint fk_cms_workflowproc_user foreign key (f_user_id)
references cms_user (f_user_id) on delete restrict on update restrict;

alter table cms_workflow_process add constraint fk_cms_workflowproc_wfstep foreign key (f_workflowstep_id)
references cms_workflow_step (f_workflowstep_id) on delete restrict on update restrict;

alter table cms_workflow_process add constraint fk_cms_workflowproc_workflow foreign key (f_workflow_id)
references cms_workflow (f_workflow_id) on delete restrict on update restrict;

alter table cms_workflow_step add constraint fk_cms_workflowstep_workflow foreign key (f_workflow_id)
references cms_workflow (f_workflow_id) on delete restrict on update restrict;

alter table cms_workflowstep_role add constraint fk_cms_wfsteprole_role foreign key (f_role_id)
references cms_role (f_role_id) on delete restrict on update restrict;

alter table cms_workflowstep_role add constraint fk_cms_wfsteprole_wfstep foreign key (f_workflowstep_id)
references cms_workflow_step (f_workflowstep_id) on delete restrict on update restrict;

alter table plug_resume add constraint fk_plug_resume_site foreign key (f_site_id)
references cms_site (f_site_id) on delete restrict on update restrict;
