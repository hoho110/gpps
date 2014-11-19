/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2014/11/19 13:54:46                          */
/*==============================================================*/


drop table if exists Activity;

drop table if exists ActivityRef;

drop table if exists Borrower;

drop table if exists BorrowerAccount;

drop table if exists CardBinding;

drop table if exists FinancingRequest;

drop table if exists Govermentorder;

drop table if exists Help;

drop table if exists Lender;

drop table if exists LenderAccount;

drop table if exists Letter;

drop table if exists News;

drop table if exists Notice;

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
/* Table: Activity                                              */
/*==============================================================*/
create table Activity
(
   id                   integer not null auto_increment,
   name                 varchar(255) not null default '0' comment '0:public
            1:private',
   applystarttime       bigint not null comment '0:��������
            1:��������',
   applyendtime         bigint not null default 0 comment '0:lender
            1:borrower',
   starttime            bigint not null,
   question             varchar(2000),
   answer               mediumtext,
   createtime           bigint not null,
   state                int not null default 0 comment '0:δ����
            1������
            2������
            3������',
   url                  varchar(255),
   primary key (id)
);

/*==============================================================*/
/* Table: ActivityRef                                           */
/*==============================================================*/
create table ActivityRef
(
   id                   integer not null,
   participatortype     int not null default 0 comment '0:lender
            1:borrower',
   participatorId       Integer not null,
   ActivityId           Integer not null,
   applytime            bigint not null,
   applystate           int not null comment '0:�μ�
            1��δ�μ�',
   awarddetail          varchar(255),
   description          varchar(255),
   primary key (id)
);

