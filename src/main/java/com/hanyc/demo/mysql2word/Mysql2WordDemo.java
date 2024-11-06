package com.hanyc.demo.mysql2word;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.style.RtfParagraphStyle;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.List;

/**
 * @author ：hanyc
 * @date ：2024/4/9 15:52
 * @description：
 */
public class Mysql2WordDemo {

    public static void main(String[] args) {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://172.16.8.46:3306/collabhub?useUnicode=true";
        String user = "root";
        String pwd = "123456";
        createWord(driver, url, user, pwd);
    }

    private static void createWord(String driver, String url, String user, String pwd) {
        //创建一个word文档，等待填写内容
        Document document = new Document(PageSize.A4);
        try {
            //内容填写完，输出文件
            RtfWriter2.getInstance(document, new FileOutputStream(
                    "D:/Tables-posp.doc"));
            //打开 doc
            document.open();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String[]> tables = getTableNames(driver, url, user, pwd);
        //循环遍历输出所有的表的信息
        for (String[] tableInfo : tables) {
            List<Object[]> list = getTableColumns(driver, url, user, pwd, tableInfo[0]);
            document = docAll(document, tableInfo, list);
            System.out.println(tableInfo[0]);
        }
        System.out.println("总数量：" + tables.size());
        //关闭 doc
        document.close();
    }

    /**
     * 获取指定库中表名和表注释
     *
     * @param driver
     * @param url
     * @param user
     * @param pwd
     * @return
     */
    public static List<String[]> getTableNames(String driver, String url, String user, String pwd) {
        Connection conn = null;
        DatabaseMetaData dbmd = null;
        List<String[]> tableNames = new ArrayList<>();
        try {
            conn = getConnections(driver, url, user, pwd);
            dbmd = conn.getMetaData();
            //Oracle的写法
//            ResultSet resultSet = dbmd.getTables(null, getSchema(conn), "%", new String[]{"TABLE"});
            //MySQL和PostgresSQL的写法
            ResultSet resultSet = dbmd.getTables(null, "%", "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                String[] tableInfo = new String[2];
                //表名
                String tableName = resultSet.getString("TABLE_NAME");
                tableInfo[0] = tableName;
                //表注释
                String tableRemark = resultSet.getString("REMARKS");
                tableInfo[1] = tableRemark;
                tableNames.add(tableInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tableNames;
    }

    /**
     * 获取指定表的字段信息（包括字段名称，字段类型，字段长度，备注）
     *
     * @param driver
     * @param url
     * @param user
     * @param pwd
     * @param tableName
     * @return
     */
    public static List<Object[]> getTableColumns(String driver, String url, String user, String pwd, String tableName) {
        List<Object[]> result = new ArrayList();
        Connection conn = null;
        DatabaseMetaData dbmd = null;
        try {
            conn = getConnections(driver, url, user, pwd);
            dbmd = conn.getMetaData();
            ResultSet rs = conn.getMetaData().getColumns(null, "%", tableName, "%");
            while (rs.next()) {
                Object[] objects = new Object[4];
                //字段名称
                String colName = rs.getString("COLUMN_NAME");
                objects[0] = colName;
                //字段类型
                String dbType = rs.getString("TYPE_NAME");
                objects[1] = dbType;
                //字段长度
                int columnSize = rs.getInt("COLUMN_SIZE");
                objects[2] = columnSize;
                //备注
                String remarks = rs.getString("REMARKS");
                if (remarks == null || remarks.equals("")) {
                    remarks = "";
                }
                objects[3] = remarks;
                result.add(objects);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    //获取连接
    public static Connection getConnections(String driver, String url, String user, String pwd) throws Exception {
        Connection conn = null;
        try {
            Properties props = new Properties();
            props.put("remarksReporting", "true");
            props.put("user", user);
            props.put("password", pwd);
            props.put("url", url);
            props.put("table_schema", "collabhub_back");
            //MySQL的要加上下面的配置
//            props.setProperty("remarks", "true"); //设置可以获取remarks信息
//            props.setProperty("useInformationSchema", "true");//设置可以获取tables remarks信息
            Class.forName(driver);
            conn = DriverManager.getConnection(url, props);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return conn;
    }

    //其他数据库不需要这个方法 oracle和db2需要
    public static String getSchema(Connection conn) throws Exception {
        String schema;
        schema = conn.getMetaData().getUserName();
        if ((schema == null) || (schema.length() == 0)) {
            throw new Exception("ORACLE数据库模式不允许为空");
        }
        return schema.toUpperCase().toString();
    }

    /**
     * 输出数据库中所有表的信息
     *
     * @param document  document
     * @param tableInfo 表名和表注释
     * @param list      查询出该表中的信息
     * @return
     */
    public static Document docAll(Document document, String[] tableInfo, List<Object[]> list) {
        try {
            Paragraph ph = new Paragraph();
            Font font = new Font();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(tableInfo[0].toLowerCase());
            if (tableInfo[1] != null && !"".equals(tableInfo[1])) {
                stringBuilder.append("（" + tableInfo[1] + "）");
            }
            Paragraph paragraph = new Paragraph(stringBuilder.toString(), RtfParagraphStyle.STYLE_HEADING_1);
            paragraph.setAlignment(0);
            document.add(paragraph);
            Table table = new Table(4);
            table.setWidth(100);
            table.setBorderWidth(Rectangle.NO_BORDER);
            table.setPadding(0);
            table.setSpacing(0);

            Cell cell = null;
            cell = new Cell("字段名称");
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setHeader(true);
            table.addCell(cell);
            cell = new Cell("字段类型");
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setHeader(true);
            table.addCell(cell);
            cell = new Cell("字段长度");
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setHeader(true);
            table.addCell(cell);
            cell = new Cell("备注");
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setHeader(true);
            table.addCell(cell);

            for (int i = 0; i < list.size(); i++) {
                cell = new Cell(list.get(i)[0].toString().toLowerCase());
                cell.setUseAscender(true);
                cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new Cell(list.get(i)[1].toString().toLowerCase());
                cell.setUseAscender(true);
                cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new Cell(list.get(i)[2].toString());
                cell.setUseAscender(true);
                cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
                table.addCell(cell);

                cell = new Cell(list.get(i)[3].toString());
                cell.setUseAscender(true);
                cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
                table.addCell(cell);
            }
            document.add(table);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return document;
    }
}
