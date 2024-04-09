package edu.java.utils.mappers;

import edu.java.dto.ChatLinkDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class ChatLinkExtractor implements ResultSetExtractor<List<ChatLinkDTO>> {

    @Override
    public List<ChatLinkDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, ChatLinkDTO> chatLinkDTOMap = new HashMap<>();
        while (rs.next()) {
            long linkId = rs.getLong("link_id");
            long chatId = rs.getLong("chat_id");
            ChatLinkDTO chatLinkDTO = chatLinkDTOMap.getOrDefault(linkId, new ChatLinkDTO(linkId, new HashSet<>()));
            chatLinkDTO.chatIds().add(chatId);
            chatLinkDTOMap.put(linkId, chatLinkDTO);
        }
        return new ArrayList<>(chatLinkDTOMap.values());
    }
}
