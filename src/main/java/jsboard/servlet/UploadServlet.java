package jsboard.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import jspBoard.dao.DBConnect;
import jspBoard.dao.JBoardImgDao;
import jspBoard.dto.ImgDto;


@WebServlet("/upload")
public class UploadServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException {
	    //���ε尡 �ִٸ� multipart/form-data���� �Ǵ�
	    if (!ServletFileUpload.isMultipartContent(request)) {
	        throw new ServletException("Content type�� multipart/form-data�� �ƴմϴ�.");
	    }

	    String oFileName = null;
	    String nFileName = null;
	    Long fileSize = 0L;
	    String ext = null;
	    String imnum = Long.toString(System.currentTimeMillis());

	    HttpSession session = request.getSession();
	    String userid = (String) session.getAttribute("userid"); //ȸ�������� session���� �޵���

	    ServletContext context = getServletContext();
	    String realPath = context.getRealPath("/uploads");

	    DiskFileItemFactory factory = new DiskFileItemFactory();
	    ServletFileUpload upload = new ServletFileUpload(factory);

	    try {
	        List<FileItem> items = upload.parseRequest(request);

	        for (FileItem item : items) {
	            if (item.isFormField()) {
	                // �� �ʵ� ó��
	                String fieldName = item.getFieldName();
	                String fieldValue = item.getString();

	                if ("imnum".equals(fieldName) && !fieldValue.isEmpty()) {
	                    imnum = fieldValue;
	                }
	            }
	        }

	        for (FileItem item : items) {
	            if (!item.isFormField()) {
	                // ���� ó��
	                oFileName = item.getName();
	                ext = oFileName.substring(oFileName.lastIndexOf('.'));
	                fileSize = item.getSize();
	                nFileName = "img_" + System.currentTimeMillis() + ext;

	                String filePath = realPath + File.separator + nFileName;
	                File storeFile = new File(filePath);

	                item.write(storeFile);  // ���� ���� �Ϸ�
	            }
	        }

	        // DB ���� �� ����
	        DBConnect db = new DBConnect();
	        Connection conn = db.getConnection();
	        JBoardImgDao idao = new JBoardImgDao(conn);
	        ImgDto idto = new ImgDto();

	        idto.setOfilename(oFileName);
	        idto.setNfilename(nFileName);
	        idto.setExt(ext);
	        idto.setFilesize(fileSize);
	        idto.setImnum(imnum);
	        idto.setUserid(userid);

	        String rs = idao.insertDB(idto);
	        String url = "uploads/" + nFileName;
	        String json = "{\"url\": \"" + url + "\", \"imnum\":\"" + rs + "\"}";

	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write(json);
	        
	    } catch (Exception e) {
	        throw new ServletException(e);
	    }
	}


}
