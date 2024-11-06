package com.hanyc.demo.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.StringUtils;
import com.hanyc.demo.entity.SheetData;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ：zjx
 * @description：
 * @date ：2023/5/6 14:14
 */

@Data
public class ArrayDataListener extends AnalysisEventListener<Map<Integer,String>> {
    private SheetData data = new SheetData();
    private Map<Integer,String> headMap;
    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        super.invokeHead(headMap, context);

        for(Integer key : headMap.keySet()){
            CellData cellData = headMap.get(key);
            String value = cellData.getStringValue();
            if(StringUtils.isEmpty(value)){
                continue;
            }
            data.getHeader().put(key,value);
        }
    }


    @Override
    public void invoke(Map<Integer, String> dataMap, AnalysisContext analysisContext) {
        Map<String, String> fieldMap = new LinkedHashMap<>();
        if (CollectionUtils.isEmpty(dataMap)) {
            return;
        }
        for(Integer key : data.getHeader().keySet()){

            String value = dataMap.get(key);
            if(StringUtils.isEmpty(value)){
                fieldMap.put(data.getHeader().get(key),"");
            }else {
                fieldMap.put(data.getHeader().get(key), value);
            }
        }

        data.getDatas().add(fieldMap);

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
