
CREATE TABLE IF NOT EXISTS player
(
    id_player NUMBER PRIMARY KEY,
    nm_player VARCHAR( 255 ) NOT NULL
);

CREATE TABLE IF NOT EXISTS season
(
  id_season NUMBER PRIMARY KEY,
  dt_start DATE NOT NULL,
  dt_end DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS game
(
  id_game NUMBER PRIMARY KEY,
  id_season NUMBER NOT NULL,
  dt_game DATE NOT NULL,
  
  constraint fk_season foreign key ( id_season ) references season ( id_season )
  /* unique date? */
);

CREATE TABLE IF NOT EXISTS result
(
  id_result NUMBER PRIMARY KEY,
  id_game NUMBER,
  id_player NUMBER,
  am_result NUMBER,
  
  /* AK on game/player */
  
  CONSTRAINT fk_player FOREIGN KEY ( id_player )
    REFERENCES player ( id_player ) ,
  CONSTRAINT fk_game FOREIGN KEY ( id_game )
    REFERENCES game ( id_game ) 
);

CREATE SEQUENCE IF NOT EXISTS player_seq;
CREATE SEQUENCE IF NOT EXISTS game_seq;