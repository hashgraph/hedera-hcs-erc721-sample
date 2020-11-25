select t.timestamp,
       encode(caller.address, 'hex') as caller,
       t.status,
       case t.function
           when 4 then 'constructor'
           when 5 then 'approve'
           when 6 then 'setApprovalForAll'
           when 7 then 'mint'
           when 8 then 'burn'
           when 9 then 'transferFrom'
           end as "function",
       case t.function
           when 4 then (
               select jsonb_build_object(
                              'tokenName', tc.token_name,
                              'tokenSymbol', tc.token_symbol,
                              'baseURI', tc.base_uri
                          )
               from transaction_constructor tc
               where tc.timestamp = t.timestamp
           )
           when 5 then (
               select jsonb_build_object('spender', encode(spender.address, 'hex'), 'id', '' || ta.token_id)
               from transaction_approve ta
               inner join address spender on spender.id = ta.spender_address_id
               where ta.timestamp = t.timestamp
           )
           when 6 then (
               select jsonb_build_object('operator', encode(operator_.address, 'hex'), 'approved', tsafa.approved)
               from transaction_set_approval_for_all tsafa
                        inner join address operator_ on operator_.id = tsafa.operator_address_id
               where tsafa.timestamp = t.timestamp
           )
           when 7 then (
               select jsonb_build_object('to', encode(to_.address, 'hex'), 'id', '' || tm.token_id)
               from transaction_mint tm
                        inner join address to_ on to_.id = tm.to_address_id
               where tm.timestamp = t.timestamp
           )
           when 8 then (
               select jsonb_build_object('id', '' || tb.token_id)
               from transaction_burn tb
               where tb.timestamp = t.timestamp
           )
           when 9 then (
               select jsonb_build_object('from', encode(from_.address, 'hex'), 'to', encode(to_.address, 'hex'), 'id', '' || ttf.token_id)
               from transaction_transfer_from ttf
                        left join address from_ on from_.id = ttf.from_address_id
                        left join address to_ on to_.id = ttf.to_address_id
               where ttf.timestamp = t.timestamp
           )
           end as data
from transaction t
inner join address caller on caller.id = t.caller_address_id
order by t.timestamp desc
limit 50
