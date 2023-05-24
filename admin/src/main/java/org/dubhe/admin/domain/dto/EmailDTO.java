

package org.dubhe.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description 邮件DTO

 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {

    @ApiModelProperty(value = "邮箱地址")
    String receiverMailAddress;

    @ApiModelProperty(value = "标题")
    String subject;

    @ApiModelProperty(value = "验证码")
    String code;

    @ApiModelProperty(value = "类型  1 用户注册 2 修改邮箱 3 其他")
    Integer type;

}
