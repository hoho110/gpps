/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2014/9/10 22:32:58                           */
/*==============================================================*/


drop table if exists Borrower;

drop table if exists BorrowerAccount;

drop table if exists Govermentorder;

drop table if exists Lender;

drop table if exists Lender2;

drop table if exists LenderAccount;

drop table if exists PayBackModel;

drop table if exists Product;

drop table if exists ProductAction;

drop table if exists StateLog;

drop table if exists Submit;

drop table if exists Task;

drop table if exists admin;

drop table if exists adminGroup;

drop table if exists cashstream;

drop table if exists groupmember;

drop table if exists handlelog;

drop table if exists payback;

drop table if exists productseries;

/*==============================================================*/
/* Table: Borrower                                              */
/*==============================================================*/
create table Borrower
(
   id                   integer not null auto_increment,
   name                 varchar(50),
   tel                  varchar(50),
   email                varchar(50),
   loginId              varchar(255),
   password             varchar(255) not null,
   identitycard         varchar(50),
   accountid            integer not null,
   createtime           BIGINT not null,
   material             mediumtext comment '记录了企业相关资料的附件路径',
   request              mediumtext,
   privilege            int not null comment '10：有查看权限的企业用户
            11：申请融资权限的企业用户
            12：有融资权限的企业用户',
   creditValue          int not null default 0,
   primary key (id)
);

/*==============================================================*/
/* Table: BorrowerAccount                                       */
/*==============================================================*/
create table BorrowerAccount
(
   id                   integer not null auto_increment,
   total                decimal(12,2) not null default 0,
   freeze               decimal(12,2) not null default 0,
   usable               decimal(12,2) not null default 0,
   primary key (id)
);

/*==============================================================*/
/* Table: Govermentorder                                        */
/*==============================================================*/
create table Govermentorder
(
   ID                   integer not null auto_increment,
   borrowerId           integer not null,
   material             mediumtext comment '记录相关资料附件的路径',
   state                int not null comment '0：申请融资
            -1：驳回重填
            -2：重新申请
            -100：拒绝
            1：审核通过
            2：产品已发布
            3：融资中
            4：还款中
            5：已关闭',
   financingStarttime   bigint not null default 0,
   financingEndtime     bigint not null default 0,
   createtime           bigint not null default 0,
   primary key (ID)
);

/*==============================================================*/
/* Table: Lender                                                */
/*==============================================================*/
create table Lender
(
   id                   integer not null auto_increment,
   name                 varchar(50),
   tel                  varchar(50),
   email                varchar(50),
   loginId              varchar(255),
   password             varchar(255) not null,
   identitycard         varchar(50),
   accountid            integer not null,
   createtime           BIGINT not null,
   privilege            int not null comment '00：普通用户
            01：vip1',
   grade                int not null default 0,
   level                int not null default 0,
   sex                  int not null default 0,
   address              varchar(255),
   thirdPartyAccount    varchar(255),
   primary key (id)
);

/*==============================================================*/
/* Table: Lender2                                               */
/*==============================================================*/
create table Lender2
(
   id                   integer not null auto_increment,
   name                 varchar(50),
   tel                  varchar(50),
   email                varchar(50),
   loginId              varchar(255),
   password             varchar(255) not null,
   identitycard         varchar(50),
   accountid            integer not null,
   createtime           BIGINT not null,
   privilege            int not null comment '00：普通用户
            01：vip1',
   grade                int not null default 0,
   level                int not null default 0,
   sex                  int not null default 0,
   address              varchar(255),
   primary key (id)
);

/*==============================================================*/
/* Table: LenderAccount                                         */
/*==============================================================*/
create table LenderAccount
(
   id                   integer not null auto_increment,
   total                decimal(12,2) not null default 0,
   freeze               decimal(12,2) not null default 0,
   usable               decimal(12,2) not null default 0,
   used                 decimal(12,2) not null default 0,
   totalincome          decimal(12,2) not null default 0,
   expectedincome       decimal(12,2) not null default 0,
   primary key (id)
);

