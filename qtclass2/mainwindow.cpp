#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
}

MainWindow::~MainWindow()
{
    delete ui;
}

 void MainWindow::resetxy()
 {
     this->x=800;
     this->y=600;
     resize(this->x,this->y);
 }
 void MainWindow::showxy() const
 {

    // this->x=10;
    // this->y=100;
     this->z=1000;

 }
