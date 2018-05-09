#define  _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include<string>
class string
{
public:
	char *p;
	int length;
	string(int num,char *str)
	{
		//获取长度，分配内存，拷贝内容
		length = num;
	    p = new char [length];
		memset(p, 0, length);//
		strcpy(p, str);

	}
	string(const string & string1)
	{
		//qian
		//this->p = string1.p;
		//	this->length = string1.length;
		//shen
		this->p = new char[string1.length];
		this->length = string1.length;
		memset(this->p, 0, this->length);//
		strcpy(this->p, string1.p);

	}

	~string()
	{
		delete[] p;//
	}
	


};
void main()
{
	string *pstr1 = new string(10, "hello");
	std::cout <<pstr1->p<< std::endl;
	string *pstr2 = new string(*pstr1);
	delete pstr1;
	std::cout << pstr2->p << std::endl;

	

	std::cin.get();
}

void main1()
{
	string str1(10, "hello");
	std::cout << str1.p << std::endl;

	string str2(str1);
	std::cout << str2.p << std::endl;



	std::cin.get();
}