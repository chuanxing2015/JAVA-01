package jvm.study;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloClassLoader extends  ClassLoader {


    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class clazz = new HelloClassLoader().findClass("Hello");
        Object object = clazz.newInstance();
        Method method = clazz.getMethod("hello");
        method.invoke(object);
    }

    /*
        加载hello.xlass文件，并执行hello方法，Hello.xlass是有Hello.class的所有字节x =255-x形成
     */
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        InputStream stream = HelloClassLoader.class.getClassLoader().getResourceAsStream("Hello.xlass");

        byte[] bytes = new byte[0];
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int var5;
            while((var5 = stream.read(buff)) != -1) {
                byteArrayOutputStream.write(buff, 0, var5);
            }
            byteArrayOutputStream.close();
            bytes = byteArrayOutputStream.toByteArray();
            for(int i = 0 ; i< bytes.length ; i++){
                bytes[i] = (byte) (255 - bytes[i]);
            }
        } catch (Exception e) {
            System.out.println("read file failed : " + e);
        }
        return super.defineClass(name,bytes,0,bytes.length);
    }
}
