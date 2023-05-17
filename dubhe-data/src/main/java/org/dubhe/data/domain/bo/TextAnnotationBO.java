

package org.dubhe.data.domain.bo;

import lombok.*;

@Builder
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextAnnotationBO {

    private Long id;

    private Long labelId;

    private Double prediction;
}
