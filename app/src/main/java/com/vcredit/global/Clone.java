package com.vcredit.global;

/**
 * Created by shibenli on 2016/6/8.
 */
public interface Clone<T> {
    /**
     * @param t 输出参数，如果为null会自动创建对象
     * @return 如果入参不为null，返回入参，如果为null会创建新的对象
     */
    public T clone(T t);
}
