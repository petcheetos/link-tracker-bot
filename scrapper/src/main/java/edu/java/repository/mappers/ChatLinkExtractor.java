package edu.java.repository.mappers;

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
        List<ChatLinkDTO> res = new ArrayList<>();
        while (rs.next()) {
            long linkId = rs.getLong("link_id");
            Long chatId = rs.getLong("chat_id");
            ChatLinkDTO chatLinkDTO =
                chatLinkDTOMap.computeIfAbsent(linkId, dto -> new ChatLinkDTO(linkId, new HashSet<>()));
            chatLinkDTO.chatIds().add(chatId);
            if (!res.contains(chatLinkDTO)) {
                res.add(chatLinkDTO);
            }
        }
        return res;
    }
}
