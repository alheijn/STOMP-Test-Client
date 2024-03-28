package com.se2team.STOMP.Test.Client;

import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

public class WebSocketClient {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new StringMessageConverter());

        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                session.subscribe("/topic/websocket-broker-response", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return String.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        System.out.println("Received message from server: " + payload);
                    }
                });

                session.send("/app/create-user", "{\"id\":1,\"username\":\"usernameA\",\"gameLobbyDto\":null}");
            }
        };

        StompSession stompSession = stompClient.connect("ws://localhost:8080/websocket-broker", sessionHandler).get();

        // Keep the client running
        while (true) {
            Thread.sleep(1000); // Adjust as needed
        }
    }
}

