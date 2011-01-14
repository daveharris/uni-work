/*****************************************************************************

    This is an empty implementation of a routing layer which uses the link-
    state algorithm algorithm for cnet. The student is required to implement 
    the required functions to initialise the routing tables and respond to
    the loss of links (reported by the event EV_LINKSTATE) and to forward 
    packets to the correct datalink according to the routing information base
    derived from the link-state protocol. Data packets are identified as of 
    "kind" NL_DATA and are not signed with a signed message digest (SMD), 
    however packets associated with the routing protocol are signed with an 
    SMD which is created using the sign(..) function and verified using the 
    verify(..) function.

    Routing packets can be created and exchanged between nodes and are 
    identified as of "kind" DL_ROUTE. As packets arrive from the datalinks 
    they are initially processed by the function up_to_routing(...) and are
    processed dependent on their "kind". Data packets (kind = NL_DATA) are sent
    up_to_transport(...) and routing packets (kind = NL_ROUTE) are verified 
    and can then be processed by your routing functions. 

    A node generates application data to be transmitted and the decision on 
    which link to send the data is made when the packet is sent to function
    sendpacket(.) in function down_to_routing(...). The function sendpacket(.)
    selects a datalink based on the forwarding table created through your 
    implementation of the routing protocol. In the sample code here link 1 is 
    always chosen. Sometimes this actually works. 

    To aid you in developing the solution a CNET event handler is delcared in
    the init_routing() function - CNET_set_handler(EV_LINKSTATE, routing_update,0) 
    which will call routing_update_timeout - or another function of your choice
    should an EV_LINKSTATE event occur. EV_LINKSTATE is raised when a link 
    changes state from up to down, or vice versa. 

    This implementation has empty dummy functions sign(..) and verify(..) - 
    these were implemented in the first part of the assignment. Please put the 
    model answer or your own code to complete these functions. 

    Kris Bubendorfer, Peter Komisarczuk, Mark Pritchard 2005, VUW
    Based on previous code such as flooding and distance vector routing. 

 *****************************************************************************/

#include <cnet.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include "transport.h"
#include "routing.h"
#include "datalink.h"

#define	MAXHOPS 4     /* Change this to 16 for distance vector */

/* YOUR CODE HERE - declare SMD/RSA variables */
RSA* private_key;     /* the host's private key */
NL_LINK* link_table;
NL_TABLE* routing_table;
int link_elements;
int routing_elements;

int dirty;

/* YOUR CODE HERE - declare routing algorithm variables */

static void routing_table_init(CnetEvent ev, CnetTimer timer);
static void routing_update(CnetEvent ev, CnetTimer timer);
static void sendpacket(NL_PACKET *p);
static int getNextHop(CnetAddr dest);
static void show_table(CnetEvent ev, CnetTimer timer, CnetData data);
static char* sign(char *message, int m_len);
static int verify(char *message, int m_len,char *smd, char *address);
static void send_ping(NL_PING* hello, int send_link, NL_TYPE kind);
static void flood(char* segment, int length);
static void route_packets(NL_TABLE* table, int entries, int link);
static void forward(NL_PACKET* packet, int link);
/*-------------------------------------------------------------------*/

int init_routing() {
  /* Initialise your routing data structures here.  */
  CnetInt64  when;
  CNET_enable_application(ALLNODES);
  
  printf("Initialising Network Layer\n");

  /* Setup debugging e.g. use function show_table to print out the routing table */
  CHECK(CNET_set_handler(EV_DEBUG1, show_table, 0));
  CHECK(CNET_set_debug_string(EV_DEBUG1, "Route Table"));

  /* Setup routing */
  CHECK(CNET_set_handler(EV_TIMER1, routing_table_init, 0));
  int64_I2L(when, 1);
  CNET_start_timer(EV_TIMER1, when, 0);
  
/*  Initalise element counters*/
  link_elements = 0;
  routing_elements = 0;
  
  /*allocate space for link and routing table*/
  routing_table = calloc(MAX_TABLE_SIZE, sizeof(NL_TABLE));
  link_table = calloc(nodeinfo.nlinks, sizeof(NL_LINK));
  

  /* Read in your private key and initialise for SMD*/
  char filename[256];
  sprintf(filename, "data/%s%s", nodeinfo.nodename, "-pr-key.txt");
  FILE *fp = fopen(filename, "r");
  private_key = PEM_read_RSAPrivateKey(fp, NULL, NULL, NULL);
  fclose(fp);
  RSA_check_key(private_key);

	return 0;

  /* 
     This enables a function to be called in order to performa an update of
     routing by calling routing_update function when a link goes down.
   */
  CHECK(CNET_set_handler(EV_LINKSTATE, routing_update, 0));
  return 0;
}


