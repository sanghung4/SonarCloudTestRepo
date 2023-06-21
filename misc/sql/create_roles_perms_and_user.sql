insert into roles values ('657df095-df55-4452-b423-af41b369a06a', 'admin', 'Admin for customer account');
insert into permissions values ('527e3af0-6318-4e27-9e17-fbb251a6e4b6', 'edit_users');
insert into permissions values ('804aac8e-b6ab-4f57-98cf-1b7b041e3916', 'edit_payment_info');

insert into role_permissions values ('657df095-df55-4452-b423-af41b369a06a', '527e3af0-6318-4e27-9e17-fbb251a6e4b6'), ('657df095-df55-4452-b423-af41b369a06a', '804aac8e-b6ab-4f57-98cf-1b7b041e3916');

insert into accounts values ('49d0d44a-e28a-477a-b3f9-566942480f3f', '123123', 'ECLIPSE', '76781c2d-75ff-40ae-a246-48ce97746a84', null);

insert into user_roles values ('76781c2d-75ff-40ae-a246-48ce97746a84','49d0d44a-e28a-477a-b3f9-566942480f3f', '657df095-df55-4452-b423-af41b369a06a');