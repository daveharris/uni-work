Index: routing.c
===================================================================
RCS file: /u/students/harrisdavi3/CVS/lab2b/routing.c,v
retrieving revision 1.1
diff -u -r1.1 routing.c
--- routing.c	25 Sep 2005 00:56:06 -0000	1.1
+++ routing.c	10 Oct 2005 10:58:42 -0000
@@ -52,6 +52,12 @@
 
 /* YOUR CODE HERE - declare SMD/RSA variables */
 RSA* private_key;     /* the host's private key */
+NL_LINK* link_table;
+NL_TABLE* routing_table;
+int link_elements;
+int routing_elements;
+
+int dirty;
 
 /* YOUR CODE HERE - declare routing algorithm variables */
 
@@ -62,24 +68,36 @@
 static void show_table(CnetEvent ev, CnetTimer timer, CnetData data);
 static char* sign(char *message, int m_len);
 static int verify(char *message, int m_len,char *smd, char *address);
-void create_n_send_ping_packet(NL_PING* hello);
-
+static void send_ping(NL_PING* hello, int send_link, NL_TYPE kind);
+static void flood(char* segment, int length);
+static void route_packets(NL_TABLE* table, int entries, int link);
+static void forward(NL_PACKET* packet, int link);
 /*-------------------------------------------------------------------*/
 
 int init_routing() {
   /* Initialise your routing data structures here.  */
   CnetInt64  when;
   CNET_enable_application(ALLNODES);
+  
   printf("Initialising Network Layer\n");
 
   /* Setup debugging e.g. use function show_table to print out the routing table */
   CHECK(CNET_set_handler(EV_DEBUG1, show_table, 0));
-  CHECK(CNET_set_debug_string(EV_DEBUG1, "Routing Table"));
+  CHECK(CNET_set_debug_string(EV_DEBUG1, "Route Table"));
 
   /* Setup routing */
   CHECK(CNET_set_handler(EV_TIMER1, routing_table_init, 0));
   int64_I2L(when, 1);
   CNET_start_timer(EV_TIMER1, when, 0);
+  
+/*  Initalise element counters*/
+  link_elements = 0;
+  routing_elements = 0;
+  
+  /*allocate space for link and routing table*/
+  routing_table = calloc(MAX_TABLE_SIZE, sizeof(NL_TABLE));
+  link_table = calloc(nodeinfo.nlinks, sizeof(NL_LINK));
+  
 
   /* Read in your private key and initialise for SMD*/
   char filename[256];
@@ -99,12 +117,30 @@
   return 0;
 }
 
-/*
- * YOUR CODE HERE - Utility methods, e.g. for printing the contents of routing table etc.
- */
 
+/*Method for printing the neighbours table and routing table*/
 static void show_table(CnetEvent ev, CnetTimer timer, CnetData data) {
-  printf("\nFunction show_table called\n");
+  /*print the link table*/
+  printf("\nNeighbours Table (with %d neighbours)", nodeinfo.nlinks);
+  printf("\nAddress\tNexthop\tCost\n");
+  printf("--------------------\n");
+  int i;
+  for(i=0; i<link_elements; i++) {
+   printf("  %d\t  %d\t  %d\n", link_table[i].address, link_table[i].nexthop, 
+          linkinfo[i+1].costperframe);
+  }
+  
+  /*print the routing table*/
+  printf("\nRouting Table - %d\n", routing_elements);
+  printf("Address\t Via\tCost\n");
+  printf("--------------------\n");
+  
+  for(i=0; i<routing_elements; i++) {
+   printf("  %d\t  %d\t  %d\n", routing_table[i].address, routing_table[i].prev_address, 
+          routing_table[i].cost);
+  }
+  
+  printf("\n");
 }
 
 /* ---------------- LINK STATE ROUTING IMPLEMENTATION ------------------------------ */
@@ -126,27 +162,8 @@
 
   /* Initalise hello packets */
   NL_PING hello;
-  memcpy(&hello.source, &nodeinfo.address, sizeof(CnetAddr));
-  strcpy(hello.type, "hello");
-
-  create_n_send_ping_packet(&hello);  
-
-  /*NL_PACKET packet;
-	NL_PACKET *p = &packet;
-	
-  p->ping = 1;
-	p->kind = NL_ROUTE;
-	p->length = sizeof(hello);
-	p->hopsleft = 1;
-  memcpy(p->from_nodename, nodeinfo.nodename, MAX_NODENAME_LEN);
-  memcpy(p->msg, &hello, sizeof(NL_PING));  
-
-  int link;
-  for(link=1; link<=nodeinfo.nlinks; link++) {
-    down_to_datalink(link,(char *)p, PACKET_SIZE(*p));
-    printf("Sending out hello packet\n");
-  }*/
-
+  hello.source = nodeinfo.address;
+  send_ping(&hello, 0, NL_HELLO);
 }
 
 /*
@@ -178,7 +195,24 @@
 
 /* finds the link a packet needs to go to in order to reach dest */
 static int getNextHop(CnetAddr dest) {
-  /* Oh dear - lets just send it out on link 1 */
+  /*int i;
+  get via address from table
+  for (i = 0; i < routing_elements; i++) {
+    if(dest == nodeinfo.address)
+      return 1;
+    if(routing_table[i].address == dest) {
+      If they are a neighbour
+      if(routing_table[i].via == nodeinfo.address) {
+        find the link that the neighbour is on
+        for(i = 0; i < link_elements; i++) {
+          if(link_table[i].address == dest) {
+            return link_table[i].nexthop;
+          }
+        }
+      }
+    }
+    return getNextHop(routing_table[i].via);
+  }*/
   return 1;
 }
 
