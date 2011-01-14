#include <sys/socket.h>
#include <sys/types.h>
#include <string.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>



class RegisterUser {
 public:
  RegisterUser();
  ~RegisterUser();
  int connect_socket(const char * dest);
  int add_user(const char *username, const char *dest, const char* password);
};
