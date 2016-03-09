DROP TABLE IF EXISTS player;
CREATE TABLE player
(
    id_player NUMBER PRIMARY KEY,
    nm_player VARCHAR( 255 ) NOT NULL,
    nm_alias VARCHAR( 255 )
);

DROP TABLE IF EXISTS game;
CREATE TABLE game
(
  id_game NUMBER PRIMARY KEY,
  dt_game DATE
);

DROP TABLE IF EXISTS result;
CREATE TABLE result
(
  id_result NUMBER PRIMARY KEY,
  id_game NUMBER,
  id_player NUMBER,
  am_result NUMBER,
  
  CONSTRAINT fk_player FOREIGN KEY ( id_player )
    REFERENCES player ( id_player ) ,
  CONSTRAINT fk_game FOREIGN KEY ( id_game )
    REFERENCES game ( id_game ) 
);