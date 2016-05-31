/* 
 * Inserts account transactions records for when a season is finalised.
 * For every player that finished the season with a non-zero total insert an appropriate record
 * into the transaction table to record their win/loss.
 */
insert into transaction ( id_player, id_season, dt_transaction, am_transaction, tx_description )
select p.id_player, s.id_season, s.dt_end, sum(r.no_result) as am_sum, concat( 'Season #', s.id_season, ' loss.' )
from season s
inner join game g on g.id_season = s.id_season
inner join result r on r.id_game = g.id_game
inner join player p on p.id_player = r.id_player
where s.id_season <= 14
group by s.id_season, p.id_player
having am_sum < 0;

insert into transaction ( id_player, id_season, dt_transaction, am_transaction, tx_description )
select p.id_player, s.id_season, s.dt_end, sum(r.no_result) as am_sum, concat( 'Season #', s.id_season, ' profit.' )
from season s
inner join game g on g.id_season = s.id_season
inner join result r on r.id_game = g.id_game
inner join player p on p.id_player = r.id_player
where s.id_season <= 14
group by s.id_season, p.id_player
having am_sum > 0;