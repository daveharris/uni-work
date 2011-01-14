/****************************************************************************
** Form interface generated from reading ui file 'ui_test.ui'
**
** Created: Thu Jul 14 11:46:27 2005
**      by: The User Interface Compiler ($Id: qt/main.cpp   3.3.4   edited Nov 24 2003 $)
**
** WARNING! All changes made in this file will be lost!
****************************************************************************/

#ifndef TESTUI_H
#define TESTUI_H

#include <qvariant.h>
#include <qmainwindow.h>

class QVBoxLayout;
class QHBoxLayout;
class QGridLayout;
class QSpacerItem;
class QAction;
class QActionGroup;
class QToolBar;
class QPopupMenu;
class QTabWidget;
class QWidget;
class QGroupBox;
class QLabel;
class QLineEdit;
class QCheckBox;
class QComboBox;
class QPushButton;
class QListView;
class QListViewItem;
class QTextEdit;

class TestUI : public QMainWindow
{
    Q_OBJECT

public:
    TestUI( QWidget* parent = 0, const char* name = 0, WFlags fl = WType_TopLevel );
    ~TestUI();

    QTabWidget* tb_main;
    QWidget* tab;
    QGroupBox* gb_server;
    QLabel* textLabel5_2;
    QLineEdit* le_user;
    QLabel* textLabel7_2;
    QLineEdit* le_pass;
    QLineEdit* le_jid;
    QGroupBox* gb_proxy;
    QLabel* textLabel2_2;
    QLineEdit* le_proxyhost;
    QLabel* textLabel5;
    QLineEdit* le_proxyuser;
    QLabel* textLabel7;
    QLineEdit* le_proxypass;
    QLabel* lb_proxyurl;
    QLineEdit* le_proxyurl;
    QCheckBox* ck_ssl;
    QComboBox* cb_proxy;
    QLabel* lb_host;
    QLabel* textLabel4;
    QLineEdit* le_host;
    QPushButton* pb_go;
    QPushButton* pb_about;
    QGroupBox* groupBox3;
    QLabel* textLabel8;
    QLineEdit* le_to;
    QPushButton* pb_im;
    QPushButton* pb_msg;
    QPushButton* pb_iqv;
    QWidget* tab_2;
    QListView* listView1;
    QTextEdit* te_log;
    QTextEdit* te_input;
    QPushButton* pb_send;
    QMenuBar *MenuBarEditor;
    QPopupMenu *Menu;
    QAction* contactsAdd_ContactAction;
    QAction* contactsAction;
    QAction* contactsDelete_ContactAction;

protected:
    QHBoxLayout* TestUILayout;
    QVBoxLayout* tabLayout;
    QSpacerItem* spacer3_2;
    QHBoxLayout* layout12;
    QGridLayout* gb_proxyLayout;
    QGridLayout* layout2;
    QHBoxLayout* layout10;
    QSpacerItem* spacer5;
    QVBoxLayout* groupBox3Layout;
    QHBoxLayout* layout8;
    QHBoxLayout* layout9;
    QVBoxLayout* layout8_2;
    QHBoxLayout* layout7;
    QSpacerItem* spacer3;

protected slots:
    virtual void languageChange();

};

#endif // TESTUI_H
