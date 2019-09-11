alter table selection_filters
drop foreign key if exists FK32ql4ga245b7ukkaw77c33a7q;

alter table selection_filters
add constraint FK32ql4ga245b7ukkaw77c33a7q
foreign key (filter_id)
references filter (id)
on delete cascade;

alter table selection_filters
add constraint fk_sf_selection
foreign key (selection_id)
references selection (id)
on delete cascade;

-- -----------------------------------------

alter table user
drop foreign key if exists FKiybtklwoljb0m27cxy08wv4s8;

alter table user
add constraint fk_u_current_selection
foreign key (current_selection_id)
references selection (id)
on delete set null;