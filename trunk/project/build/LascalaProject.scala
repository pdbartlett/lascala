import sbt._

class LascalaProject(info: ProjectInfo) extends ParentProject(info) {

  lazy val lib = project("lib", "lib", new LibProject(_))
  lazy val web = project("web", "web", new WebProject(_), lib)

  trait CommonDeps {
    val scalaToolsSnapshots = ScalaToolsSnapshots
    val stVersion = "1.2-for-scala-2.8.0.final-SNAPSHOT"
    val scalatest = "org.scalatest" % "scalatest" % stVersion % "test"
  }

  class LibProject(info: ProjectInfo) extends DefaultProject(info) with CommonDeps
	
  class WebProject(info: ProjectInfo) extends DefaultWebProject(info) with CommonDeps {
    // Depend on servlet API rather than any particular container at compile time.
    val servletApi = "javax.servlet" % "servlet-api" % "2.5"
	
    // Use Selenium WebDriver and Jetty for testing (SBT issue with v7, so use v6 for now).
    val selenium = "org.seleniumhq.selenium" % "selenium" % "2.0a4" % "test"
    val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.24" % "test"
    //val jetty7 = "org.eclipse.jetty" % "jetty-webapp" % "7.1.4.v20100610" % "test"
  }
}