/*==============================================================*/
/* Table: PayBackModel                                          */
/*==============================================================*/
create table PayBackModel
(
   ID                   int not null auto_increment,
   type                 int not null default 2 comment '0： 等额本息
            1：先还利息，到期还本
            2：到期还本付息',
   freqencyType         int not null default 9 comment '0:按日
            1：按周
            2：按半月
            3：按月
            4：按指定日期
            9：未指定',
   primary key (ID)
);

/*==============================================================*/
/* Table: Product                                               */
/*==============================================================*/
create table Product
(
   ID                   integer not null auto_increment,
   state                int not null default 0 comment '0：投标中
            1：还款中
            -1：申请关闭
            -2：延期
            2：已关闭
            3：还款完成
            4：流标',
   GovermentorderId     integer not null,
   expectAmount         decimal(12,2) not null default 0,
   realAmount           decimal(12,2) not null default 0,
   rate                 decimal(3,3) not null default 0,
   paybackmodel         int not null comment '0： 等额本息
            1：先还利息，到期还本
            2：到期还本付息
            
            
            0:按日
            1：按周
            2：按半月
            3：按月
            4：按指定日期
            9：未指定',
   accessory            mediumtext,
   productseriesId      integer not null,
   levelToBuy           int not null,
   createtime           bigint not null default 0,
   primary key (ID)
);

