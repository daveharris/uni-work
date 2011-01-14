/*****************************************************************************
 * Any data structures that are required in more than one layer of the       *
 * communication protocol.                                                   *
 *                                                                           *
 * Nic Croad 2002, based on code by Kris Bubendorfer 2000.                   *
 *****************************************************************************/

#ifndef NETWORK_H
#define NETWORK_H

#include <cnet.h>

typedef enum    	{ NL_DATA, NL_ACK , NL_ROUTING }   NL_TYPE;

typedef struct {
    CnetAddr		dest;
    NL_TYPE             kind;
    int			hopsleft;
    int			length;
    char		msg[MAX_MESSAGE_SIZE];
} NL_PACKET;

#define PACKET_HEADER_SIZE  \
		    (sizeof(CnetAddr)+sizeof(NL_TYPE)+2*sizeof(int))
#define PACKET_SIZE(p)	    (PACKET_HEADER_SIZE + (p).length)

#endif
