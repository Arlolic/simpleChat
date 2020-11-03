import java.io.IOException;
import java.util.Scanner;

import client.ChatClient;
import common.ChatIF;
import ocsf.server.ConnectionToClient;

/**
 * 
 */

/**
 * @author ahcharba
 *
 */
public class ServerConsole implements ChatIF { //Class variables *************************************************
	  
	  /**
	   * The default port to connect on.
	   */
	  final public static int DEFAULT_PORT = 5555;
	  
	  //Instance variables **********************************************
	  
	  /**
	   * The instance of the server that created this ConsoleChat.
	   */
	  EchoServer server;
	  
	  
	  
	  /**
	   * Scanner to read from the console
	   */
	  Scanner fromConsole; 

	  
	  //Constructors ****************************************************

	  /**
	   * Constructs an instance of the serverConsole UI.
	   *
	   * @param host The host to connect to.
	   * @param port The port to connect on.
	   */
	  public ServerConsole(int port) 
	  {
	    server = new EchoServer(port, this);
	    
	    // Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
	  }

	  //Instance methods ************************************************
	  
	  /**
	   * This method waits for input from the console.  Once it is 
	   * received, it sends it to the server's message handler.
	   */
	  public void accept() 
	  {
	    try
	    {
	      String message;
	      server.listen();
	      
	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        if(message.startsWith("#"))
	    	{
	    		switch(message.split(" ")[0]) {
	    			case "#quit":
	    				this.server.sendToAllClients("Server shutting down. Disconnecting.");
	    				this.server.close();
	    				System.exit(0);
	    				break;
	    			case "#stop":
	    				this.server.sendToAllClients("Warning: the server has stopped listening to connections");
	    				this.server.stopListening();
	    				break;
	    			case "#close":
	    				this.server.sendToAllClients("Server shutting down. Disconnecting.");
	    				this.server.close();
	    				break;
	    			case "#setport":
					try {
						this.server.setPort(Integer.parseInt(message.split(" ")[1]));
					} catch (Exception e) {
						display("Please add the new port as a number after #setport.");
					}
	    				break;
	    			case "#start":
	    				this.server.listen();
	    				break;
	    			case "#getport":
	    				display("The port is " + this.server.getPort());
	    				break;
	    			default:
	    				display("Your command is invalid");
	    				break;
	    				
	    		}
	    	}
	        else
	        server.sendToAllClients("SERVER MSG>" + message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }

	  /**
	   * This method overrides the method in the ChatIF interface.  It
	   * displays a message onto the screen.
	   *
	   * @param message The string to be displayed.
	   */
	  public void display(String message) 
	  {
	    System.out.println(message);
	  }

	  
	  //Class methods ***************************************************
	  
	  /**
	   * This method is responsible for the creation of the server UI.
	   *
	   * @param args[0] The host to connect to.
	   */
	  /**
	   * This method is responsible for the creation of 
	   * the server instance (there is no UI in this phase).
	   *
	   * @param args[0] The port number to listen on.  Defaults to 5555 
	   *          if no argument is entered.
	   */
	  public static void main(String[] args) 
	  {
	    int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = DEFAULT_PORT; //Set port to 5555
	    }
		
	    ServerConsole server = new ServerConsole(port);
	    
	    try 
	    {
	    	server.accept(); //Start listening for connections
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("ERROR - Could not listen for clients!");
	    }
	  }

}