@@ -210,37 +244,85 @@
  * If packet is a routing table update, process it and update the table.
  */
 int up_to_routing(int link, char *packet, int length) {
-  NL_PACKET	*p;
+  NL_PACKET* p;
   p = (NL_PACKET *)packet;
+/*printf("got %d pckt from %d with length %d and message length %d\n", (int)p->kind, link, length, sizeof(packet));*/
   switch(p->kind) {
-  case NL_DATA:
-    if(p->dest == nodeinfo.address) {  /* This one is for us. */
-      up_to_transport((SEGMENT*)p->msg);
-    } 
-    else {                          /* not for us. */
-      if(--p->hopsleft > 0) {
-        sendpacket(p);
+    case NL_DATA: {
+      /*printf("NL_DATA packet received\n");*/
+      if(p->dest == nodeinfo.address) {  /* This one is for us. */
+        up_to_transport((SEGMENT*)p->msg);
+      } 
+      else {                          /* not for us. */
+        if(--p->hopsleft > 0) {
+          sendpacket(p);
+        }
       }
+      break;
+    }
+
+    /*If the packet is a hello packet, then send reply */
+    case NL_HELLO: {
+     if(link_elements <= nodeinfo.nlinks) {
+       NL_PING reply;
+       reply.source = nodeinfo.address;
+       send_ping(&reply, link, NL_REPLY);
+     }
+     break;
     }
-    break;
-  case NL_ROUTE:
-    if((int)p->ping == 1) {
-      printf("\nPing message received from link %d\n", link);
+    
+    /* If the packet is a reply packet, then add to table */
+    case NL_REPLY: {    
+      CnetAddr source = *((CnetAddr *)p->msg);
+
+      /*Add the new link to the link table*/
+      link_table[link_elements].address = source;
+      link_table[link_elements].nexthop = link;
+      link_elements++;
       
+      /*Add the new host into the routing table*/
+      routing_table[routing_elements].address = source;
+      routing_table[routing_elements].cost = linkinfo[link].costperframe;
+      routing_table[routing_elements].prev_address = nodeinfo.address;
+      routing_elements++;
+      
+      /*Create the smaller table to send out so we 
+      arent sending out blank entries*/
+      if(link_elements == nodeinfo.nlinks) {
+        NL_TABLE temp_table[link_elements];
+        int i;
+        for (i = 0; i < link_elements; i++) {
+          NL_TABLE temp_entry;
+          temp_entry.address = link_table[i].address;
+          temp_entry.prev_address = nodeinfo.address;
+          temp_entry.cost = linkinfo[i+1].costperframe;
+          memcpy(&temp_table[i], &temp_entry, sizeof(NL_TABLE));
+        }
+        /*flood the network with our neighbours table*/
+        flood((char *)temp_table, sizeof(temp_table));
+      }
       break;
     }
     
-    /* verify the routing table, if it is ok update your routing table */
-    if (verify((char *)p->msg,p->length,p->smd,p->from_nodename)) {
-      /* routing packet received so process it */
-      printf("\nRouting message received from link %d\n", link);
-      /* just using sign here to stop a warning and get cnet to compile */
-      sign(p->msg, p->length);
-    }
-    else {
-      printf("\nError got a message with the wrong hash from link %d\n",link);
+    case NL_ROUTE: {
+      /* verify the routing table, if it is ok update your routing table */
+      if (verify((char *)p->msg, p->length, p->smd, p->from_nodename)) {
+        /* routing packet received so process it */
+        NL_TABLE* table = (NL_TABLE*) p->msg;
+        printf("Table update received from %s\n", p->from_nodename);
+        
+        /*perfrom dijkstra's*/
+        route_packets(table, p->length / sizeof(NL_TABLE), link);
+        
+        /*continue flooding the network*/
+        forward((NL_PACKET *)packet, link);        
+      }
+      else {
+        printf("\nError got a message with the wrong hash from link %d\n", link);
+      }
+      break;
     }
-    break;  
+    
   }
   return(0);
 }
@@ -270,7 +352,7 @@
 }
 
 /* verifies the message against the signed message digest (SMD), uses the addresses public key */
-static int verify(char* message, int m_len,char* smd, char* address) {
+static int verify(char* message, int m_len, char* smd, char* address) {
   /* create an MD5 hash of your key */
   char* md5 = calloc(1, MD5_DIGEST_LENGTH);
   MD5(message, m_len, md5);
@@ -286,26 +368,124 @@
   /* verify the message against the signed message digest (smd) */
   int value = RSA_verify(NID_md5, md5, MD5_DIGEST_LENGTH, smd, RSA_size(public_key), public_key);
   if(value == 1) {
-    printf("Verification sucessful");
+    /*printf("Verification sucessful\n");*/
     return 1;
   }
   return 0;  
 }
 
-void create_n_send_ping_packet(NL_PING* hello) {
+/*sends out ping messages on the given link (or on all if send_link is 0)*/
+static void send_ping(NL_PING* ping, int send_link, NL_TYPE kind) {
+  /*create the packet with the given parameters*/
   NL_PACKET packet;
 	NL_PACKET *p = &packet;
-	
-  p->ping = 1;
-	p->kind = NL_ROUTE;
-	p->length = sizeof(hello);
+	p->kind = kind;
+  p->length = sizeof(NL_PING);
 	p->hopsleft = 1;
+  strcpy(p->from_nodename, nodeinfo.nodename);
+  memcpy(p->msg, ping, sizeof(NL_PING));
+
+  /*send the packet on the given link based on send_link*/
+  if(send_link == 0) {
+    int link;
+    for(link=1; link<=nodeinfo.nlinks; link++) {
+      CHECK(down_to_datalink(link,(char *)p, PACKET_SIZE(*p)));
+    }
+  }
+  else {
+    CHECK(down_to_datalink(send_link, (char *)p, PACKET_SIZE(*p)));
+  }
+}
+
+/*flood the network with the given segment on all links*/
+static void flood(char* segment, int length) {
+  char* smd;
+  NL_PACKET packet;
+  NL_PACKET *p = &packet;
+  p->kind = NL_ROUTE;
+  p->hopsleft = MAXHOPS;
+  p->length = length;
+  
+  /*sign the segment with our RSA private key*/
+  smd = sign(segment, length);
+  memcpy(p->smd, smd, 64);
+  free(smd);
+
+  memcpy(p->msg, segment, p->length);
   memcpy(p->from_nodename, nodeinfo.nodename, MAX_NODENAME_LEN);
-  memcpy(p->msg, &hello, sizeof(NL_PING));
 
-  int link;
+  /*send on all links*/
+	int link;
   for(link=1; link<=nodeinfo.nlinks; link++) {
-    down_to_datalink(link,(char *)p, PACKET_SIZE(*p));
-    printf("Sending out hello packet\n");
+    CHECK(down_to_datalink(link, (char*) p, sizeof(packet)));
+  }
+}
+
+/*forward the given packet on all links*/
+static void forward(NL_PACKET* packet, int link) {
+  /*decrement maxhops (used for flood control*/
+  if(--packet->hopsleft > 0) {
+    int i;
+    for(i = 0; i < nodeinfo.nlinks; i++) {
+      if(i != link && linkinfo[i].linkup) 
+        CHECK(down_to_datalink(i, (char*) packet, PACKET_SIZE(*packet)));
+    }
   }
 }
+
+/*Dijkstra's algorithm*/
+static void route_packets(NL_TABLE* table, int entries, int link) {
+  int i, j, k, cost, found;
+  /*go through the table ...*/
+  for(i = 0; i < entries; i++) {
+    /*... and if the address is me ... then drop out*/
+    if(table[i].address == nodeinfo.address) 
+      continue;
+    
+    found = -1;
+    /*go through the routing table ...*/
+    for(j = 0; j < routing_elements; j++) {
+      /*... and if the address of the given table is in the routing table*/
+      if(table[i].address == routing_table[j].address) {
+        found = j;
+        break;       
+      }
+    }
+    /*if the address wasnt in the routing table ... */
+    if(found == -1) {
+      /*... then create a new emtry in the table*/
+      NL_TABLE temp_entry;
+      temp_entry.address = table[i].address;
+      temp_entry.prev_address = table[i].prev_address;
+      /*go through the routing table*/
+      for(k = 0; k < routing_elements; k++) {
+        if(temp_entry.prev_address == routing_table[k].address) {
+          /*calculate the new cost from the updated table*/
+          temp_entry.cost = table[i].cost + routing_table[k].cost;
+          break;
+        }
+      }
+      memcpy(&routing_table[routing_elements++], &temp_entry, sizeof(NL_TABLE));
+    }
+    else {
+      for(k = 0; k < routing_elements; k++) {
+        if(table[i].prev_address == routing_table[k].address) {
+          /*calculate the new cost from the updated table*/
+          cost = table[i].cost + routing_table[k].cost;
+          printf("%d = %d + %d\n", cost, table[i].cost, routing_table[k].cost);
+          break;
+        }
+      }
+      if(cost < routing_table[j].cost) {
+        printf("Changing cost from %d to %d for %d, was via %d, now via %d\n", routing_table[found].cost, cost, routing_table[found].address, routing_table[i].prev_address, table[i].prev_address);
+        /*create a new table entry*/
+        NL_TABLE temp_entry;
+        temp_entry.address = routing_table[k].address;
+        temp_entry.prev_address = table[i].prev_address;
+        temp_entry.cost = cost;
+        /*update table*/
+        memcpy(&routing_table[k], &temp_entry, sizeof(NL_TABLE));
+      }
+    }
+  }  
+}
