create or replace view jems_vw
as
select
e.start_date_time as event_date,
e.id as event_number,
o.name as organization,

CASE e.type
WHEN 0 then 'Not Specified'
WHEN 1 then 'Interactive Team Building'
WHEN 2 then 'Community Drum Circle'
WHEN 3 then 'Drums Of The World'
WHEN 4 then 'Charity Event'
WHEN 5 then 'Desert Drumming'
WHEN 6 then 'School Event'
WHEN 7 then 'Drum Lesson'
WHEN 8 then 'African Performance'
WHEN 9 then 'Workshop'
WHEN 10 then 'Birthday Party'
WHEN 11 then 'Drum For Fun'
WHEN 12 then 'Wedding'
WHEN 13 then 'Walkathon'
WHEN 14 then 'Party'
WHEN 15 then 'Parade'
WHEN 16 then 'Other'
WHEN 17 then 'Interactive Team Building And DOTW'
WHEN 18 then 'Meeting'
WHEN 19 then 'Rehearsal'
WHEN 20 then 'CarService'
WHEN 21 then 'Drumming Performance'
WHEN 22 then 'Adult Birt hday Party'
WHEN 23 then 'Exhibition'
WHEN 24 then 'School Drumming'
WHEN 25 then 'Nursery Drumming'
WHEN 26 then 'University Drumming'
ELSE 'Unknown'
END as event_type,

CASE e.status
WHEN 0 then 'Tentative'
WHEN 1 then 'Confirmed'
WHEN 2 then 'Cancelled'
WHEN 3 then 'Declined'
ELSE 'Unknown'
END as event_status,

e.title as event_title,
e.description as event_description,
e.number_drummers as number_facilitators,
e.number_people as number_people,
e.client_company as company_name,
e.client_contact_person as company_contact_person,
e.client_phone as company_contact_number,
e.client_email as company_email,
cu.iso_code as currency,
e.location as location,
e.location_lat_long as gps,
co.name as country,
r.name as region,
e.number_drums as number_drums,
e.number_sessions as number_sessions,

CASE e.chairs_required
WHEN 0 then 'No'
WHEN 1 then 'Yes'
ELSE 'Unknown'
END as chairs_required,

(select GROUP_CONCAT(u.name SEPARATOR ',') 
from jems_event_staff_assigned2 as s
inner join jems_staff as u on u.id=s.staff_assigned2
where s.jems_event=e.id) as staff_assigned,

e.receipt_voucher_number as receipt_voucher,
q.q_date as quotation_date,
q.q_number as quotation_number,
i.i_date as invoice_date,
i.i_number as invoice_number,
IFNULL(q.amount1, 0)+IFNULL(q.amount2, 0)+IFNULL(q.amount3, 0)+IFNULL(q.amount4, 0)+IFNULL(q.amount5, 0) as quoted_amount,
IFNULL(i.amount1, 0)+IFNULL(i.amount2, 0)+IFNULL(i.amount3, 0)+IFNULL(i.amount4, 0)+IFNULL(i.amount5, 0) as invoiced_amount,

CASE e.paid
WHEN 0 then 'No'
WHEN 1 then 'Yes'
ELSE 'Unknown'
END as paid_status,

e.paid_amount as paid_amount,
e.paid_date as paid_date,
concat('http://jems.dubaidrums.com/jemsevents/',e.id) as event_link,
concat('http://jems.dubaidrums.com/jemsquotations/',q.id) as quotation_link,
concat('http://jems.dubaidrums.com//jemsinvoices/',i.id) as invoice_link

from jems_event as e
left join jems_quotation as q on q.jems_event=e.id and q.active=1
left join jems_invoice as i on i.jems_event=e.id and i.active=1
left join jems_organization as o on o.id=e.organization and o.active=1
left join jems_currency as cu on cu.id=e.currency
left join jems_country as co on co.id=e.country
left join jems_region as r on r.id=e.region
where e.active=1

-- and i.jems_quotation = q.id