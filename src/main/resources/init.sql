drop table if exists player;
drop table if exists season;
drop table if exists game;
drop table if exists result;

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
  
  PRIMARY KEY ( id_game, id_player ),
  CONSTRAINT fk_player FOREIGN KEY ( id_player )
    REFERENCES player ( id_player ) ,
  CONSTRAINT fk_game FOREIGN KEY ( id_game )
    REFERENCES game ( id_game ) 
);