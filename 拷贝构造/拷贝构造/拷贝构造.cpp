#include<iostream>


//��������Ѿ����壬�㲻������
class classA
{
private:
	int a;
	int b;

public:
	
	//��������Ĺ���
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
	classA  class1(10,100);//��������Ĭ������Ĭ�ϵĹ��캯��
	classA  class2(class1);//������������Ĭ�ϵĿ������캯��
	class1.print();
	class2.print();//Ĭ�ϵĿ������캯��
	//classA class3(4);


	std::cin.get();
}
