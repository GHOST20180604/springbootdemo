package com.hanyc.demo.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.hanyc.demo.entity.SheetData;
import com.monitorjbl.xlsx.StreamingReader;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.util.MimeMessageParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hslf.usermodel.HSLFShape;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTextShape;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFShape;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

/**
 * @author ：hanyc
 * @date ：2022/9/28 10:12
 * @description： 读取doc和pdf文档内容工具
 */
public class DocReadUtil {
    public static final Logger logger = LoggerFactory.getLogger(DocReadUtil.class);


    /**
     * pdf文件解析
     *
     * @param inputStream
     */
    public static String getContentDocxPdf(InputStream inputStream) throws IOException {
        String stop = "。,.,？,?,;,；,!,！";
        List<String> list = Arrays.asList(stop.split(","));
        if (inputStream == null) {
            logger.warn("文件流不存在");
            return "";
        }
        PDDocument document = null;
        try {
            document = PDDocument.load(inputStream);
            PDFTextStripper textStripper = new PDFTextStripper();
            String content = textStripper.getText(document);
            String[] split = content.split("\n");
            StringBuilder sb = new StringBuilder();
            for (String o : split) {
                String temp = o.replace("\n", "").replace("\r", "");
                if (StringUtils.isNotEmpty(o) && list.contains(o.substring(o.length() - 1))) {
                    temp = temp + "\n";
                }
                sb.append(temp);
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error("解析文件错误:", e);
            throw e;
        } finally {
            IoUtil.close(document);
            IoUtil.close(inputStream);
        }
    }

    /**
     * 获取正文文件内容，docx方法
     *
     * @param inputStream
     * @return
     */
    public static String getContentDocx(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            logger.warn("文件流不存在");
            return "";
        }
        StringBuilder content = new StringBuilder();
        try {
            // 2007版本，仅支持docx文件处理
            XWPFDocument xwpf = new XWPFDocument(inputStream);
            List<XWPFParagraph> paragraphs = xwpf.getParagraphs();
            List<XWPFTable> tables = xwpf.getTables();
            if (paragraphs != null && paragraphs.size() > 0) {
                for (XWPFParagraph paragraph : paragraphs) {
                    content.append(paragraph.getParagraphText());
                    content.append(StrUtil.LF);
                }
            }
            for (XWPFTable table : tables) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        content.append(cell.getText());
                        content.append(StrUtil.LF);
                    }
                }

            }
        } catch (Exception e) {
            logger.error("docx解析正文异常:", e);
            throw e;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("", e);
            }

        }
        return content.toString();
    }

    /**
     * 获取正文文件内容，doc方法
     *
     * @param inputStream
     * @return
     */
    public static String getContentDoc(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            logger.warn("文件流不存在");
            return "";
        }
        StringBuilder content = new StringBuilder("");
        // 0表示获取正常，1表示获取异常
        InputStream is = null;
        try {
            is = inputStream;
            // 2003版本 仅doc格式文件可处理，docx文件不可处理
            WordExtractor extractor = new WordExtractor(is);
            // 获取段落，段落缩进无法获取，可以在前添加空格填充
            String[] paragraphText = extractor.getParagraphText();
            if (paragraphText != null && paragraphText.length > 0) {
                for (String paragraph : paragraphText) {
                    content.append(paragraph.trim());
                    content.append(StrUtil.LF);
                }
            }
        } catch (Exception e) {
            logger.error("doc解析正文异常:", e);
            throw e;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                logger.error("" + e);
            }
        }
        return content.toString();
    }

    /**
     * 获取正文文件内容，txt方法
     *
     * @param path
     * @return
     */
    public static String getContentTxt(String path) throws IOException {
        BufferedReader reader = null;
        FileReader fileReader = null;
        StringBuilder content = new StringBuilder();
        try {
            fileReader = new FileReader(path);
            reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            IoUtil.close(reader);
            IoUtil.close(fileReader);
        }
        return content.toString();
    }

    /**
     * 获取正文文件内容，txt方法
     *
     * @param filePath
     * @return
     */
    public static String getContentPpts(String filePath) throws IOException {
        FileInputStream fis = null;
        XMLSlideShow pptx = null;
        StringBuilder content = new StringBuilder();
        try {
            fis = new FileInputStream(filePath);
            pptx = new XMLSlideShow(fis);
            for (XSLFSlide slide : pptx.getSlides()) {
//                System.out.println("Slide " + slide.getSlideNumber() + ":");
                for (XSLFShape shape : slide.getShapes()) {
                    if (shape instanceof XSLFTextShape) {
//                        System.out.println(((XSLFTextShape) shape).getText());
                        content.append(((XSLFTextShape) shape).getText());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            IoUtil.close(fis);
            IoUtil.close(pptx);
        }
        return content.toString();
    }

    /**
     * 获取正文文件内容，txt方法
     *
     * @param filePath
     * @return
     */
    public static String getContentPpt(String filePath) throws IOException {
        FileInputStream fis = null;
        HSLFSlideShow ppt = null;
        StringBuilder content = new StringBuilder();
        try {
            fis = new FileInputStream(filePath);
            ppt = new HSLFSlideShow(fis);
            for (HSLFSlide slide : ppt.getSlides()) {
//                System.out.println("Slide " + slide.getSlideNumber() + ":");
                for (HSLFShape shape : slide.getShapes()) {
                    if (shape instanceof HSLFTextShape) {
//                        System.out.println(((HSLFTextShape) shape).getText());
                        content.append(((HSLFTextShape) shape).getText());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            IoUtil.close(fis);
            IoUtil.close(ppt);
        }
        return content.toString();
    }

    /**
     * 获取正文文件内容，Csv方法 (逐行读取,可能会有非一个单元格问题)
     *
     * @param filePath
     * @return
     */
    public static String getContentCsv(String filePath) throws IOException, CsvValidationException {
        FileReader in = null;
        BufferedReader br = null;
        StringBuilder content = new StringBuilder();
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            CSVReader reader = new CSVReaderBuilder(new BufferedReader(new InputStreamReader(is))).withCSVParser(new CSVParserBuilder().withQuoteChar('"').build()).build();
            List<Map<String, String>> list = new ArrayList<>();
//            String[] values = reader.getValues();
            String[] values = reader.readNext();
            Iterator<String[]> iterator = reader.iterator();
            while (iterator.hasNext()) {
                Map<String, String> fieldMap = new LinkedHashMap<>();
                String[] fields = iterator.next();
                for (int j = 0; j < values.length; j++) {
                    fieldMap.put(values[j], fields == null ? "" : String.valueOf(fields[j]));
                    content.append(fields == null ? "" : String.valueOf(fields[j]));
                }
                list.add(fieldMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            IoUtil.close(in);
            IoUtil.close(is);
            IoUtil.close(br);
        }
        return content.toString();
    }

    /**
     * 获取正文文件内容，Mhtml
     *
     * @param filePath
     * @return
     */
//    public static String getContentMhtml(String filePath) throws Exception {
//        FileReader in = null;
//        BufferedReader br = null;
//        StringBuilder content = new StringBuilder();
//        try {
//            String line;
////            in = new FileReader(filePath);
////            br = new BufferedReader(in);
//            Properties props = new Properties();
//            Session session = Session.getDefaultInstance(props, null);
//
//            // 读取 .mhtml 文件
//            File mhtmlFile = new File(filePath);
//            FileInputStream fis = new FileInputStream(mhtmlFile);
//
//            // 创建 MimeMessage 对象
//            MimeMessage message = new MimeMessage(session, fis);
//
//            // 解析 .mhtml 内容
//            MimeMessageParser parser = new MimeMessageParser(message).parse();
//
//            // 获取 HTML 内容
//            String htmlContent = parser.getHtmlContent();
//
//            // 提取纯文本
//            String plainText = htmlContent.replaceAll("<[^>]*>", ""); // 去除 HTML 标签
//            System.out.println("Plain Text Content:\n" + plainText);
//            content.append(plainText);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        } finally {
//            IoUtil.close(in);
//            IoUtil.close(br);
//        }
//        return content.toString();
//    }

    /**
     * 获取正文文件内容，HTML
     *
     * @param filePath
     * @return
     */
    public static String getContentHtml(String filePath) throws IOException {
        FileReader in = null;
        BufferedReader br = null;
        org.jsoup.nodes.Document doc = null;
        StringBuilder content = new StringBuilder();
        try {
            File htmlFile = new File(filePath);

            // 使用 Jsoup 读取文件并解析为 Document 对象
            doc = Jsoup.parse(htmlFile);

            // 获取页面标题
            String title = doc.title();
            content.append(title);
            Elements allElements = doc.getAllElements();
            for (Element element : allElements) {
                content.append(element.text());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            IoUtil.close(in);
            IoUtil.close(br);
        }
        return content.toString();
    }


    /**
     * 获取正文文件内容，wps方法
     *
     * @param path
     * @return
     */
    public static String getContentWps(String path) throws IOException {
        StringBuffer content = new StringBuffer("");
        // 0表示获取正常，1表示获取异常
        InputStream is = null;
        try {
            is = new FileInputStream(new File(path));
            // wps版本word
            HWPFDocument hwpf = new HWPFDocument(is);
            WordExtractor wordExtractor = new WordExtractor(hwpf);
            // 文档文本内容
            String[] paragraphText1 = wordExtractor.getParagraphText();
            if (paragraphText1 != null && paragraphText1.length > 0) {
                for (String paragraph : paragraphText1) {
                    content.append(paragraph.trim());
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            IoUtil.close(is);
        }
        return content.toString();
    }

    /**
     * 获取正文文件内容，xlsx方法
     *
     * @param path
     * @return
     */
    public static Set<String> getContentXlsx(String path, AhoCorasickAutomation aca) throws IOException {
        StringBuffer content = new StringBuffer("");
        // 0表示获取正常，1表示获取异常
        InputStream is = null;
        Workbook workbookRead = null;
//        List<Object> mapList = new LinkedList<>();
        Set<String> setError = new HashSet<>();
        try {
            is = new FileInputStream(path);
            workbookRead = StreamingReader.builder()
                    // 设置缓存的大小
                    .bufferSize(4096)
                    // 缓存行的数量，也就是每次读取多少行到内存中，而不是一下子全都加载进内存
                    .rowCacheSize(100)
                    // 设置要打开的文件
                    .open(is);
            Sheet sheet = workbookRead.getSheetAt(0);
            //遍历所有的行进行文件的读取
            logger.info("开始读取数据xlsx  start: ");

            for (Row row : sheet) {
                int i = 0;
                Map<Integer, Object> oneMap = new HashMap<>();
                for (; i < 18; i++) {
//                    oneMap.put(i, row.getCell(i) == null ? "" : row.getCell(i).getStringCellValue());
//                    content.append(row.getCell(i) == null ? "" : row.getCell(i).getStringCellValue());
                    if (!Objects.isNull(row.getCell(i))) {
                        setError.addAll(aca.find2ListKey(row.getCell(i).getStringCellValue()));
                    }
                }
//                mapList.add(oneMap);
            }
        } catch (Exception e) {
            logger.warn("读取excel数据失败:{}", e.getMessage());
            throw e;
        } finally {
            IoUtil.close(is);
            IoUtil.close(workbookRead);
        }
        return setError;
    }

    /**
     * 获取正文文件内容，wps方法
     *
     * @param path
     * @return
     */
    public static Set<String> getContentXls(String path, AhoCorasickAutomation aca) throws IOException {
        StringBuffer content = new StringBuffer("");
        // 0表示获取正常，1表示获取异常
        InputStream is = null;
        Workbook workbookRead = null;
//        List<Object> mapList = new LinkedList<>();
        Set<String> setError = new HashSet<>();
        try {
            ArrayDataListener arrayDataListener = new ArrayDataListener();
            ZipSecureFile.setMinInflateRatio(0);
            is = new FileInputStream(path);
            EasyExcel.read(is, arrayDataListener).autoTrim(false).ignoreEmptyRow(true).sheet(0).doRead();
            SheetData data = arrayDataListener.getData();
            List<Map<String, String>> datas = data.getDatas();
            for (Map<String, String> map : datas) {
                for (String str : map.values()) {
//                    content.append(str);
                    setError.addAll(aca.find2ListKey(str));
                }
            }
        } catch (Exception e) {
            logger.warn("读取excel数据失败:{}", e.getMessage());
            throw e;
        } finally {
            IoUtil.close(is);
            IoUtil.close(workbookRead);
        }
        return setError;
    }


    /**
     * 获取正文文件内容，wps方法
     *
     * @param filePath
     * @return
     */
    public static String getContentDot(String filePath) throws IOException {
        StringBuffer content = new StringBuffer("");
        // 0表示获取正常，1表示获取异常
        InputStream is = null;
        Workbook workbookRead = null;
        FileInputStream fis = null;
        HWPFDocument document = null;
        WordExtractor extractor = null;
        try {
            fis = new FileInputStream(filePath);
            document = new HWPFDocument(fis);
            extractor = new WordExtractor(document);
            // 提取 .dot 文件中的文本内容
            content.append(extractor.getText());
        } catch (Exception e) {
            throw e;
        } finally {
            IoUtil.close(is);
            IoUtil.close(workbookRead);
            IoUtil.close(fis);
            IoUtil.close(extractor);
            IoUtil.close(document);
        }
        return content.toString();
    }


    public static void main(String[] args) throws Exception {
//        String contentPpts = getContentPpts("D:\\code\\科管二期\\交付文档\\初验交付文档-20241014\\2023年科管系统数据安全管控模块升级与技术服务-服务运维培训手册-20241022.pptx");
//        System.out.println(contentPpts);
//        String contentPpt = getContentPpt("D:\\code\\wpsweb-java-demo\\webofficedemo\\3.ppt");
//        System.out.println(contentPpt);
//        String contentXlsx = getContentXlsx("C:\\Users\\hanyu\\Desktop\\六万条数据-勿动 - 副本.xlsx");
//        String contentXlsx = getContentXls("D:\\code\\科管二期\\权重-10.16.xls");
//        String contentXlsx = getContentWps("D:\\code\\科管二期\\newfile.wps");
//        String contentXlsx = getContentHtml("D:\\code\\科管二期\\newfile.html");
//        String contentXlsx = getContentCsv("D:\\code\\科管二期\\权重-10.16.csv");
        String contentXlsx = getContentDot("D:\\del\\test\\坚决遏制腐败现象蔓延势头.dot");
        System.out.println(contentXlsx);
    }
}
