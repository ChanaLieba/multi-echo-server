package schwimmer.multichat;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class ServerLogFrame extends JFrame implements SocketEventListener {

	private static final long serialVersionUID = 1L;
	private JTextArea area;

	public ServerLogFrame(String title) {
		
		setSize(800,600);
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		area = new JTextArea();
		// http://stackoverflow.com/questions/9000649/automatically-scroll-to-the-bottom-of-a-text-area
		DefaultCaret caret = (DefaultCaret) area.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		setLayout(new BorderLayout());
		add(new JScrollPane(area), BorderLayout.CENTER);
		
	}

	public JTextArea getArea() {
		return area;
	}
	
	public void append( String text ) {
		area.append(text);
		area.append("\n");
	}

	@Override
	public void onConnect(Socket socket) {
		append("CONNECT " + socket.getInetAddress().toString());
	}

	@Override
	public void onDisconnect(Socket socket) {
		append("DISCONNECT " + socket.getInetAddress().toString());
	}

	@Override
	public void onMessage(Socket socket, String message) {
		append(socket.getInetAddress().toString() + " " + message);
	}

	@Override
	public void onServerStart(ServerSocket serverSocket) {
		append("SERVER START " + serverSocket.getLocalPort());
	}
	
	public static void main( String args[] ) throws IOException {
		ServerLogFrame frame = new ServerLogFrame("Server");
		frame.setVisible(true);
		MultiChatServer server = new MultiChatServer(3773, frame);
		server.run();
	}

	
}