/*==============================================================*/
/* Table: ProductAction                                         */
/*==============================================================*/
create table ProductAction
(
   ID                   integer not null auto_increment,
   createtime           bigint not null default 0 comment '0：投标中
            1：还款中
            -1：申请关闭
            2：已关闭
            3：还款完成
            4：流标',
   title                varchar(255) not null,
   detail               varchar(2000) default '0',
   type                 int not null default 0 comment '-2：严重问题
            -1：不顺利
            0：正常
            1：利好',
   productId            integer not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: StateLog                                              */
/*==============================================================*/
create table StateLog
(
   ID                   integer not null auto_increment,
   type                 int not null comment '0：submit
            1：product
            2：Govermentorder',
   createtime           bigint not null,
   source               int not null,
   target               int not null,
   refid                integer not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: Submit                                                */
/*==============================================================*/
create table Submit
(
   Id                   integer not null auto_increment,
   state                int not null default 0 comment '0:申请竞标->
            1:待支付
            	（支付）
            2:竞标中
            	(融资审核成功)
            3:还款中
            4：还款完毕
            
            6:流标(融资审核不成功)
            7:退订（未支付）
            8：异常（额度不足）申请不成功',
   createtime           bigint not null,
   lenderId             integer not null,
   productId            integer not null,
   lastmodifytime       bigint not null,
   amount               decimal(12,2) not null default 0,
   primary key (Id)
);

/*==============================================================*/
/* Table: Task                                                  */
/*==============================================================*/
create table Task
(
   id                   integer not null auto_increment,
   type                 int not null,
   createTime           bigint not null,
   state                int not null default 0,
   productId            integer not null,
   payBackId            integer,
   primary key (id)
);

/*==============================================================*/
/* Table: admin                                                 */
/*==============================================================*/
create table admin
(
   id                   integer not null,
   name                 varchar(50),
   tel                  varchar(50),
   email                varchar(50),
   loginId              varchar(255),
   password             varchar(255) not null,
   identitycard         varchar(50),
   createtime           BIGINT not null,
   primary key (id)
);

/*==============================================================*/
/* Table: adminGroup                                            */
/*==============================================================*/
create table adminGroup
(
   id                   integer not null,
   primary key (id)
);

/*==============================================================*/
/* Table: cashstream                                            */
/*==============================================================*/
create table cashstream
(
   id                   integer not null auto_increment,
   action               int not null comment '0:向账户充值
            1:冻结(包括借款方竞标冻结/融资方还钱冻结)
            2：解冻
            3:借款方汇出给融资方
            4:融资方:汇入借款方
            5:从账户提现',
   chiefamount          decimal(12,2) not null default 0,
   createtime           bigint not null,
   LenderAccountId      integer,
   BorrowerAccountId    integer,
   submitid             integer,
   interest             decimal(12,2) not null default 0,
   description          varchar(2000),
   paybackId            integer,
   state                int not null default 1,
   primary key (id)
);

/*==============================================================*/
/* Table: groupmember                                           */
/*==============================================================*/
create table groupmember
(
   id                   integer not null,
   groupid              integer not null,
   adminid              integer not null,
   verify               integer not null,
   primary key (id)
);

/*==============================================================*/
/* Table: handlelog                                             */
/*==============================================================*/
create table handlelog
(
   id                   integer not null auto_increment,
   groupid              integer not null,
   adminid              integer not null,
   action               integer not null,
   createtime           bigint not null,
   primary key (id)
);

/*==============================================================*/
/* Table: payback                                               */
/*==============================================================*/
create table payback
(
   ID                   integer not null auto_increment,
   borroweraccountid    integer not null,
   productId            integer not null,
   state                int not null comment '0：待还款
            1：已还款
            2：延期(待确定)',
   type                 int not null default 0 comment '0:利息和本息;1:一次性本金',
   chiefamount          decimal(12,2) not null,
   deadline             bigint not null,
   interest             decimal(12,2) not null,
   realtime             bigint not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: productseries                                         */
/*==============================================================*/
create table productseries
(
   ID                   integer not null auto_increment,
   title                varchar(100) not null,
   message              mediumtext,
   decstription         varchar(2000),
   primary key (ID)
);

alter table Borrower add constraint FK_Ref_borrower_account foreign key (accountid)
      references BorrowerAccount (id) on delete restrict on update restrict;

alter table Govermentorder add constraint FK_Ref_Govermentorder_borrower foreign key (borrowerId)
      references Borrower (id) on delete restrict on update restrict;

alter table Lender add constraint FK_Ref_lender_account foreign key (accountid)
      references LenderAccount (id) on delete restrict on update restrict;

alter table Product add constraint FK_Ref_Product_Govermentorder foreign key (GovermentorderId)
      references Govermentorder (ID) on delete restrict on update restrict;

alter table Product add constraint FK_Ref_product_series foreign key (productseriesId)
      references productseries (ID) on delete restrict on update restrict;

alter table ProductAction add constraint FK_Ref_ProductAction_Product foreign key (productId)
      references Product (ID) on delete restrict on update restrict;

alter table Submit add constraint FK_Ref_submit_lender foreign key (lenderId)
      references Lender (id) on delete restrict on update restrict;

alter table Submit add constraint FK_Ref_submit_product foreign key (productId)
      references Product (ID) on delete restrict on update restrict;

alter table cashstream add constraint FK_Ref_cashstream_borroweraccount foreign key (BorrowerAccountId)
      references BorrowerAccount (id) on delete restrict on update restrict;

alter table cashstream add constraint FK_Ref_cashstream_lenderaccount foreign key (LenderAccountId)
      references LenderAccount (id) on delete restrict on update restrict;

alter table cashstream add constraint FK_Ref_cashstream_payback foreign key (paybackId)
      references payback (ID) on delete restrict on update restrict;

alter table cashstream add constraint FK_Ref_cashstream_submit foreign key (submitid)
      references Submit (Id) on delete restrict on update restrict;

alter table payback add constraint FK_Ref_payback_borroweraccount foreign key (borroweraccountid)
      references BorrowerAccount (id) on delete restrict on update restrict;

alter table payback add constraint FK_Ref_payback_product foreign key (productId)
      references Product (ID) on delete restrict on update restrict;
