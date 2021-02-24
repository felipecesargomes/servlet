package servlet.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.dao.DaoGenerico;
import servlet.model.Usuario;

@WebServlet("/usuario")
public class UsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nomeUsuario = request.getParameter("nome");
		Usuario u = new Usuario();
		DaoGenerico<Usuario> daoUsuario = new DaoGenerico<>();
		u.setNome(nomeUsuario);
		daoUsuario.cadastrar(u);
		request.setAttribute("nome", nomeUsuario);
		RequestDispatcher rd = request.getRequestDispatcher("/usuariocadastrado.jsp");
		rd.forward(request, response);
	}

}