package com.usermodule.config;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SocketConfig {

    @Value("${socket.host}")
    private String socketHost;
    @Value("${socket.port}")
    private int socketPort;
    private SocketIOServer socketIOServer;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(socketHost);
        config.setPort(socketPort);
        socketIOServer = new SocketIOServer(config);
        socketIOServer.start();
        return socketIOServer;
    }

    @PreDestroy
    public void stopSocketIOServer() {
        this.socketIOServer.stop();
    }
}