/*Method for printing the neighbours table and routing table*/
static void show_table(CnetEvent ev, CnetTimer timer, CnetData data) {
  /*print the link table*/
  printf("\nNeighbours Table (with %d neighbours)", nodeinfo.nlinks);
  printf("\nAddress\tNexthop\tCost\n");
  printf("--------------------\n");
  int i;
  for(i=0; i<link_elements; i++) {
   printf("  %d\t  %d\t  %d\n", link_table[i].address, link_table[i].nexthop, 
          linkinfo[i+1].costperframe);
  }
  
  /*print the routing table*/
  printf("\nRouting Table - %d\n", routing_elements);
  printf("Address\t Via\tCost\n");
  printf("--------------------\n");
  
  for(i=0; i<routing_elements; i++) {
   printf("  %d\t  %d\t  %d\n", routing_table[i].address, routing_table[i].prev_address, 
          routing_table[i].cost);
  }
  
  printf("\n");
}

/* ---------------- LINK STATE ROUTING IMPLEMENTATION ------------------------------ */
/* YOUR CODE HERE
 * Your implementation of link state routing goes here.
 * A couple of skeletal functions are provided as potential
 * starting points for your design
 */


/*
 * Routing periodic events can be dealt with here
 */
static void routing_table_init(CnetEvent ev, CnetTimer timer) {
  if (ev == EV_TIMER1)
    printf("CNET EV_TIMER1 event called routing_table_init function\n");
  else
    printf("router_table_init function called but NOT EV_TIMER1 event\n");

  /* Initalise hello packets */
  NL_PING hello;
  hello.source = nodeinfo.address;
  send_ping(&hello, 0, NL_HELLO);
}

/*
 * The state of one or more links has changed.  In this project this means
 * a link has gone down or come back up. Recalculate the routing table.
 */
static void routing_update(CnetEvent ev, CnetTimer timer) {
  switch(ev) {
  case EV_LINKSTATE: printf("Routing table update - EV_LINKSTATE event has occured\n"); break;
  case EV_TIMER1: printf("Routing table update - timeout event has occured\n"); break;
  default: printf("Routing table update - default event occured\n");
  }
  return;
}

/*
 * Send packets to the data link layer for delivery.
 *
 * You will need to change this to route packets to the right link.
 */
static void sendpacket(NL_PACKET *p) {
  int nexthop = getNextHop(p->dest);
  if(nexthop != -1) {
    if(linkinfo[nexthop].linkup) {
     CHECK(down_to_datalink(nexthop,(char *)p, PACKET_SIZE(*p)));
    }
  }
}

/* finds the link a packet needs to go to in order to reach dest */
static int getNextHop(CnetAddr dest) {
  /*int i;
  get via address from table
  for (i = 0; i < routing_elements; i++) {
    if(dest == nodeinfo.address)
      return 1;
    if(routing_table[i].address == dest) {
      If they are a neighbour
      if(routing_table[i].via == nodeinfo.address) {
        find the link that the neighbour is on
        for(i = 0; i < link_elements; i++) {
          if(link_table[i].address == dest) {
            return link_table[i].nexthop;
          }
        }
      }
    }
    return getNextHop(routing_table[i].via);
  }*/
  return 1;
}

/*
 * Handle packets passed from the application layer.  Package them up 
 * in a network layer header and send the packet on down to the data 
 * link layer.
 */
int down_to_routing(CnetAddr dest, char *segment, int length) {
  NL_PACKET packet;
  NL_PACKET *p = &packet;
  p->dest = dest;
  p->kind = NL_DATA;
  p->hopsleft = MAXHOPS;
  
  memcpy(p->from_nodename, nodeinfo.nodename, MAX_NODENAME_LEN);

  p->length = length;
  memcpy(p->msg, segment, p->length);
  sendpacket(p);
  return(0);
}

/*
 * Handle packets passed up from the datalink layer.
 * Decide if packet contains data or is a routing table update.
 * If data, and the packet is for us, pass it on to application layer.
 * If packet is not for us, send packet to the right datalink layer link.
 * If packet is a routing table update, process it and update the table.
 */
