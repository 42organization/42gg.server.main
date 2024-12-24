-- PublicSchedule 테이블
CREATE TABLE public_schedule (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 classification VARCHAR(10) NOT NULL,
                                 event_tag VARCHAR(10),
                                 job_tag VARCHAR(10),
                                 tech_tag VARCHAR(10),
                                 author VARCHAR(255) NOT NULL,
                                 title VARCHAR(255) NOT NULL,
                                 content VARCHAR(255),
                                 link VARCHAR(255),
                                 status VARCHAR(10) NOT NULL,
                                 shared_count INT NOT NULL,
                                 start_time DATETIME NOT NULL,
                                 end_time DATETIME NOT NULL,
                                 created_at DATETIME NOT NULL,
                                 modified_at DATETIME
);

-- PrivateSchedule 테이블
CREATE TABLE private_schedule (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  user_id BIGINT NOT NULL,
                                  public_schedule_id BIGINT NOT NULL,
                                  alarm BOOLEAN NOT NULL,
                                  group_id BIGINT NOT NULL,
                                  status VARCHAR(10) NOT NULL,
                                  created_at DATETIME NOT NULL,
                                  modified_at DATETIME,
                                  CONSTRAINT fk_private_schedule_user FOREIGN KEY (user_id) REFERENCES user(id),
                                  CONSTRAINT fk_private_schedule_public_schedule FOREIGN KEY (public_schedule_id) REFERENCES public_schedule(id)
);
-- ScheduleGroup 테이블
CREATE TABLE schedule_group (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                user_id BIGINT NOT NULL,
                                title VARCHAR(255) NOT NULL,
                                background_color VARCHAR(255) NOT NULL,
                                created_at DATETIME NOT NULL,
                                modified_at DATETIME,
                                CONSTRAINT fk_schedule_group_user FOREIGN KEY (user_id) REFERENCES user(id)
);
