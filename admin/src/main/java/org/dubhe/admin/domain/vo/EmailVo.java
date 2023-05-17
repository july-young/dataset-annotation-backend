

package org.dubhe.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description 用户邮箱信息
 * @date 2020-06-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVo implements Serializable {
    private static final long serialVersionUID = -5997222212073811466L;

    private String email;

    private String code;
}
