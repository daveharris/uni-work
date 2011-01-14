#include "ProcessStanza.h"
#include "display.h"

#define stringSize 2048

ProcessStanza::ProcessStanza(){ 
}
ProcessStanza::ProcessStanza(QWidget *parent = 0, const char *_jid = NULL) {
	display = parent;
	jid = _jid;
	message_dialog = NULL;
	message_dialog_len =0;
}

ProcessStanza::~ProcessStanza() {
	for (int i = 0; i < message_dialog_len; i++) {
		delete message_dialog[i];
	}
}


// stop - this is called when the client is disconnected
// it should delete all of the message_dialogs
void ProcessStanza::stop() {
	for (int i = 0; i < message_dialog_len; i++) {
		delete message_dialog[i];
	}
	message_dialog_len = 0;
	message_dialog = NULL;
}

// process stanza - this is one of the most important functions.
// This function looks at what kind of stanza was just recieved.
// and sends the stanza to the function that deals with that kind of stanza.
// There are three kinds of stanza message, iq, and presence. 
void ProcessStanza::process_stanza(XMPP::Stanza s, const char* fromJID) {
	// if the stanza is a message
	if (strcmp(s.kindString(),"message") == 0) {
		process_message(s,fromJID);
	}
	// if the stanza is iq information
	else if (strcmp(s.kindString(),"iq") == 0) {
		process_iq(s,fromJID);
	}
	//if the stanza is presence information
	else if (strcmp(s.kindString(),"presence") == 0) {
		process_presence(s,fromJID);
	}
}

// process_message - this function deals with message stanzas.
// it deals with incoming messages, and displays it in the correct window
void ProcessStanza::process_message(XMPP::Stanza s, const char* fromJID) {
	// if the message has a '<body>' see if the message window already exists.
	char* body = new char[stringSize];
	strcpy(body, s.toString());
	strstr(body, "<body>");
	if (body != NULL) {
		// look through the windows to find the right one.
		// if the user says so make it visible.
		// add message to window
		int pos = find(fromJID);

		//if the there is no window, then open one
		if(pos == -1) {
			pos = open(fromJID);
			message_dialog[pos]->show();
		}
		
		//if the window isnt visible, ask user if
		//they want to chat, and if so, show
		if (!message_dialog[pos]->isVisible()) {
			char* question = new char[stringSize];
			strcpy(question, "Would you like to chat with ");
			strcat(question, fromJID);
			char* title = new char[stringSize];
			strcpy(title, jid);
			strcat(title, " is online");
			bool result = ((MainDlg *)(display))->question(title, question);
			if (result) {
				message_dialog[pos]->show();
			}
			message_dialog[pos]->updateOnline(true);
			
			//free memory
			delete question;
			delete title;
		}
		//display mesage to window
		message_dialog[pos]->recieved_message(s);
	}
}


// process_iq - this function processes iq stanzas.
// if there is an id then we need to check for two special id's.
// These are sess_2 and roster_1 these stanza's are responses to
// Stanza's already sent.
void ProcessStanza::process_iq(XMPP::Stanza s, const char* fromJID) {
	// if there is a an id
	if (s.id() != NULL) {
		if (strcmp(s.id(), "sess_2") == 0) {
			// send an iq get request to get your roster (contact list)
			char* rosterMsg = new char[stringSize];
			strcpy(rosterMsg, "<iq from='");
			strcat(rosterMsg, jid);
			strcat(rosterMsg, "' type='get' id='roster_1'><query xmlns='jabber:iq:roster'/></iq>");
			((MainDlg*)(display))->send_xml(rosterMsg);
			
			//free memory
			delete rosterMsg;

			// then send a presence message showing that you are online.
			char* presenceMsg = "<presence><show/></presence>";
			((MainDlg*)(display))->send_xml(presenceMsg);
		}
		// if it is the response to my get request, pocess the response.
		else if (strcmp(s.id(), "roster_1") == 0){
			int count = s.getElementsCount("item");
			//Go through all the contacts
			for(int i=0; i<count; i++) {
				const char *itemLine = s.getElements("item")[i];
				char* type = strstr(itemLine, "subscription=");
				if (type != NULL) {
					strtok(type, "\"");
					type = strtok(0, "\"");
					// if I can see when this person is online (eg subscription_type is both, to, or none) then add to list
					if (strcmp(type, "both") == 0 || strcmp(type, "to") == 0 || strcmp(type, "none") == 0) {
						char* newJid = strstr(itemLine, "jid=");
						newJid = strtok(newJid, "\"");
						newJid = strtok(0, "\"");
						//Append the contact to the list
						((MainDlg *)(display))->addToList(newJid);
					}
				}
			}
		}
	}
}


