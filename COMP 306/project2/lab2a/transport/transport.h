/*****************************************************************************
 * This implements the transport layer.                                      *
 *                                                                           *
 * Kris Bubendorfer 2002.                                                    *
 *****************************************************************************/

#ifndef TRANSPORT_H
#define TRANSPORT_H

#include "network.h"

typedef struct {
    CnetAddr		src;
    CnetAddr		dest;
    int			seqno;
    int			ackno;
    int			length;
    char		msg[MAX_MESSAGE_SIZE];
} SEGMENT;

#define SEGMENT_HEADER_SIZE (2*sizeof(CnetAddr)+3*sizeof(int))
#define SEGMENT_SIZE(s)	    (SEGMENT_HEADER_SIZE + (s).length)

extern void down_to_transport(CnetEvent ev, CnetTimer timer, CnetData data);
extern int up_to_transport(SEGMENT *msg);
extern int init_transport();

#endif

