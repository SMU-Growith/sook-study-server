package org.growith.be.growith.domain.journal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmojiToggleRequest {
    private String emojiType; // "HEART", "LIKE", "LAUGH", "SURPRISE", "CURIOSITY"
}
