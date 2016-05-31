drop table if exists player;
drop table if exists season;
drop table if exists game;
drop table if exists result;
drop table if exists transaction;

CREATE TABLE player
(
  id_player NUMBER NOT NULL PRIMARY KEY auto_increment,
  nm_player VARCHAR( 255 ) NOT NULL
);

CREATE TABLE season
(
  id_season NUMBER NOT NULL PRIMARY KEY auto_increment,
  dt_start DATE NOT NULL,
  dt_end DATE NOT NULL
);

CREATE TABLE game
(
  id_game NUMBER NOT NULL PRIMARY KEY auto_increment,
  id_season NUMBER NOT NULL,
  dt_game DATE NOT NULL,
  
  constraint fk_season foreign key ( id_season ) references season ( id_season )
);

CREATE TABLE result
(
  id_game NUMBER NOT NULL,
  id_player NUMBER NOT NULL,
  no_result NUMBER NOT NULL,
  
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