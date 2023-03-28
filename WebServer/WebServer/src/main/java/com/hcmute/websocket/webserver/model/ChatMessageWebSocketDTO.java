package com.hcmute.websocket.webserver.model;

import com.hcmute.websocket.webserver.types.ChatMessageWebSocketType;
import lombok.Data;

@Data
public class ChatMessageWebSocketDTO {
    /**
     * Tên người dùng của người gửi
     */
    private String sender;

    /**
     * Tên người dùng của người nhận
     */
    private String receiver;

    /**
     * Nội dung tin nhắn (nếu type là MESSAGE thì đây là tin nhắn, còn JOIN, ... thì trường này có thể chứa dữ liệu
     * với ý nghĩa khác
   s  */
    private String message;

    /**
     * Loại thông tin gửi lên server: JOIN (client vừa mới subscribe), MESSAGE (client gửi tin nhắn cho nhau), LEAVE (client thoát)
     */
    private ChatMessageWebSocketType type;

    /**
     * Cái này có giải thích trong controller rồi
     */
    private String randomHash;
}
