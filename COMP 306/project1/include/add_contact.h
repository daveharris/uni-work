/****************************************************************************
** Form interface generated from reading ui file 'add_contact.ui'
**
** Created: Thu Jul 14 11:46:27 2005
**      by: The User Interface Compiler ($Id: qt/main.cpp   3.3.4   edited Nov 24 2003 $)
**
** WARNING! All changes made in this file will be lost!
****************************************************************************/

#ifndef ADD_CONTACT_H
#define ADD_CONTACT_H

#include <qvariant.h>
#include <qdialog.h>

class QVBoxLayout;
class QHBoxLayout;
class QGridLayout;
class QSpacerItem;
class QLabel;
class QPushButton;
class QLineEdit;

class Add_Contact : public QDialog
{
    Q_OBJECT

public:
    Add_Contact( QWidget* parent = 0, const char* name = 0, bool modal = FALSE, WFlags fl = 0 );
    ~Add_Contact();

    QLabel* textLabel2;
    QLabel* textLabel1;
    QPushButton* send;
    QLineEdit* le_new_jid;

protected:

protected slots:
    virtual void languageChange();

};

#endif // ADD_CONTACT_H
