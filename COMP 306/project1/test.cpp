#include <iostream>
#include <string>

using namespace std;

int main() {
	char *reply = new char[768];
	//reply = "<iq from=\"aq@greta-pt.mcs.vuw.ac.nz@greta-pt.mcs.vuw.ac.nz\" type=\"result\" id=\"roster_1\" ><query xmlns=\"jabber:iq:roster\"><item ask=\"subscribe\" subscription=\"none\" jid=\"dave2@greta-pt.mcs.vuw.ac.nz\" /><item subscription=\"to\" jid=\"harrisdavi3@greta-pt.mcs.vuw.ac.nz\" /><item ask=\"subscribe\" subscription=\"none\" jid=\"dave1@greta-pt.mcs.vuw.ac.nz\" /><item ask=\"subscribe\" subscription=\"none\" jid=\"tu@greta-pt.mcs.vuw.ac.nz\" /><item ask=\"subscribe\" subscription=\"none\" jid=\"a@greta-pt.mcs.vuw.ac.nz\" /><item subscription=\"to\" jid=\"sam@greta-pt.mcs.vuw.ac.nz\" /></query></iq>";
		
	reply = "item ask=\"subscribe\" subscription=\"none\" jid=\"dave2@greta-pt.mcs.vuw.ac.nz\"";
	char *type = strstr(reply, "subscription=");
	if (type != NULL) {
		strtok(type, "\"");
		type = strtok(0, "\"");
		printf("Subscription type:\n%s\n", type);
		// if I can see when this person is online (eg subscription_type is both, to, or none) then add to list
		/*if (strcmp(type, "both") == 0 || strcmp(type, "to") == 0 || strcmp(type, "none") == 0) {
			const char *contact = s.getElements("item")[i];
			char *jid = strstr(contact, "jid=");
			jid = strtok(jid, "\"");
			jid = strtok(0, "\"");
			((MainDlg *)(display))->addToList(jid);
		}*/
	}
	delete reply;
}
