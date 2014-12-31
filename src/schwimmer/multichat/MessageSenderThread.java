package schwimmer.multichat;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class MessageSenderThread extends Thread {

	private List<Socket> sockets;
	private BlockingQueue<String> messages;
	private SocketEventListener listener;

	public MessageSenderThread(
			List<Socket> sockets, 
			BlockingQueue<String> messages,
			SocketEventListener listener) {
		this.sockets = sockets;
		this.messages = messages;
		this.listener = listener;
	}

	public void run() {

		while ( true ) {

			try {
				String message = messages.take();

				Iterator<Socket> iter = sockets.iterator();
				while ( iter.hasNext() ) {
					Socket socket = iter.next();
					try {
						OutputStream out = socket.getOutputStream();
						PrintWriter writer = new PrintWriter(out);
						writer.println(message);
						writer.flush();
					} catch (IOException e) {
						e.printStackTrace();
						closeQuietly(socket);
						iter.remove();
						listener.onDisconnect(socket);
					}
				}
			} catch (Exception e) {
				// this should never happen
				throw new RuntimeException(e);
			}
			
		}

	}

	private void closeQuietly(Socket socket) {
		try {
			socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


}
