insert into player 
  select * from csvread( 'https://raw.githubusercontent.com/andywarren86/quicksplit/master/db/player.csv' );
insert into season 
  select * from csvread( 'https://raw.githubusercontent.com/andywarren86/quicksplit/master/db/season.csv' );
insert into game 
  select * from csvread( 'https://raw.githubusercontent.com/andywarren86/quicksplit/master/db/game.csv' );
insert into result 
  select * from csvread( 'https://raw.githubusercontent.com/andywarren86/quicksplit/master/db/result.csv' );
insert into transaction
  select * from csvread( 'https://raw.githubusercontent.com/andywarren86/quicksplit/master/db/transaction.csv' );