-- 检查 schema 是否存在，若不存在则创建
BEGIN
  EXECUTE IMMEDIATE 'CREATE SCHEMA test';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE != -955 THEN -- -955 是 "schema already exists" 错误码
      RAISE;
    END IF;
END;
/

-- 检查序列是否已存在，若不存在则创建
BEGIN
  FOR rec IN (SELECT sequence_name FROM user_sequences WHERE sequence_name = 'CITY_SEQ') LOOP
      -- 如果序列已存在，不做任何操作
      RETURN;
    END LOOP;

  EXECUTE IMMEDIATE 'CREATE SEQUENCE city_seq START WITH 1 INCREMENT BY 1';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE != -955 THEN -- -955 是 "sequence already exists" 错误码
      RAISE;
    END IF;
END;
/

-- 检查表是否已存在，若不存在则创建
BEGIN
  FOR rec IN (SELECT table_name FROM user_tables WHERE table_name = 'CITY') LOOP
      -- 如果表已存在，不做任何操作
      RETURN;
    END LOOP;

  EXECUTE IMMEDIATE 'CREATE TABLE city
                     (
                       id         NUMBER PRIMARY KEY,
                       name       VARCHAR2(255) NOT NULL,
                       createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updatedat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                     )';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE != -955 THEN -- -955 是 "table already exists" 错误码
      RAISE;
    END IF;
END;
/

-- 修改表以确保 ID 使用序列
BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE city MODIFY (id DEFAULT city_seq.NEXTVAL)';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE != -2292 THEN -- 如果 ALTER 操作失败，则跳过
      RAISE;
    END IF;
END;
/
