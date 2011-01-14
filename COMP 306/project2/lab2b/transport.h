/*****************************************************************************
 * This implements the transport layer.                                      *
 *                                                                           *
 * Kris Bubendorfer 2002.                                                    *
 *****************************************************************************/

#ifndef TRANSPORT_H
#define TRANSPORT_H

#include <cnet.h>

typedef struct {
    CnetAddr		src;
    CnetAddr		dest;
    short               seqno;     
    short               ackno; 
    int			length;
    char		msg[MAX_MESSAGE_SIZE];
} SEGMENT, *SEGMENTPTR;

#define SEGMENT_HEADER_SIZE (2*sizeof(CnetAddr)+2*sizeof(short)+sizeof(int))
#define SEGMENT_SIZE(s)	    (SEGMENT_HEADER_SIZE + (s).length)
#define MAX_SEGMENT_SIZE (sizeof(SEGMENT))

extern void down_to_transport(CnetEvent ev, CnetTimer timer, CnetData data);
extern int up_to_transport(SEGMENT *msg);
extern int init_transport();
extern void timeout(CnetEvent, CnetTimer, CnetData);

#endif

