package com.vcredit.global;

/**
 * Created by shibenli on 2016/4/5.
 */
public interface UpdateableEditable extends Updateable{

    /**
     * 更新编辑状态
     * @param editable
     */
    public void updateEditable(boolean editable);
}