// process_presence - this function processess presence information
// deals with contacts coming on and offline and also with new users being added to your contact list
void ProcessStanza::process_presence(XMPP::Stanza s, const char* fromJID) {
	int pos = find(fromJID);
	// if the person has just become available
	if (s.type() == NULL) {
		//add contact to list and update status
		((MainDlg *)(display))->addToList(fromJID);
		((MainDlg *)(display))->setOnline(fromJID);

		//then ask if they want to chat with the contact
		char* question = new char[stringSize];
		strcpy(question, "Would you like to chat with ");
		strcat(question, fromJID);
		char* title = new char[stringSize];
		strcpy(title, fromJID);
		strcat(title, " is online");

		if (pos == -1)
			pos = open(fromJID);				
		
		//if the window isnt visible and they say yes
		bool result = (!(message_dialog[pos]->isVisible()) && (((MainDlg *)(display))->question(title, question)));
		if (result) {
			//then show the window
			message_dialog[pos]->show();
		}
		//and update status
		message_dialog[pos]->updateOnline(true);
	}
	else {
		//if the contact has gone offline
		if (strcmp(s.type(), "unavailable") == 0) {
			// then change the title bar to say unavailable and update status
			((MainDlg *)(display))->setOffline(fromJID);
			message_dialog[pos]->updateOnline(false);
		}
		//if the contact is 'subscribing' to us
		else if (strcmp(s.type(), "subscribe") == 0) {;
			char* question = new char[stringSize];
			strcpy(question, fromJID);
			strcat(question, " has requested to add you to their contact list.\n");
			strcat(question, "Do you want to allow them to see when you are online?");
			bool result = ((MainDlg *)(display))->question("Add contact requested", question);
			
			char* presence = new char[stringSize];
			strcpy(presence, "<presence to='");
			strcat(presence, fromJID);
			
			// if the user says yes reply with a subscribed message otherwise reply with unsubscribed
			if (result) {
				strcat(presence, "' type='subscribed'/>");

				strcpy(question, fromJID);
				strcat(question,  " has just added you to their list\nDo you want to add them them to your list?");
				result = (!(((MainDlg *)(display))->listContains(fromJID)) &&
					(((MainDlg *)(display))->question("Add contact request", question)));
				// if someone just added us to their list, ask if we want to be on theirs
				if (result) {
					//add to the display and contact list
					((MainDlg *)(display))->addToList(fromJID);
					add(fromJID);
				}
			}
			else
				strcat(presence, "' type='unsubscribed'/>");

			//send the presence
			((MainDlg *)(display))->send_xml(presence);
		}
	}	
}



// set the jid (Jabber id).
void ProcessStanza::setJid(const char* _jid) {
	jid = _jid;
}


//**************************************************************************************

// Sends the message to toJid in the appropriate xml format.
// format example:
// <message from='bob@greta-pt.mcs.vuw.ac.nz' 
//	 type='chat' to='sam@greta-pt.mcs.vuw.ac.nz'>
//	 <body>Hi Sam.</body>
// </message>
void ProcessStanza::send_message(const char* toJid, const char* message) {
	int len = strlen(message);
	len += strlen(toJid);
	char st[len + 128];
	strcat(st,"<message from='");
	strcat(st,jid);
	strcat(st, "' type='chat' to='");
	strcat(st, toJid);
	strcat(st, "'>\n<body>");
	strcat(st,message);
	strcat(st,"</body>\n</message>\n");
	((MainDlg *)(display))->send_xml(st);
}


// Sends the xml requesting the JID to allow them to see when they
// are online.
// format example:
// <presence to='sam@greta-pt.mcs.vuw.ac' type='subscribe' />
void ProcessStanza::add(const char* user) {
	char st[128] = "<presence to='";
	strcat(st, user);
	strcat(st, "' type='subscribe' />\n");
	((MainDlg *)(display))->send_xml(st);
}


// Sends the xml requesting the server to remove the JID from their
// constacts list and then sends presence infromation to the user
// saying that they are unavailable
// format example 1:
// <iq type='set'>
//	<query xmlns='jabber:iq:roster'>
//		<item subscription='remove' jid='sam@greta-pt.mcs.vuw.ac.nz' />
//	</query>
// </iq>
// format example 2:
// <presence to='sam@greta-pt.mcs.vuw.ac.nz' type='unavailable' from='bob@greta-pt.mcs.vuw.ac.nz' />
void ProcessStanza::delete_user(const char* user) {

}


// open - this function handles the creatation of message_dialogs
// it needs to allocate space as required and keep message_dialog and message_dialog_len consistent
int ProcessStanza::open(const char* fromJID) {
	if (message_dialog == NULL) {
		message_dialog = (MessageDialog**)malloc(sizeof(MessageDialog*)*(message_dialog_len+1));
	} 
	else {
		for (int i = 0; i < message_dialog_len; i++) {
			if (strcmp(message_dialog[i]->getContactsJID(),fromJID) == 0) {
				return i;
			}
		}
		message_dialog = (MessageDialog**)realloc(message_dialog,sizeof(MessageDialog*)*(message_dialog_len+1));
	}
	message_dialog[message_dialog_len] = new MessageDialog((MainDlg *)display,fromJID);
	message_dialog_len++;
	return message_dialog_len-1;
}

// opens the dialog based on the JID passed in and makes it visible
void ProcessStanza::open_n_show(const char* fromJID, bool online) {
	int found = open(fromJID);
	message_dialog[found]->show();
	message_dialog[found]->updateOnline(online);
}
// close - this function handles the deletion of a message_dialog
// it needs to rellocate space as required and keep message_dialog and message_dialog_len consistent
void ProcessStanza::close(const char* fromJID) {
	if (message_dialog == NULL) {
		return;
	} 
	else {
		int found = 0;
		for (int i = 0; i < message_dialog_len; i++) {
			if (strcmp(message_dialog[i]->getContactsJID(),fromJID) == 0) {
				delete(message_dialog[i]);
				found = 1;
			}
			else if (found) {
				message_dialog[i-1] = message_dialog[i];
			}		
		}
		if (found) {
			message_dialog_len--;
			if (message_dialog_len == 0) {
				message_dialog = NULL;
			}
			else {
				message_dialog = (MessageDialog**)realloc(message_dialog,
					sizeof(MessageDialog*)*(message_dialog_len));
			}
		}
	}
	return;
}

// finds the message_dialog that matches the JID passed in and
// returns it's position in the array. If the Jid is ont found returns -1.
int ProcessStanza::find(const char* fromJID) {
	for (int i = 0; i < message_dialog_len; i++) {
		if (strcmp((const char*)message_dialog[i]->getContactsJID(),fromJID) == 0) {
			return i;
		}
	}
	return -1; 
}
