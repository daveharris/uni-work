/****************************************************************************
** Form interface generated from reading ui file 'messages.ui'
**
** Created: Thu Jul 14 11:46:27 2005
**      by: The User Interface Compiler ($Id: qt/main.cpp   3.3.4   edited Nov 24 2003 $)
**
** WARNING! All changes made in this file will be lost!
****************************************************************************/

#ifndef FORM8_H
#define FORM8_H

#include <qvariant.h>
#include <qdialog.h>

class QVBoxLayout;
class QHBoxLayout;
class QGridLayout;
class QSpacerItem;
class QPushButton;
class QTextEdit;

class MessagesUI : public QDialog
{
    Q_OBJECT

public:
    MessagesUI( QWidget* parent = 0, const char* name = 0, bool modal = FALSE, WFlags fl = 0 );
    ~MessagesUI();

    QPushButton* pb_message;
    QTextEdit* te_message;
    QTextEdit* history;

protected:

protected slots:
    virtual void languageChange();

};

#endif // FORM8_H
