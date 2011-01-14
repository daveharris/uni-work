/*****************************************************************************
 * This implements data link layer communication for cnet. This layer can be *
 * totally reliable or use unreliable links and so loose/corrupt packets.    *
 *                                                                           *
 * Nic Croad 2002, based on code by Kris Bubendorfer 2000.                   *
 *****************************************************************************/

#ifndef DATALINK_H
#define DATALINK_H

#include "network.h"

/* The maximum size in bytes of any data link frame. We want to assume that 
   this is the same as the max packet size for simplicity. */
#ifndef  FRAME_SIZE 
#define  FRAME_SIZE   sizeof(NL_PACKET)
#endif

/* If this layer is totally reliable or partially lossie. */
/* #define LOSSIE    1 */

int down_to_datalink(int link, char *packet, int length);
void up_to_datalink(CnetEvent ev, CnetTimer timer, CnetData data);
int init_datalink();

#endif
