/*****************************************************************************

	This is an implementation of the simplest (naive) flooding algorithm in
	cnet. Whenever a new network layer packet requires delivery, it is
	transmitted on *all* physical links (inlcuding the one on which it was
	received. To limit the combinatoric explosion in the number of data 
	frames in the whole network, data frames are disgarded after they have 
	travelled MAXHOPS hops.

	This naive flooding algorithm exhibits a very poor efficiency,
	typically only about 2% with 7 nodes.

	In this version we sign all outgoing packets and verify all incomming
	packets to ensure that they are from one of our routers.	When
	we do part 2 with SPF routing, then only the routing tables need
	be signed and verified (there are no routing tables in flooding).

	Kris Bubendorfer 2005.							

 *****************************************************************************/

#include <cnet.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <stdio.h>

#include "transport.h"
#include "routing.h"
#include "datalink.h"

#define	MAXHOPS 4	 /* Change this to 16 for distance vector */

static char* sign(char *message, int m_len);
static int verify(char *message, int m_len,char *smd, char *address);

/* YOUR CODE HERE - Private key for this host */
RSA* private_key;


int init_routing() {
	/* Initialise your routing data structures here.	*/
  
	CNET_enable_application(ALLNODES);
	printf("Initialising Network Layer\n");

	/* Setup debugging */
	CHECK(CNET_set_debug_string(EV_DEBUG1, "NL info"));

	/* Initialise this nodes private key */
	/* Load from appropriate file */
	/* YOUR CODE HERE */
	char filename[256];
  sprintf(filename, "gen-topology/data/%s%s", nodeinfo.nodename, "-pr-key.txt");
  FILE *fp = fopen(filename, "r");
  private_key = PEM_read_RSAPrivateKey(fp, NULL, NULL, NULL);
  fclose(fp);
	return 0;
}

/*
 * This is used by the flooding algorithm. Simply send the packet
 * on all links.
 */
static void flood(char *packet, int length){
	int link;
	
  for(link=1; link<=nodeinfo.nlinks; ++link)
    CHECK(down_to_datalink(link, packet, length));
}


/*
 * Send packets to the data link layer for delivery.
 *
 * You will need to change this to route packets along the right
 * link.
 */
static void sendpacket(NL_PACKET *p) {
	flood((char *)p, PACKET_SIZE(*p));
}

/*
 * Handle packets passed from the application layer.	Package them up 
 * in a network layer header and send the packet on down to the data 
 * link layer.
 */
int down_to_routing(CnetAddr dest, char *segment, int length) {
	char *smd;
	NL_PACKET packet;
	NL_PACKET *p = &packet;
	
	p->dest = dest;
	p->kind = NL_DATA;
	p->length = length;
	p->hopsleft = MAXHOPS;

	memcpy(p->msg, segment, p->length);
	memcpy(p->from_nodename, nodeinfo.nodename, MAX_NODENAME_LEN);

	/* Set the packet's signed message digest based on the message */

	smd = sign((char *)p->msg, p->length);

	/* YOUR CODE HERE code here to copy the smd into the packet */

  memcpy(p->smd, smd, RSA_size(private_key));
  free(smd);
	printf("Sending Packet\n");

	sendpacket(p);
	return(0);
}

/*
 * Handle packets passed up from the datalink layer.
 * Decide if packet contains data or a routing table update.
 * If data, and the packet is for us, pass it on to application layer.
 * If packet is not for us, find next hop from routing table and send
 * packet back down to datalink layer.
 * If packet is a routing table update, update the table.
 */
int up_to_routing(int link, char *packet, int length) {
	NL_PACKET	*p;	
	p = (NL_PACKET *)packet;

	/* verify the packet, if it is ok process it normally */
	
	if(verify((char *)p->msg,p->length,p->smd,p->from_nodename)) {
		switch(p->kind){
			case NL_DATA:
				if(p->dest == nodeinfo.address)	    /* This one is for us. */
					up_to_transport((SEGMENT*)p->msg);
				else								/* Not for us. */
					if(--p->hopsleft > 0)
						sendpacket(p);
						break;
			case NL_ROUTE:
			break;
		}
	}
	else {
		printf("\nError got a message with the wrong hash from %d\n",link);
	}
	return(0);
}

/*****************************************************************************

	Functions for Signing and Verifying

 *****************************************************************************/

/* 
 * produces a signed message digest (SMD) of the message using an MD5 hash 
 * and a private key 
 */

static char* sign(char *message, int m_len) {
  char* md5 = calloc(1, MD5_DIGEST_LENGTH);
  MD5(message, m_len, md5);
  char* smd = calloc(1, RSA_size(private_key));
  int siglen = 0;
	RSA_sign(NID_md5, md5, MD5_DIGEST_LENGTH, smd, &siglen, private_key);  
  return smd;
}

/* 
 *	Verifies the message against the signed message digest (SMD), uses 
 *	the addresses public key 
 */

static int verify(char *message, int m_len, char *smd, char *address) {
  char* md5 = calloc(1, MD5_DIGEST_LENGTH);
  MD5(message, m_len, md5);
  
  RSA* public_key;
  char filename[256];
  sprintf(filename, "gen-topology/data/%s%s", address, "-pub-key.txt");
  FILE *fp = fopen(filename, "r");
  public_key = PEM_read_RSAPublicKey(fp, NULL, NULL, NULL);
  fclose(fp);

  int value = RSA_verify(NID_md5, md5, MD5_DIGEST_LENGTH, smd, RSA_size(public_key), public_key);
  if(value == 1) {
    printf("Verification sucessful");
    return 1;
  }
  return 0;
}
