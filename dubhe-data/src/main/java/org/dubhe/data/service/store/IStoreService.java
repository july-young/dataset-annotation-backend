

package org.dubhe.data.service.store;

/**
 * @description 文件存储

 */
public interface IStoreService {

    /**
     * read
     *
     * @param path 文件路径
     * @return 路径
     */
    String read(String path);

    /**
     * write
     *
     * @param path 文件路径
     * @param content 文件目录
     * @return 更新结果
     */
    boolean write(String path, Object content);

    /**
     * delete
     *
     * @param fullFileOrDirPath 全文件路径
     * @return 更新结果
     */
    boolean delete(String fullFileOrDirPath);

}
