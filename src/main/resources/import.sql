-- 권한
insert into authority (`created_at`, `authority_name`) values (TIMESTAMP '2023-10-11 00:00:00.123456', 'ROLE_ADMIN');
insert into authority (`created_at`, `authority_name`) values (TIMESTAMP '2023-10-11 00:00:00.123456', 'ROLE_MANAGER');
insert into authority (`created_at`, `authority_name`) values (TIMESTAMP '2023-10-11 00:00:00.123456', 'ROLE_USER');

-- 운영자
INSERT INTO member (`created_at`, `certified`, `email`, `introduction`, `password`, `username`) VALUES (TIMESTAMP '2023-10-11 00:00:00.123456', 'CERTIFIED', 'admin@admin.com', '운영자에요', '$2a$10$xvKKHfryO0OPVJgAq9xhQeZWqAvPkN7Z5CDXs3utdsFVathqxuoFG', '운영자');

-- 유저
INSERT INTO member (`created_at`, `certified`, `email`, `introduction`, `password`, `username`) VALUES (TIMESTAMP '2023-10-11 00:00:00.123456', 'CERTIFIED', 'user@user.com', '유저에요', '$2a$10$C9qR.UDIavY5A4u7defjae5pU4R2HoLDwE1XGxOZDXy/lj2YGQPWG', '유저');

-- 권한
insert into member_authority (`created_at`, `member_id`, `authority_id`) values (TIMESTAMP '2023-10-11 00:00:00.123456', 1, 1);
insert into member_authority (`created_at`, `member_id`, `authority_id`) values (TIMESTAMP '2023-10-11 00:00:00.123456', 1, 2);
insert into member_authority (`created_at`, `member_id`, `authority_id`) values (TIMESTAMP '2023-10-11 00:00:00.123456', 1, 3);
insert into member_authority (`created_at`, `member_id`, `authority_id`) values (TIMESTAMP '2023-10-11 00:00:00.123456', 2, 1);

