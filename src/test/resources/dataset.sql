-- CHAT GROUP #############################################
MERGE INTO chat_group(jid, name, created_at, updated_at) values
('111111111111111111@g.us', 'BG das Ruas',  '2024-03-01 20:26:42.415491', '2024-03-01 20:26:42.415491'),
('222222222222222222@g.us', 'Turma BG',     '2024-01-01 20:26:42.415491', '2024-01-01 20:26:42.415491'),
('333333333333333333@g.us', 'Galera do BG', '2024-02-01 20:26:42.415491', '2024-02-01 20:26:42.415491')
;

-- EVENT ##################################################
MERGE INTO event(id, chat_group_jid, event_date, start_at, end_at, template, created_at, updated_at) values
(1, '111111111111111111@g.us', '2024-03-10', '14:00', '22:00',
'EVENTO JA PASSOU',
'2024-03-01 20:26:42.415491', '2024-03-01 20:26:42.415491'),
-----------------------------------------------------------
(2, '111111111111111111@g.us', CURRENT_DATE + 2, '14:00', '22:00',
'ID: #ID | *PROXIMO EVENTO - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das #START_AT as #END_AT
PESSOAS
#PERSON_LIST',
CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-----------------------------------------------------------
(3, '111111111111111111@g.us', CURRENT_DATE + 5, '14:00', '22:00',
'ID: #ID | *2° EVENTO DO GRUPO - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das #START_AT as #END_AT
PESSOAS
#PERSON_LIST',
CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-----------------------------------------------------------
(4, '333333333333333333@g.us', CURRENT_DATE + 2, '14:00', '22:00',
'ID: #ID | *EVENTO SEM PESSOAS - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das #START_AT as #END_AT
PESSOAS
#PERSON_LIST',
CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
;

-- PERSON #################################################
MERGE INTO person(jid, event_id, name, created_at, updated_at) values
('5511911111111:11@s.whatsapp.net', 2, 'Fulana de Tal', '2024-05-01 20:26:42.415491', '2024-05-01 20:26:42.415491'),
('5511922222222:22@s.whatsapp.net', 2, 'Sicrana', '2024-05-02 20:26:42.415491', '2024-05-01 20:26:42.415491'),
('5511933333333:22@s.whatsapp.net', 2, 'Beltrana', '2024-05-03 20:26:42.415491', '2024-05-01 20:26:42.415491')
;
