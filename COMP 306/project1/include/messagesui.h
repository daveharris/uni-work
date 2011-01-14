#ifndef MESSAGESUI__H
#define MESSAGESUI__H

#include<qapplication.h>
#include<qtextedit.h>
#include<qgroupbox.h>
#include<qlineedit.h>
#include<qlabel.h>
#include<qcheckbox.h>
#include<qtextedit.h>
#include<qcombobox.h>
#include<qpushbutton.h>
#include<qmessagebox.h>
#include<qinputdialog.h>
#include<qspinbox.h>
#include<qtimer.h>
#include<qmenubar.h>
#include<qpopupmenu.h>
#include<qtabwidget.h>
#include<qca.h>
#include "messages.h"
#include"xmpp.h"
#include<stdlib.h>
#include<time.h>
#include"ui_test.h"

#include<stdio.h>

class MessageDialog : public MessagesUI
{
  Q_OBJECT
private slots:
  void adjustLayout();
  void send_message();
 private:
  bool online;
  XMPP::Jid jid;
  QObject *display;
  void updateCaption();
  void append(const char * q);

public:
  void focus();
  
  // These are the functions you should be calling from ProcessStanza.cpp
  // Note: there are also two useful functions inherited from MessagesUI these are:
  // isVisible() - returns true if the window is open else false
  // show() - makes the window visible.

  // Constructor - the name should be the contact's JID
  MessageDialog(QWidget *parent, const char *name);

  // set the contact's JID (note this is not your JID it is your contact's)
  void setContactsJID(const char *_jid); 

  // get your contacts JID
  const char * getContactsJID();
  //get your contacts username
  const char * getContactsUsername();

  // update the window - tell it whether the contact is online or not.
  void updateOnline(bool x);

  // Called by ProcessStanza when a message Stanza arrives.
  void recieved_message(XMPP::Stanza s) ;


};



#endif
