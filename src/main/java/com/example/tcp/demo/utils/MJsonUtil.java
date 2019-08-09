package com.example.tcp.demo.utils;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author:wanzhongsu
 * @description: jack-json 常用工具类，该类不能新添加方法，如有需求请问作者，
 * 引用时候变量名称必须为mJsonUtil，使用@Resource标签引用
 * @date:2018/9/3_11:31
 */
@Component("mJsonUtil")
public class MJsonUtil {

    /**
     * @author:wanzhongsu
     * @description: 字符串转对象，注意不能转集合对象
     * @date:2018/9/3_11:33
     * @param: json字符串
     * @param: class对象
     * @return: 返回泛型对象
     */
    public <T> T jsonStrToObject(String jsonStr, Class<?> cls) {
        Gson gson = new Gson();
        Object object = null;
        object = gson.fromJson(jsonStr, cls);
        return (T) object;
    }


    /**
     * @author:wanzhongsu
     * @description: 对象变为json字符串
     * @date:2018/9/3_11:37
     * @param: 对象，注意不能是集合对象
     * @return: json字符串
     */
    public String objectToJsonStr(Object c) {
        String jsonStr = "";
        Gson gson = new Gson();
        jsonStr = gson.toJson(c);
        return jsonStr;
    }

    /**
     * @author:wanzhongsu
     * @description: list集合对象转json字符串
     * @date:2018/9/3_11:45
     * @param: list对象
     * @return: json字符串
     */
    public String listToJsonStr(List<?> list) {
        String jsonStr = "";
        Gson gson = new Gson();
        jsonStr = gson.toJson(list);
        return jsonStr;
    }


    /**
     * @author:wanzhongsu
     * @description: json字符串转List集合对象
     * @date:2018/9/3_11:41
     * @param: json字符串
     * @param: list集合类的类型
     * @return: List泛型对象
     */
    public <T> List<T> jsonStrToList(String jsonStr, Class<T> cls) {
        Gson gson = new Gson();
        return (List<T>) gson.fromJson(jsonStr, new ParameterizedTypeImpl(cls));
    }


    /**
     * @author:wanzhongsu
     * @description: 字符串转义json字符串
     * @date:2018/9/3_11:46
     * @param: 字符串
     * @return: json字符串
     */
    public String stringToJson(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }


    /**
     * @author:wanzhongsu
     * @description: 声明集合类的实列类型
     * @date:2019/4/30 12:54
     */
    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clazz) {
            this.clazz = clazz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{this.clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return MJsonUtil.class;
        }
    }
}
