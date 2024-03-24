package edu.java.dto.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "chatLinks")
@Table(name = "chat")
public class Chat {
    @Id
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "chat")
    private List<ChatLink> chatLinks = new ArrayList<>();
}