/*==============================================================*/
/* Table: Borrower                                              */
/*==============================================================*/
create table Borrower
(
   id                   integer not null,
   name                 varchar(50),
   tel                  varchar(50),
   email                varchar(50),
   loginId              varchar(255),
   password             varchar(255) not null,
   identitycard         varchar(50),
   accountid            integer not null,
   createtime           BIGINT not null,
   material             mediumtext comment '��¼����ҵ������ϵĸ���·��',
   request              mediumtext,
   privilege            int not null comment '10���в鿴Ȩ�޵���ҵ�û�
            11����������Ȩ�޵���ҵ�û�
            12��������Ȩ�޵���ҵ�û�',
   creditValue          int not null default 0,
   companyName          varchar(255),
   license              varchar(255),
   corporationPhone     varchar(255),
   corporationName      varchar(255),
   corporationAddr      varchar(255),
   thirdPartyAccount    varchar(255),
   level                int not null default 0,
   lastModifyTime       BIGINT not null default 0,
   range                varchar(255),
   cardBindingId        integer,
   authorizeTypeOpen    int not null default 0,
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
/* Table: CardBinding                                           */
/*==============================================================*/
create table CardBinding
(
   id                   Integer not null auto_increment,
   CardNo               varchar(100) not null,
   CardType             int not null default 0,
   BankCode             varchar(100) not null,
   BranchBankName       varchar(100) not null,
   Province             varchar(100) not null,
   City                 varchar(100) not null,
   primary key (id)
);

/*==============================================================*/
/* Table: FinancingRequest                                      */
/*==============================================================*/
create table FinancingRequest
(
   id                   integer not null auto_increment,
   govermentOrderName   varchar(255),
   govermentOrderDetail varchar(255),
   applyFinancingAmount int not null default 0,
   rate                 varchar(50),
   state                int not null default 0,
   createtime           bigint not null default 0,
   lastModifyTime       bigint not null default 0,
   borrowerID           integer not null,
   primary key (id)
);

/*==============================================================*/
/* Table: Govermentorder                                        */
/*==============================================================*/
create table Govermentorder
(
   ID                   integer not null auto_increment,
   borrowerId           integer not null,
   material             mediumtext comment '��¼������ϸ�����·��',
   state                int not null comment '0����������
            -1����������
            -2����������
            -100���ܾ�
            1�����ͨ��
            2����Ʒ�ѷ���
            3��������
            4��������
            5���ѹر�',
   financingStarttime   bigint not null default 0,
   financingEndtime     bigint not null default 0,
   createtime           bigint not null default 0,
   title                varchar(255),
   incomeStarttime      bigint not null,
   incomeEndtime        bigint not null,
   lastModifytime       bigint not null,
   description          varchar(2000),
   FinancingRequestId   integer,
   primary key (ID)
);

/*==============================================================*/
/* Table: Help                                                  */
/*==============================================================*/
create table Help
(
   id                   integer not null auto_increment,
   type                 int not null default 0 comment '0:public
            1:private',
   publictype           int not null comment '0:��������
            1:��������',
   questionertype       int not null default 0 comment '0:lender
            1:borrower',
   questionerId         Integer,
   question             varchar(2000),
   answer               mediumtext,
   createtime           bigint not null,
   primary key (id)
);

/*==============================================================*/
/* Table: Lender                                                */
/*==============================================================*/
create table Lender
(
   id                   integer not null,
   name                 varchar(50),
   tel                  varchar(50),
   email                varchar(50),
   loginId              varchar(255),
   password             varchar(255) not null,
   identitycard         varchar(50),
   accountid            integer not null,
   createtime           BIGINT not null,
   privilege            int not null comment '00����ͨ�û�
            01��vip1',
   grade                int not null default 0,
   level                int not null default 0,
   sex                  int not null default 0,
   address              varchar(255),
   thirdPartyAccount    varchar(255),
   annualIncome         varchar(255),
   cardbindingId        integer,
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
/* Table: Letter                                                */
/*==============================================================*/
create table Letter
(
   id                   integer not null auto_increment,
   receivertype         int not null default 0 comment '0:lender
            1:borrower',
   receiverId           Integer not null,
   content              mediumtext,
   createtime           bigint not null,
   "read"               int not null default 0 comment '0:δ��
            1���Ѷ�',
   primary key (id)
);

/*==============================================================*/
/* Table: News                                                  */
/*==============================================================*/
create table News
(
   id                   integer not null auto_increment,
   title                varchar(255) not null default '0' comment '0:public
            1:private',
   content              mediumtext,
   publishtime          bigint not null,
   primary key (id)
);

/*==============================================================*/
/* Table: Notice                                                */
/*==============================================================*/
create table Notice
(
   id                   integer not null auto_increment,
   title                varchar(255) not null default '0' comment '0:public
            1:private',
   content              mediumtext,
   publishtime          bigint not null,
   to                   int not null default -1 comment '-1:����
            0��lender
            1��borrower',
   level                int not null default -1 comment '-1�����ޣ�to��Ϊ-1ʱ����',
   primary key (id)
);

/*==============================================================*/
/* Table: PayBackModel                                          */
/*==============================================================*/
create table PayBackModel
(
   ID                   int not null auto_increment,
   type                 int not null default 2 comment '0�� �ȶϢ
            1���Ȼ���Ϣ�����ڻ���
            2�����ڻ�����Ϣ',
   freqencyType         int not null default 9 comment '0:����
            1������
            2��������
            3������
            4����ָ������
            9��δָ��',
   primary key (ID)
);

/*==============================================================*/
/* Table: Product                                               */
/*==============================================================*/
create table Product
(
   ID                   integer not null auto_increment,
   state                int not null default 0 comment '0��Ͷ����
            1��������
            -1������ر�
            -2������
            2���ѹر�
            3���������
            4������',
   GovermentorderId     integer not null,
   expectAmount         decimal(12,2) not null default 0,
   realAmount           decimal(12,2) not null default 0,
   rate                 decimal(3,3) not null default 0,
   paybackmodel         int not null comment '0�� �ȶϢ
            1���Ȼ���Ϣ�����ڻ���
            2�����ڻ�����Ϣ
            
            
            0:����
            1������
            2��������
            3������
            4����ָ������
            9��δָ��',
   accessory            mediumtext,
   productseriesId      integer not null,
   levelToBuy           int not null,
   createtime           bigint not null default 0,
   minimum              int not null default 1,
   miniAdd              int not null default 1,
   description          varchar(2000),
   incomeEndtime        bigint not null default 0,
   lastmodifytime       bigint not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: ProductAction                                         */
/*==============================================================*/
create table ProductAction
(
   ID                   integer not null auto_increment,
   createtime           bigint not null default 0 comment '0��Ͷ����
            1��������
            -1������ر�
            2���ѹر�
            3���������
            4������',
   title                varchar(255) not null,
   detail               varchar(2000) default '0',
   type                 int not null default 0 comment '-2����������
            -1����˳��
            0������
            1������',
   productId            integer not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: StateLog                                              */
/*==============================================================*/
create table StateLog
(
   ID                   integer not null auto_increment,
   type                 int not null comment '0��submit
            1��product
            2��Govermentorder',
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
   state                int not null default 0 comment '0:���뾺��->
            1:��֧��
            	��֧����
            2:������
            	(������˳ɹ�)
            3:������
            4���������
            
            6:����(������˲��ɹ�)
            7:�˶���δ֧����
            8���쳣����Ȳ��㣩���벻�ɹ�',
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
   createtime           BIGINT not null,
   privilege            int not null default 0 comment '0: ֻ��
            1���ɲ���',
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
   action               int not null comment '0:���˻���ֵ
            1:����(���������궳��/���ʷ���Ǯ����)
            2���ⶳ
            3:����������ʷ�
            4:���ʷ�:�����
            5:���˻�����',
   chiefamount          decimal(12,2) not null default 0,
   createtime           bigint not null,
   LenderAccountId      integer,
   BorrowerAccountId    integer,
   submitid             integer,
   interest             decimal(12,2) not null default 0,
   description          varchar(2000),
   paybackId            integer,
   state                int not null default 1,
   loanNo               varchar(255),
   fee                  decimal(12,2) not null default 0,
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
   handlertype          int not null comment '0:lender
            1:borrower
            2:admin',
   handlerId            integer not null,
   callService          varchar(255),
   callmethod           varchar(255),
   callparam            mediumtext,
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
   state                int not null comment '0��������
            1���ѻ���
            2������(��ȷ��)',
   type                 int not null default 0 comment '0:��Ϣ�ͱ�Ϣ;1:һ���Ա���',
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
   tag                  mediumtext,
   desctription         varchar(2000),
   type                 int not null default 0,
   typedetail           varchar(255),
   primary key (ID)
);

alter table ActivityRef add constraint FK_Ref_Activity_ActivityRef foreign key (ActivityId)
      references Activity (id) on delete restrict on update restrict;

alter table Borrower add constraint FK_Ref_Borrower_CardBinding foreign key (cardBindingId)
      references CardBinding (id) on delete restrict on update restrict;

alter table Borrower add constraint FK_Ref_borrower_account foreign key (accountid)
      references BorrowerAccount (id) on delete restrict on update restrict;

alter table FinancingRequest add constraint FK_Reference_FinancingRequest_Borrower foreign key (borrowerID)
      references Borrower (id) on delete restrict on update restrict;

alter table Govermentorder add constraint FK_Ref_Govermentorder_borrower foreign key (borrowerId)
      references Borrower (id) on delete restrict on update restrict;

alter table Govermentorder add constraint FK_Reference_GovermentOrder_FinancingRequest foreign key (FinancingRequestId)
      references FinancingRequest (id) on delete restrict on update restrict;

alter table Lender add constraint FK_Ref_Lender_CardBinding foreign key (cardbindingId)
      references CardBinding (id) on delete restrict on update restrict;

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

