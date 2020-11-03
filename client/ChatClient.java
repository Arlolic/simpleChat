// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  String clientID;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String clientID, String host, int port, ChatIF clientUI) 
  {
    super(host, port); //Call the superclass constructor
    this.clientID = clientID;
    this.clientUI = clientUI;
    try {
		openConnection();
		sendToServer("#login " + clientID);
	} catch (IOException e) {
	}
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
	if(message.startsWith("#"))
	{
		switch(message.split(" ")[0]) {
			case "#quit":
				clientUI.display("Bye bye! ");
				quit();
				break;
			case "#logoff":
				try {
					closeConnection();
				} catch (IOException e) {}
				break;
			case "#sethost":
				if(this.isConnected()) {
					clientUI.display("You are currently connected to a server! ");
				}
				else
					try {
						this.setHost(message.split(" ")[1]);
						clientUI.display("Host set to: " + this.getHost());
					}
					catch(Exception e) {
						clientUI.display("Please add the new host name after #sethost.");
					}
				break;
			case "#setport":
				if(this.isConnected()) {
					clientUI.display("You are currently connected to a server! ");
				}
				else
					try {
						this.setPort(Integer.parseInt(message.split(" ")[1]));
						clientUI.display("Port set to: " + this.getPort());
					}
					catch(Exception e) {
						clientUI.display("Please add the new port as a number after #setport.");
					}
				break;
			case "#login":
				if(this.isConnected()) {
				    try
				    {
				      sendToServer(message);
				    }
				    catch(IOException e)
				    {
				      clientUI.display
				        ("Could not send message to server.  Terminating client.");
				      quit();
				    }
				}
				else
					try {
						this.openConnection();
					    sendToServer(message);
					} catch (IOException e) {clientUI.display("Could not connect to the server with this id");}
				break;
			case "#gethost":
				clientUI.display("The current host name is : " + this.getHost());
				break;
			case "#getport":
				clientUI.display("The current port is : " + this.getPort());
				break;
			default:
				clientUI.display("This command does not exist.");
				break;
				
		}
	}
	
	else {
	    try
	    {
	      sendToServer(message);
	    }
	    catch(IOException e)
	    {
	      clientUI.display
	        ("Could not send message to server.  Terminating client.");
	      quit();
	    }
	}
  }
  
  
  @Override
  protected void connectionException(Exception exception) {
      clientUI.display
      ("Awaiting command");
  }
  /* 
   * This method handles the closure of the connection with the server
   */
  @Override
  public void connectionClosed() {
      clientUI.display
        ("The connection to the server has been lost. ");
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
