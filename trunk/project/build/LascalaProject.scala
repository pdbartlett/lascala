import sbt._

class LascalaProject(info: ProjectInfo) extends ParentProject(info) {

	val lib = project("lib", "lib", new LibProject(_))

	class LibProject(info: ProjectInfo) extends DefaultProject(info) {
		val scalaToolsSnapshots = ScalaToolsSnapshots
		val stVersion = buildScalaVersion match {
			case "2.8.0.RC6" => "1.2-for-scala-2.8.0.RC6-SNAPSHOT"
			case _ => "1.0"
		}
		val scalatest = "org.scalatest" % "scalatest" % stVersion % "test"
	}
}