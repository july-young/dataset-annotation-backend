

package org.dubhe.recycle.enums;

import lombok.Getter;

/**
 * @description 资源回收枚举类

 */
@Getter
public enum RecycleResourceEnum {

    /**
     * 数据集文件回收
     */
    DATASET_RECYCLE_FILE("datasetRecycleFile", "数据集文件回收"),

    /**
     * 医学数据集文件回收
     */
    DATAMEDICINE_RECYCLE_FILE("dataMedicineRecycleFile", "数据集文件回收"),

    /**
     * 数据集版本文件回收
     */
    DATASET_RECYCLE_VERSION_FILE("datasetRecycleVersionFile", "数据集版本文件回收"),

    /**
     * 云端Serving在线服务输入文件回收
     */
    SERVING_RECYCLE_FILE("servingRecycleFile", "云端Serving在线服务文件回收"),
    /**
     * 云端Serving批量服务输入文件回收
     */
    BATCH_SERVING_RECYCLE_FILE("batchServingRecycleFile", "云端Serving批量服务文件回收"),

    /**
     * tadl算法文件回收
     */
    TADL_ALGORITHM_RECYCLE_FILE("tadlAlgorithmRecycleFile", "tadl算法文件回收"),
    /**
     * tadl实验文件回收
     */
    TADL_EXPERIMENT_RECYCLE_FILE("tadlExperimentRecycleFile","tadl实验文件回收"),

    /**
     * 标签组文件回收
     */
    LABEL_GROUP_RECYCLE_FILE("labelGroupRecycleFile", "标签组文件回收"),

    /**
     * 度量文件回收
     */
    MEASURE_RECYCLE_FILE("measureRecycleFile", "度量文件回收"),

    /**
     * 算法文件回收
     */
    ALGORITHM_RECYCLE_FILE("algorithmRecycleFile", "算法文件回收"),

    /**
     * 模型文件回收
     */
    MODEL_RECYCLE_FILE("modelRecycleFile", "模型文件回收"),
    /**
     * 模型优化文件回收
     */
    MODEL_OPT_RECYCLE_FILE("modelOptRecycleFile", "模型优化文件回收"),
    /**
     * 点云文件回收
     */
    PC_DATASET_RECYCLE_FILE("pcDatasetRecycleFile", "点云数据集文件回收"),

    /**
     * 训练任务文件回收
     */
    TRAIN_JOB_RECYCLE_FILE("trainJobRecycleFile", "训练任务文件回收");

    private String className;

    private String message;

    RecycleResourceEnum(String className, String message) {
        this.className = className;
        this.message = message;
    }


}
