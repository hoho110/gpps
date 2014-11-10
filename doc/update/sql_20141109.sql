alter table CashStream add(loanNo varchar(255));

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
alter table Lender add(   cardBindingId        integer);
alter table Borrower add(   cardBindingId        integer);
alter table Lender add constraint FK_Ref_Lender_CardBinding foreign key (cardbindingId)
      references CardBinding (id) on delete restrict on update restrict;
alter table Borrower add constraint FK_Ref_Borrower_CardBinding foreign key (cardBindingId)
      references CardBinding (id) on delete restrict on update restrict;