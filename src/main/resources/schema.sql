CREATE TABLE users (
                       id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                       username VARCHAR2(50) NOT NULL UNIQUE,
                       password VARCHAR2(100) NOT NULL,
                       role VARCHAR2(50) CHECK (role IN ('EMPLOYEE', 'IT_SUPPORT'))
);

CREATE TABLE tickets (
                         id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                         title VARCHAR2(100) NOT NULL,
                         description CLOB NOT NULL,
                         priority VARCHAR2(20) CHECK (priority IN ('Low', 'Medium', 'High')),
                         category VARCHAR2(50) CHECK (category IN ('Network', 'Hardware', 'Software', 'Other')),
                         creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         status VARCHAR2(20) CHECK (status IN ('New', 'In Progress', 'Resolved')) DEFAULT 'New',
                         user_id NUMBER,
                         FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE audit_log (
                           id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                           ticket_id NUMBER,
                           old_status VARCHAR2(20),
                           new_status VARCHAR2(20),
                           change_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           user_id NUMBER,
                           FOREIGN KEY (ticket_id) REFERENCES tickets(id),
                           FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE comments (
                          id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                          content CLOB NOT NULL,
                          created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          ticket_id NUMBER,
                          user_id NUMBER,
                          FOREIGN KEY (ticket_id) REFERENCES tickets(id),
                          FOREIGN KEY (user_id) REFERENCES app_user(id) -- Note: app_user, not users
);