package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry entry) {
        String sql = "insert into time_entries (project_id, user_id, date, hours) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps =
                        con.prepareStatement(sql, new String[]{"id"});
                ps.setLong(1, entry.getProjectId());
                ps.setLong(2, entry.getUserId());
                ps.setDate(3, Date.valueOf(entry.getDate()));
                ps.setInt(4, entry.getHours());
                return ps;
            }
        };

        jdbcTemplate.update(psc, keyHolder);

        return new TimeEntry(keyHolder.getKey().longValue(),
                entry.getProjectId(),
                entry.getUserId(),
                entry.getDate(),
                entry.getHours());
    }

    @Override
    public TimeEntry find(long id) {
        RowMapper<TimeEntry> mapper = new RowMapper<TimeEntry>() {
            @Override
            public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new TimeEntry(
                        rs.getLong(1),
                        rs.getLong(2),
                        rs.getLong(3),
                        rs.getDate(4).toLocalDate(),
                        rs.getInt(5));
            }
        };

        List<TimeEntry> rows = jdbcTemplate.query("select id, project_id, user_id, date, hours from time_entries where id=?", new Object[] {id}, mapper);
        if ((rows == null) || (rows.size()== 0)) {
            return null;
        } else {
            return rows.get(0);
        }
    }

    @Override
    public TimeEntry update(long id, TimeEntry entry) {
       jdbcTemplate.update("update time_entries set project_id=?,user_id=?, date=?, hours=? where id=?", new Object[]{
               entry.getProjectId(),
               entry.getUserId(),
               entry.getDate(),
               entry.getHours(),
               id});
        entry.setId(id);
       return entry;
    }

    @Override
    public List<TimeEntry> list() {

            RowMapper<TimeEntry> mapper = new RowMapper<TimeEntry>() {
                @Override
                public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new TimeEntry(
                            rs.getLong(1),
                            rs.getLong(2),
                            rs.getLong(3),
                            rs.getDate(4).toLocalDate(),
                            rs.getInt(5));
                }
            };

            List<TimeEntry> rows = jdbcTemplate.query("select id, project_id, user_id, date, hours from time_entries", mapper);
            if ((rows == null) || (rows.size()== 0)) {
                return new ArrayList<>();
            } else {
                return rows;
            }
        }


    @Override
    public void delete(long id) {
        jdbcTemplate.execute("delete from time_entries where id=" + id);
    }
}
