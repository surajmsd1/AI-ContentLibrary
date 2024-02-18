
CREATE TABLE IF NOT EXISTS category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT UK_46ccwnsi9409t36lurvtyljak UNIQUE (name)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS content_page (
    rating INTEGER,
    date DATETIME(6),
    id BIGINT NOT NULL AUTO_INCREMENT,
    model_type VARCHAR(255),
    prompt VARCHAR(255),
    response MEDIUMTEXT,
    reviewed VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS interview (
    total_score INTEGER NOT NULL,
    interview_id BIGINT NOT NULL AUTO_INCREMENT,
    overall_analysis VARCHAR(255),
    PRIMARY KEY (interview_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS interview_question (
    score INTEGER NOT NULL,
    interview_id BIGINT,
    question_id BIGINT NOT NULL AUTO_INCREMENT,
    analysis VARCHAR(255),
    question VARCHAR(255) NOT NULL,
    response VARCHAR(255),
    PRIMARY KEY (question_id),
    FOREIGN KEY (interview_id) REFERENCES interview (interview_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS page_order_entry (
    sequence_index INTEGER,
    category_id BIGINT,
    id BIGINT NOT NULL AUTO_INCREMENT,
    page_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (page_id) REFERENCES content_page (id)
) ENGINE=InnoDB;

ALTER TABLE content_page ADD FULLTEXT index_name (prompt, response);
