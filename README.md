# GitTest
demo
test
   jstring  js;
   const char *Str = env->GetStringUTFChars(js, JNI_FALSE);//��java�ַ���תΪC�ַ���
   env->ReleaseStringUTFChars(js,str);//�ͷ�jstring
   jint jIntArr2[3] = {1,2,3};
   int cInt[10]={0};
   //����һ��native��jint����
   jintArray jIntArr = env->NewIntArray(10);
   //��ֵ���鵽c����
   env->SetIntArrayRegion(jIntArr,0,10,cInt);
   //��ֱ��ָ��Ĳ���
   jint *pInt = env->GetIntArrayElements(jIntArr, 0);
	//�Ը����Ĳ���
   env->GetIntArrayRegion(jIntArr,0,10,cInt);
   //�ͷ�����
   env->ReleaseIntArrayElements(jIntArr,pInt,0);
   
   
   (*env)->FindClass(env, "Lpackagename/classname;");
   ��������
	 
	 ����            ����
	boolean	         Z
	byte	         B
	char	         C
	short	         S
	int	             I
	long	         L
	float	         F
	double	         D
	void	         V
	object����	LClassName;      L����;
	Arrays	
	[array-type        [��������
	
	//����һ����������
	jobjectArray jobjectArray = env->NewObjectArray(size, jclazz, NULL);
	
	//������
	public class JavaClass{
		public String instanceFeild = "instance Feild";
		public static String staticFeild = "static Feild";
		public String get(){}
	}
	jfieldID field = env->GetFieldID(clazz,"instanceFeild","Ljava/lang/String;");
	env->GetStaticFieldID(clazz,"instanceFeild","Ljava/lang/String;");
	
	jstring  instance = (jstring) env->GetObjectField(jobj, field);//�õ�java���Ա�ַ���
	//���÷���
	jmethodID methodID = env->GetMethodID(jclazz, "get", "()Ljava/lang/String");
    env->CallObjectMethod(jobj,methodID);
	
	//�����쳣
    jthrowable occurred = env->ExceptionOccurred();
    if (occurred != 0){
        env->ExceptionClear();
        //exeception handler
    }
    //�׳��쳣
    jclass nullClazz = env->FindClass("Ljava/lang/NullPointerExeception;");
    env->ThrowNew(nullClazz,"not null");
	//����ȫ������
    jGlobalClazz = (jclass) env->NewGlobalRef(jclazz);
    //ɾ��ȫ������
    env->DeleteGlobalRef(jGlobalClazz);
	
	
	
	
	
	
	
	
	c�еĿɱ����
	#include    "stdarg.h"   
   void    simple_va_fun(int    start,    ...)     
   {     
           va_list    arg_ptr;     
           int    nArgValue    = start;   
           int    nArgCout=0;            //�ɱ��������Ŀ   
           va_start(arg_ptr,start);    //�Թ̶������ĵ�ַΪ���ȷ����ε��ڴ���ʼ��ַ��   
           do     
           {   
                   ++nArgCout;   
                   printf("the    %d    th    arg:    %d/n",nArgCout,nArgValue);            //�����������ֵ   
                   nArgValue    =    va_arg(arg_ptr,int);                                            //�õ���һ���ɱ������ֵ   
           }    while(nArgValue    !=    -1);    
           va_end��arg_ptr����                              
           return;     
   }   