package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * 深拷贝
 */
@SuppressWarnings({"unchecked", "Duplicates"})
@Slf4j
public class DeepCopyUtil {

    /**
     * List深拷贝 条件:需要model实现Serializable
     */
    public static <T> List<T> deepCopy(List<T> t) {
        try {
            // 写入字节流
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(t);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            return (List<T>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.info("deepCopy failure!! error={}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对象深拷贝 条件:需要model实现Serializable
     */
    public static <T> T deepCopy(T t) {
        try {
            // 写入字节流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(t);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.info("deepCopy failure!! error={}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
