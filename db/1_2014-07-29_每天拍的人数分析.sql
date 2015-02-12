/*===========================================================ȫ�����ݲ���===================================================*/
--ɾ��ȫ������
delete from t_yw_productmapbitinfo;
delete from t_yw_product;
delete from t_yw_bitinfo;
--ȡ��ȫ������
select * from t_yw_bitinfo;
select * from t_yw_product;
select * from t_yw_productmapbitinfo;
/*==============================================��ѯĳ����Ʒ����Ϣ�Լ����ۼ�¼============================================*/
--t_yw_bitinfo ��ѯĳ����Ʒ�ĳ��ۼ�¼,������ʱ�併������
--trueid:��Ʒ��ʵid    productid:��Ʒ��Ӧ�ı��
select b.*,m.*,b.rowid from t_yw_bitinfo b, t_yw_product p, t_yw_productmapbitinfo m
where b.id =  m.bitinfoid and m.productid = p.id and p.trueid = 550 --and m.productid = 1451 ���ж��trueid��¼ʱ������Ʒ����ٴ����� 
order by bittime desc;
--t_yw_product trueid:��Ʒ����ʵid
select t.trueid, t.* from t_yw_product t
where trueid = 3 
order by t.trueid;
/*===============================================ɾ��ĳ����Ʒ��ȫ����Ϣ=================================================*/
delete from t_yw_productmapbitinfo
where productid = 3; --��Ʒ�ı��
delete from t_yw_product
where  id = 3;  --��Ʒ�ı��
delete from t_yw_bitinfo
where id >= 3 and id <= 25; --������Ϣ���
/*===============================================���ò�ѯ����=================================================*/
/* t_yw_product ������Ʒ����ʵID���� */
select distinct t.trueid from t_yw_product t
order by t.trueid;

/*===============================================��֤������������һ=================================================*/
/* ��֤��Ʒ��Ϣ�Ƿ��ظ� */
--ִ������������䣬�����¼���Ƿ���ͬ����ͬ���ظ�,����ͬ���ظ�
select t.trueid from t_yw_product t
order by t.trueid;
select distinct t.trueid from t_yw_product t
order by t.trueid;



/*===============================================��������ʱ��=================================================*/
--������ʱ������
select trueid, productname, duration/60, startTime, endTime from t_yw_product
where trueid >= 523
order by duration desc, trueid asc;
--����Ʒ�������
select trueid, productname, duration/60, startTime, endTime from t_yw_product
where trueid >= 523
order by trueid asc;
/*===============================================�������Ĵ���=================================================*/
--�����Ĵ�������
select trueid, productname, bitcount, startTime, endTime from t_yw_product
where trueid >= 523
order by bitcount desc, trueid asc;
--ѡ�����ڵ���1000�εľ�����Ʒ
select trueid, productname, bitcount, startTime, endTime from t_yw_product
where  bitcount >= 1000;

/*===============================================���������û�=================================================*/
--ĳ����Ʒ�μӾ��ĵ��û���(������Ĺ������û�IP��ַ�ı䣬������һ��)
--trueid:��Ʒ��ʵid    productid:��Ʒ��Ӧ�ı��
select b.ip, count(*) from t_yw_bitinfo b, t_yw_product p, t_yw_productmapbitinfo m
where b.id =  m.bitinfoid and m.productid = p.id and p.trueid = 597 --and m.productid = 1451 ���ж��trueid��¼ʱ������Ʒ����ٴ����� 
group by b.ip;
--��ѯ��Ľ����������û����Ƚϣ��������ȣ�˵�����û��ھ��Ĺ����и������ˡ� ��� = �û������ִ�����������ĵĴ����� + ͬһ�û����Ĺ�����IP��ַ�仯����
select b.ip, b.username, count(*) from t_yw_bitinfo b, t_yw_product p, t_yw_productmapbitinfo m
where b.id =  m.bitinfoid and m.productid = p.id and p.trueid = 597 --and m.productid = 1451 ���ж��trueid��¼ʱ������Ʒ����ٴ����� 
group by b.ip, b.username
order by ip;
--�����Ƿ���������� 
--����:p.trueid��Ʒ��ʵ���
select * from 
(
   select b.ip, b.username from t_yw_bitinfo b, t_yw_bitinfo b2, t_yw_product p, t_yw_productmapbitinfo m
where b.id =  m.bitinfoid and m.productid = p.id and p.trueid = 597 --and m.productid = 1451 ���ж��trueid��¼ʱ������Ʒ����ٴ����� 
group by b.ip, b.username
order by ip
) x, 
(
 select b.ip, b.username, count(*) from t_yw_bitinfo b, t_yw_bitinfo b2, t_yw_product p, t_yw_productmapbitinfo m
where b.id =  m.bitinfoid and m.productid = p.id and p.trueid = 597 --and m.productid = 1451 ���ж��trueid��¼ʱ������Ʒ����ٴ����� 
group by b.ip, b.username
order by ip
) y
where x.ip = y.ip and x.username <> y.username;


