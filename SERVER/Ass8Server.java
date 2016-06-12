package dummy;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.net.InetAddress;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.util.Date;

public class Ass8Server extends JFrame{

	private ServerSocket serverSocket;
	private List<ConnectionThread> connections = new ArrayList<ConnectionThread>();
	private JTextArea textArea;
	private Date serverDate;
	private int portNum;
	private Random randGenerator = new Random();
	private int word1Index;
	private static final int unknownMax=73;
	private static final int knownMax=51;
	
	public Ass8Server(int portNum) {
		
		try {
			this.serverSocket = new ServerSocket(portNum);
			this.portNum = portNum;
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		// Initialize textArea
		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(this.textArea);
	    this.add(scrollPane);
	    
		//Setting frame's layout
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Server");
	    this.pack();
	    this.setSize(new Dimension(400, 200));
	    this.setVisible(true);
	    
	    //Setting IP
	    try{
	    	InetAddress IP=InetAddress.getLocalHost();
	    }
	    catch(Exception e){
	    	System.err.println("Error getting local host IP");
	    	e.printStackTrace();
	    }
	}
	
	public void runForever() {
		
		serverDate = new Date();
		this.textArea.append("Server started listenning on port " + this.portNum + " at " + serverDate.toString() + "\n");
		this.textArea.append("Waiting for client...\n");

		while (true){
			try{
				Socket connectionToClient = this.serverSocket.accept();
				this.textArea.append("Connection established " + " \n");
				this.textArea.append("Host name: " + connectionToClient.getInetAddress().getHostName() + "\n");
				this.textArea.append("IP address: " + connectionToClient.getInetAddress().getHostAddress() + "\n");
				
				ConnectionThread newConn = new ConnectionThread(connectionToClient);
				newConn.start();
				
				Ass8Server.this.connections.add(newConn);
			}

			catch (BindException e){
				System.err.println("BIND EXCEPTION ACCEPTING INCOMING CONNECTION IN SERVER SOCKET");
				System.err.println(e.getStackTrace());
			}
			catch (IOException e){
				System.err.println("IO EXCEPTION LISTENNING FOR INCOMING CONNECTIONS");
				System.err.println(e.getStackTrace());
			}
		}
	}
	
	private void broadcast(String message) {
		for (ConnectionThread connection: connections) {
			connection.sendMessage(message);
		}
	}
	
	class ConnectionThread extends Thread{
		private Socket threadSocket;
		private BufferedReader reader;
		private PrintWriter writer;

		public ConnectionThread(Socket socket){
			this.threadSocket = socket;
			try{
				this.reader = new BufferedReader (new InputStreamReader(threadSocket.getInputStream())) ;
				this.writer = new PrintWriter(new OutputStreamWriter(threadSocket.getOutputStream())) ;
			}
			catch (IOException e){
				System.err.println("IO EXCEPTION READING BUFFERS FOR SERVER CONNECTION THREAD");
				System.err.println(e.getStackTrace());
			}
		}
		
		public void run(){
			while(true){
				try{
					String line = this.reader.readLine();
					if(!line.isEmpty())
						Ass8Server.this.textArea.append(line+"\n");
				}
				catch (IOException e){
					System.err.println("IO EXCEPTION RUNNING SERVER'S THREAD");
					System.err.println(e.getStackTrace());
				}
			}
		}
		
		public void sendMessage(String message){
			this.writer.println(message);
			this.writer.flush();
		}
		
	}
	
	public static void main(String[] args) {
		Ass8Server server = new Ass8Server(2000);
		server.runForever();
	}

	

}
