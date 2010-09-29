package pdbartlett.lascala.lib

import scala.collection._

object Jobs {
	val jobs = mutable.Map[String, Job]();
	def defineJob(name: String) = {
		val job = new NamedJob(name)
		jobs(name) = job
		job
	}
	def job(name: String) = jobs(name)
	def pipeline[T](f1: Function0[T], f2: Function1[T, Unit]) = new CompositeJob2[T](f1, f2)
	def pipeline[T1, T2](f1: Function0[T1], f2: Function1[T1, T2], f3: Function1[T2, Unit]) =
	    new CompositeJob3[T1, T2](f1, f2, f3)
	implicit def functionToJob(f: Function0[Unit]) = new FunctionJob(f)
}

trait Job extends Function0[Unit]

object Nop extends Job {
	override def apply() {}
}

class FunctionJob(private val f: Function0[Unit]) extends Job {
	override def apply() { f() }
}

class CompositeJob2[T](private val f1: Function0[T], private val f2: Function1[T, Unit])
    extends Job {
	override def apply() { f2(f1()) }
}

class CompositeJob3[T1, T2](private val f1: Function0[T1], private val f2: Function1[T1, T2],
	  private val f3: Function1[T2, Unit]) extends Job {
	override def apply() { f3(f2(f1())) }
}

class NamedJob(val name: String) extends Job {
	private var job : Job = Nop;
	def apply() { job() }
	def as(job: Job) { this.job = job }
}
