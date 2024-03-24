package edu.java.dto.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_links")
public class ChatLink {

    @EmbeddedId
    private ChatLinkId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", insertable = false, updatable = false, nullable = false)
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id", insertable = false, updatable = false, nullable = false)
    private Link link;

    public void setChat(Chat chat) {
        this.chat = chat;
        this.chat.getChatLinks().add(this);
    }

    public void setLink(Link link) {
        this.link = link;
        this.link.getChatLinks().add(this);
    }
}
