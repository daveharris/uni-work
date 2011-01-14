#ifndef ADD_CONTACTUI__H
#define ADD_CONTACTUI__H

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
#include"xmpp.h"
#include<stdlib.h>
#include<time.h>
#include"ui_test.h"

#include"add_contact.h"

#include<stdio.h>

class Add_Contacts : public Add_Contact
{
  Q_OBJECT
    public:
  Add_Contacts(QWidget *parent, const char *name);
  void adjustLayout();
  QString get_new_jid();

 private:
  QString new_jid;
  QWidget *parent;
private slots:
  void add_contact();
};


#endif