int up_to_routing(int link, char *packet, int length) {
  NL_PACKET* p;
  p = (NL_PACKET *)packet;
/*printf("got %d pckt from %d with length %d and message length %d\n", (int)p->kind, link, length, sizeof(packet));*/
  switch(p->kind) {
    case NL_DATA: {
      /*printf("NL_DATA packet received\n");*/
      if(p->dest == nodeinfo.address) {  /* This one is for us. */
        up_to_transport((SEGMENT*)p->msg);
      } 
      else {                          /* not for us. */
        if(--p->hopsleft > 0) {
          sendpacket(p);
        }
      }
      break;
    }

    /*If the packet is a hello packet, then send reply */
    case NL_HELLO: {
     if(link_elements <= nodeinfo.nlinks) {
       NL_PING reply;
       reply.source = nodeinfo.address;
       send_ping(&reply, link, NL_REPLY);
     }
     break;
    }
    
    /* If the packet is a reply packet, then add to table */
    case NL_REPLY: {    
      CnetAddr source = *((CnetAddr *)p->msg);

      /*Add the new link to the link table*/
      link_table[link_elements].address = source;
      link_table[link_elements].nexthop = link;
      link_elements++;
      
      /*Add the new host into the routing table*/
      routing_table[routing_elements].address = source;
      routing_table[routing_elements].cost = linkinfo[link].costperframe;
      routing_table[routing_elements].prev_address = nodeinfo.address;
      routing_elements++;
      
      /*Create the smaller table to send out so we 
      arent sending out blank entries*/
      if(link_elements == nodeinfo.nlinks) {
        NL_TABLE temp_table[link_elements];
        int i;
        for (i = 0; i < link_elements; i++) {
          NL_TABLE temp_entry;
          temp_entry.address = link_table[i].address;
          temp_entry.prev_address = nodeinfo.address;
          temp_entry.cost = linkinfo[i+1].costperframe;
          memcpy(&temp_table[i], &temp_entry, sizeof(NL_TABLE));
        }
        /*flood the network with our neighbours table*/
        flood((char *)temp_table, sizeof(temp_table));
      }
      break;
    }
    
    case NL_ROUTE: {
      /* verify the routing table, if it is ok update your routing table */
      if (verify((char *)p->msg, p->length, p->smd, p->from_nodename)) {
        /* routing packet received so process it */
        NL_TABLE* table = (NL_TABLE*) p->msg;
        printf("Table update received from %s\n", p->from_nodename);
        
        /*perfrom dijkstra's*/
        route_packets(table, p->length / sizeof(NL_TABLE), link);
        
        /*continue flooding the network*/
        forward((NL_PACKET *)packet, link);        
      }
      else {
        printf("\nError got a message with the wrong hash from link %d\n", link);
      }
      break;
    }
    
  }
  return(0);
}

/*------------------------------------------------------------------------------------

	Functions to sign amd verify NL_ROUTE packets

-------------------------------------------------------------------------------------*/

/* produces a signed message digest (SMD) the message using an MD5 hash and a private key */
static char* sign(char* message, int m_len) {
  /* create an MD5 hash of your key */
  char* md5 = calloc(1, MD5_DIGEST_LENGTH);
  MD5(message, m_len, md5);
  char* smd = calloc(1, RSA_size(private_key));
  int siglen = 0;

  /* sign the md5 with your private key */
  if(!RSA_sign(NID_md5, md5, MD5_DIGEST_LENGTH, smd, &siglen, private_key)) {
    char error_code[128];
    /*ERR_error_string(EER_get_error(), error_code);*/
    fprintf(stderr, "Signing did not work ... %s\n", error_code);
  }
  
  return smd;
}

/* verifies the message against the signed message digest (SMD), uses the addresses public key */
static int verify(char* message, int m_len, char* smd, char* address) {
  /* create an MD5 hash of your key */
  char* md5 = calloc(1, MD5_DIGEST_LENGTH);
  MD5(message, m_len, md5);
  
  /* Read in the public key */
  RSA* public_key;
  char filename[256];
  sprintf(filename, "data/%s%s", address, "-pub-key.txt");
  FILE *fp = fopen(filename, "r");
  public_key = PEM_read_RSAPublicKey(fp, NULL, NULL, NULL);
  fclose(fp);

  /* verify the message against the signed message digest (smd) */
  int value = RSA_verify(NID_md5, md5, MD5_DIGEST_LENGTH, smd, RSA_size(public_key), public_key);
  if(value == 1) {
    /*printf("Verification sucessful\n");*/
    return 1;
  }
  return 0;  
}

