
package org.dubhe.data.machine.utils.identify.setting;

import cn.hutool.core.collection.CollectionUtil;
import org.dubhe.biz.base.constant.NumberConstant;
import org.dubhe.data.machine.constant.FileStateCodeConstant;
import org.dubhe.data.machine.enums.DataStateEnum;

import java.util.HashSet;
import java.util.List;

/**
 * @description 状态判断类
 */
public class StateSelect {

    /**
     * 未采样
     *
     * @param stateList 数据集下文件状态的并集
     * @return 数据集状态枚举
     */
    public static DataStateEnum isInit(List<Integer> stateList) {
        if (stateList.size() == NumberConstant.NUMBER_1 && stateList.contains(FileStateCodeConstant.NOT_ANNOTATION_FILE_STATE)) {
            return DataStateEnum.NOT_ANNOTATION_STATE;
        }
        return null;
    }

    /**
     * 手动标注中
     *
     * @param stateList 数据集下文件状态的并集
     * @return 数据集状态枚举
     */
    public static DataStateEnum isManualAnnotating(List<Integer> stateList) {
        if (stateList.size() > 1 && stateList.contains(FileStateCodeConstant.NOT_ANNOTATION_FILE_STATE)) {
            return DataStateEnum.MANUAL_ANNOTATION_STATE;
        }
        return stateList.contains(FileStateCodeConstant.MANUAL_ANNOTATION_FILE_STATE) ? DataStateEnum.MANUAL_ANNOTATION_STATE : null;
    }

    /**
     * 自动标注完成
     * 自动标注完成或者标注未识别两个一定至少包含其中之一，
     *
     * @param stateList 数据集下文件状态的并集
     * @return 数据集状态枚举
     */
    public static DataStateEnum isAutoFinished(List<Integer> stateList) {
        HashSet<Integer> states = new HashSet<Integer>() {{
            add(FileStateCodeConstant.AUTO_TAG_COMPLETE_FILE_STATE);
            add(FileStateCodeConstant.ANNOTATION_NOT_DISTINGUISH_FILE_STATE);
            add(FileStateCodeConstant.ANNOTATION_COMPLETE_FILE_STATE);
            add(FileStateCodeConstant.TARGET_COMPLETE_FILE_STATE);
        }};

        if (stateList.contains(FileStateCodeConstant.AUTO_TAG_COMPLETE_FILE_STATE) || stateList.contains(FileStateCodeConstant.ANNOTATION_NOT_DISTINGUISH_FILE_STATE)) {
            stateList.removeAll(states);
            if (CollectionUtil.isEmpty(stateList)) {
                return DataStateEnum.AUTO_TAG_COMPLETE_STATE;
            }
        }
        return null;
    }

    /**
     * 标注完成
     *
     * @param stateList 数据集下文件状态的并集
     * @return 数据集状态枚举
     */
    public static DataStateEnum isFinished(List<Integer> stateList) {
        return stateList.contains(FileStateCodeConstant.ANNOTATION_COMPLETE_FILE_STATE) && stateList.size() == NumberConstant.NUMBER_1 ?
                DataStateEnum.ANNOTATION_COMPLETE_STATE : null;
    }

    /**
     * 目标跟踪完成
     * (一定包含目标跟踪完成，可以包含标注完成)
     *
     * @param stateList 数据集下文件状态的并集
     * @return 数据集状态枚举
     */
    public static DataStateEnum isFinishedTrack(List<Integer> stateList) {
        if (stateList.contains(FileStateCodeConstant.ANNOTATION_COMPLETE_FILE_STATE)) {
            stateList.remove(FileStateCodeConstant.ANNOTATION_COMPLETE_FILE_STATE);
        }
        if (stateList.contains(FileStateCodeConstant.TARGET_COMPLETE_FILE_STATE)) {
            stateList.remove(FileStateCodeConstant.TARGET_COMPLETE_FILE_STATE);
        }
        return CollectionUtil.isEmpty(stateList) ? DataStateEnum.TARGET_COMPLETE_STATE : null;
    }


    public static DataStateEnum judge(List<Integer> stateList) {
        if (stateList == null || stateList.isEmpty()) {
            return DataStateEnum.NOT_ANNOTATION_STATE;
        }
        DataStateEnum dataStateEnum = isInit(stateList);
        if (dataStateEnum != null) return dataStateEnum;

        dataStateEnum = isManualAnnotating(stateList);
        if (dataStateEnum != null) return dataStateEnum;

        dataStateEnum = isFinished(stateList);
        if (dataStateEnum != null) return dataStateEnum;

        dataStateEnum = isAutoFinished(stateList);
        if (dataStateEnum != null) return dataStateEnum;

        dataStateEnum = isFinishedTrack(stateList);
        if (dataStateEnum != null) return dataStateEnum;
        return null;
    }
}