/*=====================��523��2014/7/9 20:01:07����Ʒ��ʼ�������µľ��ķ�ʽ����һ�μӹ̶���Ǯ������һ�����±ҡ�=====================*/
-- bitcount * bitrule == finalprice ˵����ʼ���µľ���ģʽ
select trueid, productname, duration/60, startTime, endTime, bitcount, bitrule, finalprice from t_yw_product
where trueid = 523   --bitcount * bitrule == finalprice 
-- 522 521 û������ ��520�����
select trueid, productname, duration/60, startTime, endTime, bitcount, bitrule, finalprice from t_yw_product
where trueid = 520   --bitcount * bitrule != finalprice

/*=====================bitcount * bitrule != finalprice ˵���䲢���������������=====================*/
--�磺product.trueidΪ550����Ʒ����bitinfo��Ϣ���idΪ211228,211229��Ϊ1.57Ԫ
select trueid, productname, duration/60, startTime, endTime, bitcount, bitrule, finalprice from t_yw_product
where trueid = 550   --bitcount * bitrule == finalprice 
select * from t_yw_bitinfo where id in (211228, 211229);

-- 7��21��  313
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

-- 7��22��  331
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-22','yyyy-MM-dd') and bittime < to_date('2014-07-23 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-22','yyyy-MM-dd') and bittime < to_date('2014-07-23 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;
-- 7��23��  397
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-23','yyyy-MM-dd') and bittime < to_date('2014-07-24 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7��24��  314
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-24','yyyy-MM-dd') and bittime < to_date('2014-07-25 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7��25��  288
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-25','yyyy-MM-dd') and bittime < to_date('2014-07-26 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7��26��   330
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-26','yyyy-MM-dd') and bittime < to_date('2014-07-27 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7��27��    323
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-27','yyyy-MM-dd') and bittime < to_date('2014-07-28 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7��28��   294
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-28','yyyy-MM-dd') and bittime < to_date('2014-07-29 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7��21 - 28�� һ�ܾ������� IP 734
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-21','yyyy-MM-dd') and bittime < to_date('2014-07-29 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7��21 - 28�� һ�ܾ������� username 741
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-21','yyyy-MM-dd') and bittime < to_date('2014-07-29 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;

-- 7��14 - 21�� һ�ܾ������� IP 680
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-14','yyyy-MM-dd') and bittime < to_date('2014-07-21 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7��14 - 21�� һ�ܾ������� username 673
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-14','yyyy-MM-dd') and bittime < to_date('2014-07-21 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;

-- 7��7 - 14�� һ�ܾ������� IP 676
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-7','yyyy-MM-dd') and bittime < to_date('2014-07-14 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7��7 - 14�� һ�ܾ������� username 626
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-07-7','yyyy-MM-dd') and bittime < to_date('2014-07-14 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;

-- 6��30 - 7��7�� һ�ܾ������� IP 508
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-06-30','yyyy-MM-dd') and bittime < to_date('2014-07-7 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 6��30 - 7��7�� һ�ܾ������� username 462
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-06-30','yyyy-MM-dd') and bittime < to_date('2014-07-7 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;


/*********** ������ ******************/
--5��ƽ�� 186
-- 5��  IP ������
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-05-1','yyyy-MM-dd') and bittime < to_date('2014-6-1 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 5��  USERNAME 186
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-5-1','yyyy-MM-dd') and bittime < to_date('2014-6-1 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;

--6��ƽ�� 516
-- 6��  IP 512
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-06-1','yyyy-MM-dd') and bittime < to_date('2014-7-1 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 6��  USERNAME 521
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-6-1','yyyy-MM-dd') and bittime < to_date('2014-7-1 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;

--7��ƽ��   886
-- 7��  IP  960
select ip, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-7-1','yyyy-MM-dd') and bittime < to_date('2014-8-1 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by ip;
-- 7��  USERNAME  813
select username, count(1) from t_yw_bitinfo
where bittime >= to_date('2014-7-1','yyyy-MM-dd') and bittime < to_date('2014-8-1 08:00:00','yyyy-MM-dd hh24:mi:ss')
group by username;