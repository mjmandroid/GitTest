#include<iostream>


//如果声明已经定义，便不会生成
class classA
{
private:
	int a;
	int b;

public:
	
	//拷贝构造的规则
	classA(int x, int y)//:a(x), b(y)
	{
		//a = x;
		//b = y;

	}
	void print()
	{
		std::cout <<a << b << std::endl;
	}


};



void main12313()
{
	classA  class1(10,100);//编译器会默认生成默认的构造函数
	classA  class2(class1);//编译器会生成默认的拷贝构造函数
	class1.print();
	class2.print();//默认的拷贝构造函数
	//classA class3(4);


	std::cin.get();
}
