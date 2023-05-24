

package org.dubhe.data.domain.vo;

import org.dubhe.biz.db.base.BaseConvert;
import org.dubhe.data.domain.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @description 文件数据转换

 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileConvert extends BaseConvert<FileVO, File> {

    /**
     * 文件对象转换为对应VO对象
     *
     * @param file       文件对象
     * @param annotation 图片标注信息
     * @return
     */
    @Mapping(source = "annotation", target = "annotation")
    FileVO toDto(File file, String annotation);

}
