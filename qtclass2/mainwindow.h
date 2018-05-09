#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    int x;
    int y;
    mutable  int z;//不受const成员函数的约束

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();
    void resetxy();//没有const属性，可以修改成员变量
     void showxy()const;//const属性，不可以修改一般的成员变量（没有mutable）




private:
    Ui::MainWindow *ui;
};

#endif // MAINWINDOW_H
