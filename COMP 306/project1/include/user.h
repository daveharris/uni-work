#include<qpushbutton.h>
#include<qtimer.h>
#include<qlineedit.h>
#include<qstring.h>
#include<qlabel.h>
#include<qmessagebox.h>

#include"register_new_user.h"
#include"RegisterUser.h"

class User : Form1{
  Q_OBJECT
public:
  User(QWidget *parent, const char *name); // : MessagesUI(parent, name);
  
 private:
  int sock_fd;

private slots:
  void validate(const QString &update);
  void register_user();


};
