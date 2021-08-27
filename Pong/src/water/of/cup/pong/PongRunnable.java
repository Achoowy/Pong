package water.of.cup.pong;

import org.bukkit.scheduler.BukkitRunnable;

public class PongRunnable extends BukkitRunnable {
	private PongGame game;
	private double lastTimeChange;
	

	public PongRunnable(PongGame pongGame) {

		this.game = pongGame;
		lastTimeChange = -1;
	}

	@Override
	public void run() {
		if (lastTimeChange == -1) {
			lastTimeChange = System.currentTimeMillis() / 1000;
			return;
		}
		
		double time = System.currentTimeMillis() / 1000;
		double difference = time - lastTimeChange;
		lastTimeChange = time;
		
		double[] ballPos = game.getBallPos();
		double[] ballVelocity = game.getBallVelocity();
		double[] newPos = {ballPos[0] + ballVelocity[0] * difference, ballPos[1] + ballVelocity[1] * difference};
		
		if (newPos[0] < 0 || newPos[0] > 255) {
			game.score();
			return;
		}
		if (newPos[1] > 125) {
			//bottom bounce
			newPos[1] = 125 - (newPos[1] - 125);
			ballVelocity[1] *= -1;
			
		} else if (newPos[1] < 2) {
			//top bounce
			newPos[1] = 2 - (newPos[1] - 2);
			ballVelocity[1] *= -1;
		}
		
		if (ballVelocity[0] > 0) {
			int paddlePos = 235 - 2;
			//paddle2 intersection
			if (newPos[0] > paddlePos && ballPos[0] < paddlePos) {
				double xdiff = newPos[0] - ballPos[0];
				double ydiff = newPos[1] - ballPos[1];
				
				double yIntercept = ((paddlePos - ballPos[0]) / xdiff) * ydiff + ballPos[1];
				
				int paddleY = game.getPaddleY(2);
				if (yIntercept >= paddleY && yIntercept < paddleY + 16) {
					// bounce
					double paddleInterceptY = (paddleY + 8) - yIntercept;
					double normalizedRelativeIntersectionY = (paddleInterceptY/(8));
					double bounceAngle = normalizedRelativeIntersectionY * 5 * Math.PI / 12;
					double newBallSpeed = - 1.02 * Math.sqrt(ballVelocity[0] * ballVelocity[0] + ballVelocity[1] * ballVelocity[1]);
					
					ballVelocity[0] = newBallSpeed * Math.cos(bounceAngle);
					ballVelocity[1] = - newBallSpeed * -Math.sin(bounceAngle);
					
					double timeElapsed = difference - (paddlePos - ballPos[0]) / xdiff * difference;
					newPos[0] = paddlePos + ballVelocity[0] * timeElapsed;
					newPos[1] = yIntercept + ballVelocity[1] * timeElapsed;
					game.hit();
				}
			}
			
		} else {
			int paddlePos = 21 + 2;
			//paddle1 intersection
			if (newPos[0] < paddlePos && ballPos[0] > paddlePos) {
				double xdiff = newPos[0] - ballPos[0];
				double ydiff = newPos[1] - ballPos[1];
				
				double yIntercept = ((paddlePos - ballPos[0]) / xdiff) * ydiff + ballPos[1];
				
				int paddleY = game.getPaddleY(1);
				if (yIntercept >= paddleY && yIntercept < paddleY + 16) {
					// bounce
					double paddleInterceptY = (paddleY + 8) - yIntercept;
					double normalizedRelativeIntersectionY = (paddleInterceptY/(8));
					double bounceAngle = normalizedRelativeIntersectionY * 5 * Math.PI / 12;
					double newBallSpeed = 1.02 * Math.sqrt(ballVelocity[0] * ballVelocity[0] + ballVelocity[1] * ballVelocity[1]);
					
					ballVelocity[0] = newBallSpeed * Math.cos(bounceAngle);
					ballVelocity[1] = newBallSpeed * -Math.sin(bounceAngle);
					
					double timeElapsed = difference - (paddlePos - ballPos[0]) / xdiff * difference;
					newPos[0] = paddlePos + ballVelocity[0] * timeElapsed;
					newPos[1] = yIntercept + ballVelocity[1] * timeElapsed;
					game.hit();
				}
			}	
		}
		game.setBallPos(newPos);
	}
}
