

DROP TABLE IF EXISTS pubtypes CASCADE ;
DROP TABLE IF EXISTS pubtypes_article CASCADE ;
DROP TABLE IF EXISTS reference_article CASCADE ;
DROP TABLE IF EXISTS reference CASCADE ;
DROP TABLE IF EXISTS grants CASCADE ;
DROP TABLE IF EXISTS article_grants CASCADE ;
DROP TABLE IF EXISTS article_ids CASCADE ;
DROP TABLE IF EXISTS article_author CASCADE ;
DROP TABLE IF EXISTS references_article CASCADE ;
DROP TABLE IF EXISTS publication_types CASCADE ;
DROP TABLE IF EXISTS author CASCADE ;
DROP TABLE IF EXISTS affiliation CASCADE ;
DROP TABLE IF EXISTS author_affiliation CASCADE ;
DROP TABLE IF EXISTS journal_issue CASCADE ;
DROP TYPE IF EXISTS journal_issue_type CASCADE ;
DROP TABLE IF EXISTS journal CASCADE ;
DROP TABLE IF EXISTS keywords CASCADE ;
DROP TABLE IF EXISTS article_keywords CASCADE ;
DROP TABLE IF EXISTS article CASCADE ;
DROP TABLE IF EXISTS ids CASCADE ;


CREATE TABLE article(
                        id INT PRIMARY KEY,
                        title VARCHAR(1000) NOT NULL,
                        pub_model VARCHAR,
                        date_created DATE NOT NULL,
                        date_completed DATE,
                        CONSTRAINT check_pub_model CHECK ( pub_model IN ('Print', 'Print-Electronic', 'Electronic', 'Electronic-Print', 'Electronic-eCollection') )
);

CREATE TABLE grants (
    identity SERIAL PRIMARY KEY ,
    id VARCHAR(1000),
    acronym VARCHAR(1000),
    country VARCHAR(1000),
    agency VARCHAR(1000) NOT NULL
);

CREATE TABLE article_grants(
                               grant_identity INT,
                               article_id INT,
                               PRIMARY KEY (grant_identity, article_id),
                               FOREIGN KEY (grant_identity) REFERENCES grants(identity),
                               FOREIGN KEY (article_id) REFERENCES article(id)
);
-- 如果grants和article_id有重复的呢？
-- 一般没有重复，如果有，是数据问题，即使需要解决，可以在关系表上建一个哈希表

CREATE TABLE ids (
    identity SERIAL PRIMARY KEY ,
    type VARCHAR ,
    id VARCHAR(1000),
    CONSTRAINT valid_type CHECK (type IN ('pubmed', 'doi'))
                 -- (new) 使用哈希去重后插入
);

CREATE TABLE article_ids(
    ids INT,
    article_id INT,
    PRIMARY KEY (ids, article_id),
    FOREIGN KEY (ids) REFERENCES ids(identity),
    FOREIGN KEY (article_id) REFERENCES article(id)
);

CREATE TABLE reference(
    identity SERIAL PRIMARY KEY ,
    references_id varchar(1000)
                      -- (new) 哈希去重后插入
);

CREATE TABLE reference_article(
    article_id INT,
    references_identity INT,
    PRIMARY KEY (article_id, references_identity),
    FOREIGN KEY (article_id) REFERENCES article(id),
    FOREIGN KEY (references_identity) REFERENCES reference(identity)
);

-- Reference
CREATE TABLE pubtypes(
    identity SERIAL PRIMARY KEY ,
    id VARCHAR(1000),
    name VARCHAR(1000)
);

CREATE TABLE pubtypes_article(
    pubtypes_identity INT,
    article_id INT,
    PRIMARY KEY (pubtypes_identity, article_id),
    FOREIGN KEY (pubtypes_identity) REFERENCES pubtypes(identity),
    FOREIGN KEY (article_id) REFERENCES article(id)
);

CREATE TABLE author (
--     id VARCHAR(1000),
    identity SERIAL PRIMARY KEY ,
    fore_name VARCHAR DEFAULT '',
    last_name VARCHAR NOT NULL ,
    initials VARCHAR DEFAULT '',
    is_collective BOOLEAN DEFAULT FALSE, -- 标识是否为集体名称
    CONSTRAINT unique_name_quadruple UNIQUE (fore_name, last_name, initials, is_collective)
);

CREATE TABLE article_author(
    author_id INT,
    article_id INT,
    PRIMARY KEY (author_id, article_id),
    FOREIGN KEY (author_id) REFERENCES author(identity),
    FOREIGN KEY (article_id) REFERENCES article(id)
);

CREATE TABLE affiliation (
    identity SERIAL PRIMARY KEY,
    name VARCHAR(1000) NOT NULL
);

CREATE TABLE author_affiliation (
    author_id INT,
    affiliation_id INT,
    PRIMARY KEY (author_id, affiliation_id),
    FOREIGN KEY (author_id) REFERENCES author(identity),
    FOREIGN KEY (affiliation_id) REFERENCES affiliation(identity)
);

CREATE TABLE journal (
    identity SERIAL PRIMARY KEY ,
    id VARCHAR(1000) ,
    country VARCHAR(1000),
    title VARCHAR(1000),
    issn VARCHAR(1000)
);

CREATE TABLE journal_issue(
--    identity INT PRIMARY KEY ,
    journal_id INT,
    article_id INT,
   issue VARCHAR(1000) NULL ,
   volume VARCHAR(1000) NULL ,
   PRIMARY KEY (journal_id, article_id),
   FOREIGN KEY (journal_id) REFERENCES journal(identity),
   FOREIGN KEY (article_id) REFERENCES article(id)
);
-- (new) 使用journal issue作为中间关系表，简化数据库结构

CREATE TABLE keywords (
    identity SERIAL PRIMARY KEY ,
    keyword VARCHAR(1000) UNIQUE
                      -- 先用哈希去重,整数比字符串更高效
);

CREATE TABLE article_keywords (
    keywords_identity INT,
    article_id INT,
    PRIMARY KEY (article_id, keywords_identity),
    FOREIGN KEY (keywords_identity) REFERENCES keywords(identity) ,
    FOREIGN KEY (article_id) REFERENCES article(id)
);


