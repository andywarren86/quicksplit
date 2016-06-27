/* 
 * Inserts account transactions records for when a season is finalised.
 * For every player that finished the season with a non-zero total insert an appropriate record
 * into the transaction table to record their win/loss.
 */
insert into transaction ( id_player, id_season, dt_transaction, am_transaction, tx_description )
select p.id_player, s.id_season, s.dt_end, sum(r.no_result) as am_sum, concat( 'Season #', s.id_season )
from season s
inner join game g on g.id_season = s.id_season
inner join result r on r.id_game = g.id_game
inner join player p on p.id_player = r.id_player
where s.id_season <= 14
group by s.id_season, p.id_player
having am_sum != 0;

/*
 * Inserts a depsoit/withdrawal record one day after each season result transaction.
 * Only to be used for initial data insert, then can be deleted.
 */
insert into transaction ( id_player, id_season, dt_transaction, am_transaction, tx_description )
select id_player, null, dt_transaction+1, am_transaction * -1, 'Thanks for paying. Better luck next time.' from transaction
where id_season is not null and am_transaction < 0;

insert into transaction ( id_player, id_season, dt_transaction, am_transaction, tx_description )
select id_player, null, dt_transaction+1, am_transaction * -1, 'Withdrawal' from transaction
where id_season is not null and am_transaction > 0;