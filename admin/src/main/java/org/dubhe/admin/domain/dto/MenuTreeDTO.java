package org.dubhe.admin.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuTreeDTO {
    private Long id;
    private String label;
    private List<MenuTreeDTO> children;
}
