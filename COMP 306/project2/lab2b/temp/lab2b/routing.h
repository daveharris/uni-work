/*****************************************************************************
 * This implements routing layer communication for cnet. Some different      *
 * implementations of this layer will be required/provided.                  *
 *                                                                           *
 * Nic Croad 2002, based on code by Kris Bubendorfer 2000.                   *
 *****************************************************************************/

#ifndef ROUTING_H
#define ROUTING_H
#define MAX_KEY_SIZE 256
#define MAX_TABLE_SIZE 20

#include <cnet.h>
#include "network.h"
#include <openssl/md5.h>
#include <openssl/rsa.h>
#include <openssl/pem.h>
#include <openssl/err.h>

typedef struct {           /* NETWORK LAYER ROUTING TABLE */
  CnetAddr address;        /* destination address */
  CnetAddr via;            /* source address */
  int cost;                /* cost associated with route */
} NL_TABLE;

typedef struct {           /* NETWORK LAYER ROUTING TABLE */
  CnetAddr address;        /* destination address */
  int nexthop;             /* nexthop link */
} NL_LINK;

typedef struct {           /* NETWORK LAYER PING */
  CnetAddr source;         /* The source address */
} NL_PING;

typedef struct {           /* NETWORK LAYER ROUTING TABLE UPDATE */
  int seq_num;             /* sequence number of the packet */
  NL_TABLE send_table[MAX_TABLE_SIZE];  /* the array of routing table entries that are to be added to node's table */
} NL_SEND_TABLE;

typedef struct {
  CnetAddr address; /* destination address */
  char pub_key[MAX_KEY_SIZE]; /* Public Key */
} NL_KEYS;


extern int down_to_routing(CnetAddr dest, char *segment, int length);
extern int up_to_routing(int fromlink, char *packet, int length); 
extern int init_routing();

#endif
