package com.example.collegedb.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AttendanceSchemaMigration implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceSchemaMigration.class);

    private final JdbcTemplate jdbcTemplate;

    public AttendanceSchemaMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!tableExists("attendance")) {
            return;
        }

        if (constraintExists("uk_attendance_student_date")) {
            logger.info("Dropping legacy attendance unique constraint on student_id + date");
            jdbcTemplate.execute("ALTER TABLE attendance DROP CONSTRAINT uk_attendance_student_date");
        }

        if (!constraintExists("uk_attendance_student_date_subject")) {
            logger.info("Adding attendance unique constraint on student_id + date + subject_id");
            jdbcTemplate.execute("""
                ALTER TABLE attendance
                ADD CONSTRAINT uk_attendance_student_date_subject
                UNIQUE (student_id, date, subject_id)
            """);
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
