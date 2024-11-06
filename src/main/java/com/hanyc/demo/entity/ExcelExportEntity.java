package com.hanyc.demo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 文件导出实体
 */
@Data
public class ExcelExportEntity {
    /**
     * 文件路径
     */
    @Excel(name = "路径", width = 35)
    private String url;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 风险词详情
     */
    @Excel(name = "风险词", width = 23)
    private String errorItem;
    /**
     * 压缩文件/非压缩文件
     */
    @Excel(name = "文件类型", width = 14)
    private String fileType;


}
