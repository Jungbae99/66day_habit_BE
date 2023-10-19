-- 권한
insert into authority (`created_at`, `authority_name`) values (TIMESTAMP '2023-10-11 00:00:00.123456', 'ROLE_ADMIN');
insert into authority (`created_at`, `authority_name`) values (TIMESTAMP '2023-10-11 00:00:00.123456', 'ROLE_MANAGER');
insert into authority (`created_at`, `authority_name`) values (TIMESTAMP '2023-10-11 00:00:00.123456', 'ROLE_USER');

-- 운영자
INSERT INTO member (`created_at`, `certified`, `email`, `introduction`, `password`, `username`) VALUES (TIMESTAMP '2023-10-11 00:00:00.123456', 'CERTIFIED', 'admin@admin.com', '운영자에요', '$2a$10$xvKKHfryO0OPVJgAq9xhQeZWqAvPkN7Z5CDXs3utdsFVathqxuoFG', '운영자');

-- 업로드(기본 프로필 이미지)
insert into upload (`created_at`, `extension`, `file_category`, `origin_name`, `saved_name`, `size`, `type`, `url`, `member_id`) values (TIMESTAMP '2023-10-11 00:00:00.123456', 'png', 'PROFILE', 'user.png', '3d614926-3d3c-472d-abe0-42baff030486-1697700625400.png', 16034, 'image/png' ,'https://66day-bucket.s3.ap-northeast-2.amazonaws.com/profile/3d614926-3d3c-472d-abe0-42baff030486-1697700625400.png', 1);

update member set `profile_image_id` = 1 where `member_id` = 1;

-- 유저
INSERT INTO member (`created_at`, `certified`, `email`, `introduction`, `password`, `username`, `profile_image_id`) VALUES (TIMESTAMP '2023-10-11 00:00:00.123456', 'CERTIFIED', 'user@user.com', '유저에요', '$2a$10$C9qR.UDIavY5A4u7defjae5pU4R2HoLDwE1XGxOZDXy/lj2YGQPWG', '유저', 1);



-- 권한
insert into member_authority (`created_at`, `member_id`, `authority_id`) values (TIMESTAMP '2023-10-11 00:00:00.123456', 1, 1);
insert into member_authority (`created_at`, `member_id`, `authority_id`) values (TIMESTAMP '2023-10-11 00:00:00.123456', 1, 2);
insert into member_authority (`created_at`, `member_id`, `authority_id`) values (TIMESTAMP '2023-10-11 00:00:00.123456', 1, 3);
insert into member_authority (`created_at`, `member_id`, `authority_id`) values (TIMESTAMP '2023-10-11 00:00:00.123456', 2, 3);

