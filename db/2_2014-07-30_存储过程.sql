/* 存储过程PACKAGE——活跃度计算(用每天参加竞拍的用户作为衡量指标) */
CREATE OR REPLACE PACKAGE PACK_ANALYSE_VITALITY AS
       
       --计算全部活跃度(包括月、周、日）
       PROCEDURE CAL_VITALITY(starttime IN date, endtime IN date);
       --计算月活跃度
       PROCEDURE CAL_VITALITY_MONTH(starttime IN date, endtime IN date);
       --计算周活跃度
       PROCEDURE CAL_VITALITY_WEEK(starttime IN date, endtime IN date);
       --计算日活跃度
       PROCEDURE CAL_VITALITY_DAY(starttime IN date, endtime IN date);
       
END PACK_ANALYSE_VITALITY;
/* 存储过程PACKAGE BODY——活跃度计算(用每天参加竞拍的用户作为衡量指标) */
CREATE OR REPLACE PACKAGE BODY PACK_ANALYSE_VITALITY IS

       /* 计算全部活跃度 */
       PROCEDURE CAL_VITALITY(p_starttime IN date, p_endtime IN date) IS
         p_start number(4);
         p_end number(4);
         p_count number(4);  --一共循环的次数
         p_number number(4);    --第几次
         p_newStartTime date; --新的开始时间
         p_newEndTime date;   --新的结束时间
         p_temp1 number(4);    --临时变量
         
         BEGIN
           /* 月活跃度 */
           select to_char(p_starttime, 'MM') into p_start from dual;
           select to_char(p_endtime, 'MM') into p_end from dual;
           p_count := p_end - p_start;
           p_number := 1;
           --循环计算每月的活跃度
           while p_number <= p_count loop
             begin
               p_newStartTime := p_starttime + (p_number - 1) * 31;
               p_newEndTime := p_starttime + p_number * 31;
               PACK_ANALYSE_VITALITY.CAL_VITALITY_MONTH(p_newStartTime, p_newEndTime);
               p_number := p_number + 1;
             end;
           end loop;

           /* 周活跃度 */
           select to_char(p_starttime,'WW') into p_start from dual;
           select to_char(p_endtime,'WW') into p_end from dual;
           p_count := p_end - p_start;
           p_number := 1;
           --进入循环前需要对开始日期和结束日期进行延长和延后
           --使其开始日期在星期一、结束日期在星期天,为了计算的精确和方便
           select to_char(p_starttime,'D') into p_temp1 from dual;
           p_newStartTime := p_starttime - p_temp1 + 1;
           select to_char(p_endtime,'D') into p_temp1 from dual;
           p_newEndTime := p_endtime + 7 - p_temp1;
           --先计算一次
           PACK_ANALYSE_VITALITY.CAL_VITALITY_WEEK(p_newStartTime, p_newEndTime);
           p_number := p_number + 1;
           --循环计算每周的活跃度
           while p_number <= p_count loop
             begin
               p_newStartTime := p_newEndTime;
               p_newEndTime := p_newEndTime + 7;
               PACK_ANALYSE_VITALITY.CAL_VITALITY_WEEK(p_newStartTime, p_newEndTime);
               p_number := p_number + 1;
             end;
           end loop;

           /* 日活跃度 */
           select to_char(p_starttime,'DDD') into p_start from dual;
           select to_char(p_endtime,'DDD') into p_end from dual;
           p_count := p_end - p_start;
           p_number := 1;
           --循环计算每天的活跃度
           while p_number <= p_count loop
             begin
               p_newStartTime := p_starttime + p_number - 1;
               p_newEndTime := p_starttime + p_number;
               PACK_ANALYSE_VITALITY.CAL_VITALITY_DAY(p_newStartTime, p_newEndTime);
               p_number := p_number + 1;
             end;
           end loop;
       END CAL_VITALITY;

       /* 计算月活跃度 */
       PROCEDURE CAL_VITALITY_MONTH(p_starttime IN date, p_endtime IN date) IS
         p_byIp number(10);  --用IP统计的竞拍人数
         p_byName number(10);  --用用户名统计的竞拍人数
         
         BEGIN
            --根据IP统计活跃度
            select count(*) into p_byIp
              from (select ip, count(1)
                      from t_yw_bitinfo
                     where bittime >= p_starttime
                       and bittime < p_endtime
                     group by ip);
            --根据username统计活跃度
            select count(*) into p_byName
              from (select username, count(1)
                      from t_yw_bitinfo
                     where bittime >= p_starttime
                       and bittime < p_endtime
                     group by username);
           --插入活跃统计表
           INSERT INTO t_analyse_vitality(starttime, endtime, bz, byip, byname, type) values(p_starttime, p_endtime, '每月参与竞拍用户数', p_byIp, p_byName, 3);
           commit;
       END CAL_VITALITY_MONTH;

       /* 计算周活跃度 */
       PROCEDURE CAL_VITALITY_WEEK(p_starttime IN date, p_endtime IN date) IS
         p_byIp number(10);  --用IP统计的竞拍人数
         p_byName number(10);  --用用户名统计的竞拍人数
         
         BEGIN
            --根据IP统计活跃度
            select count(*) into p_byIp
              from (select ip, count(1)
                      from t_yw_bitinfo
                     where bittime >= p_starttime
                       and bittime < p_endtime
                     group by ip);
            --根据username统计活跃度
            select count(*) into p_byName
              from (select username, count(1)
                      from t_yw_bitinfo
                     where bittime >= p_starttime
                       and bittime < p_endtime
                     group by username);
           --插入活跃统计表
           INSERT INTO t_analyse_vitality(starttime, endtime, bz, byip, byname, type) values(p_starttime, p_endtime, '每周参与竞拍用户数', p_byIp, p_byName, 2);
           commit;
       END CAL_VITALITY_WEEK;

       /* 计算日活跃度 */
       PROCEDURE CAL_VITALITY_DAY(p_starttime IN date, p_endtime IN date) IS
         p_byIp number(10);  --用IP统计的竞拍人数
         p_byName number(10);  --用用户名统计的竞拍人数
         BEGIN
            --根据IP统计活跃度
            select count(*) into p_byIp
              from (select ip, count(1)
                      from t_yw_bitinfo
                     where bittime >= p_starttime
                       and bittime < p_endtime
                     group by ip);
            --根据username统计活跃度
            select count(*) into p_byName
              from (select username, count(1)
                      from t_yw_bitinfo
                     where bittime >= p_starttime
                       and bittime < p_endtime
                     group by username);
           --插入活跃统计表
           INSERT INTO t_analyse_vitality(starttime, endtime, bz, byip, byname, type) values(p_starttime, p_endtime, '每日参与竞拍用户数', p_byIp, p_byName, 1);
           commit;
           
           EXCEPTION 
                  WHEN NO_DATA_FOUND THEN
                      return ;
       END CAL_VITALITY_DAY;
END PACK_ANALYSE_VITALITY;