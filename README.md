# GitTest
demo
test
   jstring  js;
   const char *Str = env->GetStringUTFChars(js, JNI_FALSE);//将java字符串转为C字符串
   env->ReleaseStringUTFChars(js,str);//释放jstring
   jint jIntArr2[3] = {1,2,3};
   int cInt[10]={0};
   //创建一个native的jint数组
   jintArray jIntArr = env->NewIntArray(10);
   //赋值数组到c数组
   env->SetIntArrayRegion(jIntArr,0,10,cInt);
   //对直接指针的操作
   jint *pInt = env->GetIntArrayElements(jIntArr, 0);
	//对副本的操作
   env->GetIntArrayRegion(jIntArr,0,10,cInt);
   //释放引用
   env->ReleaseIntArrayElements(jIntArr,pInt,0);
   
   
   (*env)->FindClass(env, "Lpackagename/classname;");
   参数解释
	 
	 类型            符号
	boolean	         Z
	byte	         B
	char	         C
	short	         S
	int	             I
	long	         L
	float	         F
	double	         D
	void	         V
	object对象	LClassName;      L类名;
	Arrays	
	[array-type        [数组类型
	
	//创建一个对象数组
	jobjectArray jobjectArray = env->NewObjectArray(size, jclazz, NULL);
	
	//访问域
	public class JavaClass{
		public String instanceFeild = "instance Feild";
		public static String staticFeild = "static Feild";
		public String get(){}
	}
	jfieldID field = env->GetFieldID(clazz,"instanceFeild","Ljava/lang/String;");
	env->GetStaticFieldID(clazz,"instanceFeild","Ljava/lang/String;");
	
	jstring  instance = (jstring) env->GetObjectField(jobj, field);//得到java类成员字符串
	//调用方法
	jmethodID methodID = env->GetMethodID(jclazz, "get", "()Ljava/lang/String");
    env->CallObjectMethod(jobj,methodID);
	
	//捕获异常
    jthrowable occurred = env->ExceptionOccurred();
    if (occurred != 0){
        env->ExceptionClear();
        //exeception handler
    }
    //抛出异常
    jclass nullClazz = env->FindClass("Ljava/lang/NullPointerExeception;");
    env->ThrowNew(nullClazz,"not null");
	//创建全局引用
    jGlobalClazz = (jclass) env->NewGlobalRef(jclazz);
    //删除全局引用
    env->DeleteGlobalRef(jGlobalClazz);
	
	
	
	
	
	
	
	
	c中的可变参数
	#include    "stdarg.h"   
   void    simple_va_fun(int    start,    ...)     
   {     
           va_list    arg_ptr;     
           int    nArgValue    = start;   
           int    nArgCout=0;            //可变参数的数目   
           va_start(arg_ptr,start);    //以固定参数的地址为起点确定变参的内存起始地址。   
           do     
           {   
                   ++nArgCout;   
                   printf("the    %d    th    arg:    %d/n",nArgCout,nArgValue);            //输出各参数的值   
                   nArgValue    =    va_arg(arg_ptr,int);                                            //得到下一个可变参数的值   
           }    while(nArgValue    !=    -1);    
           va_end（arg_ptr）；                              
           return;     
   }   