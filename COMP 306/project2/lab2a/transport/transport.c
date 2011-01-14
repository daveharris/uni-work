/*****************************************************************************
 * This implements an inadequate transport layer to demonstrate              *
 *                the appropriate cnet structure                             *
 *                                                                           *
 *                                                                           *
 * Kris Bubendorfer 2002, John Hine, 8 August 2001                           *
 *****************************************************************************/

#include "datalink.h"
#include "network.h"
#include "routing.h"
#include "transport.h"
#include <cnet.h>
#include <stdio.h>    /* for FILE and stderr */

/* Global varibles for each instance of the protocol.    */

static int sequenceNo;     /* current sequence number    */
static int acknowledgeNo;  /* current acknowledge number */
static SEGMENT lastseg;    /* the last segment sent      */

/* This function is called whenever the application is ready */
/* to send some data.  You call CNET_read_application to get */
/* the data and destination address.                         */

void down_to_transport(CnetEvent ev, CnetTimer timer, CnetData data) {
  
  lastseg.length = MAX_MESSAGE_SIZE;
  CHECK(CNET_read_application(&lastseg.dest, (char *)&lastseg.msg,
                              &lastseg.length));

  (void)printf(" down from application to, seq=%d, ack=%d\n",
               sequenceNo, acknowledgeNo);
  
  /* Set the correct header values. */
  lastseg.seqno = sequenceNo;        
  lastseg.ackno = acknowledgeNo;
  /* Adjust to include header.  */
  lastseg.length += SEGMENT_HEADER_SIZE; 
  
  CHECK(down_to_routing(lastseg.dest, (char *)&lastseg, lastseg.length));
}

/* This function is "upcalled" by the network layer to deliver */
/* a message.                                                  */

int  up_to_transport(SEGMENT *segment) {
  int len = segment->length - SEGMENT_HEADER_SIZE;

  (void)printf(" up from network, seq=%d, ack=%d\n",
               segment->seqno, segment->ackno);

  CHECK(CNET_write_application((char *)(segment->msg), &len));
  return 0;
}
 
int init_transport(){
  printf("Initialising Transport Layer\n");

  CHECK(CNET_set_handler(EV_APPLICATIONREADY, down_to_transport, 0));
  CNET_disable_application(ALLNODES);

  sequenceNo = 0;          /* initialise global variables */
  acknowledgeNo = 0;

  return(0);
}
