-- CHAT GROUP #############################################
INSERT INTO chat_group(jid, name) values
('111111111111111111@g.us', 'GRUPO 1'),
('222222222222222222@g.us', 'GRUPO 2')
;

-- EVENT ##################################################
INSERT INTO event(id, chat_group_jid, event_date, start_at, end_at, template) values
(-100, '111111111111111111@g.us', CURRENT_DATE + 2, '14:00', '22:00',
'ID: #ID | *GRUPO 1 - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das #START_AT as #END_AT
PESSOAS
#PERSON_LIST'),
-------------------------------------------------------------
(-200, '222222222222222222@g.us', '2024-03-01', '14:00', '22:00',
'GRUPO 2 EVENTO QUE PASSOU'),
-----------------------------------------------------------
(-201, '222222222222222222@g.us', CURRENT_DATE + 2, '14:00', '22:00',
'ID: #ID | *GRUPO 2 ATUAL - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das #START_AT as #END_AT
PESSOAS
#PERSON_LIST'),
-----------------------------------------------------------
(-202, '222222222222222222@g.us', CURRENT_DATE + 5, '14:00', '22:00',
'ID: #ID | *GRUPO 2 FUTURO - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das #START_AT as #END_AT
PESSOAS
#PERSON_LIST')
;

-- PERSON #################################################
INSERT INTO person(event_id, sender_phone, is_sender, slug, name, created_at) values
(-201, '+5511911111111', true, 'fulana_de_tal', 'Fulana de Tal', '2024-03-01'),
(-201, '+5511922222222', true, 'sicrana',       'Sicrana', '2024-03-02'),
(-201, '+5511933333333', true, 'beltrana',      'Beltrana', '2024-03-03')
;
