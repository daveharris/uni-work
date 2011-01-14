import java.io.*;
import java.net.*;

class TCPServer {

  public static void main(String argv[])throws Exception {
    String clientSentence;

    //Create a welcome socket at port 6789
    ServerSocket welcomeSocket = new ServerSocket(6789);

    while (true) {
      //Wait on welcome socket for contact by client
      Socket connectionSocket = welcomeSocket.accept();

      //Create input stream, attached to socket
      BufferedReader inFromClient = new BufferedReader(new
                                                       InputStreamReader(connectionSocket.getInputStream()));

      //Create output stream, attached to socket
      DataOutputStream outToClient =
          new DataOutputStream(connectionSocket.getOutputStream());

      //Read-in line from socket
      clientSentence = inFromClient.readLine();

      //Write out client request
      System.out.println(clientSentence + "\n");

      //End of while loop, loop back and wait for another client connection
    }
  }
}
