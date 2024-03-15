package edu.java.repository;

import edu.java.dto.LinkDTO;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import org.springframework.jdbc.core.RowMapper;

public class LinkMapper implements RowMapper<LinkDTO> {
    @Override
    public LinkDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new LinkDTO(
            rs.getLong("id"),
            URI.create(rs.getString("url")),
            rs.getTimestamp("last_updated").toLocalDateTime().atOffset(OffsetDateTime.now().getOffset())
        );
    }
}
