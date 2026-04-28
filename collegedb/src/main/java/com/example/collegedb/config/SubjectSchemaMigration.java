package com.example.collegedb.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SubjectSchemaMigration implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(SubjectSchemaMigration.class);

    private final JdbcTemplate jdbcTemplate;

    public SubjectSchemaMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!tableExists("subject")) {
            return;
        }

        if (!columnExists("subject", "department_id")) {
            logger.info("Adding department_id column to subject table");
            jdbcTemplate.execute("ALTER TABLE subject ADD COLUMN department_id BIGINT");
        }

        if (columnExists("subject", "course_id")) {
            logger.info("Backfilling subject.department_id from course.department_id");
            jdbcTemplate.update("""
                UPDATE subject s
                SET department_id = c.department_id
                FROM course c
                WHERE s.course_id = c.course_id
                  AND s.department_id IS NULL
            """);
        }

        if (!constraintExists("subject_department_fk")) {
            logger.info("Adding foreign key from subject.department_id to department.department_id");
            jdbcTemplate.execute("""
                ALTER TABLE subject
                ADD CONSTRAINT subject_department_fk
                FOREIGN KEY (department_id) REFERENCES department(department_id)
            """);
        }

        if (!columnExists("subject", "active")) {
            logger.info("Adding active column to subject table");
            jdbcTemplate.execute("ALTER TABLE subject ADD COLUMN active BOOLEAN DEFAULT TRUE");
        }

        jdbcTemplate.update("UPDATE subject SET active = TRUE WHERE active IS NULL");

        if (columnExists("subject", "code")) {
            logger.info("Backfilling empty subject codes from subject names");
            jdbcTemplate.update("""
                UPDATE subject
                SET code = UPPER(REGEXP_REPLACE(TRIM(name), '[^A-Za-z0-9]+', '_', 'g'))
                WHERE (code IS NULL OR BTRIM(code) = '')
                  AND name IS NOT NULL
                  AND BTRIM(name) <> ''
            """);
        }

        if (columnExists("subject", "course_id")) {
            logger.info("Dropping legacy course_id column from subject table");
            jdbcTemplate.execute("ALTER TABLE subject DROP COLUMN course_id");
        }
    }

    private boolean tableExists(String tableName) {
        Integer count = jdbcTemplate.queryForObject("""
            SELECT COUNT(*)
            FROM information_schema.tables
            WHERE table_schema = current_schema()
              AND table_name = ?
        """, Integer.class, tableName);
        return count != null && count > 0;
    }

    private boolean columnExists(String tableName, String columnName) {
        Integer count = jdbcTemplate.queryForObject("""
            SELECT COUNT(*)
            FROM information_schema.columns
            WHERE table_schema = current_schema()
              AND table_name = ?
              AND column_name = ?
        """, Integer.class, tableName, columnName);
        return count != null && count > 0;
    }

    private boolean constraintExists(String constraintName) {
        Integer count = jdbcTemplate.queryForObject("""
            SELECT COUNT(*)
            FROM information_schema.table_constraints
            WHERE table_schema = current_schema()
              AND constraint_name = ?
        """, Integer.class, constraintName);
        return count != null && count > 0;
    }
}
