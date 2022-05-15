/*
 * Copyright (c) 2009 - 2021, DHBW Mannheim - TIGERs Mannheim
 */

package edu.tigers.sumatra.cam;

import edu.tigers.sumatra.cam.data.CamBall;
import edu.tigers.sumatra.cam.data.CamDetectionFrame;
import edu.tigers.sumatra.cam.data.CamRobot;
import edu.tigers.sumatra.cam.proto.SslVisionDetection;
import edu.tigers.sumatra.ids.BotID;
import edu.tigers.sumatra.ids.ETeamColor;
import edu.tigers.sumatra.math.vector.Vector2;
import edu.tigers.sumatra.math.vector.Vector3;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 */
public class CamDetectionConverter
{
	private long frameId = 0;


	private static CamRobot convertRobot(
			final SslVisionDetection.SSL_DetectionRobot bot,
			final ETeamColor color,
			final long frameId,
			final int camId,
			final long tCapture)
	{
		return new CamRobot(
				bot.getConfidence(),
				Vector2.fromXY(bot.getPixelX(), bot.getPixelY()),
				tCapture,
				camId,
				frameId,
				Vector2.fromXY(bot.getX(), bot.getY()),
				bot.getOrientation(),
				bot.getHeight(),
				BotID.createBotId(bot.getRobotId(), color));
	}


	/**
	 * @param detectionFrame SSL vision frame from a single camera
	 * @return a cam detection frame based on the SSL vision frame
	 */
	public CamDetectionFrame convertDetectionFrame(final SslVisionDetection.SSL_DetectionFrame detectionFrame)
	{
		long localCaptureNs = (long) (detectionFrame.getTCapture() * 1e9);
		long localSentNs = (long) (detectionFrame.getTSent() * 1e9);

		final List<CamBall> balls = new ArrayList<>();
		final List<CamRobot> blues = new ArrayList<>();
		final List<CamRobot> yellows = new ArrayList<>();

		for (final SslVisionDetection.SSL_DetectionRobot bot : detectionFrame.getRobotsBlueList())
		{
			blues.add(convertRobot(bot, ETeamColor.BLUE, frameId, detectionFrame.getCameraId(),
					localCaptureNs));
		}

		// --- process team Yellow ---
		for (final SslVisionDetection.SSL_DetectionRobot bot : detectionFrame.getRobotsYellowList())
		{
			yellows.add(convertRobot(bot, ETeamColor.YELLOW, frameId,
					detectionFrame.getCameraId(),
					localCaptureNs));
		}

		// --- process ball ---
		for (final SslVisionDetection.SSL_DetectionBall ball : detectionFrame.getBallsList())
		{
			balls.add(convertBall(ball, localCaptureNs, detectionFrame.getCameraId(),
					frameId));
		}


		return new CamDetectionFrame(localCaptureNs, localSentNs, detectionFrame.getCameraId(),
				detectionFrame.getFrameNumber(),
				frameId++, balls, yellows, blues);
	}


	private static CamBall convertBall(
			final SslVisionDetection.SSL_DetectionBall ball,
			final long tCapture,
			final int camId,
			final long frameId)
	{
		return new CamBall(
				ball.getConfidence(),
				ball.getArea(),
				Vector3.fromXYZ(ball.getX(), ball.getY(), ball.getZ()),
				Vector2.fromXY(ball.getPixelX(), ball.getPixelY()),
				tCapture,
				camId,
				frameId);
	}

}
