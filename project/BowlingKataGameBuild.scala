import sbt._
import sbt.Keys._
import Dependencies._

object BowlingKataGameBuild extends Build {

	lazy val root = Project(id = "bowlingKataGame", base = file("."),
		settings = Project.defaultSettings ++ Seq(
			name := "bowlingKataGame",
			version := "0.1-SNAPSHOT",

		    scalaVersion := "2.11.1",

			libraryDependencies ++= tests
		)
	)
}

object Dependencies {
	val tests = Seq(
		"org.scalatest"     %% "scalatest"      % "2.1.7"   % "test"
	)
}