/*sends out ping messages on the given link (or on all if send_link is 0)*/
static void send_ping(NL_PING* ping, int send_link, NL_TYPE kind) {
  /*create the packet with the given parameters*/
  NL_PACKET packet;
	NL_PACKET *p = &packet;
	p->kind = kind;
  p->length = sizeof(NL_PING);
	p->hopsleft = 1;
  strcpy(p->from_nodename, nodeinfo.nodename);
  memcpy(p->msg, ping, sizeof(NL_PING));

  /*send the packet on the given link based on send_link*/
  if(send_link == 0) {
    int link;
    for(link=1; link<=nodeinfo.nlinks; link++) {
      CHECK(down_to_datalink(link,(char *)p, PACKET_SIZE(*p)));
    }
  }
  else {
    CHECK(down_to_datalink(send_link, (char *)p, PACKET_SIZE(*p)));
  }
}

/*flood the network with the given segment on all links*/
static void flood(char* segment, int length) {
  char* smd;
  NL_PACKET packet;
  NL_PACKET *p = &packet;
  p->kind = NL_ROUTE;
  p->hopsleft = MAXHOPS;
  p->length = length;
  
  /*sign the segment with our RSA private key*/
  smd = sign(segment, length);
  memcpy(p->smd, smd, 64);
  free(smd);

  memcpy(p->msg, segment, p->length);
  memcpy(p->from_nodename, nodeinfo.nodename, MAX_NODENAME_LEN);

  /*send on all links*/
	int link;
  for(link=1; link<=nodeinfo.nlinks; link++) {
    CHECK(down_to_datalink(link, (char*) p, sizeof(packet)));
  }
}

/*forward the given packet on all links*/
static void forward(NL_PACKET* packet, int link) {
  /*decrement maxhops (used for flood control*/
  if(--packet->hopsleft > 0) {
    int i;
    for(i = 0; i < nodeinfo.nlinks; i++) {
      if(i != link && linkinfo[i].linkup) 
        CHECK(down_to_datalink(i, (char*) packet, PACKET_SIZE(*packet)));
    }
  }
}

/*Dijkstra's algorithm*/
static void route_packets(NL_TABLE* table, int entries, int link) {
  int i, j, k, cost, found;
  /*go through the table ...*/
  for(i = 0; i < entries; i++) {
    /*... and if the address is me ... then drop out*/
    if(table[i].address == nodeinfo.address) 
      continue;
    
    found = -1;
    /*go through the routing table ...*/
    for(j = 0; j < routing_elements; j++) {
      /*... and if the address of the given table is in the routing table*/
      if(table[i].address == routing_table[j].address) {
        found = j;
        break;       
      }
    }
    /*if the address wasnt in the routing table ... */
    if(found == -1) {
      /*... then create a new emtry in the table*/
      NL_TABLE temp_entry;
      temp_entry.address = table[i].address;
      temp_entry.prev_address = table[i].prev_address;
      /*go through the routing table*/
      for(k = 0; k < routing_elements; k++) {
        if(temp_entry.prev_address == routing_table[k].address) {
          /*calculate the new cost from the updated table*/
          temp_entry.cost = table[i].cost + routing_table[k].cost;
          break;
        }
      }
      memcpy(&routing_table[routing_elements++], &temp_entry, sizeof(NL_TABLE));
    }
    else {
      for(k = 0; k < routing_elements; k++) {
        if(table[i].prev_address == routing_table[k].address) {
          /*calculate the new cost from the updated table*/
          cost = table[i].cost + routing_table[k].cost;
          printf("%d = %d + %d\n", cost, table[i].cost, routing_table[k].cost);
          break;
        }
      }
      if(cost < routing_table[j].cost) {
        printf("Changing cost from %d to %d for %d, was via %d, now via %d\n", routing_table[found].cost, cost, routing_table[found].address, routing_table[i].prev_address, table[i].prev_address);
        /*create a new table entry*/
        NL_TABLE temp_entry;
        temp_entry.address = routing_table[k].address;
        temp_entry.prev_address = table[i].prev_address;
        temp_entry.cost = cost;
        /*update table*/
        memcpy(&routing_table[k], &temp_entry, sizeof(NL_TABLE));
      }
    }
  }  
}
