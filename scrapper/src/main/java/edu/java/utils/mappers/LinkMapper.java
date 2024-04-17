package edu.java.utils.mappers;

import edu.java.dto.entity.Link;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.HashSet;
import org.springframework.jdbc.core.RowMapper;

public class LinkMapper implements RowMapper<Link> {
    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        Link link = new Link();
        link.setId(rs.getLong("id"));
        link.setUrl(rs.getString("url"));
        link.setLastUpdated(rs.getTimestamp("last_updated").toInstant().atOffset(OffsetDateTime.now().getOffset()));
        link.setCheckedAt(rs.getTimestamp("checked_at").toInstant().atOffset(OffsetDateTime.now().getOffset()));
        link.setTrackingChats(new HashSet<>());
        return link;
    }
}
