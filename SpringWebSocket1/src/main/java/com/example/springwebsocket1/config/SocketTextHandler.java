package com.example.springwebsocket1.config;


import com.example.springwebsocket1.controller.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class SocketTextHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessionStrategy = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static void forwardMessage(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(message);
        } catch (Exception ignored) {}
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Client Accepted : {}", session);

        String sessionId = session.getId();
        sessionStrategy.put(sessionId, session);

        Message message = Message.builder().sender(sessionId).receiver("all").build();
        message.newConnect();

        sessionStrategy.values().stream()
                .filter(conn -> !conn.getId().equals(sessionId))
                .forEach(conn -> {
                    try {
                        SocketTextHandler.forwardMessage(conn, new TextMessage(objectMapper.writeValueAsString(message)));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("Message : {}", message);
        sessionStrategy.values()
                .stream()
                .filter(conn -> !conn.getId().equals(session.getId()))
                .forEach(conn -> SocketTextHandler.forwardMessage(conn, message));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession currentSession, CloseStatus status) {
        log.info("Client Out : {}", currentSession);
        sessionStrategy.remove(currentSession.getId(), currentSession);
        sessionStrategy.values()
                .forEach(conn -> SocketTextHandler.forwardMessage(
                        conn, new TextMessage(String.format("Session %s Disconnected", currentSession.getId()))
                ));
    }

}
