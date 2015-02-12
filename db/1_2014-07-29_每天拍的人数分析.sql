/*===========================================================全部数据操作===================================================*/
--删除全部数据
delete from t_yw_productmapbitinfo;
delete from t_yw_product;
delete from t_yw_bitinfo;
--取出全部数据
select * from t_yw_bitinfo;
select * from t_yw_product;
select * from t_yw_productmapbitinfo;
/*==============================================查询某样商品的信息以及出价记录============================================*/
--t_yw_bitinfo 查询某件商品的出价记录,按出价时间降序排列
--trueid:商品真实id    productid:商品对应的编号
select b.*,m.*,b.rowid from t_yw_bitinfo b, t_yw_product p, t_yw_productmapbitinfo m
where b.id =  m.bitinfoid and m.productid = p.id and p.trueid = 550 --and m.productid = 1451 当有多个trueid记录时，用商品编号再次区分 
order by bittime desc;
--t_yw_product trueid:商品的真实id
select t.trueid, t.* from t_yw_product t
where trueid = 3 
order by t.trueid;
/*===============================================删掉某个商品的全部信息=================================================*/
delete from t_yw_productmapbitinfo
where productid = 3; --商品的编号
delete from t_yw_product
where  id = 3;  --商品的编号
delete from t_yw_bitinfo
where id >= 3 and id <= 25; --竞价信息编号
/*===============================================常用查询操作=================================================*/
/* t_yw_product 所有商品以真实ID排列 */
select distinct t.trueid from t_yw_product t
order by t.trueid;

/*===============================================验证数据完整、独一=================================================*/
/* 验证商品信息是否不重复 */
--执行以下两条语句，看其记录数是否相同，相同则不重复,不相同则重复
select t.trueid from t_yw_product t
order by t.trueid;
select distinct t.trueid from t_yw_product t
order by t.trueid;



/*===============================================分析持续时间=================================================*/
--按持续时间排序
select trueid, productname, duration/60, startTime, endTime from t_yw_product
where trueid >= 523
order by duration desc, trueid asc;
--按商品编号排序
select trueid, productname, duration/60, startTime, endTime from t_yw_product
where trueid >= 523
order by trueid asc;
/*===============================================分析竞拍次数=================================================*/
--按竞拍次数排序
select trueid, productname, bitcount, startTime, endTime from t_yw_product
where trueid >= 523
order by bitcount desc, trueid asc;
--选出大于等于1000次的竞拍商品
select trueid, productname, bitcount, startTime, endTime from t_yw_product
where  bitcount >= 1000;

/*===============================================分析竞拍用户=================================================*/
--某样商品参加竞拍的用户数(如果竞拍过程中用户IP地址改变，则会多算一次)
--trueid:商品真实id    productid:商品对应的编号
select b.ip, count(*) from t_yw_bitinfo b, t_yw_product p, t_yw_productmapbitinfo m
where b.id =  m.bitinfoid and m.productid = p.id and p.trueid = 597 --and m.productid = 1451 当有多个trueid记录时，用商品编号再次区分 
group by b.ip;
--查询后的结果与上面的用户数比较，如果不相等，说明有用户在竞拍过程中改名字了。 差额 = 用户改名字次数（或马甲拍的次数） + 同一用户竞拍过程中IP地址变化次数
select b.ip, b.username, count(*) from t_yw_bitinfo b, t_yw_product p, t_yw_productmapbitinfo m
where b.id =  m.bitinfoid and m.productid = p.id and p.trueid = 597 --and m.productid = 1451 当有多个trueid记录时，用商品编号再次区分 
group by b.ip, b.username
order by ip;
--分析是否有人用马甲 
--输入:p.trueid商品真实编号
select * from 
(
   select b.ip, b.username from t_yw_bitinfo b, t_yw_bitinfo b2, t_yw_product p, t_yw_productmapbitinfo m
where b.id =  m.bitinfoid and m.productid = p.id and p.trueid = 597 --and m.productid = 1451 当有多个trueid记录时，用商品编号再次区分 
group by b.ip, b.username
order by ip
) x, 
(
 select b.ip, b.username, count(*) from t_yw_bitinfo b, t_yw_bitinfo b2, t_yw_product p, t_yw_productmapbitinfo m
where b.id =  m.bitinfoid and m.productid = p.id and p.trueid = 597 --and m.productid = 1451 当有多个trueid记录时，用商品编号再次区分 
group by b.ip, b.username
order by ip
) y
where x.ip = y.ip and x.username <> y.username;


