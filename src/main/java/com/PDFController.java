package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

import freemarker.template.TemplateException;
import jdk.internal.util.xml.impl.Input;

/**
 * 注意这里集成的是WrapFreemarkerHttpServlet，该类除了具有HttpServlet的功能<br>
 * 还有初始化freemarker的功能
 * @author Tommy
 *
 */
public class PDFController extends WrapFreemarkerHttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		//初始化freemarker
		super.init();
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/pdf");

		try {
			// 1.freemarker处理

			// 创建数据模型
			Map root = new HashMap();
			root.put("title", "初三一班成绩");
			List<Student> student=new ArrayList<Student>();
			Student t1=new Student(1, "张三", 12, "语文", 46);
			Student t2=new Student(2, "李四", 41, "English", 89);
			Student t3=new Student(3, "王五", 18, "数学", 90);
			Student t4=new Student(4, "赵六", 23, "地理", 57);
			student.add(t1);
			student.add(t2);
			student.add(t3);
			student.add(t4);
			root.put("userList",student);
			//图片路径
			//动态插入图片
			List<String> imgpaths=new ArrayList<String>();
			String filePath=this.getServletConfig().getServletContext().getRealPath("/")+"resource";
			File filehoder=new File(filePath);
			if(filehoder.isDirectory()){
				File[] files=filehoder.listFiles();
				for(File img:files){
					String imgpath=ImgUtil.getImageStr(img.getAbsolutePath());
					imgpaths.add(imgpath);
				}
				
			}
			root.put("imgpaths",imgpaths);
			

			// 指定模板文件，因为在web.xml中已经指定freemarker加载的文件夹 ，所以这里只需要指定文件名即可
			String templateFile = "pdfGenerate.ftl";

			// 输出到/WEB-INF/PDF/pdfFile.html
			String pdfFileUrl = getServletContext().getRealPath(
					"/WEB-INF/PDF/pdfFile.html");
			FileOutputStream fos = new FileOutputStream(new File(pdfFileUrl));
			Writer out = new OutputStreamWriter(fos, "UTF-8");

			// 将数据加载到模板文件
			process(templateFile, root, out);

			// 2.从freemarker产生的html文件生产PDF
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputStream is = new FileInputStream(new File(pdfFileUrl));
			Document doc = builder.parse(is);
			ITextRenderer renderer = new ITextRenderer();

			// 解决中文支持问题
			ITextFontResolver fontResolver = renderer.getFontResolver();
			fontResolver.addFont("simsun.ttc", BaseFont.IDENTITY_H,
					BaseFont.NOT_EMBEDDED);
			
			renderer.setDocument(doc, null);
			renderer.layout();
			OutputStream os = response.getOutputStream();
			renderer.createPDF(os);
			os.close();

		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

	}

}
