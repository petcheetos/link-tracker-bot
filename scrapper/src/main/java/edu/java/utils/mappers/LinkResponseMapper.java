package edu.java.utils.mappers;

import edu.java.models.LinkResponse;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class LinkResponseMapper implements RowMapper<LinkResponse> {
    @Override
    public LinkResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        String urlStr = rs.getString("url");
        URI url = URI.create(urlStr);
        return new LinkResponse(id, url);
    }
}
