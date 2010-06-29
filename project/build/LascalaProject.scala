import sbt._

class LascalaProject(info: ProjectInfo) extends ParentProject(info) {

	val lib = project("lib", "lib", new LibProject(_))

	class LibProject(info: ProjectInfo) extends DefaultProject(info) {
		
		val scalatest = "org.scalatest" % "scalatest" % "1.0" % "test"
	}
}