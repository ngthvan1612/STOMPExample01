package com.hcmute.websocket.webserver.controllers;

import com.hcmute.websocket.webserver.WebServerApplication;
import com.hcmute.websocket.webserver.model.ChatMessageWebSocketDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Reference to: <a href="https://www.baeldung.com/spring-websockets-send-message-to-user">...</a>
 * Detail: Step 5 (Invoking convertAndSendToUser())
 */
@Controller
public class WebSocketChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(WebServerApplication.class);

    public WebSocketChatController() {

    }

    @MessageMapping("/secured/room")
    public void sendSpecific(
            SimpMessageHeaderAccessor simpMessageHeaderAccessor,
            @Payload ChatMessageWebSocketDTO msg
    ) throws Exception {
        //1. Đoạn này để log thôi!!
        String sender = msg.getSender();
        String receiver = msg.getReceiver();
        String message = msg.getMessage();

        logger.info(String.format("WS-INFO: %s send to %s: %s", sender, receiver, message));
        // Kết thúc log

        // 2. Lưu trữ vào database
        // -> này tự làm

        // 3. Khúc này quan trong -> gửi đi cho người kia
        /*
            Vì 1 người có thể đăng nhập cùng lúc nhiều máy nên ta phải xử lý các trường hợp sau:
                + A đăng nhập 2 máy PA.1 PA.2, B đăng nhập 3 máy PB.1 PB.2 PB.3:
                    - A dùng máy PA.1 gửi tin nhắn đến PB.2
                        -> thì  máy PA.2 cũng phải thấy được A đã gửi
                        -> 3 máy PB.1 PB.2 PB.3 cũng phải thấy được tin nhắn nhận
                    - Trường hợp còn lại là B dùng 1 trong 3 máy gửi tin nhắn tới A
                        -> xử lý tương tự
                + A cũng có 2 máy như trên, B cũng có 3 máy như trên
                    - Khi A gửi tin nhắn qua cho B:
                        -> 3 máy của B nhận là đúng
                        -> rồi còn 2 máy của A, sao biết máy nào nhận trong khi đó 2 cái máy subcribe cùng 1 endPoint
                        -> Thêm 1 cái hash random vào tin nhắn khi gửi
                        -> Ta vẫn gửi tin nhắn về cho cả 2 máy của A nhưng máy nào có cái hash đó rồi thì không nhận nữa
                        -> EZ
         */

        // 3.1. Gửi qua cho các client của bạn B
        String receiverEndPoint = "/secured/user/queue/specific-user-" + receiver;
        simpMessagingTemplate.convertAndSend(receiverEndPoint, msg);

        // 3.2. Gửi qua cho các client của bạn A
        String senderEndPoint = "/secured/user/queue/specific-user-" + sender;
        simpMessagingTemplate.convertAndSend(senderEndPoint, msg);
    }
}
