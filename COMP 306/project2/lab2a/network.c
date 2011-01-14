#include "network.h"
#include "transport.h"
#include "routing.h"
#include "datalink.h"

void reboot_node() {
	CHECK(init_datalink());
	CHECK(init_routing());
	CHECK(init_transport());
}
