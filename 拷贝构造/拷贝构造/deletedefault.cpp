
//delete���Խ���Ĭ�����ɵĺ��������ù�������޷�ʵ����
//���ÿ������죬����ʵ�ֽ�ֹ���˿�����
//defaultĬ�ϴ���
class myclassA
{
public:
	//myclassA::myclassA(void)��: ����������ɾ���ĺ���
	//  myclassA() = delete;//Ĭ��ɾ�����캯��,�޷�ʵ����
	//myclassA() = default;//Ĭ�ϴ���
	//myclassA(const 	myclassA &) = delete;//�������캯��
	//myclassA(const 	myclassA &) = default;
	//=
	~myclassA();
};

void main211()
{
	//myclassA myclassa1;
	//myclassA myclassa2(myclassa1);
	//myclassA myclassa3 = myclassa1;//������=����������
//	myclassA a1;


}