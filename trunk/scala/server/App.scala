package pdbartlett.lascala.scala.server

import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.AbstractHandler

import java.io.IOException

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object App {
  def main(args: Array[String]) {
    val server = new Server(8888)
    server.setHandler(new AppHandler)
    server.start()
    server.join()
  }
}

class AppHandler extends AbstractHandler {
  def handle(target: String, req: Request, servReq: HttpServletRequest, servResp: HttpServletResponse) {
    servResp.setContentType("text/html")
    servResp.getWriter.println("<h1>OK</h1>")
    servResp.setStatus(HttpServletResponse.SC_OK)
    req.setHandled(true)
  }
}
