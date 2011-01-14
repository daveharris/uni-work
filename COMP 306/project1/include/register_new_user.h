/****************************************************************************
** Form interface generated from reading ui file 'register_new_user.ui'
**
** Created: Thu Jul 14 11:46:27 2005
**      by: The User Interface Compiler ($Id: qt/main.cpp   3.3.4   edited Nov 24 2003 $)
**
** WARNING! All changes made in this file will be lost!
****************************************************************************/

#ifndef FORM1_H
#define FORM1_H

#include <qvariant.h>
#include <qdialog.h>

class QVBoxLayout;
class QHBoxLayout;
class QGridLayout;
class QSpacerItem;
class QLabel;
class QLineEdit;
class QPushButton;

class Form1 : public QDialog
{
    Q_OBJECT

public:
    Form1( QWidget* parent = 0, const char* name = 0, bool modal = FALSE, WFlags fl = 0 );
    ~Form1();

    QLabel* textLabel1;
    QLabel* textLabel2;
    QLabel* textLabel3_2;
    QLabel* textLabel3;
    QLabel* tl_details;
    QLineEdit* te_server;
    QLineEdit* te_username;
    QLineEdit* te_password;
    QLineEdit* te_password_2;
    QPushButton* pb_add;
    QPushButton* pb_cancel;

protected:

protected slots:
    virtual void languageChange();

};

#endif // FORM1_H
