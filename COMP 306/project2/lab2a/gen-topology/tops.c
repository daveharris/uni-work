#include <stdio.h>
#include <string.h>
#include <openssl/md5.h>
#include <openssl/rsa.h>
#include <openssl/pem.h>
#include <openssl/err.h>
#include <sys/stat.h>

static void readfile(char *filename);
static void writeKeys(char *filename);

main(int argc,char *argv[]) {
  if(argc != 1) {
    readfile(argv[1]);
  } else {
    printf("You need to pass the name of a topology file into this program.\n");
  }


}

static void readfile(char *filename) {
  char buf[1024];
  FILE *fp = fopen(filename,"r");
  while (fgets(buf, sizeof(buf), fp) != NULL) {
    char *p;
    if((p = strtok(buf,"\"")) && strncmp(p,"#include",8) == 0) {
      p = strtok(NULL,"\"");
      char filename[512];
      sprintf(filename,"../%s",p);
      readfile(filename);
    } else if((p = strtok(buf," ")) && strncmp(p,"host",4) == 0) {
      char *p;
      p = strtok(NULL," ");
      p = strtok(p,"{");
      writeKeys(p);

    }
  }
  return;
}



static void writeKeys(char *hostname) {
  
  /* Create a private and public key and save it to disk */
  struct stat dir_stat; 
  stat("data", &dir_stat);
    
  if (!(S_ISDIR(dir_stat.st_mode))) { 
    mkdir("data",S_IXUSR|S_IRUSR|S_IWUSR);
  }
  RSA *priv_key;
  priv_key = RSA_new();
  priv_key = RSA_generate_key(512,656537,NULL,NULL);
  RSA_check_key(priv_key);

  FILE *fp;
  char filename[256];
  sprintf(filename,"data/%s%s",hostname,"-pr-key.txt");
  fp = fopen(filename,"w");
  PEM_write_RSAPrivateKey(fp, priv_key, NULL, NULL, 0 ,0, NULL);
  fclose(fp);

  char filename2[256];
  sprintf(filename2,"data/%s%s",hostname,"-pub-key.txt");
  FILE *fp3;
  fp3 = fopen(filename2,"w");
  PEM_write_RSAPublicKey(fp3, priv_key);
  fclose(fp3);
}
