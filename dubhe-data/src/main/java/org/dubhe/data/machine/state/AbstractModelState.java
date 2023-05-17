

package org.dubhe.data.machine.state;

public abstract class AbstractModelState {

    public void startModel(Long primaryKeyId){
    }

    public void startModelFinish(Long primaryKeyId){
    }

    public void startModelFail(Long primaryKeyId){
    }

    public void stopModel(Long primaryKeyId){
    }

    public void stopModelFinish(Long primaryKeyId){
    }
}
