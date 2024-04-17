package edu.java.utils.mappers;

import edu.java.dto.ChatDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ChatMapper implements RowMapper<ChatDTO> {

    @Override
    public ChatDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ChatDTO(rs.getLong("id"));
    }
}
