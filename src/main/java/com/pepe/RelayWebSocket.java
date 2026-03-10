package com.pepe;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@WebSocket
public class RelayWebSocket {

    private static final Set<Session> clients =
            Collections.synchronizedSet(new HashSet<>());

    @OnWebSocketConnect
    public void onConnect(Session session) {
        clients.add(session);
        System.out.println("Cliente conectado: " + session.getRemoteAddress());
    }

    @OnWebSocketMessage
    public void onMessage(Session sender, String message) {
        System.out.println("Mensaje recibido: " + message);

        synchronized (clients) {
            for (Session client : clients) {
                if (!client.equals(sender)) {
                    try {
                        client.getRemote().sendString(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
