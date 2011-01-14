#ifndef PROCESS_DATA__H
#define PROCESS_DATA__H

#include"xmpp.h"
#include <unistd.h>
#include<stdlib.h>
#include<time.h>

#include"ui_test.h"
#include"messagesui.h"
#include"add_contactui.h"

class ProcessStanza {
 public:
  ProcessStanza();

  ProcessStanza(QWidget *parent,const char *_jid);
  ~ProcessStanza();
  void process_stanza(XMPP::Stanza s,const char *fromJID);
  void process_message(XMPP::Stanza s,const char *fromJID);
  void process_iq(XMPP::Stanza s,const char *fromJID);
  void process_presence(XMPP::Stanza s,const char *fromJID);


  void setJid(const char * _jid);
  int open(const char * fromJID);
  void open_n_show(const char * fromJID, bool online);
  void close(const char * fromJID);

  int find(const char *fromJID);
  void send_message(const char * toJid,const char *message);
  void add(const char *);
  void stop();
  void delete_user(const char * user);


 private:
  QObject *display;
  const char *jid;
  MessageDialog **message_dialog;
  int message_dialog_len;

};



#endif
  
