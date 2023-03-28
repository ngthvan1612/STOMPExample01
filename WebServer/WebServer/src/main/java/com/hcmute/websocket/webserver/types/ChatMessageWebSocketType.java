package com.hcmute.websocket.webserver.types;

public enum ChatMessageWebSocketType {
    /**
     * Khi nào mới subscribe xong thì gửi tin nhắn JOIN đến server - cũng không cần xử lý cái này kĩ
     */
    JOIN,

    /**
     * Khi đang nhắn tin thì type là cái này
     */
    MESSAGE,

    /**
     * Khi nào thoát trang web thì gửi lệnh LEAVE, thật ra cũng không cần xử lý, để cho có thôi
     * Khi nào nghiệp vụ thực sự cần trường hợp này thì xử lý -> giống cái JOIN ở trên
     */
    LEAVE
}
