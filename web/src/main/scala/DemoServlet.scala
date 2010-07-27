package pdbartlett.lascala.web

import pdbartlett.lascala.lib._

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class DemoServlet extends HttpServlet {
	override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {
		val content =
			<html>
				<head><title>Demo Servlet</title></head>
				<body><h1>Demo Servlet</h1><p>Just testing... {Demo.answer}</p></body>
			</html>
	resp.getWriter.write(content.toString)
  }
}
