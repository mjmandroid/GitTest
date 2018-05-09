#include "mainwindow.h"
#include <QApplication>
#include<QDebug>

//被友元
class window
{
   MainWindow *p;

   void settitle()
   {
       this->p->setWindowTitle("1234");
   }
   friend class opwindow;//友元类


};


class opwindow
{

private:
    window pwin;//类的变量,指针可以访问类的所有私有成员与函数
    window *ppwin;//指针必须初始化，必须分配内存
public:
    void  init()
    {

       // pwin.p=new MainWindow;
       // pwin.p->show();
        ppwin= new window;//不初始化就是野指针
        ppwin->p=new MainWindow();
        ppwin->p->show();

        //setstr();
    }
    void  setstr()
    {
       // qDebug()<<pwin;
       // pwin.settitle();
       ppwin->settitle();

    }





};







int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    opwindow opwindow1;
    opwindow1.init();
    opwindow1.setstr();//语法


    return a.exec();
}
