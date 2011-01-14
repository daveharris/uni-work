#include "RegisterUser.h"
#include <sys/socket.h>
#include <time.h>
#include <string>

using namespace std;

RegisterUser::RegisterUser() { 
}

RegisterUser::~RegisterUser() {
}


// This function should look up the dest address (gethostbyname),
// and creates a socket and connects the socket to the server on port 5222
int RegisterUser::connect_socket(const char *dest) {
	//establish the socket number 
	int portno = 5222;
	int sock_fd;
	int result = 1;

	struct sockaddr_in server;
	struct hostent *hp;

	//create the socket
	sock_fd = socket(AF_INET, SOCK_STREAM, 0);

	if (sock_fd < 0) {
		perror ("opening stream socket");
		result = -1;
	}

	server.sin_family = AF_INET;
	hp = gethostbyname(dest);
	if (hp == 0) {
		fprintf(stderr, "%s:unknown host\n", dest);
		result = -1;
	}

	bcopy(hp->h_addr, &server.sin_addr, hp->h_length);
	server.sin_port = htons(portno);
	
	if (connect(sock_fd, (struct sockaddr*)&server, sizeof(server)) < 0) {
		perror("connecting stream socket");
		result = -1;
	}
		
	//Make the socket non-blocking
	if (result != -1) {
		fcntl(sock_fd, F_SETFL, O_NONBLOCK);
	}
	else {
		sock_fd = 0;
	}

	return sock_fd;	
}


// add_user registers a new user with the server.
int RegisterUser::add_user(const char *username, const char *dest, const char* password) {
	int bufferSize = 1024;

	//create the unique identifier using epoch time
	char uniqueID[bufferSize];
	memset(uniqueID, 0, bufferSize);
	sprintf(uniqueID, "%ld", time(NULL));

	int errorCode = 1;
	
	//connect to server
	int socket = connect_socket(dest);

	char *header = new char[bufferSize];
	strcpy(header, "<?xml version='1.0'?><stream:stream xmlns:stream='http://etherx.jabber.org/streams' version='1.0' to='");
	strcat(header, dest);
	strcat(header, "'>");
	
	//send header
	if (write(socket, header, strlen(header)) < 0) {
		perror("writing header on stream socket");
		return 0;
	}
	
	char *iq = new char[bufferSize];
	strcpy(iq, "<iq type='set' id='");
	strcat(iq, uniqueID);
	strcat(iq, "' to='");
	strcat(iq, dest);
	strcat(iq, "'> <query xmlns='jabber:iq:register'><username>");
	strcat(iq, username);
	strcat(iq, "</username><password>");
	strcat(iq, password);
	strcat(iq, "</password></query></iq>");

	//send iq message requesting new user
	if (write(socket, iq, strlen(iq)) < 0) {
		perror("writing data on stream socket");	
		return 0;
	}

	sleep(1);

	char reply[bufferSize];
	memset(reply, 0, bufferSize);	
	//read from the socket until there is no data left.
	if((read(socket, reply, bufferSize)) < 0) {
		perror("reading stream message");
		return 0;
	}

	char temp[bufferSize];
	memset(temp, 0, bufferSize);

	//find unique ID
	char *id = strstr(reply, uniqueID);
	//if the buffer contains the unique id you sent, then it is a response
	if (id != NULL) {
		//find type (result or error)
		char *type = strstr(reply, "error");
		if (type != NULL) {
			char *errorString = strstr(type, "code=");
			//if error, find the error code
			if (errorString != NULL) {
				strtok(errorString, "'");
				errorString = strtok(0, "'");
				errorCode = atoi(errorString);
			}
		}

		char closeStream[] = "</stream:stream>";
		
		//send the xml close stream information
		if (write(socket, closeStream, strlen(closeStream)) < 0)
			perror("writing close on stream socket");	
		
		//close the socket
		close(socket);

		//free memory
		delete header;
		delete iq;

		//return the error code
		//1 if no error
		return errorCode;
	}
}
