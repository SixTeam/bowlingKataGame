package fr.kata

import org.scalatest.{Matchers, FunSuite}
import scala.annotation.tailrec

class GameTest extends FunSuite with Matchers {
	val game = new Game()

	val zeroValues = values(20, 0)
	val oneValues = values(20, 1)

	test("gutter game - fall no pins") {
		rollMany(zeroValues, game).score shouldBe 0
	}

	test("each rolls score 1") {
		rollMany(oneValues, game).score shouldBe 20
	}

	test("[spare case] the first roll of the next frame is counted for the previous frame's score") {
		val spareGame = rollSpare.roll(1)

		rollMany(values(17, 0), spareGame).score shouldBe 12
	}

	test("[strike case] the two roll of the next frame are counted for the previous frame's score") {
		val strike = rollStrike.roll(1)
							   .roll(1)

		rollMany(values(16, 0), strike).score shouldBe 14
	}

	test("[after][perfect game] only strike, wheter 12 rolls") {
		rollMany(values(12, 10), game).score shouldBe 300
	}

	private def values(nbValues: Int, value: Int) = (1 to nbValues).map(i => value).toList

	@tailrec
	private def rollMany(rollValues: List[Int], game: Game): Game = rollValues match {
		case x :: xs => rollMany(xs, game.roll(x))
		case Nil => game
	}

	private def rollSpare = game.roll(5).roll(5)
	private def rollStrike = game.roll(10)
}

class Game(rolls: List[Int] = Nil) {
	private val FRAMES = (1 to 10).toList

	def roll(pinfall: Int): Game = new Game(rolls :+ pinfall)

	def score: Int = calculScore(FRAMES, 0, 0)

	@tailrec
	private def calculScore(frames: List[Int], score: Int, frameIndex: Int): Int = frames match {
		case x :: xs => {
			if(isStrike(frameIndex))
				calculScore(xs, score + 10 + strikeBonus(frameIndex), frameIndex+1)

			else if(isSpare(frameIndex))
				calculScore(xs, score + 10 + spareBonus(frameIndex), frameIndex+2)

			else
				calculScore(xs, score + sumOfpinfallInFrame(frameIndex), frameIndex+2)
		}

		case Nil => score
	}

	private def isStrike(frameIndex: Int) = rolls(frameIndex) == 10
	private def isSpare(frameIndex: Int) = rolls(frameIndex) + rolls(frameIndex + 1) == 10

	private def strikeBonus(frameIndex: Int) = rolls(frameIndex+1) + rolls(frameIndex+2)
	private def spareBonus(frameIndex: Int) = rolls(frameIndex + 2)
	private def sumOfpinfallInFrame(frameIndex: Int) = rolls(frameIndex) + rolls(frameIndex + 1)
}

/*def score: Int = {
	var score = 0
	var frameIndex = 0
	for(frame <- 1 to 10) {
		if(isStrike(frameIndex)) {
			score += 10 + strikeBonus(frameIndex)
			frameIndex += 1
		} else if(isSpare(frameIndex)) {
			score += 10 + spareBonus(frameIndex)
			frameIndex += 2
		} else {
			score += sumOfpinfallInFrame(frameIndex)
			frameIndex += 2
		}
	}

	score
}	*/
