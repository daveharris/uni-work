/*****************************************************************************
 * This implements routing layer communication for cnet. Some different      *
 * implementations of this layer will be required/provided.                  *
 *                                                                           *
 * Based on code by Kris Bubendorfer 2000.                                   *
 *****************************************************************************/

#ifndef ROUTING_H
#define ROUTING_H
#define MAX_KEY_SIZE 256

#include <cnet.h>
#include "network.h"
#include <openssl/md5.h>
#include <openssl/rsa.h>
#include <openssl/pem.h>
#include <openssl/err.h>

extern int down_to_routing(CnetAddr dest, char *segment, int length);
extern int up_to_routing(int fromlink, char *packet, int length); 
extern int init_routing();

#endif
