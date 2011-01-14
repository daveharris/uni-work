#ifndef XMPPTEST__H
#define XMPPTEST__H

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
#include <qlistview.h> 
#include"xmpp.h"
#include"im.h"
#include <unistd.h>
#include <qbuttongroup.h>
#include<stdlib.h>
#include<time.h>
#include<stdio.h>

#include"ui_test.h"
#include"messagesui.h"
#include"add_contactui.h"
#include"user.h"
#include "ProcessStanza.h"

#define AppName "xmpptest"


class MainDlg : public TestUI
{
  Q_OBJECT
public:
  ProcessStanza pd;
  bool active, connected;
  XMPP::AdvancedConnector *conn;
  QCA::TLS *tls;
  XMPP::QCATLSHandler *tlsHandler;
  XMPP::ClientStream *stream;
  XMPP::Jid jid;
  QListViewItem * contactsList;
  QPixmap online;
  QPixmap offline;

  MainDlg(QWidget *parent, const char *name);
  ~MainDlg();
  XMPP::ClientStream * get_stream();
  void send_message(const char * toJid, const char * message);

  private slots:
  void adjustLayout();
  void print_num(int i);
  void probe_toggled(bool);
  void proxy_activated(int x);
  void cleanup();
  void start();
  void stop();
  void go();
  void send();
  void sc_im();
  void sc_msg();
  void sc_iqv();
  void conn_srvLookup(const QString &server);
  void conn_srvResult(bool b);
  void conn_httpSyncStarted();
  void conn_httpSyncFinished();
  void tls_handshaken();
  void cs_connected();
  void cs_securityLayerActivated(int type);
  void cs_needAuthParams(bool user, bool pass, bool realm);
  void cs_authenticated();
  void cs_connectionClosed();
  void cs_delayedCloseFinished();
  void cs_readyRead();
  void cs_stanzaWritten();
  void cs_warning(int warn);
  void cs_error(int err);
  void open(QListViewItem *qlvi,const QPoint &qp, int col);
  void add(QListViewItem *qlvi,const QPoint &qp, int col);

  void add();

 private:
  void setHostState();
  void appendSysMsg(const QString &s, const QColor &_c);
  void appendSysMsg(const QString &s);
 public:
  void appendLibMsg(const QString &s);
  void appendErrMsg(const QString &s);
  void appendXmlOut(const QString &s);
  void appendXmlIn(const QString &s);


  // These are the functions that you might want to call from ProcessStanza.cpp
  // ask the user a question if they say yes then returns true otherwise returns false.
  bool question(const char * title, const char * quest);

  //adds this JID to the contacts list display
  void addToList(const char * fromJID);
  //removes this JID to the contacts list display
  void removeFromList(const char * fromJID);

  //sends xml to the server
  void send_xml(const char *s);

  // sets this jid to online in the contacts list display (updates the image to be online)
  void setOnline(const char * fromJID);
  // sets this jid to offline in the contacts list display (updates the image to be offline)
  void setOffline(const char * fromJID);
  // check to see if the contacts list display has this jid
  bool listContains(const char * fromJID);

};



#endif