/*=====================从523（2014/7/9 20:01:07）商品开始，采用新的竞拍方式，拍一次加固定金钱，减少一定的新币。=====================*/
-- bitcount * bitrule == finalprice 说明开始了新的竞拍模式
select trueid, productname, duration/60, startTime, endTime, bitcount, bitrule, finalprice from t_yw_product
where trueid = 523   --bitcount * bitrule == finalprice 
-- 522 521 没有数据 而520不相等
select trueid, productname, duration/60, startTime, endTime, bitcount, bitrule, finalprice from t_yw_product
where trueid = 520   --bitcount * bitrule != finalprice

/*=====================bitcount * bitrule != finalprice 说明其并发插入出现了问题=====================*/
--如：product.trueid为550的商品，其bitinfo信息里的id为211228,211229都为1.57元
select trueid, productname, duration/60, startTime, endTime, bitcount, bitrule, finalprice from t_yw_product
where trueid = 550   --bitcount * bitrule == finalprice 
select * from t_yw_bitinfo where id in (211228, 211229);

-- 7月21日  313
select count(*) as num
  from (select ip, count(1)
          from t_yw_bitinfo
         where bittime >= to_date('2014-5-28', 'yyyy-MM-dd')
           and bittime <
               to_date('2014-5-29 08:00:00', 'yyyy-MM-dd hh24:mi:ss')
         group by ip);
select count(*) --320
  from (select username, count(1)
          from t_yw_bitinfo
         where bittime >= to_date('2014-5-28', 'yyyy-MM-dd')
           and bittime <
               to_date('2014-5-29 08:00:00', 'yyyy-MM-dd hh24:mi:ss')
         group by username)

-- 7月22日  331
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-22','yyyy-MM-dd') and bittime < to_date('2014-07-23 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-22','yyyy-MM-dd') and bittime < to_date('2014-07-23 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;
-- 7月23日  397
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-23','yyyy-MM-dd') and bittime < to_date('2014-07-24 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7月24日  314
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-24','yyyy-MM-dd') and bittime < to_date('2014-07-25 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7月25日  288
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-25','yyyy-MM-dd') and bittime < to_date('2014-07-26 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7月26日   330
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-26','yyyy-MM-dd') and bittime < to_date('2014-07-27 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7月27日    323
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-27','yyyy-MM-dd') and bittime < to_date('2014-07-28 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7月28日   294
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-28','yyyy-MM-dd') and bittime < to_date('2014-07-29 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7月21 - 28日 一周竞拍人数 IP 734
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-21','yyyy-MM-dd') and bittime < to_date('2014-07-29 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7月21 - 28日 一周竞拍人数 username 741
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-21','yyyy-MM-dd') and bittime < to_date('2014-07-29 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;

-- 7月14 - 21日 一周竞拍人数 IP 680
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-14','yyyy-MM-dd') and bittime < to_date('2014-07-21 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7月14 - 21日 一周竞拍人数 username 673
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-14','yyyy-MM-dd') and bittime < to_date('2014-07-21 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;

-- 7月7 - 14日 一周竞拍人数 IP 676
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-7','yyyy-MM-dd') and bittime < to_date('2014-07-14 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7月7 - 14日 一周竞拍人数 username 626
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-7','yyyy-MM-dd') and bittime < to_date('2014-07-14 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;

-- 6月30 - 7月7日 一周竞拍人数 IP 508
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-06-30','yyyy-MM-dd') and bittime < to_date('2014-07-7 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 6月30 - 7月7日 一周竞拍人数 username 462
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-06-30','yyyy-MM-dd') and bittime < to_date('2014-07-7 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;


/*********** 月增长 ******************/
--5月平均 186
-- 5月  IP 无数据
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-05-1','yyyy-MM-dd') and bittime < to_date('2014-6-1 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 5月  USERNAME 186
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-5-1','yyyy-MM-dd') and bittime < to_date('2014-6-1 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;

--6月平均 516
-- 6月  IP 512
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-06-1','yyyy-MM-dd') and bittime < to_date('2014-7-1 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 6月  USERNAME 521
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-6-1','yyyy-MM-dd') and bittime < to_date('2014-7-1 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;

--7月平均   886
-- 7月  IP  960
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-7-1','yyyy-MM-dd') and bittime < to_date('2014-8-1 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7月  USERNAME  813
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-7-1','yyyy-MM-dd') and bittime < to_date('2014-8-1 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;