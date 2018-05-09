
//delete可以禁用默认生成的函数，禁用构造可以无法实例化
//禁用拷贝构造，可以实现禁止别人拷贝你
//default默认存在
class myclassA
{
public:
	//myclassA::myclassA(void)”: 尝试引用已删除的函数
	//  myclassA() = delete;//默认删除构造函数,无法实例化
	//myclassA() = default;//默认存在
	//myclassA(const 	myclassA &) = delete;//拷贝构造函数
	//myclassA(const 	myclassA &) = default;
	//=
	~myclassA();
};

void main211()
{
	//myclassA myclassa1;
	//myclassA myclassa2(myclassa1);
	//myclassA myclassa3 = myclassa1;//重载了=，根据类型
//	myclassA a1;


}