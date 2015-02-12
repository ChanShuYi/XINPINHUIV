/* �洢����PACKAGE������Ծ�ȼ���(��ÿ��μӾ��ĵ��û���Ϊ����ָ��) */
CREATE OR REPLACE PACKAGE PACK_ANALYSE_VITALITY AS
       
       --����ȫ����Ծ��(�����¡��ܡ��գ�
       PROCEDURE CAL_VITALITY(starttime IN date, endtime IN date);
       --�����»�Ծ��
       PROCEDURE CAL_VITALITY_MONTH(starttime IN date, endtime IN date);
       --�����ܻ�Ծ��
       PROCEDURE CAL_VITALITY_WEEK(starttime IN date, endtime IN date);
       --�����ջ�Ծ��
       PROCEDURE CAL_VITALITY_DAY(starttime IN date, endtime IN date);
       
END PACK_ANALYSE_VITALITY;
/* �洢����PACKAGE BODY������Ծ�ȼ���(��ÿ��μӾ��ĵ��û���Ϊ����ָ��) */
CREATE OR REPLACE PACKAGE BODY PACK_ANALYSE_VITALITY IS

       /* ����ȫ����Ծ�� */
       PROCEDURE CAL_VITALITY(p_starttime IN date, p_endtime IN date) IS
         p_start number(4);
         p_end number(4);
         p_count number(4);  --һ��ѭ���Ĵ���
         p_number number(4);    --�ڼ���
         p_newStartTime date; --�µĿ�ʼʱ��
         p_newEndTime date;   --�µĽ���ʱ��
         p_temp1 number(4);    --��ʱ����
         
         BEGIN
           /* �»�Ծ�� */
           select to_char(p_starttime, 'MM') into p_start from dual;
           select to_char(p_endtime, 'MM') into p_end from dual;
           p_count := p_end - p_start;
           p_number := 1;
           --ѭ������ÿ�µĻ�Ծ��
           while p_number <= p_count loop
             begin
               p_newStartTime := p_starttime + (p_number - 1) * 31;
               p_newEndTime := p_starttime + p_number * 31;
               PACK_ANALYSE_VITALITY.CAL_VITALITY_MONTH(p_newStartTime, p_newEndTime);
               p_number := p_number + 1;
             end;
           end loop;

           /* �ܻ�Ծ�� */
           select to_char(p_starttime,'WW') into p_start from dual;
           select to_char(p_endtime,'WW') into p_end from dual;
           p_count := p_end - p_start;
           p_number := 1;
           --����ѭ��ǰ��Ҫ�Կ�ʼ���ںͽ������ڽ����ӳ����Ӻ�
           --ʹ�俪ʼ����������һ������������������,Ϊ�˼���ľ�ȷ�ͷ���
           select to_char(p_starttime,'D') into p_temp1 from dual;
           p_newStartTime := p_starttime - p_temp1 + 1;
           select to_char(p_endtime,'D') into p_temp1 from dual;
           p_newEndTime := p_endtime + 7 - p_temp1;
           --�ȼ���һ��
           PACK_ANALYSE_VITALITY.CAL_VITALITY_WEEK(p_newStartTime, p_newEndTime);
           p_number := p_number + 1;
           --ѭ������ÿ�ܵĻ�Ծ��
           while p_number <= p_count loop
             begin
               p_newStartTime := p_newEndTime;
               p_newEndTime := p_newEndTime + 7;
               PACK_ANALYSE_VITALITY.CAL_VITALITY_WEEK(p_newStartTime, p_newEndTime);
               p_number := p_number + 1;
             end;
           end loop;

           /* �ջ�Ծ�� */
           select to_char(p_starttime,'DDD') into p_start from dual;
           select to_char(p_endtime,'DDD') into p_end from dual;
           p_count := p_end - p_start;
           p_number := 1;
           --ѭ������ÿ��Ļ�Ծ��
           while p_number <= p_count loop
             begin
               p_newStartTime := p_starttime + p_number - 1;
               p_newEndTime := p_starttime + p_number;
               PACK_ANALYSE_VITALITY.CAL_VITALITY_DAY(p_newStartTime, p_newEndTime);
               p_number := p_number + 1;
             end;
           end loop;
       END CAL_VITALITY;

       /* �����»�Ծ�� */
       PROCEDURE CAL_VITALITY_MONTH(p_starttime IN date, p_endtime IN date) IS
         p_byIp number(10);  --��IPͳ�Ƶľ�������
         p_byName number(10);  --���û���ͳ�Ƶľ�������
         
         BEGIN
            --����IPͳ�ƻ�Ծ��
            select count(*) into p_byIp
              from (select ip, count(1)
                      from t_yw_bitinfo
                     where bittime >= p_starttime
                       and bittime < p_endtime
                     group by ip);
            --����usernameͳ�ƻ�Ծ��
            select count(*) into p_byName
              from (select username, count(1)
                      from t_yw_bitinfo
                     where bittime >= p_starttime
                       and bittime < p_endtime
                     group by username);
           --�����Ծͳ�Ʊ�
           INSERT INTO t_analyse_vitality(starttime, endtime, bz, byip, byname, type) values(p_starttime, p_endtime, 'ÿ�²��뾺���û���', p_byIp, p_byName, 3);
           commit;
       END CAL_VITALITY_MONTH;

       /* �����ܻ�Ծ�� */
       PROCEDURE CAL_VITALITY_WEEK(p_starttime IN date, p_endtime IN date) IS
         p_byIp number(10);  --��IPͳ�Ƶľ�������
         p_byName number(10);  --���û���ͳ�Ƶľ�������
         
         BEGIN
            --����IPͳ�ƻ�Ծ��
            select count(*) into p_byIp
              from (select ip, count(1)
                      from t_yw_bitinfo
                     where bittime >= p_starttime
                       and bittime < p_endtime
                     group by ip);
            --����usernameͳ�ƻ�Ծ��
            select count(*) into p_byName
              from (select username, count(1)
                      from t_yw_bitinfo
                     where bittime >= p_starttime
                       and bittime < p_endtime
                     group by username);
           --�����Ծͳ�Ʊ�
           INSERT INTO t_analyse_vitality(starttime, endtime, bz, byip, byname, type) values(p_starttime, p_endtime, 'ÿ�ܲ��뾺���û���', p_byIp, p_byName, 2);
           commit;
       END CAL_VITALITY_WEEK;

       /* �����ջ�Ծ�� */
       PROCEDURE CAL_VITALITY_DAY(p_starttime IN date, p_endtime IN date) IS
         p_byIp number(10);  --��IPͳ�Ƶľ�������
         p_byName number(10);  --���û���ͳ�Ƶľ�������
         BEGIN
            --����IPͳ�ƻ�Ծ��
            select count(*) into p_byIp
              from (select ip, count(1)
                      from t_yw_bitinfo
                     where bittime >= p_starttime
                       and bittime < p_endtime
                     group by ip);
            --����usernameͳ�ƻ�Ծ��
            select count(*) into p_byName
              from (select username, count(1)
                      from t_yw_bitinfo
                     where bittime >= p_starttime
                       and bittime < p_endtime
                     group by username);
           --�����Ծͳ�Ʊ�
           INSERT INTO t_analyse_vitality(starttime, endtime, bz, byip, byname, type) values(p_starttime, p_endtime, 'ÿ�ղ��뾺���û���', p_byIp, p_byName, 1);
           commit;
           
           EXCEPTION 
                  WHEN NO_DATA_FOUND THEN
                      return ;
       END CAL_VITALITY_DAY;
END PACK_ANALYSE_VITALITY;