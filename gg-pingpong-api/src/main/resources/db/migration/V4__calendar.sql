-- PublicSchedule 테이블
CREATE TABLE public_schedule (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 classification VARCHAR(20) NOT NULL,
                                 author VARCHAR(255) NOT NULL,
                                 title VARCHAR(255) NOT NULL,
                                 content VARCHAR(255),
                                 link VARCHAR(255),
                                 start_time DATETIME NOT NULL,
                                 end_time DATETIME NOT NULL,
                                 status VARCHAR(20) NOT NULL,
                                 color VARCHAR(255),
                                 created_at DATETIME NOT NULL,
                                 modified_at DATETIME NOT NULL
);

-- PrivateSchedule 테이블
CREATE TABLE private_schedule (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  user_id BIGINT NOT NULL,
                                  public_schedule_id BIGINT NOT NULL,
                                  alarm BIT(1) DEFAULT TRUE,
                                  color VARCHAR(255),
                                  status VARCHAR(20) NOT NULL,
                                  created_at DATETIME NOT NULL,
                                  modified_at DATETIME NOT NULL,
                                  CONSTRAINT fk_private_schedule_user FOREIGN KEY (user_id) REFERENCES user(id),
                                  CONSTRAINT fk_private_schedule_public_schedule FOREIGN KEY (public_schedule_id) REFERENCES public_schedule(id)
);

-- Tag 테이블
CREATE TABLE tag (
                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                     public_schedule_id BIGINT NOT NULL,
                     value VARCHAR(255) NOT NULL,
                     created_at DATETIME NOT NULL,
                     modified_at DATETIME NOT NULL,
                     CONSTRAINT fk_tag_public_schedule FOREIGN KEY (public_schedule_id) REFERENCES public_schedule(id)
);
