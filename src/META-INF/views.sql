create or replace view gscounts as 
select 
  (select count(*) from USRS) as userCount, 
  (select count(*) from game) as gamecount,
  (select count(*) from score) as scorecount,
  (select count(*) from rating) as ratingcount,
  (select sum(rating.rating) from rating) as ratingsum
FROM dual;

create or replace view gsusrmaxscorepom aS
(select usrid, count(usrid) as cnt from score group by usrid);

create or replace view gsusrmaxscore aS
select usrs.usrid, cnt, usrs.uname as uname from 
gsusrmaxscorepom
join usrs on usrs.USRID = gsusrmaxscorepom.USRID
where cnt = (select max(cnt) from gsusrmaxscorepom);

create or replace view gsgamestat_01 as /* for rating */
select gameid, sum(rating) as ratsum, count(gameid) as ratc from rating group by gameid;

create or replace view gsgamestat_02 as /* for score */
select gameid, count(gameid) as scoc, max(score) as scomax, min(score) as scomin from score group by gameid;

create or replace view gsgamestat_03 as /* for comment */
select gameid, count(gameid) as comc from COMMENTS group by GAMEID;

create or replace view gsgamestat as /* for comment */
select game.gameid, gname, ratsum, ratc, scoc, scomax, scomin, comc from game 
join gsgamestat_01 on game.GAMEID = gsgamestat_01.GAMEID
join gsgamestat_02 on game.GAMEID = gsgamestat_02.GAMEID
join gsgamestat_03 on game.GAMEID = gsgamestat_03.GAMEID;

