package com.hanyc.demo.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：zjx
 * @description：
 * @date ：2023/5/6 14:15
 */

@Data
public class SheetData {
    private Map<Integer,String> header = new LinkedHashMap<>();
    private List<Map<String,String>> datas = new ArrayList<>();


}
