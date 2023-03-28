/**
 * Author: Nguyen Thanh Van
 * Created at: 14:14 03/28/2023
 */

package com.hcmute.websocket.webserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 Reference to: <a href="https://www.toptal.com/java/stomp-spring-boot-websocket">Using Spring Boot for WebSocket Implementation with STOMP</a>
 Detail: Step 2
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    /**
     * Reference to: Reference to: <a href="https://www.baeldung.com/spring-websockets-send-message-to-user">Spring WebSockets: Send Messages to a Specific User</a>
     * Detail: Step 3 (Configuration)
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /*
            + Lưu ý không sử dụng setAllowedOrigin mà phải sử dụng setAllowedOriginPatterns
              setAllowedOrigin không cho sử dụng * làm pattern -> lỗi, nó phải là host, ip địa chỉ nào đó

            + Cấu hình địa chỉ này cho controller WebSocketChatController
        */
        registry
                .addEndpoint("/secured/room") //Cấu hình nhận request + response webs-socket vào địa chỉ này, sau khi subscribe mới được dùng cái này
                .setAllowedOriginPatterns("*") //Chỉnh CorsOrigin lại thành *
                .withSockJS();
    }

    /**
     * Reference to: <a href="https://www.baeldung.com/spring-websockets-send-message-to-user">Spring WebSockets: Send Messages to a Specific User</a>
     * Detail: Step 3 (Configuration)
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //Gõ chữ configureMessage (vài ký tự đầu) rồi bấm enter là tự động ra cái hàm override này

        /*
         * 1. Đặt cái địa chi websocket khi bắt đầu cho client đăng ký là /secured/user/queue/specific-user
         * 2. Bất kì client nào muốn dùng web-socket thì phải đăng ký (gọi là subscribe) vào đây trước để nhận event
         *    Nếu không subscribe thì có kết nối đến server cũng như không
         * 3. Địa chỉ này chỉ là prefix (tiền tố) client phải đăng ký vào địa chỉ có tiền tố này là được
         *    ví dụ: /secured/user/queue/specific-user-admin
         *           /secured/user/queue/specific-user-test01
         *           /secured/user/queue/specific-user-ngthvan1612
         *           /secured/user/queue/specific-user-aaajjjww-lbsksd-ahihi
         */
        registry.enableSimpleBroker("/secured/user/queue/specific-user", "/secured/room");
    }
}
