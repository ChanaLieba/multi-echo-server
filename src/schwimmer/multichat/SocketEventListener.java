package schwimmer.multichat;

import java.net.ServerSocket;
import java.net.Socket;

public interface SocketEventListener {

	public void onServerStart(ServerSocket serverSocket);
	public void onConnect(Socket socket);
	public void onDisconnect(Socket socket);
	public void onMessage(Socket socket, String message);
	
}
