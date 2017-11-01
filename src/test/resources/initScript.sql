insert into TICKET_USER (USERID, FIRST_NAME, LAST_NAME, PW, ROLE  )
VALUES ('tutor', 'tutor', 'tutor', 'tutor', 'TU');

insert into TICKET_USER (USERID, FIRST_NAME, LAST_NAME, PW, ROLE  )
VALUES ('admin', 'admin', 'admin', 'admin', 'AD');

insert into TICKET_USER (USERID, FIRST_NAME, LAST_NAME, PW, ROLE  )
VALUES ('student', 'student', 'student', 'student', 'ST');

insert into CATEGORY (CATEGID, CATEG_NAME, tutor_USERID)
    VALUES('ISEF', 'ISEF', 'tutor');