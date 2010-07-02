import sbt._

class LascalaProject(info: ProjectInfo) extends ParentProject(info) {

	val lib = project("lib", "lib", new LibProject(_))
	val web = project("web", "web", new WebProject(_))

	class LibProject(info: ProjectInfo) extends DefaultProject(info) {
		val scalaToolsSnapshots = ScalaToolsSnapshots
		val stVersion = buildScalaVersion match {
			case "2.8.0.RC6" => "1.2-for-scala-2.8.0.RC6-SNAPSHOT"
			case _ => "1.0"
		}
		val scalatest = "org.scalatest" % "scalatest" % stVersion % "test"
	}
	
	class WebProject(info: ProjectInfo) extends DefaultWebProject(info) {
		// Depend on servlet API rather than any particular container at compile time.
		val servletApi = "javax.servlet" % "servlet-api" % "2.5"
		
		// Use Jetty for testing (SBT issue with v7, so use v6 for now).
		val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.24" % "test"
		//val jetty7 = "org.eclipse.jetty" % "jetty-webapp" % "7.1.4.v20100610" % "test"
	}
}
