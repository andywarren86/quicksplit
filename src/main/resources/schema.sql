drop table if exists player;
drop table if exists season;
drop table if exists game;
drop table if exists result;
drop table if exists transaction;

create table player
(
  id_player number not null primary key auto_increment,
  nm_player varchar( 255 ) not null
);

create table season
(
  id_season number not null primary key auto_increment,
  dt_start date not null,
  dt_end date not null
);

create table game
(
  id_game number not null primary key auto_increment,
  id_season number not null,
  dt_game date not null,
  
  constraint fk_season foreign key ( id_season ) references season ( id_season )
);

create table result
(
  id_game number not null,
  id_player number not null,
  no_result number not null,
  
  primary key ( id_game, id_player ),
  constraint fk_result_player foreign key ( id_player ) references player ( id_player ),
  constraint fk_result_game foreign key ( id_game ) references game ( id_game ) on delete cascade
);

create table transaction
(
  id_transaction number not null primary key auto_increment,
  id_player number not null,
  id_season number null,
  dt_transaction date not null,
  am_transaction number not null,
  tx_description varchar( 255 ) null,
  
  constraint fk_transaction_player foreign key ( id_player ) references player ( id_player ),
  constraint fk_transaxtion_season foreign key ( id_season ) references season ( id_season )
);