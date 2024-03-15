package edu.java.repository;

import edu.java.dto.ChatDTO;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatMapper implements RowMapper<ChatDTO> {

    @Override
    public ChatDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ChatDTO(rs.getLong("id"));
    }
}
