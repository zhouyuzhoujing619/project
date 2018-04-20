package com;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class WrapFreemarkerHttpServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
	private Configuration cfg;
	
	public void init() throws ServletException {
		try {
			//获取模板所在文件夹参数
			String freemarkerTemplatesFolder = this.getServletContext().getInitParameter("freemarkerTemplatesFolder");
			/* 在整个应用的生命周期中，这个工作你应该只做一次。 */
			/* 创建和调整配置。 */
			cfg = new Configuration(Configuration.getVersion());
			
			//获取模板文件夹路径
			String freemarkerTemplatesUrl = this.getServletContext().getRealPath("/" + freemarkerTemplatesFolder);
			
			//让freemarker从指定文件夹加载模板文件
			cfg.setDirectoryForTemplateLoading(new File(freemarkerTemplatesUrl));
			cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration
					.getVersion()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 输出数据到模板
	 * @param templateFileName 模板名称
	 * @param root 数据模型
	 * @param os 
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws MalformedTemplateNameException 
	 * @throws TemplateNotFoundException 
	 * @throws TemplateException 
	 */
	protected void process(String templateFileName, Map root,Writer os) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		/* 获取或创建模板 */
		Template temp = cfg.getTemplate(templateFileName,"UTF-8");
		/* 将模板和数据模型合并 */
		// 注意：
		// 为了简单起见，这里压制了异常（在方法签名中声明了异常，译者注），而在正式运行的产品中不要这样做。;
		temp.process(root, os);
		os.flush();
	}
	

}