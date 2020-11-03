// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{

	ChatIF console;
	//Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port, ChatIF console) 
	{
		super(port);
		this.console = console;
	}


	//Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient
	(Object msg, ConnectionToClient client)
	{
		console.display("Message received: " + msg + " from " + client.getInfo("clientID"));
		if(msg.toString().startsWith("#login"))
		{
			if(client.getInfo("clientID") == null) {
				client.setInfo("clientID", msg.toString().substring(msg.toString().indexOf(" ")+1));
				console.display(client.getInfo("clientID")+" has logged on.");
				this.sendToAllClients("Please welcome " + client.getInfo("clientID"));}
			else
				try {
					client.sendToClient("You are not allowed to use this command. Terminating your connection.");
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		else {
			this.sendToAllClients(client.getInfo("clientID").toString() + ": " + msg);
		}
	}

	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server starts listening for connections.
	 */
	protected void serverStarted()
	{
		console.display
		("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server stops listening for connections.
	 */
	protected void serverStopped()
	{
		console.display
		("Server has stopped listening for connections.");
	}

	@Override
	synchronized protected void clientDisconnected(
			ConnectionToClient client) {
		console.display(client.getInfo("clientID") + " disconnected from the server");
		this.sendToAllClients("Goodbye, " + client.getInfo("clientID"));}

	@Override
	protected void clientConnected(ConnectionToClient client) {
		console.display(client.getInfo("clientID")  + " connected to the server");
		this.sendToAllClients("Someone has joined the server! ");
	}

	@Override
	synchronized protected void clientException(
			ConnectionToClient client, Throwable exception) {
		clientDisconnected(client);
	}

	//Class methods ***************************************************


}
//End of EchoServer class
