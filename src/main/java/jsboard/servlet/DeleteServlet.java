package jsboard.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jspBoard.dao.DBConnect;
import jspBoard.dao.JBoardImgDao;


@WebServlet("/delete")
public class DeleteServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileUrl = request.getParameter("fileUrl");
		String filePath = getFilePathFromUrl(fileUrl);
		
		File file = new File(filePath);
		if(file.delete()) {
			response.getWriter().write(1);
		}else {
			response.getWriter().write(0);
		}
	}

	//�����θ� ã�Ƽ� �����ִ� �޼ҵ�
	private String getFilePathFromUrl(String fileUrl) {
		
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		//������
		ServletContext context = getServletContext();
		String realPath = context.getRealPath("/uploads");
		String filePath = realPath + File.separator +  fileName;
		
		
		//db ����
		DBConnect db = new DBConnect();
		Connection conn;
		try {
			conn = db.getConnection();
			JBoardImgDao idao = new JBoardImgDao(conn);
			int rs = idao.deleteDB("nfilename", fileName);
			if(rs>0) {
				System.out.println("�����Ϸ�");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}


		
		return filePath;
	}
	
}