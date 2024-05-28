-- CHAT GROUP #############################################
MERGE INTO chat_group(jid, name, created_at, updated_at) values
('120363301052946792@g.us', 'BG TEST', '2024-03-01 20:26:42.415491', '2024-03-01 20:26:42.415491')
;

-- EVENT ##################################################
MERGE INTO event(id, chat_group_jid, event_date, start_at, end_at, template, created_at, updated_at) values
(1, '120363301052946792@g.us', CURRENT_DATE + 30, '14:00', '22:00',
'ID: #ID
*PRÓXIMA JOGATINA - SÁBADO - #DATE*
Local: Shopping Jardim Pamplona - Rua Pamplona, 1704 (Próximo ao metrô Trianon-Masp - são uns 15/20min a pé, não é colado) - 3° Andar, na frente do cinema
Horário: #START_AT

PESSOAS
#PERSON_LIST

- Compre jogos na BoardGamePlay Store! Cupom de *5%* em todo o site: *JOGATINA*!
- Leve casaco! Às vezes o shopping fica *muito* gelado.
- Lembre-se de que o shopping fecha às #END_AT!',
CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
;
