/*****************************************************************************
 * This implements data link layer communication for cnet. This layer can be *
 * totally reliable or use unreliable links and so loose/corrupt packets.	*
 *																			 *
 * Nic Croad 2002, based on code by Kris Bubendorfer 2000.					 *
 *****************************************************************************/

#include "network.h"
#include "datalink.h"
#include "routing.h"

typedef struct {
	char		packet[FRAME_SIZE];
} DLL_FRAME;

static DLL_FRAME	frame;

void render_frame(CnetEvent ev, CnetTimer timer, CnetData data);

/****************************************************************************
	Write length bytes of the packet to the data link layer on a link.
 ****************************************************************************/
int down_to_datalink(int link, char *packet, int length)
{
	if (!linkinfo[link].linkup) { return 0; }
#ifndef LOSSIE
	CHECK(CNET_write_physical_reliable(link, (char *)packet, &length));
#else
	CHECK(CNET_write_physical(link, (char *)packet, &length));
#endif
	
	return(0);
}

/****************************************************************************
	 Read bytes from the data link layer and pass them up to the transport 
	 layer.
 ****************************************************************************/
void up_to_datalink(CnetEvent ev, CnetTimer timer, CnetData data)
{
	int		link, length;

	length	= sizeof(DLL_FRAME);
	
	CHECK(CNET_read_physical(&link, (char *)&frame, &length));

	CHECK(up_to_routing(link, frame.packet, length));
}

/***************************************************************************
	Initialise the data link layer. 
 ***************************************************************************/
int init_datalink()
{
	printf("Initialising Datalink Layer\n");

	CNET_set_handler(EV_PHYSICALREADY, up_to_datalink, 0);
	/* The following call registers a handler to render frames.
		 Set drawframes = true/false in the topology file to use/disable this. */
	CNET_set_handler(EV_DRAWFRAME, render_frame, 0);
	
	return 0;
}

/***************************************************************************
	Render a data packet as it traverses the network
 ***************************************************************************/
void render_frame(CnetEvent ev, CnetTimer timer, CnetData data) 
{
	/* Ensure that frame drawing is disabled on ALPHA machines or this pointer
		 conversion will crash the program */
	 CnetDrawFrame *df = (CnetDrawFrame *)data;
	 NL_PACKET *p = (NL_PACKET *)df->frame;

	if (p->kind == NL_DATA) {
		df->colour[0] = CN_BLUE;
		df->pixels[0] = 10;
		df->colour[1] = CN_BLUE;
		df->pixels[1] = 30;
	} else if (p->kind == NL_ROUTE) {
		df->colour[0] = CN_CYAN;
		df->pixels[0] = 20;
	} else {
		df->colour[0] = CN_RED; 
		df->pixels[0] = 10;
	}
}
