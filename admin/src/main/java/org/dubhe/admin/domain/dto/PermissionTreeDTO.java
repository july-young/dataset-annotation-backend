package org.dubhe.admin.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class PermissionTreeDTO {
    private Long id;
    private String permission;
    private String label;
    private List<PermissionTreeDTO> children;
}
