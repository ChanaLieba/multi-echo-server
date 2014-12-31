package schwimmer.multichat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.Queue;

public class SocketHandler extends Thread {

	private Socket s;
	private Queue<String> messages;
	private SocketEventListener listener;
	
	public SocketHandler( Socket s, SocketEventListener listener, Queue<String> messages ) {
		this.s = s;
		this.messages = messages;
		this.listener = listener;
		listener.onConnect(s);
	}
	
	public void run() {
		
		try {
			InputStream in = s.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			
			String line;
			while( (line = reader.readLine()) != null ) {
				listener.onMessage(s, line);
				messages.add(line);
			}
			
		} catch (IOException e) {
			closeQuietly();
			listener.onDisconnect(s);
			e.printStackTrace();
		}
		
		
	}

	private void closeQuietly() {
		try {
			s.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
