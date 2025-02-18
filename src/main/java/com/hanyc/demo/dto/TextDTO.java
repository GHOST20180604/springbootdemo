package com.hanyc.demo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ：zjx
 * @description：审核文本实体
 * @date ：2023/2/13 9:37
 */
@Data
@Accessors(chain = true)
public class TextDTO {
    /**
     * 文本ID
     */
    private String id;
    /**
     * 文本
     */
    private String text;
    /**
     * word批注定位
     */
    private String wordCommentPosition;

    public TextDTO() {
    }

    public TextDTO(String id, String text, String wordCommentPosition) {
        this.id = id;
        this.text = text;
        this.wordCommentPosition = wordCommentPosition;
    }
}
