package org.nutz.mvc.view;

import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.lang.Strings;
import org.nutz.mvc.View;

/**
 * 将返回值原样写入resp,通过String.valueOf(obj)<p/>
 * 例外:</p>
 * 1. 如果是byte[]的话,则直接写入流<p/>
 * 2. 如果是直接写入Writer<p/>
 * 3. 如果obj为null,或者方法无返回值,则啥都不写入流</p>
 * <code>@Ok("raw")</code> 则 <code>ContentType=text/plain</code><p/>
 * ContentType 支持几种缩写:<p/>
 * <code>xml</code>代表<code>text/xml</code><p/>
 * <code>html</code>代表<code>text/html</code><p/>
 * <code>htm</code>代表<code>text/html</code><p/>
 * <code>stream</code>代表<code>application/octet-stream</code><p/>
 * <code>json</code>代表<code>application/x-javascript</code><p/>
 * <code>js</code>代表<code>application/x-javascript</code><p/>
 * @author wendal(wendal1985@gmail.com)
 *
 */
public class RawView implements View {
	
	private String contentType;
	
	public RawView(String contentType) {
		if (Strings.isBlank(contentType))
			contentType = "text/plain";
		else if (contentTypeMap.containsKey(contentType.toLowerCase()))
			this.contentType = contentTypeMap.get(contentType.toLowerCase());
		else
			this.contentType = contentType;
	}

	public void render(HttpServletRequest req, HttpServletResponse resp,
			Object obj) throws Throwable {
		resp.setContentType(contentType);
		if (obj == null)
			return;
		if (obj instanceof byte[]) {
			OutputStream os = resp.getOutputStream();
			os.write((byte[])obj);
			os.flush();
		}else if (obj instanceof char[]) {
			Writer writer = resp.getWriter();
			writer.write((char[])obj);
			writer.flush();
		} else {
			Writer writer = resp.getWriter();
			writer.write(String.valueOf(obj));
			writer.flush();
		}
	}

	private static final Map<String, String> contentTypeMap = new HashMap<String, String>();
	
	static {
		contentTypeMap.put("xml", "text/xml");
		contentTypeMap.put("html", "text/html");
		contentTypeMap.put("htm", "text/html");
		contentTypeMap.put("stream", "application/octet-stream");
		contentTypeMap.put("json", "application/x-javascript");
		contentTypeMap.put("js", "application/x-javascript");
	}
}