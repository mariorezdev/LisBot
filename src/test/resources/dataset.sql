MERGE INTO chat_group(jid, name, created_at, updated_at) values
('111111111111111111@g.us', 'BG das Ruas', '2024-05-01 20:26:42.415491', '2024-05-01 20:26:42.415491'),
('222222222222222222@g.us', 'Turma BG', '2024-05-01 20:26:42.415491', '2024-05-01 20:26:42.415491'),
('333333333333333333@g.us', 'Galera do BG', '2024-05-01 20:26:42.415491', '2024-05-01 20:26:42.415491')
;

MERGE INTO event(id, chat_group_jid, event_date, start_at, end_at, template, created_at, updated_at) values
(1, '111111111111111111@g.us', '2024-06-01', '14:00', '22:00',
'**PRÓXIMA JOGATINA - SÁBADO - #DATE**
Local: Shopping Jardim Pamplona - Rua Pamplona, 1704 (Próximo ao metrô Trianon-Masp - são uns 15/20min a pé, não é colado) - 3° Andar, na frente do cinema
Horário: #START_AT

PESSOAS
#PERSON_LIST

- Compre jogos na BoardGamePlay Store! Cupom de **5%** em todo o site: **JOGATINA**!
- Leve casaco! Às vezes o shopping fica **muito** gelado.
- Lembre-se de que o shopping fecha às #END_AT!',
'2024-05-01 20:26:42.415491', '2024-05-01 20:26:42.415491'),
(2, '333333333333333333@g.us', '2024-06-01', '14:00', '22:00',
'**PRÓXIMA JOGATINA - SÁBADO - #DATE**
Local: Shopping Jardim Pamplona - Rua Pamplona, 1704 (Próximo ao metrô Trianon-Masp - são uns 15/20min a pé, não é colado) - 3° Andar, na frente do cinema
Horário: #START_AT

PESSOAS
#PERSON_LIST

- Compre jogos na BoardGamePlay Store! Cupom de **5%** em todo o site: **JOGATINA**!
- Leve casaco! Às vezes o shopping fica **muito** gelado.
- Lembre-se de que o shopping fecha às #END_AT!',
'2024-05-01 20:26:42.415491', '2024-05-01 20:26:42.415491')
;

MERGE INTO person(jid, event_id, phone_number, name, created_at, updated_at) values
('5511911111111@s.whatsapp.net', 1, '5511911111111', 'Fulana de Tal', '2024-05-01 20:26:42.415491', '2024-05-01 20:26:42.415491'),
('5511922222222@s.whatsapp.net', 1, '5511922222222', 'Sicrana', '2024-05-02 20:26:42.415491', '2024-05-01 20:26:42.415491'),
('5511933333333@s.whatsapp.net', 1, '5511933333333', 'Beltrana', '2024-05-03 20:26:42.415491', '2024-05-01 20:26:42.415491');
